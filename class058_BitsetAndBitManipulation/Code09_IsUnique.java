package class032;

import java.util.BitSet;

// LeetCode 面试题 01.01. 判定字符是否唯一
// 题目链接: https://leetcode-cn.com/problems/is-unique-lcci/
// 题目大意:
// 实现一个算法，确定一个字符串 s 的所有字符是否全都不同。
// 
// 示例 1:
// 输入: s = "leetcode"
// 输出: false
// 
// 示例 2:
// 输入: s = "abc"
// 输出: true
// 
// 限制:
// 0 <= len(s) <= 100
// 如果你不使用额外的数据结构，会很加分。
//
// 解题思路:
// 使用位运算(Bitset)优化的方法:
// 1. 由于字符集可能是ASCII或Unicode，但题目中通常假设是小写字母或有限范围
// 2. 我们可以使用一个整数或位集合来表示每个字符是否出现过
// 3. 对于每个字符，检查对应的位是否已经被设置，如果是则返回false，否则设置该位
//
// 时间复杂度: O(n)，其中n是字符串的长度
// 空间复杂度: O(1)，使用了固定大小的位集合
public class Code09_IsUnique {

    // 方法1: 使用Java内置的BitSet实现
    // 优点: 适用于任意字符集，不受字符范围限制
    // 参数astr: 待检查的字符串
    // 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
    public static boolean isUnique1(String astr) {
        // 边界条件检查
        // 如果字符串为null或为空，认为所有字符都唯一
        if (astr == null || astr.isEmpty()) {
            return true;
        }
        
        // 使用BitSet存储字符是否出现过
        // BitSet的大小为256，假设是ASCII字符集
        BitSet bitSet = new BitSet(256);
        
        // 遍历字符串中的每个字符
        for (int i = 0; i < astr.length(); i++) {
            // 获取第i个字符
            char c = astr.charAt(i);
            
            // 检查当前字符是否已经出现过
            // get(c)检查第c位是否为true
            if (bitSet.get(c)) {
                // 如果该字符已经出现过，说明有重复字符
                return false;
            }
            
            // 标记当前字符已经出现
            // set(c)将第c位设置为true
            bitSet.set(c);
        }
        
        // 所有字符都不重复
        return true;
    }
    
    // 方法2: 使用位运算模拟Bitset（仅适用于小写字母a-z）
    // 优点: 空间效率更高，仅使用一个整数
    // 参数astr: 待检查的字符串（仅包含小写字母a-z）
    // 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
    public static boolean isUnique2(String astr) {
        // 边界条件检查
        // 如果字符串为null或为空，认为所有字符都唯一
        if (astr == null || astr.isEmpty()) {
            return true;
        }
        
        // 使用一个整数的二进制位来存储字符出现情况
        // 假设输入只包含小写字母a-z，使用一个整数的低26位来表示
        int checker = 0;
        
        // 遍历字符串中的每个字符
        for (int i = 0; i < astr.length(); i++) {
            // 获取第i个字符
            char c = astr.charAt(i);
            
            // 如果不是小写字母，使用其他方法
            // 这是为了处理输入不符合假设的情况
            if (c < 'a' || c > 'z') {
                // 回退到BitSet方法，处理任意字符集
                return isUnique1(astr);
            }
            
            // 计算字符对应的位位置
            // c - 'a' 将字符映射到0-25的范围
            int bit = c - 'a';
            
            // 检查该位是否已经被设置
            // (checker & (1 << bit)) > 0 检查第bit位是否为1
            if ((checker & (1 << bit)) > 0) {
                // 如果该位已经为1，说明字符重复
                return false;
            }
            
            // 设置该位为1
            // checker |= (1 << bit) 将第bit位设置为1
            checker |= (1 << bit);
        }
        
        // 所有字符都不重复
        return true;
    }
    
    // 方法3: 不使用额外数据结构的方法（仅适用于有限字符集）
    // 思路: 对字符串进行排序，然后检查相邻字符是否相同
    // 缺点: 时间复杂度较高，且会修改原字符串（如果不是副本的话）
    // 参数astr: 待检查的字符串
    // 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
    public static boolean isUnique3(String astr) {
        // 边界条件检查
        // 如果字符串为null或为空，认为所有字符都唯一
        if (astr == null || astr.isEmpty()) {
            return true;
        }
        
        // 如果字符串长度超过字符集大小，必然有重复
        // 假设是ASCII字符集，最多有256个不同的字符
        if (astr.length() > 256) {
            // 根据鸽巢原理，长度超过字符集大小的字符串必然有重复字符
            return false;
        }
        
        // 转换为字符数组并排序
        // toCharArray()将字符串转换为字符数组
        char[] chars = astr.toCharArray();
        // 对字符数组进行排序
        java.util.Arrays.sort(chars);
        
        // 检查相邻字符是否相同
        for (int i = 0; i < chars.length - 1; i++) {
            // 如果相邻字符相同，说明有重复
            if (chars[i] == chars[i + 1]) {
                return false;
            }
        }
        
        // 所有字符都不重复
        return true;
    }
    
