package class064;

// 飞行路线（自己手撸的堆）
// Alice和Bob现在要乘飞机旅行，他们选择了一家相对便宜的航空公司
// 该航空公司一共在n个城市设有业务，设这些城市分别标记为0 ~ n−1
// 一共有m种航线，每种航线连接两个城市，并且航线有一定的价格
// Alice 和 Bob 现在要从一个城市沿着航线到达另一个城市，途中可以进行转机
// 航空公司对他们这次旅行也推出优惠，他们可以免费在最多k种航线上搭乘飞机
// 那么 Alice 和 Bob 这次出行最少花费多少
// 测试链接 : https://www.luogu.com.cn/problem/P4568
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code06_FlightPath2 {

	public static int MAXN = 10001;

	public static int MAXM = 100001;

	public static int MAXK = 11;

	// 链式前向星建图需要
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXM];

	public static int[] to = new int[MAXM];

	public static int[] weight = new int[MAXM];

	public static int cnt;

	// Dijkstra需要
	// distance[i][j]表示到达城市i且已使用j次免费机会的最少花费
	public static int[][] distance = new int[MAXN][MAXK];

	// visited[i][j]表示状态(城市i, 使用j次免费机会)是否已经确定了最短路径
	public static boolean[][] visited = new boolean[MAXN][MAXK];

	// 自己写的普通堆，静态结构，推荐
	// 注意是自己写的普通堆，不是反向索引堆
	// 因为(点编号，使用免费路线的次数)，两个参数的组合才是图中的点
	// 两个参数的组合对应一个点(一个堆的下标)，所以反向索引堆不好写
	// 其实也能实现，二维点变成一维下标即可
	// 但是会造成很多困惑，索性算了，就手写普通堆吧
	// 0 : 到达的城市编号
	// 1 : 已经使用的免单次数
	// 2 : 沿途的花费
	public static int[][] heap = new int[MAXM * MAXK][3];

	public static int heapSize;

	public static int n, m, k, s, t;

	// 初始化函数
	// 时间复杂度: O(n*k)
	// 空间复杂度: O(n*k)
	public static void build() {
		cnt = 1;
		heapSize = 0;
		for (int i = 0; i < n; i++) {
			head[i] = 0;
			for (int j = 0; j <= k; j++) {
				distance[i][j] = Integer.MAX_VALUE;
				visited[i][j] = false;
			}
		}
	}

	// 链式前向星加边
	// 时间复杂度: O(1)
	// 空间复杂度: O(1)
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt++;
	}

	// 向堆中添加元素
	// 时间复杂度: O(log(n*k))
	// 空间复杂度: O(1)
	public static void push(int u, int t, int c) {
		heap[heapSize][0] = u;
		heap[heapSize][1] = t;
		heap[heapSize][2] = c;
		int i = heapSize++;
		// 向上调整堆
		while (heap[i][2] < heap[(i - 1) / 2][2]) {
			swap(i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	public static int u, use, cost;

	// 从堆中弹出元素
	// 时间复杂度: O(log(n*k))
	// 空间复杂度: O(1)
	public static void pop() {
		u = heap[0][0];
		use = heap[0][1];
		cost = heap[0][2];
		swap(0, --heapSize);
		heapify(0);
	}

	// 堆向下调整
	// 时间复杂度: O(log(n*k))
	// 空间复杂度: O(1)
	public static void heapify(int i) {
		int l = i * 2 + 1;
		while (l < heapSize) {
			int best = l + 1 < heapSize && heap[l + 1][2] < heap[l][2] ? l + 1 : l;
			best = heap[best][2] < heap[i][2] ? best : i;
			if (best == i) {
				break;
			}
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	// 交换堆中两个元素
	// 时间复杂度: O(1)
	// 空间复杂度: O(1)
	public static void swap(int i, int j) {
		int[] tmp = heap[i];
		heap[i] = heap[j];
		heap[j] = tmp;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken(); m = (int) in.nval;
			in.nextToken(); k = (int) in.nval;
			in.nextToken(); s = (int) in.nval;
			in.nextToken(); t = (int) in.nval;
			build();
			for (int i = 0, a, b, c; i < m; i++) {
				in.nextToken(); a = (int) in.nval;
				in.nextToken(); b = (int) in.nval;
				in.nextToken(); c = (int) in.nval;
				addEdge(a, b, c);
				addEdge(b, a, c);
			}
			out.println(dijkstra());
		}
		out.flush();
		out.close();
		br.close();
	}

	// Dijkstra算法主函数
	// 时间复杂度: O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数
	// 空间复杂度: O(V*k)
	public static int dijkstra() {
		// 初始状态：在起点城市且未使用免费机会，花费为0
		distance[s][0] = 0;
		push(s, 0, 0);
		
		while (heapSize > 0) {
			pop();
			
			// 如果已经处理过，跳过
			if (visited[u][use]) {
				continue;
			}
			
			// 标记为已处理
			visited[u][use] = true;
			
			// 如果到达终点，直接返回结果
			if (u == t) {
				// 常见剪枝
				// 发现终点直接返回
				// 不用等都结束
				return cost;
			}
			
			// 遍历所有出边
			for (int ei = head[u], v, w; ei > 0; ei = next[ei]) {
				v = to[ei];
				w = weight[ei];
				
				// 使用免费机会
				if (use < k && distance[v][use + 1] > distance[u][use]) {
					// 使用免费
					distance[v][use + 1] = distance[u][use];
					push(v, use + 1, distance[v][use + 1]);
				}
				
				// 不使用免费机会
				if (distance[v][use] > distance[u][use] + w) {
					// 不用免费
					distance[v][use] = distance[u][use] + w;
					push(v, use, distance[v][use]);
				}
			}
		}
		return -1;
	}

}