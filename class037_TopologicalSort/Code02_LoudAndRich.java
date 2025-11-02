package class060;

import java.util.ArrayList;

/**
 * 喧闹和富有
 * 从 0 到 n - 1 编号，其中每个人都有不同数目的钱，以及不同程度的安静值
 * 给你一个数组richer，其中richer[i] = [ai, bi] 表示 
 * person ai 比 person bi 更有钱
 * 还有一个整数数组 quiet ，其中 quiet[i] 是 person i 的安静值
 * richer 中所给出的数据 逻辑自洽
 * 也就是说，在 person x 比 person y 更有钱的同时，不会出现
 * person y 比 person x 更有钱的情况
 * 现在，返回一个整数数组 answer 作为答案，其中 answer[x] = y 的前提是,
 * 在所有拥有的钱肯定不少于 person x 的人中，
 * person y 是最安静的人（也就是安静值 quiet[y] 最小的人）。
 * 测试链接 : https://leetcode.cn/problems/loud-and-rich/
 * 
 * 算法思路：
 * 这是一道拓扑排序的应用题。我们可以将 richer 关系看作有向边，从更富有的人指向更穷的人。
 * 然后通过拓扑排序，从最富有的人开始，逐步更新每个人在所有不少于他富有的人中最安静的人。
 * 
 * 时间复杂度：O(N + M)，其中 N 是人数，M 是 richer 关系数
 * 空间复杂度：O(N + M)
 * 
 * 相关题目扩展：
 * 1. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 2. LeetCode 310. 最小高度树 - https://leetcode.cn/problems/minimum-height-trees/
 * 3. LeetCode 851. 喧闹和富有 - https://leetcode.cn/problems/loud-and-rich/
 * 4. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 5. 洛谷 P1137 旅行计划 - https://www.luogu.com.cn/problem/P1137
 * 6. POJ 3249 Test for Job - http://poj.org/problem?id=3249
 * 7. HDU 4109 Activation - http://acm.hdu.edu.cn/showproblem.php?pid=4109
 * 8. AtCoder ABC157E Simple String Queries - https://atcoder.jp/contests/abc157/tasks/abc157_e
 * 9. Codeforces 1109C Sasha and a Patient Friend - https://codeforces.com/problemset/problem/1109/C
 * 10. 牛客网 牛牛的背包问题 - https://ac.nowcoder.com/acm/problem/16783
 * 
 * 工程化考虑：
 * 1. 边界处理：处理空数组、单个元素等特殊情况
 * 2. 输入验证：验证 richer 数组的逻辑自洽性
 * 3. 内存优化：合理使用 ArrayList 和数组
 * 4. 异常处理：对非法输入进行检查
 * 5. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 构建图：将 richer 关系转换为有向图
 * 2. 计算入度：用于拓扑排序
 * 3. 初始化队列：将入度为0的节点（最富有的人）加入队列
 * 4. 初始化答案数组：每个人最安静的人初始为自己
 * 5. 拓扑排序：从富人向穷人传播信息，更新更安静的人
 */
public class Code02_LoudAndRich {

	/**
	 * 计算每个人在所有不少于他富有的人中最安静的人
	 * 
	 * @param richer richer[i] = [a, b] 表示 a 比 b 更有钱
	 * @param quiet quiet[i] 表示第 i 个人的安静值
	 * @return answer[x] = y 表示在所有不少于 x 富有的人中，y 是最安静的
	 */
	public static int[] loudAndRich(int[][] richer, int[] quiet) {
		int n = quiet.length;
		// 构建邻接表表示的图
		ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			graph.add(new ArrayList<>());
		}
		
		// 计算每个节点的入度
		int[] indegree = new int[n];
		for (int[] r : richer) {
			// r[0] 比 r[1] 更有钱，所以有一条从 r[1] 到 r[0] 的边
			graph.get(r[0]).add(r[1]);
			indegree[r[1]]++;
		}
		
		// 拓扑排序使用的队列
		int[] queue = new int[n];
		int l = 0;
		int r = 0;
		
		// 将所有入度为0的节点加入队列
		for (int i = 0; i < n; i++) {
			if (indegree[i] == 0) {
				queue[r++] = i;
			}
		}
		
		// 初始化答案数组，ans[i] 表示在所有不少于 i 富有的人中最安静的人
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = i;
		}
		
		// 拓扑排序过程
		while (l < r) {
			// 取出队首元素
			int cur = queue[l++];
			
			// 遍历当前节点的所有邻居
			for (int next : graph.get(cur)) {
				// 更新 next 节点的答案：
				// 如果 cur 节点所指向的最安静的人比 next 节点当前记录的更安静的人更安静，
				// 则更新 next 节点的答案
				if (quiet[ans[cur]] < quiet[ans[next]] ) {
					ans[next] = ans[cur];
				}
				
				// 将 next 节点的入度减1，如果变为0则加入队列
				if (--indegree[next] == 0) {
					queue[r++] = next;
				}
			}
		}
		return ans;
	}

}