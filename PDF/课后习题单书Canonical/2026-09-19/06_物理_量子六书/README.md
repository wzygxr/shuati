# 量子六书：单书 Canonical（2026-09-19）

本目录记录 Sakurai、Mizera、David Tong、Preskill、Nielsen–Chuang、Steven H. Simon 六个项目从累计合订稿拆成独立单书 PDF 的来源、严格题量口径和结构验收。

## 现行六本单书

|序号|书目|严格状态|页数|SHA-256|
|---:|---|---|---:|---|
|1|Sakurai–Napolitano, *Modern Quantum Mechanics*, 3e|241/242；仅 SK-6.13 仍待原题锁定|763|`220f840124caf10c45b74b90714dd649db20007aea7c76bec8f5599bad8a4296`|
|2|Mizera, *Physics of the Analytic S-Matrix*|17/17 formal complete|95|`b91b5cd67f9f60acc469c78d8c29e18a9553ff4a7f72da0b8caaf5dedadac8ae`|
|3|David Tong, *Quantum Mechanics*|112 formal；10/10 bundles complete|405|`9c589271494c7f3074745ff47f8105482a36cdec54ae6a0b08959037763de2a0`|
|4|Preskill, *Quantum Computation and Quantum Information* lecture notes|123 unique formal positions；滚动去重审计|579|`64dcd328cfaf0c65cbe2dc415ead14422cbb111cdd30630c71789ffc9214e0d5`|
|5|Nielsen–Chuang, *Quantum Computation and Quantum Information*, 10th Anniversary ed.|529 Exercises + 63 Problems = 592/592；旧文件名“598”已按严格分母校正|827|`277825f0a81e3d488fe39119f571733983aa88ff4e4d4466f993bfe59ce3560d`|
|6|Steven H. Simon, *Topological Quantum* / OUP exercise project|141 个数学已解 lineage；49 个 final-OUP 已认证题位，二者不混同|335|`65ed30e9d1bf5a149ff896644ff69b3998212be778ba37d9bbc23c3c8705229d`|

## 拆分与分支裁决

- Sakurai、Mizera、Tong 从 v025 累计稿按实际页边界拆出，分别对应原物理页 100–861、862–955、956–1359。
- Preskill 存在两个同名 v019 分支。采用创建时间更晚、正文自报 v019、且含 1,899 个原书签的分支；另一分支正文仍自报 v018，暂保留为历史证据。
- Nielsen–Chuang 采用独立 v017 累计稿；重建书签并在审计封面明确严格分母 592，避免继承旧文件名的 598 误口径。
- Simon 从 v043 累计稿仅保留新增 Simon 主线物理页 1–334；物理页 335 起已回到 v024 以前的六书历史，不混入 Simon 单书。

## QA

- 六本合计 3,004 页、2,549 个书签、58 个链接对象。
- 全部 PDF 可打开、未加密、页面均为 A4，0 个无文本层页面。
- 每本重新渲染封面、目录/前部、中部和末页；拆分接缝页也单独检查，未见裁切、黑块、叠页或中文缺字。
- 完整交付 ZIP 已通过 `unzip -t`。

## 存储状态

- 六本 PDF、QA、CSV、SHA-256 和完整 ZIP 已持久化到 ChatGPT Library。
- Google Drive 已创建目标目录 `06_物理_量子六书`，但账号存储配额已满，因此本轮没有虚报 PDF 上传成功。
- 本 GitHub 目录先保存可审计清单与哈希；PDF 二进制等待可用的大文件上传通道或 Drive 配额恢复后同步。

## 删除政策

本批未删除任何旧版本。只有新文件成功上传到远端、重新打开、页数与 SHA-256 复核无误，并确认旧文件不是唯一题源证据或独立分支后，才会按精确文件 ID 清理。
