package com.platform.usercenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.dto.request.AppUserQuery;
import com.platform.usercenter.dto.request.AppUserSyncRequest;
import com.platform.usercenter.dto.response.AppUserVO;

import java.util.List;

/**
 * 应用用户服务接口
 */
public interface IAppUserService {

    /**
     * 分页查询应用用户
     */
    IPage<AppUserVO> getAppUserPage(AppUserQuery query);

    /**
     * 同步应用用户
     *
     * @param appCode 应用编码
     * @param users   用户列表
     * @return 同步结果
     */
    List<AppUserVO> syncAppUsers(String appCode, List<AppUserSyncRequest> users);

    /**
     * 映射到平台用户
     *
     * @param id             应用用户ID
     * @param platformUserId 平台用户ID
     * @return 应用用户VO
     */
    AppUserVO mapToPlatformUser(Long id, Long platformUserId);

    /**
     * 解除平台用户映射
     *
     * @param id 应用用户ID
     * @return 应用用户VO
     */
    AppUserVO unmapPlatformUser(Long id);

    /**
     * 根据ID获取应用用户
     *
     * @param id 应用用户ID
     * @return 应用用户VO
     */
    AppUserVO getById(Long id);
}
