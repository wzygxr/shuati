package class021;

// ========================================
// 归并排序专题：分治策略的经典应用
// ========================================
//
// 【核心思想】
// 归并排序是一种基于分治策略的稳定排序算法：
// 1. 分解（Divide）：将待排序数组递归地分成两个子数组
// 2. 解决（Conquer）：递归地对子数组进行排序
// 3. 合并（Combine）：将两个已排序的子数组合并成一个有序数组
//
// 【时间复杂度】O(n log n) - 所有情况（最好、最坏、平均）都是这个复杂度
// 【空间复杂度】O(n) - 需要额外的辅助数组
// 【稳定性】稳定 - 相等元素的相对位置在排序后不会改变
//
// 【适用场景】
// 1. 大数据量排序 - 时间复杂度稳定
// 2. 外部排序 - 适合处理无法一次性装入内存的大数据
// 3. 稳定排序需求 - 需要保持相等元素相对顺序
// 4. 链表排序 - 特别适合链表结构，只需修改指针
// 5. 逆序对/翻转对统计 - 利用合并过程高效统计
//
// 【工程化考量】
// 1. 边界处理：空数组、单元素数组、重复元素
// 2. 异常处理：null输入检查
// 3. 性能优化：小数组使用插入排序、非递归版本避免栈溢出
// 4. 内存管理：复用辅助数组，避免频繁内存分配
// 5. 线程安全：注意辅助数组的并发访问问题
//
// 【与机器学习的联系】
// 1. 外部排序用于大规模数据预处理
// 2. 归并思想用于分布式排序（MapReduce）
// 3. 稳定排序保证数据预处理的可重现性
//
// 测试链接 : https://leetcode.cn/problems/sort-an-array/
//
// 【补充题目列表】（从各大算法平台搜集）
// 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
// 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code02_MergeSort {

	// ========================================
	// 题目1：LeetCode 912. 排序数组
	// ========================================
	// 【题目描述】给定一个整数数组 nums，将该数组升序排列。
	// 【输入】nums = [5,2,3,1]
	// 【输出】[1,2,3,5]
	// 【思路】直接应用归并排序的模板代码
	// 【时间复杂度】O(n log n) - 最优解
	// 【空间复杂度】O(n) - 辅助数组
	// 【是否最优】是 - 对于一般数据，归并排序是最优的比较排序之一
	public static int[] sortArray(int[] nums) {
		// 边界检查：处理null和小数组
		if (nums == null || nums.length <= 1) {
			return nums;
		}
		if (nums.length > 1) {
			// mergeSort1为递归方法，直观易懂，但可能栈溢出
			// mergeSort2为非递归方法，更安全，避免深递归栈溢出
			// mergeSort1(nums);
			mergeSort2(nums); // 推荐使用非递归版本
		}
		return nums;
	}

	// 辅助数组的最大长度
	public static int MAXN = 50001;

	// 辅助数组，用于合并操作
	// 工程优化：复用同一个辅助数组，避免频繁分配内存
	public static int[] help = new int[MAXN];

	// ========================================
	// 归并排序递归版（推荐理解版本）
	// ========================================
	// 【时间复杂度】O(n log n)
	// 推导：T(n) = 2*T(n/2) + O(n)
	//      每层递归时间O(n)，递归深度log(n)层
	//      总时间 = O(n) * log(n) = O(n log n)
	// 【空间复杂度】O(n) - 辅助数组 + O(log n)递归栈
	// 【优点】代码简洁，逻辑清晰
	// 【缺点】深度递归可能导致栈溢出
	public static void mergeSort1(int[] arr) {
		// 边界检查
		if (arr == null || arr.length <= 1) {
			return;
		}
		sort(arr, 0, arr.length - 1);
	}

	// 递归排序的核心函数
	// 参数: arr-待排序数组, l-左边界， r-右边界
	// 分治策略：
	// 1. 分：将[l,r]区间分为[l,m]和[m+1,r]
	// 2. 治：递归排序左右两部分
	// 3. 合：合并两个有序部分
	public static void sort(int[] arr, int l, int r) {
		// 基础情况：只有一个元素，已经有序
		if (l == r) {
			return;
		}
		// 计算中点，避免溢出：使用 l+(r-l)/2 而非 (l+r)/2
		// 但在Java中int类型不会溢出为正数，所以(l+r)/2也可以
		int m = (l + r) / 2;
		// 递归排序左半部分
		sort(arr, l, m);
		// 递归排序右半部分
		sort(arr, m + 1, r);
		// 合并两个有序部分
		merge(arr, l, m, r);
	}

	// ========================================
	// 归并排序非递归版（工程推荐版本）
	// ========================================
	// 【时间复杂度】O(n log n)
	// 【空间复杂度】O(n) - 仅需辅助数组，无递归栈开销
	// 【优点】
	// 1. 避免递归栈溢出风险
	// 2. 更好的缓存局部性（顺序访问）
	// 3. 更容易优化（如并行化）
	// 【实现思路】
	// 从小到大模拟归并过程：
	// step=1: 每1个为一组，两两合并
	// step=2: 每2个为一组，两两合并  
	// step=4: 每4个为一组，两两合并
	// ...直到整个数组有序
	public static void mergeSort2(int[] arr) {
		// 边界检查
		if (arr == null || arr.length <= 1) {
			return;
		}
		int n = arr.length;
		// step表示当前每组的大小，每次翻倍
		// step < n 保证至少还有两组需要合并
		// 总共执行 log(n) 轮
		for (int l, m, r, step = 1; step < n; step <<= 1) {
			// 每一轮中，从左往右合并所有相邻的组
			// 这一层循环总时间 O(n)
			l = 0;
			while (l < n) {
				// 第一组的右边界
				m = l + step - 1;
				// 如果没有第二组，则不需要合并
				if (m + 1 >= n) {
					break;
				}
				// 第二组的右边界，可能不足step个元素
				r = Math.min(l + (step << 1) - 1, n - 1);
				// 合并 [l,m] 和 [m+1,r]
				merge(arr, l, m, r);
				// 移动到下一对组
				l = r + 1;
			}
		}
	}

	// ========================================
	// 合并两个有序数组（核心操作）
	// ========================================
	// 【参数】arr-原数组, l-左边界, m-中点, r-右边界
	// 【前置条件】[l,m] 和 [m+1,r] 已经各自有序
	// 【后置条件】[l,r] 整体有序
	// 【时间复杂度】O(r-l+1) = O(n)
	// 【空间复杂度】O(r-l+1) = O(n)
	// 【核心思想】双指针归并：
	// 1. 用两个指针分别指向左右两部分的开头
	// 2. 每次比较两个指针指向的元素，将较小者放入结果
	// 3. 移动对应指针，直到某一部分处理完
	// 4. 将剩余部分直接拷贝
	// 【稳定性保证】使用 <= 保证相等时左侧元素先被选中
	public static void merge(int[] arr, int l, int m, int r) {
		// i: 辅助数组的当前位置
		// a: 左部分[l,m]的当前指针
		// b: 右部分[m+1,r]的当前指针
		int i = l;
		int a = l;
		int b = m + 1;
		
		// 双指针合并：当两个部分都还有元素时
		while (a <= m && b <= r) {
			// 关键：<= 保证稳定性（相等时左侧优先）
			// 这是归并排序为稳定排序的关键
			help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
		}
		
		// 左侧指针、右侧指针，必有一个越界、另一个不越界
		// 将剩余部分直接拷贝到辅助数组
		while (a <= m) {
			help[i++] = arr[a++];
		}
		while (b <= r) {
			help[i++] = arr[b++];
		}
		
		// 将辅助数组的结果拷贝回原数组
		// 注意：只拷贝 [l,r] 这个范围
		for (i = l; i <= r; i++) {
			arr[i] = help[i];
		}
	}
	
	// ========================================
	// 题目2：LeetCode 148. 排序链表
	// ========================================
	// 【题目来源】https://leetcode.cn/problems/sort-list/
	// 【题目描述】给你链表的头结点 head，请将其按升序排列并返回排序后的链表。
	// 【输入示例】head = [4,2,1,3]
	// 【输出示例】[1,2,3,4]
	// 【进阶要求】能否在 O(n log n) 时间复杂度和常数级空间复杂度下解决？
	// 【思路分析】
	// 1. 为什么链表排序首选归并排序？
	//    - 快速排序需要随机访问，链表不支持
	//    - 堆排序也需要随机访问
	//    - 归并排序只需顺序访问，完美适配链表
	// 2. 如何找到链表中点？
	//    - 使用快慢指针（龟兔赛跑）
	//    - 快指针每次移动2步，慢指针每次移动1步
	//    - 当快指针到达末尾时，慢指针到达中点
	// 3. 如何合并两个有序链表？
	//    - 使用双指针，比较节点值后连接
	// 【时间复杂度】O(n log n)
	//    - 递归深度 log(n)，每层遍历所有节点 O(n)
	// 【空间复杂度】O(log n) - 递归调用栈
	//    - 注意：没有额外的辅助数组，只修改指针
	// 【是否最优】是 - 达到了题目要求的上限
	// 【工程优化】
	//    - 可以改为非递归实现，空间复杂度降为O(1)
	//    - 但代码复杂度增加，面试中递归版本已经足够
	public static class ListNode {
        int val;           // 节点值
        ListNode next;     // 下一个节点的指针
        ListNode() {}      // 默认构造函数
        ListNode(int val) { this.val = val; }  // 带值构造
        ListNode(int val, ListNode next) {     // 带值和指针构造
            this.val = val; 
            this.next = next; 
        }
    }
	
	public static ListNode sortList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		return process(head);
	}
	
	// 链表归并排序的递归函数
	// 【参数】head - 当前链表的头节点
	// 【返回】排序后链表的头节点
	// 【核心步骤】
	// 1. 找到链表中点（快慢指针）
	// 2. 切断链表，分为左右两部分
	// 3. 递归排序左右两部分
	// 4. 合并两个有序链表
	private static ListNode process(ListNode head) {
		// 递归基础情况：单节点，已有序
		if (head.next == null) {
			return head;
		}
		
		// 步骤1：使用快慢指针找到中点
		// slow：慢指针，每次移动1步
		// fast：快指针，每次移动2步  
		// prev：记录slow的前一个节点，用于断开链表
		ListNode slow = head;
		ListNode fast = head;
		ListNode prev = null;
		
		// 快指针走两步，慢指针走一步
		// 当fast到达末尾时，slow到达中点
		// 注意：这里的中点是偏左的
		// 例如 1->2->3->4，中点是2
		while (fast != null && fast.next != null) {
			prev = slow;
			slow = slow.next;
			fast = fast.next.next;
		}
		
		// 步骤2：断开链表
		// 将链表分为 [head, prev] 和 [slow, end]
		prev.next = null;  // 关键：切断连接，避免死循环
		
		// 步骤3：递归排序左右两部分
		ListNode left = process(head);   // 排序左半部分
		ListNode right = process(slow);  // 排序右半部分
		
		// 步骤4：合并两个有序链表
		return mergeList(left, right);
	}
	
	// ========================================
	// 合并两个有序链表（链表版本merge）
	// ========================================
	// 【题目】LeetCode 21. 合并两个有序链表
	// 【参数】l1, l2 - 两个有序链表的头节点
	// 【返回】合并后的有序链表头节点
	// 【技巧】使用哑节点（dummy node）简化边界处理
	// 【时间复杂度】O(m + n) - m和n分别是两个链表的长度
	// 【空间复杂度】O(1) - 只使用常数个指针
	// 【工程技巧】
	// 1. 哑节点技巧：避免特殊处理空链表
	// 2. 三元表达式处理剩余节点
	private static ListNode mergeList(ListNode l1, ListNode l2) {
		// 哑节点技巧：创建一个虑拟头节点
		// 作用：
		// 1. 简化边界处理（不需要判断第一个节点）
		// 2. 最终返回 dummy.next 即为真正的头节点
		ListNode dummy = new ListNode(0);
		ListNode current = dummy;  // 当前指针，用于构建结果链表
		
		// 当两个链表都还有节点时，比较并连接较小者
		while (l1 != null && l2 != null) {
			// 比较两个节点的值
			if (l1.val <= l2.val) {
				// l1较小，连接l1
				current.next = l1;
				l1 = l1.next;  // l1向后移动
			} else {
				// l2较小，连接l2
				current.next = l2;
				l2 = l2.next;  // l2向后移动
			}
			current = current.next;  // current向后移动
		}
		
		// 处理剩余节点
		// 此时至少有一个链表已经处理完
		// 另一个链表的剩余部分直接连接即可
		current.next = (l1 != null) ? l1 : l2;
		
		// 返回真正的头节点（跳过哑节点）
		return dummy.next;
	}
	
	// ========================================
	// 题目3：LeetCode 23. 合并K个升序链表
	// ========================================
	// 【题目来源】https://leetcode.cn/problems/merge-k-sorted-lists/
	// 【题目描述】给你一个链表数组，每个链表都已经按升序排列。
	//          请你将所有链表合并到一个升序链表中。
	// 【输入示例】lists = [[1,4,5],[1,3,4],[2,6]]
	// 【输出示例】[1,1,2,3,4,4,5,6]
	// 【解法对比】
	// 方法1：顺序合并 - O(kN) - k个链表，总节点数N
	//       第1次合并：O(n1+n2), 第2次：O(n1+n2+n3), ...
	//       总时间 ≈ O(kN)
	// 方法2：优先队列 - O(N log k)
	//       维护大小为k的小顶堆，每次取最小值 O(log k)
	//       总时间 O(N log k)
	// 方法3：分治合并 - O(N log k) - 最优解
	//       两两合并，递归处理，类似归并排序
	// 【时间复杂度】O(N log k) - N是所有节点总数，k是链表数量
	// 推导：分治深度log(k)，每层合并所有节点O(N)
	// 【空间复杂度】O(log k) - 递归调用栈
	// 【是否最优】是 - 与优先队列方法时间复杂度相同，但代码更简洁
	// 【思路技巧】
	// - 把K个链表合并转化为多次两个链表合并
	// - 使用分治法减少合并次数
