# LeetCode 787. Cheapest Flights Within K Stops
# 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
# 
# 题目描述:
# 有n个城市通过一些航班连接。给你一个数组flights，其中flights[i] = [from_i, to_i, price_i]，表示从from_i到to_i的航班，价格为price_i。
# 现在给定所有的城市和航班，以及出发城市src和目的地dst，你的任务是找到从src到dst最多经过k站中转的最便宜的价格。如果没有这样的路线，则返回-1。
#
# 解题思路:
# 这是一个带限制条件的单源最短路径问题，可以使用以下方法解决：
# 1. 广度优先搜索(BFS) + 动态规划：维护一个距离数组，记录到达每个城市的最短距离，同时记录中转次数
# 2. Bellman-Ford算法：对图进行k+1次松弛操作
# 3. Dijkstra算法的变种：使用优先队列，但优先级考虑距离和中转次数
#
# 这里我们使用BFS + 动态规划的方法，因为有中转次数的限制
#
# 时间复杂度: O(k * E)，其中E是边数，k是最大中转次数
# 空间复杂度: O(V)，其中V是顶点数
# 是否为最优解: 是，当有中转次数限制时，这种方法效率较高

from collections import defaultdict, deque

def findCheapestPrice(n, flights, src, dst, k):
    # 构建邻接表表示的图
    graph = defaultdict(list)
    for u, v, w in flights:
        graph[u].append((v, w))
    
    # 初始化距离数组，记录到达每个城市的最低价格
    prices = [float('inf')] * n
    prices[src] = 0
    
    # 使用队列进行BFS，每个元素是(城市, 当前价格, 已中转次数)
    queue = deque([(src, 0, 0)])
    
    while queue:
        current, current_price, stops = queue.popleft()
        
        # 如果已经到达目的地或者中转次数超过k，跳过
        if current == dst or stops > k:
            continue
        
        # 遍历所有邻居
        for neighbor, price in graph[current]:
            # 只有当新的价格更便宜时，才更新并加入队列
            if prices[neighbor] > current_price + price:
                prices[neighbor] = current_price + price
                queue.append((neighbor, prices[neighbor], stops + 1))
    
    # 如果目的地无法到达，返回-1
    return prices[dst] if prices[dst] != float('inf') else -1

# 另一种实现方式：使用Bellman-Ford算法
def findCheapestPrice_bellman_ford(n, flights, src, dst, k):
    # 初始化距离数组
    prices = [float('inf')] * n
    prices[src] = 0
    
    # 执行k+1次松弛操作（最多可以有k次中转，所以最多可以乘坐k+1次航班）
    for i in range(k + 1):
        # 创建临时数组，避免在一次迭代中多次更新
        temp_prices = prices.copy()
        
        for u, v, w in flights:
            if prices[u] != float('inf') and temp_prices[v] > prices[u] + w:
                temp_prices[v] = prices[u] + w
        
        prices = temp_prices
    
    return prices[dst] if prices[dst] != float('inf') else -1

# 测试用例
def test():
    # 测试用例1
    n1 = 3
    flights1 = [[0, 1, 100], [1, 2, 100], [0, 2, 500]]
    src1, dst1, k1 = 0, 2, 1
    print(f"Test 1 (BFS): {findCheapestPrice(n1, flights1, src1, dst1, k1)}")  # 预期输出: 200
    print(f"Test 1 (Bellman-Ford): {findCheapestPrice_bellman_ford(n1, flights1, src1, dst1, k1)}")  # 预期输出: 200
    
    # 测试用例2
    n2 = 3
    flights2 = [[0, 1, 100], [1, 2, 100], [0, 2, 500]]
    src2, dst2, k2 = 0, 2, 0
    print(f"Test 2 (BFS): {findCheapestPrice(n2, flights2, src2, dst2, k2)}")  # 预期输出: 500
    print(f"Test 2 (Bellman-Ford): {findCheapestPrice_bellman_ford(n2, flights2, src2, dst2, k2)}")  # 预期输出: 500
    
    # 测试用例3 - 无法到达
    n3 = 4
    flights3 = [[0, 1, 1], [0, 2, 5], [1, 2, 1], [2, 3, 1]]
    src3, dst3, k3 = 0, 3, 1
    print(f"Test 3 (BFS): {findCheapestPrice(n3, flights3, src3, dst3, k3)}")  # 预期输出: 6
    print(f"Test 3 (Bellman-Ford): {findCheapestPrice_bellman_ford(n3, flights3, src3, dst3, k3)}")  # 预期输出: 6

if __name__ == "__main__":
    test()