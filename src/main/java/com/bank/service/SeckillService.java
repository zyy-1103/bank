package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.bean.DepositBean;
import com.bank.bean.OrderFormBean;
import com.bank.mapper.ComInfoMapper;
import com.bank.mapper.DepositMapper;
import com.bank.mapper.SeckillMapper;
import com.bank.utils.CookieUtil;
import com.bank.utils.RedisToBeanUtil;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Service
public class SeckillService {
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    ComInfoMapper mapper;
    @Autowired
    BackService service;
    @Autowired
    SeckillMapper seckillMapper;
    @Autowired
    SpringProducer producer;
    @Autowired
    DepositMapper depositMapper;
    @Autowired
    ThreadPoolExecutor executor;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public String getComInfo(String s) throws ExecutionException, InterruptedException {
        JSONObject jsonObject = JSONObject.parseObject(s);
        String id = jsonObject.getString("id");
        String word = jsonObject.getString("word");
        String cid = commands.get("u" + id).get();

        //登录信息无效或错误
//        word = SM3.encryptWithSalt(id);
        if (cid == null || id == null || word == null || !word.equals(SM3.encryptWithSalt(id))) {
            return null;
        }

        if (commands.get("_start").get() == null) {
            return null;
        }
        ComInfo comInfo = RedisToBeanUtil.getComInfo(commands);
        JSONObject object = new JSONObject();
        object.put("data", comInfo);
        object.put("capable", isCapable(id));
        return object.toJSONString();
    }

    public String getTime() throws ExecutionException, InterruptedException, ParseException {
        String start = commands.get("_start").get();
        if (start== null) {
            return String.valueOf(-1);
        }
        return String.valueOf(dateFormat.parse(start).getTime());
    }

    //判断该用户是否有资格参与秒杀
    public int  isCapable(String id) throws ExecutionException, InterruptedException {
        //用户已标记为1，可以参加秒杀
//        if (commands.get("u" + id).get().equals("1")) {
//            return 1;
//        }
        String s = commands.get("u" + id).get();
        if (!s.equals("0")) {
            return Integer.parseInt(s);
        }
        String sql = commands.get("~odsql").get();
        String cnt = commands.get("~odcnt").get();
        String ope = commands.get("~odope").get();
        if (sql == null || cnt == null || ope == null) {
            commands.set("u" + id, "1");
            return 1;
        }
        int realCnt = mapper.capable(sql, id);
        switch (ope) {
            case ">":
                if (realCnt <= Integer.parseInt(cnt)) {
                    commands.set("u"+id,"1");
                    return 1;
                }
                return 0;
            case "<":
                if(realCnt>=Integer.parseInt(cnt)){
                    commands.set("u" + id, "1");
                    return 1;
                }
                return 0;
            default:
                return 0;
        }
    }

    public String getUrl(String s) throws ExecutionException, InterruptedException {
        JSONObject object = JSONObject.parseObject(s);
        String id = object.getString("id");
        String word = object.getString("word");
        if (id == null || word == null || !word.equals(SM3.encryptWithSalt(id))) {
            return null;
        }
        if ("1".equals(commands.get("u" + id).get())) {
            return commands.get("_url").get();
        }
        return null;
    }

