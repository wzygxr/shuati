# 水王数相关题目列表

## 基础水王数问题

### 1. LeetCode 169. Majority Element
- **题目链接**: https://leetcode.com/problems/majority-element/
- **中文链接**: https://leetcode.cn/problems/majority-element/
- **难度**: Easy
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 2. SPOJ MAJOR
- **题目链接**: https://www.spoj.com/problems/MAJOR/
- **难度**: 
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 3. GeeksforGeeks Majority Element
- **题目链接**: https://www.geeksforgeeks.org/problems/majority-element-1587115620/1
- **难度**: 
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 4. 牛客网 NC143 - 数组中的水王数
- **题目链接**: https://www.nowcoder.com/practice/38802713414c4852b6982410c4187dd2
- **难度**: 
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 5. 洛谷 P1496 - 火烧赤壁
- **题目链接**: https://www.luogu.com.cn/problem/P1496
- **难度**: 
- **描述**: 相关思想应用
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

## 多数元素扩展问题

### 6. LeetCode 229. Majority Element II
- **题目链接**: https://leetcode.com/problems/majority-element-ii/
- **中文链接**: https://leetcode.cn/problems/majority-element-ii/
- **难度**: Medium
- **描述**: 找出数组中出现次数大于n/3的元素
- **最优解法**: 扩展Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 7. GeeksforGeeks Find all array elements occurring more than ⌊N/3⌋ times
- **题目链接**: https://www.geeksforgeeks.org/dsa/find-all-array-elements-occurring-more-than-floor-of-n-divided-by-3-times/
- **难度**: 
- **描述**: 找出数组中出现次数大于n/3的元素
- **最优解法**: 扩展Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 8. 牛客网 NC144 - 多数元素 II
- **题目链接**: https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2
- **难度**: 
- **描述**: 找出数组中出现次数大于n/3的元素
- **最优解法**: 扩展Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

## 分割问题

### 9. LeetCode 2780. Minimum Index of a Valid Split
- **题目链接**: https://leetcode.com/problems/minimum-index-of-a-valid-split/
- **中文链接**: https://leetcode.cn/problems/minimum-index-of-a-valid-split/
- **难度**: Medium
- **描述**: 找到一个分割点，使得分割后的两部分都有支配元素
- **最优解法**: Boyer-Moore投票算法 + 遍历验证
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 10. 牛客网 NC145 - 合法分割的最小下标
- **题目链接**: https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
- **难度**: 
- **描述**: 找到一个分割点，使得分割后的两部分都有支配元素
- **最优解法**: Boyer-Moore投票算法 + 遍历验证
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

## 在线查询问题

### 11. LeetCode 1157. Online Majority Element In Subarray
- **题目链接**: https://leetcode.com/problems/online-majority-element-in-subarray/
- **中文链接**: https://leetcode.cn/problems/online-majority-element-in-subarray/
- **难度**: Hard
- **描述**: 设计数据结构支持快速查询任意子数组中的多数元素
- **最优解法**: 线段树 + 二分查找 或 随机化算法
- **时间复杂度**: 初始化O(nlogn)，查询O(logn) 或 初始化O(n)，查询期望O(logn)
- **空间复杂度**: O(n)

### 12. 牛客网 NC146 - 子数组中占绝大多数的元素
- **题目链接**: https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
- **难度**: 
- **描述**: 设计数据结构支持快速查询任意子数组中的多数元素
- **最优解法**: 线段树 + 二分查找 或 随机化算法
- **时间复杂度**: 初始化O(nlogn)，查询O(logn) 或 初始化O(n)，查询期望O(logn)
- **空间复杂度**: O(n)

## USACO竞赛题

### 13. USACO 2024 January Contest, Bronze Problem 1. Majority Opinion
- **题目链接**: https://usaco.org/index.php?page=viewproblem2&cpid=1371
- **难度**: Bronze
- **描述**: 通过焦点小组改变牛对干草的喜好，找出可以成为所有牛都喜欢的干草类型
- **最优解法**: 前缀和 + 枚举 或 贪心算法
- **时间复杂度**: O(n²) 或 O(n)
- **空间复杂度**: O(n)

## 其他平台相关题目

### 14. LintCode Majority Element
- **题目链接**: https://www.lintcode.com/problem/46/
- **难度**: 
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 15. HackerRank Most Frequent Element
- **题目链接**: https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/most-frequent-element
- **难度**: 
- **描述**: 找出数组中最频繁的元素
- **最优解法**: Boyer-Moore投票算法 或 哈希表
- **时间复杂度**: O(n)
- **空间复杂度**: O(1) 或 O(n)

### 16. CodeChef Find the majority element
- **题目链接**: https://www.codechef.com/practice/arrays
- **难度**: 
- **描述**: 找出数组中出现次数大于n/2的元素
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 17. AtCoder Beginner Contest 155 C - Poll
- **题目链接**: https://atcoder.jp/contests/abc155/tasks/abc155_c
- **难度**: 
- **描述**: 投票算法的变种应用
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

### 18. Codeforces Round #662 (Div. 2) B - Applejack and Storages
- **题目链接**: https://codeforces.com/contest/1579/problem/E2
- **难度**: 
- **描述**: 计数相关应用
- **最优解法**: Boyer-Moore投票算法
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)

## 总结

以上是水王数相关的主要题目，涵盖了从基础到高级的各种变体。掌握这些题目和解法对于算法学习和面试准备都非常有帮助。