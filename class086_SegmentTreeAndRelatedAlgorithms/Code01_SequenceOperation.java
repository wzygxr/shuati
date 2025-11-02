package class113;

/**
 * 线段树经典应用：多标记序列操作
 * 题目来源：洛谷 P2572 [SDOI2010] 序列操作
 * 题目链接：https://www.luogu.com.cn/problem/P2572
 * 
 * 核心算法：线段树 + 多重懒标记
 * 难度：省选/NOI-
 * 
 * 【题目详细描述】
 * 给定一个长度为n的01序列，支持5种操作：
 * 1. 操作 0 l r：将区间[l,r]全部置为0
 * 2. 操作 1 l r：将区间[l,r]全部置为1
 * 3. 操作 2 l r：将区间[l,r]全部取反
 * 4. 操作 3 l r：查询区间[l,r]中1的个数
 * 5. 操作 4 l r：查询区间[l,r]中连续1的最长长度
 * 
 * 【解题思路】
 * 这是一个典型的线段树应用题，需要维护多种区间信息并处理多重懒标记。
 * 线段树节点需要保存丰富的信息来支持连续子串长度的查询。
 * 
 * 【核心数据结构设计】
 * 线段树每个节点维护以下信息：
 * - sum[i]：区间内1的总数
 * - len0[i]/len1[i]：区间内连续0/1的最长子串长度
 * - pre0[i]/pre1[i]：区间内连续0/1的最长前缀长度
 * - suf0[i]/suf1[i]：区间内连续0/1的最长后缀长度
 * 
 * 懒标记设计：
 * - change[i]：记录区间被置为的具体值（0或1）
 * - update[i]：标记区间是否有待处理的赋值操作
 * - reverse[i]：标记区间是否有待处理的翻转操作
 * 
 * 【关键技术点】
 * 1. 懒标记优先级处理：更新操作(update)优先于翻转操作(reverse)
 * 2. 区间合并逻辑：需要考虑左右子区间连接处的情况
 * 3. 多重懒标记的下传顺序和相互影响处理
 * 4. 边界条件处理：如区间为空、单元素区间等特殊情况
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 建树：O(n)
 *   - 单次操作（更新/查询）：O(log n)
 *   - m次操作总时间复杂度：O((n + m) log n)
 * - 空间复杂度：O(4n)，线段树标准空间配置
 * 
 * 【算法优化点】
 * 1. 懒标记延迟下传：避免不必要的更新操作
 * 2. 区间合并时的高效计算：通过维护前缀和后缀信息加速
 * 3. 使用位运算优化：如移位操作代替乘除法
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用BufferedReader和PrintWriter优化IO
 * 2. 数组大小预设：根据题目约束设置合理的MAXN
 * 3. 内存管理：线段树数组使用静态分配避免动态分配的开销
 * 4. 代码模块化：将up、down、build等核心函数分离
 * 
 * 【异常处理】
 * 1. 输入范围检查：确保操作区间在有效范围内
 * 2. 数据类型溢出：使用适当的数据类型避免整数溢出
 * 3. 懒标记一致性：确保懒标记的正确下传和合并
 * 
 * 【类似题目推荐】
 * 1. LeetCode 307. 区域和检索 - 数组可修改：https://leetcode.cn/problems/range-sum-query-mutable/
 * 2. Codeforces 242E - XOR on Segment：https://codeforces.com/problemset/problem/242/E
 * 3. 洛谷 P3373 【模板】线段树 2：https://www.luogu.com.cn/problem/P3373
 * 4. POJ 3468 A Simple Problem with Integers：http://poj.org/problem?id=3468
 * 5. HDU 1698 Just a Hook：http://acm.hdu.edu.cn/showproblem.php?pid=1698
 * 6. SPOJ GSS1 - Can you answer these queries I：https://www.spoj.com/problems/GSS1/
 * 7. LintCode 207. 区间求和 II：https://www.lintcode.com/problem/interval-sum-ii/description
 * 8. HackerRank Array and simple queries：https://www.hackerrank.com/challenges/array-and-simple-queries/problem
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_SequenceOperation {

	public static int MAXN = 100001;

	// 原始数组
	public static int[] arr = new int[MAXN];

	// 累加和用来统计1的数量
	public static int[] sum = new int[MAXN << 2];

	// 连续0的最长子串长度
	public static int[] len0 = new int[MAXN << 2];

	// 连续0的最长前缀长度
	public static int[] pre0 = new int[MAXN << 2];

	// 连续0的最长后缀长度
	public static int[] suf0 = new int[MAXN << 2];

	// 连续1的最长子串长度
	public static int[] len1 = new int[MAXN << 2];

	// 连续1的最长前缀长度
	public static int[] pre1 = new int[MAXN << 2];

	// 连续1的最长后缀长度
	public static int[] suf1 = new int[MAXN << 2];

	// 懒更新信息，范围上所有数字被重置成了什么
	public static int[] change = new int[MAXN << 2];

	// 懒更新信息，范围上有没有重置任务
	public static boolean[] update = new boolean[MAXN << 2];

	// 懒更新信息，范围上有没有翻转任务
	public static boolean[] reverse = new boolean[MAXN << 2];

		/**
	 * 向上更新当前节点信息（合并左右子节点信息）
	 * 
	 * @param i 当前节点索引
	 * @param ln 左子区间长度
	 * @param rn 右子区间长度
	 */
	public static void up(int i, int ln, int rn) {
		int l = i << 1;    // 左子节点索引
		int r = i << 1 | 1;  // 右子节点索引
		
		// 更新区间内1的总数
		sum[i] = sum[l] + sum[r];
		
		// 更新连续0的信息
		// 取左右子区间的最大值或左右子区间连接处的连续0
		len0[i] = Math.max(Math.max(len0[l], len0[r]), suf0[l] + pre0[r]);
		
		// 更新连续0的前缀
		// 如果左子区间全是0，则前缀包括整个左子区间和右子区间的前缀
		pre0[i] = len0[l] < ln ? pre0[l] : (pre0[l] + pre0[r]);
		
		// 更新连续0的后缀
		// 如果右子区间全是0，则后缀包括整个右子区间和左子区间的后缀
		suf0[i] = len0[r] < rn ? suf0[r] : (suf0[l] + suf0[r]);
		
		// 更新连续1的信息（与连续0的处理方式类似）
		len1[i] = Math.max(Math.max(len1[l], len1[r]), suf1[l] + pre1[r]);
		pre1[i] = len1[l] < ln ? pre1[l] : (pre1[l] + pre1[r]);
		suf1[i] = len1[r] < rn ? suf1[r] : (suf1[l] + suf1[r]);
	}

		/**
	 * 向下传递懒标记到子节点
	 * 
	 * @param i 当前节点索引
	 * @param ln 左子区间长度
	 * @param rn 右子区间长度
	 */
	public static void down(int i, int ln, int rn) {
		// 先处理更新操作（优先级高于翻转操作）
		if (update[i]) {
			// 左子节点应用更新操作
			updateLazy(i << 1, change[i], ln);
			// 右子节点应用更新操作
			updateLazy(i << 1 | 1, change[i], rn);
			// 清除当前节点的更新标记
			update[i] = false;
		}
		
		// 再处理翻转操作
		if (reverse[i]) {
			// 左子节点应用翻转操作
			reverseLazy(i << 1, ln);
			// 右子节点应用翻转操作
			reverseLazy(i << 1 | 1, rn);
			// 清除当前节点的翻转标记
			reverse[i] = false;
		}
	}

		/**
	 * 处理区间赋值的懒标记
	 * 
	 * @param i 当前节点索引
	 * @param v 要设置的值（0或1）
	 * @param n 当前节点表示的区间长度
	 */
	public static void updateLazy(int i, int v, int n) {
		// 更新区间内1的总数
		sum[i] = v * n;
		// 更新连续0的信息：如果v=0，则整个区间都是0；否则都是1（没有0）
		len0[i] = pre0[i] = suf0[i] = v == 0 ? n : 0;
		// 更新连续1的信息：如果v=1，则整个区间都是1；否则都是0（没有1）
		len1[i] = pre1[i] = suf1[i] = v == 1 ? n : 0;
		// 记录区间赋值的目标值
		change[i] = v;
		// 设置更新标记
		update[i] = true;
		// 清空翻转标记（更新操作优先于翻转操作）
		reverse[i] = false;
	}

		/**
	 * 处理区间翻转的懒标记
	 * 
	 * @param i 当前节点索引
	 * @param n 当前节点表示的区间长度
	 */
	public static void reverseLazy(int i, int n) {
		// 翻转1的个数：1变0，0变1
		sum[i] = n - sum[i];
		
		int tmp;
		// 交换连续0和连续1的各种长度信息
		tmp = len0[i]; len0[i] = len1[i]; len1[i] = tmp; // 交换最长连续0/1长度
		tmp = pre0[i]; pre0[i] = pre1[i]; pre1[i] = tmp; // 交换前缀0/1长度
		tmp = suf0[i]; suf0[i] = suf1[i]; suf1[i] = tmp; // 交换后缀0/1长度
		
		// 翻转翻转标记（多次翻转可以抵消）
		reverse[i] = !reverse[i];
	}

		/**
	 * 构建线段树
	 * 
	 * @param l 当前区间左边界（1-based）
	 * @param r 当前区间右边界（1-based）
	 * @param i 当前节点索引
	 */
	public static void build(int l, int r, int i) {
		// 叶子节点情况
		if (l == r) {
			// 直接赋值原始数组的值
			sum[i] = arr[l];
			// 初始化连续0的信息
			len0[i] = pre0[i] = suf0[i] = arr[l] == 0 ? 1 : 0;
			// 初始化连续1的信息
			len1[i] = pre1[i] = suf1[i] = arr[l] == 1 ? 1 : 0;
		} else {
			// 非叶子节点，递归构建左右子树
			int mid = (l + r) >> 1;  // 计算中点
			build(l, mid, i << 1);   // 构建左子树
			build(mid + 1, r, i << 1 | 1); // 构建右子树
			// 向上合并子节点信息
			up(i, mid - l + 1, r - mid);
		}
		
		// 初始化懒标记为未激活状态
		update[i] = false;
		reverse[i] = false;
	}

		/**
	 * 区间赋值操作
	 * 
	 * @param jobl 待更新区间的左边界（1-based）
	 * @param jobr 待更新区间的右边界（1-based）
	 * @param jobv 要设置的值（0或1）
	 * @param l 当前节点区间左边界（1-based）
	 * @param r 当前节点区间右边界（1-based）
	 * @param i 当前节点索引
	 */
	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		// 当前区间完全包含在待更新区间内
		if (jobl <= l && r <= jobr) {
			// 直接应用懒标记
			updateLazy(i, jobv, r - l + 1);
		} else {
			// 当前区间部分包含在待更新区间内
			int mid = (l + r) >> 1; // 计算中点
			
			// 先向下传递懒标记
			down(i, mid - l + 1, r - mid);
			
			// 递归处理左右子区间
			if (jobl <= mid) {
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			
			// 向上合并子节点信息
			up(i, mid - l + 1, r - mid);
		}
	}

		/**
	 * 区间翻转操作
	 * 
	 * @param jobl 待翻转区间的左边界（1-based）
	 * @param jobr 待翻转区间的右边界（1-based）
	 * @param l 当前节点区间左边界（1-based）
	 * @param r 当前节点区间右边界（1-based）
	 * @param i 当前节点索引
	 */
	public static void reverse(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在待翻转区间内
		if (jobl <= l && r <= jobr) {
			// 直接应用翻转懒标记
			reverseLazy(i, r - l + 1);
		} else {
			// 当前区间部分包含在待翻转区间内
			int mid = (l + r) >> 1; // 计算中点
			
			// 先向下传递懒标记
			down(i, mid - l + 1, r - mid);
			
			// 递归处理左右子区间
			if (jobl <= mid) {
				reverse(jobl, jobr, l, mid, i << 1);
			}
			if (jobr > mid) {
				reverse(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			
			// 向上合并子节点信息
			up(i, mid - l + 1, r - mid);
		}
	}

		/**
	 * 查询区间内1的总数
	 * 
	 * @param jobl 查询区间的左边界（1-based）
	 * @param jobr 查询区间的右边界（1-based）
	 * @param l 当前节点区间左边界（1-based）
	 * @param r 当前节点区间右边界（1-based）
	 * @param i 当前节点索引
	 * @return 区间内1的总数
	 */
	public static int querySum(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在查询区间内
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		
		// 当前区间部分包含在查询区间内
		int mid = (l + r) >> 1; // 计算中点
		
		// 先向下传递懒标记
		down(i, mid - l + 1, r - mid);
		
		int ans = 0;
		// 递归查询左右子区间
		if (jobl <= mid) {
			ans += querySum(jobl, jobr, l, mid, i << 1);
		}
		if (jobr > mid) {
			ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		
		return ans;
	}

		/**
	 * 查询区间内连续1的最长长度
	 * 
	 * @param jobl 查询区间的左边界（1-based）
	 * @param jobr 查询区间的右边界（1-based）
	 * @param l 当前节点区间左边界（1-based）
	 * @param r 当前节点区间右边界（1-based）
	 * @param i 当前节点索引
	 * @return 长度为3的数组：[最长连续1的长度, 连续1的最长前缀长度, 连续1的最长后缀长度]
	 */
	public static int[] queryLongest(int jobl, int jobr, int l, int r, int i) {
		// 当前区间完全包含在查询区间内
		if (jobl <= l && r <= jobr) {
			return new int[] { len1[i], pre1[i], suf1[i] };
		} else {
			// 当前区间部分包含在查询区间内或查询区间跨越多个子区间
			int mid = (l + r) >> 1; // 计算中点
			int ln = mid - l + 1;    // 左子区间长度
			int rn = r - mid;        // 右子区间长度
			
			// 先向下传递懒标记
			down(i, ln, rn);
			
			// 查询区间完全在左子区间
			if (jobr <= mid) {
				return queryLongest(jobl, jobr, l, mid, i << 1);
			}
			// 查询区间完全在右子区间
			if (jobl > mid) {
				return queryLongest(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			
			// 查询区间跨越左右子区间
			// 分别查询左右子区间的信息
			int[] l3 = queryLongest(jobl, jobr, l, mid, i << 1);
			int[] r3 = queryLongest(jobl, jobr, mid + 1, r, i << 1 | 1);
			
			// 提取左右子区间的信息
			int llen = l3[0], lpre = l3[1], lsuf = l3[2];  // 左子区间的最长1长度、前缀、后缀
			int rlen = r3[0], rpre = r3[1], rsuf = r3[2];  // 右子区间的最长1长度、前缀、后缀
			
			// 合并信息
			// 最长连续1长度：左子区间的最大值、右子区间的最大值、或左右连接处的连续1
			int len = Math.max(Math.max(llen, rlen), lsuf + rpre);
			
			// 连续1的最长前缀：如果左子区间的最长连续1覆盖了整个查询部分的左子区间，则包括右子区间的前缀
			// 任务实际影响了左侧范围的几个点 -> mid - Math.max(jobl, l) + 1
			int pre = llen < mid - Math.max(jobl, l) + 1 ? lpre : (lpre + rpre);
			
			// 连续1的最长后缀：如果右子区间的最长连续1覆盖了整个查询部分的右子区间，则包括左子区间的后缀
			// 任务实际影响了右侧范围的几个点 -> Math.min(r, jobr) - mid
			int suf = rlen < Math.min(r, jobr) - mid ? rsuf : (lsuf + rsuf);
			
			return new int[] { len, pre, suf };
		}
	}

		/**
	 * 主函数：读取输入并处理操作
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 * 
	 * 输入格式：
	 * - 第一行：n（序列长度）和m（操作数量）
	 * - 第二行：n个0或1，表示初始序列
	 * - 接下来m行：每行一个操作，格式为 op l r
	 * 
	 * 操作类型：
	 * - 0 l r：将区间[l,r]全部置为0
	 * - 1 l r：将区间[l,r]全部置为1
	 * - 2 l r：将区间[l,r]全部取反
	 * - 3 l r：查询区间[l,r]中1的个数
	 * - 4 l r：查询区间[l,r]中连续1的最长长度
	 */
	public static void main(String[] args) throws IOException {
		// 高效输入输出配置
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数据规模
		in.nextToken();
		int n = (int) in.nval; // 序列长度
		in.nextToken();
		int m = (int) in.nval; // 操作次数
		
		// 读取初始序列
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval; // 1-based索引存储
		}
		
		// 建立线段树
		build(1, n, 1);
		
		// 处理每个操作
		for (int i = 1, op, jobl, jobr; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			jobl = (int) in.nval + 1; // 注意题目给的下标从0开始，线段树下标从1开始
			in.nextToken();
			jobr = (int) in.nval + 1; // 注意题目给的下标从0开始，线段树下标从1开始
			
			// 根据操作类型执行相应的线段树操作
			switch (op) {
			case 0: // 区间置0
				update(jobl, jobr, 0, 1, n, 1);
				break;
			case 1: // 区间置1
				update(jobl, jobr, 1, 1, n, 1);
				break;
			case 2: // 区间取反
				reverse(jobl, jobr, 1, n, 1);
				break;
			case 3: // 查询1的个数
				out.println(querySum(jobl, jobr, 1, n, 1));
				break;
			case 4: // 查询连续1的最长长度
				int[] ans = queryLongest(jobl, jobr, 1, n, 1);
				out.println(ans[0]);
				break;
			default:
				// 异常操作类型处理
				break;
			}
		}
		
		// 清理资源
		out.flush();
		out.close();
		br.close();
	}

// 扩展学习：线段树在工程中的应用
/*
 * 1. 数据库索引：在数据库系统中，线段树可以用于优化范围查询操作
 * 2. 图像分割：在计算机视觉中，线段树可用于区域分割和特征提取
 * 3. 网络流量监控：实时监控和分析网络流量的区间统计信息
 * 4. 股票分析：维护股票价格的区间最大值、最小值、平均值等信息
 * 5. 地理信息系统：处理地理区域的查询和更新操作
 */

// 线段树调试技巧
/*
 * 1. 打印中间状态：在关键操作前后输出线段树状态
 * 2. 断言验证：使用断言验证线段树的一致性和正确性
 * 3. 小数据测试：使用小规模测试用例验证算法正确性
 * 4. 边界测试：特别关注空区间、单元素区间等边界情况
 */

// 类似题目和训练推荐
/*
 * 1. LeetCode 307. 区域和检索 - 数组可修改：https://leetcode.com/problems/range-sum-query-mutable/
 * 2. LeetCode 315. 计算右侧小于当前元素的个数：https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 * 3. LeetCode 327. 区间和的个数：https://leetcode.com/problems/count-of-range-sum/
 * 4. LeetCode 493. 翻转对：https://leetcode.com/problems/reverse-pairs/
 * 5. LeetCode 239. 滑动窗口最大值：https://leetcode.com/problems/sliding-window-maximum/
 * 6. LeetCode 732. 我的日程安排表 III：区间重叠问题
 * 7. LeetCode 699. 掉落的方块：区间更新与查询最大值
 * 8. LeetCode 995. K 连续位的最小翻转次数：区间翻转操作
 * 9. LintCode 205. Interval Minimum Number：https://www.lintcode.com/problem/interval-minimum-number/
 * 10. LintCode 207. 区间求和 II：https://www.lintcode.com/problem/interval-sum-ii/description
 * 11. LintCode 439. 线段树的查询 II：区间和查询
 * 12. LintCode 440. 线段树的修改：单点更新
 * 13. Codeforces 61E. Enemy is weak：区间统计问题
 * 14. Codeforces 459D. Pashmak and Parmida's problem：区间逆序对统计
 * 15. 牛客网 NC24970. 线段树练习一：基础区间操作
 * 16. 杭电 OJ 1542. Atlantis：扫描线算法与线段树
 * 17. USACO 2017 January Contest, Gold Problem 1. Balanced Photo：区间统计
 * 18. AtCoder ARC 008 B. 投票：区间操作与统计
 * 19. SPOJ GSS1 - Can you answer these queries I：区间最大子段和
 * 20. UVa OJ 11990. Dynamic Inversion：动态逆序对问题
 * 21. 洛谷 P3373. 【模板】线段树 2：区间乘加操作
 * 22. 洛谷 P3805. 【模板】manacher：最长回文子串
 * 23. 计蒜客 线段树专题：各种线段树应用场景
 * 24. HackerRank Array and simple queries：https://www.hackerrank.com/challenges/array-and-simple-queries/problem
 */

// 掌握线段树的关键点
/*
 * 1. 懒标记的正确传递与优先级处理
 * 2. 区间合并逻辑的设计（如本题中的连续1长度合并）
 * 3. 不同操作类型的统一处理框架
 * 4. 边界条件的处理（如叶子节点、空区间等）
 * 5. 效率优化：避免不必要的递归和计算
 * 6. Java中的数组实现技巧：使用静态数组存储线段树
 * 7. 数据规模评估与内存分配
 * 8. 异常处理与参数校验
 * 9. 多线程环境下的线程安全考虑
 * 10. 性能测试与基准比较
 */

}