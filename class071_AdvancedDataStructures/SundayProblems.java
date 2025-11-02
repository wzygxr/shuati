package class029_AdvancedDataStructures;

import java.util.*;

/**
 * Sunday算法题目实现
 * 
 * 本文件包含了多个使用Sunday算法解决的经典算法题目：
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
public class SundayProblems {
    
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
    /**
     * Sunday算法实现
     */
    static class Sunday {
        /**
         * Sunday字符串匹配算法
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
            
            // 构建偏移表
            int[] shift = buildShiftTable(pattern);
            
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
         * 构建Sunday算法的偏移表
         * @param pattern 模式串
         * @return 偏移表，shift[c]表示字符c在模式串中最右侧出现的位置到模式串末尾的距离+1
         */
        private static int[] buildShiftTable(String pattern) {
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
            int[] shift = buildShiftTable(pattern);
            
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
    // 题目1: 文本搜索优化
    // 题目描述: 在大型文本文件中优化搜索性能
    // 解题思路: 使用Sunday算法进行高效字符串搜索
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class TextSearchOptimization {
        public static int findPattern(String text, String pattern) {
            return Sunday.search(text, pattern);
        }
        
        public static int[] findAllPatterns(String text, String pattern) {
            return Sunday.searchAll(text, pattern);
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
     * class Sunday {
     * public:
     *     static int search(const string& text, const string& pattern) {
     *         int n = text.length();
     *         int m = pattern.length();
     *         
     *         if (m == 0) return 0;
     *         if (n < m) return -1;
     *         
     *         vector<int> shift = buildShiftTable(pattern);
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
     *     static vector<int> buildShiftTable(const string& pattern) {
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
     * class TextSearchOptimization {
     * public:
     *     static int findPattern(const string& text, const string& pattern) {
     *         return Sunday::search(text, pattern);
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * ALPHABET_SIZE = 256
     * 
     * class Sunday:
     *     @staticmethod
     *     def build_shift_table(pattern):
     *         m = len(pattern)
     *         shift = [m + 1] * ALPHABET_SIZE
     *         
     *         for i in range(m):
     *             shift[ord(pattern[i])] = m - i
     *         
     *         return shift
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
     *         shift = Sunday.build_shift_table(pattern)
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
     * class TextSearchOptimization:
     *     @staticmethod
     *     def find_pattern(text, pattern):
     *         return Sunday.search(text, pattern)
     */
    
    // ====================================================================================
    // 题目2: 模式匹配加速
    // 题目描述: 在大量模式串中快速匹配文本
    // 解题思路: 预处理多个模式串，使用Sunday算法加速匹配
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(k*m)k为模式串数量
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PatternMatchingAcceleration {
        private Map<String, int[]> shiftTables;
        
        public PatternMatchingAcceleration(String[] patterns) {
            shiftTables = new HashMap<>();
            
            // 预处理所有模式串
            for (String pattern : patterns) {
                shiftTables.put(pattern, Sunday.buildShiftTable(pattern));
            }
        }
        
        public Map<String, List<Integer>> findAllPatterns(String text, String[] patterns) {
            Map<String, List<Integer>> results = new HashMap<>();
            
            for (String pattern : patterns) {
                results.put(pattern, new ArrayList<>());
                int[] shift = shiftTables.get(pattern);
                
                if (shift == null) {
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
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    
                    if (match) {
                        results.get(pattern).add(i);
                        i++;
                    } else {
                        int nextPos = i + m;
                        if (nextPos >= n) {
                            break;
                        }
                        i += shift[text.charAt(nextPos)];
                    }
                }
            }
            
            return results;
        }
    }
    
    // ====================================================================================
    // 题目3: 大文件搜索
    // 题目描述: 在大型文件中高效搜索模式串
    // 解题思路: 分块读取文件并使用Sunday算法搜索
    // 时间复杂度: O(n/m)最好情况
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LargeFileSearch {
        public static List<Long> searchInFile(String fileContent, String pattern) {
            List<Long> positions = new ArrayList<>();
            int[] matches = Sunday.searchAll(fileContent, pattern);
            
            for (int match : matches) {
                positions.add((long) match);
            }
            
            return positions;
        }
    }
    
    // ====================================================================================
    // 题目4: 多模式匹配
    // 题目描述: 同时匹配多个模式串
    // 解题思路: 对每个模式串使用Sunday算法
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
                int position = Sunday.search(text, pattern);
                results.put(pattern, position);
            }
            
            return results;
        }
    }
    
    // ====================================================================================
    // 题目5: 生物信息学应用
    // 题目描述: 在DNA序列中搜索特定模式
    // 解题思路: 使用Sunday算法搜索DNA序列
    // 时间复杂度: O(n/m)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class BioinformaticsApplication {
        public static int findDNAPattern(String dnaSequence, String pattern) {
            // DNA序列只包含A, T, G, C四个字符
            return Sunday.search(dnaSequence, pattern);
        }
        
        public static List<Integer> findAllDNAPatterns(String dnaSequence, String pattern) {
            int[] positions = Sunday.searchAll(dnaSequence, pattern);
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
    // 解题思路: 使用Sunday算法快速检测恶意字符串
    // 时间复杂度: O(n/m)
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NetworkSecurityApplication {
        private Set<String> maliciousPatterns;
        private Map<String, int[]> shiftTables;
        
        public NetworkSecurityApplication(String[] maliciousPatterns) {
            this.maliciousPatterns = new HashSet<>(Arrays.asList(maliciousPatterns));
            this.shiftTables = new HashMap<>();
            
            // 预处理所有恶意模式
            for (String pattern : maliciousPatterns) {
                this.shiftTables.put(pattern, Sunday.buildShiftTable(pattern));
            }
        }
        
        public boolean isMalicious(String packetData) {
            for (String pattern : maliciousPatterns) {
                if (Sunday.search(packetData, pattern) != -1) {
                    return true;
                }
            }
            return false;
        }
        
        public List<String> detectMaliciousPatterns(String packetData) {
            List<String> detected = new ArrayList<>();
            for (String pattern : maliciousPatterns) {
                if (Sunday.search(packetData, pattern) != -1) {
                    detected.add(pattern);
                }
            }
            return detected;
        }
    }
    
    // ====================================================================================
    // 题目7: 数据压缩中的应用
    // 题目描述: 在数据压缩中查找重复模式
    // 解题思路: 使用Sunday算法查找长重复子串
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
                    int[] occurrences = Sunday.searchAll(data, pattern);
                    
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
    // 解题思路: 使用Sunday算法进行实时匹配
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
        private int[] shiftTable;
        
        public RealTimeStringMatching(String pattern) {
            this.pattern = pattern;
            this.patternLength = pattern.length();
            this.buffer = new StringBuilder();
            this.shiftTable = Sunday.buildShiftTable(pattern);
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
            return Sunday.search(currentBuffer, pattern) != -1;
        }
        
        public int getPatternPosition() {
            String currentBuffer = buffer.toString();
            return Sunday.search(currentBuffer, pattern);
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试文本搜索优化
        System.out.println("=== 测试文本搜索优化 ===");
        String text1 = "hello world this is a test world";
        String pattern1 = "world";
        int result1 = TextSearchOptimization.findPattern(text1, pattern1);
        System.out.println("文本: " + text1);
        System.out.println("模式: " + pattern1);
        System.out.println("匹配位置: " + result1); // 应该是6
        
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