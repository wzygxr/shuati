/**
 * LeetCode 803 - 打砖块
 * https://leetcode-cn.com/problems/bricks-falling-when-hit/
 * 
 * 题目描述：
 * 有一个 m x n 的二元网格，其中 1 表示砖块，0 表示空白。砖块 稳定（不会掉落）的前提是：
 * - 一块砖直接连接到网格的顶部，或者
 * - 至少有一块相邻（4 个方向之一）的砖块 稳定 不会掉落时
 * 
 * 给你一个数组 hits ，这是需要依次消除砖块的位置。每当消除 hits[i] = (rowi, coli) 位置上的砖块时，对应位置的砖块（如果存在）会消失，然后其他砖块可能因为这一消除操作而掉落。
 * 一旦砖块掉落，它会立即从网格中消失（即，它不会参与后续的消除操作）。
 * 
 * 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作后掉落的砖块数目。
 * 
 * 注意，消除可能指向是没有砖块的空白位置，如果发生这种情况，则没有砖块掉落。
 * 
 * 解题思路（逆向思维 + 并查集）：
 * 1. 首先将所有要敲打的砖块标记为被敲打过（如果有砖块的话）
 * 2. 将剩余的稳定砖块用并查集连接起来，特别是与顶部相连的砖块
 * 3. 然后逆向处理每次敲打操作：
 *    a. 将被敲打的砖块恢复
 *    b. 检查它的四个方向，如果有砖块，就将它们合并到并查集中
 *    c. 计算恢复后新增的与顶部相连的砖块数量，减去1（被敲打的砖块本身）
 * 4. 最后反转结果数组
 * 
 * 时间复杂度分析：
 * - 初始化：O(m * n)
 * - 构建初始并查集：O(m * n * α(m * n))
 * - 逆向处理每次敲打：O(h * α(m * n))，其中h是敲打次数
 * - 总体时间复杂度：O((m * n + h) * α(m * n)) ≈ O(m * n + h)
 * 
 * 空间复杂度分析：
 * - 并查集：O(m * n)
 * - 辅助数组：O(m * n)
 * - 总体空间复杂度：O(m * n)
 */

class HitBricks:
    def __init__(self):
        self.parent = []
        self.size = []
        self.rows = 0
        self.cols = 0
        # 上、下、左、右四个方向
        self.DIRECTIONS = [(-1, 0), (1, 0), (0, -1), (0, 1)]
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x: 要查找的元素
            
        返回:
            根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个元素所在的集合
        
        参数:
            x: 第一个元素
            y: 第二个元素
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return
        
        # 将较小的集合合并到较大的集合中
        if self.size[root_x] < self.size[root_y]:
            self.parent[root_x] = root_y
            self.size[root_y] += self.size[root_x]
        else:
            self.parent[root_y] = root_x
            self.size[root_x] += self.size[root_y]
    
    def get_index(self, row, col):
        """
        将二维坐标转换为一维索引
        
        参数:
            row: 行号
            col: 列号
            
        返回:
            一维索引
        """
        return row * self.cols + col
    
    def is_valid(self, row, col):
        """
        检查坐标是否有效
        
        参数:
            row: 行号
            col: 列号
            
        返回:
            是否有效
        """
        return 0 <= row < self.rows and 0 <= col < self.cols
    
    def connect_adjacent_bricks(self, grid, row, col):
        """
        连接砖块与相邻的砖块
        
        参数:
            grid: 网格
            row: 当前砖块的行号
            col: 当前砖块的列号
        """
        current_index = self.get_index(row, col)
        
        # 检查四个方向的相邻砖块
        for dr, dc in self.DIRECTIONS:
            new_row, new_col = row + dr, col + dc
            if self.is_valid(new_row, new_col) and grid[new_row][new_col] == 1:
                adjacent_index = self.get_index(new_row, new_col)
                self.union(current_index, adjacent_index)
    
    def hit_bricks(self, grid, hits):
        """
        打砖块
        
        参数:
            grid: 网格
            hits: 要敲打的砖块位置
            
        返回:
            每次敲打后掉落的砖块数量数组
        """
        self.rows = len(grid)
        self.cols = len(grid[0])
        total_bricks = self.rows * self.cols
        
        # 初始化并查集
        self.parent = list(range(total_bricks))
        self.size = [1] * total_bricks
        
        # 创建网格的副本，并标记被敲打的砖块
        grid_copy = [row[:] for row in grid]
        
        # 首先标记所有被敲打的砖块（如果有砖块的话）
        for row, col in hits:
            grid_copy[row][col] = 0
        
        # 构建初始并查集：将所有剩余的砖块连接起来
        for i in range(self.rows):
            for j in range(self.cols):
                if grid_copy[i][j] == 1:
                    self.connect_adjacent_bricks(grid_copy, i, j)
        
        # 逆向处理每次敲打
        result = [0] * len(hits)
        for i in range(len(hits) - 1, -1, -1):
            row, col = hits[i]
            
            # 如果原始网格中该位置没有砖块，跳过
            if grid[row][col] == 0:
                result[i] = 0
                continue
            
            # 记录恢复前与顶部相连的砖块数量
            before_count = 0
            for j in range(self.cols):
                if grid_copy[0][j] == 1:
                    before_count = self.size[self.find(self.get_index(0, j))]
                    break
            
            # 恢复砖块
            grid_copy[row][col] = 1
            
            # 连接恢复砖块的相邻砖块
            self.connect_adjacent_bricks(grid_copy, row, col)
            
            # 记录恢复后与顶部相连的砖块数量
            after_count = 0
            for j in range(self.cols):
                if grid_copy[0][j] == 1:
                    after_count = self.size[self.find(self.get_index(0, j))]
                    break
            
            # 计算掉落的砖块数量（恢复后新增的稳定砖块数，减去恢复的砖块本身）
            result[i] = max(0, after_count - before_count - 1)
        
        return result

