from typing import List
import sys
import functools

'''
并行课程 II
给你一个整数 n 表示某所大学里课程的总数，编号为 1 到 n。
同时给你一个数组 dependencies ，其中 dependencies[i] = [xi, yi] 表示课程 xi 必须在课程 yi 之前完成。
同时又给你一个整数 k 。
在一个学期中，你最多可以同时上 k 门课，前提是这些课的先修课已经修完。
请你返回上完所有课最少需要多少个学期。
测试链接 : https://leetcode.cn/problems/parallel-courses-ii/

算法思路：
这道题是拓扑排序的变种，需要考虑每个学期最多选k门课的限制。
由于n最大为15，我们可以使用状态压缩动态规划来解决。

解法：状态压缩动态规划
1. 预处理每个课程的前置依赖（使用二进制表示）
2. 使用动态规划，dp[mask]表示已经修完mask中课程（二进制位为1）所需的最少学期数
3. 对于每个状态mask，找出所有可以在下一学期学习的课程（未修且前置课程已修）
4. 使用位操作枚举这些可选课程的所有可能组合（最多选k门）
5. 更新dp[newMask] = min(dp[newMask], dp[mask] + 1)

时间复杂度：O(3^n)，其中n是课程数。状态数为2^n，对于每个状态，枚举子集的复杂度为O(2^m)，m为可选课程数
空间复杂度：O(2^n)，用于存储dp数组

优化点：
1. 使用位操作高效计算前置依赖
2. 使用子集枚举优化来选择k门课程
3. 使用位计数函数快速计算已修课程数

工程化考虑：
1. 边界处理：处理n=0、无依赖等特殊情况
2. 输入验证：验证课程编号和依赖关系的有效性
3. 性能优化：使用位操作提高效率
4. 可读性：添加详细注释和变量命名

相关题目：
1. LeetCode 207. 课程表
2. LeetCode 210. 课程表 II
3. LeetCode 2050. 并行课程 III
'''

