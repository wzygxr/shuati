package class137;

/**
 * 线性基模板题 - 基础线性基算法应用
 * 
 * 题目来源：洛谷 P3812 【模板】线性基
 * 题目链接：https://www.luogu.com.cn/problem/P3812
 * 
 * 题目描述：
 * 给定n个整数（数字可能重复），求在这些数中选取任意个，使得他们的异或和最大。
 * 
 * 约束条件：
 * 1 <= n <= 50
 * 0 <= 数字 <= 2^50
 * 
 * 算法思路：
 * 1. 构建线性基：将每个数字插入到线性基中
 * 2. 贪心策略：从最高位到最低位，如果当前位能使异或和增大，则选择该位
 * 3. 线性基性质：线性基中的元素能够表示原集合中所有数的异或组合
 * 
 * 时间复杂度：O(n * BIT)，其中BIT=50（数字的最大位数）
 * 空间复杂度：O(BIT)
 * 
 * 工程化考量：
 * 1. 使用long类型处理大数
 * 2. 异常处理：空输入、重复元素等边界情况
 * 3. 代码简洁性和可读性
 * 
 * 与机器学习联系：
 * 该问题类似于特征选择中的最大信息增益选择，
 * 在特征工程中用于选择最具区分度的特征组合。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code08_LinearBasisTemplate {

	// 线性基数组，存储基向量
	public static long[] basis = new long[51]; // 最大50位
	// 二进制位数
	public static int BIT = 50;

	/**
	 * 向线性基中插入数字
	 * @param num 要插入的数字
	 * @return 是否插入成功（是否线性无关）
	 * 
	 * 算法原理：
	 * 1. 从最高位开始扫描
	 * 2. 如果当前位为1，检查该位是否已有基向量
	 * 3. 如果没有，直接插入作为基向量
	 * 4. 如果有，进行异或消元操作
	 * 5. 继续处理下一个位
	 */
	public static boolean insert(long num) {
		for (int i = BIT; i >= 0; i--) {
			// 检查当前位是否为1
			if (((num >> i) & 1) == 1) {
				if (basis[i] == 0) {
					// 该位还没有基向量，直接插入
					basis[i] = num;
					return true;
				}
				// 该位已有基向量，进行消元操作
				num ^= basis[i];
			}
		}
		// 所有位都被消为0，说明该数字线性相关
		return false;
	}

	/**
	 * 查询最大异或和
	 * @return 线性基能表示的最大异或和
	 * 
	 * 算法原理：
	 * 1. 从最高位到最低位遍历线性基
	 * 2. 贪心策略：如果当前位能使异或和增大，则选择该位
	 * 3. 利用线性基的性质：每个基向量都是线性无关的
	 */
	public static long queryMax() {
		long ans = 0;
		for (int i = BIT; i >= 0; i--) {
			// 如果当前位能使异或和增大，则选择该位
			if (((ans ^ basis[i]) > ans)) {
				ans ^= basis[i];
			}
		}
		return ans;
	}

	/**
	 * 查询最小异或和
	 * @return 线性基能表示的最小非零异或和
	 * 
	 * 算法原理：
	 * 1. 线性基的最小非零异或和就是最小的基向量
	 * 2. 如果所有基向量都为0，则最小异或和为0
	 */
	public static long queryMin() {
		for (int i = 0; i <= BIT; i++) {
			if (basis[i] != 0) {
				return basis[i];
			}
		}
		return 0;
	}

	/**
	 * 查询第k小异或和
	 * @param k 第k小
	 * @return 第k小的异或和，如果不存在返回-1
	 * 
	 * 算法原理：
	 * 1. 对线性基进行标准化处理
	 * 2. 将k转换为二进制表示
	 * 3. 根据二进制位选择对应的基向量
	 */
	public static long queryKth(long k) {
		// 统计线性基中非零基向量的数量
		int cnt = 0;
		for (int i = 0; i <= BIT; i++) {
			if (basis[i] != 0) {
				cnt++;
			}
		}
		
		// 如果k大于2^cnt，说明不存在第k小的异或和
		if (k > (1L << cnt)) {
			return -1;
		}
		
		// 对线性基进行标准化处理
		long[] d = new long[BIT + 1];
		for (int i = BIT; i >= 0; i--) {
			if (basis[i] != 0) {
				for (int j = i - 1; j >= 0; j--) {
					if (((basis[i] >> j) & 1) == 1) {
						basis[i] ^= basis[j];
					}
				}
				d[cnt - 1] = basis[i];
			}
		}
		
		// 根据k的二进制位选择基向量
		long ret = 0;
		for (int i = 0; i < cnt; i++) {
			if (((k >> i) & 1) == 1) {
				ret ^= d[i];
			}
		}
		return ret;
	}

	/**
	 * 判断一个数是否能由线性基表示
	 * @param num 要判断的数字
	 * @return 是否能表示
	 * 
	 * 算法原理：
	 * 1. 尝试将数字插入线性基
	 * 2. 如果插入失败，说明该数字能被线性基表示
	 * 3. 如果插入成功，说明该数字不能被线性基表示
	 */
	public static boolean canRepresent(long num) {
		for (int i = BIT; i >= 0; i--) {
			if (((num >> i) & 1) == 1) {
				if (basis[i] == 0) {
					return false;
				}
				num ^= basis[i];
			}
		}
		return num == 0;
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
		
		// 读取数字数量
		in.nextToken();
		int n = (int) in.nval;
		
		// 读取每个数字并插入线性基
		for (int i = 0; i < n; i++) {
			in.nextToken();
			long num = (long) in.nval;
			insert(num);
		}
		
		// 查询并输出最大异或和
		long maxXor = queryMax();
		out.println(maxXor);
		
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 单元测试方法：验证线性基基本功能
	 * 测试用例：
	 * 1. 空输入测试
	 * 2. 单个数字测试
	 * 3. 重复数字测试
	 * 4. 最大异或和测试
	 * 5. 边界值测试
	 */
	public static void testLinearBasis() {
		// 测试用例1：空输入
		basis = new long[51];
		long result1 = queryMax();
		System.out.println("空输入最大异或和: " + result1); // 应为0
		
		// 测试用例2：单个数字
		basis = new long[51];
		insert(5);
		long result2 = queryMax();
		System.out.println("单个数字最大异或和: " + result2); // 应为5
		
		// 测试用例3：重复数字
		basis = new long[51];
		insert(3);
		insert(3);
		long result3 = queryMax();
		System.out.println("重复数字最大异或和: " + result3); // 应为3
		
		// 测试用例4：多个不同数字
		basis = new long[51];
		insert(1);
		insert(2);
		insert(3);
		long result4 = queryMax();
		System.out.println("多个数字最大异或和: " + result4); // 应为3 (1^2=3)
		
		// 测试用例5：边界值
		basis = new long[51];
		insert(0);
		insert(Long.MAX_VALUE);
		long result5 = queryMax();
		System.out.println("边界值最大异或和: " + result5); // 应为Long.MAX_VALUE
	}

}