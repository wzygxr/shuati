package class043;

import java.util.*;

/**
 * 回文数判断
 * 
 * 问题描述：
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 
 * 算法思路：
 * 1. 负数不是回文数
 * 2. 通过offset定位最高位
 * 3. 比较最高位和最低位是否相等
 * 4. 去掉最高位和最低位，继续比较
 * 5. 直到所有位都比较完毕
 * 
 * 时间复杂度分析：
 * O(log n) - 其中n是输入数字的值，需要遍历数字的一半位数
 * 
 * 空间复杂度分析：
 * O(1) - 只使用了常数级别的额外空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数等边界情况
 * 2. 性能优化：避免字符串转换，直接通过数学运算处理
 * 3. 鲁棒性：处理整数溢出问题
 * 
 * 相关题目：
 * 1. LeetCode 9. 回文数 - https://leetcode.cn/problems/palindrome-number/
 * 2. LeetCode 125. 验证回文串 - https://leetcode.cn/problems/valid-palindrome/
 * 3. LeetCode 680. 验证回文字符串 Ⅱ - https://leetcode.cn/problems/valid-palindrome-ii/
 * 4. LeetCode 409. 最长回文串 - https://leetcode.cn/problems/longest-palindrome/
 * 5. LeetCode 5. 最长回文子串 - https://leetcode.cn/problems/longest-palindromic-substring/
 * 6. LeetCode 234. 回文链表 - https://leetcode.cn/problems/palindrome-linked-list/
 * 7. LeetCode 266. 回文排列 - https://leetcode.cn/problems/palindrome-permutation/
 * 8. LeetCode 131. 分割回文串 - https://leetcode.cn/problems/palindrome-partitioning/
 * 9. LeetCode 336. 回文对 - https://leetcode.cn/problems/palindrome-pairs/
 * 10. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 11. LeetCode 479. 最大回文数乘积 - https://leetcode.cn/problems/largest-palindrome-product/
 * 12. 牛客网 - 回文数索引 - https://www.nowcoder.com/practice/bcd40976533d45298591611b64c57bb0
 * 13. 牛客网 - 回文数字判断 - https://www.nowcoder.com/practice/35b8166c135448c5a5ba2cff8d430c32
 * 14. 牛客网 - 最长回文子串 - https://www.nowcoder.com/practice/b4525d1d84934cf280439aeecc36f4af
 * 15. Codeforces 1438B - Valerii Against Everyone - https://codeforces.com/problemset/problem/1438/B
 * 16. Codeforces 110A - Nearly Lucky Number - https://codeforces.com/problemset/problem/110/A
 * 17. Codeforces 1332B -复合回文数 - https://codeforces.com/problemset/problem/1332/B
 * 18. AtCoder ABC162C - Sum of gcd of Tuples (Easy) - https://atcoder.jp/contests/abc162/tasks/abc162_c
 * 19. AtCoder ABC155A - Poor - https://atcoder.jp/contests/abc155/tasks/abc155_a
 * 20. AtCoder ABC159E - Dividing Chocolate - https://atcoder.jp/contests/abc159/tasks/abc159_e
 * 21. 洛谷 P1012 - 拼数 - https://www.luogu.com.cn/problem/P1012
 * 22. 洛谷 P1157 - 组合的输出 - https://www.luogu.com.cn/problem/P1157
 * 23. 洛谷 P1217 - [USACO1.5]回文质数 Prime Palindromes - https://www.luogu.com.cn/problem/P1217
 * 24. 洛谷 P1048 - 采药 - https://www.luogu.com.cn/problem/P1048
 * 25. HackerRank - The Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
 * 26. HackerRank - Making Anagrams - https://www.hackerrank.com/challenges/making-anagrams/problem
 * 27. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 28. HackerRank - The Longest Palindromic Subsequence - https://www.hackerrank.com/challenges/longest-palindromic-subsequence/problem
 * 29. UVa 10945 - Mother Bear - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1886
 * 30. UVa 10189 - Minesweeper - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=10189
 * 31. POJ 3974 - Palindrome - https://poj.org/problem?id=3974
 * 32. POJ 1163 - The Triangle - https://poj.org/problem?id=1163
 * 33. HDU 1321 - Word Amalgamation - https://acm.hdu.edu.cn/showproblem.php?pid=1321
 * 34. HDU 1203 - I NEED A OFFER! - https://acm.hdu.edu.cn/showproblem.php?pid=1203
 * 35. LintCode 1178. 有效的回文 - https://www.lintcode.com/problem/1178/
 * 36. LintCode 125. 背包问题 II - https://www.lintcode.com/problem/125/
 * 37. LintCode 200. 最长回文子串 - https://www.lintcode.com/problem/200/
 * 38. LintCode 130. 堆化 - https://www.lintcode.com/problem/130/
 * 39. LintCode 135. 数字组合 - https://www.lintcode.com/problem/135/
 * 40. AcWing 901. 滑雪 - https://www.acwing.com/problem/content/903/
 * 41. AcWing 1482. 进制 - https://www.acwing.com/problem/content/1484/
 * 42. LeetCode 334. 递增的三元子序列 - https://leetcode.cn/problems/increasing-triplet-subsequence/
 * 43. LeetCode 344. 反转字符串 - https://leetcode.cn/problems/reverse-string/
 * 44. LeetCode 459. 重复的子字符串 - https://leetcode.cn/problems/repeated-substring-pattern/
 * 45. LeetCode 680. 验证回文字符串 II - https://leetcode.cn/problems/valid-palindrome-ii/
 * 46. LeetCode 1281. 整数的各位积和之差 - https://leetcode.cn/problems/subtract-the-product-and-sum-of-digits-of-an-integer/
 * 47. 牛客网 - 最大数 - https://www.nowcoder.com/practice/fc897457408f4bbe9d3f87588f497729
 * 48. 牛客网 - 括号生成 - https://www.nowcoder.com/practice/c9addb265cdf4cdd92c092c655d164ca
 * 49. Codeforces 1481A - Space Navigation - https://codeforces.com/problemset/problem/1481/A
 * 50. Codeforces 1352B - Same Parity Summands - https://codeforces.com/problemset/problem/1352/B
 */
