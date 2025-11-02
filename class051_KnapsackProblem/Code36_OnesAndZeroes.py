# LeetCode 474. 一和零
# 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
# 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
# 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
# 链接：https://leetcode.cn/problems/ones-and-zeroes/
# 
# 解题思路：
# 这是一个二维费用的0-1背包问题。每个字符串可以看作是一个物品，它有两个费用：0的数量和1的数量。
# 我们的背包有两个容量限制：最多可以使用m个0和n个1。我们的目标是选择尽可能多的物品（字符串）。
# 
# 状态定义：dp[i][j] 表示使用i个0和j个1时，最多可以选择的字符串数量
# 状态转移方程：dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)，其中zeros和ones是当前字符串的0和1的数量
# 初始状态：dp[0][0] = 0，其他初始化为0
# 
# 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
# 空间复杂度：O(m * n)，使用二维DP数组

def find_max_form(strs: list[str], m: int, n: int) -> int:
    """
    计算最大子集的长度
    
    参数:
        strs: 二进制字符串数组
        m: 最多可以使用的0的数量
        n: 最多可以使用的1的数量
    
    返回:
        最大子集的长度
    """
    if not strs:
        return 0
    
    # 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，最多可以选择的字符串数量
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串
    for s in strs:
        # 统计当前字符串中0和1的数量
        zeros = s.count('0')
        ones = s.count('1')
        
        # 逆序遍历，避免重复使用同一个字符串
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                # 更新状态：不选当前字符串 或 选当前字符串（如果可以的话）
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]

def find_max_form_3d(strs: list[str], m: int, n: int) -> int:
    """
    使用三维DP数组的版本（更直观但空间效率较低）
    
    参数:
        strs: 二进制字符串数组
        m: 最多可以使用的0的数量
        n: 最多可以使用的1的数量
    
    返回:
        最大子集的长度
    """
    if not strs:
        return 0
    
    l = len(strs)
    # 创建三维DP数组，dp[k][i][j]表示前k个字符串中，使用i个0和j个1时，最多可以选择的字符串数量
    dp = [[[0] * (n + 1) for _ in range(m + 1)] for __ in range(l + 1)]
    
    # 遍历每个字符串
    for k in range(1, l + 1):
        s = strs[k - 1]
        zeros = s.count('0')
        ones = s.count('1')
        
        # 遍历0的数量
        for i in range(m + 1):
            # 遍历1的数量
            for j in range(n + 1):
                # 不选第k个字符串
                dp[k][i][j] = dp[k - 1][i][j]
                
                # 选第k个字符串（如果可以的话）
                if i >= zeros and j >= ones:
                    dp[k][i][j] = max(dp[k][i][j], dp[k - 1][i - zeros][j - ones] + 1)
    
    return dp[l][m][n]

def find_max_form_optimized(strs: list[str], m: int, n: int) -> int:
    """
    优化的二维DP版本，将统计0和1的过程提前
    
    参数:
        strs: 二进制字符串数组
        m: 最多可以使用的0的数量
        n: 最多可以使用的1的数量
    
    返回:
        最大子集的长度
    """
    if not strs:
        return 0
    
    # 预先统计所有字符串中0和1的数量
    counts = []
    for s in strs:
        counts.append((s.count('0'), s.count('1')))
    
    # 创建二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串
    for zeros, ones in counts:
        # 逆序遍历
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]

from functools import lru_cache

def find_max_form_recursive(strs: list[str], m: int, n: int) -> int:
    """
    使用递归+记忆化搜索实现
    这个方法对于较大的输入可能会超时，但展示了递归的思路
    
    参数:
        strs: 二进制字符串数组
        m: 最多可以使用的0的数量
        n: 最多可以使用的1的数量
    
    返回:
        最大子集的长度
    """
    if not strs:
        return 0
    
    # 预先统计所有字符串中0和1的数量
    counts = []
    for s in strs:
        counts.append((s.count('0'), s.count('1')))
    
    # 使用lru_cache装饰器进行记忆化
    @lru_cache(maxsize=None)
    def dfs(index: int, zeros_left: int, ones_left: int) -> int:
        """
        递归辅助函数
        
        参数:
            index: 当前处理的字符串索引
            zeros_left: 剩余可用的0的数量
            ones_left: 剩余可用的1的数量
        
        返回:
            最大可以选择的字符串数量
        """
        # 基础情况：处理完所有字符串
        if index == len(counts):
            return 0
        
        # 不选当前字符串
        not_take = dfs(index + 1, zeros_left, ones_left)
        
        # 选当前字符串（如果可以的话）
        take = 0
        zeros, ones = counts[index]
        if zeros_left >= zeros and ones_left >= ones:
            take = 1 + dfs(index + 1, zeros_left - zeros, ones_left - ones)
        
        return max(not_take, take)
    
    return dfs(0, m, n)

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1 = 5
    n1 = 3
    print(f"测试用例1结果: {find_max_form(strs1, m1, n1)}")  # 预期输出: 4
    print(f"三维DP版本: {find_max_form_3d(strs1, m1, n1)}")
    print(f"优化版本: {find_max_form_optimized(strs1, m1, n1)}")
    print(f"递归版本: {find_max_form_recursive(strs1, m1, n1)}")
    
    # 测试用例2
    strs2 = ["10", "0", "1"]
    m2 = 1
    n2 = 1
    print(f"测试用例2结果: {find_max_form(strs2, m2, n2)}")  # 预期输出: 2
    print(f"三维DP版本: {find_max_form_3d(strs2, m2, n2)}")
    print(f"优化版本: {find_max_form_optimized(strs2, m2, n2)}")
    print(f"递归版本: {find_max_form_recursive(strs2, m2, n2)}")
    
    # 测试用例3
    strs3 = ["001", "110", "0000", "0000"]
    m3 = 9
    n3 = 3
    print(f"测试用例3结果: {find_max_form(strs3, m3, n3)}")  # 预期输出: 4