    // 工程化改进版本：增加参数验证和异常处理
    // 参数astr: 待检查的字符串
    // 返回值: 如果字符串中所有字符都唯一返回true，否则返回false
    public static boolean isUniqueWithValidation(String astr) {
        try {
            // 参数验证
            // 检查输入参数是否为null
            if (astr == null) {
                // 抛出异常，说明输入参数不能为null
                throw new IllegalArgumentException("Input string cannot be null");
            }
            
            // 如果字符串过长，提前返回false（鸽巢原理）
            // 假设使用的是Unicode字符集的一个子集，最多128个字符
            if (astr.length() > 128) {
                // 根据鸽巢原理，长度超过字符集大小的字符串必然有重复字符
                return false;
            }
            
            // 使用BitSet实现
            // 创建一个大小为128的BitSet，用于存储ASCII字符
            BitSet seen = new BitSet(128);
            
            // 遍历字符串中的每个字符
            for (int i = 0; i < astr.length(); i++) {
                // 获取第i个字符
                char c = astr.charAt(i);
                
                // 仅处理ASCII可见字符（可根据实际需求调整）
                // ASCII可见字符范围是32-126
                if (c >= 32 && c <= 126) {
                    // 检查当前字符是否已经出现过
                    if (seen.get(c)) {
                        // 如果该字符已经出现过，说明有重复字符
                        return false;
                    }
                    // 标记当前字符已经出现
                    seen.set(c);
                }
            }
            
            // 所有字符都不重复
            return true;
        } catch (Exception e) {
            // 记录异常日志（在实际应用中）
            // 在生产环境中，应该使用日志框架记录异常
            System.err.println("Error in isUnique: " + e.getMessage());
            // 异常情况下保守返回false
            return false;
        }
    }
    
    // 单元测试方法
    // 验证各种实现方法的正确性
    public static void runTests() {
        // 测试用例1: 普通字符串，有重复字符
        String test1 = "leetcode";
        System.out.println("Test 1: " + test1 + " -> " + isUnique1(test1) + " (Expected: false)");
        
        // 测试用例2: 普通字符串，无重复字符
        String test2 = "abc";
        System.out.println("Test 2: " + test2 + " -> " + isUnique1(test2) + " (Expected: true)");
        
        // 测试用例3: 空字符串
        String test3 = "";
        System.out.println("Test 3: Empty string -> " + isUnique1(test3) + " (Expected: true)");
        
        // 测试用例4: 包含大小写字母
        String test4 = "AbCdEfG";
        System.out.println("Test 4: " + test4 + " -> " + isUnique1(test4) + " (Expected: true)");
        
        // 测试用例5: 边界情况，只有一个字符
        String test5 = "a";
        System.out.println("Test 5: " + test5 + " -> " + isUnique1(test5) + " (Expected: true)");
        
        // 测试不同方法的结果一致性
        System.out.println("\nMethod comparison:");
        System.out.println("isUnique1(\"abc\"): " + isUnique1("abc"));
        System.out.println("isUnique2(\"abc\"): " + isUnique2("abc"));
        System.out.println("isUnique3(\"abc\"): " + isUnique3("abc"));
        System.out.println("isUniqueWithValidation(\"abc\"): " + isUniqueWithValidation("abc"));
    }
    
    // 主函数，程序入口点
    public static void main(String[] args) {
        System.out.println("LeetCode 面试题 01.01. 判定字符是否唯一");
        System.out.println("使用位运算优化实现");
        
        // 运行单元测试
        runTests();
        
        // 性能测试
        System.out.println("\nPerformance Testing:");
        
        // 生成一个较长的唯一字符串
        // 创建一个包含所有小写字母的字符串
        StringBuilder sb = new StringBuilder();
        for (char c = 'a'; c <= 'z'; c++) {
            sb.append(c);
        }
        String longUniqueStr = sb.toString();
        
        // 测试性能
        long startTime, endTime;
        
        // 测试方法1性能
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            isUnique1(longUniqueStr);
        }
        endTime = System.nanoTime();
        System.out.println("Method 1 average time: " + ((endTime - startTime) / 10000) + " ns");
        
        // 测试方法2性能（仅小写字母）
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            isUnique2(longUniqueStr);
        }
        endTime = System.nanoTime();
        System.out.println("Method 2 average time: " + ((endTime - startTime) / 10000) + " ns");
        
        // 复杂度分析
        System.out.println("\n复杂度分析:");
        System.out.println("时间复杂度: O(n)，其中n是字符串的长度");
        System.out.println("空间复杂度: O(1)，使用了固定大小的位集合或整数");
    }
}