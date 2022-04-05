package com.bank.mapper;

import com.bank.bean.ComInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BackMapper {
    @Select("select * from com_info where is_over=0 limit 1")
    ComInfo getNotOver();

    @Select("select count(*) from back_user where user=#{user} and password=#{password}")
    Integer isUser(String user,String password);
}
