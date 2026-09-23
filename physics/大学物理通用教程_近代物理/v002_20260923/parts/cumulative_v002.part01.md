---
title: "《大学物理通用教程·光学·近代物理》近代物理全习题详解"
subtitle: "Ch5--9 源题锁定累计 v002：60 道唯一题 + 180 道配套题"
author: "基于用户提供的 333 页教材与 312 页配套习题解答，独立推导与数值复核"
date: "2026-09-23"
lang: zh-CN
papersize: a4
geometry: margin=1.75cm
fontsize: 10pt
mainfont: "Noto Serif CJK SC"
sansfont: "Noto Sans CJK SC"
monofont: "DejaVu Sans Mono"
mathfont: "STIX Math"
colorlinks: true
linkcolor: blue
urlcolor: blue
toc: true
toc-depth: 3
numbersections: true
header-includes:
  - |
    \usepackage{ctex}
    \usepackage{amsmath,amssymb,mathtools,bm,booktabs,longtable,array,tabularx}
    \usepackage{graphicx,float,caption,enumitem,microtype}
    \usepackage{fancyhdr,lastpage}
    \pagestyle{fancy}
    \fancyhf{}
    \fancyhead[L]{大学物理通用教程·近代物理全习题 v002}
    \fancyhead[R]{源题锁定：60/60}
    \fancyfoot[C]{\thepage/\pageref{LastPage}}
    \setlength{\headheight}{14pt}
    \setcounter{secnumdepth}{3}
    \setlist{nosep,leftmargin=2em}
    \captionsetup{font=small}
---

\newpage

# 使用说明、版本边界与本轮完成量

本卷不是上一版中“自拟内部题位”的继续堆叠，而是重新回到两份用户提供的完整 PDF，逐页锁定题面：

- 主教材《大学物理通用教程·光学·近代物理》本地物理页数为 **333 页**；连接器只展示到 150 页是预览截断，不代表文件不完整。
- 配套《大学物理通用教程习题解答（第二版）》本地物理页数为 **312 页**。
- 2002 年教材在近代物理部分明确印出的章末题为：第 5 章 19 题、第 6 章 23 题，共 42 题。
- 2016 年第二版习题解答还含 18 道在该 2002 年教材扫描件中没有独立印出题面的新增或重编题。为完整覆盖“配套解答所称近代物理 60 题”，本卷将这 18 题列为**第二版增补题**，不伪装成 2002 年教材原页题。

![两个版次的题号交叉映射](assets/fig_crosswalk.png){width=95%}

## 完成量

| 类型 | 主问题 | 本质不同变式 | 同类巩固 | 训练单元 |
|---|---:|---:|---:|---:|
| 2002 教材第 5 章 | 19 | 38 | 19 | 76 |
| 2002 教材第 6 章 | 23 | 46 | 23 | 92 |
| 2016 解答书增补 | 18 | 36 | 18 | 72 |
| **合计** | **60** | **120** | **60** | **240** |

## 重要纠错

本卷不把书后答案当作证明，只把它当作终点核验。以下几处已明确纠正或加注：

1. **5.11 的 $0.9c$ 项。** 解答书把低速级数的首个修正项 $3\beta^2/4$ 直接用于 $\beta=0.9$，得到 $0.61$。这在高速度处不再是可靠近似。按其自己采用的分母 $K_{{\rm cl}}$，精确相对误差为约 $2.20$；若以真值 $K_{\rm rel}$ 为分母，则为约 $68.7\%$。
2. **增补题 2.23。** 解答书计算铅核缪子原子时漏掉了核电荷数 $Z=82$ 的 $Z^2$ 与 $Z$ 因子，实际点核模型的能级、半径和谱线与书中结果相差巨大；同时缪子轨道半径已与铅核尺度可比，必须提醒有限核尺寸修正。
3. **增补题 3.12。** 从给定截断波函数在开区间内反演出的普通函数势能是局部结果；波函数在 $|x|=a$ 处导数跳跃，因而不存在一个普通乘法势使该分段函数在全实线上严格满足通常的薛定谔方程。
4. **6.23。** 教材用 $\Delta E\,\Delta t\sim h$ 得到数量级寿命；若“线宽”特指指数衰减所对应洛伦兹线型的半高全宽，则会多一个 $2\pi$ 因子。本卷同时给出两种约定。

