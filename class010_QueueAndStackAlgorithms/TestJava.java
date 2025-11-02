import java.util.Arrays;

public class TestJava {
    public static void main(String[] args) {
        // 测试有效的括号
        String s = "()[]{}";
        System.out.println("有效的括号测试结果: " + isValid(s));
        
        // 测试最小栈
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println("最小栈最小值: " + minStack.getMin());
        minStack.pop();
        System.out.println("最小栈栈顶: " + minStack.top());
        System.out.println("最小栈最小值: " + minStack.getMin());
        
        // 测试132模式
        int[] nums132 = {3, 1, 4, 2};
        System.out.println("132模式测试结果: " + find132pattern(nums132));
        
        // 测试去除重复字母
        String duplicateStr = "bcabc";
        System.out.println("去除重复字母结果: " + removeDuplicateLetters(duplicateStr));
        
        System.out.println("Java测试完成！");
    }
    
    // 有效的括号实现
    public static boolean isValid(String s) {
        java.util.Stack<Character> st = new java.util.Stack<>();
        
        for (char c : s.toCharArray()) {
            if (c == '(') {
                st.push(')');
            } else if (c == '[') {
                st.push(']');
            } else if (c == '{') {
                st.push('}');
            } else {
                if (st.empty() || st.peek() != c) {
                    return false;
                }
                st.pop();
            }
        }
        
        return st.empty();
    }
    
    // 最小栈实现
    static class MinStack {
        private java.util.Stack<Integer> dataStack;
        private java.util.Stack<Integer> minStack;
        
        public MinStack() {
            dataStack = new java.util.Stack<>();
            minStack = new java.util.Stack<>();
            minStack.push(Integer.MAX_VALUE);
        }
        
        public void push(int val) {
            dataStack.push(val);
            minStack.push(Math.min(val, minStack.peek()));
        }
        
        public void pop() {
            dataStack.pop();
            minStack.pop();
        }
        
        public int top() {
            return dataStack.peek();
        }
        
        public int getMin() {
            return minStack.peek();
        }
    }
    
    // 132模式实现
    public static boolean find132pattern(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;
        
        java.util.Stack<Integer> st = new java.util.Stack<>();
        int second = Integer.MIN_VALUE;
        
        for (int i = n - 1; i >= 0; i--) {
            if (nums[i] < second) return true;
            
            while (!st.empty() && nums[i] > st.peek()) {
                second = st.pop();
            }
            
            st.push(nums[i]);
        }
        
        return false;
    }
    
    // 去除重复字母实现
    public static String removeDuplicateLetters(String s) {
        java.util.Stack<Character> st = new java.util.Stack<>();
        boolean[] inStack = new boolean[256];
        int[] count = new int[256];
        
        for (char c : s.toCharArray()) {
            count[c]++;
        }
        
        for (char c : s.toCharArray()) {
            count[c]--;
            
            if (inStack[c]) continue;
            
            while (!st.empty() && c < st.peek() && count[st.peek()] > 0) {
                inStack[st.pop()] = false;
            }
            
            st.push(c);
            inStack[c] = true;
        }
        
        StringBuilder result = new StringBuilder();
        while (!st.empty()) {
            result.insert(0, st.pop());
        }
        
        return result.toString();
    }
}