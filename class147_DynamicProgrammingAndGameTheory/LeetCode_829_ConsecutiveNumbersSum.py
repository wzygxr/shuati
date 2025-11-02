"""
LeetCode 829. Consecutive Numbers Sum (连续整数求和)
题目描述：给定一个正整数N，返回连续正整数满足所有数字的和为N的组数。

解题思路：
与原题类似，但需要计算有多少种表示方法：
1. 基于数学推导：2N = k*(2a + k - 1)
2. 枚举k（序列长度），检查是否存在正整数解a
3. 计算满足条件的k的个数

时间复杂度：O(sqrt(N))
空间复杂度：O(1)

工程化考量：
1. 异常处理：处理非正整数输入
2. 边界条件：N=1等特殊情况
3. 性能优化：只枚举到sqrt(2N)
4. 数值溢出：注意大数处理
"""

def consecutiveNumbersSum(N):
    """
    计算连续正整数满足所有数字的和为N的组数
    
    Args:
        N: int - 正整数
    
    Returns:
        int - 满足条件的组数
    """
    # 异常处理
    if N <= 0:
        return 0
    
    # 边界条件
    if N == 1:
        return 1 # 只有1本身
    
    count = 0
    n2 = 2 * N
    
    # 枚举序列长度k
    import math
    for k in range(1, int(math.sqrt(n2)) + 1):
        if n2 % k == 0:
            # k是n2的因数
            m = n2 // k
            
            # 检查k是否能构成有效的连续序列
            # a = (m - k + 1) / 2
            # 需要满足a >= 1，即m - k + 1 >= 2，即m >= k + 1
            if m >= k + 1 and (m - k + 1) % 2 == 0:
                count += 1
            
            # 检查m是否能构成有效的连续序列（k和m不相等时）
            if k != m and k >= m + 1 and (k - m + 1) % 2 == 0:
                count += 1
    
    return count


# 测试函数
def test_consecutiveNumbersSum():
    """测试consecutiveNumbersSum函数"""
    # 测试用例1
    N1 = 15
    result1 = consecutiveNumbersSum(N1)
    print(f"Test case 1: N = {N1}, result = {result1}")
    
    # 测试用例2
    N2 = 9
    result2 = consecutiveNumbersSum(N2)
    print(f"Test case 2: N = {N2}, result = {result2}")
    
    # 测试用例3
    N3 = 1
    result3 = consecutiveNumbersSum(N3)
    print(f"Test case 3: N = {N3}, result = {result3}")


# 运行测试
if __name__ == "__main__":
    test_consecutiveNumbersSum()