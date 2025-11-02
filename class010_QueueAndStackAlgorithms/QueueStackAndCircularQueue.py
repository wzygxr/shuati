# 队列和栈的Python实现

class Queue1:
    """
    使用Python列表实现队列（简单版本）
    """
    
    def __init__(self):
        self.queue = []
    
    def is_empty(self):
        """检查队列是否为空"""
        return len(self.queue) == 0
    
    def offer(self, num):
        """向队列尾部添加元素"""
        self.queue.append(num)
    
    def poll(self):
        """从队列头部移除并返回元素"""
        if self.is_empty():
            raise Exception("Queue is empty")
        return self.queue.pop(0)
    
    def peek(self):
        """返回队列头部元素但不移除"""
        if self.is_empty():
            raise Exception("Queue is empty")
        return self.queue[0]
    
    def size(self):
        """返回队列大小"""
        return len(self.queue)


class Queue2:
    """
    使用固定大小数组实现循环队列
    """
    
    def __init__(self, n):
        self.queue = [0] * n
        self.l = 0
        self.r = 0
        self.limit = n
    
    def is_empty(self):
        """检查队列是否为空"""
        return self.l == self.r
    
    def offer(self, num):
        """向队列尾部添加元素"""
        if self.r < self.limit:
            self.queue[self.r] = num
            self.r += 1
        else:
            raise Exception("Queue is full")
    
    def poll(self):
        """从队列头部移除并返回元素"""
        if self.is_empty():
            raise Exception("Queue is empty")
        val = self.queue[self.l]
        self.l += 1
        return val
    
    def head(self):
        """返回队列头部元素"""
        if self.is_empty():
            raise Exception("Queue is empty")
        return self.queue[self.l]
    
    def tail(self):
        """返回队列尾部元素"""
        if self.is_empty():
            raise Exception("Queue is empty")
        return self.queue[self.r - 1]
    
    def size(self):
        """返回队列大小"""
        return self.r - self.l


class Stack1:
    """
    使用Python列表实现栈（简单版本）
    """
    
    def __init__(self):
        self.stack = []
    
    def is_empty(self):
        """检查栈是否为空"""
        return len(self.stack) == 0
    
    def push(self, num):
        """向栈顶添加元素"""
        self.stack.append(num)
    
    def pop(self):
        """移除并返回栈顶元素"""
        if self.is_empty():
            raise Exception("Stack is empty")
        return self.stack.pop()
    
    def peek(self):
        """返回栈顶元素但不移除"""
        if self.is_empty():
            raise Exception("Stack is empty")
        return self.stack[-1]
    
    def size(self):
        """返回栈大小"""
        return len(self.stack)


class Stack2:
    """
    使用固定大小数组实现栈
    """
    
    def __init__(self, n):
        self.stack = [0] * n
        self.sz = 0
    
    def is_empty(self):
        """检查栈是否为空"""
        return self.sz == 0
    
    def push(self, num):
        """向栈顶添加元素"""
        self.stack[self.sz] = num
        self.sz += 1
    
    def pop(self):
        """移除并返回栈顶元素"""
        if self.is_empty():
            raise Exception("Stack is empty")
        self.sz -= 1
        return self.stack[self.sz]
    
    def peek(self):
        """返回栈顶元素但不移除"""
        if self.is_empty():
            raise Exception("Stack is empty")
        return self.stack[self.sz - 1]
    
    def size(self):
        """返回栈大小"""
        return self.sz


class MyCircularQueue:
    """
    循环队列实现
    题目来源：LeetCode 622. 设计循环队列
    链接：https://leetcode.cn/problems/design-circular-queue/
    
    题目描述：
    设计你的循环队列实现。 循环队列是一种线性数据结构，其操作表现基于 FIFO（先进先出）原则，
    并且队尾被连接在队首之后以形成一个循环。它也被称为"环形缓冲器"。
    
    解题思路：
    使用数组实现循环队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
    当指针到达数组末尾时，通过取模运算使其回到数组开头，实现循环效果。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(k) - k是队列的容量
    """
    
    def __init__(self, k):
        """初始化循环队列"""
        self.queue = [0] * k
        self.l = 0
        self.r = 0
        self.size = 0
        self.limit = k
    
    def enQueue(self, value):
        """向循环队列插入一个元素。如果成功插入则返回真"""
        if self.isFull():
            return False
        else:
            self.queue[self.r] = value
            # r++, 结束了，跳回0
            self.r = 0 if self.r == self.limit - 1 else self.r + 1
            self.size += 1
            return True
    
    def deQueue(self):
        """从循环队列中删除一个元素。如果成功删除则返回真"""
        if self.isEmpty():
            return False
        else:
            # l++, 结束了，跳回0
            self.l = 0 if self.l == self.limit - 1 else self.l + 1
            self.size -= 1
            return True
    
    def Front(self):
        """从队首获取元素。如果队列为空，返回 -1"""
        if self.isEmpty():
            return -1
        else:
            return self.queue[self.l]
    
    def Rear(self):
        """获取队尾元素。如果队列为空，返回 -1"""
        if self.isEmpty():
            return -1
        else:
            last = self.limit - 1 if self.r == 0 else self.r - 1
            return self.queue[last]
    
    def isEmpty(self):
        """检查循环队列是否为空"""
        return self.size == 0
    
    def isFull(self):
        """检查循环队列是否已满"""
        return self.size == self.limit


class MyStack:
    """
    用队列实现栈
    题目来源：LeetCode 225. 用队列实现栈
    链接：https://leetcode.cn/problems/implement-stack-using-queues/
    
    题目描述：
    请你仅使用两个队列实现一个后入先出（LIFO）的栈，并支持普通栈的全部四种操作（push、top、pop 和 empty）。
    
    解题思路：
    使用两个队列，一个主队列和一个辅助队列。每次push操作时，将新元素加入辅助队列，
    然后将主队列的所有元素依次移到辅助队列，最后交换两个队列的角色。
    这样可以保证新元素总是在队列的前端，实现栈的LIFO特性。
    
    时间复杂度分析：
    - push操作：O(n) - 需要将主队列的所有元素移到辅助队列
    - pop操作：O(1) - 直接从主队列前端移除元素
    - top操作：O(1) - 直接返回主队列前端元素
    - empty操作：O(1) - 检查主队列是否为空
    
    空间复杂度分析：
    O(n) - 需要两个队列来存储元素
    """
    
    def __init__(self):
        self.queue1 = []  # 主队列
        self.queue2 = []  # 辅助队列
    
    def push(self, x):
        """将元素 x 压入栈顶"""
        # 将新元素加入辅助队列
        self.queue2.append(x)
        # 将主队列的所有元素移到辅助队列
        while self.queue1:
            self.queue2.append(self.queue1.pop(0))
        # 交换两个队列的角色
        self.queue1, self.queue2 = self.queue2, self.queue1
    
    def pop(self):
        """移除并返回栈顶元素"""
        return self.queue1.pop(0)
    
    def top(self):
        """返回栈顶元素"""
        return self.queue1[0]
    
    def empty(self):
        """如果栈是空的，返回 True；否则，返回 False"""
        return len(self.queue1) == 0


