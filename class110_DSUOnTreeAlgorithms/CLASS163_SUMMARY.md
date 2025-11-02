# Class163 DSU on Tree 算法专题总结

## 项目概述

本项目完成了DSU on Tree（树上启发式合并）算法的全面实现，包括经典问题的多种解法、理论知识详解、各平台题目整理以及补充训练内容。所有实现都包含详细的注释说明，涵盖题目来源、问题描述、解法思路、时间复杂度和空间复杂度分析。

## 完成的工作

### 1. 算法理论与总结
- [DSU_ON_TREE_SUMMARY.md](DSU_ON_TREE_SUMMARY.md) - DSU on Tree算法详细总结
- [README.md](README.md) - DSU on Tree算法专题介绍
- [TEST_CASES.md](TEST_CASES.md) - 测试用例设计与验证

### 2. 经典题目实现

#### 核心题目实现（Java/C++/Python三语言）
1. **树上数颜色 (洛谷 U41492)**
   - Java: [Code01_DsuOnTree1.java](Code01_DsuOnTree1.java)
   - C++: [Code01_DsuOnTree1.cpp](Code01_DsuOnTree1.cpp)
   - Python: [Code01_DsuOnTree1.py](Code01_DsuOnTree1.py)

2. **颜色平衡的子树 (洛谷 P9233)**
   - Java: [Code02_ColorBanlance1.java](Code02_ColorBanlance1.java)
   - C++: [Code02_ColorBanlance1.cpp](Code02_ColorBanlance1.cpp)
   - Python: [Code02_ColorBanlance1.py](Code02_ColorBanlance1.py)

3. **Lomsat gelral (Codeforces 600E)**
   - Java: [Code03_LomsatGelral1.java](Code03_LomsatGelral1.java)
   - C++: [Code03_LomsatGelral1.cpp](Code03_LomsatGelral1.cpp)
   - Python: [Code03_LomsatGelral1.py](Code03_LomsatGelral1.py)

4. **Tree and Queries (Codeforces 375D)**
   - Java: [Code09_TreeAndQueries1.java](Code09_TreeAndQueries1.java)
   - C++: [Code09_TreeAndQueries1.cpp](Code09_TreeAndQueries1.cpp)
   - Python: [Code09_TreeAndQueries1.py](Code09_TreeAndQueries1.py)

5. **Dominant Indices (Codeforces 1009F)**
   - Java: [Code10_DominantIndices1.java](Code10_DominantIndices1.java)
   - C++: [Code10_DominantIndices1.cpp](Code10_DominantIndices1.cpp)
   - Python: [Code10_DominantIndices1.py](Code10_DominantIndices1.py)

6. **Count on a Tree II (SPOJ COT2)**
   - Java: [Code11_CountOnATreeII1.java](Code11_CountOnATreeII1.java)
   - C++: [Code11_CountOnATreeII1.cpp](Code11_CountOnATreeII1.cpp)
   - Python: [Code11_CountOnATreeII1.py](Code11_CountOnATreeII1.py)

### 3. 题目资源汇总
- [ADDITIONAL_DSU_ON_TREE_PROBLEMS.md](ADDITIONAL_DSU_ON_TREE_PROBLEMS.md) - 各大OJ平台DSU on Tree题目整理
- 涵盖Codeforces、洛谷、SPOJ、AtCoder、HackerEarth、HackerRank、CodeChef、UVa、杭电OJ、POJ、LOJ、TimusOJ、AizuOJ、USACO、Comet OJ、acwing、牛客、计蒜客、各大高校OJ等平台
- 包含LeetCode相关题目链接

## 算法特点

### 时间复杂度
- DSU on Tree算法的时间复杂度为O(n log n)，其中n为树中节点的数量

### 空间复杂度
- 空间复杂度为O(n)，主要用于存储树结构、重链剖分信息和颜色计数数组

### 适用场景
1. 静态树上查询问题
2. 子树信息统计问题
3. 可以离线处理的问题
4. 查询数量较多，直接暴力处理会超时的问题

## 核心思想

1. **重链剖分预处理**：计算每个节点的子树大小，确定重儿子
2. **启发式合并处理**：
   - 先处理轻儿子的信息，然后清除贡献
   - 再处理重儿子的信息并保留贡献
   - 最后重新计算轻儿子的贡献
3. **时间复杂度优化**：通过这种方式，保证每个节点最多被访问O(log n)次

## 工程化实现要点

1. **边界处理**：注意空树、单节点树等特殊情况
2. **内存优化**：合理使用全局数组，避免重复分配内存
3. **常数优化**：使用位运算、减少函数调用等优化常数
4. **可扩展性**：设计通用模板，便于适应不同类型的查询问题

## 文件命名规范

在class163目录中，所有代码文件采用`CodeXX_XXX.java/.cpp/.py`的命名格式，其中：
- XX为两位数字序号
- XXX为题目英文名称或核心功能描述

例如：`Code01_DsuOnTree1.java`

## 技术栈

- **Java实现**：使用标准Java库，适合理解算法逻辑
- **C++实现**：性能最优，常数因子最小，适合竞赛环境
- **Python实现**：代码简洁易懂，适合学习和教学

## 编译与运行

### Java版本
```bash
javac Code01_DsuOnTree1.java
java Code01_DsuOnTree1 < input.txt > output.txt
```

### C++版本
```bash
g++ Code01_DsuOnTree1.cpp -o Code01_DsuOnTree1
./Code01_DsuOnTree1 < input.txt > output.txt
```

### Python版本
```bash
python Code01_DsuOnTree1.py < input.txt > output.txt
```

## 测试验证

所有实现都经过测试验证，确保：
1. 代码可以正确编译运行
2. 算法逻辑正确
3. 时间复杂度符合预期
4. 空间复杂度符合预期

## 学习建议

1. **掌握基础**：熟练掌握DFS和树的基本操作，理解重链剖分的原理和实现
2. **实践练习**：从简单题目开始练习，逐步增加题目难度
3. **深入理解**：理解算法的时间复杂度来源，掌握算法的实现细节
4. **工程化应用**：注意边界情况处理，优化代码实现，提高代码可读性和可维护性

## 项目价值

1. **全面性**：涵盖DSU on Tree算法的各个方面，从理论到实践
2. **实用性**：提供多种语言实现，适应不同使用场景
3. **教育性**：详细的注释和说明，便于学习和理解
4. **扩展性**：模块化设计，便于添加新题目和功能