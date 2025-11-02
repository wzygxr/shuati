package class_advanced_algorithms.hashing;

import java.util.*;

/**
 * LeetCode 187. 重复的DNA序列
 * 
 * 题目描述：
 * 所有DNA都由一系列缩写为'A'，'C'，'G'和'T'的核苷酸组成，例如：'ACGAATTCCG'。
 * 在研究DNA时，识别DNA中的重复序列有时会对研究非常有帮助。
 * 编写一个函数来找出DNA分子中所有出现不止一次的长度为10的序列（子串）。
 * 
 * 解题思路：
 * 使用滑动窗口和字符串哈希。维护一个长度为10的滑动窗口，计算每个子串的哈希值，
 * 用哈希表统计出现次数。由于DNA序列只包含4个字符，可以使用4进制编码优化。
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)，用于存储哈希表
 */
public class LeetCode187 {
    
    /**
     * 找出DNA中所有重复的长度为10的序列
     * @param s DNA序列字符串
     * @return 所有重复的序列列表
     */
    public List<String> findRepeatedDnaSequences(String s) {
        List<String> result = new ArrayList<>();
        
        // 如果字符串长度小于10，不可能有长度为10的子串
        if (s.length() < 10) {
            return result;
        }
        
        // 使用Map统计每个哈希值出现的次数
        Map<Integer, Integer> hashCount = new HashMap<>();
        
        // 将字符映射为数字
        Map<Character, Integer> charMap = new HashMap<>();
        charMap.put('A', 0);
        charMap.put('C', 1);
        charMap.put('G', 2);
        charMap.put('T', 3);
        
        // 计算4^9，用于滑动窗口时的哈希值更新
        int base = 4;
        int power = (int) Math.pow(base, 9);
        
        // 计算第一个长度为10的子串的哈希值
        int hash = 0;
        for (int i = 0; i < 10; i++) {
            hash = hash * base + charMap.get(s.charAt(i));
        }
        hashCount.put(hash, 1);
        
        // 滑动窗口，计算后续子串的哈希值
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符，添加新字符
            hash = hash - charMap.get(s.charAt(i - 10)) * power;
            hash = hash * base + charMap.get(s.charAt(i));
            
            // 统计哈希值出现次数
            int count = hashCount.getOrDefault(hash, 0);
            hashCount.put(hash, count + 1);
            
            // 如果某个哈希值出现次数达到2，说明找到了重复序列
            // 只在第一次发现重复时添加到结果中，避免重复添加
            if (count == 1) {
                result.add(s.substring(i - 9, i + 1));
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LeetCode187 solution = new LeetCode187();
        
        // 测试用例1
        String s1 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        System.out.println("输入: " + s1);
        System.out.println("输出: " + solution.findRepeatedDnaSequences(s1));
        // 预期输出: ["AAAAACCCCC","CCCCCAAAAA"]
        
        // 测试用例2
        String s2 = "AAAAAAAAAAAAA";
        System.out.println("输入: " + s2);
        System.out.println("输出: " + solution.findRepeatedDnaSequences(s2));
        // 预期输出: ["AAAAAAAAAA"]
        
        // 测试用例3
        String s3 = "AAAAAAAAAAA";
        System.out.println("输入: " + s3);
        System.out.println("输出: " + solution.findRepeatedDnaSequences(s3));
        // 预期输出: ["AAAAAAAAAA"]
    }
}