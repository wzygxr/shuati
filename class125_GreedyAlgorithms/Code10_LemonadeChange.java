package class094;

import java.util.Arrays;

// 柠檬水找零 (Lemonade Change)
// 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
// 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。
// 你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
// 注意，一开始你手头没有任何零钱。
// 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。
// 如果你能给每位顾客正确找零，返回 true，否则返回 false。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、资源管理(Resource Management)、状态维护(State Maintenance)
// 时间复杂度: O(n)，其中n是数组长度
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://leetcode.cn/problems/lemonade-change/
// 相关题目: LeetCode 455. 分发饼干、LeetCode 135. 分发糖果
// 贪心算法专题 - 资源分配与最优选择问题集合
public class Code10_LemonadeChange {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：找零时优先使用大面额纸币，
	 *    这样可以保留更多小面额纸币用于后续找零
	 * 2. 状态维护：通过维护5美元和10美元纸币的数量，
	 *    实时跟踪当前可用的零钱状态
	 * 3. 找零规则：
	 *    - 收到5美元：无需找零，直接增加5美元纸币数量
	 *    - 收到10美元：需找零5美元，消耗一张5美元纸币
	 *    - 收到20美元：需找零15美元，优先使用一张10美元+一张5美元
	 *      若无10美元则使用三张5美元
	 *
	 * 时间复杂度分析：
	 * - O(n)，其中n是数组长度，只需要一次遍历即可完成判断
	 * 空间复杂度分析：
	 * - O(1)，仅使用了常数级别的额外空间存储five和ten变量
	 * 是否最优解：是，这是处理此类找零问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组等
	 * 3. 性能优化：采用单次遍历策略，避免重复计算
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 状态一致性：确保每次交易后零钱状态的正确性
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：bills为空数组或null时直接返回true
	 * 2. 单一面额场景：只收到5美元或只收到大面额纸币
	 * 3. 连续大额场景：连续收到10美元或20美元的处理
	 * 4. 零钱不足场景：无法找零时的处理
	 * 5. 特殊序列场景：如[5,5,10,20,5,5,5,5,5,5,5,5,5,10,5,5,20,5,20,5]
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用增强for循环遍历数组，注意整数比较
	 * 2. C++实现：使用传统for循环，注意数组边界检查
	 * 3. Python实现：使用for循环，利用列表特性
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印当前纸币面额和零钱数量
	 * 2. 断言验证：在每次交易后添加断言确保零钱数量非负
	 * 3. 性能监控：跟踪遍历过程的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 零售系统：收银台找零策略优化
	 * 2. 自动售货机：硬币分配算法
	 * 3. 银行系统：ATM取款面额分配
	 * 4. 游戏经济：虚拟货币找零机制
	 * 5. 交通收费：公交地铁找零系统
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：优先使用大面额纸币确保后续找零能力
	 * 2. 状态转移：每次交易后正确更新零钱状态
	 * 3. 正确性证明：通过归纳法可以证明贪心策略的正确性
	 * 4. 优化扩展：可扩展支持更多面额纸币的找零
	 */
	public static boolean lemonadeChange(int[] bills) {
		// 异常处理：检查输入是否为空
		if (bills == null) {
			return true;  // 空数组表示没有顾客，返回true
		}
		
		int five = 0;   // 5美元纸币数量
		int ten = 0;    // 10美元纸币数量
		
		// 遍历顾客支付的纸币
		for (int bill : bills) {
			if (bill == 5) {
				// 收到5美元，不需要找零
				five++;
			} else if (bill == 10) {
				// 收到10美元，需要找零5美元
				if (five > 0) {
					five--;
					ten++;
				} else {
					// 没有5美元找零，返回false
					return false;
				}
			} else if (bill == 20) {
				// 收到20美元，需要找零15美元
				// 贪心策略：优先使用一张10美元和一张5美元找零
				if (ten > 0 && five > 0) {
					ten--;
					five--;
				} else if (five >= 3) {
					// 如果没有10美元，则用三张5美元找零
					five -= 3;
				} else {
					// 无法找零，返回false
					return false;
				}
			}
		}
		
		// 所有顾客都能正确找零
		return true;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] bills1 = {5, 5, 5, 10, 20};
		System.out.println("测试用例1结果: " + lemonadeChange(bills1)); // 期望输出: true
		
