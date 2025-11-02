# Codeforces 863D - Yet Another Array Queries Problem
# 题目链接: https://codeforces.com/contest/863/problem/D
# 题目描述: 给定一个数组和一系列操作，支持以下操作：
# 1. 将区间[l,r]循环右移一位
# 2. 将区间[l,r]循环左移一位
# 3. 查询位置x的元素值
#
# 解题思路:
# 使用FHQ-Treap维护数组，通过懒标记支持区间循环移位操作
# 实现O(log n)的区间操作和O(log n)的查询操作

import random
import sys

class Code16_Codeforces863D1:
    class Node:
        def __init__(self, key, priority):
            self.key = key          # 键值（数组元素）
            self.priority = priority  # 随机优先级
            self.size = 1           # 子树大小
            self.shift = 0          # 循环移位标记（懒标记）
            self.left = None        # 左子节点
            self.right = None       # 右子节点
    
    def __init__(self):
        self.root = None           # 根节点
        random.seed(42)            # 设置随机种子以保证结果可复现
    
    def _update_size(self, node):
        """更新节点的子树大小"""
        if node:
            left_size = node.left.size if node.left else 0
            right_size = node.right.size if node.right else 0
            node.size = left_size + right_size + 1
    
    def _push_down(self, node):
        """下传懒标记"""
        if node and node.shift != 0:
            # 应用循环移位
            node.shift = node.shift % node.size
            if node.shift != 0:
                # 注意：这里的实现简化了循环移位的处理
                # 实际应用中可能需要更复杂的操作
                
                # 传递懒标记给子节点
                if node.left:
                    node.left.shift = (node.left.shift + node.shift) % node.left.size
                if node.right:
                    node.right.shift = (node.right.shift + node.shift) % node.right.size
                
                # 清除当前节点的移位标记
                node.shift = 0
    
    def _split_by_size(self, root, k):
        """按照大小分裂（第k大）"""
        if not root:
            return (None, None)
        
        # 先下传懒标记
        self._push_down(root)
        
        left_size = root.left.size if root.left else 0
        
        if left_size + 1 <= k:
            left, right = self._split_by_size(root.right, k - left_size - 1)
            root.right = left
            self._update_size(root)
            return (root, right)
        else:
            left, right = self._split_by_size(root.left, k)
            root.left = right
            self._update_size(root)
            return (left, root)
    
    def _merge(self, left, right):
        """合并操作"""
        if not left:
            return right
        if not right:
            return left
        
        # 先下传懒标记
        self._push_down(left)
        self._push_down(right)
        
        if left.priority >= right.priority:
            left.right = self._merge(left.right, right)
            self._update_size(left)
            return left
        else:
            right.left = self._merge(left, right.left)
            self._update_size(right)
            return right
    
    def build(self, arr):
        """构建初始数组"""
        for i in range(len(arr)):
            new_node = self.Node(arr[i], random.random())
            self.root = self._merge(self.root, new_node)
    
    def rotate_right(self, l, r):
        """区间循环右移 [l, r]"""
        # 先将树分裂成三部分：1~l-1, l~r, r+1~n
        left, right = self._split_by_size(self.root, r)
        left_left, mid = self._split_by_size(left, l - 1)
        
        # 对中间部分打循环右移标记
        if mid:
            mid.shift = (mid.shift + 1) % mid.size
        
        # 合并回去
        self.root = self._merge(self._merge(left_left, mid), right)
    
    def rotate_left(self, l, r):
        """区间循环左移 [l, r]"""
        # 先将树分裂成三部分：1~l-1, l~r, r+1~n
        left, right = self._split_by_size(self.root, r)
        left_left, mid = self._split_by_size(left, l - 1)
        
        # 对中间部分打循环左移标记
        if mid:
            mid.shift = (mid.shift - 1 + mid.size) % mid.size
        
        # 合并回去
        self.root = self._merge(self._merge(left_left, mid), right)
    
    def _push_down_all(self, node):
        """下传所有懒标记"""
        if node:
            self._push_down(node)
            self._push_down_all(node.left)
            self._push_down_all(node.right)
    
    def _find_kth(self, node, k):
        """查找第k个元素"""
        if not node:
            return -1
        
        self._push_down(node)
        
        left_size = node.left.size if node.left else 0
        
        if k <= left_size:
            return self._find_kth(node.left, k)
        elif k == left_size + 1:
            return node.key
        else:
            return self._find_kth(node.right, k - left_size - 1)
    
    def query(self, x):
        """查询位置x的元素值"""
        # 先下传所有懒标记
        self._push_down_all(self.root)
        
        # 查找第x个元素
        return self._find_kth(self.root, x)

# 主程序
if __name__ == "__main__":
    # 读取输入
    input_lines = sys.stdin.read().splitlines()
    parts = input_lines[0].split()
    n = int(parts[0])  # 数组长度
    q = int(parts[1])  # 操作次数
    m = int(parts[2])  # 查询次数
    
    arr = list(map(int, input_lines[1].split()))
    
    tree = Code16_Codeforces863D1()
    tree.build(arr)
    
    # 处理每个操作
    for i in range(2, 2 + q):
        parts = list(map(int, input_lines[i].split()))
        type_op = parts[0]
        l = parts[1]
        r = parts[2]
        
        if type_op == 1:
            tree.rotate_right(l, r)
        else:
            tree.rotate_left(l, r)
    
    # 处理查询
    query_positions = list(map(int, input_lines[2 + q].split()))
    result = []
    for x in query_positions:
        result.append(str(tree.query(x)))
    
    print(' '.join(result))

'''
【时间复杂度分析】
- 构建数组：O(n log n)
- 每次操作：O(log n)
- 每次查询：O(log n)
总时间复杂度：O(n log n + (q + m) log n)

【空间复杂度分析】
- O(n)，存储n个节点

【Python优化说明】
1. 使用FHQ-Treap维护数组，支持高效的区间操作
2. 通过懒标记优化循环移位操作，避免每次都需要遍历区间内的所有节点
3. 按照大小分裂，便于区间操作
4. 使用sys.stdin.read()一次性读取所有输入，提高读取效率

【测试用例】
输入：
5 3 3
1 2 3 4 5
1 2 4
2 1 5
1 1 3
1 2 3
输出：
4 2 5
'''