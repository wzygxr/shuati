#!/usr/bin/env python3
"""
Codeforces 1681D. Required Length

题目来源：https://codeforces.com/contest/1681/problem/D

题目描述：
给定一个整数 n 和一个目标长度 k，你需要找到一个 n 位数 x，
使得通过不断将 x 乘以它的某一位数字，最终能得到一个至少 k 位的数。
求最小的操作次数，如果无法达到目标则返回 -1。

算法思路：
这是一个BFS搜索问题，可以使用以下方法解决：
1. BFS搜索：从初始数字开始，逐步生成新的数字
2. 剪枝优化：避免重复访问相同的数字
3. 最近点对思想：在状态空间中寻找最优路径

虽然这不是经典的最近点对问题，但可以看作是在状态空间中寻找最短路径的问题，
与最近点对问题有相似的搜索和优化思想。

时间复杂度：
- BFS搜索：O(状态数)
- 空间复杂度：O(状态数)

应用场景：
1. 数学游戏：数字变换游戏
2. 密码学：数字序列生成
3. 算法竞赛：状态搜索问题

相关题目：
1. LeetCode 973. 最接近原点的 K 个点
2. LeetCode 612. 平面上的最短距离
3. Codeforces 1042D. Petya and Array
"""

from collections import deque

def required_length(n, k):
    """
    BFS解法
    时间复杂度：O(状态数)
    空间复杂度：O(状态数)
    :param n: 初始数字
    :param k: 目标长度
    :return: 最小操作次数，如果无法达到目标则返回-1
    """
    # 如果初始数字已经满足长度要求
    if len(str(n)) >= k:
        return 0
    
    # BFS队列，存储(当前数字, 操作次数)
    queue = deque([(n, 0)])
    
    # 记录已访问的数字
    visited = set([n])
    
    while queue:
        num, steps = queue.popleft()
        
        # 获取数字的每一位
        num_str = str(num)
        for digit_char in num_str:
            digit = int(digit_char)
            # 跳过0，因为乘以0会得到0
            if digit == 0:
                continue
            
            new_num = num * digit
            # 如果新数字满足长度要求
            if len(str(new_num)) >= k:
                return steps + 1
            
            # 如果新数字未访问过且长度小于目标长度太多（剪枝）
            if new_num not in visited and len(str(new_num)) < k:
                visited.add(new_num)
                queue.append((new_num, steps + 1))
    
    return -1  # 无法达到目标

def required_length_optimized(n, k):
    """
    优化的BFS解法
    时间复杂度：O(状态数)
    空间复杂度：O(状态数)
    :param n: 初始数字
    :param k: 目标长度
    :return: 最小操作次数，如果无法达到目标则返回-1
    """
    # 如果初始数字已经满足长度要求
    if len(str(n)) >= k:
        return 0
    
    # BFS队列，存储(当前数字, 操作次数)
    queue = deque([(n, 0)])
    
    # 记录已访问的数字
    visited = set([n])
    
    # 目标长度
    target_length = k
    
    while queue:
        num, steps = queue.popleft()
        
        # 获取数字的每一位
        num_str = str(num)
        # 按数字大小降序排列，优先处理大的数字（贪心策略）
        digits = sorted([int(d) for d in num_str], reverse=True)
        
        for digit in digits:
            # 跳过0，因为乘以0会得到0
            if digit == 0:
                continue
            
            new_num = num * digit
            # 如果新数字满足长度要求
            if len(str(new_num)) >= target_length:
                return steps + 1
            
            # 剪枝：如果新数字长度已经超过目标太多，跳过
            if len(str(new_num)) > target_length + 5:
                continue
            
            # 如果新数字未访问过
            if new_num not in visited:
                visited.add(new_num)
                queue.append((new_num, steps + 1))
    
    return -1  # 无法达到目标

def test_required_length():
    """测试函数"""
    print("=== 测试 Codeforces 1681D. Required Length ===")
    
    # 测试用例1
    n1, k1 = 1, 3
    print("测试用例1:")
    print(f"n: {n1}, k: {k1}")
    print(f"BFS解法结果: {required_length(n1, k1)}")
    print(f"优化BFS解法结果: {required_length_optimized(n1, k1)}")
    print("期望结果: 3")
    print()
    
    # 测试用例2
    n2, k2 = 123, 5
    print("测试用例2:")
    print(f"n: {n2}, k: {k2}")
    print(f"BFS解法结果: {required_length(n2, k2)}")
    print(f"优化BFS解法结果: {required_length_optimized(n2, k2)}")
    print("期望结果: 2")
    print()
    
    # 测试用例3
    n3, k3 = 999, 2
    print("测试用例3:")
    print(f"n: {n3}, k: {k3}")
    print(f"BFS解法结果: {required_length(n3, k3)}")
    print(f"优化BFS解法结果: {required_length_optimized(n3, k3)}")
    print("期望结果: 0")
    print()

if __name__ == "__main__":
    test_required_length()