# Paolo Aluffi《Algebra: Chapter 0》Chapter V §4 增量 v021｜Part 2: Exercises 4.6--4.10

> 这是 v021 的分块阅读文件；计数继承 GitHub v020 的 500 道正式题高水位，本块只承载本轮新解，不冒充已重建历史 1146 页母版。

# 7. V.4.6 PID 分式域中的主分母分解与部分分式

## 7.1 题意重述与勘误化表述

设 $R$ 是 PID，$K$ 是其分式域。原题要求把 $c\in K$ 分解为若干个“互不相伴不可约元的幂”为分母的分式，并证明分母素因子、指数以及分子剩余类的唯一性，再与微积分中的部分分式联系起来。

为消除 $r_i=0$ 所带来的常数项歧义，我们采用等价而更清楚的标准式：

$$
c=r+\sum_{i=1}^m\frac{a_i}{p_i^{e_i}},
$$

其中

- $r\in R$；
- $p_i$ 两两不相伴且不可约；
- $e_i\ge1$；
- $p_i\nmid a_i$；
- 允许 $m=0$，此时 $c\in R$。

唯一性是：$p_i$、$e_i$ 在重排和相伴意义下唯一，并且固定分母后

$$
a_i\equiv b_i\pmod{p_i^{e_i}}.
$$

## 7.2 所用知识

PID 是 UFD；互素主理想的中国剩余定理；最简分母；不可约元在 PID 中是素元。

## 7.3 存在性：从最简分母和 CRT 出发

先把 $c$ 写为最简分数

$$
c=\frac ab,
\qquad a,b\in R,\quad b\ne0,\quad \gcd(a,b)=1.
$$

若 $b$ 是单位，则 $c\in R$，结论显然。否则把分母分解为

$$
b=u\prod_{i=1}^m p_i^{e_i},
$$

其中 $u$ 是单位，$p_i$ 两两不相伴，$e_i\ge1$。把单位吸收到 $a$ 中，可设

$$
b=\prod_{i=1}^m p_i^{e_i}.
$$

令

$$
d_i=\frac{b}{p_i^{e_i}}.
$$

因为 $d_i$ 与 $p_i^{e_i}$ 互素，所以存在 $q_i\in R$ 使

$$
q_id_i\equiv1\pmod{p_i^{e_i}}.
$$

同时对 $j\ne i$，$d_i$ 被 $p_j^{e_j}$ 整除。因此

$$
E_i:=q_id_i
$$

在模 $p_i^{e_i}$ 时为 1，在模其他 $p_j^{e_j}$ 时为 0。由中国剩余定理，

$$
\sum_{i=1}^mE_i\equiv1\pmod b.
$$

故存在 $k\in R$ 使

$$
\sum_iE_i=1+kb.
$$

两边乘 $a/b$：

$$
\frac ab
=
\sum_i\frac{aq_i}{p_i^{e_i}}-ak.
$$

于是可取

$$
r=-ak,
\qquad a_i=aq_i.
$$

还要检查 $p_i\nmid a_i$。因为 $\gcd(a,b)=1$，所以 $p_i\nmid a$；而 $q_i$ 在模 $p_i$ 下可逆，所以 $p_i\nmid q_i$。故 $p_i\nmid a_i$。

## 7.4 唯一性

设有两个标准式

$$
c=r+\sum_{i=1}^m\frac{a_i}{p_i^{e_i}}
=r'+\sum_{j=1}^n\frac{b_j}{q_j^{f_j}}.
$$

把每边通分。由于每个 $a_i$ 不被 $p_i$ 整除，左边约分后的分母中 $p_i$ 的指数恰为 $e_i$；没有其他项能够消掉这个最高负 $p_i$-指数。换成初等语言：乘以

$$
D=\prod_i p_i^{e_i}
$$

后，模 $p_i$ 考察，只有第 $i$ 项可能不被 $p_i$ 整除，所以整个分数不可能把 $p_i$ 从最简分母中消去。

因此 $c$ 的最简分母决定了全部不可约因子及其指数。于是两组 $p_i^{e_i}$ 与 $q_j^{f_j}$ 在重排、相伴意义下相同。

固定同一组分母并记 $D_i=D/p_i^{e_i}$。把两个分解相减并乘 $D$，再模 $p_i^{e_i}$：

$$
(a_i-b_i)D_i\equiv0\pmod{p_i^{e_i}}.
$$

$D_i$ 与 $p_i$ 互素，故 $D_i$ 在 $R/(p_i^{e_i})$ 中可逆，于是

