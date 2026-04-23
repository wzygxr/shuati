package class193;

/**
 * 点双连通分量模板题2，Java版
 * 题目描述：给定一张无向图，包含n个顶点和m条边
 * 要求：
 * 1. 忽略所有孤立点，打印点双连通分量的个数
 * 2. 打印每个点双连通分量内部的节点编号，编号按照从小到大组织
 * 3. 内部节点编号是一个序列，序列字典序小的点双连通分量先打印
 * 数据范围：1 <= n <= 5 * 10^4，1 <= m <= 3 * 10^5
 * 测试链接：https://www.luogu.com.cn/problem/B3610
 * 提交说明：提交时请把类名改成"Main"，可以通过所有测试用例
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Code05_VBCC1 {

	/**
	 * 最大顶点数，题目中n最大为50000
	 */
	public static int MAXN = 50001;
	
	/**
	 * 最大边数，题目中m最大为300000
	 */
	public static int MAXM = 300001;
	
	/**
	 * 顶点数n和边数m
	 */
	public static int n, m;

	/**
	 * 邻接表表头数组，head[u]表示以u为起点的第一条边的索引
	 */
	public static int[] head = new int[MAXN];
	
	/**
	 * 邻接表下一条边索引数组，nxt[e]表示与边e同起点的下一条边的索引
	 */
	public static int[] nxt = new int[MAXM << 1];
	
	/**
	 * 邻接表边的终点数组，to[e]表示边e的终点
	 */
	public static int[] to = new int[MAXM << 1];
	
	/**
	 * 边计数器，记录当前已添加的边数
	 */
	public static int cntg;

	/**
	 * dfn数组，记录每个顶点的深度优先搜索时间戳
	 */
	public static int[] dfn = new int[MAXN];
	
	/**
	 * low数组，记录每个顶点能通过非父子边回溯到的最早祖先的时间戳
	 */
	public static int[] low = new int[MAXN];
	
	/**
	 * 时间戳计数器，记录当前的时间戳
	 */
	public static int cntd;

	/**
	 * 顶点栈，用于存储当前路径上的顶点
	 */
	public static int[] sta = new int[MAXN];
	
	/**
	 * 栈顶指针
	 */
	public static int top;

	/**
	 * vbccArr数组，存储所有点双连通分量的节点
	 */
	public static List<List<Integer>> vbccArr = new ArrayList<>();

	/**
	 * 迭代版需要的栈，用于模拟递归过程
	 * 每个元素是一个三元组：(当前顶点u, 状态status, 边索引e)
	 */
	public static int[][] stack = new int[MAXN][3];
	
	/**
	 * 当前顶点u
	 */
	public static int u;
	
	/**
	 * 状态status：-1表示首次访问，0表示递归返回，1表示回边
	 */
	public static int status;
	
	/**
	 * 边索引e
	 */
	public static int e;
	
	/**
	 * 迭代栈的大小
	 */
	public static int stacksize;

	/**
	 * 将元素压入迭代栈
	 * @param u 当前顶点
	 * @param status 状态
	 * @param e 边索引
	 */
	public static void push(int u, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = status;
		stack[stacksize][2] = e;
		stacksize++;
	}

	/**
	 * 从迭代栈弹出元素
	 */
	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		status = stack[stacksize][1];
		e = stack[stacksize][2];
	}

	/**
	 * VbccCmp类，用于比较两个点双连通分量的字典序
	 */
	public static class VbccCmp implements Comparator<List<Integer>> {

		/**
		 * 比较两个点双连通分量的字典序
		 * @param o1 第一个点双连通分量
		 * @param o2 第二个点双连通分量
		 * @return 比较结果，负数表示o1字典序小于o2，正数表示o1字典序大于o2，0表示相等
		 */
		@Override
		public int compare(List<Integer> o1, List<Integer> o2) {
			int size = Math.min(o1.size(), o2.size());
			for (int i = 0; i < size; i++) {
				if (!o1.get(i).equals(o2.get(i))) {
					return o1.get(i).compareTo(o2.get(i));
				}
			}
			return o1.size() - o2.size();
		}

	}

	/**
	 * 向邻接表中添加一条边
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];  // 将新边的nxt指向当前head[u]
		to[cntg] = v;  // 设置新边的终点为v
		head[u] = cntg;  // 更新head[u]为新边的索引
	}

	/**
	 * 递归版Tarjan算法求解点双连通分量
	 * @param u 当前访问的顶点
	 */
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
		sta[++top] = u;  // 将当前顶点压入顶点栈
		
		// 遍历当前顶点的所有邻边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];  // 获取边的终点
			
			if (dfn[v] == 0) {  // 如果v未被访问过
				tarjan1(v);  // 递归访问v
				low[u] = Math.min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
				
				// 如果low[v] == dfn[u]，说明v无法通过非父子边回溯到u的祖先
				if (low[v] == dfn[u]) {
					ArrayList<Integer> list = new ArrayList<>();  // 创建新的点双连通分量列表
					list.add(u);  // 将u添加到点双连通分量列表
					
					// 弹出顶点栈中的顶点，直到弹出v
					int pop;
					do {
						pop = sta[top--];  // 弹出顶点栈顶元素
						list.add(pop);  // 将弹出的顶点添加到点双连通分量列表
					} while (pop != v);  // 直到弹出v为止
					
					vbccArr.add(list);  // 将点双连通分量列表添加到vbccArr数组
				}
			} else {  // 如果v已被访问过，说明是回边
				low[u] = Math.min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
			}
		}
	}

	/**
	 * 迭代版Tarjan算法求解点双连通分量
	 * @param node 起始顶点
	 */
	public static void tarjan2(int node) {
		stacksize = 0;  // 初始化迭代栈大小为0
		push(node, -1, -1);  // 将起始顶点压入迭代栈
		int v;
		
		while (stacksize > 0) {  // 迭代栈不为空
			pop();  // 弹出迭代栈顶元素
			
			if (status == -1) {  // 首次访问顶点u
				dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
				sta[++top] = u;  // 将当前顶点压入顶点栈
				e = head[u];  // 获取当前顶点的第一条边
			} else {  // 非首次访问顶点u
				v = to[e];  // 获取边的终点
				
				if (status == 0) {  // 递归返回
					low[u] = Math.min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
					
					// 如果low[v] == dfn[u]，说明v无法通过非父子边回溯到u的祖先
					if (low[v] == dfn[u]) {
						ArrayList<Integer> list = new ArrayList<>();  // 创建新的点双连通分量列表
						list.add(u);  // 将u添加到点双连通分量列表
						
						// 弹出顶点栈中的顶点，直到弹出v
						int pop;
						do {
							pop = sta[top--];  // 弹出顶点栈顶元素
							list.add(pop);  // 将弹出的顶点添加到点双连通分量列表
						} while (pop != v);  // 直到弹出v为止
						
						vbccArr.add(list);  // 将点双连通分量列表添加到vbccArr数组
					}
				} else {  // 回边
					low[u] = Math.min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
				}
				e = nxt[e];  // 获取下一条边
			}
			
			if (e != 0) {  // 还有未处理的边
				v = to[e];  // 获取边的终点
				
				if (dfn[v] == 0) {  // 如果v未被访问过
					push(u, 0, e);  // 将当前顶点压入迭代栈，状态为0（递归返回）
					push(v, -1, -1);  // 将v压入迭代栈，状态为-1（首次访问）
				} else {  // 如果v已被访问过，说明是回边
					push(u, 1, e);  // 将当前顶点压入迭代栈，状态为1（回边）
				}
			}
		}
	}

	/**
	 * 主函数，程序入口
	 * @param args 命令行参数
	 * @throws Exception 可能抛出的异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);  // 创建FastReader对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));  // 创建PrintWriter对象
		
		n = in.nextInt();  // 读取顶点数n
		m = in.nextInt();  // 读取边数m
		
		// 读取m条边并添加到邻接表
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();  // 读取边的起点u
			v = in.nextInt();  // 读取边的终点v
			addEdge(u, v);  // 添加边u->v
			addEdge(v, u);  // 添加边v->u（无向图）
		}
		
		// 遍历所有顶点，处理每个连通分量
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0 && head[i] > 0) {  // 如果顶点i未被访问过且不是孤立点
				// tarjan1(i);  // 调用递归版Tarjan算法
				tarjan2(i);  // 调用迭代版Tarjan算法
			}
		}
		
		out.println(vbccArr.size());  // 输出点双连通分量的个数
		
		// 对每个点双连通分量内部的节点进行排序
		for (int i = 0; i < vbccArr.size(); i++) {
			vbccArr.get(i).sort((a, b) -> a.compareTo(b));
		}
		
		// 对点双连通分量进行字典序排序
		vbccArr.sort(new VbccCmp());
		
		// 输出每个点双连通分量的节点
		for (int i = 0; i < vbccArr.size(); i++) {
			for (int node : vbccArr.get(i)) {
				out.print(node + " ");  // 输出节点编号
			}
			out.println();  // 换行
		}
		
		out.flush();  // 刷新输出缓冲区
		out.close();  // 关闭输出流
	}

	/**
	 * FastReader类，用于快速读取输入
	 */
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];  // 输入缓冲区
		private int ptr = 0, len = 0;  // 缓冲区指针和长度
		private final InputStream in;  // 输入流

		/**
		 * FastReader构造函数
		 * @param in 输入流
		 */
		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 读取一个字节
		 * @return 读取的字节
		 * @throws IOException 可能抛出的IO异常
		 */
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);  // 从输入流读取数据到缓冲区
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];  // 返回缓冲区中的字节
		}

		/**
		 * 读取一个整数
		 * @return 读取的整数
		 * @throws IOException 可能抛出的IO异常
		 */
		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();  // 读取字节，跳过空白字符
			} while (c <= ' ' && c != -1);
			
			boolean neg = false;
			if (c == '-') {  // 处理负数
				neg = true;
				c = readByte();
			}
			
			int val = 0;
			while (c > ' ' && c != -1) {  // 读取数字字符
				val = val * 10 + (c - '0');  // 计算整数值
				c = readByte();
			}
			
			return neg ? -val : val;  // 返回整数，负数则取反
		}
	}

}
