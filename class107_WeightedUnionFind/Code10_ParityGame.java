package class156;

// Parity game
// 给定一个01序列，每次询问一个区间内1的个数是奇数还是偶数
// 要求找出第一个错误的回答
// 使用带权并查集+离散化解决
// 测试链接 : http://poj.org/problem?id=1733
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Map;

/**
 * 带权并查集解决Parity game问题
 * 
 * 问题分析：
 * 给定一个01序列，每次询问一个区间内1的个数是奇数还是偶数，找出第一个错误的回答
 * 
 * 核心思想：
 * 1. 将区间[l,r]的奇偶性转化为前缀和sum[r]和sum[l-1]的奇偶性关系
 * 2. 使用带权并查集维护每个点到根节点的奇偶关系
 * 3. dist[i] = 0表示i与根节点同奇偶，dist[i] = 1表示i与根节点不同奇偶
 * 4. 离散化处理大数据范围
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 区间奇偶性判断
 * - 逻辑一致性验证
 * - 离散化处理大数据范围
 */
public class Code10_ParityGame {

	public static int MAXN = 1000005;

	public static int n, m;

	// 离散化映射
	public static Map<Integer, Integer> map = new HashMap<>();

	// father[i] 表示节点i的父节点
	public static int[] father = new int[MAXN];

	// dist[i] 表示节点i与根节点的奇偶关系
	// dist[i] = 0表示i与根节点同奇偶
	// dist[i] = 1表示i与根节点不同奇偶
	public static int[] dist = new int[MAXN];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每个节点为自己所在集合的代表
		for (int i = 0; i <= n; i++) {
			father[i] = i;
			// 初始时每个节点与根节点同奇偶
			dist[i] = 0;
		}
	}

	/**
	 * 查找节点i所在集合的代表，并进行路径压缩
	 * 同时更新dist[i]为节点i与根节点的奇偶关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的根节点
	 */
	public static int find(int i) {
		// 如果不是根节点
		if (i != father[i]) {
			// 保存父节点
			int tmp = father[i];
			// 递归查找根节点，同时进行路径压缩
			father[i] = find(tmp);
			// 更新奇偶关系：当前节点与根节点的关系 = 当前节点与父节点的关系 ^ 父节点与根节点的关系
			dist[i] ^= dist[tmp];
		}
		return father[i];
	}

	/**
	 * 合并两个集合，建立关系
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左边界
	 * @param r 右边界
	 * @param v 奇偶性：0表示偶数，1表示奇数
	 * @return 如果合并成功返回true，如果发现矛盾返回false
	 */
	public static boolean union(int l, int r, int v) {
		// 查找两个节点的根节点
		int lf = find(l), rf = find(r);
		// 如果在同一集合中
		if (lf == rf) {
			// 检查是否与已有关系矛盾
			// l和r的奇偶关系应该等于v
			// l与根节点的关系 ^ r与根节点的关系 = l与r的关系
			if ((dist[l] ^ dist[r]) != v) {
				// 发现矛盾
				return false;
			}
		} else {
			// 合并两个集合
			father[lf] = rf;
			// 更新奇偶关系：
			// l与r的关系 = v
			// l与根节点lf的关系 = dist[l], r与根节点rf的关系 = dist[r]
			// 根节点lf与根节点rf的关系 = dist[l] ^ dist[r] ^ v
			dist[lf] = dist[l] ^ dist[r] ^ v;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取序列长度（虽然题目给了但实际不需要用到）
		in.nextToken();
		int len = (int) in.nval;
		
		// 读取询问数量
		in.nextToken();
		m = (int) in.nval;
		
		// 收集所有需要离散化的坐标
		int[] ls = new int[m];
		int[] rs = new int[m];
		String[] parity = new String[m];
		
		for (int i = 0; i < m; i++) {
			in.nextToken();
			ls[i] = (int) in.nval;
			in.nextToken();
			rs[i] = (int) in.nval;
			in.nextToken();
			parity[i] = in.sval;
		}
		
		// 离散化
		map.clear();
		int index = 0;
		for (int i = 0; i < m; i++) {
			// 将l-1和r加入离散化
			if (!map.containsKey(ls[i] - 1)) {
				map.put(ls[i] - 1, index++);
			}
			if (!map.containsKey(rs[i])) {
				map.put(rs[i], index++);
			}
		}
		n = index;
		
		// 初始化并查集
		prepare();
		
		// 处理每个询问
		for (int i = 0; i < m; i++) {
			int l = map.get(ls[i] - 1);
			int r = map.get(rs[i]);
			int v = parity[i].equals("even") ? 0 : 1;
			
			// 尝试合并
			if (!union(l, r, v)) {
				// 发现矛盾，输出答案
				out.println(i);
				out.flush();
				out.close();
				br.close();
				return;
			}
		}
		
		// 没有发现矛盾
		out.println(m);
		out.flush();
		out.close();
		br.close();
	}

}