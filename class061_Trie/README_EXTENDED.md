# 前缀树（Trie）算法专题 - 扩展版

## 目录
- [概述](#概述)
- [基础题目](#基础题目)
- [进阶题目](#进阶题目)
- [高级题目](#高级题目)
- [算法总结](#算法总结)
- [工程化考量](#工程化考量)
- [语言特性差异](#语言特性差异)
- [调试技巧](#调试技巧)
- [性能优化](#性能优化)

## 概述

前缀树（Trie）是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。前缀树在字符串搜索、自动补全、拼写检查等场景中有广泛应用。

### 核心特性
- **高效前缀匹配**：O(L)时间复杂度，其中L是字符串长度
- **空间优化**：共享公共前缀，节省存储空间
- **动态扩展**：支持动态插入和删除操作

## 基础题目

### 1. LeetCode 208. 实现 Trie (前缀树)
- **题目链接**：https://leetcode.cn/problems/implement-trie-prefix-tree/
- **难度**：中等
- **核心算法**：基本前缀树实现
- **时间复杂度**：插入O(L)，搜索O(L)，前缀匹配O(L)
- **空间复杂度**：O(N*L)

**代码文件**：
- `Code08_LeetCode208.java` - Java实现
- `Code08_LeetCode208.py` - Python实现  
- `Code08_LeetCode208.cpp` - C++实现

### 2. LeetCode 677. 键值映射
- **题目链接**：https://leetcode.cn/problems/map-sum-pairs/
- **难度**：中等
- **核心算法**：前缀树 + 值聚合
- **时间复杂度**：插入O(L)，求和O(L)
- **空间复杂度**：O(N*L)

**代码文件**：
- `Code11_LeetCode677.java` - Java实现
- `Code11_LeetCode677.py` - Python实现
- `Code11_LeetCode677.cpp` - C++实现

## 进阶题目

### 3. LeetCode 1268. 搜索推荐系统
- **题目链接**：https://leetcode.cn/problems/search-suggestions-system/
- **难度**：中等
- **核心算法**：前缀树 + DFS收集推荐
- **时间复杂度**：O(∑len(products[i]) + ∑len(searchWord))
- **空间复杂度**：O(∑len(products[i]) + ∑len(searchWord))

**代码文件**：
- `Code12_LeetCode1268.java` - Java实现
- `Code12_LeetCode1268.py` - Python实现
- `Code12_LeetCode1268.cpp` - C++实现

### 4. LeetCode 1707. 与数组中元素的最大异或值
- **题目链接**：https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
- **难度**：困难
- **核心算法**：二进制前缀树 + 离线查询
- **时间复杂度**：O(N log N + Q log Q + (N + Q) * 32)
- **空间复杂度**：O(N * 32 + Q)

**代码文件**：
- `Code09_LeetCode1707.java` - Java实现
- `Code09_LeetCode1707.py` - Python实现
- `Code09_LeetCode1707.cpp` - C++实现

## 高级题目

### 5. LeetCode 1803. 统计异或值在范围内的数对有多少
- **题目链接**：https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
- **难度**：困难
- **核心算法**：二进制前缀树 + 范围统计
- **时间复杂度**：O(N * 32)
- **空间复杂度**：O(N * 32)

**代码文件**：
- `Code10_LeetCode1803.java` - Java实现
- `Code10_LeetCode1803.py` - Python实现
- `Code10_LeetCode1803.cpp` - C++实现

### 6. LeetCode 212. 单词搜索 II
- **题目链接**：https://leetcode.cn/problems/word-search-ii/
- **难度**：困难
- **核心算法**：前缀树 + 回溯搜索
- **时间复杂度**：O(M * N * 4^L)，其中L是单词最大长度
- **空间复杂度**：O(K * L)，其中K是单词数量

**代码文件**：
- `Code03_WordSearchII.java` - Java实现

## 算法总结

### 前缀树的应用场景
1. **字符串检索**：快速查找字符串是否存在
2. **前缀匹配**：查找具有特定前缀的所有字符串
3. **自动补全**：搜索推荐系统
4. **拼写检查**：字典查找和纠错
5. **IP路由**：最长前缀匹配
6. **异或问题**：二进制前缀树处理异或操作

### 算法模板

#### 基本前缀树结构
```java
class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEnd;
    // 其他属性：passCount, value等
}
```

#### 二进制前缀树（处理异或）
```java
class BinaryTrieNode {
    BinaryTrieNode[] children = new BinaryTrieNode[2];
    int count;
}
```

## 工程化考量

### 1. 异常处理
- 空字符串和非法字符处理
- 边界条件检查
- 内存溢出防护

### 2. 性能优化
- 使用数组代替哈希表（固定字符集）
- 预分配内存减少动态分配
- 批量操作优化

### 3. 内存管理
- 合理设置节点大小
- 及时清理无用节点
- 考虑内存池技术

### 4. 线程安全
- 读写锁机制
- 原子操作
- 并发控制

## 语言特性差异

### Java
- **优势**：垃圾回收自动管理内存，数组实现性能高
- **劣势**：固定字符集限制，内存占用较大
- **适用场景**：企业级应用，需要稳定性和性能

### Python
- **优势**：代码简洁，字典实现灵活
- **劣势**：性能相对较低，内存占用较大
- **适用场景**：快速原型开发，脚本处理

### C++
- **优势**：性能最优，内存控制精细
- **劣势**：需要手动管理内存，代码复杂
- **适用场景**：高性能要求，系统级开发

## 调试技巧

### 1. 小规模测试
```python
# 使用简单数据验证算法正确性
test_cases = [
    (["apple", "app"], "ap", ["app", "apple"]),
    (["test"], "te", ["test"])
]
```

### 2. 打印调试信息
```java
// 打印前缀树结构
void printTrie(TrieNode node, String prefix) {
    if (node.isEnd) System.out.println(prefix);
    for (int i = 0; i < 26; i++) {
        if (node.children[i] != null) {
            printTrie(node.children[i], prefix + (char)('a' + i));
        }
    }
}
```

### 3. 性能分析
- 使用性能分析工具定位瓶颈
- 测试不同规模数据的表现
- 对比不同算法的性能差异

## 性能优化

### 1. 空间优化策略
- **压缩前缀树**：合并只有一个子节点的路径
- **懒加载**：按需创建子节点
- **内存池**：预分配节点减少碎片

### 2. 时间优化策略
- **批量操作**：减少重复遍历
- **缓存结果**：存储常用查询结果
- **并行处理**：多线程处理独立操作

### 3. 极端场景处理
- **大量短字符串**：考虑使用哈希表
- **少量长字符串**：优化深度遍历
- **重复数据**：去重处理

## 扩展学习

### 相关数据结构
1. **后缀树**：处理字符串后缀的高效数据结构
2. **AC自动机**：多模式字符串匹配算法
3. **基数树**：压缩前缀树的变种

### 实际应用案例
1. **搜索引擎**：网页索引和查询建议
2. **拼写检查**：单词纠错和补全
3. **网络路由**：IP地址最长前缀匹配
4. **基因组学**：DNA序列匹配

### 进阶题目推荐
1. **LeetCode 336. 回文对** - 前缀树 + 回文判断
2. **LeetCode 421. 数组中两个数的最大异或值** - 二进制前缀树
3. **LeetCode 472. 连接词** - 前缀树 + 动态规划
4. **LeetCode 642. 设计搜索自动补全系统** - 工业级应用

通过系统学习前缀树算法，可以掌握字符串处理的核心技术，为解决复杂字符串问题奠定坚实基础。