package class054;

/**
 * 最小覆盖子串 - 滑动窗口与哈希表结合应用
 * 
 * 【题目背景】
 * 这是一道经典的滑动窗口应用题，需要找到包含目标字符串所有字符的最小子串。
 * 该问题在字符串处理和模式匹配领域有重要应用。
 * 
 * 【题目描述】
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
 * 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 注意：
 * 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 * 如果 s 中存在这样的子串，我们保证它是唯一的答案。
 * 测试链接：https://leetcode.cn/problems/minimum-window-substring/
 * 
 * 【核心算法思想】
 * 1. 滑动窗口技术：使用双指针维护一个变长窗口
 * 2. 哈希表记录：使用数组记录目标字符串t中每个字符的出现次数
 * 3. 计数器机制：记录还需要匹配的字符种类数
 * 4. 窗口收缩策略：当窗口包含t的所有字符时，尝试收缩窗口以找到最小覆盖子串
 * 
 * 【算法复杂度分析】
 * - 时间复杂度：O(n) - 每个字符最多被访问两次（右指针一次，左指针一次）
 * - 空间复杂度：O(1) - 字符集大小固定（ASCII字符集最多256个字符）
 * 
 * 【工程化考量】
 * 1. 字符集处理：支持ASCII字符集，可扩展为Unicode字符集
 * 2. 边界检查：处理空字符串、无效输入等边界情况
 * 3. 性能优化：使用数组代替哈希表提高访问效率
 * 4. 代码可读性：清晰的变量命名和算法步骤注释
 * 
 * 【面试要点】
 * - 理解滑动窗口在字符串匹配中的应用
 * - 能够解释哈希表（数组）在字符计数中的作用
 * - 分析时间复杂度的均摊分析原理
 * - 处理各种边界情况和特殊输入
 */
//
// 【题目解析与算法深度分析】
// 这是滑动窗口的经典变种，虽然不是直接使用单调队列，但可以使用单调队列的思想来优化。
// 我们可以用计数方式模拟单调队列的行为。
//
// 【算法思路详细分解】
// 1. 滑动窗口技术：使用双指针维护窗口边界
// 2. 字符频次统计：用数组代替哈希表记录字符出现次数
// 3. 匹配计数器：记录窗口中满足目标字符频次要求的字符种类数
// 4. 窗口扩展策略：右指针移动，扩展窗口直到包含所有目标字符
// 5. 窗口收缩策略：左指针移动，收缩窗口以找到最小覆盖子串
//
// 【时间复杂度数学证明】
// - 每个字符最多被右指针访问一次（扩展窗口）
// - 每个字符最多被左指针访问一次（收缩窗口）
// - 总操作次数为O(n)，因此时间复杂度为O(n)
//
// 【空间复杂度分析】
// - 使用固定大小的计数数组（128个元素）
// - 因此空间复杂度为O(1)，与字符集大小相关
//
// 【工程化实现要点】
// 1. 使用数组代替哈希表：提高访问效率，减少哈希冲突
// 2. 边界条件处理：空字符串、无效输入等
// 3. 异常处理：字符集超出范围时的处理策略
// 4. 性能优化：避免不必要的字符串操作

public class Code04_MinimumWindowSubstring {
	
