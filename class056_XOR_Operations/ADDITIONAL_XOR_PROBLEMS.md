# 异或运算补充题目清单

本文件整理了来自各大算法平台的异或运算相关题目，为每个题目提供了题目来源、链接和简要描述。

## LeetCode 题目

1. **136. Single Number** - 数组中唯一出现一次的元素
   - 链接: https://leetcode.cn/problems/single-number/
   - 描述: 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。

2. **260. Single Number III** - 数组中两个只出现一次的元素
   - 链接: https://leetcode.cn/problems/single-number-iii/
   - 描述: 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。

3. **137. Single Number II** - 数组中唯一出现一次的元素 II
   - 链接: https://leetcode.cn/problems/single-number-ii/
   - 描述: 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。

4. **268. Missing Number** - 缺失的数字
   - 链接: https://leetcode.cn/problems/missing-number/
   - 描述: 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。

5. **461. Hamming Distance** - 汉明距离
   - 链接: https://leetcode.cn/problems/hamming-distance/
   - 描述: 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。

6. **1486. XOR Operation in an Array** - 数组异或操作
   - 链接: https://leetcode.cn/problems/xor-operation-in-an-array/
   - 描述: 给你两个整数，n 和 start 。数组 nums 定义为：nums[i] = start + 2*i（下标从 0 开始）且 n == nums.length 。请返回 nums 中所有元素按位异或（XOR）后得到的结果。

7. **421. Maximum XOR of Two Numbers in an Array** - 数组中两个数的最大异或值
   - 链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
   - 描述: 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

8. **1310. XOR Queries of a Subarray** - 子数组异或查询
   - 链接: https://leetcode.cn/problems/xor-queries-of-a-subarray/
   - 描述: 给你一个数组 arr 和一个整数 queries，其中 queries[i] = [Li, Ri]。对于每个查询 i，计算从 Li 到 Ri 的 XOR 值。

9. **476. Number Complement** - 数字的补数
   - 链接: https://leetcode.cn/problems/number-complement/
   - 描述: 对整数的二进制表示取反（0 变 1，1 变 0）后，再转换为十进制表示，可以得到这个整数的补数。

10. **693. Binary Number with Alternating Bits** - 交替位二进制数
    - 链接: https://leetcode.cn/problems/binary-number-with-alternating-bits/
    - 描述: 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现。

11. **1707. Maximum XOR With an Element From Array** - 与数组中元素的最大异或值
    - 链接: https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
    - 描述: 给你一个由非负整数组成的数组 nums 和一个查询数组 queries，其中 queries[i] = [xi, mi]。第 i 个查询的答案是 xi 与 nums 中所有小于等于 mi 的元素异或的最大值。

12. **1803. Count Pairs With XOR in a Range** - 统计异或值在范围内的数对有多少
    - 链接: https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
    - 描述: 给你一个整数数组 nums（下标从 0 开始）和一个整数 low、high，返回满足以下条件的数对 (i, j) 的数目。

13. **2425. Bitwise XOR of All Pairings** - 所有数对的按位异或
    - 链接: https://leetcode.cn/problems/bitwise-xor-of-all-pairings/
    - 描述: 给你两个数组 nums1 和 nums2，你需要计算所有数对 (x, y) 的异或值的异或和，其中 x 来自 nums1，y 来自 nums2。

## LintCode 题目

1. **1490. Maximum XOR** - 数组中两个数的最大异或值 II
   - 链接: https://www.lintcode.com/problem/1490/
   - 描述: 给定一个非负整数数组，找到数组中任意两个数异或的最大值。

## HackerRank 题目

1. **Sum vs XOR**
   - 链接: https://www.hackerrank.com/challenges/sum-vs-xor/problem
   - 描述: 给定一个整数 n，找出非负整数 x 的个数，使得 x + n == x ^ n。

2. **The Great XOR**
   - 链接: https://www.hackerrank.com/challenges/the-great-xor/problem
   - 描述: 给定一个长整数 q，计算满足条件的值的数量。

3. **Xoring Ninja**
   - 链接: https://www.hackerrank.com/challenges/xoring-ninja/problem
   - 描述: 给定一个整数列表，对所有非空子集的 XOR 进行求和并对答案取模 10^9 + 7。

## Codeforces 题目

1. **276D. Little Girl and Maximum XOR**
   - 链接: https://codeforces.com/problemset/problem/276/D
   - 描述: 给定两个整数l和r，找到两个数a, b (l ≤ a ≤ b ≤ r)，使得a XOR b的值最大。

2. **617E. XOR and Favorite Number**
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 描述: 给定一个数组a和整数k，以及多个查询[l, r]，对于每个查询，统计子数组a[l...r]中有多少个子数组的异或值等于k。

