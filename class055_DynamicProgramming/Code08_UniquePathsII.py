# Unique Paths II
# 一个机器人位于一个 m x n 网格的左上角，机器人每次只能向下或者向右移动一步
# 机器人试图到达网格的右下角
# 网格中有障碍物，用 1 表示，空位置用 0 表示
# 计算从左上角到右下角有多少条不同的路径
# 测试链接 : https://leetcode.cn/problems/unique-paths-ii/

"""
算法思路深度解析：
1. 这是一个典型的二维动态规划问题，是Unique Paths I的扩展，但增加了障碍物的处理
   - 问题的核心是识别可达路径并避免障碍物
   - 动态规划的关键在于合理定义状态和转移方程，同时正确处理边界条件
2. 使用二维动态规划 dp[i][j]，其中：
   - dp[i][j] 表示从起点(0,0)到位置(i,j)的不同路径数
   - 状态定义简洁明了，符合动态规划的常规模式
3. 状态转移方程的详细分析：
   - 障碍物处理：如果 obstacleGrid[i][j] == 1，表示有障碍物，dp[i][j] = 0
   - 起点处理：如果 i == 0 且 j == 0，dp[0][0] = 1（基础情况）
   - 边界处理：
     * 第一行(i == 0)：只能从左边来，dp[i][j] = dp[i][j-1]，但如果前面有障碍物则为0
     * 第一列(j == 0)：只能从上面来，dp[i][j] = dp[i-1][j]，但如果上面有障碍物则为0
   - 一般情况：可以从上面或左边来，dp[i][j] = dp[i-1][j] + dp[i][j-1]
4. 初始化策略：
   - 特殊情况检查：如果起点(0,0)或终点(m-1,n-1)有障碍物，直接返回0
   - 逐步初始化：先处理起点，再分别处理第一行和第一列，最后处理内部格子
5. 结果提取：
   - dp[m-1][n-1]即为所求的从起点到终点的不同路径数
   - 如果终点被障碍物阻塞，结果自然为0

时间复杂度分析：
- 状态总数：O(m * n)
- 每个状态的计算是O(1)操作
- 总时间复杂度：O(m * n)

空间复杂度分析：
- 原始实现使用二维数组：O(m * n)
- 可以通过滚动数组优化到O(min(m, n))
- 对于非常大的网格，空间优化尤为重要

Python实现注意事项：
1. 初始化优化：使用列表推导式创建二维数组，简洁高效
2. 边界条件处理：正确处理障碍物对第一行和第一列初始化的影响
3. 早期返回：在检测到起点或终点有障碍物时立即返回0
4. 内存优化：可以使用一维数组进行滚动更新，减少内存消耗
5. 输入验证：确保输入网格非空且为有效尺寸

工程化考量：
1. 异常处理：处理空网格或无效输入的情况
2. 性能优化：对于大型网格，考虑使用滚动数组优化空间
3. 代码可读性：使用清晰的变量名和适当的注释
4. 边界测试：确保算法能正确处理各种边界情况
5. 并行计算：对于超大型网格，可以考虑行或列的并行计算

常见问题排查：
1. 障碍物处理错误：确保在遇到障碍物时路径数正确设为0
2. 边界初始化错误：注意第一行和第一列的障碍物对后续格子的影响
3. 数组索引越界：注意Python的索引从0开始
4. 整数溢出：Python的整数不会溢出，但需要注意数值可能变得非常大
5. 内存限制：对于超大网格，需要使用空间优化版本
"""

from typing import List

