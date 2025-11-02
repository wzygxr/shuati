# 分块算法 (Sqrt Decomposition) 题目大全

## 已有题目 (class175目录中)

### 1. 哈希冲突 (Code01_HashCollision)
- **题目链接**: https://www.luogu.com.cn/problem/P3396
- **题意**: 支持两种操作：
  1. 查询满足 i % x == y 的所有位置的 arr[i] 之和
  2. 单点更新
- **解法**: 对于x <= √n的情况预处理，对于x > √n的情况暴力查询

### 2. 数组查询 (Code02_ArrayQueries)
- **题目链接**: https://www.luogu.com.cn/problem/CF797E
- **题意**: 查询从位置p开始，每次跳跃arr[p]+k步，直到越界需要的步数
- **解法**: 对于k <= √n的情况预处理dp，对于k > √n的情况暴力计算

### 3. 等差数列求和 (Code03_SumOfProgression)
- **题目链接**: https://www.luogu.com.cn/problem/CF1921F
- **题意**: 查询等差数列子序列的加权和
- **解法**: 预处理前缀和与加权前缀和

### 4. 初始化 (Code04_Initialization)
- **题目链接**: https://www.luogu.com.cn/problem/P5309
- **题意**: 支持区间加法和区间求和
- **解法**: 分块维护区间和

### 5. 雅加达的摩天楼 (Code05_Skyscraper)
- **题目链接**: https://www.luogu.com.cn/problem/P3645
- **题意**: BFS最短路，狗可以在建筑物间跳跃
- **解法**: 分块优化BFS

### 6. 最少划分 (Code06_TillCollapse)
- **题目链接**: https://www.luogu.com.cn/problem/CF786C
- **题意**: 将数组划分成最少段数，每段不同数字种类不超过k
- **解法**: 分块优化查询

### 7. 给你一棵树 (Code07_GivenTree)
- **题目链接**: https://www.luogu.com.cn/problem/CF1039D
- **题意**: 树上路径匹配问题
- **解法**: 分块优化动态规划

### 8. Serega and Fun (Code08_SeregaAndFun)
- **题目链接**: https://codeforces.com/problemset/problem/455/D
- **题意**: 支持查询区间众数和元素移动操作
- **解法**: 分块维护，定期重构

### 9. Little Elephant and Array (Code09_LittleElephantAndArray)
- **题目链接**: https://codeforces.com/problemset/problem/220/B
- **题意**: 查询区间内满足值等于出现次数的数的个数
- **解法**: Mo算法（离线分块）

### 10. D-query (Code10_DQuery)
- **题目链接**: https://www.spoj.com/problems/DQUERY/
- **题意**: 给定一个数组，多次查询区间[l,r]内不同数字的个数
- **解法**: Mo算法（离线分块）

### 11. Powerful array (Code11_PowerfulArray)
- **题目链接**: https://codeforces.com/problemset/problem/86/D
- **题意**: 给定一个数组，多次查询区间[l,r]的加权和，权重为每个数字出现次数的平方
- **解法**: Mo算法（离线分块）

### 12. Holes (Code12_Holes)
- **题目链接**: https://codeforces.com/problemset/problem/13/E
- **题意**: 给定一个数组，支持两种操作：1. 查询从位置i开始跳出数组需要的步数；2. 修改某个位置的值
- **解法**: 分块维护跳跃信息

### 13. Points on Plane (Code13_PointsOnPlane)
- **题目链接**: https://codeforces.com/problemset/problem/1181/C
- **题意**: 在一个网格中统计满足条件的点对数量
- **解法**: 分块优化计算

### 14. Ann and Books (Code14_AnnAndBooks)
- **题目链接**: https://codeforces.com/problemset/problem/877/E
- **题意**: 给定一个数组，多次查询区间[l,r]内满足特定条件的子数组个数
- **解法**: 分块维护前缀信息

### 15. XOR and Favorite Number (Code15_XorAndFavoriteNumber)
- **题目链接**: https://codeforces.com/problemset/problem/617/E
- **题意**: 给定一个数组和一个数字k，多次查询区间[l,r]内异或值等于k的子数组对数
- **解法**: Mo算法（离线分块）+ 异或前缀和

### 16. Little Elephant and Array (Code16_LittleElephantAndArray)
- **题目链接**: https://codeforces.com/problemset/problem/220/B
- **题意**: 查询区间内满足值等于出现次数的数的个数
- **解法**: Mo算法（离线分块）

### 17. Tree and Queries (Code17_TreeAndQueries)
- **题目链接**: https://codeforces.com/problemset/problem/375/D
- **题意**: 树上查询，给定一棵树和每个节点的颜色，多次查询子树内满足条件的颜色数量
- **解法**: 树上Mo算法（离线分块）

