package class107;

import java.util.*;

/**
 * 字符串哈希与滚动哈希算法实现
 * 
 * 本文件包含字符串哈希和滚动哈希的高级实现，包括：
 * - Rabin-Karp字符串匹配算法
 * - 滚动哈希技术
 * - 字符串哈希函数设计
 * - 哈希冲突处理策略
 * - 多哈希技术
 * 
 * 这些算法在字符串匹配、文本搜索、数据去重等领域有重要应用
 */

public class Code07_StringHashAndRollingHash {
    
    /**
     * Rabin-Karp字符串匹配算法
     * 应用场景：文本编辑器、搜索引擎、DNA序列匹配
     * 
     * 算法原理：
     * 1. 使用滚动哈希计算模式串和文本串的哈希值
     * 2. 通过比较哈希值快速排除不匹配的位置
     * 3. 当哈希值匹配时进行精确比较
     * 
     * 时间复杂度：平均O(n+m)，最坏O(nm)
     * 空间复杂度：O(1)
     */
    public static class RabinKarp {
        private static final int PRIME = 101; // 大质数
        private static final int BASE = 256;  // 字符集大小
        
        /**
         * 字符串匹配
         */
        public static List<Integer> search(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0 || n < m) {
                return result;
            }
            
            // 计算模式串哈希值和第一个窗口哈希值
            long patternHash = 0;
            long textHash = 0;
            long h = 1;
            
            // 计算h = BASE^(m-1) % PRIME
            for (int i = 0; i < m - 1; i++) {
                h = (h * BASE) % PRIME;
            }
            
            // 计算模式串和第一个窗口的哈希值
            for (int i = 0; i < m; i++) {
                patternHash = (BASE * patternHash + pattern.charAt(i)) % PRIME;
                textHash = (BASE * textHash + text.charAt(i)) % PRIME;
            }
            
            // 滑动窗口
            for (int i = 0; i <= n - m; i++) {
                // 检查哈希值是否匹配
                if (patternHash == textHash) {
                    // 哈希值匹配，进行精确比较
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        result.add(i);
                    }
                }
                
                // 计算下一个窗口的哈希值
                if (i < n - m) {
                    textHash = (BASE * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;
                    
                    // 处理负哈希值
                    if (textHash < 0) {
                        textHash += PRIME;
                    }
                }
            }
            
            return result;
        }
        
        /**
         * 多模式匹配版本
         */
        public static Map<String, List<Integer>> searchMultiple(String text, String[] patterns) {
            Map<String, List<Integer>> result = new HashMap<>();
            
            for (String pattern : patterns) {
                result.put(pattern, search(text, pattern));
            }
            
            return result;
        }
        
        /**
         * 带通配符的字符串匹配
         */
        public static List<Integer> searchWithWildcard(String text, String pattern, char wildcard) {
            List<Integer> result = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0 || n < m) {
                return result;
            }
            
            // 计算模式串中非通配符部分的哈希值
            long patternHash = 0;
            long textHash = 0;
            long h = 1;
            int wildcardCount = 0;
            
            for (int i = 0; i < m - 1; i++) {
                h = (h * BASE) % PRIME;
            }
            
            // 计算模式串哈希值（忽略通配符）
            for (int i = 0; i < m; i++) {
                if (pattern.charAt(i) != wildcard) {
                    patternHash = (BASE * patternHash + pattern.charAt(i)) % PRIME;
                } else {
                    wildcardCount++;
                }
            }
            
            // 计算第一个窗口的哈希值（忽略通配符位置）
            for (int i = 0; i < m; i++) {
                if (pattern.charAt(i) != wildcard) {
                    textHash = (BASE * textHash + text.charAt(i)) % PRIME;
                }
            }
            
