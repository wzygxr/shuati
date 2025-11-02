package class027;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 相关题目7: 牛客网 - 最多线段重合问题
 * 题目链接: https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
 * 题目描述: 给定很多线段，每个线段都有两个数组[start, end]，求最多线段重合的点的重合线段数
 * 解题思路: 使用最小堆维护当前覆盖点的线段右端点
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 是否最优解: 是，这是处理线段重合问题的最优解法
 */
public class Code10_MaxCovers {
    
    public static int maxCovers(int[][] lines) {
        // 按照线段起点排序
        Arrays.sort(lines, (a, b) -> a[0] - b[0]);
        
        // 最小堆，维护当前覆盖点的线段右端点
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int max = 0;
        
        for (int[] line : lines) {
            // 移除已经结束的线段
            while (!minHeap.isEmpty() && minHeap.peek() <= line[0]) {
                minHeap.poll();
            }
            
            // 添加当前线段的右端点
            minHeap.offer(line[1]);
            
            // 更新最大重合数
            max = Math.max(max, minHeap.size());
        }
        
        return max;
    }
    
    // 测试方法
    public static void main(String[] args) {
        int[][] lines1 = {{1, 3}, {2, 4}, {3, 5}};
        System.out.println("示例1输出: " + maxCovers(lines1)); // 期望输出: 2
        
        int[][] lines2 = {{1, 4}, {2, 3}, {3, 6}, {4, 5}};
        System.out.println("示例2输出: " + maxCovers(lines2)); // 期望输出: 3
    }
}