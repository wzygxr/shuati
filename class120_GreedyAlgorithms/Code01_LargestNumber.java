package class089;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 最大数 - 贪心算法解决方案
 * 
 * 题目描述：
 * 给定一组非负整数nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数
 * 
 * 测试链接：https://leetcode.cn/problems/largest-number/
 * 
 * 算法思想：
 * 使用贪心算法 + 自定义排序，关键是比较两个字符串a和b时，比较a+b和b+a的大小
 * 如果a+b > b+a，则a应该排在b前面，这样拼接后的结果最大
 * 
 * 时间复杂度分析：
 * O(n*logn*m) - 其中n是数组长度，m是数字的平均位数
 * - 排序需要O(n*logn)次比较
 * - 每次比较需要O(m)时间（字符串拼接和比较）
 * 
 * 空间复杂度分析：
 * O(n*m) - 需要将整数转换为字符串存储
 * 
 * 是否为最优解：
 * 是，这是解决该问题的最优解
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理全为0的特殊情况
 * 2. 输入验证：检查输入参数的有效性
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：使用清晰的变量命名和详细的注释
 * 
 * 贪心策略证明：
 * 对于任意两个数字a和b，如果a+b > b+a，则a应该排在b前面
 * 这种排序方式满足传递性，因此可以得到全局最优解
 */
public class Code01_LargestNumber {

	/**
	 * 暴力方法：生成所有可能的排列，选择字典序最小的结果
	 * 用于验证贪心算法的正确性
	 * 
	 * @param strs 字符串数组
	 * @return 字典序最小的拼接结果
	 * 
	 * 时间复杂度：O(n! * n) - 生成所有排列并比较
	 * 空间复杂度：O(n!) - 存储所有排列结果
	 */
	public static String way1(String[] strs) {
		ArrayList<String> ans = new ArrayList<>();
		f(strs, 0, ans);
		ans.sort((a, b) -> a.compareTo(b));
		return ans.get(0);
	}

	/**
	 * 递归生成所有排列
	 * 
	 * @param strs 字符串数组
	 * @param i 当前处理的位置
	 * @param ans 存储所有排列结果的列表
	 */
	public static void f(String[] strs, int i, ArrayList<String> ans) {
		if (i == strs.length) {
			StringBuilder path = new StringBuilder();
			for (String s : strs) {
				path.append(s);
			}
			ans.add(path.toString());
		} else {
			for (int j = i; j < strs.length; j++) {
				swap(strs, i, j);
				f(strs, i + 1, ans);
				swap(strs, i, j);
			}
		}
	}

	/**
	 * 交换数组中两个元素的位置
	 * 
	 * @param strs 字符串数组
	 * @param i 第一个索引
	 * @param j 第二个索引
	 */
	public static void swap(String[] strs, int i, int j) {
		String tmp = strs[i];
		strs[i] = strs[j];
		strs[j] = tmp;
	}

