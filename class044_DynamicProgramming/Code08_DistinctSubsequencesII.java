package class066;

// 不同的子序列 II
// 给定一个字符串 s，计算 s 的 不同非空子序列 的个数
// 因为结果可能很大，答案对 1000000007 取模
// 字符串的 子序列 是经由原字符串删除一些（也可能不删除）
// 字符但不改变剩余字符相对位置的一个新字符串
// 例如，"ace" 是 "abcde" 的一个子序列，但 "aec" 不是
// 测试链接 : https://leetcode.cn/problems/distinct-subsequences-ii/
public class Code08_DistinctSubsequencesII {

	// 时间复杂度O(n)，n是字符串s的长度
    // 空间复杂度O(1)，cnt数组大小固定为26，存储以每个字符结尾的子序列数量
    // 核心思想：动态规划，对于每个字符，计算以该字符结尾的新子序列数量
    // 通过记录每个字符上次出现时的子序列数量来避免重复计算
	public static int distinctSubseqII(String s) {
		int mod = 1000000007;
		char[] str = s.toCharArray();
		int[] cnt = new int[26];
		int all = 1, newAdd;
		for (char x : str) {
			newAdd = (all - cnt[x - 'a'] + mod) % mod;
			cnt[x - 'a'] = (cnt[x - 'a'] + newAdd) % mod;
			all = (all + newAdd) % mod;
		}
		return (all - 1 + mod) % mod;
	}
    
    // 测试用例
    public static void main(String[] args) {
        System.out.println("测试不同的子序列II问题：");
        
        // 测试用例1
        String s1 = "abc";
        System.out.println("s = \"" + s1 + "\"");
        System.out.println("不同子序列数量: " + distinctSubseqII(s1));
        
        // 测试用例2
        String s2 = "aba";
        System.out.println("s = \"" + s2 + "\"");
        System.out.println("不同子序列数量: " + distinctSubseqII(s2));
        
        // 测试用例3
        String s3 = "aaa";
        System.out.println("s = \"" + s3 + "\"");
        System.out.println("不同子序列数量: " + distinctSubseqII(s3));
    }

}