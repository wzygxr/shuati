/*
 * 无向图的欧拉路径，java版
 * 功能：图中给定m条无向边，每条边给出两个端点
 * 目标：如果存在欧拉路径，输出字典序最小的结果，如果不存在打印No
 * 数据范围：节点编号范围[1, 500]，1 <= m <= 1024
 * 测试链接 : https://www.luogu.com.cn/problem/P2731
 * 提交说明：提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 面试要点：
 * - 欧拉路径定义：经过图中每条边恰好一次的路径
 * - 无向图欧拉路径存在条件：
 *   1. 所有点的度数都是偶数（欧拉回路），或
 *   2. 恰好有两个点的度数为奇数（欧拉路径，这两个点为起点和终点）
 * - 无向图与有向图欧拉路径判定的区别
 * - 边的访问标记处理（无向图每条边有两个方向的表示）
 * 
 * ML/DL关联：
 * - 图神经网络中无向图的遍历策略
 * - 对称性约束在图表示学习中的应用
 * - 边特征处理的对称性考虑
 */

// 导入必要的IO和工具包
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
// 导入Arrays数组工具类，提供数组操作方法如排序
// 面试要点：内置工具类的使用，如排序、查找等
// ML/DL关联：数据预处理中的数组操作
import java.util.Arrays;
// 导入Comparator比较器接口，用于自定义排序规则
// 面试要点：比较器的实现和使用场景
// ML/DL关联：排序算法在数据预处理中的应用
import java.util.Comparator;

public class Code02_UndirectedEuler1 {

	/**
	 * 边比较器类，用于对边数组进行排序
	 * 实现Comparator接口，按起点升序、终点升序排序
	 * 面试要点：自定义比较器的实现，用于保证字典序最小
	 * ML/DL关联：排序算法在数据预处理中的应用
	 */
	public static class EdgeCmp implements Comparator<int[]> {
		@Override
		/**
		 * 比较两个边的大小
		 * 
		 * @param e1 第一条边，格式为[start, end, id]
		 * @param e2 第二条边，格式为[start, end, id]
		 * @return 比较结果：如果e1小于e2返回负数，相等返回0，大于返回正数
		 * 
		 *         面试要点：
		 *         - 首先比较起点，起点小的边排在前面
		 *         - 如果起点相同，则比较终点，终点小的边排在前面
		 *         - 这样可以保证字典序最小的结果
		 * 
		 *         ML/DL关联：
		 *         - 排序算法在数据预处理中的重要性
		 *         - 比较函数设计对结果的影响
		 */
		public int compare(int[] e1, int[] e2) {
			// 首先比较边的起点，如果不同则按起点排序
			// 如果起点相同，则按终点排序，确保字典序最小
			return e1[0] != e2[0] ? (e1[0] - e2[0]) : (e1[1] - e2[1]);
		}
	}

	/**
	 * 最大节点数常量，根据题目限制节点编号范围[1, 500]设置
	 * 面试要点：合理估算空间复杂度，避免内存超限
	 * ML/DL关联：超参数选择对模型性能的影响
	 */
	public static int MAXN = 501;
	/**
	 * 最大边数常量，根据题目限制1 <= m <= 1024设置
	 * 面试要点：数组大小预分配策略
	 * ML/DL关联：批次大小(batch size)对训练效率的影响
	 */
	public static int MAXM = 2001;
	/**
	 * 节点数、边数和扩展边数变量
	 * n=500是最大可能节点数
	 * m是实际输入的边数
	 * k是扩展后的边数（每条无向边对应两条有向边）
	 * 面试要点：变量命名的意义和用途
	 * ML/DL关联：超参数管理
	 */
	public static int n = 500, m, k;
	/**
	 * 边数组，存储原始输入的边信息（无向边拆分为两条有向边）
	 * 格式：edgeArr[i][0]为起点，edgeArr[i][1]为终点，edgeArr[i][2]为边ID
	 * 面试要点：无向图转有向图的处理方式
	 * ML/DL关联：无向图的有向表示方法
	 */
	public static int[][] edgeArr = new int[MAXM << 1][3]; // MAXM << 1 等价于 MAXM * 2

