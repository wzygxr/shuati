package class019;

// ==================================================================================
// 题目2：子矩阵的最大累加和（ACM风格 - 指定数据规模）
// ==================================================================================
// 题目来源：牛客网 (NowCoder)
// 题目链接：https://www.nowcoder.com/practice/cb82a97dcd0d48a7b1f4ee917e2c0409
// 难度等级：中等
// 
// ==================================================================================
// ACM风格说明 - 指定数据规模版本
// ==================================================================================
// 【与Code01的区别】
// - Code01：填函数风格，只需要实现算法逻辑
// - Code02：ACM风格，需要自己处理输入输出
// 
// 【什么是ACM风格？】
// ACM竞赛中的标准输入输出方式：
// 1. 从标准输入(System.in)读取数据
// 2. 向标准输出(System.out)输出结果
// 3. 需要自己解析输入格式
// 4. 需要按要求格式化输出
// 
// 【本题的输入格式】
// - 第一行：两个整数n和m（矩阵的行数和列数）
// - 接下n行：每行m个整数，表示矩阵元素
// - 可能有多组测试数据（直到文件结束）
// 
// 【本题的输出格式】
// - 对于每组测试数据，输出一行：一个整数，表示最大子矩阵和
// 
// ==================================================================================
// Java中IO优化技巧详解
// ==================================================================================
// 【为什么需要IO优化？】
// - Scanner读取速度慢，大数据量时会超时
// - System.out.println输出效率低，频繁调用会卡顿
// - 竞赛中时间限制严格，IO效率很关键
// 
// 【Java快速IO方案对比】
// 1. Scanner + System.out.println
//    - 优点：简单易用，支持多种类型
//    - 缺点：最慢，不适合竞赛
//    - 适用场景：小数据量，快速调试
// 
// 2. BufferedReader + PrintWriter
//    - 优点：速度中等，代码简洁
//    - 缺点：需要手动解析数字
//    - 适用场景：中等数据量
// 
// 3. BufferedReader + StreamTokenizer + PrintWriter（本文件使用）
//    - 优点：读取数字非常快，代码也较简洁
//    - 缺点：只适合读数字，不适合读字符串
//    - 适用场景：大数据量数字输入（推荐！）
// 
// 4. FastReader + FastWriter（见Code06）
//    - 优点：最快，手动控制每一步
//    - 缺点：代码复杂，需要自己实现
//    - 适用场景：极限数据量，对速度要求极高
// 
// 5. Kattio（见Code05）
//    - 优点：能处理StreamTokenizer无法处理的特殊情况
//    - 缺点：比StreamTokenizer慢一点
//    - 适用场景：需要读取大数、科学计数法数字、字符串
// 
// ==================================================================================
// StreamTokenizer详细说明
// ==================================================================================
// 【StreamTokenizer是什么？】
// - Java内置的词法分析器
// - 能够将输入流分解为一个个“词素（token）”
// - 自动处理空格、换行符等分隔符
// 
// 【主要API】
// 1. nextToken() - 读取下一个词素
//    - 返回值：
//      - TT_EOF：文件结束
//      - TT_NUMBER：数字
//      - TT_WORD：单词
//      - 其他：字符的ASCII值
// 
// 2. nval - 当前读取的数字值（double类型）
//    - 注意：需要强制转换为int或long
// 
// 3. sval - 当前读取的字符串值
// 
// 【使用模式】
// while (in.nextToken() != StreamTokenizer.TT_EOF) {
//     int n = (int) in.nval;  // 读取第一个数
//     in.nextToken();
//     int m = (int) in.nval;  // 读取第二个数
//     // ... 处理逻辑
// }
// 
// 【注意事项】
// 1. 每次读取前必须调用nextToken()
// 2. nval是double类型，需要强转
// 3. 不能读取字符串（只能读数字和单词）
// 4. 对于极大的long、科学计数法可能出错，请用Kattio
// 
// ==================================================================================
// PrintWriter详细说明
// ==================================================================================
// 【PrintWriter是什么？】
// - Java的缓冲输出流
// - 将数据先写入内存缓冲区，批量输出
// - 比System.out.println快很多
// 
// 【主要API】
// 1. print(x) - 输出x，不换行
// 2. println(x) - 输出x，自动换行
// 3. flush() - 强制刷新缓冲区，将数据输出
// 4. close() - 关闭流（会自动flush）
// 
// 【使用模式】
// PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
// out.println(result);
// out.flush();  // 必须调用，否则可能没有输出
// out.close();
// 
// 【注意事项】
// 1. 必须调用flush()或close()，否则数据可能留在缓冲区
// 2. close()会自动调用flush()
// 3. 不要和System.out.println混用

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_SpecifyAmount{

	public static void main(String[] args) throws IOException {
		// 把文件里的内容，load进来，保存在内存里，很高效，很经济，托管的很好
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 一个一个读数字
		StreamTokenizer in = new StreamTokenizer(br);
		// 提交答案的时候用的，也是一个内存托管区
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) { // 文件没有结束就继续
			// n，二维数组的行
			int n = (int) in.nval;
			in.nextToken();
			// m，二维数组的列
			int m = (int) in.nval;
			// 装数字的矩阵，临时动态生成
			int[][] mat = new int[n][m];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					in.nextToken();
					mat[i][j] = (int) in.nval;
				}
			}
			out.println(maxSumSubmatrix(mat, n, m));
		}
		out.flush();
		br.close();
		out.close();
	}

	// 求子矩阵的最大累加和，后面的课会讲
	// 算法思路：
	// 1. 枚举所有可能的上下边界(i,j)
	// 2. 将第i行到第j行的每列元素相加，形成一个一维数组
	// 3. 对这个一维数组求最大子数组和
	// 4. 记录所有情况下的最大值
	//
	// 时间复杂度：O(n^2 * m)，其中n是行数，m是列数
	// 空间复杂度：O(m)，用于存储压缩后的数组
	public static int maxSumSubmatrix(int[][] mat, int n, int m) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			// 需要的辅助数组，临时动态生成
			int[] arr = new int[m];
			for (int j = i; j < n; j++) {
				for (int k = 0; k < m; k++) {
					arr[k] += mat[j][k];
				}
				max = Math.max(max, maxSumSubarray(arr, m));
			}
		}
		return max;
	}

	// 求子数组的最大累加和，使用Kadane算法
	// 算法思路：
	// 1. 维护当前子数组的和(cur)和全局最大值(max)
	// 2. 遍历数组，将当前元素加入到当前子数组和中
	// 3. 更新全局最大值
	// 4. 如果当前子数组和变为负数，则重新开始计算(置为0)
	//
	// 时间复杂度：O(m)，其中m是数组长度
	// 空间复杂度：O(1)
	public static int maxSumSubarray(int[] arr, int m) {
		int max = Integer.MIN_VALUE;
		int cur = 0;
		for (int i = 0; i < m; i++) {
			cur += arr[i];
			max = Math.max(max, cur);
			cur = cur < 0 ? 0 : cur;
		}
		return max;
	}

}