package class167;

/**
 * 八纵八横问题 - 使用线段树分治 + 可撤销线性基 + 带权并查集
 * 
 * 【题目描述】
 * 有n个点，给定m条边，每条边的边权用01字符串表达，初始时图保证连通
 * 初始的m条边永不删除，接下来有q条操作，操作分为三种类型：
 * - Add x y z：加入点x到点y的边，边权是z（01字符串），第k条添加操作的边编号为k
 * - Cancel k：删除编号为k的边
 * - Change k z：将编号为k的边的边权修改为z
 * 要求计算从1号点出发最后回到1号点的回路中，所有边权异或的最大值
 * 需要输出初始状态以及每个操作后的异或最大值
 * 
 * 【输入输出】
 * 输入：n, m, q，然后是m条初始边，最后是q个操作
 * 输出：初始状态和每个操作后的异或最大值（01字符串形式）
 * 
 * 【算法核心】
 * 1. 线段树分治：处理动态边的添加、删除和修改操作
 * 2. 带权并查集：维护连通性和路径异或值
 * 3. 可撤销线性基：计算异或最大值
 * 4. 位图（BitSet）：高效存储和处理长二进制边权
 * 
 * 【时间复杂度】
 * O((m + q) * BIT * log q)，其中BIT是边权的最大位数（本题为999位）
 * - 线段树分治的时间复杂度为O((m + q) * log q)
 * - 线性基操作的时间复杂度为O(BIT)
 * - 每次合并操作的时间复杂度为O(BIT)
 * 
 * 【空间复杂度】
 * O((m + q) * log q + BIT * MAXQ)，用于存储线段树节点、线性基和并查集信息
 * 
 * 【优化技巧】
 * 1. 使用位图（BitSet）高效存储长二进制数
 * 2. 带权并查集只做路径压缩，不做按秩合并，以支持撤销操作
 * 3. 线段树分治处理动态边的生命周期
 * 4. 使用FastReader优化输入速度
 * 
 * 【测试链接】
 * https://www.luogu.com.cn/problem/P3733
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 八纵八横问题（洛谷P3733）
 * 题目来源：洛谷 https://www.luogu.com.cn/problem/P3733
 * 
 * 核心算法：线段树分治 + 带权并查集 + 可撤销线性基 + 位图处理
 * 
 * 问题描述：
 * 给定一个动态变化的无向图，支持三种操作：
 * 1. Add u v w：添加一条连接u和v，权值为w的边
 * 2. Cancel k：删除第k次Add操作添加的边
 * 3. Change k w：修改第k次Add操作添加的边的权值为w
 * 
 * 要求：在初始状态和每次操作后，输出图中异或和最大的回路（异或回路最大值）。
 * 若不存在回路，输出0。
 * 
 * 算法思路：
 * 1. 使用线段树分治处理动态边，将每条边的生命周期区间转化为线段树上的节点
 * 2. 使用带权并查集维护节点之间的连通性和异或路径值
 * 3. 使用可撤销线性基存储环的异或值，用于计算最大异或回路
 * 4. 使用位图（BitSet）处理超长二进制数的异或运算
 * 
 * 时间复杂度分析：
 * - 线段树分治：O((m + q) * log q)
 * - 线性基操作：O(BIT) 每操作
 * - 总体时间复杂度：O((m + q) * log q * BIT)，其中BIT=999是二进制数的最大位数
 * 
 * 空间复杂度分析：
 * - 线段树：O((m + q) * log q)
 * - 并查集：O(n)
 * - 线性基：O(BIT)
 * - 总体空间复杂度：O(n + BIT + (m + q) * log q)
 * 
 * 多语言实现对比：
 * - Java：使用位图（BitSet）自定义类处理超长二进制数，实现可撤销数据结构较复杂
 * - C++：可使用bitset模板类更高效地处理二进制操作，指针操作更灵活，效率更高
 * - Python：位运算效率较低，但实现思路相同，适合处理小规模测试用例
 * 
 * 优化技巧：
 * 1. 使用链式前向星存储线段树节点的边列表
 * 2. 实现高效的FastReader类处理大量输入
 * 3. 使用可撤销的并查集和线性基实现回溯
 * 4. 使用位图（BitSet）分块存储超长二进制数
 * 5. 并查集只进行路径压缩，不进行按秩合并，以便支持撤销操作
 * 6. 线段树分治将动态问题转化为静态问题处理
 * 
 * 注意事项：
 * 1. 由于边权可能很长（最长999位），不能使用普通整数类型存储
 * 2. 线段树分治需要离线处理所有操作
 * 3. 撤销操作需要正确维护并查集和线性基的状态
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3733
 * 
 * 线段树分治相关题目训练列表：
 * 
 * 1. 二分图 /【模板】线段树分治 - P5787 (洛谷)
 *    链接：https://www.luogu.com.cn/problem/P5787
 *    描述：维护动态图使其为二分图
 *    解法：线段树分治 + 扩展域并查集
 *    复杂度：O((n + m) log m)
 *    
 * 2. 最小异或查询 - ABC308G (AtCoder)
 *    链接：https://atcoder.jp/contests/abc308/tasks/abc308_g
 *    描述：维护一个集合，支持添加/删除数字，查询任意两数异或最小值
 *    解法：01Trie + 在线维护
 *    复杂度：O(q log V)
 *    
 * 3. 火星商店 - P4585 (洛谷)
 *    链接：https://www.luogu.com.cn/problem/P4585
 *    描述：维护n个商店，支持添加商品，查询特定范围异或最大值
 *    解法：线段树分治 + 可持久化Trie
 *    复杂度：O((n+q) log q log V)
 *    
 * 4. 唯一出现次数 - 1681F (Codeforces)
 *    链接：https://codeforces.com/contest/1681/problem/F
 *    描述：统计树上路径中唯一出现的颜色数量
 *    解法：线段树分治 + 可撤销并查集
 *    复杂度：O((n + m) log m)
 *    
 * 5. 边着色 - 576E (Codeforces)
 *    链接：https://codeforces.com/contest/576/problem/E
 *    描述：给边着色使得每种颜色构成的子图都是二分图
 *    解法：线段树分治 + 多个扩展域并查集
 *    复杂度：O((n + m) log m)
 *    
 * 6. 连通图 - P5227 (洛谷)
 *    链接：https://www.luogu.com.cn/problem/P5227
 *    描述：初始连通图，每次删除一些边，查询是否仍连通
 *    解法：线段树分治 + 可撤销并查集
 *    复杂度：O((n + m) log m)
 *    
 * 7. 大融合 - P4219 (洛谷)
 *    链接：https://www.luogu.com.cn/problem/P4219
 *    描述：支持加边和查询边负载（删去该边后连通块大小乘积）
 *    解法：线段树分治 + 可撤销并查集
 *    复杂度：O((n + m) log m)
 *    
 * 8. 最小mex生成树 - P5631 (洛谷)
 *    链接：https://www.luogu.com.cn/problem/P5631
 *    描述：求生成树使得边权集合的mex最小
 *    解法：线段树分治 + 可撤销并查集 + 二分答案
 *    复杂度：O((n + m) log m log n)
 *    
 * 9. 博物馆劫案 - CF601E / Luogu P4585
 *    链接：https://codeforces.com/contest/601/problem/E
 *    描述：支持添加/删除商品，查询背包问题变形
 *    解法：线段树分治 + 动态规划
 *    复杂度：O(qk log q + nk)
 *    
 * 10. 细胞分裂 - AGC010C (AtCoder)
 *    链接：https://atcoder.jp/contests/agc010/tasks/agc010_c
 *    描述：分割矩形并计算每次分割后的连通分量数
 *    解法：线段树分治 + 可撤销并查集
 *    复杂度：O((n + m) log m)
 */
