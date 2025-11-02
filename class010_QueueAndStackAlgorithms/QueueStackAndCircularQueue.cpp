// 队列和栈的C++实现
// 详细的注释和扩展题目实现

#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <string>
#include <unordered_map>
#include <algorithm>
using namespace std;

/**
 * 队列的简单实现（基于STL）
 * 时间复杂度：所有操作O(1)
 * 空间复杂度：O(n)
 */
class Queue1 {
private:
    queue<int> q;

public:
    bool isEmpty() {
        return q.empty();
    }

    void offer(int num) {
        q.push(num);
    }

    int poll() {
        int val = q.front();
        q.pop();
        return val;
    }

    int peek() {
        return q.front();
    }

    int size() {
        return q.size();
    }
};

/**
 * 使用固定大小数组实现队列
 * 常数时间性能更好
 * 时间复杂度：所有操作O(1)
 * 空间复杂度：O(n)
 */
class Queue2 {
private:
    vector<int> queue;
    int l; // 队头指针
    int r; // 队尾指针
    int limit; // 队列容量

public:
    Queue2(int n) : limit(n) {
        queue.resize(n);
        l = 0;
        r = 0;
    }

    bool isEmpty() {
        return l == r;
    }

    void offer(int num) {
        if (r < limit) {
            queue[r++] = num;
        } else {
            throw "Queue is full";
        }
    }

    int poll() {
        if (isEmpty()) {
            throw "Queue is empty";
        }
        return queue[l++];
    }

    int head() {
        if (isEmpty()) {
            throw "Queue is empty";
        }
        return queue[l];
    }

    int tail() {
        if (isEmpty()) {
            throw "Queue is empty";
        }
        return queue[r - 1];
    }

    int size() {
        return r - l;
    }
};

/**
 * 栈的简单实现（基于STL）
 * 时间复杂度：所有操作O(1)
 * 空间复杂度：O(n)
 */
class Stack1 {
private:
    stack<int> s;

public:
    bool isEmpty() {
        return s.empty();
    }

    void push(int num) {
        s.push(num);
    }

    int pop() {
        int val = s.top();
        s.pop();
        return val;
    }

    int peek() {
        return s.top();
    }

    int size() {
        return s.size();
    }
};

/**
 * 使用固定大小数组实现栈
 * 常数时间性能更好
 * 时间复杂度：所有操作O(1)
 * 空间复杂度：O(n)
 */
class Stack2 {
private:
    vector<int> stack;
    int sz; // 当前栈大小
    int limit; // 栈容量

public:
    Stack2(int n) : limit(n) {
        stack.resize(n);
        sz = 0;
    }

    bool isEmpty() {
        return sz == 0;
    }

    void push(int num) {
        if (sz < limit) {
            stack[sz++] = num;
        } else {
            throw "Stack is full";
        }
    }

    int pop() {
        if (isEmpty()) {
            throw "Stack is empty";
        }
        return stack[--sz];
    }

    int peek() {
        if (isEmpty()) {
            throw "Stack is empty";
        }
        return stack[sz - 1];
    }

    int size() {
        return sz;
    }
};

/**
 * 设计循环队列
 * 题目来源：LeetCode 622. 设计循环队列
 * 链接：https://leetcode.cn/problems/design-circular-queue/
 * 
 * 题目描述：
 * 设计你的循环队列实现。循环队列是一种线性数据结构，其操作表现基于 FIFO（先进先出）原则，
 * 并且队尾被连接在队首之后以形成一个循环。它也被称为"环形缓冲器"。
 * 
 * 解题思路：
 * 使用数组实现循环队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
 * 当指针到达数组末尾时，通过取模运算使其回到数组开头，实现循环效果。
 * 
 * 时间复杂度分析：
 * 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * O(k) - k是队列的容量
 */
class MyCircularQueue {
private:
    vector<int> queue;
    int l, r, size, limit;

public:
    MyCircularQueue(int k) {
        queue.resize(k);
        l = r = size = 0;
        limit = k;
    }

    bool enQueue(int value) {
        if (isFull()) {
            return false;
        } else {
            queue[r] = value;
            // r++, 结束了，跳回0
            r = r == limit - 1 ? 0 : (r + 1);
            size++;
            return true;
        }
    }

    bool deQueue() {
        if (isEmpty()) {
            return false;
        } else {
            // l++, 结束了，跳回0
            l = l == limit - 1 ? 0 : (l + 1);
            size--;
            return true;
        }
    }

    int Front() {
        if (isEmpty()) {
            return -1;
        } else {
            return queue[l];
        }
    }

    int Rear() {
        if (isEmpty()) {
            return -1;
        } else {
            int last = r == 0 ? (limit - 1) : (r - 1);
            return queue[last];
        }
    }

    bool isEmpty() {
        return size == 0;
    }

    bool isFull() {
        return size == limit;
    }
};

/**
 * 用队列实现栈
 * 题目来源：LeetCode 225. 用队列实现栈
 * 链接：https://leetcode.cn/problems/implement-stack-using-queues/
 * 
 * 题目描述：
 * 请你仅使用两个队列实现一个后入先出（LIFO）的栈，并支持普通栈的全部四种操作（push、top、pop 和 empty）。
 * 实现 MyStack 类：
 * void push(int x) 将元素 x 压入栈顶。
 * int pop() 移除并返回栈顶元素。
 * int top() 返回栈顶元素。
 * boolean empty() 如果栈是空的，返回 true ；否则，返回 false 。
 * 
 * 解题思路：
 * 使用两个队列，一个主队列和一个辅助队列。每次push操作时，将新元素加入辅助队列，然后将主队列的所有元素依次移到辅助队列，
 * 最后交换两个队列的角色。这样可以保证新元素总是在队列的前端，实现栈的LIFO特性。
 * 
 * 时间复杂度分析：
 * - push操作：O(n) - 需要将主队列的所有元素移到辅助队列
 * - pop操作：O(1) - 直接从主队列前端移除元素
 * - top操作：O(1) - 直接返回主队列前端元素
 * - empty操作：O(1) - 检查主队列是否为空
 * 
 * 空间复杂度分析：
 * O(n) - 需要两个队列来存储元素
 */
class MyStack {
private:
    queue<int> queue1;  // 主队列
    queue<int> queue2;  // 辅助队列

public:
    MyStack() {
    }

    void push(int x) {
        // 将新元素加入辅助队列
        queue2.push(x);
        // 将主队列的所有元素移到辅助队列
        while (!queue1.empty()) {
            queue2.push(queue1.front());
            queue1.pop();
        }
        // 交换两个队列的角色
        swap(queue1, queue2);
    }

    int pop() {
        int val = queue1.front();
        queue1.pop();
        return val;
    }

    int top() {
        return queue1.front();
    }

    bool empty() {
        return queue1.empty();
    }
};

/**
 * 用栈实现队列
 * 题目来源：LeetCode 232. 用栈实现队列
 * 链接：https://leetcode.cn/problems/implement-queue-using-stacks/
 * 
 * 题目描述：
 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）：
 * 实现 MyQueue 类：
 * void push(int x) 将元素 x 推到队列的末尾
 * int pop() 从队列的开头移除并返回元素
 * int peek() 返回队列开头的元素
 * boolean empty() 如果队列为空，返回 true ；否则，返回 false
 * 
 * 解题思路：
 * 使用两个栈，一个输入栈和一个输出栈。push操作时将元素压入输入栈，pop操作时如果输出栈为空，
 * 就将输入栈的所有元素依次弹出并压入输出栈，然后再从输出栈弹出元素。这样可以保证元素的顺序符合队列的FIFO特性。
 * 
 * 时间复杂度分析：
 * - push操作：O(1) - 直接压入输入栈
 * - pop操作：均摊O(1) - 虽然有时需要将输入栈的所有元素移到输出栈，但每个元素最多只会被移动一次
 * - peek操作：均摊O(1) - 同pop操作
 * - empty操作：O(1) - 检查两个栈是否都为空
 * 
 * 空间复杂度分析：
 * O(n) - 需要两个栈来存储元素
 */
class MyQueue {
private:
    stack<int> inStack;   // 输入栈
    stack<int> outStack;  // 输出栈