class MyQueue:
    """
    用栈实现队列
    题目来源：LeetCode 232. 用栈实现队列
    链接：https://leetcode.cn/problems/implement-queue-using-stacks/
    
    题目描述：
    请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）。
    
    解题思路：
    使用两个栈，一个输入栈和一个输出栈。push操作时将元素压入输入栈，pop操作时如果输出栈为空，
    就将输入栈的所有元素依次弹出并压入输出栈，然后再从输出栈弹出元素。
    这样可以保证元素的顺序符合队列的FIFO特性。
    
    时间复杂度分析：
    - push操作：O(1) - 直接压入输入栈
    - pop操作：均摊O(1) - 虽然有时需要将输入栈的所有元素移到输出栈，但每个元素最多只会被移动一次
    - peek操作：均摊O(1) - 同pop操作
    - empty操作：O(1) - 检查两个栈是否都为空
    
    空间复杂度分析：
    O(n) - 需要两个栈来存储元素
    """
    
    def __init__(self):
        self.in_stack = []   # 输入栈
        self.out_stack = []  # 输出栈
    
    def push(self, x):
        """将元素 x 推到队列的末尾"""
        self.in_stack.append(x)
    
    def pop(self):
        """从队列的开头移除并返回元素"""
        self._check_out_stack()
        return self.out_stack.pop()
    
    def peek(self):
        """返回队列开头的元素"""
        self._check_out_stack()
        return self.out_stack[-1]
    
    def empty(self):
        """如果队列为空，返回 True；否则，返回 False"""
        return len(self.in_stack) == 0 and len(self.out_stack) == 0
    
    def _check_out_stack(self):
        """检查输出栈是否为空，如果为空则将输入栈的所有元素移到输出栈"""
        if not self.out_stack:
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())


class MinStack:
    """
    最小栈
    题目来源：LeetCode 155. 最小栈
    链接：https://leetcode.cn/problems/min-stack/
    
    题目描述：
    设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
    
    解题思路：
    使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。
    每次push操作时，数据栈正常压入元素，辅助栈压入当前元素与之前最小值中的较小者。
    这样辅助栈的栈顶始终是当前栈中的最小值。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(n) - 需要两个栈来存储元素
    """
    
    def __init__(self):
        self.data_stack = []  # 数据栈
        self.min_stack = []   # 辅助栈，存储每个位置对应的最小值
    
    def push(self, val):
        """将元素val推入堆栈"""
        self.data_stack.append(val)
        # 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)
        else:
            self.min_stack.append(self.min_stack[-1])
    
    def pop(self):
        """删除堆栈顶部的元素"""
        self.data_stack.pop()
        self.min_stack.pop()
    
    def top(self):
        """获取堆栈顶部的元素"""
        return self.data_stack[-1]
    
    def getMin(self):
        """获取堆栈中的最小元素"""
        return self.min_stack[-1]


def is_valid(s):
    """
    有效的括号
    题目来源：LeetCode 20. 有效的括号
    链接：https://leetcode.cn/problems/valid-parentheses/
    
    题目描述：
    给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
    
    解题思路：
    使用栈来解决括号匹配问题。遍历字符串，遇到左括号时将其对应的右括号压入栈，
    遇到右括号时检查是否与栈顶元素匹配。如果匹配则弹出栈顶元素，否则返回false。
    最后检查栈是否为空，如果为空则说明所有括号都正确匹配。
    
    时间复杂度分析：
    O(n) - 需要遍历整个字符串
    
    空间复杂度分析：
    O(n) - 最坏情况下栈中存储所有左括号
    """
    stack = []
    for c in s:
        # 遇到左括号时，将其对应的右括号压入栈
        if c == '(':
            stack.append(')')
        elif c == '[':
            stack.append(']')
        elif c == '{':
            stack.append('}')
        # 遇到右括号时，检查是否与栈顶元素匹配
        elif not stack or stack.pop() != c:
            return False
    # 最后检查栈是否为空
    return len(stack) == 0

def trap(height):
    """
    接雨水
    题目来源：LeetCode 42. 接雨水
    链接：https://leetcode.cn/problems/trapping-rain-water/
    
    题目描述：
    给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
    
    解题思路（单调栈）：
    使用单调栈来记录可能形成水坑的位置。当遇到一个比栈顶元素更高的柱子时，说明可能形成了一个水坑，
    弹出栈顶元素作为坑底，新的栈顶元素作为左边界，当前柱子作为右边界，计算可以接的雨水量。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 最坏情况下栈中存储所有元素
    """
    n = len(height)
    if n < 3:  # 至少需要3个柱子才能接雨水
        return 0
    
    stack = []  # 存储索引
    water = 0
    
    for i in range(n):
        # 当前高度大于栈顶高度时，说明可以形成水坑
        while stack and height[i] > height[stack[-1]]:
            bottom = stack.pop()
            
            if not stack:  # 没有左边界
                break
            
            left = stack[-1]
            width = i - left - 1
            h = min(height[left], height[i]) - height[bottom]
            water += width * h
        stack.append(i)
    
    return water

def largestRectangleArea(heights):
    """
    柱状图中最大的矩形
    题目来源：LeetCode 84. 柱状图中最大的矩形
    链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
    
    题目描述：
    给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
    求在该柱状图中，能够勾勒出来的矩形的最大面积。
    
    解题思路（单调栈）：
    使用单调栈来找到每个柱子左边和右边第一个比它小的柱子的位置。对于每个柱子，
    其能形成的最大矩形的宽度是右边界减去左边界减一，高度是柱子本身的高度。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 栈的最大空间为n
    """
    n = len(heights)
    if n == 0:
        return 0
    
    stack = []  # 存储索引
    max_area = 0
    
    for i in range(n + 1):
        # 当i=n时，将高度视为0，用于处理栈中剩余的元素
        h = 0 if i == n else heights[i]
        
        # 当当前高度小于栈顶高度时，计算栈顶柱子能形成的最大矩形
        while stack and h < heights[stack[-1]]:
            height_val = heights[stack.pop()]
            width = i if not stack else (i - stack[-1] - 1)
            max_area = max(max_area, height_val * width)
        stack.append(i)
    
    return max_area

