# 树链剖分（Heavy-Light Decomposition）

## 概述

树链剖分是一种高效的树路径处理算法，通过将树分解为若干条重链，结合线段树等数据结构，实现对树上路径的高效查询和更新操作。

## 核心算法

### 算法思想
1. **第一次DFS**：计算每个节点的子树大小，确定重儿子
2. **第二次DFS**：建立重链，分配DFS序
3. **线段树维护**：使用线段树维护每条链上的信息

### 时间复杂度
- **预处理**：O(n)
- **路径操作**：O(log²n)
- **子树操作**：O(log n)
- **空间复杂度**：O(n)

## 题目集合

### 基础模板题
1. **洛谷 P3384** - 【模板】轻重链剖分
   - 路径更新、路径查询、子树更新、子树查询
   - [题目链接](https://www.luogu.com.cn/problem/P3384)

### 经典题目
2. **SPOJ QTREE** - Query on a tree
   - 边权查询和更新
   - [题目链接](https://www.spoj.com/problems/QTREE/)

3. **SPOJ QTREE2** - Query on a tree II
   - 路径距离查询、路径第k个节点查询
   - [题目链接](https://www.spoj.com/problems/QTREE2/)

4. **HDU 3966** - Aragorn's Story
   - 路径增量更新、单点查询
   - [题目链接](http://acm.hdu.edu.cn/showproblem.php?pid=3966)

5. **POJ 3237** - Tree
   - 边权修改、路径取反、路径最大值查询
   - [题目链接](http://poj.org/problem?id=3237)

6. **Codeforces 343D** - Water Tree
   - 节点填水、子树清空、节点查询
   - [题目链接](https://codeforces.com/problemset/problem/343/D)

## 文件结构

```
树链剖分/
├── HeavyLightDecomposition.java          # 基础树链剖分实现
├── heavy_light_decomposition.cpp         # C++版本实现
├── heavy_light_decomposition.py          # Python版本实现
├── Luogu_P3384_树链剖分模板.java         # 洛谷模板题
├── SPOJ_QTREE_QueryOnTree.java           # SPOJ QTREE
├── SPOJ_QTREE2_QueryOnTree2.java         # SPOJ QTREE2
├── HDU_3966_AragornsStory.java           # HDU 3966
├── POJ_3237_Tree.java                    # POJ 3237
├── Codeforces_343D_WaterTree.java         # Codeforces 343D
├── 算法复杂度分析.md                      # 复杂度分析文档
├── 算法思路总结.md                        # 算法思路总结
├── 工程化实践指南.md                      # 工程实践指南
└── README.md                             # 项目说明
```

## 使用说明

### 编译运行

#### Java版本
```bash
javac *.java
java 主类名 < 输入文件
```

#### C++版本
```bash
g++ -std=c++11 -O2 heavy_light_decomposition.cpp -o hld
./hld < 输入文件
```

#### Python版本
```bash
python heavy_light_decomposition.py < 输入文件
```

### 输入格式

不同题目的输入格式略有不同，具体参考各个题目的实现文件中的注释说明。

## 算法特点

### 优点
1. **高效**：路径操作时间复杂度为O(log²n)
2. **通用**：支持多种路径和子树操作
3. **稳定**：预处理后操作稳定可靠

### 缺点
1. **代码复杂**：实现相对复杂
2. **常数较大**：实际运行常数较大
3. **不支持动态树**：树结构需要静态

## 学习建议

### 学习路径
1. **理解原理**：先理解树链剖分的核心思想
2. **掌握实现**：学习基础实现代码
3. **练习题目**：从简单题目开始练习
4. **优化技巧**：学习性能优化方法

### 练习顺序
1. 洛谷 P3384（模板题）
2. HDU 3966（路径更新）
3. SPOJ QTREE（边权操作）
4. POJ 3237（复杂操作）
5. Codeforces 343D（特殊操作）

## 扩展阅读

### 相关算法
- **倍增LCA**：快速求最近公共祖先
- **欧拉序+RMQ**：O(1)查询LCA
- **树分治**：解决树上的分治问题
- **Link-Cut Tree**：支持动态树的树链剖分

### 进阶题目
- **BZOJ 1036** - 树的统计
- **ZOJ 2114** - Transport
- **UVa 12424** - Answering Queries on a Tree

## 贡献指南

欢迎提交新的题目实现、优化建议或文档改进。

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

---

**注意**：所有代码都经过详细注释和测试，确保正确性和可读性。建议在学习过程中仔细阅读注释，理解算法实现的每一个细节。