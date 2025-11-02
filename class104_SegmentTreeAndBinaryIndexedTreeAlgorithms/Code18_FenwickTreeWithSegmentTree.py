# class131/Code18_FenwickTreeWithSegmentTree.py
# 洛谷 P3380 【模板】树套树
# 题目链接: https://www.luogu.com.cn/problem/P3380
"""
题目描述:
实现一个支持以下操作的数据结构：
1. 查询区间[l,r]中值<=k的元素个数
2. 查询区间[l,r]中第k小的元素
3. 单点修改数组元素
4. 查询区间[l,r]中值<k的最大元素（前驱）
5. 查询区间[l,r]中值>k的最小元素（后继）

解题思路:
使用树套树（树状数组套线段树）解决区间问题。核心思想是用树状数组维护位置维度，每个树状数组节点对应一棵线段树，
线段树维护值域维度。通过这种二维结构可以高效处理区间查询和更新操作。

算法步骤:
1. 离散化: 将输入数据离散化到连续整数范围，减少空间消耗
2. 构建树状数组: 每个节点维护一棵动态开点线段树
3. 实现各种查询操作: 利用树状数组的前缀和性质和线段树的区间查询能力

时间复杂度分析:
- 时间复杂度：
  - 单点修改：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 区间查询：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 第k小查询：O(log³n) - 二分查找O(logn)次，每次查询O(log²n)时间
  - 前驱/后继查询：O(log³n)
- 空间复杂度：O(n log n) - 树状数组的每个节点对应一棵动态开点线段树

关键优化:
1. 使用slots优化内存
2. 设置递归深度限制
3. 动态开点线段树节省空间
4. 使用bisect模块进行高效的二分查找
"""

import sys
import bisect
from sys import stdin

MAXN = 50010
MAX_VAL = 1000000010

# 线段树节点类
class Node:
    __slots__ = ['left', 'right', 'count']  # 使用slots优化内存
    def __init__(self):
        self.left = 0   # 左子节点索引
        self.right = 0  # 右子节点索引
        self.count = 0  # 该区间的元素个数

class TreeManager:
    def __init__(self, size):
        """
        初始化线段树管理器
        :param size: 预分配的节点数量
        """
        self.tree = [Node() for _ in range(size)]
        self.cnt = 1  # 当前使用的节点编号

    def get_node(self, idx):
        """
        获取指定索引的节点，如果索引超出范围则动态扩展
        :param idx: 节点索引
        :return: 对应的节点
        """
        if idx >= len(self.tree):
            # 动态扩展数组（实际中应该预先分配足够空间）
            new_size = max(len(self.tree) * 2, idx + 1)
            self.tree.extend([Node() for _ in range(new_size - len(self.tree))])
        return self.tree[idx]

    def new_node(self):
        """
        创建新节点
        :return: 新节点的索引
        """
        node_idx = self.cnt
        self.cnt += 1
        return node_idx

