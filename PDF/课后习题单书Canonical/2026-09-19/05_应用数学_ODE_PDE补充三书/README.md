# 应用数学・ODE/PDE 补充三书：单书 Canonical 总账与 QA

日期：2026-09-19

## 去重说明

早先的“应用数学数值分析九书”已经包含 HNW、Trefethen–Bau 与 Brenner–Scott。本批 SHA-256 比对确认三者与新扫描副本完全相同，因此不重复入库；本目录只保存九书之外的 Evans、张祥与 HLW。

## 三本结果

|#|书目|严格状态|页数|书签|链接|SHA-256|
|---:|---|---|---:|---:|---:|---|
|1|Lawrence C. Evans, *Partial Differential Equations*, 2nd ed.|202/202 formal Problems complete|779|2254|0|`a95de86dafc738f6c8a5c0e142ac7183ff14076449630afb853099b0f9dbf1b4`|
|2|张祥《常微分方程》|101/101 chapter-end formal exercises complete|328|282|288|`d1a0130493bdf85e6fae563e5f55d8280995d47b4bdefa73802b622849088f22`|
|3|Hairer–Lubich–Wanner, *Geometric Numerical Integration*, 2nd ed.|166/166 formal Exercises complete|500|0|1061|`733aa46f7412503d0031d5d24b2f30620cf754907a00e63feae64381732012e4`|

合计：**469 个 formal 题位，1607 页，2536 个书签，1349 个链接对象。**

## 版本裁决

- **Evans**：第20批冻结 Chapters 1–12 的 202 道印刷 Problems；12 个先修专题与 36 道专题变式独立分账。
- **张祥**：采用 v007 二次审计强化版，覆盖较早 v006；正式顶层题仍为 101。
- **HLW**：v009 与 `LATEST` 字节数及 SHA-256 完全相同；以有明确版本号的 v009 入库。59 个无文本页均为有意空白/分隔页。

## PDF QA

- 三本 PDF 均可打开、未加密，页面矩形合法。
- PyMuPDF 逐页扫描文本层、页面框、链接与目录；除 HLW 有意空白/分隔页外，无无文本页。
- 封面、目录、1/3、2/3、末页及 HLW 空白页前后均实际渲染检查，未见裁切、重叠、黑块或断字。
- Ghostscript `nullpage` 对全部 1607 页逐页解析通过。
- canonical 文件均为源 PDF 的字节级无损复制。

## Nocedal–Wright 暂缓入库

`NW_ch17-19_solutions.pdf` 仅 42 页，是 Chapters 17–19 的收尾增量卷。它虽登记全书 236 题总账，但不包含 Chapters 2–16 的完整正文；在找到早期累计主体或完成可审计无损合并以前，不冒充全书 canonical。

## 删除政策

本次只删除本轮刚产生且经 SHA 证明与既有 canonical 完全相同的三个重复副本；历史源文件、独立分支和旧回滚点不删除。
