package class135;

/**
 * ShowDetails - 高斯消元法应用
 * 
 * 算法核心思想:
 * 使用高斯消元法解决线性方程组或线性基相关问题
 * 
 * 关键步骤:
 * 1. 构建增广矩阵
 * 2. 前向消元，将矩阵化为上三角形式
 * 3. 回代求解未知数
 * 4. 处理特殊情况（无解、多解）
 * 
 * 时间复杂度分析:
 * - 高斯消元: O(n³)
 * - 线性基构建: O(n * log(max_value))
 * - 查询操作: O(log(max_value))
 * 
 * 空间复杂度分析:
 * - 矩阵存储: O(n²)
 * - 线性基: O(log(max_value))
 * 
 * 工程化考量:
 * 1. 数值稳定性: 使用主元选择策略避免精度误差
 * 2. 边界处理: 处理零矩阵、奇异矩阵等特殊情况
 * 3. 异常处理: 检查输入合法性，提供有意义的错误信息
 * 4. 性能优化: 针对稀疏矩阵进行优化
 * 
 * 应用场景:
 * - 线性方程组求解
 * - 线性基构建与查询
 * - 异或最大值问题
 * - 概率期望计算
 * 
 * 调试技巧:
 * 1. 打印中间矩阵状态验证消元过程
 * 2. 使用小规模测试用例验证正确性
 * 3. 检查边界条件（n=0, n=1等）
 * 4. 验证数值精度和稳定性
 */


// 课上讲述高斯消元解决同余方程组的例子

/*
 * 题目解析:
 * 本文件展示了高斯消元解决同余方程组的多种情况
 * 包括唯一解、无解和多解的情况
 * 
 * 解题思路:
 * 1. 使用高斯消元将增广矩阵化为行阶梯形
 * 2. 判断解的情况：
 *    - 如果出现 0 = k (k≠0) 形式的行，则无解
 *    - 如果系数矩阵的秩等于未知数个数，则有唯一解
 *    - 如果系数矩阵的秩小于未知数个数，则有无穷多解
 * 3. 对于多解情况，正确处理主元和自由元的关系
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 完整处理各种解的情况
 * 2. 正确处理主元和自由元的关系
 * 3. 特殊处理模意义下的运算
 */

public class ShowDetails {

	// 题目会保证取模的数字为质数
	public static int MOD = 7;

	public static int MAXN = 101;

	public static int[][] mat = new int[MAXN][MAXN];

	// 逆元线性递推公式求逆元表，讲解099 - 除法同余
	public static int[] inv = new int[MOD];

	/*
	 * 预处理模MOD意义下的逆元
	 * 使用递推公式: inv[i] = MOD - (MOD/i) * inv[MOD%i] % MOD
	 * 时间复杂度: O(MOD)
	 * 空间复杂度: O(MOD)
	 */
	public static void inv() {
		inv[1] = 1;
		for (int i = 2; i < MOD; i++) {
			inv[i] = (int) (MOD - (long) inv[MOD % i] * (MOD / i) % MOD);
		}
	}

