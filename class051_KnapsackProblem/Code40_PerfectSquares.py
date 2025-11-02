# LeetCode 279. 完全平方数
# 题目描述：给你一个整数 n ，返回和为 n 的完全平方数的最少数量。
# 完全平方数是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
# 链接：https://leetcode.cn/problems/perfect-squares/
# 
# 解题思路：
# 这是一个完全背包问题。我们可以将问题转化为：使用最少数量的物品（每个物品是一个完全平方数），恰好装满容量为n的背包。
# 
# 状态定义：dp[i] 表示和为i的完全平方数的最少数量
# 状态转移方程：dp[i] = min(dp[i], dp[i - j * j] + 1)，其中j * j <= i
# 初始状态：dp[0] = 0，表示和为0的完全平方数的最少数量为0
# 
# 时间复杂度：O(n * sqrt(n))，其中n是给定的整数
# 空间复杂度：O(n)，使用一维DP数组

from collections import deque
import math

def num_squares(n: int) -> int:
    """
    找出和为n的完全平方数的最少数量
    
    参数:
        n: 给定的整数
    
    返回:
        最少数量
    """
    if n < 1:
        return 0
    
    # 创建DP数组，dp[i]表示和为i的完全平方数的最少数量
    dp = [0] * (n + 1)
    
    # 初始化DP数组，初始值设为最大可能值（即全部用1相加）
    for i in range(1, n + 1):
        dp[i] = i  # 最坏情况下，i可以由i个1组成
    
    # 填充DP数组
    for i in range(2, n + 1):
        # 尝试所有可能的完全平方数j^2，其中j^2 <= i
        for j in range(1, int(math.sqrt(i)) + 1):
            # 更新状态：选择当前完全平方数j^2，那么问题转化为求dp[i - j * j] + 1
            dp[i] = min(dp[i], dp[i - j * j] + 1)
    
    return dp[n]

def num_squares_optimized(n: int) -> int:
    """
    优化版本，预先生成所有可能的完全平方数
    
    参数:
        n: 给定的整数
    
    返回:
        最少数量
    """
    if n < 1:
        return 0
    
    # 预先生成所有可能的完全平方数
    max_square_root = int(math.sqrt(n))
    squares = [(i + 1) * (i + 1) for i in range(max_square_root)]
    
    # 创建DP数组
    dp = [0] * (n + 1)
    
    # 初始化DP数组
    for i in range(1, n + 1):
        dp[i] = i
    
    # 填充DP数组
    for i in range(2, n + 1):
        # 尝试所有可能的完全平方数
        for square in squares:
            if square > i:
                break  # 由于squares是递增的，当square > i时，后面的平方数也都大于i，直接break
            dp[i] = min(dp[i], dp[i - square] + 1)
    
    return dp[n]

def num_squares_bfs(n: int) -> int:
    """
    使用广度优先搜索(BFS)实现
    我们可以将每个数字看作一个节点，如果两个数字之间相差一个完全平方数，那么它们之间有一条边
    问题转化为：从0出发，到n的最短路径长度
    
    参数:
        n: 给定的整数
    
    返回:
        最少数量
    """
    if n < 1:
        return 0
    
    # 预先生成所有可能的完全平方数
    max_square_root = int(math.sqrt(n))
    squares = [(i + 1) * (i + 1) for i in range(max_square_root)]
    
    # 使用队列进行BFS
    queue = deque()
    visited = [False] * (n + 1)  # 记录哪些数字已经访问过，避免重复处理
    
    queue.append(0)  # 从0开始
    visited[0] = True
    level = 0  # 当前的层数，即使用的完全平方数的数量
    
    while queue:
        level += 1
        size = len(queue)
        
        # 处理当前层的所有节点
        for _ in range(size):
            current = queue.popleft()
            
            # 尝试所有可能的完全平方数
            for square in squares:
                next_num = current + square
                
                if next_num == n:
                    return level  # 找到目标值，返回当前层数
                
                if next_num > n or visited[next_num]:
                    continue  # 超过目标值或者已经访问过，跳过
                
                visited[next_num] = True
                queue.append(next_num)
    
    return n  # 默认返回n（实际上不应该到达这里）

