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
        prev = dummy
        
        # 当至少还有两个节点需要交换时
        while prev.next and prev.next.next:
            # 获取需要交换的两个节点
            first = prev.next
            second = prev.next.next
            
            # 进行交换操作
            first.next = second.next  # 第一个节点指向下一组的第一个节点
            second.next = first       # 第二个节点指向第一个节点
            prev.next = second        # prev指向新的第一个节点
            
            # 更新prev指针，准备下一组交换
            prev = first
        
        return dummy.next
    
    # 方法2: 迭代法（不使用哑节点）
    def swapPairsNoDummy(self, head: ListNode) -> ListNode:
        """
        不使用哑节点的迭代实现
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next:
            return head
        
        # 新的头节点是原链表的第二个节点
        new_head = head.next
        
        # 用于跟踪前一组的末尾节点
        prev_tail = None
        
        while head and head.next:
            # 获取需要交换的两个节点
            first = head
            second = head.next
            
            # 保存下一组的起始位置
            next_pair = second.next
            
            # 交换节点
            second.next = first
            first.next = next_pair
            
            # 如果存在前一组，将其末尾指向当前组的头节点
            if prev_tail:
                prev_tail.next = second
            
            # 更新prev_tail为当前组的尾节点
            prev_tail = first
            
            # 移动到下一组
            head = next_pair
        
        return new_head
    
    # 方法3: 递归法
    def swapPairsRecursive(self, head: ListNode) -> ListNode:
        """
        递归实现两两交换链表中的节点
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：链表为空或只有一个节点
        if not head or not head.next:
            return head
        
        # 获取当前需要交换的两个节点
        first = head
        second = head.next
        
        # 递归处理剩余部分
        remaining = self.swapPairsRecursive(second.next)
        
        # 进行交换操作
        second.next = first
        first.next = remaining
        
        # 返回交换后的新头节点
        return second
    
    # 方法4: 交换节点值（不推荐，因为题目要求交换节点）
    def swapPairsByValue(self, head: ListNode) -> ListNode:
        """
        通过交换节点值实现，而非交换节点本身
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        current = head
        
        # 当至少还有两个节点时
        while current and current.next:
            # 交换节点值
            current.val, current.next.val = current.next.val, current.val
            # 移动到下一对
            current = current.next.next
        
        return head

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
    
    # 测试用例1: [1,2,3,4]
    nums1 = [1, 2, 3, 4]
    head1 = build_list(nums1)
    print(f"测试用例1:\n原始链表: {nums1}")
    
    # 测试迭代法（使用哑节点）
    result1 = solution.swapPairs(head1)
    print(f"迭代法（使用哑节点）结果: {list_to_array(result1)}")
    
    # 测试用例2: []
    head2 = None
    print(f"\n测试用例2:\n原始链表: []")
    
    result2 = solution.swapPairs(head2)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: [1]
    nums3 = [1]
    head3 = build_list(nums3)
    print(f"\n测试用例3:\n原始链表: {nums3}")
    
    result3 = solution.swapPairs(head3)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1,2,3]
    nums4 = [1, 2, 3]
    head4 = build_list(nums4)
    print(f"\n测试用例4:\n原始链表: {nums4}")
    
    # 测试迭代法（不使用哑节点）
    result4 = solution.swapPairsNoDummy(head4)
    print(f"迭代法（不使用哑节点）结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,2,3,4,5,6]
    nums5 = [1, 2, 3, 4, 5, 6]
    head5 = build_list(nums5)
    print(f"\n测试用例5:\n原始链表: {nums5}")
    
    # 测试递归法
    result5 = solution.swapPairsRecursive(head5)
    print(f"递归法结果: {list_to_array(result5)}")
    
    # 测试用例6: [1,2,3,4,5]
    nums6 = [1, 2, 3, 4, 5]
    head6 = build_list(nums6)
    print(f"\n测试用例6:\n原始链表: {nums6}")
    
    # 测试交换节点值法
    result6 = solution.swapPairsByValue(head6)
    print(f"交换节点值法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 24. 两两交换链表中的节点
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。

* 解题思路：
1. 迭代法（使用哑节点）：
   - 创建哑节点简化头节点处理
   - 使用prev指针跟踪当前处理位置
   - 每次处理两个节点，调整指针关系完成交换
   - 更新prev指针，准备处理下一对节点
2. 迭代法（不使用哑节点）：
   - 特殊处理头节点
   - 使用prev_tail指针跟踪前一组的末尾
   - 其余逻辑与方法1类似
3. 递归法：
   - 基本情况：链表为空或只有一个节点
   - 递归处理剩余部分
   - 交换当前两个节点，并与剩余部分连接
4. 交换节点值（不推荐，不符合题意）：
   - 直接交换相邻节点的值
   - 不修改节点的实际位置

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表长度

* 空间复杂度：
- 迭代法（使用哑节点）、迭代法（不使用哑节点）、交换节点值法：O(1)
- 递归法：O(n)，递归调用栈的深度

* 最优解：迭代法（使用哑节点），实现简洁，空间复杂度O(1)

* 工程化考量：
1. 使用哑节点可以统一处理逻辑，避免特殊处理头节点
2. 递归方法对于非常长的链表可能导致栈溢出
3. 指针操作需要小心，避免链表断裂
4. 注意链表长度为奇数的情况，最后一个节点不应被处理

* 与机器学习等领域的联系：
1. 链表操作是数据结构的基础，在很多算法中都有应用
2. 交换操作在排序算法中很常见
3. 递归思想在分治算法中有广泛应用
4. 迭代方法体现了状态转移的思想

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，引用传递
C++: 需要注意指针操作和内存管理

* 算法深度分析：
两两交换链表节点是一个经典的链表操作问题，主要考察对链表指针操作的熟练度。迭代方法通过维护几个关键指针（prev、first、second）来跟踪当前处理的位置和需要交换的节点。在交换过程中，需要注意指针的指向关系，避免链表断裂。

递归方法的思路更为简洁：每次处理一对节点，然后递归处理剩余部分。递归的终止条件是链表为空或只有一个节点。递归方法的优点是代码简洁，但需要额外的栈空间，对于非常长的链表可能会导致栈溢出。

从更广泛的角度看，这个问题体现了链表操作中的一个重要技巧：通过适当的指针管理，可以在O(1)时间内完成节点的重新连接，而不需要移动节点本身。这种思想在很多链表操作问题中都有应用，如插入、删除、反转等。

在实际应用中，链表的两两交换操作可以用于数据重排、特征工程等场景。理解并掌握这类问题的解法有助于处理更复杂的数据结构和算法问题。
"""