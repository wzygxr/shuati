package class048;

/**
 * 二维差分数组算法实现（牛客版）
 * 
 * 问题描述：
 * 给定一个n行m列的全0矩阵，再给出q个操作，每个操作包含5个整数x1,y1,x2,y2,k，
 * 表示将子矩阵[(x1,y1),(x2,y2)]中每个元素加上k，求所有操作完成后矩阵中每个元素的值。
 * 
 * 核心思想：
 * 1. 利用二维差分数组处理区间更新操作
 * 2. 对每个操作区域，在差分数组中进行O(1)标记
 * 3. 通过二维前缀和还原差分数组得到最终结果
 * 
 * 算法详解：
 * 1. 初始化：将原始矩阵转换为差分数组
 * 2. 差分标记：对每个操作区域，在差分数组中进行标记
 * 3. 前缀和还原：通过二维前缀和将差分数组还原为结果数组
 * 
 * 时间复杂度分析：
 * 1. 初始化差分数组：O(n*m)
 * 2. 差分标记：O(q)，q为操作数量
 * 3. 前缀和还原：O(n*m)
 * 4. 总体复杂度：O(n*m + q)
 * 
 * 空间复杂度分析：
 * O(n*m)，用于存储差分数组
 * 
 * 算法优势：
 * 1. 区间更新效率高，每次操作O(1)
 * 2. 适合处理大量区间更新操作
 * 3. 空间效率高，复用同一数组
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用StreamTokenizer和PrintWriter提高效率
 * 2. 内存管理：通过clear方法重置数组，避免多次分配内存
 * 3. 边界处理：扩展数组边界避免特殊判断
 * 
 * 应用场景：
 * 1. 图像处理中的区域操作
 * 2. 游戏开发中的区域影响计算
 * 3. 地理信息系统中的区域统计
 * 
 * 相关题目：
 * 1. 牛客 226337 二维差分
 * 2. 洛谷 P3397 地毯
 * 3. LeetCode 2132. 用邮票贴满网格图
 * 
 * 测试链接 : https://www.nowcoder.com/practice/50e1a93989df42efb0b1dec386fb4ccc
 * 请同学们务必参考如下代码中关于输入、输出的处理
 * 这是输入输出处理效率很高的写法
 * 提交以下的code，提交时请把类名改成"Main"，可以直接通过
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_DiffMatrixNowcoder {

	// 最大行列数
	public static int MAXN = 1005;
	public static int MAXM = 1005;

	// 差分数组，使用long类型防止溢出
	public static long[][] diff = new long[MAXN][MAXM];

	// 行数、列数和操作数
	public static int n, m, q;

	/**
	 * 在二维差分数组中标记区域更新
	 * 
	 * 算法原理：
	 * 对区域[(a,b),(c,d)]增加k，在差分数组中进行标记：
	 * 1. diff[a][b] += k
	 * 2. diff[c+1][b] -= k
	 * 3. diff[a][d+1] -= k
	 * 4. diff[c+1][d+1] += k
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 区域左上角行索引
	 * @param b 区域左上角列索引
	 * @param c 区域右下角行索引
	 * @param d 区域右下角列索引
	 * @param k 增加的值
	 */
	public static void add(int a, int b, int c, int d, int k) {
		diff[a][b] += k;
		diff[c + 1][b] -= k;
		diff[a][d + 1] -= k;
		diff[c + 1][d + 1] += k;
	}

	/**
	 * 通过二维前缀和还原差分数组
	 * 
	 * 算法原理：
	 * 利用容斥原理将差分数组还原为结果数组：
	 * diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(1)（原地更新）
	 */
	public static void build() {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				diff[i][j] += diff[i - 1][j] + diff[i][j - 1] - diff[i - 1][j - 1];
			}
		}
	}

	/**
	 * 清空差分数组
	 * 
	 * 工程化考虑：
	 * 1. 避免重复分配内存
	 * 2. 重置数组状态，为下一次计算做准备
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(1)
	 */
	public static void clear() {
		for (int i = 1; i <= n + 1; i++) {
			for (int j = 1; j <= m + 1; j++) {
				diff[i][j] = 0;
			}
		}
	}

	/**
	 * 主函数
	 * 
	 * 程序流程：
	 * 1. 读取矩阵尺寸n、m和操作数量q
	 * 2. 初始化差分数组（将原始矩阵转换为差分数组）
	 * 3. 处理q个操作，在差分数组中标记
	 * 4. 通过前缀和还原差分数组
	 * 5. 输出结果矩阵
	 * 6. 清空数组，准备处理下一组数据
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			in.nextToken();
			m = (int) in.nval;
			in.nextToken();
			q = (int) in.nval;
			
			// 初始化差分数组（将原始矩阵转换为差分数组）
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= m; j++) {
					in.nextToken();
					// 将原始矩阵元素转换为差分数组元素
					add(i, j, i, j, (int) in.nval);
				}
			}
			
			// 处理q个操作
			for (int i = 1, a, b, c, d, k; i <= q; i++) {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				in.nextToken();
				k = (int) in.nval;
				// 在差分数组中标记操作区域
				add(a, b, c, d, k);
			}
			
			// 通过前缀和还原差分数组得到结果
			build();
			
			// 输出结果矩阵
			for (int i = 1; i <= n; i++) {
				out.print(diff[i][1]);
				for (int j = 2; j <= m; j++) {
					out.print(" " + diff[i][j]);
				}
				out.println();
			}
			
			// 清空数组，准备处理下一组数据
			clear();
		}
		out.flush();
		out.close();
		br.close();
	}

}