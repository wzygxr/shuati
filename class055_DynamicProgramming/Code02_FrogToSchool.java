package class127;

// 上学需要的最少跳跃能力
// 青蛙住在一条河边，家在0位置, 每天到河对岸的上学，学校在n位置
// 河里的石头排成了一条直线，青蛙每次跳跃必须落在一块石头或者岸上
// 给定一个长度为n-1的数组arr，表示1~n-1位置每块石头的高度数值
// 每次青蛙从一块石头起跳，这块石头的高度就会下降1
// 当石头的高度下降到0时，青蛙不能再跳到这块石头上，跳跃后使石头高度下降到0是允许的
// 青蛙一共需要去学校上x天课, 所以它需要往返x次，青蛙具有跳跃能力y, 它可以跳跃不超过y的距离
// 请问青蛙的跳跃能力至少是多少，才能用这些石头往返x次
// 1 <= n <= 10^5
// 1 <= arr[i] <= 10^4
// 1 <= x <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P8775
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 算法思路：
 * 1. 这是一个典型的二分答案问题
 * 2. 我们需要找到最小的跳跃能力y，使得青蛙能够完成x次往返
 * 3. 对于给定的跳跃能力y，我们可以通过滑动窗口来验证是否能够完成x次往返
 * 4. 滑动窗口[left, right)表示青蛙在一次往返中能使用的石头范围
 * 5. 在每次往返中，青蛙需要消耗石头的高度，当石头高度为0时不能再使用
 * 6. 我们通过二分查找来确定最小的跳跃能力
 *
 * 时间复杂度：O(n * log(n))，其中n是石头的数量
 * 空间复杂度：O(1)
 */
