# 轮廓线DP与插头DP专题

## 算法简介

轮廓线DP（Contour Line DP）和插头DP（Plug DP）是解决棋盘类问题的重要动态规划技术。这类问题通常在较小的棋盘（如n×m，其中min(n,m)≤12）上进行操作。

### 核心思想

轮廓线DP的核心思想是逐格转移，将棋盘的边界线（轮廓线）的状态作为DP状态的一部分。插头DP是轮廓线DP的一种特殊形式，主要用于处理连通性问题。

### 适用场景

1. 骨牌覆盖问题（如多米诺骨牌铺满棋盘）
2. 染色问题（满足特定约束的棋盘染色）
3. 路径问题（哈密顿路径、回路等）
4. 连通性问题（形成特定连通块）
5. L型地板铺设问题
6. 最大权值回路问题

## 经典题目列表

### 1. 骨牌覆盖类

#### 1.1 Tiling Dominoes (UVA 11270 / POJ 2411)
- 题目：用1×2的骨牌铺满n×m棋盘，求方案数
- 链接：https://vjudge.net/problem/UVA-11270
- 类型：轮廓线DP基础题
- 代码：Code05_TilingDominoes.java, Code05_TilingDominoes.cpp, Code05_TilingDominoes.py
- 时间复杂度：O(n × m × 2^m)
- 空间复杂度：O(2^m)

#### 1.2 Domino Tiling with Obstacles
- 题目：用1×2的骨牌铺满n×m棋盘，其中有些格子是障碍物不能放置骨牌，求方案数
- 类型：轮廓线DP（带障碍物）
- 代码：Code15_DominoWithObstacles.java, Code15_DominoWithObstacles.cpp, Code15_DominoWithObstacles.py
- 时间复杂度：O(n × m × 2^m)
- 空间复杂度：O(2^m)

#### 1.3 补充题目：Domino and Tromino Tiling (LeetCode 790)
- 题目：用1×2的骨牌和L型骨牌铺满2×n的格子，求方案数
- 链接：https://leetcode.cn/problems/domino-and-tromino-tiling/
- 类型：轮廓线DP（混合骨牌）
- 代码：Code17_DominoAndTrominoTiling.java, Code17_DominoAndTrominoTiling.cpp, Code17_DominoAndTrominoTiling.py
- 时间复杂度：O(n)
- 空间复杂度：O(1)

#### 1.4 补充题目：Grid Dominoes (CodeChef)
- 题目：用L型骨牌覆盖特定形状的网格
- 链接：https://www.codechef.com/problems/GRIDDOM
- 类型：轮廓线DP（复杂形状）
- 时间复杂度：O(n × m × 2^m)
- 空间复杂度：O(2^m)

#### 1.2 Corn Fields (USACO)
- 题目：在特定条件下种草，相邻格子不能同时种草
- 链接：https://www.luogu.com.cn/problem/P1879
- 类型：轮廓线DP基础题
- 代码：Code01_CornFields1.java, Code01_CornFields2.java, Code01_CornFields3.java

#### 1.3 Paving Tiles (POJ 2411)
- 题目：用1×2的瓷砖铺满n×m区域
- 链接：http://poj.org/problem?id=2411
- 类型：轮廓线DP基础题
- 代码：Code02_PavingTile1.java, Code02_PavingTile2.java

#### 1.4 Mondriaan's Dream (POJ 2411)
- 题目：用1×2和2×1的多米诺骨牌铺满n×m的棋盘，求方案数
- 链接：http://poj.org/problem?id=2411
- 类型：轮廓线DP基础题
- 代码：Code08_MondriaanDream.java, Code08_MondriaanDream.cpp, Code08_MondriaanDream.py

### 2. 路径类

#### 2.1 Eat the Trees (HDU 1693)
- 题目：用若干回路覆盖棋盘所有非障碍格子
- 链接：http://acm.hdu.edu.cn/showproblem.php?pid=1693
- 类型：插头DP基础题（多回路）
- 代码：Code06_EatTheTrees.java, Code06_EatTheTrees.cpp, Code06_EatTheTrees.py

#### 2.2 Path Coverage Problem
- 题目：在n×m的棋盘上，找出从起点到终点的一条路径，使得路径经过所有非障碍格子恰好一次
- 类型：插头DP（哈密顿路径）
- 代码：Code16_PathCoverage.java, Code16_PathCoverage.cpp, Code16_PathCoverage.py

#### 2.3 补充题目：Robot Path (AtCoder ABC 314)
- 题目：机器人从起点到终点，经过所有格子恰好一次的路径数
- 链接：https://atcoder.jp/contests/abc314
- 类型：插头DP（机器人路径）

#### 2.4 补充题目：Hamiltonian Path (SPOJ 148)
- 题目：网格图中的哈密顿路径计数
- 链接：https://www.spoj.com/problems/HAMIL/

#### 2.2 Tony's Tour (POJ 1739)
- 题目：求哈密顿路径数量
- 链接：http://poj.org/problem?id=1739
- 类型：插头DP（单回路）

