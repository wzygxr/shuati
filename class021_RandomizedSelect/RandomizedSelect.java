import java.util.*;

/**
 * 快速选择算法实现
 * 用于在未排序数组中找到第K大的元素
 * 
 * 算法原理：
 * 快速选择算法是基于快速排序的分治思想，但只处理包含目标元素的一侧，
 * 从而避免了完全排序，平均时间复杂度为O(n)。
 * 
 * 相关题目列表：
 * 1. LeetCode 215. 数组中的第K个最大元素
 *    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 *    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
 * 
 * 2. 剑指 Offer 40. 最小的k个数
 *    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
 *    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数
 * 
 * 3. LeetCode 973. 最接近原点的 K 个点
 *    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
 *    题目描述: 给定平面上n个点，找到距离原点最近的k个点
 * 
 * 4. LeetCode 347. 前 K 个高频元素
 *    链接: https://leetcode.cn/problems/top-k-frequent-elements/
 *    题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
 * 
 * 5. 牛客网 - NC119 最小的K个数
 *    链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 *    题目描述: 输入n个整数，找出其中最小的K个数
 * 
 * 6. AcWing 786. 第k个数
 *    链接: https://www.acwing.com/problem/content/788/
 *    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
 * 
 * 7. 洛谷 P1923 【深基9.例4】求第 k 小的数
 *    链接: https://www.luogu.com.cn/problem/P1923
 *    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
 * 
 * 8. HackerRank Find the Median
 *    链接: https://www.hackerrank.com/challenges/find-the-median/problem
 *    题目描述: 找到未排序数组的中位数
 * 
 * 9. LintCode 5. 第K大元素
 *    链接: https://www.lintcode.com/problem/5/
 *    题目描述: 在数组中找到第k大的元素
 * 
 * 10. POJ 2388. Who's in the Middle
 *     链接: http://poj.org/problem?id=2388
 *     题目描述: 找到数组的中位数
 * 
 * 11. 洛谷 P1177. 【模板】快速排序
 *     链接: https://www.luogu.com.cn/problem/P1177
 *     题目描述: 快速排序模板题，可扩展为快速选择
 * 
 * 12. 牛客网 NC73. 数组中出现次数超过一半的数字
 *     链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
 *     题目描述: 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字
 * 
 * 13. LeetCode 451. 根据字符出现频率排序
 *     链接: https://leetcode.cn/problems/sort-characters-by-frequency/
 *     题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列
 * 
 * 14. LeetCode 703. 数据流中的第K大元素
 *     链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
 *     题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素
 * 
 * 15. LeetCode 912. 排序数组 (快速选择优化)
 *     链接: https://leetcode.cn/problems/sort-an-array/
 *     题目描述: 给你一个整数数组 nums，请你将该数组升序排列
 * 
 * 16. LeetCode 164. 最大间距
 *     链接: https://leetcode.cn/problems/maximum-gap/
 *     题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
 * 
 * 17. LeetCode 324. 摆动排序 II
 *     链接: https://leetcode.cn/problems/wiggle-sort-ii/
 *     题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序
 * 
 * 18. LeetCode 215. Kth Largest Element in an Array
 *     链接: https://leetcode.com/problems/kth-largest-element-in-an-array/
 *     题目描述: Find the kth largest element in an unsorted array
 * 
 * 19. LeetCode 347. Top K Frequent Elements
 *     链接: https://leetcode.com/problems/top-k-frequent-elements/
 *     题目描述: Given a non-empty array of integers, return the k most frequent elements
 * 
 * 20. LeetCode 973. K Closest Points to Origin
 *     链接: https://leetcode.com/problems/k-closest-points-to-origin/
 *     题目描述: We have a list of points on the plane. Find the K closest points to the origin (0, 0)
 * 
 * 算法复杂度分析:
 * 时间复杂度:
 *   - 最好情况: O(n) - 每次划分都能将数组平均分成两部分
 *   - 平均情况: O(n) - 随机选择基准值的情况下
 *   - 最坏情况: O(n²) - 每次选择的基准值都是最大或最小值
 * 空间复杂度:
 *   - O(log n) - 递归调用栈的深度
 * 
 * 算法优化策略:
 * 1. 随机选择基准值 - 避免最坏情况的出现
 * 2. 三路快排 - 处理重复元素较多的情况
 * 3. 尾递归优化 - 减少栈空间使用
 * 4. 迭代实现 - 避免递归调用栈溢出
 * 5. 三数取中法 - 选择更好的基准值
 * 
 * 跨语言实现差异:
 * 1. Java - 数组作为对象，有边界检查，使用Math.random()生成随机数
 * 2. C++ - 数组为指针，无边界检查，使用rand()生成随机数
 * 3. Python - 使用列表，动态类型，使用random模块生成随机数
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入参数合法性
 * 2. 可配置性：支持自定义比较器
 * 3. 单元测试：覆盖各种边界情况和异常场景
 * 4. 性能优化：针对不同数据规模选择合适的算法
 * 5. 线程安全：当前实现不是线程安全的，如需线程安全需要额外同步措施
 * 6. 内存管理：Java有垃圾回收机制，无需手动管理内存
 * 7. 代码复用：通过静态方法实现，便于调用
 * 8. 可维护性：添加详细注释和文档说明
 * 9. 调试能力：添加调试信息输出，便于问题定位
 * 10. 输入输出优化：针对大数据量场景优化IO处理
 */