    // 检查输出栈是否为空，如果为空则将输入栈的所有元素移到输出栈
    void checkOutStack() {
        if (outStack.empty()) {
            while (!inStack.empty()) {
                outStack.push(inStack.top());
                inStack.pop();
            }
        }
    }

public:
    MyQueue() {
    }

    void push(int x) {
        inStack.push(x);
    }

    int pop() {
        checkOutStack();
        int val = outStack.top();
        outStack.pop();
        return val;
    }

    int peek() {
        checkOutStack();
        return outStack.top();
    }

    bool empty() {
        return inStack.empty() && outStack.empty();
    }
};

/**
 * 最小栈
 * 题目来源：LeetCode 155. 最小栈
 * 链接：https://leetcode.cn/problems/min-stack/
 * 
 * 题目描述：
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 * 实现 MinStack 类:
 * MinStack() 初始化堆栈对象。
 * void push(int val) 将元素val推入堆栈。
 * void pop() 删除堆栈顶部的元素。
 * int top() 获取堆栈顶部的元素。
 * int getMin() 获取堆栈中的最小元素。
 * 
 * 解题思路：
 * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。每次push操作时，
 * 数据栈正常压入元素，辅助栈压入当前元素与之前最小值中的较小者。这样辅助栈的栈顶始终是当前栈中的最小值。
 * 
 * 时间复杂度分析：
 * 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * O(n) - 需要两个栈来存储元素
 */
class MinStack {
private:
    stack<int> dataStack;  // 数据栈
    stack<int> minStack;   // 辅助栈，存储每个位置对应的最小值

public:
    MinStack() {
    }

    void push(int val) {
        dataStack.push(val);
        // 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if (minStack.empty() || val <= minStack.top()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.top());
        }
    }

    void pop() {
        dataStack.pop();
        minStack.pop();
    }

    int top() {
        return dataStack.top();
    }

    int getMin() {
        return minStack.top();
    }
};

/**
 * 有效的括号
 * 题目来源：LeetCode 20. 有效的括号
 * 链接：https://leetcode.cn/problems/valid-parentheses/
 * 
 * 题目描述：
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
 * 有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合。
 * 2. 左括号必须以正确的顺序闭合。
 * 3. 每个右括号都有一个对应的相同类型的左括号。
 * 
 * 解题思路：
 * 使用栈来解决括号匹配问题。遍历字符串，遇到左括号时将其对应的右括号压入栈，
 * 遇到右括号时检查是否与栈顶元素匹配。如果匹配则弹出栈顶元素，否则返回false。
 * 最后检查栈是否为空，如果为空则说明所有括号都正确匹配。
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历整个字符串
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下栈中存储所有左括号
 */
bool isValid(string s) {
    stack<char> st;
    for (char c : s) {
        // 遇到左括号时，将其对应的右括号压入栈
        if (c == '(') {
            st.push(')');
        } else if (c == '[') {
            st.push(']');
        } else if (c == '{') {
            st.push('}');
        }
        // 遇到右括号时，检查是否与栈顶元素匹配
        else if (st.empty() || st.top() != c) {
            return false;
        } else {
            st.pop();
        }
    }
    // 最后检查栈是否为空
    return st.empty();
}

// 接雨水
// 题目来源：LeetCode 42. 接雨水
// 链接：https://leetcode.cn/problems/trapping-rain-water/
// 解题思路（单调栈）：使用单调栈来记录可能形成水坑的位置。当遇到一个比栈顶元素更高的柱子时，说明可能形成了一个水坑，
// 弹出栈顶元素作为坑底，新的栈顶元素作为左边界，当前柱子作为右边界，计算可以接的雨水量。
// 时间复杂度分析：O(n) - 每个元素最多入栈出栈一次
// 空间复杂度分析：O(n) - 最坏情况下栈中存储所有元素
int trap(vector<int>& height) {
    int n = height.size();
    if (n < 3) {  // 至少需要3个柱子才能接雨水
        return 0;
    }
    
    stack<int> st;  // 存储索引
    int water = 0;
    
    for (int i = 0; i < n; i++) {
        // 当前高度大于栈顶高度时，说明可以形成水坑
        while (!st.empty() && height[i] > height[st.top()]) {
            int bottom = st.top();
            st.pop();
            
            if (st.empty()) {  // 没有左边界
                break;
            }
            
            int left = st.top();
            int width = i - left - 1;
            int h = min(height[left], height[i]) - height[bottom];
            water += width * h;
        }
        st.push(i);
    }
    
    return water;
}

// 柱状图中最大的矩形
// 题目来源：LeetCode 84. 柱状图中最大的矩形
// 链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
// 解题思路（单调栈）：使用单调栈来找到每个柱子左边和右边第一个比它小的柱子的位置。对于每个柱子，
// 其能形成的最大矩形的宽度是右边界减去左边界减一，高度是柱子本身的高度。
// 时间复杂度分析：O(n) - 每个元素最多入栈出栈一次
// 空间复杂度分析：O(n) - 栈的最大空间为n
int largestRectangleArea(vector<int>& heights) {
    int n = heights.size();
    if (n == 0) {
        return 0;
    }
    
    stack<int> st;  // 存储索引
    int max_area = 0;
    
    for (int i = 0; i <= n; i++) {
        // 当i=n时，将高度视为0，用于处理栈中剩余的元素
        int h = (i == n) ? 0 : heights[i];
        
        // 当当前高度小于栈顶高度时，计算栈顶柱子能形成的最大矩形
        while (!st.empty() && h < heights[st.top()]) {
            int height_val = heights[st.top()];
            st.pop();
            int width = st.empty() ? i : (i - st.top() - 1);
            max_area = max(max_area, height_val * width);
        }
        st.push(i);
    }
    
    return max_area;
}

// 每日温度
// 题目来源：LeetCode 739. 每日温度
// 链接：https://leetcode.cn/problems/daily-temperatures/
// 解题思路（单调栈）：使用单调栈来存储温度的索引。遍历数组，当遇到一个温度比栈顶温度高时，说明找到了栈顶温度的下一个更高温度，
// 计算天数差并更新结果数组，然后弹出栈顶元素，继续比较新的栈顶元素，直到栈为空或栈顶温度不小于当前温度。
// 时间复杂度分析：O(n) - 每个元素最多入栈出栈一次
// 空间复杂度分析：O(n) - 栈的最大空间为n
vector<int> dailyTemperatures(vector<int>& temperatures) {
    int n = temperatures.size();
    vector<int> answer(n, 0);
    stack<int> st;  // 存储索引
    
    for (int i = 0; i < n; i++) {
        // 当前温度大于栈顶温度时，更新结果
        while (!st.empty() && temperatures[i] > temperatures[st.top()]) {
            int prev_index = st.top();
            st.pop();
            answer[prev_index] = i - prev_index;
        }
        st.push(i);
    }
    
    return answer;
}

// 滑动窗口最大值
// 题目来源：LeetCode 239. 滑动窗口最大值
// 链接：https://leetcode.cn/problems/sliding-window-maximum/
// 解题思路（单调队列）：使用双端队列来维护窗口中的最大值。队列中的元素是数组的索引，对应的数组值是单调递减的。
// 当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有小于当前元素的索引，
// 因为它们不可能成为窗口中的最大值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最大值的索引。
// 时间复杂度分析：O(n) - 每个元素最多入队出队一次
// 空间复杂度分析：O(k) - 队列的最大空间为k
vector<int> maxSlidingWindow(vector<int>& nums, int k) {
    int n = nums.size();
    vector<int> result;
    deque<int> dq;  // 存储索引，对应的值单调递减
    
    for (int i = 0; i < n; i++) {
        // 移除队列中不在窗口内的元素（即索引小于i-k+1的元素）
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        // 移除队列中所有小于当前元素的索引
        while (!dq.empty() && nums[dq.back()] < nums[i]) {
            dq.pop_back();
        }
        
        dq.push_back(i);
        
        // 当窗口形成时，队列头部是窗口中的最大值的索引
        if (i >= k - 1) {
            result.push_back(nums[dq.front()]);
        }
    }
    
    return result;
}

