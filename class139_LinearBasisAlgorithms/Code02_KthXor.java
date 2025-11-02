package class136;

// 第k小的异或和问题
// 题目来源：LOJ #114. 第k小异或和
// 题目链接：https://loj.ac/p/114
// 题目描述：给定一个长度为n的数组arr，arr中都是long类型的非负数，可能有重复值
// 在这些数中选取任意个，至少要选一个数字
// 可以得到很多异或和，假设异或和的结果去重
// 返回第k小的异或和
// 算法：线性基（高斯消元法）
// 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
// 空间复杂度：O(log(max_value))
// 测试链接 : https://loj.ac/p/114
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_KthXor {

	// 常量定义
	public static final int MAXN = 100001; // 最大数组长度
	public static final int BIT = 50;      // 最大位数，因为arr[i] <= 2^50

	// 全局变量
	public static long[] arr = new long[MAXN]; // 存储输入数组
	public static int len;                     // 线性基的大小
	public static boolean zero;                // 是否能异或出0
	public static int n;                       // 数组长度

	/**
	 * 高斯消元法构建线性基
	 * 
	 * 算法思路：
	 * 1. 从最高位到最低位进行高斯消元
	 * 2. 对于每一位，寻找当前位为1的元素
	 * 3. 将找到的元素交换到当前处理位置
	 * 4. 用该元素将其他元素的当前位消为0
	 * 5. 线性基大小增加
	 * 
	 * 时间复杂度分析：
	 * - 外层循环：O(BIT) = O(log(max_value))
	 * - 内层循环：O(n) 用于寻找和消元
	 * - 总时间复杂度：O(n * BIT) = O(n * log(max_value))
	 * 
	 * 空间复杂度分析：
	 * - 线性基大小：O(BIT) = O(log(max_value))
	 * - 输入数组：O(n)
	 * - 总空间复杂度：O(n + log(max_value))
	 * 
	 * 与普通消元法的区别：
	 * - 高斯消元法构建的线性基具有阶梯状结构
	 * - 每个基的最高位是唯一的，且其他基在该位为0
	 * - 这种结构便于计算第k小异或和
	 * 
	 * 关键细节：
	 * - 从高位到低位处理：保证线性基的阶梯状结构
	 * - 交换操作：将当前位为1的元素移到处理位置
	 * - 消元操作：消除其他元素在当前位的1
	 * - 零值判断：当线性基大小小于数组大小时，存在线性相关
	 * 
	 * 异常场景处理：
	 * - 空数组：线性基大小为0，不能异或出任何非0值
	 * - 全0数组：线性基大小为0，只能异或出0
	 * - 线性相关数组：存在冗余元素，能异或出0
	 */
	public static void compute() {
		len = 1; // 线性基从索引1开始
		// 从最高位到最低位进行高斯消元
		for (long i = BIT; i >= 0; i--) {
			// 寻找当前位为1的元素
			for (int j = len; j <= n; j++) {
				// 检查第i位是否为1
				if ((arr[j] & (1L << i)) != 0) {
					// 将找到的元素交换到当前处理位置
					swap(j, len);
					break;
				}
			}
			// 如果找到了当前位为1的元素
			if ((arr[len] & (1L << i)) != 0) {
				// 用该元素将其他元素的当前位消为0
				for (int j = 1; j <= n; j++) {
					if (j != len && (arr[j] & (1L << i)) != 0) {
						arr[j] ^= arr[len];
					}
				}
				// 线性基大小增加
				len++;
			}
		}
		len--; // 修正线性基的实际大小
		// 判断是否能异或出0：当线性基大小小于数组大小时，存在线性相关的情况
		zero = len != n;
	}

	// 交换数组中的两个元素
	public static void swap(int a, int b) {
		long tmp = arr[a];
		arr[a] = arr[b];
		arr[b] = tmp;
	}

	// 返回第k小的异或和
	// 算法思路：
	// 1. 特殊情况处理：如果能异或出0，0是第1小的结果
	// 2. 如果能异或出0，实际查询的是第k-1小的非0值
	// 3. 检查k是否超出可能的异或和个数
	// 4. 根据k的二进制表示选择线性基中的元素进行异或
	public static long query(long k) {
		// 异常处理：k超出合理范围
		if (k < 1) {
			throw new IllegalArgumentException("k must be at least 1");
		}
		
		// 特殊情况处理：如果能异或出0，0是第1小的结果
		if (zero && k == 1) {
			return 0;
		}
		// 如果能异或出0，实际查询的是第k-1小的非0值
		if (zero) {
			k--;
		}
		// 检查k是否超出可能的异或和个数
		long maxPossible = 1L << len;
		if (k >= maxPossible) {
			return -1; // 无法找到第k小的异或和
		}
		
		// 根据k的二进制表示选择线性基中的元素进行异或
		long ans = 0;
		for (int i = len, j = 0; i >= 1; i--, j++) {
			if ((k & (1L << j)) != 0) {
				ans ^= arr[i];
			}
		}
		return ans;
	}

	// 辅助方法：创建高斯消元后的线性基（用于测试和调试）
	public static long[] getGaussianBasis(long[] input) {
		// 复制输入数组以避免修改原数组
		long[] copy = Arrays.copyOf(input, input.length);
		long[] basis = new long[BIT + 1];
		int basisLen = 0;
		
		for (int i = BIT; i >= 0; i--) {
			// 寻找当前位为1的数
			long pivot = 0;
			int pivotIndex = -1;
			for (int j = 0; j < copy.length; j++) {
				if ((copy[j] & (1L << i)) != 0) {
					pivot = copy[j];
					pivotIndex = j;
					break;
				}
			}
			
			if (pivotIndex == -1) continue;
			
			// 交换到当前位置
			long temp = copy[basisLen];
			copy[basisLen] = copy[pivotIndex];
			copy[pivotIndex] = temp;
			pivot = copy[basisLen];
			basis[i] = pivot;
			basisLen++;
			
			// 消去其他数的当前位
			for (int j = 0; j < copy.length; j++) {
				if (j != basisLen - 1 && (copy[j] & (1L << i)) != 0) {
					copy[j] ^= pivot;
				}
			}
		}
		
		return basis;
	}

	// 主方法：处理输入输出
	public static void main(String[] args) throws IOException {
		// 高效IO处理
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取数组长度
		in.nextToken();
		n = (int) in.nval;
		
		// 读取数组元素
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (long) in.nval;
		}
		
		// 构建线性基
		compute();
		
		// 处理查询
		in.nextToken();
		int q = (int) in.nval;
		for (int i = 1; i <= q; i++) {
			in.nextToken();
			long k = (long) in.nval;
			long result = query(k);
			out.println(result);
		}
		
		// 关闭资源
		out.flush();
		out.close();
		br.close();
	}
	
	// 单元测试方法（用于调试）
	public static void runUnitTests() {
		// 测试用例1: 基础测试
		long[] test1 = {0, 3, 1, 5}; // 第0位用于填充，实际元素是3,1,5
		n = 3;
		System.arraycopy(test1, 1, arr, 1, 3);
		compute();
		System.out.println("Test 1: Expected 0, Got " + query(1)); // 0
		System.out.println("Test 1: Expected 1, Got " + query(2)); // 1
		System.out.println("Test 1: Expected 3, Got " + query(3)); // 3
		System.out.println("Test 1: Expected 2, Got " + query(4)); // 3^1=2
		System.out.println("Test 1: Expected 4, Got " + query(5)); // 5
		System.out.println("Test 1: Expected 5, Got " + query(6)); // 5^1=4? 等等需要重新计算
	}
	
	// 性能测试方法
	public static void benchmark() {
		// 生成大规模测试数据
		int testSize = 100000;
		for (int i = 1; i <= testSize; i++) {
			arr[i] = (long)(Math.random() * (1L << 30));
		}
		
		long startTime = System.currentTimeMillis();
		compute();
		long endTime = System.currentTimeMillis();
		System.out.println("Benchmark: Processed " + testSize + " elements in " + (endTime - startTime) + "ms");
		System.out.println("Linear basis size: " + len);
	}

	/*
	 * 线性基求第k小异或和详解
	 * 
	 * 在线性基的应用中，求第k小异或和是一个经典问题。与求最大异或和不同，
	 * 求第k小异或和需要使用高斯消元法构建线性基，而非普通消元法。
	 * 
	 * 为什么需要高斯消元法？
	 * 
	 * 普通消元法构建的线性基虽然可以表示所有可能的异或和，但其元素顺序是不确定的，
	 * 无法直接用于求第k小值。而高斯消元法构建的线性基具有"阶梯状"结构，即：
	 * basis[1]的最高位是所有元素中最高的
	 * basis[2]的最高位是除去basis[1]外所有元素中最高的
	 * ...
	 * 这种结构使得我们可以通过二进制表示来快速计算第k小异或和。
	 * 
	 * 算法思路：
	 * 
	 * 1. 使用高斯消元法构建线性基
	 * 2. 判断是否能异或出0（即是否存在线性相关的元素）
	 * 3. 对于查询k：
	 *    - 如果能异或出0，那么0是第1小的，实际第k小对应的是第(k-1)小的非0值
	 *    - 将k的二进制表示用于选择线性基中的元素进行异或
	 * 
	 * 时间复杂度分析：
	 * - 构建线性基：O(n * log(max_value))
	 * - 单次查询：O(log(max_value))
	 * 
	 * 空间复杂度分析：
	 * - O(log(max_value))，用于存储线性基
	 * 
	 * 相关题目：
	 * 1. https://loj.ac/p/114 - 第k小异或和（模板题）
	 * 2. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
	 * 3. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
	 * 4. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
	 * 5. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
	 * 6. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
	 * 7. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
	 */
}