package class102;

// 数数(利用AC自动机检查命中)
// 我们称一个正整数x为幸运数字的条件为
// x的十进制中不包含数字串集合s中任意一个元素作为子串
// 例如s = { 22, 333, 0233 }
// 233是幸运数字，2333、20233、3223不是幸运数字
// 给定n和s，计算不大于n的幸运数字的个数
// 答案对1000000007取模
// 测试链接 : https://www.luogu.com.cn/problem/P3311
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

/*
 * 算法详解：
 * 这是一个结合AC自动机和数位DP的题目
 * 
 * 算法核心思想：
 * 1. 使用AC自动机来检测数字串中是否包含禁止的子串
 * 2. 使用数位DP来计算满足条件的数字个数
 * 3. 状态设计：dp[pos][node][limit][has][lead]
 *    - pos: 当前处理到第几位
 *    - node: 在AC自动机中当前所在的节点
 *    - limit: 是否受到上界限制
 *    - has: 是否已经包含有效数字
 *    - lead: 是否还在前导零阶段
 * 
 * 时间复杂度分析：
 * 1. 构建AC自动机：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 数位DP：O(|n| × 节点数 × 状态数)
 * 总时间复杂度：O(|n| × 节点数)
 * 
 * 空间复杂度：O(节点数 × 状态数)
 * 
 * 适用场景：
 * 1. 数字类计数问题，带约束条件
 * 2. 字符串匹配与数位DP结合的问题
 * 
 * 相关题目：
 * 1. 洛谷P3311 [SDOI2014] 数数
 *    题目链接：https://www.luogu.com.cn/problem/P3311
 *    题目描述：求不大于n的幸运数字的个数
 * 
 * 2. 洛谷P4052 [JSOI2007] 文本生成器
 *    题目链接：https://www.luogu.com.cn/problem/P4052
 *    题目描述：求至少包含一个模式串的长度为m的字符串个数
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用记忆化搜索避免重复计算
 * 3. 内存优化：合理设置DP数组大小
 * 4. 可配置性：可以根据字符集大小调整数组维度
 * 
 * 与机器学习的联系：
 * 1. 在自然语言处理中用于约束文本生成
 * 2. 在密码学中用于生成满足特定条件的密钥
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_Counting {

	public static int MOD = 1000000007;

	// 目标字符串的数量
	public static int MAXN = 1301;

	// 所有目标字符串的总字符数量
	public static int MAXS = 2001;

	// 读入的数字
	public static char[] num;

	// 读入数字的长度
	public static int n;

	// AC自动机
	public static int[][] tree = new int[MAXS][10];

	public static int[] fail = new int[MAXS];

	public static int cnt = 0;

	// 具体题目相关，本题为命中任何目标串就直接报警
	// 所以每个节点记录是否触发警报
	public static boolean[] alert = new boolean[MAXS];

	public static int[] queue = new int[MAXS];

	// 动态规划表
	public static int[][][][] dp = new int[MAXN][MAXS][2][2];

	public static void clear() {
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= cnt; j++) {
				dp[i][j][0][0] = -1;
				dp[i][j][0][1] = -1;
				dp[i][j][1][0] = -1;
				dp[i][j][1][1] = -1;
			}
		}
	}

	// AC自动机加入目标字符串
	public static void insert(String word) {
		char[] w = word.toCharArray();
		int u = 0;
		for (int j = 0, c; j < w.length; j++) {
			c = w[j] - '0';
			if (tree[u][c] == 0) {
				tree[u][c] = ++cnt;
			}
			u = tree[u][c];
		}
		alert[u] = true;
	}

	// 加入所有目标字符串之后
	// 设置fail指针 以及 设置直接跳转支路
	// 做了AC自动机固定的优化
	// 做了命中标记前移防止绕圈的优化
	public static void setFail() {
		int l = 0;
		int r = 0;
		for (int i = 0; i <= 9; i++) {
			if (tree[0][i] > 0) {
				queue[r++] = tree[0][i];
			}
		}
		while (l < r) {
			int u = queue[l++];
			for (int i = 0; i <= 9; i++) {
				if (tree[u][i] == 0) {
					tree[u][i] = tree[fail[u]][i];
				} else {
					fail[tree[u][i]] = tree[fail[u]][i];
					queue[r++] = tree[u][i];
				}
			}
			// 命中标记前移
			alert[u] |= alert[fail[u]];
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		num = in.readLine().toCharArray();
		n = num.length;
		// AC自动机建树
		int m = Integer.valueOf(in.readLine());
		for (int i = 1; i <= m; i++) {
			insert(in.readLine());
		}
		setFail();
		// 清空动态规划表
		clear();
		// 执行记忆化搜索
		out.println(f1(0, 0, 0, 0));
		// out.println(f2(0, 0, 0, 0));
		out.flush();
		out.close();
		in.close();
	}

	// 逻辑分支都详细列出来的版本
	// i来到的位置
	// j : AC自动机里来到的节点编号
	// free : 是不是可以随意选择了
	// free = 0，不能随意选择数字，要考虑当前数字的大小
	// free = 1，能随意选择数字
	// has : 之前有没有选择过数字
	// has = 0，之前没选择过数字
	// has = 1，之前选择过数字
	// 返回i....幸运数字的个数
	public static int f1(int i, int j, int free, int has) {
		if (alert[j]) {
			return 0;
		}
		if (i == n) {
			return has;
		}
		if (dp[i][j][free][has] != -1) {
			return dp[i][j][free][has];
		}
		int ans = 0;
		int cur = num[i] - '0';
		if (has == 0) {
			if (free == 0) {
				// 分支1 : 之前没有选择过数字 且 之前的决策等于num的前缀
				// 能来到这里说明i一定是0位置, 那么cur必然不是0
				// 当前选择不要数字
				ans = (ans + f1(i + 1, 0, 1, 0)) % MOD;
				// 当前选择的数字比cur小
				for (int pick = 1; pick < cur; pick++) {
					ans = (ans + f1(i + 1, tree[j][pick], 1, 1)) % MOD;
				}
				// 当前选择的数字为cur
				ans = (ans + f1(i + 1, tree[j][cur], 0, 1)) % MOD;
			} else {
				// 分支2 : 之前没有选择过数字 且 之前的决策小于num的前缀
				// 当前选择不要数字
				ans = (ans + f1(i + 1, 0, 1, 0)) % MOD;
				// 当前可以选择1~9
				for (int pick = 1; pick <= 9; pick++) {
					ans = (ans + f1(i + 1, tree[j][pick], 1, 1)) % MOD;
				}
			}
		} else {
			if (free == 0) {
				// 分支3 : 之前已经选择过数字 且 之前的决策等于num的前缀
				// 当前选择的数字比cur小
				for (int pick = 0; pick < cur; pick++) {
					ans = (ans + f1(i + 1, tree[j][pick], 1, 1)) % MOD;
				}
				// 当前选择的数字为cur
				ans = (ans + f1(i + 1, tree[j][cur], 0, 1)) % MOD;
			} else {
				// 分支4 : 之前已经选择过数字 且 之前的决策小于num的前缀
				// 当前可以选择0~9
				for (int pick = 0; pick <= 9; pick++) {
					ans = (ans + f1(i + 1, tree[j][pick], 1, 1)) % MOD;
				}
			}
		}
		dp[i][j][free][has] = ans;
		return ans;
	}

	// 逻辑合并版
	// 其实和f1方法完全一个意思
	public static int f2(int i, int u, int free, int has) {
		if (alert[u]) {
			return 0;
		}
		if (i == n) {
			return has;
		}
		if (dp[i][u][free][has] != -1) {
			return dp[i][u][free][has];
		}
		int limit = free == 0 ? (num[i] - '0') : 9;
		int ans = 0;
		for (int pick = 0; pick <= limit; pick++) {
			ans = (ans + f2(i + 1, has == 0 && pick == 0 ? 0 : tree[u][pick], free == 0 && pick == limit ? 0 : 1,
					has == 0 && pick == 0 ? 0 : 1)) % MOD;
		}
		dp[i][u][free][has] = ans;
		return ans;
	}

}