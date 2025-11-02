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
        删除排序链表中的所有重复元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # prev指针指向当前已处理链表的末尾
        prev = dummy
        
        # 遍历链表
        while prev.next and prev.next.next:
            # 如果当前节点和下一个节点值相同
            if prev.next.val == prev.next.next.val:
                # 记录重复的值
                duplicate_val = prev.next.val
                # 跳过所有具有重复值的节点
                while prev.next and prev.next.val == duplicate_val:
                    prev.next = prev.next.next
            else:
                # 没有重复，移动prev指针
                prev = prev.next
        
        return dummy.next
    
    # 方法2: 递归法
    def deleteDuplicatesRecursive(self, head: ListNode) -> ListNode:
        """
        递归实现删除排序链表中的所有重复元素
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：链表为空或只有一个节点
        if not head or not head.next:
            return head
        
        # 检查当前节点是否与下一个节点重复
        if head.val == head.next.val:
            # 跳过所有具有相同值的节点
            while head.next and head.val == head.next.val:
                head = head.next
            # 递归处理下一个不同值的节点
            return self.deleteDuplicatesRecursive(head.next)
        else:
            # 当前节点不重复，递归处理下一个节点
            head.next = self.deleteDuplicatesRecursive(head.next)
            return head
    
    # 方法3: 迭代法（使用集合记录重复值）
    def deleteDuplicatesWithSet(self, head: ListNode) -> ListNode:
        """
        使用集合记录重复值，两次遍历删除重复元素
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 第一次遍历：记录所有重复的值
        duplicates = set()
        current = head
        while current and current.next:
            if current.val == current.next.val:
                duplicates.add(current.val)
            current = current.next
        
        # 第二次遍历：删除所有具有重复值的节点
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy
        current = head
        
        while current:
            if current.val in duplicates:
                # 删除当前节点
                prev.next = current.next
                current = current.next
            else:
                # 移动指针
                prev = current
                current = current.next
        
        return dummy.next
    
    # 方法4: 双指针法
    def deleteDuplicatesTwoPointers(self, head: ListNode) -> ListNode:
        """
        使用双指针删除重复元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建哑节点
        dummy = ListNode(0)
        dummy.next = head
        
        # 前驱指针
        prev = dummy
        
        while head:
            # 检查当前节点是否有重复
            if head.next and head.val == head.next.val:
                # 找到所有重复节点的末尾
                while head.next and head.val == head.next.val:
                    head = head.next
                # 跳过所有重复节点
                prev.next = head.next
            else:
                # 没有重复，移动prev指针
                prev = prev.next
            # 移动head指针
            head = head.next
        
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
    nums1 = [1, 2, 3, 3, 4, 4, 5]
    head1 = build_list(nums1)
    print(f"测试用例1:\n原始链表: {nums1}")
    
    # 测试迭代法（使用哑节点）
    result1 = solution.deleteDuplicates(head1)
    print(f"迭代法（使用哑节点）结果: {list_to_array(result1)}")
    
    # 测试用例2: [1,1,1,2,3]
    nums2 = [1, 1, 1, 2, 3]
    head2 = build_list(nums2)
    print(f"\n测试用例2:\n原始链表: {nums2}")
    
    result2 = solution.deleteDuplicates(head2)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: []
    head3 = None
    print(f"\n测试用例3:\n原始链表: []")
    
    result3 = solution.deleteDuplicates(head3)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1]
    nums4 = [1]
    head4 = build_list(nums4)
    print(f"\n测试用例4:\n原始链表: {nums4}")
    
    result4 = solution.deleteDuplicates(head4)
    print(f"结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,1]
    nums5 = [1, 1]
    head5 = build_list(nums5)
    print(f"\n测试用例5:\n原始链表: {nums5}")
    
    # 测试递归法
    result5 = solution.deleteDuplicatesRecursive(head5)
    print(f"递归法结果: {list_to_array(result5)}")
    
    # 测试用例6: [1,2,2,3,4,4,4,5,5]
    nums6 = [1, 2, 2, 3, 4, 4, 4, 5, 5]
    head6 = build_list(nums6)
    print(f"\n测试用例6:\n原始链表: {nums6}")
    
    # 测试集合方法
    result6 = solution.deleteDuplicatesWithSet(head6)
    print(f"集合方法结果: {list_to_array(result6)}")
    
    # 测试用例7: [1,1,2,2,3,3,4,4,5,5]
    nums7 = [1, 1, 2, 2, 3, 3, 4, 4, 5, 5]
    head7 = build_list(nums7)
    print(f"\n测试用例7:\n原始链表: {nums7}")
    
    # 测试双指针法
    result7 = solution.deleteDuplicatesTwoPointers(head7)
    print(f"双指针法结果: {list_to_array(result7)}")

"""
* 题目扩展：LeetCode 82. 删除排序链表中的重复元素II
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只留下不同的数字 。返回 已排序的链表 。

