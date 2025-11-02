/**
 * 最小栈与最大栈专题 - 辅助栈思想的综合应用
 * 
 * 核心思想:
 * 使用辅助栈维护每个位置对应的最值(最小值或最大值)
 * 这是一种空间换时间的经典策略,确保获取最值的时间复杂度为O(1)
 * 
 * 适用场景:
 * 1. 需要在O(1)时间内获取栈中最小值/最大值
 * 2. 需要在栈操作的同时维护某种单调性
 * 3. 需要快速查询历史最值信息
 * 
 * 题型识别关键词:
 * - "O(1)时间获取最小值/最大值"
 * - "设计支持xxx操作的栈"
 * - "维护栈中的最值"
 * 
 * 核心技巧总结:
 * 1. 双栈法:数据栈 + 辅助栈(最值栈)
 * 2. 辅助栈同步更新:每次push/pop时同时更新辅助栈
 * 3. 空间优化:辅助栈可以只存储真正的最值(需要额外判断逻辑)
 * 
 * 时间复杂度:
 * - push: O(1)
 * - pop: O(1)
 * - top: O(1)
 * - getMin/getMax: O(1)
 * 
 * 空间复杂度:
 * O(n) - 需要额外的辅助栈存储最值信息
 * 
 * 工程化考量:
 * 1. 异常处理:空栈时调用pop/top/getMin应抛出异常
 * 2. 线程安全:多线程环境下需要加锁
 * 3. 泛型支持:可以扩展为支持泛型的栈
 * 4. 容量限制:可以添加容量限制防止栈溢出
 * 
 * 与其他算法的联系:
 * 1. 单调栈:辅助栈思想的扩展应用
 * 2. 滑动窗口最大值:使用单调队列实现,思想类似
 * 3. 动态规划:维护历史状态信息的思想相通
 * 
 * 测试链接 : https://leetcode.cn/problems/min-stack/
 */

#include <stack>
#include <climits>
#include <deque>
#include <algorithm>
#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <string>
#include <thread>
#include <mutex>
#include <memory>
#include <cassert>
#include <chrono>
#include <functional>

using namespace std;

class MinStack1 {
public:
    stack<int> data;
    stack<int> min_stk;

    MinStack1() {}

    void push(int val) {
        data.push(val);
        if (min_stk.empty() || val <= min_stk.top()) {
            min_stk.push(val);
        } else {
            min_stk.push(min_stk.top());
        }
    }

    void pop() {
        data.pop();
        min_stk.pop();
    }

    int top() {
        return data.top();
    }

    int getMin() {
        return min_stk.top();
    }
};

class MinStack2 {
public:
    // leetcode的数据在测试时，同时在栈里的数据不超过这个值
    // 这是几次提交实验出来的，哈哈
    // 如果leetcode补测试数据了，超过这个量导致出错，就调大
    static const int MAXN = 8001;

    int data[MAXN];
    int min_vals[MAXN];
    int size;

    MinStack2() {
        size = 0;
    }

    void push(int val) {
        data[size] = val;
        if (size == 0 || val <= min_vals[size - 1]) {
            min_vals[size] = val;
        } else {
            min_vals[size] = min_vals[size - 1];
        }
        size++;
    }

    void pop() {
        size--;
    }

    int top() {
        return data[size - 1];
    }

    int getMin() {
        return min_vals[size - 1];
    }
};

/**
 * 最大栈
 * 题目来源：LeetCode 716. 最大栈
 * 链接：https://leetcode.cn/problems/max-stack/
 * 
 * 题目描述：
 * 设计一个最大栈数据结构，既支持栈操作，又支持查找栈中最大元素。
 * 实现 MaxStack 类：
 * MaxStack() 初始化栈对象
 * void push(int x) 将元素 x 压入栈中。
 * int pop() 移除栈顶元素并返回这个元素。
 * int top() 返回栈顶元素，无需移除。
 * int peekMax() 检索并返回栈中最大元素，无需移除。
 * int popMax() 检索并返回栈中最大元素，并将其移除。如果有多个最大元素，只要移除 最靠近栈顶 的那个。
 * 
 * 解题思路：
 * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最大值。
 * 每次push操作时，数据栈正常压入元素，辅助栈压入当前元素与之前最大值中的较大者。
 * 这样辅助栈的栈顶始终是当前栈中的最大值。
 * popMax操作时，需要将最大值上面的所有元素暂存到临时栈中，取出最大值后再将临时栈中的元素放回。
 * 
 * 时间复杂度分析：
 * - push操作：O(1) - 直接压入两个栈
 * - pop操作：O(1) - 直接从两个栈弹出
 * - top操作：O(1) - 直接返回数据栈栈顶
 * - peekMax操作：O(1) - 直接返回辅助栈栈顶
 * - popMax操作：O(n) - 最坏情况下需要将所有元素移动到临时栈再移回
 * 
 * 空间复杂度分析：
 * O(n) - 需要两个栈和一个临时栈来存储元素
 */
class MaxStack {
private:
    stack<int> data_stack;  // 数据栈
    stack<int> max_stack;   // 辅助栈，存储每个位置对应的最大值

public:
    MaxStack() {}

