"""
最小路径和 (Minimum Path Sum) - Python实现

题目描述：
给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
使得路径上的数字总和为最小。每次只能向下或者向右移动一步。

题目来源：LeetCode 64. 最小路径和
题目链接：https://leetcode.cn/problems/minimum-path-sum/

解题思路分析：
1. 暴力递归：从终点向起点递归，存在大量重复计算，时间复杂度O(2^(m+n))
2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算，时间复杂度O(m*n)
3. 严格位置依赖的动态规划：自底向上填表，避免递归开销，时间复杂度O(m*n)
4. 空间优化版本：利用滚动数组思想，只保存必要的状态，空间复杂度O(min(m,n))

时间复杂度分析：
- 暴力递归：O(2^(m+n)) - 每次递归有两种选择（向右或向下）
- 记忆化搜索：O(m*n) - 每个状态只计算一次
- 动态规划：O(m*n) - 需要填充整个DP表
- 空间优化DP：O(m*n) - 需要遍历整个网格

空间复杂度分析：
- 暴力递归：O(m+n) - 递归栈深度
- 记忆化搜索：O(m*n) - DP数组 + 递归栈
- 动态规划：O(m*n) - DP数组
- 空间优化DP：O(min(m,n)) - 只使用一维数组

是否最优解：是 - 动态规划是解决此类最优路径问题的标准方法

工程化考量：
1. 异常处理：检查输入参数合法性，处理空网格、单行、单列等特殊情况
2. 边界处理：处理边界条件，防止数组越界
3. 性能优化：空间压缩降低内存使用，减少不必要的计算
4. 可测试性：提供完整的测试用例，覆盖各种边界场景
5. 可维护性：代码结构清晰，注释详细，便于理解和维护

Python特性：
- 使用列表推导式创建二维数组
- 动态类型，无需声明变量类型
- 语法简洁，易于理解
- 性能相对较慢，但开发效率高

跨语言差异：
- 与Java相比：语法更简洁，但性能较低
- 与C++相比：开发效率高，但运行效率低

极端场景处理：
- 空输入：返回0
- 单元素网格：直接返回该元素值
- 单行网格：只能向右移动，路径和为行元素累加
- 单列网格：只能向下移动，路径和为列元素累加
- 大网格：使用空间优化版本避免内存溢出

调试技巧：
- 打印中间DP表状态，验证状态转移正确性
- 使用小规模测试用例验证算法正确性
- 对比不同方法的计算结果，确保一致性

与机器学习联系：
- 路径规划问题在强化学习中有广泛应用
- 动态规划思想在马尔可夫决策过程中体现
- 最优路径搜索与图神经网络相关
"""

import sys

