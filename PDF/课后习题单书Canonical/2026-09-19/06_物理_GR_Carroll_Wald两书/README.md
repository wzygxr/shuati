# 广义相对论两书：单书 Canonical（2026-09-19）

本目录记录 Sean M. Carroll《Spacetime and Geometry》与 Robert M. Wald《General Relativity》的最高稳定单书母本裁决、尾页边界处理、书签重建与 PDF 验收。

## 现行两本单书

|序号|书目|严格状态|页数|书签|SHA-256|
|---:|---|---|---:|---:|---|
|1|Sean M. Carroll, *Spacetime and Geometry*|78/78 formal complete；234 variants；312 units|378|1097|`9e116d6c0ec21b413d3fb74604c450a586bfe35b83190d0ada1e7a358d689c48`|
|2|Robert M. Wald, *General Relativity*|85/85 formal complete；255 variants；340 units|456|1377|`8455590742c736bca0eb4e2d133b164c48d6026568c76f9c42f136016c97e56f`|

## 边界裁决

- Carroll 取母本物理页 1–377。第377页上半部仍含 Carroll 78/78 最终总账，后半部才开始 Wald 状态，因此整页保留；第378页已完全切到后续项目，排除。
- Wald 取母本物理页 1–455。第455页含 Wald 85/85 最终总账，随后开始 DFN 等项目，整页保留；第456页已完全切到 Blau/Nakahara/DFN 后续任务，排除。
- 正文保持原 Letter 尺寸 612×792 pt，不做缩放；新增 provenance 封面使用同尺寸。

## QA

- 两本合计 834 页；未加密，0 个无文本层页面。
- 2,474 条书签目的页和 65 个链接对象均有效。
- PDFium 全页解码 834/834，0 failures。
- PDFium 150 dpi 抽查 11 个代表页，封面、目录、正文末段、终局总账与混合边界页均无裁切、黑块、叠页或中文缺字。
- `pdf_preflight.py` 两本均 `ok_open=true`、`likely_scanned=false`、`warnings=[]`；ZIP CRC 全量通过。

## 存储与清理

- 完整二进制文件已经持久化到 ChatGPT Library：`/课后习题单书Canonical_2026-09-19/06_物理_GR_Carroll_Wald两书/`。
- Google Drive 已创建目录 `06_物理_GR_Carroll_Wald两书`，folder ID `1A5rmvZMqqRXIHZ83GgUNn1Y4xuLEo_py`；账号存储配额仍已满，故没有虚报 PDF 上传成功。
- 本批没有删除任何旧版本。只有远端新文件可打开、页数与 SHA-256 复核一致，并确认旧文件不是唯一题源证据或独立分支后，才按精确文件 ID 清理。
