# Paolo Aluffi《Algebra: Chapter 0》Chapter V §4 增量 v021｜Part 5: Exercises 4.21--4.25

> v021 分块阅读文件；继承 v020 的 500 道正式题高水位。本块完整覆盖 4.21--4.25，含 4.25 的命题校正、严格下降与 Mason--Stothers 独立证明。

# 22. V.4.21 UFD 上多项式不可约性的 Gauss 判别

设 $R$ 是 UFD，$K=\operatorname{Frac}(R)$，$f\in R[x]$ 非常数。证明

$$
f\text{ 在 }R[x]\text{ 不可约}
\iff
f\text{ 本原且在 }K[x]\text{ 不可约}.
$$

若 $f$ 在 $R[x]$ 不可约而不本原，则可提出非单位 content：$f=cf_0$，两个因子都非单位，矛盾。Gauss 引理再保证非恒定不可约本原多项式升到 $K[x]$ 后仍不可约。

反向，设 $f$ 本原且在 $K[x]$ 不可约。若 $f=gh$ 于 $R[x]$，同一分解在 $K[x]$ 中迫使某因子为非零常数。设 $g=r\in R\setminus\{0\}$。若 $r$ 非单位，任一不可约因子 $p\mid r$ 都整除 $f$ 的全部系数，与本原性矛盾；故 $r$ 是单位。

易错点：必须排除常数多项式；只在 $K[x]$ 不可约不够，例如 $2x+2$；先取 content。

- 变式 A：$6x^2+12x+6=6(x+1)^2$，可约。
- 变式 B：把 $R$ 换成 $k[x]$、主变量换成 $y$，得到 $k[x,y]$ 的递归判别。
- 同类：$x^3+2x+2$ 本原且无有理根，故在 $\mathbb Z[x]$ 不可约。

---

# 23. V.4.22 从 $k(x)[y]$ 的公因子降回 $k[x,y]$

设 $f,g\in k[x,y]=k[x][y]$ 在 $k(x)[y]$ 中有正 $y$-次数公因子 $q$。清除 $q$ 的系数分母，并把所得多项式除以 content，得到本原 $q_0\in k[x][y]$；$q$ 与 $q_0$ 在 $k(x)[y]$ 中相伴。

Gauss 整除引理说：若本原 $q_0\in R[y]$ 在 $K[y]$ 中整除 $f\in R[y]$，则已在 $R[y]$ 中整除。故

$$
f=q_0F,\qquad g=q_0G
$$

于 $k[x,y]$，且 $\deg_yq_0>0$，所以它是非常数公因子。

易错点：清分母后还要取本原部分；$k(x)[y]$ 的单位是任意非零有理函数。

- 变式 A：参数扩展到 $k[x_1,\ldots,x_m]$ 完全相同。
- 变式 B：逆否命题给出互素性从 $k[x,y]$ 升到 $k(x)[y]$。
- 同类：$y-x$ 同时整除 $y^2-x^2$ 与 $y^2+(1-x)y-x$。

---

# 24. V.4.23 把分式域中的因子同时缩放回原环

设 $R$ 是 UFD，$K=\operatorname{Frac}(R)$，

$$
f=\alpha\beta\in R[x],\qquad \alpha,\beta\in K[x].
$$

分别写成标量乘本原多项式：

$$
\alpha=a\alpha_0,\qquad \beta=b\beta_0,
$$

其中 $\alpha_0,\beta_0\in R[x]$ 本原。Gauss 引理给出 $\alpha_0\beta_0$ 本原，而

$$
f=ab\,\alpha_0\beta_0\in R[x].
$$

写 $ab=r/s$ 为最简分数。若非单位不可约元 $p\mid s$，则因 $p\nmid r$，$p$ 必整除 $\alpha_0\beta_0$ 的每个系数，与本原性矛盾。故 $ab\in R$。

取 $c=a^{-1}$，则

$$
c\alpha=\alpha_0\in R[x],
\qquad
c^{-1}\beta=ab\beta_0\in R[x].
$$

若 $f$ 与 $\alpha$ 都首一，比较首项知 $\beta$ 首一。令 $A=c\alpha,B=c^{-1}\beta\in R[x]$；$A,B$ 的首项系数分别为 $c,c^{-1}$，故二者都在 $R$，于是 $c$ 是单位，最终 $\alpha,\beta\in R[x]$。

