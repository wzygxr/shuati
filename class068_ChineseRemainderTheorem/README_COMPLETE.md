# 中国剩余定理（CRT）与扩展中国剩余定理（EXCRT）

## 算法概述

**中国剩余定理（Chinese Remainder Theorem, CRT）** 是数论中的一个基本定理，用于求解模数两两互质的一元线性同余方程组。

**扩展中国剩余定理（Extended Chinese Remainder Theorem, EXCRT）** 是中国剩余定理的扩展，用于求解模数不一定两两互质的一元线性同余方程组。

## 核心原理

### 中国剩余定理（CRT）

对于同余方程组：
```
x ≡ a₁ (mod m₁)
x ≡ a₂ (mod m₂)
...
x ≡ aₖ (mod mₖ)
```

其中，m₁, m₂, ..., mₖ 两两互质，方程组在模 M = m₁ × m₂ × ... × mₖ 意义下有唯一解：

x ≡ Σ(aᵢ × Mᵢ × Mᵢ⁻¹) (mod M)

其中，Mᵢ = M / mᵢ，Mᵢ⁻¹ 是 Mᵢ 在模 mᵢ 意义下的逆元。

### 扩展中国剩余定理（EXCRT）

当模数不一定两两互质时，EXCRT 通过逐步合并方程的方式求解：

1. 假设前 k-1 个方程的解为 x，模数为 M
2. 通解为 x + t × M（t 为整数）
3. 合并第 k 个方程：x + t × M ≡ aₖ (mod mₖ)
4. 转化为：t × M ≡ (aₖ - x) (mod mₖ)
5. 使用扩展欧几里得算法求解 t
6. 更新解和模数

## 详细题目列表与解析

### CRT 相关题目

1. **洛谷 P1495【模板】中国剩余定理（CRT）/ 曹冲养猪**
   - 链接：https://www.luogu.com.cn/problem/P1495
   - 题目大意：求解同余方程组 x ≡ ai (mod mi)，其中mi两两互质
   - 解题思路：标准的中国剩余定理模板题，直接应用CRT公式求解

2. **51Nod 1079 中国剩余定理**
   - 链接：https://www.51nod.com/Challenge/Problem.html#!#problemId=1079
   - 题目大意：给定一些质数p和对应余数m，求满足所有条件的最小正整数K
   - 解题思路：题目保证所有模数都是质数，所以两两互质，直接应用CRT

3. **POJ 1006 Biorhythms**
   - 链接：http://poj.org/problem?id=1006
   - 题目大意：人的体力、情感和智力周期分别为23天、28天和33天，已知某一天三个指标的数值，求下一次三个指标同时达到峰值的天数
   - 解题思路：三个生理周期分别为23、28、33天，它们两两互质，可以直接应用中国剩余定理

4. **UVA 756 Biorhythms**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=697
   - 题目大意：与POJ 1006相同

5. **CodeChef - CHEFADV**
   - 链接：https://www.codechef.com/problems/CHEFADV
   - 题目大意：判断是否能在棋盘上移动，涉及同余条件

6. **CodeChef - COPRIME3**
   - 链接：https://www.codechef.com/problems/COPRIME3
   - 相关思想：使用同余关系解决数论问题

7. **牛客网 - NC15857 同余方程**
   - 链接：https://ac.nowcoder.com/acm/problem/15857
   - 题目大意：求解同余方程组，模数两两互质

8. **LeetCode 1189 "气球的最大数量"**
   - 链接：https://leetcode-cn.com/problems/maximum-number-of-balloons/
   - 相关思想：虽然不是直接应用CRT，但涉及到周期性和计数问题，可作为辅助训练题

9. **剑指Offer 44. 数字序列中某一位的数字**
   - 链接：https://leetcode-cn.com/problems/shu-zi-xu-lie-zhong-mou-yi-wei-de-shu-zi-lcof/
   - 相关思想：涉及数字的周期性分布，可作为辅助训练题

### EXCRT 相关题目

1. **洛谷 P4777【模板】扩展中国剩余定理（EXCRT）**
   - 链接：https://www.luogu.com.cn/problem/P4777
   - 题目大意：求解同余方程组 x ≡ ri (mod mi)，其中mi不一定两两互质
   - 解题思路：标准的扩展中国剩余定理模板题，通过合并方程求解

2. **POJ 2891 Strange Way to Express Integers**
   - 链接：http://poj.org/problem?id=2891
   - 题目大意：给定n个形如 x ≡ ri (mod mi) 的同余方程，求最小非负整数解，mi不一定两两互质
   - 解题思路：与洛谷P4777相同，是EXCRT的标准应用

