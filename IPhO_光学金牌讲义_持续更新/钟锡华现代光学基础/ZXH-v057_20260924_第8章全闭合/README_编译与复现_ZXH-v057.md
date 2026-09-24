# README：钟锡华《现代光学基础》ZXH-v057

## 范围

本版从ZXH-v056断点8.1连续完成8.1--8.42，共42道源题、126道完整变式。下一断点9.1。

## 编译增量卷

```bash
pandoc "钟锡华现代光学基础_ZXH-v057_第8章8.1-8.42全闭合_增量.md" \
  --pdf-engine=xelatex \
  --resource-path=".:assets" \
  --toc --toc-depth=2 \
  -o "钟锡华现代光学基础_ZXH-v057_第8章8.1-8.42全闭合_增量.pdf"
```

## 累计文档

`build_cumulative_v057.py`更新累计Markdown；`validation/merge_cumulative_v057.py`用新的两页ZXH-v057前页替换旧v056前页，再接入已验收历史正文与第8章增量卷。这样保留四千余页历史图片和公式，不因旧资源路径变化丢图。

## 证据册

`validation/build_evidence_v057.py`合并：

- 完整教材物理页441--445；
- 配套题解物理页175--208。

## 验证

```bash
python validation/render_and_metrics_v057.py
python validation/validate_v057.py
```

检查42题、126变式、22图、独立数值、源文件页数/哈希、116页增量渲染、40页证据册渲染、4304页累计内容流载入、PDF头/startxref/EOF、pdfinfo、pdffonts及增量/证据册Ghostscript nullpage。

当前环境没有qpdf，未声称执行qpdf检查。累计PDF继承早期母版的STSong-Light和Heiti未嵌入字体；新增卷及证据册字体全部嵌入。

## 轻量包

轻量包不重复放入约94 MB累计PDF，也不重复打包713张源书渲染PNG；累计PDF单独交付。包中保留累计Markdown、增量Markdown/PDF、证据册、22幅图、报告、脚本、验证JSON/CSV、渲染指标和校验表。