## 常数与舍入

主答案优先复现教材的有效数字；独立复算采用 $c=299\,792\,458\,\mathrm{m/s}$、$h=6.62607015\times10^{-34}\,\mathrm{J\,s}$、$e=1.602176634\times10^{-19}\,\mathrm C$ 等 2022 CODATA 值。二者只在末位舍入上不同。

## 题源页接触表

![第5章题源：PDF 物理页191](assets/source_p191.jpg){width=47%}
![第5章题源：PDF 物理页192](assets/source_p192.jpg){width=47%}

![第5章题源：PDF 物理页193](assets/source_p193.jpg){width=47%}
![第5章题源：PDF 物理页194](assets/source_p194.jpg){width=47%}

![第6章题源：PDF 物理页254](assets/source_p254.jpg){width=31%}
![第6章题源：PDF 物理页255](assets/source_p255.jpg){width=31%}
![第6章题源：PDF 物理页256](assets/source_p256.jpg){width=31%}

\newpage

# 第5章　相对论：教材 5.1--5.19

![洛伦兹变换的时空图读法](assets/fig_spacetime.png){width=70%}

## 5.1　洛伦兹变换下的事件坐标与时间间隔

> **题源定位：** 主教材 PDF 物理页191（印刷页182）

### 题意重述

$S'$ 相对 $S$ 沿 $+x$ 方向以 $v=0.600c$ 运动。求事件 $(x_1,t_1)=(50.0\,\mathrm m,2.00\times10^{-7}\,\mathrm s)$ 在 $S'$ 中的时刻；再求第二事件 $(10.0\,\mathrm m,3.00\times10^{-7}\,\mathrm s)$ 与第一事件在 $S'$ 中的时间间隔。

### 逐步解答

取 $\beta=0.600$，
$$
\gamma=\frac1{\sqrt{1-\beta^2}}=\frac1{0.8}=1.25.
$$
洛伦兹变换为
$$
x'=\gamma(x-vt),\qquad t'=\gamma\left(t-\frac{vx}{c^2}\right).
$$
第一事件满足
$$
\frac{vx_1}{c^2}=\beta\frac{x_1}{c}=0.600\frac{50.0}{3.00\times10^8}
=1.00\times10^{-7}\,\mathrm s,
$$
所以
$$
t_1'=1.25(2.00-1.00)\times10^{-7}
=\boxed{1.25\times10^{-7}\,\mathrm s}.
$$
第二事件为
$$
t_2'=1.25\left(3.00\times10^{-7}-0.600\frac{10.0}{3.00\times10^8}\right)
=3.50\times10^{-7}\,\mathrm s.
$$
因此
$$
\Delta t'=t_2'-t_1'=\boxed{2.25\times10^{-7}\,\mathrm s}.
$$
这里不能只把 $S$ 系的 $1.00\times10^{-7}\,\mathrm s$ 乘以 $\gamma$，因为两个事件并不在同一地点；时间变换中还必须保留 $-vx/c^2$。

### 易错点与自检

- 先写出适用条件，再代数值；单位一律换成 SI 或明确使用 eV、Å、MeV 的一致组合。
- 结果同时做量纲、数量级和极限检查。若与配套解答不同，本卷在正文中明确给出差异来源。


### 配套变式

