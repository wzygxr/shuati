package class138;

/**
 * 01分数规划基础题 - Dropping Tests
 * 题目来源：POJ 2976, 洛谷 P10505
 * 题目描述：给定n个数据，每个数据有(a, b)两个值，都为整数，并且都是非负的
 * 请舍弃掉k个数据，希望让剩下数据做到，所有a的和 / 所有b的和，这个比值尽量大
 * 如果剩下数据所有b的和为0，认为无意义
 * 最后，将该比值 * 100，小数部分四舍五入的整数结果返回
 * 数据范围：1 <= n <= 100，0 <= a、b <= 10^9
 * 测试链接：https://www.luogu.com.cn/problem/P10505
 * 测试链接：http://poj.org/problem?id=2976
 * 
 * 补充题目：
 * 1. Codeforces 489E Hiking - 基础01分数规划变体
 * 2. 洛谷 P1642 规划 - 基础01分数规划
 * 3. UVA 1389 Hard Life - 最大密度子图问题
 * 4. 洛谷 U581184 【模板】01-分数规划
 * 
 * 算法思路：使用二分法求解01分数规划问题
 * 时间复杂度：O(log(1/ε) * n log n)，其中ε是精度要求
 * 空间复杂度：O(n)
 * 
 * 01分数规划的数学原理：
 * 我们需要最大化 R = (sum(a_i * x_i)) / (sum(b_i * x_i))，其中x_i∈{0,1}
 * 对于给定的L，判断是否存在x的选择使得 R > L
 * 等价于：sum(a_i * x_i) > L * sum(b_i * x_i)
 * 等价于：sum((a_i - L * b_i) * x_i) > 0
 * 我们通过二分L的值，使用贪心策略判断是否可行
 * 
 * 注意：提交到OJ时请将类名改为"Main"
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.Comparator;

public class Code01_DroppingTests {

	// 常量定义
	public static int MAXN = 1001;  // 最大数据规模
	public static double sml = 1e-6;  // 精度控制，用于二分结束条件

	/**
	 * 二维数组存储数据
	 * arr[i][0] = i号数据的a值
	 * arr[i][1] = i号数据的b值
	 * arr[i][2] = i号数据的结余值，即a - x * b
	 */
	public static double[][] arr = new double[MAXN][3];

	// 全局变量，存储当前数据规模和需要选择的数据个数
	public static int n, k;

	/**
	 * 检查函数：判断给定的比率值x是否可行
	 * 原理：对于当前x，计算每个元素的结余(a_i - x*b_i)，选择结余最大的k个
	 * 如果这k个的和大于等于0，则说明存在更优的比率，可以尝试增大x
	 * 
	 * @param x 当前尝试的比率值
	 * @return 如果x可行返回true，否则返回false
	 * @throws IllegalArgumentException 如果输入参数不合法
	 */
	public static boolean check(double x) {
		// 参数验证
		if (Double.isNaN(x) || Double.isInfinite(x)) {
			throw new IllegalArgumentException("比率值x不能为NaN或无穷大: " + x);
		}
		
		if (n <= 0 || k <= 0 || k > n) {
			throw new IllegalArgumentException("数据规模不合法: n=" + n + ", k=" + k);
		}
		
		// 检查分母是否为0的情况
		boolean allBZero = true;
		for (int i = 1; i <= n; i++) {
			if (arr[i][1] != 0) {
				allBZero = false;
				break;
			}
		}
		
		if (allBZero) {
			// 如果所有b都为0，无法计算比值，返回false
			return false;
		}
		
		// x固定的情况下，计算所有数据的结余值
		for (int i = 1; i <= n; i++) {
			// 检查数值溢出
			if (Double.isInfinite(arr[i][0]) || Double.isInfinite(arr[i][1])) {
				throw new ArithmeticException("输入数据包含无穷大值");
			}
			
			double product = x * arr[i][1];
			if (Double.isInfinite(product)) {
				throw new ArithmeticException("乘法运算溢出: x=" + x + ", b=" + arr[i][1]);
			}
			
			arr[i][2] = arr[i][0] - product;
			
			// 检查减法运算结果
			if (Double.isNaN(arr[i][2])) {
				throw new ArithmeticException("减法运算结果异常: a=" + arr[i][0] + ", product=" + product);
			}
		}
		
		// 将结余值从大到小排序
		try {
			Arrays.sort(arr, 1, n + 1, new MyComparator());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("排序失败: " + e.getMessage(), e);
		}
		
		// 计算最大的k个结余值的累加和
		double sum = 0.0;
		for (int i = 1; i <= k; i++) {
			// 检查数组索引是否越界
			if (i < 1 || i > n) {
				throw new ArrayIndexOutOfBoundsException("数组索引越界: i=" + i + ", n=" + n);
			}
			
			sum += arr[i][2];
			
			// 检查累加和是否溢出
			if (Double.isInfinite(sum)) {
				throw new ArithmeticException("累加和溢出");
			}
		}
		
		// 如果总和大于等于0，说明x可行
		// 使用eps进行比较，避免浮点数精度问题
		return sum >= -sml;
	}

	/**
	 * 比较器类：用于对结余值进行从大到小排序
	 * 注意：POJ平台Java版本较老，不支持Lambda表达式，所以需要定义单独的比较器类
	 */
	public static class MyComparator implements Comparator<double[]> {

		@Override
		public int compare(double[] o1, double[] o2) {
			// 按结余值从大到小排序
			return o1[2] >= o2[2] ? -1 : 1;
		}

	}

	/**
	 * 测试函数：用于验证算法的正确性
	 * 包含多个测试用例，覆盖边界情况和一般情况
	 */
	public static void test() {
		System.out.println("=== 开始测试 Code01_DroppingTests ===");
		
		// 测试用例1：基础测试用例
		System.out.println("测试用例1：基础测试");
		n = 3;
		k = 1; // 舍弃1个，选择2个
		k = n - k; // 实际选择2个
		
		// 设置测试数据
		arr[1][0] = 5; arr[1][1] = 1;
		arr[2][0] = 0; arr[2][1] = 2;
		arr[3][0] = 2; arr[3][1] = 7;
		
		double l = 0.0, r = 0.0;
		for (int i = 1; i <= n; i++) {
			r += arr[i][0];
		}
		
		double ans = 0.0;
		while (l <= r && r - l >= sml) {
			double x = (l + r) / 2.0;
			if (check(x)) {
				ans = x;
				l = x + sml;
			} else {
				r = x - sml;
			}
		}
		
		int result = (int) (100 * (ans + 0.005));
		System.out.println("测试用例1结果：" + result);
		
		// 测试用例2：边界情况测试
		System.out.println("测试用例2：边界情况");
		n = 2;
		k = 0; // 舍弃0个，选择2个
		k = n - k;
		
		arr[1][0] = 100; arr[1][1] = 100;
		arr[2][0] = 50; arr[2][1] = 50;
		
		l = 0.0; r = 0.0;
		for (int i = 1; i <= n; i++) {
			r += arr[i][0];
		}
		
		ans = 0.0;
		while (l <= r && r - l >= sml) {
			double x = (l + r) / 2.0;
			if (check(x)) {
				ans = x;
				l = x + sml;
			} else {
				r = x - sml;
			}
		}
		
		result = (int) (100 * (ans + 0.005));
		System.out.println("测试用例2结果：" + result);
		
		System.out.println("=== 测试完成 ===");
	}

	/**
	 * 主函数：处理输入输出，执行二分查找
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 如果传入参数"test"，则运行测试用例
		if (args.length > 0 && "test".equals(args[0])) {
			test();
			return;
		}
		
		// 使用高效的输入输出方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取第一组数据
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		
		// 处理多组数据，直到输入0 0
		while (n != 0 || k != 0) {
			// 题目要求舍弃k个元素，实际上是选择n-k个元素
			k = n - k;
			
			// 读取a数组
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				arr[i][0] = in.nval;
			}
			
			// 读取b数组
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				arr[i][1] = in.nval;
			}
			
			// 初始化二分查找的左右边界
			// 左边界为0，右边界为可能的最大值（所有a的和）
			double l = 0.0, r = 0.0;
			for (int i = 1; i <= n; i++) {
				r += arr[i][0];
			}
			
			double ans = 0.0;
			// 二分查找过程
			// 当左右边界的差大于精度要求时继续循环
			while (l <= r && r - l >= sml) {
				double x = (l + r) / 2.0;
				if (check(x)) {
					// 如果x可行，记录当前答案，并尝试更大的值
					ans = x;
					l = x + sml; // 注意这里要加上sml，避免死循环
				} else {
					// 如果x不可行，尝试更小的值
					r = x - sml;
				}
			}
			
			// 输出结果，乘以100后四舍五入
			// 使用+0.005的方式实现四舍五入
			out.println((int) (100 * (ans + 0.005)));
			
			// 读取下一组数据
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			k = (int) in.nval;
		}
		
		// 刷新输出缓冲
		out.flush();
		// 关闭资源
		out.close();
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
