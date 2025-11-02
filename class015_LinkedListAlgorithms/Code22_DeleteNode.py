# 删除链表中的节点
# 测试链接：https://www.hackerrank.com/challenges/delete-a-node-from-a-linked-list/problem

from typing import Optional

# 链表节点定义
class SinglyLinkedListNode:
    def __init__(self, data: int):
        self.data = data
        self.next: Optional[SinglyLinkedListNode] = None

class Code22_DeleteNode:
    """
    删除链表中的节点
    :param head: 链表头节点
    :param position: 要删除的节点位置（从0开始）
    :return: 删除节点后的链表头节点
    
    解题思路：
    1. 处理特殊情况：删除头节点
    2. 遍历链表找到要删除节点的前一个节点
    3. 调整指针完成删除操作
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """
    @staticmethod
    def deleteNode(head: Optional[SinglyLinkedListNode], position: int) -> Optional[SinglyLinkedListNode]:
        # 特殊情况：删除头节点
        if position == 0:
            return head.next if head else None
        
        # 遍历链表找到要删除节点的前一个节点
        current = head
        for i in range(position - 1):
            if current:  # type: ignore
                current = current.next
        
        # 调整指针完成删除操作
        if current and current.next:  # type: ignore
            current.next = current.next.next  # type: ignore
        
        # 返回链表头节点
        return head

    """
    题目扩展：HackerRank - Delete a Node
    来源：HackerRank等各大算法平台
    
    题目描述：
    删除链表中给定位置的节点并返回头节点的引用。
    头节点位置为0。删除节点后链表可能为空。
    
    解题思路：
    1. 处理特殊情况：删除头节点
    2. 遍历链表找到要删除节点的前一个节点
    3. 调整指针完成删除操作
    
    时间复杂度：O(n) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是
    """