# 树的重心 (Tree Centroid)
# 题目描述:
# 找到一个点，其所有的子树中最大的子树节点数最少
# 换句话说，删除这个点后，剩余的最大子树的节点数最小
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们需要知道以下信息：
#    - 以该节点为根的子树的节点数
#    - 删除该节点后，剩余的最大子树节点数
# 3. 递归处理子树，综合计算当前节点的信息
# 4. 树的重心就是使得删除该点后，剩余的最大子树节点数最少的点
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(n) - 存储树结构和递归调用栈
# 是否为最优解: 是，这是计算树的重心的标准方法
#
# 相关题目:
# - POJ 1655 Balancing Act
# - ZOJ 3107 Godfather

from typing import List, Tuple

class TreeCentroid:
    def __init__(self):
        pass
    
    # 计算树的重心
    def find_centroid(self, n: int, edges: List[List[int]]) -> Tuple[int, int]:
        """
        计算树的重心
        :param n: 节点数量
        :param edges: 边列表 [[u, v], ...]
        :return: (重心节点, 删除重心后剩余的最大子树节点数)
        """
        # 构建邻接表
        tree = [[] for _ in range(n + 1)]
        for u, v in edges:
            tree[u].append(v)
            tree[v].append(u)
        
        # 以每个节点为根的子树节点数
        subtree_size = [0] * (n + 1)
        
        # 树的重心和对应的最小最大子树节点数
        centroid = [0]
        min_max_subtree_size = [float('inf')]
        
        # 第一次DFS：计算每个节点的子树大小
        def dfs1(u: int, parent: int) -> int:
            subtree_size[u] = 1
            
            # 遍历当前节点的所有子节点
            for v in tree[u]:
                # 避免回到父节点
                if v != parent:
                    # 递归计算子树大小
                    subtree_size[u] += dfs1(v, u)
            
            return subtree_size[u]
        
        # 第二次DFS：找到树的重心
        def dfs2(u: int, parent: int, total_nodes: int):
            # 计算删除节点u后，剩余的最大子树节点数
            max_subtree_size = 0
            
            # 遍历当前节点的所有子节点
            for v in tree[u]:
                # 避免回到父节点
                if v != parent:
                    # 更新最大子树节点数
                    max_subtree_size = max(max_subtree_size, subtree_size[v])
            
            # 计算父节点方向的子树大小（即除了当前子树外的其他节点数）
            parent_subtree_size = total_nodes - subtree_size[u]
            max_subtree_size = max(max_subtree_size, parent_subtree_size)
            
            # 更新重心
            if max_subtree_size < min_max_subtree_size[0]:
                min_max_subtree_size[0] = max_subtree_size
                centroid[0] = u
            
            # 递归处理子节点
            for v in tree[u]:
                # 避免回到父节点
                if v != parent:
                    dfs2(v, u, total_nodes)
        
        # 第一次DFS计算子树大小
        dfs1(1, -1)
        
        # 第二次DFS找到重心
        dfs2(1, -1, n)
        
        return (centroid[0], int(min_max_subtree_size[0]))
    
    # POJ 1655 Balancing Act 的解法
    def balancing_act(self, n: int, edges: List[List[int]]) -> Tuple[int, int]:
        """
        POJ 1655 Balancing Act 题目解法
        :param n: 节点数量
        :param edges: 边列表 [[u, v], ...]
        :return: (重心节点, 删除重心后剩余的最大子树节点数)
        """
        return self.find_centroid(n, edges)

# 测试代码
if __name__ == "__main__":
    solution = TreeCentroid()
    
    # 测试树的重心
    # 示例：树结构为 1-2, 1-3, 2-4, 2-5
    n = 5
    edges = [[1, 2], [1, 3], [2, 4], [2, 5]]
    
    centroid, max_subtree_size = solution.find_centroid(n, edges)
    print(f"树的重心: {centroid}, 删除重心后剩余的最大子树节点数: {max_subtree_size}")