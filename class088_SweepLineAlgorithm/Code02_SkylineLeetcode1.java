package class115;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 天际线问题 (LeetCode 218)
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 题目描述:
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 * 
 * 解题思路:
 * 使用扫描线算法结合系统提供的PriorityQueue实现天际线问题的求解。
 * 1. 将建筑物的左右边界作为事件点
 * 2. 使用离散化技术处理坐标值
 * 3. 使用最大堆维护当前活动建筑物的高度
 * 4. 扫描过程中记录高度变化的关键点
 * 
 * 时间复杂度: O(n log n) - 排序和堆操作
 * 空间复杂度: O(n) - 存储事件和堆
 * 
 * 工程化考量:
 * 1. 异常处理: 检查建筑物数据合法性
 * 2. 边界条件: 处理建筑物边界重叠情况
 * 3. 性能优化: 使用离散化减少坐标范围
 * 4. 可读性: 详细注释和模块化设计
 */
public class Code02_SkylineLeetcode1 {

	/**
	 * 求解天际线问题
	 * 使用系统提供的PriorityQueue作为最大堆，并结合离散化优化
	 * 
	 * @param arr 建筑物数组，每个元素为[left, right, height]
	 * @return 天际线的关键点列表，每个元素为[x, y]
	 */
	public static List<List<Integer>> getSkyline(int[][] arr) {
		// 边界条件检查
		if (arr == null) {
			throw new IllegalArgumentException("建筑物数组不能为空");
		}
		
		int n = arr.length;
		int m = prepare(arr, n);
		
		// 使用最大堆维护当前活动建筑物的高度
		// 堆中元素: [高度, 结束位置]
		// 按高度降序排列，高度大的在堆顶
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
		
		// 扫描线算法处理每个离散化后的坐标点
		for (int i = 1, j = 0; i <= m; i++) {
			// 将起始位置小于等于当前点的所有建筑物加入堆中
			for (; j < n && arr[j][0] <= i; j++) {
				heap.add(new int[] { arr[j][2], arr[j][1] });
			}
			
			// 移除堆中结束位置小于当前点的建筑物
			while (!heap.isEmpty() && heap.peek()[1] < i) {
				heap.poll();
			}
			
			// 当前点的最大高度即为堆顶元素的高度
			if (!heap.isEmpty()) {
				height[i] = heap.peek()[0];
			}
		}
		
		// 构造结果列表
		List<List<Integer>> ans = new ArrayList<>();
		for (int i = 1, pre = 0; i <= m; i++) {
			// 如果高度发生变化，记录关键点
			if (pre != height[i]) {
				ans.add(Arrays.asList(xsort[i], height[i]));
			}
			pre = height[i];
		}
		
		return ans;
	}

	// 最大数组容量
	public static int MAXN = 100001;

	// 离散化后的坐标值数组
	public static int[] xsort = new int[MAXN];

	// 每个离散化坐标点对应的高度
	public static int[] height = new int[MAXN];

	/**
	 * 准备工作：对坐标进行离散化处理
	 * 1) 收集大楼左边界、右边界-1、右边界的值
	 * 2) 收集的所有值排序、去重
	 * 3) 大楼的左边界和右边界，修改成排名值
	 * 4) 大楼根据左边界排序
	 * 5) 清空height数组
	 * 6) 返回离散值的个数
	 * 
	 * @param arr 建筑物数组
	 * @param n 建筑物数量
	 * @return 离散化后的坐标点数量
	 */
	public static int prepare(int[][] arr, int n) {
		int size = 0;
		
		// 收集所有需要离散化的坐标值
		// 包括大楼的左边界、右边界-1、右边界
		for (int i = 0; i < n; i++) {
			xsort[++size] = arr[i][0];      // 左边界
			xsort[++size] = arr[i][1] - 1;  // 右边界-1
			xsort[++size] = arr[i][1];      // 右边界
		}
		
		// 对收集到的坐标值进行排序
		Arrays.sort(xsort, 1, size + 1);
		
		// 排序后去重，得到m个不同的坐标值
		int m = 1;
		for (int i = 1; i <= size; i++) {
			if (xsort[m] != xsort[i]) {
				xsort[++m] = xsort[i];
			}
		}
		
		// 将建筑物的左右边界修改为对应的排名值
		for (int i = 0; i < n; i++) {
			arr[i][0] = rank(m, arr[i][0]);        // 左边界
			arr[i][1] = rank(m, arr[i][1] - 1);    // 右边界-1
		}
		
		// 所有建筑物根据左边界排序
		Arrays.sort(arr, 0, n, (a, b) -> a[0] - b[0]);
		
		// 清空高度数组
		Arrays.fill(height, 1, m + 1, 0);
		
		// 返回离散化后的坐标点数量
		return m;
	}

	/**
	 * 查询数值v在离散化数组中的排名(离散值)
	 * 使用二分查找优化查询效率
	 * 
	 * @param n 离散化数组的有效长度
	 * @param v 要查询的数值
	 * @return 数值v在离散化数组中的排名
	 */
	public static int rank(int n, int v) {
		int ans = 0;
		int l = 1, r = n, mid;
		
		// 二分查找第一个大于等于v的位置
		while (l <= r) {
			mid = (l + r) >> 1;
			if (xsort[mid] >= v) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		
		return ans;
	}

}
