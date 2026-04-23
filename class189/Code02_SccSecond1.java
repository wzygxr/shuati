package class189; // 声明当前类所在的包名为class189

// 强连通分量模版题2，java版
// 给定一张n个点，m条边的有向图
// 求出所有强连通分量，先打印强连通分量的数量
// 每个强连通分量先打印大小，然后打印节点编号，顺序随意
// 1 <= n <= 5 * 10^4
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/U224391
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入IO异常处理类
import java.io.InputStream; // 导入输入流类
import java.io.OutputStreamWriter; // 导入输出流写入器类
import java.io.PrintWriter; // 导入打印写入器类

/**
 * Code02_SccSecond1 类 - 强连通分量算法模板实现（第二题）
 * 
 * 【题目特点】
 * 相比第一题，本题数据范围更大（n <= 5 * 10^4）
 * 输出要求不同：每个强连通分量先输出大小，再输出节点列表
 * 
 * 【核心算法】Tarjan算法 - 基于DFS的一次遍历找出所有强连通分量
 * 【时间复杂度】O(V + E)
 * 【空间复杂度】O(V)
 */
public class Code02_SccSecond1 {

	// ==================== 常量定义区域 ====================
	
	/**
	 * MAXN常量定义图中最大节点数
	 * 根据题目约束 n <= 5 * 10^4，设置数组大小为50001
	 * 索引0不使用，节点编号从1开始
	 */
	public static int MAXN = 50001;
	
	/**
	 * MAXM常量定义图中最大边数
	 * 根据题目约束 m <= 10^5，设置数组大小为100001
	 */
	public static int MAXM = 100001;
	
	// ==================== 图的基本信息 ====================
	
	/**
	 * n变量存储实际节点数
	 * m变量存储实际边数
	 */
	public static int n, m;

	// ==================== 邻接表数据结构 ====================
	
	/**
	 * head数组：邻接表头指针数组
	 * head[i]表示节点i的第一条边在边数组中的索引位置
	 */
	public static int[] head = new int[MAXN];
	
	/**
	 * nxt数组：邻接表的链式存储结构
	 * nxt[i]表示第i条边的下一条边在边数组中的索引
	 */
	public static int[] nxt = new int[MAXM];
	
	/**
	 * to数组：存储每条边指向的目标节点
	 */
	public static int[] to = new int[MAXM];
	
	/**
	 * cntg变量：边计数器
	 * 记录当前已添加的边的总数
	 */
	public static int cntg;

	// ==================== Tarjan算法核心数据结构 ====================
	
	/**
	 * dfn数组：DFS序数组
	 * dfn[i]表示节点i被DFS访问的时间戳
	 */
	public static int[] dfn = new int[MAXN];
	
	/**
	 * low数组：Low Link Value数组
	 * low[i]表示从节点i出发能够到达的所有节点中最小的dfn值
	 */
	public static int[] low = new int[MAXN];
	
	/**
	 * cntd变量：DFS时间戳计数器
	 */
	public static int cntd;

	/**
	 * sta数组：Tarjan算法使用的栈
	 */
	public static int[] sta = new int[MAXN];
	
	/**
	 * top变量：栈顶指针
	 */
	public static int top;

	// ==================== 强连通分量结果存储 ====================
	
	/**
	 * belong数组：记录每个节点所属的强连通分量编号
	 */
	public static int[] belong = new int[MAXN];
	
	/**
	 * sccArr数组：存储所有强连通分量中的节点
	 */
	public static int[] sccArr = new int[MAXN];
	
	/**
	 * sccSiz数组：记录每个强连通分量的大小（包含的节点数量）
	 * 本题需要输出每个强连通分量的大小
	 */
	public static int[] sccSiz = new int[MAXN];
	
	/**
	 * sccl数组：记录每个强连通分量在sccArr中的起始位置
	 */
	public static int[] sccl = new int[MAXN];
	
	/**
	 * sccr数组：记录每个强连通分量在sccArr中的结束位置
	 */
	public static int[] sccr = new int[MAXN];
	
	/**
	 * idx变量：sccArr数组的索引计数器
	 */
	public static int idx;
	
	/**
	 * sccCnt变量：强连通分量的数量
	 */
	public static int sccCnt;

	// ==================== 迭代版Tarjan算法辅助数据结构 ====================
	
