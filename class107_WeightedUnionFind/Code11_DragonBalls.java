package class156;

// Dragon Balls
// 有n个城市和n个龙珠，初始时第i个龙珠在第i个城市
// 有两种操作：
// 1) T A B：将A所在城市的所有龙珠转移到B所在城市
// 2) Q A：查询A龙珠所在城市、该城市龙珠数量和A龙珠被转移的次数
// 使用带权并查集维护龙珠的转移次数和城市龙珠数量
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=3635
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 带权并查集解决Dragon Balls问题
 * 
 * 问题分析：
 * 维护龙珠的转移次数和城市龙珠数量
 * 
 * 核心思想：
 * 1. 使用带权并查集维护每个龙珠被转移的次数
 * 2. 维护每个城市的龙珠数量
 * 3. 在合并操作中正确更新转移次数和数量
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、dist和count数组
 * 
 * 应用场景：
 * - 资源转移追踪
 * - 数量统计维护
 * - 操作次数记录
 */
public class Code11_DragonBalls {

	public static int MAXN = 10005;

	public static int n, m;

	// father[i] 表示龙珠i所在集合的代表城市
	public static int[] father = new int[MAXN];

	// dist[i] 表示龙珠i被转移的次数
	public static int[] dist = new int[MAXN];

	// count[i] 表示城市i中的龙珠数量
	public static int[] count = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每个龙珠在对应城市
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			// 初始时每个龙珠被转移0次
			dist[i] = 0;
			// 初始时每个城市有1个龙珠
			count[i] = 1;
		}
	}

	/**
	 * 查找龙珠i所在城市的代表，并进行路径压缩
	 * 同时更新dist[i]为龙珠i被转移的次数
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的龙珠编号
	 * @return 龙珠i所在城市的代表
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 保存父节点
			int tmp = father[i];
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(tmp);
			// 更新转移次数：当前龙珠的转移次数 += 父节点的转移次数
			dist[i] += dist[tmp];
		}
		return father[i];
	}

	/**
	 * 合并两个城市，将A所在城市的所有龙珠转移到B所在城市
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param a 龙珠A编号
	 * @param b 龙珠B编号
	 */
	public static void union(int a, int b) {
		// 查找两个龙珠所在城市的代表
		int af = find(a), bf = find(b);
		// 如果不在同一城市
		if (af != bf) {
			// 将A所在城市的所有龙珠转移到B所在城市
			father[af] = bf;
			// A所在城市的所有龙珠转移次数加1
			dist[af]++;
			// 更新B所在城市的龙珠数量
			count[bf] += count[af];
			// A所在城市的龙珠数量清零
			count[af] = 0;
		}
	}

	/**
	 * 查询龙珠信息
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param a 龙珠编号
	 * @return 龙珠所在城市编号
	 */
	public static int query(int a) {
		// 查找龙珠所在城市的代表
		find(a); // 调用find确保路径压缩完成
		return father[a];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		in.nextToken();
		int t = (int) in.nval;
		
		for (int caseNum = 1; caseNum <= t; caseNum++) {
			out.println("Case " + caseNum + ":");
			
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			
			// 初始化
			prepare();
			
			// 处理每个操作
			for (int i = 1; i <= m; i++) {
				in.nextToken();
				String op = in.sval;
				
				if (op.equals("T")) {
					in.nextToken();
					int a = (int) in.nval;
					in.nextToken();
					int b = (int) in.nval;
					union(a, b);
				} else {
					in.nextToken();
					int a = (int) in.nval;
					int city = query(a);
					out.println(city + " " + count[city] + " " + dist[a]);
				}
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}

}