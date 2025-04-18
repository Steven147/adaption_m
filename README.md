# adaption - demo app for adaption logic 适配逻辑展示应用

## 本应用功能

- 模拟视频Feed页面展示，适配参数可展示，可控制
  - 开关 & 滑块参数调整
  - json 字符串导入导出
  - todo 适配参数导入导出
  - todo 除零、浮点数比较问题
- 集成适配基础能力模块

## 适配模块概述

适配逻辑的代码结构，区分为了manager、strategy、handler三层结构，每一层都有若干节点，这三层结构之间都是一对多的关系，适配逻辑的实现就是实现一个三层的节点树。

同时，适配过程可以拆分成初始化过程和适配执行过程，结合三层结构，我们可以梳理出如下的**关键时机**：

- 初始化
  - 框架初始化
  - manager初始化
  - strategy初始化
  - handler初始化
- 适配执行 【todo 可抽象成 IAdaptionProcess-doAdaption】】
  - 框架执行
  - manager适配执行 【AbstractAdaptionManager-doAdaption】
  - strategy适配执行【AbstractAdaptionStrategy-doAdaptionStrategy】
  - handler适配执行 【AbstractAdaptionHandler-handleAdaption】

注意，初始化的时机中，manager早于strategy早于handler，且strategy和handler的时机可能延迟到适配执行前。
适配执行时，manager执行中，会筛选并选定第一个符合条件【matchStrategy】的strategy，而strategy中包含了对应多个handler的执行。

而适配过程中的**数据**，区分为了context、params、result三类，其中context【IAdaptionContext】是和节点绑定的上下文输入，params【IAdaptionParams】是和每次适配行为相关的适配参数输入，result【IAdaptionResult】则代表着适配结果。

## 适配逻辑接入

在接入过程中，我们可以通过strategy的顺序和过滤条件，来实现策略间优先级以及策略间隔离。在我们需要对策略进行迭代时，可以在strategy层接入。

在迭代过程中，handler是与业务无关的代码，可以在不同strategy间复用，同时也可以通过组合不同的handler实现策略。我们可以通过在现有strategy中新增handler的方式，实现对策略的扩展。

## 适配过程感知

适配过程包括实例化manager，传入manager层context，

manager层context包含strategy工厂factory，factory中会组装各个strategy的context

---
# 项目说明

这是一个面向 Android、Web 和桌面端的 Kotlin 多平台项目。

* `/composeApp` 目录用于存放可在 Compose 多平台应用程序间共享的代码。
  该目录包含多个子文件夹：
  - `commonMain` 文件夹用于存放适用于所有目标平台的通用代码。
  - 其他文件夹则用于存放仅会为文件夹名称所指示的平台编译的 Kotlin 代码。
    例如，若你想在 Kotlin 应用的 iOS 部分使用苹果的 CoreCrypto 库，
    那么 `iosMain` 就是存放此类调用代码的合适文件夹。

## 学习资源
了解更多关于：
- [Kotlin 多平台](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose 多平台](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform)
- [Kotlin/Wasm](https://kotl.in/wasm/)

## 反馈与问题报告
我们非常欢迎你在公共 Slack 频道 [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web) 分享你对 Compose/Web 和 Kotlin/Wasm 的反馈。

如果你在使用过程中遇到任何问题，请在 [GitHub](https://github.com/JetBrains/compose-multiplatform/issues) 上提交问题报告。

## 启动 Web 应用程序
你可以通过运行 `:composeApp:wasmJsBrowserDevelopmentRun` Gradle 任务来打开 Web 应用程序。sk.