### 18. Destiny (Code18_Destiny)
- **题目链接**: https://codeforces.com/problemset/problem/840/D
- **题意**: 给定一个数组，多次查询区间[l,r]内出现次数超过阈值的数字
- **解法**: 分块维护频率信息

### 19. PATULJCI (Code41_Patuljci)
- **题目链接**: https://www.spoj.com/problems/PATULJCI/
- **题意**: 给定一个数组和多个查询，每个查询要求找出区间[l,r]内的众数（出现次数最多的元素）
- **解法**: 莫队算法（离线分块）

### 20. MKTHNUM (Code42_Mkthnum)
- **题目链接**: https://www.spoj.com/problems/MKTHNUM/
- **题意**: 给定一个数组和多个查询，每个查询要求找出区间[l,r]内第k小的数字
- **解法**: 莫队算法（离线分块）+ 离散化

### 21. GIVEAWAY (Code43_Giveaway)
- **题目链接**: https://www.spoj.com/problems/GIVEAWAY/
- **题意**: 维护一个数组，支持两种操作：1. C x y: 将位置x的值更新为y；2. Q l r k: 查询区间[l,r]内大于等于k的元素个数
- **解法**: 分块算法维护排序块

## 补充题目列表

### 19. Give Away
- **来源**: SPOJ
- **题目链接**: https://www.spoj.com/problems/GIVEAWAY/
- **题意**: 给定一个数组，支持区间更新和区间查询操作
- **解法**: 分块维护区间信息

### 20. RACETIME
- **来源**: SPOJ
- **题目链接**: https://www.spoj.com/problems/RACETIME/
- **题意**: 给定一个数组，支持区间更新和区间查询操作
- **解法**: 分块维护区间信息

### 21. Till I Collapse
- **来源**: Codeforces
- **题目链接**: https://codeforces.com/problemset/problem/786/C
- **题意**: 将数组划分成最少段数，每段不同数字种类不超过k
- **解法**: 分块优化查询

### 22. Bridges
- **来源**: APIO
- **题目链接**: https://apio2019.toki.id/problems/bridges/
- **题意**: 维护图的连通性，支持边权修改和连通性查询
- **解法**: 分块处理修改操作

### 23. Curious Robin Hood
- **来源**: LightOJ
- **题目链接**: http://lightoj.com/volume_showproblem.php?problem=1112
- **题意**: 给定一个数组，支持单点更新和区间求和操作
- **解法**: 分块维护区间和

### 24. Catapult that ball
- **来源**: SPOJ
- **题目链接**: https://www.spoj.com/problems/THRBL/
- **题意**: 给定一个数组，多次查询区间内最大值是否满足特定条件
- **解法**: 分块维护区间最大值

### 25. K-th Number
- **来源**: SPOJ
- **题目链接**: https://www.spoj.com/problems/MKTHNUM/
- **题意**: 给定一个数组，多次查询区间[l,r]内第k小的数字
- **解法**: 分块维护排序信息

### 26. Election Posters
- **来源**: SPOJ
- **题目链接**: https://www.spoj.com/problems/POSTERS/
- **题意**: 区间覆盖问题，查询最终可见的海报数量
- **解法**: 分块维护区间覆盖信息

### 27. Array Transformer
- **来源**: UVa
- **题目链接**: https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=3255
- **题意**: 给定一个数组，支持区间更新和区间查询操作
- **解法**: 分块维护区间信息

### 28. Range Sum Query - Mutable
- **来源**: LeetCode
- **题目链接**: https://leetcode.com/problems/range-sum-query-mutable/
- **题意**: 给定一个数组，支持单点更新和区间求和操作
- **解法**: 分块维护区间和

### 29. Range Frequency Queries
- **来源**: LeetCode
- **题目链接**: https://leetcode.com/problems/range-frequency-queries/
- **题意**: 设计一个数据结构来查找给定子数组中给定值的频率
- **解法**: 分块维护频率信息

### 30. Fruits Into Baskets III
- **来源**: LeetCode
- **题目链接**: https://leetcode.com/problems/fruits-into-baskets-iii/
- **题意**: 在一个数组中找到满足特定条件的最长子数组
- **解法**: 分块优化滑动窗口

### 31. DZY Loves Colors
- **来源**: Codeforces
- **题目链接**: http://codeforces.com/contest/444/problem/C
- **题意**: 支持两种操作：1. 区间染色并累加颜色变化量；2. 查询区间颜色变化量的总和
- **解法**: 分块维护区间颜色信息和懒标记

### 32. LOJ 分块一
- **来源**: LibreOJ
- **题目链接**: https://loj.ac/problem/6277
- **题意**: 实现数据的区间修改和单点查询
- **解法**: 分块维护区间加法标记