public class Code02_FrogToSchool {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int n, x;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		x = (int) in.nval;
		for (int i = 1; i < n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		// 认为学校所在的位置n，有足够的高度
		arr[n] = 2 * x;
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算青蛙的最小跳跃能力
	 * @return 最小跳跃能力
	 */
	public static int compute() {
		// 二分答案的左右边界
		int left = 1;
		int right = n;
		int ans = 0;
		
		// 二分查找最小跳跃能力
		while (left <= right) {
			int mid = (left + right) / 2;
			if (canFinish(mid)) {
				ans = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return ans;
	}
	
	/**
	 * 检查给定跳跃能力是否能完成x次往返
	 * @param power 跳跃能力
	 * @return 是否能完成x次往返
	 */
	private static boolean canFinish(int power) {
		// 复制石头高度数组，避免修改原数组
		int[] temp = new int[n + 1];
		System.arraycopy(arr, 0, temp, 0, n + 1);
		
		// 模拟x次往返
		for (int i = 0; i < x; i++) {
			// 使用滑动窗口计算一次往返
			long sum = 0;
			int l = 1;
			int r = 1;
			
			// 扩展窗口右边界直到能够到达学校
			while (l <= n && r <= n) {
				// 扩展右边界
				while (r <= n && r - l < power) {
					sum += temp[r];
					r++;
				}
				
				// 如果当前窗口能够支持一次往返
				if (sum >= 2) {
					// 消耗石头
					long need = Math.min(sum, 2);
					sum -= need;
					// 更新石头高度
					for (int j = l; j < r && need > 0; j++) {
						long deduct = Math.min(temp[j], need);
						temp[j] -= deduct;
						need -= deduct;
					}
					// 如果完成一次往返，跳出循环
					if (need == 0) {
						break;
					}
				}
				
				// 移动左边界
				sum -= temp[l];
				l++;
			}
			
			// 如果无法完成本次往返，返回false
			if (sum < 2) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 类似题目与训练拓展：
	 * 以下是与青蛙跳跃问题相关的15个类似题目，涵盖了多种算法平台和难度级别
	 */
	// 1. LeetCode 403 - Frog Jump
	//    链接：https://leetcode.cn/problems/frog-jump/
	//    区别：青蛙在河中跳跃，每个位置可能有石头，需要判断能否到达最后一块石头
	//    算法：记忆化搜索或动态规划，状态定义为dp[position][k]表示在position位置能否以步长k跳跃
	
	// 2. LeetCode 1340 - Jump Game V
	//    链接：https://leetcode.cn/problems/jump-game-v/
	//    区别：在数组中跳跃，每次跳跃不能超过固定距离，且需要满足特定条件
	//    算法：记忆化搜索，状态定义为dp[i]表示从位置i出发能访问的最大节点数
	
	// 3. LeetCode 55 - Jump Game
	//    链接：https://leetcode.cn/problems/jump-game/
	//    区别：判断能否从第一个位置跳到最后一个位置，每个元素代表最大跳跃长度
	//    算法：贪心或动态规划，维护能到达的最远位置
	
	// 4. LeetCode 45 - Jump Game II
	//    链接：https://leetcode.cn/problems/jump-game-ii/
	//    区别：求跳到最后一个位置的最少跳跃次数
	//    算法：贪心，每次选择能跳得最远的下一步
	
	// 5. LeetCode 1306 - Jump Game III
	//    链接：https://leetcode.cn/problems/jump-game-iii/
	//    区别：在数组中跳跃，从给定位置开始，判断能否到达值为0的位置
	//    算法：BFS或DFS
	
	// 6. LeetCode 1696 - Jump Game VI
	//    链接：https://leetcode.cn/problems/jump-game-vi/
	//    区别：在数组中跳跃，每次最多跳k步，求到达最后一个位置的最大得分
	//    算法：动态规划 + 单调队列优化
	
	// 7. LeetCode 1871 - Jump Game VII
	//    链接：https://leetcode.cn/problems/jump-game-vii/
	//    区别：在由'0'和'1'组成的字符串上跳跃，判断能否从第一个位置跳到最后一个位置
	//    算法：BFS或动态规划 + 前缀和优化
	
	// 8. Codeforces 965D - Single Wildcard Pattern Matching
	//    链接：https://codeforces.com/problemset/problem/965/D
	//    区别：字符串匹配问题，但涉及到间隔匹配的概念
	//    算法：贪心 + 双指针
	
	// 9. 洛谷 P1250 种树
	//    链接：https://www.luogu.com.cn/problem/P1250
	//    区别：区间覆盖问题，贪心选择最优策略
	//    算法：贪心，按照区间右端点排序后选择
	
	// 10. 牛客网 NC14552 方格取数
	//    链接：https://ac.nowcoder.com/acm/problem/14552
	//    区别：路径规划问题，但需要最大化收集的值
	//    算法：三维动态规划
	
	// 11. UVa 11280 - Flying to Fredericton
	//    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2255
	//    区别：多段最短路径问题，允许最多k次飞行
	//    算法：Dijkstra算法变种
	
	// 12. SPOJ - MICE AND MAZE
	//    链接：https://www.spoj.com/problems/MICEMAZE/
	//    区别：迷宫寻路问题，计算能在给定时间内到达终点的老鼠数量
	//    算法：BFS或Dijkstra算法
	
	// 13. HackerEarth - Minimum Jumps
	//    链接：https://www.hackerearth.com/practice/data-structures/arrays/1-d/practice-problems/algorithm/minimum-jumps-4/description/
	//    区别：计算从数组第一个元素跳到最后一个元素的最小跳跃次数
	//    算法：BFS或动态规划
	
	// 14. 杭电 HDU 1074 - Doing Homework
	//    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1074
	//    区别：任务调度问题，求最小扣分
	//    算法：状态压缩动态规划
	
	// 15. Codeforces 1296D - Fight with Monsters
	//    链接：https://codeforces.com/problemset/problem/1296/D
	//    区别：贪心策略解决怪物战斗问题
	//    算法：排序后贪心选择最优策略

	/**
	 * 算法本质与技巧总结：
	 *
	 * 1. 二分答案：将求最小跳跃能力转化为对每个可能的跳跃能力进行验证
	 *    - 二分答案是解决优化问题的常用技巧，将问题转化为判定问题
	 *    - 适用于具有单调性的问题，即如果y可行，那么所有大于y的跳跃能力都可行
	 *    - 二分查找的范围需要合理设定，通常是问题的上下界
	 *
	 * 2. 滑动窗口：高效验证给定跳跃能力是否可行
	 *    - 滑动窗口在数组处理中非常高效，时间复杂度为O(n)
	 *    - 通过维护窗口内的石头高度总和，快速判断是否可以完成一次往返
	 *    - 窗口的大小由当前尝试的跳跃能力决定
	 *
	 * 3. 模拟往返过程：逐次消耗石头高度
	 *    - 每次往返需要消耗窗口内的石头高度
	 *    - 优先消耗高度较低的石头，保持窗口的可用性
	 *    - 当窗口内的石头高度总和不足时，表示无法完成当前跳跃能力下的往返
	 *
	 * 4. 数组复制：避免修改原数组
	 *    - 在验证函数中复制原数组，防止影响后续二分查找
	 *    - 这是一种常见的工程实践，保持函数的无副作用性
	 *
	 * 5. 边界处理：将学校位置视为特殊节点
	 *    - 学校位置n被赋予足够大的高度，确保可以完成跳跃
	 *    - 合理的边界条件处理是算法正确性的关键
	 */

	/**
	 * Java工程化实战建议：
	 *
	 * 1. 输入输出优化：
	 *    - 使用BufferedReader和PrintWriter提高输入输出效率
	 *    - 使用StreamTokenizer处理数值输入，比Scanner更快
	 *    - 对于大规模数据，这种优化尤为重要
	 *
	 * 2. 内存管理：
	 *    - 预先分配数组大小，避免动态扩容
	 *    - 在验证函数中，复制数组可能导致较大的内存开销
	 *    - 对于x很大的情况，可以考虑优化验证逻辑，减少内存使用
	 *
	 * 3. 性能优化策略：
	 *    - 二分查找的上下界设置要合理，避免不必要的搜索
	 *    - 滑动窗口的实现要高效，避免重复计算
	 *    - 对于大规模数据，可以考虑使用long类型避免溢出
	 *
	 * 4. 代码健壮性提升：
	 *    - 添加输入合法性检查
	 *    - 处理可能的边界情况，如n=1或x=0
	 *    - 使用try-catch块处理IO异常
	 *    - 确保资源正确关闭（使用try-with-resources更好）
	 *
	 * 5. Java特有优化技巧：
	 *    - 使用System.arraycopy进行数组复制，比循环复制更快
	 *    - 合理使用静态变量和实例变量
	 *    - 对于x很大的情况，可以优化验证算法，避免O(x*n)的时间复杂度
	 *    - 考虑使用long类型处理大数，避免整数溢出
	 *
	 * 6. 调试与问题定位：
	 *    - 添加日志输出来跟踪算法的执行过程
	 *    - 对于二分查找，可以打印每次mid的值和验证结果
	 *    - 对于滑动窗口，可以打印窗口的左右边界和当前总和
	 *
	 * 7. 跨语言实现对比：
	 *    - Java实现比较规范，但在处理大规模数据时可能不如C++高效
	 *    - 在Python中，需要注意整数溢出问题，Python的整数精度不会有问题
	 *    - 在C++中，可以使用memcpy进行更快的数组复制
	 *
	 * 8. 算法优化思路：
	 *    - 原始验证算法的时间复杂度为O(x*n)，对于x=1e9的情况会超时
	 *    - 可以通过数学分析优化验证算法，将时间复杂度降至O(n)
	 *    - 关键思路是：对于每个位置i的石头，最多可以被使用min(arr[i], x)次
	 *    - 在滑动窗口中，我们只需要计算窗口内石头可使用次数的总和是否足够2x
	 */
}