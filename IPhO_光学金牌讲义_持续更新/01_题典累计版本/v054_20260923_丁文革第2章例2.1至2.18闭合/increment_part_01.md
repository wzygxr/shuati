
- 不能直接把1 mm当作双源间距；真正双源是两个像点。
- 屏距应从像源平面量起，即390 cm，而不是450 cm。
- 两半由同一入射光分出，天然保持相干。

### 变式A　屏移到透镜后300 cm

像源仍在透镜后60 cm，故 $D=240$ cm，

$$
\Delta x=\frac{0.5\,\mu\mathrm m\times2.40\,\mathrm m}{1.2\,\mathrm{mm}}
=\boxed{1.00\,\mathrm{mm}}.
$$

### 变式B　改用632.8 nm激光

几何不变，条纹间距按波长成正比：

$$
\Delta x=1.625\times\frac{632.8}{500}
=\boxed{2.056\,\mathrm{mm}}.
$$

### 变式C　怎样区分这是干涉还是单缝衍射

改变两半透镜的分离量：若条纹间距随像源间距的倒数变化，说明是两像源干涉；改变每半透镜口径主要改变包络宽度而不是细条纹间距。实验上同时观察“细条纹”和“慢变包络”，即可区分两种尺度。

# 例2.5　暗条纹“角宽度”与介质中的变化

## 题意恢复

用波长

$$
\lambda=589\,\mathrm{nm}
$$

的单色光作杨氏双缝实验。测得远处暗条纹的角宽度为

$$
\Delta\theta=0.02^\circ.
$$

求双缝间距。若整个装置浸入折射率 $n=1.33$ 的介质中，暗条纹角宽度是多少？

::: correctionbox
原书把“暗条纹角宽度”定义为一条暗纹中心到相邻亮纹中心的角距离，也就是通常同类条纹角间距的一半。不同教材有时把相邻暗纹中心的距离称为条纹角宽度。两种约定相差2倍，必须先声明口径。
:::

## 逐步解答

杨氏双缝远场中，同类条纹的角间距近似为

$$
\Delta\theta_{\rm same}\simeq\frac{\lambda}{d}.
$$

暗纹中心到相邻亮纹中心的角距离为半周期：

$$
\Delta\theta=\frac{\lambda}{2d}.
$$

因此

$$
\boxed{d=\frac{\lambda}{2\Delta\theta}}.
$$

先把角度换成弧度：

$$
0.02^\circ=0.02\times\frac{\pi}{180}
=3.4907\times10^{-4}\,\mathrm{rad}.
$$

于是

$$
\begin{aligned}
d
&=\frac{589\times10^{-9}}
{2\times3.4907\times10^{-4}}\\
&=8.44\times10^{-4}\,\mathrm m.
\end{aligned}
$$

故

$$
\boxed{d\approx0.84\,\mathrm{mm}}.
$$

浸入折射率为 $n$ 的介质后，频率不变，波长变为

$$
\lambda'=\frac{\lambda}{n}.
$$

所以

