# 线段树合并 (Segment Tree Merge) 算法详解

## 一、算法概述

线段树合并是一种将两棵或多棵线段树合并为一棵的技术。它通常与动态开点线段树结合使用，用于解决树上问题，特别是需要合并子树信息的场景。

### 1.1 基本思想

线段树合并的核心思想是：
1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
3. 合并过程类似于可并堆的合并方式

### 1.2 适用场景

线段树合并通常用于以下场景：
- 树上统计问题：每个节点维护一棵线段树，需要合并子树信息
- 树上差分问题：在树上进行差分操作，通过线段树合并统计答案
- 连通性维护问题：维护多个集合的信息，支持集合合并操作
- 优化某些DP问题：通过线段树合并优化树形DP

## 二、算法原理

### 2.1 合并过程

线段树合并的过程如下：

```
int merge(int x, int y, int l, int r) {
    // 如果其中一个节点为空，返回另一个节点
    if (!x || !y) return x + y;
    
    // 如果是叶子节点，合并节点信息
    if (l == r) {
        // 根据具体问题合并信息
        sum[x] += sum[y];
        return x;
    }
    
    // 递归合并左右子树
    int mid = (l + r) >> 1;
    ls[x] = merge(ls[x], ls[y], l, mid);
    rs[x] = merge(rs[x], rs[y], mid + 1, r);
    
    // 更新当前节点信息
    push_up(x);
    return x;
}
```

### 2.2 时间复杂度分析

线段树合并的时间复杂度为 O(log n)，其中 n 是值域大小。这是因为每次合并操作只会遍历两棵树共有的节点。

更准确地说，如果有 n 棵线段树，总共进行了 m 次单点插入操作，那么所有合并操作的总时间复杂度为 O(m log n)。

### 2.3 空间复杂度分析

线段树合并的空间复杂度取决于动态开点的数量。如果总共进行了 m 次单点插入操作，那么空间复杂度为 O(m log n)。

## 三、经典问题及实现

### 3.1 晋升者计数 (Promotion Counting)

**题目来源**: USACO 2017 January Contest, Platinum Problem 1
**题目链接**: https://www.luogu.com.cn/problem/P3605

**问题描述**：
给定一棵 n 个节点的树，每个节点有一个能力值。对于每个节点，统计其子树中有多少个节点的能力值严格大于该节点的能力值。

**解题思路**：
1. 为每个节点建立一棵权值线段树，维护子树中各能力值的出现次数
2. 从叶子节点开始，自底向上合并子树的线段树
3. 查询当前节点线段树中大于该节点能力值的节点数量

**关键代码**：
```java
// 合并两棵线段树
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        siz[t1] += siz[t2];
    } else {
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}

// 查询大于某个值的数量
public static int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl > jobr || i == 0) {
        return 0;
    }
    if (jobl <= l && r <= jobr) {
        return siz[i];
    }
    int mid = (l + r) >> 1;
    int ret = 0;
    if (jobl <= mid) {
        ret += query(jobl, jobr, l, mid, ls[i]);
    }
    if (jobr > mid) {
        ret += query(jobl, jobr, mid + 1, r, rs[i]);
    }
    return ret;
}
```

### 3.2 雨天的尾巴 (Rainy Day Tail)

**题目来源**: Vani有约会 洛谷P4556
**题目链接**: https://www.luogu.com.cn/problem/P4556

**问题描述**：
给定一棵 n 个节点的树和 m 次操作，每次操作在两点间路径上投放某种类型的物品。要求最后统计每个节点收到最多物品的类型。

**解题思路**：
1. 利用树上差分技术，在路径端点和LCA处打标记
2. 为每个节点建立线段树，维护各类型物品的数量
3. 自底向上合并子树信息，查询最大值对应的类型

**关键代码**：
```java
// 树上差分打标记
int lca = getLca(x, y);
int lcafa = stjump[lca][0];
root[x] = add(food, 1, 1, MAXV, root[x]);
root[y] = add(food, 1, 1, MAXV, root[y]);
root[lca] = add(food, -1, 1, MAXV, root[lca]);
root[lcafa] = add(food, -1, 1, MAXV, root[lcafa]);

// 合并线段树并查询最大值
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        maxCnt[t1] += maxCnt[t2];
    } else {
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}

// 查询最大值对应的类型
public static int query(int l, int r, int i) {
    if (l == r) {
        return l;
    }
    int mid = (l + r) >> 1;
    if (maxCnt[i] == maxCnt[ls[i]]) {
        return query(l, mid, ls[i]);
    } else {
        return query(mid + 1, r, rs[i]);
    }
}
```