- 变式 A：首一整系数多项式在 $\mathbb Q[x]$ 的首一因子自动属于 $\mathbb Z[x]$。
- 变式 B：非首一时两个有理因子可都非整系数，如
  $$
  (2x+1)(x+1)=\left(\frac23(2x+1)\right)\left(\frac32(x+1)\right).
  $$
- 同类：首一 $\alpha\mid f$ 于 $K[x]$ 时，商 $f/\alpha\in R[x]$。

---

# 25. V.4.24 任意一对因子系数的乘积都回到原环

由 4.23 取 $c\in K^\times$ 使

$$
A=c\alpha\in R[x],\qquad B=c^{-1}\beta\in R[x].
$$

若 $a_i,b_j$ 分别是 $\alpha,\beta$ 的系数，$A_i=ca_i$、$B_j=c^{-1}b_j$，则

$$
a_ib_j=(c^{-1}A_i)(cB_j)=A_iB_j\in R.
$$

不能只由卷积系数 $\sum_{i+j=k}a_ib_j\in R$ 推出逐项结论；共同缩放才是关键。

- 变式 A：系数外积矩阵 $(a_ib_j)$ 全部落在 $R$ 中，作为 $K$-矩阵秩至多 1。
- 变式 B：对多因子可递归选择 $c_i$，使 $c_i\alpha_i\in R[x]$ 且 $\prod c_i=1$。
- 同类：$\alpha=(x+1)/6$、$\beta=6x+12$ 的交叉系数乘积为 1 或 2。

---

# 26. V.4.25 复系数多项式费马方程：先校正命题，再严格下降

## 26.1 印刷字面命题若无本原条件是假的

若只说 $n>2$ 时

$$
f^n+g^n=h^n,\qquad f,g,h\in\mathbb C[t]
$$

不存在非恒定解，这是错误的。取常数 $a,b,c\in\mathbb C$ 满足 $a^n+b^n=c^n$，再乘任意非恒定 $F(t)$，就有

$$
(aF)^n+(bF)^n=(cF)^n.
$$

作者第二次印刷勘误表列出了 p.280 Exercise 4.25。数学上必要的校正版是：

> 若 $n>2$，$f^n+g^n=h^n$ 且 $\gcd(f,g,h)=1$，则 $f,g,h$ 全为常数。

等价的完整分类是：任意解都形如

$$
(f,g,h)=F(t)(a,b,c),\qquad a^n+b^n=c^n.
$$

## 26.2 本原解自动两两互素

先处理零元。若 $f=0$，则 $g^n=h^n$，故 $h=\xi g$、$\xi^n=1$；本原性迫使 $g,h$ 为常数。其余零元情形同理。

现在设 $fgh\ne0$。若不可约 $p$ 同时整除 $f,g$，则 $p\mid h^n=f^n+g^n$，UFD 中 $p$ 为素元，故 $p\mid h$，与三元 gcd 为 1 矛盾。因此

$$
\gcd(f,g)=\gcd(g,h)=\gcd(h,f)=1.
$$

若存在本原非恒定反例，从中取

$$
D=\max(\deg f,\deg g,\deg h)>0
$$

最小者。

## 26.3 分圆因子两两互素并各自为 $n$ 次幂

取本原 $n$ 次单位根 $\zeta$。由

$$
f^n=h^n-g^n=\prod_{j=0}^{n-1}(h-\zeta^jg).
$$

若不可约 $p$ 同时整除第 $i,j$ 个因子，二式相减得

$$
p\mid(\zeta^j-\zeta^i)g.
$$

$i\ne j$ 时系数是非零复常数、即单位，所以 $p\mid g$，继而 $p\mid h$，矛盾。因此这些因子两两互素。

在 UFD 中，两两互素元素的乘积若为 $n$ 次幂，则每个因子的每个不可约指数都是 $n$ 的倍数。非零复常数又都有 $n$ 次根，所以可写

$$
h-g=a^n,\qquad h-\zeta g=b^n,\qquad h-\zeta^2g=c^n.
$$

$n>2$ 保证三个根互异。

## 26.4 线性关系制造新的费马解

三个线性式位于 $\operatorname{span}_{\mathbb C}\{h,g\}$ 中，满足显式关系

