from typing import List

class UnionFind:
    """
    并查集实现，专门用于处理网格岛屿问题
    """
    def __init__(self, m: int, n: int):
        self.n = n
        self.size = m * n
        self.parent = [-1] * self.size  # -1表示水
        self.rank = [0] * self.size
        self.count = 0
    
    def get_index(self, x: int, y: int) -> int:
        """将二维坐标转换为一维索引"""
        return x * self.n + y
    
    def add_land(self, x: int, y: int) -> None:
        """添加陆地"""
        index = self.get_index(x, y)
        if self.parent[index] == -1:
            self.parent[index] = index  # 指向自己
            self.rank[index] = 1
            self.count += 1
    
    def find(self, x: int, y: int) -> int:
        """查找操作，使用路径压缩优化"""
        index = self.get_index(x, y)
        if self.parent[index] == -1:
            return -1  # 水的位置
        
        if self.parent[index] != index:
            # 递归查找并进行路径压缩
            root_x = self.parent[index] // self.n
            root_y = self.parent[index] % self.n
            self.parent[index] = self.find(root_x, root_y)
        return self.parent[index]
    
    def union(self, x1: int, y1: int, x2: int, y2: int) -> None:
        """合并两个陆地"""
        root1 = self.find(x1, y1)
        root2 = self.find(x2, y2)
        
        # 如果有一个是水，或者已经在同一个集合中，则不需要合并
        if root1 == -1 or root2 == -1 or root1 == root2:
            return
        
        # 按秩合并
        if self.rank[root1] > self.rank[root2]:
            self.parent[root2] = root1
        elif self.rank[root1] < self.rank[root2]:
            self.parent[root1] = root2
        else:
            self.parent[root2] = root1
            self.rank[root1] += 1
        self.count -= 1  # 合并后岛屿数量减1
    
    def get_count(self) -> int:
        """获取当前岛屿数量"""
        return self.count