public static ListNode mergeKLists(ListNode[] lists) {
		// 边界检查：处理null或空数组
		if (lists == null || lists.length == 0) {
			return null;
		}
		// 分治合并：将lists分成两部分，分别处理后合并
		return mergeKListsHelper(lists, 0, lists.length - 1);
	}
	
	// 分治合并K个链表的辅助函数
	// 【参数】lists-链表数组, left-左边界, right-右边界
	// 【返回】合并后的链表头节点
	// 【算法逻辑】
	// 1. 如果只有一个链表，直接返回
	// 2. 如果有两个链表，合并它们
	// 3. 其他情况，分成两部分递归处理，然后合并结果
	private static ListNode mergeKListsHelper(ListNode[] lists, int left, int right) {
		// 基础情况1：只有一个链表
		if (left == right) {
			return lists[left];
		}
		// 优化：如果恰好是两个链表，直接合并
		if (left + 1 == right) {
			return mergeList(lists[left], lists[right]);
		}
		// 分治：找中点，分成两部分
		int mid = left + (right - left) / 2;
		// 递归合并左半部分
		ListNode l1 = mergeKListsHelper(lists, left, mid);
		// 递归合并右半部分
		ListNode l2 = mergeKListsHelper(lists, mid + 1, right);
		// 合并两个结果
		return mergeList(l1, l2);
	}
	
	// ========================================
	// 题目4：LeetCode 88. 合并两个有序数组
	// ========================================
	// 【题目来源】https://leetcode.cn/problems/merge-sorted-array/
	// 【题目描述】给你两个按非递减顺序排列的整数数组 nums1 和 nums2，
	//          另有两个整数 m 和 n，分别表示 nums1 和 nums2 中的元素数目。
	//          请你合并 nums2 到 nums1 中，使合并后的数组同样按非递减顺序排列。
	// 【输入示例】nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
	// 【输出示例】[1,2,2,3,5,6]
	// 【注意】nums1 的长度为 m + n，前 m 个元素是有效数据，后 n 个为 0 用于存放结果
	// 【关键技巧】从后往前合并！
	// 为什么？
	// - 从前往后会覆盖nums1的有效数据
	// - 从后往前可以直接利用nums1尾部的空位
	// 【时间复杂度】O(m + n)
	// 【空间复杂度】O(1) - 原地修改，不需额外空间
	// 【是否最优】是 - 时间、空间都最优
	// 【面试重点】这道题经常考查，关键是"从后往前"的思路
	public static void merge(int[] nums1, int m, int[] nums2, int n) {
		// i: nums1的有效数据的最后一个位置
		// j: nums2的最后一个位置
		// k: 合并后数组的最后一个位置
		int i = m - 1;  
		int j = n - 1;  
		int k = m + n - 1;  
		
		// 从后往前合并：每次选择较大的元素放到最后
		// 当两个数组都还有元素时
		while (i >= 0 && j >= 0) {
			// 比较两个元素，将较大者放到位置k
			if (nums1[i] > nums2[j]) {
				nums1[k--] = nums1[i--];
			} else {
				// 注意：nums1[i] <= nums2[j]时选nums2[j]
				// 这保证了稳定性
				nums1[k--] = nums2[j--];
			}
		}
		
		// 处理nums2剩余元素
		// 注意：如果nums1有剩余，不需要处理（已在正确位置）
		while (j >= 0) {
			nums1[k--] = nums2[j--];
		}
		// 为什么不需要处理nums1剩余？
		// 因为nums1剩余元素已经在正确的位置上！
	}
	
	// LeetCode 21. 合并两个有序链表
	// 测试链接: https://leetcode.cn/problems/merge-two-sorted-lists/
	public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		ListNode dummy = new ListNode(0);
		ListNode current = dummy;
		
		while (list1 != null && list2 != null) {
			if (list1.val <= list2.val) {
				current.next = list1;
				list1 = list1.next;
			} else {
				current.next = list2;
				list2 = list2.next;
			}
			current = current.next;
		}
		
		// 处理剩余节点
		current.next = (list1 != null) ? list1 : list2;
		
		return dummy.next;
	}
	
	// ========================================
	// 题目5：LeetCode 315. 计算右侧小于当前元素的个数
	// ========================================
	// 【题目来源】https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
	// 【题目描述】给定一个整数数组 nums，按要求返回一个新数组 counts。
	//          counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
	// 【输入示例】nums = [5,2,6,1]
	// 【输出示例】[2,1,1,0]
	// 解释：
	//   5的右侧有 2 个更小的元素 (2 和 1)
	//   2的右侧有 1 个更小的元素 (1)
	//   6的右侧有 1 个更小的元素 (1)
	//   1的右侧有 0 个更小的元素
	// 【暗力解法】O(n^2) - 对每个元素，遍历其右侧所有元素
	// 【最优解法】O(n log n) - 利用归并排序的合并过程
	// 【核心思想】
	// 1. 逆序对的变种：统计右侧比自己小的元素个数
	// 2. 在归并排序的合并过程中：
	//    - 左右两部分已各自有序
	//    - 当左侧元素 <= 右侧元素时，右侧已处理的元素都比左侧小
	//    - 用索引数组记录原始位置，统计结果
	// 【时间复杂度】O(n log n)
	// 【空间复杂度】O(n)
	// 【是否最优】是 - 基于比较的算法下界是O(n log n)
	// 【难点】
	// 1. 需要维护索引数组，因为排序后位置会变
	// 2. 理解何时统计：左侧元素出队时
	public static List<Integer> countSmaller(int[] nums) {
		List<Integer> result = new ArrayList<>();
		int n = nums.length;
		// 边界检查
		if (n == 0) return result;
		
		// counts[i]: 记录原始位置i右侧比它小的元素个数
		int[] counts = new int[n];
		// indices[i]: 当前位置i的元素原本在哪个位置
		int[] indices = new int[n];
		
		// 初始化索引数组：0,1,2,3...
		for (int i = 0; i < n; i++) {
			indices[i] = i;
		}
		
		// 辅助数组
		int[] helper = new int[n];
		int[] helperIndices = new int[n];
		
		// 归并排序 + 统计
		mergeSortCount(nums, indices, counts, helper, helperIndices, 0, n - 1);
		
		// 构造结果
		for (int count : counts) {
			result.add(count);
		}
		
		return result;
	}
	
	// 归并排序 + 统计右侧更小元素
	// 【参数】
	// nums: 原始数组(会被修改)
	// indices: 索引数组(记录每个位置的元素原始位置)
	// counts: 统计数组(结果)
	// helper: 辅助数组
	// helperIndices: 辅助索引数组
	private static void mergeSortCount(int[] nums, int[] indices, int[] counts, 
			int[] helper, int[] helperIndices, int left, int right) {
		// 递归基础情况
		if (left >= right) {
			return;
		}
		
		int mid = left + (right - left) / 2;
		// 递归处理左半部分
		mergeSortCount(nums, indices, counts, helper, helperIndices, left, mid);
		// 递归处理右半部分  
		mergeSortCount(nums, indices, counts, helper, helperIndices, mid + 1, right);
		// 合并 + 统计
		mergeCount(nums, indices, counts, helper, helperIndices, left, mid, right);
	}
	
	// 合并 + 统计（核心逻辑）
	// 【核心思想】
	// 在合并[left,mid]和[mid+1,right]时，两部分已各自有序
	// 当左侧元素nums[p1] <= 右侧元素nums[p2]时：
	//   右侧从(mid+1)到(p2-1)的所有元素都 < nums[p1]
	//   且这些元素在原始数组中都在nums[p1]右侧！
	//   因此counts[indices[p1]] += (p2 - (mid+1))
	private static void mergeCount(int[] nums, int[] indices, int[] counts,
			int[] helper, int[] helperIndices, int left, int mid, int right) {
		// 步骤1：复制到辅助数组
		for (int i = left; i <= right; i++) {
			helper[i] = nums[i];              // 复制值
			helperIndices[i] = indices[i];    // 复制索引
		}
		
		int i = left;   // 结果数组的当前位置
		int p1 = left;  // 左部分指针
		int p2 = mid + 1;  // 右部分指针
		
		// 步骤2：合并两个有序数组，同时统计
		while (p1 <= mid && p2 <= right) {
			// 关键：当左侧元素 <= 右侧元素时
			if (helper[p1] <= helper[p2]) {
				// *** 统计逻辑在这里 ***
				// 右部分从(mid+1)到(p2-1)的元素都比helper[p1]小
				// 且在原数组中在helper[p1]的右侧
				// 所以累加到counts中
				counts[helperIndices[p1]] += (p2 - (mid + 1));
				
				// 将左侧元素放入结果
				nums[i] = helper[p1];
				indices[i] = helperIndices[p1];
				p1++;
			} else {
				// 右侧元素更小，放入结果
				// 注意：这里不统计，因为右侧元素在左侧元素右边
				nums[i] = helper[p2];
				indices[i] = helperIndices[p2];
				p2++;
			}
			i++;
		}
		
		// 步骤3：处理左侧剩余元素
		while (p1 <= mid) {
			// *** 重要：还要统计 ***
			// 右部分所有元素都已处理，都比当前左侧元素小
			counts[helperIndices[p1]] += (p2 - (mid + 1));
			
			nums[i] = helper[p1];
			indices[i] = helperIndices[p1];
			p1++;
			i++;
		}
		
		// 步骤4：处理右侧剩余元素
		// 不需要统计，直接放入
		while (p2 <= right) {
			nums[i] = helper[p2];
			indices[i] = helperIndices[p2];
			p2++;
			i++;
		}
	}
	
	// ========================================
	// 题目6：LeetCode 493. 翻转对 (Reverse Pairs)
	// ========================================
	// 【题目来源】https://leetcode.cn/problems/reverse-pairs/
	// 【题目描述】给定一个数组 nums，如果 i < j 且 nums[i] > 2*nums[j]，
	//          我们就将 (i, j) 称作一个重要翻转对。
	//          你需要返回给定数组中的重要翻转对的数量。
	// 【输入示例】nums = [1,3,2,3,1]
	// 【输出示例】2
	// 解释：翻转对为 (3,1) 和 (3,1)
	// 【与题目315的区别】
	// - LeetCode 315: 统计右侧比自己小的元素个数
	// - LeetCode 493: 统计右侧满足 nums[i] > 2*nums[j] 的对数
	// 【核心思想】
	// 1. 在归并排序的合并之前，先统计翻转对
	// 2. 利用左右两部分已各自有序的特性
	// 3. 对于左侧每个元素nums[i]，用双指针找到右侧满足条件的元素个数
	// 【时间复杂度】O(n log n)
	// 【空间复杂度】O(n)
	// 【是否最优】是
	// 【难点】
	// 1. 统计和合并是分开的两个过程
	// 2. 需要注意溢出：使用long类型
	public static int reversePairs(int[] nums) {
		// 边界检查
		if (nums == null || nums.length <= 1) {
			return 0;
		}
		// 辅助数组
		int[] helper = new int[nums.length];
		// 归并排序 + 统计翻转对
		return mergeSortReversePairs(nums, helper, 0, nums.length - 1);
	}
	
	// 归并排序 + 统计翻转对
	// 【参数】nums-原数组, helper-辅助数组, left-左边界, right-右边界
	// 【返回】[left,right]范围内的翻转对数量
	private static int mergeSortReversePairs(int[] nums, int[] helper, int left, int right) {
		// 递归基础情况
		if (left >= right) {
			return 0;
		}
		
		int mid = left + (right - left) / 2;
		// 递归统计左半部分的翻转对
		int count = mergeSortReversePairs(nums, helper, left, mid);
		// 递归统计右半部分的翻转对
		count += mergeSortReversePairs(nums, helper, mid + 1, right);
		// 统计跨越左右两部分的翻转对 + 合并
		count += mergeReversePairs(nums, helper, left, mid, right);
		
		return count;
	}
	
	// 合并 + 统计翻转对（核心逻辑）
	// 【关键点】1. 先统计，再合并！（与315不同）
	//        2. 使用long防溢出
	// 【算法流程】
	// 第1步：统计翻转对（利用有序性）
	// 第2步：合并两个有序数组
	private static int mergeReversePairs(int[] nums, int[] helper, int left, int mid, int right) {
		// 步骤1：复制到辅助数组
		for (int i = left; i <= right; i++) {
			helper[i] = nums[i];
		}
		
		int count = 0;
		int j = mid + 1;  // 右部分指针
		
		// *** 第1步：统计翻转对 ***
		// 对于左部分每个元素，找右部分多少个元素满足 nums[i] > 2*nums[j]
		for (int i = left; i <= mid; i++) {
			// 关键：利用右部分有序性，用双指针
			// 对于每个 helper[i]，找到第一个满足 helper[i] > 2*helper[j] 的 j
			// 那么 [mid+1, j-1] 都满足条件
			while (j <= right && (long) helper[i] > 2 * (long) helper[j]) {
				j++;  // j不需要重置！因为helper[i]递增，helper[j]也递增
			}
			// 统计：从 mid+1 到 j-1 都满足条件
			count += (j - (mid + 1));
			// 注意：j不重置，下次循环继续使用！这是时间复杂度O(n)的关键
		}
		
		// *** 第2步：合并两个有序数组 ***
		// 注意：这里和普通的merge一样
		int i = left;
		int p1 = left;
		int p2 = mid + 1;
		
		while (p1 <= mid && p2 <= right) {
			if (helper[p1] <= helper[p2]) {
				nums[i++] = helper[p1++];
			} else {
				nums[i++] = helper[p2++];
			}
		}
		
		// 处理剩余元素
		while (p1 <= mid) {
			nums[i++] = helper[p1++];
		}
		
		while (p2 <= right) {
			nums[i++] = helper[p2++];
		}
		
		return count;
	}
}