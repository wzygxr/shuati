package class158;

/**
 * 区间内没有出现的最小自然数，java版
 * 
 * 题目来源：洛谷 P4137 - Mex
 * 题目链接：https://www.luogu.com.cn/problem/P4137
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询
 * 每条查询 l r : 打印arr[l..r]内没有出现过的最小自然数，注意0是自然数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决区间Mex问题。
 * 1. 维护每个数字在数组中出现的最晚位置
 * 2. 对于查询区间[l,r]，找到在该区间内没有出现的最小自然数
 * 3. 利用线段树维护数字范围中每个数字出现的最晚位置中的最左位置
 * 
 * 强制在线处理:
 * 请用在线算法解决该问题，因为可以设计强制在线的要求，让离线算法失效
 * 
 * 时间复杂度: O(n log n + m log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 3
 * 0 1 2 3 4
 * 1 3
 * 2 4
 * 1 5
 * 
 * 输出:
 * 3
 * 0
 * 5
 * 
 * 解释:
 * 查询1 3：[0,1,2]中没有出现的最小自然数是3
 * 查询2 4：[1,2,3]中没有出现的最小自然数是0
 * 查询1 5：[0,1,2,3,4]中没有出现的最小自然数是5
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_MissingSmallest1 {

	public static int MAXN = 200001;

	public static int MAXT = MAXN * 22;

	public static int n, m;

	public static int[] arr = new int[MAXN];

	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	// 数字范围中，每个数字出现的最晚位置中，最左的位置在哪
	public static int[] lateLeft = new int[MAXT];

	public static int cnt;

	/**
	 * 构建空线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 根节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cnt;
		lateLeft[rt] = 0;
		if (l < r) {
			int mid = (l + r) / 2;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 更新线段树节点
	 * @param jobi 要更新的数字
	 * @param jobv 该数字最晚出现的位置
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 前一个版本的节点编号
	 * @return 新版本的根节点编号
	 */
	public static int update(int jobi, int jobv, int l, int r, int i) {
		int rt = ++cnt;
		left[rt] = left[i];
		right[rt] = right[i];
		lateLeft[rt] = lateLeft[i];
		if (l == r) {
			lateLeft[rt] = jobv;
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				left[rt] = update(jobi, jobv, l, mid, left[rt]);
			} else {
				right[rt] = update(jobi, jobv, mid + 1, r, right[rt]);
			}
			lateLeft[rt] = Math.min(lateLeft[left[rt]], lateLeft[right[rt]]);
		}
		return rt;
	}

	/**
	 * 查询区间[l,r]内没有出现的最小自然数
	 * @param pos 查询区间左端点
	 * @param l 当前数字范围左端点
	 * @param r 当前数字范围右端点
	 * @param i 当前节点编号
	 * @return 没有出现的最小自然数
	 */
	public static int query(int pos, int l, int r, int i) {
		if (l == r) {
			return l;
		}
		int mid = (l + r) / 2;
		if (lateLeft[left[i]] < pos) {
			// l...mid范围上，每个数字最晚出现的位置中
			// 最左的位置如果在pos以左，说明l...mid范围上，一定有缺失的数字
			return query(pos, l, mid, left[i]);
		} else {
			// 缺失的数字一定在mid+1....r范围
			// 因为l...r一定有缺失的数字才会来到这个范围的
			// 如果左侧不缺失，那缺失的数字一定在右侧范围上
			return query(pos, mid + 1, r, right[i]);
		}
	}

	/**
	 * 预处理，建立主席树
	 */
	public static void prepare() {
		cnt = 0;
		root[0] = build(0, n);
		for (int i = 1; i <= n; i++) {
			if (arr[i] > n || arr[i] < 0) {
				// 如果数字超出范围，则直接复制前一个版本
				root[i] = root[i - 1];
			} else {
				// 更新数字arr[i]的最晚出现位置为i
				root[i] = update(arr[i], i, 0, n, root[i - 1]);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		prepare();
		for (int i = 1, l, r; i <= m; i++) {
			in.nextToken();
			l = (int) in.nval;
			in.nextToken();
			r = (int) in.nval;
			// 查询区间[l,r]内没有出现的最小自然数
			out.println(query(l, 0, n, root[r]));
		}
		out.flush();
		out.close();
		br.close();
	}

}