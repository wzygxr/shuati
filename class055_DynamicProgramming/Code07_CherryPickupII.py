"""
樱桃采摘II问题 - Python实现
算法思路：三维动态规划
时间复杂度：O(rows * cols^2)
空间复杂度：O(rows * cols^2)，可优化到O(cols^2)

核心思想：
1. 使用三维DP数组dp[i][j1][j2]表示当机器人1在(i,j1)，机器人2在(i,j2)时能收集的最大樱桃数
2. 状态转移：考虑两个机器人从前一行的9种可能位置组合转移而来
3. 关键优化：如果两个机器人在同一位置，只计算一次樱桃数量
"""

from typing import List
import sys

class CherryPickupII:
    """
    樱桃采摘II问题解决方案
    """
    
    def cherryPickup(self, grid: List[List[int]]) -> int:
        """
        计算两个机器人能收集的最大樱桃数
        
        Args:
            grid: 二维矩阵，表示樱桃田
            
        Returns:
            int: 最大樱桃数
        """
        rows = len(grid)
        if rows == 0:
            return 0
        cols = len(grid[0])
        
        # 输入验证
        if cols == 0:
            return 0
        
        # 创建三维DP数组，初始化为-1表示未访问
        dp = [[[-1] * cols for _ in range(cols)] for _ in range(rows)]
        
        # 初始化起始位置
        dp[0][0][cols - 1] = grid[0][0] + grid[0][cols - 1]
        
        # 填充DP表
        for i in range(1, rows):
            for j1 in range(cols):
                for j2 in range(cols):
                    # 计算当前位置的樱桃数
                    cherries = grid[i][j1] if j1 == j2 else grid[i][j1] + grid[i][j2]
                    
                    # 检查所有可能的前驱状态
                    for p1 in range(max(0, j1 - 1), min(cols, j1 + 2)):
                        for p2 in range(max(0, j2 - 1), min(cols, j2 + 2)):
                            # 检查前驱状态是否有效
                            if dp[i - 1][p1][p2] != -1:
                                current_val = dp[i - 1][p1][p2] + cherries
                                if dp[i][j1][j2] < current_val:
                                    dp[i][j1][j2] = current_val
        
        # 找到最后一行的最大值
        result = 0
        for j1 in range(cols):
            for j2 in range(cols):
                if dp[rows - 1][j1][j2] > result:
                    result = dp[rows - 1][j1][j2]
        
        return result
    
    def cherryPickupOptimized(self, grid: List[List[int]]) -> int:
        """
        空间优化版本：使用滚动数组将空间复杂度优化到O(cols^2)
        
        Args:
            grid: 二维矩阵
            
        Returns:
            int: 最大樱桃数
        """
        rows = len(grid)
        if rows == 0:
            return 0
        cols = len(grid[0])
        
        # 输入验证
        if cols == 0:
            return 0
        
        # 使用滚动数组，只需要保存当前行和上一行的状态
        prev = [[-1] * cols for _ in range(cols)]
        curr = [[-1] * cols for _ in range(cols)]
        
        # 初始化起始位置
        prev[0][cols - 1] = grid[0][0] + grid[0][cols - 1]
        
        for i in range(1, rows):
            # 清空当前行状态
            for j1 in range(cols):
                for j2 in range(cols):
                    curr[j1][j2] = -1
            
            for j1 in range(cols):
                for j2 in range(cols):
                    # 计算当前位置的樱桃数
                    cherries = grid[i][j1] if j1 == j2 else grid[i][j1] + grid[i][j2]
                    
                    # 检查所有可能的前驱状态
                    for p1 in range(max(0, j1 - 1), min(cols, j1 + 2)):
                        for p2 in range(max(0, j2 - 1), min(cols, j2 + 2)):
                            if prev[p1][p2] != -1:
                                current_val = prev[p1][p2] + cherries
                                if curr[j1][j2] < current_val:
                                    curr[j1][j2] = current_val
            
            # 交换prev和curr
            prev, curr = curr, prev
        
        # 找到最大值
        result = 0
        for j1 in range(cols):
            for j2 in range(cols):
                if prev[j1][j2] > result:
                    result = prev[j1][j2]
        
        return result
    
    def test(self):
        """
        单元测试函数
        """
        # 测试用例1
        grid1 = [
            [3, 1, 1],
            [2, 5, 1],
            [1, 5, 5]
        ]
        result1 = self.cherryPickup(grid1)
        print(f"Test 1 - Basic: {result1} (Expected: 21)")
        
        # 测试用例2
        grid2 = [
            [1, 0, 0, 0, 0, 0, 1],
            [0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0],
            [1, 0, 0, 0, 0, 0, 1]
        ]
        result2 = self.cherryPickup(grid2)
        print(f"Test 2 - Large Grid: {result2} (Expected: 8)")
        
        # 测试用例3：边界情况 - 单行
        grid3 = [
            [1, 0, 1]
        ]
        result3 = self.cherryPickup(grid3)
        print(f"Test 3 - Single Row: {result3} (Expected: 2)")
        
        # 测试优化版本
        result1Opt = self.cherryPickupOptimized(grid1)
        print(f"Test 1 - Optimized: {result1Opt} (Expected: 21)")
        
        # 性能测试：大规模数据
        largeGrid = [[1] * 50 for _ in range(50)]
        largeGrid[0][0] = 0
        largeGrid[0][49] = 0
        
        print("Performance test started...")
        largeResult = self.cherryPickupOptimized(largeGrid)
        print(f"Performance test completed. Result: {largeResult}")

