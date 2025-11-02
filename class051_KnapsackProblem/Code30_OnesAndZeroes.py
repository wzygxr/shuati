# LeetCode 474. 一和零
# 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。请你找出并返回 strs 的最大子集的长度，
# 该子集中最多有 m 个 0 和 n 个 1 。如果所有字符串都不满足条件，返回 0 。
# 链接：https://leetcode.cn/problems/ones-and-zeroes/
# 
# 解题思路：
# 这是一个二维费用的0-1背包问题：
# - 每个字符串可以看作是一个物品
# - 物品的重量有两个维度：0的个数和1的个数
# - 背包的容量也有两个维度：m（最多m个0）和n（最多n个1）
# - 我们需要找出最多能放多少个物品，使得两个维度的重量都不超过背包容量
# 
# 状态定义：dp[i][j] 表示最多使用i个0和j个1时，可以组成的最大子集的长度
# 状态转移方程：对于每个字符串，计算其包含的0的个数zeros和1的个数ones
# dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)（当i >= zeros且j >= ones时）
# 初始状态：dp[0][0] = 0，表示不使用任何字符时，子集长度为0
# 
# 时间复杂度：O(l * m * n)，其中l是字符串数组的长度，m和n是给定的两个整数
# 空间复杂度：O(m * n)，使用二维DP数组

from typing import List
from functools import lru_cache


def findMaxForm(strs: List[str], m: int, n: int) -> int:
    """
    找出strs的最大子集长度，该子集中最多有m个0和n个1
    
    Args:
        strs: 二进制字符串数组
        m: 最多允许的0的个数
        n: 最多允许的1的个数
    
    Returns:
        满足条件的最大子集长度
    """
    # 参数验证
    if not strs:
        return 0
    
    # 创建二维DP数组，dp[i][j]表示最多使用i个0和j个1时，可以组成的最大子集的长度
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 对于每个字符串，计算其包含的0的个数和1的个数
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        
        # 二维0-1背包问题，需要逆序遍历两个维度
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                # 状态转移：选择当前字符串或不选择当前字符串，取最大值
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    # 返回最多使用m个0和n个1时，可以组成的最大子集的长度
    return dp[m][n]


def findMaxFormOptimized(strs: List[str], m: int, n: int) -> int:
    """
    优化版本：预处理每个字符串的0和1的个数
    """
    if not strs:
        return 0
    
    l = len(strs)
    # 预处理每个字符串的0和1的个数
    counts = []  # counts[i][0]表示第i个字符串中0的个数，counts[i][1]表示1的个数
    
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append([zeros, ones])
    
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串
    for zeros, ones in counts:
        # 逆序遍历两个维度
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]


def findMaxForm3D(strs: List[str], m: int, n: int) -> int:
    """
    三维DP数组实现（更直观但空间复杂度更高）
    dp[k][i][j]表示考虑前k个字符串，最多使用i个0和j个1时，可以组成的最大子集的长度
    """
    if not strs:
        return 0
    
    l = len(strs)
    # 预处理每个字符串的0和1的个数
    counts = []
    
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append([zeros, ones])
    
    # 创建三维DP数组
    dp = [[[0] * (n + 1) for _ in range(m + 1)] for __ in range(l + 1)]
    
    # 填充DP数组
    for k in range(1, l + 1):
        zeros, ones = counts[k - 1]
        
        for i in range(m + 1):
            for j in range(n + 1):
                # 不选择第k个字符串
                dp[k][i][j] = dp[k - 1][i][j]
                
                # 选择第k个字符串（如果可以的话）
                if i >= zeros and j >= ones:
                    dp[k][i][j] = max(dp[k][i][j], dp[k - 1][i - zeros][j - ones] + 1)
    
    return dp[l][m][n]


def findMaxFormPruned(strs: List[str], m: int, n: int) -> int:
    """
    提前剪枝优化
    """
    if not strs:
        return 0
    
    # 预处理每个字符串的0和1的个数，并过滤掉不可能被选中的字符串
    valid_counts = []
    
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        
        # 剪枝：如果字符串的0或1的个数超过给定的限制，则无法选择该字符串
        if zeros <= m and ones <= n:
            valid_counts.append([zeros, ones])
    
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历有效的字符串
    for zeros, ones in valid_counts:
        # 逆序遍历两个维度
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]


def findMaxFormScrolling(strs: List[str], m: int, n: int) -> int:
    """
    使用滚动数组优化空间复杂度
    注意：在这个问题中，滚动数组的优化已经在基本实现中完成（使用二维数组）
    这个方法只是为了展示如何进一步优化（尽管在这个问题中意义不大）
    """
    if not strs:
        return 0
    
    # 创建二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        
        # 逆序遍历两个维度（这已经是滚动数组的思想）
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]


def findMaxFormDFS(strs: List[str], m: int, n: int) -> int:
    """
    递归+记忆化搜索实现
    注意：由于这个问题的参数范围较大，递归+记忆化搜索可能会超时
    这里仅作为一种实现方式展示
    """
    if not strs:
        return 0
    
    # 预处理每个字符串的0和1的个数
    counts = []
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append([zeros, ones])
    
    # 使用lru_cache进行记忆化
    @lru_cache(maxsize=None)
    def dfs(index, m0, n1):
        """
        递归辅助函数
        
        Args:
            index: 当前考虑的字符串索引
            m0: 剩余可用的0的个数
            n1: 剩余可用的1的个数
            
        Returns:
            满足条件的最大子集长度
        """
        # 基础情况：如果已经考虑完所有字符串，返回0
        if index < 0:
            return 0
        
        # 不选择当前字符串
        not_choose = dfs(index - 1, m0, n1)
        
        # 选择当前字符串（如果可以的话）
        choose = 0
        zeros, ones = counts[index]
        
        if zeros <= m0 and ones <= n1:
            choose = 1 + dfs(index - 1, m0 - zeros, n1 - ones)
        
        # 返回最大值
        return max(not_choose, choose)
    
    # 调用递归函数
    return dfs(len(strs) - 1, m, n)


# 测试函数
def test_findMaxForm():
    # 测试用例1
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1, n1 = 5, 3
    print(f"测试用例1结果: {findMaxForm(strs1, m1, n1)}")  # 预期输出: 4
    
    # 测试用例2
    strs2 = ["10", "0", "1"]
    m2, n2 = 1, 1
    print(f"测试用例2结果: {findMaxForm(strs2, m2, n2)}")  # 预期输出: 2
    
    # 测试用例3
    strs3 = ["10", "0001", "111001", "1", "0"]
    m3, n3 = 4, 3
    print(f"测试用例3结果: {findMaxForm(strs3, m3, n3)}")  # 预期输出: 3


# 执行测试
if __name__ == "__main__":
    test_findMaxForm()