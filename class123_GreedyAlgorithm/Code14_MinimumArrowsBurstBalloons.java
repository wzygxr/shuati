package class092;

import java.util.Arrays;
import java.util.Comparator;

// 用最少数量的箭引爆气球
// 有一些球形气球贴在一堵用XY平面表示的墙面上。墙面上的气球记录在整数数组 points ，
// 其中points[i] = [xstart, xend]表示水平直径在xstart和xend之间的气球。你不知道气球的确切y坐标。
// 一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，
// 若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足  xstart ≤ x ≤ xend，则该气球会被引爆。
// 可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
// 我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/
public class Code14_MinimumArrowsBurstBalloons {

	/*
	 * 贪心算法解法
	 * 
	 * 核心思想：
	 * 1. 将所有气球按照右边界进行排序
	 * 2. 从第一个气球的右边界射出一支箭
	 * 3. 对于后续气球，如果它的左边界大于当前箭的位置，说明无法被当前箭引爆，需要再射一支箭
	 * 4. 更新箭的位置为当前气球的右边界
	 * 
	 * 时间复杂度：O(n log n) - 排序的时间复杂度为O(n log n)，遍历数组的时间复杂度为O(n)
	 * 空间复杂度：O(log n) - 排序所需的栈空间
	 * 
	 * 为什么这是最优解？
	 * 1. 贪心策略保证了每支箭尽可能多地引爆气球
	 * 2. 按右边界排序是关键，这样可以确保我们总是在尽可能远的位置射出箭，以覆盖更多可能的气球
	 * 3. 通过数学归纳法可以证明这种策略能得到全局最优解
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：空数组、单元素数组
	 * 2. 异常处理：处理可能的整数溢出问题
	 * 3. 可读性：变量命名清晰，注释详细
	 * 
	 * 算法调试技巧：
	 * 1. 可以通过打印排序后的气球数组来验证排序是否正确
	 * 2. 可以打印每一步选择的箭的位置和被引爆的气球
	 */

	public static int findMinArrowShots(int[][] points) {
		// 边界条件：如果没有气球，不需要射箭
		if (points == null || points.length == 0) {
			return 0;
		}

		// 边界条件：如果只有一个气球，只需要一支箭
		if (points.length == 1) {
			return 1;
		}

		// 按照气球的右边界进行排序
		// 使用lambda表达式或自定义Comparator都可以
		// 注意：这里使用Integer.compare而不是直接相减，以避免整数溢出
		Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

		// 初始化箭的数量为1，第一支箭的位置为第一个气球的右边界
		int arrows = 1;
		int arrowPos = points[0][1];

		// 遍历所有气球
		for (int i = 1; i < points.length; i++) {
			// 如果当前气球的左边界大于箭的位置，说明无法被当前箭引爆
			if (points[i][0] > arrowPos) {
				// 需要再射一支箭
				arrows++;
				// 更新箭的位置为当前气球的右边界
				arrowPos = points[i][1];
			}
			// 否则，当前气球可以被之前的箭引爆，不需要额外射箭
		}

		return arrows;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: [[10,16],[2,8],[1,6],[7,12]] -> 2
		int[][] points1 = {{10, 16}, {2, 8}, {1, 6}, {7, 12}};
		System.out.println("测试用例1: " + Arrays.deepToString(points1));
		System.out.println("预期结果: 2, 实际结果: " + findMinArrowShots(points1));

		// 测试用例2: [[1,2],[3,4],[5,6],[7,8]] -> 4
		int[][] points2 = {{1, 2}, {3, 4}, {5, 6}, {7, 8}};
		System.out.println("测试用例2: " + Arrays.deepToString(points2));
		System.out.println("预期结果: 4, 实际结果: " + findMinArrowShots(points2));

		// 测试用例3: [[1,2],[2,3],[3,4],[4,5]] -> 2
		int[][] points3 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
		System.out.println("测试用例3: " + Arrays.deepToString(points3));
		System.out.println("预期结果: 2, 实际结果: " + findMinArrowShots(points3));

		// 测试用例4: [] -> 0
		int[][] points4 = {};
		System.out.println("测试用例4: " + Arrays.deepToString(points4));
		System.out.println("预期结果: 0, 实际结果: " + findMinArrowShots(points4));

		// 测试用例5: [[1,2]] -> 1
		int[][] points5 = {{1, 2}};
		System.out.println("测试用例5: " + Arrays.deepToString(points5));
		System.out.println("预期结果: 1, 实际结果: " + findMinArrowShots(points5));

		// 测试用例6: [[-2147483648,2147483647]] -> 1
		int[][] points6 = {{Integer.MIN_VALUE, Integer.MAX_VALUE}};
		System.out.println("测试用例6: [[-2147483648,2147483647]]");
		System.out.println("预期结果: 1, 实际结果: " + findMinArrowShots(points6));
	}
}