// 设计循环双端队列
// 题目来源：LeetCode 641. 设计循环双端队列
// 链接：https://leetcode.cn/problems/design-circular-deque/
// 解题思路：使用数组实现循环双端队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
// 对于头部插入和删除操作，需要处理指针的循环特性。
// 时间复杂度分析：所有操作都是O(1)时间复杂度
// 空间复杂度分析：O(k) - k是队列的容量
class MyCircularDeque {
private:
    vector<int> deque;
    int front, rear, size, limit;
public:
    MyCircularDeque(int k) {
        deque.resize(k);
        front = rear = size = 0;
        limit = k;
    }
    
    bool insertFront(int value) {
        if (isFull()) {
            return false;
        }
        
        if (isEmpty()) {
            front = rear = 0;
            deque[0] = value;
        } else {
            front = (front == 0) ? limit - 1 : front - 1;
            deque[front] = value;
        }
        size++;
        return true;
    }
    
    bool insertLast(int value) {
        if (isFull()) {
            return false;
        }
        
        if (isEmpty()) {
            front = rear = 0;
            deque[0] = value;
        } else {
            rear = (rear == limit - 1) ? 0 : rear + 1;
            deque[rear] = value;
        }
        size++;
        return true;
    }
    
    bool deleteFront() {
        if (isEmpty()) {
            return false;
        }
        
        front = (front == limit - 1) ? 0 : front + 1;
        size--;
        return true;
    }
    
    bool deleteLast() {
        if (isEmpty()) {
            return false;
        }
        
        rear = (rear == 0) ? limit - 1 : rear - 1;
        size--;
        return true;
    }
    
    int getFront() {
        if (isEmpty()) {
            return -1;
        }
        return deque[front];
    }
    
    int getRear() {
        if (isEmpty()) {
            return -1;
        }
        return deque[rear];
    }
    
    bool isEmpty() {
        return size == 0;
    }
    
    bool isFull() {
        return size == limit;
    }
};

// 逆波兰表达式求值
// 题目来源：LeetCode 150. 逆波兰表达式求值
// 链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
// 解题思路：使用栈来存储操作数。遍历表达式，遇到数字时将其转换为整数并入栈，遇到运算符时弹出栈顶的两个操作数，
// 进行相应的运算，然后将结果压入栈中。最后栈中只剩下一个元素，即为表达式的结果。
// 时间复杂度分析：O(n) - 需要遍历整个表达式
// 空间复杂度分析：O(n) - 最坏情况下栈中存储所有操作数
int evalRPN(vector<string>& tokens) {
    stack<int> st;
    
    for (string& token : tokens) {
        if (token == "+" || token == "-" || token == "*" || token == "/") {
            // 弹出两个操作数
            int b = st.top();
            st.pop();
            int a = st.top();
            st.pop();
            
            // 进行相应的运算
            if (token == "+") {
                st.push(a + b);
            } else if (token == "-") {
                st.push(a - b);
            } else if (token == "*") {
                st.push(a * b);
            } else if (token == "/") {
                st.push(a / b);
            }
        } else {
            // 遇到数字，转换为整数并入栈
            st.push(stoi(token));
        }
    }
    
    return st.top();
}

// 字符串解码
// 题目来源：LeetCode 394. 字符串解码
// 链接：https://leetcode.cn/problems/decode-string/
// 解题思路：使用两个栈，一个存储数字，一个存储字符串。遍历字符串，遇到数字时解析完整的数字，遇到'['时将当前数字和字符串入栈，
// 遇到']'时弹出栈顶的数字和字符串，将当前字符串重复数字次后与弹出的字符串拼接。
// 时间复杂度分析：O(n) - 需要遍历整个字符串，每个字符最多被处理一次
// 空间复杂度分析：O(n) - 栈的最大空间为n
string decodeString(string s) {
    stack<int> num_stack;  // 存储重复次数
    stack<string> str_stack;  // 存储中间字符串
    string current_str = "";
    int num = 0;
    
    for (char c : s) {
        if (isdigit(c)) {
            // 解析完整的数字
            num = num * 10 + (c - '0');
        } else if (c == '[') {
            // 将当前数字和字符串入栈
            num_stack.push(num);
            str_stack.push(current_str);
            num = 0;
            current_str = "";
        } else if (c == ']') {
            // 弹出栈顶的数字和字符串，进行拼接
            int repeat = num_stack.top();
            num_stack.pop();
            string prev_str = str_stack.top();
            str_stack.pop();
            
            string temp = "";
            for (int i = 0; i < repeat; i++) {
                temp += current_str;
            }
            current_str = prev_str + temp;
        } else {
            // 普通字符，添加到当前字符串
            current_str += c;
        }
    }
    
    return current_str;
}

// 删除字符串中的所有相邻重复项
// 题目来源：LeetCode 1047. 删除字符串中的所有相邻重复项
// 链接：https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string/
// 解题思路：使用栈来存储字符。遍历字符串，对于每个字符，如果栈不为空且栈顶元素与当前字符相同，则弹出栈顶元素，
// 否则将当前字符压入栈中。最后将栈中的元素按顺序拼接成字符串。
// 时间复杂度分析：O(n) - 需要遍历整个字符串
// 空间复杂度分析：O(n) - 栈的最大空间为n
string removeDuplicates(string S) {
    stack<char> st;
    
    for (char c : S) {
        if (!st.empty() && st.top() == c) {
            st.pop();
        } else {
            st.push(c);
        }
    }
    
    string result = "";
    while (!st.empty()) {
        result = st.top() + result;
        st.pop();
    }
    
    return result;
}

// 基本计算器 II
// 题目来源：LeetCode 227. 基本计算器 II
// 链接：https://leetcode.cn/problems/basic-calculator-ii/
// 解题思路：使用栈来存储操作数。遍历字符串，遇到数字时解析完整的数字，遇到运算符时根据前一个运算符的类型进行相应的运算。
// 对于加减运算，将操作数压入栈中；对于乘除运算，弹出栈顶元素与当前操作数进行运算后将结果压入栈中。
// 最后将栈中的所有元素相加得到最终结果。
// 时间复杂度分析：O(n) - 需要遍历整个字符串
// 空间复杂度分析：O(n) - 栈的最大空间为n/2
int calculate(string s) {
    stack<int> st;
    char pre_sign = '+';  // 前一个运算符
    int num = 0;
    int n = s.size();
    
    for (int i = 0; i < n; i++) {
        char c = s[i];
        
        if (isdigit(c)) {
            // 解析完整的数字
            num = num * 10 + (c - '0');
        }
        
        // 遇到运算符或到达字符串末尾
        if (!isdigit(c) && c != ' ' || i == n - 1) {
            if (pre_sign == '+') {
                st.push(num);
            } else if (pre_sign == '-') {
                st.push(-num);
            } else if (pre_sign == '*') {
                st.top() *= num;
            } else if (pre_sign == '/') {
                st.top() /= num;
            }
            pre_sign = c;
            num = 0;
        }
    }
    
    // 将栈中的所有元素相加
    int result = 0;
    while (!st.empty()) {
        result += st.top();
        st.pop();
    }
    
    return result;
}

