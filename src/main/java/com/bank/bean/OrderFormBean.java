package com.bank.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_form")
@ToString
public class OrderFormBean {
    private Integer id;
    private Integer userId;
    private Integer seckillId;
    private Integer finished;
    private String date;
}
