package class085;

/**
 * LeetCode 1397. 找到所有好字符串
 * 
 * 题目描述：
 * 给你两个长度为 n 的字符串 s1 和 s2 ，以及一个字符串 evil 。
 * 请你返回好字符串的数目。
 * 好字符串的定义为：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，
 * 且不包含 evil 为子字符串。
 * 
 * 解题思路：
 * 使用数位DP结合KMP算法解决该问题。
 * 我们需要在构造字符串的过程中跟踪与evil字符串的匹配进度。
 * 状态定义：
 * dp[pos][matchPos][limitLow][limitHigh] 表示处理到第pos位，
 * 当前与evil匹配到matchPos位置，limitLow和limitHigh表示是否受到上下界限制
 * 
 * 时间复杂度：O(n * m * 2 * 2 * 26) = O(n * m)
 * 空间复杂度：O(n * m)
 * 其中n是字符串长度，m是evil字符串长度
 */
public class LeetCode1397_FindAllGoodStrings {
    
    private static final int MOD = 1000000007;
    
    // 数位DP记忆化数组
    private static int[][][][] dp;
    
    // 存储上下界字符串
    private static String low, high, evil;
    
    // 字符串长度
    private static int n, m;
    
    // KMP的next数组
    private static int[] next;
    
    /**
     * 主函数：计算好字符串的数目
     * @param n 字符串长度
     * @param s1 下界字符串
     * @param s2 上界字符串
     * @param evil 禁止包含的子字符串
     * @return 好字符串的数目
     */
    public static int findGoodStrings(int n, String s1, String s2, String evil) {
        low = s1;
        high = s2;
        LeetCode1397_FindAllGoodStrings.evil = evil;
        LeetCode1397_FindAllGoodStrings.n = n;
        m = evil.length();
        
        // 构建KMP的next数组
        buildNext();
        
        // 初始化DP数组
        dp = new int[n][m][2][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k][0] = -1;
                    dp[i][j][k][1] = -1;
                }
            }
        }
        
        // 从第0位开始进行数位DP
        return dfs(0, 0, true, true);
    }
    
    /**
     * 构建KMP的next数组
     */
    private static void buildNext() {
        next = new int[m];
        for (int i = 1, j = 0; i < m; i++) {
            while (j > 0 && evil.charAt(i) != evil.charAt(j)) {
                j = next[j - 1];
            }
            if (evil.charAt(i) == evil.charAt(j)) {
                j++;
            }
            next[i] = j;
        }
    }
    
    /**
     * 使用KMP算法计算新的匹配位置
     * @param pos 当前匹配位置
     * @param ch 当前字符
     * @return 新的匹配位置
     */
    private static int getNextPos(int pos, char ch) {
        while (pos > 0 && ch != evil.charAt(pos)) {
            pos = next[pos - 1];
        }
        if (ch == evil.charAt(pos)) {
            pos++;
        }
        return pos;
    }
    
    /**
     * 数位DP核心函数
     * @param pos 当前处理到第几位
     * @param matchPos 当前与evil匹配到的位置
     * @param limitLow 是否受到下界限制
     * @param limitHigh 是否受到上界限制
     * @return 满足条件的字符串数目
     */
    private static int dfs(int pos, int matchPos, boolean limitLow, boolean limitHigh) {
        // 如果已经匹配到evil的全部字符，说明包含evil，不合法
        if (matchPos == m) {
            return 0;
        }
        
        // 递归终止条件：处理完所有位置
        if (pos == n) {
            return 1;
        }
        
        // 记忆化搜索优化：如果该状态已经计算过，直接返回结果
        if (!limitLow && !limitHigh && dp[pos][matchPos][limitLow ? 1 : 0][limitHigh ? 1 : 0] != -1) {
            return dp[pos][matchPos][limitLow ? 1 : 0][limitHigh ? 1 : 0];
        }
        
        // 确定当前位可以填入的字符范围
        char lo = limitLow ? low.charAt(pos) : 'a';
        char hi = limitHigh ? high.charAt(pos) : 'z';
        
        int result = 0;
        
        // 枚举当前位可以填入的字符
        for (char c = lo; c <= hi; c++) {
            // 使用KMP计算新的匹配位置
            int newMatchPos = getNextPos(matchPos, c);
            
            // 递归处理下一位
            result = (result + dfs(pos + 1, newMatchPos, limitLow && c == lo, limitHigh && c == hi)) % MOD;
        }
        
        // 记忆化存储结果
        if (!limitLow && !limitHigh) {
            dp[pos][matchPos][limitLow ? 1 : 0][limitHigh ? 1 : 0] = result;
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 2;
        String s1_1 = "aa", s2_1 = "da", evil1 = "b";
        System.out.println("n = " + n1 + ", s1 = " + s1_1 + ", s2 = " + s2_1 + ", evil = " + evil1 + 
                          ", 结果 = " + findGoodStrings(n1, s1_1, s2_1, evil1)); // 期望输出: 51
        
        // 测试用例2
        int n2 = 8;
        String s1_2 = "leetcode", s2_2 = "leetgoes", evil2 = "leet";
        System.out.println("n = " + n2 + ", s1 = " + s1_2 + ", s2 = " + s2_2 + ", evil = " + evil2 + 
                          ", 结果 = " + findGoodStrings(n2, s1_2, s2_2, evil2)); // 期望输出: 0
        
        // 测试用例3
        int n3 = 2;
        String s1_3 = "gx", s2_3 = "gz", evil3 = "x";
        System.out.println("n = " + n3 + ", s1 = " + s1_3 + ", s2 = " + s2_3 + ", evil = " + evil3 + 
                          ", 结果 = " + findGoodStrings(n3, s1_3, s2_3, evil3)); // 期望输出: 2
    }
}