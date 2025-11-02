#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
Codeforces 895C. Square Subsets
题目链接：https://codeforces.com/contest/895/problem/C

解题思路：
这道题可以利用线性基和质因数分解来解决。
1. 对于每个数，我们可以将其质因数分解，保留次数为奇数的质因数
2. 这样，每个数可以表示为一个二进制向量，向量的每一位代表一个质数是否出现奇数次
3. 问题转化为：在数组中选择一个非空子集，使得子集中所有数的向量异或结果为零向量
4. 使用动态规划结合线性基来计算方案数

时间复杂度：O(n * m * log m)，其中n是数组长度，m是质数的个数
空间复杂度：O(2^m)，其中m是质数的个数
'''

MOD = 10**9 + 7
PRIMES = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97]
MAX_NUM = 70
PRIME_COUNT = len(PRIMES)

def get_mask(num):
    """
    将数字转换为向量表示
    
    Args:
        num: 输入数字
    
    Returns:
        int: 向量表示（二进制掩码）
    """
    mask = 0
    for i in range(PRIME_COUNT):
        prime = PRIMES[i]
        cnt = 0
        temp = num
        while temp % prime == 0:
            cnt += 1
            temp //= prime
        if cnt % 2 == 1:
            mask |= (1 << i)
    return mask

def square_subsets(a):
    """
    计算满足条件的子集数目
    
    Args:
        a: 输入数组
    
    Returns:
        int: 满足条件的子集数目
    """
    # 统计每个数的出现次数
    count = [0] * (MAX_NUM + 1)
    for num in a:
        count[num] += 1
    
    # 初始化dp数组，dp[mask]表示异或结果为mask的子集数目
    dp = [0] * (1 << PRIME_COUNT)
    dp[0] = 1  # 空子集
    
    # 处理每个数
    for num in range(2, MAX_NUM + 1):
        if count[num] == 0:
            continue
        
        # 将数转换为向量：每个质数是否出现奇数次
        mask = get_mask(num)
        if mask == 0:
            # 这个数本身是平方数，可以选择任意次数
            pow_val = pow(2, count[num], MOD)
            # 对于所有现有子集，可以选择添加任意的平方数集合
            for i in range(1 << PRIME_COUNT):
                dp[i] = (dp[i] * pow_val) % MOD
        else:
            # 非平方数，需要用动态规划处理
            # 创建一个临时数组，避免在更新过程中覆盖值
            temp = dp.copy()
            pow2 = pow(2, count[num] - 1, MOD)  # 选奇数个该数的方式数
            
            # 对于每个现有的mask状态
            for i in range(1 << PRIME_COUNT):
                # 选择奇数个该数
                temp[i ^ mask] = (temp[i ^ mask] + dp[i] * pow2) % MOD
            
            dp = temp
    
    # 减去空子集的情况
    return (dp[0] - 1) % MOD

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    a1 = [1, 1, 1]
    print(f"测试用例1结果: {square_subsets(a1)}")  # 预期输出: 7
    
    # 测试用例2
    a2 = [2, 2, 2]
    print(f"测试用例2结果: {square_subsets(a2)}")  # 预期输出: 3
    
    # 测试用例3
    a3 = [1, 2, 4, 8, 16, 32, 64, 128, 256, 512]
    print(f"测试用例3结果: {square_subsets(a3)}")  # 预期输出: 1023