package class107;

import java.util.*;

// 囚徒生存问题
// 有100个犯人被关在监狱，犯人编号0~99，监狱长准备了100个盒子，盒子编号0~99
// 这100个盒子排成一排，放在一个房间里面，盒子编号从左往右有序排列
// 最开始时，每个犯人的编号放在每个盒子里，两种编号一一对应，监狱长构思了一个处决犯人的计划
// 监狱长打开了很多盒子，并交换了盒子里犯人的编号
// 交换行为完全随机，但依然保持每个盒子都有一个犯人编号
// 监狱长规定，每个犯人单独进入房间，可以打开50个盒子，寻找自己的编号
// 该犯人全程无法和其他犯人进行任何交流，并且不能交换盒子中的编号，只能打开查看
// 寻找过程结束后把所有盒子关上，走出房间，然后下一个犯人再进入房间，重复上述过程
// 监狱长规定，每个犯人在尝试50次的过程中，都需要找到自己的编号
// 如果有任何一个犯人没有做到这一点，100个犯人全部处决
// 所有犯人在一起交谈的时机只能发生在游戏开始之前，游戏一旦开始直到最后一个人结束都无法交流
// 请尽量制定一个让所有犯人存活概率最大的策略
// 来自论文<The Cell Probe Complexity of Succinct Data Structures>
// 作者Anna Gal和Peter Bro Miltersen写于2007年
// 如今该题变成了流行题，还有大量科普视频

/**
 * 囚徒逃脱游戏 - 循环排列算法详解
 * 
 * 算法原理：
 * 1. 每个囚犯从自己编号对应的盒子开始
 * 2. 打开盒子，查看里面的编号
 * 3. 如果是自己的编号，则成功；否则，前往该编号对应的盒子
 * 4. 重复步骤2-3，直到找到自己的编号或尝试次数用完
 * 
 * 算法核心思想：
 * 盒子中的编号排列实际上是一个置换（Permutation），可以分解为若干个不相交的循环
 * 每个囚犯都在自己所属的循环中寻找自己的编号
 * 当且仅当所有循环的长度都不超过尝试次数时，所有囚犯都能成功
 * 
 * 数学分析：
 * 对于n个囚犯，每人最多尝试n/2次
 * 成功概率 = 1 - Σ(1/i) (i从n/2+1到n)
 * 当n=100时，成功概率约为31.18%
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * 应用场景：
 * 1. 置换群理论
 * 2. 循环检测
 * 3. 排列分析
 * 4. 概率计算
 */

public class Code07_PrisonersEscapeGame {

	// 通过多次模拟实验得到的概率
	public static double escape1(int people, int tryTimes, int testTimes) {
		int escape = 0;
		for (int i = 0; i < testTimes; i++) {
			int[] arr = generateRandomArray(people);
			if (maxCircle(arr) <= tryTimes) {
				escape++;
			}
		}
		return (double) escape / (double) testTimes;
	}

	// 求arr中最大环的长度
	public static int maxCircle(int[] arr) {
		int maxCircle = 1;
		// 创建副本避免修改原数组
		int[] copy = arr.clone();
		for (int i = 0; i < copy.length; i++) {
			int curCircle = 1;
			// 当前元素不在正确位置时继续循环
			while (i != copy[i]) {
				swap(copy, i, copy[i]);
				curCircle++;
			}
			maxCircle = Math.max(maxCircle, curCircle);
		}
		return maxCircle;
	}

