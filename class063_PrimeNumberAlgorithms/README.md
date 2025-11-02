# 质数算法专题 (class097)

## 📘 专题简介

本专题深入研究质数相关的算法问题，包括质数判断、质因数分解、质数计数等核心算法。质数在密码学、数论、算法竞赛等领域都有重要应用，掌握质数相关算法对提升算法能力至关重要。

## 🎯 核心知识点

### 1. 质数判断算法
- 试除法：适用于较小数字的质数判断
- Miller-Rabin测试：适用于大数的质数判断（概率性算法）

### 2. 质因数分解
- 试除法分解质因数
- 基于并查集的质因数应用

### 3. 质数筛选算法
- 埃氏筛法（Ehrlich Sieve）
- 欧拉筛法（Euler Sieve）

## 📚 详细题目列表

### 基础质数判断类

#### 1. 判断较小数字是否为质数
- **题目**：实现基础的质数判断算法
- **算法**：试除法
- **时间复杂度**：O(√n)
- **空间复杂度**：O(1)
- **适用范围**：适用于较小的数字（通常在long范围内）
- **相关文件**：
  - [Code01_SmallNumberIsPrime.java](Code01_SmallNumberIsPrime.java)
  - [Code01_SmallNumberIsPrime.py](Code01_SmallNumberIsPrime.py)
  - [Code01_SmallNumberIsPrime.cpp](Code01_SmallNumberIsPrime.cpp)

#### 2. 判断较大数字是否为质数
- **题目**：Miller-Rabin素性测试
- **算法**：Miller-Rabin测试
- **时间复杂度**：O(s * (log n)³)，其中s是测试轮数
- **空间复杂度**：O(1)
- **适用范围**：适用于大数的质数判断
- **相关文件**：
  - [Code02_LargeNumberIsPrime1.java](Code02_LargeNumberIsPrime1.java) - 基础实现
  - [Code02_LargeNumberIsPrime2.java](Code02_LargeNumberIsPrime2.java) - 使用BigInteger
  - [Code02_LargeNumberIsPrime3.java](Code02_LargeNumberIsPrime3.java) - 优化实现
  - [Code02_LargeNumberIsPrime4.java](Code02_LargeNumberIsPrime4.java) - 位运算优化实现
  - [Code02_LargeNumberIsPrime.py](Code02_LargeNumberIsPrime.py) - Python实现
  - [Code02_LargeNumberIsPrime.cpp](Code02_LargeNumberIsPrime.cpp) - C++实现

### 质因数分解类

#### 3. 数字n拆分质数因子
- **题目**：实现质因数分解算法
- **算法**：试除法分解质因数
- **时间复杂度**：O(√n)
- **空间复杂度**：O(1)
- **应用场景**：质因数分解、约数计算等
- **相关文件**：
  - [Code03_PrimeFactors.java](Code03_PrimeFactors.java)
  - [Code03_PrimeFactors.py](Code03_PrimeFactors.py)
  - [Code03_PrimeFactors.cpp](Code03_PrimeFactors.cpp)

### 质数计数类

#### 4. 计数质数
- **题目**：统计小于非负整数n的质数数量
- **算法**：埃氏筛法、欧拉筛法
- **时间复杂度**：
  - 埃氏筛：O(n * log(log n))
  - 欧拉筛：O(n)
- **空间复杂度**：O(n)
- **相关文件**：
  - [Code04_EhrlichAndEuler.java](Code04_EhrlichAndEuler.java)
  - [Code04_EhrlichAndEuler.py](Code04_EhrlichAndEuler.py)
  - [Code04_EhrlichAndEuler.cpp](Code04_EhrlichAndEuler.cpp)

## 🌐 各大平台题目来源（扩展版）

### LeetCode系列（扩展）
1. **LeetCode 204. Count Primes (计数质数)**
   - **题目链接**：https://leetcode.cn/problems/count-primes/
   - **题目描述**：统计所有小于非负整数 n 的质数的数量
   - **解法**：埃氏筛法、欧拉筛法、分段筛法
   - **时间复杂度**：O(n log log n) - O(n)
   - **空间复杂度**：O(n)
   - **最优解**：欧拉筛法（线性筛）

2. **LeetCode 313. Super Ugly Number (超级丑数)**
   - **题目链接**：https://leetcode.cn/problems/super-ugly-number/
   - **题目描述**：超级丑数是指其所有质因数都是长度为 k 的质数列表 primes 中的正整数
   - **解法**：最小堆、动态规划、多指针法
   - **时间复杂度**：O(n log k)
   - **空间复杂度**：O(n + k)
   - **最优解**：动态规划+多指针

3. **LeetCode 264. Ugly Number II (丑数 II)**
   - **题目链接**：https://leetcode.cn/problems/ugly-number-ii/
   - **题目描述**：给你一个整数 n ，请你找出并返回第 n 个 丑数
   - **解法**：动态规划、三指针法
   - **时间复杂度**：O(n)
   - **空间复杂度**：O(n)
   - **最优解**：三指针动态规划

4. **LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小)**
   - **题目链接**：https://leetcode.cn/problems/largest-component-size-by-common-factor/
   - **题目描述**：给定一个由不同正整数组成的非空数组 nums，如果 nums[i] 和 nums[j] 有一个大于1的公因子，那么这两个数之间有一条无向边，返回 nums 中最大连通组件的大小
   - **解法**：并查集、质因数分解、哈希优化
   - **时间复杂度**：O(n √v) 其中v是最大元素值
   - **空间复杂度**：O(n + v)
   - **最优解**：并查集+质因数分解

5. **LeetCode 1201. Ugly Number III (丑数 III)**
   - **题目链接**：https://leetcode.cn/problems/ugly-number-iii/
   - **题目描述**：请你帮忙设计一个程序，用来找出第 n 个丑数，丑数是可以被 a、b 或 c 整除的正整数
   - **解法**：二分查找+容斥原理
   - **时间复杂度**：O(log n)
   - **空间复杂度**：O(1)
   - **最优解**：二分查找+容斥原理

6. **LeetCode 762. Prime Number of Set Bits in Binary Representation (二进制表示中质数个计算置位)**
   - **题目链接**：https://leetcode.cn/problems/prime-number-of-set-bits-in-binary-representation/
   - **题目描述**：给定两个整数 L 和 R，找到闭区间 [L, R] 范围内，计算置位位数为质数的整数个数
   - **解法**：位运算+质数判断
   - **时间复杂度**：O((R-L) log R)
   - **空间复杂度**：O(1)
   - **最优解**：预处理质数表+位运算

7. **LeetCode 1952. Three Divisors (三除数)**
   - **题目链接**：https://leetcode.cn/problems/three-divisors/
   - **题目描述**：给你一个整数 n，如果 n 恰好有三个正除数，返回 true；否则，返回 false
   - **解法**：数学性质分析
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：判断是否为质数的平方

