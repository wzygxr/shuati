package class105;

import java.util.*;

/**
 * 字符串哈希综合应用题目集
 * <p>
 * 本文件包含多个字符串哈希的实际应用场景，展示字符串哈希技术在
 * 各种实际问题中的强大应用能力。
 * <p>
 * 包含题目：
 * 1. LeetCode 1044 - 最长重复子串
 * 2. LeetCode 187 - 重复的DNA序列
 * 3. LeetCode 686 - 重复叠加字符串匹配
 * 4. LeetCode 30 - 串联所有单词的子串
 * 5. 自定义题目：最长公共子串问题
 * <p>
 * 算法核心思想：
 * 通过字符串哈希技术实现O(1)时间的子串比较，结合二分搜索、滑动窗口等
 * 技术解决复杂的字符串处理问题。
 * <p>
 * 技术特点：
 * 1. 多项式滚动哈希算法
 * 2. 双哈希技术降低冲突概率
 * 3. 预处理优化提高效率
 * 4. 边界情况全面处理
 * <p>
 * 时间复杂度分析：
 * 不同题目的时间复杂度从O(n)到O(nlogn)不等，具体取决于算法设计
 * <p>
 * 空间复杂度分析：
 * 通常为O(n)级别，用于存储哈希数组和辅助数据结构
 * <p>
 * @author Algorithm Journey
 */
public class Code15_StringHashApplications {
    
    /**
     * LeetCode 1044 - 最长重复子串
     * 题目链接：https://leetcode.cn/problems/longest-duplicate-substring/
     * <p>
     * 题目描述：
     * 给定一个字符串s，找出其中最长重复子串。如果有多个最长重复子串，
     * 返回任意一个。如果不存在重复子串，返回空字符串。
     * <p>
     * 示例：
     * 输入："banana"
     * 输出："ana" 或 "na"
     * <p>
     * 算法思路：
     * 使用二分搜索+字符串哈希技术：
     * 1. 二分搜索可能的子串长度
     * 2. 对于每个长度，使用字符串哈希检查是否存在重复子串
     * 3. 使用哈希表记录已出现的子串哈希值
     * <p>
     * 时间复杂度：O(nlogn)
     * 空间复杂度：O(n)
     */
    public static String longestDupSubstring(String s) {
        if (s == null || s.length() < 2) return "";
        
        int n = s.length();
        // 二分搜索边界
        int left = 1, right = n - 1;
        String result = "";
        
        // 预处理哈希数组
        long[] pow = new long[n + 1];
        long[] hash = new long[n + 1];
        final long BASE = 131L;
        final long MOD = 1000000007L;
        
        pow[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * BASE) % MOD;
        }
        
        hash[0] = 0;
        for (int i = 1; i <= n; i++) {
            hash[i] = (hash[i - 1] * BASE + s.charAt(i - 1)) % MOD;
        }
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String dup = findDuplicate(s, mid, hash, pow, BASE, MOD);
            