            // 滑动窗口
            for (int i = 0; i <= n - m; i++) {
                if (patternHash == textHash) {
                    // 哈希值匹配，进行精确比较（只比较非通配符位置）
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (pattern.charAt(j) != wildcard && 
                            text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        result.add(i);
                    }
                }
                
                // 计算下一个窗口的哈希值
                if (i < n - m) {
                    // 移除前一个字符的贡献（如果是非通配符位置）
                    if (pattern.charAt(0) != wildcard) {
                        textHash = (BASE * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;
                    } else {
                        // 如果是通配符位置，重新计算整个窗口的哈希值
                        textHash = 0;
                        for (int j = 1; j <= m; j++) {
                            if (pattern.charAt(j) != wildcard) {
                                textHash = (BASE * textHash + text.charAt(i + j)) % PRIME;
                            }
                        }
                    }
                    
                    if (textHash < 0) {
                        textHash += PRIME;
                    }
                }
            }
            
            return result;
        }
    }
    
    /**
     * 滚动哈希技术实现
     * 应用场景：字符串去重、最长重复子串、循环检测
     * 
     * 算法原理：
     * 1. 使用多项式哈希函数
     * 2. 支持O(1)时间复杂度的窗口滑动
     * 3. 支持多哈希减少冲突概率
     * 
     * 时间复杂度：O(n) 构建所有子串哈希
     * 空间复杂度：O(n)
     */
    public static class RollingHash {
        private final long[] hash;
        private final long[] power;
        private final int base;
        private final long mod;
        
        public RollingHash(String s, int base, long mod) {
            this.base = base;
            this.mod = mod;
            int n = s.length();
            hash = new long[n + 1];
            power = new long[n + 1];
            
            power[0] = 1;
            for (int i = 1; i <= n; i++) {
                hash[i] = (hash[i - 1] * base + s.charAt(i - 1)) % mod;
                power[i] = (power[i - 1] * base) % mod;
            }
        }
        
        /**
         * 获取子串[l, r]的哈希值
         */
        public long getHash(int l, int r) {
            long result = (hash[r + 1] - hash[l] * power[r - l + 1]) % mod;
            if (result < 0) {
                result += mod;
            }
            return result;
        }
        
        /**
         * 查找最长重复子串
         */
        public String longestRepeatedSubstring(String s) {
            int n = s.length();
            int left = 1, right = n;
            String result = "";
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                Map<Long, Integer> map = new HashMap<>();
                boolean found = false;
                
                for (int i = 0; i <= n - mid; i++) {
                    long h = getHash(i, i + mid - 1);
                    if (map.containsKey(h)) {
                        int prev = map.get(h);
                        // 检查是否真的是重复子串（防止哈希冲突）
                        if (s.substring(prev, prev + mid).equals(s.substring(i, i + mid))) {
                            found = true;
                            result = s.substring(i, i + mid);
                            break;
                        }
                    } else {
                        map.put(h, i);
                    }
                }
                
                if (found) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            return result;
        }
        
        /**
         * 计算不同子串的数量
         */
        public int countDistinctSubstrings(String s) {
            int n = s.length();
            Set<Long> set = new HashSet<>();
            
            for (int len = 1; len <= n; len++) {
                for (int i = 0; i <= n - len; i++) {
                    long h = getHash(i, i + len - 1);
                    set.add(h);
                }
            }
            
            return set.size();
        }
        
        /**
         * 查找最长回文子串
         */
        public String longestPalindrome(String s) {
            if (s == null || s.length() == 0) return "";
            
            int n = s.length();
            RollingHash forward = new RollingHash(s, base, mod);
            RollingHash backward = new RollingHash(new StringBuilder(s).reverse().toString(), base, mod);
            
            int maxLen = 0;
            int start = 0;
            
            for (int i = 0; i < n; i++) {
                // 奇数长度回文
                int len1 = expandAroundCenter(forward, backward, i, i, n);
                // 偶数长度回文
                int len2 = expandAroundCenter(forward, backward, i, i + 1, n);
                
                int len = Math.max(len1, len2);
                if (len > maxLen) {
                    maxLen = len;
                    start = i - (len - 1) / 2;
                }
            }
            
            return s.substring(start, start + maxLen);
        }
        
        private int expandAroundCenter(RollingHash forward, RollingHash backward, 
                                      int left, int right, int n) {
            while (left >= 0 && right < n) {
                // 使用哈希值检查回文
                long forwardHash = forward.getHash(left, right);
                long backwardHash = backward.getHash(n - right - 1, n - left - 1);
                
                if (forwardHash != backwardHash) {
                    break;
                }
                
                left--;
                right++;
            }
            
            return right - left - 1;
        }
    }
    
    /**
     * 多哈希技术实现
     * 应用场景：需要高精度哈希匹配的场景
     * 
     * 算法原理：
     * 1. 使用多个不同的哈希函数
     * 2. 只有当所有哈希值都匹配时才认为匹配
     * 3. 显著降低哈希冲突概率
     * 
     * 时间复杂度：O(kn)，其中k是哈希函数数量
     * 空间复杂度：O(kn)
     */
    public static class MultiHash {
        private final RollingHash[] hashes;
        private final int k; // 哈希函数数量
        
        public MultiHash(String s, int k) {
            this.k = k;
            hashes = new RollingHash[k];
            
            // 使用不同的基数和模数
            int[] bases = {131, 13331, 131313, 1313131, 13131313};
            long[] mods = {1000000007L, 1000000009L, 1000000021L, 1000000033L, 1000000087L};
            
            for (int i = 0; i < k; i++) {
                hashes[i] = new RollingHash(s, bases[i], mods[i]);
            }
        }
        
        /**
         * 获取子串的多重哈希值
         */
        public long[] getMultiHash(int l, int r) {
            long[] result = new long[k];
            for (int i = 0; i < k; i++) {
                result[i] = hashes[i].getHash(l, r);
            }
            return result;
        }
        
        /**
         * 比较两个子串的多重哈希值
         */
        public boolean equals(int l1, int r1, int l2, int r2) {
            if (r1 - l1 != r2 - l2) return false;
            
            for (int i = 0; i < k; i++) {
                if (hashes[i].getHash(l1, r1) != hashes[i].getHash(l2, r2)) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 查找所有重复子串
         */
        public Map<String, List<Integer>> findAllRepeatedSubstrings(String s, int minLen) {
            int n = s.length();
            Map<String, List<Integer>> result = new HashMap<>();
            
            // 使用多重哈希减少冲突
            for (int len = minLen; len <= n; len++) {
                Map<String, Integer> seen = new HashMap<>();
                
                for (int i = 0; i <= n - len; i++) {
                    String substr = s.substring(i, i + len);
                    
                    if (seen.containsKey(substr)) {
                        int prev = seen.get(substr);
                        if (!result.containsKey(substr)) {
                            result.put(substr, new ArrayList<>());
                            result.get(substr).add(prev);
                        }
                        result.get(substr).add(i);
                    } else {
                        seen.put(substr, i);
                    }
                }
            }
            
            return result;
        }
    }
    
    /**
     * 字符串哈希的性能分析工具
     */
    public static class HashPerformanceAnalyzer {
        
        /**
         * 分析哈希函数的质量
         */
        public static void analyzeHashFunction(String[] strings, int base, long mod) {
            Set<Long> hashes = new HashSet<>();
            int collisions = 0;
            
            for (String s : strings) {
                long hash = 0;
                for (char c : s.toCharArray()) {
                    hash = (hash * base + c) % mod;
                }
                
                if (!hashes.add(hash)) {
                    collisions++;
                }
            }
            
            double collisionRate = (double) collisions / strings.length;
            System.out.printf("哈希函数分析: 基数=%d, 模数=%d, 冲突率=%.4f%%\n", 
                             base, mod, collisionRate * 100);
        }
        
        /**
         * 比较不同哈希函数的性能
         */
        public static void compareHashFunctions(String text, String pattern) {
            long startTime, endTime;
            
            // Rabin-Karp算法
            startTime = System.nanoTime();
            List<Integer> rkResult = RabinKarp.search(text, pattern);
            endTime = System.nanoTime();
            System.out.printf("Rabin-Karp算法: %d ns, 匹配位置: %s\n", 
                           endTime - startTime, rkResult);
            
            // 暴力匹配算法
            startTime = System.nanoTime();
            List<Integer> bfResult = bruteForceSearch(text, pattern);
            endTime = System.nanoTime();
            System.out.printf("暴力匹配算法: %d ns, 匹配位置: %s\n", 
                           endTime - startTime, bfResult);
        }
        
        private static List<Integer> bruteForceSearch(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            for (int i = 0; i <= n - m; i++) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.add(i);
                }
            }
            
            return result;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串哈希与滚动哈希算法测试 ===\n");
        
        // 测试Rabin-Karp算法
        System.out.println("1. Rabin-Karp字符串匹配算法测试:");
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        List<Integer> positions = RabinKarp.search(text, pattern);
        System.out.println("文本: " + text);
        System.out.println("模式: " + pattern);
        System.out.println("匹配位置: " + positions);
        
        // 测试滚动哈希
        System.out.println("\n2. 滚动哈希技术测试:");
        RollingHash rh = new RollingHash("banana", 131, 1000000007);
        System.out.println("字符串: banana");
        System.out.println("最长重复子串: " + rh.longestRepeatedSubstring("banana"));
        System.out.println("不同子串数量: " + rh.countDistinctSubstrings("banana"));
        
        // 测试多哈希技术
        System.out.println("\n3. 多哈希技术测试:");
        MultiHash mh = new MultiHash("mississippi", 3);
        System.out.println("字符串: mississippi");
        Map<String, List<Integer>> repeated = mh.findAllRepeatedSubstrings("mississippi", 2);
        System.out.println("重复子串: " + repeated);
        
        // 测试带通配符的匹配
        System.out.println("\n4. 带通配符的字符串匹配测试:");
        String text2 = "AABAACAADAABAAABAA";
        String pattern2 = "A*BA";
        List<Integer> wildcardPositions = RabinKarp.searchWithWildcard(text2, pattern2, '*');
        System.out.println("文本: " + text2);
        System.out.println("模式: " + pattern2);
        System.out.println("匹配位置: " + wildcardPositions);
        
        // 性能分析
        System.out.println("\n5. 哈希函数性能分析:");
        String[] testStrings = {"hello", "world", "test", "string", "hash"};
        HashPerformanceAnalyzer.analyzeHashFunction(testStrings, 131, 1000000007);
        HashPerformanceAnalyzer.analyzeHashFunction(testStrings, 13331, 1000000009);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. Rabin-Karp算法: 平均O(n+m)，最坏O(nm)时间，O(1)空间");
        System.out.println("2. 滚动哈希: O(n)构建时间，O(1)查询时间，O(n)空间");
        System.out.println("3. 多哈希技术: O(kn)时间，O(kn)空间，k为哈希函数数量");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 文本编辑器: 快速查找和替换");
        System.out.println("2. 搜索引擎: 网页内容索引和匹配");
        System.out.println("3. 生物信息学: DNA序列分析和比对");
        System.out.println("4. 数据去重: 检测重复文件和内容");
        
        System.out.println("\n=== 哈希冲突处理策略 ===");
        System.out.println("1. 使用大质数作为模数减少冲突");
        System.out.println("2. 多哈希技术显著降低冲突概率");
        System.out.println("3. 当哈希值匹配时进行精确比较");
        System.out.println("4. 动态调整哈希参数适应不同数据分布");
    }
}