---
title: "光学：IPhO 金牌讲义与全习题详解（丁文革分支增量）"
subtitle: "第2章光的干涉：例2.1--2.18连续闭合"
author: "题源：用户上传218页扫描本；解答：独立推导、数值复算与补充"
date: "2026 年 9 月 23 日"
documentclass: ctexbook
classoption:
  - oneside
  - openany
  - zihao=-4
geometry:
  - a4paper
  - top=20mm
  - bottom=22mm
  - left=20mm
  - right=20mm
mainfont: "Noto Serif CJK SC"
sansfont: "Noto Sans CJK SC"
monofont: "Noto Sans Mono CJK SC"
CJKmainfont: "Noto Serif CJK SC"
CJKsansfont: "Noto Sans CJK SC"
CJKmonofont: "Noto Sans Mono CJK SC"
colorlinks: true
linkcolor: "RoyalBlue"
urlcolor: "RoyalBlue"
toc: true
toc-depth: 2
numbersections: false
fontsize: 11pt
header-includes:
  - |
    \usepackage{amsmath,amssymb,mathtools,bm,booktabs,longtable,array,tabularx,multirow}
    \usepackage{graphicx,float,caption,subcaption}
    \setkeys{Gin}{keepaspectratio}
    \usepackage{xcolor,fancyhdr,enumitem}
    \usepackage[most]{tcolorbox}
    \usepackage{siunitx}
    \usepackage{xurl}
    \urlstyle{same}
    \sisetup{per-mode=symbol,detect-all}
    \definecolor{deepblue}{HTML}{1F4E79}
    \definecolor{softblue}{HTML}{EAF2F8}
    \definecolor{softgreen}{HTML}{EAF6EF}
    \definecolor{softorange}{HTML}{FFF4E6}
    \definecolor{softred}{HTML}{FDECEC}
    \definecolor{softpurple}{HTML}{F3ECFA}
    \definecolor{softyellow}{HTML}{FFFBE6}
    \newtcolorbox{corebox}{colback=softblue,colframe=deepblue,title=本题核心,breakable,fonttitle=\bfseries}
    \newtcolorbox{goldbox}{colback=softorange,colframe=orange!70!black,title=自然思路,breakable,fonttitle=\bfseries}
    \newtcolorbox{mistakebox}{colback=softred,colframe=red!70!black,title=高频失分点,breakable,fonttitle=\bfseries}
    \newtcolorbox{checkbox}{colback=softgreen,colframe=green!60!black,title=自检,breakable,fonttitle=\bfseries}
    \newtcolorbox{correctionbox}{colback=softpurple,colframe=purple!70!black,title=原书答案复核与更正,breakable,fonttitle=\bfseries}
    \newtcolorbox{extensionbox}{colback=gray!7,colframe=gray!60,title=补充材料,breakable,fonttitle=\bfseries}
    \newtcolorbox{sourcebox}{colback=softyellow,colframe=yellow!55!black,title=题源定位,breakable,fonttitle=\bfseries}
    \pagestyle{fancy}
    \fancyhf{}
    \fancyhead[L]{光学：IPhO 金牌讲义与全习题详解}
    \fancyhead[R]{丁文革 DING-C2-v001：例2.1--2.18}
    \fancyfoot[C]{\thepage}
    \setlength{\headheight}{15pt}
    \setlist[itemize]{leftmargin=2em,itemsep=0.2em,topsep=0.3em}
    \setlist[enumerate]{leftmargin=2.3em,itemsep=0.25em,topsep=0.3em}
    \setlength{\parindent}{2em}
    \setlength{\parskip}{0.25em}
    \emergencystretch=3em
    \renewcommand{\arraystretch}{1.25}
---

\frontmatter

# DING-C2-v001 本轮交付边界

本轮从上一批唯一连续断点继续，不回头做二轮审计，直接处理丁文革《光学指导·考研参考书》第2章“真题分析与解答”中的

$$
\boxed{\text{例2.1--例2.18}}.
$$

这18个题位分布在完整扫描母本的物理页45--59（原书印刷页38--52附近）。本轮每道正式例题均配置3道专属训练：一道改变参数或边界条件，一道改变模型结构，一道强调逆问题、实验设计或概念辨析。因此本批新增

$$
18\text{ 道正式例题}+54\text{ 道变式}=\boxed{72\text{ 个训练单元}}.
$$

