# Class041 - GCD和LCM算法详解与扩展

## 📘 概述

本模块主要讲解最大公约数（GCD）和最小公倍数（LCM）的算法实现，以及它们在各类算法问题中的应用。GCD和LCM是数论中的基础概念，在算法竞赛和实际工程中都有广泛应用。

## 🎯 核心知识点

### 1. 欧几里得算法（辗转相除法）

欧几里得算法是计算两个数最大公约数的经典算法，基于以下数学原理：

```
gcd(a, b) = gcd(b, a % b)
```

**时间复杂度**: O(log(min(a,b)))  
**空间复杂度**: O(log(min(a,b))) (递归) 或 O(1) (迭代)

### 2. GCD与LCM的关系

```
gcd(a, b) * lcm(a, b) = |a * b|
```

因此，可以通过GCD计算LCM：

```
lcm(a, b) = |a * b| / gcd(a, b)
```

### 3. 扩展欧几里得算法

用于求解线性丢番图方程 ax + by = gcd(a,b) 的整数解。

## 📚 算法实现

### Java实现

```java
// 计算最大公约数（递归）
public static long gcd(long a, long b) {
    return b == 0 ? a : gcd(b, a % b);
}

// 计算最小公倍数
public static long lcm(long a, long b) {
    return a / gcd(a, b) * b;
}
```

### C++实现

```cpp
// 计算最大公约数（递归）
static long long gcd(long long a, long long b) {
    return b == 0 ? a : gcd(b, a % b);
}

// 计算最小公倍数
static long long lcm(long long a, long long b) {
    return a / gcd(a, b) * b;
}
```

### Python实现

```python
import math

# Python 3.5+ 内置函数
math.gcd(a, b)  # 计算最大公约数
math.lcm(a, b)  # 计算最小公倍数 (Python 3.9+)
```

## 🧠 经典问题与解法

### 1. 基础GCD/LCM计算

**问题**: 给定两个正整数，计算它们的最大公约数和最小公倍数  
**解法**: 直接应用欧几里得算法和公式

### 2. 第N个神奇数字 (LeetCode 878)

**问题**: 一个正整数如果能被 a 或 b 整除，那么它是神奇的。给定三个整数 n, a, b，返回第 n 个神奇的数字。  
**解法**: 二分查找 + 容斥原理  
**关键公式**: 在[1, x]范围内神奇数字的个数 = x/a + x/b - x/lcm(a,b)

### 3. 丑数III (LeetCode 1201)

**问题**: 编写一个程序，找出第n个丑数，丑数是可以被a或b或c整除的正整数。  
**解法**: 二分查找 + 容斥原理（三元组版本）  
**关键公式**: 在[1, x]范围内丑数的个数 = x/a + x/b + x/c - x/lcm(a,b) - x/lcm(a,c) - x/lcm(b,c) + x/lcm(a,b,c)

### 4. 字符串的最大公因子 (LeetCode 1071)

**问题**: 对于字符串s和t，只有在s=t+t+t+...+t时，才认为t能除尽s。给定两个字符串str1和str2，返回最长字符串x，使得x能除尽str1和str2。  
**解法**: 利用字符串连接的性质 + GCD  
**关键洞察**: 如果存在公因子字符串，则str1+str2 == str2+str1，且最大公因子字符串长度为gcd(len(str1), len(str2))

### 5. 最大公因数等于K的子数组数目 (LeetCode 2447)

**问题**: 给定一个数组和一个正整数k，返回最大公因数等于k的子数组数目。  
**解法**: 枚举所有子数组 + GCD计算 + 优化剪枝  
**优化点**: 
1. 如果当前元素不能被k整除，跳过该子数组
2. 如果当前GCD小于k，不可能再变大，提前终止

### 6. 最小公倍数为K的子数组数目 (LeetCode 2470)

