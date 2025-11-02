// 队列和栈的C++实现
// 详细的注释和扩展题目实现

#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <algorithm>
#include <deque>
#include <sstream>
#include <climits>
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
    stack<int> st;

public:
    bool isEmpty() {
        return st.empty();
    }

    void push(int num) {
        st.push(num);
    }

    int pop() {
        int val = st.top();
        st.pop();
        return val;
    }

    int top() {
        return st.top();
    }

    int size() {
        return st.size();
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
    int size;

public:
    Stack2(int n) {
        stack.resize(n);
        size = 0;
    }

    bool isEmpty() {
        return size == 0;
    }

    void push(int num) {
        if (size < stack.size()) {
            stack[size++] = num;
        } else {
            throw "Stack is full";
        }
    }

    int pop() {
        if (isEmpty()) {
            throw "Stack is empty";
        }
        return stack[--size];
    }

    int top() {
        if (isEmpty()) {
            throw "Stack is empty";
        }
        return stack[size - 1];
    }

    int getSize() {
        return size;
    }
};

/**
 * 用队列实现栈
 * 题目来源：LeetCode 225. 用队列实现栈
 * 链接：https://leetcode.cn/problems/implement-stack-using-queues/
 * 
 * 解题思路：
 * 使用两个队列，一个主队列和一个辅助队列。每次push操作时，将新元素加入辅助队列，
 * 然后将主队列的所有元素依次移到辅助队列，最后交换两个队列的角色。
 * 
 * 时间复杂度分析：
 * push: O(n), pop: O(1), top: O(1), empty: O(1)
 * 
 * 空间复杂度分析：O(n)
 */
class MyStack {
private:
    queue<int> q1;
    queue<int> q2;

public:
    MyStack() {}
    
    void push(int x) {
        q2.push(x);
        while (!q1.empty()) {
            q2.push(q1.front());
            q1.pop();
        }
        swap(q1, q2);
    }
    
    int pop() {
        int val = q1.front();
        q1.pop();
        return val;
    }
    
    int top() {
        return q1.front();
    }
    
    bool empty() {
        return q1.empty();
    }
};

/**
 * 用栈实现队列
 * 题目来源：LeetCode 232. 用栈实现队列
 * 链接：https://leetcode.cn/problems/implement-queue-using-stacks/
 * 
 * 解题思路：
 * 使用两个栈，一个输入栈和一个输出栈。push操作时将元素压入输入栈，
 * pop操作时如果输出栈为空，就将输入栈的所有元素依次弹出并压入输出栈。
 * 
 * 时间复杂度分析：
 * push: O(1), pop: 均摊O(1), peek: 均摊O(1), empty: O(1)
 * 
 * 空间复杂度分析：O(n)
 */
class MyQueue {
private:
    stack<int> inStack;
    stack<int> outStack;

public:
    MyQueue() {}
    
    void push(int x) {
        inStack.push(x);
    }
    
    int pop() {
        if (outStack.empty()) {
            while (!inStack.empty()) {
                outStack.push(inStack.top());
                inStack.pop();
            }
        }
        int val = outStack.top();
        outStack.pop();
        return val;
    }
    
