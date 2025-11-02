# Minimum Path Sum
# 给定一个包含非负整数的 m x n 网格 grid，请找出一条从左上角到右下角的路径，
# 使得路径上的数字总和为最小。每次只能向下或者向右移动一步。
# 测试链接 : https://leetcode.cn/problems/minimum-path-sum/

"""
算法思路深度解析：
1. 这是一个典型的二维动态规划问题，属于路径优化类问题的基础模型
   - 问题的核心是在约束移动方向（只能向右或向下）的情况下，寻找最优路径
   - 动态规划的关键在于利用子问题的最优解构建原问题的最优解
2. 使用二维动态规划 dp[i][j]，其中：
   - dp[i][j] 表示从起点(0,0)到位置(i,j)的最小路径和
   - 状态定义清晰地捕捉了问题的最优子结构特性
3. 状态转移方程的详细分析：
   - 起点处理：dp[0][0] = grid[0][0]（基础情况）
   - 边界处理：
     * 第一行(i == 0)：只能从左边来，dp[i][j] = dp[i][j-1] + grid[i][j]
     * 第一列(j == 0)：只能从上面来，dp[i][j] = dp[i-1][j] + grid[i][j]
   - 一般情况：选择从上面或左边来的最小路径和，dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
4. 初始化策略：
   - 逐步初始化：先处理起点，再分别处理第一行和第一列，最后处理内部格子
   - 这种初始化方式确保了在计算每个状态时，其依赖的状态已经被计算完成
5. 结果提取：
   - dp[m-1][n-1]即为所求的从起点到终点的最小路径和

时间复杂度分析：
- 状态总数：O(m * n)
- 每个状态的计算是O(1)操作
- 总时间复杂度：O(m * n)

空间复杂度分析：
- 原始实现使用二维数组：O(m * n)
- 可以通过滚动数组优化到O(min(m, n))
- 更进一步，可以直接在原数组上进行修改，空间复杂度降为O(1)（如果允许修改输入）

Python实现注意事项：
1. 初始化优化：使用列表推导式创建二维数组，简洁高效
2. 边界条件处理：正确处理第一行和第一列的初始化
3. 内存优化：可以使用一维数组进行滚动更新，减少内存消耗
4. 输入验证：确保输入网格非空且为有效尺寸
5. 数值溢出：虽然题目说明是非负整数，但Python的整数不会溢出

工程化考量：
1. 异常处理：处理空网格或无效输入的情况
2. 性能优化：对于大型网格，考虑使用滚动数组优化空间
3. 代码可读性：使用清晰的变量名和适当的注释
4. 边界测试：确保算法能正确处理各种边界情况
5. 算法扩展性：如何修改以支持更多的移动方向或约束条件

常见问题排查：
1. 索引越界：注意Python的索引从0开始
2. 初始化错误：确保第一行和第一列的初始化正确
3. 状态转移错误：确保选择了正确的前驱状态
4. 内存限制：对于超大网格，需要使用空间优化版本
5. 输入验证：确保输入网格符合要求
"""

from typing import List

class Solution:
    def minPathSum(self, grid: List[List[int]]) -> int:
        """
        计算从左上角到右下角的最小路径和
        :param grid: m x n 的网格，包含非负整数
        :return: 最小路径和
        """
        m = len(grid)
        n = len(grid[0])
        
        # dp[i][j] 表示从起点到位置(i,j)的最小路径和
        dp = [[0] * n for _ in range(m)]
        
        # 初始化起点
        dp[0][0] = grid[0][0]
        
        # 初始化第一行：只能从左边来，累加左边路径的和
        for j in range(1, n):
            dp[0][j] = dp[0][j-1] + grid[0][j]
        
        # 初始化第一列：只能从上面来，累加上面路径的和
        for i in range(1, m):
            dp[i][0] = dp[i-1][0] + grid[i][0]
        
        # 填充dp表
        for i in range(1, m):
            for j in range(1, n):
                # 路径和等于从上面和左边来的较小路径和加上当前位置的值
                dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
        
        return dp[m-1][n-1]

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    grid1 = [
        [1, 3, 1],
        [1, 5, 1],
        [4, 2, 1]
    ]
    print("测试用例1结果:", solution.minPathSum(grid1))  # 预期输出: 7
    
    # 测试用例2
    grid2 = [
        [1, 2, 3],
        [4, 5, 6]
    ]
    print("测试用例2结果:", solution.minPathSum(grid2))  # 预期输出: 12
    
    # 测试用例3
    grid3 = [
        [1, 2],
        [1, 1]
    ]
    print("测试用例3结果:", solution.minPathSum(grid3))  # 预期输出: 3
    
    # 测试用例4
    grid4 = [
        [1, 3, 1, 2],
        [1, 5, 1, 3],
        [4, 2, 1, 1]
    ]
    print("测试用例4结果:", solution.minPathSum(grid4))  # 预期输出: 8

