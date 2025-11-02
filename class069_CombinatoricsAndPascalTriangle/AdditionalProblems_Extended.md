# 组合数学与杨辉三角扩展题目

本文件包含组合数学与杨辉三角相关的扩展题目，涵盖更复杂的应用场景和解题思路，覆盖LeetCode、Codeforces、AtCoder、洛谷、牛客网等多个平台。

## 题目列表

1. LeetCode 343 - 整数拆分
2. LeetCode 518 - 零钱兑换 II
3. Codeforces 1117D - Magic Gems
4. AtCoder ABC165D - Floor Function
5. 洛谷 P1313 - 计算系数
6. 杭电 OJ 2032 - 杨辉三角
7. SPOJ MSUBSTR - Substring
8. UVa 11300 - Spreading the Wealth
9. CodeChef INVCNT - Inversion Count
10. 计蒜客 T1565 - 组合数计算

## 题目详情

## 1. LeetCode 343. Integer Break

---

### 题目描述
给定一个正整数 n，将其拆分为至少两个正整数的和，并使这些整数的乘积最大化。返回最大乘积。

### 题目来源
- [LeetCode 343](https://leetcode.cn/problems/integer-break/)

### 示例
```
输入: n = 10
输出: 36
解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36
```

### 解题思路
- 数学分析：尽可能多的拆分成3，剩下的用2填充
- 当余数为0时，全部为3
- 当余数为1时，拆成两个2和剩下的3（因为2×2 > 3×1）
- 当余数为2时，拆成一个2和剩下的3

### 复杂度分析
- 时间复杂度：O(log n)，使用快速幂计算3的幂次
- 空间复杂度：O(1)

### Java 实现
```java
public class Solution {
    public int integerBreak(int n) {
        if (n <= 3) return n - 1;
        
        int quotient = n / 3;
        int remainder = n % 3;
        
        if (remainder == 0) {
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            return (int) Math.pow(3, quotient - 1) * 4;
        } else {
            return (int) Math.pow(3, quotient) * 2;
        }
    }
}
```

### Python 实现
```python
class Solution:
    def integerBreak(self, n):
        """
        将正整数n拆分为k个正整数的和，使乘积最大化
        
        Args:
            n: 正整数
            
        Returns:
            最大乘积
        """
        if n <= 3:
            return n - 1
        
        quotient = n // 3
        remainder = n % 3
        
        if remainder == 0:
            return 3 ** quotient
        elif remainder == 1:
            return 3 ** (quotient - 1) * 4
        else:
            return 3 ** quotient * 2
```

### C++ 实现
```cpp
#include <cmath>
using namespace std;

class Solution {
public:
    int integerBreak(int n) {
        if (n <= 3) return n - 1;
        
        int quotient = n / 3;
        int remainder = n % 3;
        
        if (remainder == 0) {
            return (int) pow(3, quotient);
        } else if (remainder == 1) {
            return (int) pow(3, quotient - 1) * 4;
        } else {
            return (int) pow(3, quotient) * 2;
        }
    }
};
```

## 数学与工程实践

组合数学是计算机科学中非常重要的基础学科，它与许多领域都有密切的联系：

1. **机器学习与深度学习**：
   - 概率模型中的贝叶斯定理应用
   - 排列组合在特征选择中的应用
   - 组合优化在神经网络结构设计中的应用

2. **自然语言处理**：
   - n-gram模型中的组合计数
   - 文本生成中的概率分布计算
   - 词向量空间中的组合问题

3. **图像处理**：
   - 图像分割中的组合优化
   - 图像特征描述子中的排列问题
   - 像素组合的排列计算

4. **工程化实现建议**：
   - **可复用组件设计**：将组合数计算封装为独立的工具类
   - **缓存机制**：对于频繁计算的组合数，可以使用缓存提高性能
   - **线程安全**：在多线程环境中确保计算的正确性
   - **单元测试**：覆盖各种边界情况和异常输入
   - **性能优化**：根据数据规模选择合适的算法实现

通过学习和实践组合数学算法，我们可以更深入地理解计算机科学中的许多核心概念，为解决复杂问题提供更有效的思路。

### 复杂度分析
- 时间复杂度：O(1)
- 空间复杂度：O(1)

---

## 2. LeetCode 518. Coin Change 2

### 题目描述
给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。请你计算并返回可以凑成总金额的硬币组合数。

### 题目来源
- [LeetCode 518](https://leetcode.cn/problems/coin-change-2/)

### 示例
```
输入：amount = 5, coins = [1, 2, 5]
输出：4
解释：有四种方式可以凑成总金额：
5=5
5=2+2+1
5=2+1+1+1
5=1+1+1+1+1
```

### 解题思路
- 完全背包问题的应用
- dp[i] 表示凑成金额i的硬币组合数
- 状态转移方程：dp[i] += dp[i - coin]，其中coin是当前考虑的硬币面额
- 注意遍历顺序：外层遍历硬币，内层遍历金额，确保每个硬币只被使用一次

### 复杂度分析
- 时间复杂度：O(amount * len(coins))
- 空间复杂度：O(amount)

### Java 实现
```java
public class Solution {
    public int change(int amount, int[] coins) {
        // dp[i] 表示凑成金额 i 的组合数
        int[] dp = new int[amount + 1];
        dp[0] = 1;  // 空组合凑成金额0
        
        // 外层遍历硬币，内层遍历金额
        // 这种顺序确保每个硬币只被使用一次，得到的是组合数而非排列数
        for (int coin : coins) {
            // 更新dp数组
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
}
```

## 3. Codeforces 1117D - Magic Gems

### 题目描述
有n个魔法宝石，每个魔法宝石可以变成m个普通宝石。同时，每k个普通宝石可以变成一个魔法宝石。计算最终可能的魔法宝石数量。

### 题目来源
- [Codeforces 1117D](https://codeforces.com/problemset/problem/1117/D)

### 解题思路
- 使用矩阵快速幂优化递推关系
- 递推公式：dp[n] = dp[n-1] + dp[n-k] * (m-1)
- 初始条件：dp[1]=1, dp[2]=1, ..., dp[k]=1, dp[k+1]=m

### 复杂度分析
- 时间复杂度：O(k³ log n)
- 空间复杂度：O(k²)

### Java 实现
```java
import java.util.Scanner;

public class Main {
    private static final int MOD = 1000000007;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        scanner.close();
        
        System.out.println(magicGems(n, m, k));
    }
    
    private static long magicGems(long n, int m, int k) {
        if (k == 1) {
            return pow(m, n, MOD);
        }
        
        long[][] trans = new long[k][k];
        // 构建转移矩阵
        trans[0][0] = 1; // dp[i] += dp[i-1]
        trans[0][k-1] = (m - 1) % MOD; // dp[i] += dp[i-k] * (m-1)
        
        for (int i = 1; i < k; i++) {
            trans[i][i-1] = 1; // 单位矩阵的一部分
        }
        
        // 初始向量 [dp[1], dp[2], ..., dp[k]]
        long[] initial = new long[k];
        for (int i = 0; i < k; i++) {
            initial[i] = 1;
        }
        
        // 矩阵快速幂
        long[][] mat = matrixPower(trans, n - 1, MOD);
        long result = 0;
        for (int i = 0; i < k; i++) {
            result = (result + initial[i] * mat[0][i]) % MOD;
        }
        
        return result;
    }
    
    private static long[][] matrixPower(long[][] a, long power, int mod) {
        int n = a.length;
        long[][] result = new long[n][n];
        // 初始化为单位矩阵
        for (int i = 0; i < n; i++) {
            result[i][i] = 1;
        }
        
        while (power > 0) {
            if (power % 2 == 1) {
                result = multiplyMatrix(result, a, mod);
            }
            a = multiplyMatrix(a, a, mod);
            power /= 2;
        }
        
        return result;
    }
    
    private static long[][] multiplyMatrix(long[][] a, long[][] b, int mod) {
        int n = a.length;
        long[][] result = new long[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int l = 0; l < n; l++) {
                    result[i][j] = (result[i][j] + a[i][l] * b[l][j]) % mod;
                }
            }
        }
        
        return result;
    }
    
    private static long pow(long base, long exponent, int mod) {
        long result = 1;
        base %= mod;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent /= 2;
        }
        return result;
    }
}
```

### C++ 实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

const int MOD = 1000000007;

vector<vector<long long>> multiplyMatrix(const vector<vector<long long>>& a, const vector<vector<long long>>& b, int mod) {
    int n = a.size();
    vector<vector<long long>> result(n, vector<long long>(n, 0));
    
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            for (int l = 0; l < n; l++) {
                result[i][j] = (result[i][j] + a[i][l] * b[l][j]) % mod;
            }
        }
    }
    
    return result;
}

vector<vector<long long>> matrixPower(vector<vector<long long>> a, long long power, int mod) {
    int n = a.size();
    vector<vector<long long>> result(n, vector<long long>(n, 0));
    // 初始化为单位矩阵
    for (int i = 0; i < n; i++) {
        result[i][i] = 1;
    }
    
    while (power > 0) {
        if (power % 2 == 1) {
            result = multiplyMatrix(result, a, mod);
        }
        a = multiplyMatrix(a, a, mod);
        power /= 2;
    }
    
    return result;
}

long long powMod(long long base, long long exponent, int mod) {
    long long result = 1;
    base %= mod;
    while (exponent > 0) {
        if (exponent % 2 == 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exponent /= 2;
    }
    return result;
}

long long magicGems(long long n, int m, int k) {
    if (k == 1) {
        return powMod(m, n, MOD);
    }
    
    vector<vector<long long>> trans(k, vector<long long>(k, 0));
    // 构建转移矩阵
    trans[0][0] = 1; // dp[i] += dp[i-1]
    trans[0][k-1] = (m - 1) % MOD; // dp[i] += dp[i-k] * (m-1)
    
    for (int i = 1; i < k; i++) {
        trans[i][i-1] = 1; // 单位矩阵的一部分
    }
    
    // 初始向量 [dp[1], dp[2], ..., dp[k]]
    vector<long long> initial(k, 1);
    
    // 矩阵快速幂
    vector<vector<long long>> mat = matrixPower(trans, n - 1, MOD);
    long long result = 0;
    for (int i = 0; i < k; i++) {
        result = (result + initial[i] * mat[0][i]) % MOD;
    }
    
    return result;
}

int main() {
    long long n;
    int m, k;
    cin >> n >> m >> k;
    cout << magicGems(n, m, k) << endl;
    return 0;
}
```

### Python 实现
```python
def multiply_matrix(a, b, mod):
    n = len(a)
    result = [[0]*n for _ in range(n)]
    
    for i in range(n):
        for j in range(n):
            for l in range(n):
                result[i][j] = (result[i][j] + a[i][l] * b[l][j]) % mod
    
    return result

def matrix_power(a, power, mod):
    n = len(a)
    # 初始化为单位矩阵
    result = [[0]*n for _ in range(n)]
    for i in range(n):
        result[i][i] = 1
    
    while power > 0:
        if power % 2 == 1:
            result = multiply_matrix(result, a, mod)
        a = multiply_matrix(a, a, mod)
        power //= 2
    
    return result

def pow_mod(base, exponent, mod):
    result = 1
    base %= mod
    while exponent > 0:
        if exponent % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exponent //= 2
    return result

def magic_gems(n, m, k):
    MOD = 10**9 + 7
    
    if k == 1:
        return pow_mod(m, n, MOD)
    
    # 构建转移矩阵
    trans = [[0]*k for _ in range(k)]
    trans[0][0] = 1  # dp[i] += dp[i-1]
    trans[0][k-1] = (m - 1) % MOD  # dp[i] += dp[i-k] * (m-1)
    
    for i in range(1, k):
        trans[i][i-1] = 1  # 单位矩阵的一部分
    
    # 初始向量 [dp[1], dp[2], ..., dp[k]]
    initial = [1] * k
    
    # 矩阵快速幂
    mat = matrix_power(trans, n - 1, MOD)
    result = 0
    for i in range(k):
        result = (result + initial[i] * mat[0][i]) % MOD
    
    return result

# 输入示例
n, m, k = map(int, input().split())
print(magic_gems(n, m, k))
```

### Python 实现
```python
class Solution:
    def change(self, amount, coins):
        """
        计算凑成总金额的硬币组合数
        
        Args:
            amount: 总金额
            coins: 硬币面额数组
            
        Returns:
            组合数
        """
        # dp[i] 表示凑成金额 i 的组合数
        dp = [0] * (amount + 1)
        dp[0] = 1  # 凑成金额0的组合数为1（不选任何硬币）
        
        # 遍历每种硬币
        for coin in coins:
            # 更新dp数组
            for i in range(coin, amount + 1):
                dp[i] += dp[i - coin]
        
        return dp[amount]
```

### C++ 实现
```cpp
#include <vector>
using namespace std;

class Solution {
public:
    int change(int amount, vector<int>& coins) {
        // dp[i] 表示凑成金额 i 的组合数
        vector<int> dp(amount + 1, 0);
        dp[0] = 1;  // 凑成金额0的组合数为1（不选任何硬币）
        
        // 遍历每种硬币
        for (int coin : coins) {
            // 更新dp数组
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
};
```

### 复杂度分析
- 时间复杂度：O(amount × coins.length)
- 空间复杂度：O(amount)

---

## LeetCode 629. K Inverse Pairs Array

### 题目描述
给出两个整数 n 和 k，找出所有包含从 1 到 n 的数字，且恰好拥有 k 个逆序对的不同的数组的个数。

### 示例
```
输入: n = 3, k = 1
输出: 2
解释: 数组 [1,3,2] 和 [2,1,3] 都有 1 个逆序对。
```

### 解题思路
使用动态规划，dp[i][j] 表示使用数字 1 到 i 构成的数组中恰好有 j 个逆序对的数组个数。

### Java 实现
```java
public class Solution {
    public int kInversePairs(int n, int k) {
        int MOD = 1000000007;
        // dp[i][j] 表示使用1到i的数字构成的数组中恰好有j个逆序对的数组个数
        int[][] dp = new int[n+1][k+1];
        
        // 初始化：使用1个数字构成的数组有0个逆序对
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1;
        }
        
        // 动态规划填表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                // 状态转移方程
                dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % MOD;
                if (j >= i) {
                    dp[i][j] = (dp[i][j] - dp[i-1][j-i] + MOD) % MOD;
                }
            }
        }
        
        return dp[n][k];
    }
}
```

### Python 实现
```python
class Solution:
    def kInversePairs(self, n, k):
        """
        计算恰好拥有k个逆序对的不同数组个数
        
        Args:
            n: 数字范围1到n
            k: 逆序对数量
            
        Returns:
            数组个数
        """
        MOD = 1000000007
        # dp[i][j] 表示使用1到i的数字构成的数组中恰好有j个逆序对的数组个数
        dp = [[0] * (k+1) for _ in range(n+1)]
        
        # 初始化：使用1个数字构成的数组有0个逆序对
        for i in range(1, n+1):
            dp[i][0] = 1
        
        # 动态规划填表
        for i in range(1, n+1):
            for j in range(1, k+1):
                # 状态转移方程
                dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % MOD
                if j >= i:
                    dp[i][j] = (dp[i][j] - dp[i-1][j-i] + MOD) % MOD
        
        return dp[n][k]
```

### C++ 实现
```cpp
#include <vector>
using namespace std;

class Solution {
public:
    int kInversePairs(int n, int k) {
        const int MOD = 1000000007;
        // dp[i][j] 表示使用1到i的数字构成的数组中恰好有j个逆序对的数组个数
        vector<vector<int>> dp(n+1, vector<int>(k+1, 0));
        
        // 初始化：使用1个数字构成的数组有0个逆序对
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1;
        }
        
        // 动态规划填表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                // 状态转移方程
                dp[i][j] = (dp[i][j-1] + dp[i-1][j]) % MOD;
                if (j >= i) {
                    dp[i][j] = (dp[i][j] - dp[i-1][j-i] + MOD) % MOD;
                }
            }
        }
        
        return dp[n][k];
    }
};
```

### 复杂度分析
- 时间复杂度：O(n × k)
- 空间复杂度：O(n × k)

---

## LeetCode 96. Unique Binary Search Trees

### 题目描述
给定一个整数 n，求恰由 n 个节点组成且节点值从 1 到 n 互不相同的二叉搜索树有多少种？

### 示例
```
输入：n = 3
输出：5
```

### 解题思路
使用卡塔兰数（Catalan Number）公式或动态规划解决。

### Java 实现
```java
public class Solution {
    public int numTrees(int n) {
        // dp[i] 表示由i个不同节点组成的二叉搜索树的种数
        int[] dp = new int[n+1];
        dp[0] = dp[1] = 1;
        
        // 动态规划计算
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                // 以j为根节点，左子树有j-1个节点，右子树有i-j个节点
                dp[i] += dp[j-1] * dp[i-j];
            }
        }
        
        return dp[n];
    }
}
```

### Python 实现
```python
class Solution:
    def numTrees(self, n):
        """
        计算由n个不同节点组成的二叉搜索树的种数
        
        Args:
            n: 节点数
            
        Returns:
            二叉搜索树的种数
        """
        # dp[i] 表示由i个不同节点组成的二叉搜索树的种数
        dp = [0] * (n+1)
        dp[0] = dp[1] = 1
        
        # 动态规划计算
        for i in range(2, n+1):
            for j in range(1, i+1):
                # 以j为根节点，左子树有j-1个节点，右子树有i-j个节点
                dp[i] += dp[j-1] * dp[i-j]
        
        return dp[n]
