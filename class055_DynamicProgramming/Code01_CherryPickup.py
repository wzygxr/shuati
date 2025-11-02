# 摘樱桃
# 给定一个n*n的正方形矩阵grid，每个格子值只有三种-1、0、1
# -1表示格子不能走、0表示格子可以走但是没有樱桃、1表示格子可以走且有一颗樱桃
# 你的目标是从左上角走到右下角，每一步只能 向下 或者 向右
# 然后从右下角走回左上角，每一步只能 向上 或者 向左
# 这个过程中，想尽量多的获得樱桃，但是有樱桃的格子，只能捡一次
# 返回最多能获得多少樱桃，如果不存在通路返回0
# 测试链接 : https://leetcode.cn/problems/cherry-pickup/

"""
算法思路深度解析：
1. 这道题可以看作是两个人同时从(0,0)出发，走到(n-1,n-1)位置能收集的最大樱桃数
   - 这种转化是关键，因为来回走一次等价于两个人同时从起点走到终点
   - 这样可以避免处理重复访问的问题
2. 使用三维动态规划，在Python中采用字典模拟三维数组，优化空间使用
   - i表示第一个人的横坐标
   - j表示第一个人的纵坐标
   - k表示第二个人的横坐标
   - 第二个人的纵坐标可以由d = i + j - k计算得出（因为两人走的步数相同）
   - 这个状态定义是一种空间优化，将四维问题转化为三维问题
3. 每个状态可以从四个前驱状态转移而来：
   - 两人都向右：dp[i][j-1][k]
   - 第一人向右，第二人向下：dp[i][j-1][k-1]
   - 第一人向下，第二人向右：dp[i-1][j][k]
   - 两人都向下：dp[i-1][j][k-1]
4. 关键优化点：
   - 如果两人在同一个格子，只计算一次樱桃数量
   - 使用记忆化搜索避免重复计算
   - Python中使用字典存储已计算结果，减少空间占用

时间复杂度分析：
- 状态数：O(n^3)，其中n是矩阵的边长
- 每个状态需要考虑4个前驱状态
- 总时间复杂度：O(n^3)

空间复杂度分析：
- 字典存储的已计算状态：最坏情况O(n^3)
- 递归调用栈深度：O(n)
- 总空间复杂度：O(n^3)

Python实现注意事项：
1. 使用字典而不是列表实现记忆化搜索，更节省空间
2. 递归函数中需要注意参数顺序和边界条件
3. Python递归深度限制：对于n较大的情况，可能需要调整递归深度或改用迭代方式
4. 类型注解的使用提高代码可读性

工程化考量：
1. 边界条件处理：确保不越界，检查障碍物
2. 异常处理：处理无法到达终点的情况
3. 测试用例：添加全面的测试覆盖
4. 性能优化：对于大规模数据，可以考虑使用lru_cache装饰器或迭代版本

常见问题排查：
1. 递归深度问题：Python默认递归深度限制为1000，对于大n值可能抛出RecursionError
2. 边界越界：确保所有坐标都在有效范围内
3. 障碍物处理：正确处理无法通过的格子
4. 结果判断：当ans为-1时返回0表示不存在通路
"""

from typing import List

