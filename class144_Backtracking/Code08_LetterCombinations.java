package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 17. 电话号码的字母组合
 * 
 * 题目描述：
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 答案可以按任意顺序返回。
 * 
 * 示例：
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * 
 * 输入：digits = ""
 * 输出：[]
 * 
 * 输入：digits = "2"
 * 输出：["a","b","c"]
 * 
 * 提示：
 * 0 <= digits.length <= 4
 * digits[i] 是范围 ['2', '9'] 的一个数字。
 * 
 * 链接：https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
 */
public class Code08_LetterCombinations {

    /**
     * 生成电话号码的字母组合
     * 
     * 算法思路：
     * 1. 使用回溯算法生成所有可能的字母组合
     * 2. 建立数字到字母的映射关系
     * 3. 对于每个数字，遍历其对应的所有字母
     * 4. 递归处理下一个数字，直到处理完所有数字
     * 
     * 时间复杂度：O(3^m * 4^n)，其中m是对应3个字母的数字个数，n是对应4个字母的数字个数
     * 空间复杂度：O(3^m * 4^n)，用于存储所有组合
     * 
     * @param digits 输入的数字字符串
     * @return 所有可能的字母组合
     */
    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        // 边界条件：空字符串
        if (digits == null || digits.length() == 0) return result;
        
        // 数字到字母的映射
        String[] mapping = new String[] {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        
        // 回溯生成所有组合
        backtrack(digits, mapping, result, "", 0);
        return result;
    }

    /**
     * 回溯函数生成字母组合
     * 
     * @param digits 输入的数字字符串
     * @param mapping 数字到字母的映射数组
     * @param result 结果列表
     * @param current 当前已生成的字符串
     * @param index 当前处理的数字索引
     */
    private static void backtrack(String digits, String[] mapping, List<String> result, String current, int index) {
        // 终止条件：已处理完所有数字
        if (index == digits.length()) {
            result.add(current);
            return;
        }
        
        // 获取当前数字对应的字母
        int digit = digits.charAt(index) - '0';
        String letters = mapping[digit];
        
        // 遍历所有可能的字母
        for (int i = 0; i < letters.length(); i++) {
            char letter = letters.charAt(i);
            // 递归处理下一个数字
            backtrack(digits, mapping, result, current + letter, index + 1);
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String test1 = "23";
        List<String> result1 = letterCombinations(test1);
        System.out.println("输入: \"" + test1 + "\"");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        String test2 = "";
        List<String> result2 = letterCombinations(test2);
        System.out.println("\n输入: \"" + test2 + "\"");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        String test3 = "2";
        List<String> result3 = letterCombinations(test3);
        System.out.println("\n输入: \"" + test3 + "\"");
        System.out.println("输出: " + result3);
        
        // 测试用例4
        String test4 = "234";
        List<String> result4 = letterCombinations(test4);
        System.out.println("\n输入: \"" + test4 + "\"");
        System.out.println("输出: " + result4);
    }
}