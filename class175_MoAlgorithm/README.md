# 莫队算法详解与应用

莫队算法（Mo's Algorithm）是由莫涛提出的一种离线算法，用于解决一类区间查询问题。它通过巧妙的排序和双指针技术，将暴力算法的时间复杂度从 O(n^2) 优化到 O(n*sqrt(n))。

## 算法原理

莫队算法的核心思想是：
1. 离线处理所有查询
2. 对查询进行特殊排序
3. 用双指针在相邻查询间转移状态

### 普通莫队
- 适用场景：可以 O(1) 时间内从 [l,r] 转移到 [l±1,r] 或 [l,r±1]
- 时间复杂度：O((n+m)*sqrt(n))
- 排序规则：按左端点所在块为第一关键字，右端点为第二关键字

### 回滚莫队
- 适用场景：添加元素容易，删除元素困难
- 分为两种：
  - 只增回滚莫队：只能添加元素
  - 只删回滚莫队：只能删除元素
- 时间复杂度：O((n+m)*sqrt(n))

### 带修莫队
- 适用场景：支持修改操作的区间查询
- 引入时间维度，排序规则增加时间关键字
- 时间复杂度：O((n+m)*n^(2/3))

### 树上莫队
- 适用场景：树上路径查询
- 使用欧拉序将树上问题转化为序列问题
- 时间复杂度：O((n+m)*sqrt(n))

### 二次离线莫队
- 适用场景：复杂区间查询问题
- 通过预处理转移信息优化复杂度
- 时间复杂度：O(n*sqrt(n) + m*sqrt(n))

## 经典题目

