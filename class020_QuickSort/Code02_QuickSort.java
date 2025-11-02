package class023;

// 随机快速排序，填函数练习风格
// 测试链接 : https://leetcode.cn/problems/sort-an-array/

/*
 * 补充题目列表:
 * 
 * 1. LeetCode 912. 排序数组
 *    链接: https://leetcode.cn/problems/sort-an-array/
 *    题目描述: 给你一个整数数组 nums，请你将该数组升序排列。
 *    解题思路: 使用快速排序算法对数组进行排序。
 * 
 * 2. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *    解题思路: 可以使用快速选择算法，在快速排序的基础上进行优化，只处理包含目标元素的区间。
 * 
 * 3. 剑指 Offer 40. 最小的k个数
 *    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
 *    解题思路: 使用快速选择算法或者快速排序算法找出最小的k个数。
 * 
 * 4. 牛客网 - 快速排序
 *    链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
 *    题目描述: 实现快速排序算法
 * 
 * 5. PAT 1101 Quick Sort
 *    链接: https://pintia.cn/problem-sets/994805342720868352/problems/994805366343188480
 *    题目描述: 快速排序中的主元(pivot)是左面都比它小、右边都比它大的位置对应的数字。找出所有满足条件的主元。
 * 
 * 6. 洛谷 P1177 【模板】快速排序
 *    链接: https://www.luogu.com.cn/problem/P1177
 *    题目描述: 利用快速排序算法将读入的N个数从小到大排序后输出。
 * 
 * 7. LeetCode 75. 颜色分类
 *    链接: https://leetcode.cn/problems/sort-colors/
 *    题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 *    解题思路: 使用三路快速排序的思想，将数组分为三个区域：小于基准值、等于基准值、大于基准值。
 * 
 * 8. LeetCode 283. 移动零
 *    链接: https://leetcode.cn/problems/move-zeroes/
 *    题目描述: 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *    解题思路: 使用快速排序的分区思想，将非零元素移到数组前面，零元素移到数组后面。
 * 
 * 9. Codeforces 401C. Team
 *    链接: https://codeforces.com/problemset/problem/401/C
 *    题目描述: 构造一个01序列，满足特定的约束条件。
 *    解题思路: 在某些构造方法中可以使用排序来优化解的生成。
 * 
 * 10. AtCoder ABC121C. Energy Drink Collector
 *    链接: https://atcoder.jp/contests/abc121/tasks/abc121_c
 *    题目描述: 购买能量饮料以获得最少的总花费。
 *    解题思路: 按价格排序后贪心选择。
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 最好情况: O(n log n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n log n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n^2) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 小数组使用插入排序 - 减少递归开销
 * 4. 尾递归优化 - 减少栈空间使用
 * 
 * 工程化考量:
 * 1. 异常处理: 处理空数组、null输入等边界情况
 * 2. 性能优化: 对于小数组使用插入排序优化
 * 3. 内存使用: 原地排序减少额外空间开销
 * 4. 稳定性: 标准快排不稳定，如需稳定排序需特殊处理
 * 
 * 调试技巧:
 * 1. 打印中间过程: 在分区操作后打印数组状态
 * 2. 断言验证: 验证分区后各部分的正确性
 * 3. 边界测试: 测试空数组、单元素、重复元素等边界情况
 * 
 * 跨语言实现差异:
 * 1. Java - 数组作为对象，有边界检查，使用Math.random()生成随机数
 * 2. C++ - 数组为指针，无边界检查，使用rand()生成随机数
 * 3. Python - 使用列表，动态类型，使用random模块生成随机数
 * 
 * 面试技巧:
 * 1. 理解快排与其它排序算法的比较（如归并排序、堆排序）
 * 2. 掌握快排的优化方法（随机化、三路快排等）
 * 3. 理解快排在不同数据分布下的性能表现
 * 4. 能够分析快排的稳定性和适用场景
 */

public class Code02_QuickSort {

	/**
	 * 入口函数：对整数数组进行排序
	 * @param nums 待排序的整数数组
	 * @return 排序后的数组
	 */
	public static int[] sortArray(int[] nums) {
		// 如果数组长度大于1才需要排序，长度为0或1的数组天然有序
		if (nums.length > 1) {
			// 调用改进版的快速排序算法对数组进行排序
			quickSort2(nums, 0, nums.length - 1);
		}
		return nums;
	}

