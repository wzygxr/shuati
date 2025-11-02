# SPOJ KGSS - Maximum Sum
# 题目链接：https://www.spoj.com/problems/KGSS/
# 题目描述：
# 给定一个长度为n的整数序列，执行m次操作
# 操作类型：
# 1. U i x: 将第i个位置的值更新为x
# 2. Q l r: 查询[l,r]区间内两个最大值的和
#
# 解题思路：
# 使用线段树维护区间信息，每个节点存储以下信息：
# 1. 区间最大值(max1)
# 2. 区间次大值(max2)
#
# 合并两个子区间[l,mid]和[mid+1,r]的信息：
# 1. 区间最大值 = max(左区间max1, 右区间max1)
# 2. 区间次大值 = max(左区间max2, 右区间max2, min(左区间max1, 右区间max1))
#
# 关键技术点：
# 1. 线段树维护区间最值信息
# 2. 区间合并时的最值处理
# 3. 懒惰标记优化（本题不需要，因为是单点更新）
#
# 时间复杂度分析：
# 1. 建树：O(n)
# 2. 更新：O(log n)
# 3. 查询：O(log n)
# 4. 空间复杂度：O(n)
#
# 是否最优解：是
# 这是解决区间两个最大值之和查询问题的最优解法，时间复杂度为O(log n)
# 与暴力解法O(n)相比，线段树解法在大数据量下具有明显优势

import sys
from collections import namedtuple

# 定义节点信息类，存储区间内的最大值和次大值
# max1: 区间最大值
# max2: 区间次大值
Node = namedtuple('Node', ['max1', 'max2'])

class SegmentTree:
    """
    线段树类，用于维护区间内两个最大值的和
    
    Attributes:
        n (int): 数组长度
        arr (list): 原始数组
        tree (list): 线段树数组，存储Node对象
    """
    
    def __init__(self, arr):
        """
        初始化线段树
        
        Args:
            arr (list): 输入的整数数组
        """
        self.n = len(arr)
        self.arr = arr
        # 线段树数组大小通常设置为4*n以保证足够空间
        self.tree = [None] * (4 * self.n)
        # 构建线段树
        self.build(1, 0, self.n - 1)
    
    def push_up(self, left, right):
        """
        合并两个子节点的信息，计算父节点的信息
        这是线段树的核心操作之一
        
        Args:
            left (Node): 左子节点信息
            right (Node): 右子节点信息
            
        Returns:
            Node: 合并后的节点信息
        """
        # 父节点的最大值是两个子节点最大值中的较大者
        max1 = max(left.max1, right.max1)
        # 父节点的次大值需要考虑三种情况：
        # 1. 左子节点的次大值
        # 2. 右子节点的次大值
        # 3. 两个子节点最大值中的较小者（当两个最大值不相等时）
        max2 = max(max(left.max2, right.max2), min(left.max1, right.max1))
        return Node(max1, max2)
    
    def build(self, rt, l, r):
        """
        递归构建线段树
        
        Args:
            rt (int): 当前节点在线段树数组中的索引
            l (int): 当前区间的左边界
            r (int): 当前区间的右边界
        """
        # 递归终止条件：当前区间只有一个元素
        if l == r:
            # 叶子节点只存储该位置的值作为最大值，次大值设为负无穷
            self.tree[rt] = Node(self.arr[l], float('-inf'))
            return
        
        # 分治处理左右子区间
        mid = (l + r) // 2
        # 递归构建左子树
        self.build(2 * rt, l, mid)
        # 递归构建右子树
        self.build(2 * rt + 1, mid + 1, r)
        # 合并左右子树的信息
        self.tree[rt] = self.push_up(self.tree[2 * rt], self.tree[2 * rt + 1])
    
    def update(self, idx, val, l, r, rt):
        """
        更新第idx个位置的值为val
        
        Args:
            idx (int): 要更新的位置（0索引）
            val (int): 新的值
            l (int): 当前区间的左边界
            r (int): 当前区间的右边界
            rt (int): 当前节点在线段树数组中的索引
        """
        # 递归终止条件：找到目标位置
        if l == r:
            # 更新叶子节点的值
            self.tree[rt] = Node(val, float('-inf'))
            return
        
        # 二分查找目标位置
        mid = (l + r) // 2
        if idx <= mid:
            # 目标位置在左子区间
            self.update(idx, val, l, mid, 2 * rt)
        else:
            # 目标位置在右子区间
            self.update(idx, val, mid + 1, r, 2 * rt + 1)
        
        # 更新当前节点的信息（自底向上）
        self.tree[rt] = self.push_up(self.tree[2 * rt], self.tree[2 * rt + 1])
    
    def query(self, jobl, jobr, l, r, rt):
        """
        查询区间[jobl, jobr]内两个最大值的和
        
        Args:
            jobl (int): 查询区间的左边界（0索引）
            jobr (int): 查询区间的右边界（0索引）
            l (int): 当前区间的左边界
            r (int): 当前区间的右边界
            rt (int): 当前节点在线段树数组中的索引
            
        Returns:
            Node: 查询结果，包含区间内最大值和次大值
        """
        # 完全包含：当前区间完全在查询区间内
        if jobl <= l and r <= jobr:
            return self.tree[rt]
        
        # 分治处理
        mid = (l + r) // 2
        if jobr <= mid:
            # 查询区间完全在左子区间
            return self.query(jobl, jobr, l, mid, 2 * rt)
        elif jobl > mid:
            # 查询区间完全在右子区间
            return self.query(jobl, jobr, mid + 1, r, 2 * rt + 1)
        else:
            # 查询区间跨越左右子区间，需要合并结果
            left = self.query(jobl, jobr, l, mid, 2 * rt)
            right = self.query(jobl, jobr, mid + 1, r, 2 * rt + 1)
            return self.push_up(left, right)

def main():
    """
    主函数，处理输入输出和操作执行
    """
    # 使用快速输入以提高效率
    input = sys.stdin.read
    data = input().split()
    
    # 解析输入数据
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 读取数组元素
    arr = [int(data[idx + i]) for i in range(n)]
    idx += n
    
    # 构建线段树
    seg_tree = SegmentTree(arr)
    
    # 读取操作次数
    m = int(data[idx])
    idx += 1
    
    # 存储结果
    results = []
    for _ in range(m):
        # 读取操作类型
        op = data[idx]
        idx += 1
        if op == 'U':
            # 更新操作：U i x
            i = int(data[idx]) - 1  # 转换为0索引
            x = int(data[idx + 1])
            idx += 2
            seg_tree.update(i, x, 0, n - 1, 1)
        else:  # op == 'Q'
            # 查询操作：Q l r
            l = int(data[idx]) - 1  # 转换为0索引
            r = int(data[idx + 1]) - 1
            idx += 2
            result = seg_tree.query(l, r, 0, n - 1, 1)
            # 返回两个最大值的和
            results.append(str(result.max1 + result.max2))
    
    # 输出所有查询结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()