	// 生成随机arr
	// 原本每个位置的数都等概率出现在自己或者其他位置
	public static int[] generateRandomArray(int len) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = i;
		}
		for (int i = len - 1; i > 0; i--) {
			swap(arr, i, (int) (Math.random() * (i + 1)));
		}
		return arr;
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 公式版
	// 一定要保证tryTimes大于等于people的一半，否则该函数失效
	// 导致死亡的情况数 : C(r,100) * (r-1)! * (100-r)!，r从51~100，累加起来
	// 死亡概率 : C(r,100) * (r-1)! * (100-r)! / 100!，r从51~100，累加起来
	// 化简后的死亡概率 : 1/r，r从51~100，累加起来
	public static double escape2(int people, int tryTimes) {
		double a = 0;
		for (int r = tryTimes + 1; r <= people; r++) {
			a += (double) 1 / (double) r;
		}
		return (double) 1 - a;
	}
	
	/**
	 * 模拟囚徒逃脱过程
	 * @param permutation 盒子中囚犯编号的排列
	 * @param prisonerId 囚犯编号
	 * @param maxAttempts 最大尝试次数
	 * @return 是否成功找到自己的编号
	 */
	public static boolean prisonerAttempt(int[] permutation, int prisonerId, int maxAttempts) {
		int currentBox = prisonerId;
		int attempts = 0;
		
		while (attempts < maxAttempts) {
			int numberInBox = permutation[currentBox];
			if (numberInBox == prisonerId) {
				return true; // 成功找到自己的编号
			}
			currentBox = numberInBox; // 前往下一个盒子
			attempts++;
		}
		
		return false; // 尝试次数用完仍未找到
	}
	
	/**
	 * 模拟所有囚徒的逃脱尝试
	 * @param people 囚徒数量
	 * @param tryTimes 每人最大尝试次数
	 * @return 所有囚徒是否都成功
	 */
	public static boolean allPrisonersEscape(int people, int tryTimes) {
		int[] permutation = generateRandomArray(people);
		
		// 每个囚徒都尝试找到自己的编号
		for (int prisonerId = 0; prisonerId < people; prisonerId++) {
			if (!prisonerAttempt(permutation, prisonerId, tryTimes)) {
				return false; // 任何一个囚徒失败，所有人都会被处决
			}
		}
		
		return true; // 所有囚徒都成功
	}
	
	/**
	 * 分析排列的循环结构
	 * @param permutation 排列数组
	 * @return 循环长度列表
	 */
	public static List<Integer> analyzeCycles(int[] permutation) {
		List<Integer> cycles = new ArrayList<>();
		boolean[] visited = new boolean[permutation.length];
		int[] copy = permutation.clone();
		
		for (int i = 0; i < copy.length; i++) {
			if (!visited[i]) {
				int cycleLength = 0;
				int current = i;
				
				// 遍历当前循环
				while (!visited[current]) {
					visited[current] = true;
					current = copy[current];
					cycleLength++;
				}
				
				cycles.add(cycleLength);
			}
		}
		
		return cycles;
	}

	public static void main(String[] args) {
		int people = 100;
		// 一定要保证tryTimes大于等于people的一半
		int tryTimes = 50;
		int testTimes = 100000;
		System.out.println("参与游戏的人数 : " + people);
		System.out.println("每人的尝试次数 : " + tryTimes);
		System.out.println("模拟实验的次数 : " + testTimes);
		System.out.println("通过模拟实验得到的概率为 : " + escape1(people, tryTimes, testTimes));
		System.out.println("通过公式计算得到的概率为 : " + escape2(people, tryTimes));
		
		System.out.println("\n=== 循环结构分析示例 ===");
		// 创建一个示例排列 [1, 2, 0, 4, 3] 表示:
		// 盒子0中有编号1，盒子1中有编号2，盒子2中有编号0（循环0->1->2->0）
		// 盒子3中有编号4，盒子4中有编号3（循环3->4->3）
		int[] example = {1, 2, 0, 4, 3};
		List<Integer> cycles = analyzeCycles(example);
		System.out.println("示例排列: " + Arrays.toString(example));
		System.out.println("循环结构: " + cycles);
		System.out.println("最大循环长度: " + Collections.max(cycles));
		
		System.out.println("\n=== 单次逃脱模拟 ===");
		int[] testPermutation = generateRandomArray(10);
		System.out.println("测试排列: " + Arrays.toString(testPermutation));
		List<Integer> testCycles = analyzeCycles(testPermutation);
		System.out.println("循环结构: " + testCycles);
		System.out.println("最大循环长度: " + Collections.max(testCycles));
		
		boolean success = allPrisonersEscape(10, 5);
		System.out.println("10个囚徒是否全部逃脱: " + success);
	}

}