def main():
    sys.setrecursionlimit(1 << 25)  # 设置递归深度限制
    
    n, m = map(int, stdin.readline().split())
    
    # 读取输入数组
    a = [0] * (n + 1)
    sorted_list = [0] * (n + 1)
    for i in range(1, n + 1):
        a[i] = int(stdin.readline())
        sorted_list[i] = a[i]
    
    # 离散化处理：将数值映射到连续的整数范围
    sorted_list = sorted_list[1:n+1]  # 去掉前导0
    sorted_list = sorted(list(set(sorted_list)))  # 去重并排序
    sorted_list = [0] + sorted_list  # 索引从1开始，添加前导0
    
    # 初始化树状数组和线段树管理器
    tm = TreeManager(MAXN * 40)  # 预估空间
    root = [0] * (MAXN + 1)  # 树状数组，每个元素是线段树根节点
    
    # 计算lowbit函数，用于树状数组操作
    def lowbit(x):
        """
        计算x的lowbit值（x的二进制表示中最低位的1所代表的数值）
        :param x: 输入值
        :return: lowbit值
        """
        return x & (-x)
    
    # 线段树更新（递归实现）
    def update_tree(node_idx, l, r, pos, val):
        """
        更新线段树节点
        :param node_idx: 当前线段树节点索引
        :param l: 区间左边界
        :param r: 区间右边界
        :param pos: 要更新的位置
        :param val: 更新的值
        """
        node = tm.get_node(node_idx)
        node.count += val  # 更新计数
        
        # 如果是叶子节点，直接返回
        if l == r:
            return
        
        # 计算中点，决定更新左子树还是右子树
        mid = (l + r) >> 1
        if pos <= mid:
            if not node.left:
                node.left = tm.new_node()
            update_tree(node.left, l, mid, pos, val)
        else:
            if not node.right:
                node.right = tm.new_node()
            update_tree(node.right, mid + 1, r, pos, val)
    
    # 线段树查询
    def query_tree(node_idx, l, r, ql, qr):
        """
        查询线段树区间和
        :param node_idx: 当前线段树节点索引
        :param l: 区间左边界
        :param r: 区间右边界
        :param ql: 查询区间左边界
        :param qr: 查询区间右边界
        :return: 区间和
        """
        # 如果节点不存在或查询区间无交集，返回0
        if not node_idx or r < ql or l > qr:
            return 0
        
        node = tm.get_node(node_idx)
        # 如果查询区间完全包含当前节点区间，直接返回计数
        if ql <= l and r <= qr:
            return node.count
        
        # 否则递归查询左右子树
        mid = (l + r) >> 1
        return query_tree(node.left, l, mid, ql, qr) + \
               query_tree(node.right, mid + 1, r, ql, qr)
    
    # 树状数组更新
    def update(pos, val, delta):
        """
        更新树状数组
        :param pos: 位置
        :param val: 值（离散化后的索引）
        :param delta: 变化量
        """
        while pos <= n:
            if not root[pos]:
                root[pos] = tm.new_node()
            update_tree(root[pos], 1, len(sorted_list) - 1, val, delta)
            pos += lowbit(pos)
    
    # 树状数组查询前缀和
    def query_prefix(pos, val):
        """
        查询树状数组前缀和
        :param pos: 位置
        :param val: 值（离散化后的索引）
        :return: 前缀和
        """
        sum_val = 0
        while pos > 0:
            sum_val += query_tree(root[pos], 1, len(sorted_list) - 1, 1, val)
            pos -= lowbit(pos)
        return sum_val
    
    # 离散化：获取数值对应的索引
    def get_index(val):
        """
        获取数值在离散化数组中的索引
        :param val: 数值
        :return: 索引
        """
        return bisect.bisect_left(sorted_list, val)
    
    # 查询区间[l,r]中<=k的元素个数
    def get_count(l, r, k):
        """
        查询区间[l,r]中<=k的元素个数
        :param l: 区间左边界
        :param r: 区间右边界
        :param k: 上界值
        :return: 元素个数
        """
        idx = get_index(k)
        return query_prefix(r, idx) - query_prefix(l - 1, idx)
    
    # 查询区间第k小
    def kth_smallest(l, r, k):
        """
        查询区间[l,r]中第k小的元素
        :param l: 区间左边界
        :param r: 区间右边界
        :param k: 第k小
        :return: 第k小的元素
        """
        left, right = 1, len(sorted_list) - 1
        while left < right:
            mid = (left + right) >> 1
            count = query_prefix(r, mid) - query_prefix(l - 1, mid)
            if count >= k:
                right = mid
            else:
                left = mid + 1
        return sorted_list[left]
    
    # 查询前驱（最大的小于k的数）
    def get_predecessor(l, r, k):
        """
        查询区间[l,r]中值<k的最大元素（前驱）
        :param l: 区间左边界
        :param r: 区间右边界
        :param k: 上界值
        :return: 前驱元素
        """
        left, right = 1, len(sorted_list) - 1
        ans = -MAX_VAL
        
        while left <= right:
            mid = (left + right) >> 1
            if sorted_list[mid] >= k:
                right = mid - 1
            else:
                count = query_prefix(r, mid) - query_prefix(l - 1, mid)
                if count > 0:
                    ans = sorted_list[mid]
                    left = mid + 1
                else:
                    right = mid - 1
        
        return ans
    
    # 查询后继（最小的大于k的数）
    def get_successor(l, r, k):
        """
        查询区间[l,r]中值>k的最小元素（后继）
        :param l: 区间左边界
        :param r: 区间右边界
        :param k: 下界值
        :return: 后继元素
        """
        left, right = 1, len(sorted_list) - 1
        ans = MAX_VAL
        
        while left <= right:
            mid = (left + right) >> 1
            if sorted_list[mid] <= k:
                left = mid + 1
            else:
                # 检查是否有大于k的元素
                count_r = query_prefix(r, len(sorted_list) - 1) - query_prefix(r, mid - 1)
                count_l_1 = query_prefix(l - 1, len(sorted_list) - 1) - query_prefix(l - 1, mid - 1)
                count = count_r - count_l_1
                
                if count > 0:
                    ans = sorted_list[mid]
                    right = mid - 1
                else:
                    left = mid + 1
        
        return ans
    
    # 构建初始树状数组
    for i in range(1, n + 1):
        idx = get_index(a[i])
        update(i, idx, 1)
    
    # 处理操作
    for _ in range(m):
        parts = stdin.readline().split()
        op = int(parts[0])
        
        if op == 1:  # 查询排名
            l, r, k = int(parts[1]), int(parts[2]), int(parts[3])
            idx = bisect.bisect_left(sorted_list, k)
            rank = (query_prefix(r, idx - 1) - query_prefix(l - 1, idx - 1)) + 1
            print(rank)
        elif op == 2:  # 查询第k小
            l, r, k = int(parts[1]), int(parts[2]), int(parts[3])
            ans = kth_smallest(l, r, k)
            print(ans)
        elif op == 3:  # 单点修改
            pos, k = int(parts[1]), int(parts[2])
            # 先删除旧值
            old_idx = get_index(a[pos])
            update(pos, old_idx, -1)
            # 再插入新值
            a[pos] = k
            new_idx = get_index(k)
            update(pos, new_idx, 1)
        elif op == 4:  # 查询前驱
            l, r, k = int(parts[1]), int(parts[2]), int(parts[3])
            pre = get_predecessor(l, r, k)
            print(pre)
        elif op == 5:  # 查询后继
            l, r, k = int(parts[1]), int(parts[2]), int(parts[3])
            succ = get_successor(l, r, k)
            print(succ)

if __name__ == "__main__":
    main()

'''
复杂度分析：
- 时间复杂度：
  - 单点修改：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 区间查询：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 第k小查询：O(log²n) - 二分查找O(logn)次，每次查询O(log²n)时间
  - 前驱/后继查询：O(log²n)
- 空间复杂度：O(n log n) - 树状数组的每个节点对应一棵动态开点线段树

Python实现注意事项：
1. 由于Python的递归深度限制，对于大规模数据可能需要增加递归限制
2. 使用bisect模块进行二分查找，效率比手动实现更高
3. 使用__slots__优化类的内存占用
4. 动态扩展线段树节点数组以避免内存浪费
5. 实际应用中应预先分配足够大的空间以提高效率
'''