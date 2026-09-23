# v003 编译与复现说明

## 范围

- 父版：v002，第1章1.1--1.33；
- 本轮新增：第2章2.1--2.21；
- 累计：54道正式题、162道变式、216个训练单元。

## 流程

```bash
python source/generate_figures_v003.py
python source/build_markdown_v003.py
python source/verify_v003.py
pandoc "梁铨廷_物理光学学习指导与题解_累计v003_第1至2章整章闭合.md" --standalone -o build/main_v003.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v003.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v003.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v003.tex
python /home/oai/skills/pdfs/scripts/render_pdf.py "梁铨廷_物理光学学习指导与题解_累计v003_第1至2章整章闭合.pdf" --out_dir rendered_png --dpi 100 --fmt png
python source/pdf_audit_v003.py
```

## 验收

- 数值/恒等式/结构：803/803 PASS；
- XeLaTeX最终遍警告0；
- PDF 158页，全页渲染；
- 严格pypdf、PyMuPDF、Ghostscript、xref/startxref/唯一EOF通过；
- 字体全部嵌入；当前环境无qpdf，未虚报qpdf结果。
