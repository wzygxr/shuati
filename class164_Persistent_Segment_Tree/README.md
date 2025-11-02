# 可持久化线段树（主席树）详解

## 1. 概述

可持久化线段树（Persistent Segment Tree），也被称为主席树，是一种可以保存历史版本的数据结构。它通过函数式编程的思想，在每次修改时只创建新节点，共享未修改的部分，从而实现对历史版本的访问。

## 2. 核心思想

1. **函数式编程思想**：每次修改时只创建新节点，共享未修改部分
2. **前缀和思想**：利用前缀和的差值来计算区间信息
3. **离散化处理**：对大数据范围进行离散化以节省空间

## 3. 主要应用场景

1. **静态区间第K小**：给定一个序列，多次查询区间[l,r]内第k小的数
2. **带历史版本的区间查询**：支持查询历史版本的区间信息
3. **树上路径第K小**：在树上查询两点间路径上第k小的点权
4. **离线处理区间问题**：结合离线处理解决复杂的区间查询问题
5. **动态区间第K小**：支持修改操作的区间第K小查询
6. **区间不同元素个数**：查询区间内有多少个不同的元素

## 4. 经典题目

### 4.1 洛谷 P3834 【模板】可持久化线段树 2
- **题目描述**：静态区间第K小
- **解法**：主席树模板题
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](P3834_PersistentSegmentTree.java) | [Python](P3834_PersistentSegmentTree.py) | [C++](P3834_PersistentSegmentTree.cpp)

### 4.2 SPOJ MKTHNUM - K-th Number
- **题目描述**：静态区间第K小
- **解法**：主席树模板题
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](SPOJ_MKTHNUM.java) | [Python](SPOJ_MKTHNUM.py) | [C++](SPOJ_MKTHNUM.cpp)

### 4.3 SPOJ COT - Count on a tree
- **题目描述**：树上路径第K小
- **解法**：树上主席树 + LCA
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Code07_COT.java) | [Python](Code07_COT.py) | [C++](Code07_COT.cpp)

### 4.4 SPOJ KQUERY - K-query
- **题目描述**：离线处理区间大于K的数的个数
- **解法**：主席树 + 离线处理
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](SPOJ_KQUERY.java) | [Python](SPOJ_KQUERY.py) | [C++](SPOJ_KQUERY.cpp)

### 4.5 Luogu P2617 Dynamic Rankings
- **题目描述**：动态区间第K小
- **解法**：树状数组套主席树
- **时间复杂度**：O(n log^2 n + m log^2 n)
- **空间复杂度**：O(n log^2 n)
- **实现**：[Java](Code08_DynamicRankings.java) | [Python](Code08_DynamicRankings.py) | [C++](Code08_DynamicRankings.cpp)

### 4.6 SPOJ DQUERY - D-query
- **题目描述**：区间不同元素个数
- **解法**：主席树 + 离散化
- **时间复杂度**：O((n + q) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Code06_DQUERY.java) | [Python](Code06_DQUERY.py) | [C++](Code06_DQUERY.cpp)

### 4.7 SPOJ TTM - To the moon
- **题目描述**：区间加法操作的历史版本
- **解法**：可持久化线段树 + 懒惰标记
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Code03_RangePersistentClassic1.java) | [Python](Code03_RangePersistentClassic1.py) | [C++](Code03_RangePersistentClassic1.cpp)

### 4.8 LightOJ 1188 - Fast Queries
- **题目描述**：区间不同元素个数
- **解法**：主席树 + 离散化
- **时间复杂度**：O((n + q) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](LightOJ_1188.java) | [Python](LightOJ_1188.py) | [C++](LightOJ_1188.cpp)

### 4.9 Codeforces 813E - Army Creation
- **题目描述**：带限制的区间元素选择
- **解法**：主席树 + 贪心
- **时间复杂度**：O((n + q) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Codeforces_813E_ArmyCreation.java) | [Python](Codeforces_813E_ArmyCreation.py) | [C++](Codeforces_813E_ArmyCreation.cpp)

### 4.10 Codeforces 707D - Persistent Bookcase
- **题目描述**：持久化书架
- **解法**：可持久化数据结构
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Codeforces_707D.java) | [Python](Codeforces_707D.py) | [C++](Codeforces_707D.cpp)

### 4.11 Codeforces 762E - Radio stations
- **题目描述**：区间频率查询
- **解法**：主席树 + 扫描线
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)

### 4.21 洛谷 P3372 - 标记永久化
- **题目链接**：https://www.luogu.com.cn/problem/P3372
- **题目描述**：区间加法和区间求和
- **解法**：标记永久化的线段树
- **时间复杂度**：O(m log n)
- **空间复杂度**：O(n)
- **实现**：[Java](Code04_TagPermanentization1.java) | [Python](Code04_TagPermanentization1.py) | [C++](Code04_TagPermanentization1.cpp)

