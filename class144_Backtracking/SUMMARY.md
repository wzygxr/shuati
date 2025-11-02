# 递归与回溯算法完整实现总结

## 项目概述

本项目对递归与回溯算法进行了全面的实现和分析，涵盖了从基础到高级的各种经典问题。所有代码都提供了Java、C++和Python三种语言的实现，并包含详细的注释和复杂度分析。

## 已实现的题目

### 1. 基础题目（已存在于原仓库）
1. **Code01_Subsequences** - 字符串的所有子序列
2. **Code02_Combinations** - 数组组合去重
3. **Code03_Permutations** - 无重复数字全排列
4. **Code04_PermutationWithoutRepetition** - 有重复数字全排列
5. **Code05_ReverseStackWithRecursive** - 递归逆序栈
6. **Code06_SortStackWithRecursive** - 递归排序栈
7. **Code07_TowerOfHanoi** - 汉诺塔移动

### 2. 经典题目（已实现）
8. **Code08_LetterCombinations** - 电话号码的字母组合 (LeetCode 17)
9. **Code09_GenerateParentheses** - 括号生成 (LeetCode 22)
10. **Code10_SudokuSolver** - 解数独 (LeetCode 37)
11. **Code11_NQueens** - N皇后 (LeetCode 51)
12. **Code12_TargetSum** - 目标和 (LeetCode 494)
13. **Code13_WordSearch** - 单词搜索 (LeetCode 79)
14. **Code14_PalindromePartitioning** - 分割回文串 (LeetCode 131)
15. **Code15_WordSearchII** - 单词搜索II (LeetCode 212)
16. **Code16_Permutations** - 全排列 (LeetCode 46)
17. **Code17_CombinationSum** - 组合总和 (LeetCode 39)

### 3. 新增题目（本次补充）
18. **Code18_SubsetsII** - 子集 II (LeetCode 90)
19. **Code19_Combinations** - 组合 (LeetCode 77)
20. **Code20_PermutationsII** - 全排列 II (LeetCode 47)
21. **Code21_CombinationSumII** - 组合总和 II (LeetCode 40)
22. **Code22_CombinationSumIII** - 组合总和 III (LeetCode 216)
23. **Code23_PermutationSequence** - 排列序列 (LeetCode 60)
24. **Code24_RestoreIPAddresses** - 复原 IP 地址 (LeetCode 93)
25. **Code25_WordBreakII** - 单词拆分 II (LeetCode 140)
26. **Code26_BeautifulArrangement** - 优美的排列 (LeetCode 526)
27. **Code27_MatchsticksToSquare** - 火柴拼正方形 (LeetCode 473)
28. **Code28_PartitionToKEqualSumSubsets** - 划分为k个相等的子集 (LeetCode 698)
29. **Code29_AdditiveNumber** - 累加数 (LeetCode 306)

## 文件结构

```
class038/
├── README.md                           # 递归与回溯算法详解
├── COMPREHENSIVE_PROBLEMS.md           # 综合题目集锦
├── SOLUTIONS.md                        # 详细题解
├── SUMMARY.md                          # 项目总结
├── Code01_Subsequences.java/cpp/py    # 字符串的所有子序列
├── Code02_Combinations.java/cpp/py    # 数组组合去重
├── Code03_Permutations.java/cpp/py    # 无重复数字全排列
├── Code04_PermutationWithoutRepetition.java/cpp/py  # 有重复数字全排列
├── Code05_ReverseStackWithRecursive.java/cpp/py     # 递归逆序栈
├── Code06_SortStackWithRecursive.java/cpp/py        # 递归排序栈
├── Code07_TowerOfHanoi.java/cpp/py                  # 汉诺塔移动
├── Code08_LetterCombinations.java/cpp/py            # 电话号码的字母组合
├── Code09_GenerateParentheses.java/cpp/py           # 括号生成
├── Code10_SudokuSolver.java/cpp/py                  # 解数独
├── Code11_NQueens.java/cpp/py                       # N皇后
├── Code12_TargetSum.java/cpp/py                     # 目标和
├── Code13_WordSearch.java/cpp/py                    # 单词搜索
├── Code14_PalindromePartitioning.java/cpp/py        # 分割回文串
├── Code15_WordSearchII.java/cpp/py                  # 单词搜索II
├── Code16_Permutations.java/cpp/py                  # 全排列
├── Code17_CombinationSum.java/cpp/py                # 组合总和
├── Code18_SubsetsII.java/cpp/py                     # 子集 II
├── Code19_Combinations.java/cpp/py                  # 组合
├── Code20_PermutationsII.java/cpp/py                # 全排列 II
├── Code21_CombinationSumII.java/cpp/py             # 组合总和 II
├── Code22_CombinationSumIII.java/cpp/py             # 组合总和 III
├── Code23_PermutationSequence.java/cpp/py           # 排列序列
├── Code24_RestoreIPAddresses.java/cpp/py            # 复原 IP 地址
├── Code25_WordBreakII.java/cpp/py                   # 单词拆分 II
├── Code26_BeautifulArrangement.java/cpp/py          # 优美的排列
├── Code27_MatchsticksToSquare.java/cpp/py          # 火柴拼正方形
├── Code28_PartitionToKEqualSumSubsets.java/cpp/py   # 划分为k个相等的子集
└── Code29_AdditiveNumber.java/cpp/py               # 累加数
```

