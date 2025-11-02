package class032;

import java.util.ArrayList;
import java.util.List;

// LeetCode 1239. 串联字符串的最大长度
// 题目链接: https://leetcode-cn.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/
// 题目大意:
// 给定一个字符串数组 arr，字符串 s 是将 arr 某一子序列字符串连接所得的字符串，如果 s 中的每个字符都只出现过一次，
// 请返回所有可能的 s 中最长长度。
// 
// 示例 1:
// 输入：arr = ["un","iq","ue"]
// 输出：4
// 解释：所有可能的串联组合是 "","un","iq","ue","uniq" 和 "ique"，最大长度为 4。
// 
// 示例 2:
// 输入：arr = ["cha","r","act","ers"]
// 输出：6
// 解释：可能的解答是 "chaers"，长度为 6。
// 
// 示例 3:
// 输入：arr = ["abcdefghijklmnopqrstuvwxyz"]
// 输出：26
// 
// 提示：
// 1 <= arr.length <= 16
// 1 <= arr[i].length <= 26
// arr[i] 中只含有小写英文字母
//
// 解题思路:
// 由于题目中要求字符串中的每个字符只出现一次，我们可以使用位掩码来表示每个字符串中包含的字符。
// 对于长度为16的数组，我们可以使用回溯算法或动态规划来求解最长长度。
// 这里我们使用位运算优化，通过掩码来判断字符是否重复。

public class Code11_MaxLength {

    // 方法一：回溯算法 + 位运算
    public static int maxLength1(List<String> arr) {
        // 边界条件检查
        if (arr == null || arr.isEmpty()) {
            return 0;
        }
        
        // 过滤掉自身包含重复字符的字符串
        List<Integer> masks = new ArrayList<>();
        for (String s : arr) {
            int mask = 0;
            boolean isValid = true;
            
            for (char c : s.toCharArray()) {
                int bit = 1 << (c - 'a');
                // 检查当前字符是否已经在mask中设置
                if ((mask & bit) != 0) {
                    isValid = false;
                    break;
                }
                mask |= bit;
            }
            
            if (isValid) {
                masks.add(mask);
            }
        }
        
        // 调用回溯函数
        return backtrack(masks, 0, 0);
    }
    
    // 回溯函数
    // masks: 所有有效字符串的位掩码列表
    // index: 当前处理到的字符串索引
    // currentMask: 当前已选字符串的位掩码
    private static int backtrack(List<Integer> masks, int index, int currentMask) {
        // 基本情况：已经处理完所有字符串
        if (index == masks.size()) {
            // 计算currentMask中设置的位的数量，即字符数量
            return Integer.bitCount(currentMask);
        }
        
        // 不选当前字符串
        int maxLength = backtrack(masks, index + 1, currentMask);
        
        // 选当前字符串（如果不会导致重复字符）
        int currentMaskVal = masks.get(index);
        if ((currentMask & currentMaskVal) == 0) {  // 没有共同的字符
            maxLength = Math.max(maxLength, backtrack(masks, index + 1, currentMask | currentMaskVal));
        }
        
        return maxLength;
    }
    
    // 方法二：动态规划 + 位运算
    public static int maxLength2(List<String> arr) {
        // 边界条件检查
        if (arr == null || arr.isEmpty()) {
            return 0;
        }
        
        // 过滤掉自身包含重复字符的字符串，并转换为位掩码
        List<Integer> validMasks = new ArrayList<>();
        for (String s : arr) {
            int mask = 0;
            boolean isValid = true;
            
            for (char c : s.toCharArray()) {
                int bit = 1 << (c - 'a');
                if ((mask & bit) != 0) {
                    isValid = false;
                    break;
                }
                mask |= bit;
            }
            
            if (isValid && mask != 0) {  // 确保mask不为0（空字符串被过滤）
                validMasks.add(mask);
            }
        }
        
        // 动态规划：dp存储所有可能的有效掩码组合
        List<Integer> dp = new ArrayList<>();
        dp.add(0);  // 初始状态：空字符串
        
        int maxLen = 0;
        
        // 对于每个有效的字符串掩码
        for (int mask : validMasks) {
            // 创建一个临时列表，存储新的组合
            List<Integer> temp = new ArrayList<>();
            
            // 对于当前的所有组合
            for (int existingMask : dp) {
                // 如果当前mask和已有mask没有重叠的位
                if ((existingMask & mask) == 0) {
                    int combinedMask = existingMask | mask;
                    temp.add(combinedMask);
                    maxLen = Math.max(maxLen, Integer.bitCount(combinedMask));
                }
            }
            
            // 将新的组合添加到dp中
            dp.addAll(temp);
        }
        
        return maxLen;
    }
    
