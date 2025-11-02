package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * 剑指Offer 50. 第一个只出现一次的字符
 * 题目链接：https://leetcode-cn.com/problems/di-yi-ge-zhi-chu-xian-yi-ci-de-zi-fu-lcof/
 * 
 * 题目描述：
 * 在字符串s中找出第一个只出现一次的字符。如果没有，返回一个单空格。
 * s只包含小写字母。
 * 
 * 示例：
 * 输入: s = "abaccdeff"
 * 输出: 'b'
 * 
 * 输入: s = ""
 * 输出: ' '
 * 
 * 算法思路：
 * 1. 第一次遍历字符串，使用哈希表统计每个字符出现的次数
 * 2. 第二次遍历字符串，找到第一个出现次数为1的字符
 * 3. 如果没有找到，返回空格
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(1)，因为字符集大小固定为26个小写字母
 * 
 * 优化思路：
 * 1. 使用数组代替哈希表提高性能（字符集固定且较小）
 * 2. 使用LinkedHashMap保持插入顺序
 * 3. 一次遍历优化（记录第一次出现位置和次数）
 * 
 * 工程化考量：
 * 1. 边界情况处理：空字符串、所有字符都重复、所有字符都唯一
 * 2. 输入验证：确保只包含小写字母
 * 3. 异常处理：返回约定的默认值
 * 4. 性能优化：选择合适的数据结构
 */
public class Code18_JianZhiOffer50_FirstUniqueCharacter {
    
    /**
     * 方法1：使用数组统计字符频次（最优解）
     * 
     * @param s 输入字符串
     * @return 第一个只出现一次的字符，如果没有则返回空格
     */
    public static char firstUniqCharWithArray(String s) {
        if (s == null || s.isEmpty()) {
            return ' ';
        }
        
        // 使用数组统计字符出现次数（只考虑小写字母）
        int[] charCount = new int[26];
        
        // 第一次遍历：统计字符频次
        for (int i = 0; i < s.length(); i++) {
            charCount[s.charAt(i) - 'a']++;
        }
        
        // 第二次遍历：找到第一个出现次数为1的字符
        for (int i = 0; i < s.length(); i++) {
            if (charCount[s.charAt(i) - 'a'] == 1) {
                return s.charAt(i);
            }
        }
        
        return ' '; // 没有找到只出现一次的字符
    }
    
    /**
     * 方法2：使用HashMap统计字符频次
     * 
     * @param s 输入字符串
     * @return 第一个只出现一次的字符，如果没有则返回空格
     */
    public static char firstUniqCharWithHashMap(String s) {
        if (s == null || s.isEmpty()) {
            return ' ';
        }
        
        // 使用HashMap统计字符出现次数
        Map<Character, Integer> charCount = new HashMap<>();
        
        // 第一次遍历：统计字符频次
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        
        // 第二次遍历：找到第一个出现次数为1的字符
        for (char c : s.toCharArray()) {
            if (charCount.get(c) == 1) {
                return c;
            }
        }
        
        return ' '; // 没有找到只出现一次的字符
    }
    
