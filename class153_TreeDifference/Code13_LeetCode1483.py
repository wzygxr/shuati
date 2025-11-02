"""
LeetCode 1483. 树节点的第 K 个祖先（树上倍增法）
题目链接：https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
题目描述：给定一棵树，每个节点有唯一的父节点，实现一个类TreeAncestor：
   - TreeAncestor(n, parent): 初始化，n个节点，parent[i]是节点i的父节点
   - getKthAncestor(node, k): 返回节点node的第k个祖先节点
解法：树上倍增法（二进制拆分）

算法思路：
1. 使用倍增法预处理每个节点的2^i级祖先
2. 对于查询getKthAncestor(node, k)，将k二进制拆分
3. 从高位到低位，如果k的第i位为1，则node跳到其2^i级祖先
4. 重复直到k为0或node为-1

时间复杂度：
   - 初始化：O(N log N)
   - 查询：O(log K)
空间复杂度：O(N log N)
"""

import math

class TreeAncestor:
    """
    树节点的第K个祖先类
    """
    
    def __init__(self, n: int, parent: list):
        """
        构造函数：初始化倍增数组
        :param n: 节点数量
        :param parent: 父节点数组，parent[i]是节点i的父节点
        """
        self.n = n
        # 计算最大层数：log2(n)
        self.max_level = 0
        while (1 << self.max_level) <= n:
            self.max_level += 1
        
        # 初始化倍增数组
        self.stjump = [[-1] * self.max_level for _ in range(n)]
        
        # 初始化第0级祖先（直接父节点）
        for i in range(n):
            self.stjump[i][0] = parent[i]
        
        # 预处理倍增数组
        for j in range(1, self.max_level):
            for i in range(n):
                if self.stjump[i][j-1] == -1:
                    self.stjump[i][j] = -1
                else:
                    self.stjump[i][j] = self.stjump[self.stjump[i][j-1]][j-1]
    
    def getKthAncestor(self, node: int, k: int) -> int:
        """
        查询节点node的第k个祖先
        :param node: 当前节点
        :param k: 祖先级别
        :return: 第k个祖先节点，如果不存在返回-1
        """
        if node < 0 or node >= self.n or k < 0:
            return -1
        
        # 二进制拆分k
        for j in range(self.max_level):
            if (k >> j) & 1:
                node = self.stjump[node][j]
                if node == -1:
                    return -1
        
        return node

def main():
    """
    测试用例
    """
    # 测试用例1：简单的链式结构
    n1 = 7
    parent1 = [-1, 0, 0, 1, 1, 2, 2]
    ta1 = TreeAncestor(n1, parent1)
    
    # 测试查询
    print("测试用例1:")
    print(f"节点3的第1个祖先: {ta1.getKthAncestor(3, 1)}")  # 期望: 1
    print(f"节点3的第2个祖先: {ta1.getKthAncestor(3, 2)}")  # 期望: 0
    print(f"节点3的第3个祖先: {ta1.getKthAncestor(3, 3)}")  # 期望: -1
    
    # 测试用例2：更复杂的树结构
    n2 = 5
    parent2 = [-1, 0, 0, 1, 2]
    ta2 = TreeAncestor(n2, parent2)
    
    print("\n测试用例2:")
    print(f"节点4的第1个祖先: {ta2.getKthAncestor(4, 1)}")  # 期望: 2
    print(f"节点4的第2个祖先: {ta2.getKthAncestor(4, 2)}")  # 期望: 0
    print(f"节点4的第3个祖先: {ta2.getKthAncestor(4, 3)}")  # 期望: -1

if __name__ == "__main__":
    main()