package class008_AdvancedAlgorithmsAndDataStructures.difference_array_problems;

import java.util.*;

/**
 * LeetCode 1109. 航班预订统计 (Corporate Flight Bookings)
 * 
 * 题目来源：https://leetcode.cn/problems/corporate-flight-bookings/
 * 
 * 题目描述：
 * 有 n 个航班，它们分别从 1 到 n 编号。
 * 有一份航班预订表 bookings ，表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi]
 * 意味着在从 firsti 到 lasti（包含 firsti 和 lasti）的每个航班上预订了 seatsi 个座位。
 * 请你返回一个长度为 n 的数组 answer，其中 answer[i] 是第 i+1 个航班预定的座位总数。
 * 
 * 算法思路：
 * 这是一个典型的差分数组应用场景，可以使用以下方法解决：
 * 1. 差分数组：对于每个预订记录，在差分数组的起始位置加座位数，
 *             在结束位置的下一个位置减座位数，最后通过前缀和得到结果
 * 2. 暴力法：直接对每个预订记录更新区间内的所有航班
 * 
 * 时间复杂度：
 * - 差分数组：O(n + m)，其中n是航班数，m是预订记录数
 * - 暴力法：O(m * n)
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 资源分配：批量资源预订统计
 * 2. 数据分析：区间数据聚合
 * 3. 金融：时间段内的交易统计
 * 
 * 相关题目：
 * 1. LeetCode 370. 区间加法
 * 2. LeetCode 1094. 拼车
 * 3. LeetCode 1893. 检查是否区域内所有整数都被覆盖
 */
public class LeetCode_1109_CorporateFlightBookings {
    