// 主函数：测试所有算法实现
int main() {
    cout << "队列和栈相关算法实现测试" << endl;
    
    // 测试有效的括号
    string s = "()[]{}";
    cout << "有效的括号测试结果: " << boolalpha << isValid(s) << endl;
    
    // 测试最小栈
    MinStack min_stack;
    min_stack.push(-2);
    min_stack.push(0);
    min_stack.push(-3);
    cout << "最小栈最小值: " << min_stack.getMin() << endl;
    min_stack.pop();
    cout << "最小栈栈顶: " << min_stack.top() << endl;
    cout << "最小栈最小值: " << min_stack.getMin() << endl;
    
    // 测试接雨水
    vector<int> height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
    cout << "接雨水结果: " << trap(height) << endl;
    
    // 测试柱状图中最大的矩形
    vector<int> heights = {2, 1, 5, 6, 2, 3};
    cout << "柱状图中最大的矩形面积: " << largestRectangleArea(heights) << endl;
    
    // 测试每日温度
    vector<int> temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
    vector<int> daily_temp_result = dailyTemperatures(temperatures);
    cout << "每日温度结果: ";
    for (int num : daily_temp_result) {
        cout << num << " ";
    }
    cout << endl;
    
    // 测试滑动窗口最大值
    vector<int> nums = {1, 3, -1, -3, 5, 3, 6, 7};
    int k = 3;
    vector<int> max_window_result = maxSlidingWindow(nums, k);
    cout << "滑动窗口最大值结果: ";
    for (int num : max_window_result) {
        cout << num << " ";
    }
    cout << endl;
    
    // 测试循环双端队列
    MyCircularDeque deque(3);
    cout << "循环双端队列插入尾部: " << boolalpha << deque.insertLast(1) << endl;
    cout << "循环双端队列插入尾部: " << boolalpha << deque.insertLast(2) << endl;
    cout << "循环双端队列插入头部: " << boolalpha << deque.insertFront(3) << endl;
    cout << "循环双端队列插入头部: " << boolalpha << deque.insertFront(4) << endl;
    cout << "循环双端队列尾部元素: " << deque.getRear() << endl;
    cout << "循环双端队列是否已满: " << boolalpha << deque.isFull() << endl;
    cout << "循环双端队列删除尾部: " << boolalpha << deque.deleteLast() << endl;
    cout << "循环双端队列插入头部: " << boolalpha << deque.insertFront(4) << endl;
    cout << "循环双端队列头部元素: " << deque.getFront() << endl;
    
    // 测试逆波兰表达式求值
    vector<string> tokens = {"2", "1", "+", "3", "*"};
    cout << "逆波兰表达式求值结果: " << evalRPN(tokens) << endl;
    
    // 测试字符串解码
    string encode_str = "3[a]2[bc]";
    cout << "字符串解码结果: " << decodeString(encode_str) << endl;
    
    // 测试删除字符串中的所有相邻重复项
    string S = "abbaca";
    cout << "删除相邻重复项结果: " << removeDuplicates(S) << endl;
    
    // 测试基本计算器 II
    string expr = "3+2*2";
    cout << "基本计算器 II 结果: " << calculate(expr) << endl;
    
    return 0;
}

/**
 * 接雨水
 * 题目来源：LeetCode 42. 接雨水
 * 链接：https://leetcode.cn/problems/trapping-rain-water/
 * 
 * 题目描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 解题思路（单调栈）：
 * 使用单调栈来记录可能形成水坑的位置。当遇到一个比栈顶元素更高的柱子时，说明可能形成了一个水坑，
 * 弹出栈顶元素作为坑底，新的栈顶元素作为左边界，当前柱子作为右边界，计算可以接的雨水量。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下栈中存储所有元素
 */
int trap(vector<int>& height) {
    int n = height.size();
    if (n < 3) return 0; // 至少需要3个柱子才能接雨水
    
    stack<int> st; // 存储索引
    int water = 0;
    
    for (int i = 0; i < n; i++) {
        // 当前高度大于栈顶高度时，说明可以形成水坑
        while (!st.empty() && height[i] > height[st.top()]) {
            int bottom = st.top();
            st.pop();
            
            if (st.empty()) break; // 没有左边界
            
            int left = st.top();
            int width = i - left - 1;
            int h = min(height[left], height[i]) - height[bottom];
            water += width * h;
        }
        st.push(i);
    }
    
    return water;
}



/**
 * 滑动窗口最大值
 * 题目来源：LeetCode 239. 滑动窗口最大值
 * 链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回 滑动窗口中的最大值 。
 * 
 * 解题思路（单调队列）：
 * 使用单调队列来维护窗口中的最大值。队列中的元素是数组的索引，对应的数组值是单调递减的。
 * 当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有小于当前元素的索引，
 * 因为它们不可能成为窗口中的最大值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最大值的索引。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 * 
 * 空间复杂度分析：
 * O(k) - 队列的最大空间为k
 */
vector<int> maxSlidingWindow(vector<int>& nums, int k) {
    int n = nums.size();
    vector<int> result;
    deque<int> dq; // 存储索引，对应的值单调递减
    
    for (int i = 0; i < n; i++) {
        // 移除队列中不在窗口内的元素（即索引小于i-k+1的元素）
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        // 移除队列中所有小于当前元素的索引
        while (!dq.empty() && nums[dq.back()] < nums[i]) {
            dq.pop_back();
        }
        
        dq.push_back(i);
        
        // 当窗口形成时，队列头部是窗口中的最大值的索引
        if (i >= k - 1) {
            result.push_back(nums[dq.front()]);
        }
    }
    
    return result;
}

/**
 * 设计循环双端队列
 * 题目来源：LeetCode 641. 设计循环双端队列
 * 链接：https://leetcode.cn/problems/design-circular-deque/
 * 
 * 题目描述：
 * 设计实现双端队列。
 * 实现 MyCircularDeque 类:
 * MyCircularDeque(int k) ：构造函数，双端队列最大为 k 。
 * boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
 * boolean insertLast(int value) ：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
 * boolean deleteFront() ：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
 * boolean deleteLast() ：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
 * int getFront() )：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
 * int getRear() ：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
 * boolean isEmpty() ：若双端队列为空，则返回 true ，否则返回 false 。
 * boolean isFull() ：若双端队列满了，则返回 true ，否则返回 false 。
 * 
 * 解题思路：
 * 使用数组实现循环双端队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
 * 对于头部插入和删除操作，需要处理指针的循环特性。
 * 
 * 时间复杂度分析：
 * 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * O(k) - k是队列的容量
 */
class MyCircularDeque {
private:
    vector<int> deque;
    int l, r, size, limit;

public:
    MyCircularDeque(int k) {
        deque.resize(k);
        l = r = size = 0;
        limit = k;
    }

    bool insertFront(int value) {
        if (isFull()) {
            return false;
        }
        
        if (isEmpty()) {
            l = r = 0;
            deque[0] = value;
        } else {
            l = l == 0 ? limit - 1 : l - 1;
            deque[l] = value;
        }
        size++;
        return true;
    }

    bool insertLast(int value) {
        if (isFull()) {
            return false;
        }
        
        if (isEmpty()) {
            l = r = 0;
            deque[0] = value;
        } else {
            r = r == limit - 1 ? 0 : r + 1;
            deque[r] = value;
        }
        size++;
        return true;
    }

    bool deleteFront() {
        if (isEmpty()) {
            return false;
        }
        
        l = l == limit - 1 ? 0 : l + 1;
        size--;
        return true;
    }

    bool deleteLast() {
        if (isEmpty()) {
            return false;
        }
        
        r = r == 0 ? limit - 1 : r - 1;
        size--;
        return true;
    }

    int getFront() {
        if (isEmpty()) {
            return -1;
        }
        return deque[l];
    }

    int getRear() {
        if (isEmpty()) {
            return -1;
        }
        return deque[r];
    }

    bool isEmpty() {
        return size == 0;
    }

    bool isFull() {
        return size == limit;
    }
};

/**
 * 逆波兰表达式求值
 * 题目来源：LeetCode 150. 逆波兰表达式求值
 * 链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
 * 
 * 题目描述：
 * 给你一个字符串数组 tokens ，表示一个根据 逆波兰表示法 表示的算术表达式。
 * 请你计算该表达式。返回一个表示表达式值的整数。
 * 注意：
 * 有效的算符为 '+'、'-'、'*' 和 '/' 。
 * 每个操作数可以是整数，也可以是另一个表达式的结果。
 * 除法运算向零截断。
 * 表达式中不含除零运算。
 * 输入是一个根据逆波兰表示法表示的算术表达式。
 * 逆波兰表达式是一种后缀表达式，所谓后缀就是指算符写在后面。
 * 
 * 解题思路：
 * 使用栈来存储操作数。遍历表达式，遇到数字时将其转换为整数并入栈，遇到运算符时弹出栈顶的两个操作数，
 * 进行相应的运算，然后将结果压入栈中。最后栈中只剩下一个元素，即为表达式的结果。
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历整个表达式
 * 
 * 空间复杂度分析：
 * O(n) - 最坏情况下栈中存储所有操作数
 */
