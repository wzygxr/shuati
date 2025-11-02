package class185.sweep_line_problems;

import java.util.*;

/**
 * Codeforces 612D. The Union of k-Segments
 * 
 * 题目来源：https://codeforces.com/problemset/problem/612/D
 * 
 * 题目描述：
 * 给定n条线段和一个整数k，求被至少k条线段覆盖的区间的并集。
 * 
 * 输入格式：
 * 第一行包含两个整数n和k(1 ≤ n ≤ 10^6, 1 ≤ k ≤ n)。
 * 接下来n行，每行包含两个整数li和ri(-10^9 ≤ li ≤ ri ≤ 10^9)，表示第i条线段的左右端点。
 * 
 * 输出格式：
 * 第一行输出一个整数m，表示结果区间的数量。
 * 接下来m行，每行输出两个整数aj和bj，表示结果区间。
 * 
 * 示例输入：
 * 3 2
 * 0 5
 * -3 2
 * 3 8
 * 
 * 示例输出：
 * 2
 * -3 2
 * 3 5
 * 
 * 解题思路：
 * 使用扫描线算法解决线段覆盖问题。核心思想是：
 * 1. 将每个线段的左右端点转换为事件点
 * 2. 对所有事件点按位置排序
 * 3. 扫描所有事件点，维护当前覆盖的线段数量
 * 4. 当覆盖数量从<k变为≥k时开始新区间，从≥k变为<k时结束区间
 * 
 * 时间复杂度：O(n log n)，其中 n 是线段的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 56. 合并区间
 * - LeetCode 253. 会议室II
 */
public class Codeforces_612D_TheUnionOfKSegments {
    
    /**
     * 线段覆盖问题的扫描线解法
     * @param segments 线段数组，每个线段是 [left, right] 形式
     * @param k 覆盖线段的最小数量
     * @return 被至少k条线段覆盖的区间列表
     */
    public static List<int[]> unionOfKSegments(int[][] segments, int k) {
        if (segments == null || segments.length == 0 || k <= 0) {
            return new ArrayList<>();
        }
        
        // 创建事件点列表：[位置, 类型]
        // 类型：0表示线段开始，1表示线段结束
        List<int[]> events = new ArrayList<>();
        
        // 为每个线段创建开始和结束事件
        for (int[] segment : segments) {
            events.add(new int[]{segment[0], 0});  // 开始事件
            events.add(new int[]{segment[1], 1});  // 结束事件
        }
        
        // 按照位置排序事件点
        // 如果位置相同，结束事件优先于开始事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[1], a[1]);
        });
        
        List<int[]> result = new ArrayList<>();
        int coverageCount = 0;
        int start = 0;
        
        // 扫描所有事件点
        for (int[] event : events) {
            int position = event[0];
            int type = event[1];
            
            if (type == 0) {
                // 线段开始事件
                coverageCount++;
                if (coverageCount == k) {
                    // 开始新区间
                    start = position;
                }
            } else {
                // 线段结束事件
                if (coverageCount == k) {
                    // 结束区间
                    result.add(new int[]{start, position});
                }
                coverageCount--;
            }
        }
        
        return result;
    }
    
    /**
     * 测试线段覆盖问题解法
     */
    public static void main(String[] args) {
        System.out.println("=== Codeforces 612D. The Union of k-Segments ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] segments1 = {
            {0, 5}, {-3, 2}, {3, 8}
        };
        int k1 = 2;
        List<int[]> result1 = unionOfKSegments(segments1, k1);
        System.out.println("输入线段: " + Arrays.deepToString(segments1));
        System.out.println("k = " + k1);
        System.out.println("输出区间数量: " + result1.size());
        System.out.print("输出区间: ");
        for (int[] interval : result1) {
            System.out.print("[" + interval[0] + "," + interval[1] + "] ");
        }
        System.out.println();
        System.out.println("期望: 2个区间，[-3,2] [3,5]");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] segments2 = {
            {0, 10}, {5, 15}, {10, 20}
        };
        int k2 = 3;
        List<int[]> result2 = unionOfKSegments(segments2, k2);
        System.out.println("输入线段: " + Arrays.deepToString(segments2));
        System.out.println("k = " + k2);
        System.out.println("输出区间数量: " + result2.size());
        System.out.print("输出区间: ");
        for (int[] interval : result2) {
            System.out.print("[" + interval[0] + "," + interval[1] + "] ");
        }
        System.out.println();
        System.out.println("期望: 1个区间，[10,10]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 100000;
        int[][] segments = new int[n][2];
        
        for (int i = 0; i < n; i++) {
            int left = random.nextInt(2000000) - 1000000;
            int right = left + random.nextInt(10000) + 1;
            segments[i][0] = left;
            segments[i][1] = right;
        }
        
        int k = 50000;
        
        long startTime = System.nanoTime();
        List<int[]> result = unionOfKSegments(segments, k);
        long endTime = System.nanoTime();
        
        System.out.println("100000个随机线段，k=50000的覆盖计算完成");
        System.out.println("覆盖区间数量: " + result.size());
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}