# 类似题目与训练拓展：
# 1. LeetCode 62. Unique Paths (不同路径)
#    链接：https://leetcode.cn/problems/unique-paths/
#    区别：计算从左上角到右下角的不同路径数量
#    算法：组合数学或二维动态规划，dp[i][j] = dp[i-1][j] + dp[i][j-1]
#
# 2. LeetCode 63. Unique Paths II (不同路径 II)
#    链接：https://leetcode.cn/problems/unique-paths-ii/
#    区别：网格中有障碍物，计算不同路径的数量
#    算法：二维动态规划，遇到障碍物时dp[i][j] = 0
#
# 3. LeetCode 174. Dungeon Game (地下城游戏)
#    链接：https://leetcode.cn/problems/dungeon-game/
#    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
#    算法：从右下角向左上角动态规划，dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
#
# 4. LeetCode 64. Minimum Path Sum (本题)
#    链接：https://leetcode.cn/problems/minimum-path-sum/
#    算法：二维动态规划，计算最小路径和
#
# 5. LeetCode 741. Cherry Pickup (摘樱桃 I)
#    链接：https://leetcode.cn/problems/cherry-pickup/
#    区别：一个人从(0,0)走到(n-1,n-1)再走回(0,0)，求最大收集樱桃数
#    算法：三维动态规划，转化为两个人同时从起点到终点的问题
#
# 6. LeetCode 1463. Cherry Pickup II (摘樱桃 II)
#    链接：https://leetcode.cn/problems/cherry-pickup-ii/
#    区别：两个机器人同时从顶部向底部移动，收集樱桃
#    算法：三维动态规划，状态定义为dp[i][j1][j2]
#
# 7. LeetCode 688. Knight Probability in Chessboard
#    链接：https://leetcode.cn/problems/knight-probability-in-chessboard/
#    区别：国际象棋骑士移动，计算留在棋盘内的概率
#    算法：动态规划，dp[k][r][c]表示k步后在位置(r,c)的概率
#
# 8. LeetCode 980. Unique Paths III
#    链接：https://leetcode.cn/problems/unique-paths-iii/
#    区别：需要访问所有非障碍物格子的路径数
#    算法：回溯+剪枝或位掩码动态规划
#
# 9. 洛谷 P1002 [NOIP2002 普及组] 过河卒
#    链接：https://www.luogu.com.cn/problem/P1002
#    区别：类似不同路径，但马的位置不能走
#    算法：二维动态规划，注意避开马的控制点
#
# 10. 牛客网 NC14552 方格取数
#    链接：https://ac.nowcoder.com/acm/problem/14552
#    区别：两个人同时从左上角出发到右下角取数，求最大和
#    算法：三维动态规划，状态定义为dp[k][x1][x2]，表示走了k步，第一个人在(x1,k-x1)，第二个人在(x2,k-x2)
#
# 11. UVa 10913 - Walking on a Grid
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
#    区别：在网格中行走，有负数，最多允许k次负数
#    算法：四维动态规划，状态定义为dp[i][j][k][d]，表示在(i,j)位置，已经经过k次负数，方向为d
#
# 12. SPOJ - SBANK
#    链接：https://www.spoj.com/problems/SBANK/
#    区别：银行账号排序问题，使用哈希表优化
#    算法：哈希表+排序
#
# 13. HackerEarth - Roy and Coin Boxes
#    链接：https://www.hackerearth.com/practice/data-structures/arrays/1-d/practice-problems/algorithm/roy-and-coin-boxes-1/description/
#    区别：区间更新问题，使用差分数组优化
#    算法：差分数组+前缀和
#
# 14. 杭电 HDU 1024 - Max Sum Plus Plus
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1024
#    区别：最大m段子数组和问题
#    算法：二维动态规划优化为一维
#
# 15. Codeforces 1295E - Permutation Separation
#    链接：https://codeforces.com/problemset/problem/1295/E
#    区别：排列分割问题，需要找到最优分割点
#    算法：前缀和+动态规划
#
# 算法本质与技巧总结：
# 1. 二维动态规划的基本模型：从左上到右下，只能向右或向下移动
# 2. 最优子结构：当前位置的最优解取决于左上两个位置的最优解
# 3. 空间优化方法：使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
# 4. 原地更新：在允许修改输入的情况下，可以直接在原数组上更新以节省空间
# 5. 贪心策略：在每一步选择当前最优的移动方向（向左或向上）
#
# Python工程化实战建议：
# 1. 测试用例设计：
#    - 空网格或单行单列网格
#    - 网格中所有值相同的情况
#    - 网格中有特别大的值，需要测试路径选择
#    - 大型网格测试性能
# 2. 性能优化：
#    - 空间优化：使用一维数组进行滚动更新
#    - 原地更新：直接在输入数组上修改以节省空间
#    - 计算优化：避免重复计算
# 3. 代码健壮性：
#    - 添加输入合法性检查，如空网格或无效尺寸
#    - 使用try-except块处理可能的异常
#    - 添加断言验证关键条件
# 4. 调试技巧：
#    - 添加打印语句输出中间状态
#    - 使用assert语句验证关键条件
#    - 考虑使用Python调试器pdb进行断点调试
# 5. Python特性应用：
#    - 使用类型注解提高代码可读性和IDE支持
#    - 考虑使用functools.lru_cache实现记忆化搜索版本
#    - 利用Python的生成器表达式和列表推导式简化代码
# 6. 跨语言实现对比：
#    - 在C++中，可以使用二维数组或vector<vector<int>>
#    - 在Java中，可以使用二维int数组
#    - 不同语言在处理大整数时的差异需要注意