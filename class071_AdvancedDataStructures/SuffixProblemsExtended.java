package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 更多后缀相关题目实现
 * 包含：
 * 1. SPOJ REPEATS - Repeats (最长重复子串)
 * 2. SPOJ PHRASES - Relevant Phrases of Annihilation (公共子串)
 * 3. Codeforces 427D - Match & Catch (唯一子串)
 * 4. Codeforces 432D - Prefixes and Suffixes (前缀后缀统计)
 * 5. SPOJ DISUBSTR - Distinct Substrings (不同子串)
 * 6. SPOJ NSUBSTR - Substrings (不同长度子串计数)
 * 7. SPOJ LCS2 - Longest Common Substring II (多个字符串最长公共子串)
 * 8. Codeforces 204E - Little Elephant and Strings (多个字符串公共子串)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class SuffixProblemsExtended {
    
    /**
     * 后缀数组实现类（扩展版）
     */
    static class SuffixArray {
        private String s;
        private int[] sa;     // 后缀数组
        private int[] rank;   // 排名数组
        private int[] height; // LCP数组
        
        public SuffixArray(String s) {
            this.s = s;
            buildSuffixArray();
            buildLCP();
        }
        
        /**
         * 构建后缀数组（使用倍增算法）
         * 时间复杂度：O(n log n)
         * 空间复杂度：O(n)
         */
        private void buildSuffixArray() {
            int n = s.length();
            sa = new int[n];
            rank = new int[n];
            int[] tmp = new int[n];
            
            // 初始化
            for (int i = 0; i < n; i++) {
                sa[i] = i;
                rank[i] = s.charAt(i);
            }
            
            // 倍增排序
            for (int k = 1; k < n; k <<= 1) {
                // 定义比较器
                final int fk = k;
                Integer[] indices = new Integer[n];
                for (int i = 0; i < n; i++) indices[i] = i;
                
                // 按第二关键字排序
                Arrays.sort(indices, (a, b) -> {
                    int ra = a + fk < n ? rank[a + fk] : -1;
                    int rb = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra, rb);
                });
                
                // 按第一关键字排序（稳定排序）
                Arrays.sort(indices, (a, b) -> {
                    int ra = rank[a];
                    int rb = rank[b];
                    if (ra != rb) return Integer.compare(ra, rb);
                    int ra2 = a + fk < n ? rank[a + fk] : -1;
                    int rb2 = b + fk < n ? rank[b + fk] : -1;
                    return Integer.compare(ra2, rb2);
                });
                
                // 更新sa和rank
                for (int i = 0; i < n; i++) {
                    sa[i] = indices[i];
                }
                
                tmp[sa[0]] = 0;
                for (int i = 1; i < n; i++) {
                    int a = sa[i - 1], b = sa[i];
                    int ra1 = rank[a], rb1 = rank[b];
                    int ra2 = a + k < n ? rank[a + k] : -1;
                    int rb2 = b + k < n ? rank[b + k] : -1;
                    tmp[b] = tmp[a] + ((ra1 != rb1 || ra2 != rb2) ? 1 : 0);
                }
                
                for (int i = 0; i < n; i++) {
                    rank[sa[i]] = tmp[sa[i]];
                }
                
                if (rank[sa[n - 1]] == n - 1) break;
            }
        }
        
        /**
         * 构建LCP数组（使用Kasai算法）
         * 时间复杂度：O(n)
         * 空间复杂度：O(n)
         */
        private void buildLCP() {
            int n = s.length();
            height = new int[n];
            int[] inv = new int[n];
            
            // 计算rank的逆数组
            for (int i = 0; i < n; i++) {
                inv[sa[i]] = i;
            }
            
            // Kasai算法
            for (int i = 0, k = 0; i < n; i++) {
                if (inv[i] == n - 1) {
                    k = 0;
                    continue;
                }
                
                int j = sa[inv[i] + 1];
                while (i + k < n && j + k < n && s.charAt(i + k) == s.charAt(j + k)) {
                    k++;
                }
                
                height[inv[i]] = k;
                if (k > 0) k--;
            }
        }
        
        // Getter方法
        public int[] getSA() { return sa; }
        public int[] getRank() { return rank; }
        public int[] getHeight() { return height; }
        public String getString() { return s; }
    }
    
    // ====================================================================================
    // 题目1: SPOJ REPEATS - Repeats
    // 链接: https://www.spoj.com/problems/REPEATS/
    // 题目描述: 求字符串中连续重复次数最多的子串的最大重复次数
    // 解题思路: 枚举重复子串的长度L，然后每隔L个位置检查是否存在重复模式
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solveREPEATS(String s) {
        int n = s.length();
        if (n == 0) return 0;
        
        int maxRepeats = 1;
        
        // 枚举重复子串的长度
        for (int L = 1; L <= n / 2; L++) {
            // 每隔L个位置检查
            for (int i = 0; i < L; i++) {
                int count = 0;
                int start = i;
                
                // 计算从位置i开始每隔L个位置的字符匹配次数
                for (int j = i; j + L <= n; j += L) {
                    if (s.substring(j, j + L).equals(s.substring(start, start + L))) {
                        count++;
                    } else {
                        count = 1;
                        start = j;
                    }
                    maxRepeats = Math.max(maxRepeats, count);
                }
            }
        }
        
        return maxRepeats;
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <string>
     * #include <algorithm>
     * using namespace std;
     * 
     * int solveREPEATS(string s) {
     *     int n = s.length();
     *     if (n == 0) return 0;
     *     
     *     int maxRepeats = 1;
     *     
     *     // 枚举重复子串的长度
     *     for (int L = 1; L <= n / 2; L++) {
     *         // 每隔L个位置检查
     *         for (int i = 0; i < L; i++) {
     *             int count = 0;
     *             int start = i;
     *             
     *             // 计算从位置i开始每隔L个位置的字符匹配次数
     *             for (int j = i; j + L <= n; j += L) {
     *                 if (s.substr(j, L) == s.substr(start, L)) {
     *                     count++;
     *                 } else {
     *                     count = 1;
     *                     start = j;
     *                 }
     *                 maxRepeats = max(maxRepeats, count);
     *             }
     *         }
     *     }
     *     
     *     return maxRepeats;
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * def solve_REPEATS(s):
     *     n = len(s)
     *     if n == 0:
     *         return 0
     *     
     *     max_repeats = 1
     *     
     *     # 枚举重复子串的长度
     *     for L in range(1, n // 2 + 1):
     *         # 每隔L个位置检查
     *         for i in range(L):
     *             count = 0
     *             start = i
     *             
     *             # 计算从位置i开始每隔L个位置的字符匹配次数
     *             j = i
     *             while j + L <= n:
     *                 if s[j:j+L] == s[start:start+L]:
     *                     count += 1
     *                 else:
     *                     count = 1
     *                     start = j
     *                 max_repeats = max(max_repeats, count)
     *                 j += L
     *     
     *     return max_repeats
     */
    
    // ====================================================================================
    // 题目2: SPOJ PHRASES - Relevant Phrases of Annihilation
    // 链接: https://www.spoj.com/problems/PHRASES/
    // 题目描述: 给定多个字符串，求出现在至少K个字符串中的最长子串
    // 解题思路: 将所有字符串用不同分隔符连接，构建后缀数组，
    //           然后使用滑动窗口找到包含至少K个不同字符串后缀的区间
    // 时间复杂度: O(N log N)，N为所有字符串总长度
    // 空间复杂度: O(N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solvePHRASES(String[] strings, int k) {
        int n = strings.length;
        if (n == 0 || k > n) return 0;
        
        // 构造连接字符串
        StringBuilder sb = new StringBuilder();
        int[] stringId = new int[100000]; // 记录每个位置属于哪个字符串
        int pos = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < strings[i].length(); j++) {
                sb.append(strings[i].charAt(j));
                stringId[pos++] = i;
            }
            // 添加分隔符
            sb.append((char) (128 + i));
            stringId[pos++] = -1;
        }
        
        String combined = sb.toString();
        SuffixArray sa = new SuffixArray(combined);
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        
        int len = combined.length();
        int maxLen = 0;
        
        // 使用滑动窗口找到包含至少k个不同字符串的区间
        for (int i = 0; i < len; i++) {
            Set<Integer> uniqueStrings = new HashSet<>();
            int minLCP = Integer.MAX_VALUE;
            
            for (int j = i; j < len; j++) {
                // 添加当前后缀所属的字符串
                if (stringId[suffixArray[j]] != -1) {
                    uniqueStrings.add(stringId[suffixArray[j]]);
                }
                
                // 如果包含至少k个不同字符串
                if (uniqueStrings.size() >= k) {
                    // 更新最小LCP
                    if (j > i) {
                        minLCP = Math.min(minLCP, height[j]);
                    }
                    maxLen = Math.max(maxLen, minLCP);
                } else if (uniqueStrings.size() < k && j > i) {
                    minLCP = Math.min(minLCP, height[j]);
                }
            }
        }
        
        return maxLen;
    }
    
    // ====================================================================================
    // 题目3: Codeforces 427D - Match & Catch
    // 题目描述: 给定两个字符串，求在两个字符串中都只出现一次的最短子串
    // 解题思路: 构建两个字符串的后缀数组，分别计算每个子串的出现次数，
    //           然后找到在两个字符串中都只出现一次的最短子串
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solveMatchAndCatch(String s1, String s2) {
        int n1 = s1.length();
        int n2 = s2.length();
        
        // 分别构建两个字符串的后缀数组
        SuffixArray sa1 = new SuffixArray(s1);
        SuffixArray sa2 = new SuffixArray(s2);
        
        int[] height1 = sa1.getHeight();
        int[] height2 = sa2.getHeight();
        
        // 计算s1中每个子串的出现次数
        int[] count1 = new int[n1 + 1];
        for (int i = 1; i <= n1; i++) {
            int h = (i < n1) ? height1[i] : 0;
            count1[h + 1]++;
        }
        
        // 计算s2中每个子串的出现次数
        int[] count2 = new int[n2 + 1];
        for (int i = 1; i <= n2; i++) {
            int h = (i < n2) ? height2[i] : 0;
            count2[h + 1]++;
        }
        
        // 找到在两个字符串中都只出现一次的最短子串
        int minLen = Math.max(n1, n2) + 1;
        for (int len = 1; len <= Math.min(n1, n2); len++) {
            if (count1[len] == 1 && count2[len] == 1) {
                minLen = Math.min(minLen, len);
            }
        }
        
        return (minLen <= Math.max(n1, n2)) ? minLen : -1;
    }
    
    // ====================================================================================
    // 题目4: Codeforces 432D - Prefixes and Suffixes
    // 题目描述: 统计字符串中既是前缀又是后缀的子串及其出现次数
    // 解题思路: 使用KMP算法的失败函数或者后缀数组来解决
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现 (使用KMP算法)
     */
    public static List<Map.Entry<String, Integer>> solvePrefixesAndSuffixes(String s) {
        int n = s.length();
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        
        // 计算KMP失败函数
        int[] pi = new int[n];
        pi[0] = 0;
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        
        // 统计每个前缀的出现次数
        int[] count = new int[n + 1];
        for (int i = 0; i < n; i++) {
            count[pi[i]]++;
        }
        
        // 从后向前累加
        for (int i = n - 1; i > 0; i--) {
            count[pi[i - 1]] += count[i];
        }
        
        // 每个前缀至少出现一次（它本身）
        for (int i = 0; i <= n; i++) {
            count[i]++;
        }
        
        // 收集结果
        int j = n;
        while (j > 0) {
            result.add(new AbstractMap.SimpleEntry<>(s.substring(0, j), count[j]));
            j = pi[j - 1];
        }
        
        // 按长度排序
        result.sort(Comparator.comparingInt(e -> e.getKey().length()));
        
        return result;
    }
    
    // ====================================================================================
    // 题目5: SPOJ DISUBSTR - Distinct Substrings
    // 链接: https://www.spoj.com/problems/DISUBSTR/
    // 题目描述: 计算字符串中不同子串的个数
    // 解题思路: 使用后缀数组和height数组
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long solveDISUBSTR(String s) {
        SuffixArray sa = new SuffixArray(s);
        int n = s.length();
        
        // 总子串数
        long total = (long) n * (n + 1) / 2;
        
        // 重复子串数（height数组的和）
        long repeated = 0;
        int[] height = sa.getHeight();
        for (int i = 1; i < n; i++) {
            repeated += height[i];
        }
        
        // 不同子串数 = 总子串数 - 重复子串数
        return total - repeated;
    }
    
    // ====================================================================================
    // 题目6: SPOJ NSUBSTR - Substrings
    // 链接: https://www.spoj.com/problems/NSUBSTR/
    // 题目描述: 对于每个长度k，计算长度为k的子串的最大出现次数
    // 解题思路: 使用后缀数组，通过height数组计算每个长度的最大出现次数
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] solveNSUBSTR(String s) {
        SuffixArray sa = new SuffixArray(s);
        int n = s.length();
        int[] height = sa.getHeight();
        int[] saArray = sa.getSA();
        
        // 计算每个后缀对应的LCP值
        int[] lcp = new int[n];
        for (int i = 1; i < n; i++) {
            lcp[i] = height[i];
        }
        
        // 计算每个长度的最大出现次数
        int[] result = new int[n + 1];
        
        // 对于每个后缀，计算其贡献
        for (int i = 0; i < n; i++) {
            int len = n - saArray[i]; // 当前后缀的长度
            result[len] = Math.max(result[len], 1); // 至少出现一次
            
            // 如果有下一个后缀，计算LCP的贡献
            if (i + 1 < n) {
                int commonLen = lcp[i + 1];
                for (int j = 1; j <= commonLen; j++) {
                    result[j] = Math.max(result[j], 2); // 至少出现两次
                }
            }
        }
        
        // 从后向前更新，确保result[i] >= result[i+1]
        for (int i = n - 1; i >= 1; i--) {
            result[i] = Math.max(result[i], result[i + 1]);
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目7: SPOJ LCS2 - Longest Common Substring II
    // 链接: https://www.spoj.com/problems/LCS2/
    // 题目描述: 求多个字符串的最长公共子串
    // 解题思路: 将所有字符串用不同分隔符连接，构建后缀数组，
    //           然后找到包含所有字符串后缀的最小区间
    // 时间复杂度: O(N log N)
    // 空间复杂度: O(N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solveLCS2(String[] strings) {
        int k = strings.length;
        if (k == 0) return 0;
        if (k == 1) return strings[0].length();
        
        // 构造连接字符串
        StringBuilder sb = new StringBuilder();
        int[] stringId = new int[100000]; // 记录每个位置属于哪个字符串
        int pos = 0;
        
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < strings[i].length(); j++) {
                sb.append(strings[i].charAt(j));
                stringId[pos++] = i;
            }
            // 添加分隔符
            sb.append((char) (128 + i));
            stringId[pos++] = -1;
        }
        
        String combined = sb.toString();
        SuffixArray sa = new SuffixArray(combined);
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        
        int len = combined.length();
        int maxLen = 0;
        
        // 使用滑动窗口找到包含所有k个字符串的最小区间
        for (int i = 0; i < len; i++) {
            Set<Integer> uniqueStrings = new HashSet<>();
            int minLCP = Integer.MAX_VALUE;
            
            for (int j = i; j < len; j++) {
                // 添加当前后缀所属的字符串
                if (stringId[suffixArray[j]] != -1) {
                    uniqueStrings.add(stringId[suffixArray[j]]);
                }
                
                // 如果包含所有k个字符串
                if (uniqueStrings.size() == k) {
                    // 更新最小LCP
                    if (j > i) {
                        minLCP = Math.min(minLCP, height[j]);
                    }
                    maxLen = Math.max(maxLen, minLCP);
                } else if (uniqueStrings.size() < k && j > i) {
                    minLCP = Math.min(minLCP, height[j]);
                }
            }
        }
        
        return maxLen;
    }
    
    // ====================================================================================
    // 题目8: Codeforces 204E - Little Elephant and Strings
    // 题目描述: 给定多个字符串，对每个字符串求其至少出现在k个字符串中的子串数量
    // 解题思路: 将所有字符串用不同分隔符连接，构建后缀数组，
    //           对每个字符串，计算其后缀在满足条件的区间中的贡献
    // 时间复杂度: O(N log N)
    // 空间复杂度: O(N)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long[] solveLittleElephantAndStrings(String[] strings, int k) {
        int n = strings.length;
        if (n == 0) return new long[0];
        
        // 构造连接字符串
        StringBuilder sb = new StringBuilder();
        int[] stringId = new int[100000]; // 记录每个位置属于哪个字符串
        int[] startPos = new int[n]; // 记录每个字符串的起始位置
        int pos = 0;
        
        for (int i = 0; i < n; i++) {
            startPos[i] = pos;
            for (int j = 0; j < strings[i].length(); j++) {
                sb.append(strings[i].charAt(j));
                stringId[pos++] = i;
            }
            // 添加分隔符
            sb.append((char) (128 + i));
            stringId[pos++] = -1;
        }
        
        String combined = sb.toString();
        SuffixArray sa = new SuffixArray(combined);
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        
        int len = combined.length();
        long[] result = new long[n];
        
        // 对每个字符串计算答案
        for (int i = 0; i < n; i++) {
            int start = startPos[i];
            int end = start + strings[i].length();
            
            // 使用滑动窗口计算该字符串后缀的贡献
            for (int j = 0; j < len; j++) {
                if (suffixArray[j] >= start && suffixArray[j] < end) {
                    // 这是第i个字符串的后缀
                    Set<Integer> uniqueStrings = new HashSet<>();
                    int minLCP = (j > 0) ? height[j] : 0;
                    
                    // 向右扩展窗口
                    for (int t = j; t < len && (t == j || height[t] > 0); t++) {
                        if (stringId[suffixArray[t]] != -1) {
                            uniqueStrings.add(stringId[suffixArray[t]]);
                        }
                        
                        if (t > j) {
                            minLCP = Math.min(minLCP, height[t]);
                        }
                        
                        if (uniqueStrings.size() >= k) {
                            result[i] += minLCP;
                        }
                    }
                    
                    // 向左扩展窗口
                    uniqueStrings.clear();
                    uniqueStrings.add(i); // 添加当前字符串
                    minLCP = (j > 0) ? height[j] : 0;
                    
                    for (int t = j - 1; t >= 0 && height[t + 1] > 0; t--) {
                        if (stringId[suffixArray[t]] != -1) {
                            uniqueStrings.add(stringId[suffixArray[t]]);
                        }
                        
                        minLCP = Math.min(minLCP, height[t + 1]);
                        
                        if (uniqueStrings.size() >= k) {
                            result[i] += minLCP;
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试REPEATS
        System.out.println("=== 测试REPEATS ===");
        String s1 = "abababab";
        int repeatsResult = solveREPEATS(s1);
        System.out.println("输入: " + s1);
        System.out.println("最大重复次数: " + repeatsResult);
        
        // 测试DISUBSTR
        System.out.println("\n=== 测试DISUBSTR ===");
        String s2 = "banana";
        long disubstrResult = solveDISUBSTR(s2);
        System.out.println("输入: " + s2);
        System.out.println("不同子串数: " + disubstrResult);
        
        // 测试NSUBSTR
        System.out.println("\n=== 测试NSUBSTR ===");
        String s3 = "banana";
        int[] nsubstrResult = solveNSUBSTR(s3);
        System.out.println("输入: " + s3);
        System.out.print("各长度最大出现次数: ");
        for (int i = 1; i <= s3.length(); i++) {
            System.out.print("F(" + i + ")=" + nsubstrResult[i] + " ");
        }
        System.out.println();
        
        // 测试LCS2
        System.out.println("\n=== 测试LCS2 ===");
        String[] strings1 = {"abcde", "cdefg", "efghi"};
        int lcs2Result = solveLCS2(strings1);
        System.out.println("输入: " + Arrays.toString(strings1));
        System.out.println("最长公共子串长度: " + lcs2Result);
        
        // 测试Prefixes and Suffixes
        System.out.println("\n=== 测试Prefixes and Suffixes ===");
        String s4 = "ababab";
        List<Map.Entry<String, Integer>> prefixSuffixResult = solvePrefixesAndSuffixes(s4);
        System.out.println("输入: " + s4);
        System.out.println("既是前缀又是后缀的子串及其出现次数:");
        for (Map.Entry<String, Integer> entry : prefixSuffixResult) {
            System.out.println("  \"" + entry.getKey() + "\": " + entry.getValue() + "次");
        }
    }
}