class Solution:
    def cherryPickup(self, grid: List[List[int]]) -> int:
        """
        计算能够摘到的最大樱桃数
        :param grid: n*n的正方形矩阵
        :return: 最多能获得的樱桃数，如果不存在通路返回0
        """
        n = len(grid)
        # dp[i][j][k]表示第一个人在(i,j)，第二个人在(k, i+j-k)时能摘到的最大樱桃数
        # 使用字典来模拟三维数组，避免初始化大型数组
        dp = {}
        
        def f(a: int, b: int, c: int) -> int:
            """
            递归函数，计算从(0,0)到(n-1,n-1)两人能摘到的最大樱桃数
            :param a: 第一个人的横坐标
            :param b: 第一个人的纵坐标
            :param c: 第二个人的横坐标
            :return: 能摘到的最大樱桃数，如果无法到达终点返回-1
            """
            # 计算第二个人的纵坐标
            d = a + b - c
            # 边界条件检查
            if a == n or b == n or c == n or d == n or grid[a][b] == -1 or grid[c][d] == -1:
                return -1
            # 到达终点
            if a == n - 1 and b == n - 1:
                return grid[a][b]
            # 如果已经计算过，直接返回结果
            if (a, b, c) in dp:
                return dp[(a, b, c)]
            
            # 计算当前位置的樱桃数
            get = grid[a][b] if a == c and b == d else (grid[a][b] + grid[c][d])
            # 四种可能的移动方式
            next_val = f(a + 1, b, c + 1)  # 两人都向下
            next_val = max(next_val, f(a + 1, b, c))  # 第一人向下，第二人向右
            next_val = max(next_val, f(a, b + 1, c + 1))  # 第一人向右，第二人向下
            next_val = max(next_val, f(a, b + 1, c))  # 两人都向右
            
            ans = -1
            if next_val != -1:
                ans = get + next_val
                
            dp[(a, b, c)] = ans
            return ans
        
        ans = f(0, 0, 0)
        return 0 if ans == -1 else ans

# 类似题目与训练拓展：
"""
以下是与摘樱桃问题相关的15个类似题目，涵盖了多种算法平台
这些题目展示了动态规划、状态压缩、路径优化等相关技巧的应用
"""

# 1. LeetCode 1463. Cherry Pickup II (摘樱桃 II)
#    链接：https://leetcode.cn/problems/cherry-pickup-ii/
#    区别：两个机器人分别从(0,0)和(0, cols-1)出发，只能向下移动，每步可选择三个方向
#    算法：三维动态规划，状态定义为dp[i][j1][j2]，表示两个机器人在第i行第j1和j2列的最大樱桃数

# 2. LeetCode 64. Minimum Path Sum (最小路径和)
#    链接：https://leetcode.cn/problems/minimum-path-sum/
#    区别：求从左上角到右下角的最小路径和，每步只能向下或向右
#    算法：二维动态规划，dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]

# 3. LeetCode 174. Dungeon Game (地下城游戏)
#    链接：https://leetcode.cn/problems/dungeon-game/
#    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
#    算法：从右下角向左上角动态规划，dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])

# 4. LeetCode 741. Cherry Pickup (本题)
#    链接：https://leetcode.cn/problems/cherry-pickup/
#    算法：三维动态规划，转化为两人同时从起点到终点的问题

# 5. LeetCode 62. Unique Paths (不同路径)
#    链接：https://leetcode.cn/problems/unique-paths/
#    区别：计算从左上角到右下角的不同路径数量，每步只能向下或向右
#    算法：组合数学或二维动态规划

# 6. LeetCode 63. Unique Paths II (不同路径 II)
#    链接：https://leetcode.cn/problems/unique-paths-ii/
#    区别：网格中有障碍物，计算不同路径数量
#    算法：二维动态规划，遇到障碍物时dp[i][j] = 0

# 7. Codeforces 1296D - Fight with Monsters
#    链接：https://codeforces.com/problemset/problem/1296/D
#    区别：贪心策略解决怪物战斗问题，但状态转移思想类似
#    算法：排序后贪心选择最优策略

# 8. AtCoder ABC159E - Dividing Chocolate
#    链接：https://atcoder.jp/contests/abc159/tasks/abc159_e
#    区别：二维网格分割问题，但需要类似的状态转移思路
#    算法：前缀和+状态压缩动态规划

# 9. 洛谷 P1434 [SHOI2002] 滑雪
#    链接：https://www.luogu.com.cn/problem/P1434
#    区别：寻找最长滑雪路径，每步只能滑向相邻四个方向且高度更低的位置
#    算法：记忆化搜索或拓扑排序动态规划

# 10. 牛客网 NC14552 方格取数
#    链接：https://ac.nowcoder.com/acm/problem/14552
#    区别：与摘樱桃问题非常相似，也是两个人从左上角出发到右下角取数
#    算法：三维动态规划，状态定义与本题类似

