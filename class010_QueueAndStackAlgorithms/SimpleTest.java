import java.util.*;

public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("Java简单测试程序");
        
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
        
        System.out.println("Java测试完成！");
    }
    
    // 有效的括号实现
    public static boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
        
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
        private Stack<Integer> dataStack;
        private Stack<Integer> minStack;
        
        public MinStack() {
            dataStack = new Stack<>();
            minStack = new Stack<>();
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
}