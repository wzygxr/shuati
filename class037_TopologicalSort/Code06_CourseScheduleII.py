#!/usr/bin/env python3

"""
课程表 II (Python版本)
现在你总共有 numCourses 门课需要选，记为 0 到 numCourses - 1。
给你一个数组 prerequisites ，其中 prerequisites[i] = [ai, bi] ，
表示在选修课程 ai 前必须先选修 bi。
请你返回你为了学完所有课程所安排的学习顺序。可能会有多个正确的顺序，你只要返回任意一种就可以。
如果不可能完成所有课程，返回一个空数组。
测试链接 : https://leetcode.cn/problems/course-schedule-ii/

算法思路：
这是课程表问题的升级版，不仅需要判断是否能完成所有课程，还需要返回一个可行的学习顺序。
我们可以使用拓扑排序来解决这个问题。

解法：Kahn算法（BFS）
1. 构建邻接表表示的图和入度数组
2. 将所有入度为0的节点加入队列
3. 不断从队列中取出节点，将其加入结果数组，并将其所有邻居的入度减1
4. 如果邻居的入度减为0，则加入队列
5. 如果结果数组的长度等于总节点数，说明可以完成所有课程，返回结果数组；否则返回空数组

时间复杂度：O(N + M)，其中N是课程数，M是先修关系数
空间复杂度：O(N + M)

相关题目扩展：
1. LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
2. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
3. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
4. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
5. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
6. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
7. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
8. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
9. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
10. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e

工程化考虑：
1. 边界处理：处理课程数为0、先修关系为空等特殊情况
2. 输入验证：验证课程编号的有效性
3. 内存优化：合理使用列表和字典
4. 异常处理：对非法输入进行检查
5. 可读性：添加详细注释和变量命名

算法要点：
1. 拓扑排序可以得到一个满足依赖关系的线性序列
2. Kahn算法通过不断移除入度为0的节点来实现拓扑排序
3. 如果图中存在环，则无法进行拓扑排序
4. 结果数组的长度可以用来判断是否存在环
5. 拓扑排序的结果可能不唯一
"""

from collections import defaultdict, deque

class Solution:
    def findOrder(self, numCourses: int, prerequisites: list[list[int]]) -> list[int]:
        """
        使用拓扑排序返回课程学习顺序
        
        Args:
            numCourses: 课程总数
            prerequisites: 先修关系数组
            
        Returns:
            课程学习顺序，如果无法完成所有课程则返回空数组
        """
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 计算每个节点的入度
        indegree = [0] * numCourses
        for edge in prerequisites:
            # edge[1] -> edge[0]，即学习edge[0]前必须学习edge[1]
            graph[edge[1]].append(edge[0])
            indegree[edge[0]] += 1
        
        # 拓扑排序使用的队列
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(numCourses):
            if indegree[i] == 0:
                queue.append(i)
        
        # 存储拓扑排序结果
        result = []
        
        # 拓扑排序过程
        while queue:
            # 取出队首元素
            cur = queue.popleft()
            # 将当前节点加入结果数组
            result.append(cur)
            
            # 遍历当前节点的所有邻居
            for next_node in graph[cur]:
                # 将邻居节点的入度减1
                indegree[next_node] -= 1
                # 如果邻居节点的入度变为0，则加入队列
                if indegree[next_node] == 0:
                    queue.append(next_node)
        
        # 如果处理的节点数等于总节点数，说明可以完成所有课程，返回结果数组；否则返回空数组
        return result if len(result) == numCourses else []

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 可以完成
    prerequisites1 = [[1, 0]]
    result1 = solution.findOrder(2, prerequisites1)
    print("测试用例1: ", end="")
    if not result1:
        print("无法完成所有课程")
    else:
        print("学习顺序:", " ".join(map(str, result1)))
    
    # 测试用例2: 无法完成（存在循环依赖）
    prerequisites2 = [[1, 0], [0, 1]]
    result2 = solution.findOrder(2, prerequisites2)
    print("测试用例2: ", end="")
    if not result2:
        print("无法完成所有课程")
    else:
        print("学习顺序:", " ".join(map(str, result2)))
    
    # 测试用例3: 复杂情况
    prerequisites3 = [[1, 0], [2, 0], [3, 1], [3, 2]]
    result3 = solution.findOrder(4, prerequisites3)
    print("测试用例3: ", end="")
    if not result3:
        print("无法完成所有课程")
    else:
        print("学习顺序:", " ".join(map(str, result3)))