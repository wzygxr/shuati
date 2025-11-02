# 线段树合并专题

线段树合并是一种强大的数据结构技术，主要用于解决树上问题，特别是需要高效维护子树信息的场景。通过动态开点和递归合并的方式，可以高效地处理各种复杂的树上信息统计和查询问题。

## 核心思想

线段树合并的核心思想是将两棵线段树合并为一棵，通常用于权值线段树。合并时，对于重合的节点将其值相加，不重合的节点保持原样。这种操作使得我们可以在树上自底向上地合并子树信息，从而高效地维护全局统计信息。

## 复杂度分析

线段树合并的时间复杂度是 O(m log n)，其中 m 是插入次数，n 是值域大小。这是因为每次合并操作的复杂度与两棵线段树的重叠节点数量成正比，而由于线段树的性质，这个数量通常是 O(log n) 级别的。

空间复杂度为 O(n log n)，其中 n 是节点数。使用动态开点技术可以有效减少空间占用，避免不必要的节点创建。

## 常见应用场景

1. **树上问题**：维护子树内的权值信息，如子树内元素统计、子树最大值/最小值查询等
2. **概率DP**：维护每个节点可能出现的值及其概率分布
3. **启发式合并**：将多个子树的信息合并，常用于大规模树上统计
4. **树上差分**：在树上进行路径修改操作，结合线段树合并高效处理子树查询
5. **众数统计**：统计子树中出现次数最多的元素，及其出现次数
6. **李超线段树**：维护直线信息，优化动态规划转移过程
7. **区间众数查询**：通过离线处理和线段树合并解决区间众数问题
8. **树上路径统计**：结合LCA和线段树合并处理路径上的信息统计
9. **动态图问题**：处理动态连接的图结构中的信息维护
10. **可持久化数据结构**：与可持久化技术结合，支持历史版本查询

## 相关题目列表

### 1. P4577 [FJOI2018]领导集团问题
- 题目链接：https://www.luogu.com.cn/problem/P4577
- 类似题目：BZOJ4919 [Lydsy1706月赛]大根堆
- 题目大意：给定一棵树，每个节点有一个权值，要求选出最多的节点，使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值
- 解法：线段树合并 + 树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 2. P5298 [PKUWC2018]Minimax
- 题目链接：https://www.luogu.com.cn/problem/P5298
- 类似题目：P5281 [ZJOI2019] Minimax搜索
- 题目大意：给定一棵二叉树，叶子节点有权值，非叶子节点有权值概率，求根节点权值的期望值
- 解法：线段树合并 + 树形DP + 概率计算
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 3. P4556 [Vani有约会]雨天的尾巴
- 题目链接：https://www.luogu.com.cn/problem/P4556
- 线段树合并模板题
- 题目大意：在树上进行路径加操作，每次给路径上所有节点添加某种类型的救济粮，最后查询每个节点最多的救济粮类型
- 解法：树链剖分 + 线段树合并 + 树上差分
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 4. CF600E Lomsat gelral
- 题目链接：https://codeforces.com/problemset/problem/600/E
- 树上启发式合并/线段树合并经典题
- 题目大意：给定一棵树，每个节点有一种颜色，求每个子树中出现次数最多的颜色的编号之和
- 解法：线段树合并 + 树形DP 或 树上启发式合并
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 5. P3605 [USACO17JAN]Promotion Counting P
- 题目链接：https://www.luogu.com.cn/problem/P3605
- 题目大意：给定一棵树，每个节点有一个权值，对于每个节点，统计其子树中权值大于该节点权值的节点个数
- 解法：线段树合并 + 离散化
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 6. P3521 [POI2011]ROT-Tree Rotations
- 题目链接：https://www.luogu.com.cn/problem/P3521
- 题目大意：给定一棵二叉树，叶子节点有权值，可以交换任意节点的左右子树，求最小逆序对数
- 解法：线段树合并优化树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 7. CF208E Blood Cousins
- 题目链接：https://codeforces.com/problemset/problem/208/E
- 题目大意：给定一棵树，多次询问某个节点的第k代堂兄弟数量
- 解法：线段树合并 + 倍增 + DFS序
- 时间复杂度：O(n log n + q log n)
- 空间复杂度：O(n log n)

