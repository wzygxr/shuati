---
title: "光学：IPhO 金牌讲义与全习题详解（丁文革分支增量）"
subtitle: "第2章光的干涉：填空题1--22、简答题1--15、综合计算题1--23"
author: "题源：用户上传218页完整扫描本；解答：逐题独立推导、复算、勘误、变式与补充"
date: "2026 年 9 月 24 日"
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
    \fancyhead[R]{丁文革 DING-C2-v003}
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

# DING-C2-v003 本轮交付边界

上一批已经连续完成第2章例2.1--2.36以及自测选择题1--34。本轮从扫描本物理页72的“填空题1”继续，不回头做二轮审计，并把上一版断点说明中遗漏列出的“简答题1--15”一并恢复，连续清零：

$$
\boxed{\text{填空题1--22 + 简答题1--15 + 综合计算题1--23}}.
$$

本批共有

$$
22+15+23=\boxed{60\text{ 个正式题位}}.
$$

每个题位配置3道专属训练，因此新增

$$
60\text{ 道正式题}+180\text{ 道变式}=\boxed{240\text{ 个训练单元}}.
$$

::: sourcebox
题源来自用户上传的《光学指导·考研参考书》完整扫描母本。原文件实际有218个物理页、28,275,795字节，SHA-256为 `2859f841147d8473f0db5074ae1133aea9f80f40a545bb76f06b1b73e23ef98f`。本卷使用物理页72--77的题面和物理页80--87的原书解题思路与答案；150页只是连接器预览层，不是原书终点。
:::

\begin{center}
\includegraphics[width=.98\textwidth]{figures_v056/source_questions_contact.jpg}
\end{center}

\begin{center}
\includegraphics[width=.98\textwidth]{figures_v056/source_answers_contact.jpg}
\end{center}

# 本批高风险勘误先行

| 题位 | 原书口径或答案 | 本卷独立复核 |
|---|---|---|
| 填空6 | $0.1\,\mu\mathrm m$、$0.53\,\mu\mathrm m$ | 该数值把“与膜面成30°”按“与法线成30°”处理。照字面应为约 $0.124\,\mu\mathrm m$、$0.659\,\mu\mathrm m$；按出题意图则约 $0.101\,\mu\mathrm m$、$0.540\,\mu\mathrm m$ |
| 填空7 | $90.6\,\mathrm{nm}$ | 空气--MgF$_2$--玻璃中两次反射都发生 $\pi$ 跃迁。若目标是**增强反射**，最小非零厚度应为 $\lambda/(2n_f)\approx181\,\mathrm{nm}$；$90.6\,\mathrm{nm}$ 是减反射的四分之一波厚度 |
| 填空22 | 精细度定义含“半角宽度” | 标准精细度是自由光谱区除以**半高全宽**。若把分母真取半高半宽，数值会多2倍，必须先统一术语 |
| 综合10 | 只给 $9.1\,\mu\mathrm m$ | 题面数据允许厚度族 $h=(2q+1)\times0.700\,\mu\mathrm m$。$9.1\,\mu\mathrm m$ 是 $q=6$ 的有效高阶解，但不是唯一解；最小正解为 $0.700\,\mu\mathrm m$ |
| 综合13 | 4个完整暗环 | 两个反射都发生 $\pi$ 跃迁，边缘为亮；$2nh_{\max}/\lambda=4.8$，暗环厚度为半整数级，实际有5个完整暗环 |
| 综合15 | $0.563\,\mu\mathrm m$ | $\lambda=2\Delta d/N=588.4\,\mathrm{nm}=0.588\,\mu\mathrm m$ |
| 综合16(3) | 反射镜移动 $0.29\,\mathrm{mm}$ | $0.29\,\mathrm{mm}$ 是光程差变化；镜面机械位移应为其一半，约 $0.145\,\mathrm{mm}$ |
| 综合21(1) | $4.27\times10^{-3}\,\mathrm{nm}$ | 相邻透射峰间距是自由光谱区 $0.060\,\mathrm{nm}$；$4.27\times10^{-3}\,\mathrm{nm}$ 是单峰半高全宽 |
| 综合23(3) | 直接把“振幅反射比0.95”当强度反射率 | 若0.95是振幅反射比，则强度反射率为0.9025，可见度约0.9948；若题目实指强度反射率0.95，才得到约0.9987 |

# 统一工具箱

## 1. 双光束干涉

