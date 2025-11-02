package class091;

import java.util.HashMap;

// 两个0和1数量相等区间的最大长度
// 给出一个长度为n的01串，现在请你找到两个区间
// 使得这两个区间中，1的个数相等，0的个数也相等
// 这两个区间可以相交，但是不可以完全重叠，即两个区间的左右端点不可以完全一样
// 现在请你找到两个最长的区间，满足以上要求
// 返回区间最大长度
// 来自真实大厂笔试，没有在线测试，对数器验证
public class Code06_LongestSameZerosOnes {

	// 暴力方法
	// 为了验证
	public static int len1(int[] arr) {
		HashMap<Integer, HashMap<Integer, Integer>> map = new HashMap<>();
		for (int i = 0; i < arr.length; i++) {
			int zero = 0;
			int one = 0;
			for (int j = i; j < arr.length; j++) {
				zero += arr[j] == 0 ? 1 : 0;
				one += arr[j] == 1 ? 1 : 0;
				map.putIfAbsent(zero, new HashMap<>());
				map.get(zero).put(one, map.get(zero).getOrDefault(one, 0) + 1);
			}
		}
		int ans = 0;
		for (int zeros : map.keySet()) {
			for (int ones : map.get(zeros).keySet()) {
				int num = map.get(zeros).get(ones);
				if (num > 1) {
					ans = Math.max(ans, zeros + ones);
				}
			}
		}
		return ans;
	}

	/**
	 * 计算两个0和1数量相等区间的最大长度
	 * 
	 * 算法思路：
	 * 贪心策略：
	 * 1. 找到最左边和最右边的0，计算它们之间的距离
	 * 2. 找到最左边和最右边的1，计算它们之间的距离
	 * 3. 返回两个距离中的最大值
	 * 
	 * 正确性分析：
	 * 1. 如果要找两个区间，使得它们的0和1数量分别相等
	 * 2. 那么这两个区间可以是任意两个包含相同数量0和1的区间
	 * 3. 为了使长度最大，我们可以选择包含所有0或所有1的区间
	 * 4. 包含所有0的区间就是从最左边的0到最右边的0
	 * 5. 包含所有1的区间就是从最左边的1到最右边的1
	 * 6. 比较这两个区间的长度，返回较大者
	 * 
	 * 时间复杂度：O(n) - 只需要遍历数组常数次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param arr 输入的01数组
	 * @return 两个区间中最大的长度
	 */
	public static int len2(int[] arr) {
		int leftZero = -1;
		int rightZero = -1;
		int leftOne = -1;
		int rightOne = -1;
		
		// 找到最左边的0
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 0) {
				leftZero = i;
				break;
			}
		}
		
		// 找到最左边的1
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 1) {
				leftOne = i;
				break;
			}
		}
		
		// 找到最右边的0
		for (int i = arr.length - 1; i >= 0; i--) {
			if (arr[i] == 0) {
				rightZero = i;
				break;
			}
		}
		
		// 找到最右边的1
		for (int i = arr.length - 1; i >= 0; i--) {
			if (arr[i] == 1) {
				rightOne = i;
				break;
			}
		}
		
		// 计算包含所有0的区间长度和包含所有1的区间长度
		int p1 = rightZero - leftZero;
		int p2 = rightOne - leftOne;
		return Math.max(p1, p2);
	}

	// 为了验证
	public static int[] randomArray(int len) {
		int[] ans = new int[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (int) (Math.random() * 2);
		}
		return ans;
	}

	// 为了验证
	public static void main(String[] args) {
		int N = 500;
		int testTimes = 2000;
		System.out.println("测试开始");
		for (int i = 1; i <= testTimes; i++) {
			int n = (int) (Math.random() * N) + 2;
			int[] arr = randomArray(n);
			int ans1 = len1(arr);
			int ans2 = len2(arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
			if (i % 100 == 0) {
				System.out.println("测试到第" + i + "组");
			}
		}
		System.out.println("测试结束");
		
		// 额外测试用例
		int[] testArr = {0, 1, 0, 1, 1, 0, 0, 1};
		System.out.println("\n额外测试用例:");
		System.out.println("数组: " + java.util.Arrays.toString(testArr));
		System.out.println("最大长度: " + len2(testArr));
	}
}