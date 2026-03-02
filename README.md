# 一体化平台用户管理系统

用户中心服务，为多应用提供统一的用户管理能力。

## 技术栈

### 后端
- **框架**: Spring Boot 3.2.5
- **ORM**: MyBatis Plus 3.5.7
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.0
- **安全**: Spring Security (BCrypt密码加密)
- **Java版本**: 21

### 前端
- **框架**: Vue 3
- **构建工具**: Vite 7.3.1

## 快速开始

### 1. Docker启动（MySQL + Redis）

```bash
# 启动MySQL和Redis容器
docker-compose up -d

# 查看容器状态
docker-compose ps
```

容器配置：
- MySQL: 端口 3306，用户名 root，密码 root123456
- Redis: 端口 6379（无密码）

### 2. 后端启动

```bash
# 编译项目
mvn clean package -DskipTests

# 运行项目
java -jar target/user-center-1.0.0-SNAPSHOT.jar
```

或使用IDE直接运行 `UserCenterApplication.java`

后端默认端口：**8080**

### 3. 前端启动

```bash
# 进入前端目录
cd z-integrated-platform-web

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端默认端口：**5173**

## 项目结构

```
z-integrated-platform/
├── src/main/java/com/platform/
│   └── usercenter/
│       ├── config/          # 配置类
│       ├── controller/      # 控制器
│       ├── entity/          # 实体类
│       ├── mapper/          # MyBatis映射器
│       ├── service/         # 服务层
│       ├── dto/             # 数据传输对象
│       └── common/          # 公共组件
├── src/main/resources/
│   └── application.yml      # 应用配置
├── sql/
│   └── init.sql             # 数据库初始化脚本
├── docker-compose.yml       # Docker配置
├── pom.xml                  # Maven配置
└── z-integrated-platform-web/  # 前端项目
    ├── src/
    │   └── main.js          # 入口文件
    └── package.json         # NPM配置
```

## API接口说明

Base URL: `http://localhost:8080/api/v1`

### 应用管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页查询应用 | GET | /apps | 分页获取应用列表 |
| 获取应用详情 | GET | /apps/{id} | 获取指定应用 |
| 创建应用 | POST | /apps | 创建新应用 |
| 更新应用 | PUT | /apps/{id} | 更新应用信息 |
| 删除应用 | DELETE | /apps/{id} | 删除应用 |

### 组织管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 获取组织树 | GET | /orgs/tree | 获取组织树形列表 |
| 获取组织详情 | GET | /orgs/{id} | 获取指定组织 |
| 创建组织 | POST | /orgs | 创建新组织 |
| 更新组织 | PUT | /orgs/{id} | 更新组织信息 |
| 删除组织 | DELETE | /orgs/{id} | 删除组织 |

### 平台用户管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页查询用户 | GET | /platform-users | 分页获取用户列表 |
| 获取用户详情 | GET | /platform-users/{id} | 获取指定用户 |
| 创建用户 | POST | /platform-users | 创建新用户 |
| 更新用户 | PUT | /platform-users/{id} | 更新用户信息 |
| 删除用户 | DELETE | /platform-users/{id} | 删除用户 |

### 应用用户管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 分页查询用户 | GET | /app-users | 分页获取应用用户列表 |
| 获取用户详情 | GET | /app-users/{id} | 获取指定用户 |
| 同步应用用户 | POST | /app-users/sync | 批量同步应用用户 |
| 映射平台用户 | POST | /app-users/{id}/map | 映射到平台用户 |
| 解除映射 | DELETE | /app-users/{id}/map | 解除平台用户映射 |

### 通用响应格式

```json
// 成功响应
{
  "code": 200,
  "message": "success",
  "data": {}
}

// 分页响应
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

## 默认账号

- 用户名: admin
- 密码: admin123
