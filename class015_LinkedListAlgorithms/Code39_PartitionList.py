# 分隔链表 - LeetCode 86
# 测试链接: https://leetcode.cn/problems/partition-list/

# 定义链表节点类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

class Solution:
    # 方法1: 双链表法
    def partition(self, head: ListNode, x: int) -> ListNode:
        """
        使用两个链表分别存储小于x和大于等于x的节点，然后合并
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 创建两个哑节点，分别用于小于x和大于等于x的链表
        before_dummy = ListNode(0)
        after_dummy = ListNode(0)
        
        # 当前指针，用于构建两个链表
        before = before_dummy
        after = after_dummy
        
        # 遍历原链表，将节点分配到两个链表中
        current = head
        while current:
            if current.val < x:
                # 将当前节点添加到小于x的链表
                before.next = current
                before = before.next
            else:
                # 将当前节点添加到大于等于x的链表
                after.next = current
                after = after.next
            
            # 移动到下一个节点
            current = current.next
        
        # 确保大于等于x的链表的最后一个节点的next为None，防止形成环
        after.next = None
        
        # 合并两个链表：将小于x的链表的尾部连接到大于等于x的链表的头部
        before.next = after_dummy.next
        
        # 返回合并后的链表头节点
        return before_dummy.next
    
    # 方法2: 单链表插入法
    def partitionInsert(self, head: ListNode, x: int) -> ListNode:
        """
        在单链表上直接操作，将小于x的节点插入到前面
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head:
            return None
        
        # 创建哑节点，简化头节点的处理
        dummy = ListNode(0)
        dummy.next = head
        
        # prev指向已处理部分的最后一个小于x的节点
        prev = dummy
        # curr用于遍历链表
        curr = head
        # prev_curr指向curr的前一个节点
        prev_curr = dummy
        
        while curr:
            # 如果当前节点值小于x且不在正确位置
            if curr.val < x and prev.next != curr:
                # 保存当前节点的下一个节点
                next_temp = curr.next
                # 将当前节点移动到prev后面
                curr.next = prev.next
                prev.next = curr
                # 更新prev为当前节点
                prev = curr
                # 连接剩余部分
                prev_curr.next = next_temp
                # 移动curr到下一个节点
                curr = next_temp
            else:
                # 如果当前节点值小于x且在正确位置，更新prev
                if curr.val < x:
                    prev = curr
                # 移动prev_curr和curr
                prev_curr = curr
                curr = curr.next
        
        return dummy.next
    
    # 方法3: 数组收集法
    def partitionArray(self, head: ListNode, x: int) -> ListNode:
        """
        将链表节点值收集到数组中，重新排列后重建链表
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 收集节点值到数组
        values = []
        current = head
        while current:
            values.append(current.val)
            current = current.next
        
        # 重新排列数组：小于x的元素在前，大于等于x的元素在后
        # 保持相对顺序
        less = [val for val in values if val < x]
        greater_or_equal = [val for val in values if val >= x]
        
        # 合并两个部分
        new_values = less + greater_or_equal
        
        # 重建链表
        dummy = ListNode(0)
        current = dummy
        for val in new_values:
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
    
    # 测试用例1: [1,4,3,2,5,2], x=3
    head1 = build_list([1, 4, 3, 2, 5, 2])
    print("测试用例1: [1,4,3,2,5,2], x=3")
    
    # 测试双链表法
    result1 = solution.partition(head1, 3)
    print(f"双链表法结果: {list_to_array(result1)}")
    
    # 测试用例2: [2,1], x=2
    head2 = build_list([2, 1])
    print("\n测试用例2: [2,1], x=2")
    
    result2 = solution.partition(head2, 2)
    print(f"双链表法结果: {list_to_array(result2)}")
    
    # 测试用例3: [], x=0
    head3 = None
    print("\n测试用例3: [], x=0")
    
    result3 = solution.partition(head3, 0)
    print(f"结果: {list_to_array(result3)}")
    
    # 测试用例4: [1], x=1
    head4 = build_list([1])
    print("\n测试用例4: [1], x=1")
    
    result4 = solution.partition(head4, 1)
    print(f"结果: {list_to_array(result4)}")
    
    # 测试用例5: [3,1,2,5,4,6,0], x=4
    head5 = build_list([3, 1, 2, 5, 4, 6, 0])
    print("\n测试用例5: [3,1,2,5,4,6,0], x=4")
    
    # 测试插入法
    result5 = solution.partitionInsert(head5, 4)
    print(f"插入法结果: {list_to_array(result5)}")
    
    # 测试用例6: [-10,-5,0,5,10], x=0
    head6 = build_list([-10, -5, 0, 5, 10])
    print("\n测试用例6: [-10,-5,0,5,10], x=0")
    
    # 测试数组收集法
    result6 = solution.partitionArray(head6, 0)
    print(f"数组收集法结果: {list_to_array(result6)}")

"""
* 题目扩展：LeetCode 86. 分隔链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
你应当 保留 两个分区中每个节点的初始相对位置。

