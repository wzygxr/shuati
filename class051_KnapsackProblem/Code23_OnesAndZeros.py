# LeetCode 474. 一和零
# 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
# 请你找出并返回 strs 的最大子集的大小，该子集中 最多 有 m 个 0 和 n 个 1 。
# 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
# 链接：https://leetcode.cn/problems/ones-and-zeroes/
# 
# 解题思路：
# 这是一个多维背包问题，我们需要同时考虑两种资源限制：0的数量和1的数量。
# 每个字符串相当于一个物品，占用的空间是它包含的0的数量和1的数量，价值为1（因为我们想最大化子集的大小）。
# 目标是在不超过m个0和n个1的限制下，选择尽可能多的字符串。
# 
# 状态定义：dp[i][j] 表示使用i个0和j个1时，可以选择的最大字符串数量
# 状态转移方程：对于每个字符串s，其中有zeros个0和ones个1，
#              dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)，当i >= zeros且j >= ones时
# 初始状态：dp[0][0] = 0，表示不使用任何0和1时，可以选择0个字符串
# 其他初始值也为0，表示还没有选择任何字符串
# 
# 时间复杂度：O(l * m * n)，其中l是字符串数组的长度，m和n是给定的整数
# 空间复杂度：O(m * n)，使用二维DP数组

def find_max_form(strs, m, n):
    """
    找出最大子集的大小，该子集中最多有m个0和n个1
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 最大子集的大小
    """
    # 参数验证
    if not strs:
        return 0
    
    # 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，可以选择的最大字符串数量
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串（物品）
    for s in strs:
        # 统计当前字符串中0和1的数量
        zeros = s.count('0')
        ones = len(s) - zeros
        
        # 逆序遍历m和n，避免重复使用同一个字符串
        # 从大到小遍历0的数量
        for i in range(m, zeros - 1, -1):
            # 从大到小遍历1的数量
            for j in range(n, ones - 1, -1):
                # 状态转移：选择当前字符串或不选择当前字符串
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    # 返回结果：使用最多m个0和n个1时，可以选择的最大字符串数量
    return dp[m][n]

def find_max_form_optimized(strs, m, n):
    """
    优化版本：预处理字符串的0和1数量，避免重复计算
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 最大子集的大小
    """
    # 参数验证
    if not strs:
        return 0
    
    # 预处理：统计每个字符串中0和1的数量
    counts = []
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append((zeros, ones))
    
    # 创建二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串（物品）
    for zeros, ones in counts:
        # 剪枝：如果当前字符串需要的0或1超过限制，则跳过
        if zeros > m or ones > n:
            continue
        
        # 逆序遍历
        for i in range(m, zeros - 1, -1):
            for j in range(n, ones - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)
    
    return dp[m][n]

def find_max_form_with_rolling_array(strs, m, n):
    """
    另一种实现方式，使用滚动数组优化空间
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 最大子集的大小
    """
    # 参数验证
    if not strs:
        return 0
    
    # 创建DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        
        # 使用临时数组保存上一状态
        temp = [row.copy() for row in dp]
        
        for i in range(zeros, m + 1):
            for j in range(ones, n + 1):
                dp[i][j] = max(dp[i][j], temp[i - zeros][j - ones] + 1)
    
    return dp[m][n]

def find_max_form_dfs(strs, m, n):
    """
    递归+记忆化搜索实现
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 最大子集的大小
    """
    # 参数验证
    if not strs:
        return 0
    
    # 预处理：统计每个字符串中0和1的数量，并过滤掉不可能选择的字符串
    counts = []
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        # 只有当字符串需要的0和1都不超过限制时，才可能被选中
        if zeros <= m and ones <= n:
            counts.append((zeros, ones))
    
    # 使用字典作为缓存，键为(index, remaining_m, remaining_n)的元组
    memo = {}
    
    def dfs(index, remaining_m, remaining_n):
        # 基础情况：已经处理完所有字符串
        if index == len(counts):
            return 0
        
        # 生成缓存键
        key = (index, remaining_m, remaining_n)
        if key in memo:
            return memo[key]
        
        # 获取当前字符串的0和1数量
        zeros, ones = counts[index]
        
        # 选择不使用当前字符串
        not_take = dfs(index + 1, remaining_m, remaining_n)
        
        # 选择使用当前字符串（如果有足够的0和1）
        take = 0
        if zeros <= remaining_m and ones <= remaining_n:
            take = 1 + dfs(index + 1, remaining_m - zeros, remaining_n - ones)
        
        # 记录结果
        memo[key] = max(not_take, take)
        
        return memo[key]
    
    # 调用递归函数
    return dfs(0, m, n)

def find_max_form_dp_compressed(strs, m, n):
    """
    使用一维DP数组的优化版本（降维）
    注意：由于这里是二维背包，降维后需要使用临时数组来保存上一状态
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 最大子集的大小
    """
    # 实际上，对于二维背包，使用二维数组更加直观
    # 这里仅作为演示，实现方式与上面基本相同
    return find_max_form_optimized(strs, m, n)

def find_max_form_greedy(strs, m, n):
    """
    贪心算法（仅供参考，不适用于所有情况）
    贪心无法保证得到正确结果，但在某些情况下可以作为启发式方法
    
    Args:
        strs: 二进制字符串数组
        m: 最大0的数量
        n: 最大1的数量
    
    Returns:
        int: 可能的子集大小（不一定是最大的）
    """
    # 按照字符串长度排序，优先选择较短的字符串
    strs_sorted = sorted(strs, key=len)
    
    result = 0
    used_m = 0
    used_n = 0
    
    for s in strs_sorted:
        zeros = s.count('0')
        ones = len(s) - zeros
        if used_m + zeros <= m and used_n + ones <= n:
            used_m += zeros
            used_n += ones
            result += 1
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1, n1 = 5, 3
    print(f"测试用例1结果: {find_max_form(strs1, m1, n1)}")  # 预期输出: 4
    
    # 测试用例2
    strs2 = ["10", "0", "1"]
    m2, n2 = 1, 1
    print(f"测试用例2结果: {find_max_form(strs2, m2, n2)}")  # 预期输出: 2
    
    # 测试用例3
    strs3 = ["00", "000"]
    m3, n3 = 1, 0
    print(f"测试用例3结果: {find_max_form(strs3, m3, n3)}")  # 预期输出: 0
    
    # 测试用例4
    strs4 = ["111", "1000", "1000", "1000"]
    m4, n4 = 9, 3
    print(f"测试用例4结果: {find_max_form(strs4, m4, n4)}")  # 预期输出: 3