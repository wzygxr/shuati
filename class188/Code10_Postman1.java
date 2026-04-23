/*
 * 邮递员问题，java版
 * 问题描述：给定n个点，m条有向边，邮递员从1号点开始，最后回到1号点，每条边都要走1次
 * 路径约束：给定t个序列，每个序列必须是路径的连续子段
 * 数据范围：2 <= n <= 5 * 10^4, 1 <= m <= 2 * 10^5, 0 <= t <= 10^4
 * 测试链接 : https://www.luogu.com.cn/problem/P3443
 * 
 * 面试要点：
 * - 邮递员问题（中国邮路问题）的变种
 * - 带约束的欧拉回路问题
 * - 路径压缩与解压缩技术
 * - 图的连通性与欧拉回路存在条件
 * 
 * ML/DL关联：
 * - 约束满足问题在机器学习中的应用
 * - 序列预测中的约束建模
 * - 图神经网络中的路径约束学习
 */

// 导入IOException异常处理类，用于处理输入输出过程中的异常情况
// 面试要点：异常处理机制在程序健壮性中的作用
// ML/DL关联：错误处理机制在模型训练中的重要性
import java.io.IOException;
// 导入InputStream输入流类，用于高效读取输入数据
// 面试要点：流的概念和操作在IO优化中的应用
// ML/DL关联：数据流管道的构建
import java.io.InputStream;
// 导入OutputStreamWriter输出流写入器类，用于包装输出流
// 面试要点：输出流的包装和优化
// ML/DL关联：输出结果的格式化处理
import java.io.OutputStreamWriter;
// 导入PrintWriter打印写入器类，提供格式化输出功能
// 面试要点：输出流的缓冲机制和格式化功能
// ML/DL关联：结果输出的标准化
import java.io.PrintWriter;
// 导入HashMap哈希映射类，用于高效的键值对存储
// 面试要点：哈希表的实现原理和性能特点
// ML/DL关联：特征映射和查找表的实现
import java.util.HashMap;

public class Code10_Postman1 {

	/**
	 * 键值对映射类，用于高效存储边的信息
	 * 使用long作为键，避免哈希冲突，提高查询效率
	 * 
	 * 面试要点：
	 * - 自定义数据结构的设计
	 * - 高效键值对存储策略
	 * - 位运算优化性能
	 * 
	 * ML/DL关联：
	 * - 高效数据结构在模型推理中的重要性
	 * - 哈希表在特征存储中的应用
	 */
	static class PairMap {
		/**
		 * 内部HashMap存储边的映射关系
		 * 面试要点：选择合适的数据结构
		 */
		public HashMap<Long, Integer> map = new HashMap<>();

		/**
		 * 生成唯一的long类型的键
		 * 将两个int值打包成一个long值
		 * 
		 * @param a 第一个整数
		 * @param b 第二个整数
		 * @return 组合后的long键值
		 * 
		 *         面试要点：
		 *         - 位运算技巧
		 *         - 键值唯一性保证
		 * 
		 *         ML/DL关联：
		 *         - 特征组合的高效表示
		 *         - 键值对存储的优化
		 */
		public long key(int a, int b) {
			// 使用位运算将两个int值打包成一个long值，提高查询效率
			return (((long) a) << 32) | (b & 0xffffffffL);
		}

		/**
		 * 检查是否存在指定的边
		 * 
		 * @param a 起点
		 * @param b 终点
		 * @return 是否存在该边
		 * 
		 *         面试要点：
		 *         - 查询操作的实现
		 *         - 封装底层数据结构的细节
		 */
		public boolean contains(int a, int b) {
			return map.containsKey(key(a, b));
		}

		/**
		 * 检查映射是否为空
		 * 
		 * @return 是否为空
		 * 
		 *         面试要点：
		 *         - 状态检查方法
		 *         - 封装性设计
		 */
		public boolean isEmpty() {
			return map.isEmpty();
		}

		/**
		 * 添加边到映射中
		 * 
		 * @param a 起点
		 * @param b 终点
		 * @param v 边的值
		 * 
		 *          面试要点：
		 *          - 插入操作的实现
		 *          - 键值对的管理
		 */
		public void put(int a, int b, int v) {
			map.put(key(a, b), v);
		}