	/**
	 * 邻接表头指针数组，用于链式前向星存储图
	 * head[i]表示节点i的第一条出边的索引
	 * 面试要点：图的多种存储方式对比（邻接矩阵vs邻接表vs链式前向星）
	 * ML/DL关联：稀疏图的高效存储方法
	 */
	public static int[] head = new int[MAXN];
	/**
	 * 邻接表边的下一个指针数组，用于链式前向星
	 * nxt[i]表示第i条边的下一条边的索引
	 * 面试要点：链式前向星的实现原理
	 * ML/DL关联：链表结构在图表示中的应用
	 */
	public static int[] nxt = new int[MAXM << 1];
	/**
	 * 邻接表边的目标节点数组，用于链式前向星
	 * to[i]表示第i条边指向的目标节点
	 * 面试要点：链式前向星的空间复杂度O(M)
	 * ML/DL关联：图的邻接关系表示
	 */
	public static int[] to = new int[MAXM << 1];
	/**
	 * 边ID数组，用于追踪原始边
	 * eid[i]表示第i条边对应的原始边ID
	 * 面试要点：边访问标记的处理（避免重复访问同一条无向边）
	 * ML/DL关联：边特征的唯一标识
	 */
	public static int[] eid = new int[MAXM << 1];
	/**
	 * 图的边计数器，用于链式前向星的边编号
	 * 面试要点：边的唯一标识符管理
	 * ML/DL关联：图中边的索引机制
	 */
	public static int cntg;

	/**
	 * 当前节点的当前边指针数组，用于Hierholzer算法
	 * cur[i]表示节点i当前应该访问的下一条边
	 * 面试要点：Hierholzer算法的关键数据结构，避免重复访问边
	 * ML/DL关联：动态规划中的状态转移指针
	 */
	public static int[] cur = new int[MAXN];
	/**
	 * 节点度数数组，deg[i]表示节点i的度数（在无向图中即为度数）
	 * 面试要点：欧拉路径判定定理的核心数据结构
	 * ML/DL关联：节点度数作为图的重要特征
	 */
	public static int[] deg = new int[MAXN];
	/**
	 * 边访问标记数组，用于标记边是否已被访问
	 * vis[i]表示第i条原始边是否已被访问
	 * 面试要点：无向图中避免重复访问同一条边的策略
	 * ML/DL关联：图遍历中的访问状态管理
	 */
	public static boolean[] vis = new boolean[MAXM];

	/**
	 * 欧拉路径结果数组，存储最终的路径节点
	 * 面试要点：路径重构的数据结构
	 * ML/DL关联：序列生成模型的输出结构
	 */
	public static int[] path = new int[MAXM];
	/**
	 * 路径节点计数器，记录路径中节点的数量
	 * 面试要点：计数器在算法中的作用
	 * ML/DL关联：序列长度统计
	 */
	public static int cntp;

	/**
	 * 添加边到链式前向星图结构中
	 * 
	 * @param u  起点
	 * @param v  终点
	 * @param id 原始边的ID
	 * 
	 *           面试要点：
	 *           - 链式前向星的插入操作
	 *           - 时间复杂度：O(1)
	 *           - 空间复杂度：O(M)
	 *           - 无向图转有向图的处理
	 * 
	 *           ML/DL关联：
	 *           - 图构建操作在GNN预处理中的应用
	 *           - 邻接关系的动态更新
	 */
	public static void addEdge(int u, int v, int id) {
		// 将新边插入到邻接表的头部，形成链表结构
		// nxt[++cntg]指向原来的首边，实现链表头插
		nxt[++cntg] = head[u];
		// to[cntg]记录新边的目标节点
		to[cntg] = v;
		// eid[cntg]记录新边对应的原始边ID
		eid[cntg] = id;
		// head[u]更新为新边的索引
		head[u] = cntg;
	}