def dailyTemperatures(temperatures):
    """
    每日温度
    题目来源：LeetCode 739. 每日温度
    链接：https://leetcode.cn/problems/daily-temperatures/
    
    题目描述：
    给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，其中 answer[i] 是指对于第 i 天，
    下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用 0 来代替。
    
    解题思路（单调栈）：
    使用单调栈来存储温度的索引。遍历数组，当遇到一个温度比栈顶温度高时，说明找到了栈顶温度的下一个更高温度，
    计算天数差并更新结果数组，然后弹出栈顶元素，继续比较新的栈顶元素，直到栈为空或栈顶温度不小于当前温度。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 栈的最大空间为n
    """
    n = len(temperatures)
    answer = [0] * n
    stack = []  # 存储索引
    
    for i in range(n):
        # 当前温度大于栈顶温度时，更新结果
        while stack and temperatures[i] > temperatures[stack[-1]]:
            prev_index = stack.pop()
            answer[prev_index] = i - prev_index
        stack.append(i)
    
    return answer

def maxSlidingWindow(nums, k):
    """
    滑动窗口最大值
    题目来源：LeetCode 239. 滑动窗口最大值
    链接：https://leetcode.cn/problems/sliding-window-maximum/
    
    题目描述：
    给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
    返回 滑动窗口中的最大值 。
    
    解题思路（单调队列）：
    使用双端队列来维护窗口中的最大值。队列中的元素是数组的索引，对应的数组值是单调递减的。
    当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有小于当前元素的索引，
    因为它们不可能成为窗口中的最大值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最大值的索引。
    
    时间复杂度分析：
    O(n) - 每个元素最多入队出队一次
    
    空间复杂度分析：
    O(k) - 队列的最大空间为k
    """
    from collections import deque
    
    n = len(nums)
    result = []
    dq = deque()  # 存储索引，对应的值单调递减
    
    for i in range(n):
        # 移除队列中不在窗口内的元素（即索引小于i-k+1的元素）
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        
        # 移除队列中所有小于当前元素的索引
        while dq and nums[dq[-1]] < nums[i]:
            dq.pop()
        
        dq.append(i)
        
        # 当窗口形成时，队列头部是窗口中的最大值的索引
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

class MyCircularDeque:
    """
    设计循环双端队列
    题目来源：LeetCode 641. 设计循环双端队列
    链接：https://leetcode.cn/problems/design-circular-deque/
    
    题目描述：
    设计实现双端队列。
    实现 MyCircularDeque 类:
    MyCircularDeque(int k) ：构造函数，双端队列最大为 k 。
    boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
    boolean insertLast(int value) ：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
    boolean deleteFront() ：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
    boolean deleteLast() ：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
    int getFront() )：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
    int getRear() ：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
    boolean isEmpty() ：若双端队列为空，则返回 true ，否则返回 false 。
    boolean isFull() ：若双端队列满了，则返回 true ，否则返回 false 。
    
    解题思路：
    使用数组实现循环双端队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
    对于头部插入和删除操作，需要处理指针的循环特性。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(k) - k是队列的容量
    """
    def __init__(self, k):
        self.deque = [0] * k
        self.l = self.r = self.size = 0
        self.limit = k

    def insertFront(self, value):
        if self.isFull():
            return False
        
        if self.isEmpty():
            self.l = self.r = 0
            self.deque[0] = value
        else:
            self.l = self.limit - 1 if self.l == 0 else self.l - 1
            self.deque[self.l] = value
        self.size += 1
        return True

    def insertLast(self, value):
        if self.isFull():
            return False
        
        if self.isEmpty():
            self.l = self.r = 0
            self.deque[0] = value
        else:
            self.r = 0 if self.r == self.limit - 1 else self.r + 1
            self.deque[self.r] = value
        self.size += 1
        return True

    def deleteFront(self):
        if self.isEmpty():
            return False
        
        self.l = 0 if self.l == self.limit - 1 else self.l + 1
        self.size -= 1
        return True

    def deleteLast(self):
        if self.isEmpty():
            return False
        
        self.r = self.limit - 1 if self.r == 0 else self.r - 1
        self.size -= 1
        return True

    def getFront(self):
        if self.isEmpty():
            return -1
        return self.deque[self.l]

    def getRear(self):
        if self.isEmpty():
            return -1
        return self.deque[self.r]

    def isEmpty(self):
        return self.size == 0

    def isFull(self):
        return self.size == self.limit

def evalRPN(tokens):
    """
    逆波兰表达式求值
    题目来源：LeetCode 150. 逆波兰表达式求值
    链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
    
    题目描述：
    给你一个字符串数组 tokens ，表示一个根据 逆波兰表示法 表示的算术表达式。
    请你计算该表达式。返回一个表示表达式值的整数。
    注意：
    有效的算符为 '+'、'-'、'*' 和 '/' 。
    每个操作数可以是整数，也可以是另一个表达式的结果。
    除法运算向零截断。
    表达式中不含除零运算。
    输入是一个根据逆波兰表示法表示的算术表达式。
    逆波兰表达式是一种后缀表达式，所谓后缀就是指算符写在后面。
    
    解题思路：
    使用栈来存储操作数。遍历表达式，遇到数字时将其转换为整数并入栈，遇到运算符时弹出栈顶的两个操作数，
    进行相应的运算，然后将结果压入栈中。最后栈中只剩下一个元素，即为表达式的结果。
    
    时间复杂度分析：
    O(n) - 需要遍历整个表达式
    
    空间复杂度分析：
    O(n) - 最坏情况下栈中存储所有操作数
    """
    stack = []
    
    for token in tokens:
        if token in ['+', '-', '*', '/']:
            # 弹出两个操作数
            b = stack.pop()
            a = stack.pop()
            
            # 进行相应的运算
            if token == '+':
                stack.append(a + b)
            elif token == '-':
                stack.append(a - b)
            elif token == '*':
                stack.append(a * b)
            elif token == '/':
                # Python 3中的除法向零截断需要特殊处理负数
                stack.append(int(a / b))
        else:
            # 遇到数字，转换为整数并入栈
            stack.append(int(token))
    
    return stack.pop()

