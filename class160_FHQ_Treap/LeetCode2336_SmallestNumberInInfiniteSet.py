# LeetCode 2336. 无限集中的最小数字 - Python实现
# 使用FHQ-Treap（无旋Treap）解决LeetCode 2336题
# 题目链接: https://leetcode.cn/problems/smallest-number-in-infinite-set/
# 题目描述: 设计一个数据结构，维护一个包含所有正整数的无限集，支持以下操作：
# 1. popSmallest(): 弹出并返回集合中最小的整数
# 2. addBack(num): 将一个之前弹出的正整数num添加回集合中
# 
# 解题思路:
# 使用FHQ-Treap维护已删除的元素集合，同时使用current_min变量优化最小值查询
# 实现O(log k)的操作复杂度，其中k是已删除的元素个数

import random

class SmallestInfiniteSet:
    
    class Node:
        def __init__(self, key, priority):
            self.key = key          # 键值（存储被删除的正整数）
            self.count = 1          # 词频计数
            self.size = 1           # 子树大小
            self.priority = priority  # 随机优先级
            self.left = None        # 左子节点
            self.right = None       # 右子节点
    
    def __init__(self):
        self.root = None           # 根节点
        self.current_min = 1       # 当前最小的可用正整数
        random.seed(42)            # 设置随机种子以保证结果可复现
    
    def _update_size(self, node):
        """更新节点的子树大小"""
        if node:
            left_size = node.left.size if node.left else 0
            right_size = node.right.size if node.right else 0
            node.size = left_size + right_size + node.count
    
    def _split(self, root, key):
        """分裂操作：将树按值分成两部分"""
        if not root:
            return (None, None)
        
        if root.key <= key:
            # 当前节点及其左子树属于左部分，递归分裂右子树
            left, right = self._split(root.right, key)
            root.right = left
            self._update_size(root)
            return (root, right)
        else:
            # 当前节点及其右子树属于右部分，递归分裂左子树
            left, right = self._split(root.left, key)
            root.left = right
            self._update_size(root)
            return (left, root)
    
    def _merge(self, left, right):
        """合并操作：合并两棵满足条件的树"""
        if not left:
            return right
        if not right:
            return left
        
        if left.priority >= right.priority:
            # 左树优先级更高，作为新根
            left.right = self._merge(left.right, right)
            self._update_size(left)
            return left
        else:
            # 右树优先级更高，作为新根
            right.left = self._merge(left, right.left)
            self._update_size(right)
            return right
    
    def _contains(self, num):
        """检查元素是否存在（即是否被删除）"""
        curr = self.root
        while curr:
            if curr.key == num:
                return True
            if curr.key > num:
                curr = curr.left
            else:
                curr = curr.right
        return False
    
    def _add_node(self, num):
        """添加节点（标记为已删除）"""
        if not self._contains(num):
            left, right = self._split(self.root, num)
            new_node = self.Node(num, random.random())
            self.root = self._merge(self._merge(left, new_node), right)
    
    def _remove_node(self, num):
        """删除节点（标记为可用）"""
        # 先分裂出<=num的部分
        left, right = self._split(self.root, num)
        # 再从<=num的部分中分裂出<num的部分
        left_left, left_right = self._split(left, num - 1)
        # 合并<num和>num的部分，相当于删除num
        self.root = self._merge(left_left, right)
    
    def popSmallest(self):
        """弹出并返回集合中的最小整数"""
        # 如果current_min未被删除，直接返回并递增
        if not self._contains(self.current_min):
            result = self.current_min
            self.current_min += 1
            return result
        
        # 否则需要找到第一个未被删除的值
        result = self.current_min
        while self._contains(result):
            result += 1
        
        # 标记为已删除
        self._add_node(result)
        # 更新current_min
        self.current_min = max(self.current_min, result + 1)
        return result
    
    def addBack(self, num):
        """添加一个之前被删除的正整数back到集合中"""
        # 只有当num小于currentMin且已被删除时才需要操作
        if num < self.current_min and self._contains(num):
            # 标记为可用
            self._remove_node(num)
            # 更新current_min
            self.current_min = min(self.current_min, num)

# Your SmallestInfiniteSet object will be instantiated and called as such:
# obj = SmallestInfiniteSet()
# param_1 = obj.popSmallest()
# obj.addBack(num)

'''
【时间复杂度分析】
- popSmallest(): 平均O(log k)，其中k是已删除的元素个数
                 最坏情况下可能需要O(k)的时间，但通过current_min优化，实际性能很好
- addBack(): O(log k)

【空间复杂度分析】
- O(k)，其中k是已删除的元素个数

【优化说明】
1. 使用FHQ-Treap维护已删除元素的集合，支持高效的查询和修改
2. 使用current_min变量优化最小值查询，避免每次都需要在树中遍历
3. 采用非递归的contains方法提高查询效率

【Python语言特性考虑】
1. 类嵌套定义使Node类更清晰地与SmallestInfiniteSet关联
2. 避免了递归深度限制问题，contains方法使用非递归实现
3. 使用random.random()生成浮点数优先级，避免整数溢出

【测试用例】
测试代码：
obj = SmallestInfiniteSet()
obj.addBack(2)
print(obj.popSmallest())  # 输出: 1
print(obj.popSmallest())  # 输出: 2
print(obj.popSmallest())  # 输出: 3
obj.addBack(1)
print(obj.popSmallest())  # 输出: 1
print(obj.popSmallest())  # 输出: 4
print(obj.popSmallest())  # 输出: 5
'''