$$
I=I_1+I_2+2\sqrt{I_1I_2}\cos\delta,
\qquad
\gamma=\frac{I_{\max}-I_{\min}}{I_{\max}+I_{\min}}
=\frac{2\sqrt{I_1I_2}}{I_1+I_2}.
$$

杨氏双缝近轴条纹间距

$$
\Delta x=\frac{\lambda D}{d}.
$$

单侧插入厚度 $t$、折射率 $n$ 的薄片，附加光程差

$$
\Delta_{\rm plate}=(n-1)t,
$$

条纹整体位移量

$$
|\Delta x_0|=\frac{(n-1)tD}{d}.
$$

## 2. 空间与时间相干

均匀线光源宽度为 $p$、光源到双缝距离为 $D$ 时，双缝间相干度

$$
\gamma=\left|\frac{\sin u}{u}\right|,
\qquad
u=\frac{\pi d p}{\lambda D}.
$$

第一零点给出数量级判据

$$
pd\sim\lambda D.
$$

窄带光的相干长度近似

$$
L_c\sim\frac{\lambda^2}{\Delta\lambda}=c\tau_c.
$$

## 3. 薄膜相位跃迁

从低折射率向高折射率界面反射，电场相位跃迁 $\pi$；从高向低反射则无这项跃迁。亮暗条件不能背成一套固定公式，必须先比较两束反射光的相对相位跃迁次数。

\begin{center}
\includegraphics[width=.82\textwidth]{figures_v056/thin_film_phase_accounting.png}
\end{center}

## 4. 牛顿环、迈克耳孙与F-P

反射牛顿环的基本量级关系为

$$
r_k^2\propto k\lambda R.
$$

迈克耳孙中镜面移动 $\Delta d$ 引起往返光程变化 $2\Delta d$：

$$
2\Delta d=N\lambda.
$$

F-P正入射共振、自由光谱区和反射率精细度为

$$
2nd=m\lambda,
\qquad
\Delta\nu_{\rm FSR}=\frac{c}{2nd},
\qquad
\mathcal F\simeq\frac{\pi\sqrt R}{1-R}.
$$

\mainmatter

# 第一部分　填空题1--22

# 填空题1　由波函数读出五个基本量

## 题意重述
给定 $\varphi=5\cos 2\pi(0.5x-4t)$，求频率、波长、振幅、相速度和传播方向。

## 所需基础
行波标准式为 $\varphi=A\cos 2\pi(x/\lambda-ft+\varphi_0)$。相位中 $x$ 的系数是 $1/\lambda$，$t$ 的系数是频率 $f$。

## 自然思路与逐步解答
逐项比较可得 $A=5$，$1/\lambda=0.5\,\mathrm{m^{-1}}$，故 $\lambda=2\,\mathrm m$；$f=4\,\mathrm{Hz}$。相速度 $v=f\lambda=8\,\mathrm{m/s}$。相位写成 $kx-\omega t$，所以波沿 $x$ 轴正方向传播。

## 结论
$$\boxed{f=4\,\mathrm{Hz},\ \lambda=2\,\mathrm m,\ A=5\text{（按题设振幅单位）},\ v=8\,\mathrm{m/s},\ +x\text{方向}}.$$

::: mistakebox
不要把 $0.5$ 误当波数 $k$；真正的角波数是 $k=2\pi\times0.5=\pi\,\mathrm{rad/m}$。
:::

## 三道本质不同的变式
**变式A**　$y=3\cos2\pi(2x-5t)$。**解：** $A=3$，$\lambda=0.5\,$m，$f=5\,$Hz，$v=2.5\,$m/s，沿 $+x$。  
**变式B**　$y=2\cos2\pi(0.25x+3t)$。**解：** $\lambda=4\,$m，$f=3\,$Hz，$v=12\,$m/s；加号表示沿 $-x$。  
**变式C**　已知 $A=1\,$mm、$f=50\,$Hz、$v=20\,$m/s，写沿 $+x$ 的波。**解：** $\lambda=0.4\,$m，$y=10^{-3}\cos2\pi(x/0.4-50t)$。

# 填空题2　相干叠加与非相干叠加

## 题意重述
两束光的单束光强都为 $I$。先求相干叠加的最大光强，再求非相干叠加的最大光强。

## 所需基础
相干光必须先叠加电场；非相干光的交叉项做时间平均后为零，只能叠加光强。

## 自然思路与逐步解答
相干且同相时，电场振幅由 $E_0+E_0=2E_0$，光强与振幅平方成正比，所以 $I_{\max}=4I$。非相干时 $I_{\rm total}=I+I=2I$，不存在稳定的相位调制项。

