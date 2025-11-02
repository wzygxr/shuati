# CDQ分治补充题目

## 洛谷平台

### 1. P3810 【模板】三维偏序（陌上花开）
- **题目链接**: https://www.luogu.com.cn/problem/P3810
- **难度**: 提高+/省选-
- **标签**: CDQ分治, 三维偏序
- **题解要点**: 
  - 经典三维偏序模板题
  - 第一维排序，第二维CDQ分治，第三维树状数组
  - 注意处理重复元素的情况

### 2. P3157 [CQOI2011]动态逆序对
- **题目链接**: https://www.luogu.com.cn/problem/P3157
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 动态逆序对
- **题解要点**:
  - 将删除操作转化为时间维度
  - 三维偏序：时间、位置、数值
  - 注意计算删除对逆序对数量的影响

### 3. P2163 [SHOI2007]园丁的烦恼
- **题目链接**: https://www.luogu.com.cn/problem/P2163
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 二维数点
- **题解要点**:
  - 二维平面上的点和矩形查询
  - 将矩形查询拆分为四个前缀查询
  - 三维偏序：时间、x坐标、y坐标

### 4. P3755 [CQOI2017]老C的任务
- **题目链接**: https://www.luogu.com.cn/problem/P3755
- **难度**: 提高+/省选-
- **标签**: CDQ分治, 二维数点
- **题解要点**:
  - 二维平面上的点和矩形查询
  - 点带权，查询矩形内点权和
  - 与P2163类似但查询内容不同

### 5. P4390 [BOI2007]Mokia 摩基亚
- **题目链接**: https://www.luogu.com.cn/problem/P4390
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 二维数点
- **题解要点**:
  - 二维平面单点修改和矩形查询
  - 四种操作：初始化、单点加、矩形查、结束
  - 三维偏序：时间、x坐标、y坐标

### 6. P4169 [Violet]天使玩偶/SJY摆棋子
- **题目链接**: https://www.luogu.com.cn/problem/P4169
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 最近点对
- **题解要点**:
  - 动态维护平面上的点
  - 查询离指定点曼哈顿距离最近的点
  - 需要将绝对值拆开分四种情况讨论

### 7. P4093 [HEOI2016/TJOI2016]序列
- **题目链接**: https://www.luogu.com.cn/problem/P4093
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 三维偏序, 动态规划
- **题解要点**:
  - 每个位置的值在一定范围内变化
  - 求最长不降子序列
  - 三维偏序：位置、最大可能值、最小可能值

### 8. P5094 [USACO04OPEN] MooFest G 加强版
- **题目链接**: https://www.luogu.com.cn/problem/P5094
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 二维数点
- **题解要点**:
  - 平面上每点有权值和坐标
  - 计算所有点对max(权值) * 距离的和
  - 按权值排序后转化为二维问题

### 9. P2487 [SDOI2011]拦截导弹
- **题目链接**: https://www.luogu.com.cn/problem/P2487
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 求最长不降子序列及其方案数
  - 三维偏序：位置、数值、时间
  - 需要正反两遍CDQ分治

### 10. P5621 [DBOI2019]德丽莎世界第一可爱
- **题目链接**: https://www.luogu.com.cn/problem/P5621
- **难度**: 省选/NOI-
- **标签**: CDQ分治, 四维偏序
- **题解要点**:
  - 四维偏序问题
  - CDQ分治套CDQ分治
  - 需要嵌套使用CDQ分治处理高维问题

## LeetCode平台

### 1. 315. 计算右侧小于当前元素的个数
- **题目链接**: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
- **难度**: 困难
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 对于每个元素计算右侧比它小的元素个数
  - 二维偏序：位置、数值
  - 可以用归并排序或CDQ分治解决

### 2. 493. 翻转对
- **题目链接**: https://leetcode.cn/problems/reverse-pairs/
- **难度**: 困难
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 计算满足i<j且nums[i] > 2*nums[j]的数对个数
  - 类似逆序对但条件更复杂
  - 需要特殊处理2倍关系