8. **LeetCode 2427. Number of Common Factors (公因子的数目)**
   - **题目链接**：https://leetcode.cn/problems/number-of-common-factors/
   - **题目描述**：给你两个正整数 a 和 b，返回 a 和 b 的公因子数目
   - **解法**：最大公约数+因数分解
   - **时间复杂度**：O(√gcd(a,b))
   - **空间复杂度**：O(1)
   - **最优解**：计算gcd的因数个数

9. **LeetCode 1250. Check If It Is a Good Array (检查好数组)**
   - **题目链接**：https://leetcode.cn/problems/check-if-it-is-a-good-array/
   - **题目描述**：给你一个正整数数组 nums，你需要从中选出一个子集，使得子集中元素的最大公约数为 1
   - **解法**：数论+裴蜀定理
   - **时间复杂度**：O(n log max(nums))
   - **空间复杂度**：O(1)
   - **最优解**：检查整个数组的最大公约数是否为1

10. **LeetCode 1819. Number of Different Subsequences GCDs (不同的子序列的最大公约数数目)**
    - **题目链接**：https://leetcode.cn/problems/number-of-different-subsequences-gcds/
    - **题目描述**：给你一个由正整数组成的数组 nums，计算并返回 nums 的所有非空子序列中不同最大公约数的数目
    - **解法**：数论+因数分解
    - **时间复杂度**：O(n + m log m) 其中m是最大值
    - **空间复杂度**：O(m)
    - **最优解**：枚举所有可能的gcd值

### POJ系列（扩展）
1. **POJ 1811 Prime Test (大素数判定)**
   - **题目链接**：http://poj.org/problem?id=1811
   - **题目描述**：给定一个大整数(2 <= N < 2^54)，判断它是否为素数，如果不是输出最小质因子
   - **解法**：Miller-Rabin测试、Pollard-Rho算法
   - **时间复杂度**：O(n^(1/4))
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin+Pollard-Rho

2. **POJ 3641 Pseudoprime numbers (伪素数)**
   - **题目链接**：http://poj.org/problem?id=3641
   - **题目描述**：判断一个数是否是伪素数（满足费马小定理但不是素数）
   - **解法**：费马测试+质数判断
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：费马测试+质数判断

3. **POJ 2142 The Balance (天平问题)**
   - **题目链接**：http://poj.org/problem?id=2142
   - **题目描述**：用两种砝码称量物品，求最小砝码数量
   - **解法**：扩展欧几里得算法
   - **时间复杂度**：O(log min(a,b))
   - **空间复杂度**：O(1)
   - **最优解**：扩展欧几里得算法

### Codeforces系列（扩展）
1. **Codeforces 271B Prime Matrix (质数矩阵)**
   - **题目链接**：https://codeforces.com/problemset/problem/271/B
   - **题目描述**：给定一个矩阵，通过最少的移动次数将其转换为素数矩阵
   - **解法**：预处理质数、贪心算法
   - **时间复杂度**：O(nm + v log log v)
   - **空间复杂度**：O(v)
   - **最优解**：埃氏筛+贪心

2. **Codeforces 679A Bear and Prime 100 (交互题)**
   - **题目链接**：https://codeforces.com/problemset/problem/679/A
   - **题目描述**：系统想了一个2到100之间的数，你需要通过最多20次询问判断这个数是否为质数
   - **解法**：交互式算法、质数性质
   - **时间复杂度**：O(1)
   - **空间复杂度**：O(1)
   - **最优解**：询问小质数的倍数

3. **Codeforces 735A Ostap and Grasshopper (蚱蜢问题)**
   - **题目链接**：https://codeforces.com/problemset/problem/735/A
   - **题目描述**：蚱蜢在网格上跳跃，判断能否到达目标位置
   - **解法**：BFS、数论
   - **时间复杂度**：O(n)
   - **空间复杂度**：O(n)
   - **最优解**：BFS

4. **Codeforces 1332E Height All the Same (高度相同)**
   - **题目链接**：https://codeforces.com/problemset/problem/1332/E
   - **题目描述**：通过操作使所有格子高度相同
   - **解法**：组合数学、奇偶性分析
   - **时间复杂度**：O(1)
   - **空间复杂度**：O(1)
   - **最优解**：数学公式推导

5. **Codeforces 1465A Odd Divisor (奇数因子)**
   - **题目链接**：https://codeforces.com/problemset/problem/1465/A
   - **题目描述**：判断一个数是否有大于1的奇数因子
   - **解法**：数学性质
   - **时间复杂度**：O(1)
   - **空间复杂度**：O(1)
   - **最优解**：判断是否为2的幂

### 洛谷系列（扩展）
1. **Luogu U148828 大数质数判断**
   - **题目链接**：https://www.luogu.com.cn/problem/U148828
   - **题目描述**：判断给定的大整数是否为质数
   - **解法**：Miller-Rabin测试
   - **时间复杂度**：O(k log³n)
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin

2. **Luogu P1217 [USACO1.5] 回文质数 Prime Palindromes**
   - **题目链接**：https://www.luogu.com.cn/problem/P1217
   - **题目描述**：找出所有在 [a,b] 范围内的回文质数
   - **解法**：生成回文数+质数判断
   - **时间复杂度**：O(√b log b)
   - **空间复杂度**：O(1)
   - **最优解**：先生成回文数再判断质数

### UVa OJ系列
1. **UVa 10140 Prime Distance (质数距离)**
   - **题目链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1081
   - **题目描述**：给定两个整数L和U，求区间[L,U]内相邻质数的最大和最小距离
   - **解法**：分段筛法
   - **时间复杂度**：O(√U + (U-L) log log U)
   - **空间复杂度**：O(√U)
   - **最优解**：分段筛法

2. **UVa 10780 Again Prime? No Time (又是质数？没时间了)**
   - **题目链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1721
   - **题目描述**：计算最大的指数k，使得m^k可以整除n!
   - **解法**：质因数分解+勒让德公式
   - **时间复杂度**：O(√m + log n)
   - **空间复杂度**：O(1)
   - **最优解**：质因数分解

### SPOJ系列
1. **SPOJ TDPRIMES - Printing some primes (打印质数)**
   - **题目链接**：https://www.spoj.com/problems/TDPRIMES/
   - **题目描述**：打印前5000000个质数
   - **解法**：欧拉筛法
   - **时间复杂度**：O(n)
   - **空间复杂度**：O(n)
   - **最优解**：欧拉筛法

### HackerRank系列
1. **HackerRank Primality Test (质数测试)**
   - **题目链接**：https://www.hackerrank.com/challenges/primality-test/problem
   - **题目描述**：使用Miller-Rabin算法判断一个数是否是质数
   - **解法**：Miller-Rabin测试
   - **时间复杂度**：O(k log³n)
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin

### Project Euler系列
1. **Project Euler Problem 3 Largest prime factor (最大质因数)**
   - **题目链接**：https://projecteuler.net/problem=3
   - **题目描述**：找出600851475143的最大质因数
   - **解法**：试除法分解质因数
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

2. **Project Euler Problem 7 10001st prime (第10001个质数)**
   - **题目链接**：https://projecteuler.net/problem=7
   - **题目描述**：找到第10001个质数
   - **解法**：欧拉筛法
   - **时间复杂度**：O(n)
   - **空间复杂度**：O(n)
   - **最优解**：欧拉筛法