public class Code03_IsPalindrome {

	/**
	 * 判断一个整数是否是回文数
	 * 
	 * @param num 待判断的整数
	 * @return 如果是回文数返回true，否则返回false
	 * 
	 * 算法思路：
	 * 1. 负数不是回文数，直接返回false
	 * 2. 通过offset定位最高位数字
	 * 3. 比较最高位和最低位数字是否相等
	 * 4. 去掉最高位和最低位，继续比较
	 * 5. 直到所有位都比较完毕
	 * 
	 * 示例：
	 * isPalindrome(121) -> true (121从左到右和从右到左都是121)
	 * isPalindrome(-121) -> false (负数不是回文数)
	 * isPalindrome(10) -> false (从右到左是01，不是回文数)
	 * 
	 * 工程化考量：
	 * 1. 算法选择：不使用字符串转换，直接通过数学运算判断，避免额外的内存分配
	 * 2. 边界处理：特别处理0和负数
	 * 3. 性能优化：通过除以offset定位最高位，避免使用Math.pow可能导致的浮点数精度问题
	 */
	public static boolean isPalindrome(int num) {
		// 负数不是回文数
		if (num < 0) {
			return false;
		}
		int offset = 1;
		// 注意这么写是为了防止溢出
		// 通过不断乘以10来定位最高位的位置
		while (num / offset >= 10) {
			offset *= 10;
		}
		// 首尾判断
		// 比较最高位数字(num / offset)和最低位数字(num % 10)
		// 然后去掉最高位和最低位继续比较
		while (num != 0) {
			// 如果最高位和最低位不相等，则不是回文数
			if (num / offset != num % 10) {
				return false;
			}
			// 去掉最高位和最低位
			// (num % offset) 去掉最高位
			// / 10 去掉最低位
			num = (num % offset) / 10;
			// offset减少两位（因为去掉了两个数字）
			offset /= 100;
		}
		return true;
	}
    
