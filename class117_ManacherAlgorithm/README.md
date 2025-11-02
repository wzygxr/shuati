# Manacher 算法专题

## 算法简介

Manacher算法（也称为马拉车算法）是由Glenn K. Manacher在1975年提出的，用于在线性时间内查找字符串中所有回文子串的算法。该算法主要解决以下问题：

1. 查找字符串中的最长回文子串
2. 统计字符串中回文子串的数量
3. 解决与回文相关的各种问题

## 算法原理

### 核心思想

Manacher算法的核心思想是利用回文串的对称性来避免重复计算。对于一个回文串，其中心点左右两侧的字符是镜像对称的，因此我们可以利用已经计算过的信息来加速新位置的计算。

### 预处理

为了统一处理奇数长度和偶数长度的回文串，Manacher算法首先对原字符串进行预处理：

1. 在每个字符之间插入特殊字符（如'#'）
2. 在字符串的开头和结尾也插入特殊字符

例如，原字符串"abc"经过预处理后变成"#a#b#c#"。

### 关键变量

1. **p[i]**: 表示以位置i为中心的最长回文串的半径
2. **c**: 当前最右回文子串的中心
3. **r**: 当前最右回文子串的右边界

### 算法步骤

1. 初始化所有变量
2. 遍历预处理后的字符串中的每个位置i
3. 利用回文对称性优化：
   - 如果i在当前右边界内，则可以利用对称点的信息
   - p[i] = min(p[2*c-i], r-i)
4. 尝试扩展回文串
5. 更新最右回文边界和中心

## 时间与空间复杂度

- **时间复杂度**: O(n)，其中n为字符串长度
- **空间复杂度**: O(n)，用于存储预处理字符串和回文半径数组

## 相关题目

### 1. 最长回文子串
- **题目**: 给定一个字符串 s，找到 s 中最长的回文子串
- **链接**: https://leetcode.cn/problems/longest-palindromic-substring/
- **文件**: [Code01_LongestPalindromeSubstring.java](Code01_LongestPalindromeSubstring.java) | [Code01_LongestPalindromeSubstring.cpp](Code01_LongestPalindromeSubstring.cpp) | [Code01_LongestPalindromeSubstring.py](Code01_LongestPalindromeSubstring.py)

### 2. 回文子串数量
- **题目**: 给定一个字符串，计算其中回文子串的总数
- **链接**: https://leetcode.cn/problems/palindromic-substrings/
- **文件**: [Code02_NumberOfPalindromeSubstrings.java](Code02_NumberOfPalindromeSubstrings.java) | [Code02_NumberOfPalindromeSubstrings.cpp](Code02_NumberOfPalindromeSubstrings.cpp) | [Code02_NumberOfPalindromeSubstrings.py](Code02_NumberOfPalindromeSubstrings.py)

### 3. 不重叠回文子串的最多数目
- **题目**: 给定一个字符串和正数k，找到某一种划分方案，有尽可能多的回文子串，且每个回文子串都要求长度>=k、且彼此没有重合的部分
- **链接**: https://leetcode.cn/problems/maximum-number-of-non-overlapping-palindrome-substrings/
- **文件**: [Code03_SplitMaximumPalindromes.java](Code03_SplitMaximumPalindromes.java) | [Code03_SplitMaximumPalindromes.cpp](Code03_SplitMaximumPalindromes.cpp) | [Code03_SplitMaximumPalindromes.py](Code03_SplitMaximumPalindromes.py)

### 4. 长度前k名的奇数长度回文子串长度乘积
- **题目**: 给定一个字符串s和数值k，只关心所有奇数长度的回文子串，返回其中长度前k名的回文子串的长度乘积
- **链接**: https://www.luogu.com.cn/problem/P1659
- **文件**: [Code04_TopKOddLengthProduct.java](Code04_TopKOddLengthProduct.java) | [Code04_TopKOddLengthProduct.cpp](Code04_TopKOddLengthProduct.cpp) | [Code04_TopKOddLengthProduct.py](Code04_TopKOddLengthProduct.py)

### 5. 最长双回文串长度
- **题目**: 输入字符串s，求s的最长双回文子串t的长度（双回文子串就是可以分成两个回文串的字符串）
- **链接**: https://www.luogu.com.cn/problem/P4555
- **文件**: [Code05_LongestDoublePalindrome.java](Code05_LongestDoublePalindrome.java) | [Code05_LongestDoublePalindrome.cpp](Code05_LongestDoublePalindrome.cpp) | [Code05_LongestDoublePalindrome.py](Code05_LongestDoublePalindrome.py)

### 6. 分割回文串 II
- **题目**: 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数
- **链接**: https://leetcode.cn/problems/palindrome-partitioning-ii/
- **文件**: [Code06_PalindromePartitioning.java](Code06_PalindromePartitioning.java) | [Code06_PalindromePartitioning.cpp](Code06_PalindromePartitioning.cpp) | [Code06_PalindromePartitioning.py](Code06_PalindromePartitioning.py)

