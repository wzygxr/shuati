import copy

# Bellman-Ford算法应用（不是模版）
# k站中转内最便宜的航班
# 有 n 个城市通过一些航班连接。给你一个数组 flights
# 其中 flights[i] = [fromi, toi, pricei]
# 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
# 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k 站中转的路线
# 使得从 src 到 dst 的 价格最便宜 ，并返回该价格。 如果不存在这样的路线，则输出 -1。
# 测试链接 : https://leetcode.cn/problems/cheapest-flights-within-k-stops/

# Bellman-Ford算法用于解决单源最短路径问题，特别适用于存在负权边的图
# 时间复杂度: O(K*E)，其中K是最多中转站数，E是边数
# 空间复杂度: O(N)，其中N是节点数

def findCheapestPrice(n, flights, start, target, k):
    """Bellman-Ford算法
    针对此题改写了松弛逻辑，课上讲了细节
    通过k+1轮松弛操作来限制最多经过k个中转站
    
    Args:
        n: 城市数量
        flights: 航班信息列表，每个元素为[from, to, price]
        start: 起始城市
        target: 目标城市
        k: 最多中转站数
    
    Returns:
        最便宜的价格，如果无法到达则返回-1
    """
    # cur[i]表示从起点到节点i的当前最小花费
    cur = [float('inf')] * n
    # 起点到自身的花费为0
    cur[start] = 0
    
    # 进行k+1轮松弛操作，表示最多经过k个中转站
    for i in range(k + 1):
        # 备份上一轮的结果，防止在同一轮中重复使用更新后的值
        next_arr = copy.copy(cur)
        # 遍历所有航班进行松弛操作
        for edge in flights:
            # edge[0]是起点，edge[1]是终点，edge[2]是价格
            # 如果起点可达，则尝试更新终点的最小花费
            if cur[edge[0]] != float('inf'):
                # 松弛操作：如果通过当前边能够获得更小的花费，则更新
                next_arr[edge[1]] = min(next_arr[edge[1]], cur[edge[0]] + edge[2])
        # 更新cur数组为本轮松弛后的结果
        cur = next_arr
    
    # 如果目标节点仍不可达，返回-1，否则返回最小花费
    return -1 if cur[target] == float('inf') else cur[target]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    n = 3
    flights = [[0,1,100],[1,2,100],[0,2,500]]
    src = 0
    dst = 2
    k = 1
    result = findCheapestPrice(n, flights, src, dst, k)
    print(f"测试用例1结果: {result}")  # 期望输出: 200
    
    # 测试用例2
    n = 3
    flights = [[0,1,100],[1,2,100],[0,2,500]]
    src = 0
    dst = 2
    k = 0
    result = findCheapestPrice(n, flights, src, dst, k)
    print(f"测试用例2结果: {result}")  # 期望输出: 500