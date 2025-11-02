package class100;

import java.util.*;

/**
 * KMP算法高级应用与工程化实现 - Java版本
 * 
 * 本类包含KMP算法的高级应用场景和工程化实现，包括：
 * - 多模式匹配
 * - 字符串周期检测
 * - 最长回文子串
 * - 字符串压缩
 * - 生物信息学应用
 * - 文本编辑器实现
 * 
 * 每个应用都包含：
 * 1. 详细的应用场景描述
 * 2. 完整的算法实现
 * 3. 时间复杂度和空间复杂度分析
 * 4. 工程化考量（性能优化、内存管理、异常处理）
 * 5. 完整的测试用例
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2024-01-01
 */
public class Code07_AdvancedKMPApplications {

    /**
     * AC自动机实现 - 多模式匹配
     * 
     * 应用场景: 在文本中同时查找多个模式串的所有出现位置
     * 典型应用: 敏感词过滤、病毒检测、DNA序列分析
     * 
     * 算法原理:
     * 1. 构建Trie树存储所有模式串
     * 2. 为Trie树构建失败指针（类似KMP的next数组）
     * 3. 使用失败指针实现高效的多模式匹配
     * 
     * 时间复杂度: O(n + m + k)，其中n是文本长度，m是所有模式串总长度，k是匹配次数
     * 空间复杂度: O(m)，用于存储Trie树
     */
    public static class ACAutomaton {
        private ACNode root;
        
        public ACAutomaton() {
            this.root = new ACNode();
        }
        
        /**
         * 插入模式串到Trie树
         * 
         * @param pattern 模式串
         */
        public void insert(String pattern) {
            ACNode current = root;
            for (char c : pattern.toCharArray()) {
                int index = c - 'a';
                if (current.children[index] == null) {
                    current.children[index] = new ACNode();
                }
                current = current.children[index];
            }
            current.isEnd = true;
            current.pattern = pattern;
        }
        
        /**
         * 构建失败指针（BFS遍历）
         */
        public void buildFailure() {
            Queue<ACNode> queue = new LinkedList<>();
            
            // 第一层节点的失败指针指向root
            for (ACNode child : root.children) {
                if (child != null) {
                    child.fail = root;
                    queue.offer(child);
                }
            }
            
            // BFS构建失败指针
            while (!queue.isEmpty()) {
                ACNode current = queue.poll();
                
                for (int i = 0; i < 26; i++) {
                    ACNode child = current.children[i];
                    if (child != null) {
                        ACNode failNode = current.fail;
                        
                        // 沿着失败指针向上找，直到找到匹配或到达root
                        while (failNode != null && failNode.children[i] == null) {
                            failNode = failNode.fail;
                        }
                        
                        if (failNode == null) {
                            child.fail = root;
                        } else {
                            child.fail = failNode.children[i];
                        }
                        
                        queue.offer(child);
                    }
                }
            }
        }
        
        /**
         * 多模式匹配
         * 
         * @param text 文本串
         * @return 所有匹配的模式串及其位置
         */
        public Map<String, List<Integer>> search(String text) {
            Map<String, List<Integer>> result = new HashMap<>();
            ACNode current = root;
            
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int index = c - 'a';
                
                // 如果当前字符不匹配，沿着失败指针回溯
                while (current != root && current.children[index] == null) {
                    current = current.fail;
                }
                
                if (current.children[index] != null) {
                    current = current.children[index];
                }
                
                // 检查以当前位置结尾的所有模式串
                ACNode temp = current;
                while (temp != root) {
                    if (temp.isEnd) {
                        result.computeIfAbsent(temp.pattern, k -> new ArrayList<>())
                             .add(i - temp.pattern.length() + 1);
                    }
                    temp = temp.fail;
                }
            }
            
            return result;
        }
        