	/**
	 * 构建图结构，包括排序边、计算度数、构建邻接表
	 * 
	 * 面试要点：
	 * - 输入预处理的重要性
	 * - 多重循环的时间复杂度分析
	 * - 数据结构的构建顺序
	 * - 无向图的特殊处理
	 * 
	 * ML/DL关联：
	 * - 数据预处理管道的设计
	 * - 特征工程中的数据组织
	 */
	public static void connect() {
		// 对边数组按起点、终点排序，保证字典序最小
		Arrays.sort(edgeArr, 1, k + 1, new EdgeCmp());
		// 遍历所有边，按起点分组处理
		for (int l = 1, r = 1; l <= k; l = ++r) {
			// 找到所有具有相同起点的边的区间
			while (r + 1 <= k && edgeArr[l][0] == edgeArr[r + 1][0]) {
				r++;
			}
			// 逆序处理相同起点的边，以保证字典序最小
			for (int i = r, u, v, id; i >= l; i--) {
				// 获取边的起点、终点和ID
				u = edgeArr[i][0];
				v = edgeArr[i][1];
				id = edgeArr[i][2];
				// 更新节点的度数（在无向图中，每条边都会增加起点的度数）
				deg[u]++;
				// 将边添加到图的邻接表中
				addEdge(u, v, id);
			}
		}
		// 初始化每个节点的当前边指针
		for (int i = 1; i <= n; i++) {
			// cur[i]初始化为head[i]，表示从第一条边开始访问
			cur[i] = head[i];
		}
	}

	// 无向图中找到一个起点，去生成欧拉回路 或者 欧拉路径
	/**
	 * 在无向图中找到欧拉路径的起点
	 * 
	 * 面试要点：
	 * - 无向图欧拉路径存在条件的实现
	 * - 度数奇偶性的判断逻辑
	 * - 起点选择的策略（欧拉回路vs欧拉路径）
	 * 
	 * ML/DL关联：
	 * - 图遍历的起始点选择策略
	 * - 序列生成的初始状态设定
	 */
	public static int undirectedStart() {
		// 统计度数为奇数的节点数量
		int odd = 0;
		// 遍历所有节点，统计奇度数节点
		for (int i = 1; i <= n; i++) {
			// 使用位运算检查度数是否为奇数（等价于 deg[i] % 2 == 1）
			if ((deg[i] & 1) == 1) {
				// 增加奇度数节点计数
				odd++;
			}
		}
		// 检查奇度数节点数量是否合法
		// 无向图欧拉路径存在条件：奇度数节点数为0（欧拉回路）或2（欧拉路径）
		if (odd != 0 && odd != 2) {
			// 如果奇度数节点数不是0也不是2，返回-1表示不存在欧拉路径
			return -1;
		}
		// 根据奇度数节点数量确定起点
		for (int i = 1; i <= n; i++) {
			// 如果是欧拉回路（奇度数节点数为0），返回任意有度数的节点
			if (odd == 0 && deg[i] > 0) {
				// 返回有度数的节点作为起点
				return i;
			}
			// 如果是欧拉路径（奇度数节点数为2），返回奇度数节点之一
			if (odd == 2 && (deg[i] & 1) == 1) {
				// 返回奇度数节点作为起点
				return i;
			}
		}
		// 没有找到合适的起点，返回-1
		return -1;
	}

