package class112;

// 开关问题 - 线段树实现
// 题目来源：洛谷 P3870 https://www.luogu.com.cn/problem/P3870
// 
// 题目描述：
// 现有n盏灯排成一排，从左到右依次编号为1~n，一开始所有的灯都是关着的
// 操作分两种：
// 操作 0 l r : 改变l~r范围上所有灯的状态，开着的灯关上、关着的灯打开
// 操作 1 l r : 查询l~r范围上有多少灯是打开的
//
// 解题思路：
// 使用线段树配合懒惰传播来高效处理区间翻转和区间求和操作
// 1. 线段树节点维护区间内开着的灯的数量
// 2. 使用reverse数组作为懒惰标记，记录区间是否需要翻转
// 3. 翻转操作时，区间内开着的灯数量变为区间长度减去原来的数量
//
// 核心思想：
// 1. 线段树是一种二叉树结构，每个节点代表数组的一个区间
// 2. 懒惰传播用于延迟更新，只有在需要时才将更新操作传递给子节点
// 3. 翻转操作的实现：当一个区间需要翻转时，开着的灯数量变为区间长度减去原来的数量
//
// 时间复杂度分析：
// - 建树：O(n)
// - 区间翻转：O(log n)
// - 区间查询：O(log n)
// 空间复杂度：O(n)
//
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_Switch {

	// 最大节点数，设置为100001*4以确保足够的空间
	public static int MAXN = 100001;

	// light[i] 表示线段树节点i维护的区间内开着的灯的数量
	public static int[] light = new int[MAXN << 2];

	// reverse[i] 表示线段树节点i是否需要翻转的懒惰标记
	public static boolean[] reverse = new boolean[MAXN << 2];

	/**
	 * 向上更新函数
	 * 更新当前节点的值为左右子节点值的和
	 * 在线段树中，父节点的值通常由子节点的值计算得出
	 * 对于本问题，父节点维护的区间内开着的灯数量等于左右子节点维护区间内开着的灯数量之和
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void up(int i) {
		light[i] = light[i << 1] + light[i << 1 | 1];
	}

	/**
	 * 向下传递懒惰标记
	 * 将当前节点的懒惰标记传递给左右子节点
	 * 懒惰传播是线段树优化的重要技术，用于延迟更新操作
	 * 只有在真正需要访问子节点时才将更新操作传递下去，避免不必要的计算
	 * @param i 当前节点在线段树数组中的索引
	 * @param ln 左子树节点数量
	 * @param rn 右子树节点数量
	 */
	public static void down(int i, int ln, int rn) {
		if (reverse[i]) {
			// 将翻转标记传递给左右子节点
			lazy(i << 1, ln);
			lazy(i << 1 | 1, rn);
			// 清除当前节点的翻转标记
			reverse[i] = false;
		}
	}

	/**
	 * 懒惰标记处理函数
	 * 对节点i进行翻转操作，开着的灯数量变为区间长度减去原来的数量
	 * 这是区间翻转操作的核心实现
	 * 翻转后：开着的灯数量 = 区间总长度 - 原来开着的灯数量
	 * 同时更新懒惰标记的状态
	 * @param i 要翻转的节点在线段树数组中的索引
	 * @param n 该节点维护的区间长度
	 */
	public static void lazy(int i, int n) {
		light[i] = n - light[i];
		reverse[i] = !reverse[i];
	}

	/**
	 * 构建线段树
	 * 采用递归方式构建线段树，每个节点维护一个区间的信息
	 * 叶子节点对应数组中的单个元素，非叶子节点对应区间的合并结果
	 * @param l 区间左边界
	 * @param r 区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			// 叶子节点，初始所有灯都是关闭的
			light[i] = 0;
		} else {
			int mid = (l + r) >> 1;
			// 递归构建左子树
			build(l, mid, i << 1);
			// 递归构建右子树
			build(mid + 1, r, i << 1 | 1);
			// 向上更新当前节点的值
			up(i);
		}
		// 初始化懒惰标记为false
		reverse[i] = false;
	}

	/**
	 * 区间翻转操作
	 * 将区间[jobl, jobr]内的所有灯状态翻转
	 * 利用懒惰传播优化，避免对每个元素逐一翻转
	 * @param jobl 操作区间左边界
	 * @param jobr 操作区间右边界
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void reverse(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在操作区间内，直接打懒惰标记
			// 这是懒惰传播的关键：只标记不立即执行
			lazy(i, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			// 向下传递懒惰标记
			down(i, mid - l + 1, r - mid);
			// 递归处理左子树
			if (jobl <= mid) {
				reverse(jobl, jobr, l, mid, i << 1);
			}
			// 递归处理右子树
			if (jobr > mid) {
				reverse(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			// 向上更新当前节点的值
			up(i);
		}
	}

	/**
	 * 区间查询操作
	 * 查询区间[jobl, jobr]内开着的灯的数量
	 * 在查询过程中需要确保懒惰标记已经正确传递
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 * @return 区间内开着的灯的数量
	 */
	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在查询区间内，直接返回节点值
			return light[i];
		}
		int mid = (l + r) >> 1;
		// 向下传递懒惰标记
		// 在查询时必须确保懒惰标记已经传递，以保证结果正确
		down(i, mid - l + 1, r - mid);
		int ans = 0;
		// 递归查询左子树
		if (jobl <= mid) {
			ans += query(jobl, jobr, l, mid, i << 1);
		}
		// 递归查询右子树
		if (jobr > mid) {
			ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		// 使用高效的IO处理方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入参数
		in.nextToken();
		int n = (int) in.nval;  // 灯的数量
		in.nextToken();
		int m = (int) in.nval;  // 操作的数量
		
		// 构建线段树
		// 初始时所有灯都是关闭的，所以每个节点的值都为0
		build(1, n, 1);
		
		// 处理m个操作
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;     // 操作类型：0表示翻转，1表示查询
			in.nextToken();
			jobl = (int) in.nval;   // 操作区间左边界
			in.nextToken();
			jobr = (int) in.nval;   // 操作区间右边界
			
			if (op == 0) {
				// 翻转操作：改变[jobl, jobr]范围内所有灯的状态
				// 利用线段树和懒惰传播高效处理区间翻转
				reverse(jobl, jobr, 1, n, 1);
			} else {
				// 查询操作：查询[jobl, jobr]范围内开着的灯的数量
				// 利用线段树高效处理区间查询
				out.println(query(jobl, jobr, 1, n, 1));
			}
		}
		
		// 刷新输出缓冲区并关闭资源
		out.flush();
		out.close();
		br.close();
	}

}