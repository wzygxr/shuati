package class023;

// 随机快速排序，acm练习风格
// 测试链接 : https://www.luogu.com.cn/problem/P1177
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

/*
 * 快速排序算法详解与实战 - 全面指南
 * 
 * 一、算法核心原理
 * 快速排序是基于分治思想的高效排序算法，通过选择基准元素，将数组分为两部分，递归排序实现。
 * 时间复杂度：最好/平均O(n log n)，最坏O(n²)，空间复杂度：O(log n)
 * 
 * 二、优化策略总结
 * 1. 随机化基准选择 - 避免最坏情况
 * 2. 三路快排 - 处理重复元素
 * 3. 小数组插入排序优化 - 减少递归开销
 * 4. 尾递归优化 - 降低栈空间使用
 * 5. 三数取中法 - 选择更优基准
 * 
 * 三、详尽题目列表与解决方案
 * 
 * 1. LeetCode 912. 排序数组
 *    链接: https://leetcode.cn/problems/sort-an-array/
 *    题目描述: 给你一个整数数组 nums，请你将该数组升序排列。
 *    最优解: 随机化快速排序 - 避免最坏情况的O(n²)复杂度
 *    时间复杂度: O(n log n)，空间复杂度: O(log n)
 * 
 * 2. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *    最优解: 快速选择算法 - 平均O(n)时间复杂度
 *    时间复杂度: 平均O(n)，最坏O(n²)，空间复杂度: O(log n)
 * 
 * 3. 剑指 Offer 40. 最小的k个数
 *    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
 *    最优解: 快速选择算法或堆排序
 *    时间复杂度: 平均O(n)，空间复杂度: O(k)或O(log n)
 * 
 * 4. LeetCode 75. 颜色分类
 *    链接: https://leetcode.cn/problems/sort-colors/
 *    题目描述: 给定一个包含红色、白色和蓝色的数组，原地排序使得相同颜色相邻，按红、白、蓝顺序排列。
 *    最优解: 三路快排思想 - 一次遍历完成排序
 *    时间复杂度: O(n)，空间复杂度: O(1)
 * 
 * 5. LeetCode 283. 移动零
 *    链接: https://leetcode.cn/problems/move-zeroes/
 *    题目描述: 将所有0移动到数组末尾，保持非零元素相对顺序不变。
 *    最优解: 双指针（分区思想）- 一次遍历完成
 *    时间复杂度: O(n)，空间复杂度: O(1)
 * 
 * 6. LeetCode 347. 前K个高频元素
 *    链接: https://leetcode.cn/problems/top-k-frequent-elements/
 *    题目描述: 返回数组中出现频率前k高的元素。
 *    最优解: 哈希表+快速选择 - 不需要完全排序
 *    时间复杂度: O(n)，空间复杂度: O(n)
 * 
 * 7. LeetCode 973. 最接近原点的K个点
 *    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 *    题目描述: 返回距离原点最近的K个点。
 *    最优解: 快速选择 - 基于距离排序
 *    时间复杂度: 平均O(n)，空间复杂度: O(log n)
 * 
 * 8. LeetCode 324. 摆动排序II
 *    链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *    题目描述: 重排数组使得nums[0] < nums[1] > nums[2] < nums[3]...
 *    最优解: 快速选择找中位数 + 三路划分
 *    时间复杂度: O(n)，空间复杂度: O(n)
 * 
 * 9. LeetCode 414. 第三大的数
 *    链接: https://leetcode.cn/problems/third-maximum-number/
 *    题目描述: 返回数组中第三大的数，如果不存在则返回最大数。
 *    最优解: 一次遍历维护三个最大值
 *    时间复杂度: O(n)，空间复杂度: O(1)
 * 
 * 10. LeetCode 462. 最少移动次数使数组元素相等II
 *     链接: https://leetcode.cn/problems/minimum-moves-to-equal-array-elements-ii/
 *     题目描述: 返回使所有元素相等的最少移动次数。
 *     最优解: 快速选择找中位数
 *     时间复杂度: O(n)，空间复杂度: O(log n)
 * 
 * 11. LeetCode 703. 数据流中的第K大元素
 *     链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述: 设计一个类来找到数据流中第K大元素。
 *     最优解: 最小堆维护前K大元素
 *     时间复杂度: O(log k) per add，空间复杂度: O(k)
 * 
 * 12. 牛客网 - 快速排序
 *     链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
 *     题目描述: 实现快速排序算法
 * 
 * 13. PAT 1101 Quick Sort
 *     链接: https://pintia.cn/problem-sets/994805342720868352/problems/994805366343188480
 *     题目描述: 找出所有满足条件的主元（左边都比它小、右边都比它大）
 *     最优解: 预处理左右边界最大值数组
 *     时间复杂度: O(n)，空间复杂度: O(n)
 * 
 * 14. 洛谷 P1177 【模板】快速排序
 *     链接: https://www.luogu.com.cn/problem/P1177
 *     题目描述: 利用快速排序算法将读入的N个数从小到大排序后输出
 * 
 * 15. Codeforces 401C. Team
 *     链接: https://codeforces.com/problemset/problem/401/C
 *     题目描述: 构造一个01序列，满足特定约束条件
 * 
 * 16. AtCoder ABC121C. Energy Drink Collector
 *     链接: https://atcoder.jp/contests/abc121/tasks/abc121_c
 *     题目描述: 购买能量饮料以获得最少总花费
 *     最优解: 按价格排序后贪心选择
 * 
 * 17. HackerRank - QuickSort 1 - Partition
 *     链接: https://www.hackerrank.com/challenges/quicksort1/problem
 *     题目描述: 实现快速排序的分区操作
 * 
 * 18. HackerRank - QuickSort 2 - Sorting
 *     链接: https://www.hackerrank.com/challenges/quicksort2/problem
 *     题目描述: 实现完整的快速排序算法
 * 
 * 19. ZOJ 2581 Random Walking
 *     链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367080
 *     题目描述: 随机游走问题，需要排序预处理数据
 * 
 * 20. SPOJ - SORT1 - Sorting Test
 *     链接: https://www.spoj.com/problems/SORT1/
 *     题目描述: 基本排序问题，测试排序算法效率
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序基准测试
 * 
 * 21. UVa 10152 - ShellSort
 *     链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1093
 *     题目描述: 实现一种特殊的排序算法，与快速排序思想有关
 *     时间复杂度: O(n log n)，空间复杂度: O(n)
 *     最优解: 特殊排序算法
 * 
 * 22.杭电 OJ 1425. sort
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=1425
 *     题目描述: 对整数数组进行快速排序
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序或堆排序
 * 
 * 23. POJ 2388. Who's in the Middle
 *     链接: http://poj.org/problem?id=2388
 *     题目描述: 找出一组数的中位数，快速选择的经典应用
 *     时间复杂度: O(n) 平均，空间复杂度: O(log n)
 *     最优解: 快速选择算法找中位数
 * 
 * 24. AizuOJ ALDS1_6_C. Quick Sort
 *     链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_6_C
 *     题目描述: 实现快速排序算法并输出每一步的分区结果
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序算法实现
 * 
 * 25. Comet OJ Contest 11 E. 快速排序
 *     链接: https://cometoj.com/contest/59/problem/E?problem_id=2830
 *     题目描述: 快速排序相关的概率问题
 *     时间复杂度: O(n log n)，空间复杂度: O(log n)
 *     最优解: 快速排序相关的概率问题
 * 
 * 26. LeetCode 169. 多数元素
 *     链接: https://leetcode.cn/problems/majority-element/
 *     题目描述: 给定一个大小为 n 的数组，找到其中的多数元素
 *     时间复杂度: O(n)，空间复杂度: O(1)
 *     最优解: Boyer-Moore投票算法（与快速选择思想相关）
 * 
 * 27. LeetCode 229. 求众数 II
 *     链接: https://leetcode.cn/problems/majority-element-ii/
 *     题目描述: 找出数组中所有出现次数超过 ⌊ n/3 ⌋ 的元素
 *     时间复杂度: O(n)，空间复杂度: O(1)
 *     最优解: 摩尔投票法扩展
 * 
 * 28. LeetCode 274. H 指数
 *     链接: https://leetcode.cn/problems/h-index/
 *     题目描述: 计算研究人员的 h 指数
 *     时间复杂度: O(n) 平均，空间复杂度: O(n)
 *     最优解: 计数排序或快速选择
 * 
 * 29. LeetCode 378. 有序矩阵中第K小的元素
 *     链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
 *     题目描述: 给定一个 n x n 矩阵，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素
 *     时间复杂度: O(n log(max-min))，空间复杂度: O(1)
 *     最优解: 二分查找 + 计数
 * 
 * 四、工程化深度考量
 * 1. 异常处理: 空数组、null输入、单元素数组等边界情况
 * 2. 多线程安全: 排序算法在并发环境中的使用注意事项
 * 3. 内存优化: 原地排序减少额外空间开销
 * 4. 缓存友好性: 优化数据访问模式，提高缓存命中率
 * 5. 单元测试: 全面覆盖各种输入场景
 * 6. 性能监控: 针对大规模数据的性能退化检测
 * 
 * 五、跨语言实现差异
 * 1. Java: 数组作为对象，有边界检查，使用Math.random()生成随机数
 * 2. C++: 数组为指针，无边界检查，性能更高，使用rand()生成随机数
 * 3. Python: 使用列表，动态类型，使用random模块，语法简洁但性能较低
 * 
 * 六、调试与优化技巧
 * 1. 断点式打印: 输出关键变量变化过程
 * 2. 断言验证: 使用assert验证分区的正确性
 * 3. 性能分析: 使用profiler找出瓶颈
 * 4. 边界测试: 空数组、单元素、重复元素、有序/逆序数组
 * 5. 常数优化: 减少不必要的操作和判断
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_QuickSort {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int n;

	/**
	 * 主函数，程序入口
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 创建高效的输入输出流
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度
		in.nextToken();
		n = (int) in.nval;
		
		// 读取数组元素
		for (int i = 0; i < n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		
		// 调用改进版快速排序算法对数组进行排序
		quickSort2(0, n - 1);
		
		// 输出排序后的数组
		for (int i = 0; i < n - 1; i++) {
			out.print(arr[i] + " ");
		}
		out.println(arr[n - 1]);
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 随机快速排序经典版(不推荐)
	 * 甚至在洛谷上测试因为递归开太多层会爆栈导致出错
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSort1(int l, int r) {
		// l == r，只有一个数
		// l > r，范围不存在，不用管
		if (l >= r) {
			return;
		}
		
		// 随机这一下，常数时间比较大
		// 但只有这一下随机，才能在概率上把快速排序的时间复杂度收敛到O(n * logn)
		// l......r 随机选一个位置，x这个值，做划分
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		
		// 调用partition1函数进行分区操作，返回等于x区域的右边界
		int mid = partition1(l, r, x);
		
		// 递归处理小于等于x的区域
		quickSort1(l, mid - 1);
		
		// 递归处理大于x的区域
		quickSort1(mid + 1, r);
	}

	/**
	 * 分区函数 - 经典版本
	 * 将数组划分为两个部分：小于等于x的部分和大于x的部分
	 * 并确保划分完成后小于等于x区域的最后一个数字是x
	 * @param l 分区区间的左边界（包含）
	 * @param r 分区区间的右边界（包含）
	 * @param x 基准值
	 * @return 等于x区域的右边界索引
	 */
	public static int partition1(int l, int r, int x) {
		// a表示小于等于x区域的右边界下一个位置
		// xi记录在小于等于x区域内任意一个x的位置
		int a = l, xi = 0;
		
		// 遍历整个区间
		for (int i = l; i <= r; i++) {
			// 如果当前元素小于等于基准值x
			if (arr[i] <= x) {
				// 将当前元素交换到小于等于x区域
				swap(a, i);
				
				// 如果交换过来的元素正好等于x，则记录其位置
				if (arr[a] == x) {
					xi = a;
				}
				
				// 小于等于x区域向右扩展一位
				a++;
			}
		}
		
		// 将记录的x位置的元素与小于等于x区域的最后一个元素交换
		// 确保小于等于x区域的最后一个数字是x
		swap(xi, a - 1);
		
		// 返回等于x区域的右边界索引
		return a - 1;
	}

	/**
	 * 交换数组中两个位置的元素
	 * @param i 第一个位置
	 * @param j 第二个位置
	 */
	public static void swap(int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 随机快速排序改进版(推荐)
	 * 使用三路快排优化，更好地处理重复元素
	 * 可以通过所有测试用例
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSort2(int l, int r) {
		// 递归终止条件：当左边界大于等于右边界时，表示区间内没有元素或只有一个元素，无需排序
		if (l >= r) {
			return;
		}
		
		// 随机选择基准值pivot，避免最坏情况的发生
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		
		// 调用partition2函数进行三路分区操作
		partition2(l, r, x);
		
		// 为了防止底层的递归过程覆盖全局变量
		// 这里用临时变量记录等于x区域的左右边界
		int left = first;
		int right = last;
		
		// 递归处理小于x的区域
		quickSort2(l, left - 1);
		
		// 递归处理大于x的区域
		quickSort2(right + 1, r);
	}
	
	/**
	 * 【优化版本1】小数组插入排序优化
	 * 当数组长度小于阈值时，使用插入排序，减少递归开销
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSortOptimized(int l, int r) {
		// 小数组阈值，经验值为10-20
		final int INSERTION_SORT_THRESHOLD = 15;
		
		// 对小数组使用插入排序
		if (r - l <= INSERTION_SORT_THRESHOLD) {
			insertionSort(l, r);
			return;
		}
		
		// 对大数组继续使用快速排序
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		partition2(l, r, x);
		int left = first;
		int right = last;
		quickSortOptimized(l, left - 1);
		quickSortOptimized(right + 1, r);
	}
	
	/**
	 * 插入排序算法，用于小数组优化
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	private static void insertionSort(int l, int r) {
		// 从第二个元素开始，逐个插入到已排序序列中
		for (int i = l + 1; i <= r; i++) {
			int key = arr[i]; // 当前要插入的元素
			int j = i - 1;    // 已排序序列的最后一个位置
			
			// 在已排序序列中找到合适的插入位置
			while (j >= l && arr[j] > key) {
				arr[j + 1] = arr[j]; // 元素后移
				j--;
			}
			
			// 插入元素
			arr[j + 1] = key;
		}
	}
	
	/**
	 * 【优化版本2】尾递归优化
	 * 将尾递归转换为迭代，减少栈空间使用
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSortTailRecursive(int l, int r) {
		// 使用循环代替尾递归
		while (l < r) {
			int x = arr[l + (int) (Math.random() * (r - l + 1))];
			partition2(l, r, x);
			int left = first;
			int right = last;
			
			// 优先处理较小的子数组，减少递归深度
			if (left - l < r - right) {
				// 左边子数组较小，先递归处理左边
				quickSortTailRecursive(l, left - 1);
				// 尾递归优化：将右边子数组的处理转换为迭代
				l = right + 1;
			} else {
				// 右边子数组较小，先递归处理右边
				quickSortTailRecursive(right + 1, r);
				// 尾递归优化：将左边子数组的处理转换为迭代
				r = left - 1;
			}
		}
	}
	
	/**
	 * 【优化版本3】三数取中法选择基准
	 * 选择左、中、右三个位置的中位数作为基准，提高最坏情况性能
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSortMedianOfThree(int l, int r) {
		// 递归终止条件
		if (l >= r) {
			return;
		}
		
		// 三数取中选择基准
		int mid = l + ((r - l) >> 1); // 中间位置
		int x = medianOfThree(l, mid, r);
		
		// 进行三路分区
		partition2(l, r, x);
		int left = first;
		int right = last;
		
		// 递归处理左右子数组
		quickSortMedianOfThree(l, left - 1);
		quickSortMedianOfThree(right + 1, r);
	}
	
	/**
	 * 三数取中辅助方法
	 * @param a 左边界索引
	 * @param b 中间索引
	 * @param c 右边界索引
	 * @return 三个位置元素的中位数
	 */
	private static int medianOfThree(int a, int b, int c) {
		// 确保arr[a] <= arr[b] <= arr[c]
		if (arr[a] > arr[b]) swap(a, b);
		if (arr[b] > arr[c]) swap(b, c);
		if (arr[a] > arr[b]) swap(a, b);
		return arr[b]; // 返回中位数
	}
	
	/**
	 * 【快速选择算法】用于LeetCode 215等第K大/小问题
	 * 平均时间复杂度O(n)，最坏O(n²)
	 * @param l 搜索区间的左边界（包含）
	 * @param r 搜索区间的右边界（包含）
	 * @param k 目标位置索引
	 * @return 第k小的元素
	 */
	public static int quickSelect(int l, int r, int k) {
		// 当区间只有一个元素时，就是要找的位置
		if (l == r) {
			return arr[l];
		}
		
		// 随机选择基准值
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		
		// 进行三路分区
		partition2(l, r, x);
		int left = first;
		int right = last;
		
		// 根据目标位置决定处理哪个子区间
		if (k < left) {
			// 目标位置在左半部分
			return quickSelect(l, left - 1, k);
		} else if (k > right) {
			// 目标位置在右半部分
			return quickSelect(right + 1, r, k);
		} else {
			// 目标位置在等于区域，直接返回
			return arr[k];
		}
	}
	
	/**
	 * 【LeetCode 215解法】数组中的第K个最大元素
	 * @param nums 输入数组
	 * @param k 第k大的元素
	 * @return 第k大的元素值
	 */
	public static int findKthLargest(int[] nums, int k) {
		// 注意：第K大元素等价于第(nums.length - k)小元素
		int n = nums.length;
		// 复制到全局数组进行操作
		System.arraycopy(nums, 0, arr, 0, n);
		return quickSelect(0, n - 1, n - k);
	}
	
	/**
	 * 【剑指Offer 40解法】最小的k个数
	 * @param nums 输入数组
	 * @param k 需要返回的最小元素个数
	 * @return 包含最小k个数的数组
	 */
	public static int[] getLeastNumbers(int[] nums, int k) {
		// 边界条件处理
		if (k <= 0) return new int[0];
		if (k >= nums.length) return nums;
		
		int n = nums.length;
		System.arraycopy(nums, 0, arr, 0, n);
		
		// 使用快速选择找到第k小的元素
		quickSelect(0, n - 1, k - 1);
		
		// 收集前k个最小元素
		int[] result = new int[k];
		System.arraycopy(arr, 0, result, 0, k);
		return result;
	}
	
	/**
	 * 【LeetCode 75解法】颜色分类（三路快排应用）
	 * @param nums 包含0、1、2的数组，分别代表红、白、蓝三种颜色
	 */
	public static void sortColors(int[] nums) {
		// 三路快排思想：0放左边，1放中间，2放右边
		int zero = -1;      // [0...zero] == 0
		int two = nums.length; // [two...n-1] == 2
		int i = 0;          // 当前处理的位置
		
		while (i < two) {
			if (nums[i] == 0) {
				// 当前元素为0，交换到0区域的下一个位置
				zero++;
				swap(nums, zero, i);
				i++;
			} else if (nums[i] == 1) {
				// 当前元素为1，保持在中间区域
				i++;
			} else {
				// 当前元素为2，交换到2区域的前一个位置
				two--;
				swap(nums, i, two);
				// 注意这里i不增加，因为交换过来的元素还未处理
			}
		}
	}
	
	/**
	 * 辅助交换方法（用于传入的数组）
	 * @param nums 数组
	 * @param i 第一个位置
	 * @param j 第二个位置
	 */
	private static void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}
	
	/**
	 * 【LeetCode 283解法】移动零（分区思想应用）
	 * @param nums 输入数组
	 */
	public static void moveZeroes(int[] nums) {
		// 双指针：将非零元素移动到数组前面
		int nonZeroPos = 0; // 指向下一个非零元素应该放的位置
		
		// 第一次遍历：将所有非零元素移到前面
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] != 0) {
				if (i != nonZeroPos) {
					swap(nums, i, nonZeroPos);
				}
				nonZeroPos++;
			}
		}
	}
	
	/**
	 * 【LeetCode 462解法】最少移动次数使数组元素相等II（中位数思想）
	 * @param nums 输入数组
	 * @return 最少移动次数
	 */
	public static int minMoves2(int[] nums) {
		int n = nums.length;
		System.arraycopy(nums, 0, arr, 0, n);
		
		// 找到中位数
		int median = quickSelect(0, n - 1, n / 2);
		
		// 计算所有元素到中位数的距离和
		int moves = 0;
		for (int num : nums) {
			moves += Math.abs(num - median);
		}
		return moves;
	}
	
	/**
	 * 【调试辅助方法】打印数组内容
	 * @param nums 要打印的数组
	 */
	public static void printArray(int[] nums) {
		for (int num : nums) {
			System.out.print(num + " ");
		}
		System.out.println();
	}
	
	/**
	 * 【测试验证方法】检查数组是否有序
	 * @param nums 要检查的数组
	 * @return 是否有序
	 */
	public static boolean isSorted(int[] nums) {
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] < nums[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 【LeetCode 912解法】排序数组（标准快速排序实现）
	 * @param nums 待排序数组
	 * @return 排序后的数组
	 */
	public static int[] sortArray(int[] nums) {
		int n = nums.length;
		System.arraycopy(nums, 0, arr, 0, n);
		quickSortOptimized(0, n - 1); // 使用优化版本的快速排序
		int[] result = new int[n];
		System.arraycopy(arr, 0, result, 0, n);
		return result;
	}
	
	/**
	 * 【HackerRank Partition解法】快速排序分区操作
	 * @param nums 待分区数组
	 */
	public static void quickSortPartition(int[] nums) {
		int pivot = nums[0]; // 选择第一个元素作为基准
		int n = nums.length;
		int[] result = new int[n];
		int left = 0;
		int right = n - 1;
		
		// 从右到左填充小于基准的元素
		for (int i = n - 1; i >= 0; i--) {
			if (nums[i] < pivot) {
				result[left++] = nums[i];
			}
		}
		
		// 填充基准元素
		result[left++] = pivot;
		
		// 从左到右填充大于等于基准的元素
		for (int i = 1; i < n; i++) {
			if (nums[i] >= pivot) {
				result[right--] = nums[i];
			}
		}
		
		// 复制回原数组
		System.arraycopy(result, 0, nums, 0, n);
	}
	
	/**
	 * 【剑指Offer 51解法】数组中的逆序对（归并排序应用，与快速排序对比）
	 * 注意：虽然本题更适合用归并排序解决，但我们这里提供实现作为对比
	 * @param nums 输入数组
	 * @return 逆序对数量
	 */
	private static int count = 0; // 记录逆序对数量
	public static int reversePairs(int[] nums) {
		count = 0;
		int n = nums.length;
		int[] temp = new int[n];
		mergeSort(nums, 0, n - 1, temp);
		return count;
	}
	
	/**
	 * 归并排序辅助方法（用于逆序对计算）
	 * @param nums 待排序数组
	 * @param left 左边界
	 * @param right 右边界
	 * @param temp 临时数组
	 */
	private static void mergeSort(int[] nums, int left, int right, int[] temp) {
		if (left < right) {
			int mid = left + ((right - left) >> 1);
			mergeSort(nums, left, mid, temp);
			mergeSort(nums, mid + 1, right, temp);
			merge(nums, left, mid, right, temp);
		}
	}
	
	/**
	 * 合并过程中计算逆序对
	 * @param nums 待合并数组
	 * @param left 左边界
	 * @param mid 中间位置
	 * @param right 右边界
	 * @param temp 临时数组
	 */
	private static void merge(int[] nums, int left, int mid, int right, int[] temp) {
		int i = left;
		int j = mid + 1;
		int k = 0;
		
		while (i <= mid && j <= right) {
			if (nums[i] <= nums[j]) {
				temp[k++] = nums[i++];
			} else {
				// 计算逆序对：nums[i...mid]都与nums[j]构成逆序对
				count += (mid - i + 1);
				temp[k++] = nums[j++];
			}
		}
		
		// 处理剩余元素
		while (i <= mid) {
			temp[k++] = nums[i++];
		}
		while (j <= right) {
			temp[k++] = nums[j++];
		}
		
		// 复制回原数组
		k = 0;
		while (left <= right) {
			nums[left++] = temp[k++];
		}
	}
	
	/**
	 * 【洛谷 P1177 解法】快速排序模板题
	 * @param n 数组长度
	 * @param arr 待排序数组
	 */
	public static void luoguP1177(int n, int[] arr) {
		// 直接使用优化版本的快速排序
		quickSortOptimized(0, n - 1);
	}
	
	/**
	 * 【POJ 2388 解法】Who's in the Middle（中位数问题）
	 * @param nums 输入数组
	 * @return 中位数
	 */
	public static int findMedian(int[] nums) {
		int n = nums.length;
		System.arraycopy(nums, 0, arr, 0, n);
		// 中位数就是第 (n-1)/2 小的元素（0-based索引）
		return quickSelect(0, n - 1, (n - 1) / 2);
	}
	
	/**
	 * 【工程化应用】外部排序辅助函数
	 * 当数据量很大，无法全部加载到内存时，使用分块快速排序
	 * @param chunk 内存中的数据块
	 */
	public static void externalSortHelper(int[] chunk) {
		// 对内存中的数据块进行快速排序
		quickSortOptimized(0, chunk.length - 1);
	}
	
	/**
	 * 【异常处理】健壮的快速排序实现
	 * @param nums 待排序数组
	 */
	public static void robustQuickSort(int[] nums) {
		// 空数组检查
		if (nums == null || nums.length <= 1) {
			return;
		}
		
		// 执行排序
		System.arraycopy(nums, 0, arr, 0, nums.length);
		quickSortOptimized(0, nums.length - 1);
		System.arraycopy(arr, 0, nums, 0, nums.length);
	}
	
	/**
	 * 【AtCoder ABC121C 解法】Energy Drink Collector（贪心+排序）
	 * @param drinks 能量饮料信息，每行包含价格和数量
	 * @param budget 预算
	 * @return 能获得的最大能量
	 */
	public static long energyDrinkCollector(int[][] drinks, int budget) {
		// 按价格升序排序
		java.util.Arrays.sort(drinks, (a, b) -> a[0] - b[0]);
		
		long totalEnergy = 0;
		int remainingBudget = budget;
		
		for (int[] drink : drinks) {
			int price = drink[0]; // 价格
			int amount = drink[1]; // 数量
			
			int canBuy = Math.min(amount, remainingBudget / price);
			totalEnergy += canBuy;
			remainingBudget -= canBuy * price;
			
			if (remainingBudget < price) {
				break;
			}
		}
		
		return totalEnergy;
	}
	
	/**
	 * 【Codeforces 401C 解法】Team（贪心构造）
	 * @param n 0的数量
	 * @param m 1的数量
	 * @return 构造的01序列
	 */
	public static String constructTeam(int n, int m) {
		// 构造一个01序列，满足特定约束条件
		StringBuilder sb = new StringBuilder();
		
		while (n > 0 || m > 0) {
			// 优先放置0的情况
			if (n > m) {
				// 检查是否可以放置00
				if (n >= 2 && m >= 1) {
					sb.append("001");
					n -= 2;
					m -= 1;
				} else {
					sb.append("0");
					n -= 1;
				}
			} else {
				// 优先放置1的情况
				if (m >= 2 && n >= 1) {
					sb.append("110");
					m -= 2;
					n -= 1;
				} else {
					sb.append("1");
					m -= 1;
				}
			}
		}
		
		return sb.toString();
	}

	// 荷兰国旗问题
	public static int first, last;

	/**
	 * 三路分区函数 - 改进版本
	 * 将数组划分为三个部分：<x的部分、=x的部分、>x的部分
	 * 更新全局变量first和last为=x区域的左右边界
	 * @param l 分区区间的左边界（包含）
	 * @param r 分区区间的右边界（包含）
	 * @param x 基准值
	 */
	public static void partition2(int l, int r, int x) {
		// 初始化等于x区域的左右边界
		first = l;
		last = r;
		
		// 当前处理的元素索引
		int i = l;
		
		// 当i不超过等于区域的右边界时继续循环
		while (i <= last) {
			// 如果当前元素等于基准值x
			if (arr[i] == x) {
				// 直接移动到下一个元素
				i++;
			} 
			// 如果当前元素小于基准值x
			else if (arr[i] < x) {
				// 将当前元素与等于区域左边界的元素交换
				swap(first++, i++);
			} 
			// 如果当前元素大于基准值x
			else {
				// 将当前元素与等于区域右边界的元素交换
				// 注意这里i不自增，因为交换过来的元素还未处理
				swap(i, last--);
			}
		}
	}

}