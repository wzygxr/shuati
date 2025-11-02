# 在特定位置插入节点
# 测试链接：https://www.hackerrank.com/challenges/insert-a-node-at-a-specific-position-in-a-linked-list/problem

from typing import Optional

# 链表节点定义
class SinglyLinkedListNode:
    def __init__(self, data: int):
        self.data = data
        self.next: Optional[SinglyLinkedListNode] = None

class Code21_InsertNodeAtPosition:
    """
    在特定位置插入节点
    :param head: 链表头节点
    :param data: 要插入的节点数据
    :param position: 插入位置（从0开始）
    :return: 插入节点后的链表头节点
    
    解题思路：
    1. 处理特殊情况：在链表头部插入节点
    2. 遍历链表找到插入位置的前一个节点
    3. 调整指针完成插入操作
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """
    @staticmethod
    def insertNodeAtPosition(head: Optional[SinglyLinkedListNode], data: int, position: int) -> Optional[SinglyLinkedListNode]:
        # 创建新节点
        new_node = SinglyLinkedListNode(data)
        
        # 特殊情况：在链表头部插入节点
        if position == 0:
            new_node.next = head
            return new_node
        
        # 遍历链表找到插入位置的前一个节点
        current = head
        for i in range(position - 1):
            if current:  # type: ignore
                current = current.next
        
        # 调整指针完成插入操作
        if current:  # type: ignore
            new_node.next = current.next
            current.next = new_node
        
        # 返回链表头节点
        return head

    """
    题目扩展：HackerRank - Insert a node at a specific position in a linked list
    来源：HackerRank等各大算法平台
    
    题目描述：
    给定一个链表头节点和一个整数，将具有该整数值的新节点插入到链表的指定位置。
    位置0表示头节点，位置1表示距离头节点一个节点的位置，以此类推。
    给定的头节点可能为空，表示初始链表为空。
    
    解题思路：
    1. 处理特殊情况：在链表头部插入节点
    2. 遍历链表找到插入位置的前一个节点
    3. 调整指针完成插入操作
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """