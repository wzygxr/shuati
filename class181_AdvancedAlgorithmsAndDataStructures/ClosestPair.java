package class185;

import java.util.*;

/**
 * 平面分治算法 - 最近点对问题实现 (Java版本)
 * 
 * 问题描述：
 * 给定平面上的n个点，找出距离最近的两个点。
 * 
 * 算法思路：
 * 使用分治法解决最近点对问题：
 * 1. 将点集按照x坐标排序
 * 2. 递归地在左半部分和右半部分找最近点对
 * 3. 找到跨越中线的最近点对
 * 4. 返回三者中的最小值
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 计算几何中的碰撞检测
 * 2. 机器学习中的最近邻搜索
 * 3. 地理信息系统中的最近设施查询
 */
public class ClosestPair {
    
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
     * 最近点对结果类
     */
    static class ClosestPairResult {
        Point p1, p2;
        double distance;
        
        ClosestPairResult(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }
    }
    
    /**
     * 查找最近点对
     * @param points 点集
     * @return 最近点对结果
     */
    public static ClosestPairResult findClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("点集必须包含至少两个点");
        }
        
        // 按照x坐标排序
        Point[] pointsSortedByX = points.clone();
        Arrays.sort(pointsSortedByX, Comparator.comparingDouble(p -> p.x));
        
        // 按照y坐标排序（用于后续处理）
        Point[] pointsSortedByY = pointsSortedByX.clone();
        Arrays.sort(pointsSortedByY, Comparator.comparingDouble(p -> p.y));
        
        // 调用递归函数
        return closestPairRecursive(pointsSortedByX, 0, pointsSortedByX.length - 1, pointsSortedByY);
    }
    
    /**
     * 递归求解最近点对
     * @param pointsSortedByX 按x坐标排序的点集
     * @param left 左边界
     * @param right 右边界
     * @param pointsSortedByY 按y坐标排序的点集
     * @return 最近点对结果
     */
    private static ClosestPairResult closestPairRecursive(Point[] pointsSortedByX, int left, int right, 
                                                         Point[] pointsSortedByY) {
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
        
        int leftIdx = 0, rightIdx = 0;
        for (Point p : pointsSortedByY) {
            if (p.x <= midPoint.x && leftIdx < leftPointsSortedByY.length) {
                leftPointsSortedByY[leftIdx++] = p;
            } else if (rightIdx < rightPointsSortedByY.length) {
                rightPointsSortedByY[rightIdx++] = p;
            }
        }
        
        // 递归求解左右子数组
        ClosestPairResult leftResult = closestPairRecursive(pointsSortedByX, left, mid, leftPointsSortedByY);
        ClosestPairResult rightResult = closestPairRecursive(pointsSortedByX, mid + 1, right, rightPointsSortedByY);
        
        // 确定左右子数组中的最小距离
        ClosestPairResult minResult = leftResult.distance < rightResult.distance ? leftResult : rightResult;
        
        // 处理跨越中线的点对
        // 筛选出在中线附近的点
        List<Point> strip = new ArrayList<>();
        for (Point p : pointsSortedByY) {
            if (Math.abs(p.x - midPoint.x) < minResult.distance) {
                strip.add(p);
            }
        }
        
        // 检查strip中的点对
        ClosestPairResult stripResult = checkStrip(strip, minResult.distance);
        
        // 返回最小距离的点对
        if (stripResult.distance < minResult.distance) {
            return stripResult;
        } else {
            return minResult;
        }
    }
    
    /**
     * 暴力求解小规模问题
     * @param points 点集
     * @param left 左边界
     * @param right 右边界
     * @return 最近点对结果
     */
    private static ClosestPairResult bruteForce(Point[] points, int left, int right) {
        double minDist = Double.MAX_VALUE;
        Point p1 = null, p2 = null;
        
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDist) {
                    minDist = dist;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }
        
        return new ClosestPairResult(p1, p2, minDist);
    }
    
    /**
     * 检查跨越中线的点对
     * @param strip 中线附近的点集
     * @param minDist 当前最小距离
     * @return 最近点对结果
     */
    private static ClosestPairResult checkStrip(List<Point> strip, double minDist) {
        double currentMin = minDist;
        Point p1 = null, p2 = null;
        
        // 按照y坐标排序（已经是排序好的）
        // 只需要检查相邻的最多7个点
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < currentMin; j++) {
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < currentMin) {
                    currentMin = dist;
                    p1 = strip.get(i);
                    p2 = strip.get(j);
                }
            }
        }
        
        // 如果没有找到更近的点对，返回一个无效结果
        if (p1 == null) {
            return new ClosestPairResult(null, null, Double.MAX_VALUE);
        }
        
        return new ClosestPairResult(p1, p2, currentMin);
    }
    
    /**
     * 测试最近点对算法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试最近点对算法 ===");
        
        // 测试用例1：随机点集
        Point[] points1 = {
            new Point(2, 3),
            new Point(12, 30),
            new Point(40, 50),
            new Point(5, 1),
            new Point(12, 10),
            new Point(3, 4)
        };
        
        ClosestPairResult result1 = findClosestPair(points1);
        System.out.println("最近点对1: " + result1.p1 + " 和 " + result1.p2);
        System.out.println("距离: " + result1.distance);
        
        // 测试用例2：所有点在一条直线上
        Point[] points2 = {
            new Point(0, 0),
            new Point(1, 0),
            new Point(2, 0),
            new Point(3, 0),
            new Point(100, 0)
        };
        
        ClosestPairResult result2 = findClosestPair(points2);
        System.out.println("最近点对2: " + result2.p1 + " 和 " + result2.p2);
        System.out.println("距离: " + result2.distance);
        
        // 测试用例3：边界情况
        Point[] points3 = {
            new Point(0, 0),
            new Point(0, 0)  // 重复点
        };
        
        ClosestPairResult result3 = findClosestPair(points3);
        System.out.println("最近点对3: " + result3.p1 + " 和 " + result3.p2);
        System.out.println("距离: " + result3.distance);
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        Random random = new Random(42); // 固定种子以确保可重复性
        int n = 10000;
        Point[] points4 = new Point[n];
        for (int i = 0; i < n; i++) {
            points4[i] = new Point(random.nextDouble() * 1000, random.nextDouble() * 1000);
        }
        
        long startTime = System.nanoTime();
        ClosestPairResult result4 = findClosestPair(points4);
        long endTime = System.nanoTime();
        
        System.out.println("10000个随机点的最近点对:");
        System.out.println("最近点对: " + result4.p1 + " 和 " + result4.p2);
        System.out.println("距离: " + result4.distance);
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000 + " ms");
    }
}