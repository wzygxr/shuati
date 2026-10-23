# 梁铨廷《物理光学》累计全解 v006：编译与复现

## 版本范围

- v005父版：第1--4章106道正式题；
- v006新增：第5章5.1--5.41，共41道正式题、123道本质不同变式；
- 累计：147道正式题、441道变式、588个训练单元；
- 下一断点：第6章6.1。

## 源文件

- 累计Markdown：`梁铨廷_物理光学学习指导与题解_累计v006_第1至5章整章闭合.md`；
- 第5章增量：`source/chapter5_full.md`；
- 图片目录：`figures/`、`figures_v002/`、`figures_ch2/`、`figures_ch3/`、`figures_ch4/`、`figures_ch5/`；
- 数值复算：`source/verify_v006.py`；
- PDF全页验收：`source/pdf_audit_v006.py`；
- 编译脚本：`source/compile_v006.sh`。

## 编译命令

```bash
bash source/compile_v006.sh
```

累计稿目录和书签很多，XeLaTeX多遍编译耗时较长。最终交付PDF已经由Pandoc/XeLaTeX生成，并通过404页全量渲染、严格pypdf、PyMuPDF、Ghostscript、字体嵌入、xref/startxref和唯一`%%EOF`检查。

## 独立验收

```bash
python source/verify_v006.py
python source/pdf_audit_v006.py
```

最终结果：12,693项复算与结构检查全部通过；404/404页渲染成功；空白页、边缘裁切、文本越界和替换字符均为0。