	/**
	 * 寻找最小覆盖子串的核心算法实现
	 * 
	 * 【算法原理深度解析】
	 * 本算法通过滑动窗口技术结合字符计数数组，实现高效的最小覆盖子串查找。
	 * 关键设计要点：
	 * 1. 字符计数数组：使用固定大小的数组记录目标字符串t中每个字符的出现次数
	 * 2. 滑动窗口：通过双指针控制窗口范围，动态调整窗口大小
	 * 3. 匹配计数器：记录还需要匹配的字符种类数，当计数器为0时表示窗口包含所有字符
	 * 4. 最小子串记录：在满足条件时记录最小覆盖子串的起始位置和长度
	 * 
	 * 【时间复杂度数学证明】
	 * 虽然算法包含嵌套循环，但通过均摊分析可知：
	 * - 每个字符最多被右指针访问一次（扩展窗口）
	 * - 每个字符最多被左指针访问一次（收缩窗口）
	 * - 总操作次数为O(n)，因此时间复杂度为O(n)
	 * 
	 * 【空间复杂度分析】
	 * - 使用固定大小的计数数组（128个元素）
	 * - 因此空间复杂度为O(1)
	 * 
	 * @param s 源字符串
	 * @param t 目标字符串
	 * @return s中涵盖t所有字符的最小子串，如果不存在返回空字符串
	 * 
	 * 【测试用例覆盖】
	 * - 常规测试："ADOBECODEBANC", "ABC" → "BANC"
	 * - 边界测试：空字符串、单字符、完全匹配等
	 * - 特殊测试：重复字符、不包含目标字符等
	 * 
	 * 【工程化优化点】
	 * 1. 使用数组代替哈希表：提高访问效率，减少哈希冲突
	 * 2. 边界条件检查：处理空字符串、无效输入等
	 * 3. 性能监控：可添加性能统计代码
	 * 4. 异常处理：字符集超出范围时的处理策略
	 */
	public static String minWindow(String s, String t) {
		// 【边界条件检查】处理空字符串和无效输入
		if (s == null || t == null || s.length() == 0 || t.length() == 0) {
			return "";
		}
		
		// 【字符计数数组初始化】
		// 使用数组代替哈希表，提高访问效率
		// ASCII码范围是0-127，这里用128足够覆盖所有ASCII字符
		// 工程化考量：如果支持Unicode，需要扩展数组大小或使用HashMap
		int[] need = new int[128]; // 记录目标字符串t中每个字符需要的频次
		int[] window = new int[128]; // 记录当前窗口中每个字符的实际频次
		
		// 【目标字符频次统计】
		// 遍历目标字符串t，统计每个字符的出现次数
		// 时间复杂度：O(m)，其中m是字符串t的长度
		for (int i = 0; i < t.length(); i++) {
			char c = t.charAt(i);
			// 字符范围检查：确保字符在ASCII范围内
			if (c < 0 || c >= 128) {
				throw new IllegalArgumentException("字符超出ASCII范围: " + c);
			}
			need[c]++;
		}
		
		// 【滑动窗口指针初始化】
		int left = 0, right = 0; // 滑动窗口的左右边界指针
		int valid = 0; // 窗口中满足need条件的字符种类数（不是字符个数）
		int start = 0, len = Integer.MAX_VALUE; // 记录最小覆盖子串的起始位置和长度
		
		// 【滑动窗口主循环 - 右指针扩展】
		// 时间复杂度：O(n)，每个字符最多被访问一次
		while (right < s.length()) {
			// 获取当前右指针指向的字符
			char c = s.charAt(right);
			// 右指针右移，扩展窗口
			right++;
			
			// 【窗口数据更新逻辑】
			// 只有当字符c是目标字符串t中的字符时才需要处理
			if (need[c] > 0) {
				// 增加窗口中字符c的计数
				window[c]++;
				// 【关键匹配条件判断】
				// 当窗口中字符c的数量刚好等于目标需要的数量时，valid计数器加1
				// 注意：这里使用"等于"而不是"大于等于"，避免重复计数
				if (window[c] == need[c]) {
					valid++;
				}
			}
			
			// 【窗口收缩条件判断】
			// 当窗口中包含所有目标字符时（valid等于目标字符种类数），开始收缩窗口
			// 这是滑动窗口算法的核心：找到满足条件的最小窗口
			while (valid == getValidCount(need)) {
				// 【更新最小覆盖子串】
				// 检查当前窗口是否比之前记录的最小窗口更小
				if (right - left < len) {
					start = left;    // 记录新的起始位置
					len = right - left; // 记录新的窗口长度
				}
				
				// 【左指针移动 - 窗口收缩】
				// 获取当前左指针指向的字符
				char d = s.charAt(left);
				// 左指针右移，收缩窗口
				left++;
				
				// 【窗口收缩时的数据更新】
				// 只有当移出的字符是目标字符时才需要处理
				if (need[d] > 0) {
					// 【关键匹配条件判断】
					// 如果移出前窗口中字符d的数量刚好等于目标数量，移出后valid需要减1
					if (window[d] == need[d]) {
						valid--;
					}
					// 减少窗口中字符d的计数
					window[d]--;
				}
			}
		}
		
		// 【结果返回处理】
		// 如果len仍然是初始值，说明没有找到满足条件的子串，返回空字符串
		// 否则返回从start开始长度为len的子串
		return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
	}
	
