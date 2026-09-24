# DING-C3-v002 编译、复算与合流说明

## 1. 编译增量PDF

```bash
cd /mnt/data/ding_optics_v058
pandoc 丁文革_第3章例3.21-3.49全解_合流增量_DING-C3-v002.md \
  --from=markdown+raw_tex+fenced_divs \
  --pdf-engine=xelatex \
  --resource-path=/mnt/data/ding_optics_v058 \
  -o 丁文革_第3章例3.21-3.49全解_合流增量_DING-C3-v002.pdf
```

## 2. 合流累计Markdown

去掉增量文件YAML头和 `frontmatter/mainmatter/backmatter` 控制行，在上一累计Markdown末尾插入 `\clearpage` 后原样追加正文；不删除历史正文。

## 3. 合流累计PDF

```bash
pdfunite 上一累计.pdf 本轮增量.pdf 新累计.pdf
```

## 4. 复算与验收

```bash
python verify_v058.py
python audit_pdfs_v058.py
python /home/oai/skills/pdfs/scripts/render_pdf.py 增量.pdf --out_dir qa/render_increment_150 --dpi 150
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 增量.pdf
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 累计.pdf
pdfinfo 增量.pdf
pdffonts 增量.pdf
```

## 5. 目录说明

- `source_pages/`：例3.21--3.49题面页；
- `next_source_pages/`：第3章自测题题面与答案页，用于冻结下一批；
- `figures_v058/`：题源接触表和自绘示意图；
- `qa/`：数值、全页栅格化、非回归、Ghostscript、PDF字节和字体验收；
- `.tex`：Pandoc生成的可编译XeLaTeX源。
