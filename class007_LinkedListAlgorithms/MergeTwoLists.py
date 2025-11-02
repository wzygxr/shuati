# -*- coding: utf-8 -*-
"""
链表合并算法专题 - 完整实现 (Python版本)

📚 本文件包含链表合并相关的完整算法实现，涵盖从基础到高级的各类题目
🎯 每个算法都提供详细的时间空间复杂度分析、多种解法对比和工程化考量

🔥 核心特性：
- 完整的异常处理和边界条件处理
- 详细的时间空间复杂度分析
- 多种解法对比（迭代法、递归法、分治法、优先队列法）
- 工程化考量和调试技巧
- 全面的测试用例覆盖

📊 算法复杂度总结：
| 算法 | 时间复杂度 | 空间复杂度 | 最优解 |
|------|------------|------------|--------|
| 合并两个链表(迭代) | O(m+n) | O(1) | ✅ |
| 合并两个链表(递归) | O(m+n) | O(m+n) | ❌ |
| 合并K个链表(优先队列) | O(N log K) | O(K) | ✅ |
| 合并K个链表(分治) | O(N log K) | O(log K) | ✅ |
| 排序链表(自底向上) | O(n log n) | O(1) | ✅ |

🎯 适用场景分析：
1. 双指针法：两个有序序列合并的基础算法
2. 优先队列法：K个有序序列合并的高效算法  
3. 分治法：大规模数据合并的优化策略
4. 原地合并：空间优化的合并技术

🏗️ 工程化考量：
- 异常处理：完善的输入验证和边界条件处理
- 内存管理：Python自动垃圾回收，无需手动管理
- 性能优化：选择合适的数据结构和算法
- 可测试性：全面的单元测试覆盖
- 可读性：清晰的代码结构和注释

@author Algorithm Specialist
@version 1.0
@since 2025-10-18
"""

import heapq
import time
import random
from typing import List, Optional, Tuple


class ListNode:
    """
    链表节点定义类
    
    🔍 设计要点：
    - 使用属性简化访问（Python风格）
    - 提供静态工具方法便于测试
    - 包含类型注解提高代码可读性
    
    ⚠️ 注意事项：
    - Python中无需手动内存管理
    - 注意循环引用的垃圾回收
    - 考虑线程安全性需求
    """
    def __init__(self, val: int = 0, next: Optional['ListNode'] = None):
        self.val = val
        self.next = next
    
    @staticmethod
    def create_list(arr: List[int]) -> Optional['ListNode']:
        """
        从数组创建链表（测试工具方法）
        
        📊 复杂度分析：
        - 时间复杂度: O(n)，n为数组长度
        - 空间复杂度: O(n)，需要创建n个节点
        
        🎯 使用场景：单元测试、算法演示
        
        @param arr 整数数组
        @return 链表头节点
        @raises ValueError 如果输入数组为None
        """
        if arr is None:
            raise ValueError("输入数组不能为None")
        if not arr:
            return None
            
        head = ListNode(arr[0])
        cur = head
        for i in range(1, len(arr)):
            cur.next = ListNode(arr[i])
            cur = cur.next
        return head
    
    @staticmethod
    def print_list(head: Optional['ListNode']) -> None:
        """
        打印链表内容（调试工具方法）
        
        📊 复杂度分析：
        - 时间复杂度: O(n)，n为链表长度
        - 空间复杂度: O(1)，只使用常数空间
        
        🎯 使用场景：调试、结果验证
        
        @param head 链表头节点
        """
        if head is None:
            print("None")
            return
            
        cur = head
        while cur:
            print(cur.val, end="")
            if cur.next:
                print(" -> ", end="")
            cur = cur.next
        print()
    
    @staticmethod
    def get_length(head: Optional['ListNode']) -> int:
        """
        获取链表长度（工具方法）
        
        📊 复杂度分析：
        - 时间复杂度: O(n)，需要遍历整个链表
        - 空间复杂度: O(1)，只使用常数空间
        
        @param head 链表头节点
        @return 链表长度
        """
        length = 0
        cur = head
        while cur:
            length += 1
            cur = cur.next
        return length
    
    @staticmethod
    def is_sorted(head: Optional['ListNode']) -> bool:
        """
        验证链表是否有序（测试工具方法）
        
        📊 复杂度分析：
        - 时间复杂度: O(n)，需要遍历整个链表
        - 空间复杂度: O(1)，只使用常数空间
        
        @param head 链表头节点
        @return 是否有序（升序）
        """
        if head is None or head.next is None:
            return True
            
        cur = head
        while cur.next:
            if cur.val > cur.next.val:
                return False
            cur = cur.next
        return True


