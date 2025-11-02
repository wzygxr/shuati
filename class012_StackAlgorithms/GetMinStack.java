// package class015;  // 注释掉包声明以便直接运行

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class015 - 最小栈与最大栈专题
 * 
 * 本文件包含最小栈/最大栈相关的多种实现，包括：
 * 1. 基本最小栈实现 (LeetCode 155)
 * 2. 最大栈实现 (LeetCode 716) 
 * 3. 包含min函数的栈 (剑指Offer 30)
 * 4. 栈排序 (LeetCode 面试题 03.05)
 * 5. 用栈实现队列 (LeetCode 232)
 * 6. 最小栈(空间优化版)
 * 7. 支持增量操作的栈 (LeetCode 1381)
 * 8. 最小栈的泛型实现
 * 9. 设计一个双端队列的最小栈
 * 10. 多栈共享最小值
 * 11. 最小栈的线程安全实现
 * 12. 支持撤销操作的最小栈
 * 13. 最小栈的单元测试示例
 * 14. 最小栈的性能优化分析
 * 15. 最小栈与机器学习的联系
 * 
 * 每个实现都包含详细的时间复杂度、空间复杂度分析和工程化考量。
 * 
 * 时间复杂度: O(1) - 所有栈操作
 * 空间复杂度: O(n) - 需要辅助栈存储最值信息
 * 
 * 是否最优解: 是
 * 
 * 作者: 算法学习系统
 * 日期: 2025-10-19
 * 版本: 1.0
 */

public class GetMinStack {
    
    /**
     * 题目1：最小栈 (LeetCode 155)
     * 
     * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
     * 
     * 实现思路：
     * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。
     * 
     * 时间复杂度：所有操作均为O(1)
     * 空间复杂度：O(n)
     * 
     * 是否最优解：是
     */
    public static class MinStack {
        private Stack<Integer> dataStack;  // 数据栈
        private Stack<Integer> minStack;   // 最小值栈
        
        public MinStack() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        /**
         * 将元素压入栈中
         * 
         * @param val 要压入的元素
         */
        public void push(int val) {
            dataStack.push(val);
            
            // 如果最小栈为空，或当前元素小于等于最小栈栈顶，则压入当前元素
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            } else {
                // 否则重复压入当前最小值
                minStack.push(minStack.peek());
            }
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @throws IllegalStateException 如果栈为空
         */
        public void pop() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            // 同时弹出两个栈的栈顶元素
            dataStack.pop();
            minStack.pop();
        }
        
