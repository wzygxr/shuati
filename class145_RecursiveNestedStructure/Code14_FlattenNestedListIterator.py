# LeetCode 341. Flatten Nested List Iterator (扁平化嵌套列表迭代器)
# 来源: LeetCode
# 网址: https://leetcode.cn/problems/flatten-nested-list-iterator/
# 
# 题目描述:
# 给你一个嵌套的整数列表 nestedList 。每个元素要么是一个整数，要么是一个列表；该列表的元素也可能是整数或者是其他列表。
# 请你实现一个迭代器将其扁平化，使之能够遍历这个列表中的所有整数。
# 实现扁平迭代器接口 NestedIterator ：
# - NestedIterator(List<NestedInteger> nestedList) 用嵌套列表 nestedList 初始化迭代器。
# - int next() 返回嵌套列表的下一个整数。
# - boolean hasNext() 如果仍然存在待迭代的整数，返回 true ；否则，返回 false 。
# 
# 示例:
# 输入: nestedList = [[1,1],2,[1,1]]
# 输出: [1,1,2,1,1]
# 解释: 通过重复调用 next() 直到 hasNext() 返回 false，next() 返回的元素的顺序应该是: [1,1,2,1,1]。
# 
# 解题思路:
# 方法1: 预计算所有整数并存储在列表中
# 方法2: 使用栈进行惰性计算（惰性迭代器）
# 这里使用方法2，更节省空间，符合迭代器的惰性计算原则
# 
# 时间复杂度:
# - 构造函数: O(k)，其中k是嵌套列表中整数的总数量
# - next(): O(1)
# - hasNext(): O(1)，最坏情况下可能为O(k)，但均摊分析仍为O(1)
# 
# 空间复杂度: O(d)，其中d是嵌套列表的最大深度

from typing import List, Iterator

# 这是题目给定的接口，在实际提交时不需要实现
class NestedInteger:
    def isInteger(self) -> bool:
        """@return True if this NestedInteger holds a single integer, rather than a nested list."""
        pass
    
    def getInteger(self) -> int:
        """@return the single integer that this NestedInteger holds, if it holds a single integer
        Return None if this NestedInteger holds a nested list
        """
        pass
    
    def getList(self) -> List['NestedInteger']:
        """@return the nested list that this NestedInteger holds, if it holds a nested list
        Return None if this NestedInteger holds a single integer
        """
        pass

# 为了测试创建的示例实现类
class NestedIntegerImpl(NestedInteger):
    def __init__(self, value=None):
        self.value = value
        self.list = [] if value is None else None
    
    # 向列表中添加元素
    def add(self, ni):
        if self.list is None:
            self.list = []
            self.value = None
        self.list.append(ni)
    
    def isInteger(self) -> bool:
        return self.value is not None
    
    def getInteger(self) -> int:
        return self.value
    
    def getList(self) -> List['NestedInteger']:
        return self.list

class NestedIterator:
    def __init__(self, nestedList: List['NestedInteger']):
        """
        使用栈进行惰性计算的迭代器初始化
        
        Args:
            nestedList: 嵌套的整数列表
        """
        # 使用栈存储嵌套列表的迭代器，以便回溯
        self.stack = []
        # 指向下一个要返回的整数
        self.next_val = None
        
        # 将顶层列表的迭代器压入栈中
        if nestedList and len(nestedList) > 0:
            self.stack.append(iter(nestedList))
        
        # 预先寻找第一个整数
        self.next_val = self._find_next_integer()
    
    def next(self) -> int:
        """返回嵌套列表的下一个整数"""
        if not self.hasNext():
            raise StopIteration("No more integers in the nested list")
        
        # 保存当前的next_val
        result = self.next_val
        # 寻找下一个整数
        self.next_val = self._find_next_integer()
        return result
    
    def hasNext(self) -> bool:
        """检查是否还有更多整数"""
        return self.next_val is not None
    
    def _find_next_integer(self):
        """
        查找下一个整数
        使用栈进行深度优先搜索，直到找到一个整数或者栈为空
        """
        while self.stack:
            current_iterator = self.stack[-1]  # 栈顶元素
            
            try:
                # 获取下一个元素
                next_nested = next(current_iterator)
            except StopIteration:
                # 如果当前迭代器已经遍历完，则弹出栈顶
                self.stack.pop()
                continue
            
            # 如果是整数，直接返回
            if next_nested.isInteger():
                return next_nested.getInteger()
            else:
                # 如果是列表，将其迭代器压入栈中
                nested_list = next_nested.getList()
                if nested_list and len(nested_list) > 0:
                    self.stack.append(iter(nested_list))
        
        # 栈为空，没有更多整数
        return None

