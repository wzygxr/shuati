# 删除排序链表中的重复元素 - LeetCode 83
# 测试链接: https://leetcode.cn/problems/remove-duplicates-from-sorted-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        """
        迭代删除排序链表中的重复元素，每个元素只保留一个
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head:
            return None
        
        # 当前指针，用于遍历链表
        current = head
        
        # 遍历链表
        while current.next:
            # 如果当前节点和下一个节点的值相等，删除下一个节点
            if current.val == current.next.val:
                current.next = current.next.next
            else:
                # 否则，移动到下一个节点
                current = current.next
        
        return head
    
    # 方法2: 递归法
    def deleteDuplicatesRecursive(self, head: ListNode) -> ListNode:
        """
        递归删除排序链表中的重复元素
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：链表为空或只有一个节点
        if not head or not head.next:
            return head
        
        # 递归处理剩余部分
        head.next = self.deleteDuplicatesRecursive(head.next)
        
        # 如果当前节点和下一个节点的值相等，跳过当前节点
        if head.val == head.next.val:
            return head.next
        else:
            return head
    
    # 方法3: 双指针法（更清晰的实现）
    def deleteDuplicatesTwoPointers(self, head: ListNode) -> ListNode:
        """
        使用双指针删除排序链表中的重复元素
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head:
            return None
        
        # 慢指针指向已处理链表的末尾
        slow = head
        # 快指针用于遍历链表
        fast = head.next
        
        while fast:
            # 如果快慢指针指向的值不同
            if slow.val != fast.val:
                # 将慢指针移动一位，并更新值
                slow = slow.next
                slow.val = fast.val
            # 快指针继续向前移动
            fast = fast.next
        
        # 断开慢指针后面的连接
        slow.next = None
        
        return head
    
    # 方法4: 集合去重法（不推荐，因为题目要求只保留一个重复元素，且链表已排序）
    def deleteDuplicatesSet(self, head: ListNode) -> ListNode:
        """
        使用集合记录已出现的值，删除重复元素
        时间复杂度: O(n)
        空间复杂度: O(n)
        注意：此方法适用于未排序链表，但对于已排序链表效率不如其他方法
        """
        # 处理边界情况
        if not head:
            return None
        
        # 创建一个集合用于记录已出现的值
        seen = set()
        # 前驱节点，用于删除操作
        prev = None
        current = head
        
        while current:
            # 如果当前节点的值已经在集合中
            if current.val in seen:
                # 删除当前节点
                prev.next = current.next
            else:
                # 将当前节点的值加入集合
                seen.add(current.val)
                # 更新前驱节点
                prev = current
            # 移动到下一个节点
            current = current.next
        
        return head

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
    
    # 测试用例1: [1,1,2]
    head1 = build_list([1, 1, 2])
    print("测试用例1: [1,1,2]")
    
    # 测试迭代法
    result1 = solution.deleteDuplicates(head1)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: [1,1,2,3,3]
    head2 = build_list([1, 1, 2, 3, 3])
    print("\n测试用例2: [1,1,2,3,3]")
    
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
    
    # 测试用例5: [1,1,1,2,2,3,3,3,4]
    head5 = build_list([1, 1, 1, 2, 2, 3, 3, 3, 4])
    print("\n测试用例5: [1,1,1,2,2,3,3,3,4]")
    
    # 测试递归法
    result5 = solution.deleteDuplicatesRecursive(head5)
    print(f"递归法结果: {list_to_array(result5)}")
    
    # 测试用例6: [-10,-10,-5,-5,0,0,5,5]
    head6 = build_list([-10, -10, -5, -5, 0, 0, 5, 5])
    print("\n测试用例6: [-10,-10,-5,-5,0,0,5,5]")
    
    # 测试双指针法
    result6 = solution.deleteDuplicatesTwoPointers(head6)
    print(f"双指针法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 83. 删除排序链表中的重复元素
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给定一个已排序的链表的头 head ， 删除所有重复的元素，使每个元素只出现一次 。返回 已排序的链表 。

* 解题思路：
1. 迭代法：
   - 遍历链表，对于每个节点，检查它和下一个节点的值是否相等
   - 如果相等，删除下一个节点（通过跳过它）
   - 如果不相等，移动到下一个节点
2. 递归法：
   - 基本情况：链表为空或只有一个节点时直接返回
   - 递归处理当前节点之后的链表
   - 比较当前节点和递归后返回的头节点的值，删除重复
3. 双指针法：
   - 使用慢指针指向已处理链表的末尾
   - 使用快指针遍历链表
   - 当快慢指针指向的值不同时，将慢指针向前移动并更新值
4. 集合去重法：
   - 使用集合记录已出现的值
   - 遍历链表，删除值已在集合中的节点
   - 注意：此方法适用于未排序链表，但对于已排序链表效率不如其他方法

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 迭代法和双指针法：O(1)
- 递归法：O(n)，递归调用栈的深度
- 集合去重法：O(n)

* 最优解：迭代法，时间复杂度O(n)，空间复杂度O(1)，实现简单

* 工程化考量：
1. 迭代法是首选，实现简单，逻辑清晰
2. 递归法代码简洁，但对于长链表可能导致栈溢出
3. 双指针法也是一种有效的实现方式
4. 集合去重法不适合这个问题，因为链表已经排序，不需要额外的空间

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
删除排序链表中的重复元素是一个经典的链表操作问题，主要考察对链表特性的理解和指针操作的能力。迭代法是解决这个问题的最优方法，其核心思想是通过遍历链表，跳过重复的节点。

具体来说，迭代法的步骤如下：
1. 从链表的头节点开始遍历。
2. 对于当前节点，检查它和下一个节点的值是否相等。
3. 如果相等，说明下一个节点是重复的，我们可以通过将当前节点的next指针指向下一个节点的next指针来删除下一个节点。
4. 如果不相等，则将当前节点向前移动一位，继续检查。

由于链表已经排序，所以重复的元素一定是连续的，这使得我们可以在一次遍历中完成去重操作。

递归法的思路也很清晰。我们可以将问题分解为：删除当前节点之后的链表中的重复元素，然后处理当前节点和去重后的链表头节点之间的关系。如果它们的值相等，说明当前节点也是重复的，我们应该返回去重后的链表头节点；否则，我们应该将当前节点与去重后的链表连接起来，并返回当前节点。

双指针法虽然也能达到O(1)的空间复杂度，但在这个问题中，迭代法的实现更为简单直观。集合去重法虽然也能解决问题，但由于链表已经排序，我们可以利用这个特性来优化算法，避免使用额外的空间。

在实际应用中，去重操作是数据处理的常见需求。理解并掌握删除排序链表中的重复元素的算法有助于处理更复杂的数据处理任务。

此外，这个问题还体现了一个重要的算法设计原则：在处理排序数据时，我们可以利用数据已排序的特性来优化算法，减少不必要的操作和空间使用。这是一个在很多算法问题中都适用的原则。
"""