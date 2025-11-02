# 合并两个有序链表（进阶版）
# 测试链接：https://leetcode.cn/problems/merge-two-sorted-lists/

# 提交时不要提交这个类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 提交如下的方法
class Solution:
    def mergeTwoLists(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        合并两个有序链表（迭代法）
        
        解题思路：
        1. 使用虚拟头节点简化边界处理
        2. 使用双指针分别遍历两个链表
        3. 比较当前节点值，将较小值连接到结果链表
        4. 连接剩余节点
        
        时间复杂度：O(m+n) - m和n分别是两个链表的长度
        空间复杂度：O(1) - 只使用常数额外空间
        是否最优解：是
        """
        # 创建虚拟头节点
        dummy = ListNode(0)
        current = dummy
        
        # 双指针遍历两个链表
        while list1 is not None and list2 is not None:
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            current = current.next
        
        # 连接剩余节点
        current.next = list1 if list1 is not None else list2
        
        return dummy.next
    
    def mergeTwoListsRecursive(self, list1: ListNode, list2: ListNode) -> ListNode:
        """
        合并两个有序链表（递归法）
        
        解题思路：
        1. 递归处理链表
        2. 比较两个链表头节点的值
        3. 将较小节点作为当前节点，递归合并剩余部分
        
        时间复杂度：O(m+n) - m和n分别是两个链表的长度
        空间复杂度：O(m+n) - 递归调用栈的深度
        是否最优解：不是（空间复杂度较高）
        """
        # 基本情况
        if list1 is None:
            return list2
        if list2 is None:
            return list1
        
        # 递归情况
        if list1.val <= list2.val:
            list1.next = self.mergeTwoListsRecursive(list1.next, list2)
            return list1
        else:
            list2.next = self.mergeTwoListsRecursive(list1, list2.next)
            return list2
    
    def mergeKLists(self, lists: list[ListNode]) -> ListNode:
        """
        合并K个有序链表（分治法）
        
        解题思路：
        1. 使用分治思想，将K个链表两两合并
        2. 每次合并后链表数量减半，直到只剩一个链表
        3. 复用合并两个有序链表的实现
        
        时间复杂度：O(N log K) - N是所有链表节点总数，K是链表数量
        空间复杂度：O(log K) - 递归调用栈的深度
        是否最优解：是
        """
        if not lists:
            return None
        return self._mergeKLists(lists, 0, len(lists) - 1)
    
    def _mergeKLists(self, lists: list[ListNode], left: int, right: int) -> ListNode:
        """
        分治合并K个有序链表
        
        Args:
            lists: 链表列表
            left: 左边界
            right: 右边界
            
        Returns:
            合并后的有序链表
        """
        if left == right:
            return lists[left]
        
        mid = left + (right - left) // 2
        l1 = self._mergeKLists(lists, left, mid)
        l2 = self._mergeKLists(lists, mid + 1, right)
        
        return self.mergeTwoLists(l1, l2)

'''
题目扩展：LeetCode 21. 合并两个有序链表
来源：LeetCode、牛客网、剑指Offer等各大算法平台

题目描述：
将两个升序链表合并为一个新的升序链表并返回。
新链表是通过拼接给定的两个链表的所有节点组成的。

解题思路：
方法一：迭代法（推荐）
1. 使用虚拟头节点简化边界处理
2. 使用双指针分别遍历两个链表
3. 比较当前节点值，将较小值连接到结果链表
4. 连接剩余节点

方法二：递归法
1. 递归处理链表
2. 比较两个链表头节点的值
3. 将较小节点作为当前节点，递归合并剩余部分

时间复杂度：
- 迭代法：O(m+n)
- 递归法：O(m+n)

空间复杂度：
- 迭代法：O(1)
- 递归法：O(m+n)

是否最优解：迭代法是最优解

工程化考量：
1. 边界情况处理：空链表、单链表
2. 异常处理：输入参数校验
3. 代码可读性：清晰的变量命名和注释

与机器学习等领域的联系：
1. 在归并排序中，合并有序序列是核心操作
2. 在外部排序中，需要合并多个有序文件
3. 在数据库系统中，合并有序结果集

语言特性差异：
Java: 垃圾回收自动管理内存
C++: 需要手动管理内存，注意避免内存泄漏
Python: 语法简洁，支持函数式编程

极端输入场景：
1. 两个空链表
2. 一个空链表，另一个非空
3. 两个单节点链表
4. 链表长度差异很大
5. 已排序的链表

反直觉但关键的设计：
1. 虚拟头节点：简化边界处理
2. 双指针遍历：同时处理两个链表
3. 剩余节点连接：避免重复比较

工程选择依据：
1. 可维护性：代码结构清晰，易于理解和修改
2. 性能：时间复杂度最优，空间复杂度常数级
3. 鲁棒性：处理各种边界情况

异常防御：
1. 空指针检查
2. 链表长度检查
3. 排序验证

单元测试要点：
1. 测试两个空链表
2. 测试一个空链表
3. 测试两个单节点链表
4. 测试链表长度不同的情况
5. 测试已排序的链表

性能优化策略：
1. 一次遍历完成合并
2. 原地操作，不创建新节点
3. 使用虚拟头节点避免特殊判断

算法安全与业务适配：
1. 避免内存泄漏：正确处理节点引用
2. 异常捕获：捕获可能的运行时异常
3. 处理溢出：处理大链表情况

与标准库实现的对比：
1. 标准库通常不提供链表合并功能
2. 需要自定义实现特定需求
3. 边界处理更加细致

笔试解题效率：
1. 模板化：掌握链表合并的通用模板
2. 边界处理：熟练处理各种边界情况
3. 代码简洁：使用虚拟头节点简化代码

面试深度表达：
1. 解释设计思路：为什么使用虚拟头节点
2. 分析复杂度：时间和空间复杂度分析
3. 讨论变种：递归法 vs 迭代法
4. 实际应用：合并操作在工程中的应用
'''