### 33. 牛牛算题
- **来源**: 牛客编程巅峰赛
- **题意**: 计算对于小于等于n的每个数p，求n = p×k + m中的k×m之和
- **解法**: 整数分块优化

### 34. 最大子阵列
- **来源**: 计蒜客
- **题目链接**: https://www.jisuanke.com/course/705/27296
- **题意**: 在数组中找出和最大的连续子数组
- **解法**: 分块预处理子数组信息

### 35. 区间第k大
- **来源**: POJ
- **题目链接**: http://poj.org/problem?id=2104
- **题意**: 多次查询区间[l,r]内第k小的数字
- **解法**: 分块维护排序块

### 36. 扫描线问题
- **来源**: HDU
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1542
- **题意**: 计算矩形覆盖的面积
- **解法**: 分块处理扫描线

### 37. 彩色树
- **来源**: AizuOJ
- **题目链接**: https://onlinejudge.u-aizu.ac.jp/problems/2667
- **题意**: 树上路径查询，求路径上不同颜色的数量
- **解法**: 树上分块 + Mo算法

### 38. 区间众数查询
- **来源**: 洛谷
- **题目链接**: https://www.luogu.com.cn/problem/P3709
- **题意**: 多次查询区间[l,r]内的众数
- **解法**: 分块预处理众数信息

### 39. 二维区间查询
- **来源**: 牛客
- **题目链接**: https://ac.nowcoder.com/acm/contest/884/A
- **题意**: 二维数组的区间查询和更新
- **解法**: 二维分块

### 40. 滑动窗口最大值
- **来源**: LeetCode
- **题目链接**: https://leetcode.com/problems/sliding-window-maximum/
- **题意**: 给定一个数组和滑动窗口大小，输出每个窗口的最大值
- **解法**: 分块预处理窗口信息

## 题目分类

### 1. 区间查询类
- D-query
- Powerful array
- Ann and Books
- XOR and Favorite Number
- Tree and Queries
- Destiny
- K-th Number
- Range Frequency Queries
- PATULJCI (Code41_Patuljci)
- MKTHNUM (Code42_Mkthnum)
- GIVEAWAY (Code43_Giveaway)

### 2. 区间更新类
- Give Away
- RACETIME
- Curious Robin Hood
- Election Posters
- Array Transformer
- Range Sum Query - Mutable
- GIVEAWAY (Code43_Giveaway)

### 3. 跳跃类
- Holes
- Points on Plane
- Serega and Fun

### 4. 划分类
- Till I Collapse
- 最少划分 (Code06_TillCollapse)

### 5. 树上问题
- Tree and Queries
- 给你一棵树 (Code07_GivenTree)

### 6. 优化类
- Bridges
- Catapult that ball
- Fruits Into Baskets III

## 解法分类

### 1. 标准分块
- 哈希冲突 (Code01_HashCollision)
- 数组查询 (Code02_ArrayQueries)
- 初始化 (Code04_Initialization)
- 雅加达的摩天楼 (Code05_Skyscraper)
- Serega and Fun (Code08_SeregaAndFun)
- Curious Robin Hood
- Range Sum Query - Mutable
- GIVEAWAY (Code43_Giveaway)

### 2. Mo算法（离线分块）
- Little Elephant and Array (Code09_LittleElephantAndArray)
- D-query
- Powerful array
- XOR and Favorite Number
- Tree and Queries
- PATULJCI (Code41_Patuljci)
- MKTHNUM (Code42_Mkthnum)

### 3. 分块重构
- Serega and Fun (Code08_SeregaAndFun)

### 4. 分块+DP
- 等差数列求和 (Code03_SumOfProgression)
- 给你一棵树 (Code07_GivenTree)

### 5. 树上分块
- Tree and Queries

## 时间复杂度分析

大多数分块算法的时间复杂度为O(√n)或O(n√n)，具体取决于问题类型：
- 区间查询：O(√n)
- 区间更新：O(√n)
- 单点更新：O(1)或O(√n)
- Mo算法：O((n+m)√n)，其中n为数组长度，m为查询数量

## 空间复杂度分析

分块算法的空间复杂度通常为O(n)，需要额外存储块信息和预处理数据。

## 学习建议

1. **从简单题目开始**：建议先掌握标准分块算法，再学习Mo算法等高级技巧
2. **理解核心思想**：分块的本质是平衡预处理时间和查询时间
3. **掌握实现细节**：注意边界处理、块大小选择等细节
4. **多做练习**：通过大量练习掌握不同场景下的分块应用
5. **学习优化技巧**：了解分块重构、树上分块等高级技巧