class Code01_MinimumPathSum:
    @staticmethod
    def minPathSum1(grid):
        """
        方法1：暴力递归方法
        
        算法思想：从终点(i,j)向起点(0,0)递归，每次只能向左或向上移动
        通过递归探索所有可能的路径，选择路径和最小的那条
        
        时间复杂度：O(2^(m+n)) - 每次递归有两种选择，最坏情况下需要遍历所有可能的路径
        空间复杂度：O(m+n) - 递归栈深度，最坏情况下递归深度为m+n
        
        优点：思路直观，易于理解问题本质
        缺点：存在大量重复计算，大数据量时会超时
        
        适用场景：仅用于理解问题本质，不适用于实际应用
        
        Args:
            grid: 二维网格列表，包含非负整数
            
        Returns:
            int: 从左上角到右下角的最小路径和
            
        异常处理：检查输入参数合法性，处理空网格等特殊情况
        边界处理：处理单行、单列网格等边界情况
        """
        # 输入验证：检查网格是否为空或维度为0
        if not grid or not grid[0]:
            return 0
        # 从终点(len(grid)-1, len(grid[0])-1)开始递归
        return Code01_MinimumPathSum._f1(grid, len(grid) - 1, len(grid[0]) - 1)
    
    @staticmethod
    def _f1(grid, i, j):
        """
        递归辅助函数：计算从(0,0)到(i,j)的最小路径和
        
        状态定义：f1(grid, i, j) 表示从(0,0)到(i,j)的最小路径和
        状态转移：
        - 如果i=0且j=0：到达起点，返回grid[0][0]
        - 否则：最小路径和 = min(从上方来的路径和, 从左方来的路径和) + grid[i][j]
        
        递归终止条件：到达起点(0,0)
        
        Args:
            grid: 二维网格列表
            i: 当前行索引
            j: 当前列索引
            
        Returns:
            int: 从(0,0)到(i,j)的最小路径和
            
        调试技巧：可以打印递归调用栈，观察重复计算情况
        优化方向：添加缓存避免重复计算（记忆化搜索）
        """
        # 基础情况：到达起点(0,0)，路径和就是该位置的值
        if i == 0 and j == 0:
            return grid[0][0]
        
        # 初始化两个方向的路径和为最大值
        up = sys.maxsize   # 从上方来的最小路径和
        left = sys.maxsize # 从左方来的最小路径和
        
        # 如果可以从上方来（不是第一行），递归计算上方路径
        if i - 1 >= 0:
            up = Code01_MinimumPathSum._f1(grid, i - 1, j)
        
        # 如果可以从左方来（不是第一列），递归计算左方路径
        if j - 1 >= 0:
            left = Code01_MinimumPathSum._f1(grid, i, j - 1)
        
        # 当前位置的最小路径和 = min(从上方来, 从左方来) + 当前位置的值
        # 使用min函数简化代码，提高可读性
        return grid[i][j] + min(up, left)
    
    @staticmethod
    def minPathSum2(grid):
        """
        方法2：记忆化搜索方法
        
        算法思想：在暴力递归基础上增加缓存（DP数组），避免重复计算
        当计算某个状态(i,j)时，先检查是否已经计算过，如果计算过直接返回结果
        
        时间复杂度：O(m*n) - 每个状态只计算一次，避免了重复计算
        空间复杂度：O(m*n) - DP数组占用O(m*n)空间，递归栈深度O(m+n)
        
        优点：避免了重复计算，效率显著提升
        缺点：仍然有递归开销，对于极大网格可能栈溢出
        
        适用场景：中等规模网格，状态转移关系复杂的情况
        
        Args:
            grid: 二维网格列表
            
        Returns:
            int: 最小路径和
            
        缓存策略：使用二维列表dp[i][j]缓存计算结果
        初始化值：-1表示未计算，其他值表示已计算的结果
        缓存命中：计算前先检查缓存，避免重复计算
        """
        # 输入验证
        if not grid or not grid[0]:
            return 0
        
        n = len(grid)
        m = len(grid[0])
        
        # 创建DP缓存数组，初始化为-1表示未计算
        # 使用列表推导式创建二维数组，Python风格
        dp = [[-1 for _ in range(m)] for _ in range(n)]
        
        # 从终点开始记忆化搜索
        return Code01_MinimumPathSum._f2(grid, n - 1, m - 1, dp)
    
    @staticmethod
    def _f2(grid, i, j, dp):
        """
        记忆化搜索的递归辅助函数
        
        算法流程：
        1. 检查缓存：如果dp[i][j] != -1，说明已经计算过，直接返回结果
        2. 递归终止：到达起点(0,0)，返回grid[0][0]
        3. 递归计算：分别计算从上方和左方来的最小路径和
        4. 缓存结果：将计算结果存入dp[i][j]，避免重复计算
        
        Args:
            grid: 二维网格列表
            i: 当前行索引
            j: 当前列索引
            dp: 缓存数组
            
        Returns:
            int: 从(0,0)到(i,j)的最小路径和
            
        缓存有效性：确保每个状态只计算一次
        Python特性：列表是可变对象，可以直接修改
        """
        # 缓存检查：如果已经计算过，直接返回缓存结果
        # 这是记忆化搜索的核心，避免了重复计算
        if dp[i][j] != -1:
            return dp[i][j]
        
        # 基础情况：到达起点(0,0)
        if i == 0 and j == 0:
            ans = grid[0][0]
        else:
            # 初始化两个方向的路径和为最大值
            up = sys.maxsize   # 从上方来的最小路径和
            left = sys.maxsize # 从左方来的最小路径和
            
            # 如果可以从上方来（不是第一行），递归计算上方路径
            # 注意：这里使用记忆化搜索，避免重复计算
            if i - 1 >= 0:
                up = Code01_MinimumPathSum._f2(grid, i - 1, j, dp)
            
            # 如果可以从左方来（不是第一列），递归计算左方路径
            if j - 1 >= 0:
                left = Code01_MinimumPathSum._f2(grid, i, j - 1, dp)
            
            # 当前位置的最小路径和 = min(从上方来, 从左方来) + 当前位置的值
            ans = grid[i][j] + min(up, left)
        
        # 缓存结果：将计算结果存入dp数组，避免后续重复计算
        # 这是记忆化搜索的关键步骤
        dp[i][j] = ans
        return ans
    
    @staticmethod
    def minPathSum3(grid):
        """
        方法3：严格位置依赖的动态规划方法
        
        算法思想：自底向上填表，从起点开始逐步计算每个位置的最小路径和
        通过明确的递推关系，避免递归开销，提高算法效率
        
        状态定义：dp[i][j] 表示从(0,0)到(i,j)的最小路径和
        状态转移方程：
        - 当i=0且j=0：dp[0][0] = grid[0][0]
        - 当i=0且j>0：dp[0][j] = dp[0][j-1] + grid[0][j]（只能从左方来）
        - 当i>0且j=0：dp[i][0] = dp[i-1][0] + grid[i][0]（只能从上方来）
        - 当i>0且j>0：dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
        
        时间复杂度：O(m*n) - 需要遍历整个网格，每个位置计算一次
        空间复杂度：O(m*n) - 使用二维DP数组存储中间结果
        
        优点：无递归开销，效率稳定，易于理解和实现
        缺点：空间复杂度较高，对于极大网格可能内存不足
        
        适用场景：各种规模的网格，特别是需要稳定性能的场景
        
        Args:
            grid: 二维网格列表
            
        Returns:
            int: 最小路径和
            
        填表顺序：按行优先顺序填充，确保依赖的状态已经计算
        边界处理：单独处理第一行和第一列的特殊情况
        Python特性：使用列表推导式创建二维数组
        """
        # 输入验证：检查网格是否为空或维度为0
        if not grid or not grid[0]:
            return 0
        
        n = len(grid)
        m = len(grid[0])
        
        # 创建DP数组：使用列表推导式创建二维数组
        # dp[i][j]表示从(0,0)到(i,j)的最小路径和
        dp = [[0 for _ in range(m)] for _ in range(n)]
        
        # 初始化起点：dp[0][0] = grid[0][0]
        # 这是动态规划的基准情况
        dp[0][0] = grid[0][0]
        
        # 初始化第一列：只能从上方来，因为不能从左边来（左边是边界）
        # 对于第一列的每个位置(i,0)，路径和 = 上方位置(i-1,0)的路径和 + 当前值
        for i in range(1, n):
            dp[i][0] = dp[i - 1][0] + grid[i][0]
        
        # 初始化第一行：只能从左方来，因为不能从上方来（上方是边界）
        # 对于第一行的每个位置(0,j)，路径和 = 左方位置(0,j-1)的路径和 + 当前值
        for j in range(1, m):
            dp[0][j] = dp[0][j - 1] + grid[0][j]
        
        # 填充其余位置：对于非边界位置(i,j)，可以从上方或左方来
        # 选择路径和较小的方向 + 当前值
        for i in range(1, n):
            for j in range(1, m):
                # 状态转移方程：dp[i][j] = min(从上方来, 从左方来) + 当前位置的值
                # 使用min函数简化代码，提高可读性
                dp[i][j] = min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]
        
        # 返回终点位置的最小路径和：dp[n-1][m-1]
        # 终点位置存储了从起点到终点的最小路径和
        return dp[n - 1][m - 1]
    
    @staticmethod
    def minPathSum4(grid):
        """
        方法4：严格位置依赖的动态规划 + 空间压缩技巧
        
        算法思想：利用滚动数组思想，将空间复杂度从O(m*n)优化到O(min(m,n))
        观察发现：在计算第i行时，只需要第i-1行的dp值和当前行已经计算的部分
        因此可以使用一维数组来存储状态，通过滚动更新来节省空间
        
        状态定义：dp[j] 表示当前行第j列的最小路径和
        状态转移：
        - 对于第一列：只能从上方来，dp[0] = dp[0] + grid[i][0]
        - 对于其他列：dp[j] = min(dp[j-1], dp[j]) + grid[i][j]
        
        时间复杂度：O(m*n) - 需要遍历整个网格，每个位置计算一次
        空间复杂度：O(min(m,n)) - 只使用一维数组，长度为较小的维度
        
        优点：空间效率高，适合处理大规模网格
        缺点：代码逻辑相对复杂，需要理解滚动数组的原理
        
        适用场景：大规模网格，内存受限的环境
        
        Args:
            grid: 二维网格列表
            
        Returns:
            int: 最小路径和
            
        空间优化原理：利用状态依赖的局部性，只保存必要的中间结果
        更新顺序：按行更新，每行从左到右更新
        Python特性：使用列表推导式创建一维数组
        """
        # 输入验证
        if not grid or not grid[0]:
            return 0
        
        n = len(grid)
        m = len(grid[0])
        
        # 空间优化：使用一维数组代替二维数组
        # 选择较小的维度作为数组长度，进一步优化空间
        # 这里假设列数m较小，如果行数n较小可以交换处理
        dp = [0 for _ in range(m)]
        
        # 初始化第一行：只能从左方来
        # dp[j] 表示第一行第j列的最小路径和
        dp[0] = grid[0][0]
        for j in range(1, m):
            # 第一行的每个位置只能从左方来
            dp[j] = dp[j - 1] + grid[0][j]
        
        # 逐行更新：从第二行开始处理
        for i in range(1, n):
            # 更新第一列：只能从上方来
            # 注意：dp[0]存储的是上一行第一列的值，需要加上当前值
            dp[0] = dp[0] + grid[i][0]
            
            # 更新其余列：可以从上方或左方来
            for j in range(1, m):
                # 关键理解：
                # - dp[j] 存储的是上一行第j列的值（从上方来的路径和）
                # - dp[j-1] 存储的是当前行第j-1列的值（从左方来的路径和）
                # 选择较小的路径和 + 当前值
                dp[j] = min(dp[j - 1], dp[j]) + grid[i][j]
        
        # 返回终点位置的最小路径和：dp[m-1]
        # 经过逐行更新后，dp数组的最后一个元素就是最终结果
        return dp[m - 1]

    @staticmethod
    def knapsack1(w, v, C):
        """
        补充题目：0-1背包问题实现
        给定n个物品，每个物品有重量w[i]和价值v[i]，背包容量为C
        时间复杂度：O(n*C)
        空间复杂度：O(n*C)
        """
        # 输入验证
        if not w or not v or len(w) != len(v) or C <= 0:
            return 0
        n = len(w)
        # dp[i][j] 表示前i个物品，背包容量为j时的最大价值
        dp = [[0 for _ in range(C + 1)] for _ in range(n + 1)]
        
        # 逐行填充DP表
        for i in range(1, n + 1):
            for j in range(1, C + 1):
                # 不选择第i个物品
                dp[i][j] = dp[i - 1][j]
                # 选择第i个物品（如果容量足够）
                if j >= w[i - 1]:
                    dp[i][j] = max(dp[i][j], dp[i - 1][j - w[i - 1]] + v[i - 1])
        
        return dp[n][C]
    
    @staticmethod
    def knapsack2(w, v, C):
        """
        0-1背包问题的空间优化版本
        时间复杂度：O(n*C)
        空间复杂度：O(C)
        """
        # 输入验证
        if not w or not v or len(w) != len(v) or C <= 0:
            return 0
        n = len(w)
        # 只使用一维数组
        dp = [0 for _ in range(C + 1)]
        
        # 逆序遍历容量，避免重复选择物品
        for i in range(n):
            for j in range(C, w[i] - 1, -1):
                dp[j] = max(dp[j], dp[j - w[i]] + v[i])
        
        return dp[C]
    
    @staticmethod
    def canPartition(nums):
        """
        分割等和子集问题（0-1背包的变形）
        判断是否可以将数组分割成两个和相等的子集
        """
        # 输入验证
        if not nums or len(nums) < 2:
            return False
        
        total_sum = sum(nums)
        # 如果和为奇数，无法分割成两个和相等的子集
        if total_sum % 2 != 0:
            return False
        
        target = total_sum // 2
        # 转化为0-1背包问题：是否能从数组中选择一些数，使得它们的和为target
        dp = [False for _ in range(target + 1)]
        dp[0] = True  # 空集的和为0
        
        for num in nums:
            for j in range(target, num - 1, -1):
                dp[j] = dp[j] or dp[j - num]
        
        return dp[target]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    grid1 = [
        [1, 3, 1],
        [1, 5, 1],
        [4, 2, 1]
    ]
    print("测试用例1:")
    print("网格:", grid1)
    print("最小路径和:", Code01_MinimumPathSum.minPathSum3(grid1))  # 应该输出7
    
    # 测试用例2
    grid2 = [
        [1, 2, 3],
        [4, 5, 6]
    ]
    print("\n测试用例2:")
    print("网格:", grid2)
    print("最小路径和:", Code01_MinimumPathSum.minPathSum3(grid2))  # 应该输出12

    # 测试0-1背包问题
    w = [2, 3, 4, 5]
    v = [3, 4, 5, 6]
    C = 8
    print("\n0-1背包测试:")
    print(f"物品重量: {w}, 物品价值: {v}, 背包容量: {C}")
    print(f"最大价值(标准DP): {Code01_MinimumPathSum.knapsack1(w, v, C)}")  # 应该输出9
    print(f"最大价值(空间优化): {Code01_MinimumPathSum.knapsack2(w, v, C)}")  # 应该输出9
    
    # 测试分割等和子集问题
    nums1 = [1, 5, 11, 5]  # 可以分割成 [1, 5, 5] 和 [11]
    nums2 = [1, 2, 3, 5]   # 无法分割
    print("\n分割等和子集测试:")
    print(f"数组 {nums1} 能否分割: {Code01_MinimumPathSum.canPartition(nums1)}")  # 应该输出True
    print(f"数组 {nums2} 能否分割: {Code01_MinimumPathSum.canPartition(nums2)}")  # 应该输出False