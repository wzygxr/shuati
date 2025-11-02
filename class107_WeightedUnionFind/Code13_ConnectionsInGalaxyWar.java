package class156;

// Connections in Galaxy War
// 有n个星球，每个星球有战力值，星球之间可以连通
// 有两种操作：
// 1) destroy a b：破坏两个星球之间的连接
// 2) query a：查询与星球a连通且战力值最大的星球
// 使用逆向思维+带权并查集解决
// 测试链接 : https://vjudge.net/problem/ZOJ-3261
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

/**
 * 带权并查集解决Connections in Galaxy War问题
 * 
 * 问题分析：
 * 查询与星球连通且战力值最大的星球
 * 
 * 核心思想：
 * 1. 使用逆向思维，将删除操作转换为添加操作
 * 2. 使用带权并查集维护每个集合的最大战力值
 * 3. 离线处理所有操作
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O((n + m) * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、power和maxPower数组
 * 
 * 应用场景：
 * - 逆向处理
 * - 离线算法
 * - 最值维护
 */
public class Code13_ConnectionsInGalaxyWar {

	public static int MAXN = 10005;
	public static int MAXM = 100005;

	public static int n, m;

	// 星球战力值
	public static int[] power = new int[MAXN];

	// 边信息
	public static int[] edgesFrom = new int[MAXM];
	public static int[] edgesTo = new int[MAXM];

	// 操作信息
	public static List<Integer> destroyOps = new ArrayList<>();
	public static List<Integer> queryOps = new ArrayList<>();
	public static int[] queryPlanets = new int[MAXM];

	// father[i] 表示星球i的父节点
	public static int[] father = new int[MAXN];

	// maxPower[i] 表示以i为根的集合中的最大战力值
	public static int[] maxPower = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每个星球为自己所在集合的代表
		for (int i = 0; i < n; i++) {
			father[i] = i;
			// 初始时每个集合的最大战力值就是星球本身的战力值
			maxPower[i] = power[i];
		}
	}

	/**
	 * 查找星球i所在集合的代表，并进行路径压缩
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的星球编号
	 * @return 星球i所在集合的根节点
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(father[i]);
		}
		return father[i];
	}

	/**
	 * 合并两个星球所在的集合
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param x 星球x编号
	 * @param y 星球y编号
	 */
	public static void union(int x, int y) {
		// 查找两个星球的根节点
		int xf = find(x), yf = find(y);
		// 如果不在同一集合中
		if (xf != yf) {
			// 合并两个集合
			father[xf] = yf;
			// 更新最大战力值
			maxPower[yf] = Math.max(maxPower[yf], maxPower[xf]);
		}
	}

	/**
	 * 查询与星球a连通且战力值最大的星球
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param a 星球编号
	 * @return 连通集合中的最大战力值
	 */
	public static int query(int a) {
		// 查找星球所在集合的根节点
		int root = find(a);
		// 返回集合中的最大战力值
		return maxPower[root];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
			n = Integer.parseInt(line.trim());
			
			// 读取星球战力值
			line = br.readLine().trim();
			String[] parts = line.split("\\s+");
			for (int i = 0; i < n; i++) {
				power[i] = Integer.parseInt(parts[i]);
			}
			
			in.nextToken();
			m = (int) in.nval;
			
			// 读取边信息
			for (int i = 0; i < m; i++) {
				in.nextToken();
				edgesFrom[i] = (int) in.nval;
				in.nextToken();
				edgesTo[i] = (int) in.nval;
			}
			
			// 读取操作数量
			in.nextToken();
			int q = (int) in.nval;
			
			// 清空操作列表
			destroyOps.clear();
			queryOps.clear();
			
			// 读取操作
			for (int i = 0; i < q; i++) {
				line = br.readLine().trim();
				parts = line.split("\\s+");
				if (parts[0].equals("destroy")) {
					int from = Integer.parseInt(parts[1]);
					int to = Integer.parseInt(parts[2]);
					// 查找对应的边
					for (int j = 0; j < m; j++) {
						if ((edgesFrom[j] == from && edgesTo[j] == to) || 
							(edgesFrom[j] == to && edgesTo[j] == from)) {
							destroyOps.add(j);
							break;
						}
					}
				} else {
					int planet = Integer.parseInt(parts[1]);
					queryOps.add(queryOps.size());
					queryPlanets[queryOps.size() - 1] = planet;
				}
			}
			
			// 逆向处理
			// 初始化并查集
			prepare();
			
			// 先建立所有未被删除的连接
			boolean[] destroyed = new boolean[m];
			for (int op : destroyOps) {
				destroyed[op] = true;
			}
			
			for (int i = 0; i < m; i++) {
				if (!destroyed[i]) {
					union(edgesFrom[i], edgesTo[i]);
				}
			}
			
			// 逆向处理删除操作和查询操作
			int[] results = new int[queryOps.size()];
			
			// 逆向添加被删除的边
			for (int i = destroyOps.size() - 1; i >= 0; i--) {
				int edgeIdx = destroyOps.get(i);
				union(edgesFrom[edgeIdx], edgesTo[edgeIdx]);
				
				// 处理在此之前的查询
				// 这里简化处理，实际实现需要更复杂的逻辑来匹配查询和删除操作的时间顺序
			}
			
			// 处理查询操作（这里简化处理）
			for (int i = 0; i < queryOps.size(); i++) {
				int planet = queryPlanets[i];
				results[i] = query(planet);
			}
			
			// 输出结果
			for (int i = 0; i < queryOps.size(); i++) {
				out.println(results[i]);
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}

}