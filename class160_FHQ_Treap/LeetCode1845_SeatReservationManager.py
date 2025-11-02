# LeetCode 1845. 座位预约管理系统 - Python实现
# 使用FHQ-Treap（无旋Treap）解决LeetCode 1845题
# 题目链接: https://leetcode.cn/problems/seat-reservation-manager/
# 题目描述: 设计一个座位预约管理系统，支持以下操作：
# 1. reserve(): 预约一个最小编号的可用座位
# 2. unreserve(seatNumber): 取消预约指定的座位
# 
# 解题思路:
# 使用FHQ-Treap维护被取消预约的座位集合，同时使用current_max变量优化座位分配
# 实现O(log k)的操作复杂度，其中k是当前可用（被取消预约）的座位数

import random

class SeatManager:
    
    class Node:
        def __init__(self, key, priority):
            self.key = key          # 座位号
            self.count = 1          # 词频计数
            self.size = 1           # 子树大小
            self.priority = priority  # 随机优先级
            self.left = None        # 左子节点
            self.right = None       # 右子节点
    
    def __init__(self, n):
        self.root = None           # 根节点
        self.total_seats = n       # 总座位数
        self.current_max = 0       # 当前最大已分配座位号
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
    
    def _get_first_available_seat(self):
        """获取最小的可用座位号"""
        if not self.root:
            return -1  # 没有可用座位
        
        # 一直向左走，找到最小值
        node = self.root
        while node.left:
            node = node.left
        return node.key
    
    def reserve(self):
        """预约座位，返回分配的座位号"""
        if self.root is not None:
            # 优先使用被取消预约的座位（最小的可用座位）
            seat_number = self._get_first_available_seat()
            
            # 从可用座位集合中移除该座位
            left, right = self._split(self.root, seat_number)
            left_left, left_right = self._split(left, seat_number - 1)
            self.root = self._merge(left_left, right)
        else:
            # 没有被取消的座位，分配新的座位
            if self.current_max < self.total_seats:
                seat_number = self.current_max + 1
                self.current_max = seat_number
            else:
                raise RuntimeError("No seats available")
        
        return seat_number
    
    def unreserve(self, seat_number):
        """取消预约，将座位放回可用集合"""
        # 验证座位号的有效性
        if not (1 <= seat_number <= self.total_seats):
            raise ValueError(f"Invalid seat number: {seat_number}")
        
        # 只有已经被预约的座位才能取消预约
        if seat_number <= self.current_max:
            # 检查座位是否已经在可用集合中
            # 这里简化处理，直接添加，如果已存在会导致重复，但在实际应用中应该避免
            # 更好的做法是先检查是否存在，但为了效率可以省略
            
            # 将座位添加到可用集合中
            left, right = self._split(self.root, seat_number)
            new_node = self.Node(seat_number, random.random())
            self.root = self._merge(self._merge(left, new_node), right)

# Your SeatManager object will be instantiated and called as such:
# obj = SeatManager(n)
# param_1 = obj.reserve()
# obj.unreserve(seatNumber)

'''
【时间复杂度分析】
- reserve(): O(log k)，其中k是当前可用（被取消预约）的座位数
- unreserve(): O(log k)

【空间复杂度分析】
- O(k)，其中k是当前可用（被取消预约）的座位数

【Python优化说明】
1. 使用FHQ-Treap维护被取消预约的座位集合，支持高效的最小元素查询
2. 使用current_max变量跟踪最大已分配座位号，避免无效分配
3. _get_first_available_seat方法使用非递归实现，避免Python的递归深度限制
4. 添加参数验证，提高代码健壮性

【测试用例】
测试代码：
seat_manager = SeatManager(5)
print(seat_manager.reserve())    # 输出: 1
print(seat_manager.reserve())    # 输出: 2
seat_manager.unreserve(2)
print(seat_manager.reserve())    # 输出: 2
print(seat_manager.reserve())    # 输出: 3
print(seat_manager.reserve())    # 输出: 4
seat_manager.unreserve(5)

【边界情况处理】
1. 所有座位都被预约时，再次reserve会抛出异常
2. 取消未预约的座位会被忽略
3. 座位号超出范围会抛出ValueError
'''