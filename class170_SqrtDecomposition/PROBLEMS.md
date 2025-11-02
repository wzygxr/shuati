# 分块算法题目大全

## 一、LibreOJ数列分块入门系列

### 1. 数列分块入门1 (LibreOJ #6277)
**题目要求**：区间加法，单点查值
**核心技巧**：懒惰标记
**时间复杂度**：O(√n) / 操作
**测试链接**：https://loj.ac/p/6277

### 2. 数列分块入门2 (LibreOJ #6278)
**题目要求**：区间加法，询问区间内小于某个值x的元素个数
**核心技巧**：排序 + 二分查找
**时间复杂度**：O(√n * log(√n)) / 操作
**测试链接**：https://loj.ac/p/6278

### 3. 数列分块入门3 (LibreOJ #6279)
**题目要求**：区间加法，询问区间内小于某个值x的前驱
**核心技巧**：有序数据结构
**时间复杂度**：O(√n * log(√n)) / 操作
**测试链接**：https://loj.ac/p/6279

### 4. 数列分块入门4 (LibreOJ #6280)
**题目要求**：区间加法，区间求和
**核心技巧**：维护块元素和
**时间复杂度**：O(√n) / 操作
**测试链接**：https://loj.ac/p/6280

### 5. 数列分块入门5 (LibreOJ #6281)
**题目要求**：区间开方，区间求和
**核心技巧**：标记优化
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://loj.ac/p/6281

### 6. 数列分块入门6 (LibreOJ #6282)
**题目要求**：单点插入，单点询问
**核心技巧**：动态分块 + 重构
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://loj.ac/p/6282

### 7. 数列分块入门7 (LibreOJ #6283)
**题目要求**：区间乘法，区间加法，单点询问
**核心技巧**：标记优先级
**时间复杂度**：O(√n) / 操作
**测试链接**：https://loj.ac/p/6283

### 8. 数列分块入门8 (LibreOJ #6284)
**题目要求**：区间询问等于一个数c的元素个数，并将区间所有元素改为c
**核心技巧**：标记优化
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://loj.ac/p/6284

### 9. 数列分块入门9 (LibreOJ #6285)
**题目要求**：询问区间的最小众数
**核心技巧**：预处理 + 统计优化
**时间复杂度**：O(√n) / 查询
**测试链接**：https://loj.ac/p/6285

## 二、SPOJ题目

### 10. SPOJ GIVEAWAY
**题目要求**：区间查询大于等于某个值的元素个数，单点修改
**核心技巧**：排序 + 二分查找
**时间复杂度**：O(√n * log(√n)) / 操作
**测试链接**：https://www.spoj.com/problems/GIVEAWAY/

### 11. SPOJ DQUERY
**题目要求**：区间查询不同元素个数
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://www.spoj.com/problems/DQUERY/

## 三、洛谷题目

### 12. [Violet]蒲公英
**题目要求**：区间查询众数
**核心技巧**：预处理 + 分块
**时间复杂度**：O(√n) / 查询
**测试链接**：https://www.luogu.com.cn/problem/P4168

## 四、UVa题目

### 13. UVA 12003 Array Transformer
**题目要求**：区间查询小于某个值的元素个数，单点修改
**核心技巧**：分块 + 排序 + 二分查找
**时间复杂度**：O(√n * log(√n)) / 操作
**测试链接**：https://vjudge.net/problem/UVA-12003

## 五、POJ题目

### 14. POJ 2104 K-th Number
**题目要求**：区间查询第k小元素
**核心技巧**：分块 + 排序 + 二分答案
**时间复杂度**：O(√n * log(n)) / 查询
**测试链接**：http://poj.org/problem?id=2104

## 六、CodeChef题目

### 15. CodeChef COUNTARI
**题目要求**：统计满足等差数列条件的三元组个数
**核心技巧**：分块 + FFT
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：https://www.codechef.com/problems/COUNTARI

### 16. CodeChef FNCS
**题目要求**：区间函数求和，单点修改
**核心技巧**：分块 + 前缀和
**时间复杂度**：O(√n) / 操作
**测试链接**：https://www.codechef.com/problems/FNCS