    // 方法三：优化的回溯算法
    public static int maxLength3(List<String> arr) {
        // 预处理：过滤无效字符串并转换为掩码
        List<Integer> masks = new ArrayList<>();
        for (String s : arr) {
            int mask = 0;
            boolean valid = true;
            
            for (char c : s.toCharArray()) {
                int bit = 1 << (c - 'a');
                if ((mask & bit) != 0) {
                    valid = false;
                    break;
                }
                mask |= bit;
            }
            
            if (valid) {
                masks.add(mask);
            }
        }
        
        // 创建长度数组，避免重复计算bitCount
        int[] lengths = new int[masks.size()];
        for (int i = 0; i < masks.size(); i++) {
            lengths[i] = Integer.bitCount(masks.get(i));
        }
        
        // 优化的回溯函数
        int[] result = {0};  // 使用数组作为引用传递
        optimizedBacktrack(masks, lengths, 0, 0, 0, result);
        return result[0];
    }
    
    private static void optimizedBacktrack(List<Integer> masks, int[] lengths, int index, 
                                          int currentMask, int currentLength, int[] result) {
        // 更新最大长度
        result[0] = Math.max(result[0], currentLength);
        
        // 剪枝：如果剩余的字符串即使全部选上也无法超过当前最大长度，提前返回
        int remainingMaxLength = currentLength;
        for (int i = index; i < masks.size(); i++) {
            if ((currentMask & masks.get(i)) == 0) {
                remainingMaxLength += lengths[i];
            }
        }
        
        if (remainingMaxLength <= result[0]) {
            return;  // 剪枝
        }
        
        // 回溯
        for (int i = index; i < masks.size(); i++) {
            if ((currentMask & masks.get(i)) == 0) {  // 没有共同的字符
                optimizedBacktrack(masks, lengths, i + 1, 
                                 currentMask | masks.get(i), 
                                 currentLength + lengths[i], result);
            }
        }
    }
    
    // 工程化改进版本：增加参数验证和异常处理
    public static int maxLengthWithValidation(List<String> arr) {
        try {
            // 参数验证
            if (arr == null) {
                throw new IllegalArgumentException("Input list cannot be null");
            }
            
            // 检查数组长度是否在题目限制范围内
            if (arr.size() > 16) {
                throw new IllegalArgumentException("Input list size exceeds maximum allowed length of 16");
            }
            
            // 验证每个字符串是否符合要求
            for (String s : arr) {
                if (s == null) {
                    throw new IllegalArgumentException("String elements cannot be null");
                }
                if (s.length() > 26) {
                    throw new IllegalArgumentException("String element exceeds maximum allowed length of 26");
                }
                // 检查是否只包含小写英文字母
                if (!s.matches("[a-z]++")) {
                    throw new IllegalArgumentException("String elements must contain only lowercase English letters");
                }
            }
            
            // 使用方法三实现
            return maxLength3(arr);
        } catch (Exception e) {
            // 记录异常日志（在实际应用中）
            System.err.println("Error in maxLength: " + e.getMessage());
            // 异常情况下返回0
            return 0;
        }
    }
    