def num_squares_math(n: int) -> int:
    """
    使用数学方法优化，基于拉格朗日四平方定理
    四平方定理：每个自然数都可以表示为4个整数的平方和
    
    参数:
        n: 给定的整数
    
    返回:
        最少数量
    """
    if n < 1:
        return 0
    
    # 如果n是完全平方数，直接返回1
    if is_perfect_square(n):
        return 1
    
    # 检查是否可以表示为两个完全平方数的和
    if can_be_expressed_as_sum_of_two_squares(n):
        return 2
    
    # 检查是否可以表示为三个完全平方数的和
    # 根据Legendre三平方定理，如果n不是形如4^k*(8m+7)，则可以表示为三个平方数的和
    temp = n
    while temp % 4 == 0:
        temp //= 4
    if temp % 8 != 7:
        return 3
    
    # 根据四平方定理，所有自然数都可以表示为4个平方数的和
    return 4

def is_perfect_square(num: int) -> bool:
    """
    判断一个数是否是完全平方数
    
    参数:
        num: 要判断的数
    
    返回:
        是否是完全平方数
    """
    sqrt_num = int(math.sqrt(num))
    return sqrt_num * sqrt_num == num

def can_be_expressed_as_sum_of_two_squares(num: int) -> bool:
    """
    判断一个数是否可以表示为两个完全平方数的和
    
    参数:
        num: 要判断的数
    
    返回:
        是否可以表示为两个完全平方数的和
    """
    for i in range(int(math.sqrt(num)) + 1):
        remainder = num - i * i
        if is_perfect_square(remainder):
            return True
    return False

def num_squares_dp_alt(n: int) -> int:
    """
    另一种DP实现方式，从完全平方数的角度考虑
    
    参数:
        n: 给定的整数
    
    返回:
        最少数量
    """
    if n < 1:
        return 0
    
    # 创建DP数组
    dp = [float('inf')] * (n + 1)
    dp[0] = 0  # 初始状态：和为0的完全平方数的最少数量为0
    
    # 对于每个完全平方数
    for i in range(1, int(math.sqrt(n)) + 1):
        square = i * i
        # 对于所有可以表示为当前完全平方数加上另一个数的数
        for j in range(square, n + 1):
            # 更新状态
            dp[j] = min(dp[j], dp[j - square] + 1)
    
    return dp[n]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    n1 = 12
    print(f"测试用例1结果: {num_squares(n1)} (预期: 3)")
    print(f"优化版本结果: {num_squares_optimized(n1)}")
    print(f"BFS版本结果: {num_squares_bfs(n1)}")
    print(f"数学优化版本结果: {num_squares_math(n1)}")
    print(f"另一种DP实现结果: {num_squares_dp_alt(n1)}")
    
    print("---------------------------")
    
    # 测试用例2
    n2 = 13
    print(f"测试用例2结果: {num_squares(n2)} (预期: 2)")
    print(f"优化版本结果: {num_squares_optimized(n2)}")
    print(f"BFS版本结果: {num_squares_bfs(n2)}")
    print(f"数学优化版本结果: {num_squares_math(n2)}")
    
    print("---------------------------")
    
    # 测试用例3
    n3 = 1
    print(f"测试用例3结果: {num_squares(n3)} (预期: 1)")
    
    print("---------------------------")
    
    # 测试用例4
    n4 = 2
    print(f"测试用例4结果: {num_squares(n4)} (预期: 2)")
    
    # 测试用例5 - 边界情况
    n5 = 0
    print(f"\n测试用例5结果: {num_squares(n5)} (预期: 0)")
    
    # 测试用例6 - 较大的数
    n6 = 1000
    print(f"\n测试用例6结果: {num_squares(n6)}")
    print(f"数学优化版本结果: {num_squares_math(n6)}")