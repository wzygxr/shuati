# 用栈实现队列
# 用队列实现栈

class MyQueue:
    """
    用栈实现队列
    测试链接: https://leetcode.cn/problems/implement-queue-using-stacks/
    
    题目描述：
    请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）：
    
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

    def push(self, x: int) -> None:
        """将元素 x 推到队列的末尾"""
        self.in_stack.append(x)

    def pop(self) -> int:
        """从队列的开头移除并返回元素"""
        self._check_out_stack()
        return self.out_stack.pop()

    def peek(self) -> int:
        """返回队列开头的元素"""
        self._check_out_stack()
        return self.out_stack[-1]

    def empty(self) -> bool:
        """如果队列为空，返回 True；否则，返回 False"""
        return len(self.in_stack) == 0 and len(self.out_stack) == 0

    def _check_out_stack(self) -> None:
        """检查输出栈是否为空，如果为空则将输入栈的所有元素移到输出栈"""
        if not self.out_stack:
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())


class MyStack:
    """
    用队列实现栈
    测试链接: https://leetcode.cn/problems/implement-stack-using-queues/
    
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

    def push(self, x: int) -> None:
        """将元素 x 压入栈顶"""
        # 将新元素加入辅助队列
        self.queue2.append(x)
        # 将主队列的所有元素移到辅助队列
        while self.queue1:
            self.queue2.append(self.queue1.pop(0))
        # 交换两个队列的角色
        self.queue1, self.queue2 = self.queue2, self.queue1

    def pop(self) -> int:
        """移除并返回栈顶元素"""
        return self.queue1.pop(0)

    def top(self) -> int:
        """返回栈顶元素"""
        return self.queue1[0]

    def empty(self) -> bool:
        """如果栈是空的，返回 True；否则，返回 False"""
        return len(self.queue1) == 0


class MyStackOneQueue:
    """
    用一个队列实现栈
    题目来源：LeetCode 225. 用队列实现栈（进阶解法）
    链接：https://leetcode.cn/problems/implement-stack-using-queues/
    
    题目描述：
    请你仅使用一个队列实现一个后入先出（LIFO）的栈，并支持普通栈的全部四种操作（push、top、pop 和 empty）。
    
    解题思路：
    使用一个队列实现栈。在push操作时，先将新元素加入队列尾部，然后将队列中已有的n-1个元素依次从头部取出并重新加入队列尾部，
    这样新元素就位于队列头部，实现了栈的LIFO特性。
    
    时间复杂度分析：
    - push操作：O(n) - 需要将队列中已有的元素重新排列
    - pop操作：O(1) - 直接从队列头部移除元素
    - top操作：O(1) - 直接返回队列头部元素
    - empty操作：O(1) - 检查队列是否为空
    
    空间复杂度分析：
    O(n) - 需要一个队列来存储元素
    """

    def __init__(self):
        self.queue = []

    def push(self, x: int) -> None:
        """将元素 x 压入栈顶"""
        n = len(self.queue)
        self.queue.append(x)
        # 将前面的n个元素依次从头部取出并重新加入队列尾部
        for _ in range(n):
            self.queue.append(self.queue.pop(0))

    def pop(self) -> int:
        """移除并返回栈顶元素"""
        return self.queue.pop(0)

    def top(self) -> int:
        """返回栈顶元素"""
        return self.queue[0]

    def empty(self) -> bool:
        """如果栈是空的，返回 True；否则，返回 False"""
        return len(self.queue) == 0


