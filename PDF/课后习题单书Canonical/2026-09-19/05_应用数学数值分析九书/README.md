# 应用数学与数值分析九书：单书 Canonical（2026-09-19）

本目录记录九书项目的逐书最高稳定稿、版本冲突裁决和 PDF 结构验收。二进制 PDF 已持久化到 ChatGPT Library；Google Drive 目标目录已建立，但账号存储配额已满，因此本轮没有把 PDF 上传成功，也没有删除任何旧版本。

## 现行九本单书

|序号|书目|严格状态|页数|SHA-256|
|---:|---|---|---:|---|
|1|Bender–Orszag, *Advanced Mathematical Methods for Scientists and Engineers I*|169 strict formal；Ch1–3 闭合；Ch4 的 4.10、4.13、4.32(c) 数学材料保留但不冒充完整 formal|427|`eee038c5a034cb6c68c7503ce959edb09def2187b435985eae151021895cff32`|
|2|Conte–de Boor, *Elementary Numerical Analysis*, 3e|389/389 formal complete|901|`67ec12649a7decac4b30bd3741301750899b1ce5d072106bbf72f432268b6880`|
|3|Golub–Van Loan, *Matrix Computations*, 3e|428/428 formal complete|917|`0f47cece3cd3d02cd0e6c3fc329df90288a78e14ebe68f9b371c2b703273013a`|
|4|Hairer–Nørsett–Wanner, *Solving Ordinary Differential Equations I*, 2e|226/226 formal complete|566|`f90ae708e491ffe57d01801491481fe95d55766dcf0657c560bb5e7d5b50daba`|
|5|Gustafsson–Kreiss–Oliger, *Time-Dependent Problems and Difference Methods*, 2e|Chapter 1 为 24/24；Chapter 2–13 SOURCE_LOCK|161|`1c27adb950fa6c051bff590cd198d503a1795c477c931c9ecc6af62befdbaff2`|
|6|Keener, *Principles of Applied Mathematics*, Revised Edition|65 strict formal；三处 source-lock 数学核心预闭合|363|`315e520131a429f84e67db9e33c74049fef8549884e0296f883a4bccc8136dad`|
|7|Trefethen–Bau, *Numerical Linear Algebra*|159/159 formal complete|374|`445978af81396b219c3c59c7866d0c270d7f9259c832eba418f3f2f34384ce00`|
|8|Brenner–Scott, *The Mathematical Theory of Finite Element Methods*, 3e|348/348 formal complete；Ch0–14 闭合|699|`b428ef3c717a9dd1103fc21c77acac1d171e8168e5bafd8465d46551daf80138`|
|9|F. Y. M. Wan, *Introduction to the Calculus of Variations and Its Applications*, 2e (1995)|2 full formal + 2 verified subparts；其余保持 SOURCE_LOCK|184|`de1b859a55ba82998cb6efc7599ba0fd62148100b8c87a22cc7457900fb1a6f9`|

## 关键裁决

- Bender–Orszag v022 曾写 170 formal；v023 恢复更严格的 169 口径，因为 Chapter 4 Problems 原题页没有完整恢复。数学已闭合的材料继续保留，但不虚增正式题。
- Library 搜索只暴露 Brenner–Scott v013；重新扫描 Google Drive 后找到 v020，明确 348/348、Chapter 0–14 全闭合，因此 v020 取代旧视图。
- Wan 早期总账存在母书串版次；现冻结为 1995 第二版、18 章加 Appendix。补充理论与数值实验不冒充原书 formal。
- GKO 与 Keener 均严格区分正式原题和 source-lock / supplemental 训练。

## QA

- 9 本合计 4,592 页、5,174 个书签、7,317 个链接对象。
- 全部 PDF 可打开、未加密、页面均为 A4，0 个无文本层页面。
- 每本重新渲染封面、目录/第二页、中页和末页，共检查 36 个代表页；未见裁切、黑块、叠页或中文缺字。
- 完整交付包已通过 `unzip -t`。

## 删除政策

本轮没有删除旧版。只有新 PDF 成功上传到远端、远端重新打开、页数与 SHA-256 复核通过，并确认旧文件不是唯一题源证据或独立分支后，才会按精确文件 ID 清理。
