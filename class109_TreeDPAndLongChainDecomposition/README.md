# 树形DP与长链剖分专题

## 题目列表

### 1. LeetCode 834. 树中距离之和
- **题目描述**: 给定一个无向、连通的树，计算每个节点到其他所有节点的距离之和。
- **解题思路**: 树形DP，通过两次DFS遍历解决。
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现语言**: Java, Python, C++

### 2. LeetCode 2246. 相邻字符不同的最长路径
- **题目描述**: 给定一棵树和每个节点的字符，找到相邻节点字符都不同的最长路径。
- **解题思路**: 树形DP，维护每个节点向下延伸的最长路径和次长路径。
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现语言**: Java, Python, C++

### 3. CF1009F Dominant Indices
- **题目描述**: 给定一棵树，对每个节点求最小的x使得d(u,x)最大，其中d(u,x)表示u子树中到u距离为x的节点数。
- **解题思路**: 长链剖分优化树形DP。
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **实现语言**: Java, Python, C++

## 算法详解

### 树形DP
树形DP是一种在树形结构上进行的动态规划方法，通常通过DFS遍历来实现。

#### 基本思路
1. 从叶子节点开始，自底向上计算每个节点的状态值
2. 利用子节点的结果推导父节点的结果
3. 有时需要换根DP来计算以每个节点为根时的结果

#### 常见应用场景
- 计算树中路径相关问题
- 树上最大独立集、最小支配集等图论问题
- 树上游戏、树上博弈等问题

### 长链剖分
长链剖分是一种优化树形DP的技术，通过重用长链上的计算结果来降低时间复杂度。

#### 基本概念
- **重儿子**: 子树深度最大的子节点
- **长链**: 从一个节点出发，一直选择重儿子形成的链
- **轻儿子**: 除重儿子外的其他子节点

#### 优化原理
1. 重儿子信息继承: 通过指针偏移实现O(1)继承重儿子的DP数组
2. 轻儿子信息合并: 暴力合并轻儿子信息，但每条链只合并一次
3. 空间优化: 同一条长链共享内存空间

#### 适用场景
- DP状态与深度相关的树形DP问题
- 需要计算子树中到根节点距离为d的节点数的问题
- 可以通过长链剖分优化时间复杂度的问题

## 文件说明

### Java实现
- `Code01_GrassPlanting1.java`: 边权转点权的模板题
- `Code02_NationalTour1.java`: 国家集训队旅游
- `Code03_UnderMoon1.java`: 月下毛景树
- `Code04_TreeKthAncestor1.java`: 树上k级祖先
- `Code05_Walkthrough1.java`: 攻略
- `Code06_DominantIndices1.java`: Dominant Indices
- `Code07_HotHotels1.java`: 火热旅馆
- `LC834_SumOfDistancesInTree.java`: LeetCode 834题解
- `LC2246_LongestPathWithDifferentAdjacentCharacters.java`: LeetCode 2246题解

### Python实现
- `CF1009F_Dominant_Indices.py`: CF1009F题解
- `LC834_SumOfDistancesInTree.py`: LeetCode 834题解
- `LC2246_LongestPathWithDifferentAdjacentCharacters.py`: LeetCode 2246题解

### C++实现
- `CF1009F_Dominant_Indices.cpp`: CF1009F题解
- `LC834_SumOfDistancesInTree.cpp`: LeetCode 834题解
- `LC2246_LongestPathWithDifferentAdjacentCharacters.cpp`: LeetCode 2246题解

### Markdown文档
- `CF1009F_Dominant_Indices.md`: CF1009F详细题解
- `CF1499F_Diamond_Miner.md`: CF1499F详细题解
- `LOJ3053_十二省联考2019_希望.md`: LOJ3053详细题解
- `P3899_湖南集训_谈笑风生.md`: P3899详细题解
- `P4292_WC2010_重建计划.md`: P4292详细题解
- `P5904_POI2014_HOT_Hotels.md`: P5904详细题解

## 编译和运行

### Java程序
```bash
# 编译
javac FileName.java

# 运行（需要在正确的目录下）
java -cp . packageName.FileName
```

### Python程序
```bash
python FileName.py
```

### C++程序
```bash
# 编译（需要支持C++11及以上标准）
g++ -std=c++11 FileName.cpp -o FileName

# 运行
./FileName
```

## 测试说明

所有Java程序都包含了测试用例，可以直接运行查看结果。

## 相关资源

- [OI Wiki - 树形DP](https://oi-wiki.org/dp/tree/)
- [OI Wiki - 长链剖分](https://oi-wiki.org/graph/hld/#长链剖分)
- [LeetCode 树形DP题目](https://leetcode.cn/tag/tree-dynamic-programming/)
- [Codeforces 树形DP题目](https://codeforces.com/problemset/tags/trees)