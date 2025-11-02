package class093;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 最低加油次数（Minimum Number of Refueling Stops）
 * 题目来源：LeetCode 871
 * 题目链接：https://leetcode.cn/problems/minimum-number-of-refueling-stops/
 * 
 * 问题描述：
 * 汽车从起点出发驶向目的地，该目的地距离起点target英里。
 * 沿途有加油站，每个station[i]代表一个加油站，位于距离起点station[i][0]英里处，有station[i][1]升汽油。
 * 假设汽车油箱的容量是无限的，其中最初有startFuel升燃料。
 * 它每行驶1英里就会用掉1升汽油。
 * 当汽车到达加油站时，它可能停下来加油，将所有汽油从加油站转移到汽车中。
 * 为了到达目的地，汽车所必要的最低加油次数是多少？如果无法到达目的地，则返回-1。
 * 
 * 算法思路：
 * 使用贪心策略，结合最大堆：
 * 1. 遍历加油站，维护当前能到达的最远位置
 * 2. 当油量不足以到达下一个加油站时，从经过的加油站中选择油量最大的进行加油
 * 3. 使用最大堆来存储经过的加油站的油量
 * 4. 每次加油后更新当前油量和加油次数
 * 
 * 时间复杂度：O(n log n) - 堆操作的时间复杂度
 * 空间复杂度：O(n) - 最大堆的空间
 * 
 * 是否最优解：是。这是该问题的最优解法。
 * 
 * 适用场景：
 * 1. 路径规划问题
 * 2. 资源调度问题
 * 
 * 异常处理：
 * 1. 处理无法到达目的地的情况
 * 2. 处理边界条件
 * 
 * 工程化考量：
 * 1. 输入验证：检查参数是否合法
 * 2. 边界条件：处理起点就是目的地的情况
 * 3. 性能优化：使用堆提高效率
 * 
 * 相关题目：
 * 1. LeetCode 134. 加油站 - 环路加油问题
 * 2. LeetCode 45. 跳跃游戏 II - 最少跳跃次数
 * 3. LeetCode 55. 跳跃游戏 - 可达性判断
 * 4. 牛客网 NC48 跳跃游戏 - 与LeetCode 55相同
 * 5. LintCode 117. 跳跃游戏 II - 与LeetCode 45相同
 * 6. HackerRank - Jumping on the Clouds - 简化版跳跃游戏
 * 7. CodeChef - JUMP - 类似跳跃游戏的变种
 * 8. AtCoder ABC161D - Lunlun Number - BFS搜索相关
 * 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
 * 10. POJ 1700 - Crossing River - 经典过河问题
 */
public class Code18_MinimumRefuelingStops {
    
    /**
     * 计算最低加油次数
     * 
     * @param target 目的地距离
     * @param startFuel 初始油量
     * @param stations 加油站数组，每个元素是[位置, 油量]
     * @return 最低加油次数，无法到达返回-1
     */
    public static int minRefuelStops(int target, int startFuel, int[][] stations) {
        // 边界条件检查
        if (startFuel >= target) {
            return 0; // 初始油量足够到达目的地
        }
        
        if (stations == null || stations.length == 0) {
            return startFuel >= target ? 0 : -1; // 没有加油站，检查初始油量是否足够
        }
        
        // 最大堆，存储经过的加油站的油量
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        
        int currentFuel = startFuel; // 当前油量
        int currentPosition = 0;    // 当前位置
        int refuelCount = 0;        // 加油次数
        int stationIndex = 0;        // 加油站索引
        
        while (currentPosition + currentFuel < target) {
            int nextPosition = currentPosition + currentFuel; // 当前能到达的最远位置
            
            // 将能到达的加油站加入最大堆
            while (stationIndex < stations.length && stations[stationIndex][0] <= nextPosition) {
                maxHeap.offer(stations[stationIndex][1]);
                stationIndex++;
            }
            
            // 如果没有加油站可加油，且无法到达目的地
            if (maxHeap.isEmpty()) {
                return -1;
            }
            
            // 选择油量最大的加油站加油
            int maxFuel = maxHeap.poll();
            currentFuel = currentFuel - (stations[stationIndex - 1][0] - currentPosition) + maxFuel;
            currentPosition = stations[stationIndex - 1][0];
            refuelCount++;
            
            // 如果加油后能到达目的地
            if (currentPosition + currentFuel >= target) {
                return refuelCount;
            }
        }
        
        return refuelCount;
    }
    
