#!/usr/bin/env python3

"""
并行课程 III (Python版本)
给你一个整数 n ，表示有 n 节课，课程编号从 1 到 n
同时给你一个二维整数数组 relations ，
其中 relations[j] = [prevCoursej, nextCoursej]
表示课程 prevCoursej 必须在课程 nextCoursej 之前 完成（先修课的关系）
同时给你一个下标从 0 开始的整数数组 time
其中 time[i] 表示完成第 (i+1) 门课程需要花费的 月份 数。
请你根据以下规则算出完成所有课程所需要的 最少 月份数：
如果一门课的所有先修课都已经完成，你可以在 任意 时间开始这门课程。
你可以 同时 上 任意门课程 。请你返回完成所有课程所需要的 最少 月份数。
注意：测试数据保证一定可以完成所有课程（也就是先修课的关系构成一个有向无环图）
测试链接 : https://leetcode.cn/problems/parallel-courses-iii/

算法思路：
这是一个基于拓扑排序的动态规划问题。由于可以并行上课，我们需要计算每个课程的最早开始时间，
然后加上该课程的学习时间，得到完成该课程的时间。最终答案是所有课程完成时间的最大值。

时间复杂度：O(N + M)，其中 N 是课程数，M 是先修关系数
空间复杂度：O(N + M)

相关题目扩展：
1. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
2. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
3. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
4. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
5. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
7. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
10. 牛客网 课程表 - https://ac.nowcoder.com/acm/problem/24725

工程化考虑：
1. 边界处理：处理没有先修课程的情况
2. 输入验证：验证课程编号和时间数组的有效性
3. 内存优化：合理使用列表和字典
4. 异常处理：对非法输入进行检查
5. 可读性：添加详细注释和变量命名

算法要点：
1. 构建图：将先修关系转换为有向图
2. 计算入度：用于拓扑排序
3. 初始化队列：将入度为0的课程（可以立即开始的课程）加入队列
4. 动态规划：计算每门课程的最早完成时间
5. 更新答案：记录所有课程完成时间的最大值
"""

from collections import defaultdict, deque
from typing import List

class Solution:
    def minimumTime(self, n: int, relations: List[List[int]], time: List[int]) -> int:
        """
        计算完成所有课程所需的最少月份数
        
        Args:
            n: 课程总数
            relations: 先修关系数组，relations[j] = [prevCoursej, nextCoursej]
            time: time[i] 表示完成第 (i+1) 门课程需要的月份数
            
        Returns:
            完成所有课程所需的最少月份数
        """
        # 点 : 1....n
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 计算每个节点的入度
        indegree = [0] * (n + 1)
        for prev_course, next_course in relations:
            graph[prev_course].append(next_course)
            indegree[next_course] += 1
        
        # 拓扑排序使用的队列
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(1, n + 1):
            if indegree[i] == 0:
                queue.append(i)
        
        # cost[i] 表示完成课程 i 的最早时间
        cost = [0] * (n + 1)
        ans = 0
        
        # 拓扑排序过程
        while queue:
            cur = queue.popleft()
            # 1 : time[0]
            # x : time[x-1]
            # 完成当前课程的时间 = 开始时间 + 学习时间
            cost[cur] += time[cur - 1]
            # 更新最大完成时间
            ans = max(ans, cost[cur])
            
            # 遍历当前课程的所有后续课程
            for next_course in graph[cur]:
                # 更新后续课程的最早开始时间：
                # 后续课程的最早开始时间 = max(当前值, 当前课程的完成时间)
                cost[next_course] = max(cost[next_course], cost[cur])
                # 将后续课程的入度减1，如果变为0则加入队列
                indegree[next_course] -= 1
                if indegree[next_course] == 0:
                    queue.append(next_course)
        
        return ans

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    n1 = 3
    relations1 = [[1, 3], [2, 3]]
    time1 = [3, 2, 5]
    result1 = solution.minimumTime(n1, relations1, time1)
    print(f"测试用例1: {result1}")  # 应输出 8
    
    # 测试用例2
    n2 = 5
    relations2 = [[1, 5], [2, 5], [3, 5], [3, 4], [4, 5]]
    time2 = [1, 2, 3, 4, 5]
    result2 = solution.minimumTime(n2, relations2, time2)
    print(f"测试用例2: {result2}")  # 应输出 12