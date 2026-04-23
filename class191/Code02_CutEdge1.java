package class191;

// 割边模版题2，java版
// 给定一张无向图，一共n个点、m条边
// 点的编号0~n-1，保证所有点连通
// 找出图中所有的割边，返回每条割边的两个端点
// 请保证原图即使有重边和自环，答案依然正确
// 1 <= n、m <= 10^5
// 测试链接 : https://leetcode.cn/problems/critical-connections-in-a-network/
// 提交以下代码中的Solution类，可以通过所有测试用例

// 导入Java集合框架，用于存储结果
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code02_CutEdge1 {

	/**
	 * LeetCode题目：1192. Critical Connections in a Network
	 * 求割边（Critical Connections）的Solution类
	 */
	class Solution {

		// 定义常量：最大节点数和最大边数（+1因为数组下标从1开始）
		public static int MAXN = 100001;
		public static int MAXM = 100001;
		// 图的节点数和边数
		public static int n, m;
		// 存储每条边的两个端点（a[i]和b[i]表示第i条边）
		public static int[] a = new int[MAXM];
		public static int[] b = new int[MAXM];

		// 邻接表存储：head[i]表示节点i的第一条边的编号
		public static int[] head = new int[MAXN];
		// nxt[i]表示编号为i的边的下一条边
		public static int[] nxt = new int[MAXM << 1];
		// to[i]表示编号为i的边的终点节点
		public static int[] to = new int[MAXM << 1];
		// 当前使用的边的编号
		public static int cntg;

		// Tarjan算法核心数组
		// dfn[u]表示节点u的发现时间（dfs序）
		public static int[] dfn = new int[MAXN];
		// low[u]表示节点u及其子树中所有节点能追溯到的最小dfn值
		public static int[] low = new int[MAXN];
		// 当前dfs的时间戳计数器
		public static int cntd;

		// 标记每条边是否为割边
		public static boolean[] cutEdge = new boolean[MAXM];

		/**
		 * 初始化/重置函数
		 * 在每次调用criticalConnections前清理数据
		 */
		public static void prepare() {
			// 边的编号从1开始
			cntg = 1;
			// 重置时间戳
			cntd = 0;
			// 初始化所有节点的邻接表头和dfn、low数组
			for (int i = 1; i <= n; i++) {
				head[i] = dfn[i] = low[i] = 0;
			}
			// 初始化所有边的割边标记为false
			for (int i = 1; i <= m; i++) {
				cutEdge[i] = false;
			}
		}

		/**
		 * 添加一条从u到v的无向边
		 * 使用邻接表存储，每条无向边存储为两条有向边
		 * @param u 边的起点
		 * @param v 边的终点
		 */
		public static void addEdge(int u, int v) {
			// 头插法：将新边插入到链表头部
			nxt[++cntg] = head[u];
			to[cntg] = v;
			head[u] = cntg;
		}

		/**
		 * Tarjan算法 - 求割边
		 * 核心思想：使用dfn和low数组判断割边
		 * 对于边(u,v)，如果low[v] > dfn[u]，则该边是割边
		 * @param u 当前访问的节点
		 * @param preEdge 从父节点到u的边编号（用于跳过反向边）
		 */
		public static void tarjan(int u, int preEdge) {
			// 初始化当前节点的发现时间和low值
			dfn[u] = low[u] = ++cntd;
			// 遍历节点u的所有邻接边
			for (int e = head[u]; e > 0; e = nxt[e]) {
				// 如果这条边是来时的边（父子边），跳过
				// 通过异或1判断反向边：边e和e^1是一对反向边
				if ((e ^ 1) == preEdge) {
					continue;
				}
				// 获取边的终点节点
				int v = to[e];
				if (dfn[v] == 0) {
					// 树边：v还未被访问过
					// 递归处理子节点
					tarjan(v, e);
					// 回溯时更新low值：取min(low[u], low[v])
					low[u] = Math.min(low[u], low[v]);
					// 判断割边条件：low[v] > dfn[u]
					// 意味着v及其子树无法回到u或u的祖先
					if (low[v] > dfn[u]) {
						cutEdge[e >> 1] = true; // e>>1得到边的原始编号
					}
				} else {
					// 回边或弃边：用dfn[v]更新low[u]
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}

		/**
		 * 主函数：找出图中所有的割边
		 * @param nodeCnt 图中节点的数量
		 * @param connections 给定的边列表，每条边用两个节点编号表示（0-indexed）
		 * @return 返回所有割边的列表，每条割边用两个端点表示
		 */
		public static List<List<Integer>> criticalConnections(int nodeCnt, List<List<Integer>> connections) {
			// 设置节点数和边数
			n = nodeCnt;
			m = connections.size();
			// 初始化数据
			prepare();
			// 读取所有边，构建邻接表
			// 注意：LeetCode中节点编号是0~(n-1)，我们转为1~n便于处理
			for (int i = 1, j = 0; i <= m; i++, j++) {
				// 获取边的两个端点，+1转为1-indexed
				a[i] = connections.get(j).get(0) + 1;
				b[i] = connections.get(j).get(1) + 1;
				// 添加无向边（双向存储）
				addEdge(a[i], b[i]);
				addEdge(b[i], a[i]);
			}
			// 从节点1开始执行Tarjan算法（保证连通，任意节点开始即可）
			tarjan(1, 0);
			// 收集所有割边
			List<List<Integer>> ans = new ArrayList<>();
			for (int i = 1; i <= m; i++) {
				if (cutEdge[i]) {
					// 将边编号转回0-indexed后加入答案
					ans.add(Arrays.asList(a[i] - 1, b[i] - 1));
				}
			}
			return ans;
		}

	}

}