#### 2.3 Postman (HNOI2004)
- 题目：求n×m网格图中哈密顿回路的个数
- 链接：https://www.luogu.com.cn/problem/P2289
- 类型：插头DP（哈密顿回路）
- 代码：Code09_Postman.java, Code09_Postman.cpp, Code09_Postman.py

#### 2.4 Template Plug DP (洛谷P5056)
- 题目：给出n*m的方格，有些格子不能铺线，其它格子必须铺，形成一个闭合回路。问有多少种铺法？
- 链接：https://www.luogu.com.cn/problem/P5056
- 类型：插头DP（模板题）
- 代码：Code11_TemplatePlugDP.java, Code11_TemplatePlugDP.cpp, Code11_TemplatePlugDP.py

#### 2.5 Magic Park (HNOI2007)
- 题目：给一个m*n的矩阵，每个矩阵内有个权值V(i,j)（可能为负数），要求找一条回路，使得每个点最多经过一次，并且经过的点权值之和最大
- 链接：https://www.luogu.com.cn/problem/P3190
- 类型：插头DP（最大权值回路）
- 代码：Code12_MagicPark.java, Code12_MagicPark.cpp, Code12_MagicPark.py

### 3. 染色类

#### 3.1 Black and White (UVA 10572)
- 题目：黑白染色，要求同色连通且2×2子矩阵颜色不全相同
- 链接：https://vjudge.net/problem/UVA-10572
- 类型：插头DP（染色问题）
- 代码：Code07_BlackAndWhite.java, Code07_BlackAndWhite.cpp, Code07_BlackAndWhite.py

#### 3.2 Adjacent Different
- 题目：相邻格子染不同颜色
- 类型：轮廓线DP（染色问题）
- 代码：Code03_AdjacentDifferent1.java, Code03_AdjacentDifferent2.java

#### 3.3 Chessboard Coloring Problem
- 题目：给n×m的棋盘染色，有k种颜色，相邻格子颜色不能相同，求染色方案数
- 类型：轮廓线DP（多进制状态）
- 代码：Code14_ColoringProblem.java, Code14_ColoringProblem.cpp, Code14_ColoringProblem.py

#### 3.4 补充题目：Colorful Rectangle (LeetCode 1997)
- 题目：给n×m的矩形染色，要求每行颜色不同且相邻行颜色不能有相同的列
- 链接：https://leetcode.cn/problems/first-day-where-you-have-been-in-all-the-rooms/
- 类型：轮廓线DP + 容斥原理

#### 3.5 补充题目：Coloring a Grid (Codeforces 1260E)
- 题目：3×n网格的染色问题，相邻格子颜色不同，求方案数
- 链接：https://codeforces.com/problemset/problem/1260/E
- 类型：轮廓线DP（特殊网格）

### 4. 其他类

#### 4.1 Kings Fighting
- 题目：在棋盘上放置国王，使其互不攻击
- 类型：轮廓线DP（状态压缩）
- 代码：Code04_KingsFighting1.java, Code04_KingsFighting2.java

#### 4.2 Floor (SCOI2011)
- 题目：用L型地板铺满n×m的房间，求方案数
- 链接：https://www.luogu.com.cn/problem/P3272
- 类型：插头DP（L型地板铺设）
- 代码：Code10_Floor.java, Code10_Floor.cpp, Code10_Floor.py

## 算法要点

### 状态表示

轮廓线DP的状态通常表示为：
- `dp[i][j][s]` 表示处理到第i行第j列，轮廓线状态为s的方案数

插头DP的状态通常表示为：
- `dp[i][j][s]` 表示处理到第i行第j列，插头状态为s的方案数（方案数或最大权值）

### 状态转移

状态转移需要考虑当前格子的处理方式：
1. 骨牌类：不放、横放、竖放
2. 路径类：生成插头、延续插头、合并插头
3. 染色类：选择颜色并确保满足约束
4. L型地板类：根据插头状态放置不同方向的L型地板
5. 权值类：在转移时累加权值

### 优化技巧

1. 使用滚动数组优化空间复杂度
2. 使用哈希表存储状态（稀疏状态）
3. 根据题目特性选择合适的编码方式

## 时间与空间复杂度

- 轮廓线DP时间复杂度通常为O(n×m×2^m)
- 空间复杂度可通过滚动数组优化至O(m×2^m)
- 插头DP复杂度类似，但常数因子可能更大
- 三进制插头DP时间复杂度为O(n×m×3^m)

## 实现细节

1. 位运算操作（get、set函数）
2. 状态合法性检查
3. 边界条件处理
4. 答案统计方式
5. 模运算处理大数
6. 负无穷表示不可达状态

## 算法技巧总结

### 题型分类与解题思路

#### 1. 骨牌覆盖类问题
- **特征**：棋盘覆盖、多米诺骨牌、瓷砖铺设
- **解题思路**：轮廓线DP，逐格转移，记录轮廓线状态
- **关键技巧**：状态压缩、滚动数组优化、位运算
- **典型题目**：POJ 2411, LeetCode 790

