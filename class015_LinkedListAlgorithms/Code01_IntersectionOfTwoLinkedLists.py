# 返回两个无环链表相交的第一个节点
# 测试链接 : https://leetcode.cn/problems/intersection-of-two-linked-lists/

# 链表节点定义
class ListNode:
    # 初始化节点
    # val: 节点值
    # next: 指向下一个节点的指针，默认为None
    def __init__(self, x):
        self.val = x
        self.next = None

# 解决方案类
class Solution:
    # 查找两个链表相交的第一个节点
    # 方法：双指针法，先计算长度差，再同步移动
    # 时间复杂度：O(m+n)，其中m和n分别是两个链表的长度
    # 空间复杂度：O(1)，只使用常数额外空间
    # 参数：
    #   h1 - 第一个链表的头节点
    #   h2 - 第二个链表的头节点
    # 返回值：如果相交返回相交节点，否则返回None
    def getIntersectionNode(self, h1, h2):
        # 边界条件检查：如果任一链表为空，直接返回None
        if h1 is None or h2 is None:
            return None
        
        # 初始化两个指针分别指向两个链表的头节点
        a, b = h1, h2
        # 计算两个链表的长度差
        diff = 0
        
        # 遍历第一个链表到最后一个节点，同时计算长度
        while a.next is not None:
            a = a.next
            diff += 1
            
        # 遍历第二个链表到最后一个节点，同时计算长度差
        while b.next is not None:
            b = b.next
            diff -= 1
            
        # 如果两个链表的最后一个节点不相同，说明不相交
        if a != b:
            return None
            
        # 确定哪个链表更长，将a指向长链表的头节点，b指向短链表的头节点
        if diff >= 0:
            a, b = h1, h2
        else:
            a, b = h2, h1
            
        # 取长度差的绝对值
        diff = abs(diff)
        # 让长链表先走diff步，使得两个链表剩余长度相等
        while diff > 0:
            a = a.next
            diff -= 1
            
        # 两个链表同时移动，直到相遇或到达末尾
        while a != b:
            a = a.next
            b = b.next
            
        # 返回相交节点（如果没相交则返回None）
        return a

'''
题目扩展：LeetCode 160. 相交链表
来源：LeetCode、牛客网、剑指Offer等各大算法平台
链接：https://leetcode.cn/problems/intersection-of-two-linked-lists/

题目描述：
给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。
如果两个链表不存在相交节点，返回 null。

解题思路：
1. 先遍历两个链表，计算长度差
2. 让长链表先走差值步
3. 两个链表同时移动，直到相遇

时间复杂度：O(m+n) - 需要遍历两个链表
空间复杂度：O(1) - 只使用常数额外空间
是否最优解：是

工程化考量：
1. 边界情况处理：空链表、无交点
2. 异常处理：输入参数校验
3. 代码可读性：变量命名清晰

与机器学习等领域的联系：
1. 在图神经网络中，链表结构可以看作特殊的图结构
2. 相交检测类似于图中寻找公共节点的问题

语言特性差异：
Java: 对象引用比较使用 ==
C++: 需要比较指针地址
Python: 比较节点对象的id

极端输入场景：
1. 空链表
2. 单节点链表
3. 非常长的链表
4. 无交点链表

测试用例示例：
# 创建相交链表示例
# headA = ListNode(4)
# headA.next = ListNode(1)
# headA.next.next = ListNode(8)
# headA.next.next.next = ListNode(4)
# headA.next.next.next.next = ListNode(5)
# 
# headB = ListNode(5)
# headB.next = ListNode(6)
# headB.next.next = ListNode(1)
# headB.next.next.next = headA.next.next  # 相交于节点8
# 
# solution = Solution()
# result = solution.getIntersectionNode(headA, headB)
# print(f"Intersected at {result.val}" if result else "No intersection")
'''