def decodeString(s):
    """
    字符串解码
    题目来源：LeetCode 394. 字符串解码
    链接：https://leetcode.cn/problems/decode-string/
    
    题目描述：
    给定一个经过编码的字符串，返回它解码后的字符串。
    编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
    你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
    此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
    
    解题思路：
    使用两个栈，一个存储数字，一个存储字符串。遍历字符串，遇到数字时解析完整的数字，遇到'['时将当前数字和字符串入栈，
    遇到']'时弹出栈顶的数字和字符串，将当前字符串重复数字次后与弹出的字符串拼接。
    
    时间复杂度分析：
    O(n) - 需要遍历整个字符串，每个字符最多被处理一次
    
    空间复杂度分析：
    O(n) - 栈的最大空间为n
    """
    num_stack = []  # 存储重复次数
    str_stack = []  # 存储中间字符串
    current_str = ''
    num = 0
    
    for c in s:
        if c.isdigit():
            # 解析完整的数字
            num = num * 10 + int(c)
        elif c == '[':
            # 将当前数字和字符串入栈
            num_stack.append(num)
            str_stack.append(current_str)
            num = 0
            current_str = ''
        elif c == ']':
            # 弹出栈顶的数字和字符串，进行拼接
            repeat = num_stack.pop()
            prev_str = str_stack.pop()
            current_str = prev_str + current_str * repeat
        else:
            # 普通字符，添加到当前字符串
            current_str += c
    
    return current_str

def removeDuplicates(S):
    """
    删除字符串中的所有相邻重复项
    题目来源：LeetCode 1047. 删除字符串中的所有相邻重复项
    链接：https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string/
    
    题目描述：
    给出由小写字母组成的字符串 S，重复项删除操作会选择两个相邻且相同的字母，并删除它们。
    在 S 上反复执行重复项删除操作，直到无法继续删除。
    在完成所有重复项删除操作后返回最终的字符串。答案保证唯一。
    
    解题思路：
    使用栈来存储字符。遍历字符串，对于每个字符，如果栈不为空且栈顶元素与当前字符相同，则弹出栈顶元素，
    否则将当前字符压入栈中。最后将栈中的元素按顺序拼接成字符串。
    
    时间复杂度分析：
    O(n) - 需要遍历整个字符串
    
    空间复杂度分析：
    O(n) - 栈的最大空间为n
    """
    stack = []
    
    for c in S:
        if stack and stack[-1] == c:
            stack.pop()
        else:
            stack.append(c)
    
    return ''.join(stack)

def calculate(s):
    """
    基本计算器 II
    题目来源：LeetCode 227. 基本计算器 II
    链接：https://leetcode.cn/problems/basic-calculator-ii/
    
    题目描述：
    给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
    整数除法仅保留整数部分。
    你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
    
    解题思路：
    使用栈来存储操作数。遍历字符串，遇到数字时解析完整的数字，遇到运算符时根据前一个运算符的类型进行相应的运算。
    对于加减运算，将操作数压入栈中；对于乘除运算，弹出栈顶元素与当前操作数进行运算后将结果压入栈中。
    最后将栈中的所有元素相加得到最终结果。
    
    时间复杂度分析：
    O(n) - 需要遍历整个字符串
    
    空间复杂度分析：
    O(n) - 栈的最大空间为n/2
    """
    stack = []
    pre_sign = '+'  # 前一个运算符
    num = 0
    n = len(s)
    
    for i in range(n):
        c = s[i]
        
        if c.isdigit():
            # 解析完整的数字
            num = num * 10 + int(c)
        
        # 遇到运算符或到达字符串末尾
        if not c.isdigit() and c != ' ' or i == n - 1:
            if pre_sign == '+':
                stack.append(num)
            elif pre_sign == '-':
                stack.append(-num)
            elif pre_sign == '*':
                stack.append(stack.pop() * num)
            elif pre_sign == '/':
                # Python 3中的除法向零截断需要特殊处理负数
                stack.append(int(stack.pop() / num))
            pre_sign = c
            num = 0
    
    # 将栈中的所有元素相加
    return sum(stack)

# 测试函数
def test_all():
    """
    测试所有算法实现
    """
    print("队列和栈相关算法实现测试")
    
    # 测试有效的括号
    s = "()[]{}"
    print(f"有效的括号测试结果: {is_valid(s)}")
    
    # 测试最小栈
    min_stack = MinStack()
    min_stack.push(-2)
    min_stack.push(0)
    min_stack.push(-3)
    print(f"最小栈最小值: {min_stack.getMin()}")
    min_stack.pop()
    print(f"最小栈栈顶: {min_stack.top()}")
    print(f"最小栈最小值: {min_stack.getMin()}")
    
    # 测试接雨水
    height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print(f"接雨水结果: {trap(height)}")
    
    # 测试柱状图中最大的矩形
    heights = [2, 1, 5, 6, 2, 3]
    print(f"柱状图中最大的矩形面积: {largestRectangleArea(heights)}")
    
    # 测试每日温度
    temperatures = [73, 74, 75, 71, 69, 72, 76, 73]
    print(f"每日温度结果: {dailyTemperatures(temperatures)}")
    
    # 测试滑动窗口最大值
    nums = [1, 3, -1, -3, 5, 3, 6, 7]
    k = 3
    print(f"滑动窗口最大值结果: {maxSlidingWindow(nums, k)}")
    
    # 测试循环双端队列
    deque = MyCircularDeque(3)
    print(f"循环双端队列插入尾部: {deque.insertLast(1)}")
    print(f"循环双端队列插入尾部: {deque.insertLast(2)}")
    print(f"循环双端队列插入头部: {deque.insertFront(3)}")
    print(f"循环双端队列插入头部: {deque.insertFront(4)}")
    print(f"循环双端队列尾部元素: {deque.getRear()}")
    print(f"循环双端队列是否已满: {deque.isFull()}")
    print(f"循环双端队列删除尾部: {deque.deleteLast()}")
    print(f"循环双端队列插入头部: {deque.insertFront(4)}")
    print(f"循环双端队列头部元素: {deque.getFront()}")
    
    # 测试逆波兰表达式求值
    tokens = ["2", "1", "+", "3", "*"]
    print(f"逆波兰表达式求值结果: {evalRPN(tokens)}")
    
    # 测试字符串解码
    s = "3[a]2[bc]"
    print(f"字符串解码结果: {decodeString(s)}")
    
    # 测试删除字符串中的所有相邻重复项
    S = "abbaca"
    print(f"删除相邻重复项结果: {removeDuplicates(S)}")
    
    # 测试基本计算器 II
    s = "3+2*2"
    print(f"基本计算器 II 结果: {calculate(s)}")