	/**
	 * stack数组：模拟递归调用栈的二维数组
	 * 每行存储一个三元组 (u, status, e)
	 */
	public static int[][] stack = new int[MAXN][3];
	
	/**
	 * u变量：当前处理的节点
	 * status变量：当前处理状态
	 * e变量：当前处理的边索引
	 */
	public static int u, status, e;
	
	/**
	 * stacksize变量：模拟栈的当前大小
	 */
	public static int stacksize;

	// ==================== 迭代版辅助方法 ====================
	
	/**
	 * 将三元组(u, status, e)压入模拟栈
	 * 
	 * @param u 当前处理的节点
	 * @param status 处理状态
	 * @param e 当前边索引
	 */
	public static void push(int u, int status, int e) {
		// 将u存入栈顶位置的第一个元素
		stack[stacksize][0] = u;
		// 将status存入栈顶位置的第二个元素
		stack[stacksize][1] = status;
		// 将e存入栈顶位置的第三个元素
		stack[stacksize][2] = e;
		// 栈大小加1
		stacksize++;
	}

	/**
	 * 从模拟栈中弹出栈顶元素
	 */
	public static void pop() {
		// 栈大小减1
		stacksize--;
		// 恢复u、status、e的值
		u = stack[stacksize][0];
		status = stack[stacksize][1];
		e = stack[stacksize][2];
	}

	// ==================== 邻接表操作 ====================
	
