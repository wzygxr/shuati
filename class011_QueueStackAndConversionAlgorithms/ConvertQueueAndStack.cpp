#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <string>
#include <algorithm>
#include <unordered_map>
#include <functional>
#include <climits>
#include <cmath>
#include <set>

using namespace std;

/**
 * 队列和栈转换算法实现类
 * 包含LeetCode等各大算法平台中队列和栈相关的经典题目
 * 时间复杂度：O(n) - O(n^2) 根据具体算法
 * 空间复杂度：O(n) - O(n) 根据具体算法
 */
class ConvertQueueAndStack {
public:
    /**
     * 柱状图中最大的矩形 - 单调栈解法
     * LeetCode 84: https://leetcode.com/problems/largest-rectangle-in-histogram/
     * 时间复杂度：O(n)，每个元素入栈出栈一次
     * 空间复杂度：O(n)，栈空间
     */
    int largestRectangleArea(vector<int>& heights) {
        int n = heights.size();
        if (n == 0) return 0;
        
        vector<int> left(n), right(n);
        stack<int> st;
        
        // 计算每个柱子左边第一个小于它的位置
        for (int i = 0; i < n; i++) {
            while (!st.empty() && heights[st.top()] >= heights[i]) {
                st.pop();
            }
            left[i] = st.empty() ? -1 : st.top();
            st.push(i);
        }
        
        // 清空栈
        while (!st.empty()) st.pop();
        
        // 计算每个柱子右边第一个小于它的位置
        for (int i = n - 1; i >= 0; i--) {
            while (!st.empty() && heights[st.top()] >= heights[i]) {
                st.pop();
            }
            right[i] = st.empty() ? n : st.top();
            st.push(i);
        }
        
        // 计算最大面积
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = right[i] - left[i] - 1;
            maxArea = max(maxArea, heights[i] * width);
        }
        
        return maxArea;
    }
    
    /**
     * 柱状图中最大的矩形 - 优化版单调栈
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int largestRectangleAreaOptimized(vector<int>& heights) {
        int n = heights.size();
        if (n == 0) return 0;
        
        vector<int> left(n), right(n, n);
        stack<int> st;
        
        for (int i = 0; i < n; i++) {
            while (!st.empty() && heights[st.top()] >= heights[i]) {
                right[st.top()] = i;
                st.pop();
            }
            left[i] = st.empty() ? -1 : st.top();
            st.push(i);
        }
        
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            maxArea = max(maxArea, heights[i] * (right[i] - left[i] - 1));
        }
        
        return maxArea;
    }
    
    /**
     * 字符串解码 - 栈解法
     * LeetCode 394: https://leetcode.com/problems/decode-string/
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    string decodeString(string s) {
        stack<pair<int, string>> st;
        int currentNum = 0;
        string currentStr = "";
        
        for (char c : s) {
            if (isdigit(c)) {
                currentNum = currentNum * 10 + (c - '0');
            } else if (c == '[') {
                st.push({currentNum, currentStr});
                currentNum = 0;
                currentStr = "";
            } else if (c == ']') {
                auto topPair = st.top();
                int num = topPair.first;
                string prevStr = topPair.second;
                st.pop();
                string temp = "";
                for (int i = 0; i < num; i++) {
                    temp += currentStr;
                }
                currentStr = prevStr + temp;
            } else {
                currentStr += c;
            }
        }
        
        return currentStr;
    }
    
    /**
     * 基本计算器 II - 栈解法
     * LeetCode 227: https://leetcode.com/problems/basic-calculator-ii/
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int calculate(string s) {
        stack<int> st;
        int num = 0;
        char sign = '+';
        
        for (int i = 0; i < s.length(); i++) {
            char c = s[i];
            
            if (isdigit(c)) {
                num = num * 10 + (c - '0');
            }
            
            if ((!isdigit(c) && c != ' ') || i == s.length() - 1) {
                if (sign == '+') {
                    st.push(num);
                } else if (sign == '-') {
                    st.push(-num);
                } else if (sign == '*') {
                    int top = st.top();
                    st.pop();
                    st.push(top * num);
                } else if (sign == '/') {
                    int top = st.top();
                    st.pop();
                    st.push(top / num);
                }
                
                sign = c;
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
    
    /**
     * 最小栈类 - 支持常数时间获取最小元素
     * LeetCode 155: https://leetcode.com/problems/min-stack/
     */
    class MinStack {
    private:
        stack<int> dataStack;
        stack<int> minStack;
        
    public:
        MinStack() {}
        
        void push(int val) {
            dataStack.push(val);
            if (minStack.empty() || val <= minStack.top()) {
                minStack.push(val);
            }
        }
        
        void pop() {
            if (dataStack.top() == minStack.top()) {
                minStack.pop();
            }
            dataStack.pop();
        }
        
        int top() {
            return dataStack.top();
        }
        
        int getMin() {
            return minStack.top();
        }
    };
    
    /**
     * 用栈实现队列类
     * LeetCode 232: https://leetcode.com/problems/implement-queue-using-stacks/
     */
    class CQueue {
    private:
        stack<int> inStack;
        stack<int> outStack;
        
        void transfer() {
            while (!inStack.empty()) {
                outStack.push(inStack.top());
                inStack.pop();
            }
        }
        
    public:
        CQueue() {}
        
        void appendTail(int value) {
            inStack.push(value);
        }
        
        int deleteHead() {
            if (outStack.empty()) {
                if (inStack.empty()) {
                    return -1;
                }
                transfer();
            }
            int value = outStack.top();
            outStack.pop();
            return value;
        }
    };
    
    /**
     * 滑动窗口中位数 - 双堆解法
     * LeetCode 480: https://leetcode.com/problems/sliding-window-median/
     * 时间复杂度：O(n log k)
     * 空间复杂度：O(k)
     */
    vector<double> medianSlidingWindow(vector<int>& nums, int k) {
        multiset<int> window(nums.begin(), nums.begin() + k);
        auto mid = next(window.begin(), k / 2);
        vector<double> medians;
        
        for (int i = k; ; i++) {
            // 推送当前中位数
            medians.push_back((double(*mid) + *prev(mid, 1 - k % 2)) / 2.0);
            
            // 如果所有元素处理完毕
            if (i == nums.size()) break;
            
            // 插入新元素
            window.insert(nums[i]);
            if (nums[i] < *mid) {
                mid--;
            }
            
            // 移除旧元素
            if (nums[i - k] <= *mid) {
                mid++;
            }
            window.erase(window.lower_bound(nums[i - k]));
        }
        
        return medians;
    }
};

