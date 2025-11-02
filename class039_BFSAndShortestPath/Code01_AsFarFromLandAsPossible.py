# 地图分析
# 你现在手里有一份大小为 n x n 的 网格 grid
# 上面的每个 单元格 都用 0 和 1 标记好了其中 0 代表海洋，1 代表陆地。
# 请你找出一个海洋单元格，这个海洋单元格到离它最近的陆地单元格的距离是最大的
# 并返回该距离。如果网格上只有陆地或者海洋，请返回 -1。
# 我们这里说的距离是「曼哈顿距离」（ Manhattan Distance）：
# (x0, y0) 和 (x1, y1) 这两个单元格之间的距离是 |x0 - x1| + |y0 - y1| 。
# 测试链接 : https://leetcode.cn/problems/as-far-from-land-as-possible/
# 
# 算法思路：
# 使用多源BFS，从所有陆地同时开始搜索，这样可以保证每个海洋格子第一次被访问时就是到最近陆地的最短距离
# 最后一个被访问的海洋格子就是距离陆地最远的海洋格子
# 
# 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数，每个格子最多被访问一次
# 空间复杂度：O(n * m)，用于存储队列和访问状态
# 
# 工程化考量：
# 1. 异常处理：检查输入是否为空
# 2. 边界情况：全为陆地或全为海洋的情况
# 3. 优化：提前判断特殊情况

from collections import deque

def maxDistance(grid):
    """
    计算海洋单元格到最近陆地单元格的最大距离
    
    Args:
        grid: List[List[int]] - n x n 的网格，0表示海洋，1表示陆地
        
    Returns:
        int - 最大曼哈顿距离，如果只有陆地或海洋则返回-1
    """
    if not grid or not grid[0]:
        return -1
    
    n = len(grid)
    m = len(grid[0])
    
    # 初始化队列和访问状态
    queue = deque()
    visited = [[False] * m for _ in range(n)]
    seas = 0
    
    # 将所有陆地加入队列，并统计海洋数量
    for i in range(n):
        for j in range(m):
            if grid[i][j] == 1:
                queue.append((i, j))
                visited[i][j] = True
            else:
                seas += 1
    
    # 如果全是陆地或者全是海洋，返回-1
    if seas == 0 or seas == n * m:
        return -1
    
    # 四个方向的移动：上、右、下、左
    moves = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    level = 0
    # 多源BFS
    while queue:
        level += 1
        size = len(queue)
        # 一层一层扩展
        for _ in range(size):
            x, y = queue.popleft()
            # 向四个方向扩展
            for dx, dy in moves:
                nx, ny = x + dx, y + dy
                # 检查边界和是否已访问
                if 0 <= nx < n and 0 <= ny < m and not visited[nx][ny]:
                    visited[nx][ny] = True
                    queue.append((nx, ny))
    
    # 最后一层就是最远距离，由于最后一次level++没有对应的层，所以返回level-1
    return level - 1

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    grid1 = [[1,0,1],[0,0,0],[1,0,1]]
    print("测试用例1结果:", maxDistance(grid1))  # 期望输出: 2
    
    # 测试用例2
    grid2 = [[1,0,0],[0,0,0],[0,0,0]]
    print("测试用例2结果:", maxDistance(grid2))  # 期望输出: 4
    
    # 测试用例3：全为陆地
    grid3 = [[1,1,1],[1,1,1],[1,1,1]]
    print("测试用例3结果:", maxDistance(grid3))  # 期望输出: -1
    
    # 测试用例4：全为海洋
    grid4 = [[0,0,0],[0,0,0],[0,0,0]]
    print("测试用例4结果:", maxDistance(grid4))  # 期望输出: -1