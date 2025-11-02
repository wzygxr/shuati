"""
LeetCode 365. 水壶问题
有两个容量分别为 x 升和 y 升的水壶以及无限多的水。
请判断能否通过使用这两个水壶，从而可以得到恰好 z 升的水？
如果可以，最后请用以上水壶中的一或两个来盛放取得的 z 升水。
你允许：
1. 装满任意一个水壶
2. 清空任意一个水壶
3. 从一个水壶向另外一个水壶倒水，直到装满或者倒空
测试链接：https://leetcode.cn/problems/water-and-jug-problem/

LeetCode 365. 水壶问题

问题描述：
有两个容量分别为 x 升和 y 升的水壶以及无限多的水。
请判断能否通过使用这两个水壶，从而可以得到恰好 z 升的水？

解题思路：
1. 根据裴蜀定理，如果 z 是 x 和 y 的最大公约数的倍数，且 z <= x + y，则有解
2. 特殊情况：如果 z == 0，直接返回 True
3. 如果 x + y < z，返回 False
4. 如果 x == 0 或 y == 0，需要特殊处理

数学原理：
1. 裴蜀定理：方程 ax + by = z 有整数解当且仅当 gcd(a,b) 能整除 z
2. 水壶问题可以转化为线性丢番图方程：x * a + y * b = z
   a 和 b 可以是正数（装满）或负数（倒空）

时间复杂度：O(log(min(x,y)))，主要消耗在求最大公约数上
空间复杂度：O(1)

相关题目：
1. LeetCode 365. 水壶问题
   链接：https://leetcode.cn/problems/water-and-jug-problem/
2. POJ 2142 The Balance
   链接：http://poj.org/problem?id=2142
3. UVA 10090 Marbles
   链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
"""

def gcd(a: int, b: int) -> int:
    """
    计算两个数的最大公约数（欧几里得算法）
    
    Args:
        a: 第一个数
        b: 第二个数
        
    Returns:
        a 和 b 的最大公约数
    """
    # 使用欧几里得算法
    while b != 0:
        a, b = b, a % b
    return a

def extended_gcd(a: int, b: int) -> tuple:
    """
    扩展欧几里得算法，求解 ax + by = gcd(a,b) 的一组特解
    
    Args:
        a: 第一个系数
        b: 第二个系数
        
    Returns:
        (x, y, gcd) 的元组
    """
    if b == 0:
        return (1, 0, a)
    x, y, g = extended_gcd(b, a % b)
    return (y, x - (a // b) * y, g)

def can_measure_water(x: int, y: int, z: int) -> bool:
    """
    判断是否可以通过两个水壶得到恰好 z 升水
    
    Args:
        x: 第一个水壶的容量
        y: 第二个水壶的容量
        z: 目标水量
        
    Returns:
        是否可以得到 z 升水
    """
    # 边界条件处理
    if z < 0:
        return False
    if z == 0:
        return True
    if x + y < z:
        return False
    if x == 0 and y == 0:
        return z == 0
    if x == 0:
        return z % y == 0
    if y == 0:
        return z % x == 0
    
    # 使用裴蜀定理判断
    return z % gcd(x, y) == 0

if __name__ == "__main__":
    # 测试用例1：经典水壶问题
    print(f"测试用例1: x=3, y=5, z=4 -> {can_measure_water(3, 5, 4)}")  # True
    
    # 测试用例2：无法得到的情况
    print(f"测试用例2: x=2, y=6, z=5 -> {can_measure_water(2, 6, 5)}")  # False
    
    # 测试用例3：边界情况
    print(f"测试用例3: x=0, y=0, z=0 -> {can_measure_water(0, 0, 0)}")  # True
    print(f"测试用例4: x=0, y=5, z=0 -> {can_measure_water(0, 5, 0)}")  # True
    print(f"测试用例5: x=0, y=5, z=10 -> {can_measure_water(0, 5, 10)}")  # False
    
    # 测试用例6：裴蜀定理验证
    print(f"测试用例6: x=4, y=6, z=2 -> {can_measure_water(4, 6, 2)}")  # True
    print(f"测试用例7: x=4, y=6, z=7 -> {can_measure_water(4, 6, 7)}")  # False
    
    # 测试扩展欧几里得算法
    x, y, g = extended_gcd(4, 6)
    print(f"扩展欧几里得算法测试: 4*{x} + 6*{y} = {g}")  # 4*(-1) + 6*(1) = 2