::: sourcebox
下图是物理页45--58的题源接触表。正式题号、原书参数、图号和原书解答均先从扫描页逐页恢复；本卷的详细推导和数值结果再独立复算。网络资料只用于核验一般规律，不替代书中题面。
:::

\begin{center}
\includegraphics[width=.97\textwidth]{figures_v054/source_contact_p45_58.jpg}
\end{center}

# 本批最重要的证据边界与勘误

| 题位 | 原书或常见口径 | 本卷处理 |
|---|---|---|
| 例2.5 | 原书把“暗条纹角宽度”解释为暗纹中心到相邻亮纹中心的半周期 | 明确写出该约定；若理解为相邻暗纹中心间距，答案会相差2倍 |
| 例2.7 | 原书最后给出的含色散条纹位置式，$A/\lambda^2$ 项少了一个10倍因子 | 从光程差方程重新推导并给出可复算更正式 |
| 例2.11 | 原书采用 $d\,\Delta\theta\sim\lambda$ 的简化空间相干判据 | 保留原书数量级答案；另说明均匀圆盘源的精确第一零点含 $1.22$ 因子 |
| 例2.13 | 原书把第11暗纹残余强度比算成约 $0.0026$ | 依原书自己列出的积分式复算应约为 $0.00535$，原值少约2倍 |
| 例2.14 | “薄膜很薄便无干涉条纹” | 精确说法是：均匀薄膜不给出空间条纹；楔形或厚度有梯度时仍会出现条纹 |
| 例2.15 | 原书把最小厚度先粗略取 $0.11\,\mu$m，再算正视颜色为 $586.7$ nm | 用未提前舍入的厚度复算约为 $590$ nm；两者均处于黄光附近 |

# 干涉题的统一基础

## 1. 两束相干光的强度

若两束光的复振幅为

$$
\tilde E_1=A_1e^{i\phi_1},\qquad
\tilde E_2=A_2e^{i\phi_2},
$$

则

$$
I=I_1+I_2+2\sqrt{I_1I_2}\cos\delta,
\qquad
\delta=\phi_2-\phi_1.
$$

可见度

$$
\gamma=\frac{I_{\max}-I_{\min}}{I_{\max}+I_{\min}}
=\frac{2\sqrt{I_1I_2}}{I_1+I_2}
$$

在两束强度相等时达到1。

## 2. 光程差、相位差与条纹移动

$$
\delta=\frac{2\pi}{\lambda}\Delta,
$$

其中 $\Delta$ 是光程差。插入厚度 $h$、折射率 $n$ 的平行薄片，相对空气新增光程

$$
\Delta_{\rm plate}=(n-1)h.
$$

相当于移动的条纹数为

$$
N=\frac{(n-1)h}{\lambda}.
$$

## 3. 杨氏双缝近轴公式

双缝间距 $d$、观察屏到双缝距离 $D$，则

$$
\Delta(x)\simeq \frac{dx}{D},
\qquad
\Delta x=\frac{\lambda D}{d}.
$$

若实际相干源是双缝的像，必须使用“像源间距”和“像源到屏的距离”，不能机械套入原缝参数。

## 4. 薄膜反射相位突变

从低折射率介质向高折射率介质反射，反射波多出 $\pi$ 相位；从高到低反射不产生这项突变。判断反射增强或减弱前，先数两束反射光各自经历了几次“低到高”反射。

\mainmatter

# 例2.1　两束对称斜入射平面波的干涉

## 题意恢复

两束相干、等振幅的平面单色光在 $xOz$ 平面内传播，波长

$$
\lambda=6328\,\text{\AA}=0.6328\,\mu\mathrm m,
$$

它们与 $z$ 轴的夹角分别为 $+\theta$ 和 $-\theta$，在 $z=0$ 的 $xOy$ 平面相遇。求：

1. 两束波及合场的复振幅；
2. 屏上强度分布；
3. $\theta=30^\circ$ 时的条纹间距和空间频率；
4. 如何在实验中获得两束平行相干光。

\begin{center}
\includegraphics[width=.78\textwidth]{figures_v054/fig_2_1_plane_waves.png}
\end{center}

::: corebox
空间条纹来自两个波矢横向分量的差。沿 $z$ 的相位因子相同，只给合场乘一个公共相位；沿 $x$ 的相位一正一负，才产生稳定的余弦调制。
:::

## 所需基础

真空波数

$$
k=\frac{2\pi}{\lambda}.
$$

两波矢可写成