```

### C++ 实现
```cpp
#include <vector>
using namespace std;

class Solution {
public:
    int numTrees(int n) {
        // dp[i] 表示由i个不同节点组成的二叉搜索树的种数
        vector<int> dp(n+1, 0);
        dp[0] = dp[1] = 1;
        
        // 动态规划计算
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                // 以j为根节点，左子树有j-1个节点，右子树有i-j个节点
                dp[i] += dp[j-1] * dp[i-j];
            }
        }
        
        return dp[n];
    }
};
```

### 复杂度分析
- 时间复杂度：O(n²)
- 空间复杂度：O(n)

---

## Codeforces 1359E - 组合数学问题

### 题目描述
给定 n 和 k，计算从 1 到 n 中选择 k 个不同的数字的方案数，使得这 k 个数字的乘积能被某个给定的数整除。

### 解题思路
这是一个组合数学问题，需要计算满足特定条件的组合数。

### Java 实现
```java
public class Solution {
    static final long MOD = 998244353;
    
    // 计算组合数 C(n, k) % MOD
    public static long comb(int n, int k) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        
        long[] fact = new long[n+1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i-1] * i) % MOD;
        }
        
        long result = fact[n];
        result = (result * modInverse(fact[k], MOD)) % MOD;
        result = (result * modInverse(fact[n-k], MOD)) % MOD;
        return result;
    }
    
    // 计算模逆元
    public static long modInverse(long a, long mod) {
        return power(a, mod-2, mod);
    }
    
    // 快速幂
    public static long power(long base, long exp, long mod) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }
    
    public static void main(String[] args) {
        // 示例计算
        int n = 10, k = 3;
        System.out.println(comb(n, k));
    }
}
```

### Python 实现
```python
MOD = 998244353

