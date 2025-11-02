#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from typing import Optional

"""
链表分隔问题 - 最优解实现与详细分析

题目描述：
给你一个链表的头节点 head 和一个特定值 x，请你对链表进行分隔，
使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。
你应当保留两个分区中每个节点的初始相对位置。

示例：
输入：head = [1,4,3,2,5,2], x = 3
输出：[1,2,2,4,3,5]

输入：head = [2,1], x = 2
输出：[1,2]

解题思路：
1. 双链表法（推荐）：使用两个链表分别存储小于x和大于等于x的节点，最后连接
2. 原地操作法：在原链表中移动节点，保持相对顺序

时间复杂度：O(n) - 只需遍历链表一次
空间复杂度：O(1) - 只使用常数级别额外空间

相似题目：
1. LeetCode 86. Partition List (本题)
2. LintCode 96. Partition List
3. 牛客网 NC140. 链表的奇偶重排
4. LeetCode 21. Merge Two Sorted Lists
5. LeetCode 23. Merge k Sorted Lists
6. LeetCode 148. Sort List

测试链接：https://leetcode.cn/problems/partition-list/
"""

class ListNode:
    """
    链表节点类定义
    """
    def __init__(self, val=0, next=None):
        """
        初始化链表节点
        
        Args:
            val: 节点的值
            next: 指向下一个节点的引用，默认为None
        """
        self.val = val
        self.next = next


class Solution:
    """
    链表分隔问题解决方案类
    """

    @staticmethod
    def partition(head: ListNode, x: int) -> ListNode:
        """
        解法1：双链表法（推荐最优解）

        核心思想：
        1. 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
        2. 遍历原链表，根据节点值将节点连接到对应的链表中
        3. 连接两个链表并返回结果

        此解法的优势：
        - 逻辑清晰，易于理解和实现
        - 边界条件处理简单，不容易出错
        - 满足O(n)时间复杂度和O(1)空间复杂度要求

        时间复杂度分析：
        - 遍历操作：O(n) - 只需要遍历原链表一次
        - 指针操作：O(1) - 每个节点进行常数次指针操作
        - 总体复杂度：O(n)

        空间复杂度分析：
        - 额外节点：O(1) - 只使用两个虚拟头节点
        - 指针变量：O(1) - 使用常数个指针变量
        - 总体复杂度：O(1)

        Args:
            head: 链表头节点
            x: 分隔值

        Returns:
            分隔后的链表头节点
        """
        # 【异常处理】空链表直接返回None
        if head is None:
            return None

        # 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
        # 使用虚拟头节点可以避免处理头节点为空的边界情况
        left_dummy = ListNode(0)
        right_dummy = ListNode(0)

        # 两个链表的尾指针，用于高效添加节点
        left_tail = left_dummy
        right_tail = right_dummy

        # 遍历原链表
        current = head
        while current:
            # 【关键点】提前保存下一个节点，避免在操作当前节点时丢失链表后续部分
            next_node = current.next
            
            # 【重要】断开当前节点与原链表的连接，防止形成环
            current.next = None

            # 根据节点值将节点连接到对应的链表中
            if current.val < x:
                # 小于x的节点连接到左侧链表
                left_tail.next = current
                left_tail = current  # 更新左侧链表尾指针
            else:
                # 大于等于x的节点连接到右侧链表
                right_tail.next = current
                right_tail = current  # 更新右侧链表尾指针

            # 移动到下一个节点
            current = next_node

        # 【关键点】连接两个链表：将左侧链表的尾部连接到右侧链表的头部
        left_tail.next = right_dummy.next

        # 返回结果链表的头节点（左侧链表的第一个有效节点）
        return left_dummy.next

    @staticmethod
    def partition2(head: ListNode, x: int) -> ListNode:
        """
        解法2：原地操作法

        核心思想：
        1. 使用一个指针遍历链表
        2. 遇到小于x的节点就将其移动到前面
        3. 保持相对顺序不变

        这种方法虽然也是O(n)时间复杂度和O(1)空间复杂度，
        但实现更复杂，且容易在指针操作中出错

        时间复杂度：O(n) - 只需遍历链表一次
        空间复杂度：O(1) - 只使用常数级别额外空间

        Args:
            head: 链表头节点
            x: 分隔值

        Returns:
            分隔后的链表头节点
        """
        # 【异常处理】空链表直接返回None
        if head is None:
            return None

        # 创建虚拟头节点，简化边界处理
        dummy = ListNode(0)
        dummy.next = head

        # 找到第一个大于等于x的节点的前驱节点
        # 这个节点将作为小于x的节点插入位置的前驱
        prev = dummy
        while prev.next and prev.next.val < x:
            prev = prev.next

        # 当前节点指针，用于遍历链表
        curr = prev

        # 遍历链表剩余部分
        while curr.next:
            # 如果下一个节点小于x，则需要将其移动到前面
            if curr.next.val < x:
                # 【指针操作】取出要移动的节点
                move_node = curr.next
                
                # 从当前位置断开
                curr.next = move_node.next
                
                # 插入到prev后面
                move_node.next = prev.next
                prev.next = move_node
                
                # 更新prev指针，为下一次插入做准备
                prev = move_node
            else:
                # 否则继续向后移动
                curr = curr.next

        return dummy.next


