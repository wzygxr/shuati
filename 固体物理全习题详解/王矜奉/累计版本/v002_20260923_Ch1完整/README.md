# 固体物理全习题详解：王矜奉支线 v002

本目录对应总项目 v025，日期 2026-09-23。

- 王矜奉《固体物理教程》第一章：27/27；
- 全书：27/123，尚余 96；
- 累计变式/巩固：81；
- 自绘/重绘解释图：17；
- 最终 PDF：152 页 A4；
- 自动检查：132/132 PASS；
- 下一断点：第二章第 1 题。

由于连接器不能把本地二进制 PDF 直接流式提交到 GitHub，且 Google Drive 当前返回存储配额已满，本分支先保存完整 Markdown 的无损分片、进度账本、QA、剩余总账和交付包 SHA-256。PDF 与完整 ZIP 同时保留在本次 ChatGPT 交付中。

## 重建累计 Markdown

```bash
python assemble_markdown.py
```

脚本按顺序拼接 `markdown_parts/part_*.mdpart`。拼接后 SHA-256 应为：

```text
2e252903a8c9f6fd9faea43874cbe1af068044dbeaa1f756463bc2a52e4f0ed1
```

`markdown_parts_manifest.json` 给出每个分片的字节数和 SHA-256。
