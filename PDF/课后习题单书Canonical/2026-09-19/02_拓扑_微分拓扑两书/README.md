# 微分拓扑两书：单书 Canonical（2026-09-19）

本目录记录 `Differential_Topology_两书累计主文档_高正确率复审版_v015.pdf` 的逐书拆分、边界页裁决、书签重建与 PDF 验收。

## 现行两本单书

|序号|书目|严格状态|页数|书签|SHA-256|
|---:|---|---|---:|---:|---|
|1|Guillemin–Pollack, *Differential Topology*|352/352 formal complete|626|2794|`82c5973a30c8ed542613831bde7d944614777d06e60e78d9df0bd89854eef66c`|
|2|Milnor, *Topology from the Differentiable Viewpoint*|17/17 §8 Problems complete|56|222|`4e44811548303b71c02e0b21adf1a4ca7a207f194e7c9e9c8ebcc00a2971dfcc`|

## 页段裁决

- Guillemin–Pollack：母本物理页 60–637、675–695、698–713、714–723。
- Milnor：母本物理页 638–674、695–701、713–723。
- 母本第 695、713、714 等页在同一物理页内发生书目切换。为了不丢失一书复审段落的开头或结尾，这些边界页允许在两本单书中重复保留；没有把页面内容裁成半页。
- Guillemin–Pollack 的书签由母本 2,978 条目录按页段与书目归属过滤、重映射；Milnor 同样重建并排除明确的 GP-specific 复审子树。

## QA

- 两本合计 682 页，全部 A4、未加密，0 个无文本层页面。
- PDFium 全页解码 682/682，0 failures。
- PDFium 150 dpi 人工抽查 16 个代表页：封面、首尾、书目切换、滚动复审边界及末页均未见裁切、黑块、叠页或中文缺字。
- 所有书签目的页均在有效页码范围内；完整 ZIP 已通过 CRC 全量检查。

## 存储与清理

- PDF、QA、CSV、机器检查、SHA-256 和 ZIP 已持久化到 ChatGPT Library：`/课后习题单书Canonical_2026-09-19/02_拓扑_微分拓扑两书/`。
- 本目录保存审计清单与哈希；PDF 二进制不直接提交普通 Git 仓库。
- 本批没有删除任何旧版。只有远端新文件可打开、页数与 SHA-256 复核一致，并确认旧文件不是唯一题源证据或独立分支后，才进入精确 ID 清理。