	/**
	 * 添加边的方法
	 * 
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		// 为新边分配索引，cntg先自增
		nxt[++cntg] = head[u];
		// 设置边的目标节点
		to[cntg] = v;
		// 更新头指针
		head[u] = cntg;
	}

	// ==================== 递归版Tarjan算法 ====================
	
	/**
	 * 递归版本Tarjan算法
	 * 
	 * 【执行流程】
	 * 1. 初始化dfn和low
	 * 2. 节点入栈
	 * 3. 遍历邻接边
	 * 4. 判断是否为强连通分量根
	 * 
	 * @param u 当前节点
	 */
	public static void tarjan1(int u) {
		// 初始化dfn和low为当前时间戳
		dfn[u] = low[u] = ++cntd;
		// 将当前节点压入栈
		sta[++top] = u;
		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取邻接节点
			int v = to[e];
			// 如果v未被访问
			if (dfn[v] == 0) {
				// 递归处理子节点
				tarjan1(v);
				// 用子节点的low更新当前节点的low
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 如果v在栈中（回边）
				if (belong[v] == 0) {
					// 用v的dfn更新low
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}
		// 检查是否形成强连通分量
		if (dfn[u] == low[u]) {
			// 强连通分量计数加1
			sccCnt++;
			// 记录起始位置
			sccl[sccCnt] = idx + 1;
			// 临时变量，存储弹出的节点
			int pop;
			// 弹出栈中节点直到u
			do {
				// 弹出栈顶
				pop = sta[top--];
				// 标记所属强连通分量
				belong[pop] = sccCnt;
				// 加入结果数组
				sccArr[++idx] = pop;
				// 当前强连通分量大小加1
				sccSiz[sccCnt]++;
			} while (pop != u);
			// 记录结束位置
			sccr[sccCnt] = idx;
		}
	}

	// ==================== 迭代版Tarjan算法 ====================
	
	/**
	 * 迭代版本Tarjan算法
	 * 
	 * 【状态说明】
	 * status = -1：节点刚被访问
	 * status = 0：刚从子节点返回，需要更新low
	 * status = 1：遇到回边，需要更新low
	 * 
	 * @param node 起始节点
	 */
	public static void tarjan2(int node) {
		// 初始化栈大小
		stacksize = 0;
		// 压入初始节点
		push(node, -1, -1);
		// 临时变量v
		int v;
		// 当栈不为空时继续
		while (stacksize > 0) {
			// 弹出栈顶
			pop();
			// 根据status处理
			if (status == -1) {
				// 第一次访问该节点
				// 初始化dfn和low
				dfn[u] = low[u] = ++cntd;
				// 节点入栈
				sta[++top] = u;
				// 获取第一条邻接边
				e = head[u];
			} else {
				// 从子节点返回
				// 获取邻接节点
				v = to[e];
				// status = 0表示从子节点返回
				if (status == 0) {
					// 用子节点的low更新
					low[u] = Math.min(low[u], low[v]);
				}
				// status = 1且v在栈中，表示回边
				if (status == 1 && belong[v] == 0) {
					// 用v的dfn更新
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 移动到下一条边
				e = nxt[e];
			}
			// 如果还有未处理的边
			if (e != 0) {
				// 获取邻接节点
				v = to[e];
				// 如果v未被访问
				if (dfn[v] == 0) {
					// 压入当前节点，状态0表示待子节点返回后处理
					push(u, 0, e);
					// 压入子节点，状态-1表示初次访问
					push(v, -1, -1);
				} else {
					// v已访问，压入当前节点，状态1表示处理回边
					push(u, 1, e);
				}
			} else {
				// 所有边处理完毕
				// 检查是否形成强连通分量
				if (dfn[u] == low[u]) {
					// 强连通分量计数加1
					sccCnt++;
					// 记录起始位置
					sccl[sccCnt] = idx + 1;
					// 临时变量
					int pop;
					// 弹出栈中节点直到u
					do {
						// 弹出栈顶
						pop = sta[top--];
						// 标记所属强连通分量
						belong[pop] = sccCnt;
						// 加入结果数组
						sccArr[++idx] = pop;
						// 当前强连通分量大小加1
						sccSiz[sccCnt]++;
					} while (pop != u);
					// 记录结束位置
					sccr[sccCnt] = idx;
				}
			}
		}
	}

	// ==================== 主程序入口 ====================
	
	/**
	 * 主函数
	 * 
	 * 【执行流程】
	 * 1. 读取输入
	 * 2. 构建图
	 * 3. 运行Tarjan算法
	 * 4. 输出结果
	 * 
	 * @param args 命令行参数
	 * @throws Exception 异常
	 */
	public static void main(String[] args) throws Exception {
		// 创建快速读取器
		FastReader in = new FastReader(System.in);
		// 创建打印写入器
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取节点数
		n = in.nextInt();
		// 读取边数
		m = in.nextInt();
		// 读取所有边
		for (int i = 1, u, v; i <= m; i++) {
			// 读取起点
			u = in.nextInt();
			// 读取终点
			v = in.nextInt();
			// 添加边
			addEdge(u, v);
		}
		// 对所有未访问节点运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			// 如果节点i未被访问
			if (dfn[i] == 0) {
				// 使用迭代版本
				tarjan2(i);
			}
		}
		// 输出强连通分量数量
		out.println(sccCnt);
		// 输出每个强连通分量的信息
		for (int i = 1; i <= sccCnt; i++) {
			// 先输出强连通分量的大小
			out.print(sccSiz[i] + " ");
			// 输出该强连通分量中的所有节点
			for (int j = sccl[i]; j <= sccr[i]; j++) {
				// 输出节点编号
				out.print(sccArr[j] + " ");
			}
			// 换行
			out.println();
		}
		// 刷新缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	// ==================== 快速输入类 ====================
	
	/**
	 * FastReader类 - 快速读取器
	 * 
	 * 【特点】
	 * 使用缓冲区批量读取，提高输入效率
	 */
	static class FastReader {
		/**
		 * buffer数组：字节缓冲区
		 * 大小为65536字节（64KB）
		 */
		private final byte[] buffer = new byte[1 << 16];
		
		/**
		 * ptr：缓冲区读取指针
		 * len：缓冲区有效数据长度
		 */
		private int ptr = 0, len = 0;
		
		/**
		 * in：输入流对象
		 */
		private final InputStream in;

		/**
		 * 构造函数
		 * 
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 读取下一个字节
		 * 
		 * @return 字节的整数值，文件结束返回-1
		 * @throws IOException IO异常
		 */
		private int readByte() throws IOException {
			// 检查是否需要重新填充缓冲区
			if (ptr >= len) {
				// 从输入流读取数据
				len = in.read(buffer);
				// 重置指针
				ptr = 0;
				// 检查是否读取到数据
				if (len <= 0)
					// 文件结束
					return -1;
			}
			// 返回当前字节并移动指针
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 * 
		 * @return 整数值
		 * @throws IOException IO异常
		 */
		int nextInt() throws IOException {
			// 当前字符
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 标记是否为负数
			boolean neg = false;
			// 检查负号
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			// 数值结果
			int val = 0;
			// 读取数字字符
			while (c > ' ' && c != -1) {
				// 累加数字
				val = val * 10 + (c - '0');
				// 读取下一个字符
				c = readByte();
			}
			// 返回结果
			return neg ? -val : val;
		}
	}

}
