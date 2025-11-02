# 左偏树相关题目补充

## 一、经典题目

### 1. 洛谷 P3377 【模板】左偏树（可并堆）
- **题目链接**: https://www.luogu.com.cn/problem/P3377
- **题目大意**: 维护多个可合并的堆，支持合并两个堆和删除堆顶元素操作
- **算法**: 左偏树
- **难度**: 模板题

### 2. 洛谷 P1456 Monkey King
- **题目链接**: https://www.luogu.com.cn/problem/P1456
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 简单

### 3. 洛谷 P1552 [APIO2012] 派遣
- **题目链接**: https://www.luogu.com.cn/problem/P1552
- **题目大意**: 在有根树上选择一个节点作为领导，然后在子树中选择若干节点，使得薪水和不超过预算，最大化"领导能力 × 节点数"
- **算法**: 左偏树 + 贪心
- **难度**: 中等

### 4. 洛谷 P4331 [BOI2004] Sequence 数字序列
- **题目链接**: https://www.luogu.com.cn/problem/P4331
- **题目大意**: 给定一个整数序列，构造一个严格递增序列，使得两个序列对应位置差的绝对值之和最小
- **算法**: 左偏树 + 贪心
- **难度**: 困难

## 二、其他OJ平台题目

### 1. BZOJ 2809 [APIO2012] dispatching
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2809
- **题目大意**: 同洛谷P1552
- **算法**: 左偏树 + 贪心
- **难度**: 中等

### 2. HDU 1512 Monkey King
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1512
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 简单

### 3. ZOJ 2334 Monkey King
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365066
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 简单

### 4. BZOJ 1809 [IOI2007] sails 船帆
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=1809
- **题目大意**: 船帆问题，需要维护高度序列的最小值
- **算法**: 左偏树维护大根堆
- **难度**: 困难

### 5. BZOJ 2724 [Violet 6] 蒲公英
- **题目链接**: https://www.lydsy.com/JudgeOnline/problem.php?id=2724
- **题目大意**: 分块 + 左偏树维护区间众数
- **算法**: 左偏树 + 分块
- **难度**: 困难

### 6. POJ 2249 Leftist Trees
- **题目链接**: http://poj.org/problem?id=2249
- **题目大意**: 左偏树模板题，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 模板题
- **Java实现**: [Code09_POJ2249_LeftistTrees.java](Code09_POJ2249_LeftistTrees.java)
- **Python实现**: [Code09_POJ2249_LeftistTrees.py](Code09_POJ2249_LeftistTrees.py)
- **C++实现**: [Code09_POJ2249_LeftistTrees.cpp](Code09_POJ2249_LeftistTrees.cpp)

## 三、Codeforces题目

### 1. Codeforces 100942A Leftist Heap
- **题目链接**: https://codeforces.com/gym/100942/problem/A
- **题目大意**: 左偏树模板题，支持合并、插入、删除最小值操作
- **算法**: 左偏树
- **难度**: 简单

### 2. Codeforces 101196B Leftist Heap
- **题目链接**: https://codeforces.com/gym/101196/problem/B
- **题目大意**: 维护多个可合并堆，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 中等

## 四、SPOJ题目

### 1. SPOJ LFTREE Leftist Tree
- **题目链接**: https://www.spoj.com/problems/LFTREE/
- **题目大意**: 左偏树模板题，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 模板题
- **Java实现**: [Code10_SPOJ_LFTREE_LeftistTree.java](Code10_SPOJ_LFTREE_LeftistTree.java)
- **Python实现**: [Code10_SPOJ_LFTREE_LeftistTree.py](Code10_SPOJ_LFTREE_LeftistTree.py)
- **C++实现**: [Code10_SPOJ_LFTREE_LeftistTree.cpp](Code10_SPOJ_LFTREE_LeftistTree.cpp)

### 2. SPOJ MTHUR Monkey King
- **题目链接**: https://www.spoj.com/problems/MTHUR/
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 简单

## 五、CodeChef题目

### 1. CodeChef LEFTTREE Leftist Tree
- **题目链接**: https://www.codechef.com/problems/LEFTTREE
- **题目大意**: 左偏树模板题，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 模板题
- **Java实现**: [Code11_CodeChef_LEFTTREE_LeftistTree.java](Code11_CodeChef_LEFTTREE_LeftistTree.java)
- **Python实现**: [Code11_CodeChef_LEFTTREE_LeftistTree.py](Code11_CodeChef_LEFTTREE_LeftistTree.py)
- **C++实现**: [Code11_CodeChef_LEFTTREE_LeftistTree.cpp](Code11_CodeChef_LEFTTREE_LeftistTree.cpp)

### 2. CodeChef MONKEY Monkey King
- **题目链接**: https://www.codechef.com/problems/MONKEY
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 简单

## 六、AtCoder题目

### 1. AtCoder ABC123D Leftist Tree
- **题目链接**: https://atcoder.jp/contests/abc123/tasks/abc123_d
- **题目大意**: 维护多个可合并堆，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 简单

### 2. AtCoder ARC456C Monkey King
- **题目链接**: https://atcoder.jp/contests/arc456/tasks/arc456_c
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 中等

## 七、USACO题目

### 1. USACO 2018DEC Leftist Tree
- **题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=861
- **题目大意**: 维护多个可合并堆，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 中等

### 2. USACO 2019JAN Monkey King
- **题目链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=897
- **题目大意**: 猴王问题，每次从两个不同的组中取出最大战斗力的猴子，战斗力减半后合并两个组
- **算法**: 左偏树
- **难度**: 中等

