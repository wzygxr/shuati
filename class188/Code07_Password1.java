// 所有可能的密码串，java版
// 给定正数n，表示密码有n位，每一位可能的数字是[0..9]
// 密码有(10^n)个可能性，构造一个字符串，其中的连续子串包含所有可能的密码
// 先保证字符串的长度最短，然后保证字典序尽量的小，返回这个字符串
// 1 <= n <= 6
// 测试链接 : http://poj.org/problem?id=1780
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code07_Password1 {

	// 定义最大节点数量常量，用于数组初始化
	public static int MAXN = 1000002;
	// n表示密码位数，k表示每位可能的数字个数，m表示节点总数
	public static int n, k, m;
	// cur数组记录每个节点当前访问的边的索引
	public static int[] cur = new int[MAXN];
	// path数组存储欧拉路径上的边序列
	public static int[] path = new int[MAXN];
	// cntp用于记录路径数组中的元素数量
	public static int cntp;

	// 用二维数组模拟栈结构，第一列存储节点，第二列存储边
	public static int[][] sta = new int[MAXN][2];
	// u和e分别临时存储栈顶的节点和边
	public static int u, e;
	// stacksize记录栈中元素的数量
	public static int stacksize;

	// 将节点u和边e压入栈中
	public static void push(int u, int e) {
		// 将节点u存储到栈的第0列
		sta[stacksize][0] = u;
		// 将边e存储到栈的第1列
		sta[stacksize][1] = e;
		// 栈大小加1
		stacksize++;
	}

	// 从栈中弹出节点和边
	public static void pop() {
		// 栈大小减1
		stacksize--;
		// 获取栈顶的节点
		u = sta[stacksize][0];
		// 获取栈顶的边
		e = sta[stacksize][1];
	}

	// 准备阶段：初始化参数和数据结构
	public static void prepare(int len, int num) {
		// 设置密码长度
		n = len;
		// 设置每位可能的数字个数
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

	// 使用迭代实现欧拉路径算法，避免递归深度过大
	// 功能：非递归方式实现欧拉路径，适用于大图
	// 面试考点：递归转迭代技巧、显式栈管理
	// 边界条件：栈溢出风险控制
	// ML/DL关联：内存受限环境下的图遍历优化
	public static void euler(int node, int edge) {
		// 将栈大小初始化为0
		stacksize = 0;
		// 将初始节点和边压入栈中
		push(node, edge);
		// 当栈不为空时继续循环
		while (stacksize > 0) {
			// 弹出栈顶元素
			pop();
			// 如果当前节点还有未访问的边
			if (cur[u] < k) {
				// 获取下一条要访问的边，并将cur[u]自增
				int ne = cur[u]++;
				// 将当前节点和边重新压入栈
				push(u, e);
				// 将下一个节点和边压入栈
				push((u * k + ne) % m, ne);
			} else {
				// 将当前边添加到路径中
				path[++cntp] = e;
			}
		}
	}

	// 主函数：读取输入、处理数据、输出结果
	// 功能：处理多个测试用例，每个用例构造包含所有密码的最短字符串
	// 面试考点：de Bruijn序列构造、欧拉路径应用
	// 边界条件：n=1时的特殊处理
	// ML/DL关联：序列建模、状态转移矩阵构建
	public static void main(String[] args) throws Exception {
		// 创建快速读取器，提高IO效率
		FastReader in = new FastReader(System.in);
		// 创建打印写入器，用于格式化输出
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取密码长度
		int len = in.nextInt();
		// 固定数字范围为0-9
		int num = 10;
		// 处理多个测试用例，直到输入为0
		while (len != 0) {
			// 调用准备函数初始化参数和数据结构
			prepare(len, num);
			// 使用迭代版欧拉路径算法生成路径
			euler(0, -1);
			// 输出前缀：n-1个'0'，对应起始节点
			for (int i = 1; i <= n - 1; i++) {
				// 输出'0'字符
				out.print("0");
			}
			// 逆序输出路径中的边，构成最终结果字符串
			for (int i = cntp - 1; i >= 1; i--) {
				// 输出路径中的边
				out.print(path[i]);
			}
			// 换行
			out.println();
			// 读取下一个测试用例
			len = in.nextInt();
		}
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
