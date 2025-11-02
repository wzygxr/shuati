"""  
最小栈与最大栈专题 - 辅助栈思想的综合应用

核心思想:
使用辅助栈维护每个位置对应的最值(最小值或最大值)
这是一种空间换时间的经典策略,确保获取最值的时间复杂度为O(1)

适用场景:
1. 需要在O(1)时间内获取栈中最小值/最大值
2. 需要在栈操作的同时维护某种单调性
3. 需要快速查询历史最值信息

题型识别关键词:
- "O(1)时间获取最小值/最大值"
- "设计支持xxx操作的栈"
- "维护栈中的最值"

核心技巧总结:
1. 双栈法:数据栈 + 辅助栈(最值栈)
2. 辅助栈同步更新:每次push/pop时同时更新辅助栈
3. 空间优化:辅助栈可以只存储真正的最值(需要额外判断逻辑)

时间复杂度:
- push: O(1)
- pop: O(1)
- top: O(1)
- getMin/getMax: O(1)

空间复杂度:
O(n) - 需要额外的辅助栈存储最值信息

工程化考量:
1. 异常处理:空栈时调用pop/top/getMin应抛出异常
2. 线程安全:多线程环境下需要加锁
3. 泛型支持:可以扩展为支持泛型的栈
4. 容量限制:可以添加容量限制防止栈溢出

与其他算法的联系:
1. 单调栈:辅助栈思想的扩展应用
2. 滑动窗口最大值:使用单调队列实现,思想类似
3. 动态规划:维护历史状态信息的思想相通

测试链接 : https://leetcode.cn/problems/min-stack/
"""


class MinStack1:
    """
    最小栈实现方式一：使用两个栈
    使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(n) - 需要两个栈来存储元素
    """

    def __init__(self):
        self.data = []  # 数据栈
        self.min_stack = []  # 辅助栈，存储每个位置对应的最小值

    def push(self, val: int) -> None:
        """将元素val推入堆栈"""
        self.data.append(val)
        # 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)
        else:
            self.min_stack.append(self.min_stack[-1])

    def pop(self) -> None:
        """删除堆栈顶部的元素"""
        self.data.pop()
        self.min_stack.pop()

    def top(self) -> int:
        """获取堆栈顶部的元素"""
        return self.data[-1]

    def getMin(self) -> int:
        """获取堆栈中的最小元素"""
        return self.min_stack[-1]


class MinStack2:
    """
    最小栈实现方式二：使用数组
    使用数组实现栈，一个数组存储数据，另一个数组存储每个位置对应的最小值
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(n) - 需要两个数组来存储元素
    """

    def __init__(self):
        # leetcode的数据在测试时，同时在栈里的数据不超过这个值
        # 这是几次提交实验出来的，哈哈
        # 如果leetcode补测试数据了，超过这个量导致出错，就调大
        self.MAXN = 8001
        self.data = [0] * self.MAXN
        self.min_vals = [0] * self.MAXN
        self.size = 0

    def push(self, val: int) -> None:
        """将元素val推入堆栈"""
        self.data[self.size] = val
        if self.size == 0 or val <= self.min_vals[self.size - 1]:
            self.min_vals[self.size] = val
        else:
            self.min_vals[self.size] = self.min_vals[self.size - 1]
        self.size += 1

    def pop(self) -> None:
        """删除堆栈顶部的元素"""
        self.size -= 1

    def top(self) -> int:
        """获取堆栈顶部的元素"""
        return self.data[self.size - 1]

    def getMin(self) -> int:
        """获取堆栈中的最小元素"""
        return self.min_vals[self.size - 1]


class MaxStack:
    """
    最大栈
    题目来源：LeetCode 716. 最大栈
    链接：https://leetcode.cn/problems/max-stack/
    
    题目描述：
    设计一个最大栈数据结构，既支持栈操作，又支持查找栈中最大元素。
    实现 MaxStack 类：
    MaxStack() 初始化栈对象
    void push(int x) 将元素 x 压入栈中。
    int pop() 移除栈顶元素并返回这个元素。
    int top() 返回栈顶元素，无需移除。
    int peekMax() 检索并返回栈中最大元素，无需移除。
    int popMax() 检索并返回栈中最大元素，并将其移除。如果有多个最大元素，只要移除 最靠近栈顶 的那个。
    
    解题思路：
    使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最大值。
    每次push操作时，数据栈正常压入元素，辅助栈压入当前元素与之前最大值中的较大者。
    这样辅助栈的栈顶始终是当前栈中的最大值。
    popMax操作时，需要将最大值上面的所有元素暂存到临时栈中，取出最大值后再将临时栈中的元素放回。
    
    时间复杂度分析：
    - push操作：O(1) - 直接压入两个栈
    - pop操作：O(1) - 直接从两个栈弹出
    - top操作：O(1) - 直接返回数据栈栈顶
    - peekMax操作：O(1) - 直接返回辅助栈栈顶
    - popMax操作：O(n) - 最坏情况下需要将所有元素移动到临时栈再移回
    
    空间复杂度分析：
    O(n) - 需要两个栈和一个临时栈来存储元素
    """

    def __init__(self):
        self.data_stack = []  # 数据栈
        self.max_stack = []   # 辅助栈，存储每个位置对应的最大值

    def push(self, x: int) -> None:
        """将元素 x 压入栈中"""
        self.data_stack.append(x)
        # 如果辅助栈为空，或者当前元素大于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if not self.max_stack or x >= self.max_stack[-1]:
            self.max_stack.append(x)
        else:
            self.max_stack.append(self.max_stack[-1])

    def pop(self) -> int:
        """移除栈顶元素并返回这个元素"""
        self.max_stack.pop()
        return self.data_stack.pop()

    def top(self) -> int:
        """返回栈顶元素"""
        return self.data_stack[-1]

    def peekMax(self) -> int:
        """检索并返回栈中最大元素，无需移除"""
        return self.max_stack[-1]

    def popMax(self) -> int:
        """检索并返回栈中最大元素，并将其移除"""
        max_val = self.peekMax()
        temp = []
        # 将最大值上面的元素暂存到临时栈中
        while self.top() != max_val:
            temp.append(self.pop())
        # 弹出最大值
        self.pop()
        # 将临时栈中的元素放回
        while temp:
            self.push(temp.pop())
        return max_val