    // 将元素 x 压入栈中
    void push(int x) {
        data_stack.push(x);
        // 如果辅助栈为空，或者当前元素大于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if (max_stack.empty() || x >= max_stack.top()) {
            max_stack.push(x);
        } else {
            max_stack.push(max_stack.top());
        }
    }

    // 移除栈顶元素并返回这个元素
    int pop() {
        int val = data_stack.top();
        data_stack.pop();
        max_stack.pop();
        return val;
    }

    // 返回栈顶元素
    int top() {
        return data_stack.top();
    }

    // 检索并返回栈中最大元素，无需移除
    int peekMax() {
        return max_stack.top();
    }

    // 检索并返回栈中最大元素，并将其移除
    int popMax() {
        int max_val = peekMax();
        stack<int> temp_stack;
        // 将最大值上面的元素暂存到临时栈中
        while (top() != max_val) {
            temp_stack.push(pop());
        }
        // 弹出最大值
        pop();
        // 将临时栈中的元素放回
        while (!temp_stack.empty()) {
            push(temp_stack.top());
            temp_stack.pop();
        }
        return max_val;
    }
};

/**
 * 带最小值操作的栈
 * 题目来源：LintCode 12. 带最小值操作的栈
 * 链接：https://www.lintcode.com/problem/12/
 * 
 * 题目描述：
 * 实现一个栈, 支持以下操作:
 * push(val) 将 val 压入栈
 * pop() 将栈顶元素弹出, 并返回这个弹出的元素
 * min() 返回栈中元素的最小值
 * 要求 O(1) 开销，保证栈中没有数字时不会调用 min()
 * 
 * 解题思路：
 * 与最小栈问题相同，使用两个栈实现，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。
 * 
 * 时间复杂度分析：
 * 所有操作都是O(1)时间复杂度
 * 
 * 空间复杂度分析：
 * O(n) - 需要两个栈来存储元素
 */
class MinStack {
private:
    stack<int> stk;      // 数据栈
    stack<int> min_stk;   // 辅助栈，存储每个位置对应的最小值

public:
    MinStack() {}

    // 将 val 压入栈
    void push(int number) {
        stk.push(number);
        // 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
        if (min_stk.empty() || number <= min_stk.top()) {
            min_stk.push(number);
        } else {
            min_stk.push(min_stk.top());
        }
    }

    // 将栈顶元素弹出, 并返回这个弹出的元素
    int pop() {
        int val = stk.top();
        stk.pop();
        min_stk.pop();
        return val;
    }

    // 返回栈中元素的最小值
    int min() {
        return min_stk.top();
    }
};

/**
 * 题目3:包含min函数的栈(剑指Offer 30)
 * 题目来源:剑指Offer 30 / LeetCode 155
 * 链接:https://leetcode.cn/problems/bao-han-minhan-shu-de-zhan-lcof/
 * 
 * 题目描述:
 * 定义栈的数据结构,请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中,
 * 调用 min、push 及 pop 的时间复杂度都是 O(1)。
 * 
 * 示例:
 * MinStack minStack = new MinStack();
 * minStack.push(-2);
 * minStack.push(0);
 * minStack.push(-3);
 * minStack.min();   --> 返回 -3.
 * minStack.pop();
 * minStack.top();      --> 返回 0.
 * minStack.min();   --> 返回 -2.
 * 
 * 解题思路:
 * 经典的辅助栈问题,与上述MinStack实现完全相同。
 * 关键点:辅助栈需要与数据栈同步push和pop,保证辅助栈栈顶始终是当前栈的最小值。
 * 
 * 边界场景:
 * 1. 空栈时调用min/top/pop - 需要异常处理或约定不会发生
 * 2. 所有元素相同 - 辅助栈每个位置都是该值
 * 3. 严格递增序列 - 辅助栈所有位置都是首个元素
 * 4. 严格递减序列 - 辅助栈与数据栈完全相同
 * 5. 包含整数溢出边界值(INT_MIN, INT_MAX)
 * 
 * 时间复杂度:O(1) - 所有操作
 * 空间复杂度:O(n) - 需要辅助栈
 * 
 * 是否最优解:是。无法在保证O(1)时间复杂度的前提下进一步优化空间复杂度。
 * 虽然可以优化辅助栈只存储真正的最小值,但最坏情况下(严格递减序列)空间复杂度仍为O(n)。
 */
class MinStackOffer {
private:
    deque<int> data_stack;  // 数据栈
    deque<int> min_stack;   // 辅助栈,存储最小值

public:
    MinStackOffer() {}
    
    void push(int x) {
        data_stack.push_back(x);
        // 如果辅助栈为空,或当前元素小于等于辅助栈栈顶,则压入当前元素
        // 否则压入辅助栈栈顶元素(复制最小值)
        if (min_stack.empty() || x <= min_stack.back()) {
            min_stack.push_back(x);
        } else {
            min_stack.push_back(min_stack.back());
        }
    }
    
    void pop() {
        // 两个栈同步弹出
        data_stack.pop_back();
        min_stack.pop_back();
    }
    
    int top() {
        return data_stack.back();
    }
    
    int min() {
        return min_stack.back();
    }
};

