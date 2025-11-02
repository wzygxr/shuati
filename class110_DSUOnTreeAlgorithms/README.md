# DSU on Tree (树上启发式合并) 算法专题

## 算法简介

DSU on Tree（树上启发式合并）是一种在树上进行信息统计的高效算法。它通过重链剖分的思想，将轻重儿子的信息合并过程进行优化，使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)。

## 算法核心思想

1. **重链剖分**：对树进行重链剖分，区分重儿子和轻儿子
2. **启发式合并**：先处理轻儿子的信息，然后清除；再处理重儿子的信息并保留；最后重新计算轻儿子的贡献
3. **时间复杂度优化**：通过这种方式，保证每个节点最多被访问O(log n)次

## 适用场景

- 树上信息统计问题
- 子树查询问题
- 需要统计子树中某些属性的问题

## 题目列表

### 基础题目

1. [Lomsat gelral (Codeforces 600E)](https://codeforces.com/problemset/problem/600/E) - 统计子树中出现次数最多的颜色值之和
2. [树上数颜色 (洛谷 U41492)](https://www.luogu.com.cn/problem/U41492) - 统计子树中不同颜色的数量
3. [Tree and Queries (Codeforces 375D)](https://codeforces.com/problemset/problem/375/D) - 查询子树中出现次数至少为k的颜色数量

### 进阶题目

4. [Dominant Indices (Codeforces 1009F)](https://codeforces.com/problemset/problem/1009/F) - 查询子树中深度最深的节点数量
5. [Blood Cousins Return (Codeforces 246E)](https://codeforces.com/problemset/problem/246/E) - 查询k级儿子中不同名字的数量
6. [Tree Requests (Codeforces 570D)](https://codeforces.com/problemset/problem/570/D) - 查询子树中深度为h的节点是否能重排成回文
7. [Count on a Tree II (SPOJ COT2)](https://www.spoj.com/problems/COT2/) - 树上路径不同颜色数量查询
8. [Arpa's letter-marked tree and Mehrdad's Dokhtar-kosh paths (Codeforces 741D)](https://codeforces.com/problemset/problem/741/D) - 统计子树中满足条件的路径数量

### 更多平台题目

9. [Blood Cousins (Codeforces 208E)](https://codeforces.com/problemset/problem/208/E) - 查询k级堂兄弟节点数量
10. [Tree-String Problem (Codeforces 291E)](https://codeforces.com/problemset/problem/291/E) - 在树上字符串匹配问题
11. [Water Tree (Codeforces 343D)](https://codeforces.com/problemset/problem/343/D) - 树上区间操作问题
12. [Query on a tree again! (SPOJ QTREE3)](https://www.spoj.com/problems/QTREE3/) - 树上节点颜色操作和查询
13. [Tree Requests (HackerEarth)](https://www.hackerearth.com/practice/algorithms/graphs/graph-representation/practice-problems/algorithm/tree-stock-market-1-9872b56f/) - 树上股票市场问题
14. [Tree (计蒜客 42586)](https://vjudge.net/problem/%E8%AE%A1%E8%92%9C%E5%AE%A2-42586) - 计蒜客树上问题
15. [Tree (HDU 6765)](https://vjudge.net/problem/HDU-6765) - 杭电多校树上问题
16. [Race (洛谷 P4149/IOI2011)](https://www.luogu.com.cn/problem/P4149) - 找出树上距离恰好为k的点对且路径边数最少
17. [旗鼓相当的对手 (牛客竞赛)](https://ac.nowcoder.com/acm/contest/4853/E) - 树上路径距离为k的点对贡献问题
18. [观察员 (洛谷 P1600)](https://www.luogu.com.cn/problem/P1600) - 树上路径移动观察问题
19. [Tree Intersection (牛客)](https://www.nowcoder.com/practice/0b4e7f3f70ae49299010c0bf4c9085b1) - 树上路径与集合交问题
20. [Color the Tree (AtCoder Beginner Contest 133F)](https://atcoder.jp/contests/abc133/tasks/abc133_f) - 对每个节点求其子树中与该节点颜色相同的节点数量
21. [Query on a tree II (HackerEarth)](https://www.hackerearth.com/practice/algorithms/graphs/tree-algorithms/practice-problems/) - 多次查询子树中的第k大元素
22. [Count Distinct Colors in a Subtree (SPOJ QTREE7)](https://www.spoj.com/problems/QTREE7/) - 支持颜色修改和查询子树中不同颜色的数量
23. [统计子树信息 (牛客竞赛 NC19341)](https://ac.nowcoder.com/acm/problem/19341) - 求每个子树中权值的众数出现次数
24. [Subtree Sum Queries (HackerRank)](https://www.hackerrank.com/challenges/subtree-sum-queries) - 多次查询子树权值和
25. [Colorful Trees (CodeChef)](https://www.codechef.com/problems/COLORFULL) - 求所有子树中颜色的方差
26. [Path in a Tree (UVa 12333)](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3755) - 求树中路径上不同元素的个数
27. [Tree Query (杭电OJ 6092)](http://acm.hdu.edu.cn/showproblem.php?pid=6092) - 求子树中权值小于等于k的节点数目
28. [LCA with Subtree Queries (POJ 3417)](http://poj.org/problem?id=3417) - 树上的动态LCA查询和子树信息统计
29. [树上统计 (LOJ 2590)](https://loj.ac/p/2590) - 求每个子树中权值的最大值
30. [树上有多少条路径 (清华大学OJ THUOJ)](https://dsa.cs.tsinghua.edu.cn/oj/problem.shtml?id=409) - 求树中有多少条路径满足条件
31. [Subtree K-th Smallest (AizuOJ 2872)](https://onlinejudge.u-aizu.ac.jp/problems/2872) - 查询子树中第k小元素
32. [Tree Rotations (USACO 2015 January Gold)](http://www.usaco.org/index.php?page=viewproblem2&cpid=495) - 树旋转问题
33. [Subtree Maximum Product (TimusOJ 2144)](http://acm.timus.ru/problem.aspx?space=1&num=2144) - 求子树节点乘积的最大值
34. [Colorful Tree (Comet OJ C0294)](https://cometoj.com/contest/33/problem/C?problemId=1131) - 树上颜色统计问题
35. [树的统计 (acwing 358)](https://www.acwing.com/problem/content/360/) - 树上信息查询
36. [节点的权值 (浙江大学OJ ZOJ 3982)](https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364500)
37. [树中的最长路径 (北京航空航天大学OJ BUAOJ 4342)](https://acm.buaa.edu.cn/problem-detail.do?&prob_id=4342)
38. [统计子树中的叶子节点数 (哈尔滨工业大学OJ HIT OJ 3429)](http://acm.hit.edu.cn/problemset/3429)
39. [树上颜色匹配 (南京大学OJ NYOJ 421)](https://nyoj.top/problem/421)
40. [Tree and Sequence (北京大学OJ PKU OJ 3801)](http://poj.org/problem?id=3801)

### LeetCode相关题目（可用DSU on Tree解决）

20. [1339. 分裂二叉树的最大乘积](https://leetcode-cn.com/problems/maximum-product-of-splitted-binary-tree/) - 子树和相关问题
21. [543. 二叉树的直径](https://leetcode-cn.com/problems/diameter-of-binary-tree/) - 子树路径长度问题
22. [250. 统计同值子树](https://leetcode-cn.com/problems/count-univalue-subtrees/) - 子树属性统计问题
23. [1245. 树的直径](https://leetcode-cn.com/problems/tree-diameter/) - 树的最长路径问题
24. [834. 树中距离之和](https://leetcode-cn.com/problems/sum-of-distances-in-tree/) - 树上距离统计问题
25. [1443. 收集树上所有苹果的最少时间](https://leetcode-cn.com/problems/minimum-time-to-collect-all-apples-in-a-tree/) - 树上路径覆盖问题
26. [1617. 统计子树中城市之间最大距离](https://leetcode-cn.com/problems/count-subtrees-with-max-distance-between-cities/) - 子树最长路径问题
27. [1522. N 叉树的直径](https://leetcode-cn.com/problems/diameter-of-n-ary-tree/) - 多叉树路径长度问题
28. [2265. 统计值等于子树平均值的节点数](https://leetcode-cn.com/problems/count-nodes-equal-to-average-of-subtree/) - 子树统计问题
29. [1026. 节点与其祖先之间的最大差值](https://leetcode-cn.com/problems/maximum-difference-between-node-and-ancestor/) - 子树极值问题
30. [2049. 统计最高分的节点数目](https://leetcode-cn.com/problems/count-nodes-with-the-highest-score/) - 子树乘积统计问题
31. [1372. 二叉树中的最长交错路径](https://leetcode-cn.com/problems/longest-zigzag-path-in-a-binary-tree/) - 子树路径方向统计问题
32. [1609. 奇偶树](https://leetcode-cn.com/problems/even-odd-tree/) - 子树层序遍历属性判断
33. [1992. 找到所有的农场组](https://leetcode-cn.com/problems/find-all-groups-of-farmland/) - 二维网格中的连通子图（树）统计
34. [2359. 找到离给定两个节点最近的节点](https://leetcode-cn.com/problems/find-closest-node-to-given-two-nodes/) - 树中距离查询问题

### 本项目实现的题目

1. Lomsat gelral (Codeforces 600E) - 统计子树中出现次数最多的颜色值之和
    - Java实现: 已添加到 [Code09_TreeAndQueries1.java](Code09_TreeAndQueries1.java)
    - C++实现: 已添加到 [Code09_TreeAndQueries2.cpp](Code09_TreeAndQueries2.cpp)
    - Python实现: 已添加到 [Code09_TreeAndQueries3.py](Code09_TreeAndQueries3.py)

2. Tree and Queries (Codeforces 375D) - 查询子树中出现次数至少为k的颜色数量
    - Java实现: [Code09_TreeAndQueries1.java](Code09_TreeAndQueries1.java)
    - C++实现: [Code09_TreeAndQueries2.cpp](Code09_TreeAndQueries2.cpp)
    - Python实现: [Code09_TreeAndQueries3.py](Code09_TreeAndQueries3.py)

3. Dominant Indices (Codeforces 1009F) - 查询子树中深度最深的节点数量
    - Java实现: 已添加到 [Code09_TreeAndQueries1.java](Code09_TreeAndQueries1.java)
    - C++实现: 已添加到 [Code09_TreeAndQueries2.cpp](Code09_TreeAndQueries2.cpp)
    - Python实现: 已添加到 [Code09_TreeAndQueries3.py](Code09_TreeAndQueries3.py)

## 算法详解

### 时间复杂度分析

DSU on Tree算法的时间复杂度为O(n log n)，其中n为树中节点的数量。这个复杂度的来源是：
1. 每个节点在DFS过程中最多被访问O(log n)次
2. 通过重链剖分，保证了每个节点只会被其轻边祖先访问

### 空间复杂度分析

空间复杂度为O(n)，主要用于存储：
1. 树的邻接表表示
2. 重链剖分相关信息（父节点、子树大小、重儿子等）
3. 颜色计数数组
4. DFS递归栈空间

### 算法实现要点

1. **重链剖分**：通过一次DFS计算每个节点的子树大小，并确定重儿子
2. **启发式合并**：优先处理轻儿子，最后处理重儿子并保留其贡献
3. **信息维护**：根据具体问题维护相应的信息（颜色计数、深度信息等）
4. **答案统计**：在处理每个节点时统计其子树的答案

### 适用题型特征

1. 静态树上查询问题
2. 需要统计子树信息的问题
3. 可以离线处理的问题
4. 查询数量较多，直接暴力处理会超时的问题

### 常见变种

1. **颜色统计类**：统计子树中不同颜色数量或出现次数最多的颜色
2. **深度相关类**：统计子树中特定深度节点的信息
3. **路径相关类**：结合LCA处理树上路径问题
4. **字符串匹配类**：在树上进行字符串匹配操作

### 工程化考虑

1. **边界处理**：注意空树、单节点树等特殊情况
2. **内存优化**：合理使用全局数组，避免重复分配内存
3. **常数优化**：使用位运算、减少函数调用等优化常数
4. **可扩展性**：设计通用模板，便于适应不同类型的查询问题