# 计算模逆元
def mod_inverse(a, mod):
    return power(a, mod-2, mod)

# 快速幂
def power(base, exp, mod):
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result

# 计算组合数 C(n, k) % MOD
def comb(n, k):
    if k > n or k < 0:
        return 0
    if k == 0 or k == n:
        return 1
    
    fact = [1] * (n+1)
    for i in range(1, n+1):
        fact[i] = (fact[i-1] * i) % MOD
    
    result = fact[n]
    result = (result * mod_inverse(fact[k], MOD)) % MOD
    result = (result * mod_inverse(fact[n-k], MOD)) % MOD
    return result

# 示例计算
n, k = 10, 3
print(comb(n, k))
```

### C++ 实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

const long long MOD = 998244353;

// 快速幂
long long power(long long base, long long exp, long long mod) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exp /= 2;
    }
    return result;
}

// 计算模逆元
long long modInverse(long long a, long long mod) {
    return power(a, mod-2, mod);
}

// 计算组合数 C(n, k) % MOD
long long comb(int n, int k) {
    if (k > n || k < 0) return 0;
    if (k == 0 || k == n) return 1;
    
    vector<long long> fact(n+1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = (fact[i-1] * i) % MOD;
    }
    
    long long result = fact[n];
    result = (result * modInverse(fact[k], MOD)) % MOD;
    result = (result * modInverse(fact[n-k], MOD)) % MOD;
    return result;
}

int main() {
    // 示例计算
    int n = 10, k = 3;
    cout << comb(n, k) << endl;
    return 0;
}
```

