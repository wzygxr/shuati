# 矩阵中的最长递增路径
# 给定一个 m x n 整数矩阵 matrix ，找出其中 最长递增路径 的长度
# 对于每个单元格，你可以往上，下，左，右四个方向移动
# 你 不能 在 对角线 方向上移动或移动到 边界外（即不允许环绕）
# 测试链接 : https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
#
# 题目来源：LeetCode 329. 矩阵中的最长递增路径
# 题目链接：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
# 时间复杂度：O(m*n) - 每个单元格只计算一次
# 空间复杂度：O(m*n) - DP数组 + 递归栈
# 是否最优解：是 - 记忆化搜索是解决此类图中路径问题的标准方法
#
# 解题思路：
# 1. 暴力递归：从每个单元格开始进行深度优先搜索，但存在大量重复计算
# 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界处理：处理空矩阵、单元素矩阵等特殊情况
# 3. 性能优化：记忆化搜索避免重复计算
# 4. 可测试性：提供完整的测试用例
#
# 算法详解：
# 这是一个经典的图搜索问题，可以看作在有向无环图(DAG)中寻找最长路径。
# 
# 状态定义：
# dp[i][j] 表示从位置(i,j)出发能走的最长递增路径长度
#
# 状态转移：
# 对于位置(i,j)，我们可以向四个方向移动到相邻位置，如果相邻位置的值大于当前位置的值，
# 则可以移动。转移方程为：
# dp[i][j] = max(dp[相邻位置]) + 1 （对于所有可以移动到的相邻位置）
#
# 边界条件：
# 当无法向任何方向移动时，路径长度为1（只有当前位置）
#
# 为什么使用记忆化搜索而不是BFS？
# 1. 问题特性：每个位置的最长路径长度是固定的，可以缓存
# 2. 实现简单：DFS+记忆化比BFS更直观
# 3. 时间复杂度相同：都是O(m*n)

class Code06_LongestIncreasingPath:
    @staticmethod
    def longestIncreasingPath1(grid):
        """
        方法1：暴力递归
        时间复杂度：O(m*n*4^(m*n)) - 存在大量重复计算
        空间复杂度：O(m*n) - 递归栈深度
        该方法在大数据量时会超时，仅用于理解问题本质
        """
        # 输入验证
        if not grid or not grid[0]:
            return 0
        
        ans = 0
        # 从每个单元格开始搜索
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                pathLength = Code06_LongestIncreasingPath._f1(grid, i, j)
                if pathLength > ans:
                    ans = pathLength
        return ans

    @staticmethod
    def _f1(grid, i, j):
        """
        从(i,j)出发，能走出来多长的递增路径，返回最长长度
        :param grid: 矩阵
        :param i:    当前行坐标
        :param j:    当前列坐标
        :return: 从(i,j)出发的最长递增路径长度
        """
        next_length = 0
        
        # 向上移动
        if i > 0 and grid[i][j] < grid[i - 1][j]:
            up = Code06_LongestIncreasingPath._f1(grid, i - 1, j)
            if up > next_length:
                next_length = up
        
        # 向下移动
        if i + 1 < len(grid) and grid[i][j] < grid[i + 1][j]:
            down = Code06_LongestIncreasingPath._f1(grid, i + 1, j)
            if down > next_length:
                next_length = down
        
        # 向左移动
        if j > 0 and grid[i][j] < grid[i][j - 1]:
            left = Code06_LongestIncreasingPath._f1(grid, i, j - 1)
            if left > next_length:
                next_length = left
        
        # 向右移动
        if j + 1 < len(grid[0]) and grid[i][j] < grid[i][j + 1]:
            right = Code06_LongestIncreasingPath._f1(grid, i, j + 1)
            if right > next_length:
                next_length = right
        
        # 当前位置算1步，加上后续最长路径
        return next_length + 1

    @staticmethod
    def longestIncreasingPath2(grid):
        """
        方法2：记忆化搜索
        时间复杂度：O(m*n) - 每个单元格只计算一次
        空间复杂度：O(m*n) - DP数组 + 递归栈
        通过缓存已计算的结果避免重复计算
        """
        # 输入验证
        if not grid or not grid[0]:
            return 0
        
        n = len(grid)
        m = len(grid[0])
        
        # 创建DP数组并初始化为0，表示未计算
        dp = [[0 for _ in range(m)] for _ in range(n)]
        
        ans = 0
        # 从每个单元格开始搜索
        for i in range(n):
            for j in range(m):
                pathLength = Code06_LongestIncreasingPath._f2(grid, i, j, dp)
                if pathLength > ans:
                    ans = pathLength
        return ans

    @staticmethod
    def _f2(grid, i, j, dp):
        """
        带记忆化的递归函数
        dp[i][j] 表示从(i,j)出发的最长递增路径长度
        """
        # 如果已经计算过，直接返回结果
        if dp[i][j] != 0:
            return dp[i][j]
        
        next_length = 0
        
        # 向上移动
        if i > 0 and grid[i][j] < grid[i - 1][j]:
            up = Code06_LongestIncreasingPath._f2(grid, i - 1, j, dp)
            if up > next_length:
                next_length = up
        
        # 向下移动
        if i + 1 < len(grid) and grid[i][j] < grid[i + 1][j]:
            down = Code06_LongestIncreasingPath._f2(grid, i + 1, j, dp)
            if down > next_length:
                next_length = down
        
        # 向左移动
        if j > 0 and grid[i][j] < grid[i][j - 1]:
            left = Code06_LongestIncreasingPath._f2(grid, i, j - 1, dp)
            if left > next_length:
                next_length = left
        
        # 向右移动
        if j + 1 < len(grid[0]) and grid[i][j] < grid[i][j + 1]:
            right = Code06_LongestIncreasingPath._f2(grid, i, j + 1, dp)
            if right > next_length:
                next_length = right
        
        # 当前位置算1步，加上后续最长路径
        ans = next_length + 1
        
        # 缓存结果并返回
        dp[i][j] = ans
        return ans

# 辅助函数：打印矩阵
def print_matrix(matrix):
    for row in matrix:
        print(" ".join(map(str, row)))

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    grid1 = [
        [9, 9, 4],
        [6, 6, 8],
        [2, 1, 1]
    ]
    print("测试用例1:")
    print("矩阵:")
    print_matrix(grid1)
    print("最长递增路径长度:", Code06_LongestIncreasingPath.longestIncreasingPath2(grid1))  # 应该输出4
    
    # 测试用例2
    grid2 = [
        [3, 4, 5],
        [3, 2, 6],
        [2, 2, 1]
    ]
    print("\n测试用例2:")
    print("矩阵:")
    print_matrix(grid2)
    print("最长递增路径长度:", Code06_LongestIncreasingPath.longestIncreasingPath2(grid2))  # 应该输出4
    
    # 测试用例3
    grid3 = [[1]]
    print("\n测试用例3:")
    print("矩阵:")
    print_matrix(grid3)
    print("最长递增路径长度:", Code06_LongestIncreasingPath.longestIncreasingPath2(grid3))  # 应该输出1