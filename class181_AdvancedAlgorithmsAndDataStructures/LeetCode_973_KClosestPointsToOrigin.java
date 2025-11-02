package class185.closest_pair_problems;

import java.util.*;

/**
 * LeetCode 973. 最接近原点的K个点
 * 
 * 问题描述：
 * 给定一个由平面上的点组成的数组 points，其中 points[i] = [xi, yi]，
 * 从中选取 k 个距离原点 (0, 0) 最近的点。可以按任意顺序返回答案。
 * 
 * 算法思路：
 * 本题可以使用多种方法解决：
 * 1. 排序法：按照距离原点的距离排序，取前k个点
 * 2. 最小堆法：维护一个大小为k的最大堆
 * 3. 快速选择法：使用快速选择算法找到第k小的元素
 * 4. 最近点对算法的变种
 * 
 * 时间复杂度：
 * - 排序法：O(n log n)
 * - 最小堆法：O(n log k)
 * - 快速选择法：O(n) 平均情况
 * 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 机器学习中的最近邻搜索
 * 2. 地理信息系统中的最近设施查询
 * 3. 推荐系统中的相似用户查找
 * 
 * 相关题目：
 * 1. LeetCode 347. 前 K 个高频元素
 * 2. LeetCode 215. 数组中的第K个最大元素
 * 3. LeetCode 719. 找出第 k 小的距离对
 */
public class LeetCode_973_KClosestPointsToOrigin {
    
    /**
     * 方法1：排序法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public int[][] kClosestSort(int[][] points, int k) {
        // 按照距离原点的平方排序（避免开方运算）
        Arrays.sort(points, (a, b) -> 
            (a[0]*a[0] + a[1]*a[1]) - (b[0]*b[0] + b[1]*b[1])
        );
        
        // 返回前k个点
        return Arrays.copyOfRange(points, 0, k);
    }
    
    /**
     * 方法2：最小堆法
     * 时间复杂度：O(n log k)
     * 空间复杂度：O(k)
     */
    public int[][] kClosestHeap(int[][] points, int k) {
        // 使用最大堆，保持堆大小为k
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> 
            (b[0]*b[0] + b[1]*b[1]) - (a[0]*a[0] + a[1]*a[1])
        );
        
        // 遍历所有点
        for (int[] point : points) {
            maxHeap.offer(point);
            // 如果堆大小超过k，移除最远的点
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }
        
        // 将堆中元素复制到结果数组
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        
        return result;
    }
    
    /**
     * 方法3：快速选择法
     * 时间复杂度：O(n) 平均情况
     * 空间复杂度：O(1)
     */
    public int[][] kClosestQuickSelect(int[][] points, int k) {
        quickSelect(points, 0, points.length - 1, k);
        return Arrays.copyOfRange(points, 0, k);
    }
    
    /**
     * 快速选择实现
     */
    private void quickSelect(int[][] points, int left, int right, int k) {
        if (left >= right) return;
        
        // 随机选择pivot以避免最坏情况
        Random random = new Random();
        int pivotIndex = left + random.nextInt(right - left + 1);
        swap(points, pivotIndex, right);
        
        // 分区操作
        int pivotDist = getDistance(points[right]);
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (getDistance(points[j]) <= pivotDist) {
                swap(points, i, j);
                i++;
            }
        }
        
        swap(points, i, right);
        
        // 递归处理
        if (i == k - 1) {
            return;
        } else if (i < k - 1) {
            quickSelect(points, i + 1, right, k);
        } else {
            quickSelect(points, left, i - 1, k);
        }
    }
    
    /**
     * 计算点到原点的距离的平方
     */
    private int getDistance(int[] point) {
        return point[0] * point[0] + point[1] * point[1];
    }
    
    /**
     * 交换数组中两个元素
     */
    private void swap(int[][] points, int i, int j) {
        int[] temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        LeetCode_973_KClosestPointsToOrigin solution = new LeetCode_973_KClosestPointsToOrigin();
        
        System.out.println("=== 测试 LeetCode 973. 最接近原点的K个点 ===");
        
        // 测试用例1
        int[][] points1 = {{1,1},{2,2},{3,3}};
        int k1 = 1;
        System.out.println("测试用例1:");
        System.out.println("点集: " + Arrays.deepToString(points1));
        System.out.println("k = " + k1);
        System.out.println("排序法结果: " + Arrays.deepToString(solution.kClosestSort(points1.clone(), k1)));
        System.out.println("堆法结果: " + Arrays.deepToString(solution.kClosestHeap(points1.clone(), k1)));
        System.out.println("快速选择法结果: " + Arrays.deepToString(solution.kClosestQuickSelect(points1.clone(), k1)));
        
        // 测试用例2
        int[][] points2 = {{3,3},{5,-1},{-2,4}};
        int k2 = 2;
        System.out.println("\n测试用例2:");
        System.out.println("点集: " + Arrays.deepToString(points2));
        System.out.println("k = " + k2);
        System.out.println("排序法结果: " + Arrays.deepToString(solution.kClosestSort(points2.clone(), k2)));
        System.out.println("堆法结果: " + Arrays.deepToString(solution.kClosestHeap(points2.clone(), k2)));
        System.out.println("快速选择法结果: " + Arrays.deepToString(solution.kClosestQuickSelect(points2.clone(), k2)));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        Random random = new Random(42);
        int n = 10000;
        int[][] points3 = new int[n][2];
        for (int i = 0; i < n; i++) {
            points3[i][0] = random.nextInt(10000) - 5000;
            points3[i][1] = random.nextInt(10000) - 5000;
        }
        int k3 = 100;
        
        long startTime = System.nanoTime();
        solution.kClosestSort(points3.clone(), k3);
        long endTime = System.nanoTime();
        System.out.println("排序法处理" + n + "个点选取" + k3 + "个最近点时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        solution.kClosestHeap(points3.clone(), k3);
        endTime = System.nanoTime();
        System.out.println("堆法处理" + n + "个点选取" + k3 + "个最近点时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        solution.kClosestQuickSelect(points3.clone(), k3);
        endTime = System.nanoTime();
        System.out.println("快速选择法处理" + n + "个点选取" + k3 + "个最近点时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}