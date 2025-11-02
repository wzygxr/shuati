package class134;

// 课上讲述高斯消元解决异或方程组的例子

/**
 * 高斯消元解决异或方程组 - 课堂示例
 * 
 * 本文件包含多个示例，演示了异或方程组的不同解的情况：
 * 1. 唯一解
 * 2. 无解（存在矛盾）
 * 3. 多解
 * 
 * 异或方程组的一般形式：
 * a[1][1]*x[1] ^ a[1][2]*x[2] ^ ... ^ a[1][n]*x[n] = b[1]
 * a[2][1]*x[1] ^ a[2][2]*x[2] ^ ... ^ a[2][n]*x[n] = b[2]
 * ...
 * a[n][1]*x[1] ^ a[n][2]*x[2] ^ ... ^ a[n][n]*x[n] = b[n]
 * 
 * 其中 ^ 表示异或运算，a[i][j] 和 b[i] 取值为 0 或 1
 */
public class ShowDetails {

	public static int MAXN = 101;

	public static int[][] mat = new int[MAXN][MAXN];

	/**
	 * 高斯消元解决异或方程组模版
	 * 需要保证变量有n个，表达式也有n个
	 * 
	 * @param n 未知数个数
	 */
	public static void gauss(int n) {
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				if (j < i && mat[j][j] == 1) {
					continue;
				}
				if (mat[j][i] == 1) {
					swap(i, j);
					break;
				}
			}
			if (mat[i][i] == 1) {
				for (int j = 1; j <= n; j++) {
					if (i != j && mat[j][i] == 1) {
						for (int k = i; k <= n + 1; k++) {
							mat[j][k] ^= mat[i][k];
						}
					}
				}
			}
		}
	}

	/**
	 * 交换矩阵中的两行
	 * 
	 * @param a 行号1
	 * @param b 行号2
	 */
	public static void swap(int a, int b) {
		int[] tmp = mat[a];
		mat[a] = mat[b];
		mat[b] = tmp;
	}

	/**
	 * 打印增广矩阵
	 * 
	 * @param n 未知数个数
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
		System.out.println("课上图解的例子，有唯一解");
		// x1 ^ x2 ^ x3 = 0
		// x1 ^ x3 ^ x4 = 1
		// x2 ^ x3 ^ x4 = 1
		// x3 ^ x4 = 0
		mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 1; mat[1][4] = 0; mat[1][5] = 0;
		mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 1; mat[2][5] = 1;
		mat[3][1] = 0; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 1; mat[3][5] = 1;
		mat[4][1] = 0; mat[4][2] = 0; mat[4][3] = 1; mat[4][4] = 1; mat[4][5] = 0;
		gauss(4);
		print(4);

		System.out.println("表达式存在矛盾的例子");
		// x1 ^ x2 = 1
		// x1 ^ x3 = 1
		// x2 ^ x3 = 1
		mat[1][1] = 1; mat[1][2] = 1; mat[1][3] = 0; mat[1][4] = 1;
		mat[2][1] = 1; mat[2][2] = 0; mat[2][3] = 1; mat[2][4] = 1;
		mat[3][1] = 0; mat[3][2] = 1; mat[3][3] = 1; mat[3][4] = 1;
		gauss(3);
		print(3);

		System.out.println("表达式存在多解的例子");
		// x1 ^ x3 = 1
		// x2 ^ x3 = 1
		// x1 ^ x2 = 0
		mat[1][1] = 1; mat[1][2] = 0; mat[1][3] = 1; mat[1][4] = 1;
		mat[2][1] = 0; mat[2][2] = 1; mat[2][3] = 1; mat[2][4] = 1;
		mat[3][1] = 1; mat[3][2] = 1; mat[3][3] = 0; mat[3][4] = 0;
		mat[4][1] = 1; mat[4][2] = 1; mat[4][3] = 0; mat[4][4] = 0;
		gauss(3);
		print(3);

		System.out.println("注意下面这个多解的例子");
		// x1 ^ x3 ^ x4 = 0
		// x2 ^ x3 ^ x4 = 0
		// x1 ^ x2 = 0
		// x3 ^ x4 = 1
		mat[1][1] = 1; mat[1][2] = 0; mat[1][3] = 1; mat[1][4] = 1; mat[1][5] = 0;
		mat[2][1] = 0; mat[2][2] = 1; mat[2][3] = 1; mat[2][4] = 1; mat[2][5] = 0;
		mat[3][1] = 1; mat[3][2] = 1; mat[3][3] = 0; mat[3][4] = 0; mat[3][5] = 0;
		mat[4][1] = 0; mat[4][2] = 0; mat[4][3] = 1; mat[4][4] = 1; mat[4][5] = 1;
		gauss(4);
		print(4);
		System.out.println("最后一个例子里");
		System.out.println("主元x1和x2，不受其他自由元影响，值可以直接确定");
		System.out.println("但是主元x3，受到自由元x4的影响，x3 ^ x4 = 1");
		System.out.println("只有自由元x4确定了值，主元x3的值才能确定");
		System.out.println("这里是想说，消元完成后，如果结论是多解，那么");
		System.out.println("有些主元的值可以直接确定");
		System.out.println("有些主元的值需要若干自由元确定之后才能确定");
		System.out.println("这就是上节课，也就是讲解133讲的：");
		System.out.println("主元和自由元之间的依赖关系");
		System.out.println("请确保已经掌握");
	}

}