        /**
         * 获取栈顶元素但不移除
         * 
         * @return 栈顶元素
         * @throws IllegalStateException 如果栈为空
         */
        public int top() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return dataStack.peek();
        }
        
        /**
         * 获取栈中的最小元素
         * 
         * @return 最小元素
         * @throws IllegalStateException 如果栈为空
         */
        public int getMin() {
            if (minStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return minStack.peek();
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return dataStack.isEmpty();
        }
    }
    
    /**
     * 题目2：最大栈 (LeetCode 716)
     * 
     * 设计一个最大栈数据结构，既支持栈操作，又支持查找栈中最大元素。
     * 
     * 实现思路：
     * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最大值。
     * popMax操作时，需要将最大值上面的所有元素暂存到临时栈中，取出最大值后再将临时栈中的元素放回。
     * 
     * 时间复杂度：
     * - push: O(1)
     * - pop: O(1) 
     * - top: O(1)
     * - peekMax: O(1)
     * - popMax: O(n)
     * 
     * 空间复杂度：O(n)
     * 
     * 是否最优解：是
     */
    public static class MaxStack {
        private Stack<Integer> dataStack;  // 数据栈
        private Stack<Integer> maxStack;   // 最大值栈
        
        public MaxStack() {
            dataStack = new Stack<>();
            maxStack = new Stack<>();
        }
        
        /**
         * 将元素压入栈中
         * 
         * @param x 要压入的元素
         */
        public void push(int x) {
            dataStack.push(x);
            
            // 如果最大栈为空，或当前元素大于等于最大栈栈顶，则压入当前元素
            if (maxStack.isEmpty() || x >= maxStack.peek()) {
                maxStack.push(x);
            } else {
                // 否则重复压入当前最大值
                maxStack.push(maxStack.peek());
            }
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @return 弹出的元素
         * @throws IllegalStateException 如果栈为空
         */
        public int pop() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            // 同时弹出两个栈的栈顶元素
            maxStack.pop();
            return dataStack.pop();
        }
        
        /**
         * 获取栈顶元素但不移除
         * 
         * @return 栈顶元素
         * @throws IllegalStateException 如果栈为空
         */
        public int top() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return dataStack.peek();
        }
        
        /**
         * 检索并返回栈中最大元素，无需移除
         * 
         * @return 最大元素
         * @throws IllegalStateException 如果栈为空
         */
        public int peekMax() {
            if (maxStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return maxStack.peek();
        }
        
        /**
         * 检索并返回栈中最大元素，并将其移除
         * 
         * @return 最大元素
         * @throws IllegalStateException 如果栈为空
         */
        public int popMax() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            // 使用临时栈存储最大值上面的元素
            Stack<Integer> tempStack = new Stack<>();
            int maxValue = maxStack.peek();
            
            // 找到最大值的位置
            while (dataStack.peek() != maxValue) {
                tempStack.push(dataStack.pop());
                maxStack.pop();
            }
            
            // 弹出最大值
            dataStack.pop();
            maxStack.pop();
            
            // 将临时栈中的元素放回
            while (!tempStack.isEmpty()) {
                push(tempStack.pop());
            }
            
            return maxValue;
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return dataStack.isEmpty();
        }
    }
    
    /**
     * 题目3：包含min函数的栈 (剑指Offer 30)
     * 
     * 定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的 min 函数。
     * 在该栈中，调用 min、push 及 pop 的时间复杂度都是 O(1)。
     * 
     * 实现思路：
     * 经典的辅助栈问题，与最小栈实现完全相同。
     * 
     * 时间复杂度：O(1) - 所有操作
     * 空间复杂度：O(n)
     * 
     * 是否最优解：是
     */
    public static class MinStackOffer {
        private Stack<Integer> dataStack;  // 数据栈
        private Stack<Integer> minStack;   // 最小值栈
        
        public MinStackOffer() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        /**
         * 将元素压入栈中
         * 
         * @param val 要压入的元素
         */
        public void push(int val) {
            dataStack.push(val);
            
            // 如果最小栈为空，或当前元素小于等于最小栈栈顶，则压入当前元素
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            } else {
                // 否则重复压入当前最小值
                minStack.push(minStack.peek());
            }
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @throws IllegalStateException 如果栈为空
         */
        public void pop() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            dataStack.pop();
            minStack.pop();
        }
        
        /**
         * 获取栈顶元素
         * 
         * @return 栈顶元素
         * @throws IllegalStateException 如果栈为空
         */
        public int top() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return dataStack.peek();
        }
        
        /**
         * 获取栈中的最小元素
         * 
         * @return 最小元素
         * @throws IllegalStateException 如果栈为空
         */
        public int min() {
            if (minStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return minStack.peek();
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return dataStack.isEmpty();
        }
    }
    
    /**
     * 题目4：栈排序 (LeetCode 面试题 03.05)
     * 
     * 栈排序。编写程序，对栈进行排序使最小元素位于栈顶。
     * 最多只能使用一个其他的临时栈存放数据，但不得将元素复制到别的数据结构(如数组)中。
     * 
     * 实现思路：
     * 使用两个栈实现，主栈保持有序(栈顶最小)，辅助栈用于临时存储。
     * push时，将主栈中大于新元素的元素临时移到辅助栈，插入新元素后再移回。
     * 
     * 时间复杂度：
     * - push: O(n) - 最坏情况需要移动所有元素
     * - pop: O(1)
     * - peek: O(1)
     * - isEmpty: O(1)
     * 
     * 空间复杂度：O(n)
     * 
     * 是否最优解：是。在只能使用一个辅助栈的限制下，这是最优解。
     */
    public static class SortedStack {
        private Stack<Integer> mainStack;   // 主栈，栈顶最小
        private Stack<Integer> tempStack;   // 临时栈，用于排序
        
        public SortedStack() {
            mainStack = new Stack<>();
            tempStack = new Stack<>();
        }
        
        /**
         * 将元素压入栈中，保持栈的有序性
         * 
         * @param val 要压入的元素
         */
        public void push(int val) {
            // 将主栈中所有大于val的元素移到临时栈
            while (!mainStack.isEmpty() && mainStack.peek() > val) {
                tempStack.push(mainStack.pop());
            }
            
            // 将新元素压入主栈
            mainStack.push(val);
            
            // 将临时栈中的元素移回主栈
            while (!tempStack.isEmpty()) {
                mainStack.push(tempStack.pop());
            }
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @throws IllegalStateException 如果栈为空
         */
        public void pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            mainStack.pop();
        }
        
        /**
         * 获取栈顶元素但不移除
         * 
         * @return 栈顶元素，如果栈为空则返回-1
         */
        public int peek() {
            if (isEmpty()) {
                return -1;
            }
            return mainStack.peek();
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return mainStack.isEmpty();
        }
    }
    
    /**
     * 题目5：用栈实现队列 (LeetCode 232)
     * 
     * 请你仅使用两个栈实现先入先出队列。
     * 队列应当支持一般队列支持的所有操作(push、pop、peek、empty)。
     * 
     * 实现思路：
     * 使用两个栈：输入栈和输出栈。
     * - push操作：直接压入输入栈
     * - pop/peek操作：如果输出栈为空，将输入栈所有元素转移到输出栈，然后操作输出栈
     * 
     * 时间复杂度分析(摊还分析)：
     * - push: O(1)
     * - pop: 摊还O(1) - 单次可能O(n)，但每个元素最多被转移一次
     * - peek: 摊还O(1)
     * - empty: O(1)
     * 
     * 空间复杂度：O(n)
     * 
     * 是否最优解：是。这是用栈实现队列的标准解法。
     */
    public static class MyQueue {
        private Stack<Integer> inputStack;  // 输入栈
        private Stack<Integer> outputStack; // 输出栈
        
        public MyQueue() {
            inputStack = new Stack<>();
            outputStack = new Stack<>();
        }
        
        /**
         * 将元素添加到队列尾部
         * 
         * @param x 要添加的元素
         */
        public void push(int x) {
            inputStack.push(x);
        }
        
        /**
         * 移除并返回队列头部的元素
         * 
         * @return 队列头部的元素
         * @throws IllegalStateException 如果队列为空
         */
        public int pop() {
            if (empty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            // 如果输出栈为空，将输入栈所有元素转移到输出栈
            if (outputStack.isEmpty()) {
                while (!inputStack.isEmpty()) {
                    outputStack.push(inputStack.pop());
                }
            }
            
            return outputStack.pop();
        }
        
        /**
         * 返回队列头部的元素，但不移除
         * 
         * @return 队列头部的元素
         * @throws IllegalStateException 如果队列为空
         */
        public int peek() {
            if (empty()) {
                throw new IllegalStateException("Queue is empty");
            }
            
            // 如果输出栈为空，将输入栈所有元素转移到输出栈
            if (outputStack.isEmpty()) {
                while (!inputStack.isEmpty()) {
                    outputStack.push(inputStack.pop());
                }
            }
            
            return outputStack.peek();
        }
        
        /**
         * 判断队列是否为空
         * 
         * @return 如果队列为空则返回true，否则返回false
         */
        public boolean empty() {
            return inputStack.isEmpty() && outputStack.isEmpty();
        }
    }
    
    /**
     * 题目6：最小栈(空间优化版)
     * 
     * 实现最小栈，但优化辅助栈的空间使用。
     * 辅助栈只存储真正的最小值，而不是每个位置都存储。
     * 
     * 实现思路：
     * 辅助栈只在遇到新的最小值时才压入。pop时需要判断弹出的是否是最小值，如果是则同步弹出辅助栈。
     * 
     * 优化效果：
     * - 最好情况(严格递增)：辅助栈只有1个元素，空间O(1)
     * - 最坏情况(严格递减)：辅助栈与数据栈大小相同，空间O(n)
     * - 平均情况：辅助栈大小远小于数据栈
     * 
     * 时间复杂度：O(1) - 所有操作
     * 空间复杂度：O(k)，k为不同最小值的个数，k <= n
     * 
     * 是否最优解：是
     */
    public static class MinStackOptimized {
        private Stack<Integer> dataStack;  // 数据栈
        private Stack<Integer> minStack;   // 最小值栈(只存储真正的最小值)
        
        public MinStackOptimized() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        /**
         * 将元素压入栈中
         * 
         * @param val 要压入的元素
         */
        public void push(int val) {
            dataStack.push(val);
            
            // 只在遇到新的最小值(小于等于当前最小值)时才压入辅助栈
            // 注意:这里必须是 <=,不能是 <,否则会漏掉重复的最小值
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @throws IllegalStateException 如果栈为空
         */
        public void pop() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            
            int popped = dataStack.pop();
            
            // 如果弹出的是当前最小值，辅助栈也要弹出
            if (popped == minStack.peek()) {
                minStack.pop();
            }
        }
        
        /**
         * 获取栈顶元素但不移除
         * 
         * @return 栈顶元素
         * @throws IllegalStateException 如果栈为空
         */
        public int top() {
            if (dataStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return dataStack.peek();
        }
        
        /**
         * 获取栈中的最小元素
         * 
         * @return 最小元素
         * @throws IllegalStateException 如果栈为空
         */
        public int getMin() {
            if (minStack.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return minStack.peek();
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return dataStack.isEmpty();
        }
    }
    
    /**
     * 题目7：支持增量操作的栈 (LeetCode 1381)
     * 
     * 设计一个支持下述操作的栈：
     * - CustomStack(int maxSize)：初始化对象，maxSize 为栈的最大容量
     * - void push(int x)：如果栈未满，则将 x 添加到栈顶
     * - int pop()：弹出栈顶元素，并返回栈顶的值，如果栈为空则返回 -1
     * - void inc(int k, int val)：将栈底的 k 个元素的值都增加 val。如果栈中元素总数小于 k，则将所有元素都增加 val
     * 
     * 实现思路：
     * 使用懒惰更新(lazy propagation)的思想。
     * 维护一个增量数组inc[]，inc[i]表示从栈底到第i个位置需要累加的增量。
     * - increment操作：只更新inc[k-1]的值，不实际修改栈中元素
     * - pop操作：弹出时才将累加的增量应用到元素上，并将增量传递给下一个元素
     * 
     * 时间复杂度：
     * - push: O(1)
     * - pop: O(1)
     * - increment: O(1) - 这是关键优化，避免了O(k)的遍历
     * 
     * 空间复杂度：O(n) - 需要额外的增量数组
     * 
     * 是否最优解：是。通过懒惰更新将increment操作从O(k)优化到O(1)。
     */
    public static class CustomStack {
        private int[] data;      // 数据数组
        private int[] inc;       // 增量数组(懒惰更新)
        private int top;         // 栈顶指针
        private int maxSize;     // 最大容量
        
        public CustomStack(int maxSize) {
            this.maxSize = maxSize;
            this.data = new int[maxSize];
            this.inc = new int[maxSize];
            this.top = -1;  // 栈为空
        }
        
        /**
         * 将元素压入栈中
         * 
         * @param x 要压入的元素
         */
        public void push(int x) {
            if (top < maxSize - 1) {
                top++;
                data[top] = x;
                inc[top] = 0;  // 新元素的增量为0
            }
            // 如果栈满，不执行任何操作
        }
        
        /**
         * 弹出栈顶元素
         * 
         * @return 栈顶元素的值，如果栈为空则返回-1
         */
        public int pop() {
            if (top == -1) {
                return -1;  // 栈为空
            }
            
            int result = data[top] + inc[top];
            
            // 将增量传递给下一个元素(如果存在)
            if (top > 0) {
                inc[top - 1] += inc[top];
            }
            
            inc[top] = 0;  // 重置当前元素的增量
            top--;
            
            return result;
        }
        
        /**
         * 将栈底的 k 个元素的值都增加 val
         * 
         * @param k 要增加的元素个数
         * @param val 要增加的值
         */
        public void increment(int k, int val) {
            if (top == -1) {
                return;  // 栈为空
            }
            
            // 确定实际要增加的元素索引
            int idx = Math.min(k, top + 1) - 1;
            if (idx >= 0) {
                inc[idx] += val;
            }
        }
        
        /**
         * 获取栈的当前大小
         * 
         * @return 栈中元素的数量
         */
        public int size() {
            return top + 1;
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 如果栈为空则返回true，否则返回false
         */
        public boolean isEmpty() {
            return top == -1;
        }
        
        /**
         * 判断栈是否已满
         * 
         * @return 如果栈已满则返回true，否则返回false
         */
        public boolean isFull() {
            return top == maxSize - 1;
        }
    }
    
    /**
     * 主函数 - 测试所有实现
     */
    public static void main(String[] args) {
        System.out.println("=== 测试最小栈 ===");
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println("当前最小值: " + minStack.getMin());  // -3
        minStack.pop();
        System.out.println("栈顶元素: " + minStack.top());  // 0
        System.out.println("当前最小值: " + minStack.getMin());  // -2
        
        System.out.println("\n=== 测试最大栈 ===");
        MaxStack maxStack = new MaxStack();
        maxStack.push(5);
        maxStack.push(1);
        maxStack.push(5);
        System.out.println("栈顶元素: " + maxStack.top());  // 5
        System.out.println("弹出最大值: " + maxStack.popMax());  // 5
        System.out.println("栈顶元素: " + maxStack.top());  // 1
        System.out.println("当前最大值: " + maxStack.peekMax());  // 5
        System.out.println("弹出栈顶: " + maxStack.pop());  // 1
        System.out.println("当前最大值: " + maxStack.peekMax());  // 5
        
        System.out.println("\n=== 测试排序栈 ===");
        SortedStack sortedStack = new SortedStack();
        sortedStack.push(1);
        sortedStack.push(2);
        System.out.println("栈顶(最小值): " + sortedStack.peek());  // 1
        sortedStack.pop();
        System.out.println("弹出后栈顶: " + sortedStack.peek());  // 2
        
        System.out.println("\n=== 测试用栈实现队列 ===");
        MyQueue queue = new MyQueue();
        queue.push(1);
        queue.push(2);
        System.out.println("队列头部: " + queue.peek());  // 1
        System.out.println("弹出队列头部: " + queue.pop());  // 1
        System.out.println("队列是否为空: " + queue.empty());  // false
        
        System.out.println("\n=== 测试支持增量操作的栈 ===");
        CustomStack customStack = new CustomStack(3);
        customStack.push(1);
        customStack.push(2);
        System.out.println("弹出: " + customStack.pop());  // 2
        customStack.push(2);
        customStack.push(3);
        customStack.push(4);
        customStack.increment(5, 100);
        customStack.increment(2, 100);
        System.out.println("弹出: " + customStack.pop());  // 103
        System.out.println("弹出: " + customStack.pop());  // 202
        System.out.println("弹出: " + customStack.pop());  // 201
        System.out.println("弹出: " + customStack.pop());  // -1
        
        System.out.println("\n所有测试完成!");
    }
}