3. **Project Euler Problem 10 Summation of primes (质数求和)**
   - **题目链接**：https://projecteuler.net/problem=10
   - **题目描述**：计算2000000以下所有质数的和
   - **解法**：埃氏筛法
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：埃氏筛法

### 国内平台系列
1. **HDU 2098 分拆素数和**
   - **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=2098
   - **题目描述**：把一个偶数拆成两个不同素数的和，有几种拆法呢？
   - **解法**：质数判断+枚举
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：预处理质数表+枚举

2. **HDU 1719 Friend or Foe (朋友还是敌人)**
   - **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=1719
   - **题目描述**：判断一个数是否是友好数或敌人
   - **解法**：数论性质
   - **时间复杂度**：O(1)
   - **空间复杂度**：O(1)
   - **最优解**：数学性质判断

3. **牛客网 NC15688 质数拆分**
   - **题目链接**：https://ac.nowcoder.com/acm/problem/15688
   - **题目描述**：将一个数拆分成若干个质数之和
   - **解法**：动态规划+质数判断
   - **时间复杂度**：O(n²)
   - **空间复杂度**：O(n)
   - **最优解**：动态规划

4. **acwing 866. 试除法判定质数**
   - **题目链接**：https://www.acwing.com/problem/content/868/
   - **题目描述**：使用试除法判定一个数是否是质数
   - **解法**：试除法
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

5. **acwing 867. 分解质因数**
   - **题目链接**：https://www.acwing.com/problem/content/869/
   - **题目描述**：分解质因数，结合质数判断
   - **解法**：试除法分解
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

### 其他国际平台
1. **AtCoder ABC152 D - Handstand 2 (倒立2)**
   - **题目链接**：https://atcoder.jp/contests/abc152/tasks/abc152_d
   - **题目描述**：涉及质数的判断和应用
   - **解法**：数论+组合
   - **时间复杂度**：O(n)
   - **空间复杂度**：O(1)
   - **最优解**：数学推导

2. **TimusOJ 1007 数学问题**
   - **题目链接**：https://acm.timus.ru/problem.aspx?space=1&num=1007
   - **题目描述**：判断一个数是否是质数
   - **解法**：质数判断
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

3. **AizuOJ 0100 Prime Factorize (质因数分解)**
   - **题目链接**：https://onlinejudge.u-aizu.ac.jp/problems/0100
   - **题目描述**：对输入的数进行质因数分解
   - **解法**：试除法分解
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

4. **CodeChef PRIME1 Prime Generator (质数生成器)**
   - **题目链接**：https://www.codechef.com/problems/PRIME1
   - **题目描述**：生成区间内的所有质数
   - **解法**：分段筛法
   - **时间复杂度**：O((R-L) log log R + √R)
   - **空间复杂度**：O(√R)
   - **最优解**：分段筛法

5. **TopCoder SRM 769 Div1 Easy PrimeFactorization (质因数分解)**
   - **题目链接**：https://community.topcoder.com/stat?c=problem_statement&pm=15772
   - **题目描述**：质因数分解问题
   - **解法**：试除法
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

6. **MarsCode 质数检测**
   - **题目链接**：https://www.mars.pub/code/view/1000000028
   - **题目描述**：实现一个高效的质数检测算法
   - **解法**：Miller-Rabin测试
   - **时间复杂度**：O(k log³n)
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin

7. **计蒜客 质数判定**
   - **题目链接**：https://www.jisuanke.com/course/705/28547
   - **题目描述**：实现质数判定算法
   - **解法**：试除法
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

8. **剑指Offer II 002. 二进制中1的个数**
   - **题目链接**：https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
   - **题目描述**：统计二进制中1的个数，可与质数判断结合
   - **解法**：位运算
   - **时间复杂度**：O(log n)
   - **空间复杂度**：O(1)
   - **最优解**：位运算

9. **LOJ #10205. 「一本通 6.5 例 2」Prime Distance (质数距离)**
   - **题目链接**：https://loj.ac/p/10205
   - **题目描述**：求区间内的质数距离
   - **解法**：分段筛法
   - **时间复杂度**：O(√R + (R-L) log log R)
   - **空间复杂度**：O(√R)
   - **最优解**：分段筛法

10. **Comet OJ Contest #1 A 整数规划**
    - **题目链接**：https://cometoj.com/contest/24/problem/A?problemId=1058
    - **题目描述**：涉及质数的判断和应用
    - **解法**：数论
    - **时间复杂度**：O(1)
    - **空间复杂度**：O(1)
    - **最优解**：数学推导

## 🧠 算法思路与技巧总结（扩展版）

### 质数判断技巧深度分析
1. **试除法优化策略**：
   - **数学原理**：如果n是合数，则必有一个因子≤√n
   - **优化点1**：特判2后只检查奇数，减少一半计算量
   - **优化点2**：预处理小质数表（2,3,5,7,11,13,17,19,23,29,31,37,41,43,47）
   - **优化点3**：使用i*i <= n避免重复计算平方根
   - **适用场景**：n ≤ 10^12（64位整数范围内）
   - **时间复杂度**：O(√n) 最坏情况，O(√n / log n) 平均情况
   - **空间复杂度**：O(1)
   - **工程实践**：对于n < 10^6是最优选择

2. **Miller-Rabin测试深度解析**：
   - **数学基础**：费马小定理 + 二次探测定理
   - **误判率分析**：单次测试误判率≤1/4，12次测试误判率≤(1/4)^12 ≈ 6×10^-8
   - **确定性测试**：对于n < 2^64，使用特定底数集合可实现确定性测试
   - **底数选择**：
     - n < 2^32: {2, 7, 61}
     - n < 2^64: {2, 325, 9375, 28178, 450775, 9780504, 1795265022}
   - **工程实践**：结合试除法预处理小质数，提高效率
   - **时间复杂度**：O(k * (log n)^3)，其中k是测试轮数
   - **空间复杂度**：O(1)
   - **适用场景**：10^6 ≤ n < 10^18

3. **Pollard-Rho算法（大数分解）**：
   - **适用场景**：n > 10^18的超大数分解
   - **时间复杂度**：O(n^(1/4)) 期望复杂度
   - **算法思想**：随机漫步+生日悖论原理
   - **优化技巧**：Brent循环检测优化
   - **空间复杂度**：O(1)
   - **工程应用**：RSA密码破解、大数分解挑战

4. **AKS质数测试**：
   - **理论意义**：第一个多项式时间的确定性质数测试算法
   - **时间复杂度**：O(log^6 n) 理论最优
   - **实际应用**：理论价值大于实际应用价值
   - **空间复杂度**：O(log n)
   - **工程局限**：常数因子过大，实际效率不如Miller-Rabin

