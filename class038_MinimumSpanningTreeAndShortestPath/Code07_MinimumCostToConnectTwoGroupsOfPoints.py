# LeetCode 1595. Minimum Cost to Connect Two Groups of Points
# 题目链接: https://leetcode.cn/problems/minimum-cost-to-connect-two-groups-of-points/
# 
# 题目描述:
# 给你两组点，其中第一组中有size1个点，第二组中有size2个点，且size1 <= size2。
# 任意两点间的连接费用定义为这两点坐标的曼哈顿距离。
# 我们需要把所有第一组的点与第二组的点连接起来，使得：
# 1. 每个第一组的点必须至少连接到一个第二组的点
# 2. 每个第二组的点可以连接到任意数量的第一组的点
# 3. 总连接费用最小
#
# 解题思路:
# 这是一个最小生成树的变种问题，但更适合用状态压缩动态规划来解决。
# 我们可以将问题转化为：选择一些边，使得所有第一组的点都被覆盖，同时尽可能覆盖第二组的点，
# 最后可能需要添加一些边来连接未被覆盖的第二组的点。
#
# 时间复杂度: O(size1 * 2^size2 * size2)，其中size1是第一组的点数量，size2是第二组的点数量
# 空间复杂度: O(size1 * 2^size2)
# 是否为最优解: 对于给定的约束条件，这是一个有效的解法，但对于较大的size2可能需要其他优化方法

def minCost(connectCost):
    # connectCost[i][j] 表示第一组第i个点连接到第二组第j个点的费用
    size1 = len(connectCost)
    size2 = len(connectCost[0]) if size1 > 0 else 0
    
    # 预处理第二组每个点连接到第一组点的最小费用
    min_cost_group2 = [min(col) for col in zip(*connectCost)]
    
    # dp[i][mask] 表示处理了第一组的前i个点，且第二组中已连接的点集合为mask时的最小费用
    # 其中mask的第j位为1表示第二组的第j个点已经被连接
    dp = [[float('inf')] * (1 << size2) for _ in range(size1 + 1)]
    dp[0][0] = 0  # 初始状态：没有处理任何点，费用为0
    
    for i in range(size1):
        for mask in range(1 << size2):
            if dp[i][mask] == float('inf'):
                continue
            
            # 尝试将第一组的第i个点连接到第二组的每个点j
            for j in range(size2):
                new_mask = mask | (1 << j)
                # 更新费用
                dp[i + 1][new_mask] = min(dp[i + 1][new_mask], dp[i][mask] + connectCost[i][j])
    
    # 最后需要确保所有第二组的未连接点都被连接，每个未连接的点取连接到第一组的最小费用
    result = float('inf')
    for mask in range(1 << size2):
        if dp[size1][mask] == float('inf'):
            continue
        
        # 计算需要补充连接的第二组点的最小费用
        additional_cost = 0
        for j in range(size2):
            if not (mask & (1 << j)):
                additional_cost += min_cost_group2[j]
        
        result = min(result, dp[size1][mask] + additional_cost)
    
    return result

# 测试用例
def test():
    # 测试用例1
    connectCost1 = [[15, 96], [36, 2]]
    print(f"Test 1: {minCost(connectCost1)}")  # 预期输出: 17
    
    # 测试用例2
    connectCost2 = [[1, 3, 5], [4, 1, 1], [1, 5, 3]]
    print(f"Test 2: {minCost(connectCost2)}")  # 预期输出: 4
    
    # 测试用例3
    connectCost3 = [[2, 5, 1], [3, 4, 7], [8, 1, 2], [6, 2, 4], [3, 8, 8]]
    print(f"Test 3: {minCost(connectCost3)}")  # 预期输出: 10

if __name__ == "__main__":
    test()