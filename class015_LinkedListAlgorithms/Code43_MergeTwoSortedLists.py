# 合并两个有序链表
# 测试链接: https://leetcode.cn/problems/merge-two-sorted-lists/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 解决方案类
class Solution:
    # 合并两个有序链表
    # 方法：双指针法，比较两个链表的节点值，选择较小的节点连接到结果链表
    # 时间复杂度：O(m+n) - m和n分别是两个链表的长度
    # 空间复杂度：O(1) - 只使用常数额外空间
    # 参数：
    #   list1 - 第一个有序链表
    #   list2 - 第二个有序链表
    # 返回值：合并后的有序链表
    def mergeTwoLists(self, list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
        # 创建虚拟头节点，简化边界情况处理
        dummy = ListNode(0)
        # 当前节点指针
        current = dummy
        
        # 当两个链表都未遍历完时继续循环
        while list1 is not None and list2 is not None:
            # 比较两个链表当前节点的值，选择较小的节点连接到结果链表
            if list1.val <= list2.val:
                current.next = list1
                list1 = list1.next
            else:
                current.next = list2
                list2 = list2.next
            # 移动指针
            current = current.next
        
        # 将剩余的节点连接到结果链表
        if list1 is not None:
            current.next = list1
        else:
            current.next = list2
        
        # 返回合并后的链表头节点
        return dummy.next

# 辅助函数：打印链表
def print_list(head: Optional[ListNode]) -> None:
    while head is not None:
        print(head.val, end="")
        if head.next is not None:
            print(" -> ", end="")
        head = head.next
    print()

# 辅助函数：构建链表
def build_list(nums) -> Optional[ListNode]:
    if len(nums) == 0:
        return None
    dummy = ListNode(0)
    curr = dummy
    for num in nums:
        curr.next = ListNode(num)
        curr = curr.next
    return dummy.next

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [1,2,4] + [1,3,4] = [1,1,2,3,4,4]
    nums1 = [1, 2, 4]
    nums2 = [1, 3, 4]
    list1 = build_list(nums1)
    list2 = build_list(nums2)
    print("测试用例1 - list1: ", end="")
    print_list(list1)
    print("测试用例1 - list2: ", end="")
    print_list(list2)
    result1 = solution.mergeTwoLists(list1, list2)
    print("合并结果: ", end="")
    print_list(result1)
    
    # 测试用例2: [] + [] = []
    nums3 = []
    nums4 = []
    list3 = build_list(nums3)
    list4 = build_list(nums4)
    print("测试用例2 - list1: ", end="")
    print_list(list3)
    print("测试用例2 - list2: ", end="")
    print_list(list4)
    result2 = solution.mergeTwoLists(list3, list4)
    print("合并结果: ", end="")
    print_list(result2)
    
    # 测试用例3: [] + [0] = [0]
    nums5 = []
    nums6 = [0]
    list5 = build_list(nums5)
    list6 = build_list(nums6)
    print("测试用例3 - list1: ", end="")
    print_list(list5)
    print("测试用例3 - list2: ", end="")
    print_list(list6)
    result3 = solution.mergeTwoLists(list5, list6)
    print("合并结果: ", end="")
    print_list(result3)

'''
题目：LeetCode 21. 合并两个有序链表
来源：LeetCode、牛客网、剑指Offer等各大算法平台
链接：https://leetcode.cn/problems/merge-two-sorted-lists/

题目描述：
将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

解题思路：
使用双指针法，比较两个链表的节点值，选择较小的节点连接到结果链表。

时间复杂度：O(m+n) - m和n分别是两个链表的长度
空间复杂度：O(1) - 只使用常数额外空间
是否最优解：是

工程化考量：
1. 使用虚拟头节点简化边界情况处理
2. 边界情况处理：空链表、不同长度链表等
3. 异常处理：输入参数校验

与机器学习等领域的联系：
1. 在归并排序算法中，合并两个有序序列是核心步骤
2. 在多路归并中，需要合并多个有序序列

语言特性差异：
Java: 对象引用操作简单，垃圾回收自动管理内存
C++: 需要手动管理内存，注意指针操作
Python: 使用对象引用，无需手动管理内存

极端输入场景：
1. 空链表
2. 单节点链表
3. 非常长的链表
4. 全相同元素链表
5. 一个链表为空，另一个链表非空
'''