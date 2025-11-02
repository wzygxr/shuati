# 玉米田 (Corn Fields)
# 题目来源: POJ 3254 Corn Fields
# 题目链接: http://poj.org/problem?id=3254
# 题目描述:
# Farmer John 放牧cow，有些草地上的草是不能吃的，用0表示，然后规定两头牛不能相邻放牧。
# 问题要求计算在给定的网格中，有多少种合法的放牧方案。
#
# 解题思路:
# 这是一道经典的状态压缩DP问题。我们可以按行进行状态压缩，用二进制位表示每一行的放牧状态。
# 对于每一行，我们需要考虑：
# 1. 当前行的地形是否允许在某个位置放牧（草地为1，不能放牧的为0）
# 2. 当前行的放牧状态是否合法（相邻位置不能同时放牧）
# 3. 当前行与前一行的放牧状态是否冲突（上下相邻位置不能同时放牧）
#
# 状态定义:
# dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
#
# 状态转移:
# 对于每一行，我们枚举所有可能的合法状态，然后检查与前一行是否冲突
#
# 时间复杂度: O(m * 2^(2*n)) 其中m是行数，n是列数
# 空间复杂度: O(2^n)
#
# 补充题目1: 格雷编码 (Gray Code)
# 题目来源: LeetCode 89. Gray Code
# 题目链接: https://leetcode.cn/problems/gray-code/
# 题目描述:
# 格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
# 给定一个代表编码总位数的非负整数n，打印其格雷编码序列。
# 解题思路:
# 1. 观察格雷编码的生成规律
# 2. 递推关系: G(i) = i XOR (i >> 1)
# 3. 也可以使用动态规划方法，从n-1位的格雷编码生成n位的格雷编码
# 时间复杂度: O(2^n)
# 空间复杂度: O(1)

# 常量定义
MOD = 1000000007  # 取模常量，防止整数溢出

# POJ 3254 Corn Fields 解法
def corn_fields(m, n, grid):
    """
    计算玉米田问题的解法
    
    Args:
        m (int): 行数
        n (int): 列数
        grid (List[List[int]]): 二维列表，表示网格，1表示可以放牧，0表示不能放牧
    
    Returns:
        int: 合法的放牧方案数
    """
    # 预处理每一行的合法状态
    # valid_states[i] 表示第i行的地形状态，用二进制位表示哪些位置可以放牧
    valid_states = [0] * m
    for i in range(m):
        for j in range(n):
            if grid[i][j] == 1:
                valid_states[i] |= (1 << j)  # 将第j位设为1，表示位置j可以放牧
    
    # 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
    # all_states列表存储所有合法的行状态
    all_states = []
    for i in range(1 << n):  # 枚举所有可能的行状态(0到2^n-1)
        # 检查相邻位置是否冲突
        # (i << 1) 将状态左移一位，与原状态按位与，如果不为0说明有相邻的1
        # (i >> 1) 将状态右移一位，与原状态按位与，如果不为0说明有相邻的1
        if (i & (i << 1)) == 0 and (i & (i >> 1)) == 0:
            all_states.append(i)
    
    # dp[i][mask] 表示处理到第i行，且第i行的放牧状态为mask时的方案数
    # 初始化DP数组，dp[0][0] = 1 表示处理第0行，状态为0（没有放牧）的方案数为1
    dp = [[0] * (1 << n) for _ in range(m + 1)]
    dp[0][0] = 1
    
    # 状态转移过程
    for i in range(1, m + 1):
        # 枚举所有合法的行状态
        for state in all_states:
            # 检查当前状态是否在当前行的合法地形内
            # 如果(state & valid_states[i - 1]) != state，说明state中有某些位置在地形上是不可放牧的
            if (state & valid_states[i - 1]) != state:
                continue
            
            # 检查与前一行是否冲突
            # 枚举前一行的所有可能状态
            for k in range(1 << n):
                # 如果当前行状态state与前一行状态k没有上下相邻（按位与为0），则可以转移
                if (state & k) == 0:  # 上下不相邻
                    dp[i][state] = (dp[i][state] + dp[i - 1][k]) % MOD
    
    # 计算最终结果：将最后一行所有状态的方案数相加
    result = 0
    for i in range(1 << n):
        result = (result + dp[m][i]) % MOD
    return result

