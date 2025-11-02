package class156;

// 团伙
// 注意洛谷关于本题的描述有问题，请按照如下的描述来理解题意
// 一共有n个黑帮成员，编号1 ~ n，发现了m条事实，每条事实一定属于如下两种类型中的一种
// F l r : l号成员和r号成员是朋友
// E l r : l号成员和r号成员是敌人
// 黑帮遵守如下的约定，敌人的敌人一定是朋友，朋友都来自同一个黑帮，敌人一定不是同一个黑帮
// 如果根据事实无法推断出一个成员有哪些朋友，那么该成员自己是一个黑帮
// 输入数据不存在矛盾，也就是任何两人不会推出既是朋友又是敌人的结论
// 遵守上面的约定，根据m条事实，计算黑帮有多少个
// 1 <= n <= 1000    1 <= m <= 5000
// 测试链接 : https://www.luogu.com.cn/problem/P1892
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * 扩展域并查集解决团伙问题
 * 
 * 问题分析：
 * 根据朋友和敌人关系，计算最终的黑帮（连通分量）数量
 * 
 * 核心思想：
 * 1. 使用扩展域并查集（种类并查集）处理朋友和敌人关系
 * 2. 对于每个成员i，维护两个节点：
 *    - i：表示i在某个团伙中
 *    - i+n：表示i的敌人在某个团伙中
 * 3. 关系处理：
 *    - F l r：l和r是朋友，直接合并l和r
 *    - E l r：l和r是敌人，合并(l,r+n)和(r,l+n)
 * 4. 利用性质：敌人的敌人是朋友
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n)
 * 
 * 应用场景：
 * - 社交网络分析
 * - 敌对关系处理
 * - 连通分量计算
 */
public class Code08_Gangster {

	public static int MAXN = 1001;

	public static int n, m;

	// father[i] 表示节点i的父节点
	public static int[] father = new int[MAXN];

	// enemy[i] 表示i的敌人所在的节点
	public static int[] enemy = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每个节点为自己所在集合的代表
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			// 初始时没有已知的敌人
			enemy[i] = 0;
		}
	}

	/**
	 * 查找节点i所在集合的代表
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的根节点
	 */
	public static int find(int i) {
		// 路径压缩
		father[i] = father[i] == i ? i : find(father[i]);
		return father[i];
	}

	/**
	 * 合并两个集合
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧节点
	 * @param r 右侧节点
	 */
	public static void union(int l, int r) {
		father[find(l)] = find(r);
	}

	public static void main(String[] args) throws IOException {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		prepare();
		String op;
		int l, r;
		// 处理m条事实
		for (int i = 1; i <= m; i++) {
			op = io.next();
			l = io.nextInt();
			r = io.nextInt();
			// 根据事实类型处理
			if (op.equals("F")) {
				// 朋友关系，直接合并
				union(l, r);
			} else {
				// 敌人关系，利用"敌人的敌人是朋友"的性质
				// 如果l还没有分配敌人
				if (enemy[l] == 0) {
					// 将r作为l的敌人
					enemy[l] = r;
				} else {
					// 将l的敌人和r合并（敌人的敌人是朋友）
					union(enemy[l], r);
				}
				// 如果r还没有分配敌人
				if (enemy[r] == 0) {
					// 将l作为r的敌人
					enemy[r] = l;
				} else {
					// 将r的敌人和l合并（敌人的敌人是朋友）
					union(l, enemy[r]);
				}
			}
		}
		// 统计黑帮数量
		int ans = 0;
		for (int i = 1; i <= n; i++) {
			// 如果i是所在集合的代表，说明找到一个黑帮
			if (i == father[i]) {
				ans++;
			}
		}
		// 输出黑帮数量
		io.println(ans);
		io.flush();
		io.close();
	}

	// 读写工具类
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}