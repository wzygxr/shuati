#!/usr/bin/env python3
from __future__ import annotations
from pathlib import Path

BASE = Path('/mnt/data/电磁学与电动力学上册_累计全解_v002_第1至2章1.1-2.24.md')
WORK = Path('/mnt/data/em_project_v003/work')
OUT = Path('/mnt/data/电磁学与电动力学上册_累计全解_v003_第1至3章1.1-3.15.md')

base = BASE.read_text(encoding='utf-8')
start = base.index('# 真空中的静电场')
end = base.index('# 累计公式复算与质量检查')
body12 = base[start:end].rstrip()
body12 = body12.replace('本轮对关键积分、导数、边界连续性和数值进行了可复现的符号/数值复算。脚本 `formula_checks_v001.py`\n共执行', '本轮对关键积分、导数、边界连续性和数值进行了可复现的符号/数值复算。脚本\n\n`formula_checks_v001.py`\n\n共执行')
chapter3 = '\n\n'.join((WORK/f'chapter3_part{i}.md').read_text(encoding='utf-8').strip() for i in (1,2,3))

front = r'''---
title: "《电磁学与电动力学（第二版，上册）》课后习题累计全解"
subtitle: "v003：第1章1.1—1.21、第2章2.1—2.24、第3章3.1—3.15连续闭合"
author: "逐题详解累计工程"
date: "2026年9月23日"
lang: zh-CN
documentclass: ctexbook
papersize: a4
classoption:
  - oneside
  - openany
  - fontset=none
geometry:
  - top=20mm
  - bottom=22mm
  - left=20mm
  - right=20mm
fontsize: 11pt
linestretch: 1.16
toc: true
toc-depth: 2
numbersections: true
CJKmainfont: "Noto Serif CJK SC"
CJKoptions:
  - AutoFakeSlant=0.15
CJKsansfont: "Noto Sans CJK SC"
CJKmonofont: "Noto Sans Mono CJK SC"
mainfont: "DejaVu Serif"
sansfont: "DejaVu Sans"
monofont: "DejaVu Sans Mono"
colorlinks: true
linkcolor: blue
urlcolor: blue
header-includes:
  - |
    \usepackage{amsmath,amssymb}
    \usepackage{booktabs,longtable,array,tabularx}
    \usepackage{graphicx,float}
    \usepackage{xcolor}
    \usepackage[most]{tcolorbox}
    \usepackage{enumitem}
    \usepackage{microtype}
    \usepackage{fancyhdr}
    \usepackage{siunitx}
    \sisetup{per-mode=symbol,detect-all}
    \makeatletter
    \renewcommand{\@pnumwidth}{3.5em}
    \renewcommand{\@tocrmarg}{4.5em}
    \makeatother
    \definecolor{DeepBlue}{RGB}{19,64,112}
    \definecolor{SoftBlue}{RGB}{237,245,252}
    \definecolor{SoftGold}{RGB}{252,247,232}
    \definecolor{SoftRed}{RGB}{253,240,240}
    \newtcolorbox{keybox}{colback=SoftBlue,colframe=DeepBlue,boxrule=0.6pt,arc=1.5mm,left=2mm,right=2mm,top=1mm,bottom=1mm}
    \newtcolorbox{warnbox}{colback=SoftRed,colframe=red!60!black,boxrule=0.6pt,arc=1.5mm,left=2mm,right=2mm,top=1mm,bottom=1mm}
    \newtcolorbox{checkBox}{colback=SoftGold,colframe=orange!70!black,boxrule=0.6pt,arc=1.5mm,left=2mm,right=2mm,top=1mm,bottom=1mm}
    \pagestyle{fancy}
    \fancyhf{}
    \fancyhead[L]{\small 电磁学与电动力学（上册）累计全解 v003}
    \fancyhead[R]{\small 第1—3章 1.1—3.15}
    \fancyfoot[C]{\small 第 \thepage 页}
    \setlength{\headheight}{14pt}
    \setlist{nosep,leftmargin=2em}
    \setcounter{secnumdepth}{3}
---

\frontmatter

# 版本说明与本轮边界 {-}

本稿是连续累计正文。v003 完整保留 v002 的第1章21题和第2章24题，本轮不重复返工旧题，直接新增第3章正式习题 **3.1—3.15 共15题** 的逐题详解。现在第1—3章连续闭合。

每道正式题均包含：题意重述、所用知识、自然思路、逐步推导、结果解释、量纲或极限自检、易错点、两道本质不同变式，以及一道同类巩固题。代数、积分和数值检查在写作同时完成，没有另起一轮重复审计。

| 章节 | 题号范围 | 正式题数 | 累计完成 |
|:--:|:--:|--:|--:|
| 第1章 真空中的静电场 | 1.1—1.21 | 21 | **21** |
| 第2章 静电场中的导体和电介质 | 2.1—2.24 | 24 | **24** |
| 第3章 静电能 | 3.1—3.15 | 15 | **15** |
| 第4章 稳恒电流 | 4.1—4.15 | 15 | 0 |
| 第5章 真空中的静磁场 | 5.1—5.18 | 18 | 0 |
| 第6章 静磁场中的磁介质 | 6.1—6.22 | 22 | 0 |
| 第7章 电磁感应 | 7.1—7.16 | 16 | 0 |
| 第8章 磁能 | 8.1—8.7 | 7 | 0 |
| 第9章 交流电路 | 9.1—9.10 | 10 | 0 |
| 第10章 麦克斯韦电磁理论 | 10.1—10.10 | 10 | 0 |
| **合计** | 1.1—10.10 | **158** | **60** |

所以本版完成度为
$$
\frac{60}{158}\approx 37.97\%,
$$
剩余 **98题**，下一连续断点为 **4.1**。

\begin{keybox}
\textbf{完整页数口径。}
教材原始 PDF 为332页，科大《答案与解题提示》为20页。本稿直接使用完整 PDF 页树。第3章题面来自教材印刷第264--266页，书后答案来自印刷第285--286页，科大提示第7--8页只作交叉核对。150页只是某些预览接口的显示上限，不是文件终点。
\end{keybox}

## 本版新增的关键纠错与证据边界 {-}

1. **3.4是经典电子半径模型，不是现代实验意义下的电子几何半径。** 本文把“薄球壳”和“均匀实心球”两种自能模型分别推导，并说明经典模型的适用边界。
2. **3.6是纯静电液滴式裂变估算。** 它得到约 $293\,\mathrm{MeV}$ 的库仑能释放，只说明数量级和机制；真实核裂变还包含表面能、壳效应、形变路径、中子与中微子等贡献，不能把模型数值冒充实验裂变能。
3. **3.9严格区分两个优化目标。** 最大耐压给 $R_1=R_2/e$；最大单位长度储能给 $R_1=R_2/\sqrt e$。二者不是同一道极值问题。
4. **3.11—3.13把固定电压与固定自由电荷分开。** 固定电压时电池参与能量交换，机械力不能简单写成 $-d(\frac12CV^2)/dx$；本文从虚功或完整能量平衡得到正确符号。
5. **3.14的像电荷能量含二分之一因子。** 求力可以让真实电荷直接受像电荷场作用；求真实体系装配能时，像电荷对的库仑能必须乘 $1/2$。
6. **3.15区分正的感应电荷自能与负的全系统能量变化。** 二者计入的能量组成不同，不可相互替代。

## 全书统一记号 {-}

除题目另有说明外，记
$$
k_{\rm e}=\frac1{4\pi\varepsilon_0}.
$$
电势记作 $V$ 或 $\varphi$；教材有时使用 $U$ 表示电势或电势差，正文会在局部说明。电容器两极板的自由电荷量记作 $\pm Q$。静电能统一以从无穷远或从未充电状态准静态装配为基准。

\mainmatter
'''