3. **NOI 2018 屠龙勇士**
   - 链接：https://www.luogu.com.cn/problem/P4774
   - 题目大意：游戏中需要击败n条龙，每条龙有血量hp[i]和恢复能力recovery[i]，勇士有m把剑，每把剑有攻击力，求最少攻击次数
   - 解题思路：将问题转化为线性同余方程组，然后用EXCRT求解

4. **Codeforces 707D Two chandeliers**
   - 链接：https://codeforces.com/contest/1483/problem/D
   - 题目大意：有两个循环亮灯的序列，每天亮一种颜色的灯，老板会在两个灯颜色相同时生气，求第k次生气在第几天
   - 解题思路：枚举颜色相同的配对，转化为同余方程组求解

5. **UVa 11754 Code Feat**
   - 链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2854
   - 题目大意：给定C个条件，每个条件形如N除以X的余数在集合Y中，求前S个满足条件的数
   - 解题思路：枚举所有可能的余数组合，对每个组合使用EXCRT求解

6. **HDU 3579 Hello Kiki**
   - 链接：https://acm.hdu.edu.cn/showproblem.php?pid=3579
   - 题目大意：求解同余方程组，模数不一定互质

7. **AtCoder Beginner Contest 186 F. Rook on Grid**
   - 链接：https://atcoder.jp/contests/abc186/tasks/abc186_f
   - 解题思路：可使用EXCRT解决的周期性问题

8. **SPOJ - MOD**
   - 链接：https://www.spoj.com/problems/MOD/
   - 题目大意：求解同余方程组，模数不一定互质

9. **牛客网 - NC15293 同余方程**
   - 链接：https://ac.nowcoder.com/acm/problem/15293
   - 题目大意：求解同余方程组，模数不一定互质

10. **SPOJ - CHIAVI**
    - 链接：https://www.spoj.com/problems/CHIAVI/
    - 题目大意：密码学相关问题，涉及同余方程求解

11. **Codeforces 546C Soldier and Cards**
    - 链接：https://codeforces.com/contest/546/problem/C
    - 相关思想：虽然不是直接应用EXCRT，但涉及到周期性问题，可作为辅助训练题

## 算法实现与代码分析

### CRT 实现（Java）

```java
public class CRT {
    // 扩展欧几里得算法
    private static long exgcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        long gcd = exgcd(b, a % b, y, x);
        y[0] -= a / b * x[0];
        return gcd;
    }

    // 中国剩余定理求解函数
    // r[]表示余数，m[]表示模数，且模数两两互质
    public static long crt(long[] r, long[] m) {
        int n = r.length;
        long M = 1; // 所有模数的乘积
        long result = 0; // 最终解

        // 计算所有模数的乘积
        for (int i = 0; i < n; i++) {
            M *= m[i];
        }

        // 计算最终解
        for (int i = 0; i < n; i++) {
            long Mi = M / m[i]; // Mi = M / mi
            long[] x = new long[1];
            long[] y = new long[1];
            long gcd = exgcd(Mi, m[i], x, y); // 计算Mi的逆元

            // 因为模数两两互质，所以gcd一定是1，所以逆元一定存在
            result = (result + r[i] * Mi % M * x[0] % M) % M;
        }

        // 确保结果是正数
        return (result % M + M) % M;
    }
}
```

### EXCRT 实现（Java）

```java
public class EXCRT {
    // 龟速乘法，防止大数溢出
    private static long mul(long a, long b, long mod) {
        long res = 0;
        a %= mod;
        while (b > 0) {
            if ((b & 1) == 1) res = (res + a) % mod;
            a = (a << 1) % mod;
            b >>= 1;
        }
        return res;
    }

    // 扩展欧几里得算法
    private static long exgcd(long a, long b, long[] x, long[] y) {
        if (b == 0) {
            x[0] = 1;
            y[0] = 0;
            return a;
        }
        long gcd = exgcd(b, a % b, y, x);
        y[0] -= a / b * x[0];
        return gcd;
    }

    // 扩展中国剩余定理求解函数
    // r[]表示余数，m[]表示模数，模数不一定两两互质
    public static long excrt(long[] r, long[] m) {
        int n = r.length;
        long x = 0; // 当前解
        long M = 1; // 当前模数的最小公倍数

        for (int i = 0; i < n; i++) {
            // 合并第i+1个方程：x ≡ r[i] (mod m[i])
            // 新的方程：x + t*M ≡ r[i] (mod m[i])
            // 转化为：t*M ≡ (r[i] - x) (mod m[i])
            long a = M;
            long b = m[i];
            long c = ((r[i] - x) % b + b) % b; // 确保c为正数

            long[] t = new long[1];
            long[] s = new long[1];
            long gcd = exgcd(a, b, t, s); // 求解线性同余方程

            if (c % gcd != 0) {
                return -1; // 无解
            }

            // 调整t的值
            long k = (c / gcd) * t[0];
            long b_div_gcd = b / gcd;
            if (k < 0) {
                k += Math.abs(b_div_gcd / gcd);
            }
            k %= b_div_gcd;

            // 更新解和模数
            x += k * M;
            M *= b_div_gcd;
            x = (x % M + M) % M; // 确保x为正数
        }

        return x;
    }
}
```

