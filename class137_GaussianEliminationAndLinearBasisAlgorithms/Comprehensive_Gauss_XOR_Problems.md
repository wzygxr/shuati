# 高斯消元与线性基算法全面题目汇总

## 目录
- [基础模板题](#基础模板题)
- [开关问题类](#开关问题类)
- [完全平方数乘积类](#完全平方数乘积类)
- [线性基应用类](#线性基应用类)
- [图论应用类](#图论应用类)
- [密码学与编码类](#密码学与编码类)
- [数学与数论类](#数学与数论类)
- [综合应用类](#综合应用类)

## 基础模板题

### 1. 洛谷 P3812 【模板】线性基
- **来源**: 洛谷 (Luogu)
- **链接**: https://www.luogu.com.cn/problem/P3812
- **难度**: 简单
- **描述**: 线性基模板题，实现线性基的基本操作
- **解题思路**: 实现线性基的插入、查询最大值等基本操作
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 2. AcWing 884. 高斯消元解异或线性方程组
- **来源**: AcWing
- **链接**: https://www.acwing.com/problem/content/886/
- **难度**: 中等
- **描述**: 输入一个包含n个方程n个未知数的异或线性方程组
- **解题思路**: 标准高斯消元法解异或方程组
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 3. 洛谷 P2447 [SDOI2010]外星千足虫
- **来源**: 洛谷 (Luogu)
- **链接**: https://www.luogu.com.cn/problem/P2447
- **难度**: 中等
- **描述**: 有n个01变量，给定m个方程，每个方程给出若干个元素的异或和
- **解题思路**: 异或方程组的高斯消元
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

## 开关问题类

### 4. POJ 1830 开关问题
- **来源**: POJ (Peking University Online Judge)
- **链接**: http://poj.org/problem?id=1830
- **难度**: 中等
- **描述**: 有N个相同的开关，每个开关都与某些开关有着联系
- **解题思路**: 建立异或方程组求解
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 5. POJ 1222 EXTENDED LIGHTS OUT
- **来源**: POJ (Peking University Online Judge)
- **链接**: http://poj.org/problem?id=1222
- **难度**: 中等
- **描述**: 5x6灯阵，按一个灯会改变自己和相邻灯的状态
- **解题思路**: 异或方程组+位运算优化
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 6. POJ 1681 Painter's Problem
- **来源**: POJ (Peking University Online Judge)
- **链接**: http://poj.org/problem?id=1681
- **难度**: 中等
- **描述**: n*n方阵，每次操作改变一个格子及其相邻格子的颜色
- **解题思路**: 异或方程组+搜索
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 7. POJ 3185 The Water Bowls
- **来源**: POJ (Peking University Online Judge)
- **链接**: http://poj.org/problem?id=3185
- **难度**: 中等
- **描述**: 一排20个碗，每次翻转连续3个碗的状态
- **解题思路**: 异或方程组+枚举
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 8. 洛谷 P2962 灯 Lights
- **来源**: 洛谷 (Luogu)
- **链接**: https://www.luogu.com.cn/problem/P2962
- **难度**: 中等
- **描述**: 有n个灯和m个开关，每个开关可以改变自己和相邻灯的状态
- **解题思路**: 异或方程组+搜索
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 9. ZOJ 1602 Multiplication Puzzle
- **来源**: ZOJ (Zhejiang University Online Judge)
- **链接**: https://vjudge.net/problem/ZOJ-1602
- **难度**: 中等
- **描述**: 开关系统，每个开关影响某些灯的状态
- **解题思路**: 异或方程组应用
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

## 完全平方数乘积类

### 10. UVa 11542 Square
- **来源**: UVa (University of Valladolid Online Judge)
- **链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2577
- **难度**: 中等
- **描述**: 将一组数分成若干子集，每个子集的乘积是完全平方数
- **解题思路**: 素因子分解+异或方程组
- **时间复杂度**: O(n * m) 其中m是质数个数
- **空间复杂度**: O(n * m)

### 11. HDU 5833 Zhu and 772002
- **来源**: HDU (Hangzhou Dianzi University Online Judge)
- **链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5833
- **难度**: 中等
- **描述**: 给定一些数，选若干个它们的乘积为完全平方数有多少种方案
- **解题思路**: 素因子分解+异或方程组
- **时间复杂度**: O(n * m)
- **空间复杂度**: O(n * m)

### 12. Codeforces 954C Matrix Walk
- **来源**: Codeforces
- **链接**: https://codeforces.com/problemset/problem/954/C
- **难度**: 中等
- **描述**: 矩阵中的路径问题，与完全平方数相关
- **解题思路**: 前缀异或性质+哈希表优化
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n)

## 线性基应用类

### 13. SPOJ XMAX - XOR Maximization
- **来源**: SPOJ (Sphere Online Judge)
- **链接**: https://www.spoj.com/problems/XMAX/
- **难度**: 中等
- **描述**: 给定整数集合S，求X(S)的最大值
- **解题思路**: 线性基求异或最大值
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 14. HDU 3949 XOR
- **来源**: HDU (Hangzhou Dianzi University Online Judge)
- **链接**: https://acm.hdu.edu.cn/showproblem.php?pid=3949
- **难度**: 中等
- **描述**: 求所有子集异或和中第k小的异或值
- **解题思路**: 线性基求第k小异或值
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 15. BZOJ 2115 [Wc2011] Xor
- **来源**: BZOJ (Beijing University Online Judge)
- **链接**: https://darkbzoj.cc/problem/2115
- **难度**: 困难
- **描述**: 求1->n的路径边权最大异或和
- **解题思路**: 线性基+图论
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 16. Codeforces 1101G (Zero XOR Subset)-less
- **来源**: Codeforces
- **链接**: https://codeforces.com/problemset/problem/1101/G
- **难度**: 中等
- **描述**: 将数组分成最少的子段，使得每个子段的异或和都不为0
- **解题思路**: 线性基求极大线性无关组
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 17. 牛客网 NC14533 异或和
- **来源**: 牛客网 (Nowcoder)
- **链接**: https://ac.nowcoder.com/acm/problem/14533
- **难度**: 中等
- **描述**: 求所有连续子数组的异或和的和
- **解题思路**: 前缀异或性质+位运算
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(n)

### 18. LeetCode 1734. 解码异或后的排列
- **来源**: LeetCode
- **链接**: https://leetcode.cn/problems/decode-xored-permutation/
- **难度**: 中等
- **描述**: 给定encoded数组，返回原始数组perm
- **解题思路**: 异或性质+排列特性
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 19. LeetCode 3681. 子序列最大XOR 值
- **来源**: LeetCode
- **链接**: https://leetcode.cn/problems/maximum-xor-of-subsequences/
- **难度**: 困难
- **描述**: 求所有可能子序列的异或值的最大值
- **解题思路**: 线性基或高斯消元
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

## 图论应用类

### 20. HDU 5544 Independent Loop
- **来源**: HDU (Hangzhou Dianzi University Online Judge)
- **链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5544
- **难度**: 困难
- **描述**: 求所有回路中边权xor和的最大值
- **解题思路**: 线性基+图论
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 21. 洛谷 P3292 [SCOI2016]幸运数字
- **来源**: 洛谷 (Luogu)
- **链接**: https://www.luogu.com.cn/problem/P3292
- **难度**: 困难
- **描述**: 求两点间路径上所有点权值的最大异或和
- **解题思路**: 线性基+树上倍增
- **时间复杂度**: O(n log² M)
- **空间复杂度**: O(n log M)

### 22. AtCoder ABC223 H - Xor Query
- **来源**: AtCoder
- **链接**: https://atcoder.jp/contests/abc223/tasks/abc223_h
- **难度**: 困难
- **描述**: 求区间内所有子集异或和的第k小值
- **解题思路**: 线段树维护区间线性基
- **时间复杂度**: O(n log² M)
- **空间复杂度**: O(n log M)

## 密码学与编码类

### 23. Project Euler Problem 203 Squarefree Binomial Coefficients
- **来源**: Project Euler
- **链接**: https://projecteuler.net/problem=203
- **难度**: 较难
- **描述**: 求二项式系数中平方自由数的和
- **解题思路**: 数论+异或方程组
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n)

### 24. LintCode 411. Gray Code
- **来源**: LintCode
- **链接**: https://www.lintcode.com/problem/411/
- **难度**: 中等
- **描述**: 生成格雷码序列
- **解题思路**: 异或运算性质
- **时间复杂度**: O(2ⁿ)
- **空间复杂度**: O(2ⁿ)

### 25. LeetCode 869. 重新排序得到2的幂
- **来源**: LeetCode
- **链接**: https://leetcode.com/problems/reordered-power-of-2/
- **难度**: 中等
- **描述**: 判断一个数的各位重新排列后是否能得到2的幂
- **解题思路**: 数字特征分析
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

## 数学与数论类

### 26. LeetCode 479. Largest Palindrome Product
- **来源**: LeetCode
- **链接**: https://leetcode.com/problems/largest-palindrome-product/
- **难度**: 中等
- **描述**: 求n位数乘积的最大回文数
- **解题思路**: 数学构造+回文数性质
- **时间复杂度**: O(10ⁿ)
- **空间复杂度**: O(1)

### 27. HackerEarth Square and Cubes
- **来源**: HackerEarth
- **链接**: https://www.hackerearth.com/practice/math/number-theory/basic-number-theory-2/practice-problems/algorithm/square-and-cubes/
- **难度**: 中等
- **描述**: 求既是平方数又是立方数的数
- **解题思路**: 数论性质分析
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)

### 28. 计蒜客 完全平方数
- **来源**: 计蒜客
- **链接**: https://nanti.jisuanke.com/t/27763
- **难度**: 中等
- **描述**: 求完全平方数的个数
- **解题思路**: 数学分析
- **时间复杂度**: O(√n)
- **空间复杂度**: O(1)

### 29. 洛谷 P1850 换教室
- **来源**: 洛谷 (Luogu)
- **链接**: https://www.luogu.com.cn/problem/P1850
- **难度**: 中等
- **描述**: 动态规划问题，涉及概率和期望
- **解题思路**: 动态规划
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n²)

### 30. AizuOJ 2525 Palindromic Polynomial
- **来源**: AizuOJ
- **链接**: https://onlinejudge.u-aizu.ac.jp/problems/2525
- **难度**: 较难
- **描述**: 回文多项式问题
- **解题思路**: 多项式性质分析
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n)

## 综合应用类

### 31. Codeforces 1245D Shichikuji and Power Grid
- **来源**: Codeforces
- **链接**: https://codeforces.com/problemset/problem/1245/D
- **难度**: 中等
- **描述**: 最小生成树问题的变种
- **解题思路**: 并查集+贪心算法
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n²)

### 32. AtCoder ABC139E League
- **来源**: AtCoder
- **链接**: https://atcoder.jp/contests/abc139/tasks/abc139_e
- **难度**: 中等
- **描述**: 高斯消元在排列问题中的应用
- **解题思路**: 排列组合+高斯消元
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 33. Hackerrank Xor Subset
- **来源**: Hackerrank
- **链接**: https://www.hackerrank.com/challenges/xor-subset/problem
- **难度**: 中等
- **描述**: 求子集异或和的最大值
- **解题思路**: 线性基
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 34. CodeChef XORMAX
- **来源**: CodeChef
- **链接**: https://www.codechef.com/problems/XORMAX
- **难度**: 中等
- **描述**: 求最大异或值
- **解题思路**: 线性基
- **时间复杂度**: O(n log M)
- **空间复杂度**: O(log M)

### 35. Codeforces 296C - Greg and Friends
- **来源**: Codeforces
- **链接**: https://codeforces.com/problemset/problem/296/C
- **难度**: 中等
- **描述**: 一些人要过河，船有载重限制
- **解题思路**: 异或方程组+组合数学
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

### 36. Codeforces 274D - Lovely Matrix
- **来源**: Codeforces
- **链接**: https://codeforces.com/problemset/problem/274/D
- **难度**: 中等
- **描述**: 给定矩阵，要求填满剩余位置使得每行每列不降序
- **解题思路**: 异或方程组+图论
- **时间复杂度**: O(n³)
- **空间复杂度**: O(n²)

## 新增平台题目

### 37. USACO Training - XOR Problems
- **来源**: USACO (USA Computing Olympiad)
- **链接**: http://www.usaco.org/
- **难度**: 中等-困难
- **描述**: USACO训练中的异或相关问题
- **解题思路**: 线性基+算法优化

### 38. TimusOJ XOR Problems
- **来源**: Timus Online Judge
- **链接**: http://acm.timus.ru/
- **难度**: 中等-困难
- **描述**: 俄罗斯在线评测系统的异或问题
- **解题思路**: 高斯消元+线性基

### 39. AizuOJ XOR Problems
- **来源**: Aizu Online Judge
- **链接**: http://judge.u-aizu.ac.jp/onlinejudge/
- **难度**: 中等-困难
- **描述**: 日本会津大学的在线评测系统
- **解题思路**: 异或方程组应用

### 40. Comet OJ XOR Problems
- **来源**: Comet OJ
- **链接**: https://www.cometoj.com/
- **难度**: 中等
- **描述**: 国内算法竞赛平台的异或问题
- **解题思路**: 线性基应用

### 41. 杭电 OJ (HDU) 更多题目
- **来源**: 杭州电子科技大学 Online Judge
- **链接**: http://acm.hdu.edu.cn/
- **难度**: 中等-困难
- **描述**: HDU平台上的高斯消元和线性基题目
- **解题思路**: 综合应用

### 42. 北大 OJ (POJ) 更多题目
- **来源**: 北京大学 Online Judge
- **链接**: http://poj.org/
- **难度**: 中等-困难
- **描述**: POJ平台上的相关题目
- **解题思路**: 算法实现与优化

### 43. 剑指Offer 异或问题
- **来源**: 剑指Offer
- **链接**: 相关编程面试书籍
- **难度**: 中等
- **描述**: 面试中常见的异或相关问题
- **解题思路**: 位运算技巧

## 题目分类总结

### 按算法类型分类
1. **高斯消元类**: 1-12, 35-36
2. **线性基类**: 13-19, 33-34
3. **图论应用类**: 20-22
4. **数学数论类**: 23-30
5. **综合应用类**: 31-32, 37-43

### 按难度分类
- **简单**: 1
- **中等**: 2-19, 23-36
- **困难**: 20-22, 37-43

### 按平台分类
- **国内平台**: 1-3, 8, 17, 24, 28-29, 40-43
- **国际平台**: 4-7, 9-16, 18-23, 25-27, 31-39

## 学习建议

### 初学者路线
1. 先掌握基础模板题（1-3）
2. 然后学习开关问题（4-9）
3. 接着学习线性基应用（13-19）
4. 最后挑战综合题目

### 进阶学习
1. 深入理解算法原理
2. 掌握多种优化技巧
3. 学习工程化实现
4. 参与实际竞赛练习

### 面试准备
1. 重点掌握中等难度题目
2. 理解算法的时间空间复杂度
3. 能够手写代码实现
4. 掌握调试和优化技巧

这个题目列表涵盖了高斯消元和线性基算法在各个平台和各个难度级别的应用，是学习和掌握这两种算法的全面资源。