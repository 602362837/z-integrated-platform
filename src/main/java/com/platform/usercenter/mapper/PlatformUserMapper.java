package com.platform.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.usercenter.entity.PlatformUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台用户Mapper接口
 */
@Mapper
public interface PlatformUserMapper extends BaseMapper<PlatformUser> {
}
