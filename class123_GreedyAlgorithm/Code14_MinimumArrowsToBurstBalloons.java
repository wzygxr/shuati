package class092;

import java.util.Arrays;
import java.util.Comparator;

/**
 * LeetCode 452. 用最少数量的箭引爆气球
 * 题目链接：https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
 * 难度：中等
 * 
 * 问题描述：
 * 有一些球形气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组 points ，其中points[i] = [xstart, xend] 表示水平直径在xstart和xend之间的气球。
 * 你不知道气球的确切y坐标。一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足 xstart ≤ x ≤ xend，则该气球会被引爆。
 * 可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
 * 
 * 解题思路：
 * 贪心算法 + 区间排序
 * 1. 按气球的右边界进行排序
 * 2. 每次尽可能用一支箭引爆最多的气球
 * 3. 维护当前箭的位置为当前气球的右边界
 * 4. 遍历所有气球，如果当前气球的左边界大于箭的位置，则需要一支新箭，并更新箭的位置
 * 
 * 时间复杂度分析：
 * - 排序的时间复杂度：O(n log n)，其中n是气球的数量
 * - 遍历的时间复杂度：O(n)
 * - 总的时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 排序所需的额外空间：O(log n)
 * - 总的空间复杂度：O(log n)
 * 
 * 最优性证明：
 * 这是一个区间调度问题的变种。通过按右边界排序，我们能够保证每次选择的箭尽可能多地引爆气球，从而得到最优解。
 * 因为如果有一个更优的解使用更少的箭，那么在某个区间一定存在一支箭可以同时引爆更多的气球，这与我们的贪心策略矛盾。
 */
public class Code14_MinimumArrowsToBurstBalloons {
    
    /**
     * 计算引爆所有气球所需的最小箭数
     * @param points 气球的坐标数组，每个元素为[xstart, xend]
     * @return 最小箭数
     */
    public int findMinArrowShots(int[][] points) {
        // 处理边界情况
        if (points == null || points.length == 0) {
            return 0;
        }
        if (points.length == 1) {
            return 1;
        }
        
        // 按气球的右边界排序
        // 注意：这里使用Integer.compare避免溢出问题
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
        
        // 初始化箭数为1，第一支箭的位置为第一个气球的右边界
        int arrows = 1;
        int currentEnd = points[0][1];
        
        // 遍历所有气球
        for (int i = 1; i < points.length; i++) {
            // 如果当前气球的左边界大于箭的位置，需要一支新箭
            if (points[i][0] > currentEnd) {
                arrows++;
                // 更新箭的位置为当前气球的右边界
                currentEnd = points[i][1];
            }
            // 否则，当前箭可以引爆这个气球，不需要额外操作
        }
        
        return arrows;
    }
    
    /**
     * 测试代码
     */
    public static void main(String[] args) {
        Code14_MinimumArrowsToBurstBalloons solution = new Code14_MinimumArrowsToBurstBalloons();
        
        // 测试用例1：基本用例
        int[][] points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
        System.out.println("测试用例1结果：" + solution.findMinArrowShots(points1)); // 预期输出：2
        
        // 测试用例2：无重叠的气球
        int[][] points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
        System.out.println("测试用例2结果：" + solution.findMinArrowShots(points2)); // 预期输出：4
        
        // 测试用例3：完全重叠的气球
        int[][] points3 = {{1, 5}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("测试用例3结果：" + solution.findMinArrowShots(points3)); // 预期输出：1
        
        // 测试用例4：边界情况 - 空数组
        int[][] points4 = {};
        System.out.println("测试用例4结果：" + solution.findMinArrowShots(points4)); // 预期输出：0
        
        // 测试用例5：边界情况 - 单气球
        int[][] points5 = {{1, 2}};
        System.out.println("测试用例5结果：" + solution.findMinArrowShots(points5)); // 预期输出：1
        
        // 测试用例6：负数坐标
        int[][] points6 = {{-10, -5}, {-8, -3}, {-6, 0}, {-4, 2}};
        System.out.println("测试用例6结果：" + solution.findMinArrowShots(points6)); // 预期输出：2
    }
}

/*
工程化考量：
1. 边界条件处理：
   - 空数组返回0
   - 单元素数组返回1

2. 异常处理：
   - 输入参数验证在main方法中进行
   - 在findMinArrowShots方法中对null和空数组进行检查

3. 性能优化：
   - 使用Integer.compare避免整数溢出问题
   - 排序后只需一次遍历

4. 代码可读性：
   - 变量命名清晰：arrows表示箭数，currentEnd表示当前箭的位置
   - 注释详细：解释了算法思路、时间空间复杂度和最优性证明

5. 测试用例：
   - 包含基本用例
   - 包含边界情况
   - 包含特殊情况（负数坐标）

6. 算法变种思考：
   - 如果气球是三维空间中的，如何求解？
   - 如果箭有射程限制，如何调整算法？
   - 如果气球有不同的价值，要求用最少的箭获得最大价值，如何求解？

7. 与其他算法的对比：
   - 贪心算法在这个问题上比动态规划更高效
   - 时间复杂度比暴力解法的O(2^n)要好得多

8. 调试技巧：
   - 可以在排序后打印数组，验证排序是否正确
   - 在遍历过程中打印currentEnd和箭数的变化，观察算法执行过程
*/