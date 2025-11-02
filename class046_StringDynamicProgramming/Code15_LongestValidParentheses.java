/**
 * 最长有效括号（Longest Valid Parentheses）
 * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度
 * 
 * 题目来源：LeetCode 32. 最长有效括号
 * 测试链接：https://leetcode.cn/problems/longest-valid-parentheses/
 * 
 * 算法核心思想：
 * 提供三种解法：
 * 1. 动态规划：通过构建一维DP数组计算以每个位置结尾的最长有效括号长度
 * 2. 栈：使用栈存储索引，通过匹配括号计算有效长度
 * 3. 双向扫描：通过两次扫描（从左到右和从右到左）计算最长有效括号
 * 
 * 时间复杂度分析：
 * - 动态规划版本：O(n)
 * - 栈版本：O(n)
 * - 双向扫描版本：O(n)
 * 
 * 空间复杂度分析：
 * - 动态规划版本：O(n)
 * - 栈版本：O(n)
 * - 双向扫描版本：O(1)
 * 
 * 最优解判定：
 * - 动态规划：适合需要记录详细状态的场景
 * - 栈：直观易懂，适合理解问题本质
 * - 双向扫描：✅ 是最优解，空间复杂度最优
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串和极端情况
 * 3. 性能优化：提供多种解法满足不同需求
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 编译原理：语法分析和括号匹配
 * - 表达式求值：数学表达式解析
 * - 数据结构：栈的应用和动态规划
 */
public class Code15_LongestValidParentheses {

	/*
	 * 算法思路：
	 * 使用动态规划解决最长有效括号问题
	 * dp[i] 表示以索引为 i 的字符结尾的最长有效括号的长度
	 * 
	 * 状态转移方程：
	 * 如果 s[i] 是 '('，则 dp[i] = 0（以左括号结尾的子串无法构成有效括号）
	 * 如果 s[i] 是 ')'：
	 *   1. 如果 s[i-1] 是 '('，则 dp[i] = dp[i-2] + 2（形如"...()"）
	 *   2. 如果 s[i-1] 是 ')'，且 s[i - dp[i-1] - 1] 是 '('，则 dp[i] = dp[i-1] + 2 + dp[i - dp[i-1] - 2]（形如"...(())"）
	 * 
	 * 边界条件：
	 * dp[0] = 0（单个字符无法构成有效括号）
	 * 
	 * 时间复杂度：O(n)，其中n为字符串s的长度
	 * 空间复杂度：O(n)
	 */
	public static int longestValidParentheses1(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况处理
		if (s.length() < 2) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// dp[i] 表示以str[i]结尾的最长有效括号子串的长度
		int[] dp = new int[n];
		
		// 记录最长有效括号子串的长度
		int maxLen = 0;
		
		// 从第二个字符开始遍历（索引为1）
		for (int i = 1; i < n; i++) {
			// 只有右括号才可能形成有效括号
			if (str[i] == ')') {
				if (str[i - 1] == '(') {
					// 情况1: "...()" 形式的有效括号
					// 长度等于前两个字符的有效长度加2
					dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
				} else if (dp[i - 1] > 0) {
					// 情况2: "...))" 形式，需要检查前面是否有匹配的左括号
					// 计算匹配的左括号位置
					int matchIndex = i - dp[i - 1] - 1;
					
					// 检查匹配的左括号是否存在且确实为左括号
					if (matchIndex >= 0 && str[matchIndex] == '(') {
						// 长度等于内部有效括号长度加2，再加上前面可能连接的有效括号长度
						dp[i] = dp[i - 1] + 2;
						
						// 加上前面可能连接的有效括号子串的长度
						if (matchIndex > 0) {
							dp[i] += dp[matchIndex - 1];
						}
					}
				}
				
				// 更新最大长度
				maxLen = Math.max(maxLen, dp[i]);
			}
			// 对于'('，dp[i]保持为0（以左括号结尾无法构成有效括号）
		}
		
		return maxLen;
	}

