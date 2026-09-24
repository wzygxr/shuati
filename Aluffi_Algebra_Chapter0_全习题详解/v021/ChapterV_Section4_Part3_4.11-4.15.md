# Paolo Aluffi《Algebra: Chapter 0》Chapter V §4 增量 v021｜Part 3: Exercises 4.11--4.15

> v021 分块阅读文件；继承 v020 的 500 道正式题高水位。本块完整覆盖 4.11--4.15，不冒充已重建历史 1146 页母版。

# 12. V.4.11 在素理想处局部化得到局部环

## 12.1 题意重述

设 $\mathfrak p\in\operatorname{Spec}R$，令 $S=R\setminus\mathfrak p$、$R_{\mathfrak p}=S^{-1}R$。证明 $R_{\mathfrak p}$ 是局部环，唯一极大理想为 $\mathfrak pR_{\mathfrak p}$；并描述其素理想。

## 12.2 分母集与单位判别

素性保证 $S$ 乘法闭。任取 $a/s\in R_{\mathfrak p}$：

- 若 $a\notin\mathfrak p$，则 $a\in S$，且 $(a/s)(s/a)=1$，所以它是单位；
- 若 $a\in\mathfrak p$，则 $a/s\in\mathfrak pR_{\mathfrak p}$，不可能为单位。

因此

$$
R_{\mathfrak p}^{\times}=\{a/s:a\notin\mathfrak p\},
\qquad
R_{\mathfrak p}\setminus R_{\mathfrak p}^{\times}=\mathfrak pR_{\mathfrak p}.
$$

所有非单位构成理想，就说明它是唯一极大理想。

## 12.3 素理想对应

由局部化的素理想对应，$R_{\mathfrak p}$ 的素理想来自与 $S$ 不交的 $R$-素理想 $\mathfrak q$。这等价于 $\mathfrak q\subseteq\mathfrak p$。故

$$
\{\mathfrak q\in\operatorname{Spec}R:\mathfrak q\subseteq\mathfrak p\}
\longleftrightarrow
\operatorname{Spec}R_{\mathfrak p}.
$$

## 12.4 易错点与自检

分母来自 $R\setminus\mathfrak p$，不是 $\mathfrak p$；局部环可以有多个素理想，但只有一个极大理想。例：$\mathbb Z_{(p)}$ 的非单位恰是分子被 $p$ 整除的分数。

## 12.5 变式 A（显式单位）

在 $\mathbb Z_{(7)}$ 中，$12/25$ 可逆，逆为 $25/12$；$14/15\in7\mathbb Z_{(7)}$，不可逆。

## 12.6 变式 B（二维局部环）

在 $k[x,y]_{(x,y)}$ 中，$f/g$ 是单位当且仅当 $f(0,0)\ne0$；唯一极大理想由分子在原点消失的分数组成。

## 12.7 同类巩固

$$
R_{\mathfrak p}/\mathfrak pR_{\mathfrak p}
\cong\operatorname{Frac}(R/\mathfrak p),
$$

因为商与局部化交换，而 $R/\mathfrak p$ 是整环，分母集的像正是其非零元。

---

# 13. V.4.12 用所有局部化检测一个模是否为零

## 13.1 命题

对 $R$-模 $M$，以下等价：

1. $M=0$；
2. $M_{\mathfrak p}=0$ 对所有素理想成立；
3. $M_{\mathfrak m}=0$ 对所有极大理想成立。

## 13.2 证明

$(1)\Rightarrow(2)\Rightarrow(3)$ 显然。证明 $(3)\Rightarrow(1)$。

若 $M\ne0$，取 $0\ne m\in M$。湮灭理想

$$
\operatorname{Ann}(m)=\{r\in R:rm=0\}
$$

是真理想，故包含于某个极大理想 $\mathfrak m$。断言 $m/1\ne0$ 于 $M_{\mathfrak m}$。否则局部化零判别给出 $s\notin\mathfrak m$ 且 $sm=0$；于是 $s\in\operatorname{Ann}(m)\subseteq\mathfrak m$，矛盾。