	/**
	 * 计算目标字符串中不同字符的种类数
	 * 
	 * 【算法原理】
	 * 遍历计数数组，统计出现次数大于0的字符种类数
	 * 
	 * 【时间复杂度】O(1) - 因为数组大小固定为128
	 * 【空间复杂度】O(1) - 只使用常数空间
	 * 
	 * @param need 目标字符计数数组
	 * @return 不同字符的种类数
	 * 
	 * 【工程化考量】
	 * 1. 可缓存结果避免重复计算（如果need数组不变）
	 * 2. 支持字符集扩展时的动态调整
	 */
	private static int getValidCount(int[] need) {
		int count = 0;
		// 遍历整个字符集（ASCII 0-127）
		for (int i = 0; i < need.length; i++) {
			if (need[i] > 0) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 单元测试方法 - 验证算法正确性
	 * 
	 * 【测试用例设计原则】
	 * 1. 常规测试：标准输入输出验证
	 * 2. 边界测试：空字符串、单字符等
	 * 3. 特殊测试：重复字符、不包含目标字符等
	 * 4. 性能测试：大数据量验证
	 */
	public static void main(String[] args) {
		// 测试用例1：标准测试
		String s1 = "ADOBECODEBANC";
		String t1 = "ABC";
		String result1 = minWindow(s1, t1);
		System.out.println("测试1 - 输入: s=\"" + s1 + "\", t=\"" + t1 + "\"");
		System.out.println("期望输出: \"BANC\"");
		System.out.println("实际输出: \"" + result1 + "\"");
		System.out.println("测试结果: " + ("BANC".equals(result1) ? "通过" : "失败"));
		System.out.println();
		
		// 测试用例2：边界测试 - 空字符串
		String s2 = "";
		String t2 = "ABC";
		String result2 = minWindow(s2, t2);
		System.out.println("测试2 - 空字符串测试");
		System.out.println("期望输出: \"\"");
		System.out.println("实际输出: \"" + result2 + "\"");
		System.out.println("测试结果: " + ("".equals(result2) ? "通过" : "失败"));
		System.out.println();
		
		// 测试用例3：完全匹配
		String s3 = "ABC";
		String t3 = "ABC";
		String result3 = minWindow(s3, t3);
		System.out.println("测试3 - 完全匹配测试");
		System.out.println("期望输出: \"ABC\"");
		System.out.println("实际输出: \"" + result3 + "\"");
		System.out.println("测试结果: " + ("ABC".equals(result3) ? "通过" : "失败"));
		System.out.println();
		
		// 测试用例4：不包含目标字符
		String s4 = "DEFGH";
		String t4 = "ABC";
		String result4 = minWindow(s4, t4);
		System.out.println("测试4 - 不包含目标字符测试");
		System.out.println("期望输出: \"\"");
		System.out.println("实际输出: \"" + result4 + "\"");
		System.out.println("测试结果: " + ("".equals(result4) ? "通过" : "失败"));
		System.out.println();
		
		// 性能测试：大数据量验证
		System.out.println("性能测试开始...");
		long startTime = System.currentTimeMillis();
		
		// 构造大数据量测试
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}
		String s5 = sb.toString();
		String t5 = "XYZ";
		
		String result5 = minWindow(s5, t5);
		long endTime = System.currentTimeMillis();
		
		System.out.println("性能测试 - 数据量: " + s5.length() + " 字符");
		System.out.println("执行时间: " + (endTime - startTime) + "ms");
		System.out.println("结果长度: " + result5.length());
		System.out.println("性能测试完成");
	}
	
}