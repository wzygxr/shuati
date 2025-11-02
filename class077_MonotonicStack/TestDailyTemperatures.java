/**
 * 测试文件 - 不包含包声明，直接测试算法逻辑
 */
public class TestDailyTemperatures {
    
    public static int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        int[] stack = new int[n];
        int top = -1;
        
        for (int i = 0; i < n; i++) {
            while (top >= 0 && temperatures[stack[top]] < temperatures[i]) {
                int index = stack[top--];
                answer[index] = i - index;
            }
            stack[++top] = i;
        }
        
        return answer;
    }
    
    public static void main(String[] args) {
        System.out.println("=== Daily Temperatures 算法测试 ===");
        
        // 测试用例1
        int[] temperatures1 = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] result1 = dailyTemperatures(temperatures1);
        System.out.print("测试用例1输出: ");
        for (int val : result1) {
            System.out.print(val + " ");
        }
        System.out.println("(预期: 1 1 4 2 1 1 0 0)");
        
        // 测试用例2
        int[] temperatures2 = {30, 40, 50, 60};
        int[] result2 = dailyTemperatures(temperatures2);
        System.out.print("测试用例2输出: ");
        for (int val : result2) {
            System.out.print(val + " ");
        }
        System.out.println("(预期: 1 1 1 0)");
        
        System.out.println("=== 测试完成 ===");
    }
}