    /**
     * 测试函数，验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况 - 需要加油
        int target1 = 100;
        int startFuel1 = 10;
        int[][] stations1 = {{10, 60}, {20, 30}, {30, 30}, {60, 40}};
        int result1 = minRefuelStops(target1, startFuel1, stations1);
        System.out.println("测试用例1:");
        System.out.println("目的地距离: " + target1);
        System.out.println("初始油量: " + startFuel1);
        System.out.print("加油站: [");
        for (int i = 0; i < stations1.length; i++) {
            System.out.print("[" + stations1[i][0] + "," + stations1[i][1] + "]");
            if (i < stations1.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最低加油次数: " + result1);
        System.out.println("期望输出: 2");
        System.out.println();
        
        // 测试用例2: 基本情况 - 无法到达
        int target2 = 100;
        int startFuel2 = 10;
        int[][] stations2 = {{20, 20}};
        int result2 = minRefuelStops(target2, startFuel2, stations2);
        System.out.println("测试用例2:");
        System.out.println("目的地距离: " + target2);
        System.out.println("初始油量: " + startFuel2);
        System.out.print("加油站: [");
        for (int i = 0; i < stations2.length; i++) {
            System.out.print("[" + stations2[i][0] + "," + stations2[i][1] + "]");
            if (i < stations2.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最低加油次数: " + result2);
        System.out.println("期望输出: -1");
        System.out.println();
        
        // 测试用例3: 边界情况 - 不需要加油
        int target3 = 50;
        int startFuel3 = 60;
        int[][] stations3 = {{10, 20}, {20, 30}};
        int result3 = minRefuelStops(target3, startFuel3, stations3);
        System.out.println("测试用例3:");
        System.out.println("目的地距离: " + target3);
        System.out.println("初始油量: " + startFuel3);
        System.out.print("加油站: [");
        for (int i = 0; i < stations3.length; i++) {
            System.out.print("[" + stations3[i][0] + "," + stations3[i][1] + "]");
            if (i < stations3.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最低加油次数: " + result3);
        System.out.println("期望输出: 0");
        System.out.println();
        
        // 测试用例4: 边界情况 - 没有加油站
        int target4 = 50;
        int startFuel4 = 40;
        int[][] stations4 = {};
        int result4 = minRefuelStops(target4, startFuel4, stations4);
        System.out.println("测试用例4:");
        System.out.println("目的地距离: " + target4);
        System.out.println("初始油量: " + startFuel4);
        System.out.println("加油站: []");
        System.out.println("最低加油次数: " + result4);
        System.out.println("期望输出: -1");
        System.out.println();
        
        // 测试用例5: 复杂情况 - 多个加油站
        int target5 = 100;
        int startFuel5 = 20;
        int[][] stations5 = {{10, 10}, {20, 20}, {30, 30}, {40, 40}, {50, 50}};
        int result5 = minRefuelStops(target5, startFuel5, stations5);
        System.out.println("测试用例5:");
        System.out.println("目的地距离: " + target5);
        System.out.println("初始油量: " + startFuel5);
        System.out.print("加油站: [");
        for (int i = 0; i < stations5.length; i++) {
            System.out.print("[" + stations5[i][0] + "," + stations5[i][1] + "]");
            if (i < stations5.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("最低加油次数: " + result5);
        System.out.println("期望输出: 3");
    }
}