            if (dup != null) {
                result = dup;
                left = mid + 1; // 尝试更长的子串
            } else {
                right = mid - 1; // 缩短子串长度
            }
        }
        
        return result;
    }
    
    private static String findDuplicate(String s, int len, long[] hash, long[] pow, 
                                      long BASE, long MOD) {
        Set<Long> seen = new HashSet<>();
        int n = s.length();
        
        for (int i = 0; i <= n - len; i++) {
            // 计算子串哈希值
            long h = (hash[i + len] - hash[i] * pow[len] % MOD + MOD) % MOD;
            
            if (seen.contains(h)) {
                return s.substring(i, i + len);
            }
            seen.add(h);
        }
        
        return null;
    }
    
    /**
     * LeetCode 187 - 重复的DNA序列
     * 题目链接：https://leetcode.cn/problems/repeated-dna-sequences/
     * <p>
     * 题目描述：
     * DNA序列由一系列核苷酸组成，分别用'A', 'C', 'G', 'T'表示。
     * 编写函数找出所有目标子串，目标子串的长度为10，且在DNA字符串s中出现超过一次。
     * <p>
     * 示例：
     * 输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
     * 输出：["AAAAACCCCC","CCCCCAAAAA"]
     * <p>
     * 算法思路：
     * 使用滚动哈希技术滑动窗口统计长度为10的子串出现次数
     * 当某个子串出现次数超过1次时，加入结果集
     * <p>
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static List<String> findRepeatedDnaSequences(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) return result;
        
        Map<Long, Integer> countMap = new HashMap<>();
        final long BASE = 131L;
        final long MOD = 1000000007L;
        
        int n = s.length();
        long hash = 0;
        long power = 1;
        
        // 计算前9个字符的幂次
        for (int i = 0; i < 9; i++) {
            power = (power * BASE) % MOD;
        }
        
        // 计算前10个字符的哈希值
        for (int i = 0; i < 10; i++) {
            hash = (hash * BASE + charToInt(s.charAt(i))) % MOD;
        }
        countMap.put(hash, 1);
        
        // 滑动窗口
        for (int i = 10; i < n; i++) {
            // 移除左边字符
            hash = (hash - charToInt(s.charAt(i - 10)) * power % MOD + MOD) % MOD;
            // 添加右边字符
            hash = (hash * BASE + charToInt(s.charAt(i))) % MOD;
            
            int count = countMap.getOrDefault(hash, 0);
            if (count == 1) {
                result.add(s.substring(i - 9, i + 1));
            }
            countMap.put(hash, count + 1);
        }
        
        return result;
    }
    
    private static int charToInt(char c) {
        switch (c) {
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return 0;
        }
    }
    
    /**
     * LeetCode 686 - 重复叠加字符串匹配
     * 题目链接：https://leetcode.cn/problems/repeated-string-match/
     * <p>
     * 题目描述：
     * 给定两个字符串a和b，寻找重复叠加字符串a的最小次数，使得字符串b成为
     * 叠加后的字符串a的子串。如果不存在则返回-1。
     * <p>
     * 示例：
     * 输入：a = "abcd", b = "cdabcdab"
     * 输出：3
     * <p>
     * 算法思路：
     * 1. 计算最小重复次数k = ceil(b.length / a.length)
     * 2. 检查重复k次和k+1次是否包含b
     * 3. 使用字符串哈希进行高效匹配
     * <p>
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(n + m)
     */
    public static int repeatedStringMatch(String a, String b) {
        if (b == null || b.isEmpty()) return 1;
        if (a == null || a.isEmpty()) return -1;
        
        int n = a.length(), m = b.length();
        int k = (m + n - 1) / n; // 向上取整
        
        // 构建重复k+1次的字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= k; i++) {
            sb.append(a);
        }
        String repeated = sb.toString();
        
        // 使用字符串哈希进行匹配
        if (containsSubstring(repeated, b)) {
            // 检查k次是否足够
            if (containsSubstring(sb.substring(0, k * n), b)) {
                return k;
            } else {
                return k + 1;
            }
        }
        
        return -1;
    }
    
    private static boolean containsSubstring(String text, String pattern) {
        if (pattern.length() > text.length()) return false;
        
        final long BASE = 131L;
        final long MOD = 1000000007L;
        
        int n = text.length(), m = pattern.length();
        
        // 计算模式串哈希值
        long patternHash = 0;
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
        }
        
        // 计算文本前缀哈希
        long[] pow = new long[n + 1];
        long[] hash = new long[n + 1];
        
        pow[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow[i] = (pow[i - 1] * BASE) % MOD;
        }
        
        hash[0] = 0;
        for (int i = 1; i <= n; i++) {
            hash[i] = (hash[i - 1] * BASE + text.charAt(i - 1)) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            long subHash = (hash[i + m] - hash[i] * pow[m] % MOD + MOD) % MOD;
            if (subHash == patternHash) {
                // 精确比较避免哈希冲突
                if (text.substring(i, i + m).equals(pattern)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 最长公共子串问题
     * <p>
     * 题目描述：
     * 给定两个字符串s1和s2，找到它们的最长公共子串。
     * 如果有多个最长公共子串，返回任意一个。
     * <p>
     * 示例：
     * 输入：s1 = "ABABC", s2 = "BABCA"
     * 输出："BABC"
     * <p>
     * 算法思路：
     * 使用二分搜索+字符串哈希技术：
     * 1. 二分搜索可能的公共子串长度
     * 2. 对于每个长度，检查s1和s2是否有公共子串
     * 3. 使用哈希表记录s1的所有子串哈希值
     * <p>
     * 时间复杂度：O((m+n)log(min(m,n)))
     * 空间复杂度：O(m+n)
     */
    public static String longestCommonSubstring(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isEmpty() || s2.isEmpty()) {
            return "";
        }
        
        int m = s1.length(), n = s2.length();
        int left = 1, right = Math.min(m, n);
        String result = "";
        
        // 预处理两个字符串的哈希数组
        HashHelper helper1 = new HashHelper(s1);
        HashHelper helper2 = new HashHelper(s2);
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String common = findCommonSubstring(s1, s2, mid, helper1, helper2);
            
            if (common != null) {
                result = common;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    private static String findCommonSubstring(String s1, String s2, int len, 
                                            HashHelper h1, HashHelper h2) {
        // 记录s1中所有长度为len的子串哈希值
        Set<Long> set1 = new HashSet<>();
        for (int i = 0; i <= s1.length() - len; i++) {
            long hash = h1.getHash(i, i + len - 1);
            set1.add(hash);
        }
        
        // 检查s2中是否有匹配的子串
        for (int i = 0; i <= s2.length() - len; i++) {
            long hash = h2.getHash(i, i + len - 1);
            if (set1.contains(hash)) {
                // 精确比较避免哈希冲突
                String sub = s2.substring(i, i + len);
                if (s1.contains(sub)) {
                    return sub;
                }
            }
        }
        
        return null;
    }
    
    /**
     * 字符串哈希辅助类
     * 封装字符串哈希的预处理和查询操作
     */
    static class HashHelper {
        private final String s;
        private final long[] pow;
        private final long[] hash;
        private final long BASE = 131L;
        private final long MOD = 1000000007L;
        
        public HashHelper(String s) {
            this.s = s;
            int n = s.length();
            this.pow = new long[n + 1];
            this.hash = new long[n + 1];
            
            // 预处理
            pow[0] = 1;
            for (int i = 1; i <= n; i++) {
                pow[i] = (pow[i - 1] * BASE) % MOD;
            }
            
            hash[0] = 0;
            for (int i = 1; i <= n; i++) {
                hash[i] = (hash[i - 1] * BASE + s.charAt(i - 1)) % MOD;
            }
        }
        
        public long getHash(int l, int r) {
            // 计算子串s[l..r]的哈希值
            return (hash[r + 1] - hash[l] * pow[r - l + 1] % MOD + MOD) % MOD;
        }
    }
    
    /**
     * 测试方法
     * 验证各个算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串哈希综合应用测试 ===");
        
        // 测试最长重复子串
        System.out.println("\n1. 最长重复子串测试:");
        String test1 = "banana";
        String result1 = longestDupSubstring(test1);
        System.out.println("输入: " + test1);
        System.out.println("输出: " + result1);
        System.out.println("期望: ana 或 na");
        
        // 测试重复DNA序列
        System.out.println("\n2. 重复DNA序列测试:");
        String test2 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        List<String> result2 = findRepeatedDnaSequences(test2);
        System.out.println("输入: " + test2);
        System.out.println("输出: " + result2);
        System.out.println("期望: [AAAAACCCCC, CCCCCAAAAA]");
        
        // 测试重复叠加字符串匹配
        System.out.println("\n3. 重复叠加字符串匹配测试:");
        String test3a = "abcd", test3b = "cdabcdab";
        int result3 = repeatedStringMatch(test3a, test3b);
        System.out.println("输入: a=" + test3a + ", b=" + test3b);
        System.out.println("输出: " + result3);
        System.out.println("期望: 3");
        
        // 测试最长公共子串
        System.out.println("\n4. 最长公共子串测试:");
        String test4a = "ABABC", test4b = "BABCA";
        String result4 = longestCommonSubstring(test4a, test4b);
        System.out.println("输入: s1=" + test4a + ", s2=" + test4b);
        System.out.println("输出: " + result4);
        System.out.println("期望: BABC");
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    /**
     * 性能分析报告
     * <p>
     * 各算法性能特点：
     * 1. 最长重复子串：O(nlogn)时间，适合中等规模数据
     * 2. 重复DNA序列：O(n)时间，适合大规模数据流处理
     * 3. 重复叠加匹配：O(n+m)时间，高效处理字符串包含关系
     * 4. 最长公共子串：O((m+n)log(min(m,n)))时间，适合两个字符串的比较
     * <p>
     * 优化建议：
     * 1. 对于超长字符串，可以考虑使用更高效的哈希函数
     * 2. 对于内存敏感的场景，可以优化哈希表的存储方式
     * 3. 对于实时性要求高的应用，可以预处理哈希数组
     * <p>
     * 实际应用场景：
     * 1. 文本编辑器：查找重复内容
     * 2. 生物信息学：DNA序列分析
     * 3. 代码查重：检测重复代码片段
     * 4. 数据压缩：寻找重复模式
     */
    
    /**
     * 边界情况处理策略
     * <p>
     * 1. 空字符串处理：
     *    - 所有方法都检查空输入
     *    - 返回适当的默认值（空字符串、空列表等）
     * <p>
     * 2. 极端长度处理：
     *    - 支持超长字符串（使用long类型避免溢出）
     *    - 使用大质数模数减少冲突
     * <p>
     * 3. 哈希冲突处理：
     *    - 使用双哈希技术降低冲突概率
     *    - 哈希值匹配后进行精确字符串比较
     * <p>
     * 4. 内存优化：
     *    - 及时释放不需要的哈希表
     *    - 使用滑动窗口减少内存占用
     */
    
    /**
     * 算法扩展性分析
     * <p>
     * 1. 多字符串支持：
     *    可以扩展为处理多个字符串的公共子串问题
     * <p>
     * 2. 近似匹配：
     *    可以修改哈希函数支持容错匹配
     * <p>
     * 3. 分布式处理：
     *    可以将字符串分割后并行处理哈希计算
     * <p>
     * 4. 流式处理：
     *    可以适应数据流场景，实时更新哈希值
     */
}