class Solution:
    def uniquePathsWithObstacles(self, obstacleGrid: List[List[int]]) -> int:
        """
        计算从左上角到右下角的不同路径数
        :param obstacleGrid: m x n 的网格，1表示障碍物，0表示空位置
        :return: 不同路径的数量
        """
        m = len(obstacleGrid)
        n = len(obstacleGrid[0])
        
        # 如果起点或终点有障碍物，直接返回0
        if obstacleGrid[0][0] == 1 or obstacleGrid[m-1][n-1] == 1:
            return 0
        
        # dp[i][j] 表示从起点到位置(i,j)的不同路径数
        dp = [[0] * n for _ in range(m)]
        
        # 初始化起点
        dp[0][0] = 1
        
        # 初始化第一行：只能从左边来，如果遇到障碍物，则后续都不可达
        for j in range(1, n):
            dp[0][j] = 0 if obstacleGrid[0][j] == 1 else dp[0][j-1]
        
        # 初始化第一列：只能从上面来，如果遇到障碍物，则后续都不可达
        for i in range(1, m):
            dp[i][0] = 0 if obstacleGrid[i][0] == 1 else dp[i-1][0]
        
        # 填充dp表
        for i in range(1, m):
            for j in range(1, n):
                # 如果当前位置有障碍物，则路径数为0
                if obstacleGrid[i][j] == 1:
                    dp[i][j] = 0
                else:
                    # 否则路径数等于从上面和左边来的路径数之和
                    dp[i][j] = dp[i-1][j] + dp[i][j-1]
        
        return dp[m-1][n-1]

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    obstacleGrid1 = [
        [0, 0, 0],
        [0, 1, 0],
        [0, 0, 0]
    ]
    print("测试用例1结果:", solution.uniquePathsWithObstacles(obstacleGrid1))  # 预期输出: 2
    
    # 测试用例2
    obstacleGrid2 = [
        [0, 1],
        [0, 0]
    ]
    print("测试用例2结果:", solution.uniquePathsWithObstacles(obstacleGrid2))  # 预期输出: 1
    
    # 测试用例3
    obstacleGrid3 = [
        [0, 0],
        [1, 1],
        [0, 0]
    ]
    print("测试用例3结果:", solution.uniquePathsWithObstacles(obstacleGrid3))  # 预期输出: 0
    
    # 测试用例4
    obstacleGrid4 = [
        [0, 0, 0, 0],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ]
    print("测试用例4结果:", solution.uniquePathsWithObstacles(obstacleGrid4))  # 预期输出: 10

# 类似题目与训练拓展：
# 1. LeetCode 62. Unique Paths (不同路径)
#    链接：https://leetcode.cn/problems/unique-paths/
#    区别：没有障碍物的网格路径问题
#    算法：组合数学或二维动态规划，dp[i][j] = dp[i-1][j] + dp[i][j-1]
#
# 2. LeetCode 64. Minimum Path Sum (最小路径和)
#    链接：https://leetcode.cn/problems/minimum-path-sum/
#    区别：求从左上角到右下角的最小路径和，每步只能向下或向右
#    算法：二维动态规划，dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
#
# 3. LeetCode 174. Dungeon Game (地下城游戏)
#    链接：https://leetcode.cn/problems/dungeon-game/
#    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
#    算法：从右下角向左上角动态规划，dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
#
# 4. LeetCode 63. Unique Paths II (本题)
#    链接：https://leetcode.cn/problems/unique-paths-ii/
#    算法：二维动态规划，处理障碍物的路径计数问题
#
# 5. LeetCode 688. Knight Probability in Chessboard
#    链接：https://leetcode.cn/problems/knight-probability-in-chessboard/
#    区别：国际象棋骑士移动，计算留在棋盘内的概率
#    算法：动态规划，dp[k][r][c]表示k步后在位置(r,c)的概率
#
# 6. LeetCode 980. Unique Paths III
#    链接：https://leetcode.cn/problems/unique-paths-iii/
#    区别：需要访问所有非障碍物格子的路径数
#    算法：回溯+剪枝或位掩码动态规划
#
# 7. Codeforces 1296D - Fight with Monsters
#    链接：https://codeforces.com/problemset/problem/1296/D
#    区别：贪心策略解决怪物战斗问题，但状态转移思想类似
#    算法：排序后贪心选择最优策略
#
# 8. AtCoder ABC159E - Dividing Chocolate
#    链接：https://atcoder.jp/contests/abc159/tasks/abc159_e
#    区别：二维网格分割问题，但需要类似的状态转移思路
#    算法：前缀和+状态压缩动态规划
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
# 2. 障碍物处理技巧：遇到障碍物时路径数置为0
# 3. 边界条件处理：第一行和第一列需要特别处理
# 4. 空间优化方法：使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
# 5. 早期检测优化：快速判断起点或终点是否被阻塞
#
# Python工程化实战建议：
# 1. 测试用例设计：
#    - 空网格或单行单列网格
#    - 起点或终点有障碍物的情况
#    - 障碍物阻断所有路径的情况
#    - 网格中有大量障碍物的情况
# 2. 性能优化：
#    - 空间优化：使用一维数组进行滚动更新
#    - 计算优化：对于非常大的网格，可以使用组合数学方法结合障碍物检查
#    - 内存优化：使用生成器表达式代替列表推导式减少内存占用
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
#    - 利用Python的列表推导式简化初始化代码
# 6. 跨语言实现对比：
#    - 在C++中，可以使用二维数组或vector<vector<int>>
#    - 在Java中，可以使用二维int数组，注意整数溢出问题
#    - 不同语言在处理大整数时的差异需要注意