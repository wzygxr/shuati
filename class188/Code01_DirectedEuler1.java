/*
 * 有向图的欧拉路径，java版
 * 功能：给定n个点，m条有向边的图，每条边给出两个端点
 * 目标：如果存在欧拉路径，输出字典序最小的结果，如果不存在打印No
 * 数据范围：1 <= n <= 10^5，1 <= m <= 2 * 10^5
 * 测试链接 : https://www.luogu.com.cn/problem/P7771
 * 提交说明：提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 面试要点：
 * - 欧拉路径定义：经过图中每条边恰好一次的路径
 * - 有向图欧拉路径存在条件：
 *   1. 所有点的入度等于出度（欧拉回路），或
 *   2. 仅有一个点出度比入度多1（起点），一个点入度比出度多1（终点），其他点入度等于出度（欧拉路径）
 * - Hierholzer算法：O(E)时间复杂度求解欧拉路径
 * - 边界条件：孤立点、重边、自环等特殊情况
 * 
 * ML/DL关联：
 * - 图神经网络中节点遍历序列的生成
 * - 序列建模中的路径优化问题
 * - 图嵌入学习中的连通性约束
 */

// 引入IOException异常处理类，用于处理输入输出过程中的异常情况
// 面试要点：异常处理机制在程序健壮性中的作用
// ML/DL关联：错误处理机制在模型训练中的重要性
import java.io.IOException;
// 引入InputStream输入流类，用于高效读取输入数据
// 面试要点：流的概念和操作在IO优化中的应用
// ML/DL关联：数据流管道的构建
import java.io.InputStream;
// 引入OutputStreamWriter输出流写入器类，用于包装输出流
// 面试要点：输出流的包装和优化
// ML/DL关联：输出结果的格式化处理
import java.io.OutputStreamWriter;
// 引入PrintWriter打印写入器类，提供格式化输出功能
// 面试要点：输出流的缓冲机制和格式化功能
// ML/DL关联：结果输出的标准化
import java.io.PrintWriter;
// 引入Arrays数组工具类，提供数组操作方法如排序
// 面试要点：内置工具类的使用，如排序、查找等
// ML/DL关联：数据预处理中的数组操作
import java.util.Arrays;
// 引入Comparator比较器接口，用于自定义排序规则
// 面试要点：比较器的实现和使用场景
// ML/DL关联：排序算法在数据预处理中的应用
import java.util.Comparator;

