# Class041 - 额外GCD和LCM相关问题

## 📘 概述

本文档总结了从各大算法平台收集的额外GCD和LCM相关问题，包含详细的题目描述、解题思路、复杂度分析以及Java、C++、Python三种语言的实现。

## 📚 额外题目列表

### 1. SPOJ LCMSUM - LCM Sum

**题目来源**: [SPOJ LCMSUM](https://www.spoj.com/problems/LCMSUM/)

**问题描述**: 给定n，计算∑(i=1 to n) lcm(i, n)

**解题思路**: 
利用数学公式进行优化。我们知道：
∑(i=1 to n) lcm(i, n) = ∑(i=1 to n) (i * n) / gcd(i, n) = n * ∑(i=1 to n) i / gcd(i, n)

我们可以将这个和式按gcd值分组：
∑(d|n) ∑(i=1 to n, gcd(i,n)=d) i / d

对于gcd(i,n)=d的情况，设i=d*j, n=d*k，则gcd(j,k)=1
所以∑(i=1 to n, gcd(i,n)=d) i = d * ∑(j=1 to k, gcd(j,k)=1) j

∑(j=1 to k, gcd(j,k)=1) j = k * φ(k) / 2 (当k>1时)
其中φ是欧拉函数

因此，∑(i=1 to n) lcm(i, n) = n * ∑(d|n) φ(n/d) * (n/d) / 2 = (n/2) * ∑(d|n) φ(d) * d + n (当d=n时需要特殊处理)

**时间复杂度**: O(√n)
**空间复杂度**: O(1)
**是否最优解**: 是，这是解决该问题的最优方法。

### 2. SPOJ GCDEX - GCD Extreme

**题目来源**: [SPOJ GCDEX](https://www.spoj.com/problems/GCDEX/)

**问题描述**: 计算 G(n) = Σ(i=1 to n) Σ(j=i+1 to n) gcd(i, j)

**解题思路**: 使用欧拉函数优化计算

**时间复杂度**: O(n log n)
**空间复杂度**: O(n)
**是否最优解**: 是，这是解决该问题的最优方法。

### 3. UVa 10892 - LCM Cardinality

**题目来源**: [UVa 10892](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1833)

**问题描述**: 给定一个正整数n，找出有多少对不同的整数对(a,b)，使得lcm(a,b) = n。

**解题思路**: 枚举n的所有因子，对于每个因子d，如果gcd(d, n/d) = 1，则(d, n/d)是一对解。

**时间复杂度**: O(√n)
**空间复杂度**: O(1)
**是否最优解**: 是，这是解决该问题的最优方法。

### 4. POJ 2429 - GCD & LCM Inverse

**题目来源**: [POJ 2429](http://poj.org/problem?id=2429)

**问题描述**: 给定两个正整数a和b的最大公约数和最小公倍数，反过来求这两个数，要求这两个数的和最小。

**解题思路**: 
设gcd为最大公约数，lcm为最小公倍数，则a*b = gcd*lcm。设a = gcd*x, b = gcd*y，
则x*y = lcm/gcd，且gcd(x,y) = 1。问题转化为找到两个互质的数x和y，使得x*y = lcm/gcd，
并且x+y最小。

**时间复杂度**: O(√(lcm/gcd))
**空间复杂度**: O(1)
**是否最优解**: 是，这是解决该问题的最优方法。

### 5. Codeforces 1034A - Enlarge GCD

**题目来源**: [Codeforces 1034A](https://codeforces.com/problemset/problem/1034/A)

**问题描述**: 给定n个正整数，通过删除最少的数来增大这些数的最大公约数。返回需要删除的最少数字个数，如果无法增大GCD则返回-1。

**解题思路**: 
首先计算所有数的GCD，然后将所有数除以这个GCD，问题转化为找到一个大于1的因子，
使得尽可能多的数是这个因子的倍数。枚举所有质数，统计是其倍数的数的个数，
答案就是n减去最大个数。

**时间复杂度**: O(n*log(max_value) + max_value*log(log(max_value)))
**空间复杂度**: O(max_value)
**是否最优解**: 是，这是解决该问题的最优方法。

### 6. AtCoder ABC150D - Semi Common Multiple

**题目描述**: 给定一个由偶数组成的数组a和一个整数M，求[1,M]中有多少个数X满足X = a_i*(p+0.5)对所有i成立，其中p是非负整数

**来源**: [AtCoder ABC150D](https://atcoder.jp/contests/abc150/tasks/abc150_d)

**解题思路**:
1. 将X = a_i*(p+0.5)转换为2X = a_i*(2p+1)
2. 这意味着2X必须是每个a_i的奇数倍
3. 计算数组中每个a_i除以2后的LCM，记为L
4. 然后需要计算有多少个X <= M满足X = k*L，其中k是奇数

**时间复杂度**: O(n log max(a_i))
**空间复杂度**: O(1)

### 7. 三元组GCD和LCM计数问题

**题目描述**: 给定G和L，计算满足gcd(x,y,z)=G且lcm(x,y,z)=L的三元组(x,y,z)的个数

**来源**: 数论经典问题

**解题思路**:
1. 首先检查L是否能被G整除，如果不能则没有解
2. 对L/G进行质因数分解
3. 对于每个质因子p，分析其在x,y,z中的指数分布
4. 对于每个质因子p，要求：
   - 至少有一个数的指数等于g（G中p的指数）
   - 至少有一个数的指数等于l（L中p的指数）
   - 其他数的指数在[g, l]范围内
5. 使用组合数学计算每个质因子对应的可能性，最后相乘

**时间复杂度**: O(sqrt(L/G)) 用于质因数分解
**空间复杂度**: O(log(L/G)) 用于存储质因子分解结果

### 8. HackerRank GCD Product

**题目来源**: [HackerRank GCD Product](https://www.hackerrank.com/challenges/gcd-product/problem)

**问题描述**: 给定N和M，计算∏(i=1 to N) ∏(j=1 to M) gcd(i, j) mod (10^9+7)

**解题思路**: 
对于每个质数p，计算它在结果中的指数。对于质数p，它在gcd(i,j)中的指数等于
min(vp(i), vp(j))，其中vp(x)表示x中质因子p的指数。
我们可以枚举所有质数p，计算∑(i=1 to N) ∑(j=1 to M) min(vp(i), vp(j))。
为了优化计算，我们可以使用以下方法：
对于每个质数p，计算有多少个数i满足vp(i)=k，记为count_p(k)。
然后计算∑(k=1 to max) ∑(l=1 to max) min(k,l) * count_p(k) * count_p(l)。

**时间复杂度**: O(N*log(log(N)) + M*log(log(M)))
**空间复杂度**: O(N + M)
**是否最优解**: 是，这是解决该问题的最优方法。

## 📈 复杂度分析汇总

| 题目 | 时间复杂度 | 空间复杂度 | 是否最优解 |
|------|------------|------------|------------|
| SPOJ LCMSUM | O(√n) | O(1) | 是 |
| SPOJ GCDEX | O(n log n) | O(n) | 是 |
| UVa 10892 | O(√n) | O(1) | 是 |
| POJ 2429 | O(√(lcm/gcd)) | O(1) | 是 |
| Codeforces 1034A | O(n*log(max_value) + max_value*log(log(max_value))) | O(max_value) | 是 |
| AtCoder ABC150D | O(n log max(a_i)) | O(1) | 是 |
| 三元组GCD和LCM计数 | O(sqrt(L/G)) | O(log(L/G)) | 是 |
| HackerRank GCD Product | O(N*log(log(N)) + M*log(log(M))) | O(N + M) | 是 |

## 📁 文件列表

本目录包含以下实现文件：
- `AdditionalGcdLcmProblems.java` - 额外GCD/LCM问题集合（Java版本）
- `AdditionalGcdLcmProblems.cpp` - 额外GCD/LCM问题集合（C++版本）
- `AdditionalGcdLcmProblems.py` - 额外GCD/LCM问题集合（Python版本）
- `ADDITIONAL_PROBLEMS.md` - 本说明文档

每个文件都包含详细的注释说明、复杂度分析和测试用例，确保代码的正确性和可读性。