/**
 * 题目4:栈排序(LeetCode 面试题 03.05)
 * 题目来源:LeetCode 面试题 03.05. 栈排序
 * 链接:https://leetcode.cn/problems/sort-of-stacks-lcci/
 * 
 * 题目描述:
 * 栈排序。编写程序,对栈进行排序使最小元素位于栈顶。最多只能使用一个其他的临时栈存放数据,
 * 但不得将元素复制到别的数据结构(如数组)中。该栈支持如下操作:push、pop、peek 和isEmpty。
 * 当栈为空时,peek 返回 -1。
 * 
 * 示例:
 * ["SortedStack", "push", "push", "peek", "pop", "peek"]
 * [[], [1], [2], [], [], []]
 * 输出:
 * [null,null,null,1,null,2]
 * 
 * 解题思路:
 * 使用两个栈实现,主栈保持有序(栈顶最小),辅助栈用于临时存储。
 * push时,将主栈中大于新元素的元素临时移到辅助栈,插入新元素后再移回。
 * 
 * 详细步骤:
 * 1. push(x)时:
 *    - 将主栈中所有大于x的元素弹出到辅助栈
 *    - 将x压入主栈
 *    - 将辅助栈中的元素全部弹回主栈
 * 2. pop/peek/isEmpty直接操作主栈即可
 * 
 * 时间复杂度:
 * - push: O(n) - 最坏情况需要移动所有元素
 * - pop: O(1)
 * - peek: O(1)
 * - isEmpty: O(1)
 * 
 * 空间复杂度:O(n) - 需要辅助栈
 * 
 * 是否最优解:是。在只能使用一个辅助栈的限制下,这是最优解。
 */
class SortedStack {
private:
    stack<int> main_stack;  // 主栈,保持有序(栈顶最小)
    stack<int> temp_stack;  // 辅助栈,临时存储

public:
    SortedStack() {}
    
    void push(int val) {
        // 将主栈中所有大于val的元素临时移到辅助栈
        while (!main_stack.empty() && main_stack.top() < val) {
            temp_stack.push(main_stack.top());
            main_stack.pop();
        }
        // 将val压入主栈
        main_stack.push(val);
        // 将辅助栈中的元素全部弹回主栈
        while (!temp_stack.empty()) {
            main_stack.push(temp_stack.top());
            temp_stack.pop();
        }
    }
    
    void pop() {
        if (!main_stack.empty()) {
            main_stack.pop();
        }
    }
    
    int peek() {
        if (main_stack.empty()) {
            return -1;
        }
        return main_stack.top();
    }
    
    bool isEmpty() {
        return main_stack.empty();
    }
};

/**
 * 题目5:用栈实现队列(LeetCode 232)
 * 题目来源:LeetCode 232. 用栈实现队列
 * 链接:https://leetcode.cn/problems/implement-queue-using-stacks/
 * 
 * 题目描述:
 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作(push、pop、peek、empty)。
 * 
 * 解题思路:
 * 使用两个栈:输入栈和输出栈。
 * - push操作:直接压入输入栈
 * - pop/peek操作:如果输出栈为空,将输入栈所有元素转移到输出栈,然后操作输出栈
 * 
 * 核心思想:
 * 通过两次反转实现FIFO。第一次反转在输入栈,第二次反转在转移到输出栈时。
 * 
 * 时间复杂度分析(摊还分析):
 * - push: O(1)
 * - pop: 摊还O(1) - 单次可能O(n),但每个元素最多被转移一次
 * - peek: 摊还O(1)
 * - empty: O(1)
 * 
 * 空间复杂度:O(n)
 * 
 * 是否最优解:是。这是用栈实现队列的标准解法。
 */
class MyQueue {
private:
    stack<int> in_stack;   // 输入栈
    stack<int> out_stack;  // 输出栈

public:
    MyQueue() {}
    
    // 将元素压入队列尾部
    void push(int x) {
        in_stack.push(x);
    }
    
    // 从队列头部移除并返回元素
    int pop() {
        // 如果输出栈为空,将输入栈所有元素转移到输出栈
        if (out_stack.empty()) {
            while (!in_stack.empty()) {
                out_stack.push(in_stack.top());
                in_stack.pop();
            }
        }
        int val = out_stack.top();
        out_stack.pop();
        return val;
    }
    
    // 获取队列头部元素
    int peek() {
        if (out_stack.empty()) {
            while (!in_stack.empty()) {
                out_stack.push(in_stack.top());
                in_stack.pop();
            }
        }
        return out_stack.top();
    }
    
    // 判断队列是否为空
    bool empty() {
        return in_stack.empty() && out_stack.empty();
    }
};

/**
 * 题目6:最小栈(空间优化版)
 * 题目来源:优化实现
 * 
 * 题目描述:
 * 实现最小栈,但优化辅助栈的空间使用。辅助栈只存储真正的最小值,而不是每个位置都存储。
 * 
 * 解题思路:
 * 辅助栈只在遇到新的最小值时才压入。pop时需要判断弹出的是否是最小值,如果是则同步弹出辅助栈。
 * 
 * 优化效果:
 * - 最好情况(严格递增):辅助栈只有1个元素,空间O(1)
 * - 最坏情况(严格递减):辅助栈与数据栈大小相同,空间O(n)
 * - 平均情况:辅助栈大小远小于数据栈
 * 
 * 时间复杂度:O(1) - 所有操作
 * 空间复杂度:O(k),k为不同最小值的个数,k <= n
 * 
 * 注意事项:
 * 需要小心处理相等的情况,特别是当栈顶元素等于最小值时的pop操作。
 */