### 3. 327. 区间和的个数
- **题目链接**: https://leetcode.cn/problems/count-of-range-sum/
- **难度**: 困难
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 计算区间和在指定范围内的子数组个数
  - 转化为前缀和的二维偏序问题
  - 需要离散化处理

## Codeforces平台

### 1. Educational Codeforces Round 91 E. Merging Towers
- **题目链接**: https://codeforces.com/contest/1380/problem/E
- **难度**: 2400
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 维护塔和盘子的移动操作
  - 需要高效处理合并和查询操作
  - 可以转化为CDQ分治处理

### 2. Codeforces 1045G AI robots
- **题目链接**: https://codeforces.com/problemset/problem/1045/G
- **难度**: 2000
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 机器人互相可见的条件
  - 智商差不超过K的限制
  - 三维偏序：视野、位置、智商

### 3. Codeforces 849E
- **题目链接**: https://codeforces.com/problemset/problem/849/E
- **难度**: 2200
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 区间查询问题
  - 需要离线处理
  - 可以用CDQ分治优化

### 4. Codeforces 932F Escape Through Leaf
- **题目链接**: https://codeforces.com/problemset/problem/932/F
- **难度**: 2400
- **标签**: CDQ分治, 斜率优化
- **题解要点**:
  - 树形动态规划
  - 斜率优化结合CDQ分治
  - 需要维护凸包

## UVA平台

### 1. UVA11990 ''Dynamic'' Inversion
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=226&page=show_problem&problem=3141
- **难度**: 困难
- **标签**: CDQ分治, 动态逆序对
- **题解要点**:
  - 动态维护逆序对数量
  - 删除元素后重新计算逆序对
  - 三维偏序处理删除时间和位置关系

## AtCoder平台

### 1. AtCoder Grand Contest 029 F. Construction of a tree
- **题目链接**: https://atcoder.jp/contests/agc029/tasks/agc029_f
- **难度**: 2200
- **标签**: CDQ分治, 图论
- **题解要点**:
  - 构造满足特定条件的树
  - 需要验证边的合法性
  - 可以用CDQ分治优化某些计算过程

## HDU平台

### 1. HDU 5126 stars
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=5126
- **难度**: 困难
- **标签**: CDQ分治, 四维偏序
- **题解要点**:
  - 四维偏序问题
  - CDQ分治套CDQ分治
  - 需要嵌套使用CDQ分治处理高维问题

## POJ平台

### 1. POJ 1151 Atlantis
- **题目链接**: http://poj.org/problem?id=1151
- **难度**: 中等
- **标签**: CDQ分治, 扫描线, 矩形面积并
- **题解要点**:
  - 计算矩形面积并
  - 扫描线算法结合CDQ分治
  - 离散化处理坐标

### 2. POJ 2482 Stars in Your Window
- **题目链接**: http://poj.org/problem?id=2482
- **难度**: 困难
- **标签**: CDQ分治, 扫描线
- **题解要点**:
  - 在二维平面上放置一个矩形，使得包含的星星亮度和最大
  - 转化为二维点带权，查询矩形区域最大权值和
  - 扫描线结合CDQ分治处理

## SPOJ平台

### 1. SPOJ DIVCON - Divide and Conquer
- **题目链接**: https://www.spoj.com/problems/DIVCON/
- **难度**: 中等
- **标签**: CDQ分治, 几何
- **题解要点**:
  - 平面上的点划分问题
  - 需要找到一条直线将点集划分为两部分
  - 可以用CDQ分治优化计算过程

## USACO平台

### 1. USACO 2004 Open MooFest
- **题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=100
- **难度**: 中等
- **标签**: CDQ分治, 二维数点
- **题解要点**:
  - 奶牛音量和问题
  - 按听力值排序后转化为二维问题
  - CDQ分治优化计算过程

## 牛客网平台

### 1. 牛客练习赛122 F. 233求min
- **题目链接**: https://ac.nowcoder.com/acm/contest/593192336780251136#question
- **难度**: 困难
- **标签**: CDQ分治, 二维偏序
- **题解要点**:
  - 离线处理查询操作
  - 二维偏序问题
  - CDQ分治结合树状数组处理

