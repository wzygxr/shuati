package class048;

import java.util.Arrays;

/**
 * 最强祝福力场问题
 * 
 * 问题描述：
 * 小扣在探索丛林的过程中，无意间发现了传说中"落寞的黄金之都"。
 * 而在这片建筑废墟的地带中，小扣使用探测仪监测到了存在某种带有「祝福」效果的力场。
 * 经过不断的勘测记录，小扣将所有力场的分布都记录了下来。
 * forceField[i] = [x,y,side] 表示第 i 片力场将覆盖以坐标 (x,y) 为中心，边长为 side 的正方形区域。
 * 若任意一点的 力场强度 等于覆盖该点的力场数量。
 * 请求出在这片地带中 力场强度 最强处的 力场强度。
 * 注意：力场范围的边缘同样被力场覆盖。
 * 
 * 核心思想：
 * 1. 利用离散化处理浮点数坐标
 * 2. 利用二维差分数组统计重叠区域
 * 3. 通过前缀和还原得到最大重叠次数
 * 
 * 算法详解：
 * 1. 坐标离散化：将所有力场的边界坐标收集并去重排序
 * 2. 差分标记：对每个力场区域，在离散化后的差分数组中进行标记
 * 3. 前缀和还原：通过二维前缀和将差分数组还原并找出最大值
 * 
 * 时间复杂度分析：
 * O(n²)，其中n为力场数量
 * - 收集坐标：O(n)
 * - 排序去重：O(n log n)
 * - 差分标记：O(n)
 * - 前缀和还原：O(n²)
 * 
 * 空间复杂度分析：
 * O(n²)，用于存储差分数组
 * 
 * 算法优势：
 * 1. 通过离散化处理浮点数坐标，避免精度问题
 * 2. 利用差分数组高效处理区域更新
 * 3. 时间复杂度已达到常见解决方案的较优水平
 * 
 * 工程化考虑：
 * 1. 数据类型选择：使用long避免整数溢出
 * 2. 离散化处理：处理浮点数坐标精度问题
 * 3. 边界处理：扩展数组边界避免特殊判断
 * 
 * 应用场景：
 * 1. 地理信息系统中的区域重叠统计
 * 2. 图像处理中的区域特征提取
 * 3. 游戏开发中的区域影响计算
 * 
 * 相关题目：
 * 1. LeetCode LCP 74. 最强祝福力场
 * 2. Codeforces 816B - Karen and Coffee
 * 3. AtCoder ABC106D - AtCoder Express 2
 * 
 * 测试链接 : https://leetcode.cn/problems/xepqZ5/
 */
public class Code05_StrongestForceField {

