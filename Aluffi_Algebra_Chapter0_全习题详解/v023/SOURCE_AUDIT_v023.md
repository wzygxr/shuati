# SOURCE AUDIT v023

## 1. 权威版次

Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016。

优先级：

1. 作者的 corrected-second-printing 正式勘误；
2. corrected second printing 的完整正文与练习顺序；
3. 正式大学讲义和标准公开教材；
4. 独立公开解答只用于发现风险点、对比路线，不作为正确性权威。

官方入口：

- 作者主页：`https://www.math.fsu.edu/~aluffi/`
- 第二次印刷勘误：`https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html`
- AMS 书目：`https://bookstore.ams.org/gsm-104`

## 2. v023 冻结范围

- VI.1.1–VI.1.20：20 个正式题位；
- VI.2.1–VI.2.19：19 个正式题位；
- 总计 39；
- 下一题：VI.3.1。

完整题序按整本 corrected second printing 核定，没有把网页或文件预览层当作全书页数。

## 3. 本轮正式勘误敏感点

作者勘误表在 Chapter VI 本范围列出 p.306、pp.309–310、p.310、p.314 Exercise 1.19、p.326 Exercise 2.10、p.328 与 p.329。

本稿特别落实：

1. VI.1.19 明确构造**非零**倍式；若允许零倍式，命题退化。
2. VI.2.10 行等价矩阵的行空间相等；列空间由可逆左乘自然同构，一般不逐点相等。
3. IBN 讨论明确排除零环。
4. 非交换端同态环的左/右模作用逐一定向，避免把预复合、后复合混用。
5. Smith 标准形证明写出欧几里得下降、整除右下块和递归三阶段，不以“显然终止”代替论证。

## 4. 外部交叉核验

- Stacks Project, Tag `0FJ7`：非零交换环上有限自由模的秩良定义；
- Columbia University 的 Lie Groups / Representation Theory 课程资源：`SU(2)`、`SO(3)` 与 Lie 代数背景；
- CRing Project：模与交换代数基础的公开交叉参考；
- `hooyuser/Solution-to-Algebra-Chapter-0` 与 `mactonya/algebra-chapter-0-solutions`：只比对题号和候选路线，并对其中缺解或左右模混淆处重新证明。

## 5. 版权边界

原题只保留完成数学任务所需的中文重述。证明、解释、变式、巩固题、图和复算代码均重新组织；未大段复刻原书或第三方解答。
