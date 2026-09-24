# QA REPORT v021

日期：2026-09-24

## 1. 内容结构

- 正式题标题：25/25（V.4.1--V.4.25 连续，无缺号）；
- 变式 A：25/25；
- 变式 B：25/25；
- 同类巩固：25/25；
- `TODO` / `FIXME` / Unicode replacement character：0；
- 下一断点 `Chapter V §5 Exercise 5.1` 已写入正文。

## 2. 编译

- Pandoc 生成 XeLaTeX 源码；
- XeLaTeX 连续两遍返回 0；
- PDF：76 页，A4，619718 bytes；
- SHA-256：`7ef722c45a6636fe791985b64f06e0c5efdc0a01f1112cc6bd82e2f5981932a9`。

## 3. 字节与解释验收

- `pdfinfo` 可正常读取；Suspects: no；未加密；
- `startxref=1`，`%%EOF=1`；
- Ghostscript 全文 nullpage 解释：错误输出 0 bytes；
- 字体表可读，所有列出的字体均嵌入；
- `pdftotext` 可抽取全文，replacement character=0，NUL=0。

## 4. 全页视觉验收

- 使用 Poppler 以 160 dpi 渲染：76/76 页；
- 每页尺寸统一为 1323×1871；
- 空白页候选：0；
- 20 像素页边深色裁切候选：0；
- 人工抽查页：1、38、65、66、69、72、76；封面/目录、正文中段、4.25 校正版、下降步骤、Mason--Stothers 证明和末页总账均正常。

## 5. 结论

本轮 PDF 通过“编译成功”之外的结构、字节、全文解释、文字抽取和全页渲染验收。它是 76 页独立增量分册；不虚报已经物理合并当前无法取得字节的 v020 历史 1146 页母版。
