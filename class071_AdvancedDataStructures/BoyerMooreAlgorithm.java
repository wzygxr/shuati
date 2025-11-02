package class029_AdvancedDataStructures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * BM (Boyer-Moore) 字符串匹配算法实现
 * 包含坏字符规则和好后缀规则
 * 时间复杂度：
 *   - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)
 *   - 平均情况: O(n)
 * 空间复杂度：O(k)，其中k是字符集大小
 */
public class BoyerMooreAlgorithm {
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
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
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试用例1：基本匹配
        String text1 = "hello world";
        String pattern1 = "world";
        System.out.println("测试1 - 查找'world'在'hello world'中的位置: " + search(text1, pattern1)); // 应该是6
        
        // 测试用例2：多次匹配
        String text2 = "abababa";
        String pattern2 = "aba";
        int[] results2 = searchAll(text2, pattern2);
        System.out.print("测试2 - 查找所有'aba'在'abababa'中的位置: ");
        for (int pos : results2) {
            System.out.print(pos + " "); // 应该是0 2 4
        }
        System.out.println();
        
        // 测试用例3：无匹配
        String text3 = "hello";
        String pattern3 = "world";
        System.out.println("测试3 - 查找'world'在'hello'中的位置: " + search(text3, pattern3)); // 应该是-1
        
        // 测试用例4：边界情况
        String text4 = "test";
        String pattern4 = "";
        System.out.println("测试4 - 查找空串在'test'中的位置: " + search(text4, pattern4)); // 应该是0
        
        // 测试用例5：BM算法优势场景
        String text5 = "GCATCGCAGAGAGTATACAGTACG";
        String pattern5 = "GCAGAGAG";
        System.out.println("测试5 - 查找模式串在文本串中的位置: " + search(text5, pattern5)); // 应该是5
        
        // 测试用例6：好后缀规则测试
        String text6 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pattern6 = "XYZ";
        System.out.println("测试6 - 查找'XYZ'在字母表中的位置: " + search(text6, pattern6)); // 应该是23
    }
}