## 结论
$$\boxed{4I,\quad 2I}.$$

::: mistakebox
“两束各为 $I$”时，相干同相不是 $2I$，因为先加的是振幅。
:::

## 三道本质不同的变式
**变式A**　两束相干光强分别为 $I$、$4I$。**解：** $I_{\max}=(\sqrt I+2\sqrt I)^2=9I$，$I_{\min}=I$。  
**变式B**　上题若非相干。**解：** 恒为 $5I$。  
**变式C**　若可见度为0.8且 $I_1/I_2\ge1$。**解：** 令 $r=\sqrt{I_1/I_2}$，$2r/(1+r^2)=0.8$，得 $r=2$，故 $I_1/I_2=4$。

# 填空题3　整套杨氏装置浸入介质

## 题意重述
空气中杨氏条纹间距为 $\Delta x$。装置整体浸入折射率为 $n$ 的均匀液体后，求新间距。

## 所需基础
介质中频率不变、波长变为 $\lambda/n$。双缝几何量 $D,d$ 不变。

## 自然思路与逐步解答
由 $\Delta x=\lambda D/d$，浸液后 $\Delta x'=\lambda D/(nd)=\Delta x/n$。

## 结论
$$\boxed{\Delta x'=\frac{\Delta x}{n}}.$$

::: mistakebox
只把光速改成 $c/n$ 还不够，必须落实到波长缩短；频率由光源决定，不变。
:::

## 三道本质不同的变式
**变式A**　水中 $n=4/3$，空气间距1.2mm。**解：** 水中0.90mm。  
**变式B**　浸液后屏距同时加倍，$n=1.5$。**解：** 新间距为原来的 $2/1.5=4/3$。  
**变式C**　实测空气、水中间距分别1.50mm和1.13mm。**解：** $n\approx1.50/1.13=1.33$。

# 填空题4　扩展光源使杨氏条纹第一次消失

## 题意重述
非相干线光源通过单孔屏照明双缝。单孔屏缝宽 $b$ 增大到某临界值时条纹第一次消失；光源屏到双缝距离为 $a$，双缝间距为 $d$。

## 所需基础
扩展非相干源可视为许多独立点源。不同点源产生的条纹横向错位，均匀叠加后可见度为 sinc 函数。第一零点对应 $db/(\lambda a)=1$。

## 自然思路与逐步解答
由第一零点条件 $\pi db/(\lambda a)=\pi$，得 $b=a\lambda/d$。

\begin{center}
\includegraphics[width=.82\textwidth]{figures_v056/spatial_coherence_sinc.png}
\end{center}

## 结论
$$\boxed{b_c=\frac{a\lambda}{d}}.$$

::: mistakebox
这是空间相干判据，不是衍射缝宽公式；$a$ 是光源到双缝距离，不是双缝到观察屏距离。
:::

## 三道本质不同的变式
**变式A**　$a=1\,$m、$d=1\,$mm、$\lambda=500\,$nm。**解：** $b_c=0.50\,$mm。  
**变式B**　保持其他量不变，把 $d$ 减半。**解：** 临界源宽增大2倍，空间相干容忍度提高。  
**变式C**　若要求第一零点前可见度约0.64。**解：** 取 $u=\pi/2$，即 $b\approx a\lambda/(2d)$。

# 填空题5　500nm玻璃板的反射增强波长

## 题意重述
白光垂直入射厚度 $h=500\,$nm、折射率 $n=1.52$ 的玻璃板，求可见范围内反射增强的波长。

## 所需基础
上表面空气到玻璃反射发生 $\pi$ 跃迁，下表面玻璃到空气反射不发生，所以两束反射光之间多一个相对 $\pi$。垂直入射反射增强条件为 $2nh=(m+1/2)\lambda$。

## 自然思路与逐步解答
整理为 $\lambda=4nh/(2m+1)=3040/(2m+1)\,$nm。逐个试奇数分母：3给1013nm（红外），5给608nm，7给434.3nm，9给337.8nm（紫外）。

## 结论
$$\boxed{\lambda\approx608\,\mathrm{nm},\quad434.3\,\mathrm{nm}}.$$

::: mistakebox
先判断相位跃迁，再写亮暗条件。把条件误写成 $2nh=m\lambda$ 会得到另一组波长。
:::

## 三道本质不同的变式
**变式A**　若厚度改为400nm。**解：** $4nh=2432$nm，可见增强为486.4nm（分母5）；347.4nm已在紫外。  
**变式B**　求同一玻璃板反射减弱波长。**解：** $2nh=m\lambda$，可见可取760nm、506.7nm。  
**变式C**　观察到608nm反射增强，已知 $n=1.52$ 且取最低可见级。**解：** 对应分母5，$h=5\lambda/(4n)=500$nm。

# 填空题6　斜视肥皂膜的最薄厚度与换色

## 题意重述
用 $\lambda=0.5\,\mu\mathrm m$ 绿光观察折射率1.33的肥皂膜。原题写“观察方向与膜面成30°”，膜最亮；求最薄厚度，并求改为垂直观察时仍最亮所需波长。

## 所需基础
反射最亮且存在一次相对半波损失时，最低级满足 $2nh\cos r=\lambda/2$。外部入射角 $i$ 与膜内角 $r$ 满足 $\sin i=n\sin r$。

## 自然思路与逐步解答
按原书数值意图，应把30°理解成相对法线的角度：$r=\arcsin(\sin30^\circ/1.33)$，于是 $h=\lambda/(4n\cos r)\approx0.101\,\mu$m。垂直观察时 $\lambda'=4nh\approx0.540\,\mu$m。若严格按“与膜面成30°”，则 $i=60^\circ$，得到 $h\approx0.124\,\mu$m、$\lambda'\approx0.659\,\mu$m。

::: correctionbox
原书的 $0.1\,\mu$m 和 $0.53\,\mu$m 只与“相对法线30°”相符；题面写成“与膜面30°”存在几何口径矛盾。
:::

## 结论
出题意图下：$$\boxed{h_{\min}\approx0.101\,\mu\mathrm m,\quad \lambda'\approx0.540\,\mu\mathrm m}.$$

::: mistakebox
“与平面成角”和“与法线成角”相差余角。薄膜题中角度通常相对法线定义，必须看清。
:::

## 三道本质不同的变式
**变式A**　若入射角相对法线为45°。**解：** $r=\arcsin(\sin45^\circ/1.33)$，$h\approx0.110\,\mu$m。  
**变式B**　若膜厚0.12μm，垂直观察最亮的最长波长。**解：** $\lambda=4nh\approx0.638\,\mu$m。  
**变式C**　若折射率增大而波长、外角不变。**解：** 膜内角减小，$n\cos r$总体增大，最低亮膜厚减小。

# 填空题7　MgF$_2$单层膜究竟增反还是减反

## 题意重述
玻璃 $n_s=1.5$ 表面镀 MgF$_2$ 薄膜 $n_f=1.38$，希望增强 $\lambda=500\,$nm 的反射，求最小非零厚度。

## 所需基础
空气到MgF$_2$和MgF$_2$到玻璃都是低折射率到高折射率反射，两束反射光都发生 $\pi$ 跃迁，彼此相消，因此相对相位只来自往返传播。增强反射需 $2n_fh=m\lambda$。

## 自然思路与逐步解答
取最小非零整数 $m=1$，$h=\lambda/(2n_f)=500/(2\times1.38)\approx181.2\,$nm。四分之一波厚度 $\lambda/(4n_f)=90.6\,$nm 会令两束反射光反相，属于减反射设计。

\begin{center}
\includegraphics[width=.82\textwidth]{figures_v056/thin_film_phase_accounting.png}
\end{center}

::: correctionbox
原书给出的90.6nm与“增加反射”目标相反；该数值是此折射率排列下的减反射四分之一波厚度。
:::

## 结论
$$\boxed{h_{\min}\approx181\,\mathrm{nm}}.$$

::: mistakebox
不能看到“薄膜”就机械套 $\lambda/4n$；相对相位跃迁次数决定是四分之一波还是半波。
:::

## 三道本质不同的变式
**变式A**　若基底折射率改为1.20。**解：** 顶面有 $\pi$、底面无 $\pi$，增强反射最低厚度变为 $\lambda/(4n_f)=90.6$nm。  
**变式B**　要求在空气--玻璃界面理想单层减反。**解：** 还需 $n_f=\sqrt{n_0n_s}\approx1.225$，并取 $h=\lambda/(4n_f)$。  
**变式C**　为什么实际高反膜常用多层？**解：** 单层只能有限调节两束振幅，多层四分之一波堆栈可让多束反射同相并显著提高反射率。

# 填空题8　增大空气劈尖角时条纹怎样动

## 题意重述
空气劈尖上板倾斜、下板水平，劈角 $\theta$ 增大，问条纹移动方向及条纹间距变化。

## 所需基础
小角度下膜厚 $h\simeq\theta x$，同类条纹位置 $x_m\propto1/\theta$，间距 $\Delta x=\lambda/(2\theta)$。

## 自然思路与逐步解答
增大 $\theta$ 时，同一级次的 $x_m$ 变小，条纹向劈尖交棱（尖端）移动；同时 $\Delta x$ 变小，条纹变密。

## 结论
$$\boxed{\text{向交棱方向移动；间距减小}}.$$

::: mistakebox
“条纹向厚处还是薄处”要用固定级次位置判断，不能凭图感。
:::

## 三道本质不同的变式
**变式A**　劈角减半。**解：** 条纹向厚端移动，间距加倍。  
**变式B**　波长增大20%。**解：** 位置和间距均增大20%，方向不变。  
**变式C**　实测间距0.5mm、$\lambda=500$nm。**解：** $\theta=\lambda/(2\Delta x)=5.0\times10^{-4}$rad。

# 填空题9　整体抬高劈尖上板

## 题意重述
空气劈尖的上表面整体向上平移，劈角不变。问条纹移动方向和间距。

## 所需基础
设膜厚 $h(x)=h_0+\theta x$。固定级次满足 $2h=m\lambda$，故 $x_m=[m\lambda/2-h_0]/\theta$。

## 自然思路与逐步解答
上板抬高使 $h_0$ 增大，$x_m$ 减小，所以条纹向交棱方向移动；由于斜率 $\theta$ 不变，间距 $\lambda/(2\theta)$ 不变。

## 结论
$$\boxed{\text{向交棱方向移动；间距不变}}.$$

::: mistakebox
平移改变截距，转动改变斜率；前者不改条纹间距，后者会改。
:::

## 三道本质不同的变式
**变式A**　上板整体下移。**解：** 条纹向厚端移动，间距不变。  
**变式B**　上板同时上移并增大劈角。**解：** 平移决定整体位移，增角使间距减小，两效应需分开叠加。  
**变式C**　某点有10条纹扫过。**解：** 厚度变化量 $\Delta h=10\lambda/2=5\lambda$。

# 填空题10　20cm空气劈尖中的条纹数与间距

## 题意重述
两块20cm长平晶一端接触，另一端夹直径0.05mm细丝，垂直照射 $\lambda=680$nm。求全长条纹数和间距。

## 所需基础
反射同类条纹对应膜厚每增加 $\lambda/2$。末端厚度 $h=0.05$mm，所以跨越级次数 $N=2h/\lambda$。

## 自然思路与逐步解答
$N=2\times0.05/(680\times10^{-6})\approx147.1$，可见约147条。间距 $\Delta x=L/N=200/147.1\approx1.36$mm。

## 结论
$$\boxed{N\approx147,\qquad \Delta x\approx1.36\,\mathrm{mm}}.$$

::: mistakebox
细丝“直径”就是两板末端间隙，不要再除2。
:::

## 三道本质不同的变式
**变式A**　细丝直径0.10mm。**解：** 约294条，间距约0.68mm。  
**变式B**　改用510nm光。**解：** 约196条，间距约1.02mm。  
**变式C**　已知20cm内150条、$\lambda=600$nm。**解：** 末端厚度 $h=N\lambda/2=0.045$mm。

# 填空题11　牛顿环属于哪类干涉

## 题意重述
判断牛顿环属于等厚还是等倾干涉。

## 所需基础
牛顿环的每条环对应空气膜的同一厚度；厚度随离接触点半径变化。

## 自然思路与逐步解答
因此牛顿环是典型的等厚干涉。

## 结论
$$\boxed{\text{等厚干涉}}.$$

::: mistakebox
圆环形状不等于等倾干涉；分类看“条纹由厚度等值线还是入射角等值线决定”。
:::

## 三道本质不同的变式
**变式A**　F-P焦平面圆环。**解：** 等倾干涉。  
**变式B**　空气劈尖直条纹。**解：** 等厚干涉。  
**变式C**　迈克耳孙两镜严格平行所得同心圆。**解：** 等倾干涉。

# 填空题12　液体折射率由牛顿环直径变化反求

## 题意重述
空气中第10个亮环直径1.40cm，填入液体后变为1.27cm，求液体折射率。

## 所需基础
亮环半径平方与介质中波长成正比，即 $D_k^2\propto\lambda/n$。同一级次、同一曲率半径下，$D_{\rm liq}^2=D_{\rm air}^2/n$。