		/**
		 * 获取边的值
		 * 
		 * @param a 起点
		 * @param b 终点
		 * @return 边的值
		 * 
		 *         面试要点：
		 *         - 查询操作的实现
		 *         - 异常处理考虑
		 */
		public int get(int a, int b) {
			return map.get(key(a, b));
		}

		/**
		 * 从映射中删除边
		 * 
		 * @param a 起点
		 * @param b 终点
		 * 
		 *          面试要点：
		 *          - 删除操作的实现
		 *          - 数据一致性维护
		 */
		public void remove(int a, int b) {
			map.remove(key(a, b));
		}

		/**
		 * 清空映射
		 * 
		 * 面试要点：
		 * - 批量操作的实现
		 * - 资源清理策略
		 */
		public void clear() {
			map.clear();
		}
	}

	/**
	 * 最大节点数常量，根据题目限制设置
	 * 面试要点：合理的空间预分配
	 * ML/DL关联：批次大小对内存的影响
	 */
	public static int MAXN = 50005;
	/**
	 * 最大边数常量，根据题目限制设置
	 * 面试要点：数组大小的合理估计
	 * ML/DL关联：序列长度对内存的影响
	 */
	public static int MAXM = 200005;
	/**
	 * 最大序列长度常量，根据题目限制设置
	 * 面试要点：内存使用上限控制
	 * ML/DL关联：特征维度对内存的影响
	 */
	public static int MAXC = 2000005;
	/**
	 * 节点数、边数和约束序列数
	 * 面试要点：全局变量的含义和作用
	 * ML/DL关联：超参数管理
	 */
	public static int n, m, t;
	/**
	 * 边的起点和终点数组
	 * a[i]表示第i条边的起点，b[i]表示第i条边的终点
	 * 面试要点：边的存储方式
	 * ML/DL关联：图结构的边表示
	 */
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];
	/**
	 * 约束序列数组，存储所有约束条件
	 * 面试要点：约束条件的存储
	 * ML/DL关联：约束条件的向量化表示
	 */
	public static int[] seq = new int[MAXC];

	/**
	 * 边映射对象，用于快速查找边
	 * 面试要点：高效查找数据结构
	 * ML/DL关联：快速检索机制
	 */
	public static PairMap pairEdge = new PairMap();

	/**
	 * 邮递员问题专用的图结构 - 邻接表
	 * 面试要点：图的多种存储方式
	 * ML/DL关联：图的邻接表表示
	 */
	public static int[] headg = new int[MAXN]; // 邻接表头指针
	public static int[] nextg = new int[MAXM]; // 邻接表下一个节点指针
	public static int[] tog = new int[MAXM]; // 邻接表目标节点
	/**
	 * 链头数组，记录每条边所属的链
	 * 面试要点：边的分组管理
	 * ML/DL关联：边的聚类表示
	 */
	public static int[] chainHead = new int[MAXM];
	/**
	 * 图的边计数器
	 * 面试要点：边的唯一标识
	 * ML/DL关联：边的索引管理
	 */
	public static int cntg;
	/**
	 * 有效边计数器
	 * 面试要点：有效元素计数
	 * ML/DL关联：有效样本计数
	 */
	public static int edgeCnt;

	/**
	 * 当前边指针数组，用于Hierholzer算法
	 * 面试要点：欧拉路径算法的关键数据结构
	 * ML/DL关联：路径遍历的状态管理
	 */
	public static int[] cur = new int[MAXN];
	/**
	 * 节点入度和出度数组
	 * 面试要点：欧拉回路存在条件的必要数据
	 * ML/DL关联：节点度数特征
	 */
	public static int[] inDeg = new int[MAXN];
	public static int[] outDeg = new int[MAXN];

	/**
	 * 标记数组，记录边是否为链头
	 * 面试要点：状态标记技术
	 * ML/DL关联：二进制特征表示
	 */
	public static boolean[] isHead = new boolean[MAXM];
	/**
	 * 边到边的映射数组，表示边之间的连接关系
	 * 面试要点：约束传播机制
	 * ML/DL关联：图中边的邻接关系
	 */
	public static int[] etoe = new int[MAXM];
	/**
	 * 边访问标记数组
	 * 面试要点：访问状态跟踪
	 * ML/DL关联：注意力机制中的访问掩码
	 */
	public static boolean[] vis = new boolean[MAXM];

	/**
	 * 欧拉路径结果数组
	 * 面试要点：路径存储结构
	 * ML/DL关联：序列生成结果存储
	 */
	public static int[] path = new int[MAXM];
	/**
	 * 路径计数器
	 * 面试要点：计数器管理
	 * ML/DL关联：序列长度统计
	 */
	public static int cntp;

	/**
	 * 最终答案数组
	 * 面试要点：结果重构
	 * ML/DL关联：模型输出格式化
	 */
	public static int[] ans = new int[MAXM];
	/**
	 * 答案计数器
	 * 面试要点：输出长度管理
	 * ML/DL关联：输出序列长度控制
	 */
	public static int cnta;

	/**
	 * 用于迭代算法的栈
	 * 面试要点：显式栈替代递归
	 * ML/DL关联：显式状态管理
	 */
	public static int[][] sta = new int[MAXM][2]; // [节点, 链头]
	/**
	 * 栈顶元素的节点和链头
	 * 面试要点：栈元素的组成部分
	 * ML/DL关联：状态变量管理
	 */
	public static int u, h;
	/**
	 * 栈大小
	 * 面试要点：栈操作管理
	 * ML/DL关联：缓存大小管理
	 */
	public static int stacksize;

	/**
	 * 入栈操作
	 * 
	 * @param u 节点编号
	 * @param c 链头编号
	 * 
	 *          面试要点：
	 *          - 栈的基本操作
	 *          - 元素的复合存储
	 */
	public static void push(int u, int c) {
		// 将节点和链头作为一个单元压入栈
		sta[stacksize][0] = u; // 节点
		sta[stacksize][1] = c; // 链头
		stacksize++; // 更新栈大小
	}

	/**
	 * 出栈操作
	 * 
	 * 面试要点：
	 * - 栈的基本操作
	 * - 元素的复合取出
	 */
	public static void pop() {
		stacksize--; // 更新栈大小
		u = sta[stacksize][0]; // 取出节点
		h = sta[stacksize][1]; // 取出链头
	}

	/**
	 * 添加边到图中
	 * 
	 * @param x 起点
	 * @param y 终点
	 * @param h 链头编号
	 * 
	 *          面试要点：
	 *          - 链式前向星建图
	 *          - 图的动态构建
	 */
	public static void addEdge(int x, int y, int h) {
		// 链式前向星建图
		nextg[++cntg] = headg[x]; // 新边指向原第一条边
		tog[cntg] = y; // 设置目标节点
		chainHead[cntg] = h; // 设置链头
		headg[x] = cntg; // 更新头指针
	}

	/**
	 * 建立边的链接关系
	 * 根据约束序列建立边之间的连接关系
	 * 
	 * 面试要点：
	 * - 约束条件的处理
	 * - 图的边约束建模
	 * - 邻接边的连接关系建立
	 * 
	 * ML/DL关联：
	 * - 约束满足问题的建模
	 * - 图中边的依赖关系
	 */
	public static boolean linkEdge() {
		// 将所有边加入映射
		for (int i = 1; i <= m; i++) {
			pairEdge.put(a[i], b[i], i); // 边(i,j)映射到编号i
			isHead[i] = true; // 初始时每条边都是链头
		}

		// 处理约束序列
		int siz = seq[1], l = 2, r = l + siz - 1; // 第一个序列的长度和范围
		int a, b, ledge, redge; // 左边、右边、左邻接边、右邻接边

		while (siz > 0) { // 遍历所有约束序列
			ledge = 0; // 左邻接边初始化为0
			for (int i = l; i < r; i++) { // 遍历序列中的相邻节点对
				a = seq[i]; // 当前节点
				b = seq[i + 1]; // 下一节点
				if (!pairEdge.contains(a, b)) { // 如果不存在这条边
					return false; // 约束无法满足
				}
				redge = pairEdge.get(a, b); // 获取右边的边编号
				if (ledge != 0) { // 如果左边有边
					if (etoe[ledge] != 0 && etoe[ledge] != redge) { // 如果约束冲突
						return false; // 约束无法满足
					}
					etoe[ledge] = redge; // 建立边的连接关系
					isHead[redge] = false; // 右边不再是链头
				}
				ledge = redge; // 更新左边为当前边
			}
			// 处理下一个序列
			siz = seq[r + 1]; // 下一个序列的长度
			l = r + 2; // 下一个序列的起始位置
			r = l + siz - 1; // 下一个序列的结束位置
		}
		return true; // 所有约束都能满足
	}

	/**
	 * 获取链的终点
	 * 
	 * @param edge 起始边
	 * @return 链的终点边，如果存在冲突返回-1
	 * 
	 *         面试要点：
	 *         - 链的遍历
	 *         - 访问状态管理
	 *         - 冲突检测
	 * 
	 *         ML/DL关联：
	 *         - 路径遍历算法
	 *         - 访问状态跟踪
	 */
	public static int getLinkEnd(int edge) {
		while (etoe[edge] != 0) { // 当还有下一个边时
			if (vis[edge] == true) { // 如果已访问过（存在环或冲突）
				return -1; // 返回错误
			}
			vis[edge] = true; // 标记为已访问
			edge = etoe[edge]; // 移动到下一个边
		}
		return edge; // 返回链的终点
	}

	/**
	 * 压缩图，将约束链压缩为单个节点
	 * 检查是否存在可行的欧拉回路
	 * 
	 * 面试要点：
	 * - 图的压缩技术
	 * - 欧拉回路存在条件验证
	 * - 约束传播与简化
	 * 
	 * ML/DL关联：
	 * - 图的层次化表示
	 * - 约束优化问题的简化
	 */
	public static boolean compress() {
		// 遍历所有链头（未被约束连接的边）
		for (int i = 1; i <= m; i++) {
			if (isHead[i]) { // 如果是链头
				int x = a[i]; // 链的起点
				int y = b[i]; // 链的终点
				if (etoe[i] != 0) { // 如果链有后续部分
					int end = getLinkEnd(i); // 获取链的终点
					if (end == -1) { // 如果存在冲突
						return false; // 无法构造
					}
					y = b[end]; // 更新链的真实终点
				}
				// 更新度数
				outDeg[x]++; // 起点出度加1
				inDeg[y]++; // 终点入度加1
				edgeCnt++; // 有效边数加1
				// 添加压缩后的边到图中
				addEdge(x, y, i);
			}
		}
		// 检查欧拉回路存在条件：所有节点入度等于出度
		for (int i = 1; i <= n; i++) {
			if (inDeg[i] != outDeg[i]) { // 如果度数不平衡
				return false; // 不存在欧拉回路
			}
		}
		// 初始化当前边指针
		for (int i = 1; i <= n; i++) {
			cur[i] = headg[i]; // 每个节点从第一条边开始访问
		}
		return true; // 压缩成功，存在欧拉回路
	}

	// 递归版Hierholzer算法，u表示当前节点，h表示来到当前节点的链头
	/**
	 * 递归版Hierholzer算法
	 * 用于寻找欧拉回路
	 * 
	 * @param u 当前节点
	 * @param h 到达当前节点的链头
	 * 
	 *          面试要点：
	 *          - 递归实现的Hierholzer算法
	 *          - 路径记录方法
	 *          - 栈溢出风险
	 * 
	 *          ML/DL关联：
	 *          - 递归神经网络的应用
	 *          - 序列生成的递归方法
	 */
	public static void euler1(int u, int h) {
		// 遍历从当前节点出发的所有未访问边
		for (int e = cur[u]; e > 0; e = cur[u]) {
			cur[u] = nextg[e]; // 更新当前边指针
			euler1(tog[e], chainHead[e]); // 递归访问下一个节点
		}
		path[++cntp] = h; // 将链头加入路径（回溯时）
	}

	// 迭代版Hierholzer算法
	/**
	 * 迭代版Hierholzer算法
	 * 避免递归深度过大导致栈溢出
	 * 
	 * @param node   起始节点
	 * @param chainh 起始链头
	 * 
	 *               面试要点：
	 *               - 迭代实现的Hierholzer算法
	 *               - 显式栈的使用
	 *               - 避免栈溢出的策略
	 * 
	 *               ML/DL关联：
	 *               - 循环神经网络的实现
	 *               - 隐状态的显式管理
	 */
	public static void euler2(int node, int chainh) {
		stacksize = 0; // 初始化栈
		push(node, chainh); // 将起始节点和链头压入栈
		while (stacksize > 0) { // 当栈不为空时
			pop(); // 出栈
			int e = cur[u]; // 获取当前节点的当前边
			if (e > 0) { // 如果还有未访问的边
				cur[u] = nextg[e]; // 更新当前边指针
				push(u, h); // 将当前节点和链头重新压入栈
				push(tog[e], chainHead[e]); // 将下一个节点和链头压入栈
			} else { // 如果没有未访问的边
				path[++cntp] = h; // 将链头加入路径
			}
		}
	}

	/**
	 * 解压缩路径，将链分解为具体的边序列
	 * 
	 * 面试要点：
	 * - 结果重构技术
	 * - 链的展开方法
	 * - 输出格式化
	 * 
	 * ML/DL关联：
	 * - 序列的细化生成
	 * - 层次化输出的展开
	 */
	public static void decompress() {
		ans[++cnta] = 1; // 路径从节点1开始
		for (int i = cntp - 1; i >= 1; i--) { // 遍历路径中的每条链
			int e = path[i]; // 获取链的起始边
			while (e > 0) { // 遍历链中的每条边
				ans[++cnta] = b[e]; // 将边的终点加入结果
				e = etoe[e]; // 移动到下一条边
			}
		}
	}

	/**
	 * 主计算函数
	 * 执行整个算法流程
	 * 
	 * @return 是否存在满足约束的路径
	 * 
	 *         面试要点：
	 *         - 算法流程的整合
	 *         - 错误处理机制
	 *         - 模块化设计
	 * 
	 *         ML/DL关联：
	 *         - 端到端推理流程
	 *         - 多阶段处理管道
	 */
	public static boolean compute() {
		if (!linkEdge()) { // 建立边的链接关系
			return false; // 如果约束无法满足
		}
		if (!compress()) { // 压缩图并检查欧拉回路存在性
			return false; // 如果不存在欧拉回路
		}
		// 使用迭代版Hierholzer算法找欧拉回路
		// euler1(1, -1); // 递归版（可能导致栈溢出）
		euler2(1, -1); // 迭代版（推荐）
		if (cntp != edgeCnt + 1) { // 检查路径长度
			return false; // 如果路径不完整
		}
		decompress(); // 解压缩路径
		return cnta == m + 1; // 检查结果长度是否正确
	}

	/**
	 * 主函数：读取输入、处理数据、输出结果
	 * 
	 * 面试要点：
	 * - 程序整体架构
	 * - 输入输出处理
	 * - 结果输出格式
	 * 
	 * ML/DL关联：
	 * - 推理管道的入口
	 * - 输入预处理和输出后处理
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读取图的基本信息
		n = in.nextInt(); // 节点数
		m = in.nextInt(); // 边数
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt(); // 读取边的起点
			b[i] = in.nextInt(); // 读取边的终点
		}

		// 读取约束序列
		t = in.nextInt(); // 约束序列数
		for (int i = 1, siz, idx = 0; i <= t; i++) {
			siz = in.nextInt(); // 序列长度
			seq[++idx] = siz; // 存储长度
			for (int j = 1; j <= siz; j++) {
				seq[++idx] = in.nextInt(); // 存储序列中的节点
			}
		}

		// 执行计算
		boolean check = compute();
		if (!check) {
			out.println("NIE"); // 无解
		} else {
			out.println("TAK"); // 有解
			for (int i = 1; i <= cnta; i++) {
				out.println(ans[i]); // 输出路径
			}
		}
		out.flush();
		out.close();
	}

	// 读写工具类
	/**
	 * 快速读取器类，用于高效读取输入
	 * 
	 * 面试要点：
	 * - IO优化技术
	 * - 缓冲区机制
	 * - 输入解析算法
	 * 
	 * ML/DL关联：
	 * - 高效数据加载器
	 * - 批处理输入解析
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16]; // 缓冲区
		private int ptr = 0, len = 0; // 当前位置和有效长度
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}