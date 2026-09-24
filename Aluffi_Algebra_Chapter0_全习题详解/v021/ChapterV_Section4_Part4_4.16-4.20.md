# Paolo Aluffi《Algebra: Chapter 0》Chapter V §4 增量 v021｜Part 4: Exercises 4.16--4.20

> v021 分块阅读文件；继承 v020 的 500 道正式题高水位。本块完整覆盖 4.16--4.20。

# 17. V.4.16 只倒置一个素元的 Nagata 型判别

## 17.1 命题

设 $R$ 是 Noetherian 整环，$s\in R$ 是非零素元，$S=\{1,s,s^2,\ldots\}$。证明

$$
R\text{ 是 UFD}\iff R[1/s]\text{ 是 UFD}.
$$

正向由“UFD 的局部化仍为 UFD”立即得到。下面严格证明反向。

## 17.2 用高度 1 素理想判别

假设 $R[1/s]$ 是 UFD。Noetherian 整环是 UFD，当且仅当每个高度 1 素理想为主理想。因此固定高度 1 素理想 $\mathfrak p\subset R$。

### 情形一：$s\in\mathfrak p$

$(s)$ 是非零素理想，且

$$
(0)\subsetneq(s)\subseteq\mathfrak p.
$$

高度为 1 排除严格中间层，所以 $\mathfrak p=(s)$。

### 情形二：$s\notin\mathfrak p$

此时 $\mathfrak p$ 与 $S$ 不交，延拓 $\mathfrak p^e$ 是 $R[1/s]$ 的高度 1 素理想。若其下还有非零真素理想，收缩回 $R$ 会给出 $(0)\subsetneq\mathfrak q\subsetneq\mathfrak p$，矛盾。

UFD 中高度 1 素理想主，故把生成元分母吸收为单位后可写

$$
\mathfrak p^e=(a/1).
$$

收缩得到

$$
\mathfrak p=(a):S^\infty=\bigcup_{n\ge0}((a):s^n).
$$

## 17.3 冒号理想稳定并保持主性

链

$$
(a):s^0\subseteq(a):s\subseteq(a):s^2\subseteq\cdots
$$

因 Noether 性而稳定，所以 $\mathfrak p=(a):s^N$。

把 $a$ 中可提出的 $s$ 次数提到最大：

$$
a=s^kb,\qquad s\nmid b.
$$

最大次数存在；否则会产生严格升链 $(a)\subsetneq(a/s)\subsetneq(a/s^2)\subsetneq\cdots$，违反 Noether 性。

- 若 $N\le k$，则
  $$
  (a):s^N=(s^{k-N}b).
  $$
- 若 $N>k$，则
  $$
  (a):s^N=(b).
  $$
  因为 $s^Nx=s^kby$ 蕴含 $s^{N-k}x=by$；$s$ 是素元且 $s\nmid b$，可反复推出 $s^{N-k}\mid y$，从而 $x\in(b)$。

所以 $\mathfrak p$ 主。所有高度 1 素理想都主，故 $R$ 是 UFD。

## 17.4 易错点与自检

Noether 性使用两次：冒号理想链稳定；可从 $a$ 提出的 $s$ 次数有限。$s$ 必须是素元，不只是不可约元。例 $k[x,y]$ 倒置 $x$ 后与原环都为 UFD。

## 17.5 变式 A（有限多个素元）

若 $s_1,\ldots,s_r$ 都是素元且 $R[1/(s_1\cdots s_r)]$ 是 UFD，则逐个应用原题，得到 $R$ 是 UFD。

## 17.6 变式 B（冒号理想计算）

若 $a=s^5b$ 且 $s\nmid b$，则

$$
(a):s^2=(s^3b),\qquad (a):s^8=(b).
$$

## 17.7 同类巩固

一般地，若 $a=s^kb$、$s\nmid b$，则 $(a):S^\infty=(b)$。

---

# 18. V.4.17 域的特征与素子域

## 18.1 从 $\mathbb Z$ 到任意域

存在唯一含幺同态

$$
\eta:\mathbb Z\to F,\qquad n\mapsto n1_F.
$$

其核是 $(0)$ 或 $(p)$，其中 $p$ 为素数：若正特征合成，会在域中产生非零零因子。

## 18.2 特征 0

若 $\operatorname{char}F=0$，则 $\eta$ 单射，且每个非零整数像可逆。分式域泛性质给出唯一嵌入

$$
\mathbb Q\hookrightarrow F,
\qquad a/b\mapsto(a1_F)(b1_F)^{-1}.
$$

反之，若 $F$ 含 $\mathbb Q$ 的含幺拷贝，则非零整数不可能变为零，故特征为 0。

## 18.3 特征 $p$

若 $\operatorname{char}F=p$，第一同构定理给出

$$
\mathbb F_p=\mathbb Z/p\mathbb Z\hookrightarrow F.
$$

反之，含有 $\mathbb F_p$ 立即给出 $p1_F=0$，而 $1,\ldots,p-1$ 非零，所以特征恰为 $p$。

任何子域都含 $1_F$，从而含整数像及必要的逆元；因此上述 $\mathbb Q$ 或 $\mathbb F_p$ 包含在所有子域中，是唯一最小子域。

