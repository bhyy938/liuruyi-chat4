# 刘茹依 — Android 聊天 App

一个基于 DeepSeek API 的单人文字聊天 App，人设是"嘴贫但走心、喜欢你但嘴硬"的内蒙高三女生 **刘茹依**（App 名 liuruyi）。

> 交付物为 **Android Studio 工程源码**。本机无 JDK/Android SDK，无法本地打包 APK，用下面的 A/B/C 任一方式打包即可。

## 技术栈
- Kotlin 2.0.21 + Jetpack Compose（BOM 2024.09.03，Material3）
- OkHttp 4.12.0、kotlinx-serialization-json 1.7.3、kotlinx-coroutines-android 1.9.0
- navigation-compose 2.8.1、lifecycle-viewmodel-compose 2.8.6
- Gradle 8.7 + AGP 8.6.1，compileSdk/targetSdk 35，minSdk 26

## 运行 / 打包（三选一）
- **A. 云端打包（推荐）**：把整个文件夹推到 GitHub 仓库（已内置 .github/workflows/build-apk.yml），Actions 自动编译，产物在 Artifacts 里下载 app-debug.apk。
- **B. 本地一键打包**：装了 JDK 17 + Android SDK 的 Windows 电脑上双击 build_apk.bat，APK 在 app/build/outputs/apk/debug/。
- **C. Android Studio**：打开项目 → Build → Build APK(s)。

## 配置 API Key
1. 首次进入后到「设置」页，粘贴 DeepSeek API Key（sk-...）。
2. Key 用 Android Keystore（AES/GCM）加密存本机，不进聊天文件、不进日志。
3. 模型名默认 deepseek-flash，在 Config.kt 里可改。

## 已实现功能（当前版）
- **后台整段生成、逐条气泡弹出**：不显示逐字打字过程；一次回复拆成 2~4 条短消息逐条弹出，短句约 0.5~1.5 秒，像微信实时收到消息。
- **彻底消灭 null**：接口返回空/失败时静默或给自然短句，绝不渲染 null/undefined；历史 null 消息展示前过滤。
- **单会话、微信式界面**：只有一个联系人「刘茹依」，无「+」新建入口，不显示 token/模型名等 AI 痕迹，强制浅色主题。
- **人格**：基于 156 页真实聊天记录提炼，写在 Prompts.kt；含聊天风格、关系态度（满眼都是你、认识两年、暧昧分寸）、真实对白 few-shot 样本。
- **关系/好感度演化**：亲密度随对话增长（初识→熟悉→熟络→亲密→心照不宣），阶段决定语气亲疏，动态注入。
- **全局长期记忆 + 会话摘要**：软阈值 45000 token（触发摘要）、硬阈值 70000（旧消息摘要压缩续聊，不弹"开新窗口"）。
- **主动消息**：每天最多 5 条、每次打开最多 1 条，支持 SKIP。
- **其它**：消息引用回溯、撤回会话+撤销、会话内搜索、单条复制/删除、手动头像、称呼演化、单向图片发送、设备 TTS、网络天气/日期。

## 提示词
三段提示词（附录 A 系统人设 / B 摘要 / C 主动消息）写在 Prompts.kt。附录 A 已按《人格设定-prompt.md》重写，并补回"满眼都是你 / 认识两年 / 暧昧分寸"的态度设定。

## 测试

    gradlew.bat testDebugUnitTest   (Windows)

## 说明
- 单用户文字聊天，无登录；需要网络访问 https://api.deepseek.com 和 Open-Meteo（天气）。