	// Hierholzer算法递归版
	/**
	 * Hierholzer算法递归版本，用于寻找无向图的欧拉路径
	 * 注意：对于大数据，递归版本可能会栈溢出，建议使用迭代版本
	 * 
	 * 面试要点：
	 * - 递归算法的实现原理
	 * - 栈溢出的风险评估
	 * - 递归与迭代的选择
	 * - 无向图中边访问标记的特殊处理
	 * 
	 * ML/DL关联：
	 * - 递归神经网络的深度限制
	 * - 深度优先搜索在图神经网络中的应用
	 */
	public static void euler1(int u) {
		// 遍历从节点u出发的所有未访问边
		for (int e = cur[u]; e > 0; e = cur[u]) {
			// 更新当前边指针，跳过已访问的边
			cur[u] = nxt[e];
			// 检查当前边是否已被访问
			if (!vis[eid[e]]) {
				// 标记当前边为已访问（注意：这里标记的是原始边ID）
				vis[eid[e]] = true;
				// 递归访问下一个节点
				euler1(to[e]);
			}
		}
		// 将当前节点加入路径
		path[++cntp] = u;
	}

	// Hierholzer算法迭代版
	/**
	 * 用于Hierholzer算法迭代版本的栈
	 * 避免递归深度过大导致栈溢出
	 * 
	 * 面试要点：
	 * - 显式栈的使用替代递归
	 * - 空间复杂度优化
	 * - 迭代算法的设计思路
	 * 
	 * ML/DL关联：
	 * - 循环神经网络中的显式状态管理
	 * - 递归展开为循环的技术
	 */
	public static int[] sta = new int[MAXM];
	/**
	 * 栈顶指针，用于Hierholzer算法迭代版本
	 * 
	 * 面试要点：
	 * - 栈的基本操作
	 * - 指针管理技巧
	 * 
	 * ML/DL关联：
	 * - 状态栈在序列建模中的作用
	 */
	public static int top;

	/**
	 * Hierholzer算法迭代版本，用于寻找无向图的欧拉路径
	 * 解决递归版本可能的栈溢出问题
	 * 
	 * 面试要点：
	 * - 迭代算法的实现细节
	 * - DFS的非递归实现
	 * - 时间复杂度：O(M)
	 * - 无向图中边访问标记的特殊处理
	 * 
	 * ML/DL关联：
	 * - 图遍历算法在GNN消息传递中的应用
	 * - 序列生成的非递归方法
	 */
	public static void euler2(int node) {
		// 初始化栈，将起始节点压入栈
		top = 0;
		sta[++top] = node;
		// 当栈不为空时继续处理
		while (top > 0) {
			// 取栈顶节点
			int u = sta[top--];
			// 获取当前节点的当前边
			int e = cur[u];
			// 如果还有未访问的边
			if (e != 0) {
				// 更新当前边指针
				cur[u] = nxt[e];
				// 检查当前边是否已被访问
				if (!vis[eid[e]]) {
					// 标记当前边为已访问（注意：这里标记的是原始边ID）
					vis[eid[e]] = true;
					// 将当前节点重新压入栈（稍后需要输出）
					sta[++top] = u;
					// 将下一个节点压入栈
					sta[++top] = to[e];
				} else {
					// 如果边已被访问，将当前节点重新压入栈
					sta[++top] = u;
				}
			} else {
				// 如果没有未访问的边，将当前节点加入路径
				path[++cntp] = u;
			}
		}
	}

