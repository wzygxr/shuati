# 树的重心相关题目补充

## 新增题目列表

### 1. POJ 1655 Balancing Act
- **题目描述**: 给定一棵树，找到树的重心（如果有多个重心，返回编号最小的）
- **重心定义**: 删除这个点后，剩余各个连通块中点数的最大值最小
- **测试链接**: http://poj.org/problem?id=1655
- **实现文件**: 
  - Code01_BalancingAct.java (已存在)
  - Code01_BalancingAct.py (新增)
  - Code01_BalancingAct.cpp (新增)

### 2. POJ 3107 Godfather
- **题目描述**: 找到树的所有重心
- **重心定义**: 删除这个点后，剩余各个连通块中点数的最大值不超过总节点数的一半
- **测试链接**: http://poj.org/problem?id=3107
- **实现文件**: 
  - Code02_Godfather.java (已存在)
  - Code02_Godfather.py (新增)
  - Code02_Godfather.cpp (新增)

### 3. Codeforces 1406C Link Cut Centroids
- **题目描述**: 通过删除一条边并添加一条边，使树的重心唯一
- **算法思想**: 树最多有两个重心且相邻，通过调整边使重心唯一
- **测试链接**: https://codeforces.com/problemset/problem/1406/C
- **实现文件**: 
  - Code04_LinkCutCentroids.java (已存在)
  - Code04_LinkCutCentroids.py (新增)
  - Code04_LinkCutCentroids.cpp (新增)

### 4. Codeforces 686D Kay and Snowflake
- **题目描述**: 给定一棵有根树，求出每一棵子树的重心
- **算法思想**: 利用树的性质，通过优化计算找到子树重心
- **测试链接**: https://codeforces.com/contest/686/problem/D
- **实现文件**: 
  - Code05_KayAndSnowflake.java (已存在)
  - Code05_KayAndSnowflake.py (已存在)
  - Code05_KayAndSnowflake.cpp (新增)

### 5. Codeforces 708C Centroids
- **题目描述**: 对于树上的每个点，判断是否可以通过调整一条边使其成为重心
- **算法思想**: 通过分析每个节点的最大子树，判断是否可以通过调整边使其成为重心
- **测试链接**: https://codeforces.com/contest/708/problem/C
- **实现文件**: 
  - Code06_Centroids.java (已存在)
  - Code06_Centroids.py (新增)
  - Code06_Centroids.cpp (新增)

### 6. Luogu P1364 医院设置
- **题目描述**: 在一棵树上找一个点，使得该点到其他点距离之和最小
- **算法思想**: 利用树的重心性质，所有点到重心的距离和最小
- **测试链接**: https://www.luogu.com.cn/problem/P1364
- **实现文件**: 
  - Code07_HospitalLocation.java (已存在)
  - Code07_HospitalLocation.py (新增)
  - Code07_HospitalLocation.cpp (新增)

### 7. Luogu U328173 【模板】树的重心
- **题目描述**: 给定一棵无根树，求这棵树的重心（可能有多个）
- **重心定义**: 计算以无根树每个点为根节点时的最大子树大小，这个值最小的点称为无根树的重心
- **测试链接**: https://www.luogu.com.cn/problem/U328173
- **实现文件**: 
  - Code08_TreeCentroidTemplate.java (已存在)
  - Code08_TreeCentroidTemplate.py (新增)
  - Code08_TreeCentroidTemplate.cpp (新增)

### 8. Luogu P4582 [FJOI2014] 树的重心
- **题目描述**: 给定一个n个点的树，问这个树有多少不同的连通子树，和这个树有相同的重心
- **重心定义**: 删掉某点i后，若剩余k个连通分量，那么定义d(i)为这些连通分量中点的个数的最大值，所谓重心，就是使得d(i)最小的点i
- **测试链接**: https://www.luogu.com.cn/problem/P4582
- **实现文件**: 
  - Code09_FJOI2014TreeCentroid.java (已存在)
  - Code09_FJOI2014TreeCentroid.py (新增)
  - Code09_FJOI2014TreeCentroid.cpp (新增)

