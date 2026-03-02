package com.platform.usercenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.dto.request.PlatformUserCreateRequest;
import com.platform.usercenter.dto.request.PlatformUserQuery;
import com.platform.usercenter.dto.request.PlatformUserUpdateRequest;
import com.platform.usercenter.dto.response.PlatformUserVO;

/**
 * 平台用户服务接口
 */
public interface IPlatformUserService {

    /**
     * 分页查询用户
     */
    IPage<PlatformUserVO> getUserPage(PlatformUserQuery query);

    /**
     * 获取用户详情
     */
    PlatformUserVO getById(Long id);

    /**
     * 创建用户
     */
    PlatformUserVO createUser(PlatformUserCreateRequest request);

    /**
     * 更新用户
     */
    PlatformUserVO updateUser(Long id, PlatformUserUpdateRequest request);

    /**
     * 删除用户
     */
    void deleteUser(Long id);
}
