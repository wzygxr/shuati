package class138;

/**
 * 01分数规划问题 - 牛群的才艺展示（Talent Show）
 * 
 * <h3>题目信息</h3>
 * <ul>
 *   <li><strong>题目来源</strong>：Luogu P4377，USACO 2018 Open Contest</li>
 *   <li><strong>题目描述</strong>：有n头奶牛，每头奶牛有重量和才艺两个属性值。要求选若干头牛，使得总重量不少于w，
 *   并且选出的牛的才艺的和与重量的和的比值尽量大。返回该比值乘以1000的整数结果，小数部分舍弃。</li>
 *   <li><strong>数据范围</strong>：
 *     <ul>
 *       <li>1 <= n <= 250</li>
 *       <li>1 <= w <= 1000</li>
 *       <li>1 <= 牛的重量 <= 10^6</li>
 *       <li>1 <= 牛的才艺 <= 10^3</li>
 *     </ul>
 *   </li>
 *   <li><strong>测试链接</strong>：<a href="https://www.luogu.com.cn/problem/P4377">Luogu P4377</a></li>
 * </ul>
 * 
 * <h3>算法思路</h3>
 * <p>使用二分法求解01分数规划问题，结合01背包动态规划进行可行性判断：</p>
 * <ol>
 *   <li><strong>二分法</strong>：在可能的比值范围内进行二分查找</li>
 *   <li><strong>背包模型</strong>：对于每个二分中点，将问题转化为判断是否存在总重量至少为w的奶牛选择，
 *   使得总才艺值与总重量的比值大于当前中点值</li>
 * </ol>
 * 
 * <h3>数学原理</h3>
 * <p>我们需要最大化 R = (sum(talent_i * x_i)) / (sum(weight_i * x_i))，其中x_i∈{0,1}且sum(weight_i * x_i) >= W</p>
 * <p>对于给定的L，判断是否存在x的选择使得 R > L：</p>
 * <ul>
 *   <li>等价于：sum(talent_i * x_i) > L * sum(weight_i * x_i)</li>
 *   <li>等价于：sum((talent_i - L * weight_i) * x_i) > 0，且sum(weight_i * x_i) >= W</li>
 * </ul>
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li><strong>时间复杂度</strong>：O(n * W * log(1/ε))，其中ε是精度要求（本题中取1e-6）</li>
 *   <li><strong>空间复杂度</strong>：O(W)，使用滚动数组优化动态规划空间</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>提交到OJ时请将类名改为"Main"</li>
 *   <li>使用高精度数据类型（double）避免计算精度问题</li>
 *   <li>注意处理总重量超过目标重量w的情况</li>
 * </ul>
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_TalentShow {

	// 常量定义
	public static final int MAXN = 251;  // 最大奶牛数量
	public static final int MAXW = 1001; // 最大目标重量W

	// 足够小代表无效解（无法达到的状态）
	public static final double NA = -1e9;

	// 精度控制，用于二分结束条件
	public static final double PRECISION = 1e-6;

	// 重量数组
	public static int[] weight = new int[MAXN];

	// 才艺数组
	public static int[] talent = new int[MAXN];

	/**
	 * 计算每头牛的value值
	 * value[i] = talent[i] - x * weight[i]
	 * 用于01分数规划判断
	 */
	public static double[] value = new double[MAXN];

	/**
	 * dp[j]表示在前i头牛中选择若干头，总重量为j时的最大结余和
	 * 特别地，dp[w]表示总重量>=w时的最大结余和
	 * 采用空间压缩的方式实现，仅用一维数组
	 */
	public static double[] dp = new double[MAXW];

	// 全局变量，存储当前数据规模和目标重量
	public static int n, w;

	/**
	 * 检查函数：判断给定的比率值x是否可行
	 * 
	 * <p>核心思想：将01分数规划问题转化为01背包问题。对于当前比率x，计算每个奶牛的结余值（talent_i - x * weight_i），
	 * 然后求解总重量至少为w的情况下，能否使总结余值大于等于0。</p>
	 * 
	 * <p>动态规划设计：</p>
	 * <ul>
	 *   <li>状态定义：dp[j]表示总重量为j时的最大结余和</li>
	 *   <li>状态转移：对于每头牛，有选或不选两种选择</li>
	 *   <li>特殊处理：当总重量超过w时，统一记录在dp[w]中</li>
	 * </ul>
	 * 
	 * @param x 当前尝试的比率值
	 * @return 如果存在一种选择使得比值大于x，则返回true；否则返回false
	 * @throws IllegalArgumentException 如果输入参数不合法
	 * @throws ArithmeticException 如果数值计算出现异常
	 */
	public static boolean check(double x) {
		// 参数验证
		if (Double.isNaN(x) || Double.isInfinite(x)) {
			throw new IllegalArgumentException("比率值x不能为NaN或无穷大: " + x);
		}
		
		if (n <= 0 || n > MAXN) {
			throw new IllegalArgumentException("奶牛数量不合法: n=" + n + ", 最大允许=" + MAXN);
		}
		
		if (w <= 0 || w > MAXW) {
			throw new IllegalArgumentException("目标重量不合法: w=" + w + ", 最大允许=" + MAXW);
		}
		
		// 检查输入数据有效性
		for (int i = 1; i <= n; i++) {
			if (weight[i] < 0 || talent[i] < 0) {
				throw new IllegalArgumentException("重量或才艺值不能为负数: weight[" + i + "]=" + weight[i] + ", talent[" + i + "]=" + talent[i]);
			}
			
			if (weight[i] > 1000000 || talent[i] > 1000) {
				throw new IllegalArgumentException("重量或才艺值超出范围: weight[" + i + "]=" + weight[i] + ", talent[" + i + "]=" + talent[i]);
			}
		}
		
		// 计算每头牛的value值（才艺值减去x倍的重量值）
		for (int i = 1; i <= n; i++) {
			// 检查数值溢出
			double product = x * weight[i];
			if (Double.isInfinite(product)) {
				throw new ArithmeticException("乘法运算溢出: x=" + x + ", weight=" + weight[i]);
			}
			
			value[i] = (double) talent[i] - product;
			
			// 检查减法运算结果
			if (Double.isNaN(value[i])) {
				throw new ArithmeticException("减法运算结果异常: talent=" + talent[i] + ", product=" + product);
			}
		}
		
		// 初始化dp数组
		dp[0] = 0.0; // 初始状态：重量为0时，结余和为0
		
		// 检查数组边界
		if (w < 0 || w >= MAXW) {
			throw new ArrayIndexOutOfBoundsException("目标重量w超出数组范围: w=" + w);
		}
		
		// 初始化其余状态为无效值
		for (int j = 1; j <= w; j++) {
			dp[j] = NA;
		}
		
		// 01背包动态规划过程
		for (int i = 1; i <= n; i++) { // 遍历每头牛
			// 倒序遍历重量，避免重复选择同一头牛
			for (int p = w; p >= 0; p--) {
				if (dp[p] != NA) { // 如果当前状态有效（可达）
					int j = p + weight[i]; // 计算选择当前牛后的总重量
					
					// 检查加法运算是否溢出
					if (j < 0) {
						throw new ArithmeticException("重量加法溢出: p=" + p + ", weight=" + weight[i]);
					}
					
					// 检查数组索引是否越界
					if (j >= MAXW) {
						j = w; // 统一处理到w
					}
					
					// 计算新的结余和
					double newValue = dp[p] + value[i];
					
					// 检查加法运算是否溢出
					if (Double.isInfinite(newValue)) {
						throw new ArithmeticException("结余和加法溢出: dp[" + p + "]=" + dp[p] + ", value[" + i + "]=" + value[i]);
					}
					
					// 两种情况处理：
					// 1. 如果总重量超过或等于目标重量w，统一更新dp[w]
					// 2. 否则更新对应重量的dp值
					if (j >= w) {
						if (w >= 0 && w < MAXW) {
							dp[w] = Math.max(dp[w], newValue);
						}
					} else {
						if (j >= 0 && j < MAXW) {
							dp[j] = Math.max(dp[j], newValue);
						}
					}
				}
			}
		}
		
		// 判断条件：如果总重量>=w时的最大结余和>=0，说明存在更优的解
		// 使用eps进行比较，避免浮点数精度问题
		return dp[w] >= -PRECISION;
	}

	/**
	 * 测试函数：用于验证算法的正确性
	 * 包含多个测试用例，覆盖边界情况和一般情况
	 */
	public static void test() {
		System.out.println("=== 开始测试 Code02_TalentShow ===");
		
		// 测试用例1：基础测试用例
		System.out.println("测试用例1：基础测试");
		n = 3;
		w = 5;
		
		// 设置测试数据
		weight[1] = 1; talent[1] = 2;
		weight[2] = 2; talent[2] = 2;
		weight[3] = 3; talent[3] = 1;
		
		double left = 0.0, right = 0.0;
		for (int i = 1; i <= n; i++) {
			right += talent[i];
		}

		double result = 0.0;
		while (left < right && right - left >= PRECISION) {
			double mid = (left + right) / 2;
			if (check(mid)) {
				result = mid;
				left = mid + PRECISION;
			} else {
				right = mid - PRECISION;
			}
		}

		int output = (int) (result * 1000);
		System.out.println("测试用例1结果：" + output);
		
		// 测试用例2：边界情况测试
		System.out.println("测试用例2：边界情况");
		n = 2;
		w = 1;
		
		weight[1] = 1; talent[1] = 100;
		weight[2] = 1; talent[2] = 50;
		
		left = 0.0; right = 0.0;
		for (int i = 1; i <= n; i++) {
			right += talent[i];
		}

		result = 0.0;
		while (left < right && right - left >= PRECISION) {
			double mid = (left + right) / 2;
			if (check(mid)) {
				result = mid;
				left = mid + PRECISION;
			} else {
				right = mid - PRECISION;
			}
		}

		output = (int) (result * 1000);
		System.out.println("测试用例2结果：" + output);
		
		System.out.println("=== 测试完成 ===");
	}

	/**
	 * 主函数：处理输入输出，执行二分查找算法
	 * 
	 * <p>算法流程：</p>
	 * <ol>
	 *   <li>读取输入数据</li>
	 *   <li>初始化二分查找的左右边界</li>
	 *   <li>进行二分查找，每次调用check函数判断当前比率是否可行</li>
	 *   <li>输出结果，将最优比值乘以1000后取整数部分</li>
	 * </ol>
	 */
	public static void main(String[] args) {
		// 如果传入参数"test"，则运行测试用例
		if (args.length > 0 && "test".equals(args[0])) {
			test();
			return;
		}
		
		BufferedReader br = null;
		PrintWriter out = null;
		
		try {
			// 使用高效的输入输出流处理大数据
			br = new BufferedReader(new InputStreamReader(System.in));
			StreamTokenizer in = new StreamTokenizer(br);
			out = new PrintWriter(new OutputStreamWriter(System.out));
			
			// 读取奶牛数量和目标重量
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			w = (int) in.nval;
			
			// 读取每头奶牛的重量和才艺值
			for (int i = 1; i <= n; i++) {
				in.nextToken();
				weight[i] = (int) in.nval;
				in.nextToken();
				talent[i] = (int) in.nval;
			}

			// 初始化二分查找的左右边界
			// 左边界为0，右边界为所有才艺值的和（最大可能比值的上界）
			double left = 0.0, right = 0.0;
			for (int i = 1; i <= n; i++) {
				right += talent[i];
			}

			double result = 0.0;
			// 二分查找过程
			// 当左右边界的差大于精度要求时继续循环
			while (left < right && right - left >= PRECISION) {
				double mid = (left + right) / 2; // 取中点作为当前尝试的比率值
				if (check(mid)) { // 如果mid可行，说明可以尝试更大的值
					result = mid; // 记录当前最优解
					left = mid + PRECISION; // 左边界右移
				} else { // 如果mid不可行，需要尝试更小的值
					right = mid - PRECISION; // 右边界左移
				}
			}

			// 输出结果，将比值乘以1000后取整数部分（向下取整）
			out.println((int) (result * 1000));
			out.flush();
			
		} catch (IOException e) {
			// 处理输入输出异常
			e.printStackTrace();
		} finally {
			// 确保资源正确关闭
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (out != null) {
				out.close();
			}
		}
	}

}
