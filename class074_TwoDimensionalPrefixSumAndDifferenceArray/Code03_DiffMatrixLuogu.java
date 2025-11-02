package class048;

/**
 * 二维差分数组算法实现（洛谷版）
 * 
 * 问题描述：
 * 在n×n的格子上有m个地毯，给出这些地毯的信息，问每个点被多少个地毯覆盖。
 * 
 * 核心思想：
 * 1. 利用二维差分数组处理区间更新操作
 * 2. 对每个地毯覆盖区域，在差分数组中进行O(1)标记
 * 3. 通过二维前缀和还原差分数组得到最终结果
 * 
 * 算法详解：
 * 1. 差分标记：对区域[(a,b),(c,d)]增加1，在差分数组中标记：
 *    - diff[a][b] += 1
 *    - diff[c+1][b] -= 1
 *    - diff[a][d+1] -= 1
 *    - diff[c+1][d+1] += 1
 * 2. 前缀和还原：通过二维前缀和将差分数组还原为结果数组
 * 
 * 时间复杂度分析：
 * 1. 差分标记：O(m)，m为地毯数量
 * 2. 前缀和还原：O(n²)，n为网格边长
 * 3. 总体复杂度：O(m + n²)
 * 
 * 空间复杂度分析：
 * O(n²)，用于存储差分数组
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
 * 1. 洛谷 P3397 地毯
 * 2. LeetCode 2132. 用邮票贴满网格图
 * 3. Codeforces 835C - Star sky
 * 
 * 测试链接 : https://www.luogu.com.cn/problem/P3397
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

public class Code03_DiffMatrixLuogu {

	// 最大网格尺寸
	public static int MAXN = 1002;

	// 差分数组，扩展边界避免特殊判断
	public static int[][] diff = new int[MAXN][MAXN];

	// 网格边长和操作数量
	public static int n, q;

	/**
	 * 在二维差分数组中标记区域更新 - 详细注释版
	 * 
	 * 算法原理：
	 * 对区域[(a,b),(c,d)]增加k，在差分数组中进行标记：
	 * 1. diff[a][b] += k      // 左上角标记+k
	 * 2. diff[c+1][b] -= k    // 右上角右侧标记-k
	 * 3. diff[a][d+1] -= k    // 左下角下方标记-k
	 * 4. diff[c+1][d+1] += k  // 右下角标记+k，补偿多减的部分
	 * 
	 * 数学推导：
	 * 差分标记的核心思想是将区域更新操作分解为四个角的操作，
	 * 通过这四个标记的组合，可以在后续的前缀和还原过程中正确计算出每个位置的值。
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 工程化考量：
	 * 1. 参数校验：应添加对坐标有效性的检查
	 * 2. 边界安全：依赖外部确保数组不越界
	 * 3. 性能优化：直接操作数组，避免函数调用开销
	 * 
	 * @param a 区域左上角行索引，要求1 ≤ a ≤ n
	 * @param b 区域左上角列索引，要求1 ≤ b ≤ n
	 * @param c 区域右下角行索引，要求a ≤ c ≤ n
	 * @param d 区域右下角列索引，要求b ≤ d ≤ n
	 * @param k 增加的值，可以为正数或负数
	 */
	public static void add(int a, int b, int c, int d, int k) {
		// 差分标记操作
		diff[a][b] += k;          // 标记区域开始位置
		diff[c + 1][b] -= k;       // 标记区域结束位置右侧
		diff[a][d + 1] -= k;       // 标记区域结束位置下方
		diff[c + 1][d + 1] += k;   // 补偿标记，避免多减
		
		// 调试输出：验证标记操作
		// System.out.printf("区域更新: [(%d,%d),(%d,%d)] +%d, 差分标记完成%n", a, b, c, d, k);
	}

	/**
	 * 通过二维前缀和还原差分数组
	 * 
	 * 算法原理：
	 * 利用容斥原理将差分数组还原为结果数组：
	 * diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]
	 * 
	 * 时间复杂度：O(n²)
	 * 空间复杂度：O(1)（原地更新）
	 */
	public static void build() {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
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
	 * 时间复杂度：O(n²)
	 * 空间复杂度：O(1)
	 */
	public static void clear() {
		for (int i = 1; i <= n + 1; i++) {
			for (int j = 1; j <= n + 1; j++) {
				diff[i][j] = 0;
			}
		}
	}

	/**
	 * 主函数
	 * 
	 * 程序流程：
	 * 1. 读取网格尺寸n和操作数量q
	 * 2. 处理q个地毯操作，在差分数组中标记
	 * 3. 通过前缀和还原差分数组
	 * 4. 输出结果矩阵
	 * 5. 清空数组，准备处理下一组数据
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
			q = (int) in.nval;
			
			// 处理q个地毯操作
			for (int i = 1, a, b, c, d; i <= q; i++) {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				// 在差分数组中标记地毯覆盖区域
				add(a, b, c, d, 1);
			}
			
			// 通过前缀和还原差分数组得到结果
			build();
			
			// 输出结果矩阵
			for (int i = 1; i <= n; i++) {
				out.print(diff[i][1]);
				for (int j = 2; j <= n; j++) {
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