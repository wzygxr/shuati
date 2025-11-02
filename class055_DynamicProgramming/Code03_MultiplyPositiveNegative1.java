package class127;

// 相乘为正或负的子数组数量
// 给定一个长度为n的数组arr，其中所有值都不是0
// 返回有多少个子数组相乘的结果是正
// 返回有多少个子数组相乘的结果是负
// 1 <= n <= 10^6
// -10^9 <= arr[i] <= +10^9，arr[i]一定不是0
// 来自真实大厂笔试，对数器验证

/**
 * 算法思路：
 * 1. 使用前缀和思想，维护当前位置之前正数和负数的子数组数量
 * 2. 遍历数组，维护一个变量cur表示到当前位置的累积符号（0表示正，1表示负）
 * 3. cnt[0]表示累积符号为正的前缀数量，cnt[1]表示累积符号为负的前缀数量
 * 4. 对于当前位置i：
 *    - 如果arr[i]为正数，符号不变，cur保持不变
 *    - 如果arr[i]为负数，符号改变，cur ^= 1
 * 5. 如果当前累积符号为cur，那么：
 *    - 与之前累积符号为cur的前缀组合，乘积为正数
 *    - 与之前累积符号为cur^1的前缀组合，乘积为负数
 * 6. 更新cnt数组
 *
 * 时间复杂度：O(n)，只需要遍历一次数组
 * 空间复杂度：O(1)，只使用了常数额外空间
 */
public class Code03_MultiplyPositiveNegative1 {

	// 正式方法
	public static int[] num(int[] arr) {
		// 0 : 正状态
		// 1 : 负状态
		// ^ : 乘法运算
		int[] cnt = new int[2];
		// 初始化，空数组乘积为正数
		cnt[0] = 1;
		cnt[1] = 0;
		int ans1 = 0; // 正数子数组数量
		int ans2 = 0; // 负数子数组数量
		// cur表示当前累积符号，0表示正，1表示负
		for (int i = 0, cur = 0; i < arr.length; i++) {
			// 如果当前元素为负数，改变符号
			cur ^= arr[i] > 0 ? 0 : 1;
			// 与之前相同符号的前缀组合，乘积为正数
			ans1 += cnt[cur];
			// 与之前不同符号的前缀组合，乘积为负数
			ans2 += cnt[cur ^ 1];
			// 更新cnt数组
			cnt[cur]++;
		}
		return new int[] { ans1, ans2 };
	}

	// 暴力方法
	// 为了验证
	public static int[] right(int[] arr) {
		int n = arr.length;
		int ans1 = 0;
		int ans2 = 0;
		for (int i = 0; i < n; i++) {
			long cur = 1;
			for (int j = i; j < n; j++) {
				cur = cur * arr[j];
				if (cur > 0) {
					ans1++;
				} else {
					ans2++;
				}
			}
		}
		return new int[] { ans1, ans2 };
	}

	// 为了验证
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			do {
				ans[i] = (int) (Math.random() * v * 2) - v;
			} while (ans[i] == 0);
		}
		return ans;
	}

	public static void main(String[] args) {
		// 正式方法无所谓，怎么都正确
		// 但是对数器方法是暴力乘起来，所以n和v不要太大，防止溢出
		int n = 20;
		int v = 10;
		int testTime = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int size = (int) (Math.random() * n);
			int[] arr = randomArray(size, v);
			int[] ans1 = num(arr);
			int[] ans2 = right(arr);
			if (ans1[0] != ans2[0] || ans1[1] != ans2[1]) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

	// 相关题目：
	// 1. LeetCode 152 - Maximum Product Subarray (乘积最大子数组)
	//    链接：https://leetcode.cn/problems/maximum-product-subarray/
	//    区别：求乘积最大的连续子数组
	// 2. LeetCode 53 - Maximum Subarray (最大子数组和)
	//    链接：https://leetcode.cn/problems/maximum-subarray/
	//    区别：求和最大的连续子数组
	// 3. LeetCode 918 - Maximum Sum Circular Subarray (环形子数组的最大和)
	//    链接：https://leetcode.cn/problems/maximum-sum-circular-subarray/
	//    区别：数组是环形的，求和最大的连续子数组
	// 4. Codeforces 1215B - The Number of Products
	//    链接：https://codeforces.com/problemset/problem/1215/B
	//    区别：与本题相同，统计乘积为正和负的子数组数量
}