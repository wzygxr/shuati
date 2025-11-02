# 环形链表II - LeetCode 142
# 测试链接: https://leetcode.cn/problems/linked-list-cycle-ii/

# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # 方法1: 哈希表法
    def detectCycle(self, head: ListNode) -> ListNode:
        """
        使用哈希表查找链表中环的入口
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        # 创建一个集合用于存储已访问的节点
        visited = set()
        
        # 遍历链表
        current = head
        while current:
            # 如果当前节点已经在集合中，说明这是环的入口
            if current in visited:
                return current
            # 否则将当前节点加入集合
            visited.add(current)
            # 移动到下一个节点
            current = current.next
        
        # 如果遍历结束没有发现环，返回None
        return None
    
    # 方法2: 快慢指针法（Floyd's Cycle-Finding Algorithm）
    def detectCycleTwoPointers(self, head: ListNode) -> ListNode:
        """
        使用快慢指针查找链表中环的入口
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 处理边界情况
        if not head or not head.next:
            return None
        
        # 第一阶段：检测是否有环并找到相遇点
        slow = head
        fast = head
        has_cycle = False
        
        while fast and fast.next:
            slow = slow.next       # 慢指针每次移动一步
            fast = fast.next.next  # 快指针每次移动两步
            
            # 如果快慢指针相遇，说明有环
            if slow == fast:
                has_cycle = True
                break
        
        # 如果没有环，返回None
        if not has_cycle:
            return None
        
        # 第二阶段：找到环的入口
        # 数学证明：将一个指针重新指向头节点，然后两个指针都每次移动一步
        # 它们会在环的入口处相遇
        pointer1 = head
        pointer2 = slow  # pointer2指向相遇点
        
        while pointer1 != pointer2:
            pointer1 = pointer1.next
            pointer2 = pointer2.next
        
        # 返回环的入口
        return pointer1
    
    # 方法3: 标记法（修改原链表，不推荐）
    def detectCycleMark(self, head: ListNode) -> ListNode:
        """
        通过修改节点标记已访问的节点
        时间复杂度: O(n)
        空间复杂度: O(1)
        注意：这个方法会修改原链表结构
        """
        current = head
        
        # 使用特殊标记表示已访问
        while current:
            # 检查当前节点是否已被标记
            if hasattr(current, 'visited') and current.visited:
                # 清除标记（可选，恢复链表）
                current.visited = False
                return current
            # 标记当前节点
            current.visited = True
            # 移动到下一个节点
            current = current.next
        
        return None
    
    # 方法4: 暴力破解法（不推荐，时间复杂度高）
    def detectCycleBruteForce(self, head: ListNode) -> ListNode:
        """
        暴力破解法 - 对每个节点进行检查
        时间复杂度: O(n^2)
        空间复杂度: O(1)
        """
        # 遍历链表
        current = head
        index = 0
        
        while current:
            # 对于每个节点，检查它之后的所有节点
            check = current.next
            while check:
                # 如果后续节点引用指向当前节点或之前的节点，说明找到了环的入口
                if check == current:
                    return current
                check = check.next
            
            current = current.next
            index += 1
        
        return None

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
    nodes = [head]  # 存储所有节点，用于创建环和返回正确的入口
    
    for num in nums[1:]:
        current.next = ListNode(num)
        current = current.next
        nodes.append(current)
    
    # 创建环
    if pos >= 0 and pos < len(nodes):
        current.next = nodes[pos]
        return head, nodes[pos]  # 返回头节点和环的入口
    
    return head, None  # 无环情况

# 辅助函数：构建无环的链表
def create_list(nums):
    return create_cycle_list(nums, -1)

# 主函数用于测试
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 有环链表 [3,2,0,-4]，环从索引1开始
    print("测试用例1: 有环链表 [3,2,0,-4]，环从索引1开始")
    head1, expected1 = create_cycle_list([3, 2, 0, -4], 1)
    
    # 测试哈希表法
    result1 = solution.detectCycle(head1)
    print(f"哈希表法结果: {result1.val if result1 else 'None'}")
    print(f"预期结果: {expected1.val if expected1 else 'None'}")
    
    # 测试用例2: 有环链表 [1,2]，环从索引0开始
    print("\n测试用例2: 有环链表 [1,2]，环从索引0开始")
    head2, expected2 = create_cycle_list([1, 2], 0)
    
    # 测试快慢指针法
    result2 = solution.detectCycleTwoPointers(head2)
    print(f"快慢指针法结果: {result2.val if result2 else 'None'}")
    print(f"预期结果: {expected2.val if expected2 else 'None'}")
    
    # 测试用例3: 有环链表 [1]，环从索引0开始
    print("\n测试用例3: 有环链表 [1]，环从索引0开始")
    head3, expected3 = create_cycle_list([1], 0)
    
    result3 = solution.detectCycleTwoPointers(head3)
    print(f"快慢指针法结果: {result3.val if result3 else 'None'}")
    print(f"预期结果: {expected3.val if expected3 else 'None'}")
    
    # 测试用例4: 无环链表 [1,2,3,4,5]
    print("\n测试用例4: 无环链表 [1,2,3,4,5]")
    head4, expected4 = create_list([1, 2, 3, 4, 5])
    
    result4 = solution.detectCycle(head4)
    print(f"哈希表法结果: {result4.val if result4 else 'None'}")
    print(f"预期结果: {expected4.val if expected4 else 'None'}")
    
    # 测试用例5: 无环链表 []
    print("\n测试用例5: 无环链表 []")
    head5, expected5 = create_list([])
    
    result5 = solution.detectCycle(head5)
    print(f"结果: {result5.val if result5 else 'None'}")
    print(f"预期结果: {expected5.val if expected5 else 'None'}")
    
    # 测试用例6: 有环链表 [1,1,1,1,1]，环从索引2开始
    print("\n测试用例6: 有环链表 [1,1,1,1,1]，环从索引2开始")
    head6, expected6 = create_cycle_list([1, 1, 1, 1, 1], 2)
    
    result6 = solution.detectCycleTwoPointers(head6)
    print(f"快慢指针法结果: {result6.val if result6 else 'None'}")
    print(f"预期结果: {expected6.val if expected6 else 'None'}")

