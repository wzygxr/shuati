import time
import random
from typing import TypeVar, Generic, List

T = TypeVar('T')

class MinStack:
    """
    最小栈 - Python实现
    
    题目描述：
    设计一个支持 push，pop，top 操作，并能在常数时间内检索到最小元素的栈。
    
    测试链接：https://leetcode.cn/problems/min-stack/
    题目来源：LeetCode
    难度：简单
    
    核心算法：双栈法（一个存储数据，一个存储最小值）
    
    解题思路：
    使用两个栈来实现最小栈：
    1. data_stack：普通的栈，用于存储所有元素
    2. min_stack：辅助栈，栈顶始终存储当前栈中的最小值
    
    具体操作：
    - push(x): 将x压入data_stack，如果min_stack为空或x<=min_stack栈顶，则也压入min_stack
    - pop(): 弹出data_stack栈顶，如果弹出的值等于min_stack栈顶，则也弹出min_stack栈顶
    - top(): 返回data_stack栈顶元素
    - getMin(): 返回min_stack栈顶元素（当前最小值）
    
    时间复杂度分析：
    - 所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    - O(n) - 需要两个栈来存储数据
    
    Python语言特性：
    - 使用列表实现栈操作
    - 使用类型注解提高代码可读性
    - 使用异常处理机制
    - 支持泛型编程
    """
    
    def __init__(self):
        """初始化最小栈"""
        self.data_stack = []  # 数据栈
        self.min_stack = []   # 最小值栈
    
    def push(self, x: T) -> None:
        """
        压入元素
        
        Args:
            x: 要压入的元素
            
        Raises:
            无显式异常抛出
        """
        self.data_stack.append(x)
        # 如果最小值栈为空，或者x小于等于当前最小值，则压入最小值栈
        if not self.min_stack or x <= self.min_stack[-1]:
            self.min_stack.append(x)
    
    def pop(self) -> None:
        """
        弹出栈顶元素
        
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("pop from empty stack")
        
        popped = self.data_stack.pop()
        # 如果弹出的是当前最小值，则也从最小值栈弹出
        if popped == self.min_stack[-1]:
            self.min_stack.pop()
    
    def top(self) -> T:
        """
        获取栈顶元素
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("top from empty stack")
        return self.data_stack[-1]
    
    def getMin(self) -> T:
        """
        获取栈中的最小值
        
        Returns:
            最小值
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.min_stack:
            raise IndexError("getMin from empty stack")
        return self.min_stack[-1]
    
    def is_empty(self) -> bool:
        """
        检查栈是否为空
        
        Returns:
            如果栈为空返回True，否则返回False
        """
        return len(self.data_stack) == 0
    
    def size(self) -> int:
        """
        获取栈的大小
        
        Returns:
            栈中元素的数量
        """
        return len(self.data_stack)
    
    def __str__(self) -> str:
        """返回栈的字符串表示"""
        return f"MinStack(data_stack={self.data_stack}, min_stack={self.min_stack})"
    
    def __len__(self) -> int:
        """返回栈的大小"""
        return len(self.data_stack)


class ThreadSafeMinStack:
    """
    线程安全的最小栈实现
    使用锁机制保证线程安全
    """
    
    def __init__(self):
        """初始化线程安全最小栈"""
        import threading
        self.data_stack = []
        self.min_stack = []
        self.lock = threading.Lock()
    
    def push(self, x: T) -> None:
        """线程安全的压入操作"""
        with self.lock:
            self.data_stack.append(x)
            if not self.min_stack or x <= self.min_stack[-1]:
                self.min_stack.append(x)
    
    def pop(self) -> None:
        """线程安全的弹出操作"""
        with self.lock:
            if not self.data_stack:
                raise IndexError("pop from empty stack")
            
            popped = self.data_stack.pop()
            if popped == self.min_stack[-1]:
                self.min_stack.pop()
    
    def top(self) -> T:
        """线程安全的获取栈顶操作"""
        with self.lock:
            if not self.data_stack:
                raise IndexError("top from empty stack")
            return self.data_stack[-1]
    
    def getMin(self) -> T:
        """线程安全的获取最小值操作"""
        with self.lock:
            if not self.min_stack:
                raise IndexError("getMin from empty stack")
            return self.min_stack[-1]
    
    def is_empty(self) -> bool:
        """线程安全的检查空栈操作"""
        with self.lock:
            return len(self.data_stack) == 0
    
    def size(self) -> int:
        """线程安全的获取大小操作"""
        with self.lock:
            return len(self.data_stack)


def test_min_stack():
    """单元测试函数"""
    print("=== 最小栈单元测试 ===")
    
    # 测试用例1：基本操作
    stack1 = MinStack()
    stack1.push(-2)
    stack1.push(0)
    stack1.push(-3)
    print("测试用例1 - 压入[-2, 0, -3]")
    print(f"当前最小值: {stack1.getMin()}")  # 期望: -3
    stack1.pop()
    print(f"弹出后栈顶: {stack1.top()}")     # 期望: 0
    print(f"当前最小值: {stack1.getMin()}")  # 期望: -2
    
    # 测试用例2：重复最小值
    stack2 = MinStack()
    stack2.push(5)
    stack2.push(3)
    stack2.push(3)
    stack2.push(7)
    print("\n测试用例2 - 压入[5, 3, 3, 7]")
    print(f"当前最小值: {stack2.getMin()}")  # 期望: 3
    stack2.pop()  # 弹出7
    print(f"弹出7后最小值: {stack2.getMin()}")  # 期望: 3
    stack2.pop()  # 弹出3
    print(f"弹出3后最小值: {stack2.getMin()}")  # 期望: 3
    stack2.pop()  # 弹出3
    print(f"弹出3后最小值: {stack2.getMin()}")  # 期望: 5
    
    # 测试用例3：边界情况 - 单个元素
    stack3 = MinStack()
    stack3.push(10)
    print("\n测试用例3 - 单个元素10")
    print(f"栈顶: {stack3.top()}")     # 期望: 10
    print(f"最小值: {stack3.getMin()}")  # 期望: 10
    
    # 测试用例4：边界情况 - 空栈异常处理
    stack4 = MinStack()
    try:
        stack4.pop()
    except IndexError as e:
        print(f"\n测试用例4 - 空栈pop操作抛出异常: {e}")
    
    # 测试用例5：字符串类型测试
    stack5 = MinStack()
    stack5.push("banana")
    stack5.push("apple")
    stack5.push("cherry")
    print(f"\n测试用例5 - 字符串栈最小值: {stack5.getMin()}")  # 期望: "apple"


def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    
    stack = MinStack()
    n = 100000
    
    start_time = time.time()
    
    # 压入n个随机数
    for _ in range(n):
        stack.push(random.randint(0, 1000))
    
    # 交替进行getMin和pop操作
    for _ in range(n):
        stack.getMin()
        stack.pop()
    
    end_time = time.time()
    
    print(f"数据规模: {n}个元素")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f}ms")


def performance_comparison():
    """性能对比测试：普通栈 vs 最小栈"""
    print("\n=== 性能对比测试 ===")
    
    n = 100000
    
    # 测试普通栈
    normal_stack = []
    start_time1 = time.time()
    
    for _ in range(n):
        normal_stack.append(random.randint(0, 1000))
    
    for _ in range(n):
        normal_stack.pop()
    
    end_time1 = time.time()
    
    # 测试最小栈
    min_stack = MinStack()
    start_time2 = time.time()
    
    for _ in range(n):
        min_stack.push(random.randint(0, 1000))
    
    for _ in range(n):
        min_stack.getMin()
        min_stack.pop()
    
    end_time2 = time.time()
    
    normal_time = (end_time1 - start_time1) * 1000
    min_stack_time = (end_time2 - start_time2) * 1000
    
    print(f"普通栈操作时间: {normal_time:.2f}ms")
    print(f"最小栈操作时间: {min_stack_time:.2f}ms")
    print(f"性能开销比例: {min_stack_time / normal_time:.2f}")


def main():
    """主函数"""
    # 运行单元测试
    test_min_stack()
    
    # 运行性能测试
    performance_test()
    
    # 运行性能对比测试
    performance_comparison()
    
    print("\n=== 最小栈算法验证完成 ===")


if __name__ == "__main__":
    main()