# Class159 - 可持久化数据结构补充题目

这个目录包含了更多关于可持久化数据结构（特别是可持久化Trie）的练习题和实现。

## 题目列表

### 1. 最大异或对 (Code08_XorPair)
- **题目描述**: 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
- **测试链接**: 
  - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
  - https://www.luogu.com.cn/problem/P4551
- **算法**: 经典Trie + 贪心
- **时间复杂度**: O(n * log M)
- **空间复杂度**: O(n * log M)

### 2. 可持久化异或最大值 (Code09_PersistentXor)
- **题目描述**: 支持在线添加数字和区间异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4735
- **算法**: 可持久化Trie
- **时间复杂度**: O((n + m) * log M)
- **空间复杂度**: O(n * log M)

### 3. 树上异或路径最大值 (Code10_XorPath)
- **题目描述**: 树上子树和路径的异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4592
- **算法**: 可持久化Trie + 树上DFS + LCA
- **时间复杂度**: O((n + m) * log M)
- **空间复杂度**: O(n * log M)

### 4. 超级钢琴 (Code11_Piano1/2)
- **题目描述**: 在给定数组中选择k个不相交的区间，使得这些区间的和的最大
- **测试链接**: https://www.luogu.com.cn/problem/P2048
- **算法**: 可持久化Trie + ST表 + 优先队列
- **时间复杂度**: O((n + k) * log n)
- **空间复杂度**: O(n * log n)

### 5. 美味 (Code12_Delicious1/2)
- **题目描述**: 区间内数字与给定值加法后再异或的最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P3293
- **算法**: 可持久化Trie
- **时间复杂度**: O((n + m) * log M)
- **空间复杂度**: O(n * log M)

### 6. 异或粽子 (Code13_Zongzi1/2)
- **题目描述**: 选择k个不相交的区间，使得这些区间的异或和的最大值之和最大
- **测试链接**: https://www.luogu.com.cn/problem/P5283
- **算法**: 可持久化Trie + 前缀异或和 + 优先队列
- **时间复杂度**: O((n + k) * log M)
- **空间复杂度**: O(n * log M)

## 已有题目 (来自原始class159)

### 1. 最大异或和 (Code01_MaxXor1/2)
- **题目描述**: 支持添加数字和区间异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4735

### 2. 字符串树 (Code02_StringTree1/2)
- **题目描述**: 树上路径字符串前缀查询
- **测试链接**: https://www.luogu.com.cn/problem/P6088

### 3. 路径和子树的异或 (Code03_PathDfnXor1/2)
- **题目描述**: 树上子树和路径异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4592

### 4. 美味 (Code04_Yummy1/2)
- **题目描述**: 区间内数字与给定值异或加法的最大值
- **测试链接**: https://www.luogu.com.cn/problem/P3293

### 5. 生成能量密度最大的宝石 (Code05_ALO1/2)
- **题目描述**: 数组中子数组次大值与其余元素异或的最大值
- **测试链接**: https://www.luogu.com.cn/problem/P4098

### 6. 异或运算 (Code06_XorOperation1/2)
- **题目描述**: 二维矩阵中区域第k大异或值
- **测试链接**: https://www.luogu.com.cn/problem/P5795

### 7. 前m大两两异或值的和 (Code07_Friends1/2)
- **题目描述**: 所有两两异或值中前k个的和
- **测试链接**: 
  - https://www.luogu.com.cn/problem/CF241B
  - https://codeforces.com/problemset/problem/241/B

## 算法要点总结

### 可持久化Trie核心思想
1. **版本控制**: 每次更新只创建新节点，其余部分继承历史版本
2. **空间优化**: 利用可持久化思想，避免完全复制数据结构
3. **异或贪心**: 在Trie上从高位到低位贪心选择使异或结果最大的路径
4. **区间查询**: 通过维护历史版本信息实现区间限制的查询

### 复杂度分析
- **时间复杂度**: 通常为O(n * log M)，其中n为元素个数，M为值域大小
- **空间复杂度**: 通常为O(n * log M)

### 应用场景
1. **异或最值问题**: 区间异或最大值查询
2. **树上问题**: 结合DFS序和LCA解决树上路径查询
3. **在线算法**: 支持动态添加元素的实时查询

## 实现语言
- Java
- C++
- Python

每道题目都提供了详细的注释和复杂度分析，确保代码的可读性和可维护性。
## 已有题目 (来自原始class159)

### 1. 最大异或和 (Code01_MaxXor1/2)
- **题目描述**: 支持添加数字和区间异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4735

### 2. 字符串树 (Code02_StringTree1/2)
- **题目描述**: 树上路径字符串前缀查询
- **测试链接**: https://www.luogu.com.cn/problem/P6088

### 3. 路径和子树的异或 (Code03_PathDfnXor1/2)
- **题目描述**: 树上子树和路径异或最大值查询
- **测试链接**: https://www.luogu.com.cn/problem/P4592

### 4. 美味 (Code04_Yummy1/2)
- **题目描述**: 区间内数字与给定值异或加法的最大值
- **测试链接**: https://www.luogu.com.cn/problem/P3293

### 5. 生成能量密度最大的宝石 (Code05_ALO1/2)
- **题目描述**: 数组中子数组次大值与其余元素异或的最大值
- **测试链接**: https://www.luogu.com.cn/problem/P4098

### 6. 异或运算 (Code06_XorOperation1/2)
- **题目描述**: 二维矩阵中区域第k大异或值
- **测试链接**: https://www.luogu.com.cn/problem/P5795

### 7. 前m大两两异或值的和 (Code07_Friends1/2)
- **题目描述**: 所有两两异或值中前k个的和
- **测试链接**: 
  - https://www.luogu.com.cn/problem/CF241B
  - https://codeforces.com/problemset/problem/241/B

## 算法要点总结

### 可持久化Trie核心思想
1. **版本控制**: 每次更新只创建新节点，其余部分继承历史版本
2. **空间优化**: 利用可持久化思想，避免完全复制数据结构
3. **异或贪心**: 在Trie上从高位到低位贪心选择使异或结果最大的路径
4. **区间查询**: 通过维护历史版本信息实现区间限制的查询

### 复杂度分析
- **时间复杂度**: 通常为O(n * log M)，其中n为元素个数，M为值域大小
- **空间复杂度**: 通常为O(n * log M)

### 应用场景
1. **异或最值问题**: 区间异或最大值查询
2. **树上问题**: 结合DFS序和LCA解决树上路径查询
3. **在线算法**: 支持动态添加元素的实时查询

## 实现语言
- Java
- C++
- Python

每道题目都提供了详细的注释和复杂度分析，确保代码的可读性和可维护性。