### 9. SPOJ PT07Z Longest path in a tree
- **题目描述**: 求树的直径，与树的重心密切相关
- **算法思想**: 树的直径可以通过两次BFS或DFS求解，与重心性质相关
- **测试链接**: https://www.spoj.com/problems/PT07Z/
- **实现文件**: 
  - Code11_SPOJPT07Z.java (已存在)
  - Code11_SPOJPT07Z.py (新增)
  - Code11_SPOJPT07Z.cpp (新增)

### 10. LeetCode 310. 最小高度树
- **题目描述**: 对于一个具有 n 个节点的树，给定 n-1 条边，找到所有可能的最小高度树的根节点。
- **算法思想**: 最小高度树的根节点就是树的重心
- **测试链接**: https://leetcode.cn/problems/minimum-height-trees/
- **实现文件**: 
  - Code16_LeetCode310.java (已存在)
  - Code16_LeetCode310.py (新增)
  - Code16_LeetCode310.cpp (新增)

### 11. LeetCode 543. 二叉树的直径
- **题目描述**: 给定一棵二叉树，计算它的直径长度。直径是指树中任意两个节点之间最长路径的长度。
- **算法思想**: 利用深度优先搜索计算每个节点的高度，同时更新最长路径长度（直径）
- **测试链接**: https://leetcode.com/problems/diameter-of-binary-tree/
- **实现文件**: 
  - Code25_LeetCode543.java (已存在)
  - Code25_LeetCode543.py (新增)
  - Code25_LeetCode543.cpp (新增)

## 算法复杂度分析

所有实现的时间复杂度均为O(n)，空间复杂度也为O(n)，其中n为树中节点的数量。

## 实现语言

每道题目都提供了Java、Python、C++三种语言的实现，包含详细的注释和复杂度分析。

## 树的重心定义与性质

### 定义
树的重心：找到一个点，其所有的子树中最大的子树节点数最少。

### 性质
1. 以树的重心为根时，所有子树的大小都不超过整棵树大小的一半
2. 树中所有点到某个点的距离和中，到重心的距离和是最小的；如果有两个重心，那么到它们的距离和一样
3. 把两棵树通过一条边相连得到一棵新的树，那么新的树的重心在连接原来两棵树的重心的路径上
4. 在一棵树上添加或删除一个叶子，那么它的重心最多只移动一条边的距离
5. 一棵树最多有两个重心，且相邻
6. 树的重心将树分成若干子树，这些子树的大小都不超过原树大小的1/2
7. 树的重心是树的中心节点，即距离所有节点的最远点的距离最小的点

## 解题思路与技巧总结

### 什么时候使用树的重心？
1. 当问题涉及到树的最优分割时（如最小化最大子树大小）
2. 当需要找到一个点，使得所有节点到该点的距离和最小时
3. 当问题需要将树分解为多个平衡子树时
4. 当需要优化树上的查询操作时（如树分治）
5. 当问题与树的直径、中心节点相关时

### 解题技巧
1. **寻找树的重心**：通过一次DFS或BFS计算每个节点的子树大小，并记录最大子树大小，找到最小的那个节点
2. **树分治**：利用重心将树分割成多个子树，递归处理每个子树
3. **换根DP**：在计算某些树上的全局性质时，通过换根来优化计算
4. **利用树的重心性质**：在需要最小化距离和或平衡分割时，优先考虑重心

### 常见题型
1. **寻找树的重心**：直接应用定义
2. **最优分割问题**：利用重心性质进行分割
3. **距离和最小化问题**：利用重心的距离和最小性质
4. **树分治问题**：基于重心分解的分治算法
5. **动态树问题**：处理树的动态变化，如添加/删除节点后寻找新的重心