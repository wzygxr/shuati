package class086;

// LeetCode 1449. 数位成本和为目标值的最大数字
// 给你一个整数数组 cost 和一个整数 target 。
// 请你返回满足如下规则可以得到的 最大 整数：
// 给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
// 总成本必须恰好等于 target 。
// 添加的数位中没有数字 0 。
// 由于答案可能会很大，请你以字符串形式返回。
// 如果按照上述要求无法得到任何整数，请你返回 "0" 。
// 测试链接 : https://leetcode.cn/problems/form-largest-integer-with-digits-that-add-up-to-target/

public class LeetCode1449_Form_Largest_Integer_With_Digits_That_Add_Up_To_Target {
    
    /*
     * 算法详解：数位成本和为目标值的最大数字（LeetCode 1449）
     * 
     * 问题描述：
     * 给你一个整数数组 cost 和一个整数 target 。
     * 请你返回满足如下规则可以得到的 最大 整数：
     * 给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
     * 总成本必须恰好等于 target 。
     * 添加的数位中没有数字 0 。
     * 由于答案可能会很大，请你以字符串形式返回。
     * 如果按照上述要求无法得到任何整数，请你返回 "0" 。
     * 
     * 算法思路：
     * 这是一个完全背包问题的变种。
     * 1. 每个数字（1-9）都有一个成本
     * 2. 背包容量为target，要求恰好装满
     * 3. 目标是构造最大的数字（位数最多，相同位数时字典序最大）
     * 
     * 时间复杂度分析：
     * 1. 动态规划：O(9 * target)
     * 2. 构造结果：O(target)
     * 3. 总体时间复杂度：O(target)
     * 
     * 空间复杂度分析：
     * 1. dp数组：O(target)
     * 2. 总体空间复杂度：O(target)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数是否有效
     * 2. 边界处理：处理target为0和无法构造的情况
     * 3. 字符串处理：正确构造最大数字
     * 
     * 极端场景验证：
     * 1. target为0的情况
     * 2. 所有成本都大于target的情况
     * 3. 所有成本都等于target的情况
     * 4. 成本数组包含重复值的情况
     */
    
    public static String largestNumber(int[] cost, int target) {
        // 异常处理：检查输入参数是否有效
        if (cost == null || cost.length != 9 || target < 0) {
            return "0";
        }
        
        // dp[i] 表示成本恰好为i时能构造的最大数字长度
        // -1 表示无法构造
        int[] dp = new int[target + 1];
        // 初始化：除了dp[0]外，其他都设为-1
        for (int i = 1; i <= target; i++) {
            dp[i] = -1;
        }
        dp[0] = 0; // 成本为0时，构造空字符串，长度为0
        
        // 完全背包：遍历每个数字（1-9）
        for (int i = 0; i < 9; i++) {
            int digit = i + 1; // 数字1-9
            int c = cost[i];   // 对应的成本
            
            // 从小到大遍历成本，因为是完全背包
            for (int j = c; j <= target; j++) {
                // 如果成本j-c可以构造
                if (dp[j - c] != -1) {
                    // 更新dp[j]：选择能构造更长数字的方案
                    dp[j] = Math.max(dp[j], dp[j - c] + 1);
                }
            }
        }
        
        // 如果无法构造成本恰好为target的数字
        if (dp[target] == -1) {
            return "0";
        }
        
        // 构造最大数字
        StringBuilder result = new StringBuilder();
        int remaining = target;
        
        // 从数字9开始往下构造，保证字典序最大
        for (int digit = 9; digit >= 1; digit--) {
            int c = cost[digit - 1];
            
            // 贪心地尽可能多地选择当前数字
            while (remaining >= c && dp[remaining] == dp[remaining - c] + 1) {
                result.append(digit);
                remaining -= c;
            }
        }
        
        return result.toString();
    }
    
    // 另一种实现方式：使用字符串DP
    public static String largestNumberAlternative(int[] cost, int target) {
        // 异常处理：检查输入参数是否有效
        if (cost == null || cost.length != 9 || target < 0) {
            return "0";
        }
        
        // dp[i] 表示成本恰好为i时能构造的最大数字字符串
        // "" 表示无法构造
        String[] dp = new String[target + 1];
        // 初始化：除了dp[0]外，其他都设为null
        for (int i = 1; i <= target; i++) {
            dp[i] = null;
        }
        dp[0] = ""; // 成本为0时，构造空字符串
        
        // 完全背包：遍历每个数字（1-9）
        for (int i = 0; i < 9; i++) {
            int digit = i + 1; // 数字1-9
            int c = cost[i];   // 对应的成本
            
            // 从小到大遍历成本，因为是完全背包
            for (int j = c; j <= target; j++) {
                // 如果成本j-c可以构造
                if (dp[j - c] != null) {
                    // 构造新字符串：将当前数字放在最前面
                    String newStr = digit + dp[j - c];
                    
                    // 更新dp[j]：选择字典序更大的字符串
                    if (dp[j] == null || compareStrings(newStr, dp[j]) > 0) {
                        dp[j] = newStr;
                    }
                }
            }
        }
        
        // 如果无法构造成本恰好为target的数字
        return dp[target] == null ? "0" : dp[target];
    }
    
    // 比较两个数字字符串的大小
    private static int compareStrings(String s1, String s2) {
        // 首先比较长度
        if (s1.length() != s2.length()) {
            return s1.length() - s2.length();
        }
        // 长度相同时比较字典序
        return s1.compareTo(s2);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] cost1 = {4,3,2,5,6,7,2,5,5};
        int target1 = 9;
        System.out.println("Test 1: " + largestNumber(cost1, target1));
        System.out.println("Test 1 (Alternative): " + largestNumberAlternative(cost1, target1));
        // 期望输出: "7772"
        
        // 测试用例2
        int[] cost2 = {7,6,5,5,5,6,8,7,8};
        int target2 = 12;
        System.out.println("Test 2: " + largestNumber(cost2, target2));
        System.out.println("Test 2 (Alternative): " + largestNumberAlternative(cost2, target2));
        // 期望输出: "85"
        
        // 测试用例3
        int[] cost3 = {2,4,6,2,4,6,4,4,4};
        int target3 = 5;
        System.out.println("Test 3: " + largestNumber(cost3, target3));
        System.out.println("Test 3 (Alternative): " + largestNumberAlternative(cost3, target3));
        // 期望输出: "0"
        
        // 测试用例4
        int[] cost4 = {6,10,15,40,40,40,40,40,40};
        int target4 = 47;
        System.out.println("Test 4: " + largestNumber(cost4, target4));
        System.out.println("Test 4 (Alternative): " + largestNumberAlternative(cost4, target4));
        // 期望输出: "32211"
        
        // 测试用例5
        int[] cost5 = {1,1,1,1,1,1,1,1,1};
        int target5 = 0;
        System.out.println("Test 5: " + largestNumber(cost5, target5));
        System.out.println("Test 5 (Alternative): " + largestNumberAlternative(cost5, target5));
        // 期望输出: "0"
    }
}