class MinStackOptimized {
private:
    stack<int> data_stack;  // 数据栈
    stack<int> min_stack;   // 辅助栈,只存储最小值

public:
    MinStackOptimized() {}
    
    void push(int val) {
        data_stack.push(val);
        // 只在遇到新的最小值(小于等于当前最小值)时才压入辅助栈
        // 注意:这里必须是 <=,不能是 <,否则会漏掉重复的最小值
        if (min_stack.empty() || val <= min_stack.top()) {
            min_stack.push(val);
        }
    }
    
    void pop() {
        // 如果弹出的元素是当前最小值,辅助栈也要弹出
        if (data_stack.top() == min_stack.top()) {
            min_stack.pop();
        }
        data_stack.pop();
    }
    
    int top() {
        return data_stack.top();
    }
    
    int getMin() {
        return min_stack.top();
    }
};

/**
 * 题目7:设计一个支持增量操作的栈(LeetCode 1381)
 * 题目来源:LeetCode 1381. 设计一个支持增量操作的栈
 * 链接:https://leetcode.cn/problems/design-a-stack-with-increment-operation/
 * 
 * 题目描述:
 * 请你设计一个支持下述操作的栈:
 * - CustomStack(int maxSize):初始化对象,maxSize 为栈的最大容量
 * - void push(int x):如果栈未满,则将 x 添加到栈顶
 * - int pop():弹出栈顶元素,并返回栈顶的值,如果栈为空则返回 -1
 * - void inc(int k, int val):将栈底的 k 个元素的值都增加 val。如果栈中元素总数小于 k,则将所有元素都增加 val
 * 
 * 解题思路:
 * 使用懒惰更新(lazy propagation)的思想。
 * 维护一个增量数组inc[],inc[i]表示从栈底到第i个位置需要累加的增量。
 * - increment操作:只更新inc[k-1]的值,不实际修改栈中元素
 * - pop操作:弹出时才将累加的增量应用到元素上,并将增量传递给下一个元素
 * 
 * 时间复杂度:
 * - push: O(1)
 * - pop: O(1)
 * - increment: O(1) - 这是关键优化,避免了O(k)的遍历
 * 
 * 空间复杂度:O(n) - 需要额外的增量数组
 * 
 * 是否最优解:是。通过懒惰更新将increment操作从O(k)优化到O(1)。
 */
class CustomStack {
private:
    int* stack_arr;         // 栈数组
    int* increment_arr;     // 增量数组,increment_arr[i]表示位置i的累加增量
    int top_idx;            // 栈顶指针
    int max_size;           // 栈的最大容量

public:
    CustomStack(int maxSize) {
        max_size = maxSize;
        stack_arr = new int[maxSize];
        increment_arr = new int[maxSize]();
        top_idx = -1;
    }
    
    ~CustomStack() {
        delete[] stack_arr;
        delete[] increment_arr;
    }
    
    void push(int x) {
        // 如果栈未满,则压入元素
        if (top_idx < max_size - 1) {
            top_idx++;
            stack_arr[top_idx] = x;
        }
    }
    
    int pop() {
        if (top_idx < 0) {
            return -1;
        }
        // 计算实际值(原值 + 累加增量)
        int result = stack_arr[top_idx] + increment_arr[top_idx];
        // 将当前位置的增量传递给下一个位置(关键步骤)
        if (top_idx > 0) {
            increment_arr[top_idx - 1] += increment_arr[top_idx];
        }
        // 清空当前位置的增量
        increment_arr[top_idx] = 0;
        top_idx--;
        return result;
    }
    
    void increment(int k, int val) {
        // 只更新第 min(k, top_idx+1)-1 个位置的增量
        // 这个增量会在pop时逐层传递
        int idx = std::min(k, top_idx + 1) - 1;
        if (idx >= 0) {
            increment_arr[idx] += val;
        }
    }
};

/**
 * 测试代码
 * 用于验证各个实现的正确性
 */
int main() {
    // 测试最小栈
    cout << "=== 测试最小栈 ===" << endl;
    MinStack minStack;
    minStack.push(-2);
    minStack.push(0);
    minStack.push(-3);
    std::cout << "当前最小值: " << minStack.min() << std::endl; // -3
    minStack.pop();
    cout << "栈顶元素: " << minStack.pop() << endl; // 0
    cout << "当前最小值: " << minStack.min() << endl; // -2
    
    // 测试最大栈
    cout << "\n=== 测试最大栈 ===" << endl;
    MaxStack maxStack;
    maxStack.push(5);
    maxStack.push(1);
    maxStack.push(5);
    cout << "栈顶元素: " << maxStack.top() << endl; // 5
    cout << "弹出最大值: " << maxStack.popMax() << endl; // 5
    cout << "栈顶元素: " << maxStack.top() << endl; // 1
    cout << "当前最大值: " << maxStack.peekMax() << endl; // 5
    cout << "弹出栈顶: " << maxStack.pop() << endl; // 1
    cout << "当前最大值: " << maxStack.peekMax() << endl; // 5
    
    // 测试排序栈
    cout << "\n=== 测试排序栈 ===" << endl;
    SortedStack sortedStack;
    sortedStack.push(1);
    sortedStack.push(2);
    cout << "栈顶(最小值): " << sortedStack.peek() << endl; // 1
    sortedStack.pop();
    cout << "弹出后栈顶: " << sortedStack.peek() << endl; // 2
    
    // 测试用栈实现队列
    cout << "\n=== 测试用栈实现队列 ===" << endl;
    MyQueue queue;
    queue.push(1);
    queue.push(2);
    cout << "队列头部: " << queue.peek() << endl; // 1
    cout << "弹出队列头部: " << queue.pop() << endl; // 1
    cout << "队列是否为空: " << queue.empty() << endl; // false
    
    // 测试支持增量操作的栈
    cout << "\n=== 测试支持增量操作的栈 ===" << endl;
    CustomStack customStack(3);
    customStack.push(1);
    customStack.push(2);
    cout << "弹出: " << customStack.pop() << endl; // 2
    customStack.push(2);
    customStack.push(3);
    customStack.push(4);
    customStack.increment(5, 100);
    customStack.increment(2, 100);
    cout << "弹出: " << customStack.pop() << endl; // 103
    cout << "弹出: " << customStack.pop() << endl; // 202
    cout << "弹出: " << customStack.pop() << endl; // 201
    cout << "弹出: " << customStack.pop() << endl; // -1
    
    cout << "\n所有测试完成!" << endl;
    
    return 0;
}

