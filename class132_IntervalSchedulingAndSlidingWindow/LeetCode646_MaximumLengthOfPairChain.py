"""
LeetCode 646. Maximum Length of Pair Chain

题目描述：
给出 n 个数对 pairs，其中 pairs[i] = [left_i, right_i] 且 left_i < right_i。
现在，我们定义一种"跟随"关系，当且仅当 b < c 时，数对 [c, d] 可以跟在 [a, b] 后面。
我们可以构造一个数对链，链中每两个相邻的数对都满足"跟随"关系。
找出并返回能够形成的最长数对链的长度。

解题思路：
这是一个经典的贪心算法问题，类似于活动选择问题。

算法步骤：
1. 将所有数对按结束值排序
2. 使用贪心策略：总是选择结束值最小的数对
3. 遍历排序后的数对，统计可以组成的最长链长度

贪心策略的正确性：
选择结束值最小的数对可以为后续数对留下更多空间，从而最大化链的长度。

时间复杂度：O(n * log n)
空间复杂度：O(1)

相关题目：
- LeetCode 435. 无重叠区间 (贪心)
- LeetCode 1353. 最多可以参加的会议数目 (贪心)
- LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
"""

def findLongestChain(pairs):
    """
    计算最长数对链的长度
    
    Args:
        pairs: 数对列表，每个元素为 [first, second]
    
    Returns:
        最长数对链的长度
    """
    # 边界情况处理
    if not pairs:
        return 0
    
    n = len(pairs)
    
    # 按结束值排序
    pairs.sort(key=lambda x: x[1])
    
    # 初始化计数器和上一个选择数对的结束值
    count = 1  # 至少可以选择一个数对
    end = pairs[0][1]  # 第一个数对的结束值
    
    # 遍历剩余数对
    for i in range(1, n):
        # 如果当前数对的开始值 > 上一个选择数对的结束值
        # 说明可以连接，可以选择当前数对
        if pairs[i][0] > end:
            count += 1
            end = pairs[i][1]  # 更新结束值
        # 如果不能连接，则跳过当前数对
    
    return count

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    pairs1 = [[1,2],[2,3],[3,4]]
    print("测试用例1:")
    print(f"输入: pairs = {pairs1}")
    print(f"输出: {findLongestChain(pairs1)}")  # 期望输出: 2
    
    # 测试用例2
    pairs2 = [[1,2],[7,8],[4,5]]
    print("\n测试用例2:")
    print(f"输入: pairs = {pairs2}")
    print(f"输出: {findLongestChain(pairs2)}")  # 期望输出: 3
    
    # 测试用例3
    pairs3 = [[1,2]]
    print("\n测试用例3:")
    print(f"输入: pairs = {pairs3}")
    print(f"输出: {findLongestChain(pairs3)}")  # 期望输出: 1
    
    # 测试用例4
    pairs4 = []
    print("\n测试用例4:")
    print(f"输入: pairs = {pairs4}")
    print(f"输出: {findLongestChain(pairs4)}")  # 期望输出: 0