# K 个一组翻转链表 - LeetCode 25
# 测试链接: https://leetcode.cn/problems/reverse-nodes-in-k-group/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法 - 模拟翻转过程
    def reverseKGroup(self, head: ListNode, k: int) -> ListNode:
        """
        K个一组翻转链表
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建哑节点
        dummy = ListNode(0)
        dummy.next = head
        
        # 前驱节点，初始指向哑节点
        prev = dummy
        
        while True:
            # 找到第k个节点
            tail = prev
            for i in range(k):
                tail = tail.next
                # 如果剩余节点不足k个，直接返回
                if not tail:
                    return dummy.next
            
            # 保存下一组的头节点
            next_group = tail.next
            
            # 翻转当前k个节点，并获取新的头节点
            new_head = self.reverseList(prev.next, tail)
            
            # 连接翻转后的链表
            old_head = prev.next  # 原头节点变成尾节点
            prev.next = new_head  # 前驱指向新的头节点
            old_head.next = next_group  # 原头节点（现在是尾节点）指向下一组
            
            # 更新前驱节点，为下一组做准备
            prev = old_head
    
    # 方法2: 递归法
    def reverseKGroupRecursive(self, head: ListNode, k: int) -> ListNode:
        """
        递归实现K个一组翻转链表
        时间复杂度: O(n)
        空间复杂度: O(n/k)，递归调用栈的深度
        """
        # 检查剩余节点是否足够k个
        count = 0
        current = head
        while current and count < k:
            current = current.next
            count += 1
        
        # 如果剩余节点不足k个，直接返回
        if count < k:
            return head
        
        # 递归翻转后续链表
        next_group = self.reverseKGroupRecursive(current, k)
        
        # 翻转当前k个节点
        new_head = self.reverseFirstK(head, k)
        
        # 连接翻转后的链表和后续链表
        head.next = next_group
        
        return new_head
    
    # 方法3: 使用栈辅助翻转
    def reverseKGroupWithStack(self, head: ListNode, k: int) -> ListNode:
        """
        使用栈辅助实现K个一组翻转链表
        时间复杂度: O(n)
        空间复杂度: O(k)
        """
        if k <= 1 or not head:
            return head
        
        dummy = ListNode(0)
        current = dummy
        stack = []
        
        while True:
            # 入栈k个节点
            count = 0
            temp = head
            while temp and count < k:
                stack.append(temp)
                temp = temp.next
                count += 1
            
            # 如果不足k个节点，直接连接剩余部分并返回
            if count < k:
                current.next = head
                break
            
            # 出栈并连接节点（实现翻转）
            while stack:
                current.next = stack.pop()
                current = current.next
            
            # 连接下一组的头节点
            current.next = temp
            head = temp
        
        return dummy.next
    
    # 方法4: 记录关键位置后翻转
    def reverseKGroupRecord(self, head: ListNode, k: int) -> ListNode:
        """
        记录关键位置后进行翻转
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 计算链表长度
        length = 0
        current = head
        while current:
            length += 1
            current = current.next
        
        # 计算需要翻转的组数
        groups = length // k
        
        dummy = ListNode(0)
        dummy.next = head
        prev = dummy
        
        # 对每组进行翻转
        for _ in range(groups):
            # 当前组的第一个节点
            group_start = prev.next
            # 记录当前节点用于循环
            curr = group_start
            
            # 翻转当前组的k个节点
            for _ in range(k - 1):
                next_temp = curr.next
                curr.next = next_temp.next
                next_temp.next = prev.next
                prev.next = next_temp
            
            # 更新prev为下一组的前一个节点
            prev = group_start
        
        return dummy.next
    
    # 辅助方法：翻转从head到tail的链表，并返回新的头节点
    def reverseList(self, head: ListNode, tail: ListNode) -> ListNode:
        prev = None
        current = head
        while prev != tail:
            next_temp = current.next
            current.next = prev
            prev = current
            current = next_temp
        return prev  # 返回新的头节点（原tail）
    
    # 辅助方法：翻转链表的前k个节点
    def reverseFirstK(self, head: ListNode, k: int) -> ListNode:
        prev = None
        current = head
        while k > 0:
            next_temp = current.next
            current.next = prev
            prev = current
            current = next_temp
            k -= 1
        return prev

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
    
    # 测试用例1: [1,2,3,4,5], k=2
    nums1 = [1, 2, 3, 4, 5]
    head1 = build_list(nums1)
    print(f"测试用例1:\n原始链表: {nums1}, k=2")
    
    # 测试迭代法
    result1 = solution.reverseKGroup(head1, 2)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: [1,2,3,4,5], k=3
    nums2 = [1, 2, 3, 4, 5]
    head2 = build_list(nums2)
    print(f"\n测试用例2:\n原始链表: {nums2}, k=3")
    
    result2 = solution.reverseKGroup(head2, 3)
    print(f"迭代法结果: {list_to_array(result2)}")
    
    # 测试用例3: [1,2,3,4,5,6,7,8], k=3
    nums3 = [1, 2, 3, 4, 5, 6, 7, 8]
    head3 = build_list(nums3)
    print(f"\n测试用例3:\n原始链表: {nums3}, k=3")
    
    # 测试递归法
    result3_recursive = solution.reverseKGroupRecursive(head3, 3)
    print(f"递归法结果: {list_to_array(result3_recursive)}")
    
    # 测试用例4: [1], k=1
    nums4 = [1]
    head4 = build_list(nums4)
    print(f"\n测试用例4:\n原始链表: {nums4}, k=1")
    
    result4 = solution.reverseKGroup(head4, 1)
    print(f"迭代法结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,2,3,4,5,6], k=4
    nums5 = [1, 2, 3, 4, 5, 6]
    head5 = build_list(nums5)
    print(f"\n测试用例5:\n原始链表: {nums5}, k=4")
    
    # 测试栈辅助方法
    result5_stack = solution.reverseKGroupWithStack(head5, 4)
    print(f"栈辅助方法结果: {list_to_array(result5_stack)}")
    
    # 测试用例6: [1,2,3,4,5,6,7], k=2
    nums6 = [1, 2, 3, 4, 5, 6, 7]
    head6 = build_list(nums6)
    print(f"\n测试用例6:\n原始链表: {nums6}, k=2")
    
    # 测试记录关键位置方法
    result6_record = solution.reverseKGroupRecord(head6, 2)
    print(f"记录关键位置方法结果: {list_to_array(result6_record)}")

"""
* 题目扩展：LeetCode 25. K 个一组翻转链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。

* 解题思路：
1. 迭代法：
   - 找到每组的头和尾
   - 翻转该组节点
   - 重新连接到原链表
2. 递归法：
   - 先递归处理后面的k组
   - 再翻转当前k个节点
   - 连接翻转后的当前组和递归处理后的后续部分
3. 栈辅助法：
   - 使用栈保存k个节点
   - 出栈时实现翻转
4. 记录关键位置法：
   - 计算链表长度
   - 对每组进行翻转，记录关键节点位置

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表长度

* 空间复杂度：
- 迭代法、记录关键位置法：O(1)
- 递归法：O(n/k)，递归调用栈的深度
- 栈辅助法：O(k)，栈的空间

* 最优解：迭代法，空间复杂度O(1)，实现清晰

* 工程化考量：
1. 使用哑节点简化头节点处理
2. 边界条件处理：空链表、k=1、剩余节点不足k个等情况
3. 翻转操作需要仔细处理指针，避免链表断裂
4. 递归方法对于长链表可能存在栈溢出风险
5. 栈辅助方法需要额外空间，但实现相对简单

* 与机器学习等领域的联系：
1. 链表操作是数据结构基础，在各种算法中广泛应用
2. 分组处理的思想在数据批处理中很常见
3. 递归和迭代的转换在算法设计中是重要概念
4. 栈在编译器实现、表达式求值等场景中有重要应用

* 语言特性差异：
Python: 无需手动管理内存，代码简洁易读
Java: 有自动内存管理，语法相对严格
C++: 需要注意指针操作和内存管理

* 算法深度分析：
K个一组翻转链表是一个较为复杂的链表操作问题，综合考察了链表的遍历、翻转和重连操作。该问题的核心在于如何将链表分成多个k长度的组，对每组进行翻转，然后重新连接。迭代方法通过维护前驱节点、当前组的头尾节点等关键位置，实现了O(1)空间复杂度的解法。递归方法则利用了函数调用栈保存中间状态，代码更加简洁，但空间复杂度略高。栈辅助方法则直观地利用栈的LIFO特性实现了翻转。在实际应用中，根据具体需求和链表长度，可以选择不同的实现方式。对于特别长的链表，迭代方法可能更安全，避免栈溢出的风险。
"""