# 🖥️ 智慧农联节点部署推荐系统 · 前端

本目录为系统的 **Vue 前端**，与后端 **Spring Boot** 配合，采用前后端分离方式开发。主要技术包括 Vue.js、Element UI、Axios、HTML5 Canvas（节点部署可视化）。整体项目说明见仓库根目录 [`README.md`](../README.md)。

浏览器访问：http://localhost:80

---

## 🛠️ 开发

```bash
# 进入前端目录
cd ruoyi-ui

# 安装依赖（建议使用国内镜像，避免直接使用 cnpm）
npm install --registry=https://registry.npmmirror.com

# Node 版本高于 16 时（PowerShell）
$env:NODE_OPTIONS="--openssl-legacy-provider"

# 启动开发服务（需同时启动 Spring Boot 后端）
npm run dev
```

如需图表能力，可执行：`npm install echarts --save`

---

## 📦 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```
