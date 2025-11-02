package class129;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * 寻找最近和次近
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
 * 关于近的定义，距离的定义如下:
 * 对i位置的数字x来说，只关注右侧的数字，和x的差值绝对值越小就越近
 * 距离为差值绝对值，如果距离一样，数值越小的越近
 * 
 * 解题思路：
 * 这是一个寻找最近邻元素的问题，可以使用两种不同的方法解决。
 * 
 * 方法一：使用TreeSet（有序表）
 * 1. 从右向左遍历数组
 * 2. 对于每个元素，使用TreeSet查找最近和次近的元素
 * 3. 更新结果数组
 * 
 * 方法二：使用双向链表
 * 1. 将数组元素按值排序，建立双向链表
 * 2. 从左向右遍历原数组
 * 3. 对于每个元素，在双向链表中查找最近和次近的元素
 * 4. 删除当前元素，避免影响后续查找
 * 
 * 时间复杂度：
 * - TreeSet方法：O(n * log n)
 * - 双向链表方法：O(n * log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 220. 存在重复元素 III (TreeSet滑动窗口)
 * 2. LeetCode 219. 存在重复元素 II (哈希表滑动窗口)
 * 3. LeetCode 480. 滑动窗口中位数
 * 4. LeetCode 992. K 个不同整数的子数组
 * 5. LeetCode 76. 最小覆盖子串
 * 6. LeetCode 3. 无重复字符的最长子串
 * 7. LintCode 363. 接雨水
 * 8. HackerRank - Sliding Window Median
 * 9. Codeforces 372C. Watching Fireworks is Fun
 * 10. AtCoder ABC134F. Permutation Oddness
 * 11. 牛客网 NC123. 滑动窗口的最大值
 * 12. 杭电OJ 6827. Master of Subgraph
 * 13. POJ 2823. Sliding Window
 * 14. UVa 11572. Unique Snowflakes
 * 15. CodeChef - CHEFCOMP
 * 
 * 工程化考量：
 * 1. 在实际应用中，这类算法常用于：
 *    - 推荐系统中的相似用户查找
 *    - 数据分析中的近邻匹配
 *    - 游戏中的碰撞检测
 * 2. 实现优化：
 *    - 对于大规模数据，可以考虑使用KD树等空间分割数据结构
 *    - 可以使用并行处理加速计算
 *    - 对于多次查询，可以预处理更多数据以加速查询
 * 3. 可扩展性：
 *    - 支持动态添加和删除元素
 *    - 处理多维数据的近邻查找
 *    - 扩展到K近邻问题
 * 4. 鲁棒性考虑：
 *    - 处理重复元素的情况
 *    - 处理大规模数据时的内存管理
 *    - 优化极端情况下的性能
 */
public class Code04_FindNear {

	public static int MAXN = 10001;

	public static int[] arr = new int[MAXN];

	public static int n;

	public static int[] to1 = new int[MAXN];

	public static int[] dist1 = new int[MAXN];

	public static int[] to2 = new int[MAXN];

	public static int[] dist2 = new int[MAXN];

	// 如下三个数组只有near2方法需要
	public static int[][] rank = new int[MAXN][2];

	public static int[] last = new int[MAXN];

	public static int[] next = new int[MAXN];

	/**
	 * 使用TreeSet（有序表）实现寻找最近和次近元素
	 * 
	 * 算法步骤：
	 * 1. 从右向左遍历数组
	 * 2. 对于每个元素，使用TreeSet查找最近和次近的元素
	 * 3. 更新结果数组
	 */
	// 有序表的实现
	public static void near1() {
		TreeSet<int[]> set = new TreeSet<>((a, b) -> a[1] - b[1]);
		for (int i = n; i >= 1; i--) {
			to1[i] = 0;
			dist1[i] = 0;
			to2[i] = 0;
			dist2[i] = 0;
			int[] cur = new int[] { i, arr[i] };
			int[] p1 = set.floor(cur);
			int[] p2 = p1 != null ? set.floor(new int[] { p1[0], p1[1] - 1 }) : null;
			int[] p3 = set.ceiling(cur);
			int[] p4 = p3 != null ? set.ceiling(new int[] { p3[0], p3[1] + 1 }) : null;
			update(i, p1 != null ? p1[0] : 0);
			update(i, p2 != null ? p2[0] : 0);
			update(i, p3 != null ? p3[0] : 0);
			update(i, p4 != null ? p4[0] : 0);
			set.add(cur);
		}
	}

	/**
	 * 使用双向链表实现寻找最近和次近元素
	 * 
	 * 算法步骤：
	 * 1. 将数组元素按值排序，建立双向链表
	 * 2. 从左向右遍历原数组
	 * 3. 对于每个元素，在双向链表中查找最近和次近的元素
	 * 4. 删除当前元素，避免影响后续查找
	 */
	// 数组手搓双向链表的实现
	public static void near2() {
		for (int i = 1; i <= n; i++) {
			rank[i][0] = i;
			rank[i][1] = arr[i];
		}
		Arrays.sort(rank, 1, n + 1, (a, b) -> a[1] - b[1]);
		rank[0][0] = 0;
		rank[n + 1][0] = 0;
		for (int i = 1; i <= n; i++) {
			last[rank[i][0]] = rank[i - 1][0];
			next[rank[i][0]] = rank[i + 1][0];
		}
		for (int i = 1; i <= n; i++) {
			to1[i] = 0;
			dist1[i] = 0;
			to2[i] = 0;
			dist2[i] = 0;
			update(i, last[i]);
			update(i, last[last[i]]);
			update(i, next[i]);
			update(i, next[next[i]]);
			delete(i);
		}
	}

	/**
	 * 更新i位置的最近和次近元素
	 * 
	 * @param i 当前位置
	 * @param j 候选位置
	 */
	// i位置右侧的j位置
	// 看看能不能更新i右侧的最近或者次近
	// 如果j==0则不更新
	public static void update(int i, int j) {
		if (j == 0) {
			return;
		}
		int dist = Math.abs(arr[i] - arr[j]);
		if (to1[i] == 0 || dist < dist1[i] || (dist == dist1[i] && arr[j] < arr[to1[i]])) {
			to2[i] = to1[i];
			dist2[i] = dist1[i];
			to1[i] = j;
			dist1[i] = dist;
		} else if (to2[i] == 0 || dist < dist2[i] || (dist == dist2[i] && arr[j] < arr[to2[i]])) {
			to2[i] = j;
			dist2[i] = dist;
		}
	}

	/**
	 * 在双向链表中删除指定位置的元素
	 * 
	 * @param i 要删除的位置
	 */
	// 双向链表中删掉i位置
	public static void delete(int i) {
		int l = last[i];
		int r = next[i];
		if (l != 0) {
			next[l] = r;
		}
		if (r != 0) {
			last[r] = l;
		}
	}

	// 随机生成arr[1...n]确保没有重复数值
	// 为了测试
	public static void random(int v) {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 1, cur; i <= n; i++) {
			do {
				cur = (int) (Math.random() * v * 2) - v;
			} while (set.contains(cur));
			set.add(cur);
			arr[i] = cur;
		}
	}

	// 如下四个数组用来做备份
	public static int[] a = new int[MAXN];

	public static int[] b = new int[MAXN];

	public static int[] c = new int[MAXN];

	public static int[] d = new int[MAXN];

	// 验证的过程
	// 为了测试
	public static boolean check() {
		// near1方法会设置to1、dist1、to2、dist2
		near1();
		// 把near1方法的结果备份
		for (int i = 1; i <= n; i++) {
			a[i] = to1[i];
			b[i] = dist1[i];
			c[i] = to2[i];
			d[i] = dist2[i];
		}
		// near2方法会再次设置to1、dist1、to2、dist2
		near2();
		// a、b、c、d，是near1生成的结果
		// to1、dist1、to2、dist2，是near2生成的结果
		for (int i = 1; i <= n; i++) {
			if (a[i] != to1[i] || b[i] != dist1[i]) {
				return false;
			}
		}
		for (int i = 1; i <= n; i++) {
			if (c[i] != to2[i] || d[i] != dist2[i]) {
				return false;
			}
		}
		return true;
	}

	// 对数器
	// 为了测试
	public static void main(String[] args) {
		// 一定要确保arr中的数字无重复，所以让v大于n
		n = 100;
		int v = 500;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTime; i++) {
			random(v);
			if (!check()) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}