# 11. UVa 10913 - Walking on a Grid
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
#    区别：在网格中行走，有负数，最多允许k次负数
#    算法：四维动态规划，状态定义为dp[i][j][k][d]，表示在(i,j)位置，已经经过k次负数，方向为d

# 12. SPOJ - SBANK
#    链接：https://www.spoj.com/problems/SBANK/
#    区别：银行账号排序问题，使用哈希表优化
#    算法：哈希表+排序

# 13. HackerEarth - Roy and Coin Boxes
#    链接：https://www.hackerearth.com/practice/data-structures/arrays/1-d/practice-problems/algorithm/roy-and-coin-boxes-1/description/
#    区别：区间更新问题，使用差分数组优化
#    算法：差分数组+前缀和

# 14. 杭电 HDU 1024 - Max Sum Plus Plus
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1024
#    区别：最大m段子数组和问题
#    算法：二维动态规划优化为一维

# 15. Codeforces 1295E - Permutation Separation
#    链接：https://codeforces.com/problemset/problem/1295/E
#    区别：排列分割问题，需要找到最优分割点
#    算法：前缀和+动态规划

"""
算法本质与技巧总结：

1. 问题转化：将"来回走一次"转化为"两个人同时走一次"，这是解决这类问题的关键技巧
   - 这种转化避免了处理重复访问问题，简化了状态定义
   - 可以扩展到更多类似路径优化问题
   - 转化思维体现了算法设计中的问题重构能力

2. 状态压缩：利用步数相同的特性，将四维状态压缩为三维
   - 这种优化在路径规划问题中非常常见
   - 状态压缩可以显著减少空间复杂度
   - 基于问题特性的状态压缩是高级动态规划的重要技巧

3. 记忆化搜索：避免重复计算，提高效率
   - 递归实现的记忆化搜索比迭代方式更直观
   - 对于复杂状态转移方程的问题，记忆化搜索更易实现
   - 在Python中可以使用字典或lru_cache实现记忆化

4. 边界处理：仔细处理边界条件和障碍物情况
   - 边界检查在网格类问题中尤为重要
   - 障碍物的处理需要特殊考虑
   - 正确的边界处理是算法正确性的关键

5. 结果判断：处理无法到达终点的异常情况
   - 返回合适的默认值表示无解
   - 异常情况的处理体现了代码的健壮性
"""

