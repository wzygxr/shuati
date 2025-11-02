# -*- coding: utf-8 -*-
"""
链表反转相关算法题目集合
包含LeetCode、牛客网、Codeforces、LintCode、HackerRank等平台的相关题目
每个题目都提供详细的解题思路、复杂度分析和多种解法

题目列表：
1. 反转链表 (LeetCode 206, 牛客网, HackerRank, LintCode 35, 剑指Offer 24)
2. 反转链表 II (LeetCode 92)
3. K个一组翻转链表 (LeetCode 25)
4. 回文链表 (LeetCode 234, LintCode 223, 牛客网 NC78)
5. 旋转链表 (LeetCode 61, LintCode 170)
6. 合并两个有序链表 (LeetCode 21, LintCode 165, 剑指Offer 25, 牛客网 NC33)
7. 两两交换链表中的节点 (LeetCode 24, LintCode 451)
8. 重排链表 (LeetCode 143, LintCode 99)
9. 删除链表的倒数第N个节点 (LeetCode 19, LintCode 174, 牛客网 NC53, 剑指Offer 22)
10. 奇偶链表 (LeetCode 328, LintCode 1292, 牛客网 NC142)
11. 分隔链表 (LeetCode 86, LintCode 96, 牛客网 NC188)
12. 链表求和 (LeetCode 2, LeetCode 445, LintCode 167, 牛客网 NC40)
13. 环形链表 (LeetCode 141, LeetCode 142, LintCode 102, 牛客网 NC4, 剑指Offer 23)
14. 相交链表 (LeetCode 160, LintCode 380, 牛客网 NC66, 剑指Offer 52)
15. 排序链表 (LeetCode 148, LintCode 98)
16. 链表随机节点 (LeetCode 382)
17. 复制带随机指针的链表 (LeetCode 138, 剑指Offer 35)
18. 链表组件 (LeetCode 817)
19. 链表中的下一个更大节点 (LeetCode 1019)
20. 链表最大孪生和 (LeetCode 2130)
"""

from typing import Optional

class ListNode:
    """单链表节点"""
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next
    
    def __str__(self):
        """字符串表示链表"""
        result = []
        current = self
        while current:
            result.append(str(current.val))
            current = current.next
        return " -> ".join(result)
    
    @staticmethod
    def create_list(vals):
        """创建链表的辅助方法"""
        if not vals:
            return None
        
        head = ListNode(vals[0])
        current = head
        for i in range(1, len(vals)):
            current.next = ListNode(vals[i])
            current = current.next
        return head


class DoubleListNode:
    """双链表节点"""
    def __init__(self, val=0):
        self.val = val
        self.prev: Optional['DoubleListNode'] = None
        self.next: Optional['DoubleListNode'] = None


