package class077;

// TimusOJ 1018 Binary Apple Tree
// 题目来源：https://acm.timus.ru/problem.aspx?space=1&num=1018
// 题目大意：给定一棵二叉苹果树，树的节点代表分叉点，边代表树枝，每条边上有一定数量的苹果。
// 现在要移除一些树枝，使得最终剩下的树枝数量恰好为Q条，同时保留的苹果数量最多。
// 树的根节点是1号节点。
//
// 解题思路：
// 1. 这是一个树形动态规划问题，但也可以用区间DP的思想来解决
// 2. dp[i][j]表示以节点i为根的子树中保留j条边能获得的最大苹果数
// 3. 对于每个节点，考虑其左右子树的分配情况
// 4. 状态转移：枚举左子树保留的边数k，右子树保留的边数为j-1-k
// 5. dp[i][j] = max(dp[left][k] + dp[right][j-1-k] + apple[left_edge] + apple[right_edge])
//
// 时间复杂度：O(n^3) - 三层循环：节点数、保留边数、左子树边数分配
// 空间复杂度：O(n^2) - dp数组占用空间
//
// 工程化考虑：
// 1. 输入验证：检查输入是否合法
// 2. 树结构处理：正确构建树的结构
// 3. 边界处理：处理节点数较少的特殊情况
// 4. 异常处理：对于不合法输入给出适当提示

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TimusOJ_1018_BinaryAppleTree {

	// 树节点类
	static class TreeNode {
		int node;
		int apple;
		TreeNode left;
		TreeNode right;
		
		TreeNode(int node, int apple) {
			this.node = node;
			this.apple = apple;
		}
	}
	
	// 边类
	static class Edge {
		int from;
		int to;
		int apple;
		
		Edge(int from, int to, int apple) {
			this.from = from;
			this.to = to;
			this.apple = apple;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String[] parts = br.readLine().split(" ");
		int n = Integer.parseInt(parts[0]);
		int q = Integer.parseInt(parts[1]);
		
		// 读取边信息
		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < n - 1; i++) {
			parts = br.readLine().split(" ");
			int from = Integer.parseInt(parts[0]);
			int to = Integer.parseInt(parts[1]);
			int apple = Integer.parseInt(parts[2]);
			edges.add(new Edge(from, to, apple));
			edges.add(new Edge(to, from, apple)); // 无向图
		}
		
		int result = solve(n, q, edges);
		out.println(result);
		
		out.flush();
		out.close();
		br.close();
	}

	// 主函数：解决二叉苹果树问题
	// 时间复杂度: O(n^3) - 三层循环：节点数、保留边数、左子树边数分配
	// 空间复杂度: O(n^2) - dp数组占用空间
	public static int solve(int n, int q, List<Edge> edges) {
		if (n <= 1 || q <= 0) {
			return 0;
		}
		
		// 构建邻接表表示的树
		List<List<Edge>> graph = new ArrayList<>();
		for (int i = 0; i <= n; i++) {
			graph.add(new ArrayList<>());
		}
		
		for (Edge edge : edges) {
			graph.get(edge.from).add(edge);
		}
		
		// dp[i][j]表示以节点i为根的子树中保留j条边能获得的最大苹果数
		int[][] dp = new int[n + 1][q + 1];
		
		// 初始化dp数组
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= q; j++) {
				dp[i][j] = -1;
			}
		}
		
		// 从根节点开始进行树形DP
		return dfs(1, -1, q, graph, dp);
	}
	
	// 深度优先搜索进行树形DP
	private static int dfs(int node, int parent, int edgesCount, List<List<Edge>> graph, int[][] dp) {
		// 如果已经计算过，直接返回结果
		if (dp[node][edgesCount] != -1) {
			return dp[node][edgesCount];
		}
		
		// 边界条件
		if (edgesCount == 0) {
			return dp[node][edgesCount] = 0;
		}
		
		// 获取子节点
		List<Edge> children = new ArrayList<>();
		for (Edge edge : graph.get(node)) {
			if (edge.to != parent) {
				children.add(edge);
			}
		}
		
		// 如果没有子节点
		if (children.isEmpty()) {
			return dp[node][edgesCount] = 0;
		}
		
		// 只有一个子节点的情况
		if (children.size() == 1) {
			Edge childEdge = children.get(0);
			int result = dfs(childEdge.to, node, edgesCount - 1, graph, dp) + childEdge.apple;
			return dp[node][edgesCount] = result;
		}
		
		// 有两个子节点的情况
		Edge leftEdge = children.get(0);
		Edge rightEdge = children.get(1);
		
		int maxApples = 0;
		// 枚举左子树保留的边数
		for (int leftEdges = 0; leftEdges < edgesCount; leftEdges++) {
			int rightEdges = edgesCount - 1 - leftEdges;
			if (rightEdges >= 0) {
				int leftApples = dfs(leftEdge.to, node, leftEdges, graph, dp);
				int rightApples = dfs(rightEdge.to, node, rightEdges, graph, dp);
				int totalApples = leftApples + rightApples + leftEdge.apple + rightEdge.apple;
				maxApples = Math.max(maxApples, totalApples);
			}
		}
		
		return dp[node][edgesCount] = maxApples;
	}
}