def removeDuplicatesK(s, k):
    """
    删除字符串中的所有相邻重复项 II
    题目来源:LeetCode 1209. 删除字符串中的所有相邻重复项 II
    链接:https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string-ii/
    
    题目描述:
    给你一个字符串 s,「k 倍重复项删除操作」将会从 s 中选择 k 个相邻且相等的字母,并删除它们,
    使被删去的字符串的左侧和右侧连在一起。
    你需要对 s 重复进行无限次这样的删除操作,直到无法继续为止。
    在执行完所有删除操作后,返回最终得到的字符串。
    
    解题思路:
    使用栈来存储字符和对应的出现次数。遍历字符串,对于每个字符,如果栈不为空且栈顶字符与当前字符相同,
    则将栈顶的计数加1,如果计数等于k则弹出栈顶元素;否则将当前字符和计数1入栈。
    
    时间复杂度分析:
    O(n) - 需要遍历整个字符串
    
    空间复杂度分析:
    O(n) - 栈的最大空间为n
    
    是否最优解:是,这是最优解,时间和空间复杂度都无法再优化
    """
    stack = []  # 存储字符和出现次数的元组
    
    for c in s:
        if stack and stack[-1][0] == c:
            # 如果栈顶字符与当前字符相同,增加计数
            count = stack[-1][1] + 1
            if count == k:
                # 如果计数等于k,弹出栈顶元素
                stack.pop()
            else:
                # 否则更新计数
                stack[-1] = (c, count)
        else:
            # 如果栈为空或栈顶字符与当前字符不同,将当前字符和计数1入栈
            stack.append((c, 1))
    
    # 构建结果字符串
    result = ''
    for char, count in stack:
        result += char * count
    
    return result

def nextGreaterElement(nums1, nums2):
    """
    下一个更大元素 I
    题目来源:LeetCode 496. 下一个更大元素 I
    链接:https://leetcode.cn/problems/next-greater-element-i/
    
    题目描述:
    nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置 右侧 的 第一个 比 x 大的元素。
    给你两个 没有重复元素 的数组 nums1 和 nums2 ,下标从 0 开始计数,其中nums1 是 nums2 的子集。
    对于每个 0 <= i < nums1.length ,找出满足 nums1[i] == nums2[j] 的下标 j ,并且在 nums2 确定 nums2[j] 的 下一个更大元素 。
    如果不存在下一个更大元素,那么本次查询的答案是 -1 。
    返回一个长度为 nums1.length 的数组 ans 作为答案,满足 ans[i] 是如上所述的 下一个更大元素 。
    
    解题思路(单调栈):
    使用单调栈来找到nums2中每个元素的下一个更大元素。从右往左遍历nums2,维护一个单调递减的栈,
    对于每个元素,弹出栈中所有小于等于当前元素的值,栈顶元素即为当前元素的下一个更大元素。
    用字典存储每个元素及其下一个更大元素的映射关系。
    
    时间复杂度分析:
    O(m + n) - m是nums1的长度,n是nums2的长度,每个元素最多入栈出栈一次
    
    空间复杂度分析:
    O(n) - 栈和字典的空间
    
    是否最优解:是,这是最优解
    """
    # 使用字典存储每个元素及其下一个更大元素
    mapping = {}
    stack = []
    
    # 从右往左遍历nums2
    for i in range(len(nums2) - 1, -1, -1):
        num = nums2[i]
        # 弹出栈中所有小于等于当前元素的值
        while stack and stack[-1] <= num:
            stack.pop()
        # 栈顶元素即为当前元素的下一个更大元素
        mapping[num] = stack[-1] if stack else -1
        stack.append(num)
    
    # 构建结果数组
    result = []
    for num in nums1:
        result.append(mapping[num])
    
    return result

def nextGreaterElements(nums):
    """
    下一个更大元素 II
    题目来源:LeetCode 503. 下一个更大元素 II
    链接:https://leetcode.cn/problems/next-greater-element-ii/
    
    题目描述:
    给定一个循环数组 nums( nums[nums.length - 1] 的下一个元素是 nums[0] ),返回 nums 中每个元素的 下一个更大元素 。
    数字 x 的 下一个更大的元素 是按数组遍历顺序,这个数字之后的第一个比它更大的数,这意味着你应该循环地搜索它的下一个更大的数。
    如果不存在,则输出 -1 。
    
    解题思路(单调栈):
    因为是循环数组,可以将数组遍历两遍。使用单调栈存储索引,当遇到一个元素比栈顶索引对应的元素大时,
    说明找到了栈顶元素的下一个更大元素。为了处理循环,遍历时使用取模运算。
    
    时间复杂度分析:
    O(n) - 虽然遍历两遍,但每个元素最多入栈出栈一次
    
    空间复杂度分析:
    O(n) - 栈的空间
    
    是否最优解:是,这是最优解
    """
    n = len(nums)
    result = [-1] * n
    stack = []  # 存储索引
    
    # 遍历两遍数组以处理循环
    for i in range(2 * n):
        num = nums[i % n]
        # 当遇到一个元素比栈顶索引对应的元素大时
        while stack and nums[stack[-1]] < num:
            result[stack.pop()] = num
        # 只在第一遍遍历时将索引入栈
        if i < n:
            stack.append(i)
    
    return result

def calculateBasic(s):
    """
    基本计算器
    题目来源:LeetCode 224. 基本计算器
    链接:https://leetcode.cn/problems/basic-calculator/
    
    题目描述:
    给你一个字符串表达式 s ,请你实现一个基本计算器来计算并返回它的值。
    注意:不允许使用任何将字符串作为数学表达式计算的内置函数,比如 eval() 。
    表达式可能包含 '(' 和 ')' ,以及 '+' 和 '-' 运算符。
    
    解题思路:
    使用栈来处理括号。遍历字符串,遇到数字时解析完整的数字,遇到运算符时更新符号,
    遇到左括号时将当前结果和符号入栈,遇到右括号时弹出栈顶的结果和符号进行计算。
    
    时间复杂度分析:
    O(n) - 需要遍历整个字符串
    
    空间复杂度分析:
    O(n) - 栈的最大空间为n
    
    是否最优解:是,这是最优解
    """
    stack = []
    result = 0
    num = 0
    sign = 1  # 1表示正号,-1表示负号
    
    for i in range(len(s)):
        c = s[i]
        
        if c.isdigit():
            # 解析完整的数字
            num = num * 10 + int(c)
        elif c == '+':
            result += sign * num
            num = 0
            sign = 1
        elif c == '-':
            result += sign * num
            num = 0
            sign = -1
        elif c == '(':
            # 遇到左括号,将当前结果和符号入栈
            stack.append(result)
            stack.append(sign)
            result = 0
            sign = 1
        elif c == ')':
            # 遇到右括号,计算括号内的结果
            result += sign * num
            num = 0
            # 弹出栈顶的符号和结果
            result *= stack.pop()  # 弹出符号
            result += stack.pop()  # 弹出之前的结果
    
    # 处理最后的数字
    if num != 0:
        result += sign * num
    
    return result