### 8. P5384 [Cnoi2019]雪松果树
- 题目链接：https://www.luogu.com.cn/problem/P5384
- 题目大意：树上路径查询问题，需要维护路径信息
- 解法：线段树合并 + DFS序 + 区间更新
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 9. P3899 [湖南集训]更为厉害
- 题目链接：https://www.luogu.com.cn/problem/P3899
- 题目大意：树上DP问题，需要维护子树信息
- 解法：线段树合并 + 树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 10. CF1009F Dominant Indices
- 题目链接：https://codeforces.com/problemset/problem/1009/F
- 题目大意：对于每个节点，求其子树中深度最大的节点的深度
- 解法：线段树合并 + 树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 11. CF570D Tree Requests
- 题目链接：https://codeforces.com/problemset/problem/570/D
- 题目大意：树上字符串查询问题，判断子树中节点字符能否重排成回文串
- 解法：线段树合并 + 位运算 + DFS序
- 时间复杂度：O(n log n + q log n)
- 空间复杂度：O(n log n)

### 12. CF246E Blood Cousins Return
- 题目链接：https://codeforces.com/problemset/problem/246/E
- 题目大意：Blood Cousins的加强版，查询第k代子孙的不同名字数量
- 解法：线段树合并 + DFS序 + Set合并
- 时间复杂度：O(n log n + q log n)
- 空间复杂度：O(n log n)

### 13. CF932F Escape Through Leaf
- 题目链接：https://codeforces.com/problemset/problem/932/F
- 题目大意：树上动态规划问题，使用李超线段树优化DP转移
- 解法：李超线段树合并 + 树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 14. P6773 [NOI2020]命运
- 题目链接：https://www.luogu.com.cn/problem/P6773
- 题目大意：NOI真题，复杂的树上DP，需要维护路径信息
- 解法：线段树合并优化树形DP
- 时间复杂度：O(n log n)
- 空间复杂度：O(n log n)

### 15. P8496 [NOI2022]众数
- 题目链接：https://www.luogu.com.cn/problem/P8496
- 题目大意：NOI真题，维护区间众数，需要高效处理大量查询
- 解法：线段树合并维护序列信息
- 时间复杂度：O(n log n + q log n)
- 空间复杂度：O(n log n)

## 更多推荐题目

### LeetCode (力扣) 相关题目
1. **LeetCode 834. 树中距离之和**
   - 题目链接：https://leetcode.cn/problems/sum-of-distances-in-tree/
   - 题意：给定一棵树，计算每个节点到其他所有节点的距离之和
   - 解法：树形DP + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

2. **LeetCode 1519. 子树中标签相同的节点数**
   - 题目链接：https://leetcode.cn/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
   - 题意：统计每个节点的子树中，与该节点标签相同的节点个数
   - 解法：线段树合并或哈希表合并
   - 时间复杂度：O(n)
   - 空间复杂度：O(n)

3. **LeetCode 2049. 统计最高分的节点数目**
   - 题目链接：https://leetcode.cn/problems/count-nodes-with-the-highest-score/
   - 题意：计算删除某个节点后，剩余各部分的乘积最大值出现的次数
   - 解法：树形DP + 线段树优化
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### LintCode (炼码) 相关题目
1. **LintCode 828. 字模式 II**
   - 题目链接：https://www.lintcode.com/problem/828/
   - 题意：判断字符串是否匹配给定的模式，支持模式到字符串的多对多映射
   - 解法：回溯 + 线段树优化
   - 时间复杂度：O(n^2 log n)
   - 空间复杂度：O(n log n)

### AtCoder 相关题目
1. **AtCoder ABC280E Critical Hit**
   - 题目链接：https://atcoder.jp/contests/abc280/tasks/abc280_e
   - 题意：概率DP问题，计算击败敌人所需的期望攻击次数
   - 解法：概率DP + 线段树优化
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