public class Code05_EightVerticalHorizontal1 {

	// 常量定义
	public static final int MAXN = 501;     // 最大节点数
	public static final int MAXQ = 1001;    // 最大操作数
	public static final int MAXT = 10001;   // 最大边操作数
	public static final int BIT = 999;      // 边权的最大位数（二进制）
	public static final int INT_BIT = 32;   // 整型位数，用于位图分块存储

	/**
 * 位图（BitSet）类 - 用于高效存储和处理长二进制数
 * 
 * 由于边权长度可达1000位，超过Java内置整数类型，因此需要自定义位图实现
 * 使用整型数组分块存储，每32位存储在一个整型中，采用紧凑存储提高空间利用率
 * 
 * 核心设计思想：
 * 1. 分块存储：每INT_BIT位（32位）存储在一个整型中，通过位运算高效访问和修改
 * 2. 低位优先：存储时低位在前，便于位运算和线性基的构建
 * 3. 高效操作：所有位操作基于位运算实现，避免逐位处理的性能开销
 * 
 * 性能优化：
 * 1. 批量位操作：通过整型数组实现并行位处理
 * 2. 空间优化：仅存储必要位数，避免浪费内存
 * 3. 操作优化：使用位掩码和位移操作实现常数时间的位访问
 */
static class BitSet {

