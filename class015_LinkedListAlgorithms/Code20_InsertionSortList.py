# 对链表进行插入排序
# 测试链接：https://leetcode.cn/problems/insertion-sort-list/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Code20_InsertionSortList:
    """
    对链表进行插入排序
    :param head: 链表头节点
    :return: 排序后的链表头节点
    
    解题思路：
    1. 使用虚拟头节点简化操作
    2. 维护已排序部分和未排序部分
    3. 从未排序部分取节点，插入到已排序部分的正确位置
    
    时间复杂度：O(n²) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：对于链表插入排序是
    """
    @staticmethod
    def insertionSortList(head: Optional[ListNode]) -> Optional[ListNode]:
        if not head or not head.next:
            return head
        
        # 创建虚拟头节点
        dummy = ListNode(0)
        
        # 遍历原链表
        current = head
        while current:
            # 保存下一个节点
            next_node = current.next
            
            # 在已排序部分找到插入位置
            prev = dummy
            while prev.next and prev.next.val < current.val:
                prev = prev.next
            
            # 插入当前节点
            current.next = prev.next
            prev.next = current
            
            # 移动到下一个未排序节点
            current = next_node
        
        # 返回排序后的链表
        return dummy.next

    """
    题目扩展：LeetCode 147. 对链表进行插入排序
    来源：LeetCode、牛客网、剑指Offer等各大算法平台
    
    题目描述：
    给定单个链表的头 head ，使用插入排序对链表进行排序，并返回排序后链表的头。
    插入排序算法的步骤：
    插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。
    每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。
    重复直到所有输入数据插入完为止。
    
    解题思路：
    1. 使用虚拟头节点简化操作
    2. 维护已排序部分和未排序部分
    3. 从未排序部分取节点，插入到已排序部分的正确位置
    
    时间复杂度：O(n²) - n 是链表节点数量
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：对于链表插入排序是
    """