package class189; // 声明当前类所在的包名为class189，用于组织和管理类文件

// 强连通分量模版题1，java版
// 给定一张n个点，m条边的有向图
// 求出所有强连通分量，先打印强连通分量的数量
// 然后打印1号点所在的强连通分量，然后打印2号点所在的强连通分量，以此类推
// 如果当前节点属于之前的强连通分量，那就跳过，直到打印所有强连通分量
// 打印每个强连通分量时，按照节点编号从小到大打印
// 1 <= n <= 10^4
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/B3609
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException; // 导入Java标准库中的IO异常处理类，用于处理输入输出过程中可能出现的异常情况
import java.io.InputStream; // 导入Java标准库中的输入流抽象类，是所有输入流的基类
import java.io.OutputStreamWriter; // 导入Java标准库中的输出流写入器类，用于将字符转换为字节并写入输出流
import java.io.PrintWriter; // 导入Java标准库中的打印写入器类，提供便捷的打印输出方法
import java.util.Arrays; // 导入Java标准库中的数组工具类，提供各种数组操作方法如排序、查找等

/**
 * Code01_SccFirst1 类 - 强连通分量(SCC)算法模板实现
 * 
 * 【核心算法】Tarjan算法 - 基于DFS的一次遍历找出所有强连通分量
 * 【时间复杂度】O(V + E)，其中V是顶点数，E是边数
 * 【空间复杂度】O(V)，用于存储DFS序、low值、栈等辅助数据结构
 * 
 * 【算法原理详解】
 * 1. dfn数组：记录每个节点被DFS访问的时间戳（发现顺序）
 * 2. low数组：记录从当前节点出发能够回溯到的最早祖先节点的dfn值
 * 3. 栈：维护当前DFS路径上的节点，用于识别强连通分量
 * 
 * 【关键判定条件】
 * 当 dfn[u] == low[u] 时，说明找到了一个强连通分量的根节点
 * 此时从栈中弹出节点直到u，这些节点构成一个强连通分量
 */
public class Code01_SccFirst1 {

	// ==================== 常量定义区域 ====================
	
	/**
	 * MAXN常量定义图中最大节点数
	 * 根据题目约束 n <= 10^4，设置数组大小为10001
	 * 索引0不使用，节点编号从1开始
	 */
	public static int MAXN = 10001;
	
	/**
	 * MAXM常量定义图中最大边数
	 * 根据题目约束 m <= 10^5，设置数组大小为100001
	 * 同样索引0不使用，边编号从1开始
	 */
	public static int MAXM = 100001;
	
	// ==================== 图的基本信息 ====================
	
	/**
	 * n变量存储实际节点数，从输入读取
	 * m变量存储实际边数，从输入读取
	 */
	public static int n, m;

	// ==================== 邻接表数据结构 ====================
	
	/**
	 * head数组：邻接表头指针数组
	 * head[i]表示节点i的第一条边在边数组中的索引位置
	 * 初始值为0表示没有邻接边
	 */
	public static int[] head = new int[MAXN];
	
	/**
	 * nxt数组：邻接表的链式存储结构
	 * nxt[i]表示第i条边的下一条边在边数组中的索引
	 * 使用链式前向星结构实现邻接表，节省空间且遍历高效
	 */
	public static int[] nxt = new int[MAXM];
	
	/**
	 * to数组：存储每条边指向的目标节点
	 * to[i]表示第i条边指向的节点编号
	 */
	public static int[] to = new int[MAXM];
	
	/**
	 * cntg变量：边计数器
	 * 记录当前已添加的边的总数，用于给新边分配索引
	 * 从1开始计数，0表示无效边
	 */
	public static int cntg;

	// ==================== Tarjan算法核心数据结构 ====================
	
	/**
	 * dfn数组：DFS序数组（Discovery Time / Depth First Number）
	 * dfn[i]表示节点i被DFS访问的时间戳（发现顺序）
	 * 初始值为0表示该节点尚未被访问
	 * 时间戳从1开始递增
	 */
	public static int[] dfn = new int[MAXN];
	
