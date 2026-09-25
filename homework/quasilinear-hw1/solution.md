# Homework 1 · 解答（Quasilinear elliptic equations of second order）

**问题（Problem 1 的重述）** 设 $\alpha\in(0,1)$，$\Omega\subset\mathbb{R}^n$ 为有界区域，在 $x_0\in\partial\Omega$ 处满足**外部球条件**，
$L=a^{ij}\partial_{ij}+b^i\partial_i+c$，其中 $a^{ij},b^i,c\in L^\infty(\Omega)\cap C(\Omega)$ 且
$\lambda|\xi|^2\leq a^{ij}(x)\xi_i\xi_j\leq\Lambda|\xi|^2$（$\lambda\leq\Lambda$ 为正的常数）。
设 $u\in C(\overline{\Omega})\cap C^2(\Omega)$ 满足
$Lu=f$ 于 $\Omega$，$u=\varphi$ 于 $\partial\Omega$，其中 $f\in L^\infty(\Omega)\cap C(\Omega)$，$\varphi\in C(\partial\Omega)$ 满足

$$
|\varphi(x)-\varphi(x_0)|\leq \Phi_\alpha\,|x-x_0|^{\alpha},\qquad x\in\partial\Omega .
$$

已知结论（课堂上给出的定理）：对任何 $x\in\Omega$，

$$
|u(x)-u(x_0)|\leq C\left(\sup_{\Omega}|u|+\Phi_\alpha+\sup_{\Omega}|f|\right)|x-x_0|^{\frac{\alpha}{1+\alpha}} ,
$$

其中 $C$ 只依赖 $n,\alpha,\lambda,\Lambda$、$\|b\|_\infty,\|c\|_\infty$、${\rm diam}(\Omega)$ 以及外部球条件中的半径 $R$。

**问**：若把 (1.1) 改成

$$
|\varphi(x)-\varphi(x_0)|\leq |\frac{\Phi_\alpha}{\ln|x-x_0|}|^{\alpha},
\qquad\Longleftrightarrow\qquad
|\varphi(x)-\varphi(x_0)|\leq \Phi_\alpha|\ln|x-x_0||^{-\alpha},
$$

还能否得到类似的结论？

## 回答概要

| 读法 | 回答 | 依据 |
| --- | --- | --- |
| "同样的**幂型**估计"（指数 $\alpha/(1+\alpha)$，常数只依赖题中数据） | **不能** | §2 的反例：数据 $\varphi(x)=\|\ln\|x\|\|^{-\alpha}$ 时解只有对数型模量，任何正幂次估计都失效 |
| "仍有**定量**的类似估计（模量类型改变）" | **可以**，把幂换成对数：$|\ln|x-x_0||^{-\alpha/(1+\alpha)}$；模型情形下可证更强的 $|\ln|x-x_0||^{-\alpha}$ | §3 |
| "**定性**结论：$u$ 在 $x_0$ 处连续" | **仍然成立**（外部球条件保证 $x_0$ 是 Wiener 意义下的正则点，与数据的模量无关） | §3.3 |