$$
a_i\equiv b_i\pmod{p_i^{e_i}}.
$$

这正是所需唯一性。

## 7.5 与微积分部分分式的关系

取 $R=k[x]$，$K=k(x)$。$k[x]$ 是 PID。对有理函数 $F(x)$ 先做多项式除法，把它写成

$$
F(x)=Q(x)+\frac{A(x)}{B(x)},
\qquad \deg A<\deg B.
$$

再把 $B$ 分解为不可约多项式幂。上面的结论给出

$$
\frac{A}{B}
=
\sum_i\frac{A_i(x)}{p_i(x)^{e_i}}.
$$

继续对每个 $A_i$ 按 $p_i$ 作除法，就得到熟悉的细分形式

$$
\frac{A_i}{p_i^{e_i}}
=
\frac{C_{i,1}}{p_i}+\frac{C_{i,2}}{p_i^2}+\cdots+
\frac{C_{i,e_i}}{p_i^{e_i}},
\qquad \deg C_{i,j}<\deg p_i.
$$

在 $k=\mathbb C$ 时，不可约多项式都是一次式，这正是微积分中的线性因子部分分式；在 $k=\mathbb R$ 时还会出现不可约二次因子。

## 7.6 易错点

1. 把“分母互素”错写成“分子彼此互素”；真正需要的是 $p_i\nmid a_i$。
2. 忽略整数/多项式部分 $r$ 会导致 $r_i=0$ 时的非唯一表达。
3. 唯一的是分子模 $p_i^{e_i}$ 的剩余类，不是任意选定的分子代表元。

## 7.7 自检

在 $\mathbb Q$ 中，

$$
\frac{17}{60}
=
\frac{17}{2^2\cdot3\cdot5}.
$$

CRT 可给出一种分解。为避免凭感觉凑数，令

$$
\frac{17}{60}=r+\frac a4+\frac b3+\frac c5.
$$

需解 $15a+20b+12c\equiv17\pmod{60}$。分别模 $4,3,5$ 得

- $a\equiv3\pmod4$；
- $b\equiv1\pmod3$；
- $c\equiv1\pmod5$。

取 $a=3,b=1,c=1$，左边为 $77=17+60$，故

$$
\frac{17}{60}=-1+\frac34+\frac13+\frac15.
$$

通分为 $(-60+45+20+12)/60=17/60$，正确。

## 7.8 变式 A（本质不同：整数 CRT 分解）

**题目。** 把 $29/84$ 写成

$$
r+\frac a4+\frac b3+\frac c7
$$

的形式。

**解答。** 解

$$
21a+28b+12c\equiv29\pmod{84}.
$$

分别取模：

- 模 4：$a\equiv1$；
- 模 3：$b\equiv2$；
- 模 7：$5c\equiv1$，故 $c\equiv3$。

取 $a=1,b=2,c=3$，左边 $21+56+36=113=29+84$。故

$$
\frac{29}{84}=-1+\frac14+\frac23+\frac37.
$$

## 7.9 变式 B（本质不同：有理函数部分分式）

**题目。** 在 $\mathbb Q(x)$ 中分解

$$
\frac{2x+3}{x(x-1)^2}.
$$

**解答。** 设

$$
\frac{2x+3}{x(x-1)^2}
=
\frac A x+\frac B{x-1}+\frac C{(x-1)^2}.
$$

乘以 $x(x-1)^2$：

$$
2x+3=A(x-1)^2+Bx(x-1)+Cx.
$$

令 $x=0$ 得 $A=3$；令 $x=1$ 得 $C=5$；比较 $x^2$ 系数得 $A+B=0$，故 $B=-3$。所以

$$
\frac{2x+3}{x(x-1)^2}
=
\frac3x-\frac3{x-1}+\frac5{(x-1)^2}.
$$

## 7.10 同类巩固

**题目。** 证明 $1/30$ 可写成整数项加分母分别为 $2,3,5$ 的三个分式，并给出一种写法。

**答案。** 解 $15a+10b+6c\equiv1\pmod{30}$。取 $a=b=c=1$ 得 31，故

$$
\frac1{30}=-1+\frac12+\frac13+\frac15.
$$

---

# 8. V.4.7 环的局部化：从等价关系到泛性质

## 8.1 题意重述

设 $S$ 是交换环 $R$ 的乘法闭子集：$1\in S$，且 $s,t\in S\Rightarrow st\in S$。在 $R\times S$ 上定义