	/**
	 * 计算力场强度最强处的力场强度
	 * 
	 * 算法思路：
	 * 1. 收集所有力场的边界坐标并离散化
	 * 2. 利用二维差分数组标记力场覆盖区域
	 * 3. 通过二维前缀和还原并找出最大重叠次数
	 * 
	 * 关键技术点：
	 * 1. 坐标离散化：处理浮点数坐标，避免精度问题
	 * 2. 差分数组：高效处理区域更新操作
	 * 
	 * @param fields 力场分布数组，fields[i] = [x,y,side]
	 * @return 最强力场强度
	 */
	// 时间复杂度O(n^2)，额外空间复杂度O(n^2)，n是力场的个数
	public static int fieldOfGreatestBlessing(int[][] fields) {
		int n = fields.length;
		
		// n : 矩形的个数，x 2*n个坐标
		long[] xs = new long[n << 1];  // x方向坐标最多就是2n个
		long[] ys = new long[n << 1];  // y方向坐标最多就是2n个
		
		// 收集所有力场的边界坐标
		for (int i = 0, k = 0, p = 0; i < n; i++) {
			long x = fields[i][0];
			long y = fields[i][1];
			long r = fields[i][2];
			// 将坐标和边长乘以2，避免浮点数运算
			// 左边界坐标
			xs[k++] = (x << 1) - r;
			// 右边界坐标
			xs[k++] = (x << 1) + r;
			// 下边界坐标
			ys[p++] = (y << 1) - r;
			// 上边界坐标
			ys[p++] = (y << 1) + r;
		}
		
		// 对坐标数组进行排序并去重，返回有效长度
		int sizex = sort(xs);
		int sizey = sort(ys);
		
		// 创建差分数组
		// n个力场，sizex : 2 * n, sizey : 2 * n
		int[][] diff = new int[sizex + 2][sizey + 2];
		
		// 对每个力场，在差分数组中进行标记
		for (int i = 0, a, b, c, d; i < n; i++) {
			long x = fields[i][0];
			long y = fields[i][1];
			long r = fields[i][2];
			// 获取离散化后的坐标
			a = rank(xs, (x << 1) - r, sizex);
			b = rank(ys, (y << 1) - r, sizey);
			c = rank(xs, (x << 1) + r, sizex);
			d = rank(ys, (y << 1) + r, sizey);
			// 在差分数组中标记力场区域
			add(diff, a, b, c, d);
		}
		
		int ans = 0;
		// 通过二维前缀和还原差分数组，并找出最大值
		// O(n^2)
		for (int i = 1; i < diff.length; i++) {
			for (int j = 1; j < diff[0].length; j++) {
				diff[i][j] += diff[i - 1][j] + diff[i][j - 1] - diff[i - 1][j - 1];
				ans = Math.max(ans, diff[i][j]);
			}
		}
		return ans;
	}

	/**
	 * 对坐标数组进行排序并去重
	 * 
	 * 算法原理：
	 * 1. 对数组进行排序
	 * 2. 遍历数组，保留不重复的元素
	 * 3. 返回去重后的有效长度
	 * 
	 * 时间复杂度：O(n log n)
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 坐标数组
	 * @return 去重后的有效长度
	 */
	// [50,70,30,70,30,60] 长度6
	// [30,30,50,60,70,70]
	// [30,50,60,70] 60 -> 3
	//  1  2  3  4
	// 长度4，
 	public static int sort(long[] nums) {
		// 对坐标数组进行排序
		Arrays.sort(nums);
		int size = 1;
		// 去除重复元素
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] != nums[size - 1]) {
				nums[size++] = nums[i];
			}
		}
		return size;
	}

	/**
	 * 在有序数组中查找值对应的排名（离散化后的索引）
	 * 
	 * 算法原理：
	 * 使用二分查找在有序数组中找到值v的插入位置
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 * 
	 * @param nums 有序数组
	 * @param v 要查找的值
	 * @param size 数组有效长度
	 * @return 值v在数组中的排名（从1开始）
	 */
 	// nums 有序数组，有效长度是size，0~size-1范围上无重复值
 	// 已知v一定在nums[0~size-1]，返回v所对应的编号
	public static int rank(long[] nums, long v, int size) {
		int l = 0;
		int r = size - 1;
		int m, ans = 0;
		// 二分查找
		while (l <= r) {
			m = (l + r) / 2;
			if (nums[m] >= v) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		// 返回排名（从1开始）
		return ans + 1;
	}

	/**
	 * 在二维差分数组中标记区域更新
	 * 
	 * 算法原理：
	 * 对区域[(a,b),(c,d)]增加1，在差分数组中进行标记：
	 * 1. diff[a][b] += 1
	 * 2. diff[c+1][d+1] += 1
	 * 3. diff[c+1][b] -= 1
	 * 4. diff[a][d+1] -= 1
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * @param diff 差分数组
	 * @param a 区域左上角行索引
	 * @param b 区域左上角列索引
	 * @param c 区域右下角行索引
	 * @param d 区域右下角列索引
	 */
	// 二维差分
	public static void add(int[][] diff, int a, int b, int c, int d) {
		diff[a][b] += 1;
		diff[c + 1][d + 1] += 1;
		diff[c + 1][b] -= 1;
		diff[a][d + 1] -= 1;
	}

}