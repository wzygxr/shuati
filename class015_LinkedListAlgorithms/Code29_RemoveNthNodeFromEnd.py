# 删除链表的倒数第N个节点 - LeetCode 19
# 测试链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 双指针法
    def removeNthFromEnd(self, head: ListNode, n: int) -> ListNode:
        """
        删除链表的倒数第N个节点
        时间复杂度: O(L)，其中L是链表长度
        空间复杂度: O(1)
        """
        # 创建哑节点，简化对头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # 初始化快慢指针
        fast = dummy
        slow = dummy
        
        # 快指针先走n+1步
        # 这样当快指针到达末尾时，慢指针正好指向待删除节点的前一个节点
        for _ in range(n + 1):
            # 确保n不大于链表长度
            if not fast:
                break
            fast = fast.next
        
        # 同时移动快慢指针
        while fast:
            fast = fast.next
            slow = slow.next
        
        # 删除目标节点
        slow.next = slow.next.next
        
        return dummy.next
    
    # 方法2: 两次遍历法
    def removeNthFromEndTwoPass(self, head: ListNode, n: int) -> ListNode:
        """
        两次遍历删除链表的倒数第N个节点
        时间复杂度: O(L)
        空间复杂度: O(1)
        """
        # 第一次遍历：计算链表长度
        length = 0
        current = head
        while current:
            length += 1
            current = current.next
        
        # 创建哑节点
        dummy = ListNode(0)
        dummy.next = head
        current = dummy
        
        # 第二次遍历：找到待删除节点的前一个节点
        for _ in range(length - n):
            current = current.next
        
        # 删除节点
        current.next = current.next.next
        
        return dummy.next
    
    # 方法3: 递归法
    def removeNthFromEndRecursive(self, head: ListNode, n: int) -> ListNode:
        """
        递归删除链表的倒数第N个节点
        时间复杂度: O(L)
        空间复杂度: O(L)，递归调用栈的深度
        """
        # 全局变量，用于记录递归深度
        self.count = 0
        
        # 递归函数
        def dfs(node):
            if not node:
                return None
            
            node.next = dfs(node.next)
            self.count += 1
            
            # 当count等于n时，当前节点就是要删除的节点
            # 返回node.next以跳过当前节点
            if self.count == n:
                return node.next
            
            return node
        
        # 使用哑节点处理删除头节点的情况
        dummy = ListNode(0)
        dummy.next = head
        dummy.next = dfs(dummy.next)
        
        return dummy.next
    
    # 方法4: 使用栈
    def removeNthFromEndStack(self, head: ListNode, n: int) -> ListNode:
        """
        使用栈删除链表的倒数第N个节点
        时间复杂度: O(L)
        空间复杂度: O(L)
        """
        # 创建哑节点
        dummy = ListNode(0)
        dummy.next = head
        
        # 创建栈并将所有节点压入栈中
        stack = []
        current = dummy
        while current:
            stack.append(current)
            current = current.next
        
        # 弹出栈顶的n个节点
        for _ in range(n):
            stack.pop()
        
        # 此时栈顶元素是待删除节点的前一个节点
        prev = stack[-1]
        prev.next = prev.next.next
        
        return dummy.next

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
    
    # 测试用例1: [1,2,3,4,5], n=2
    nums1 = [1, 2, 3, 4, 5]
    head1 = build_list(nums1)
    print(f"测试用例1:\n原始链表: {nums1}, n=2")
    
    # 测试双指针法
    result1 = solution.removeNthFromEnd(head1, 2)
    print(f"双指针法结果: {list_to_array(result1)}")
    
    # 测试用例2: [1], n=1
    nums2 = [1]
    head2 = build_list(nums2)
    print(f"\n测试用例2:\n原始链表: {nums2}, n=1")
    
    result2 = solution.removeNthFromEnd(head2, 1)
    print(f"结果: {list_to_array(result2)}")
    
    # 测试用例3: [1,2], n=1
    nums3 = [1, 2]
    head3 = build_list(nums3)
    print(f"\n测试用例3:\n原始链表: {nums3}, n=1")
    
    # 测试两次遍历法
    result3 = solution.removeNthFromEndTwoPass(head3, 1)
    print(f"两次遍历法结果: {list_to_array(result3)}")
    
    # 测试用例4: [1,2,3,4,5,6,7,8,9,10], n=5
    nums4 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    head4 = build_list(nums4)
    print(f"\n测试用例4:\n原始链表: {nums4}, n=5")
    
    # 测试递归法
    result4 = solution.removeNthFromEndRecursive(head4, 5)
    print(f"递归法结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,2,3,4,5], n=5
    nums5 = [1, 2, 3, 4, 5]
    head5 = build_list(nums5)
    print(f"\n测试用例5:\n原始链表: {nums5}, n=5")
    
    # 测试栈方法
    result5 = solution.removeNthFromEndStack(head5, 5)
    print(f"栈方法结果: {list_to_array(result5)}")
    
    # 测试用例6: [1,2,3], n=3
    nums6 = [1, 2, 3]
    head6 = build_list(nums6)
    print(f"\n测试用例6:\n原始链表: {nums6}, n=3")
    
    result6 = solution.removeNthFromEnd(head6, 3)
    print(f"结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 19. 删除链表的倒数第N个节点
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。

* 解题思路：
1. 双指针法：
   - 使用快慢指针，快指针先走n+1步
   - 然后同时移动快慢指针，当快指针到达末尾时，慢指针正好指向待删除节点的前一个节点
   - 删除慢指针的下一个节点
2. 两次遍历法：
   - 第一次遍历计算链表长度
   - 第二次遍历找到待删除节点的前一个节点并删除目标节点
3. 递归法：
   - 递归遍历链表到末尾，然后回溯
   - 使用计数器记录递归深度，当深度等于n时，删除该节点
4. 使用栈：
   - 将所有节点压入栈中
   - 弹出栈顶的n个节点
   - 此时栈顶元素是待删除节点的前一个节点
   - 修改指针删除目标节点

* 时间复杂度：
所有方法的时间复杂度均为 O(L)，其中L是链表长度

* 空间复杂度：
- 双指针法、两次遍历法：O(1)
- 递归法、使用栈：O(L)

* 最优解：双指针法，一次遍历，空间复杂度O(1)

* 工程化考量：
1. 使用哑节点可以统一处理删除头节点的情况
2. 需要注意n的取值范围，确保不会越界
3. 边界情况处理：空链表、单节点链表等
4. 指针操作需要小心，避免链表断裂

* 与机器学习等领域的联系：
1. 双指针技巧在数据处理中广泛应用
2. 栈和递归在算法设计中是常用的思维方式
3. 链表操作的思想在动态数据结构中很重要
4. 时间空间复杂度的权衡是算法设计的核心考量

* 语言特性差异：
Python: 无需手动管理内存，代码简洁
Java: 有自动内存管理，引用传递
C++: 需要注意指针操作和内存管理

* 算法深度分析：
删除链表倒数第N个节点是一个经典的链表操作问题，主要考察对链表特性和指针操作的理解。双指针法是一种非常优雅的解法，通过一次遍历就能找到倒数第N个节点的前一个节点，从而实现O(1)时间复杂度的删除操作。

双指针法的核心思想是利用快慢指针之间的"距离"来定位倒数第N个节点。快指针先走n+1步，然后两个指针同时前进，当快指针到达链表末尾时，慢指针正好位于待删除节点的前一个位置。这种方法避免了需要两次遍历的情况，提高了效率。

从更广泛的角度看，这个问题体现了链表操作中常见的技巧：
1. 使用哑节点简化头节点的特殊处理
2. 利用多指针协作进行定位和操作
3. 通过一次遍历完成复杂的定位操作

在实际应用中，链表的倒数第N个元素的概念在很多场景中都有应用，如日志分析、数据流处理等。理解并掌握这类问题的解法有助于处理更复杂的数据结构和算法问题。
"""