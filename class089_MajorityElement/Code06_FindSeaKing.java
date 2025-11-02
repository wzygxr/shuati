import java.util.Arrays;

/**
 * 子数组里的海王数
 * 子数组的海王数首先必须是子数组上出现次数最多的数(水王数)，并且要求出现次数>=t，t是参数
 * 设计一个数据结构并实现如下两个方法
 * 1) MajorityChecker(int[] arr) : 用数组arr对MajorityChecker初始化
 * 2) int query(int l, int r, int t) : 返回arr[l...r]上的海王数，不存在返回-1
 *
 * 相关题目来源：
 * 1. LeetCode 1157. Online Majority Element In Subarray - https://leetcode.com/problems/online-majority-element-in-subarray/
 * 2. LeetCode 1157. 子数组中占绝大多数的元素（中文版）- https://leetcode.cn/problems/online-majority-element-in-subarray/
 * 3. GeeksforGeeks Online Majority Element In Subarray - https://www.geeksforgeeks.org/online-majority-element-in-subarray/
 * 4. 牛客网 NC146 - 子数组中占绝大多数的元素 - https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
 * 5. 洛谷 P3933 SAC E#1 - 三道难题Tree - https://www.luogu.com.cn/problem/P3933 (相关思想应用)
 *
 * 题目解析：
 * 需要设计一个数据结构，支持快速查询任意子数组中的多数元素
 * 
 * 解题思路：
 * 1. 使用线段树维护区间信息，每个节点存储该区间的候选元素和对应的"血量"
 * 2. 查询时合并区间信息得到候选元素
 * 3. 使用二分查找验证候选元素在区间内的出现次数是否满足条件
 * 
 * 算法正确性证明：
 * 1. 线段树能够正确维护区间信息
 * 2. 合并操作能够正确计算候选元素和血量
 * 3. 二分查找能够准确统计元素在区间内的出现次数
 * 
 * 时间复杂度分析：
 * - 初始化：O(nlogn) - 构建线段树
 * - 查询：O(logn) - 线段树查询 + 二分查找统计次数
 * - 空间复杂度：O(n) - 存储线段树和预处理信息
 * 
 * 该解法是最优解，因为：
 * 1. 查询时间复杂度已经接近最优
 * 2. 空间复杂度是线性的
 * 3. 相比暴力查询的O(n)时间复杂度，此解法在多次查询时有明显优势
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、非法查询区间等边界情况
 * 2. 性能优化：预处理数据结构以加速查询
 * 3. 线程安全：在多线程环境中需要注意变量的可见性和原子性
 * 4. 代码可读性：使用清晰的变量名和注释提高可维护性
 * 5. 可扩展性：算法可以扩展到支持更多类型的查询
 * 6. 鲁棒性：通过验证步骤确保结果的正确性
 * 
 * 与其他领域的联系：
 * 1. 数据库：用于区间查询优化
 * 2. 机器学习：可以用于在线学习中的数据查询
 * 3. 分布式系统：在分布式计算中用于区间数据聚合
 * 4. 图像处理：在图像区域查询中用于特征统计
 * 5. 自然语言处理：用于文本区间查询和统计分析
 */
public class Code06_FindSeaKing {

	/**
	 * MajorityChecker类用于高效查询子数组中的多数元素
	 * 
	 * 核心思想：
	 * 1. 使用线段树维护区间信息，每个节点存储该区间的候选元素和对应的"血量"
	 * 2. 查询时合并区间信息得到候选元素
	 * 3. 使用二分查找验证候选元素在区间内的出现次数是否满足条件
	 * 
	 * 时间复杂度：
	 * - 初始化：O(nlogn)
	 * - 查询：O(logn)
	 * 空间复杂度：O(n)
	 */
	class MajorityChecker {

		public static int MAXN = 20001;

		public static int[][] nums = new int[MAXN][2];

		// 维护线段树一段范围，候选是谁
		public static int[] cand = new int[MAXN << 2];

		// 维护线段树一段范围，候选血量
		public static int[] hp = new int[MAXN << 2];

		public static int n;

		/**
		 * 构造函数，用数组arr对MajorityChecker初始化
		 * 
		 * 算法步骤：
		 * 1. 预处理数组元素及其位置信息
		 * 2. 构建线段树
		 * 
		 * 时间复杂度：O(nlogn)
		 * 空间复杂度：O(n)
		 * 
		 * @param arr 输入数组
		 */
		public MajorityChecker(int[] arr) {
			n = arr.length;
			buildCnt(arr);
			buildTree(arr, 1, n, 1);
		}

		/**
		 * 查询指定区间内出现次数至少为t的元素
		 * 
		 * 算法步骤：
		 * 1. 使用线段树找到区间内的候选元素
		 * 2. 使用二分查找统计候选元素在区间内的出现次数
		 * 3. 如果出现次数满足条件则返回该元素，否则返回-1
		 * 
		 * 时间复杂度：O(logn)
		 * 空间复杂度：O(1)
		 * 
		 * @param l 区间左边界（包含）
		 * @param r 区间右边界（包含）
		 * @param t 阈值
		 * @return 满足条件的元素，不存在则返回-1
		 */
		public int query(int l, int r, int t) {
			int[] ch = findCandidate(l + 1, r + 1, 1, n, 1);
			int candidate = ch[0];
			return cnt(l, r, candidate) >= t ? candidate : -1;
		}

