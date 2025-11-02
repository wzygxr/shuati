# LeetCode 1102. Path With Maximum Minimum Value
# 题目链接: https://leetcode.cn/problems/path-with-maximum-minimum-value/
# 
# 题目描述:
# 给你一个由正整数组成的二维网格grid，你需要找到一条从左上角(0,0)到右下角(m-1,n-1)的路径，使得路径上所有数字中的最小值尽可能大。
# 路径可以向四个方向移动：上、下、左、右。
#
# 解题思路:
# 这是一个典型的最大-最小路径问题，可以使用以下几种方法解决：
# 1. 二分答案 + BFS/DFS：二分搜索可能的最小值，然后检查是否存在一条路径上的所有值都不小于该值
# 2. 并查集：将所有点按照值从大到小排序，然后逐步合并相邻的点，直到起点和终点连通
# 3. 最大堆（贪心）：总是选择当前可到达的最大最小值的路径
#
# 这里我们使用最大堆的方法，这类似于最小生成树中的Kruskal算法的思想
#
# 时间复杂度: O(m*n log(m*n))，其中m和n分别是网格的行数和列数
# 空间复杂度: O(m*n)
# 是否为最优解: 是，这是解决此类问题的有效方法之一

import heapq

def maximumMinimumPath(grid):
    if not grid or not grid[0]:
        return 0
    
    m, n = len(grid), len(grid[0])
    # 四个方向：下、右、上、左
    directions = [(1, 0), (0, 1), (-1, 0), (0, -1)]
    # 记录已访问的点
    visited = [[False for _ in range(n)] for _ in range(m)]
    # 最大堆，存储(-value, x, y)，因为Python的heapq是最小堆
    # 使用负值来模拟最大堆
    max_heap = [(-grid[0][0], 0, 0)]
    visited[0][0] = True
    
    # 结果初始化为起点的值
    result = grid[0][0]
    
    while max_heap:
        current_val, x, y = heapq.heappop(max_heap)
        current_val = -current_val  # 转换回正值
        
        # 更新结果为路径上的最小值
        result = min(result, current_val)
        
        # 如果到达终点，返回结果
        if x == m - 1 and y == n - 1:
            return result
        
        # 探索四个方向
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            # 检查边界和是否已访问
            if 0 <= nx < m and 0 <= ny < n and not visited[nx][ny]:
                visited[nx][ny] = True
                heapq.heappush(max_heap, (-grid[nx][ny], nx, ny))
    
    return -1  # 理论上不会到达这里

# 另一种实现方式：并查集
class UnionFind:
    def __init__(self, size):
        self.parent = list(range(size))
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            self.parent[fy] = fx
            return True
        return False

def maximumMinimumPath_uf(grid):
    if not grid or not grid[0]:
        return 0
    
    m, n = len(grid), len(grid[0])
    total_cells = m * n
    uf = UnionFind(total_cells)
    
    # 将所有单元格按照值从大到小排序
    cells = []
    for i in range(m):
        for j in range(n):
            cells.append((-grid[i][j], i, j))  # 使用负值进行小根堆排序
    heapq.heapify(cells)
    
    # 四个方向
    directions = [(1, 0), (0, 1), (-1, 0), (0, -1)]
    # 记录已访问的单元格
    visited = [[False for _ in range(n)] for _ in range(m)]
    
    start = 0  # (0,0)
    end = m * n - 1  # (m-1, n-1)
    
    while cells:
        val, x, y = heapq.heappop(cells)
        val = -val  # 转换回正值
        visited[x][y] = True
        
        # 检查四个方向的邻居
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            if 0 <= nx < m and 0 <= ny < n and visited[nx][ny]:
                # 合并当前单元格和已访问的邻居
                uf.union(x * n + y, nx * n + ny)
        
        # 检查起点和终点是否连通
        if uf.find(start) == uf.find(end):
            return val
    
    return -1

# 测试用例
def test():
    # 测试用例1
    grid1 = [[5, 4, 5], [1, 2, 6], [7, 4, 6]]
    print(f"Test 1 (max heap): {maximumMinimumPath(grid1)}")  # 预期输出: 4
    print(f"Test 1 (union find): {maximumMinimumPath_uf(grid1)}")  # 预期输出: 4
    
    # 测试用例2
    grid2 = [[2, 2, 1, 2, 2, 2], [1, 2, 2, 2, 1, 2]]
    print(f"Test 2 (max heap): {maximumMinimumPath(grid2)}")  # 预期输出: 2
    print(f"Test 2 (union find): {maximumMinimumPath_uf(grid2)}")  # 预期输出: 2
    
    # 测试用例3
    grid3 = [[3, 4, 6, 3, 4], [0, 2, 1, 1, 7], [8, 8, 3, 2, 7], [3, 2, 4, 9, 8], [4, 1, 2, 0, 0], [4, 6, 5, 4, 3]]
    print(f"Test 3 (max heap): {maximumMinimumPath(grid3)}")  # 预期输出: 3
    print(f"Test 3 (union find): {maximumMinimumPath_uf(grid3)}")  # 预期输出: 3

if __name__ == "__main__":
    test()