# 统计物理两书：单书 Canonical（2026-09-19）

本目录记录 David Tong《Lectures on Statistical Physics》与 Landau–Lifshitz《Statistical Physics, Part 1》第三版从同一累计母本拆成独立单书 PDF 的页段、书签与质量验收。

## 现行两本单书

|序号|书目|严格状态|页数|书签|SHA-256|
|---:|---|---|---:|---:|---|
|1|David Tong, *Lectures on Statistical Physics*|41/41 official problems complete|322|651|`d02a7ac0e1905e014dd877c43b0daa3f7e441260da8674d743734ca26d4df8db`|
|2|Landau–Lifshitz, *Statistical Physics, Part 1*, 3rd ed.|94/94 formal problems complete；282 variants|375|982|`8c863d7d051ec9840a23f72332d3282dda36f94b81f8b4efc5c3b007f95e59da`|

## 页段裁决

- Tong：母本物理页 1–56（两书共享 QA / 工具箱）与 57–321（四张 Example Sheets 全解）。
- Landau–Lifshitz：母本物理页 1–56（两书共享 QA / 工具箱）与 322–639（正式 Problems 全解、审校和最终总账）。
- 共享页在两本单书中有意重复，因为它们定义统一符号、题源层级、六重正确率审计和基础工具；第321页结束 Tong，第322页开始 Landau 专属正文。

## QA

- 两本合计 697 页，全部 A4、未加密，0 个无文本层页面。
- 1,633 条书签目的页与 54 个链接对象均有效。
- PDFium 全页解码 697/697，0 failures。
- PDFium 150 dpi 抽查 12 个代表页，封面、共享 QA 末页、拆分边界、终局总账与末页均无裁切、黑块、叠页或中文缺字。
- `pdf_preflight.py` 两本均 `ok_open=true`、`likely_scanned=false`、`warnings=[]`；ZIP CRC 全量通过。

## 存储与清理

- 完整二进制文件已经持久化到 ChatGPT Library：`/课后习题单书Canonical_2026-09-19/06_物理_统计物理_Tong_Landau两书/`。
- Google Drive 已创建目录 `06_物理_统计物理_Tong_Landau两书`，folder ID `1wr1CG0SvuSebGx2cN4rjaY_vXuVgd21u`；账号存储配额仍已满，故没有虚报 PDF 上传成功。
- 本批没有删除任何旧版本。只有远端新文件可打开、页数与 SHA-256 复核一致，并确认旧文件不是唯一题源证据或独立分支后，才按精确文件 ID 清理。
