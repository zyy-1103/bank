package com.bank.mapper;

import com.bank.bean.AddressChart;
import com.bank.bean.ComInfo;
import com.bank.bean.SecResultBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

    @Select("select count(address) as count,address from user where id in (select user_id from result_person where seckill_id=#{seckillId}) group by address")
    List<AddressChart> selectProvinces(String seckillId);
}