// 题目8：最小栈的泛型实现
// 题目来源：扩展实现
// 
// 题目描述：
// 实现一个支持泛型的最小栈，能够处理任何可比较的类型。
// 
// 解题思路：
// 扩展基本的最小栈实现，使用C++模板确保元素可以比较。
// 
// 时间复杂度：O(1) - 所有操作
// 空间复杂度：O(n) - 需要辅助栈
// 
// 工程化考量：
// 1. 泛型支持：增强代码复用性
// 2. 边界检查：防止空栈操作
// 3. 类型安全：确保元素类型正确

#include <stack>
#include <stdexcept>
#include <vector>
#include <queue>
#include <unordered_map>
#include <string>
#include <thread>
#include <mutex>
#include <memory>
#include <cassert>
#include <chrono>

using namespace std;

// 最小栈的泛型实现
template <typename T>
class GenericMinStack {
private:
    stack<T> dataStack;  // 数据栈
    stack<T> minStack;   // 辅助栈，存储最小值

public:
    GenericMinStack() {}
    
    void push(const T& val) {
        dataStack.push(val);
        // 如果辅助栈为空，或当前元素小于等于辅助栈栈顶，则压入当前元素
        if (minStack.empty() || val <= minStack.top()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.top());
        }
    }
    
    T pop() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        T val = dataStack.top();
        dataStack.pop();
        minStack.pop();
        return val;
    }
    
    T top() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return dataStack.top();
    }
    
    T getMin() {
        if (minStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return minStack.top();
    }
    
    bool isEmpty() const {
        return dataStack.empty();
    }
};

// 题目9：设计一个双端队列的最小栈
// 题目来源：力扣扩展题
// 
// 题目描述：
// 设计一个数据结构，支持在双端队列的两端进行添加和删除操作，并且能够在O(1)时间内获取最小值。
// 
// 解题思路：
// 使用两个双端队列，一个存储数据，一个维护最小值。每次在任意一端添加元素时，
// 同步更新最小值双端队列。
// 
// 时间复杂度：O(1) - 所有操作（均摊）
// 空间复杂度：O(n) - 需要额外的双端队列

class MinDeque {
private:
    deque<int> dataDeque;  // 数据双端队列
    deque<int> minDeque;   // 最小值双端队列

public:
    MinDeque() {}
    
    // 在队列头部添加元素
    void addFirst(int val) {
        dataDeque.push_front(val);
        // 维护最小值队列
        while (!minDeque.empty() && minDeque.front() > val) {
            minDeque.pop_front();
        }
        minDeque.push_front(val);
    }
    
    // 在队列尾部添加元素
    void addLast(int val) {
        dataDeque.push_back(val);
        // 维护最小值队列
        while (!minDeque.empty() && minDeque.back() > val) {
            minDeque.pop_back();
        }
        minDeque.push_back(val);
    }
    
    // 从队列头部移除元素
    int removeFirst() {
        if (dataDeque.empty()) {
            throw runtime_error("Deque is empty");
        }
        int val = dataDeque.front();
        dataDeque.pop_front();
        if (val == minDeque.front()) {
            minDeque.pop_front();
        }
        return val;
    }
    
    // 从队列尾部移除元素
    int removeLast() {
        if (dataDeque.empty()) {
            throw runtime_error("Deque is empty");
        }
        int val = dataDeque.back();
        dataDeque.pop_back();
        if (val == minDeque.back()) {
            minDeque.pop_back();
        }
        return val;
    }
    
    // 获取队列头部元素
    int getFirst() {
        if (dataDeque.empty()) {
            throw runtime_error("Deque is empty");
        }
        return dataDeque.front();
    }
    
    // 获取队列尾部元素
    int getLast() {
        if (dataDeque.empty()) {
            throw runtime_error("Deque is empty");
        }
        return dataDeque.back();
    }
    
    // 获取最小值
    int getMin() {
        if (minDeque.empty()) {
            throw runtime_error("Deque is empty");
        }
        return minDeque.front();
    }
    
