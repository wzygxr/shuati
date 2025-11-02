package class027;

import java.util.PriorityQueue;

/**
 * 相关题目6: LeetCode 973. 最接近原点的 K 个点
 * 题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 * 题目描述: 给定一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
 * 并给定一个整数 k，返回离原点最近的 k 个点
 * 解题思路: 使用最大堆维护k个最近的点
 * 时间复杂度: O(n log k)
 * 空间复杂度: O(k)
 * 是否最优解: 是，这是处理Top K距离问题的经典解法
 */
public class Code09_KClosestPointsToOrigin {
    
    public static int[][] kClosest(int[][] points, int k) {
        // 使用最大堆维护k个最近的点
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
        );
        
        for (int[] point : points) {
            if (maxHeap.size() < k) {
                maxHeap.offer(point);
            } else {
                int[] farthest = maxHeap.peek();
                // 如果当前点比堆顶点更近，则替换
                if ((point[0] * point[0] + point[1] * point[1]) < 
                    (farthest[0] * farthest[0] + farthest[1] * farthest[1])) {
                    maxHeap.poll();
                    maxHeap.offer(point);
                }
            }
        }
        
        // 提取结果
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        int[][] points1 = {{1, 1}, {3, 3}, {2, 2}};
        int k1 = 2;
        int[][] result1 = kClosest(points1, k1);
        System.out.print("示例1输出: ");
        for (int[] point : result1) {
            System.out.print("[" + point[0] + "," + point[1] + "] ");
        }
        System.out.println(); // 期望输出: [[1,1],[2,2]] 或 [[2,2],[1,1]]
        
        int[][] points2 = {{3, 3}, {5, -1}, {-2, 4}};
        int k2 = 2;
        int[][] result2 = kClosest(points2, k2);
        System.out.print("示例2输出: ");
        for (int[] point : result2) {
            System.out.print("[" + point[0] + "," + point[1] + "] ");
        }
        System.out.println(); // 期望输出: [[3,3],[-2,4]] 或 [[-2,4],[3,3]]
    }
}