    public String go(HttpServletRequest request, String url) throws ExecutionException, InterruptedException {
        String id = CookieUtil.getCookieValueByName("id", request);
        String word = CookieUtil.getCookieValueByName("word", request);
        word = SM3.encryptWithSalt(id);
        if (word == null || !word.equals(SM3.encryptWithSalt(id)) || commands.get("u"+id).get() == null) {
            return "登录信息无效";
        }
        if (!"1".equals(commands.get("u" + id).get())) {
            if ("2".equals(commands.get("u" + id).get())) {
                return "您已参与过本次秒杀";
            }
            return "您不符合参与本次活动的条件";
        }

//        if (url.equals(commands.get("_url").get()) && Integer.parseInt(commands.get("_sum").get()) <Integer.parseInt(commands.get("_total").get())) {
        if (url.equals(commands.get("_url").get())) {
            if (Integer.parseInt(commands.get("_remain").get()) <= 0) {
                return "已售罄";
            }
            Long aLong = commands.decr("_remain").get();
            if (aLong < 0) {
                return "已售罄";
            } else {
//                秒杀成功

//                生成订单
                OrderFormBean bean = new OrderFormBean(0, Integer.parseInt(id),
                        Integer.parseInt(commands.get("_id").get()), 0, commands.get("_price").get(), dateFormat.format(new Date())
                ,commands.get("_word").get());
//                producer.generateOrder(bean);
                executor.submit(()->{
                    seckillMapper.insert(bean);
                });
//                延迟消息，删除8分钟未支付的订单
                producer.listener(bean);
                commands.incr("_sum");
                commands.set("u" + id, "2");
//                service.update();
                return "秒杀成功";
            }
        }
        return "秒杀尚未开始或已经结束";
    }

    @Transactional
    public String pay(HttpServletRequest request,String s) throws Throwable {
        String orderId = JSONObject.parseObject(s).getString("id");
        String id = CookieUtil.getCookieValueByName("id", request);
        String word = CookieUtil.getCookieValueByName("word", request);

        if (word == null || !word.equals(SM3.encryptWithSalt(id)) || commands.get("u" + id).get() == null) {
            return "登录信息失效";
        }
        int i=0;
        Double oldBalance = seckillMapper.selectBalance(id);
        double price = Double.parseDouble(commands.get("_price").get());
        String secId = commands.get("_id").get();
        if (oldBalance == null || oldBalance < price) {
//            支付失败后，用户可重新发起秒杀
            throw new Exception("余额不足");
        }

        //更新用户个人余额，冲突小
        while (seckillMapper.updateBalance(id, oldBalance - price, oldBalance) == 0) {
//            乐观锁失败时最多重复三次
            oldBalance = seckillMapper.selectBalance(id);
            if (oldBalance < price) {
                throw new Exception("余额不足");
            }
            if (++i == 3) {
                throw new Exception("支付失败");
            }
        }

//        增加存款记录
        DepositBean bean = new DepositBean("0", id, String.valueOf(price),
                commands.get("_rate").get(), dateFormat.format(new Date()));
        depositMapper.insert(bean);
//        支付成功，继续执行

//        如果是支付本次秒杀，则减库存
        if (orderId.equals(commands.get("_id").get())) {
            int oldRemain = seckillMapper.selectRemain(secId);
            if(oldRemain<=0)
                throw new Exception("已售罄");
            i = 0;

//        更新数据库库存，由于可以延迟支付，这里冲突不会很大
            while(seckillMapper.updateRemain(secId, oldRemain - 1, oldRemain)==0){
                oldRemain = seckillMapper.selectRemain(secId);
                if (oldRemain <= 0) {
                    throw new Exception("已售罄");
                }
                if (++i == 3) {
                    throw new Exception("支付失败");
                }
            }
        }

        //更新订单，使该订单标记为已完成
        seckillMapper.updateOrder(orderId);
        return "支付成功";
    }

    public String getOrder(HttpServletRequest request) throws ExecutionException, InterruptedException {
        String id = CookieUtil.getCookieValueByName("id", request);
        String word = CookieUtil.getCookieValueByName("word", request);
        if (word == null || !word.equals(SM3.encryptWithSalt(String.valueOf(id)))||commands.get("u"+id).get()==null) {
            return "登录信息过期";
        }
//        String secId = commands.get("_id").get();
//
//        OrderFormBean bean = seckillMapper.selectUnfinishedAmount(id, secId);
//        if (bean == null) {
//            return "0";
//        }
        JSONObject object = new JSONObject();
        object.put("data", seckillMapper.selectAllUnfinished(id));
        object.put("finished", seckillMapper.selectAllFinished(id));
        return object.toJSONString();
    }
}
