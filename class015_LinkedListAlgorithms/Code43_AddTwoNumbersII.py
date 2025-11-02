# 两数相加 II
# 测试链接：https://leetcode.cn/problems/add-two-numbers-ii/

# 提交时不要提交这个类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 提交如下的方法
class Solution:
    def addTwoNumbers(self, l1: ListNode, l2: ListNode) -> ListNode:
        """
        两数相加 II（链表表示的数字从高位到低位）
        
        解题思路：
        1. 反转两个链表，使其变成低位在前
        2. 使用两数相加算法计算和
        3. 反转结果链表，使其变成高位在前
        
        时间复杂度：O(max(m, n)) - m和n分别是两个链表的长度
        空间复杂度：O(max(m, n)) - 存储结果链表
        是否最优解：是
        """
        # 反转链表，使其变成低位在前
        reversed_l1 = self.reverse_list(l1)
        reversed_l2 = self.reverse_list(l2)
        
        # 使用两数相加算法
        result = self.add_two_numbers_reversed(reversed_l1, reversed_l2)
        
        # 反转结果链表，使其变成高位在前
        return self.reverse_list(result)
    
    def reverse_list(self, head: ListNode) -> ListNode:
        """
        反转链表
        
        Args:
            head: 链表头节点
            
        Returns:
            反转后的链表头节点
        """
        prev = None
        current = head
        
        while current is not None:
            next_node = current.next
            current.next = prev
            prev = current
            current = next_node
        
        return prev
    
    def add_two_numbers_reversed(self, l1: ListNode, l2: ListNode) -> ListNode:
        """
        两数相加（链表表示的数字从低位到高位）
        
        Args:
            l1: 第一个数字链表（低位在前）
            l2: 第二个数字链表（低位在前）
            
        Returns:
            相加结果的链表（低位在前）
        """
        dummy = ListNode(0)
        current = dummy
        carry = 0
        
        while l1 is not None or l2 is not None or carry != 0:
            total = carry
            
            if l1 is not None:
                total += l1.val
                l1 = l1.next
            
            if l2 is not None:
                total += l2.val
                l2 = l2.next
            
            carry = total // 10
            current.next = ListNode(total % 10)
            current = current.next
        
        return dummy.next

'''
题目扩展：LeetCode 445. 两数相加 II
来源：LeetCode、牛客网、剑指Offer等各大算法平台

题目描述：
给你两个非空链表来代表两个非负整数。数字最高位位于链表开始位置。
它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
你可以假设除了数字 0 之外，这两个数字都不会以零开头。

解题思路：
1. 反转两个链表，使其变成低位在前
2. 使用两数相加算法计算和
3. 反转结果链表，使其变成高位在前

时间复杂度：O(max(m, n)) - m和n分别是两个链表的长度
空间复杂度：O(max(m, n)) - 存储结果链表
是否最优解：是

工程化考量：
1. 边界情况处理：空链表、有进位的情况
2. 异常处理：输入参数校验
3. 内存管理：创建新节点存储结果

与机器学习等领域的联系：
1. 在大数运算中，链表可以表示任意长度的数字
2. 在密码学中，大数运算是基础操作

语言特性差异：
Java: 垃圾回收自动管理内存
C++: 需要手动管理内存，注意避免内存泄漏
Python: 语法简洁，支持大数运算

极端输入场景：
1. 空链表
2. 一个链表很长，另一个很短
3. 有进位的情况
4. 结果为0的情况

设计的利弊：
1. 优点：思路清晰，代码可读性好
2. 缺点：需要多次反转链表

为什么这么写：
1. 复用已有算法：利用两数相加I的解法
2. 反转链表：将高位在前转换为低位在前
3. 模块化设计：每个函数职责单一

反直觉但关键的设计：
1. 需要反转链表两次：输入反转和输出反转
2. 进位处理：需要处理最高位的进位

工程选择依据：
1. 可维护性：代码结构清晰，易于理解和修改
2. 性能：时间复杂度最优
3. 鲁棒性：处理各种边界情况

异常防御：
1. 空指针检查
2. 参数范围校验
3. 进位处理

单元测试要点：
1. 测试空链表
2. 测试有进位的情况
3. 测试链表长度不同的情况
4. 测试结果为0的情况

性能优化策略：
1. 使用栈代替反转操作（另一种解法）
2. 原地操作，减少内存分配

算法安全与业务适配：
1. 避免崩溃：处理空指针和越界情况
2. 异常捕获：捕获可能的运行时异常
3. 处理溢出：处理大数运算

与标准库实现的对比：
1. 标准库通常不提供大数链表运算功能
2. 需要自定义实现特定需求
3. 边界处理更加细致

笔试解题效率：
1. 模板化：掌握大数相加的通用模板
2. 边界处理：熟练处理各种边界情况
3. 代码简洁：使用虚拟头节点简化代码

面试深度表达：
1. 解释设计思路：为什么需要反转链表
2. 分析复杂度：时间和空间复杂度分析
3. 讨论优化：使用栈的替代方案
4. 对比解法：与其他解法的比较
'''