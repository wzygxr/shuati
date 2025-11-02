# 树形动态规划与换根DP专题

## 简介

本目录包含了树形动态规划（Tree DP）和换根DP（Re-rooting DP）相关的经典题目和解法。换根DP是树形DP的一个重要分支，用于解决需要计算以每个节点为根时某些属性的问题。

## 题目列表

### 基础题目

1. **最大深度和问题** - POJ 3478 [POI2008] STA-Station
   - 文件: Code01_MaximizeSumOfDeeps1.java, Code01_MaximizeSumOfDeeps1.py等
   - 目标: 找到一个节点作为根，使得所有节点到根的深度之和最大

2. **树上染色问题** - CF 1187E Tree Painting
   - 文件: Code02_TreePainting.java, Code02_TreePainting.py
   - 目标: 选择起始节点使得染色过程中的总收益最大

3. **有向树根选择问题** - CF 219D Choosing Capital for Treeland
   - 文件: Code03_ChooseCapital.java, Code03_ChooseCapital.py
   - 目标: 选择根节点使得需要翻转的边数最少

4. **流量最大化问题** - POJ 3585 Accumulation Degree
   - 文件: Code04_MaximizeFlow1.java, Code04_MaximizeFlow1.py等
   - 目标: 选择根节点使得流向叶子节点的流量最大

5. **距离K以内权值和问题** - USACO 2012 FEB Nearby Cows
   - 文件: Code05_SumOfNearby1.java, Code05_SumOfNearby1.py等
   - 目标: 计算每个节点距离K以内的权值和

6. **重心判定问题** - CF 708C Centroids
   - 文件: Code06_Centroids.java, Code06_Centroids.py
   - 目标: 判断每个节点是否能通过调整一条边成为重心

7. **聚会接送问题** - COCI 2015 Kamp
   - 文件: Code07_Kamp.java, Code07_Kamp.py
   - 目标: 计算以每个节点为聚会点时接送所有乘客的最短时间

### 补充题目

13. **树的直径问题** - LeetCode 543. Diameter of Binary Tree
    - 目标: 计算树中任意两个节点之间的最长路径长度

14. **子树中的最大平均值** - LeetCode 1120. Maximum Average Subtree
    - 目标: 找到子树平均值最大的子树

15. **打家劫舍III** - LeetCode 337. House Robber III
    - 目标: 在二叉树上选择不相邻的节点，使得总金额最大

16. **树中的最长路径** - Codeforces 1083E The Fair Nut and Rectangles
    - 目标: 找到树中最长的路径，满足特定条件

17. **最小权覆盖树问题** - HDU 4918 Query on the subtree
    - 目标: 计算子树的最小权覆盖

18. **树上的最长回文路径** - Codeforces 1304F2 Animal Observation (hard version)
    - 目标: 找到树上的最长回文路径

19. **森林中的最长路径** - POJ 1985 Cow Marathon
    - 目标: 计算森林中最长的路径长度

20. **带权树的最大路径和** - LeetCode 124. Binary Tree Maximum Path Sum
    - 目标: 计算二叉树中的最大路径和

21. **树上的最大独立集** - HDU 1520 Anniversary party
    - 目标: 选择最大的节点集合，使得没有两个节点直接相连

22. **树的中心问题** - HDU 2196 Computer
    - 目标: 找到树的中心节点，使得该节点到所有其他节点的最远距离最小

23. **树的分治问题** - POJ 1741 Tree
    - 目标: 统计树中距离不超过k的点对数目

24. **多叉树转二叉树** - ZOJ 3822 Domination
    - 目标: 将多叉树转换为二叉树并计算相关属性

25. **树的最小支配集** - HDU 3338 Kakuro Extension
    - 目标: 找到树的最小支配集

26. **树的最小点覆盖** - HDU 2819 Swap
    - 目标: 找到树的最小点覆盖

27. **树的最大团** - HDU 3333 Turing Tree
    - 目标: 找到树的最大团

28. **树的最大匹配** - HDU 3341 Lost's revenge
    - 目标: 找到树的最大匹配

29. **树的同构问题** - HDU 2815 Mod Tree
    - 目标: 判断两棵树是否同构

30. **树的最近公共祖先** - HDU 2586 How far away?
    - 目标: 计算两个节点的最近公共祖先

### 新增题目

8. **树根猜测问题** - LeetCode 2581 Count Possible Roots
   - 文件: Code08_CountPossibleRoots.java, Code08_CountPossibleRoots.py
   - 目标: 统计有多少个节点可以作为根，使得至少有k个猜测是正确的

9. **最小高度树问题** - LeetCode 310 Minimum Height Trees
   - 文件: Code09_MinimumHeightTrees.java, Code09_MinimumHeightTrees.py
   - 目标: 找到树的中心节点，这些节点作为根时树的高度最小