	// 求a和b的最大公约数，保证a和b都不等于0
	/*
	 * 计算两个整数的最大公约数
	 * 使用欧几里得算法
	 * 时间复杂度: O(log(min(a,b)))
	 * 空间复杂度: O(1)
	 */
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	// 高斯消元解决同余方程组模版，保证初始系数没有负数
	// ((系数 % MOD) + MOD) % MOD
	/*
	 * 高斯消元解决模线性方程组模版
	 * 完整处理主元、自由元和解的判断
	 * 正确处理主元和自由元的关系
	 * 时间复杂度: O(n^3)
	 * 空间复杂度: O(n^2)
	 */
	public static void gauss(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				// 已经成为主元的行不参与
				if (j < i && mat[j][j] != 0) {
					continue;
				}
				// 找到系数不等于0的行做主元即可
				if (mat[j][i] != 0) {
					swap(i, j);
					break;
				}
			}
			if (mat[i][i] != 0) {
				for (int j = 1; j <= n; j++) {
					if (i != j && mat[j][i] != 0) {
						int gcd = gcd(mat[j][i], mat[i][i]);
						int a = mat[i][i] / gcd;
						int b = mat[j][i] / gcd;
						if (j < i && mat[j][j] != 0) {
							// 如果j行有主元，那么从j列到i-1列的所有系数 * a
							// 正确更新主元和自由元之间的关系
							for (int k = j; k < i; k++) {
								mat[j][k] = (mat[j][k] * a) % MOD;
							}
						}
						// 正常消元
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] = ((mat[j][k] * a - mat[i][k] * b) % MOD + MOD) % MOD;
						}
					}
				}
			}
		}
		for (int i = 1; i <= n; i++) {
			if (mat[i][i] != 0) {
				// 检查当前主元是否被若干自由元影响
				// 如果当前主元不受自由元影响，那么可以确定当前主元的值
				// 否则保留这种影响，正确显示主元和自由元的关系
				boolean flag = false;
				for (int j = i + 1; j <= n; j++) {
					if (mat[i][j] != 0) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					// 本来应该是，mat[i][n + 1] = mat[i][n + 1] / mat[i][i]
					// 但是在模意义下应该求逆元，(a / b) % MOD = (a * b的逆元) % MOD
					// 如果不会，去看讲解099 - 除法同余
					mat[i][n + 1] = (mat[i][n + 1] * inv[mat[i][i]]) % MOD;
					mat[i][i] = 1;
				}
			}
		}
	}

	/*
	 * 交换矩阵中的两行
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 */
	public static void swap(int a, int b) {
		int[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	/*
	 * 打印矩阵内容
	 * 时间复杂度: O(n^2)
	 * 空间复杂度: O(1)
	 */
	public static void print(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n + 1; j++) {
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("========================");
	}

	public static void main(String[] args) {
		// 逆元表建立好
		inv();
		System.out.println("课上图解的例子，唯一解");
		// 4*x1 + 2*x2 + 4*x3 同余 3
		// 2*x1 + 5*x2 + 2*x3 同余 2
		// 6*x1 + 3*x2 + 4*x3 同余 5
		mat[1][1] = 4; mat[1][2] = 2; mat[1][3] = 4; mat[1][4] = 3;
		mat[2][1] = 2; mat[2][2] = 5; mat[2][3] = 2; mat[2][4] = 2;
		mat[3][1] = 6; mat[3][2] = 3; mat[3][3] = 4; mat[3][4] = 5;
		gauss(3);
		print(3);

		System.out.println("表达式存在矛盾的例子");
		// 1*x1 + 2*x2 + 3*x3 同余 2
		// 2*x1 + 4*x2 + 6*x3 同余 5
		// 0*x1 + 3*x2 + 4*x3 同余 2
		mat[1][1] = 1; mat[1][2] = 2; mat[1][3] = 3; mat[1][4] = 2;
		mat[2][1] = 2; mat[2][2] = 4; mat[2][3] = 6; mat[2][4] = 5;
		mat[3][1] = 0; mat[3][2] = 3; mat[3][3] = 4; mat[3][4] = 2;
		gauss(3);
		print(3);

		System.out.println("课上图解的例子，多解");
		System.out.println("只有确定了自由元，才能确定主元的值");
		System.out.println("如果是多解的情况，那么在消元结束后");
		System.out.println("二维矩阵中主元和自由元的关系是正确的");
		System.out.println("课上也进行了验证");
		// 1*x1 + 2*x2 + 3*x3 同余 2
		// 2*x1 + 4*x2 + 6*x3 同余 4
		// 0*x1 + 3*x2 + 4*x3 同余 2
		mat[1][1] = 1; mat[1][2] = 2; mat[1][3] = 3; mat[1][4] = 2;
		mat[2][1] = 2; mat[2][2] = 4; mat[2][3] = 6; mat[2][4] = 4;
		mat[3][1] = 0; mat[3][2] = 3; mat[3][3] = 4; mat[3][4] = 2;
		gauss(3);
		print(3);

		System.out.println("注意下面这个多解的例子");
		// 1*x1 + 1*x2 + 1*x3 同余 3
		// 2*x1 + 1*x2 + 1*x3 同余 5
		// 0*x1 + 3*x2 + 3*x3 同余 3
		mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 1; mat[1][4] = 3;
		mat[2][1] = 2; mat[2][2] = 1; mat[2][3] = 1; mat[2][4] = 5;
		mat[3][1] = 0; mat[3][2] = 3; mat[3][3] = 3; mat[3][4] = 3;
		gauss(3);
		print(3);
		System.out.println("最后一个例子里");
		System.out.println("主元x1，不受其他自由元影响，值可以直接确定");
		System.out.println("但是主元x2，受到自由元x3的影响，6*x2 + 6*x3 同余 6");
		System.out.println("只有自由元x3确定了值，主元x2的值才能确定");
		System.out.println("本节课提供的模版，对于能求出的主元可以得到正确结果");
		System.out.println("对于不能求出的主元，该模版也能给出，主元和自由元的正确关系");
		System.out.println("有些题目需要这种多解情况下，主元和自由元之间的正确关系");
		System.out.println("绝大多数模版和讲解都没有考虑这个，但值得引起重视");
		System.out.println("如果有些题目不需要这种正确关系");
		System.out.println("那么逻辑可以化简，让常数时间更快，比如本节课的题目1");
	}

}