    /**
     * 方法3：使用LinkedHashMap保持插入顺序
     * 
     * @param s 输入字符串
     * @return 第一个只出现一次的字符，如果没有则返回空格
     */
    public static char firstUniqCharWithLinkedHashMap(String s) {
        if (s == null || s.isEmpty()) {
            return ' ';
        }
        
        // 使用LinkedHashMap统计字符出现次数并保持插入顺序
        Map<Character, Integer> charCount = new LinkedHashMap<>();
        
        // 统计字符频次
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        
        // 遍历LinkedHashMap找到第一个出现次数为1的字符
        for (Map.Entry<Character, Integer> entry : charCount.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        
        return ' '; // 没有找到只出现一次的字符
    }
    
    /**
     * 方法4：一次遍历优化版本（记录字符首次出现位置）
     * 
     * @param s 输入字符串
     * @return 第一个只出现一次的字符，如果没有则返回空格
     */
    public static char firstUniqCharOptimized(String s) {
        if (s == null || s.isEmpty()) {
            return ' ';
        }
        
        // 使用数组记录字符信息：正数表示出现次数，负数表示第一次出现的位置（从1开始）
        int[] charInfo = new int[26];
        
        // 遍历字符串，记录字符信息
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - 'a';
            if (charInfo[index] == 0) {
                // 第一次出现，记录位置（负数表示）
                charInfo[index] = -(i + 1);
            } else if (charInfo[index] < 0) {
                // 第二次出现，转换为正数表示出现次数
                charInfo[index] = -charInfo[index] + 1;
            } else {
                // 第三次及以后出现，增加计数
                charInfo[index]++;
            }
        }
        
        // 找到出现次数为1且位置最小的字符
        int minPos = Integer.MAX_VALUE;
        char result = ' ';
        
        for (int i = 0; i < 26; i++) {
            if (charInfo[i] == -1) { // 只出现一次
                int pos = -charInfo[i] - 1; // 转换为0-based位置
                if (pos < minPos) {
                    minPos = pos;
                    result = (char) ('a' + i);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 剑指Offer 50. 第一个只出现一次的字符 ===");
        
        // 测试用例1
        String s1 = "abaccdeff";
        char result1a = firstUniqCharWithArray(s1);
        char result1b = firstUniqCharWithHashMap(s1);
        char result1c = firstUniqCharWithLinkedHashMap(s1);
        char result1d = firstUniqCharOptimized(s1);
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("数组方法结果: '" + result1a + "' (期望: 'b')");
        System.out.println("HashMap方法结果: '" + result1b + "' (期望: 'b')");
        System.out.println("LinkedHashMap方法结果: '" + result1c + "' (期望: 'b')");
        System.out.println("优化方法结果: '" + result1d + "' (期望: 'b')");
        System.out.println("测试结果: " + (result1a == 'b' && result1b == 'b' && result1c == 'b' && result1d == 'b' ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2
        String s2 = "";
        char result2a = firstUniqCharWithArray(s2);
        char result2b = firstUniqCharWithHashMap(s2);
        char result2c = firstUniqCharWithLinkedHashMap(s2);
        char result2d = firstUniqCharOptimized(s2);
        System.out.println("输入: \"" + s2 + "\" (空字符串)");
        System.out.println("数组方法结果: '" + result2a + "' (期望: ' ')");
        System.out.println("HashMap方法结果: '" + result2b + "' (期望: ' ')");
        System.out.println("LinkedHashMap方法结果: '" + result2c + "' (期望: ' ')");
        System.out.println("优化方法结果: '" + result2d + "' (期望: ' ')");
        System.out.println("测试结果: " + (result2a == ' ' && result2b == ' ' && result2c == ' ' && result2d == ' ' ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3
        String s3 = "aabb";
        char result3a = firstUniqCharWithArray(s3);
        char result3b = firstUniqCharWithHashMap(s3);
        char result3c = firstUniqCharWithLinkedHashMap(s3);
        char result3d = firstUniqCharOptimized(s3);
        System.out.println("输入: \"" + s3 + "\" (所有字符都重复)");
        System.out.println("数组方法结果: '" + result3a + "' (期望: ' ')");
        System.out.println("HashMap方法结果: '" + result3b + "' (期望: ' ')");
        System.out.println("LinkedHashMap方法结果: '" + result3c + "' (期望: ' ')");
        System.out.println("优化方法结果: '" + result3d + "' (期望: ' ')");
        System.out.println("测试结果: " + (result3a == ' ' && result3b == ' ' && result3c == ' ' && result3d == ' ' ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例4
        String s4 = "abcdef";
        char result4a = firstUniqCharWithArray(s4);
        char result4b = firstUniqCharWithHashMap(s4);
        char result4c = firstUniqCharWithLinkedHashMap(s4);
        char result4d = firstUniqCharOptimized(s4);
        System.out.println("输入: \"" + s4 + "\" (所有字符都唯一)");
        System.out.println("数组方法结果: '" + result4a + "' (期望: 'a')");
        System.out.println("HashMap方法结果: '" + result4b + "' (期望: 'a')");
        System.out.println("LinkedHashMap方法结果: '" + result4c + "' (期望: 'a')");
        System.out.println("优化方法结果: '" + result4d + "' (期望: 'a')");
        System.out.println("测试结果: " + (result4a == 'a' && result4b == 'a' && result4c == 'a' && result4d == 'a' ? "通过" : "失败"));
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        String largeString = sb.toString();
        
        long startTime = System.currentTimeMillis();
        char resultArray = firstUniqCharWithArray(largeString);
        long arrayTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        char resultHashMap = firstUniqCharWithHashMap(largeString);
        long hashMapTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        char resultLinkedHashMap = firstUniqCharWithLinkedHashMap(largeString);
        long linkedHashMapTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        char resultOptimized = firstUniqCharOptimized(largeString);
        long optimizedTime = System.currentTimeMillis() - startTime;
        
        System.out.println("大字符串长度: " + largeString.length());
        System.out.println("数组方法耗时: " + arrayTime + "ms, 结果: '" + resultArray + "'");
        System.out.println("HashMap方法耗时: " + hashMapTime + "ms, 结果: '" + resultHashMap + "'");
        System.out.println("LinkedHashMap方法耗时: " + linkedHashMapTime + "ms, 结果: '" + resultLinkedHashMap + "'");
        System.out.println("优化方法耗时: " + optimizedTime + "ms, 结果: '" + resultOptimized + "'");
        System.out.println("结果一致性: " + (resultArray == resultHashMap && resultHashMap == resultLinkedHashMap && resultLinkedHashMap == resultOptimized ? "通过" : "失败"));
        
        System.out.println("所有测试完成");
    }
}