package com.platform.usercenter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.usercenter.common.Result;
import com.platform.usercenter.dto.request.PlatformUserCreateRequest;
import com.platform.usercenter.dto.request.PlatformUserQuery;
import com.platform.usercenter.dto.request.PlatformUserUpdateRequest;
import com.platform.usercenter.dto.response.PlatformUserVO;
import com.platform.usercenter.service.IPlatformUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 平台用户管理Controller
 */
@RestController
@RequestMapping("/api/v1/platform-users")
public class PlatformUserController {

    private final IPlatformUserService platformUserService;

    public PlatformUserController(IPlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    public Result<IPage<PlatformUserVO>> getUserPage(PlatformUserQuery query) {
        return Result.success(platformUserService.getUserPage(query));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<PlatformUserVO> getById(@PathVariable Long id) {
        return Result.success(platformUserService.getById(id));
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<PlatformUserVO> createUser(@Valid @RequestBody PlatformUserCreateRequest request) {
        return Result.success(platformUserService.createUser(request));
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<PlatformUserVO> updateUser(@PathVariable Long id, @Valid @RequestBody PlatformUserUpdateRequest request) {
        return Result.success(platformUserService.updateUser(id, request));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        platformUserService.deleteUser(id);
        return Result.success();
    }
}