def simplifyPath(path):
    """
    简化路径
    题目来源:LeetCode 71. 简化路径
    链接:https://leetcode.cn/problems/simplify-path/
    
    题目描述:
    给你一个字符串 path ,表示指向某一文件或目录的 Unix 风格 绝对路径 (以 '/' 开头),请你将其转化为更加简洁的规范路径。
    在 Unix 风格的文件系统中,一个点(.)表示当前目录本身;此外,两个点 (..) 表示将目录切换到上一级(指向父目录);
    两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠(即,'//')都被视为单个斜杠 '/' 。 
    对于此问题,任何其他格式的点(例如,'...')均被视为文件/目录名称。
    
    解题思路:
    使用栈来处理路径。将路径按'/'分割成各个部分,遍历每个部分:
    - 如果是"..",则弹出栈顶元素(如果栈不为空)
    - 如果是"."或空字符串,则忽略
    - 否则将部分压入栈中
    最后将栈中的元素按顺序拼接成路径。
    
    时间复杂度分析:
    O(n) - 需要遍历整个路径字符串
    
    空间复杂度分析:
    O(n) - 栈的空间
    
    是否最优解:是,这是最优解
    """
    parts = path.split('/')
    stack = []
    
    for part in parts:
        if part == "..":
            # 返回上一级目录
            if stack:
                stack.pop()
        elif part and part != ".":
            # 进入下一级目录
            stack.append(part)
        # 如果是"."或空字符串,则忽略
    
    # 构建结果路径
    return '/' + '/'.join(stack)

def backspaceCompare(s, t):
    """
    比较含退格的字符串
    题目来源:LeetCode 844. 比较含退格的字符串
    链接:https://leetcode.cn/problems/backspace-string-compare/
    
    题目描述:
    给定 s 和 t 两个字符串,当它们分别被输入到空白的文本编辑器后,如果两者相等,返回 true 。
    # 代表退格字符。
    注意:如果对空文本输入退格字符,文本继续为空。
    
    解题思路:
    使用栈来模拟退格操作。遍历字符串,遇到非'#'字符时压入栈,遇到'#'时弹出栈顶元素(如果栈不为空)。
    最后比较两个栈是否相等。
    
    优化方案:可以使用双指针从后往前遍历,不需要额外的栈空间,空间复杂度O(1)。
    
    时间复杂度分析:
    O(m + n) - m和n是两个字符串的长度
    
    空间复杂度分析:
    O(m + n) - 两个栈的空间
    优化后:O(1)
    
    是否最优解:栈的方法不是最优解,双指针方法是最优解
    """
    def build_string(s):
        stack = []
        for c in s:
            if c != '#':
                stack.append(c)
            elif stack:
                stack.pop()
        return ''.join(stack)
    
    return build_string(s) == build_string(t)

def backspaceCompareOptimized(s, t):
    """
    双指针优化版本(最优解)
    """
    i, j = len(s) - 1, len(t) - 1
    skip_s, skip_t = 0, 0
    
    while i >= 0 or j >= 0:
        # 找到s中下一个有效字符
        while i >= 0:
            if s[i] == '#':
                skip_s += 1
                i -= 1
            elif skip_s > 0:
                skip_s -= 1
                i -= 1
            else:
                break
        
        # 找到t中下一个有效字符
        while j >= 0:
            if t[j] == '#':
                skip_t += 1
                j -= 1
            elif skip_t > 0:
                skip_t -= 1
                j -= 1
            else:
                break
        
        # 比较字符
        if i >= 0 and j >= 0:
            if s[i] != t[j]:
                return False
        elif i >= 0 or j >= 0:
            return False
        
        i -= 1
        j -= 1
    
    return True

def removeKdigits(num, k):
    """
    移掉 K 位数字
    题目来源:LeetCode 402. 移掉 K 位数字
    链接:https://leetcode.cn/problems/remove-k-digits/
    
    题目描述:
    给你一个以字符串表示的非负整数 num 和一个整数 k ,移除这个数中的 k 位数字,使得剩下的数字最小。
    请你以字符串形式返回这个最小的数字。
    
    解题思路(单调栈):
    使用单调栈维护一个单调递增的数字序列。遍历数字字符串,如果当前数字小于栈顶数字且还可以移除数字(k>0),
    则弹出栈顶数字并将k减1。遍历完成后,如果k还大于0,则从栈顶继续移除k个数字。
    最后去除前导0并返回结果。
    
    时间复杂度分析:
    O(n) - 每个数字最多入栈出栈一次
    
    空间复杂度分析:
    O(n) - 栈的空间
    
    是否最优解:是,这是最优解
    """
    stack = []
    
    for digit in num:
        # 如果当前数字小于栈顶数字且还可以移除数字,则弹出栈顶
        while stack and k > 0 and stack[-1] > digit:
            stack.pop()
            k -= 1
        stack.append(digit)
    
    # 如果k还大于0,从栈顶继续移除
    while k > 0 and stack:
        stack.pop()
        k -= 1
    
    # 构建结果字符串,去除前导0
    result = ''.join(stack).lstrip('0')
    return result if result else '0'

def validateStackSequences(pushed, popped):
    """
    验证栈序列
    题目来源:LeetCode 946. 验证栈序列
    链接:https://leetcode.cn/problems/validate-stack-sequences/
    
    题目描述:
    给定 pushed 和 popped 两个序列,每个序列中的 值都不重复,
    只有当它们可能是在最初空栈上进行的推入 push 和弹出 pop 操作序列的结果时,返回 true;否则,返回 false 。
    
    解题思路:
    使用栈模拟入栈和出栈操作。遍历pushed数组,将元素依次入栈,每次入栈后检查栈顶元素是否等于popped数组的当前元素,
    如果相等则出栈并移动popped的指针。最后检查栈是否为空。
    
    时间复杂度分析:
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析:
    O(n) - 栈的空间
    
    是否最优解:是,这是最优解
    """
    stack = []
    j = 0  # popped数组的指针
    
    for num in pushed:
        stack.append(num)
        # 检查栈顶元素是否等于popped的当前元素
        while stack and stack[-1] == popped[j]:
            stack.pop()
            j += 1
    
    return len(stack) == 0