* 解题思路：
1. 迭代法（使用哑节点）：
   - 创建哑节点简化头节点处理
   - 使用prev指针跟踪当前已处理链表的末尾
   - 当发现重复时，跳过所有相同值的节点
   - 否则，正常移动prev指针
2. 递归法：
   - 基本情况：链表为空或只有一个节点
   - 检查当前节点是否与下一个节点重复
   - 如果重复，跳过所有相同值的节点，递归处理剩余部分
   - 如果不重复，递归处理下一个节点并连接到当前节点
3. 迭代法（使用集合记录重复值）：
   - 第一次遍历：记录所有重复的值
   - 第二次遍历：删除所有具有重复值的节点
4. 双指针法：
   - 使用prev和head两个指针
   - 当发现重复时，跳过所有相同值的节点
   - 否则，正常移动prev指针

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表长度

* 空间复杂度：
- 迭代法（使用哑节点）、双指针法：O(1)
- 递归法：O(n)，递归调用栈的深度
- 迭代法（使用集合记录重复值）：O(n)

* 最优解：迭代法（使用哑节点），实现简洁，空间复杂度O(1)

* 工程化考量：
1. 使用哑节点可以统一处理逻辑，避免特殊处理头节点
2. 递归方法对于非常长的链表可能导致栈溢出
3. 注意边界情况：空链表、单节点链表、所有节点都重复等
4. 指针操作需要小心，避免链表断裂

* 与机器学习等领域的联系：
1. 去重操作在数据预处理中很常见
2. 链表操作是数据结构的基础，在很多算法中都有应用
3. 递归思想在分治算法中有广泛应用
4. 集合数据结构在快速查找中有重要作用

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，引用传递
C++: 需要注意指针操作和内存管理

* 算法深度分析：
删除排序链表中的重复元素II是一个经典的链表操作问题，与删除排序链表中的重复元素I不同，本题要求删除所有重复出现的节点，而不是保留一个。这使得问题更加复杂，因为需要删除节点本身，而不仅仅是跳过重复的值。

迭代方法使用哑节点和prev指针来跟踪当前已处理链表的末尾。当发现重复时，通过循环跳过所有相同值的节点，然后将prev指针的next指向第一个不同值的节点。这种方法的关键在于正确地管理指针，确保链表不会断裂。

递归方法的思路更为简洁：每次处理当前节点，检查是否与下一个节点重复。如果重复，跳过所有相同值的节点，然后递归处理剩余部分；如果不重复，递归处理下一个节点并连接到当前节点。递归方法的优点是代码简洁，但需要额外的栈空间。

使用集合记录重复值的方法虽然空间复杂度较高，但思路直观，分为两次遍历：第一次记录所有重复的值，第二次删除所有具有重复值的节点。

从更广泛的角度看，这个问题体现了链表操作中的一个重要技巧：通过适当的指针管理和遍历策略，可以高效地处理复杂的节点删除操作。这种思想在很多链表操作问题中都有应用。

在实际应用中，删除重复元素的操作在数据清洗、去重等场景中非常常见。理解并掌握这类问题的解法有助于处理更复杂的数据处理任务。
"""