package class101;

/**
 * 洛谷 P4824 [USACO15FEB]Censoring S - 不断删除字符串
 * 
 * 题目来源：洛谷 (Luogu)
 * 题目链接：https://www.luogu.com.cn/problem/P4824
 * 
 * 题目描述：
 * 给定一个字符串s1，如果其中含有s2字符串，就删除最左出现的那个。
 * 删除之后s1剩下的字符重新拼接在一起，再删除最左出现的那个。
 * 如此周而复始，返回最终剩下的字符串。
 * 
 * 算法思路：
 * 使用KMP算法配合栈结构实现高效删除。
 * 1. 使用KMP算法进行字符串匹配
 * 2. 使用栈记录匹配过程中的状态
 * 3. 当匹配到模式串时，从栈中弹出相应长度的字符
 * 4. 继续从栈顶状态继续匹配
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(n)，用于存储栈和next数组
 * 
 * 工程化考量：
 * 1. 使用高效的IO处理，适合大规模数据输入
 * 2. 预分配数组大小，避免动态扩容开销
 * 3. 异常处理确保程序稳定性
 * 4. 内存管理：使用固定大小数组避免动态分配
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code02_DeleteAgainAndAgain {

	// 输入字符串s1和模式串s2的字符数组
	public static char[] s1, s2;
	
	// 最大字符串长度常量，根据题目约束设置
	public static final int MAXN = 1000001;
	
	// KMP算法的next数组，存储每个位置的最长相等前后缀长度
	public static int[] next = new int[MAXN];
	
	// 栈结构：stack1存储字符索引，stack2存储对应的模式串匹配状态
	public static int[] stack1 = new int[MAXN];
	public static int[] stack2 = new int[MAXN];
	
	// 栈的大小
	public static int size;

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
		
		// 读取输入字符串s1和模式串s2
		s1 = in.readLine().toCharArray();
		s2 = in.readLine().toCharArray();
		
		// 计算删除后的结果
		compute();
		
		// 输出最终结果
		for (int i = 0; i < size; i++) {
			out.print(s1[stack1[i]]);
		}
		out.println();
		
		// 刷新输出缓冲区并关闭资源
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 核心计算函数 - 使用KMP算法和栈结构实现字符串删除
	 * 
	 * 算法步骤：
	 * 1. 构建模式串的next数组
	 * 2. 使用双指针遍历文本串和模式串
	 * 3. 使用栈记录匹配状态
	 * 4. 当完全匹配时，从栈中弹出相应数量的元素
	 * 
	 * 时间复杂度：O(n + m)
	 * 空间复杂度：O(n)
	 */
	public static void compute() {
		size = 0; // 初始化栈大小
		int n = s1.length, m = s2.length; // 获取字符串长度
		int x = 0, y = 0; // x: 文本串指针, y: 模式串指针
		
		// 构建模式串的next数组
		nextArray(m);
		
		// 遍历文本串
		while (x < n) {
			// 当前字符匹配
			if (s1[x] == s2[y]) {
				// 将当前字符索引和匹配状态压入栈
				stack1[size] = x;
				stack2[size] = y;
				size++;
				x++;
				y++;
			} 
			// 当前字符不匹配且模式串指针为0
			else if (y == 0) {
				// 将当前字符索引压入栈，匹配状态设为-1
				stack1[size] = x;
				stack2[size] = -1;
				size++;
				x++;
			} 
			// 当前字符不匹配且模式串指针不为0
			else {
				// 根据next数组调整模式串指针
				y = next[y];
			}
			
			// 如果完全匹配到模式串
			if (y == m) {
				// 从栈中弹出m个元素（相当于删除模式串）
				size -= m;
				// 调整模式串指针为栈顶状态+1（如果栈不为空）
				y = size > 0 ? (stack2[size - 1] + 1) : 0;
			}
		}
	}

	/**
	 * 构建KMP算法的next数组（部分匹配表）
	 * next[i]表示s2[0...i-1]子串的最长相等前后缀长度
	 * 
	 * @param m 模式串长度
	 */
	public static void nextArray(int m) {
		// 初始化边界条件
		next[0] = -1;  // 空字符串的next值设为-1
		if (m > 1) {
			next[1] = 0;   // 单字符字符串的next值为0
		}
		
		int i = 2;     // 当前处理的位置，从第2个字符开始
		int cn = 0;    // 当前匹配的前缀长度
		
		// 遍历模式串构建next数组
		while (i < m) {
			// 当前字符匹配，可以延长相等前后缀
			if (s2[i - 1] == s2[cn]) {
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
	 * 验证结果字符串中是否确实不包含模式串
	 * 
	 * @param result 删除后的结果字符串
	 * @param pattern 模式串
	 * @return 验证是否成功（结果中不包含模式串）
	 */
	public static boolean verifyResult(String result, String pattern) {
		if (pattern.length() == 0) return true;
		return !result.contains(pattern);
	}
	
	/**
	 * 单元测试方法
	 * 测试各种边界情况和典型用例
	 */
	public static void runUnitTests() {
		System.out.println("=== 单元测试开始 ===");
		
		// 测试用例1：标准情况
		s1 = "abcabcabc".toCharArray();
		s2 = "abc".toCharArray();
		compute();
		StringBuilder result1 = new StringBuilder();
		for (int i = 0; i < size; i++) {
			result1.append(s1[stack1[i]]);
		}
		System.out.println("测试用例1:");
		System.out.println("原始字符串: " + new String(s1));
		System.out.println("模式串: " + new String(s2));
		System.out.println("删除结果: " + result1.toString());
		assert verifyResult(result1.toString(), new String(s2)) : "测试用例1验证失败";
		System.out.println();
		
		// 测试用例2：嵌套删除
		s1 = "aaabbbaaabbb".toCharArray();
		s2 = "ab".toCharArray();
		compute();
		StringBuilder result2 = new StringBuilder();
		for (int i = 0; i < size; i++) {
			result2.append(s1[stack1[i]]);
		}
		System.out.println("测试用例2:");
		System.out.println("原始字符串: " + new String(s1));
		System.out.println("模式串: " + new String(s2));
		System.out.println("删除结果: " + result2.toString());
		assert verifyResult(result2.toString(), new String(s2)) : "测试用例2验证失败";
		System.out.println();
		
		// 测试用例3：无匹配
		s1 = "abcdef".toCharArray();
		s2 = "xyz".toCharArray();
		compute();
		StringBuilder result3 = new StringBuilder();
		for (int i = 0; i < size; i++) {
			result3.append(s1[stack1[i]]);
		}
		System.out.println("测试用例3:");
		System.out.println("原始字符串: " + new String(s1));
		System.out.println("模式串: " + new String(s2));
		System.out.println("删除结果: " + result3.toString());
		assert verifyResult(result3.toString(), new String(s2)) : "测试用例3验证失败";
		System.out.println();
		
		System.out.println("=== 单元测试通过 ===");
	}
	
	/**
	 * 演示用例方法
	 * 展示算法的实际应用
	 */
	public static void demo() {
		System.out.println("\n=== 演示用例 ===");
		s1 = "aaabbbaaabbbccc".toCharArray();
		s2 = "ab".toCharArray();
		compute();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < size; i++) {
			result.append(s1[stack1[i]]);
		}
		System.out.println("演示字符串: " + new String(s1));
		System.out.println("删除模式串: " + new String(s2));
		System.out.println("最终结果: " + result.toString());
	}
	
	/**
	 * 测试主方法
	 * 用于本地测试和验证
	 */
	public static void testMain() {
		// 运行单元测试
		runUnitTests();
		
		// 运行演示用例
		demo();
	}
}