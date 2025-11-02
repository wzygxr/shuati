"""
SPOJ TTM - To the moon
题目链接: https://www.spoj.com/problems/TTM/
题目描述: 维护一个可持久化数组，支持以下操作：
1. C l r d : 将区间[l,r]的每个元素加上d
2. Q l r : 查询区间[l,r]的元素和
3. H l r t : 查询在时间t时区间[l,r]的元素和
4. B t : 回到时间t
"""

import random

class Node:
    def __init__(self, key, value):
        self.key = key          # 键值（数组下标）
        self.priority = random.randint(0, 2**31-1)  # 随机优先级
        self.size = 1           # 子树大小
        self.value = value      # 节点值
        self.sum = value        # 子树和
        self.add = 0            # 加法标记（懒标记）
        self.left = None        # 左子节点
        self.right = None       # 右子节点

class Code18_ToTheMoon2:
    def __init__(self):
        self.root = None        # 根节点
    
    def update(self, node):
        """更新节点信息"""
        if node:
            left_size = node.left.size if node.left else 0
            right_size = node.right.size if node.right else 0
            node.size = left_size + right_size + 1
            
            left_sum = node.left.sum if node.left else 0
            right_sum = node.right.sum if node.right else 0
            node.sum = left_sum + node.value + right_sum
    
    def push_down(self, node):
        """下传懒标记"""
        if node and node.add != 0:
            # 更新当前节点的值
            node.value += node.add
            node.sum += node.size * node.add
            
            # 传递懒标记给子节点
            if node.left:
                node.left.add += node.add
            if node.right:
                node.right.add += node.add
            
            # 清除当前节点的加法标记
            node.add = 0
    
    def split_by_position(self, root, pos):
        """按位置分裂，将树按照位置pos分裂为两棵树"""
        if not root:
            return None, None
        
        # 先下传懒标记
        self.push_down(root)
        
        left_size = root.left.size if root.left else 0
        
        if left_size + 1 <= pos:
            left_tree, right_tree = self.split_by_position(root.right, pos - left_size - 1)
            root.right = left_tree
            self.update(root)
            return root, right_tree
        else:
            left_tree, right_tree = self.split_by_position(root.left, pos)
            root.left = right_tree
            self.update(root)
            return left_tree, root
    
    def merge(self, left, right):
        """合并操作，将两棵树合并为一棵树"""
        if not left:
            return right
        if not right:
            return left
        
        # 先下传懒标记
        self.push_down(left)
        self.push_down(right)
        
        if left.priority >= right.priority:
            left.right = self.merge(left.right, right)
            self.update(left)
            return left
        else:
            right.left = self.merge(left, right.left)
            self.update(right)
            return right
    
    def build(self, arr):
        """构建初始数组"""
        for i, value in enumerate(arr):
            new_node = Node(i + 1, value)
            self.root = self.merge(self.root, new_node)
    
    def add_range(self, l, r, d):
        """区间加法 [l, r] += d"""
        # 先将树分裂成三部分：1~l-1, l~r, r+1~n
        left, right = self.split_by_position(self.root, r)
        left_left, mid = self.split_by_position(left, l - 1)
        
        # 对中间部分打加法标记
        if mid:
            mid.add += d
        
        # 合并回去
        self.root = self.merge(self.merge(left_left, mid), right)
    
    def query_sum(self, l, r):
        """查询区间和 [l, r]"""
        # 先将树分裂成三部分：1~l-1, l~r, r+1~n
        left, right = self.split_by_position(self.root, r)
        left_left, mid = self.split_by_position(left, l - 1)
        
        # 查询中间部分的和
        result = 0
        if mid:
            self.push_down(mid)
            result = mid.sum
        
        # 合并回去
        self.root = self.merge(self.merge(left_left, mid), right)
        
        return result

# 测试函数
def main():
    tree = Code18_ToTheMoon2()
    
    # 示例数组
    arr = [1, 2, 3, 4, 5]
    tree.build(arr)
    
    # 示例操作
    tree.add_range(2, 4, 10)  # 区间[2,4]加10
    sum_result = tree.query_sum(1, 3)  # 查询区间[1,3]的和
    
    print("Sum of range [1,3]:", sum_result)  # 输出: 26 (1 + 12 + 13)

if __name__ == "__main__":
    main()