/**
 * 主函数用于测试所有算法实现
 */
int main() {
    ConvertQueueAndStack solution;
    
    cout << "=== 测试柱状图中最大的矩形 ===" << endl;
    vector<int> heights = {2, 1, 5, 6, 2, 3};
    cout << "输入: [2,1,5,6,2,3]" << endl;
    cout << "输出: " << solution.largestRectangleAreaOptimized(heights) << endl;
    cout << "预期输出: 10" << endl;
    
    cout << "\n=== 测试字符串解码 ===" << endl;
    string encoded = "3[a]2[bc]";
    cout << "输入: \"3[a]2[bc]\"" << endl;
    cout << "输出: \"" << solution.decodeString(encoded) << "\"" << endl;
    cout << "预期输出: \"aaabcbc\"" << endl;
    
    cout << "\n=== 测试基本计算器 II ===" << endl;
    string expression = "3+2*2";
    cout << "输入: \"3+2*2\"" << endl;
    cout << "输出: " << solution.calculate(expression) << endl;
    cout << "预期输出: 7" << endl;
    
    cout << "\n=== 测试最小栈 ===" << endl;
    ConvertQueueAndStack::MinStack minStack;
    minStack.push(-2);
    minStack.push(0);
    minStack.push(-3);
    cout << "输入: push(-2), push(0), push(-3)" << endl;
    cout << "getMin(): " << minStack.getMin() << endl;
    cout << "预期: -3" << endl;
    minStack.pop();
    cout << "pop()" << endl;
    cout << "top(): " << minStack.top() << endl;
    cout << "预期: 0" << endl;
    cout << "getMin(): " << minStack.getMin() << endl;
    cout << "预期: -2" << endl;
    
    cout << "\n=== 测试用栈实现队列(CQueue) ===" << endl;
    ConvertQueueAndStack::CQueue queue;
    queue.appendTail(3);
    cout << "appendTail(3)" << endl;
    cout << "deleteHead(): " << queue.deleteHead() << endl;
    cout << "预期: 3" << endl;
    cout << "deleteHead(): " << queue.deleteHead() << endl;
    cout << "预期: -1" << endl;
    
    cout << "\n=== 测试滑动窗口中位数 ===" << endl;
    vector<int> nums = {1, 3, -1, -3, 5, 3, 6, 7};
    int k = 3;
    cout << "输入: [1,3,-1,-3,5,3,6,7], k=3" << endl;
    cout << "输出: [";
    vector<double> medians = solution.medianSlidingWindow(nums, k);
    for (int i = 0; i < medians.size(); i++) {
        cout << medians[i];
        if (i < medians.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "预期输出: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]" << endl;
    
    return 0;
}