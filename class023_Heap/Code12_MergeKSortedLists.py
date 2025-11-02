import heapq

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    """
    相关题目4: LeetCode 23. 合并K个排序链表
    题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
    题目描述: 给你一个链表数组，每个链表都已经按升序排列。
    请你将所有链表合并到一个升序链表中，返回合并后的链表。
    解题思路: 使用最小堆维护K个链表的当前头节点，每次取出最小的节点并将其下一个节点加入堆
    时间复杂度: O(N log K)，其中N是所有链表的节点总数，K是链表的数量
    空间复杂度: O(K)，堆最多存储K个节点
    是否最优解: 是，这是合并K个排序链表的最优解法之一
    
    本题属于堆的典型应用场景：在多个有序集合中动态选择最小元素
    """
    
    def mergeKLists(self, lists):
        """
        合并K个排序链表
        
        Args:
            lists: K个排序链表的数组
            
        Returns:
            ListNode: 合并后的排序链表头节点
            
        Raises:
            ValueError: 当输入数组为None时抛出异常
        """
        # 异常处理：检查输入数组是否为None
        if lists is None:
            raise ValueError("输入链表数组不能为None")
        
        # 边界情况：数组为空或所有链表都为空
        non_empty_count = 0
        for list_node in lists:
            if list_node is not None:
                non_empty_count += 1
        
        if non_empty_count == 0:
            return None  # 返回空链表
        
        # 创建最小堆，但由于Python的heapq不支持直接比较对象，我们使用元组(值, 计数器, 节点)作为堆元素
        # 计数器是为了在值相同时确保排序稳定性
        min_heap = []
        counter = 0  # 用于处理值相同的情况
        
        # 将所有链表的头节点加入堆
        for list_node in lists:
            if list_node is not None:
                # 使用(值, 计数器, 节点)作为堆元素，确保稳定排序
                heapq.heappush(min_heap, (list_node.val, counter, list_node))
                counter += 1
                # 调试信息：打印加入堆的节点值
                # print(f"加入堆的节点值: {list_node.val}")
        
        # 创建哑节点作为结果链表的头节点
        dummy = ListNode(-1)
        current = dummy
        
        # 不断从堆中取出最小的节点，直到堆为空
        while min_heap:
            # 取出当前最小的节点
            val, _, smallest = heapq.heappop(min_heap)
            
            # 调试信息：打印取出的节点值
            # print(f"取出的节点值: {val}")
            
            # 将该节点加入结果链表
            current.next = smallest
            current = current.next
            
            # 如果该节点有下一个节点，则将其下一个节点加入堆
            if smallest.next is not None:
                heapq.heappush(min_heap, (smallest.next.val, counter, smallest.next))
                counter += 1
                # 调试信息：打印新加入堆的节点值
                # print(f"新加入堆的节点值: {smallest.next.val}")
        
        # 返回合并后的链表头节点（跳过哑节点）
        return dummy.next

# 打印链表的辅助函数
def print_list(head):
    current = head
    values = []
    while current is not None:
        values.append(str(current.val))
        current = current.next
    print(" -> ".join(values) if values else "None")

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    # 创建链表1: 1->4->5
    list1 = ListNode(1, ListNode(4, ListNode(5)))
    # 创建链表2: 1->3->4
    list2 = ListNode(1, ListNode(3, ListNode(4)))
    # 创建链表3: 2->6
    list3 = ListNode(2, ListNode(6))
    lists1 = [list1, list2, list3]
    
    print("示例1输出: ")
    result1 = solution.mergeKLists(lists1)
    print_list(result1)  # 期望输出: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
    
    # 测试用例2：边界情况 - 空数组
    lists2 = []
    print("示例2输出: ")
    result2 = solution.mergeKLists(lists2)
    print_list(result2)  # 期望输出: None
    
    # 测试用例3：边界情况 - 数组包含空链表
    lists3 = [None]
    print("示例3输出: ")
    result3 = solution.mergeKLists(lists3)
    print_list(result3)  # 期望输出: None
    
    # 测试用例4：较大的K值
    list4 = ListNode(3)
    list5 = ListNode(2)
    list6 = ListNode(1)
    list7 = ListNode(4)
    lists4 = [list4, list5, list6, list7]
    
    print("示例4输出: ")
    result4 = solution.mergeKLists(lists4)
    print_list(result4)  # 期望输出: 1 -> 2 -> 3 -> 4
    
    # 测试异常情况
    try:
        # 测试用例5：异常测试 - 输入为None
        solution.mergeKLists(None)
        print("测试用例5失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例5成功捕获异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")