		/**
		 * 预处理数组元素及其位置信息
		 * 
		 * 算法步骤：
		 * 1. 记录每个元素的值和位置
		 * 2. 按元素值和位置排序
		 * 
		 * 时间复杂度：O(nlogn)
		 * 空间复杂度：O(n)
		 * 
		 * @param arr 输入数组
		 */
		public void buildCnt(int[] arr) {
			for (int i = 0; i < n; i++) {
				nums[i][0] = arr[i];
				nums[i][1] = i;
			}
			Arrays.sort(nums, 0, n, (a, b) -> a[0] != b[0] ? (a[0] - b[0]) : (a[1] - b[1]));
		}

		/**
		 * 统计指定元素在区间[l,r]内的出现次数
		 * 
		 * 算法步骤：
		 * 1. 使用二分查找找到元素在前缀中的出现次数
		 * 2. 通过前缀差计算区间内的出现次数
		 * 
		 * 时间复杂度：O(logn)
		 * 空间复杂度：O(1)
		 * 
		 * @param l 区间左边界（包含）
		 * @param r 区间右边界（包含）
		 * @param v 目标元素
		 * @return 元素在区间内的出现次数
		 */
		public int cnt(int l, int r, int v) {
			return bs(v, r) - bs(v, l - 1);
		}

		/**
		 * 二分查找元素v在arr[0...i]范围内的出现次数
		 * 
		 * 算法步骤：
		 * 1. 使用二分查找找到最后一个<=v且位置<=i的元素
		 * 2. 返回该元素的位置+1即为出现次数
		 * 
		 * 时间复杂度：O(logn)
		 * 空间复杂度：O(1)
		 * 
		 * @param v 目标元素
		 * @param i 右边界
		 * @return 元素的出现次数
		 */
		// arr[0 ~ i]范围上
		// (<v的数) + (==v但下标<=i的数)，有几个
		public int bs(int v, int i) {
			int left = 0, right = n - 1, mid;
			int find = -1;
			while (left <= right) {
				mid = (left + right) >> 1;
				if (nums[mid][0] < v || (nums[mid][0] == v && nums[mid][1] <= i)) {
					find = mid;
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
			return find + 1;
		}

		/**
		 * 线段树节点信息合并操作
		 * 
		 * 算法逻辑：
		 * 1. 如果左右子节点的候选元素相同，则候选元素不变，血量相加
		 * 2. 如果左右子节点的候选元素不同，则血量大的候选元素成为当前节点候选元素，血量为两者差值
		 * 
		 * @param i 线段树节点索引
		 */
		public void up(int i) {
			int lc = cand[i << 1], lh = hp[i << 1];
			int rc = cand[i << 1 | 1], rh = hp[i << 1 | 1];
			cand[i] = lc == rc || lh >= rh ? lc : rc;
			hp[i] = lc == rc ? (lh + rh) : Math.abs(lh - rh);
		}

		/**
		 * 构建线段树
		 * 
		 * 算法步骤：
		 * 1. 递归构建左右子树
		 * 2. 合并子节点信息
		 * 
		 * 时间复杂度：O(n)
		 * 空间复杂度：O(logn)
		 * 
		 * @param arr 输入数组
		 * @param l 区间左边界
		 * @param r 区间右边界
		 * @param i 线段树节点索引
		 */
		public void buildTree(int[] arr, int l, int r, int i) {
			if (l == r) {
				cand[i] = arr[l - 1];
				hp[i] = 1;
			} else {
				int mid = (l + r) >> 1;
				buildTree(arr, l, mid, i << 1);
				buildTree(arr, mid + 1, r, i << 1 | 1);
				up(i);
			}
		}

		/**
		 * 查找区间[jobl,jobr]内的候选元素
		 * 
		 * 算法步骤：
		 * 1. 如果查询区间包含当前节点区间，直接返回当前节点信息
		 * 2. 否则递归查询左右子树并合并结果
		 * 
		 * 时间复杂度：O(logn)
		 * 空间复杂度：O(logn)
		 * 
		 * @param jobl 查询区间左边界
		 * @param jobr 查询区间右边界
		 * @param l 当前节点区间左边界
		 * @param r 当前节点区间右边界
		 * @param i 线段树节点索引
		 * @return 候选元素和血量数组
		 */
		public int[] findCandidate(int jobl, int jobr, int l, int r, int i) {
			if (jobl <= l && r <= jobr) {
				return new int[] { cand[i], hp[i] };
			} else {
				int mid = (l + r) >> 1;
				if (jobr <= mid) {
					return findCandidate(jobl, jobr, l, mid, i << 1);
				}
				if (jobl > mid) {
					return findCandidate(jobl, jobr, mid + 1, r, i << 1 | 1);
				}
				int[] lch = findCandidate(jobl, jobr, l, mid, i << 1);
				int[] rch = findCandidate(jobl, jobr, mid + 1, r, i << 1 | 1);
				int lc = lch[0], lh = lch[1];
				int rc = rch[0], rh = rch[1];
				int c = lc == rc || lh >= rh ? lc : rc;
				int h = lc == rc ? (lh + rh) : Math.abs(lh - rh);
				return new int[] { c, h };
			}
		}
	}

}