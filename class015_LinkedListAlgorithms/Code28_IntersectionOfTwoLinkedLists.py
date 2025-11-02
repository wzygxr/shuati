# 相交链表 - LeetCode 160
# 测试链接: https://leetcode.cn/problems/intersection-of-two-linked-lists/

# 定义链表节点类
class ListNode:
    def __init__(self, x):
        self.val = x
        self.next = None

class Solution:
    # 方法1: 双指针法（浪漫相遇法）
    def getIntersectionNode(self, headA: ListNode, headB: ListNode) -> ListNode:
        """
        找出两个链表的相交节点
        时间复杂度: O(n + m)
        空间复杂度: O(1)
        """
        # 边界条件检查
        if not headA or not headB:
            return None
        
        # 初始化两个指针
        pA, pB = headA, headB
        
        # 当两个指针不相等时继续遍历
        # 如果没有相交，最终两个指针都会变为None而退出循环
        while pA != pB:
            # 如果pA到达链表末尾，则指向另一个链表的头部
            # 否则继续前进
            pA = headB if pA is None else pA.next
            # 如果pB到达链表末尾，则指向另一个链表的头部
            # 否则继续前进
            pB = headA if pB is None else pB.next
        
        # 返回相交节点（如果没有相交，pA为None）
        return pA
    
    # 方法2: 计算长度差法
    def getIntersectionNodeByLength(self, headA: ListNode, headB: ListNode) -> ListNode:
        """
        通过计算两个链表的长度差来找出相交节点
        时间复杂度: O(n + m)
        空间复杂度: O(1)
        """
        # 计算两个链表的长度
        lenA, lenB = self.getLength(headA), self.getLength(headB)
        
        # 让较长的链表先走长度差步
        if lenA > lenB:
            for _ in range(lenA - lenB):
                headA = headA.next
        else:
            for _ in range(lenB - lenA):
                headB = headB.next
        
        # 同时遍历两个链表，找到第一个相同的节点
        while headA and headB:
            if headA == headB:  # 比较节点引用是否相同，不是比较值
                return headA
            headA = headA.next
            headB = headB.next
        
        return None
    
    # 方法3: 使用哈希集合
    def getIntersectionNodeByHash(self, headA: ListNode, headB: ListNode) -> ListNode:
        """
        使用哈希集合记录第一个链表的所有节点
        时间复杂度: O(n + m)
        空间复杂度: O(n)
        """
        # 创建一个集合用于存储第一个链表的节点
        visited = set()
        
        # 遍历第一个链表，将所有节点加入集合
        current = headA
        while current:
            visited.add(current)
            current = current.next
        
        # 遍历第二个链表，检查节点是否在集合中
        current = headB
        while current:
            if current in visited:
                return current
            current = current.next
        
        return None
    
    # 方法4: 暴力破解法（不推荐，时间复杂度高）
    def getIntersectionNodeBruteForce(self, headA: ListNode, headB: ListNode) -> ListNode:
        """
        暴力破解法 - 对每个节点进行比较
        时间复杂度: O(n * m)
        空间复杂度: O(1)
        """
        # 遍历第一个链表的每个节点
        while headA:
            # 对于每个节点，遍历第二个链表查找匹配
            currentB = headB
            while currentB:
                if headA == currentB:
                    return headA
                currentB = currentB.next
            headA = headA.next
        
        return None
    
    # 辅助方法: 计算链表长度
    def getLength(self, head: ListNode) -> int:
        length = 0
        current = head
        while current:
            length += 1
            current = current.next
        return length

# 辅助函数：构建链表
# nums: 链表节点的值列表
# return: (链表头节点, 链表尾节点)
def build_list(nums):
    if not nums:
        return None, None
    
    head = ListNode(nums[0])
    current = head
    
    for num in nums[1:]:
        current.next = ListNode(num)
        current = current.next
    
    return head, current

