# 已实现的异或运算题目

本文件记录了已经为三种编程语言（Java、Python、C++）实现的异或运算相关题目。

## 已实现题目列表

### 1. LeetCode 136. Single Number
- **题目描述**: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
- **解题思路**: 利用异或运算的性质，所有出现两次的元素会相互抵消为0，最终只剩下出现一次的元素。
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **实现文件**:
  - Java: [LeetCode136_SingleNumber.java](xor_problems_solutions/LeetCode136_SingleNumber.java)
  - Python: [LeetCode136_SingleNumber.py](xor_problems_solutions/LeetCode136_SingleNumber.py)
  - C++: [LeetCode136_SingleNumber_simple.cpp](xor_problems_solutions/LeetCode136_SingleNumber_simple.cpp)

### 2. LeetCode 421. Maximum XOR of Two Numbers in an Array
- **题目描述**: 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
- **解题思路**: 使用前缀树(Trie)结构和贪心策略，对于每一位尽量寻找相反的位以最大化异或结果。
- **时间复杂度**: O(n * 32) = O(n)
- **空间复杂度**: O(n * 32) = O(n)
- **实现文件**:
  - Java: [LeetCode421_MaximumXOR.java](xor_problems_solutions/LeetCode421_MaximumXOR.java)
  - Python: [LeetCode421_MaximumXOR.py](xor_problems_solutions/LeetCode421_MaximumXOR.py)
  - C++: [LeetCode421_MaximumXOR_simplest.cpp](xor_problems_solutions/LeetCode421_MaximumXOR_simplest.cpp)

## 待实现题目列表

以下题目将在后续版本中实现：

### 基础题目
1. **LeetCode 260. Single Number III** - 数组中两个只出现一次的元素
2. **LeetCode 137. Single Number II** - 数组中唯一出现一次的元素 II
3. **LeetCode 268. Missing Number** - 缺失的数字
4. **LeetCode 461. Hamming Distance** - 汉明距离
5. **LeetCode 1486. XOR Operation in an Array** - 数组异或操作

### 进阶题目
1. **LeetCode 1310. XOR Queries of a Subarray** - 子数组异或查询
2. **LeetCode 476. Number Complement** - 数字的补数
3. **LeetCode 693. Binary Number with Alternating Bits** - 交替位二进制数
4. **LeetCode 1707. Maximum XOR With an Element From Array** - 与数组中元素的最大异或值
5. **LeetCode 1803. Count Pairs With XOR in a Range** - 统计异或值在范围内的数对有多少
6. **LeetCode 2425. Bitwise XOR of All Pairings** - 所有数对的按位异或

### 平台题目
1. **LintCode 1490. Maximum XOR** - 数组中两个数的最大异或值 II
2. **HackerRank Sum vs XOR** - 和与异或
3. **Codeforces 276D. Little Girl and Maximum XOR** - 小女孩和最大异或
4. **Codeforces 617E. XOR and Favorite Number** - 异或和喜欢的数字
5. **AtCoder AGC045A. Xor Battle** - 异或战斗
6. **POJ 3764. The xor-longest Path** - 最长异或路径
7. **SPOJ SUBXOR. SubXor** - 子异或
8. **牛客网 NC152. 数组中两个数的最大异或值** - 牛客网最大异或值

## 实现计划

我们将按照以下优先级顺序实现剩余题目：

### 第一优先级（基础必会）
1. LeetCode 260. Single Number III
2. LeetCode 137. Single Number II
3. LeetCode 268. Missing Number

### 第二优先级（进阶应用）
1. LeetCode 1310. XOR Queries of a Subarray
2. LeetCode 1707. Maximum XOR With an Element From Array
3. LeetCode 1803. Count Pairs With XOR in a Range

### 第三优先级（平台特色）
1. Codeforces 617E. XOR and Favorite Number
2. POJ 3764. The xor-longest Path
3. HackerRank Sum vs XOR

## 技术要点

### 异或运算基本性质
1. **归零律**: a ^ a = 0
2. **恒等律**: a ^ 0 = a
3. **交换律**: a ^ b = b ^ a
4. **结合律**: (a ^ b) ^ c = a ^ (b ^ c)
5. **自反性**: a ^ b ^ a = b

### 常用技巧
1. **不使用额外变量交换两个数**
2. **找到数组中唯一出现奇数次的元素**
3. **找到数组中两个唯一出现奇数次的元素**
4. **Brian Kernighan算法**
5. **前缀树(Trie)在异或运算中的应用**
6. **前缀异或数组**
7. **线性基(Linear Basis)**

### 工程化考虑
1. **边界条件处理**
2. **性能优化**
3. **可读性**
4. **内存管理**