	/*
	 * 优化版本：使用栈
	 * 栈中存储未匹配的左括号索引和上一个未匹配的右括号索引
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
	public static int longestValidParentheses2(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况处理
		if (s.length() < 2) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// 记录最长有效括号子串的长度
		int maxLen = 0;
		
		// 栈中存储索引，初始放入-1作为基准
		java.util.Stack<Integer> stack = new java.util.Stack<>();
		stack.push(-1);
		
		for (int i = 0; i < n; i++) {
			if (str[i] == '(') {
				// 遇到左括号，将其索引入栈
				stack.push(i);
			} else {
				// 遇到右括号，先弹出栈顶元素
				stack.pop();
				
				if (stack.isEmpty()) {
					// 栈为空，说明这个右括号没有匹配的左括号
					// 将其索引入栈作为新的基准
					stack.push(i);
				} else {
					// 栈不为空，计算当前有效括号子串的长度
					// 当前位置减去栈顶元素（上一个未匹配位置）即为有效长度
					maxLen = Math.max(maxLen, i - stack.peek());
				}
			}
		}
		
		return maxLen;
	}
	
	/*
	 * 优化版本：双向扫描
	 * 不需要额外空间，通过两次扫描（从左到右和从右到左）来找到最长有效括号
	 * 
	 * 核心思想：
	 * 1. 从左到右扫描：统计左括号和右括号的数量，当右括号数量超过左括号时重置计数
	 * 2. 从右到左扫描：统计左括号和右括号的数量，当左括号数量超过右括号时重置计数
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(1)
	 */
	public static int longestValidParentheses3(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况处理
		if (s.length() < 2) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// 记录左括号和右括号的数量
		int left = 0, right = 0;
		
		// 记录最长有效括号子串的长度
		int maxLen = 0;
		
		// 从左到右扫描
		for (int i = 0; i < n; i++) {
			if (str[i] == '(') {
				left++;
			} else {
				right++;
			}
			
			if (left == right) {
				// 左右括号数量相等，形成有效括号子串
				maxLen = Math.max(maxLen, left + right);
			} else if (right > left) {
				// 右括号数量超过左括号，说明当前子串无法形成有效括号
				// 重置计数器，重新开始统计
				left = right = 0;
			}
		}
		
		// 重置计数器，从右到左扫描
		left = right = 0;
		for (int i = n - 1; i >= 0; i--) {
			if (str[i] == '(') {
				left++;
			} else {
				right++;
			}
			
			if (left == right) {
				// 左右括号数量相等，形成有效括号子串
				maxLen = Math.max(maxLen, left + right);
			} else if (left > right) {
				// 左括号数量超过右括号，说明当前子串无法形成有效括号
				// 重置计数器，重新开始统计
				left = right = 0;
			}
		}
		
		return maxLen;
	}
	
	/*
	 * 单元测试
	 */
	public static void main(String[] args) {
		// 测试用例1: "(()"
		// 预期输出: 2 (子串 "()")
		System.out.println("Test 1: " + longestValidParentheses1("(()")); // 应输出2
		System.out.println("Test 1 (Stack): " + longestValidParentheses2("(()")); // 应输出2
		System.out.println("Test 1 (Two Pass): " + longestValidParentheses3("(()")); // 应输出2
		
		// 测试用例2: ")()())"
		// 预期输出: 4 (子串 "()()")
		System.out.println("Test 2: " + longestValidParentheses1(")()())")); // 应输出4
		System.out.println("Test 2 (Stack): " + longestValidParentheses2(")()())")); // 应输出4
		System.out.println("Test 2 (Two Pass): " + longestValidParentheses3(")()())")); // 应输出4
		
		// 测试用例3: ""
		// 预期输出: 0
		System.out.println("Test 3: " + longestValidParentheses1("")); // 应输出0
		System.out.println("Test 3 (Stack): " + longestValidParentheses2("")); // 应输出0
		System.out.println("Test 3 (Two Pass): " + longestValidParentheses3("")); // 应输出0
		
		// 测试用例4: "(())"
		// 预期输出: 4
		System.out.println("Test 4: " + longestValidParentheses1("(())")); // 应输出4
		System.out.println("Test 4 (Stack): " + longestValidParentheses2("(())")); // 应输出4
		System.out.println("Test 4 (Two Pass): " + longestValidParentheses3("(())")); // 应输出4
		
		// 测试用例5: "()(()"
		// 预期输出: 2
		System.out.println("Test 5: " + longestValidParentheses1("()(()")); // 应输出2
		System.out.println("Test 5 (Stack): " + longestValidParentheses2("()(()")); // 应输出2
		System.out.println("Test 5 (Two Pass): " + longestValidParentheses3("()(()")); // 应输出2
	}
}