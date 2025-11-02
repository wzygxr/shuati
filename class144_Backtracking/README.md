# 递归与回溯算法详解

## 概述

递归和回溯是解决组合、排列、子集等问题的重要算法思想。递归通过函数自身调用来解决问题，而回溯则是在递归过程中通过"选择-探索-撤销"的方式来遍历所有可能的解空间。

## 核心知识点

### 1. 递归三要素
- **递归函数定义**：明确函数的输入输出
- **递归终止条件**：确定何时停止递归
- **递归关系**：如何通过子问题求解原问题

### 2. 回溯算法框架
```
def backtrack(路径, 选择列表):
    if 满足结束条件:
        result.add(路径)
        return
    
    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表)
        撤销选择
```

### 3. 常见题型分类
1. **子序列问题**：生成所有子序列
2. **组合问题**：从n个数中选出k个数的所有组合
3. **排列问题**：n个数的所有排列
4. **栈操作问题**：用递归实现栈的逆序和排序
5. **汉诺塔问题**：经典的递归问题
6. **字符串组合问题**：电话号码字母组合、括号生成等
7. **棋盘问题**：N皇后、解数独等
8. **路径搜索问题**：单词搜索、目标和等
9. **分区问题**：火柴拼正方形、划分为k个相等的子集等
10. **字符串分割问题**：复原IP地址、单词拆分等

## 算法复杂度分析

### 时间复杂度
- 子序列问题：O(2^n)
- 组合问题：O(C(n, k))
- 排列问题：O(n!)
- 栈操作问题：O(n^2)
- 汉诺塔问题：O(2^n)
- 字符串组合问题：O(3^m * 4^n)
- 棋盘问题：O(N!) 或 O(9^(n*n))
- 路径搜索问题：O(m*n*4^L)
- 分区问题：O(k^n) 或 O(4^n)
- 字符串分割问题：O(2^n * n)

### 空间复杂度
- 递归调用栈：O(n)
- 存储结果：根据具体问题而定

## 工程化考虑

### 异常处理
- 输入为空的处理
- 边界条件检查
- 非法输入的验证

### 性能优化
- 剪枝优化：提前终止无效分支
- 记忆化搜索：避免重复计算
- 迭代替代递归：避免栈溢出

### 代码可读性
- 函数命名清晰
- 添加详细注释
- 模块化设计

## 适用场景

1. **组合优化问题**：需要找出所有满足条件的组合
2. **搜索问题**：在解空间中搜索满足条件的解
3. **游戏AI**：如棋类游戏的走法生成
4. **编译器设计**：语法分析
5. **人工智能**：决策树搜索
6. **字符串处理**：模式匹配、文本分割等
7. **资源分配**：任务调度、资源分区等

## 面试重点

1. 理解递归的本质和执行过程
2. 掌握回溯算法的模板和应用
3. 能够分析时间和空间复杂度
4. 熟悉常见的变种题目
5. 能够进行代码优化和边界处理
6. 掌握剪枝技巧和记忆化搜索
7. 理解去重策略和状态压缩

## 文件说明

本目录包含以下Java、C++和Python实现文件：

### 基础题目（已存在于原仓库）
1. **Code01_Subsequences** - 字符串的所有子序列
2. **Code02_Combinations** - 数组组合去重
3. **Code03_Permutations** - 无重复数字全排列
4. **Code04_PermutationWithoutRepetition** - 有重复数字全排列
5. **Code05_ReverseStackWithRecursive** - 递归逆序栈
6. **Code06_SortStackWithRecursive** - 递归排序栈
7. **Code07_TowerOfHanoi** - 汉诺塔移动

### 经典题目（已实现）
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

### 新增题目（本次补充）
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

## 算法技巧总结

### 1. 回溯算法模板
```java
void backtrack(参数) {
    if (终止条件) {
        存放结果;
        return;
    }
    
    for (选择：本层集合中元素) {
        处理节点;
        backtrack(路径，选择列表); // 递归
        回溯，撤销处理结果
    }
}
```

### 2. 去重技巧
- **排序去重**：先排序，然后跳过重复元素
- **相对顺序**：确保相同元素的相对顺序，避免生成重复结果
- **哈希去重**：使用Set存储已访问的状态

### 3. 剪枝优化
- **提前终止**：当当前路径不可能得到解时提前返回
- **排序剪枝**：从大到小排序，便于提前发现不可能的情况
- **状态压缩**：使用位运算减少空间使用

### 4. 记忆化搜索
- **存储中间结果**：避免重复计算相同子问题
- **状态表示**：使用合适的数据结构表示状态

### 5. 工程化考虑
- **异常处理**：空输入、非法输入检查
- **边界条件**：极端值、边界值处理
- **性能优化**：选择合适的算法和数据结构
- **代码可读性**：清晰的命名和注释

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

有关完整项目结构和实现细节的总结，请参阅 [SUMMARY.md](SUMMARY.md) 文件。
有关所有题目的详细说明和链接，请参阅 [COMPREHENSIVE_PROBLEMS.md](COMPREHENSIVE_PROBLEMS.md) 文件。
有关每道题目的详细题解，请参阅 [SOLUTIONS.md](SOLUTIONS.md) 文件。