int evalRPN(vector<string>& tokens) {
    stack<int> st;
    
    for (const string& token : tokens) {
        if (token == "+" || token == "-" || token == "*" || token == "/") {
            // 弹出两个操作数
            int b = st.top(); st.pop();
            int a = st.top(); st.pop();
            
            // 进行相应的运算
            if (token == "+") st.push(a + b);
            else if (token == "-") st.push(a - b);
            else if (token == "*") st.push(a * b);
            else if (token == "/") st.push(a / b);
        } else {
            // 遇到数字，转换为整数并入栈
            st.push(stoi(token));
        }
    }
    
    return st.top();
}

/**
 * 字符串解码
 * 题目来源：LeetCode 394. 字符串解码
 * 链接：https://leetcode.cn/problems/decode-string/
 * 
 * 题目描述：
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 * 
 * 解题思路：
 * 使用两个栈，一个存储数字，一个存储字符串。遍历字符串，遇到数字时解析完整的数字，遇到'['时将当前数字和字符串入栈，
 * 遇到']'时弹出栈顶的数字和字符串，将当前字符串重复数字次后与弹出的字符串拼接。
 * 
 * 时间复杂度分析：
 * O(n) - 需要遍历整个字符串，每个字符最多被处理一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的最大空间为n
 */
string decodeString(string s) {
    stack<int> numStack; // 存储重复次数
    stack<string> strStack; // 存储中间字符串
    string currentStr = "";
    int num = 0;
    
    for (char c : s) {
        if (isdigit(c)) {
            // 解析完整的数字
            num = num * 10 + (c - '0');
        } else if (c == '[') {
            // 将当前数字和字符串入栈
            numStack.push(num);
            strStack.push(currentStr);
            num = 0;
            currentStr = "";
        } else if (c == ']') {
            // 弹出栈顶的数字和字符串，进行拼接
            int repeat = numStack.top(); numStack.pop();
            string prevStr = strStack.top(); strStack.pop();
            
            string temp = "";
            for (int i = 0; i < repeat; i++) {
                temp += currentStr;
            }
            
            currentStr = prevStr + temp;
        } else {
            // 普通字符，添加到当前字符串
            currentStr += c;
        }
    }
    
    return currentStr;
}

/**
 * 删除字符串中的所有相邻重复项 II
 * 题目来源:LeetCode 1209. 删除字符串中的所有相邻重复项 II
 * 链接:https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string-ii/
 * 
 * 题目描述:
 * 给你一个字符串 s,「k 倍重复项删除操作」将会从 s 中选择 k 个相邻且相等的字母,并删除它们,
 * 使被删去的字符串的左侧和右侧连在一起。
 * 你需要对 s 重复进行无限次这样的删除操作,直到无法继续为止。
 * 在执行完所有删除操作后,返回最终得到的字符串。
 * 
 * 解题思路:
 * 使用栈来存储字符和对应的出现次数。遍历字符串,对于每个字符,如果栈不为空且栈顶字符与当前字符相同,
 * 则将栈顶的计数加1,如果计数等于k则弹出栈顶元素;否则将当前字符和计数1入栈。
 * 最后将栈中的元素按顺序拼接成字符串。
 * 
 * 时间复杂度分析:
 * O(n) - 需要遍历整个字符串
 * 
 * 空间复杂度分析:
 * O(n) - 栈的最大空间为n
 * 
 * 是否最优解:是,这是最优解,时间和空间复杂度都无法再优化
 */
string removeDuplicates(string s, int k) {
    // 使用栈存储字符和出现次数
    stack<pair<char, int>> st;
    
    for (char c : s) {
        if (!st.empty() && st.top().first == c) {
            // 如果栈顶字符与当前字符相同,增加计数
            int count = st.top().second + 1;
            if (count == k) {
                // 如果计数等于k,弹出栈顶元素
                st.pop();
            } else {
                // 否则更新计数
                st.pop();
                st.push({c, count});
            }
        } else {
            // 如果栈为空或栈顶字符与当前字符不同,将当前字符和计数1入栈
            st.push({c, 1});
        }
    }
    
    // 构建结果字符串
    string result = "";
    while (!st.empty()) {
        auto p = st.top();
        st.pop();
        result = string(p.second, p.first) + result;
    }
    
    return result;
}

/**
 * 下一个更大元素 I
 * 题目来源:LeetCode 496. 下一个更大元素 I
 * 链接:https://leetcode.cn/problems/next-greater-element-i/
 * 
 * 题目描述:
 * nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置 右侧 的 第一个 比 x 大的元素。
 * 给你两个 没有重复元素 的数组 nums1 和 nums2 ,下标从 0 开始计数,其中nums1 是 nums2 的子集。
 * 对于每个 0 <= i < nums1.length ,找出满足 nums1[i] == nums2[j] 的下标 j ,并且在 nums2 确定 nums2[j] 的 下一个更大元素 。
 * 如果不存在下一个更大元素,那么本次查询的答案是 -1 。
 * 返回一个长度为 nums1.length 的数组 ans 作为答案,满足 ans[i] 是如上所述的 下一个更大元素 。
 * 
 * 解题思路(单调栈):
 * 使用单调栈来找到nums2中每个元素的下一个更大元素。从右往左遍历nums2,维护一个单调递减的栈,
 * 对于每个元素,弹出栈中所有小于等于当前元素的值,栈顶元素即为当前元素的下一个更大元素。
 * 用HashMap存储每个元素及其下一个更大元素的映射关系。
 * 
 * 时间复杂度分析:
 * O(m + n) - m是nums1的长度,n是nums2的长度,每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析:
 * O(n) - 栈和HashMap的空间
 * 
 * 是否最优解:是,这是最优解
 */
vector<int> nextGreaterElement(vector<int>& nums1, vector<int>& nums2) {
    // 使用HashMap存储每个元素及其下一个更大元素
    unordered_map<int, int> map;
    stack<int> st;
    
    // 从右往左遍历nums2
    for (int i = nums2.size() - 1; i >= 0; i--) {
        int num = nums2[i];
        // 弹出栈中所有小于等于当前元素的值
        while (!st.empty() && st.top() <= num) {
            st.pop();
        }
        // 栈顶元素即为当前元素的下一个更大元素
        map[num] = st.empty() ? -1 : st.top();
        st.push(num);
    }
    
    // 构建结果数组
    vector<int> result;
    for (int num : nums1) {
        result.push_back(map[num]);
    }
    
    return result;
}

/**
 * 下一个更大元素 II
 * 题目来源:LeetCode 503. 下一个更大元素 II
 * 链接:https://leetcode.cn/problems/next-greater-element-ii/
 * 
 * 题目描述:
 * 给定一个循环数组 nums( nums[nums.length - 1] 的下一个元素是 nums[0] ),返回 nums 中每个元素的 下一个更大元素 。
 * 数字 x 的 下一个更大的元素 是按数组遍历顺序,这个数字之后的第一个比它更大的数,这意味着你应该循环地搜索它的下一个更大的数。
 * 如果不存在,则输出 -1 。
 * 
 * 解题思路(单调栈):
 * 因为是循环数组,可以将数组遍历两遍。使用单调栈存储索引,当遇到一个元素比栈顶索引对应的元素大时,
 * 说明找到了栈顶元素的下一个更大元素。为了处理循环,遍历时使用取模运算。
 * 
 * 时间复杂度分析:
 * O(n) - 虽然遍历两遍,但每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析:
 * O(n) - 栈的空间
 * 
 * 是否最优解:是,这是最优解
 */
vector<int> nextGreaterElements(vector<int>& nums) {
    int n = nums.size();
    vector<int> result(n, -1);
    stack<int> st; // 存储索引
    
    // 遍历两遍数组以处理循环
    for (int i = 0; i < 2 * n; i++) {
        int num = nums[i % n];
        // 当遇到一个元素比栈顶索引对应的元素大时
        while (!st.empty() && nums[st.top()] < num) {
            result[st.top()] = num;
            st.pop();
        }
        // 只在第一遍遍历时将索引入栈
        if (i < n) {
            st.push(i);
        }
    }
    
    return result;
}

