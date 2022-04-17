package com.bank.mapper;

import com.bank.bean.OrderFormBean;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SeckillMapper extends BaseMapper<OrderFormBean> {

    @Update("update com_info set remain=#{remain} where id=#{id}")
    void update(String remain, String id);

    @Select("select count(*) from order_form where user_id=#{userId} and seckill_id=#{secId} and finished=0")
    int isFinished(Integer userId, Integer secId);

    @Delete("delete from order_form where user_id=#{userId} and seckill_id=#{secId} and finished=0")
    void delUnFinished(Integer userId, Integer secId);

    @Select("select * from order_form where user_id=#{userId} and seckill_id=#{secId} and finished=0")
    OrderFormBean selectUnfinishedAmount(String userId, String secId);

    @Select("select * from order_form where user_id=#{userId} and finished=0")
    List<OrderFormBean> selectAllUnfinished(String userId);

    @Select("select * from order_form where user_id=#{userId} and finished=1")
    List<OrderFormBean> selectAllFinished(String userId);

    @Select("select amount from balance where user_id=#{userId}")
    Double selectBalance(String userId);

//    乐观锁
    @Update("update balance set amount=#{newBalance} " +
            "where user_id=#{userId} and amount=#{oldBalance}")
    int updateBalance(String userId, Double newBalance, Double oldBalance);

    @Select("select remain from com_info where id=#{secId}")
    int selectRemain(String secId);

    //    乐观锁
    @Update("update com_info set remain=#{newRemain} where remain=#{oldRemain} and id=#{secId}")
    int updateRemain(String secId, int newRemain, int oldRemain);

    @Update("update order_form set finished=1 where id=#{orderId}")
    int updateOrder(String orderId);
}