	/**
	 * 贪心算法：使用自定义排序规则
	 * 
	 * @param strs 字符串数组
	 * @return 字典序最小的拼接结果
	 * 
	 * 时间复杂度：O(n*logn)
	 * 空间复杂度：O(n)
	 */
	public static String way2(String[] strs) {
		Arrays.sort(strs, (a, b) -> (a + b).compareTo(b + a));
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			path.append(strs[i]);
		}
		return path.toString();
	}

	/**
	 * 生成长度1~n的随机字符串数组
	 * 
	 * @param n 最大数组长度
	 * @param m 最大字符串长度
	 * @param v 字符种类数
	 * @return 随机字符串数组
	 */
	public static String[] randomStringArray(int n, int m, int v) {
		String[] ans = new String[(int) (Math.random() * n) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = randomString(m, v);
		}
		return ans;
	}

	/**
	 * 生成长度1~m，字符种类有v种的随机字符串
	 * 
	 * @param m 最大字符串长度
	 * @param v 字符种类数
	 * @return 随机字符串
	 */
	public static String randomString(int m, int v) {
		int len = (int) (Math.random() * m) + 1;
		char[] ans = new char[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (char) ('a' + (int) (Math.random() * v));
		}
		return String.valueOf(ans);
	}

	/**
	 * 对数器：验证贪心算法的正确性
	 * 
	 * 测试策略：
	 * 1. 生成大量随机测试用例
	 * 2. 比较暴力解法和贪心解法的结果
	 * 3. 如果结果不一致，输出错误信息
	 */
	public static void main(String[] args) {
		int n = 8; // 数组中最多几个字符串
		int m = 5; // 字符串长度最大多长
		int v = 4; // 字符的种类有几种
		int testTimes = 2000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTimes; i++) {
			String[] strs = randomStringArray(n, m, v);
			String ans1 = way1(strs);
			String ans2 = way2(strs);
			if (!ans1.equals(ans2)) {
				// 如果出错了，可以增加打印行为找到一组出错的例子，然后去debug
				System.out.println("出错了！");
			}
			if (i % 100 == 0) {
				System.out.println("测试到第" + i + "组");
			}
		}
		System.out.println("测试结束");
	}

	/**
	 * 解决最大数问题的核心方法
	 * 
	 * @param nums 非负整数数组
	 * @return 重新排列后组成的最大整数（字符串形式）
	 * 
	 * 算法步骤：
	 * 1. 将整数数组转换为字符串数组
	 * 2. 使用自定义比较器对字符串数组进行排序
	 * 3. 处理全为0的特殊情况
	 * 4. 拼接排序后的字符串
	 * 
	 * 特殊处理：
	 * 如果排序后的第一个字符串是"0"，说明所有数字都是0，直接返回"0"
	 * 避免出现"000"这样的结果，应该返回"0"
	 */
	public static String largestNumber(int[] nums) {
		// 输入验证
		if (nums == null || nums.length == 0) {
			return "0";
		}
		
		int n = nums.length;
		String[] strs = new String[n];
		for (int i = 0; i < n; i++) {
			strs[i] = String.valueOf(nums[i]);
		}
		
		// 自定义排序：比较a+b和b+a的大小
		// 如果b+a > a+b，则a应该排在b前面（降序排列）
		Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));
		
		// 处理全为0的特殊情况
		if (strs[0].equals("0")) {
			return "0";
		}
		
		// 拼接所有字符串
		StringBuilder path = new StringBuilder();
		for (String s : strs) {
			path.append(s);
		}
		return path.toString();
	}

	/**
	 * 测试函数：验证最大数算法的正确性
	 */
	public static void testLargestNumber() {
		// 测试用例1: [10,2]
		int[] nums1 = {10, 2};
		String result1 = largestNumber(nums1);
		System.out.println("输入: [10,2]");
		System.out.println("输出: " + result1);
		System.out.println("预期: \"210\"");
		System.out.println();
		
		// 测试用例2: [3,30,34,5,9]
		int[] nums2 = {3, 30, 34, 5, 9};
		String result2 = largestNumber(nums2);
		System.out.println("输入: [3,30,34,5,9]");
		System.out.println("输出: " + result2);
		System.out.println("预期: \"9534330\"");
		System.out.println();
		
		// 测试用例3: [0,0,0] - 全为0的特殊情况
		int[] nums3 = {0, 0, 0};
		String result3 = largestNumber(nums3);
		System.out.println("输入: [0,0,0]");
		System.out.println("输出: " + result3);
		System.out.println("预期: \"0\"");
		System.out.println();
		
		// 测试用例4: [1] - 单个元素
		int[] nums4 = {1};
		String result4 = largestNumber(nums4);
		System.out.println("输入: [1]");
		System.out.println("输出: " + result4);
		System.out.println("预期: \"1\"");
		System.out.println();
		
		// 测试用例5: [432,43243] - 复杂比较
		int[] nums5 = {432, 43243};
		String result5 = largestNumber(nums5);
		System.out.println("输入: [432,43243]");
		System.out.println("输出: " + result5);
		System.out.println("预期: \"43243432\"");
		System.out.println();
	}
}