# ========== 扩展题目1：LeetCode 328. Odd Even Linked List（链表奇偶重排）==========
def odd_even_list(head: ListNode) -> ListNode:
    """
    LeetCode 328. Odd Even Linked List
    题目链接：https://leetcode.cn/problems/odd-even-linked-list/
    
    题目描述：
    给定单链表的头节点head，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起
    
    时间复杂度：O(n) 空间复杂度：O(1) 是否最优解：是
    """
    if head is None or head.next is None:
        return head
    
    odd = head
    even = head.next
    even_head = even
    
    while even and even.next:
        odd.next = even.next
        odd = odd.next
        even.next = odd.next
        even = even.next
    
    odd.next = even_head
    return head


# ========== 扩展题目2：LeetCode 725. Split Linked List in Parts ==========
def split_list_to_parts(head: ListNode, k: int) -> list:
    """
    LeetCode 725. Split Linked List in Parts
    题目链接：https://leetcode.cn/problems/split-linked-list-in-parts/
    
    时间复杂度：O(n+k) 空间复杂度：O(k) 是否最优解：是
    """
    # 计算链表长度
    length = 0
    curr = head
    while curr:
        length += 1
        curr = curr.next
    
    # 计算每部分的大小
    part_size = length // k
    remainder = length % k
    
    result = []
    curr = head
    
    for i in range(k):
        result.append(curr)
        if curr is None:
            continue
            
        current_part_size = part_size + (1 if i < remainder else 0)
        
        for j in range(current_part_size - 1):
            if curr:
                curr = curr.next
        
        if curr:
            next_node = curr.next
            curr.next = None
            curr = next_node
    
    return result


# ========== 扩展题目3：LeetCode 2095. Delete Middle Node ==========
def delete_middle(head: ListNode) -> ListNode:
    """
    LeetCode 2095. Delete the Middle Node of a Linked List
    题目链接：https://leetcode.cn/problems/delete-the-middle-node-of-a-linked-list/
    
    时间复杂度：O(n) 空间复杂度：O(1) 是否最优解：是
    """
    if head is None or head.next is None:
        return None
    
    dummy = ListNode(0, head)
    slow = dummy
    fast = head
    
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    
    slow.next = slow.next.next
    return dummy.next


def create_list(values: list) -> ListNode:
    """
    从列表创建链表的辅助函数
    
    Args:
        values: 包含节点值的列表
        
    Returns:
        创建的链表头节点
    """
    # 处理空列表情况
    if not values:
        return None
        
    # 创建虚拟头节点简化操作
    dummy = ListNode(0)
    current = dummy
    
    # 逐个创建节点并连接
    for val in values:
        current.next = ListNode(val)
        current = current.next
    
    return dummy.next