    /**
     * 方法1：差分数组（最优解）
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(n)
     * @param bookings 预订记录数组
     * @param n 航班数量
     * @return 每个航班的座位预订总数
     */
    public static int[] corpFlightBookingsDifferenceArray(int[][] bookings, int n) {
        // 创建差分数组
        int[] diff = new int[n + 1];
        
        // 处理每个预订记录
        for (int[] booking : bookings) {
            int first = booking[0];
            int last = booking[1];
            int seats = booking[2];
            
            // 在差分数组中标记区间更新
            diff[first - 1] += seats;  // 航班编号从1开始，数组索引从0开始
            if (last < n) {
                diff[last] -= seats;
            }
        }
        
        // 通过计算差分数组的前缀和来得到最终结果
        int[] result = new int[n];
        result[0] = diff[0];
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] + diff[i];
        }
        
        return result;
    }
    
    /**
     * 方法2：暴力法（用于对比）
     * 时间复杂度：O(m * n)
     * 空间复杂度：O(1)
     * @param bookings 预订记录数组
     * @param n 航班数量
     * @return 每个航班的座位预订总数
     */
    public static int[] corpFlightBookingsBruteForce(int[][] bookings, int n) {
        int[] result = new int[n];
        
        // 处理每个预订记录
        for (int[] booking : bookings) {
            int first = booking[0];
            int last = booking[1];
            int seats = booking[2];
            
            // 直接更新区间内的每个航班
            for (int i = first - 1; i < last; i++) {  // 航班编号从1开始，数组索引从0开始
                result[i] += seats;
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：使用TreeMap的扫描线算法（适用于稀疏数据）
     * 时间复杂度：O(m log m + n)
     * 空间复杂度：O(m)
     * @param bookings 预订记录数组
     * @param n 航班数量
     * @return 每个航班的座位预订总数
     */
    public static int[] corpFlightBookingsTreeMap(int[][] bookings, int n) {
        // 使用TreeMap记录每个位置的变化
        TreeMap<Integer, Integer> changes = new TreeMap<>();
        
        // 记录每个预订记录的变化
        for (int[] booking : bookings) {
            int first = booking[0];
            int last = booking[1];
            int seats = booking[2];
            
            changes.put(first, changes.getOrDefault(first, 0) + seats);
            changes.put(last + 1, changes.getOrDefault(last + 1, 0) - seats);
        }
        
        int[] result = new int[n];
        int currentSeats = 0;
        int changeIndex = 0;
        
        // 按顺序处理所有变化
        for (Map.Entry<Integer, Integer> entry : changes.entrySet()) {
            int position = entry.getKey();
            int change = entry.getValue();
            
            // 填充当前位置之前的所有航班
            while (changeIndex < position - 1 && changeIndex < n) {
                result[changeIndex] = currentSeats;
                changeIndex++;
            }
            
            // 更新当前座位数
            currentSeats += change;
        }
        
        // 填充剩余的航班
        while (changeIndex < n) {
            result[changeIndex] = currentSeats;
            changeIndex++;
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 1109. 航班预订统计 ===");
        
        // 测试用例1
        int[][] bookings1 = {{1,2,10},{2,3,20},{2,5,25}};
        int n1 = 5;
        System.out.println("测试用例1:");
        System.out.println("预订记录: " + Arrays.deepToString(bookings1));
        System.out.println("航班数: " + n1);
        System.out.println("差分数组结果: " + Arrays.toString(corpFlightBookingsDifferenceArray(bookings1, n1)));
        System.out.println("暴力法结果: " + Arrays.toString(corpFlightBookingsBruteForce(bookings1, n1)));
        System.out.println("TreeMap结果: " + Arrays.toString(corpFlightBookingsTreeMap(bookings1, n1)));
        System.out.println("期望结果: [10, 55, 45, 25, 25]");
        System.out.println();
        
        // 测试用例2
        int[][] bookings2 = {{1,2,10},{2,2,15}};
        int n2 = 2;
        System.out.println("测试用例2:");
        System.out.println("预订记录: " + Arrays.deepToString(bookings2));
        System.out.println("航班数: " + n2);
        System.out.println("差分数组结果: " + Arrays.toString(corpFlightBookingsDifferenceArray(bookings2, n2)));
        System.out.println("暴力法结果: " + Arrays.toString(corpFlightBookingsBruteForce(bookings2, n2)));
        System.out.println("TreeMap结果: " + Arrays.toString(corpFlightBookingsTreeMap(bookings2, n2)));
        System.out.println("期望结果: [10, 25]");
        System.out.println();
        
        // 测试用例3：边界情况
        int[][] bookings3 = {{1,1,100}};
        int n3 = 1;
        System.out.println("测试用例3 (单个航班):");
        System.out.println("预订记录: " + Arrays.deepToString(bookings3));
        System.out.println("航班数: " + n3);
        System.out.println("差分数组结果: " + Arrays.toString(corpFlightBookingsDifferenceArray(bookings3, n3)));
        System.out.println("暴力法结果: " + Arrays.toString(corpFlightBookingsBruteForce(bookings3, n3)));
        System.out.println("TreeMap结果: " + Arrays.toString(corpFlightBookingsTreeMap(bookings3, n3)));
        System.out.println("期望结果: [100]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int flightCount = 100000;
        int bookingCount = 10000;
        int[][] bookings = new int[bookingCount][3];
        
        // 生成随机预订记录
        for (int i = 0; i < bookingCount; i++) {
            int first = random.nextInt(flightCount) + 1;
            int last = Math.min(first + random.nextInt(1000) + 1, flightCount);
            int seats = random.nextInt(1000) + 1;
            bookings[i][0] = first;
            bookings[i][1] = last;
            bookings[i][2] = seats;
        }
        
        long startTime = System.nanoTime();
        int[] result1 = corpFlightBookingsDifferenceArray(bookings, flightCount);
        long endTime = System.nanoTime();
        System.out.println("差分数组法处理" + flightCount + "个航班," + bookingCount + "个预订记录时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        int[] result2 = corpFlightBookingsBruteForce(bookings, flightCount);
        endTime = System.nanoTime();
        System.out.println("暴力解法处理" + flightCount + "个航班," + bookingCount + "个预订记录时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        int[] result3 = corpFlightBookingsTreeMap(bookings, flightCount);
        endTime = System.nanoTime();
        System.out.println("TreeMap法处理" + flightCount + "个航班," + bookingCount + "个预订记录时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 验证结果一致性
        System.out.println("结果一致性检查: " + Arrays.equals(result1, result2) + " (差分数组 vs 暴力法)");
        System.out.println("结果一致性检查: " + Arrays.equals(result1, result3) + " (差分数组 vs TreeMap法)");
    }
}