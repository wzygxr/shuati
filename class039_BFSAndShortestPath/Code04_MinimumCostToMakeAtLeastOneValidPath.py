# 使网格图至少有一条有效路径的最小代价
# 给你一个 m * n 的网格图 grid 。 grid 中每个格子都有一个数字
# 对应着从该格子出发下一步走的方向。 grid[i][j] 中的数字可能为以下几种情况：
# 1 ，下一步往右走，也就是你会从 grid[i][j] 走到 grid[i][j + 1]
# 2 ，下一步往左走，也就是你会从 grid[i][j] 走到 grid[i][j - 1]
# 3 ，下一步往下走，也就是你会从 grid[i][j] 走到 grid[i + 1][j]
# 4 ，下一步往上走，也就是你会从 grid[i][j] 走到 grid[i - 1][j]
# 注意网格图中可能会有 无效数字 ，因为它们可能指向 grid 以外的区域
# 一开始，你会从最左上角的格子 (0,0) 出发
# 我们定义一条 有效路径 为从格子 (0,0) 出发，每一步都顺着数字对应方向走
# 最终在最右下角的格子 (m - 1, n - 1) 结束的路径
# 有效路径 不需要是最短路径
# 你可以花费1的代价修改一个格子中的数字，但每个格子中的数字 只能修改一次
# 请你返回让网格图至少有一条有效路径的最小代价
# 测试链接 : https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
# 
# 算法思路：
# 这也是一个0-1 BFS问题
# 将网格看作图，每个单元格是一个节点
# 如果按照原有方向移动，边权为0（不需要修改）
# 如果改变方向移动，边权为1（需要修改，花费1的代价）
# 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
# 
# 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数
# 空间复杂度：O(m * n)，用于存储距离数组和队列
# 
# 工程化考量：
# 1. 使用collections.deque作为双端队列
# 2. 使用distance数组记录到每个点的最小修改次数
# 3. 通过比较新路径和已有路径的权重来决定是否更新

from collections import deque

def minCost(grid):
    """
    0-1 BFS解法
    
    Args:
        grid: List[List[int]] - 二维网格，每个格子的数字表示方向
    
    Returns:
        int - 让网格图至少有一条有效路径的最小代价
    """
    # 格子的数值对应的方向:
    # 1 右
    # 2 左
    # 3 下
    # 4 上
    move = [(), (0, 1), (0, -1), (1, 0), (-1, 0)]
    m, n = len(grid), len(grid[0])
    
    # distance[i][j]表示从起点(0,0)到(i,j)的最小修改次数
    distance = [[float('inf')] * n for _ in range(m)]
    
    # 双端队列，用于0-1 BFS
    dq = deque()
    dq.appendleft((0, 0))
    distance[0][0] = 0
    
    while dq:
        # 从队首取出节点
        x, y = dq.popleft()
        
        # 如果到达终点
        if x == m - 1 and y == n - 1:
            return distance[x][y]
        
        # 尝试四个方向
        for i in range(1, 5):
            nx, ny = x + move[i][0], y + move[i][1]
            # 如果当前格子的方向与尝试的方向一致，则不需要修改，权重为0；否则需要修改，权重为1
            weight = 1 if grid[x][y] != i else 0
            
            # 检查边界和是否能找到更优路径
            if 0 <= nx < m and 0 <= ny < n and distance[x][y] + weight < distance[nx][ny]:
                distance[nx][ny] = distance[x][y] + weight
                # 根据权重决定放在队首还是队尾
                if weight == 0:
                    dq.appendleft((nx, ny))
                else:
                    dq.append((nx, ny))
    
    return -1

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    grid1 = [
        [1, 1, 1, 1],
        [2, 2, 2, 2],
        [1, 1, 1, 1],
        [2, 2, 2, 2]
    ]
    print("测试用例1结果:", minCost(grid1))  # 预期输出: 3
    
    # 测试用例2
    grid2 = [
        [1, 1, 3],
        [3, 2, 2],
        [1, 1, 4]
    ]
    print("测试用例2结果:", minCost(grid2))  # 预期输出: 0
    
    # 测试用例3
    grid3 = [
        [1, 2],
        [4, 3]
    ]
    print("测试用例3结果:", minCost(grid3))  # 预期输出: 1