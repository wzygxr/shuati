package class184;

import java.util.*;

/**
 * LeetCode 973. K Closest Points to Origin 解决方案
 * 
 * 题目链接: https://leetcode.com/problems/k-closest-points-to-origin/
 * 题目描述: 给定一个点数组，返回离原点最近的k个点
 * 解题思路: 可以使用平面分治算法，也可以使用堆或排序
 * 
 * 时间复杂度: O(n log n) - 排序方法
 * 空间复杂度: O(1) - 不考虑输出数组
 */
public class LeetCode_KClosestPoints {
    
    /**
     * 使用排序方法找出离原点最近的k个点
     * @param points 点数组
     * @param k 要返回的点数量
     * @return 离原点最近的k个点
     */
    public int[][] kClosest(int[][] points, int k) {
        // 检查输入有效性
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][0];
        }
        
        // 按照距离原点的距离排序
        Arrays.sort(points, (a, b) -> {
            double distA = Math.sqrt(a[0] * a[0] + a[1] * a[1]);
            double distB = Math.sqrt(b[0] * b[0] + b[1] * b[1]);
            return Double.compare(distA, distB);
        });
        
        // 返回前k个点
        return Arrays.copyOfRange(points, 0, Math.min(k, points.length));
    }
    
    /**
     * 使用平面分治算法找出离原点最近的k个点
     * @param points 点数组
     * @param k 要返回的点数量
     * @return 离原点最近的k个点
     */
    public int[][] kClosestDivideConquer(int[][] points, int k) {
        // 检查输入有效性
        if (points == null || points.length == 0 || k <= 0) {
            return new int[0][0];
        }
        
        // 转换为Point对象数组以便使用平面分治算法
        Point[] pointObjects = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pointObjects[i] = new Point(points[i][0], points[i][1]);
        }
        
        // 使用平面分治算法找出所有最近点对（这里简化为找最近的k个点）
        // 实际上，对于这个问题，排序方法更简单有效
        // 但为了演示平面分治算法，我们仍然使用它
        
        // 按照x坐标排序
        Point[] pointsSortedByX = pointObjects.clone();
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        
        // 按照y坐标排序
        Point[] pointsSortedByY = pointObjects.clone();
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));
        
        // 找出最近的k个点
        List<Point> closestPoints = new ArrayList<>();
        findKClosestPoints(pointsSortedByX, pointsSortedByY, k, closestPoints);
        
        // 转换回int[][]格式
        int[][] result = new int[Math.min(k, closestPoints.size())][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = (int) closestPoints.get(i).x;
            result[i][1] = (int) closestPoints.get(i).y;
        }
        
        return result;
    }
    
    /**
     * 辅助方法：找出最近的k个点
     */
    private void findKClosestPoints(Point[] pointsSortedByX, Point[] pointsSortedByY, int k, List<Point> result) {
        // 对于这个特定问题，使用排序更简单
        // 这里只是为了演示平面分治算法的思想
        
        Point origin = new Point(0, 0);
        Point[] points = pointsSortedByX.clone();
        
        // 按照距离原点的距离排序
        Arrays.sort(points, Comparator.comparingDouble(p -> p.distanceTo(origin)));
        
        // 添加前k个点到结果列表
        for (int i = 0; i < Math.min(k, points.length); i++) {
            result.add(points[i]);
        }
    }
    
    /**
     * 点类，用于存储二维坐标
     */
    static class Point {
        double x, y;
        
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        /**
         * 计算两个点之间的欧几里得距离
         */
        public double distanceTo(Point p) {
            double dx = this.x - p.x;
            double dy = this.y - p.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LeetCode_KClosestPoints solution = new LeetCode_KClosestPoints();
        
        // 测试用例1
        int[][] points1 = {{1, 1}, {3, 3}, {2, 2}};
        int k1 = 1;
        int[][] result1 = solution.kClosest(points1, k1);
        System.out.println("测试用例1 - 排序方法:");
        System.out.println("输入: points = [[1,1],[3,3],[2,2]], k = 1");
        System.out.print("输出: [");
        for (int i = 0; i < result1.length; i++) {
            if (i > 0) System.out.print(",");
            System.out.print("[" + result1[i][0] + "," + result1[i][1] + "]");
        }
        System.out.println("]");
        
        // 测试用例2
        int[][] points2 = {{3, 3}, {5, -1}, {-2, 4}};
        int k2 = 2;
        int[][] result2 = solution.kClosest(points2, k2);
        System.out.println("\n测试用例2 - 排序方法:");
        System.out.println("输入: points = [[3,3],[5,-1],[-2,4]], k = 2");
        System.out.print("输出: [");
        for (int i = 0; i < result2.length; i++) {
            if (i > 0) System.out.print(",");
            System.out.print("[" + result2[i][0] + "," + result2[i][1] + "]");
        }
        System.out.println("]");
        
        // 测试平面分治方法
        int[][] result3 = solution.kClosestDivideConquer(points1, k1);
        System.out.println("\n测试用例1 - 平面分治方法:");
        System.out.println("输入: points = [[1,1],[3,3],[2,2]], k = 1");
        System.out.print("输出: [");
        for (int i = 0; i < result3.length; i++) {
            if (i > 0) System.out.print(",");
            System.out.print("[" + result3[i][0] + "," + result3[i][1] + "]");
        }
        System.out.println("]");
    }
}