# 空间优化版本
def corn_fields_optimized(m, n, grid):
    """
    空间优化版本的玉米田问题解法
    通过滚动数组优化空间复杂度，只使用两个一维数组
    
    Args:
        m (int): 行数
        n (int): 列数
        grid (List[List[int]]): 二维列表，表示网格，1表示可以放牧，0表示不能放牧
    
    Returns:
        int: 合法的放牧方案数
    """
    # 预处理每一行的合法状态
    valid_states = [0] * m
    for i in range(m):
        for j in range(n):
            if grid[i][j] == 1:
                valid_states[i] |= (1 << j)
    
    # 预处理所有可能的行状态（不考虑地形，只考虑相邻位置不冲突）
    all_states = []
    for i in range(1 << n):
        # 检查相邻位置是否冲突
        if (i & (i << 1)) == 0 and (i & (i >> 1)) == 0:
            all_states.append(i)
    
    # 空间优化的DP数组
    # 只需要保存当前行和下一行的状态，使用滚动数组优化空间
    dp = [0] * (1 << n)
    next_dp = [0] * (1 << n)
    dp[0] = 1  # 初始状态
    
    # 状态转移过程
    for i in range(1, m + 1):
        # 初始化next_dp数组
        for j in range(1 << n):
            next_dp[j] = 0
        
        # 枚举所有合法的行状态
        for state in all_states:
            # 检查当前状态是否在当前行的合法地形内
            if (state & valid_states[i - 1]) != state:
                continue
            
            # 检查与前一行是否冲突
            for k in range(1 << n):
                if (state & k) == 0:  # 上下不相邻
                    next_dp[state] = (next_dp[state] + dp[k]) % MOD
        
        # 交换dp数组，将next_dp的值复制到dp中，为下一次迭代做准备
        dp, next_dp = next_dp, dp
    
    # 计算最终结果
    result = 0
    for i in range(1 << n):
        result = (result + dp[i]) % MOD
    return result

# LeetCode 89. Gray Code 解法
def gray_code(n):
    """
    生成格雷编码
    使用数学公式直接生成格雷编码
    
    Args:
        n (int): 编码位数
    
    Returns:
        List[int]: 格雷编码序列
    """
    result = []
    # 格雷编码总数为2^n
    for i in range(1 << n):
        # 格雷编码的数学公式: G(i) = i XOR (i >> 1)
        result.append(i ^ (i >> 1))
    return result

# 动态规划方法生成格雷编码
def gray_code_dp(n):
    """
    使用动态规划方法生成格雷编码
    通过递推方式生成格雷编码
    
    Args:
        n (int): 编码位数
    
    Returns:
        List[int]: 格雷编码序列
    """
    if n == 0:
        return [0]
    
    # dp[i] 表示i位格雷编码序列
    dp = [[] for _ in range(n + 1)]
    dp[0] = [0]

    # 递推生成过程
    for i in range(1, n + 1):
        # 前半部分是i-1位的格雷编码
        dp[i] = dp[i - 1][:]
        # 后半部分是i-1位的格雷编码逆序，再加上2^(i-1)
        for j in range(len(dp[i - 1]) - 1, -1, -1):
            dp[i].append(dp[i - 1][j] | (1 << (i - 1)))
    
    return dp[n]

# 测试方法
if __name__ == "__main__":
    # 测试 POJ 3254 Corn Fields
    grid1 = [
        [1, 1, 1],
        [0, 1, 0],
        [1, 1, 1]
    ]
    print("POJ 3254 Corn Fields 测试:")
    print("结果:", corn_fields(3, 3, grid1))
    print("优化版结果:", corn_fields_optimized(3, 3, grid1))
    
    # 测试 LeetCode 89. Gray Code
    print("\nLeetCode 89. Gray Code 测试:")
    gray1 = gray_code(2)
    print("n=2:", gray1)
    
    gray2 = gray_code_dp(3)
    print("n=3:", gray2)