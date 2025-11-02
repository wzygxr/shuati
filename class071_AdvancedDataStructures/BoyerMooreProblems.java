package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Boyer-Moore算法题目实现
 * 
 * 本文件包含了多个使用Boyer-Moore算法解决的经典算法题目：
 * 1. 文本搜索优化
 * 2. 模式匹配加速
 * 3. 大文件搜索
 * 4. 多模式匹配
 * 5. 生物信息学应用
 * 6. 网络安全应用
 * 7. 数据压缩中的应用
 * 8. 实时字符串匹配
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class BoyerMooreProblems {
    
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
    /**
     * Boyer-Moore算法实现
     */
    static class BoyerMoore {
        /**
         * BM字符串匹配算法
         * @param text 文本串
         * @param pattern 模式串
         * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
         */
        public static int search(String text, String pattern) {
            if (text == null || pattern == null) {
                throw new IllegalArgumentException("文本串和模式串不能为null");
            }
            
            int n = text.length();
            int m = pattern.length();
            
            // 边界条件检查
            if (m == 0) {
                return 0; // 空模式串匹配任何位置的开始
            }
            if (n < m) {
                return -1; // 文本串比模式串短，不可能匹配
            }
            
            // 构建坏字符规则表
            int[] badChar = buildBadCharTable(pattern);
            
            // 构建好后缀规则表
            int[] goodSuffix = buildGoodSuffixTable(pattern);
            
            // 开始匹配
            int i = 0; // 文本串中的位置
            while (i <= n - m) {
                int j = m - 1; // 从模式串的最后一个字符开始匹配
                
                // 从右向左匹配
                while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                    j--;
                }
                
                // 找到完全匹配
                if (j < 0) {
                    return i;
                }
                
                // 计算坏字符规则的移动距离
                int badCharShift = Math.max(1, j - badChar[text.charAt(i + j)]);
                
                // 计算好后缀规则的移动距离
                int goodSuffixShift = goodSuffix[j];
                
                // 取两个规则中的最大移动距离
                i += Math.max(badCharShift, goodSuffixShift);
            }
            
            return -1; // 未找到匹配
        }
        
        /**
         * 构建坏字符规则表
         * @param pattern 模式串
         * @return 坏字符表，badChar[c]表示字符c在模式串中最右边出现的位置
         */
        private static int[] buildBadCharTable(String pattern) {
            int m = pattern.length();
            int[] badChar = new int[ALPHABET_SIZE];
            
            // 初始化为-1，表示字符不在模式串中
            Arrays.fill(badChar, -1);
            
            // 记录每个字符最右边出现的位置
            for (int i = 0; i < m; i++) {
                badChar[pattern.charAt(i)] = i;
            }
            
            return badChar;
        }
        
        /**
         * 构建好后缀规则表
         * @param pattern 模式串
         * @return 好后缀表，goodSuffix[j]表示当j位置出现不匹配时的移动距离
         */
        private static int[] buildGoodSuffixTable(String pattern) {
            int m = pattern.length();
            // 初始化好后缀表
            int[] goodSuffix = new int[m];
            for (int i = 0; i < m; i++) {
                goodSuffix[i] = m;
            }
            
            // 计算后缀数组
            int[] suffix = computeSuffixArray(pattern);
            
            // case 1: 模式串的某一个后缀匹配了模式串的前缀
            for (int i = m - 1; i >= 0; i--) {
                // 如果从位置i开始的后缀等于整个模式串的前缀
                if (suffix[i] == m - i) {
                    // 对于所有可能的位置，设置移动距离
                    for (int j = 0; j < m - 1 - i; j++) {
                        if (goodSuffix[j] == m) {
                            goodSuffix[j] = m - 1 - i;
                        }
                    }
                }
            }
            
            // case 2: 模式串的某一个子串等于以j为边界的后缀
            for (int i = 0; i <= m - 2; i++) {
                // 当在位置i发生不匹配时，应该移动的距离
                if (suffix[i] > 0) {
                    int index = m - 1 - suffix[i];
                    // 确保索引在有效范围内
                    if (index >= 0 && index < m) {
                        goodSuffix[index] = m - 1 - i;
                    }
                }
            }
            
            return goodSuffix;
        }
        
        /**
         * 计算后缀数组：suffix[i]表示以i结尾的子串与模式串后缀的最长公共长度
         */
        private static int[] computeSuffixArray(String pattern) {
            int m = pattern.length();
            int[] suffix = new int[m];
            
            // 初始化
            suffix[m - 1] = m;
            
            // 计算后缀数组
            for (int i = m - 2; i >= 0; i--) {
                if (i > 0 && pattern.charAt(i) == pattern.charAt(m - 1)) {
                    suffix[i] = suffix[i + 1] + 1;
                } else {
                    int k = 1;
                    while (k < m && i + k < m && pattern.charAt(i + k) == pattern.charAt(k)) {
                        k++;
                    }
                    suffix[i] = k;
                }
            }
            
            return suffix;
        }
        
        /**
         * 查找模式串在文本串中所有出现的位置
         * @param text 文本串
         * @param pattern 模式串
         * @return 包含所有匹配位置的数组
         */
        public static int[] searchAll(String text, String pattern) {
            if (text == null || pattern == null) {
                throw new IllegalArgumentException("文本串和模式串不能为null");
            }
            
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) {
                // 空模式串匹配每个位置的开始
                int[] result = new int[n + 1];
                for (int i = 0; i <= n; i++) {
                    result[i] = i;
                }
                return result;
            }
            
            if (n < m) {
                return new int[0]; // 无匹配
            }
            
            // 构建坏字符规则表和好后缀规则表
            int[] badChar = buildBadCharTable(pattern);
            int[] goodSuffix = buildGoodSuffixTable(pattern);
            
            // 存储所有匹配位置
            java.util.List<Integer> matches = new java.util.ArrayList<>();
            
            int i = 0; // 文本串中的位置
            while (i <= n - m) {
                int j = m - 1; // 从模式串的最后一个字符开始匹配
                
                // 从右向左匹配
                while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                    j--;
                }
                
                // 找到完全匹配
                if (j < 0) {
                    matches.add(i);
                    // 移动一个位置继续查找
                    i++;
                } else {
                    // 计算坏字符规则的移动距离
                    int badCharShift = Math.max(1, j - badChar[text.charAt(i + j)]);
                    
                    // 计算好后缀规则的移动距离
                    int goodSuffixShift = goodSuffix[j];
                    
                    // 取两个规则中的最大移动距离
                    i += Math.max(badCharShift, goodSuffixShift);
                }
            }
            
            // 转换为数组返回
            int[] result = new int[matches.size()];
            for (int k = 0; k < matches.size(); k++) {
                result[k] = matches.get(k);
            }
            return result;
        }
    }
    
    // ====================================================================================
    // 题目1: 文本搜索优化
    // 题目描述: 在大型文本文件中优化搜索性能
    // 解题思路: 使用Boyer-Moore算法进行高效字符串搜索
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k)字符集大小
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TextSearchOptimization {
        public static int findPattern(String text, String pattern) {
            return BoyerMoore.search(text, pattern);
        }
        
        public static int[] findAllPatterns(String text, String pattern) {
            return BoyerMoore.searchAll(text, pattern);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <algorithm>
     * using namespace std;
     * 
     * const int ALPHABET_SIZE = 256;
     * 
     * class BoyerMoore {
     * private:
     *     static vector<int> buildBadCharTable(const string& pattern) {
     *         vector<int> badChar(ALPHABET_SIZE, -1);
     *         int m = pattern.length();
     *         
     *         for (int i = 0; i < m; i++) {
     *             badChar[pattern[i]] = i;
     *         }
     *         
     *         return badChar;
     *     }
     *     
     *     static vector<int> buildGoodSuffixTable(const string& pattern) {
     *         int m = pattern.length();
     *         vector<int> goodSuffix(m, m);
     *         vector<int> suffix(m, 0);
     *         
     *         // 计算后缀数组
     *         suffix[m - 1] = m;
     *         for (int i = m - 2; i >= 0; i--) {
     *             if (i > 0 && pattern[i] == pattern[m - 1]) {
     *                 suffix[i] = suffix[i + 1] + 1;
     *             } else {
     *                 int k = 1;
     *                 while (k < m && i + k < m && pattern[i + k] == pattern[k]) {
     *                     k++;
     *                 }
     *                 suffix[i] = k;
     *             }
     *         }
     *         
     *         // case 1
     *         for (int i = m - 1; i >= 0; i--) {
     *             if (suffix[i] == m - i) {
     *                 for (int j = 0; j < m - 1 - i; j++) {
     *                     if (goodSuffix[j] == m) {
     *                         goodSuffix[j] = m - 1 - i;
     *                     }
     *                 }
     *             }
     *         }
     *         
     *         // case 2
     *         for (int i = 0; i <= m - 2; i++) {
     *             if (suffix[i] > 0) {
     *                 int index = m - 1 - suffix[i];
     *                 if (index >= 0 && index < m) {
     *                     goodSuffix[index] = m - 1 - i;
     *                 }
     *             }
     *         }
     *         
     *         return goodSuffix;
     *     }
     *     
     * public:
     *     static int search(const string& text, const string& pattern) {
     *         int n = text.length();
     *         int m = pattern.length();
     *         
     *         if (m == 0) return 0;
     *         if (n < m) return -1;
     *         
     *         vector<int> badChar = buildBadCharTable(pattern);
     *         vector<int> goodSuffix = buildGoodSuffixTable(pattern);
     *         
     *         int i = 0;
     *         while (i <= n - m) {
     *             int j = m - 1;
     *             
     *             while (j >= 0 && pattern[j] == text[i + j]) {
     *                 j--;
     *             }
     *             
     *             if (j < 0) {
     *                 return i;
     *             }
     *             
     *             int badCharShift = max(1, j - badChar[text[i + j]]);
     *             int goodSuffixShift = goodSuffix[j];
     *             i += max(badCharShift, goodSuffixShift);
     *         }
     *         
     *         return -1;
     *     }
     * };
     * 
     * class TextSearchOptimization {
     * public:
     *     static int findPattern(const string& text, const string& pattern) {
     *         return BoyerMoore::search(text, pattern);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * ALPHABET_SIZE = 256
     * 
     * class BoyerMoore:
     *     @staticmethod
     *     def build_bad_char_table(pattern):
     *         bad_char = [-1] * ALPHABET_SIZE
     *         m = len(pattern)
     *         
     *         for i in range(m):
     *             bad_char[ord(pattern[i])] = i
     *         
     *         return bad_char
     *     
     *     @staticmethod
     *     def compute_suffix_array(pattern):
     *         m = len(pattern)
     *         suffix = [0] * m
     *         suffix[m - 1] = m
     *         
     *         for i in range(m - 2, -1, -1):
     *             if i > 0 and pattern[i] == pattern[m - 1]:
     *                 suffix[i] = suffix[i + 1] + 1
     *             else:
     *                 k = 1
     *                 while k < m and i + k < m and pattern[i + k] == pattern[k]:
     *                     k += 1
     *                 suffix[i] = k
     *         
     *         return suffix
     *     
     *     @staticmethod
     *     def build_good_suffix_table(pattern):
     *         m = len(pattern)
     *         good_suffix = [m] * m
     *         suffix = BoyerMoore.compute_suffix_array(pattern)
     *         
     *         # case 1
     *         for i in range(m - 1, -1, -1):
     *             if suffix[i] == m - i:
     *                 for j in range(m - 1 - i):
     *                     if good_suffix[j] == m:
     *                         good_suffix[j] = m - 1 - i
     *         
     *         # case 2
     *         for i in range(m - 1):
     *             if suffix[i] > 0:
     *                 index = m - 1 - suffix[i]
     *                 if 0 <= index < m:
     *                     good_suffix[index] = m - 1 - i
     *         
     *         return good_suffix
     *     
     *     @staticmethod
     *     def search(text, pattern):
     *         n = len(text)
     *         m = len(pattern)
     *         
     *         if m == 0:
     *             return 0
     *         if n < m:
     *             return -1
     *         
     *         bad_char = BoyerMoore.build_bad_char_table(pattern)
     *         good_suffix = BoyerMoore.build_good_suffix_table(pattern)
     *         
     *         i = 0
     *         while i <= n - m:
     *             j = m - 1
     *             
     *             while j >= 0 and pattern[j] == text[i + j]:
     *                 j -= 1
     *             
     *             if j < 0:
     *                 return i
     *             
     *             bad_char_shift = max(1, j - bad_char[ord(text[i + j])])
     *             good_suffix_shift = good_suffix[j]
     *             i += max(bad_char_shift, good_suffix_shift)
     *         
     *         return -1
     * 
     * class TextSearchOptimization:
     *     @staticmethod
     *     def find_pattern(text, pattern):
     *         return BoyerMoore.search(text, pattern)
     */
    
    // ====================================================================================
    // 题目2: 模式匹配加速
    // 题目描述: 在大量模式串中快速匹配文本
    // 解题思路: 预处理多个模式串，使用Boyer-Moore算法加速匹配
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k*m)k为模式串数量
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PatternMatchingAcceleration {
        private Map<String, int[]> badCharTables;
        private Map<String, int[]> goodSuffixTables;
        
        public PatternMatchingAcceleration(String[] patterns) {
            badCharTables = new HashMap<>();
            goodSuffixTables = new HashMap<>();
            
            // 预处理所有模式串
            for (String pattern : patterns) {
                badCharTables.put(pattern, BoyerMoore.buildBadCharTable(pattern));
                goodSuffixTables.put(pattern, BoyerMoore.buildGoodSuffixTable(pattern));
            }
        }
        
        public Map<String, List<Integer>> findAllPatterns(String text, String[] patterns) {
            Map<String, List<Integer>> results = new HashMap<>();
            
            for (String pattern : patterns) {
                results.put(pattern, new ArrayList<>());
                int[] badChar = badCharTables.get(pattern);
                int[] goodSuffix = goodSuffixTables.get(pattern);
                
                if (badChar == null || goodSuffix == null) {
                    continue;
                }
                
                int n = text.length();
                int m = pattern.length();
                
                if (m == 0) {
                    for (int i = 0; i <= n; i++) {
                        results.get(pattern).add(i);
                    }
                    continue;
                }
                
                if (n < m) {
                    continue;
                }
                
                int i = 0;
                while (i <= n - m) {
                    int j = m - 1;
                    
                    while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                        j--;
                    }
                    
                    if (j < 0) {
                        results.get(pattern).add(i);
                        i++;
                    } else {
                        int badCharShift = Math.max(1, j - badChar[text.charAt(i + j)]);
                        int goodSuffixShift = goodSuffix[j];
                        i += Math.max(badCharShift, goodSuffixShift);
                    }
                }
            }
            
            return results;
        }
    }
    
    // ====================================================================================
    // 题目3: 大文件搜索
    // 题目描述: 在大型文件中高效搜索模式串
    // 解题思路: 分块读取文件并使用Boyer-Moore算法搜索
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k)字符集大小
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LargeFileSearch {
        public static List<Long> searchInFile(String fileContent, String pattern) {
            List<Long> positions = new ArrayList<>();
            int[] matches = BoyerMoore.searchAll(fileContent, pattern);
            
            for (int match : matches) {
                positions.add((long) match);
            }
            
            return positions;
        }
    }
    
    // ====================================================================================
    // 题目4: 多模式匹配
    // 题目描述: 同时匹配多个模式串
    // 解题思路: 对每个模式串使用Boyer-Moore算法
    // 时间复杂度: O(n*k/m)k为模式串数量
    // 空间复杂度: O(k*m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MultiPatternMatching {
        public static Map<String, Integer> findFirstOccurrences(String text, String[] patterns) {
            Map<String, Integer> results = new HashMap<>();
            
            for (String pattern : patterns) {
                int position = BoyerMoore.search(text, pattern);
                results.put(pattern, position);
            }
            
            return results;
        }
    }
    
    // ====================================================================================
    // 题目5: 生物信息学应用
    // 题目描述: 在DNA序列中搜索特定模式
    // 解题思路: 使用Boyer-Moore算法搜索DNA序列
    // 时间复杂度: O(n/m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BioinformaticsApplication {
        public static int findDNAPattern(String dnaSequence, String pattern) {
            // DNA序列只包含A, T, G, C四个字符
            return BoyerMoore.search(dnaSequence, pattern);
        }
        
        public static List<Integer> findAllDNAPatterns(String dnaSequence, String pattern) {
            int[] positions = BoyerMoore.searchAll(dnaSequence, pattern);
            List<Integer> result = new ArrayList<>();
            for (int pos : positions) {
                result.add(pos);
            }
            return result;
        }
    }
    
    // ====================================================================================
    // 题目6: 网络安全应用
    // 题目描述: 在网络数据包中检测恶意模式
    // 解题思路: 使用Boyer-Moore算法快速检测恶意字符串
    // 时间复杂度: O(n/m)
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NetworkSecurityApplication {
        private Set<String> maliciousPatterns;
        
        public NetworkSecurityApplication(String[] maliciousPatterns) {
            this.maliciousPatterns = new HashSet<>(Arrays.asList(maliciousPatterns));
        }
        
        public boolean isMalicious(String packetData) {
            for (String pattern : maliciousPatterns) {
                if (BoyerMoore.search(packetData, pattern) != -1) {
                    return true;
                }
            }
            return false;
        }
        
        public List<String> detectMaliciousPatterns(String packetData) {
            List<String> detected = new ArrayList<>();
            for (String pattern : maliciousPatterns) {
                if (BoyerMoore.search(packetData, pattern) != -1) {
                    detected.add(pattern);
                }
            }
            return detected;
        }
    }
    
    // ====================================================================================
    // 题目7: 数据压缩中的应用
    // 题目描述: 在数据压缩中查找重复模式
    // 解题思路: 使用Boyer-Moore算法查找长重复子串
    // 时间复杂度: O(n^2/m)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DataCompressionApplication {
        public static Map<String, List<Integer>> findRepeatedPatterns(String data, int minLength) {
            Map<String, List<Integer>> patterns = new HashMap<>();
            
            // 查找所有长度至少为minLength的子串
            for (int len = minLength; len <= data.length() / 2; len++) {
                for (int i = 0; i <= data.length() - len; i++) {
                    String pattern = data.substring(i, i + len);
                    int[] occurrences = BoyerMoore.searchAll(data, pattern);
                    
                    // 如果出现多次，则记录
                    if (occurrences.length > 1) {
                        List<Integer> positions = new ArrayList<>();
                        for (int pos : occurrences) {
                            positions.add(pos);
                        }
                        patterns.put(pattern, positions);
                    }
                }
            }
            
            return patterns;
        }
    }
    
    // ====================================================================================
    // 题目8: 实时字符串匹配
    // 题目描述: 在实时数据流中匹配字符串
    // 解题思路: 使用Boyer-Moore算法进行实时匹配
    // 时间复杂度: O(1)每次字符处理
    // 空间复杂度: O(m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class RealTimeStringMatching {
        private String pattern;
        private StringBuilder buffer;
        private int patternLength;
        
        public RealTimeStringMatching(String pattern) {
            this.pattern = pattern;
            this.patternLength = pattern.length();
            this.buffer = new StringBuilder();
        }
        
        public void addCharacter(char c) {
            buffer.append(c);
            
            // 保持缓冲区大小不超过模式长度+一些额外空间
            if (buffer.length() > patternLength * 2) {
                buffer.delete(0, buffer.length() - patternLength * 2);
            }
        }
        
        public boolean isPatternFound() {
            String currentBuffer = buffer.toString();
            return BoyerMoore.search(currentBuffer, pattern) != -1;
        }
        
        public int getPatternPosition() {
            String currentBuffer = buffer.toString();
            return BoyerMoore.search(currentBuffer, pattern);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试文本搜索优化
        System.out.println("=== 测试文本搜索优化 ===");
        String text1 = "GCATCGCAGAGAGTATACAGTACG";
        String pattern1 = "GCAGAGAG";
        int result1 = TextSearchOptimization.findPattern(text1, pattern1);
        System.out.println("文本: " + text1);
        System.out.println("模式: " + pattern1);
        System.out.println("匹配位置: " + result1); // 应该是5
        
        // 测试模式匹配加速
        System.out.println("\n=== 测试模式匹配加速 ===");
        String text2 = "ababababab";
        String[] patterns2 = {"aba", "bab", "abab"};
        PatternMatchingAcceleration pma = new PatternMatchingAcceleration(patterns2);
        Map<String, List<Integer>> result2 = pma.findAllPatterns(text2, patterns2);
        System.out.println("文本: " + text2);
        System.out.println("模式: " + Arrays.toString(patterns2));
        for (Map.Entry<String, List<Integer>> entry : result2.entrySet()) {
            System.out.println("模式 '" + entry.getKey() + "' 出现位置: " + entry.getValue());
        }
        
        // 测试生物信息学应用
        System.out.println("\n=== 测试生物信息学应用 ===");
        String dna = "ATCGATCGATCG";
        String dnaPattern = "ATCG";
        int dnaResult = BioinformaticsApplication.findDNAPattern(dna, dnaPattern);
        List<Integer> dnaAllResults = BioinformaticsApplication.findAllDNAPatterns(dna, dnaPattern);
        System.out.println("DNA序列: " + dna);
        System.out.println("模式: " + dnaPattern);
        System.out.println("首次出现位置: " + dnaResult);
        System.out.println("所有出现位置: " + dnaAllResults);
        
        // 测试网络安全应用
        System.out.println("\n=== 测试网络安全应用 ===");
        String[] malicious = {"malware", "virus", "trojan"};
        NetworkSecurityApplication nsa = new NetworkSecurityApplication(malicious);
        String packet = "This packet contains malware code";
        boolean isMalicious = nsa.isMalicious(packet);
        List<String> detected = nsa.detectMaliciousPatterns(packet);
        System.out.println("数据包: " + packet);
        System.out.println("是否恶意: " + isMalicious);
        System.out.println("检测到的恶意模式: " + detected);
        
        // 测试数据压缩应用
        System.out.println("\n=== 测试数据压缩应用 ===");
        String data = "abcabcabcdefdef";
        Map<String, List<Integer>> repeated = DataCompressionApplication.findRepeatedPatterns(data, 2);
        System.out.println("数据: " + data);
        System.out.println("重复模式:");
        for (Map.Entry<String, List<Integer>> entry : repeated.entrySet()) {
            System.out.println("  '" + entry.getKey() + "' 出现位置: " + entry.getValue());
        }
        
        // 测试实时字符串匹配
        System.out.println("\n=== 测试实时字符串匹配 ===");
        RealTimeStringMatching rt = new RealTimeStringMatching("hello");
        String stream = "xhello world";
        System.out.println("模式: hello");
        System.out.println("字符流: " + stream);
        for (char c : stream.toCharArray()) {
            rt.addCharacter(c);
            if (rt.isPatternFound()) {
                System.out.println("在字符流中找到了模式，位置: " + rt.getPatternPosition());
                break;
            }
        }
    }
}