        private static class ACNode {
            ACNode[] children = new ACNode[26];
            ACNode fail;
            boolean isEnd;
            String pattern;
        }
    }
    
    /**
     * 字符串周期检测与压缩
     * 
     * 应用场景: 数据压缩、重复模式检测、字符串周期分析
     * 
     * 算法原理:
     * 利用KMP的next数组性质检测字符串的最小周期
     * 如果n % (n - next[n]) == 0，则字符串有周期
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static class StringCompressor {
        
        /**
         * 检测字符串的最小周期
         * 
         * @param s 输入字符串
         * @return 最小周期长度，如果没有周期返回字符串长度
         */
        public static int findMinPeriod(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }
            
            int n = s.length();
            int[] next = buildNextArray(s);
            int period = n - next[n];
            
            if (n % period == 0) {
                return period;
            }
            return n;
        }
        
        /**
         * 压缩重复模式的字符串
         * 
         * @param s 输入字符串
         * @return 压缩后的字符串表示
         */
        public static String compress(String s) {
            int period = findMinPeriod(s);
            
            if (period == s.length()) {
                return s; // 没有重复模式
            }
            
            int repeat = s.length() / period;
            return s.substring(0, period) + "{" + repeat + "}";
        }
        
        /**
         * 解压缩字符串
         * 
         * @param compressed 压缩后的字符串
         * @return 原始字符串
         */
        public static String decompress(String compressed) {
            // 简单的解压缩实现，处理格式：abc{3}
            int braceIndex = compressed.indexOf('{');
            if (braceIndex == -1) {
                return compressed;
            }
            
            String base = compressed.substring(0, braceIndex);
            int endBrace = compressed.indexOf('}');
            int repeat = Integer.parseInt(compressed.substring(braceIndex + 1, endBrace));
            
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < repeat; i++) {
                result.append(base);
            }
            
            return result.toString();
        }
    }
    
    /**
     * 最长回文子串的Manacher算法（基于KMP思想）
     * 
     * 应用场景: 文本分析、DNA序列分析、回文检测
     * 
     * 算法原理:
     * 1. 预处理字符串，插入特殊字符处理偶长度回文
     * 2. 维护回文半径数组，利用对称性减少重复计算
     * 3. 类似KMP的思想，利用已知信息避免重复计算
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static class ManacherAlgorithm {
        
        /**
         * 查找最长回文子串
         * 
         * @param s 输入字符串
         * @return 最长回文子串
         */
        public static String longestPalindrome(String s) {
            if (s == null || s.length() == 0) {
                return "";
            }
            
            // 预处理字符串
            String processed = preprocess(s);
            int n = processed.length();
            int[] p = new int[n]; // 回文半径数组
            int center = 0, right = 0;
            int maxLen = 0, maxCenter = 0;
            
            for (int i = 0; i < n; i++) {
                // 利用对称性
                int mirror = 2 * center - i;
                if (i < right) {
                    p[i] = Math.min(right - i, p[mirror]);
                }
                
                // 尝试扩展回文
                int leftExpand = i - (1 + p[i]);
                int rightExpand = i + (1 + p[i]);
                
                while (leftExpand >= 0 && rightExpand < n && 
                       processed.charAt(leftExpand) == processed.charAt(rightExpand)) {
                    p[i]++;
                    leftExpand--;
                    rightExpand++;
                }
                
                // 更新中心和右边界
                if (i + p[i] > right) {
                    center = i;
                    right = i + p[i];
                }
                
                // 更新最长回文
                if (p[i] > maxLen) {
                    maxLen = p[i];
                    maxCenter = i;
                }
            }
            
            // 提取最长回文子串
            int start = (maxCenter - maxLen) / 2;
            return s.substring(start, start + maxLen);
        }
        
        private static String preprocess(String s) {
            StringBuilder sb = new StringBuilder();
            sb.append('^');
            for (char c : s.toCharArray()) {
                sb.append('#').append(c);
            }
            sb.append("#$");
            return sb.toString();
        }
    }
    
    /**
     * 生物信息学应用 - DNA序列模式匹配
     * 
     * 应用场景: 基因序列分析、蛋白质序列匹配、生物标记检测
     * 
     * 特殊考量:
     * 1. 处理模糊匹配（允许错配）
     * 2. 处理通配符匹配
     * 3. 处理序列比对
     */
    public static class BioinformaticsKMP {
        
        /**
         * 模糊KMP匹配 - 允许最多k个错配
         * 
         * @param text DNA文本序列
         * @param pattern DNA模式序列
         * @param k 最大允许错配数
         * @return 所有匹配位置
         */
        public static List<Integer> fuzzyKMP(String text, String pattern, int k) {
            List<Integer> result = new ArrayList<>();
            if (text == null || pattern == null || pattern.length() > text.length()) {
                return result;
            }
            
            int n = text.length(), m = pattern.length();
            
            for (int i = 0; i <= n - m; i++) {
                int mismatches = 0;
                boolean match = true;
                
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        mismatches++;
                        if (mismatches > k) {
                            match = false;
                            break;
                        }
                    }
                }
                
                if (match) {
                    result.add(i);
                }
            }
            
            return result;
        }
        
        /**
         * 通配符KMP匹配 - 支持通配符'?'（匹配任意字符）
         * 
         * @param text 文本序列
         * @param pattern 包含通配符的模式串
         * @return 所有匹配位置
         */
        public static List<Integer> wildcardKMP(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            if (text == null || pattern == null) {
                return result;
            }
            
            int n = text.length(), m = pattern.length();
            if (m > n) {
                return result;
            }
            
            // 构建next数组（考虑通配符）
            int[] next = buildNextArrayWithWildcard(pattern);
            
            int i = 0, j = 0;
            while (i < n) {
                if (j == -1 || pattern.charAt(j) == '?' || text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
                
                if (j == m) {
                    result.add(i - m);
                    j = next[j];
                }
            }
            
            return result;
        }
        
        private static int[] buildNextArrayWithWildcard(String pattern) {
            int m = pattern.length();
            int[] next = new int[m + 1];
            next[0] = -1;
            
            int i = 0, j = -1;
            while (i < m) {
                if (j == -1 || pattern.charAt(i) == '?' || pattern.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                    next[i] = j;
                } else {
                    j = next[j];
                }
            }
            
            return next;
        }
    }
    
    /**
     * 文本编辑器应用 - 查找替换功能
     * 
     * 应用场景: 文本编辑器、IDE、文档处理系统
     * 
     * 功能特性:
     * 1. 高效查找所有匹配位置
     * 2. 批量替换功能
     * 3. 支持大小写敏感/不敏感
     * 4. 支持正则表达式（简化版）
     */
    public static class TextEditorKMP {
        
        /**
         * 查找文本中所有匹配位置
         * 
         * @param text 文本内容
         * @param pattern 查找模式
         * @param caseSensitive 是否大小写敏感
         * @return 所有匹配位置
         */
        public static List<Integer> findAllMatches(String text, String pattern, boolean caseSensitive) {
            if (!caseSensitive) {
                text = text.toLowerCase();
                pattern = pattern.toLowerCase();
            }
            return kmpAllMatches(text, pattern);
        }
        
        /**
         * 替换文本中的匹配内容
         * 
         * @param text 原始文本
         * @param pattern 查找模式
         * @param replacement 替换内容
         * @param caseSensitive 是否大小写敏感
         * @return 替换后的文本
         */
        public static String replaceAll(String text, String pattern, String replacement, boolean caseSensitive) {
            List<Integer> matches = findAllMatches(text, pattern, caseSensitive);
            if (matches.isEmpty()) {
                return text;
            }
            
            StringBuilder result = new StringBuilder();
            int lastIndex = 0;
            
            for (int match : matches) {
                // 添加匹配前的部分
                result.append(text, lastIndex, match);
                // 添加替换内容
                result.append(replacement);
                lastIndex = match + pattern.length();
            }
            
            // 添加剩余部分
            result.append(text.substring(lastIndex));
            return result.toString();
        }
        
        /**
         * 统计匹配次数
         * 
         * @param text 文本内容
         * @param pattern 查找模式
         * @param caseSensitive 是否大小写敏感
         * @return 匹配次数
         */
        public static int countMatches(String text, String pattern, boolean caseSensitive) {
            return findAllMatches(text, pattern, caseSensitive).size();
        }
    }
    
    /**
     * 通用的KMP算法实现（基础版本）
     */
    private static int[] buildNextArray(String pattern) {
        int m = pattern.length();
        if (m == 0) {
            return new int[0];
        }
        
        int[] next = new int[m + 1];
        next[0] = -1;
        if (m == 1) {
            return next;
        }
        
        next[1] = 0;
        int i = 2, cn = 0;
        
        while (i <= m) {
            if (pattern.charAt(i - 1) == pattern.charAt(cn)) {
                next[i++] = ++cn;
            } else if (cn > 0) {
                cn = next[cn];
            } else {
                next[i++] = 0;
            }
        }
        
        return next;
    }
    
    private static List<Integer> kmpAllMatches(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        
        if (text == null || pattern == null || pattern.length() == 0) {
            return result;
        }
        
        int n = text.length(), m = pattern.length();
        if (m > n) {
            return result;
        }
        
        int[] next = buildNextArray(pattern);
        int i = 0, j = 0;
        
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = next[j];
            }
            
            if (j == m) {
                result.add(i - j);
                j = next[j];
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== KMP算法高级应用测试 ===\n");
        
        // 测试AC自动机
        testACAutomaton();
        
        // 测试字符串压缩
        testStringCompression();
        
        // 测试Manacher算法
        testManacherAlgorithm();
        
        // 测试生物信息学应用
        testBioinformaticsKMP();
        
        // 测试文本编辑器应用
        testTextEditorKMP();
        
        System.out.println("所有测试完成!");
    }
    
    private static void testACAutomaton() {
        System.out.println("=== AC自动机测试 ===");
        
        ACAutomaton automaton = new ACAutomaton();
        automaton.insert("he");
        automaton.insert("she");
        automaton.insert("his");
        automaton.insert("hers");
        automaton.buildFailure();
        
        String text = "ushers";
        Map<String, List<Integer>> result = automaton.search(text);
        
        System.out.println("文本: " + text);
        System.out.println("匹配结果: " + result);
        System.out.println();
    }
    
    private static void testStringCompression() {
        System.out.println("=== 字符串压缩测试 ===");
        
        String test1 = "abcabcabc";
        String compressed = StringCompressor.compress(test1);
        String decompressed = StringCompressor.decompress(compressed);
        
        System.out.println("原始字符串: " + test1);
        System.out.println("压缩后: " + compressed);
        System.out.println("解压缩后: " + decompressed);
        System.out.println("压缩比: " + (double)compressed.length() / test1.length());
        System.out.println();
    }
    
    private static void testManacherAlgorithm() {
        System.out.println("=== Manacher算法测试 ===");
        
        String test1 = "babad";
        String longest = ManacherAlgorithm.longestPalindrome(test1);
        
        System.out.println("输入: " + test1);
        System.out.println("最长回文子串: " + longest);
        System.out.println();
    }
    
    private static void testBioinformaticsKMP() {
        System.out.println("=== 生物信息学应用测试 ===");
        
        String dnaText = "ATCGATCGATCG";
        String dnaPattern = "ATCG";
        
        List<Integer> fuzzyMatches = BioinformaticsKMP.fuzzyKMP(dnaText, dnaPattern, 1);
        List<Integer> wildcardMatches = BioinformaticsKMP.wildcardKMP(dnaText, "AT?G");
        
        System.out.println("DNA序列: " + dnaText);
        System.out.println("模式: " + dnaPattern);
        System.out.println("模糊匹配(允许1个错配): " + fuzzyMatches);
        System.out.println("通配符匹配: " + wildcardMatches);
        System.out.println();
    }
    
    private static void testTextEditorKMP() {
        System.out.println("=== 文本编辑器应用测试 ===");
        
        String text = "Hello world, hello everyone!";
        String pattern = "hello";
        
        int count = TextEditorKMP.countMatches(text, pattern, false);
        String replaced = TextEditorKMP.replaceAll(text, pattern, "HI", false);
        
        System.out.println("原文: " + text);
        System.out.println("模式: " + pattern);
        System.out.println("匹配次数(不区分大小写): " + count);
        System.out.println("替换后: " + replaced);
        System.out.println();
    }
}