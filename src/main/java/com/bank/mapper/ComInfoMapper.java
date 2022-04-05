package com.bank.mapper;

import com.bank.bean.ComInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ComInfoMapper extends BaseMapper<ComInfo> {

}
