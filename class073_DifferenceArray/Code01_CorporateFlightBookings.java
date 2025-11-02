package class047;

// 航班预订统计
// 这里有 n 个航班，它们分别从 1 到 n 进行编号。
// 有一份航班预订表 bookings ，
// 表中第 i 条预订记录 bookings[i] = [firsti, lasti, seatsi]
// 意味着在从 firsti 到 lasti 
//（包含 firsti 和 lasti ）的 每个航班 上预订了 seatsi 个座位。
// 请你返回一个长度为 n 的数组 answer，里面的元素是每个航班预定的座位总数。
// 测试链接 : https://leetcode.cn/problems/corporate-flight-bookings/
//
// 相关题目:
// 1. LeetCode 370. 区间加法 (Range Addition)
//    链接: https://leetcode.com/problems/range-addition/
//    题目描述: 假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
//             你将会被给出 k 个更新的操作。其中，每个操作会被表示为一个三元组：
//             [startIndex, endIndex, inc]，你需要将子数组 A[startIndex ... endIndex]
//             （包括 startIndex 和 endIndex）增加 inc。
//
// 2. LeetCode 1094. 拼车 (Car Pooling)
//    链接: https://leetcode.com/problems/car-pooling/
//    题目描述: 假设你是一位顺风车司机，车上最初有 capacity 个空座位可以用来
//             载客。由于道路拥堵，你只能向一个方向行驶。
//             给定一个数组 trips，其中 trips[i] = [num_passengers, start, end]
//             表示第 i 次旅行有 num_passengers 位乘客，接他们和放他们的位置分别是 start 和 end。
//             这些位置是从你的初始位置向东的公里数。
//             当且仅当你可以在所有给定的行程中接送所有乘客时，返回 true，否则返回 false。
//
// 3. LeetCode 1109. 航班预订统计 (Corporate Flight Bookings) - 当前题目
//    链接: https://leetcode.com/problems/corporate-flight-bookings/
//    题目描述: 这里有 n 个航班，它们分别从 1 到 n 进行编号。
//             有一份航班预订表 bookings，表中第 i 条预订记录 bookings[i] = [first, last, seats]
//             意味着在从 first 到 last（包含 first 和 last）的每个航班上预订了 seats 个座位。
//             请你返回一个长度为 n 的数组 answer，里面的元素是每个航班预定的座位总数。
//
// 4. LeetCode 1854. 人口最多的年份 (Maximum Population Year)
//    链接: https://leetcode.com/problems/maximum-population-year/
//    题目描述: 给你一个二维整数数组 logs，其中每个 logs[i] = [birth, death] 表示第 i 个人的出生和死亡年份。
//             年份 x 的人口定义为这一年期间活着的人的数目。第 i 个人被计入年份 x 的人口需要满足：
//             x 在闭区间 [birth, death - 1] 内。注意，人不应当计入他们死亡当年的人口中。
//             返回人口最多且最早的年份。
//
// 5. HackerRank Array Manipulation
//    链接: https://www.hackerrank.com/challenges/crush/problem
//    题目描述: 给定一个大小为 n 的数组，初始值都为 0。有 m 次操作，
//             每次操作给出三个数 a, b, k，表示将数组下标从 a 到 b 的所有元素都加上 k。
//             求执行完所有操作后数组中的最大值。
//
// 差分数组核心思想:
// 差分数组是前缀和的逆运算。对于数组 a，其差分数组 b 定义为:
// b[0] = a[0]
// b[i] = a[i] - a[i-1] (i > 0)
//
// 差分数组的主要用途:
// 1. 快速处理区间加减操作:
//    对数组区间 [l, r] 中的每个数加上 x，可以通过以下操作实现:
//    b[l] += x
//    b[r+1] -= x (如果 r+1 在数组范围内)
//    然后通过计算差分数组的前缀和得到更新后的原数组
//
// 时间复杂度分析:
// 构造差分数组: O(n)
// 每次区间更新操作: O(1)
// 还原原数组(通过前缀和): O(n)
// 总时间复杂度: O(n + m) 其中 m 是操作次数
//
// 空间复杂度分析:
// 需要额外的差分数组空间: O(n)
public class Code01_CorporateFlightBookings {

	// bookings
	// [1,5,6]
	// [2,9,3]
	// ...
	public static int[] corpFlightBookings(int[][] bookings, int n) {
		// 创建差分数组，大小为 n+2 是为了处理边界情况
		// 索引 0 不使用，索引 1 到 n 对应航班 1 到 n
		// 索引 n+1 用于处理边界情况，避免数组越界
		int[] cnt = new int[n + 2];
		
		// 设置差分数组，每一个操作对应两个设置
		// 对于预订记录 [first, last, seats]，在差分数组中:
		// 1. 在位置 first 增加 seats (表示从 first 开始每个航班增加 seats 个座位)
		// 2. 在位置 last+1 减少 seats (表示从 last+1 开始每个航班减少 seats 个座位)
		for (int[] book : bookings) {
			cnt[book[0]] += book[2];
			cnt[book[1] + 1] -= book[2];
		}
		
		// 加工前缀和，将差分数组还原为结果数组
		// 通过前缀和操作，将差分数组转换为实际的座位数数组
		for (int i = 1; i < cnt.length; i++) {
			cnt[i] += cnt[i - 1];
		}
		
		// 构造结果数组
		// 由于差分数组是从索引 1 开始使用的，所以结果数组从 cnt[1] 开始
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = cnt[i + 1];
		}
		return ans;
	}

}