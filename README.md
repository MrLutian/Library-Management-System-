# 高校图书管理系统（Java Web）

本项目是毕业设计开发版本，技术栈：Java 17、JSP、Servlet 6、JDBC、MySQL 8.4、Tomcat 10.1、Maven。

## 当前功能

- 管理员登录与会话拦截
- 首页数据概览
- 图书信息新增、列表、删除（编辑功能待完善）
- 读者信息新增、列表、删除（编辑功能待完善）
- 借阅登记、归还登记、库存数量联动
- MySQL 初始化脚本和演示数据

## 浏览器运行方案（推荐 GitHub Codespaces）

你的电脑不需要安装 Java、Maven、Tomcat、MySQL 或 IDE。运行环境由 Codespaces 云端容器提供。

1. 登录 GitHub，创建一个空的私有仓库。
2. 将本项目文件上传到仓库根目录（可以在 GitHub 网页端使用 Add file / Upload files 上传 ZIP 解压后的文件）。
3. 在仓库中打开 Code → Codespaces → Create codespace on main。
4. Codespaces 根据 `.devcontainer/devcontainer.json` 和 `docker-compose.yml` 自动启动 Java/Tomcat 与 MySQL。首次启动需要等待镜像构建和 Maven 下载依赖。
5. 当 8080 端口提示可用时，点击 Open in Browser。
6. 访问系统后使用演示账号：`admin`，密码：`admin123`。

如果 Codespaces 账户没有可用额度，可能需要启用计费或选择其他在线开发环境；开始前先查看 GitHub 当前的 Codespaces 配额和计费说明。

## 数据库配置

数据库连接通过环境变量读取：`DB_URL`、`DB_USER`、`DB_PASSWORD`。本项目的 Compose 演示配置使用 MySQL 服务名 `db`。本地部署时可按需修改环境变量。

## 注意事项

- 这是开发和教学演示配置，不是生产环境安全配置。演示密码仅用于测试，正式部署前必须修改。
- `database/library.sql` 只会在 MySQL 数据卷首次初始化时自动执行。若要重置演示数据库，需要删除 Compose 数据卷（这会清空数据）。
- 当前源码尚未完成全部功能、编译验证和真实环境测试。请勿将演示数据、测试结果或系统截图写成已经实际验证的事实。后续需要补充编辑、查询、分类管理、输入校验、异常提示和测试记录。
