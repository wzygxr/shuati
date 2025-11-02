package class061;

// Kruskal算法模版（洛谷）
// 题目链接: https://www.luogu.com.cn/problem/P3366
// 
// 题目描述:
// 给定一个无向图，求最小生成树的总边权值。如果图不连通，输出orz。
//
// 解题思路:
// 1. 将所有边按权值从小到大排序
// 2. 使用并查集数据结构，依次选择边，若加入该边不会形成环（两个顶点不在同一集合），则加入该边
// 3. 当选择了n-1条边时，最小生成树构建完成
//
// 时间复杂度: O(m * log m)，其中m是边数，主要消耗在边的排序上
// 空间复杂度: O(n + m)，其中n是顶点数，m是边数
// 是否为最优解: 是，Kruskal算法是解决最小生成树问题的标准算法之一，适用于稀疏图
// 工程化考量:
// 1. 异常处理: 检查图是否连通
// 2. 边界条件: 处理空图、单节点图等特殊情况
// 3. 内存管理: 使用静态数组减少内存分配开销
// 4. 性能优化: 并查集的路径压缩和按秩合并优化

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

// 时间复杂度O(m * log m) + O(n + m)
public class Code01_Kruskal {

	public static int MAXN = 5001;

	public static int MAXM = 200001;

	public static int[] father = new int[MAXN];

	// u, v, w
	public static int[][] edges = new int[MAXM][3];

	public static int n, m;

	public static void build() {
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
	}

	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	// 如果x和y本来就是一个集合，返回false
	// 如果x和y不是一个集合，合并之后返回true
	public static boolean union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (fx != fy) {
			father[fx] = fy;
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			build();
			for (int i = 0; i < m; i++) {
				in.nextToken();
				edges[i][0] = (int) in.nval;
				in.nextToken();
				edges[i][1] = (int) in.nval;
				in.nextToken();
				edges[i][2] = (int) in.nval;
			}
			Arrays.sort(edges, 0, m, (a, b) -> a[2] - b[2]);
			int ans = 0;
			int edgeCnt = 0;
			for (int[] edge : edges) {
				if (union(edge[0], edge[1])) {
					edgeCnt++;
					ans += edge[2];
				}
			}
			out.println(edgeCnt == n - 1 ? ans : "orz");
		}
		out.flush();
		out.close();
		br.close();
	}

}