### 复杂度分析
- 时间复杂度：O(n)
- 空间复杂度：O(n)

---

## AtCoder ABC165D - Floor Function

### 题目描述
给定正整数 A、B 和 N，求 f(x) = floor(Ax/B) - A*floor(x/B) 在 0 ≤ x ≤ N 时的最大值。

### 解题思路
通过数学分析发现，当 x = min(B-1, N) 时取得最大值。

### Java 实现
```java
public class Solution {
    public static long floorFunction(long A, long B, long N) {
        // 当x = min(B-1, N)时取得最大值
        long x = Math.min(B-1, N);
        return (A * x) / B - A * (x / B);
    }
    
    public static void main(String[] args) {
        long A = 5, B = 7, N = 4;
        System.out.println(floorFunction(A, B, N));
    }
}
```

### Python 实现
```python
def floor_function(A, B, N):
    """
    计算floor函数的最大值
    
    Args:
        A, B, N: 正整数参数
        
    Returns:
        最大值
    """
    # 当x = min(B-1, N)时取得最大值
    x = min(B-1, N)
    return (A * x) // B - A * (x // B)

# 示例计算
A, B, N = 5, 7, 4
print(floor_function(A, B, N))
```

### C++ 实现
```cpp
#include <iostream>
#include <algorithm>
using namespace std;

long long floorFunction(long long A, long long B, long long N) {
    // 当x = min(B-1, N)时取得最大值
    long long x = min(B-1, N);
    return (A * x) / B - A * (x / B);
}

int main() {
    long long A = 5, B = 7, N = 4;
    cout << floorFunction(A, B, N) << endl;
    return 0;
}
```