$$
\mathbf{k}_1=k(\sin\theta,0,\cos\theta),\qquad
\mathbf{k}_2=k(-\sin\theta,0,\cos\theta).
$$

## 自然思路

先分别写复振幅，再相加。不要先猜条纹间距，因为相位中究竟出现 $k\sin\theta$ 还是 $2k\sin\theta$，很容易少一个2。

## 逐步解答

设两束光在原点的初相位相同，单束复振幅均为 $A$。则

$$
\tilde U_1(x,z)=A\exp\{ik(x\sin\theta+z\cos\theta)\},
$$

$$
\tilde U_2(x,z)=A\exp\{ik(-x\sin\theta+z\cos\theta)\}.
$$

相加得

$$
\begin{aligned}
\tilde U
&=Ae^{ikz\cos\theta}
\left(e^{ikx\sin\theta}+e^{-ikx\sin\theta}\right)\\
&=2Ae^{ikz\cos\theta}\cos(kx\sin\theta).
\end{aligned}
$$

屏位于 $z=0$ 时，强度与 $|\tilde U|^2$ 成正比：

$$
I(x)=4A^2\cos^2(kx\sin\theta)
=2A^2\left[1+\cos(2kx\sin\theta)\right].
$$

亮纹满足

$$
kx\sin\theta=m\pi,
$$

所以

$$
x_m=m\frac{\lambda}{2\sin\theta},
\qquad
\boxed{\Delta x=\frac{\lambda}{2\sin\theta}}.
$$

当 $\theta=30^\circ$ 时，$2\sin\theta=1$，因此

$$
\boxed{\Delta x=0.6328\,\mu\mathrm m}.
$$

空间频率为

$$
f_x=\frac1{\Delta x}
=1.580\,\mu\mathrm m^{-1}
=\boxed{1580\,\mathrm{mm}^{-1}}.
$$

获得两束平行相干光的核心不是“找两个独立激光器”，而是把同一束光分成两路，再使两路以不同方向平行入射，例如：

- 分束镜与两面反射镜组成的马赫--曾德尔型光路；
- 双棱镜或双反射镜分波前；
- 光栅产生的 $+1$、$-1$ 级衍射光，经透镜准直后重合。

## 自检

- $\theta\to0$ 时，$\Delta x\to\infty$：两束完全同向，不再产生横向条纹。
- $\theta$ 增大，横向波矢差增大，条纹应变密，与公式一致。
- 条纹只随 $x$ 变化，沿 $y$ 为直条纹。

::: mistakebox
最常见错误是把两束之间的夹角 $2\theta$ 与每束相对 $z$ 轴的夹角 $\theta$ 混用。小角近似下，若直接用两束夹角 $\alpha=2\theta$，应写 $\Delta x\simeq\lambda/\alpha$。
:::

### 变式A　两束振幅不相等

若振幅分别为 $A$、$2A$，则

$$
I=A^2+4A^2+4A^2\cos(2kx\sin\theta),
$$

故

$$
I_{\max}=9A^2,
\quad I_{\min}=A^2,
\quad
\boxed{\gamma=\frac{9-1}{9+1}=0.8}.
$$

条纹间距不变，只有对比度下降。

### 变式B　两束角度不对称

若两束分别与 $z$ 轴成 $\theta_1$、$\theta_2$，且位于同一侧或异侧，横向波矢差为

$$
\Delta k_x=k(\sin\theta_1-\sin\theta_2),
$$

所以

$$
\boxed{\Delta x=\frac{2\pi}{|\Delta k_x|}
=\frac{\lambda}{|\sin\theta_1-\sin\theta_2|}}.
$$

### 变式C　由条纹间距反求夹角

若 $\lambda=532\,\mathrm{nm}$，测得 $\Delta x=10\,\mu\mathrm m$，且两束对称，则

$$
\sin\theta=\frac{\lambda}{2\Delta x}=0.0266,
$$

$$
\theta\simeq1.524^\circ,
\qquad
\boxed{\text{两束总夹角 }2\theta\simeq3.05^\circ}.
$$

# 例2.2　维恩干涉装置的二维条纹间距

## 题意恢复

一束平行光同时照到感光板 $P$ 与平面镜 $M$。板与镜夹角为 $\beta$，入射光与板的夹角为 $\alpha$。直接照到板上的光与镜面反射后到达板的光发生干涉。求板面上两个方向的条纹间距。

