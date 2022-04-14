package com.bank.mapper;

import com.bank.bean.OrderFormBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SeckillMapper extends BaseMapper<OrderFormBean> {

    @Update("update com_info set remain=#{remain} where id=#{id}")
    void update(String remain, String id);

    @Select("select count(*) from order_form where user_id=#{userId} and seckill_id=#{secId} and finished=0")
    int isFinished(Integer userId, Integer secId);

    @Delete("delete from order_form where user_id=#{userId} and seckill_id=#{secId} and finished=0")
    void delUnFinished(Integer userId, Integer secId);
}
