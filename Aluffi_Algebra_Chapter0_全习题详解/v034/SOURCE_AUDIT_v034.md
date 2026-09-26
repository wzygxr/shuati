# SOURCE AUDIT v034 — Chapter IX §§2--3

## 1. 版次与完整页树

本轮采用 Paolo Aluffi, *Algebra: Chapter 0*, GSM 104, 2016 corrected second printing。题面冲突时的优先级为：

> 作者官方 second-printing errata > 2016 corrected printing 完整题面 > 可逐行核对的大学讲义和标准参考 > 论坛或二手转述。

全书为 713 页；Chapter IX 位于书末后段，不能把网页约 150 页预览层当成整书终点。

## 2. 本轮正式题块

- Chapter IX §2 *Working in abelian categories*：Exercises 2.1--2.17，共 17 题，题块始于 printed p.589；
- Chapter IX §3 *Complexes and homology, again*：Exercises 3.1--3.15，共 15 题，题块位于 printed pp.602--604。

\[
17+15=32\text{ formal exercises}.
\]

## 3. 关键题源风险

- 四引理、五引理中的元素追逐必须用 §2.5--2.7 的广义元素合法化；
- Yoneda 嵌入只自动左正合，不能误写成对所有短正合列都正合；
- `Seq(A)` 的核、余核不能简单逐次计算，蛇引理连接态射必须参与修正；
- IX.3.8 要求在同一个短正合复形列的同一次数上同时观察左右两端失去正合；
- IX.3.9 的连接同态需要分别证明提升选择无关、代表元选择无关、加法性、三个位置的 exactness；
- IX.3.12 是 Exercise III.7.17 的同调版本；
- IX.3.13 中诱导态射为 `bar d : coker d -> ker d`，其核和余核都同构于 `ker d / im d`。

## 4. 交叉核验

交叉核验来源包括作者主页和 second-printing errata、corrected printing 的完整题面、Stacks Project 关于阿贝尔范畴和复形范畴的条目，以及 Weibel、Rotman 等同调代数标准资料。公开答案只用于题号 inventory 和错误雷达，正文、变式及复算均重新组织。
