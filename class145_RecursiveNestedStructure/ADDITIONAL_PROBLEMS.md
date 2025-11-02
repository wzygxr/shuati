# 递归处理嵌套结构算法题目补充列表

## 一、表达式计算类

### 1. LeetCode 224. Basic Calculator (基本计算器)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/basic-calculator/
- **区别**: 只包含加减法和括号
- **核心**: 递归处理嵌套括号结构

### 2. LeetCode 227. Basic Calculator II (基本计算器 II)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/basic-calculator-ii/
- **区别**: 包含加减乘除，但不包含括号
- **核心**: 处理运算符优先级

### 3. LeetCode 772. Basic Calculator III (基本计算器 III)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/basic-calculator-iii/
- **区别**: 包含加减乘除和括号，是这三题中最复杂的
- **核心**: 综合处理运算符优先级和嵌套结构

### 4. LeetCode 856. Score of Parentheses (括号的分数)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/score-of-parentheses/
- **区别**: 计算括号的分数，((())())这种结构的计算
- **核心**: 递归计算嵌套括号的分数

### 5. LeetCode 385. Mini Parser (迷你语法分析器)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/mini-parser/
- **区别**: 解析嵌套的整数列表结构
- **核心**: 递归解析嵌套数据结构

## 二、字符串解码类

### 6. LeetCode 394. Decode String (字符串解码)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/decode-string/
- **区别**: 解码字符串而不是统计原子数量
- **核心**: 递归处理嵌套字符串结构

### 7. LeetCode 659. Encode and Decode Strings
- **来源**: LintCode
- **网址**: https://www.lintcode.com/problem/659/
- **区别**: 设计算法将字符串列表编码为单个字符串并解码
- **核心**: 字符串编码解码技术

## 三、化学式解析类

### 8. LeetCode 726. Number of Atoms (原子的数量)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/number-of-atoms/
- **区别**: 处理化学式中的原子计数，结构类似但需要统计不同原子的数量
- **核心**: 递归处理嵌套化学式结构

## 四、括号匹配类

### 9. LeetCode 20. Valid Parentheses (有效的括号)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/valid-parentheses/
- **区别**: 验证括号字符串是否有效
- **核心**: 使用栈验证括号匹配

### 10. LeetCode 32. Longest Valid Parentheses (最长有效括号)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/longest-valid-parentheses/
- **区别**: 找到最长的有效括号子串
- **核心**: 动态规划或栈处理括号匹配

### 11. UVA 551 Nesting a Bunch of Brackets
- **来源**: UVA Online Judge
- **网址**: https://onlinejudge.org/external/5/551.pdf
- **区别**: 处理多种类型的括号匹配
- **核心**: 验证多种类型括号的正确嵌套

### 12. POJ 2955 Brackets
- **来源**: POJ
- **网址**: http://poj.org/problem?id=2955
- **区别**: 找到最长的正确匹配括号子序列
- **核心**: 区间动态规划处理括号匹配

## 五、递归基础类

### 13. HackerRank Day 9: Recursion 3
- **来源**: HackerRank
- **网址**: https://www.hackerrank.com/challenges/30-recursion/problem
- **区别**: 计算阶乘的递归实现
- **核心**: 递归基础概念

### 14. LeetCode 50. Pow(x, n)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/powx-n/
- **区别**: 快速幂的递归实现
- **核心**: 分治法与递归优化

### 15. LeetCode 70. Climbing Stairs
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/climbing-stairs/
- **区别**: 爬楼梯问题的递归解法
- **核心**: 递归与动态规划的转换

## 六、嵌套列表处理类

### 16. LeetCode 339. Nested List Weight Sum (嵌套列表权重和)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/nested-list-weight-sum/
- **区别**: 计算嵌套列表中所有整数的加权和，权重为深度
- **核心**: 递归处理嵌套结构并计算加权和

### 17. LeetCode 364. Nested List Weight Sum II (嵌套列表权重和 II)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/nested-list-weight-sum-ii/
- **区别**: 反向加权和，深度最大的权重为1
- **核心**: 递归计算最大深度或使用迭代方法累积权重