**问题**: 给定一个数组和一个正整数k，返回最小公倍数等于k的子数组数目。  
**解法**: 枚举所有子数组 + LCM计算 + 优化剪枝  
**优化点**: 
1. 如果当前元素不能整除k，跳过该子数组
2. 如果当前LCM大于k，不可能再变小，提前终止

### 7. 链表中插入最大公约数 (LeetCode 2807)

**问题**: 给定一个链表，在每对相邻节点之间插入一个值为它们最大公约数的新节点。  
**解法**: 遍历链表，对每对相邻节点，计算它们的最大公约数并插入新节点。  
**关键点**: 
1. 遍历链表时注意处理指针
2. 插入新节点后要正确更新指针

### 8. 数组中最小数和最大数的最大公约数 (LeetCode 1979)

**问题**: 给定一个整数数组nums，返回数组中最小数和最大数的最大公约数。  
**解法**: 首先找到数组中的最小值和最大值，然后计算它们的最大公约数。  
**关键点**: 
1. 遍历数组找到最小值和最大值
2. 应用欧几里得算法计算GCD

### 9. GCD Extreme (SPOJ GCDEX)

**问题**: 计算 G(n) = Σ(i=1 to n) Σ(j=i+1 to n) gcd(i, j)  
**解法**: 使用欧拉函数优化计算  
**关键点**: 
1. 利用欧拉函数的性质进行优化
2. 预处理欧拉函数值

### 10. LCM Cardinality (UVa 10892)

**问题**: 给定一个正整数n，找出有多少对不同的整数对(a,b)，使得lcm(a,b) = n。  
**解法**: 枚举n的所有因子，对于每个因子d，如果gcd(d, n/d) = 1，则(d, n/d)是一对解。  
**关键点**: 
1. 找到n的所有因子
2. 检查因子对是否互质

### 11. GCD & LCM Inverse (POJ 2429)

**问题**: 给定两个正整数a和b的最大公约数和最小公倍数，反过来求这两个数，要求这两个数的和最小。  
**解法**: 设gcd为最大公约数，lcm为最小公倍数，则a*b = gcd*lcm。设a = gcd*x, b = gcd*y，则x*y = lcm/gcd，且gcd(x,y) = 1。问题转化为找到两个互质的数x和y，使得x*y = lcm/gcd，并且x+y最小。  
**关键点**: 
1. 将问题转化为寻找互质因子对
2. 枚举所有可能的因子对并找到和最小的

### 12. Enlarge GCD (Codeforces 1034A)

**问题**: 给定n个正整数，通过删除最少的数来增大这些数的最大公约数。返回需要删除的最少数字个数，如果无法增大GCD则返回-1。  
**解法**: 首先计算所有数的GCD，然后将所有数除以这个GCD，问题转化为找到一个大于1的因子，使得尽可能多的数是这个因子的倍数。枚举所有质数，统计是其倍数的数的个数，答案就是n减去最大个数。  
**关键点**: 
1. 线性筛法预处理质数
2. 统计每个质数的倍数个数

### 13. GCD Product (HackerRank)

**问题**: 给定N和M，计算∏(i=1 to N) ∏(j=1 to M) gcd(i, j) mod (10^9+7)  
**解法**: 对于每个质数p，计算它在结果中的指数。对于质数p，它在gcd(i,j)中的指数等于min(vp(i), vp(j))，其中vp(x)表示x中质因子p的指数。  
**关键点**: 
1. 质因子分解
2. 使用费马小定理进行模运算

### 14. LCM Sum (SPOJ LCMSUM)

**问题**: 给定n，计算∑(i=1 to n) lcm(i, n)  
**解法**: 利用数学公式进行优化。我们知道：∑(i=1 to n) lcm(i, n) = ∑(i=1 to n) (i * n) / gcd(i, n) = n * ∑(i=1 to n) i / gcd(i, n)。可以将这个和式按gcd值分组，并利用欧拉函数进行计算。  
**关键点**: 
1. 按gcd值分组求和
2. 利用欧拉函数的性质

## 📈 复杂度分析

