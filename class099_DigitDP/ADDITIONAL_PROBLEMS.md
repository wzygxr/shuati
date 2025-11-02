# 数位DP扩展题目列表

以下是在学习数位DP过程中可以练习的更多题目，涵盖各大OJ平台。

## 一、LeetCode题目

### 1. 233. 数字 1 的个数
- **题目链接**: https://leetcode.cn/problems/number-of-digit-one/
- **题目描述**: 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
- **相关文件**: Code05_CountDigitOne.java, Code05_CountDigitOne.py

### 2. 600. 不含连续1的非负整数
- **题目链接**: https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
- **题目描述**: 给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。
- **相关文件**: Code06_NonNegativeIntegersWithoutConsecutiveOnes.java, Code06_NonNegativeIntegersWithoutConsecutiveOnes.py

### 3. 902. 最大为 N 的数字组合
- **题目链接**: https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
- **题目描述**: 给定一个按 非递减顺序 排列的数字数组 digits 。你可以用任意次数 digits[i] 来写的数字。返回可以生成的小于或等于给定整数 n 的正整数的个数。
- **相关文件**: Code02_NumbersAtMostGivenDigitSet.java

### 4. 1012. 至少有 1 位重复的数字
- **题目链接**: https://leetcode.cn/problems/numbers-with-repeated-digits/
- **题目描述**: 给定正整数 n，返回在 [1, n] 范围内具有至少 1 位重复数字的正整数的个数。
- **相关文件**: Code04_NumbersWithRepeatedDigits.java

### 5. 357. 统计各位数字都不同的数字个数
- **题目链接**: https://leetcode.cn/problems/count-numbers-with-unique-digits/
- **题目描述**: 给你一个整数 n，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n。
- **相关文件**: Code01_CountNumbersWithUniqueDigits.java

### 6. 2719. 统计整数数目
- **题目链接**: https://leetcode.cn/problems/count-of-integers/
- **题目描述**: 给你两个数字字符串 num1 和 num2 ，以及两个整数 max_sum 和 min_sum 。如果一个整数 x 满足以下条件，我们称它是一个好整数：num1 <= x <= num2；min_sum <= digit_sum(x) <= max_sum。请你返回好整数的数目。答案可能很大，请你返回答案对 10^9 + 7 取余后的结果。
- **相关文件**: Code03_CountOfIntegers.java

### 7. 2827. 范围中美丽整数的数目
- **题目链接**: https://leetcode.cn/problems/number-of-beautiful-integers-in-the-range/
- **题目描述**: 给你两个正整数：low 和 high 。如果一个整数满足以下条件，我们称它为美丽整数：1. 偶数数位的数目与奇数数位的数目相同；2. 这个整数能被 k 整除。请你返回范围 [low, high] 中美丽整数的数目。
- **相关文件**: Code07_NumberOfBeautifulIntegersInTheRange.java, Code07_NumberOfBeautifulIntegersInTheRange.py, Code07_NumberOfBeautifulIntegersInTheRange.cpp

### 8. 1397. 找到所有好字符串
- **题目链接**: https://leetcode.cn/problems/find-all-good-strings/
- **题目描述**: 给你两个长度为 n 的字符串 s1 和 s2，以及一个字符串 evil。请你返回好字符串的数目。好字符串的定义是：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，且不包含 evil 为子字符串。
- **相关文件**: Code08_FindAllGoodStrings.java, Code08_FindAllGoodStrings.py, Code08_FindAllGoodStrings.cpp

## 二、洛谷题目

### 1. P2602 [ZJOI2010] 数字计数
- **题目链接**: https://www.luogu.com.cn/problem/P2602
- **题目描述**: 给定两个正整数 a 和 b，求在 [a,b] 范围上的所有整数中，每个数码 (digit) 各出现了多少次。
- **相关文件**: Code09_DigitCount.java, Code09_DigitCount.py, Code09_DigitCount.cpp

### 2. P3414 SAC#1 - 组合数
- **题目链接**: https://www.luogu.com.cn/problem/P3414
- **题目描述**: 求 C(n,0) + C(n,1) + ... + C(n,n) 的值，其中 C(n,m) 表示组合数。

### 3. P4127 [AHOI2009] 同类分布
- **题目链接**: https://www.luogu.com.cn/problem/P4127
- **题目描述**: 给出两个数 a,b，求出 [a,b] 中各位数字之和能整除原数的数的个数。

