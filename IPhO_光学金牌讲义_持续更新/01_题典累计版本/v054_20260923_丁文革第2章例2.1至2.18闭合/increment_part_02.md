
所需厚度仍为39 μm，但条纹移动方向相反。若实验只记录移动条数而不记录方向，无法判断玻璃片究竟放在哪一路。

# 例2.10　菲涅耳双棱镜：由条纹间距求棱镜角

## 题意恢复

两块很薄的棱镜底边相接，构成菲涅耳双棱镜。波长

$$
\lambda=0.5\,\mu\mathrm m
$$

的平行光沿系统对称轴入射。观察到屏上条纹间距

$$
\Delta x=0.5\,\mathrm{mm}.
$$

双棱镜材料折射率为 $n=1.5$。求每个小棱镜的顶角 $\alpha$。

## 所需基础

薄棱镜的小偏向角

$$
\delta\simeq(n-1)\alpha.
$$

上下两半分别把光偏向相反方向，因此两束出射平面波的总夹角约为

$$
\Theta\simeq2\delta=2(n-1)\alpha.
$$

两束小夹角平面波的条纹间距

$$
\Delta x\simeq\frac{\lambda}{\Theta}.
$$

## 逐步解答

由

$$
\Delta x=\frac{\lambda}{2(n-1)\alpha}
$$

得

$$
\alpha=\frac{\lambda}{2(n-1)\Delta x}.
$$

代入

$$
\lambda=0.5\times10^{-6}\,\mathrm m,
\quad n-1=0.5,
\quad \Delta x=0.5\times10^{-3}\,\mathrm m,
$$

得

$$
\boxed{\alpha=1.0\times10^{-3}\,\mathrm{rad}}.
$$

换成角度：

$$
1.0\times10^{-3}\,\mathrm{rad}
=0.0573^\circ
\approx3.44'.
$$

## 自检

顶角越大，偏向越大，两束夹角越大，条纹应越密；公式给出 $\Delta x\propto1/\alpha$。

### 变式A　已知顶角求条纹间距

若 $\alpha=2.0\times10^{-3}\,\mathrm{rad}$，其余不变，则

$$
\Delta x=\frac{0.5\,\mu\mathrm m}{2\times0.5\times2.0\times10^{-3}}
=\boxed{0.25\,\mathrm{mm}}.
$$

### 变式B　棱镜材料换成 $n=1.60$

要维持 $0.5$ mm 的条纹间距：

$$
\alpha=\frac{0.5\times10^{-6}}
{2(0.60)(0.5\times10^{-3})}
=\boxed{8.33\times10^{-4}\,\mathrm{rad}}.
$$

折射率越大，所需顶角越小。

### 变式C　有限距离点光源

若入射不是平行光，而是距双棱镜有限距离的点光源，出射光等效来自两个虚像源。此时条纹间距应写成

$$
\Delta x=\frac{\lambda D}{d_{\rm virtual}},
$$

其中 $d_{\rm virtual}$ 与源距、棱镜角共同有关，不能再只由出射夹角的简单式独立决定。

# 例2.11　太阳角直径对双缝相干性的限制

## 题意恢复

用太阳光通过波长

$$
\lambda=550\,\mathrm{nm}
$$

的窄带滤光片作杨氏双缝实验。太阳的角直径约为

$$
\Delta\theta=30'=0.5^\circ.
$$

问双缝间距最大可取多少，才能仍看到明显干涉条纹？

::: corebox
太阳不是点光源。太阳盘面上不同点各自产生一套稍有平移的条纹；双缝越宽，两套条纹错位越严重，最后彼此平均掉。这是空间相干性问题。
:::

## 原书采用的数量级判据

对角宽度为 $\Delta\theta$ 的扩展光源，简单相干条件可写为

$$
d\,\Delta\theta\lesssim\lambda.
$$

所以

$$
d_{\max}\simeq\frac{\lambda}{\Delta\theta}.
$$

先换成弧度：

$$
30'=0.5^\circ
=8.7266\times10^{-3}\,\mathrm{rad}.
$$

因此

$$
\begin{aligned}
d_{\max}
&\simeq\frac{550\times10^{-9}}
{8.7266\times10^{-3}}\\
&=6.30\times10^{-5}\,\mathrm m.
\end{aligned}
$$

