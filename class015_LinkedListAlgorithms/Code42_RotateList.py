# 旋转链表 - LeetCode 61
# 测试链接: https://leetcode.cn/problems/rotate-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 闭合为环，断开指定位置
    def rotateRight(self, head: ListNode, k: int) -> ListNode:
        """
        将链表向右旋转k个位置，通过先闭合为环，再在指定位置断开
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next or k == 0:
            return head
        
        # 计算链表长度
        n = 1
        current = head
        while current.next:
            current = current.next
            n += 1
        
        # 优化k值，避免不必要的旋转
        k = k % n
        if k == 0:
            return head
        
        # 将链表闭合为环
        current.next = head
        
        # 找到新的尾节点（原链表的第n-k-1个节点）
        new_tail = head
        for _ in range(n - k - 1):
            new_tail = new_tail.next
        
        # 新的头节点是尾节点的下一个节点
        new_head = new_tail.next
        
        # 断开环
        new_tail.next = None
        
        return new_head
    
    # 方法2: 快慢指针法
    def rotateRightTwoPointers(self, head: ListNode, k: int) -> ListNode:
        """
        使用快慢指针找到旋转位置
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next or k == 0:
            return head
        
        # 计算链表长度
        n = 1
        current = head
        while current.next:
            current = current.next
            n += 1
        
        # 优化k值
        k = k % n
        if k == 0:
            return head
        
        # 快指针先移动k步
        fast = head
        for _ in range(k):
            fast = fast.next
        
        # 慢指针从头开始，快慢指针同时移动
        slow = head
        while fast.next:
            slow = slow.next
            fast = fast.next
        
        # 此时slow指向新的尾节点的前一个位置
        new_head = slow.next
        slow.next = None
        fast.next = head
        
        return new_head
    
    # 方法3: 数组转换法
    def rotateRightArray(self, head: ListNode, k: int) -> ListNode:
        """
        将链表转换为数组，旋转后重新构建链表
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 处理边界情况
        if not head or not head.next or k == 0:
            return head
        
        # 将链表转换为数组
        values = []
        current = head
        while current:
            values.append(current.val)
            current = current.next
        
        n = len(values)
        # 优化k值
        k = k % n
        if k == 0:
            return head
        
        # 旋转数组
        rotated_values = values[-k:] + values[:-k]
        
        # 重新构建链表
        dummy = ListNode(0)
        current = dummy
        for val in rotated_values:
            current.next = ListNode(val)
            current = current.next
        
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
    
    # 测试用例1: [1,2,3,4,5], k=2
    head1 = build_list([1, 2, 3, 4, 5])
    print("测试用例1: [1,2,3,4,5], k=2")
    
    # 测试闭合为环法
    result1 = solution.rotateRight(head1, 2)
    print(f"闭合为环法结果: {list_to_array(result1)}")
    
    # 测试用例2: [0,1,2], k=4
    head2 = build_list([0, 1, 2])
    print("\n测试用例2: [0,1,2], k=4")
    
    result2 = solution.rotateRight(head2, 4)
    print(f"闭合为环法结果: {list_to_array(result2)}")
    
    # 测试用例3: [], k=0
    head3 = None
    print("\n测试用例3: [], k=0")
    
    result3 = solution.rotateRight(head3, 0)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1], k=1
    head4 = build_list([1])
    print("\n测试用例4: [1], k=1")
    
    result4 = solution.rotateRight(head4, 1)
    print(f"结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,2], k=3
    head5 = build_list([1, 2])
    print("\n测试用例5: [1,2], k=3")
    
    # 测试快慢指针法
    result5 = solution.rotateRightTwoPointers(head5, 3)
    print(f"快慢指针法结果: {list_to_array(result5)}")
    
    # 测试用例6: [1,2,3,4,5,6], k=10
    head6 = build_list([1, 2, 3, 4, 5, 6])
    print("\n测试用例6: [1,2,3,4,5,6], k=10")
    
    # 测试数组转换法
    result6 = solution.rotateRightArray(head6, 10)
    print(f"数组转换法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 61. 旋转链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。

* 解题思路：
1. 闭合为环，断开指定位置：
   - 计算链表长度n
   - 优化k值，k = k % n，避免不必要的旋转
   - 将链表的尾节点连接到链表的头节点，形成环形链表
   - 找到新的尾节点（原链表的第n-k-1个节点），断开环
   - 返回新的头节点（原链表的第n-k个节点）
2. 快慢指针法：
   - 计算链表长度n，优化k值
   - 快指针先移动k步，然后快慢指针同时移动
   - 当快指针到达末尾时，慢指针指向新的尾节点的前一个位置
   - 调整指针关系，实现旋转
3. 数组转换法：
   - 将链表转换为数组
   - 旋转数组
   - 根据旋转后的数组重新构建链表

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 闭合为环法和快慢指针法：O(1)
- 数组转换法：O(n)

* 最优解：闭合为环法，时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 闭合为环法是首选，实现简单，逻辑清晰
2. 快慢指针法也是一种有效的实现方式
3. 数组转换法实现简单，但需要额外的O(n)空间
4. 优化k值是关键，可以避免不必要的旋转

* 与机器学习等领域的联系：
1. 旋转操作是数据处理的常见需求
2. 链表操作是数据结构的基础
3. 快慢指针技巧在链表问题中有广泛应用
4. 环形结构在算法设计中有特殊用途

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
旋转链表是一个经典的链表操作问题，主要考察对链表特性的理解和指针操作的能力。闭合为环法是解决这个问题的最优方法，其核心思想是先将链表闭合为环形链表，然后在适当的位置断开，形成新的链表。

具体来说，闭合为环法分为以下几个步骤：
1. 计算链表长度n。这是因为旋转n次后，链表会回到原始状态。

2. 优化k值。由于旋转n次后链表会回到原始状态，我们可以将k对n取模，即k = k % n。这样可以避免不必要的旋转。如果k取模后为0，说明链表不需要旋转，可以直接返回。

3. 将链表闭合为环。将链表的尾节点的next指针指向链表的头节点，形成一个环形链表。

4. 找到新的尾节点。新的尾节点是原链表的第n-k-1个节点。因为旋转k个位置后，原来的第n-k个节点会成为新的头节点，原来的第n-k-1个节点会成为新的尾节点。

5. 断开环。将新的尾节点的next指针设为None，断开环。

6. 返回新的头节点。新的头节点是新的尾节点的下一个节点。

这种方法的优点是实现简单，逻辑清晰，只需要一次遍历就能完成旋转操作，空间复杂度为O(1)。

快慢指针法的思路也很巧妙。我们可以使用两个指针：快指针和慢指针。首先，让快指针先走k步。然后，两个指针同时走，当快指针到达链表末尾时，慢指针正好指向新的尾节点的前一个位置。接下来，我们只需要调整指针关系，就能实现链表的旋转。

数组转换法虽然也能解决问题，但需要额外的O(n)空间，效率不如前两种方法。

在实际应用中，旋转链表的思想在很多场景中都有应用，如数组旋转、环形队列等。理解并掌握这类问题的解法有助于处理更复杂的数据处理任务。

此外，这个问题还体现了一个重要的算法设计原则：在处理链表问题时，我们可以充分利用链表的特性，如将链表闭合为环，然后在适当的位置断开，从而实现特定的功能。同时，优化操作，如计算链表长度并对k值取模，可以减少不必要的操作，提高算法效率。
"""