### 4. P4124 [CQOI2016] 手机号码
- **题目链接**: https://www.luogu.com.cn/problem/P4124
- **题目描述**: 给定一个区间 [L,R]，统计这个区间内满足特定条件的手机号码个数。条件包括：不能出现4；不能出现连续3个相同的数字；后四位必须是相同数字；后五位必须是顺子（例如12345）。

### 5. P4317 花神的数论题
- **题目链接**: https://www.luogu.com.cn/problem/P4317
- **题目描述**: 定义 sum(i) 表示 i 的二进制表示中 1 的个数。给出一个正整数 N，求 sum(1) 到 sum(N) 的乘积。

## 三、牛客网题目

### 1. 数位dp - 数位小孩
- **题目链接**: https://ac.nowcoder.com/acm/problem/17023
- **题目描述**: 给出一个区间 [l,r]，求这个区间内有多少个数字满足特定条件。

## 四、Codeforces题目

### 1. 55D. Beautiful numbers
- **题目链接**: https://codeforces.com/problemset/problem/55/D
- **题目描述**: 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。给定区间 [l,r]，求其中美丽数字的个数。

### 2. 628D. Magic Numbers
- **题目链接**: https://codeforces.com/problemset/problem/628/D
- **题目描述**: 定义一个magic number为满足特定条件的数字，给定区间 [a,b]，求其中magic number的个数。

### 3. 464C. Substitutes in Number
- **题目链接**: https://codeforces.com/problemset/problem/464/C
- **题目描述**: 给定一个初始数字字符串和一系列替换规则，求经过替换后得到的数字对某个数取模的结果。

## 五、AtCoder题目

### 1. ABC135 D - Digits Parade
- **题目链接**: https://atcoder.jp/contests/abc135/tasks/abc135_d
- **题目描述**: 给定一个由数字和?组成的字符串，?可以替换成0-9的任意数字，求有多少种替换方案使得结果能被13整除。

### 2. ABC102 D - Equal Cut
- **题目链接**: https://atcoder.jp/contests/abc102/tasks/arc100_b
- **题目描述**: 给定一个数组，将其分为4段，使得4段和的最大值与最小值差值最小。

## 六、HackerRank题目

### 1. Digit DP - Special Numbers
- **题目链接**: https://www.hackerrank.com/contests/hourrank-21/challenges
- **题目描述**: 给定一个区间 [l,r]，统计满足特定条件的数字个数。

## 七、面试高频题

### 1. 统计区间内特定数字出现次数
- **题目描述**: 给定区间 [l,r] 和数字 d，统计 d 在区间内所有数字中出现的次数。

### 2. 统计满足数位和条件的数字
- **题目描述**: 给定区间 [l,r] 和数位和范围 [min_sum, max_sum]，统计满足条件的数字个数。

### 3. 统计不含特定模式的数字
- **题目描述**: 给定区间 [l,r]，统计不含特定数字模式（如连续相同数字、连续递增等）的数字个数。

## 数位DP常见题型总结

### 1. 基础计数问题
- 统计某个数字在区间内出现次数
- 统计满足特定数位和的数字个数
- 统计不含连续相同数字的数字个数

### 2. 约束条件问题
- 数字必须由特定集合中的数字组成
- 数字必须满足特定的整除性质
- 数字必须不包含某些子串

### 3. 最优化问题
- 在满足条件的数字中找最大/最小值
- 在满足条件的数字中找具有特定性质的数字

### 4. 复合问题
- 结合字符串匹配的数位DP
- 结合图论的数位DP
- 结合博弈论的数位DP

## 数位DP解题技巧

### 1. 状态设计
- 确定当前位置
- 确定已有的约束条件（如已出现的数字个数、数位和等）
- 确定是否达到上限（isLimit）
- 确定是否已经开始填数字（isNum，处理前导零）

### 2. 转移方程
- 枚举当前位可以填入的数字
- 根据填入的数字更新状态
- 递归处理下一位

### 3. 边界条件
- 处理到末尾时的返回值
- 处理前导零的情况
- 处理上限约束的情况

### 4. 优化技巧
- 记忆化搜索避免重复计算
- 状态压缩减少空间复杂度
- 提前剪枝减少搜索空间