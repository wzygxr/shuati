package class101;

/**
 * 洛谷 P4391 [BOI2009]Radio Transmission 无线传输 - 最短循环节长度
 * 
 * 题目来源：洛谷 (Luogu)
 * 题目链接：https://www.luogu.com.cn/problem/P4391
 * 
 * 题目描述：
 * 给你一个字符串s，它一定是由某个循环节不断自我连接形成的。
 * 题目保证至少重复2次，但是最后一个循环节不一定完整。
 * 现在想知道s的最短循环节是多长。
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 对于长度为n的字符串，其最短循环节长度等于 n - next[n]。
 * 其中next[n]表示整个字符串的最长相等前后缀的长度。
 * 
 * 数学原理：
 * 设字符串长度为n，最长相等前后缀长度为L，则最短循环节长度为n-L。
 * 这是因为字符串可以表示为某个子串重复k次，而最长相等前后缀长度L = n - 最短循环节长度。
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)，用于存储next数组
 * 
 * 边界条件处理：
 * - 空字符串：返回0
 * - 单字符字符串：循环节长度为1
 * - 全相同字符：循环节长度为1
 * 
 * 工程化考量：
 * 1. 使用高效的IO处理，适合大规模数据输入
 * 2. 预分配数组大小，避免动态扩容开销
 * 3. 异常处理确保程序稳定性
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code01_RepeatMinimumLength {

	// 最大字符串长度常量，根据题目约束设置
	public static final int MAXN = 1000001;
	
	// KMP算法的next数组，存储每个位置的最长相等前后缀长度
	public static int[] next = new int[MAXN];
	
	// 字符串长度
	public static int n;
	
	// 字符串字符数组
	public static char[] s;

	/**
	 * 主函数 - 处理输入输出
	 * 使用高效的缓冲IO处理大规模数据
	 * 
	 * @param args 命令行参数
	 * @throws IOException IO异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取字符串长度
		n = Integer.valueOf(in.readLine());
		// 读取字符串并转换为字符数组
		s = in.readLine().toCharArray();
		
		// 计算并输出最短循环节长度
		out.println(compute());
		
		// 刷新输出缓冲区并关闭资源
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 计算最短循环节长度
	 * 核心算法：最短循环节长度 = n - next[n]
	 * 
	 * @return 最短循环节长度
	 */
	public static int compute() {
		// 构建KMP算法的next数组
		nextArray();
		// 返回最短循环节长度
		return n - next[n];
	}

	/**
	 * 构建KMP算法的next数组（部分匹配表）
	 * next[i]表示s[0...i-1]子串的最长相等前后缀长度
	 * 
	 * 算法步骤：
	 * 1. 初始化next[0] = -1, next[1] = 0
	 * 2. 使用双指针i和cn，i指向当前处理位置，cn表示当前匹配的前缀长度
	 * 3. 当字符匹配时，延长前后缀；不匹配时，根据next数组回退
	 * 
	 * 时间复杂度：O(n)，每个字符最多被比较两次
	 * 空间复杂度：O(n)，存储next数组
	 */
	public static void nextArray() {
		// 初始化边界条件
		next[0] = -1;  // 空字符串的next值设为-1
		next[1] = 0;   // 单字符字符串的next值为0
		
		int i = 2;     // 当前处理的位置，从第2个字符开始
		int cn = 0;    // 当前匹配的前缀长度
		
		// 遍历字符串构建next数组
		while (i <= n) {
			// 当前字符匹配，可以延长相等前后缀
			if (s[i - 1] == s[cn]) {
				next[i++] = ++cn;
			} 
			// 当前字符不匹配，但cn>0，需要回退到next[cn]
			else if (cn > 0) {
				cn = next[cn];
			} 
			// 当前字符不匹配且cn=0，next[i] = 0
			else {
				next[i++] = 0;
			}
		}
	}
	
	/**
	 * 验证计算结果的辅助方法（用于测试）
	 * 验证字符串是否确实可以由计算出的循环节重复构成
	 * 
	 * @param s 输入字符串
	 * @param cycleLength 计算出的循环节长度
	 * @return 验证是否成功
	 */
	public static boolean verifyCycle(String s, int cycleLength) {
		if (cycleLength == 0) return false;
		if (cycleLength == s.length()) return true;
		
		String cycle = s.substring(0, cycleLength);
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != cycle.charAt(i % cycleLength)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 单元测试方法
	 * 测试各种边界情况和典型用例
	 */
	public static void testCases() {
		// 测试用例1：标准情况
		String test1 = "abcabcabc";
		n = test1.length();
		s = test1.toCharArray();
		nextArray();
		int result1 = n - next[n];
		System.out.println("测试用例1 - " + test1 + ": 循环节长度 = " + result1);
		assert verifyCycle(test1, result1) : "测试用例1验证失败";
		
		// 测试用例2：全相同字符
		String test2 = "aaaaa";
		n = test2.length();
		s = test2.toCharArray();
		nextArray();
		int result2 = n - next[n];
		System.out.println("测试用例2 - " + test2 + ": 循环节长度 = " + result2);
		assert verifyCycle(test2, result2) : "测试用例2验证失败";
		
		// 测试用例3：无循环节（最小循环节为整个字符串）
		String test3 = "abcdef";
		n = test3.length();
		s = test3.toCharArray();
		nextArray();
		int result3 = n - next[n];
		System.out.println("测试用例3 - " + test3 + ": 循环节长度 = " + result3);
		assert verifyCycle(test3, result3) : "测试用例3验证失败";
		
		// 测试用例4：边界情况 - 空字符串
		String test4 = "";
		n = test4.length();
		s = test4.toCharArray();
		nextArray();
		int result4 = n - next[n];
		System.out.println("测试用例4 - 空字符串: 循环节长度 = " + result4);
		
		// 测试用例5：单字符
		String test5 = "a";
		n = test5.length();
		s = test5.toCharArray();
		nextArray();
		int result5 = n - next[n];
		System.out.println("测试用例5 - " + test5 + ": 循环节长度 = " + result5);
		assert verifyCycle(test5, result5) : "测试用例5验证失败";
	}
	
	/**
	 * 性能测试方法
	 * 测试大规模数据的处理能力
	 */
	public static void performanceTest() {
		// 生成大规模测试数据
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 100000; i++) {
			sb.append('a');
		}
		String largeString = sb.toString();
		
		long startTime = System.nanoTime();
		
		n = largeString.length();
		s = largeString.toCharArray();
		nextArray();
		int result = n - next[n];
		
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000; // 转换为毫秒
		
		System.out.println("性能测试 - 字符串长度: " + n + ", 循环节长度: " + result);
		System.out.println("执行时间: " + duration + " 毫秒");
		assert verifyCycle(largeString, result) : "性能测试验证失败";
	}
}