故

$$
\boxed{d_{\max}\approx63.0\,\mu\mathrm m}.
$$

## 为什么扩展光源会降低可见度

设光源上两个边缘点的入射方向相差 $\Delta\theta$。它们在双缝处产生的初始光程差相差约

$$
\Delta_{\rm source}\simeq d\,\Delta\theta.
$$

当这个差达到一个波长量级时，光源不同部分给出的亮纹和暗纹会严重错开，积分后可见度很低。

::: extensionbox
若把太阳近似为均匀圆盘，按范西特--泽尼克定理，复相干度是贝塞尔型函数，第一零点对应

$$
d\simeq1.22\frac{\lambda}{\Delta\theta}.
$$

这是比原书数量级判据更精确的圆盘源结果。本卷保留原书的 $63\,\mu\mathrm m$ 作为本题标准答案；精确圆盘模型会给约 $77\,\mu\mathrm m$。
:::

### 变式A　测量恒星角直径

若一颗恒星在基线 $d=10\,\mathrm m$ 时首次失去条纹，$\lambda=600\,\mathrm{nm}$，按均匀圆盘第一零点

$$
\Delta\theta=1.22\frac{\lambda}{d}
=7.32\times10^{-8}\,\mathrm{rad}
\approx\boxed{15.1\,\mathrm{mas}}.
$$

### 变式B　改用红光700 nm

按原书简化判据

$$
d_{\max}\propto\lambda,
$$

所以

$$
d_{\max}=63.0\times\frac{700}{550}
=\boxed{80.2\,\mu\mathrm m}.
$$

长波更容易保持空间相干。

### 变式C　缩小光源角宽度

在太阳像前加小孔，把有效角宽度缩小为原来的 $1/5$，则允许的双缝间距扩大5倍，约为

$$
\boxed{315\,\mu\mathrm m}.
$$

代价是通光量显著降低。

# 例2.12　均匀线光源照明双缝的空间相干度

## 题意恢复

长度为 $a$ 的均匀线光源位于双缝前方距离 $R$ 处，双缝间距为 $d$，屏距为 $D$。求屏上强度分布和条纹可见度。

数值部分给出

$$
a=0.10\,\mathrm{mm},
\quad \lambda=0.60\,\mu\mathrm m,
\quad R=50\,\mathrm{cm},
\quad d=1.20\,\mathrm{mm},
\quad D=60\,\mathrm{cm}.
$$

另在一缝后插入厚度 $h=0.30\,\mathrm{mm}$、折射率 $n=1.5$ 的玻璃片，讨论条纹变化。

\begin{center}
\includegraphics[width=.82\textwidth]{figures_v054/fig_2_12_extended_source.png}
\end{center}

## 单个源点产生的条纹

令线光源上的坐标为 $s$，中心为 $s=0$。在近轴条件下，源点 $s$ 到双缝引入初始光程差约

$$
\Delta_{\rm in}\simeq-\frac{ds}{R}.
$$

双缝到屏上坐标 $x$ 的几何光程差约

$$
\Delta_{\rm out}\simeq\frac{dx}{D}.
$$

因此该源点产生的强度可写为

$$
I_s(x)=I_s^{(0)}
\left[1+\cos\frac{2\pi d}{\lambda}
\left(\frac{x}{D}-\frac{s}{R}\right)\right].
$$

## 对均匀线光源积分

各源点彼此不相干，强度相加：

$$
I(x)\propto\int_{-a/2}^{a/2}I_s(x)\,ds.
$$

积分后得到

$$
\boxed{
I(x)=I_{\rm av}
\left[1+\gamma
\cos\left(\frac{2\pi d x}{\lambda D}\right)
\right]
},
$$

其中

$$
\boxed{
\gamma=
\left|
\frac{\sin\left(\dfrac{\pi d a}{\lambda R}\right)}
{\dfrac{\pi d a}{\lambda R}}
\right|
}
$$

就是由有限光源尺寸决定的条纹可见度。

## 数值结果

条纹间距

$$
\Delta x=\frac{\lambda D}{d}
=\frac{0.60\times10^{-6}\times0.60}{1.20\times10^{-3}}
=\boxed{0.30\,\mathrm{mm}}.
$$

第一次完全失去条纹满足

