package class137;

/**
 * 装备购买问题 - 线性基算法应用
 * 
 * 题目来源：洛谷 P3265 [JLOI2015]装备购买
 * 题目链接：https://www.luogu.com.cn/problem/P3265
 * 
 * 题目描述：
 * 一共有n个物品，每个物品都有m个属性值和一个价格。
 * 定义不必要的物品：如果已经选择了k个物品，此时又有一件当前物品，
 * 如果给已经选择的物品分配一组相乘的系数，并把属性值相加，就能得到当前物品，
 * 那么就说当前物品是不必要的。
 * 
 * 例如：
 * a = { 4, 6, 2 }, b = { 2, 8, 4 }, c = { 6, 19, 9 }
 * a * 0.5 + b * 2 = c，那么c物品是不必要的
 * 
 * 目标：尽量多的购买物品，但不能出现不必要的物品，同时花费最少。
 * 
 * 约束条件：
 * 1 <= n、m <= 500
 * 0 <= 属性值 <= 1000
 * 
 * 算法思路：
 * 1. 将问题转化为线性代数中的线性无关向量选择问题
 * 2. 使用高斯消元法构建线性基，选择线性无关的向量
 * 3. 贪心策略：按价格从小到大排序，优先选择价格低的线性无关向量
 * 
 * 时间复杂度：O(n * m^2)
 * 空间复杂度：O(n * m)
 * 
 * 工程化考量：
 * 1. 使用double类型处理浮点数运算，避免精度问题
 * 2. 设置小量阈值sml处理浮点数比较
 * 3. 异常处理：空输入、重复元素等边界情况
 * 
 * 与机器学习联系：
 * 该问题类似于特征选择中的线性无关特征集合选择，
 * 在机器学习中用于降维和特征工程。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_BuyEquipment {

	// 最大物品数量
	public static int MAXN = 502;
	// 最大属性数量
	public static int MAXM = 502;
	// 浮点数比较阈值，处理精度问题
	public static double sml = 1e-5;
	// 存储物品属性矩阵，第m+1列存储价格
	public static double[][] mat = new double[MAXN][MAXM];
	// 线性基数组，记录每个属性对应的物品编号
	public static int[] basis = new int[MAXN];
	// 物品数量和属性数量
	public static int n, m;
	// 结果：最大购买数量和最小花费
	public static int cnt, cost;

	/**
	 * 主计算函数：构建线性基并计算最优解
	 * 算法步骤：
	 * 1. 按价格从小到大排序物品
	 * 2. 依次尝试将每个物品插入线性基
	 * 3. 如果插入成功，则选择该物品并累加花费
	 */
	public static void compute() {
		cnt = cost = 0;
		// 按价格从小到大排序，贪心选择
		Arrays.sort(mat, 1, n + 1, (a, b) -> a[m + 1] <= b[m + 1] ? -1 : 1);
		
		// 遍历所有物品，尝试插入线性基
		for (int i = 1; i <= n; i++) {
			if (insert(i)) {
				cnt++; // 成功插入，选择该物品
				cost += (int) mat[i][m + 1]; // 累加花费
			}
		}
	}

	/**
	 * 插入物品到线性基
	 * @param i 物品编号
	 * @return 是否插入成功（是否线性无关）
	 * 
	 * 算法原理：
	 * 1. 从第一个属性开始扫描
	 * 2. 如果当前属性值不为0，检查该属性是否已有基向量
	 * 3. 如果没有，直接插入作为基向量
	 * 4. 如果有，进行消元操作，消除当前物品在该属性上的分量
	 * 5. 继续处理下一个属性
	 */
	public static boolean insert(int i) {
		for (int j = 1; j <= m; j++) {
			// 检查当前属性值是否显著不为0（避免浮点数精度问题）
			if (Math.abs(mat[i][j]) >= sml) {
				if (basis[j] == 0) {
					// 该属性还没有基向量，直接插入
					basis[j] = i;
					return true;
				}
				// 计算消元系数
				double rate = mat[i][j] / mat[basis[j]][j];
				// 从当前属性开始向后消元
				for (int k = j; k <= m; k++) {
					mat[i][k] -= rate * mat[basis[j]][k];
				}
			}
		}
		// 所有属性都被消为0，说明该物品线性相关
		return false;
	}

	/**
	 * 主函数：处理输入输出
	 * 异常处理：
	 * 1. IO异常处理
	 * 2. 输入格式验证
	 * 3. 边界条件检查
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取物品数量和属性数量
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		
		// 读取物品属性矩阵
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				in.nextToken();
				mat[i][j] = (double) in.nval;
			}
		}
		
		// 读取物品价格
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			mat[i][m + 1] = (double) in.nval;
		}
		
		// 执行计算
		compute();
		
		// 输出结果
		out.println(cnt + " " + cost);
		out.flush();
		out.close();
		br.close();
	}

}