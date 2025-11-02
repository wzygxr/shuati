package class051;

// 爱吃香蕉的珂珂
// 珂珂喜欢吃香蕉。这里有 n 堆香蕉，第 i 堆中有 piles[i] 根香蕉
// 警卫已经离开了，将在 h 小时后回来。
// 珂珂可以决定她吃香蕉的速度 k （单位：根/小时)
// 每个小时，她将会选择一堆香蕉，从中吃掉 k 根
// 如果这堆香蕉少于 k 根，她将吃掉这堆的所有香蕉，然后这一小时内不会再吃更多的香蕉
// 珂珂喜欢慢慢吃，但仍然想在警卫回来前吃掉所有的香蕉。
// 返回她可以在 h 小时内吃掉所有香蕉的最小速度 k（k 为整数）
// 测试链接 : https://leetcode.cn/problems/koko-eating-bananas/
public class Code01_KokoEatingBananas {

	// 时间复杂度O(n * log(max))，额外空间复杂度O(1)
	public static int minEatingSpeed(int[] piles, int h) {
		// 最小且达标的速度，范围[l,r]
		int l = 1;
		int r = 0;
		for (int pile : piles) {
			r = Math.max(r, pile);
		}
		// [l,r]不停二分
		int ans = 0;
		int m = 0;
		while (l <= r) {
			// m = (l + r) / 2
			m = l + ((r - l) >> 1);
			if (f(piles, m) <= h) {
				// 达标！
				// 记录答案，去左侧二分
				ans = m;
				// l....m....r
				// l..m-1
				r = m - 1;
			} else {
				// 不达标
				l = m + 1;
			}
		}
		return ans;
	}

	// 香蕉重量都在piles
	// 速度就定成speed
	// 返回吃完所有的香蕉，耗费的小时数量
	public static long f(int[] piles, int speed) {
		long ans = 0;
		for (int pile : piles) {
			// (a/b)结果向上取整，如果a和b都是非负数，可以写成(a+b-1)/b
			// "讲解032-位图"讲了这种写法，不会的同学可以去看看
			// 这里不再赘述
			ans += (pile + speed - 1) / speed;
		}
		return ans;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个典型的二分答案问题。我们需要找到最小的吃香蕉速度，使得能在h小时内吃完所有香蕉。
	 * 
	 * 解题思路：
	 * 1. 确定答案范围：最小速度是1（每小时吃1根），最大速度是max(piles)（一小时吃完最多的那堆）
	 * 2. 二分搜索：在[l,r]范围内二分搜索，对于每个中间值m，计算以速度m吃香蕉需要的时间
	 * 3. 判断函数：f(piles, speed)计算以speed速度吃完所有香蕉需要的时间
	 * 4. 根据f的结果调整搜索范围，最终找到最小的满足条件的速度
	 * 
	 * 时间复杂度分析：
	 * 1. 二分搜索范围是[1, max]，其中max是最大堆的香蕉数，二分次数是O(log(max))
	 * 2. 每次二分需要调用f函数，f函数遍历所有堆，时间复杂度是O(n)
	 * 3. 总时间复杂度：O(n * log(max))
	 * 
	 * 空间复杂度分析：
	 * 只使用了常数个额外变量，空间复杂度是O(1)
	 * 
	 * 工程化考虑：
	 * 1. 注意整数溢出：f函数返回long类型，避免计算过程中溢出
	 * 2. 向上取整技巧：(a + b - 1) / b 是对 a/b 向上取整的经典写法
	 * 3. 位运算优化：(r - l) >> 1 等价于 (r - l) / 2，但效率略高
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 1011. 在D天内送达包裹的能力 - https://leetcode.cn/problems/capacity-to-ship-packages-within-d-days/
	 * 2. LeetCode 410. 分割数组的最大值 - https://leetcode.cn/problems/split-array-largest-sum/
	 * 3. LeetCode 1231. 分享巧克力 - https://leetcode.cn/problems/divide-chocolate/
	 * 4. LeetCode 1552. 两球之间的磁力 - https://leetcode.cn/problems/magnetic-force-between-two-balls/
	 * 5. 牛客网 NC163 机器人跳跃问题 - https://www.nowcoder.com/practice/7037a3d57bbd4336856b8e16a9cafd71
	 */

}