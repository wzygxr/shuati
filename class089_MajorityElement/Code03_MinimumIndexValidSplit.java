import java.util.List;
import java.util.Arrays;

/**
 * 合法分割的最小下标
 * 给定一个下标从 0 开始且全是正整数的数组 nums
 * 如果一个元素在数组中占据主导地位（出现次数严格大于数组长度的一半），则称其为支配元素
 * 一个有效分割是将数组分成 nums[0...i] 和 nums[i+1...n-1] 两部分
 * 要求这两部分的支配元素都存在且等于原数组的支配元素
 * 返回满足条件的最小分割下标 i，如果不存在有效分割，返回 -1
 *
 * 相关题目来源：
 * 1. LeetCode 2780. Minimum Index of a Valid Split - https://leetcode.com/problems/minimum-index-of-a-valid-split/
 * 2. LeetCode 2780. 合法分割的最小下标（中文版）- https://leetcode.cn/problems/minimum-index-of-a-valid-split/
 * 3. GeeksforGeeks Minimum Index of a Valid Split - https://www.geeksforgeeks.org/minimum-index-of-a-valid-split/
 * 4. Codeforces Round #662 (Div. 2) B - Applejack and Storages - 类似思想的计数应用
 * 5. AtCoder Beginner Contest 155 C - Poll - 投票算法的变种应用
 * 6. USACO 2024 January Contest, Bronze Problem 1. Majority Opinion - 类似思想的投票应用
 * 7. 牛客网 NC145 - 合法分割的最小下标 - https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
 * 8. 洛谷 P3932 SAC E#1 - 二道难题Tree - https://www.luogu.com.cn/problem/P3932 (相关思想应用)
 *
 * 题目解析：
 * 需要找到一个最小的分割点，使得分割后的两部分都有支配元素，且都等于原数组的支配元素
 * 
 * 解题思路：
 * 1. 首先找出原数组的支配元素（使用Boyer-Moore投票算法）
 * 2. 统计该元素在整个数组中的出现次数
 * 3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
 * 
 * 算法正确性证明：
 * 1. 如果原数组存在支配元素，那么分割后的两部分也必须包含该支配元素
 * 2. 通过遍历所有可能的分割点，可以找到满足条件的最小分割点
 * 3. 如果不存在有效分割点，则返回-1
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 需要遍历数组三次
 *   - 第一次遍历用于找到候选元素：O(n)
 *   - 第二次遍历用于统计候选元素出现次数：O(n)
 *   - 第三次遍历用于检查所有可能的分割点：O(n)
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
 * 1. 数据分析：用于数据分割和一致性分析
 * 2. 机器学习：可以用于数据集划分和模型验证
 * 3. 分布式系统：在分布式计算中用于数据分片和一致性检查
 * 4. 图像处理：在图像分割和特征提取中用于确定主要特征
 * 5. 自然语言处理：用于文本分割和主题一致性分析
 */

public class Code03_MinimumIndexValidSplit {

	/**
	 * 查找合法分割的最小下标
	 * 
	 * 算法思路：
	 * 1. 使用Boyer-Moore投票算法找出原数组的候选元素
	 * 2. 统计候选元素在整个数组中的出现次数
	 * 3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
	 * 
	 * 时间复杂度：O(n) - 需要遍历数组三次（找候选元素、统计次数、检查分割点）
	 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
	 * 
	 * @param nums 输入列表
	 * @return 满足条件的最小分割下标，如果不存在有效分割返回-1
	 */
	public static int minimumIndex(List<Integer> nums) {
		// 第一步：使用Boyer-Moore投票算法找出候选元素
		int candidate = 0;
		int count = 0;
		
		// 投票阶段：找出可能的支配元素
		for (int num : nums) {
			if (count == 0) {
				candidate = num;
				count = 1;
			} else if (num == candidate) {
				count++;
			} else {
				count--;
			}
		}
		
		// 第二步：统计候选元素在整个数组中的出现次数
		count = 0;
		for (int num : nums) {
			if (num == candidate) {
				count++;
			}
		}
		
		// 第三步：遍历所有可能的分割点，检查是否满足条件
		int n = nums.size();
		int leftCount = 0; // 左半部分中候选元素的出现次数
		
		// 遍历所有可能的分割点 i (0 <= i < n-1)
		for (int i = 0; i < n - 1; i++) {
			// 更新左半部分中候选元素的出现次数
			if (nums.get(i) == candidate) {
				leftCount++;
			}
			
			// 计算右半部分中候选元素的出现次数
			int rightCount = count - leftCount;
			
			// 检查左半部分是否满足支配元素条件
			// 左半部分长度为 i+1，需要候选元素出现次数 > (i+1)/2
			boolean leftValid = leftCount * 2 > (i + 1);
			
			// 检查右半部分是否满足支配元素条件
			// 右半部分长度为 n-i-1，需要候选元素出现次数 > (n-i-1)/2
			boolean rightValid = rightCount * 2 > (n - i - 1);
			
			// 如果两部分都满足条件，则找到了有效分割点
			if (leftValid && rightValid) {
				return i;
			}
		}
		
		// 不存在有效分割点
		return -1;
	}

	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		// 测试用例1: [1,2,2,2] -> 2
		// 原数组支配元素是2，分割点2处，左半部分[1,2,2]支配元素是2，右半部分[2]支配元素是2
		List<Integer> nums1 = Arrays.asList(1, 2, 2, 2);
		System.out.println("输入: [1,2,2,2]");
		System.out.println("输出: " + minimumIndex(nums1));
		
		// 测试用例2: [2,1,3,1,1,1,7,1,2,1] -> 4
		List<Integer> nums2 = Arrays.asList(2, 1, 3, 1, 1, 1, 7, 1, 2, 1);
		System.out.println("输入: [2,1,3,1,1,1,7,1,2,1]");
		System.out.println("输出: " + minimumIndex(nums2));
		
		// 测试用例3: [3,3,3,3,7,2,2] -> -1
		List<Integer> nums3 = Arrays.asList(3, 3, 3, 3, 7, 2, 2);
		System.out.println("输入: [3,3,3,3,7,2,2]");
		System.out.println("输出: " + minimumIndex(nums3));
	}
}