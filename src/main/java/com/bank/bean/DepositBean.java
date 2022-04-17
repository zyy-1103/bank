package com.bank.bean;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "deposit_record")
public class DepositBean {
    private String id;
    private String userId;
    private String amount;
    private String rate;
    private String date;
}