2. **AtCoder ABC273E Notebook**
   - 题目链接：https://atcoder.jp/contests/abc273/tasks/abc273_e
   - 题意：维护动态集合，支持插入、删除和查询第k大的数
   - 解法：线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### USACO 相关题目
1. **USACO 2020 January Contest, Platinum Problem 2. Wormhole Sort**
   - 题目链接：https://usaco.org/index.php?page=viewproblem2&cpid=993
   - 题意：寻找最大的 wormhole 大小，使得可以对数组进行排序
   - 解法：线段树合并 + 并查集
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

2. **USACO 2021 December Contest, Platinum Problem 3. Paired Up**
   - 题目链接：https://usaco.org/index.php?page=viewproblem2&cpid=1194
   - 题意：将牛配对，使得总价值最大且满足特定条件
   - 解法：树形DP + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### 洛谷 (Luogu) 相关题目
1. **P5024 [NOIP2018 提高组] 保卫王国**
   - 题目链接：https://www.luogu.com.cn/problem/P5024
   - 题意：动态树上DP问题，支持强制选或不选特定节点
   - 解法：树链剖分 + 线段树合并 + 动态DP
   - 时间复杂度：O(n log n + q log n)
   - 空间复杂度：O(n log n)

2. **P4219 [BJOI2014] 大融合**
   - 题目链接：https://www.luogu.com.cn/problem/P4219
   - 题意：动态图连通性问题，支持连边和查询两个节点之间的路径数
   - 解法：LCT + 线段树合并
   - 时间复杂度：O(n log n + q log n)
   - 空间复杂度：O(n log n)

### Codeforces 更多题目
1. **CF521D Shop**
   - 题目链接：https://codeforces.com/problemset/problem/521/D
   - 题意：动态规划问题，选择最优的操作序列
   - 解法：贪心 + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

2. **CF773F Test Data Generation**
   - 题目链接：https://codeforces.com/problemset/problem/773/F
   - 题意：构造满足特定条件的测试数据
   - 解法：线段树合并 + 数学建模
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### HackerRank 相关题目
1. **HackerRank - Tree Pruning**
   - 题目链接：https://www.hackerrank.com/challenges/tree-pruning/problem
   - 题意：剪枝树使得剩余子树的节点值之和最大
   - 解法：树形DP + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### CodeChef 相关题目
1. **CodeChef - CHEFPRAD**
   - 题目链接：https://www.codechef.com/problems/CHEFPRAD
   - 题意：树上路径计数问题
   - 解法：点分治 + 线段树合并
   - 时间复杂度：O(n log^2 n)
   - 空间复杂度：O(n log n)

### 牛客网相关题目
1. **牛客网 - 小A的树**
   - 题目链接：https://ac.nowcoder.com/acm/problem/21674
   - 题意：树上覆盖问题，选择最少的节点覆盖所有边
   - 解法：树形DP + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

### POJ 相关题目
1. **POJ 3764 The xor-longest Path**
   - 题目链接：https://poj.org/problem?id=3764
   - 题意：在树中找一条路径，使得路径上的边权异或和最大
   - 解法：Trie树 + 线段树合并
   - 时间复杂度：O(n log n)
   - 空间复杂度：O(n log n)

## 算法要点

1. **动态开点**：避免空间浪费，只在需要时创建节点
2. **权值线段树**：以值域为下标建立线段树，便于统计离散化后的值
3. **合并策略**：递归合并左右子树，对于重合节点执行特定的合并操作
4. **信息维护**：根据题目要求维护相应的信息（如最大值、最小值、和、众数等）
5. **树上差分**：将路径操作转化为点操作，结合线段树合并高效处理
6. **启发式合并**：优化合并顺序，总是将较小的树合并到较大的树中，减少时间复杂度
7. **LCA配合**：结合最近公共祖先技术处理树上路径问题
8. **离散化**：对于大范围的值进行离散化处理，减少空间占用
9. **懒标记处理**：在需要区间更新时，正确处理懒标记的下放和合并
10. **内存管理**：合理管理动态开点的内存，避免内存泄漏和溢出

## 实现细节