### 质数筛选算法对比分析
1. **埃氏筛法（Eratosthenes Sieve）**：
   - **核心思想**：标记每个质数的所有倍数
   - **时间复杂度**：O(n log log n) - 基于质数定理的调和级数推导
   - **空间复杂度**：O(n) - 使用布尔数组标记
   - **空间优化**：位压缩（每个数用1bit表示），可减少内存使用8倍
   - **分段优化**：处理大范围时使用分段筛法，空间复杂度降至O(√n)
   - **适用场景**：n ≤ 10^7，内存充足，需要简单实现
   - **工程实践**：从i*i开始标记，跳过偶数优化

2. **欧拉筛法（Euler Sieve/Linear Sieve）**：
   - **核心思想**：每个合数只被最小质因子筛掉一次
   - **时间复杂度**：O(n) - 线性复杂度，每个数只被访问一次
   - **空间复杂度**：O(n)存储标记数组 + O(n/log n)存储质数列表
   - **关键优化**：当i % prime[j] == 0时break，保证线性复杂度
   - **优势**：同时得到质数列表和质数个数，适合需要质数列表的场景
   - **适用场景**：n ≤ 10^8，需要高效获取质数列表
   - **工程局限**：内存使用较大，不适合极大范围

3. **分段筛法（Segmented Sieve）**：
   - **核心思想**：将大区间[L, R]分成大小为√R的小段，逐段筛选
   - **时间复杂度**：O((R-L) log log R + √R) - 接近线性
   - **空间复杂度**：O(√R) - 只需存储小质数和当前段的标记数组
   - **适用场景**：R接近10^12甚至更大，内存受限环境
   - **实现技巧**：计算每段的起始位置，使用小质数筛掉当前段合数
   - **工程优势**：可以处理超出内存限制的超大范围

4. **轮式筛法（Wheel Sieve）**：
   - **核心思想**：跳过更多已知的合数模式，减少标记次数
   - **时间复杂度**：O(n / log log n) - 优于埃氏筛
   - **空间复杂度**：O(n)
   - **适用场景**：需要极致性能的质数生成
   - **实现复杂度**：较高，主要用于理论研究

5. **并行筛法（Parallel Sieve）**：
   - **核心思想**：利用多核CPU并行处理不同区段
   - **时间复杂度**：O(n log log n / p) - p为处理器数量
   - **空间复杂度**：O(n)
   - **适用场景**：多核服务器，需要处理超大规模数据
   - **工程挑战**：负载均衡、数据同步、缓存一致性

### 质因数分解高级技巧
1. **试除法分解优化**：
   - **预处理**：先检查小质数（2,3,5,7,11,13,17,19,23,29）
   - **轮式优化**：使用轮式法跳过更多合数，检查形如6k±1的数
   - **并行分解**：对超大数使用多线程并行试除
   - **时间复杂度**：O(√n) 最坏情况，O(√n / log n) 平均情况
   - **空间复杂度**：O(1)
   - **适用场景**：n ≤ 10^12，简单可靠

2. **Pollard's Rho分解**：
   - **随机函数**：f(x) = (x² + c) mod n，c为随机常数
   - **循环检测**：Floyd龟兔赛跑算法，空间复杂度O(1)
   - **优化技巧**：使用gcd批量计算减少模运算，Brent优化版本
   - **时间复杂度**：O(n^(1/4)) 期望复杂度
   - **空间复杂度**：O(1)
   - **适用场景**：10^12 < n < 10^18，中等大小合数分解
   - **工程实践**：结合试除法预处理小因子

3. **二次筛法（Quadratic Sieve）**：
   - **适用场景**：n > 10^50的超大数分解
   - **数学基础**：平方同余原理，寻找x² ≡ y² (mod n)
   - **时间复杂度**：O(exp(√(log n log log n)))
   - **空间复杂度**：O(√n)
   - **实现复杂度**：较高，主要用于密码学研究和RSA破解
   - **工程应用**：历史上曾用于分解100+位的大数

4. **数域筛法（Number Field Sieve）**：
   - **适用场景**：n > 10^100的极大数分解
   - **时间复杂度**：O(exp((64/9)^(1/3) (log n)^(1/3) (log log n)^(2/3)))
   - **空间复杂度**：极大，需要超级计算机
   - **工程意义**：目前最有效的大数分解算法
   - **实际应用**：RSA-768（232位）的分解

5. **椭圆曲线分解（ECM）**：
   - **适用场景**：寻找中等大小的质因子（10^20以内）
   - **时间复杂度**：O(exp(√(2 log p log log p)))，其中p是找到的因子
   - **空间复杂度**：O(1)
   - **优势**：特别适合寻找相对较小的质因子
   - **工程应用**：在Pollard's Rho失败时的备选方案

### 并查集在质数问题中的应用
1. **按公因数连通性分析**：
   - **问题建模**：将数字看作节点，公因数看作边，构建无向图
   - **并查集优化**：路径压缩 + 按秩合并，单次操作近似O(1)
   - **时间复杂度**：O(n α(n))，其中α(n)是反阿克曼函数，增长极慢
   - **空间复杂度**：O(n)存储父节点和秩信息
   - **适用题目**：LeetCode 952, 1627等连通性问题

2. **质因数映射技巧**：
   - **关键观察**：共享质因数的数字属于同一连通分量
   - **实现策略**：为每个质数维护第一个出现的数字索引
   - **内存优化**：使用HashMap代替大数组，只存储实际出现的质数
   - **时间复杂度**：O(n √v)，其中v是数组中最大元素值
   - **空间复杂度**：O(n + π(v))，π(v)是v以内的质数个数
   - **工程优化**：对每个数只分解到√v，剩余的大质数单独处理

3. **连通分量统计优化**：
   - **延迟合并**：先收集所有边，再批量合并
   - **按大小合并**：总是将小集合合并到大集合
   - **路径压缩**：在查找时压缩路径，优化后续查询
   - **组件跟踪**：实时维护最大组件大小，避免最后遍历

4. **多质因数处理**：
   - **质因数去重**：每个数字的质因数只处理一次
   - **最早出现原则**：每个质因数只与第一个遇到的数字合并
   - **批量操作**：对每个数字的所有质因数进行批量合并
   - **内存效率**：使用质因数到索引的映射，避免重复分解

## ⚙️ 工程化考虑（深度分析）

### 1. 异常处理与边界条件
- **输入验证**：
  - 负数处理：质数定义域为正整数
  - 0和1处理：特殊情况的明确返回
  - 溢出检测：64位整数乘法溢出预防
- **边界测试**：
  - 极小值：0,1,2,3
  - 极大值：接近数据类型上限的值
  - 特殊数：2的幂、质数的平方等

### 2. 性能优化策略
1. **算法选择依据**：
   - n < 10^6: 试除法
   - 10^6 ≤ n < 10^12: Miller-Rabin测试
   - n ≥ 10^12: 需要特殊算法或近似解

2. **内存使用优化**：
   - **位级压缩**：使用bitset代替boolean数组
   - **分段处理**：大范围数据的分块处理
   - **缓存友好**：数据访问模式优化

3. **并行计算优化**：
   - **多线程分解**：对大数使用多线程试除
   - **GPU加速**：适合大规模质数筛选
   - **分布式计算**：超大规模问题的分布式处理