$$
(a,s)\sim(a',s')
\iff
\exists u\in S,\quad u(s'a-sa')=0.
$$

证明它是等价关系；在等价类上定义分数加法、乘法，得到环 $S^{-1}R$；证明自然映射 $R\to S^{-1}R$ 的泛性质；讨论整环性与零环条件。

## 8.2 为什么要多出一个 $u$

若 $R$ 是整环且 $0\notin S$，可消去非零因子，所以分数相等就是

$$
s'a=sa'.
$$

有零因子时不能消去。条件

$$
\exists u\in S,\quad u(s'a-sa')=0
$$

表示“把允许成为单位的某个元素再乘上去后，两边确实相等”。这正好是最弱且足以保证运算良定义的关系。

## 8.3 证明 $\sim$ 是等价关系

**自反性。** 取 $u=1$：$1(sa-sa)=0$。

**对称性。** 若 $u(s'a-sa')=0$，则 $u(sa'-s'a)=0$。

**传递性。** 假设

$$
u(s'a-sa')=0,
\qquad
v(s''a'-s'a'')=0.
$$

计算

$$
s'(s''a-sa'')=s''(s'a-sa')+s(s''a'-s'a'').
$$

两边乘 $uv$，右边两项分别为零，所以

$$
uvs'(s''a-sa'')=0.
$$

而 $uvs'\in S$，故 $(a,s)\sim(a'',s'')$。

## 8.4 运算的定义与良定义

记 $(a,s)$ 的等价类为 $a/s$。定义

$$
\frac as+\frac bt=\frac{at+bs}{st},
\qquad
\frac as\cdot\frac bt=\frac{ab}{st}.
$$

设 $a/s=a'/s'$、$b/t=b'/t'$，即存在 $u,v\in S$ 使

$$
u(s'a-sa')=0,
\qquad
v(t'b-tb')=0.
$$

对加法，两个候选结果的交叉差为

$$
(s't')(at+bs)-(st)(a't'+b's')
=tt'(s'a-sa')+ss'(t'b-tb').
$$

乘 $uv$ 后为零，所以加法良定义。

对乘法，利用

$$
s't'ab-sta'b'=t'b(s'a-sa')+sa'(t'b-tb').
$$

乘 $uv$ 后也为零，所以乘法良定义。环公理在代表元层面归结为 $R$ 中的环公理；零元、单位元分别是 $0/1,1/1$。

## 8.5 自然映射与 $S$ 中元素的可逆性

定义

$$
\iota:R\to S^{-1}R,
\qquad a\mapsto a/1.
$$

对任意 $s\in S$，

$$
\frac s1\cdot\frac1s=\frac11,
$$

所以 $\iota(s)$ 可逆。自然映射未必单射；事实上

$$
\frac a1=0
\iff
\exists s\in S,\quad sa=0.
$$

## 8.6 泛性质

设 $f:R\to A$ 是环同态，并且每个 $f(s)$（$s\in S$）都在 $A$ 中可逆。定义

$$
\widetilde f:S^{-1}R\to A,
\qquad
\widetilde f(a/s)=f(a)f(s)^{-1}.
$$

若 $a/s=a'/s'$，存在 $u\in S$ 使 $u(s'a-sa')=0$。施加 $f$ 并消去可逆的 $f(u)$，得到

$$
f(s')f(a)=f(s)f(a').
$$

再乘逆元即得 $f(a)f(s)^{-1}=f(a')f(s')^{-1}$，所以良定义。唯一性来自

$$
\frac as=\frac a1\left(\frac s1\right)^{-1}.
$$

## 8.7 整环性与零环边界

若 $R$ 是整环且 **$0\notin S$**，则 $S^{-1}R$ 是整环。若 $(a/s)(b/t)=0$，则存在 $u\in S$ 使 $uab=0$；由于 $u\ne0$ 且 $R$ 是整环，$a=0$ 或 $b=0$。

条件 $0\notin S$ 不能省。更精确地，

$$
S^{-1}R\text{ 是零环}
\iff 0\in S.
$$

若 $0\in S$，可用见证元 $u=0$ 证明所有分数相等；反之若 $1/1=0/1$，则存在 $u\in S$ 且 $u=0$。

## 8.8 易错点

1. 传递性见证可取 $uvs'$，不是简单的 $uv$。
2. 整环结论必须加 $0\notin S$。
3. 自然映射的核是被某个 $s\in S$ 消掉的元素。

## 8.9 变式 A（本质不同：$\mathbb Z$ 在素数处局部化）