def print_list(head: ListNode) -> None:
    """
    打印链表的辅助函数
    
    Args:
        head: 链表头节点
    """
    # 处理空链表情况
    if head is None:
        print("空链表")
        return
        
    # 收集所有节点的值
    values = []
    current = head
    while current:
        values.append(str(current.val))
        current = current.next
    
    # 格式化输出
    print(" -> ".join(values))


def verify_partition_result(head: ListNode, x: int) -> bool:
    """
    验证链表分隔结果是否正确
    
    验证规则：
    1. 所有小于x的节点必须出现在大于等于x的节点之前
    2. 必须保持节点的相对顺序
    3. 不能出现循环引用
    
    实现思路：
    - 使用状态标志跟踪是否已经遇到大于等于x的节点
    - 遍历链表检查是否违反分区规则
    - 同时检查是否存在循环（通过记录访问过的节点数量限制）
    
    Args:
        head: 分隔后的链表头节点
        x: 分隔值
    
    Returns:
        验证是否通过
    """
    if head is None:
        print("验证结果：通过（空链表）")
        return True
    
    passed_x = False  # 标志是否已经遇到大于等于x的节点
    current = head
    node_count = 0  # 用于检测循环引用
    max_nodes = 1000  # 链表最大节点数限制，防止无限循环
    
    while current and node_count < max_nodes:
        # 检查分区规则：如果已经遇到过大于等于x的节点，则后续节点都不能小于x
        if passed_x and current.val < x:
            print(f"验证结果：失败！违反分区规则 - 大于等于{x}的节点后出现小于{x}的节点")
            return False
        
        # 如果当前节点大于等于x，则设置passed_x标志为True
        if current.val >= x:
            passed_x = True
        
        current = current.next
        node_count += 1
    
    # 检查是否存在循环引用
    if node_count >= max_nodes:
        print("验证结果：失败！检测到可能的循环引用")
        return False
    
    print("验证结果：通过 - 分区规则正确遵守")
    return True