### 3. 可配置性与扩展性
1. **参数配置**：
   - Miller-Rabin测试轮数可配置
   - 筛法范围动态调整
   - 内存使用上限设置

2. **插件架构**：
   - 算法选择器模式
   - 可插拔的质数测试接口
   - 自定义优化策略

### 4. 跨语言实现差异深度分析

#### Java实现深度分析
**语言特性优势**：
- **大数支持**：BigInteger类提供任意精度整数运算，内置质数测试方法
- **内存管理**：JVM自动内存管理，但需要注意GC对性能的影响
- **并发安全**：synchronized关键字、Atomic类保证线程安全
- **异常处理**：完善的异常体系，便于错误处理

**性能优化策略**：
- **JIT优化**：热点代码会被JIT编译器优化成本地代码
- **内存布局**：对象内存布局优化，减少缓存未命中
- **GC调优**：选择合适的垃圾收集器（G1、ZGC等）

**工程实践**：
- **模块化设计**：使用包(package)组织代码结构
- **单元测试**：JUnit框架提供完善的测试支持
- **性能监控**：JMX、JProfiler等工具进行性能分析

**适用场景**：
- 企业级应用，需要稳定性和可维护性
- 大规模数据处理，利用JVM的优化能力
- 需要与Spring等框架集成的场景

#### C++实现深度分析
**语言特性优势**：
- **零开销抽象**：模板元编程、内联函数、编译期计算
- **内存控制**：精确的内存管理，避免不必要的开销
- **硬件访问**：内联汇编、SIMD指令、缓存优化
- **编译优化**：强大的编译器优化（GCC、Clang、MSVC）

**性能优化策略**：
- **模板特化**：针对特定类型的优化实现
- **内存池**：自定义内存分配器减少malloc开销
- **向量化**：使用SIMD指令并行处理数据
- **缓存友好**：数据布局优化提高缓存命中率

**工程实践**：
- **RAII模式**：资源获取即初始化，避免资源泄漏
- **移动语义**：C++11的移动语义减少拷贝开销
- **constexpr**：编译期计算减少运行时开销

**适用场景**：
- 高性能计算，需要极致性能
- 系统级编程，需要直接硬件访问
- 游戏开发、图形处理等实时性要求高的场景

#### Python实现深度分析
**语言特性优势**：
- **动态类型**：灵活的变量类型，快速原型开发
- **内置大数**：自动处理大整数，无需担心溢出
- **丰富库生态**：NumPy、SciPy等科学计算库
- **解释执行**：快速迭代开发，无需编译

**性能优化策略**：
- **JIT编译**：使用PyPy、Numba等JIT编译器
- **C扩展**：使用Cython将关键代码编译为C扩展
- **向量化操作**：利用NumPy的向量化运算
- **多进程并行**：避开GIL限制，使用多进程并行

**工程实践**：
- **装饰器模式**：使用装饰器添加功能而不修改原函数
- **生成器表达式**：惰性求值节省内存
- **类型注解**：Python 3.5+的类型提示提高代码可读性

**适用场景**：
- 数据科学、机器学习应用
- 快速原型开发和脚本编写
- 需要与深度学习框架集成的场景

#### 语言选择指导
**性能优先**：
- 选择C++，利用硬件级优化和编译期计算
- 适用场景：算法竞赛、高性能计算、实时系统

**开发效率优先**：
- 选择Python，快速原型开发和丰富的库支持
- 适用场景：数据分析、机器学习、快速验证

**平衡性能与开发效率**：
- 选择Java，良好的性能与开发效率平衡
- 适用场景：企业应用、大型系统、需要长期维护的项目

**混合架构**：
- 关键算法用C++实现，上层逻辑用Python调用
- 使用JNI（Java Native Interface）或Cython混合编程
- 适用场景：需要兼顾性能和开发效率的复杂系统

### 5. 测试与验证体系
1. **单元测试覆盖**：
   - 基础功能测试：质数判断正确性
   - 边界条件测试：特殊输入处理
   - 性能基准测试：执行时间监控

2. **正确性验证**：
   - 与已知质数表对比验证
   - 交叉验证：多种算法结果对比
   - 数学性质验证：如质数定理近似验证

3. **压力测试**：
   - 大数据量测试：处理百万级质数
   - 长时间运行测试：内存泄漏检测
   - 并发安全测试：多线程环境验证

## 📈 复杂度分析（详细推导）

### 试除法质数判断复杂度分析
**数学推导**：
- **最坏情况**：n为质数，需要检查所有可能的因子，检查次数为π(√n) ≈ 2√n / log n
- **平均情况**：根据质数定理，平均需要检查的因子数量为O(√n / log n)
- **优化效果**：只检查奇数后，实际检查次数约为√n/2

**常数因子分析**：
- 每次检查包含一次模运算，现代CPU模运算约1-3个时钟周期
- 对于n=10^12，√n=10^6，检查次数约5×10^5次
- 在现代CPU上（3GHz），理论耗时约0.17-0.5毫秒

**实际性能考虑**：
- 缓存效应：小因子检查有更好的缓存局部性
- 分支预测：循环中的条件判断影响流水线效率
- 编译器优化：循环展开、向量化等优化技术

### Miller-Rabin测试复杂度分析
**数学推导**：
- **单次测试**：快速幂运算需要O(log n)次乘法，每次乘法O((log n)^2)，总O((log n)^3)
- **k次测试**：O(k (log n)^3)
- **误判率**：单次测试误判率≤1/4，k次测试误判率≤(1/4)^k

**实际性能**：
- 对于64位整数，log n ≤ 64，log³n ≤ 262,144
- 12次测试总操作数约3.15×10^6次乘法
- 现代CPU每秒可执行10^9-10^10次操作，理论耗时微秒级

**优化空间**：
- 使用Montgomery乘法减少模运算开销
- 预计算小质数的幂次加速测试
- 使用汇编优化关键循环

### 埃氏筛法复杂度推导
**数学证明**：
- 标记次数：∑_{p≤√n} ⌊n/p⌋ ≈ n ∑_{p≤√n} 1/p
- 根据Mertens定理：∑_{p≤x} 1/p ≈ log log x + M（M为Meissel-Mertens常数）
- 总标记次数：O(n log log n)

**空间复杂度优化**：
- 原始实现：O(n)字节数组，8n字节
- 位压缩：O(n)位数组，n/8字节
- 分段处理：O(√n)空间处理任意大范围

**实际性能**：
- n=10^8时，标记次数约3.5×10^8次
- 内存访问模式对性能影响巨大
- 缓存友好的实现比理论分析更重要

### 欧拉筛法复杂度证明
**线性复杂度证明**：
- 关键观察：每个合数x只被标记一次（被其最小质因子p标记）
- 标记时机：当i是x/p的最小质因子的倍数时
- 总操作数：每个数被访问一次，O(n)

**内存访问模式**：
- 顺序访问质数列表，缓存友好
- 随机访问标记数组，可能引起缓存未命中
- 实际性能受内存带宽限制

**优化策略**：
- 使用更紧凑的数据结构减少内存占用
- 预取技术优化内存访问
- 多线程并行处理不同区段

