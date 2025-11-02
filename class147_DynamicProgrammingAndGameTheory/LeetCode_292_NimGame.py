"""
LeetCode 292. Nim Game (尼姆游戏)
题目描述：有n个石子，两个玩家轮流从石子堆中拿走1-3个石子，
拿走最后一个石子的玩家获胜。你先手，判断你是否能获胜。

解题思路：
这是一个经典的博弈论问题，可以通过数学规律解决：
1. 当n是4的倍数时，后手必胜
2. 当n不是4的倍数时，先手必胜

核心思想：如果当前玩家面对的是4的倍数个石子，无论他拿走1、2还是3个，
对手都可以通过拿走(4-他拿走的数量)个石子，使得剩余石子数仍然是4的倍数。
最终先手会面对4个石子，无论拿走几个，对手都能拿走剩余的获胜。

时间复杂度：O(1)
空间复杂度：O(1)

工程化考量：
1. 异常处理：处理负数输入
2. 边界条件：n=0时的处理
3. 性能优化：直接使用数学规律，避免递归或DP
"""

def canWinNim(n):
    """
    判断先手是否能获胜
    
    Args:
        n: int - 石子数量
    
    Returns:
        bool - 能获胜返回True，否则返回False
    """
    # 异常处理
    if n < 0:
        return False
    
    # 边界条件
    if n == 0:
        return False # 没有石子，无法获胜
    
    # 数学规律：n不是4的倍数时先手必胜
    return n % 4 != 0


# 测试函数
def test_canWinNim():
    """测试canWinNim函数"""
    # 测试用例1
    n1 = 4
    result1 = canWinNim(n1)
    print(f"Test case 1: n = {n1}, result = {result1}")
    
    # 测试用例2
    n2 = 5
    result2 = canWinNim(n2)
    print(f"Test case 2: n = {n2}, result = {result2}")
    
    # 测试用例3
    n3 = 0
    result3 = canWinNim(n3)
    print(f"Test case 3: n = {n3}, result = {result3}")


# 运行测试
if __name__ == "__main__":
    test_canWinNim()