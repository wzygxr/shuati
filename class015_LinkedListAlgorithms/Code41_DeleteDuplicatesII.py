# 删除排序链表中的重复元素II - LeetCode 82
# 测试链接: https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法（使用哑节点）
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        """
        删除排序链表中所有重复出现的元素，只保留没有重复的元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next:
            return head
        
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 前驱节点，用于删除操作
        prev = dummy
        # 当前节点，用于遍历链表
        curr = head
        
        while curr:
            # 标记是否有重复
            has_duplicate = False
            
            # 找到所有连续相等的节点
            while curr.next and curr.val == curr.next.val:
                has_duplicate = True
                curr = curr.next
            
            # 如果有重复，跳过所有相等的节点
            if has_duplicate:
                prev.next = curr.next
            else:
                # 如果没有重复，移动前驱节点
                prev = prev.next
            
            # 移动当前节点
            curr = curr.next
        
        return dummy.next
    
    # 方法2: 递归法
    def deleteDuplicatesRecursive(self, head: ListNode) -> ListNode:
        """
        使用递归删除排序链表中所有重复出现的元素
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：链表为空或只有一个节点
        if not head or not head.next:
            return head
        
        # 检查当前节点和下一个节点是否相等
        if head.val == head.next.val:
            # 跳过所有相等的节点
            while head.next and head.val == head.next.val:
                head = head.next
            # 递归处理剩余部分
            return self.deleteDuplicatesRecursive(head.next)
        else:
            # 递归处理当前节点之后的链表
            head.next = self.deleteDuplicatesRecursive(head.next)
            return head
    
    # 方法3: 计数法
    def deleteDuplicatesCount(self, head: ListNode) -> ListNode:
        """
        先统计每个值出现的次数，然后删除出现次数大于1的值对应的所有节点
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        from collections import defaultdict
        
        # 处理边界情况
        if not head or not head.next:
            return head
        
        # 统计每个值出现的次数
        count = defaultdict(int)
        current = head
        while current:
            count[current.val] += 1
            current = current.next
        
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 前驱节点，用于删除操作
        prev = dummy
        # 当前节点，用于遍历链表
        curr = head
        
        while curr:
            # 如果当前节点的值出现次数大于1，删除当前节点
            if count[curr.val] > 1:
                prev.next = curr.next
            else:
                # 如果当前节点的值只出现一次，移动前驱节点
                prev = prev.next
            # 移动当前节点
            curr = curr.next
        
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
    
    # 测试用例1: [1,2,3,3,4,4,5]
    head1 = build_list([1, 2, 3, 3, 4, 4, 5])
    print("测试用例1: [1,2,3,3,4,4,5]")
    
    # 测试迭代法
    result1 = solution.deleteDuplicates(head1)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: [1,1,1,2,3]
    head2 = build_list([1, 1, 1, 2, 3])
    print("\n测试用例2: [1,1,1,2,3]")
    
    result2 = solution.deleteDuplicates(head2)
    print(f"迭代法结果: {list_to_array(result2)}")
    
    # 测试用例3: []
    head3 = None
    print("\n测试用例3: []")
    
    result3 = solution.deleteDuplicates(head3)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1]
    head4 = build_list([1])
    print("\n测试用例4: [1]")
    
    result4 = solution.deleteDuplicates(head4)
    print(f"结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,1,2,2,3,3,4,4]
    head5 = build_list([1, 1, 2, 2, 3, 3, 4, 4])
    print("\n测试用例5: [1,1,2,2,3,3,4,4]")
    
    # 测试递归法
    result5 = solution.deleteDuplicatesRecursive(head5)
    print(f"递归法结果: {list_to_array(result5)}")
    
    # 测试用例6: [-3,-1,-1,0,0,0,1,1,2,3,3]
    head6 = build_list([-3, -1, -1, 0, 0, 0, 1, 1, 2, 3, 3])
    print("\n测试用例6: [-3,-1,-1,0,0,0,1,1,2,3,3]")
    
    # 测试计数法
    result6 = solution.deleteDuplicatesCount(head6)
    print(f"计数法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 82. 删除排序链表中的重复元素II
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只保留原始链表中 没有重复出现 的数字。返回 已排序的链表 。

* 解题思路：
1. 迭代法（使用哑节点）：
   - 创建哑节点，简化头节点的处理
   - 使用两个指针：prev指向已处理部分的末尾，curr用于遍历链表
   - 当发现重复节点时，跳过所有连续相等的节点
   - 否则，移动prev指针
2. 递归法：
   - 基本情况：链表为空或只有一个节点时直接返回
   - 如果当前节点和下一个节点值相等，跳过所有相等的节点，递归处理剩余部分
   - 否则，递归处理当前节点之后的链表
3. 计数法：
   - 第一次遍历链表，统计每个值出现的次数
   - 第二次遍历链表，删除出现次数大于1的值对应的所有节点

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 迭代法：O(1)
- 递归法：O(n)，递归调用栈的深度
- 计数法：O(n)，用于存储每个值的出现次数

* 最优解：迭代法，时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 迭代法是首选，实现简单，逻辑清晰
2. 递归法代码简洁，但对于长链表可能导致栈溢出
3. 计数法需要两次遍历，但实现直观
4. 使用哑节点可以有效处理头节点可能被删除的情况

* 与机器学习等领域的联系：
1. 去重是数据处理的基本操作
2. 链表操作是数据结构的基础
3. 迭代和递归是算法设计的两种基本范式
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
删除排序链表中的重复元素II是LeetCode 83的升级版，要求删除所有重复出现的元素，而不仅仅是重复的多余元素。这个问题的关键在于识别连续的重复节点，并将它们全部删除。

迭代法是解决这个问题的最优方法，其核心思想是：
1. 创建一个哑节点，使其指向链表的头节点。这样可以方便地处理头节点可能被删除的情况。
2. 使用两个指针：prev指向已处理部分的末尾，curr用于遍历链表。
3. 对于每个节点，检查它和下一个节点的值是否相等。如果相等，说明找到了重复节点，我们需要跳过所有连续相等的节点。
4. 当处理完重复节点后，如果有重复，我们将prev的next指针指向curr的下一个节点，从而删除所有重复的节点；否则，我们将prev向前移动一位。

递归法的思路也很清晰。我们可以将问题分解为：
1. 如果当前节点和下一个节点值相等，说明这两个节点都应该被删除。我们跳过所有连续相等的节点，然后递归处理剩余部分。
2. 如果当前节点和下一个节点值不相等，那么当前节点应该被保留，我们递归处理当前节点之后的链表，并将处理结果连接到当前节点之后。

计数法的思路更加直观。我们首先统计每个值出现的次数，然后再次遍历链表，删除所有出现次数大于1的值对应的节点。这种方法需要两次遍历，但实现简单，容易理解。

在这三种方法中，迭代法是最优的，因为它只需要一次遍历，并且空间复杂度为O(1)。递归法虽然代码简洁，但对于长链表可能导致栈溢出。计数法虽然实现直观，但需要额外的空间来存储每个值的出现次数。

在实际应用中，删除重复元素的操作是数据处理的常见需求。理解并掌握删除排序链表中的重复元素II的算法有助于处理更复杂的数据处理任务。

此外，这个问题还体现了一个重要的算法设计原则：在处理链表问题时，适当地使用哑节点可以简化对头节点的处理，减少边界条件的检查。同时，保持链表的有序性也是一个重要的要求，这在很多实际应用中都很重要。
"""