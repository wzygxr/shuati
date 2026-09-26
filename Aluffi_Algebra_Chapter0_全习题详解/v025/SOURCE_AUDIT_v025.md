# SOURCE AUDIT v025

## 1. 权威版次

Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016。

资料优先级：

1. 作者的 corrected-second-printing 正式勘误；
2. corrected second printing 的完整正文与练习顺序；
3. 正式大学讲义、Stacks Project 与标准线性代数/模论资料；
4. 独立公开解答只用于发现风险点、核对题号和比较路线，不作为正确性权威。

官方入口：

- 作者主页：`https://www.math.fsu.edu/~aluffi/`
- 第二次印刷勘误：`https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html`
- AMS 书目：`https://bookstore.ams.org/gsm-104`

## 2. v025 冻结范围

- VI.5.1–VI.5.17：17 个正式题位；
- VI.6.1–VI.6.22：22 个正式题位；
- VI.7.1–VI.7.20：20 个正式题位；
- 总计 59；
- Chapter VI §§1–7 至此全闭合；
- 下一题：VII.1.1。

完整题序按整本 corrected second printing 核定，没有把约 150 页的网页预览层当作全书页数。

## 3. 本轮正式勘误敏感点

作者第二次印刷勘误在本轮范围列出：

- p.366, Lemma 6.14；
- p.367, bottom；
- p.370, top；
- p.371, Exercise 6.21。

本稿据完整修订版重新核对对应命题和题面，不继承早期印次的缺字或方向错误。

## 4. 重点正确性边界

1. “每个有限生成无挠模自由”若只给这一量词，不能无条件反推 PID；需要结合 Noetherian 条件或对适当模类的更强假设。本稿明确区分 PID、Bézout 域与 Noetherian 域。
2. 有限自由模端同态满射的判据是 `det(A)` 为单位；整环上单射的判据是 `det(A) ≠ 0`，二者不可混用。
3. 复内积中的自伴、Hermitian 与正交性证明始终保持同一共轭线性约定。
4. 扩域后相似不能仅凭“同特征多项式”下降；本稿通过不变因子/模结构处理。
5. Schur 分解、正规矩阵对角化和实对称谱定理的依赖次序分别写清，没有循环论证。

## 5. 外部交叉核验

公开项目 `hooyuser/Solution-to-Algebra-Chapter-0`、`mactonya/algebra-chapter-0-solutions`、`choco-bear/solutions-algebra-chapter-0` 等仅用于核对编号和发现常见错误；这些项目在本轮范围并不完整。模分类、Grothendieck 群、Cayley–Hamilton、Schur 与谱定理另与标准课程资料交叉检查，正文证明仍独立重建。

## 6. 版权边界

原题只保留完成数学任务所需的中文重述。证明、解释、反例、变式、巩固题、图和复算代码均重新组织，未大段复刻原书或第三方解答。
