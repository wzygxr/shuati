---
title: "ZXH-v057 来源审计与质量验收"
author: "钟锡华《现代光学基础》全习题详解项目"
date: "2026 年 9 月 24 日"
documentclass: ctexart
classoption: [UTF8]
geometry: "a4paper,margin=1.8cm"
mainfont: "Noto Serif CJK SC"
sansfont: "Noto Sans CJK SC"
monofont: "Noto Sans Mono CJK SC"
CJKmainfont: "Noto Serif CJK SC"
fontsize: 10.5pt
colorlinks: true
linkcolor: blue
urlcolor: blue
numbersections: false
header-includes:
  - |
    \usepackage{amsmath,amssymb,booktabs,longtable,array,tabularx,xcolor}
    \usepackage[most]{tcolorbox}
    \newtcolorbox{note}{colback=blue!3,colframe=blue!55!black,breakable}
    \setlength{\parindent}{2em}
    \setlength{\parskip}{0.2em}
---

# 一、源文件与页数边界

| 文件 | 原始页数 | SHA-256 | 本轮题位 |
|---|---:|---|---|
| 《现代光学基础》 | 488 | `9bf22f263e9b44a2c47bac0ef8a008c818ee07e930c01e9817b9684e5d470644` | 物理页441--445 |
| 《现代光学基础题解指导》 | 225 | `5066e025ead2e7c9540dce1a64a8d334ab8b0fe2abcea168687216f00cafd2d5` | 物理页175--208 |

接口显示的150页只属于预览层。题号、题面和数字以原始488页教材为权威；225页题解只用于路线、答案和冲突对照。

两份源文件的哈希与ZXH-v056一致，因此沿用同一二进制文件已经完成的全页渲染证明：教材488/488，题解225/225。不是只检查目录页或最后一页。

# 二、正文覆盖

- 正式题标题：42/42；
- 题号连续性：8.1--8.42无缺号；
- 变式标题：126/126；
- 每题均有三道本质不同变式及答案；
- 图片引用：22/22存在；
- 无控制字符、无损坏的LaTeX右括号命令；
- Markdown数学块成对；
- 累计Markdown共128820行。

# 三、独立复算

验证脚本没有调用正文中的最终数字作为计算源，而是重新代入原始参数计算。覆盖折射与最小偏向、普通光/非常光夹角和走离、射线与波法线折射率、波片厚度、偏振干涉极值、补偿器条纹、圆双折射、旋光、浓度、法拉第效应和Kerr效应。

最终验证结果：**106/106通过，0失败**。

特别地，8.7第一次复算发现草稿中的7.33 mm应为7.199 mm；正文、PDF和变式已经统一修正为7.20 mm后重新编译、重新渲染并重新运行全部检查。

# 四、PDF字节完整性与渲染

| 文件 | 页数 | 全页渲染 | 空白候选 | 黑页候选 | 书签 |
|---|---:|---:|---:|---:|---:|
| 第8章增量卷 | 116 | 116/116 | 0 | 0 | 254 |
| 题面与题解证据册 | 40 | 40/40 | 0 | 0 | 42 |
| 累计母版 | 4304 | 关键页渲染 + 全页内容流载入 | 0个载入失败 | 0个空内容流页 | 8048 |

三份PDF均通过 `%PDF-`、`startxref`、`%%EOF`、`pdfinfo`和PyMuPDF逐页内容流载入。Ghostscript nullpage已对增量卷和证据册全文件运行并返回0；4304页累计母版采用4304/4304内容流载入和关键页实际渲染。当前环境没有安装`qpdf`，未声称执行`qpdf --check`。

# 五、字体与可移植性

增量卷和证据册使用嵌入字体。累计母版继承早期批次的STSong-Light与Heiti未嵌入字体；本轮没有新增字体债。

# 六、外部补充资料

外部资料仅用于核对一般原理：MIT OCW偏振、波片和Snell定律课程；NIST Optical Polarization Metrology、石英/MgF2延迟色散测量和SRM 2538。正文数值均由本项目独立重算。