class MinStack:
    """
    带最小值操作的栈
    题目来源：LintCode 12. 带最小值操作的栈
    链接：https://www.lintcode.com/problem/12/
    
    题目描述：
    实现一个栈, 支持以下操作:
    push(val) 将 val 压入栈
    pop() 将栈顶元素弹出, 并返回这个弹出的元素
    min() 返回栈中元素的最小值
    要求 O(1) 开销，保证栈中没有数字时不会调用 min()
    
    解题思路：
    与最小栈问题相同，使用两个栈实现，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(n) - 需要两个栈来存储元素
    """

    def __init__(self):
        self.stack = []      # 数据栈
        self.min_stack = []   # 辅助栈，存储每个位置对应的最小值

    def push(self, number: int) -> None:
        """将 val 压入栈"""
        self.stack.append(number)
        # 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if not self.min_stack or number <= self.min_stack[-1]:
            self.min_stack.append(number)
        else:
            self.min_stack.append(self.min_stack[-1])

    def pop(self) -> int:
        """将栈顶元素弹出, 并返回这个弹出的元素"""
        self.min_stack.pop()
        return self.stack.pop()

    def min(self) -> int:
        """返回栈中元素的最小值"""
        return self.min_stack[-1]


class MinStackOffer:
    """
    题目3:包含min函数的栈(剑指Offer 30)
    题目来源:剑指Offer 30 / LeetCode 155
    链接:https://leetcode.cn/problems/bao-han-minhan-shu-de-zhan-lcof/
    
    题目描述:
    定义栈的数据结构,请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中,
    调用 min、push 及 pop 的时间复杂度都是 O(1)。
    
    示例:
    MinStack minStack = new MinStack();
    minStack.push(-2);
    minStack.push(0);
    minStack.push(-3);
    minStack.min();   --> 返回 -3.
    minStack.pop();
    minStack.top();      --> 返回 0.
    minStack.min();   --> 返回 -2.
    
    解题思路:
    经典的辅助栈问题,与上述MinStack实现完全相同。
    关键点:辅助栈需要与数据栈同步push和pop,保证辅助栈栈顶始终是当前栈的最小值。
    
    边界场景:
    1. 空栈时调用min/top/pop - 需要异常处理或约定不会发生
    2. 所有元素相同 - 辅助栈每个位置都是该值
    3. 严格递增序列 - 辅助栈所有位置都是首个元素
    4. 严格递减序列 - 辅助栈与数据栈完全相同
    5. 包含整数溢出边界值(float('-inf'), float('inf'))
    
    时间复杂度:O(1) - 所有操作
    空间复杂度:O(n) - 需要辅助栈
    
    是否最优解:是。无法在保证O(1)时间复杂度的前提下进一步优化空间复杂度。
    虽然可以优化辅助栈只存储真正的最小值,但最坏情况下(严格递减序列)空间复杂度仍为O(n)。
    """

    def __init__(self):
        self.data_stack = []  # 数据栈
        self.min_stack = []   # 辅助栈,存储最小值

    def push(self, x: int) -> None:
        self.data_stack.append(x)
        # 如果辅助栈为空,或当前元素小于等于辅助栈栈顶,则压入当前元素
        # 否则压入辅助栈栈顶元素(复制最小值)
        if not self.min_stack or x <= self.min_stack[-1]:
            self.min_stack.append(x)
        else:
            self.min_stack.append(self.min_stack[-1])

    def pop(self) -> None:
        # 两个栈同步弹出
        self.data_stack.pop()
        self.min_stack.pop()

    def top(self) -> int:
        return self.data_stack[-1]

    def min(self) -> int:
        return self.min_stack[-1]


class SortedStack:
    """
    题目4:栈排序(LeetCode 面试题 03.05)
    题目来源:LeetCode 面试题 03.05. 栈排序
    链接:https://leetcode.cn/problems/sort-of-stacks-lcci/
    
    题目描述:
    栈排序。编写程序,对栈进行排序使最小元素位于栈顶。最多只能使用一个其他的临时栈存放数据,
    但不得将元素复制到别的数据结构(如数组)中。该栈支持如下操作:push、pop、peek 和isEmpty。
    当栈为空时,peek 返回 -1。
    
    示例:
    ["SortedStack", "push", "push", "peek", "pop", "peek"]
    [[], [1], [2], [], [], []]
    输出:
    [null,null,null,1,null,2]
    
    解题思路:
    使用两个栈实现,主栈保持有序(栈顶最小),辅助栈用于临时存储。
    push时,将主栈中大于新元素的元素临时移到辅助栈,插入新元素后再移回。
    
    详细步骤:
    1. push(x)时:
       - 将主栈中所有大于x的元素弹出到辅助栈
       - 将x压入主栈
       - 将辅助栈中的元素全部弹回主栈
    2. pop/peek/isEmpty直接操作主栈即可
    
    时间复杂度:
    - push: O(n) - 最坏情况需要移动所有元素
    - pop: O(1)
    - peek: O(1)
    - isEmpty: O(1)
    
    空间复杂度:O(n) - 需要辅助栈
    
    是否最优解:是。在只能使用一个辅助栈的限制下,这是最优解。
    """

    def __init__(self):
        self.main_stack = []  # 主栈,保持有序(栈顶最小)
        self.temp_stack = []  # 辅助栈,临时存储

    def push(self, val: int) -> None:
        # 将主栈中所有大于val的元素临时移到辅助栈
        while self.main_stack and self.main_stack[-1] < val:
            self.temp_stack.append(self.main_stack.pop())
        # 将val压入主栈
        self.main_stack.append(val)
        # 将辅助栈中的元素全部弹回主栈
        while self.temp_stack:
            self.main_stack.append(self.temp_stack.pop())

    def pop(self) -> None:
        if self.main_stack:
            self.main_stack.pop()

    def peek(self) -> int:
        if not self.main_stack:
            return -1
        return self.main_stack[-1]

    def isEmpty(self) -> bool:
        return len(self.main_stack) == 0


