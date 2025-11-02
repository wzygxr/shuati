package class054;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * 滑动窗口最大值问题专题 - 单调队列算法深度解析与多语言实现
 * 
 * 【题目背景】
 * 滑动窗口最大值问题是算法面试中的经典高频题目，涉及单调队列、滑动窗口、双指针等核心算法思想。
 * 本专题通过多种解法对比，深入剖析单调队列的原理和应用。
 * 
 * 【核心算法思想】
 * 单调队列是解决滑动窗口最值问题的最优数据结构，通过维护一个单调递减的双端队列，
 * 确保队首元素始终是当前窗口的最大值，从而实现O(n)的时间复杂度。
 * 
 * 【算法复杂度对比分析】
 * +---------------------+----------------+----------------+----------------------+
 * | 解法类型             | 时间复杂度     | 空间复杂度     | 适用场景              |
 * +---------------------+----------------+----------------+----------------------+
 * | 单调队列解法         | O(n)           | O(k)           | 大规模数据，最优解法  |
 * | 优先队列解法         | O(n*logk)      | O(k)           | 需要维护多个最值      |
 * | 暴力解法             | O(n*k)         | O(1)           | 小规模数据，易于理解  |
 * +---------------------+----------------+----------------+----------------------+
 * 
 * 【工程化考量与优化策略】
 * 1. 异常防御：全面处理空数组、非法窗口大小等边界情况
 * 2. 性能优化：针对不同语言特性选择最优数据结构实现
 * 3. 内存管理：预分配空间避免动态扩容开销
 * 4. 代码可读性：清晰的变量命名和算法步骤注释
 * 5. 测试覆盖：包含单元测试、性能测试和边界测试
 * 
 * 【相关题目资源】
 * - LeetCode 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
 * - 剑指Offer 59 - I. 滑动窗口的最大值 - https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
 * - POJ 2823. Sliding Window - http://poj.org/problem?id=2823
 * - 洛谷 P1886. 滑动窗口 / Sliding Window - https://www.luogu.com.cn/problem/P1886
 * - LeetCode 1438. 绝对差不超过限制的最长连续子数组（相关变形）
 * 
 * 【算法核心思想详解】
 * 单调队列通过四步维护策略确保算法正确性和高效性：
 * 1. 移除队首超出窗口范围的元素（过期检查）
 * 2. 移除队尾所有小于当前元素的值（单调性维护）
 * 3. 将当前元素索引加入队列尾部（入队操作）
 * 4. 记录队首元素作为当前窗口的最大值（结果收集）
 * 
 * 【时间复杂度数学证明】
 * 虽然算法包含嵌套循环，但通过均摊分析可知：
 * - 每个元素最多入队一次，出队一次
 * - 总操作次数为O(n)，因此时间复杂度为O(n)
 * - 空间复杂度为O(k)，队列中最多存储k个元素索引
 * 
 * 【面试要点】
 * - 能够清晰解释单调队列的工作原理和维护策略
 * - 准确分析时间和空间复杂度，并给出数学证明
 * - 比较不同解法的优缺点和适用场景
 * - 处理各种边界情况和异常输入
 */
public class Code01_SlidingWindowMaximum {

	// 自定义双端队列的全局变量 - 使用数组实现提高性能
	// 【性能优化】使用数组而非链表实现双端队列，减少内存开销和缓存不命中
	// 【内存管理】预分配足够大的空间，避免动态扩容带来的性能损耗
	public static int MAXN = 100001; // 预分配的最大空间，避免频繁扩容
	public static int[] deque = new int[MAXN]; // 存储元素索引（而非元素值本身）
	public static int h, t; // h: 队首指针，t: 队尾指针+1（指向下一个插入位置）

