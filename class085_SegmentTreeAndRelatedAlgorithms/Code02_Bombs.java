package class112;

// 贪婪大陆 - 线段树实现
// 题目来源：洛谷 P2184 https://www.luogu.com.cn/problem/P2184
// 
// 题目描述：
// 一共有n个格子，编号1~n，开始时格子上没有地雷，实现两种操作，一共调用m次
// 操作 1 l r : 在l~r范围的格子上放置一种新型地雷，每次地雷都是新款
// 操作 2 l r : 查询l~r范围的格子上一共放置过多少款不同的地雷
// 
// 解题思路：
// 使用两个线段树分别维护地雷范围的起始点和终止点
// 1. 当在[l,r]范围放置地雷时，在起始点线段树的l位置+1，在终止点线段树的r位置+1
// 2. 查询[l,r]范围内不同地雷数量时，计算起始点在[1,r]范围内的地雷数量减去终止点在[1,l-1]范围内的地雷数量
// 
// 核心思想：
// 对于任意一个查询区间[l,r]，落在这个区间内的地雷种类数等于：
// 起始点在[1,r]范围内的地雷数量 - 终止点在[1,l-1]范围内的地雷数量
// 
// 为什么这个方法是正确的？
// 1. 每个地雷都有唯一的起始点和终止点
// 2. 如果一个地雷的起始点在[1,r]范围内，说明这个地雷可能影响到查询区间
// 3. 但如果这个地雷的终止点在[1,l-1]范围内，说明这个地雷完全在查询区间左侧，不会影响查询区间
// 4. 因此，落在查询区间内的地雷种类数 = 可能影响查询区间的地雷数 - 完全在查询区间左侧的地雷数
// 
// 时间复杂度分析：
// - 建树：O(n)
// - 区间更新：O(log n)
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

public class Code02_Bombs {

	// 最大节点数，设置为100001*4以确保足够的空间
	public static int MAXN = 100001;

	// bombStarts[i] 表示线段树节点i维护的区间内新增地雷范围起始点的数量
	public static int[] bombStarts = new int[MAXN << 2];

	// bombEnds[i] 表示线段树节点i维护的区间内新增地雷范围终止点的数量
	public static int[] bombEnds = new int[MAXN << 2];

	/**
	 * 向上更新函数
	 * 更新当前节点的值为左右子节点值的和
	 * 由于我们需要同时维护起始点和终止点两个信息，所以需要分别更新
	 * @param i 当前节点在线段树数组中的索引
	 */
	public static void up(int i) {
		// 更新起始点计数：当前节点的起始点数量等于左右子节点起始点数量之和
		bombStarts[i] = bombStarts[i << 1] + bombStarts[i << 1 | 1];
		// 更新终止点计数：当前节点的终止点数量等于左右子节点终止点数量之和
		bombEnds[i] = bombEnds[i << 1] + bombEnds[i << 1 | 1];
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
		if (l < r) {
			int mid = (l + r) >> 1;
			// 递归构建左子树
			build(l, mid, i << 1);
			// 递归构建右子树
			build(mid + 1, r, i << 1 | 1);
		}
		// 初始化起始点和终止点计数为0
		// 初始时没有任何地雷放置，所以起始点和终止点计数都为0
		bombStarts[i] = 0;
		bombEnds[i] = 0;
	}

	/**
	 * 添加地雷范围的起始点或终止点
	 * 这是单点更新操作，用于记录地雷范围的边界信息
	 * @param jobt 操作类型：0表示添加起始点，1表示添加终止点
	 * @param jobi 操作位置
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 */
	// jobt==0表示在添加地雷范围的开头，jobi就是地雷范围的开头位置
	// jobt==1表示在添加地雷范围的结尾，jobi就是地雷范围的结尾位置
	public static void add(int jobt, int jobi, int l, int r, int i) {
		if (l == r) {
			// 到达叶子节点，更新对应计数
			// 叶子节点对应数组中的一个具体位置
			if (jobt == 0) {
				// 添加起始点：在起始点线段树的对应位置计数加1
				bombStarts[i]++;
			} else {
				// 添加终止点：在终止点线段树的对应位置计数加1
				bombEnds[i]++;
			}
		} else {
			int mid = (l + r) >> 1;
			// 根据位置递归更新左子树或右子树
			// 如果操作位置在左半区间，则更新左子树
			if (jobi <= mid) {
				add(jobt, jobi, l, mid, i << 1);
			} else {
				// 如果操作位置在右半区间，则更新右子树
				add(jobt, jobi, mid + 1, r, i << 1 | 1);
			}
			// 向上更新当前节点的值
			// 将子节点的更新结果合并到当前节点
			up(i);
		}
	}

