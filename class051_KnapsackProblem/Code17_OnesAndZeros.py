# LeetCode 474. 一和零
# 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
# 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
# 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
# 链接：https://leetcode.cn/problems/ones-and-zeroes/
# 
# 解题思路：
# 这是一个二维费用的01背包问题。
# 每个字符串可以看作一个物品，它有两个「费用」：0的数量和1的数量
# 背包的容量是两个维度的：最多可以装m个0和n个1
# 每个物品的「价值」都是1（因为我们要最大化子集的长度）
# 
# 状态定义：dp[i][j] 表示最多使用i个0和j个1时能组成的最大子集长度
# 状态转移方程：dp[i][j] = max(dp[i][j], dp[i-zeroCount][j-oneCount] + 1)
# 其中zeroCount是当前字符串中0的数量，oneCount是当前字符串中1的数量
# 
# 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
# 空间复杂度：O(m * n)，使用二维DP数组

def find_max_form(strs, m, n):
    """
    计算最多使用m个0和n个1时能组成的最大子集长度
    
    解题思路：
    这是一个二维费用的01背包问题。
    每个字符串可以看作一个物品，它有两个「费用」：0的数量和1的数量
    背包的容量是两个维度的：最多可以装m个0和n个1
    每个物品的「价值」都是1（因为我们要最大化子集的长度）
    
    Args:
        strs: 二进制字符串数组
        m: 最多可以使用的0的个数
        n: 最多可以使用的1的个数
    
    Returns:
        int: 最大子集长度
    """
    # 参数验证
    if not strs:
        return 0
    
    # 创建二维DP数组，dp[i][j]表示最多使用i个0和j个1时能组成的最大子集长度
    # 这是一个二维费用的01背包问题
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串（物品）
    for s in strs:
        # 计算当前字符串中0和1的数量
        # 这相当于获取当前物品的两个费用属性
        zero_count = s.count('0')
        one_count = len(s) - zero_count
        
        # 二维01背包，需要倒序遍历两个维度，避免重复计算
        # i表示当前可用的0的个数，j表示当前可用的1的个数
        # 倒序遍历确保每个物品只使用一次
        for i in range(m, zero_count - 1, -1):
            for j in range(n, one_count - 1, -1):
                # 状态转移：选择当前字符串或不选择当前字符串
                # dp[i][j] = max(不选择当前字符串, 选择当前字符串)
                # 不选择当前字符串：dp[i][j]（保持原值）
                # 选择当前字符串：dp[i - zero_count][j - one_count] + 1（前一个状态+1）
                dp[i][j] = max(dp[i][j], dp[i - zero_count][j - one_count] + 1)
    
    # 返回最多使用m个0和n个1时能组成的最大子集长度
    return dp[m][n]

def find_max_form_optimized(strs, m, n):
    """
    优化版本：预处理计算每个字符串的0和1数量，减少重复计算
    
    Args:
        strs: 二进制字符串数组
        m: 最多可以使用的0的个数
        n: 最多可以使用的1的个数
    
    Returns:
        int: 最大子集长度
    """
    # 参数验证
    if not strs:
        return 0
    
    # 预处理：计算每个字符串中0和1的数量
    # 这样可以避免在动态规划过程中重复计算
    counts = []
    for s in strs:
        zero_count = s.count('0')
        one_count = len(s) - zero_count
        counts.append((zero_count, one_count))
    
    # 创建二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个字符串（物品）
    for zero_count, one_count in counts:
        # 剪枝：如果当前字符串的0或1数量超过背包容量，直接跳过
        # 因为当前字符串本身就无法放入背包
        if zero_count > m or one_count > n:
            continue
        
        # 二维01背包，倒序遍历
        for i in range(m, zero_count - 1, -1):
            for j in range(n, one_count - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zero_count][j - one_count] + 1)
    
    return dp[m][n]

def find_max_form_further_optimized(strs, m, n):
    """
    进一步优化：使用滚动数组（虽然这里已经是二维，但可以看作是特殊的滚动数组）
    并添加更多剪枝条件
    
    Args:
        strs: 二进制字符串数组
        m: 最多可以使用的0的个数
        n: 最多可以使用的1的个数
    
    Returns:
        int: 最大子集长度
    """
    # 参数验证
    if not strs or (m == 0 and n == 0):
        return 0
    
    # 预处理并过滤不符合条件的字符串
    # 只保留那些至少有一个维度不超过背包容量的字符串
    valid_counts = []
    
    for s in strs:
        zero_count = s.count('0')
        one_count = len(s) - zero_count
        
        # 剪枝：如果当前字符串的0和1数量都超过背包容量，直接跳过
        # 修改条件：只要至少有一个维度不超过背包容量就可以考虑
        if zero_count <= m or one_count <= n:
            valid_counts.append((zero_count, one_count))
    
    # 创建二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 遍历每个有效的字符串
    for zero_count, one_count in valid_counts:
        # 二维01背包，倒序遍历
        for i in range(m, zero_count - 1, -1):
            for j in range(n, one_count - 1, -1):
                dp[i][j] = max(dp[i][j], dp[i - zero_count][j - one_count] + 1)
    
    return dp[m][n]

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
    strs3 = ["00", "01", "11", "10"]
    m3, n3 = 2, 2
    print(f"测试用例3结果: {find_max_form(strs3, m3, n3)}")  # 预期输出: 2

'''
示例:
输入: strs = ["10", "0001", "111001", "1", "0"], m = 5, n = 3
输出: 4
解释: 最多有5个0和3个1的子集是{"10","0001","1","0"}，因此答案是4。

输入: strs = ["10", "0", "1"], m = 1, n = 1
输出: 2
解释: 最多有1个0和1个1的子集是{"0", "1"}，因此答案是2。

时间复杂度: O(l * m * n)
  - 外层循环遍历所有字符串：O(l)
  - 中层循环遍历m：O(m)
  - 内层循环遍历n：O(n)
空间复杂度: O(m * n)
  - 二维DP数组的空间消耗
'''