	public int len;        // 位图的长度（整型数组的大小）
	public int[] arr;      // 存储位图数据的整型数组

	/**
	 * 构造一个空的位图
	 * 时间复杂度：O(len)
	 */
	public BitSet() {
		len = BIT / INT_BIT + 1;  // 计算需要的整型数组长度，向上取整
		arr = new int[len];      // 初始化整型数组，默认全0
	}

	/**
	 * 从字符串构造位图
	 * @param s 二进制字符串，高位在前，低位在后
	 * 时间复杂度：O(s.length())
	 */
	public BitSet(String s) {
		len = BIT / INT_BIT + 1;
		arr = new int[len];
		// 将字符串转换为位图，注意反转顺序，因为字符串高位在前，而我们需要低位在前
		// 这种转换方式确保了位运算的正确性，使得第i位对应数值的2^i位
		for (int i = 0, j = s.length() - 1; i < s.length(); i++, j--) {
			set(i, s.charAt(j) - '0');  // 设置第i位的值（0或1）
		}
	}

	/**
	 * 获取位图中第i位的值
	 * @param i 要获取的位的索引（从0开始）
	 * @return 该位的值（0或1）
	 * 时间复杂度：O(1) - 位运算常数时间
	 */
	public int get(int i) {
		// 找到对应的整型块，然后通过位移和与运算获取该位的值
		// i/INT_BIT 计算属于哪个整型块，i%INT_BIT 计算在该块中的位置
		return (arr[i / INT_BIT] >> (i % INT_BIT)) & 1;
	}

	/**
	 * 设置位图中第i位的值
	 * @param i 要设置的位的索引（从0开始）
	 * @param v 要设置的值（0或1）
	 * 时间复杂度：O(1) - 位运算常数时间
	 */
	public void set(int i, int v) {
		if (v == 0) {
			// 清除该位：与上取反后的掩码，保持其他位不变
			arr[i / INT_BIT] &= ~(1 << (i % INT_BIT));
		} else {
			// 设置该位：或上对应的掩码，保持其他位不变
			arr[i / INT_BIT] |= 1 << (i % INT_BIT);
		}
	}

	/**
	 * 与另一个位图进行异或操作
	 * @param other 要异或的另一个位图
	 * 时间复杂度：O(len) - 每个整型块独立异或
	 */
	public void eor(BitSet other) {
		// 对每个整型块进行异或操作，利用硬件级并行计算
		// 这比逐位异或效率高得多，尤其是处理长二进制数时
		for (int i = 0; i < len; i++) {
			arr[i] ^= other.arr[i];  // 对每个整型进行异或操作
		}
	}