class Solution:
    """
    LeetCode 305. 岛屿数量 II
    链接: https://leetcode.cn/problems/number-of-islands-ii/
    难度: 困难
    
    题目描述:
    给你一个大小为 m x n 的二进制网格 grid 。网格表示一个地图，其中，0 表示水，1 表示陆地。
    最初，网格中的单元格要么是水（0），要么是陆地（1）。
    你可以执行 addLand 操作，将位置 (row, col) 的水变成陆地。
    返回一个结果数组，其中每个结果[i] 表示在第 i 次操作后，地图中岛屿的数量。
    
    注意: 一个岛屿被水包围，并且通过水平或垂直方向上相邻的陆地连接而成。
    你可以假设网格网格的四边均被水包围。
    
    示例 1:
    输入: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
    输出: [1,1,2,3]
    
    示例 2:
    输入: m = 1, n = 1, positions = [[0,0]]
    输出: [1]
    
    约束条件:
    1 <= m, n, positions.length <= 10^4
    positions[i].length == 2
    0 <= positions[i][0] < m
    0 <= positions[i][1] < n
    """
    
    def numIslands2(self, m: int, n: int, positions: List[List[int]]) -> List[int]:
        """
        使用并查集解决动态岛屿数量问题
        时间复杂度: O(L * α(m*n))，其中L是positions的长度
        空间复杂度: O(m*n)
        
        解题思路:
        1. 初始化并查集，大小为m*n，初始时所有位置都是水（不属于任何集合）
        2. 对于每个添加陆地的操作:
           - 如果该位置已经是陆地，直接跳过
           - 否则，将该位置标记为陆地，岛屿数量加1
           - 检查四个方向，如果相邻位置是陆地，则合并集合，岛屿数量相应减少
        3. 记录每次操作后的岛屿数量
        """
        if m <= 0 or n <= 0 or not positions:
            return []
        
        uf = UnionFind(m, n)
        grid = [[0] * n for _ in range(m)]  # 0表示水，1表示陆地
        
        # 四个方向：上、右、下、左
        directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
        result = []
        
        for pos in positions:
            x, y = pos
            
            # 如果该位置已经是陆地，直接添加当前岛屿数量
            if grid[x][y] == 1:
                result.append(uf.get_count())
                continue
            
            # 标记为陆地
            grid[x][y] = 1
            uf.add_land(x, y)  # 岛屿数量加1
            
            # 检查四个方向，合并相邻的陆地
            for dx, dy in directions:
                new_x, new_y = x + dx, y + dy
                
                # 检查新位置是否在网格内且是陆地
                if 0 <= new_x < m and 0 <= new_y < n and grid[new_x][new_y] == 1:
                    uf.union(x, y, new_x, new_y)
            
            result.append(uf.get_count())
        
        return result
    
    def numIslands2Optimized(self, m: int, n: int, positions: List[List[int]]) -> List[int]:
        """
        方法2: 使用哈希表存储陆地位置的优化解法
        时间复杂度: O(L * α(L))，其中L是positions的长度
        空间复杂度: O(L)
        """
        if m <= 0 or n <= 0 or not positions:
            return []
        
        # 使用哈希表存储陆地位置，避免使用m*n的数组
        parent = {}  # 位置索引 -> 父节点索引
        rank = {}    # 位置索引 -> 秩
        count = 0
        
        # 四个方向
        directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
        result = []
        
        def find(x: int) -> int:
            """查找操作，使用路径压缩优化"""
            if parent[x] != x:
                parent[x] = find(parent[x])  # 路径压缩
            return parent[x]
        
        for pos in positions:
            x, y = pos
            index = x * n + y
            
            # 如果该位置已经是陆地，直接添加当前岛屿数量
            if index in parent:
                result.append(count)
                continue
            
            # 添加新陆地
            parent[index] = index
            rank[index] = 1
            count += 1
            
            # 检查四个方向
            for dx, dy in directions:
                new_x, new_y = x + dx, y + dy
                new_index = new_x * n + new_y
                
                # 检查新位置是否在网格内且是陆地
                if 0 <= new_x < m and 0 <= new_y < n and new_index in parent:
                    # 合并集合
                    root1 = find(index)
                    root2 = find(new_index)
                    
                    if root1 != root2:
                        # 按秩合并
                        if rank[root1] > rank[root2]:
                            parent[root2] = root1
                        elif rank[root1] < rank[root2]:
                            parent[root1] = root2
                        else:
                            parent[root2] = root1
                            rank[root1] += 1
                        count -= 1
            
            result.append(count)
        
        return result

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1
    m1, n1 = 3, 3
    positions1 = [[0, 0], [0, 1], [1, 2], [2, 1]]
    result1 = solution.numIslands2(m1, n1, positions1)
    print(f"测试用例1 - 方法1: {result1}")  # 预期: [1, 1, 2, 3]
    
    result1_opt = solution.numIslands2Optimized(m1, n1, positions1)
    print(f"测试用例1 - 方法2: {result1_opt}")  # 预期: [1, 1, 2, 3]
    
    # 测试用例2
    m2, n2 = 1, 1
    positions2 = [[0, 0]]
    result2 = solution.numIslands2(m2, n2, positions2)
    print(f"测试用例2 - 方法1: {result2}")  # 预期: [1]
    
    result2_opt = solution.numIslands2Optimized(m2, n2, positions2)
    print(f"测试用例2 - 方法2: {result2_opt}")  # 预期: [1]
    
    # 测试用例3: 重复添加同一位置
    m3, n3 = 2, 2
    positions3 = [[0, 0], [0, 0], [0, 1]]
    result3 = solution.numIslands2(m3, n3, positions3)
    print(f"测试用例3 - 方法1: {result3}")  # 预期: [1, 1, 1]
    
    result3_opt = solution.numIslands2Optimized(m3, n3, positions3)
    print(f"测试用例3 - 方法2: {result3_opt}")  # 预期: [1, 1, 1]
    
    # 测试用例4: 形成一个大岛屿
    m4, n4 = 2, 2
    positions4 = [[0, 0], [0, 1], [1, 0], [1, 1]]
    result4 = solution.numIslands2(m4, n4, positions4)
    print(f"测试用例4 - 方法1: {result4}")  # 预期: [1, 2, 2, 1]
    
    result4_opt = solution.numIslands2Optimized(m4, n4, positions4)
    print(f"测试用例4 - 方法2: {result4_opt}")  # 预期: [1, 2, 2, 1]

if __name__ == "__main__":
    test_solution()