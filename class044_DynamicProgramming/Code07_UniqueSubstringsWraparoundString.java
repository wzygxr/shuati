package class066;

// 环绕字符串中唯一的子字符串
// 定义字符串 base 为一个 "abcdefghijklmnopqrstuvwxyz" 无限环绕的字符串
// 所以 base 看起来是这样的：
// "..zabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcd.."
// 给你一个字符串 s ，请你统计并返回 s 中有多少 不同非空子串 也在 base 中出现
// 测试链接 : https://leetcode.cn/problems/unique-substrings-in-wraparound-string/
public class Code07_UniqueSubstringsWraparoundString {

	// 时间复杂度O(n)，n是字符串s的长度，字符串base长度为正无穷
    // 空间复杂度O(1)，dp数组大小固定为26，存储每个字符结尾的最长子串长度
    // 核心思想：对于每个字符，我们只关心以该字符结尾的最长连续子串长度
    // 因为如果以字符c结尾的最长连续子串长度为k，那么以c结尾的所有子串数量就是k
	public static int findSubstringInWraproundString(String str) {
		int n = str.length();
		int[] s = new int[n];
		// abcde...z -> 0, 1, 2, 3, 4....25
		for (int i = 0; i < n; i++) {
			s[i] = str.charAt(i) - 'a';
		}
		// dp[0] : s中必须以'a'的子串，最大延伸长度是多少，延伸一定要跟据base串的规则
		int[] dp = new int[26];
		// s : c d e....
		//     2 3 4
		dp[s[0]] = 1;
		for (int i = 1, cur, pre, len = 1; i < n; i++) {
			cur = s[i];
			pre = s[i - 1];
			// pre cur
			if ((pre == 25 && cur == 0) || pre + 1 == cur) {
				// (前一个字符是'z' && 当前字符是'a') || 前一个字符比当前字符的ascii码少1
				len++;
			} else {
				len = 1;
			}
			dp[cur] = Math.max(dp[cur], len);
		}
		int ans = 0;
		for (int i = 0; i < 26; i++) {
			ans += dp[i];
		}
		return ans;
	}
    
    // 测试用例
    public static void main(String[] args) {
        System.out.println("测试环绕字符串中唯一的子字符串问题：");
        
        // 测试用例1
        String s1 = "a";
        System.out.println("s = \"" + s1 + "\"");
        System.out.println("不同子串数量: " + findSubstringInWraproundString(s1));
        
        // 测试用例2
        String s2 = "cac";
        System.out.println("s = \"" + s2 + "\"");
        System.out.println("不同子串数量: " + findSubstringInWraproundString(s2));
        
        // 测试用例3
        String s3 = "zab";
        System.out.println("s = \"" + s3 + "\"");
        System.out.println("不同子串数量: " + findSubstringInWraproundString(s3));
    }

}