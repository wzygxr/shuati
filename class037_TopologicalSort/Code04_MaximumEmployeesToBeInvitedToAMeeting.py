#!/usr/bin/env python3

"""
参加会议的最多员工数 (Python版本)
一个公司准备组织一场会议，邀请名单上有 n 位员工
公司准备了一张 圆形 的桌子，可以坐下 任意数目 的员工
员工编号为 0 到 n - 1 。每位员工都有一位 喜欢 的员工
每位员工 当且仅当 他被安排在喜欢员工的旁边，他才会参加会议
每位员工喜欢的员工 不会 是他自己。给你一个下标从 0 开始的整数数组 favorite
其中 favorite[i] 表示第 i 位员工喜欢的员工。请你返回参加会议的 最多员工数目
测试链接 : https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/

算法思路：
这是一个基环树问题。每个员工只喜欢一个员工，形成的是一个基环树森林（每个连通分量有且仅有一个环）。
圆形桌子可以坐下两种类型的员工组合：
1. 整个环：如果环的大小大于等于3，那么只能选择这个环上的所有员工
2. 所有包含长度为2的环及其扩展链：长度为2的环上两个员工可以面对面坐，然后各自可以带上最长的下属链

时间复杂度：O(N)
空间复杂度：O(N)

相关题目扩展：
1. LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
2. LeetCode 1559. 二维网格图中探测环 - https://leetcode.cn/problems/detect-cycles-in-2d-grid/
3. LeetCode 1306. 跳跃游戏 III - https://leetcode.cn/problems/jump-game-iii/
4. 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453
5. 洛谷 P2607 [ZJOI2008]骑士 - https://www.luogu.com.cn/problem/P2607
6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
7. HDU 3523 Image copy detection - http://acm.hdu.edu.cn/showproblem.php?pid=3523
8. AtCoder ABC157E Simple String Queries - https://atcoder.jp/contests/abc157/tasks/abc157_e
9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
10. 牛客网 基环树问题 - https://ac.nowcoder.com/acm/problem/24725

工程化考虑：
1. 边界处理：处理小数组、特殊情况
2. 输入验证：验证 favorite 数组的有效性
3. 内存优化：合理使用列表和数组
4. 异常处理：对非法输入进行检查
5. 可读性：添加详细注释和变量命名

算法要点：
1. 计算入度：用于拓扑排序，找出环
2. 拓扑排序：删除所有不在环上的节点
3. 计算链深度：计算每个节点延伸出去的最长链
4. 分类讨论：
   - 大环（节点数 >= 3）：只能选择一个完整的环
   - 小环（节点数 = 2）：可以选择所有小环及其扩展链
"""

from typing import List
from collections import deque

class Solution:
    def maximumInvitations(self, favorite: List[int]) -> int:
        """
        计算参加会议的最多员工数
        
        Args:
            favorite: favorite[i] 表示第 i 位员工喜欢的员工
            
        Returns:
            参加会议的最多员工数
        """
        # 图 : favorite[a] = b : a -> b
        n = len(favorite)
        
        # 计算每个节点的入度
        indegree = [0] * n
        for i in range(n):
            indegree[favorite[i]] += 1
        
        # 拓扑排序使用的队列
        queue = deque()
        
        # 将所有入度为0的节点加入队列
        for i in range(n):
            if indegree[i] == 0:
                queue.append(i)
        
        # deep[i] : 不包括i在内，i之前的最长链的长度
        deep = [0] * n
        
        # 拓扑排序，删除所有不在环上的节点
        while queue:
            cur = queue.popleft()
            next_node = favorite[cur]
            # 更新 next_node 节点的最长链长度
            deep[next_node] = max(deep[next_node], deep[cur] + 1)
            # 将 next_node 节点的入度减1，如果变为0则加入队列
            indegree[next_node] -= 1
            if indegree[next_node] == 0:
                queue.append(next_node)
        
        # 目前图中的点，不在环上的点，都删除了！ indegree[i] == 0
        # 可能性1 : 所有小环(中心个数 == 2)，算上中心点 + 延伸点，总个数
        sumOfSmallRings = 0
        # 可能性2 : 所有大环(中心个数 > 2)，只算中心点，最大环的中心点个数
        bigRings = 0
        
        # 遍历所有仍在图中的节点（即在环上的节点）
        for i in range(n):
            # 只关心的环！
            if indegree[i] > 0:
                # 计算环的大小
                ringSize = 1
                indegree[i] = 0  # 标记已访问
                # 遍历环中的所有节点
                j = favorite[i]
                while j != i:
                    ringSize += 1
                    indegree[j] = 0  # 标记已访问
                    j = favorite[j]
                
                # 根据环的大小分类处理
                if ringSize == 2:
                    # 小环：可以和其他小环同时存在
                    # 总人数 = 环上2个节点 + 各自延伸出的最长链
                    sumOfSmallRings += 2 + deep[i] + deep[favorite[i]]
                else:
                    # 大环：只能选择一个最大的环
                    bigRings = max(bigRings, ringSize)
        
        # 返回两种情况的最大值
        return max(sumOfSmallRings, bigRings)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    favorite1 = [2, 2, 1, 2]
    result1 = solution.maximumInvitations(favorite1)
    print(f"测试用例1: {result1}")  # 应输出 3
    
    # 测试用例2
    favorite2 = [1, 2, 0]
    result2 = solution.maximumInvitations(favorite2)
    print(f"测试用例2: {result2}")  # 应输出 3
    
    # 测试用例3
    favorite3 = [3, 0, 1, 2]
    result3 = solution.maximumInvitations(favorite3)
    print(f"测试用例3: {result3}")  # 应输出 4