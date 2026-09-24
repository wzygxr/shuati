# README：钟锡华《现代光学基础》ZXH-v056 编译与复现

## 1. 版本范围

本版从 ZXH-v055 的精确断点第6章习题6.1继续，连续完成：

- 第6章6.1--6.21，共21题；
- 第7章7.1--7.4，共4题；
- 每题3道本质不同、含完整解答的变式，共75道；
- 下一断点：第8章8.1。

## 2. 编译增量卷

```bash
pandoc "钟锡华现代光学基础_ZXH-v056_第6章第7章全闭合_增量.md" \
  --pdf-engine=xelatex \
  --resource-path=".:assets" \
  --toc --number-sections \
  -o "钟锡华现代光学基础_ZXH-v056_第6章第7章全闭合_增量.pdf"
```

## 3. 生成累计 PDF

累计 Markdown 是长期正文源。累计 PDF 为“重新生成的ZXH-v056封面和更新说明 + 已验收ZXH-v055历史正文 + 已验收ZXH-v056增量卷”合并，以免几千页历史图片因路径变动而丢失。

```bash
python merge_v056.py
```

## 4. 证据册

```bash
python build_evidence_v056.py
```

证据册包含教材第6、7章习题页和配套题解对应页，用于题号、题面、数字和原解路线回查。正文不是对配套题解的照抄；所有公式和数值均重新推导。

## 5. 验证

```bash
python validate_v056_fast.py
```

核心验收包括：25道源题、75道变式、19幅图、26组数值复算；教材488页与题解225页逐页渲染；增量85/85页和证据册56/56页逐页渲染；累计4188/4188页内容流载入；PDF头、startxref、EOF和Ghostscript nullpage检查。

当前环境没有安装 `qpdf`，因此没有声称执行 `qpdf --check`。

## 6. 字体边界

- ZXH-v056增量卷采用完整嵌入的 Noto Serif CJK 与 Latin Modern 字体；
- 累计PDF仍继承早期母版的 Heiti、STSong-Light 未嵌入字体；
- 新增85页没有引入新的未嵌入字体债。

## 7. 轻量包

轻量源码与验收包不重复包含约97 MB累计PDF，也不重复打包713张源书渲染PNG。包中保留累计Markdown、增量Markdown/PDF、证据册、全部自绘图、报告、验证脚本、JSON/CSV日志、源书全页渲染指标与哈希表。
