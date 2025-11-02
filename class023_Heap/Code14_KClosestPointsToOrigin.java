package class027;

import java.util.PriorityQueue;

/**
 * 相关题目6: LeetCode 973. 最接近原点的 K 个点
 * 题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 * 题目描述: 给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
 * 并且是一个整数 k ，返回离原点 (0,0) 最近的 k 个点。
 * 这里，平面上两点之间的距离是欧几里德距离。
 * 解题思路: 使用最大堆维护K个最近的点，堆中存储点的平方距离和点坐标
 * 时间复杂度: O(n log k)，其中n是点的数量，堆操作需要O(log k)时间
 * 空间复杂度: O(k)，堆最多存储k个点
 * 是否最优解: 是，这是解决Top K最近点问题的经典解法
 * 
 * 本题属于堆的典型应用场景：需要在大量数据中动态维护前K个最小/最大值
 */
public class Code14_KClosestPointsToOrigin {
    
    /**
     * 找出离原点最近的K个点
     * @param points 二维整数数组，每个元素表示一个点的坐标 [x, y]
     * @param k 需要返回的最近点的数量
     * @return 离原点最近的k个点组成的二维数组
     * @throws IllegalArgumentException 当输入参数无效时抛出异常
     */
    public static int[][] kClosest(int[][] points, int k) {
        // 异常处理：检查输入数组是否为null或空
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException("输入点数组不能为空");
        }
        
        // 异常处理：检查k是否在有效范围内
        if (k <= 0 || k > points.length) {
            throw new IllegalArgumentException("k的值必须在1到数组长度之间");
        }
        
        // 创建最大堆，按照距离的平方降序排列（这样堆顶是当前最远的点）
        // 堆中存储的是[距离平方, x坐标, y坐标]的数组
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        
        // 遍历所有点
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            // 计算点到原点的距离的平方（避免浮点数运算和平方根操作）
            int distSquare = x * x + y * y;
            
            // 调试信息：打印当前处理的点和距离
            // System.out.println("处理点: [" + x + ", " + y + "], 距离平方: " + distSquare);
            
            if (maxHeap.size() < k) {
                // 如果堆的大小小于k，直接将当前点加入堆
                maxHeap.offer(new int[]{distSquare, x, y});
            } else if (distSquare < maxHeap.peek()[0]) {
                // 如果当前点比堆顶的点更近（距离平方更小）
                // 则移除堆顶的点（当前k个点中最远的），并加入新点
                maxHeap.poll();
                maxHeap.offer(new int[]{distSquare, x, y});
            }
            // 否则（当前点比堆顶的点更远或相等），不做任何操作
        }
        
        // 将堆中的k个点转换为结果数组
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            int[] pointWithDist = maxHeap.poll();
            result[i][0] = pointWithDist[1]; // x坐标
            result[i][1] = pointWithDist[2]; // y坐标
        }
        
        return result;
    }
    
    /**
     * 打印二维数组的辅助方法
     */
    public static void printPoints(int[][] points) {
        System.out.print("[");
        for (int i = 0; i < points.length; i++) {
            System.out.print("[" + points[i][0] + ", " + points[i][1] + "]");
            if (i < points.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        int[][] points1 = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
        int k1 = 2;
        System.out.print("示例1输出: ");
        int[][] result1 = kClosest(points1, k1);
        printPoints(result1); // 期望输出: [[-2, 2], [0, 1]] 或 [[0, 1], [-2, 2]]
        
        // 测试用例2：k等于数组长度
        int[][] points2 = {{3, 3}, {5, -1}, {-2, 4}};
        int k2 = 3;
        System.out.print("示例2输出: ");
        int[][] result2 = kClosest(points2, k2);
        printPoints(result2); // 期望输出: 原数组的所有点，按距离排序
        
        // 测试用例3：k=1，只有一个点
        int[][] points3 = {{1, 2}, {1, 3}};
        int k3 = 1;
        System.out.print("示例3输出: ");
        int[][] result3 = kClosest(points3, k3);
        printPoints(result3); // 期望输出: [[1, 2]]
        
        // 测试用例4：边界情况 - 原点
        int[][] points4 = {{0, 0}, {1, 2}, {3, 4}};
        int k4 = 1;
        System.out.print("示例4输出: ");
        int[][] result4 = kClosest(points4, k4);
        printPoints(result4); // 期望输出: [[0, 0]]
    }
}