def find132pattern(nums):
    """
    132模式
    题目来源：LeetCode 456. 132模式
    链接：https://leetcode.cn/problems/132-pattern/
    
    题目描述：
    给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
    并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
    如果 nums 中存在 132 模式的子序列，返回 true ；否则，返回 false 。
    
    解题思路（单调栈）：
    从右往左遍历数组，维护一个单调递减的栈，同时记录第二大的元素（即132模式中的2）。
    当遇到一个比第二大的元素还小的元素时，说明找到了132模式。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 栈的空间
    
    是否最优解：是
    """
    n = len(nums)
    if n < 3:
        return False
    
    stack = []
    second = float('-inf')  # 132模式中的2
    
    for i in range(n - 1, -1, -1):
        # 如果当前元素小于second，说明找到了132模式
        if nums[i] < second:
            return True
        
        # 维护单调递减栈
        while stack and nums[i] > stack[-1]:
            second = stack.pop()  # 更新第二大的元素
        
        stack.append(nums[i])
    
    return False

def removeDuplicateLetters(s):
    """
    去除重复字母
    题目来源：LeetCode 316. 去除重复字母
    链接：https://leetcode.cn/problems/remove-duplicate-letters/
    
    题目描述：
    给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。
    需保证 返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
    
    解题思路（单调栈）：
    使用单调栈维护一个字典序最小的结果。同时记录每个字符的最后出现位置和是否在栈中。
    遍历字符串，如果当前字符不在栈中，且比栈顶字符小，并且栈顶字符在后面还会出现，则弹出栈顶字符。
    
    时间复杂度分析：
    O(n) - 每个字符最多入栈出栈一次
    
    空间复杂度分析：
    O(1) - 因为字符集大小固定（26个字母）
    
    是否最优解：是
    """
    last_index = {}  # 记录每个字符最后出现的位置
    in_stack = set()  # 记录字符是否在栈中
    stack = []
    
    # 记录每个字符最后出现的位置
    for i, c in enumerate(s):
        last_index[c] = i
    
    for i, c in enumerate(s):
        # 如果字符已经在栈中，跳过
        if c in in_stack:
            continue
        
        # 如果栈不为空，且当前字符比栈顶字符小，且栈顶字符在后面还会出现，则弹出栈顶
        while stack and c < stack[-1] and last_index[stack[-1]] > i:
            in_stack.remove(stack.pop())
        
        stack.append(c)
        in_stack.add(c)
    
    return ''.join(stack)

def maximalRectangle(matrix):
    """
    最大矩形
    题目来源：LeetCode 85. 最大矩形
    链接：https://leetcode.cn/problems/maximal-rectangle/
    
    题目描述：
    给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
    
    解题思路（单调栈）：
    将问题转化为多个柱状图最大矩形问题。对于每一行，计算以该行为底边的柱状图高度，
    然后使用柱状图最大矩形的单调栈解法。
    
    时间复杂度分析：
    O(m*n) - m是行数，n是列数
    
    空间复杂度分析：
    O(n) - 高度数组和栈的空间
    
    是否最优解：是
    """
    if not matrix or not matrix[0]:
        return 0
    
    m, n = len(matrix), len(matrix[0])
    heights = [0] * n
    max_area = 0
    
    for i in range(m):
        # 更新高度数组
        for j in range(n):
            if matrix[i][j] == '1':
                heights[j] += 1
            else:
                heights[j] = 0
        
        # 计算当前行的最大矩形面积
        stack = []
        for j in range(n + 1):
            h = 0 if j == n else heights[j]
            
            while stack and h < heights[stack[-1]]:
                height_val = heights[stack.pop()]
                width = j if not stack else (j - stack[-1] - 1)
                max_area = max(max_area, height_val * width)
            stack.append(j)
    
    return max_area

def minSlidingWindow(nums, k):
    """
    滑动窗口最小值
    题目来源：LeetCode 239. 滑动窗口最小值（扩展）
    链接：https://leetcode.cn/problems/sliding-window-maximum/（类似题目）
    
    题目描述：
    给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
    返回滑动窗口中的最小值。
    
    解题思路（单调队列）：
    使用单调队列维护窗口中的最小值。队列中的元素是数组的索引，对应的数组值是单调递增的。
    当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有大于当前元素的索引，
    因为它们不可能成为窗口中的最小值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最小值的索引。
    
    时间复杂度分析：
    O(n) - 每个元素最多入队出队一次
    
    空间复杂度分析：
    O(k) - 队列的最大空间为k
    
    是否最优解：是
    """
    from collections import deque
    
    n = len(nums)
    result = []
    dq = deque()  # 存储索引，对应的值单调递增
    
    for i in range(n):
        # 移除队列中不在窗口内的元素
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        
        # 移除队列中所有大于当前元素的索引
        while dq and nums[dq[-1]] > nums[i]:
            dq.pop()
        
        dq.append(i)
        
        # 当窗口形成时，队列头部是窗口中的最小值的索引
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result

def sumSubarrayMins(arr):
    """
    子数组的最小值之和
    题目来源：LeetCode 907. 子数组的最小值之和
    链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
    
    题目描述：
    给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
    由于答案可能很大，因此返回答案模 10^9 + 7。
    
    解题思路（单调栈）：
    使用单调栈找到每个元素作为最小值出现的子数组范围。对于每个元素，找到左边第一个比它小的元素位置和右边第一个比它小的元素位置，
    那么该元素作为最小值的子数组个数为 (i - left) * (right - i)。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 栈的空间
    
    是否最优解：是
    """
    n = len(arr)
    MOD = 10**9 + 7
    left = [-1] * n  # 左边第一个比当前元素小的位置
    right = [n] * n  # 右边第一个比当前元素小的位置
    stack = []
    
    # 计算左边边界
    for i in range(n):
        while stack and arr[stack[-1]] > arr[i]:
            stack.pop()
        left[i] = stack[-1] if stack else -1
        stack.append(i)
    
    # 清空栈
    stack = []
    
    # 计算右边边界
    for i in range(n - 1, -1, -1):
        while stack and arr[stack[-1]] >= arr[i]:
            stack.pop()
        right[i] = stack[-1] if stack else n
        stack.append(i)
    
    # 计算总和
    total = 0
    for i in range(n):
        total = (total + arr[i] * (i - left[i]) * (right[i] - i)) % MOD
    
    return total

