package class186;

/**
 * Manacher算法实现
 * 用于在O(n)时间复杂度内查找字符串中的最长回文子串
 * 核心思想：利用已知回文子串的信息，避免重复计算
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
public class ManacherAlgorithm {
    
    /**
     * 预处理字符串，在每个字符之间插入特殊字符（如'#'）
     * 这样可以统一处理奇数长度和偶数长度的回文子串
     * 
     * @param s 原始字符串
     * @return 预处理后的字符串
     */
    private static String preprocess(String s) {
        if (s == null || s.isEmpty()) {
            return "^$";
        }
        
        StringBuilder result = new StringBuilder("^");
        for (int i = 0; i < s.length(); i++) {
            result.append('#').append(s.charAt(i));
        }
        result.append("#$");
        return result.toString();
    }
    
    /**
     * 使用Manacher算法查找最长回文子串
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String findLongestPalindromicSubstring(String s) {
        if (s == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        if (s.length() <= 1) {
            return s; // 空字符串或单字符字符串的最长回文子串就是自身
        }
        
        // 预处理字符串
        String T = preprocess(s);
        int n = T.length();
        
        // P[i]表示以T[i]为中心的最长回文子串的半径（不包括中心）
        int[] P = new int[n];
        
        // C是当前回文子串的中心，R是当前回文子串的右边界
        int C = 0, R = 0;
        
        // 最大回文子串的中心索引和半径
        int maxLen = 0, centerIndex = 0;
        
        // 遍历预处理后的字符串，跳过^和$
        for (int i = 1; i < n - 1; i++) {
            // 计算i关于C的对称点
            int iMirror = 2 * C - i; // C - (i - C)
            
            // 利用回文的对称性初始化P[i]
            // 如果i在R的范围内，可以利用对称点的信息
            // 否则初始化为0
            P[i] = (R > i) ? Math.min(R - i, P[iMirror]) : 0;
            
            // 尝试扩展回文子串
            // 注意这里是直接比较字符，而不是像暴力方法那样每次都检查边界
            while (T.charAt(i + 1 + P[i]) == T.charAt(i - 1 - P[i])) {
                P[i]++;
            }
            
            // 如果扩展后的回文子串的右边界超过R，则更新C和R
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // 更新最长回文子串的信息
            if (P[i] > maxLen) {
                maxLen = P[i];
                centerIndex = i;
            }
        }
        
        // 计算原始字符串中最长回文子串的起始和结束位置
        // 注意预处理字符串中的索引转换
        int start = (centerIndex - maxLen) / 2; // 转换为原始字符串的索引
        return s.substring(start, start + maxLen);
    }
    
    /**
     * 计算字符串中回文子串的数量（包括单个字符）
     * 
     * @param s 输入字符串
     * @return 回文子串的数量
     */
    public static int countPalindromicSubstrings(String s) {
        if (s == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        if (s.isEmpty()) {
            return 0;
        }
        
        // 预处理字符串
        String T = preprocess(s);
        int n = T.length();
        int[] P = new int[n];
        int C = 0, R = 0;
        int count = 0;
        
        for (int i = 1; i < n - 1; i++) {
            int iMirror = 2 * C - i;
            P[i] = (R > i) ? Math.min(R - i, P[iMirror]) : 0;
            
            while (T.charAt(i + 1 + P[i]) == T.charAt(i - 1 - P[i])) {
                P[i]++;
            }
            
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // P[i]表示半径，每个半径对应一个回文子串
            // 注意：这里需要除以2因为预处理字符串中的'#'不代表实际字符
            count += (P[i] + 1) / 2;
        }
        
        return count;
    }
    
    /**
     * 查找所有不同的回文子串
     * 
     * @param s 输入字符串
     * @return 包含所有不同回文子串的集合
     */
    public static java.util.Set<String> findAllDistinctPalindromicSubstrings(String s) {
        if (s == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
        
        java.util.Set<String> result = new java.util.HashSet<>();
        if (s.isEmpty()) {
            return result;
        }
        
        // 预处理字符串
        String T = preprocess(s);
        int n = T.length();
        int[] P = new int[n];
        int C = 0, R = 0;
        
        for (int i = 1; i < n - 1; i++) {
            int iMirror = 2 * C - i;
            P[i] = (R > i) ? Math.min(R - i, P[iMirror]) : 0;
            
            while (T.charAt(i + 1 + P[i]) == T.charAt(i - 1 - P[i])) {
                P[i]++;
            }
            
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // 提取所有以i为中心的回文子串
            // 从1开始（半径至少为1）到P[i]
            for (int r = 1; r <= P[i]; r++) {
                int start = (i - r) / 2;
                int end = start + r;
                String palindrome = s.substring(start, end);
                result.add(palindrome);
            }
        }
        
        return result;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        String text1 = "babad";
        System.out.println("=== 测试用例1 ===");
        System.out.println("文本: " + text1);
        System.out.println("最长回文子串: " + findLongestPalindromicSubstring(text1)); // "bab" 或 "aba"
        System.out.println("回文子串数量: " + countPalindromicSubstrings(text1)); // 7
        System.out.println("不同回文子串: " + findAllDistinctPalindromicSubstrings(text1));
        
        // 测试用例2：边界情况
        String text2 = "cbbd";
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("文本: " + text2);
        System.out.println("最长回文子串: " + findLongestPalindromicSubstring(text2)); // "bb"
        
        // 测试用例3：单个字符
        String text3 = "a";
        System.out.println("\n=== 测试用例3 ===");
        System.out.println("文本: " + text3);
        System.out.println("最长回文子串: " + findLongestPalindromicSubstring(text3)); // "a"
        
        // 测试用例4：重复字符
        String text4 = "aaa";
        System.out.println("\n=== 测试用例4 ===");
        System.out.println("文本: " + text4);
        System.out.println("最长回文子串: " + findLongestPalindromicSubstring(text4)); // "aaa"
        System.out.println("回文子串数量: " + countPalindromicSubstrings(text4)); // 6
        
        // 测试用例5：较长文本
        String text5 = "mississippi";
        System.out.println("\n=== 测试用例5 ===");
        System.out.println("文本: " + text5);
        System.out.println("最长回文子串: " + findLongestPalindromicSubstring(text5)); // "ississi"
    }
}