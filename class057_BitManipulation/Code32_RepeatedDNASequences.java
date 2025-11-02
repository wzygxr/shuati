/**
 * 重复的DNA序列
 * 测试链接：https://leetcode.cn/problems/repeated-dna-sequences/
 * 
 * 题目描述：
 * DNA序列 由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
 * 例如，"ACGAATTCCG" 是一个 DNA序列 。
 * 在研究 DNA 时，识别 DNA 中的重复序列非常有用。
 * 给定一个表示 DNA序列 的字符串 s ，返回所有在 DNA 分子中出现不止一次的 长度为 10 的序列(子字符串)。你可以按 任意顺序 返回答案。
 * 
 * 解题思路：
 * 1. 哈希表法：使用HashMap统计所有10位子串的出现次数
 * 2. 位运算优化：使用2位表示一个字符，将字符串转换为整数
 * 3. 滑动窗口：使用滑动窗口和位运算结合
 * 4. Rabin-Karp算法：使用滚动哈希优化
 * 
 * 时间复杂度：O(n) - n为字符串长度
 * 空间复杂度：O(n) - 需要存储哈希表
 */
import java.util.*;

public class Code32_RepeatedDNASequences {
    
    /**
     * 方法1：哈希表法（直接使用字符串）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public List<String> findRepeatedDnaSequences1(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }
        
        Map<String, Integer> countMap = new HashMap<>();
        
        // 遍历所有长度为10的子串
        for (int i = 0; i <= s.length() - 10; i++) {
            String substring = s.substring(i, i + 10);
            countMap.put(substring, countMap.getOrDefault(substring, 0) + 1);
        }
        
        // 收集出现次数大于1的子串
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(entry.getKey());
            }
        }
        
        return result;
    }
    
    /**
     * 方法2：位运算优化（使用整数表示子串）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public List<String> findRepeatedDnaSequences2(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }
        
        // 字符到2位编码的映射
        Map<Character, Integer> charToCode = new HashMap<>();
        charToCode.put('A', 0);  // 00
        charToCode.put('C', 1);  // 01
        charToCode.put('G', 2);  // 10
        charToCode.put('T', 3);  // 11
        
        Map<Integer, Integer> countMap = new HashMap<>();
        
        // 第一个窗口的编码
        int code = 0;
        for (int i = 0; i < 10; i++) {
            code = (code << 2) | charToCode.get(s.charAt(i));
        }
        countMap.put(code, 1);
        
        // 滑动窗口处理剩余部分
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符（左移2位，然后取低20位）
            code = ((code << 2) | charToCode.get(s.charAt(i))) & 0xFFFFF;
            countMap.put(code, countMap.getOrDefault(code, 0) + 1);
        }
        
        // 重新遍历字符串，将编码转换回字符串
        Map<Integer, String> codeToString = new HashMap<>();
        for (int i = 0; i <= s.length() - 10; i++) {
            int currentCode = 0;
            for (int j = 0; j < 10; j++) {
                currentCode = (currentCode << 2) | charToCode.get(s.charAt(i + j));
            }
            codeToString.put(currentCode, s.substring(i, i + 10));
        }
        
        // 收集结果
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(codeToString.get(entry.getKey()));
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：滑动窗口+位运算（优化版）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public List<String> findRepeatedDnaSequences3(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }
        
        // 字符到2位编码的映射
        Map<Character, Integer> charToCode = new HashMap<>();
        charToCode.put('A', 0);
        charToCode.put('C', 1);
        charToCode.put('G', 2);
        charToCode.put('T', 3);
        
        Set<Integer> seen = new HashSet<>();
        Set<String> output = new HashSet<>();
        
        int code = 0;
        // 处理前10个字符
        for (int i = 0; i < 10; i++) {
            code = (code << 2) | charToCode.get(s.charAt(i));
        }
        seen.add(code);
        
        // 滑动窗口
        for (int i = 10; i < s.length(); i++) {
            code = ((code << 2) | charToCode.get(s.charAt(i))) & 0xFFFFF;
            if (seen.contains(code)) {
                output.add(s.substring(i - 9, i + 1));
            } else {
                seen.add(code);
            }
        }
        
        return new ArrayList<>(output);
    }
    
    /**
     * 方法4：Rabin-Karp算法（滚动哈希）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public List<String> findRepeatedDnaSequences4(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }
        
        // 使用质数作为基数
        int base = 4;  // 4个字符
        long mod = (long)1e9 + 7;  // 大质数取模
        
        // 字符到数字的映射
        Map<Character, Integer> charToNum = new HashMap<>();
        charToNum.put('A', 0);
        charToNum.put('C', 1);
        charToNum.put('G', 2);
        charToNum.put('T', 3);
        
        // 计算base^9 mod mod
        long highestPower = 1;
        for (int i = 0; i < 9; i++) {
            highestPower = (highestPower * base) % mod;
        }
        
        Map<Long, Integer> countMap = new HashMap<>();
        
        // 计算第一个窗口的哈希值
        long hash = 0;
        for (int i = 0; i < 10; i++) {
            hash = (hash * base + charToNum.get(s.charAt(i))) % mod;
        }
        countMap.put(hash, 1);
        
        // 滑动窗口计算哈希值
        for (int i = 10; i < s.length(); i++) {
            // 移除最左边的字符
            long leftCharValue = charToNum.get(s.charAt(i - 10)) * highestPower % mod;
            hash = (hash - leftCharValue + mod) % mod;
            // 添加新的字符
            hash = (hash * base + charToNum.get(s.charAt(i))) % mod;
            
            countMap.put(hash, countMap.getOrDefault(hash, 0) + 1);
        }
        
        // 收集结果
        Set<String> added = new HashSet<>();
        for (int i = 0; i <= s.length() - 10; i++) {
            String substring = s.substring(i, i + 10);
            long currentHash = 0;
            for (int j = 0; j < 10; j++) {
                currentHash = (currentHash * base + charToNum.get(s.charAt(i + j))) % mod;
            }
            if (countMap.get(currentHash) > 1 && !added.contains(substring)) {
                result.add(substring);
                added.add(substring);
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code32_RepeatedDNASequences solution = new Code32_RepeatedDNASequences();
        
        // 测试用例1：正常情况
        String s1 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
        List<String> result1 = solution.findRepeatedDnaSequences1(s1);
        List<String> result2 = solution.findRepeatedDnaSequences2(s1);
        List<String> result3 = solution.findRepeatedDnaSequences3(s1);
        List<String> result4 = solution.findRepeatedDnaSequences4(s1);
        System.out.println("测试用例1 - 输入: " + s1);
        System.out.println("方法1结果: " + result1 + " (预期: [AAAAACCCCC, CCCCCAAAAA])");
        System.out.println("方法2结果: " + result2 + " (预期: [AAAAACCCCC, CCCCCAAAAA])");
        System.out.println("方法3结果: " + result3 + " (预期: [AAAAACCCCC, CCCCCAAAAA])");
        System.out.println("方法4结果: " + result4 + " (预期: [AAAAACCCCC, CCCCCAAAAA])");
        
        // 测试用例2：边界情况（无重复）
        String s2 = "AAAAAAAAAA";
        List<String> result5 = solution.findRepeatedDnaSequences2(s2);
        System.out.println("测试用例2 - 输入: " + s2);
        System.out.println("方法2结果: " + result5 + " (预期: [])");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 哈希表法:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法2 - 位运算优化:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法3 - 滑动窗口+位运算:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法4 - Rabin-Karp算法:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 实际工程：方法2（位运算优化）最优");
        System.out.println("   - 面试场景：方法2（位运算优化）最优");
        System.out.println("2. 性能优化：避免字符串比较，使用整数编码");
        System.out.println("3. 边界处理：处理字符串长度不足的情况");
        System.out.println("4. 内存优化：使用位运算减少内存占用");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 位编码：使用2位表示一个字符，节省空间");
        System.out.println("2. 滑动窗口：高效处理固定长度子串");
        System.out.println("3. 哈希优化：使用整数编码替代字符串比较");
        System.out.println("4. 滚动哈希：Rabin-Karp算法处理字符串匹配");
    }
}