3. **959F. Mahmoud and Ehab and the xor**
   - 链接: https://codeforces.com/problemset/problem/959/F
   - 描述: 给定一个数组a和多个查询，每个查询给出l, x，问在a[0...l]的子序列中，有多少个子序列的异或值等于x。

4. **1055F. Tree and XOR**
   - 链接: https://codeforces.com/problemset/problem/1055/F
   - 描述: 给定一棵带权树，找到第k小的路径异或值。

## AtCoder 题目

1. **AGC045A. Xor Battle**
   - 链接: https://atcoder.jp/contests/agc045/tasks/agc045_a
   - 描述: 两个玩家，0和1，玩一个游戏，x从0开始。玩家0的目标是x=0，玩家1的目标是x!=0。问题是确定x是否变为0。

2. **ARC124B. XOR Matching 2**
   - 链接: https://atcoder.jp/contests/arc124/tasks/arc124_b
   - 描述: 给定长度为N的序列a和b，由非负整数组成。

## POJ 题目

1. **3764. The xor-longest Path**
   - 链接: http://poj.org/problem?id=3764
   - 描述: 给定一个带权树，找到异或最长路径。

## SPOJ 题目

1. **SUBXOR. SubXor**
   - 链接: https://www.spoj.com/problems/SUBXOR/
   - 描述: 给定一个正整数数组，打印异或值小于K的子数组数量。

2. **MAXXOR. Find the max XOR value**
   - 链接: https://www.spoj.com/problems/MAXXOR/
   - 描述: 给定两个整数L和R，要求找到a和b的最大异或值，其中L ≤ a ≤ R且L ≤ b ≤ R。

## 牛客网题目

1. **NC152. 数组中两个数的最大异或值**
   - 链接: https://www.nowcoder.com/practice/363b9cab5ab142459f757c79c0b540be
   - 描述: 给定一个非负整数数组，找到数组中任意两个数异或的最大值。

## 洛谷题目

1. **P4551. 最长异或路径**
   - 链接: https://www.luogu.com.cn/problem/P4551
   - 描述: 给定一棵n个点的带权树，结点下标从1开始到n。求树中所有异或路径的最大值。

2. **P10471. 最大异或对The XOR Largest Pair**
   - 链接: https://www.luogu.com.cn/problem/P10471
   - 描述: 给定N个整数中选出两个进行异或计算，得到的结果最大是多少？

3. **P4151. [WC2011]最大XOR和路径**
   - 链接: https://www.luogu.com.cn/problem/P4151
   - 描述: 给定一个无向连通图，求从1号节点到n号节点的路径异或和的最大值。

## 杭电OJ题目

虽然没有找到具体的题目链接，但异或运算在杭电OJ中也有广泛应用，通常涉及：
- 基础异或性质应用
- 异或与图论结合的问题
- 异或与动态规划结合的问题

## 解题技巧总结

### 基础技巧
1. **异或基本性质**：
   - 归零律: a ^ a = 0
   - 恒等律: a ^ 0 = a
   - 交换律: a ^ b = b ^ a
   - 结合律: (a ^ b) ^ c = a ^ (b ^ c)
   - 自反性: a ^ b ^ a = b

2. **不使用额外变量交换两个数**：
   ```java
   a = a ^ b;
   b = a ^ b;
   a = a ^ b;
   ```

3. **找到数组中唯一出现奇数次的元素**：
   利用归零律和恒等律，对所有元素进行异或运算。

4. **找到数组中两个唯一出现奇数次的元素**：
   - 对所有元素异或得到 a ^ b
   - 找到 a ^ b 中最右边的1位
   - 根据该位将数组分为两组分别异或

### 进阶技巧
1. **Brian Kernighan算法**：
   - `n & (-n)` 可以提取出最右边的1位
   - `n & (n-1)` 可以清除最右边的1位

2. **前缀树(Trie)在异或运算中的应用**：
   - 用于解决最大异或值问题
   - 用于解决异或范围查询问题
   - 用于解决第k大异或值问题

3. **前缀异或数组**：
   - 用于快速计算区间异或值
   - 类似前缀和数组的思想

4. **莫队算法在异或问题中的应用**：
   - 用于解决区间异或查询问题
   - 结合前缀异或数组使用

5. **线性基(Linear Basis)**：
   - 用于解决向量空间中的异或问题
   - 用于解决子集异或相关问题

### 工程化考虑
1. **边界条件处理**：
   - 空数组检查
   - 单元素数组处理
   - 整数溢出检查

2. **性能优化**：
   - 使用位运算替代乘除法
   - 利用短路求值
   - 避免重复计算

3. **可读性**：
   - 添加详细注释
   - 使用有意义的变量名
   - 拆分复杂逻辑

4. **前缀树优化**：
   - 内存管理：及时释放前缀树节点
   - 查询优化：使用贪心策略查找最大异或值
   - 离线处理：对查询进行排序以提高效率