# 并查集 (Union-Find) 算法详解与题目实践

## 算法简介

并查集（Union-Find）是一种树型的数据结构，用于处理一些不相交集合（Disjoint Sets）的合并及查询问题。有一个联合-查找算法（union-find algorithm）定义了两个用于此数据结构的操作：

- Find：确定元素属于哪一个子集。它可以被用来确定两个元素是否属于同一子集。
- Union：将两个子集合并成同一个集合。

## 核心思想

并查集主要解决图的动态连通性问题，可以高效地支持以下操作：
1. 合并两个集合
2. 查询元素所属集合
3. 判断两个元素是否属于同一集合

## 优化策略

### 1. 路径压缩
在查找操作时，将路径上的所有节点直接连接到根节点，使树更加扁平化。

### 2. 按秩合并
在合并操作时，将秩小的树合并到秩大的树下，避免树退化成链表。

## 时间复杂度

优化后的并查集操作时间复杂度为 O(α(n))，其中 α 是阿克曼函数的反函数，在实际应用中可视为常数。

## 题目列表

### 1. 牛客网并查集模板题
- **文件**: Code01_UnionFindNowCoder.java
- **平台**: 牛客网
- **链接**: https://www.nowcoder.com/practice/e7ed657974934a30b2010046536a5372
- **特点**: 路径压缩 + 小挂大优化

### 2. 洛谷并查集模板题
- **文件**: Code02_UnionFindLuogu.java
- **平台**: 洛谷
- **链接**: https://www.luogu.com.cn/problem/P3367
- **特点**: 递归路径压缩，简化实现

### 3. LeetCode 情侣牵手
- **文件**: Code03_CouplesHoldingHands.java
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/couples-holding-hands/
- **特点**: 将实际问题抽象为并查集问题

### 4. LeetCode 相似字符串组
- **文件**: Code04_SimilarStringGroups.java
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/similar-string-groups/
- **特点**: 字符串相似性判断 + 并查集

### 5. LeetCode 岛屿数量
- **文件**: Code05_NumberOfIslands.java
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/number-of-islands/
- **特点**: 二维坐标映射到一维 + 并查集

### 6. LeetCode 朋友圈
- **文件**: 
  - Code06_FriendCircles.java (Java版本)
  - Code06_FriendCircles.cpp (C++版本)
  - Code06_FriendCircles.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/friend-circles/
- **特点**: 邻接矩阵表示的图 + 并查集

### 7. LeetCode 账户合并
- **文件**: 
  - Code07_AccountsMerge.java (Java版本)
  - Code07_AccountsMerge.cpp (C++版本)
  - Code07_AccountsMerge.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/accounts-merge/
- **特点**: 字符串处理 + 并查集

### 8. LeetCode 冗余连接
- **文件**: 
  - Code08_RedundantConnection.java (Java版本)
  - Code08_RedundantConnection.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/redundant-connection/
- **特点**: 检测图中的环 + 并查集

### 9. LeetCode 句子相似性II
- **文件**: 
  - Code09_SentenceSimilarityII.java (Java版本)
  - Code09_SentenceSimilarityII.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/sentence-similarity-ii/
- **特点**: 字符串相似性判断 + 并查集传递性

### 10. LeetCode 按公因数计算最大组件大小
- **文件**: 
  - Code10_LargestComponentSizeByCommonFactor.java (Java版本)
  - Code10_LargestComponentSizeByCommonFactor.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/largest-component-size-by-common-factor/
- **特点**: 质因数分解 + 并查集

### 11. LeetCode 等式方程的可满足性
- **文件**: 
  - Code11_SatisfiabilityOfEqualityEquations.java (Java版本)
  - Code11_SatisfiabilityOfEqualityEquations.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/satisfiability-of-equality-equations/
- **特点**: 逻辑推理 + 并查集

### 12. LeetCode 连通网络的操作次数
- **文件**: 
  - Code12_NumberOfOperationsToMakeNetworkConnected.java (Java版本)
  - Code12_NumberOfOperationsToMakeNetworkConnected.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/number-of-operations-to-make-network-connected/
- **特点**: 网络连通性 + 并查集

### 13. LeetCode 交换字符串中的元素
- **文件**: 
  - Code13_SmallestStringWithSwaps.java (Java版本)
  - Code13_SmallestStringWithSwaps.py (Python版本)
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/smallest-string-with-swaps/
- **特点**: 字符串排序 + 并查集分组

## 解题技巧总结

### 1. 适用场景
- 连通性问题
- 集合合并问题
- 检测环问题
- 等价类问题

### 2. 实现要点
- 初始化时每个元素都是独立集合
- Find操作要实现路径压缩
- Union操作要实现按秩合并或小挂大
- 注意边界条件处理

### 3. 常见变形
- 带权并查集
- 可撤销并查集
- 可持久化并查集

## 工程化考量

### 1. 异常处理
- 输入参数校验
- 空指针检查
- 边界条件处理

### 2. 性能优化
- 路径压缩优化
- 按秩合并优化
- 内存使用优化

### 3. 代码可读性
- 清晰的变量命名
- 详细的注释说明
- 合理的函数拆分

## 复杂度分析

| 操作 | 优化前 | 优化后(路径压缩+按秩合并) |
|------|--------|--------------------------|
| 构建 | O(n) | O(n) |
| Find | O(n) | O(α(n)) ≈ O(1) |
| Union | O(n) | O(α(n)) ≈ O(1) |

## 应用领域

1. **图论算法**：检测连通性、环检测
2. **网络分析**：社交网络中的社区发现
3. **图像处理**：连通区域标记
4. **编译原理**：等价类分析
5. **分布式系统**：集群管理

## 学习建议

1. 熟练掌握基础模板实现
2. 理解路径压缩和按秩合并原理
3. 多做相关题目，积累经验
4. 注意边界条件和特殊情况处理
5. 学习并查集的扩展应用（如带权并查集等）

## 相关平台题目索引

| 平台 | 题号 | 题目 | 难度 |
|------|------|------|------|
| LeetCode | 547 | 朋友圈 | 中等 |
| LeetCode | 684 | 冗余连接 | 中等 |
| LeetCode | 721 | 账户合并 | 中等 |
| LeetCode | 200 | 岛屿数量 | 中等 |
| LeetCode | 765 | 情侣牵手 | 困难 |
| LeetCode | 839 | 相似字符串组 | 困难 |
| 洛谷 | P3367 | 并查集模板 | 入门 |
| 牛客网 | NC14550 | 并查集模板题 | 入门 |
| LintCode | 1045 | 朋友圈 | 中等 |
| LintCode | 1297 | 账户合并 | 中等 |
| LeetCode | 737 | 句子相似性II | 中等 |
| LeetCode | 952 | 按公因数计算最大组件大小 | 困难 |
| LeetCode | 990 | 等式方程的可满足性 | 中等 |
| LeetCode | 1319 | 连通网络的操作次数 | 中等 |
| LeetCode | 1202 | 交换字符串中的元素 | 中等 |

## 参考资料

1. 《算法导论》第21章：用于不相交集合的数据结构
2. 《算法竞赛入门经典》第5章：并查集
3. LeetCode官方题解
4. 各大OJ平台相关题目