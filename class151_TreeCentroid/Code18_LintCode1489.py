# LintCode 1489. 树中的中心点
# 题目描述：给定一棵树，找出树的中心点（重心）
# 算法思想：直接应用树的重心查找算法
# 测试链接：https://www.lintcode.com/problem/1489/
# 时间复杂度：O(n)
# 空间复杂度：O(n)

import sys
from collections import defaultdict

# 设置递归深度以避免栈溢出
sys.setrecursionlimit(10**6)

class Solution:
    def findMinHeightTrees(self, n, edges):
        """
        寻找树的中心点（重心）
        
        参数:
            n: 节点数量
            edges: 边的列表
        返回:
            树的中心点列表
        """
        # 边界情况处理
        if n == 1:
            return [0]
        if n == 2:
            return [0, 1]
        
        # 初始化邻接表
        graph = [[] for _ in range(n)]
        
        # 构建图
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        # 子树大小
        size = [0] * n
        # 每个节点的最大子树大小
        max_sub = [0] * n
        
        def dfs(u, parent):
            """
            深度优先搜索计算子树大小和最大子树大小
            
            参数:
                u: 当前节点
                parent: 父节点
            """
            size[u] = 1
            max_sub[u] = 0
            
            # 遍历所有邻居
            for v in graph[u]:
                if v != parent:
                    dfs(v, u)
                    size[u] += size[v]
                    max_sub[u] = max(max_sub[u], size[v])
            
            # 计算父方向的子树大小
            max_sub[u] = max(max_sub[u], n - size[u])
        
        # 第一次DFS计算子树信息
        dfs(0, -1)
        
        # 找到最小的最大子树大小
        min_max_sub = float('inf')
        for i in range(n):
            if max_sub[i] < min_max_sub:
                min_max_sub = max_sub[i]
        
        # 收集所有重心
        result = []
        for i in range(n):
            if max_sub[i] == min_max_sub:
                result.append(i)
        
        return result

# 测试函数
def test():
    solution = Solution()
    
    # 测试用例1
    n1 = 4
    edges1 = [[1, 0], [1, 2], [1, 3]]
    print("Test Case 1:", solution.findMinHeightTrees(n1, edges1))  # Expected: [1]
    
    # 测试用例2
    n2 = 6
    edges2 = [[0, 3], [1, 3], [2, 3], [4, 3], [5, 4]]
    print("Test Case 2:", solution.findMinHeightTrees(n2, edges2))  # Expected: [3, 4]

# 运行测试
if __name__ == "__main__":
    test()

# 注意：在LintCode上提交时，直接提交Solution类即可