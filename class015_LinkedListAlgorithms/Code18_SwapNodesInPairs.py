# 两两交换链表中的节点
# 测试链接：https://leetcode.cn/problems/swap-nodes-in-pairs/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Code18_SwapNodesInPairs:
    """
    两两交换链表中的节点（迭代法）
    :param head: 链表头节点
    :return: 交换后的链表头节点
    
    解题思路：
    1. 使用虚拟头节点简化操作
    2. 使用三个指针分别指向要交换的两个节点和前一个节点
    3. 按照特定顺序调整指针指向完成交换
    4. 移动指针处理下一组节点
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """
    @staticmethod
    def swapPairs(head: Optional[ListNode]) -> Optional[ListNode]:
        # 创建虚拟头节点，简化边界处理
        dummy = ListNode(0)
        dummy.next = head
        
        # prev 指向前一个节点，用于连接交换后的节点
        prev = dummy
        
        # 当还有至少两个节点时继续交换
        while head and head.next:
            # 定义要交换的两个节点
            first = head
            second = head.next
            
            # 交换节点
            prev.next = second
            first.next = second.next
            second.next = first
            
            # 移动指针到下一组
            prev = first
            head = first.next
        
        # 返回交换后的链表
        return dummy.next
    
    """
    两两交换链表中的节点（递归法）
    :param head: 链表头节点
    :return: 交换后的链表头节点
    
    解题思路：
    1. 递归处理链表
    2. 每次处理前两个节点，交换后递归处理剩余部分
    3. 将交换后的前两个节点与递归处理的结果连接
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(n) - 递归调用栈的深度
    是否最优解：不是（空间复杂度较高）
    """
    @staticmethod
    def swapPairsRecursive(head: Optional[ListNode]) -> Optional[ListNode]:
        # 基本情况：空链表或只有一个节点
        if not head or not head.next:
            return head
        
        # 定义要交换的两个节点
        first = head
        second = head.next
        
        # 递归处理剩余部分
        first.next = Code18_SwapNodesInPairs.swapPairsRecursive(second.next)
        
        # 交换前两个节点
        second.next = first
        
        # 返回新的头节点
        return second

    """
    题目扩展：LeetCode 24. 两两交换链表中的节点
    来源：LeetCode、牛客网、剑指Offer等各大算法平台
    
    题目描述：
    给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。
    你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。
    
    解题思路：
    方法一：迭代法（推荐）
    1. 使用虚拟头节点简化操作
    2. 使用三个指针分别指向要交换的两个节点和前一个节点
    3. 按照特定顺序调整指针指向完成交换
    4. 移动指针处理下一组节点
    
    方法二：递归法
    1. 递归处理链表
    2. 每次处理前两个节点，交换后递归处理剩余部分
    3. 将交换后的前两个节点与递归处理的结果连接
    
    时间复杂度：
    - 迭代法：O(n)
    - 递归法：O(n)
    
    空间复杂度：
    - 迭代法：O(1)
    - 递归法：O(n) - 递归调用栈
    """