线段树合并的关键在于合并函数的实现：

```cpp
// 线段树合并的核心实现
int merge(int a, int b, int l, int r) {
    // 基础情况：如果其中一棵树为空，直接返回另一棵
    if (!a || !b) return a + b;
    
    // 叶子节点处理：根据题目要求合并信息
    if (l == r) {
        // 例如：合并计数
        tree[a].sum += tree[b].sum;
        return a;
    }
    
    // 递归合并左右子树
    int mid = (l + r) >> 1;
    tree[a].ls = merge(tree[a].ls, tree[b].ls, l, mid);
    tree[a].rs = merge(tree[a].rs, tree[b].rs, mid + 1, r);
    
    // 向上更新当前节点的信息
    push_up(a);
    return a;
}

// 单点更新函数
void update(int &p, int l, int r, int pos, int val) {
    // 动态开点
    if (!p) p = ++segCnt;
    
    // 叶子节点处理
    if (l == r) {
        tree[p].sum += val;
        return;
    }
    
    // 递归更新
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        update(tree[p].ls, l, mid, pos, val);
    } else {
        update(tree[p].rs, mid + 1, r, pos, val);
    }
    
    // 向上更新
    push_up(p);
}

// 信息上传函数
void push_up(int p) {
    // 根据左右子节点的信息更新当前节点
    tree[p].sum = tree[tree[p].ls].sum + tree[tree[p].rs].sum;
    // 其他信息根据题目要求进行合并
}
```

## 工程化考量

1. **内存回收**：线段树合并会产生大量无用节点，在实际工程中需要考虑内存池或垃圾回收机制
2. **常数优化**：使用快读、快写等技术优化IO，对于大数据量输入输出尤为重要
3. **空间优化**：动态开点技术避免空间浪费，使用结构体数组而非指针实现可以提高访问效率
4. **边界处理**：正确处理空节点和叶子节点，确保算法在极端情况下的正确性
5. **异常处理**：对非法输入进行检查和处理，增强程序的健壮性
6. **性能优化**：使用启发式合并策略（小合并到大）优化时间复杂度
7. **多线程优化**：在支持并行的环境中，可以考虑对独立子树的合并操作进行并行处理
8. **缓存优化**：通过合理的数据结构设计提高缓存命中率，减少缓存失效
9. **测试覆盖**：编写全面的单元测试，覆盖各种边界情况和异常输入
10. **代码可读性**：使用清晰的命名和注释，提高代码的可维护性

## 设计本质与核心思路

线段树合并的本质是将两个维护相同信息的线段树合并成一个，通常用于树上问题中将子树信息向上传递。核心思路包括：

1. **信息传递**：通过合并操作将子树信息传递给父节点
2. **动态维护**：动态开点避免不必要的空间浪费
3. **递归处理**：自底向上递归合并左右子树
4. **标记下放**：在需要时正确处理懒标记

## 适用场景与约束条件

线段树合并适用于以下场景：
1. 树上问题需要维护子树信息
2. 需要将多个线段树的信息合并
3. 信息满足可合并性（如求和、求最值等）
4. 需要维护权值信息的统计

约束条件：
1. 需要足够的空间存储线段树节点
2. 信息必须满足结合律
3. 合并操作的时间复杂度需要在可接受范围内

## 与其他算法的对比

线段树合并与以下算法有相似之处但也有区别：
1. **树上启发式合并(DSU on tree)**：两者都可以解决树上信息合并问题，但线段树合并更适用于需要维护复杂信息的场景
2. **主席树**：都使用了可持久化思想，但主席树主要用于历史版本查询，线段树合并用于信息合并
3. **树链剖分+线段树**：都可以解决树上问题，但树链剖分适用于路径查询，线段树合并适用于子树信息维护

## 总结

线段树合并是一种强大的数据结构技术，特别适用于树上问题中需要维护子树信息的场景。通过动态开点和递归合并的方式，可以高效地处理各种复杂的树上信息维护问题。掌握线段树合并不仅需要理解其基本原理，还需要熟练掌握其实现细节和优化技巧。