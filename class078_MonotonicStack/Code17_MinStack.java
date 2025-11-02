package class053;

import java.util.*;

/**
 * 最小栈
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
 * 是否为最优解：
 * 是，这是解决该问题的最优解之一
 * 
 * 工程化考量：
 * 1. 线程安全：在多线程环境下需要同步操作
 * 2. 异常处理：处理栈为空时的pop和top操作
 * 3. 内存管理：合理管理栈空间，避免内存泄漏
 * 
 * 算法调试技巧：
 * 1. 边界测试：测试空栈操作、单个元素操作
 * 2. 顺序测试：测试push、pop、getMin的组合操作
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 相关题目：
 * 1. 最大栈（LeetCode 716）- 类似思路维护最大值
 * 2. 队列的最大值（剑指Offer 59）- 使用双端队列维护最大值
 * 3. 滑动窗口最大值（LeetCode 239）- 使用单调队列
 * 
 * 语言特性差异：
 * - Java: 使用Stack或Deque实现
 * - C++: 使用stack容器
 * - Python: 使用list实现栈操作
 */
public class Code17_MinStack {
    
    /**
     * 最小栈实现类
     */
    static class MinStack {
        private Stack<Integer> dataStack;    // 数据栈
        private Stack<Integer> minStack;     // 最小值栈
        
        public MinStack() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        /**
         * 压入元素
         * @param x 要压入的元素
         */
        public void push(int x) {
            dataStack.push(x);
            // 如果最小值栈为空，或者x小于等于当前最小值，则压入最小值栈
            if (minStack.isEmpty() || x <= minStack.peek()) {
                minStack.push(x);
            }
        }
        
        /**
         * 弹出栈顶元素
         * @throws EmptyStackException 如果栈为空
         */
        public void pop() {
            if (dataStack.isEmpty()) {
                throw new EmptyStackException();
            }
            int popped = dataStack.pop();
            // 如果弹出的是当前最小值，则也从最小值栈弹出
            if (popped == minStack.peek()) {
                minStack.pop();
            }
        }
        
        /**
         * 获取栈顶元素
         * @return 栈顶元素
         * @throws EmptyStackException 如果栈为空
         */
        public int top() {
            if (dataStack.isEmpty()) {
                throw new EmptyStackException();
            }
            return dataStack.peek();
        }
        
        /**
         * 获取栈中的最小值
         * @return 最小值
         * @throws EmptyStackException 如果栈为空
         */
        public int getMin() {
            if (minStack.isEmpty()) {
                throw new EmptyStackException();
            }
            return minStack.peek();
        }
        
        /**
         * 检查栈是否为空
         * @return 如果栈为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return dataStack.isEmpty();
        }
        
        /**
         * 获取栈的大小
         * @return 栈中元素的数量
         */
        public int size() {
            return dataStack.size();
        }
    }
    
    /**
     * 线程安全的最小栈实现
     * 使用synchronized关键字保证线程安全
     */
    static class ThreadSafeMinStack {
        private final Stack<Integer> dataStack;
        private final Stack<Integer> minStack;
        
        public ThreadSafeMinStack() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        public synchronized void push(int x) {
            dataStack.push(x);
            if (minStack.isEmpty() || x <= minStack.peek()) {
                minStack.push(x);
            }
        }
        
        public synchronized void pop() {
            if (dataStack.isEmpty()) {
                throw new EmptyStackException();
            }
            int popped = dataStack.pop();
            if (popped == minStack.peek()) {
                minStack.pop();
            }
        }
        
        public synchronized int top() {
            if (dataStack.isEmpty()) {
                throw new EmptyStackException();
            }
            return dataStack.peek();
        }
        
        public synchronized int getMin() {
            if (minStack.isEmpty()) {
                throw new EmptyStackException();
            }
            return minStack.peek();
        }
        
        public synchronized boolean isEmpty() {
            return dataStack.isEmpty();
        }
        
        public synchronized int size() {
            return dataStack.size();
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void testMinStack() {
        System.out.println("=== 最小栈单元测试 ===");
        
        // 测试用例1：基本操作
        MinStack stack1 = new MinStack();
        stack1.push(-2);
        stack1.push(0);
        stack1.push(-3);
        System.out.println("测试用例1 - 压入[-2, 0, -3]");
        System.out.println("当前最小值: " + stack1.getMin()); // 期望: -3
        stack1.pop();
        System.out.println("弹出后栈顶: " + stack1.top());     // 期望: 0
        System.out.println("当前最小值: " + stack1.getMin()); // 期望: -2
        
        // 测试用例2：重复最小值
        MinStack stack2 = new MinStack();
        stack2.push(5);
        stack2.push(3);
        stack2.push(3);
        stack2.push(7);
        System.out.println("\n测试用例2 - 压入[5, 3, 3, 7]");
        System.out.println("当前最小值: " + stack2.getMin()); // 期望: 3
        stack2.pop(); // 弹出7
        System.out.println("弹出7后最小值: " + stack2.getMin()); // 期望: 3
        stack2.pop(); // 弹出3
        System.out.println("弹出3后最小值: " + stack2.getMin()); // 期望: 3
        stack2.pop(); // 弹出3
        System.out.println("弹出3后最小值: " + stack2.getMin()); // 期望: 5
        
        // 测试用例3：边界情况 - 单个元素
        MinStack stack3 = new MinStack();
        stack3.push(10);
        System.out.println("\n测试用例3 - 单个元素10");
        System.out.println("栈顶: " + stack3.top());     // 期望: 10
        System.out.println("最小值: " + stack3.getMin()); // 期望: 10
        
        // 测试用例4：边界情况 - 空栈异常处理
        MinStack stack4 = new MinStack();
        try {
            stack4.pop();
        } catch (EmptyStackException e) {
            System.out.println("\n测试用例4 - 空栈pop操作抛出异常: " + e.getClass().getSimpleName());
        }
        
        // 测试用例5：大规模数据测试
        MinStack stack5 = new MinStack();
        int n = 10000;
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < n; i++) {
            int num = random.nextInt(1000);
            stack5.push(num);
        }
        
        for (int i = 0; i < n; i++) {
            stack5.getMin();
            stack5.pop();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("\n测试用例5 - 大规模数据测试(" + n + "个元素)");
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 性能对比测试：普通栈 vs 最小栈
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能对比测试 ===");
        
        int n = 100000;
        Random random = new Random();
        
        // 测试普通栈
        Stack<Integer> normalStack = new Stack<>();
        long startTime1 = System.currentTimeMillis();
        
        for (int i = 0; i < n; i++) {
            normalStack.push(random.nextInt(1000));
        }
        
        for (int i = 0; i < n; i++) {
            normalStack.pop();
        }
        
        long endTime1 = System.currentTimeMillis();
        
        // 测试最小栈
        MinStack minStack = new MinStack();
        long startTime2 = System.currentTimeMillis();
        
        for (int i = 0; i < n; i++) {
            minStack.push(random.nextInt(1000));
        }
        
        for (int i = 0; i < n; i++) {
            minStack.getMin();
            minStack.pop();
        }
        
        long endTime2 = System.currentTimeMillis();
        
        System.out.println("普通栈操作时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("最小栈操作时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("性能开销比例: " + 
            String.format("%.2f", (double)(endTime2 - startTime2) / (endTime1 - startTime1)));
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testMinStack();
        
        // 运行性能对比测试
        performanceComparison();
        
        System.out.println("\n=== 最小栈算法验证完成 ===");
    }
}