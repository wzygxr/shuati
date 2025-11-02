# 二维接雨水
# 给你一个 m * n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度
# 请计算图中形状最多能接多少体积的雨水。
# 测试链接 : https://leetcode.cn/problems/trapping-rain-water-ii/
# 
# 算法思路：
# 这是一个使用优先队列的BFS问题
# 从边界开始，因为边界无法存储雨水
# 使用优先队列（最小堆）维护当前所有边界点中高度最低的点
# 每次取出高度最低的点，检查其相邻点
# 如果相邻点未访问过，计算该点能存储的雨水量
# 雨水量 = max(当前点高度, 相邻点高度) - 相邻点实际高度
# 将相邻点加入优先队列，高度为max(当前点高度, 相邻点高度)
# 
# 时间复杂度：O(m * n * log(m * n))，其中m和n分别是矩阵的行数和列数
# 空间复杂度：O(m * n)，用于存储访问状态和优先队列
# 
# 工程化考量：
# 1. 使用heapq作为优先队列（最小堆）
# 2. 使用visited数组记录访问状态
# 3. 从边界开始处理，确保正确计算雨水量

import heapq

def trapRainWater(height):
    """
    使用优先队列的BFS解法
    
    Args:
        height: List[List[int]] - 二维高度图
    
    Returns:
        int - 最多能接的雨水体积
    """
    if not height or not height[0]:
        return 0
    
    # 四个方向的移动：上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    n, m = len(height), len(height[0])
    
    # 优先队列，按高度排序，存储(高度, 行, 列)
    # 高度是指该点能保持的最高水位
    heap = []
    visited = [[False] * m for _ in range(n)]
    
    # 将边界点加入优先队列
    for i in range(n):
        for j in range(m):
            # 边界点
            if i == 0 or i == n - 1 or j == 0 or j == m - 1:
                heapq.heappush(heap, (height[i][j], i, j))
                visited[i][j] = True
    
    ans = 0
    while heap:
        # 取出高度最低的点
        h, r, c = heapq.heappop(heap)
        
        # 累加雨水量
        ans += h - height[r][c]
        
        # 检查四个方向的相邻点
        for dr, dc in move:
            nr, nc = r + dr, c + dc
            # 检查边界和是否已访问
            if 0 <= nr < n and 0 <= nc < m and not visited[nr][nc]:
                # 新点的水位线是max(当前点水位线, 新点高度)
                new_height = max(height[nr][nc], h)
                heapq.heappush(heap, (new_height, nr, nc))
                visited[nr][nc] = True
    
    return ans

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    height1 = [
        [1, 4, 3, 1, 3, 2],
        [3, 2, 1, 3, 2, 4],
        [2, 3, 3, 2, 3, 1]
    ]
    print("测试用例1结果:", trapRainWater(height1))  # 预期输出: 4
    
    # 测试用例2
    height2 = [
        [3, 3, 3, 3, 3],
        [3, 2, 2, 2, 3],
        [3, 2, 1, 2, 3],
        [3, 2, 2, 2, 3],
        [3, 3, 3, 3, 3]
    ]
    print("测试用例2结果:", trapRainWater(height2))  # 预期输出: 10