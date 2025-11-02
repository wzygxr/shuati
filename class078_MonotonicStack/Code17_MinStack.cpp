#include <iostream>
#include <stack>
#include <stdexcept>
#include <chrono>
#include <random>
#include <vector>
#include <mutex>

using namespace std;

/**
 * 最小栈 - C++实现
 * 
 * 题目描述：
 * 设计一个支持 push，pop，top 操作，并能在常数时间内检索到最小元素的栈。
 * 
 * 测试链接：https://leetcode.cn/problems/min-stack/
 * 题目来源：LeetCode
 * 难度：简单
 * 
 * 核心算法：双栈法（一个存储数据，一个存储最小值）
 * 
 * 解题思路：
 * 使用两个栈来实现最小栈：
 * 1. dataStack：普通的栈，用于存储所有元素
 * 2. minStack：辅助栈，栈顶始终存储当前栈中的最小值
 * 
 * 具体操作：
 * - push(x): 将x压入dataStack，如果minStack为空或x<=minStack栈顶，则也压入minStack
 * - pop(): 弹出dataStack栈顶，如果弹出的值等于minStack栈顶，则也弹出minStack栈顶
 * - top(): 返回dataStack栈顶元素
 * - getMin(): 返回minStack栈顶元素（当前最小值）
 * 
 * 时间复杂度分析：
 * - 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * - O(n) - 需要两个栈来存储数据
 * 
 * C++语言特性：
 * - 使用std::stack容器
 * - 使用异常处理机制
 * - 使用chrono库进行性能测试
 * - 使用模板实现通用类型支持
 */
template<typename T>
class MinStack {
private:
    stack<T> dataStack;    // 数据栈
    stack<T> minStack;     // 最小值栈
    
public:
    MinStack() = default;
    
    /**
     * 压入元素
     * @param x 要压入的元素
     */
    void push(const T& x) {
        dataStack.push(x);
        // 如果最小值栈为空，或者x小于等于当前最小值，则压入最小值栈
        if (minStack.empty() || x <= minStack.top()) {
            minStack.push(x);
        }
    }
    
    /**
     * 弹出栈顶元素
     * @throws std::runtime_error 如果栈为空
     */
    void pop() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        T popped = dataStack.top();
        dataStack.pop();
        // 如果弹出的是当前最小值，则也从最小值栈弹出
        if (popped == minStack.top()) {
            minStack.pop();
        }
    }
    
    /**
     * 获取栈顶元素
     * @return 栈顶元素
     * @throws std::runtime_error 如果栈为空
     */
    T top() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return dataStack.top();
    }
    
    /**
     * 获取栈中的最小值
     * @return 最小值
     * @throws std::runtime_error 如果栈为空
     */
    T getMin() {
        if (minStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return minStack.top();
    }
    
    /**
     * 检查栈是否为空
     * @return 如果栈为空返回true，否则返回false
     */
    bool empty() const {
        return dataStack.empty();
    }
    
    /**
     * 获取栈的大小
     * @return 栈中元素的数量
     */
    size_t size() const {
        return dataStack.size();
    }
};

/**
 * 线程安全的最小栈实现
 * 使用互斥锁保证线程安全（简化版，实际生产环境需要更完善的锁机制）
 */
template<typename T>
class ThreadSafeMinStack {
private:
    stack<T> dataStack;
    stack<T> minStack;
    mutable mutex mtx;  // 互斥锁
    
public:
    ThreadSafeMinStack() = default;
    
    void push(const T& x) {
        lock_guard<mutex> lock(mtx);
        dataStack.push(x);
        if (minStack.empty() || x <= minStack.top()) {
            minStack.push(x);
        }
    }
    
    void pop() {
        lock_guard<mutex> lock(mtx);
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        T popped = dataStack.top();
        dataStack.pop();
        if (popped == minStack.top()) {
            minStack.pop();
        }
    }
    
    T top() {
        lock_guard<mutex> lock(mtx);
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return dataStack.top();
    }
    