#### 2. 路径类问题
- **特征**：哈密顿路径、回路、连通性
- **解题思路**：插头DP，记录连通性状态
- **关键技巧**：括号表示法、最小表示法、状态哈希
- **典型题目**：HDU 1693, 洛谷P5056

#### 3. 染色类问题
- **特征**：相邻不同色、特定约束染色
- **解题思路**：轮廓线DP，多进制状态表示
- **关键技巧**：颜色编码、约束检查、状态转移优化
- **典型题目**：UVA 10572, 洛谷P2435

#### 4. L型地板铺设问题
- **特征**：L形瓷砖、复杂形状覆盖
- **解题思路**：插头DP，特殊状态处理
- **关键技巧**：多状态组合、特殊转移规则
- **典型题目**：SCOI2011 Floor

### 工程化考量

#### 1. 异常处理与边界场景
- 空棋盘处理（n=0或m=0）
- 奇数面积棋盘处理
- 障碍物位置合法性检查
- 输入数据范围验证

#### 2. 性能优化策略
- 滚动数组减少空间复杂度
- 预处理合法状态减少无效枚举
- 位运算优化状态操作
- 状态哈希表优化稀疏状态

#### 3. 代码质量保障
- 详细的注释和文档
- 完整的测试用例
- 多语言实现验证
- 复杂度分析

### 调试与问题定位

#### 1. 调试技巧
- 打印中间状态变量
- 小规模测试用例验证
- 边界条件单独测试
- 状态转移过程可视化

#### 2. 常见问题排查
- 状态编码错误
- 转移条件遗漏
- 边界处理不当
- 模运算错误

### 面试与笔试应用

#### 1. 面试准备
- 掌握核心算法思想
- 熟悉典型题目解法
- 能够分析时间空间复杂度
- 理解算法优化思路

#### 2. 笔试技巧
- 快速实现模板代码
- 处理大规模数据输入
- 优化常数因子
- 验证算法正确性

## 扩展学习资源

### 1. 推荐学习资料
- 《算法竞赛入门经典》
- OI Wiki 轮廓线DP专题
- Codeforces 相关题目讨论
- LeetCode 动态规划专题

### 2. 进阶题目
- URAL 1519 Formula 1
- SPOJ COBALT
- HYSBZ 3125
- CodeChef GRIDDOM

### 3. 相关技术
- 状态压缩动态规划
- 连通性状态表示
- 最小表示法
- 哈希表优化

## 项目文件结构

```
class125/
├── Code01_CornFields1.java/.cpp/.py    # 种草问题（普通状压DP）
├── Code01_CornFields2.java/.cpp/.py    # 种草问题（轮廓线DP）
├── Code01_CornFields3.java/.cpp/.py    # 种草问题（空间压缩）
├── Code02_PavingTile1.java/.cpp/.py    # 贴瓷砖（轮廓线DP）
├── Code02_PavingTile2.java/.cpp/.py    # 贴瓷砖（空间压缩）
├── Code03_AdjacentDifferent1.java/.cpp/.py # 相邻不同色染色
├── Code04_KingsFighting1.java          # 国王互不攻击
├── Code04_KingsFighting2.java          # 国王互不攻击（优化）
├── Code05_TilingDominoes.java/.cpp/.py # 多米诺骨牌覆盖
├── Code06_EatTheTrees.java/.cpp/.py    # 吃树问题（多回路）
├── Code07_BlackAndWhite.java/.cpp/.py  # 黑白染色
├── Code08_MondriaanDream.java/.cpp/.py # 蒙德里安的梦想
├── Code09_Postman.java/.cpp/.py        # 邮递员问题
├── Code10_Floor.java/.cpp/.py          # L型地板铺设
├── Code11_TemplatePlugDP.java/.cpp/.py # 插头DP模板
├── Code12_MagicPark.java/.cpp/.py      # 魔法公园
├── Code13_GridMaxSum.java/.cpp         # 网格最大和
├── Code14_ColoringProblem.java/.cpp/.py # 棋盘染色
├── Code15_DominoWithObstacles.java/.cpp/.py # 带障碍多米诺
├── Code16_PathCoverage.java/.cpp/.py   # 路径覆盖
├── Code17_DominoAndTrominoTiling.java/.cpp/.py # 多米诺和托米诺平铺
└── README.md                           # 项目文档
```

## 使用说明

### 1. 编译运行
- **Java**: `javac CodeXX_*.java && java Main`
- **C++**: `g++ CodeXX_*.cpp -o main && ./main`
- **Python**: `python CodeXX_*.py`

### 2. 输入格式
- 参考每个代码文件中的注释说明
- 通常为标准输入格式
- 包含测试用例示例

### 3. 测试验证
- 使用小规模测试用例验证正确性
- 对比不同语言实现结果
- 检查边界条件处理

## 贡献指南

欢迎提交新的轮廓线DP和插头DP题目实现，请确保：
1. 提供Java、C++、Python三语言实现
2. 包含详细的注释和复杂度分析
3. 添加完整的测试用例
4. 更新README.md文档

## 许可证

本项目采用MIT许可证，详见LICENSE文件。