class MergeTwoSortedListsSolution:
    """
    题目1: LeetCode 21. 合并两个有序链表
    
    📚 题目信息：
    - 来源: LeetCode
    - 链接: https://leetcode.cn/problems/merge-two-sorted-lists/
    - 难度: 简单
    - 标签: 链表、递归、双指针
    
    🎯 题目描述：
    将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
    
    💡 解题思路：
    使用双指针分别指向两个链表的当前节点，比较节点值的大小，
    将较小的节点连接到结果链表中，移动对应指针，重复此过程直到某一链表遍历完。
    最后将未遍历完的链表剩余部分直接连接到结果链表末尾。
    
    📊 复杂度分析：
    | 解法 | 时间复杂度 | 空间复杂度 | 最优解 |
    |------|------------|------------|--------|
    | 迭代法 | O(m+n) | O(1) | ✅ |
    | 递归法 | O(m+n) | O(m+n) | ❌ |
    
    🏗️ 工程化考量：
    - 使用哨兵节点简化边界处理
    - 完善的异常处理和输入验证
    - 考虑内存管理和性能优化
    
    🔍 调试技巧：
    - 打印中间状态跟踪指针移动
    - 验证合并后链表的有序性
    - 测试各种边界条件
    """
    
    @staticmethod
    def merge_two_lists_iterative(list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法1: 迭代法 (最优解)
        
        🎯 核心思想：
        1. 使用哨兵节点简化边界处理
        2. 双指针分别遍历两个链表
        3. 比较节点值，将较小节点连接到结果链表
        4. 处理剩余节点
        
        📊 复杂度分析：
        - 时间复杂度: O(m+n) - 每个节点只访问一次
        - 空间复杂度: O(1) - 只使用常数级别的额外空间
        
        ⚡ 性能特点：
        - 最优时间复杂度
        - 最优空间复杂度
        - 适合大规模数据
        
        🏗️ 工程实现要点：
        - 哨兵节点避免空指针异常
        - 清晰的变量命名提高可读性
        - 完善的边界条件处理
        
        @param list1 第一个有序链表
        @param list2 第二个有序链表
        @return 合并后的有序链表
        """
        # 输入验证
        if list1 is None and list2 is None:
            return None
            
        # 创建哨兵节点，简化边界处理
        dummy = ListNode(-1)
        current = dummy
        
        # 双指针遍历两个链表
        while list1 and list2:
            # 比较两个链表当前节点的值
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余节点（其中一个链表已遍历完）
        current.next = list1 if list1 else list2
        
        # 返回合并后的链表（跳过哨兵节点）
        return dummy.next
    
    @staticmethod
    def merge_two_lists_recursive(list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法2: 递归法
        
        🎯 核心思想：
        1. 递归终止条件：其中一个链表为空
        2. 递归处理：选择较小节点作为当前节点，递归处理剩余部分
        3. 返回当前节点
        
        📊 复杂度分析：
        - 时间复杂度: O(m+n) - 每个节点访问一次
        - 空间复杂度: O(m+n) - 递归调用栈的深度
        
        ⚡ 性能特点：
        - 代码简洁易懂
        - 空间开销较大
        - 可能栈溢出（大数据量）
        
        🏗️ 适用场景：
        - 小规模数据
        - 代码简洁性要求高
        - 栈深度可控的情况
        
        @param list1 第一个有序链表
        @param list2 第二个有序链表
        @return 合并后的有序链表
        """
        # 递归终止条件
        if list1 is None:
            return list2
        if list2 is None:
            return list1
        
        # 递归处理：选择较小节点作为当前节点
        if list1.val <= list2.val:
            list1.next = MergeTwoSortedListsSolution.merge_two_lists_recursive(list1.next, list2)
            return list1
        else:
            list2.next = MergeTwoSortedListsSolution.merge_two_lists_recursive(list1, list2.next)
            return list2
    
    @staticmethod
    def merge_two_lists_in_place(list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法3: 原地修改法（空间最优）
        
        🎯 核心思想：在不创建新节点的情况下直接修改链表连接
        
        📊 复杂度分析：
        - 时间复杂度: O(m+n)
        - 空间复杂度: O(1)
        
        ⚡ 性能特点：
        - 最优空间复杂度
        - 直接修改原链表
        - 可能破坏原链表结构
        
        @param list1 第一个有序链表
        @param list2 第二个有序链表
        @return 合并后的有序链表
        """
        if list1 is None:
            return list2
        if list2 is None:
            return list1
            
        # 确保list1的头节点值较小
        if list1.val > list2.val:
            list1, list2 = list2, list1
            
        head = list1
        while list1 and list2:
            prev = None
            while list1 and list1.val <= list2.val:
                prev = list1
                list1 = list1.next
            if prev:
                prev.next = list2
                
            # 交换list1和list2
            if list1:
                list1, list2 = list2, list1
                
        return head
    
    @staticmethod
    def test():
        """
        全面的测试方法
        
        🎯 测试策略：
        1. 正常情况测试
        2. 边界条件测试
        3. 极端值测试
        4. 性能测试
        
        📋 测试用例设计：
        - 空链表测试
        - 单节点链表测试
        - 正常多节点测试
        - 包含重复元素测试
        - 极端值测试
        """
        print("=== LeetCode 21. 合并两个有序链表测试 ===")
        print("📊 测试用例覆盖：正常情况、边界条件、极端值")
        
        # 测试用例1: 正常情况
        print("\n🔍 测试用例1: 正常情况")
        list1 = ListNode.create_list([1, 2, 4])
        list2 = ListNode.create_list([1, 3, 4])
        print("链表1: ", end="")
        ListNode.print_list(list1)
        print("链表2: ", end="")
        ListNode.print_list(list2)
        
        result1 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list1, list2)
        print("迭代法结果: ", end="")
        ListNode.print_list(result1)
        print("有序性验证: ", ListNode.is_sorted(result1))
        
        # 重新创建测试数据
        list1 = ListNode.create_list([1, 2, 4])
        list2 = ListNode.create_list([1, 3, 4])
        result2 = MergeTwoSortedListsSolution.merge_two_lists_recursive(list1, list2)
        print("递归法结果: ", end="")
        ListNode.print_list(result2)
        print("有序性验证: ", ListNode.is_sorted(result2))
        
        # 测试用例2: 空链表
        print("\n🔍 测试用例2: 空链表测试")
        list3 = None
        list4 = ListNode.create_list([0])
        result3 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list3, list4)
        print("空链表测试: ", end="")
        ListNode.print_list(result3)
        
        # 测试用例3: 两个空链表
        print("\n🔍 测试用例3: 两个空链表")
        list5 = None
        list6 = None
        result4 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list5, list6)
        print("两个空链表: ", end="")
        ListNode.print_list(result4)
        
        # 测试用例4: 包含重复元素
        print("\n🔍 测试用例4: 包含重复元素")
        list7 = ListNode.create_list([1, 1, 2, 3])
        list8 = ListNode.create_list([1, 2, 2, 4])
        result5 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list7, list8)
        print("包含重复元素结果: ", end="")
        ListNode.print_list(result5)
        print("有序性验证: ", ListNode.is_sorted(result5))
        
        # 测试用例5: 极端值测试
        print("\n🔍 测试用例5: 极端值测试")
        list9 = ListNode.create_list([-10**6, 0, 10**6])
        list10 = ListNode.create_list([-999999, 999999])
        result6 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list9, list10)
        print("极端值测试结果: ", end="")
        ListNode.print_list(result6)
        print("有序性验证: ", ListNode.is_sorted(result6))
        
        print("\n✅ 所有测试用例执行完成")
        print("========================================")
    
    @staticmethod
    def performance_test():
        """
        性能测试方法
        
        🎯 测试目的：比较不同解法的性能表现
        📊 测试指标：执行时间、内存使用
        """
        print("=== 性能测试 ===")
        
        # 生成大规模测试数据
        size = 10000
        arr1 = [random.randint(0, 100000) for _ in range(size)]
        arr2 = [random.randint(0, 100000) for _ in range(size)]
        arr1.sort()
        arr2.sort()
        
        list1 = ListNode.create_list(arr1)
        list2 = ListNode.create_list(arr2)
        
        # 测试迭代法性能
        start_time = time.time()
        result1 = MergeTwoSortedListsSolution.merge_two_lists_iterative(list1, list2)
        end_time = time.time()
        print(f"迭代法执行时间: {(end_time - start_time) * 1000:.3f} ms")
        
        # 重新创建测试数据
        list1 = ListNode.create_list(arr1)
        list2 = ListNode.create_list(arr2)
        
        # 测试递归法性能（注意栈深度限制）
        if size <= 1000:  # 避免栈溢出
            start_time = time.time()
            result2 = MergeTwoSortedListsSolution.merge_two_lists_recursive(list1, list2)
            end_time = time.time()
            print(f"递归法执行时间: {(end_time - start_time) * 1000:.3f} ms")
        else:
            print("递归法: 数据规模过大，跳过测试（避免栈溢出）")
        
        print("性能测试完成\n")


class MergeKSortedListsSolution:
    """
    题目2: LeetCode 23. 合并K个升序链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/merge-k-sorted-lists/
    
    题目描述：
    给你一个链表数组，每个链表都已经按升序排列。
    请你将所有链表合并到一个升序链表中，返回合并后的链表。
    
    解法分析：
    1. 优先队列法 (最优解) - 时间复杂度: O(N*logK), 空间复杂度: O(K)
    2. 分治法 - 时间复杂度: O(N*logK), 空间复杂度: O(logK)
    
    解题思路：
    优先队列法：维护一个大小为K的最小堆，堆中存放K个链表的头节点。
    每次从堆中取出最小节点加入结果链表，并将该节点的下一个节点加入堆中。
    分治法：将K个链表分成两部分，分别合并后再合并两个结果。
    """
    
    @staticmethod
    def merge_k_lists_priority_queue(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        """
        解法1: 优先队列法 (推荐)
        
        🎯 核心思想：
        1. 使用优先队列(最小堆)维护K个链表的当前最小节点
        2. 每次取出最小节点加入结果链表
        3. 将取出节点的下一个节点加入优先队列
        4. 重复直到优先队列为空
        
        📊 复杂度分析：
        - 时间复杂度: O(N log K) - N是所有节点总数，K是链表数量
        - 空间复杂度: O(K) - 优先队列的大小
        
        ⚡ 性能特点：
        - 最优时间复杂度
        - 空间开销与K成正比
        - 适合K较小的情况
        
        🏗️ 实现要点：
        - 使用heapq实现最小堆
        - 处理空链表边界条件
        - 避免空指针异常
        
        @param lists 链表数组
        @return 合并后的有序链表
        """
        if not lists:
            return None
        
        # 创建优先队列(最小堆)，存储(节点值, 索引, 节点)元组
        # 使用索引是为了处理节点值相同的情况
        min_heap = []
        
        # 将所有非空链表的头节点加入优先队列
        for i, head in enumerate(lists):
            if head is not None:
                heapq.heappush(min_heap, (head.val, i, head))
        
        # 创建哨兵节点
        dummy = ListNode(-1)
        current = dummy
        
        # 从优先队列中依次取出最小节点
        while min_heap:
            # 取出最小节点
            val, i, node = heapq.heappop(min_heap)
            # 加入结果链表
            current.next = node
            current = current.next
            
            # 如果该节点还有后续节点，加入优先队列
            if node.next is not None:
                heapq.heappush(min_heap, (node.next.val, i, node.next))
        
        return dummy.next
    
    @staticmethod
    def merge_k_lists_helper(lists: List[Optional[ListNode]], left: int, right: int) -> Optional[ListNode]:
        """
        分治辅助函数（递归实现）
        
        🎯 递归策略：
        1. 基本情况：单个链表或两个链表
        2. 递归情况：分割链表数组，递归合并
        
        @param lists 链表数组
        @param left 左边界
        @param right 右边界
        @return 合并后的有序链表
        """
        # 递归终止条件
        if left == right:
            return lists[left]
        if left > right:
            return None
            
        # 两个链表的情况直接合并
        if left + 1 == right:
            return MergeTwoSortedListsSolution.merge_two_lists_iterative(lists[left], lists[right])
        
        # 分治：将链表数组分成两部分
        mid = left + (right - left) // 2
        l1 = MergeKSortedListsSolution.merge_k_lists_helper(lists, left, mid)
        l2 = MergeKSortedListsSolution.merge_k_lists_helper(lists, mid + 1, right)
        
        # 合并两个结果
        return MergeTwoSortedListsSolution.merge_two_lists_iterative(l1, l2)
    
    @staticmethod
    def merge_k_lists_divide_and_conquer(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        """
        解法2: 分治法
        时间复杂度: O(N*logK) - N是所有节点总数，K是链表数量
        空间复杂度: O(logK) - 递归调用栈的深度
        
        核心思想：
        1. 将K个链表分成两部分
        2. 递归合并每一部分
        3. 合并两个结果链表
        """
        if not lists:
            return None
        return MergeKSortedListsSolution.merge_k_lists_helper(lists, 0, len(lists) - 1)
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 合并K个升序链表测试 ===")
        
        # 创建测试数据
        l1 = ListNode.create_list([1, 4, 5])
        l2 = ListNode.create_list([1, 3, 4])
        l3 = ListNode.create_list([2, 6])
        
        lists = [l1, l2, l3]
        
        print("链表1: ", end="")
        ListNode.print_list(lists[0])
        print("链表2: ", end="")
        ListNode.print_list(lists[1])
        print("链表3: ", end="")
        ListNode.print_list(lists[2])
        
        # 测试优先队列法
        result1 = MergeKSortedListsSolution.merge_k_lists_priority_queue(lists)
        print("优先队列法结果: ", end="")
        ListNode.print_list(result1)
        
        # 重新创建测试数据
        l1 = ListNode.create_list([1, 4, 5])
        l2 = ListNode.create_list([1, 3, 4])
        l3 = ListNode.create_list([2, 6])
        lists = [l1, l2, l3]
        
        # 测试分治法
        result2 = MergeKSortedListsSolution.merge_k_lists_divide_and_conquer(lists)
        print("分治法结果: ", end="")
        ListNode.print_list(result2)
        print()


class MergeSortedArraySolution:
    """
    题目3: LeetCode 88. 合并两个有序数组
    来源: LeetCode
    链接: https://leetcode.cn/problems/merge-sorted-array/
    
    题目描述：
    给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，
    分别表示 nums1 和 nums2 中的元素数目。
    请你合并 nums2 到 nums1 中，使合并后的数组同样按非递减顺序排列。
    注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。
    为了应对这种情况，nums1 的初始长度为 m + n，其中前 m 个元素表示应合并的元素，后 n 个元素为 0，应忽略。
    nums2 的长度为 n。
    
    解法分析：
    1. 从后往前合并 (最优解) - 时间复杂度: O(m+n), 空间复杂度: O(1)
    2. 从前往后合并 - 时间复杂度: O(m+n), 空间复杂度: O(m+n)
    3. 合并后排序 - 时间复杂度: O((m+n)log(m+n)), 空间复杂度: O(1)
    
    解题思路：
    从后往前合并可以避免覆盖nums1中未处理的元素。
    使用三个指针分别指向nums1有效元素末尾、nums2末尾和nums1实际末尾。
    比较两个数组当前元素，将较大者放入nums1末尾，移动相应指针。
    """
    
    @staticmethod
    def merge_from_back(nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        解法1: 从后往前合并 (推荐)
        时间复杂度: O(m+n) - 每个元素访问一次
        空间复杂度: O(1) - 原地修改
        
        核心思想：
        1. 从两个数组的末尾开始比较
        2. 将较大元素放到nums1的末尾
        3. 移动相应指针
        4. 处理剩余元素
        """
        # 三个指针
        i = m - 1      # nums1有效元素的末尾
        j = n - 1      # nums2的末尾
        k = m + n - 1  # nums1实际末尾
        
        # 从后往前合并
        while i >= 0 and j >= 0:
            if nums1[i] > nums2[j]:
                nums1[k] = nums1[i]
                i -= 1
            else:
                nums1[k] = nums2[j]
                j -= 1
            k -= 1
        
        # 处理nums2剩余元素
        while j >= 0:
            nums1[k] = nums2[j]
            j -= 1
            k -= 1
        
        # 注意：如果nums1有剩余元素，它们已经在正确位置，无需处理
    
    @staticmethod
    def merge_from_front(nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        解法2: 从前往后合并
        时间复杂度: O(m+n)
        空间复杂度: O(m) - 需要额外数组存储nums1的前m个元素
        """
        # 创建临时数组存储nums1的前m个元素
        nums1_copy = nums1[:m]
        
        # 三个指针
        i = 0  # nums1_copy的指针
        j = 0  # nums2的指针
        k = 0  # nums1的指针
        
        # 从前往后合并
        while i < m and j < n:
            if nums1_copy[i] <= nums2[j]:
                nums1[k] = nums1_copy[i]
                i += 1
            else:
                nums1[k] = nums2[j]
                j += 1
            k += 1
        
        # 处理剩余元素
        while i < m:
            nums1[k] = nums1_copy[i]
            i += 1
            k += 1
        
        while j < n:
            nums1[k] = nums2[j]
            j += 1
            k += 1
    
    @staticmethod
    def merge_and_sort(nums1: List[int], m: int, nums2: List[int], n: int) -> None:
        """
        解法3: 合并后排序
        时间复杂度: O((m+n)log(m+n))
        空间复杂度: O(1)
        """
        # 将nums2复制到nums1的后半部分
        nums1[m:m+n] = nums2
        # 排序
        nums1.sort()
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 合并两个有序数组测试 ===")
        
        # 测试用例1
        nums1 = [1, 2, 3, 0, 0, 0]
        m = 3
        nums2 = [2, 5, 6]
        n = 3
        
        print(f"数组1: {nums1}, m = {m}")
        print(f"数组2: {nums2}, n = {n}")
        
        # 测试从后往前合并
        nums1_copy1 = nums1.copy()
        MergeSortedArraySolution.merge_from_back(nums1_copy1, m, nums2, n)
        print(f"从后往前合并: {nums1_copy1}")
        
        # 测试从前往后合并
        nums1_copy2 = nums1.copy()
        MergeSortedArraySolution.merge_from_front(nums1_copy2, m, nums2, n)
        print(f"从前往后合并: {nums1_copy2}")
        
        # 测试合并后排序
        nums1_copy3 = nums1.copy()
        MergeSortedArraySolution.merge_and_sort(nums1_copy3, m, nums2, n)
        print(f"合并后排序: {nums1_copy3}")
        
        # 测试用例2: nums1为空
        nums3 = [0]
        m2 = 0
        nums4 = [1]
        n2 = 1
        
        print(f"\n数组1: {nums3}, m = {m2}")
        print(f"数组2: {nums4}, n = {n2}")
        
        MergeSortedArraySolution.merge_from_back(nums3, m2, nums4, n2)
        print(f"从后往前合并: {nums3}")
        print()


class SortListSolution:
    """
    题目4: LeetCode 148. 排序链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/sort-list/
    
    题目描述：
    给你链表的头结点 head，请将其按 升序 排列并返回 排序后的链表 。
    要求在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
    """
    
    @staticmethod
    def sort_list_top_down(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法1: 归并排序（自顶向下）
        时间复杂度: O(nlogn) - 归并排序的标准时间复杂度
        空间复杂度: O(logn) - 递归调用栈的深度
        """
        # 基本情况：空链表或只有一个节点
        if not head or not head.next:
            return head
        
        # 使用快慢指针找到中点
        slow, fast = head, head.next
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next
        
        # 分割链表
        mid = slow.next
        slow.next = None
        
        # 递归排序两个子链表
        left = SortListSolution.sort_list_top_down(head)
        right = SortListSolution.sort_list_top_down(mid)
        
        # 合并排序后的链表
        return SortListSolution.merge_two_lists(left, right)
    
    @staticmethod
    def sort_list_bottom_up(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法2: 归并排序（自底向上） - 最优解
        
        🎯 核心思想：
        1. 从步长1开始，每次翻倍
        2. 按当前步长将链表分割成多个子链表并合并
        3. 重复直到步长大于等于链表长度
        
        📊 复杂度分析：
        - 时间复杂度: O(n log n) - 与自顶向下相同
        - 空间复杂度: O(1) - 只使用常数级额外空间
        
        ⚡ 性能特点：
        - 避免递归栈溢出
        - 空间效率高
        - 实现相对复杂
        
        @param head 链表头节点
        @return 排序后的链表
        """
        if head is None or head.next is None:
            return head
        
        # 计算链表长度
        length = 0
        current = head
        while current:
            length += 1
            current = current.next
        
        # 创建哨兵节点
        dummy = ListNode(-1)
        dummy.next = head
        
        # 自底向上进行归并
        step = 1
        while step < length:
            prev = dummy
            current = dummy.next
            
            while current:
                # 第一个子链表的头节点
                left = current
                # 分割第一个子链表
                for i in range(1, step):
                    if current.next is not None:
                        current = current.next
                    else:
                        break
                
                # 第二个子链表的头节点
                right = current.next
                # 断开第一个子链表
                current.next = None
                current = right
                
                # 分割第二个子链表
                for i in range(1, step):
                    if current is not None and current.next is not None:
                        current = current.next
                    else:
                        break
                
                # 记录下一段链表的起始位置
                next_node = None
                if current is not None:
                    next_node = current.next
                    current.next = None
                
                # 合并两个子链表
                prev.next = SortListSolution.merge_two_lists(left, right)
                
                # 移动prev到合并后链表的末尾
                while prev.next is not None:
                    prev = prev.next
                
                # 处理下一段链表
                current = next_node
            
            step *= 2
        
        return dummy.next
    
    @staticmethod
    def merge_two_lists(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
        """
        合并两个有序链表的辅助函数
        """
        dummy = ListNode(-1)
        current = dummy
        
        while l1 and l2:
            if l1.val <= l2.val:
                current.next = l1
                l1 = l1.next
            else:
                current.next = l2
                l2 = l2.next
            current = current.next
        
        current.next = l1 if l1 else l2
        return dummy.next
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 排序链表测试 ===")
        
        # 测试用例1: 正常情况
        list1 = ListNode.create_list([4, 2, 1, 3])
        print("原链表: ", end="")
        ListNode.print_list(list1)
        
        result1 = SortListSolution.sort_list_top_down(list1)
        print("自顶向下归并排序结果: ", end="")
        ListNode.print_list(result1)
        
        # 重新创建测试数据
        list2 = ListNode.create_list([4, 2, 1, 3])
        result2 = SortListSolution.sort_list_bottom_up(list2)
        print("自底向上归并排序结果: ", end="")
        ListNode.print_list(result2)
        
        # 测试用例2: 包含重复元素
        list3 = ListNode.create_list([-1, 5, 3, 4, 0])
        print("\n原链表: ", end="")
        ListNode.print_list(list3)
        
        result3 = SortListSolution.sort_list_bottom_up(list3)
        print("排序结果: ", end="")
        ListNode.print_list(result3)
        print()


class AddTwoNumbersSolution:
    """
    题目5: LeetCode 2. 两数相加
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-two-numbers/
    
    题目描述：
    给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
    请你将两个数相加，并以相同形式返回一个表示和的链表。
    """
    
    @staticmethod
    def add_two_numbers(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法: 模拟加法过程
        时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
        空间复杂度: O(max(m,n)) - 输出链表的长度最多为max(m,n)+1
        """
        # 创建哨兵节点
        dummy = ListNode(-1)
        current = dummy
        
        # 进位
        carry = 0
        
        # 同时遍历两个链表
        while l1 or l2 or carry > 0:
            # 计算当前位的和
            sum_val = carry
            if l1:
                sum_val += l1.val
                l1 = l1.next
            if l2:
                sum_val += l2.val
                l2 = l2.next
            
            # 更新进位
            carry = sum_val // 10
            # 创建新节点存储当前位的结果
            current.next = ListNode(sum_val % 10)
            current = current.next
        
        return dummy.next
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 两数相加测试 ===")
        
        # 测试用例1: 正常情况
        l1 = ListNode.create_list([2, 4, 3])  # 342
        l2 = ListNode.create_list([5, 6, 4])  # 465
        print("链表1 (342逆序): ", end="")
        ListNode.print_list(l1)
        print("链表2 (465逆序): ", end="")
        ListNode.print_list(l2)
        
        result1 = AddTwoNumbersSolution.add_two_numbers(l1, l2)
        print("结果 (807逆序): ", end="")
        ListNode.print_list(result1)
        
        # 测试用例2: 包含进位
        l3 = ListNode.create_list([9, 9, 9, 9, 9, 9, 9])
        l4 = ListNode.create_list([9, 9, 9, 9])
        print("\n链表1: ", end="")
        ListNode.print_list(l3)
        print("链表2: ", end="")
        ListNode.print_list(l4)
        
        result2 = AddTwoNumbersSolution.add_two_numbers(l3, l4)
        print("结果: ", end="")
        ListNode.print_list(result2)
        print()


class SwapNodesInPairsSolution:
    """
    题目6: LeetCode 24. 两两交换链表中的节点
    来源: LeetCode
    链接: https://leetcode.cn/problems/swap-nodes-in-pairs/
    
    题目描述：
    给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。
    你必须在不修改节点内部值的情况下完成本题（即，只能进行节点交换）。
    """
    
    @staticmethod
    def swap_pairs_iterative(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法1: 迭代法 (推荐)
        时间复杂度: O(n) - 每个节点只访问一次
        空间复杂度: O(1) - 只使用常数级额外空间
        """
        # 创建哨兵节点
        dummy = ListNode(-1)
        dummy.next = head
        
        prev = dummy
        
        # 确保有至少两个节点可以交换
        while prev.next and prev.next.next:
            # 标记需要交换的两个节点
            first = prev.next
            second = prev.next.next
            
            # 交换节点
            first.next = second.next
            second.next = first
            prev.next = second
            
            # 移动prev到下一对的前一个位置
            prev = first
        
        return dummy.next
    
    @staticmethod
    def swap_pairs_recursive(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法2: 递归法
        时间复杂度: O(n) - 每个节点只访问一次
        空间复杂度: O(n) - 递归调用栈的深度
        """
        # 递归终止条件
        if not head or not head.next:
            return head
        
        # 标记需要交换的两个节点
        first = head
        second = head.next
        
        # 交换节点
        first.next = SwapNodesInPairsSolution.swap_pairs_recursive(second.next)
        second.next = first
        
        # 返回新的头节点
        return second
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 两两交换链表中的节点测试 ===")
        
        # 测试用例1: 偶数个节点
        list1 = ListNode.create_list([1, 2, 3, 4])
        print("原链表: ", end="")
        ListNode.print_list(list1)
        
        result1 = SwapNodesInPairsSolution.swap_pairs_iterative(list1)
        print("迭代法结果: ", end="")
        ListNode.print_list(result1)
        
        # 重新创建测试数据
        list2 = ListNode.create_list([1, 2, 3, 4])
        result2 = SwapNodesInPairsSolution.swap_pairs_recursive(list2)
        print("递归法结果: ", end="")
        ListNode.print_list(result2)
        
        # 测试用例2: 奇数个节点
        list3 = ListNode.create_list([1, 2, 3])
        print("\n原链表: ", end="")
        ListNode.print_list(list3)
        
        result3 = SwapNodesInPairsSolution.swap_pairs_iterative(list3)
        print("交换结果: ", end="")
        ListNode.print_list(result3)
        print()


class NowCoderMergeSortedListsSolution:
    """
    题目7: 牛客 NC33. 合并两个排序的链表
    来源: 牛客网
    链接: https://www.nowcoder.com/practice/d8b6b4358f774294a89de2a6ac4d9337
    """
    
    @staticmethod
    def merge(pHead1: Optional[ListNode], pHead2: Optional[ListNode]) -> Optional[ListNode]:
        """
        合并两个排序的链表
        """
        dummy = ListNode(-1)
        current = dummy
        
        while pHead1 and pHead2:
            if pHead1.val <= pHead2.val:
                current.next = pHead1
                pHead1 = pHead1.next
            else:
                current.next = pHead2
                pHead2 = pHead2.next
            current = current.next
        
        current.next = pHead1 if pHead1 else pHead2
        return dummy.next
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== 牛客 NC33. 合并两个排序的链表测试 ===")
        
        list1 = ListNode.create_list([1, 3, 5])
        list2 = ListNode.create_list([2, 4, 6])
        print("链表1: ", end="")
        ListNode.print_list(list1)
        print("链表2: ", end="")
        ListNode.print_list(list2)
        
        result = NowCoderMergeSortedListsSolution.merge(list1, list2)
        print("合并结果: ", end="")
        ListNode.print_list(result)
        print()


class LintCodeMergeKListsSolution:
    """
    题目8: LintCode 104. 合并k个排序链表
    来源: LintCode
    链接: https://www.lintcode.com/problem/104/
    """
    
    @staticmethod
    def merge_k_lists(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        """
        使用优先队列合并k个排序链表
        """
        # 优先队列的自定义比较需要用到一个包装类
        # 或者使用元组(值, 索引, 节点)来避免比较节点对象
        dummy = ListNode(-1)
        current = dummy
        
        # 优先队列
        heap = []
        
        # 将所有非空链表的头节点加入优先队列
        for i, node in enumerate(lists):
            if node:
                # 使用值和索引作为比较的键，避免节点比较
                heapq.heappush(heap, (node.val, i, node))
        
        # 从优先队列中依次取出最小节点
        while heap:
            val, i, node = heapq.heappop(heap)
            current.next = node
            current = current.next
            
            if node.next:
                heapq.heappush(heap, (node.next.val, i, node.next))
        
        return dummy.next
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LintCode 104. 合并k个排序链表测试 ===")
        
        l1 = ListNode.create_list([2, 4])
        l2 = ListNode.create_list([1, 3, 5])
        l3 = ListNode.create_list([6, 7])
        lists = [l1, l2, l3]
        
        print("链表1: ", end="")
        ListNode.print_list(lists[0])
        print("链表2: ", end="")
        ListNode.print_list(lists[1])
        print("链表3: ", end="")
        ListNode.print_list(lists[2])
        
        result = LintCodeMergeKListsSolution.merge_k_lists(lists)
        print("合并结果: ", end="")
        ListNode.print_list(result)
        print()


class PartitionListSolution:
    """
    题目9: LeetCode 86. 分隔链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/partition-list/
    """
    
    @staticmethod
    def partition(head: Optional[ListNode], x: int) -> Optional[ListNode]:
        less_head = ListNode(-1)
        greater_head = ListNode(-1)
        less = less_head
        greater = greater_head
        
        while head:
            if head.val < x:
                less.next = head
                less = less.next
            else:
                greater.next = head
                greater = greater.next
            head = head.next
        
        greater.next = None
        less.next = greater_head.next
        
        return less_head.next
    
    @staticmethod
    def test():
        print("=== LeetCode 86. 分隔链表测试 ===")
        list_node = ListNode.create_list([1, 4, 3, 2, 5, 2])
        print("原链表: ", end="")
        ListNode.print_list(list_node)
        result = PartitionListSolution.partition(list_node, 3)
        print("分隔后(x=3): ", end="")
        ListNode.print_list(result)
        print()


class LinkedListCycleSolution:
    """
    题目10: LeetCode 141. 环形链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/linked-list-cycle/
    
    题目描述：
    给你一个链表的头节点 head ，判断链表中是否有环。
    如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
    
    解法分析：
    1. 快慢指针法 (Floyd 判圈算法) - 时间复杂度: O(n), 空间复杂度: O(1)
    
    解题思路：
    使用两个指针，一个快指针和一个慢指针。快指针每次移动两步，慢指针每次移动一步。
    如果链表中存在环，快指针最终会追上慢指针；如果不存在环，快指针会先到达链表末尾。
    """
    
    @staticmethod
    def has_cycle(head: Optional[ListNode]) -> bool:
        """
        解法: 快慢指针法 (Floyd 判圈算法)
        
        🎯 核心思想：
        1. 初始化快慢指针都指向头节点
        2. 快指针每次移动两步，慢指针每次移动一步
        3. 如果存在环，快指针会追上慢指针
        4. 如果不存在环，快指针会先到达链表末尾
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 最多遍历链表两次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        ⚡ 性能特点：
        - 最优时间复杂度
        - 最优空间复杂度
        - 适合大规模数据
        
        @param head 链表头节点
        @return 是否存在环
        """
        # 边界条件检查
        if head is None or head.next is None:
            return False
            
        # 初始化快慢指针
        slow = head
        fast = head
        
        # 移动指针
        while fast is not None and fast.next is not None:
            slow = slow.next        # 慢指针每次移动一步
            fast = fast.next.next   # 快指针每次移动两步
            
            # 如果快慢指针相遇，说明存在环
            if slow == fast:
                return True
                
        # 如果快指针到达链表末尾，说明不存在环
        return False
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 141. 环形链表测试 ===")
        
        # 测试用例1: 无环链表
        print("测试用例1: 无环链表")
        list1 = ListNode.create_list([1, 2, 3, 4])
        print("链表: ", end="")
        ListNode.print_list(list1)
        print(f"是否有环: {LinkedListCycleSolution.has_cycle(list1)}")
        
        # 测试用例2: 有环链表 (构造环)
        print("测试用例2: 有环链表")
        list2 = ListNode.create_list([1, 2, 3, 4])
        # 构造环: 将尾节点指向第二个节点
        cur = list2
        while cur.next is not None:
            cur = cur.next
        cur.next = list2.next  # 尾节点指向第二个节点
        print("链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)")
        print(f"是否有环: {LinkedListCycleSolution.has_cycle(list2)}")
        
        # 测试用例3: 单节点无环
        print("测试用例3: 单节点无环")
        list3 = ListNode(1)
        print("链表: 1")
        print(f"是否有环: {LinkedListCycleSolution.has_cycle(list3)}")
        
        # 测试用例4: 空链表
        print("测试用例4: 空链表")
        list4 = None
        print("链表: None")
        print(f"是否有环: {LinkedListCycleSolution.has_cycle(list4)}")
        
        print("所有测试用例执行完成")
        print("=======================================================")


class LinkedListCycleIISolution:
    """
    题目11: LeetCode 142. 环形链表 II
    来源: LeetCode
    链接: https://leetcode.cn/problems/linked-list-cycle-ii/
    
    题目描述：
    给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null 。
    
    解法分析：
    1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
    
    解题思路：
    使用快慢指针找到环后，将快指针重新指向头节点，然后快慢指针都每次移动一步，
    当它们再次相遇时，相遇点就是环的入口节点。
    """
    
    @staticmethod
    def detect_cycle(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法: 快慢指针法
        
        🎯 核心思想：
        1. 使用快慢指针找到环
        2. 将快指针重新指向头节点
        3. 快慢指针都每次移动一步
        4. 再次相遇点就是环的入口
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 最多遍历链表三次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        @param head 链表头节点
        @return 环的入口节点，如果无环则返回None
        """
        # 边界条件检查
        if head is None or head.next is None:
            return None
            
        # 第一阶段：使用快慢指针判断是否有环
        slow = head
        fast = head
        
        while fast is not None and fast.next is not None:
            slow = slow.next
            fast = fast.next.next
            
            # 如果快慢指针相遇，说明存在环
            if slow == fast:
                break
                
        # 如果没有环，返回None
        if fast is None or fast.next is None:
            return None
            
        # 第二阶段：找到环的入口
        # 将快指针重新指向头节点
        fast = head
        # 快慢指针都每次移动一步，直到相遇
        while slow != fast:
            slow = slow.next
            fast = fast.next
            
        # 相遇点就是环的入口
        return slow
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 142. 环形链表 II测试 ===")
        
        # 测试用例1: 无环链表
        print("测试用例1: 无环链表")
        list1 = ListNode.create_list([1, 2, 3, 4])
        print("链表: ", end="")
        ListNode.print_list(list1)
        cycle_start1 = LinkedListCycleIISolution.detect_cycle(list1)
        print(f"环的入口: {cycle_start1.val if cycle_start1 else 'null'}")
        
        # 测试用例2: 有环链表 (构造环)
        print("测试用例2: 有环链表")
        list2 = ListNode.create_list([1, 2, 3, 4])
        # 构造环: 将尾节点指向第二个节点
        cur = list2
        while cur.next is not None:
            cur = cur.next
        cur.next = list2.next  # 尾节点指向第二个节点
        print("链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)")
        cycle_start2 = LinkedListCycleIISolution.detect_cycle(list2)
        print(f"环的入口: {cycle_start2.val if cycle_start2 else 'null'}")
        
        print("所有测试用例执行完成")
        print("=======================================================")


class IntersectionOfTwoLinkedListsSolution:
    """
    题目12: LeetCode 160. 相交链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/intersection-of-two-linked-lists/
    
    题目描述：
    给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。
    
    解法分析：
    1. 双指针法 - 时间复杂度: O(m+n), 空间复杂度: O(1)
    
    解题思路：
    使用两个指针分别遍历两个链表，当一个指针到达链表末尾时，将其指向另一个链表的头节点。
    如果两个链表相交，两个指针会在相交节点相遇；如果不相交，两个指针会同时到达链表末尾。
    """
    
    @staticmethod
    def get_intersection_node(headA: Optional[ListNode], headB: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法: 双指针法
        
        🎯 核心思想：
        1. 使用两个指针分别遍历两个链表
        2. 当指针到达链表末尾时，将其指向另一个链表的头节点
        3. 如果两个链表相交，两个指针会在相交节点相遇
        4. 如果不相交，两个指针会同时到达链表末尾
        
        📊 复杂度分析：
        - 时间复杂度: O(m+n) - 最多遍历两个链表各两次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        @param headA 链表A的头节点
        @param headB 链表B的头节点
        @return 相交节点，如果不相交则返回None
        """
        # 边界条件检查
        if headA is None or headB is None:
            return None
            
        # 初始化两个指针
        pointer_a = headA
        pointer_b = headB
        
        # 当两个指针不相等时继续遍历
        while pointer_a != pointer_b:
            # 当指针到达链表末尾时，将其指向另一个链表的头节点
            pointer_a = headB if pointer_a is None else pointer_a.next
            pointer_b = headA if pointer_b is None else pointer_b.next
            
        # 返回相交节点或None
        return pointer_a
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 160. 相交链表测试 ===")
        
        # 测试用例1: 相交链表
        print("测试用例1: 相交链表")
        common = ListNode.create_list([8, 4, 5])
        list_a = ListNode.create_list([4, 1])
        list_b = ListNode.create_list([5, 6, 1])
        
        # 构造相交链表
        cur_a = list_a
        while cur_a.next is not None:
            cur_a = cur_a.next
        cur_a.next = common
        
        cur_b = list_b
        while cur_b.next is not None:
            cur_b = cur_b.next
        cur_b.next = common
        
        print("链表A: 4 -> 1 -> 8 -> 4 -> 5")
        print("链表B: 5 -> 6 -> 1 -> 8 -> 4 -> 5")
        intersection1 = IntersectionOfTwoLinkedListsSolution.get_intersection_node(list_a, list_b)
        print(f"相交节点: {intersection1.val if intersection1 else 'null'}")
        
        # 测试用例2: 不相交链表
        print("测试用例2: 不相交链表")
        list_c = ListNode.create_list([1, 2, 3])
        list_d = ListNode.create_list([4, 5, 6])
        print("链表C: 1 -> 2 -> 3")
        print("链表D: 4 -> 5 -> 6")
        intersection2 = IntersectionOfTwoLinkedListsSolution.get_intersection_node(list_c, list_d)
        print(f"相交节点: {intersection2.val if intersection2 else 'null'}")
        
        print("所有测试用例执行完成")
        print("=======================================================")


class ReverseLinkedListSolution:
    """
    题目13: LeetCode 206. 反转链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/reverse-linked-list/
    
    题目描述：
    给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
    
    解法分析：
    1. 迭代法 - 时间复杂度: O(n), 空间复杂度: O(1)
    2. 递归法 - 时间复杂度: O(n), 空间复杂度: O(n)
    
    解题思路：
    迭代法：使用三个指针分别指向前一个节点、当前节点和下一个节点，逐个反转节点的指向。
    递归法：递归到链表末尾，然后在回溯过程中反转节点的指向。
    """
    
    @staticmethod
    def reverse_list_iterative(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法1: 迭代法 (推荐)
        
        🎯 核心思想：
        1. 使用三个指针：prev(前一个节点)、current(当前节点)、next(下一个节点)
        2. 逐个反转节点的指向
        3. 移动指针继续处理下一个节点
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 需要遍历链表一次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        ⚡ 性能特点：
        - 最优时间复杂度
        - 最优空间复杂度
        - 适合大规模数据
        
        @param head 链表头节点
        @return 反转后的链表头节点
        """
        # 初始化指针
        prev = None
        current = head
        
        # 遍历链表
        while current is not None:
            # 保存下一个节点
            next_node = current.next
            # 反转当前节点的指向
            current.next = prev
            # 移动指针
            prev = current
            current = next_node
            
        # 返回新的头节点
        return prev
    
    @staticmethod
    def reverse_list_recursive(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        解法2: 递归法
        
        🎯 核心思想：
        1. 递归到链表末尾
        2. 在回溯过程中反转节点的指向
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 需要遍历链表一次
        - 空间复杂度: O(n) - 递归调用栈的深度
        
        ⚡ 性能特点：
        - 代码简洁易懂
        - 空间开销较大
        - 可能栈溢出（大数据量）
        
        @param head 链表头节点
        @return 反转后的链表头节点
        """
        # 递归终止条件
        if head is None or head.next is None:
            return head
            
        # 递归处理下一个节点
        new_head = ReverseLinkedListSolution.reverse_list_recursive(head.next)
        # 反转当前节点和下一个节点的连接
        head.next.next = head
        head.next = None
        
        # 返回新的头节点
        return new_head
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 206. 反转链表测试 ===")
        
        # 测试用例1: 正常链表
        print("测试用例1: 正常链表")
        list1 = ListNode.create_list([1, 2, 3, 4, 5])
        print("原链表: ", end="")
        ListNode.print_list(list1)
        reversed1 = ReverseLinkedListSolution.reverse_list_iterative(list1)
        print("迭代法反转后: ", end="")
        ListNode.print_list(reversed1)
        
        # 重新创建测试数据
        list2 = ListNode.create_list([1, 2, 3, 4, 5])
        reversed2 = ReverseLinkedListSolution.reverse_list_recursive(list2)
        print("递归法反转后: ", end="")
        ListNode.print_list(reversed2)
        
        # 测试用例2: 单节点链表
        print("测试用例2: 单节点链表")
        list3 = ListNode(1)
        print("原链表: ", end="")
        ListNode.print_list(list3)
        reversed3 = ReverseLinkedListSolution.reverse_list_iterative(list3)
        print("反转后: ", end="")
        ListNode.print_list(reversed3)
        
        # 测试用例3: 空链表
        print("测试用例3: 空链表")
        list4 = None
        print("原链表: ", end="")
        ListNode.print_list(list4)
        reversed4 = ReverseLinkedListSolution.reverse_list_iterative(list4)
        print("反转后: ", end="")
        ListNode.print_list(reversed4)
        
        print("所有测试用例执行完成")
        print("=======================================================")


class PalindromeLinkedListSolution:
    """
    题目14: LeetCode 234. 回文链表
    来源: LeetCode
    链接: https://leetcode.cn/problems/palindrome-linked-list/
    
    题目描述：
    给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
    
    解法分析：
    1. 快慢指针 + 反转链表 - 时间复杂度: O(n), 空间复杂度: O(1)
    
    解题思路：
    1. 使用快慢指针找到链表中点
    2. 反转后半部分链表
    3. 比较前半部分和反转后的后半部分
    4. 恢复链表结构(可选)
    """
    
    @staticmethod
    def is_palindrome(head: Optional[ListNode]) -> bool:
        """
        解法: 快慢指针 + 反转链表
        
        🎯 核心思想：
        1. 使用快慢指针找到链表中点
        2. 反转后半部分链表
        3. 比较前半部分和反转后的后半部分
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 需要遍历链表多次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        @param head 链表头节点
        @return 是否为回文链表
        """
        # 边界条件检查
        if head is None or head.next is None:
            return True
            
        # 第一步：使用快慢指针找到链表中点
        slow = head
        fast = head
        
        while fast.next is not None and fast.next.next is not None:
            slow = slow.next
            fast = fast.next.next
            
        # 第二步：反转后半部分链表
        second_half = PalindromeLinkedListSolution._reverse_list(slow.next)
        
        # 第三步：比较前半部分和反转后的后半部分
        first_half = head
        second_half_copy = second_half  # 保存用于恢复
        is_palindrome = True
        
        while second_half is not None:
            if first_half.val != second_half.val:
                is_palindrome = False
                break
            first_half = first_half.next
            second_half = second_half.next
            
        # 第四步：恢复链表结构(可选)
        slow.next = PalindromeLinkedListSolution._reverse_list(second_half_copy)
        
        return is_palindrome
    
    @staticmethod
    def _reverse_list(head: Optional[ListNode]) -> Optional[ListNode]:
        """
        反转链表的辅助函数
        """
        prev = None
        current = head
        
        while current is not None:
            next_node = current.next
            current.next = prev
            prev = current
            current = next_node
            
        return prev
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 234. 回文链表测试 ===")
        
        # 测试用例1: 回文链表
        print("测试用例1: 回文链表")
        list1 = ListNode.create_list([1, 2, 2, 1])
        print("链表: ", end="")
        ListNode.print_list(list1)
        print(f"是否为回文链表: {PalindromeLinkedListSolution.is_palindrome(list1)}")
        
        # 测试用例2: 非回文链表
        print("测试用例2: 非回文链表")
        list2 = ListNode.create_list([1, 2, 3, 4])
        print("链表: ", end="")
        ListNode.print_list(list2)
        print(f"是否为回文链表: {PalindromeLinkedListSolution.is_palindrome(list2)}")
        
        # 测试用例3: 单节点链表
        print("测试用例3: 单节点链表")
        list3 = ListNode(1)
        print("链表: ", end="")
        ListNode.print_list(list3)
        print(f"是否为回文链表: {PalindromeLinkedListSolution.is_palindrome(list3)}")
        
        print("所有测试用例执行完成")
        print("=======================================================")


class RemoveNthNodeFromEndOfListSolution:
    """
    题目15: LeetCode 19. 删除链表的倒数第 N 个结点
    来源: LeetCode
    链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
    
    题目描述：
    给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
    
    解法分析：
    1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
    
    解题思路：
    使用两个指针，快指针先移动n+1步，然后快慢指针同时移动，
    当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点。
    """
    
    @staticmethod
    def remove_nth_from_end(head: Optional[ListNode], n: int) -> Optional[ListNode]:
        """
        解法: 快慢指针法
        
        🎯 核心思想：
        1. 使用哨兵节点简化边界处理
        2. 快指针先移动n+1步
        3. 快慢指针同时移动
        4. 当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点
        
        📊 复杂度分析：
        - 时间复杂度: O(n) - 需要遍历链表一次
        - 空间复杂度: O(1) - 只使用了常数级别的额外空间
        
        @param head 链表头节点
        @param n 倒数第n个节点
        @return 删除节点后的链表头节点
        """
        # 创建哨兵节点，简化边界处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 初始化快慢指针
        fast = dummy
        slow = dummy
        
        # 快指针先移动n+1步
        for i in range(n + 1):
            fast = fast.next
            
        # 快慢指针同时移动
        while fast is not None:
            fast = fast.next
            slow = slow.next
            
        # 删除倒数第n个节点
        slow.next = slow.next.next
        
        # 返回头节点
        return dummy.next
    
    @staticmethod
    def test():
        """
        测试方法
        """
        print("=== LeetCode 19. 删除链表的倒数第 N 个结点测试 ===")
        
        # 测试用例1: 删除中间节点
        print("测试用例1: 删除中间节点")
        list1 = ListNode.create_list([1, 2, 3, 4, 5])
        print("原链表: ", end="")
        ListNode.print_list(list1)
        result1 = RemoveNthNodeFromEndOfListSolution.remove_nth_from_end(list1, 2)
        print("删除倒数第2个节点后: ", end="")
        ListNode.print_list(result1)
        
        # 测试用例2: 删除头节点
        print("测试用例2: 删除头节点")
        list2 = ListNode.create_list([1, 2, 3, 4, 5])
        print("原链表: ", end="")
        ListNode.print_list(list2)
        result2 = RemoveNthNodeFromEndOfListSolution.remove_nth_from_end(list2, 5)
        print("删除倒数第5个节点后: ", end="")
        ListNode.print_list(result2)
        
        # 测试用例3: 删除尾节点
        print("测试用例3: 删除尾节点")
        list3 = ListNode.create_list([1, 2, 3, 4, 5])
        print("原链表: ", end="")
        ListNode.print_list(list3)
        result3 = RemoveNthNodeFromEndOfListSolution.remove_nth_from_end(list3, 1)
        print("删除倒数第1个节点后: ", end="")
        ListNode.print_list(result3)
        
        print("所有测试用例执行完成")
        print("=======================================================")


class AlgorithmSummary:
    """
    算法总结与技巧提升
    """
    
    @staticmethod
    def print_summary():
        """
        打印算法总结
        """
        print("========== 链表合并算法总结 ==========")
        print("1. 核心算法技巧:")
        print("   - 双指针法: 适用于两个有序序列的合并，时间复杂度O(m+n)")
        print("   - 优先队列法: 适用于K个有序序列的合并，时间复杂度O(N*logK)")
        print("   - 分治法: 适用于K个序列的归并，时间复杂度O(N*logK)")
        print("   - 哨兵节点: 简化链表操作的边界处理，提高代码可读性")
        print("   - 原地修改: 避免额外空间开销，适用于数组合并等场景")
        print()
        print("2. 工程化考量:")
        print("   - 异常处理: 处理空链表、单节点链表等边界情况")
        print("   - 内存管理: 在Python中通过垃圾回收自动管理内存")
        print("   - 性能优化: 对于大规模数据，优先队列的常数项优化很重要")
        print("   - 线程安全: 在多线程环境下需要考虑同步问题")
        print()
        print("3. 调试技巧:")
        print("   - 打印中间状态: 使用print跟踪指针移动")
        print("   - 边界测试: 测试空输入、单元素输入、极端值等情况")
        print("   - 断言验证: 使用assert验证关键条件是否满足")
        print()
        print("4. 拓展应用:")
        print("   - 归并排序: 链表排序的最佳选择之一")
        print("   - 多路归并: 外部排序的基础算法")
        print("   - 数据流处理: 实时合并多个有序数据流")
        print("======================================\n")


def run_all_tests():
    """
    综合测试函数
    """
    MergeTwoSortedListsSolution.test()
    MergeKSortedListsSolution.test()
    MergeSortedArraySolution.test()
    SortListSolution.test()
    AddTwoNumbersSolution.test()
    SwapNodesInPairsSolution.test()
    NowCoderMergeSortedListsSolution.test()
    LintCodeMergeKListsSolution.test()
    PartitionListSolution.test()
    
    # 新增题目的测试
    LinkedListCycleSolution.test()
    LinkedListCycleIISolution.test()
    IntersectionOfTwoLinkedListsSolution.test()
    ReverseLinkedListSolution.test()
    PalindromeLinkedListSolution.test()
    RemoveNthNodeFromEndOfListSolution.test()
    
    AlgorithmSummary.print_summary()


if __name__ == "__main__":
    run_all_tests()