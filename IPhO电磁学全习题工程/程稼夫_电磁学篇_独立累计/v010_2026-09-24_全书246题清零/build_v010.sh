#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"
CUM_MD='程稼夫电磁学篇_全习题详解_累计v010_全书246题闭合.md'
CUM_TEX='程稼夫电磁学篇_全习题详解_累计v010_全书246题闭合.tex'
CUM_PDF='程稼夫电磁学篇_全习题详解_累计v010_全书246题闭合.pdf'
INC_MD='程稼夫电磁学篇_v010_新增卷_5-1至6-9.md'
INC_TEX='程稼夫电磁学篇_v010_新增卷_5-1至6-9.tex'
INC_PDF='程稼夫电磁学篇_v010_新增卷_5-1至6-9.pdf'
python3 '程稼夫电磁学篇_v010_公式复算.py'
pandoc "$INC_MD" --from='markdown+tex_math_single_backslash+raw_tex' --standalone --toc --number-sections --pdf-engine=xelatex --resource-path='.:assets_v010' -o "$INC_TEX"
for pass in 1 2 3; do xelatex -interaction=nonstopmode -halt-on-error "$INC_TEX" > "build_inc_pass${pass}.log"; done
pandoc "$CUM_MD" --from='markdown+tex_math_single_backslash+raw_tex' --standalone --toc --number-sections --pdf-engine=xelatex --resource-path='.:assets:assets_v006:assets_v007:assets_v008:assets_v009:assets_v010:figures:figures_v004' -o "$CUM_TEX"
for pass in 1 2 3; do xelatex -interaction=nonstopmode -halt-on-error "$CUM_TEX" > "build_cum_pass${pass}.log"; done
pdfinfo "$CUM_PDF" > PDF_Info_v010_cumulative.txt
pdfinfo "$INC_PDF" > PDF_Info_v010_increment.txt
pdffonts "$CUM_PDF" > PDF_Fonts_v010_cumulative.txt
pdffonts "$INC_PDF" > PDF_Fonts_v010_increment.txt
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage "$CUM_PDF"
gs -q -dNOPAUSE -dBATCH -sDEVICE=nullpage "$INC_PDF"
python /home/oai/skills/pdfs/scripts/pdf_preflight.py "$CUM_PDF" > PDF_Preflight_v010_cumulative.json
python /home/oai/skills/pdfs/scripts/pdf_preflight.py "$INC_PDF" > PDF_Preflight_v010_increment.json
echo 'v010 rebuild complete'
