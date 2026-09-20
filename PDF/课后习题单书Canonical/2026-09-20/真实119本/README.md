# 课后习题单书 Canonical：真实 119 本（2026-09-20）

## 当前冻结结果

- 独立单书 PDF：**119 本**
- 上一机器验收基线：**100 本**
- 本轮经版本比较、书名差集与合订本拆分后新增：**19 本**
- 累计页数：**61,777 页**
- 新增 19 本：**4,544 页**
- 100 本基线 SHA-256 不一致：**0**
- 119 本精确 SHA-256 重复：**0**
- 归一化书名碰撞：**0**
- 新增卷结构、书签、内部链接问题：**0**
- Ghostscript 全页解释失败：**0**
- 删除旧版本：**0**

`Canonical` 表示该书目前可回读的最全稳定卷，不等于每一本都已全书清零。`SOURCE LOCK`、`EDITION AUDIT`、`GAP AUDIT`、`CURRENT PARTIAL` 均保留原状态。

## 本轮新增 101–119

101. Dubrovin–Fomenko–Novikov, *Modern Geometry Part II*：69 formal + 11 reader；source/edition audit。
102. Gilbarg–Trudinger, *Elliptic PDE of Second Order*, 2e：当前高正确率累计分支。
103. Serre, *A Course in Arithmetic* 配套卷：211 derived tasks + 633 variants；原书无成套 official exercise block。
104. 丁勇《现代分析基础》第二版：41/42 strict formal。
105. 丁同仁、李承治《常微分方程教程》第三版：159/159 当前映射账。
106. 聂灵沼、丁石孙《代数学引论》第二版：当前最全；Ch0–7 已处理，Ch8–9 继续。
107. 胡发胜、宿洁《数理统计》：当前扫描范围 120/120。
108. 谷超豪等《数学物理方程》第三版：Chapters 1–7 formal closed。
109. Rubakov, *Classical Theory of Gauge Fields*：258/258 项目编号题账。
110. Weinberg, *The Quantum Theory of Fields*, Vol. I：当前可定位 65/65；出版社 70 metadata 差额 5 继续审计。
111. Mnev QFT/BV Chapter 3 项目：§§3.1–3.11。
112. Radovanović, *Problem Book in QFT*, 2e：Chapters 1–11，222/222。
113. ISLR2：当前可恢复 Chapter 4 部分稿；GAP AUDIT。
114. Weisberg, *Applied Linear Regression*：当前可恢复批次；GAP AUDIT。
115. DFN *Modern Geometry Part III*：93 formal + 9 reader；source/edition audit。
116. Wan, *Introduction to the Calculus of Variations and Its Applications*, 2e：2 full formal + 2 verified subparts。
117. Weinberg QTF Vol. II：当前可定位 46/46；出版社 50 metadata 差额 4 继续审计。
118. Durrett, *Essentials of Stochastic Processes*, 3e：当前恢复 Chapter 1，77 positions + 231 variants；PARTIAL。
119. Ross, *Introduction to Probability Models*, 12e：当前恢复 Chapter 10 Problems 1–37 + 111 variants；PARTIAL。

## QFT 分卷纪律

Rubakov、Mnev、Radovanović、Weinberg Vol. I、Weinberg Vol. II 已按实际章节/问题标题重新分界；混合项目尾账不再复制到各卷末尾。仅当一个物理页同时包含本书题解与下一书开头时，保留不可再切的过渡页。

Weinberg 的 65/65 与 46/46 只表示当前能逐页定位的 numbered Problems/sections 已覆盖；出版社 70/50 metadata 的 5/4 差额不能反推成虚构题号。

## 存储状态

- ChatGPT Library 已持久化：README、机器总账、CSV、QA、SHA-256、001–100 分卷、101–119 增量包。
- Google Drive 已成功创建目标目录，但当前挂载点拒绝文件写入（`forbidden`），没有虚报上传成功。
- 571 MB 全整包超过 Library 单文件 512 MB 上限，因此长期保存采用两个可独立解压的分卷。
- GitHub 本目录保存审计说明与包级哈希；不把大型 PDF 二进制伪装成已上传 GitHub。

## 删除规则

只有新 Canonical 远端上传成功、重新下载成功、页数一致、SHA-256 一致，并确认旧稿不是唯一题源、独立分支或必要回滚点后，才允许按精确文件 ID 删除。本批删除 0。
