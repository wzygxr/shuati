package class031;

import java.util.*;

/**
 * 最大单词长度乘积（位掩码优化）
 * 测试链接：https://leetcode.cn/problems/maximum-product-of-word-lengths/
 * 
 * 题目描述：
 * 给定一个字符串数组 words，找到 length(word[i]) * length(word[j]) 的最大值，
 * 并且这两个单词不含有公共字母。你可以认为每个单词只包含小写字母。如果不存在这样的两个单词，返回 0。
 * 
 * 示例：
 * 输入: ["abcw","baz","foo","bar","xtfn","abcdef"]
 * 输出: 16 
 * 解释: 这两个单词为 "abcw", "xtfn"，长度乘积为 4 * 4 = 16。
 * 
 * 输入: ["a","ab","abc","d","cd","bcd","abcd"]
 * 输出: 4 
 * 解释: 这两个单词为 "ab", "cd"，长度乘积为 2 * 2 = 4。
 * 
 * 提示：
 * 2 <= words.length <= 1000
 * 1 <= words[i].length <= 1000
 * words[i] 仅包含小写字母
 * 
 * 解题思路：
 * 1. 暴力法：双重循环检查所有组合（会超时）
 * 2. 位掩码法：使用位掩码表示每个单词的字符集合
 * 3. 位掩码优化：预计算位掩码和长度，优化比较过程
 * 4. 哈希表法：使用哈希表存储相同位掩码的最大长度
 * 5. 分组法：按位掩码分组，只比较不同组的单词
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n² * L)，L为单词平均长度
 * - 位掩码法：O(n² + nL)
 * - 位掩码优化：O(n² + nL)
 * - 哈希表法：O(n² + nL)
 * - 分组法：O(n² + nL)
 * 
 * 空间复杂度分析：
 * - 暴力法：O(1)
 * - 位掩码法：O(n)
 * - 位掩码优化：O(n)
 * - 哈希表法：O(n)
 * - 分组法：O(n)
 */