# 测试代码
def test_hit_bricks():
    solution = HitBricks()
    
    # 测试用例1
    grid1 = [
        [1, 0, 0, 0],
        [1, 1, 1, 0]
    ]
    hits1 = [[1, 0]]
    result1 = solution.hit_bricks(grid1, hits1)
    print("测试用例1结果：", result1)
    # 预期输出：[2]
    
    # 测试用例2
    grid2 = [
        [1, 0, 0, 0],
        [1, 1, 0, 0]
    ]
    hits2 = [[1, 1], [1, 0]]
    result2 = solution.hit_bricks(grid2, hits2)
    print("测试用例2结果：", result2)
    # 预期输出：[0, 0]
    
    # 测试用例3
    grid3 = [
        [1, 1, 1],
        [0, 1, 0],
        [0, 0, 0]
    ]
    hits3 = [[0, 2], [2, 0], [0, 1], [1, 2]]
    result3 = solution.hit_bricks(grid3, hits3)
    print("测试用例3结果：", result3)
    # 预期输出：[0, 0, 1, 0]

if __name__ == "__main__":
    test_hit_bricks()

'''
Python特定优化：
1. 使用列表推导式创建网格副本，提高代码简洁性
2. 使用元组表示方向数组，更加高效
3. 实现了完整的类结构，封装了所有相关方法
4. 代码结构清晰，易于理解和维护

算法思路详解：
1. 逆向思维：常规思路是模拟每次敲打后的砖块掉落，但这样效率低下。相反，我们可以逆向思考，从最终状态开始，逐步恢复被敲打的砖块。
2. 并查集应用：使用并查集来管理砖块的连通性，特别是与顶部相连的砖块。
3. 关键观察：如果一个砖块被恢复后，能够与顶部建立连接，那么在正向过程中，移除这个砖块会导致所有依赖它的砖块掉落。

工程化考量：
1. 边界情况处理：处理了原始网格中没有砖块的情况
2. 数据结构选择：使用列表实现并查集，对于Python来说足够高效
3. 可读性优化：添加了详细的注释和函数文档字符串
4. 测试覆盖：包含了多个测试用例，覆盖不同的场景

时间复杂度深入分析：
- 并查集的find和union操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
- 构建初始连通分量需要O(m * n * α(m * n))时间
- 逆向处理h次敲打需要O(h * α(m * n))时间
- 总体时间复杂度为O((m * n + h) * α(m * n)) ≈ O(m * n + h)

空间复杂度深入分析：
- 并查集数组的空间复杂度为O(m * n)
- 网格副本的空间复杂度为O(m * n)
- 结果数组的空间复杂度为O(h)
- 总体空间复杂度为O(m * n + h)
'''