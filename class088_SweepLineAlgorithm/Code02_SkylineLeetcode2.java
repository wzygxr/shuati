package class115;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 天际线问题 (LeetCode 218)
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 题目描述:
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 * 
 * 解题思路:
 * 使用扫描线算法结合自定义最大堆实现天际线问题的求解。
 * 1. 将建筑物的左右边界作为事件点
 * 2. 使用离散化技术处理坐标值
 * 3. 使用自定义最大堆维护当前活动建筑物的高度
 * 4. 扫描过程中记录高度变化的关键点
 * 
 * 时间复杂度: O(n log n) - 排序和堆操作
 * 空间复杂度: O(n) - 存储事件和堆
 * 
 * 工程化考量:
 * 1. 异常处理: 检查建筑物数据合法性
 * 2. 边界条件: 处理建筑物边界重叠情况
 * 3. 性能优化: 使用自定义堆减少系统开销
 * 4. 可读性: 详细注释和模块化设计
 */
public class Code02_SkylineLeetcode2 {

	/**
	 * 求解天际线问题
	 * 使用自定义实现的最大堆，并结合离散化优化
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
		
		// 扫描线算法处理每个离散化后的坐标点
		for (int i = 1, j = 0; i <= m; i++) {
			// 将起始位置小于等于当前点的所有建筑物加入堆中
			for (; j < n && arr[j][0] <= i; j++) {
				push(arr[j][2], arr[j][1]);
			}
			
			// 移除堆中结束位置小于当前点的建筑物
			while (!isEmpty() && peekEnd() < i) {
				poll();
			}
			
			// 当前点的最大高度即为堆顶元素的高度
			if (!isEmpty()) {
				height[i] = peekHeight();
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

	// 自定义堆数组，每个元素为[高度, 结束位置]
	public static int[][] heap = new int[MAXN][2];

	// 堆大小
	public static int heapSize;

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

	/**
	 * 检查堆是否为空
	 * 
	 * @return 如果堆为空返回true，否则返回false
	 */
	public static boolean isEmpty() {
		return heapSize == 0;
	}

	/**
	 * 获取堆顶元素的高度
	 * 
	 * @return 堆顶元素的高度
	 */
	public static int peekHeight() {
		return heap[0][0];
	}

	/**
	 * 获取堆顶元素的结束位置
	 * 
	 * @return 堆顶元素的结束位置
	 */
	public static int peekEnd() {
		return heap[0][1];
	}

	/**
	 * 向堆中添加元素
	 * 
	 * @param h 建筑物高度
	 * @param e 建筑物结束位置
	 */
	public static void push(int h, int e) {
		heap[heapSize][0] = h;
		heap[heapSize][1] = e;
		heapInsert(heapSize++);
	}

	/**
	 * 移除堆顶元素
	 */
	public static void poll() {
		swap(0, --heapSize);
		heapify(0);
	}

	/**
	 * 堆插入操作（上浮）
	 * 由于是最大堆，高度大的元素需要上浮
	 * 
	 * @param i 要上浮的元素索引
	 */
	public static void heapInsert(int i) {
		// 当前元素的高度大于父节点高度时，需要上浮
		while (heap[i][0] > heap[(i - 1) / 2][0]) {
			swap(i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	/**
	 * 堆化操作（下沉）
	 * 由于是最大堆，高度小的元素需要下沉
	 * 
	 * @param i 要下沉的元素索引
	 */
	public static void heapify(int i) {
		int l = i * 2 + 1; // 左子节点索引
		
		// 当存在子节点时继续下沉
		while (l < heapSize) {
			// 找到左右子节点中高度较大的节点索引
			int best = l + 1 < heapSize && heap[l + 1][0] > heap[l][0] ? l + 1 : l;
			
			// 比较当前节点与较大子节点，确定是否需要交换
			best = heap[best][0] > heap[i][0] ? best : i;
			
			// 如果当前节点已经是最大的，则停止下沉
			if (best == i) {
				break;
			}
			
			// 交换节点并继续下沉
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	/**
	 * 交换堆中两个元素
	 * 
	 * @param i 第一个元素索引
	 * @param j 第二个元素索引
	 */
	public static void swap(int i, int j) {
		int[] tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

}