	/**
	 * 随机快速排序经典版(不推荐使用)
	 * @param arr 待排序数组
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSort1(int[] arr, int l, int r) {
		// 递归终止条件：当左边界大于等于右边界时，表示区间内没有元素或只有一个元素，无需排序
		if (l >= r) {
			return;
		}
		
		// 随机选择基准值pivot，避免最坏情况的发生
		// Math.random()*(r-l+1)生成[0,r-l]之间的随机整数
		// 加上l后得到[l,r]之间的随机索引
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		
		// 调用partition1函数进行分区操作，返回等于x区域的右边界
		int mid = partition1(arr, l, r, x);
		
		// 递归处理小于等于x的区域
		quickSort1(arr, l, mid - 1);
		
		// 递归处理大于x的区域
		quickSort1(arr, mid + 1, r);
	}

	/**
	 * 分区函数 - 经典版本
	 * 将数组划分为两个部分：小于等于x的部分和大于x的部分
	 * 并确保划分完成后小于等于x区域的最后一个数字是x
	 * @param arr 待分区的数组
	 * @param l 分区区间的左边界（包含）
	 * @param r 分区区间的右边界（包含）
	 * @param x 基准值
	 * @return 等于x区域的右边界索引
	 */
	public static int partition1(int[] arr, int l, int r, int x) {
		// a表示小于等于x区域的右边界下一个位置
		// xi记录在小于等于x区域内任意一个x的位置
		int a = l, xi = 0;
		
		// 遍历整个区间
		for (int i = l; i <= r; i++) {
			// 如果当前元素小于等于基准值x
			if (arr[i] <= x) {
				// 将当前元素交换到小于等于x区域
				swap(arr, a, i);
				
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
		swap(arr, xi, a - 1);
		
		// 返回等于x区域的右边界索引
		return a - 1;
	}

	/**
	 * 交换数组中两个位置的元素
	 * @param arr 数组
	 * @param i 第一个位置
	 * @param j 第二个位置
	 */
	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 随机快速排序改进版(推荐使用)
	 * 使用三路快排优化，更好地处理重复元素
	 * @param arr 待排序数组
	 * @param l 排序区间的左边界（包含）
	 * @param r 排序区间的右边界（包含）
	 */
	public static void quickSort2(int[] arr, int l, int r) {
		// 递归终止条件：当左边界大于等于右边界时，表示区间内没有元素或只有一个元素，无需排序
		if (l >= r) {
			return;
		}
		
		// 随机选择基准值pivot，避免最坏情况的发生
		int x = arr[l + (int) (Math.random() * (r - l + 1))];
		
		// 调用partition2函数进行三路分区操作
		partition2(arr, l, r, x);
		
		// 为了防止底层的递归过程覆盖全局变量
		// 这里用临时变量记录等于x区域的左右边界
		int left = first;
		int right = last;
		
		// 递归处理小于x的区域
		quickSort2(arr, l, left - 1);
		
		// 递归处理大于x的区域
		quickSort2(arr, right + 1, r);
	}

	// 荷兰国旗问题中的全局变量，用于记录等于基准值区域的左右边界
	// first: 等于区域的左边界
	// last: 等于区域的右边界
	public static int first, last;

	/**
	 * 三路分区函数 - 改进版本
	 * 将数组划分为三个部分：<x的部分、=x的部分、>x的部分
	 * 更新全局变量first和last为=x区域的左右边界
	 * @param arr 待分区的数组
	 * @param l 分区区间的左边界（包含）
	 * @param r 分区区间的右边界（包含）
	 * @param x 基准值
	 */
	public static void partition2(int[] arr, int l, int r, int x) {
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
				swap(arr, first++, i++);
			} 
			// 如果当前元素大于基准值x
			else {
				// 将当前元素与等于区域右边界的元素交换
				// 注意这里i不自增，因为交换过来的元素还未处理
				swap(arr, i, last--);
			}
		}
	}

}