package class090;

import java.util.Arrays;

// 用最少数量的箭引爆气球
// 有一些球形气球贴在一堵用 XY 平面表示的墙面上。
// 墙面上的气球记录在整数数组 points ，其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
// 你不知道气球的确切 y 坐标。
// 一支弓箭可以沿着 x 轴从不同点完全垂直地射出。
// 在坐标 x 处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，
// 且满足 xstart ≤ x ≤ xend，则该气球会被引爆。
// 可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
// 给你一个数组 points ，返回引爆所有气球所必须射出的最小弓箭数。
// 测试链接: https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
public class Code15_MinimumNumberOfArrowsToBurstBalloons {

    /**
     * 用最少数量的箭引爆气球问题的贪心解法
     * 
     * 解题思路：
     * 1. 将气球按右端点升序排序
     * 2. 贪心策略：尽可能多地引爆重叠的气球
     * 3. 遍历排序后的气球，如果当前气球与前一个气球不重叠，则需要增加一支箭
     * 
     * 贪心策略的正确性：
     * 局部最优：当气球出现重叠时，一起射，所用弓箭最少
     * 全局最优：把所有气球射爆所用弓箭最少
     * 
     * 时间复杂度：O(n log n)，主要消耗在排序上
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param points 气球坐标数组
     * @return 引爆所有气球所需的最小弓箭数
     */
    public static int findMinArrowShots(int[][] points) {
        // 边界条件处理：如果没有气球，则不需要任何箭
        if (points == null || points.length == 0) {
            return 0;
        }

        // 1. 按气球右端点升序排序
        Arrays.sort(points, (a, b) -> {
            // 防止整数溢出
            if (a[1] > b[1]) return 1;
            if (a[1] < b[1]) return -1;
            return 0;
        });

        // 2. 初始化变量
        int arrows = 1;           // 弓箭数，至少需要一支箭
        int end = points[0][1];   // 当前箭能射到的最远位置

        // 3. 从第二个气球开始遍历
        for (int i = 1; i < points.length; i++) {
            // 4. 如果当前气球与前一个气球不重叠
            if (points[i][0] > end) {
                arrows++;           // 需要增加一支箭
                end = points[i][1]; // 更新箭能射到的最远位置
            }
            // 5. 如果重叠，则当前箭可以引爆这个气球，不需要额外操作
        }

        // 6. 返回所需的最小弓箭数
        return arrows;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: points = [[10,16],[2,8],[1,6],[7,12]]
        // 输出: 2
        // 解释: 气球可以用2支箭来爆破:
        // - 在x = 6处射出箭，击破气球[2,8]和[1,6]。
        // - 在x = 11处发射箭，击破气球[10,16]和[7,12]。
        int[][] points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
        System.out.println("测试用例1结果: " + findMinArrowShots(points1)); // 期望输出: 2

        // 测试用例2
        // 输入: points = [[1,2],[3,4],[5,6],[7,8]]
        // 输出: 4
        // 解释: 每个气球需要单独的一支箭
        int[][] points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        System.out.println("测试用例2结果: " + findMinArrowShots(points2)); // 期望输出: 4

        // 测试用例3
        // 输入: points = [[1,2],[2,3],[3,4],[4,5]]
        // 输出: 2
        // 解释: 气球可以用2支箭来爆破:
        // - 在x = 2处发射箭，击破气球[1,2]和[2,3]。
        // - 在x = 4处发射箭，击破气球[3,4]和[4,5]。
        int[][] points3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("测试用例3结果: " + findMinArrowShots(points3)); // 期望输出: 2

        // 测试用例4：边界情况
        // 输入: points = []
        // 输出: 0
        int[][] points4 = {};
        System.out.println("测试用例4结果: " + findMinArrowShots(points4)); // 期望输出: 0

        // 测试用例5：复杂情况
        // 输入: points = [[1,2],[2,3],[3,4],[4,5],[5,6],[6,7],[7,8],[8,9],[9,10],[10,11]]
        // 输出: 5
        int[][] points5 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {7, 8}, {8, 9}, {9, 10}, {10, 11}};
        System.out.println("测试用例5结果: " + findMinArrowShots(points5)); // 期望输出: 5
    }
}