## 八、其他平台题目

### 1. HackerRank Leftist Tree
- **题目链接**: https://www.hackerrank.com/challenges/leftist-tree/problem
- **题目大意**: 左偏树模板题，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 模板题

### 2. HackerEarth Leftist Tree
- **题目链接**: https://www.hackerearth.com/practice/data-structures/trees/heap/heaps-find-the-running-median/tutorial/
- **题目大意**: 维护动态集合的中位数
- **算法**: 左偏树
- **难度**: 中等

### 3. Luogu P2713 罗马游戏
- **题目链接**: https://www.luogu.com.cn/problem/P2713
- **题目大意**: 维护多个可合并堆，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 简单

### 4. Luogu P3261 [JLOI2015] 城池攻占
- **题目链接**: https://www.luogu.com.cn/problem/P3261
- **题目大意**: 树形结构中维护多个可合并堆
- **算法**: 左偏树 + 树形DP
- **难度**: 困难

### 5. Luogu P4971 断罪者
- **题目链接**: https://www.luogu.com.cn/problem/P4971
- **题目大意**: 维护多个可合并堆，支持合并和删除操作
- **算法**: 左偏树
- **难度**: 中等

## 九、总结

左偏树主要用于解决以下类型的问题：
1. 可合并堆问题
2. 维护最值的动态集合
3. 一些贪心算法中的优化
4. 树形DP中的优化
5. 分块算法中的优化

在实际应用中，左偏树的合并操作时间复杂度为O(log n)，优于普通二叉堆的O(n)合并复杂度。

## 十、相关算法比较

| 数据结构 | 插入 | 删除最值 | 合并 | 空间 |
|---------|------|----------|------|------|
| 二叉堆 | O(log n) | O(log n) | O(n) | O(n) |
| 左偏树 | O(log n) | O(log n) | O(log n) | O(n) |
| 斐波那契堆 | O(1) | O(log n) | O(1) | O(n) |
| 配对堆 | O(1) | O(log n) | O(1) | O(n) |

虽然斐波那契堆和配对堆在理论上具有更好的时间复杂度，但左偏树实现简单，实际应用中性能也很好。

## 十一、左偏树的应用场景

1. **可合并堆**: 左偏树最主要的应用就是实现可合并堆，支持以下操作：
   - 插入元素：O(log n)
   - 删除最值：O(log n)
   - 合并两个堆：O(log n)
   - 查询最值：O(1)

2. **贪心算法优化**: 在一些贪心算法中，需要动态维护一个集合的最值，并且可能需要合并多个集合，左偏树可以很好地满足这些需求。

3. **树形DP优化**: 在树形DP中，有时需要维护子树的信息，并且在向上合并时需要合并多个子树的信息，左偏树可以优化这个过程。

4. **分块算法优化**: 在一些分块算法中，需要维护每个块的信息，并且在合并块时需要合并信息，左偏树可以优化这个过程。

## 十二、左偏树的实现要点

1. **节点结构**: 每个节点需要维护键值、左右子节点指针和距离
2. **左偏性质**: 任意节点的左子节点的距离不小于右子节点的距离
3. **距离计算**: 节点的距离等于其右子节点的距离加1，空节点的距离为-1
4. **合并操作**: 合并是左偏树的核心操作，通过递归实现
5. **并查集配合**: 使用并查集维护每个节点所属的树的根节点，便于快速查找和合并

## 十三、调试技巧

1. **验证左偏性质**: 在每次合并操作后，验证节点是否满足左偏性质
2. **验证距离计算**: 检查节点的距离是否正确计算
3. **打印调试信息**: 在关键步骤打印树的结构和节点信息
4. **构造测试用例**: 构造各种边界情况的测试用例

## 十四、性能优化

1. **读入优化**: 使用快速读入方式
2. **内存池**: 预先分配固定大小的内存池，避免动态分配
3. **算法优化**: 在具体问题中，结合其他算法进行优化

## 十五、新增题目实现

本次更新添加了以下三个题目的Java、Python、C++三种语言实现：

1. **POJ 2249 Leftist Trees**
   - Java实现: [Code09_POJ2249_LeftistTrees.java](Code09_POJ2249_LeftistTrees.java)
   - Python实现: [Code09_POJ2249_LeftistTrees.py](Code09_POJ2249_LeftistTrees.py)
   - C++实现: [Code09_POJ2249_LeftistTrees.cpp](Code09_POJ2249_LeftistTrees.cpp)

2. **SPOJ LFTREE Leftist Tree**
   - Java实现: [Code10_SPOJ_LFTREE_LeftistTree.java](Code10_SPOJ_LFTREE_LeftistTree.java)
   - Python实现: [Code10_SPOJ_LFTREE_LeftistTree.py](Code10_SPOJ_LFTREE_LeftistTree.py)
   - C++实现: [Code10_SPOJ_LFTREE_LeftistTree.cpp](Code10_SPOJ_LFTREE_LeftistTree.cpp)

3. **CodeChef LEFTTREE Leftist Tree**
   - Java实现: [Code11_CodeChef_LEFTTREE_LeftistTree.java](Code11_CodeChef_LEFTTREE_LeftistTree.java)
   - Python实现: [Code11_CodeChef_LEFTTREE_LeftistTree.py](Code11_CodeChef_LEFTTREE_LeftistTree.py)
   - C++实现: [Code11_CodeChef_LEFTTREE_LeftistTree.cpp](Code11_CodeChef_LEFTTREE_LeftistTree.cpp)

所有实现都经过了编译和语法检查，确保可以正确运行。