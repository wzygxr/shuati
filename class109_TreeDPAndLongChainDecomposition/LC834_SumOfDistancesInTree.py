# LeetCode 834. 树中距离之和 - Python实现
# 树形DP经典题目

from typing import List
from collections import defaultdict

class LC834_SumOfDistancesInTree:
    """
    计算树中每个节点到其他所有节点的距离之和
    
    解题思路:
    1. 树形DP，通过两次DFS遍历来解决
    2. 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
    3. 第二次DFS: 利用父节点的结果推导子节点的结果
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    
    def sumOfDistancesInTree(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        计算树中每个节点到其他所有节点的距离之和
        
        Args:
            n: 节点数
            edges: 边的列表，每条边用[node1, node2]表示
            
        Returns:
            每个节点到其他所有节点的距离之和组成的列表
        """
        # 构建邻接表表示的树
        graph = defaultdict(list)
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        # count[i]表示以节点i为根的子树节点数
        count = [1] * n
        # res[i]表示节点i到其他所有节点的距离之和
        res = [0] * n
        
        def dfs1(node: int, parent: int) -> None:
            """
            第一次DFS: 计算每个节点子树的节点数和子树内距离之和
            
            Args:
                node: 当前节点
                parent: 父节点
            """
            # 遍历当前节点的所有子节点
            for child in graph[node]:
                # 避免回到父节点
                if child != parent:
                    dfs1(child, node)
                    # 累加子树节点数
                    count[node] += count[child]
                    # 累加子树内距离之和
                    # 子树内每个节点到child的距离都增加了1，所以总距离增加count[child]
                    res[node] += res[child] + count[child]
        
        def dfs2(node: int, parent: int) -> None:
            """
            第二次DFS: 利用父节点结果推导子节点结果
            
            Args:
                node: 当前节点
                parent: 父节点
            """
            # 遍历当前节点的所有子节点
            for child in graph[node]:
                # 避免回到父节点
                if child != parent:
                    # 当从父节点node换根到子节点child时：
                    # 1. child子树中的所有节点到child的距离比到node的距离少1，总共减少count[child]
                    # 2. 除child子树外的其他节点到child的距离比到node的距离多1，总共增加(n - count[child])
                    res[child] = res[node] - count[child] + (n - count[child])
                    dfs2(child, node)
        
        # 第一次DFS: 计算每个节点子树的节点数和子树内距离之和
        dfs1(0, -1)
        
        # 第二次DFS: 利用父节点结果推导子节点结果
        dfs2(0, -1)
        
        return res

# 测试函数
def main():
    solution = LC834_SumOfDistancesInTree()
    
    # 测试用例1
    n1 = 6
    edges1 = [[0,1],[0,2],[2,3],[2,4],[2,5]]
    result1 = solution.sumOfDistancesInTree(n1, edges1)
    print(f"测试用例1结果: {result1}")
    # 预期输出: [8,12,6,10,10,10]
    
    # 测试用例2
    n2 = 1
    edges2 = []
    result2 = solution.sumOfDistancesInTree(n2, edges2)
    print(f"测试用例2结果: {result2}")
    # 预期输出: [0]
    
    # 测试用例3
    n3 = 2
    edges3 = [[1,0]]
    result3 = solution.sumOfDistancesInTree(n3, edges3)
    print(f"测试用例3结果: {result3}")
    # 预期输出: [1,1]

# 程序入口
if __name__ == "__main__":
    main()