	/**
	 * low数组：Low Link Value数组
	 * low[i]表示从节点i出发，通过树边、回边、横叉边能够到达的
	 * 所有节点中最小的dfn值
	 * 用于判断节点是否为强连通分量的根
	 */
	public static int[] low = new int[MAXN];
	
	/**
	 * cntd变量：DFS时间戳计数器
	 * 每次访问一个新节点时，cntd自增1并赋值给该节点的dfn
	 * 记录DFS访问节点的顺序
	 */
	public static int cntd;

	/**
	 * sta数组：Tarjan算法使用的栈
	 * 存储当前DFS搜索路径上的所有节点
	 * 用于在找到强连通分量时弹出属于同一分量的所有节点
	 */
	public static int[] sta = new int[MAXN];
	
	/**
	 * top变量：栈顶指针
	 * 指向栈中最后一个元素的位置
	 * top=0表示栈为空
	 */
	public static int top;

	// ==================== 强连通分量结果存储 ====================
	
	/**
	 * belong数组：记录每个节点所属的强连通分量编号
	 * belong[i]表示节点i属于第几个强连通分量
	 * 初始值为0表示尚未分配强连通分量
	 */
	public static int[] belong = new int[MAXN];
	
	/**
	 * sccArr数组：存储所有强连通分量中的节点
	 * 按强连通分量编号顺序连续存储各分量的节点
	 * 配合sccl和sccr数组使用
	 */
	public static int[] sccArr = new int[MAXN];
	
	/**
	 * sccl数组：记录每个强连通分量在sccArr中的起始位置（左边界）
	 * sccl[i]表示第i个强连通分量的第一个节点在sccArr中的索引
	 */
	public static int[] sccl = new int[MAXN];
	
	/**
	 * sccr数组：记录每个强连通分量在sccArr中的结束位置（右边界）
	 * sccr[i]表示第i个强连通分量的最后一个节点在sccArr中的索引
	 */
	public static int[] sccr = new int[MAXN];
	
	/**
	 * idx变量：sccArr数组的索引计数器
	 * 记录sccArr中已存储的节点总数
	 * 用于给新加入的强连通分量节点分配位置
	 */
	public static int idx;
	
	/**
	 * sccCnt变量：强连通分量的数量
	 * 记录图中找到的强连通分量总数
	 */
	public static int sccCnt;

	/**
	 * sccPrint数组：标记数组
	 * sccPrint[i]表示第i个强连通分量是否已经输出过
	 * 用于避免重复输出同一个强连通分量
	 */
	public static boolean[] sccPrint = new boolean[MAXN];

	// ==================== 邻接表操作 ====================
	
	/**
	 * 添加边的方法 - 使用链式前向星实现邻接表
	 * 
	 * 【算法步骤】
	 * 1. cntg自增，为新边分配索引
	 * 2. 将新边的next指针指向当前head[u]（头插法）
	 * 3. 设置新边的目标节点为v
	 * 4. 更新head[u]指向新边
	 * 
	 * @param u 边的起点节点编号
	 * @param v 边的终点节点编号
	 */
	public static void addEdge(int u, int v) {
		// cntg先自增，为新边分配唯一的索引编号（从1开始）
		nxt[++cntg] = head[u];
		// 设置这条边指向的目标节点为v
		to[cntg] = v;
		// 更新节点u的邻接链表头指针，使其指向新加入的边
		// 使用头插法，新边总是插入到链表头部
		head[u] = cntg;
	}

	// ==================== 递归版Tarjan算法 ====================
	
