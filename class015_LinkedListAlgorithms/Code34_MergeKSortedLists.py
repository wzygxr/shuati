# 合并K个有序链表 - LeetCode 23
# 测试链接: https://leetcode.cn/problems/merge-k-sorted-lists/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 优先队列（最小堆）法
    def mergeKLists(self, lists: list[ListNode]) -> ListNode:
        """
        使用优先队列（最小堆）合并K个有序链表
        时间复杂度: O(N log K)，其中N是所有节点的总数，K是链表的数量
        空间复杂度: O(K)
        """
        import heapq
        
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        current = dummy
        
        # 创建优先队列，存储(节点值, 节点索引, 节点引用)
        # 使用节点索引是为了在值相等时能够稳定比较
        heap = []
        
        # 将每个链表的头节点加入堆中
        for i, head in enumerate(lists):
            if head:
                # 堆中的元素是元组，格式为(节点值, 节点索引, 节点引用)
                # 使用索引确保在节点值相等时可以稳定比较
                heapq.heappush(heap, (head.val, i, head))
        
        # 不断从堆中取出最小元素，然后将其下一个节点加入堆中
        while heap:
            val, i, node = heapq.heappop(heap)
            
            # 将当前最小节点添加到结果链表
            current.next = node
            current = current.next
            
            # 如果当前节点还有下一个节点，将其加入堆中
            if node.next:
                heapq.heappush(heap, (node.next.val, i, node.next))
        
        return dummy.next
    
    # 方法2: 分治法
    def mergeKListsDivideConquer(self, lists: list[ListNode]) -> ListNode:
        """
        使用分治法合并K个有序链表
        时间复杂度: O(N log K)
        空间复杂度: O(log K)，递归调用栈的深度
        """
        # 处理边界情况
        if not lists:
            return None
        
        # 辅助函数：合并两个有序链表
        def mergeTwoLists(l1, l2):
            dummy = ListNode(0)
            current = dummy
            
            while l1 and l2:
                if l1.val <= l2.val:
                    current.next = l1
                    l1 = l1.next
                else:
                    current.next = l2
                    l2 = l2.next
                current = current.next
            
            current.next = l1 if l1 else l2
            return dummy.next
        
        # 分治函数
        def divideAndConquer(lists, start, end):
            # 基本情况：只有一个链表
            if start == end:
                return lists[start]
            # 基本情况：没有链表
            if start > end:
                return None
            
            # 分：将链表数组分成两部分
            mid = (start + end) // 2
            # 治：递归合并左半部分和右半部分
            left = divideAndConquer(lists, start, mid)
            right = divideAndConquer(lists, mid + 1, end)
            # 合：合并两个有序链表
            return mergeTwoLists(left, right)
        
        # 调用分治函数合并所有链表
        return divideAndConquer(lists, 0, len(lists) - 1)
    
    # 方法3: 逐一合并法
    def mergeKListsIterative(self, lists: list[ListNode]) -> ListNode:
        """
        逐一合并链表
        时间复杂度: O(K*N)，其中K是链表的数量，N是所有节点的总数
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not lists:
            return None
        
        # 辅助函数：合并两个有序链表
        def mergeTwoLists(l1, l2):
            dummy = ListNode(0)
            current = dummy
            
            while l1 and l2:
                if l1.val <= l2.val:
                    current.next = l1
                    l1 = l1.next
                else:
                    current.next = l2
                    l2 = l2.next
                current = current.next
            
            current.next = l1 if l1 else l2
            return dummy.next
        
        # 从第一个链表开始，逐一合并后续链表
        result = lists[0]
        for i in range(1, len(lists)):
            result = mergeTwoLists(result, lists[i])
        
        return result
    
    # 方法4: 收集所有节点然后排序
    def mergeKListsCollectAndSort(self, lists: list[ListNode]) -> ListNode:
        """
        收集所有节点值，排序后重建链表
        时间复杂度: O(N log N)
        空间复杂度: O(N)
        """
        # 收集所有节点值
        values = []
        for head in lists:
            current = head
            while current:
                values.append(current.val)
                current = current.next
        
        # 对节点值排序
        values.sort()
        
        # 重建链表
        dummy = ListNode(0)
        current = dummy
        for val in values:
            current.next = ListNode(val)
            current = current.next
        
        return dummy.next

# 辅助函数：构建链表
from typing import List

def build_list(nums: List[int]) -> ListNode:
    dummy = ListNode(0)
    curr = dummy
    for num in nums:
        curr.next = ListNode(num)
        curr = curr.next
    return dummy.next

# 辅助函数：将链表转换为列表

def list_to_array(head: ListNode) -> List[int]:
    result = []
    while head:
        result.append(head.val)
        head = head.next
    return result

# 辅助函数：构建多个链表

def build_lists(list_of_nums: List[List[int]]) -> List[ListNode]:
    return [build_list(nums) for nums in list_of_nums]

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: lists = [[1,4,5],[1,3,4],[2,6]]
    lists1_nums = [[1, 4, 5], [1, 3, 4], [2, 6]]
    lists1 = build_lists(lists1_nums)
    print(f"测试用例1:\n原始链表: {lists1_nums}")
    
    # 测试优先队列法
    result1 = solution.mergeKLists(lists1)
    print(f"优先队列法结果: {list_to_array(result1)}")
    
    # 测试用例2: lists = []
    lists2 = []
    print(f"\n测试用例2:\n原始链表: []")
    
    result2 = solution.mergeKLists(lists2)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: lists = [[]]
    lists3 = [None]
    print(f"\n测试用例3:\n原始链表: [[]]")
    
    result3 = solution.mergeKLists(lists3)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: lists = [[1,2,3],[4,5,6],[7,8,9]]
    lists4_nums = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    lists4 = build_lists(lists4_nums)
    print(f"\n测试用例4:\n原始链表: {lists4_nums}")
    
    # 测试分治法
    result4 = solution.mergeKListsDivideConquer(lists4)
    print(f"分治法结果: {list_to_array(result4)}")
    
    # 测试用例5: lists = [[5,6,7],[1,2,3,4],[8,9,10]]
    lists5_nums = [[5, 6, 7], [1, 2, 3, 4], [8, 9, 10]]
    lists5 = build_lists(lists5_nums)
    print(f"\n测试用例5:\n原始链表: {lists5_nums}")
    
    # 测试逐一合并法
    result5 = solution.mergeKListsIterative(lists5)
    print(f"逐一合并法结果: {list_to_array(result5)}")
    
    # 测试用例6: lists = [[-1,0,2], [-3,1,5], [-2,3,6]]
    lists6_nums = [[-1, 0, 2], [-3, 1, 5], [-2, 3, 6]]
    lists6 = build_lists(lists6_nums)
    print(f"\n测试用例6:\n原始链表: {lists6_nums}")
    
    # 测试收集并排序法
    result6 = solution.mergeKListsCollectAndSort(lists6)
    print(f"收集并排序法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 23. 合并K个有序链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。

* 解题思路：
1. 优先队列（最小堆）法：
   - 使用优先队列维护每个链表的当前头节点
   - 每次从堆中取出值最小的节点加入结果链表
   - 然后将该节点的下一个节点加入堆中
   - 重复上述过程直到所有节点都被处理
2. 分治法：
   - 将K个链表两两分组，合并每组的两个链表
   - 然后将合并后的链表继续两两分组，直到只剩下一个链表
   - 递归实现这个过程
3. 逐一合并法：
   - 从第一个链表开始，逐一与后续链表合并
   - 合并两个链表可以使用LeetCode 21的解法
4. 收集所有节点然后排序：
   - 遍历所有链表，收集所有节点的值
   - 对收集到的值进行排序
   - 根据排序后的序列重建链表

* 时间复杂度：
- 优先队列法：O(N log K)，其中N是所有节点的总数，K是链表的数量
- 分治法：O(N log K)
- 逐一合并法：O(K*N)
- 收集并排序法：O(N log N)

* 空间复杂度：
- 优先队列法：O(K)
- 分治法：O(log K)，递归调用栈的深度
- 逐一合并法：O(1)
- 收集并排序法：O(N)

* 最优解：优先队列法或分治法，时间复杂度O(N log K)

* 工程化考量：
1. 优先队列法在实现时需要注意处理节点值相等的情况，通常通过添加索引来确保稳定比较
2. 分治法实现较为复杂，但时间复杂度最优
3. 逐一合并法实现简单，但时间复杂度较高
4. 收集并排序法对于大数据量可能会有内存问题

* 与机器学习等领域的联系：
1. 合并有序数据集是数据处理的基础操作
2. 优先队列在调度算法中有广泛应用
3. 分治法是算法设计的重要思想
4. 多路归并在外部排序等场景中很常见

* 语言特性差异：
Python: 使用heapq模块实现优先队列，需要注意元组比较的特性
Java: 使用PriorityQueue类，需要自定义比较器
C++: 使用priority_queue，需要注意默认是最大堆

* 算法深度分析：
合并K个有序链表是一个经典的链表操作问题，也是多路归并算法的一个具体应用。优先队列法和分治法是解决这个问题的最优方法，时间复杂度均为O(N log K)。

优先队列法的核心思想是维护每个链表的当前头节点，每次从这些头节点中选择最小值加入结果链表。这种方法可以高效地找到当前所有链表中的最小值，时间复杂度为O(log K)每次选择。优先队列法的优点是实现直观，适用于K值不是特别大的情况。

分治法的核心思想是将问题分解为更小的子问题，然后合并子问题的解。具体来说，我们可以将K个链表两两分组，合并每组的两个链表，然后将合并后的链表继续两两分组，直到只剩下一个链表。这种方法充分利用了已有的合并两个有序链表的算法，并且通过分治的思想降低了时间复杂度。

逐一合并法虽然实现简单，但时间复杂度较高，当K值较大时效率较低。收集并排序法虽然时间复杂度为O(N log N)，但需要额外的O(N)空间来存储所有节点的值，对于大数据量可能会有内存问题。

从更广泛的角度看，合并K个有序链表的问题体现了数据结构选择和算法设计的重要性。不同的实现方法有不同的时间和空间复杂度，适用于不同的场景。在实际应用中，我们需要根据具体情况选择合适的算法。

在大数据处理、外部排序、分布式系统等领域，多路归并是一个非常重要的技术。理解并掌握合并K个有序链表的算法有助于处理更复杂的大数据处理任务。
"""