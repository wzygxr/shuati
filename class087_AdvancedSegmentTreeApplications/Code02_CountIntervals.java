package class114;

import java.util.Arrays;

/**
 * LeetCode 715. Range Module & LeetCode 2276. Count Integers in Intervals
 * 
 * 题目描述：
 * 实现CountIntervals类的如下三个方法
 * 1) CountIntervals() : 初始化
 * 2) void add(int l, int r) : 把[l, r]范围上的数字都设置成1
 * 3) int count() : 返回整个区间有多少个1
 * CountIntervals类需要支持1 ~ 10^9范围
 * 调用add和count方法总共10^5次
 * 
 * 解题思路：
 * 使用动态开点线段树维护区间覆盖状态。由于值域很大(10^9)，不能预先建立完整的线段树，
 * 需要按需创建节点。此题的特殊性在于只有设置为1的操作，没有设置为0的操作，
 * 因此可以进行优化。
 * 
 * 关键技术：
 * 1. 动态开点线段树：只在需要时创建节点
 * 2. 区间覆盖优化：已完全覆盖的区间无需继续递归
 * 3. 静态空间实现：避免频繁内存分配
 * 
 * 时间复杂度分析：
 * 1. add操作：O(log n) 均摊
 * 2. count操作：O(1)
 * 3. 空间复杂度：O(q log n)，其中q是操作次数
 * 
 * 是否最优解：是
 * 动态开点线段树是处理此类区间覆盖问题的最优解法
 * 
 * 工程化考量：
 * 1. 内存优化：静态数组避免频繁内存分配
 * 2. 性能优化：已覆盖区间提前返回，避免无效递归
 * 3. 边界处理：处理节点动态创建和查询边界情况
 * 
 * 题目链接：https://leetcode.cn/problems/count-integers-in-intervals/
 * 
 * @author Algorithm Journey
 * @version 1.0
 */
public class Code02_CountIntervals {

	// 开点线段树的实现
	// 为了所有语言的同学都容易改出来
	// 选择用静态空间的方式实现
	// 该方法的打败比例不高但是非常好想
	// 有兴趣的同学可以研究其他做法
	class CountIntervals {

		// 支持的最大范围
		public static int n = 1000000000;

		// 空间大小定成这个值是实验的结果
		public static int LIMIT = 700001;

		// 左子节点数组
		public static int[] left = new int[LIMIT];

		// 右子节点数组
		public static int[] right = new int[LIMIT];

		// 区间覆盖数量数组
		public static int[] sum = new int[LIMIT];

		// 当前使用的节点数
		public static int cnt = 1;

		/**
		 * 构造函数
		 * 初始化线段树
		 */
		public CountIntervals() {
			Arrays.fill(left, 1, cnt + 1, 0);
			Arrays.fill(right, 1, cnt + 1, 0);
			Arrays.fill(sum, 1, cnt + 1, 0);
			cnt = 1;
		}

		/**
		 * 向上更新节点信息
		 * 将左右子节点的信息合并到父节点
		 * 
		 * @param h 父节点索引
		 * @param l 左子节点索引
		 * @param r 右子节点索引
		 */
		public static void up(int h, int l, int r) {
			sum[h] = sum[l] + sum[r];
		}

		/**
		 * 区间设置为1的操作
		 * 将区间[jobl, jobr]内的所有数字都设置成1
		 * 
		 * 这个题的特殊性在于，只有改1的操作，没有改0的操作
		 * 理解这个就可以分析出不需要懒更新机制，原因有两个
		 * 1) 查询操作永远查的是整个范围1的数量，不会有小范围的查询，每次都返回sum[1]
		 *    这意味着只要能把sum[1]更新正确即可，up函数可以保证这一点
		 * 2) 一个范围已经全是1，那以后都会是1，没有必要把全是1的懒更新信息向下传递
		 * 这个函数的功能比线段树能做到的范围修改功能简单很多
		 * 功能有阉割就意味着存在优化的点
		 * 
		 * @param jobl 操作区间左端点
		 * @param jobr 操作区间右端点
		 * @param l    当前节点表示的区间左端点
		 * @param r    当前节点表示的区间右端点
		 * @param i    当前节点索引
		 */
		public static void setOne(int jobl, int jobr, int l, int r, int i) {
			// 如果当前区间已经完全覆盖，直接返回
			if (sum[i] == r - l + 1) {
				return;
			}
			// 如果当前区间完全被操作区间包含，直接设置为完全覆盖
			if (jobl <= l && r <= jobr) {
				sum[i] = r - l + 1;
			} else {
				int mid = (l + r) >> 1;
				if (jobl <= mid) {
					// 动态创建左子节点
					if (left[i] == 0) {
						left[i] = ++cnt;
					}
					setOne(jobl, jobr, l, mid, left[i]);
				}
				if (jobr > mid) {
					// 动态创建右子节点
					if (right[i] == 0) {
						right[i] = ++cnt;
					}
					setOne(jobl, jobr, mid + 1, r, right[i]);
				}
				// 向上更新节点信息
				up(i, left[i], right[i]);
			}
		}

		/**
		 * 添加区间
		 * 将区间[left, right]内的所有数字都设置成1
		 * 
		 * @param left  区间左端点
		 * @param right 区间右端点
		 */
		public void add(int left, int right) {
			setOne(left, right, 1, n, 1);
		}

		/**
		 * 统计区间中1的个数
		 * 
		 * @return 区间中1的个数
		 */
		public int count() {
			return sum[1];
		}
	}

}