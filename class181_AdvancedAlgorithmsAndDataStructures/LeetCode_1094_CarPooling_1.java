package class008_AdvancedAlgorithmsAndDataStructures.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 1094. 拼车 (Car Pooling)
 * 
 * 题目来源：https://leetcode.cn/problems/car-pooling/
 * 
 * 题目描述：
 * 车上最初有 capacity 个空座位。车只能向一个方向行驶（不允许掉头或改变方向）。
 * 给定整数 capacity 和一个数组 trips ，trip[i] = [numPassengersi, fromi, toi]
 * 表示第 i 次旅行有 numPassengersi 乘客，接他们和放他们的位置分别是 fromi 和 toi。
 * 这些位置是从汽车的初始位置向东的公里数。
 * 当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。
 * 
 * 算法思路：
 * 这是一个典型的扫描线算法问题，可以使用以下方法解决：
 * 1. 扫描线算法：将上车和下车事件排序后处理
 * 2. 差分数组：记录每个位置的乘客变化
 * 3. 模拟：按位置顺序模拟乘客上下车过程
 * 
 * 时间复杂度：
 * - 扫描线算法：O(n log n)，其中n是行程数
 * - 差分数组：O(n + m)，其中n是行程数，m是最大位置
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 交通调度：公交车、出租车调度
 * 2. 资源分配：服务器负载均衡
 * 3. 活动安排：会议室预订
 * 
 * 相关题目：
 * 1. LeetCode 1109. 航班预订统计
 * 2. LeetCode 253. 会议室 II
 * 3. LeetCode 218. 天际线问题
 */
public class LeetCode_1094_CarPooling {
    