**题目。** 令 $S=\mathbb Z\setminus(p)$，描述 $S^{-1}\mathbb Z$。

**解答。**

$$
\mathbb Z_{(p)}=\left\{\frac ab\in\mathbb Q:p\nmid b\right\}.
$$

## 8.10 变式 B（本质不同：局部化杀死零因子）

**题目。** 取 $R=\mathbb Z/6\mathbb Z$，$S=\{1,3,3^2,\ldots\}$。求 $S^{-1}R$。

**解答。** 局部化后 $3$ 可逆，而 $3\cdot2=0$，故 $2=0$；于是只剩模 2 信息，

$$
S^{-1}R\cong\mathbb Z/2\mathbb Z.
$$

## 8.11 同类巩固

**题目。** 令 $S=\{2^n:n\ge0\}\subset\mathbb Z$。描述 $S^{-1}\mathbb Z$。

**答案。** $\mathbb Z[1/2]=\{a/2^n:a\in\mathbb Z,n\ge0\}$。

---

# 9. V.4.8 模的局部化

## 9.1 题意重述

设 $M$ 是 $R$-模，$S\subseteq R$ 为乘法闭集。在 $M\times S$ 上定义

$$
(m,s)\sim(m',s')
\iff
\exists t\in S,\quad t(s'm-sm')=0.
$$

证明这是等价关系，并在等价类集合 $S^{-1}M$ 上定义 $S^{-1}R$-模结构。

## 9.2 等价关系

自反性与对称性和环的局部化相同。传递性用

$$
s'(s''m-sm'')=s''(s'm-sm')+s(s''m'-s'm'').
$$

若前两对分别有见证 $u,v\in S$，则 $uvs'$ 消掉右边两项。

## 9.3 加法与标量乘法

记等价类为 $m/s$。定义

$$
\frac ms+\frac nt=\frac{tm+sn}{st},
\qquad
\frac au\cdot\frac ms=\frac{am}{us}.
$$

良定义的证明与 4.7 平行：用代表元关系的见证相乘，再把候选结果之差写成两类已知为零的表达式之和。模公理在代表元上验证。

自然映射

$$
\eta_M:M\to S^{-1}M,
\qquad m\mapsto m/1
$$

是 $R$-线性的。

## 9.4 零判别

$$
\frac ms=0
\iff
\exists t\in S,\quad tm=0.
$$

右边与分母 $s$ 无关：只要某个允许被倒置的元素消掉 $m$，它在局部化后就消失。

## 9.5 易错点

1. $m/s$ 不是环元素，不能任意相乘；只有 $S^{-1}R$ 的分数可作用在它上面。
2. $m/1=0$ 不等价于 $m=0$，除非 $S$ 中元素在 $M$ 上都不是零因子。

## 9.6 自检

取 $M=\mathbb Z/6\mathbb Z$，$S=\{2^n\}$。元素 $3$ 满足 $2\cdot3=0$，所以 $3/1=0$ 于 $S^{-1}M$。

## 9.7 变式 A（本质不同：局部化杀死挠元）

**题目。** 令 $R=\mathbb Z$、$S=\mathbb Z\setminus\{0\}$。证明对任意有限阿贝尔群 $M$，$S^{-1}M=0$。

**解答。** 对每个 $m\in M$，存在正整数 $n$ 使 $nm=0$；又 $n\in S$，故 $m/1=0$。

## 9.8 变式 B（本质不同：自由模局部化）

**题目。** 证明

$$
S^{-1}(R^{\oplus n})\cong(S^{-1}R)^{\oplus n}.
$$

**解答。** 映射 $((r_1,\ldots,r_n)/s)\mapsto(r_1/s,\ldots,r_n/s)$。逆映射把各坐标通到共同分母；逐坐标检查互逆和线性。

## 9.9 同类巩固

**题目。** 取 $M=\mathbb Z/12\mathbb Z$，$S=\{3^n\}$。判断 $4/1$ 与 $3/1$ 是否为零。

**答案。** $3\cdot4=0$，所以 $4/1=0$；而任意 $3^n\cdot3$ 都不为零模 12，故 $3/1\ne0$。

---

# 10. V.4.9 理想的延拓、收缩与饱和

## 10.1 题意重述

设 $S$ 是 $R$ 的乘法闭集，$\iota:R\to S^{-1}R$。对理想定义延拓 $I^e=S^{-1}I$、收缩 $J^c=\iota^{-1}(J)$。证明

$$
(J^c)^e=J,
\qquad
(I^e)^c=\{a\in R:\exists s\in S,\ sa\in I\},
$$

