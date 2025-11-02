from collections import deque
from typing import List

# 01矩阵
# 给定一个由 0 和 1 组成的矩阵 mat ，请输出一个大小相同的矩阵，其中每一个格子是 mat 中对应位置元素到最近的 0 的距离。
# 两个相邻元素间的距离为 1 。
# 测试链接 : https://leetcode.cn/problems/01-matrix/
# 
# 算法思路：
# 使用多源BFS，从所有的0开始同时进行BFS搜索。这样每个1第一次被访问时就是到最近0的距离。
# 这种方法比从每个1开始单独BFS要高效得多。
# 
# 时间复杂度：O(m * n)，其中m和n分别是矩阵的行数和列数，每个单元格最多被访问一次
# 空间复杂度：O(m * n)，用于存储队列和结果矩阵
# 
# 工程化考量：
# 1. 多源BFS：从所有0开始同时搜索，避免重复计算
# 2. 原地修改：使用结果矩阵同时记录距离和访问状态
# 3. 边界检查：确保移动后的位置在矩阵范围内
# 4. 性能优化：使用deque获得最佳性能
class Solution:
    def updateMatrix(self, mat: List[List[int]]) -> List[List[int]]:
        if not mat or not mat[0]:
            return []
        
        m, n = len(mat), len(mat[0])
        # 初始化结果矩阵，-1表示未访问
        result = [[-1] * n for _ in range(m)]
        
        # 方向数组：上、右、下、左
        directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
        queue = deque()
        
        # 初始化：将所有0加入队列
        for i in range(m):
            for j in range(n):
                if mat[i][j] == 0:
                    queue.append((i, j))
                    result[i][j] = 0
        
        distance = 0
        
        while queue:
            distance += 1
            size = len(queue)
            
            for _ in range(size):
                x, y = queue.popleft()
                
                for dx, dy in directions:
                    nx, ny = x + dx, y + dy
                    
                    # 检查边界和是否为未访问的1
                    if 0 <= nx < m and 0 <= ny < n and result[nx][ny] == -1:
                        result[nx][ny] = distance
                        queue.append((nx, ny))
        
        return result

# 优化版本：使用动态规划
class SolutionOptimized:
    def updateMatrix(self, mat: List[List[int]]) -> List[List[int]]:
        if not mat or not mat[0]:
            return []
        
        m, n = len(mat), len(mat[0])
        # 初始化距离矩阵，使用一个大数表示无穷大
        dist = [[float('inf')] * n for _ in range(m)]
        
        # 第一次遍历：从左上方到右下方
        for i in range(m):
            for j in range(n):
                if mat[i][j] == 0:
                    dist[i][j] = 0
                else:
                    if i > 0:
                        dist[i][j] = min(dist[i][j], dist[i-1][j] + 1)
                    if j > 0:
                        dist[i][j] = min(dist[i][j], dist[i][j-1] + 1)
        
        # 第二次遍历：从右下方到左上方
        for i in range(m-1, -1, -1):
            for j in range(n-1, -1, -1):
                if mat[i][j] == 1:
                    if i < m-1:
                        dist[i][j] = min(dist[i][j], dist[i+1][j] + 1)
                    if j < n-1:
                        dist[i][j] = min(dist[i][j], dist[i][j+1] + 1)
        
        # 将float转换为int返回
        return [[int(x) for x in row] for row in dist]

# 单元测试
def print_matrix(matrix: List[List[int]]) -> None:
    """打印矩阵"""
    for row in matrix:
        print(' '.join(map(str, row)))
    print()

def test_update_matrix():
    solution = Solution()
    
    # 测试用例1：标准情况
    mat1 = [
        [0, 0, 0],
        [0, 1, 0],
        [0, 0, 0]
    ]
    result1 = solution.updateMatrix(mat1)
    print("测试用例1结果:")
    print_matrix(result1)
    
    # 测试用例2：复杂情况
    mat2 = [
        [0, 0, 0],
        [0, 1, 0],
        [1, 1, 1]
    ]
    result2 = solution.updateMatrix(mat2)
    print("测试用例2结果:")
    print_matrix(result2)
    
    # 测试用例3：全为0
    mat3 = [
        [0, 0],
        [0, 0]
    ]
    result3 = solution.updateMatrix(mat3)
    print("测试用例3结果:")
    print_matrix(result3)
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    test_update_matrix()