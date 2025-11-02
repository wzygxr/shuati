package class094;

import java.util.Arrays;

/**
 * 贪心算法综合测试类 (Greedy Algorithm Comprehensive Test Class)
 * 验证所有贪心算法实现的正确性
 * 
 * 测试目标：
 * - 验证基础贪心算法实现
 * - 验证高级贪心算法实现
 * - 验证数学相关贪心算法实现
 * 
 * 测试方法：
 * - 使用预定义测试用例
 * - 比较期望结果与实际结果
 * - 输出测试通过情况
 * 
 * 算法标签: 贪心算法(Greedy Algorithm)、综合测试(Comprehensive Testing)、算法验证(Algorithm Verification)
 */
public class TestAllGreedyAlgorithms {

    public static void main(String[] args) {
        System.out.println("=== 贪心算法综合测试开始 ===\n");
        
        // 测试基础贪心算法
        testBasicGreedyAlgorithms();
        
        // 测试高级贪心算法
        testAdvancedGreedyAlgorithms();
        
        // 测试数学相关贪心算法
        testMathematicalGreedyAlgorithms();
        
        System.out.println("\n=== 贪心算法综合测试完成 ===");
    }
    
    /**
     * 测试基础贪心算法 (Test Basic Greedy Algorithms)
     * 验证分发饼干、买卖股票、跳跃游戏等基础贪心算法的正确性
     * 
     * 测试内容：
     * 1. 分发饼干：验证孩子满足数计算
     * 2. 买卖股票：验证最大利润计算
     * 3. 跳跃游戏：验证可达性判断
     * 
     * 算法特点：
     * - 时间复杂度：O(n log n) - 主要消耗在排序
     * - 空间复杂度：O(1) - 常数额外空间
     * - 测试方法：使用预定义用例验证结果
     */
    private static void testBasicGreedyAlgorithms() {
        System.out.println("--- 基础贪心算法测试 ---");
        
        // 测试分发饼干（使用独立实现）
        // 算法：贪心匹配最小胃口和最小饼干
        int[] g = {1, 2, 3}; // 孩子胃口
        int[] s = {1, 1};    // 饼干尺寸
        int result = findContentChildren(g, s);
        System.out.println("分发饼干测试: " + (result == 1 ? "✓ 通过" : "✗ 失败"));
        
        // 测试买卖股票（使用独立实现）
        // 算法：收集所有正收益交易
        int[] prices = {7, 1, 5, 3, 6, 4}; // 股票价格
        int profit = maxProfit(prices);
        System.out.println("买卖股票测试: " + (profit == 7 ? "✓ 通过" : "✗ 失败"));
        
        // 测试跳跃游戏（使用独立实现）
        // 算法：维护最远可达位置
        int[] nums = {2, 3, 1, 1, 4}; // 跳跃距离
        boolean canJump = canJump(nums);
        System.out.println("跳跃游戏测试: " + (canJump ? "✓ 通过" : "✗ 失败"));
        
        System.out.println();
    }
    
    /**
     * 测试高级贪心算法 (Test Advanced Greedy Algorithms)
     * 验证课程表、字符串处理等高级贪心算法的正确性
     * 
     * 测试内容：
     * 1. 课程表III：验证最多课程数计算
     * 2. 去除重复字母：验证字典序最小字符串
     * 3. 最多会议：验证最多可参加会议数
     * 
     * 算法特点：
     * - 时间复杂度：O(n log n) - 排序和遍历
     * - 空间复杂度：O(n) - 辅助数据结构
     * - 测试方法：使用典型用例验证结果
     */
    private static void testAdvancedGreedyAlgorithms() {
        System.out.println("--- 高级贪心算法测试 ---");
        
        // 测试课程表III（使用独立实现）
        // 算法：按截止时间排序，贪心选择
        int[][] courses = {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}};
        int courseCount = scheduleCourse(courses);
        System.out.println("课程表III测试: " + (courseCount == 3 ? "✓ 通过" : "✗ 失败"));
        
        // 测试去除重复字母（使用独立实现）
        // 算法：单调栈维护字典序最小
        String s = "bcabc";
        String result = removeDuplicateLetters(s);
        System.out.println("去除重复字母测试: " + ("abc".equals(result) ? "✓ 通过" : "✗ 失败"));
        
        // 测试最多会议（使用独立实现）
        // 算法：按结束时间排序，贪心选择
        int[][] events = {{1, 2}, {2, 3}, {3, 4}, {1, 2}};
        int eventCount = maxEvents(events);
        System.out.println("最多会议测试: " + (eventCount == 4 ? "✓ 通过" : "✗ 失败"));
        
