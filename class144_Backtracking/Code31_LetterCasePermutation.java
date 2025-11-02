package class038;

import java.util.*;

/**
 * LeetCode 784. 字母大小写全排列
 * 
 * 给定一个字符串S，通过将字符串S中的每个字母转变大小写，我们可以获得一个新的字符串。
 * 返回所有可能得到的字符串集合。以任意顺序返回输出。
 * 
 * 算法思路：
 * 使用回溯算法遍历字符串中的每个字符，对于字母字符，分别尝试大写和小写两种情况。
 * 
 * 时间复杂度：O(2^n * n)，其中n是字符串中字母的个数
 * 空间复杂度：O(2^n * n)，用于存储所有可能的字符串
 */
public class Code31_LetterCasePermutation {
    
    /**
     * 返回所有可能得到的字符串集合
     * @param s 输入字符串
     * @return 所有可能的字符串集合
     */
    public List<String> letterCasePermutation(String s) {
        List<String> result = new ArrayList<>();
        char[] chars = s.toCharArray();
        backtrack(chars, 0, result);
        return result;
    }
    
    /**
     * 回溯函数
     * @param chars 字符数组
     * @param index 当前处理的字符位置
     * @param result 结果列表
     */
    private void backtrack(char[] chars, int index, List<String> result) {
        // 终止条件：处理完所有字符
        if (index == chars.length) {
            result.add(new String(chars));
            return;
        }
        
        char ch = chars[index];
        
        // 如果是字母，则分别尝试大写和小写
        if (Character.isLetter(ch)) {
            // 尝试小写
            chars[index] = Character.toLowerCase(ch);
            backtrack(chars, index + 1, result);
            
            // 尝试大写
            chars[index] = Character.toUpperCase(ch);
            backtrack(chars, index + 1, result);
        } else {
            // 如果不是字母，直接处理下一个字符
            backtrack(chars, index + 1, result);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code31_LetterCasePermutation solution = new Code31_LetterCasePermutation();
        
        // 测试用例1
        System.out.println("Input: \"a1b2\"");
        System.out.println(solution.letterCasePermutation("a1b2"));
        
        // 测试用例2
        System.out.println("\nInput: \"3z4\"");
        System.out.println(solution.letterCasePermutation("3z4"));
        
        // 测试用例3
        System.out.println("\nInput: \"12345\"");
        System.out.println(solution.letterCasePermutation("12345"));
    }
}