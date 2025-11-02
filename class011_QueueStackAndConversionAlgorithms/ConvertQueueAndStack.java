import java.util.*;

/**
 * 队列和栈转换算法实现类
 * 包含LeetCode等各大算法平台中队列和栈相关的经典题目
 * 时间复杂度：O(n) - O(n^2) 根据具体算法
 * 空间复杂度：O(n) - O(n) 根据具体算法
 */
public class ConvertQueueAndStack {
    
    /**
     * 柱状图中最大的矩形 - 单调栈解法
     * LeetCode 84: https://leetcode.com/problems/largest-rectangle-in-histogram/
     * 时间复杂度：O(n)，每个元素入栈出栈一次
     * 空间复杂度：O(n)，栈空间
     */
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        
        int[] left = new int[n];
        int[] right = new int[n];
        Stack<Integer> st = new Stack<>();
        
        // 计算每个柱子左边第一个小于它的位置
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                st.pop();
            }
            left[i] = st.isEmpty() ? -1 : st.peek();
            st.push(i);
        }
        
        // 清空栈
        st.clear();
        
        // 计算每个柱子右边第一个小于它的位置
        for (int i = n - 1; i >= 0; i--) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                st.pop();
            }
            right[i] = st.isEmpty() ? n : st.peek();
            st.push(i);
        }
        
        // 计算最大面积
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = right[i] - left[i] - 1;
            maxArea = Math.max(maxArea, heights[i] * width);
        }
        
        return maxArea;
    }
    
    /**
     * 柱状图中最大的矩形 - 优化版单调栈
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int largestRectangleAreaOptimized(int[] heights) {
        int n = heights.length;
        if (n == 0) return 0;
        
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(right, n);
        Stack<Integer> st = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            while (!st.isEmpty() && heights[st.peek()] >= heights[i]) {
                right[st.peek()] = i;
                st.pop();
            }
            left[i] = st.isEmpty() ? -1 : st.peek();
            st.push(i);
        }
        
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            maxArea = Math.max(maxArea, heights[i] * (right[i] - left[i] - 1));
        }
        
        return maxArea;
    }
    
    /**
     * 字符串解码 - 栈解法
     * LeetCode 394: https://leetcode.com/problems/decode-string/
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public String decodeString(String s) {
        Stack<Object[]> st = new Stack<>();
        int currentNum = 0;
        StringBuilder currentStr = new StringBuilder();
        
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                currentNum = currentNum * 10 + (c - '0');
            } else if (c == '[') {
                st.push(new Object[]{currentNum, currentStr.toString()});
                currentNum = 0;
                currentStr = new StringBuilder();
            } else if (c == ']') {
                Object[] top = st.pop();
                int num = (int) top[0];
                String prevStr = (String) top[1];
                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < num; i++) {
                    temp.append(currentStr);
                }
                currentStr = new StringBuilder(prevStr + temp.toString());
            } else {
                currentStr.append(c);
            }
        }
        
        return currentStr.toString();
    }
    
    /**
     * 基本计算器 II - 栈解法
     * LeetCode 227: https://leetcode.com/problems/basic-calculator-ii/
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int calculate(String s) {
        Stack<Integer> st = new Stack<>();
        int num = 0;
        char sign = '+';
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }
            
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                if (sign == '+') {
                    st.push(num);
                } else if (sign == '-') {
                    st.push(-num);
                } else if (sign == '*') {
                    int top = st.pop();
                    st.push(top * num);
                } else if (sign == '/') {
                    int top = st.pop();
                    st.push(top / num);
                }
                
                sign = c;
                num = 0;
            }
        }
        
        int result = 0;
        while (!st.isEmpty()) {
            result += st.pop();
        }
        
        return result;
    }
    
    /**
     * 最小栈类 - 支持常数时间获取最小元素
     * LeetCode 155: https://leetcode.com/problems/min-stack/
     */
    class MinStack {
        private Stack<Integer> dataStack;
        private Stack<Integer> minStack;
        
        public MinStack() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
        }
        
        public void push(int val) {
            dataStack.push(val);
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }
        
        public void pop() {
            if (dataStack.peek().equals(minStack.peek())) {
                minStack.pop();
            }
            dataStack.pop();
        }
        
        public int top() {
            return dataStack.peek();
        }
        
        public int getMin() {
            return minStack.peek();
        }
    }
    
    /**
     * 用栈实现队列类
     * LeetCode 232: https://leetcode.com/problems/implement-queue-using-stacks/
     */
    class CQueue {
        private Stack<Integer> inStack;
        private Stack<Integer> outStack;
        
        public CQueue() {
            inStack = new Stack<>();
            outStack = new Stack<>();
        }
        
        private void transfer() {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
        
        public void appendTail(int value) {
            inStack.push(value);
        }
        
        public int deleteHead() {
            if (outStack.isEmpty()) {
                if (inStack.isEmpty()) {
                    return -1;
                }
                transfer();
            }
            return outStack.pop();
        }
    }
    
    /**
     * 滑动窗口中位数 - 双堆解法
     * LeetCode 480: https://leetcode.com/problems/sliding-window-median/
     * 时间复杂度：O(n log k)
     * 空间复杂度：O(k)
     */
    public double[] medianSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) return new double[0];
        
        // 使用两个PriorityQueue模拟平衡二叉搜索树
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        double[] medians = new double[nums.length - k + 1];
        
        for (int i = 0; i < nums.length; i++) {
            // 添加新元素
            if (maxHeap.isEmpty() || nums[i] <= maxHeap.peek()) {
                maxHeap.offer(nums[i]);
            } else {
                minHeap.offer(nums[i]);
            }
            
            // 平衡两个堆
            balanceHeaps(maxHeap, minHeap);
            
            // 如果窗口已满，计算中位数并移除旧元素
            if (i >= k - 1) {
                medians[i - k + 1] = getMedian(maxHeap, minHeap);
                
                // 移除窗口最左边的元素
                int toRemove = nums[i - k + 1];
                if (toRemove <= maxHeap.peek()) {
                    maxHeap.remove(toRemove);
                } else {
                    minHeap.remove(toRemove);
                }
                
                // 重新平衡
                balanceHeaps(maxHeap, minHeap);
            }
        }
        
        return medians;
    }
    
    // 辅助方法：平衡两个堆，确保maxHeap的大小等于或比minHeap大1
    private void balanceHeaps(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap) {
        // 如果maxHeap的大小比minHeap大2，将maxHeap的堆顶元素移到minHeap
        while (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        }
        
        // 如果minHeap的大小比maxHeap大，将minHeap的堆顶元素移到maxHeap
        while (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    // 辅助方法：计算中位数
    private double getMedian(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap) {
        if (maxHeap.size() > minHeap.size()) {
            // 窗口大小为奇数，中位数是maxHeap的堆顶元素
            return maxHeap.peek();
        } else {
            // 窗口大小为偶数，中位数是两个堆顶元素的平均值
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }

    // 主函数用于测试所有算法实现
    public static void main(String[] args) {
        ConvertQueueAndStack solution = new ConvertQueueAndStack();
        
        System.out.println("=== 测试柱状图中最大的矩形 ===");
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("输入: [2,1,5,6,2,3]");
        System.out.println("输出: " + solution.largestRectangleAreaOptimized(heights));
        System.out.println("预期输出: 10");
        
        System.out.println("\n=== 测试字符串解码 ===");
        String encoded = "3[a]2[bc]";
        System.out.println("输入: \"3[a]2[bc]\"");
        System.out.println("输出: \"" + solution.decodeString(encoded) + "\"");
        System.out.println("预期输出: \"aaabcbc\"");
        
        System.out.println("\n=== 测试基本计算器 II ===");
        String expression = "3+2*2";
        System.out.println("输入: \"3+2*2\"");
        System.out.println("输出: " + solution.calculate(expression));
        System.out.println("预期输出: 7");
        
        System.out.println("\n=== 测试最小栈 ===");
        ConvertQueueAndStack.MinStack minStack = solution.new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println("输入: push(-2), push(0), push(-3)");
        System.out.println("getMin(): " + minStack.getMin());
        System.out.println("预期: -3");
        minStack.pop();
        System.out.println("pop()");
        System.out.println("top(): " + minStack.top());
        System.out.println("预期: 0");
        System.out.println("getMin(): " + minStack.getMin());
        System.out.println("预期: -2");
        
        System.out.println("\n=== 测试用栈实现队列(CQueue) ===");
        ConvertQueueAndStack.CQueue queue = solution.new CQueue();
        queue.appendTail(3);
        System.out.println("appendTail(3)");
        System.out.println("deleteHead(): " + queue.deleteHead());
        System.out.println("预期: 3");
        System.out.println("deleteHead(): " + queue.deleteHead());
        System.out.println("预期: -1");
        
        System.out.println("\n=== 测试滑动窗口中位数 ===");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.print("输入: [1,3,-1,-3,5,3,6,7], k=3\n输出: [");
        double[] medians = solution.medianSlidingWindow(nums, k);
        for (int i = 0; i < medians.length; i++) {
            System.out.print(medians[i]);
            if (i < medians.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("预期输出: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]");
    }
}