### 18. LeetCode 582. Kill Process (杀死进程)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/kill-process/
- **区别**: 树形结构中杀死进程及其所有子进程
- **核心**: 深度优先搜索或广度优先搜索遍历树形结构

### 19. LeetCode 341. Flatten Nested List Iterator (扁平化嵌套列表迭代器)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/flatten-nested-list-iterator/
- **区别**: 设计迭代器扁平化嵌套列表
- **核心**: 惰性计算和深度优先搜索的迭代实现

## 七、图的递归遍历类

### 20. LeetCode 797. All Paths From Source to Target (所有可能的路径)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/all-paths-from-source-to-target/
- **区别**: 寻找有向无环图中从源节点到目标节点的所有路径
- **核心**: 递归深度优先搜索和回溯算法

## 八、树的递归遍历类

### 21. LeetCode 429. N-ary Tree Level Order Traversal (N叉树的层序遍历)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
- **区别**: 按层级遍历N叉树，收集每个层级的节点值
- **核心**: 递归深度优先搜索或迭代广度优先搜索实现层序遍历

### 22. LeetCode 104. Maximum Depth of Binary Tree (二叉树的最大深度)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
- **区别**: 计算二叉树从根节点到最远叶子节点的最长路径上的节点数
- **核心**: 递归分解问题，计算左右子树的最大深度

### 23. LeetCode 100. Same Tree (相同的树)
- **来源**: LeetCode
- **网址**: https://leetcode.cn/problems/same-tree/
- **区别**: 检验两棵二叉树是否在结构上相同并且节点值相同
- **核心**: 递归地比较两棵树的对应节点

## 九、回溯算法类

回溯算法是一种通过探索所有可能的候选解来找出所有解的算法。如果候选解被确认不是一个解，回溯算法会通过在上一步进行一些变化来舍弃该解，即回溯并且尝试另一种可能。

1. **Code19_Permutations** - LeetCode 46. 全排列
   - 题目：给定一个不含重复数字的数组 nums，返回其所有可能的全排列。
   - 算法：使用回溯算法生成所有可能的排列，实现了两种方式：使用used数组标记已选元素和通过交换元素实现回溯。
   - 时间复杂度：O(N * N!)，空间复杂度：O(N)
   - 文件：
     - [Code19_Permutations.java](Code19_Permutations.java)
     - [Code19_Permutations.py](Code19_Permutations.py)
     - [Code19_Permutations.cpp](Code19_Permutations.cpp)

2. **Code20_Subsets** - LeetCode 78. 子集
   - 题目：给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
   - 算法：实现了三种方法：回溯算法、位运算和迭代增量法。
   - 时间复杂度：O(N * 2^N)，空间复杂度：O(N)
   - 文件：
     - [Code20_Subsets.java](Code20_Subsets.java)
     - [Code20_Subsets.py](Code20_Subsets.py)
     - [Code20_Subsets.cpp](Code20_Subsets.cpp)

### 新增实现题目（Java/Python/C++完整实现）

3. **LC856_ScoreOfParentheses** - LeetCode 856. Score of Parentheses (括号的分数)
   - 题目：给定一个平衡括号字符串 S，按下述规则计算该字符串的分数。
   - 算法：使用栈处理嵌套括号结构，计算括号的分数。
   - 时间复杂度：O(N)，空间复杂度：O(N)
   - 文件：
     - [LC856_ScoreOfParentheses.java](LC856_ScoreOfParentheses.java)
     - [LC856_ScoreOfParentheses.py](LC856_ScoreOfParentheses.py)
     - [LC856_ScoreOfParentheses.cpp](LC856_ScoreOfParentheses.cpp)

4. **LC20_ValidParentheses** - LeetCode 20. Valid Parentheses (有效的括号)
   - 题目：给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s，判断字符串是否有效。
   - 算法：使用栈验证括号匹配。
   - 时间复杂度：O(N)，空间复杂度：O(N)
   - 文件：
     - [LC20_ValidParentheses.java](LC20_ValidParentheses.java)
     - [LC20_ValidParentheses.py](LC20_ValidParentheses.py)
     - [LC20_ValidParentheses.cpp](LC20_ValidParentheses.cpp)