### 3.3 天天爱跑步 (Running Everyday)

**题目来源**: NOIP 2016 提高组 D1T2
**题目链接**: https://www.luogu.com.cn/problem/P1600

**问题描述**：
给定一棵 n 个节点的树，每个节点有一个观察员，只观察跑步经过且在特定时间的选手。m 个选手在树上按指定路径跑步，求每个观察员能观察到多少选手。

**解题思路**：
1. 将路径分解为向上的路径和向下的路径
2. 对向上路径用线段树维护深度信息，对向下路径用线段树维护深度差信息
3. 通过线段树合并统计每个节点的答案

**关键代码**：
```java
// 合并线段树
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        sum[t1] += sum[t2];
    } else {
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}

// 查询特定值的数量
public static int query(int jobi, int l, int r, int i) {
    if (jobi < l || jobi > r || i == 0) {
        return 0;
    }
    if (l == r) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    if (jobi <= mid) {
        return query(jobi, l, mid, ls[i]);
    } else {
        return query(jobi, mid + 1, r, rs[i]);
    }
}
```

### 3.4 永无乡 (Neverland)

**题目来源**: HNOI2012
**题目链接**: https://www.luogu.com.cn/problem/P3224

**问题描述**：
有 n 座岛屿，每座岛屿有一个重要度。支持两种操作：1. 连接两座岛屿；2. 查询某个岛屿所在连通块中第 k 重要的岛屿。

**解题思路**：
1. 用并查集维护连通性
2. 为每个连通块维护一棵权值线段树，记录重要度信息
3. 连接岛屿时合并对应的线段树
4. 查询时在线段树上二分查找第 k 小

**关键代码**：
```java
// 合并线段树
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        sum[t1] += sum[t2];
    } else {
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}

// 查询第 k 小
public static int query(int jobk, int l, int r, int i) {
    if (i == 0 || jobk > sum[i]) {
        return -1;
    }
    if (l == r) {
        return pos[l];
    }
    int mid = (l + r) >> 1;
    if (sum[ls[i]] >= jobk) {
        return query(jobk, l, mid, ls[i]);
    } else {
        return query(jobk - sum[ls[i]], mid + 1, r, rs[i]);
    }
}
```

### 3.5 最小化逆序对 (Minimize Inversion)

**题目来源**: POI2011 Tree Rotations
**题目链接**: https://www.luogu.com.cn/problem/P3521

**问题描述**：
给定一棵二叉树，每个叶子节点有一个权值。可以交换任意节点的左右子树，求先序遍历后排列的最小逆序对数。

**解题思路**：
1. 递归构建线段树，叶子节点建立单点线段树
2. 合并左右子树时，计算交换前后的逆序对数
3. 取较小值作为当前节点的答案

**关键代码**：
```java
// 合并线段树并计算逆序对
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        siz[t1] += siz[t2];
    } else {
        // 计算跨越左右子树的逆序对数
        u += (long) siz[rs[t1]] * siz[ls[t2]];  // 不交换的逆序对
        v += (long) siz[ls[t1]] * siz[rs[t2]];  // 交换后的逆序对
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}
```

### 3.6 主导下标 (Dominant Indices)

**题目来源**: Codeforces 1009F
**题目链接**: https://codeforces.com/contest/1009/problem/F

**问题描述**：
给定一棵 n 个节点的树，根节点为1。对于每个节点 u，定义其深度数组为一个无限序列，其中第 d 项表示 u 的子树中深度为 d 的节点数量。求每个节点的深度数组中最大值的下标。如果有多个最大值，输出最小的下标。

**解题思路**：
1. 为每个节点建立一棵深度线段树，维护子树中各深度的节点数量
2. 从叶子节点开始，自底向上合并子树的线段树
3. 查询当前节点线段树中节点数量最多的深度

**关键代码**：
```java
// 合并线段树
public static int merge(int l, int r, int t1, int t2) {
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    if (l == r) {
        maxCnt[t1] += maxCnt[t2];
        maxDep[t1] = l;
    } else {
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1);
    }
    return t1;
}
```

## 四、经典题目汇总

### 4.1 POI2011 Tree Rotations

**题目链接**: https://www.luogu.com.cn/problem/P3521

