package com.bank.mapper;

import com.bank.bean.UserBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserBean> {
    /**
     * @return 用户的id
     */
    @Select("select id from user where user=#{user} and password=#{password}")
    Integer isUser(UserBean bean);

    @Update("update user set password=#{s}")
    Integer update(String s);

    @Insert("insert into order_form values(#{uid},#{sid})")
    void insertRP(int uid, int sid);
}