/**
 * 基本计算器
 * 题目来源:LeetCode 224. 基本计算器
 * 链接:https://leetcode.cn/problems/basic-calculator/
 * 
 * 题目描述:
 * 给你一个字符串表达式 s ,请你实现一个基本计算器来计算并返回它的值。
 * 注意:不允许使用任何将字符串作为数学表达式计算的内置函数,比如 eval() 。
 * 表达式可能包含 '(' 和 ')' ,以及 '+' 和 '-' 运算符。
 * 
 * 解题思路:
 * 使用栈来处理括号。遍历字符串,遇到数字时解析完整的数字,遇到运算符时更新符号,
 * 遇到左括号时将当前结果和符号入栈,遇到右括号时弹出栈顶的结果和符号进行计算。
 * 
 * 时间复杂度分析:
 * O(n) - 需要遍历整个字符串
 * 
 * 空间复杂度分析:
 * O(n) - 栈的最大空间为n
 * 
 * 是否最优解:是,这是最优解
 */
int calculateBasic(string s) {
    stack<int> st;
    int result = 0;
    int num = 0;
    int sign = 1; // 1表示正号,-1表示负号
    
    for (int i = 0; i < s.length(); i++) {
        char c = s[i];
        
        if (isdigit(c)) {
            // 解析完整的数字
            num = num * 10 + (c - '0');
        } else if (c == '+') {
            result += sign * num;
            num = 0;
            sign = 1;
        } else if (c == '-') {
            result += sign * num;
            num = 0;
            sign = -1;
        } else if (c == '(') {
            // 遇到左括号,将当前结果和符号入栈
            st.push(result);
            st.push(sign);
            result = 0;
            sign = 1;
        } else if (c == ')') {
            // 遇到右括号,计算括号内的结果
            result += sign * num;
            num = 0;
            // 弹出栈顶的符号和结果
            result *= st.top(); st.pop(); // 弹出符号
            result += st.top(); st.pop(); // 弹出之前的结果
        }
    }
    
    // 处理最后的数字
    if (num != 0) {
        result += sign * num;
    }
    
    return result;
}

/**
 * 简化路径
 * 题目来源:LeetCode 71. 简化路径
 * 链接:https://leetcode.cn/problems/simplify-path/
 * 
 * 题目描述:
 * 给你一个字符串 path ,表示指向某一文件或目录的 Unix 风格 绝对路径 (以 '/' 开头),请你将其转化为更加简洁的规范路径。
 * 在 Unix 风格的文件系统中,一个点(.)表示当前目录本身;此外,两个点 (..) 表示将目录切换到上一级(指向父目录);
 * 两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠(即,'//')都被视为单个斜杠 '/' 。 
 * 对于此问题,任何其他格式的点(例如,'...')均被视为文件/目录名称。
 * 
 * 解题思路:
 * 使用栈来处理路径。将路径按'/'分割成各个部分,遍历每个部分:
 * - 如果是"..",则弹出栈顶元素(如果栈不为空)
 * - 如果是"."或空字符串,则忽略
 * - 否则将部分压入栈中
 * 最后将栈中的元素按顺序拼接成路径。
 * 
 * 时间复杂度分析:
 * O(n) - 需要遍历整个路径字符串
 * 
 * 空间复杂度分析:
 * O(n) - 栈的空间
 * 
 * 是否最优解:是,这是最优解
 */
string simplifyPath(string path) {
    vector<string> stack;
    stringstream ss(path);
    string part;
    
    while (getline(ss, part, '/')) {
        if (part == "..") {
            // 返回上一级目录
            if (!stack.empty()) {
                stack.pop_back();
            }
        } else if (!part.empty() && part != ".") {
            // 进入下一级目录
            stack.push_back(part);
        }
        // 如果是"."或空字符串,则忽略
    }
    
    // 构建结果路径
    string result = "";
    for (const string& dir : stack) {
        result += "/" + dir;
    }
    
    return result.empty() ? "/" : result;
}

/**
 * 比较含退格的字符串
 * 题目来源:LeetCode 844. 比较含退格的字符串
 * 链接:https://leetcode.cn/problems/backspace-string-compare/
 * 
 * 题目描述:
 * 给定 s 和 t 两个字符串,当它们分别被输入到空白的文本编辑器后,如果两者相等,返回 true 。
 * # 代表退格字符。
 * 注意:如果对空文本输入退格字符,文本继续为空。
 * 
 * 解题思路:
 * 使用栈来模拟退格操作。遍历字符串,遇到非'#'字符时压入栈,遇到'#'时弹出栈顶元素(如果栈不为空)。
 * 最后比较两个栈是否相等。
 * 
 * 优化方案:可以使用双指针从后往前遍历,不需要额外的栈空间,空间复杂度O(1)。
 * 
 * 时间复杂度分析:
 * O(m + n) - m和n是两个字符串的长度
 * 
 * 空间复杂度分析:
 * O(m + n) - 两个栈的空间
 * 优化后:O(1)
 * 
 * 是否最优解:栈的方法不是最优解,双指针方法是最优解
 */
bool backspaceCompare(string s, string t) {
    return buildString(s) == buildString(t);
}

string buildString(string s) {
    string stack = "";
    for (char c : s) {
        if (c != '#') {
            stack += c;
        } else if (!stack.empty()) {
            stack.pop_back();
        }
    }
    return stack;
}

// 双指针优化版本(最优解)
bool backspaceCompareOptimized(string s, string t) {
    int i = s.length() - 1, j = t.length() - 1;
    int skipS = 0, skipT = 0;
    
    while (i >= 0 || j >= 0) {
        // 找到s中下一个有效字符
        while (i >= 0) {
            if (s[i] == '#') {
                skipS++;
                i--;
            } else if (skipS > 0) {
                skipS--;
                i--;
            } else {
                break;
            }
        }
        
        // 找到t中下一个有效字符
        while (j >= 0) {
            if (t[j] == '#') {
                skipT++;
                j--;
            } else if (skipT > 0) {
                skipT--;
                j--;
            } else {
                break;
            }
        }
        
        // 比较字符
        if (i >= 0 && j >= 0) {
            if (s[i] != t[j]) {
                return false;
            }
        } else if (i >= 0 || j >= 0) {
            return false;
        }
        
        i--;
        j--;
    }
    
    return true;
}

/**
 * 移掉 K 位数字
 * 题目来源:LeetCode 402. 移掉 K 位数字
 * 链接:https://leetcode.cn/problems/remove-k-digits/
 * 
 * 题目描述:
 * 给你一个以字符串表示的非负整数 num 和一个整数 k ,移除这个数中的 k 位数字,使得剩下的数字最小。
 * 请你以字符串形式返回这个最小的数字。
 * 
 * 解题思路(单调栈):
 * 使用单调栈维护一个单调递增的数字序列。遍历数字字符串,如果当前数字小于栈顶数字且还可以移除数字(k>0),
 * 则弹出栈顶数字并将k减1。遍历完成后,如果k还大于0,则从栈顶继续移除k个数字。
 * 最后去除前导0并返回结果。
 * 
 * 时间复杂度分析:
 * O(n) - 每个数字最多入栈出栈一次
 * 
 * 空间复杂度分析:
 * O(n) - 栈的空间
 * 
 * 是否最优解:是,这是最优解
 */
string removeKdigits(string num, int k) {
    string stack = "";
    
    for (char digit : num) {
        // 如果当前数字小于栈顶数字且还可以移除数字,则弹出栈顶
        while (!stack.empty() && k > 0 && stack.back() > digit) {
            stack.pop_back();
            k--;
        }
        stack += digit;
    }
    
    // 如果k还大于0,从栈顶继续移除
    while (k > 0 && !stack.empty()) {
        stack.pop_back();
        k--;
    }
    
    // 构建结果字符串,去除前导0
    string result = "";
    bool leadingZero = true;
    for (char digit : stack) {
        if (leadingZero && digit == '0') continue;
        leadingZero = false;
        result += digit;
    }
    
    return result.empty() ? "0" : result;
}

/**
 * 验证栈序列
 * 题目来源:LeetCode 946. 验证栈序列
 * 链接:https://leetcode.cn/problems/validate-stack-sequences/
 * 
 * 题目描述:
 * 给定 pushed 和 popped 两个序列,每个序列中的 值都不重复,
 * 只有当它们可能是在最初空栈上进行的推入 push 和弹出 pop 操作序列的结果时,返回 true;否则,返回 false 。
 * 
 * 解题思路:
 * 使用栈模拟入栈和出栈操作。遍历pushed数组,将元素依次入栈,每次入栈后检查栈顶元素是否等于popped数组的当前元素,
 * 如果相等则出栈并移动popped的指针。最后检查栈是否为空。
 * 
 * 时间复杂度分析:
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析:
 * O(n) - 栈的空间
 * 
 * 是否最优解:是,这是最优解
 */
