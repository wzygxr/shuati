#!/usr/bin/env python3

"""
确定比赛名次 (Python版本)
有N个队参加比赛，第i队的编号为i (1 <= i <= N)
给出M个关系，每个关系形如 a b，表示a队的名次比b队高（即a队排在b队前面）
请你确定所有队伍的名次，要求：
1. 符合所有给定的关系
2. 名次越小表示排名越高
3. 如果有多种可能的排名，输出字典序最小的那个
测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1285

算法思路：
这是一个字典序最小拓扑排序问题。我们需要在满足拓扑排序的前提下，使得输出的序列字典序最小。

解法：优先队列优化的Kahn算法
1. 构建邻接表表示的图和入度数组
2. 将所有入度为0的节点加入优先队列（最小堆）
3. 不断从优先队列中取出编号最小的节点，将其加入结果数组，并将其所有邻居的入度减1
4. 如果邻居的入度减为0，则加入优先队列
5. 重复步骤3-4直到优先队列为空

时间复杂度：O(N*logN + M)，其中N是队伍数，M是关系数
空间复杂度：O(N + M)

相关题目扩展：
1. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
2. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
3. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
4. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
5. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
6. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
7. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
9. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
10. 洛谷 B3644 【模板】拓扑排序 - https://www.luogu.com.cn/problem/B3644

工程化考虑：
1. 边界处理：处理队伍数为0、关系为空等特殊情况
2. 输入验证：验证队伍编号的有效性
3. 内存优化：合理使用列表和优先队列
4. 异常处理：对非法输入进行检查
5. 可读性：添加详细注释和变量命名

算法要点：
1. 字典序最小拓扑排序的关键是使用优先队列（最小堆）
2. 每次选择入度为0且编号最小的节点
3. 优先队列可以保证每次取出的都是当前可选节点中编号最小的
4. 与普通拓扑排序相比，时间复杂度多了一个logN因子
5. 结果序列是唯一的（在字典序最小的要求下）
"""

import heapq
from collections import defaultdict

class Solution:
    def findRanking(self, n: int, relations: list[list[int]]) -> list[int]:
        """
        使用优先队列优化的Kahn算法确定比赛名次
        
        Args:
            n: 队伍数
            relations: 关系数组，relations[i] = [a, b] 表示a队名次比b队高
            
        Returns:
            比赛名次数组，按名次从高到低排列
        """
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 计算每个节点的入度
        indegree = [0] * (n + 1)
        for a, b in relations:
            # a队名次比b队高
            graph[a].append(b)
            indegree[b] += 1
        
        # 使用优先队列（最小堆）替代普通队列
        pq = []
        
        # 将所有入度为0的节点加入优先队列
        for i in range(1, n + 1):
            if indegree[i] == 0:
                heapq.heappush(pq, i)
        
        # 存储拓扑排序结果
        result = []
        
        # 拓扑排序过程
        while pq:
            # 取出编号最小的入度为0的节点
            cur = heapq.heappop(pq)
            # 将当前节点加入结果数组
            result.append(cur)
            
            # 遍历当前节点的所有邻居
            for next_node in graph[cur]:
                # 将邻居节点的入度减1
                indegree[next_node] -= 1
                # 如果邻居节点的入度变为0，则加入优先队列
                if indegree[next_node] == 0:
                    heapq.heappush(pq, next_node)
        
        # 返回结果数组
        return result

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 4个队伍，3个关系
    relations1 = [[1, 2], [2, 3], [3, 4]]
    result1 = solution.findRanking(4, relations1)
    print("测试用例1:", " ".join(map(str, result1)))
    
    # 测试用例2: 4个队伍，4个关系
    relations2 = [[1, 3], [2, 3], [1, 4], [2, 4]]
    result2 = solution.findRanking(4, relations2)
    print("测试用例2:", " ".join(map(str, result2)))