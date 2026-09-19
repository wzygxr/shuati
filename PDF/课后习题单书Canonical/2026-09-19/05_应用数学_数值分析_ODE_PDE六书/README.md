# 应用数学・数值分析・ODE/PDE 六书：单书 Canonical 总账与 QA

日期：2026-09-19

## 选择规则

直接单书最高稳定累计稿 > 可审计的版本并集 > LATEST/umbrella > 历史增量卷。正式题、显式小问、reader tasks、先修专题与补充训练严格分账。

## 六本结果

|#|书目|严格状态|页数|书签|链接|SHA-256|
|---:|---|---|---:|---:|---:|---|
|1|Lawrence C. Evans, Partial Differential Equations, 2nd ed.|202/202 formal Problems complete|779|2254|0|`a95de86dafc738f6c8a5c0e142ac7183ff14076449630afb853099b0f9dbf1b4`|
|2|张祥《常微分方程》|101/101 chapter-end formal exercises complete|328|282|288|`d1a0130493bdf85e6fae563e5f55d8280995d47b4bdefa73802b622849088f22`|
|3|Hairer-Lubich-Wanner, Geometric Numerical Integration, 2nd ed.|166/166 formal Exercises complete|500|0|1061|`733aa46f7412503d0031d5d24b2f30620cf754907a00e63feae64381732012e4`|
|4|Hairer-Norsett-Wanner, Solving Ordinary Differential Equations I, 2nd ed.|226/226 formal tasks complete|566|0|635|`f90ae708e491ffe57d01801491481fe95d55766dcf0657c560bb5e7d5b50daba`|
|5|Trefethen-Bau, Numerical Linear Algebra|159/159 official Exercises complete|374|0|1338|`445978af81396b219c3c59c7866d0c270d7f9259c832eba418f3f2f34384ce00`|
|6|Brenner-Scott, The Mathematical Theory of Finite Element Methods, 3rd ed.|348/348 formal Exercises complete|699|2893|2896|`b428ef3c717a9dd1103fc21c77acac1d171e8168e5bafd8465d46551daf80138`|

合计：**1202 个 formal 题位，3246 页，5429 个书签，6218 个链接对象。**

## 版本裁决

- **Lawrence C. Evans, Partial Differential Equations, 2nd ed.**：第20批冻结 Chapters 1-12 的 202 道印刷 Problems；12 个先修专题与 36 道专题变式继续独立分账。
- **张祥《常微分方程》**：采用 v007 二次审计强化版，覆盖较早 v006 清零卷；正式顶层题仍为 101，不把显式小问、reader tasks 与综合训练虚增为 formal。
- **Hairer-Lubich-Wanner, Geometric Numerical Integration, 2nd ed.**：Drive 的 v009 与 LATEST PDF 字节数及 SHA-256 完全相同；以有明确版本号的 v009 命名入库。59 个无文本页经渲染确认均为有意空白/分隔页。
- **Hairer-Norsett-Wanner, Solving Ordinary Differential Equations I, 2nd ed.**：采用 v015 全书闭合终卷；Chapter I-III 全闭，正式任务剩余 0。
- **Trefethen-Bau, Numerical Linear Algebra**：采用 v008 二轮质量重构累计卷，而不是仅含增补内容的 v008 补充卷；正式题数沿 v006 校正账冻结为 159。
- **Brenner-Scott, The Mathematical Theory of Finite Element Methods, 3rd ed.**：采用 v020 全书终卷；Chapter 0-14 全部正式 Exercises 闭合，剩余 0。

## PDF QA

- 六本 PDF 均可打开、未加密，页面矩形合法。
- 使用 PyMuPDF 逐页扫描文本层、页面框、链接与目录；除 HLW 有意空白/分隔页外，无无文本页。
- 对封面、目录、1/3、2/3、末页及 HLW 空白页前后进行实际渲染检查；未见裁切、重叠、黑块或断字。
- 使用 Ghostscript `nullpage` 对全部 3246 页逐页解析，六本均通过。
- canonical 文件是源 PDF 的字节级无损复制；SHA-256 与源文件一致。

## Nocedal-Wright 暂缓入库

`NW_ch17-19_solutions.pdf` 只有 42 页，内容是第七批 Chapter 17-19 的增量卷。它的封面/末页确实登记全书 236 道题的总账，但正文不含 Chapters 2-16 的完整解答，因此本轮不把它冒充为全量单书 canonical。后续必须找到早期累计主体，或用已验收分批卷做可审计无损合并后再入库。

## 删除政策

本批不删除任何旧版本。只有新 canonical 二进制在远端完成上传、重新打开、页数与 SHA-256 复核，且确认旧文件不是独立分支、回滚点或唯一题源证据后，才按精确文件 ID 清理。