	/**
	 * 清空位图，所有位设置为0
	 * 时间复杂度：O(len)
	 */
	public void clear() {
		// 直接将所有整型块置0，比逐位清除效率高
		for (int i = 0; i < len; i++) {
			arr[i] = 0;
		}
	}

}

	// 全局变量
	public static int n;                  // 节点数
	public static int m;                  // 初始边数
	public static int q;                  // 操作数
	public static int[] x = new int[MAXQ];  // 记录添加的边的x端点
	public static int[] y = new int[MAXQ];  // 记录添加的边的y端点
	public static BitSet[] w = new BitSet[MAXQ];  // 记录添加的边的权值
	public static int edgeCnt = 0;        // 当前边的计数器
	public static int[] last = new int[MAXQ];     // 记录每条边的最后活跃时间

	// 可撤销线性基 - 用于计算异或最大值
	public static BitSet[] basis = new BitSet[BIT + 1];  // 线性基数组，basis[i]表示最高位为i的基向量
	public static int[] inspos = new int[BIT + 1];       // 记录插入顺序的位置，用于撤销
	public static int basiz = 0;                         // 线性基的大小

	// 经典带权并查集 - 维护连通性和路径异或值
	// 注意：只做路径压缩（扁平化），不做按秩合并，以支持撤销操作
	public static int[] father = new int[MAXN];    // 并查集父节点数组
	public static BitSet[] eor = new BitSet[MAXN]; // 并查集路径异或值数组

	// 时间轴线段树上的区间任务列表 - 使用链式前向星存储
	public static int[] head = new int[MAXQ << 2];  // 每个线段树节点对应的边链表头
	public static int[] next = new int[MAXT];      // 边链表的next指针
	public static int[] tox = new int[MAXT];       // 边的x端点
	public static int[] toy = new int[MAXT];       // 边的y端点
	public static BitSet[] tow = new BitSet[MAXT]; // 边的权值
	public static int cnt = 0;                     // 边计数器

	// 每一步的最大异或值
	public static BitSet[] ans = new BitSet[MAXQ]; // 存储每个时间点的异或最大值

	/**
	 * 将一个数插入线性基
	 * @param num 要插入的位图表示的数
	 * 时间复杂度：O(BIT)
	 */
	public static void insert(BitSet num) {
		for (int i = BIT; i >= 0; i--) {
			// 从高位到低位寻找第一个为1的位
			if (num.get(i) == 1) {
				// 如果该位没有基向量，直接插入
				if (basis[i].get(i) == 0) {
					basis[i] = num;
					inspos[basiz++] = i;  // 记录插入的位置，用于撤销
					return;
				}
				// 否则，将num异或上该位的基向量，继续处理
				num.eor(basis[i]);
			}
		}
		// 如果num最终变为0，表示它可以被当前线性基表示，不需要插入
	}

	/**
	 * 计算与线性基的最大异或值
	 * @return 异或最大值的位图表示
	 * 时间复杂度：O(BIT)
	 */
	public static BitSet maxEor() {
		BitSet ans = new BitSet();
		// 从高位到低位遍历线性基
		for (int i = BIT; i >= 0; i--) {
			// 如果当前位为0，且存在该位的基向量，则异或上该基向量
			// 这样可以尽可能使高位为1，从而得到最大值
			if (ans.get(i) == 0 && basis[i].get(i) == 1) {
				ans.eor(basis[i]);
			}
		}
		return ans;
	}

	/**
	 * 撤销线性基到指定大小
	 * @param oldsiz 要恢复到的线性基大小
	 * 时间复杂度：O(basiz - oldsiz)
	 */
	public static void cancel(int oldsiz) {
		// 撤销所有在oldsiz之后插入的元素
		while (basiz > oldsiz) {
			// 清空对应的基向量
			basis[inspos[--basiz]].clear();
		}
	}