// 定义主类Code01_DirectedEuler1，实现有向图欧拉路径算法
// 面试要点：类的设计原则和封装概念
// ML/DL关联：面向对象编程在算法模块化中的应用
public class Code01_DirectedEuler1 {

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
		 * @param e1 第一条边，格式为[start, end]
		 * @param e2 第二条边，格式为[start, end]
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
	 * 最大节点数常量，根据题目限制1 <= n <= 10^5设置
	 * 面试要点：合理估算空间复杂度，避免内存超限
	 * ML/DL关联：超参数选择对模型性能的影响
	 */
	public static int MAXN = 100001;
	/**
	 * 最大边数常量，根据题目限制1 <= m <= 2 * 10^5设置
	 * 面试要点：数组大小预分配策略
	 * ML/DL关联：批次大小(batch size)对训练效率的影响
	 */
	public static int MAXM = 200002;
	/**
	 * 节点数和边数变量
	 * 面试要点：全局变量的作用域和生命周期
	 * ML/DL关联：超参数管理
	 */
	public static int n, m;
	/**
	 * 边数组，存储原始输入的边信息
	 * 格式：edgeArr[i][0]为起点，edgeArr[i][1]为终点
	 * 面试要点：二维数组的内存布局和访问效率
	 * ML/DL关联：张量形状设计
	 */
	public static int[][] edgeArr = new int[MAXM][2];

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
	public static int[] nxt = new int[MAXM];
	/**
	 * 邻接表边的目标节点数组，用于链式前向星
	 * to[i]表示第i条边指向的目标节点
	 * 面试要点：链式前向星的空间复杂度O(M)
	 * ML/DL关联：图的邻接关系表示
	 */
	public static int[] to = new int[MAXM];
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
	 * 节点出度数组，outDeg[i]表示节点i的出度
	 * 面试要点：欧拉路径判定定理的核心数据结构
	 * ML/DL关联：节点度数作为图的重要特征
	 */
	public static int[] outDeg = new int[MAXN];
	/**
	 * 节点入度数组，inDeg[i]表示节点i的入度
	 * 面试要点：入度出度计算是图论算法的基础
	 * ML/DL关联：有向图中节点的流入流出特征
	 */
	public static int[] inDeg = new int[MAXN];

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
	 * @param u 起点
	 * @param v 终点
	 * 
	 *          面试要点：
	 *          - 链式前向星的插入操作
	 *          - 时间复杂度：O(1)
	 *          - 空间复杂度：O(M)
	 * 
	 *          ML/DL关联：
	 *          - 图构建操作在GNN预处理中的应用
	 *          - 邻接关系的动态更新
	 */
	public static void addEdge(int u, int v) {
		// 将新边插入到邻接表的头部，形成链表结构
		// nxt[++cntg]指向原来的首边，实现链表头插
		nxt[++cntg] = head[u];
		// to[cntg]记录新边的目标节点
		to[cntg] = v;
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
	 * 
	 * ML/DL关联：
	 * - 数据预处理管道的设计
	 * - 特征工程中的数据组织
	 */
	public static void connect() {
		// 对边数组按起点、终点排序，保证字典序最小
		Arrays.sort(edgeArr, 1, m + 1, new EdgeCmp());
		// 遍历所有边，按起点分组处理
		for (int l = 1, r = 1; l <= m; l = ++r) {
			// 找到所有具有相同起点的边的区间
			while (r + 1 <= m && edgeArr[l][0] == edgeArr[r + 1][0]) {
				r++;
			}
			// 逆序处理相同起点的边，以保证字典序最小
			for (int i = r, u, v; i >= l; i--) {
				// 获取边的起点和终点
				u = edgeArr[i][0];
				v = edgeArr[i][1];
				// 更新节点的出入度
				outDeg[u]++;
				inDeg[v]++;
				// 将边添加到图的邻接表中
				addEdge(u, v);
			}
		}
		// 初始化每个节点的当前边指针
		for (int i = 1; i <= n; i++) {
			// cur[i]初始化为head[i]，表示从第一条边开始访问
			cur[i] = head[i];
		}
	}

	// 有向图中找到一个起点，去生成欧拉回路 或者 欧拉路径
	/**
	 * 在有向图中找到欧拉路径的起点
	 * 
	 * 面试要点：
	 * - 欧拉路径存在条件的实现
	 * - 度数差的判断逻辑
	 * - 起点选择的策略
	 * 
	 * ML/DL关联：
	 * - 图遍历的起始点选择策略
	 * - 序列生成的初始状态设定
	 */
	public static int directedStart() {
		// 记录起点和终点（出度比入度多1的点和入度比出度多1的点）
		int start = -1, end = -1;
		// 遍历所有节点，检查度数差
		for (int i = 1; i <= n; i++) {
			// 计算当前节点的出度减入度
			int v = outDeg[i] - inDeg[i];
			// 检查度数差是否合法（只能是-1, 0, 1）
			if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
				// 如果度数差不合法，返回-1表示不存在欧拉路径
				return -1;
			}
			// 如果出度比入度多1，这是起点
			if (v == 1) {
				start = i;
			}
			// 如果入度比出度多1，这是终点
			if (v == -1) {
				end = i;
			}
		}
		// 检查起点和终点的配对情况（要么都是-1，要么都不是-1）
		if ((start == -1) ^ (end == -1)) {
			// 只有一个存在而另一个不存在，不符合欧拉路径条件
			return -1;
		}
		// 如果找到了起点（欧拉路径），返回起点
		if (start != -1) {
			return start;
		}
		// 如果没有起点和终点（欧拉回路），返回任意有出边的节点
		for (int i = 1; i <= n; i++) {
			// 如果节点有出边，它可以作为欧拉回路的起点
			if (outDeg[i] > 0) {
				return i;
			}
		}
		// 没有找到合适的起点，返回-1
		return -1;
	}

	// Hierholzer算法递归版，java会爆栈，C++可以通过
	/**
	 * Hierholzer算法递归版本，用于寻找欧拉路径
	 * 注意：对于大数据，递归版本可能会栈溢出，建议使用迭代版本
	 * 
	 * 面试要点：
	 * - 递归算法的实现原理
	 * - 栈溢出的风险评估
	 * - 递归与迭代的选择
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
			// 递归访问下一个节点
			euler1(to[e]);
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
	 * Hierholzer算法迭代版本，用于寻找欧拉路径
	 * 解决递归版本可能的栈溢出问题
	 * 
	 * 面试要点：
	 * - 迭代算法的实现细节
	 * - DFS的非递归实现
	 * - 时间复杂度：O(M)
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
				// 将当前节点重新压入栈（稍后需要输出）
				sta[++top] = u;
				// 将下一个节点压入栈
				sta[++top] = to[e];
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
		// 读取节点数和边数
		n = in.nextInt();
		m = in.nextInt();
		// 读取所有边的信息
		for (int i = 1; i <= m; i++) {
			// 读取每条边的起点和终点
			edgeArr[i][0] = in.nextInt();
			edgeArr[i][1] = in.nextInt();
		}
		// 构建图结构
		connect();
		// 寻找欧拉路径的起点
		int start = directedStart();
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
					out.print(path[i] + " ");
				}
				// 换行
				out.println();
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