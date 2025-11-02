package class185.difference_array_problems;

import java.util.*;

/**
 * LeetCode 1094. 拼车 (Car Pooling)
 * 
 * 题目来源：https://leetcode.cn/problems/car-pooling/
 * 
 * 题目描述：
 * 假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来载客。
 * 由于道路的限制，车只能向一个方向行驶（也就是说，不允许掉头或改变方向，可以将其想象为一个向量）。
 * 给定整数 capacity 和一个数组 trips，
 * trip[i] = [numPassengersi, fromi, toi] 表示第 i 次旅行有 numPassengersi 乘客，
 * 接他们以及放下他们的位置分别是 fromi 和 toi。这些位置是从汽车的初始位置向东的公里数。
 * 当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则请返回 false。
 * 
 * 示例 1：
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 4
 * 输出：false
 * 
 * 示例 2：
 * 输入：trips = [[2,1,5],[3,3,7]], capacity = 5
 * 输出：true
 * 
 * 提示：
 * 1 <= trips.length <= 1000
 * trips[i].length == 3
 * 1 <= numPassengersi <= 100
 * 0 <= fromi < toi <= 1000
 * 1 <= capacity <= 10^5
 * 
 * 解题思路：
 * 使用差分数组解决拼车问题。核心思想是：
 * 1. 将每个行程的上下车位置转换为事件点
 * 2. 使用差分数组记录每个位置乘客数量的变化
 * 3. 计算差分数组的前缀和，得到每个位置的乘客数量
 * 4. 检查是否有位置的乘客数量超过容量
 * 
 * 时间复杂度：O(n + m)，其中 n 是行程数量，m 是最大位置
 * 空间复杂度：O(m)
 * 
 * 相关题目：
 * - LeetCode 370. 区间加法
 * - LeetCode 1109. 航班预订统计
 * - LeetCode 253. 会议室II
 */
public class LeetCode_1094_CarPooling {
    
    /**
     * 使用差分数组解决拼车问题
     * @param trips 行程数组，每个行程是 [乘客数, 上车位置, 下车位置]
     * @param capacity 车辆容量
     * @return 是否能完成所有行程
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        if (trips == null || trips.length == 0) {
            return true;
        }
        
        // 找到最大位置
        int maxLocation = 0;
        for (int[] trip : trips) {
            maxLocation = Math.max(maxLocation, trip[2]);
        }
        
        // 创建差分数组，大小为最大位置 + 1
        int[] diff = new int[maxLocation + 1];
        
        // 处理每个行程
        for (int[] trip : trips) {
            int passengers = trip[0];
            int from = trip[1];
            int to = trip[2];
            
            // 在差分数组中标记乘客变化
            diff[from] += passengers;  // 上车位置增加乘客
            if (to < maxLocation) {
                diff[to] -= passengers;  // 下车位置减少乘客
            }
        }
        
        // 计算每个位置的乘客数量
        int currentPassengers = 0;
        for (int i = 0; i <= maxLocation; i++) {
            currentPassengers += diff[i];
            // 如果某个位置的乘客数量超过容量，返回false
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 暴力解法（用于对比）
     * 时间复杂度：O(n * m)，其中 n 是行程数量，m 是最大位置
     */
    public static boolean carPoolingBruteForce(int[][] trips, int capacity) {
        if (trips == null || trips.length == 0) {
            return true;
        }
        
        // 找到最大位置
        int maxLocation = 0;
        for (int[] trip : trips) {
            maxLocation = Math.max(maxLocation, trip[2]);
        }
        
        // 创建乘客数量数组
        int[] passengers = new int[maxLocation + 1];
        
        // 处理每个行程
        for (int[] trip : trips) {
            int numPassengers = trip[0];
            int from = trip[1];
            int to = trip[2];
            
            // 直接更新每个位置的乘客数量
            for (int i = from; i < to; i++) {
                passengers[i] += numPassengers;
                if (passengers[i] > capacity) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 测试拼车问题解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 1094. 拼车 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] trips1 = {{2, 1, 5}, {3, 3, 7}};
        int capacity1 = 4;
        boolean result1 = carPooling(trips1, capacity1);
        System.out.println("行程: " + Arrays.deepToString(trips1));
        System.out.println("容量: " + capacity1);
        System.out.println("差分数组结果: " + result1);
        System.out.println("暴力解法结果: " + carPoolingBruteForce(trips1, capacity1));
        System.out.println("期望: false");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] trips2 = {{2, 1, 5}, {3, 3, 7}};
        int capacity2 = 5;
        boolean result2 = carPooling(trips2, capacity2);
        System.out.println("行程: " + Arrays.deepToString(trips2));
        System.out.println("容量: " + capacity2);
        System.out.println("差分数组结果: " + result2);
        System.out.println("暴力解法结果: " + carPoolingBruteForce(trips2, capacity2));
        System.out.println("期望: true");
        System.out.println();
        
        // 测试用例3
        System.out.println("测试用例3:");
        int[][] trips3 = {{3, 2, 7}, {3, 7, 9}, {8, 3, 9}};
        int capacity3 = 11;
        boolean result3 = carPooling(trips3, capacity3);
        System.out.println("行程: " + Arrays.deepToString(trips3));
        System.out.println("容量: " + capacity3);
        System.out.println("差分数组结果: " + result3);
        System.out.println("暴力解法结果: " + carPoolingBruteForce(trips3, capacity3));
        System.out.println("期望: true");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 1000;
        int maxLoc = 1000;
        int[][] trips = new int[n][3];
        
        for (int i = 0; i < n; i++) {
            int from = random.nextInt(maxLoc - 1);
            int to = from + random.nextInt(maxLoc - from) + 1;
            int passengers = random.nextInt(10) + 1;
            trips[i] = new int[]{passengers, from, to};
        }
        
        int capacity = 50;
        
        long startTime = System.nanoTime();
        boolean diffResult = carPooling(trips, capacity);
        long endTime = System.nanoTime();
        System.out.println("差分数组法处理" + n + "个行程时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        boolean bruteResult = carPoolingBruteForce(trips, capacity);
        endTime = System.nanoTime();
        System.out.println("暴力解法处理" + n + "个行程时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        System.out.println("两种方法结果是否一致: " + (diffResult == bruteResult));
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 边界处理：处理空输入和位置边界");
        System.out.println("2. 性能优化：差分数组将O(n*m)优化为O(n+m)");
        System.out.println("3. 内存优化：只存储必要的差分数组");
        System.out.println("4. 异常处理：检查输入数据的有效性");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度: O(n + m)");
        System.out.println("  - 遍历行程: O(n)");
        System.out.println("  - 计算前缀和: O(m)");
        System.out.println("空间复杂度: O(m)");
        System.out.println("  - 差分数组: O(m)");
    }
}