## 七、Codeforces题目

### 17. Codeforces 617E XOR and Favorite Number
**题目要求**：区间查询异或值等于k的子区间个数
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/617/E

### 18. Codeforces 220B Little Elephant and Array
**题目要求**：区间查询"好数"个数
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/220/B

### 19. Codeforces 86D Powerful Array
**题目要求**：区间查询"能量值"
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/86/D

### 20. Codeforces 1129D Isolation
**题目要求**：划分数组使得每段中只出现一次的元素个数不超过k
**核心技巧**：分块优化DP
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/1129/D

### 21. Codeforces 915E Physical Education Lessons
**题目要求**：区间染色，查询白色区间个数
**核心技巧**：分块 + 懒惰标记
**时间复杂度**：O(√n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/915/E

## 八、HYSBZ题目

### 22. HYSBZ 2038 小Z的袜子
**题目要求**：区间查询相同颜色袜子对的概率
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=2038

### 23. HYSBZ 2741 【FOTILE模拟赛】L
**题目要求**：区间查询最大连续异或和
**核心技巧**：分块 + 可持久化Trie
**时间复杂度**：O(n * √n) / 操作
**测试链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=2741

## 九、其他平台题目

### 24. HYSBZ 3509 [CodeChef] COUNTARI
**题目要求**：统计满足等差数列条件的三元组个数
**核心技巧**：分块 + FFT
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=3509

### 25. HYSBZ 2724 [Violet 6]蒲公英
**题目要求**：区间查询众数
**核心技巧**：预处理 + 分块
**时间复杂度**：O(√n) / 查询
**测试链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=2724

## 十、扩展题目

### 26. LibreOJ #6286 数列分块入门6扩展
**题目要求**：单点插入，单点查询
**核心技巧**：动态分块 + 重构
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://loj.ac/p/6286

### 27. LibreOJ #6287 数列分块入门7扩展
**题目要求**：区间乘法，区间加法，单点查询
**核心技巧**：标记优先级
**时间复杂度**：O(√n) / 操作
**测试链接**：https://loj.ac/p/6287

### 28. LibreOJ #6288 数列分块入门8扩展
**题目要求**：区间查询等于某个值的元素个数，区间修改为同一值
**核心技巧**：标记优化
**时间复杂度**：O(√n) 均摊 / 操作
**测试链接**：https://loj.ac/p/6288

### 29. LibreOJ #6289 数列分块入门9扩展
**题目要求**：区间查询最小众数
**核心技巧**：预处理 + 统计优化
**时间复杂度**：O(√n) / 查询
**测试链接**：https://loj.ac/p/6289

## 十一、新增题目

### 30. HDU 5381 The sum of gcd
**题目要求**：区间查询所有子区间的GCD之和
**核心技巧**：分块预处理
**时间复杂度**：O(n * √n * log n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5381

### 31. 牛客网 NC15277 区间异或和
**题目要求**：区间异或操作，单点查询
**核心技巧**：分块标记
**时间复杂度**：O(√n) / 操作
**测试链接**：https://ac.nowcoder.com/acm/problem/15277

### 32. 洛谷 P5356 由乃打扑克
**题目要求**：区间查询第k小，区间加法
**核心技巧**：分块排序 + 二分答案
**时间复杂度**：O(√n * log n) / 查询
**测试链接**：https://www.luogu.com.cn/problem/P5356

### 33. 力扣 LeetCode 307. 区域和检索 - 数组可修改
**题目要求**：区间求和，单点修改
**核心技巧**：分块维护区间和
**时间复杂度**：O(√n) / 操作
**测试链接**：https://leetcode.cn/problems/range-sum-query-mutable/

### 34. 计蒜客 T1131 数列区间最大值
**题目要求**：区间最大值查询，单点修改
**核心技巧**：分块维护区间最大值
**时间复杂度**：O(√n) / 操作
**测试链接**：https://nanti.jisuanke.com/t/T1131

### 35. 杭电 HDU 1556 Color the ball
**题目要求**：区间更新，单点查询
**核心技巧**：分块标记
**时间复杂度**：O(√n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=1556

### 36. 洛谷 P2054 [AHOI2005] 洗牌
**题目要求**：模拟洗牌过程，查询最终位置
**核心技巧**：分块优化模拟
**时间复杂度**：O(√n) / 操作
**测试链接**：https://www.luogu.com.cn/problem/P2054

### 37. 牛客网 NC24210 区间加区间求和
**题目要求**：区间加法，区间求和
**核心技巧**：分块维护区间和
**时间复杂度**：O(√n) / 操作
**测试链接**：https://ac.nowcoder.com/acm/problem/24210

### 38. AtCoder ABC174 F Range Set Query
**题目要求**：区间查询不同元素个数
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://atcoder.jp/contests/abc174/tasks/abc174_f

### 39. Codeforces 103D Time to Raid Cowavans
**题目要求**：多次跳跃查询区间和
**核心技巧**：分块预处理
**时间复杂度**：O(n√n) 预处理，O(√n) 查询
**测试链接**：https://codeforces.com/problemset/problem/103/D

### 40. 力扣 LeetCode 2439. 最小化数组中的最大值
**题目要求**：将数组分成k个子数组，最小化子数组最大值
**核心技巧**：分块 + 贪心
**时间复杂度**：O(n log n) / 操作
**测试链接**：https://leetcode.cn/problems/minimize-maximum-of-array/

### 41. 赛码网 区间修改区间查询
**题目要求**：区间乘法，区间加法，区间求和
**核心技巧**：分块双标记
**时间复杂度**：O(√n) / 操作
**测试链接**：https://www.acmcoder.com/#/problem/

### 42. HackerEarth Range Query Challenges
**题目要求**：区间查询不同元素个数
**核心技巧**：分块 + 预处理
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://www.hackerearth.com/practice/data-structures/advanced-data-structures/fenwick-binary-indexed-trees/practice-problems/

### 43. UVa 11990 Dynamic Inversion
**题目要求**：动态维护逆序对数量
**核心技巧**：分块 + 树状数组
**时间复杂度**：O(n√n log n) / 操作
**测试链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3141

### 44. 剑指Offer 03. 数组中重复的数字
**题目要求**：查找数组中重复的数字
**核心技巧**：分块统计
**时间复杂度**：O(n) / 操作
**测试链接**：https://leetcode.cn/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof/

### 45. 杭电 HDU 5072 Coprime
**题目要求**：区间查询与给定数互质的元素个数
**核心技巧**：分块 + 容斥原理
**时间复杂度**：O(n√n * 2^m) / 操作，其中m是质因数个数
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5072

### 46. Codeforces 486E LIS of Sequence
**题目要求**：求序列的最长递增子序列相关信息
**核心技巧**：分块预处理
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://codeforces.com/problemset/problem/486/E

### 47. 洛谷 P1494 [国家集训队] 小 Z 的袜子
**题目要求**：区间查询相同颜色袜子对的概率
**核心技巧**：莫队算法 + 分块
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://www.luogu.com.cn/problem/P1494

### 48. Project Euler 283 Integer sided triangles for which the area is a multiple of the perimeter
**题目要求**：统计满足条件的三角形数量
**核心技巧**：分块优化枚举
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://projecteuler.net/problem=283

### 49. HackerRank Frequency Queries
**题目要求**：维护频率信息的查询
**核心技巧**：分块统计频率
**时间复杂度**：O(n√n) / 操作
**测试链接**：https://www.hackerrank.com/challenges/frequency-queries/problem

## 十二、分块算法综合训练题目

### 50. 综合训练1：分块 + 莫队算法实战
**训练内容**：
- 实现普通莫队解决区间不同元素计数问题
- 实现带修莫队处理动态区间查询
- 优化莫队算法常数
- 处理大数据测试用例

**推荐题目**：
1. SPOJ DQUERY
2. Codeforces 617E XOR and Favorite Number
3. 洛谷 P1903 数颜色

### 51. 综合训练2：分块优化DP和其他高级应用
**训练内容**：
- 学习分块如何优化动态规划
- 掌握分块与其他数据结构的结合
- 处理复杂的块内数据维护

**推荐题目**：
1. Codeforces 1129D Isolation
2. HYSBZ 2741 FOTILE模拟赛
3. HDU 5381 The sum of gcd

### 52. 综合训练3：分块算法工程化实践
**训练内容**：
- 实现高效的分块模板
- 添加异常处理和边界检查
- 性能优化和常数优化
- 编写单元测试确保正确性

**实践目标**：
- 构建一个通用的分块算法库
- 支持多种常见的区间操作
- 能够处理1e5规模的数据
- 具备良好的代码可读性和可维护性
**核心技巧**：分块 + GCD性质
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5381

### 31. HDU 5140 HDU 5140
**题目要求**：三维偏序问题
**核心技巧**：分块 + CDQ分治
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5140

### 32. HDU 5636 Shortest Path
**题目要求**：最短路问题
**核心技巧**：分块 + 最短路算法
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5636

### 33. HDU 5618 Jam's problem again
**题目要求**：三维偏序问题
**核心技巧**：分块 + 树状数组
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5618

### 34. HDU 5412 CRB and Queries
**题目要求**：区间第k小元素
**核心技巧**：分块 + 整体二分
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5412

### 35. HDU 5293 Tree
**题目要求**：树上DP问题
**核心技巧**：分块 + 树链剖分
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5293

### 36. HDU 5171 GTY's birthday gift
**题目要求**：区间修改，区间查询
**核心技巧**：分块 + 线段树
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5171

### 37. HDU 5029 Relief grain
**题目要求**：树上差分问题
**核心技巧**：分块 + 树上差分
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5029

### 38. HDU 4941 Magical Box
**题目要求**：二维数据结构问题
**核心技巧**：分块 + 二维树状数组
**时间复杂度**：O(n * √n * log²(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4941

### 39. HDU 4867 Easy String Problem
**题目要求**：字符串处理问题
**核心技巧**：分块 + 字符串哈希
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4867

### 40. HDU 4777 Rabbit and Hopscotch
**题目要求**：图论问题
**核心技巧**：分块 + 最短路
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4777

### 41. HDU 4638 Group
**题目要求**：区间查询问题
**核心技巧**：分块 + 并查集
**时间复杂度**：O(n * √n * α(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4638

### 42. HDU 4622 Reincarnation
**题目要求**：字符串不同子串个数
**核心技巧**：分块 + 后缀数组
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4622

### 43. HDU 4507 吉哥系列故事——恨7不成妻
**题目要求**：数位DP问题
**核心技巧**：分块 + 数位DP
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4507

### 44. HDU 4366 Successor
**题目要求**：树上查询问题
**核心技巧**：分块 + 树链剖分
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4366

### 45. HDU 4358 Boring counting
**题目要求**：树上查询问题
**核心技巧**：分块 + 树上莫队
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4358

### 46. HDU 4348 To the moon
**题目要求**：区间历史版本查询
**核心技巧**：分块 + 可持久化数据结构
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4348

### 47. HDU 4251 The Famous ICPC Team Again
**题目要求**：区间最值问题
**核心技巧**：分块 + RMQ
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4251

### 48. HDU 4217 Data Structure?
**题目要求**：动态排名问题
**核心技巧**：分块 + 二分查找
**时间复杂度**：O(n * √n * log(n)) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4217

### 49. HDU 4008 Parent and son
**题目要求**：树上查询问题
**核心技巧**：分块 + 树链剖分
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4008

### 50. HDU 3950 Parking Log
**题目要求**：区间操作问题
**核心技巧**：分块 + 懒惰标记
**时间复杂度**：O(n * √n) / 操作
**测试链接**：http://acm.hdu.edu.cn/showproblem.php?pid=3950

## 总结

以上题目涵盖了分块算法的主要应用场景和技巧，从基础的区间操作到高级的优化应用，为深入学习分块算法提供了丰富的练习材料。