		// 测试用例2
		int[] bills2 = {5, 5, 10, 10, 20};
		System.out.println("测试用例2结果: " + lemonadeChange(bills2)); // 期望输出: false
		
		// 测试用例3
		int[] bills3 = {10, 10};
		System.out.println("测试用例3结果: " + lemonadeChange(bills3)); // 期望输出: false
		
		// 测试用例4
		int[] bills4 = {5, 5, 10, 20, 5, 5, 5, 5, 5, 5, 5, 5, 5, 10, 5, 5, 20, 5, 20, 5};
		System.out.println("测试用例4结果: " + lemonadeChange(bills4)); // 期望输出: true
		
		// 测试用例5：边界情况
		int[] bills5 = {};
		System.out.println("测试用例5结果: " + lemonadeChange(bills5)); // 期望输出: true
	}

	// 补充题目1: LeetCode 455. 分发饼干 (Assign Cookies)
	// 题目描述: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
	// 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
	// 并且每块饼干 j，都有一个尺寸 s[j]。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i，
	// 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、双指针(Double Pointers)、排序(Sorting)
	// 时间复杂度: O(m*log(m)+n*log(n))，其中m是孩子数量，n是饼干数量
	// 空间复杂度: O(1)，仅使用常数额外空间
	// 链接: https://leetcode.cn/problems/assign-cookies/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：优先满足胃口小的孩子，
	 *    这样可以保留大饼干给胃口大的孩子
	 * 2. 排序优化：对孩子胃口和饼干尺寸都进行排序，
	 *    为贪心策略实施奠定基础
	 * 3. 匹配机制：使用双指针技术进行高效匹配
	 *
	 * 算法步骤详解：
	 * 1. 预处理：对两个数组进行升序排序
	 * 2. 初始化：设置孩子和饼干的双指针
	 * 3. 匹配过程：
	 *    - 若当前饼干能满足当前孩子则两个指针都前进
	 *    - 否则仅饼干指针前进寻找更大饼干
	 * 4. 结果返回：孩子指针位置即为满足的孩子数
	 *
	 * 算法优化与正确性：
	 * 贪心选择性质：优先满足小胃口是最优的局部选择
	 * 最优子结构：满足一个孩子后剩余问题仍保持最优性
	 */
	public static int findContentChildren(int[] g, int[] s) {
		// 异常处理：检查输入是否为空
		if (g == null || s == null || g.length == 0 || s.length == 0) {
			return 0; // 空数组，无法满足任何孩子
		}
		
		// 贪心策略：对胃口值和饼干尺寸进行排序，尽可能用最小的能满足孩子胃口的饼干
		Arrays.sort(g); // 对孩子的胃口排序
		Arrays.sort(s); // 对饼干尺寸排序
		
		int childIndex = 0; // 当前考虑的孩子索引
		int cookieIndex = 0; // 当前考虑的饼干索引
		
		// 遍历饼干和孩子
		while (childIndex < g.length && cookieIndex < s.length) {
			// 如果当前饼干能满足当前孩子的胃口
			if (s[cookieIndex] >= g[childIndex]) {
				childIndex++; // 孩子得到满足，移动到下一个孩子
			}
			// 无论如何都移动到下一个饼干
			cookieIndex++;
		}
		
		// 返回满足的孩子数量
		return childIndex;
	}

	// 补充题目2: LeetCode 135. 分发糖果 (Candy)
	// 题目描述: n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
	// 你需要按照以下要求，给这些孩子分发糖果：
	// 1. 每个孩子至少分配到 1 个糖果
	// 2. 相邻两个孩子评分更高的孩子会获得更多的糖果
	// 请你给每个孩子分发糖果，计算并返回需要准备的最小糖果数目。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、两次遍历(Two Pass)、动态规划思想(DP Thinking)
	// 时间复杂度: O(n)，其中n是孩子数量
	// 空间复杂度: O(n)，需要额外数组存储糖果数
	// 链接: https://leetcode.cn/problems/candy/
	/*
	 * 算法详解与策略分析：
	 * 1. 两次遍历策略：通过从左到右和从右到左两次遍历，
	 *    分别处理左右相邻约束条件
	 * 2. 左遍历目的：确保评分高的右孩子比左孩子获得更多糖果
	 * 3. 右遍历目的：确保评分高的左孩子比右孩子获得更多糖果
	 * 4. 结果合并：对每个孩子取两次遍历结果的最大值
	 *
	 * 算法步骤详解：
	 * 1. 初始化：为每个孩子分配1个糖果作为基础
	 * 2. 左遍历：
	 *    - 若右孩子评分高于左孩子则右孩子糖果数=左孩子+1
	 * 3. 右遍历：
	 *    - 若左孩子评分高于右孩子则左孩子糖果数=max(当前值,右孩子+1)
	 * 4. 结果统计：累加所有孩子的糖果数
	 *
	 * 算法优化与实现细节：
	 * 空间优化：可优化为O(1)空间的单次遍历算法
	 * 正确性保证：两次遍历确保满足所有约束条件
	 */
	public static int candy(int[] ratings) {
		// 异常处理：检查输入是否为空
		if (ratings == null || ratings.length == 0) {
			return 0; // 空数组，不需要糖果
		}
		
		int n = ratings.length;
		int[] candies = new int[n];
		
		// 初始化：每个孩子至少有1个糖果
		for (int i = 0; i < n; i++) {
			candies[i] = 1;
		}
		
		// 贪心策略：从左到右遍历，确保右边评分高的孩子得到更多糖果
		for (int i = 1; i < n; i++) {
			if (ratings[i] > ratings[i - 1]) {
				candies[i] = candies[i - 1] + 1;
			}
		}
		
		// 贪心策略：从右到左遍历，确保左边评分高的孩子得到更多糖果
		for (int i = n - 2; i >= 0; i--) {
			if (ratings[i] > ratings[i + 1]) {
				candies[i] = Math.max(candies[i], candies[i + 1] + 1);
			}
		}
		
		// 计算总糖果数
		int total = 0;
		for (int candy : candies) {
			total += candy;
		}
		
		return total;
	}

	// 补充题目3: LeetCode 605. 种花问题 (Can Place Flowers)
	// 题目描述: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
	// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
	// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
	// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)、约束满足(Constraint Satisfaction)
	// 时间复杂度: O(n)，其中n是花坛长度
	// 空间复杂度: O(1)，原地修改数组
	// 链接: https://leetcode.cn/problems/can-place-flowers/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：遍历花坛，尽可能多地种花，
	 *    在满足约束条件下优先种花
	 * 2. 约束检查：当前位置可以种花当且仅当：
	 *    - 当前位置为0（未种花）
	 *    - 前一个位置为0或不存在
	 *    - 后一个位置为0或不存在
	 * 3. 优化终止：一旦满足n朵花就提前返回
	 *
	 * 算法步骤详解：
	 * 1. 初始化：设置可种花计数器
	 * 2. 遍历过程：
	 *    - 检查当前位置是否满足种花条件
	 *    - 若满足则在当前位置种花并增加计数器
	 *    - 检查是否已满足n朵花，满足则提前返回true
	 * 3. 结果判断：返回累计种花数是否大于等于n
	 *
	 * 算法优化与边界处理：
	 * 边界情况：处理花坛首尾位置的约束检查
	 * 空间优化：可原地修改数组标记已种花位置
	 */
	public static boolean canPlaceFlowers(int[] flowerbed, int n) {
		// 异常处理：检查输入是否为空
		if (flowerbed == null) {
			return n <= 0; // 空数组，只能种0朵花
		}
		
		if (n <= 0) {
			return true; // 需要种0朵花，直接返回true
		}
		
		int count = 0; // 可以种植的花的数量
		int len = flowerbed.length;
		
		// 贪心策略：遍历每个位置，如果可以种花就种
		for (int i = 0; i < len; i++) {
			// 检查当前位置、前一个位置和后一个位置是否都没有花
			boolean leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
			boolean rightEmpty = (i == len - 1) || (flowerbed[i + 1] == 0);
			
			if (flowerbed[i] == 0 && leftEmpty && rightEmpty) {
				count++; // 可以种花
				flowerbed[i] = 1; // 标记为已种花
				
				// 提前结束，如果已经能满足需求
				if (count >= n) {
					return true;
				}
			}
		}
		
		return count >= n;
	}

	// 补充题目4: LeetCode 406. 根据身高重建队列 (Queue Reconstruction by Height)
	// 题目描述: 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
	// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
	// 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，
	// 其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、排序(Sorting)、链表插入(Linked List Insertion)
	// 时间复杂度: O(n²)，其中n是人数
	// 空间复杂度: O(n)，使用List存储结果
	// 链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
	/*
	 * 算法详解与策略分析：
	 * 1. 排序策略：身高降序，k值升序
	 * 2. 贪心策略：按排序后的顺序插入到指定位置
	 * 3. 插入位置：k值即为插入位置
	 * 4. 数据结构：使用List支持动态插入
	 *
	 * 算法步骤详解：
	 * 1. 排序阶段：
	 *    - 按身高降序排列
	 *    - 身高相同时按k值升序排列
	 * 2. 插入过程：
	 *    - 依次处理排序后的人群
	 *    - 将当前人插入到结果列表的p[1]位置
	 * 3. 结果生成：将List转换为数组返回
	 *
	 * 算法优化与正确性：
	 * 贪心选择性质：先处理高个子可以确保后续插入不影响前面排列
	 * 最优子结构：每步插入都保持当前队列的正确性
	 */
	public static int[][] reconstructQueue(int[][] people) {
		// 异常处理：检查输入是否为空
		if (people == null || people.length <= 1) {
			return people; // 空数组或只有一个人，直接返回
		}
		
		// 贪心策略：按身高降序排序，身高相同时按ki升序排序
		Arrays.sort(people, (a, b) -> {
			if (a[0] != b[0]) {
				return b[0] - a[0]; // 身高降序
			} else {
				return a[1] - b[1]; // 身高相同时，ki升序
			}
		});
		
		// 使用链表插入，提高插入效率
		java.util.List<int[]> result = new java.util.LinkedList<>();
		
		// 遍历排序后的数组，根据ki插入到相应位置
		for (int[] person : people) {
			result.add(person[1], person); // 插入到索引为ki的位置
		}
		
		// 转换为二维数组返回
		return result.toArray(new int[result.size()][]);
	}

	// 补充题目5: LeetCode 1005. K 次取反后最大化的数组和 (Maximize Sum Of Array After K Negations)
	// 题目描述: 给你一个整数数组 nums 和一个整数 k ，按以下方法修改数组：
	// 1. 选择某个下标 i 并将 nums[i] 替换为 -nums[i] 。
	// 你可以重复这个过程恰好 k 次。你也可以选择同一个下标 i 多次。
	// 以这种方式修改数组后，返回数组 可能的最大和 。
	// 
	// 算法标签: 贪心算法(Greedy Algorithm)、数组操作(Array Operations)、排序(Sorting)
	// 时间复杂度: O(n*log(n))，其中n是数组长度
	// 空间复杂度: O(1)，原地修改数组
	// 链接: https://leetcode.cn/problems/maximize-sum-of-array-after-k-negations/
	/*
	 * 算法详解与策略分析：
	 * 1. 贪心策略核心：每次将绝对值最大的负数变为正数，
	 *    这样可以最大化数组和
	 * 2. 分阶段处理：
	 *    - 优先将负数变为正数
	 *    - 若还有剩余操作次数且为奇数，将最小正数取反
	 * 3. 排序优化：通过排序快速找到需要操作的元素
	 *
	 * 算法步骤详解：
	 * 1. 预处理：对数组进行升序排序
	 * 2. 负数处理：
	 *    - 将尽可能多的负数变为正数
	 * 3. 剩余处理：
	 *    - 若剩余操作次数为奇数，将最小元素取反
	 * 4. 结果计算：计算数组元素和
	 *
	 * 算法优化与实现细节：
	 * 边界处理：处理k为0或数组全为正数的情况
	 * 正确性保证：贪心策略确保每次操作都能最大化数组和
	 */
	public static int largestSumAfterKNegations(int[] nums, int k) {
		// 异常处理：检查输入是否为空
		if (nums == null || nums.length == 0) {
			return 0; // 空数组，和为0
		}
		
		// 贪心策略：每次将最小的负数变为正数，这样可以最大化数组和
		// 排序数组，从小到大
		Arrays.sort(nums);
		int i = 0;
		
		// 尽可能将负数变为正数
		while (i < nums.length && k > 0 && nums[i] < 0) {
			nums[i] = -nums[i]; // 取反
			k--; // 减少取反次数
			i++; // 移动到下一个元素
		}
		
		// 如果k还有剩余，且k是奇数，需要将最小的正数取反
		if (k > 0 && k % 2 == 1) {
			// 重新排序，找到最小的数
			Arrays.sort(nums);
			nums[0] = -nums[0]; // 将最小的数取反
		}
		
		// 计算数组和
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		return sum;
	}
}