class MyQueue:
    """
    题目5:用栈实现队列(LeetCode 232)
    题目来源:LeetCode 232. 用栈实现队列
    链接:https://leetcode.cn/problems/implement-queue-using-stacks/
    
    题目描述:
    请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作(push、pop、peek、empty)。
    
    解题思路:
    使用两个栈:输入栈和输出栈。
    - push操作:直接压入输入栈
    - pop/peek操作:如果输出栈为空,将输入栈所有元素转移到输出栈,然后操作输出栈
    
    核心思想:
    通过两次反转实现FIFO。第一次反转在输入栈,第二次反转在转移到输出栈时。
    
    时间复杂度分析(摊还分析):
    - push: O(1)
    - pop: 摊还O(1) - 单次可能O(n),但每个元素最多被转移一次
    - peek: 摊还O(1)
    - empty: O(1)
    
    空间复杂度:O(n)
    
    是否最优解:是。这是用栈实现队列的标准解法。
    """

    def __init__(self):
        self.in_stack = []   # 输入栈
        self.out_stack = []  # 输出栈

    def push(self, x: int) -> None:
        """将元素压入队列尾部"""
        self.in_stack.append(x)

    def pop(self) -> int:
        """从队列头部移除并返回元素"""
        # 如果输出栈为空,将输入栈所有元素转移到输出栈
        if not self.out_stack:
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())
        return self.out_stack.pop()

    def peek(self) -> int:
        """获取队列头部元素"""
        if not self.out_stack:
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())
        return self.out_stack[-1]

    def empty(self) -> bool:
        """判断队列是否为空"""
        return not self.in_stack and not self.out_stack


class MinStackOptimized:
    """
    题目6:最小栈(空间优化版)
    题目来源:优化实现
    
    题目描述:
    实现最小栈,但优化辅助栈的空间使用。辅助栈只存储真正的最小值,而不是每个位置都存储。
    
    解题思路:
    辅助栈只在遇到新的最小值时才压入。pop时需要判断弹出的是否是最小值,如果是则同步弹出辅助栈。
    
    优化效果:
    - 最好情况(严格递增):辅助栈只有1个元素,空间O(1)
    - 最坏情况(严格递减):辅助栈与数据栈大小相同,空间O(n)
    - 平均情况:辅助栈大小远小于数据栈
    
    时间复杂度:O(1) - 所有操作
    空间复杂度:O(k),k为不同最小值的个数,k <= n
    
    注意事项:
    需要小心处理相等的情况,特别是当栈顶元素等于最小值时的pop操作。
    """

    def __init__(self):
        self.data_stack = []  # 数据栈
        self.min_stack = []   # 辅助栈,只存储最小值

    def push(self, val: int) -> None:
        self.data_stack.append(val)
        # 只在遇到新的最小值(小于等于当前最小值)时才压入辅助栈
        # 注意:这里必须是 <=,不能是 <,否则会漏掉重复的最小值
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)

    def pop(self) -> None:
        # 如果弹出的元素是当前最小值,辅助栈也要弹出
        if self.data_stack[-1] == self.min_stack[-1]:
            self.min_stack.pop()
        self.data_stack.pop()

    def top(self) -> int:
        return self.data_stack[-1]

    def getMin(self) -> int:
        return self.min_stack[-1]


class CustomStack:
    """
    题目7:设计一个支持增量操作的栈(LeetCode 1381)
    题目来源:LeetCode 1381. 设计一个支持增量操作的栈
    链接:https://leetcode.cn/problems/design-a-stack-with-increment-operation/
    
    题目描述:
    请你设计一个支持下述操作的栈:
    - CustomStack(int maxSize):初始化对象,maxSize 为栈的最大容量
    - void push(int x):如果栈未满,则将 x 添加到栈顶
    - int pop():弹出栈顶元素,并返回栈顶的值,如果栈为空则返回 -1
    - void inc(int k, int val):将栈底的 k 个元素的值都增加 val。如果栈中元素总数小于 k,则将所有元素都增加 val
    
    解题思路:
    使用懒惰更新(lazy propagation)的思想。
    维护一个增量数组inc[],inc[i]表示从栈底到第i个位置需要累加的增量。
    - increment操作:只更新inc[k-1]的值,不实际修改栈中元素
    - pop操作:弹出时才将累加的增量应用到元素上,并将增量传递给下一个元素
    
    时间复杂度:
    - push: O(1)
    - pop: O(1)
    - increment: O(1) - 这是关键优化,避免了O(k)的遍历
    
    空间复杂度:O(n) - 需要额外的增量数组
    
    是否最优解:是。通过懒惰更新将increment操作从O(k)优化到O(1)。
    """

    def __init__(self, maxSize: int):
        self.stack = []             # 栈数组
        self.increment_arr = []     # 增量数组,increment[i]表示位置i的累加增量
        self.max_size = maxSize     # 栈的最大容量

    def push(self, x: int) -> None:
        # 如果栈未满,则压入元素
        if len(self.stack) < self.max_size:
            self.stack.append(x)
            self.increment_arr.append(0)

    def pop(self) -> int:
        if not self.stack:
            return -1
        # 计算实际值(原值 + 累加增量)
        idx = len(self.stack) - 1
        result = self.stack[idx] + self.increment_arr[idx]
        # 将当前位置的增量传递给下一个位置(关键步骤)
        if idx > 0:
            self.increment_arr[idx - 1] += self.increment_arr[idx]
        # 弹出栈顶
        self.stack.pop()
        self.increment_arr.pop()
        return result

    def increment(self, k: int, val: int) -> None:
        # 只更新第 min(k, len(stack))-1 个位置的增量
        # 这个增量会在pop时逐层传递
        idx = min(k, len(self.stack)) - 1
        if idx >= 0:
            self.increment_arr[idx] += val


