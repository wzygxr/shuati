package class060;

// Passcode derivation - Project Euler Problem 79
// 给定一系列登录尝试的密码片段，推断出最短的可能密码
// 测试链接 : https://projecteuler.net/problem=79
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是Project Euler的第79题，通过给定的密码片段推断最短可能密码。
 * 本质上是一个拓扑排序问题，需要确定数字的相对顺序。
 * 
 * 算法思路：
 * 1. 从密码片段中提取数字间的顺序关系
 * 2. 构建有向图，边表示数字间的先后顺序
 * 3. 使用拓扑排序确定数字的排列顺序
 * 4. 输出字典序最小的拓扑序列
 * 
 * 时间复杂度：O(n)，其中n是密码片段的数量
 * 空间复杂度：O(10^2) = O(1)，因为只有10个数字
 * 
 * 相关题目扩展：
 * 1. Project Euler Problem 79: Passcode derivation - https://projecteuler.net/problem=79
 * 2. LeetCode 269. 火星词典 - https://leetcode.cn/problems/alien-dictionary/
 * 3. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 
 * 工程化考虑：
 * 1. 关系提取：从密码片段中提取数字顺序
 * 2. 图构建：建立数字间的依赖关系
 * 3. 拓扑排序：确定数字的排列顺序
 * 4. 结果验证：确保密码满足所有片段约束
 */
public class Code18_PasscodeDerivation {

    /**
     * 推断最短可能密码
     * @param attempts 登录尝试的密码片段数组
     * @return 最短的可能密码
     */
    public static String derivePasscode(String[] attempts) {
        // 构建图：10个数字（0-9）
        boolean[][] graph = new boolean[10][10];
        int[] indegree = new int[10];
        boolean[] exists = new boolean[10];
        
        // 从密码片段中提取顺序关系
        for (String attempt : attempts) {
            char[] digits = attempt.toCharArray();
            
            // 标记存在的数字
            for (char digit : digits) {
                exists[digit - '0'] = true;
            }
            
            // 提取顺序关系：digits[i] 在 digits[j] 之前 (i < j)
            for (int i = 0; i < digits.length; i++) {
                for (int j = i + 1; j < digits.length; j++) {
                    int u = digits[i] - '0';
                    int v = digits[j] - '0';
                    
                    if (!graph[u][v]) {
                        graph[u][v] = true;
                        indegree[v]++;
                    }
                }
            }
        }
        
        // 拓扑排序（使用最小堆实现字典序最小）
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int i = 0; i < 10; i++) {
            if (exists[i] && indegree[i] == 0) {
                minHeap.offer(i);
            }
        }
        
        StringBuilder passcode = new StringBuilder();
        while (!minHeap.isEmpty()) {
            int u = minHeap.poll();
            passcode.append(u);
            
            for (int v = 0; v < 10; v++) {
                if (graph[u][v]) {
                    if (--indegree[v] == 0) {
                        minHeap.offer(v);
                    }
                }
            }
        }
        
        return passcode.toString();
    }

    public static void main(String[] args) {
        // Project Euler官方示例
        String[] attempts = {
            "319", "680", "180", "690", "129", "620", "762", "689", "762", "318", 
            "368", "710", "720", "710", "629", "168", "160", "689", "716", "731", 
            "736", "729", "316", "729", "729", "710", "769", "290", "719", "680", 
            "318", "389", "162", "289", "162", "718", "729", "319", "790", "680", 
            "890", "362", "319", "760", "316", "729", "380", "319", "728", "716"
        };
        
        String passcode = derivePasscode(attempts);
        System.out.println("推断出的密码: " + passcode);
        
        // 验证密码是否满足所有约束
        if (validatePasscode(passcode, attempts)) {
            System.out.println("密码验证通过");
        } else {
            System.out.println("密码验证失败");
        }
    }
    
    /**
     * 验证密码是否满足所有片段约束
     */
    private static boolean validatePasscode(String passcode, String[] attempts) {
        for (String attempt : attempts) {
            if (!isSubsequence(passcode, attempt)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查attempt是否是passcode的子序列（保持相对顺序）
     */
    private static boolean isSubsequence(String passcode, String attempt) {
        int i = 0, j = 0;
        while (i < passcode.length() && j < attempt.length()) {
            if (passcode.charAt(i) == attempt.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == attempt.length();
    }
}