### 质因数分解复杂度
**试除法**：
- **最坏情况**：n为质数或两个大质数乘积，O(√n)
- **平均情况**：根据质数分布，O(√n / log n)
- **工程优化**：结合小质数表，实际性能更好

**Pollard's Rho算法**：
- **期望复杂度**：O(√p)，其中p是n的最小质因子
- **最坏情况**：O(√n)，当n是质数时
- **实际性能**：对于有中等大小因子的数非常高效

**算法选择策略**：
- n < 10^12：试除法
- 10^12 < n < 10^18：Pollard's Rho
- n > 10^18：需要更高级算法（二次筛、数域筛）

## 🧪 测试验证体系（完整方案）

### 1. 功能正确性测试框架

**测试用例设计原则**：
```python
# 全面的测试用例分类
test_cases = {
    # 边界值测试：特殊值和极限情况
    "boundary": [(0, False), (1, False), (2, True), (3, True)],
    
    # 小质数测试：前100个质数
    "small_primes": [(5, True), (7, True), (11, True), (13, True), ...],
    
    # 小合数测试：典型的合数模式
    "small_composites": [(4, False), (6, False), (8, False), (9, False), ...],
    
    # 大质数测试：已知的大质数
    "large_primes": [
        (1000003, True), (1000033, True), (1000037, True),
        (999983, True), (999979, True), (2147483647, True)  # 2^31-1
    ],
    
    # 大合数测试：大质数的乘积
    "large_composites": [
        (1000001, False), (1000002, False), (1000004, False),
        (999981, False), (2147483648, False)  # 2^31
    ],
    
    # 特殊数测试：卡迈克尔数、梅森素数等
    "special_numbers": [
        (561, False), (1105, False), (1729, False),  # 卡迈克尔数
        (8191, True), (131071, True), (524287, True)  # 梅森素数
    ],
    
    # 极端值测试：接近数据类型边界的值
    "extreme_values": [
        (2**31-1, True), (2**63-1, True),  # 最大有符号整数
        (10**18+1, None), (10**20+1, None)  # 超大数测试
    ]
}
```

**测试框架实现**：
```python
class PrimeTestFramework:
    def __init__(self):
        self.test_results = {}
        self.performance_stats = {}
    
    def run_functional_tests(self, algorithm, test_cases):
        """运行功能正确性测试"""
        results = {}
        for category, cases in test_cases.items():
            passed = 0
            total = len(cases)
            for n, expected in cases:
                try:
                    result = algorithm(n)
                    if expected is not None and result == expected:
                        passed += 1
                    elif expected is None:
                        # 对于未知结果，只检查是否正常返回
                        passed += 1
                except Exception as e:
                    print(f"测试失败: {n}, 错误: {e}")
            
            results[category] = (passed, total, passed/total if total > 0 else 0)
        
        return results
    
    def generate_report(self, results):
        """生成测试报告"""
        print("=== 功能正确性测试报告 ===")
        total_passed = 0
        total_cases = 0
        
        for category, (passed, total, ratio) in results.items():
            total_passed += passed
            total_cases += total
            status = "✅ 通过" if ratio == 1.0 else "⚠️ 部分通过" if ratio > 0.9 else "❌ 失败"
            print(f"{category:15} {passed:3d}/{total:3d} {ratio:.1%} {status}")
        
        overall_ratio = total_passed / total_cases if total_cases > 0 else 0
        print(f"
总体通过率: {overall_ratio:.1%} ({total_passed}/{total_cases})")
```

### 2. 性能基准测试框架

**多维度性能测试**：
```python
class PerformanceBenchmark:
    def __init__(self):
        self.performance_data = {}
    
    def benchmark_algorithm(self, algorithm, test_sizes, iterations=100):
        """基准测试算法性能"""
        results = {}
        
        for size in test_sizes:
            times = []
            memory_usage = []
            
            for _ in range(iterations):
                # 时间性能测试
                start_time = time.perf_counter()
                result = algorithm(size)
                end_time = time.perf_counter()
                times.append(end_time - start_time)
                
                # 内存使用测试
                if hasattr(self, 'measure_memory'):
                    memory_usage.append(self.measure_memory(algorithm, size))
            
            # 统计分析
            avg_time = statistics.mean(times)
            std_time = statistics.stdev(times) if len(times) > 1 else 0
            avg_memory = statistics.mean(memory_usage) if memory_usage else 0
            
            results[size] = {
                'avg_time': avg_time,
                'std_time': std_time,
                'avg_memory': avg_memory,
                'iterations': iterations
            }
        
        return results
    
    def measure_memory(self, algorithm, n):
        """测量内存使用"""
        import tracemalloc
        
        tracemalloc.start()
        algorithm(n)
        current, peak = tracemalloc.get_traced_memory()
        tracemalloc.stop()
        
        return peak / 1024  # 转换为KB
    
    def analyze_complexity(self, performance_data):
        """分析时间复杂度是否符合理论预期"""
        sizes = sorted(performance_data.keys())
        times = [performance_data[size]['avg_time'] for size in sizes]
        
        # 拟合时间复杂度曲线
        # 这里可以添加多项式拟合、对数拟合等分析
        pass
```

### 3. 并发安全测试框架

**多线程并发测试**：
```python
class ConcurrencyTest:
    def __init__(self):
        self.lock = threading.Lock()
        self.results = []
    
    def stress_test(self, algorithm, test_cases, num_threads=8, duration=10):
        """压力测试：多线程并发执行"""
        start_time = time.time()
        threads = []
        results = []
        
        def worker(worker_id, cases):
            local_results = []
            while time.time() - start_time < duration:
                for n, expected in cases:
                    try:
                        result = algorithm(n)
                        local_results.append((n, result, expected, worker_id))
                    except Exception as e:
                        local_results.append((n, None, expected, worker_id, str(e)))
            
            with self.lock:
                results.extend(local_results)
        
        # 创建并启动线程
        for i in range(num_threads):
            thread = threading.Thread(target=worker, args=(i, test_cases))
            threads.append(thread)
            thread.start()
        
        # 等待所有线程完成
        for thread in threads:
            thread.join()
        
        return self.analyze_concurrency_results(results)
    
    def analyze_concurrency_results(self, results):
        """分析并发测试结果"""
        analysis = {
            'total_operations': len(results),
            'successful_operations': 0,
            'failed_operations': 0,
            'consistency_issues': 0,
            'performance_degradation': 0
        }
        
        # 分析结果一致性和正确性
        result_map = {}
        for n, result, expected, worker_id, error in results:
            if error:
                analysis['failed_operations'] += 1
                continue
            
            analysis['successful_operations'] += 1
            
            if n not in result_map:
                result_map[n] = set()
            result_map[n].add(result)
            
            if expected is not None and result == expected:
                analysis['successful_operations'] += 1
        
        # 检查一致性：同一个输入是否产生相同输出
        for n, results_set in result_map.items():
            if len(results_set) > 1:
                analysis['consistency_issues'] += 1
        
        return analysis
```

### 4. 边界条件与异常处理测试

