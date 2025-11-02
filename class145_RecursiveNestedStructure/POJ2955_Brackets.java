package class039;

// POJ 2955 Brackets (最长括号匹配子序列)
// 测试链接 : http://poj.org/problem?id=2955

public class POJ2955_Brackets {
    public int longestValidParentheses(String s) {
        int n = s.length();
        if (n == 0) return 0;
        
        // dp[i][j] 表示区间[i,j]内最长的有效括号长度
        int[][] dp = new int[n][n];
        
        // 填充dp表
        for (int len = 2; len <= n; len++) { // 区间长度从2开始
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 如果首尾字符匹配
                if ((s.charAt(i) == '(' && s.charAt(j) == ')') ||
                    (s.charAt(i) == '[' && s.charAt(j) == ']')) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                }
                
                // 尝试分割区间
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 测试用例
    public static void main(String[] args) {
        POJ2955_Brackets solution = new POJ2955_Brackets();
        
        // 测试用例1
        String s1 = "((()))";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.longestValidParentheses(s1));
        System.out.println("期望: 6\n");
        
        // 测试用例2
        String s2 = "()()()";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.longestValidParentheses(s2));
        System.out.println("期望: 6\n");
    }
}