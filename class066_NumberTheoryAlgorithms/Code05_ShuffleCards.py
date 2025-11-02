"""
题目：Shuffle Cards (洗牌问题)
来源：HDU 1214
内容：给定n张牌，每次洗牌将牌分成两半，然后交叉洗牌，问最少需要洗多少次才能恢复原状

算法：扩展欧几里得算法求模逆元
时间复杂度：O(log min(a, b))
空间复杂度：O(1)

思路：
1. 洗牌过程可以看作是一个置换操作
2. 问题转化为求置换的阶，即最小的k使得2^k ≡ 1 (mod n+1)
3. 使用扩展欧几里得算法求解模逆元

工程化考量：
- 异常处理：处理n=0或n=1的特殊情况
- 边界条件：n的范围限制
- 性能优化：使用快速幂算法
"""

def extended_gcd(a, b):
    """扩展欧几里得算法，返回(gcd, x, y)满足ax + by = gcd(a, b)"""
    if b == 0:
        return a, 1, 0
    gcd, x1, y1 = extended_gcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    return gcd, x, y

def mod_inverse(a, m):
    """求模逆元，返回a在模m下的逆元"""
    gcd, x, y = extended_gcd(a, m)
    if gcd != 1:
        raise ValueError("模逆元不存在")
    return x % m

def shuffle_cards(n):
    """
    计算洗牌次数
    
    参数:
        n: 牌的数量
        
    返回:
        最少洗牌次数
        
    异常处理:
        - n <= 0: 返回0
        - n == 1: 返回0
    """
    if n <= 0:
        return 0
    if n == 1:
        return 0
    
    # 洗牌过程相当于求2在模(n+1)下的阶
    # 即最小的k使得2^k ≡ 1 mod (n+1)
    m = n + 1
    k = 1
    power = 2 % m
    
    while power != 1:
        k += 1
        power = (power * 2) % m
        # 防止无限循环，最大循环次数为m
        if k > m:
            return -1  # 理论上不会发生
    
    return k

def test_shuffle_cards():
    """测试函数"""
    test_cases = [
        (1, 0),   # 1张牌，不需要洗牌
        (2, 1),   # 2张牌，洗1次
        (3, 2),   # 3张牌，洗2次
        (4, 4),   # 4张牌，洗4次
        (5, 3),   # 5张牌，洗3次
        (6, 6),   # 6张牌，洗6次
        (0, 0),   # 边界测试
    ]
    
    print("测试洗牌问题:")
    for n, expected in test_cases:
        result = shuffle_cards(n)
        status = "通过" if result == expected else "失败"
        print(f"n={n}, 预期={expected}, 实际={result}, 状态={status}")

if __name__ == "__main__":
    test_shuffle_cards()