5. **LC32_LongestValidParentheses** - LeetCode 32. Longest Valid Parentheses (最长有效括号)
   - 题目：给定一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
   - 算法：使用栈处理括号匹配，计算最长有效括号子串的长度。
   - 时间复杂度：O(N)，空间复杂度：O(N)
   - 文件：
     - [LC32_LongestValidParentheses.java](LC32_LongestValidParentheses.java)
     - [LC32_LongestValidParentheses.py](LC32_LongestValidParentheses.py)
     - [LC32_LongestValidParentheses.cpp](LC32_LongestValidParentheses.cpp)

6. **POJ2955_Brackets** - POJ 2955 Brackets (最长括号匹配子序列)
   - 题目：给定一个括号序列，求最长的合法的子序列的长度。
   - 算法：使用区间动态规划处理括号匹配。
   - 时间复杂度：O(N^3)，空间复杂度：O(N^2)
   - 文件：
     - [POJ2955_Brackets.java](POJ2955_Brackets.java)
     - [POJ2955_Brackets.py](POJ2955_Brackets.py)
     - [POJ2955_Brackets.cpp](POJ2955_Brackets.cpp)

7. **UVA551_NestingBrackets** - UVA 551 Nesting a Bunch of Brackets (多种类型括号匹配)
   - 题目：验证多种类型的括号字符串是否有效。
   - 算法：使用栈验证多种类型括号的正确嵌套。
   - 时间复杂度：O(N)，空间复杂度：O(N)
   - 文件：
     - [UVA551_NestingBrackets.java](UVA551_NestingBrackets.java)
     - [UVA551_NestingBrackets.py](UVA551_NestingBrackets.py)
     - [UVA551_NestingBrackets.cpp](UVA551_NestingBrackets.cpp)

8. **HR_Day9_Recursion3** - HackerRank Day 9: Recursion 3 (阶乘递归)
   - 题目：计算阶乘的递归实现。
   - 算法：使用递归计算阶乘。
   - 时间复杂度：O(N)，空间复杂度：O(N)
   - 文件：
     - [HR_Day9_Recursion3.java](HR_Day9_Recursion3.java)
     - [HR_Day9_Recursion3.py](HR_Day9_Recursion3.py)
     - [HR_Day9_Recursion3.cpp](HR_Day9_Recursion3.cpp)

9. **LC50_Pow** - LeetCode 50. Pow(x, n) (快速幂递归)
   - 题目：实现 pow(x, n) ，即计算x 的整数 n 次幂函数。
   - 算法：使用快速幂算法的递归实现。
   - 时间复杂度：O(log N)，空间复杂度：O(log N)
   - 文件：
     - [LC50_Pow.java](LC50_Pow.java)
     - [LC50_Pow.py](LC50_Pow.py)
     - [LC50_Pow.cpp](LC50_Pow.cpp)

10. **LC70_ClimbingStairs** - LeetCode 70. Climbing Stairs (爬楼梯递归)
    - 题目：你需要爬 n 阶楼梯，每次可以爬 1 或 2 个台阶，有多少种不同的方法可以爬到楼顶。
    - 算法：使用记忆化递归解决爬楼梯问题。
    - 时间复杂度：O(N)，空间复杂度：O(N)
    - 文件：
      - [LC70_ClimbingStairs.java](LC70_ClimbingStairs.java)
      - [LC70_ClimbingStairs.py](LC70_ClimbingStairs.py)
      - [LC70_ClimbingStairs.cpp](LC70_ClimbingStairs.cpp)

11. **LintCode659_EncodeDecodeStrings** - LintCode 659. Encode and Decode Strings (字符串编码解码)
    - 题目：设计一个将字符串列表编码为字符串的算法，并能解码回原始的字符串列表。
    - 算法：使用长度+#的格式进行字符串的编码和解码。
    - 时间复杂度：O(N)，空间复杂度：O(N)
    - 文件：
      - [LintCode659_EncodeDecodeStrings.java](LintCode659_EncodeDecodeStrings.java)
      - [LintCode659_EncodeDecodeStrings.py](LintCode659_EncodeDecodeStrings.py)
      - [LintCode659_EncodeDecodeStrings.cpp](LintCode659_EncodeDecodeStrings.cpp)