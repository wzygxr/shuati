# 树的直径算法专题 (Class121)

## 概述

本目录包含树的直径相关算法的完整实现，涵盖了从基础概念到高级应用的各个层面。树的直径是指树中任意两点之间最长的简单路径，是一类经典的图论问题，在算法竞赛和实际应用中都有广泛的应用。

## 核心算法

### 1. 两次DFS/BFS法
- **适用场景**：边权非负的树
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **实现文件**：
  - [Code01_Diameter1.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Code01_Diameter1.java) - 基础实现
  - [SPOJ_PT07Z_LongestPathInTree.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_PT07Z_LongestPathInTree.java) - SPOJ题目实现
  - [CSES1131_TreeDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/CSES1131_TreeDiameter.java) - CSES题目实现

### 2. 树形动态规划法
- **适用场景**：可以处理负权边的树
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **实现文件**：
  - [Code01_Diameter2.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Code01_Diameter2.java) - 标准实现
  - [Code01_Diameter3.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Code01_Diameter3.java) - 简化实现

## 题目分类

### 基础题目
1. **LeetCode 543. 二叉树的直径**
   - 文件：[LeetCode543_DiameterOfBinaryTree.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode543_DiameterOfBinaryTree.java) / [LeetCode543_DiameterOfBinaryTree.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode543_DiameterOfBinaryTree.py) / [LeetCode543_DiameterOfBinaryTree.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode543_DiameterOfBinaryTree.cpp)
   - 难度：简单

2. **SPOJ PT07Z - Longest path in a tree**
   - 文件：[SPOJ_PT07Z_LongestPathInTree.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_PT07Z_LongestPathInTree.java) / [SPOJ_PT07Z_LongestPathInTree.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_PT07Z_LongestPathInTree.py) / [SPOJ_PT07Z_LongestPathInTree.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_PT07Z_LongestPathInTree.cpp)
   - 难度：简单

3. **CSES 1131 - Tree Diameter**
   - 文件：[CSES1131_TreeDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/CSES1131_TreeDiameter.java) / [CSES1131_TreeDiameter.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/CSES1131_TreeDiameter.py) / [CSES1131_TreeDiameter.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/CSES1131_TreeDiameter.cpp)
   - 难度：简单

4. **51Nod-2602 - 树的直径**
   - 文件：[Nod2602_TreeDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod2602_TreeDiameter.java) / [Nod2602_TreeDiameter.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod2602_TreeDiameter.py) / [Nod2602_TreeDiameter.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod2602_TreeDiameter.cpp)
   - 难度：简单

5. **洛谷 U81904 - 树的直径**
   - 文件：[LuoguU81904_TreeDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LuoguU81904_TreeDiameter.java) / [LuoguU81904_TreeDiameter.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LuoguU81904_TreeDiameter.py) / [LuoguU81904_TreeDiameter.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LuoguU81904_TreeDiameter.cpp)
   - 难度：简单

### 进阶题目
1. **AtCoder ABC221F - Diameter Set**
   - 文件：[AtCoderABC221F_DiameterSet.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/AtCoderABC221F_DiameterSet.java) / [AtCoderABC221F_DiameterSet.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/AtCoderABC221F_DiameterSet.py) / [AtCoderABC221F_DiameterSet.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/AtCoderABC221F_DiameterSet.cpp)
   - 难度：困难
   - 涉及算法：组合数学、快速幂

2. **Codeforces 1499F - Diameter Cuts**
   - 文件：[Codeforces1499F_DiameterCuts.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Codeforces1499F_DiameterCuts.java) / [Codeforces1499F_DiameterCuts.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Codeforces1499F_DiameterCuts.py) / [Codeforces1499F_DiameterCuts.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Codeforces1499F_DiameterCuts.cpp)
   - 难度：困难
   - 涉及算法：树形DP

3. **LeetCode 1245. Tree Diameter**
   - 文件：[LeetCode1245_TreeDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1245_TreeDiameter.java) / [LeetCode1245_TreeDiameter.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1245_TreeDiameter.py) / [LeetCode1245_TreeDiameter.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1245_TreeDiameter.cpp)
   - 难度：中等

4. **LeetCode 1522. Diameter of N-Ary Tree**
   - 文件：[LeetCode1522_DiameterOfNAryTree.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1522_DiameterOfNAryTree.java) / [LeetCode1522_DiameterOfNAryTree.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1522_DiameterOfNAryTree.py) / [LeetCode1522_DiameterOfNAryTree.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1522_DiameterOfNAryTree.cpp)
   - 难度：简单

5. **LeetCode 1617. Count Subtrees With Max Distance**
   - 文件：[LeetCode1617_CountSubtreesWithMaxDistance.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/LeetCode1617_CountSubtreesWithMaxDistance.java)
   - 难度：困难
   - 涉及算法：位运算、BFS

6. **SPOJ MDST - Minimum Diameter Spanning Tree**
   - 文件：[SPOJ_MDST_MinimumDiameterSpanningTree.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_MDST_MinimumDiameterSpanningTree.java) / [SPOJ_MDST_MinimumDiameterSpanningTree.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_MDST_MinimumDiameterSpanningTree.py) / [SPOJ_MDST_MinimumDiameterSpanningTree.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/SPOJ_MDST_MinimumDiameterSpanningTree.cpp)
   - 难度：困难
   - 涉及算法：Floyd-Warshall、绝对中心

7. **51Nod-1803 - 森林直径**
   - 文件：[Nod1803_ForestDiameter.java](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod1803_ForestDiameter.java) / [Nod1803_ForestDiameter.py](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod1803_ForestDiameter.py) / [Nod1803_ForestDiameter.cpp](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/Nod1803_ForestDiameter.cpp)
   - 难度：困难

## 文档资料

1. [TreeDiameterProblems.md](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/TreeDiameterProblems.md) - 树的直径算法详解与题目汇总
2. [TreeDiameterProblemsComplete.md](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class121/TreeDiameterProblemsComplete.md) - 完整版树的直径算法详解与题目汇总

## 编译和运行

### Java文件
```bash
javac FileName.java
java FileName
```

### Python文件
```bash
python FileName.py
```

### C++文件
```bash
g++ -std=c++11 -o FileName.exe FileName.cpp
./FileName.exe
```

## 算法复杂度总结

| 算法 | 时间复杂度 | 空间复杂度 | 适用场景 |
|------|------------|------------|----------|
| 两次DFS/BFS法 | O(n) | O(n) | 边权非负的树 |
| 树形DP法 | O(n) | O(n) | 可以处理负权边的树 |
| 树形DP（Codeforces 1499F） | O(n^2) | O(n^2) | 计数问题 |
| 最小直径生成树 | O(n^3) | O(n^2) | 无向图 |

## 学习建议

1. **初学者**：从基础题目开始，掌握两次DFS/BFS法和树形DP法的基本思想
2. **进阶学习者**：尝试解决进阶题目，理解如何将树的直径算法与其他算法结合
3. **高手**：挑战困难题目，掌握优化技巧和高级应用

## 相关算法

- 树的中心
- 树的重心
- 最近公共祖先(LCA)
- 树上差分
- 虚树