## 18.4 易错点、自检与变式

“含有”指含幺域嵌入。$\mathbb C$ 的素子域是 $\mathbb Q$；$\mathbb F_{p^n}$ 的素子域是 $\mathbb F_p$。

- 变式 A：有限域是有限维 $\mathbb F_p$-向量空间，故大小为 $p^n$。
- 变式 B：不存在含幺域同态 $\mathbb Q\to\mathbb F_p$，否则非零元 $p$ 落入核。
- 同类：$\mathbb Q(t)$、$\mathbb F_7(t)$ 的素子域分别是 $\mathbb Q$、$\mathbb F_7$。

---

# 19. V.4.18 整环多项式环中的单位

若 $R$ 是整环，则

$$
R[x]^\times=R^\times.
$$

常数单位显然仍可逆。反之若 $fg=1$，整环上非零多项式次数可加：

$$
0=\deg1=\deg(fg)=\deg f+\deg g.
$$

所以 $f,g$ 都是常数，且在 $R$ 中互逆。

整环条件不可省。在 $(\mathbb Z/4\mathbb Z)[x]$ 中

$$
(1+2x)^2=1,
$$

所以存在非常数单位。

- 变式 A：对变量数归纳，$R[x_1,\ldots,x_n]^\times=R^\times$。
- 变式 B：在 $(\mathbb Z/8\mathbb Z)[x]$ 中
  $$
  (1+2x)^{-1}=1-2x+4x^2,
  $$
  因为 $(2x)^3=0$。
- 同类：$\mathbb F_5[x,y]$ 的单位是非零常数 $1,2,3,4$。

---

# 20. V.4.19 幂零元加一必为单位

若 $a^n=0$，有限几何级数给出

$$
(1+a)(1-a+a^2-\cdots+(-a)^{n-1})=1-(-a)^n=1.
$$

故

$$
(1+a)^{-1}=1-a+a^2-\cdots+(-a)^{n-1}.
$$

它是有限恒等式，不涉及收敛；在非交换环中也成立，因为只出现同一个元素 $a$ 的幂。

- 变式 A：严格上三角 $N\in M_n(R)$ 满足 $N^n=0$，故
  $$
  (I+N)^{-1}=I-N+\cdots+(-1)^{n-1}N^{n-1}.
  $$
- 变式 B：若 $1-ab$ 可逆，则
  $$
  (1-ba)^{-1}=1+b(1-ab)^{-1}a.
  $$
  直接左右相乘验证。
- 同类：模 16 下 $4^2=0$，故 $(1+4)^{-1}=1-4\equiv13$。

---

# 21. V.4.20 一般交换环上多项式单位的完整刻画

## 21.1 命题

对交换环 $R$，

$$
f=a_0+a_1x+\cdots+a_dx^d\in R[x]^\times
$$

当且仅当 $a_0\in R^\times$ 且 $a_1,\ldots,a_d$ 全部幂零。

## 21.2 必要性

若 $fg=1$，比较常数项得 $a_0b_0=1$，故 $a_0$ 是单位。

再取任意素理想 $\mathfrak p$。$f$ 模 $\mathfrak p$ 后仍是 $(R/\mathfrak p)[x]$ 的单位；但 $R/\mathfrak p$ 是整环，由 4.18，它只能是常数。因此

$$
a_i\in\mathfrak p\quad(i\ge1)
$$

对每个素理想成立。所有素理想的交是 nilradical，所以每个 $a_i$ 幂零。

这里不能只“比较最高项”；有零因子时最高项可能消失。

## 21.3 充分性

假设 $a_0$ 可逆，$a_i$（$i\ge1$）幂零。写

$$
f=a_0(1+h),\qquad h=a_0^{-1}(a_1x+\cdots+a_dx^d).
$$

设 $(a_0^{-1}a_i)^{n_i}=0$，取

$$
N=1+\sum_{i=1}^d(n_i-1).
$$

展开 $h^N$。每个单项式含 $N$ 个来自有限集合的系数因子；抽屉原理保证某类至少出现 $n_i$ 次，因此每项为零，故 $h^N=0$。由 4.19，

$$
f^{-1}=a_0^{-1}(1-h+h^2-\cdots+(-h)^{N-1}).
$$

## 21.4 易错点与自检

有限多个幂零元才可用上述统一指数；常数项必须可逆，不只是非零。在 $\mathbb Z/8\mathbb Z$ 中，$1+2x+4x^2$ 是单位。

## 21.5 变式 A（显式求逆）

在 $k[\varepsilon]/(\varepsilon^2)$ 中，若 $2$ 可逆，

$$
(2+\varepsilon x)^{-1}=\frac12-\frac{\varepsilon}{4}x.
$$

## 21.6 变式 B（与形式幂级数对比）

在 $R[[x]]$ 中，只要求常数项可逆；逆元系数递归求出，不必在有限次数处停止。多项式环中高次系数需幂零，正是为了让逆级数截断。

## 21.7 同类巩固

$5+6x+3x^2\in(\mathbb Z/9\mathbb Z)[x]$ 是单位：5 可逆，且 $6^2=3^2=0\pmod9$。
