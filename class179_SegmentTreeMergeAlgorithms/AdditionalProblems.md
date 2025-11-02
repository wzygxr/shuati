# 线段树合并（Segment Tree Merge）算法题目集合

## 一、主要题目

### 1. BZOJ2733/HNOI2012 永无乡
- **题目来源**: BZOJ/HNOI2012
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2733
- **题目大意**: 给定n个岛屿和每个岛屿的重要度，支持两种操作：将两个岛屿连接起来；询问某个岛屿所在连通块中重要度第k小的岛屿编号。
- **解法**: 使用并查集维护连通性，每个连通块维护一棵权值线段树，合并连通块时合并对应的线段树。
- **实现文件**: 
  - Code12_EverlastingCountry.java
  - Code12_EverlastingCountry.cpp
  - Code12_EverlastingCountry.py

### 2. Codeforces 600E Lomsat gelral
- **题目来源**: Codeforces Round #286 (Div. 1)
- **题目链接**: https://codeforces.com/problemset/problem/600/E
- **题目大意**: 给定一棵树，每个节点有一个颜色，求每个子树中出现次数最多的颜色的颜色值之和。
- **解法**: 使用后序遍历和线段树合并，为每个子树维护一个权值线段树，记录颜色出现次数，合并时更新最大值。
- **实现文件**:
  - Code11_LomsatGelral.java
  - Code11_LomsatGelral.cpp
  - Code11_LomsatGelral.py

### 3. Codeforces 1009F Dominant Indices
- **题目来源**: Codeforces Round #494 (Div. 3)
- **题目链接**: https://codeforces.com/problemset/problem/1009/F
- **题目大意**: 给定一棵树，求每个节点的子树中，出现次数最多的深度（相对于该节点）。
- **解法**: 使用后序遍历和线段树合并，为每个子树维护一个线段树，记录不同深度的节点数目，合并时更新最大值。
- **实现文件**:
  - Code13_DominantIndices.java
  - Code13_DominantIndices.cpp
  - Code13_DominantIndices.py

### 4. BZOJ2212/POI2011 Tree Rotations
- **题目来源**: BZOJ/POI2011
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2212 / https://www.luogu.com.cn/problem/P3521
- **题目大意**: 给定一棵二叉树，每个叶子节点有一个权值。可以交换任意节点的左右子树，求交换后中序遍历的逆序对的最小数量。
- **解法**: 线段树合并，在合并时计算逆序对数。使用后序遍历和线段树合并，为每个子树维护一个权值线段树，计算交换和不交换左右子树时的逆序对数目，选择较小的方案。
- **实现文件**:
  - Code14_TreeRotations.java
  - Code14_TreeRotations.cpp
  - Code14_TreeRotations.py

## 二、补充题目

### 5. HDU 6547 Tree
- **题目来源**: HDU Multi-University Training Contest 2019
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=6547
- **题目大意**: 给定一棵树，支持路径加、子树求和、子树第k大查询等操作。
- **解法**: 使用树链剖分结合线段树合并，维护子树信息。

### 6. Luogu P4556 [Vani有约会]雨天的尾巴
- **题目来源**: 洛谷
- **题目链接**: https://www.luogu.com.cn/problem/P4556
- **题目大意**: 给定一棵树，多次进行路径上添加物品，求每个节点上最多的物品种类。
- **解法**: 使用树上差分和线段树合并，对每条路径进行差分处理，然后在树的遍历过程中合并线段树。

### 7. BZOJ 3514 Codechef MARCH14 GERALD07加强版
- **题目来源**: BZOJ
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=3514
- **题目大意**: 动态维护连通性问题，支持添加边和查询两个点是否连通。
- **解法**: 使用线段树分治和并查集，结合线段树合并处理离线查询。

### 8. Codeforces 786B Legacy
- **题目来源**: Codeforces Round #406 (Div. 1)
- **题目链接**: https://codeforces.com/problemset/problem/786/B
- **题目大意**: 给定一张图，支持点到点、点到区间、区间到点的边添加，求单源最短路径。
- **解法**: 使用线段树优化建图，线段树的每个节点代表一个区间，合并线段树节点构建分层图。

### 9. LOJ #2277. 「HAOI2017」八纵八横
- **题目来源**: LOJ
- **题目链接**: https://loj.ac/problem/2277
- **题目大意**: 给定一个铁路网络，支持区间修改和路径查询。
- **解法**: 使用线段树合并维护区间信息，结合树链剖分处理路径查询。

### 10. POI 2015 Logistyka
- **题目来源**: POI 2015
- **题目链接**: https://szkopul.edu.pl/problemset/problem/hj9oDD908pM7IHB8w3f08e0g/site/
- **题目大意**: 维护一个序列，支持区间加、区间乘，以及查询区间中小于等于某个值的元素个数。
- **解法**: 使用线段树合并和懒标记，维护区间信息。

## 三、线段树合并算法总结

### 核心思想
线段树合并是一种将两棵线段树合并成一棵的高效操作，通常与动态开点线段树结合使用。其基本思路是递归地合并两棵线段树的对应节点，保留两棵树中的所有信息。

### 适用场景
1. **树形统计问题**: 如子树信息合并、深度统计、权值统计等
2. **连通性维护**: 结合并查集维护连通块信息
3. **序列与区间问题**: 如逆序对统计、区间信息合并等
4. **动态数据结构**: 处理动态变化的数据集合

### 时间复杂度
对于n个节点的树，线段树合并的时间复杂度为O(n log n)，因为每个节点最多被合并一次。

### 空间复杂度
使用动态开点线段树时，空间复杂度为O(n log n)，每个元素最多被存储在O(log n)个节点中。

### 实现技巧
1. **动态开点**: 只在需要时创建节点，节省空间
2. **递归合并**: 自底向上合并线段树节点
3. **信息维护**: 根据问题需求，设计合适的节点信息和合并方式
4. **树上应用**: 通常采用后序遍历的方式，先处理子节点，再合并信息

## 四、学习资源

### 参考资料
- 《算法竞赛进阶指南》- 李煜东
- 《数据结构与算法分析》- Mark Allen Weiss
- Codeforces、BZOJ等OJ平台的题解区

### 推荐练习顺序
1. BZOJ2733 永无乡（并查集+线段树合并）
2. Codeforces 600E Lomsat gelral（子树统计）
3. Codeforces 1009F Dominant Indices（深度统计）
4. BZOJ2212 Tree Rotations（逆序对统计）
5. 其他补充题目

通过这些题目，可以全面掌握线段树合并算法的应用技巧，以及在不同场景下的优化方法。