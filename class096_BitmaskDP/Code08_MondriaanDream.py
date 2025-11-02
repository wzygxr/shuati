# Mondriaan's Dream (蒙德里安的梦想)
# 题目来源: POJ 2411 Mondriaan's Dream
# 题目链接: http://poj.org/problem?id=2411
# 题目描述:
# 给定n行m列的矩形，用1×2的砖块填充，问有多少种填充方案。
#
# 解题思路:
# 这是一道经典的轮廓线DP问题，也是状态压缩DP的一种。
# 1. 按格子进行DP，从上到下，从左到右填充
# 2. 用二进制状态表示当前轮廓线上的格子是否已被填充
# 3. dp[i][mask] 表示处理到第i个格子，轮廓线状态为mask时的方案数
# 4. 对于每个格子，有两种选择：横放或竖放砖块
#
# 时间复杂度: O(n*m*2^m)
# 空间复杂度: O(2^m)
#
# 补充题目1: 不要62 (不要62)
# 题目来源: HDU 2089 不要62
# 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=2089
# 题目描述:
# 杭州人称那些傻乎乎粘嗒嗒的人为62（音：laoer）。
# 杭州交通管理局经常会扩充一些的士车牌照，新近出来一个好消息，
# 以后上牌照，不再含有不吉利的数字了，这样就可以消除个别的士司机和乘客的心理障碍，
# 为社会和谐做出贡献。
# 不吉利的数字为所有包含4或62的号码。例如：62315 73418 88914都属于不吉利的号码。
# 问题是：从n到m的所有整数中，有多少个吉利的数字？
# 解题思路:
# 1. 数位DP解法
# 2. dfs(pos, pre, state, limit) 表示处理到第pos位，前一位数字是pre，状态为state，是否有限制
# 3. state表示是否包含不吉利数字的状态
# 时间复杂度: O(log(n) * 10 * 2)
# 空间复杂度: O(log(n) * 10 * 2)

# POJ 2411 Mondriaan's Dream 解法
def mondriaan_dream(n, m):
    """
    计算蒙德里安的梦想问题的解法
    
    Args:
        n (int): 矩形行数
        m (int): 矩形列数
    
    Returns:
        int: 填充方案数
    """
    # 特殊情况：如果n*m是奇数，则无法完全填充
    if (n * m) % 2 == 1:
        return 0
    
    # 交换n和m，确保m<=n，优化时间复杂度
    if m > n:
        n, m = m, n
    
    # dp[mask] 表示当前行的轮廓线状态为mask时的方案数
    dp = [0] * (1 << m)
    next_dp = [0] * (1 << m)
    dp[0] = 1
    
    # 按行进行状态转移
    for i in range(n):
        next_dp = [0] * (1 << m)
        
        # 按列进行状态转移
        for mask in range(1 << m):
            if dp[mask] > 0:
                # 尝试在当前行放置砖块
                dfs_simple(i, 0, mask, 0, dp[mask], next_dp, m)
        
        dp = next_dp
    
    return dp[0]

# DFS辅助函数，用于处理当前行的砖块放置（简洁版）
def dfs_simple(row, col, prev_mask, curr_mask, count, next_dp, m):
    """
    DFS辅助函数，用于处理当前行的砖块放置
    
    Args:
        row (int): 当前行号
        col (int): 当前列号
        prev_mask (int): 前一行的轮廓线状态
        curr_mask (int): 当前行的轮廓线状态
        count (int): 当前状态的方案数
        next_dp (List[int]): 下一行的DP数组
        m (int): 列数
    """
    # 如果处理完当前行
    if col == m:
        next_dp[curr_mask] += count
        return
    
    # 如果当前位置在前一行已经被填充（prev_mask的第col位为1）
    if (prev_mask & (1 << col)) != 0:
        # 当前位置不需要填充，直接处理下一个位置
        dfs_simple(row, col + 1, prev_mask, curr_mask, count, next_dp, m)
    else:
        # 当前位置未被填充，需要放置砖块
        
        # 竖放砖块（占用当前位置和下一行的同一列）
        dfs_simple(row, col + 1, prev_mask, curr_mask | (1 << col), count, next_dp, m)
        
        # 横放砖块（占用当前位置和同一行的下一列），前提是下一列存在且未被填充
        if col + 1 < m and (prev_mask & (1 << (col + 1))) == 0:
            # 横放砖块不需要在当前轮廓线上标记，因为两个位置都被填充了
            dfs_simple(row, col + 2, prev_mask, curr_mask, count, next_dp, m)