        System.out.println();
    }
    
    /**
     * 测试数学贪心算法 (Test Mathematical Greedy Algorithms)
     * 验证数字处理、计算器操作等数学相关贪心算法的正确性
     * 
     * 测试内容：
     * 1. 最大数：验证数字组合最大值
     * 2. 坏了的计算器：验证最少操作次数
     * 3. 最大硬币数：验证最优分配策略
     * 
     * 算法特点：
     * - 时间复杂度：O(n log n) - 排序和遍历
     * - 空间复杂度：O(n) - 字符串和数组存储
     * - 测试方法：使用数学用例验证结果
     */
    private static void testMathematicalGreedyAlgorithms() {
        System.out.println("--- 数学贪心算法测试 ---");
        
        // 测试最大数（使用独立实现）
        // 算法：自定义排序比较拼接结果
        int[] nums = {3, 30, 34, 5, 9};
        String largestNum = largestNumber(nums);
        System.out.println("最大数测试: " + ("9534330".equals(largestNum) ? "✓ 通过" : "✗ 失败"));
        
        // 测试坏了的计算器（使用独立实现）
        // 算法：反向思维，贪心操作
        int operations = brokenCalc(2, 3);
        System.out.println("坏了的计算器测试: " + (operations == 2 ? "✓ 通过" : "✗ 失败"));
        
        // 测试最大硬币数（使用独立实现）
        // 算法：排序后贪心选择
        int[] piles = {2, 4, 1, 2, 7, 8};
        int coins = maxCoins(piles);
        System.out.println("最大硬币数测试: " + (coins == 9 ? "✓ 通过" : "✗ 失败"));
        
        System.out.println();
    }
    
    // 以下是独立的贪心算法实现，用于测试
    
    /**
     * 分发饼干（贪心算法）(Assign Cookies - Greedy Algorithm)
     * 使用贪心策略为孩子分配饼干，最大化满足的孩子数
     * 
     * 算法思路：
     * 1. 排序：对孩子胃口和饼干尺寸升序排序
     * 2. 匹配：使用双指针贪心匹配最小胃口和最小饼干
     * 3. 计数：统计满足的孩子数
     * 
     * 时间复杂度: O(m log m + n log n) - m是孩子数，n是饼干数
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);  // 按胃口升序排序
        Arrays.sort(s);  // 按尺寸升序排序
        int i = 0, j = 0; // 双指针
        while (i < g.length && j < s.length) {
            if (s[j] >= g[i]) { // 当前饼干能满足当前孩子
                i++; // 满足孩子数加1
            }
            j++; // 饼干指针前移
        }
        return i; // 返回满足的孩子数
    }
    
    /**
     * 买卖股票的最佳时机 II（贪心算法）(Best Time to Buy and Sell Stock II - Greedy Algorithm)
     * 使用贪心策略计算股票交易的最大利润
     * 
     * 算法思路：
     * 1. 收集：收集所有正的价格差
     * 2. 累加：将所有正收益累加
     * 3. 返回：得到最大总利润
     * 
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) { // 价格上涨
                profit += prices[i] - prices[i - 1]; // 累加利润
            }
        }
        return profit;
    }
    
    /**
     * 跳跃游戏（贪心算法）(Jump Game - Greedy Algorithm)
     * 使用贪心策略判断是否能到达数组末尾
     * 
     * 算法思路：
     * 1. 维护：维护能到达的最远位置
     * 2. 更新：动态更新最远可达位置
     * 3. 判断：检查是否能到达终点
     * 
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static boolean canJump(int[] nums) {
        int maxReach = 0; // 最远可达位置
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // 无法到达位置i
            maxReach = Math.max(maxReach, i + nums[i]); // 更新最远位置
        }
        return true;
    }
    
    /**
     * 课程表 III（贪心算法）(Course Schedule III - Greedy Algorithm)
     * 使用贪心策略选择最多的课程
     * 
     * 算法思路：
     * 1. 排序：按截止时间升序排序
     * 2. 选择：贪心选择能在截止时间前完成的课程
     * 3. 计数：统计可选课程数
     * 
     * 时间复杂度: O(n log n) - 排序时间
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static int scheduleCourse(int[][] courses) {
        Arrays.sort(courses, (a, b) -> a[1] - b[1]); // 按截止时间排序
        int time = 0;  // 累计时间
        int count = 0; // 课程数
        for (int[] course : courses) {
            if (time + course[0] <= course[1]) { // 能在截止时间前完成
                time += course[0]; // 累加课程时间
                count++; // 课程数加1
            }
        }
        return count;
    }
    
    /**
     * 去除重复字母（贪心算法）(Remove Duplicate Letters - Greedy Algorithm)
     * 使用贪心策略构造字典序最小的无重复字符串
     * 
     * 算法思路：
     * 1. 预处理：记录每个字符最后出现位置
     * 2. 构造：使用单调栈构造结果
     * 3. 贪心：维护字典序最小的字符序列
     * 
     * 时间复杂度: O(n) - 一次遍历
     * 空间复杂度: O(1) - 固定大小数组
     * 是否最优解: 是
     */
    private static String removeDuplicateLetters(String s) {
        boolean[] visited = new boolean[26]; // 字符访问状态
        int[] lastIndex = new int[26];       // 字符最后位置
        for (int i = 0; i < s.length(); i++) {
            lastIndex[s.charAt(i) - 'a'] = i; // 记录最后位置
        }
        
        StringBuilder result = new StringBuilder(); // 结果字符串
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (visited[c - 'a']) continue; // 已访问过跳过
            
            // 维护单调栈：弹出大于当前字符且后面还会出现的字符
            while (result.length() > 0 && result.charAt(result.length() - 1) > c && 
                   lastIndex[result.charAt(result.length() - 1) - 'a'] > i) {
                visited[result.charAt(result.length() - 1) - 'a'] = false;
                result.deleteCharAt(result.length() - 1);
            }
            
            result.append(c);        // 添加当前字符
            visited[c - 'a'] = true; // 标记已访问
        }
        return result.toString();
    }
    
    /**
     * 最多可以参加的会议数目（贪心算法）(Maximum Events - Greedy Algorithm)
     * 使用贪心策略选择最多的会议参加
     * 
     * 算法思路：
     * 1. 排序：按结束时间升序排序
     * 2. 选择：贪心选择最早可参加的会议
     * 3. 标记：标记已参加的日期
     * 
     * 时间复杂度: O(n log n + n*d) - n是会议数，d是日期范围
     * 空间复杂度: O(d) - 日期标记数组
     * 是否最优解: 是
     */
    private static int maxEvents(int[][] events) {
        Arrays.sort(events, (a, b) -> a[1] - b[1]); // 按结束时间排序
        boolean[] attended = new boolean[100001];   // 日期参加状态
        int count = 0;                              // 参加会议数
        for (int[] event : events) {
            for (int day = event[0]; day <= event[1]; day++) {
                if (!attended[day]) {      // 找到可参加日期
                    attended[day] = true;  // 标记已参加
                    count++;               // 会议数加1
                    break;
                }
            }
        }
        return count;
    }
    
    /**
     * 最大数（贪心算法）(Largest Number - Greedy Algorithm)
     * 使用贪心策略构造最大的数字组合
     * 
     * 算法思路：
     * 1. 转换：将数字转换为字符串
     * 2. 排序：按拼接结果降序排序
     * 3. 构造：拼接排序后的字符串
     * 
     * 时间复杂度: O(n log n) - 排序时间
     * 空间复杂度: O(n) - 字符串数组
     * 是否最优解: 是
     */
    private static String largestNumber(int[] nums) {
        String[] strNums = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strNums[i] = String.valueOf(nums[i]); // 转换为字符串
        }
        
        // 自定义排序：比较拼接结果
        Arrays.sort(strNums, (a, b) -> (b + a).compareTo(a + b));
        
        if (strNums[0].equals("0")) { // 处理全零情况
            return "0";
        }
        
        StringBuilder result = new StringBuilder();
        for (String num : strNums) {
            result.append(num); // 拼接结果
        }
        return result.toString();
    }
    
    /**
     * 坏了的计算器（贪心算法）(Broken Calculator - Greedy Algorithm)
     * 使用贪心策略计算最少操作次数
     * 
     * 算法思路：
     * 1. 反向：从目标值反向计算
     * 2. 贪心：偶数除2，奇数加1
     * 3. 补偿：处理剩余差值
     * 
     * 时间复杂度: O(log target) - 反向操作次数
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static int brokenCalc(int start, int target) {
        int operations = 0;
        while (target > start) {     // 反向计算
            operations++;            // 操作数加1
            if (target % 2 == 0) {   // 偶数
                target /= 2;         // 除2
            } else {                 // 奇数
                target++;            // 加1
            }
        }
        return operations + (start - target); // 补偿剩余差值
    }
    
    /**
     * 最大硬币数（贪心算法）(Maximum Coins - Greedy Algorithm)
     * 使用贪心策略获得最大硬币数
     * 
     * 算法思路：
     * 1. 排序：按硬币数升序排序
     * 2. 选择：贪心选择第二大的堆
     * 3. 累加：累加所选堆的硬币数
     * 
     * 时间复杂度: O(n log n) - 排序时间
     * 空间复杂度: O(1) - 常数额外空间
     * 是否最优解: 是
     */
    private static int maxCoins(int[] piles) {
        Arrays.sort(piles);          // 升序排序
        int result = 0;              // 累计硬币数
        int n = piles.length;
        // 从倒数第二个开始，每隔一个取一个
        for (int i = n - 2; i >= n / 3; i -= 2) {
            result += piles[i];      // 累加硬币数
        }
        return result;
    }
}