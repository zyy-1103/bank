package com.bank.service;

import com.bank.bean.ComInfo;
import com.bank.bean.OverdueRuleBean;
import com.bank.bean.UserBean;
import com.bank.bean.UserRuleBean;
import com.bank.mapper.BackMapper;
import com.bank.mapper.ComInfoMapper;
import com.bank.utils.SM3;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class BackService {
    @Autowired
    RedisAsyncCommands<String, String> commands;
    @Autowired
    BackMapper mapper;
    @Autowired
    ComInfoMapper infoMapper;
    @Autowired
    OverdueRuleBean overdueRuleBean;
    @Autowired
    UserRuleBean userRuleBean;

    public void init() throws ParseException {
        ComInfo notOver = mapper.getNotOver();
        if (notOver != null) {
            commands.set("_start", notOver.getStartTime());
            commands.set("_end", notOver.getEndTime());
            commands.set("_quantity", notOver.getQuantity());
            commands.set("_total", notOver.getTotal());
            commands.set("_price", notOver.getPrice());
            commands.set("_remain", notOver.getTotal());
        }
    }

    public String config(ComInfo info) {
        System.out.println("here");
        info.setId("0");
        int insert = infoMapper.insert(info);
        return String.valueOf(insert);
    }

    public String login(UserBean bean) {
        bean.setPassword(SM3.encrypt(bean.getPassword()));
        return String.valueOf(mapper.isUser(bean.getUser(), bean.getPassword()));
    }

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
        System.out.println(builder);
        return builder.toString();
    }
}