# 辅助函数：创建相交的链表
# numsA: 链表A独有的部分
# numsB: 链表B独有的部分
# numsCommon: 共同部分
# return: (链表A头节点, 链表B头节点)
def create_intersecting_lists(numsA, numsB, numsCommon):
    # 构建链表A的独有部分
    headA, tailA = build_list(numsA)
    # 构建链表B的独有部分
    headB, tailB = build_list(numsB)
    # 构建共同部分
    commonHead, commonTail = build_list(numsCommon)
    
    # 连接独有部分和共同部分
    if tailA:
        tailA.next = commonHead
    else:
        headA = commonHead
    
    if tailB:
        tailB.next = commonHead
    else:
        headB = commonHead
    
    return headA, headB

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
    
    # 测试用例1: 相交于节点值为8的节点
    # listA: 4->1->8->4->5
    # listB: 5->6->1->8->4->5
    headA1, headB1 = create_intersecting_lists([4, 1], [5, 6, 1], [8, 4, 5])
    print("测试用例1:")
    print("链表A: ")
    print_list(headA1)
    print("链表B: ")
    print_list(headB1)
    
    # 测试双指针法
    intersection1 = solution.getIntersectionNode(headA1, headB1)
    print(f"双指针法相交节点值: {intersection1.val if intersection1 else 'None'}")
    
    # 测试用例2: 相交于节点值为2的节点
    # listA: 1->9->1->2->4
    # listB: 3->2->4
    headA2, headB2 = create_intersecting_lists([1, 9, 1], [3], [2, 4])
    print("\n测试用例2:")
    print("链表A: ")
    print_list(headA2)
    print("链表B: ")
    print_list(headB2)
    
    # 测试长度差法
    intersection2 = solution.getIntersectionNodeByLength(headA2, headB2)
    print(f"长度差法相交节点值: {intersection2.val if intersection2 else 'None'}")
    
    # 测试用例3: 不相交
    # listA: 2->6->4
    # listB: 1->5
    headA3, headB3 = create_intersecting_lists([2, 6, 4], [1, 5], [])
    print("\n测试用例3:")
    print("链表A: ")
    print_list(headA3)
    print("链表B: ")
    print_list(headB3)
    
    # 测试哈希集合法
    intersection3 = solution.getIntersectionNodeByHash(headA3, headB3)
    print(f"哈希集合法相交节点值: {intersection3.val if intersection3 else 'None'}")
    
    # 测试用例4: 链表A为空
    headA4, headB4 = None, build_list([1, 2, 3])[0]
    print("\n测试用例4:")
    print("链表A: 空链表")
    print("链表B: ")
    print_list(headB4)
    
    intersection4 = solution.getIntersectionNode(headA4, headB4)
    print(f"相交节点值: {intersection4.val if intersection4 else 'None'}")
    
    # 测试用例5: 相交于头节点
    headA5, headB5 = create_intersecting_lists([], [], [1, 2, 3])
    print("\n测试用例5:")
    print("链表A: ")
    print_list(headA5)
    print("链表B: ")
    print_list(headB5)
    
    intersection5 = solution.getIntersectionNode(headA5, headB5)
    print(f"相交节点值: {intersection5.val if intersection5 else 'None'}")

# 运行主函数
if __name__ == "__main__":
    main()

'''
* 题目扩展：LeetCode 160. 相交链表
* 来源：LeetCode、LintCode、牛客网、剑指Offer

* 题目描述：
给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。

* 解题思路：
1. 双指针法（浪漫相遇法）：
   - 两个指针分别从两个链表的头部开始遍历
   - 当一个指针到达链表末尾时，转向另一个链表的头部继续遍历
   - 如果两个链表相交，两个指针最终会在相交点相遇
   - 如果不相交，两个指针最终都会变为null
2. 计算长度差法：
   - 计算两个链表的长度
   - 让较长的链表先走长度差步
   - 然后同时遍历两个链表，找到第一个相同的节点
3. 使用哈希集合：
   - 遍历第一个链表，将所有节点存入集合
   - 遍历第二个链表，检查节点是否在集合中
4. 暴力破解法：
   - 对第一个链表的每个节点，遍历第二个链表查找匹配
   - 时间复杂度较高，不推荐

* 时间复杂度：
- 双指针法：O(n + m)，其中n和m是两个链表的长度
- 长度差法：O(n + m)
- 哈希集合法：O(n + m)
- 暴力破解法：O(n * m)

* 空间复杂度：
- 双指针法：O(1)
- 长度差法：O(1)
- 哈希集合法：O(n)
- 暴力破解法：O(1)

* 最优解：双指针法，时间复杂度O(n + m)，空间复杂度O(1)，实现优雅

* 工程化考量：
1. 注意边界条件处理：空链表的情况
2. 相交节点是节点引用相同，不是节点值相同
3. 双指针法实现简洁，但需要理解其原理
4. 长度差法直观易懂，容易实现

* 与机器学习等领域的联系：
1. 链表相交问题在图算法中有应用
2. 哈希集合的使用在数据去重、查找等场景中很常见
3. 双指针技巧在滑动窗口等算法中广泛使用
4. 时间空间复杂度的权衡是算法设计的重要考量

* 语言特性差异：
Python: 无需手动管理内存，使用is比较对象引用
Java: 使用==比较对象引用
C++: 使用指针比较，需要注意空指针问题

* 算法深度分析：
相交链表问题是一个经典的链表操作问题，主要考察对链表特性和指针操作的理解。双指针法是一个非常优雅的解法，其核心思想是让两个指针分别走完"自己的链表+对方的链表"，这样如果有相交，两个指针会在相交点相遇；如果没有相交，两个指针最终都会走完两个链表的长度而同时变为null。

从数学角度证明双指针法的正确性：假设链表A的长度为a+c，链表B的长度为b+c，其中c是共同部分的长度。指针pA走完链表A后转向链表B，指针pB走完链表B后转向链表A。当pA走了a+c+b步，pB走了b+c+a步时，如果有相交，它们会在相交点相遇；如果没有相交(c=0)，它们会同时到达null。

在实际应用中，相交链表的概念可以类比于数据结构中的共享子结构，如共享内存块、共享子树等。理解这个问题有助于处理更复杂的数据结构和算法问题。
'''