### 7. 最短回文串
- **题目**: 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串
- **链接**: https://leetcode.cn/problems/shortest-palindrome/
- **文件**: [Code07_ShortestPalindrome.java](Code07_ShortestPalindrome.java) | [Code07_ShortestPalindrome.cpp](Code07_ShortestPalindrome.cpp) | [Code07_ShortestPalindrome.py](Code07_ShortestPalindrome.py)

### 8. 回文子串
- **题目**: 给定一个字符串 s，请你统计并返回这个字符串中回文子串的数目
- **链接**: https://leetcode.cn/problems/palindromic-substrings/
- **文件**: [Code12_PalindromicSubstrings.java](Code12_PalindromicSubstrings.java) | [Code12_PalindromicSubstrings.cpp](Code12_PalindromicSubstrings.cpp) | [Code12_PalindromicSubstrings.py](Code12_PalindromicSubstrings.py)

### 9. 洛谷 P3805 【模板】manacher
- **题目**: 给出一个只由小写英文字符a,b,c...y,z组成的字符串S,求S中最长回文串的长度
- **链接**: https://www.luogu.com.cn/problem/P3805
- **文件**: [Code08_P3805_Manacher.java](Code08_P3805_Manacher.java) | [Code08_P3805_Manacher.cpp](Code08_P3805_Manacher.cpp) | [Code08_P3805_Manacher.py](Code08_P3805_Manacher.py)

### 10. POJ 3974 Palindrome
- **题目**: 给定一个字符串，找到其中最长的回文子串并返回其长度
- **链接**: http://poj.org/problem?id=3974
- **文件**: [Code09_POJ3974_Palindrome.java](Code09_POJ3974_Palindrome.java) | [Code09_POJ3974_Palindrome.cpp](Code09_POJ3974_Palindrome.cpp) | [Code09_POJ3974_Palindrome.py](Code09_POJ3974_Palindrome.py)

### 11. HDU 3068 最长回文
- **题目**: 给定一个字符串，找到其中最长的回文子串并返回其长度
- **链接**: https://vjudge.net/problem/HDU-3068
- **文件**: [Code10_HDU3068_LongestPalindrome.java](Code10_HDU3068_LongestPalindrome.java) | [Code10_HDU3068_LongestPalindrome.cpp](Code10_HDU3068_LongestPalindrome.cpp) | [Code10_HDU3068_LongestPalindrome.py](Code10_HDU3068_LongestPalindrome.py)

### 12. Codeforces 137D Palindromes
- **题目**: 给定一个字符串，将其分割成最少的回文子串
- **链接**: https://codeforces.com/problemset/problem/137/D
- **文件**: [Code11_CF137D_Palindromes.java](Code11_CF137D_Palindromes.java) | [Code11_CF137D_Palindromes.cpp](Code11_CF137D_Palindromes.cpp) | [Code11_CF137D_Palindromes.py](Code11_CF137D_Palindromes.py)

## 更多练习题目

以下是一些可以用Manacher算法解决的经典题目：

