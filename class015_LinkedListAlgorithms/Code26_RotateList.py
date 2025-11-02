# 旋转链表 - LeetCode 61
# 测试链接: https://leetcode.cn/problems/rotate-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 闭合链表成环，找到新的头尾节点
    def rotateRight(self, head: ListNode, k: int) -> ListNode:
        """
        旋转链表
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 边界条件处理
        if not head or not head.next or k == 0:
            return head
        
        # 计算链表长度
        length = 1
        current = head
        while current.next:
            current = current.next
            length += 1
        
        # 处理k大于链表长度的情况
        k = k % length
        if k == 0:
            return head  # 不需要旋转
        
        # 找到倒数第k+1个节点（新的尾节点）
        new_tail = head
        for _ in range(length - k - 1):
            new_tail = new_tail.next
        
        # 新的头节点是倒数第k个节点
        new_head = new_tail.next
        
        # 断开链表，形成新的链表
        new_tail.next = None
        
        # 将原链表的尾节点连接到原头节点
        current.next = head  # current此时是原尾节点
        
        return new_head
    
    # 方法2: 找到倒数第k个节点，重连链表
    def rotateRightFindKth(self, head: ListNode, k: int) -> ListNode:
        """
        通过找到倒数第k个节点来旋转链表
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 边界条件处理
        if not head or not head.next or k == 0:
            return head
        
        # 计算链表长度
        length = 1
        tail = head
        while tail.next:
            tail = tail.next
            length += 1
        
        # 处理k大于链表长度的情况
        k = k % length
        if k == 0:
            return head
        
        # 使用双指针找到倒数第k+1个节点
        fast = head
        slow = head
        
        # 快指针先走k步
        for _ in range(k):
            fast = fast.next
        
        # 同时移动快慢指针，直到快指针到达末尾
        while fast.next:
            fast = fast.next
            slow = slow.next
        
        # 此时slow指向倒数第k+1个节点
        new_head = slow.next
        slow.next = None  # 断开链表
        tail.next = head  # 连接原尾和原头
        
        return new_head
    
    # 方法3: 递归方法（不推荐，因为可能栈溢出）
    def rotateRightRecursive(self, head: ListNode, k: int) -> ListNode:
        """
        递归实现旋转链表
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 边界条件处理
        if not head or not head.next or k == 0:
            return head
        
        # 计算链表长度
        length = 1
        current = head
        while current.next:
            current = current.next
            length += 1
        
        # 处理k大于链表长度的情况
        k = k % length
        if k == 0:
            return head
        
        # 递归旋转k次
        return self.rotateOnce(head, k)
    
    # 辅助方法: 旋转一次链表（将最后一个节点移到前面）
    def rotateOnce(self, head: ListNode, remaining: int) -> ListNode:
        if remaining == 0:
            return head
        
        # 找到倒数第二个节点
        prev = head
        while prev.next.next:
            prev = prev.next
        
        # 获取尾节点
        tail = prev.next
        
        # 断开连接并重新连接
        prev.next = None
        tail.next = head
        
        # 递归旋转剩余次数
        return self.rotateOnce(tail, remaining - 1)
    
    # 方法4: 记录所有节点到列表，然后重新排列
    def rotateRightWithList(self, head: ListNode, k: int) -> ListNode:
        """
        使用列表记录所有节点，然后重新排列
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 边界条件处理
        if not head or not head.next or k == 0:
            return head
        
        # 收集所有节点到列表
        nodes = []
        current = head
        while current:
            nodes.append(current)
            current = current.next
        
        n = len(nodes)
        k = k % n
        if k == 0:
            return head
        
        # 重新连接节点
        nodes[-1].next = nodes[0]  # 形成环
        nodes[n - k - 1].next = None  # 断开环
        
        return nodes[n - k]

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
    
    # 测试方法1
    result1 = solution.rotateRight(head1, 2)
    print(f"闭合成环方法结果: {list_to_array(result1)}")
    
    # 测试用例2: [0,1,2], k=4
    nums2 = [0, 1, 2]
    head2 = build_list(nums2)
    print(f"\n测试用例2:\n原始链表: {nums2}, k=4")
    
    # 测试方法2
    result2 = solution.rotateRightFindKth(head2, 4)
    print(f"双指针找倒数第k个节点方法结果: {list_to_array(result2)}")
    
    # 测试用例3: [1], k=0
    nums3 = [1]
    head3 = build_list(nums3)
    print(f"\n测试用例3:\n原始链表: {nums3}, k=0")
    
    result3 = solution.rotateRight(head3, 0)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1,2], k=1
    nums4 = [1, 2]
    head4 = build_list(nums4)
    print(f"\n测试用例4:\n原始链表: {nums4}, k=1")
    
    # 测试方法3（递归）
    result4_recursive = solution.rotateRightRecursive(head4, 1)
    print(f"递归方法结果: {list_to_array(result4_recursive)}")
    
    # 测试用例5: [1,2,3,4,5,6,7], k=3
    nums5 = [1, 2, 3, 4, 5, 6, 7]
    head5 = build_list(nums5)
    print(f"\n测试用例5:\n原始链表: {nums5}, k=3")
    
    # 测试方法4（使用列表）
    result5_list = solution.rotateRightWithList(head5, 3)
    print(f"使用列表方法结果: {list_to_array(result5_list)}")
    
    # 测试用例6: [1,2,3], k=3
    nums6 = [1, 2, 3]
    head6 = build_list(nums6)
    print(f"\n测试用例6:\n原始链表: {nums6}, k=3")
    
    result6 = solution.rotateRight(head6, 3)
    print(f"结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 61. 旋转链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。

* 解题思路：
1. 闭合链表成环：
   - 计算链表长度
   - 将链表首尾相连形成环
   - 找到新的头节点和尾节点
   - 断开环形成新的链表
2. 找到倒数第k个节点：
   - 使用双指针找到倒数第k个节点作为新的头节点
   - 断开原链表，重连形成新链表
3. 递归方法：
   - 递归地将链表旋转k次
   - 每次旋转将最后一个节点移到前面
4. 使用列表记录节点：
   - 将所有节点存入列表
   - 重新排列节点位置
   - 重建链表

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表长度

* 空间复杂度：
- 闭合链表成环法、找到倒数第k个节点法：O(1)
- 递归方法：O(n)，递归调用栈的深度
- 使用列表记录节点法：O(n)，额外的列表空间

* 最优解：闭合链表成环法或找到倒数第k个节点法，空间复杂度O(1)

* 工程化考量：
1. 处理边界条件：空链表、单节点链表、k=0、k大于链表长度等
2. 计算k的有效旋转次数（k % length）
3. 注意链表指针操作，避免链表断裂或形成环
4. 递归方法对于长链表可能存在栈溢出风险

* 与机器学习等领域的联系：
1. 链表旋转操作在数据预处理中有应用
2. 双指针技巧在滑动窗口等算法中广泛使用
3. 周期性数据处理（如时间序列）可能涉及旋转操作
4. 列表重排思想在数据转换中有应用

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，语法相对严格
C++: 需要注意指针操作和内存管理

* 算法深度分析：
旋转链表问题的核心在于找到旋转后的新头节点和新尾节点。闭合链表成环的方法是一种优雅的解法，通过将链表首尾相连形成环，然后找到合适的位置断开环。双指针方法则利用快慢指针的特性找到倒数第k个节点。两种方法的时间复杂度都是O(n)，空间复杂度都是O(1)，但闭合成环的方法在实现上可能更直观。递归方法虽然代码结构清晰，但对于长链表可能会导致栈溢出，实际应用中较少使用。使用列表记录节点的方法虽然空间复杂度较高，但在某些情况下实现起来可能更简单。

从更深入的角度看，旋转链表问题可以视为一种特定的链表重排操作，其核心思想是将链表分为两部分，然后交换这两部分的位置。这种思想在其他链表操作问题中也有应用。理解链表的旋转操作有助于掌握更复杂的链表操作技巧。
"""