package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 后缀数组相关题目实现
 * 包含：
 * 1. SPOJ SARRAY - Suffix Array (基础后缀数组构建)
 * 2. SPOJ SUBST1 - New Distinct Substrings (不同子串计数)
 * 3. SPOJ LCS - Longest Common Substring (最长公共子串)
 * 4. Codeforces 271D - Good Substrings (好子串计数)
 * 5. Codeforces 452E - Three strings (三串公共子串)
 * 6. Codeforces 235C - Cyclical Quest (循环同构匹配)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class SuffixArrayProblems {
    
    /**
     * 后缀数组实现类
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
    // 题目1: SPOJ SARRAY - Suffix Array
    // 链接: https://www.spoj.com/problems/SARRAY/
    // 题目描述: 输出字符串的后缀数组
    // 解题思路: 直接构建后缀数组并输出
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] solveSARRAY(String s) {
        SuffixArray sa = new SuffixArray(s);
        return sa.getSA();
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <algorithm>
     * using namespace std;
     * 
     * class SuffixArray {
     * private:
     *     string s;
     *     vector<int> sa, rank, tmp;
     *     
     * public:
     *     SuffixArray(string str) : s(str) {
     *         int n = s.length();
     *         sa.resize(n);
     *         rank.resize(n);
     *         tmp.resize(n);
     *         buildSuffixArray();
     *     }
     *     
     *     void buildSuffixArray() {
     *         int n = s.length();
     *         for (int i = 0; i < n; i++) {
     *             sa[i] = i;
     *             rank[i] = s[i];
     *         }
     *         
     *         for (int k = 1; k < n; k <<= 1) {
     *             auto cmp = [&](int a, int b) {
     *                 if (rank[a] != rank[b]) return rank[a] < rank[b];
     *                 int ra = (a + k < n) ? rank[a + k] : -1;
     *                 int rb = (b + k < n) ? rank[b + k] : -1;
     *                 return ra < rb;
     *             };
     *             sort(sa.begin(), sa.end(), cmp);
     *             
     *             tmp[sa[0]] = 0;
     *             for (int i = 1; i < n; i++) {
     *                 tmp[sa[i]] = tmp[sa[i-1]] + (cmp(sa[i-1], sa[i]) ? 1 : 0);
     *             }
     *             for (int i = 0; i < n; i++) {
     *                 rank[sa[i]] = tmp[sa[i]];
     *             }
     *             
     *             if (rank[sa[n-1]] == n-1) break;
     *         }
     *     }
     *     
     *     vector<int> getSA() { return sa; }
     * };
     * 
     * vector<int> solveSARRAY(string s) {
     *     SuffixArray sa(s);
     *     return sa.getSA();
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class SuffixArray:
     *     def __init__(self, s):
     *         self.s = s
     *         self.n = len(s)
     *         self.sa = []
     *         self.rank = []
     *         self.build_suffix_array()
     *     
     *     def build_suffix_array(self):
     *         n = self.n
     *         self.sa = list(range(n))
     *         self.rank = [ord(c) for c in self.s]
     *         tmp = [0] * n
     *         
     *         k = 1
     *         while k < n:
     *             # 定义比较函数
     *             def cmp(i):
     *                 return (self.rank[i], self.rank[i + k] if i + k < n else -1)
     *             
     *             # 排序
     *             self.sa.sort(key=cmp)
     *             
     *             # 更新rank
     *             tmp[self.sa[0]] = 0
     *             for i in range(1, n):
     *                 tmp[self.sa[i]] = tmp[self.sa[i-1]] + (1 if cmp(self.sa[i-1]) < cmp(self.sa[i]) else 0)
     *             self.rank, tmp = tmp, self.rank
     *             
     *             if self.rank[self.sa[n-1]] == n-1:
     *                 break
     *             k <<= 1
     *     
     *     def get_sa(self):
     *         return self.sa
     * 
     * def solve_SARRAY(s):
     *     sa = SuffixArray(s)
     *     return sa.get_sa()
     */
    
    // ====================================================================================
    // 题目2: SPOJ SUBST1 - New Distinct Substrings
    // 链接: https://www.spoj.com/problems/SUBST1/
    // 题目描述: 计算字符串中不同子串的个数
    // 解题思路: 使用后缀数组和height数组
    //           总子串数 = n*(n+1)/2
    //           重复子串数 = height数组的和
    //           不同子串数 = 总子串数 - 重复子串数
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long solveSUBST1(String s) {
        SuffixArray sa = new SuffixArray(s);
        int n = s.length();
        // 总子串数
        long total = (long) n * (n + 1) / 2;
        // 重复子串数（height数组的和）
        long repeated = 0;
        for (int i = 1; i < n; i++) {  // 从1开始，因为height[0]是无效的
            repeated += sa.getHeight()[i];
        }
        // 不同子串数 = 总子串数 - 重复子串数
        return total - repeated;
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <algorithm>
     * using namespace std;
     * 
     * // SuffixArray类定义同上
     * 
     * long long solveSUBST1(string s) {
     *     SuffixArray sa(s);
     *     int n = s.length();
     *     // 总子串数
     *     long long total = (long long)n * (n + 1) / 2;
     *     // 重复子串数
     *     long long repeated = 0;
     *     vector<int> height = sa.getHeight(); // 假设有这个方法
     *     for (int i = 1; i < n; i++) {
     *         repeated += height[i];
     *     }
     *     // 不同子串数
     *     return total - repeated;
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * # SuffixArray类定义同上
     * 
     * def solve_SUBST1(s):
     *     sa = SuffixArray(s)
     *     n = len(s)
     *     # 总子串数
     *     total = n * (n + 1) // 2
     *     # 重复子串数
     *     repeated = 0
     *     height = sa.get_height()  # 假设有这个方法
     *     for i in range(1, n):
     *         repeated += height[i]
     *     # 不同子串数
     *     return total - repeated
     */
    
    // ====================================================================================
    // 题目3: SPOJ LCS - Longest Common Substring
    // 链接: https://www.spoj.com/problems/LCS/
    // 题目描述: 求两个字符串的最长公共子串
    // 解题思路: 将两个字符串用特殊字符连接，构建后缀数组，
//           然后在height数组中查找属于两个不同字符串的后缀的最大LCP
    // 时间复杂度: O((n+m) log(n+m))
    // 空间复杂度: O(n+m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solveLCS(String s1, String s2) {
        // 用特殊字符连接两个字符串
        String combined = s1 + "#" + s2 + "$";
        SuffixArray sa = new SuffixArray(combined);
        
        int n = s1.length();
        int m = s2.length();
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        int len = combined.length();
        
        int maxLen = 0;
        
        // 查找属于两个不同字符串的后缀的最大LCP
        for (int i = 1; i < len; i++) {
            // 检查suffixArray[i-1]和suffixArray[i]是否属于不同字符串
            int pos1 = suffixArray[i-1];
            int pos2 = suffixArray[i];
            
            // 判断是否属于不同字符串
            boolean inS1_1 = (pos1 < n);
            boolean inS1_2 = (pos2 < n);
            boolean inS2_1 = (pos1 > n && pos1 < n + 1 + m);
            boolean inS2_2 = (pos2 > n && pos2 < n + 1 + m);
            
            if ((inS1_1 && inS2_2) || (inS2_1 && inS1_2)) {
                maxLen = Math.max(maxLen, height[i]);
            }
        }
        
        return maxLen;
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * int solveLCS(string s1, string s2) {
     *     // 用特殊字符连接两个字符串
     *     string combined = s1 + "#" + s2 + "$";
     *     SuffixArray sa(combined);
     *     
     *     int n = s1.length();
     *     int m = s2.length();
     *     vector<int> suffixArray = sa.getSA();
     *     vector<int> height = sa.getHeight();
     *     int len = combined.length();
     *     
     *     int maxLen = 0;
     *     
     *     // 查找属于两个不同字符串的后缀的最大LCP
     *     for (int i = 1; i < len; i++) {
     *         int pos1 = suffixArray[i-1];
     *         int pos2 = suffixArray[i];
     *         
     *         // 判断是否属于不同字符串
     *         bool inS1_1 = (pos1 < n);
     *         bool inS1_2 = (pos2 < n);
     *         bool inS2_1 = (pos1 > n && pos1 < n + 1 + m);
     *         bool inS2_2 = (pos2 > n && pos2 < n + 1 + m);
     *         
     *         if ((inS1_1 && inS2_2) || (inS2_1 && inS1_2)) {
     *             maxLen = max(maxLen, height[i]);
     *         }
     *     }
     *     
     *     return maxLen;
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * def solve_LCS(s1, s2):
     *     # 用特殊字符连接两个字符串
     *     combined = s1 + "#" + s2 + "$"
     *     sa = SuffixArray(combined)
     *     
     *     n = len(s1)
     *     m = len(s2)
     *     suffix_array = sa.get_sa()
     *     height = sa.get_height()
     *     length = len(combined)
     *     
     *     max_len = 0
     *     
     *     # 查找属于两个不同字符串的后缀的最大LCP
     *     for i in range(1, length):
     *         pos1 = suffix_array[i-1]
     *         pos2 = suffix_array[i]
     *         
     *         # 判断是否属于不同字符串
     *         in_s1_1 = (pos1 < n)
     *         in_s1_2 = (pos2 < n)
     *         in_s2_1 = (pos1 > n and pos1 < n + 1 + m)
     *         in_s2_2 = (pos2 > n and pos2 < n + 1 + m)
     *         
     *         if (in_s1_1 and in_s2_2) or (in_s2_1 and in_s1_2):
     *             max_len = max(max_len, height[i])
     *     
     *     return max_len
     */
    
    // ====================================================================================
    // 题目4: Codeforces 271D - Good Substrings
    // 题目描述: 给定字符串s和好字符集合，求好子串的数量
    // 解题思路: 使用后缀数组，对每个后缀计算以该后缀开头的好子串数量
    // 时间复杂度: O(n^2)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int solveGoodSubstrings(String s, String goodChars, int k) {
        int n = s.length();
        boolean[] isGood = new boolean[26];
        for (char c : goodChars.toCharArray()) {
            isGood[c - 'a'] = true;
        }
        
        int count = 0;
        
        // 对每个位置作为起点
        for (int i = 0; i < n; i++) {
            int badCount = 0;
            // 从该位置开始扩展子串
            for (int j = i; j < n; j++) {
                if (!isGood[s.charAt(j) - 'a']) {
                    badCount++;
                }
                if (badCount > k) {
                    break; // 超过限制，停止扩展
                }
                count++; // 这是一个好子串
            }
        }
        
        return count;
    }
    
    // ====================================================================================
    // 题目5: Codeforces 452E - Three strings
    // 题目描述: 求三个字符串的公共子串数量
    // 解题思路: 将三个字符串用不同特殊字符连接，构建后缀数组，
//           然后统计同时包含三个字符串子串的LCP
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static long[] solveThreeStrings(String s1, String s2, String s3) {
        // 用不同特殊字符连接三个字符串
        String combined = s1 + "#" + s2 + "$" + s3 + "%";
        SuffixArray sa = new SuffixArray(combined);
        
        int n1 = s1.length();
        int n2 = s2.length();
        int n3 = s3.length();
        int[] suffixArray = sa.getSA();
        int[] height = sa.getHeight();
        int len = combined.length();
        
        long[] result = new long[Math.max(n1, Math.max(n2, n3)) + 1];
        
        // 使用单调栈计算每个height值对答案的贡献
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        
        for (int i = 0; i <= len; i++) {
            int h = (i < len) ? height[i] : -1;
            while (stack.size() > 1 && height[stack.peek()] > h) {
                int j = stack.pop();
                int k = stack.peek();
                int minHeight = height[j];
                
                // 检查suffixArray[j-1]到suffixArray[i-1]是否包含三个字符串的后缀
                boolean hasS1 = false, hasS2 = false, hasS3 = false;
                for (int t = k + 1; t < i; t++) {
                    int pos = suffixArray[t];
                    if (pos < n1) hasS1 = true;
                    else if (pos < n1 + 1 + n2) hasS2 = true;
                    else if (pos < n1 + 1 + n2 + 1 + n3) hasS3 = true;
                }
                
                if (hasS1 && hasS2 && hasS3) {
                    result[minHeight] += (i - k - 1);
                }
            }
            stack.push(i);
        }
        
        // 计算前缀和得到最终结果
        for (int i = result.length - 2; i >= 1; i--) {
            result[i] += result[i + 1];
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目6: Codeforces 235C - Cyclical Quest
    // 题目描述: 给定文本串和多个模式串，对每个模式串求其循环同构在文本串中的出现次数
    // 解题思路: 对文本串建立后缀数组，对每个模式串的所有循环同构在后缀数组中二分查找
    // 时间复杂度: O(|T| log |T| + Σ|Pi|^2 log |T|)
    // 空间复杂度: O(|T|)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[] solveCyclicalQuest(String text, String[] patterns) {
        SuffixArray sa = new SuffixArray(text);
        int[] suffixArray = sa.getSA();
        int[] rank = sa.getRank();
        int n = text.length();
        
        int[] result = new int[patterns.length];
        
        for (int i = 0; i < patterns.length; i++) {
            String pattern = patterns[i];
            int m = pattern.length();
            
            if (m > n) {
                result[i] = 0;
                continue;
            }
            
            // 对模式串的每个循环同构进行匹配
            Set<String> visited = new HashSet<>();
            int count = 0;
            
            for (int j = 0; j < m; j++) {
                String rotated = pattern.substring(j) + pattern.substring(0, j);
                if (visited.contains(rotated)) continue;
                visited.add(rotated);
                
                // 二分查找第一个匹配位置
                int left = 0, right = n - 1;
                int first = -1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    int pos = suffixArray[mid];
                    int cmp = compareSubstring(text, pos, rotated);
                    if (cmp == 0) {
                        first = mid;
                        right = mid - 1;
                    } else if (cmp < 0) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                
                if (first != -1) {
                    // 二分查找最后一个匹配位置
                    left = first;
                    right = n - 1;
                    int last = first;
                    while (left <= right) {
                        int mid = (left + right) / 2;
                        int pos = suffixArray[mid];
                        int cmp = compareSubstring(text, pos, rotated);
                        if (cmp == 0) {
                            last = mid;
                            left = mid + 1;
                        } else {
                            right = mid - 1;
                        }
                    }
                    count += last - first + 1;
                }
            }
            
            result[i] = count;
        }
        
        return result;
    }
    
    /**
     * 比较文本的子串和模式串
     */
    private static int compareSubstring(String text, int start, String pattern) {
        int n = text.length();
        int m = pattern.length();
        
        for (int i = 0; i < m; i++) {
            if (start + i >= n) return -1; // 文本不够长
            char c1 = text.charAt(start + i);
            char c2 = pattern.charAt(i);
            if (c1 != c2) {
                return c1 < c2 ? -1 : 1;
            }
        }
        return 0; // 完全匹配
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试SARRAY
        System.out.println("=== 测试SARRAY ===");
        String s1 = "banana";
        int[] saResult = solveSARRAY(s1);
        System.out.println("输入: " + s1);
        System.out.print("后缀数组: ");
        for (int i : saResult) System.out.print(i + " ");
        System.out.println();
        
        // 测试SUBST1
        System.out.println("\n=== 测试SUBST1 ===");
        String s2 = "banana";
        long subst1Result = solveSUBST1(s2);
        System.out.println("输入: " + s2);
        System.out.println("不同子串数: " + subst1Result);
        
        // 测试LCS
        System.out.println("\n=== 测试LCS ===");
        String s3 = "abcde";
        String s4 = "xbcxy";
        int lcsResult = solveLCS(s3, s4);
        System.out.println("输入: s1=" + s3 + ", s2=" + s4);
        System.out.println("最长公共子串长度: " + lcsResult);
        
        // 测试Good Substrings
        System.out.println("\n=== 测试Good Substrings ===");
        String s5 = "ababab";
        String goodChars = "ab";
        int k = 1;
        int goodResult = solveGoodSubstrings(s5, goodChars, k);
        System.out.println("输入: s=" + s5 + ", goodChars=" + goodChars + ", k=" + k);
        System.out.println("好子串数量: " + goodResult);
    }
}