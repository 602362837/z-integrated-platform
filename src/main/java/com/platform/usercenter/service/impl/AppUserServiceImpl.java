package com.platform.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.usercenter.dto.request.AppUserQuery;
import com.platform.usercenter.dto.request.AppUserSyncRequest;
import com.platform.usercenter.dto.response.AppUserVO;
import com.platform.usercenter.entity.AppUser;
import com.platform.usercenter.entity.PlatformUser;
import com.platform.usercenter.mapper.AppUserMapper;
import com.platform.usercenter.mapper.PlatformUserMapper;
import com.platform.usercenter.service.IAppUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用用户服务实现类
 */
@Service
public class AppUserServiceImpl implements IAppUserService {

    private final AppUserMapper appUserMapper;
    private final PlatformUserMapper platformUserMapper;

    public AppUserServiceImpl(AppUserMapper appUserMapper, PlatformUserMapper platformUserMapper) {
        this.appUserMapper = appUserMapper;
        this.platformUserMapper = platformUserMapper;
    }

    @Override
    public IPage<AppUserVO> getAppUserPage(AppUserQuery query) {
        Page<AppUser> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
        if (query.getAppCode() != null && !query.getAppCode().isEmpty()) {
            wrapper.eq(AppUser::getAppCode, query.getAppCode());
        }
        if (query.getUsername() != null && !query.getUsername().isEmpty()) {
            wrapper.like(AppUser::getUsername, query.getUsername());
        }
        if (query.getNickname() != null && !query.getNickname().isEmpty()) {
            wrapper.like(AppUser::getNickname, query.getNickname());
        }
        if (query.getEmail() != null && !query.getEmail().isEmpty()) {
            wrapper.like(AppUser::getEmail, query.getEmail());
        }
        if (query.getPhone() != null && !query.getPhone().isEmpty()) {
            wrapper.like(AppUser::getPhone, query.getPhone());
        }
        if (query.getHasPlatformUser() != null) {
            if (query.getHasPlatformUser()) {
                wrapper.isNotNull(AppUser::getPlatformUserId);
            } else {
                wrapper.isNull(AppUser::getPlatformUserId);
            }
        }

        wrapper.orderByDesc(AppUser::getCreateTime);

        IPage<AppUser> appUserPage = appUserMapper.selectPage(page, wrapper);

        // 查询平台用户信息
        List<AppUser> appUsers = appUserPage.getRecords();
        List<Long> platformUserIds = appUsers.stream()
                .map(AppUser::getPlatformUserId)
                .filter(id -> id != null)
                .collect(Collectors.toList());

        Map<Long, PlatformUser> platformUserMap = null;
        if (!platformUserIds.isEmpty()) {
            List<PlatformUser> platformUsers = platformUserMapper.selectBatchIds(platformUserIds);
            platformUserMap = platformUsers.stream()
                    .collect(Collectors.toMap(PlatformUser::getId, p -> p));
        }

        // 转换为VO
        List<AppUserVO> voList = new ArrayList<>();
        for (AppUser appUser : appUsers) {
            AppUserVO vo = convertToVO(appUser);
            if (appUser.getPlatformUserId() != null && platformUserMap != null) {
                PlatformUser platformUser = platformUserMap.get(appUser.getPlatformUserId());
                if (platformUser != null) {
                    vo.setPlatformUserCode(platformUser.getCode());
                    vo.setPlatformUsername(platformUser.getUsername());
                }
            }
            voList.add(vo);
        }

        Page<AppUserVO> voPage = new Page<>(appUserPage.getCurrent(), appUserPage.getSize(), appUserPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional
    public List<AppUserVO> syncAppUsers(String appCode, List<AppUserSyncRequest> users) {
        List<AppUserVO> result = new ArrayList<>();

        for (AppUserSyncRequest request : users) {
            // 检查是否已存在
            LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AppUser::getAppCode, appCode)
                   .eq(AppUser::getUserCode, request.getUserCode());
            AppUser appUser = appUserMapper.selectOne(wrapper);

            LocalDateTime now = LocalDateTime.now();

            if (appUser == null) {
                // 新建应用用户，并自动创建平台用户
                appUser = new AppUser();
                appUser.setAppCode(appCode);
                appUser.setUserCode(request.getUserCode());
                appUser.setUsername(request.getUsername());
                appUser.setNickname(request.getNickname());
                appUser.setEmail(request.getEmail());
                appUser.setPhone(request.getPhone());
                appUser.setSyncTime(now);
                appUserMapper.insert(appUser);

                // 自动创建平台用户
                PlatformUser platformUser = new PlatformUser();
                platformUser.setCode(appCode + "_" + request.getUserCode());
                platformUser.setUsername(request.getUsername());
                platformUser.setNickname(request.getNickname());
                platformUser.setEmail(request.getEmail());
                platformUser.setPhone(request.getPhone());
                platformUser.setPassword("$2a$10$DefaultPwd123"); // 默认密码
                platformUser.setStatus(1);
                platformUserMapper.insert(platformUser);

                // 关联平台用户
                appUser.setPlatformUserId(platformUser.getId());
                appUserMapper.updateById(appUser);

                result.add(convertToVOWithPlatformUser(appUser, platformUser));
            } else {
                // 更新应用用户信息
                appUser.setUsername(request.getUsername());
                appUser.setNickname(request.getNickname());
                appUser.setEmail(request.getEmail());
                appUser.setPhone(request.getPhone());
                appUser.setSyncTime(now);
                appUserMapper.updateById(appUser);

                // 如果已有平台用户关联，同步更新平台用户信息
                if (appUser.getPlatformUserId() != null) {
                    PlatformUser platformUser = platformUserMapper.selectById(appUser.getPlatformUserId());
                    if (platformUser != null) {
                        platformUser.setUsername(request.getUsername());
                        platformUser.setNickname(request.getNickname());
                        platformUser.setEmail(request.getEmail());
                        platformUser.setPhone(request.getPhone());
                        platformUserMapper.updateById(platformUser);
                        result.add(convertToVOWithPlatformUser(appUser, platformUser));
                    } else {
                        result.add(convertToVO(appUser));
                    }
                } else {
                    result.add(convertToVO(appUser));
                }
            }
        }

        return result;
    }

    @Override
    @Transactional
    public AppUserVO mapToPlatformUser(Long id, Long platformUserId) {
        AppUser appUser = appUserMapper.selectById(id);
        if (appUser == null) {
            throw new RuntimeException("应用用户不存在");
        }

        PlatformUser platformUser = platformUserMapper.selectById(platformUserId);
        if (platformUser == null) {
            throw new RuntimeException("平台用户不存在");
        }

        // 检查是否已被其他应用用户映射
        LambdaQueryWrapper<AppUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppUser::getPlatformUserId, platformUserId);
        Long count = appUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("该平台用户已被其他应用用户映射");
        }

