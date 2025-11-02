# 删除链表中的节点 - LeetCode 237
# 测试链接: https://leetcode.cn/problems/delete-node-in-a-linked-list/

# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # 方法1: 节点值替换法
    def deleteNode(self, node):
        """
        给定要删除的节点，通过节点值替换的方式删除该节点
        时间复杂度: O(1)
        空间复杂度: O(1)
        注意：这个方法只适用于删除链表中的非尾节点
        """
        # 将要删除节点的下一个节点的值复制到当前节点
        node.val = node.next.val
        # 跳过下一个节点（相当于删除了当前节点）
        node.next = node.next.next
    
    # 方法2: 递归删除法（不太适用于这个问题的场景，但提供作为参考）
    def deleteNodeRecursive(self, node):
        """
        使用递归方式删除节点
        时间复杂度: O(1)，但递归调用栈深度为O(1)
        空间复杂度: O(1)
        """
        # 基本情况：如果是最后一个节点，无法使用此方法删除
        if not node.next:
            raise ValueError("Cannot delete the last node using this method")
        
        # 将下一个节点的值复制到当前节点
        node.val = node.next.val
        
        # 如果下一个节点不是尾节点，递归处理
        if node.next.next:
            self.deleteNodeRecursive(node.next)
        else:
            # 如果下一个节点是尾节点，直接删除
            node.next = None

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

# 辅助函数：根据值查找节点

def find_node(head: ListNode, val: int) -> ListNode:
    current = head
    while current:
        if current.val == val:
            return current
        current = current.next
    return None

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [4,5,1,9], 删除节点5
    head1 = build_list([4, 5, 1, 9])
    print("测试用例1: [4,5,1,9], 删除节点5")
    print(f"删除前链表: {list_to_array(head1)}")
    
    # 查找要删除的节点
    node1 = find_node(head1, 5)
    if node1:
        solution.deleteNode(node1)
        print(f"删除后链表: {list_to_array(head1)}")
    else:
        print("未找到要删除的节点")
    
    # 测试用例2: [4,5,1,9], 删除节点1
    head2 = build_list([4, 5, 1, 9])
    print("\n测试用例2: [4,5,1,9], 删除节点1")
    print(f"删除前链表: {list_to_array(head2)}")
    
    node2 = find_node(head2, 1)
    if node2:
        solution.deleteNode(node2)
        print(f"删除后链表: {list_to_array(head2)}")
    else:
        print("未找到要删除的节点")
    
    # 测试用例3: [1,2,3,4], 删除节点2
    head3 = build_list([1, 2, 3, 4])
    print("\n测试用例3: [1,2,3,4], 删除节点2")
    print(f"删除前链表: {list_to_array(head3)}")
    
    node3 = find_node(head3, 2)
    if node3:
        solution.deleteNodeRecursive(node3)
        print(f"递归删除后链表: {list_to_array(head3)}")
    else:
        print("未找到要删除的节点")
    
    # 测试用例4: [0,1], 删除节点0
    head4 = build_list([0, 1])
    print("\n测试用例4: [0,1], 删除节点0")
    print(f"删除前链表: {list_to_array(head4)}")
    
    node4 = find_node(head4, 0)
    if node4:
        solution.deleteNode(node4)
        print(f"删除后链表: {list_to_array(head4)}")
    else:
        print("未找到要删除的节点")
    
    # 测试用例5: [1,2,3,4,5], 删除节点3
    head5 = build_list([1, 2, 3, 4, 5])
    print("\n测试用例5: [1,2,3,4,5], 删除节点3")
    print(f"删除前链表: {list_to_array(head5)}")
    
    node5 = find_node(head5, 3)
    if node5:
        solution.deleteNode(node5)
        print(f"删除后链表: {list_to_array(head5)}")
    else:
        print("未找到要删除的节点")

"""
* 题目扩展：LeetCode 237. 删除链表中的节点
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
请编写一个函数，用于 删除单链表中某个特定节点 。在设计函数时需要注意，你无法访问链表的头节点 head ，只能直接访问 要被删除的节点 。

* 解题思路：
1. 节点值替换法：
   - 将要删除节点的下一个节点的值复制到当前节点
   - 然后跳过下一个节点（相当于删除了当前节点）
   - 这种方法实际上并没有删除给定的节点，而是将其替换为下一个节点的值，然后删除下一个节点
   - 注意：这个方法只适用于删除链表中的非尾节点
2. 递归删除法：
   - 递归地将下一个节点的值复制到当前节点
   - 当到达倒数第二个节点时，将其next指针设为None
   - 这种方法对于本题场景不是特别必要，但提供作为参考

* 时间复杂度：
两种方法的时间复杂度均为 O(1)

* 空间复杂度：
- 节点值替换法：O(1)
- 递归删除法：O(1)，但递归调用栈深度为O(1)

* 最优解：节点值替换法，时间复杂度O(1)，空间复杂度O(1)

* 工程化考量：
1. 节点值替换法是首选，实现简单，效率高
2. 递归删除法对于本题场景不是特别必要，但在某些递归相关的问题中可能有用
3. 这个问题的特殊之处在于无法访问头节点，只能访问要删除的节点
4. 这种删除方法只适用于非尾节点，题目保证输入的节点不是尾节点

* 与机器学习等领域的联系：
1. 链表操作是数据结构的基础
2. 节点替换的思想在很多算法中有应用
3. 特殊情况下的操作需要灵活变通
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
删除链表中的节点是一个经典的链表操作问题，但这个题目有一个特殊的约束：无法访问链表的头节点，只能直接访问要被删除的节点。这导致我们无法使用传统的删除链表节点的方法（找到前一个节点，然后跳过当前节点）。

为了解决这个问题，我们可以采用节点值替换的方法。具体来说，我们将要删除节点的下一个节点的值复制到当前节点，然后跳过下一个节点。这样，从功能上看，就相当于删除了当前节点。

这种方法的巧妙之处在于，我们实际上并没有删除给定的节点，而是将其替换为下一个节点的值，然后删除下一个节点。从外部观察，链表中不再包含原来的节点值，实现了删除节点的效果。

需要注意的是，这种删除方法只适用于删除链表中的非尾节点。如果要删除的节点是尾节点，那么它没有下一个节点，无法使用这种方法。但题目保证输入的节点不是尾节点，所以我们不需要处理这种情况。

在实际应用中，这种删除节点的方法可能不是很常见，因为我们通常都能访问链表的头节点。但这个问题提醒我们，在特殊情况下，我们需要灵活变通，寻找其他解决方案。

此外，这个问题还体现了一个重要的算法设计原则：在某些情况下，我们可以通过改变节点的值而不是改变节点的连接关系来实现相同的功能。这种思路在很多算法问题中都有应用。
"""