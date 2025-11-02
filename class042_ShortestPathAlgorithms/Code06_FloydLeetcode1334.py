# LeetCode 1334. 阈值距离内邻居最少的城市
# 题目链接: https://leetcode.cn/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
# 题目描述: 有 n 个城市，按从 0 到 n-1 编号。给你一个边数组 edges，其中 edges[i] = [fromi, toi, weighti] 
# 代表 fromi 和 toi 两个城市之间的双向加权边，距离阈值是一个整数 distanceThreshold。
# 返回在路径距离限制为 distanceThreshold 以内可到达城市最少的城市。如果有多个这样的城市，则返回编号最大的城市。
#
# 解题思路:
# 这道题可以使用Floyd算法来解决。我们需要计算任意两个城市之间的最短距离，
# 然后统计每个城市在距离阈值内能到达的城市数量，最后返回数量最少且编号最大的城市。
#
# 时间复杂度: O(N^3)，其中N是城市数量
# 空间复杂度: O(N^2)

def findTheCity(n, edges, distanceThreshold):
    """
    使用Floyd算法求解阈值距离内邻居最少的城市
    
    Args:
        n: 城市数量
        edges: 边的列表，每个元素为[from, to, weight]
        distanceThreshold: 距离阈值
    
    Returns:
        整数，满足条件的城市编号
    """
    # 初始化距离矩阵
    distance = [[float('inf')] * n for _ in range(n)]
    for i in range(n):
        distance[i][i] = 0
    
    # 根据边的信息初始化距离矩阵
    for edge in edges:
        frm, to, weight = edge
        distance[frm][to] = weight
        distance[to][frm] = weight  # 因为是无向图
    
    # Floyd算法求所有点对之间的最短距离
    floyd(n, distance)
    
    # 统计每个城市在距离阈值内能到达的城市数量
    minCount = n  # 最少城市数量
    result = -1   # 结果城市编号
    
    for i in range(n):
        count = 0
        for j in range(n):
            if i != j and distance[i][j] <= distanceThreshold:
                count += 1
        
        # 更新结果：城市数量更少，或者城市数量相同但编号更大
        if count < minCount or (count == minCount and i > result):
            minCount = count
            result = i
    
    return result

def floyd(n, distance):
    """
    Floyd算法核心实现
    
    Args:
        n: 节点数量
        distance: 距离矩阵
    """
    # 三层循环：中间节点k，起点i，终点j
    for k in range(n):
        for i in range(n):
            for j in range(n):
                # 注意处理无穷大的情况
                if distance[i][k] != float('inf') and \
                   distance[k][j] != float('inf') and \
                   distance[i][k] + distance[k][j] < distance[i][j]:
                    distance[i][j] = distance[i][k] + distance[k][j]

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    edges1 = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]]
    distanceThreshold1 = 4
    print("测试用例1结果:", findTheCity(n1, edges1, distanceThreshold1))  # 期望输出: 3
    
    # 测试用例2
    n2 = 5
    edges2 = [[0,1,2],[0,4,8],[1,2,3],[1,4,2],[2,3,1],[3,4,1]]
    distanceThreshold2 = 2
    print("测试用例2结果:", findTheCity(n2, edges2, distanceThreshold2))  # 期望输出: 0