# Manacher算法与Z函数（扩展KMP）

本目录包含Manacher算法和Z函数（扩展KMP）的高效实现和详细讲解，以及相关题目的解决方案。这两种算法都是处理字符串匹配和回文问题的强大工具，时间复杂度均为O(n)。

## 目录结构

- **algorithm_summary.md**: 算法原理、核心思想、复杂度分析、适用场景等详细总结
- **comprehensive_problems.md**: 综合题目集，包含LeetCode、洛谷等平台的相关题目
- **Code01_Manacher.java**: Manacher算法的Java实现，包含工程化特性
- **Code02_ExpandKMP.java**: Z函数（扩展KMP）的Java实现，包含工程化特性
- **README.md**: 本说明文件

## 算法简介

### Manacher算法

Manacher算法用于高效查找字符串中的最长回文子串，时间复杂度为O(n)。它通过利用已计算的回文信息来避免重复计算，主要特点：

- 统一处理奇数和偶数长度的回文串
- 利用回文的对称性加速计算
- 维护最右回文边界来优化扩展过程

### Z函数（扩展KMP）

Z函数计算字符串的每个后缀与原字符串的最长公共前缀长度，时间复杂度为O(n)。主要应用于：

- 模式匹配问题
- 重复子串检测
- 字符串周期性分析

## 工程化特性

我们的实现包含以下工程化特性：

1. **参数验证与异常处理**：全面的输入验证和异常捕获机制
2. **性能监控与日志记录**：可配置的性能统计和详细日志
3. **调试支持**：可开关的调试模式，输出关键中间状态
4. **单元测试**：内置测试用例，确保算法正确性
5. **边界情况处理**：完善的空字符串、单字符等特殊情况处理
6. **资源管理**：安全的文件和资源关闭机制

## 使用方法

### Manacher算法

```java
// 创建Manacher算法实例
Code01_Manacher manacher = new Code01_Manacher();

// 设置调试模式
manacher.setDebugMode(true);

// 查找最长回文子串
String s = "babad";
String longestPalindrome = manacher.longestPalindrome(s);

// 统计回文子串数量
int count = manacher.countSubstrings(s);

// 计算最短回文串
String shortestPalindrome = manacher.shortestPalindrome(s);

// 运行单元测试
manacher.runUnitTests();
```

### Z函数（扩展KMP）

```java
// 创建Z函数实例
Code02_ExpandKMP zFunction = new Code02_ExpandKMP();

// 设置性能监控
zFunction.setPerformanceMonitoring(true);

// 计算构造字符串的总得分和（LeetCode 2223）
long score = zFunction.sumScores("babab");

// 计算恢复初始状态所需时间（LeetCode 3031）
int time = zFunction.minimumTimeToInitialState("abacaba", 3);

// 运行单元测试
zFunction.runUnitTests();
```

## 命令行参数

运行程序时支持以下命令行参数：

- `--debug`: 启用调试模式，输出详细日志
- `--performance`: 启用性能监控，统计操作耗时
- `--test`: 运行内置单元测试

## 性能优化

1. **局部变量优化**：减少数组访问开销
2. **懒加载数组扩展**：根据实际需求动态调整数组大小
3. **边界检查前置**：提前处理特殊情况，避免无效计算
4. **算法常数优化**：减少不必要的操作，提高执行效率

## 常见问题与解决方案

### 内存溢出
- **问题**：处理长字符串时可能出现内存溢出
- **解决**：使用`ensureArrayCapacity`方法动态调整数组大小

### 性能问题
- **问题**：大数据量下性能下降
- **解决**：启用性能监控，分析瓶颈；确保关闭调试模式

### 算法正确性
- **问题**：某些边缘情况下结果不正确
- **解决**：运行单元测试；检查输入参数是否有效

## 算法应用场景与相关题目

### Manacher算法应用场景
1. **回文检测**：判断字符串是否为回文串
2. **最长回文子串**：找到字符串中的最长回文子串
3. **回文计数**：统计字符串中回文子串的个数
4. **回文构造**：通过添加字符构造回文串

### Z函数应用场景
1. **字符串匹配**：在一个字符串中查找另一个字符串的所有出现位置
2. **周期检测**：检测字符串的周期性
3. **前缀后缀匹配**：查找既是前缀又是后缀的子串
4. **字符串压缩**：利用周期性进行字符串压缩

### 相关题目链接

#### LeetCode题目
- [LeetCode 5. 最长回文子串](https://leetcode.com/problems/longest-palindromic-substring/)
- [LeetCode 647. 回文子串](https://leetcode.com/problems/palindromic-substrings/)
- [LeetCode 214. 最短回文串](https://leetcode.com/problems/shortest-palindrome/)
- [LeetCode 1960. 两个回文子字符串长度的最大乘积](https://leetcode.com/problems/maximum-product-of-the-length-of-two-palindromic-substrings/)
- [LeetCode 2223. 构造字符串的总得分和](https://leetcode.com/problems/sum-of-scores-of-built-strings/)
- [LeetCode 3031. 将单词恢复初始状态所需的最短时间 II](https://leetcode.com/problems/minimum-time-to-revert-word-to-initial-state-ii/)
- [LeetCode 336. 回文对](https://leetcode.com/problems/palindrome-pairs/)
- [LeetCode 131. 分割回文串](https://leetcode.com/problems/palindrome-partitioning/)
- [LeetCode 132. 分割回文串 II](https://leetcode.com/problems/palindrome-partitioning-ii/)

#### Codeforces题目
- [Codeforces 126B. Password](https://codeforces.com/problemset/problem/126/B)
- [Codeforces 1326D2 - Prefix-Suffix Palindrome](https://codeforces.com/problemset/problem/1326/D2)

#### 洛谷题目
- [洛谷 P3805 【模板】manacher](https://www.luogu.com.cn/problem/P3805)
- [洛谷 P5410 【模板】扩展KMP/exKMP（Z 函数）](https://www.luogu.com.cn/problem/P5410)

#### 其他平台题目
- [UVa 11475 - Extend to Palindrome](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470)
- [SPOJ - Pattern Find](https://www.spoj.com/problems/)
- [HackerEarth - String Similarity](https://www.hackerearth.com/practice/algorithms/string-algorithm/z-algorithm/tutorial/)
- [AtCoder ABC141E - Who Says a Pun?](https://atcoder.jp/contests/abc141/tasks/abc141_e)
- [USACO 2011 November Contest, Bronze - Cow Photographs](http://www.usaco.org/index.php?page=viewproblem2&cpid=95)
- [牛客网 NC15051 - 字符串的匹配](https://www.nowcoder.com/)
- [AcWing 141. 周期](https://www.acwing.com/problem/content/143/)
- [POJ 3240 - 回文串](http://poj.org/problem?id=3240)

## 学习资源

- 详细算法原理请参考`algorithm_summary.md`
- 实战题目练习请参考`comprehensive_problems.md`
- 更多字符串算法请查看本项目其他相关章节

## 贡献指南

欢迎提交问题反馈和代码改进！如果发现bug或有更好的实现方式，请提交issue或pull request。

## 许可证

本项目采用MIT许可证。