# 重排链表
# 测试链接：https://leetcode.cn/problems/reorder-list/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Code19_ReorderList:
    """
    重排链表
    :param head: 链表头节点
    
    解题思路：
    1. 找到链表中点，将链表分为两部分
    2. 反转后半部分链表
    3. 合并前半部分和反转后的后半部分
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """
    @staticmethod
    def reorderList(head: Optional[ListNode]) -> None:
        if not head or not head.next:
            return
        
        # 1. 找到链表中点
        mid = Code19_ReorderList._findMiddle(head)
        
        # 2. 反转后半部分链表
        second_half = Code19_ReorderList._reverseList(mid.next)
        mid.next = None  # 断开链表
        
        # 3. 合并前半部分和反转后的后半部分
        Code19_ReorderList._mergeLists(head, second_half)
    
    """
    找到链表中点（快慢指针法）
    :param head: 链表头节点
    :return: 链表中点
    """
    @staticmethod
    def _findMiddle(head: ListNode) -> ListNode:
        slow: ListNode = head
        fast: ListNode = head
        
        while fast.next and fast.next.next:
            slow = slow.next  # type: ignore
            fast = fast.next.next  # type: ignore
        
        return slow
    
    """
    反转链表
    :param head: 链表头节点
    :return: 反转后的链表头节点
    """
    @staticmethod
    def _reverseList(head: Optional[ListNode]) -> Optional[ListNode]:
        prev: Optional[ListNode] = None
        current = head
        
        while current:
            next_node = current.next
            current.next = prev
            prev = current
            current = next_node
        
        return prev
    
    """
    合并两个链表
    :param first: 第一个链表
    :param second: 第二个链表
    """
    @staticmethod
    def _mergeLists(first: ListNode, second: Optional[ListNode]) -> None:
        while second:
            temp1 = first.next
            temp2 = second.next
            
            first.next = second
            second.next = temp1
            
            first = temp1  # type: ignore
            second = temp2

    """
    题目扩展：LeetCode 143. 重排链表
    来源：LeetCode、牛客网、剑指Offer等各大算法平台
    
    题目描述：
    给定一个单链表 L 的头节点 head ，单链表 L 表示为：
    L0 → L1 → … → Ln - 1 → Ln
    请将其重新排列后变为：
    L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
    不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
    
    解题思路：
    1. 找到链表中点，将链表分为两部分
    2. 反转后半部分链表
    3. 合并前半部分和反转后的后半部分
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """