# DING-C2-v003 编译、复算与合流说明

## 1. 编译增量PDF

```bash
cd /mnt/data/ding_optics_v056
pandoc 丁文革_第2章填空1-22简答1-15综合1-23全解_合流增量_DING-C2-v003.md \
  --from=markdown+raw_tex+fenced_divs \
  --pdf-engine=xelatex \
  --resource-path=/mnt/data/ding_optics_v056 \
  -o 丁文革_第2章填空1-22简答1-15综合1-23全解_合流增量_DING-C2-v003.pdf
```

## 2. 合流累计Markdown

去掉增量文件的YAML头和首个`\frontmatter`，在上一累计Markdown末尾加入`\clearpage`后原样追加正文。不要删除历史正文。

## 3. 合流累计PDF

```bash
pdfunite 上一累计.pdf 本轮增量.pdf 新累计.pdf
```

## 4. 复算与验收

```bash
python verify_v056.py
python qa/pdf_render_audit_v056.py
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 增量.pdf
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage 累计.pdf
pdfinfo 增量.pdf
pdffonts 增量.pdf
```

## 5. 目录说明

- `source_pages/`：本轮题面与答案页；
- `figures_v056/`：题源接触表和自绘示意图；
- `qa/`：编译、数值、全页栅格化、非回归、Ghostscript和PDF字节验收；
- `.tex`：Pandoc生成的可编译XeLaTeX中间源。
