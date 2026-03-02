package com.platform.usercenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.dto.request.AppCreateRequest;
import com.platform.usercenter.dto.request.AppQuery;
import com.platform.usercenter.dto.request.AppUpdateRequest;
import com.platform.usercenter.dto.response.AppVO;

/**
 * 应用服务接口
 */
public interface IAppService {

    /**
     * 分页查询应用
     */
    IPage<AppVO> getAppPage(AppQuery query);

    /**
     * 获取应用详情
     */
    AppVO getById(Long id);

    /**
     * 创建应用
     */
    AppVO createApp(AppCreateRequest request);

    /**
     * 更新应用
     */
    AppVO updateApp(AppUpdateRequest request);

    /**
     * 删除应用
     */
    void deleteApp(Long id);
}