class StockSpanner:
    """
    股票价格跨度
    题目来源：LeetCode 901. 股票价格跨度
    链接：https://leetcode.cn/problems/online-stock-span/
    
    题目描述：
    编写一个 StockSpanner 类，它收集某些股票的每日报价，并返回该股票当日价格的跨度。
    今天股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
    
    解题思路（单调栈）：
    使用单调栈存储价格和对应的跨度。当新价格到来时，弹出栈中所有小于等于当前价格的价格，
    并将它们的跨度累加到当前价格的跨度中。
    
    时间复杂度分析：
    每个价格最多入栈出栈一次，均摊O(1)
    
    空间复杂度分析：
    O(n) - 栈的空间
    
    是否最优解：是
    """
    def __init__(self):
        self.stack = []  # 存储价格和跨度的元组
    
    def next(self, price):
        span = 1
        
        # 弹出栈中所有小于等于当前价格的价格，累加它们的跨度
        while self.stack and self.stack[-1][0] <= price:
            span += self.stack.pop()[1]
        
        self.stack.append((price, span))
        return span

def asteroidCollision(asteroids):
    """
    行星碰撞
    题目来源：LeetCode 735. 行星碰撞
    链接：https://leetcode.cn/problems/asteroid-collision/
    
    题目描述：
    给定一个整数数组 asteroids，表示在同一行的行星。
    对于数组中的每一个元素，其绝对值表示行星的大小，正负表示行星的移动方向（正表示向右移动，负表示向左移动）。
    每一颗行星以相同的速度移动。找出碰撞后剩下的所有行星。
    碰撞规则：两个行星相互碰撞，较小的行星会爆炸。如果两颗行星大小相同，则两颗行星都会爆炸。
    两颗移动方向相同的行星不会发生碰撞。
    
    解题思路（栈）：
    使用栈模拟行星碰撞过程。遍历行星数组，对于每个行星：
    - 如果栈为空或当前行星向右移动，直接入栈
    - 如果当前行星向左移动，与栈顶行星碰撞，直到栈为空或栈顶行星向左移动
    
    时间复杂度分析：
    O(n) - 每个行星最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 栈的空间
    
    是否最优解：是
    """
    stack = []
    
    for asteroid in asteroids:
        destroyed = False
        
        # 当前行星向左移动，且栈顶行星向右移动时发生碰撞
        while stack and asteroid < 0 and stack[-1] > 0:
            if stack[-1] < -asteroid:
                # 栈顶行星较小，被摧毁
                stack.pop()
                continue
            elif stack[-1] == -asteroid:
                # 大小相等，两个都摧毁
                stack.pop()
            destroyed = True
            break
        
        if not destroyed:
            stack.append(asteroid)
    
    return stack

def longestWPI(hours):
    """
    表现良好的最长时间段
    题目来源：LeetCode 1124. 表现良好的最长时间段
    链接：https://leetcode.cn/problems/longest-well-performing-interval/
    
    题目描述：
    给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
    我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
    所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格 大于「不劳累的天数」。
    请你返回「表现良好时间段」的最大长度。
    
    解题思路（单调栈）：
    将问题转化为前缀和问题，然后使用单调栈找到最大的区间使得前缀和大于0。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈出栈一次
    
    空间复杂度分析：
    O(n) - 前缀和数组和栈的空间
    
    是否最优解：是
    """
    n = len(hours)
    prefix = [0] * (n + 1)
    
    # 计算前缀和，劳累天记为1，不劳累天记为-1
    for i in range(n):
        prefix[i + 1] = prefix[i] + (1 if hours[i] > 8 else -1)
    
    # 使用单调栈存储递减的前缀和索引
    stack = []
    for i in range(n + 1):
        if not stack or prefix[i] < prefix[stack[-1]]:
            stack.append(i)
    
    # 从右往左遍历，找到最大的区间
    max_len = 0
    for i in range(n, -1, -1):
        while stack and prefix[i] > prefix[stack[-1]]:
            max_len = max(max_len, i - stack.pop())
    
    return max_len

def findUnsortedSubarray(nums):
    """
    最短无序连续子数组
    题目来源：LeetCode 581. 最短无序连续子数组
    链接：https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
    
    题目描述：
    给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
    请你找出符合题意的 最短 子数组，并输出它的长度。
    
    解题思路（单调栈）：
    使用单调栈找到需要排序的子数组的左右边界。
    从左往右找到第一个破坏递增的位置作为右边界，从右往左找到第一个破坏递减的位置作为左边界。
    
    时间复杂度分析：
    O(n) - 两次遍历
    
    空间复杂度分析：
    O(1) - 只使用常数空间
    
    是否最优解：是（有更简单的双指针解法，但单调栈思路清晰）
    """
    n = len(nums)
    left, right = n - 1, 0
    stack = []
    
    # 从左往右找到右边界
    for i in range(n):
        while stack and nums[stack[-1]] > nums[i]:
            right = max(right, i)
            left = min(left, stack.pop())
        stack.append(i)
    
    # 清空栈
    stack = []
    
    # 从右往左找到左边界
    for i in range(n - 1, -1, -1):
        while stack and nums[stack[-1]] < nums[i]:
            left = min(left, i)
            right = max(right, stack.pop())
        stack.append(i)
    
    return right - left + 1 if right > left else 0

def test_extended():
    """
    测试扩展算法实现
    """
    print("\n扩展算法测试")
    
    # 测试132模式
    nums132 = [3, 1, 4, 2]
    print(f"132模式测试结果: {find132pattern(nums132)}")
    
    # 测试去除重复字母
    duplicate_str = "bcabc"
    print(f"去除重复字母结果: {removeDuplicateLetters(duplicate_str)}")
    
    # 测试滑动窗口最小值
    nums_min = [1, 3, -1, -3, 5, 3, 6, 7]
    min_window = minSlidingWindow(nums_min, 3)
    print(f"滑动窗口最小值结果: {min_window}")
    
    # 测试行星碰撞
    asteroids = [5, 10, -5]
    collision_result = asteroidCollision(asteroids)
    print(f"行星碰撞结果: {collision_result}")
    
    # 测试股票价格跨度
    stock_spanner = StockSpanner()
    prices = [100, 80, 60, 70, 60, 75, 85]
    spans = [stock_spanner.next(price) for price in prices]
    print(f"股票价格跨度结果: {spans}")
    
    # 测试最短无序连续子数组
    unsorted_nums = [2, 6, 4, 8, 10, 9, 15]
    print(f"最短无序连续子数组长度: {findUnsortedSubarray(unsorted_nums)}")

# 如果直接运行此文件，则执行测试
if __name__ == "__main__":
    test_all()
    test_extended()