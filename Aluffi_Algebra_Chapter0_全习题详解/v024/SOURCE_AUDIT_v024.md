# SOURCE AUDIT v024

## 1. 权威版次

本项目采用：

> Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016.

题面、题序和修订的优先级为：

1. 作者的 corrected-second-printing 正式勘误；
2. corrected second printing 的完整正文与练习次序；
3. 作者或大学正式讲义、标准公开教材；
4. 独立公开解答与论坛，仅用于发现风险点和比较证明路线。

官方入口：

- 作者主页：`https://www.math.fsu.edu/~aluffi/`
- 第二次印刷勘误：`https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html`
- AMS 书目：`https://bookstore.ams.org/gsm-104`

作者主页说明第二次印刷可由第 xv 页的 *Preface to the second printing* 识别；勘误页最近更新时间为 2025-12-24。

## 2. v024 冻结范围

- VI.3.1–VI.3.20：20 个正式题位；
- VI.4.1–VI.4.17：17 个正式题位；
- 本轮合计：37；
- 下一连续入口：VI.5.1。

本轮按完整书序读取到 §4 末，不受网页或文件“150 页预览层”影响。

## 3. 本轮直接相关的正式勘误

作者勘误表在本范围明确列出：

- p.333，Proposition 3.7 的证明；
- p.341，中部；
- p.343，页底；
- p.347，Exercise 4.6。

正文据此特别处理：

1. Nakayama 行列式形式的矩阵关系和符号方向逐项检查；
2. Exercise VI.4.4 的正确条件是有限生成挠模满足 `Ann(M) ≠ 0`，不是等于零；
3. Exercise VI.4.6 的结论是 Noetherian 环上非零模的相关素理想集合非空；
4. Exercise VI.4.6 中用于极大湮灭理想论证的元素必须保持非零；
5. 三元 Koszul 复形的三个微分采用同一基序与符号体系，并直接验证相邻复合为零；
6. VI.4.17 的 Smith 不变因子在任意特征中均重新核对，没有默认 2、3 可逆。

## 4. 二手材料使用边界

交叉查看了 `hooyuser/Solution-to-Algebra-Chapter-0`、`mactonya/algebra-chapter-0-solutions` 等公开项目。这些项目在 Chapter VI 后半部分存在缺题、空解或简略推导，因此只用于题号和候选路线核对；正文证明全部重新组织。

补充背景参考包括：标准线性代数资料中的秩分解和 Smith 标准形；交换代数资料中的 Nakayama 引理、相关素理想、素过滤和 Koszul 复形；Grothendieck 群资料中的可加关系。凡与作者正式勘误冲突者，以正式勘误和重新推导为准。

## 5. 版权边界

原题采用完成数学任务所需的中文重述，不大段复刻原书。详细证明、初学者解释、变式、巩固题、图和复算代码均为本项目重新组织。
