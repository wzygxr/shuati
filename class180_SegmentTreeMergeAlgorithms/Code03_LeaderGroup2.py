import sys
from collections import defaultdict
from bisect import bisect_left, bisect_right, insort

"""
线段树合并专题 - Code03_LeaderGroup2.py

大根堆问题（BZOJ4919），Python版
测试链接：https://www.lydsy.com/JudgeOnline/problem.php?id=4919

题目来源：Lydsy1706月赛
题目大意：给定一棵树，每个节点有一个权值，要求选出最多的节点，
使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值

算法思路：
1. 使用树链剖分技术将树分解为链
2. 采用启发式合并策略优化合并效率
3. 使用有序列表维护每个链上的权值信息
4. 通过后序遍历自底向上计算最优解

核心思想：
- 树链剖分：将树分解为若干条链，便于高效处理
- 启发式合并：将较小的集合合并到较大的集合，优化时间复杂度
- 有序列表维护：利用bisect模块维护递增序列
- LIS维护：在每个节点维护一个最长递增子序列

时间复杂度分析：
- 树链剖分：O(n)
- 启发式合并：O(n log² n)
- 有序列表操作：O(log n) 每次插入/删除
- 总时间复杂度：O(n log² n)

空间复杂度分析：
- 树结构存储：O(n)
- 有序列表数组：O(n)
- 总空间复杂度：O(n)

工程化考量：
1. 使用bisect模块替代C++的multiset，保持有序性
2. 字典存储树结构，节省空间
3. 树链剖分优化查询效率
4. 启发式合并减少合并操作次数

优化技巧：
- 启发式合并：选择较小的集合合并到较大的集合
- 树链剖分：将树分解为链，便于高效处理
- bisect优化：利用二分查找特性保证有序性

边界情况处理：
- 单节点树
- 链状树
- 权值全部相同的情况
- 大规模数据输入

测试用例设计：
1. 基础测试：小规模树结构验证算法正确性
2. 边界测试：单节点、链状树、完全二叉树
3. 性能测试：n=200000的大规模数据
4. 极端测试：权值全部相同或严格递增/递减

运行命令：
python Code03_LeaderGroup2.py < input.txt

注意事项：
1. Python版本由于性能限制，适合中小规模数据
2. 对于大规模数据，建议使用C++或Java版本
3. 注意递归深度限制，可能需要调整系统设置
"""

def main():
    """
    主函数 - 解决领导集团问题
    输入：树的节点数，各节点权值，父节点关系
    输出：最大领导集团的节点数
    """
    # 读取输入数据
    n = int(sys.stdin.readline())
    
    # 初始化变量数组
    val = [0] * (n + 1)        # 节点权值
    fa = [0] * (n + 1)         # 父节点
    sz = [0] * (n + 1)         # 子树大小
    hs = [0] * (n + 1)         # 重儿子
    s = defaultdict(list)      # 使用列表模拟multiset，存储每个节点的有序序列
    
    # 构建树的邻接表表示
    graph = defaultdict(list)
    
    # 读取每个节点的权值和父节点信息
    for i in range(1, n + 1):
        line = sys.stdin.readline().split()
        val[i] = int(line[0])  # 当前节点的权值
        fa[i] = int(line[1])   # 当前节点的父节点
        if fa[i] != 0:         # 如果不是根节点，添加到父节点的邻接表
            graph[fa[i]].append(i)
    
    # 第一次DFS：计算子树大小和确定重儿子
    def dfs1(u):
        """
        第一次深度优先搜索 - 计算子树大小和确定重儿子
        
        参数:
            u: 当前节点编号
        功能:
            1. 计算每个节点的子树大小
            2. 确定每个节点的重儿子（子树最大的子节点）
        """
        sz[u] = 1  # 初始化为1（包含自己）
        
        # 遍历所有子节点
        for v in graph[u]:
            dfs1(v)           # 递归处理子节点
            sz[u] += sz[v]    # 累加子树大小
            
            # 更新重儿子：选择子树最大的作为重儿子
            if sz[v] > sz[hs[u]]:
                hs[u] = v
    
    # 第二次DFS：进行启发式合并，维护每个节点的有序序列
    def dfs2(u):
        """
        第二次深度优先搜索 - 启发式合并维护有序序列
        
        参数:
            u: 当前节点编号
        功能:
            1. 优先处理重儿子，实现树链剖分
            2. 对轻儿子进行启发式合并
            3. 维护每个节点的有序序列，用于求最大递增子序列
        """
        # 如果存在重儿子，则先处理重儿子
        if hs[u] != 0:
            dfs2(hs[u])
            id_u = hs[u]  # 继承重儿子的id，实现重链剖分
        else:
            # 如果没有重儿子，为当前节点新建一个有序列表
            id_u = u
            s[u] = []
        
        # 处理所有轻儿子
        for v in graph[u]:
            if v != hs[u]:  # 确保只处理轻儿子
                dfs2(v)     # 递归处理轻儿子
                
                # 启发式合并核心思想：将较小的列表合并到较大的列表中
                # 这样可以保证合并操作的总复杂度不超过O(n log n)
                if len(s[v]) > len(s[u]):
                    s[u], s[v] = s[v], s[u]  # 交换，确保u保存较大的列表
                
                # 将轻儿子的有序列表合并到当前节点的有序列表
                for x in s[v]:
                    insort(s[u], x)  # 使用bisect.insort保持列表有序
        
        # 将当前节点的值插入到有序列表中
        insort(s[u], val[u])
        
        # 删除大于当前节点值的最小元素，以维护最长递增子序列性质
        # 这一步保证了对于当前节点，其有序列表中的元素都是可以作为其子树中
        # 满足条件的节点集合的权值序列（父节点权值不大于子节点）
        idx = bisect_right(s[u], val[u])
        if idx < len(s[u]):
            s[u].pop(idx)
    
    # 执行两次DFS
    dfs1(1)  # 根节点默认为1
    dfs2(1)
    
    # 输出结果：根节点对应的有序列表长度即为所求的最大节点数
    print(len(s[1]))