所以若所有极大局部化都为零，只能 $M=0$。

## 13.3 易错点与自检

极大理想必须选成包含 $\operatorname{Ann}(m)$，不是任意选择。以 $M=\mathbb Z/6\mathbb Z$ 为例，$(2)$ 与 $(3)$ 处的局部化分别看见不同的挠信息。

## 13.4 变式 A（检测元素）

$$
m=0\iff m/1=0\text{ 于所有 }M_{\mathfrak m}.
$$

对循环子模 $Rm$ 应用原题即可。

## 13.5 变式 B（检测同态）

对 $f,g:M\to N$，有 $f=g$ 当且仅当 $f_{\mathfrak m}=g_{\mathfrak m}$ 对所有极大理想成立。令 $h=f-g$，逐元素应用变式 A。

## 13.6 同类巩固

若 $I_{\mathfrak m}=R_{\mathfrak m}$ 对所有极大理想成立，则

$$
(R/I)_{\mathfrak m}=0
$$

对所有 $\mathfrak m$ 成立，故 $R/I=0$，即 $I=R$。

---

# 14. V.4.13 离散赋值环与 PID 的素点局部化

## 14.1 DVR 的局部性

设 $v:K^\times\to\mathbb Z$ 为满射离散赋值，

$$
R=\{0\}\cup\{x\in K^\times:v(x)\ge0\}.
$$

取 $t$ 使 $v(t)=1$。若 $v(x)=0$，则 $x^{-1}\in R$，所以 $x$ 是单位；若 $v(x)>0$，则 $x^{-1}\notin R$，所以 $x$ 非单位。因此非单位构成

$$
\mathfrak m=\{0\}\cup\{x:v(x)>0\}.
$$

又若 $v(x)=n\ge1$，则 $v(x/t)=n-1\ge0$，故 $x\in(t)$；反向显然，所以 $\mathfrak m=(t)$。这就是唯一极大理想。

任取 $x\in K^\times$：若 $v(x)\ge0$，则 $x=x/1$；若 $v(x)<0$，则 $x=1/x^{-1}$。故 $\operatorname{Frac}(R)=K$。

## 14.2 PID 的非零素点局部化

设 $A$ 是 PID，$\mathfrak p=(\pi)\ne(0)$。对 $0\ne x\in\operatorname{Frac}(A)$，唯一写成

$$
x=\pi^n\frac ab,
\qquad \pi\nmid a,b,
$$

定义 $v_\pi(x)=n$。唯一分解给出赋值公理，并且

$$
x\in A_{\mathfrak p}\iff v_\pi(x)\ge0.
$$

因此 $A_{\mathfrak p}$ 是 DVR，参数为 $\pi$。

## 14.3 边界与易错点

$\mathfrak p=(0)$ 时局部化是分式域，不属于这里的非平凡 DVR 口径；参数只确定到单位；通常记 $v(0)=+\infty$。

## 14.4 变式 A（$t$-进赋值）

$k[t]_{(t)}$ 是 DVR，赋值为 $v_t(f/g)=\operatorname{ord}_t(f)-\operatorname{ord}_t(g)$。

## 14.5 变式 B（不可约多项式处）

若 $q(t)$ 不可约，则 $k[t]_{(q)}$ 是 DVR，参数为 $q$，剩余域为 $k[t]/(q)$。

## 14.6 同类巩固

在 $\mathbb Z_{(5)}$ 中，

$$
\frac{350}{9}=5^2\cdot\frac{14}{9},
$$

且 $14/9$ 是单位，故赋值为 2。

---

# 15. V.4.14 子模的延拓收缩与 Noether 性的局部化

## 15.1 延拓、收缩

对 $N\le M$ 定义 $N^e=S^{-1}N$；对 $L\le S^{-1}M$ 定义

$$
L^c=\{m\in M:m/1\in L\}.
$$

