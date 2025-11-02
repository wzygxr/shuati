package class137;

/**
 * P哥的桶问题 - 线段树 + 线性基算法应用
 * 
 * 题目来源：洛谷 P4839 P哥的桶
 * 题目链接：https://www.luogu.com.cn/problem/P4839
 * 
 * 题目描述：
 * 一共有n个桶，排成一排，编号1~n，每个桶可以装下任意个数字。
 * 需要高效实现以下两个操作：
 * 操作 1 k v : 把数字v放入k号桶中
 * 操作 2 l r : 可以从l..r号桶中随意拿数字，返回异或和最大的结果
 * 
 * 约束条件：
 * 1 <= n、m <= 5 * 10^4
 * 0 <= v <= 2^31 - 1
 * 
 * 算法思路：
 * 1. 使用线段树维护区间线性基，每个线段树节点存储对应区间的线性基
 * 2. 插入操作：在线段树对应位置插入数字，并更新路径上的线性基
 * 3. 查询操作：合并查询区间内所有线段树节点的线性基，然后求最大异或和
 * 
 * 时间复杂度：
 * - 插入操作：O(log n * BIT)，其中BIT=31（32位整数）
 * - 查询操作：O(log n * BIT^2)
 * 总时间复杂度：O(m * log n * BIT^2)
 * 
 * 空间复杂度：O(n * BIT)
 * 
 * 工程化考量：
 * 1. 使用线段树优化区间查询效率
 * 2. 线性基合并时注意避免重复计算
 * 3. 处理大规模数据时的内存优化
 * 
 * 与机器学习联系：
 * 该问题类似于在线学习中的特征组合优化，
 * 在推荐系统中用于实时更新用户特征组合。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_BucketMaximumXor {

	// 最大桶数量
	public static int MAXN = 50001;
	// 二进制位数（32位整数，最高位是第30位）
	public static int BIT = 30;
	// 线段树节点存储的线性基，每个节点维护对应区间的线性基
	public static int[][] treeBasis = new int[MAXN << 2][BIT + 1];
	// 临时线性基，用于合并查询结果
	public static int[] basis = new int[BIT + 1];

	/**
	 * 向线段树中添加数字
	 * @param jobi 桶编号
	 * @param jobv 要添加的数字
	 * @param l 当前线段树节点左边界
	 * @param r 当前线段树节点右边界
	 * @param i 当前线段树节点索引
	 * 
	 * 算法步骤：
	 * 1. 在当前节点插入数字到线性基
	 * 2. 如果当前节点不是叶子节点，递归到子节点
	 * 3. 线段树路径上的所有节点都会更新线性基
	 */
	public static void add(int jobi, int jobv, int l, int r, int i) {
		// 在当前节点插入数字
		insert(treeBasis[i], jobv);
		if (l < r) {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				// 递归到左子树
				add(jobi, jobv, l, mid, i << 1);
			} else {
				// 递归到右子树
				add(jobi, jobv, mid + 1, r, i << 1 | 1);
			}
		}
	}

	/**
	 * 向线性基中插入数字
	 * @param basis 线性基数组
	 * @param num 要插入的数字
	 * @return 是否插入成功（是否线性无关）
	 * 
	 * 算法原理：
	 * 1. 从最高位开始扫描
	 * 2. 如果当前位为1，检查该位是否已有基向量
	 * 3. 如果没有，直接插入作为基向量
	 * 4. 如果有，进行异或消元操作
	 */
	public static boolean insert(int[] basis, int num) {
		for (int i = BIT; i >= 0; i--) {
			if ((num >> i) == 1) {
				if (basis[i] == 0) {
					basis[i] = num;
					return true;
				}
				num ^= basis[i];
			}
		}
		return false;
	}

	/**
	 * 查询区间最大异或和
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param m 桶的总数
	 * @return 区间内数字的最大异或和
	 * 
	 * 算法步骤：
	 * 1. 清空临时线性基
	 * 2. 合并查询区间内所有线段树节点的线性基
	 * 3. 从合并后的线性基中计算最大异或和
	 */
	public static int query(int jobl, int jobr, int m) {
		// 清空临时线性基
		Arrays.fill(basis, 0);
		// 合并查询区间内的线性基
		merge(jobl, jobr, 1, m, 1);
		// 计算最大异或和
		int ans = 0;
		for (int j = BIT; j >= 0; j--) {
			ans = Math.max(ans, ans ^ basis[j]);
		}
		return ans;
	}

	/**
	 * 合并查询区间内的线性基
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l 当前线段树节点左边界
	 * @param r 当前线段树节点右边界
	 * @param i 当前线段树节点索引
	 * 
	 * 算法原理：
	 * 1. 如果当前节点完全包含在查询区间内，直接合并其线性基
	 * 2. 否则递归处理左右子树
	 * 3. 将区间内所有线性基合并到临时线性基中
	 */
	public static void merge(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点完全包含在查询区间内，合并其线性基
			for (int j = BIT; j >= 0; j--) {
				if (treeBasis[i][j] != 0) {
					insert(basis, treeBasis[i][j]);
				}
			}
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				// 递归处理左子树
				merge(jobl, jobr, l, mid, i << 1);
			}
			if (jobr > mid) {
				// 递归处理右子树
				merge(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
		}
	}

	/**
	 * 主函数：处理输入输出
	 * 异常处理：
	 * 1. IO异常处理
	 * 2. 输入格式验证
	 * 3. 边界条件检查
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取操作数量n和桶数量m
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 处理每个操作
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			int op = (int) in.nval;
			if (op == 1) {
				// 插入操作：向指定桶添加数字
				in.nextToken();
				int jobi = (int) in.nval;
				in.nextToken();
				int jobv = (int) in.nval;
				add(jobi, jobv, 1, m, 1);
			} else {
				// 查询操作：查询区间最大异或和
				in.nextToken();
				int jobl = (int) in.nval;
				in.nextToken();
				int jobr = (int) in.nval;
				out.println(query(jobl, jobr, m));
			}
		}
		
		out.flush();
		out.close();
		br.close();
	}

}
