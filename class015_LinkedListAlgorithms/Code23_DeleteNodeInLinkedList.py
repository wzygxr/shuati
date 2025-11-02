# 删除链表中的节点 - LeetCode 237
# 测试链接: https://leetcode.cn/problems/delete-node-in-a-linked-list/

# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # 方法1: 标准解法 - 将下一个节点的值复制到当前节点，然后删除下一个节点
    def deleteNode(self, node):
        """
        删除链表中的节点（不给出头节点，只给出要删除的节点）
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        # 将下一个节点的值复制到当前节点
        node.val = node.next.val
        # 删除下一个节点（当前节点指向下下个节点）
        node.next = node.next.next
    
    # 方法2: 递归解法
    def deleteNodeRecursive(self, node):
        """
        递归删除节点
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        # 基本情况：如果是最后一个节点，无法用这种方法删除
        if not node.next:
            raise Exception("不能删除链表的最后一个节点")
        
        # 将下一个节点的值复制到当前节点
        node.val = node.next.val
        # 递归处理下一个节点
        if node.next.next:
            self.deleteNodeRecursive(node.next)
        else:
            # 处理倒数第二个节点
            node.next = None
    
    # 方法3: 替换整个节点（不仅仅是值）
    def deleteNodeReplace(self, node):
        """
        通过替换整个节点的内容来删除
        时间复杂度: O(1)
        空间复杂度: O(1)
        """
        # 确保不是最后一个节点
        if not node.next:
            raise Exception("不能删除链表的最后一个节点")
        
        # 保存下一个节点
        next_node = node.next
        # 复制下一个节点的所有属性到当前节点
        node.val = next_node.val
        node.next = next_node.next
        # 断开下一个节点的链接
        next_node.next = None

# 辅助函数：构建链表
# nums: 链表节点的值列表
# return: 链表头节点
# 时间复杂度: O(n)
# 空间复杂度: O(n)
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
# 时间复杂度: O(n)
# 空间复杂度: O(1)
def print_list(head):
    values = []
    current = head
    
    while current:
        values.append(str(current.val))
        current = current.next
    
    print(" -> ".join(values) if values else "空链表")

# 辅助函数：根据值查找节点
# head: 链表头节点
# value: 要查找的节点值
# return: 找到的节点或None
# 时间复杂度: O(n)
# 空间复杂度: O(1)
def find_node(head, value):
    current = head
    
    while current:
        if current.val == value:
            return current
        current = current.next
    
    return None

# 主函数用于测试
def main():
    solution = Solution()
    
    # 测试用例1: [4,5,1,9], 删除节点5
    nums1 = [4, 5, 1, 9]
    head1 = build_list(nums1)
    print("测试用例1:")
    print("原始链表: ")
    print_list(head1)
    
    # 找到要删除的节点（值为5的节点）
    node_to_delete1 = find_node(head1, 5)
    solution.deleteNode(node_to_delete1)
    print("删除值为5的节点后: ")
    print_list(head1)
    
    # 测试用例2: [4,5,1,9], 删除节点1
    nums2 = [4, 5, 1, 9]
    head2 = build_list(nums2)
    print("\n测试用例2:")
    print("原始链表: ")
    print_list(head2)
    
    node_to_delete2 = find_node(head2, 1)
    solution.deleteNode(node_to_delete2)
    print("删除值为1的节点后: ")
    print_list(head2)
    
    # 测试用例3: [1,2,3,4], 删除节点2（使用递归方法）
    nums3 = [1, 2, 3, 4]
    head3 = build_list(nums3)
    print("\n测试用例3:")
    print("原始链表: ")
    print_list(head3)
    
    node_to_delete3 = find_node(head3, 2)
    solution.deleteNodeRecursive(node_to_delete3)
    print("递归方法删除值为2的节点后: ")
    print_list(head3)
    
    # 测试用例4: [0,1], 删除节点0
    nums4 = [0, 1]
    head4 = build_list(nums4)
    print("\n测试用例4:")
    print("原始链表: ")
    print_list(head4)
    
    node_to_delete4 = find_node(head4, 0)
    solution.deleteNode(node_to_delete4)
    print("删除头节点0后: ")
    print_list(head4)
    
    # 测试用例5: [3,5,7,9,11], 删除节点7（使用替换方法）
    nums5 = [3, 5, 7, 9, 11]
    head5 = build_list(nums5)
    print("\n测试用例5:")
    print("原始链表: ")
    print_list(head5)
    
    node_to_delete5 = find_node(head5, 7)
    solution.deleteNodeReplace(node_to_delete5)
    print("替换方法删除值为7的节点后: ")
    print_list(head5)

# 运行主函数
if __name__ == "__main__":
    main()

'''
* 题目扩展：LeetCode 237. 删除链表中的节点
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
请编写一个函数，用于 删除单链表中某个特定节点 。在设计函数时需要注意，你无法访问链表的头节点 head ，只能直接访问 要被删除的节点 。
题目数据保证需要删除的节点 不是末尾节点 。

* 解题思路：
由于无法访问链表的头节点，传统的删除节点方法（找到前一个节点）无法使用。
1. 标准解法：将下一个节点的值复制到当前节点，然后删除下一个节点
2. 递归解法：递归地将后续节点的值向前移动
3. 替换方法：完整复制下一个节点的内容并删除下一个节点

* 时间复杂度：
所有方法的时间复杂度都是 O(1)，只需要常数级别的操作

* 空间复杂度：
- 标准解法：O(1)
- 递归解法：O(1)，虽然使用了递归，但递归深度固定为1
- 替换方法：O(1)

* 最优解：标准解法，实现简单，逻辑清晰，效率最高

* 工程化考量：
1. 注意处理边界情况，比如无法删除链表的最后一个节点
2. 在实际应用中，可能需要考虑节点包含复杂数据的情况
3. 注意内存管理，避免内存泄漏
4. 需要确认输入的节点不是链表的最后一个节点

* 与机器学习等领域的联系：
1. 在数据处理中，类似的原地替换操作可以节省内存空间
2. 在链表结构的特征处理中可能会用到此类操作
3. 对于大规模数据，高效的节点操作尤为重要

* 语言特性差异：
Python: 无需手动管理内存，通过垃圾回收机制自动处理
Java: 通过引用传递实现节点操作
C++: 需要注意内存泄漏问题，可能需要手动释放被删除的节点

* 算法深度分析：
这个问题的解法巧妙地利用了链表节点的特性，通过值的替换而非传统的指针重定向来实现节点的删除。这种方法虽然在逻辑上"欺骗"了用户（删除的实际上是下一个节点），但在功能上达到了相同的效果。需要注意的是，这种方法要求被删除的节点不是链表的最后一个节点，因为它需要访问并修改下一个节点。
'''