## 技术特点

### 1. 多语言实现
- 所有题目均提供Java、C++、Python三种语言实现
- 保持各语言版本算法逻辑一致性
- 遵循各语言最佳实践和编码规范

### 2. 详细注释
- 每个文件都包含完整的题目描述
- 算法思路详细解释
- 时间复杂度和空间复杂度分析
- 关键步骤注释说明

### 3. 完整测试
- 每个文件都包含测试用例
- 覆盖边界条件和特殊情况
- 提供清晰的输入输出示例

### 4. 工程化考虑
- 异常处理：空输入、非法输入检查
- 性能优化：剪枝、提前终止等优化策略
- 代码可读性：清晰的变量命名和模块化设计

## 算法分类

### 1. 子序列问题
- 字符串的所有子序列
- LeetCode 78. 子集
- LeetCode 90. 子集 II

### 2. 组合问题
- 数组组合去重
- LeetCode 77. 组合
- LeetCode 39. 组合总和
- LeetCode 40. 组合总和 II
- LeetCode 216. 组合总和 III

### 3. 排列问题
- 无重复数字全排列
- 有重复数字全排列
- LeetCode 46. 全排列
- LeetCode 47. 全排列 II
- LeetCode 60. 排列序列

### 4. 栈操作问题
- 递归逆序栈
- 递归排序栈

### 5. 汉诺塔问题
- 汉诺塔移动

### 6. 字符串组合问题
- 电话号码的字母组合
- 括号生成

### 7. 棋盘问题
- 解数独
- N皇后

### 8. 路径搜索问题
- 目标和
- 单词搜索
- 单词搜索II

### 9. 字符串分割问题
- 分割回文串
- 复原IP地址
- 单词拆分II
- 累加数

### 10. 分区问题
- 火柴拼正方形
- 划分为k个相等的子集

### 11. 约束满足问题
- 优美的排列

## 复杂度分析

### 时间复杂度
- 子序列问题：O(2^n * n)
- 组合问题：O(C(n, k) * k)
- 排列问题：O(n! * n)
- 栈操作问题：O(n^2)
- 汉诺塔问题：O(2^n)
- 电话号码字母组合：O(3^m * 4^n)
- 括号生成：O(4^n / sqrt(n))
- 解数独：O(9^(n*n))
- N皇后：O(N!)
- 目标和：O(2^n) 回溯 / O(n * sum) 动态规划
- 单词搜索：O(m*n*4^L)
- 分割回文串：O(N * 2^N)
- 单词搜索II：O(m*n*4^L)
- 全排列：O(N * N!)
- 组合总和：O(N^(T/M))
- 子集II：O(n * 2^n)
- 组合：O(C(n, k) * k)
- 全排列II：O(n * n!)
- 组合总和II：O(2^n)
- 组合总和III：O(C(9, k))
- 排列序列：O(n^2)
- 复原IP地址：O(3^4) = O(81)
- 单词拆分II：O(2^n * n)
- 优美的排列：O(n!)
- 火柴拼正方形：O(4^n)
- 划分为k个相等的子集：O(k^n)
- 累加数：O(n^3)

### 空间复杂度
- 递归调用栈：O(n) 到 O(n^2)
- 存储结果：根据具体问题而定

## 面试重点

1. **理解递归的本质**：函数调用栈、递归基条件
2. **掌握回溯算法模板**：选择-探索-撤销选择
3. **复杂度分析能力**：时间复杂度和空间复杂度计算
4. **优化技巧**：剪枝、去重、记忆化搜索
5. **边界处理**：空输入、极端值处理
6. **工程化思维**：异常处理、代码可读性、测试覆盖
7. **多解法对比**：能够分析不同解法的优缺点
8. **实际问题应用**：能够将算法应用于实际问题解决

## 使用说明

### 编译和运行

#### Java
```bash
javac class038/CodeXX_XXX.java
java -cp . class038.CodeXX_XXX
```

#### C++
```bash
g++ -std=c++11 class038/CodeXX_XXX.cpp -o class038/CodeXX_XXX.exe
class038/CodeXX_XXX.exe
```

#### Python
```bash
python class038/CodeXX_XXX.py
```

## 总结

本项目完整实现了递归与回溯算法的经典题目，涵盖了从基础到高级的各种应用场景。通过多语言实现和详细注释，帮助学习者深入理解算法本质，掌握工程化实现技巧，为算法面试和实际开发打下坚实基础。

通过系统学习本项目，您将能够：
1. 熟练掌握递归与回溯算法的核心思想
2. 理解各种经典问题的解决思路
3. 掌握算法复杂度分析和优化技巧
4. 具备解决实际工程问题的能力
5. 在算法面试中表现出色

本项目不仅提供了完整的代码实现，还包含了详细的算法分析和工程化考虑，是学习递归与回溯算法的理想资源。