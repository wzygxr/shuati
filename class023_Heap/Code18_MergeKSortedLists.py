import heapq

class Solution:
    """
    相关题目10: LeetCode 23. 合并K个升序链表
    题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
    题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。
    解题思路: 使用最小堆维护K个链表的头节点，每次从堆中取出最小值，并将其下一个节点加入堆中
    时间复杂度: O(N log K)，其中N是所有节点的总数，K是链表的数量
    空间复杂度: O(K)，堆中最多存储K个节点
    是否最优解: 是，这是合并K个有序链表的最优解法之一
    
    本题属于堆的典型应用场景：多源有序数据的合并
    """
    
    def mergeKLists(self, lists):
        """
        使用最小堆合并K个有序链表
        
        Args:
            lists: K个有序链表的数组
            
        Returns:
            ListNode: 合并后的有序链表头节点
        """
        # 异常处理：检查输入数组是否为None或空
        if not lists:
            return None
        
        # 边界情况：如果只有一个链表，直接返回
        if len(lists) == 1:
            return lists[0]
        
        # 创建一个最小堆
        # 由于Python的heapq默认是最小堆，我们可以直接使用
        # 为了处理值相同的节点，我们需要在堆中存储(节点值, 唯一标识符, 节点)的元组
        # 唯一标识符是为了在节点值相同的情况下仍然可以比较
        min_heap = []
        
        # 初始化：将所有链表的头节点加入堆中（如果不为None）
        for i, head in enumerate(lists):
            if head:
                # 存储元组(节点值, 索引, 节点)，索引用于当值相同时的比较
                heapq.heappush(min_heap, (head.val, i, head))
        
        # 创建一个哑节点作为合并后链表的头节点前一个节点
        dummy = ListNode(-1)
        curr = dummy
        
        # 不断从堆中取出最小值节点，直到堆为空
        while min_heap:
            # 取出堆顶元素（当前最小值节点）
            val, i, node = heapq.heappop(min_heap)
            
            # 将最小值节点添加到结果链表中
            curr.next = node
            curr = curr.next
            
            # 如果当前最小值节点还有下一个节点，将下一个节点加入堆中
            if node.next:
                heapq.heappush(min_heap, (node.next.val, i, node.next))
        
        # 返回合并后链表的头节点
        return dummy.next
    
    def mergeTwoLists(self, l1, l2):
        """
        递归方式合并两个有序链表
        
        Args:
            l1: 第一个有序链表的头节点
            l2: 第二个有序链表的头节点
            
        Returns:
            ListNode: 合并后的有序链表头节点
        """
        if not l1:
            return l2
        if not l2:
            return l1
        
        if l1.val <= l2.val:
            l1.next = self.mergeTwoLists(l1.next, l2)
            return l1
        else:
            l2.next = self.mergeTwoLists(l1, l2.next)
            return l2
    
    def mergeKListsDivideConquer(self, lists):
        """
        使用分治法合并K个有序链表
        
        Args:
            lists: K个有序链表的数组
            
        Returns:
            ListNode: 合并后的有序链表头节点
        """
        if not lists:
            return None
        
        n = len(lists)
        return self._mergeKLists(lists, 0, n - 1)
    
    def _mergeKLists(self, lists, start, end):
        """
        分治法的递归实现
        
        Args:
            lists: K个有序链表的数组
            start: 起始索引
            end: 结束索引
            
        Returns:
            ListNode: 合并后的有序链表头节点
        """
        if start == end:
            return lists[start]
        
        mid = start + (end - start) // 2
        left = self._mergeKLists(lists, start, mid)
        right = self._mergeKLists(lists, mid + 1, end)
        
        return self.mergeTwoLists(left, right)

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 打印链表的辅助函数
def printList(head):
    result = []
    curr = head
    while curr:
        result.append(str(curr.val))
        curr = curr.next
    print(" -> ".join(result) if result else "空链表")

# 创建链表的辅助函数
def createList(nums):
    dummy = ListNode(-1)
    curr = dummy
    for num in nums:
        curr.next = ListNode(num)
        curr = curr.next
    return dummy.next

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    print("测试用例1：")
    l1 = createList([1, 4, 5])
    l2 = createList([1, 3, 4])
    l3 = createList([2, 6])
    lists1 = [l1, l2, l3]
    
    print("堆实现输出:")
    result1 = solution.mergeKLists(lists1)
    printList(result1)  # 期望输出: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
    
    # 重置测试用例1
    l1 = createList([1, 4, 5])
    l2 = createList([1, 3, 4])
    l3 = createList([2, 6])
    lists1 = [l1, l2, l3]
    
    print("分治实现输出:")
    result1DivideConquer = solution.mergeKListsDivideConquer(lists1)
    printList(result1DivideConquer)
    
    # 测试用例2：空数组
    print("\n测试用例2：")
    lists2 = []
    result2 = solution.mergeKLists(lists2)
    printList(result2)  # 期望输出: 空链表
    
    # 测试用例3：包含空链表
    print("\n测试用例3：")
    l1 = createList([1, 4, 5])
    l2 = createList([1, 3, 4])
    lists3 = [None, l1, None, l2]
    result3 = solution.mergeKLists(lists3)
    printList(result3)  # 期望输出: 1 -> 1 -> 3 -> 4 -> 4 -> 5
    
    # 测试用例4：只有一个链表
    print("\n测试用例4：")
    l3 = createList([2, 6])
    lists4 = [l3]
    result4 = solution.mergeKLists(lists4)
    printList(result4)  # 期望输出: 2 -> 6
    
    print("\n所有测试用例通过！")

# 运行测试
if __name__ == "__main__":
    test_solution()