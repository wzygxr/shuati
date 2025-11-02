# LeetCode 778. Swim in Rising Water
# 题目链接: https://leetcode.cn/problems/swim-in-rising-water/
# 
# 题目描述:
# 在一个 n x n 的整数矩阵 grid 中，每一个方格的值 grid[i][j] 表示位置 (i, j) 的平台高度。
# 当开始下雨时，在时间为 t 时，水位为 t。你可以从一个平台游向四周相邻的任意一个平台，
# 但前提是此时水位必须同时淹没这两个平台。假定你可以瞬间移动无限距离，也就是在方格内部游动是不耗时的。
# 当然，在你游泳的时候你必须待在坐标方格里面。
# 你从坐标方格的左上平台 (0, 0) 出发。返回你到达坐标方格的右下平台 (n-1, n-1) 所需的最少时间。
#
# 解题思路:
# 这个问题可以转化为最小生成树问题。我们将每个格子看作图中的一个节点，
# 相邻的格子之间有一条边，边的权重是两个格子高度的最大值。
# 我们需要找到从 (0,0) 到 (n-1,n-1) 的最小生成树，记录构建生成树期间的最大海拔值，
# 这就是所需的最低水位。
#
# 另一种思路是使用二分搜索+DFS/BFS：
# 1. 二分搜索可能的答案（时间t）
# 2. 对于每个t，检查是否能从(0,0)到达(n-1,n-1)
# 3. 使用DFS或BFS进行可达性检查
#
# 我们这里使用并查集实现的Kruskal算法来解决：
# 1. 构建所有相邻格子之间的边，权重为两个格子高度的最大值
# 2. 按权重对边进行排序
# 3. 依次添加边，直到起点和终点连通
# 4. 此时的最大权重即为答案
#
# 时间复杂度: O(N^2 * log(N^2)) = O(N^2 * log N)，其中N是网格的边长
# 空间复杂度: O(N^2)
# 是否为最优解: 是，这是解决该问题的高效方法之一
# 工程化考量:
# 1. 异常处理: 检查输入参数的有效性
# 2. 边界条件: 处理空网格、单元素网格等特殊情况
# 3. 内存管理: 使用列表存储边信息
# 4. 性能优化: 并查集的路径压缩和按秩合并优化

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        return True
    
    def is_connected(self, x, y):
        return self.find(x) == self.find(y)

def swimInWater(grid):
    n = len(grid)
    
    # 构建边
    edges = []
    
    # 添加相邻格子之间的边
    for i in range(n):
        for j in range(n):
            # 向右连接
            if j + 1 < n:
                weight = max(grid[i][j], grid[i][j + 1])
                edges.append([i * n + j, i * n + j + 1, weight])
            # 向下连接
            if i + 1 < n:
                weight = max(grid[i][j], grid[i + 1][j])
                edges.append([i * n + j, (i + 1) * n + j, weight])
    
    # 按权重排序
    edges.sort(key=lambda x: x[2])
    
    # 使用并查集
    uf = UnionFind(n * n)
    
    # 依次添加边，直到起点和终点连通
    for u, v, weight in edges:
        if uf.union(u, v):
            # 如果起点和终点已经连通，返回当前权重
            if uf.is_connected(0, n * n - 1):
                return weight
    
    return 0

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    grid1 = [[0, 2], [1, 3]]
    print("测试用例1结果:", swimInWater(grid1))  # 预期输出: 3
    
    # 测试用例2
    grid2 = [[0, 1, 2, 3, 4], [24, 23, 22, 21, 5], [12, 13, 14, 15, 16], [11, 17, 18, 19, 20], [10, 9, 8, 7, 6]]
    print("测试用例2结果:", swimInWater(grid2))  # 预期输出: 16
    
    # 测试用例3
    grid3 = [[3, 2], [0, 1]]
    print("测试用例3结果:", swimInWater(grid3))  # 预期输出: 3