**变式 A（逆变换设计题，参数组 01）。** 某事件在 $S$ 中为
$x=21.0\,\mathrm{m}$、$t=1.280\times10^{-7}\,\mathrm{s}$，$S'$ 以
$v=0.24c$ 运动。先求 $(x',t')$，再用逆变换还原。  
**解。** $\gamma=1.03011$，
$$
x'=\gamma(x-vt)=12.139\,\mathrm{m},\qquad
t'=\gamma\!\left(t-\frac{vx}{c^2}\right)=1.145\times10^{-7}\,\mathrm{s}.
$$
代入 $x=\gamma(x'+vt')$、$t=\gamma(t'+vx'/c^2)$ 可恢复原事件；这一步同时检查符号。

**变式 B（本质不同：因果类型）。** 两事件满足
$c\Delta t=3.0\,\mathrm{m}$、$\Delta x=5.0\,\mathrm{m}$。能否找到同地系？能否找到同时系？  
**解。** 因 $|\Delta x|>|c\Delta t|$，间隔为类空间；不能找到同地系，但可取
$v=c^2\Delta t/\Delta x=0.600c$ 使两事件同时。

**同类巩固。** $S'$ 以 $0.34c$ 运动，$S$ 中事件为
$x=17.0\,\mathrm{m}$、$t=1.450\times10^{-7}\,\mathrm{s}$。  
**答。** $x'=2.350\,\mathrm{m}$，$t'=1.337\times10^{-7}\,\mathrm{s}$。

## 5.2　寻找使两事件同时发生的参考系

> **题源定位：** 主教材 PDF 物理页191（印刷页182）

### 题意重述

在 $S$ 中两事件为 $(x_0,x_0/c)$ 与 $(2x_0,x_0/(2c))$。求使两事件同时发生的惯性系速度，并求新系中的共同发生时刻。

### 逐步解答

先计算差量：
$$
\Delta x=x_2-x_1=x_0,\qquad
\Delta t=t_2-t_1=-\frac{x_0}{2c}.
$$
在速度为 $v$ 的新系中，
$$
\Delta t'=\gamma\left(\Delta t-\frac{v\Delta x}{c^2}\right).
$$
令 $\Delta t'=0$，得
$$
v=\frac{c^2\Delta t}{\Delta x}=-\frac12c.
$$
负号说明该系应沿 $-x$ 方向运动。此时 $\gamma=2/\sqrt3$。对任一事件作时间变换，例如事件1：
$$
t_1'=\gamma\left(\frac{x_0}{c}-\frac{(-c/2)x_0}{c^2}\right)
=\frac{2}{\sqrt3}\frac{3x_0}{2c}
=\frac{\sqrt3x_0}{c}.
$$
事件2给出同样结果。因此
$$
\boxed{v=-0.500c},\qquad
\boxed{t_1'=t_2'=\sqrt3\,x_0/c}.
$$
还可用间隔检查：原间隔为类空间隔，所以确实存在一个“同时系”，但不存在使两事件同地发生的系。

### 易错点与自检

- 先写出适用条件，再代数值；单位一律换成 SI 或明确使用 eV、Å、MeV 的一致组合。
- 结果同时做量纲、数量级和极限检查。若与配套解答不同，本卷在正文中明确给出差异来源。


### 配套变式

**变式 A（反求参考系速度，参数组 02）。** 两事件在 $S$ 中同时发生，间距
$400.0\,\mathrm{m}$；在 $S'$ 中间距为 $560.0\,\mathrm{m}$。求 $|v|$。  
**解。** 对同时事件 $\Delta x'=\gamma\Delta x$，故
$\gamma=1.400$，
$$
|v|=c\sqrt{1-\gamma^{-2}}=0.6999c.
$$
注意这不是“尺缩”直接套用，因为这里比较的是一对事件；结论来自洛伦兹变换。

**变式 B（本质不同：固有时）。** 某钟相邻两次滴答的固有时为
$1.50\,\mu\mathrm{s}$，实验室测得 $2.250\,\mu\mathrm{s}$。求钟速。  
**解。** $\gamma=\Delta t/\Delta\tau=1.500$，所以
$v=0.7454c$。固有时必须由同一地点发生的两事件给出。