class MyCircularDeque:
    """
    循环双端队列
    题目来源：LeetCode 641. 设计循环双端队列
    链接：https://leetcode.cn/problems/design-circular-deque/
    
    题目描述：
    设计实现双端队列。实现 MyCircularDeque 类:
    MyCircularDeque(int k)：构造函数,双端队列最大为 k 。
    boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
    boolean insertLast(int value)：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
    boolean deleteFront()：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
    boolean deleteLast()：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
    int getFront()：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
    int getRear()：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
    boolean isEmpty()：若双端队列为空，则返回 true ，否则返回 false 。
    boolean isFull()：若双端队列满了，则返回 true ，否则返回 false 。
    
    解题思路：
    使用数组实现循环双端队列。维护头指针front、尾指针rear和元素个数size，通过取模运算实现循环特性。
    头部插入时front指针向前移动，尾部插入时rear指针向后移动，注意处理边界情况和循环特性。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(k) - k是双端队列的容量
    """

    def __init__(self, k: int):
        """构造函数,双端队列最大为 k"""
        self.elements = [0] * (k + 1)  # 多申请一个空间用于区分队列满和空的情况
        self.capacity = k + 1
        self.front = 0
        self.rear = 0
        self.size = 0

    def insertFront(self, value: int) -> bool:
        """将一个元素添加到双端队列头部"""
        if self.isFull():
            return False
        # front指针向前移动一位（考虑循环特性）
        self.front = (self.front - 1 + self.capacity) % self.capacity
        self.elements[self.front] = value
        self.size += 1
        return True

    def insertLast(self, value: int) -> bool:
        """将一个元素添加到双端队列尾部"""
        if self.isFull():
            return False
        self.elements[self.rear] = value
        # rear指针向后移动一位（考虑循环特性）
        self.rear = (self.rear + 1) % self.capacity
        self.size += 1
        return True

    def deleteFront(self) -> bool:
        """从双端队列头部删除一个元素"""
        if self.isEmpty():
            return False
        # front指针向后移动一位（考虑循环特性）
        self.front = (self.front + 1) % self.capacity
        self.size -= 1
        return True

    def deleteLast(self) -> bool:
        """从双端队列尾部删除一个元素"""
        if self.isEmpty():
            return False
        # rear指针向前移动一位（考虑循环特性）
        self.rear = (self.rear - 1 + self.capacity) % self.capacity
        self.size -= 1
        return True

    def getFront(self) -> int:
        """从双端队列头部获得一个元素"""
        if self.isEmpty():
            return -1
        return self.elements[self.front]

    def getRear(self) -> int:
        """获得双端队列的最后一个元素"""
        if self.isEmpty():
            return -1
        # 注意：rear指向的是下一个插入位置，最后一个元素在(rear-1+capacity)%capacity位置
        return self.elements[(self.rear - 1 + self.capacity) % self.capacity]

    def isEmpty(self) -> bool:
        """若双端队列为空，则返回 true ，否则返回 false"""
        return self.size == 0

    def isFull(self) -> bool:
        """若双端队列满了，则返回 true ，否则返回 false"""
        return self.size == self.capacity - 1  # 留一个空位用于区分满和空


def maxSlidingWindow(nums, k):
    """
    滑动窗口最大值
    题目来源：LeetCode 239. 滑动窗口最大值
    链接：https://leetcode.cn/problems/sliding-window-maximum/
    
    题目描述：
    给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
    返回 滑动窗口中的最大值 。
    
    解题思路：
    使用双端队列实现单调队列。队列中存储数组下标，队列头部始终是当前窗口的最大值下标，
    队列保持单调递减特性。遍历数组时，维护队列的单调性并及时移除窗口外的元素下标，
    当窗口形成后，队列头部元素就是当前窗口的最大值。
    
    时间复杂度分析：
    O(n) - 每个元素最多入队和出队一次
    
    空间复杂度分析：
    O(k) - 双端队列最多存储k个元素
    """
    if not nums or k <= 0:
        return []
    
    from collections import deque
    
    n = len(nums)
    # 结果数组，大小为 n-k+1
    result = []
    # 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
    dq = deque()
    
    for i in range(n):
        # 移除队列中超出窗口范围的下标
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        
        # 维护队列单调性，移除所有小于当前元素的下标
        while dq and nums[dq[-1]] < nums[i]:
            dq.pop()
        
        # 将当前元素下标加入队列尾部
        dq.append(i)
        
        # 当窗口形成后，记录当前窗口的最大值
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result