并说明延拓后收缩可能严格变大。

## 10.2 延拓是真理想

$S^{-1}I$ 对加法和乘以任意局部化元素封闭。若 $1/1=a/s$ 且 $a\in I$，则存在 $u\in S$ 使 $u(s-a)=0$，于是 $us=ua\in I$，但 $us\in S$，与 $I\cap S=\varnothing$ 矛盾。

## 10.3 收缩与 $S$ 不交

理想的逆像仍是理想。若 $s\in J^c\cap S$，则 $s/1\in J$，但它可逆，故 $1\in J$，矛盾。

## 10.4 证明 $(J^c)^e=J$

若 $a/s\in(J^c)^e$，则 $a/1\in J$，故 $a/s=(1/s)(a/1)\in J$。反之，若 $a/s\in J$，乘以单位 $s/1$ 得 $a/1\in J$，所以 $a\in J^c$。

## 10.5 证明收缩是 $S$-饱和

$$
a\in(I^e)^c
\iff a/1\in S^{-1}I.
$$

若 $a/1=b/s$ 且 $b\in I$，则某个 $u\in S$ 满足 $u(sa-b)=0$，所以 $(us)a=ub\in I$。反之若 $ta\in I$，则 $a/1=(ta)/t\in S^{-1}I$。因此

$$
(I^e)^c=I:S^\infty.
$$

## 10.6 反例

取 $R=\mathbb C[x,y]$、$S=\{x^n\}$、$I=(xy)$。因为 $xy\in I$，故 $y\in(I^e)^c$，但 $y\notin I$。实际上 $(I^e)^c=(y)$。

## 10.7 易错点

延拓后再收缩得到的是饱和，不一定是原理想；而 $(J^c)^e=J$ 对局部化中的理想总成立。

## 10.8 变式 A（本质不同：高次饱和）

在 $k[x,y]$ 中，$S=\{x^n\}$，有

$$
((x^3y^2)^e)^c=(y^2).
$$

## 10.9 变式 B（本质不同：饱和判别）

$I=(I^e)^c$ 当且仅当 $sa\in I,s\in S\Rightarrow a\in I$。

## 10.10 同类巩固

在 $\mathbb Z$ 中令 $S=\{2^n\}$、$I=(12)$，则 $(I^e)^c=(3)$。

---

# 11. V.4.10 局部化后的素理想与原环素理想的对应

## 11.1 题意重述

证明 $S^{-1}R$ 的素理想与 $R$ 中不交于 $S$ 的素理想一一对应，映射分别为延拓和收缩。

## 11.2 从原环到局部化

若 $\mathfrak p\cap S=\varnothing$ 且 $\mathfrak p$ 素，$S^{-1}\mathfrak p$ 为真理想。若 $ab/(st)\in S^{-1}\mathfrak p$，则某个 $u\in S$ 满足 $uab\in\mathfrak p$；因 $u\notin\mathfrak p$，故 $a\in\mathfrak p$ 或 $b\in\mathfrak p$。

## 11.3 从局部化到原环

若 $\mathfrak q$ 是 $S^{-1}R$ 的素理想，则

$$
\mathfrak q^c=\{a\in R:a/1\in\mathfrak q\}
$$

是素理想，且与 $S$ 不交。

## 11.4 两个过程互逆

4.9 已证明 $(\mathfrak q^c)^e=\mathfrak q$。对 $\mathfrak p\cap S=\varnothing$，若 $sa\in\mathfrak p$ 且 $s\in S$，素性迫使 $a\in\mathfrak p$，故 $(\mathfrak p^e)^c=\mathfrak p$。

## 11.5 易错点

只有与 $S$ 不交的素理想才能延拓为真素理想；相交时延拓为整个局部化环。

## 11.6 自检

对 $R=\mathbb Z$、$S=\{2^n\}$，保留下来的素理想是 $(0)$ 与所有奇素数理想 $(p)$。

## 11.7 变式 A（本质不同：几何开集）

在 $k[x,y]$ 中倒置 $x$，保留的素理想恰是不含 $x$ 的素理想；几何上是主开集 $D(x)$。

## 11.8 变式 B（本质不同：全分式域）

若 $R$ 是整环且 $S=R\setminus\{0\}$，与 $S$ 不交的素理想只有 $(0)$，故分式域只有零素理想。

## 11.9 同类巩固

$\mathbb Z_{(5)}$ 的素理想只有 $(0)$ 与 $5\mathbb Z_{(5)}$。

---