    int peek() {
        if (outStack.empty()) {
            while (!inStack.empty()) {
                outStack.push(inStack.top());
                inStack.pop();
            }
        }
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
 * 解题思路：
 * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。
 * 
 * 时间复杂度分析：所有操作都是O(1)
 * 空间复杂度分析：O(n)
 */
class MinStack {
private:
    stack<int> dataStack;
    stack<int> minStack;

public:
    MinStack() {
        minStack.push(INT_MAX);
    }
    
    void push(int val) {
        dataStack.push(val);
        minStack.push(min(val, minStack.top()));
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
 * 解题思路：
 * 使用栈来解决括号匹配问题。遍历字符串，遇到左括号时将其对应的右括号压入栈，
 * 遇到右括号时检查是否与栈顶元素匹配。
 * 
 * 时间复杂度分析：O(n)
 * 空间复杂度分析：O(n)
 */
bool isValid(string s) {
    stack<char> st;
    
    for (char c : s) {
        if (c == '(') {
            st.push(')');
        } else if (c == '[') {
            st.push(']');
        } else if (c == '{') {
            st.push('}');
        } else {
            if (st.empty() || st.top() != c) {
                return false;
            }
            st.pop();
        }
    }
    
    return st.empty();
}

/**
 * 接雨水
 * 题目来源：LeetCode 42. 接雨水
 * 链接：https://leetcode.cn/problems/trapping-rain-water/
 * 
 * 题目描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * 解题思路(单调栈)：
 * 使用单调栈存储柱子的索引，当遇到一个比栈顶高的柱子时，说明可以形成凹槽接雨水。
 * 计算当前柱子与栈顶柱子之间的雨水面积，宽度为当前索引与栈顶索引的差值减1，
 * 高度为当前柱子和栈顶柱子的较小值减去凹槽底部的高度。
 * 
 * 时间复杂度分析：
 * O(n) - 每个柱子最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的空间
 * 
 * 是否最优解：是，这是最优解
 */
int trap(vector<int>& height) {
    int n = height.size();
    if (n < 3) return 0;
    
    stack<int> st;
    int water = 0;
    
    for (int i = 0; i < n; i++) {
        while (!st.empty() && height[i] > height[st.top()]) {
            int bottom = st.top();
            st.pop();
            
            if (st.empty()) break;
            
            int left = st.top();
            int distance = i - left - 1;
            int h = min(height[i], height[left]) - height[bottom];
            water += distance * h;
        }
        st.push(i);
    }
    
    return water;
}

/**
 * 柱状图中最大的矩形
 * 题目来源：LeetCode 84. 柱状图中最大的矩形
 * 链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
 * 
 * 解题思路（单调栈）：
 * 使用单调栈来找到每个柱子左边和右边第一个比它小的柱子的位置。对于每个柱子，
 * 其能形成的最大矩形的宽度是右边界减去左边界减一，高度是柱子本身的高度。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的最大空间为n
 */
int largestRectangleArea(vector<int>& heights) {
    int n = heights.size();
    if (n == 0) return 0;
    
    stack<int> st;
    int max_area = 0;
    
    for (int i = 0; i <= n; i++) {
        int h = (i == n) ? 0 : heights[i];
        
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

/**
 * 每日温度
 * 题目来源：LeetCode 739. 每日温度
 * 链接：https://leetcode.cn/problems/daily-temperatures/
 * 
 * 解题思路（单调栈）：
 * 使用单调栈来存储温度的索引。遍历数组，当遇到一个温度比栈顶温度高时，说明找到了栈顶温度的下一个更高温度，
 * 计算天数差并更新结果数组，然后弹出栈顶元素，继续比较新的栈顶元素，直到栈为空或栈顶温度不小于当前温度。
 * 
 * 时间复杂度分析：
 * O(n) - 每个元素最多入栈出栈一次
 * 
 * 空间复杂度分析：
 * O(n) - 栈的最大空间为n
 */
vector<int> dailyTemperatures(vector<int>& temperatures) {
    int n = temperatures.size();
    vector<int> answer(n, 0);
    stack<int> st;
    
    for (int i = 0; i < n; i++) {
        while (!st.empty() && temperatures[i] > temperatures[st.top()]) {
            int prev_index = st.top();
            st.pop();
            answer[prev_index] = i - prev_index;
        }
        st.push(i);
    }
    
    return answer;
}

/**
 * 滑动窗口最大值
 * 题目来源：LeetCode 239. 滑动窗口最大值
 * 链接：https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 解题思路（单调队列）：
 * 使用双端队列来维护窗口中的最大值。队列中的元素是数组的索引，对应的数组值是单调递减的。
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
    deque<int> dq;
    
    for (int i = 0; i < n; i++) {
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        while (!dq.empty() && nums[dq.back()] < nums[i]) {
            dq.pop_back();
        }
        
        dq.push_back(i);
        
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
        if (isFull()) return false;
        if (isEmpty()) {
            l = r = 0;
            deque[0] = value;
        } else {
            l = (l == 0) ? limit - 1 : l - 1;
            deque[l] = value;
        }
        size++;
        return true;
    }

    bool insertLast(int value) {
        if (isFull()) return false;
        if (isEmpty()) {
            l = r = 0;
            deque[0] = value;
        } else {
            r = (r + 1) % limit;
            deque[r] = value;
        }
        size++;
        return true;
    }

    bool deleteFront() {
        if (isEmpty()) return false;
        if (size == 1) {
            l = r = 0;
        } else {
            l = (l + 1) % limit;
        }
        size--;
        return true;
    }

    bool deleteLast() {
        if (isEmpty()) return false;
        if (size == 1) {
            l = r = 0;
        } else {
            r = (r == 0) ? limit - 1 : r - 1;
        }
        size--;
        return true;
    }

    int getFront() {
        return isEmpty() ? -1 : deque[l];
    }

    int getRear() {
        return isEmpty() ? -1 : deque[r];
    }

    bool isEmpty() {
        return size == 0;
    }

    bool isFull() {
        return size == limit;
    }
};

// 扩展题目实现...

/**
 * 逆波兰表达式求值
 * 题目来源：LeetCode 150. 逆波兰表达式求值
 */
int evalRPN(vector<string>& tokens) {
    stack<int> st;
    
    for (string token : tokens) {
        if (token == "+" || token == "-" || token == "*" || token == "/") {
            int b = st.top(); st.pop();
            int a = st.top(); st.pop();
            
            if (token == "+") st.push(a + b);
            else if (token == "-") st.push(a - b);
            else if (token == "*") st.push(a * b);
            else if (token == "/") st.push(a / b);
        } else {
            st.push(stoi(token));
        }
    }
    
    return st.top();
}

/**
 * 字符串解码
 * 题目来源：LeetCode 394. 字符串解码
 */
string decodeString(string s) {
    stack<int> numStack;
    stack<string> strStack;
    string current;
    int num = 0;
    
    for (char c : s) {
        if (isdigit(c)) {
            num = num * 10 + (c - '0');
        } else if (c == '[') {
            numStack.push(num);
            strStack.push(current);
            num = 0;
            current = "";
        } else if (c == ']') {
            int repeat = numStack.top(); numStack.pop();
            string prev = strStack.top(); strStack.pop();
            
            string temp;
            for (int i = 0; i < repeat; i++) {
                temp += current;
            }
            current = prev + temp;
        } else {
            current += c;
        }
    }
    
    return current;
}

/**
 * 删除相邻重复项
 * 题目来源：LeetCode 1047. 删除字符串中的所有相邻重复项
 */
string removeDuplicates(string s) {
    string result;
    
    for (char c : s) {
        if (!result.empty() && result.back() == c) {
            result.pop_back();
        } else {
            result.push_back(c);
        }
    }
    
    return result;
}

/**
 * 基本计算器 II
 * 题目来源：LeetCode 227. 基本计算器 II
 */
int calculate(string s) {
    stack<int> st;
    int num = 0;
    char sign = '+';
    
    for (int i = 0; i < s.length(); i++) {
        if (isdigit(s[i])) {
            num = num * 10 + (s[i] - '0');
        }
        
        if ((!isdigit(s[i]) && s[i] != ' ') || i == s.length() - 1) {
            if (sign == '+') {
                st.push(num);
            } else if (sign == '-') {
                st.push(-num);
            } else if (sign == '*') {
                int top = st.top(); st.pop();
                st.push(top * num);
            } else if (sign == '/') {
                int top = st.top(); st.pop();
                st.push(top / num);
            }
            
            sign = s[i];
            num = 0;
        }
    }
    
    int result = 0;
    while (!st.empty()) {
        result += st.top();
        st.pop();
    }
    
    return result;
}

// 更多扩展题目实现...

/**
 * 132模式
 * 题目来源：LeetCode 456. 132模式
 */
bool find132pattern(vector<int>& nums) {
    int n = nums.size();
    if (n < 3) return false;
    
    stack<int> st;
    int second = INT_MIN;
    
    for (int i = n - 1; i >= 0; i--) {
        if (nums[i] < second) return true;
        
        while (!st.empty() && nums[i] > st.top()) {
            second = st.top();
            st.pop();
        }
        
        st.push(nums[i]);
    }
    
    return false;
}

/**
 * 去除重复字母
 * 题目来源：LeetCode 316. 去除重复字母
 */
string removeDuplicateLetters(string s) {
    unordered_map<char, int> lastIndex;
    unordered_set<char> inStack;
    stack<char> st;
    
    for (int i = 0; i < s.length(); i++) {
        lastIndex[s[i]] = i;
    }
    
    for (int i = 0; i < s.length(); i++) {
        if (inStack.count(s[i])) continue;
        
        while (!st.empty() && s[i] < st.top() && lastIndex[st.top()] > i) {
            inStack.erase(st.top());
            st.pop();
        }
        
        st.push(s[i]);
        inStack.insert(s[i]);
    }
    
    string result;
    while (!st.empty()) {
        result = st.top() + result;
        st.pop();
    }
    
    return result;
}

/**
 * 滑动窗口最小值
 */
vector<int> minSlidingWindow(vector<int>& nums, int k) {
    int n = nums.size();
    vector<int> result;
    deque<int> dq;
    
    for (int i = 0; i < n; i++) {
        while (!dq.empty() && dq.front() < i - k + 1) {
            dq.pop_front();
        }
        
        while (!dq.empty() && nums[dq.back()] > nums[i]) {
            dq.pop_back();
        }
        
        dq.push_back(i);
        
        if (i >= k - 1) {
            result.push_back(nums[dq.front()]);
        }
    }
    
    return result;
}

/**
 * 行星碰撞
 * 题目来源：LeetCode 735. 行星碰撞
 */
vector<int> asteroidCollision(vector<int>& asteroids) {
    stack<int> st;
    
    for (int asteroid : asteroids) {
        bool destroyed = false;
        
        while (!st.empty() && asteroid < 0 && st.top() > 0) {
            if (st.top() < -asteroid) {
                st.pop();
                continue;
            } else if (st.top() == -asteroid) {
                st.pop();
            }
            destroyed = true;
            break;
        }
        
        if (!destroyed) {
            st.push(asteroid);
        }
    }
    
    vector<int> result;
    while (!st.empty()) {
        result.insert(result.begin(), st.top());
        st.pop();
    }
    
    return result;
}

// 股票价格跨度类
class StockSpanner {
private:
    stack<pair<int, int>> st; // 价格和跨度

public:
    StockSpanner() {}
    
    int next(int price) {
        int span = 1;
        
        while (!st.empty() && st.top().first <= price) {
            span += st.top().second;
            st.pop();
        }
        
        st.push({price, span});
        return span;
    }
};

/**
 * 最短无序连续子数组
 * 题目来源：LeetCode 581. 最短无序连续子数组
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
    cout << "简化路径结果: " << path << endl;
    
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
    
    // 测试股票价格跨度
    StockSpanner stockSpanner;
    vector<int> prices = {100, 80, 60, 70, 60, 75, 85};
    cout << "股票价格跨度结果: ";
    for (int price : prices) {
        cout << stockSpanner.next(price) << " ";
    }
    cout << endl;
    
    // 测试最短无序连续子数组
    vector<int> unsortedNums = {2, 6, 4, 8, 10, 9, 15};
    cout << "最短无序连续子数组长度: " << findUnsortedSubarray(unsortedNums) << endl;
    
    return 0;
}