tail = r'''

\clearpage

# 累计公式复算与质量检查 {-}

三个章节的复算脚本相互独立，以便定位问题：

| 章节 | 检查项目 | 结果 |
|:--:|--:|:--:|
| 第1章 | 19 | 19/19 PASS |
| 第2章 | 55 | 55/55 PASS |
| 第3章 | 49 | 49/49 PASS |
| **累计** | **123** | **123/123 PASS** |

第3章脚本覆盖：三电荷中点稳定性、六边形15对电荷的分类计数及相邻/对顶刚性电荷对移走功、球形连续电荷自能积分、同心球接线前后能量、铀核裂变静电模型、同轴电容器场能积分、两种抗击穿极值、固定电压/固定电荷介质力、像电荷能量二分之一因子，以及导体球感应偶极矩和自能。详细逐项输出见随稿附带的 `公式复算报告_v003.txt`。

这些检查用于发现代数、积分、单位和极限错误，但不把“脚本通过”冒充物理证明；正文仍给出完整推导。

# 剩余题目总账 {-}

## 本书精确剩余

| 章节 | 尚未完成题号 | 剩余数 |
|:--:|:--|--:|
| 第4章 稳恒电流 | 4.1—4.15 | 15 |
| 第5章 真空中的静磁场 | 5.1—5.18 | 18 |
| 第6章 静磁场中的磁介质 | 6.1—6.22 | 22 |
| 第7章 电磁感应 | 7.1—7.16 | 16 |
| 第8章 磁能 | 8.1—8.7 | 7 |
| 第9章 交流电路 | 9.1—9.10 | 10 |
| 第10章 麦克斯韦电磁理论 | 10.1—10.10 | 10 |
| **合计** | **下一题4.1，至10.10** | **98** |

本书下一连续断点是
$$
\boxed{\text{第4章 4.1}}.
$$

## 电磁学大项目的其他未完成主线

下表只记录仍需做的新题。已经闭合的赵凯华316题、沈克琦既定范围521题、《大学物理通用教程·电磁学》126题以及贾起民《电磁学（第二版）》363题，不再重复计入。

| 书目 | 最新可核边界 | 后续未完成 |
|:--|:--|:--|
| 仝响《物理奥林匹克竞赛大题典·电磁学卷》 | 3.1—3.22已完成，22/295 | 从3.23继续，剩余273题 |
| 程稼夫《中学奥林匹克竞赛物理教程·电磁学篇》 | 1-1—1-25已完成，25/246 | 从1-26继续，剩余221题 |
| 江四喜《物理竞赛专题精编》电磁部分 | 专题51的51-1—51-6已完成 | 从52-1继续，尚余专题52—71 |
| 郭硕鸿《电动力学（第三版）》 | 完整304页原书已收集 | 冻结全书正式习题分母，并从第1章首题连续推进 |
| 范小辉两书电磁部分 | 第8—11章尚未纳入统一电磁账 | 冻结正式题号、跨书去重并连续完成 |

没有冻结完整题号分母的书，不虚报完成比例。

# 补充阅读与交叉核对来源 {-}

正式题面只以用户提供的完整教材为准；科大20页提示和教材书后答案用于核对结果。为了把“为什么这样做”和适用条件讲清楚，本章还交叉参考以下公开教学与数据来源：

1. OpenStax, *University Physics, Volume 2*, §8.3 “Energy Stored in a Capacitor”：$W=Q^2/(2C)=QV/2=CV^2/2$ 与真空场能密度 $u=\varepsilon_0E^2/2$；
2. MIT OpenCourseWare 6.641, *Energy, Power Flow, and Forces*：场能、功率流、宏观电磁力和能量守恒；
3. MIT OpenCourseWare 6.641, Lecture 5 “Method of Images”：接地平面、球面像电荷与感应面电荷；
4. NIST 2022 CODATA：元电荷、光速和其他基本常数；
5. IAEA/INIS, *Uranium and plutonium energy release per fission event in a nuclear reactor*：用于说明3.6静电液滴模型与真实裂变能核算的区别。

公开链接：

- <https://openstax.org/books/university-physics-volume-2/pages/8-3-energy-stored-in-a-capacitor>
- <https://www.ocw.mit.edu/courses/6-641-electromagnetic-fields-forces-and-motion-spring-2005/resources/11/>
- <https://ocw.mit.edu/courses/6-641-electromagnetic-fields-forces-and-motion-spring-2005/resources/lecture5/>
- <https://physics.nist.gov/cuu/Constants/>
- <https://inis.iaea.org/records/2c11w-h7h06>

这些资料用于补足概念、边界条件、能量符号和模型范围，不替代原书题面，也不照搬现成答案。

\appendix

# 题源证据页

以下页图来自完整332页教材以及20页科大提示，均绕过150页预览层直接由原始 PDF 页树渲染。

## 第1章题源与书后核对页

![教材印刷第257页：1.1—1.7](images/source_p257.png){width=82%}

\clearpage

![教材印刷第258页：1.8—1.13](images/source_p258.png){width=82%}

\clearpage

![教材印刷第259页：1.13续—1.18](images/source_p259.png){width=82%}

\clearpage

![教材印刷第260页：1.19—1.21及第2章起点](images/source_p260.png){width=82%}

\clearpage

![教材答案页283](images/source_ans_p283.png){width=82%}

\clearpage

![教材答案页284](images/source_ans_p284.png){width=82%}

\clearpage

## 全书习题页接触表

下图覆盖教材印刷第264—283页，显示第3—10章习题的连续章界以及书后参考答案起点，用于校验全书题号分母。

![全书习题页接触表](images/full_exercises_contact.jpg){width=80%}

\clearpage

## 第2章完整习题页

![教材印刷第260页：2.1—2.3起点](images/source_p260_ch2_start.png){width=82%}

\clearpage

![教材印刷第261页：2.4—2.10](images/source_p261.png){width=82%}

\clearpage

![教材印刷第262页：2.10续—2.17](images/source_p262.png){width=82%}

\clearpage

![教材印刷第263页：2.17续—2.21](images/source_p263.png){width=82%}

\clearpage

![教材印刷第264页：2.22—2.24及第3章起点](images/source_p264.png){width=82%}

\clearpage

![科大提示第4页](images/source_hint_p4.png){width=82%}

\clearpage

![科大提示第5页](images/source_hint_p5.png){width=82%}

\clearpage

![科大提示第6页](images/source_hint_p6.png){width=82%}

\clearpage

## 第3章完整习题页与答案核对页

![教材印刷第264页下半：第3章3.1—3.4起点](images/source_ch3_book_p280.png){width=82%}

\clearpage

![教材印刷第265页：3.4续—3.12起点](images/source_ch3_book_p281.png){width=82%}

\clearpage

![教材印刷第266页：3.12续—3.15及第4章起点](images/source_ch3_book_p282.png){width=82%}

\clearpage

![教材书后答案第285页：3.1—3.13](images/source_book_answer_p301.png){width=82%}

\clearpage

![教材书后答案第286页：3.14—3.15](images/source_book_answer_p302.png){width=82%}

\clearpage

![科大提示第7页：第3章前半](images/source_hint_p7.png){width=82%}

\clearpage

![科大提示第8页：第3章后半](images/source_hint_p8.png){width=82%}

\backmatter

# v003版本记录 {-}

- 完成日期：2026年9月23日；
- 本版新增正式原题：15（3.1—3.15）；
- 累计正式原题：60；
- 累计本质不同变式：120；
- 累计同类巩固题：60；
- 累计训练单元：240；
- 全书正式题分母：158；
- 本书累计完成：60；
- 本书剩余：98；
- 下一断点：4.1；
- 第3章公式/数值复算：49/49 PASS；
- 三章独立复算累计：123/123 PASS；
- PDF验收：以最终编译返回码、`pdfinfo`、`qpdf`/Ghostscript 结构检查、尾部 `%%EOF`、字体嵌入、全页渲染和代表页图像检查为准。
'''

out = front.rstrip() + '\n\n' + body12 + '\n\n\\clearpage\n\n' + chapter3 + '\n' + tail
out = out.replace('\x0c', '\\')
_normalized: list[str] = []
_in_fence = False
for _line in out.splitlines():
    _stripped = _line.lstrip()
    if _stripped.startswith('```') or _stripped.startswith('~~~'):
        _in_fence = not _in_fence
    if (not _in_fence and _line.startswith('#') and len(_line) > 1
            and _line.lstrip('#').startswith(' ')
            and _normalized and _normalized[-1].strip()):
        _normalized.append('')
    _normalized.append(_line)
out = '\n'.join(_normalized) + '\n'
OUT.write_text(out, encoding='utf-8')
print(OUT)
print('bytes', OUT.stat().st_size, 'lines', out.count('\n')+1)