	/**
	 * 主函数：读取输入、处理数据、输出结果
	 * 
	 * 面试要点：
	 * - 程序的整体流程设计
	 * - IO优化的重要性
	 * - 算法模块的组合使用
	 * - 无向图特殊处理的实现
	 * 
	 * ML/DL关联：
	 * - 训练/推理管道的构建
	 * - 数据流的端到端处理
	 */
	public static void main(String[] args) throws Exception {
		// 创建快速读取器，提高IO效率
		FastReader in = new FastReader(System.in);
		// 创建打印写入器，用于格式化输出
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取边数
		m = in.nextInt();
		// 初始化扩展边数计数器
		k = 0;
		// 读取所有边的信息，每条无向边拆分为两条有向边
		for (int i = 1, u, v; i <= m; i++) {
			// 读取边的两个端点
			u = in.nextInt();
			v = in.nextInt();
			// 添加边 u->v，边ID为i
			edgeArr[++k][0] = u;
			edgeArr[k][1] = v;
			edgeArr[k][2] = i;
			// 添加反向边 v->u，边ID也为i（这样两条边对应同一条原始无向边）
			edgeArr[++k][0] = v;
			edgeArr[k][1] = u;
			edgeArr[k][2] = i;
		}
		// 构建图结构
		connect();
		// 寻找欧拉路径的起点
		int start = undirectedStart();
		// 如果不存在欧拉路径
		if (start == -1) {
			// 输出"No"
			out.println("No");
		} else {
			// 使用迭代版Hierholzer算法求解欧拉路径
			// euler1(start); // 递归版（可能栈溢出）
			euler2(start); // 迭代版（推荐）
			// 检查路径长度是否正确（应包含m+1个节点）
			if (cntp != m + 1) {
				// 如果路径长度不正确，说明图不连通
				out.println("No");
			} else {
				// 输出欧拉路径（反向输出，因为是DFS回溯时记录的）
				for (int i = cntp; i >= 1; i--) {
					// 输出路径上的每个节点
					out.println(path[i]);
				}
			}
		}
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	// 读写工具类
	/**
	 * 快速读取器类，用于高效读取输入
	 * 通过缓冲区减少IO操作次数，提高读取速度
	 * 
	 * 面试要点：
	 * - IO优化技术
	 * - 缓冲区机制
	 * - 位运算优化
	 * 
	 * ML/DL关联：
	 * - 数据加载器的性能优化
	 * - 批处理中的缓冲机制
	 */
	static class FastReader {
		/**
		 * 输入缓冲区，用于批量读取数据
		 * 面试要点：缓冲区大小的选择（通常为2的幂次）
		 * ML/DL关联：批次缓冲区的设计
		 */
		private final byte[] buffer = new byte[1 << 16];
		/**
		 * 缓冲区当前指针位置和有效数据长度
		 * 面试要点：双指针技术
		 * ML/DL关联：滑动窗口机制
		 */
		private int ptr = 0, len = 0;
		/**
		 * 输入流对象
		 * 面试要点：装饰器模式的应用
		 * ML/DL关联：数据流管道
		 */
		private final InputStream in;

		/**
		 * 构造函数，初始化输入流
		 * 
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			// 保存输入流引用
			this.in = in;
		}

		/**
		 * 读取下一个字节
		 * 
		 * @return 下一字节的值，如果到达流末尾返回-1
		 * 
		 *         面试要点：
		 *         - 缓冲区管理
		 *         - EOF检测
		 *         - 边界条件处理
		 * 
		 *         ML/DL关联：
		 *         - 序列数据的逐步读取
		 *         - 流式数据处理
		 */
		private int readByte() throws IOException {
			// 如果缓冲区已用完，重新填充
			if (ptr >= len) {
				// 从输入流读取数据到缓冲区
				len = in.read(buffer);
				// 重置指针
				ptr = 0;
				// 检查是否到达流末尾
				if (len <= 0)
					// 返回-1表示已到达流末尾
					return -1;
			}
			// 返回当前字节并移动指针
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 * 
		 * @return 读取的整数值
		 * 
		 *         面试要点：
		 *         - 数字解析算法
		 *         - 符号处理
		 *         - 字符到数字的转换
		 * 
		 *         ML/DL关联：
		 *         - 数据预处理中的类型转换
		 *         - 特征提取中的数值解析
		 */
		int nextInt() throws IOException {
			// 临时变量存储当前字符
			int c;
			// 跳过空白字符，直到找到数字或符号
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 标记是否为负数
			boolean neg = false;
			// 检查符号
			if (c == '-') {
				// 设置负数标志
				neg = true;
				// 读取下一个字符
				c = readByte();
			}
			// 存储数值结果
			int val = 0;
			// 读取数字字符并转换为整数
			while (c > ' ' && c != -1) {
				// 将当前字符转换为数字并累加到结果中
				val = val * 10 + (c - '0');
				// 读取下一个字符
				c = readByte();
			}
			// 根据符号标志返回正数或负数
			return neg ? -val : val;
		}
	}

}