证明

$$
(L^c)^e=L.
$$

若 $m/s\in(L^c)^e$，则 $m/1\in L$，故 $m/s=(1/s)(m/1)\in L$。反向若 $m/s\in L$，乘以单位 $s/1$ 得 $m/1\in L$。

而

$$
(N^e)^c=\{m\in M:\exists s\in S,\ sm\in N\},
$$

即子模的 $S$-饱和。

## 15.2 Noether 性

设 $M$ Noetherian。任取 $L\le S^{-1}M$。其收缩有限生成：

$$
L^c=Rm_1+\cdots+Rm_r.
$$

于是

$$
L=(L^c)^e=(S^{-1}R)\frac{m_1}{1}+\cdots+(S^{-1}R)\frac{m_r}{1}.
$$

故 $S^{-1}M$ Noetherian。取 $M=R$，得到 Noetherian 环的任意局部化仍 Noetherian。

## 15.3 易错点与自检

不需要 $S^{-1}R$ 作为 $R$-模有限生成；分母全部吸收到系数中。例：$\mathbb Z[1/6]$ 与 $\mathbb Z_{(p)}$ 都 Noetherian。

## 15.4 变式 A（有限生成模）

若 $M=\sum_{i=1}^rRm_i$，则

$$
S^{-1}M=\sum_{i=1}^r(S^{-1}R)(m_i/1).
$$

## 15.5 变式 B（ACC 证明）

对子模升链 $L_1\subseteq L_2\subseteq\cdots$ 收缩到 $M$；收缩链稳定后，用 $L_i=(L_i^c)^e$ 推回原链稳定。

## 15.6 同类巩固

若 $R$ Noetherian，则 $R_f=R[1/f]$ Noetherian，取 $S=\{1,f,f^2,\ldots\}$ 即可。

---

# 16. V.4.15 UFD 的局部化仍为 UFD

## 16.1 哪些素因子成为单位

设 $p\in R$ 不可约。UFD 中 $p$ 是素元。断言

$$
p/1\text{ 在 }S^{-1}R\text{ 中可逆}
\iff (p)\cap S\ne\varnothing.
$$

若 $s=pa\in S$，则 $(p/1)(a/s)=1$。反之若 $(p/1)(a/s)=1$，分数相等给出某个 $u\in S$ 满足 $us=upa\in(p)$，故 $(p)\cap S\ne\varnothing$。

## 16.2 未被倒置的素因子仍为素元

若 $(p)\cap S=\varnothing$，则 $(p)$ 与 $S$ 不交，局部化后 $S^{-1}(p)$ 仍是素理想，并由 $p/1$ 生成。因此 $p/1$ 是素元。

## 16.3 存在性与唯一性

任取非零非单位 $a/s$。在 $R$ 中分解

$$
a=u p_1\cdots p_r.
$$

于是

$$
\frac as=\frac us\prod_i\frac{p_i}{1}.
$$

把局部化后成为单位的 $p_i/1$ 吸收到单位部分；剩余因子都是素元，得到不可约分解。唯一性由素元整除乘积必整除某因子逐项消去得到。因此 $S^{-1}R$ 是 UFD；其不可约元恰与未被 $S$ 命中的 $p/1$ 相伴。

## 16.4 易错点与自检

不是每个原环不可约元都保留；分母总属于单位部分。在 $\mathbb Z[1/2]$ 中，2 是单位，奇素数仍不可约。

## 16.5 变式 A（局部化多项式 UFD）

$k[x,y][1/x]$ 中，与 $x$ 相伴的因子成为单位；其他不可约多项式的像仍不可约。

## 16.6 变式 B（极端局部化）

若 $S=R\setminus\{0\}$，则所有非零不可约元都成为单位，局部化是分式域，UFD 条件真空成立。

## 16.7 同类巩固

在 $\mathbb Z_{(5)}$ 中，所有普通素数 $q\ne5$ 都成为单位；只有 5 的相伴类仍不可约。