| 算法 | 时间复杂度 | 空间复杂度 | 说明 |
|------|------------|------------|------|
| 欧几里得算法 | O(log(min(a,b))) | O(log(min(a,b))) | 递归实现 |
| 欧几里得算法 | O(log(min(a,b))) | O(1) | 迭代实现 |
| 二分查找第N个神奇数字 | O(log(n*min(a,b))) | O(1) |  |
| 子数组GCD计数 | O(n²*log(max)) | O(1) | 优化后实际更快 |
| 子数组LCM计数 | O(n²*log(max)) | O(1) | 优化后实际更快 |
| GCD Extreme | O(n log n) | O(n) | 使用欧拉函数优化 |
| LCM Cardinality | O(√n) | O(1) | 枚举因子 |
| GCD & LCM Inverse | O(√(lcm/gcd)) | O(1) | 枚举因子对 |
| Enlarge GCD | O(n*log(max_value) + max_value*log(log(max_value))) | O(max_value) | 线性筛法 |
| GCD Product | O(N*log(log(N)) + M*log(log(M))) | O(N + M) | 质因子分解 |
| LCM Sum | O(√n) | O(1) | 按因子分组 |

## 🔧 工程化考量

### 1. 溢出处理
- 使用long类型避免整数溢出
- 在计算LCM时先除后乘：`a / gcd(a,b) * b`

### 2. 边界情况
- 处理其中一个数为0的情况
- 处理负数输入（取绝对值）

### 3. 性能优化
- 提前终止条件判断
- 利用GCD和LCM的单调性进行剪枝
- 使用欧拉函数等数学工具优化计算

### 4. 代码质量
- 详细的注释说明题目来源、解题思路、复杂度分析
- 完整的测试用例覆盖各种情况
- 清晰的代码结构和命名规范

## 🌐 相关题目平台

