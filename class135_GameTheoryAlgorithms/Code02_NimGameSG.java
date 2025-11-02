package class096;

import java.util.Arrays;

// 尼姆博弈(SG定理简单用法展示)
// 一共有 n 堆石头，两人轮流进行游戏
// 在每个玩家的回合中，玩家需要 选择任一 非空 石头堆，从中移除任意 非零 数量的石头
// 如果不能移除任意的石头，就输掉游戏
// 返回先手是否一定获胜
// 对数器验证
// 
// 题目来源：
// 1. 洛谷 P2197 【模板】Nim 游戏 - https://www.luogu.com.cn/problem/P2197
// 2. LeetCode 292. Nim Game - https://leetcode.com/problems/nim-game/
// 3. 牛客网 NC13685 取石子游戏 - https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
// 4. HDU 1850 Being a Good Boy in Spring Festival - http://acm.hdu.edu.cn/showproblem.php?pid=1850
// 5. POJ 2234. Matches Game - http://poj.org/problem?id=2234
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个状态的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. Nim游戏最优解：所有堆石子数异或和不为0表示先手必胜，否则后手必胜
// 
// 时间复杂度分析：
// 1. nim1方法：O(n) - 遍历所有堆计算异或和
// 2. nim2方法：O(max*n) - 需要计算每个石子数的SG值
// 
// 空间复杂度分析：
// 1. nim1方法：O(1) - 只需要常数空间
// 2. nim2方法：O(max) - 需要存储SG值数组
// 
// 工程化考量：
// 1. 异常处理：处理空数组和负数输入
// 2. 性能优化：nim1方法是Nim游戏的最优解
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：nim2方法展示了SG函数的通用求解过程
// 
// SG函数原理：
// SG函数是博弈论中用于解决公平组合游戏的重要工具，其定义为：
// SG(x) = mex{SG(y) | y是x的后继状态}
// 其中mex(S)表示不属于集合S的最小非负整数
// 
// Nim游戏可以看作是多个独立子游戏的组合，根据SG定理：
// 整个游戏的SG值等于各子游戏SG值的异或和
// 当SG值为0时，当前玩家必败；否则必胜
// 
// 对于Nim游戏，每个堆可以看作一个独立的子游戏
// 堆中有k个石子的状态SG值就是k
// 这是因为SG(k) = mex{SG(0), SG(1), ..., SG(k-1)} = mex{0, 1, ..., k-1} = k
public class Code02_NimGameSG {

	// 时间复杂度O(n)
	// 充分研究了性质
	// 
	// 算法原理：
	// Nim游戏的经典解法是计算所有堆石子数的异或和
	// 如果异或和为0，表示当前状态是必败态，否则是必胜态
	// 
	// 证明思路：
	// 1. 终止状态（所有堆都为0）的异或和为0，是必败态
	// 2. 对于异或和不为0的状态，总能通过一次操作使异或和变为0
	// 3. 对于异或和为0的状态，任何操作都会使异或和变为非0
	// 4. 因此，异或和为0的状态是必败态，非0状态是必胜态
	public static String nim1(int[] arr) {
		// 异常处理：处理空数组
		if (arr == null || arr.length == 0) {
			return "后手";
		}
		
		// 计算所有堆石子数的异或和
		int eor = 0;
		for (int num : arr) {
			// 异常处理：处理负数
			if (num < 0) {
				return "输入非法";
			}
			eor ^= num;
		}
		
		// 异或和不为0表示必胜态，为0表示必败态
		return eor != 0 ? "先手" : "后手";
	}

	// sg函数去求解
	// 过程时间复杂度高，但是可以轻易发现规律，进而优化成最优解
	// 证明不好想，但是从sg表出发，去观察最终的解，要好做很多
	// 
	// 算法原理：
	// 通过SG函数计算每个堆的SG值，然后根据SG定理计算整个游戏的SG值
	// 1. 对于每个堆，计算其SG值
	// 2. 根据SG定理，整个游戏的SG值等于各堆SG值的异或和
	// 3. SG值不为0表示必胜态，为0表示必败态
	public static String nim2(int[] arr) {
		// 异常处理：处理空数组
		if (arr == null || arr.length == 0) {
			return "后手";
		}
		
		// 找到最大的石子数，用于计算SG值
		int max = 0;
		for (int num : arr) {
			// 异常处理：处理负数
			if (num < 0) {
				return "输入非法";
			}
			max = Math.max(max, num);
		}
		
		// SG数组，sg[i]表示一堆有i个石子的SG值
		int[] sg = new int[max + 1];
		// appear数组用于计算mex值
		boolean[] appear = new boolean[max + 1];
		
		// 计算每个石子数对应的SG值
		for (int i = 1; i <= max; i++) {
			// 初始化appear数组
			Arrays.fill(appear, false);
			
			// 计算状态i的所有后继状态的SG值
			// 对于Nim游戏，一堆i个石子可以变成0到i-1个石子的任意状态
			for (int j = 0; j < i; j++) {
				appear[j] = true;
			}
			
			// 计算mex值
			for (int s = 0; s <= max; s++) {
				if (!appear[s]) {
					sg[i] = s;
					break;
				}
			}
		}
		
		// 打印sg表之后，可以发现，sg[x] = x
		// 那么eor ^= sg[num] 等同于 eor ^= num
		// 从sg定理发现了最优解
		
		// 根据SG定理，计算整个游戏的SG值
		int eor = 0;
		for (int num : arr) {
			eor ^= sg[num];
		}
		
		// SG值不为0表示必胜态，为0表示必败态
		return eor != 0 ? "先手" : "后手";
	}

	// 为了验证
	public static int[] randomArray(int n, int v) {
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = (int) (Math.random() * v);
		}
		return ans;
	}

	public static void main(String[] args) {
		int N = 200;
		int V = 1000;
		int testTimes = 10000;
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] arr = randomArray(n, V);
			String ans1 = nim1(arr);
			String ans2 = nim2(arr);
			if (!ans1.equals(ans2)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}