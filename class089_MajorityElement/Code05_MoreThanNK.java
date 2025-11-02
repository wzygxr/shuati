import java.util.ArrayList;
import java.util.List;

/**
 * 出现次数大于n/k的数
 * 给定一个大小为n的数组nums，给定一个较小的正数k
 * 水王数是指在数组中出现次数大于n/k的数
 * 返回所有的水王数，如果没有水王数返回空列表
 * 
 * 相关题目来源：
 * 1. LeetCode 229. Majority Element II (k=3) - https://leetcode.com/problems/majority-element-ii/
 * 2. LeetCode 229. 多数元素 II（中文版）- https://leetcode.cn/problems/majority-element-ii/
 * 3. GeeksforGeeks Find all array elements occurring more than ⌊N/k⌋ times - https://www.geeksforgeeks.org/find-all-array-elements-occurring-more-than-nk-times/
 * 4. LintCode 47. Majority Element II - https://www.lintcode.com/problem/47/
 * 5. HackerRank Majority Element II - https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/majority-element-ii
 * 6. CodeChef Majority Element II - https://www.codechef.com/practice/arrays
 * 7. AtCoder Beginner Contest 155 C - Poll - 类似思想的投票算法应用
 * 8. Codeforces Round #662 (Div. 2) B - Applejack and Storages - 计数相关应用
 * 9. 牛客网 NC144 - 多数元素 II - https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2
 * 10. 洛谷 P3931 SAC E#1 - 一道难题Tree - https://www.luogu.com.cn/problem/P3931 (相关思想应用)
 *
 * 算法核心思想：
 * 使用扩展的Boyer-Moore投票算法：
 * 1. 维护k-1个候选元素和对应的计数器
 * 2. 遍历数组，按规则更新候选元素和计数器
 * 3. 最后验证候选元素是否满足条件
 *
 * 算法正确性证明：
 * 1. 如果数组中存在出现次数超过n/k的元素，那么它们最终会成为候选元素
 * 2. 因为其他元素的总出现次数不超过(k-1)n/k，无法完全抵消这些多数元素
 * 3. 最后通过验证步骤确保候选元素确实满足条件
 *
 * 时间复杂度分析：
 * - 时间复杂度：O(nk) - 需要遍历数组两次，每次遍历需要处理k-1个候选元素
 *   - 第一次遍历用于找到候选元素：O(nk)
 *   - 第二次遍历用于验证候选元素：O(nk)
 * - 空间复杂度：O(k) - 需要存储k-1个候选元素和对应的计数器
 *
 * 最优解分析：
 * 该解法是比较优的解法，因为：
 * 1. 时间复杂度是O(nk)，当k较小时效率很高
 * 2. 空间复杂度是O(k)，相比使用哈希表计数的O(n)空间解法，此解法在空间上有明显优势
 * 3. 当k较大时，可以考虑使用哈希表计数的方法
 *
 * 工程化考量：
 * 1. 异常处理：处理空数组、单元素数组等边界情况
 * 2. 性能优化：在实际应用中，可以根据k的大小选择不同的算法
 * 3. 线程安全：在多线程环境中需要注意变量的可见性和原子性
 * 4. 代码可读性：使用清晰的变量名和注释提高可维护性
 * 5. 可扩展性：算法可以扩展到不同的k值
 * 6. 鲁棒性：通过验证步骤确保结果的正确性
 *
 * 与其他领域的联系：
 * 1. 机器学习：可以用于多类别投票集成方法中确定最终预测结果
 * 2. 数据挖掘：用于频繁模式挖掘中的频繁项发现
 * 3. 分布式系统：在分布式计算中用于多候选数据聚合和一致性决策
 * 4. 图像处理：在图像分割和特征提取中用于确定主要特征
 * 5. 自然语言处理：用于文本分类和主题建模中的高频特征识别
 */
public class Code05_MoreThanNK {

	/**
	 * 查找数组中出现次数大于n/3的元素（特例k=3）
	 * 
	 * @param nums 输入数组
	 * @return 所有出现次数大于n/3的元素列表
	 */
	public static List<Integer> majorityElement(int[] nums) {
		return majority(nums, 3);
	}

	/**
	 * 查找数组中所有出现次数大于n/k的元素
	 * 
	 * 算法思路：
	 * 1. 使用扩展的Boyer-Moore投票算法维护k-1个候选元素和它们的计数
	 * 2. 第一遍遍历数组找出候选元素
	 * 3. 第二遍遍历数组验证候选元素是否真的满足条件
	 * 
	 * 时间复杂度：O(nk) - 需要遍历数组两次，每次遍历需要处理k-1个候选元素
	 * 空间复杂度：O(k) - 需要存储k-1个候选元素和对应的计数器
	 * 
	 * @param nums 输入数组
	 * @param k 阈值参数
	 * @return 所有出现次数大于n/k的元素列表
	 */
	public static List<Integer> majority(int[] nums, int k) {
		int[][] cands = new int[--k][2];
		for (int num : nums) {
			update(cands, k, num);
		}
		List<Integer> ans = new ArrayList<>();
		collect(cands, k, nums, nums.length, ans);
		return ans;
	}

	/**
	 * 更新候选元素和计数器
	 * 
	 * 算法逻辑：
	 * 1. 如果当前元素等于某个候选元素且计数大于0，则该候选元素计数加1
	 * 2. 否则，如果存在计数为0的候选元素，则将当前元素设为该候选元素，计数设为1
	 * 3. 否则，所有候选元素计数减1（相当于抵消）
	 * 
	 * @param cands 候选元素和计数器数组
	 * @param k 候选元素数量
	 * @param num 当前元素
	 */
	public static void update(int[][] cands, int k, int num) {
		for (int i = 0; i < k; i++) {
			if (cands[i][0] == num && cands[i][1] > 0) {
				cands[i][1]++;
				return;
			}
		}
		for (int i = 0; i < k; i++) {
			if (cands[i][1] == 0) {
				cands[i][0] = num;
				cands[i][1] = 1;
				return;
			}
		}
		for (int i = 0; i < k; i++) {
			if (cands[i][1] > 0) {
				cands[i][1]--;
			}
		}
	}

	/**
	 * 收集并验证候选元素
	 * 
	 * 算法逻辑：
	 * 1. 遍历所有候选元素
	 * 2. 对于每个计数大于0的候选元素，统计其在原数组中的真实出现次数
	 * 3. 如果真实出现次数大于n/(k+1)，则将其加入结果列表
	 * 
	 * @param cands 候选元素和计数器数组
	 * @param k 候选元素数量
	 * @param nums 原数组
	 * @param n 数组长度
	 * @param ans 结果列表
	 */
	public static void collect(int[][] cands, int k, int[] nums, int n, List<Integer> ans) {
		for (int i = 0, cur, real; i < k; i++) {
			if (cands[i][1] > 0) {
				cur = cands[i][0];
				real = 0;
				for (int num : nums) {
					if (cur == num) {
						real++;
					}
				}
				if (real > n / (k + 1)) {
					ans.add(cur);
				}
			}
		}
	}

}