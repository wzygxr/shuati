"""
LeetCode 878. 第 N 个神奇数字
如果正整数可以被 A 或 B 整除，那么它是神奇的。
返回第 N 个神奇数字。由于答案可能非常大，返回它模 10^9 + 7 的结果。
1 <= N <= 10^9
2 <= A, B <= 40000
测试链接：https://leetcode.cn/problems/nth-magical-number/

LeetCode 878. 第 N 个神奇数字

问题描述：
如果正整数可以被 A 或 B 整除，那么它是神奇的。
返回第 N 个神奇数字。由于答案可能非常大，返回它模 10^9 + 7 的结果。

解题思路：
1. 使用二分搜索法在可能的范围内查找第 N 个神奇数字
2. 对于给定的数字 x，计算小于等于 x 的神奇数字个数
3. 神奇数字个数 = x/A + x/B - x/lcm(A,B)
4. 使用容斥原理避免重复计数

数学原理：
1. 容斥原理：|A ∪ B| = |A| + |B| - |A ∩ B|
2. 最小公倍数：lcm(a,b) = a*b / gcd(a,b)
3. 二分搜索：在有序序列中快速定位目标值

时间复杂度：O(log(N * min(A,B)))，二分搜索的时间复杂度
空间复杂度：O(1)

相关题目：
1. LeetCode 878. 第 N 个神奇数字
   链接：https://leetcode.cn/problems/nth-magical-number/
2. LeetCode 1201. 丑数 III
   链接：https://leetcode.cn/problems/ugly-number-iii/
3. LeetCode 204. 计数质数
   链接：https://leetcode.cn/problems/count-primes/
"""

MOD = 10**9 + 7

def gcd(a: int, b: int) -> int:
    """
    计算两个数的最大公约数（欧几里得算法）
    
    Args:
        a: 第一个数
        b: 第二个数
        
    Returns:
        a 和 b 的最大公约数
    """
    while b != 0:
        a, b = b, a % b
    return a

def lcm(a: int, b: int) -> int:
    """
    计算两个数的最小公倍数
    
    Args:
        a: 第一个数
        b: 第二个数
        
    Returns:
        a 和 b 的最小公倍数
    """
    return a * b // gcd(a, b)

def count_magical_numbers(x: int, A: int, B: int, lcm_val: int) -> int:
    """
    计算小于等于 x 的神奇数字个数
    
    Args:
        x: 上限
        A: 第一个除数
        B: 第二个除数
        lcm_val: A 和 B 的最小公倍数
        
    Returns:
        小于等于 x 的神奇数字个数
    """
    return x // A + x // B - x // lcm_val

def nth_magical_number(N: int, A: int, B: int) -> int:
    """
    计算第 N 个神奇数字
    
    Args:
        N: 第 N 个
        A: 第一个除数
        B: 第二个除数
        
    Returns:
        第 N 个神奇数字模 10^9+7 的结果
    """
    # 计算最小公倍数
    lcm_val = lcm(A, B)
    
    # 二分搜索的左右边界
    left = 1
    right = N * min(A, B)
    
    while left < right:
        mid = left + (right - left) // 2
        # 计算小于等于 mid 的神奇数字个数
        count = count_magical_numbers(mid, A, B, lcm_val)
        
        if count < N:
            left = mid + 1
        else:
            right = mid
    
    return left % MOD

if __name__ == "__main__":
    # 测试用例1：基本测试
    print(f"测试用例1: N=1, A=2, B=3 -> {nth_magical_number(1, 2, 3)}")  # 2
    
    # 测试用例2：N=4的情况
    print(f"测试用例2: N=4, A=2, B=3 -> {nth_magical_number(4, 2, 3)}")  # 6
    
    # 测试用例3：A和B相等的情况
    print(f"测试用例3: N=3, A=2, B=2 -> {nth_magical_number(3, 2, 2)}")  # 6
    
    # 测试用例4：较大的N
    print(f"测试用例4: N=5, A=2, B=4 -> {nth_magical_number(5, 2, 4)}")  # 10
    
    # 测试用例5：边界情况
    print(f"测试用例5: N=1000000000, A=40000, B=40000 -> {nth_magical_number(1000000000, 40000, 40000)}")
    
    # 验证容斥原理
    print("验证容斥原理:")
    A_val, B_val = 2, 3
    lcm_val = lcm(A_val, B_val)
    print(f"A={A_val}, B={B_val}, lcm={lcm_val}")
    print(f"x=10时，神奇数字个数: {count_magical_numbers(10, A_val, B_val, lcm_val)}")
    print("实际神奇数字: 2,3,4,6,8,9,10 -> 共7个")
    
    # 测试最大公约数和最小公倍数
    print(f"gcd(12, 18) = {gcd(12, 18)}")  # 6
    print(f"lcm(12, 18) = {lcm(12, 18)}")  # 36