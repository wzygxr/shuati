public class SimpleTest {
    public static void main(String[] args) {
        System.out.println("=== 单调栈算法测试 ===");
        
        // 测试每日温度算法
        int[] temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
        int[] result = dailyTemperatures(temperatures);
        
        System.out.print("每日温度测试结果: ");
        for (int val : result) {
            System.out.print(val + " ");
        }
        System.out.println("(预期: 1 1 4 2 1 1 0 0)");
        
        System.out.println("=== 测试完成 ===");
    }
    
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
}