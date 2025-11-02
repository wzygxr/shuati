package class072;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 用最少数量的箭引爆气球
 * 
 * 题目来源：LeetCode 452. 用最少数量的箭引爆气球
 * 题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
 * 题目描述：一些球形的气球贴在一堵用 XY 平面表示的墙面上。墙面上的气球记录在整数数组 points，
 * 其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
 * 你不知道气球的确切 y 坐标。一支弓箭可以沿着 x 轴从不同点完全垂直地射出。
 * 在坐标 x 处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，
 * 且满足 xstart ≤ x ≤ xend，则该气球会被引爆。可以射出的弓箭的数量没有限制。
 * 弓箭一旦被射出之后，可以无限地前进。给你一个数组 points，返回引爆所有气球所必须射出的最小弓箭数。
 * 
 * 算法思路：
 * 1. 这是一个区间重叠问题，可以使用贪心算法解决
 * 2. 按结束位置排序，优先选择结束位置早的区间
 * 3. 每次选择当前箭能射爆的最多气球
 * 4. 当遇到不重叠的区间时，需要增加一支箭
 * 
 * 时间复杂度：O(n*logn) - 排序时间复杂度
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 是否最优解：是，这是最优解法
 * 
 * 示例：
 * 输入: points = [[10,16],[2,8],[1,6],[7,12]]
 * 输出: 2
 * 解释: 气球可以用2支箭来爆破:
 * -在x=6处射出箭，击破气球[2,8]和[1,6]
 * -在x=11处射出箭，击破气球[10,16]和[7,12]
 * 
 * 输入: points = [[1,2],[3,4],[5,6],[7,8]]
 * 输出: 4
 * 解释: 每个气球都需要一支箭，总共需要4支箭。
 */

public class Code20_MinimumNumberOfArrowsToBurstBalloons {

