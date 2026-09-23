# README：统一 v053 编译与复现

## 主要文件

- `光学_IPhO金牌讲义与全习题详解_统一累计v053_钟锡华第2章2.22至2.42全闭合.md`
- `光学_IPhO金牌讲义与全习题详解_统一累计v053_钟锡华第2章2.22至2.42全闭合.pdf`
- `光学_IPhO全习题详解_统一v053_新增卷_钟锡华现代光学基础第2章2.22至2.42.md`
- `光学_IPhO全习题详解_统一v053_新增卷_钟锡华现代光学基础第2章2.22至2.42.pdf`

## 重新编译新增卷

```bash
pandoc 光学_IPhO全习题详解_统一v053_新增卷_钟锡华现代光学基础第2章2.22至2.42.md \
  --pdf-engine=xelatex \
  --resource-path='.:assets' \
  -o 光学_IPhO全习题详解_统一v053_新增卷_钟锡华现代光学基础第2章2.22至2.42.pdf
```

## 重新生成累计 PDF

运行：

```bash
python validation/merge_v053.py
```

累计 Markdown 是长期正文源；由于历史 v001--v052 引用过多批次图片，当前累计 PDF 采用“已验收 v052 PDF + 已验收 v053 增量 PDF”合并，避免旧图片路径丢失造成历史内容缺页。

## 验证

```bash
python validation/validate_v053.py
python validation/check_cumulative_all_pages.py
python validation/render_metrics_v053.py
```

完整源书渲染证明沿用 v052 的713页清单；本轮首先核对两份原PDF的SHA-256不变，因此该证明仍对应同一二进制文件。

## 轻量验收包说明

`光学_IPhO全习题详解_统一v053_轻量源码与验收包.zip` 包含累计 Markdown、增量 Markdown/PDF、证据册、报告、自绘图、验证脚本、JSON/CSV 日志和联系表。为避免重复占用空间，包内不重复放入 94 MB 的累计 PDF，也不放入逐页渲染 PNG；累计 PDF 单独交付，逐页检查由清单和联系表记录。

运行环境未安装 `qpdf`，因此没有声称通过 `qpdf --check`。结构验收使用 `pdfinfo`、PDF 头/尾标记、PyMuPDF 全页内容流载入以及实际渲染检查。