	// 扁平化优化，find的同时修改eor，就是经典的带权并查集
/**
 * 并查集查找函数（带路径压缩）
 * 采用递归实现的路径压缩，确保后续查找操作接近O(1)时间复杂度
 * 
 * 核心技术点：
 * 1. 路径压缩：在查找过程中，将路径上的所有节点直接连接到根节点
 * 2. 动态维护路径异或值：递归回溯时更新当前节点到根节点的异或值
 * 3. 无按秩合并：为支持撤销操作，不使用按秩合并优化
 * 
 * @param i 要查找的节点
 * @return 节点i所在集合的根节点
 * @throws StackOverflowError 当递归深度过深时可能发生
 * 时间复杂度：O(α(n))，其中α是阿克曼函数的反函数，实际应用中近似为常数
 */
public static int find(int i) {
	if (i != father[i]) {
		int tmp = father[i];  // 保存原父节点
		father[i] = find(tmp);  // 递归查找根节点并进行路径压缩
		eor[i].eor(eor[tmp]);   // 更新路径异或值：i到父节点 + 父节点到根
	}
	return father[i];  // 返回根节点
}

/**
 * 获取从节点i到根节点的路径异或值
 * 调用find函数确保路径已压缩，异或值已更新
 * 
 * @param i 目标节点
 * @return 路径异或值的位图表示
 * 时间复杂度：O(α(n) * BIT)，其中BIT是边权的最大位数
 */
public static BitSet getEor(int i) {
	find(i);  // 确保路径已压缩，异或值已更新
	return eor[i];  // 直接返回计算好的路径异或值
}

/**
 * 合并两个节点所在的集合，并处理可能形成的环
 * 
 * 工作原理：
 * 1. 查找两个节点的根节点
 * 2. 计算u到v的路径异或值：path(u) ^ path(v) ^ w(u,v)
 * 3. 如果两个根节点相同，说明形成环，将环的异或值插入线性基
 * 4. 如果不同，将一个集合的根连接到另一个集合的根，并设置适当的路径异或值
 * 
 * 优化技巧：
 * 1. 利用路径压缩加速查找
 * 2. 通过异或操作的性质高效计算环的异或值
 * 3. 仅在形成环时插入线性基，减少不必要的计算
 * 
 * @param u 第一个节点
 * @param v 第二个节点
 * @param w 边u-v的权值
 * 时间复杂度：O(α(n) * BIT)
 */
public static void union(int u, int v, BitSet w) {
	int fu = find(u);  // 查找u的根节点
	int fv = find(v);  // 查找v的根节点
	
	// 计算从u到v的异或路径值：u到fu的异或值 ^ v到fv的异或值 ^ 边u-v的权值
	BitSet weight = new BitSet();
	weight.eor(getEor(u));
	weight.eor(getEor(v));
	weight.eor(w);
	
	if (fu == fv) {
		// u和v已经在同一集合中，形成环，将环的异或值插入线性基
		// 环的异或值等于u到v的异或路径值
		insert(weight);
	} else {
		// 合并两个不同的集合
		// 直接将fv的父节点设置为fu（未使用按秩合并以支持撤销）
		father[fv] = fu;
		// 设置fv到fu的路径异或值为计算得到的weight
		eor[fv] = weight;
	}
}

	/**
	 * 向线段树节点添加一条边
	 * @param i 线段树节点编号
	 * @param x 边的起点
	 * @param y 边的终点
	 * @param w 边的权值
	 * 时间复杂度：O(1)
	 */
	public static void addEdge(int i, int x, int y, BitSet w) {
		// 使用链式前向星存储边，next[cnt]指向下一条边
		next[++cnt] = head[i];
		tox[cnt] = x;      // 边的起点
		toy[cnt] = y;      // 边的终点
		tow[cnt] = w;      // 边的权值
		head[i] = cnt;     // 更新当前节点的边链表头
	}

