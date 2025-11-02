package class177;

// 树上莫队应用：树上路径不同颜色数
// 给定一棵n个节点的树，每个节点有一个颜色
// 有m次查询，每次查询两点间路径上不同颜色的数目
// 1 <= n, m <= 100000
// 1 <= color[i] <= 100000
// 测试链接 : https://vjudge.net/problem/HDU-5678

// 树上莫队的经典应用
// 核心思想：
// 1. 使用欧拉序将树上问题转化为序列问题
// 2. 利用莫队算法处理转化后的序列问题
// 3. 通过特定的处理方式，解决树上路径查询问题

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code11_ColorfulTree {

	public static int MAXN = 100001;
	public static int MAXM = 100001;
	public static int MAXP = 20;
	
	public static int n, m;
	// 颜色数组
	public static int[] color = new int[MAXN];
	// 查询: l, r, lca, id
	public static int[][] queries = new int[MAXM][4];
	
	// 链式前向星存树
	public static int[] head = new int[MAXN];
	public static int[] to = new int[MAXN << 1];
	public static int[] next = new int[MAXN << 1];
	public static int edgeCount = 0;
	
	// 树上信息
	public static int[] depth = new int[MAXN];
	public static int[] euler = new int[MAXN << 1];  // 欧拉序
	public static int[] first = new int[MAXN];       // 第一次出现位置
	public static int[] last = new int[MAXN];        // 最后一次出现位置
	public static int[][] jump = new int[MAXN][MAXP]; // 倍增表
	public static int eulerLen = 0;                  // 欧拉序长度
	
	// 分块相关
	public static int[] belong = new int[MAXN << 1];
	
	// 窗口信息
	public static boolean[] visited = new boolean[MAXN];  // 节点是否在窗口中
	public static int[] count = new int[MAXN];            // 每种颜色的出现次数
	public static int colorTypes = 0;                     // 不同颜色的种类数
	public static int[] answers = new int[MAXM];
	
	// 添加边
	public static void addEdge(int u, int v) {
		next[++edgeCount] = head[u];
		to[edgeCount] = v;
		head[u] = edgeCount;
	}
	
	// DFS生成欧拉序和预处理LCA信息
	public static void dfs(int u, int parent) {
		depth[u] = depth[parent] + 1;
		euler[++eulerLen] = u;
		first[u] = eulerLen;
		jump[u][0] = parent;
		
		// 填充倍增表
		for (int i = 1; i < MAXP; i++) {
			jump[u][i] = jump[jump[u][i - 1]][i - 1];
		}
		
		// 遍历子节点
		for (int e = head[u]; e != 0; e = next[e]) {
			int v = to[e];
			if (v != parent) {
				dfs(v, u);
			}
		}
		
		euler[++eulerLen] = u;
		last[u] = eulerLen;
	}
	
	// 倍增法求LCA
	public static int lca(int a, int b) {
		if (depth[a] < depth[b]) {
			int temp = a;
			a = b;
			b = temp;
		}
		
		// 让a和b在同一深度
		for (int i = MAXP - 1; i >= 0; i--) {
			if (depth[jump[a][i]] >= depth[b]) {
				a = jump[a][i];
			}
		}
		
		if (a == b) return a;
		
		// 一起向上跳
		for (int i = MAXP - 1; i >= 0; i--) {
			if (jump[a][i] != jump[b][i]) {
				a = jump[a][i];
				b = jump[b][i];
			}
		}
		
		return jump[a][0];
	}
	
	// 普通莫队排序规则
	public static class QueryComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] a, int[] b) {
			if (belong[a[0]] != belong[b[0]]) {
				return belong[a[0]] - belong[b[0]];
			}
			return a[1] - b[1];
		}
	}
	
	// 翻转节点状态
	public static void toggle(int node) {
		int c = color[node];
		if (visited[node]) {
			// 节点在窗口中，移除它
			count[c]--;
			if (count[c] == 0) {
				colorTypes--;
			}
		} else {
			// 节点不在窗口中，添加它
			count[c]++;
			if (count[c] == 1) {
				colorTypes++;
			}
		}
		visited[node] = !visited[node];
	}
	
	// 主计算函数
	public static void compute() {
		int l = 1, r = 0;
		
		for (int i = 1; i <= m; i++) {
			int ql = queries[i][0];
			int qr = queries[i][1];
			int lcaNode = queries[i][2];
			int id = queries[i][3];
			
			// 调整窗口边界
			while (l > ql) toggle(euler[--l]);
			while (r < qr) toggle(euler[++r]);
			while (l < ql) toggle(euler[l++]);
			while (r > qr) toggle(euler[r--]);
			
			// 特殊处理LCA
			if (lcaNode != 0) {
				toggle(lcaNode);
			}
			
			answers[id] = colorTypes;
			
			// 恢复LCA状态
			if (lcaNode != 0) {
				toggle(lcaNode);
			}
		}
	}
	
	// 预处理函数
	public static void prepare() {
		// 对欧拉序分块
		int blockSize = (int) Math.sqrt(eulerLen);
		for (int i = 1; i <= eulerLen; i++) {
			belong[i] = (i - 1) / blockSize + 1;
		}
		
		// 对查询进行排序
		Arrays.sort(queries, 1, m + 1, new QueryComparator());
	}
	
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		while (true) {
			try {
				n = in.nextInt();
				m = in.nextInt();
			} catch (Exception e) {
				break;
			}
			
			// 初始化
			edgeCount = 0;
			Arrays.fill(head, 0);
			Arrays.fill(visited, false);
			Arrays.fill(count, 0);
			eulerLen = 0;
			colorTypes = 0;
			
			// 读取颜色
			for (int i = 1; i <= n; i++) {
				color[i] = in.nextInt();
			}
			
			// 读取边
			for (int i = 1; i < n; i++) {
				int u = in.nextInt();
				int v = in.nextInt();
				addEdge(u, v);
				addEdge(v, u);
			}
			
			// 生成欧拉序
			dfs(1, 0);
			
			// 处理查询
			for (int i = 1; i <= m; i++) {
				int u = in.nextInt();
				int v = in.nextInt();
				
				// 确保first[u] <= first[v]
				if (first[u] > first[v]) {
					int temp = u;
					u = v;
					v = temp;
				}
				
				int lcaNode = lca(u, v);
				
				if (u == lcaNode) {
					// u是LCA
					queries[i][0] = first[u];
					queries[i][1] = first[v];
					queries[i][2] = 0;  // 不需要特殊处理LCA
				} else {
					// u不是LCA
					queries[i][0] = last[u];
					queries[i][1] = first[v];
					queries[i][2] = lcaNode;
				}
				queries[i][3] = i;
			}
			
			prepare();
			compute();
			
			// 输出结果
			for (int i = 1; i <= m; i++) {
				out.println(answers[i]);
			}
		}
		
		out.flush();
		out.close();
	}
	
	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}
}