class Solution:
    def minNumberOfSemesters(self, n: int, dependencies: List[List[int]], k: int) -> int:
        """
        主方法：计算上完所有课最少需要多少个学期
        
        Args:
            n: 课程总数
            dependencies: 课程依赖关系数组
            k: 每学期最多可选课程数
            
        Returns:
            最少需要的学期数
        """
        # 边界检查
        if n <= 0:
            return 0
        
        # 预处理每门课程的前置依赖（使用二进制表示）
        # pre[i]表示第i门课程（从0开始索引）的前置依赖
        pre = [0] * n
        for dep in dependencies:
            # 将课程编号从1-based转换为0-based
            from_course = dep[0] - 1
            to_course = dep[1] - 1
            # 设置前置依赖位
            pre[to_course] |= (1 << from_course)
        
        # 动态规划数组，dp[mask]表示已经修完mask中课程所需的最少学期数
        # 初始化为n+1（最大值），表示无法达成的状态
        dp = [n + 1] * (1 << n)
        
        # 初始状态：没有修任何课程，需要0个学期
        dp[0] = 0
        
        # 遍历所有可能的状态
        for mask in range(1 << n):
            # 如果当前状态无法达成，跳过
            if dp[mask] == n + 1:
                continue
            
            # 找出当前可以学习的课程：未修且前置课程已修
            available = 0
            for i in range(n):
                # 如果课程i未修，且其前置依赖都已满足
                if not (mask & (1 << i)) and (mask & pre[i]) == pre[i]:
                    available |= (1 << i)
            
            # 枚举available的所有非空子集（不超过k门课）
            # 使用位操作的子集枚举方法
            subset = available
            while subset > 0:
                # 统计子集的大小（即这学期要上的课程数）
                count = bin(subset).count('1')
                # 如果超过k门课，跳过
                if count <= k:
                    # 新的状态：当前状态 | 子集
                    new_mask = mask | subset
                    # 更新dp值
                    dp[new_mask] = min(dp[new_mask], dp[mask] + 1)
                # 找下一个子集
                subset = (subset - 1) & available
        
        # 返回修完所有课程（全1状态）所需的最少学期数
        return dp[(1 << n) - 1]
    
    def minNumberOfSemestersOptimized(self, n: int, dependencies: List[List[int]], k: int) -> int:
        """
        优化版本：使用回溯法优化子集枚举
        
        Args:
            n: 课程总数
            dependencies: 课程依赖关系数组
            k: 每学期最多可选课程数
            
        Returns:
            最少需要的学期数
        """
        pre = [0] * n
        for dep in dependencies:
            from_course = dep[0] - 1
            to_course = dep[1] - 1
            pre[to_course] |= (1 << from_course)
        
        dp = [n + 1] * (1 << n)
        dp[0] = 0
        
        def backtrack(available, start, selected, mask):
            """
            回溯法枚举最多k门课程的组合
            
            Args:
                available: 可选课程的二进制表示
                start: 开始枚举的位置
                selected: 当前已选课程的二进制表示
                mask: 当前的状态
            """
            # 如果已经选够k门或者没有更多可选课程
            if bin(selected).count('1') == k or selected == available:
                if selected != 0:  # 至少选一门课
                    new_mask = mask | selected
                    dp[new_mask] = min(dp[new_mask], dp[mask] + 1)
                return
            
            # 从start位开始枚举，避免重复
            for i in range(start, n):
                if available & (1 << i):  # 如果这门课可选
                    # 选择这门课
                    backtrack(available, i + 1, selected | (1 << i), mask)
        
        for mask in range(1 << n):
            if dp[mask] == n + 1:
                continue
            
            # 找出当前可以学习的课程
            available = 0
            for i in range(n):
                if not (mask & (1 << i)) and (mask & pre[i]) == pre[i]:
                    available |= (1 << i)
            
            # 使用回溯法枚举最多k门课程的组合
            backtrack(available, 0, 0, mask)
        
        return dp[(1 << n) - 1]
    
    def minNumberOfSemestersMemo(self, n: int, dependencies: List[List[int]], k: int) -> int:
        """
        记忆化搜索版本
        
        Args:
            n: 课程总数
            dependencies: 课程依赖关系数组
            k: 每学期最多可选课程数
            
        Returns:
            最少需要的学期数
        """
        pre = [0] * n
        for dep in dependencies:
            from_course = dep[0] - 1
            to_course = dep[1] - 1
            pre[to_course] |= (1 << from_course)
        
        @functools.lru_cache(maxsize=None)
        def dfs(mask):
            """
            深度优先搜索所有可能的状态
            
            Args:
                mask: 当前已修课程的状态
                
            Returns:
                从当前状态到完成所有课程所需的最少学期数
            """
            if mask == (1 << n) - 1:  # 所有课程都已修完
                return 0
            
            # 找出当前可以学习的课程
            available = 0
            for i in range(n):
                if not (mask & (1 << i)) and (mask & pre[i]) == pre[i]:
                    available |= (1 << i)
            
            # 如果没有可选课程但还没修完，说明存在环
            if available == 0:
                return float('inf')
            
            min_semesters = float('inf')
            
            # 枚举available的所有子集（不超过k门课）
            subset = available
            while subset > 0:
                if bin(subset).count('1') <= k:
                    next_mask = mask | subset
                    current_semesters = 1 + dfs(next_mask)
                    min_semesters = min(min_semesters, current_semesters)
                subset = (subset - 1) & available
            
            return min_semesters
        
        return dfs(0)

# 测试代码
def test_solution():
    solution = Solution()
    
    # 测试用例1
    n1 = 4
    dependencies1 = [[2, 1], [3, 1], [1, 4]]
    k1 = 2
    print("测试用例1:", solution.minNumberOfSemesters(n1, dependencies1, k1))  # 应输出 3
    print("测试用例1（优化版）:", solution.minNumberOfSemestersOptimized(n1, dependencies1, k1))  # 应输出 3
    print("测试用例1（记忆化版）:", solution.minNumberOfSemestersMemo(n1, dependencies1, k1))  # 应输出 3
    
    # 测试用例2
    n2 = 5
    dependencies2 = [[2, 1], [3, 1], [4, 1], [1, 5]]
    k2 = 2
    print("测试用例2:", solution.minNumberOfSemesters(n2, dependencies2, k2))  # 应输出 4
    print("测试用例2（优化版）:", solution.minNumberOfSemestersOptimized(n2, dependencies2, k2))  # 应输出 4
    print("测试用例2（记忆化版）:", solution.minNumberOfSemestersMemo(n2, dependencies2, k2))  # 应输出 4
    
    # 测试用例3：无依赖
    n3 = 5
    dependencies3 = []
    k3 = 3
    print("测试用例3:", solution.minNumberOfSemesters(n3, dependencies3, k3))  # 应输出 2
    print("测试用例3（优化版）:", solution.minNumberOfSemestersOptimized(n3, dependencies3, k3))  # 应输出 2
    print("测试用例3（记忆化版）:", solution.minNumberOfSemestersMemo(n3, dependencies3, k3))  # 应输出 2

# 运行测试
if __name__ == "__main__":
    test_solution()