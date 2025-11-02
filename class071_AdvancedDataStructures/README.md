# Class029 高级数据结构扩展项目

## 项目概述

本项目实现了多种高级数据结构和算法，包括Boyer-Moore算法、FHQ Treap、K-D树、Link-Cut Tree和回文自动机等。每种算法都提供了Java、C++和Python三种语言的实现，并包含详细的测试用例和文档。

## 项目结构

```
class029_AdvancedDataStructures/
├── README.md                           # 项目说明文档
├── AdvancedBoyerMooreProblems.java    # Boyer-Moore算法Java实现
├── boyer_moore_algorithm.cpp          # Boyer-Moore算法C++实现
├── boyer_moore_algorithm.py           # Boyer-Moore算法Python实现
├── AdvancedFHQTreapProblems.java       # FHQ Treap算法Java实现
├── fhq_treap_algorithm.cpp            # FHQ Treap算法C++实现
├── fhq_treap_algorithm.py             # FHQ Treap算法Python实现
├── AdvancedKdTreeProblems.java         # K-D树算法Java实现
├── kd_tree.cpp                        # K-D树算法C++实现
├── kd_tree.py                         # K-D树算法Python实现
├── AdvancedLinkCutTreeProblems.java    # Link-Cut Tree算法Java实现
├── link_cut_tree.cpp                  # Link-Cut Tree算法C++实现
├── link_cut_tree.py                   # Link-Cut Tree算法Python实现
├── AdvancedPalindromicAutomatonProblems.java  # 回文自动机算法Java实现
├── palindromic_automaton.cpp          # 回文自动机算法C++实现
└── palindromic_automaton.py           # 回文自动机算法Python实现
```

## 算法实现详情

### 1. Boyer-Moore算法

**算法描述**: Boyer-Moore算法是一种高效的字符串匹配算法，通过坏字符规则和好后缀规则来跳过不必要的比较。

**特性**:
- 坏字符规则优化
- 好后缀规则优化
- 支持多模式匹配
- 时间复杂度：O(n/m) 最坏情况

**测试用例**:
- 基本字符串匹配
- 模式串在文本串中多次出现
- 模式串不在文本串中
- 边界情况测试

### 2. FHQ Treap算法

**算法描述**: FHQ Treap是一种平衡二叉搜索树，结合了Treap的随机性和分裂合并操作的高效性。

**特性**:
- 支持分裂和合并操作
- 随机化平衡
- 支持区间操作
- 时间复杂度：O(log n)

**测试用例**:
- 插入和删除操作
- 分裂和合并操作
- 区间查询
- 性能测试

### 3. K-D树算法

**算法描述**: K-D树是一种用于k维空间的数据结构，支持高效的范围查询和最近邻搜索。

**特性**:
- 多维空间索引
- 最近邻搜索
- 范围查询
- 动态插入和删除

**测试用例**:
- 二维点集构建
- 最近邻搜索
- 范围查询
- 动态更新测试

### 4. Link-Cut Tree算法

**算法描述**: Link-Cut Tree是一种动态树数据结构，支持路径操作和子树操作。

**特性**:
- 动态树维护
- 路径操作
- 子树操作
- 时间复杂度：O(log n)

**测试用例**:
- 树的基本操作
- 路径查询和更新
- 子树操作
- 动态连接和断开

### 5. 回文自动机算法

**算法描述**: 回文自动机（Palindromic Automaton）是一种专门用于处理回文串的数据结构，能够高效维护字符串的所有回文子串信息。

**特性**:
- 每个节点表示一个唯一的回文子串
- 支持动态添加字符
- 统计不同回文子串数量
- 查询最长回文子串

**测试用例**:
- 基本回文检测
- 最长回文子串查找
- 回文子串统计
- 动态字符串处理

## 编译和运行

### C++版本

```bash
# 编译
cd class029_AdvancedDataStructures
g++ -std=c++11 <algorithm_name>.cpp -o <executable_name>

# 运行
./<executable_name>
```

### Python版本

```bash
# 直接运行
cd class029_AdvancedDataStructures
python <algorithm_name>.py
```

### Java版本

```bash
# 编译
cd class029_AdvancedDataStructures
javac <algorithm_name>.java

# 运行
java <algorithm_name>
```

## 测试结果

所有算法都经过了严格的测试，包括：

1. **功能测试**: 验证算法的基本功能是否正确
2. **边界测试**: 测试边界情况和异常处理
3. **性能测试**: 测试算法的时间复杂度和空间复杂度
4. **兼容性测试**: 验证不同语言实现的一致性

## 性能分析

| 算法 | 时间复杂度 | 空间复杂度 | 适用场景 |
|------|------------|------------|----------|
| Boyer-Moore | O(n/m) | O(m+σ) | 字符串匹配 |
| FHQ Treap | O(log n) | O(n) | 动态集合操作 |
| K-D树 | O(log n) | O(n) | 多维空间查询 |
| Link-Cut Tree | O(log n) | O(n) | 动态树操作 |
| 回文自动机 | O(n) | O(n) | 回文串处理 |

## 扩展功能

每种算法都提供了高级扩展功能：

1. **Boyer-Moore**: 多模式匹配、近似匹配
2. **FHQ Treap**: 持久化、区间操作
3. **K-D树**: 动态更新、批量操作
4. **Link-Cut Tree**: 子树操作、路径聚合
5. **回文自动机**: 在线算法、统计功能

## 贡献指南

1. 遵循代码规范
2. 添加详细的注释
3. 提供完整的测试用例
4. 更新文档说明

## 许可证

本项目采用MIT许可证。

## 联系方式

如有问题或建议，请联系项目维护者。