    // 单元测试方法
    public static void runTests() {
        // 测试用例1
        List<String> arr1 = new ArrayList<>();
        arr1.add("un");
        arr1.add("iq");
        arr1.add("ue");
        System.out.println("Test 1: [\"un\",\"iq\",\"ue\"] -> " + maxLength2(arr1) + " (Expected: 4)");
        
        // 测试用例2
        List<String> arr2 = new ArrayList<>();
        arr2.add("cha");
        arr2.add("r");
        arr2.add("act");
        arr2.add("ers");
        System.out.println("Test 2: [\"cha\",\"r\",\"act\",\"ers\"] -> " + maxLength2(arr2) + " (Expected: 6)");
        
        // 测试用例3
        List<String> arr3 = new ArrayList<>();
        arr3.add("abcdefghijklmnopqrstuvwxyz");
        System.out.println("Test 3: [\"abcdefghijklmnopqrstuvwxyz\"] -> " + maxLength2(arr3) + " (Expected: 26)");
        
        // 测试用例4：包含无效字符串（自身有重复字符）
        List<String> arr4 = new ArrayList<>();
        arr4.add("abc");
        arr4.add("def");
        arr4.add("a");
        arr4.add("ghi");
        arr4.add("abb");  // 自身有重复字符，应被过滤
        System.out.println("Test 4: [\"abc\",\"def\",\"a\",\"ghi\",\"abb\"] -> " + maxLength2(arr4) + " (Expected: 9)");
        
        // 测试不同方法的结果一致性
        System.out.println("\nMethod comparison:");
        System.out.println("Test 1 results:");
        System.out.println("  maxLength1: " + maxLength1(arr1));
        System.out.println("  maxLength2: " + maxLength2(arr1));
        System.out.println("  maxLength3: " + maxLength3(arr1));
        System.out.println("  maxLengthWithValidation: " + maxLengthWithValidation(arr1));
    }
    
    // 性能测试方法
    public static void performanceTest() {
        // 生成测试数据：所有字母组合
        List<String> largeArr = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 生成不重复字符的字符串
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 5 && (i * 5 + j) < 26; j++) {
                sb.append((char) ('a' + i * 5 + j));
            }
            largeArr.add(sb.toString());
        }
        
        // 测试方法一时间
        long startTime = System.nanoTime();
        int result1 = maxLength1(largeArr);
        long endTime = System.nanoTime();
        System.out.println("\nPerformance of maxLength1: " + 
                          (endTime - startTime) / 1000 + " μs, Result: " + result1);
        
        // 测试方法二时间
        startTime = System.nanoTime();
        int result2 = maxLength2(largeArr);
        endTime = System.nanoTime();
        System.out.println("Performance of maxLength2: " + 
                          (endTime - startTime) / 1000 + " μs, Result: " + result2);
        
        // 测试方法三时间
        startTime = System.nanoTime();
        int result3 = maxLength3(largeArr);
        endTime = System.nanoTime();
        System.out.println("Performance of maxLength3: " + 
                          (endTime - startTime) / 1000 + " μs, Result: " + result3);
    }
    
    public static void main(String[] args) {
        System.out.println("LeetCode 1239. 串联字符串的最大长度");
        System.out.println("使用位运算优化实现");
        
        // 运行单元测试
        runTests();
        
        // 运行性能测试
        performanceTest();
        
        // 复杂度分析
        System.out.println("\n复杂度分析:");
        System.out.println("方法一（回溯算法）:");
        System.out.println("  时间复杂度: O(2^n)，其中n是有效字符串的数量（过滤后）");
        System.out.println("  空间复杂度: O(n)，递归调用栈的深度");
        
        System.out.println("\n方法二（动态规划）:");
        System.out.println("  时间复杂度: O(n * 2^m)，其中n是字符串数量，m是字符集大小（最多26）");
        System.out.println("  空间复杂度: O(2^m)，存储所有可能的掩码组合");
        
        System.out.println("\n方法三（优化的回溯算法）:");
        System.out.println("  时间复杂度: O(2^n)，但通过剪枝优化实际运行时间");
        System.out.println("  空间复杂度: O(n)");
        System.out.println("  优点: 利用剪枝减少不必要的计算，对于大多数情况效率更高");
        
        System.out.println("\n适用场景总结:");
        System.out.println("1. 当数组长度较小时，三种方法都可以使用");
        System.out.println("2. 当字符串包含大量重复字符时，方法三的剪枝效果更好");
        System.out.println("3. 当需要更稳定的性能时，方法二的动态规划更可靠");
        System.out.println("4. 在工程实践中，应根据具体数据特征选择合适的方法");
    }
}