### 4.12 LeetCode 230 - 二叉搜索树中第K小的元素
- **题目链接**：https://leetcode.com/problems/kth-smallest-element-in-a-bst/
- **题目描述**：给定一个二叉搜索树，查找其中第k小的元素
- **解法**：可以使用中序遍历，也可以使用主席树的思想维护每个子树的节点数
- **时间复杂度**：O(n) 或 O(log n)
- **空间复杂度**：O(n)

### 4.13 洛谷 P4587 - FJOI2016 神秘数
- **题目链接**：https://www.luogu.com.cn/problem/P4587
- **题目描述**：区间神秘数查询
- **解法**：主席树 + 贪心
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)

### 4.14 HDU 4348 - To the moon
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4348
- **题目描述**：区间加法操作的历史版本查询
- **解法**：可持久化线段树 + 懒惰标记
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Code03_RangePersistentClassic1.java) | [Python](Code03_RangePersistentClassic1.py) | [C++](Code03_RangePersistentClassic1.cpp)

### 4.15 牛客网 NC205216 - 区间第K大
- **题目链接**：https://ac.nowcoder.com/acm/problem/205216
- **题目描述**：静态区间第K大
- **解法**：主席树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)

### 4.16 BZOJ 3123 - [SDOI2013]森林
- **题目链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=3123
- **题目描述**：动态森林上的路径第K小
- **解法**：并查集 + 主席树 + LCA
- **时间复杂度**：O(n log^2 n)
- **空间复杂度**：O(n log n)

### 4.17 POJ 2104 - K-th Number
- **题目链接**：http://poj.org/problem?id=2104
- **题目描述**：静态区间第K小
- **解法**：主席树或划分树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](POJ_2104.java) | [Python](POJ_2104.py) | [C++](POJ_2104.cpp)

### 4.18 CodeChef - MAXMEDIAN
- **题目链接**：https://www.codechef.com/problems/MAXMEDIAN
- **题目描述**：最大化区间中位数
- **解法**：二分答案 + 主席树
- **时间复杂度**：O(n log^2 n)
- **空间复杂度**：O(n log n)

### 4.19 HackerRank - Median Updates
- **题目链接**：https://www.hackerrank.com/challenges/median
- **题目描述**：动态维护中位数
- **解法**：主席树或平衡树
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)

### 4.20 AtCoder ARC033 C - データ構造
- **题目链接**：https://atcoder.jp/contests/arc033/tasks/arc033_3
- **题目描述**：可持久化数组
- **解法**：可持久化线段树
- **时间复杂度**：O(log n) per query
- **空间复杂度**：O(n log n)
- **实现**：[Java](Code01_PointPersistent1.java) | [Python](Code01_PointPersistent1.py) | [C++](Code01_PointPersistent1.cpp)

### 4.22 BZOJ 3932 [CQOI2015]任务查询系统
- **题目链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=3932
- **题目描述**：任务管理系统中的优先级查询
- **解法**：可持久化线段树 + 差分
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](BZOJ3932_TaskQuerySystem.java) | [Python](BZOJ3932_TaskQuerySystem.py) | [C++](BZOJ3932_TaskQuerySystem.cpp)

### 4.23 LOJ 6280 数列分块入门4
- **题目链接**：https://loj.ac/p/6280
- **题目描述**：带历史版本的区间加法和区间求和
- **解法**：可持久化线段树 + 懒惰标记
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](LOJ6280_BlockArray4.java) | [Python](LOJ6280_BlockArray4.py) | [C++](LOJ6280_BlockArray4.cpp)

### 4.24 POJ 2761 Feed the dogs
- **题目链接**：http://poj.org/problem?id=2761
- **题目描述**：区间第K小（特殊约束）
- **解法**：主席树（区间第K小）
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](POJ2761_FeedTheDogs.java) | [Python](POJ2761_FeedTheDogs.py) | [C++](POJ2761_FeedTheDogs.cpp)

### 4.25 SPOJ COT2 - Count on a tree II
- **题目链接**：https://www.spoj.com/problems/COT2/
- **题目描述**：树上路径不同元素个数
- **解法**：树上莫队算法
- **时间复杂度**：O((n + m) * sqrt(n))
- **空间复杂度**：O(n)
- **实现**：[Java](SPOJ_COT2_CountOnTreeII.java) | [Python](SPOJ_COT2_CountOnTreeII.py) | [C++](SPOJ_COT2_CountOnTreeII.cpp)

### 4.26 HDU 4417 Super Mario
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4417
- **题目描述**：区间小于等于H的元素个数
- **解法**：主席树（区间查询小于等于某值的元素个数）
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](HDU4417_SuperMario.java) | [Python](HDU4417_SuperMario.py) | [C++](HDU4417_SuperMario.cpp)