def main():
    """
    主函数
    """
    solution = CherryPickupII()
    
    # 运行单元测试
    solution.test()

if __name__ == "__main__":
    main()

"""
相关题目扩展：

1. LeetCode 741 - Cherry Pickup (摘樱桃 I)
   链接：https://leetcode.cn/problems/cherry-pickup/
   区别：一个人从(0,0)走到(n-1,n-1)再走回(0,0)，求最大收集樱桃数
   算法：三维动态规划，转化为两个人同时从起点到终点的问题

2. LeetCode 64 - Minimum Path Sum (最小路径和)
   链接：https://leetcode.cn/problems/minimum-path-sum/
   区别：求从左上角到右下角的最小路径和，每步只能向下或向右
   算法：二维动态规划

3. LeetCode 174 - Dungeon Game (地下城游戏)
   链接：https://leetcode.cn/problems/dungeon-game/
   区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
   算法：从右下角向左上角动态规划

4. LeetCode 62 - Unique Paths (不同路径)
   链接：https://leetcode.cn/problems/unique-paths/
   区别：计算从左上角到右下角的不同路径数量
   算法：组合数学或二维动态规划

5. LeetCode 63 - Unique Paths II (不同路径 II)
   链接：https://leetcode.cn/problems/unique-paths-ii/
   区别：网格中有障碍物，计算不同路径数量
   算法：二维动态规划，遇到障碍物时dp[i][j] = 0

6. Codeforces 1296D - Fight with Monsters
   链接：https://codeforces.com/problemset/problem/1296/D
   区别：贪心策略解决怪物战斗问题
   算法：排序后贪心选择最优策略

7. AtCoder ABC159E - Dividing Chocolate
   链接：https://atcoder.jp/contests/abc159/tasks/abc159_e
   区别：二维网格分割问题
   算法：前缀和+状态压缩动态规划

8. 洛谷 P1434 - [SHOI2002] 滑雪
   链接：https://www.luogu.com.cn/problem/P1434
   区别：寻找最长滑雪路径
   算法：记忆化搜索或拓扑排序动态规划

9. 牛客网 NC14552 - 方格取数
   链接：https://ac.nowcoder.com/acm/problem/14552
   区别：与摘樱桃I非常相似
   算法：三维动态规划

10. UVa 10913 - Walking on a Grid
    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
    区别：在网格中行走，有负数，最多允许k次负数
    算法：四维动态规划

算法技巧总结：
1. 三维状态设计：同时跟踪两个机器人的位置
2. 状态转移优化：考虑所有可能的前驱状态组合
3. 避免重复计算：同一位置的樱桃只计算一次
4. 边界条件处理：仔细检查机器人移动是否越界
5. 空间优化：使用滚动数组优化空间复杂度

Python工程化考量：
1. 类型注解：使用typing模块提高代码可读性
2. 性能优化：使用列表推导式代替循环
3. 内存管理：Python有自动垃圾回收，但要注意避免循环引用
4. 代码可读性：使用有意义的变量名和注释
5. 测试覆盖：包含边界测试、性能测试

调试技巧：
1. 使用print语句输出中间变量值
2. 使用pdb调试器进行调试
3. 编写单元测试验证算法正确性
4. 使用性能分析工具优化代码效率

Python语言特性：
1. 列表推导式：可以简化数组初始化
2. 切片操作：可以方便地处理数组范围
3. 内置函数：max、min、range等函数优化了性能
4. 动态类型：不需要声明变量类型，但建议使用类型注解

异常处理：
1. 输入验证：检查矩阵是否为空
2. 边界检查：确保索引不越界
3. 类型检查：确保输入参数类型正确
4. 错误处理：使用try-except处理可能的异常
"""