\begin{center}
\includegraphics[width=.70\textwidth]{figures_v054/fig_2_2_wien.png}
\end{center}

## 自然思路

把两束到达板面的光都看成平面波。板面上相位变化的快慢由两束波矢在板面内的投影差决定。图中几何只在一个截面内变化，因此另一个垂直方向没有相位梯度，条纹无限长。

## 逐步解答

取板面内、入射面方向为 $x$，垂直入射面方向为 $y$。直接光与反射光在 $x$ 方向的相位梯度差为

$$
\Delta k_x
=k\left[\cos(\beta-\alpha)-\cos(\beta+\alpha)\right].
$$

利用恒等式

$$
\cos(A-B)-\cos(A+B)=2\sin A\sin B,
$$

得

$$
\Delta k_x=2k\sin\alpha\sin\beta.
$$

相邻同类条纹的相位差增加 $2\pi$，故

$$
\boxed{
\Delta x=\frac{2\pi}{\Delta k_x}
=\frac{\lambda}{2\sin\alpha\sin\beta}
}.
$$

在 $y$ 方向，两波矢投影相同，因而

$$
\Delta k_y=0,
\qquad
\boxed{\Delta y=\infty}.
$$

条纹是平行于 $y$ 方向的直线。

## 小角极限

若 $\alpha,\beta\ll1$，则

$$
\Delta x\simeq\frac{\lambda}{2\alpha\beta}.
$$

这说明两个角中任一个趋于0，条纹都会变得极宽。

## 自检与易错点

- 不能把 $\cos(\beta-\alpha)-\cos(\beta+\alpha)$ 错写成 $2\cos\alpha\cos\beta$。
- $\alpha$、$\beta$ 参加近似时必须用弧度。
- $\Delta y=\infty$ 不是“没有光”，而是沿该方向相位不变。

### 变式A　不作小角近似

设 $\lambda=633\,\mathrm{nm}$、$\alpha=5^\circ$、$\beta=10^\circ$，则

$$
\Delta x=\frac{633\times10^{-9}}
{2\sin5^\circ\sin10^\circ}
\approx\boxed{20.9\,\mu\mathrm m}.
$$

### 变式B　由条纹间距反求入射角

若 $\lambda=500\,\mathrm{nm}$、$\beta=8^\circ$、$\Delta x=50\,\mu\mathrm m$，则

$$
\sin\alpha=\frac{\lambda}{2\Delta x\sin\beta}
\approx0.0359,
$$

所以

$$
\boxed{\alpha\approx2.06^\circ}.
$$

### 变式C　镜面与板面近似垂直

若 $\beta=90^\circ$，则

$$
\boxed{\Delta x=\frac{\lambda}{2\sin\alpha}}.
$$

这与两束对称倾斜平面波的结果同形，因为此时镜面反射等效地产生了横向波矢相反的两束光。

# 例2.3　切去透镜中央条带后的干涉区与条纹数

## 题意恢复

焦距 $f=25\,\mathrm{cm}$ 的会聚透镜中央切去宽度为 $a$ 的条带，剩余两半拼合。波长 $\lambda=500\,\mathrm{nm}$。在透镜后方移动屏，观察到条纹间距始终为

$$
\Delta y=0.25\,\mathrm{mm}.
$$

透镜口径为 $d=5\,\mathrm{cm}$。求：

1. 切去条带宽度 $a$；
2. 干涉区何处最大、最多有多少条纹；
3. 屏移到多远条纹消失。

\begin{center}
\includegraphics[width=.78\textwidth]{figures_v054/fig_2_3_overlap.png}
\end{center}

::: corebox
条纹间距不随屏距变化，说明两束出射光近似平行。因而点光源应放在原透镜焦平面上；切去中央条带只是让两半透镜把同一球面波变成两束有横向错位的平行相干光。
:::

## 第一步：求条带宽度

两半透镜拼合后，相当于两个光轴相距 $a$ 的透镜。焦平面上的点源经每一半后出射为平行光，两束之间小夹角满足

$$
\alpha\simeq\frac{a}{f}.
$$

两束平行相干光的条纹间距

$$
\Delta y=\frac{\lambda}{\alpha}
=\frac{\lambda f}{a}.
$$

所以

$$
\boxed{a=\frac{\lambda f}{\Delta y}}.
$$

代入

$$
\lambda=5.0\times10^{-7}\,\mathrm m,
\quad f=0.25\,\mathrm m,
\quad \Delta y=2.5\times10^{-4}\,\mathrm m,
$$

