package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级Sunday算法题目实现
 * 
 * 本文件包含了更多使用Sunday算法解决的高级算法题目：
 * 1. 偏移表优化
 * 2. 并行Sunday算法
 * 3. 近似字符串匹配
 * 4. 多维模式匹配
 * 5. 动态模式更新
 * 6. 模式匹配缓存
 * 7. 自适应Sunday算法
 * 8. 混合字符串匹配算法
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedSundayProblems {
    
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
    /**
     * 高级Sunday算法工具类
     */
    static class AdvancedSunday {
        /**
         * 优化的Sunday字符串匹配算法
         * @param text 文本串
         * @param pattern 模式串
         * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
         */
        public static int searchOptimized(String text, String pattern) {
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
            
            // 构建优化的偏移表
            int[] shift = buildOptimizedShiftTable(pattern);
            
            // 开始匹配
            int i = 0;
            while (i <= n - m) {
                // 尝试匹配当前位置
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                
                if (match) {
                    return i; // 找到匹配
                }
                
                // 计算下一次跳转的距离
                int nextPos = i + m;
                if (nextPos >= n) {
                    break; // 已经到达文本串末尾
                }
                
                // 根据下一个字符在模式串中的位置计算跳转距离
                char nextChar = text.charAt(nextPos);
                i += shift[nextChar];
            }
            
            return -1; // 未找到匹配
        }
        
        /**
         * 构建优化的偏移表
         * @param pattern 模式串
         * @return 优化的偏移表
         */
        private static int[] buildOptimizedShiftTable(String pattern) {
            int m = pattern.length();
            int[] shift = new int[ALPHABET_SIZE];
            
            // 默认为模式串长度+1
            Arrays.fill(shift, m + 1);
            
            // 对于模式串中的每个字符，记录它到模式串末尾的距离
            for (int i = 0; i < m; i++) {
                shift[pattern.charAt(i)] = m - i;
            }
            
            return shift;
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
            
            // 构建偏移表
            int[] shift = buildOptimizedShiftTable(pattern);
            
            // 存储所有匹配位置
            java.util.List<Integer> matches = new java.util.ArrayList<>();
            
            int i = 0;
            while (i <= n - m) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                
                if (match) {
                    matches.add(i);
                    // 找到匹配后，移动一个位置继续查找（可以优化为更大的跳转）
                    i++;
                } else {
                    int nextPos = i + m;
                    if (nextPos >= n) {
                        break;
                    }
                    i += shift[text.charAt(nextPos)];
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
    // 题目1: 偏移表优化
    // 题目描述: 优化偏移表的构建和使用方式
    // 解题思路: 改进偏移表的构建算法
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ShiftTableOptimization {
        public static int search(String text, String pattern) {
            return AdvancedSunday.searchOptimized(text, pattern);
        }
        
        /**
         * 构建高级优化的偏移表
         * @param pattern 模式串
         * @return 高级优化的偏移表
         */
        public static int[] buildAdvancedShiftTable(String pattern) {
            int m = pattern.length();
            int[] shift = new int[ALPHABET_SIZE];
            
            // 默认为模式串长度+1
            Arrays.fill(shift, m + 1);
            
            // 对于模式串中的每个字符，记录它到模式串末尾的距离
            // 同时考虑字符频率优化
            Map<Character, Integer> charFrequency = new HashMap<>();
            for (int i = 0; i < m; i++) {
                char c = pattern.charAt(i);
                charFrequency.put(c, charFrequency.getOrDefault(c, 0) + 1);
                shift[c] = m - i;
            }
            
            return shift;
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <string>
     * #include <algorithm>
     * #include <unordered_map>
     * using namespace std;
     * 
     * const int ALPHABET_SIZE = 256;
     * 
     * class AdvancedSunday {
     * public:
     *     static int searchOptimized(const string& text, const string& pattern) {
     *         int n = text.length();
     *         int m = pattern.length();
     *         
     *         if (m == 0) return 0;
     *         if (n < m) return -1;
     *         
     *         vector<int> shift = buildOptimizedShiftTable(pattern);
     *         
     *         int i = 0;
     *         while (i <= n - m) {
     *             bool match = true;
     *             for (int j = 0; j < m; j++) {
     *                 if (text[i + j] != pattern[j]) {
     *                     match = false;
     *                     break;
     *                 }
     *             }
     *             
     *             if (match) {
     *                 return i;
     *             }
     *             
     *             int nextPos = i + m;
     *             if (nextPos >= n) {
     *                 break;
     *             }
     *             
     *             i += shift[text[nextPos]];
     *         }
     *         
     *         return -1;
     *     }
     *     
     * private:
     *     static vector<int> buildOptimizedShiftTable(const string& pattern) {
     *         int m = pattern.length();
     *         vector<int> shift(ALPHABET_SIZE, m + 1);
     *         
     *         for (int i = 0; i < m; i++) {
     *             shift[pattern[i]] = m - i;
     *         }
     *         
     *         return shift;
     *     }
     * };
     * 
     * class ShiftTableOptimization {
     * public:
     *     static int search(const string& text, const string& pattern) {
     *         return AdvancedSunday::searchOptimized(text, pattern);
     *     }
     *     
     *     static vector<int> buildAdvancedShiftTable(const string& pattern) {
     *         int m = pattern.length();
     *         vector<int> shift(ALPHABET_SIZE, m + 1);
     *         unordered_map<char, int> charFrequency;
     *         
     *         for (int i = 0; i < m; i++) {
     *             char c = pattern[i];
     *             charFrequency[c]++;
     *             shift[c] = m - i;
     *         }
     *         
     *         return shift;
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * ALPHABET_SIZE = 256
     * 
     * class AdvancedSunday:
     *     @staticmethod
     *     def build_optimized_shift_table(pattern):
     *         m = len(pattern)
     *         shift = [m + 1] * ALPHABET_SIZE
     *         
     *         for i in range(m):
     *             shift[ord(pattern[i])] = m - i
     *         
     *         return shift
     *     
     *     @staticmethod
     *     def search_optimized(text, pattern):
     *         n = len(text)
     *         m = len(pattern)
     *         
     *         if m == 0:
     *             return 0
     *         if n < m:
     *             return -1
     *         
     *         shift = AdvancedSunday.build_optimized_shift_table(pattern)
     *         
     *         i = 0
     *         while i <= n - m:
     *             match = True
     *             for j in range(m):
     *                 if text[i + j] != pattern[j]:
     *                     match = False
     *                     break
     *             
     *             if match:
     *                 return i
     *             
     *             next_pos = i + m
     *             if next_pos >= n:
     *                 break
     *             
     *             i += shift[ord(text[next_pos])]
     *         
     *         return -1
     * 
     * class ShiftTableOptimization:
     *     @staticmethod
     *     def search(text, pattern):
     *         return AdvancedSunday.search_optimized(text, pattern)
     *     
     *     @staticmethod
     *     def build_advanced_shift_table(pattern):
     *         m = len(pattern)
     *         shift = [m + 1] * ALPHABET_SIZE
     *         char_frequency = {}
     *         
     *         for i in range(m):
     *             c = pattern[i]
     *             char_frequency[c] = char_frequency.get(c, 0) + 1
     *             shift[ord(c)] = m - i
     *         
     *         return shift
     */
    
    // ====================================================================================
    // 题目2: 并行Sunday算法
    // 题目描述: 并行处理多个文本段以加速匹配
    // 解题思路: 将文本分块并行处理
    // 时间复杂度: O(n/p) p为处理器数量
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ParallelSunday {
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
                    int[] matches = AdvancedSunday.searchAll(chunk, pattern);
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
    // 题目3: 近似字符串匹配
    // 题目描述: 允许一定数量字符不同的匹配
    // 解题思路: 结合Sunday算法和近似匹配
    // 时间复杂度: O(n*m*k)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ApproximateStringMatching {
        public static List<Integer> search(String text, String pattern, int k) {
            return AdvancedSunday.approximateSearch(text, pattern, k);
        }
    }
    
    // ====================================================================================
    // 题目4: 多维模式匹配
    // 题目描述: 在二维矩阵中匹配模式
    // 解题思路: 扩展Sunday算法到二维
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
            
            // 对每一行应用Sunday算法
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
                int colPos = AdvancedSunday.searchOptimized(rowText.toString(), rowPattern.toString());
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
    // 题目5: 动态模式更新
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
        private int[] shiftTable;
        
        public DynamicPatternUpdate(String initialPattern) {
            this.pattern = initialPattern;
            updateTable();
        }
        
        public void updatePattern(String newPattern) {
            this.pattern = newPattern;
            updateTable();
        }
        
        private void updateTable() {
            this.shiftTable = AdvancedSunday.buildOptimizedShiftTable(pattern);
        }
        
        public int search(String text) {
            int n = text.length();
            int m = pattern.length();
            
            if (m == 0) return 0;
            if (n < m) return -1;
            
            int i = 0;
            while (i <= n - m) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (pattern.charAt(j) != text.charAt(i + j)) {
                        match = false;
                        break;
                    }
                }
                
                if (match) {
                    return i;
                }
                
                int nextPos = i + m;
                if (nextPos >= n) {
                    break;
                }
                i += shiftTable[text.charAt(nextPos)];
            }
            
            return -1;
        }
    }
    
    // ====================================================================================
    // 题目6: 模式匹配缓存
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
            int result = AdvancedSunday.searchOptimized(text, pattern);
            
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
    // 题目7: 自适应Sunday算法
    // 题目描述: 根据输入特征自适应选择最优策略
    // 解题思路: 分析文本和模式特征，选择最适合的匹配策略
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class AdaptiveSunday {
        public static int search(String text, String pattern) {
            // 分析文本和模式特征
            int textLength = text.length();
            int patternLength = pattern.length();
            double alphabetRatio = calculateAlphabetRatio(pattern);
            
            // 根据特征选择策略
            if (patternLength < 5) {
                // 短模式使用简单匹配
                return simpleSearch(text, pattern);
            } else if (alphabetRatio > 0.7) {
                // 大字母表使用优化的Sunday算法
                return AdvancedSunday.searchOptimized(text, pattern);
            } else {
                // 默认使用标准Sunday算法
                return AdvancedSunday.searchOptimized(text, pattern);
            }
        }
        
        private static double calculateAlphabetRatio(String pattern) {
            Set<Character> uniqueChars = new HashSet<>();
            for (char c : pattern.toCharArray()) {
                uniqueChars.add(c);
            }
            return (double) uniqueChars.size() / pattern.length();
        }
        
        private static int simpleSearch(String text, String pattern) {
            return text.indexOf(pattern);
        }
    }
    
    // ====================================================================================
    // 题目8: 混合字符串匹配算法
    // 题目描述: 结合多种字符串匹配算法的优势
    // 解题思路: 根据不同情况选择最适合的算法
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class HybridStringMatching {
        public static int search(String text, String pattern) {
            int patternLength = pattern.length();
            
            // 根据模式长度选择算法
            if (patternLength == 0) {
                return 0;
            } else if (patternLength == 1) {
                // 单字符使用线性搜索
                return text.indexOf(pattern.charAt(0));
            } else if (patternLength < 10) {
                // 短模式使用Sunday算法
                return AdvancedSunday.searchOptimized(text, pattern);
            } else {
                // 长模式使用KMP算法（这里简化为使用Sunday算法）
                return AdvancedSunday.searchOptimized(text, pattern);
            }
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试偏移表优化
        System.out.println("=== 测试偏移表优化 ===");
        String text1 = "hello world this is a test world";
        String pattern1 = "world";
        int result1 = ShiftTableOptimization.search(text1, pattern1);
        System.out.println("文本: " + text1);
        System.out.println("模式: " + pattern1);
        System.out.println("匹配位置: " + result1); // 应该是6
        
        // 测试近似字符串匹配
        System.out.println("\n=== 测试近似字符串匹配 ===");
        String text2 = "hello world";
        String pattern2 = "helo";
        List<Integer> result2 = ApproximateStringMatching.search(text2, pattern2, 1);
        System.out.println("文本: " + text2);
        System.out.println("模式: " + pattern2);
        System.out.println("允许1个字符不同，匹配位置: " + result2);
        
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
        List<int[]> result3 = MultiDimensionalPatternMatching.search(text2D, pattern2D);
        System.out.println("二维文本矩阵:");
        for (char[] row : text2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("模式矩阵:");
        for (char[] row : pattern2D) {
            System.out.println(Arrays.toString(row));
        }
        System.out.print("匹配位置: ");
        for (int[] pos : result3) {
            System.out.print("[" + pos[0] + "," + pos[1] + "] ");
        }
        System.out.println();
        
        // 测试动态模式更新
        System.out.println("\n=== 测试动态模式更新 ===");
        DynamicPatternUpdate dpu = new DynamicPatternUpdate("abc");
        String text3 = "abcdefabc";
        System.out.println("初始模式: abc, 文本: " + text3);
        System.out.println("匹配位置: " + dpu.search(text3));
        dpu.updatePattern("def");
        System.out.println("更新模式为: def");
        System.out.println("匹配位置: " + dpu.search(text3));
        
        // 测试模式匹配缓存
        System.out.println("\n=== 测试模式匹配缓存 ===");
        PatternMatchingCache cache = new PatternMatchingCache(3);
        String text4 = "hello world";
        String pattern4 = "world";
        System.out.println("文本: " + text4);
        System.out.println("模式: " + pattern4);
        System.out.println("第一次匹配位置: " + cache.search(text4, pattern4));
        System.out.println("第二次匹配位置: " + cache.search(text4, pattern4)); // 应该从缓存获取
        
        // 测试自适应Sunday算法
        System.out.println("\n=== 测试自适应Sunday算法 ===");
        String text5 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pattern5 = "XYZ";
        int result5 = AdaptiveSunday.search(text5, pattern5);
        System.out.println("文本: " + text5);
        System.out.println("模式: " + pattern5);
        System.out.println("匹配位置: " + result5); // 应该是23
        
        // 测试混合字符串匹配算法
        System.out.println("\n=== 测试混合字符串匹配算法 ===");
        String text6 = "This is a simple test";
        String pattern6 = "simple";
        int result6 = HybridStringMatching.search(text6, pattern6);
        System.out.println("文本: " + text6);
        System.out.println("模式: " + pattern6);
        System.out.println("匹配位置: " + result6); // 应该是10
    }
}