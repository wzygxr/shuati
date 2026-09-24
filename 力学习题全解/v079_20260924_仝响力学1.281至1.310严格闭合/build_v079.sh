#!/usr/bin/env bash
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
PREV="/mnt/data/tong_mechanics_v078/TX-v013_20260924_仝响力学1.251至1.280严格闭合/力学习题全解_仝响专线_累计_TX-v013.pdf"
cd "$HERE"

python build_content_v079.py
python formula_checks_v079.py > formula_checks_v079.txt

pandoc '第93分册_仝响力学_题1.281至1.310_严格全解_TX-v014.md' \
  --pdf-engine=xelatex -o '第93分册_仝响力学_题1.281至1.310_严格全解_TX-v014.pdf'

for stem in 'v079_更新与交付摘要' '尚未解决的书与题目总账_v079' '质量与编译验收报告_v079' '外部参考与补充阅读_v079'; do
  pandoc "${stem}.md" --pdf-engine=xelatex -o "${stem}.pdf"
done

pdfunite 'v079_更新与交付摘要.pdf' "$PREV" \
  '第93分册_仝响力学_题1.281至1.310_严格全解_TX-v014.pdf' \
  '力学习题全解_仝响专线_累计_TX-v014.pdf'

pdfinfo '第93分册_仝响力学_题1.281至1.310_严格全解_TX-v014.pdf'
pdfinfo '力学习题全解_仝响专线_累计_TX-v014.pdf'