def dailyTemperatures(temperatures):
    """
    每日温度
    题目来源：LeetCode 739. 每日温度
    链接：https://leetcode.cn/problems/daily-temperatures/
    
    题目描述：
    给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，
    其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
    如果气温在这之后都不会升高，请在该位置用 0 来代替。
    
    解题思路：
    使用单调栈解决。栈中存储数组下标，保持栈中下标对应的温度单调递减。
    遍历温度数组，当遇到比栈顶元素对应温度更高的温度时，不断弹出栈顶元素并计算天数差，
    直到栈为空或当前温度不大于栈顶元素对应温度，然后将当前下标入栈。
    
    时间复杂度分析：
    O(n) - 每个元素最多入栈和出栈一次
    
    空间复杂度分析：
    O(n) - 栈最多存储n个元素
    """
    if not temperatures:
        return []
    
    n = len(temperatures)
    result = [0] * n
    # 单调栈，存储数组下标，保持栈中下标对应的温度单调递减
    stack = []
    
    for i in range(n):
        # 当栈不为空且当前温度大于栈顶下标对应的温度时
        while stack and temperatures[i] > temperatures[stack[-1]]:
            # 弹出栈顶元素并计算天数差
            index = stack.pop()
            result[index] = i - index
        # 将当前下标入栈
        stack.append(i)
    
    # 栈中剩余元素对应的天数差为0（默认值）
    return result


def trap(height):
    """
    接雨水
    题目来源：LeetCode 42. 接雨水
    链接：https://leetcode.cn/problems/trapping-rain-water/
    
    题目描述：
    给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
    
    解题思路：
    使用单调栈解决。栈中存储数组下标，保持栈中下标对应的高度单调递减。
    当遇到比栈顶元素高的柱子时，说明形成了一个凹槽，可以接水。
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是最优解之一
    """
    if not height:
        return 0
    result = 0
    stack = []
    for i in range(len(height)):
        while stack and height[i] > height[stack[-1]]:
            bottom = stack.pop()
            if not stack:
                break
            left = stack[-1]
            width = i - left - 1
            h = min(height[left], height[i]) - height[bottom]
            result += width * h
        stack.append(i)
    return result