### 复杂度分析
- 时间复杂度：O(1)
- 空间复杂度：O(1)

---

## 洛谷 P1313 计算系数

### 题目描述
给定多项式 (ax + by)^k，计算展开后 x^n * y^m 项的系数。

### 解题思路
根据二项式定理，系数为 C(k, n) * a^n * b^m。

### Java 实现
```java
import java.util.*;

public class Main {
    static final int MOD = 10007;
    
    // 快速幂
    public static long power(long base, int exp, int mod) {
        long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exp /= 2;
        }
        return result;
    }
    
    // 计算组合数 C(n, k) % MOD
    public static long comb(int n, int k, int mod) {
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        
        long[] fact = new long[n+1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = (fact[i-1] * i) % mod;
        }
        
        long result = fact[n];
        result = (result * power(fact[k], mod-2, mod)) % mod;
        result = (result * power(fact[n-k], mod-2, mod)) % mod;
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int k = scanner.nextInt();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        long result = (comb(k, n, MOD) * power(a, n, MOD)) % MOD;
        result = (result * power(b, m, MOD)) % MOD;
        System.out.println(result);
    }
}
```

### Python 实现
```python
MOD = 10007

# 快速幂
def power(base, exp, mod):
    result = 1
    while exp > 0:
        if exp % 2 == 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp //= 2
    return result

# 计算组合数 C(n, k) % MOD
def comb(n, k, mod):
    if k > n or k < 0:
        return 0
    if k == 0 or k == n:
        return 1
    
    fact = [1] * (n+1)
    for i in range(1, n+1):
        fact[i] = (fact[i-1] * i) % mod
    
    result = fact[n]
    result = (result * power(fact[k], mod-2, mod)) % mod
    result = (result * power(fact[n-k], mod-2, mod)) % mod
    return result

# 读取输入
a, b, k, n, m = map(int, input().split())

result = (comb(k, n, MOD) * power(a, n, MOD)) % MOD
result = (result * power(b, m, MOD)) % MOD
print(result)
```