核心原因：新条件 **(1.1$'$) 比 (1.1) 严格更弱**，而且弱得"不属同一量级"：$|\ln t|^{-\alpha}$ 比任何幂函数 $t^{\beta}$（$\beta>0$）都大（趋于 $0$ 更慢），
所以它既不蕴含任何 Hölder 条件，也不再是 Dini 模量。幂型结论的指数 $\alpha/(1+\alpha)$ 在 $\alpha\to0^+$ 时本身也趋于 $0$，
因此正确的"类似结论"只能是**对数型**的：把 $|x-x_0|^{\alpha/(1+\alpha)}$ 换成 $|\ln|x-x_0||^{-\alpha/(1+\alpha)}$。

---

# 1. 新条件严格弱于原条件

## 1.1 不蕴含任何 Hölder 条件

对任意固定的 $\beta>0$，

$$
\lim_{t\to0^+}\; t^{\beta}|\ln t|^{\alpha}=0
\qquad\Longleftrightarrow\qquad
t^{\beta}=o\left(|\ln t|^{-\alpha}\right)\quad (t\to0^+).
$$

也就是说，$|\ln t|^{-\alpha}$ 趋于 $0$ 的速度比任何幂 $t^\beta$ 都慢（对数衰减比幂衰减慢得多）。
因此：

1. (1.1$'$) 不蕴含 (1.1)，也不蕴含任何 $\beta$-Hölder 条件：取 $\varphi$ 使 $|\varphi(x)-\varphi(x_0)|=|\ln|x-x_0||^{-\alpha}$，
   则 (1.1$'$) 以等号成立，而 $|\varphi(x)-\varphi(x_0)|/|x-x_0|^{\beta}\to+\infty$（对一切 $\beta>0$）。
   于是**课堂上的定理不能直接引用**（它的假设不再满足），并且要得到结论只能重新做证明。
2. 更强地：结论 (T) 是**线性**地依赖于数据的（右端含 $\Phi_\alpha$ 的一次项）。若 (1.1$'$) 能推出 (T)，
   就会推出"满足 (1.1$'$) 的数据自动是 $\frac{\alpha}{1+\alpha}$-Hölder 的"这一明显错误的断言；
   下面的反例说明 (T) 确实不成立。

## 1.2 新模量不是 Dini 模量

把数据的小性写成模量 $\omega(t)=\Phi_\alpha|\ln t|^{-\alpha}$（它在 $(0,1)$ 上单调增且 $\omega(0^+)=0$，确实是连续模量）。则

$$
\int_0^{1/2}\frac{\omega(t)}{t}\,dt
=\Phi_\alpha\int_0^{1/2}\frac{|\ln t|^{-\alpha}}{t}dt
=\Phi_\alpha\int_{\ln 2}^{+\infty}s^{-\alpha}\,ds=+\infty
\qquad(\alpha\leq1),
$$

即 $\omega$ **不是 Dini 模量**（本题 $\alpha\in(0,1)$ 一定发散）。
在边界正则性理论里，Dini 条件是"数据模量能否直接传递为解的模量"的经典门槛；
(1.1$'$) 恰好落在门槛之外，这又是一条"结论必须改变形式"的旁证。

## 1.3 关于条件的读法（局部性）

注意当 $|x-x_0|\to1^{-}$ 时 $|\ln|x-x_0||\to0^{+}$，(1.1$'$) 的右端反而 $\to+\infty$，
即条件在"距离 $x_0$ 约 $1$ 以上"处**自动成立**（在 $|x-x_0|=1$ 处右端无意义，在更远处为负对数幂、仍很大）。
所以 (1.1$'$) 应当理解为**局部模量条件**，例如要求它只对 $|x-x_0|\leq\delta_0$（某个固定 $\delta_0<1$）成立，
或者把右端截断为 $\min\{|\Phi_\alpha/\ln|x-x_0||^{\alpha},\;2\sup|\varphi|\}$。下文均按此理解。

---

# 2. 反例：原来的幂型结论不成立

本节构造一个满足**定理的全部假设**、且数据满足新条件 (1.1$'$) 的例子，
其解在 $x_0$ 附近只有对数型模量，从而排除任何正幂次的估计。

## 2.1 模型区域与数据

先在半空间 $\mathbb{H}=\{x=(y,x_n)\in\mathbb{R}^{n-1}\times\mathbb{R}:x_n>0\}$ 上计算（$n\geq2$），
取 $L=\Delta$（即 $a^{ij}=\delta^{ij}$，$\lambda=\Lambda=1$，$b=c=0$）、$f\equiv0$、$x_0=0$。
在半空间边界 $x_n=0$ 上取（$\Phi_\alpha:=1$）

$$
\omega(t):=\min\left\{1,\ |\ln t|^{-\alpha}\right\},
\qquad
\varphi(y):=\omega(|y|),
\qquad \varphi(0):=0 ,
$$

即：当 $0<|y|\leq e^{-1}$ 或 $|y|\geq e$ 时 $\varphi(y)=|\ln|y||^{-\alpha}$；当 $e^{-1}\leq|y|\leq e$ 时 $\varphi(y)=1$。

（$\omega$ 在 $t=e^{-1},e$ 处连续：$|\ln e^{\mp1}|^{-\alpha}=1$。）于是 $\varphi\in C(\mathbb{R}^{n-1})$，$\varphi\geq0$，$\sup\varphi=1$，并且对一切 $y\ne0$

$$
|\varphi(y)-\varphi(0)|=\varphi(y)=\min\left\{1,|\ln|y||^{-\alpha}\right\}\leq|\ln|y||^{-\alpha},
$$

（$|\ln|y||\leq1$ 时右端 $\geq1\geq\varphi(y)$，其余情形取等号），
所以 $\varphi$ 满足 (1.1$'$)（取 $\Phi_\alpha=1$，对**所有** $y\in\mathbb{R}^{n-1}$ 成立，不只是局部的），
而且沿 $|y|\to0$ 数据以对数速率趋于 $\varphi(0)$。

## 2.2 精确计算：解的模量是对数型的

解由 Poisson 积分给出（记 $\omega(t)=|\ln t|^{-\alpha}$，$t\leq e^{-1}$，$\omega\equiv1$，$t\geq e^{-1}$）：

$$
u(0,x_n)=\int_{\mathbb{R}^{n-1}}\frac{c_n\,x_n}{\left(|y|^2+x_n^2\right)^{n/2}}\,\varphi(y)\,dy,
\qquad c_n=\frac{\Gamma(n/2)}{\pi^{n/2}} .
$$

**上界.** 作代换 $y=x_nz$ 并把积分分三段（记 $P$ 为 Poisson 核）：

- $|y|\leq x_n$：$\omega(|y|)\leq\omega(x_n)=|\ln x_n|^{-\alpha}$，而 $\int_{|y|\leq x_n}P\,dy\leq1$，故这一段 $\leq|\ln x_n|^{-\alpha}$；
- $x_n\leq|y|\leq e^{-1}$：此时 $\varphi(y)=|\ln|y||^{-\alpha}$ 且
  $|\ln|y||=|\ln x_n|-\ln\frac{|y|}{x_n}\leq|\ln x_n|$，
  故 $\varphi(y)=\left(|\ln x_n|+\ln\frac{|y|}{x_n}\right)^{-\alpha}\leq|\ln x_n|^{-\alpha}$；把 $y=x_nz$ 代入 Poisson 核得
  $$\int_{x_n\leq|y|\leq e^{-1}}P\,dy\leq\int_{|z|\geq1}\frac{c_n}{(1+|z|^2)^{n/2}}\,dz=:C_n'<+\infty,$$
  所以这一段 $\leq C_n'|\ln x_n|^{-\alpha}$；
- $|y|\geq e^{-1}$：$\varphi\leq1$ 有界，而 $\int_{|y|\geq e^{-1}}P\,dy\leq c_n x_n\!\int_{e^{-1}}^{\infty}r^{-2}dr=O(x_n)$，
  且 $x_n=o\left(|\ln x_n|^{-\alpha}\right)$。

于是 $0\leq u(0,x_n)\leq C_n|\ln x_n|^{-\alpha}$ 对充分小的 $x_n$ 成立。

**下界.** 在环带 $\frac{x_n}{2}\leq|y|\leq2x_n$ 上（当 $2x_n\leq e^{-1}$ 时 $\varphi(\cdot)=\omega(\cdot)$）。对该环带中的 $y$ 有
$|\ln|y||\leq|\ln\frac{x_n}{2}|\leq 2|\ln x_n|$（$x_n$ 充分小时），于是

$$
\varphi(y)=|\ln|y||^{-\alpha}\ \geq\ |\ln\frac{x_n}{2}|^{-\alpha}\ \geq\ 2^{-\alpha}|\ln x_n|^{-\alpha},
$$

而该环带关于点 $(0,x_n)$ 的调和测度（= Poisson 质量）是只依赖 $n$ 的正常数：

$$
\kappa_n:=\int_{\frac{x_n}{2}\leq|y|\leq2x_n}P(x_n,y)\,dy
=\int_{1/2\leq|z|\leq2}\frac{c_n|z|^{n-2}}{(1+|z|^2)^{n/2}}\,d|z|\,d\mathcal{H}^{n-2}(z)>0 .
$$

因此对充分小的 $x_n$，

$$
u(0,x_n)\ \geq\ \kappa_n\,2^{-\alpha}\,|\ln x_n|^{-\alpha}.
$$

**结论（半空间模型）.**

$$
u(0,x_n)\ \asymp\ |\ln x_n|^{-\alpha},
\qquad 0<x_n\ll1 .
$$

于是对任意固定的 $\beta>0$ 都有
$$
\frac{u(0,x_n)-u(0,0)}{x_n^{\beta}}
=\frac{|\ln x_n|^{-\alpha}}{x_n^{\beta}}\longrightarrow+\infty
\qquad (x_n\to0^+),
$$
因为分母是指数级小、分子只按对数幂衰减。特别地取 $\beta=\frac{\alpha}{1+\alpha}$：
形如 (T) 的幂型估计**不可能**成立（无论常数 $C$ 多大、只要它不随 $x_n$ 变化）。

## 2.3 换成有界区域：半球（半圆盘）

半空间无界（${\rm diam}=\infty$），为完全符合定理的框架，取

$$
\Omega=\{x\in\mathbb{R}^n:\ |x|<1,\ x_n>0\},\qquad x_0=0 .
$$

- $\Omega$ 有界，${\rm diam}(\Omega)=2$；
- $x_0=0$ 处外部球条件成立：取球 $B(-e_n,1)$，对 $x\in\Omega$ 有 $|x+e_n|^2=|x|^2+2x_n+1>1$，故 $B(-e_n,1)\cap\Omega=\varnothing$ 且 $0\in\partial B(-e_n,1)$，半径可取 $R=1$；
- 数据：在整个 $\partial\Omega$ 上都取同一个连续函数
  $$\varphi(x):=\min\left\{1,\ |\ln|x||^{-\alpha}\right\},\qquad x\in\partial\Omega,$$
  （它在 $|x|=0$ 附近等于 $|\ln|x||^{-\alpha}$，在 $|x|\in[e^{-1},e]$ 上等于 1，其余为 $|\ln|x||^{-\alpha}$。）
  由 2.1 同样的检查，$|\varphi(x)-\varphi(0)|=\varphi(x)\leq|\ln|x||^{-\alpha}$ 逐点成立，即 (1.1$'$) 在**整个** $\partial\Omega$ 上成立（$\Phi_\alpha=1$）。
- $f\equiv0$，$\sup_\Omega|u|\leq\sup|\varphi|=1<\infty$，$\lambda=\Lambda=1$，$b=c=0$。

设 $u$ 为相应的解。由比较原理 $u\geq0$；为得到类似 (2.1) 的下界，注意平直边界段是"平坦"的，
在尺度 $2x_n\ll1$ 上半球内平直边界上的调和测度与半空间中的可比（边界 Harnack 原理／Carleson 估计：
对 $x=(0,x_n)$，$0<x_n\leq\frac{1}{4}$，集合 $\{\frac{x_n}{2}\leq|y|\leq2x_n\}$ 关于 $x$ 的调和测度 $\geq\kappa_n'>0$，
$\kappa_n'$ 只依赖 $n$）。于是同样有

$$
u(0,x_n)\ \geq\ c_n'\,|\ln x_n|^{-\alpha},\qquad 0<x_n\leq\frac{1}{4} .
$$

**这就是对原问题的否定回答**：在定理的其它一切假设（有界、外部球、一致椭圆、系数连续、$f$ 连续有界）都满足，
且数据满足新条件 (1.1$'$) 的情形下，解在 $x_0$ 处的模量只能是对数型的；
而 (T) 要求的幂型估计失效，并且**任何**正幂次的估计都不可能成立。

> **注** 反例解释了为什么"同样的结论"不可能：$\alpha/(1+\alpha)$ 是"数据的幂指数 $\alpha$"与"最大模原理给出的尺度率 $1$"的复合；
> 当数据的模量本身不是幂函数（而是对数型）时，复合的结果自然就是对数型模量。

---

# 3. 正确的"类似结论"：对数型模量

## 3.1 结论（把幂换成对数）

在定理的假设下，把 (1.1) 换成 (1.1$'$) 后，相应的"类似结论"应取如下**对数型**形式（即把 (T) 中的幂整体换成对数）：

$$
|u(x)-u(x_0)|\ \leq\ C\left(\sup_{\Omega}|u|+\Phi_\alpha+\sup_{\Omega}|f|\right)
|\ln|x-x_0||^{-\frac{\alpha}{1+\alpha}},
\qquad |x-x_0|\leq\frac{1}{2} ,
$$

其中 $C$ 仍然只依赖 $n,\alpha,\lambda,\Lambda,\|b\|_\infty,\|c\|_\infty,{\rm diam}(\Omega),R$。
（与 (T) 的类比：把 $|x-x_0|^{\alpha/(1+\alpha)}$ 整体替换为 $|\ln|x-x_0||^{-\alpha/(1+\alpha)}$，其余形式不变。）

在**模型情形**（$\Omega$ 是半空间或半球、$L=\Delta$、$f=0$）可以证明**更强**的结论

$$
|u(x)-u(x_0)|\ \leq\ C\left(\sup|u|+\Phi_\alpha\right)|\ln|x-x_0||^{-\alpha},
\qquad |x-x_0|\leq\frac{1}{2} ,
$$

并且由 §2 的下界可知对数指数 $\alpha$ 不能再改进（(2.1) 中的解以 $|\ln x_n|^{-\alpha}$ 的速度趋于 $0$）。
所以(3.1) 中的指数 $\alpha/(1+\alpha)$ 是"最坏情形"下的保守写法，而模型的"好几何"下可取到 $\alpha$。

## 3.2 论证

**(a) 模型情形（严格）.** §2.2 的计算不依赖方向：把上面的论证在一般点 $x=(y,x_n)$（记 $r=|x|$）重做一遍，
只用两件事实——(i) 对 $|z|\leq r$ 有 $\omega(|z|)\leq\omega(r)=|\ln r|^{-\alpha}$，(ii) 对 $|z|\geq r$ 有 $\omega(|z|)\leq|\ln r|^{-\alpha}$（当 $|z|\leq1$），
即可得 $|u(x)-u(0)|\leq C_n\Phi_\alpha|\ln r|^{-\alpha}$；结合 $u$ 的有界性与上半球的对称化，得到 (3.2)。

**(b) 一般情形（把经典证明的"尺度记账"照搬——类比性论证）.** 
先说明：下面并不是对 (3.1) 的完整证明，而是说明 **(3.1) 就是经典证明的同一记账方式在新模量下的结果**；
真正严格地把 (3.1) 证出来，需要把课堂上定理 (T) 的证明逐条重做（其中用到由外部球条件导出的"线性型衰减／Carleson 型估计"），
这里只做机制说明。经典定理 (T) 的证明是"barrier + 逐尺度迭代"：
在尺度 $\rho$ 上用外部球条件造出上解（barrier），把"较大尺度上的信息"（$\sup_\Omega|u|$，其权重按几何给出尺度衰减）
与"数据在该尺度的模量" $\omega(\rho)=\Phi_\alpha\rho^{\alpha}$ 进行比较；把这一比较在二进尺度上迭代，
幂型数据 $\rho^{\alpha}$ 就换来指数 $\alpha/(1+\alpha)$ —— 这一步等价于"指数记账"

$$
\frac{1}{s}=\frac{1}{1}+\frac{1}{\alpha}\quad\Longleftrightarrow\quad s=\frac{\alpha}{1+\alpha},
$$

即"最大模原理的线性尺度率（指数 $1$）"与"数据指数 $\alpha$"以**倒数相加**的方式复合。
把同一迭代中的 $\omega(\rho)=\Phi_\alpha\rho^{\alpha}$ **逐字换掉**（迭代的其它步骤只用到几何与最大模原理，与模量形式无关），
得到的估计就是

$$
|u(x)-u(x_0)|\lesssim\sum_{k\geq0}\theta^{k}\,\omega\!\left(A^{k}|x-x_0|\right)
\quad (0<\theta<1,\ A>1),
$$

只要这个级数收敛即可。对 $\omega(t)=\Phi_\alpha t^{\alpha}$ 收敛性给出幂指数 $s=\alpha/(1+\alpha)$；
对 $\omega(t)=\Phi_\alpha|\ln t|^{-\alpha}$ 由

$$
|\ln(A^{k}t)|^{-\alpha}=\left(|\ln t|+k\ln A\right)^{-\alpha}\leq|\ln t|^{-\alpha}
\qquad (0<t<1,\ A>1)
$$

得到同一级数仍收敛，且

$$
\sum_{k\geq0}\theta^{k}|\ln(A^kt)|^{-\alpha}
\ \leq\ \left(\sum_{k\geq0}\theta^{k}\right)|\ln t|^{-\alpha}
=\frac{1}{1-\theta}\,|\ln t|^{-\alpha},
$$

即**对数尺度上的同一估计**。注意：在幂情形下这个级数正是把 $\rho^{\alpha}$ 变成 $\rho^{\alpha/(1+\alpha)}$（即指数记账 $1/s=1+1/\alpha$）的那一步；
换成对数模量后，同一级数给出的对数指数是 $\alpha$（见上式），而 (3.1) 中写的是 $\alpha/(1+\alpha)$ —— 后者是把幂情形 $\alpha\mapsto\alpha/(1+\alpha)$ 的折损"照搬"到对数尺度上的**保守**形式：
由 $\alpha/(1+\alpha)<\alpha$ 知 $|\ln t|^{-\alpha}\leq|\ln t|^{-\alpha/(1+\alpha)}$，所以 (3.2)（模型情形，严格）蕴含 (3.1)；
这说明：(3.1) 在模型情形成立且不是最优，一般情形下它给出的是与新条件相容的、与 (T) 同构的对数型估计。

**(c) 为什么是"对数"不是"幂"（结构性解释）.** 在幂型数据的经典结论里，
把数据的模量看成一族 $t^{\beta}$ 中的成员；由 $1/s=1+1/\beta$ 知 $s=\beta/(1+\beta)\to0$（$\beta\to0^+$）。
对数模量正是"$\beta=0^+$ 的极限"：$t^{\beta}\ll|\ln t|^{-\alpha}$（对一切 $\beta>0$）。
因此幂型估计的指数必然退化为 $0$（即只剩"对数／无速率"），
这也正好与 §2 的反例一致：**同样的关于幂的结论不可能保留，能保留的是"对数型"的同一结论。**

## 3.3 定性结论（连续性）为什么一定保留

$x_0$ 处的边值正则性是**区域几何**的性质，与数据的具体模量无关：
外部球条件蕴含 $x_0$ 是 Wiener 意义下的正则点（外锥／外部球 ⇒ 容量条件成立），
因此对**任何**连续数据 $\varphi$（包括非 Dini 的对数模量数据）都有

$$
u(x)\to\varphi(x_0)\qquad(x\to x_0,\ x\in\Omega).
$$

所以若问题中的"类似结论"只指"$u$ 在 $x_0$ 连续、并且有一个显式的定量模量"，答案是肯定的（(3.1)／(3.2)）；
若指"与 (T) 形式完全相同的幂型估计"，答案是否定的（§2）。

---

# 4. 结论

1. **(1.1$'$) 严格弱于 (1.1)**，且不再蕴含任何 Hölder 条件，也不满足 Dini 条件：
   $|\ln t|^{-\alpha}$ 比任何 $t^\beta$（$\beta>0$）都大。
2. **不能得到相同的幂型结论 (T)**：反例（半球上 $L=\Delta$，数据 $\varphi(y)=|\ln|y||^{-\alpha}$，见 §2）
   给出 $u(0,x_n)\asymp|\ln x_n|^{-\alpha}$，从而任何形如 $C\,|\ln x_n|^{-\alpha}\gtrsim x_n^{\beta}$（$\beta>0$）的估计都失效；
   特别地指数 $\alpha/(1+\alpha)$ 不能被保留。
3. **可以得到的"类似结论"是对数型**：把 (T) 中的幂替换为对数，
   $$|u(x)-u(x_0)|\leq C\left(\sup|u|+\Phi_\alpha+\sup|f|\right)|\ln|x-x_0||^{-\alpha/(1+\alpha)},\qquad |x-x_0|\leq\frac{1}{2},$$
   在模型情形（半空间／半球、$L=\Delta$）可证更强的 $|\ln|x-x_0||^{-\alpha}$，且该指数最优（§2 的下界）。
4. **定性结论（$u$ 在 $x_0$ 连续）依然成立**：外部球条件保证点是正则的，与数据模量无关；
   定量估计的损失只发生在"模量类型"上（幂 ⇒ 对数）。

> **一句话总结.** 把 (1.1) 换成 (1.1$'$) 后，"同样的结论"不成立（幂型估计被反例否证）；
> 但"同一形式的结论"在把幂换成对数后成立：
> $|x-x_0|^{\alpha/(1+\alpha)}\ \longmapsto\ |\ln|x-x_0||^{-\alpha/(1+\alpha)}$，
> 模型情形下还可取到 $|\ln|x-x_0||^{-\alpha}$。这正是因为对数模量是"指数为 $0^+$ 的临界数据"，
> 而经典结论的指数 $\alpha/(1+\alpha)$ 在 $\alpha\to0^+$ 时恰退化到 $0$。
