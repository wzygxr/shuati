"""
LeetCode 1970. Smallest Missing Genetic Value in Each Subtree

题目描述:
给你一棵根节点为0的家族树，树中节点从0到n-1编号，parents[i]是节点i的父节点。
每个节点有一个基因值genes[i]。对于每个节点，求出以其为根的子树中缺失的最小基因值。

解题思路:
使用可持久化线段树（主席树）解决子树Mex问题。
1. 通过DFS遍历树，为每个节点建立主席树
2. 对于每个节点，将其子树中的所有基因值插入到主席树中
3. 查询Mex即为查询区间内未出现的最小自然数

时间复杂度: O(n log n)
空间复杂度: O(n log n)

示例:
输入:
parents = [-1,0,0,2], genes = [1,2,3,4]
输出:
[5,1,1,1]

解释:
- 节点0的子树包含基因值1,2,3,4，缺失的最小值为5
- 节点1的子树只包含基因值2，缺失的最小值为1
- 节点2的子树包含基因值3,4，缺失的最小值为1
- 节点3的子树只包含基因值4，缺失的最小值为1
"""


class Node:
    """线段树节点"""
    def __init__(self):
        self.sum: int = 0      # 区间内元素个数
        self.left = None  # 左子节点
        self.right = None # 右子节点


class PersistentSegmentTree:
    """可持久化线段树（主席树）"""
    def __init__(self, n):
        """
        初始化主席树
        :param n: 数组长度
        """
        self.n = n
        self.root = [None] * (n + 1)  # 每个版本线段树的根节点
        self.cnt = 0  # 节点计数器
    
    def build(self, l, r):
        """
        构建空线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点
        """
        node = Node()
        if l < r:
            mid = (l + r) // 2
            node.left = self.build(l, mid)
            node.right = self.build(mid + 1, r)
        return node
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的位置
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点
        :return: 新节点
        """
        node = Node()
        node.left = pre.left if pre else None
        node.right = pre.right if pre else None
        node.sum = (pre.sum if pre else 0) + 1
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                node.left = self.insert(pos, l, mid, pre.left if pre else None)
            else:
                node.right = self.insert(pos, mid + 1, r, pre.right if pre else None)
        return node
    
    def query_mex(self, l, r, u, v):
        """
        查询区间Mex
        :param l: 查询区间左端点
        :param r: 查询区间右端点
        :param u: 前一个版本的根节点
        :param v: 当前版本的根节点
        :return: Mex值
        """
        if l == r:
            return l
        mid = (l + r) // 2
        # 计算左子树中数的个数
        left_count = (v.left.sum if v.left else 0) - (u.left.sum if u.left else 0)
        # 如果左子树中数的个数等于区间长度，说明左子树满
        if left_count == mid - l + 1:
            # Mex在右子树中
            return self.query_mex(mid + 1, r, u.right if u else None, v.right if v else None)
        else:
            # Mex在左子树中
            return self.query_mex(l, mid, u.left if u else None, v.left if v else None)


def smallestMissingValueSubtree(parents, genes):
    """
    计算每个子树中缺失的最小基因值
    :param parents: 父节点数组
    :param genes: 基因值数组
    :return: 每个子树中缺失的最小基因值数组
    """
    n = len(parents)
    
    # 构建邻接表
    children = [[] for _ in range(n)]
    for i in range(1, n):
        children[parents[i]].append(i)
    
    # DFS序
    dfn = [0] * n
    end = [0] * n
    timestamp = 0
    
    def dfs(u):
        nonlocal timestamp
        timestamp += 1
        dfn[u] = timestamp
        
        for v in children[u]:
            dfs(v)
        
        end[u] = timestamp
    
    # DFS遍历
    dfs(0)
    
    # 构建主席树
    pst = PersistentSegmentTree(n)
    pst.root[0] = pst.build(1, n)
    for i in range(1, n + 1):
        pst.root[i] = pst.insert(genes[i - 1], 1, n, pst.root[i - 1])
    
    # 计算答案
    ans = [0] * n
    for i in range(n):
        ans[i] = pst.query_mex(1, n, pst.root[dfn[i] - 1], pst.root[end[i]])
    
    return ans


# 测试代码
if __name__ == "__main__":
    parents = [-1, 0, 0, 2]
    genes = [1, 2, 3, 4]
    result = smallestMissingValueSubtree(parents, genes)
    print(result)  # [5, 1, 1, 1]