public class Code40_MaximumProductofWordLengths {
    
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n² * L)，L为单词平均长度
     * 空间复杂度：O(1)
     */
    public int maxProduct1(String[] words) {
        int maxProduct = 0;
        int n = words.length;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!hasCommonChars(words[i], words[j])) {
                    maxProduct = Math.max(maxProduct, words[i].length() * words[j].length());
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 检查两个单词是否有公共字符
     * 时间复杂度：O(L1 + L2)
     * 空间复杂度：O(26) = O(1)
     */
    private boolean hasCommonChars(String word1, String word2) {
        boolean[] chars = new boolean[26];
        
        // 记录第一个单词的字符
        for (char c : word1.toCharArray()) {
            chars[c - 'a'] = true;
        }
        
        // 检查第二个单词是否有相同字符
        for (char c : word2.toCharArray()) {
            if (chars[c - 'a']) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 方法2：位掩码法（推荐）
     * 核心思想：使用26位整数表示每个单词的字符集合
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    public int maxProduct2(String[] words) {
        int n = words.length;
        int[] masks = new int[n];  // 存储每个单词的位掩码
        int[] lengths = new int[n];  // 存储每个单词的长度
        
        // 预处理：计算每个单词的位掩码和长度
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i].toCharArray()) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 比较所有单词对
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 使用位与运算检查是否有公共字符
                if ((masks[i] & masks[j]) == 0) {
                    maxProduct = Math.max(maxProduct, lengths[i] * lengths[j]);
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法3：位掩码优化版
     * 优化比较过程，减少不必要的计算
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    public int maxProduct3(String[] words) {
        int n = words.length;
        int[] masks = new int[n];
        int[] lengths = new int[n];
        
        // 预处理
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i].toCharArray()) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 优化：先按长度降序排序，可以提前终止
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // 按长度降序排序
        Arrays.sort(indices, (a, b) -> lengths[b] - lengths[a]);
        
        for (int i = 0; i < n; i++) {
            int idx1 = indices[i];
            
            // 如果当前最大乘积已经大于可能的最大值，提前终止
            if (lengths[idx1] * lengths[idx1] <= maxProduct) {
                break;
            }
            
            for (int j = i + 1; j < n; j++) {
                int idx2 = indices[j];
                
                if ((masks[idx1] & masks[idx2]) == 0) {
                    maxProduct = Math.max(maxProduct, lengths[idx1] * lengths[idx2]);
                    break;  // 由于排序，后面的长度更小，乘积不会更大
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法4：哈希表法
     * 使用哈希表存储相同位掩码的最大长度
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    public int maxProduct4(String[] words) {
        Map<Integer, Integer> maskToMaxLength = new HashMap<>();
        
        // 预处理：对于相同的位掩码，只保留最长的单词长度
        for (String word : words) {
            int mask = 0;
            for (char c : word.toCharArray()) {
                mask |= 1 << (c - 'a');
            }
            
            // 更新相同掩码的最大长度
            maskToMaxLength.put(mask, Math.max(
                maskToMaxLength.getOrDefault(mask, 0), 
                word.length()
            ));
        }
        
        int maxProduct = 0;
        List<Integer> masks = new ArrayList<>(maskToMaxLength.keySet());
        int size = masks.size();
        
        // 比较所有不同的位掩码
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                int mask1 = masks.get(i);
                int mask2 = masks.get(j);
                
                if ((mask1 & mask2) == 0) {
                    int product = maskToMaxLength.get(mask1) * maskToMaxLength.get(mask2);
                    maxProduct = Math.max(maxProduct, product);
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法5：分组法
     * 按位掩码分组，优化比较过程
     * 时间复杂度：O(n² + nL)
     * 空间复杂度：O(n)
     */
    public int maxProduct5(String[] words) {
        int n = words.length;
        
        // 预处理：计算位掩码和长度
        int[] masks = new int[n];
        int[] lengths = new int[n];
        
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i].toCharArray()) {
                mask |= 1 << (c - 'a');
            }
            masks[i] = mask;
            lengths[i] = words[i].length();
        }
        
        int maxProduct = 0;
        
        // 分组比较：使用位运算优化
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // 快速检查：如果长度乘积小于当前最大值，跳过
                if (lengths[i] * lengths[j] <= maxProduct) {
                    continue;
                }
                
                // 位运算检查公共字符
                if ((masks[i] & masks[j]) == 0) {
                    maxProduct = lengths[i] * lengths[j];
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 方法6：位掩码+排序优化
     * 结合排序和位掩码技术
     * 时间复杂度：O(n² + nL + nlogn)
     * 空间复杂度：O(n)
     */
    public int maxProduct6(String[] words) {
        int n = words.length;
        
        // 创建单词信息数组
        WordInfo[] wordInfos = new WordInfo[n];
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (char c : words[i].toCharArray()) {
                mask |= 1 << (c - 'a');
            }
            wordInfos[i] = new WordInfo(mask, words[i].length());
        }
        
        // 按长度降序排序
        Arrays.sort(wordInfos, (a, b) -> b.length - a.length);
        
        int maxProduct = 0;
        
        for (int i = 0; i < n; i++) {
            // 提前终止：如果当前单词长度的平方小于最大乘积
            if (wordInfos[i].length * wordInfos[i].length <= maxProduct) {
                break;
            }
            
            for (int j = i + 1; j < n; j++) {
                int product = wordInfos[i].length * wordInfos[j].length;
                if (product <= maxProduct) {
                    break;  // 由于排序，后面的乘积更小
                }
                
                if ((wordInfos[i].mask & wordInfos[j].mask) == 0) {
                    maxProduct = product;
                    break;  // 找到当前i的最大可能乘积
                }
            }
        }
        
        return maxProduct;
    }
    
    /**
     * 单词信息辅助类
     */
    class WordInfo {
        int mask;
        int length;
        
        WordInfo(int mask, int length) {
            this.mask = mask;
            this.length = length;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code40_MaximumProductofWordLengths solution = new Code40_MaximumProductofWordLengths();
        
        // 测试用例1：示例1
        String[] words1 = {"abcw", "baz", "foo", "bar", "xtfn", "abcdef"};
        int result1 = solution.maxProduct2(words1);
        System.out.println("测试用例1 - 输入: " + Arrays.toString(words1));
        System.out.println("结果: " + result1 + " (预期: 16)");
        
        // 测试用例2：示例2
        String[] words2 = {"a", "ab", "abc", "d", "cd", "bcd", "abcd"};
        int result2 = solution.maxProduct2(words2);
        System.out.println("测试用例2 - 输入: " + Arrays.toString(words2));
        System.out.println("结果: " + result2 + " (预期: 4)");
        
        // 测试用例3：无解情况
        String[] words3 = {"a", "aa", "aaa", "aaaa"};
        int result3 = solution.maxProduct2(words3);
        System.out.println("测试用例3 - 输入: " + Arrays.toString(words3));
        System.out.println("结果: " + result3 + " (预期: 0)");
        
        // 测试用例4：边界情况（两个单词）
        String[] words4 = {"ab", "cd"};
        int result4 = solution.maxProduct2(words4);
        System.out.println("测试用例4 - 输入: " + Arrays.toString(words4));
        System.out.println("结果: " + result4 + " (预期: 4)");
        
        // 性能测试
        String[] largeWords = new String[100];
        for (int i = 0; i < 100; i++) {
            largeWords[i] = generateRandomWord(100);
        }
        
        long startTime = System.currentTimeMillis();
        int result5 = solution.maxProduct2(largeWords);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 输入长度: " + largeWords.length);
        System.out.println("结果: " + result5);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 所有方法结果对比
        System.out.println("\n=== 所有方法结果对比 ===");
        String[] testWords = {"abc", "def", "ghi", "jkl"};
        System.out.println("测试单词数组: " + Arrays.toString(testWords));
        System.out.println("方法1 (暴力法): " + solution.maxProduct1(testWords));
        System.out.println("方法2 (位掩码法): " + solution.maxProduct2(testWords));
        System.out.println("方法3 (位掩码优化): " + solution.maxProduct3(testWords));
        System.out.println("方法4 (哈希表法): " + solution.maxProduct4(testWords));
        System.out.println("方法5 (分组法): " + solution.maxProduct5(testWords));
        System.out.println("方法6 (位掩码+排序): " + solution.maxProduct6(testWords));
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 暴力法:");
        System.out.println("  时间复杂度: O(n² * L) - 会超时");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 位掩码法:");
        System.out.println("  时间复杂度: O(n² + nL)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法3 - 位掩码优化:");
        System.out.println("  时间复杂度: O(n² + nL)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法4 - 哈希表法:");
        System.out.println("  时间复杂度: O(n² + nL)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法5 - 分组法:");
        System.out.println("  时间复杂度: O(n² + nL)");
        System.out.println("  空间复杂度: O(n)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 算法选择：方法2 (位掩码法) 最优");
        System.out.println("2. 性能优化：避免O(n² * L)的暴力解法");
        System.out.println("3. 空间优化：使用位掩码压缩存储");
        System.out.println("4. 实际应用：适合处理大量单词数据");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 位掩码技术：26位整数表示字符集合");
        System.out.println("2. 位运算优化：使用 & 运算快速检查公共字符");
        System.out.println("3. 预处理思想：先计算位掩码，再进行比较");
        System.out.println("4. 排序优化：按长度降序排序，提前终止");
    }
    
    /**
     * 生成随机单词（用于性能测试）
     */
    private static String generateRandomWord(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char)('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
}