# PKI_CA_Backend

基于 **Spring Boot** 的简化 PKI / CA（证书颁发机构）后端系统，实现用户认证、证书签发、证书吊销（CRL）与操作审计等核心功能，适用于课程设计、实验教学与原型验证场景。

---

## 一、项目简介

本项目实现了一个基础的 **PKI / CA 后端服务**，主要功能包括：

- 用户注册与身份认证（JWT + 挑战应答）
- X.509 数字证书的申请、签发与查询
- 证书吊销与 CRL 管理
- 操作日志审计
- 基于 Spring Security 的接口访问控制
- 基于 BouncyCastle 的密码学实现

项目采用 **分层架构设计**，结构清晰，便于扩展与前后端分离部署。

---

## 二、技术栈

| 技术 | 说明 |
|----|----|
| Java | JDK 17 |
| Spring Boot | 后端主框架 |
| Spring Security | 认证与鉴权 |
| JWT | 无状态身份认证 |
| BouncyCastle | 密码学库 |
| Maven | 构建与依赖管理 |
| JPA / Hibernate | 数据访问层 |

---

## 三、项目目录结构

```text
PKI_CA_Backend
├── pom.xml
├── mvnw / mvnw.cmd
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.algorithm.pki_ca_backend
│   │   │       ├── controller
│   │   │       ├── service
│   │   │       ├── repository
│   │   │       ├── entity
│   │   │       ├── dto
│   │   │       └── util
│   │   └── resources
│   │       ├── application.properties
│   │       └── ca
│   │           ├── ca.cert.pem
│   │           └── ca.key.pem
│   └── test
│       └── java
└── target
```

---

## 四、系统架构说明

系统采用典型的分层架构：

```
Controller → Service → Repository → Database
```

- **Controller**：对外提供 REST API
- **Service**：核心业务逻辑
- **Repository**：数据库访问
- **Util**：密码学与工具类

---

## 五、核心功能模块

### 1. 用户与认证模块
- JWT 无状态认证
- 登录挑战-应答机制
- Spring Security 统一鉴权

### 2. 证书管理模块
- X.509 证书生成与签发
- 证书信息存储与查询

### 3. CRL 管理模块
- 证书吊销
- 吊销列表维护与查询

### 4. 操作审计模块
- 关键操作日志记录
- 支持安全审计

---

## 六、运行方式

### 环境要求
- JDK 17
- Maven 3.8+
- 数据库（按配置文件）

### 启动项目

```bash
mvn spring-boot:run
```

或直接运行：

```
PkiCaBackendApplication.java
```

---

## 七、注意事项

- `resources/ca` 中的 CA 私钥仅用于教学实验
- 不适合直接用于生产环境
- 实际部署需配合 HSM / KMS 等安全措施

---

## 八、适用场景

- 信息安全 / 密码学课程设计
- PKI / CA 原理教学
- 安全系统原型验证

---

## 九、许可证

本项目仅用于学习与教学目的。
