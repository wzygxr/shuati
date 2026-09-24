# 梁铨廷《物理光学》累计全解 v008：编译与复现说明

## 版本范围

- 父版：v007，第1--6章172道正式题闭合；
- 本轮新增：第7章7.1--7.38，共38道正式题、114道本质不同变式；
- 累计：第1--7章210道正式题、630道变式、840个训练单元；
- 教材章末正式题：全书清零。

## 复现流程

```bash
python source/generate_figures_v008.py
python source/build_markdown_v008.py
python source/verify_v008.py
pandoc 第7章_新增卷_v008.md --standalone -o build/ch7_v008.tex
xelatex -interaction=nonstopmode -halt-on-error -output-directory=build build/ch7_v008.tex  # 三遍
python source/build_front_v008.py
python source/merge_v008.py
python /home/oai/skills/pdfs/scripts/render_pdf.py 累计v008.pdf --out_dir rendered_png --dpi 96 --fmt png
python source/pdf_audit_v008.py
```

## 验收结果

- 第7章复算：45655/45655 PASS；
- 第7章增量PDF：83页；
- 累计PDF：510页、2204条书签；
- 累计卷510/510全页渲染；
- 历史正文像素冻结抽查：11/11一致；
- 严格pypdf、PyMuPDF、Ghostscript、字体嵌入、xref/startxref、唯一EOF通过；
- 当前环境无qpdf，未虚报qpdf结果。
