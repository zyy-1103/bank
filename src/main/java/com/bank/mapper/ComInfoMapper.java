package com.bank.mapper;

import com.bank.bean.ComInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ComInfoMapper extends BaseMapper<ComInfo> {

    @Select("select * from com_info where is_over!=1 order by id desc limit 1")
    ComInfo selectLatest();

    @Select("${sql}")
    int capable(String sql, String id);
}
