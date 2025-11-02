# LeetCode 743. 网络延迟时间
# 题目链接: https://leetcode.cn/problems/network-delay-time/
# 题目描述: 有 n 个网络节点，标记为 1 到 n。
# 给你一个列表 times，表示信号经过有向边的传递时间。times[i] = (ui, vi, wi)，
# 其中 ui 是源节点，vi 是目标节点，wi 是一个信号从源节点传递到目标节点的时间。
# 现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？
# 如果不能使所有节点收到信号，返回 -1。
#
# 解题思路:
# 这道题可以使用Bellman-Ford算法来解决。我们需要计算从节点K到所有其他节点的最短路径。
# 如果存在节点无法从K到达，则返回-1。否则返回所有最短路径中的最大值。
#
# 时间复杂度: O(N*E)，其中N是节点数，E是边数
# 空间复杂度: O(N)

def networkDelayTime(times, n, k):
    """
    使用Bellman-Ford算法求解网络延迟时间
    
    Args:
        times: 边的列表，每个元素为[u, v, w]
        n: 节点数量
        k: 起始节点
    
    Returns:
        整数，网络延迟时间，如果无法到达所有节点则返回-1
    """
    # 初始化距离数组，表示从节点k到其他节点的距离
    distance = [float('inf')] * (n + 1)
    distance[k] = 0  # 起点到自身的距离为0
    
    # 进行n-1轮松弛操作
    for i in range(1, n):
        # 遍历所有边进行松弛操作
        for edge in times:
            u, v, w = edge  # u是起点，v是终点，w是权重
            
            # 如果起点可达，则尝试更新终点的最短距离
            if distance[u] != float('inf') and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
    
    # 检查是否存在无法到达的节点
    maxDistance = 0
    for i in range(1, n + 1):
        if distance[i] == float('inf'):
            return -1  # 存在无法到达的节点
        maxDistance = max(maxDistance, distance[i])
    
    return maxDistance

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    times1 = [[2,1,1],[2,3,1],[3,4,1]]
    n1 = 4
    k1 = 2
    print("测试用例1结果:", networkDelayTime(times1, n1, k1))  # 期望输出: 2
    
    # 测试用例2
    times2 = [[1,2,1]]
    n2 = 2
    k2 = 1
    print("测试用例2结果:", networkDelayTime(times2, n2, k2))  # 期望输出: 1
    
    # 测试用例3
    times3 = [[1,2,1]]
    n3 = 2
    k3 = 2
    print("测试用例3结果:", networkDelayTime(times3, n3, k3))  # 期望输出: -1