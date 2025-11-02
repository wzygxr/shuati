package class095;

// 尼姆博弈(Nim Game)
// 一共有n堆石头，两人轮流进行游戏
// 在每个玩家的回合中，玩家需要选择任何一个非空的石头堆，并从这堆石头中移除任意正数的石头数量
// 谁先拿走最后的石头就获胜，返回最终谁会获胜
// 测试链接 : https://www.luogu.com.cn/problem/P2197
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
//
// 算法思路：
// 1. 尼姆博弈是经典的博弈论问题
// 2. 核心思想是计算所有堆石子数的异或和(Nim-sum)
// 3. 当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
// 4. 这是因为处于必胜态的玩家总能通过一步操作使Nim-sum变为0
// 5. 而处于必败态的玩家无论如何操作都会使Nim-sum变为非0
//
// 时间复杂度：O(n) - 需要遍历所有堆计算异或和
// 空间复杂度：O(1) - 只使用了常数级别的额外空间
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 多堆石子
//    - 两人轮流从任意一堆取任意数量石子
//    - 取走最后一颗石子者获胜
// 2. 解题技巧：
//    - 计算所有堆石子数的异或和
//    - 异或和为0表示当前玩家必败，否则必胜
// 3. 变种问题：
//    - 每堆可取石子数量受限
//    - 最后取石子者失败（反尼姆博弈）
//    - 取石子规则变化（如只能取斐波那契数个石子）
//
// 相关题目链接：
// 1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197
// 2. LeetCode 292: https://leetcode.com/problems/nim-game/
// 3. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850
// 4. POJ 2234: http://poj.org/problem?id=2234
// 5. AtCoder DP Contest L - Deque: https://atcoder.jp/contests/dp/tasks/dp_l

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_NimGame {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int t = (int) in.nval;
		for (int i = 0; i < t; i++) {
			in.nextToken();
			int n = (int) in.nval;
			int eor = 0;
			for (int j = 0; j < n; j++) {
				in.nextToken();
				eor ^= (int) in.nval;
			}
			if (eor != 0) {
				out.println("Yes");
			} else {
				out.println("No");
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}