**题目大意**: 给定一棵二叉树，每个叶子节点有一个权值。可以交换任意节点的左右子树，求先序遍历后排列的最小逆序对数。

**解法**: 线段树合并，在合并时计算交换前后的逆序对数。

### 4.2 USACO17JAN Promotion Counting

**题目链接**: https://www.luogu.com.cn/problem/P3605

**题目大意**: 给定一棵树，每个节点有一个能力值。对于每个节点，统计其子树中有多少个节点的能力值严格大于该节点的能力值。

**解法**: 线段树合并，维护子树中各能力值的出现次数。

### 4.3 Vani有约会 雨天的尾巴

**题目链接**: https://www.luogu.com.cn/problem/P4556

**题目大意**: 树上差分问题，在路径上投放物品，统计每个节点收到最多物品的类型。

**解法**: 树上差分+线段树合并。

### 4.4 天天爱跑步

**题目链接**: https://www.luogu.com.cn/problem/P1600

**题目大意**: 树上路径统计问题，观察员只观察特定时间经过的选手。

**解法**: 线段树合并，分别维护向上和向下的路径信息。

### 4.5 HNOI2012 永无乡

**题目链接**: https://www.luogu.com.cn/problem/P3224

**题目大意**: 维护岛屿连通性，支持查询连通块中第k重要的岛屿。

**解法**: 并查集+线段树合并。

### 4.6 CF1009F Dominant Indices

**题目链接**: https://codeforces.com/problemset/problem/1009/F

**题目大意**: 给定一棵树，对每个节点求子树中哪个深度的节点数量最多。

**解法**: 线段树合并，维护每个节点不同深度的节点数量。

### 4.7 BZOJ2212 Tree Rotations

**题目链接**: https://www.luogu.com.cn/problem/P3521

**题目大意**: 与POI2011 Tree Rotations相同。

**解法**: 线段树合并。

### 4.8 CF1336F Journey

**题目链接**: https://codeforces.com/problemset/problem/1336/F

**题目大意**: 给定一棵树和一些路径，求所有路径交的长度为k的点对数。

**解法**: 虚树+线段树合并。

### 4.9 UOJ #46 玄学

**题目链接**: https://uoj.ac/problem/46

**题目大意**: 维护一个序列，支持区间赋值和单点查询操作。

**解法**: 二进制分组线段树。

### 4.10 HDU 6315 Naive Operations

**题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=6315

**题目大意**: 维护两个数组a和b，支持区间加法和区间查询向下取整a[i]/b[i]的和。

**解法**: 线段树合并维护区间操作。

### 4.11 POJ 3667 Hotel

**题目链接**: http://poj.org/problem?id=3667

**题目大意**: 维护一个序列，支持查询连续空房间和区间占用操作。

**解法**: 线段树区间合并。

### 4.12 CF932F Escape Through Leaf

**题目链接**: https://codeforces.com/problemset/problem/932/F

**题目大意**: 树上DP问题，从每个节点跳到子树中叶子节点的最小花费。

**解法**: 李超树+线段树合并。

### 4.13 CF600E Lomsat gelral

**题目链接**: https://codeforces.com/contest/600/problem/E

**题目大意**: 给定一棵树，每个节点有一种颜色。对每个节点求其子树中出现次数最多的颜色的编号和。

**解法**: 线段树合并，维护每种颜色的出现次数。

### 4.14 CF570D Tree Requests

**题目链接**: https://codeforces.com/contest/570/problem/D

**题目大意**: 给定一棵树，每个节点有一个字符。支持查询某个子树在特定深度的字符能否重排成回文串。

**解法**: 线段树合并，维护每个深度上各字符的出现次数。

## 五、更多练习题目

### 5.1 CF1336F Journey

**题目链接**: https://codeforces.com/problemset/problem/1336/F

**题目大意**: 给定一棵树和一些路径，求所有路径交的长度为k的点对数。

**解法**: 虚树+线段树合并。

### 5.2 UOJ #46 玄学

**题目链接**: https://uoj.ac/problem/46

**题目大意**: 维护一个序列，支持区间赋值和单点查询操作。

**解法**: 二进制分组线段树。

### 5.3 HDU 6315 Naive Operations

**题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=6315

**题目大意**: 维护两个数组a和b，支持区间加法和区间查询向下取整a[i]/b[i]的和。

**解法**: 线段树合并维护区间操作。

### 5.4 POJ 3667 Hotel

