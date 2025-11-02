# 两数相加
# 测试链接: https://leetcode.cn/problems/add-two-numbers/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 解决方案类
class Solution:
    # 两数相加
    # 方法：模拟加法运算，从低位到高位逐位相加
    # 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
    # 空间复杂度：O(1) - 只使用常数额外空间（不计算结果链表）
    # 参数：
    #   l1 - 第一个数的链表表示（低位在前）
    #   l2 - 第二个数的链表表示（低位在前）
    # 返回值：两数之和的链表表示
    def addTwoNumbers(self, l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
        # 创建虚拟头节点，简化边界情况处理
        dummy = ListNode(0)
        # 当前节点指针
        current = dummy
        # 进位值
        carry = 0
        
        # 当两个链表都未遍历完或还有进位时继续循环
        while l1 is not None or l2 is not None or carry != 0:
            # 获取当前位的值
            val1 = l1.val if l1 is not None else 0
            val2 = l2.val if l2 is not None else 0
            
            # 计算当前位的和
            sum_val = val1 + val2 + carry
            # 更新进位值
            carry = sum_val // 10
            # 创建新节点存储当前位的结果
            current.next = ListNode(sum_val % 10)
            # 移动指针
            current = current.next
            
            # 移动到下一个节点
            if l1 is not None:
                l1 = l1.next
            if l2 is not None:
                l2 = l2.next
        
        # 返回结果链表的头节点
        return dummy.next

# 辅助函数：打印链表
def print_list(head: Optional[ListNode]) -> None:
    while head is not None:
        print(head.val, end="")
        if head.next is not None:
            print(" -> ", end="")
        head = head.next
    print()

# 辅助函数：构建链表（数字低位在前）
def build_list(nums) -> Optional[ListNode]:
    dummy = ListNode(0)
    curr = dummy
    for num in nums:
        curr.next = ListNode(num)
        curr = curr.next
    return dummy.next

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 342 + 465 = 807
    # l1: 2 -> 4 -> 3 (表示342)
    # l2: 5 -> 6 -> 4 (表示465)
    # 结果: 7 -> 0 -> 8 (表示807)
    nums1 = [2, 4, 3]
    nums2 = [5, 6, 4]
    l1 = build_list(nums1)
    l2 = build_list(nums2)
    print("测试用例1 - l1: ", end="")
    print_list(l1)
    print("测试用例1 - l2: ", end="")
    print_list(l2)
    result1 = solution.addTwoNumbers(l1, l2)
    print("两数相加结果: ", end="")
    print_list(result1)
    
    # 测试用例2: 0 + 0 = 0
    nums3 = [0]
    nums4 = [0]
    l3 = build_list(nums3)
    l4 = build_list(nums4)
    print("测试用例2 - l1: ", end="")
    print_list(l3)
    print("测试用例2 - l2: ", end="")
    print_list(l4)
    result2 = solution.addTwoNumbers(l3, l4)
    print("两数相加结果: ", end="")
    print_list(result2)
    
    # 测试用例3: 999 + 9999 = 10998
    # l1: 9 -> 9 -> 9 (表示999)
    # l2: 9 -> 9 -> 9 -> 9 (表示9999)
    # 结果: 8 -> 9 -> 9 -> 0 -> 1 (表示10998)
    nums5 = [9, 9, 9]
    nums6 = [9, 9, 9, 9]
    l5 = build_list(nums5)
    l6 = build_list(nums6)
    print("测试用例3 - l1: ", end="")
    print_list(l5)
    print("测试用例3 - l2: ", end="")
    print_list(l6)
    result3 = solution.addTwoNumbers(l5, l6)
    print("两数相加结果: ", end="")
    print_list(result3)

'''
题目：LeetCode 2. 两数相加
来源：LeetCode、牛客网、剑指Offer等各大算法平台
链接：https://leetcode.cn/problems/add-two-numbers/

题目描述：
给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，
并且每个节点只能存储一位数字。请你将两个数相加，并以相同形式返回一个表示和的链表。

解题思路：
模拟加法运算过程，从低位到高位逐位相加，处理进位。

时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
空间复杂度：O(1) - 只使用常数额外空间（不计算结果链表）
是否最优解：是

工程化考量：
1. 使用虚拟头节点简化边界情况处理
2. 边界情况处理：不同长度链表、进位处理等
3. 异常处理：输入参数校验

与机器学习等领域的联系：
1. 在大整数运算中，链表可以用来表示超长整数
2. 模拟运算过程在算法设计中很常见

语言特性差异：
Java: 对象引用操作简单，垃圾回收自动管理内存
C++: 需要手动管理内存，注意指针操作
Python: 使用对象引用，无需手动管理内存

极端输入场景：
1. 空链表
2. 单节点链表
3. 非常长的链表
4. 全0链表
5. 全9链表（产生连续进位）
'''