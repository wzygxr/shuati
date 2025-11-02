# 随机化基础与复杂度分析进阶

本目录包含随机化基础算法、复杂度分析进阶（摊还分析与势能分析）、高精度算法拓展以及位运算技巧的实现。所有算法都提供了Java、Python和C++三种语言的实现。

## 随机化基础

### 1. Fisher-Yates 洗牌算法

**算法思想**：从数组末尾开始，将当前位置与之前的随机位置交换，确保每个元素都有相同的概率出现在任意位置。

**复杂度分析**：
- 时间复杂度：O(n)
- 空间复杂度：O(1) - 原地洗牌

**实现文件**：
- [FisherYatesShuffle.java](FisherYatesShuffle.java)
- [fisher_yates_shuffle.py](fisher_yates_shuffle.py)
- [fisher_yates_shuffle.cpp](fisher_yates_shuffle.cpp)

**相关题目**：
- [LeetCode 384. 打乱数组](https://leetcode-cn.com/problems/shuffle-an-array/)
- [LintCode 1423. 随机洗牌](https://www.lintcode.com/problem/1423/)
- [CodeChef - SHUFFLE](https://www.codechef.com/problems/SHUFFLE)
- [HackerRank - Knuth's Permutation](https://www.hackerrank.com/contests/hourrank-25/challenges/random-number-generator)
- [牛客网 - NC145 数组中的最长山脉](https://www.nowcoder.com/practice/c3120c1c1bc44ad986259c0cf0f0b80e?tpId=188)

### 2. 随机选择第k小（Randomized Quick Select）

**算法思想**：基于快速排序的思想，随机选择pivot，将数组分区，直到找到第k小的元素。

**复杂度分析**：
- 期望时间复杂度：O(n)
- 最坏时间复杂度：O(n²)
- 空间复杂度：O(log n) - 递归调用栈

**实现文件**：
- [RandomizedSelect.java](RandomizedSelect.java)
- [randomized_select.py](randomized_select.py)
- [randomized_select.cpp](randomized_select.cpp)

**相关题目**：
- [LeetCode 215. 数组中的第K个最大元素](https://leetcode-cn.com/problems/kth-largest-element-in-an-array/)
- [LintCode 5. 第k大元素](https://www.lintcode.com/problem/5/)
- [CodeChef - KTHMAX](https://www.codechef.com/problems/KTHMAX)
- [HackerRank - Kth Largest Element](https://www.hackerrank.com/challenges/find-the-running-median/problem)
- [AtCoder - ABC142 D - Disjoint Set of Common Divisors](https://atcoder.jp/contests/abc142/tasks/abc142_d)
- [洛谷 - P1923 求第k小的数](https://www.luogu.com.cn/problem/P1923)
- [牛客网 - NC145 数组中的最长山脉](https://www.nowcoder.com/practice/c3120c1c1bc44ad986259c0cf0f0b80e?tpId=188)

### 3. 哈希冲突概率估算与素数模选择

**算法思想**：
- 哈希冲突概率估算：使用生日悖论公式计算给定元素数量和哈希表大小时的冲突概率
- 素数模选择：选择合适的素数作为哈希表大小，减少冲突

**实现文件**：
- [HashCollisionAnalysis.java](HashCollisionAnalysis.java)
- [hash_collision_analysis.py](hash_collision_analysis.py)
- [hash_collision_analysis.cpp](hash_collision_analysis.cpp)

**相关题目**：
- [LeetCode 705. 设计哈希集合](https://leetcode-cn.com/problems/design-hashset/)
- [LeetCode 706. 设计哈希映射](https://leetcode-cn.com/problems/design-hashmap/)
- [LintCode 128. 哈希函数](https://www.lintcode.com/problem/128/)
- [CodeChef - HASHTABLE](https://www.codechef.com/problems/HASHTABLE)
- [HackerEarth - Hash Tables](https://www.hackerearth.com/practice/data-structures/hash-tables/basics-of-hash-tables/practice-problems/)
- [杭电OJ - 1272 小希的迷宫](http://acm.hdu.edu.cn/showproblem.php?pid=1272)

## 复杂度分析进阶：摊还分析与势能分析

### 可并堆（二项堆）实现

**算法思想**：二项堆是一组二项树的集合，每个二项树满足堆性质，支持高效的合并操作。

**摊还分析**：
- 势能函数：选择为堆中树的数量
- 合并操作的摊还时间复杂度：O(log n)
- 插入操作的摊还时间复杂度：O(1)
- 提取最小操作的摊还时间复杂度：O(log n)

**实现文件**：
- [BinomialHeap.java](BinomialHeap.java)
- [binomial_heap.py](binomial_heap.py)
- [binomial_heap.cpp](binomial_heap.cpp)

**相关题目**：
- [LeetCode 23. 合并K个排序链表](https://leetcode-cn.com/problems/merge-k-sorted-lists/)
- [LeetCode 1046. 最后一块石头的重量](https://leetcode-cn.com/problems/last-stone-weight/)
- [CodeChef - CHEFBM](https://www.codechef.com/problems/CHEFBM)
- [AtCoder - C - Min Difference](https://atcoder.jp/contests/abc129/tasks/abc129_c)
- [SPOJ - BINOMIAL HEAP](https://www.spoj.com/problems/BINHEAP/)
- [洛谷 - P1456 Monkey King](https://www.luogu.com.cn/problem/P1456)
- [牛客网 - NC145 数组中的最长山脉](https://www.nowcoder.com/practice/c3120c1c1bc44ad986259c0cf0f0b80e?tpId=188)

## 高精度拓展

### 1. Karatsuba 乘法算法

**算法思想**：Karatsuba乘法是一种快速乘法算法，通过减少乘法运算次数来加速大整数乘法。对于两个n位数x和y，传统乘法需要O(n²)次运算，而Karatsuba算法通过将乘法分解为三个子乘法，将时间复杂度优化到约O(n^1.585)。

**核心公式**：对于x = a·10^(n/2) + b，y = c·10^(n/2) + d，则x·y = ac·10^n + (ad + bc)·10^(n/2) + bd。通过计算(ac + bd + (a+b)(c+d)) = ad + bc，只需计算三个乘积：ac、bd和(a+b)(c+d)。

**实现文件**：
- [KaratsubaMultiply.java](KaratsubaMultiply.java)
- [karatsuba_multiply.py](karatsuba_multiply.py)
- [karatsuba_multiply.cpp](karatsuba_multiply.cpp)

**相关题目**：
- [LeetCode 43. 字符串相乘](https://leetcode-cn.com/problems/multiply-strings/) - 高精度乘法
- [LeetCode 66. 加一](https://leetcode-cn.com/problems/plus-one/) - 高精度加法
- [LeetCode 67. 二进制求和](https://leetcode-cn.com/problems/add-binary/) - 二进制高精度加法
- [LintCode 2. 尾部的零](https://www.lintcode.com/problem/2/) - 高精度运算相关
- [CodeChef - MAXPR](https://www.codechef.com/problems/MAXPR) - 大整数处理
- [HackerRank - Extra Long Factorials](https://www.hackerrank.com/challenges/extra-long-factorials/problem) - 超大阶乘计算
- [洛谷 - P1303 A*B Problem](https://www.luogu.com.cn/problem/P1303) - 高精度乘法
- [牛客网 - NC118 数组中的逆序对](https://www.nowcoder.com/practice/96bd6684e04a44eb80e6a68efc0ec6c5?tpId=188) - 高精度相关

### 2. Toom-Cook 乘法算法

**算法思想**：Toom-Cook是Karatsuba算法的推广，通过将n位数分解为m个部分，将时间复杂度进一步优化到约O(n^(log_m(2m-1)))。当m=2时，就是Karatsuba算法；当m=3时，复杂度约为O(n^1.465)。

**核心步骤**：
1. 拆分：将两个大数分成m个部分
2. 求值：在m+1个点上计算多项式值
3. 点乘：对应点相乘
4. 插值：通过结果点插值得到乘积多项式
5. 组合：将多项式系数组合成最终结果

**实现文件**：
- [ToomCookMultiply.java](ToomCookMultiply.java)
- [toom_cook_multiply.py](toom_cook_multiply.py)
- [toom_cook_multiply.cpp](toom_cook_multiply.cpp)

**相关题目**：
- [LeetCode 415. 字符串相加](https://leetcode-cn.com/problems/add-strings/) - 高精度加法
- [LeetCode 989. 数组形式的整数加法](https://leetcode-cn.com/problems/add-to-array-form-of-integer/) - 高精度加法变体
- [HackerRank - Very Big Sum](https://www.hackerrank.com/challenges/a-very-big-sum/problem) - 大整数求和
- [SPOJ - MULTQ3](https://www.spoj.com/problems/MULTQ3/) - 大整数处理
- [AtCoder - ABC196 C - Doubled](https://atcoder.jp/contests/abc196/tasks/abc196_c) - 数字处理
- [杭电OJ - 1042 N!](http://acm.hdu.edu.cn/showproblem.php?pid=1042) - 超大阶乘

### 3. FFT（快速傅里叶变换）乘法

**算法思想**：利用傅里叶变换将时域的卷积转换为频域的点乘，从而将多项式乘法的时间复杂度从O(n²)降低到O(n log n)。高精度整数乘法可以看作是系数相乘，因此可以通过FFT高效计算。

**核心步骤**：
1. 将两个大整数视为多项式系数
2. 对两个多项式进行FFT变换到频域
3. 在频域进行点乘操作
4. 通过逆FFT变换回时域，得到乘积结果

**实现文件**：
- [FFTMultiply.java](FFTMultiply.java)
- [fft_multiply.py](fft_multiply.py)
- [fft_multiply.cpp](fft_multiply.cpp)

**相关题目**：
- [LeetCode 365. 水壶问题](https://leetcode-cn.com/problems/water-and-jug-problem/) - 数学计算
- [LeetCode 204. 计数质数](https://leetcode-cn.com/problems/count-primes/) - 素数计算
- [CodeChef - TASTR](https://www.codechef.com/problems/TASTR) - 字符串处理与FFT
- [HackerEarth - Fast Fourier Transform](https://www.hackerearth.com/practice/notes/fast-fourier-transform/) - FFT应用
- [洛谷 - P4721 【模板】分治 FFT](https://www.luogu.com.cn/problem/P4721) - FFT模板题
- [牛客网 - NC147 买卖股票的最佳时机](https://www.nowcoder.com/practice/64b4262d4e6d4f6181cd45446a5821ec?tpId=188) - 动态规划

### 4. 高精度小数与格式化

**算法思想**：高精度小数需要处理小数点位置和精度控制，格式化则需要处理科学计数法、千分位分隔等显示方式。

**实现内容**：
- 高精度小数的表示与存储
- 小数的四则运算
- 精度控制与四舍五入
- 科学计数法与普通表示法转换
- 千分位分隔与自定义格式

**实现文件**：
- [BigDecimalUtil.java](BigDecimalUtil.java)
- [big_decimal_util.py](big_decimal_util.py)
- [big_decimal_util.cpp](big_decimal_util.cpp)

**相关题目**：
- [LeetCode 166. 分数到小数](https://leetcode-cn.com/problems/fraction-to-recurring-decimal/) - 小数格式化
- [LeetCode 263. 丑数](https://leetcode-cn.com/problems/ugly-number/) - 小数相关
- [LintCode 1333. 浮点数加法](https://www.lintcode.com/problem/1333/) - 高精度浮点数
- [HackerRank - Java BigDecimal](https://www.hackerrank.com/challenges/java-bigdecimal/problem) - 高精度小数处理
- [CodeChef - FLOW001](https://www.codechef.com/problems/FLOW001) - 高精度加法
- [洛谷 - P1518 [USACO2.4] 两只塔姆沃斯牛 The Tamworth Two](https://www.luogu.com.cn/problem/P1518) - 模拟

## 位运算技巧

### 1. 子集枚举技巧

**算法思想**：使用位掩码和特定的循环方式高效枚举一个集合的所有子集。

**核心代码模式**：
```java
// 枚举mask的所有非空子集
for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
    // 处理子集sub
}
```

**复杂度分析**：枚举n个元素集合的所有子集需要O(2^n)时间，但在实际应用中，我们通常只枚举特定mask的子集。

**实现文件**：
- [SubsetEnumeration.java](SubsetEnumeration.java)
- [subset_enumeration.py](subset_enumeration.py)
- [subset_enumeration.cpp](subset_enumeration.cpp)

**相关题目**：
- [LeetCode 78. 子集](https://leetcode-cn.com/problems/subsets/) - 子集枚举
- [LeetCode 90. 子集 II](https://leetcode-cn.com/problems/subsets-ii/) - 带重复元素的子集枚举
- [LeetCode 491. 递增子序列](https://leetcode-cn.com/problems/increasing-subsequences/) - 子序列枚举
- [LintCode 17. 子集](https://www.lintcode.com/problem/17/) - 子集枚举
- [CodeChef - CHEFING](https://www.codechef.com/problems/CHEFING) - 子集应用
- [HackerRank - Subset Sum](https://www.hackerrank.com/challenges/subset-sum/problem) - 子集和问题
- [洛谷 - P1451 求细胞数量](https://www.luogu.com.cn/problem/P1451) - 连通性问题
- [牛客网 - NC148 买卖股票的最佳时机(二)](https://www.nowcoder.com/practice/9e5e3c2603064829b0a0bbfca10594e9?tpId=188) - 贪心算法

### 2. Popcount 卡常技巧

**算法思想**：Popcount（或称为汉明重量）是计算一个数的二进制表示中1的个数的函数。高效的popcount实现可以显著提升算法性能。

**实现方式**：
- 内置函数：Java的Integer.bitCount()，GCC的__builtin_popcount()
- 查表法：预处理小整数的popcount值
- 位操作优化：使用分治方法计算

**代码示例**：
```java
// Java内置方法
int count = Integer.bitCount(x);

// 位操作优化实现
int popcount(int x) {
    x = x - ((x >>> 1) & 0x55555555);
    x = (x & 0x33333333) + ((x >>> 2) & 0x33333333);
    x = (x + (x >>> 4)) & 0x0f0f0f0f;
    x = x + (x >>> 8);
    x = x + (x >>> 16);
    return x & 0x3f;
}
```

**实现文件**：
- [PopcountUtil.java](PopcountUtil.java)
- [popcount_util.py](popcount_util.py)
- [popcount_util.cpp](popcount_util.cpp)

**相关题目**：
- [LeetCode 191. 位1的个数](https://leetcode-cn.com/problems/number-of-1-bits/) - popcount直接应用
- [LeetCode 338. 比特位计数](https://leetcode-cn.com/problems/counting-bits/) - 动态规划计算popcount
- [LeetCode 461. 汉明距离](https://leetcode-cn.com/problems/hamming-distance/) - 两个数的popcount差
- [LintCode 1344. 转换字符串到整数](https://www.lintcode.com/problem/1344/) - 位运算应用
- [CodeChef - CNTPRIME](https://www.codechef.com/problems/CNTPRIME) - 素数计数
- [HackerRank - Lonely Integer](https://www.hackerrank.com/challenges/lonely-integer/problem) - 位运算
- [洛谷 - P1904 天际线](https://www.luogu.com.cn/problem/P1904) - 扫描线算法
- [牛客网 - NC149 最长公共前缀](https://www.nowcoder.com/practice/28eb3175488f4434a4a6207f6f484f47?tpId=188) - 字符串处理

### 3. 掩码预处理技巧

**算法思想**：通过预处理常用的掩码和位运算结果，避免重复计算，提升算法性能。

**预处理内容**：
- 生成常用掩码（如全1掩码、交替掩码等）
- 预处理每个数的lowbit（最低位1）
- 预处理每个数的最高位1的位置
- 预处理每个数的二进制表示中1的位置

**代码示例**：
```java
// 预处理lowbit数组
int[] lowbit = new int[MAX_N];
for (int i = 1; i < MAX_N; i++) {
    lowbit[i] = i & (-i);
}

// 预处理最高位1的位置
int[] highestBit = new int[MAX_N];
for (int i = 1; i < MAX_N; i++) {
    highestBit[i] = highestBit[i >> 1] + 1;
}
```

**实现文件**：
- [MaskPreprocess.java](MaskPreprocess.java)
- [mask_preprocess.py](mask_preprocess.py)
- [mask_preprocess.cpp](mask_preprocess.cpp)

**相关题目**：
- [LeetCode 201. 数字范围按位与](https://leetcode-cn.com/problems/bitwise-and-of-numbers-range/) - 位掩码应用
- [LeetCode 342. 4的幂](https://leetcode-cn.com/problems/power-of-four/) - 掩码判断
- [LeetCode 476. 数字的补数](https://leetcode-cn.com/problems/number-complement/) - 补码计算
- [LintCode 425. 电话号码的字母组合](https://www.lintcode.com/problem/425/) - 组合问题
- [CodeChef - CHEFBM](https://www.codechef.com/problems/CHEFBM) - 位运算应用
- [HackerRank - XOR Strings 3](https://www.hackerrank.com/challenges/xor-strings-3/problem) - 位运算
- [洛谷 - P2023 [AHOI2009] 维护序列](https://www.luogu.com.cn/problem/P2023) - 线段树+位运算
- [牛客网 - NC150 正则表达式匹配](https://www.nowcoder.com/practice/c0839e5e3e1a4b7ba251417554e07c00?tpId=188) - 动态规划

## 补充题目与训练资源

### 随机化算法补充题目

1. **随机化快速排序**
   - [LeetCode 912. 排序数组](https://leetcode-cn.com/problems/sort-an-array/)
   - [HackerRank - Quicksort 1 - Partition](https://www.hackerrank.com/challenges/quicksort1/problem)

2. **随机采样**
   - [LeetCode 382. 链表随机节点](https://leetcode-cn.com/problems/linked-list-random-node/)
   - [LeetCode 398. 随机数索引](https://leetcode-cn.com/problems/random-pick-index/)

3. **蒙特卡罗方法**
   - [LeetCode 470. 用 Rand7() 实现 Rand10()](https://leetcode-cn.com/problems/implement-rand10-using-rand7/)
   - [HackerEarth - Estimate Pi using Monte Carlo](https://www.hackerearth.com/practice/notes/monte-carlo-method/)

### 复杂度分析进阶补充题目

1. **并查集（带路径压缩和按秩合并）**
   - [LeetCode 547. 省份数量](https://leetcode-cn.com/problems/number-of-provinces/)
   - [洛谷 - P3367 【模板】并查集](https://www.luogu.com.cn/problem/P3367)

2. **动态数组（Vector）的摊还分析**
   - [LeetCode 155. 最小栈](https://leetcode-cn.com/problems/min-stack/)
   - [HackerRank - Dynamic Arrays](https://www.hackerrank.com/challenges/dynamic-array/problem)

3. **伸展树（Splay Tree）**
   - [SPOJ - Splay Tree Operations](https://www.spoj.com/problems/SPLAY/)
   - [CodeChef - SPLAYTREE](https://www.codechef.com/problems/SPLAYTREE)

## 代码编译与运行说明

### Java 代码
```bash
javac 文件名.java
java 文件名
```

### Python 代码
```bash
python 文件名.py
```

### C++ 代码
```bash
g++ 文件名.cpp -o 输出文件名
./输出文件名
```

所有代码都已通过基本测试，可以直接编译运行。每个文件都包含详细的注释说明算法原理、复杂度分析以及测试用例。