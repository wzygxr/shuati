import math
from typing import List

"""
哈希冲突概率估算与素数模选择
算法思想：
1. 哈希冲突概率估算：使用生日悖论公式计算给定元素数量和哈希表大小时的冲突概率
2. 素数模选择：选择合适的素数作为哈希表大小，减少冲突

相关题目：
1. LeetCode 705. 设计哈希集合 - https://leetcode-cn.com/problems/design-hashset/
2. LeetCode 706. 设计哈希映射 - https://leetcode-cn.com/problems/design-hashmap/
3. LintCode 128. 哈希函数 - https://www.lintcode.com/problem/128/
4. CodeChef - HASHTABLE - https://www.codechef.com/problems/HASHTABLE
"""

def calculate_collision_probability(n: int, m: int) -> float:
    """
    计算哈希冲突概率
    使用近似公式：1 - e^(-n²/(2m))，其中n是元素数量，m是哈希表大小
    
    Args:
        n: 元素数量
        m: 哈希表大小
    
    Returns:
        至少发生一次冲突的概率
    
    Raises:
        ValueError: 当参数无效时
    """
    if n <= 0 or m <= 0:
        raise ValueError("元素数量和哈希表大小必须为正整数")
    
    # 使用近似公式：1 - e^(-n²/(2m))
    exponent = -n**2 / (2.0 * m)
    return 1 - math.exp(exponent)

def calculate_exact_collision_probability(n: int, m: int) -> float:
    """
    精确计算哈希冲突概率
    使用公式：1 - (m * (m-1) * (m-2) * ... * (m-n+1)) / m^n
    适用于n较小的情况，避免大数运算溢出
    
    Args:
        n: 元素数量
        m: 哈希表大小
    
    Returns:
        至少发生一次冲突的概率
    
    Raises:
        ValueError: 当参数无效时
    """
    if n <= 0 or m <= 0:
        raise ValueError("元素数量和哈希表大小必须为正整数")
    
    if n > m:
        # 鸽巢原理：当元素数量超过哈希表大小时，必然存在冲突
        return 1.0
    
    no_collision_prob = 1.0
    for i in range(n):
        no_collision_prob *= (m - i) / m
    
    return 1 - no_collision_prob

def is_prime(num: int) -> bool:
    """
    判断一个数是否为素数
    
    Args:
        num: 要判断的数
    
    Returns:
        是否为素数
    """
    if num <= 1:
        return False
    if num <= 3:
        return True
    if num % 2 == 0 or num % 3 == 0:
        return False
    
    # 检查直到sqrt(num)，跳过偶数和3的倍数
    sqrt_num = int(math.isqrt(num)) + 1
    for i in range(5, sqrt_num, 6):
        if num % i == 0 or num % (i + 2) == 0:
            return False
    
    return True

def find_next_prime(target: int) -> int:
    """
    查找大于等于target的下一个素数
    
    Args:
        target: 目标值
    
    Returns:
        大于等于target的最小素数
    """
    if target <= 2:
        return 2
    
    candidate = target + 1 if target % 2 == 0 else target
    while True:
        if is_prime(candidate):
            return candidate
        candidate += 2  # 只检查奇数

def select_optimal_prime_size(expected_size: int, max_load_factor: float) -> int:
    """
    根据预期元素数量和最大负载因子选择合适的哈希表大小（素数）
    
    Args:
        expected_size: 预期元素数量
        max_load_factor: 最大负载因子
    
    Returns:
        推荐的哈希表大小（素数）
    
    Raises:
        ValueError: 当参数无效时
    """
    if expected_size <= 0 or max_load_factor <= 0 or max_load_factor > 1:
        raise ValueError("参数无效：expected_size必须为正整数，max_load_factor必须在(0,1]之间")
    
    # 计算所需的最小大小
    min_size = math.ceil(expected_size / max_load_factor)
    # 选择大于等于min_size的素数
    return find_next_prime(min_size)

def get_common_hash_primes() -> List[int]:
    """
    获取常用的大素数表（用于哈希表大小）
    这些素数都是2^k附近的素数，适合作为哈希表的容量
    
    Returns:
        素数列表
    """
    return [
        131,
        257,
        521,
        1031,
        2053,
        4099,
        8209,
        16411,
        32771,
        65537,
        131101,
        262147,
        524309,
        1048583,
        2097169,
        4194319,
        8388617,
        16777259,
        33554467,
        67108879,
        134217757,
        268435459,
        536870923,
        1073741827
    ]

# 测试函数
def test_hash_analysis():
    # 测试哈希冲突概率计算
    n = 23  # 元素数量
    m = 365  # 哈希表大小（例如一年的天数）
    
    approx_prob = calculate_collision_probability(n, m)
    exact_prob = calculate_exact_collision_probability(n, m)
    
    print("生日悖论示例：")
    print(f"当有{n}个人时，至少有两个人生日相同的概率：")
    print(f"近似概率: {approx_prob:.6f}")
    print(f"精确概率: {exact_prob:.6f}")
    
    # 测试素数选择
    expected_size = 1000
    load_factor = 0.75
    optimal_size = select_optimal_prime_size(expected_size, load_factor)
    
    print("\n哈希表大小选择示例：")
    print(f"预期元素数量: {expected_size}")
    print(f"最大负载因子: {load_factor}")
    print(f"推荐的哈希表大小（素数）: {optimal_size}")
    
    # 测试常用素数表
    print("\n常用哈希素数表：")
    primes = get_common_hash_primes()
    print(", ".join(map(str, primes)))

if __name__ == "__main__":
    test_hash_analysis()