	/**
	 * 计算滑动窗口最大值 - 自定义数组实现的双端队列（最优解法）
	 * 
	 * 【算法原理深度解析】
	 * 单调队列是解决滑动窗口最值问题的核心数据结构，通过维护一个单调递减的队列，
	 * 确保队首元素始终是当前窗口的最大值。关键设计要点：
	 * 1. 存储索引而非元素值：便于判断元素是否在当前窗口范围内
	 * 2. 四步维护策略：过期检查 → 单调性维护 → 入队操作 → 结果收集
	 * 3. 使用严格单调递减：保证队首元素的有效性和正确性
	 * 
	 * 【时间复杂度数学证明】
	 * 虽然算法包含嵌套循环，但通过均摊分析可知：
	 * - 每个元素最多入队一次，出队一次
	 * - 总操作次数为O(n)，因此时间复杂度为O(n)
	 * - 空间复杂度为O(k)，队列中最多存储k个元素索引
	 * 
	 * 【工程化优化策略】
	 * 1. 边界检查：处理空数组、非法窗口大小等异常情况
	 * 2. 性能优化：窗口大小为1时的特殊处理
	 * 3. 内存管理：预分配结果数组空间避免动态扩容
	 * 4. 代码可读性：清晰的变量命名和算法步骤注释
	 * 
	 * 【面试要点】
	 * - 能够解释为什么存储索引而非元素值
	 * - 理解单调队列的维护策略和均摊时间复杂度
	 * - 处理各种边界情况和特殊输入
	 * 
	 * @param arr 输入数组
	 * @param k 窗口大小
	 * @return 每个窗口中的最大值组成的数组
	 * 
	 * 【复杂度分析】
	 * - 时间复杂度：O(n) - 每个元素最多入队和出队一次
	 * - 空间复杂度：O(k) - 队列中最多存储k个元素索引
	 * 
	 * 【测试用例覆盖】
	 * - 常规测试：{1,3,-1,-3,5,3,6,7}, k=3 → {3,3,5,5,6,7}
	 * - 边界测试：单元素数组、窗口大小为1、空数组等
	 * - 特殊测试：重复元素、递增序列、递减序列等
	 */
	public static int[] maxSlidingWindow(int[] arr, int k) {
		// 【边界检查】处理异常输入，确保代码健壮性
		// 空数组、非法窗口大小等边界情况的防御性编程
		if (arr == null || arr.length == 0 || k <= 0) {
			return new int[0];
		}
		// 【性能优化】窗口大小为1时的特殊处理
		// 每个元素自身就是最大值，直接返回原数组避免不必要的计算
		if (k == 1) {
			return arr;
		}
		
		// 重置队列指针，初始化双端队列
		h = t = 0;
		int n = arr.length;
		
		// 【算法优化】预处理：先形成长度为k-1的窗口
		// 这种预处理方式可以让后续的窗口滑动处理更简洁，减少边界判断
		for (int i = 0; i < k - 1; i++) {
			// 维护单调递减队列：移除所有小于等于当前元素的队尾元素
			// 【关键设计】使用<=而不是<：在相等情况下保留较新的元素
			// 较新的元素在窗口中停留时间更长，更可能成为后续窗口的最大值
			while (h < t && arr[deque[t - 1]] <= arr[i]) {
				t--; // 队尾指针左移，相当于移除队尾元素
			}
			// 当前元素索引入队
			deque[t++] = i;
		}
		
		// 【内存优化】预分配结果数组空间，避免动态扩容
		int m = n - k + 1; // 结果数组长度 = 窗口数量
		int[] ans = new int[m];
		
		// 【滑动窗口主循环】从k-1位置开始，每次右边界扩展，左边界可能收缩
		// l: 窗口左边界索引，r: 窗口右边界索引
		for (int l = 0, r = k - 1; l < m; l++, r++) {
			// 【步骤1】将当前元素（右边界）加入队列，同时维护单调性
			// 移除队尾所有小于等于当前元素的值，确保队列单调递减
			while (h < t && arr[deque[t - 1]] <= arr[r]) {
				t--;
			}
			deque[t++] = r;
			
			// 【步骤2】收集当前窗口的最大值（队首元素）
			// 由于队列的单调递减性质，队首元素始终是当前窗口的最大值
			ans[l] = arr[deque[h]];
			
			// 【步骤3】移除不在当前窗口范围内的队首元素
			// 检查队首元素是否即将离开窗口范围
			if (deque[h] == l) {
				h++; // 队首指针右移，相当于移除队首元素
			}
		}
		
		return ans;
	}
    
