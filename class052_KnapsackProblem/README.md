# Class074: 背包问题与字符串匹配深度解析

## 概述

Class074深入探讨了背包问题和字符串匹配两大类算法问题，包括分组背包、完全背包及其变种，以及正则表达式匹配、通配符匹配等字符串处理问题。本目录提供了来自各大算法平台的相关题目，并给出了Java、C++、Python三种语言的详细实现。

## 题目列表

### 原有题目（来自课程）
1. **分组背包模板题** - Code01_PartitionedKnapsack.java
2. **从栈中取出K个硬币的最大面值和** - Code02_MaximumValueOfKcoinsFromPiles.java
3. **完全背包模板题** - Code03_UnboundedKnapsack.java
4. **正则表达式匹配** - Code04_RegularExpressionMatching.java
5. **通配符匹配** - Code05_WildcardMatching.java
6. **购买足量干草的最小花费** - Code06_BuyingHayMinimumCost.java

### 新增题目（扩展练习）
7. **LeetCode 1155. 掷骰子的N种方法** - 分组背包思想
   - Java: Code07_DiceRollsToTargetSum.java
   - C++: Code07_DiceRollsToTargetSum.cpp
   - Python: Code07_DiceRollsToTargetSum.py

8. **HDU 1712 ACboy needs your help** - 分组背包模板题
   - Java: Code08_ACboyNeedsYourHelp.java
   - C++: Code08_ACboyNeedsYourHelp.cpp
   - Python: Code08_ACboyNeedsYourHelp.py

9. **LeetCode 322. 零钱兑换** - 完全背包求最小数量
   - Java: Code09_CoinChange.java
   - C++: Code09_CoinChange.cpp
   - Python: Code09_CoinChange.py

10. **LeetCode 279. 完全平方数** - 完全背包变种
    - Java: Code10_PerfectSquares.java
    - C++: Code10_PerfectSquares.cpp
    - Python: Code10_PerfectSquares.py

11. **LeetCode 518. 零钱兑换 II** - 完全背包求组合数
    - Java: Code11_CoinChangeII.java
    - C++: Code11_CoinChangeII.cpp
    - Python: Code11_CoinChangeII.py

### 新增题目（本次扩展）
12. **LeetCode 474. 一和零** - 二维背包问题
    - Java: Code12_OnesAndZeroes.java
    - C++: Code12_OnesAndZeroes.cpp
    - Python: Code12_OnesAndZeroes.py

13. **LeetCode 72. 编辑距离** - 字符串匹配经典问题
    - Java: Code13_EditDistance.java
    - C++: Code13_EditDistance.cpp
    - Python: Code13_EditDistance.py

14. **HDU 1712 ACboy needs your help** - 分组背包模板题（增强版）
    - Java: Code14_ACboyNeedsYourHelp.java
    - C++: Code14_ACboyNeedsYourHelp.cpp
    - Python: Code14_ACboyNeedsYourHelp.py

15. **LeetCode 377. 组合总和 Ⅳ** - 完全背包求排列数
    - Java: Code15_CombinationSumIV.java
    - C++: Code15_CombinationSumIV.cpp
    - Python: Code15_CombinationSumIV.py

16. **LeetCode 97. 交错字符串** - 字符串匹配动态规划
    - Java: Code16_InterleavingString.java
    - C++: Code16_InterleavingString.cpp
    - Python: Code16_InterleavingString.py

### 补充题目（新增实现）
17. **LeetCode 139. 单词拆分** - 字符串匹配与背包结合
    - Java: Code17_WordBreak.java
    - C++: Code17_WordBreak.cpp
    - Python: Code17_WordBreak.py

## 算法分类

### 背包问题
- **分组背包**：物品分组，每组内物品互斥，每组最多选一个
- **完全背包**：每种物品可以选择无限次
- **二维背包**：同时受到两个维度的约束
- **变种问题**：求最小数量、求组合数、求排列数、特殊约束条件

### 字符串匹配
- **正则表达式匹配**：支持'.'和'*'的匹配
- **通配符匹配**：支持'?'和'*'的匹配
- **编辑距离**：计算字符串转换的最小操作数
- **交错字符串**：验证字符串的交错组成
- **最长公共子序列**：找到两个字符串的最长公共子序列
- **单词拆分**：验证字符串是否可由字典单词组成

## 实现特点

### 1. 多语言实现
所有题目均提供Java、C++、Python三种语言实现，便于跨语言学习和对比。

### 2. 详细注释
每个实现都包含：
- 题目描述和来源链接
- 算法详解和解题思路
- 时间复杂度和空间复杂度分析
- 相关题目扩展
- 工程化考量
- 语言特性差异分析
- 调试技巧
- 优化点说明

### 3. 测试用例
每个实现都包含测试方法和测试用例，验证算法正确性。

### 4. 代码规范
- 遵循各语言的最佳实践
- 命名规范清晰易懂
- 代码结构模块化

### 5. 工程化考量
- 异常处理：处理各种边界情况和异常输入
- 性能优化：提供空间优化和时间优化版本
- 可配置性：关键参数可配置，便于扩展
- 单元测试：完整的测试用例覆盖
- 调试技巧：提供调试方法和技巧

### 6. 语言特性分析
- Java：静态类型、内存管理、性能特点
- C++：内存控制、模板支持、性能优势
- Python：动态类型、简洁语法、库支持

## 编译和运行

### Java
```bash
# 编译
javac CodeXX_Xxxxxxx.java

# 运行
java -cp . CodeXX_Xxxxxxx
```

### C++
```bash
# 编译
g++ -o CodeXX_Xxxxxxx CodeXX_Xxxxxxx.cpp

# 运行
./CodeXX_Xxxxxxx
```

### Python
```bash
# 运行
python CodeXX_Xxxxxxx.py
```

## 学习路径建议

### 第一阶段：基础掌握
1. 理解背包问题的基本概念和分类
2. 掌握分组背包和完全背包的模板实现
3. 理解字符串匹配的基本原理

### 第二阶段：深入理解
1. 分析各类背包问题的变种和应用场景
2. 掌握字符串匹配的优化技巧
3. 理解算法的时间复杂度和空间复杂度

### 第三阶段：工程实践
1. 实现自己的算法模板库
2. 解决实际问题中的背包和字符串匹配问题
3. 关注算法的工程化考量

## 相关资源

### 在线练习平台
- [LeetCode](https://leetcode.cn/)
- [洛谷](https://www.luogu.com.cn/)
- [HDU OJ](http://acm.hdu.edu.cn/)

### 参考资料
- 《算法导论》
- 《背包九讲》
- 各大算法竞赛教材

## 贡献

欢迎提交Issue或Pull Request来改进这个项目。