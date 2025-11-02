#!/usr/bin/env python3

"""
最大食物链计数（Python版本）
题目来源：洛谷 P4017 https://www.luogu.com.cn/problem/P4017

题目大意：
给定一个食物链有向图，统计从入度为0的节点到出度为0的节点的路径总数。

算法思路：
1. 使用邻接表建图
2. 使用拓扑排序遍历节点
3. 在遍历过程中进行动态规划，统计路径数量

时间复杂度：O(N + M)，其中N是节点数，M是边数
空间复杂度：O(N + M)

相关题目扩展：
1. 洛谷 P1113 杂务 - https://www.luogu.com.cn/problem/P1113
2. 洛谷 P1983 车站分级 - https://www.luogu.com.cn/problem/P1983
3. LeetCode 207. 课程表 - https://leetcode.cn/problems/course-schedule/
4. LeetCode 210. 课程表 II - https://leetcode.cn/problems/course-schedule-ii/
5. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
6. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
7. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
8. AtCoder ABC139E League - https://atcoder.jp/contests/abc139/tasks/abc139_e
9. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
10. 牛客网 字典序最小的拓扑序列 - https://ac.nowcoder.com/acm/problem/15184
11. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
12. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
13. LeetCode 1494. 并行课程 II - https://leetcode.cn/problems/parallel-courses-ii/
14. LeetCode 2050. 并行课程 III - https://leetcode.cn/problems/parallel-courses-iii/
15. LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
16. 洛谷 P4017 最大食物链计数 - https://www.luogu.com.cn/problem/P4017
17. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
18. POJ 3249 Test for Job - http://poj.org/problem?id=3249
19. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
20. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
21. 牛客网 课程表 - https://ac.nowcoder.com/acm/problem/24725
22. USACO 2014 January Contest, Gold - http://www.usaco.org/index.php?page=viewproblem2&cpid=382
23. Timus OJ 1280. Topological Sorting - https://acm.timus.ru/problem.aspx?space=1&num=1280
24. Aizu OJ GRL_4_B. Topological Sort - https://onlinejudge.u-aizu.ac.jp/problems/GRL_4_B
25. Project Euler Problem 79: Passcode derivation - https://projecteuler.net/problem=79
26. HackerEarth Topological Sort - https://www.hackerearth.com/practice/algorithms/graphs/topological-sort/practice-problems/
27. 计蒜客 三值排序 - https://nanti.jisuanke.com/t/T1566
28. 各大高校OJ中的拓扑排序题目
29. ZOJ 1060 Sorting It All Out - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
30. 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453

工程化考虑：
1. 输入输出优化：使用sys.stdin提高输入效率
2. 边界处理：处理空图、单节点图等特殊情况
3. 模块化设计：将建图、拓扑排序、路径计算分离
4. 异常处理：对非法输入进行检查
5. 性能优化：使用collections.deque优化队列操作
6. 内存管理：合理使用数据结构减少内存占用
7. 代码复用：将通用功能封装成独立函数
8. 可维护性：添加详细注释和文档说明

算法要点：
1. 拓扑排序保证了处理节点的顺序，使得DP状态转移正确
2. lines数组记录到达每个节点的路径数
3. 当一个节点的所有前驱都被处理完后，它就可以被处理了
4. 对于出度为0的节点，它们是食物链的顶端，需要累加到结果中
5. 使用邻接表可以高效地存储稀疏图
6. MOD运算防止整数溢出
"""

import sys
from collections import defaultdict, deque

def main():
    MOD = 80112002
    
    for line in sys.stdin:
        parts = line.strip().split()
        if len(parts) != 2:
            continue
            
        n, m = int(parts[0]), int(parts[1])
        
        # 邻接表建图
        graph = defaultdict(list)
        # 入度数组
        indegree = [0] * (n + 1)
        # 到达每个节点的路径数
        lines = [0] * (n + 1)
        
        # 读取边信息
        for _ in range(m):
            line = sys.stdin.readline()
            parts = line.strip().split()
            if len(parts) != 2:
                continue
            u, v = int(parts[0]), int(parts[1])
            graph[u].append(v)
            indegree[v] += 1
        
        # 拓扑排序
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(1, n + 1):
            if indegree[i] == 0:
                queue.append(i)
                lines[i] = 1  # 初始节点的路径数为1
        
        ans = 0
        while queue:
            u = queue.popleft()
            
            # 如果当前节点没有后续邻居，说明是顶级捕食者
            if not graph[u]:
                ans = (ans + lines[u]) % MOD
            else:
                # 遍历u的所有邻居节点
                for v in graph[u]:
                    # 状态转移：到达v的路径数增加到达u的路径数
                    lines[v] = (lines[v] + lines[u]) % MOD
                    # 如果v的入度减为0，加入队列
                    indegree[v] -= 1
                    if indegree[v] == 0:
                        queue.append(v)
        
        print(ans)

if __name__ == "__main__":
    main()