# 补充题目：LeetCode 834. 树中距离之和
class LeetCode834:
    """
    LeetCode 834. 树中距离之和 - 树形DP解法
    题目链接：https://leetcode.cn/problems/sum-of-distances-in-tree/
    
    题目大意：给定一棵树，计算每个节点到其他所有节点的距离之和
    
    解法思路：
    1. 第一次DFS：计算每个节点作为根时，其子树内所有节点到它的距离和，以及子树大小
    2. 第二次DFS：通过换根DP，利用父节点的信息高效计算子节点的全局距离和
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    算法核心：
    - 树形DP与换根法结合
    - 动态转移方程的推导
    - 状态回溯的正确性维护
    """
    def sumOfDistancesInTree(self, n, edges):
        """
        计算每个节点到其他所有节点的距离之和
        
        参数:
            n: 节点数量
            edges: 树的边列表
        
        返回:
            包含每个节点到其他所有节点距离和的数组
        """
        # 构建树的邻接表
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        ans = [0] * n
        sz = [0] * n  # 子树大小
        dp = [0] * n  # dp[u] 表示以u为根的子树中，所有节点到u的距离之和
        
        # 第一次DFS：计算子树大小和子树内距离和
        self._dfs1(0, -1, sz, dp, graph)
        
        # 第二次DFS：通过换根DP计算所有节点到其他节点的距离和
        self._dfs2(0, -1, sz, dp, ans, graph, n)
        
        return ans
    
    def _dfs1(self, u, parent, sz, dp, graph):
        """
        第一次DFS：计算子树大小和子树内距离和
        
        参数:
            u: 当前节点
            parent: 父节点
            sz: 子树大小数组
            dp: 子树内距离和数组
            graph: 树的邻接表
        """
        sz[u] = 1
        for v in graph[u]:
            if v != parent:
                self._dfs1(v, u, sz, dp, graph)
                sz[u] += sz[v]
                dp[u] += dp[v] + sz[v]  # 子树v中的每个节点到u的距离都比到v多1
    
    def _dfs2(self, u, parent, sz, dp, ans, graph, n):
        """
        第二次DFS：通过换根DP计算全局距离和
        
        参数:
            u: 当前节点
            parent: 父节点
            sz: 子树大小数组
            dp: 子树内距离和数组
            ans: 最终答案数组
            graph: 树的邻接表
            n: 节点总数
        """
        ans[u] = dp[u]
        for v in graph[u]:
            if v != parent:
                # 换根：将根从u换到v
                # 保存原值用于回溯
                prev_dp_u = dp[u]
                prev_dp_v = dp[v]
                prev_sz_u = sz[u]
                prev_sz_v = sz[v]
                
                # 重新计算u和v的dp值
                # 当根从u变为v时，u的子树大小减少sz[v]，v的子树大小变为n
                dp[u] -= dp[v] + sz[v]
                sz[u] -= sz[v]
                dp[v] += dp[u] + sz[u]
                sz[v] = n
                
                self._dfs2(v, u, sz, dp, ans, graph, n)
                
                # 回溯，恢复原值，确保其他分支的计算正确
                dp[u] = prev_dp_u
                dp[v] = prev_dp_v
                sz[u] = prev_sz_u
                sz[v] = prev_sz_v


if __name__ == "__main__":
    # 处理大根堆问题
    main()
    
    # 如果要测试LeetCode 834题，可以取消下面的注释
    # solution = LeetCode834()
    # n = 6
    # edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
    # print(solution.sumOfDistancesInTree(n, edges))  # 输出: [8, 12, 6, 10, 10, 10]