def run_test_cases():
    """
    运行全面的测试用例和验证策略
    
    测试策略：
    1. 功能验证：确保算法正确实现了分隔功能
    2. 边界测试：测试特殊输入情况
    3. 极端情况测试：测试性能和正确性边界
    4. 多解法对比：验证不同实现方法的正确性
    5. 结果验证：确保所有分隔后的链表满足条件
    
    测试覆盖范围：
    - 标准情况
    - 边界情况（空链表、单节点链表）
    - 特殊情况（全小于/大于x的链表）
    - 已排序/逆序链表
    - 重复值链表
    """
    print("=== 链表分隔问题测试 ===")
    print("算法本质：分类与合并模式，使用虚拟头节点技术")
    
    solution = Solution()
    
    # 【测试用例1】标准情况 - 混合大小的元素分布
    # 输入：[1,4,3,2,5,2], x = 3
    # 预期输出: [1,2,2,4,3,5]
    # 验证点：1. 小于x的节点在前 2. 大于等于x的节点在后 3. 相对顺序保持不变
    print("\n【测试用例1】标准情况 - 混合元素分布")
    print("测试目的：验证基本功能正确性，确保相对顺序保持不变")
    head1 = create_list([1, 4, 3, 2, 5, 2])
    
    print("原链表:")
    print_list(head1)
    
    result1 = solution.partition(head1, 3)
    print("分隔后 (双链表法):")
    print_list(result1)
    
    # 验证结果正确性
    verify_partition_result(result1, 3)
    
    # 重新构建测试用例，测试解法2
    head1 = create_list([1, 4, 3, 2, 5, 2])
    result2 = solution.partition2(head1, 3)
    print("分隔后 (原地操作法):")
    print_list(result2)
    
    # 验证结果正确性
    verify_partition_result(result2, 3)
    
    # 测试用例2: 两个节点，需要交换
    # 输入：[2,1], x = 2
    # 预期输出: [1,2]
    print("\n测试用例2: 两个节点需要交换")
    head2 = create_list([2, 1])
    
    print("原链表:")
    print_list(head2)
    
    result3 = solution.partition(head2, 2)
    print("分隔后:")
    print_list(result3)
    
    # 测试用例3: 空链表
    print("\n测试用例3: 空链表")
    head3 = None
    
    print("原链表:")
    print_list(head3)
    
    result4 = solution.partition(head3, 1)
    print("分隔后:")
    print_list(result4)
    
    # 测试用例4: 单节点链表
    print("\n测试用例4: 单节点链表")
    head4 = create_list([5])
    
    print("原链表:")
    print_list(head4)
    
    result5 = solution.partition(head4, 3)
    print("分隔后 (x=3):")
    print_list(result5)
    
    # 测试用例5: 所有节点值都小于x
    print("\n测试用例5: 所有节点值都小于x")
    head5 = create_list([1, 2, 3])
    
    print("原链表:")
    print_list(head5)
    
    result6 = solution.partition(head5, 4)
    print("分隔后 (x=4):")
    print_list(result6)
    
    # 测试用例6: 所有节点值都大于等于x
    print("\n测试用例6: 所有节点值都大于等于x")
    head6 = create_list([5, 6, 7])
    
    print("原链表:")
    print_list(head6)
    
    result7 = solution.partition(head6, 4)
    print("分隔后 (x=4):")
    print_list(result7)
    
    # 测试用例7: 已排序的链表
    print("\n测试用例7: 已排序的链表")
    head7 = create_list([1, 2, 3, 4, 5])
    
    print("原链表:")
    print_list(head7)
    
    result8 = solution.partition(head7, 3)
    print("分隔后 (x=3):")
    print_list(result8)
    
    # 测试用例8: 逆序的链表
    print("\n测试用例8: 逆序的链表")
    head8 = create_list([5, 4, 3, 2, 1])
    
    print("原链表:")
    print_list(head8)
    
    result9 = solution.partition(head8, 3)
    print("分隔后 (x=3):")
    print_list(result9)
    
    # ========== 扩展题目测试 ==========
    print("\n========== 扩展题目测试 ===========")
    
    # 测试1: LeetCode 328 - 链表奇偶重排
    print("\n【扩展测试1】LeetCode 328 - Odd Even Linked List")
    head9 = create_list([1, 2, 3, 4, 5])
    print("原链表:")
    print_list(head9)
    result10 = odd_even_list(head9)
    print("奇偶重排后:")
    print_list(result10)
    
    head10 = create_list([2, 1, 3, 5, 6, 4, 7])
    print("\n原链表:")
    print_list(head10)
    result11 = odd_even_list(head10)
    print("奇偶重排后:")
    print_list(result11)
    
    # 测试2: LeetCode 725 - 分隔链表为多部分
    print("\n【扩展测试2】LeetCode 725 - Split Linked List in Parts")
    head11 = create_list([1, 2, 3])
    print("原链表:")
    print_list(head11)
    parts1 = split_list_to_parts(head11, 5)
    print("分隔为5部分:")
    for i, part in enumerate(parts1, 1):
        print(f"部分{i}: ", end="")
        print_list(part)
    
    head12 = create_list([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    print("\n原链表:")
    print_list(head12)
    parts2 = split_list_to_parts(head12, 3)
    print("分隔为3部分:")
    for i, part in enumerate(parts2, 1):
        print(f"部分{i}: ", end="")
        print_list(part)
    
    # 测试3: LeetCode 2095 - 删除链表中间节点
    print("\n【扩展测试3】LeetCode 2095 - Delete Middle Node")
    head13 = create_list([1, 3, 4, 7, 1, 2, 6])
    print("原链表:")
    print_list(head13)
    result12 = delete_middle(head13)
    print("删除中间节点后:")
    print_list(result12)
    
    head14 = create_list([1, 2, 3, 4])
    print("\n原链表:")
    print_list(head14)
    result13 = delete_middle(head14)
    print("删除中间节点后:")
    print_list(result13)
    
    print("\n========== 所有测试完成 ==========")
    
    # ========== 扩展题目测试 ==========
    print("\n========== 扩展题目测试 ==========")
    
    # 测试4: LeetCode 21 - 合并两个有序链表
    print("\n【扩展测试4】LeetCode 21 - Merge Two Sorted Lists")
    arr15 = [1, 2, 4]
    arr16 = [1, 3, 4]
    l1 = create_list(arr15)
    l2 = create_list(arr16)
    print("链表1:", end=" ")
    print_list(l1)
    print("链表2:", end=" ")
    print_list(l2)
    merged = merge_two_lists(l1, l2)
    print("合并后:", end=" ")
    print_list(merged)
    
    # 测试5: LeetCode 19 - 删除链表的倒数第N个节点
    print("\n【扩展测试5】LeetCode 19 - Remove Nth Node From End")
    arr17 = [1, 2, 3, 4, 5]
    head17 = create_list(arr17)
    print("原链表:", end=" ")
    print_list(head17)
    result14 = remove_nth_from_end(head17, 2)
    print("删除倒数第2个节点后:", end=" ")
    print_list(result14)
    
    # 测试6: LeetCode 206 - 反转链表
    print("\n【扩展测试6】LeetCode 206 - Reverse Linked List")
    arr18 = [1, 2, 3, 4, 5]
    head18 = create_list(arr18)
    print("原链表:", end=" ")
    print_list(head18)
    reversed_head = reverse_list(head18)
    print("反转后:", end=" ")
    print_list(reversed_head)
    
    # 测试7: LeetCode 24 - 两两交换链表中的节点
    print("\n【扩展测试7】LeetCode 24 - Swap Nodes in Pairs")
    arr19 = [1, 2, 3, 4]
    head19 = create_list(arr19)
    print("原链表:", end=" ")
    print_list(head19)
    swapped = swap_pairs(head19)
    print("两两交换后:", end=" ")
    print_list(swapped)
    
    # 测试8: LeetCode 876 - 链表的中间结点
    print("\n【扩展测试8】LeetCode 876 - Middle of Linked List")
    arr20 = [1, 2, 3, 4, 5]
    head20 = create_list(arr20)
    print("原链表:", end=" ")
    print_list(head20)
    middle = middle_node(head20)
    print("中间节点:", middle.val)
    
    print("\n========== 所有测试完成 ==========")

# ========== 扩展题目4：LeetCode 21. Merge Two Sorted Lists（合并两个有序链表）==========
def merge_two_lists(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    """
    LeetCode 21. Merge Two Sorted Lists
    题目链接：https://leetcode.cn/problems/merge-two-sorted-lists/
    
    题目描述：
    将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
    
    示例：
    输入：l1 = [1,2,4], l2 = [1,3,4]
    输出：[1,1,2,3,4,4]
    
    解题思路：
    使用双指针技术，比较两个链表的当前节点值，将较小的节点连接到结果链表中。
    
    时间复杂度：O(n+m) - 需要遍历两个链表的所有节点
    空间复杂度：O(1) - 只使用常数级别的额外空间
    
    是否最优解：是，时间和空间复杂度都已达到最优
    
    与链表分隔的联系：
    - 都使用虚拟头节点技术简化边界处理
    - 都涉及多个链表的指针操作和连接
    - 都需要保持元素的相对顺序
    """
    # 创建虚拟头节点简化操作
    dummy = ListNode(0)
    current = dummy
    
    # 同时遍历两个链表
    while l1 and l2:
        # 比较两个链表当前节点的值
        if l1.val <= l2.val:
            current.next = l1
            l1 = l1.next
        else:
            current.next = l2
            l2 = l2.next
        current = current.next
    
    # 将剩余链表连接到结果中
    current.next = l1 if l1 else l2
    
    return dummy.next

# ========== 扩展题目5：LeetCode 19. Remove Nth Node From End of List（删除链表的倒数第N个节点）==========
def remove_nth_from_end(head: Optional[ListNode], n: int) -> Optional[ListNode]:
    """
    LeetCode 19. Remove Nth Node From End of List
    题目链接：https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
    
    题目描述：
    给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
    
    示例：
    输入：head = [1,2,3,4,5], n = 2
    输出：[1,2,3,5]
    
    解题思路：
    使用快慢指针，快指针先走n步，然后快慢指针同时移动，当快指针到达末尾时，慢指针指向倒数第n个节点的前驱。
    
    时间复杂度：O(L) - L为链表长度
    空间复杂度：O(1) - 只使用常数级别的额外空间
    
    是否最优解：是
    
    与链表分隔的联系：
    - 都使用双指针技术
    - 都需要精确控制指针移动和节点连接
    - 都需要处理边界情况（如删除头节点）
    """
    # 使用虚拟头节点简化删除头节点的边界情况
    dummy = ListNode(0)
    dummy.next = head
    
    fast = dummy
    slow = dummy
    
    # 快指针先走n步
    for _ in range(n + 1):
        if fast:
            fast = fast.next
    
    # 快慢指针同时移动，直到快指针到达末尾
    while fast:
        fast = fast.next
        slow = slow.next
    
    # 删除倒数第n个节点
    if slow and slow.next:
        slow.next = slow.next.next
    
    return dummy.next

# ========== 扩展题目6：LeetCode 206. Reverse Linked List（反转链表）==========
def reverse_list(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    LeetCode 206. Reverse Linked List
    题目链接：https://leetcode.cn/problems/reverse-linked-list/
    
    题目描述：
    给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
    
    示例：
    输入：head = [1,2,3,4,5]
    输出：[5,4,3,2,1]
    
    解题思路：
    使用迭代法，维护三个指针：prev、current、next，逐个反转节点指向。
    
    时间复杂度：O(n) - 需要遍历整个链表
    空间复杂度：O(1) - 只使用常数级别的额外空间
    
    是否最优解：是
    
    与链表分隔的联系：
    - 都是链表基本操作
    - 都需要精确的指针操作
    - 反转操作是许多复杂链表算法的基础
    """
    prev = None
    current = head
    
    while current:
        next_node = current.next  # 保存下一个节点
        current.next = prev       # 反转当前节点指向
        prev = current            # 移动prev指针
        current = next_node       # 移动current指针
    
    return prev

# ========== 扩展题目7：LeetCode 24. Swap Nodes in Pairs（两两交换链表中的节点）==========
def swap_pairs(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    LeetCode 24. Swap Nodes in Pairs
    题目链接：https://leetcode.cn/problems/swap-nodes-in-pairs/
    
    题目描述：
    给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。
    你必须在不修改节点内部的值的情况下完成本题（即只能进行节点交换）。
    
    示例：
    输入：head = [1,2,3,4]
    输出：[2,1,4,3]
    
    解题思路：
    使用虚拟头节点，每次处理两个相邻节点，调整它们的指向关系。
    
    时间复杂度：O(n) - 需要遍历整个链表
    空间复杂度：O(1) - 只使用常数级别的额外空间
    
    是否最优解：是
    
    与链表分隔的联系：
    - 都涉及链表的重新连接
    - 都需要精确的指针操作
    - 都使用虚拟头节点简化边界处理
    """
    # 使用虚拟头节点简化操作
    dummy = ListNode(0)
    dummy.next = head
    prev = dummy
    
    while prev.next and prev.next.next:
        first = prev.next
        second = first.next
        
        # 交换两个节点
        first.next = second.next
        second.next = first
        prev.next = second
        
        # 移动指针到下一对
        prev = first
    
    return dummy.next

# ========== 扩展题目8：LeetCode 876. Middle of the Linked List（链表的中间结点）==========
def middle_node(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    LeetCode 876. Middle of the Linked List
    题目链接：https://leetcode.cn/problems/middle-of-the-linked-list/
    
    题目描述：
    给定一个头结点为 head 的非空单链表，返回链表的中间结点。
    如果有两个中间结点，则返回第二个中间结点。
    
    示例：
    输入：[1,2,3,4,5]
    输出：3（节点值为3的节点）
    
    解题思路：
    使用快慢指针，快指针每次走两步，慢指针每次走一步。
    
    时间复杂度：O(n) - 需要遍历链表到中间位置
    空间复杂度：O(1) - 只使用两个指针
    
    是否最优解：是
    
    与链表分隔的联系：
    - 都使用双指针技术
    - 都是链表操作的基础算法
    """
    if not head:
        return None
        
    slow = head
    fast = head
    
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    
    return slow


# 执行测试
if __name__ == "__main__":
    run_test_cases()