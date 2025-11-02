# 两两交换链表中的节点 - LeetCode 24
# 测试链接: https://leetcode.cn/problems/swap-nodes-in-pairs/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法（使用哑节点）
    def swapPairs(self, head: ListNode) -> ListNode:
        """
        两两交换链表中的节点
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 前驱节点，初始指向哑节点
        prev = dummy
        
        # 当还有至少两个节点需要交换时
        while prev.next and prev.next.next:
            # 定义需要交换的两个节点
            first = prev.next      # 第一个节点
            second = prev.next.next  # 第二个节点
            
            # 交换节点
            first.next = second.next  # 第一个节点指向下一对的第一个节点
            second.next = first       # 第二个节点指向第一个节点
            prev.next = second        # 前驱节点指向第二个节点（新的第一个节点）
            
            # 移动前驱节点到下一对的前一个位置
            prev = first
        
        return dummy.next
    
    # 方法2: 递归法
    def swapPairsRecursive(self, head: ListNode) -> ListNode:
        """
        递归实现两两交换链表中的节点
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：空链表或只有一个节点
        if not head or not head.next:
            return head
        
        # 定义需要交换的两个节点
        first = head
        second = head.next
        
        # 交换节点：第一个节点指向下一对交换后的结果
        first.next = self.swapPairsRecursive(second.next)
        # 第二个节点指向第一个节点
        second.next = first
        
        # 返回新的头节点（原第二个节点）
        return second
    
    # 方法3: 迭代法（不使用哑节点）
    def swapPairsNoDummy(self, head: ListNode) -> ListNode:
        """
        不使用哑节点的迭代实现
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 特殊情况处理：空链表或只有一个节点
        if not head or not head.next:
            return head
        
        # 新的头节点是原链表的第二个节点
        new_head = head.next
        
        # 前驱节点
        prev = None
        current = head
        
        while current and current.next:
            # 定义需要交换的两个节点
            first = current
            second = current.next
            
            # 交换节点
            first.next = second.next
            second.next = first
            
            # 连接前一对交换后的结果
            if prev:
                prev.next = second
            
            # 更新前驱节点和当前节点
            prev = first
            current = first.next
        
        return new_head
    
    # 方法4: 值交换法（不交换节点指针，只交换节点值）
    def swapPairsValues(self, head: ListNode) -> ListNode:
        """
        通过交换节点值而不是指针来实现两两交换
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        current = head
        
        while current and current.next:
            # 交换节点值
            current.val, current.next.val = current.next.val, current.val
            # 移动两步
            current = current.next.next
        
        return head

# 辅助函数：构建链表
# nums: 链表节点的值列表
# return: 链表头节点
def build_list(nums):
    if not nums:
        return None
    
    head = ListNode(nums[0])
    current = head
    
    for num in nums[1:]:
        current.next = ListNode(num)
        current = current.next
    
    return head

# 辅助函数：打印链表
# head: 链表头节点
def print_list(head):
    values = []
    current = head
    
    while current:
        values.append(str(current.val))
        current = current.next
    
    print(" -> ".join(values) if values else "空链表")

# 主函数用于测试
def main():
    solution = Solution()
    
    # 测试用例1: [1,2,3,4]
    nums1 = [1, 2, 3, 4]
    head1 = build_list(nums1)
    print("测试用例1:")
    print("原始链表: ")
    print_list(head1)
    
    # 测试迭代法
    result1 = solution.swapPairs(head1)
    print("迭代法结果: ")
    print_list(result1)
    
    # 测试用例2: []
    head2 = None
    print("\n测试用例2:")
    print("原始链表: 空链表")
    
    result2 = solution.swapPairs(head2)
    print("迭代法结果: ")
    print_list(result2)
    
    # 测试用例3: [1]
    nums3 = [1]
    head3 = build_list(nums3)
    print("\n测试用例3:")
    print("原始链表: ")
    print_list(head3)
    
    result3 = solution.swapPairs(head3)
    print("迭代法结果: ")
    print_list(result3)
    
    # 测试用例4: [1,2,3,4,5]
    nums4 = [1, 2, 3, 4, 5]
    head4 = build_list(nums4)
    print("\n测试用例4:")
    print("原始链表: ")
    print_list(head4)
    
    # 测试递归法
    result4_recursive = solution.swapPairsRecursive(head4)
    print("递归法结果: ")
    print_list(result4_recursive)
    
    # 测试用例5: [1,2,3]
    nums5 = [1, 2, 3]
    head5 = build_list(nums5)
    print("\n测试用例5:")
    print("原始链表: ")
    print_list(head5)
    
    # 测试不使用哑节点的方法
    result5_no_dummy = solution.swapPairsNoDummy(head5)
    print("不使用哑节点方法结果: ")
    print_list(result5_no_dummy)
    
    # 测试用例6: [1,2,3,4,5,6]
    nums6 = [1, 2, 3, 4, 5, 6]
    head6 = build_list(nums6)
    print("\n测试用例6:")
    print("原始链表: ")
    print_list(head6)
    
    # 测试值交换法
    result6_values = solution.swapPairsValues(head6)
    print("值交换法结果: ")
    print_list(result6_values)

# 运行主函数
if __name__ == "__main__":
    main()

'''
* 题目扩展：LeetCode 24. 两两交换链表中的节点
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。

* 解题思路：
1. 迭代法（使用哑节点）：创建哑节点简化头节点处理，使用三个指针跟踪前驱节点和当前需要交换的两个节点
2. 递归法：递归处理剩余链表，然后交换当前两个节点
3. 迭代法（不使用哑节点）：单独处理头节点，然后迭代交换后续节点
4. 值交换法：不交换节点指针，只交换节点值（虽然不符合题目要求，但提供一种思路）

* 时间复杂度：
所有方法的时间复杂度都是 O(n)，其中n是链表的长度

* 空间复杂度：
- 迭代法（使用哑节点）：O(1)
- 递归法：O(n)，递归调用栈的深度
- 迭代法（不使用哑节点）：O(1)
- 值交换法：O(1)

* 最优解：迭代法（使用哑节点），空间复杂度O(1)，实现清晰

* 工程化考量：
1. 使用哑节点可以统一处理头节点和其他节点的交换逻辑
2. 需要注意指针操作的顺序，避免链表断裂
3. 递归法虽然代码简洁，但对于长链表可能会有栈溢出的风险
4. 值交换法虽然简单，但不符合题目要求的"只能进行节点交换"条件

* 与机器学习等领域的联系：
1. 链表操作是数据结构基础，在很多算法中都会用到
2. 递归思想在分治算法、树遍历等场景中有广泛应用
3. 指针操作的模式在图算法、内存管理等领域也有应用

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，引用传递
C++: 需要注意指针操作和内存管理

* 算法深度分析：
两两交换链表节点是一个经典的链表操作问题，主要考察对链表指针操作的掌握。迭代法使用三个指针（前驱节点、第一个节点、第二个节点），通过改变指针指向实现节点交换。递归法则是利用递归的特性，先处理后面的节点，再处理前面的节点，体现了"先解决子问题，再解决当前问题"的分治思想。使用哑节点是一个常用的技巧，可以避免单独处理头节点的情况，使代码更加简洁统一。
'''