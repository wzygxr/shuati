package class156;

import java.util.HashMap;
import java.util.List;

// 除法求值
// 所有变量都用字符串表示，并且给定若干组等式
// 比如等式
// ["ab", "ef"] = 8，代表ab / ef = 8
// ["ct", "ef"] = 2，代表ct / ef = 2
// 所有等式都是正确的并且可以进行推断，给定所有等式之后，会给你若干条查询
// 比如查询，["ab", "ct"]，根据上面的等式推断，ab / ct = 4
// 如果某条查询中的变量，从来没在等式中出现过，认为答案是-1.0
// 如果某条查询的答案根本推断不出来，认为答案是-1.0
// 返回所有查询的答案
// 测试链接 : https://leetcode.cn/problems/evaluate-division/

/**
 * 带权并查集解决变量除法求值问题
 * 
 * 问题分析：
 * 给定一些变量之间的除法等式关系，查询其他变量之间的除法结果
 * 
 * 核心思想：
 * 1. 将变量之间的除法关系转化为图上的权重关系
 * 2. 如果 a/b = v，则在图中添加边 a->b 权重为v，b->a 权重为1/v
 * 3. 使用带权并查集维护变量之间的倍数关系
 * 4. dist[x] 表示变量x是其根节点代表变量的多少倍
 * 
 * 时间复杂度分析：
 * - prepare: O(e) e为等式数量
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O(e * α(n) + q * α(n)) q为查询数量
 * 
 * 空间复杂度: O(n) n为不同变量的数量
 * 
 * 应用场景：
 * - 变量关系推导
 * - 单位换算
 * - 比例计算
 */
public class Code05_EvaluateDivision {

	/**
	 * 计算所有查询的答案
	 * 
	 * @param equations 等式列表，每个等式包含两个变量
	 * @param values    等式对应的值
	 * @param queries   查询列表
	 * @return 所有查询的答案
	 */
	public static double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
		// 初始化并查集
		prepare(equations);
		// 处理所有等式，建立变量间的关系
		for (int i = 0; i < values.length; i++) {
			// 建立变量之间的倍数关系
			union(equations.get(i).get(0), equations.get(i).get(1), values[i]);
		}
		// 处理所有查询
		double[] ans = new double[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			ans[i] = query(queries.get(i).get(0), queries.get(i).get(1));
		}
		return ans;
	}

	// father[x] 表示变量x的父节点
	public static HashMap<String, String> father = new HashMap<>();

	// dist[x] 表示变量x是其根节点代表变量的多少倍
	public static HashMap<String, Double> dist = new HashMap<>();

	/**
	 * 初始化并查集
	 * 时间复杂度: O(e) e为等式数量
	 * 空间复杂度: O(n) n为不同变量的数量
	 * 
	 * @param equations 等式列表
	 */
	public static void prepare(List<List<String>> equations) {
		// 清空之前的数据
		father.clear();
		dist.clear();
		// 初始化所有出现的变量
		for (List<String> list : equations) {
			for (String key : list) {
				// 每个变量初始时是自己的根节点
				father.put(key, key);
				// 每个变量初始时是自己根节点的1倍
				dist.put(key, 1.0);
			}
		}
	}

	/**
	 * 查找变量x的根节点，并进行路径压缩
	 * 同时更新dist[x]为变量x是其根节点代表变量的多少倍
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param x 要查找的变量
	 * @return 变量x所在集合的根节点
	 */
	public static String find(String x) {
		// 如果变量不存在，返回null
		if (!father.containsKey(x)) {
			return null;
		}
		String tmp, fa = x;
		// 如果不是根节点
		if (!x.equals(father.get(x))) {
			// 保存父节点
			tmp = father.get(x);
			// 递归查找根节点，同时进行路径压缩
			fa = find(tmp);
			// 更新倍数关系：当前变量是根节点的倍数 = 当前变量是父节点的倍数 * 父节点是根节点的倍数
			dist.put(x, dist.get(x) * dist.get(tmp));
			// 路径压缩
			father.put(x, fa);
		}
		return fa;
	}

	/**
	 * 合并两个变量所在的集合，建立倍数关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧变量
	 * @param r 右侧变量
	 * @param v 倍数关系 l/r = v
	 */
	public static void union(String l, String r, double v) {
		// 查找两个变量的根节点
		String lf = find(l), rf = find(r);
		// 如果不在同一集合中
		if (!lf.equals(rf)) {
			// 合并两个集合
			father.put(lf, rf);
			// 更新倍数关系：
			// l = v * r
			// l = dist[l] * lf, r = dist[r] * rf
			// 所以 dist[l] * lf = v * dist[r] * rf
			// 即 lf = (v * dist[r] / dist[l]) * rf
			// 因此 dist[lf] = v * dist[r] / dist[l]
			dist.put(lf, dist.get(r) / dist.get(l) * v);
		}
	}

	/**
	 * 查询两个变量之间的倍数关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧变量
	 * @param r 右侧变量
	 * @return l/r的结果，如果无法确定返回-1.0
	 */
	public static double query(String l, String r) {
		// 查找两个变量的根节点
		String lf = find(l), rf = find(r);
		// 如果任一变量不存在或不在同一集合中，无法确定关系
		if (lf == null || rf == null || !lf.equals(rf)) {
			return -1.0;
		}
		// l/r = (dist[l] * lf) / (dist[r] * rf) = dist[l] / dist[r] (因为lf == rf)
		return dist.get(l) / dist.get(r);
	}

}