$$
\frac{\pi d a}{\lambda R}=\pi,
$$

即

$$
\boxed{d_0=\frac{\lambda R}{a}}.
$$

代入得

$$
d_0=\frac{0.60\times10^{-6}\times0.50}{0.10\times10^{-3}}
=\boxed{3.0\,\mathrm{mm}}.
$$

## 插入玻璃片

附加光程差

$$
(n-1)h=0.5\times0.30\,\mathrm{mm}=0.15\,\mathrm{mm}.
$$

相当于移动

$$
N=\frac{(n-1)h}{\lambda}
=\frac{0.15\,\mathrm{mm}}{0.00060\,\mathrm{mm}}
=\boxed{250}
$$

个条纹周期。

所以屏上位移

$$
\Delta X=N\Delta x
=250\times0.30\,\mathrm{mm}
=\boxed{75\,\mathrm{mm}}.
$$

理想平行薄片只加常量相位差，因此：

- 条纹整体平移；
- 条纹间距不变；
- 由光源尺寸决定的可见度 $\gamma$ 不变。

## 自检

- $a\to0$ 时，$\gamma\to1$，恢复点光源。
- $d$ 越大，空间相干要求越苛刻。
- 玻璃片只改变常量相位，不改变光源积分的振幅包络。

### 变式A　高斯线光源

若源强分布为

$$
S(s)\propto e^{-s^2/(2\sigma^2)},
$$

则傅里叶积分给

$$
\boxed{
\gamma=\exp\left[-\frac12
\left(\frac{2\pi d\sigma}{\lambda R}\right)^2\right]
}.
$$

高斯源没有有限基线处的严格零点，但可见度会快速衰减。

### 变式B　求可见度为0.5时的源宽

均匀线源满足

$$
\frac{\sin q}{q}=0.5,
\qquad q\approx1.895.
$$

所以

$$
a\approx\frac{1.895\lambda R}{\pi d}.
$$

代入本题 $\lambda,R,d$ 得

$$
\boxed{a\approx0.151\,\mathrm{mm}}.
$$

### 变式C　插片略有楔角

若玻璃片厚度随屏上有效孔径位置变化，就不再只是常量相位。它会引入额外相位梯度或波前畸变，使条纹倾斜、弯曲，甚至降低可见度。因此“间距和对比度不变”只适用于理想平行、均匀、无吸收薄片。

# 例2.13　有限谱宽对时间相干性的限制

## 题意恢复

杨氏双缝参数为

$$
d=0.10\,\mathrm{mm},
\quad R=40\,\mathrm{cm},
\quad D=80\,\mathrm{cm}.
$$

光源是中心波长

$$
\lambda_0=6500\,\text{\AA}=650\,\mathrm{nm}
$$

的窄带线光源，谱线宽度

$$
\Delta\lambda=50\,\text{\AA}=5\,\mathrm{nm},
$$

在带宽内近似均匀。求：

1. 条纹间距；
2. 强度包络与可见度；
3. 第11条暗纹处的相对强度量级；
4. 第一可见度零点距中心多远。

## 中心波长的条纹间距

$$
\Delta x_0=\frac{\lambda_0D}{d}
=\frac{650\times10^{-9}\times0.80}{0.10\times10^{-3}}
=\boxed{5.20\,\mathrm{mm}}.
$$

## 对谱线积分

屏上坐标 $x$ 对应光程差

$$
\Delta=\frac{dx}{D}.
$$

在窄带近似下，波数

$$
k(\lambda)=\frac{2\pi}{\lambda}
\simeq k_0-\frac{2\pi}{\lambda_0^2}(\lambda-\lambda_0).
$$

对宽度为 $\Delta\lambda$ 的均匀矩形谱积分，得到

$$
\boxed{
I(x)=I_{\rm av}
\left[1+\gamma(x)
\cos\left(\frac{2\pi d x}{\lambda_0D}\right)
\right]
},
$$

其中

$$
\boxed{
\gamma(x)=
\left|
\frac{\sin q}{q}
\right|,
\qquad
q=\frac{\pi d x\Delta\lambda}{\lambda_0^2D}
}.
$$

这就是时间相干包络：离中心越远，所需光程差越大，不同波长的条纹逐渐错开。

## 第11条暗纹处

理想单色光第11条暗纹大约对应