    /**
     * 补充训练题目
     */
    
    /**
     * 补充题目1：LeetCode 131. 分割回文串
     * 题目描述：给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
     * 链接：https://leetcode.cn/problems/palindrome-partitioning/
     * 
     * 解题思路：
     * - 使用回溯算法，枚举所有可能的分割点
     * - 对于每个分割点，判断当前子串是否为回文串
     * - 如果是回文串，将其加入当前路径，并继续递归处理剩余部分
     * - 时间复杂度：O(n*2^n)，空间复杂度：O(n)
     */
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }
    
    private void backtrack(String s, int start, List<String> path, List<List<String>> result) {
        // 基本情况：已经处理完整个字符串
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 枚举所有可能的结束位置
        for (int i = start; i < s.length(); i++) {
            // 检查子串是否为回文串
            if (isPalindromeStrHelper(s, start, i)) {
                // 如果是回文串，加入路径
                path.add(s.substring(start, i + 1));
                // 递归处理剩余部分
                backtrack(s, i + 1, path, result);
                // 回溯，移除最后添加的子串
                path.remove(path.size() - 1);
            }
        }
    }
    
    private boolean isPalindromeStrHelper(String s, int left, int right) {
        // 双指针法判断子串是否为回文串
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 补充题目2：LeetCode 234. 回文链表
     * 题目描述：请判断一个链表是否为回文链表。
     * 链接：https://leetcode.cn/problems/palindrome-linked-list/
     * 
     * 解题思路：
     * - 找到链表的中点
     * - 反转链表的后半部分
     * - 比较前半部分和反转后的后半部分
     * - 恢复链表结构（可选）
     * - 时间复杂度：O(n)，空间复杂度：O(1)
     */
    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    public boolean isPalindrome(ListNode head) {
        // 处理边界情况
        if (head == null || head.next == null) {
            return true;
        }
        
        // 找到链表的中点
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 反转后半部分链表
        ListNode secondHalfStart = reverseList(slow.next);
        
        // 比较前半部分和反转后的后半部分
        ListNode p1 = head, p2 = secondHalfStart;
        boolean isPalindrome = true;
        
        while (p2 != null) {
            if (p1.val != p2.val) {
                isPalindrome = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        
        // 恢复链表结构（可选但好的做法）
        slow.next = reverseList(secondHalfStart);
        
        return isPalindrome;
    }
    
    private ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }
    
    /**
     * 补充题目3：LeetCode 5. 最长回文子串（辅助方法版本）
     * 题目描述：给你一个字符串 s，找到 s 中最长的回文子串。
     * 链接：https://leetcode.cn/problems/longest-palindromic-substring/
     * 
     * 解题思路：
     * - 使用中心扩展法，枚举每个可能的回文中心
     * - 考虑奇数长度和偶数长度两种情况
     * - 对于每个中心，向两边扩展，直到不能形成回文为止
     * - 时间复杂度：O(n^2)，空间复杂度：O(1)
     */
    public String findLongestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        
        int start = 0, end = 0;
        
        // 枚举每个可能的回文中心
        for (int i = 0; i < s.length(); i++) {
            // 奇数长度的回文串，中心是单个字符
            int len1 = expandAroundCenter(s, i, i);
            // 偶数长度的回文串，中心是两个字符之间
            int len2 = expandAroundCenter(s, i, i + 1);
            
            // 取两种情况中的最大长度
            int maxLen = Math.max(len1, len2);
            
            // 如果找到更长的回文串，更新起始和结束位置
            if (maxLen > end - start + 1) {
                start = i - (maxLen - 1) / 2;
                end = i + maxLen / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    private int expandAroundCenter(String s, int left, int right) {
        // 向两边扩展，直到不能形成回文为止
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // 返回回文串的长度
        return right - left - 1;
    }
    
    /**
     * 补充题目4：LeetCode 516. 最长回文子序列
     * 题目描述：给你一个字符串 s，找到其中最长的回文子序列，并返回该序列的长度。
     * 链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
     * 
     * 解题思路：
     * - 使用动态规划，dp[i][j]表示子串s[i...j]的最长回文子序列长度
     * - 状态转移方程：如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2；否则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
     * - 时间复杂度：O(n^2)，空间复杂度：O(n^2)
     */
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        // dp[i][j] 表示子串s[i...j]的最长回文子序列长度
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的最长回文子序列长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 从长度为2的子串开始，逐渐扩展到整个字符串
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                // 如果首尾字符相等，可以同时包含在回文子序列中
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // 否则取两种情况的最大值
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 整个字符串的最长回文子序列长度
        return dp[0][n - 1];
    }
    
    /**
     * 补充题目5：LeetCode 336. 回文对
     * 题目描述：给定一组唯一的单词，找出所有不同的索引对(i, j)，使得单词 words[i] + words[j] 形成回文串。
     * 链接：https://leetcode.cn/problems/palindrome-pairs/
     * 
     * 解题思路：
     * - 使用哈希表存储单词及其索引
     * - 对于每个单词，枚举所有可能的分割点，检查前缀或后缀是否可以与其他单词形成回文
     * - 利用回文性质进行优化
     * - 时间复杂度：O(n*k^2)，其中n是单词数量，k是单词的最大长度
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        if (words == null || words.length < 2) {
            return result;
        }
        
        // 构建哈希表，存储单词及其索引
        Map<String, Integer> wordMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordMap.put(words[i], i);
        }
        
        // 处理每个单词
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            
            // 情况1：空字符串与回文单词配对
            if (word.isEmpty()) {
                for (int j = 0; j < words.length; j++) {
                    if (i != j && isPalindromeStrHelper(words[j], 0, words[j].length() - 1)) {
                        result.add(Arrays.asList(i, j));
                        result.add(Arrays.asList(j, i));
                    }
                }
                continue;
            }
            
            // 情况2：尝试所有可能的分割
            for (int j = 0; j <= word.length(); j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);
                
                // 检查前缀的反转是否存在
                String reversedPrefix = new StringBuilder(prefix).reverse().toString();
                if (isPalindromeStrHelper(suffix, 0, suffix.length() - 1) && wordMap.containsKey(reversedPrefix) && wordMap.get(reversedPrefix) != i) {
                    result.add(Arrays.asList(i, wordMap.get(reversedPrefix)));
                }
                
                // 避免重复添加（当j=0时，reversedSuffix和word相同）
                if (j > 0) {
                    // 检查后缀的反转是否存在
                    String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                    if (isPalindromeStrHelper(prefix, 0, prefix.length() - 1) && wordMap.containsKey(reversedSuffix) && wordMap.get(reversedSuffix) != i) {
                        result.add(Arrays.asList(wordMap.get(reversedSuffix), i));
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * 补充题目6：LeetCode 680. 验证回文字符串 II
     * 题目描述：给定一个非空字符串 s，最多删除一个字符后，判断能否成为回文字符串。
     * 链接：https://leetcode.cn/problems/valid-palindrome-ii/
     * 
     * 解题思路：
     * - 使用双指针法判断是否为回文串
     * - 当遇到不匹配的字符时，尝试删除左指针指向的字符或右指针指向的字符
     * - 递归检查剩余部分是否为回文串
     * - 时间复杂度：O(n)，空间复杂度：O(n)（递归调用栈）
     */
    public boolean validPalindrome(String s) {
        return isPalindromeWithDelete(s, 0, s.length() - 1, false);
    }
    
    private boolean isPalindromeWithDelete(String s, int left, int right, boolean hasDeleted) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                // 如果已经删除过字符，则不能再删除
                if (hasDeleted) {
                    return false;
                }
                // 尝试删除左字符或右字符
                return isPalindromeWithDelete(s, left + 1, right, true) || 
                       isPalindromeWithDelete(s, left, right - 1, true);
            }
            left++;
            right--;
        }
        return true;
    }
    
    /**
     * 补充题目7：LeetCode 409. 最长回文串
     * 题目描述：给定一个包含大写字母和小写字母的字符串 s ，返回通过这些字母构造成的最长的回文串的长度。
     * 链接：https://leetcode.cn/problems/longest-palindrome/
     * 
     * 解题思路：
     * - 统计每个字符出现的次数
     * - 偶数次的字符可以全部用于构建回文串
     * - 奇数次的字符最多可以有一个字符保留所有出现次数（放在回文串中心），其他需要减1后取偶数部分
     * - 时间复杂度：O(n)，空间复杂度：O(1)（因为字符集大小固定）
     */
    public int longestPalindrome(String s) {
        int[] charCount = new int[128]; // 假设使用ASCII字符
        
        // 统计每个字符出现的次数
        for (char c : s.toCharArray()) {
            charCount[c]++;
        }
        
        int result = 0;
        boolean hasOdd = false;
        
        // 计算可以构成回文串的最大长度
        for (int count : charCount) {
            if (count % 2 == 0) {
                // 偶数次的字符全部使用
                result += count;
            } else {
                // 奇数次的字符，使用最大的偶数部分
                result += count - 1;
                hasOdd = true;
            }
        }
        
        // 如果有奇数次的字符，可以在回文串中心添加一个字符
        if (hasOdd) {
            result++;
        }
        
        return result;
    }
    
    /**
     * 补充题目8：LeetCode 266. 回文排列
     * 题目描述：给定一个字符串，判断该字符串中是否可以通过重新排列组合，形成一个回文字符串。
     * 链接：https://leetcode.cn/problems/palindrome-permutation/
     * 
     * 解题思路：
     * - 使用位掩码记录字符出现次数的奇偶性
     * - 遍历字符串，对于每个字符，翻转其对应的位
     * - 最终，如果位掩码为0（所有字符出现次数均为偶数）或只有一个位为1（有一个字符出现次数为奇数），则可以构成回文串
     * - 时间复杂度：O(n)，空间复杂度：O(1)
     */
    public boolean canPermutePalindrome(String s) {
        int bitMask = 0;
        
        for (char c : s.toCharArray()) {
            // 翻转对应字符的位
            bitMask ^= 1 << (c - 'a');
        }
        
        // 检查是否最多只有一个位为1
        return bitMask == 0 || (bitMask & (bitMask - 1)) == 0;
    }
    
    /**
     * 回文算法技术与应用深度剖析
     * 
     * 核心概念与理论基础：
     * 1. 回文数学理论：
     *    - 定义扩展：经典回文、镜像回文、数字回文等概念体系
     *    - 回文数分布：在自然数中的稀疏性与生成规律
     *    - 数学性质：回文数与素数、平方数的特殊关系
     *    - 回文猜想：关于回文数生成的数学猜想（如利克瑞尔数猜想）
     * 
     * 2. 计算模型：
     *    - 确定性有限自动机与回文识别
     *    - 上下文无关文法描述回文结构
     *    - 形式语言理论中的回文类
     *    - 回文等价类与商空间分析
     * 
     * 3. 复杂度理论视角：
     *    - 不同回文问题的计算复杂度分类
     *    - P与NP问题在回文领域的体现
     *    - 近似算法与启发式方法的边界
     *    - 参数化复杂度分析
     * 
     * 高级算法技术：
     * 1. Manacher算法深度解析：
     *    - 核心思想：利用已知回文信息避免重复计算
     *    - 算法流程：中心扩展与边界维护
     *    - 复杂度证明：O(n)时间复杂度的理论依据
     *    - 实现技巧：预处理与特殊字符插入
     * 
     * 2. 回文自动机（Palindromic Tree）：
     *    - 数据结构设计：节点定义与边结构
     *    - 构建算法：增量添加字符的过程
     *    - 应用场景：统计不同回文子串、最长回文子串等
     *    - 性能分析：时间与空间复杂度保证
     * 
     * 3. 高级哈希技术：
     *    - 前缀哈希与后缀哈希结合
     *    - 多项式滚动哈希的应用
     *    - 双向哈希验证策略
     *    - 哈希冲突的处理与防范
     * 
     * 4. 动态规划优化技术：
     *    - 状态压缩：从O(n²)到O(n)空间优化
     *    - 并行计算：对角线并行处理
     *    - 区间DP的高级应用
     *    - 决策单调性优化
     * 
     * 多维度优化策略：
     * 1. 算法层面优化：
     *    - 预计算与缓存：空间换时间的经典策略
     *    - 剪枝技术：基于启发式规则的搜索空间缩减
     *    - 问题转换：等价问题的求解思路转换
     *    - 近似算法：处理NP难问题的次优解
     * 
     * 2. 实现层面优化：
     *    - 位运算加速：字符频率统计与奇偶判断
     *    - 内存布局优化：数据局部性原理应用
     *    - 指令级并行：SIMD指令集的利用
     *    - 分支预测友好：避免条件分支的优化技巧
     * 
     * 3. 系统层面优化：
     *    - 多线程并行：工作窃取算法的应用
     *    - GPU加速：大规模并行计算
     *    - 分布式处理：MapReduce模式应用
     *    - 内存层次结构优化：缓存友好的数据结构
     * 
     * 工程化实践指南：
     * 1. 算法选择矩阵：
     *    - 问题特性与算法匹配表
     *    - 时间-空间权衡决策框架
     *    - 实现复杂度考量因素
     *    - 可维护性与性能平衡
     * 
     * 2. 测试与验证体系：
     *    - 全面测试用例设计方法
     *    - 边界条件覆盖策略
     *    - 性能基准测试框架
     *    - 随机测试与压力测试
     * 
     * 3. 代码质量保证：
     *    - 模块化设计模式
     *    - 接口抽象原则
     *    - 文档化实践
     *    - 单元测试覆盖策略
     * 
     * 跨学科应用：
     * 1. 生物信息学：
     *    - DNA序列中的回文结构识别
     *    - 基因调控区域分析
     *    - 蛋白质结构预测中的回文模式
     * 
     * 2. 密码学应用：
     *    - 基于回文的哈希函数
     *    - 对称加密算法中的应用
     *    - 消息完整性验证
     *    - 数字签名中的回文特性利用
     * 
     * 3. 自然语言处理：
     *    - 回文在诗歌与修辞中的应用
     *    - 语言模式识别
     *    - 文本生成中的回文构造
     *    - 语言演化研究
     * 
     * 4. 量子计算视角：
     *    - 量子算法中的回文处理
     *    - 量子并行性在回文搜索中的优势
     *    - 量子复杂度与经典算法的对比
     * 
     * 前沿研究方向：
     * 1. 理论研究：
     *    - 回文数在大数域中的分布性质
     *    - 回文猜想的计算验证
     *    - 参数化回文问题的复杂度
     *    - 随机字符串中的回文统计
     * 
     * 2. 算法创新：
     *    - 面向流数据的在线回文算法
     *    - 大规模分布式回文搜索
     *    - 量子回文算法
     *    - 近似算法的性能边界研究
     * 
     * 3. 应用拓展：
     *    - 区块链中的回文应用
     *    - AI模型中的回文模式学习
     *    - 量子通信中的回文编码
     *    - 分布式系统中的回文验证机制
     * 
     * 总结：
     * 回文算法作为计算机科学中的经典问题，其应用范围已远超传统的字符串处理领域。
     * 从数学理论到工程实践，从经典算法到前沿研究，回文问题体现了算法设计的深刻思想和广泛应用。
     * 掌握回文算法的设计与优化技巧，不仅有助于解决具体的算法问题，更能培养抽象思维和问题转化能力。
     * 在未来的计算机科学发展中，回文算法将继续在各个领域发挥重要作用，特别是在大数据处理、
     * 人工智能和量子计算等新兴领域，为解决复杂问题提供创新思路。
     */
    public static void main(String[] args) {
        // 测试回文数判断
        int[] testCases = {121, -121, 10, -101, 0, 1, 12321, 12345, 1001, 9999};
        
        System.out.println("回文数判断测试:");
        for (int num : testCases) {
            boolean result = isPalindrome(num);
            System.out.println("数字: " + num + " -> " + (result ? "是" : "否"));
        }
    }
}
