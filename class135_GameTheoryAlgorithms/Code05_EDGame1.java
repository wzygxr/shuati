package class096;

// 计算两堆石子的SG值
// 桌上有两堆石子，石头数量分别为a、b
// 任取一堆石子，将其移走，然后分割同一组的另一堆石子
// 从中取出若干个石子放在被移走的位置，组成新的一堆
// 操作完成后，组内每堆的石子数必须保证大于0
// 显然，被分割的一堆的石子数至少要为2
// 两个人轮流进行分割操作，如果轮到某人进行操作时，两堆石子数均为1，判此人输掉比赛
// 计算sg[a][b]的值并找到简洁规律
// 本文件仅为题目5打表找规律的代码
// 
// 题目来源：
// 1. 洛谷 P2148 [SDOI2009]E&D - https://www.luogu.com.cn/problem/P2148
// 2. SPOJ 3805. E&D Game - https://www.spoj.com/problems/ED/
// 
// 算法核心思想：
// 1. 动态规划方法：通过递归+记忆化搜索计算每个状态的SG值
// 2. SG函数方法：通过SG定理计算每个状态的SG值
// 
// 时间复杂度分析：
// O(a*b*(a+b)) - 递归计算每个状态
// 
// 空间复杂度分析：
// O(a*b) - 记忆化搜索数组
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：使用记忆化搜索避免重复计算
// 3. 可读性：添加详细注释说明算法原理
public class Code05_EDGame1 {

	public static int MAXN = 1001;

	public static int[][] dp = new int[MAXN][MAXN];

	public static void build() {
		for (int i = 0; i < MAXN; i++) {
			for (int j = 0; j < MAXN; j++) {
				dp[i][j] = -1;
			}
		}
	}

	// 
	// 算法原理：
	// 使用递归+记忆化搜索计算每个状态的SG值
	// 1. 终止状态：当两堆石子数均为1时，SG值为0
	// 2. 递推关系：SG值等于所有后继状态SG值集合的mex值
	// 3. 记忆化：避免重复计算相同状态
	public static int sg(int a, int b) {
		// 异常处理：处理非法输入
		if (a <= 0 || b <= 0) {
			return -1;
		}
		
		// 终止状态：当两堆石子数均为1时，SG值为0
		if (a == 1 && b == 1) {
			return 0;
		}
		
		// 记忆化搜索
		if (dp[a][b] != -1) {
			return dp[a][b];
		}
		
		// appear数组用于计算mex值
		boolean[] appear = new boolean[Math.max(a, b) + 1];
		
		// 计算从第一堆移走石子的所有后继状态
		if (a > 1) {
			// 将第一堆分割成l和r两部分，l+r=a-1
			for (int l = 1, r = a - 1; l < a; l++, r--) {
				appear[sg(l, r)] = true;
			}
		}
		
		// 计算从第二堆移走石子的所有后继状态
		if (b > 1) {
			// 将第二堆分割成l和r两部分，l+r=b-1
			for (int l = 1, r = b - 1; l < b; l++, r--) {
				appear[sg(l, r)] = true;
			}
		}
		
		// 计算mex值
		int ans = 0;
		for (int s = 0; s <= Math.max(a, b); s++) {
			if (!appear[s]) {
				ans = s;
				break;
			}
		}
		
		// 记忆化结果
		dp[a][b] = ans;
		return ans;
	}

	public static void f1() {
		System.out.println("石子数9以内所有组合的sg值");
		System.out.println();
		System.out.print("    ");
		for (int i = 1; i <= 9; i++) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println();
		for (int a = 1; a <= 9; a++) {
			System.out.print(a + "   ");
			for (int b = 1; b < a; b++) {
				System.out.print("X ");
			}
			for (int b = a; b <= 9; b++) {
				int sg = sg(a, b);
				System.out.print(sg + " ");
			}
			System.out.println();
		}
	}

	public static void f2() {
		System.out.println("石子数9以内所有组合的sg值，但是行列都-1");
		System.out.println();
		System.out.print("    ");
		for (int i = 0; i <= 8; i++) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println();
		for (int a = 1; a <= 9; a++) {
			System.out.print((a - 1) + "   ");
			for (int b = 1; b < a; b++) {
				System.out.print("X ");
			}
			for (int b = a; b <= 9; b++) {
				int sg = sg(a, b);
				System.out.print(sg + " ");
			}
			System.out.println();
		}
	}

	public static void f3() {
		System.out.println("测试开始");
		for (int a = 1; a < MAXN; a++) {
			for (int b = 1; b < MAXN; b++) {
				int sg1 = sg(a, b);
				int sg2 = lowZero((a - 1) | (b - 1));
				if (sg1 != sg2) {
					System.out.println("出错了!");
				}
			}
		}
		System.out.println("测试结束");
	}

	// 返回status最低位的0在第几位
	// 
	// 算法原理：
	// 通过观察SG值表发现规律：
	// sg(a,b) = lowZero((a-1)|(b-1))
	// 其中lowZero(x)返回x的二进制表示中最低位0的位置
	public static int lowZero(int status) {
		int cnt = 0;
		while (status > 0) {
			if ((status & 1) == 0) {
				break;
			}
			status >>= 1;
			cnt++;
		}
		return cnt;
	}

	public static void main(String[] args) {
		build();
		f1();
		System.out.println();
		System.out.println();
		f2();
		System.out.println();
		System.out.println();
		f3();
	}

}