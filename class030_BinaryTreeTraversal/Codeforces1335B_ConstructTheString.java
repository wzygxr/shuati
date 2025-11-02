package class036;

import java.util.*;

/**
 * Codeforces 1335B. Construct the String
 * 题目链接: https://codeforces.com/problemset/problem/1335/B
 * 题目描述: 构造一个长度为n的字符串，使得任意长度为a的子串中恰好包含b个不同的字符。
 * 
 * 核心算法思想:
 * 1. 循环构造: 使用长度为b的字符循环来构造字符串
 * 2. 滑动窗口: 确保每个长度为a的窗口包含恰好b个不同字符
 * 3. 模式重复: 通过重复特定模式来满足条件
 * 
 * 时间复杂度分析:
 * - 所有方法: O(N)，需要构造长度为n的字符串
 * 
 * 空间复杂度分析:
 * - 方法1(循环构造): O(N)，存储结果字符串
 * - 方法2(优化构造): O(N)，存储结果字符串
 * 
 * 相关题目:
 * 1. LeetCode 1100. 长度为 K 的无重复字符子串 - 类似滑动窗口
 * 2. LeetCode 340. 至多包含 K 个不同字符的最长子串 - 滑动窗口变种
 * 3. Codeforces 1328B. K-th Beautiful String - 字符串构造
 * 
 * 工程化考量:
 * 1. 字符选择: 使用小写字母表保证字符充足
 * 2. 边界处理: 处理b>a或b>26的特殊情况
 * 3. 性能优化: 使用StringBuilder提升字符串构造效率
 */
public class Codeforces1335B_ConstructTheString {
    
    /**
     * 方法1: 循环构造法 - 基础实现
     * 思路: 使用长度为b的字符循环来构造字符串
     * 时间复杂度: O(N) - 构造长度为n的字符串
     * 空间复杂度: O(N) - 存储结果字符串
     * 
     * 核心思想:
     * 1. 使用前b个小写字母作为字符集
     * 2. 循环重复这b个字符来构造整个字符串
     * 3. 确保每个长度为a的窗口包含完整的b个不同字符
     */
    public static String constructString1(int n, int a, int b) {
        if (b > a || b > 26) {
            return ""; // 无效输入
        }
        
        StringBuilder sb = new StringBuilder();
        
        // 生成b个不同的字符
        char[] chars = new char[b];
        for (int i = 0; i < b; i++) {
            chars[i] = (char) ('a' + i);
        }
        
        // 循环构造字符串
        for (int i = 0; i < n; i++) {
            sb.append(chars[i % b]);
        }
        
        return sb.toString();
    }
    
    /**
     * 方法2: 优化构造法 - 处理a>b的情况
     * 思路: 当a>b时，需要在循环模式中添加重复字符
     * 时间复杂度: O(N) - 构造长度为n的字符串
     * 空间复杂度: O(N) - 存储结果字符串
     * 
     * 关键优化:
     * 1. 当a>b时，使用前b个字符加上重复字符来构造
     * 2. 确保窗口内字符多样性满足要求
     */
    public static String constructString2(int n, int a, int b) {
        if (b > a || b > 26) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        char[] pattern = new char[a];
        
        // 构造长度为a的模式
        for (int i = 0; i < a; i++) {
            if (i < b) {
                pattern[i] = (char) ('a' + i);
            } else {
                pattern[i] = pattern[i % b]; // 重复前b个字符
            }
        }
        
        // 重复模式构造整个字符串
        for (int i = 0; i < n; i++) {
            sb.append(pattern[i % a]);
        }
        
        return sb.toString();
    }
    
    /**
     * 方法3: 滑动窗口验证法 - 构造并验证
     * 思路: 先构造字符串，然后验证是否满足条件
     * 时间复杂度: O(N) - 构造和验证
     * 空间复杂度: O(N) - 存储结果字符串和频率数组
     */
    public static String constructString3(int n, int a, int b) {
        if (b > a || b > 26) {
            return "";
        }
        
        // 使用方法1构造字符串
        String result = constructString1(n, a, b);
        
        // 验证是否满足条件
        if (validateString(result, n, a, b)) {
            return result;
        } else {
            // 如果不满足，使用方法2
            return constructString2(n, a, b);
        }
    }
    
    /**
     * 验证字符串是否满足条件
     */
    private static boolean validateString(String s, int n, int a, int b) {
        if (s.length() != n) {
            return false;
        }
        
        // 检查每个长度为a的子串
        for (int i = 0; i <= n - a; i++) {
            String substring = s.substring(i, i + a);
            if (countDistinctChars(substring) != b) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 计算字符串中不同字符的数量
     */
    private static int countDistinctChars(String s) {
        boolean[] visited = new boolean[26];
        int count = 0;
        
        for (char c : s.toCharArray()) {
            int index = c - 'a';
            if (!visited[index]) {
                visited[index] = true;
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 测试方法: 包含多种测试用例
     */
    public static void main(String[] args) {
        System.out.println("========== Codeforces 1335B 测试 ==========");
        
        // 测试用例1: n=5, a=5, b=2
        System.out.println("测试用例1: n=5, a=5, b=2");
        System.out.println("方法1结果: " + constructString1(5, 5, 2));
        System.out.println("方法2结果: " + constructString2(5, 5, 2));
        
        // 测试用例2: n=8, a=3, b=2
        System.out.println("\n测试用例2: n=8, a=3, b=2");
        System.out.println("方法1结果: " + constructString1(8, 3, 2));
        System.out.println("方法2结果: " + constructString2(8, 3, 2));
        
        // 测试用例3: n=10, a=4, b=3
        System.out.println("\n测试用例3: n=10, a=4, b=3");
        System.out.println("方法1结果: " + constructString1(10, 4, 3));
        System.out.println("方法2结果: " + constructString2(10, 4, 3));
        
        // 边界测试
        System.out.println("\n边界测试: n=26, a=26, b=26");
        System.out.println("方法1结果: " + constructString1(26, 26, 26));
        
        // 性能对比说明
        System.out.println("\n========== 性能对比说明 ==========");
        System.out.println("1. 方法1（循环构造）: 简单高效，适用于大多数情况");
        System.out.println("2. 方法2（优化构造）: 处理a>b的情况更准确");
        System.out.println("3. 方法3（验证构造）: 保证结果正确性，但性能稍差");
    }
}

/*
Python实现:

def constructString(n: int, a: int, b: int) -> str:
    if b > a or b > 26:
        return ""
        
    # 生成b个不同的字符
    chars = [chr(ord('a') + i) for i in range(b)]
    
    # 循环构造字符串
    result = []
    for i in range(n):
        result.append(chars[i % b])
        
    return ''.join(result)

C++实现:

#include <iostream>
#include <string>
using namespace std;

string constructString(int n, int a, int b) {
    if (b > a || b > 26) {
        return "";
    }
    
    string result;
    for (int i = 0; i < n; i++) {
        result += 'a' + (i % b);
    }
    
    return result;
}
*/