$$
\Delta\theta'
=\frac{\lambda'}{2d}
=\frac{\Delta\theta}{n}
=\frac{0.02^\circ}{1.33}
\approx\boxed{0.0150^\circ}.
$$

## 自检

介质中波长缩短，条纹应变密，角宽度应减小；结果符合直觉。

### 变式A　采用“相邻暗纹中心间距”定义

若题目所说的 $0.02^\circ$ 是相邻暗纹中心的角间距，则

$$
\Delta\theta_{\rm same}=\frac{\lambda}{d},
$$

因此

$$
\boxed{d=1.69\,\mathrm{mm}}.
$$

这正好是原书口径结果的2倍。

### 变式B　由浸液前后角宽度反求折射率

若空气中为 $0.030^\circ$，液体中为 $0.0225^\circ$，则

$$
\boxed{n=\frac{0.030}{0.0225}=1.333}.
$$

这个方法与双缝间距无关，适合做液体折射率的相对测量。

### 变式C　改用532 nm激光

双缝不变，原书口径下

$$
\Delta\theta_{532}
=0.02^\circ\times\frac{532}{589}
=\boxed{0.0181^\circ}.
$$

# 例2.6　双缝后加会聚透镜：应使用双缝的像

## 题意恢复

双缝 $S_1,S_2$ 的间距为

$$
d=0.2\,\mathrm{mm}.
$$

其右侧 $8\,\mathrm{cm}$ 处放一焦距为 $10\,\mathrm{cm}$ 的薄凸透镜，观察屏在透镜右侧 $12\,\mathrm{cm}$。入射波长

$$
\lambda=0.5\,\mu\mathrm m.
$$

求屏上条纹间距。

\begin{center}
\includegraphics[width=.76\textwidth]{figures_v054/fig_2_6_lens_young.png}
\end{center}

::: corebox
双缝位于凸透镜焦内，透镜给每个缝成一个放大的虚像。到达屏幕的两束光，等效于从这两个虚像发出；所以用虚像间距和虚像到屏的距离代入杨氏公式。
:::

## 第一步：求双缝的虚像位置

采用笛卡尔符号，薄透镜公式

$$
\frac1f=\frac1u+\frac1v.
$$

这里 $f=10\,\mathrm{cm}$、$u=8\,\mathrm{cm}$，所以

$$
\frac1v=\frac1{10}-\frac18=-\frac1{40},
$$

$$
\boxed{v=-40\,\mathrm{cm}}.
$$

负号表示虚像在透镜左侧40 cm。

横向放大率

$$
m=-\frac vu=5,
$$

故两个虚像间距

$$
\boxed{d'=md=5\times0.2=1.0\,\mathrm{mm}}.
$$

## 第二步：求虚像源到屏幕的距离

屏在透镜右侧12 cm，虚像在左侧40 cm，因此

$$
D=40+12=52\,\mathrm{cm}=0.52\,\mathrm m.
$$

## 第三步：代入杨氏公式

$$
\Delta x=\frac{\lambda D}{d'}
=\frac{0.5\times10^{-6}\times0.52}{1.0\times10^{-3}}
=2.6\times10^{-4}\,\mathrm m.
$$

所以

$$
\boxed{\Delta x=0.26\,\mathrm{mm}}.
$$

## 易错点

1. 把原双缝间距 $0.2$ mm 直接代入，会大5倍。
2. 把屏距写成12 cm，会小很多。
3. 虚像虽然“不能直接接屏”，却完全可以作为后续干涉的等效相干源。

### 变式A　屏放到透镜右侧32 cm

虚像仍在左侧40 cm，故 $D=72$ cm：

$$
\Delta x=\frac{0.5\,\mu\mathrm m\times0.72\,\mathrm m}{1.0\,\mathrm{mm}}
=\boxed{0.36\,\mathrm{mm}}.
$$

### 变式B　双缝恰在透镜前焦面

若 $u=f$，每个缝经透镜后成为一束平行光，两束夹角约为 $d/f$，条纹间距

$$
\boxed{\Delta x=\frac{\lambda f}{d}},
$$

并且近似不随屏到透镜的距离改变。

### 变式C　去掉透镜

双缝到屏距离为 $8+12=20$ cm，故

$$
\Delta x_0=\frac{0.5\,\mu\mathrm m\times0.20\,\mathrm m}{0.2\,\mathrm{mm}}
=\boxed{0.50\,\mathrm{mm}}.
$$

加透镜后条纹反而变为0.26 mm，因为虚像源间距被放大5倍，而传播距离只增加到0.52 m。

# 例2.7　插入玻璃片后的条纹移动与色散

## 题意恢复

杨氏双缝参数为

$$
\lambda=0.55\,\mu\mathrm m,
\quad d=3.3\,\mathrm{mm},
\quad L=3.0\,\mathrm m.
$$

1. 求条纹间距；
2. 在一个缝后插入厚度 $h=0.01\,\mathrm{mm}$ 的平行玻璃片，条纹移动 $4.73\,\mathrm{mm}$，求玻璃折射率；
3. 若色散满足

$$
n(\lambda)=n_0+\frac{A}{\lambda^2},
\qquad n_0=1.5,
\quad A=0.00605,
$$

其中 $\lambda$ 用 $\mu\mathrm m$ 表示，写出各波长亮纹位置。

\begin{center}
\includegraphics[width=.72\textwidth]{figures_v054/fig_2_7_plate_shift.png}
\end{center}

## 第一问：条纹间距

$$
\Delta x=\frac{\lambda L}{d}
=\frac{0.55\times10^{-6}\times3.0}{3.3\times10^{-3}}
=5.0\times10^{-4}\,\mathrm m.
$$

故

$$
\boxed{\Delta x=0.50\,\mathrm{mm}}.
$$

## 第二问：由条纹移动求折射率

玻璃片引入附加光程差

$$
\Delta_{\rm g}=(n-1)h.
$$

移动条纹数

$$
N=\frac{\Delta_{\rm g}}{\lambda}
=\frac{\Delta X}{\Delta x}.
$$

因此

$$
n-1=\frac{\lambda}{h}\frac{\Delta X}{\Delta x}.
$$

代入

$$
N=\frac{4.73}{0.50}=9.46,
$$

$$
n=1+\frac{0.55\,\mu\mathrm m}{10\,\mu\mathrm m}\times9.46
=1.5203.
$$

所以

$$
\boxed{n\approx1.52}.
$$

## 第三问：含色散时的亮纹位置

设在下缝后插片，并取几何光程差为 $dx/L$。亮纹条件写成

$$
\frac{dx}{L}+(n-1)h=k\lambda.
$$

因此

$$
x_k(\lambda)=\frac{L}{d}
\left[k\lambda-(n-1)h\right].
$$

代入

$$
n-1=0.5+\frac{0.00605}{\lambda^2},
\quad
\frac{L}{d}=\frac{3000}{3.3}=909.09,
\quad h=0.01\,\mathrm{mm}=10\,\mu\mathrm m,
$$

若 $x$ 用 mm、$\lambda$ 用 $\mu$m，则

$$
\boxed{
x_k(\lambda)
=0.9091k\lambda-4.5455-\frac{0.0550}{\lambda^2}
\quad(\mathrm{mm})
}.
$$

::: correctionbox
原书最后写成近似 $-0.91(5-k\lambda+0.00605/\lambda^2)$ mm。展开后色散项系数为 $0.00551/\lambda^2$，比由原题 $h=0.01$ mm 直接推导所得 $0.0550/\lambda^2$ 小10倍。本卷采用上面的光程差方程作为可复算更正。
:::

## 方向判断

条纹向插片一侧移动还是反向移动，取决于坐标轴与“哪一缝插片”的定义。本题最稳妥的做法是先写带符号光程差方程，再由结果判断；不要只背“向玻璃片移动”。

### 变式A　玻璃片放到另一缝后

附加光程差的符号反向，所以所有条纹位移方向反向，而位移大小不变：

$$
|\Delta X|=4.73\,\mathrm{mm}.
$$

### 变式B　两缝后都插入玻璃片

若厚度和折射率完全相同，两路新增光程相同，相对光程差不变，因此

$$
\boxed{\text{条纹不移动}}.
$$

若厚度分别为 $h_1,h_2$，则只需用 $(n-1)(h_1-h_2)$ 代替 $(n-1)h$。

### 变式C　白光下零级条纹为何仍有颜色分离

当 $n$ 随 $\lambda$ 变化时，补偿条件

$$
\frac{dx}{L}+(n(\lambda)-1)h=0
$$

对不同波长给出不同位置。即使选择“零级” $k=0$，由于色散项 $A/\lambda^2$，不同颜色也不能完全重合，可用来测量材料色散。

# 例2.8　玻璃片厚度变化引起中心亮暗振荡

## 题意恢复

杨氏双缝极窄，在一个缝后插入折射率为 $n$、厚度为 $h$ 的玻璃片。未插片时屏中心强度为 $I_0$。求插片后中心强度随 $h$ 的变化，并求中心最暗时的厚度。

## 逐步解答

屏中心的几何光程差为0。插片只给一路增加

$$
\Delta=(n-1)h.
$$

相位差

$$
\delta=\frac{2\pi}{\lambda}(n-1)h.
$$

两束等强时，若未插片中心亮纹强度记为 $I_0=4I_1$，则

$$
I(h)=4I_1\cos^2\frac\delta2.
$$

所以

$$
\boxed{
I(h)=I_0\cos^2\left[\frac{\pi(n-1)h}{\lambda}\right]
}.
$$

中心最暗要求

$$
\frac{\pi(n-1)h}{\lambda}
=\left(k+\frac12\right)\pi,
$$

从而

$$
\boxed{
h_k=\frac{(2k+1)\lambda}{2(n-1)},
\qquad k=0,1,2,\ldots
}.
$$

最小正厚度

$$
\boxed{h_{\min}=\frac{\lambda}{2(n-1)}}.
$$

## 自检

当 $h=0$ 时，$I=I_0$；厚度每增加

$$
\Delta h=\frac{\lambda}{n-1}
$$

中心明暗完成一个周期。

### 变式A　两束强度不等

若两束强度为 $I_1,I_2$，则

$$
I(h)=I_1+I_2+2\sqrt{I_1I_2}
\cos\frac{2\pi(n-1)h}{\lambda}.
$$

最暗时

$$
I_{\min}=(\sqrt{I_1}-\sqrt{I_2})^2,
$$

不再一定为0。

### 变式B　求所有中心最亮厚度

最亮要求相位差为 $2k\pi$：

$$
\boxed{h_k=\frac{k\lambda}{n-1}},
\qquad k=0,1,2,\ldots
$$

### 变式C　两路分别插入不同玻璃片

若两片参数为 $(n_1,h_1)$、$(n_2,h_2)$，则

$$
\Delta=(n_1-1)h_1-(n_2-1)h_2.
$$

中心最暗条件变成

$$
\boxed{
(n_1-1)h_1-(n_2-1)h_2
=\left(k+\frac12\right)\lambda
}.
$$

# 例2.9　洛埃镜的干涉区、条纹数与插片补偿

## 题意恢复

洛埃镜装置中，波长

$$
\lambda=5000\,\text{\AA}=0.5\,\mu\mathrm m.
$$

点光源距镜面高度 $SP=d/2=1\,\mathrm{mm}$，镜面长度几何参数为

$$
AB=PA=5\,\mathrm{cm},
\qquad BO=190\,\mathrm{cm}.
$$

1. 求屏上干涉区；
2. 求条纹间距与大致条纹数；
3. 在光源附近插入 $n=1.5$ 的云母片，使原来最下面的条纹移到最上面原条纹的位置，求厚度。

\begin{center}
\includegraphics[width=.82\textwidth]{figures_v054/fig_2_9_lloyd.png}
\end{center}

::: corebox
洛埃镜可用虚像法：镜面反射光等效于来自光源关于镜面的虚像 $S'$。于是问题转化为间距 $SS'=2SP$ 的杨氏双源，但反射还多一个 $\pi$ 相位，使几何中心由亮变暗。
:::

## 第一步：确定干涉区

从光源 $S$ 向镜面两端作极限反射光线并延长到屏。由相似三角形，原书几何给出屏上干涉区下、上边界分别距 $O$ 点

$$
\boxed{1.9\,\mathrm{cm}\quad\text{和}\quad3.9\,\mathrm{cm}}.
$$

因此干涉区宽度约

$$
W=2.0\,\mathrm{cm}=20\,\mathrm{mm}.
$$

## 第二步：条纹间距和条纹数

虚像源间距

$$
d_{\rm eff}=SS'=2SP=2\,\mathrm{mm}.
$$

源到屏的有效距离约为

$$
D=PA+AB+BO=5+5+190=200\,\mathrm{cm}=2.0\,\mathrm m.
$$

所以

$$
\Delta x=\frac{\lambda D}{d_{\rm eff}}
=\frac{0.5\times10^{-6}\times2.0}{2\times10^{-3}}
=0.50\,\mathrm{mm}.
$$

故

$$
\boxed{\Delta x=0.50\,\mathrm{mm}}.
$$

干涉区内大约有

$$
N\simeq\frac{20}{0.50}=\boxed{40}
$$

个条纹间隔。

反射相位突变只把亮、暗标签互换，不改变间距与条纹数。

## 第三步：云母片厚度

从最下方条纹移到原来最上方条纹，需平移约39个条纹间隔（40个条纹位置之间有39个间隔）。因此

$$
\frac{(n-1)h}{\lambda}=39.
$$

所以

$$
h=\frac{39\lambda}{n-1}
=\frac{39\times0.5\,\mu\mathrm m}{0.5}
=\boxed{39\,\mu\mathrm m}.
$$

## 易错点

- “约40条条纹”与“从第一条位置移到第四十条位置”之间是39个间隔。
- 洛埃镜中心附近因反射相位突变为暗纹，不能照搬普通杨氏中心亮纹结论。
- 插片放在哪一路决定移动方向，厚度大小由绝对条纹数决定。

### 变式A　证明镜面交线处为暗纹

几何光程在镜面交线附近趋于相等，但反射光从空气到镜面反射产生额外 $\pi$ 相位，所以总相位差为 $\pi$，故中心为暗。

### 变式B　光源高度加倍

$SP$ 从1 mm变为2 mm，虚像源间距加倍到4 mm，条纹间距减半：

$$
\boxed{\Delta x=0.25\,\mathrm{mm}}.
$$

### 变式C　云母片放到另一条光路
