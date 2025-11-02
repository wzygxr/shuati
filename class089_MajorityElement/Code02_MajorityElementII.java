import java.util.ArrayList;
import java.util.List;

/**
 * 多数元素 II
 * 给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
 *
 * 相关题目来源：
 * 1. LeetCode 229. Majority Element II - https://leetcode.com/problems/majority-element-ii/
 * 2. LeetCode 229. 多数元素 II（中文版）- https://leetcode.cn/problems/majority-element-ii/
 * 3. GeeksforGeeks Find all array elements occurring more than ⌊N/3⌋ times - https://www.geeksforgeeks.org/dsa/find-all-array-elements-occurring-more-than-floor-of-n-divided-by-3-times/
 * 4. LintCode 47. Majority Element II - https://www.lintcode.com/problem/47/
 * 5. HackerRank Majority Element II - https://www.hackerrank.com/contests/bits-hyderabad-practice-test-1/challenges/majority-element-ii
 * 6. CodeChef Majority Element II - https://www.codechef.com/practice/arrays
 * 7. AtCoder Beginner Contest 155 C - Poll - 类似思想的投票算法应用
 * 8. Codeforces Round #662 (Div. 2) B - Applejack and Storages - 计数相关应用
 * 9. 牛客网 NC144 - 多数元素 II - https://www.nowcoder.com/practice/79165152ac2b4a28804947ed1981e0c2
 * 10. 洛谷 P3931 SAC E#1 - 一道难题Tree - https://www.luogu.com.cn/problem/P3931 (相关思想应用)
 *
 * 题目解析：
 * 需要找出数组中出现次数超过 n/3 的元素
 * 由于数组中最多只能有2个这样的元素（因为如果3个元素都出现超过n/3次，总数会超过n），我们可以使用扩展的Boyer-Moore投票算法
 * 
 * 解题思路：
 * 1. 使用Boyer-Moore投票算法的扩展版本，维护两个候选元素和它们的计数
 * 2. 第一遍遍历数组，找出两个候选元素
 * 3. 第二遍遍历数组，验证候选元素是否真的出现超过 n/3 次
 * 
 * 算法正确性证明：
 * 1. 如果数组中存在出现次数超过n/3的元素，那么它们最终会成为候选元素
 * 2. 因为其他元素的总出现次数不超过2n/3，无法完全抵消这些多数元素
 * 3. 最后通过验证步骤确保候选元素确实满足条件
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 需要遍历数组两次
 *   - 第一次遍历用于找到候选元素：O(n)
 *   - 第二次遍历用于验证候选元素：O(n)
 * - 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 该解法是最优解，因为：
 * 1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
 * 2. 空间复杂度也是最优的，只使用了常数级别的额外空间
 * 3. 相比使用哈希表计数的O(n)空间解法，此解法在空间上有明显优势
 * 
 * 工程化考量：
 * 1. 异常处理：处理空数组、单元素数组等边界情况
 * 2. 性能优化：在实际应用中，可以根据数据分布情况优化验证步骤
 * 3. 线程安全：在多线程环境中需要注意变量的可见性和原子性
 * 4. 代码可读性：使用清晰的变量名和注释提高可维护性
 * 5. 可扩展性：算法可以扩展到寻找超过n/k次的元素
 * 6. 鲁棒性：通过验证步骤确保结果的正确性
 * 
 * 与其他领域的联系：
 * 1. 机器学习：可以用于多类别投票集成方法中确定最终预测结果
 * 2. 数据挖掘：用于频繁模式挖掘中的频繁项发现
 * 3. 分布式系统：在分布式计算中用于多候选数据聚合和一致性决策
 * 4. 图像处理：在图像分割和特征提取中用于确定主要特征
 * 5. 自然语言处理：用于文本分类和主题建模中的高频特征识别
 */

public class Code02_MajorityElementII {

	/**
	 * 查找数组中所有出现次数超过 ⌊ n/3 ⌋ 次的元素
	 * 
	 * 算法思路：
	 * 1. 使用扩展的Boyer-Moore投票算法维护两个候选元素和它们的计数
	 * 2. 第一遍遍历数组找出候选元素
	 * 3. 第二遍遍历数组验证候选元素是否真的满足条件
	 * 
	 * 时间复杂度：O(n) - 需要遍历数组两次
	 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
	 * 
	 * @param nums 输入数组
	 * @return 所有出现次数超过 ⌊ n/3 ⌋ 次的元素列表
	 */
	public static List<Integer> majorityElement(int[] nums) {
		// 初始化两个候选元素和它们的计数
		int cand1 = 0, cand2 = 0;
		int count1 = 0, count2 = 0;
		
		// 第一遍遍历，找出候选元素
		// Boyer-Moore投票算法的核心思想：
		// 1. 如果当前元素等于候选元素，则计数加1
		// 2. 如果当前元素不等于任何候选元素：
		//    a. 如果某个候选元素计数为0，则替换该候选元素为当前元素
		//    b. 否则所有候选元素计数减1（相当于抵消）
		for (int num : nums) {
			if (count1 > 0 && num == cand1) {
				// 当前元素等于第一个候选元素，计数加1
				count1++;
			} else if (count2 > 0 && num == cand2) {
				// 当前元素等于第二个候选元素，计数加1
				count2++;
			} else if (count1 == 0) {
				// 第一个候选元素计数为0，替换为当前元素
				cand1 = num;
				count1 = 1;
			} else if (count2 == 0) {
				// 第二个候选元素计数为0，替换为当前元素
				cand2 = num;
				count2 = 1;
			} else {
				// 当前元素不等于任何候选元素，且两个候选元素计数都大于0
				// 则两个候选元素计数都减1（相当于抵消）
				count1--;
				count2--;
			}
		}
		
		// 第二遍遍历，统计候选元素的真实出现次数
		count1 = 0;
		count2 = 0;
		for (int num : nums) {
			if (num == cand1) {
				count1++;
			} else if (num == cand2) {
				count2++;
			}
		}
		
		// 构造结果列表
		List<Integer> result = new ArrayList<>();
		int n = nums.length;
		// 验证候选元素是否真的出现超过 n/3 次
		if (count1 > n / 3) {
			result.add(cand1);
		}
		if (count2 > n / 3) {
			result.add(cand2);
		}
		
		return result;
	}

	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		// 测试用例1: [3,2,3] -> [3]
		int[] nums1 = {3, 2, 3};
		System.out.println("输入: [3,2,3]");
		System.out.println("输出: " + majorityElement(nums1));
		
		// 测试用例2: [1] -> [1]
		int[] nums2 = {1};
		System.out.println("输入: [1]");
		System.out.println("输出: " + majorityElement(nums2));
		
		// 测试用例3: [1,2] -> [1,2]
		int[] nums3 = {1, 2};
		System.out.println("输入: [1,2]");
		System.out.println("输出: " + majorityElement(nums3));
		
		// 测试用例4: [2,2,1,1,1,2,2] -> [1,2]
		int[] nums4 = {2, 2, 1, 1, 1, 2, 2};
		System.out.println("输入: [2,2,1,1,1,2,2]");
		System.out.println("输出: " + majorityElement(nums4));
	}
}