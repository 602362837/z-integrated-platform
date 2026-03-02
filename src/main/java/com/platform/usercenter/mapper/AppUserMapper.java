package com.platform.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.usercenter.entity.AppUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用用户Mapper接口
 */
@Mapper
public interface AppUserMapper extends BaseMapper<AppUser> {
}
