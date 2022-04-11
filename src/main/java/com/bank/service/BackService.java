package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.AddressChart;
import com.bank.bean.ComInfo;
import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserBean;
import com.bank.mapper.BackMapper;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class BackService {

    private static final long PRE_TIME=1000*60*30;

    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;
    @Autowired
    ComInfoMapper infoMapper;


    public String getProvinces(String seckillId) {
        JSONObject object = new JSONObject();
        object.put("data", mapper.selectProvinces(seckillId));
        return object.toJSONString();
    }

    public String getDataTest(){
        JSONObject object = new JSONObject();
        object.put("data", mapper.selectTest());
        return object.toJSONString();
    }

    public String getAll(){
        JSONObject object = new JSONObject();
        object.put("data", mapper.selectAll());
        return object.toJSONString();
    }

    public void init() throws ParseException {
        //将最新并且没有执行完毕的一条商品信息存入redis
        ComInfo notOver = infoMapper.selectLatest();
        if (notOver != null) {
            commands.set("_start", notOver.getStartTime());
            commands.set("_end", notOver.getEndTime());
            commands.set("_quantity", notOver.getQuantity());
            commands.set("_total", notOver.getTotal());
            commands.set("_price", notOver.getPrice());
            commands.set("_remain", notOver.getTotal());
        }
    }

    //配置秒杀规则，将后台传递的商品信息存入数据库
    public String config(ComInfo info) throws ParseException {
        info.setId("0");
        int insert = infoMapper.insert(info);
        Date parse = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(info.getStartTime());
        long cur = System.currentTimeMillis();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

        //秒杀开始时，将随机url存入redis
        service.schedule(() -> {
            commands.set("~url", SM3.getUrl());
            service.shutdown();
        }, System.currentTimeMillis() - parse.getTime(), TimeUnit.MILLISECONDS);

        if (parse.getTime() + PRE_TIME >= cur) {
            //距离秒杀开始时间小于预热时间（PRE_TIME）
            init();
        }else {
            //定时任务，在距秒杀开始剩余PRE_TIME时执行init，将商品信息读入redis
            service.schedule(() -> {
                try {
                    init();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }, cur - parse.getTime() - PRE_TIME, TimeUnit.MILLISECONDS);
        }
        return String.valueOf(insert);
    }

    public String login(UserBean bean) {
        bean.setPassword(SM3.encrypt(bean.getPassword()));
        return String.valueOf(mapper.isUser(bean.getUser(), bean.getPassword()));
    }

    //将逾期记录规则写入redis
    public String overdue(OverdueRuleBean bean) {
        StringBuilder builder = new StringBuilder();
        if (bean.getStart() == null || bean.getEnd() == null && bean.getCount() == null) {
            return "信息填写错误";
        }
        builder.append("select count(*) from overdue_record where id=#{id} and datediff(agreed_time,'").append(bean.getStart()).append("')>0 and ").append("datediff(agreed_time,'").append(bean.getEnd()).append("')<0 ");
        if (bean.getExCount() != null && bean.getExTime() != null &&
                bean.getExCountOperator() != null && bean.getExTimeOperator() != null) {
            if (bean.getExTimeUnit().equals("day")) {
                builder.append(" and (repayment_date is null or datediff(repayment_date,agreed_time) ").append(bean.getExTimeOperator()).append(" ").append(bean.getExTime()).append(")");
            } else if (bean.getExTimeUnit().equals("month")) {
                builder.append(" and (repayment_date is null or (year(repayment_date)-year(agreed_time))+month(repayment_date)-month(agreed_time)").append(bean.getExTimeOperator()).append(" ").append(bean.getExTime()).append(")");
            }else{

                builder.append(" and (repayment_date is null or year(repayment_date) - year(agreed_time) ").append(bean.getExTimeOperator()).append(" ").append(bean.getExTime()).append(")");
            }
            builder.append(" and amount").append(bean.getOperator()).append(" ").append(bean.getExCount());
        }
        commands.set("~odcnt", bean.getCount());
        commands.set("~odope", bean.getOperator());
        commands.set("~odsql", builder.toString());
        return "1";
    }

}