def largestRectangleArea(heights):
    """
    柱状图中最大的矩形
    题目来源：LeetCode 84. 柱状图中最大的矩形
    链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not heights:
        return 0
    max_area = 0
    stack = []
    for i in range(len(heights)):
        while stack and heights[i] < heights[stack[-1]]:
            h = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, h * width)
        stack.append(i)
    while stack:
        h = heights[stack.pop()]
        width = len(heights) if not stack else len(heights) - stack[-1] - 1
        max_area = max(max_area, h * width)
    return max_area


def nextGreaterElement(nums1, nums2):
    """
    下一个更大元素 I
    题目来源：LeetCode 496
    链接：https://leetcode.cn/problems/next-greater-element-i/
    
    时间复杂度：O(m + n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not nums1 or not nums2:
        return []
    map_dict = {}
    stack = []
    for num in nums2:
        while stack and num > stack[-1]:
            map_dict[stack.pop()] = num
        stack.append(num)
    while stack:
        map_dict[stack.pop()] = -1
    return [map_dict[num] for num in nums1]


def nextGreaterElements(nums):
    """
    下一个更大元素 II
    题目来源：LeetCode 503
    链接：https://leetcode.cn/problems/next-greater-element-ii/
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not nums:
        return []
    n = len(nums)
    result = [-1] * n
    stack = []
    for i in range(2 * n):
        num = nums[i % n]
        while stack and num > nums[stack[-1]]:
            result[stack.pop()] = num
        if i < n:
            stack.append(i)
    return result


def removeDuplicates(s):
    """
    删除字符串中的所有相邻重复项
    题目来源：LeetCode 1047
    链接：https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string/
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not s:
        return s
    stack = []
    for c in s:
        if stack and stack[-1] == c:
            stack.pop()
        else:
            stack.append(c)
    return ''.join(stack)


def evalRPN(tokens):
    """
    逆波兰表达式求值
    题目来源：LeetCode 150
    链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not tokens:
        return 0
    stack = []
    for token in tokens:
        if token == '+':
            b, a = stack.pop(), stack.pop()
            stack.append(a + b)
        elif token == '-':
            b, a = stack.pop(), stack.pop()
            stack.append(a - b)
        elif token == '*':
            b, a = stack.pop(), stack.pop()
            stack.append(a * b)
        elif token == '/':
            b, a = stack.pop(), stack.pop()
            stack.append(int(a / b))
        else:
            stack.append(int(token))
    return stack[-1]


def isValid(s):
    """
    有效的括号
    题目来源：LeetCode 20
    链接：https://leetcode.cn/problems/valid-parentheses/
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    是否为最优解：是
    """
    if not s or len(s) % 2 != 0:
        return False
    stack = []
    for c in s:
        if c in '([{':
            stack.append(c)
        else:
            if not stack:
                return False
            top = stack.pop()
            if (c == ')' and top != '(') or (c == ']' and top != '[') or (c == '}' and top != '{'):
                return False
    return not stack


def largestRectangleArea(heights):
    """
    柱状图中最大的矩形
    题目来源：LeetCode 84. 柱状图中最大的矩形
    链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
    
    题目描述：
    给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
    求在该柱状图中，能够勾勒出来的矩形的最大面积。
    
    解题思路：
    使用单调栈。对于每个柱子，我们需要找到其左侧第一个比它矮的柱子和右侧第一个比它矮或等于它的柱子，
    这样就能确定以当前柱子为高度的最大矩形的宽度。使用单调栈可以在线性时间内找到所有柱子的左右边界。
    
    时间复杂度分析：
    O(n) - 每个柱子最多入栈和出栈一次
    
    空间复杂度分析：
    O(n) - 栈最多存储n个柱子的索引
    
    是否为最优解：是
    """
    if not heights:
        return 0
    
    n = len(heights)
    left_bound = [-1] * n  # 左边界数组
    right_bound = [n] * n  # 右边界数组
    stack = []
    
    # 计算每个柱子的左边界（左侧第一个比当前柱子矮的索引）
    for i in range(n):
        while stack and heights[stack[-1]] >= heights[i]:
            stack.pop()
        if stack:
            left_bound[i] = stack[-1]
        stack.append(i)
    
    # 清空栈，准备计算右边界
    stack = []
    
    # 计算每个柱子的右边界（右侧第一个比当前柱子矮的索引）
    for i in range(n-1, -1, -1):
        while stack and heights[stack[-1]] > heights[i]:
            stack.pop()
        if stack:
            right_bound[i] = stack[-1]
        stack.append(i)
    
    # 计算最大矩形面积
    max_area = 0
    for i in range(n):
        width = right_bound[i] - left_bound[i] - 1
        area = heights[i] * width
        if area > max_area:
            max_area = area
    
    return max_area


def decodeString(s):
    """
    字符串解码
    题目来源：LeetCode 394. 字符串解码
    链接：https://leetcode.cn/problems/decode-string/
    
    题目描述：
    给定一个经过编码的字符串，返回它解码后的字符串。
    编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。
    注意 k 保证为正整数。
    你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
    此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
    
    解题思路：
    使用两个栈，一个存储数字（重复次数），一个存储字符串。遍历字符串，遇到数字就解析出完整的数字，
    遇到左括号就将当前数字和当前字符串入栈，并重置数字和当前字符串；遇到右括号就弹出栈顶的数字和字符串，
    将当前字符串重复对应次数后与弹出的字符串拼接；遇到普通字符就添加到当前字符串中。
    
    时间复杂度分析：
    O(n) - 虽然有嵌套结构，但每个字符只会被处理一次
    
    空间复杂度分析：
    O(n) - 栈的大小与字符串的嵌套深度有关
    
    是否为最优解：是
    """
    num_stack = []  # 存储重复次数
    str_stack = []  # 存储中间字符串
    current_str = ""  # 当前处理的字符串
    current_num = 0  # 当前处理的数字
    
    for c in s:
        if c.isdigit():
            # 解析数字
            current_num = current_num * 10 + int(c)
        elif c == '[':
            # 遇到左括号，将当前数字和当前字符串入栈，重置
            num_stack.append(current_num)
            str_stack.append(current_str)
            current_num = 0
            current_str = ""
        elif c == ']':
            # 遇到右括号，弹出栈顶元素并处理
            repeat = num_stack.pop()
            prev_str = str_stack.pop()
            
            # 将当前字符串重复repeat次后与prev_str拼接
            current_str = prev_str + current_str * repeat
        else:
            # 普通字符，添加到当前字符串
            current_str += c
    
    return current_str


def calculate(s):
    """
    基本计算器 II
    题目来源：LeetCode 227. 基本计算器 II
    链接：https://leetcode.cn/problems/basic-calculator-ii/
    
    题目描述：
    给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
    整数除法仅保留整数部分。
    你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
    注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
    
    解题思路：
    使用栈来存储数字和运算符。遍历字符串，解析出数字，根据当前运算符与栈顶运算符的优先级关系，
    决定是否需要先进行计算。对于乘除法，我们可以立即计算，对于加减法，我们将操作数和运算符分别入栈，
    最后再对栈中的元素进行计算。
    
    时间复杂度分析：
    O(n) - 只需遍历一次字符串
    
    空间复杂度分析：
    O(n) - 栈的大小与表达式的复杂度有关
    
    是否为最优解：是
    """
    num_stack = []  # 存储数字
    op = '+'  # 当前运算符，默认为加号
    num = 0  # 当前解析的数字
    
    # 为了处理最后一个数字，在字符串末尾添加一个非数字字符
    s += '+'
    
    for c in s:
        if c.isdigit():
            # 解析数字
            num = num * 10 + int(c)
        elif c != ' ':
            # 遇到运算符
            if op == '+':
                num_stack.append(num)
            elif op == '-':
                num_stack.append(-num)
            elif op == '*':
                # 乘法优先级高，立即计算
                temp = num_stack.pop()
                num_stack.append(temp * num)
            elif op == '/':
                # 除法优先级高，立即计算
                temp = num_stack.pop()
                # 处理Python中负数除法的问题
                if temp * num < 0:
                    num_stack.append(-(abs(temp) // abs(num)))
                else:
                    num_stack.append(temp // num)
            # 更新运算符和重置数字
            op = c
            num = 0
    
    # 计算栈中所有数字的和
    return sum(num_stack)


class MinStack:
    """
    最小栈
    题目来源：LeetCode 155. 最小栈
    链接：https://leetcode.cn/problems/min-stack/
    
    题目描述：
    设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
    实现 MinStack 类:
    MinStack() 初始化堆栈对象。
    void push(int val) 将元素val推入堆栈。
    void pop() 删除堆栈顶部的元素。
    int top() 获取堆栈顶部的元素。
    int getMin() 获取堆栈中的最小元素。
    
    解题思路：
    使用辅助栈来同步存储当前栈中的最小值。当我们向主栈中push一个元素时，也向辅助栈中push当前的最小值；
    当我们从主栈中pop一个元素时，也从辅助栈中pop一个元素。这样，辅助栈的栈顶元素始终是当前栈中的最小值。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(n) - 需要一个辅助栈来存储最小值
    
    是否为最优解：是
    """
    def __init__(self):
        """初始化堆栈对象"""
        self.stack = []  # 主栈，存储所有元素
        self.min_stack = []  # 辅助栈，存储最小值
    
    def push(self, val):
        """将元素val推入堆栈"""
        self.stack.append(val)
        # 辅助栈为空或当前元素小于等于辅助栈栈顶元素时，将当前元素入辅助栈
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)
        else:
            # 否则，重复压入当前最小值
            self.min_stack.append(self.min_stack[-1])
    
    def pop(self):
        """删除堆栈顶部的元素"""
        if self.stack:
            self.stack.pop()
            self.min_stack.pop()
    
    def top(self):
        """获取堆栈顶部的元素"""
        return self.stack[-1]
    
    def getMin(self):
        """获取堆栈中的最小元素"""
        return self.min_stack[-1]


class CQueue:
    """
    用栈实现队列（CQueue）
    题目来源：剑指Offer 09. 用两个栈实现队列
    链接：https://leetcode.cn/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/
    
    题目描述：
    用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，
    分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
    
    解题思路：
    使用两个栈，一个输入栈和一个输出栈。appendTail操作时将元素压入输入栈，deleteHead操作时如果输出栈为空，
    就将输入栈的所有元素依次弹出并压入输出栈，然后再从输出栈弹出元素。
    这样可以保证元素的顺序符合队列的FIFO特性。
    
    时间复杂度分析：
    - appendTail操作：O(1) - 直接压入输入栈
    - deleteHead操作：均摊O(1) - 虽然有时需要将输入栈的所有元素移到输出栈，但每个元素最多只会被移动一次
    
    空间复杂度分析：
    O(n) - 需要两个栈来存储元素
    
    是否为最优解：是
    """
    def __init__(self):
        """初始化队列"""
        self.in_stack = []  # 输入栈
        self.out_stack = []  # 输出栈
    
    def appendTail(self, value):
        """在队列尾部插入整数"""
        self.in_stack.append(value)
    
    def deleteHead(self):
        """在队列头部删除整数，如果队列为空则返回-1"""
        if not self.out_stack:
            # 如果输出栈为空，将输入栈的所有元素移到输出栈
            while self.in_stack:
                self.out_stack.append(self.in_stack.pop())
        
        if not self.out_stack:
            return -1  # 队列为空
        
        return self.out_stack.pop()


def medianSlidingWindow(nums, k):
    """
    滑动窗口中位数
    题目来源：LeetCode 480. 滑动窗口中位数
    链接：https://leetcode.cn/problems/sliding-window-median/
    
    题目描述：
    给你一个数组 nums，有一个大小为 k 的窗口从最左端滑动到最右端。窗口中有 k 个数，每次窗口向右移动 1 位。
    你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
    
    解题思路：
    使用两个堆，一个最大堆和一个最小堆来维护窗口中的元素。最大堆存储窗口中较小的一半元素，
    最小堆存储窗口中较大的一半元素。这样，当k为奇数时，中位数就是最大堆的堆顶元素；当k为偶数时，
    中位数是两个堆顶元素的平均值。当窗口滑动时，我们需要从堆中移除离开窗口的元素，并添加新进入窗口的元素。
    
    时间复杂度分析：
    O(n log k) - n是数组长度，每个元素的插入和删除操作的时间复杂度为O(log k)
    
    空间复杂度分析：
    O(k) - 需要两个堆来存储窗口中的元素
    
    是否为最优解：是
    """
    import heapq
    
    if not nums or k <= 0:
        return []
    
    # 由于Python的heapq只支持最小堆，我们可以通过存储负数来模拟最大堆
    max_heap = []  # 最大堆，存储较小的一半元素
    min_heap = []  # 最小堆，存储较大的一半元素
    
    # 辅助函数：将元素添加到堆中，保持两个堆的平衡
    def add_to_heaps(num):
        # 优先添加到最大堆
        heapq.heappush(max_heap, -num)
        # 确保最大堆的最大值不大于最小堆的最小值
        if min_heap and -max_heap[0] > min_heap[0]:
            # 交换两个堆的堆顶元素
            max_val = -heapq.heappop(max_heap)
            min_val = heapq.heappop(min_heap)
            heapq.heappush(max_heap, -min_val)
            heapq.heappush(min_heap, max_val)
        # 重新平衡两个堆的大小
        if len(max_heap) > len(min_heap) + 1:
            # 将最大堆的堆顶元素移到最小堆
            heapq.heappush(min_heap, -heapq.heappop(max_heap))
        if len(min_heap) > len(max_heap):
            # 将最小堆的堆顶元素移到最大堆
            heapq.heappush(max_heap, -heapq.heappop(min_heap))
    
    # 辅助函数：从堆中移除元素（注意：这里使用了一种简化的方法，实际上需要更高效的实现）
    def remove_from_heaps(num):
        # 由于堆不支持高效的随机访问删除，这里使用一个临时方法
        # 将堆转换为列表，找到并移除元素，然后重新建堆
        nonlocal max_heap, min_heap
        
        # 尝试从max_heap中移除
        found = False
        temp = []
        while max_heap:
            val = -heapq.heappop(max_heap)
            if not found and val == num:
                found = True
            else:
                temp.append(-val)
        # 重建max_heap
        max_heap = temp
        heapq.heapify(max_heap)
        
        # 如果在max_heap中没有找到，则从min_heap中移除
        if not found:
            temp = []
            while min_heap:
                val = heapq.heappop(min_heap)
                if val == num:
                    found = True
                    break
                else:
                    temp.append(val)
            # 重建min_heap
            for v in temp:
                heapq.heappush(min_heap, v)
        
        # 重新平衡两个堆
        if len(max_heap) > len(min_heap) + 1:
            heapq.heappush(min_heap, -heapq.heappop(max_heap))
        if len(min_heap) > len(max_heap):
            heapq.heappush(max_heap, -heapq.heappop(min_heap))
    
    # 辅助函数：计算中位数
    def get_median():
        if len(max_heap) > len(min_heap):
            # 窗口大小为奇数，中位数是max_heap的堆顶元素
            return -max_heap[0]
        else:
            # 窗口大小为偶数，中位数是两个堆顶元素的平均值
            return (-max_heap[0] + min_heap[0]) / 2
    
    # 首先将前k个元素加入堆中
    for i in range(k):
        add_to_heaps(nums[i])
    
    # 计算初始窗口的中位数
    result = [get_median()]
    
    # 滑动窗口
    for i in range(k, len(nums)):
        # 移除离开窗口的元素
        remove_from_heaps(nums[i - k])
        # 添加新进入窗口的元素
        add_to_heaps(nums[i])
        # 计算当前窗口的中位数
        result.append(get_median())
    
    return result

# 测试函数
def test_all_algorithms():
    print("=== 测试柱状图中最大的矩形 ===")
    heights = [2, 1, 5, 6, 2, 3]
    print(f"输入: {heights}")
    result = largestRectangleArea(heights)
    print(f"输出: {result}")
    print("预期输出: 10")
    
    print("\n=== 测试字符串解码 ===")
    encoded = "3[a]2[bc]"
    print(f"输入: \"{encoded}\")")
    result = decodeString(encoded)
    print(f"输出: \"{result}\")")
    print("预期输出: \"aaabcbc\"")
    
    print("\n=== 测试基本计算器 II ===")
    expression = "3+2*2"
    print(f"输入: \"{expression}\")")
    result = calculate(expression)
    print(f"输出: {result}")
    print("预期输出: 7")
    
    print("\n=== 测试最小栈 ===")
    minStack = MinStack()
    minStack.push(-2)
    minStack.push(0)
    minStack.push(-3)
    print("输入: push(-2), push(0), push(-3)")
    print(f"getMin(): {minStack.getMin()}")
    print("预期: -3")
    minStack.pop()
    print("pop()")
    print(f"top(): {minStack.top()}")
    print("预期: 0")
    print(f"getMin(): {minStack.getMin()}")
    print("预期: -2")
    
    print("\n=== 测试用栈实现队列(CQueue) ===")
    queue = CQueue()
    queue.appendTail(3)
    print("appendTail(3)")
    print(f"deleteHead(): {queue.deleteHead()}")
    print("预期: 3")
    print(f"deleteHead(): {queue.deleteHead()}")
    print("预期: -1")
    
    print("\n=== 测试滑动窗口中位数 ===")
    nums = [1, 3, -1, -3, 5, 3, 6, 7]
    k = 3
    print(f"输入: {nums}, k={k}")
    result = medianSlidingWindow(nums, k)
    print(f"输出: {result}")
    print("预期输出: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]")

# 如果作为主程序运行，则执行测试
if __name__ == "__main__":
    test_all_algorithms()