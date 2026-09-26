# SOURCE AUDIT v027

## 1. 权威版次

Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016。

优先级固定为：作者第二次印刷正式勘误 > corrected second printing 完整题序 > Stacks Project / 正式大学讲义 > 独立公开题解与论坛讨论。

官方入口：

- 作者主页：`https://www.math.fsu.edu/~aluffi/`
- second-printing errata：`https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html`
- AMS 书目：`https://bookstore.ams.org/gsm-104`

作者页面说明，可由第 xv 页的 “Preface to the second printing” 判断 2016 第二次印刷。官方勘误在本轮核对时列 173 项，最后更新日期为 2025-12-24。

## 2. v027 冻结范围

- Chapter VII §3：Exercises 3.1–3.15，共 15 题；
- Chapter VII §4：Exercises 4.1–4.22，共 22 题；
- 合计 37 道正式原题；
- 下一题：Chapter VII §5 Exercise 5.1。

完整题序按整本 corrected second printing 冻结，不以约 150 页预览层作为全书边界。

## 3. 正式勘误敏感点

作者 second-printing errata 在本轮附近列有 p.426 middle、p.431 Example 4.6、p.431 Proof of Theorem 4.8、p.438 Exercises 4.4–4.5、p.439 Exercise 4.8 等位置。本稿据此重点核对：

1. 可作角、可作复数与逐次二次扩张的底域和坐标口径；
2. 分裂域到分裂环境的“存在嵌入”，不误写为唯一嵌入；
3. 固定环境域中的因子分裂子域唯一性，与抽象分裂域只在同构意义下唯一的区别；
4. 正规扩张判据中的嵌入延拓与逆自同构反包含；
5. 特征 p 中导数为零、Frobenius、可分次数与不可分次数；
6. 一般有限扩张的迹范数公式必须计入不可分次数。

## 4. 外部交叉核验

- Stacks Project：正规扩张、可分扩张、纯不可分扩张、迹与范数；
- `hooyuser/Solution-to-Algebra-Chapter-0`；
- `mactonya/algebra-chapter-0-solutions`；
- Archimedes / neusis marked-ruler 三等分的公开几何说明。

公开解答只用于定位题号、比较候选路线和发现风险点。正文未复制其文字；特别修正了代数元处整个 k(t) 评价、不可分与纯不可分混淆、迹范数漏乘不可分次数等问题。

## 5. 版权边界

原题仅作完成证明所需的中文数学重述。证明、解释、变式、巩固题、结构图和复算脚本均重新组织。
