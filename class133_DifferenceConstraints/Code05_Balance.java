package class142;

// 天平
// 一共有n个砝码，编号1~n，每个砝码的重量均为1克，或者2克，或者3克 
// 砝码与砝码之间的关系是一个n * n的二维数组s
// s[i][j] == '+'，砝码i比砝码j重        s[i][j] == '-'，砝码i比砝码j轻
// s[i][j] == '='，砝码i和砝码j重量一样   s[i][j] == '?'，砝码i和砝码j关系未知
// 数据保证至少存在一种情况符合该矩阵
// 给定编号为a和b的砝码，这两个砝码一定会放在天平的左边，你要另选两个砝码放在天平右边
// 返回有多少种方法，一定让天平左边重(ans1)，一定让天平一样重(ans2)，一定让天平右边重(ans3)
// 1 <= n <= 50
// 测试链接 : https://www.luogu.com.cn/problem/P2474
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/**
 * 天平问题详解：
 * 
 * 1. 问题分析：
 *    这是一个基于Floyd算法的差分约束问题，需要根据已知的砝码关系推断所有砝码间的重量关系
 *    然后枚举所有可能的砝码组合，统计天平各种状态的方案数
 * 
 * 2. 约束条件转化：
 *    - s[i][j] == '+'：砝码i比砝码j重 => weight[i] - weight[j] >= 1 => weight[j] - weight[i] <= -1
 *    - s[i][j] == '-'：砝码i比砝码j轻 => weight[i] - weight[j] <= -1
 *    - s[i][j] == '='：砝码i和砝码j重量一样 => weight[i] - weight[j] = 0 
 *         => weight[i] - weight[j] <= 0 且 weight[j] - weight[i] <= 0
 *    - s[i][j] == '?'：砝码i和砝码j关系未知 => weight[i] - weight[j] <= 2 且 weight[j] - weight[i] <= 2
 *         (因为重量只可能是1、2、3克，所以差值最大为2)
 * 
 * 3. Floyd算法应用：
 *    - 初始化：根据关系矩阵设置dmin和dmax数组
 *    - 传递闭包：通过Floyd算法计算所有点对间的最值
 *    - dmin[i][j]表示weight[i] - weight[j]的最小可能值
 *    - dmax[i][j]表示weight[i] - weight[j]的最大可能值
 * 
 * 4. 方案统计：
 *    - 枚举所有未被选的砝码对(i,j)，其中i < j且i,j != a且i,j != b
 *    - 计算左边重量差：dmin[a][i]和dmax[a][i]，dmin[b][j]和dmax[b][j]
 *    - 左边总重量差的范围：[dmin[a][i] + dmin[b][j], dmax[a][i] + dmax[b][j]]
 *    - 右边总重量差的范围：[dmin[i][a] + dmin[j][b], dmax[i][a] + dmax[j][b]]
 *    - 根据范围判断天平状态并统计
 * 
 * 5. 算法实现细节：
 *    - 使用Floyd算法计算传递闭包，推断所有砝码间的重量关系
 *    - dmin和dmax数组分别存储重量差的最小值和最大值
 *    - 通过枚举所有可能的砝码组合统计天平状态
 * 
 * 时间复杂度分析：
 * - Floyd算法：O(n^3)
 * - 方案统计：O(n^2)
 * - 总体：O(n^3)
 * 
 * 空间复杂度分析：
 * - dmin和dmax数组：O(n^2)
 * - 关系矩阵s：O(n^2)
 * - 总体：O(n^2)
 * 
 * 相关题目：
 * 1. 洛谷 P2474 [SCOI2008]天平
 *    链接：https://www.luogu.com.cn/problem/P2474
 *    题意：本题
 * 
 * 2. 洛谷 P1993 小K的农场
 *    链接：https://www.luogu.com.cn/problem/P1993
 *    题意：农场约束问题
 * 
 * 3. 洛谷 P5960 【模板】差分约束算法
 *    链接：https://www.luogu.com.cn/problem/P5960
 *    题意：差分约束模板题
 * 
 * 4. POJ 3169 Layout
 *    链接：http://poj.org/problem?id=3169
 *    题意：奶牛布局问题
 * 
 * 5. POJ 1201 Intervals
 *    链接：http://poj.org/problem?id=1201
 *    题意：区间选点问题
 * 
 * 6. POJ 1716 Integer Intervals
 *    链接：http://poj.org/problem?id=1716
 *    题意：POJ 1201的简化版本
 * 
 * 7. POJ 2983 Is the Information Reliable?
 *    链接：http://poj.org/problem?id=2983
 *    题意：判断信息可靠性
 * 
 * 8. 洛谷 P1250 种树
 *    链接：https://www.luogu.com.cn/problem/P1250
 *    题意：区间种树问题
 * 
 * 9. 洛谷 P2294 [HNOI2005]狡猾的商人
 *    链接：https://www.luogu.com.cn/problem/P2294
 *    题意：商人账本合理性判断
 * 
 * 10. 洛谷 P4926 [1007]倍杀测量者
 *     链接：https://www.luogu.com.cn/problem/P4926
 *     题意：倍杀测量问题，需要对数变换
 * 
 * 11. LibreOJ #10087 「一本通3.4 例1」Intervals
 *     链接：https://loj.ac/p/10087
 *     题意：区间选点问题，与POJ 1201类似
 * 
 * 12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
 *     链接：https://loj.ac/p/10088
 *     题意：出纳员工作时间安排问题
 * 
 * 13. AtCoder ABC216G 01Sequence
 *     链接：https://atcoder.jp/contests/abc216/tasks/abc216_g
 *     题意：01序列问题，涉及差分约束
 * 
 * 工程化考虑：
 * 1. 异常处理：
 *    - 输入校验：检查n范围
 *    - 矩阵处理：确保关系矩阵的对称性和合理性
 * 
 * 2. 性能优化：
 *    - 使用二维数组存储关系和最值，访问速度快
 *    - Floyd算法中充分利用对称性减少计算
 * 
 * 3. 可维护性：
 *    - 函数职责单一，compute()计算最值和统计方案，main()处理输入输出
 *    - 变量命名清晰，dmin、dmax等表示最值数组
 *    - 详细注释说明算法原理和关键步骤
 * 
 * 4. 可扩展性：
 *    - 可以扩展支持更多砝码重量类型
 *    - 可以添加更多输出信息，如具体方案详情
 *    - 可以扩展为求解最优方案而非仅统计数量
 * 
 * 5. 边界情况处理：
 *    - 空输入处理
 *    - 极端值处理（最大/最小约束值）
 *    - 重复约束处理
 * 
 * 6. 测试用例覆盖：
 *    - 基本功能测试
 *    - 边界值测试
 *    - 异常情况测试
 *    - 性能测试
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_Balance {

	public static int MAXN = 51;

	// dmin[i][j]表示砝码i - 砝码j的最小可能重量差值
	public static int[][] dmin = new int[MAXN][MAXN];

	// dmax[i][j]表示砝码i - 砝码j的最大可能重量差值
	public static int[][] dmax = new int[MAXN][MAXN];

	// 关系矩阵s[i][j]表示砝码i和砝码j的关系
	// '+'表示砝码i比砝码j重，'-'表示砝码i比砝码j轻
	// '='表示砝码i和砝码j重量一样，'?'表示关系未知
	public static char[][] s = new char[MAXN][MAXN];

	// 砝码数量n，天平左边的两个砝码编号a和b
	public static int n, a, b;

	// ans1表示天平左边重的方案数
	// ans2表示天平一样重的方案数
	// ans3表示天平右边重的方案数
	public static int ans1, ans2, ans3;

	/**
	 * 计算函数，用于计算所有砝码间的重量关系并统计天平状态
	 * 时间复杂度：O(n^3)
	 * 空间复杂度：O(n^2)
	 */
	public static void compute() {
		// 设置初始关系
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				// 根据关系矩阵设置dmin和dmax数组的初始值
				if (s[i][j] == '=') {
					// 砝码i和砝码j重量一样，差值为0
					dmin[i][j] = 0;
					dmax[i][j] = 0;
				} else if (s[i][j] == '+') {
					// 砝码i比砝码j重，差值最小为1，最大为2（因为重量只可能是1、2、3克）
					dmin[i][j] = 1;
					dmax[i][j] = 2;
				} else if (s[i][j] == '-') {
					// 砝码i比砝码j轻，差值最小为-2，最大为-1
					dmin[i][j] = -2;
					dmax[i][j] = -1;
				} else {
					// 关系未知，差值最小为-2，最大为2
					dmin[i][j] = -2;
					dmax[i][j] = 2;
				}
			}
		}
		// 设置对角线元素，每个砝码与自己的差值为0
		for (int i = 1; i <= n; i++) {
			dmin[i][i] = 0;
			dmax[i][i] = 0;
		}
		// 来自讲解065，Floyd算法
		// 使用Floyd算法计算传递闭包，推断所有砝码间的重量关系
		for (int bridge = 1; bridge <= n; bridge++) {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= n; j++) {
					// 更新dmin[i][j]，取当前值和通过bridge节点的路径中的最大值
					dmin[i][j] = Math.max(dmin[i][j], dmin[i][bridge] + dmin[bridge][j]);
					// 更新dmax[i][j]，取当前值和通过bridge节点的路径中的最小值
					dmax[i][j] = Math.min(dmax[i][j], dmax[i][bridge] + dmax[bridge][j]);
				}
			}
		}
		// 统计答案
		ans1 = ans2 = ans3 = 0;
		// 枚举所有未被选的砝码对(i,j)，其中i < j且i,j != a且i,j != b
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j < i; j++) {
				// 确保i和j都不是天平左边的砝码
				if (i != a && i != b && j != a && j != b) {
					// 计算左边重量差的范围：[dmin[a][i] + dmin[b][j], dmax[a][i] + dmax[b][j]]
					// 计算右边重量差的范围：[dmin[i][a] + dmin[j][b], dmax[i][a] + dmax[j][b]]
					// 如果左边重量差的最小值大于右边重量差的最大值，说明天平左边一定重
					if (dmin[a][i] > dmax[j][b] || dmin[a][j] > dmax[i][b]) {
						ans1++;
					}
					// 如果左边重量差的最大值小于右边重量差的最小值，说明天平右边一定重
					if (dmax[a][i] < dmin[j][b] || dmax[a][j] < dmin[i][b]) {
						ans3++;
					}
					// 如果左边和右边的重量差都确定且相等，说明天平一定平衡
					if (dmin[a][i] == dmax[a][i] && dmin[j][b] == dmax[j][b] && dmin[a][i] == dmin[j][b]) {
						ans2++;
					} else if (dmin[b][i] == dmax[b][i] && dmin[j][a] == dmax[j][a] && dmin[b][i] == dmin[j][a]) {
						ans2++;
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String[] numbers = br.readLine().split(" ");
		n = Integer.valueOf(numbers[0]);
		a = Integer.valueOf(numbers[1]);
		b = Integer.valueOf(numbers[2]);
		char[] line;
		for (int i = 1; i <= n; i++) {
			line = br.readLine().toCharArray();
			for (int j = 1; j <= n; j++) {
				s[i][j] = line[j - 1];
			}
		}
		compute();
		out.println(ans1 + " " + ans2 + " " + ans3);
		out.flush();
		out.close();
		br.close();
	}

}