10. **边反转问题** - LeetCode 2858 Minimum Edge Reversals
    - 文件: Code10_MinEdgeReversals.java, Code10_MinEdgeReversals.py
    - 目标: 计算每个节点作为根时需要翻转的最少边数

11. **附近奶牛问题** - USACO 2012 FEB Nearby Cows (补充实现)
    - 文件: Code11_NearbyCows.java, Code11_NearbyCows.py
    - 目标: 计算每个节点距离K以内的权值和

12. **树染色问题** - Codeforces 1187E Tree Painting (补充实现)
    - 文件: Code12_TreePainting.java, Code12_TreePainting.py
    - 目标: 选择起始节点使得染色过程中的总收益最大

### 扩展题目（新增）

19. **二叉树中的最大路径和** - LeetCode 124
    - 文件: Code19_MaximumPathSum.java, Code19_MaximumPathSum.cpp, Code19_MaximumPathSum.py
    - 目标: 计算二叉树中的最大路径和，路径可以从任意节点开始到任意节点结束
    - 网址: https://leetcode.com/problems/binary-tree-maximum-path-sum/

20. **树的直径问题** - LeetCode 543 / HDU 2196
    - 文件: Code20_TreeDiameter.java
    - 目标: 计算树中最长的路径（直径），支持二叉树和通用树
    - 网址: https://leetcode.com/problems/diameter-of-binary-tree/

21. **打家劫舍III** - LeetCode 337
    - 文件: Code21_HouseRobberIII.java
    - 目标: 在二叉树上选择不相邻的节点，使得总金额最大
    - 网址: https://leetcode.com/problems/house-rober-iii/

22. **子树中的最大平均值** - LeetCode 1120
    - 文件: Code22_MaximumAverageSubtree.java
    - 目标: 找到平均值最大的子树
    - 网址: https://leetcode.com/problems/maximum-average-subtree/

23. **树中的最长交错路径** - LeetCode 1372
    - 文件: Code23_LongestZigZagPath.java
    - 目标: 找到最长的交错路径（路径中相邻节点交替向左和向右移动）
    - 网址: https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/

24. **二叉树的最近公共祖先** - LeetCode 236
    - 文件: Code24_LowestCommonAncestor.java
    - 目标: 找到两个节点的最近公共祖先
    - 网址: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/

25. **二叉树的序列化与反序列化** - LeetCode 297
    - 文件: Code25_SerializeAndDeserializeBinaryTree.java
    - 目标: 实现二叉树的序列化和反序列化
    - 网址: https://leetcode.com/problems/serialize-and-deserialize-binary-tree/

26. **树的同构问题** - HDU 2815 / POJ 1635
    - 文件: Code26_TreeIsomorphism.java
    - 目标: 判断两棵树是否同构（结构相同，节点可以重命名）
    - 网址: http://acm.hdu.edu.cn/showproblem.php?pid=2815

27. **树的最大匹配问题** - HDU 3341 / POJ 1463
    - 文件: Code27_TreeMatching.java
    - 目标: 在树中找到最大的边集合，使得没有两条边共享同一个顶点
    - 网址: http://acm.hdu.edu.cn/showproblem.php?pid=3341

## 算法要点

### 换根DP核心思想

换根DP通过两次DFS遍历来解决问题：
1. 第一次DFS：以某个固定节点为根，计算初始状态
2. 第二次DFS：通过换根技术，计算其他节点为根时的状态

### 适用场景

换根DP适用于以下场景：
1. 需要计算以每个节点为根时的某种属性值
2. 树的形态固定，但根节点可以变化
3. 相邻节点之间的状态可以快速转换

## 复杂度分析

### 时间复杂度
- 两次DFS遍历：O(n)
- 对于涉及距离K的问题：O(n*K)
- 总体时间复杂度：O(n) 或 O(n*K)

### 空间复杂度
- 存储树结构：O(n)
- DP数组：O(n) 或 O(n*K)
- 总体空间复杂度：O(n) 或 O(n*K)

## 工程化考量

### 异常处理
1. 输入校验：检查节点数、边数是否合法
2. 边界条件：处理n=1等特殊情况
3. 溢出处理：使用long long等大整数类型

### 性能优化
1. 递归深度：Python需要设置递归限制
2. 内存优化：及时释放不需要的中间结果
3. 常数优化：减少重复计算

### 代码质量
1. 命名规范：变量名见名知意
2. 注释完整：详细解释算法思路和关键步骤
3. 模块化：将功能拆分为独立函数

## 学习建议

1. 先掌握基础的树形DP概念
2. 理解换根DP的核心思想和模板
3. 通过练习不同类型的题目加深理解
4. 注意边界条件和特殊情况的处理
5. 关注时间和空间复杂度的优化