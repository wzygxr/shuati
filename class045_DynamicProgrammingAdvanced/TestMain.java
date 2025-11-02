package class067;

public class TestMain {
    public static void main(String[] args) {
        // 测试Code01_MinimumPathSum
        int[][] grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("Code01_MinimumPathSum测试:");
        System.out.println("方法3结果: " + Code01_MinimumPathSum.minPathSum3(grid1));
        System.out.println("方法4结果: " + Code01_MinimumPathSum.minPathSum4(grid1));
        
        // 测试Code03_LongestCommonSubsequence
        String str1 = "abcde";
        String str2 = "ace";
        System.out.println("\nCode03_LongestCommonSubsequence测试:");
        System.out.println("方法4结果: " + Code03_LongestCommonSubsequence.longestCommonSubsequence4(str1, str2));
        System.out.println("方法5结果: " + Code03_LongestCommonSubsequence.longestCommonSubsequence5(str1, str2));
        
        // 测试Code04_LongestPalindromicSubsequence
        String str3 = "bbbab";
        System.out.println("\nCode04_LongestPalindromicSubsequence测试:");
        System.out.println("方法3结果: " + Code04_LongestPalindromicSubsequence.longestPalindromeSubseq3(str3));
        System.out.println("方法4结果: " + Code04_LongestPalindromicSubsequence.longestPalindromeSubseq4(str3));
    }
}