package class096;

// 分裂游戏
// 一共有n个瓶子，编号为0 ~ n-1，第i瓶里装有nums[i]个糖豆，每个糖豆认为无差别
// 有两个玩家轮流取糖豆，每一轮的玩家必须选i、j、k三个编号，并且满足i < j <= k
// 当前玩家从i号瓶中拿出一颗糖豆，分裂成两颗糖豆，并且往j、k瓶子中各放入一颗，分裂的糖豆继续无差别
// 要求i号瓶一定要有糖豆，如果j == k，那么相当于从i号瓶中拿出一颗，向另一个瓶子放入了两颗
// 如果轮到某个玩家发现所有糖豆都在n-1号瓶里，导致无法行动，那么该玩家输掉比赛
// 先手希望知道，第一步如何行动可以保证自己获胜，要求返回字典序最小的行动
// 第一步从0号瓶拿出一颗糖豆，并且往2、3号瓶中各放入一颗，可以确保最终自己获胜
// 第一步从0号瓶拿出一颗糖豆，并且往11、13号瓶中各放入一颗，也可以确保自己获胜
// 本题要求每个瓶子的编号看做是一个字符的情况下，最小的字典序，所以返回"0 2 3"
// 如果先手怎么行动都无法获胜，返回"-1 -1 -1"
// 先手还希望知道自己有多少种第一步取糖的行动，可以确保自己获胜，返回方法数
// 测试链接 : https://www.luogu.com.cn/problem/P3185
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过
// 
// 题目来源：
// 1. 洛谷 P3185 [HNOI2007]分裂游戏 - https://www.luogu.com.cn/problem/P3185
// 2. BZOJ 1188. [HNOI2007]分裂游戏 - https://www.lydsy.com/JudgeOnline/problem.php?id=1188
// 
// 算法核心思想：
// 1. SG函数方法：通过SG定理计算每个状态的SG值
// 2. Multi-SG游戏：每个糖豆可以看作一个独立的游戏
// 
// 时间复杂度分析：
// 1. 预处理：O(n^3) - 计算每个位置的SG值
// 2. 查询：O(n^3) - 遍历所有可能的操作
// 
// 空间复杂度分析：
// O(n) - 存储SG值和状态数组
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 输入输出优化：使用高效的输入输出方法
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_SplitGame {

	// 20 -> 0
	// 左    右
	public static int MAXN = 21;

	public static int[] nums = new int[MAXN];

	public static int[] sg = new int[MAXN];

	public static int MAXV = 101;

	public static boolean[] appear = new boolean[MAXV];

	public static int t, n;

	// 
	// 算法原理：
	// 1. 每个糖豆可以看作一个独立的游戏
	// 2. 位置i的SG值可以通过其后继状态计算得出
	// 3. 整个游戏的SG值等于所有奇数个糖豆位置SG值的异或和
	public static void build() {
		// 计算每个位置的SG值
		for (int i = 1; i < MAXN; i++) {
			// 初始化appear数组
			Arrays.fill(appear, false);
			
			// 计算位置i的所有后继状态的SG值
			// 后继状态为(j,k)，其中i-1 >= j >= k >= 0
			for (int j = i - 1; j >= 0; j--) {
				for (int k = j; k >= 0; k--) {
					appear[sg[j] ^ sg[k]] = true;
				}
			}
			
			// 计算mex值
			for (int s = 0; s < MAXV; s++) {
				if (!appear[s]) {
					sg[i] = s;
					break;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		build();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		t = (int) in.nval;
		for (int i = 0; i < t; i++) {
			in.nextToken();
			n = (int) in.nval;
			// 为了方便处理，将瓶子编号反转
			for (int j = n - 1; j >= 0; j--) {
				in.nextToken();
				nums[j] = (int) in.nval;
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	// 
	// 算法原理：
	// 1. 计算当前状态的SG值
	// 2. 遍历所有可能的第一步操作
	// 3. 找到能使对手处于必败态的操作
	public static String compute() {
		// 计算当前状态的SG值
		// 每个糖豆都是独立游戏，所以把所有糖果的sg值异或
		int eor = 0; // 每个糖果都是独立游戏，所以把所有糖果的SG值异或
		for (int i = n - 1; i >= 0; i--) {
			// 只有奇数个糖豆的位置对SG值有贡献
			if (nums[i] % 2 == 1) {
				eor ^= sg[i];
			}
		}
		
		// SG值为0表示当前玩家必败
		if (eor == 0) {
			return "-1 -1 -1\n" + "0";
		}
		
		// 计数和记录字典序最小的操作
		int cnt = 0, a = -1, b = -1, c = -1, pos;
		// 遍历所有可能的第一步操作
		for (int i = n - 1; i >= 1; i--) {
			// 只有有糖豆的瓶子才能操作
			if (nums[i] > 0) {
				// 遍历所有可能的(j,k)对
				for (int j = i - 1; j >= 0; j--) {
					for (int k = j; k >= 0; k--) {
						// 计算操作后的SG值
						// i j k
						pos = eor ^ sg[i] ^ sg[j] ^ sg[k];
						// 如果能使对手处于必败态
						if (pos == 0) {
							cnt++;
							// 记录字典序最小的操作
							if (a == -1) {
								a = i;
								b = j;
								c = k;
							}
						}
					}
				}
			}
		}
		
		// 返回结果
		return String.valueOf((n - 1 - a) + " " + (n - 1 - b) + " " + (n - 1 - c) + "\n" + cnt);
	}

}