得

$$
\boxed{a=5.0\times10^{-4}\,\mathrm m=0.50\,\mathrm{mm}}.
$$

## 第二步：干涉区最大的位置

每一半透镜有效宽度约为

$$
\frac{d-a}{2}.
$$

两束平行光彼此倾斜，重叠宽度从透镜后开始先增大、再减小。最大重叠发生在由两束边缘光线构成的菱形中部。用相似三角形估算菱形全长

$$
L_{\rm diamond}\simeq\frac{df}{a}
=\frac{50\times250}{0.5}\,\mathrm{mm}
=25\,\mathrm m.
$$

因此最大重叠大约在

$$
\boxed{z\simeq12.5\,\mathrm m}
$$

处。

最大重叠宽度约为 $d/2=25\,\mathrm{mm}$，故可见条纹数

$$
N_{\max}\simeq\frac{25\,\mathrm{mm}}{0.25\,\mathrm{mm}}
=\boxed{100}.
$$

## 第三步：条纹消失位置

菱形远端两束不再重叠，故在

$$
\boxed{z\simeq25\,\mathrm m}
$$

处条纹消失。

## 自检

- $a$ 越大，两束夹角越大，条纹越密，公式给出 $\Delta y\propto1/a$。
- $d$ 只控制重叠范围和可见条纹总数，不控制局部条纹间距。
- 若光源不在焦平面，两束不再平行，条纹间距会随屏距改变。

### 变式A　切去宽度变为1.0 mm

其余参数不变，则

$$
\Delta y=\frac{\lambda f}{a}
=\boxed{0.125\,\mathrm{mm}}.
$$

条纹加密一倍，菱形全长缩短为约12.5 m。

### 变式B　口径改为8 cm

$a=0.5\,\mathrm{mm}$ 不变时，条纹间距仍为0.25 mm；最大重叠宽度约40 mm，因此

$$
\boxed{N_{\max}\simeq160}.
$$

这说明口径改变条纹数，却不改变条纹间距。

### 变式C　光源离焦1 mm

光源不再处于焦平面，两半透镜输出的是两束略会聚或略发散的波。屏上条纹可呈现位置依赖的间距，甚至产生弯曲；实验上可利用“条纹间距是否随屏移动”反向判断光源是否严格位于焦平面。

# 例2.4　分割透镜形成的两个像源

## 题意恢复

焦距 $f=50\,\mathrm{cm}$ 的会聚透镜沿直径分成两半，两半横向分开 $1\,\mathrm{mm}$。单色点光源位于透镜前 $300\,\mathrm{cm}$，观察屏位于透镜后 $450\,\mathrm{cm}$，波长 $\lambda=0.5\,\mu\mathrm m$。求屏上条纹间距。

\begin{center}
\includegraphics[width=.76\textwidth]{figures_v054/fig_2_4_split_lens.png}
\end{center}

## 自然思路

两半透镜不是“两个缝”。每一半先给同一物点成像，形成两个相干像源；再把这两个像源当作杨氏双源。关键是求像距、像源间距和像源到屏的距离。

## 第一步：完整透镜的像距

由

$$
\frac1u+\frac1v=\frac1f
$$

得

$$
\frac1{300}+\frac1v=\frac1{50},
$$

所以

$$
\boxed{v=60\,\mathrm{cm}}.
$$

## 第二步：两个像源的间距

两半透镜的光轴相距1 mm。若把每一半的光心横移量看成 $\pm0.5\,\mathrm{mm}$，像的位置相对各自光轴按横向放大率移动。总像源间距可用几何或原书关系得到

$$
d'=1.2\,\mathrm{mm}.
$$

一种直观推导是：横移薄透镜会使像相对固定实验坐标移动因子

$$
1-\frac vu=1-\frac{60}{300}=0.8
$$

的反向量；两光轴本身又相距1 mm，合计给出 $1.2$ mm 的像源分离。

## 第三步：像源到屏的距离

$$
D=450-60=390\,\mathrm{cm}=3.90\,\mathrm m.
$$

因此杨氏条纹间距

$$
\Delta x=\frac{\lambda D}{d'}
=\frac{0.5\times10^{-6}\times3.90}{1.2\times10^{-3}}
=1.625\times10^{-3}\,\mathrm m.
$$

故

$$
\boxed{\Delta x=1.625\,\mathrm{mm}}.
$$

## 自检和易错点