$$
\Delta=\left(10+\frac12\right)\lambda_0.
$$

有限谱宽下，不同波长不能同时严格为暗，因此残余强度不为0。把第11暗纹写成 $k=10$，其中心光程差为

$$
\Delta_{11}=\frac{21}{2}\lambda_0.
$$

因此包络自变量

$$
q=\frac{\pi\Delta_{11}\Delta\lambda}{\lambda_0^2}
=\frac{21\pi\Delta\lambda}{2\lambda_0}
\approx0.25385.
$$

在中央波长的暗纹位置，余弦项等于 $-1$，而中央零级亮纹强度是平均强度的2倍，所以

$$
\frac{I_{11}}{I_{\rm center}}
=\frac{1-\sin q/q}{2}
\approx\boxed{5.35\times10^{-3}}.
$$

这表示该处仍很暗，但不是绝对黑。

::: correctionbox
原书在同一积分式下给出约 $0.0026$。直接代入其写出的 $0.08\pi$ 也得到约 $0.00525$；使用不提前舍入的 $q=21\pi(50)/(2\times6500)$ 得 $0.00535$。因此原书该数值少约2倍，本卷采用独立复算值。
:::

## 第一可见度零点

$\gamma=0$ 的第一零点为 $q=\pi$，所以

$$
\frac{\pi d x_0\Delta\lambda}{\lambda_0^2D}=\pi,
$$

$$
\boxed{x_0=\frac{\lambda_0^2D}{d\Delta\lambda}}.
$$

代入得

$$
x_0=\frac{(650\times10^{-9})^2\times0.80}
{0.10\times10^{-3}\times5\times10^{-9}}
=0.676\,\mathrm m.
$$

故

$$
\boxed{x_0=67.6\,\mathrm{cm}}.
$$

对应光程差

$$
\Delta_0=\frac{dx_0}{D}
=\frac{\lambda_0^2}{\Delta\lambda},
$$

这正是常用的相干长度数量级。

## 易错点

- $R=40$ cm 控制空间相干性；若线光源足够窄，本题时间相干包络主要由 $D,d,\Delta\lambda$ 决定。
- 必须把 $50$ Å 换成 $5$ nm，不能误当50 nm。
- 第一零点是可见度为0，不代表总光强为0，而是干涉调制消失。

### 变式A　高斯谱线

若谱线为高斯分布，波数标准差为 $\sigma_k$，则

$$
\boxed{\gamma(\Delta)=e^{-\sigma_k^2\Delta^2/2}}.
$$

高斯包络没有有限光程差处的严格零点，实验上常用 $1/e$ 宽度定义相干长度。

### 变式B　由第一零点反求谱宽

若测得第一零点在 $x_0=1.00$ m，其余参数不变，则

$$
\Delta\lambda=\frac{\lambda_0^2D}{dx_0}
=\boxed{3.38\,\mathrm{nm}}.
$$

### 变式C　谱宽减半

若 $\Delta\lambda$ 从5 nm降为2.5 nm，则

$$
x_0\propto\frac1{\Delta\lambda},
$$

所以第一零点移到

$$
\boxed{135.2\,\mathrm{cm}}.
$$

窄带滤光片增大时间相干长度。

# 例2.14　为什么均匀超薄膜不形成空间条纹

## 题意恢复

问：若两束反射光来自厚度远小于波长的均匀薄膜，是否一定能看到空间干涉条纹？

## 解答

干涉本身需要相干叠加，但“有干涉”不等于“有空间条纹”。空间条纹要求相位差随观察位置改变。

若薄膜两表面严格平行、厚度 $h$ 处处相同，给定入射角下两束反射光的光程差

$$
\Delta=2nh\cos r
$$

在整个视场近似为常量；反射相位突变也只是常量。因此强度

$$
I=I_1+I_2+2\sqrt{I_1I_2}\cos\delta
$$

在空间上近似均匀，只表现为整体偏亮或偏暗，不形成一条条可分辨的条纹。

当 $h\ll\lambda$ 时，传播相位

$$
\frac{2\pi}{\lambda}2nh\cos r
$$

很小，若没有反射相位差，两个波近似同相；若只有一次 $\pi$ 相位突变，则近似反相。但无论哪种情况，只要厚度均匀，都没有空间变化。
