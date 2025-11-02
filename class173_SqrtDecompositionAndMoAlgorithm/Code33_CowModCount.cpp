// 牛牛算题 - 整数分块算法实现 (C++版本)
// 题目来源: 牛客编程巅峰赛
// 题目大意: 计算对于小于等于n的每个数p，求n = p×k + m中的k×m之和
// 约束条件: 1 ≤ n ≤ INT_MAX

#include <cstdio>
using namespace std;

typedef long long ll;
const int MOD = 1000000007;

// 计算k*m之和，其中n = p*k + m (0 ≤ m < p)
// 使用整数分块优化，时间复杂度O(√n)
ll cowModCount(ll n) {
    ll ans = 0;
    ll i = 1;
    
    while (i <= n) {
        // 计算当前块的右端点
        // 对于相同的k = n/p，p的取值范围是 [i, j]
        // 其中j = n/k
        ll k = n / i;
        ll j = n / k;
        
        // 计算当前块贡献的总和
        // 对于p ∈ [i, j]，k = n/p
        // 所以 m = n % p = n - p*k
        // 因此k*m = k*(n - p*k) = k*n - k²*p
        
        // 计算k*n*(j-i+1) mod MOD
        ll term1 = (k % MOD) * (n % MOD) % MOD;
        term1 = term1 * ((j - i + 1) % MOD) % MOD;
        
        // 计算k² * sum(p from i to j) mod MOD
        // sum(p from i to j) = (i + j) * (j - i + 1) / 2
        ll sum_p = ((i % MOD) + (j % MOD)) % MOD;
        sum_p = sum_p * ((j - i + 1) % MOD) % MOD;
        sum_p = sum_p * 500000004 % MOD; // 乘以2的逆元mod MOD
        
        ll term2 = (k % MOD) * (k % MOD) % MOD;
        term2 = term2 * sum_p % MOD;
        
        // 当前块的贡献是 (term1 - term2) mod MOD
        ans = (ans + term1 - term2 + MOD) % MOD; // +MOD确保结果非负
        
        // 移动到下一个块
        i = j + 1;
    }
    
    return ans;
}

int main() {
    ll n;
    scanf("%lld", &n);
    printf("%lld\n", cowModCount(n));
    return 0;
}

/*
时间复杂度分析：
- 整数分块的时间复杂度为O(√n)
- 每个块的计算时间为O(1)
- 总共有O(√n)个块
- 因此总体时间复杂度为O(√n)

空间复杂度分析：
- 只使用了常数个变量
- 空间复杂度为O(1)

优化说明：
1. 使用整数分块将时间复杂度从O(n)降低到O(√n)
2. 利用模运算的性质避免数值溢出
3. 预先计算2的逆元以快速计算等差数列和

数学推导：
对于每个p，有n = p*k + m，其中0 ≤ m < p
所以k = n/p（整数除法），m = n % p = n - p*k
因此k*m = k*(n - p*k) = k*n - k²*p

我们需要计算sum_{p=1}^n (k*n - k²*p)
= n*sum_{p=1}^n k - sum_{p=1}^n k²*p

通过整数分块，我们可以将具有相同k值的p分成一组，每组内的计算可以批量处理。

工程化考虑：
1. 大数处理：使用long long类型避免溢出
2. 模运算：注意负数的模运算处理
3. 性能优化：块处理比逐个计算更高效
4. 测试用例：
   - 示例1：输入1，输出0（1 = 1*1 + 0，k*m=1*0=0）
   - 示例2：输入5，输出5（计算所有p的k*m之和为5）
*/