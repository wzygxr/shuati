package class095;

// 反尼姆博弈(反常游戏)
// 一共有n堆石头，两人轮流进行游戏
// 在每个玩家的回合中，玩家需要选择任何一个非空的石头堆，并从这堆石头中移除任意正数的石头数量
// 谁先拿走最后的石头就失败，返回最终谁会获胜
// 先手获胜，打印John
// 后手获胜，打印Brother
// 测试链接 : https://www.luogu.com.cn/problem/P4279
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 算法思路：
// 1. 反尼姆博弈是尼姆博弈的变种，胜利条件相反
// 2. 解题需要分两种情况讨论：
//    a) 所有堆的石子数都是1：此时判断堆数的奇偶性，奇数后手胜，偶数先手胜
//    b) 存在石子数大于1的堆：此时判断所有堆石子数的异或和，异或和为0后手胜，否则先手胜
// 3. 这是因为在反尼姆博弈中，玩家需要避免拿到最后一个石子
//
// 时间复杂度：O(n) - 需要遍历所有堆计算异或和和统计石子数为1的堆数
// 空间复杂度：O(1) - 只使用了常数级别的额外空间
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 多堆石子
//    - 两人轮流从任意一堆取任意数量石子
//    - 最后取石子者失败
// 2. 解题技巧：
//    - 分情况讨论：所有堆都只有1个石子 vs 存在石子数大于1的堆
//    - 所有堆都只有1个石子时，根据堆数奇偶性判断胜负
//    - 存在石子数大于1的堆时，根据异或和判断胜负
// 3. 变种问题：
//    - 每堆可取石子数量受限
//    - 石子价值不同
//
// 相关题目链接：
// 1. 洛谷 P4279: https://www.luogu.com.cn/problem/P4279
// 2. HDU 2509: http://acm.hdu.edu.cn/showproblem.php?pid=2509
// 3. POJ 2975: http://poj.org/problem?id=2975

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_AntiNimGame {

	public static int MAXN = 51;

	public static int[] stones = new int[MAXN];

	public static int t, n;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		t = (int) in.nval;
		for (int i = 0; i < t; i++) {
			in.nextToken();
			n = (int) in.nval;
			for (int j = 0; j < n; j++) {
				in.nextToken();
				stones[j] = (int) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算反尼姆博弈结果
	 * @return 获胜者
	 * 
	 * 算法思路：
	 * 1. 如果所有堆都只有1个石子，判断堆数奇偶性
	 * 2. 如果存在石子数大于1的堆，判断异或和是否为0
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static String compute() {
		int eor = 0, sum = 0;
		for (int i = 0; i < n; i++) {
			eor ^= stones[i];
			sum += stones[i] == 1 ? 1 : 0;
		}
		// 所有堆都只有1个石子
		if (sum == n) {
			// 奇数堆后手胜，偶数堆先手胜
			return (n & 1) == 1 ? "Brother" : "John";
		} else {
			// 存在石子数大于1的堆，异或和为0后手胜，否则先手胜
			return eor != 0 ? "John" : "Brother";
		}
	}

}