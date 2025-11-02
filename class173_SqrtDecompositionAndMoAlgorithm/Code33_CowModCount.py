# 牛牛算题 - 整数分块算法实现 (Python版本)
# 题目来源: 牛客编程巅峰赛
# 题目大意: 计算对于小于等于n的每个数p，求n = p×k + m中的k×m之和
# 约束条件: 1 ≤ n ≤ int_max

import sys

MOD = 1000000007

def cow_mod_count(n):
    """
    计算k*m之和，其中n = p*k + m (0 ≤ m < p)
    使用整数分块优化，时间复杂度O(√n)
    """
    ans = 0
    i = 1
    
    while i <= n:
        # 计算当前块的右端点
        k = n // i
        j = n // k
        
        # 计算k*n*(j-i+1) mod MOD
        term1 = (k % MOD) * (n % MOD) % MOD
        term1 = term1 * ((j - i + 1) % MOD) % MOD
        
        # 计算k² * sum(p from i to j) mod MOD
        # sum(p from i to j) = (i + j) * (j - i + 1) // 2
        sum_p = ((i % MOD) + (j % MOD)) % MOD
        sum_p = sum_p * ((j - i + 1) % MOD) % MOD
        # 乘以2的逆元mod MOD
        sum_p = sum_p * 500000004 % MOD  # 2^(-1) mod 1e9+7 = 500000004
        
        term2 = (k % MOD) * (k % MOD) % MOD
        term2 = term2 * sum_p % MOD
        
        # 当前块的贡献是 (term1 - term2) mod MOD
        ans = (ans + term1 - term2 + MOD) % MOD  # +MOD确保结果非负
        
        # 移动到下一个块
        i = j + 1
    
    return ans

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    # 计算结果并输出
    print(cow_mod_count(n))

# 测试用例
# 示例1：输入1，输出0
# 示例2：输入5，输出5

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 整数分块的时间复杂度为O(√n)
- 每个块的计算时间为O(1)
- 总共有O(√n)个块
- 因此总体时间复杂度为O(√n)

空间复杂度分析：
- 只使用了常数个变量
- 空间复杂度为O(1)

Python语言特性注意事项：
1. Python的整数精度没有限制，可以处理非常大的整数
2. 但为了与其他语言保持一致，仍然使用了模运算
3. 使用//运算符进行整数除法
4. 输入输出使用sys.stdin.readline()和print()

数学推导：
对于每个p，有n = p*k + m，其中0 ≤ m < p
所以k = n//p（整数除法），m = n % p = n - p*k
因此k*m = k*(n - p*k) = k*n - k²*p

我们需要计算sum_{p=1}^n (k*n - k²*p)
= n*sum_{p=1}^n k - sum_{p=1}^n k²*p

通过整数分块，我们可以将具有相同k值的p分成一组，每组内的计算可以批量处理。

整数分块原理：
对于函数f(p) = n//p，其值在一定范围内保持不变。
具体来说，当p在[i, j]范围内时，n//p的值都是k，其中j = n//k。
这样我们就可以将连续的p值分成O(√n)个块，每个块内的计算可以批量处理。

示例分析：
当n=5时，p从1到5：
p=1: 5=1*5+0 → k*m=5*0=0
p=2: 5=2*2+1 → k*m=2*1=2
p=3: 5=3*1+2 → k*m=1*2=2
p=4: 5=4*1+1 → k*m=1*1=1
p=5: 5=5*1+0 → k*m=1*0=0
总和：0+2+2+1+0=5
'''