# 梁铨廷《物理光学》累计全解 v004：编译与复现说明

## 版本范围

- 父版：v003，第1章1.1--1.33、第2章2.1--2.21；
- 本轮新增：第3章3.1--3.31；
- 本轮新增31道正式题、93道本质不同变式、124个训练单元；
- 累计85道正式题、255道变式、340个训练单元；
- 下一连续断点：第4章正式习题4.1。

## 关键源文件

- `source/chapter3_chunk2.md`、`source/chapter3_chunk3.md`；
- `source/v004_tail.md`；
- `source/generate_figures_v004.py`；
- `source/verify_v004.py`；
- `source/pdf_audit_v004.py`。

## 复现命令

```bash
python source/generate_figures_v004.py
python source/assemble_v004.py
python source/verify_v004.py
pandoc "梁铨廷_物理光学学习指导与题解_累计v004_第1至3章整章闭合.md" --standalone -o build/main_v004.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v004.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v004.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/main_v004.tex
python /home/oai/skills/pdfs/scripts/render_pdf.py "梁铨廷_物理光学学习指导与题解_累计v004_第1至3章整章闭合.pdf" --out_dir rendered_png --dpi 100 --fmt png
python source/pdf_audit_v004.py
```

## 最终验收

- 数值、恒等式、极限、压力测试与结构：1952/1952通过；
- XeLaTeX三遍，最终234页稳定；
- Fatal、Missing character、Overfull、Underfull、LaTeX Warning、Package Warning均为0；
- PyMuPDF与严格pypdf均为234页；
- Ghostscript、xref、startxref和唯一尾部EOF通过；
- 234/234页全量渲染，空白页0、边缘告警0；
- 14组字体全部嵌入；当前环境无qpdf，未虚报qpdf结果。
