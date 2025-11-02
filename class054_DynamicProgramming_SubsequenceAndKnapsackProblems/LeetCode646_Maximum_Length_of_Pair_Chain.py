# LeetCode 646. 最长数对链
# 给出 n 个数对。 在每一个数对中，第一个数字总是比第二个数字小。
# 现在，我们定义一种跟随关系，当且仅当 b < c 时，数对(c, d) 才可以跟在 (a, b) 后面。
# 我们用这种形式来构造一个数对链。
# 给定一个数对集合，找出能够形成的最长数对链的长度。
# 你不需要用到所有的数对，你可以以任何顺序选择其中的一些数对来构造。
# 测试链接 : https://leetcode.cn/problems/maximum-length-of-pair-chain/

"""
算法详解：最长数对链（LeetCode 646）

问题描述：
给出 n 个数对。 在每一个数对中，第一个数字总是比第二个数字小。
现在，我们定义一种跟随关系，当且仅当 b < c 时，数对(c, d) 才可以跟在 (a, b) 后面。
我们用这种形式来构造一个数对链。
给定一个数对集合，找出能够形成的最长数对链的长度。
你不需要用到所有的数对，你可以以任何顺序选择其中的一些数对来构造。

算法思路：
这是一个类似LIS的贪心问题，可以使用贪心算法解决。
1. 按照数对的第二个元素升序排序
2. 贪心地选择数对，每次选择第二个元素最小且能满足条件的数对

时间复杂度分析：
1. 排序：O(n log n)
2. 贪心选择：O(n)
3. 总体时间复杂度：O(n log n)

空间复杂度分析：
1. 排序辅助数组：O(n)
2. 总体空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入数组是否为空
2. 边界处理：处理空数组和单元素数组的情况
3. 贪心策略正确性：按照第二个元素排序保证贪心选择的正确性

极端场景验证：
1. 输入数组为空的情况
2. 输入数组只有一个元素的情况
3. 所有数对第二个元素相同的情况
4. 数对随机分布的情况
"""

def findLongestChain(pairs):
    """
    计算最长数对链的长度
    
    Args:
        pairs (List[List[int]]): 数对列表
    
    Returns:
        int: 最长数对链的长度
    """
    # 异常处理：检查输入数组是否为空
    if not pairs:
        return 0
    
    n = len(pairs)
    
    # 特殊情况：只有一个数对
    if n == 1:
        return 1
    
    # 按照数对的第二个元素升序排序
    pairs.sort(key=lambda x: x[1])
    
    # 贪心地选择数对
    count = 1  # 至少选择第一个数对
    end = pairs[0][1]  # 当前链的末尾元素
    
    # 遍历排序后的数对
    for i in range(1, n):
        # 如果当前数对的第一个元素大于链末尾元素
        if pairs[i][0] > end:
            count += 1  # 选择当前数对
            end = pairs[i][1]  # 更新链末尾元素
    
    return count

# 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
def findLongestChainDP(pairs):
    """
    使用动态规划计算最长数对链的长度
    
    Args:
        pairs (List[List[int]]): 数对列表
    
    Returns:
        int: 最长数对链的长度
    """
    # 异常处理：检查输入数组是否为空
    if not pairs:
        return 0
    
    n = len(pairs)
    
    # 特殊情况：只有一个数对
    if n == 1:
        return 1
    
    # 按照数对的第一个元素升序排序
    pairs.sort(key=lambda x: x[0])
    
    # dp[i] 表示以pairs[i]结尾的最长数对链长度
    dp = [1] * n
    
    # 记录最长长度
    max_len = 1
    
    # 填充dp数组
    for i in range(1, n):
        for j in range(i):
            # 如果pairs[j]可以连接到pairs[i]
            if pairs[j][1] < pairs[i][0]:
                dp[i] = max(dp[i], dp[j] + 1)
        # 更新最长长度
        max_len = max(max_len, dp[i])
    
    return max_len

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    pairs1 = [[1,2],[2,3],[3,4]]
    print(f"Test 1 (Greedy method): {findLongestChain(pairs1)}")
    print(f"Test 1 (DP method): {findLongestChainDP(pairs1)}")
    # 期望输出: 2 ([1,2] -> [3,4])
    
    # 测试用例2
    pairs2 = [[1,2],[7,8],[4,5]]
    print(f"Test 2 (Greedy method): {findLongestChain(pairs2)}")
    print(f"Test 2 (DP method): {findLongestChainDP(pairs2)}")
    # 期望输出: 3 ([1,2] -> [4,5] -> [7,8])
    
    # 测试用例3
    pairs3 = [[1,2]]
    print(f"Test 3 (Greedy method): {findLongestChain(pairs3)}")
    print(f"Test 3 (DP method): {findLongestChainDP(pairs3)}")
    # 期望输出: 1
    
    # 测试用例4
    pairs4 = []
    print(f"Test 4 (Greedy method): {findLongestChain(pairs4)}")
    print(f"Test 4 (DP method): {findLongestChainDP(pairs4)}")
    # 期望输出: 0
    
    # 测试用例5
    pairs5 = [[-10,-8],[8,9],[-5,0],[6,10],[-6,-4],[1,7],[9,10],[-4,7]]
    print(f"Test 5 (Greedy method): {findLongestChain(pairs5)}")
    print(f"Test 5 (DP method): {findLongestChainDP(pairs5)}")
    # 期望输出: 4