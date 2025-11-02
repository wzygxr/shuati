# 合并 K 个升序链表
# 测试链接：https://leetcode.cn/problems/merge-k-sorted-lists/

import heapq
from typing import List, Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Code17_MergeKSortedLists:
    """
    合并 K 个升序链表（分治法）
    :param lists: K 个升序链表数组
    :return: 合并后的升序链表
    
    解题思路：
    1. 使用分治思想，将 K 个链表两两合并
    2. 每次合并后链表数量减半，直到只剩一个链表
    3. 复用合并两个有序链表的实现
    
    时间复杂度：O(N * log K) - N 是所有链表节点总数，K 是链表数量
    空间复杂度：O(log K) - 递归调用栈
    是否最优解：是
    """
    @staticmethod
    def mergeKLists(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        if not lists:
            return None
        
        # 分治合并
        return Code17_MergeKSortedLists._merge(lists, 0, len(lists) - 1)
    
    """
    分治合并链表
    :param lists: 链表数组
    :param left: 左边界
    :param right: 右边界
    :return: 合并后的链表
    """
    @staticmethod
    def _merge(lists: List[Optional[ListNode]], left: int, right: int) -> Optional[ListNode]:
        if left == right:
            return lists[left]
        
        mid = left + (right - left) // 2
        l1 = Code17_MergeKSortedLists._merge(lists, left, mid)
        l2 = Code17_MergeKSortedLists._merge(lists, mid + 1, right)
        
        return Code17_MergeKSortedLists._mergeTwoLists(l1, l2)
    
    """
    合并两个有序链表
    :param list1: 第一个有序链表
    :param list2: 第二个有序链表
    :return: 合并后的有序链表
    """
    @staticmethod
    def _mergeTwoLists(list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        # 创建虚拟头节点，简化边界处理
        dummy = ListNode(0)
        current = dummy
        
        # 双指针遍历两个链表
        while list1 and list2:
            # 比较两个链表当前节点的值
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余节点
        current.next = list1 if list1 else list2
        
        # 返回合并后的链表
        return dummy.next
    
    """
    合并 K 个升序链表（优先队列法）
    :param lists: K 个升序链表数组
    :return: 合并后的升序链表
    
    解题思路：
    1. 使用最小堆（优先队列）维护所有链表的头节点
    2. 每次从堆中取出最小节点，连接到结果链表
    3. 将取出节点的下一个节点加入堆中
    4. 重复直到堆为空
    
    时间复杂度：O(N * log K) - N 是所有链表节点总数，K 是链表数量
    空间复杂度：O(K) - 堆的空间
    是否最优解：是
    """
    @staticmethod
    def mergeKListsHeap(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        if not lists:
            return None
        
        # 创建最小堆
        heap = []
        
        # 将所有链表的头节点加入堆中
        for i, list_node in enumerate(lists):
            if list_node:
                heapq.heappush(heap, (list_node.val, i, list_node))
        
        # 创建虚拟头节点
        dummy = ListNode(0)
        current = dummy
        
        # 从堆中取出最小节点，连接到结果链表
        while heap:
            _, i, node = heapq.heappop(heap)
            current.next = node
            current = current.next
            
            # 将下一个节点加入堆中
            if node.next:
                heapq.heappush(heap, (node.next.val, i, node.next))
        
        return dummy.next

    """
    题目扩展：LeetCode 23. 合并 K 个升序链表
    来源：LeetCode、牛客网、剑指Offer等各大算法平台
    
    题目描述：
    给你一个链表数组，每个链表都已经按升序排列。
    请你将所有链表合并到一个升序链表中，返回合并后的链表。
    
    解题思路：
    方法一：分治法（推荐）
    1. 使用分治思想，将 K 个链表两两合并
    2. 每次合并后链表数量减半，直到只剩一个链表
    3. 复用合并两个有序链表的实现
    
    方法二：优先队列法
    1. 使用最小堆维护所有链表的头节点
    2. 每次从堆中取出最小节点，连接到结果链表
    3. 将取出节点的下一个节点加入堆中
    
    时间复杂度：
    - 分治法：O(N * log K) - N 是所有链表节点总数，K 是链表数量
    - 优先队列法：O(N * log K)
    
    空间复杂度：
    - 分治法：O(log K) - 递归调用栈
    - 优先队列法：O(K) - 堆的空间
    
    是否最优解：两种方法都是最优解
    
    工程化考量：
    1. 边界情况处理：空数组、空链表
    2. 代码可读性：分治法逻辑清晰，优先队列法直观易懂
    3. 性能优化：根据数据规模选择合适方法
    
    与机器学习等领域的联系：
    1. 在处理多个有序数据流时，需要合并操作
    2. 在外部排序算法中，需要合并多个有序文件
    3. 在分布式系统中，需要合并多个有序数据分片
    
    语言特性差异：
    Java: PriorityQueue 提供堆的实现
    C++: priority_queue 需要自定义比较函数
    Python: heapq 模块提供堆操作
    
    极端输入场景：
    1. 链表数组为空
    2. 链表数组中包含空链表
    3. 只有一个链表
    4. 所有链表都只有一个节点
    5. 链表数量很多但每个链表很短
    
    递归与非递归的区别对比：
    1. 分治法使用递归，代码简洁但有栈空间开销
    2. 优先队列法使用迭代，空间复杂度更可预测
    3. 在链表数量很多时，递归可能导致栈溢出
    
    设计的利弊：
    1. 分治法：时间复杂度优秀，适合链表数量较多的情况
    2. 优先队列法：空间复杂度固定，适合内存受限的环境
    """