### 1. 普通莫队
- [小Z的袜子](https://www.luogu.com.cn/problem/P1494) - 入门题
- [HH的项链](https://www.luogu.com.cn/problem/P1972) - 经典题
- [XOR and Favorite Number](https://codeforces.com/contest/617/problem/E) - Codeforces 617E
- [小B的询问](https://www.luogu.com.cn/problem/P2709) - 模板题
- [数列找不同](https://www.luogu.com.cn/problem/P3901) - 应用题
- [DQUERY](https://www.spoj.com/problems/DQUERY/) - SPOJ经典题
- [Powerful array](https://codeforces.com/contest/86/problem/D) - Codeforces 86D

### 2. 回滚莫队
- [歴史の研究](https://www.luogu.com.cn/problem/AT_joisc2014_c) - 只增回滚莫队
- [相同数最远距离](https://www.luogu.com.cn/problem/P5906) - 回滚莫队应用
- [累加和为0的最长子数组](https://www.spoj.com/problems/ZQUERY/) - 问题转换
- [Rmq Problem / mex](https://www.luogu.com.cn/problem/P4137) - 只删回滚莫队
- [秃子酋长](https://www.luogu.com.cn/problem/P8078) - 复杂回滚莫队

### 3. 带修莫队
- [数颜色](https://www.luogu.com.cn/problem/P1903) - 经典带修莫队
- [糖果公园](https://www.luogu.com.cn/problem/P4074) - 带修树上莫队
- [小Z的袜子](https://www.luogu.com.cn/problem/P1494) - 带修版本
- [动态逆序对](https://www.luogu.com.cn/problem/P3157) - 带修莫队应用

### 4. 树上莫队
- [Count on a tree II](https://www.spoj.com/problems/COT2/) - 树上路径不同颜色数
- [秃子酋长](https://www.luogu.com.cn/problem/P8078) - 复杂树上莫队
- [COT2 - Count on a tree II](https://www.luogu.com.cn/problem/SP10707) - 树上莫队模板
- [Colorful Tree](https://vjudge.net/problem/HDU-5678) - HDU 5678

### 5. 二次离线莫队
- [Yuno loves sqrt technology II](https://www.luogu.com.cn/problem/P5047) - 二次离线莫队模板
- [掉进兔子洞](https://www.luogu.com.cn/problem/P4688) - 二次离线莫队应用
- [第十四分块(前体)](https://www.luogu.com.cn/problem/P4887) - SPOJ FOTILE

## 各大平台莫队题目汇总

### LeetCode (力扣)
- [3636. 查询超过阈值频率最高元素](https://leetcode.cn/problems/threshold-majority-queries/) - 回滚莫队
- [1157. 子数组中占绝大多数的元素](https://leetcode.com/problems/online-majority-element-in-subarray/) - 线段树/莫队
- [995. K 连续位的最小翻转次数](https://leetcode.cn/problems/minimum-number-of-k-consecutive-bit-flips/) - 差分/莫队
- [307. 区域和检索 - 数组可修改](https://leetcode.cn/problems/range-sum-query-mutable/) - 树状数组/带修莫队
- [699. 掉落的方块](https://leetcode.cn/problems/falling-squares/) - 线段树/莫队
- [846. 一手顺子](https://leetcode.cn/problems/hand-of-straights/) - 贪心/莫队
- [827. 最大人工岛](https://leetcode.cn/problems/making-a-large-island/) - 并查集/莫队

### 洛谷 (Luogu)
- [P1494 小Z的袜子](https://www.luogu.com.cn/problem/P1494) - 普通莫队入门
- [P1972 HH的项链](https://www.luogu.com.cn/problem/P1972) - 普通莫队经典
- [P2709 小B的询问](https://www.luogu.com.cn/problem/P2709) - 普通莫队模板
- [P3901 数列找不同](https://www.luogu.com.cn/problem/P3901) - 普通莫队应用
- [P5906 相同数最远距离](https://www.luogu.com.cn/problem/P5906) - 回滚莫队
- [P4137 Rmq Problem / mex](https://www.luogu.com.cn/problem/P4137) - 只删回滚莫队
- [P1903 数颜色](https://www.luogu.com.cn/problem/P1903) - 带修莫队
- [P4074 糖果公园](https://www.luogu.com.cn/problem/P4074) - 带修树上莫队
- [P3157 动态逆序对](https://www.luogu.com.cn/problem/P3157) - 带修莫队应用
- [P8078 秃子酋长](https://www.luogu.com.cn/problem/P8078) - 复杂回滚莫队
- [P5047 Yuno loves sqrt technology II](https://www.luogu.com.cn/problem/P5047) - 二次离线莫队
- [P4887 第十四分块(前体)](https://www.luogu.com.cn/problem/P4887) - 二次离线莫队
- [P4688 掉进兔子洞](https://www.luogu.com.cn/problem/P4688) - 二次离线莫队
- [P3857 [TJOI2008] 彩灯](https://www.luogu.com.cn/problem/P3857) - 线性基/莫队
- [P3527 [POI2011] MET-Meteors](https://www.luogu.com.cn/problem/P3527) - 二分/莫队
- [P4151 [WC2011] 最大XOR和](https://www.luogu.com.cn/problem/P4151) - 线性基/莫队
- [P3554 [POI2013] LUK-Triumphal arch](https://www.luogu.com.cn/problem/P3554) - 二分/树上莫队

### Codeforces
- [617E XOR and Favorite Number](https://codeforces.com/contest/617/problem/E) - 普通莫队
- [86D Powerful array](https://codeforces.com/contest/86/problem/D) - 普通莫队
- [940F Machine Learning](https://codeforces.com/contest/940/problem/F) - 带修莫队
- [1009F Dominant Indices](https://codeforces.com/contest/1009/problem/F) - 树上莫队
- [438D The Child and Sequence](https://codeforces.com/contest/438/problem/D) - 线段树/莫队
- [245H Queries for Number of Palindromes](https://codeforces.com/contest/245/problem/H) - 回文树/莫队
- [765F Souvenirs](https://codeforces.com/contest/765/problem/F) - 二次离线莫队
- [364D Ghd](https://codeforces.com/contest/364/problem/D) - 随机化/莫队

### SPOJ
- [DQUERY D-query](https://www.spoj.com/problems/DQUERY/) - 普通莫队
- [COT2 Count on a tree II](https://www.spoj.com/problems/COT2/) - 树上莫队
- [ZQUERY Zero Query](https://www.spoj.com/problems/ZQUERY/) - 回滚莫队
- [FOTILE 第十四分块(前体)](https://www.spoj.com/problems/FOTILE/) - 二次离线莫队
- [COT Count on a tree](https://www.spoj.com/problems/COT/) - 树上莫队
- [MKTHNUM K-th number](https://www.spoj.com/problems/MKTHNUM/) - 主席树/莫队
- [LCS Longest Common Substring](https://www.spoj.com/problems/LCS/) - 后缀自动机/莫队
- [TRIP Triplets](https://www.spoj.com/problems/TRIP/) - 莫队应用

### AtCoder
- [AT_joisc2014_c 歴史の研究](https://www.luogu.com.cn/problem/AT_joisc2014_c) - 只增回滚莫队
- [ARC081F Flip and Rectangles](https://atcoder.jp/contests/arc081/tasks/arc081_d) - 单调栈/莫队
- [ABC242G Range Count Query](https://atcoder.jp/contests/abc242/tasks/abc242_g) - 离线查询/莫队
- [ABC267G Constrained Nim 2](https://atcoder.jp/contests/abc267/tasks/abc267_g) - 博弈论/莫队
- [ABC293G Triple Index](https://atcoder.jp/contests/abc293/tasks/abc293_g) - 莫队应用

### HDU (杭电OJ)
- [5678 Colorful Tree](https://acm.hdu.edu.cn/showproblem.php?pid=5678) - 树上莫队
- [3339 In Action](https://acm.hdu.edu.cn/showproblem.php?pid=3339) - mex相关/莫队
- [4394 Digital Square](https://acm.hdu.edu.cn/showproblem.php?pid=4394) - BFS/莫队
- [4417 Super Mario](https://acm.hdu.edu.cn/showproblem.php?pid=4417) - 二分/莫队
- [5927 Auxiliary Set](https://acm.hdu.edu.cn/showproblem.php?pid=5927) - 树结构/莫队

### LibreOJ
- [2874.歴史の研究](https://loj.ac/p/2874) - 只增回滚莫队
- [2128. 「SCOI2015」情报传递](https://loj.ac/p/2128) - 树上莫队
- [2136. 「SCOI2015」小凸玩矩阵](https://loj.ac/p/2136) - 二分/莫队
- [2139. 「SCOI2015」品酒大会](https://loj.ac/p/2139) - 后缀数组/莫队

### LintCode (炼码)
- [465. K-th Smallest Sum In Two Sorted Arrays](https://www.lintcode.com/problem/465/) - 二分/莫队
- [685. 最近公共祖先 III](https://www.lintcode.com/problem/685/) - 树上莫队
- [892. Alien Dictionary](https://www.lintcode.com/problem/892/) - 拓扑排序/莫队
- [919. Meeting Rooms II](https://www.lintcode.com/problem/919/) - 扫描线/莫队

### HackerRank
- [The Maximum Subarray](https://www.hackerrank.com/challenges/maximum-subarray/problem) - 动态规划/莫队
- [Dynamic Array](https://www.hackerrank.com/challenges/dynamic-array/problem) - 数组/莫队
- [Candies](https://www.hackerrank.com/challenges/candies/problem) - 贪心/莫队
- [Sherlock and Anagrams](https://www.hackerrank.com/challenges/sherlock-and-anagrams/problem) - 哈希/莫队

### USACO
- [USACO 2012 Open Silver Cow Coupons](https://usaco.org/index.php?page=viewproblem2&cpid=124) - 贪心/莫队
- [USACO 2013 US Open Gold Photo](https://usaco.org/index.php?page=viewproblem2&cpid=283) - 单调栈/莫队
- [USACO 2014 February Gold Roadblock](https://usaco.org/index.php?page=viewproblem2&cpid=382) - 双端队列/莫队

### 牛客
- [牛客OI赛制测试赛](https://ac.nowcoder.com/acm/contest/277/B) - 莫队应用
- [牛客挑战赛](https://ac.nowcoder.com/acm/contest/1033/F) - 树上莫队
- [牛客练习赛](https://ac.nowcoder.com/acm/contest/1113/C) - 回滚莫队

### UVa OJ
- [UVa 11991 Easy Problem from Rujia Liu?](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3142) - 莫队应用
- [UVa 12345 Dynamic len(set(a[i..j]))](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3576) - 普通莫队
- [UVa 12995 Farey Sequence](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4877) - 数学/莫队

### TimusOJ
- [Timus 1730 Oleg and squares](https://acm.timus.ru/problem.aspx?space=1&num=1730) - 数学/莫队
- [Timus 1846 Nephren Runs a Cinema](https://acm.timus.ru/problem.aspx?space=1&num=1846) - 前缀和/莫队

### AizuOJ
- [Aizu 2453 XOR Puzzle](https://onlinejudge.u-aizu.ac.jp/problems/2453) - XOR/莫队
- [Aizu 2624 Unfair Nim](https://onlinejudge.u-aizu.ac.jp/problems/2624) - 博弈论/莫队

### 其他平台
- [Project Euler Problem 185](https://projecteuler.net/problem=185) - 回溯/莫队
- [HackerEarth Problem](https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/) - 线段树/莫队
- [计蒜客 Problem](https://nanti.jisuanke.com/t/45499) - 莫队应用
- [Comet OJ Problem](https://cometoj.com/contest/58/problem/E) - 树上莫队
- [MarsCode Problem](https://marscode.cn/problem/214) - 莫队应用

## 实现要点

### 分块策略
```java
// 块大小通常选择 sqrt(n)
int blockSize = (int) Math.sqrt(n);
int blockNum = (n + blockSize - 1) / blockSize;

// 带修改莫队通常选择 n^(2/3)
int blockSize = (int) Math.pow(n, 2.0/3.0);
```

### 排序规则
```java
// 普通莫队排序
public static class QueryComparator implements Comparator<int[]> {
    @Override
    public int compare(int[] a, int[] b) {
        if (belong[a[0]] != belong[b[0]]) {
            return belong[a[0]] - belong[b[0]];
        }
        return a[1] - b[1];
    }
}
```

### 状态转移
```java
// 添加元素
public static void add(int value) {
    // 更新计数
    count[value]++;
    // 更新答案
    currentAnswer += count[value] - 1;
}

// 删除元素
public static void remove(int value) {
    // 更新答案
    currentAnswer -= count[value] - 1;
    // 更新计数
    count[value]--;
}
```

## 复杂度分析

| 算法类型 | 时间复杂度 | 空间复杂度 | 适用场景 |
|---------|-----------|-----------|---------|
| 普通莫队 | O(n*sqrt(n)) | O(n) | 基本区间查询 |
| 回滚莫队 | O(n*sqrt(n)) | O(n) | 单向操作问题 |
| 带修莫队 | O(n^(5/3)) | O(n) | 支持修改操作 |
| 树上莫队 | O(n*sqrt(n)) | O(n) | 树上路径查询 |
| 二次离线莫队 | O(n*sqrt(n) + m*sqrt(n)) | O(n*sqrt(n)) | 复杂区间查询 |

## 优化技巧

1. **IO优化**：使用快速读写
2. **离散化**：处理值域较大的情况
3. **分块大小**：根据具体题目调整
4. **常数优化**：减少不必要的计算
5. **预处理优化**：合理预处理转移信息
6. **奇偶排序优化**：对于奇偶块采用不同的排序策略，减少指针移动
7. **内存优化**：合理使用数据结构，避免内存浪费

## 应用场景总结

莫队算法适用于以下场景：
1. 离线区间查询问题
2. 可以O(1)时间转移状态
3. 传统数据结构难以维护的信息
4. 需要处理大量查询的情况
5. 区间统计类问题（如不同元素个数、出现次数、最大/最小值等）
6. 特殊性质的区间问题（如异或和、连续子数组和等）

通过合理选择莫队算法的变种，可以解决各种复杂的区间查询问题。