# 合并两个有序链表 - LeetCode 21
# 测试链接: https://leetcode.cn/problems/merge-two-sorted-lists/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法
    def mergeTwoLists(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        合并两个有序链表
        时间复杂度: O(n + m)，其中n和m是两个链表的长度
        空间复杂度: O(1)
        """
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        current = dummy
        
        # 同时遍历两个链表，比较节点值的大小
        while list1 and list2:
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余的节点
        current.next = list1 if list1 else list2
        
        return dummy.next
    
    # 方法2: 递归法
    def mergeTwoListsRecursive(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        递归实现合并两个有序链表
        时间复杂度: O(n + m)
        空间复杂度: O(n + m)，递归调用栈的深度
        """
        # 基本情况：如果其中一个链表为空，返回另一个链表
        if not list1:
            return list2
        if not list2:
            return list1
        
        # 比较两个链表的头节点
        if list1.val <= list2.val:
            # list1的头节点较小，递归合并list1的剩余部分和list2
            list1.next = self.mergeTwoListsRecursive(list1.next, list2)
            return list1
        else:
            # list2的头节点较小，递归合并list1和list2的剩余部分
            list2.next = self.mergeTwoListsRecursive(list1, list2.next)
            return list2
    
    # 方法3: 迭代法（不使用哑节点）
    def mergeTwoListsNoDummy(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        不使用哑节点的迭代实现
        时间复杂度: O(n + m)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not list1:
            return list2
        if not list2:
            return list1
        
        # 确定头节点
        if list1.val <= list2.val:
            head = list1
            list1 = list1.next
        else:
            head = list2
            list2 = list2.next
        
        current = head
        
        # 合并剩余节点
        while list1 and list2:
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余部分
        current.next = list1 if list1 else list2
        
        return head
    
    # 方法4: 使用优先级队列（堆）
    def mergeTwoListsHeap(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        使用优先级队列合并两个有序链表
        时间复杂度: O((n + m) * log(2)) = O(n + m)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not list1:
            return list2
        if not list2:
            return list1
        
        # 创建哑节点
        dummy = ListNode(0)
        current = dummy
        
        # 遍历两个链表
        while list1 and list2:
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余部分
        current.next = list1 if list1 else list2
        
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

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: list1 = [1,2,4], list2 = [1,3,4]
    nums1 = [1, 2, 4]
    nums2 = [1, 3, 4]
    list1 = build_list(nums1)
    list2 = build_list(nums2)
    
    print(f"测试用例1:\nlist1: {nums1}, list2: {nums2}")
    
    # 测试迭代法
    result1 = solution.mergeTwoLists(list1, list2)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: list1 = [], list2 = []
    list3 = None
    list4 = None
    print(f"\n测试用例2:\nlist1: [], list2: []")
    
    result2 = solution.mergeTwoLists(list3, list4)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: list1 = [], list2 = [0]
    list5 = None
    nums3 = [0]
    list6 = build_list(nums3)
    print(f"\n测试用例3:\nlist1: [], list2: {nums3}")
    
    result3 = solution.mergeTwoLists(list5, list6)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: list1 = [5], list2 = [1,2,3,4]
    nums4 = [5]
    nums5 = [1, 2, 3, 4]
    list7 = build_list(nums4)
    list8 = build_list(nums5)
    print(f"\n测试用例4:\nlist1: {nums4}, list2: {nums5}")
    
    # 测试递归法
    result4_recursive = solution.mergeTwoListsRecursive(list7, list8)
    print(f"递归法结果: {list_to_array(result4_recursive)}")
    
    # 测试用例5: list1 = [2,6,8], list2 = [1,3,5,7]
    nums6 = [2, 6, 8]
    nums7 = [1, 3, 5, 7]
    list9 = build_list(nums6)
    list10 = build_list(nums7)
    print(f"\n测试用例5:\nlist1: {nums6}, list2: {nums7}")
    
    # 测试不使用哑节点的方法
    result5_no_dummy = solution.mergeTwoListsNoDummy(list9, list10)
    print(f"不使用哑节点方法结果: {list_to_array(result5_no_dummy)}")
    
    # 测试用例6: list1 = [1,1,2], list2 = [1,3,4,4]
    nums8 = [1, 1, 2]
    nums9 = [1, 3, 4, 4]
    list11 = build_list(nums8)
    list12 = build_list(nums9)
    print(f"\n测试用例6:\nlist1: {nums8}, list2: {nums9}")
    
    # 测试堆方法
    result6_heap = solution.mergeTwoListsHeap(list11, list12)
    print(f"堆方法结果: {list_to_array(result6_heap)}")

"""
* 题目扩展：LeetCode 21. 合并两个有序链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

* 解题思路：
1. 迭代法：
   - 创建哑节点简化头节点处理
   - 同时遍历两个链表，比较节点值的大小
   - 将较小值的节点添加到结果链表中
   - 处理剩余节点
2. 递归法：
   - 比较两个链表的头节点
   - 递归合并较小头节点的剩余部分与另一个链表
3. 迭代法（不使用哑节点）：
   - 手动处理头节点
   - 然后进行与方法1类似的迭代合并
4. 使用优先级队列：
   - 对于合并两个链表，优先级队列优势不明显
   - 但这种方法可以扩展到合并k个有序链表

* 时间复杂度：
所有方法的时间复杂度均为 O(n + m)，其中n和m是两个链表的长度

* 空间复杂度：
- 迭代法、迭代法（不使用哑节点）：O(1)
- 递归法：O(n + m)，递归调用栈的深度
- 使用优先级队列：O(1)（对于两个链表的情况）

* 最优解：迭代法，实现简单，空间复杂度O(1)

* 工程化考量：
1. 使用哑节点可以统一处理逻辑，避免特殊处理头节点
2. 递归方法对于非常长的链表可能导致栈溢出
3. 处理边界情况：空链表的情况
4. 确保指针操作正确，避免链表断裂

* 与机器学习等领域的联系：
1. 合并操作在归并排序等算法中是基础
2. 在数据合并、特征融合等场景中可能用到
3. 递归思想在分治算法中有广泛应用
4. 有序数据的合并在数据流处理中很常见

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，引用传递
C++: 需要注意指针操作和内存管理

* 算法深度分析：
合并两个有序链表是一个经典的链表操作问题，也是归并排序算法的核心操作之一。迭代方法通过维护一个结果链表的指针，逐步将两个链表中的节点按顺序添加到结果链表中。递归方法则利用了函数调用栈来隐式地维护合并的顺序，代码更加简洁优雅，但对于特别长的链表可能会有栈溢出的风险。

从更广泛的角度看，合并有序链表的思想可以扩展到合并k个有序链表（LeetCode 23），这时通常会使用优先队列（最小堆）来提高效率。合并操作的核心思想是利用数据已经有序的特性，通过比较和选择的方式构建新的有序序列，这种思想在很多排序和搜索算法中都有应用。

在实际应用中，合并有序链表的算法常用于合并两个有序数据集、合并日志文件、实现外部排序等场景。理解这个问题有助于掌握更复杂的链表操作和排序算法。
"""