# 删除排序链表中的重复元素（进阶版）
# 测试链接：https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/

# 提交时不要提交这个类
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 提交如下的方法
class Solution:
    def deleteDuplicates(self, head: ListNode) -> ListNode:
        """
        删除排序链表中的重复元素 II（删除所有重复元素）
        
        解题思路：
        1. 使用虚拟头节点简化边界处理
        2. 使用双指针，一个指针指向当前不重复的最后一个节点
        3. 另一个指针遍历链表，检测重复区间
        4. 跳过所有重复节点，只保留不重复的节点
        
        时间复杂度：O(n) - n 是链表节点数量
        空间复杂度：O(1) - 只使用常数额外空间
        是否最优解：是
        """
        if head is None or head.next is None:
            return head
        
        # 创建虚拟头节点
        dummy = ListNode(0)
        dummy.next = head
        
        prev = dummy  # 前一个不重复节点
        current = head  # 当前遍历节点
        
        while current is not None:
            # 检测当前节点是否重复
            duplicate = False
            
            # 跳过所有重复节点
            while current is not None and current.next is not None and current.val == current.next.val:
                duplicate = True
                current = current.next
            
            if duplicate:
                # 当前节点是重复节点，跳过整个重复区间
                prev.next = current.next if current is not None else None
            else:
                # 当前节点不是重复节点，移动prev指针
                prev = prev.next if prev is not None else None
            
            current = current.next if current is not None else None
        
        return dummy.next
    
    def deleteDuplicatesKeepOne(self, head: ListNode) -> ListNode:
        """
        删除排序链表中的重复元素（保留一个重复元素）
        
        解题思路：
        1. 使用单个指针遍历链表
        2. 如果当前节点与下一个节点值相同，跳过下一个节点
        3. 否则移动指针继续遍历
        
        时间复杂度：O(n) - n 是链表节点数量
        空间复杂度：O(1) - 只使用常数额外空间
        是否最优解：是
        """
        if head is None:
            return None
        
        current = head
        
        while current is not None and current.next is not None:
            if current.val == current.next.val:
                # 跳过重复节点
                current.next = current.next.next
            else:
                current = current.next
        
        return head

'''
题目扩展：LeetCode 82. 删除排序链表中的重复元素 II
来源：LeetCode、牛客网、剑指Offer等各大算法平台

题目描述：
给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，
只留下不同的数字 。返回已排序的链表。

解题思路：
方法一：虚拟头节点 + 双指针
1. 使用虚拟头节点简化边界处理
2. 使用双指针检测重复区间
3. 跳过整个重复区间，只保留不重复节点

方法二：递归解法
1. 递归处理链表
2. 如果当前节点重复，跳过所有重复节点
3. 否则递归处理剩余部分

时间复杂度：O(n) - n 是链表节点数量
空间复杂度：O(1) - 迭代法；O(n) - 递归法
是否最优解：迭代法是最优解

工程化考量：
1. 边界情况处理：空链表、单节点链表、全重复链表
2. 异常处理：输入参数校验
3. 代码可读性：清晰的变量命名和注释

与机器学习等领域的联系：
1. 在数据清洗中，删除重复数据是常见操作
2. 在特征工程中，需要处理重复特征值
3. 在时间序列分析中，处理重复时间点

语言特性差异：
Java: 垃圾回收自动管理内存
C++: 需要手动管理删除的节点内存
Python: 垃圾回收自动管理内存

极端输入场景：
1. 空链表
2. 单节点链表
3. 全相同元素的链表
4. 已排序的链表
5. 未排序的链表（需要先排序）

反直觉但关键的设计：
1. 虚拟头节点：简化头节点可能被删除的情况
2. 重复区间检测：需要检测整个重复区间而非单个节点
3. 指针更新：正确更新prev指针的位置

工程选择依据：
1. 可维护性：代码结构清晰，易于理解和修改
2. 性能：时间复杂度最优，空间复杂度常数级
3. 鲁棒性：处理各种边界情况

异常防御：
1. 空指针检查
2. 链表长度检查
3. 重复检测逻辑验证

单元测试要点：
1. 测试空链表
2. 测试单节点链表
3. 测试全重复链表
4. 测试无重复链表
5. 测试混合情况

性能优化策略：
1. 一次遍历完成删除操作
2. 原地操作，不创建新节点
3. 使用虚拟头节点避免特殊判断

算法安全与业务适配：
1. 避免内存泄漏：正确处理删除的节点
2. 异常捕获：捕获可能的运行时异常
3. 处理溢出：处理大链表情况

与标准库实现的对比：
1. 标准库通常不提供链表去重功能
2. 需要自定义实现特定需求
3. 边界处理更加细致

笔试解题效率：
1. 模板化：掌握链表去重的通用模板
2. 边界处理：熟练处理各种边界情况
3. 代码简洁：使用虚拟头节点简化代码

面试深度表达：
1. 解释设计思路：为什么使用虚拟头节点
2. 分析复杂度：时间和空间复杂度分析
3. 讨论变种：保留一个重复元素 vs 删除所有重复元素
4. 实际应用：去重操作在工程中的应用
'''