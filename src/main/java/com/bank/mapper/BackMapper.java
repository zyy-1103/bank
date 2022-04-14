package com.bank.mapper;

import com.bank.bean.AddressChart;
import com.bank.bean.ComInfo;
import com.bank.bean.SecResultBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BackMapper {
//    @Select("select * from com_info where is_over=0 limit 1")
//    ComInfo getNotOver();

    @Select("select count(*) from back_user where user=#{user} and password=#{password}")
    Integer isUser(String user, String password);

    @Select("select * from result_all limit 40")
    List<SecResultBean> selectTest();

    @Select("select * from result_all")
    List<SecResultBean> selectAll();

    @Update("update com_info set remain=#{remain} where id=#{id}")
    void update(String remain,String id);

    @Select("select count(*) from order_form where user_id in(select id from user where age>=#{l} and age<=#{r}) and seckill_id=#{id}")
    Integer selectAge(int l, int r,String id);

    @Select("select count(*) from order_form where user_id in(select id from user where work_state=#{s}) and seckill_id=#{id}")
    Integer selectWork(String s, String id);

    @Select("select count(*) from order_form where user_id in(select id from user where address=#{s}) and seckill_id=#{id}")
    Integer selectAddr(String s, String id);

    @Select("select * from result_all limit 30")
    List<SecResultBean> selectRAll();
}
