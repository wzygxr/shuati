# Class039: 递归处理嵌套结构算法

## 介绍

本类别专注于递归处理嵌套结构的算法问题，包括表达式计算、字符串解码、化学式解析等。通过递归技术，我们可以优雅地处理具有自相似性的复杂问题。

## 核心知识点

1. **递归基础**：理解递归的本质和执行过程
2. **嵌套结构识别**：识别问题中的嵌套模式
3. **状态管理**：使用全局变量或返回值管理递归状态
4. **边界条件处理**：正确定义递归的终止条件

## 题目列表（完整三语言实现）

### 原始题目（Java/Python/C++）

1. [Code01_BasicCalculatorIII.java](Code01_BasicCalculatorIII.java) / [.py](Code01_BasicCalculatorIII.py) / [.cpp](Code01_BasicCalculatorIII.cpp) - 含有嵌套的表达式求值
2. [Code02_DecodeString.java](Code02_DecodeString.java) / [.py](Code02_DecodeString.py) / [.cpp](Code02_DecodeString.cpp) - 含有嵌套的字符串解码
3. [Code03_NumberOfAtoms.java](Code03_NumberOfAtoms.java) / [.py](Code03_NumberOfAtoms.py) / [.cpp](Code03_NumberOfAtoms.cpp) - 含有嵌套的分子式求原子数量

### 补充题目（Java/Python/C++）

4. [Code04_BasicCalculator.java](Code04_BasicCalculator.java) / [.py](Code04_BasicCalculator.py) / [.cpp](Code04_BasicCalculator.cpp) - LeetCode 224. Basic Calculator (基本计算器)
5. [Code05_BasicCalculatorII.java](Code05_BasicCalculatorII.java) / [.py](Code05_BasicCalculatorII.py) / [.cpp](Code05_BasicCalculatorII.cpp) - LeetCode 227. Basic Calculator II (基本计算器 II)
6. [Code06_BasicCalculatorIII.java](Code06_BasicCalculatorIII.java) / [.py](Code06_BasicCalculatorIII.py) / [.cpp](Code06_BasicCalculatorIII.cpp) - LeetCode 772. Basic Calculator III (基本计算器 III)
7. [Code07_DecodeString.java](Code07_DecodeString.java) / [.py](Code07_DecodeString.py) / [.cpp](Code07_DecodeString.cpp) - LeetCode 394. Decode String (字符串解码)
8. [Code08_NumberOfAtoms.java](Code08_NumberOfAtoms.java) / [.py](Code08_NumberOfAtoms.py) / [.cpp](Code08_NumberOfAtoms.cpp) - LeetCode 726. Number of Atoms (原子的数量)
9. [Code09_Recursion3.java](Code09_Recursion3.java) / [.py](Code09_Recursion3.py) / [.cpp](Code09_Recursion3.cpp) - HackerRank Day 9: Recursion 3
10. [Code10_NestingBrackets.java](Code10_NestingBrackets.java) / [.py](Code10_NestingBrackets.py) / [.cpp](Code10_NestingBrackets.cpp) - UVA 551 Nesting a Bunch of Brackets
11. [Code11_NestedListWeightSum.java](Code11_NestedListWeightSum.java) / [.py](Code11_NestedListWeightSum.py) / [.cpp](Code11_NestedListWeightSum.cpp) - LeetCode 339. Nested List Weight Sum (嵌套列表权重和)
12. [Code12_NestedListWeightSumII.java](Code12_NestedListWeightSumII.java) / [.py](Code12_NestedListWeightSumII.py) / [.cpp](Code12_NestedListWeightSumII.cpp) - LeetCode 364. Nested List Weight Sum II (嵌套列表权重和 II)
13. [Code13_KillProcess.java](Code13_KillProcess.java) / [.py](Code13_KillProcess.py) / [.cpp](Code13_KillProcess.cpp) - LeetCode 582. Kill Process (杀死进程)
14. [Code14_FlattenNestedListIterator.java](Code14_FlattenNestedListIterator.java) / [.py](Code14_FlattenNestedListIterator.py) / [.cpp](Code14_FlattenNestedListIterator.cpp) - LeetCode 341. Flatten Nested List Iterator (扁平化嵌套列表迭代器)
15. [Code15_AllPathsFromSourceToTarget.java](Code15_AllPathsFromSourceToTarget.java) / [.py](Code15_AllPathsFromSourceToTarget.py) / [.cpp](Code15_AllPathsFromSourceToTarget.cpp) - LeetCode 797. All Paths From Source to Target (所有可能的路径)
16. [Code16_NaryTreeLevelOrderTraversal.java](Code16_NaryTreeLevelOrderTraversal.java) / [.py](Code16_NaryTreeLevelOrderTraversal.py) / [.cpp](Code16_NaryTreeLevelOrderTraversal.cpp) - LeetCode 429. N-ary Tree Level Order Traversal (N叉树的层序遍历)
17. [Code17_MaximumDepthOfBinaryTree.java](Code17_MaximumDepthOfBinaryTree.java) / [.py](Code17_MaximumDepthOfBinaryTree.py) / [.cpp](Code17_MaximumDepthOfBinaryTree.cpp) - LeetCode 104. Maximum Depth of Binary Tree (二叉树的最大深度)
18. [Code18_SameTree.java](Code18_SameTree.java) / [.py](Code18_SameTree.py) / [.cpp](Code18_SameTree.cpp) - LeetCode 100. Same Tree (相同的树)
19. [Code19_Permutations.java](Code19_Permutations.java) / [.py](Code19_Permutations.py) / [.cpp](Code19_Permutations.cpp) - LeetCode 46. Permutations (全排列)
20. [Code20_Subsets.java](Code20_Subsets.java) / [.py](Code20_Subsets.py) / [.cpp](Code20_Subsets.cpp) - LeetCode 78. Subsets (子集)