bool validateStackSequences(vector<int>& pushed, vector<int>& popped) {
    stack<int> st;
    int j = 0; // popped数组的指针
    
    for (int num : pushed) {
        st.push(num);
        // 检查栈顶元素是否等于popped的当前元素
        while (!st.empty() && st.top() == popped[j]) {
            st.pop();
            j++;
        }
    }
    
    return st.empty();
}

/**
 * 单调栈和单调队列的扩展应用
 * 以下是一些重要的扩展题目，涵盖了各种算法平台上的经典问题
 */

/**
 * 132模式
 * 题目来源：LeetCode 456. 132模式
 * 链接：https://leetcode.cn/problems/132-pattern/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
 * 如果 nums 中存在 132 模式的子序列，返回 true ；否则，返回 false 。
 * 
 * 解题思路（单调栈）：
 * 从右往左遍历数组，维护一个单调递减的栈，同时记录第二大的元素（即132模式中的2）。
 * 当遇到一个比第二大的元素还小的元素时，说明找到了132模式。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间
 * 
 * 是否最优解：是
 */
bool find132pattern(vector<int>& nums) {
    int n = nums.size();
    if (n < 3) return false;
    
    stack<int> st;
    int second = INT_MIN; // 132模式中的2
    
    for (int i = n - 1; i >= 0; i--) {
        // 如果当前元素小于second，说明找到了132模式
        if (nums[i] < second) return true;
        
        // 维护单调递减栈
        while (!st.empty() && nums[i] > st.top()) {
            second = st.top(); // 更新第二大的元素
            st.pop();
        }
        
        st.push(nums[i]);
    }
    
    return false;
}

/**
 * 去除重复字母
 * 题目来源：LeetCode 316. 去除重复字母
 * 链接：https://leetcode.cn/problems/remove-duplicate-letters/
 * 
 * 题目描述：
 * 给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。
 * 需保证 返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
 * 
 * 解题思路（单调栈）：
 * 使用单调栈维护一个字典序最小的结果。同时记录每个字符的最后出现位置和是否在栈中。
 * 遍历字符串，如果当前字符不在栈中，且比栈顶字符小，并且栈顶字符在后面还会出现，则弹出栈顶字符。
 * 
 * 时间复杂度分析：
 * O(n) - 每个字符最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(1) - 因为字符集大小固定（26个字母）
 * 
 * 是否最优解：是
 */
string removeDuplicateLetters(string s) {
    vector<int> lastIndex(26, -1); // 记录每个字符最后出现的位置
    vector<bool> inStack(26, false); // 记录字符是否在栈中
    stack<char> st;
    
    // 记录每个字符最后出现的位置
    for (int i = 0; i < s.length(); i++) {
        lastIndex[s[i] - 'a'] = i;
    }
    
    for (int i = 0; i < s.length(); i++) {
        char c = s[i];
        
        // 如果字符已经在栈中，跳过
        if (inStack[c - 'a']) continue;
        
        // 如果栈不为空，且当前字符比栈顶字符小，且栈顶字符在后面还会出现，则弹出栈顶
        while (!st.empty() && c < st.top() && lastIndex[st.top() - 'a'] > i) {
            inStack[st.top() - 'a'] = false;
            st.pop();
        }
        
        st.push(c);
        inStack[c - 'a'] = true;
    }
    
    // 构建结果字符串
    string result = "";
    while (!st.empty()) {
        result = st.top() + result;
        st.pop();
    }
    
    return result;
}

/**
 * 最大矩形
 * 题目来源：LeetCode 85. 最大矩形
 * 链接：https://leetcode.cn/problems/maximal-rectangle/
 * 
 * 题目描述：
 * 给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
 * 
 * 解题思路（单调栈）：
 * 将问题转化为多个柱状图最大矩形问题。对于每一行，计算以该行为底边的柱状图高度，
 * 然后使用柱状图最大矩形的单调栈解法。
 * 
 * 时间复杂度分析：
 * O(m*n) - m是行数，n是列数
 * 
 * 空间复杂度分析：
 * O(n) - 高度数组和栈的空间
 * 
 * 是否最优解：是
 */
int maximalRectangle(vector<vector<char>>& matrix) {
    if (matrix.empty() || matrix[0].empty()) return 0;
    
    int m = matrix.size();
    int n = matrix[0].size();
    vector<int> heights(n, 0);
    int maxArea = 0;
    
    for (int i = 0; i < m; i++) {
        // 更新高度数组
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] == '1') {
                heights[j] += 1;
            } else {
                heights[j] = 0;
            }
        }
        
        // 计算当前行的最大矩形面积
        stack<int> st;
        for (int j = 0; j <= n; j++) {
            int h = (j == n) ? 0 : heights[j];
            
            while (!st.empty() && h < heights[st.top()]) {
                int height = heights[st.top()];
                st.pop();
                int width = st.empty() ? j : (j - st.top() - 1);
                maxArea = max(maxArea, height * width);
            }
            st.push(j);
        }
    }
    
    return maxArea;
}

/**
 * 滑动窗口最小值
 * 题目来源：LeetCode 239. 滑动窗口最小值（扩展）
 * 链接：https://leetcode.cn/problems/sliding-window-maximum/（类似题目）
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最小值。
 * 
 * 解题思路（单调队列）：
 * 使用单调队列维护窗口中的最小值。队列中的元素是数组的索引，对应的数组值是单调递增的。
 * 当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有大于当前元素的索引，
 * 因为它们不可能成为窗口中的最小值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最小值的索引。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入队出队一次
 * 
 * 空间复杂度分析：
 * O(k) - 队列的最大空间为k
 * 
 * 是否最优解：是
 */
vector<int> minSlidingWindow(vector<int>& nums, int k) {
    int n = nums.size();
    vector<int> result;
    deque<int> dq; // 存储索引，对应的值单调递增
    
    for (int i = 0; i < n; i++) {
        // 移除队列中不在窗口内的元素
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        // 移除队列中所有大于当前元素的索引
        while (!dq.empty() && nums[dq.back()] > nums[i]) {
            dq.pop_back();
        }
        
        dq.push_back(i);
        
        // 当窗口形成时，队列头部是窗口中的最小值的索引
        if (i >= k - 1) {
            result.push_back(nums[dq.front()]);
        }
    }
    
    return result;
}

/**
 * 子数组的最小值之和
 * 题目来源：LeetCode 907. 子数组的最小值之和
 * 链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
 * 
 * 题目描述：
 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
 * 由于答案可能很大，因此返回答案模 10^9 + 7。
 * 
 * 解题思路（单调栈）：
 * 使用单调栈找到每个元素作为最小值出现的子数组范围。对于每个元素，找到左边第一个比它小的元素位置和右边第一个比它小的元素位置，
 * 那么该元素作为最小值的子数组个数为 (i - left) * (right - i)。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间
 * 
 * 是否最优解：是
 */
int sumSubarrayMins(vector<int>& arr) {
    int n = arr.size();
    const int MOD = 1e9 + 7;
    vector<int> left(n, -1);  // 左边第一个比当前元素小的位置
    vector<int> right(n, n);  // 右边第一个比当前元素小的位置
    stack<int> st;
    
    // 计算左边边界
    for (int i = 0; i < n; i++) {
        while (!st.empty() && arr[st.top()] > arr[i]) {
            st.pop();
        }
        left[i] = st.empty() ? -1 : st.top();
        st.push(i);
    }
    
    // 清空栈
    while (!st.empty()) st.pop();
    
    // 计算右边边界
    for (int i = n - 1; i >= 0; i--) {
        while (!st.empty() && arr[st.top()] >= arr[i]) {
            st.pop();
        }
        right[i] = st.empty() ? n : st.top();
        st.push(i);
    }
    
    // 计算总和
    long long sum = 0;
    for (int i = 0; i < n; i++) {
        sum = (sum + (long long)arr[i] * (i - left[i]) * (right[i] - i)) % MOD;
    }
    
    return sum;
}

