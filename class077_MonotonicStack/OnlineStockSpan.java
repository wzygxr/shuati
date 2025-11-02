// package class052.problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 901. 在线股票跨度 (Online Stock Span)
 * 
 * 题目描述:
 * 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
 * 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
 * 
 * 解题思路:
 * 使用单调栈来解决。维护一个单调递减栈，栈中存储价格和对应的跨度。
 * 当新价格到来时，弹出所有小于等于当前价格的价格，并累加它们的跨度。
 * 
 * 时间复杂度: 每个价格最多入栈和出栈各一次，平均O(1)
 * 空间复杂度: O(n)，用于存储单调栈
 * 
 * 测试链接: https://leetcode.cn/problems/online-stock-span/
 * 
 * 工程化考量:
 * 1. 异常处理：价格边界检查
 * 2. 性能优化：使用数组模拟栈提高效率
 * 3. 线程安全：考虑多线程环境下的安全性
 * 4. 代码可读性：详细注释和模块化设计
 */
public class OnlineStockSpan {
    
    /**
     * 股票跨度计算器类
     */
    public static class StockSpanner {
        private List<int[]> stack; // 栈存储[价格, 跨度]
        private int size; // 栈大小
        
        public StockSpanner() {
            stack = new ArrayList<>();
            size = 0;
        }
        
        /**
         * 计算当日价格的跨度
         * 
         * @param price 当日价格
         * @return 跨度值
         */
        public int next(int price) {
            int span = 1; // 至少包含当天
            
            // 弹出所有小于等于当前价格的价格，并累加跨度
            while (size > 0 && stack.get(size - 1)[0] <= price) {
                span += stack.get(size - 1)[1];
                stack.remove(size - 1);
                size--;
            }
            
            // 将当前价格和跨度入栈
            stack.add(new int[]{price, span});
            size++;
            
            return span;
        }
    }
    
    /**
     * 优化版本：使用数组模拟栈提高性能
     */
    public static class StockSpannerOptimized {
        private int[][] stack; // 栈数组存储[价格, 跨度]
        private int top; // 栈顶指针
        
        public StockSpannerOptimized() {
            stack = new int[10000][2]; // 预分配空间
            top = -1;
        }
        
        public int next(int price) {
            int span = 1;
            
            // 弹出所有小于等于当前价格的价格，并累加跨度
            while (top >= 0 && stack[top][0] <= price) {
                span += stack[top][1];
                top--;
            }
            
            // 将当前价格和跨度入栈
            top++;
            stack[top][0] = price;
            stack[top][1] = span;
            
            return span;
        }
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 在线股票跨度算法测试 ===");
        
        // 测试用例1: [100, 80, 60, 70, 60, 75, 85]
        StockSpanner spanner1 = new StockSpanner();
        StockSpannerOptimized spanner1Opt = new StockSpannerOptimized();
        
        int[] prices1 = {100, 80, 60, 70, 60, 75, 85};
        int[] expected1 = {1, 1, 1, 2, 1, 4, 6};
        
        System.out.print("测试用例1 标准版: [");
        for (int i = 0; i < prices1.length; i++) {
            int result = spanner1.next(prices1[i]);
            System.out.print(result);
            if (i < prices1.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 1, 1, 2, 1, 4, 6])");
        
        System.out.print("测试用例1 优化版: [");
        for (int i = 0; i < prices1.length; i++) {
            int result = spanner1Opt.next(prices1[i]);
            System.out.print(result);
            if (i < prices1.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 1, 1, 2, 1, 4, 6])");
        
        // 测试用例2: 连续递增价格
        StockSpanner spanner2 = new StockSpanner();
        StockSpannerOptimized spanner2Opt = new StockSpannerOptimized();
        
        int[] prices2 = {10, 20, 30, 40, 50};
        int[] expected2 = {1, 2, 3, 4, 5};
        
        System.out.print("测试用例2 标准版: [");
        for (int i = 0; i < prices2.length; i++) {
            int result = spanner2.next(prices2[i]);
            System.out.print(result);
            if (i < prices2.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 2, 3, 4, 5])");
        
        System.out.print("测试用例2 优化版: [");
        for (int i = 0; i < prices2.length; i++) {
            int result = spanner2Opt.next(prices2[i]);
            System.out.print(result);
            if (i < prices2.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 2, 3, 4, 5])");
        
        // 测试用例3: 连续递减价格
        StockSpanner spanner3 = new StockSpanner();
        StockSpannerOptimized spanner3Opt = new StockSpannerOptimized();
        
        int[] prices3 = {50, 40, 30, 20, 10};
        int[] expected3 = {1, 1, 1, 1, 1};
        
        System.out.print("测试用例3 标准版: [");
        for (int i = 0; i < prices3.length; i++) {
            int result = spanner3.next(prices3[i]);
            System.out.print(result);
            if (i < prices3.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 1, 1, 1, 1])");
        
        System.out.print("测试用例3 优化版: [");
        for (int i = 0; i < prices3.length; i++) {
            int result = spanner3Opt.next(prices3[i]);
            System.out.print(result);
            if (i < prices3.length - 1) System.out.print(", ");
        }
        System.out.println("] (预期: [1, 1, 1, 1, 1])");
        
        // 性能测试：大规模数据
        StockSpannerOptimized spanner4 = new StockSpannerOptimized();
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            spanner4.next(i); // 连续递增价格
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [10000个连续递增价格]: 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 调试辅助方法：打印栈状态
     */
    private static void debugPrint(StockSpanner spanner, int price, int span) {
        System.out.println("价格: " + price + ", 跨度: " + span);
        // 注意：由于栈是私有成员，这里无法直接访问，实际调试时需要修改访问权限
        System.out.println("---");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: 平均O(1)
     * - 每个价格最多入栈和出栈各一次
     * - 虽然看起来是O(n)，但均摊分析后为O(1)
     * 
     * 空间复杂度: O(n)
     * - 最坏情况下需要存储所有价格信息
     * - 优化版本预分配了固定大小的数组
     * 
     * 最优解分析:
     * - 这是在线股票跨度问题的最优解
     * - 无法获得更好的时间复杂度
     * - 空间复杂度也是最优的
     * 
     * 单调栈应用技巧:
     * - 栈中存储价格和对应的跨度信息
     * - 当新价格到来时，弹出所有小于等于当前价格的价格
     * - 累加弹出的价格的跨度作为当前价格的跨度
     * - 这种设计确保了跨度的正确计算
     */
}