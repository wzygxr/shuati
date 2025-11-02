# 反转链表 - LeetCode 206
# 测试链接: https://leetcode.cn/problems/reverse-linked-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 迭代法（双指针法）
    def reverseList(self, head: ListNode) -> ListNode:
        """
        使用迭代法反转链表
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 初始化前驱节点为None
        prev = None
        # 当前节点从头节点开始
        curr = head
        
        # 遍历链表
        while curr:
            # 保存当前节点的下一个节点
            next_temp = curr.next
            # 反转当前节点的指针，指向前驱节点
            curr.next = prev
            # 前驱节点和当前节点都向前移动一步
            prev = curr
            curr = next_temp
        
        # 反转后，prev成为新的头节点
        return prev
    
    # 方法2: 递归法
    def reverseListRecursive(self, head: ListNode) -> ListNode:
        """
        使用递归法反转链表
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        """
        # 基本情况：链表为空或只有一个节点
        if not head or not head.next:
            return head
        
        # 递归反转当前节点之后的链表
        new_head = self.reverseListRecursive(head.next)
        
        # 将当前节点添加到反转后的链表末尾
        head.next.next = head
        # 断开当前节点与原链表的连接
        head.next = None
        
        # 返回反转后的链表头节点
        return new_head
    
    # 方法3: 头插法
    def reverseListInsert(self, head: ListNode) -> ListNode:
        """
        使用头插法反转链表
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建一个哑节点
        dummy = ListNode(0)
        curr = head
        
        # 遍历原链表
        while curr:
            # 保存下一个节点
            next_temp = curr.next
            # 将当前节点插入到dummy后面（头插）
            curr.next = dummy.next
            dummy.next = curr
            # 移动到原链表的下一个节点
            curr = next_temp
        
        # 返回新的头节点
        return dummy.next
    
    # 方法4: 栈实现
    def reverseListStack(self, head: ListNode) -> ListNode:
        """
        使用栈实现链表反转
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 边界情况处理
        if not head or not head.next:
            return head
        
        # 创建一个栈用于存储节点
        stack = []
        curr = head
        
        # 将所有节点压入栈中
        while curr:
            stack.append(curr)
            curr = curr.next
        
        # 创建新的头节点（原链表的尾节点）
        new_head = stack.pop()
        curr = new_head
        
        # 从栈中依次弹出节点，构建反转后的链表
        while stack:
            curr.next = stack.pop()
            curr = curr.next
        
        # 将最后一个节点的next指针设为None
        curr.next = None
        
        return new_head

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
    
    # 测试用例1: [1,2,3,4,5]
    head1 = build_list([1, 2, 3, 4, 5])
    print("测试用例1: [1,2,3,4,5]")
    
    # 测试迭代法
    result1 = solution.reverseList(head1)
    print(f"迭代法结果: {list_to_array(result1)}")
    
    # 测试用例2: [1,2]
    head2 = build_list([1, 2])
    print("\n测试用例2: [1,2]")
    
    # 测试递归法
    result2 = solution.reverseListRecursive(head2)
    print(f"递归法结果: {list_to_array(result2)}")
    
    # 测试用例3: []
    head3 = None
    print("\n测试用例3: []")
    
    result3 = solution.reverseList(head3)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [5]
    head4 = build_list([5])
    print("\n测试用例4: [5]")
    
    result4 = solution.reverseList(head4)
    print(f"结果: {list_to_array(result4)}")
    
    # 测试用例5: [1,3,5,7,9]
    head5 = build_list([1, 3, 5, 7, 9])
    print("\n测试用例5: [1,3,5,7,9]")
    
    # 测试头插法
    result5 = solution.reverseListInsert(head5)
    print(f"头插法结果: {list_to_array(result5)}")
    
    # 测试用例6: [-10,-5,0,5,10]
    head6 = build_list([-10, -5, 0, 5, 10])
    print("\n测试用例6: [-10,-5,0,5,10]")
    
    # 测试栈实现
    result6 = solution.reverseListStack(head6)
    print(f"栈实现结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 206. 反转链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。

* 解题思路：
1. 迭代法（双指针法）：
   - 使用两个指针：prev和curr
   - 遍历链表时，保存curr.next，然后将curr.next指向prev
   - 然后prev和curr都向前移动一步
   - 最后返回prev作为新的头节点
2. 递归法：
   - 递归反转当前节点之后的链表
   - 然后将当前节点添加到反转后的链表末尾
   - 将当前节点的next指针设为None
3. 头插法：
   - 创建一个哑节点
   - 遍历原链表，将每个节点插入到哑节点的后面
4. 栈实现：
   - 将所有节点压入栈中
   - 然后从栈中依次弹出节点，构建新的链表

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 迭代法和头插法：O(1)
- 递归法：O(n)，递归调用栈的深度
- 栈实现：O(n)，需要额外的栈空间

* 最优解：迭代法（双指针法），时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 迭代法是首选，空间复杂度O(1)，实现简单
2. 递归法代码简洁，但对于长链表可能导致栈溢出
3. 头插法也是一种常用的实现方式
4. 栈实现需要额外的空间，不推荐

* 与机器学习等领域的联系：
1. 链表操作是数据结构的基础
2. 反转操作在字符串处理、数组处理等场景中很常见
3. 递归思想在算法设计中很重要
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
反转链表是一个经典的链表操作问题，主要考察对链表特性和指针操作的理解。迭代法（双指针法）是解决这个问题的最优方法，其核心思想是通过两个指针的协同工作，逐个反转链表节点的指向。

在迭代过程中，我们需要保存当前节点的下一个节点，以防止在反转当前节点指向后丢失原链表的后续节点。然后将当前节点的next指针指向前一个节点，实现局部反转。接着将前一个节点和当前节点都向前移动一步，准备处理下一个节点。

递归法的思路也很清晰。假设当前节点之后的链表已经被反转，那么我们只需要将当前节点添加到反转后的链表末尾，并将当前节点的next指针设为None。递归的终止条件是链表为空或只有一个节点。

链表反转操作在实际应用中有很多场景，例如：
1. 在链表操作中，经常需要通过反转链表来改变数据访问的顺序
2. 在算法题中，链表反转是很多复杂链表操作的基础
3. 在系统设计中，反转操作可以用于优化数据处理流程

理解并掌握链表反转的实现有助于处理更复杂的链表问题，也为学习其他数据结构和算法打下基础。
"""