    // 判断是否为空
    bool isEmpty() const {
        return dataDeque.empty();
    }
};

// 题目10：多栈共享最小值
// 题目来源：算法设计扩展题
// 
// 题目描述：
// 设计一个数据结构，支持创建多个栈，并且能够在O(1)时间内获取所有栈中的最小值。
// 
// 解题思路：
// 维护一个全局最小值堆和每个栈的最小值记录。使用unordered_map记录每个最小值出现的次数。
// 
// 时间复杂度：
// - push/pop: O(log k) - k为不同最小值的数量
// - getGlobalMin: O(1)
// 
// 空间复杂度：O(n + k) - n为所有栈元素总数，k为不同最小值的数量

class MultiStackMinSystem {
private:
    vector<stack<int>> stacks;  // 存储多个栈
    priority_queue<int, vector<int>, greater<int>> minHeap;  // 全局最小值堆
    unordered_map<int, int> minCount;  // 记录每个最小值的出现次数

public:
    MultiStackMinSystem() {}
    
    // 创建一个新栈
    int createStack() {
        stacks.emplace_back();
        return stacks.size() - 1;  // 返回栈的索引
    }
    
    // 向指定栈中压入元素
    void push(int stackId, int val) {
        if (stackId < 0 || stackId >= stacks.size()) {
            throw invalid_argument("Invalid stack ID");
        }
        stacks[stackId].push(val);
        
        // 更新最小值堆和计数
        minHeap.push(val);
        minCount[val]++;
    }
    
    // 从指定栈中弹出元素
    int pop(int stackId) {
        if (stackId < 0 || stackId >= stacks.size()) {
            throw invalid_argument("Invalid stack ID");
        }
        if (stacks[stackId].empty()) {
            throw runtime_error("Stack is empty");
        }
        
        int val = stacks[stackId].top();
        stacks[stackId].pop();
        
        // 更新计数
        minCount[val]--;
        if (minCount[val] == 0) {
            minCount.erase(val);
            // 清理堆顶无效元素
            while (!minHeap.empty() && minCount.find(minHeap.top()) == minCount.end()) {
                minHeap.pop();
            }
        }
        return val;
    }
    
    // 获取指定栈的栈顶元素
    int top(int stackId) {
        if (stackId < 0 || stackId >= stacks.size()) {
            throw invalid_argument("Invalid stack ID");
        }
        if (stacks[stackId].empty()) {
            throw runtime_error("Stack is empty");
        }
        return stacks[stackId].top();
    }
    
    // 获取所有栈中的全局最小值
    int getGlobalMin() {
        if (minHeap.empty()) {
            throw runtime_error("All stacks are empty");
        }
        return minHeap.top();
    }
};

// 题目11：最小栈的线程安全实现
// 题目来源：工程实践题
// 
// 题目描述：
// 实现一个线程安全的最小栈，在多线程环境下能够正确工作。
// 
// 解题思路：
// 使用互斥锁同步所有操作，确保线程安全。
// 
// 时间复杂度：O(1) - 所有操作，但由于锁的开销，实际性能可能降低
// 空间复杂度：O(n) - 需要辅助栈
// 
// 工程化考量：
// 1. 线程安全：使用互斥锁确保多线程环境下的正确性
// 2. 性能优化：可以考虑使用更细粒度的锁来提高并发性能

class ThreadSafeMinStack {
private:
    stack<int> dataStack;
    stack<int> minStack;
    mutable mutex mtx;  // 互斥锁，用于线程同步

public:
    ThreadSafeMinStack() {}
    
    void push(int val) {
        lock_guard<mutex> lock(mtx);  // 加锁
        dataStack.push(val);
        if (minStack.empty() || val <= minStack.top()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.top());
        }
    }
    
    int pop() {
        lock_guard<mutex> lock(mtx);  // 加锁
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        int val = dataStack.top();
        dataStack.pop();
        minStack.pop();
        return val;
    }
    
    int top() const {
        lock_guard<mutex> lock(mtx);  // 加锁
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return dataStack.top();
    }
    
    int getMin() const {
        lock_guard<mutex> lock(mtx);  // 加锁
        if (minStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return minStack.top();
    }
    
    bool isEmpty() const {
        lock_guard<mutex> lock(mtx);  // 加锁
        return dataStack.empty();
    }
};

// 题目12：支持撤销操作的最小栈
// 题目来源：力扣扩展题
// 
// 题目描述：
// 设计一个支持撤销操作的最小栈，可以撤销最近的push或pop操作。
// 
// 解题思路：
// 使用操作历史栈记录每次操作的类型和参数，撤销时根据历史记录恢复状态。
// 
// 时间复杂度：
// - push/pop: O(1)
// - undo: O(1) 对于撤销push，O(1) 对于撤销pop
// 
// 空间复杂度：O(n) - 需要额外的空间存储历史操作

class UndoableMinStack {
private:
    stack<int> dataStack;
    stack<int> minStack;
    
    // 操作历史
    struct Operation {
        string type;  // "push" 或 "pop"
        int value;    // push的值或pop的值
        int oldMin;   // 之前的最小值（用于撤销push）
        
        Operation(const string& t, int v, int om) : type(t), value(v), oldMin(om) {}
    };
    
    stack<Operation> history;

public:
    UndoableMinStack() {}
    
