#!/usr/bin/env python3

"""
喧闹和富有 (Python版本)
从 0 到 n - 1 编号，其中每个人都有不同数目的钱，以及不同程度的安静值
给你一个数组richer，其中richer[i] = [ai, bi] 表示 
person ai 比 person bi 更有钱
还有一个整数数组 quiet ，其中 quiet[i] 是 person i 的安静值
richer 中所给出的数据 逻辑自洽
也就是说，在 person x 比 person y 更有钱的同时，不会出现
person y 比 person x 更有钱的情况
现在，返回一个整数数组 answer 作为答案，其中 answer[x] = y 的前提是,
在所有拥有的钱肯定不少于 person x 的人中，
person y 是最安静的人（也就是安静值 quiet[y] 最小的人）。
测试链接 : https://leetcode.cn/problems/loud-and-rich/

算法思路：
这是一道拓扑排序的应用题。我们可以将 richer 关系看作有向边，从更富有的人指向更穷的人。
然后通过拓扑排序，从最富有的人开始，逐步更新每个人在所有不少于他富有的人中最安静的人。

时间复杂度：O(N + M)，其中 N 是人数，M 是 richer 关系数
空间复杂度：O(N + M)

相关题目扩展：
1. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
2. LeetCode 310. 最小高度树 - https://leetcode.cn/problems/minimum-height-trees/
3. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
4. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
5. 洛谷 P1137 旅行计划 - https://www.luogu.com.cn/problem/P1137
6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
7. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
8. AtCoder ABC157E Simple String Queries - https://atcoder.jp/contests/abc157/tasks/abc157_e
9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
10. 牛客网 牛牛的背包问题 - https://ac.nowcoder.com/acm/problem/16783

工程化考虑：
1. 边界处理：处理空数组、单个元素等特殊情况
2. 输入验证：验证 richer 数组的逻辑自洽性
3. 内存优化：合理使用列表和字典
4. 异常处理：对非法输入进行检查
5. 可读性：添加详细注释和变量命名

算法要点：
1. 构建图：将 richer 关系转换为有向图
2. 计算入度：用于拓扑排序
3. 初始化队列：将入度为0的节点（最富有的人）加入队列
4. 初始化答案数组：每个人最安静的人初始为自己
5. 拓扑排序：从富人向穷人传播信息，更新更安静的人
"""

from collections import defaultdict, deque
from typing import List

class Solution:
    def loudAndRich(self, richer: List[List[int]], quiet: List[int]) -> List[int]:
        """
        计算每个人在所有不少于他富有的人中最安静的人
        
        Args:
            richer: richer[i] = [a, b] 表示 a 比 b 更有钱
            quiet: quiet[i] 表示第 i 个人的安静值
            
        Returns:
            answer[x] = y 表示在所有不少于 x 富有的人中，y 是最安静的
        """
        n = len(quiet)
        # 构建邻接表表示的图
        graph = defaultdict(list)
        
        # 计算每个节点的入度
        indegree = [0] * n
        for a, b in richer:
            # a 比 b 更有钱，所以有一条从 a 到 b 的边
            graph[a].append(b)
            indegree[b] += 1
        
        # 拓扑排序使用的队列
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(n):
            if indegree[i] == 0:
                queue.append(i)
        
        # 初始化答案数组，ans[i] 表示在所有不少于 i 富有的人中最安静的人
        ans = list(range(n))
        
        # 拓扑排序过程
        while queue:
            # 取出队首元素
            cur = queue.popleft()
            
            # 遍历当前节点的所有邻居
            for next_node in graph[cur]:
                # 更新 next_node 节点的答案：
                # 如果 cur 节点所指向的最安静的人比 next_node 节点当前记录的更安静的人更安静，
                # 则更新 next_node 节点的答案
                if quiet[ans[cur]] < quiet[ans[next_node]]:
                    ans[next_node] = ans[cur]
                
                # 将 next_node 节点的入度减1，如果变为0则加入队列
                indegree[next_node] -= 1
                if indegree[next_node] == 0:
                    queue.append(next_node)
        
        return ans

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    richer1 = [[1, 0], [2, 1], [3, 1], [3, 7], [4, 3], [5, 3], [6, 3]]
    quiet1 = [3, 2, 5, 4, 6, 1, 7, 0]
    result1 = solution.loudAndRich(richer1, quiet1)
    print(f"测试用例1: {result1}")  # 应输出 [5, 5, 2, 5, 4, 5, 6, 7]
    
    # 测试用例2
    richer2 = []
    quiet2 = [0]
    result2 = solution.loudAndRich(richer2, quiet2)
    print(f"测试用例2: {result2}")  # 应输出 [0]