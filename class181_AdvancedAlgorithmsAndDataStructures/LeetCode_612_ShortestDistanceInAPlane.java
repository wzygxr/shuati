package class185.closest_pair_problems;

import java.util.*;

/**
 * LeetCode 612. 平面上的最短距离
 * 
 * 问题描述：
 * 给定一个平面上的点集，找到其中距离最近的两个点之间的距离。
 * 
 * 算法思路：
 * 使用平面分治算法（Closest Pair of Points）解决最近点对问题。
 * 1. 将点集按照x坐标排序
 * 2. 递归地将点集分为左右两部分
 * 3. 分别计算左右两部分的最近点对距离
 * 4. 计算跨越分割线的最近点对距离
 * 5. 返回三者中的最小值
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 地理信息系统：最近设施查询
 * 2. 计算机图形学：碰撞检测
 * 3. 机器学习：最近邻搜索
 * 
 * 相关题目：
 * 1. LeetCode 973. 最接近原点的K个点
 * 2. LeetCode 719. 找出第 k 小的距离对
 * 3. LeetCode 149. 直线上最多的点数
 */
public class LeetCode_612_ShortestDistanceInAPlane {
    
    static class Point {
        double x, y;
        
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        double distanceTo(Point p) {
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
     * 暴力解法：计算所有点对的距离
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    public double shortestDistanceBruteForce(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("点集必须包含至少两个点");
        }
        
        double minDistance = Double.MAX_VALUE;
        
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
    
    /**
     * 平面分治算法：高效解决最近点对问题
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    public double shortestDistanceDivideConquer(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("点集必须包含至少两个点");
        }
        
        // 按照x坐标排序
        Point[] pointsSortedByX = points.clone();
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        
        // 按照y坐标排序（用于后续处理）
        Point[] pointsSortedByY = pointsSortedByX.clone();
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));
        
        return closestPairRecursive(pointsSortedByX, 0, pointsSortedByX.length - 1, pointsSortedByY);
    }
    
    /**
     * 递归求解最近点对
     */
    private double closestPairRecursive(Point[] pointsSortedByX, int left, int right, Point[] pointsSortedByY) {
        // 基本情况：小规模问题直接暴力求解
        if (right - left <= 3) {
            return bruteForce(pointsSortedByX, left, right);
        }
        
        // 分治求解
        int mid = left + (right - left) / 2;
        Point midPoint = pointsSortedByX[mid];
        
        // 分割y排序的数组
        Point[] leftPointsSortedByY = new Point[mid - left + 1];
        Point[] rightPointsSortedByY = new Point[right - mid];
        
        int leftIndex = 0, rightIndex = 0;
        for (Point point : pointsSortedByY) {
            if (point.x <= midPoint.x) {
                leftPointsSortedByY[leftIndex++] = point;
            } else {
                rightPointsSortedByY[rightIndex++] = point;
            }
        }
        
        // 递归求解左右两部分的最近距离
        double leftMin = closestPairRecursive(pointsSortedByX, left, mid, leftPointsSortedByY);
        double rightMin = closestPairRecursive(pointsSortedByX, mid + 1, right, rightPointsSortedByY);
        double minDistance = Math.min(leftMin, rightMin);
        
        // 检查跨越分割线的点对
        List<Point> strip = new ArrayList<>();
        for (Point point : pointsSortedByY) {
            if (Math.abs(point.x - midPoint.x) < minDistance) {
                strip.add(point);
            }
        }
        
        // 在strip中检查最近点对
        for (int i = 0; i < strip.size(); i++) {
            // 只需要检查后面的7个点（理论证明最多需要检查7个点）
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < minDistance; j++) {
                double distance = strip.get(i).distanceTo(strip.get(j));
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
    
    /**
     * 暴力求解小规模点集的最近距离
     */
    private double bruteForce(Point[] points, int left, int right) {
        double minDistance = Double.MAX_VALUE;
        
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        
        return minDistance;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode_612_ShortestDistanceInAPlane solution = new LeetCode_612_ShortestDistanceInAPlane();
        
        System.out.println("=== 测试 LeetCode 612. 平面上的最短距离 ===");
        
        // 测试用例1：简单点集
        Point[] points1 = {
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            new Point(4, 4)
        };
        
        System.out.println("测试用例1:");
        System.out.println("点集: " + Arrays.toString(points1));
        System.out.println("暴力解法结果: " + solution.shortestDistanceBruteForce(points1));
        System.out.println("分治算法结果: " + solution.shortestDistanceDivideConquer(points1));
        
        // 测试用例2：随机点集
        Random random = new Random(42);
        Point[] points2 = new Point[10];
        for (int i = 0; i < 10; i++) {
            points2[i] = new Point(random.nextDouble() * 100, random.nextDouble() * 100);
        }
        
        System.out.println("\n测试用例2:");
        System.out.println("随机点集大小: " + points2.length);
        System.out.println("暴力解法结果: " + solution.shortestDistanceBruteForce(points2));
        System.out.println("分治算法结果: " + solution.shortestDistanceDivideConquer(points2));
        
        // 测试用例3：边界情况（两个点）
        Point[] points3 = {
            new Point(0, 0),
            new Point(3, 4)
        };
        
        System.out.println("\n测试用例3:");
        System.out.println("点集: " + Arrays.toString(points3));
        System.out.println("暴力解法结果: " + solution.shortestDistanceBruteForce(points3));
        System.out.println("分治算法结果: " + solution.shortestDistanceDivideConquer(points3));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        // 小规模测试
        Point[] smallPoints = new Point[100];
        for (int i = 0; i < 100; i++) {
            smallPoints[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
        
        long startTime = System.nanoTime();
        double bruteResult = solution.shortestDistanceBruteForce(smallPoints);
        long bruteTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        double divideResult = solution.shortestDistanceDivideConquer(smallPoints);
        long divideTime = System.nanoTime() - startTime;
        
        System.out.println("100个点:");
        System.out.println("暴力解法时间: " + bruteTime / 1_000_000.0 + " ms, 结果: " + bruteResult);
        System.out.println("分治算法时间: " + divideTime / 1_000_000.0 + " ms, 结果: " + divideResult);
        
        // 大规模测试
        Point[] largePoints = new Point[10000];
        for (int i = 0; i < 10000; i++) {
            largePoints[i] = new Point(random.nextDouble() * 10000, random.nextDouble() * 10000);
        }
        
        startTime = System.nanoTime();
        divideResult = solution.shortestDistanceDivideConquer(largePoints);
        divideTime = System.nanoTime() - startTime;
        
        System.out.println("\n10000个点:");
        System.out.println("分治算法时间: " + divideTime / 1_000_000.0 + " ms, 结果: " + divideResult);
        
        // 验证算法正确性
        System.out.println("\n=== 算法正确性验证 ===");
        
        // 创建已知最小距离的点集
        Point[] knownPoints = {
            new Point(0, 0),
            new Point(1, 1),
            new Point(3, 3),
            new Point(0.5, 0.5)  // 这个点距离(0,0)和(1,1)都很近
        };
        
        double expectedMin = Math.sqrt(0.5);  // (0,0)到(0.5,0.5)的距离
        double actualMin = solution.shortestDistanceDivideConquer(knownPoints);
        
        System.out.println("预期最小距离: " + expectedMin);
        System.out.println("实际最小距离: " + actualMin);
        System.out.println("算法正确性: " + (Math.abs(expectedMin - actualMin) < 1e-10));
        
        // 工程化考量：异常处理测试
        System.out.println("\n=== 异常处理测试 ===");
        
        try {
            solution.shortestDistanceDivideConquer(new Point[0]);
            System.out.println("空数组测试: 失败（应该抛出异常）");
        } catch (IllegalArgumentException e) {
            System.out.println("空数组测试: 通过（正确抛出异常）");
        }
        
        try {
            solution.shortestDistanceDivideConquer(new Point[1]);
            System.out.println("单点数组测试: 失败（应该抛出异常）");
        } catch (IllegalArgumentException e) {
            System.out.println("单点数组测试: 通过（正确抛出异常）");
        }
    }
}