**题目链接**: http://poj.org/problem?id=3667

**题目大意**: 维护一个序列，支持查询连续空房间和区间占用操作。

**解法**: 线段树区间合并。

### 5.5 CF932F Escape Through Leaf

**题目链接**: https://codeforces.com/problemset/problem/932/F

**题目大意**: 树上DP问题，从每个节点跳到子树中叶子节点的最小花费。

**解法**: 李超树+线段树合并。

### 5.6 PKUWC2018 Minimax

**题目链接**: https://www.luogu.com.cn/problem/P5298

**题目大意**: 树上概率DP问题，每个节点有一定概率取子节点的最大值或最小值。

**解法**: 线段树合并维护概率生成函数。

### 5.7 CF240F TorCoder

**题目链接**: https://codeforces.com/contest/240/problem/F

**题目大意**: 给定一个字符串，支持区间排序操作和查询。

**解法**: 线段树合并或平衡树。

### 5.8 CF765F Souvenirs

**题目链接**: https://codeforces.com/contest/765/problem/F

**题目大意**: 给定一个序列，多次询问区间内两个不同位置元素差的最小值。

**解法**: 线段树合并或分治。

### 5.9 CF914D Bash and a Tough Math Puzzle

**题目链接**: https://codeforces.com/contest/914/problem/D

**题目大意**: 给定一个数组，支持修改元素和查询区间内能否通过最多修改一个元素使所有元素都是某个数的倍数。

**解法**: 线段树合并或线段树上二分。

### 5.10 CF960F Pathwalks

**题目链接**: https://codeforces.com/contest/960/problem/F

**题目大意**: 给定一个有向图，求最长上升路径（边权严格递增）。

**解法**: 线段树合并维护DP状态。

### 5.11 CF993E Nikita and Order Statistics

**题目链接**: https://codeforces.com/contest/993/problem/E

**题目大意**: 给定一个数组和一个阈值，对每个k计算恰好有k个元素小于阈值的连续子数组个数。

**解法**: FFT或线段树合并。

### 5.12 洛谷P4197 Peaks

**题目链接**: https://www.luogu.com.cn/problem/P4197

**题目大意**: 在线查询区间第k小值。

**解法**: 线段树合并或主席树。

### 5.13 洛谷P4211 [LNOI2014]LCA

**题目链接**: https://www.luogu.com.cn/problem/P4211

**题目大意**: 多次询问区间内每个点到根节点路径的并集大小。

**解法**: 树链剖分或线段树合并。

### 5.14 洛谷P4719 【模板】动态DP

**题目链接**: https://www.luogu.com.cn/problem/P4719

**题目大意**: 动态维护树上最大独立集。

**解法**: 线段树合并或动态DP。

## 六、工程化考量

### 6.1 异常处理

在实际应用中，需要注意以下异常情况：
1. 空指针检查：合并时检查节点是否为空
2. 边界条件：处理值域边界情况
3. 内存管理：合理分配和释放线段树节点

### 6.2 性能优化

1. **垃圾回收**：对于大规模数据，可以实现节点回收机制
2. **标记永久化**：对于区间修改操作，可以使用标记永久化技术
3. **空间优化**：只在需要时创建节点，避免空间浪费

### 6.3 调试技巧

1. **打印中间状态**：在合并过程中打印关键信息
2. **小数据测试**：使用小规模数据手动验证算法正确性
3. **边界测试**：测试各种边界情况，确保算法鲁棒性

## 七、与其他算法的对比

### 7.1 与启发式合并的对比

线段树合并 vs 启发式合并：
- 线段树合并：时间复杂度更稳定，适用于复杂信息维护
- 启发式合并：实现简单，但时间复杂度可能较高

### 7.2 与树链剖分的对比

线段树合并 vs 树链剖分：
- 线段树合并：适用于维护子树信息
- 树链剖分：适用于维护路径信息

## 八、总结

线段树合并是一种强大的数据结构技术，特别适用于以下场景：
1. 需要合并子树信息的树上问题
2. 需要维护复杂信息的集合合并问题
3. 优化某些树形DP问题

掌握线段树合并需要：
1. 理解其基本原理和实现方式
2. 熟悉各种经典问题的解法
3. 掌握工程化实现技巧
4. 能够根据具体问题灵活应用

通过大量练习和实践，可以熟练掌握这一技术，并在算法竞赛和实际工程中发挥重要作用。