# HDU 2089 不要62 解法
def count_lucky_numbers(n, m):
    """
    计算不要62问题的解法
    
    Args:
        n (int): 起始数字
        m (int): 结束数字
    
    Returns:
        int: [n, m]范围内吉利数字的个数
    """
    # 计算[0, m]范围内的吉利数字个数
    count2 = count_lucky_numbers_helper(str(m))
    # 计算[0, n-1]范围内的吉利数字个数
    count1 = count_lucky_numbers_helper(str(n - 1))
    
    return count2 - count1

# 数位DP辅助函数
def count_lucky_numbers_helper(num_str):
    """
    数位DP辅助函数
    
    Args:
        num_str (str): 数字字符串
    
    Returns:
        int: [0, num]范围内吉利数字的个数
    """
    length = len(num_str)
    if length == 0:
        return 0
    
    # dp[pos][has62][has4][limit] 表示处理到第pos位，是否包含62，是否包含4，是否有限制时的方案数
    # 初始化为-1，表示未计算
    dp = [[[[-1 for _ in range(2)] for _ in range(2)] for _ in range(2)] for _ in range(length)]
    
    return dfs_lucky(0, 0, 0, True, num_str, dp)

# DFS辅助函数，用于数位DP
def dfs_lucky(pos, has62, has4, limit, num_str, dp):
    """
    DFS辅助函数，用于数位DP
    
    Args:
        pos (int): 当前处理的位数
        has62 (int): 是否包含62
        has4 (int): 是否包含4
        limit (bool): 是否有限制
        num_str (str): 数字字符串
        dp (List[List[List[List[int]]]]): 记忆化数组
    
    Returns:
        int: 方案数
    """
    # 如果处理完所有位数
    if pos == len(num_str):
        # 如果不包含不吉利数字，返回1；否则返回0
        return 1 if (has62 == 0 and has4 == 0) else 0
    
    # 记忆化搜索
    # 如果没有限制且已经计算过，直接返回结果
    if not limit and dp[pos][has62][has4][1 if limit else 0] != -1:
        return dp[pos][has62][has4][1 if limit else 0]
    
    # 确定当前位可以填的最大数字
    up = int(num_str[pos]) if limit else 9
    res = 0
    
    # 枚举当前位可以填的数字
    for i in range(up + 1):
        # 如果当前数字是4，标记包含4
        new_has4 = 1 if (has4 == 1 or i == 4) else 0
        # 如果前一位是6且当前位是2，标记包含62
        new_has62 = 1 if (has62 == 1 or (pos > 0 and num_str[pos - 1] == '6' and i == 2)) else 0
        
        # 递归处理下一位
        # limit and (i == up) 表示下一位是否有限制
        res += dfs_lucky(pos + 1, new_has62, new_has4, limit and (i == up), num_str, dp)
    
    # 记忆化存储
    # 只有在没有限制时才存储，因为有限制的状态每次可能不同
    if not limit:
        dp[pos][has62][has4][1 if limit else 0] = res
    
    return res

# 测试方法
if __name__ == "__main__":
    # 测试 POJ 2411 Mondriaan's Dream
    print("POJ 2411 Mondriaan's Dream 测试:")
    print("2×2:", mondriaan_dream(2, 2))
    print("3×2:", mondriaan_dream(3, 2))
    print("4×2:", mondriaan_dream(4, 2))
    
    # 测试 HDU 2089 不要62
    print("\nHDU 2089 不要62 测试:")
    print("[1, 100]范围内的吉利数字个数:", count_lucky_numbers(1, 100))
    print("[1, 1000]范围内的吉利数字个数:", count_lucky_numbers(1, 1000))