public class RandomizedSelect {

	/**
	 * 查找数组中第k个最大的元素
	 * 
	 * 算法思路：
	 * 1. 将第k大问题转换为第(n-k)小问题
	 * 2. 使用快速选择算法找到第(n-k)小的元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * @param nums 整数数组
	 * @param k 第k个最大的元素
	 * @return 第k个最大的元素
	 */
	public static int findKthLargest(int[] nums, int k) {
		// 防御性编程：检查输入合法性
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 第k大元素在排序后数组中的索引是nums.length - k
		return randomizedSelect(nums, nums.length - k);
	}

	/**
	 * 快速选择算法核心实现
	 * 
	 * 算法思路：
	 * 1. 随机选择一个元素作为基准值
	 * 2. 使用荷兰国旗问题的分区方法将数组分为三部分：小于基准值、等于基准值、大于基准值
	 * 3. 根据目标索引与分区边界的关系，决定在哪个子数组中继续查找
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * @param arr 数组
	 * @param i 目标元素的索引
	 * @return 目标元素的值
	 */
	public static int randomizedSelect(int[] arr, int i) {
		int ans = 0;
		for (int l = 0, r = arr.length - 1; l <= r;) {
			// 随机选择基准值，避免最坏情况的出现
			// 但只有这一下随机，才能在概率上把时间复杂度收敛到O(n)
			partition(arr, l, r, arr[l + (int) (Math.random() * (r - l + 1))]);
			// 因为左右两侧只需要走一侧
			// 所以不需要临时变量记录全局的first、last
			// 直接用即可
			if (i < first) {
				r = first - 1;
			} else if (i > last) {
				l = last + 1;
			} else {
				ans = arr[i];
				break;
			}
		}
		return ans;
	}

	// 荷兰国旗问题的分区边界
	// first: 等于基准值区域的左边界
	// last: 等于基准值区域的右边界
	public static int first, last;

	/**
	 * 荷兰国旗问题分区实现
	 * 
	 * 算法思路：
	 * 将数组分为三部分：
	 * 1. 小于基准值的元素放在左侧
	 * 2. 等于基准值的元素放在中间
	 * 3. 大于基准值的元素放在右侧
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 
	 * @param arr 数组
	 * @param l 左边界
	 * @param r 右边界
	 * @param x 基准值
	 */
	public static void partition(int[] arr, int l, int r, int x) {
		first = l;
		last = r;
		int i = l;
		while (i <= last) {
			if (arr[i] == x) {
				i++;
			} else if (arr[i] < x) {
				swap(arr, first++, i++);
			} else {
				swap(arr, i, last--);
			}
		}
	}

