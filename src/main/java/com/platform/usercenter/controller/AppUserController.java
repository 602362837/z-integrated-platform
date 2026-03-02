package com.platform.usercenter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.common.Result;
import com.platform.usercenter.dto.request.AppUserQuery;
import com.platform.usercenter.dto.request.AppUserSyncRequest;
import com.platform.usercenter.dto.response.AppUserVO;
import com.platform.usercenter.service.IAppUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应用用户管理Controller
 */
@RestController
@RequestMapping("/api/v1/app-users")
public class AppUserController {

    private final IAppUserService appUserService;

    public AppUserController(IAppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * 分页查询应用用户
     */
    @GetMapping
    public Result<IPage<AppUserVO>> getAppUserPage(AppUserQuery query) {
        return Result.success(appUserService.getAppUserPage(query));
    }

    /**
     * 获取应用用户详情
     */
    @GetMapping("/{id}")
    public Result<AppUserVO> getById(@PathVariable Long id) {
        return Result.success(appUserService.getById(id));
    }

    /**
     * 同步应用用户
     */
    @PostMapping("/sync")
    public Result<List<AppUserVO>> syncAppUsers(
            @RequestParam @NotBlank(message = "应用编码不能为空") String appCode,
            @Valid @RequestBody List<AppUserSyncRequest> users) {
        return Result.success(appUserService.syncAppUsers(appCode, users));
    }

    /**
     * 映射到平台用户
     */
    @PostMapping("/{id}/map")
    public Result<AppUserVO> mapToPlatformUser(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        Long platformUserId = request.get("platformUserId");
        if (platformUserId == null) {
            return Result.error("平台用户ID不能为空");
        }
        return Result.success(appUserService.mapToPlatformUser(id, platformUserId));
    }

    /**
     * 解除平台用户映射
     */
    @DeleteMapping("/{id}/map")
    public Result<AppUserVO> unmapPlatformUser(@PathVariable Long id) {
        return Result.success(appUserService.unmapPlatformUser(id));
    }
}
