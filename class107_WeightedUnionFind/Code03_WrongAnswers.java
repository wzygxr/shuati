package class156;

// 错误答案数量，带权并查集模版题3
// 有n个数字，下标1 ~ n，但是并不知道每个数字是多少
// 操作 l r v，代表l~r范围上累加和为v
// 一共m个操作，如果某个操作和之前的操作信息自相矛盾，认为当前操作是错误的，不进行这个操作
// 最后打印错误操作的数量
// 1 <= n <= 200000    1 <= m <= 40000
// 累加和不会超过int类型范围
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=3038
// 测试链接 : https://vjudge.net/problem/HDU-3038
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 带权并查集用于错误操作计数
 * 
 * 问题分析：
 * 给定一系列区间和约束，统计其中错误（矛盾）的约束数量
 * 
 * 核心思想：
 * 1. 将区间和问题转化为前缀和问题：区间[l,r]的和等于sum[r] - sum[l-1]
 * 2. 使用带权并查集维护前缀和之间的关系
 * 3. 对于每个约束，先检查是否与已有约束矛盾，如果矛盾则计数，否则合并
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 数据一致性验证
 * - 错误检测与计数
 * - 约束满足问题
 */
public class Code03_WrongAnswers {

	public static int MAXN = 200002;

	public static int n, m, ans;

	public static int[] father = new int[MAXN];

	// dist[i] 表示 sum[i] - sum[find(i)]，即节点i到根节点的距离
	public static int[] dist = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 错误操作计数器清零
		ans = 0;
		// 初始化每个节点为自己所在集合的代表
		for (int i = 0; i <= n; i++) {
			father[i] = i;
			// 初始时每个节点到根节点的距离为0
			dist[i] = 0;
		}
	}

	/**
	 * 查找节点i的根节点，并进行路径压缩
	 * 同时更新dist[i]为节点i到根节点的距离
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的根节点
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 保存父节点
			int tmp = father[i];
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(tmp);
			// 更新距离：当前节点到根节点的距离 = 当前节点到父节点的距离 + 父节点到根节点的距离
			dist[i] += dist[tmp];
		}
		return father[i];
	}

	/**
	 * 合并两个集合，建立l和r之间的关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左边界
	 * @param r 右边界+1（转换为前缀和表示）
	 * @param v 区间和值
	 */
	public static void union(int l, int r, int v) {
		// 查找两个节点的根节点
		int lf = find(l), rf = find(r);
		// 如果不在同一集合中
		if (lf != rf) {
			// 合并两个集合
			father[lf] = rf;
			// 更新距离关系：
			// sum[lf] - sum[rf] = v + (sum[r] - sum[rf]) - (sum[l] - sum[lf])
			// 整理得：dist[lf] = v + dist[r] - dist[l]
			dist[lf] = v + dist[r] - dist[l];
		}
	}

	/**
	 * 检查新的约束条件是否与已有约束一致
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 区间左边界
	 * @param r 区间右边界+1（转换为前缀和表示）
	 * @param v 区间和值
	 * @return 如果一致返回true，否则返回false
	 */
	public static boolean check(int l, int r, int v) {
		// 如果两个节点在同一集合中，可以验证一致性
		if (find(l) == find(r)) {
			// 验证：区间[l,r]的和是否等于给定值v
			// 区间[l,r]的和 = sum[r] - sum[l-1] = (sum[r] - sum[find(r)]) - (sum[l-1] - sum[find(l-1)])
			// 由于find(l) == find(r)，所以结果为 dist[l-1] - dist[r]
			if ((dist[l] - dist[r]) != v) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 处理多个测试用例直到文件结束
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			// n+1是为了处理前缀和，将区间[l,r]转换为sum[r] - sum[l-1]
			n = (int) in.nval + 1;
			in.nextToken();
			m = (int) in.nval;
			prepare();
			// 处理m个操作
			for (int i = 1, l, r, v; i <= m; i++) {
				in.nextToken();
				l = (int) in.nval;
				in.nextToken();
				r = (int) in.nval + 1; // 转换为前缀和表示
				in.nextToken();
				v = (int) in.nval;
				// 先检查一致性
				if (!check(l, r, v)) {
					// 发现矛盾，错误操作计数器加1
					ans++;
				} else {
					// 一致则合并
					union(l, r, v);
				}
			}
			// 输出错误操作数量
			out.println(ans);
		}
		out.flush();
		out.close();
		br.close();
	}

}