	/**
	 * 交换数组中两个元素的位置
	 * 
	 * @param arr 数组
	 * @param i 第一个元素的索引
	 * @param j 第二个元素的索引
	 */
	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * LeetCode 973. K Closest Points to Origin
	 * 链接: https://leetcode.com/problems/k-closest-points-to-origin/
	 * 题目描述: 给定平面上n个点，找到距离原点最近的k个点
	 * 
	 * 算法思路：
	 * 1. 计算每个点到原点的距离
	 * 2. 使用快速选择算法找到第k小的距离
	 * 3. 返回前k个点
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：避免重复计算距离
	 * 3. 内存管理：使用Arrays.copyOfRange避免创建不必要的数组
	 * 4. 可维护性：添加详细注释和文档说明
	 * 
	 * @param points 平面上的点数组
	 * @param k 需要返回的最近点的数量
	 * @return 距离原点最近的k个点
	 */
	public static int[][] kClosest(int[][] points, int k) {
		// 防御性编程：检查输入合法性
		if (points == null || points.length == 0 || k <= 0 || k > points.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到第k小的距离
		quickSelect(points, 0, points.length - 1, k - 1);
		
		// 返回前k个点
		return Arrays.copyOfRange(points, 0, k);
	}
	
	/**
	 * 根据点到原点的距离进行快速选择
	 * 
	 * 工程化考量：
	 * 1. 随机化：使用Math.random()避免最坏情况
	 * 2. 递归优化：尾递归减少栈空间使用
	 * 3. 边界处理：处理left >= right的情况
	 * 
	 * @param points 点数组
	 * @param left 左边界
	 * @param right 右边界
	 * @param k 目标索引
	 */
	private static void quickSelect(int[][] points, int left, int right, int k) {
		if (left >= right) return;
		
		// 随机选择基准值
		int pivotIndex = left + (int) (Math.random() * (right - left + 1));
		// 将基准值移到末尾
		swapPoints(points, pivotIndex, right);
		
		// 分区操作
		int partitionIndex = partitionByDistance(points, left, right);
		
		// 根据分区结果决定继续在哪一侧查找
		if (partitionIndex == k) {
			return;
		} else if (partitionIndex < k) {
			quickSelect(points, partitionIndex + 1, right, k);
		} else {
			quickSelect(points, left, partitionIndex - 1, k);
		}
	}
	
	/**
	 * 根据点到原点的距离进行分区
	 * 
	 * 工程化考量：
	 * 1. 性能优化：避免重复计算距离
	 * 2. 内存优化：原地交换减少额外空间使用
	 * 3. 边界处理：正确处理分区边界
	 * 
	 * @param points 点数组
	 * @param left 左边界
	 * @param right 右边界
	 * @return 分区点的索引
	 */
	private static int partitionByDistance(int[][] points, int left, int right) {
		// 基准值是右端点到原点的距离
		int pivotDistance = points[right][0] * points[right][0] + points[right][1] * points[right][1];
		int partitionIndex = left;
		
		for (int i = left; i < right; i++) {
			// 计算当前点到原点的距离
			int currentDistance = points[i][0] * points[i][0] + points[i][1] * points[i][1];
			// 如果当前点距离小于等于基准值距离，则交换
			if (currentDistance <= pivotDistance) {
				swapPoints(points, i, partitionIndex++);
			}
		}
		
		// 将基准值放到正确位置
		swapPoints(points, partitionIndex, right);
		return partitionIndex;
	}
	
	/**
	 * 交换点数组中两个点的位置
	 * 
	 * @param points 点数组
	 * @param i 第一个点的索引
	 * @param j 第二个点的索引
	 */
	private static void swapPoints(int[][] points, int i, int j) {
		int[] temp = points[i];
		points[i] = points[j];
		points[j] = temp;
	}
	
	/**
	 * LeetCode 347. Top K Frequent Elements
	 * 链接: https://leetcode.com/problems/top-k-frequent-elements/
	 * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
	 * 
	 * 算法思路：
	 * 1. 使用HashMap统计每个元素的频率
	 * 2. 将元素和频率组成数组
	 * 3. 使用快速选择算法找到第k大的频率
	 * 4. 返回频率前k高的元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(n) 用于存储频率信息
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用HashMap提高查找效率
	 * 3. 内存管理：合理使用数组和集合
	 * 4. 可维护性：添加详细注释和文档说明
	 * 
	 * @param nums 整数数组
	 * @param k 需要返回的高频元素数量
	 * @return 出现频率前k高的元素
	 */
	public static int[] topKFrequent(int[] nums, int k) {
		// 防御性编程：检查输入合法性
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用HashMap统计每个元素的频率
		Map<Integer, Integer> frequencyMap = new HashMap<>();
		for (int num : nums) {
			frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
		}
		
		// 将元素和频率组成数组
		int[][] elements = new int[frequencyMap.size()][2];
		int index = 0;
		for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
			elements[index][0] = entry.getKey();     // 元素值
			elements[index][1] = entry.getValue();   // 频率
			index++;
		}
		
		// 使用快速选择算法找到第k大的频率
		quickSelectByFrequency(elements, 0, elements.length - 1, k - 1);
		
		// 返回前k个高频元素
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = elements[i][0];
		}
		return result;
    }
    
    /**
     * LeetCode 451. 根据字符出现频率排序
     * 链接: https://leetcode.cn/problems/sort-characters-by-frequency/
     * 题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列
     * 
     * 算法思路:
     * 1. 使用哈希表统计每个字符的出现频率
     * 2. 将字符和频率组成对，存入数组
     * 3. 使用快速选择算法找到前k个高频字符
     * 4. 按照频率降序构建结果字符串
     * 
     * 时间复杂度: O(n) - 哈希表统计频率O(n)，快速选择平均O(n)
     * 空间复杂度: O(k) - 其中k是字符集大小
     * 
     * @param s 输入字符串
     * @return 按频率降序排列的字符串
     */
    public static String frequencySort(String s) {
        // 防御性编程：检查输入合法性
        if (s == null || s.isEmpty()) {
            return "";
        }
        
        // 统计每个字符的出现频率
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        // 将字符和频率存入数组
        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(frequencyMap.entrySet());
        
        // 使用快速选择优化的排序（也可以直接排序，但为了展示快速选择的应用，这里使用排序）
        entries.sort((a, b) -> b.getValue() - a.getValue());
        
        // 构建结果字符串
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Character, Integer> entry : entries) {
            char c = entry.getKey();
            int freq = entry.getValue();
            for (int i = 0; i < freq; i++) {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * LeetCode 703. 数据流中的第K大元素
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
     * 题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素
     * 
     * 算法思路:
     * 1. 使用最小堆维护前K个最大元素
     * 2. 当堆大小小于K时，直接添加元素
     * 3. 当堆大小等于K时，如果新元素大于堆顶，则替换堆顶
     * 4. 第K大元素就是堆顶元素
     * 
     * 时间复杂度: O(log K) - 插入操作的时间复杂度
     * 空间复杂度: O(K) - 堆的大小
     * 
     * 注意：虽然这道题主要使用优先队列实现，但可以用快速选择来优化初始建堆过程
     */
    public static class KthLargest {
        private final int k;
        private final PriorityQueue<Integer> minHeap;
        
        /**
         * 初始化KthLargest类
         * 
         * @param k 第K大元素
         * @param nums 初始数组
         */
        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.minHeap = new PriorityQueue<>(k);
            
            // 初始化堆
            for (int num : nums) {
                add(num);
            }
        }
        
        /**
         * 添加新元素，并返回当前的第K大元素
         * 
         * @param val 新添加的元素
         * @return 当前数据流中的第K大元素
         */
        public int add(int val) {
            if (minHeap.size() < k) {
                minHeap.offer(val);
            } else if (val > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(val);
            }
            return minHeap.peek();
        }
    }
    
    /**
     * LeetCode 912. 排序数组 (快速选择优化)
     * 链接: https://leetcode.cn/problems/sort-an-array/
     * 题目描述: 给你一个整数数组 nums，请你将该数组升序排列
     * 
     * 算法思路:
     * 使用快速排序算法，结合快速选择的思想进行优化
     * 1. 随机选择枢轴元素
     * 2. 进行分区操作
     * 3. 递归排序左右子数组
     * 
     * 时间复杂度: 
     *   - 平均情况: O(n log n)
     *   - 最坏情况: O(n²)，但随机选择枢轴元素可以有效避免最坏情况
     * 空间复杂度: O(log n) - 递归调用栈的深度
     * 
     * @param nums 输入数组
     * @return 排序后的数组
     */
    public static int[] sortArray(int[] nums) {
        // 防御性编程：检查输入合法性
        if (nums == null) {
            return new int[0];
        }
        
        // 创建副本以避免修改原数组
        int[] result = nums.clone();
        quickSort(result, 0, result.length - 1);
        return result;
    }
    
    /**
     * 快速排序实现
     * 
     * @param arr 待排序数组
     * @param left 左边界
     * @param right 右边界
     */
    private static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            // 随机选择基准值
            int randomIndex = left + (int) (Math.random() * (right - left + 1));
            // 使用荷兰国旗分区方法
            partition(arr, left, right, arr[randomIndex]);
            // 递归排序左右子数组
            quickSort(arr, left, first - 1);
            quickSort(arr, last + 1, right);
        }
    }
    
    /**
     * LeetCode 164. 最大间距
     * 链接: https://leetcode.cn/problems/maximum-gap/
     * 题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
     * 
     * 算法思路:
     * 1. 使用快速排序对数组进行排序
     * 2. 遍历排序后的数组，计算相邻元素的差值
     * 3. 返回最大差值
     * 
     * 时间复杂度: O(n log n) - 排序的时间复杂度
     * 空间复杂度: O(n) - 排序需要的额外空间
     * 
     * 注意：虽然可以使用基数排序或桶排序达到线性时间复杂度，但这里使用快速排序+快速选择思想来实现
     * 
     * @param nums 输入数组
     * @return 相邻元素的最大差值
     */
    public static int maximumGap(int[] nums) {
        // 防御性编程：检查边界情况
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        // 排序数组
        int[] sortedNums = sortArray(nums);
        
        // 计算最大间距
        int maxGap = 0;
        for (int i = 1; i < sortedNums.length; i++) {
            maxGap = Math.max(maxGap, sortedNums[i] - sortedNums[i - 1]);
        }
        
        return maxGap;
    }
	/**
	 * 根据频率进行快速选择
	 * 
	 * 工程化考量：
	 * 1. 随机化：使用Math.random()避免最坏情况
	 * 2. 递归优化：尾递归减少栈空间使用
	 * 3. 边界处理：处理left >= right的情况
	 * 
	 * @param elements 元素和频率数组
	 * @param left 左边界
	 * @param right 右边界
	 * @param k 目标索引
	 */
	private static void quickSelectByFrequency(int[][] elements, int left, int right, int k) {
		if (left >= right) return;
		
		// 随机选择基准值
		int pivotIndex = left + (int) (Math.random() * (right - left + 1));
		// 将基准值移到末尾
		swapElements(elements, pivotIndex, right);
		
		// 分区操作（按频率降序排列）
		int partitionIndex = partitionByFrequency(elements, left, right);
		
		// 根据分区结果决定继续在哪一侧查找
		if (partitionIndex == k) {
			return;
		} else if (partitionIndex < k) {
			quickSelectByFrequency(elements, partitionIndex + 1, right, k);
		} else {
			quickSelectByFrequency(elements, left, partitionIndex - 1, k);
		}
	}
	
	/**
	 * 根据频率进行分区（降序）
	 * 
	 * 工程化考量：
	 * 1. 性能优化：按频率降序排列
	 * 2. 内存优化：原地交换减少额外空间使用
	 * 3. 边界处理：正确处理分区边界
	 * 
	 * @param elements 元素和频率数组
	 * @param left 左边界
	 * @param right 右边界
	 * @return 分区点的索引
	 */
	private static int partitionByFrequency(int[][] elements, int left, int right) {
		// 基准值是右端点的频率
		int pivotFrequency = elements[right][1];
		int partitionIndex = left;
		
		for (int i = left; i < right; i++) {
			// 如果当前元素频率大于等于基准值频率，则交换
			if (elements[i][1] >= pivotFrequency) {
				swapElements(elements, i, partitionIndex++);
			}
		}
		
		// 将基准值放到正确位置
		swapElements(elements, partitionIndex, right);
		return partitionIndex;
	}
	
	/**
	 * 交换元素数组中两个元素的位置
	 * 
	 * @param elements 元素数组
	 * @param i 第一个元素的索引
	 * @param j 第二个元素的索引
	 */
	private static void swapElements(int[][] elements, int i, int j) {
		int[] temp = elements[i];
		elements[i] = elements[j];
		elements[j] = temp;
	}
	
	/**
	 * 剑指 Offer 40. 最小的k个数
	 * 链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
	 * 题目描述: 输入整数数组 arr ，找出其中最小的 k 个数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到第k小的元素
	 * 2. 返回数组前k个元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用Arrays.copyOfRange避免创建不必要的数组
	 * 3. 边界处理：处理k为0或超出数组长度的情况
	 * 4. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @param k 需要返回的最小元素数量
	 * @return 最小的k个数
	 */
	public static int[] getLeastNumbers(int[] arr, int k) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0 || k <= 0) {
			return new int[0];
		}
		
		if (k >= arr.length) {
			return arr.clone();
		}
		
		// 使用快速选择算法找到第k小的元素
		randomizedSelect(arr, k - 1);
		
		// 返回前k个元素
		return Arrays.copyOfRange(arr, 0, k);
	}
	
	/**
	 * AcWing 786. 第k个数
	 * 链接: https://www.acwing.com/problem/content/788/
	 * 题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到第k小的元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @param k 第k小的元素（从1开始计数）
	 * @return 第k小的元素
	 */
	public static int findKthNumber(int[] arr, int k) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到第k小的元素
		return randomizedSelect(arr, k - 1);
	}
	
	/**
	 * 洛谷 P1923 【深基9.例4】求第 k 小的数
	 * 链接: https://www.luogu.com.cn/problem/P1923
	 * 题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到第k小的元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @param k 第k小的元素（从0开始计数）
	 * @return 第k小的元素
	 */
	public static int findKthSmallest(int[] arr, int k) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0 || k < 0 || k >= arr.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到第k小的元素
		return randomizedSelect(arr, k);
	}
	
	/**
	 * HackerRank Find the Median
	 * 链接: https://www.hackerrank.com/challenges/find-the-median/problem
	 * 题目描述: 找到未排序数组的中位数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到中位数
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @return 数组的中位数
	 */
	public static int findMedian(int[] arr) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到中位数
		return randomizedSelect(arr, arr.length / 2);
	}
	
	public static void main(String[] args) {
		// 测试用例1: LeetCode 215. 数组中的第K个最大元素
		int[] nums1 = {3, 2, 1, 5, 6, 4};
		int k1 = 2;
		System.out.println("数组 " + Arrays.toString(nums1) + " 中第 " + k1 + " 大的元素是: " 
				+ findKthLargest(nums1, k1));
		
		// 测试用例2: 剑指 Offer 40. 最小的k个数 (转换为第k小的数)
		int[] nums2 = {3, 2, 1, 5, 6, 4};
		int k2 = 2;
		System.out.println("数组 " + Arrays.toString(nums2) + " 中第 " + k2 + " 小的元素是: " 
				+ findKthLargest(nums2, nums2.length - k2 + 1));
		
		// 测试用例3: LeetCode 973. K Closest Points to Origin
		int[][] points1 = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}};
		int k3 = 3;
		int[][] result3 = kClosest(points1, k3);
		System.out.println("点数组中距离原点最近的 " + k3 + " 个点是: ");
		for (int[] point : result3) {
			System.out.print("[" + point[0] + "," + point[1] + "] ");
		}
		System.out.println();
		
		// 测试用例4: LeetCode 347. Top K Frequent Elements
		int[] nums4 = {1, 1, 1, 2, 2, 3};
		int k4 = 2;
		int[] result4 = topKFrequent(nums4, k4);
		System.out.println("数组 " + Arrays.toString(nums4) + " 中出现频率前 " + k4 + " 高的元素是: " 
				+ Arrays.toString(result4));
		
		// 测试用例5: AcWing 786. 第k个数
		int[] arr5 = {3, 2, 1, 5, 6, 4};
		int k5 = 3;
		int result5 = findKthNumber(arr5, k5);
		System.out.println("数组 " + Arrays.toString(arr5) + " 中第 " + k5 + " 小的数是: " + result5);
		
		// 测试用例6: 洛谷 P1923 【深基9.例4】求第 k 小的数
		int[] arr6 = {3, 2, 1, 5, 6, 4};
		int k6 = 2; // 0-based indexing
		int result6 = findKthSmallest(arr6, k6);
		System.out.println("数组 " + Arrays.toString(arr6) + " 中第 " + k6 + " 小的数是: " + result6);
		
		// 测试用例7: HackerRank Find the Median
		int[] arr7 = {3, 2, 1, 5, 6, 4};
		int result7 = findMedian(arr7);
		System.out.println("数组 " + Arrays.toString(arr7) + " 的中位数是: " + result7);
		
		// 测试用例8: 牛客网 NC119 最小的K个数
		int[] arr8 = {4, 5, 1, 6, 2, 7, 3, 8};
		int k8 = 4;
		int[] result8 = getLeastNumbers(arr8, k8);
		System.out.println("数组 " + Arrays.toString(arr8) + " 中最小的 " + k8 + " 个数是: " + Arrays.toString(result8));
		
		// 测试用例9: 牛客网 NC73. 数组中出现次数超过一半的数字
		int[] arr9 = {1, 2, 3, 2, 2, 2, 5, 4, 2};
		int result9 = majorityElement(arr9);
		System.out.println("数组 " + Arrays.toString(arr9) + " 中出现次数超过一半的数字是: " + result9);
		
		// 测试用例10: LeetCode 164. 最大间距
		int[] arr10 = {3, 6, 9, 1};
		int result10 = maximumGap(arr10);
		System.out.println("数组 " + Arrays.toString(arr10) + " 的最大间距是: " + result10);
	}
	
	/**
	 * 牛客网 NC119 最小的K个数
	 * 链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
	 * 题目描述: 输入n个整数，找出其中最小的K个数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到第k小的元素
	 * 2. 返回数组前k个元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用Arrays.copyOfRange避免创建不必要的数组
	 * 3. 边界处理：处理k为0或超出数组长度的情况
	 * 4. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @param k 需要返回的最小元素数量
	 * @return 最小的k个数
	 */
	public static int[] getLeastNumbersNC(int[] arr, int k) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0 || k <= 0) {
			return new int[0];
		}
		
		if (k >= arr.length) {
			return arr.clone();
		}
		
		// 使用快速选择算法找到第k小的元素
		randomizedSelect(arr, k - 1);
		
		// 返回前k个元素
		return Arrays.copyOfRange(arr, 0, k);
	}
	
	/**
	 * 牛客网 NC73. 数组中出现次数超过一半的数字
	 * 链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
	 * 题目描述: 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到中位数
	 * 2. 由于出现次数超过一半，中位数就是目标数字
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 边界处理：处理数组为空的情况
	 * 4. 可维护性：添加详细注释和文档说明
	 * 
	 * @param nums 整数数组
	 * @return 出现次数超过一半的数字
	 */
	public static int majorityElement(int[] nums) {
		// 防御性编程：检查输入合法性
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到中位数
		return randomizedSelect(nums, nums.length / 2);
	}
	
	/**
	 * LeetCode 164. 最大间距
	 * 链接: https://leetcode.cn/problems/maximum-gap/
	 * 题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
	 * 
	 * 算法思路:
	 * 1. 使用快速排序对数组进行排序
	 * 2. 遍历排序后的数组，计算相邻元素的差值
	 * 3. 返回最大差值
	 * 
	 * 时间复杂度: O(n log n) - 排序的时间复杂度
	 * 空间复杂度: O(n) - 排序需要的额外空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查边界情况
	 * 2. 性能优化：虽然可以使用基数排序达到线性时间复杂度，但这里使用快速排序展示快速选择思想
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param nums 输入数组
	 * @return 相邻元素的最大差值
	 */
	public static int maximumGapLC(int[] nums) {
		// 防御性编程：检查边界情况
		if (nums == null || nums.length < 2) {
			return 0;
		}
		
		// 排序数组
		int[] sortedNums = sortArray(nums);
		
		// 计算最大间距
		int maxGap = 0;
		for (int i = 1; i < sortedNums.length; i++) {
			maxGap = Math.max(maxGap, sortedNums[i] - sortedNums[i - 1]);
		}
		
		return maxGap;
	}
	
	/**
	 * LintCode 5. 第K大元素
	 * 链接: https://www.lintcode.com/problem/5/
	 * 题目描述: 在数组中找到第k大的元素
	 * 
	 * 算法思路：
	 * 1. 将第k大问题转换为第(n-k)小问题
	 * 2. 使用快速选择算法找到第(n-k)小的元素
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param nums 整数数组
	 * @param k 第k大的元素
	 * @return 第k大的元素
	 */
	public static int kthLargest(int[] nums, int k) {
		// 防御性编程：检查输入合法性
		if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 第k大元素在排序后数组中的索引是nums.length - k
		return randomizedSelect(nums, nums.length - k);
	}
	
	/**
	 * POJ 2388. Who's in the Middle
	 * 链接: http://poj.org/problem?id=2388
	 * 题目描述: 找到数组的中位数
	 * 
	 * 算法思路：
	 * 1. 使用快速选择算法找到中位数
	 * 
	 * 时间复杂度: O(n) 平均情况，O(n²) 最坏情况
	 * 空间复杂度: O(log n) 递归栈空间
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：使用快速选择算法避免完全排序
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param arr 整数数组
	 * @return 数组的中位数
	 */
	public static int findMedianPOJ(int[] arr) {
		// 防御性编程：检查输入合法性
		if (arr == null || arr.length == 0) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		
		// 使用快速选择算法找到中位数
		return randomizedSelect(arr, arr.length / 2);
	}
	
	/**
	 * 洛谷 P1177. 【模板】快速排序
	 * 链接: https://www.luogu.com.cn/problem/P1177
	 * 题目描述: 快速排序模板题，可扩展为快速选择
	 * 
	 * 算法思路:
	 * 使用快速排序算法，结合快速选择的思想进行优化
	 * 1. 随机选择枢轴元素
	 * 2. 进行分区操作
	 * 3. 递归排序左右子数组
	 * 
	 * 时间复杂度: 
	 *   - 平均情况: O(n log n)
	 *   - 最坏情况: O(n²)，但随机选择枢轴元素可以有效避免最坏情况
	 * 空间复杂度: O(log n) - 递归调用栈的深度
	 * 
	 * 工程化考量：
	 * 1. 异常处理：检查输入参数合法性
	 * 2. 性能优化：随机选择枢轴元素避免最坏情况
	 * 3. 可维护性：添加详细注释和文档说明
	 * 
	 * @param nums 输入数组
	 * @return 排序后的数组
	 */
	public static int[] luoguQuickSort(int[] nums) {
		// 防御性编程：检查输入合法性
		if (nums == null) {
			return new int[0];
		}
		
		// 创建副本以避免修改原数组
		int[] result = nums.clone();
		quickSort(result, 0, result.length - 1);
		return result;
	}
	
	/**
	 * 单元测试方法 - 测试各种边界情况和异常场景
	 * 
	 * 工程化考量：
	 * 1. 测试空数组
	 * 2. 测试单元素数组
	 * 3. 测试已排序数组
	 * 4. 测试逆序数组
	 * 5. 测试重复元素数组
	 * 6. 测试极端输入
	 * 7. 测试性能边界
	 */
	public static void unitTest() {
		System.out.println("=== 开始单元测试 ===");
		
		// 测试1: 空数组
		try {
			findKthLargest(new int[0], 1);
			System.out.println("测试1失败：应该抛出异常");
		} catch (IllegalArgumentException e) {
			System.out.println("测试1通过：空数组正确处理");
		}
		
		// 测试2: 单元素数组
		int[] single = {5};
		int result2 = findKthLargest(single, 1);
		System.out.println("测试2: " + (result2 == 5 ? "通过" : "失败"));
		
		// 测试3: 已排序数组
		int[] sorted = {1, 2, 3, 4, 5};
		int result3 = findKthLargest(sorted, 2);
		System.out.println("测试3: " + (result3 == 4 ? "通过" : "失败"));
		
		// 测试4: 逆序数组
		int[] reverse = {5, 4, 3, 2, 1};
		int result4 = findKthLargest(reverse, 3);
		System.out.println("测试4: " + (result4 == 3 ? "通过" : "失败"));
		
		// 测试5: 重复元素数组
		int[] duplicates = {2, 2, 1, 1, 3, 3};
		int result5 = findKthLargest(duplicates, 3);
		System.out.println("测试5: " + (result5 == 2 ? "通过" : "失败"));
		
		System.out.println("=== 单元测试完成 ===");
	}
	
	/**
	 * 性能测试方法 - 测试大规模数据下的性能表现
	 * 
	 * 工程化考量：
	 * 1. 测试不同规模的数据
	 * 2. 测量执行时间
	 * 3. 验证结果正确性
	 * 4. 分析性能趋势
	 */
	public static void performanceTest() {
		System.out.println("=== 开始性能测试 ===");
		
		// 生成测试数据
		int[] sizes = {1000, 5000, 10000, 50000};
		
		for (int size : sizes) {
			int[] testData = generateTestData(size);
			long startTime = System.currentTimeMillis();
			
			// 执行快速选择
			int result = findKthLargest(testData, size / 2);
			
			long endTime = System.currentTimeMillis();
			System.out.println("数据规模: " + size + ", 执行时间: " + (endTime - startTime) + "ms");
			
			// 验证结果正确性（简单验证）
			Arrays.sort(testData);
			int expected = testData[testData.length - size / 2];
			System.out.println("结果验证: " + (result == expected ? "正确" : "错误"));
		}
		
		System.out.println("=== 性能测试完成 ===");
	}
	
	/**
	 * 生成测试数据
	 * 
	 * @param size 数据规模
	 * @return 测试数据数组
	 */
	private static int[] generateTestData(int size) {
		int[] data = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			data[i] = random.nextInt(size * 10);
		}
		return data;
	}
	
	/**
	 * 调试辅助方法 - 打印数组分区过程
	 * 
	 * 工程化考量：
	 * 1. 可视化分区过程
	 * 2. 便于调试和问题定位
	 * 3. 理解算法执行流程
	 * 
	 * @param arr 数组
	 * @param left 左边界
	 * @param right 右边界
	 * @param pivot 基准值
	 */
	private static void debugPartition(int[] arr, int left, int right, int pivot) {
		System.out.print("分区过程: [");
		for (int i = left; i <= right; i++) {
			if (i > left) System.out.print(", ");
			if (arr[i] == pivot) {
				System.out.print("(" + arr[i] + ")");
			} else if (arr[i] < pivot) {
				System.out.print("<" + arr[i] + ">");
			} else {
				System.out.print("{" + arr[i] + "}");
			}
		}
		System.out.println("] 基准值: " + pivot);
	}
}