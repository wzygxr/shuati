import math

"""
Codeforces 1415A - Prison Break

问题描述：
有一个 n×m 的监狱网格，每个囚犯都在自己的牢房里。
当夜晚来临时，囚犯们可以开始移动到相邻的牢房（上、下、左、右）。
有一个秘密隧道在位置 (r,c)，所有囚犯都想逃到那里。
问题是要找到所有囚犯到达位置 (r,c) 所需的最少秒数。

解题思路：
这是一个简单的数学问题。对于网格中的任意位置 (i,j) 到目标位置 (r,c)，
最短距离就是曼哈顿距离：|i-r| + |j-c|
但是题目要求的是所有囚犯都能到达目标位置的最少时间，
这意味着我们需要找到所有可能位置到目标位置的最大曼哈顿距离。

在 n×m 的网格中，距离 (r,c) 最远的角落是四个角落之一：
1. (1,1) - 左上角
2. (1,m) - 右上角
3. (n,1) - 左下角
4. (n,m) - 右下角

我们只需要计算这四个角落到目标位置的距离，然后取最大值。

时间复杂度：O(1)
空间复杂度：O(1)
"""

def min_time_to_escape(n: int, m: int, r: int, c: int) -> int:
    """
    计算监狱逃脱所需的最少时间
    
    Args:
        n: 网格行数
        m: 网格列数
        r: 目标行位置（1-indexed）
        c: 目标列位置（1-indexed）
        
    Returns:
        最少时间
    """
    # 计算四个角落到目标位置的曼哈顿距离，取最大值
    dist1 = abs(1 - r) + abs(1 - c)      # 左上角 (1,1)
    dist2 = abs(1 - r) + abs(m - c)      # 右上角 (1,m)
    dist3 = abs(n - r) + abs(1 - c)      # 左下角 (n,1)
    dist4 = abs(n - r) + abs(m - c)      # 右下角 (n,m)
    
    return max(dist1, dist2, dist3, dist4)


def min_time_to_escape_v2(n: int, m: int, r: int, c: int) -> int:
    """
    另一种更直观的解法
    对于每个维度，找到最远的距离
    
    Args:
        n: 网格行数
        m: 网格列数
        r: 目标行位置（1-indexed）
        c: 目标列位置（1-indexed）
        
    Returns:
        最少时间
    """
    # 在行方向上，最远距离是到第一行或最后一行的距离
    row_dist = max(r - 1, n - r)
    
    # 在列方向上，最远距离是到第一列或最后一列的距离
    col_dist = max(c - 1, m - c)
    
    # 总的最远距离是两个方向距离之和
    return row_dist + col_dist


def main():
    """测试函数"""
    print("=== Codeforces 1415A - Prison Break ===")
    print("问题：计算所有囚犯逃到位置(r,c)所需的最少时间")
    
    # 测试用例1
    print("\n测试用例1:")
    n1, m1, r1, c1 = 10, 10, 1, 1
    result1 = min_time_to_escape(n1, m1, r1, c1)
    result1_v2 = min_time_to_escape_v2(n1, m1, r1, c1)
    print(f"网格大小: {n1}×{m1}, 目标位置: ({r1},{c1})")
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result1_v2}")
    
    # 测试用例2
    print("\n测试用例2:")
    n2, m2, r2, c2 = 5, 3, 2, 2
    result2 = min_time_to_escape(n2, m2, r2, c2)
    result2_v2 = min_time_to_escape_v2(n2, m2, r2, c2)
    print(f"网格大小: {n2}×{m2}, 目标位置: ({r2},{c2})")
    print(f"方法1结果: {result2}")
    print(f"方法2结果: {result2_v2}")
    
    # 测试用例3
    print("\n测试用例3:")
    n3, m3, r3, c3 = 3, 3, 2, 2
    result3 = min_time_to_escape(n3, m3, r3, c3)
    result3_v2 = min_time_to_escape_v2(n3, m3, r3, c3)
    print(f"网格大小: {n3}×{m3}, 目标位置: ({r3},{c3})")
    print(f"方法1结果: {result3}")
    print(f"方法2结果: {result3_v2}")
    
    # 交互式测试
    print("\n=== 交互式测试 ===")
    print("请输入网格大小和目标位置 (n m r c)，或输入0 0 0 0退出:")
    while True:
        try:
            line = input()
            n, m, r, c = map(int, line.split())
            
            if n == 0 and m == 0 and r == 0 and c == 0:
                break
            
            result = min_time_to_escape(n, m, r, c)
            print(f"网格大小: {n}×{m}, 目标位置: ({r},{c}), 最少时间: {result}")
        except EOFError:
            break
        except ValueError:
            print("输入格式错误，请重新输入")
    
    print("程序结束")


if __name__ == "__main__":
    main()