"""
方法2: 预计算所有整数并存储在列表中
这是一个更简单但可能占用更多空间的实现
"""
class PreComputeNestedIterator:
    def __init__(self, nestedList: List['NestedInteger']):
        self.flattened_list = []
        self.index = 0
        # 预先递归展平整个嵌套列表
        self._flatten(nestedList)
    
    def _flatten(self, nestedList: List['NestedInteger']):
        for ni in nestedList:
            if ni.isInteger():
                self.flattened_list.append(ni.getInteger())
            else:
                self._flatten(ni.getList())
    
    def hasNext(self) -> bool:
        return self.index < len(self.flattened_list)
    
    def next(self) -> int:
        if not self.hasNext():
            raise StopIteration
        result = self.flattened_list[self.index]
        self.index += 1
        return result

# 测试函数
def test_nested_iterator():
    # 测试用例1: [[1,1],2,[1,1]]
    test_case1 = []
    
    # [1,1]
    list1 = NestedIntegerImpl()
    list1.add(NestedIntegerImpl(1))
    list1.add(NestedIntegerImpl(1))
    test_case1.append(list1)
    
    # 2
    test_case1.append(NestedIntegerImpl(2))
    
    # [1,1]
    list2 = NestedIntegerImpl()
    list2.add(NestedIntegerImpl(1))
    list2.add(NestedIntegerImpl(1))
    test_case1.append(list2)
    
    print("测试用例1 (惰性迭代器):")
    print("输入: [[1,1],2,[1,1]]")
    iterator1 = NestedIterator(test_case1)
    result = []
    while iterator1.hasNext():
        result.append(iterator1.next())
    print(f"输出: {result}")
    print("期望: [1, 1, 2, 1, 1]")
    print()
    
    # 重置测试用例
    test_case1_again = []
    list1_again = NestedIntegerImpl()
    list1_again.add(NestedIntegerImpl(1))
    list1_again.add(NestedIntegerImpl(1))
    test_case1_again.append(list1_again)
    test_case1_again.append(NestedIntegerImpl(2))
    list2_again = NestedIntegerImpl()
    list2_again.add(NestedIntegerImpl(1))
    list2_again.add(NestedIntegerImpl(1))
    test_case1_again.append(list2_again)
    
    print("测试用例1 (预计算迭代器):")
    pre_iterator1 = PreComputeNestedIterator(test_case1_again)
    result = []
    while pre_iterator1.hasNext():
        result.append(pre_iterator1.next())
    print(f"输出: {result}")
    print("期望: [1, 1, 2, 1, 1]")
    print()
    
    # 测试用例2: [1,[4,[6]]]
    test_case2 = []
    test_case2.append(NestedIntegerImpl(1))
    
    outer_list = NestedIntegerImpl()
    outer_list.add(NestedIntegerImpl(4))
    
    inner_list = NestedIntegerImpl()
    inner_list.add(NestedIntegerImpl(6))
    outer_list.add(inner_list)
    
    test_case2.append(outer_list)
    
    print("测试用例2 (惰性迭代器):")
    print("输入: [1,[4,[6]]]")
    iterator2 = NestedIterator(test_case2)
    result = []
    while iterator2.hasNext():
        result.append(iterator2.next())
    print(f"输出: {result}")
    print("期望: [1, 4, 6]")

if __name__ == "__main__":
    test_nested_iterator()