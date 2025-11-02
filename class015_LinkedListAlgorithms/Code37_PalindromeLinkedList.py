# 回文链表 - LeetCode 234
# 测试链接: https://leetcode.cn/problems/palindrome-linked-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 将值复制到数组中再检查回文
    def isPalindromeArray(self, head: ListNode) -> bool:
        """
        将链表值复制到数组中，然后使用双指针检查是否为回文
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 创建一个数组用于存储链表节点的值
        values = []
        
        # 遍历链表，将值添加到数组中
        current = head
        while current:
            values.append(current.val)
            current = current.next
        
        # 使用双指针检查是否为回文
        left, right = 0, len(values) - 1
        while left < right:
            if values[left] != values[right]:
                return False
            left += 1
            right -= 1
        
        return True
    
    # 方法2: 快慢指针 + 反转后半部分链表
    def isPalindrome(self, head: ListNode) -> bool:
        """
        使用快慢指针找到链表中点，反转后半部分，然后比较
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 边界情况处理
        if not head or not head.next:
            return True
        
        # 步骤1: 使用快慢指针找到链表的中点
        slow = head
        fast = head
        
        while fast.next and fast.next.next:
            slow = slow.next      # 慢指针每次移动一步
            fast = fast.next.next  # 快指针每次移动两步
        
        # 此时slow指向链表的中点（如果链表长度为奇数）或前半部分的最后一个节点（如果链表长度为偶数）
        
        # 步骤2: 反转后半部分链表
        second_half_head = self._reverseList(slow.next)
        
        # 保存反转后的头节点，用于后续恢复链表
        second_half_start = second_half_head
        
        # 步骤3: 比较前半部分和反转后的后半部分
        first_half_head = head
        result = True
        
        while second_half_head:
            if first_half_head.val != second_half_head.val:
                result = False
                break
            first_half_head = first_half_head.next
            second_half_head = second_half_head.next
        
        # 步骤4: 恢复链表（可选，但在实际应用中是良好实践）
        slow.next = self._reverseList(second_half_start)
        
        return result
    
    # 辅助函数：反转链表
    def _reverseList(self, head: ListNode) -> ListNode:
        prev = None
        curr = head
        while curr:
            next_temp = curr.next
            curr.next = prev
            prev = curr
            curr = next_temp
        return prev
    
    # 方法3: 递归法
    def isPalindromeRecursive(self, head: ListNode) -> bool:
        """
        使用递归检查链表是否为回文
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 全局变量，用于在递归过程中从左到右遍历链表
        self.front_pointer = head
        
        # 辅助递归函数，从右到左遍历链表
        def recursively_check(current_node):
            if current_node:
                # 递归到链表末尾
                if not recursively_check(current_node.next):
                    return False
                # 比较当前节点和全局指针指向的节点
                if current_node.val != self.front_pointer.val:
                    return False
                # 全局指针向前移动
                self.front_pointer = self.front_pointer.next
            return True
        
        return recursively_check(head)
    
    # 方法4: 栈实现
    def isPalindromeStack(self, head: ListNode) -> bool:
        """
        使用栈检查链表是否为回文
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 处理边界情况
        if not head or not head.next:
            return True
        
        # 创建一个栈
        stack = []
        current = head
        
        # 计算链表长度
        length = 0
        while current:
            length += 1
            current = current.next
        
        # 将前半部分节点值压入栈中
        current = head
        for _ in range(length // 2):
            stack.append(current.val)
            current = current.next
        
        # 如果链表长度为奇数，跳过中间节点
        if length % 2 == 1:
            current = current.next
        
        # 比较后半部分节点值与栈中弹出的值
        while current:
            if current.val != stack.pop():
                return False
            current = current.next
        
        return True

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
    
    # 测试用例1: [1,2,2,1]
    head1 = build_list([1, 2, 2, 1])
    print("测试用例1: [1,2,2,1]")
    
    # 测试数组法
    print(f"数组法结果: {solution.isPalindromeArray(head1)}")
    # 验证链表在方法2中被正确恢复
    print(f"原始链表: {list_to_array(head1)}")
    # 测试最优方法
    print(f"快慢指针+反转法结果: {solution.isPalindrome(head1)}")
    print(f"恢复后的链表: {list_to_array(head1)}")
    
    # 测试用例2: [1,2]
    head2 = build_list([1, 2])
    print("\n测试用例2: [1,2]")
    print(f"数组法结果: {solution.isPalindromeArray(head2)}")
    
    # 测试用例3: [1]
    head3 = build_list([1])
    print("\n测试用例3: [1]")
    print(f"结果: {solution.isPalindrome(head3)}")
    
    # 测试用例4: []
    head4 = None
    print("\n测试用例4: []")
    print(f"结果: {solution.isPalindrome(head4)}")
    
    # 测试用例5: [1,2,3,2,1]
    head5 = build_list([1, 2, 3, 2, 1])
    print("\n测试用例5: [1,2,3,2,1]")
    
    # 测试递归法
    print(f"递归法结果: {solution.isPalindromeRecursive(head5)}")
    
    # 测试用例6: [-10,-5,0,-5,-10]
    head6 = build_list([-10, -5, 0, -5, -10])
    print("\n测试用例6: [-10,-5,0,-5,-10]")
    
    # 测试栈实现
    print(f"栈实现结果: {solution.isPalindromeStack(head6)}")
    
    # 测试用例7: [1,1,2,1]
    head7 = build_list([1, 1, 2, 1])
    print("\n测试用例7: [1,1,2,1]")
    print(f"快慢指针+反转法结果: {solution.isPalindrome(head7)}")

"""
* 题目扩展：LeetCode 234. 回文链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。

* 解题思路：
1. 将值复制到数组中再检查回文：
   - 遍历链表，将节点值复制到数组中
   - 使用双指针从数组两端向中间移动，比较值是否相等
2. 快慢指针 + 反转后半部分链表：
   - 使用快慢指针找到链表的中点
   - 反转后半部分链表
   - 比较前半部分和反转后的后半部分
   - 恢复链表（可选，但在实际应用中是良好实践）
3. 递归法：
   - 使用递归从右到左遍历链表
   - 同时使用一个全局指针从左到右遍历链表
   - 比较对应位置的节点值
4. 栈实现：
   - 将前半部分节点值压入栈中
   - 对于后半部分节点，与栈中弹出的值进行比较

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 数组法、递归法、栈实现：O(n)
- 快慢指针+反转法：O(1)

* 最优解：快慢指针+反转后半部分链表，时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 快慢指针+反转法是首选，空间复杂度O(1)，效率最高
2. 数组法实现简单，但需要额外的O(n)空间
3. 递归法代码优雅，但对于长链表可能导致栈溢出
4. 栈实现也是一种常用方法，但同样需要额外空间
5. 在实际应用中，应该考虑是否需要保留原始链表结构

* 与机器学习等领域的联系：
1. 回文检测在文本处理中很常见
2. 快慢指针技巧在其他算法中有广泛应用
3. 链表操作是数据结构的基础
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
回文链表问题是一个经典的链表操作问题，主要考察对链表特性的理解和算法设计能力。快慢指针+反转后半部分链表是解决这个问题的最优方法，其核心思想是找到链表中点，然后通过反转后半部分来实现空间复杂度O(1)的回文检测。

具体来说，这个算法分为以下几个步骤：
1. 使用快慢指针找到链表的中点。快指针每次移动两步，慢指针每次移动一步。当快指针到达链表末尾时，慢指针恰好位于链表的中点或中点前一个位置。

2. 反转后半部分链表。这样，我们就可以从链表的开头和反转后的后半部分链表的开头同时遍历，比较对应的节点值。

3. 比较前半部分和反转后的后半部分。如果所有对应的节点值都相等，则链表是回文链表。

4. 恢复链表（可选）。在实际应用中，为了不改变原始数据结构，我们通常需要将反转的后半部分链表再次反转，恢复原始链表结构。

这种方法的巧妙之处在于，通过反转后半部分链表，我们可以在不使用额外空间的情况下实现回文检测。这对于处理大型链表或内存受限的场景尤为重要。

在实际应用中，回文检测问题在很多场景中都有出现，如文本处理、字符串匹配、数据校验等。理解并掌握这类问题的解法有助于处理更复杂的算法问题。

此外，这个问题还体现了一个重要的工程化原则：在不影响正确性的前提下，我们应该尽可能地优化算法的空间复杂度，特别是对于可能处理大规模数据的算法。同时，如果我们修改了输入数据，我们通常应该在操作完成后将其恢复到原始状态，以避免影响其他可能使用同一数据的代码。
"""