package class186;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Sunday字符串匹配算法实现
 * Sunday算法是一种高效的字符串匹配算法，由Daniel M. Sunday于1990年提出
 * 核心思想：当匹配失败时，根据目标字符串中位于模式串后一位的字符来决定移动距离
 * 
 * 时间复杂度：
 *   - 最好情况: O(n/m)，其中n是文本长度，m是模式长度
 *   - 最坏情况: O(n*m)
 *   - 平均情况: O(n)
 * 空间复杂度：O(1) - 使用固定大小的字符集（ASCII）
 * 
 * 应用场景：
 * - 文本搜索
 * - 字符串替换
 * - 数据挖掘中的模式匹配
 * - 文本处理和分析
 */
public class SundayAlgorithm {
    private static final int ALPHABET_SIZE = 256; // ASCII字符集大小
    
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
    
    /**
     * 计算模式串在文本串中的出现次数
     * @param text 文本串
     * @param pattern 模式串
     * @return 出现次数
     */
    public static int countOccurrences(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        if (pattern.isEmpty()) {
            // 空模式串的特殊处理
            return text.length() + 1;
        }
        
        return searchAll(text, pattern).length;
    }
    
    /**
     * 替换文本串中所有出现的模式串为指定的替换字符串
     * @param text 文本串
     * @param pattern 模式串
     * @param replacement 替换字符串
     * @return 替换后的文本
     */
    public static String replaceAll(String text, String pattern, String replacement) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        if (replacement == null) {
            replacement = "";
        }
        
        int[] positions = searchAll(text, pattern);
        if (positions.length == 0) {
            return text; // 没有匹配，直接返回原文本
        }
        
        // 构建替换后的文本
        StringBuilder result = new StringBuilder();
        int lastPos = 0;
        int m = pattern.length();
        
        for (int pos : positions) {
            // 添加匹配位置之前的文本
            result.append(text.substring(lastPos, pos));
            // 添加替换字符串
            result.append(replacement);
            // 更新上次处理的位置
            lastPos = pos + m;
        }
        
        // 添加最后一个匹配之后的文本
        result.append(text.substring(lastPos));
        
        return result.toString();
    }
    
    /**
     * 查找模式串在文本串中的出现次数（优化版本）
     * 不使用searchAll方法，直接计算以提高效率
     * @param text 文本串
     * @param pattern 模式串
     * @return 出现次数
     */
    public static int countOccurrencesOptimized(String text, String pattern) {
        if (text == null || pattern == null) {
            throw new IllegalArgumentException("文本串和模式串不能为null");
        }
        
        int n = text.length();
        int m = pattern.length();
        int count = 0;
        
        if (m == 0) {
            return n + 1;
        }
        
        if (n < m) {
            return 0;
        }
        
        // 构建偏移表
        int[] shift = buildShiftTable(pattern);
        
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
                count++;
                i++; // 移动1位继续查找下一个匹配
            } else {
                int nextPos = i + m;
                if (nextPos >= n) {
                    break;
                }
                i += shift[text.charAt(nextPos)];
            }
        }
        
        return count;
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
        
        // 测试用例5：特殊字符
        String text5 = "a!b@c#d$e%";
        String pattern5 = "c#d";
        System.out.println("测试5 - 查找'c#d'在特殊字符串中的位置: " + search(text5, pattern5)); // 应该是4
        
        // 测试用例6：计数功能
        System.out.println("测试6 - 'aba'在'abababa'中出现的次数: " + countOccurrences(text2, pattern2)); // 应该是3
        System.out.println("测试6-1 - 优化版计数: " + countOccurrencesOptimized(text2, pattern2)); // 应该是3
        
        // 测试用例7：替换功能
        String text7 = "banana is a banana and another banana";
        String pattern7 = "banana";
        String replacement7 = "orange";
        System.out.println("测试7 - 替换前: " + text7);
        System.out.println("测试7 - 替换后: " + replaceAll(text7, pattern7, replacement7));
        
        // 测试用例8：较长文本
        String text8 = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String pattern8 = "XYZ";
        System.out.println("测试8 - 'XYZ'在长文本中的位置: " + search(text8, pattern8));
        System.out.println("测试8 - 'XYZ'在长文本中出现的次数: " + countOccurrences(text8, pattern8));
    }
}