**同类巩固。** 两事件同地系的固有时间为 $2.50\,\mathrm{s}$，另一系测得
$\Delta t=7.00\,\mathrm{s}$。  
**答。** $|\Delta x|=c\sqrt{\Delta t^2-\Delta\tau^2}
=1.962\times10^{9}\,\mathrm{m}$。

## 5.3　由固有时间求另一系的空间间隔

> **题源定位：** 主教材 PDF 物理页191（印刷页182）

### 题意重述

两事件在某惯性系中同地发生，时间间隔为 $4.00\,\mathrm s$；另一惯性系测得时间间隔为 $6.00\,\mathrm s$。求后一惯性系中的空间间隔。

### 逐步解答

同地发生的时间间隔就是固有时间 $\Delta\tau=4.00\,\mathrm s$。时空间隔不变量给出
$$
c^2\Delta\tau^2=c^2\Delta t'^2-\Delta x'^2.
$$
故
$$
|\Delta x'|=c\sqrt{\Delta t'^2-\Delta\tau^2}
=c\sqrt{6.00^2-4.00^2}
=c\sqrt{20}.
$$
代入 $c=3.00\times10^8\,\mathrm{m/s}$，
$$
\boxed{|\Delta x'|=1.34\times10^9\,\mathrm m}.
$$
因为另一系的时间比固有时间长，这也符合时间膨胀。若根号内出现负数，则说明把“同地”或参考系角色认反了。

### 易错点与自检

- 先写出适用条件，再代数值；单位一律换成 SI 或明确使用 eV、Å、MeV 的一致组合。
- 结果同时做量纲、数量级和极限检查。若与配套解答不同，本卷在正文中明确给出差异来源。


### 配套变式

**变式 A（反求参考系速度，参数组 03）。** 两事件在 $S$ 中同时发生，间距
$425.0\,\mathrm{m}$；在 $S'$ 中间距为 $637.5\,\mathrm{m}$。求 $|v|$。  
**解。** 对同时事件 $\Delta x'=\gamma\Delta x$，故
$\gamma=1.500$，
$$
|v|=c\sqrt{1-\gamma^{-2}}=0.7454c.
$$
注意这不是“尺缩”直接套用，因为这里比较的是一对事件；结论来自洛伦兹变换。

**变式 B（本质不同：固有时）。** 某钟相邻两次滴答的固有时为
$1.75\,\mu\mathrm{s}$，实验室测得 $2.800\,\mu\mathrm{s}$。求钟速。  
**解。** $\gamma=\Delta t/\Delta\tau=1.600$，所以
$v=0.7806c$。固有时必须由同一地点发生的两事件给出。

**同类巩固。** 两事件同地系的固有时间为 $2.75\,\mathrm{s}$，另一系测得
$\Delta t=8.00\,\mathrm{s}$。  
**答。** $|\Delta x|=c\sqrt{\Delta t^2-\Delta\tau^2}
=2.254\times10^{9}\,\mathrm{m}$。

## 5.4　同时性的相对性：由长度变换求时差

> **题源定位：** 主教材 PDF 物理页191（印刷页182）

### 题意重述

两事件在 $S$ 中同时发生且沿 $x$ 轴相距 $1.00\,\mathrm{km}$；在 $S'$ 中它们相距 $2.00\,\mathrm{km}$。求 $S'$ 中两事件的时间差。

### 逐步解答

因 $\Delta t=0$，空间变换化为
$$
\Delta x'=\gamma\Delta x.
$$
于是 $\gamma=2$，从而
$$
\beta=\sqrt{1-\frac1{\gamma^2}}=\frac{\sqrt3}{2}.
$$
时间差为
$$
\Delta t'=\gamma\left(0-\frac{v\Delta x}{c^2}\right)
=-\gamma\beta\frac{\Delta x}{c}
=-\sqrt3\frac{1000}{3.00\times10^8}\,\mathrm s.
$$
因此
$$
\boxed{\Delta t'=-5.77\times10^{-6}\,\mathrm s}.
$$