$$
(\zeta-\zeta^2)(h-g)
+(\zeta^2-1)(h-\zeta g)
+(1-\zeta)(h-\zeta^2g)=0.
$$

代入 $a^n,b^n,c^n$，并给三个非零常数系数取 $n$ 次根，得到

$$
(\lambda a)^n+(\mu b)^n=(\nu c)^n.
$$

原来的三个分圆因子两两互素，所以 $a,b,c$ 两两互素；乘非零常数不改变公因子。故新三元组已经本原，不能含糊地说“再约去 gcd”。

## 26.5 最大次数严格下降

由 $a^n=h-g$ 等式，

$$
\deg a,\deg b,\deg c
\le\frac{\max(\deg g,\deg h)}n.
$$

还须证明 $\max(\deg g,\deg h)=D$。若 $\deg f=D$ 严格大于后二者，则

$$
\deg f^n=nD>\deg(h^n-g^n),
$$

与方程矛盾。因此新解的最大次数至多

$$
\frac Dn<D.
$$

新解不可能全为常数：若 $a,b,c$ 常数，则 $h-g$、$h-\zeta g$ 都常数，相减得 $(1-\zeta)g$ 常数，继而 $g,h,f$ 全常数。

于是从最小 $D$ 的本原非恒定解构造出更小的本原非恒定解，矛盾。校正版得证。任意解除去公共 gcd 后为常数三元组，从而得到完整分类。

## 26.6 为什么整数版不能照搬

这里依赖：$\mathbb C[t]$ 是 UFD；$\mathbb C$ 含全部单位根；不同单位根之差是单位。到分圆整数环后，$1-\zeta$ 等通常不是单位，各因子可能共享位于 $n$ 上方的素因子，且环未必 UFD。Lamé 型论证正会在这些点失效。

## 26.7 易错点与自检

1. 无本原条件时公共因子缩放立即反例；
2. 三元 gcd 为 1 是借助方程才推出两两互素；
3. 逐因子开 $n$ 次方需 UFD + 两两互素；
4. 新解必须验证本原；
5. 次数下降前必须证明 $D$ 由 $g,h$ 中至少一个达到；
6. $n=2$ 确有本原非恒定解：
   $$
   (t^2-1)^2+(2t)^2=(t^2+1)^2.
   $$

## 26.8 变式 A：Mason--Stothers 证明

对两两互素、非零且不全常数的 $A+B=C$，Mason--Stothers 定理给出

$$
\max(\deg A,\deg B,\deg C)<\deg\operatorname{rad}(ABC).
$$

其初等证明可取 $W=A'B-AB'$。若 $W=0$，特征 0 下 $A/B$ 为常数，与互素且不全常数矛盾。若 $p^e\Vert A$，则 $p^{e-1}\mid A'$，故 $p^{e-1}\mid W$；对 $B$ 同理。又因 $C=A+B$，有 $W=A'C-AC'$，所以 $C$ 的重因子也以少一次的幂整除 $W$。两两互素于是给出

$$
\frac{ABC}{\operatorname{rad}(ABC)}\mid W.
$$

比较次数即得定理。

应用到

$$
f^n+g^n=h^n
$$

并令 $D=\max(\deg f,\deg g,\deg h)>0$，有

$$
nD<\deg\operatorname{rad}(f^ng^nh^n)
=\deg\operatorname{rad}(fgh)
\le\deg f+\deg g+\deg h\le3D.
$$

$n>2$ 意味着 $n\ge3$，与 $nD<3D$ 矛盾。

## 26.9 变式 B：正特征反例

在 $\mathbb F_p[t]$（$p>2$）中，Frobenius 给出

$$
t^p+1=(t+1)^p.
$$

取 $f=t,g=1,h=t+1,n=p$，得到本原非恒定解。导数为零也解释了 Mason 证明为何退化。

## 26.10 同类巩固：$n=3$

若 $\zeta^3=1$、$\zeta\ne1$，则

$$
f^3=(h-g)(h-\zeta g)(h-\zeta^2g).
$$

三因子两两互素，故分别为 $a^3,b^3,c^3$。通用线性关系给出新三次费马解；$a,b,c$ 仍两两互素，且新最大次数至多旧值的三分之一。
