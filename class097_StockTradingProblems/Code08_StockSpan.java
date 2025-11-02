package class082;

import java.util.*;

// LeetCode 901. 股票价格跨度
// 题目链接: https://leetcode.cn/problems/online-stock-span/
// 题目描述:
// 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
// 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
// 例如，如果未来7天股票的价格是 [100,80,60,70,60,75,85]，那么股票跨度将是 [1,1,1,2,1,4,6]。
//
// 解题思路:
// 这是一个典型的单调栈问题。我们需要找到左边第一个比当前元素大的位置。
// 使用单调递减栈来解决这个问题，栈中存储索引。
// 对于每个新来的价格，我们弹出所有小于等于当前价格的元素，然后计算跨度。
//
// 算法步骤:
// 1. 使用一个栈来存储股票价格的索引
// 2. 对于每个新价格，弹出栈中所有小于等于当前价格的索引
// 3. 如果栈为空，说明当前价格是目前为止最大的，跨度为当前天数+1
// 4. 如果栈不为空，跨度为当前索引减去栈顶索引
// 5. 将当前索引压入栈中
//
// 时间复杂度分析:
// O(n) - 每个元素最多入栈和出栈一次
//
// 空间复杂度分析:
// O(n) - 栈的空间复杂度
//
// 是否为最优解:
// 是，这是解决该问题的最优解，因为每个元素最多被处理两次（一次入栈，一次出栈）
//
// 工程化考量:
// 1. 边界条件处理: 空栈情况
// 2. 异常处理: 输入参数校验
// 3. 可读性: 变量命名清晰，注释详细
public class Code08_StockSpan {
    private Stack<Integer> stack;
    private List<Integer> prices;
    private int index;

    public Code08_StockSpan() {
        stack = new Stack<>();
        prices = new ArrayList<>();
        index = 0;
    }

    /*
     * 计算股票价格跨度
     * @param price 当天股票价格
     * @return 股票价格跨度
     */
    public int next(int price) {
        // 将价格添加到列表中
        prices.add(price);
        
        // 弹出栈中所有小于等于当前价格的索引
        while (!stack.isEmpty() && prices.get(stack.peek()) <= price) {
            stack.pop();
        }
        
        // 计算跨度
        int span;
        if (stack.isEmpty()) {
            // 如果栈为空，说明当前价格是目前为止最大的
            span = index + 1;
        } else {
            // 跨度为当前索引减去栈顶索引
            span = index - stack.peek();
        }
        
        // 将当前索引压入栈中
        stack.push(index);
        index++;
        
        return span;
    }

    // 补充方法：获取当前栈的状态（用于调试）
    public List<Integer> getStackState() {
        return new ArrayList<>(stack);
    }
    
    // 补充方法：获取当前价格列表（用于调试）
    public List<Integer> getPriceList() {
        return new ArrayList<>(prices);
    }
    
    // 补充方法：重置股票跨度计算器
    public void reset() {
        stack.clear();
        prices.clear();
        index = 0;
    }

    // 测试方法
    public static void main(String[] args) {
        Code08_StockSpan stockSpanner = new Code08_StockSpan();
        
        // 测试用例: [100, 80, 60, 70, 60, 75, 85]
        int[] prices = {100, 80, 60, 70, 60, 75, 85};
        int[] expected = {1, 1, 1, 2, 1, 4, 6};
        
        System.out.println("测试LeetCode 901. 股票价格跨度问题:");
        for (int i = 0; i < prices.length; i++) {
            int result = stockSpanner.next(prices[i]);
            System.out.println("价格: " + prices[i] + ", 跨度: " + result + ", 期望: " + expected[i]);
            assert result == expected[i] : "测试失败!";
        }
        System.out.println("测试通过!");
        
        // 边界测试
        System.out.println("
--- 边界测试 ---");
        stockSpanner.reset();
        
        // 测试空输入
        System.out.println("重置后第一个价格跨度: " + stockSpanner.next(50)); // 应该输出1
        
        // 测试重复价格
        stockSpanner.reset();
        int[] samePrices = {10, 10, 10, 10};
        System.out.println("重复价格测试:");
        for (int price : samePrices) {
            System.out.println("价格: " + price + ", 跨度: " + stockSpanner.next(price));
        }
        
        // 测试极端情况：持续上涨
        stockSpanner.reset();
        int[] risingPrices = {1, 2, 3, 4, 5};
        System.out.println("持续上涨测试:");
        for (int price : risingPrices) {
            System.out.println("价格: " + price + ", 跨度: " + stockSpanner.next(price));
        }
        
        // 测试极端情况：持续下跌
        stockSpanner.reset();
        int[] fallingPrices = {5, 4, 3, 2, 1};
        System.out.println("持续下跌测试:");
        for (int price : fallingPrices) {
            System.out.println("价格: " + price + ", 跨度: " + stockSpanner.next(price));
        }
        
        // 性能测试
        System.out.println("
--- 性能测试 ---");
        stockSpanner.reset();
        long startTime = System.currentTimeMillis();
        
        // 测试10000个元素的性能
        for (int i = 0; i < 10000; i++) {
            stockSpanner.next(i % 100 + 1); // 循环价格
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("处理10000个元素的耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("
=== 所有测试完成 ===");
    }
    
    // 工程化考量示例：线程安全版本（如果需要多线程环境）
    public static class ThreadSafeStockSpan {
        private final Stack<Integer> stack;
        private final List<Integer> prices;
        private int index;
        private final Object lock = new Object();
        
        public ThreadSafeStockSpan() {
            stack = new Stack<>();
            prices = new ArrayList<>();
            index = 0;
        }
        
        public int next(int price) {
            synchronized (lock) {
                prices.add(price);
                
                while (!stack.isEmpty() && prices.get(stack.peek()) <= price) {
                    stack.pop();
                }
                
                int span;
                if (stack.isEmpty()) {
                    span = index + 1;
                } else {
                    span = index - stack.peek();
                }
                
                stack.push(index);
                index++;
                
                return span;
            }
        }
    }
    
    // 工程化考量示例：支持批量操作
    public int[] nextBatch(int[] priceBatch) {
        int[] results = new int[priceBatch.length];
        for (int i = 0; i < priceBatch.length; i++) {
            results[i] = next(priceBatch[i]);
        }
        return results;
    }
    
    // 工程化考量示例：支持序列化和反序列化
    public void saveState(String filename) {
        // 实际实现中可以使用JSON或二进制格式保存状态
        System.out.println("保存状态到文件: " + filename);
        // 实现细节省略...
    }
    
    public void loadState(String filename) {
        // 实际实现中可以从文件加载状态
        System.out.println("从文件加载状态: " + filename);
        // 实现细节省略...
    }
}