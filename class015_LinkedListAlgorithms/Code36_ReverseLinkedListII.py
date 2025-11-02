# 反转链表II - LeetCode 92
# 测试链接: https://leetcode.cn/problems/reverse-linked-list-ii/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法
    def reverseBetween(self, head: ListNode, left: int, right: int) -> ListNode:
        """
        迭代法反转链表指定区间
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况：如果left等于right，不需要反转
        if left == right:
            return head
        
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 找到left位置的前一个节点
        prev = dummy
        for _ in range(left - 1):
            prev = prev.next
        
        # current指向left位置的节点
        current = prev.next
        
        # 从left位置开始，反转right-left次
        for _ in range(right - left):
            # 保存下一个节点
            next_temp = current.next
            # 反转指针
            current.next = next_temp.next
            next_temp.next = prev.next
            prev.next = next_temp
        
        return dummy.next
    
    # 方法2: 递归法
    def reverseBetweenRecursive(self, head: ListNode, left: int, right: int) -> ListNode:
        """
        递归法反转链表指定区间
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：如果到达链表末尾或right位置，返回当前头节点
        if right == 1:
            return head
        
        # 基本情况：当left为1时，从当前位置开始反转
        if left == 1:
            # 使用头插法递归反转
            return self._reverseN(head, right)
        
        # 递归处理剩余部分
        head.next = self.reverseBetweenRecursive(head.next, left - 1, right - 1)
        return head
    
    # 辅助函数：反转链表的前n个节点
    def _reverseN(self, head: ListNode, n: int) -> ListNode:
        # 基本情况：如果到达链表末尾或第n个节点，返回当前头节点
        if n == 1:
            return head
        
        # 递归反转后n-1个节点
        new_head = self._reverseN(head.next, n - 1)
        
        # 保存反转部分的下一个节点
        successor = head.next.next
        # 反转当前节点
        head.next.next = head
        head.next = successor
        
        return new_head
    
    # 方法3: 分段法 - 将链表分为三部分处理
    def reverseBetweenSegment(self, head: ListNode, left: int, right: int) -> ListNode:
        """
        分段法：将链表分为三段，反转中间段，然后重新连接
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建哑节点
        dummy = ListNode(0)
        dummy.next = head
        
        # 找到反转区间的前一个节点 (part1_end)
        part1_end = dummy
        for _ in range(left - 1):
            part1_end = part1_end.next
        
        # 找到反转区间的开始节点
        reverse_start = part1_end.next
        
        # 找到反转区间的结束节点
        reverse_end = reverse_start
        for _ in range(right - left):
            reverse_end = reverse_end.next
        
        # 保存第三部分的开始节点
        part3_start = reverse_end.next
        
        # 断开链表，便于反转中间部分
        part1_end.next = None
        reverse_end.next = None
        
        # 反转中间部分
        self._reverseList(reverse_start)
        
        # 重新连接三部分
        part1_end.next = reverse_end
        reverse_start.next = part3_start
        
        return dummy.next
    
    # 辅助函数：反转整个链表
    def _reverseList(self, head: ListNode) -> ListNode:
        prev = None
        curr = head
        while curr:
            next_temp = curr.next
            curr.next = prev
            prev = curr
            curr = next_temp
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
    
    # 测试用例1: [1,2,3,4,5], left=2, right=4
    head1 = build_list([1, 2, 3, 4, 5])
    print("测试用例1: [1,2,3,4,5], left=2, right=4")
    
    # 测试迭代法
    result1 = solution.reverseBetween(head1, 2, 4)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: [5], left=1, right=1
    head2 = build_list([5])
    print("\n测试用例2: [5], left=1, right=1")
    
    result2 = solution.reverseBetween(head2, 1, 1)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: [3,5], left=1, right=2
    head3 = build_list([3, 5])
    print("\n测试用例3: [3,5], left=1, right=2")
    
    # 测试递归法
    result3 = solution.reverseBetweenRecursive(head3, 1, 2)
    print(f"递归法结果: {list_to_array(result3)}")
    
    # 测试用例4: [1,2,3,4,5], left=1, right=5
    head4 = build_list([1, 2, 3, 4, 5])
    print("\n测试用例4: [1,2,3,4,5], left=1, right=5")
    
    # 测试分段法
    result4 = solution.reverseBetweenSegment(head4, 1, 5)
    print(f"分段法结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,2,3,4,5,6], left=3, right=5
    head5 = build_list([1, 2, 3, 4, 5, 6])
    print("\n测试用例5: [1,2,3,4,5,6], left=3, right=5")
    
    result5 = solution.reverseBetween(head5, 3, 5)
    print(f"迭代法结果: {list_to_array(result5)}")
    
    # 测试用例6: [-10,-5,0,5,10,15], left=2, right=5
    head6 = build_list([-10, -5, 0, 5, 10, 15])
    print("\n测试用例6: [-10,-5,0,5,10,15], left=2, right=5")
    
    result6 = solution.reverseBetweenRecursive(head6, 2, 5)
    print(f"递归法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 92. 反转链表II
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。

* 解题思路：
1. 迭代法（头插法的变种）：
   - 找到left位置的前一个节点
   - 从left位置开始，逐个将节点插入到反转区间的头部
   - 重复right-left次，完成区间反转
2. 递归法：
   - 当left为1时，从当前位置开始反转前right个节点
   - 否则，递归处理剩余部分
   - 使用辅助函数_reverseN处理前n个节点的反转
3. 分段法：
   - 将链表分为三段：反转区间前的部分、反转区间、反转区间后的部分
   - 断开链表，反转中间部分
   - 重新连接三部分

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 迭代法和分段法：O(1)
- 递归法：O(n)，递归调用栈的深度

* 最优解：迭代法，时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 迭代法是首选，空间复杂度O(1)，实现效率高
2. 递归法代码简洁，但对于长链表可能导致栈溢出
3. 分段法逻辑清晰，但需要多次遍历
4. 所有方法都需要正确处理边界情况，尤其是left=1和right=n的情况

* 与机器学习等领域的联系：
1. 链表操作是数据结构的基础
2. 局部反转操作在字符串处理、数组处理等场景中很常见
3. 递归思想在算法设计中很重要
4. 分段处理是解决复杂问题的常用策略

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
反转链表II是反转链表的一个变种问题，要求只反转链表中的一部分节点。这个问题比单纯的反转整个链表更加复杂，需要精确控制反转的起始和结束位置。

迭代法是解决这个问题的最优方法，其核心思想是使用头插法的变种。具体来说，我们找到反转区间的前一个节点，然后从反转区间的第一个节点开始，逐个将后续节点插入到反转区间的头部。这种方法的优点是只需要一次遍历就能完成反转，空间复杂度为O(1)。

递归法的思路也很巧妙。当left为1时，我们只需要反转链表的前right个节点。当left大于1时，我们递归处理head.next，并将left和right都减1。这样递归下去，最终会将问题转化为反转链表的前n个节点的问题。

分段法的思路最为清晰，将链表分为三段，分别处理后再重新连接。这种方法的优点是逻辑清晰，容易理解，但需要多次遍历链表。

在实现过程中，需要特别注意以下几点：
1. 使用哑节点简化对头节点的处理
2. 正确保存和恢复各个关键节点之间的连接
3. 处理边界情况，如left=1、right=n或left=right等

链表局部反转操作在实际应用中有很多场景，例如：
1. 在链表操作中，经常需要对链表的某一部分进行特殊处理
2. 在算法题中，链表局部反转是很多复杂链表操作的基础
3. 在数据处理中，需要对数据序列的某一部分进行反转

理解并掌握链表局部反转的实现有助于处理更复杂的链表问题，也为学习其他数据结构和算法打下基础。
"""