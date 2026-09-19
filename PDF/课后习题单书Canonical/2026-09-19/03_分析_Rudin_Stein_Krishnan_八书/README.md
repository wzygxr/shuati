# Rudin–Stein–Krishnan 八书单书化 Canonical 总账（2026-09-19）

## 选择原则

直接单书稳定稿 > TRUE_CANONICAL_UNION / 可审计题位并集 > source-level union > LATEST 指针 > 历史快照。

本次发现 v032 与 v031 属于不同推进分支：v032 的 Rudin RCA 已到 Chapter 12，但 Stein–Shakarchi Functional Analysis 在该分支只到 Chapter 2；另一分支已经 267/267 全书闭合。因此 Stein FA 采用跨分支严格并集，避免版本号回退。

## 已生成文件

|序号|书目|现行状态|PDF页数|大小(MB)|SHA-256|
|---:|---|---|---:|---:|---|
|1|Walter Rudin《Principles of Mathematical Analysis》3e 全习题详解|287/287 formal complete；仅余二轮 QA / 来源审计 / 变式去重。|424|2.312|`794ac0138474f0e4123a6877fa864b927b66e3786a0e22ab0a968ec6470c6f45`|
|2|Walter Rudin《Functional Analysis》2e 全习题详解|314/314 formal complete；仅余二轮 proof / errata / source QA。|541|2.849|`248f4c975f1a59c0bf7b5dbfff7be7b3a2e254130ab7588d897308f37dc3612a`|
|3|Walter Rudin《Real and Complex Analysis》3e 习题详解|248 formal；Chapters 1-12 连续闭合；744 variants。|521|2.652|`b151b97ec6e30168780070e6d40f1873b6cca901b38b55b9cfa68c259e8c5c33`|
|4|Stein-Shakarchi《Fourier Analysis》全习题详解|170/170 formal complete。|166|1.578|`52728ce1f44dc909ad4ac2f21f1801b1a9813850e22f902a95e20a62574c89b8`|
|5|Stein-Shakarchi《Complex Analysis》全习题详解|210/210 formal complete。|399|2.205|`7d4ac1a06fc5ad831a8cd139e6a7cb45f9a2bd1dcbff6dcb918f4a5ba8b49a6e`|
|6|Stein-Shakarchi《Real Analysis》全习题详解|265/265 formal complete。|398|2.329|`0ef10e8507868b65b52ab820d979af5774fb03157d057c45c0aca525c6c18850`|
|7|Stein-Shakarchi《Functional Analysis》全习题详解|267/267 formal complete；Chapters 1-8 全部闭合。|597|3.550|`40696d366b2929182ff56e45aeb42831a289ba6406b1a1f109406779bdfa8e21`|
|8|V. K. Krishnan《Textbook of Functional Analysis》2e 项目现有材料|0 strict formal + 40 supplemental units；SOURCE_LOCK。|21|0.981|`6afb0bb87e3192e1c17d5f20da9b0104ae0e86d2cda63c1406dc21856b261920`|

## QA

- 全部 PDF 可打开、未加密、全部页面为 A4。
- 每页均有可检索文本层。
- 8 本合计 3,067 页，书签合计 9,110 条。
- 每本均重新渲染封面、来源页、正文首段、中段和末页；未见裁切、黑块、叠页或中文缺字。
- 原解答正文未改写；只按书目抽页、排序、剔除其他书正文并重建书签。
- 页边界处若两个书目共用原合订本同一物理页，为防止内容丢失，该页允许在两个单书 PDF 中同时保留。

## 存储状态

- 8 本 PDF、QA、CSV、SHA-256 和 ZIP 已持久化到 ChatGPT Library。
- Google Drive 已创建目标目录，但 PDF 上传因账号存储配额已满而被 Drive 拒绝；未虚报上传成功。
- GitHub 本目录保存审计清单与哈希。PDF 二进制待 Drive 配额恢复或采用适合大文件的上传通道后再同步。

## 删除政策

本批尚未删除任何旧版本。只有在新文件成功上传、远端可打开、SHA-256/页数复核无误，并确认旧文件不是独立分支或唯一证据后，才会删除；当前先建立候选清理队列。