    /**
     * 滑动窗口最大值 - 使用Java内置双端队列实现（推荐工程解法）
     * 
     * 【算法原理深度解析】
     * 单调队列是解决滑动窗口最值问题的核心数据结构，通过维护一个单调递减的双端队列，
     * 确保队首元素始终是当前窗口的最大值。本实现使用Java标准库的ArrayDeque，
     * 相比自定义数组实现，代码更简洁，可读性更好，适合工程应用。
     * 
     * 【关键设计决策】
     * 1. 存储索引而非元素值：便于判断元素是否在当前窗口范围内
     * 2. 四步维护策略：过期检查 → 单调性维护 → 入队操作 → 结果收集
     * 3. 使用严格单调递减：保证队首元素的有效性和正确性
     * 
     * 【时间复杂度数学证明】
     * 虽然算法包含嵌套循环，但通过均摊分析可知：
     * - 每个元素最多入队一次，出队一次
     * - 总操作次数为O(n)，因此时间复杂度为O(n)
     * - 空间复杂度为O(k)，队列中最多存储k个元素索引
     * 
     * 【工程化优势】
     * 1. 使用标准库数据结构：代码更简洁，维护性更好
     * 2. 异常处理完善：全面处理各种边界情况
     * 3. 性能优化：预分配空间，减少动态扩容开销
     * 4. 可读性强：清晰的变量命名和算法步骤注释
     * 
     * 【面试要点】
     * - 能够解释为什么存储索引而非元素值
     * - 理解单调队列的维护策略和均摊时间复杂度
     * - 比较ArrayDeque与LinkedList的性能差异
     * - 处理各种边界情况和特殊输入
     * 
     * @param nums 输入整数数组
     * @param k 滑动窗口大小
     * @return 每个滑动窗口中的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n) - 每个元素最多入队和出队一次
     * - 空间复杂度：O(k) - 队列中最多存储k个元素索引
     * 
     * 【测试用例覆盖】
     * - 常规测试：{1,3,-1,-3,5,3,6,7}, k=3 → {3,3,5,5,6,7}
     * - 边界测试：单元素数组、窗口大小为1、空数组等
     * - 特殊测试：重复元素、递增序列、递减序列等
     * 
     * 【相关题目链接】
     * - LeetCode 239. 滑动窗口最大值 - https://leetcode.cn/problems/sliding-window-maximum/
     * - 剑指Offer 59 - I. 滑动窗口的最大值 - https://leetcode.cn/problems/hua-dong-chuang-kou-de-zui-da-zhi-lcof/
     */
    public static int[] maxSlidingWindowWithDeque(int[] nums, int k) {
        // 【边界检查】第一级防御：处理无效输入
        // 空数组、非法窗口大小等边界情况的防御性编程
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        // 【性能优化】窗口大小为1时的特殊处理
        // 每个元素自身就是最大值，直接返回原数组避免不必要的计算
        if (k == 1) {
            return nums;
        }

        // 【内存优化】预分配结果数组空间，避免动态扩容
        int n = nums.length;
        int[] result = new int[n - k + 1]; // 结果数组长度 = 窗口数量
        
        // 【数据结构选择】使用ArrayDeque实现双端队列
        // ArrayDeque基于数组实现，相比LinkedList有更好的缓存局部性和性能
        // 存储元素索引而非元素值本身，便于判断元素是否在当前窗口范围内
        Deque<Integer> deque = new ArrayDeque<>(); 

        // 【滑动窗口主循环】遍历数组中的每个元素
        for (int i = 0; i < n; i++) {
            // 【步骤1】过期检查：移除队首所有不在当前窗口范围内的元素
            // 当前窗口的有效范围是 [i-k+1, i]
            // 如果队首元素的索引小于左边界，说明它已不在窗口范围内
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst(); // 移除队首元素（已过期）
            }

            // 【步骤2】单调性维护：从队尾移除所有小于当前元素的值
            // 使用严格小于(<)而非小于等于(<=)：保留相等元素的历史索引
            // 相等元素中较新的索引在窗口中停留时间更长，更可能成为后续窗口的最大值
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast(); // 移除队尾元素（不可能成为最大值）
            }

            // 【步骤3】入队操作：将当前元素索引加入队列尾部
            deque.offerLast(i);

