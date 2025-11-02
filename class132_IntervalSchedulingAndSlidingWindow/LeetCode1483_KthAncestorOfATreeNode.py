"""
LeetCode 1483. Kth Ancestor of a Tree Node

题目描述：
给你一棵树，树上有 n 个节点，编号从 0 到 n-1。
树用一个父节点数组 parent 来表示，其中 parent[i] 是节点 i 的父节点。
节点 0 是树的根节点，所以 parent[0] = -1。

实现 TreeAncestor 类：
- TreeAncestor(int n, int[] parent)：初始化树结构
- getKthAncestor(int node, int k)：返回节点 node 的第 k 个祖先节点，如果不存在则返回 -1

解题思路：
这是一个经典的倍增算法（Binary Lifting）问题。

算法步骤：
1. 预处理阶段：构建倍增表
   - 创建二维数组 p[i][j]，表示节点 i 的第 2^j 个祖先
   - p[i][0] = parent[i]（第 1 个祖先就是直接父节点）
   - p[i][j] = p[p[i][j-1]][j-1]（第 2^j 个祖先 = 第 2^(j-1) 个祖先的第 2^(j-1) 个祖先）
2. 查询阶段：利用二进制分解
   - 将 k 分解为二进制表示
   - 对于 k 的每一位为 1 的位置 j，向上跳 2^j 步

时间复杂度：
- 预处理：O(n * log n)
- 查询：O(log k)
空间复杂度：O(n * log n)

相关题目：
- Luogu P1613. 跑路（倍增算法）
- Codeforces 609E. Minimum spanning tree for each edge（倍增算法）
"""

class TreeAncestor:
    def __init__(self, n, parent):
        """
        构造函数，初始化树结构并预处理倍增表
        
        Args:
            n: 节点数量
            parent: 父节点数组
        """
        # 最大幂次，17 足够处理 10^5 范围内的节点
        self.MAX_LOG = 18
        
        # 初始化倍增表，p[i][j] 表示节点 i 的第 2^j 个祖先
        self.p = [[-1] * self.MAX_LOG for _ in range(n)]
        
        # 初始化直接父节点（2^0 = 1 步）
        for i in range(n):
            self.p[i][0] = parent[i]
        
        # 构建倍增表
        for j in range(1, self.MAX_LOG):
            for i in range(n):
                # 如果第 2^(j-1) 个祖先存在
                if self.p[i][j - 1] != -1:
                    # 第 2^j 个祖先 = 第 2^(j-1) 个祖先的第 2^(j-1) 个祖先
                    self.p[i][j] = self.p[self.p[i][j - 1]][j - 1]
    
    def getKthAncestor(self, node, k):
        """
        获取节点 node 的第 k 个祖先
        
        Args:
            node: 起始节点
            k: 祖先的步数
            
        Returns:
            第 k 个祖先节点，如果不存在则返回 -1
        """
        # 按二进制位从高到低遍历
        for i in range(self.MAX_LOG - 1, -1, -1):
            # 如果 k 的第 i 位是 1
            if (k >> i) & 1:
                # 向上跳 2^i 步
                node = self.p[node][i]
                # 如果不存在祖先，直接返回 -1
                if node == -1:
                    return -1
        return node

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    parent1 = [-1, 0, 0, 1, 1, 2, 2]
    treeAncestor1 = TreeAncestor(7, parent1)
    
    print("测试用例1:")
    print("树结构: 节点0为根，节点1,2是节点0的子节点，节点3,4是节点1的子节点，节点5,6是节点2的子节点")
    print(f"getKthAncestor(3, 1) = {treeAncestor1.getKthAncestor(3, 1)}")  # 期望输出: 1
    print(f"getKthAncestor(5, 2) = {treeAncestor1.getKthAncestor(5, 2)}")  # 期望输出: 0
    print(f"getKthAncestor(6, 3) = {treeAncestor1.getKthAncestor(6, 3)}")  # 期望输出: -1
    
    # 测试用例2
    parent2 = [-1, 0, 0, 1, 2]
    treeAncestor2 = TreeAncestor(5, parent2)
    
    print("\n测试用例2:")
    print("树结构: 节点0为根，节点1,2是节点0的子节点，节点3是节点1的子节点，节点4是节点2的子节点")
    print(f"getKthAncestor(3, 1) = {treeAncestor2.getKthAncestor(3, 1)}")  # 期望输出: 1
    print(f"getKthAncestor(3, 2) = {treeAncestor2.getKthAncestor(3, 2)}")  # 期望输出: 0
    print(f"getKthAncestor(4, 3) = {treeAncestor2.getKthAncestor(4, 3)}")  # 期望输出: -1