**异常场景测试**：
```python
class EdgeCaseTest:
    def test_edge_cases(self, algorithm):
        """测试边界条件和异常输入"""
        edge_cases = [
            # 非法输入
            (-1, ValueError), (-100, ValueError),
            # 极大值
            (10**100, None), (2**1000, None),
            # 特殊格式
            ("123", TypeError), ([123], TypeError),
            # 浮点数
            (3.14, TypeError), (1e10, TypeError)
        ]
        
        results = {}
        for input_val, expected_exception in edge_cases:
            try:
                result = algorithm(input_val)
                if expected_exception is None:
                    results[input_val] = ('PASS', result)
                else:
                    results[input_val] = ('FAIL', f"期望异常 {expected_exception}")
            except Exception as e:
                if type(e) == expected_exception:
                    results[input_val] = ('PASS', f"正确抛出 {type(e).__name__}")
                else:
                    results[input_val] = ('FAIL', f"错误异常 {type(e).__name__}")
        
        return results
```

### 5. 集成测试与持续集成

**自动化测试流水线**：
```python
class CICDTestPipeline:
    def __init__(self):
        self.test_suites = {
            'unit': UnitTestSuite(),
            'integration': IntegrationTestSuite(),
            'performance': PerformanceTestSuite(),
            'security': SecurityTestSuite()
        }
    
    def run_pipeline(self, algorithm):
        """运行完整的CI/CD测试流水线"""
        pipeline_results = {}
        
        for suite_name, suite in self.test_suites.items():
            print(f"运行 {suite_name} 测试...")
            start_time = time.time()
            
            try:
                results = suite.run(algorithm)
                duration = time.time() - start_time
                
                pipeline_results[suite_name] = {
                    'results': results,
                    'duration': duration,
                    'status': self.evaluate_results(results)
                }
                
                print(f"{suite_name} 测试完成: {pipeline_results[suite_name]['status']}")
                
            except Exception as e:
                pipeline_results[suite_name] = {
                    'error': str(e),
                    'status': 'FAILED'
                }
                print(f"{suite_name} 测试失败: {e}")
        
        return self.generate_ci_report(pipeline_results)
    
    def evaluate_results(self, results):
        """评估测试结果"""
        # 根据具体测试套件的评估标准进行判断
        if all(r['passed'] for r in results if 'passed' in r):
            return 'PASS'
        elif any(r.get('critical_failure', False) for r in results):
            return 'CRITICAL_FAILURE'
        else:
            return 'PARTIAL_FAILURE'
```

## 🔬 算法调试与问题定位

### 1. 中间过程打印调试
```python
def debug_is_prime(n):
    print(f"检查数字: {n}")
    if n <= 1:
        print("n <= 1，不是质数")
        return False
    if n == 2:
        print("n == 2，是质数")
        return True
    if n % 2 == 0:
        print("n是偶数（除了2），不是质数")
        return False
    
    print(f"开始检查奇数因子，上限: {int(n**0.5)}")
    for i in range(3, int(n**0.5) + 1, 2):
        if n % i == 0:
            print(f"找到因子: {i}，不是质数")
            return False
        print(f"检查因子: {i}，不能整除")
    
    print("没有找到因子，是质数")
    return True
```

### 2. 断言验证中间结果
```python
def validated_is_prime(n):
    # 基础断言
    assert isinstance(n, int), "输入必须是整数"
    assert n >= 0, "输入必须是非负整数"
    
    if n <= 1:
        return False
    if n <= 3:
        return True
    if n % 2 == 0:
        return False
    
    # 验证数学性质
    sqrt_n = int(n**0.5)
    assert sqrt_n * sqrt_n <= n < (sqrt_n + 1) * (sqrt_n + 1)
    
    for i in range(3, sqrt_n + 1, 2):
        if n % i == 0:
            # 验证因子正确性
            assert n % i == 0, f"{i}应该是{n}的因子"
            return False
    
    return True
```

### 3. 性能退化排查方法
```python
import cProfile
import pstats

def profile_algorithm(algorithm, test_cases):
    profiler = cProfile.Profile()
    profiler.enable()
    
    for n, _ in test_cases:
        algorithm(n)
    
    profiler.disable()
    stats = pstats.Stats(profiler)
    stats.sort_stats('cumulative').print_stats(10)
```

## 📚 参考资料与进阶学习

### 经典教材
1. **《算法导论》（Introduction to Algorithms）**
   - 第31章：数论算法
   - 质数测试、大数分解、模运算

2. **《具体数学》（Concrete Mathematics）**
   - 数论基础章节
   - 质数分布、筛法原理

3. **《算法竞赛入门经典》**
   - 第10章：数学概念与方法
   - 实战算法实现技巧

### 在线资源
1. **OEIS（整数序列在线百科全书）**
   - 质数序列：https://oeis.org/A000040
   - 相关数学序列查询

2. **Project Euler**
   - 数学与编程结合的挑战平台
   - 大量质数相关题目

3. **CP-Algorithms**
   - 数论算法详细讲解
   - 代码实现和复杂度分析

### 研究论文
1. **"Primes is in P"** - AKS质数测试
   - 第一个多项式时间的确定性质数测试算法
   - 理论意义重大，实际应用有限

2. **Miller-Rabin原始论文**
   - 概率性质数测试的理论基础
   - 误判率分析和优化策略

3. **Pollard's Rho算法论文**
   - 大数分解的随机算法
   - 实际应用中的各种优化变种

### 实用工具库
1. **GMP（GNU Multiple Precision Arithmetic Library）**
   - 高性能大数运算库
   - 包含各种质数测试和分解算法

2. **OpenSSL加密库**
   - 密码学相关的质数生成和测试
   - 工业级的安全实现

3. **SymPy数学符号计算库**
   - Python中的数学计算库
   - 质数相关函数的完整实现

## 🧩 更多质数算法题目（扩展版）

### Codeforces系列（新增）
1. **Codeforces 1062B Math**
   - **题目链接**：https://codeforces.com/contest/1062/problem/B
   - **题目描述**：给定一个数n，有两种操作：1）n乘以一个正整数x；2）n开平方根。求最少操作次数使n变为1
   - **解法**：质因数分解、数学分析
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：基于质因数分解的数学推导

2. **Codeforces 271B Prime Matrix**
   - **题目链接**：https://codeforces.com/problemset/problem/271/B
   - **题目描述**：给定一个矩阵，通过最少的移动次数将其转换为素数矩阵
   - **解法**：预处理质数、贪心算法
   - **时间复杂度**：O(nm + v log log v)
   - **空间复杂度**：O(v)
   - **最优解**：埃氏筛+贪心

### LeetCode系列（新增）
1. **LeetCode 1175. 质数排列**
   - **题目链接**：https://leetcode.cn/problems/prime-arrangements/
   - **题目描述**：求1到n的排列中，质数必须出现在质数索引位置的方案数
   - **解法**：质数计数、排列组合
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：埃氏筛法+排列组合

2. **LeetCode 2761. 和等于目标值的质数对**
   - **题目链接**：https://leetcode.cn/problems/prime-pairs-with-target-sum/
   - **题目描述**：计算和等于n的所有质数对
   - **解法**：埃氏筛法、双指针
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：筛法预处理+双指针