    /**
     * 计算引爆所有气球所需的最小弓箭数
     * 
     * @param points 气球区间数组
     * @return 最小弓箭数
     */
    public static int findMinArrowShots(int[][] points) {
        if (points == null || points.length == 0) {
            return 0;
        }
        
        int n = points.length;
        
        // 按结束位置升序排序
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                // 使用Long比较避免整数溢出
                return Long.compare(a[1], b[1]);
            }
        });
        
        // 至少需要一支箭
        int arrows = 1;
        // 当前箭的位置（射在第一个气球的结束位置）
        long arrowPos = points[0][1];
        
        for (int i = 1; i < n; i++) {
            // 如果当前气球的开始位置 > 当前箭的位置，需要新的箭
            if ((long)points[i][0] > arrowPos) {
                arrows++;
                arrowPos = points[i][1];
            }
            // 否则当前箭可以射爆这个气球（不需要更新箭的位置）
        }
        
        return arrows;
    }

    /**
     * 使用开始位置排序的解法（用于对比）
     * 
     * 算法思路：
     * 1. 按开始位置排序
     * 2. 维护当前重叠区间的最小结束位置
     * 3. 当遇到不重叠的区间时，增加箭的数量
     * 
     * 时间复杂度：O(n*logn)
     * 空间复杂度：O(1)
     * 是否最优解：是，这也是正确的解法
     * 
     * @param points 气球区间数组
     * @return 最小弓箭数
     */
    public static int findMinArrowShotsByStart(int[][] points) {
        if (points == null || points.length == 0) {
            return 0;
        }
        
        int n = points.length;
        
        // 按开始位置升序排序
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        
        int arrows = 1;
        // 当前重叠区间的最小结束位置
        long minEnd = points[0][1];
        
        for (int i = 1; i < n; i++) {
            // 如果当前气球的开始位置 > 最小结束位置，需要新的箭
            if ((long)points[i][0] > minEnd) {
                arrows++;
                minEnd = points[i][1];
            } else {
                // 更新最小结束位置
                minEnd = Math.min(minEnd, points[i][1]);
            }
        }
        
        return arrows;
    }

    /**
     * 区间调度问题的通用解法
     * 
     * 算法思路：
     * 1. 将问题转化为求最大不重叠区间数
     * 2. 最小箭数 = 总区间数 - 最大不重叠区间数（错误思路）
     * 3. 实际上，最小箭数 = 最大不重叠区间数
     * 
     * 注意：这个思路是错误的，用于展示常见误区
     * 
     * @param points 气球区间数组
     * @return 最小弓箭数
     */
    public static int findMinArrowShotsWrong(int[][] points) {
        if (points == null || points.length == 0) {
            return 0;
        }
        
        int n = points.length;
        
        // 按结束位置排序
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Long.compare(a[1], b[1]);
            }
        });
        
        // 计算最大不重叠区间数
        int count = 1;
        long end = points[0][1];
        
        for (int i = 1; i < n; i++) {
            if ((long)points[i][0] > end) {
                count++;
                end = points[i][1];
            }
        }
        
        // 错误：最小箭数应该等于最大不重叠区间数，而不是总区间数减去最大不重叠区间数
        return n - count; // 这是错误的
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] points1 = {{10,16}, {2,8}, {1,6}, {7,12}};
        System.out.println("输入: [[10,16],[2,8],[1,6],[7,12]]");
        System.out.println("结束位置排序方法输出: " + findMinArrowShots(points1));
        System.out.println("开始位置排序方法输出: " + findMinArrowShotsByStart(points1));
        System.out.println("错误方法输出: " + findMinArrowShotsWrong(points1));
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例2
        int[][] points2 = {{1,2}, {3,4}, {5,6}, {7,8}};
        System.out.println("输入: [[1,2],[3,4],[5,6],[7,8]]");
        System.out.println("结束位置排序方法输出: " + findMinArrowShots(points2));
        System.out.println("开始位置排序方法输出: " + findMinArrowShotsByStart(points2));
        System.out.println("错误方法输出: " + findMinArrowShotsWrong(points2));
        System.out.println("期望: 4");
        System.out.println();
        
        // 测试用例3：单个气球
        int[][] points3 = {{1,2}};
        System.out.println("输入: [[1,2]]");
        System.out.println("结束位置排序方法输出: " + findMinArrowShots(points3));
        System.out.println("开始位置排序方法输出: " + findMinArrowShotsByStart(points3));
        System.out.println("错误方法输出: " + findMinArrowShotsWrong(points3));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例4：完全重叠的气球
        int[][] points4 = {{1,5}, {2,4}, {3,6}, {4,8}};
        System.out.println("输入: [[1,5],[2,4],[3,6],[4,8]]");
        System.out.println("结束位置排序方法输出: " + findMinArrowShots(points4));
        System.out.println("开始位置排序方法输出: " + findMinArrowShotsByStart(points4));
        System.out.println("错误方法输出: " + findMinArrowShotsWrong(points4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例5：边界情况，大数测试
        int[][] points5 = {{-2147483646, -2147483645}, {2147483646, 2147483647}};
        System.out.println("输入: [[-2147483646,-2147483645],[2147483646,2147483647]]");
        System.out.println("结束位置排序方法输出: " + findMinArrowShots(points5));
        System.out.println("开始位置排序方法输出: " + findMinArrowShotsByStart(points5));
        System.out.println("错误方法输出: " + findMinArrowShotsWrong(points5));
        System.out.println("期望: 2");
        System.out.println();
        
        // 性能测试
        int[][] largePoints = new int[10000][2];
        for (int i = 0; i < 10000; i++) {
            largePoints[i][0] = (int) (Math.random() * 1000000);
            largePoints[i][1] = largePoints[i][0] + (int) (Math.random() * 1000) + 1;
        }
        
        long startTime = System.currentTimeMillis();
        int result1 = findMinArrowShots(largePoints);
        long endTime = System.currentTimeMillis();
        System.out.println("结束位置排序方法处理10000个气球耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int result2 = findMinArrowShotsByStart(largePoints);
        endTime = System.currentTimeMillis();
        System.out.println("开始位置排序方法处理10000个气球耗时: " + (endTime - startTime) + "ms");
        System.out.println("两种方法结果是否一致: " + (result1 == result2));
    }
}