    /**
     * 方法1：扫描线算法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * @param trips 行程信息数组
     * @param capacity 车辆容量
     * @return 是否可以完成所有行程
     */
    public static boolean carPoolingSweepLine(int[][] trips, int capacity) {
        // 创建事件列表：[位置, 乘客变化]
        List<int[]> events = new ArrayList<>();
        
        // 为每个行程创建上车和下车事件
        for (int[] trip : trips) {
            int passengers = trip[0];
            int start = trip[1];
            int end = trip[2];
            
            // 上车事件（乘客增加）
            events.add(new int[]{start, passengers});
            // 下车事件（乘客减少）
            events.add(new int[]{end, -passengers});
        }
        
        // 按位置排序事件，如果位置相同，下车事件优先于上车事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        
        int currentPassengers = 0;
        
        // 扫描所有事件
        for (int[] event : events) {
            currentPassengers += event[1];
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法2：差分数组
     * 时间复杂度：O(n + m)，其中n是行程数，m是最大位置
     * 空间复杂度：O(m)
     * @param trips 行程信息数组
     * @param capacity 车辆容量
     * @return 是否可以完成所有行程
     */
    public static boolean carPoolingDifferenceArray(int[][] trips, int capacity) {
        // 找到最大位置
        int maxLocation = 0;
        for (int[] trip : trips) {
            maxLocation = Math.max(maxLocation, Math.max(trip[1], trip[2]));
        }
        
        // 创建差分数组
        int[] diff = new int[maxLocation + 1];
        
        // 记录每个行程的乘客变化
        for (int[] trip : trips) {
            int passengers = trip[0];
            int start = trip[1];
            int end = trip[2];
            
            diff[start] += passengers;
            diff[end] -= passengers;
        }
        
        // 通过前缀和计算每个位置的实际乘客数
        int currentPassengers = 0;
        for (int i = 0; i <= maxLocation; i++) {
            currentPassengers += diff[i];
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法3：使用TreeMap的扫描线算法（适用于位置范围很大的情况）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * @param trips 行程信息数组
     * @param capacity 车辆容量
     * @return 是否可以完成所有行程
     */
    public static boolean carPoolingTreeMap(int[][] trips, int capacity) {
        // 使用TreeMap记录每个位置的乘客变化
        TreeMap<Integer, Integer> changes = new TreeMap<>();
        
        // 记录每个行程的乘客变化
        for (int[] trip : trips) {
            int passengers = trip[0];
            int start = trip[1];
            int end = trip[2];
            
            changes.put(start, changes.getOrDefault(start, 0) + passengers);
            changes.put(end, changes.getOrDefault(end, 0) - passengers);
        }
        
        int currentPassengers = 0;
        
        // 按位置顺序处理所有变化
        for (int change : changes.values()) {
            currentPassengers += change;
            if (currentPassengers > capacity) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 方法4：模拟法（适用于位置范围较小的情况）
     * 时间复杂度：O(n * m)，其中n是行程数，m是最大位置
     * 空间复杂度：O(m)
     * @param trips 行程信息数组
     * @param capacity 车辆容量
     * @return 是否可以完成所有行程
     */
    public static boolean carPoolingSimulation(int[][] trips, int capacity) {
        // 找到最大位置
        int maxLocation = 0;
        for (int[] trip : trips) {
            maxLocation = Math.max(maxLocation, Math.max(trip[1], trip[2]));
        }
        
        // 记录每个位置的乘客数
        int[] passengersAtLocation = new int[maxLocation + 1];
        
        // 模拟每个行程
        for (int[] trip : trips) {
            int passengers = trip[0];
            int start = trip[1];
            int end = trip[2];
            
            // 在行程区间内增加乘客数
            for (int i = start; i < end; i++) {
                passengersAtLocation[i] += passengers;
                if (passengersAtLocation[i] > capacity) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 1094. 拼车 ===");
        
        // 测试用例1
        int[][] trips1 = {{2,1,5},{3,3,7}};
        int capacity1 = 4;
        System.out.println("测试用例1:");
        System.out.println("行程: " + Arrays.deepToString(trips1));
        System.out.println("容量: " + capacity1);
        System.out.println("扫描线算法结果: " + carPoolingSweepLine(trips1, capacity1));
        System.out.println("差分数组结果: " + carPoolingDifferenceArray(trips1, capacity1));
        System.out.println("TreeMap结果: " + carPoolingTreeMap(trips1, capacity1));
        System.out.println("模拟法结果: " + carPoolingSimulation(trips1, capacity1));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 测试用例2
        int[][] trips2 = {{2,1,5},{3,3,7}};
        int capacity2 = 5;
        System.out.println("测试用例2:");
        System.out.println("行程: " + Arrays.deepToString(trips2));
        System.out.println("容量: " + capacity2);
        System.out.println("扫描线算法结果: " + carPoolingSweepLine(trips2, capacity2));
        System.out.println("差分数组结果: " + carPoolingDifferenceArray(trips2, capacity2));
        System.out.println("TreeMap结果: " + carPoolingTreeMap(trips2, capacity2));
        System.out.println("模拟法结果: " + carPoolingSimulation(trips2, capacity2));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 测试用例3
        int[][] trips3 = {{3,2,7},{3,7,9},{8,3,9}};
        int capacity3 = 11;
        System.out.println("测试用例3:");
        System.out.println("行程: " + Arrays.deepToString(trips3));
        System.out.println("容量: " + capacity3);
        System.out.println("扫描线算法结果: " + carPoolingSweepLine(trips3, capacity3));
        System.out.println("差分数组结果: " + carPoolingDifferenceArray(trips3, capacity3));
        System.out.println("TreeMap结果: " + carPoolingTreeMap(trips3, capacity3));
        System.out.println("模拟法结果: " + carPoolingSimulation(trips3, capacity3));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int tripCount = 10000;
        int[][] trips = new int[tripCount][3];
        
        // 生成随机行程
        for (int i = 0; i < tripCount; i++) {
            int passengers = random.nextInt(100) + 1;
            int start = random.nextInt(1000);
            int end = start + random.nextInt(100) + 1;
            trips[i][0] = passengers;
            trips[i][1] = start;
            trips[i][2] = end;
        }
        int capacity = 10000;
        
        long startTime = System.nanoTime();
        boolean result1 = carPoolingSweepLine(trips, capacity);
        long endTime = System.nanoTime();
        System.out.println("扫描线算法处理" + tripCount + "个行程时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        boolean result2 = carPoolingTreeMap(trips, capacity);
        endTime = System.nanoTime();
        System.out.println("TreeMap算法处理" + tripCount + "个行程时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
    }
}