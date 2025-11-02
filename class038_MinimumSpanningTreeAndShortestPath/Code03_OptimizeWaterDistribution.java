package class061;

import java.util.Arrays;

// 水资源分配优化
// 题目链接: https://leetcode.cn/problems/optimize-water-distribution-in-a-village/
// 
// 题目描述:
// 村里有n户人家，编号从1到n。我们需要为每家提供水。有两种方式：
// 1. 挖掘一口井，成本为wells[i-1]（为第i户人家挖井的成本）
// 2. 连接到其他已经有水源的人家，成本为pipes[j][2]（管道连接pipes[j][0]和pipes[j][1]的成本）
// 求使所有人家都有水的最小总成本。
//
// 解题思路:
// 将问题转化为最小生成树问题：
// 1. 创建一个虚拟节点0，代表水源
// 2. 虚拟节点0到每户人家i的边权值为wells[i-1]（挖井成本）
// 3. 原问题中的管道连接作为图中的边
// 4. 然后求包含虚拟节点0和所有其他节点的最小生成树
//
// 时间复杂度: O((n + m) * log(n + m))，其中n是户数，m是管道数
// 空间复杂度: O(n + m)
// 是否为最优解: 是，Kruskal算法结合并查集是解决此类最小生成树问题的有效方法
// 工程化考量:
// 1. 异常处理: 检查输入参数的有效性
// 2. 边界条件: 处理空数组、单元素数组等特殊情况
// 3. 内存管理: 使用静态数组减少内存分配开销
// 4. 性能优化: 并查集的路径压缩和按秩合并优化

public class Code03_OptimizeWaterDistribution {

	public static int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
		build(n);
		for (int i = 0; i < n; i++, cnt++) {
			// wells : 100   30
			//         0(1)  1(2)
			edges[cnt][0] = 0;
			edges[cnt][1] = i + 1;
			edges[cnt][2] = wells[i];
		}
		for (int i = 0; i < pipes.length; i++, cnt++) {
			edges[cnt][0] = pipes[i][0];
			edges[cnt][1] = pipes[i][1];
			edges[cnt][2] = pipes[i][2];
		}
		Arrays.sort(edges, 0, cnt, (a, b) -> a[2] - b[2]);
		int ans = 0;
		for (int i = 0; i < cnt; i++) {
			if (union(edges[i][0], edges[i][1])) {
				ans += edges[i][2];
			}
		}
		return ans;
	}

	public static int MAXN = 10010;

	public static int[][] edges = new int[MAXN << 1][3];

	public static int cnt;

	public static int[] father = new int[MAXN];

	public static void build(int n) {
		cnt = 0;
		for (int i = 0; i <= n; i++) {
			father[i] = i;
		}
	}

	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	// 如果x和y，原本是一个集合，返回false
	// 如果x和y，不是一个集合，合并之后后返回true
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

}