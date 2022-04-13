package com.bank.service;

import com.alibaba.fastjson.JSONObject;
import com.bank.bean.ComInfo;
import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserBean;
import com.bank.mapper.BackMapper;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.Province;
import com.bank.utils.SM3;
import com.bank.utils.WorkState;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

@Service
public class BackService {

    private static final long PRE_TIME=1000*60*30;

    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;
    @Autowired
    ComInfoMapper infoMapper;
    ComInfo notOver;
    ExecutorService executorService = new ThreadPoolExecutor(
            1,1,3, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public void update(){
        executorService.submit(()->{
            try {
                System.out.println("开始执行");
                int n = Integer.parseInt(commands.get("_remain").get());
                String s = n >= 0 ? String.valueOf(n) : "0";
                mapper.update(s, notOver.getId());
                System.out.println("执行完成");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    public String getProvinces(String id) {
        JSONObject object = new JSONObject();
        Province[] values = Province.values();
        int max=0;
        for (Province i : values) {
            int r = mapper.selectAddr(i.name(), id);
            max = Math.max(max, r);
            object.put(i.name(), r);
        }
        object.put("max", max);
        return object.toJSONString();
    }

    public String getWork(String id){
        WorkState[] values = WorkState.values();
        int[] ints = new int[13];
        for (int i = 0; i < 13; i++) {
            ints[i] = mapper.selectWork(values[i].name(), id);
        }
        JSONObject object = new JSONObject();
        object.put("data", ints);
        return object.toJSONString();
    }

    public String getAges(String seckillId) {
        JSONObject object = new JSONObject();
        Integer[] integers = new Integer[7];
        integers[0] = mapper.selectAge(0, 17, seckillId);
        integers[1] = mapper.selectAge(18, 30, seckillId);
        integers[2] = mapper.selectAge(31, 40, seckillId);
        integers[3] = mapper.selectAge(41, 50, seckillId);
        integers[4] = mapper.selectAge(51, 60, seckillId);
        integers[5] = mapper.selectAge(61, 80, seckillId);
        integers[6] = mapper.selectAge(81, 200, seckillId);
        object.put("data", integers);
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
        notOver = infoMapper.selectLatest();
        long time=0;
        if (notOver == null) {
            return;
        }
        commands.set("_sum", "0");
        commands.set("_start", notOver.getStartTime());
        commands.set("_end", notOver.getEndTime());
        commands.set("_quantity", notOver.getQuantity());
        commands.set("_total", notOver.getTotal());
        commands.set("_price", notOver.getPrice());
        commands.set("_remain", notOver.getQuantity());
        time = format.parse(notOver.getStartTime()).getTime();
//        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
//        service.scheduleAtFixedRate(() -> {
//            try {
//                if (commands.get("_start").get() == null) {
//                    System.out.println("异步更新关闭");
//                    commands.del("_remain");
//                    service.shutdown();
//                } else {
//                    String remain = commands.get("_remain").get();
//                    if (Integer.parseInt(remain) >= 0) {
//                        System.out.println("执行异步更新");
//                        mapper.update(remain, notOver.getId());
//                    }
//                }
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }, time - System.currentTimeMillis(), 5000, TimeUnit.MILLISECONDS);


    }

    //配置秒杀规则，将后台传递的商品信息存入数据库
    public String config(ComInfo info) throws ParseException {
        info.setId("0");
        info.setRemain(info.getQuantity());
        int insert = infoMapper.insert(info);
        Date parse = format.parse(info.getStartTime());
        Date end = format.parse(info.getEndTime());
        long cur = System.currentTimeMillis();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        //秒杀开始时，将随机url存入redis
        service.schedule(() -> {
            commands.set("_url", SM3.getUrl());
            service.shutdown();
        },parse.getTime()-System.currentTimeMillis(), TimeUnit.MILLISECONDS);


        service.schedule(() -> {
//            remain字段在异步更新模块删除
//            commands.del("_remain");
            commands.del("_total");
            commands.del("_quantity");
            commands.del("_start");
            commands.del("_end");
            commands.del("_price");
            commands.del("_url");
            commands.del("~odsql");
            commands.del("~odcnt");
            commands.del("~odope");
        }, end.getTime() - cur, TimeUnit.MILLISECONDS);

        if (parse.getTime()<= cur+PRE_TIME) {
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
            }, parse.getTime() - cur - PRE_TIME, TimeUnit.MILLISECONDS);
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
