package class_advanced_algorithms.compression;

import java.util.*;

/**
 * LZW字典编码实现
 * 
 * LZW（Lempel-Ziv-Welch）是一种无损数据压缩算法，属于字典编码的一种。
 * 
 * 算法原理：
 * 1. 初始化字典，包含所有可能的单字符
 * 2. 读取输入字符串，查找字典中最长的匹配字符串
 * 3. 输出匹配字符串对应的编码
 * 4. 将匹配字符串加上下一个字符组成的新字符串添加到字典中
 * 5. 重复步骤2-4直到处理完所有输入
 * 
 * 时间复杂度：O(n)，其中n是输入字符串长度
 * 空间复杂度：O(d)，其中d是字典中条目的数量
 * 
 * 优势：
 * 1. 实现相对简单
 * 2. 压缩效果好，特别适合重复模式较多的数据
 * 3. 不需要预先知道数据的统计特性
 * 4. 编码和解码过程对称
 * 
 * 劣势：
 * 1. 需要维护字典，占用内存
 * 2. 对于随机数据压缩效果不佳
 * 3. 字典可能会变得很大
 * 
 * 应用场景：
 * 1. GIF图像格式
 * 2. TIFF图像格式
 * 3. Unix系统的compress工具
 */
public class LZWEncoding {
    
    // 初始字典大小（ASCII字符集）
    private static final int INITIAL_DICTIONARY_SIZE = 256;
    
    /**
     * LZW编码
     * @param input 输入字符串
     * @return 编码结果（整数列表）
     */
    public static List<Integer> encode(String input) {
        // 初始化字典
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < INITIAL_DICTIONARY_SIZE; i++) {
            dictionary.put("" + (char) i, i);
        }
        
        List<Integer> result = new ArrayList<>();
        int dictSize = INITIAL_DICTIONARY_SIZE;
        String current = "";
        
        for (char c : input.toCharArray()) {
            String combined = current + c;
            
            // 如果组合字符串在字典中，继续扩展
            if (dictionary.containsKey(combined)) {
                current = combined;
            } else {
                // 输出当前字符串的编码
                result.add(dictionary.get(current));
                
                // 将新字符串添加到字典
                dictionary.put(combined, dictSize++);
                
                // 重新开始
                current = "" + c;
            }
        }
        
        // 输出最后一个字符串
        if (!current.equals("")) {
            result.add(dictionary.get(current));
        }
        
        return result;
    }
    
    /**
     * LZW解码
     * @param encoded 编码结果（整数列表）
     * @return 解码结果（字符串）
     */
    public static String decode(List<Integer> encoded) {
        // 初始化字典
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < INITIAL_DICTIONARY_SIZE; i++) {
            dictionary.put(i, "" + (char) i);
        }
        
        StringBuilder result = new StringBuilder();
        String current = "";
        int dictSize = INITIAL_DICTIONARY_SIZE;
        
        for (int code : encoded) {
            String entry = "";
            
            if (dictionary.containsKey(code)) {
                entry = dictionary.get(code);
            } else if (code == dictSize) {
                // 特殊情况：处理字符串+首字符的重复情况
                entry = current + current.charAt(0);
            } else {
                throw new IllegalArgumentException("无效的编码: " + code);
            }
            
            result.append(entry);
            
            // 将新字符串添加到字典
            if (!current.equals("")) {
                dictionary.put(dictSize++, current + entry.charAt(0));
            }
            
            current = entry;
        }
        
        return result.toString();
    }
    
    /**
     * 计算压缩率
     * @param original 原始数据大小（字节）
     * @param compressed 压缩后数据大小（字节）
     * @return 压缩率（百分比）
     */
    public static double calculateCompressionRatio(int original, int compressed) {
        if (original == 0) return 0;
        return (1.0 - (double) compressed / original) * 100;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：包含重复模式的字符串
        String test1 = "ABABABA";
        System.out.println("原始字符串: " + test1);
        System.out.println("原始长度: " + test1.length() + " 字符");
        
        List<Integer> encoded1 = encode(test1);
        System.out.println("编码结果: " + encoded1);
        System.out.println("编码长度: " + encoded1.size() + " 个整数");
        
        String decoded1 = decode(encoded1);
        System.out.println("解码结果: " + decoded1);
        System.out.println("编码解码是否正确: " + test1.equals(decoded1));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test1.length() * 2, encoded1.size() * 2))); // 假设每个整数占2字节
        System.out.println();
        
        // 测试用例2：更复杂的字符串
        String test2 = "ABCABCABCABCABC";
        System.out.println("原始字符串: " + test2);
        System.out.println("原始长度: " + test2.length() + " 字符");
        
        List<Integer> encoded2 = encode(test2);
        System.out.println("编码结果: " + encoded2);
        System.out.println("编码长度: " + encoded2.size() + " 个整数");
        
        String decoded2 = decode(encoded2);
        System.out.println("解码结果: " + decoded2);
        System.out.println("编码解码是否正确: " + test2.equals(decoded2));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test2.length() * 2, encoded2.size() * 2)));
        System.out.println();
        
        // 测试用例3：无重复的字符串
        String test3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        System.out.println("原始字符串: " + test3);
        System.out.println("原始长度: " + test3.length() + " 字符");
        
        List<Integer> encoded3 = encode(test3);
        System.out.println("编码结果: " + encoded3);
        System.out.println("编码长度: " + encoded3.size() + " 个整数");
        
        String decoded3 = decode(encoded3);
        System.out.println("解码结果: " + decoded3);
        System.out.println("编码解码是否正确: " + test3.equals(decoded3));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test3.length() * 2, encoded3.size() * 2)));
    }
}