# 测试代码
# 用于验证各个实现的正确性
if __name__ == "__main__":
    # 测试最小栈
    print("=== 测试最小栈 ===")
    min_stack = MinStack()
    min_stack.push(-2)
    min_stack.push(0)
    min_stack.push(-3)
    print(f"当前最小值: {min_stack.min()}")  # -3
    min_stack.pop()
    print(f"栈顶元素: {min_stack.pop()}")  # 0
    print(f"当前最小值: {min_stack.min()}")  # -2
    
    # 测试最大栈
    print("\n=== 测试最大栈 ===")
    max_stack = MaxStack()
    max_stack.push(5)
    max_stack.push(1)
    max_stack.push(5)
    print(f"栈顶元素: {max_stack.top()}")  # 5
    print(f"弹出最大值: {max_stack.popMax()}")  # 5
    print(f"栈顶元素: {max_stack.top()}")  # 1
    print(f"当前最大值: {max_stack.peekMax()}")  # 5
    print(f"弹出栈顶: {max_stack.pop()}")  # 1
    print(f"当前最大值: {max_stack.peekMax()}")  # 5
    
    # 测试排序栈
    print("\n=== 测试排序栈 ===")
    sorted_stack = SortedStack()
    sorted_stack.push(1)
    sorted_stack.push(2)
    print(f"栈顶(最小值): {sorted_stack.peek()}")  # 1
    sorted_stack.pop()
    print(f"弹出后栈顶: {sorted_stack.peek()}")  # 2
    
    # 测试用栈实现队列
    print("\n=== 测试用栈实现队列 ===")
    queue = MyQueue()
    queue.push(1)
    queue.push(2)
    print(f"队列头部: {queue.peek()}")  # 1
    print(f"弹出队列头部: {queue.pop()}")  # 1
    print(f"队列是否为空: {queue.empty()}")  # False
    
    # 测试支持增量操作的栈
    print("\n=== 测试支持增量操作的栈 ===")
    custom_stack = CustomStack(3)
    custom_stack.push(1)
    custom_stack.push(2)
    print(f"弹出: {custom_stack.pop()}")  # 2
    custom_stack.push(2)
    custom_stack.push(3)
    custom_stack.push(4)
    custom_stack.increment(5, 100)
    custom_stack.increment(2, 100)
    print(f"弹出: {custom_stack.pop()}")  # 103
    print(f"弹出: {custom_stack.pop()}")  # 202
    print(f"弹出: {custom_stack.pop()}")  # 201
    print(f"弹出: {custom_stack.pop()}")  # -1
    
    print("\n所有测试完成!")

# 主函数运行
if __name__ == "__main__":
    # 测试最小栈
    print("=== 测试最小栈 ===")
    min_stack = MinStack()
    min_stack.push(-2)
    min_stack.push(0)
    min_stack.push(-3)
    print(f"当前最小值: {min_stack.min()}")  # -3
    min_stack.pop()
    print(f"栈顶元素: {min_stack.pop()}")  # 0
    print(f"当前最小值: {min_stack.min()}")  # -2
    
    # 测试最大栈
    print("\n=== 测试最大栈 ===")
    max_stack = MaxStack()
    max_stack.push(5)
    max_stack.push(1)
    max_stack.push(5)
    print(f"栈顶元素: {max_stack.top()}")  # 5
    print(f"弹出最大值: {max_stack.popMax()}")  # 5
    print(f"栈顶元素: {max_stack.top()}")  # 1
    print(f"当前最大值: {max_stack.peekMax()}")  # 5
    print(f"弹出栈顶: {max_stack.pop()}")  # 1
    print(f"当前最大值: {max_stack.peekMax()}")  # 5
    
    # 测试排序栈
    print("\n=== 测试排序栈 ===")
    sorted_stack = SortedStack()
    sorted_stack.push(1)
    sorted_stack.push(2)
    print(f"栈顶(最小值): {sorted_stack.peek()}")  # 1
    sorted_stack.pop()
    print(f"弹出后栈顶: {sorted_stack.peek()}")  # 2
    
    # 测试用栈实现队列
    print("\n=== 测试用栈实现队列 ===")
    queue = MyQueue()
    queue.push(1)
    queue.push(2)
    print(f"队列头部: {queue.peek()}")  # 1
    print(f"弹出队列头部: {queue.pop()}")  # 1
    print(f"队列是否为空: {queue.empty()}")  # False
    
    # 测试支持增量操作的栈
    print("\n=== 测试支持增量操作的栈 ===")
    custom_stack = CustomStack(3)
    custom_stack.push(1)
    custom_stack.push(2)
    print(f"弹出: {custom_stack.pop()}")  # 2
    custom_stack.push(2)
    custom_stack.push(3)
    custom_stack.push(4)
    custom_stack.increment(5, 100)
    custom_stack.increment(2, 100)
    print(f"弹出: {custom_stack.pop()}")  # 103
    print(f"弹出: {custom_stack.pop()}")  # 202
    print(f"弹出: {custom_stack.pop()}")  # 201
    print(f"弹出: {custom_stack.pop()}")  # -1
    
    print("\n所有测试完成!")