	/**
	 * 查询区间内起始点或终止点的数量
	 * 这是区间查询操作，用于统计指定区间内的地雷边界数量
	 * @param jobt 查询类型：0表示查询起始点，1表示查询终止点
	 * @param jobl 查询区间左边界
	 * @param jobr 查询区间右边界
	 * @param l 当前节点维护的区间左边界
	 * @param r 当前节点维护的区间右边界
	 * @param i 当前节点在线段树数组中的索引
	 * @return 区间内起始点或终止点的数量
	 */
	// jobt==0表示在查询[jobl ~ jobr]范围上有多少地雷范围的开头
	// jobt==1表示在查询[jobl ~ jobr]范围上有多少地雷范围的结尾
	public static int query(int jobt, int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前节点维护的区间完全包含在查询区间内，直接返回节点值
			// 这是线段树查询的优化点：如果当前区间完全在查询区间内，直接返回结果
			return jobt == 0 ? bombStarts[i] : bombEnds[i];
		} else {
			int mid = (l + r) >> 1;
			int ans = 0;
			// 递归查询左子树
			// 只有当查询区间与左子树区间有交集时才继续查询
			if (jobl <= mid) {
				ans += query(jobt, jobl, jobr, l, mid, i << 1);
			}
			// 递归查询右子树
			// 只有当查询区间与右子树区间有交集时才继续查询
			if (jobr > mid) {
				ans += query(jobt, jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			return ans;
		}
	}

	public static void main(String[] args) throws IOException {
		// 使用高效的IO处理方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取输入参数
		in.nextToken();
		int n = (int) in.nval;  // 格子数量
		in.nextToken();
		int m = (int) in.nval;  // 操作数量
		
		// 构建线段树
		// 初始时没有任何地雷放置，所以起始点和终止点计数都为0
		build(1, n, 1);
		
		// 处理m个操作
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;     // 操作类型：1表示添加地雷，2表示查询
			in.nextToken();
			jobl = (int) in.nval;   // 操作区间左边界
			in.nextToken();
			jobr = (int) in.nval;   // 操作区间右边界
			
			if (op == 1) {
				// 添加地雷操作：在起始点和终止点分别+1
				// 每次放置地雷都是新款，所以需要记录其起始点和终止点
				add(0, jobl, 1, n, 1);  // 在起始点线段树的jobl位置+1
				add(1, jobr, 1, n, 1);  // 在终止点线段树的jobr位置+1
			} else {
				// 查询操作：计算区间内不同地雷种类数
				// 根据核心思想：落在查询区间内的地雷种类数 = 
				// 起始点在[1,jobr]范围内的地雷数量 - 终止点在[1,jobl-1]范围内的地雷数量
				
				// 起始点在[1,jobr]范围内的地雷数量
				// 这些地雷可能影响到查询区间[1,jobr]
				int s = query(0, 1, jobr, 1, n, 1);
				
				// 终止点在[1,jobl-1]范围内的地雷数量
				// 这些地雷完全在查询区间左侧，不会影响查询区间[jobl,jobr]
				// 如果jobl==1，说明没有更左的区域，返回0
				// 如果jobl>1，说明有更左的区域，查询终止点数量
				int e = jobl == 1 ? 0 : query(1, 1, jobl - 1, 1, n, 1);
				
				// 不同地雷种类数 = 起始点数量 - 终止点数量
				// 这就是落在查询区间[jobl,jobr]内的地雷种类数
				out.println(s - e);
			}
		}
		
		// 刷新输出缓冲区并关闭资源
		out.flush();
		out.close();
		br.close();
	}

}