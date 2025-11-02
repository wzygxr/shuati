package class063;

import java.util.*;

// 世界冰球锦标赛
// 题目来源：洛谷 P4799 (CEOI2015 Day2)
// 题目描述：
// 有 n 场比赛，第 i 场比赛的门票价格为 a_i。Bobek 有 m 元钱，问他有多少种不同的观赛方案。
// 方案可以是空方案，但不能超过他的钱数。
// 测试链接 : https://www.luogu.com.cn/problem/P4799
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后对其中一半进行排序，通过二分查找找到符合条件的组合数目
// 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(2^(n/2) * n)
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查输入是否合法
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用ArrayList存储子集和，使用Arrays.sort进行排序，使用Collections.binarySearch进行二分查找

public class Code07_WorldIceHockeyChampionship {
    
    /**
     * 计算Bobek可以选择的不同观赛方案数目
     * @param prices 各场比赛的门票价格数组
     * @param money Bobek拥有的钱数
     * @return 不同的观赛方案数目
     */
    public static long countWays(long[] prices, long money) {
        // 边界条件检查
        if (prices == null || prices.length == 0) {
            return 1; // 只有空方案一种
        }
        
        int n = prices.length;
        int mid = n / 2;
        
        // 分别存储左右两部分的所有可能子集和
        List<Long> leftSums = new ArrayList<>();
        List<Long> rightSums = new ArrayList<>();
        
        // 计算左半部分的所有可能子集和
        generateSubsetSums(prices, 0, mid - 1, 0, leftSums, money);
        
        // 计算右半部分的所有可能子集和
        generateSubsetSums(prices, mid, n - 1, 0, rightSums, money);
        
        // 对右半部分的子集和进行排序，以便进行二分查找
        Collections.sort(rightSums);
        
        // 统计符合条件的组合数目
        long count = 0;
        for (long leftSum : leftSums) {
            // 查找右半部分中不超过(money - leftSum)的最大子集和的位置
            long remaining = money - leftSum;
            if (remaining < 0) {
                continue;
            }
            
            // 二分查找找到第一个大于remaining的位置
            int index = Collections.binarySearch(rightSums, remaining + 1);
            if (index < 0) {
                // 如果没找到，index是-(insertion point) - 1
                index = -index - 1;
            }
            
            // 所有小于等于remaining的子集和都符合条件
            count += index;
        }
        
        return count;
    }
    
    /**
     * 递归生成指定范围内所有可能的子集和
     * @param prices 门票价格数组
     * @param start 起始索引
     * @param end 结束索引
     * @param currentSum 当前累积和
     * @param sums 存储结果的列表
     * @param money 最大允许的钱数
     */
    private static void generateSubsetSums(long[] prices, int start, int end, long currentSum, List<Long> sums, long money) {
        // 递归终止条件
        if (start > end) {
            sums.add(currentSum);
            return;
        }
        
        // 剪枝：如果当前和已经超过了money，就不再继续搜索
        if (currentSum > money) {
            return;
        }
        
        // 不选择当前比赛
        generateSubsetSums(prices, start + 1, end, currentSum, sums, money);
        
        // 剪枝：如果选择当前比赛后会超过money，就不再选择
        if (currentSum + prices[start] <= money) {
            // 选择当前比赛
            generateSubsetSums(prices, start + 1, end, currentSum + prices[start], sums, money);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 读取输入
        System.out.println("请输入比赛场次n和Bobek的钱数m：");
        int n = scanner.nextInt();
        long m = scanner.nextLong();
        
        long[] prices = new long[n];
        System.out.println("请输入每场比赛的门票价格：");
        for (int i = 0; i < n; i++) {
            prices[i] = scanner.nextLong();
        }
        
        // 计算结果
        long result = countWays(prices, m);
        System.out.println("不同的观赛方案数目：" + result);
        
        // 测试用例1
        System.out.println("\n测试用例1：");
        long[] prices1 = {1, 2, 3, 4};
        long money1 = 5;
        System.out.println("比赛门票价格：[1, 2, 3, 4]");
        System.out.println("Bobek的钱数：5");
        System.out.println("期望输出：7"); // 空方案, {1}, {2}, {3}, {4}, {1,2}, {1,3}
        System.out.println("实际输出：" + countWays(prices1, money1));
        
        // 测试用例2
        System.out.println("\n测试用例2：");
        long[] prices2 = {1000000000, 1000000000, 1000000000};
        long money2 = 1000000000;
        System.out.println("比赛门票价格：[1000000000, 1000000000, 1000000000]");
        System.out.println("Bobek的钱数：1000000000");
        System.out.println("期望输出：4"); // 空方案, {1000000000}, {1000000000}, {1000000000}
        System.out.println("实际输出：" + countWays(prices2, money2));
        
        scanner.close();
    }
}