import java.util.*;

/**
 * LeetCode 131. 分割回文串
 * 
 * 题目描述：
 * 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
 * 
 * 示例：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 * 
 * 输入：s = "a"
 * 输出：[["a"]]
 * 
 * 提示：
 * 1 <= s.length <= 16
 * s 仅由小写英文字母组成
 * 
 * 链接：https://leetcode.cn/problems/palindrome-partitioning/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的分割方案
 * 2. 对于每个位置，判断从当前位置开始的子串是否为回文串
 * 3. 如果是回文串，则将其加入路径，并递归处理剩余部分
 * 4. 回溯时移除当前子串，尝试其他分割方式
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：只在子串是回文串时才继续递归
 * 2. 提前终止：当剩余字符串无法形成有效分割时提前终止
 * 3. 预处理优化：预先计算所有子串是否为回文串
 * 
 * 时间复杂度：O(N * 2^N)，其中N是字符串长度。在最坏情况下，每个字符都可以单独作为回文串，共有O(2^N)种分割方案，每种方案需要O(N)时间检查回文。
 * 空间复杂度：O(N^2)，递归栈的深度加上存储当前路径的空间和预处理的回文矩阵。
 * 
 * 工程化考量：
 * 1. 边界处理：处理空字符串和单字符字符串的特殊情况
 * 2. 性能优化：使用动态规划预处理回文串信息
 * 3. 内存管理：合理使用数据结构减少内存占用
 * 4. 可读性：添加详细注释和变量命名
 * 5. 异常处理：验证输入参数的有效性
 * 6. 模块化设计：将核心逻辑封装成独立方法
 * 7. 可维护性：添加详细注释和文档说明
 */
public class Code14_PalindromePartitioning {

    /**
     * 分割回文串
     * 
     * @param s 输入字符串
     * @return 所有可能的分割方案
     */
    public static List<List<String>> partition(String s) {
        // 边界条件检查
        if (s == null || s.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<List<String>> result = new ArrayList<>();
        List<String> path = new ArrayList<>();
        // 回溯生成所有分割方案
        backtrack(s, 0, path, result);
        return result;
    }

    /**
     * 回溯函数生成分割方案
     * 
     * @param s 输入字符串
     * @param start 当前处理的起始位置
     * @param path 当前分割路径
     * @param result 结果列表
     */
    private static void backtrack(String s, int start, List<String> path, List<List<String>> result) {
        // 终止条件：已处理到字符串末尾
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 从start开始尝试不同长度的子串
        for (int end = start + 1; end <= s.length(); end++) {
            // 可行性剪枝：判断子串s[start...end-1]是否为回文串
            if (isPalindrome(s, start, end - 1)) {
                // 将回文子串加入路径
                path.add(s.substring(start, end));
                // 递归处理剩余部分
                backtrack(s, end, path, result);
                // 回溯：移除当前子串
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * 判断字符串的子串是否为回文串
     * 
     * @param s 原始字符串
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 是否为回文串
     */
    private static boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 优化版本：使用动态规划预处理回文串信息
     * 
     * @param s 输入字符串
     * @return 所有可能的分割方案
     */
    public static List<List<String>> partitionOptimized(String s) {
        // 边界条件检查
        if (s == null || s.isEmpty()) {
            return new ArrayList<>();
        }
        
        int n = s.length();
        // 预处理：计算所有子串是否为回文串
        boolean[][] isPalindrome = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            isPalindrome[i][i] = true;  // 单个字符都是回文串
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    if (len == 2 || isPalindrome[i + 1][j - 1]) {
                        isPalindrome[i][j] = true;
                    }
                }
            }
        }
        
        List<List<String>> result = new ArrayList<>();
        List<String> path = new ArrayList<>();
        // 回溯生成所有分割方案
        backtrackOptimized(s, 0, path, result, isPalindrome);
        return result;
    }

    /**
     * 优化版本的回溯函数
     * 
     * @param s 输入字符串
     * @param start 当前处理的起始位置
     * @param path 当前分割路径
     * @param result 结果列表
     * @param isPalindrome 预处理的回文串信息
     */
    private static void backtrackOptimized(String s, int start, List<String> path, List<List<String>> result, boolean[][] isPalindrome) {
        // 终止条件：已处理到字符串末尾
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 从start开始尝试不同长度的子串
        for (int end = start + 1; end <= s.length(); end++) {
            // 可行性剪枝：使用预处理的回文串信息
            if (isPalindrome[start][end - 1]) {
                // 将回文子串加入路径
                path.add(s.substring(start, end));
                // 递归处理剩余部分
                backtrackOptimized(s, end, path, result, isPalindrome);
                // 回溯：移除当前子串
                path.remove(path.size() - 1);
            }
        }
    }

    /**
     * 验证分割方案是否正确
     * 
     * @param s 原始字符串
     * @param partition 分割方案
     * @return 是否正确
     */
    public static boolean isValidPartition(String s, List<String> partition) {
        if (partition == null) return false;
        
        // 检查拼接后是否等于原字符串
        StringBuilder sb = new StringBuilder();
        for (String part : partition) {
            if (part == null || part.isEmpty()) return false;
            sb.append(part);
        }
        if (!sb.toString().equals(s)) return false;
        
        // 检查每个部分是否为回文串
        for (String part : partition) {
            if (!isPalindrome(part, 0, part.length() - 1)) return false;
        }
        
        return true;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        String test1 = "aab";
        List<List<String>> result1 = partition(test1);
        System.out.println("=== 测试用例1 ===");
        System.out.println("输入: \"" + test1 + "\"");
        System.out.println("输出: " + result1);
        for (List<String> partition : result1) {
            System.out.println("  方案正确性: " + isValidPartition(test1, partition));
        }
        
        // 测试用例2
        String test2 = "a";
        List<List<String>> result2 = partition(test2);
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("输入: \"" + test2 + "\"");
        System.out.println("输出: " + result2);
        for (List<String> partition : result2) {
            System.out.println("  方案正确性: " + isValidPartition(test2, partition));
        }
        
        // 测试用例3
        String test3 = "aabb";
        List<List<String>> result3 = partition(test3);
        System.out.println("\n=== 测试用例3 ===");
        System.out.println("输入: \"" + test3 + "\"");
        System.out.println("输出: " + result3);
        for (List<String> partition : result3) {
            System.out.println("  方案正确性: " + isValidPartition(test3, partition));
        }
        
        // 性能对比测试
        String test4 = "abccba";
        long startTime = System.currentTimeMillis();
        List<List<String>> result4a = partition(test4);
        long endTime = System.currentTimeMillis();
        System.out.println("\n=== 性能对比测试 ===");
        System.out.println("输入: \"" + test4 + "\"");
        System.out.println("基础版本解的数量: " + result4a.size());
        System.out.println("基础版本耗时: " + (endTime - startTime) + " ms");
        
        startTime = System.currentTimeMillis();
        List<List<String>> result4b = partitionOptimized(test4);
        endTime = System.currentTimeMillis();
        System.out.println("优化版本解的数量: " + result4b.size());
        System.out.println("优化版本耗时: " + (endTime - startTime) + " ms");
    }
}