    T getMin() {
        lock_guard<mutex> lock(mtx);
        if (minStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return minStack.top();
    }
    
    bool empty() const {
        lock_guard<mutex> lock(mtx);
        return dataStack.empty();
    }
    
    size_t size() const {
        lock_guard<mutex> lock(mtx);
        return dataStack.size();
    }
};

/**
 * 单元测试函数
 */
void testMinStack() {
    cout << "=== 最小栈单元测试 ===" << endl;
    
    // 测试用例1：基本操作
    MinStack<int> stack1;
    stack1.push(-2);
    stack1.push(0);
    stack1.push(-3);
    cout << "测试用例1 - 压入[-2, 0, -3]" << endl;
    cout << "当前最小值: " << stack1.getMin() << endl; // 期望: -3
    stack1.pop();
    cout << "弹出后栈顶: " << stack1.top() << endl;     // 期望: 0
    cout << "当前最小值: " << stack1.getMin() << endl; // 期望: -2
    
    // 测试用例2：重复最小值
    MinStack<int> stack2;
    stack2.push(5);
    stack2.push(3);
    stack2.push(3);
    stack2.push(7);
    cout << "\n测试用例2 - 压入[5, 3, 3, 7]" << endl;
    cout << "当前最小值: " << stack2.getMin() << endl; // 期望: 3
    stack2.pop(); // 弹出7
    cout << "弹出7后最小值: " << stack2.getMin() << endl; // 期望: 3
    stack2.pop(); // 弹出3
    cout << "弹出3后最小值: " << stack2.getMin() << endl; // 期望: 3
    stack2.pop(); // 弹出3
    cout << "弹出3后最小值: " << stack2.getMin() << endl; // 期望: 5
    
    // 测试用例3：边界情况 - 单个元素
    MinStack<int> stack3;
    stack3.push(10);
    cout << "\n测试用例3 - 单个元素10" << endl;
    cout << "栈顶: " << stack3.top() << endl;     // 期望: 10
    cout << "最小值: " << stack3.getMin() << endl; // 期望: 10
    
    // 测试用例4：边界情况 - 空栈异常处理
    MinStack<int> stack4;
    try {
        stack4.pop();
    } catch (const runtime_error& e) {
        cout << "\n测试用例4 - 空栈pop操作抛出异常: " << e.what() << endl;
    }
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "\n=== 性能测试 ===" << endl;
    
    MinStack<int> stack;
    int n = 100000;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dist(0, 1000);
    
    auto startTime = chrono::high_resolution_clock::now();
    
    // 压入n个随机数
    for (int i = 0; i < n; i++) {
        stack.push(dist(gen));
    }
    
    // 交替进行getMin和pop操作
    for (int i = 0; i < n; i++) {
        stack.getMin();
        stack.pop();
    }
    
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "数据规模: " << n << "个元素" << endl;
    cout << "执行时间: " << duration.count() << "ms" << endl;
}

/**
 * 性能对比测试：普通栈 vs 最小栈
 */
void performanceComparison() {
    cout << "\n=== 性能对比测试 ===" << endl;
    
    int n = 100000;
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dist(0, 1000);
    
    // 测试普通栈
    stack<int> normalStack;
    auto startTime1 = chrono::high_resolution_clock::now();
    
    for (int i = 0; i < n; i++) {
        normalStack.push(dist(gen));
    }
    
    for (int i = 0; i < n; i++) {
        normalStack.pop();
    }
    
    auto endTime1 = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
    
    // 测试最小栈
    MinStack<int> minStack;
    auto startTime2 = chrono::high_resolution_clock::now();
    
    for (int i = 0; i < n; i++) {
        minStack.push(dist(gen));
    }
    
    for (int i = 0; i < n; i++) {
        minStack.getMin();
        minStack.pop();
    }
    
    auto endTime2 = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
    
    cout << "普通栈操作时间: " << duration1.count() << "ms" << endl;
    cout << "最小栈操作时间: " << duration2.count() << "ms" << endl;
    cout << "性能开销比例: " << static_cast<double>(duration2.count()) / duration1.count() << endl;
}

/**
 * 主函数
 */
int main() {
    // 运行单元测试
    testMinStack();
    
    // 运行性能测试
    performanceTest();
    
    // 运行性能对比测试
    performanceComparison();
    
    cout << "\n=== 最小栈算法验证完成 ===" << endl;
    return 0;
}