    void push(int val) {
        int oldMin = minStack.empty() ? INT_MAX : minStack.top();
        dataStack.push(val);
        if (minStack.empty() || val <= minStack.top()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.top());
        }
        history.emplace("push", val, oldMin);
    }
    
    int pop() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        int val = dataStack.top();
        dataStack.pop();
        int oldMin = minStack.top();
        minStack.pop();
        history.emplace("pop", val, oldMin);
        return val;
    }
    
    int top() {
        if (dataStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return dataStack.top();
    }
    
    int getMin() {
        if (minStack.empty()) {
            throw runtime_error("Stack is empty");
        }
        return minStack.top();
    }
    
    // 撤销最近的操作
    void undo() {
        if (history.empty()) {
            throw runtime_error("No operation to undo");
        }
        
        Operation op = history.top();
        history.pop();
        
        if (op.type == "push") {
            // 撤销push操作
            dataStack.pop();
            minStack.pop();
        } else if (op.type == "pop") {
            // 撤销pop操作，需要恢复数据和最小值
            dataStack.push(op.value);
            minStack.push(op.oldMin);
        }
    }
};

// 题目13：最小栈的单元测试示例
// 题目来源：工程实践
// 
// 题目描述：
// 为最小栈实现编写全面的单元测试，覆盖正常场景、边界场景和异常场景。
// 
// 测试策略：
// 1. 正常场景测试：基本操作流程
// 2. 边界场景测试：空栈、单元素栈、重复元素、极端值
// 3. 异常场景测试：空栈操作异常

class MinStackTest {
public:
    static void runTests() {
        cout << "=== 运行最小栈单元测试 ===" << endl;
        
        // 测试1：基本功能测试
        basicTest();
        
        // 测试2：边界场景测试
        boundaryTest();
        
        // 测试3：异常场景测试
        exceptionTest();
        
        cout << "=== 所有测试通过！===" << endl;
    }
    
private:
    // 使用题目1中的MinStack1作为测试对象
    static void basicTest() {
        cout << "\n1. 基本功能测试：" << endl;
        MinStack1 minStack;
        minStack.push(5);
        minStack.push(2);
        minStack.push(7);
        minStack.push(1);
        
        assert(minStack.getMin() == 1 && "最小值应该是1");
        assert(minStack.top() == 1 && "栈顶应该是1");
        
        minStack.pop();
        assert(minStack.getMin() == 2 && "弹出后最小值应该是2");
        assert(minStack.top() == 7 && "弹出后栈顶应该是7");
        
        cout << "基本功能测试通过" << endl;
    }
    
    static void boundaryTest() {
        cout << "\n2. 边界场景测试：" << endl;
        
        // 测试空栈
        MinStack1 emptyStack;
        
        // 测试单元素栈
        MinStack1 singleStack;
        singleStack.push(42);
        assert(singleStack.getMin() == 42 && "单元素栈最小值应该是该元素");
        assert(singleStack.top() == 42 && "单元素栈栈顶应该是该元素");
        singleStack.pop();
        
        // 测试重复元素
        MinStack1 duplicateStack;
        duplicateStack.push(3);
        duplicateStack.push(3);
        duplicateStack.push(3);
        assert(duplicateStack.getMin() == 3 && "重复元素栈最小值应该是3");
        duplicateStack.pop();
        assert(duplicateStack.getMin() == 3 && "弹出后最小值应该还是3");
        
        // 测试极端值
        MinStack1 extremeStack;
        extremeStack.push(INT_MIN);
        extremeStack.push(INT_MAX);
        assert(extremeStack.getMin() == INT_MIN && "最小值应该是INT_MIN");
        
        cout << "边界场景测试通过" << endl;
    }
    
    static void exceptionTest() {
        cout << "\n3. 异常场景测试：" << endl;
        MinStack1 exceptionStack;
        
        bool exceptionCaught = false;
        try {
            exceptionStack.pop();
        } catch (const exception& e) {
            exceptionCaught = true;
        }
        assert(exceptionCaught && "空栈pop应该抛出异常");
        
        exceptionCaught = false;
        try {
            exceptionStack.top();
        } catch (const exception& e) {
            exceptionCaught = true;
        }
        assert(exceptionCaught && "空栈top应该抛出异常");
        
        exceptionCaught = false;
        try {
            exceptionStack.getMin();
        } catch (const exception& e) {
            exceptionCaught = true;
        }
        assert(exceptionCaught && "空栈getMin应该抛出异常");
        
        cout << "异常场景测试通过" << endl;
    }
};

// 题目14：最小栈的性能优化分析
// 题目来源：算法优化实践
// 
// 题目描述：
// 分析不同最小栈实现的性能特点，进行性能测试和优化建议。
// 
// 优化方向：
// 1. 空间优化：辅助栈只存储必要的最小值
// 2. 内存局部性：使用数组代替stack容器提高缓存命中率
// 3. 避免不必要的内存分配：使用预分配的数组

class MinStackPerformanceAnalyzer {
private:
    // 最小栈接口定义
    struct MinStackInterface {
        virtual ~MinStackInterface() = default;
        virtual void push(int val) = 0;
        virtual void pop() = 0;
        virtual int getMin() = 0;
    };
    