### CRT 实现（C++）

```cpp
#include <iostream>
#include <vector>
using namespace std;

// 扩展欧几里得算法
long long exgcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long gcd = exgcd(b, a % b, y, x);
    y -= a / b * x;
    return gcd;
}

// 中国剩余定理求解函数
// r[]表示余数，m[]表示模数，且模数两两互质
long long crt(vector<long long> r, vector<long long> m) {
    int n = r.size();
    long long M = 1; // 所有模数的乘积
    long long result = 0; // 最终解

    // 计算所有模数的乘积
    for (int i = 0; i < n; i++) {
        M *= m[i];
    }

    // 计算最终解
    for (int i = 0; i < n; i++) {
        long long Mi = M / m[i]; // Mi = M / mi
        long long x, y;
        long long gcd = exgcd(Mi, m[i], x, y); // 计算Mi的逆元

        // 因为模数两两互质，所以gcd一定是1，所以逆元一定存在
        result = (result + r[i] * Mi % M * x % M) % M;
    }

    // 确保结果是正数
    return (result % M + M) % M;
}
```

### EXCRT 实现（C++）

```cpp
#include <iostream>
#include <vector>
using namespace std;

// 龟速乘法，防止大数溢出
long long mul(long long a, long long b, long long mod) {
    long long res = 0;
    a %= mod;
    while (b > 0) {
        if ((b & 1) == 1) res = (res + a) % mod;
        a = (a << 1) % mod;
        b >>= 1;
    }
    return res;
}

// 扩展欧几里得算法
long long exgcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long gcd = exgcd(b, a % b, y, x);
    y -= a / b * x;
    return gcd;
}

// 扩展中国剩余定理求解函数
// r[]表示余数，m[]表示模数，模数不一定两两互质
long long excrt(vector<long long> r, vector<long long> m) {
    int n = r.size();
    long long x = 0; // 当前解
    long long M = 1; // 当前模数的最小公倍数

    for (int i = 0; i < n; i++) {
        // 合并第i+1个方程：x ≡ r[i] (mod m[i])
        // 新的方程：x + t*M ≡ r[i] (mod m[i])
        // 转化为：t*M ≡ (r[i] - x) (mod m[i])
        long long a = M;
        long long b = m[i];
        long long c = ((r[i] - x) % b + b) % b; // 确保c为正数

        long long t, s;
        long long gcd = exgcd(a, b, t, s); // 求解线性同余方程

        if (c % gcd != 0) {
            return -1; // 无解
        }

        // 调整t的值
        long long k = (c / gcd) * t;
        long long b_div_gcd = b / gcd;
        if (k < 0) {
            k += abs(b_div_gcd / gcd);
        }
        k %= b_div_gcd;

        // 更新解和模数
        x += k * M;
        M *= b_div_gcd;
        x = (x % M + M) % M; // 确保x为正数
    }

    return x;
}
```

### CRT 实现（Python）

```python
def exgcd(a, b):
    """
    扩展欧几里得算法
    返回 (gcd, x, y) 其中 ax + by = gcd
    """
    if b == 0:
        return a, 1, 0
    else:
        gcd, y, x = exgcd(b, a % b)
        y -= (a // b) * x
        return gcd, x, y

def crt(r, m):
    """
    中国剩余定理求解函数
    r: 余数列表
    m: 模数列表（模数两两互质）
    返回：最小正整数解
    """
    n = len(r)
    M = 1  # 所有模数的乘积
    result = 0  # 最终解

    # 计算所有模数的乘积
    for i in range(n):
        M *= m[i]

    # 计算最终解
    for i in range(n):
        Mi = M // m[i]  # Mi = M / mi
        gcd, x, y = exgcd(Mi, m[i])  # 计算Mi的逆元

        # 因为模数两两互质，所以gcd一定是1，所以逆元一定存在
        result = (result + r[i] * Mi % M * x % M) % M

    # 确保结果是正数
    return (result % M + M) % M
```

### EXCRT 实现（Python）

