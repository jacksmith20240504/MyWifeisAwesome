# MyWifeisAwesome
赞美老婆APP

# SweetReminder — 安卓应用设计需求说明

> **应用名称（暂定）：** **SweetReminder**

---

## 1. 目标与核心场景

> **目标**：帮助用户在不干扰日常工作的前提下，定时收到“提醒夸赞妻子”的推送，从而维系并增进亲密关系。  
> **核心场景**：应用在后台静默运行；每 3 小时弹出一条本地通知，提示用户用不少于 20 字、且主题与上一次不同的赞美语；用户点开通知后可查看／编辑并记录本次夸赞。

---

## 2. 功能需求

| 编号 | 功能                | 说明 |
|-----:|---------------------|------|
| **F1** | **定时提醒**           | - 默认间隔 3 小时<br>- 闹钟精准触发；兼容 Doze / 省电模式（`WorkManager` 或 `AlarmManager.setExactAndAllowWhileIdle`） |
| **F2** | **通知推送**           | - 独立 *Notification Channel*（“Love Reminders”）<br>- 标题：“到了夸夸老婆的时间啦！”<br>- 内容预览：随机生成 ≥ 20 字的赞美草案（见 F3） |
| **F3** | **自动生成赞美草案**     | - 维护 6–8 大主题：外貌、才华、性格、成长回忆、共同愿景、感激之情…<br>- 轮换或随机，**确保与上一次主题不同**<br>- 模板填充 + 可自定义句库 |
| **F4** | **用户编辑与发送**       | - 点开通知跳转到 Compose 界面<br>- 可复制分享 / 手动修改<br>- 字数计数器（< 20 字时高亮提示） |
| **F5** | **历史记录**           | - 时间线方式展示已发送夸赞（Room 持久化）<br>- 关键词搜索、主题筛选 |
| **F6** | **设置中心**           | - 频率（1–6 小时步进选择）<br>- 活跃时段（例 07:00–23:00）<br>- 震动／铃声／静默<br>- 主题库管理、草案开关 |

---

## 3. 非功能需求

| 类别       | 需求 |
|------------|------|
| **性能**     | 后台常驻 ≤ 60 MB；闹钟误差 ≤ 5 min |
| **电量**     | `WorkManager` 周期调度 + `JobScheduler`，避免频繁唤醒 |
| **隐私**     | 所有数据本地保存，不上传云端 |
| **可用性**   | 支持 Android 8.0 (API 26) 以上；暗/亮色自适应 |
| **易用性**   | 首次启动“一键开始”；其余参数置于设置页 |
| **可维护性** | MVVM + Jetpack Compose；依赖注入 Koin；统一数据仓库 |

---

## 4. 技术方案概览

1. **语言 & 框架**  
   - Kotlin 1.9+  
   - Jetpack Compose UI  
   - `WorkManager` (`PeriodicWorkRequest`) + `setInitialDelay`  
   - Room DB（历史记录 & 上次主题）  
   - DataStore (键值配置)

2. **主题判重逻辑**

   ```kotlin
   val themes = listOf(A, B, C, D, E, F)
   val lastTheme = prefs.lastTheme
   val nextTheme = (themes - lastTheme).random()
   prefs.lastTheme = nextTheme
   generateCompliment(nextTheme)