"""
Python工程化实战建议：

1. 测试用例设计：
   - 空输入：n=0的情况
   - 单元素网格：只有起点和终点的情况
   - 全是障碍物的情况
   - 无法到达终点的情况
   - 最优路径需要交叉的情况

2. 性能优化策略：
   - 对于大规模数据，可以使用滚动数组将空间复杂度优化到O(n^2)
   - 可以使用递推方式代替递归，避免栈溢出问题
   - Python中使用lru_cache装饰器可以简化记忆化实现
   - 对于大n值，考虑调整递归深度限制或改用迭代方式

3. 代码健壮性提升：
   - 加入输入合法性检查，防止非法输入导致程序崩溃
   - 使用try-except块处理可能的异常
   - 使用类型注解提高代码可读性和可维护性
   - 编写完整的单元测试确保功能正确

4. Python特有优化技巧：
   - 利用字典存储已计算状态，避免初始化大型数组
   - 使用functools.lru_cache替代手动实现的记忆化搜索
   - 针对递归深度限制问题，可以使用sys.setrecursionlimit()临时调整
   - 考虑使用生成器和迭代器优化内存使用

5. 调试与问题定位：
   - 使用print语句打印中间状态值
   - 在关键节点添加断言验证中间结果
   - 使用pdb调试器进行交互式调试
   - 针对大规模数据，使用增量调试策略

6. 跨语言实现对比：
   - Python实现更简洁，但在性能上可能不如C++
   - 字典实现的记忆化搜索比列表更节省空间，但访问速度略慢
   - 递归深度限制是Python实现的一个潜在问题，需要特别注意
   - Java和C++的静态类型检查可以在编译时发现更多错误

7. 工程化与产品化建议：
   - 将算法封装为独立模块，提供清晰的API
   - 添加详细的文档说明使用方法和限制条件
   - 考虑添加配置选项，允许用户自定义算法参数
   - 实现性能监控功能，便于分析和优化

8. 与机器学习的联系：
   - 类似的路径规划问题在强化学习中有广泛应用
   - 动态规划思想是强化学习中价值迭代和策略迭代的基础
   - 可以考虑使用深度强化学习方法解决更大规模的路径规划问题
"""
# 2. 性能优化：
#    - 对于大规模数据，可以考虑使用lru_cache装饰器优化记忆化搜索
#    - 递归深度问题：Python默认递归深度限制为1000，对于大n值可能需要改用迭代方式
#    - 可以使用sys.setrecursionlimit()调整递归深度，但要注意栈溢出风险
# 3. 代码健壮性：
#    - 添加输入合法性检查
#    - 使用try-except块处理可能的异常
#    - 类型注解提高代码可读性
# 4. 调试技巧：
#    - 使用print语句打印中间状态值
#    - 使用断点调试工具
#    - 可以考虑使用pdb调试器进行交互式调试
# 5. Python特性应用：
#    - 利用collections.defaultdict简化字典操作
#    - 使用functools.lru_cache装饰器替代手动实现的记忆化搜索
#    - 考虑使用numpy优化数组操作（如果处理大规模数据）
# 6. 跨语言实现对比：
#    - Python实现更简洁，但在性能上可能不如C++
#    - 字典实现的记忆化搜索比列表更节省空间，但访问速度略慢
#    - 递归深度限制是Python实现的一个潜在问题，需要特别注意

# 测试用例：全面覆盖各种情况
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：基本情况
    grid1 = [[0, 1, -1], [1, 0, -1], [1, 1, 1]]
    print(f"测试用例1结果: {solution.cherryPickup(grid1)}")  # 输出应为5
    
    # 测试用例2：无法到达终点的情况
    grid2 = [[0, -1], [-1, 0]]
    print(f"测试用例2结果: {solution.cherryPickup(grid2)}")  # 输出应为0
    
    # 测试用例3：单元素网格
    grid3 = [[1]]
    print(f"测试用例3结果: {solution.cherryPickup(grid3)}")  # 输出应为1
    
    # 测试用例4：需要交叉路径的情况
    grid4 = [[1,1,1,1,0,0,0],[0,0,0,1,0,0,0],[0,0,0,1,0,0,1],[1,0,0,1,0,0,0],[0,0,0,1,0,0,0],[0,0,0,1,0,0,0],[0,0,0,1,1,1,1]]
    print(f"测试用例4结果: {solution.cherryPickup(grid4)}")  # 复杂情况的测试
    
    # 测试用例5：起点或终点是障碍物
    grid5 = [[-1, 1], [1, 1]]
    print(f"测试用例5结果: {solution.cherryPickup(grid5)}")  # 输出应为0
    
    # 性能优化提示：对于较大的n值，可以考虑以下替代实现
    '''
    # 使用lru_cache装饰器的替代实现
    from functools import lru_cache
    
    class SolutionOptimized:
        def cherryPickup(self, grid: List[List[int]]) -> int:
            n = len(grid)
            
            @lru_cache(maxsize=None)
            def dp(a, b, c):
                d = a + b - c
                if a == n or b == n or c == n or d == n or grid[a][b] == -1 or grid[c][d] == -1:
                    return -1
                if a == n - 1 and b == n - 1:
                    return grid[a][b]
                
                res = max(
                    dp(a+1, b, c+1),
                    dp(a+1, b, c),
                    dp(a, b+1, c+1),
                    dp(a, b+1, c)
                )
                
                if res == -1:
                    return -1
                
                current = grid[a][b] if (a == c and b == d) else (grid[a][b] + grid[c][d])
                return current + res
            
            result = dp(0, 0, 0)
            return 0 if result == -1 else result
    '''