### C++ 实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

const int MOD = 10007;

// 快速幂
long long power(long long base, int exp, int mod) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exp /= 2;
    }
    return result;
}

// 计算组合数 C(n, k) % MOD
long long comb(int n, int k, int mod) {
    if (k > n || k < 0) return 0;
    if (k == 0 || k == n) return 1;
    
    vector<long long> fact(n+1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = (fact[i-1] * i) % mod;
    }
    
    long long result = fact[n];
    result = (result * power(fact[k], mod-2, mod)) % mod;
    result = (result * power(fact[n-k], mod-2, mod)) % mod;
    return result;
}

int main() {
    int a, b, k, n, m;
    cin >> a >> b >> k >> n >> m;
    
    long long result = (comb(k, n, MOD) * power(a, n, MOD)) % MOD;
    result = (result * power(b, m, MOD)) % MOD;
    cout << result << endl;
    return 0;
}
```

### 复杂度分析
- 时间复杂度：O(k)
- 空间复杂度：O(k)

---

## 杭电 OJ 2032 杨辉三角

### 题目描述
输入n值，使用递归函数，求杨辉三角形中各个位置上的值。

### 解题思路
使用递归或动态规划方法生成杨辉三角。

### Java 实现
```java
import java.util.*;

public class Main {
    // 递归方法计算杨辉三角
    public static int pascal(int n, int m) {
        if (m == 0 || m == n) {
            return 1;
        }
        return pascal(n-1, m-1) + pascal(n-1, m);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        // 生成并输出杨辉三角
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                System.out.print(pascal(i, j) + " ");
            }
            System.out.println();
        }
    }
}
```

### Python 实现
```python
# 递归方法计算杨辉三角
def pascal(n, m):
    if m == 0 or m == n:
        return 1
    return pascal(n-1, m-1) + pascal(n-1, m)

# 读取输入
n = int(input())

# 生成并输出杨辉三角
for i in range(n):
    for j in range(i+1):
        print(pascal(i, j), end=" ")
    print()
```

### C++ 实现
```cpp
#include <iostream>
using namespace std;

// 递归方法计算杨辉三角
int pascal(int n, int m) {
    if (m == 0 || m == n) {
        return 1;
    }
    return pascal(n-1, m-1) + pascal(n-1, m);
}

int main() {
    int n;
    cin >> n;
    
    // 生成并输出杨辉三角
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= i; j++) {
            cout << pascal(i, j) << " ";
        }
        cout << endl;
    }
    
    return 0;
}
```

### 复杂度分析
- 递归方法时间复杂度：O(2^n)
- 动态规划方法时间复杂度：O(n²)
- 空间复杂度：O(n²)