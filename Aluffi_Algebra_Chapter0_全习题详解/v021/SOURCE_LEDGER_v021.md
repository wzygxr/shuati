# SOURCE LEDGER v021 — 题源、勘误与独立核验

日期：2026-09-24

## A 级：题面与作者勘误

1. Paolo Aluffi, *Algebra: Chapter 0*, AMS GSM 104，2016 corrected second printing：本轮 V.§4.1--4.25 的题号、上下文与章节结构基线。
2. 作者维护的 second-printing errata：本节直接涉及 4.6、4.7、4.13、4.25。

特别说明：官方勘误目录明确列出 p.280 Exercise 4.25；当前工具没有取得该图片红线的可读正文。因此本稿不冒充逐字引用官方修改，而是用一个立即可检验的公共因子反例证明“无非常数公因子”的限定在数学上不可缺，并在此基础上证明校正版和完整分类。

## B 级：标准理论交叉核验

- Stacks Project：局部化、素理想对应、局部环、Noether 性在局部化下保持、UFD 的多项式扩张；
- Isabelle Archive of Formal Proofs：Mason--Stothers theorem 及其多项式 Fermat 推论；
- 标准 Gauss 引理与 content 理论：用于 4.2--4.5、4.21--4.24；
- 素谱/nilradical 交刻画：用于 4.20。

## C 级：公开解答的使用边界

公开 GitHub 仓库 `mactonya/algebra-chapter-0-solutions` 的 Chapter V 只用于核对少数题号和发现过短证明的风险；§4 覆盖不完整，且其文字不作为本稿证明来源。主证明全部重新推导。

## 证据优先级

作者正式勘误 > 2016 corrected printing > 标准参考文献/形式化条目 > 独立重新推导 > 公开个人解答。

## 本轮关键纠错

- 4.7：整环局部化必须排除 `0 ∈ S`；
- 4.13：PID 在素理想处局部化的 DVR 型描述需区分零素理想；
- 4.16：局部主理想性反推全局时，Noether 性与高度 1 素理想不可省略；
- 4.20：一般交换环多项式单位的高次系数是幂零元，不能只比较最高次项；
- 4.25：未经本原化的字面命题有公共因子缩放反例；校正版证明中必须验证下降后三元组仍本原。
