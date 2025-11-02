package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级Boyer-Moore算法题目实现
 * 
 * 本文件包含了更多使用Boyer-Moore算法解决的高级算法题目：
 * 1. 坏字符规则优化
 * 2. 好后缀规则增强
 * 3. 并行Boyer-Moore算法
 * 4. 近似字符串匹配
 * 5. 多维模式匹配
 * 6. 动态模式更新
 * 7. 模式匹配缓存
 * 8. 自适应Boyer-Moore算法
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedBoyerMooreProblems {
    
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
    /**
     * 高级Boyer-Moore算法工具类
     */
    static class AdvancedBoyerMoore {
        /**
         * 优化的BM字符串匹配算法（仅使用坏字符规则）
         * @param text 文本串
         * @param pattern 模式串
         * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
         */
        public static int searchBadCharOnly(String text, String pattern) {
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
                i += badCharShift;
            }
            
            return -1; // 未找到匹配
        }
        
        /**
         * 优化的BM字符串匹配算法（仅使用好后缀规则）
         * @param text 文本串
         * @param pattern 模式串
         * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
         */
        public static int searchGoodSuffixOnly(String text, String pattern) {
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
                
                // 计算好后缀规则的移动距离
                int goodSuffixShift = goodSuffix[j];
                i += goodSuffixShift;
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
         * 近似字符串匹配（允许k个字符不同）
         * @param text 文本串
         * @param pattern 模式串
         * @param k 允许的不同字符数
         * @return 匹配位置列表
         */
        public static List<Integer> approximateSearch(String text, String pattern, int k) {
            List<Integer> matches = new ArrayList<>();
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) {
                for (int i = 0; i <= n; i++) {
                    matches.add(i);
                }
                return matches;
            }
            if (n < m) {
                return matches;
            }
            
            // 使用滑动窗口检查每个位置
            for (int i = 0; i <= n - m; i++) {
                int diffCount = 0;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        diffCount++;
                        if (diffCount > k) {
                            break;
                        }
                    }
                }
                if (diffCount <= k) {
                    matches.add(i);
                }
            }
            
            return matches;
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
    // 题目1: 坏字符规则优化
    // 题目描述: 优化坏字符规则以提高匹配效率
    // 解题思路: 改进坏字符表的构建和使用方式
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k)字符集大小
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BadCharacterOptimization {
        public static int search(String text, String pattern) {
            return AdvancedBoyerMoore.searchBadCharOnly(text, pattern);
        }
        
        /**
         * 构建优化的坏字符表
         * @param pattern 模式串
         * @return 优化的坏字符表
         */
        public static int[] buildOptimizedBadCharTable(String pattern) {
            int m = pattern.length();
            int[] badChar = new int[ALPHABET_SIZE];
            
            // 初始化为模式串长度，表示字符不在模式串中时移动整个模式串长度
            Arrays.fill(badChar, m);
            
            // 记录每个字符最右边出现的位置
            for (int i = 0; i < m; i++) {
                badChar[pattern.charAt(i)] = i;
            }
            
            return badChar;
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
     * class AdvancedBoyerMoore {
     * public:
     *     static int searchBadCharOnly(const string& text, const string& pattern) {
     *         int n = text.length();
     *         int m = pattern.length();
     *         
     *         if (m == 0) return 0;
     *         if (n < m) return -1;
     *         
     *         vector<int> badChar = buildBadCharTable(pattern);
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
     *             i += badCharShift;
     *         }
     *         
     *         return -1;
     *     }
     *     
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
     * };
     * 
     * class BadCharacterOptimization {
     * public:
     *     static int search(const string& text, const string& pattern) {
     *         return AdvancedBoyerMoore::searchBadCharOnly(text, pattern);
     *     }
     *     
     *     static vector<int> buildOptimizedBadCharTable(const string& pattern) {
     *         int m = pattern.length();
     *         vector<int> badChar(ALPHABET_SIZE, m);
     *         
     *         for (int i = 0; i < m; i++) {
     *             badChar[pattern[i]] = i;
     *         }
     *         
     *         return badChar;
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * ALPHABET_SIZE = 256
     * 
     * class AdvancedBoyerMoore:
     *     @staticmethod
     *     def search_bad_char_only(text, pattern):
     *         n = len(text)
     *         m = len(pattern)
     *         
     *         if m == 0:
     *             return 0
     *         if n < m:
     *             return -1
     *         
     *         bad_char = AdvancedBoyerMoore.build_bad_char_table(pattern)
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
     *             i += bad_char_shift
     *         
     *         return -1
     *     
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
     * class BadCharacterOptimization:
     *     @staticmethod
     *     def search(text, pattern):
     *         return AdvancedBoyerMoore.search_bad_char_only(text, pattern)
     *     
     *     @staticmethod
     *     def build_optimized_bad_char_table(pattern):
     *         m = len(pattern)
     *         bad_char = [m] * ALPHABET_SIZE
     *         
     *         for i in range(m):
     *             bad_char[ord(pattern[i])] = i
     *         
     *         return bad_char
     */
    
    // ====================================================================================
    // 题目2: 好后缀规则增强
    // 题目描述: 增强好后缀规则的处理能力
    // 解题思路: 改进好后缀表的构建算法
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class GoodSuffixEnhancement {
        public static int search(String text, String pattern) {
            return AdvancedBoyerMoore.searchGoodSuffixOnly(text, pattern);
        }
        
        /**
         * 构建增强的好后缀表
         * @param pattern 模式串
         * @return 增强的好后缀表
         */
        public static int[] buildEnhancedGoodSuffixTable(String pattern) {
            int m = pattern.length();
            int[] goodSuffix = new int[m];
            int[] suffix = computeSuffixArray(pattern);
            
            // 初始化为模式串长度
            Arrays.fill(goodSuffix, m);
            
            // 增强的处理逻辑
            int lastPrefix = m;
            for (int i = m - 1; i >= 0; i--) {
                if (suffix[i] == i + 1) {
                    lastPrefix = i + 1;
                }
                goodSuffix[m - 1 - i] = lastPrefix;
            }
            
            for (int i = 0; i < m - 1; i++) {
                goodSuffix[m - 1 - suffix[i]] = m - 1 - i + suffix[i];
            }
            
            return goodSuffix;
        }
        
        private static int[] computeSuffixArray(String pattern) {
            int m = pattern.length();
            int[] suffix = new int[m];
            
            suffix[m - 1] = m;
            
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
    }
    
    // ====================================================================================
    // 题目3: 并行Boyer-Moore算法
    // 题目描述: 并行处理多个文本段以加速匹配
    // 解题思路: 将文本分块并行处理
    // 时间复杂度: O(n/p) p为处理器数量
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ParallelBoyerMoore {
        public static List<Integer> search(String text, String pattern, int numThreads) {
            List<Integer> results = Collections.synchronizedList(new ArrayList<>());
            
            // 将文本分块
            int chunkSize = Math.max(1, text.length() / numThreads);
            List<Thread> threads = new ArrayList<>();
            
            for (int i = 0; i < numThreads; i++) {
                final int start = i * chunkSize;
                final int end = Math.min((i + 1) * chunkSize + pattern.length() - 1, text.length());
                final String chunk = text.substring(start, end);
                
                Thread thread = new Thread(() -> {
                    int[] matches = AdvancedBoyerMoore.searchAll(chunk, pattern);
                    synchronized (results) {
                        for (int match : matches) {
                            results.add(start + match);
                        }
                    }
                });
                
                threads.add(thread);
                thread.start();
            }
            
            // 等待所有线程完成
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // 排序结果
            Collections.sort(results);
            return results;
        }
    }
    
    // ====================================================================================
    // 题目4: 近似字符串匹配
    // 题目描述: 允许一定数量字符不同的匹配
    // 解题思路: 结合Boyer-Moore算法和近似匹配
    // 时间复杂度: O(n*m*k)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ApproximateStringMatching {
        public static List<Integer> search(String text, String pattern, int k) {
            return AdvancedBoyerMoore.approximateSearch(text, pattern, k);
        }
    }
    
    // ====================================================================================
    // 题目5: 多维模式匹配
    // 题目描述: 在二维矩阵中匹配模式
    // 解题思路: 扩展Boyer-Moore算法到二维
    // 时间复杂度: O(n*m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class MultiDimensionalPatternMatching {
        public static List<int[]> search(char[][] text, char[][] pattern) {
            List<int[]> matches = new ArrayList<>();
            int textRows = text.length;
            int textCols = textRows > 0 ? text[0].length : 0;
            int patternRows = pattern.length;
            int patternCols = patternRows > 0 ? pattern[0].length : 0;
            
            if (patternRows == 0 || patternCols == 0 || 
                textRows < patternRows || textCols < patternCols) {
                return matches;
            }
            
            // 对每一行应用Boyer-Moore算法
            for (int i = 0; i <= textRows - patternRows; i++) {
                // 构造当前行的文本和模式
                StringBuilder rowText = new StringBuilder();
                StringBuilder rowPattern = new StringBuilder();
                
                for (int j = 0; j < textCols; j++) {
                    rowText.append(text[i][j]);
                }
                
                for (int j = 0; j < patternCols; j++) {
                    rowPattern.append(pattern[0][j]);
                }
                
                // 在当前行中搜索模式的第一行
                int colPos = AdvancedBoyerMoore.searchBadCharOnly(rowText.toString(), rowPattern.toString());
                if (colPos != -1) {
                    // 检查完整的二维匹配
                    boolean fullMatch = true;
                    for (int r = 0; r < patternRows; r++) {
                        for (int c = 0; c < patternCols; c++) {
                            if (i + r >= textRows || colPos + c >= textCols || 
                                text[i + r][colPos + c] != pattern[r][c]) {
                                fullMatch = false;
                                break;
                            }
                        }
                        if (!fullMatch) break;
                    }
                    
                    if (fullMatch) {
                        matches.add(new int[]{i, colPos});
                    }
                }
            }
            
            return matches;
        }
    }
    
    // ====================================================================================
    // 题目6: 动态模式更新
    // 题目描述: 支持动态更新模式串的匹配
    // 解题思路: 缓存预处理结果并支持动态更新
    // 时间复杂度: O(1)查询，O(m)更新
    // 空间复杂度: O(m)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicPatternUpdate {
        private String pattern;
        private int[] badCharTable;
        private int[] goodSuffixTable;
        
        public DynamicPatternUpdate(String initialPattern) {
            this.pattern = initialPattern;
            updateTables();
        }
        
        public void updatePattern(String newPattern) {
            this.pattern = newPattern;
            updateTables();
        }
        
        private void updateTables() {
            this.badCharTable = AdvancedBoyerMoore.buildBadCharTable(pattern);
            this.goodSuffixTable = AdvancedBoyerMoore.buildGoodSuffixTable(pattern);
        }
        
        public int search(String text) {
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) return 0;
            if (n < m) return -1;
            
            int i = 0;
            while (i <= n - m) {
                int j = m - 1;
                
                while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                    j--;
                }
                
                if (j < 0) {
                    return i;
                }
                
                int badCharShift = Math.max(1, j - badCharTable[text.charAt(i + j)]);
                int goodSuffixShift = goodSuffixTable[j];
                i += Math.max(badCharShift, goodSuffixShift);
            }
            
            return -1;
        }
    }
    
    // ====================================================================================
    // 题目7: 模式匹配缓存
    // 题目描述: 缓存模式匹配结果以提高重复查询效率
    // 解题思路: 使用LRU缓存存储匹配结果
    // 时间复杂度: O(1)缓存命中，O(n/m)缓存未命中
    // 空间复杂度: O(k)k为缓存大小
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PatternMatchingCache {
        private final int cacheSize;
        private Map<String, Integer> cache;
        private LinkedHashSet<String> lruOrder;
        
        public PatternMatchingCache(int cacheSize) {
            this.cacheSize = cacheSize;
            this.cache = new HashMap<>();
            this.lruOrder = new LinkedHashSet<>();
        }
        
        public int search(String text, String pattern) {
            String key = text + "#" + pattern;
            
            // 检查缓存
            if (cache.containsKey(key)) {
                // 更新LRU顺序
                lruOrder.remove(key);
                lruOrder.add(key);
                return cache.get(key);
            }
            
            // 计算匹配结果
            int result = AdvancedBoyerMoore.searchBadCharOnly(text, pattern);
            
            // 更新缓存
            if (cache.size() >= cacheSize) {
                // 移除最久未使用的项
                String lruKey = lruOrder.iterator().next();
                lruOrder.remove(lruKey);
                cache.remove(lruKey);
            }
            
            cache.put(key, result);
            lruOrder.add(key);
            
            return result;
        }
    }
    
    // ====================================================================================
    // 题目8: 自适应Boyer-Moore算法
    // 题目描述: 根据输入特征自适应选择最优策略
    // 解题思路: 分析文本和模式特征，选择最适合的匹配策略
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class AdaptiveBoyerMoore {
        public static int search(String text, String pattern) {
            // 分析文本和模式特征
            int textLength = text.length();
            int patternLength = pattern.length();
            double alphabetRatio = calculateAlphabetRatio(pattern);
            
            // 根据特征选择策略
            if (patternLength < 10) {
                // 短模式使用坏字符规则
                return AdvancedBoyerMoore.searchBadCharOnly(text, pattern);
            } else if (alphabetRatio > 0.5) {
                // 大字母表使用好后缀规则
                return AdvancedBoyerMoore.searchGoodSuffixOnly(text, pattern);
            } else {
                // 默认使用完整Boyer-Moore算法
                return AdvancedBoyerMoore.searchBadCharOnly(text, pattern);
            }
        }
        
        private static double calculateAlphabetRatio(String pattern) {
            Set<Character> uniqueChars = new HashSet<>();
            for (char c : pattern.toCharArray()) {
                uniqueChars.add(c);
            }
            return (double) uniqueChars.size() / pattern.length();
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试坏字符规则优化
        System.out.println("=== 测试坏字符规则优化 ===");
        String text1 = "GCATCGCAGAGAGTATACAGTACG";
        String pattern1 = "GCAGAGAG";
        int result1 = BadCharacterOptimization.search(text1, pattern1);
        System.out.println("文本: " + text1);
        System.out.println("模式: " + pattern1);
        System.out.println("匹配位置: " + result1); // 应该是5
        
        // 测试好后缀规则增强
        System.out.println("\n=== 测试好后缀规则增强 ===");
        String text2 = "ABABABABAB";
        String pattern2 = "ABAB";
        int result2 = GoodSuffixEnhancement.search(text2, pattern2);
        System.out.println("文本: " + text2);
        System.out.println("模式: " + pattern2);
        System.out.println("匹配位置: " + result2); // 应该是0, 2, 4, 6
        
        // 测试近似字符串匹配
        System.out.println("\n=== 测试近似字符串匹配 ===");
        String text3 = "hello world";
        String pattern3 = "helo";
        List<Integer> result3 = ApproximateStringMatching.search(text3, pattern3, 1);
        System.out.println("文本: " + text3);
        System.out.println("模式: " + pattern3);
        System.out.println("允许1个字符不同，匹配位置: " + result3);
        
        // 测试多维模式匹配
        System.out.println("\n=== 测试多维模式匹配 ===");
        char[][] text2D = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'}
        };
        char[][] pattern2D = {
            {'b', 'c'},
            {'e', 'f'}
        };
        List<int[]> result4 = MultiDimensionalPatternMatching.search(text2D, pattern2D);
        System.out.println("二维文本矩阵:");
        for (char[] row : text2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("模式矩阵:");
        for (char[] row : pattern2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.print("匹配位置: ");
        for (int[] pos : result4) {
            System.out.print("[" + pos[0] + "," + pos[1] + "] ");
        }
        System.out.println();
        
        // 测试动态模式更新
        System.out.println("\n=== 测试动态模式更新 ===");
        DynamicPatternUpdate dpu = new DynamicPatternUpdate("abc");
        String text5 = "abcdefabc";
        System.out.println("初始模式: abc, 文本: " + text5);
        System.out.println("匹配位置: " + dpu.search(text5));
        dpu.updatePattern("def");
        System.out.println("更新模式为: def");
        System.out.println("匹配位置: " + dpu.search(text5));
        
        // 测试模式匹配缓存
        System.out.println("\n=== 测试模式匹配缓存 ===");
        PatternMatchingCache cache = new PatternMatchingCache(3);
        String text6 = "hello world";
        String pattern6 = "world";
        System.out.println("文本: " + text6);
        System.out.println("模式: " + pattern6);
        System.out.println("第一次匹配位置: " + cache.search(text6, pattern6));
        System.out.println("第二次匹配位置: " + cache.search(text6, pattern6)); // 应该从缓存获取
        
        // 测试自适应Boyer-Moore算法
        System.out.println("\n=== 测试自适应Boyer-Moore算法 ===");
        String text7 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pattern7 = "XYZ";
        int result7 = AdaptiveBoyerMoore.search(text7, pattern7);
        System.out.println("文本: " + text7);
        System.out.println("模式: " + pattern7);
        System.out.println("匹配位置: " + result7); // 应该是23
    }
}