# 题目8：最小栈的泛型实现
# 题目来源：扩展实现
# 
# 题目描述：
# 实现一个支持泛型的最小栈，能够处理任何可比较的类型。
# 
# 解题思路：
# 扩展基本的最小栈实现，Python中的列表天然支持任何类型，我们可以使用类型提示来增强类型安全性。
# 
# 时间复杂度：O(1) - 所有操作
# 空间复杂度：O(n) - 需要辅助栈
# 
# 工程化考量：
# 1. 类型提示：使用Python的类型注解增强代码可读性和可维护性
# 2. 异常处理：提供明确的错误信息
# 3. 文档字符串：详细说明类和方法的用途

from typing import Generic, TypeVar, List, Optional

T = TypeVar('T')

class GenericMinStack(Generic[T]):
    """
    泛型最小栈实现，支持任何可比较的类型。
    
    使用两个列表，一个存储数据，一个存储最小值。
    """
    
    def __init__(self):
        """初始化空的泛型最小栈"""
        self.data_stack: List[T] = []  # 数据栈
        self.min_stack: List[T] = []   # 最小值栈
    
    def push(self, val: T) -> None:
        """将元素压入栈中
        
        Args:
            val: 要压入栈的元素
        """
        self.data_stack.append(val)
        # 如果最小栈为空，或当前元素小于等于最小栈栈顶，则压入当前元素
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)
        else:
            self.min_stack.append(self.min_stack[-1])
    
    def pop(self) -> T:
        """弹出栈顶元素并返回
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("Stack is empty")
        self.min_stack.pop()
        return self.data_stack.pop()
    
    def top(self) -> T:
        """获取栈顶元素但不移除
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("Stack is empty")
        return self.data_stack[-1]
    
    def get_min(self) -> T:
        """获取栈中的最小值
        
        Returns:
            栈中的最小值
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.min_stack:
            raise IndexError("Stack is empty")
        return self.min_stack[-1]
    
    def is_empty(self) -> bool:
        """检查栈是否为空
        
        Returns:
            如果栈为空返回True，否则返回False
        """
        return len(self.data_stack) == 0

# 题目9：设计一个双端队列的最小栈
# 题目来源：力扣扩展题
# 
# 题目描述：
# 设计一个数据结构，支持在双端队列的两端进行添加和删除操作，并且能够在O(1)时间内获取最小值。
# 
# 解题思路：
# 使用两个双端队列，一个存储数据，一个维护最小值。每次在任意一端添加元素时，
# 同步更新最小值双端队列。
# 
# 时间复杂度：O(1) - 所有操作（均摊）
# 空间复杂度：O(n) - 需要额外的双端队列

from collections import deque

class MinDeque:
    """
    支持双端操作且能在O(1)时间内获取最小值的双端队列。
    
    使用两个双端队列，一个存储数据，一个维护最小值。
    """
    
    def __init__(self):
        """初始化空的最小双端队列"""
        self.data_deque = deque()  # 数据双端队列
        self.min_deque = deque()   # 最小值双端队列
    
    def add_first(self, val: int) -> None:
        """在队列头部添加元素
        
        Args:
            val: 要添加的整数
        """
        self.data_deque.appendleft(val)
        # 维护最小值队列
        while self.min_deque and self.min_deque[0] > val:
            self.min_deque.popleft()
        self.min_deque.appendleft(val)
    
    def add_last(self, val: int) -> None:
        """在队列尾部添加元素
        
        Args:
            val: 要添加的整数
        """
        self.data_deque.append(val)
        # 维护最小值队列
        while self.min_deque and self.min_deque[-1] > val:
            self.min_deque.pop()
        self.min_deque.append(val)
    
    def remove_first(self) -> int:
        """从队列头部移除并返回元素
        
        Returns:
            队列头部的元素
            
        Raises:
            IndexError: 如果队列为空
        """
        if not self.data_deque:
            raise IndexError("Deque is empty")
        val = self.data_deque.popleft()
        if val == self.min_deque[0]:
            self.min_deque.popleft()
        return val
    
    def remove_last(self) -> int:
        """从队列尾部移除并返回元素
        
        Returns:
            队列尾部的元素
            
        Raises:
            IndexError: 如果队列为空
        """
        if not self.data_deque:
            raise IndexError("Deque is empty")
        val = self.data_deque.pop()
        if val == self.min_deque[-1]:
            self.min_deque.pop()
        return val
    
    def get_first(self) -> int:
        """获取队列头部元素但不移除
        
        Returns:
            队列头部的元素
            
        Raises:
            IndexError: 如果队列为空
        """
        if not self.data_deque:
            raise IndexError("Deque is empty")
        return self.data_deque[0]
    
    def get_last(self) -> int:
        """获取队列尾部元素但不移除
        
        Returns:
            队列尾部的元素
            
        Raises:
            IndexError: 如果队列为空
        """
        if not self.data_deque:
            raise IndexError("Deque is empty")
        return self.data_deque[-1]
    
    def get_min(self) -> int:
        """获取队列中的最小值
        
        Returns:
            队列中的最小值
            
        Raises:
            IndexError: 如果队列为空
        """
        if not self.min_deque:
            raise IndexError("Deque is empty")
        return self.min_deque[0]
    
    def is_empty(self) -> bool:
        """检查队列是否为空
        
        Returns:
            如果队列为空返回True，否则返回False
        """
        return len(self.data_deque) == 0

# 题目10：多栈共享最小值
# 题目来源：算法设计扩展题
# 
# 题目描述：
# 设计一个数据结构，支持创建多个栈，并且能够在O(1)时间内获取所有栈中的最小值。
# 
# 解题思路：
# 维护一个全局最小值堆和每个栈的最小值记录。使用字典记录每个最小值出现的次数。
# 
# 时间复杂度：
# - push/pop: O(log k) - k为不同最小值的数量
# - getGlobalMin: O(1)
# 
# 空间复杂度：O(n + k) - n为所有栈元素总数，k为不同最小值的数量

import heapq
from typing import Dict, List

class MultiStackMinSystem:
    """
    支持创建多个栈并能在O(1)时间内获取全局最小值的系统。
    
    使用堆来快速获取最小值，并使用字典维护每个最小值的出现次数。
    """
    
    def __init__(self):
        """初始化多栈最小值系统"""
        self.stacks: List[List[int]] = []  # 存储多个栈
        self.min_heap: List[int] = []     # 全局最小值堆
        self.min_count: Dict[int, int] = {}  # 记录每个最小值的出现次数
    
    def create_stack(self) -> int:
        """创建一个新栈
        
        Returns:
            新创建栈的索引
        """
        self.stacks.append([])
        return len(self.stacks) - 1  # 返回栈的索引
    
    def push(self, stack_id: int, val: int) -> None:
        """向指定栈中压入元素
        
        Args:
            stack_id: 栈的索引
            val: 要压入的元素
            
        Raises:
            IndexError: 如果栈索引无效
        """
        if stack_id < 0 or stack_id >= len(self.stacks):
            raise IndexError("Invalid stack ID")
        
        self.stacks[stack_id].append(val)
        
        # 更新最小值堆和计数
        heapq.heappush(self.min_heap, val)
        self.min_count[val] = self.min_count.get(val, 0) + 1
    
    def pop(self, stack_id: int) -> int:
        """从指定栈中弹出元素
        
        Args:
            stack_id: 栈的索引
            
        Returns:
            弹出的元素
            
        Raises:
            IndexError: 如果栈索引无效或栈为空
        """
        if stack_id < 0 or stack_id >= len(self.stacks):
            raise IndexError("Invalid stack ID")
        
        if not self.stacks[stack_id]:
            raise IndexError("Stack is empty")
        
        val = self.stacks[stack_id].pop()
        
        # 更新计数
        self.min_count[val] -= 1
        if self.min_count[val] == 0:
            del self.min_count[val]
        
        return val
    
    def top(self, stack_id: int) -> int:
        """获取指定栈的栈顶元素
        
        Args:
            stack_id: 栈的索引
            
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈索引无效或栈为空
        """
        if stack_id < 0 or stack_id >= len(self.stacks):
            raise IndexError("Invalid stack ID")
        
        if not self.stacks[stack_id]:
            raise IndexError("Stack is empty")
        
        return self.stacks[stack_id][-1]
    
    def get_global_min(self) -> int:
        """获取所有栈中的全局最小值
        
        Returns:
            全局最小值
            
        Raises:
            IndexError: 如果所有栈都为空
        """
        # 清理堆顶无效元素
        while self.min_heap and self.min_heap[0] not in self.min_count:
            heapq.heappop(self.min_heap)
        
        if not self.min_heap:
            raise IndexError("All stacks are empty")
        
        return self.min_heap[0]

# 题目11：最小栈的线程安全实现
# 题目来源：工程实践题
# 
# 题目描述：
# 实现一个线程安全的最小栈，在多线程环境下能够正确工作。
# 
# 解题思路：
# 使用线程锁同步所有操作，确保线程安全。
# 
# 时间复杂度：O(1) - 所有操作，但由于锁的开销，实际性能可能降低
# 空间复杂度：O(n) - 需要辅助栈
# 
# 工程化考量：
# 1. 线程安全：使用threading.Lock确保多线程环境下的正确性
# 2. 性能优化：在Python中可以考虑使用更细粒度的锁来提高并发性能

import threading
from typing import List

class ThreadSafeMinStack:
    """
    线程安全的最小栈实现。
    
    使用线程锁确保多线程环境下的操作正确性。
    """
    
    def __init__(self):
        """初始化线程安全的最小栈"""
        self.data_stack: List[int] = []
        self.min_stack: List[int] = []
        self.lock = threading.Lock()  # 线程锁
    
    def push(self, val: int) -> None:
        """线程安全地将元素压入栈中
        
        Args:
            val: 要压入栈的元素
        """
        with self.lock:  # 获取锁
            self.data_stack.append(val)
            if not self.min_stack or val <= self.min_stack[-1]:
                self.min_stack.append(val)
            else:
                self.min_stack.append(self.min_stack[-1])
    
    def pop(self) -> int:
        """线程安全地弹出栈顶元素
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        with self.lock:  # 获取锁
            if not self.data_stack:
                raise IndexError("Stack is empty")
            self.min_stack.pop()
            return self.data_stack.pop()
    
    def top(self) -> int:
        """线程安全地获取栈顶元素
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        with self.lock:  # 获取锁
            if not self.data_stack:
                raise IndexError("Stack is empty")
            return self.data_stack[-1]
    
    def get_min(self) -> int:
        """线程安全地获取最小值
        
        Returns:
            栈中的最小值
            
        Raises:
            IndexError: 如果栈为空
        """
        with self.lock:  # 获取锁
            if not self.min_stack:
                raise IndexError("Stack is empty")
            return self.min_stack[-1]
    
    def is_empty(self) -> bool:
        """线程安全地检查栈是否为空
        
        Returns:
            如果栈为空返回True，否则返回False
        """
        with self.lock:  # 获取锁
            return len(self.data_stack) == 0

# 题目12：支持撤销操作的最小栈
# 题目来源：力扣扩展题
# 
# 题目描述：
# 设计一个支持撤销操作的最小栈，可以撤销最近的push或pop操作。
# 
# 解题思路：
# 使用操作历史栈记录每次操作的类型和参数，撤销时根据历史记录恢复状态。
# 
# 时间复杂度：
# - push/pop: O(1)
# - undo: O(1) 对于撤销push，O(1) 对于撤销pop
# 
# 空间复杂度：O(n) - 需要额外的空间存储历史操作

from typing import List, Tuple, Optional
import sys

class UndoableMinStack:
    """
    支持撤销操作的最小栈。
    
    可以撤销最近的push或pop操作，恢复到之前的状态。
    """
    
    def __init__(self):
        """初始化可撤销的最小栈"""
        self.data_stack: List[int] = []
        self.min_stack: List[int] = []
        # 历史操作：每个元素是元组 (操作类型, 值, 旧最小值)
        self.history: List[Tuple[str, int, Optional[int]]] = []
    
    def push(self, val: int) -> None:
        """将元素压入栈中，并记录操作历史
        
        Args:
            val: 要压入的元素
        """
        old_min = self.min_stack[-1] if self.min_stack else sys.maxsize
        self.data_stack.append(val)
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)
        else:
            self.min_stack.append(self.min_stack[-1])
        # 记录push操作
        self.history.append(("push", val, old_min))
    
    def pop(self) -> int:
        """弹出栈顶元素，并记录操作历史
        
        Returns:
            弹出的元素
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("Stack is empty")
        
        val = self.data_stack.pop()
        old_min = self.min_stack.pop()
        # 记录pop操作
        self.history.append(("pop", val, old_min))
        return val
    
    def top(self) -> int:
        """获取栈顶元素
        
        Returns:
            栈顶元素
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.data_stack:
            raise IndexError("Stack is empty")
        return self.data_stack[-1]
    
    def get_min(self) -> int:
        """获取最小值
        
        Returns:
            栈中的最小值
            
        Raises:
            IndexError: 如果栈为空
        """
        if not self.min_stack:
            raise IndexError("Stack is empty")
        return self.min_stack[-1]
    
    def undo(self) -> None:
        """撤销最近的操作
        
        Raises:
            IndexError: 如果没有操作可以撤销
        """
        if not self.history:
            raise IndexError("No operation to undo")
        
        operation, val, old_min = self.history.pop()
        
        if operation == "push":
            # 撤销push操作
            self.data_stack.pop()
            self.min_stack.pop()
        elif operation == "pop":
            # 撤销pop操作，恢复数据和最小值
            self.data_stack.append(val)
            self.min_stack.append(old_min)

# 题目13：最小栈的单元测试示例
# 题目来源：工程实践
# 
# 题目描述：
# 为最小栈实现编写全面的单元测试，覆盖正常场景、边界场景和异常场景。
# 
# 测试策略：
# 1. 正常场景测试：基本操作流程
# 2. 边界场景测试：空栈、单元素栈、重复元素、极端值
# 3. 异常场景测试：空栈操作异常

class MinStackTest:
    """
    最小栈的单元测试类。
    
    提供全面的测试方法，覆盖各种使用场景。
    """
    
    @staticmethod
    def run_tests():
        """运行所有测试"""
        print("=== 运行最小栈单元测试 ===")
        
        # 测试1：基本功能测试
        MinStackTest.basic_test()
        
        # 测试2：边界场景测试
        MinStackTest.boundary_test()
        
        # 测试3：异常场景测试
        MinStackTest.exception_test()
        
        print("=== 所有测试通过！===")
    
    @staticmethod
    def basic_test():
        """测试基本功能"""
        print("\n1. 基本功能测试：")
        min_stack = MinStackOptimized()
        min_stack.push(5)
        min_stack.push(2)
        min_stack.push(7)
        min_stack.push(1)
        
        assert min_stack.min() == 1, "最小值应该是1"
        assert min_stack.top() == 1, "栈顶应该是1"
        
        min_stack.pop()
        assert min_stack.min() == 2, "弹出后最小值应该是2"
        assert min_stack.top() == 7, "弹出后栈顶应该是7"
        
        print("基本功能测试通过")
    
    @staticmethod
    def boundary_test():
        """测试边界场景"""
        print("\n2. 边界场景测试：")
        
        # 测试空栈
        empty_stack = MinStackOptimized()
        
        # 测试单元素栈
        single_stack = MinStackOptimized()
        single_stack.push(42)
        assert single_stack.min() == 42, "单元素栈最小值应该是该元素"
        assert single_stack.top() == 42, "单元素栈栈顶应该是该元素"
        single_stack.pop()
        
        # 测试重复元素
        duplicate_stack = MinStackOptimized()
        duplicate_stack.push(3)
        duplicate_stack.push(3)
        duplicate_stack.push(3)
        assert duplicate_stack.min() == 3, "重复元素栈最小值应该是3"
        duplicate_stack.pop()
        assert duplicate_stack.min() == 3, "弹出后最小值应该还是3"
        
        # 测试极端值
        extreme_stack = MinStackOptimized()
        extreme_stack.push(-sys.maxsize)
        extreme_stack.push(sys.maxsize)
        assert extreme_stack.min() == -sys.maxsize, "最小值应该是- sys.maxsize"
        
        print("边界场景测试通过")
    
    @staticmethod
    def exception_test():
        """测试异常场景"""
        print("\n3. 异常场景测试：")
        exception_stack = MinStackOptimized()
        
        exception_caught = False
        try:
            exception_stack.pop()
        except IndexError:
            exception_caught = True
        assert exception_caught, "空栈pop应该抛出异常"
        
        exception_caught = False
        try:
            exception_stack.top()
        except IndexError:
            exception_caught = True
        assert exception_caught, "空栈top应该抛出异常"
        
        exception_caught = False
        try:
            exception_stack.min()
        except IndexError:
            exception_caught = True
        assert exception_caught, "空栈get_min应该抛出异常"
        
        print("异常场景测试通过")

# 题目14：最小栈的性能优化分析
# 题目来源：算法优化实践
# 
# 题目描述：
# 分析不同最小栈实现的性能特点，进行性能测试和优化建议。
# 
# 优化方向：
# 1. 空间优化：辅助栈只存储必要的最小值
# 2. 内存局部性：使用预分配的数组提高性能
# 3. 避免不必要的内存分配：使用列表的append和pop操作优化

import time

class MinStackPerformanceAnalyzer:
    """
    最小栈性能分析类。
    
    分析不同实现的性能特点，并提供优化建议。
    """
    
    class MinStackInterface:
        """最小栈接口定义"""
        def push(self, val: int) -> None:
            raise NotImplementedError
        
        def pop(self) -> None:
            raise NotImplementedError
        
        def get_min(self) -> int:
            raise NotImplementedError
    
    class StandardMinStack(MinStackInterface):
        """标准实现"""
        def __init__(self):
            self.data = []
            self.min_stack = []
        
        def push(self, val: int) -> None:
            self.data.append(val)
            if not self.min_stack or val <= self.min_stack[-1]:
                self.min_stack.append(val)
            else:
                self.min_stack.append(self.min_stack[-1])
        
        def pop(self) -> None:
            self.data.pop()
            self.min_stack.pop()
        
        def get_min(self) -> int:
            return self.min_stack[-1]
    
    class SpaceOptimizedMinStack(MinStackInterface):
        """空间优化实现"""
        def __init__(self):
            self.data = []
            self.min_stack = []
        
        def push(self, val: int) -> None:
            self.data.append(val)
            if not self.min_stack or val <= self.min_stack[-1]:
                self.min_stack.append(val)
        
        def pop(self) -> None:
            if self.data[-1] == self.min_stack[-1]:
                self.min_stack.pop()
            self.data.pop()
        
        def get_min(self) -> int:
            return self.min_stack[-1]
    
    @staticmethod
    def analyze_performance():
        """分析不同实现的性能"""
        print("=== 最小栈性能分析 ===")
        
        # 测试不同实现的性能
        MinStackPerformanceAnalyzer.test_implementation("标准实现", MinStackPerformanceAnalyzer.StandardMinStack())
        MinStackPerformanceAnalyzer.test_implementation("空间优化实现", MinStackPerformanceAnalyzer.SpaceOptimizedMinStack())
    
    @staticmethod
    def test_implementation(name: str, stack: MinStackInterface) -> None:
        """测试特定实现的性能
        
        Args:
            name: 实现名称
            stack: 最小栈实例
        """
        print(f"\n测试 {name}：")
        
        # 测试push性能
        start = time.time()
        for i in range(100000):
            stack.push(i % 1000)
        push_time = (time.time() - start) * 1000  # 转换为毫秒
        print(f"Push 100,000 elements: {push_time:.2f} ms")
        
        # 测试get_min性能
        start = time.time()
        for _ in range(100000):
            stack.get_min()
        get_min_time = (time.time() - start) * 1000
        print(f"GetMin 100,000 times: {get_min_time:.2f} ms")
        
        # 测试pop性能
        start = time.time()
        for _ in range(100000):
            stack.pop()
        pop_time = (time.time() - start) * 1000
        print(f"Pop 100,000 elements: {pop_time:.2f} ms")

# 题目15：最小栈与机器学习的联系
# 题目来源：跨领域应用
# 
# 题目描述：
# 探讨最小栈在机器学习和数据分析中的应用场景。
# 
# 应用场景：
# 1. 在线学习中的滑动窗口最小值监控
# 2. 异常检测算法中的阈值维护
# 3. 梯度下降算法中的学习率自适应调整

class MinStackMLApplications:
    """
    最小栈在机器学习中的应用示例。
    
    提供实际应用场景和实现。
    """
    
    @staticmethod
    def sliding_window_minimum(nums: List[int], window_size: int) -> List[int]:
        """使用最小栈实现滑动窗口最小值监控
        
        Args:
            nums: 输入数组
            window_size: 窗口大小
            
        Returns:
            每个窗口的最小值数组
        """
        result = []
        if not nums or window_size <= 0 or window_size > len(nums):
            return result
        
        # 使用两个栈实现队列，并维护最小值
        class MinQueue:
            """支持最小值查询的队列"""
            
            def __init__(self):
                self.stack1 = []  # 用于入队
                self.min_stack1 = []
                self.stack2 = []  # 用于出队
                self.min_stack2 = []
            
            def _transfer_if_needed(self):
                """当stack2为空时，将stack1的元素转移到stack2"""
                if not self.stack2:
                    while self.stack1:
                        val = self.stack1.pop()
                        self.stack2.append(val)
                        current_min = val if not self.min_stack2 else min(val, self.min_stack2[-1])
                        self.min_stack2.append(current_min)
            
            def push(self, val: int) -> None:
                """入队操作"""
                self.stack1.append(val)
                current_min = val if not self.min_stack1 else min(val, self.min_stack1[-1])
                self.min_stack1.append(current_min)
            
            def pop(self) -> int:
                """出队操作"""
                self._transfer_if_needed()
                return self.stack2.pop()
            
            def get_min(self) -> int:
                """获取队列中的最小值"""
                if not self.stack1:
                    return self.min_stack2[-1]
                if not self.stack2:
                    return self.min_stack1[-1]
                return min(self.min_stack1[-1], self.min_stack2[-1])
        
        min_queue = MinQueue()
        
        # 初始化窗口
        for i in range(window_size - 1):
            min_queue.push(nums[i])
        
        # 滑动窗口
        for i in range(window_size - 1, len(nums)):
            min_queue.push(nums[i])
            result.append(min_queue.get_min())
            min_queue.pop()
        
        return result