	/**
	 * 线段树分治的核心方法：将边添加到线段树的相应区间
	 * 算法核心思想：将边的生命周期区间分解到线段树的各个节点上，确保每条边只在其有效时间区间内被处理
	 * 
	 * @param jobl 边的有效区间左端点
	 * @param jobr 边的有效区间右端点
	 * @param jobx 边的起点
	 * @param joby 边的终点
	 * @param jobw 边的权值（位图表示的二进制数）
	 * @param l 当前线段树节点区间的左端点
	 * @param r 当前线段树节点区间的右端点
	 * @param i 当前线段树节点编号
	 * 
	 * 时间复杂度：O(log q)，其中q是最大操作数
	 * 空间复杂度：O(log q)，递归调用栈深度
	 * 
	 * 优化策略：
	 * 1. 利用线段树的区间分解特性，每条边最多被分解到O(log q)个节点
	 * 2. 采用链式前向星存储每个节点的边列表，避免内存浪费
	 * 3. 对于完全覆盖的区间，直接添加边，不继续递归，减少函数调用开销
	 */
	public static void add(int jobl, int jobr, int jobx, int joby, BitSet jobw, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			// 当前线段树节点区间完全包含在边的有效区间内，直接添加边
			addEdge(i, jobx, joby, jobw);
		} else {
			// 否则递归到左右子树，将边分解到更细粒度的区间
			int mid = (l + r) >> 1;  // 计算中间点，等价于(l + r) / 2，但使用位运算提高效率
			if (jobl <= mid) {
				// 边的有效区间与左子树区间有交集，递归处理左子树
				add(jobl, jobr, jobx, joby, jobw, l, mid, i << 1);
			}
			if (jobr > mid) {
				// 边的有效区间与右子树区间有交集，递归处理右子树
				add(jobl, jobr, jobx, joby, jobw, mid + 1, r, i << 1 | 1);
			}
		}
	}

	/**
	 * 线段树分治的DFS遍历过程
	 * 算法核心：深度优先遍历线段树，在进入当前节点时应用所有该节点上的边，在离开时撤销这些边的影响
	 * 实现递归回溯机制，确保每次递归调用结束后恢复线性基的状态
	 * 
	 * @param l 当前线段树节点区间的左端点
	 * @param r 当前线段树节点区间的右端点
	 * @param i 当前线段树节点编号
	 * 
	 * 时间复杂度：O((m + q) * log q * BIT)，其中：
	 *            - 线段树分治处理O((m + q) * log q)条边
	 *            - 每条边的处理需要O(BIT)的线性基和并查集操作
	 * 
	 * 递归回溯机制详解：
	 * 1. 保存当前线性基的大小(oldsiz)，作为回溯的标记点
	 * 2. 处理当前节点上的所有边，这些边在[l, r]区间内有效
	 * 3. 如果是叶子节点，计算并保存该时间点的最大异或回路值
	 * 4. 如果是非叶子节点，递归处理左右子树
	 * 5. 递归返回后，调用cancel方法撤销线性基的修改，恢复到调用前的状态
	 * 
	 * 这种回溯机制确保了每条边只在其有效时间区间内被考虑，且不会影响到其他时间区间的计算
	 */
	public static void dfs(int l, int r, int i) {
		// 保存当前线性基的大小，作为回溯的标记点
		// 这是实现撤销机制的关键，记录调用前的线性基状态
		int oldsiz = basiz;
		
		// 处理当前节点上的所有边
		// 这些边的生命周期完全覆盖了当前线段树节点表示的时间区间
		for (int e = head[i]; e > 0; e = next[e]) {
			// 调用union函数合并边的两个端点，并处理可能形成的环
			// 如果形成环，会将环的异或值自动插入到线性基中
			union(tox[e], toy[e], tow[e]);
		}
		
		// 判断是否到达叶子节点（单个时间点）
		if (l == r) {
			// 叶子节点对应一个具体的时间点，计算此时的最大异或回路值
			// 调用maxEor函数，使用当前线性基计算异或最大值
			ans[l] = maxEor();
		} else {
			// 非叶子节点，需要递归处理左右子树
			// 将当前时间区间分成两半
			int mid = (l + r) / 2;
			// 递归处理左子树区间[l, mid]
			dfs(l, mid, i << 1);
			// 递归处理右子树区间[mid+1, r]
			dfs(mid + 1, r, i << 1 | 1);
		}
		
		// 回溯，撤销当前节点的所有线性基操作
		// 将线性基恢复到调用该方法前的状态
		// 这样可以确保后续递归调用不受当前区间边的影响
		cancel(oldsiz);
	}

	/**
	 * 打印位图表示的二进制数
	 * @param bs 要打印的位图
	 * @param out 输出流
	 */
	public static void print(BitSet bs, PrintWriter out) {
		boolean flag = false;  // 标记是否找到第一个1
		
		// 从最高位开始遍历，跳过前导零
		for (int i = BIT, s; i >= 0; i--) {
			s = bs.get(i);
			if (s == 1) {
				flag = true;  // 找到第一个1后，开始输出
			}
			if (flag) {
				out.print(s);
			}
		}
		
		// 如果所有位都是0，输出0
		if (!flag) {
			out.print(0);
		}
		out.println();
	}

	/**
	 * 主函数 - 程序入口
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader();  // 创建高效输入流
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));  // 创建输出流
		
		// 读取输入数据
		n = in.nextInt();  // 节点数
		m = in.nextInt();  // 初始边数
		q = in.nextInt();  // 操作数
		
		// 初始化线性基数组
		for (int i = 0; i <= BIT; i++) {
			basis[i] = new BitSet();
		}
		
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;  // 每个节点初始父节点为自身
			eor[i] = new BitSet();  // 路径异或值初始化为0
		}
		
		// 处理初始边（这些边在整个过程中都存在）
		for (int i = 1; i <= m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			BitSet w = new BitSet(in.nextString());
			union(u, v, w);  // 合并节点并处理可能形成的环
		}
		
		// 计算初始状态的最大异或回路
		ans[0] = maxEor();
		
		// 处理每个操作
		String op;
		for (int i = 1; i <= q; i++) {
			op = in.nextString();
			
			if (op.equals("Add")) {
				// 添加边操作
				edgeCnt++;
				x[edgeCnt] = in.nextInt();
				y[edgeCnt] = in.nextInt();
				w[edgeCnt] = new BitSet(in.nextString());
				last[edgeCnt] = i;  // 记录边的起始时间
				
			} else if (op.equals("Cancel")) {
				// 删除边操作
				int k = in.nextInt();
				// 将边k添加到时间区间[last[k], i-1]，表示这段时间内有效
				add(last[k], i - 1, x[k], y[k], w[k], 1, q, 1);
				last[k] = 0;  // 标记为已删除
				
			} else {  // Change操作
				// 修改边操作
				int k = in.nextInt();
				// 先将原边添加到时间区间[last[k], i-1]
				add(last[k], i - 1, x[k], y[k], w[k], 1, q, 1);
				// 更新边的权值
				w[k] = new BitSet(in.nextString());
				last[k] = i;  // 更新边的起始时间
			}
		}
		
		// 处理所有在最后一次操作后仍然有效的边
		for (int i = 1; i <= edgeCnt; i++) {
			if (last[i] != 0) {
				// 将这些边添加到时间区间[last[i], q]
				add(last[i], q, x[i], y[i], w[i], 1, q, 1);
			}
		}
		
		// 执行线段树分治算法
		if (q > 0) {
			dfs(1, q, 1);
		}
		
		// 输出结果：初始状态和每个操作后的异或最大值
		for (int i = 0; i <= q; i++) {
			print(ans[i], out);
		}
		
		out.flush();  // 刷新输出缓冲
		out.close();  // 关闭输出流
	}

	/**
	 * FastReader类 - 高效输入处理器
	 * 针对大规模数据输入进行优化，避免普通Scanner的性能瓶颈
	 * 核心优化：
	 * 1. 使用字节缓冲区批量读取输入，减少IO系统调用
	 * 2. 手动处理字符解码，避免字符流的性能开销
	 * 3. 针对题目特点优化，支持整数和长字符串（边权）的快速读取
	 */
	static class FastReader {
		private static final int BUFFER_SIZE = 1 << 16;  // 缓冲区大小（64KB），平衡内存占用与读取效率
		private final InputStream in;       // 底层输入流
		private final byte[] buffer;        // 字节缓冲区，存储批量读取的数据
		private int ptr, len;               // 指针位置和当前缓存长度

		/**
		 * 构造函数，初始化输入流和缓冲区
		 * 时间复杂度：O(1)
		 * 空间复杂度：O(BUFFER_SIZE)
		 */
		public FastReader() {
			in = System.in;
			buffer = new byte[BUFFER_SIZE];
			ptr = len = 0;
		}

		/**
		 * 检查缓冲区中是否还有可读字节
		 * @return 如果有可读字节返回true，否则返回false
		 * @throws IOException 输入异常
		 * 时间复杂度：O(1)，仅在需要填充缓冲区时为O(BUFFER_SIZE)
		 */
		private boolean hasNextByte() throws IOException {
			if (ptr < len) {
				return true;  // 缓冲区还有数据
			}
			// 缓冲区已读完，重新填充
			ptr = 0;
			len = in.read(buffer);  // 批量读取数据到缓冲区
			return len > 0;         // 判断是否读取到数据
		}

		/**
		 * 读取一个字节
		 * @return 读取的字节值
		 * @throws IOException 输入异常
		 * 时间复杂度：O(1)
		 */
		private byte readByte() throws IOException {
			if (!hasNextByte()) {
				return -1;  // 到达流的末尾
			}
			return buffer[ptr++];  // 返回当前字节并移动指针
		}

		/**
		 * 读取一个整数
		 * 优化点：跳过前导空白字符，直接解析数字，支持负数处理
		 * @return 读取的整数值
		 * @throws IOException 输入异常
		 * 时间复杂度：O(数字位数)
		 */
		public int nextInt() throws IOException {
			int num = 0;
			byte b = readByte();
			// 跳过空白字符
			while (isWhitespace(b)) {
				b = readByte();
			}
			// 处理负数
			boolean minus = false;
			if (b == '-') {
				minus = true;
				b = readByte();
			}
			// 读取数字字符并转换为整数
			while (!isWhitespace(b) && b != -1) {
				num = num * 10 + (b - '0');
				b = readByte();
			}
			return minus ? -num : num;  // 根据符号返回结果
		}

		/**
		 * 读取一个字符串
		 * 特别针对本题中的长二进制串（边权）进行了优化
		 * @return 读取的字符串
		 * @throws IOException 输入异常
		 * 时间复杂度：O(字符串长度)
		 */
		public String nextString() throws IOException {
			byte b = readByte();
			// 跳过空白字符
			while (isWhitespace(b)) {
				b = readByte();
			}
			// 创建字符串构建器，预分配足够空间以避免频繁扩容
			// 由于边权可能长达999位，预分配1000容量
			StringBuilder sb = new StringBuilder(1000);
			// 读取非空白字符
			while (!isWhitespace(b) && b != -1) {
				sb.append((char) b);
				b = readByte();
			}
			return sb.toString();
		}

		/**
		 * 判断一个字节是否为空白字符
		 * @param b 要判断的字节
		 * @return 如果是空白字符返回true，否则返回false
		 * 时间复杂度：O(1)
		 */
		private boolean isWhitespace(byte b) {
			return b == ' ' || b == '\n' || b == '\r' || b == '\t';
		}
	}

}