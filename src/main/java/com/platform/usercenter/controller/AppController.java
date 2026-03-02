package com.platform.usercenter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.common.result.Result;
import com.platform.usercenter.dto.request.AppCreateRequest;
import com.platform.usercenter.dto.request.AppQuery;
import com.platform.usercenter.dto.request.AppUpdateRequest;
import com.platform.usercenter.dto.response.AppVO;
import com.platform.usercenter.service.IAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 应用管理控制器
 */
@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
public class AppController {

    private final IAppService appService;

    /**
     * 分页查询应用列表
     */
    @GetMapping
    public Result<IPage<AppVO>> getAppPage(AppQuery query) {
        return Result.success(appService.getAppPage(query));
    }

    /**
     * 获取应用详情
     */
    @GetMapping("/{id}")
    public Result<AppVO> getById(@PathVariable Long id) {
        return Result.success(appService.getById(id));
    }

    /**
     * 创建应用
     */
    @PostMapping
    public Result<AppVO> createApp(@Valid @RequestBody AppCreateRequest request) {
        return Result.success(appService.createApp(request));
    }

    /**
     * 更新应用
     */
    @PutMapping("/{id}")
    public Result<AppVO> updateApp(@PathVariable Long id, @Valid @RequestBody AppUpdateRequest request) {
        request.setId(id);
        return Result.success(appService.updateApp(request));
    }

    /**
     * 删除应用
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteApp(@PathVariable Long id) {
        appService.deleteApp(id);
        return Result.success();
    }
}