/**
 * 股票价格跨度
 * 题目来源：LeetCode 901. 股票价格跨度
 * 链接：https://leetcode.cn/problems/online-stock-span/
 * 
 * 题目描述：
 * 编写一个 StockSpanner 类，它收集某些股票的每日报价，并返回该股票当日价格的跨度。
 * 今天股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
 * 
 * 解题思路（单调栈）：
 * 使用单调栈存储价格和对应的跨度。当新价格到来时，弹出栈中所有小于等于当前价格的价格，
 * 并将它们的跨度累加到当前价格的跨度中。
 * 
 * 时间复杂度分析：
 * 每个价格最多入栈出栈一次，均摊O(1)
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间
 * 
 * 是否最优解：是
 */
class StockSpanner {
private:
    stack<pair<int, int>> st; // 存储价格和跨度
    
public:
    StockSpanner() {
    }
    
    int next(int price) {
        int span = 1;
        
        // 弹出栈中所有小于等于当前价格的价格，累加它们的跨度
        while (!st.empty() && st.top().first <= price) {
            span += st.top().second;
            st.pop();
        }
        
        st.push({price, span});
        return span;
    }
};

/**
 * 行星碰撞
 * 题目来源：LeetCode 735. 行星碰撞
 * 链接：https://leetcode.cn/problems/asteroid-collision/
 * 
 * 题目描述：
 * 给定一个整数数组 asteroids，表示在同一行的行星。
 * 对于数组中的每一个元素，其绝对值表示行星的大小，正负表示行星的移动方向（正表示向右移动，负表示向左移动）。
 * 每一颗行星以相同的速度移动。找出碰撞后剩下的所有行星。
 * 碰撞规则：两个行星相互碰撞，较小的行星会爆炸。如果两颗行星大小相同，则两颗行星都会爆炸。
 * 两颗移动方向相同的行星不会发生碰撞。
 * 
 * 解题思路（栈）：
 * 使用栈模拟行星碰撞过程。遍历行星数组，对于每个行星：
 * - 如果栈为空或当前行星向右移动，直接入栈
 * - 如果当前行星向左移动，与栈顶行星碰撞，直到栈为空或栈顶行星向左移动
 * 
 * 时间复杂度分析：
 * O(n) - 每个行星最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间
 * 
 * 是否最优解：是
 */
vector<int> asteroidCollision(vector<int>& asteroids) {
    vector<int> st;
    
    for (int asteroid : asteroids) {
        bool destroyed = false;
        
        // 当前行星向左移动，且栈顶行星向右移动时发生碰撞
        while (!st.empty() && asteroid < 0 && st.back() > 0) {
            if (st.back() < -asteroid) {
                // 栈顶行星较小，被摧毁
                st.pop_back();
                continue;
            } else if (st.back() == -asteroid) {
                // 大小相等，两个都摧毁
                st.pop_back();
            }
            destroyed = true;
            break;
        }
        
        if (!destroyed) {
            st.push_back(asteroid);
        }
    }
    
    return st;
}

/**
 * 表现良好的最长时间段
 * 题目来源：LeetCode 1124. 表现良好的最长时间段
 * 链接：https://leetcode.cn/problems/longest-well-performing-interval/
 * 
 * 题目描述：
 * 给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
 * 我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
 * 所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格 大于「不劳累的天数」。
 * 请你返回「表现良好时间段」的最大长度。
 * 
 * 解题思路（单调栈）：
 * 将问题转化为前缀和问题，然后使用单调栈找到最大的区间使得前缀和大于0。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 前缀和数组和栈的空间
 * 
 * 是否最优解：是
 */
int longestWPI(vector<int>& hours) {
    int n = hours.size();
    vector<int> prefix(n + 1, 0);
    
    // 计算前缀和，劳累天记为1，不劳累天记为-1
    for (int i = 0; i < n; i++) {
        prefix[i + 1] = prefix[i] + (hours[i] > 8 ? 1 : -1);
    }
    
    // 使用单调栈存储递减的前缀和索引
    stack<int> st;
    for (int i = 0; i <= n; i++) {
        if (st.empty() || prefix[i] < prefix[st.top()]) {
            st.push(i);
        }
    }
    
    // 从右往左遍历，找到最大的区间
    int maxLen = 0;
    for (int i = n; i >= 0; i--) {
        while (!st.empty() && prefix[i] > prefix[st.top()]) {
            maxLen = max(maxLen, i - st.top());
            st.pop();
        }
    }
    
    return maxLen;
}

/**
 * 最短无序连续子数组
 * 题目来源：LeetCode 581. 最短无序连续子数组
 * 链接：https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。
 * 请你找出符合题意的 最短 子数组，并输出它的长度。
 * 
 * 解题思路（单调栈）：
 * 使用单调栈找到需要排序的子数组的左右边界。
 * 从左往右找到第一个破坏递增的位置作为右边界，从右往左找到第一个破坏递减的位置作为左边界。
 * 
 * 时间复杂度分析：
 * O(n) - 两次遍历
 * 
 * 空间复杂度分析：
 * O(1) - 只使用常数空间
 * 
 * 是否最优解：是（有更简单的双指针解法，但单调栈思路清晰）
 */
int findUnsortedSubarray(vector<int>& nums) {
    int n = nums.size();
    int left = n - 1, right = 0;
    stack<int> st;
    
    // 从左往右找到右边界
    for (int i = 0; i < n; i++) {
        while (!st.empty() && nums[st.top()] > nums[i]) {
            right = max(right, i);
            left = min(left, st.top());
            st.pop();
        }
        st.push(i);
    }
    
    // 清空栈
    while (!st.empty()) st.pop();
    
    // 从右往左找到左边界
    for (int i = n - 1; i >= 0; i--) {
        while (!st.empty() && nums[st.top()] < nums[i]) {
            left = min(left, i);
            right = max(right, st.top());
            st.pop();
        }
        st.push(i);
    }
    
    return right > left ? right - left + 1 : 0;
}

// 主函数用于测试
int main() {
    cout << "队列和栈相关算法实现测试" << endl;
    
    // 测试有效的括号
    string s = "()[]{}";
    cout << "有效的括号测试结果: " << (isValid(s) ? "true" : "false") << endl;
    
    // 测试最小栈
    MinStack minStack;
    minStack.push(-2);
    minStack.push(0);
    minStack.push(-3);
    cout << "最小栈最小值: " << minStack.getMin() << endl;
    minStack.pop();
    cout << "最小栈栈顶: " << minStack.top() << endl;
    cout << "最小栈最小值: " << minStack.getMin() << endl;
    
    // 测试简化路径
    string path = "/a/./b/../../c/";
    cout << "简化路径结果: " << simplifyPath(path) << endl;
    
    // 测试移掉K位数字
    string num = "1432219";
    int k = 3;
    cout << "移掉K位数字结果: " << removeKdigits(num, k) << endl;
    
    // 测试验证栈序列
    vector<int> pushed = {1, 2, 3, 4, 5};
    vector<int> popped = {4, 5, 3, 2, 1};
    cout << "验证栈序列结果: " << (validateStackSequences(pushed, popped) ? "true" : "false") << endl;
    
    // 测试132模式
    vector<int> nums132 = {3, 1, 4, 2};
    cout << "132模式测试结果: " << (find132pattern(nums132) ? "true" : "false") << endl;
    
    // 测试去除重复字母
    string duplicateStr = "bcabc";
    cout << "去除重复字母结果: " << removeDuplicateLetters(duplicateStr) << endl;
    
    // 测试滑动窗口最小值
    vector<int> numsMin = {1, 3, -1, -3, 5, 3, 6, 7};
    vector<int> minWindow = minSlidingWindow(numsMin, 3);
    cout << "滑动窗口最小值结果: ";
    for (int num : minWindow) cout << num << " ";
    cout << endl;
    
    // 测试行星碰撞
    vector<int> asteroids = {5, 10, -5};
    vector<int> collisionResult = asteroidCollision(asteroids);
    cout << "行星碰撞结果: ";
    for (int num : collisionResult) cout << num << " ";
    cout << endl;
    
    return 0;
}