### 新增补充题目（来自各大算法平台）

21. **LeetCode 856. Score of Parentheses** - 括号的分数计算
22. **LeetCode 385. Mini Parser** - 迷你语法分析器
23. **LeetCode 20. Valid Parentheses** - 有效的括号
24. **LeetCode 32. Longest Valid Parentheses** - 最长有效括号
25. **POJ 2955 Brackets** - 最长括号匹配子序列
26. **LeetCode 50. Pow(x, n)** - 快速幂递归实现
27. **LeetCode 70. Climbing Stairs** - 爬楼梯递归解法
28. **LeetCode 659. Encode and Decode Strings** - 字符串编码解码

### 新增实现题目（Java/Python/C++完整实现）

29. [LC856_ScoreOfParentheses.java](LC856_ScoreOfParentheses.java) / [.py](LC856_ScoreOfParentheses.py) / [.cpp](LC856_ScoreOfParentheses.cpp) - LeetCode 856. Score of Parentheses (括号的分数)
30. [LC20_ValidParentheses.java](LC20_ValidParentheses.java) / [.py](LC20_ValidParentheses.py) / [.cpp](LC20_ValidParentheses.cpp) - LeetCode 20. Valid Parentheses (有效的括号)
31. [LC32_LongestValidParentheses.java](LC32_LongestValidParentheses.java) / [.py](LC32_LongestValidParentheses.py) / [.cpp](LC32_LongestValidParentheses.cpp) - LeetCode 32. Longest Valid Parentheses (最长有效括号)
32. [POJ2955_Brackets.java](POJ2955_Brackets.java) / [.py](POJ2955_Brackets.py) / [.cpp](POJ2955_Brackets.cpp) - POJ 2955 Brackets (最长括号匹配子序列)
33. [UVA551_NestingBrackets.java](UVA551_NestingBrackets.java) / [.py](UVA551_NestingBrackets.py) / [.cpp](UVA551_NestingBrackets.cpp) - UVA 551 Nesting a Bunch of Brackets (多种类型括号匹配)
34. [HR_Day9_Recursion3.java](HR_Day9_Recursion3.java) / [.py](HR_Day9_Recursion3.py) / [.cpp](HR_Day9_Recursion3.cpp) - HackerRank Day 9: Recursion 3 (阶乘递归)
35. [LC50_Pow.java](LC50_Pow.java) / [.py](LC50_Pow.py) / [.cpp](LC50_Pow.cpp) - LeetCode 50. Pow(x, n) (快速幂递归)
36. [LC70_ClimbingStairs.java](LC70_ClimbingStairs.java) / [.py](LC70_ClimbingStairs.py) / [.cpp](LC70_ClimbingStairs.cpp) - LeetCode 70. Climbing Stairs (爬楼梯递归)
37. [LintCode659_EncodeDecodeStrings.java](LintCode659_EncodeDecodeStrings.java) / [.py](LintCode659_EncodeDecodeStrings.py) / [.cpp](LintCode659_EncodeDecodeStrings.cpp) - LintCode 659. Encode and Decode Strings (字符串编码解码)

## 文档资料

- [ADDITIONAL_PROBLEMS.md](ADDITIONAL_PROBLEMS.md) - 补充题目列表
- [SOLUTIONS_SUMMARY.md](SOLUTIONS_SUMMARY.md) - 解决方案总结

## 算法特点

### 1. 递归处理嵌套结构
- 使用递归自然地处理嵌套的括号、方括号等结构
- 通过全局变量管理递归过程中的状态

### 2. 运算符优先级处理
- 使用栈或特殊逻辑处理不同运算符的优先级
- 乘除法优先于加减法

### 3. 字符串处理技巧
- 构建数字：逐位处理字符转换为整数
- 字符串重复：根据数字重复字符串内容

### 4. 化学式解析
- 识别原子名称（大写字母开头，后跟小写字母）
- 处理原子数量和括号嵌套

## 时间与空间复杂度分析

大多数算法的时间复杂度为O(n)，其中n为输入字符串的长度。空间复杂度取决于递归深度和额外数据结构的使用，通常为O(n)。

## 工程化应用

1. **表达式计算**：在计算器、公式引擎中应用
2. **模板引擎**：处理嵌套的模板变量和逻辑
3. **配置文件解析**：解析具有嵌套结构的配置文件
4. **数据格式解析**：解析JSON、XML等具有嵌套结构的数据格式

## 学习建议

1. **掌握递归基础**：深入理解递归的执行过程和内存模型
2. **练习经典题目**：从简单到复杂逐步练习相关题目
3. **分析算法本质**：理解每种解法的设计思路和适用场景
4. **工程化实践**：在实际项目中应用这些算法技巧