        appUser.setPlatformUserId(platformUserId);
        appUserMapper.updateById(appUser);

        return convertToVOWithPlatformUser(appUser, platformUser);
    }

    @Override
    @Transactional
    public AppUserVO unmapPlatformUser(Long id) {
        AppUser appUser = appUserMapper.selectById(id);
        if (appUser == null) {
            throw new RuntimeException("应用用户不存在");
        }

        appUser.setPlatformUserId(null);
        appUserMapper.updateById(appUser);

        return convertToVO(appUser);
    }

    @Override
    public AppUserVO getById(Long id) {
        AppUser appUser = appUserMapper.selectById(id);
        if (appUser == null) {
            return null;
        }

        AppUserVO vo = convertToVO(appUser);

        if (appUser.getPlatformUserId() != null) {
            PlatformUser platformUser = platformUserMapper.selectById(appUser.getPlatformUserId());
            if (platformUser != null) {
                vo.setPlatformUserCode(platformUser.getCode());
                vo.setPlatformUsername(platformUser.getUsername());
            }
        }

        return vo;
    }

    /**
     * 转换为VO
     */
    private AppUserVO convertToVO(AppUser appUser) {
        AppUserVO vo = new AppUserVO();
        BeanUtils.copyProperties(appUser, vo);
        return vo;
    }

    /**
     * 转换为VO(包含平台用户信息)
     */
    private AppUserVO convertToVOWithPlatformUser(AppUser appUser, PlatformUser platformUser) {
        AppUserVO vo = convertToVO(appUser);
        if (platformUser != null) {
            vo.setPlatformUserId(platformUser.getId());
            vo.setPlatformUserCode(platformUser.getCode());
            vo.setPlatformUsername(platformUser.getUsername());
        }
        return vo;
    }
}