"""
* 题目扩展：LeetCode 142. 环形链表II
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。

* 解题思路：
1. 哈希表法：
   - 使用集合记录已访问过的节点
   - 遍历链表，检查每个节点是否已在集合中
   - 第一个重复出现的节点就是环的入口
2. 快慢指针法（Floyd's Cycle-Finding Algorithm）：
   - 第一阶段：使用快慢指针检测是否有环并找到相遇点
   - 第二阶段：将一个指针重新指向头节点，两个指针都每次移动一步，它们会在环的入口处相遇
   - 数学证明：设环外长度为a，环内相遇点距离入口为b，环长为c
     当快慢指针相遇时，快指针走过a + b + k*c，慢指针走过a + b
     由于快指针速度是慢指针的两倍，所以a + b + k*c = 2*(a + b)
     化简得a + b = k*c，即a = k*c - b，这意味着从头节点和相遇点各走a步会在环的入口相遇
3. 标记法：
   - 遍历链表时使用节点的额外属性标记已访问
   - 第一个被再次访问的节点就是环的入口
4. 暴力破解法：
   - 对每个节点，检查它之后的所有节点是否引用自己或之前的节点
   - 时间复杂度高，不推荐

* 时间复杂度：
- 哈希表法、快慢指针法、标记法：O(n)
- 暴力破解法：O(n^2)

* 空间复杂度：
- 哈希表法：O(n)
- 快慢指针法：O(1)
- 标记法：O(1)
- 暴力破解法：O(1)

* 最优解：快慢指针法，空间复杂度O(1)，实现优雅

* 工程化考量：
1. 快慢指针法是首选，不需要额外空间
2. 哈希表法虽然空间复杂度较高，但实现简单直观
3. 标记法会修改原链表结构，可能导致数据污染
4. 暴力破解法时间复杂度高，不适合实际应用

* 与机器学习等领域的联系：
1. 环检测问题在算法分析中很重要
2. 快慢指针技巧在其他算法中有应用
3. 数据结构的理解对于算法设计至关重要
4. 数学证明在算法设计中的应用

* 语言特性差异：
Python: 无需手动管理内存，使用is比较对象引用，可以动态添加属性
Java: 使用==比较对象引用
C++: 使用指针比较，需要注意空指针问题

* 算法深度分析：
寻找链表环的入口是一个经典的链表问题，快慢指针法是解决这个问题的最优方法。这个算法由Robert W. Floyd在1967年提出，也称为Floyd的龟兔赛跑算法。

算法分为两个阶段：
1. 第一阶段：使用快慢指针检测链表是否有环并找到相遇点。慢指针每次移动一步，快指针每次移动两步。如果链表有环，两个指针最终会在环内相遇。

2. 第二阶段：找到环的入口。将一个指针重新指向链表头，然后两个指针都每次移动一步。令人惊讶的是，它们会在环的入口处相遇。

这个算法的正确性可以通过数学证明：
- 设环外部分长度为a，环内部分长度为c
- 当快慢指针相遇时，慢指针走了a+b步，快指针走了a+b+k*c步（k为快指针在环内多走的圈数）
- 由于快指针速度是慢指针的两倍，所以a+b+k*c = 2*(a+b)
- 化简得a = k*c - b
- 这意味着从头节点出发走a步，和从相遇点出发走k*c - b步，都会到达环的入口
- 由于k*c是环长的整数倍，所以从相遇点出发走c - b步也会到达环的入口
- 因此，从头节点和相遇点各走a步会在环的入口相遇

在实际应用中，这个算法在很多场景中都有应用，如任务调度中的死锁检测、程序执行中的循环依赖检测等。理解并掌握这个算法有助于处理更复杂的链表和图论问题。
"""