### 4.27 Codeforces 813E - Army Creation
- **题目链接**：https://codeforces.com/problemset/problem/813/E
- **题目描述**：带限制的区间元素选择（每种类型最多选k个）
- **解法**：主席树 + 预处理
- **时间复杂度**：O((n + q) log n)
- **空间复杂度**：O(n log n)
- **实现**：[Java](Codeforces_813E_ArmyCreation.java) | [Python](Codeforces_813E_ArmyCreation.py) | [C++](Codeforces_813E_ArmyCreation.cpp)

## 5. 算法实现要点

### 5.1 建树过程
```java
// 构建空线段树
static int build(int l, int r) {
    int rt = ++cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}
```

### 5.2 插入操作
```java
// 在线段树中插入一个值
static int insert(int pos, int l, int r, int pre) {
    int rt = ++cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + 1;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt]);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt]);
        }
    }
    return rt;
}
```

### 5.3 查询操作
```java
// 查询区间第k小的数
static int query(int k, int l, int r, int u, int v) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[v]] - sum[left[u]];
    if (x >= k) {
        // 第k小在左子树中
        return query(k, l, mid, left[u], left[v]);
    } else {
        // 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v]);
    }
}
```

## 6. 复杂度分析

- **时间复杂度**：
  - 建树：O(n log n) - 构建初始线段树
  - 插入：O(log n) - 每次插入只需要创建O(log n)个新节点
  - 查询：O(log n) - 每次查询需要遍历O(log n)层节点
- **空间复杂度**：O(n log n) - n次插入操作，每次插入创建O(log n)个节点

## 7. 工程化考量

1. **内存优化**：只在需要时创建新节点，共享未修改部分，避免不必要的内存消耗
2. **离散化处理**：对大数据范围进行离散化以节省空间，特别是当数值范围很大时
3. **边界处理**：注意数组下标和边界条件，避免数组越界错误
4. **异常处理**：处理非法输入和查询，如k值超过区间长度等
5. **性能优化**：
   - 使用快速IO以应对大数据量
   - 避免递归过深导致栈溢出
   - 合理设置数组大小以避免内存超限
6. **线程安全**：主席树本质上是函数式的数据结构，适合多线程环境下的只读操作

## 8. 优缺点分析

### 8.1 优点
1. 可以访问历史版本，支持回滚操作
2. 空间效率较高（相比存储所有版本），共享不变的节点
3. 查询效率高，单次查询时间复杂度为O(log n)
4. 支持多种区间查询操作，功能强大

### 8.2 缺点
1. 实现较为复杂，理解和编码难度较大
2. 常数较大，实际运行效率可能不如一些针对性算法
3. 空间占用仍然较大，特别是当数据规模大或操作次数多时
4. 不适合频繁修改的场景，因为每次修改都会创建新节点

## 9. 扩展应用

1. **树上主席树**：结合LCA处理树上路径问题，如树上第K小
2. **二维主席树**：处理二维平面上的问题，如二维区间第K小
3. **动态主席树**：结合其他数据结构（如树状数组）支持动态修改
4. **整体二分**：结合整体二分处理复杂问题，如K大查询的多种变体
5. **可持久化并查集**：结合可持久化思想的并查集结构
6. **可持久化平衡树**：支持历史版本的平衡树结构

## 10. 与机器学习/深度学习的联系

1. **特征选择**：在特征选择过程中，可以利用主席树高效地维护和查询特征的统计信息
2. **数据流分析**：处理大规模数据流时，可以利用可持久化结构保存历史状态
3. **时空数据处理**：处理时空数据时，主席树可以保存不同时间点的数据状态
4. **模型压缩**：在模型训练过程中，保存不同训练阶段的模型状态，支持模型回滚

## 11. 优化技巧与实战经验

### 11.1 代码实现优化
1. **使用非递归实现**：对于大数据量，可以使用非递归方式实现以避免栈溢出
2. **内存池管理**：预分配内存池以提高节点创建效率
3. **位运算优化**：使用位运算替代除法和乘法操作
4. **离散化优化**：使用更高效的离散化方法，如排序去重后用二分查找映射

### 11.2 常见错误与调试技巧
1. **数组越界**：确保数组大小足够，一般为40*MAXN或更大
2. **离散化错误**：仔细检查离散化过程，确保所有可能出现的值都被正确映射
3. **递归栈溢出**：对于深度较大的递归，考虑使用非递归实现或增加栈空间
4. **内存超限**：优化空间使用，避免不必要的节点创建

### 11.3 边界情况处理
1. **空输入**：处理n=0或m=0的情况
2. **极端值**：处理数值范围特别大或特别小的情况
3. **重复数据**：确保离散化过程正确处理重复数据
4. **特殊格式**：处理非标准输入格式，如空格分隔、换行符等

## 12. 总结

可持久化线段树（主席树）是一种强大的数据结构，特别适用于需要访问历史版本或处理静态区间查询的场景。掌握其核心思想和实现方法对于解决相关问题非常有帮助。通过大量的练习和实践，才能真正掌握这一数据结构的精髓，并在实际问题中灵活应用。