    // 标准实现
    class StandardMinStack : public MinStackInterface {
    private:
        stack<int> data;
        stack<int> min;
    public:
        void push(int val) override {
            data.push(val);
            if (min.empty() || val <= min.top()) {
                min.push(val);
            } else {
                min.push(min.top());
            }
        }
        
        void pop() override {
            data.pop();
            min.pop();
        }
        
        int getMin() override {
            return min.top();
        }
    };
    
    // 空间优化实现
    class SpaceOptimizedMinStack : public MinStackInterface {
    private:
        stack<int> data;
        stack<int> min;
    public:
        void push(int val) override {
            data.push(val);
            if (min.empty() || val <= min.top()) {
                min.push(val);
            }
        }
        
        void pop() override {
            if (data.top() == min.top()) {
                min.pop();
            }
            data.pop();
        }
        
        int getMin() override {
            return min.top();
        }
    };
    
    // 数组实现
    class ArrayMinStack : public MinStackInterface {
    private:
        static const int MAX_SIZE = 1000000;
        int data[MAX_SIZE];
        int min[MAX_SIZE];
        int size = 0;
    public:
        void push(int val) override {
            data[size] = val;
            if (size == 0 || val <= min[size - 1]) {
                min[size] = val;
            } else {
                min[size] = min[size - 1];
            }
            size++;
        }
        
        void pop() override {
            size--;
        }
        
        int getMin() override {
            return min[size - 1];
        }
    };
    
public:
    static void analyzePerformance() {
        cout << "=== 最小栈性能分析 ===" << endl;
        
        // 测试不同实现的性能
        testImplementation("标准实现", std::unique_ptr<MinStackInterface>(new StandardMinStack()));
        testImplementation("空间优化实现", std::unique_ptr<MinStackInterface>(new SpaceOptimizedMinStack()));
        testImplementation("数组实现", std::unique_ptr<MinStackInterface>(new ArrayMinStack()));
    }
    
    static void testImplementation(const string& name, unique_ptr<MinStackInterface> stack) {
        cout << "\n测试 " << name << "：" << endl;
        
        // 测试push性能
        auto start = chrono::high_resolution_clock::now();
        for (int i = 0; i < 100000; i++) {
            stack->push(i % 1000);
        }
        auto end = chrono::high_resolution_clock::now();
        auto pushTime = chrono::duration_cast<chrono::milliseconds>(end - start).count();
        cout << "Push 100,000 elements: " << pushTime << " ms" << endl;
        
        // 测试getMin性能
        start = chrono::high_resolution_clock::now();
        for (int i = 0; i < 100000; i++) {
            stack->getMin();
        }
        end = chrono::high_resolution_clock::now();
        auto getMinTime = chrono::duration_cast<chrono::milliseconds>(end - start).count();
        cout << "GetMin 100,000 times: " << getMinTime << " ms" << endl;
        
        // 测试pop性能
        start = chrono::high_resolution_clock::now();
        for (int i = 0; i < 100000; i++) {
            stack->pop();
        }
        end = chrono::high_resolution_clock::now();
        auto popTime = chrono::duration_cast<chrono::milliseconds>(end - start).count();
        cout << "Pop 100,000 elements: " << popTime << " ms" << endl;
    }
};

// 题目15：最小栈与机器学习的联系
// 题目来源：跨领域应用
// 
// 题目描述：
// 探讨最小栈在机器学习和数据分析中的应用场景。
// 
// 应用场景：
// 1. 在线学习中的滑动窗口最小值监控
// 2. 异常检测算法中的阈值维护
// 3. 梯度下降算法中的学习率自适应调整

class MinStackMLApplications {
public:
    // 示例：使用最小栈实现滑动窗口最小值监控
    static vector<int> slidingWindowMinimum(const vector<int>& nums, int windowSize) {
        vector<int> result;
        if (nums.empty() || windowSize <= 0 || windowSize > nums.size()) {
            return result;
        }
        
        // 使用两个栈实现队列，并维护最小值
        class MinQueue {
        private:
            stack<int> stack1;
            stack<int> minStack1;
            stack<int> stack2;
            stack<int> minStack2;
            
            void transferIfNeeded() {
                if (stack2.empty()) {
                    while (!stack1.empty()) {
                        int val = stack1.top();
                        stack1.pop();
                        stack2.push(val);
                        int currentMin = minStack2.empty() ? val : min(val, minStack2.top());
                        minStack2.push(currentMin);
                    }
                }
            }
        
        public:
            void push(int val) {
                stack1.push(val);
                int currentMin = minStack1.empty() ? val : min(val, minStack1.top());
                minStack1.push(currentMin);
            }
            
            int pop() {
                transferIfNeeded();
                int val = stack2.top();
                stack2.pop();
                minStack2.pop();
                return val;
            }
            
            int getMin() {
                if (stack1.empty()) return minStack2.top();
                if (stack2.empty()) return minStack1.top();
                return min(minStack1.top(), minStack2.top());
            }
        };
        
        MinQueue minQueue;
        
        // 初始化窗口
        for (int i = 0; i < windowSize - 1; i++) {
            minQueue.push(nums[i]);
        }
        
        // 滑动窗口
        for (int i = windowSize - 1; i < nums.size(); i++) {
            minQueue.push(nums[i]);
            result.push_back(minQueue.getMin());
            minQueue.pop();
        }
        
        return result;
    }
};