	/**
	 * 递归版本Tarjan算法 - 寻找强连通分量的核心方法
	 * 
	 * 【核心思想】
	 * 通过一次DFS遍历，利用dfn和low数组识别强连通分量
	 * 
	 * 【执行流程】
	 * 1. 初始化当前节点的dfn和low值为当前时间戳
	 * 2. 将当前节点压入栈中
	 * 3. 遍历所有邻接边：
	 *    - 树边（未访问）：递归处理，用子节点的low更新当前节点的low
	 *    - 回边（在栈中）：用邻接节点的dfn更新当前节点的low
	 *    - 横叉边/前向边（已出栈）：忽略
	 * 4. 如果dfn[u] == low[u]，说明u是强连通分量的根，弹出栈中节点构成SCC
	 * 
	 * @param u 当前正在处理的节点编号
	 */
	public static void tarjan1(int u) {
		// 初始化当前节点的DFS序为当前时间戳（先自增再赋值）
		// 同时初始化low值为当前时间戳
		dfn[u] = low[u] = ++cntd;
		// 将当前节点压入栈中，++top先自增再赋值
		// 表示该节点在当前DFS搜索路径上
		sta[++top] = u;
		// 遍历当前节点u的所有邻接边
		// 从head[u]开始，通过nxt数组遍历整个邻接链表
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 获取当前边e指向的目标节点v
			int v = to[e];
			// 判断节点v的访问状态
			if (dfn[v] == 0) { // 如果dfn[v]为0，说明v尚未被访问，e是树边
				// 递归调用tarjan处理子节点v，继续深度优先搜索
				tarjan1(v);
				// 子节点v处理完毕后，用子节点的low值更新当前节点的low值
				// 取最小值是因为low表示能回溯到的最早祖先
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 如果节点v已经被访问过，需要判断v是否在当前栈中
				// belong[v] == 0 表示v仍在栈中（尚未被分配到任何SCC）
				if (belong[v] == 0) { // 回边情况：v在栈中，说明v是u的祖先
					// 用v的dfn值更新u的low值
					// 因为v能被u到达，且v是u的祖先，所以u可以通过v回溯到更早的节点
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 如果belong[v] != 0，说明v已经被弹出栈并分配到某个SCC
				// 这是横叉边或前向边，不更新low值
			}
		}
		// 处理完所有邻接边后，判断当前节点u是否为强连通分量的根
		// 当dfn[u] == low[u]时，说明u无法回溯到比它更早的祖先
		// 因此u是当前强连通分量的根节点
		if (dfn[u] == low[u]) {
			// 发现一个新的强连通分量，计数器加1
			sccCnt++;
			// 记录当前强连通分量在sccArr数组中的起始位置
			// idx+1是因为idx指向最后一个已存储的位置
			sccl[sccCnt] = idx + 1;
			// 声明临时变量pop，用于存储从栈中弹出的节点
			int pop;
			// 从栈中弹出节点，直到弹出当前节点u
			// 这些弹出的节点构成一个完整的强连通分量
			do {
				// 弹出栈顶节点，top先取值再自减
				pop = sta[top--];
				// 标记弹出的节点属于当前找到的强连通分量
				belong[pop] = sccCnt;
				// 将弹出的节点加入强连通分量结果数组
				sccArr[++idx] = pop;
			} while (pop != u); // 继续循环直到弹出当前节点u
			// 记录当前强连通分量在sccArr数组中的结束位置
			sccr[sccCnt] = idx;
		}
	}

	// ==================== 迭代版Tarjan算法（非递归） ====================
	
	/**
	 * 迭代版本需要的模拟递归栈
	 * 用于将递归算法改写为非递归形式，避免栈溢出
	 * 
	 * 【存储结构】
	 * 每行是一个三元组 (u, status, e)：
	 * - u：当前处理的节点
	 * - status：处理状态（-1未开始，0处理完子节点，1处理回边）
	 * - e：当前处理的边索引
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
	 * 表示栈中元素的数量
	 */
	public static int stacksize;

	/**
	 * 将三元组(u, status, e)压入模拟栈的方法
	 * 
	 * 【参数说明】
	 * @param u 当前处理的节点编号
	 * @param status 处理状态：
	 *               -1：节点刚被访问，未处理任何子节点
	 *                0：正在处理某个子节点（子节点DFS完成后返回）
	 *                1：遇到回边（目标节点已访问且在栈中）
	 * @param e 当前处理的边索引
	 */
	public static void push(int u, int status, int e) {
		// 将节点u存入栈顶位置的第一个元素（索引0）
		stack[stacksize][0] = u;
		// 将状态status存入栈顶位置的第二个元素（索引1）
		stack[stacksize][1] = status;
		// 将边索引e存入栈顶位置的第三个元素（索引2）
		stack[stacksize][2] = e;
		// 栈大小加1，指向下一个空闲位置
		stacksize++;
	}

