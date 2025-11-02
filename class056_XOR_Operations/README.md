# Class030 - 异或运算(XOR)专题

## 概述

本目录专注于异或运算(XOR)在算法中的应用。异或运算是位运算中的一种重要操作，具有许多独特的性质，可以用来解决各种算法问题。

## 文件结构

- `Code01_SwapExclusiveOr.java` - 使用异或运算交换两个数
- `Code02_GetMaxWithoutJudge.java` - 不用判断语句求两数最大值
- `Code03_MissingNumber.java` - 找到缺失的数字
- `Code04_SingleNumber.java` - 数组中唯一出现一次的元素
- `Code05_DoubleNumber1.java` - 数组中两个出现一次的元素(Java版)
- `Code05_DoubleNumber2.java` - 数组中两个出现一次的元素(C++版)
- `Code06_OneKindNumberLessMtimes.java` - 数组中出现次数少于m次的元素
- `Code07_XorInRange.java` - 异或运算经典题目集合
- `Code08_XorAdvanced.java` - 异或运算高级应用
- `Code09_XorPropertiesAndTricks.java` - 异或运算性质和技巧
- `Code10_XorAdvancedProblems.java` - 异或运算高级题目实现(Java版)
- `Code10_XorAdvancedProblems.cpp` - 异或运算高级题目实现(C++版)
- `Code10_XorAdvancedProblems.py` - 异或运算高级题目实现(Python版)

## 异或运算基本性质

1. **归零律**: `a ^ a = 0` - 任何数与自身异或结果为0
2. **恒等律**: `a ^ 0 = a` - 任何数与0异或结果为其本身
3. **交换律**: `a ^ b = b ^ a` - 异或运算满足交换律
4. **结合律**: `(a ^ b) ^ c = a ^ (b ^ c)` - 异或运算满足结合律
5. **自反性**: `a ^ b ^ a = b` - 一个数与另一个数异或两次等于自身

## 核心技巧

### 1. 交换两个数
```java
a = a ^ b;
b = a ^ b;
a = a ^ b;
```

### 2. 找到数组中唯一出现奇数次的元素
利用归零律和恒等律，出现偶数次的元素异或后变为0，出现奇数次的元素异或后保留本身。

### 3. 找到数组中两个唯一出现奇数次的元素
1. 对所有元素异或得到 `a ^ b`
2. 找到 `a ^ b` 中最右边的1位，根据该位将数组分为两组
3. 两个目标数分别在两组中，对每组分别异或得到两个数

### 4. Brian Kernighan算法
- `n & (-n)` 可以提取出最右边的1位
- `n & (n-1)` 可以清除最右边的1位

## 经典题目

### 基础题目

1. **LeetCode 136. Single Number** - 数组中唯一出现一次的元素
2. **LeetCode 268. Missing Number** - 缺失的数字
3. **LeetCode 461. Hamming Distance** - 汉明距离
4. **LeetCode 1486. XOR Operation in an Array** - 数组异或操作

### 进阶题目

1. **LeetCode 260. Single Number III** - 数组中两个出现一次的元素
2. **LeetCode 137. Single Number II** - 数组中唯一出现一次的元素(其他都出现三次)
3. **LeetCode 421. Maximum XOR of Two Numbers in an Array** - 最大异或值
4. **LeetCode 1310. XOR Queries of a Subarray** - 子数组异或查询
5. **LeetCode 476. Number Complement** - 数字的补数
6. **LeetCode 693. Binary Number with Alternating Bits** - 交替位二进制数
7. **LeetCode 1707. Maximum XOR With an Element From Array** - 与数组中元素的最大异或值
8. **LeetCode 1803. Count Pairs With XOR in a Range** - 统计异或值在范围内的数对有多少
9. **LintCode 1490. Maximum XOR** - 数组中两个数的最大异或值 II
10. **牛客网 NC152. 数组中两个数的最大异或值** - 牛客网NC152最大异或值

### 更多题目

查看 [ADDITIONAL_XOR_PROBLEMS.md](ADDITIONAL_XOR_PROBLEMS.md) 获取来自各大算法平台的异或运算相关题目清单。
查看 [XOR_PROBLEMS_IMPLEMENTED.md](XOR_PROBLEMS_IMPLEMENTED.md) 获取已实现的异或运算题目列表。

## 复杂度分析

| 操作 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| 基础异或运算 | O(1) | O(1) |
| 数组遍历异或 | O(n) | O(1) |
| 前缀异或数组构建 | O(n) | O(n) |
| 前缀树方法 | O(n) | O(n) |
| 前缀树查询最大异或值 | O(n * log(max)) | O(n * log(max)) |
| 前缀树统计异或范围数对 | O(n * log(max)) | O(n * log(max)) |

## 工程化考虑

1. **边界条件处理**:
   - 空数组检查
   - 单元素数组处理
   - 整数溢出检查

2. **性能优化**:
   - 使用位运算替代乘除法
   - 利用短路求值
   - 避免重复计算

3. **可读性**:
   - 添加详细注释
   - 使用有意义的变量名
   - 拆分复杂逻辑

4. **前缀树优化**:
   - 内存管理：及时释放前缀树节点
   - 查询优化：使用贪心策略查找最大异或值
   - 离线处理：对查询进行排序以提高效率

## 实际应用场景

1. **加密算法** - 利用自反性进行简单加密
2. **错误检测** - CRC校验码
3. **数据备份** - RAID磁盘阵列
4. **算法优化** - 替代临时变量交换数值
5. **布隆过滤器** - 位操作实现
6. **网络路由** - 前缀树用于IP地址查找
7. **数据库索引** - 前缀树用于快速查找
8. **自动补全** - 前缀树用于字符串匹配

## 学习建议

1. **掌握基本性质** - 熟练掌握异或运算的五个基本性质
2. **练习经典题目** - 从简单到复杂逐步练习相关题目
3. **理解应用场景** - 了解异或运算在实际中的应用
4. **总结解题模式** - 归纳常见题型的解题思路
5. **扩展相关知识** - 学习其他位运算技巧
6. **掌握高级数据结构** - 学习前缀树(Trie)在异或运算中的应用
7. **理解离线算法** - 掌握查询排序和离线处理技巧

## 参考资料

1. 《算法导论》位运算章节
2. LeetCode相关题目解析
3. 《编程珠玑》位运算技巧
4. 各大公司面试真题

## 新增题目解法详解

### LeetCode 1707. Maximum XOR With an Element From Array
**核心思想**: 使用前缀树(Trie)配合离线处理
- 将查询按照mi排序，数组按值排序
- 对于每个查询，将所有小于等于mi的数插入前缀树
- 在前缀树中查找与xi异或的最大值

### LeetCode 1803. Count Pairs With XOR in a Range
**核心思想**: 使用前缀树配合容斥原理
- 利用容斥原理：count(low, high) = count(0, high) - count(0, low-1)
- 对于每个数，在前缀树中查找与其异或结果小于等于某个值的数的个数

### LintCode 1490. Maximum XOR & 牛客网 NC152. 数组中两个数的最大异或值
**核心思想**: 使用前缀树(Trie)贪心策略
- 将所有数字的二进制表示插入前缀树
- 对于每个数字，在前缀树中查找能产生最大异或值的数字
- 贪心策略：在前缀树中尽量走相反的位（0走1，1走0）