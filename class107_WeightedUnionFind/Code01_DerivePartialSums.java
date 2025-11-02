package class156;

// 推导部分和，带权并查集模版题1
// 有n个数字，下标1 ~ n，但是并不知道每个数字是多少
// 先给出m个数字段的累加和，再查询q个数字段的累加和
// 给出数字段累加和的操作 l r v，代表l~r范围上的数字，累加和为v
// 查询数字段累加和的操作 l r，代表查询l~r范围上的数字累加和
// 请根据m个给定，完成q个查询，如果某个查询无法给出答案，打印"UNKNOWN"
// 1 <= n, m, q <= 10^5
// 累加和不会超过long类型范围
// 测试链接 : https://www.luogu.com.cn/problem/P8779
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 带权并查集解决区间和问题
 * 
 * 核心思想：
 * 1. 将区间和问题转化为前缀和问题：区间[l,r]的和等于sum[r] - sum[l-1]
 * 2. 使用带权并查集维护前缀和之间的关系
 * 3. dist[i] 表示 sum[i] - sum[find(i)]，即节点i到其根节点的"距离"
 * 
 * 时间复杂度分析：
 * - prepare: O(n) 初始化操作
 * - find: O(α(n)) 近似O(1)，其中α是阿克曼函数的反函数
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O((m+q) * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 区间和查询与更新
 * - 差分约束系统
 * - 数据一致性验证
 */
public class Code01_DerivePartialSums {

	public static int MAXN = 100002;

	public static long INF = Long.MAX_VALUE;

	public static int n, m, q;

	// father[i] 表示节点i的父节点
	public static int[] father = new int[MAXN];
	
	// dist[i] 表示 sum[i] - sum[find(i)]，即节点i到根节点的距离
	public static long[] dist = new long[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
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
	public static void union(int l, int r, long v) {
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
	 * 查询区间[l,r]的和
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 查询区间左边界
	 * @param r 查询区间右边界+1（转换为前缀和表示）
	 * @return 区间和，如果无法确定则返回INF
	 */
	public static long query(int l, int r) {
		// 如果两个节点不在同一集合中，无法确定关系
		if (find(l) != find(r)) {
			return INF;
		}
		// 区间[l,r]的和 = sum[r] - sum[l-1] = (sum[r] - sum[find(r)]) - (sum[l-1] - sum[find(l-1)])
		// 由于find(l) == find(r)，所以结果为 dist[l-1] - dist[r]
		return dist[l] - dist[r];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		// n+1是为了处理前缀和，将区间[l,r]转换为sum[r] - sum[l-1]
		n = (int) in.nval + 1;
		in.nextToken();
		m = (int) in.nval;
		in.nextToken();
		q = (int) in.nval;
		prepare();
		int l, r;
		long v;
		// 处理m个给定条件
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval + 1; // 转换为前缀和表示
			in.nextToken();
			v = (long) in.nval;
			union(l, r, v);
		}
		// 处理q个查询
		for (int i = 1; i <= q; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval + 1; // 转换为前缀和表示
			v = query(l, r);
			if (v == INF) {
				out.println("UNKNOWN");
			} else {
				out.println(v);
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}