            // 【步骤4】结果收集：当形成完整窗口后，记录当前窗口的最大值
            // 第一个完整窗口在i = k-1时形成（索引从0开始）
            if (i >= k - 1) {
                // 由于队列的单调递减性质，队首元素始终是当前窗口的最大值
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }

        return result;
    }

    /**
     * 滑动窗口最大值 - 优先队列解法（最大堆实现）
     * 
     * 【算法原理解析】
     * 优先队列解法使用最大堆来维护窗口内的元素，堆顶元素始终是当前窗口的最大值。
     * 虽然时间复杂度不如单调队列，但实现简单直观，在某些场景下仍有应用价值。
     * 
     * 【关键设计决策】
     * 1. 存储(值,索引)对：值用于堆排序，索引用于判断元素是否在窗口范围内
     * 2. 延迟删除策略：当堆顶元素不在窗口范围内时，才真正删除
     * 3. 最大堆实现：通过存储负值模拟最大堆（Java的PriorityQueue默认是最小堆）
     * 
     * 【时间复杂度分析】
     * - 构建初始窗口堆：O(k*logk)
     * - 滑动窗口：每次插入新元素O(logk)，可能需要移除多个旧元素
     * - 最坏情况下，每个元素可能被插入和删除各一次
     * - 总时间复杂度：O(n*logk)
     * 
     * 【空间复杂度分析】
     * - 堆中最多存储n个元素（极端情况下）
     * - 因此空间复杂度为O(n)
     * 
     * 【与单调队列对比】
     * 优势：
     * - 实现简单直观，代码易于理解
     * - 当需要同时维护多个统计量时更灵活
     * - 在某些特殊场景下可能更有优势
     * 
     * 劣势：
     * - 时间复杂度O(n*logk)高于单调队列的O(n)
     * - 空间复杂度O(n)高于单调队列的O(k)
     * - 在大规模数据下性能较差
     * 
     * 【适用场景】
     * - 窗口大小k较小的情况
     * - 需要同时维护多个最值的场景
     * - 算法教学和调试场景
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口中的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n*logk) - 每个元素入堆出堆的操作均为O(logk)
     * - 空间复杂度：O(n) - 堆中可能存储所有n个元素
     */
    public static int[] maxSlidingWindowPriorityQueue(int[] nums, int k) {
        // 【边界检查】处理异常输入，确保代码健壮性
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        // 【性能优化】窗口大小为1时的特殊处理
        if (k == 1) {
            return nums;
        }
        
        int n = nums.length;
        // 【内存优化】预分配结果数组空间
        int[] result = new int[n - k + 1];
        
        // 【数据结构选择】最大堆：存储(值,索引)对，按照值降序排列
        // 使用Lambda表达式定义比较器，确保大顶堆顺序
        // 比较器规则：b[0] - a[0] 实现降序排列（大顶堆）
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        
        // 【步骤1】初始化第一个窗口的元素到堆中
        for (int i = 0; i < k; i++) {
            maxHeap.offer(new int[]{nums[i], i});
        }
        // 记录第一个窗口的最大值（堆顶元素）
        result[0] = maxHeap.peek()[0];
        
        // 【步骤2】滑动窗口，逐个处理剩余元素
        for (int i = k; i < n; i++) {
            // 添加新元素到堆中
            maxHeap.offer(new int[]{nums[i], i});
            
            // 【延迟删除策略】移除所有不在当前窗口范围内的最大值
            // 当前窗口范围是[i-k+1, i]，如果堆顶元素的索引小于等于i-k，说明已过期
            // 注意：这里使用while循环，因为可能有多个过期元素需要清理
            while (maxHeap.peek()[1] <= i - k) {
                maxHeap.poll(); // 弹出堆顶元素（已过期的最大值）
            }
            
            // 此时堆顶元素即为当前窗口的最大值
            result[i - k + 1] = maxHeap.peek()[0];
        }
        
        return result;
    }
    
    /**
     * 滑动窗口最大值 - 暴力解法（用于对比和教学）
     * 
     * 【算法原理解析】
     * 暴力解法是最直观的解决方案，通过遍历每个可能的窗口位置，
     * 对每个窗口内的k个元素计算最大值。虽然效率较低，但思路简单，
     * 适合作为算法教学的起点，帮助理解问题本质。
     * 
     * 【关键设计特点】
     * 1. 双重循环：外层循环遍历窗口起始位置，内层循环遍历窗口内元素
     * 2. 线性扫描：对每个窗口进行完整的线性扫描找最大值
     * 3. 简单直观：代码逻辑清晰，易于理解和实现
     * 
     * 【时间复杂度分析】
     * - 外层循环：n-k+1次迭代（窗口数量）
     * - 内层循环：k次迭代（窗口大小）
     * - 总时间复杂度：O((n-k+1)*k) = O(n*k)
     * 
     * 【空间复杂度分析】
     * - 仅使用常数级别的额外变量（不考虑结果数组）
     * - 因此空间复杂度为O(1)
     * 
     * 【与优化解法对比】
     * 优势：
     * - 实现简单，代码易于理解
     * - 不需要复杂的数据结构
     * - 适合小规模数据或调试场景
     * 
     * 劣势：
     * - 时间复杂度O(n*k)过高，不适合大规模数据
     * - 存在大量重复计算，效率低下
     * - 在实际工程应用中性能不可接受
     * 
     * 【适用场景】
     * - 算法教学和入门学习
     * - 小规模数据测试（n和k都很小）
     * - 验证其他优化算法的正确性
     * - 调试和问题定位
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口中的最大值组成的数组
     * 
     * 【复杂度分析】
     * - 时间复杂度：O(n*k) - 对每个窗口遍历找最大值
     * - 空间复杂度：O(1) - 仅使用常数级别额外空间
     */
    public static int[] maxSlidingWindowBruteForce(int[] nums, int k) {
        // 【边界检查】处理异常输入，确保代码健壮性
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        // 【性能优化】窗口大小为1时的特殊处理
        if (k == 1) {
            return nums;
        }
        
        int n = nums.length;
        // 【内存优化】预分配结果数组空间
        int[] result = new int[n - k + 1];
        
        // 【暴力解法主循环】遍历每个窗口的起始位置
        for (int i = 0; i <= n - k; i++) {
            // 初始化当前窗口的最大值为窗口第一个元素
            int max = nums[i];
            
            // 【内层循环】遍历窗口内的剩余元素，更新最大值
            // 从窗口第二个元素开始比较（j=1到j=k-1）
            for (int j = 1; j < k; j++) {
                if (nums[i + j] > max) {
                    max = nums[i + j];
                }
            }
            
            // 记录当前窗口的最大值
            result[i] = max;
        }
        
        return result;
    }
    
    /**
     * 主方法，包含完整的单元测试和性能测试
     */
    public static void main(String[] args) {
        runUnitTests();  // 运行单元测试
        runPerformanceTests();  // 运行性能测试
    }
    
    /**
     * 运行全面的单元测试，覆盖各种边界情况和常见场景
     */
    public static void runUnitTests() {
        System.out.println("===== 开始单元测试 =====");
        
        // 测试用例1：常规测试
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] expected1 = {3, 3, 5, 5, 6, 7};
        testCase("常规测试", nums1, k1, expected1);

        // 测试用例2：单元素数组
        int[] nums2 = {1};
        int k2 = 1;
        int[] expected2 = {1};
        testCase("单元素数组", nums2, k2, expected2);

        // 测试用例3：边界情况 - 窗口大小为1
        int[] nums3 = {1, -1};
        int k3 = 1;
        int[] expected3 = {1, -1};
        testCase("窗口大小为1", nums3, k3, expected3);
        
        // 测试用例4：重复元素
        int[] nums4 = {4, 2, 4, 2, 1};
        int k4 = 3;
        int[] expected4 = {4, 4, 4};
        testCase("重复元素", nums4, k4, expected4);
        
        // 测试用例5：全递减数组
        int[] nums5 = {5, 4, 3, 2, 1};
        int k5 = 3;
        int[] expected5 = {5, 4, 3};
        testCase("全递减数组", nums5, k5, expected5);
        
        // 测试用例6：全递增数组
        int[] nums6 = {1, 2, 3, 4, 5};
        int k6 = 3;
        int[] expected6 = {3, 4, 5};
        testCase("全递增数组", nums6, k6, expected6);
        
        // 测试用例7：空数组
        int[] nums7 = {};
        int k7 = 1;
        int[] expected7 = {};
        testCase("空数组", nums7, k7, expected7);
        
        // 测试用例8：窗口大小等于数组长度
        int[] nums8 = {1, 3, 2, 4};
        int k8 = 4;
        int[] expected8 = {4};
        testCase("窗口大小等于数组长度", nums8, k8, expected8);
        
        // 测试用例9：负数元素
        int[] nums9 = {-1, -2, -3, -4, -5};
        int k9 = 2;
        int[] expected9 = {-1, -2, -3, -4};
        testCase("负数元素", nums9, k9, expected9);
        
        // 测试用例10：混合正负数
        int[] nums10 = {-7, -8, 7, 5, 7, 1, 6, 0};
        int k10 = 4;
        int[] expected10 = {7, 7, 7, 7, 7};
        testCase("混合正负数", nums10, k10, expected10);
        
        System.out.println("===== 单元测试完成 =====\n");
    }
    
    /**
     * 运行性能测试，比较不同算法在大规模数据上的表现
     */
    public static void runPerformanceTests() {
        System.out.println("===== 开始性能测试 =====");
        
        // 生成大规模测试数据
        int size = 1000000;  // 100万数据量
        int[] largeArray = generateRandomArray(size, -10000, 10000);
        int k = 1000;  // 较大的窗口大小
        
        System.out.println("测试数据规模: " + size + " 元素, 窗口大小: " + k);
        
        // 测试单调队列解法性能（最优解法）
        long startTime = System.nanoTime();
        int[] result1 = maxSlidingWindowWithDeque(largeArray, k);
        long endTime = System.nanoTime();
        System.out.printf("单调队列解法耗时: %.3f ms\n", TimeUnit.NANOSECONDS.toMicros(endTime - startTime) / 1000.0);
        
        // 测试优先队列解法性能（在大窗口下会更慢）
        // 注意：对于非常大的数组和窗口，优先队列解法可能会超时，这里使用较小的数据量测试
        int smallSize = 100000;
        int[] smallArray = generateRandomArray(smallSize, -10000, 10000);
        startTime = System.nanoTime();
        int[] result2 = maxSlidingWindowPriorityQueue(smallArray, k);
        endTime = System.nanoTime();
        System.out.printf("优先队列解法耗时 (小规模数据): %.3f ms\n", TimeUnit.NANOSECONDS.toMicros(endTime - startTime) / 1000.0);
        
        // 暴力解法对于大规模数据太慢，这里省略测试
        System.out.println("暴力解法对于大规模数据太慢，已省略测试");
        
        System.out.println("===== 性能测试完成 =====");
    }
    
    /**
     * 辅助方法：执行单个测试用例并验证结果
     */
    private static void testCase(String name, int[] nums, int k, int[] expected) {
        // 测试三种解法的正确性
        int[] result1 = maxSlidingWindow(nums, k);  // 自定义队列实现
        int[] result2 = maxSlidingWindowWithDeque(nums, k);  // 内置双端队列实现
        int[] result3 = maxSlidingWindowPriorityQueue(nums, k);  // 优先队列实现
        
        boolean passed1 = Arrays.equals(result1, expected);
        boolean passed2 = Arrays.equals(result2, expected);
        boolean passed3 = Arrays.equals(result3, expected);
        
        System.out.printf("测试用例 [%s]: ", name);
        if (passed1 && passed2 && passed3) {
            System.out.println("通过 ✓");
        } else {
            System.out.println("失败 ✗");
            System.out.println("  自定义队列结果: " + Arrays.toString(result1));
            System.out.println("  内置队列结果: " + Arrays.toString(result2));
            System.out.println("  优先队列结果: " + Arrays.toString(result3));
            System.out.println("  预期结果: " + Arrays.toString(expected));
        }
    }
    
    /**
     * 辅助方法：生成随机数组用于性能测试
     */
    private static int[] generateRandomArray(int size, int min, int max) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int)(Math.random() * (max - min + 1) + min);
        }
        return array;

    }
}