### LeetCode
1. [878. 第N个神奇数字](https://leetcode.cn/problems/nth-magical-number/)
2. [1201. 丑数III](https://leetcode.cn/problems/ugly-number-iii/)
3. [1071. 字符串的最大公因子](https://leetcode.cn/problems/greatest-common-divisor-of-strings/)
4. [2447. 最大公因数等于K的子数组数目](https://leetcode.cn/problems/number-of-subarrays-with-gcd-equal-to-k/)
5. [2470. 最小公倍数为K的子数组数目](https://leetcode.cn/problems/number-of-subarrays-with-lcm-equal-to-k/)
6. [2807. 链表中插入最大公约数](https://leetcode.cn/problems/insert-greatest-common-divisors-in-linked-list/)
7. [1979. 数组中最小数和最大数的最大公约数](https://leetcode.cn/problems/find-greatest-common-divisor-of-array/)

### 其他平台
1. [POJ 2429](http://poj.org/problem?id=2429) - GCD & LCM Inverse
2. [Codeforces 1034A](https://codeforces.com/problemset/problem/1034/A) - Enlarge GCD
3. [HackerRank GCD Product](https://www.hackerrank.com/contests/hourrank-17/challenges/gcd-product) - GCD Product
4. [SPOJ GCDEX](https://www.spoj.com/problems/GCDEX/) - GCD Extreme
5. [UVa 10892](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1833) - LCM Cardinality
6. [SPOJ LCMSUM](https://www.spoj.com/problems/LCMSUM/) - LCM Sum

## 🔍 扩展题目与进阶应用

### 15. 区间GCD查询 (洛谷P1890)

**问题**: 给定一个数组，多次查询区间所有数的最大公约数  
**解法**: 使用Sparse Table预处理区间GCD  
**时间复杂度**: O(n log n) 预处理，O(1) 查询  
**空间复杂度**: O(n log n)  
**是否最优解**: 是，这是解决区间GCD查询的最优方法。

### 16. CGCDSSQ (Codeforces 475D)

**问题**: 给定一个数组，多次查询有多少个子区间满足其GCD等于给定值  
**解法**: Sparse Table预处理区间GCD + 二分查找  
**时间复杂度**: O(n log n) 预处理，O(log n) 查询  
**空间复杂度**: O(n log n)  
**是否最优解**: 是，这是解决该问题的最优方法。

### 17. Timus 1846. GCD 2010

**问题**: 动态区间GCD查询问题  
**解法**: 线段树维护区间GCD  
**时间复杂度**: O(n log n) 构建，O(log n) 查询和更新  
**空间复杂度**: O(n)  
**是否最优解**: 是，这是解决动态区间GCD查询的最优方法。

### 18. 扩展欧几里得算法相关题目

**问题类型**: 求解线性丢番图方程 ax + by = gcd(a,b) 的整数解  
**解法**: 扩展欧几里得算法  
**时间复杂度**: O(log(min(a,b)))  
**空间复杂度**: O(log(min(a,b)))  
**是否最优解**: 是，这是解决该问题的最优方法。

### 19. 裴蜀定理应用题目

**问题类型**: 判断线性方程是否有整数解  
**解法**: 根据裴蜀定理，方程 ax + by = m 有整数解当且仅当 gcd(a,b) | m  
**时间复杂度**: O(log(min(a,b)))  
**空间复杂度**: O(1)  
**是否最优解**: 是，这是解决该问题的最优方法。

### 20. 同余关系与GCD

**问题类型**: 判断两个数是否模某个数同余  
**解法**: 利用GCD的性质判断同余关系  
**时间复杂度**: O(log(min(a,b)))  
**空间复杂度**: O(1)  
**是否最优解**: 是，这是解决该问题的最优方法。

### 21. 质因数分解与GCD/LCM

**问题类型**: 涉及质因数分解的GCD/LCM问题  
**解法**: 对每个质因子分别处理，取最小指数(GCD)或最大指数(LCM)  
**时间复杂度**: O(√n) 用于质因数分解  
**空间复杂度**: O(log n) 用于存储质因子  
**是否最优解**: 是，这是解决该问题的最优方法。

### 22. 多组数的GCD/LCM

**问题类型**: 计算多个数的GCD或LCM  
**解法**: 依次计算，利用结合律 gcd(a,b,c) = gcd(gcd(a,b),c)  
**时间复杂度**: O(n log(max_value))  
**空间复杂度**: O(1)  
**是否最优解**: 是，这是解决该问题的最优方法。

### 23. GCD/LCM在数论中的应用

**问题类型**: 涉及整除性、同余、模运算的问题  
**解法**: 利用GCD/LCM的性质进行数学推导  
**时间复杂度**: 取决于具体问题  
**空间复杂度**: 取决于具体问题  
**是否最优解**: 通常是最优解

### 24. GCD/LCM在字符串处理中的应用

**问题类型**: 字符串周期、循环节等问题  
**解法**: 利用GCD计算最小周期长度  
**时间复杂度**: O(n)  
**空间复杂度**: O(1)  
**是否最优解**: 是，这是解决该问题的最优方法。

### 25. GCD/LCM在图形学中的应用

**问题类型**: 像素对齐、网格划分等问题  
**解法**: 利用GCD/LCM计算最小公倍数网格  
**时间复杂度**: O(log(min(a,b)))  
**空间复杂度**: O(1)  
**是否最优解**: 是，这是解决该问题的最优方法。

## 🚀 算法优化技巧

### 1. 时间复杂度优化

- **预处理技术**: 对于多次查询的问题，使用Sparse Table、线段树等数据结构预处理
- **数学优化**: 利用数论公式和性质减少计算量
- **剪枝策略**: 在枚举过程中根据GCD/LCM的性质提前终止

### 2. 空间复杂度优化

- **原地计算**: 尽可能使用原地算法，减少额外空间
- **流式处理**: 对于大数据量，使用流式处理避免存储全部数据
- **压缩存储**: 对于稀疏数据，使用压缩存储技术

### 3. 边界情况处理

- **零值处理**: 注意处理输入为零的情况
- **溢出处理**: 注意整数溢出问题，特别是乘法运算
- **极端输入**: 处理极端大小的输入数据

## 💡 工程化考量

### 1. 异常处理

- **输入验证**: 验证输入数据的合法性
- **边界检查**: 检查数组索引、除数等边界条件
- **错误处理**: 提供清晰的错误信息和处理机制

### 2. 性能优化

- **缓存友好**: 优化数据访问模式，提高缓存命中率
- **并行计算**: 对于可并行的问题，使用多线程加速
- **算法选择**: 根据数据规模选择最合适的算法

### 3. 代码质量

- **模块化设计**: 将功能分解为独立的模块
- **测试覆盖**: 编写全面的单元测试
- **文档完善**: 提供清晰的代码注释和文档

## 📚 扩展学习资源

### 1. 在线评测平台

- **LeetCode**: https://leetcode.com/tag/gcd/
- **Codeforces**: https://codeforces.com/problemset?tags=number+theory
- **SPOJ**: https://www.spoj.com/problems/tags/gcd
- **AtCoder**: https://atcoder.jp/contests/tags/gcd
- **洛谷**: https://www.luogu.com.cn/problem/list?keyword=gcd
- **HDU**: http://acm.hdu.edu.cn/search.php?field=problem&key=gcd
- **POJ**: http://poj.org/searchproblem?field=title&key=gcd

### 2. 参考书籍

- 《算法导论》 - 数论基础章节
- 《具体数学》 - 数论和组合数学
- 《挑战程序设计竞赛》 - 数论算法章节
- 《计算机程序设计艺术》 - 数论相关章节

### 3. 学术论文

- 欧几里得算法及其扩展的相关研究
- 快速GCD算法研究
- GCD/LCM在密码学中的应用

## 🎯 面试与笔试技巧

### 1. 笔试核心技巧

- **模板准备**: 提前准备GCD/LCM的基础模板
- **边界处理**: 注意处理特殊输入情况
- **性能优化**: 掌握常见的优化技巧

### 2. 面试深度表达

- **数学原理**: 能够清晰解释GCD/LCM的数学原理
- **算法选择**: 能够说明为什么选择特定算法
- **复杂度分析**: 能够准确分析时间和空间复杂度

### 3. 调试技巧

- **打印调试**: 使用System.out.println打印关键变量
- **断言验证**: 使用断言验证中间结果
- **小例子测试**: 使用小规模输入验证算法正确性

## 📝 总结

GCD和LCM是数论中的基础工具，掌握欧几里得算法及其扩展形式对于解决相关问题至关重要。通过本模块的学习，你应该能够：

1. **熟练实现算法**: 掌握欧几里得算法、扩展欧几里得算法及其变种
2. **理解数学原理**: 深入理解GCD和LCM的数学性质和应用场景
3. **解决复杂问题**: 能够解决各类涉及GCD/LCM的算法问题
4. **进行算法优化**: 掌握时间复杂度、空间复杂度的优化技巧
5. **工程化实现**: 具备编写高质量、可维护代码的能力

通过大量练习这些经典问题，可以加深对GCD/LCM算法的理解和应用能力，为算法竞赛和实际工程应用打下坚实基础。

## 🔗 相关文件

本目录包含以下实现文件：
- `Code01_GcdAndLcm.java` - 基础GCD/LCM计算
- `Code02_NthMagicalNumber.java` - 第N个神奇数字
- `Code03_SameMod.java` - 同余关系判断
- `ExtendedGcdLcmProblems.java` - 扩展GCD/LCM问题集合（Java版本）
- `ExtendedGcdLcmProblems.cpp` - 扩展GCD/LCM问题集合（C++版本）
- `ExtendedGcdLcmProblems.py` - 扩展GCD/LCM问题集合（Python版本）

每个文件都包含详细的注释说明、复杂度分析和测试用例，确保代码的正确性和可读性。