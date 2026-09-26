# SOURCE AUDIT v026

## 1. 权威版次

本项目采用 Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016。

作者主页说明，可由第 xv 页的 “Preface to the second printing” 判断 2016 第二次印刷。作者维护的 second-printing errata 在本轮核对时共列 173 项，最后更新日期为 2025-12-24。

资料优先级：

1. 作者第二次印刷正式勘误；
2. 2016 corrected second printing 的完整正文与练习题序；
3. Stacks Project 与正式大学讲义；
4. 独立公开解答只用于题号、候选路线和风险点交叉核验。

## 2. v026 冻结范围

- Chapter VII §1：Exercises 1.1–1.30，30 道正式题；
- Chapter VII §2：Exercises 2.1–2.21，21 道正式题；
- 本轮合计：51 道；
- 下一题：Chapter VII §3 Exercise 3.1。

完整题序来自整本 corrected second printing，不采用网页预览层作为全书页数或练习分母。

## 3. 本轮勘误敏感点

作者勘误表在本轮范围中列有 p.385 bottom、p.386 second paragraph、p.387 top、p.388 second bullet point、p.389 second paragraph、p.393 bottom、p.398 Exercises 1.15–1.16、p.410 top、p.414 Exercise 2.6、p.417 bottom。

本稿特别落实以下边界：

1. **VII.1.3**：若 `α` 代数，整个 `k(t)` 上的评价通常根本不良定义；不能先写成域同态再声称它有非零核。
2. **VII.1.15–1.16**：迹、范数的幂次公式和扩大底域后最小多项式次数不增，均明确写出塔式次数和块对角基。
3. **VII.1.17**：区分 ring epimorphism 与集合满射。
4. **VII.2.6**：有限域单点指示多项式的补集方向逐点核对。
5. **VII.2.16–2.17**：先约分，再用 Nullstellensatz／局部化处理正则函数，避免把可消去分母当作真极点。
6. **VII.2.18–2.19**：抛物线与尖点虽有同构分式域，但坐标环和原点局部环不同。
7. **VII.2.20**：`K^n` 只与标准图 `U_i` 双射，单个仿射图不覆盖整个射影空间。

## 4. 二手资料的使用边界

交叉核验的公开项目包括 `hooyuser/Solution-to-Algebra-Chapter-0` 和 `mactonya/algebra-chapter-0-solutions`。这些项目在本范围中存在缺解、简写或边界错误候选。正文未复制其文字，而是重新推导；特别修正了有理函数评价、有限域指示函数、射影图册以及尖点局部环等问题。

## 5. 版权边界

本稿对原题采用数学必要条件下的中文重述，不逐句复刻受版权保护的题面。证明、解释、变式、巩固题、结构图和复算代码均重新组织。