```python
def exgcd(a, b):
    """
    扩展欧几里得算法
    返回 (gcd, x, y) 其中 ax + by = gcd
    """
    if b == 0:
        return a, 1, 0
    else:
        gcd, y, x = exgcd(b, a % b)
        y -= (a // b) * x
        return gcd, x, y

def mul(a, b, mod):
    """
    龟速乘法，防止大数溢出
    """
    res = 0
    a %= mod
    while b > 0:
        if (b & 1) == 1:
            res = (res + a) % mod
        a = (a << 1) % mod
        b >>= 1
    return res

def excrt(r, m):
    """
    扩展中国剩余定理求解函数
    r: 余数列表
    m: 模数列表（模数不一定两两互质）
    返回：最小正整数解，无解返回-1
    """
    n = len(r)
    x = 0  # 当前解
    M = 1  # 当前模数的最小公倍数

    for i in range(n):
        # 合并第i+1个方程：x ≡ r[i] (mod m[i])
        # 新的方程：x + t*M ≡ r[i] (mod m[i])
        # 转化为：t*M ≡ (r[i] - x) (mod m[i])
        a = M
        b = m[i]
        c = ((r[i] - x) % b + b) % b  # 确保c为正数

        gcd, t, s = exgcd(a, b)  # 求解线性同余方程

        if c % gcd != 0:
            return -1  # 无解

        # 调整t的值
        k = (c // gcd) * t
        b_div_gcd = b // gcd
        if k < 0:
            k += abs(b_div_gcd // gcd)
        k %= b_div_gcd

        # 更新解和模数
        x += k * M
        M *= b_div_gcd
        x = (x % M + M) % M  # 确保x为正数

    return x
```

## 时间与空间复杂度分析

### CRT 复杂度

- **时间复杂度**：O(n² log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法
- **空间复杂度**：O(n)，用于存储模数和余数

### EXCRT 复杂度

- **时间复杂度**：O(n log max(mi))，其中n为方程个数，log项来自于扩展欧几里得算法和龟速乘法
- **空间复杂度**：O(n)，用于存储模数和余数

## 算法优化与工程化考量

### 性能优化

1. **龟速乘法**：处理大数乘法，防止溢出
2. **提前判断模数互质**：对于CRT，可提前验证模数是否两两互质
3. **快速IO**：对于大数据量输入，使用快速IO提高效率
4. **内存优化**：避免不必要的数组创建和数据复制
5. **常数优化**：优化循环结构，减少不必要的计算

### 工程化考量

1. **异常处理**：
   - 处理无解的情况
   - 检查输入合法性（如模数为0）
   - 提供明确的错误信息

2. **鲁棒性**：
   - 处理负数余数
   - 确保结果为正整数
   - 处理模数为1的特殊情况

3. **可扩展性**：
   - 将算法封装为可复用的函数
   - 支持不同数据类型的输入

4. **测试用例**：
   - 边界情况测试（n=1、模数为1）
   - 大数值测试
   - 无解情况测试

5. **文档完善**：
   - 函数注释
   - 使用示例
   - 参数说明

## 与其他领域的联系

### 与密码学的联系

- **RSA加密算法**：使用中国剩余定理加速解密过程
- **公钥密码学**：处理大数运算中的模运算
- **密钥生成**：利用数论性质生成安全的密钥

### 与计算机科学的联系

- **分布式系统**：任务调度和同步问题
- **计算机图形学**：周期性模式生成
- **多线程编程**：线程同步和协调

### 与数学的联系

- **数论**：基础的数论定理和应用
- **代数**：模运算和同余关系
- **组合数学**：计数问题和组合优化

### 与人工智能的联系

- **强化学习**：处理周期性奖励信号
- **机器学习**：特征工程中的周期性特征提取
- **自然语言处理**：周期性模式识别

## 总结与技巧

1. **问题识别**：
   - 当问题涉及多个周期性条件或同余约束时，考虑使用CRT/EXCRT
   - 明确模数是否两两互质，选择合适的算法
   - 寻找问题中的隐藏周期性，特别是在游戏、调度、密码学等场景

2. **实现要点**：
   - 熟练掌握扩展欧几里得算法，这是两种算法的基础
   - 注意大数运算中的溢出问题，特别是在C++中
   - 正确处理边界情况和无解情况
   - 对于不同语言，注意类型范围和溢出处理方式的差异

3. **调试技巧**：
   - 打印中间变量的值，特别是逆元和乘积结果
   - 验证每个方程的解是否正确
   - 使用小数据集手动验证算法的正确性
   - 对于大数据，使用分步验证法定位问题

4. **工程应用技巧**：
   - 将算法模块化，便于集成到实际项目中
   - 考虑并发和线程安全问题
   - 针对具体应用场景进行性能优化
   - 编写完善的单元测试确保代码质量

通过掌握中国剩余定理和扩展中国剩余定理，我们可以解决许多涉及周期性和同余约束的问题，特别是在数论、密码学和系统设计等领域有广泛的应用。这些算法不仅具有理论价值，也有重要的工程实践意义。