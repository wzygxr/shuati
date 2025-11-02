# 环形链表 - LeetCode 141
# 测试链接: https://leetcode.cn/problems/linked-list-cycle/

# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # 方法1: 哈希表法
    def hasCycle(self, head: ListNode) -> bool:
        """
        使用哈希表检测链表中是否有环
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 创建一个集合用于存储已访问的节点
        visited = set()
        
        # 遍历链表
        current = head
        while current:
            # 如果当前节点已经在集合中，说明有环
            if current in visited:
                return True
            # 否则将当前节点加入集合
            visited.add(current)
            # 移动到下一个节点
            current = current.next
        
        # 如果遍历结束没有发现环，返回False
        return False
    
    # 方法2: 快慢指针法（Floyd's Cycle-Finding Algorithm）
    def hasCycleTwoPointers(self, head: ListNode) -> bool:
        """
        使用快慢指针检测链表中是否有环
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next:
            return False
        
        # 初始化快慢指针
        slow = head      # 慢指针每次移动一步
        fast = head.next # 快指针每次移动两步
        
        # 当快指针和慢指针不相等时继续遍历
        while slow != fast:
            # 如果快指针到达链表末尾，说明没有环
            if not fast or not fast.next:
                return False
            # 慢指针移动一步
            slow = slow.next
            # 快指针移动两步
            fast = fast.next.next
        
        # 如果快慢指针相遇，说明有环
        return True
    
    # 方法3: 标记法（修改原链表，不推荐）
    def hasCycleMark(self, head: ListNode) -> bool:
        """
        通过修改节点标记已访问的节点
        时间复杂度: O(n)
        空间复杂度: O(1)
        注意：这个方法会修改原链表结构
        """
        current = head
        
        # 使用特殊标记表示已访问
        # 这里使用一个不太可能作为正常值的值作为标记
        while current:
            # 检查当前节点是否已被标记
            if current.val == 'visited':
                return True
            # 标记当前节点
            current.val = 'visited'
            # 移动到下一个节点
            current = current.next
        
        return False
    
    # 方法4: 递归标记法（修改原链表，不推荐）
    def hasCycleRecursive(self, head: ListNode) -> bool:
        """
        使用递归标记已访问的节点
        时间复杂度: O(n)
        空间复杂度: O(n)，递归调用栈的深度
        注意：这个方法会修改原链表结构
        """
        # 基本情况：链表为空
        if not head:
            return False
        
        # 检查当前节点是否已被标记
        if head.val == 'visited':
            return True
        
        # 标记当前节点
        head.val = 'visited'
        
        # 递归检查下一个节点
        return self.hasCycleRecursive(head.next)

# 辅助函数：构建有环的链表
def create_cycle_list(nums, pos):
    """
    构建一个有环的链表
    nums: 链表节点的值列表
    pos: 环的起始位置（从0开始，如果为-1表示无环）
    return: 链表头节点
    """
    if not nums:
        return None
    
    # 构建链表
    head = ListNode(nums[0])
    current = head
    nodes = [head]  # 存储所有节点，用于创建环
    
    for num in nums[1:]:
        current.next = ListNode(num)
        current = current.next
        nodes.append(current)
    
    # 创建环
    if pos >= 0 and pos < len(nodes):
        current.next = nodes[pos]
    
    return head