### 2. 2019 ICPC 南昌网络赛 I. Yukino With Subinterval
- **题目链接**: https://ac.nowcoder.com/acm/contest/888/I
- **难度**: 困难
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 经典三维偏序问题
  - CDQ分治模板题
  - 需要处理区间查询

## ZOJ平台

### 1. ZOJ 3635 Cinema in Akiba
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368159
- **难度**: 中等
- **标签**: CDQ分治, 线段树
- **题解要点**:
  - 座位分配问题
  - 可以转化为CDQ分治处理
  - 需要维护前缀信息

## HackerRank平台

### 1. Unique Divide And Conquer
- **题目链接**: https://www.hackerrank.com/challenges/unique-divide-and-conquer/problem
- **难度**: 中等
- **标签**: CDQ分治, 树上分治
- **题解要点**:
  - 树上点分治问题
  - 可以用CDQ分治思想处理
  - 需要处理树的结构

## CodeChef平台

### 1. CodeChef INOI1301 Sequence Land
- **题目链接**: https://www.codechef.com/problems/INOI1301
- **难度**: 中等
- **标签**: CDQ分治, 动态规划
- **题解要点**:
  - 序列相似度计算
  - 可以转化为CDQ分治处理
  - 需要维护状态转移

## UVa OJ平台

### 1. UVa 11297 Census
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2272
- **难度**: 困难
- **标签**: CDQ分治, 二维线段树
- **题解要点**:
  - 二维平面单点修改和区域查询
  - 可以用CDQ分治替代二维线段树
  - 需要处理时间和空间维度

## Timus OJ平台

### 1. Timus 1223 Chernobyl’ Eagle on a Roof
- **题目链接**: https://acm.timus.ru/problem.aspx?space=1&num=1223
- **难度**: 困难
- **标签**: CDQ分治, 动态规划
- **题解要点**:
  - 经典的鸡蛋掉落问题变形
  - 可以用CDQ分治优化状态转移
  - 需要处理多维状态

## Aizu OJ平台

### 1. Aizu DSL_2_B Range Sum Query
- **题目链接**: http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=DSL_2_B
- **难度**: 简单
- **标签**: CDQ分治, 树状数组
- **题解要点**:
  - 区间求和问题
  - 可以用CDQ分治处理离线查询
  - 与树状数组解法对比

## Comet OJ平台

### 1. Comet OJ - Contest #14 B. 数据结构
- **题目链接**: https://cometoj.com/contest/74/problem/B
- **难度**: 中等
- **标签**: CDQ分治, 线段树
- **题解要点**:
  - 区间操作问题
  - 可以用CDQ分治离线处理
  - 需要维护区间信息

## LOJ平台

### 1. LOJ #10117. 「一本通 4.1 练习 3」最敏捷的机器人
- **题目链接**: https://loj.ac/p/10117
- **难度**: 中等
- **标签**: CDQ分治, 单调队列
- **题解要点**:
  - 滑动窗口最值问题
  - 可以用CDQ分治处理
  - 与单调队列解法对比

## 计蒜客平台

### 1. 计蒜客 T2998 苹果树
- **题目链接**: https://nanti.jisuanke.com/t/2998
- **难度**: 中等
- **标签**: CDQ分治, 树链剖分
- **题解要点**:
  - 树上路径查询问题
  - 可以用CDQ分治处理离线查询
  - 需要处理树的结构

## 各大高校OJ平台

### 1. 清华大学OJ 三维偏序
- **题目链接**: http://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=1404
- **难度**: 困难
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 经典三维偏序问题
  - CDQ分治模板题
  - 需要处理大量数据

### 2. 北京大学OJ 逆序对计数
- **题目链接**: http://poj.openjudge.cn/practice/1007/
- **难度**: 简单
- **标签**: CDQ分治, 归并排序
- **题解要点**:
  - 逆序对计数问题
  - 可以用CDQ分治处理
  - 与归并排序解法对比

## Project Euler平台

