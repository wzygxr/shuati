/*
 * 无序字母对，java版
 * 问题描述：给定n个字母对，每个字母对有两个字母，字母只可能是A~Z、a~z
 * 字母对内部不区分顺序，ab和ba是相同的，但是区分大小写，Ab和ab是不同的
 * 题目不会给定重复的字母对，构造一个长度n+1的字符串，让每个字母对都出现
 * 一个字母对的两个字母，在字符串中相邻出现即可，输出字典序最小的方案
 * 如果没有满足要求的方案，打印"No Solution"
 * 数据范围：1 <= n <= 1326
 * 测试链接 : https://www.luogu.com.cn/problem/P1341
 * 
 * 面试要点：
 * - 无向图欧拉路径问题
 * - 字符到节点的映射
 * - 度数奇偶性检验
 * - Hierholzer算法求解欧拉路径
 * 
 * ML/DL关联：
 * - 字符序列生成中的约束满足问题
 * - 图神经网络在字符处理中的应用
 * - 序列建模中的路径优化
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_LetterPairs1 {

	/**
	 * 最大节点数常量，对应52个字母（A-Z, a-z）加1
	 * 面试要点：合理的空间预分配
	 * ML/DL关联：批次大小对内存的影响
	 */
	public static int MAXN = 53;
	/**
	 * 最大边数常量，根据题目限制设置
	 * 面试要点：数组大小预分配策略
	 * ML/DL关联：批次大小(batch size)对训练效率的影响
	 */
	public static int MAXM = 2001;
	/**
	 * 节点数（52个字母：A-Z, a-z）和边数
	 * 面试要点：全局变量的作用域和生命周期
	 * ML/DL关联：超参数管理
	 */
	public static int n = 52, m;
	/**
	 * 字母对数组，存储原始输入的字母对信息
	 * 面试要点：二维数组的内存布局和访问效率
	 * ML/DL关联：字符对的张量表示
	 */
	public static char[][] arr = new char[MAXM][2];

	/**
	 * 邻接矩阵，表示图中节点之间的连接关系
	 * graph[i][j]表示节点i和节点j之间有多少条边
	 * 面试要点：图的邻接矩阵表示法
	 * ML/DL关联：图的邻接矩阵在GNN中的应用
	 */
	public static int[][] graph = new int[MAXN][MAXN];
	/**
	 * 度数数组，deg[i]表示节点i的度数
	 * 面试要点：无向图中节点度数统计
	 * ML/DL关联：节点度数作为图的重要特征
	 */
	public static int[] deg = new int[MAXN];
	/**
	 * 当前节点的当前边指针数组，用于Hierholzer算法
	 * cur[i]表示节点i当前应该访问的下一个节点
	 * 面试要点：Hierholzer算法的关键数据结构，避免重复访问边
	 * ML/DL关联：动态规划中的状态转移指针
	 */
	public static int[] cur = new int[MAXN];

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
	 * 将字符转换为对应的节点编号
	 * 大写字母A-Z映射到1-26，小写字母a-z映射到27-52
	 * 
	 * @param c 输入字符
	 * @return 节点编号
	 * 
	 *         面试要点：
	 *         - 字符到数字的映射
	 *         - ASCII码运算
	 *         - 节点编号规范化
	 * 
	 *         ML/DL关联：
	 *         - 字符编码在NLP中的应用
	 *         - 特征映射方法
	 */
	public static int getInt(char c) {
		// 如果是大写字母，映射到1-26；如果是小写字母，映射到27-52
		return c <= 'Z' ? (c - 'A' + 1) : (c - 'a' + 27);
	}

	/**
	 * 将节点编号转换为对应的字符
	 * 节点编号1-26映射到大写字母A-Z，节点编号27-52映射到小写字母a-z
	 * 
	 * @param v 节点编号
	 * @return 对应字符
	 * 
	 *         面试要点：
	 *         - 数字到字符的映射
	 *         - ASCII码运算
	 *         - 节点编号反向转换
	 * 
	 *         ML/DL关联：
	 *         - 字符解码在NLP中的应用
	 *         - 特征反向映射
	 */
	public static char getChar(int v) {
		// 如果节点编号在1-26范围内，返回大写字母；否则返回小写字母
		return (char) (v <= 26 ? ('A' + v - 1) : ('a' + v - 27));
	}

	/**
	 * 构建图结构，包括添加边和计算度数
	 * 
	 * 面试要点：
	 * - 无向图的构建过程
	 * - 度数统计的重要性
	 * - 邻接矩阵的动态构建
	 * 
	 * ML/DL关联：
	 * - 图构建在GNN中的预处理步骤
	 * - 度数特征在图学习中的作用
	 */
	public static void connect() {
		// 遍历所有字母对，构建图结构
		for (int i = 1, u, v; i <= m; i++) {
			// 获取字母对对应的节点编号
			u = getInt(arr[i][0]);
			v = getInt(arr[i][1]);
			// 在邻接矩阵中增加边（无向图，所以两个方向都要加）
			graph[u][v]++;
			graph[v][u]++;
			// 更新节点的度数
			deg[u]++;
			deg[v]++;
		}
		// 初始化每个节点的当前边指针
		for (int i = 1; i <= n; i++) {
			// cur[i]初始化为1，表示从节点1开始访问
			cur[i] = 1;
		}
	}

	/**
	 * 在无向图中找到欧拉路径的起点
	 * 
	 * 面试要点：
	 * - 无向图欧拉路径存在条件的实现
	 * - 度数奇偶性的判断逻辑
	 * - 起点选择的策略
	 * 
	 * ML/DL关联：
	 * - 图遍历的起始点选择策略
	 * - 序列生成的初始状态设定
	 */
	public static int undirectedStart() {
		// 统计度数为奇数的节点数量
		int odd = 0;
		for (int i = 1; i <= n; i++) {
			// 使用位运算检查度数是否为奇数
			if ((deg[i] & 1) == 1) {
				odd++;
			}
		}
		// 检查奇数度数节点的数量是否合法（只能是0或2）
		if (odd != 0 && odd != 2) {
			// 如果奇数度数节点数量不是0或2，不存在欧拉路径
			return -1;
		}
		// 根据奇数度数节点的数量选择起点
		for (int i = 1; i <= n; i++) {
			// 如果没有奇数度数节点（欧拉回路），返回任意有边的节点
			if (odd == 0 && deg[i] > 0) {
				return i;
			}
			// 如果有两个奇数度数节点（欧拉路径），返回其中一个
			if (odd == 2 && (deg[i] & 1) == 1) {
				return i;
			}
		}
		// 没有找到合适的起点，返回-1
		return -1;
	}

	/**
	 * Hierholzer算法，用于寻找无向图的欧拉路径
	 * 
	 * @param u 当前节点
	 * 
	 *          面试要点：
	 *          - 无向图Hierholzer算法的实现
	 *          - 边的访问状态管理
	 *          - 路径记录方法
	 * 
	 *          ML/DL关联：
	 *          - 图遍历算法在GNN中的应用
	 *          - 序列生成的递归方法
	 */
	public static void euler(int u) {
		// 遍历从节点u出发的所有未访问边
		for (int v = cur[u]; v <= n; v = cur[u]) {
			// 更新当前节点的访问位置
			cur[u]++;
			// 检查节点u和v之间是否有边
			if (graph[u][v] > 0) {
				// 标记边已访问（减少边的数量）
				graph[u][v]--;
				graph[v][u]--;
				// 递归访问下一个节点
				euler(v);
			}
		}
		// 将当前节点加入路径
		path[++cntp] = u;
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
		// 读取字母对数量
		m = in.nextInt();
		// 读取所有字母对
		for (int i = 1; i <= m; i++) {
			// 读取第i个字母对的第一个字母
			arr[i][0] = in.nextChar();
			// 读取第i个字母对的第二个字母
			arr[i][1] = in.nextChar();
		}
		// 构建图结构
		connect();
		// 寻找欧拉路径的起点
		int start = undirectedStart();
		// 如果不存在欧拉路径
		if (start == -1) {
			// 输出"No Solution"
			out.println("No Solution");
		} else {
			// 使用Hierholzer算法求解欧拉路径
			euler(start);
			// 检查路径长度是否正确（应包含m+1个节点）
			if (cntp != m + 1) {
				// 如果路径长度不正确，说明图不连通
				out.println("No Solution");
			} else {
				// 输出欧拉路径（反向输出，因为是DFS回溯时记录的）
				for (int i = cntp; i >= 1; i--) {
					// 输出路径上的每个字符
					out.print(getChar(path[i]));
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

	/**
	 * 快速读取器类，用于高效读取输入
	 * 通过缓冲区减少IO操作次数，提高读取速度
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

		/**
		 * 读取下一个字符（大写或小写字母）
		 * 
		 * @return 读取的字符
		 * 
		 *         面试要点：
		 *         - 字符解析算法
		 *         - 字符范围检查
		 *         - 字符过滤
		 * 
		 *         ML/DL关联：
		 *         - 字符数据预处理
		 *         - 序列数据的解析
		 */
		char nextChar() throws IOException {
			// 临时变量存储当前字符
			int c;
			// 跳过非字母字符，直到找到大写或小写字母
			do {
				c = readByte();
				// 如果到达流末尾，返回0
				if (c == -1)
					return 0;
			} while (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')));
			// 返回找到的字母字符
			return (char) c;
		}

	}

}
