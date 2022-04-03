package com.bank.mapper;

import com.bank.bean.UserBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    /**
     * @return 用户的id
     */
    @Select("select id from user where user=#{user} and password=#{password}")
    Integer isUser(UserBean bean);

    @Update("update user set password=#{s}")
    int update(String s);

}