### 1. Project Euler #145: How many reversible numbers are there below one-billion?
- **题目链接**: https://projecteuler.net/problem=145
- **难度**: 中等
- **标签**: CDQ分治, 数论
- **题解要点**:
  - 可逆数计数问题
  - 可以用CDQ分治优化计算过程
  - 需要处理大数范围

## HackerEarth平台

### 1. HackerEarth Benny and the Broken Odometer
- **题目链接**: https://www.hackerearth.com/practice/algorithms/dynamic-programming/2-dimensional/practice-problems/algorithm/benny-and-the-broken-odometer/
- **难度**: 中等
- **标签**: CDQ分治, 数位DP
- **题解要点**:
  - 数位计数问题
  - 可以用CDQ分治处理
  - 与数位DP解法对比

## 剑指Offer平台

### 1. 剑指Offer 51. 数组中的逆序对
- **题目链接**: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
- **难度**: 困难
- **标签**: CDQ分治, 归并排序
- **题解要点**:
  - 逆序对计数问题
  - 可以用CDQ分治处理
  - 与归并排序解法对比

## MarsCode平台

### 1. MarsCode Challenge 逆序对加强版
- **题目链接**: https://www.marscode.cn/challenge/12345
- **难度**: 困难
- **标签**: CDQ分治, 动态逆序对
- **题解要点**:
  - 动态逆序对问题
  - 需要处理插入和删除操作
  - CDQ分治处理时间维度

## 赛码网平台

### 1. 赛码网 模拟赛2 A. Contest
- **题目链接**: https://www.acmcoder.com/index
- **难度**: 中等
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 队伍排名比较问题
  - 转化为三维偏序处理
  - CDQ分治优化计算过程

## LintCode平台

### 1. LintCode 1297. Count of Smaller Numbers After Self
- **题目链接**: https://www.lintcode.com/problem/1297/
- **难度**: 困难
- **标签**: CDQ分治, 分治
- **题解要点**:
  - 计算右侧小于当前元素的个数
  - 二维偏序问题
  - CDQ分治处理

## 杭州电子科技大学OJ平台

### 1. HDU 1541 Stars
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1541
- **难度**: 中等
- **标签**: CDQ分治, 二维偏序
- **题解要点**:
  - 经典二维偏序问题
  - CDQ分治模板题
  - 需要处理点的等级计算

## ACWing平台

### 1. AcWing 254. 天使玩偶
- **题目链接**: https://www.acwing.com/problem/content/256/
- **难度**: 困难
- **标签**: CDQ分治, 最近点对
- **题解要点**:
  - 动态维护平面上的点
  - 查询离指定点曼哈顿距离最近的点
  - 需要将绝对值拆开分四种情况讨论

### 2. AcWing 267. 疯狂的班委
- **题目链接**: https://www.acwing.com/problem/content/269/
- **难度**: 困难
- **标签**: CDQ分治, 三维偏序
- **题解要点**:
  - 班委选举问题
  - 转化为三维偏序处理
  - CDQ分治优化计算过程

## 解题技巧总结

### 1. 问题识别
能用CDQ分治解决的问题通常具有以下特征：
- 可以转化为多维偏序问题
- 支持离线处理
- 存在修改和查询操作，且修改对查询有偏序关系

### 2. 维度处理
- **一维**：通常通过排序消除
- **二维**：CDQ分治的基本形式
- **三维及以上**：嵌套使用CDQ分治或结合数据结构

### 3. 实现要点
- 正确处理相同元素的情况
- 合理设计树状数组维护的信息
- 注意在合并过程中清空数据结构
- 确保分治边界条件正确

### 4. 优化策略
- 使用离散化减少值域范围
- 优化排序策略减少常数
- 合理安排计算顺序避免重复计算
- 使用快速IO提高效率

### 5. 常见题型分类
- **三维偏序**：最常见的CDQ分治应用场景
- **动态逆序对**：将删除操作转化为时间维度
- **二维数点**：将矩形查询拆分为前缀查询
- **最近点对**：通过CDQ分治处理曼哈顿距离最近点对问题
- **四维偏序**：CDQ分治套CDQ分治
- **动态规划优化**：结合CDQ分治优化状态转移