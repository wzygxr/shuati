package class156;

// 甄别食物链
// 一共有n只动物，编号1 ~ n，每只动物都是A、B、C中的一种，A吃B、B吃C、C吃A
// 一共有k句话，希望你判断哪些话是假话，每句话是如下两类句子中的一类
// 1 X Y : 第X只动物和第Y只动物是同类
// 2 X Y : 第X只动物吃第Y只动物
// 当前的话与前面的某些真话冲突，视为假话
// 当前的话提到的X和Y，有任何一个大于n，视为假话
// 当前的话如果关于吃，又有X==Y，视为假话
// 返回k句话中，假话的数量
// 1 <= n <= 5 * 10^4    1 <= k <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P2024
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 种类并查集解决食物链问题
 * 
 * 问题分析：
 * 判断动物之间的关系描述是否一致，统计矛盾的数量
 * 
 * 核心思想：
 * 1. 使用种类并查集（扩展域并查集）处理多个种类之间的关系
 * 2. 对于每个动物，维护三种状态：
 *    - dist[i] = 0：动物i与根节点同类
 *    - dist[i] = 1：动物i吃根节点
 *    - dist[i] = 2：动物i被根节点吃
 * 3. 利用模运算处理环形关系：A吃B，B吃C，C吃A
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n + k * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 多种类关系维护
 * - 环形关系处理
 * - 逻辑一致性验证
 */
public class Code06_JudgeFoodChain {

	public static int MAXN = 50001;

	public static int n, k, ans;

	// father[i] 表示动物i的父节点
	public static int[] father = new int[MAXN];

	/**
	 * dist[i] 表示动物i与根节点的关系：
	 * - dist[i] = 0：动物i与根节点同类
	 * - dist[i] = 1：动物i吃根节点
	 * - dist[i] = 2：动物i被根节点吃
	 */
	public static int[] dist = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 假话计数器清零
		ans = 0;
		// 初始化每只动物为自己所在集合的代表
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			// 初始时每只动物与根节点同类
			dist[i] = 0;
		}
	}

	/**
	 * 查找动物i所在集合的代表，并进行路径压缩
	 * 同时更新dist[i]为动物i与根节点的关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的动物编号
	 * @return 动物i所在集合的根节点
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 保存父节点
			int tmp = father[i];
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(tmp);
			// 更新关系：当前动物与根节点的关系 = 当前动物与父节点的关系 + 父节点与根节点的关系
			// 使用模3运算处理环形关系
			dist[i] = (dist[i] + dist[tmp]) % 3;
		}
		return father[i];
	}

	/**
	 * 合并两个动物所在的集合，建立关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param op 操作类型：1表示同类，2表示吃
	 * @param l  左侧动物编号
	 * @param r  右侧动物编号
	 */
	// op == 1, 1 l r，l和r是同类
	// op == 2, 2 l r，l吃r
	public static void union(int op, int l, int r) {
		// 查找两个动物的根节点
		int lf = find(l), rf = find(r), v = op == 1 ? 0 : 1;
		// 如果不在同一集合中
		if (lf != rf) {
			// 合并两个集合
			father[lf] = rf;
			// 更新关系：
			// dist[lf] = (dist[r] - dist[l] + v + 3) % 3
			// 这里v=0表示同类，v=1表示l吃r
			dist[lf] = (dist[r] - dist[l] + v + 3) % 3;
		}
	}

	/**
	 * 检查新的关系描述是否与已有关系一致
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param op 操作类型：1表示同类，2表示吃
	 * @param l  左侧动物编号
	 * @param r  右侧动物编号
	 * @return 如果一致返回true，否则返回false
	 */
	public static boolean check(int op, int l, int r) {
		// 检查基本合法性
		if (l > n || r > n || (op == 2 && l == r)) {
			return false;
		}
		// 如果两个动物在同一集合中，可以验证一致性
		if (find(l) == find(r)) {
			if (op == 1) {
				// 检查是否同类
				// l和r同类当且仅当它们与根节点的关系相同
				if (dist[l] != dist[r]) {
					return false;
				}
			} else {
				// 检查是否l吃r
				// l吃r当且仅当l比r高一个等级（模3意义下）
				if ((dist[l] - dist[r] + 3) % 3 != 1) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		prepare();
		// 处理k句话
		for (int i = 1, op, l, r; i <= k; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval;
			// 先检查一致性
			if (!check(op, l, r)) {
				// 发现矛盾，假话计数器加1
				ans++;
			} else {
				// 一致则合并
				union(op, l, r);
			}
		}
		// 输出假话数量
		out.println(ans);
		out.flush();
		out.close();
		br.close();
	}

}