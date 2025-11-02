"""
SPOJ DQUERY - D-query

题目描述:
给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。

解题思路:
使用可持久化线段树（主席树）解决静态区间不同元素个数问题。
1. 对于每个位置，记录该位置的数字上一次出现的位置
2. 从前向后建立主席树，第i棵线段树表示前i个位置的信息
3. 对于位置i，如果该数字之前出现过位置j，则在第i棵线段树中将位置j的计数减1，
   位置i的计数加1；否则只在位置i的计数加1
4. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
5. 查询区间不同数字个数即为查询区间内计数大于0的位置个数

时间复杂度: O(n log n + q log n)
空间复杂度: O(n log n)

示例:
输入:
5
1 1 2 1 3
3
1 5
2 4
3 5

输出:
3
2
3
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
    
    def update(self, pos, val, l, r, pre):
        """
        在线段树中更新一个位置的值
        :param pos: 要更新的位置
        :param val: 要增加的值
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点
        :return: 新节点
        """
        node = Node()
        node.left = pre.left if pre else None
        node.right = pre.right if pre else None
        node.sum = (pre.sum if pre else 0) + val
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                node.left = self.update(pos, val, l, mid, pre.left if pre else None)
            else:
                node.right = self.update(pos, val, mid + 1, r, pre.right if pre else None)
        return node
    
    def query(self, l, r, u, v):
        """
        查询区间不同数字个数
        :param l: 查询区间左端点
        :param r: 查询区间右端点
        :param u: 前一个版本的根节点
        :param v: 当前版本的根节点
        :return: 不同数字个数
        """
        if l == r:
            return 1 if (v.sum if v else 0) - (u.sum if u else 0) > 0 else 0
        mid = (l + r) // 2
        left_count = self.query(l, mid, u.left if u else None, v.left if v else None)
        right_count = self.query(mid + 1, r, u.right if u else None, v.right if v else None)
        return left_count + right_count


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 读取原始数组
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    # 构建主席树
    pst = PersistentSegmentTree(n)
    pst.root[0] = pst.build(1, n)
    pos = {}  # pos[v] : 数字v上次出现的位置
    
    for i in range(1, n + 1):
        if arr[i] not in pos:
            # 第一次出现该数字
            pst.root[i] = pst.update(i, 1, 1, n, pst.root[i - 1])
        else:
            # 该数字之前出现过，需要先减去之前的计数，再加上当前计数
            pst.root[i] = pst.update(pos[arr[i]], -1, 1, n, pst.root[i - 1])
            pst.root[i] = pst.update(i, 1, 1, n, pst.root[i])
        pos[arr[i]] = i
    
    q = int(data[idx])
    idx += 1
    results = []
    for _ in range(q):
        l = int(data[idx])
        idx += 1
        r = int(data[idx])
        idx += 1
        results.append(str(pst.query(1, n, pst.root[l - 1], pst.root[r])))
    
    print('\n'.join(results))


if __name__ == "__main__":
    main()