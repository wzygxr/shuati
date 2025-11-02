package class038;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 93. 复原 IP 地址
 * 
 * 题目描述：
 * 有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔。
 * 例如："0.1.2.201" 和 "192.168.1.1" 是有效 IP 地址，
 * 但是 "0.011.255.245"、"192.168.1.312" 和 "192.168@1.1" 是无效 IP 地址。
 * 给定一个只包含数字的字符串 s ，用以表示一个 IP 地址，返回所有可能的有效 IP 地址。
 * 
 * 示例：
 * 输入：s = "25525511135"
 * 输出：["255.255.11.135","255.255.111.35"]
 * 
 * 输入：s = "0000"
 * 输出：["0.0.0.0"]
 * 
 * 输入：s = "101023"
 * 输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 * 
 * 提示：
 * 1 <= s.length <= 20
 * s 仅由数字组成
 * 
 * 链接：https://leetcode.cn/problems/restore-ip-addresses/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的IP地址分割方案
 * 2. IP地址由4个部分组成，每个部分必须是0-255之间的整数
 * 3. 不能有前导0（除非数字本身就是0）
 * 4. 通过递归分割字符串，检查每个分割部分是否合法
 * 
 * 时间复杂度：O(3^4) = O(81)，因为每个部分最多3位数字，共4个部分
 * 空间复杂度：O(n)，递归栈深度
 */
public class Code24_RestoreIPAddresses {

    /**
     * 复原IP地址
     * 
     * @param s 输入字符串
     * @return 所有可能的有效IP地址
     */
    public static List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();
        // 边界条件检查
        if (s == null || s.length() < 4 || s.length() > 12) {
            return result;
        }
        backtrack(s, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成IP地址
     * 
     * @param s 输入字符串
     * @param start 当前起始位置
     * @param path 当前路径（已分割的部分）
     * @param result 结果列表
     */
    private static void backtrack(String s, int start, List<String> path, List<String> result) {
        // 终止条件：已分割成4部分且处理完所有字符
        if (path.size() == 4) {
            if (start == s.length()) {
                result.add(String.join(".", path));
            }
            return;
        }
        
        // 尝试分割1-3个字符
        for (int len = 1; len <= 3; len++) {
            if (start + len > s.length()) {
                break;
            }
            
            String segment = s.substring(start, start + len);
            
            // 检查分割部分是否合法
            if (isValidSegment(segment)) {
                path.add(segment);
                backtrack(s, start + len, path, result);
                path.remove(path.size() - 1);  // 回溯
            }
        }
    }

    /**
     * 检查IP地址分段是否合法
     * 
     * @param segment IP地址分段
     * @return 是否合法
     */
    private static boolean isValidSegment(String segment) {
        // 长度检查
        if (segment.length() == 0 || segment.length() > 3) {
            return false;
        }
        
        // 前导0检查：如果长度大于1且以0开头，不合法
        if (segment.length() > 1 && segment.charAt(0) == '0') {
            return false;
        }
        
        // 数值范围检查：必须在0-255之间
        int value = Integer.parseInt(segment);
        return value >= 0 && value <= 255;
    }

    /**
     * 解法二：使用迭代法生成所有可能的分割方案
     * 通过三层循环枚举所有可能的分割点
     * 
     * @param s 输入字符串
     * @return 所有可能的有效IP地址
     */
    public static List<String> restoreIpAddresses2(String s) {
        List<String> result = new ArrayList<>();
        int n = s.length();
        
        // 枚举三个分割点的位置
        for (int i = 1; i <= 3 && i <= n - 3; i++) {
            for (int j = i + 1; j <= i + 3 && j <= n - 2; j++) {
                for (int k = j + 1; k <= j + 3 && k <= n - 1; k++) {
                    String seg1 = s.substring(0, i);
                    String seg2 = s.substring(i, j);
                    String seg3 = s.substring(j, k);
                    String seg4 = s.substring(k);
                    
                    if (isValidSegment(seg1) && isValidSegment(seg2) && 
                        isValidSegment(seg3) && isValidSegment(seg4)) {
                        result.add(seg1 + "." + seg2 + "." + seg3 + "." + seg4);
                    }
                }
            }
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "25525511135";
        List<String> result1 = restoreIpAddresses(s1);
        System.out.println("输入: s = \"" + s1 + "\"");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        String s2 = "0000";
        List<String> result2 = restoreIpAddresses(s2);
        System.out.println("\n输入: s = \"" + s2 + "\"");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        String s3 = "101023";
        List<String> result3 = restoreIpAddresses(s3);
        System.out.println("\n输入: s = \"" + s3 + "\"");
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<String> result4 = restoreIpAddresses2(s1);
        System.out.println("输入: s = \"" + s1 + "\"");
        System.out.println("输出: " + result4);
    }
}