### 洛谷系列（新增）
1. **Luogu P5723 【深基4.例13】质数口袋**
   - **题目链接**：https://www.luogu.com.cn/problem/P5723
   - **题目描述**：从2开始依次判断自然数是否为质数，装入质数口袋直到超重
   - **解法**：试除法、质数判断
   - **时间复杂度**：O(n√n)
   - **空间复杂度**：O(1)
   - **最优解**：埃氏筛法优化

2. **Luogu P5736 【深基7.例2】质数筛**
   - **题目链接**：https://www.luogu.com.cn/problem/P5736
   - **题目描述**：输入n个正整数，去除不是质数的数字，输出剩余质数
   - **解法**：质数判断、筛选
   - **时间复杂度**：O(n√v)
   - **空间复杂度**：O(1)
   - **最优解**：埃氏筛法预处理

### POJ系列（新增）
1. **POJ 1595 Prime Cuts**
   - **题目链接**：http://poj.org/problem?id=1595
   - **题目描述**：给定N和C，输出质数序列的中心部分
   - **解法**：埃氏筛法、序列处理
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：埃氏筛法+序列截取

### SPOJ系列（新增）
1. **SPOJ PRIME1 - Prime Generator**
   - **题目链接**：https://www.spoj.com/problems/PRIME1/
   - **题目描述**：生成区间[m,n]内的所有质数
   - **解法**：分段筛法
   - **时间复杂度**：O((n-m) log log n + √n)
   - **空间复杂度**：O(√n)
   - **最优解**：分段筛法

### HackerRank系列（新增）
1. **HackerRank Primality Test**
   - **题目链接**：https://www.hackerrank.com/challenges/primality-test/problem
   - **题目描述**：使用Miller-Rabin算法判断一个数是否是质数
   - **解法**：Miller-Rabin测试
   - **时间复杂度**：O(k log³n)
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin

### Project Euler系列（新增）
1. **Project Euler Problem 10 Summation of primes**
   - **题目链接**：https://projecteuler.net/problem=10
   - **题目描述**：计算2000000以下所有质数的和
   - **解法**：埃氏筛法
   - **时间复杂度**：O(n log log n)
   - **空间复杂度**：O(n)
   - **最优解**：埃氏筛法

2. **Project Euler Problem 27 Quadratic primes**
   - **题目链接**：https://projecteuler.net/problem=27
   - **题目描述**：找出产生最多连续质数的二次多项式系数
   - **解法**：质数判断、暴力枚举
   - **时间复杂度**：O(n²√v)
   - **空间复杂度**：O(1)
   - **最优解**：埃氏筛法预处理+枚举优化

### AtCoder系列（新增）
1. **AtCoder ABC149 C - Next Prime**
   - **题目链接**：https://atcoder.jp/contests/abc149/tasks/abc149_c
   - **题目描述**：给定一个整数X，找到大于等于X的最小质数
   - **解法**：质数判断、线性搜索
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法优化

### CodeChef系列（新增）
1. **CodeChef PRIME1 Prime Generator**
   - **题目链接**：https://www.codechef.com/problems/PRIME1
   - **题目描述**：生成区间[m,n]内的所有质数
   - **解法**：分段筛法
   - **时间复杂度**：O((n-m) log log n + √n)
   - **空间复杂度**：O(√n)
   - **最优解**：分段筛法

### UVa OJ系列（新增）
1. **UVa 10140 Prime Distance**
   - **题目链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1081
   - **题目描述**：给定两个整数L和U，求区间[L,U]内相邻质数的最大和最小距离
   - **解法**：分段筛法
   - **时间复杂度**：O(√U + (U-L) log log U)
   - **空间复杂度**：O(√U)
   - **最优解**：分段筛法

### 其他平台系列（新增）
1. **HackerEarth Prime Numbers**
   - **题目链接**：https://www.hackerearth.com/practice/math/number-theory/primality-tests/practice-problems/
   - **题目描述**：判断给定数字是否为质数
   - **解法**：Miller-Rabin测试
   - **时间复杂度**：O(k log³n)
   - **空间复杂度**：O(1)
   - **最优解**：Miller-Rabin

2. **TopCoder SRM 769 Div1 Easy PrimeFactorization**
   - **题目链接**：https://community.topcoder.com/stat?c=problem_statement&pm=15772
   - **题目描述**：质因数分解问题
   - **解法**：试除法
   - **时间复杂度**：O(√n)
   - **空间复杂度**：O(1)
   - **最优解**：试除法

## 📚 参考资料与进阶学习（扩展版）

### 经典教材（扩展）
1. **《算法导论》（Introduction to Algorithms）**
   - 第31章：数论算法
   - 质数测试、大数分解、模运算

2. **《具体数学》（Concrete Mathematics）**
   - 数论基础章节
   - 质数分布、筛法原理

3. **《算法竞赛入门经典》**
   - 第10章：数学概念与方法
   - 实战算法实现技巧

4. **《初等数论》（潘承洞、潘承彪）**
   - 质数理论基础
   - 解析数论初步

5. **《计算数论》（Neal Koblitz）**
   - 现代数论算法
   - 密码学中的数论应用

### 在线资源（扩展）
1. **OEIS（整数序列在线百科全书）**
   - 质数序列：https://oeis.org/A000040
   - 相关数学序列查询

2. **Project Euler**
   - 数学与编程结合的挑战平台
   - 大量质数相关题目

3. **CP-Algorithms**
   - 数论算法详细讲解
   - 代码实现和复杂度分析

4. **数论教程（William Stein）**
   - 现代数论入门
   - SageMath实践

5. **MIT OpenCourseWare 数论课程**
   - 理论与应用并重
   - 算法实现指导

### 研究论文（扩展）
1. **"Primes is in P"** - AKS质数测试
   - 第一个多项式时间的确定性质数测试算法
   - 理论意义重大，实际应用有限

2. **Miller-Rabin原始论文**
   - 概率性质数测试的理论基础
   - 误判率分析和优化策略

3. **Pollard's Rho算法论文**
   - 大数分解的随机算法
   - 实际应用中的各种优化变种

4. **二次筛法（Quadratic Sieve）研究**
   - 大数分解的经典算法
   - 工业级实现细节

5. **数域筛法（Number Field Sieve）综述**
   - 当前最强大的大数分解算法
   - 理论分析和实践优化

### 实用工具库（扩展）
1. **GMP（GNU Multiple Precision Arithmetic Library）**
   - 高性能大数运算库
   - 包含各种质数测试和分解算法

2. **OpenSSL加密库**
   - 密码学相关的质数生成和测试
   - 工业级的安全实现

3. **SymPy数学符号计算库**
   - Python中的数学计算库
   - 质数相关函数的完整实现

4. **PARI/GP数论计算系统**
   - 专门用于数论计算的工具
   - 高效的质数相关算法实现

5. **SageMath数学软件系统**
   - 基于Python的开源数学软件
   - 集成多种数论算法和工具