### LeetCode
1. [LeetCode 5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)
2. [LeetCode 9. 回文数](https://leetcode.cn/problems/palindrome-number/)
3. [LeetCode 125. 验证回文串](https://leetcode.cn/problems/valid-palindrome/)
4. [LeetCode 131. 分割回文串](https://leetcode.cn/problems/palindrome-partitioning/)
5. [LeetCode 132. 分割回文串 II](https://leetcode.cn/problems/palindrome-partitioning-ii/)
6. [LeetCode 214. 最短回文串](https://leetcode.cn/problems/shortest-palindrome/)
7. [LeetCode 336. 回文对](https://leetcode.cn/problems/palindrome-pairs/)
8. [LeetCode 647. 回文子串](https://leetcode.cn/problems/palindromic-substrings/)
9. [LeetCode 1089. 复写零](https://leetcode.cn/problems/duplicate-zeros/) (部分情况)
10. [LeetCode 730. 统计不同回文子序列](https://leetcode.cn/problems/count-different-palindromic-subsequences/)
11. [LeetCode 1216. 验证回文字符串 III](https://leetcode.cn/problems/valid-palindrome-iii/)
12. [LeetCode 1312. 让字符串成为回文串的最少插入次数](https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/)

### 洛谷
1. [洛谷 P3805 【模板】manacher](https://www.luogu.com.cn/problem/P3805)
2. [洛谷 P1659 [国家集训队]拉拉队排练](https://www.luogu.com.cn/problem/P1659)
3. [洛谷 P4555 [国家集训队]最长双回文串](https://www.luogu.com.cn/problem/P4555)
4. [洛谷 P1435 回文字串](https://www.luogu.com.cn/problem/P1435)
5. [洛谷 P4287 [SHOI2011]双倍回文](https://www.luogu.com.cn/problem/P4287)
6. [洛谷 P5496 【模板】回文自动机](https://www.luogu.com.cn/problem/P5496)

### POJ
1. [POJ 3974 Palindrome](http://poj.org/problem?id=3974)
2. [POJ 1159 Palindrome](http://poj.org/problem?id=1159)
3. [POJ 3280 Cheapest Palindrome](http://poj.org/problem?id=3280)

### HDU
1. [HDU 3068 最长回文](https://vjudge.net/problem/HDU-3068)
2. [HDU 3294 Girls' research](https://vjudge.net/problem/HDU-3294)
3. [HDU 4513 吉哥系列故事——完美队形II](https://vjudge.net/problem/HDU-4513)
4. [HDU 6264 Super-palindrome](https://vjudge.net/problem/HDU-6264)

### Codeforces
1. [Codeforces 137D Palindromes](https://codeforces.com/problemset/problem/137/D)
2. [Codeforces 7D Palindrome Degree](https://codeforces.com/problemset/problem/7/D)
3. [Codeforces 17E Palisection](https://codeforces.com/problemset/problem/17/E)
4. [Codeforces 245H Queries for Number of Palindromes](https://codeforces.com/problemset/problem/245/H)
5. [Codeforces 137E Last Chance](https://codeforces.com/problemset/problem/137/E)

### LintCode
1. [LintCode 200. 最长回文子串](https://www.lintcode.com/problem/200/)
2. [LintCode 891. 有效回文 II](https://www.lintcode.com/problem/891/)

### HackerRank
1. [HackerRank Build a Palindrome](https://www.hackerrank.com/challenges/challenging-palindromes/problem)
2. [HackerRank Circular Palindromes](https://www.hackerrank.com/challenges/circular-palindromes/problem)
3. [HackerRank Palindromic Border](https://www.hackerrank.com/challenges/palindromic-border/problem)

### CodeChef
1. [CodeChef Practice Problems on Manacher's Algorithm](https://www.codechef.com/tags/problems/manachers-algorithm)

### SPOJ
1. [SPOJ LPS - Longest Palindromic Substring](https://www.spoj.com/problems/LPS/)
2. [SPOJ NUMOFPAL - Number of Palindromes](https://www.spoj.com/problems/NUMOFPAL/)
3. [SPOJ EPALIN - Extend to Palindrome](https://www.spoj.com/problems/EPALIN/)
4. [SPOJ PLNDROME - Palindrome Or Not](https://www.spoj.com/problems/PLNDROME/)

### AtCoder
1. [AtCoder ABC 349 - Manacher's Algorithm](https://atcoder.jp/contests/abc349/tasks/abc349_e)
2. [AtCoder ABC 197 D - Opposite](https://atcoder.jp/contests/abc197/tasks/abc197_d)

### Project Euler
1. [Project Euler Problem 4 - Largest palindrome product](https://projecteuler.net/problem=4)
2. [Project Euler Problem 655 - Divisible Palindromes](https://projecteuler.net/problem=655)

### HackerEarth
1. [HackerEarth Manachar's Algorithm Practice Problems](https://www.hackerearth.com/practice/algorithms/string-algorithm/manachars-algorithm/practice-problems/)
2. [HackerEarth Longest Palindromic String](https://www.hackerearth.com/practice/algorithms/string-algorithm/manachars-algorithm/practice-problems/algorithm/longest-palindromic-string/)

### USACO
1. [USACO Training Palindrome](http://www.usaco.org/index.php?page=viewproblem2&cpid=895)

### 牛客网
1. [牛客网 Manacher算法](https://www.nowcoder.com/practice/c1408adc44294f88a795144e50c23e7c)

### 杭电OJ
1. [HDU 3068 最长回文](https://vjudge.net/problem/HDU-3068)

### 其他OJ平台题目
1. [UVa 11475 Extend to Palindrome](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470)
2. [ZOJ 3720 - Alice's Print Service](https://vjudge.net/problem/ZOJ-3720)
3. [ACWing 143. 最大异或对](https://www.acwing.com/problem/content/145/)
4. [计蒜客 T1458 - 回文子串](https://nanti.jisuanke.com/t/T1458)
5. [各大高校OJ - 回文串问题](https://vjudge.net/problem)
6. [Comet OJ - 回文串](https://cometoj.com/)
7. [Timus OJ - Palindrome](https://acm.timus.ru/problem.aspx?space=1&num=1458)
8. [Aizu OJ - Palindrome](https://onlinejudge.u-aizu.ac.jp/problems/1458)
9. [MarsCode - 回文串](https://www.marscode.cn/)
10. [LOJ - Palindrome Partitioning](https://lightoj.com/problem/palindrome-partitioning)

## 补充题目实现（已完善三语言版本）

### 1. 最长回文子串 (LeetCode 5)
- **题目**: 给定一个字符串 s，找到 s 中最长的回文子串
- **链接**: https://leetcode.cn/problems/longest-palindromic-substring/
- **文件**: 
  - [Code01_LongestPalindromeSubstring.java](Code01_LongestPalindromeSubstring.java)
  - [Code01_LongestPalindromeSubstring.cpp](Code01_LongestPalindromeSubstring.cpp)
  - [Code01_LongestPalindromeSubstring.py](Code01_LongestPalindromeSubstring.py)

### 2. 回文子串数量 (LeetCode 647)
- **题目**: 给定一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目
- **链接**: https://leetcode.cn/problems/palindromic-substrings/
- **文件**: 
  - [Code02_NumberOfPalindromeSubstrings.java](Code02_NumberOfPalindromeSubstrings.java)
  - [Code02_NumberOfPalindromeSubstrings.cpp](Code02_NumberOfPalindromeSubstrings.cpp)
  - [Code02_NumberOfPalindromeSubstrings.py](Code02_NumberOfPalindromeSubstrings.py)

### 3. 分割回文串 II (LeetCode 132)
- **题目**: 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数
- **链接**: https://leetcode.cn/problems/palindrome-partitioning-ii/
- **文件**: 
  - [Code03_SplitMaximumPalindromes.java](Code03_SplitMaximumPalindromes.java)
  - [Code03_SplitMaximumPalindromes.cpp](Code03_SplitMaximumPalindromes.cpp)
  - [Code03_SplitMaximumPalindromes.py](Code03_SplitMaximumPalindromes.py)

### 4. 长度前k名的奇数长度回文子串长度乘积 (洛谷 P1659)
- **题目**: 给定一个字符串s和数值k，只关心所有奇数长度的回文子串，返回其中长度前k名的回文子串的长度乘积
- **链接**: https://www.luogu.com.cn/problem/P1659
- **文件**: 
  - [Code04_TopKOddLengthProduct.java](Code04_TopKOddLengthProduct.java)
  - [Code04_TopKOddLengthProduct.cpp](Code04_TopKOddLengthProduct.cpp)
  - [Code04_TopKOddLengthProduct.py](Code04_TopKOddLengthProduct.py)

### 5. 最长双回文串长度 (洛谷 P4555)
- **题目**: 输入字符串s，求s的最长双回文子串t的长度（双回文子串就是可以分成两个回文串的字符串）
- **链接**: https://www.luogu.com.cn/problem/P4555
- **文件**: 
  - [Code05_LongestDoublePalindrome.java](Code05_LongestDoublePalindrome.java)
  - [Code05_LongestDoublePalindrome.cpp](Code05_LongestDoublePalindrome.cpp)
  - [Code05_LongestDoublePalindrome.py](Code05_LongestDoublePalindrome.py)

### 6. 分割回文串 II (LeetCode 132)
- **题目**: 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数
- **链接**: https://leetcode.cn/problems/palindrome-partitioning-ii/
- **文件**: 
  - [Code06_PalindromePartitioning.java](Code06_PalindromePartitioning.java)
  - [Code06_PalindromePartitioning.cpp](Code06_PalindromePartitioning.cpp)
  - [Code06_PalindromePartitioning.py](Code06_PalindromePartitioning.py)

### 7. 最短回文串 (LeetCode 214)
- **题目**: 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串
- **链接**: https://leetcode.cn/problems/shortest-palindrome/
- **文件**: 
  - [Code07_ShortestPalindrome.java](Code07_ShortestPalindrome.java)
  - [Code07_ShortestPalindrome.cpp](Code07_ShortestPalindrome.cpp)
  - [Code07_ShortestPalindrome.py](Code07_ShortestPalindrome.py)

### 8. 洛谷 P3805 【模板】manacher
- **题目**: 给出一个只由小写英文字符a,b,c...y,z组成的字符串S,求S中最长回文串的长度
- **链接**: https://www.luogu.com.cn/problem/P3805
- **文件**: 
  - [Code08_P3805_Manacher.java](Code08_P3805_Manacher.java)
  - [Code08_P3805_Manacher.cpp](Code08_P3805_Manacher.cpp)
  - [Code08_P3805_Manacher.py](Code08_P3805_Manacher.py)

### 9. POJ 3974 Palindrome
- **题目**: 给定一个字符串，找到其中最长的回文子串并返回其长度
- **链接**: http://poj.org/problem?id=3974
- **文件**: 
  - [Code09_POJ3974_Palindrome.java](Code09_POJ3974_Palindrome.java)
  - [Code09_POJ3974_Palindrome.cpp](Code09_POJ3974_Palindrome.cpp)
  - [Code09_POJ3974_Palindrome.py](Code09_POJ3974_Palindrome.py)

### 10. HDU 3068 最长回文
- **题目**: 给定一个字符串，找到其中最长的回文子串并返回其长度
- **链接**: https://vjudge.net/problem/HDU-3068
- **文件**: 
  - [Code10_HDU3068_LongestPalindrome.java](Code10_HDU3068_LongestPalindrome.java)
  - [Code10_HDU3068_LongestPalindrome.cpp](Code10_HDU3068_LongestPalindrome.cpp)
  - [Code10_HDU3068_LongestPalindrome.py](Code10_HDU3068_LongestPalindrome.py)

### 11. Codeforces 137D Palindromes
- **题目**: 给定一个字符串，将其分割成最少的回文子串
- **链接**: https://codeforces.com/problemset/problem/137/D
- **文件**: 
  - [Code11_CF137D_Palindromes.java](Code11_CF137D_Palindromes.java)
  - [Code11_CF137D_Palindromes.cpp](Code11_CF137D_Palindromes.cpp)
  - [Code11_CF137D_Palindromes.py](Code11_CF137D_Palindromes.py)

### 12. 回文子串 (LeetCode 647)
- **题目**: 给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目
- **链接**: https://leetcode.cn/problems/palindromic-substrings/
- **文件**: 
  - [Code12_PalindromicSubstrings.java](Code12_PalindromicSubstrings.java)
  - [Code12_PalindromicSubstrings.cpp](Code12_PalindromicSubstrings.cpp)
  - [Code12_PalindromicSubstrings.py](Code12_PalindromicSubstrings.py)

### 13. 验证回文串 (LeetCode 125)
- **题目**: 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写
- **链接**: https://leetcode.cn/problems/valid-palindrome/
- **文件**: 
  - [Code13_ValidPalindrome.java](Code13_ValidPalindrome.java)
  - [Code13_ValidPalindrome.cpp](Code13_ValidPalindrome.cpp)
  - [Code13_ValidPalindrome.py](Code13_ValidPalindrome.py)

### 14. 回文数 (LeetCode 9)
- **题目**: 判断一个整数是否是回文数
- **链接**: https://leetcode.cn/problems/palindrome-number/
- **文件**: 
  - [Code14_PalindromeNumber.java](Code14_PalindromeNumber.java)
  - [Code14_PalindromeNumber.cpp](Code14_PalindromeNumber.cpp)
  - [Code14_PalindromeNumber.py](Code14_PalindromeNumber.py)

### 15. 最长回文子序列 (LeetCode 516)
- **题目**: 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度
- **链接**: https://leetcode.cn/problems/longest-palindromic-subsequence/
- **文件**: 
  - [Code15_LongestPalindromeSubseq.java](Code15_LongestPalindromeSubseq.java)
  - [Code15_LongestPalindromeSubseq.cpp](Code15_LongestPalindromeSubseq.cpp)
  - [Code15_LongestPalindromeSubseq.py](Code15_LongestPalindromeSubseq.py)

## 更多相关题目（广泛搜索各大OJ平台）

### LeetCode 相关题目
1. **[LeetCode 5. 最长回文子串](https://leetcode.cn/problems/longest-palindromic-substring/)** - 使用Manacher算法求解
2. **[LeetCode 9. 回文数](https://leetcode.cn/problems/palindrome-number/)** - 判断整数是否为回文数
3. **[LeetCode 125. 验证回文串](https://leetcode.cn/problems/valid-palindrome/)** - 验证字符串是否为回文（忽略大小写和非字母数字字符）
4. **[LeetCode 131. 分割回文串](https://leetcode.cn/problems/palindrome-partitioning/)** - 返回所有可能的分割方案
5. **[LeetCode 132. 分割回文串 II](https://leetcode.cn/problems/palindrome-partitioning-ii/)** - 最少分割次数
6. **[LeetCode 214. 最短回文串](https://leetcode.cn/problems/shortest-palindrome/)** - 在字符串前添加字符使其成为回文
7. **[LeetCode 336. 回文对](https://leetcode.cn/problems/palindrome-pairs/)** - 找出所有回文对
8. **[LeetCode 647. 回文子串](https://leetcode.cn/problems/palindromic-substrings/)** - 统计回文子串数量
9. **[LeetCode 680. 验证回文字符串 Ⅱ](https://leetcode.cn/problems/valid-palindrome-ii/)** - 最多删除一个字符
10. **[LeetCode 730. 统计不同回文子序列](https://leetcode.cn/problems/count-different-palindromic-subsequences/)** - 统计不同的回文子序列
11. **[LeetCode 1216. 验证回文字符串 III](https://leetcode.cn/problems/valid-palindrome-iii/)** - 最多删除k个字符

### LintCode 相关题目
1. **[LintCode 200. 最长回文子串](https://www.lintcode.com/problem/200/)** - 使用Manacher算法求解

### HackerRank 相关题目
1. **[HackerRank Build a Palindrome](https://www.hackerrank.com/challenges/challenging-palindromes/problem)** - 构造回文串
2. **[HackerRank Circular Palindromes](https://www.hackerrank.com/challenges/circular-palindromes/problem)** - 循环回文串
3. **[HackerRank Palindromic Border](https://www.hackerrank.com/challenges/palindromic-border/problem)** - 回文边界

### CodeChef 相关题目
1. **[CodeChef Practice Problems on Manacher's Algorithm](https://www.codechef.com/tags/problems/manachers-algorithm)** - Manacher算法练习题

### SPOJ 相关题目
1. **[SPOJ LPS - Longest Palindromic Substring](https://www.spoj.com/problems/LPS/)** - 最长回文子串
2. **[SPOJ NUMOFPAL - Number of Palindromes](https://www.spoj.com/problems/NUMOFPAL/)** - 回文串数量

### AtCoder 相关题目
1. **[AtCoder ABC 349 - Manacher's Algorithm](https://atcoder.jp/contests/abc349/tasks/abc349_e)** - 使用Manacher算法求解

### Project Euler 相关题目
1. **[Project Euler Problem 4 - Largest palindrome product](https://projecteuler.net/problem=4)** - 最大回文乘积

### HackerEarth 相关题目
1. **[HackerEarth Manachar's Algorithm Practice Problems](https://www.hackerearth.com/practice/algorithms/string-algorithm/manachars-algorithm/practice-problems/)** - Manacher算法练习题
2. **[HackerEarth Longest Palindromic String](https://www.hackerearth.com/practice/algorithms/string-algorithm/manachars-algorithm/practice-problems/algorithm/longest-palindromic-string/)** - 最长回文串

### USACO 相关题目
1. **[USACO Training Palindrome](http://www.usaco.org/index.php?page=viewproblem2&cpid=895)** - 回文训练题

### Codeforces 相关题目
1. **[Codeforces 137D Palindromes](https://codeforces.com/problemset/problem/137/D)** - 最少分割成回文子串
2. **[Codeforces 7D Palindrome Degree](https://codeforces.com/problemset/problem/7/D)** - 回文度计算
3. **[Codeforces 17E Palisection](https://codeforces.com/problemset/problem/17/E)** - 相交回文子串对

### 洛谷相关题目
1. **[洛谷 P3805 【模板】manacher](https://www.luogu.com.cn/problem/P3805)** - Manacher算法模板题
2. **[洛谷 P1435 回文字串](https://www.luogu.com.cn/problem/P1435)** - 插入字符使字符串成为回文
3. **[洛谷 P1659 [国家集训队]拉拉队排练](https://www.luogu.com.cn/problem/P1659)** - 奇数长度回文串长度乘积
4. **[洛谷 P4555 [国家集训队]最长双回文串](https://www.luogu.com.cn/problem/P4555)** - 最长双回文串
5. **[洛谷 P4287 [SHOI2011]双倍回文](https://www.luogu.com.cn/problem/P4287)** - 双倍回文串
6. **[洛谷 P5496 【模板】回文自动机](https://www.luogu.com.cn/problem/P5496)** - 回文自动机模板

### POJ相关题目
1. **[POJ 1159 Palindrome](http://poj.org/problem?id=1159)** - 最少插入字符使字符串成为回文
2. **[POJ 3280 Cheapest Palindrome](http://poj.org/problem?id=3280)** - 最小代价构造回文
3. **[POJ 3974 Palindrome](http://poj.org/problem?id=3974)** - 最长回文子串

### HDU相关题目
1. **[HDU 3068 最长回文](https://vjudge.net/problem/HDU-3068)** - 最长回文子串
2. **[HDU 3294 Girls' research](https://vjudge.net/problem/HDU-3294)** - 字符替换后的最长回文
3. **[HDU 4513 吉哥系列故事——完美队形II](https://vjudge.net/problem/HDU-4513)** - 最长非递减回文

### 牛客网相关题目
1. **[牛客网 Manacher算法](https://www.nowcoder.com/practice/c1408adc44294f88a795144e50c23e7c)** - Manacher算法题

### 杭电OJ相关题目
1. **[HDU 3068 最长回文](https://vjudge.net/problem/HDU-3068)** - 最长回文子串

### 其他OJ平台题目
1. **[UVa 11475 Extend to Palindrome](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470)** - 扩展为回文
2. **[AtCoder ABC 197 D - Opposite](https://atcoder.jp/contests/abc197/tasks/abc197_d)** - 回文性质应用
3. **[ZOJ 3720 - Alice's Print Service](https://vjudge.net/problem/ZOJ-3720)** - 回文相关应用
4. **[ACWing 143. 最大异或对](https://www.acwing.com/problem/content/145/)** - 回文性质应用
5. **[计蒜客 T1458 - 回文子串](https://nanti.jisuanke.com/t/T1458)** - 回文子串计数
6. **[各大高校OJ - 回文串问题](https://vjudge.net/problem)** - 各类回文串问题
7. **[Comet OJ - 回文串](https://cometoj.com/)** - 回文串相关题目
8. **[Timus OJ - Palindrome](https://acm.timus.ru/problem.aspx?space=1&num=1458)** - 回文串问题
9. **[Aizu OJ - Palindrome](https://onlinejudge.u-aizu.ac.jp/problems/1458)** - 回文串问题
10. **[MarsCode - 回文串](https://www.marscode.cn/)** - 回文串相关题目
11. **[LOJ - Palindrome Partitioning](https://lightoj.com/problem/palindrome-partitioning)** - 回文分割问题
12. **[剑指Offer LCR 086. 分割回文串](https://leetcode.cn/problems/M99OJA/)** - 分割回文串
13. **[剑指Offer LCR 027. 回文链表](https://leetcode.cn/problems/aMhZSa/)** - 回文链表

## 算法深度分析与工程化考量

### 一、Manacher算法核心细节拆解

#### 1. 预处理阶段的必要性
- **统一处理**：通过插入特殊字符，将奇数长度和偶数长度回文统一处理
- **边界简化**：预处理后字符串的首尾都是'#'，简化边界判断
- **数学原理**：预处理后字符串长度变为2n+1，确保中心扩展的对称性

#### 2. 回文对称性利用的数学证明
- **对称点计算**：对于位置i，其对称点为2*c-i
- **半径传递性**：p[i] ≥ min(p[2*c-i], r-i)
- **线性复杂度证明**：每个字符最多被扩展一次，总扩展次数为O(n)

### 二、时间复杂度与空间复杂度详细计算

#### 时间复杂度分析
```
T(n) = 预处理时间 + 主循环时间 + 扩展时间
     = O(n) + O(n) + O(n) = O(n)
```

#### 空间复杂度分析
```
S(n) = 预处理字符串空间 + 回文半径数组空间
     = O(2n+1) + O(2n+1) = O(n)
```

### 三、工程化实现的关键考量

#### 1. 异常场景处理
```java
// 空字符串处理
if (s == null || s.length() == 0) return 0;

// 单字符处理
if (s.length() == 1) return 1;

// 内存溢出防护
if (s.length() > MAX_LENGTH) throw new IllegalArgumentException("字符串过长");
```

#### 2. 性能优化策略
- **内存预分配**：避免动态内存分配的开销
- **循环优化**：减少循环内的条件判断
- **缓存友好**：顺序访问数组，提高缓存命中率

#### 3. 多语言实现差异

**Java实现特点**：
```java
// 使用字符数组而非String操作
char[] arr = s.toCharArray();
// 利用System.arraycopy进行高效复制
```

**C++实现特点**：
```cpp
// 使用原生数组和指针操作
char ss[MAXN * 2];
// 避免STL容器开销
```

**Python实现特点**：
```python
# 利用字符串切片和列表推导式
processed = '#' + '#'.join(s) + '#'
# 使用列表而非字符串连接
```

### 四、算法调试与问题定位

#### 1. 调试技巧
```java
// 打印中间过程
System.out.println("i=" + i + ", c=" + c + ", r=" + r + ", len=" + len);
// 断言验证
assert i >= 0 && i < n : "索引越界";
```

#### 2. 边界测试用例
- 空字符串：""
- 单字符："a"
- 全相同字符："aaaa"
- 交替字符："ababab"
- 最大长度字符串

### 五、与其他回文算法的对比分析

| 算法 | 时间复杂度 | 空间复杂度 | 适用场景 | 优势 | 劣势 |
|------|------------|------------|----------|------|------|
| 暴力法 | O(n³) | O(1) | 教学演示 | 简单易懂 | 效率极低 |
| 中心扩展 | O(n²) | O(1) | 中等规模 | 实现简单 | 重复计算 |
| 动态规划 | O(n²) | O(n²) | 需要所有信息 | 信息完整 | 空间开销大 |
| Manacher | O(n) | O(n) | 大规模数据 | 最优效率 | 实现复杂 |

### 六、实际应用场景扩展

#### 1. 文本处理领域
- **DNA序列分析**：寻找回文序列
- **自然语言处理**：回文词识别
- **代码审查**：对称代码模式检测

#### 2. 网络安全领域
- **恶意代码检测**：回文模式识别
- **数据加密**：回文性质应用

#### 3. 机器学习关联
- **特征提取**：回文特征作为输入特征
- **数据增强**：利用回文性质生成训练数据

### 七、进阶学习路径

#### 1. 算法变种
- **回文自动机**：更高效的回文处理数据结构
- **后缀自动机**：处理更复杂的字符串模式
- **Z算法**：另一种线性字符串匹配算法

#### 2. 相关数据结构
- **Trie树**：前缀匹配
- **后缀数组**：字符串排序和匹配
- **AC自动机**：多模式匹配

## 总结

## 代码测试与验证

### 编译测试

所有C++代码文件已经通过编译测试，确保语法正确性：

```bash
# 编译测试示例
g++ -o test_compilation Code06_PalindromePartitioning.cpp
```

### 运行测试

每个代码文件都包含详细的测试用例，可以验证算法的正确性：

```python
# Python测试示例
python Code02_NumberOfPalindromeSubstrings.py
```

```java
// Java测试示例
javac Code01_LongestPalindromeSubstring.java
java Code01_LongestPalindromeSubstring
```

### 性能验证

所有算法实现都经过时间复杂度分析，确保满足最优解要求：

- **时间复杂度**: O(n) - 线性时间
- **空间复杂度**: O(n) - 线性空间

## 算法深度总结

### 一、Manacher算法核心思想

#### 1. 预处理阶段的数学原理
- **统一处理**: 通过插入特殊字符，将奇偶长度回文统一处理
- **对称性利用**: 利用回文串的镜像对称性质避免重复计算
- **边界维护**: 动态维护最右回文边界，实现线性时间复杂度

#### 2. 时间复杂度证明
```
T(n) = 预处理时间 + 主循环时间 + 扩展时间
     = O(n) + O(n) + O(n) = O(n)
```

### 二、工程化实现考量

#### 1. 多语言实现差异对比

| 特性 | Java | C++ | Python |
|------|------|-----|--------|
| 字符串处理 | String/char[] | string/char[] | str/list |
| 内存管理 | 自动垃圾回收 | 手动/智能指针 | 自动管理 |
| 性能优化 | JIT编译优化 | 编译期优化 | 解释执行 |
| 异常处理 | try-catch | try-catch | try-except |

#### 2. 边界场景处理策略
- **空字符串**: 直接返回0或空字符串
- **单字符**: 最小回文情况处理
- **全相同字符**: 最大回文情况优化
- **极端长度**: 内存预分配和边界检查

### 三、算法调试与优化

#### 1. 调试技巧
```java
// Java调试示例
System.out.println("调试信息: i=" + i + ", p[i]=" + p[i]);
```

```python
# Python调试示例
print(f"调试信息: i={i}, p[i]={p[i]}")
```

#### 2. 性能优化策略
- **内存预分配**: 避免动态内存分配开销
- **循环优化**: 减少不必要的条件判断
- **缓存友好**: 顺序访问提高缓存命中率

## 实际应用场景

### 1. 文本处理领域
- **DNA序列分析**: 寻找生物信息中的回文序列
- **自然语言处理**: 回文词识别和文本对称性分析
- **代码审查**: 对称代码模式检测

### 2. 网络安全领域
- **恶意代码检测**: 回文模式识别
- **数据加密**: 利用回文性质设计加密算法

### 3. 机器学习关联
- **特征工程**: 回文特征作为输入特征
- **数据增强**: 利用回文性质生成训练数据

## 进阶学习路径

### 1. 相关算法扩展
- **回文自动机**: 更高效的回文处理数据结构
- **后缀自动机**: 处理复杂字符串模式匹配
- **Z算法**: 另一种线性字符串匹配算法

### 2. 数据结构关联
- **Trie树**: 前缀匹配和字典搜索
- **后缀数组**: 字符串排序和模式匹配
- **AC自动机**: 多模式匹配算法

## 面试准备指南

### 1. 笔试重点
- **模板记忆**: 熟练掌握Manacher算法模板
- **边界处理**: 注意空字符串和极端情况
- **时间复杂度**: 能够分析算法复杂度

### 2. 面试技巧
- **算法原理**: 清晰讲解Manacher算法思想
- **代码实现**: 熟练编写三语言版本代码
- **问题扩展**: 能够解决相关变种问题

### 3. 常见问题
1. **Manacher算法为什么是O(n)时间复杂度？**
2. **预处理阶段插入特殊字符的作用是什么？**
3. **如何利用回文对称性优化计算？**
4. **如何处理偶数和奇数长度回文？**

## 总结

Manacher算法是字符串处理领域的重要里程碑算法，通过本专题的系统学习，您已经：

### 掌握的技能
1. **算法理解**: 深入理解Manacher算法的核心思想和数学原理
2. **代码实现**: 熟练编写Java、C++、Python三语言版本的代码
3. **问题解决**: 能够解决各类回文相关的算法问题
4. **性能优化**: 理解时间空间复杂度，进行实际性能优化
5. **工程实践**: 处理异常场景、边界情况和工程化需求

### 达到的水平
- **基础掌握**: 能够独立实现Manacher算法
- **中级应用**: 能够解决复杂的回文相关问题
- **高级优化**: 能够进行算法优化和工程化改进
- **专家级**: 能够指导他人学习和解决疑难问题

### 后续学习建议
1. **巩固基础**: 反复练习算法模板和常见题型
2. **扩展应用**: 学习回文自动机等高级数据结构
3. **实战演练**: 参与算法竞赛和实际项目开发
4. **深入研究**: 阅读相关论文和源码实现

通过持续的学习和实践，您将成为字符串算法领域的专家，为后续的算法学习和职业发展打下坚实基础。
