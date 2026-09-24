# DING-C3-v001 编译、复算与合流说明

## 1. 编译增量PDF

```bash
cd /mnt/data/ding_optics_v057
pandoc 丁文革_第3章例3.1-3.20全解_合流增量_DING-C3-v001.md \
  --from=markdown+raw_tex+fenced_divs \
  --pdf-engine=xelatex \
  --resource-path=/mnt/data/ding_optics_v057 \
  -o 丁文革_第3章例3.1-3.20全解_合流增量_DING-C3-v001.pdf
```

## 2. 合流累计Markdown

去掉增量Markdown的YAML头，并删除中间的 `\frontmatter`、`\mainmatter`、`\backmatter` 命令；在上一累计Markdown末尾加入 `\clearpage` 后原样追加正文。不得删除历史正文。

## 3. 合流累计PDF

```bash
pdfunite 上一累计.pdf 本轮增量.pdf 新累计.pdf
```

## 4. 复算与验收

```bash
python verify_v057.py
python qa/audit_pdfs_v057.py
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 增量.pdf
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 累计.pdf
pdfinfo 增量.pdf
pdffonts 增量.pdf
```

## 5. 目录

- `source_pages/`：物理页95--106高分辨率渲染；
- `figures_v057/`：题源接触表和自绘示意图；
- `qa/`：数值复算、全页栅格化、PDF预检、非回归和接缝检查；
- `.tex`：Pandoc导出的可编译XeLaTeX源。
