# 梁铨廷《物理光学》累计全解 v005：编译与复现说明

## 版本范围

- 父版：v004，第1--3章85道正式题闭合；
- 本轮新增：第4章4.1--4.21，共21道正式题、63道本质不同变式；
- 累计：第1--4章106道正式题、318道变式、424个训练单元；
- 下一断点：第5章5.1。

## 主要输入

- 累计父稿：`parent_v004.md`；
- 第4章正文：`source/chapter4_*.md`；
- 插图：`source/generate_figures_v005.py`；
- 累计构建：`source/build_cumulative_v005.py`；
- 数值与结构复算：`source/verify_v005.py`；
- PDF验收：`source/pdf_audit_v005.py`。

## 验收结果

- 6152 PASS，0 FAIL；
- XeLaTeX干净重编译三遍，第三遍警告0；
- PDF 318页、1195条书签；
- PyMuPDF、严格pypdf、Ghostscript、xref、startxref、唯一EOF通过；
- PDFium全页渲染318/318；空白、裁切、替换字符和文本越界均为0；
- 14组字体全部嵌入；当前环境无qpdf，未虚报qpdf结果。

## 核心哈希

- Markdown：`cbe4ccaa4d2d062a4ad0016435ff9ae62451b86bf3bdfa79d4847717f0c12de2`
- PDF：`dc4600f4f3f45d018e320c526da264b892a413fb117810feedf909fbaff22c4d`
- ZIP：`138472e7275cad30b7de066d467527a8fcdaf419825a0f06e90ae0c664884c25`
