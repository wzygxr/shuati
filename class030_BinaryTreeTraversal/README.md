# 二叉树遍历算法专题 - Class 036

本专题涵盖二叉树的各种遍历算法和相关题目，包括层序遍历、深度优先遍历及其变种。

## 已包含题目列表

### 基础层序遍历
1. **Code01_LevelOrderTraversal.java** - 二叉树的层序遍历
   - 包含两种实现方法：HashMap记录层级和优化BFS
   - 相关题目：LeetCode 102, 107, 103, 637, 515, 429等

### LeetCode题目
2. **LeetCode100_SameTree.java** - 相同的树
3. **LeetCode101_SymmetricTree.java** - 对称二叉树
4. **LeetCode103_BinaryTreeZigzagLevelOrderTraversal.java** - 二叉树的锯齿形层序遍历
5. **LeetCode104_MaximumDepthOfBinaryTree.java** - 二叉树的最大深度
6. **LeetCode105_ConstructBinaryTreeFromPreorderAndInorderTraversal.java** - 从前序与中序遍历序列构造二叉树
7. **LeetCode107_BinaryTreeLevelOrderTraversalII.java** - 二叉树的层序遍历 II
8. **LeetCode116_PopulatingNextRightPointersInEachNode.java** - 填充每个节点的下一个右侧节点指针（完美二叉树）
9. **LeetCode117_PopulatingNextRightPointersInEachNodeII.java** - 填充每个节点的下一个右侧节点指针 II（任意二叉树）
10. **LeetCode199_BinaryTreeRightSideView.java** - 二叉树的右视图
11. **LeetCode222_CountCompleteTreeNodes.java** - 完全二叉树的节点个数
12. **LeetCode226_InvertBinaryTree.java** - 翻转二叉树
13. **LeetCode297_SerializeAndDeserializeBinaryTree.java** - 二叉树的序列化与反序列化
14. **LeetCode429_NaryTreeLevelOrderTraversal.java** - N叉树的层序遍历
15. **LeetCode513_FindBottomLeftTreeValue.java** - 找树左下角的值
16. **LeetCode515_FindLargestValueInEachTreeRow.java** - 在每个树行中找最大值
17. **LeetCode637_AverageOfLevelsInBinaryTree.java** - 二叉树的层平均值
18. **LeetCode662_MaximumWidthOfBinaryTree.java** - 二叉树最大宽度

### 剑指Offer题目
19. **剑指Offer32_I_从上到下打印二叉树.java** - 从上到下打印二叉树
20. **剑指Offer32_II_从上到下打印二叉树II.java** - 从上到下打印二叉树 II
21. **剑指Offer32_III_从上到下打印二叉树III.java** - 从上到下打印二叉树 III

### 牛客网题目
22. **牛客NC15_求二叉树的层序遍历.java** - 求二叉树的层序遍历

### LintCode题目
23. **LintCode69_二叉树的层次遍历.java** - 二叉树的层次遍历

### HackerRank题目
24. **HackerRank_TreeLevelOrderTraversal.java** - 树层序遍历

### UVA题目
25. **UVA122_TreesOnTheLevel.java** - 层序构建树

## 算法思想总结

### 层序遍历(BFS)核心思想
1. **队列数据结构**: 使用队列存储待访问节点
2. **分层处理**: 记录每层节点数量，确保分层处理
3. **空间复杂度**: O(W)，W为树的最大宽度

### 深度优先遍历(DFS)核心思想
1. **递归或栈**: 使用递归调用栈或显式栈
2. **前中后序**: 不同的访问顺序
3. **空间复杂度**: O(H)，H为树的高度

## 时间复杂度分析
- **所有遍历算法**: O(N)，N为节点数
- **优化技巧**: 利用树的性质减少不必要的遍历

## 空间复杂度分析
- **BFS**: O(W)，W为最大宽度
- **DFS**: O(H)，H为树高度
- **最优情况**: 平衡树O(logN)，最坏情况O(N)

## 工程化考量

### 1. 异常处理
- 空树处理
- 边界条件检查
- 数值溢出防护

### 2. 性能优化
- 选择合适的数据结构
- 避免不必要的对象创建
- 预分配内存空间

### 3. 代码可读性
- 清晰的变量命名
- 适当的注释说明
- 模块化的代码结构

## 学习建议

### 基础掌握
1. 熟练掌握层序遍历和深度优先遍历
2. 理解不同遍历顺序的应用场景
3. 掌握递归和迭代两种实现方式

### 进阶提升
1. 学习利用树的性质进行优化
2. 掌握各种变种问题的解法
3. 理解时空复杂度的权衡

### 实战训练
1. 完成所有题目的三种语言实现
2. 分析不同解法的优缺点
3. 总结解题模式和技巧

## 相关资源
- [LeetCode二叉树专题](https://leetcode.cn/tag/tree/)
- [算法可视化工具](https://visualgo.net/en/bst)
- [二叉树学习路线](https://github.com/youngyangyang04/leetcode-master)

---
*最后更新: 2025-10-28*