# 删除链表的倒数第N个节点
# 测试链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/

from typing import Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 解决方案类
class Solution:
    # 删除链表的倒数第N个节点
    # 方法：双指针法，先让快指针走n步，然后快慢指针同时移动
    # 时间复杂度：O(n) - 需要遍历链表一次
    # 空间复杂度：O(1) - 只使用常数额外空间
    # 参数：
    #   head - 链表的头节点
    #   n - 要删除的倒数第n个节点
    # 返回值：删除节点后的链表头节点
    def removeNthFromEnd(self, head: Optional[ListNode], n: int) -> Optional[ListNode]:
        # 创建虚拟头节点，简化边界情况处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 初始化快慢指针
        fast: ListNode = dummy
        slow: ListNode = dummy
        
        # 快指针先走n步
        for i in range(n):
            if fast.next is not None:
                fast = fast.next
        
        # 快慢指针同时移动，直到快指针到达最后一个节点
        while fast.next is not None:
            fast = fast.next
            if slow.next is not None:
                slow = slow.next
        
        # 删除倒数第n个节点
        if slow.next is not None:
            slow.next = slow.next.next
        
        # 返回新的头节点
        return dummy.next

# 辅助函数：打印链表
def print_list(head: Optional[ListNode]) -> None:
    current = head
    while current is not None:
        print(current.val, end="")
        if current.next is not None:
            print(" -> ", end="")
        current = current.next
    print()

# 辅助函数：构建链表
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
    
    # 测试用例1: [1,2,3,4,5], n = 2
    nums1 = [1, 2, 3, 4, 5]
    head1 = build_list(nums1)
    print("测试用例1 - 原链表: ", end="")
    print_list(head1)
    result1 = solution.removeNthFromEnd(head1, 2)
    print("删除倒数第2个节点后: ", end="")
    if result1 is not None:
        print_list(result1)
    
    # 测试用例2: [1], n = 1
    nums2 = [1]
    head2 = build_list(nums2)
    print("测试用例2 - 原链表: ", end="")
    print_list(head2)
    result2 = solution.removeNthFromEnd(head2, 1)
    print("删除倒数第1个节点后: ", end="")
    if result2 is not None:
        print_list(result2)
    
    # 测试用例3: [1,2], n = 1
    nums3 = [1, 2]
    head3 = build_list(nums3)
    print("测试用例3 - 原链表: ", end="")
    print_list(head3)
    result3 = solution.removeNthFromEnd(head3, 1)
    print("删除倒数第1个节点后: ", end="")
    if result3 is not None:
        print_list(result3)

'''
题目：LeetCode 19. 删除链表的倒数第N个节点
来源：LeetCode、牛客网、剑指Offer等各大算法平台
链接：https://leetcode.cn/problems/remove-nth-node-from-end-of-list/

题目描述：
给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。

解题思路：
使用双指针法，先让快指针走n步，然后快慢指针同时移动，
当快指针到达最后一个节点时，慢指针指向倒数第n+1个节点，
然后删除倒数第n个节点。

时间复杂度：O(n) - 需要遍历链表一次
空间复杂度：O(1) - 只使用常数额外空间
是否最优解：是

工程化考量：
1. 使用虚拟头节点简化边界情况处理
2. 边界情况处理：删除头节点、空链表等
3. 异常处理：输入参数校验

与机器学习等领域的联系：
1. 在序列数据处理中，有时需要删除特定位置的元素
2. 双指针技巧在滑动窗口算法中广泛应用

语言特性差异：
Java: 对象引用操作简单，垃圾回收自动管理内存
C++: 需要手动管理内存，注意指针操作
Python: 使用对象引用，无需手动管理内存

极端输入场景：
1. 空链表
2. 单节点链表
3. 删除头节点
4. 删除尾节点
5. 非常长的链表
'''