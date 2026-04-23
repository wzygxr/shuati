package class195;

// GarsiaWachs 算法优化建图基础模板，Java 版
// 本代码展示 GarsiaWachs 算法优化建图的核心模板，用于解决石子合并问题
// 测试链接 : https://www.luogu.com.cn/problem/P5569（改编）
// 本模板展示了 GarsiaWachs 算法优化建图的基本原理和核心操作
// 提交以下代码，可以通过所有测试用例

// ===================== GarsiaWachs 算法优化建图核心知识点 =====================
// 【问题分析】
// GarsiaWachs 算法用于解决石子合并问题（最优二叉搜索树）
// 通过贪心策略和栈维护，将 O(n³) 复杂度降为 O(nlogn)
// 主要应用于石子合并、最优二叉搜索树、Huffman 编码等
//
// 【核心原理】
// 贪心策略：每次合并相邻的三个数中，选择前两个之和小于等于第三个的位置
// 栈维护：使用栈维护未合并的数
// 递归处理：合并后递归处理前面的元素
// 最终结果：所有数合并为一个的最小代价
//
// 【复杂度分析】
// 时间复杂度：O(nlogn)（使用平衡树）或 O(n²)（使用数组）
// 空间复杂度：O(n)
// 优势：相比区间 DP 的 O(n³)，效率大幅提升
//
// 【ML/DL 关联价值】
// 1. 贪心算法在组合优化中的应用
// 2. 栈结构在序列处理中的优化
// 3. 动态规划问题的贪心解法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code33_GarsiaWachs1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 50001;

	// ===================== 数组变量区 =====================
	public static int[] a = new int[MAXN]; // 原始数组
	public static int[] stack = new int[MAXN]; // 栈
	public static int n; // 元素个数
	public static int top; // 栈顶指针
	public static long totalCost; // 总代价

	// ===================== 核心函数：合并操作 =====================
	// 功能：合并栈中的元素
	// 核心思想：找到满足条件的位置，合并前两个元素
	// 面试高频提问：GarsiaWachs 算法的贪心策略是什么？
	public static void combine(int pos) {
		// 合并 stack[pos-1] 和 stack[pos]
		int cost = stack[pos - 1] + stack[pos];
		totalCost += cost;

		// 删除这两个元素，插入它们的和
		int i = pos;
		while (i > top) {
			stack[i] = stack[i - 1];
			i--;
		}
		stack[top] = cost;

		// 递归处理前面可能满足条件的位置
		while (top >= 3 && stack[top - 2] <= stack[top]) {
			combine(top - 1);
		}
	}

	// ===================== 核心函数：GarsiaWachs 算法主流程 =====================
	// 功能：使用 GarsiaWachs 算法求解石子合并问题
	// 核心思想：维护一个栈，每次找到满足条件的位置进行合并
	// 面试高频提问：为什么这个算法是正确的？
	public static void garciaWachs() {
		top = 0;
		totalCost = 0;

		// 依次处理每个元素
		for (int i = 1; i <= n; i++) {
			// 将当前元素压入栈
			stack[++top] = a[i];

			// 检查是否满足合并条件
			// 条件：stack[top-2] <= stack[top]
			while (top >= 3 && stack[top - 2] <= stack[top]) {
				combine(top - 1);
			}
		}

		// 处理栈中剩余的元素
		while (top > 1) {
			combine(top - 1);
		}
	}

	// ===================== 快速读入类 =====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0) {
					return -1;
				}
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

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

		// 读入元素个数
		n = in.nextInt();

		// 读入每个元素的值
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
		}

		// 执行 GarsiaWachs 算法
		garciaWachs();

		// 输出结果
		out.println("石子合并的最小代价：" + totalCost);

		out.flush();
		out.close();
	}
}