def reverse_list_iterative(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    方法1: 迭代法反转链表
    时间复杂度: O(n) - 需要遍历链表一次
    空间复杂度: O(1) - 只使用了常数级别的额外空间
    
    解题思路:
    使用三个指针(pre, current, next)来逐个反转链表中的节点指向关系
    1. pre指向已反转部分的最后一个节点
    2. current指向当前待处理节点
    3. next保存current的下一个节点，防止断链
    
    执行过程:
    原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
    步骤1: null <- 1    2 -> 3 -> 4 -> 5 -> null
    步骤2: null <- 1 <- 2    3 -> 4 -> 5 -> null
    步骤3: null <- 1 <- 2 <- 3    4 -> 5 -> null
    ...
    最终: null <- 1 <- 2 <- 3 <- 4 <- 5
    """
    pre: Optional[ListNode] = None      # 已反转部分的头节点
    current: Optional[ListNode] = head  # 当前待处理节点
    next_node: Optional[ListNode] = None  # 保存current的下一个节点
    
    while current:
        next_node = current.next  # 保存下一个节点
        current.next = pre        # 反转当前节点的指向
        pre = current             # 移动pre指针
        current = next_node       # 移动current指针
    
    return pre  # pre指向原链表的最后一个节点，即新链表的头节点


def reverse_list_recursive(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    方法2: 递归法反转链表
    时间复杂度: O(n) - 递归调用n次
    空间复杂度: O(n) - 递归调用栈的深度为n
    
    解题思路:
    1. 递归到链表末尾
    2. 在回溯过程中逐个反转节点的指向
    3. 假设除了当前节点外，后续链表已经完成反转
    
    执行过程:
    原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
    递归到5，返回5
    回溯到4: 4.next.next = 4 (即5->4)，4.next = null
    回溯到3: 3.next.next = 3 (即4->3)，3.next = null
    ...
    """
    # 递归终止条件：空节点或只有一个节点
    if not head or not head.next:
        return head
    
    # 递归处理后续节点，获取反转后链表的头节点
    new_head = reverse_list_recursive(head.next)
    
    # 反转当前节点和下一个节点的连接关系
    head.next.next = head  # 让下一个节点指向当前节点
    head.next = None       # 断开当前节点的next指针
    
    return new_head  # 返回反转后链表的头节点


def reverse_between(head: Optional[ListNode], left: int, right: int) -> Optional[ListNode]:
    """
    反转链表指定区间
    时间复杂度: O(n) - 最多遍历一次链表
    空间复杂度: O(1) - 只使用常数级别的额外空间
    
    解题思路:
    1. 找到需要反转区间的前一个节点(pre)
    2. 找到需要反转区间的第一个节点(start)
    3. 使用头插法将区间内的节点逐个插入到pre节点之后
    4. 连接反转后的链表与其他部分
    
    执行过程:
    原链表: 1 -> 2 -> 3 -> 4 -> 5, left=2, right=4
    步骤1: 找到pre(节点1)和start(节点2)
    步骤2: 将节点3插入到pre之后: 1 -> 3 -> 2 -> 4 -> 5
    步骤3: 将节点4插入到pre之后: 1 -> 4 -> 3 -> 2 -> 5
    结果: 1 -> 4 -> 3 -> 2 -> 5
    """
    if head is None:
        return None
    
    # 创建虚拟头节点，简化边界处理
    dummy = ListNode(0)
    dummy.next = head
    
    # 找到反转区间的前一个节点
    pre = dummy
    for i in range(left - 1):
        if pre.next is None:
            break
        pre = pre.next
    
    # start指向反转区间的第一个节点
    if pre.next is None:
        return dummy.next
    start = pre.next
    
    # then指向待处理节点
    if start.next is None:
        return dummy.next
    then = start.next
    
    # 头插法实现区间反转
    for i in range(right - left):
        if then is None:
            break
        start.next = then.next
        then.next = pre.next
        pre.next = then
        then = start.next
    
    return dummy.next


def reverse_k_group(head: Optional[ListNode], k: int) -> Optional[ListNode]:
    """
    K个一组反转链表
    时间复杂度: O(n) - 每个节点最多被访问两次
    空间复杂度: O(1) - 只使用常数级别的额外空间
    
    解题思路:
    1. 分组处理，每次处理k个节点
    2. 对每组节点进行反转
    3. 连接各组之间的关系
    4. 处理不足k个的剩余节点（保持原顺序）
    
    执行过程:
    原链表: 1 -> 2 -> 3 -> 4 -> 5, k=3
    第一组(1,2,3)反转: 3 -> 2 -> 1
    第二组(4,5)不足k个，保持原顺序: 4 -> 5
    结果: 3 -> 2 -> 1 -> 4 -> 5
    """
    if head is None or k <= 1:
        return head
    
    # 计算链表长度
    length = 0
    current = head
    while current:
        length += 1
        current = current.next
    
    # 创建虚拟头节点
    dummy = ListNode(0)
    dummy.next = head
    
    # pre指向已处理部分的最后一个节点
    pre = dummy
    
    # 分组处理
    while length >= k:
        # start指向当前组的第一个节点
        if pre.next is None:
            break
        start = pre.next
        
        # then指向待处理节点
        if start.next is None:
            break
        then = start.next
        
        # 对当前组进行k-1次头插操作
        for i in range(k - 1):
            if then is None:
                break
            start.next = then.next
            then.next = pre.next
            pre.next = then
            then = start.next
        
        # 更新pre指针和剩余长度
        pre = start
        length -= k
    
    return dummy.next


def reverse_double_list(head):
    pre = None
    next_node = None
    while head is not None:
        next_node = head.next      # 保存下一个节点
        head.next = pre           # 反转next指针
        head.prev = next_node     # 反转prev指针
        pre = head                # 移动pre指针
        head = next_node          # 移动head指针
    return pre  # 返回新的头节点

"""
补充题目4: 回文链表
题目来源：
1. LeetCode 234. 回文链表 - https://leetcode.cn/problems/palindrome-linked-list/
2. LintCode 223. 回文链表 - https://www.lintcode.com/problem/palindrome-linked-list/
3. 牛客网 NC78 链表中倒数最后k个结点（相关题目）

题目描述：判断一个链表是否是回文链表
输入：1->2->2->1
输出：True

最优解法：使用快慢指针找到中点，反转后半部分，然后比较
时间复杂度: O(n) - 只需要一次遍历找到中点，一次反转，一次比较
空间复杂度: O(1) - 只使用常数级别的额外空间
"""
def is_palindrome(head):
    if head is None or head.next is None:
        return True  # 空链表或单节点链表是回文的
    
    # 步骤1: 使用快慢指针找到链表的中点
    slow = head
    fast = head
    while fast is not None and fast.next is not None:
        slow = slow.next        # 慢指针每次走一步
        fast = fast.next.next   # 快指针每次走两步
    # 循环结束后，slow指向中点位置（如果节点数为奇数）或后半部分的第一个节点（如果节点数为偶数）
    
    # 步骤2: 反转后半部分链表
    second_half_head = reverse_list_iterative(slow)
    # 保存反转后的头节点，用于后续恢复
    second_half_start = second_half_head
    
    # 步骤3: 比较前半部分和反转后的后半部分
    first_half_head = head
    is_palindrome_flag = True
    while second_half_head is not None:
        if first_half_head.val != second_half_head.val:
            is_palindrome_flag = False
            break
        first_half_head = first_half_head.next
        second_half_head = second_half_head.next
    
    # 步骤4: 恢复链表（可选，但这是良好的工程实践）
    reverse_list_iterative(second_half_start)
    
    return is_palindrome_flag

"""
补充题目5: 旋转链表
题目来源：
1. LeetCode 61. 旋转链表 - https://leetcode.cn/problems/rotate-list/
2. LintCode 170. 旋转链表 - https://www.lintcode.com/problem/rotate-list/
3. 牛客网 NC53 删除链表的倒数第n个节点（相关题目）

题目描述：将链表向右旋转k个位置
输入：1->2->3->4->5->NULL, k = 2
输出：4->5->1->2->3->NULL

解题思路：
1. 先计算链表长度
2. 将链表首尾相连形成环
3. 在合适位置断开环
时间复杂度: O(n) - 需要遍历链表
空间复杂度: O(1) - 只使用常数级别的额外空间
"""
def rotate_right(head, k):
    # 处理特殊情况
    if head is None or head.next is None or k == 0:
        return head
    
    # 步骤1: 计算链表长度并找到尾节点
    length = 1
    tail = head
    while tail.next is not None:
        tail = tail.next
        length += 1
    
    # 步骤2: 计算实际需要旋转的次数（取模操作避免多余旋转）
    k = k % length
    if k == 0:
        return head  # 不需要旋转
    
    # 步骤3: 将链表首尾相连形成环
    tail.next = head
    
    # 步骤4: 找到新的尾节点位置，距离原头节点 (length - k) 个位置
    new_tail = head
    for i in range(length - k - 1):
        new_tail = new_tail.next
    
    # 步骤5: 新的头节点是新尾节点的下一个节点
    new_head = new_tail.next
    
    # 步骤6: 断开环
    new_tail.next = None
    
    return new_head

"""
补充题目6: 合并两个有序链表
题目来源：
1. LeetCode 21. 合并两个有序链表 - https://leetcode.cn/problems/merge-two-sorted-lists/
2. LintCode 165. 合并两个排序链表 - https://www.lintcode.com/problem/merge-two-sorted-lists/
3. 剑指Offer 25. 合并两个排序的链表
4. 牛客网 NC33 合并两个排序的链表

题目描述：将两个升序链表合并为一个新的升序链表
输入：l1 = [1,2,4], l2 = [1,3,4]
输出：[1,1,2,3,4,4]

解题思路：使用迭代或递归方法，逐个比较两个链表的节点值
时间复杂度: O(n+m) - n和m分别是两个链表的长度
空间复杂度: O(1) - 迭代版本，只使用常数级别的额外空间
"""
def merge_two_lists(l1, l2):
    # 创建虚拟头节点，简化边界情况处理
    dummy = ListNode(0)
    current = dummy
    
    # 迭代比较两个链表的节点值
    while l1 is not None and l2 is not None:
        if l1.val <= l2.val:
            current.next = l1
            l1 = l1.next
        else:
            current.next = l2
            l2 = l2.next
        current = current.next
    
    # 连接剩余部分
    current.next = l1 if l1 is not None else l2
    
    return dummy.next

"""
补充题目7: 两两交换链表中的节点
题目来源：
1. LeetCode 24. 两两交换链表中的节点 - https://leetcode.cn/problems/swap-nodes-in-pairs/
2. LintCode 451. 两两交换链表中的节点 - https://www.lintcode.com/problem/swap-nodes-in-pairs/
3. 牛客网 NC142 链表的奇偶重排（相关题目）

题目描述：两两交换链表中的相邻节点
输入：1->2->3->4
输出：2->1->4->3

解题思路：使用虚拟头节点和迭代方法
时间复杂度: O(n) - 需要遍历链表一次
空间复杂度: O(1) - 只使用常数级别的额外空间
"""
def swap_pairs(head):
    # 创建虚拟头节点
    dummy = ListNode(0)
    dummy.next = head
    prev = dummy
    
    # 当有至少两个节点可以交换时
    while prev.next is not None and prev.next.next is not None:
        # 获取需要交换的两个节点
        first = prev.next
        second = prev.next.next
        
        # 执行交换操作
        first.next = second.next  # 1 -> 3
        second.next = first       # 2 -> 1
        prev.next = second        # dummy -> 2
        
        # 移动prev指针到下一对的前一个位置
        prev = first
    
    return dummy.next

"""
补充题目8: 重排链表
题目来源：
1. LeetCode 143. 重排链表 - https://leetcode.cn/problems/reorder-list/
2. LintCode 99. 重排链表 - https://www.lintcode.com/problem/reorder-list/
3. 牛客网 NC40 链表相加（二）（相关题目）

题目描述：按照 L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → ... 重新排列链表
输入：1->2->3->4
输出：1->4->2->3

解题思路：
1. 使用快慢指针找到中点
2. 反转后半部分链表
3. 合并两个链表
时间复杂度: O(n) - 需要遍历链表三次
空间复杂度: O(1) - 只使用常数级别的额外空间
"""
def reorder_list(head):
    if head is None or head.next is None or head.next.next is None:
        return  # 无需重排
    
    # 步骤1: 使用快慢指针找到链表中点
    slow = head
    fast = head
    while fast.next is not None and fast.next.next is not None:
        slow = slow.next
        fast = fast.next.next
    
    # 步骤2: 反转后半部分链表
    second_half = reverse_list_iterative(slow.next)
    slow.next = None  # 断开前半部分和后半部分
    
    # 步骤3: 合并两个链表
    first_half = head
    while second_half is not None:
        temp1 = first_half.next
        temp2 = second_half.next
        
        first_half.next = second_half
        second_half.next = temp1
        
        first_half = temp1
        second_half = temp2

"""
补充题目9: 删除链表的倒数第N个节点
题目来源：
1. LeetCode 19. 删除链表的倒数第N个节点 - https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
2. LintCode 174. 删除链表中倒数第n个节点 - https://www.lintcode.com/problem/remove-nth-node-from-end-of-list/
3. 牛客网 NC53 删除链表的倒数第n个节点
4. 剑指Offer 22. 链表中倒数第k个节点（相关题目）

题目描述：删除链表的倒数第n个节点，返回链表的头节点
输入：head = [1,2,3,4,5], n = 2
输出：[1,2,3,5]

解题思路：使用快慢指针，快指针先走n步，然后快慢指针一起走
时间复杂度: O(n) - 只需要遍历链表一次
空间复杂度: O(1) - 只使用常数级别的额外空间
"""
def remove_nth_from_end(head, n):
    # 创建虚拟头节点，简化边界情况处理
    dummy = ListNode(0)
    dummy.next = head
    
    # 设置快慢指针
    fast = dummy
    slow = dummy
    
    # 快指针先走n+1步
    for i in range(n + 1):
        if fast is None and i < n + 1:
            return head  # n大于链表长度，无法删除
        fast = fast.next
    
    # 快慢指针一起走，直到快指针到达链表末尾
    while fast is not None:
        fast = fast.next
        slow = slow.next
    
    # 此时slow指向待删除节点的前一个节点
    slow.next = slow.next.next
    
    return dummy.next


def test_reverse_list():
    """测试基础链表反转"""
    print("=== 测试基础链表反转 ===")
    
    # 测试用例1: [1,2,3,4,5] -> [5,4,3,2,1]
    head1 = ListNode.create_list([1, 2, 3, 4, 5])
    print("原链表:", head1)
    reversed1 = reverse_list_iterative(head1)
    print("反转后:", reversed1)
    
    # 测试用例2: [1,2] -> [2,1]
    head2 = ListNode.create_list([1, 2])
    print("原链表:", head2)
    reversed2 = reverse_list_recursive(head2)
    print("反转后:", reversed2)
    
    # 测试用例3: [] -> []
    head3 = None
    print("原链表:", head3)
    reversed3 = reverse_list_iterative(head3)
    print("反转后:", reversed3)
    print()


def test_reverse_list_ii():
    """测试指定区间链表反转"""
    print("=== 测试指定区间链表反转 ===")
    
    # 测试用例1: [1,2,3,4,5], left=2, right=4 -> [1,4,3,2,5]
    head1 = ListNode.create_list([1, 2, 3, 4, 5])
    print("原链表:", head1)
    reversed1 = reverse_between(head1, 2, 4)
    print("反转位置2到4后:", reversed1)
    
    # 测试用例2: [5], left=1, right=1 -> [5]
    head2 = ListNode.create_list([5])
    print("原链表:", head2)
    reversed2 = reverse_between(head2, 1, 1)
    print("反转位置1到1后:", reversed2)
    print()


def test_reverse_k_group():
    """测试K个一组反转链表"""
    print("=== 测试K个一组反转链表 ===")
    
    # 测试用例1: [1,2,3,4,5], k=2 -> [2,1,4,3,5]
    head1 = ListNode.create_list([1, 2, 3, 4, 5])
    print("原链表:", head1)
    reversed1 = reverse_k_group(head1, 2)
    print("每2个一组反转后:", reversed1)
    
    # 测试用例2: [1,2,3,4,5], k=3 -> [3,2,1,4,5]
    head2 = ListNode.create_list([1, 2, 3, 4, 5])
    print("原链表:", head2)
    reversed2 = reverse_k_group(head2, 3)
    print("每3个一组反转后:", reversed2)
    print()


def run_unit_tests():
    """运行单元测试"""
    print("=== 链表反转单元测试 ===")
    
    # 测试用例1: 正常情况
    test1 = ListNode.create_list([1, 2, 3, 4, 5])
    result1 = reverse_list_iterative(test1)
    print("测试1 - 输入[1,2,3,4,5]，期望[5,4,3,2,1]，实际:", result1)
    
    # 测试用例2: 空链表
    result2 = reverse_list_iterative(None)
    print("测试2 - 输入[]，期望[]，实际:", result2)
    
    # 测试用例3: 单节点链表
    test3 = ListNode(1)
    result3 = reverse_list_iterative(test3)
    print("测试3 - 输入[1]，期望[1]，实际:", result3.val if result3 else "null")
    
    # 测试用例4: 两节点链表
    test4 = ListNode.create_list([1, 2])
    result4 = reverse_list_iterative(test4)
    print("测试4 - 输入[1,2]，期望[2,1]，实际:", result4)
    
    print("单元测试完成\n")

"""
补充题目10: 奇偶链表
题目来源:
1. LeetCode 328. 奇偶链表 - https://leetcode.cn/problems/odd-even-linked-list/
2. LintCode 1292. 奇偶链表 - https://www.lintcode.com/problem/odd-even-linked-list/
3. 牛客网 NC142 链表的奇偶重排

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def odd_even_list(head):
    if head is None or head.next is None:
        return head
    
    odd = head
    even = head.next
    even_head = even
    
    while even is not None and even.next is not None:
        odd.next = even.next
        odd = odd.next
        even.next = odd.next
        even = even.next
    
    odd.next = even_head
    return head


"""
补充题目11: 分隔链表
题目来源:
1. LeetCode 86. 分隔链表 - https://leetcode.cn/problems/partition-list/
2. LintCode 96. 分隔链表 - https://www.lintcode.com/problem/partition-list/
3. 牛客网 NC188 分隔链表

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def partition(head, x):
    before_head = ListNode(0)
    before = before_head
    after_head = ListNode(0)
    after = after_head
    
    while head is not None:
        if head.val < x:
            before.next = head
            before = before.next
        else:
            after.next = head
            after = after.next
        head = head.next
    
    after.next = None
    before.next = after_head.next
    
    return before_head.next


"""
补充题目12: 链表求和
题目来源:
1. LeetCode 2. 两数相加 - https://leetcode.cn/problems/add-two-numbers/
2. LeetCode 445. 两数相加 II - https://leetcode.cn/problems/add-two-numbers-ii/

时间复杂度: O(max(m,n))
空间复杂度: O(max(m,n))
是否为最优解: 是
"""
def add_two_numbers(l1, l2):
    dummy = ListNode(0)
    current = dummy
    carry = 0
    
    while l1 is not None or l2 is not None or carry != 0:
        val1 = l1.val if l1 is not None else 0
        val2 = l2.val if l2 is not None else 0
        
        sum_val = val1 + val2 + carry
        carry = sum_val // 10
        
        current.next = ListNode(sum_val % 10)
        current = current.next
        
        if l1 is not None:
            l1 = l1.next
        if l2 is not None:
            l2 = l2.next
    
    return dummy.next


"""
补充题目13: 环形链表
题目来源:
1. LeetCode 141. 环形链表 - https://leetcode.cn/problems/linked-list-cycle/
2. LeetCode 142. 环形链表 II - https://leetcode.cn/problems/linked-list-cycle-ii/

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def has_cycle(head):
    if head is None or head.next is None:
        return False
    
    slow = head
    fast = head
    
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        
        if slow == fast:
            return True
    
    return False


def detect_cycle(head):
    if head is None or head.next is None:
        return None
    
    slow = head
    fast = head
    has_cycle_flag = False
    
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
        
        if slow == fast:
            has_cycle_flag = True
            break
    
    if not has_cycle_flag:
        return None
    
    slow = head
    while slow != fast:
        slow = slow.next
        fast = fast.next
    
    return slow


"""
补充题目14: 相交链表
题目来源:
1. LeetCode 160. 相交链表 - https://leetcode.cn/problems/intersection-of-two-linked-lists/
2. LintCode 380. 相交链表 - https://www.lintcode.com/problem/intersection-of-two-linked-lists/

时间复杂度: O(m+n)
空间复杂度: O(1)
是否为最优解: 是
"""
def get_intersection_node(headA, headB):
    if headA is None or headB is None:
        return None
    
    pA = headA
    pB = headB
    
    while pA != pB:
        pA = headB if pA is None else pA.next
        pB = headA if pB is None else pB.next
    
    return pA


"""
补充题目15: 排序链表
题目来源:
1. LeetCode 148. 排序链表 - https://leetcode.cn/problems/sort-list/
2. LintCode 98. 排序链表 - https://www.lintcode.com/problem/sort-list/

时间复杂度: O(n log n)
空间复杂度: O(log n)
是否为最优解: 是
"""
def sort_list(head):
    if head is None or head.next is None:
        return head
    
    slow = head
    fast = head.next
    
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
    
    mid = slow.next
    slow.next = None
    
    left = sort_list(head)
    right = sort_list(mid)
    
    return merge_two_lists(left, right)


"""
补充题目16: 链表随机节点
题目来源:
1. LeetCode 382. 链表随机节点 - https://leetcode.cn/problems/linked-list-random-node/
2. 蓄水池抽样算法应用

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
import random

class RandomNodeSelector:
    def __init__(self, head):
        self.head = head
    
    def get_random(self):
        current = self.head
        result = 0
        count = 0
        
        while current is not None:
            count += 1
            # 以1/count的概率选择当前节点
            if random.randint(1, count) == 1:
                result = current.val
            current = current.next
        
        return result


"""
补充题目17: 复制带随机指针的链表
题目来源:
1. LeetCode 138. 复制带随机指针的链表 - https://leetcode.cn/problems/copy-list-with-random-pointer/
2. 剑指Offer 35. 复杂链表的复制

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
class NodeWithRandom:
    def __init__(self, x):
        self.val = x
        self.next = None
        self.random = None

def copy_random_list(head):
    if head is None:
        return None
    
    # 第一次遍历：在每个节点后面插入复制节点
    current = head
    while current is not None:
        copy_node = NodeWithRandom(current.val)
        copy_node.next = current.next
        current.next = copy_node
        current = copy_node.next
    
    # 第二次遍历：设置复制节点的随机指针
    current = head
    while current is not None:
        if current.random is not None:
            current.next.random = current.random.next
        current = current.next.next
    
    # 第三次遍历：分离原链表和复制链表
    current = head
    copy_head = head.next
    copy_current = copy_head
    
    while current is not None:
        current.next = current.next.next
        if copy_current.next is not None:
            copy_current.next = copy_current.next.next
        current = current.next
        copy_current = copy_current.next
    
    return copy_head


"""
补充题目18: 链表组件
题目来源:
1. LeetCode 817. 链表组件 - https://leetcode.cn/problems/linked-list-components/

时间复杂度: O(n + m)
空间复杂度: O(m)
是否为最优解: 是
"""
def num_components(head, nums):
    num_set = set(nums)
    
    components = 0
    in_component = False
    current = head
    
    while current is not None:
        if current.val in num_set:
            if not in_component:
                components += 1
                in_component = True
        else:
            in_component = False
        current = current.next
    
    return components


"""
补充题目19: 链表中的下一个更大节点
题目来源:
1. LeetCode 1019. 链表中的下一个更大节点 - https://leetcode.cn/problems/next-greater-node-in-linked-list/

时间复杂度: O(n)
空间复杂度: O(n)
是否为最优解: 是
"""
def next_larger_nodes(head):
    # 将链表转换为数组
    values = []
    current = head
    while current is not None:
        values.append(current.val)
        current = current.next
    
    n = len(values)
    result = [0] * n
    stack = []
    
    # 从右向左遍历，使用单调栈
    for i in range(n - 1, -1, -1):
        current_val = values[i]
        
        # 弹出栈顶比当前值小的元素
        while stack and stack[-1] <= current_val:
            stack.pop()
        
        # 如果栈不为空，栈顶就是下一个更大节点
        result[i] = stack[-1] if stack else 0
        
        # 将当前值压入栈
        stack.append(current_val)
    
    return result


"""
补充题目20: 链表最大孪生和
题目来源:
1. LeetCode 2130. 链表最大孪生和 - https://leetcode.cn/problems/maximum-twin-sum-of-a-linked-list/

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def pair_sum(head):
    # 使用快慢指针找到中点
    slow = head
    fast = head
    while fast is not None and fast.next is not None:
        slow = slow.next
        fast = fast.next.next
    
    # 反转后半部分链表
    second_half = reverse_list_iterative(slow)
    
    # 计算孪生和的最大值
    max_sum = 0
    first_half = head
    while second_half is not None:
        max_sum = max(max_sum, first_half.val + second_half.val)
        first_half = first_half.next
        second_half = second_half.next
    
    return max_sum


"""
补充题目21: 合并K个升序链表
题目来源:
1. LeetCode 23. 合并K个升序链表 - https://leetcode.cn/problems/merge-k-sorted-lists/
2. LintCode 104. 合并k个排序链表 - https://www.lintcode.com/problem/merge-k-sorted-lists/
3. 牛客网 NC127. 合并k个已排序的链表

时间复杂度: O(N log K)
空间复杂度: O(K)
是否为最优解: 是
"""
import heapq

def merge_k_lists(lists):
    # 使用最小堆
    heap = []
    
    # 将所有非空链表的头节点加入最小堆
    for i, lst in enumerate(lists):
        if lst:
            heapq.heappush(heap, (lst.val, i))
    
    # 创建虚拟头节点
    dummy = ListNode(0)
    current = dummy
    
    # 从堆中取出最小节点，加入结果链表
    while heap:
        val, index = heapq.heappop(heap)  # 取出最小节点
        current.next = lists[index]       # 加入结果链表
        current = current.next            # 移动指针
        lists[index] = lists[index].next  # 移动原链表指针
        
        # 将取出节点的下一个节点加入堆中(如果不为空)
        if lists[index]:
            heapq.heappush(heap, (lists[index].val, index))
    
    return dummy.next


"""
补充题目22: 删除链表中的节点
题目来源:
1. LeetCode 237. 删除链表中的节点 - https://leetcode.cn/problems/delete-node-in-a-linked-list/
2. LintCode 37. 删除链表中的节点 - https://www.lintcode.com/problem/delete-node-in-a-linked-list/
3. 牛客网 NC138. 删除链表的节点

时间复杂度: O(1)
空间复杂度: O(1)
是否为最优解: 是
"""
def delete_node(node):
    # 将下一个节点的值复制到当前节点
    node.val = node.next.val
    
    # 跳过下一个节点
    node.next = node.next.next


"""
补充题目23: 删除排序链表中的重复元素
题目来源:
1. LeetCode 83. 删除排序链表中的重复元素 - https://leetcode.cn/problems/remove-duplicates-from-sorted-list/
2. LintCode 112. 删除排序链表中的重复元素 - https://www.lintcode.com/problem/remove-duplicates-from-sorted-list/
3. 牛客网 NC141. 判断一个链表是否为回文结构

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def delete_duplicates(head):
    # 边界情况处理
    if head is None or head.next is None:
        return head
    
    current = head
    
    # 遍历链表
    while current.next is not None:
        # 如果当前节点值等于下一个节点值，跳过下一个节点
        if current.val == current.next.val:
            current.next = current.next.next
        else:
            # 只有当下一个节点不被删除时，才移动current指针
            current = current.next
    
    return head


"""
补充题目24: 删除排序链表中的重复元素 II
题目来源:
1. LeetCode 82. 删除排序链表中的重复元素 II - https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/
2. LintCode 113. 删除排序链表中的重复元素 II - https://www.lintcode.com/problem/remove-duplicates-from-sorted-list-ii/
3. 牛客网 NC140

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def delete_duplicates_ii(head):
    # 创建虚拟头节点，简化边界处理
    dummy = ListNode(0)
    dummy.next = head
    
    # prev指向已处理部分的最后一个节点
    prev = dummy
    # current指向当前待处理节点
    current = head
    
    while current is not None:
        # 检查是否有重复节点
        if current.next is not None and current.val == current.next.val:
            # 记录重复值
            duplicate_value = current.val
            
            # 跳过所有重复节点
            while current is not None and current.val == duplicate_value:
                current = current.next
            
            # 连接prev和current
            prev.next = current
        else:
            # 没有重复，正常移动指针
            prev = current
            current = current.next
    
    return dummy.next


"""
补充题目25: 移除链表元素
题目来源:
1. LeetCode 203. 移除链表元素 - https://leetcode.cn/problems/remove-linked-list-elements/
2. 牛客网相关题目

时间复杂度: O(n)
空间复杂度: O(1)
是否为最优解: 是
"""
def remove_elements(head, val):
    # 创建虚拟头节点，简化删除头节点的情况
    dummy = ListNode(0)
    dummy.next = head
    
    # prev指向已处理部分的最后一个节点
    prev = dummy
    # current指向当前待处理节点
    current = head
    
    while current is not None:
        if current.val == val:
            # 删除当前节点
            prev.next = current.next
        else:
            # 移动prev指针
            prev = current
        # 移动current指针
        current = current.next
    
    return dummy.next


"""
性能分析工具类
"""
import time

class LinkedListProfiler:
    def __init__(self):
        self.start_time = 0
        self.end_time = 0
    
    def start(self):
        self.start_time = time.time_ns()
    
    def end(self):
        self.end_time = time.time_ns()
        duration = self.end_time - self.start_time
        print(f"执行时间: {duration} 纳秒")


"""
调试工具类
"""
class LinkedListDebugger:
    @staticmethod
    def print_list_state(head, message):
        print(f"{message}: {head}")
    
    @staticmethod
    def assert_list(condition, message):
        if not condition:
            raise AssertionError(f"链表断言失败: {message}")
    
    @staticmethod
    def verify_no_cycle(head):
        if head is None:
            return True
        
        slow = head
        fast = head
        
        while fast is not None and fast.next is not None:
            slow = slow.next
            fast = fast.next.next
            if slow == fast:
                return False  # 有环
        return True  # 无环


"""
测试补充题目
"""
def test_merge_k_lists():
    print("=== 测试合并K个升序链表 ===")
    
    # 创建测试用例: lists = [[1,4,5],[1,3,4],[2,6]]
    list1 = ListNode.create_list([1, 4, 5])
    list2 = ListNode.create_list([1, 3, 4])
    list3 = ListNode.create_list([2, 6])
    lists = [list1, list2, list3]
    
    print("输入链表数组: ", end="")
    for i, lst in enumerate(lists):
        print(f"{'' if i == 0 else ', '}{lst if lst else '[]'}", end="")
    print()
    
    result = merge_k_lists(lists)
    print(f"合并后: {result if result else '[]'}")
    print()


def test_delete_node():
    print("=== 测试删除链表中的节点 ===")
    
    # 创建测试用例: [4,5,1,9], 删除节点5
    head = ListNode.create_list([4, 5, 1, 9])
    print(f"原链表: {head}")
    
    # 找到要删除的节点(值为5的节点)
    node_to_delete = head.next  # 值为5的节点
    
    print(f"删除节点: {node_to_delete.val}")
    delete_node(node_to_delete)
    print(f"删除后: {head}")
    print()


def test_delete_duplicates():
    print("=== 测试删除排序链表中的重复元素 ===")
    
    # 测试用例1: [1,1,2] -> [1,2]
    head1 = ListNode.create_list([1, 1, 2])
    print(f"原链表: {head1}")
    result1 = delete_duplicates(head1)
    print(f"去重后: {result1}")
    
    # 测试用例2: [1,1,2,3,3] -> [1,2,3]
    head2 = ListNode.create_list([1, 1, 2, 3, 3])
    print(f"原链表: {head2}")
    result2 = delete_duplicates(head2)
    print(f"去重后: {result2}")
    print()


def test_delete_duplicates_ii():
    print("=== 测试删除排序链表中的重复元素 II ===")
    
    # 测试用例1: [1,2,3,3,4,4,5] -> [1,2,5]
    head1 = ListNode.create_list([1, 2, 3, 3, 4, 4, 5])
    print(f"原链表: {head1}")
    result1 = delete_duplicates_ii(head1)
    print(f"删除重复元素后: {result1}")
    
    # 测试用例2: [1,1,1,2,3] -> [2,3]
    head2 = ListNode.create_list([1, 1, 1, 2, 3])
    print(f"原链表: {head2}")
    result2 = delete_duplicates_ii(head2)
    print(f"删除重复元素后: {result2}")
    print()


def test_remove_elements():
    print("=== 测试移除链表元素 ===")
    
    # 测试用例: [1,2,6,3,4,5,6], val = 6 -> [1,2,3,4,5]
    head = ListNode.create_list([1, 2, 6, 3, 4, 5, 6])
    val = 6
    print(f"原链表: {head}")
    print(f"移除元素: {val}")
    result = remove_elements(head, val)
    print(f"移除后: {result}")
    print()


def test_additional_problems():
    print("=== 补充题目测试 ===")
    
    # 测试链表随机节点
    random_test = ListNode.create_list([1, 2, 3, 4, 5])
    selector = RandomNodeSelector(random_test)
    print(f"随机节点选择测试: {selector.get_random()}")
    
    # 测试链表组件
    component_test = ListNode.create_list([0, 1, 2, 3])
    nums = [0, 1, 3]
    print(f"链表组件个数: {num_components(component_test, nums)}")
    
    # 测试下一个更大节点
    larger_test = ListNode.create_list([2, 1, 5])
    larger_result = next_larger_nodes(larger_test)
    print(f"下一个更大节点: {larger_result}")
    
    # 测试链表最大孪生和
    twin_test = ListNode.create_list([5, 4, 2, 1])
    print(f"链表最大孪生和: {pair_sum(twin_test)}")
    
    print("补充题目测试完成\n")


# 主函数
if __name__ == "__main__":
    # 设置随机种子
    random.seed(42)
    
    # 运行测试
    test_reverse_list()
    test_reverse_list_ii()
    test_reverse_k_group()
    run_unit_tests()
    test_additional_problems()
    
    # 运行补充题目的测试
    test_merge_k_lists()
    test_delete_node()
    test_delete_duplicates()
    test_delete_duplicates_ii()
    test_remove_elements()
    
    # 性能分析示例
    profiler = LinkedListProfiler()
    perf_test = ListNode.create_list([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    
    profiler.start()
    reversed_list = reverse_list_iterative(perf_test)
    profiler.end()
    
    print("\n=== 所有测试完成 ===")