	/**
	 * 从模拟栈中弹出栈顶元素，并恢复相关变量值
	 * 
	 * 【执行流程】
	 * 1. 栈大小减1
	 * 2. 从栈顶位置恢复u、status、e的值
	 */
	public static void pop() {
		// 栈大小减1，指向栈顶元素
		// 注意：这里先减1，所以下面直接用stacksize访问
		stacksize--;
		// 从栈顶元素恢复节点u的值
		u = stack[stacksize][0];
		// 从栈顶元素恢复状态值
		status = stack[stacksize][1];
		// 从栈顶元素恢复边索引
		e = stack[stacksize][2];
	}

	/**
	 * 迭代版本Tarjan算法实现
	 * 
	 * 【状态说明】
	 * status = -1：节点u刚被访问，未处理任何邻接边
	 * status = 0：节点u的某个子节点v已完成DFS，需要更新low[u]
	 * status = 1：节点u遇到回边，需要更新low[u]
	 * 
	 * 【与递归版的区别】
	 * 递归版使用函数调用栈，迭代版使用显式栈模拟递归过程
	 * 适用于数据规模大、递归深度过深可能导致栈溢出的场景
	 * 
	 * @param node 起始节点编号
	 */
	public static void tarjan2(int node) {
		// 初始化模拟栈大小为0，表示栈为空
		stacksize = 0;
		// 将初始节点压入栈，状态为-1表示刚访问未处理子节点
		// 边为-1表示初始状态，无特定边
		push(node, -1, -1);
		// 声明临时变量v，用于存储邻接节点
		int v;
		// 当栈不为空时，继续处理
		// 循环模拟递归的展开和回溯过程
		while (stacksize > 0) {
			// 弹出栈顶元素，恢复u、status和e的值
			pop();
			// 根据status的值判断当前处理阶段
			if (status == -1) {
				// status == -1表示这是第一次访问该节点
				// 执行递归版中进入节点时的初始化操作
				// 设置当前节点的DFS序和low值为当前时间戳
				dfn[u] = low[u] = ++cntd;
				// 将当前节点压入Tarjan算法的工作栈
				sta[++top] = u;
				// 获取当前节点的第一条邻接边
				// head[u]为0表示没有邻接边
				e = head[u];
			} else {
				// status != -1表示这是从子节点返回后的处理
				// 获取当前边指向的节点v
				v = to[e];
				// 根据status的值判断是哪种类型的返回
				if (status == 0) {
					// status == 0表示刚从子节点v返回
					// 子节点v的DFS已完成，用子节点的low值更新当前节点的low值
					// 对应递归版中的 low[u] = Math.min(low[u], low[v])
					low[u] = Math.min(low[u], low[v]);
				}
				// 如果status == 1且目标节点v仍在栈中
				// 这是回边的情况，需要用v的dfn更新u的low
				if (status == 1 && belong[v] == 0) {
					// 使用目标节点v的DFS序更新当前节点u的low值
					// 对应递归版中的回边处理
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 处理完当前边后，移动到下一条邻接边
				e = nxt[e];
			}
			// 判断当前节点是否还有未处理的边
			if (e != 0) {
				// 如果还有未处理的边，获取当前边指向的节点v
				v = to[e];
				// 判断目标节点v是否已被访问
				if (dfn[v] == 0) {
					// 如果目标节点v还未被访问过，这是树边
					// 需要先处理子节点v，再返回到当前节点u
					// 
					// 将当前节点信息压栈，状态设为0
					// 状态0表示：待子节点v处理完毕后，需要用v的low更新u的low
					push(u, 0, e);
					// 将子节点v压栈，状态设为-1
					// 状态-1表示：v是第一次被访问，需要初始化dfn和low
					push(v, -1, -1);
				} else {
					// 如果目标节点v已被访问过
					// 直接压栈当前节点，状态设为1
					// 状态1表示：处理回边，需要用v的dfn更新u的low
					// 注意：这里不需要将v压栈，因为v已经处理过
					push(u, 1, e);
				}
			} else {
				// 如果所有邻接边都处理完毕（e == 0）
				// 检查是否形成强连通分量
				// 这是递归版中for循环结束后的判断
				if (dfn[u] == low[u]) {
					// 强连通分量数量加1
					sccCnt++;
					// 记录当前强连通分量在结果数组中的起始位置
					sccl[sccCnt] = idx + 1;
					// 声明临时变量用于存储弹出的节点
					int pop;
					// 从栈中弹出节点，直到弹出当前节点u
					// 这些节点构成一个强连通分量
					do {
						// 弹出栈顶节点
						pop = sta[top--];
						// 标记弹出节点属于当前强连通分量
						belong[pop] = sccCnt;
						// 将弹出节点加入强连通分量结果数组
						sccArr[++idx] = pop;
					} while (pop != u); // 循环直到弹出当前节点u
					// 记录当前强连通分量在结果数组中的结束位置
					sccr[sccCnt] = idx;
				}
			}
		}
	}

	// ==================== 主程序入口 ====================
	
	/**
	 * 主函数 - 程序入口点
	 * 
	 * 【执行流程】
	 * 1. 读取输入数据（节点数n和边数m）
	 * 2. 构建图的邻接表
	 * 3. 对所有未访问节点运行Tarjan算法找强连通分量
	 * 4. 对每个强连通分量内的节点排序
	 * 5. 按节点编号顺序输出每个强连通分量
	 * 
	 * @param args 命令行参数（未使用）
	 * @throws Exception 可能抛出的异常
	 */
	public static void main(String[] args) throws Exception {
		// 创建快速读取器实例，用于高效读取输入数据
		// FastReader使用缓冲区读取，比Scanner更快
		FastReader in = new FastReader(System.in);
		// 创建打印写入器实例，用于输出结果
		// PrintWriter使用缓冲输出，比System.out.print更高效
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 从输入读取节点数n
		n = in.nextInt();
		// 从输入读取边数m
		m = in.nextInt();
		// 循环读取所有边的信息并构建图
		// i从1到m，读取m条边
		for (int i = 1, u, v; i <= m; i++) {
			// 读取第i条边的起点u
			u = in.nextInt();
			// 读取第i条边的终点v
			v = in.nextInt();
			// 向图中添加从u到v的有向边
			addEdge(u, v);
		}
		// 对所有未访问过的节点运行Tarjan算法
		// 注意：图可能不连通，需要遍历所有节点
		for (int i = 1; i <= n; i++) {
			// 如果节点i的dfn为0，说明尚未被访问
			if (dfn[i] == 0) {
				// 调用迭代版本寻找强连通分量
				// 递归版本tarjan1(i)在大数据时可能栈溢出
				tarjan2(i);
			}
		}
		// 输出强连通分量的总数
		out.println(sccCnt);
		// 对每个强连通分量内的节点按编号升序排序
		// 题目要求输出时每个分量内的节点按从小到大排序
		for (int i = 1; i <= sccCnt; i++) {
			// Arrays.sort对sccArr数组的指定范围排序
			// 排序范围是从sccl[i]到sccr[i]（包含）
			Arrays.sort(sccArr, sccl[i], sccr[i] + 1);
		}
		// 按节点编号顺序输出每个强连通分量
		// 题目要求：先打印1号点所在的强连通分量，然后打印2号点所在的...
		for (int i = 1; i <= n; i++) {
			// 获取节点i所属的强连通分量编号
			int scc = belong[i];
			// 检查该强连通分量是否已经输出过
			if (!sccPrint[scc]) {
				// 标记该强连通分量已输出
				sccPrint[scc] = true;
				// 遍历该强连通分量中的所有节点并输出
				for (int j = sccl[scc]; j <= sccr[scc]; j++) {
					// 输出节点编号，后跟空格
					out.print(sccArr[j] + " ");
				}
				// 换行，完成一个强连通分量的输出
				out.println();
			}
		}
		// 刷新输出缓冲区，确保所有数据都写入输出流
		out.flush();
		// 关闭输出流，释放资源
		out.close();
	}

	// ==================== 快速输入类 ====================
	
	/**
	 * FastReader类 - 快速读取器
	 * 
	 * 【设计目的】
	 * Java的Scanner类在处理大量输入时较慢
	 * FastReader使用缓冲区批量读取，显著提高输入效率
	 * 
	 * 【实现原理】
	 * 1. 使用byte数组作为缓冲区，批量读取输入数据
	 * 2. 自定义解析方法，直接处理字节数据
	 * 3. 避免使用字符串分割等耗时操作
	 */
	static class FastReader {
		/**
		 * buffer数组：字节缓冲区
		 * 大小为1 << 16 = 65536字节 = 64KB
		 * 用于批量存储从输入流读取的数据
		 */
		private final byte[] buffer = new byte[1 << 16];
		
		/**
		 * ptr变量：缓冲区读取指针
		 * 指向当前要读取的字节位置
		 */
		private int ptr = 0;
		
		/**
		 * len变量：缓冲区有效数据长度
		 * 表示buffer数组中实际存储的有效字节数
		 */
		private int len = 0;
		
		/**
		 * in变量：输入流对象
		 * 存储传入的输入流，通常是System.in
		 */
		private final InputStream in;

		/**
		 * 构造函数，初始化输入流
		 * 
		 * @param in 输入流对象，通常是System.in
		 */
		FastReader(InputStream in) {
			// 保存传入的输入流对象
			this.in = in;
		}

		/**
		 * 从输入流读取下一个字节的方法
		 * 
		 * 【执行流程】
		 * 1. 检查当前读取指针是否超出缓冲区有效范围
		 * 2. 如果是，从输入流读取一批数据到缓冲区
		 * 3. 返回当前指针处的字节，并将指针后移
		 * 
		 * @return 下一个字节的整数值（0-255），文件结束返回-1
		 * @throws IOException 读取过程中可能抛出的IO异常
		 */
		private int readByte() throws IOException {
			// 检查当前读取指针是否超出缓冲区有效范围
			// ptr >= len表示缓冲区中的数据已经读取完毕
			if (ptr >= len) {
				// 从输入流中读取一批数据到缓冲区
				// read(byte[])方法返回实际读取的字节数
				len = in.read(buffer);
				// 重置读取指针到缓冲区开头
				ptr = 0;
				// 检查是否读取到数据
				// len <= 0表示输入流已到达末尾或读取失败
				if (len <= 0)
					// 返回-1表示文件结束（EOF）
					return -1;
			}
			// 返回当前指针处的字节（转换为无符号整数）
			// 同时将指针ptr后移1位
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数的方法
		 * 
		 * 【算法步骤】
		 * 1. 跳过所有空白字符（空格、换行、制表符等）
		 * 2. 判断是否为负数
		 * 3. 连续读取数字字符，累加计算整数值
		 * 4. 根据符号返回结果
		 * 
		 * 【支持的格式】
		 * - 可选的前导空白字符
		 * - 可选的负号
		 * - 连续的数字字符
		 * 
		 * @return 读取到的整数值
		 * @throws IOException 读取过程中可能抛出的IO异常
		 */
		int nextInt() throws IOException {
			// 声明变量c，存储当前读取的字符（字节）
			int c;
			// 跳过空白字符直到遇到非空白字符或文件结束
			// 空白字符包括：空格(' ')、制表符('\t')、换行('\n')、回车('\r')等
			do {
				// 读取下一个字节
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 声明布尔变量neg，标记是否为负数
			// 默认为false，表示正数
			boolean neg = false;
			// 检查是否遇到负号
			if (c == '-') {
				// 标记为负数
				neg = true;
				// 读取下一个字符（应该是数字）
				c = readByte();
			}
			// 声明变量val，存储数值结果
			// 初始值为0
			int val = 0;
			// 继续读取数字字符直到遇到非数字字符
			// c > ' '确保不是空白字符
			// c != -1确保不是文件结束
			while (c > ' ' && c != -1) {
				// 将字符转换为数字并累加到结果中
				// (c - '0')将字符'0'-'9'转换为整数0-9
				// val * 10将已有数字左移一位（乘以10）
				val = val * 10 + (c - '0');
				// 读取下一个字符
				c = readByte();
			}
			// 根据符号返回结果
			// 如果是负数，返回-val；否则返回val
			return neg ? -val : val;
		}
	}

}