* 解题思路：
1. 双链表法：
   - 创建两个哑节点，分别用于存储小于x和大于等于x的节点
   - 遍历原链表，根据节点值将节点分配到对应的链表中
   - 连接两个链表，返回结果
2. 单链表插入法：
   - 在单链表上直接操作，维护一个指针指向已处理部分的最后一个小于x的节点
   - 遍历链表，将小于x的节点插入到该指针后面
   - 保持原有的相对顺序
3. 数组收集法：
   - 将链表节点值收集到数组中
   - 重新排列数组，使小于x的元素在前，大于等于x的元素在后
   - 根据排列后的数组重建链表

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表的长度

* 空间复杂度：
- 双链表法和单链表插入法：O(1)
- 数组收集法：O(n)

* 最优解：双链表法，时间复杂度O(n)，空间复杂度O(1)

* 工程化考量：
1. 双链表法是首选，实现简单，逻辑清晰
2. 单链表插入法需要更复杂的指针操作，但空间复杂度同样为O(1)
3. 数组收集法实现简单，但需要额外的O(n)空间
4. 双链表法在实现时需要注意将大于等于x的链表的最后一个节点的next设为None，防止形成环

* 与机器学习等领域的联系：
1. 链表分区是数据处理的常见操作
2. 保持相对顺序在排序算法中有重要应用
3. 指针操作是链表处理的基础
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，对象引用操作简单
Java: 引用传递，不需要处理指针
C++: 需要处理指针，注意内存管理

* 算法深度分析：
分隔链表是一个经典的链表操作问题，主要考察对链表特性的理解和指针操作的能力。双链表法是解决这个问题的最优方法，其核心思想是将链表分为两部分，然后重新连接。

具体来说，双链表法分为以下几个步骤：
1. 创建两个哑节点，分别用于存储小于x和大于等于x的节点。哑节点可以简化对链表头节点的处理。

2. 遍历原链表，根据节点值将节点分配到对应的链表中。对于每个节点，如果其值小于x，则将其添加到第一个链表；否则，将其添加到第二个链表。

3. 在遍历结束后，需要将第二个链表的最后一个节点的next指针设为None，以防止形成环。这是一个容易被忽视的细节。

4. 最后，将第一个链表的尾部连接到第二个链表的头部，形成最终的链表。

这种方法的优点是实现简单，逻辑清晰，只需要一次遍历就能完成分隔操作，空间复杂度为O(1)。

单链表插入法虽然也能达到O(1)的空间复杂度，但需要更复杂的指针操作，容易出错。数组收集法则需要额外的O(n)空间，但实现相对简单。

在实际应用中，分隔链表的思想在很多场景中都有应用，如快速排序算法中的分区操作、数据筛选等。理解并掌握这类问题的解法有助于处理更复杂的数据处理任务。

此外，这个问题还体现了一个重要的算法设计原则：在处理链表问题时，适当地使用哑节点可以简化对链表头节点的处理，减少边界条件的检查。同时，保持原始数据的相对顺序也是一个重要的要求，这在很多实际应用中都很重要。
"""