# 辅助函数：构建无环的链表
def create_list(nums):
    return create_cycle_list(nums, -1)

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 有环链表 [3,2,0,-4]，环从索引1开始
    print("测试用例1: 有环链表 [3,2,0,-4]，环从索引1开始")
    head1 = create_cycle_list([3, 2, 0, -4], 1)
    
    # 测试哈希表法
    print(f"哈希表法结果: {solution.hasCycle(head1)}")
    
    # 测试用例2: 有环链表 [1,2]，环从索引0开始
    print("\n测试用例2: 有环链表 [1,2]，环从索引0开始")
    head2 = create_cycle_list([1, 2], 0)
    
    # 测试快慢指针法
    print(f"快慢指针法结果: {solution.hasCycleTwoPointers(head2)}")
    
    # 测试用例3: 无环链表 [1]
    print("\n测试用例3: 无环链表 [1]")
    head3 = create_list([1])
    print(f"快慢指针法结果: {solution.hasCycleTwoPointers(head3)}")
    
    # 测试用例4: 无环链表 []
    print("\n测试用例4: 无环链表 []")
    head4 = None
    print(f"快慢指针法结果: {solution.hasCycleTwoPointers(head4)}")
    
    # 测试用例5: 无环链表 [1,2,3,4,5]
    print("\n测试用例5: 无环链表 [1,2,3,4,5]")
    head5 = create_list([1, 2, 3, 4, 5])
    print(f"哈希表法结果: {solution.hasCycle(head5)}")
    
    # 测试用例6: 有环链表 [1,1,1,1,1]，环从索引2开始
    print("\n测试用例6: 有环链表 [1,1,1,1,1]，环从索引2开始")
    head6 = create_cycle_list([1, 1, 1, 1, 1], 2)
    print(f"快慢指针法结果: {solution.hasCycleTwoPointers(head6)}")

"""
* 题目扩展：LeetCode 141. 环形链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你一个链表的头节点 head ，判断链表中是否有环。
如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。

* 解题思路：
1. 哈希表法：
   - 使用集合记录已访问过的节点
   - 遍历链表，检查每个节点是否已在集合中
   - 如果是，说明有环；否则，将节点加入集合
2. 快慢指针法（Floyd's Cycle-Finding Algorithm）：
   - 使用两个指针：慢指针每次移动一步，快指针每次移动两步
   - 如果链表有环，两个指针最终会相遇
   - 如果链表无环，快指针会先到达链表末尾
3. 标记法：
   - 遍历链表时修改节点值作为已访问标记
   - 注意：这个方法会修改原链表结构，不推荐在实际应用中使用
4. 递归标记法：
   - 使用递归遍历链表并标记已访问节点
   - 同样会修改原链表结构，不推荐使用

* 时间复杂度：
所有方法的时间复杂度均为 O(n)，其中n是链表长度

* 空间复杂度：
- 哈希表法：O(n)
- 快慢指针法：O(1)
- 标记法：O(1)
- 递归标记法：O(n)，递归调用栈的深度

* 最优解：快慢指针法，空间复杂度O(1)，实现优雅

* 工程化考量：
1. 快慢指针法是首选，不需要额外空间
2. 哈希表法虽然空间复杂度较高，但实现简单直观
3. 标记法会修改原链表结构，可能导致数据污染
4. 递归方法对于长链表可能导致栈溢出

* 与机器学习等领域的联系：
1. 环检测问题在算法分析中很重要
2. 快慢指针技巧在其他算法中有应用
3. 数据结构的理解对于算法设计至关重要
4. 空间优化是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，使用is比较对象引用
Java: 使用==比较对象引用
C++: 使用指针比较，需要注意空指针问题

* 算法深度分析：
环形链表问题是一个经典的数据结构问题，主要考察对链表特性的理解和算法设计能力。快慢指针法（也称为Floyd的龟兔赛跑算法）是解决这个问题的最优方法，其核心思想是利用两个指针以不同的速度遍历链表。

从数学角度证明快慢指针法的正确性：假设链表有环，环的长度为c，环外部分长度为a，快慢指针在环内相遇时快指针已经在环内走了b步。当快慢指针相遇时，快指针走过的距离是慢指针的两倍，即a + b + k*c = 2*(a + b)，其中k是快指针在环内多走的圈数。化简得a + b = k*c，这意味着如果从相遇点出发走a步，正好可以到达环的入口。这也是解决LeetCode 142（寻找环入口）问题的关键。

在实际应用中，环检测问题在很多场景中都有出现，如任务调度中的死锁检测、程序执行中的循环依赖检测等。理解并掌握这类问题的解法有助于处理更复杂的算法问题。
"""