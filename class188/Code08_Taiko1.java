// 太鼓达人，java版
// 给定一个正数n，所有长度为n的二进制状态一共有(2^n)个
// 构造一个字符串，字符串可以循环使用，其中的连续子串包含所有二进制状态
// 求出字符串的最小长度值，并且给出字典序最小的方案
// 比如n=3，字符串最小长度值为8，字典序最小的方案为00010111
// 注意到 000、001、010、101、011、111、110、100 都已包含
// 注意到 最后两个二进制状态 是字符串循环使用构造出来的
// 1 <= n <= 11
// 本题可以推广到k进制，代码就是按照推广来实现的
// 测试链接 : https://www.luogu.com.cn/problem/P10950
// 测试链接 : https://loj.ac/p/10110
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code08_Taiko1 {

	// 定义最大节点数量常量，用于数组初始化
	public static int MAXN = 3001;
	// n表示二进制状态长度，k表示进制数，m表示节点总数
	public static int n, k, m;

	// cur数组记录每个节点当前访问的边的索引
	public static int[] cur = new int[MAXN];
	// path数组存储欧拉路径上的边序列
	public static int[] path = new int[MAXN];
	// cntp用于记录路径数组中的元素数量
	public static int cntp;

	// 准备阶段：初始化参数和数据结构
	// 功能：设置二进制状态长度、进制数并初始化相关数组
	// 面试考点：de Bruijn序列构造的参数初始化
	// 边界条件：n=1时的特殊处理
	// ML/DL关联：序列建模的初始化策略
	public static void prepare(int len, int num) {
		// 设置二进制状态长度
		n = len;
		// 设置进制数
		k = num;
		// 初始化节点总数为1
		m = 1;
		// 计算节点总数，即k^(n-1)
		for (int i = 1; i <= n - 1; i++) {
			// 每次乘以k，得到k^(n-1)
			m *= k;
		}
		// 初始化cur数组，记录每个节点当前访问的边的索引
		for (int i = 0; i < m; i++) {
			// 将每个节点的边索引初始化为0
			cur[i] = 0;
		}
		// 将路径计数器初始化为0
		cntp = 0;
	}

	// 使用递归实现欧拉路径算法
	// 功能：找到有向图中的欧拉路径，解决太鼓达人问题
	// 面试考点：欧拉路径/回路的存在性判断、Hierholzer算法
	// 边界条件：考虑重边、自环等特殊情况
	// ML/DL关联：图神经网络中路径遍历的应用
	public static void euler(int u, int e) {
		// 当当前节点u还有未访问的边时继续循环
		while (cur[u] < k) {
			// 获取下一条要访问的边，并将cur[u]自增
			int ne = cur[u]++;
			// 递归调用，计算下一个节点并访问该边
			euler((u * k + ne) % m, ne);
		}
		// 将当前边添加到路径中
		path[++cntp] = e;
	}

	// 主函数：读取输入、处理数据、输出结果
	// 功能：处理太鼓达人问题，构造包含所有二进制状态的最短字符串
	// 面试考点：de Bruijn序列构造、欧拉路径应用
	// 边界条件：n=1时的特殊处理
	// ML/DL关联：序列建模、状态转移矩阵构建
	public static void main(String[] args) throws Exception {
		// 创建快速读取器，提高IO效率
		FastReader in = new FastReader(System.in);
		// 创建打印写入器，用于格式化输出
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取二进制状态长度
		int len = in.nextInt();
		// 固定为二进制
		int num = 2;
		// 调用准备函数初始化参数和数据结构
		prepare(len, num);
		// 使用递归版欧拉路径算法生成路径
		euler(0, -1);
		// 输出字符串最小长度
		out.print((m * k) + " ");
		// 输出前缀：n-1个'0'，对应起始节点
		for (int i = 1; i <= n - 1; i++) {
			// 输出'0'字符
			out.print("0");
		}
		// 逆序输出路径中的边，构成最终结果字符串
		for (int i = cntp - 1; i >= n; i--) {
			// 输出路径中的边
			out.print(path[i]);
		}
		// 换行
		out.println();
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	// 读写工具类
	// 功能：提供高效的输入输出操作
	// 面试考点：IO优化技术、缓冲区机制
	// 边界条件：EOF处理、异常处理
	// ML/DL关联：高效数据加载器、批处理输入解析
	static class FastReader {
		// 输入缓冲区，用于批量读取数据
		private final byte[] buffer = new byte[1 << 16];
		// 缓冲区当前指针位置和有效数据长度
		private int ptr = 0, len = 0;
		// 输入流对象
		private final InputStream in;

		// 构造函数，初始化输入流
		FastReader(InputStream in) {
			// 保存输入流引用
			this.in = in;
		}

		// 读取下一个字节
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

		// 读取下一个整数
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