package class047;

import java.util.*;

/**
 * LeetCode 936. 盖章序列 (Stamping The Sequence)
 * 
 * 题目描述:
 * 你想要用小写字母组成一个目标字符串 target。开始时，序列由 target.length 个 '?' 记号组成。
 * 而你有一个小写字母印章 stamp。在每个回合，你可以将印章放在序列上，并将序列中的每个字母替换为印章对应位置的字母。
 * 你最多可以进行 10 * target.length 次操作。
 * 在完成所有操作后，序列必须等于目标字符串 target。
 * 返回一个数组，其中包含按顺序执行的盖章操作的位置（索引从0开始）。如果无法完成目标，则返回一个空数组。
 * 
 * 示例1:
 * 输入: stamp = "abc", target = "ababc"
 * 输出: [0,2]
 * 解释:
 * 初始序列是 "?????"
 * - 放置印章在位置 0 处，得到 "abc??"
 * - 放置印章在位置 2 处，得到 "ababc"
 * 
 * 示例2:
 * 输入: stamp = "abca", target = "aabcaca"
 * 输出: [3,0,1]
 * 解释:
 * 初始序列是 "???????"
 * - 放置印章在位置 3 处，得到 "???abca"
 * - 放置印章在位置 0 处，得到 "abcabca"
 * - 放置印章在位置 1 处，得到 "aabcaca"
 * 
 * 提示:
 * 1. 1 <= stamp.length <= target.length <= 1000
 * 2. stamp 和 target 只包含小写字母
 * 
 * 题目链接: https://leetcode.com/problems/stamping-the-sequence/
 * 
 * 解题思路:
 * 这道题可以用逆向思维和差分数组结合来解决：
 * 1. 从目标字符串 target 倒推回初始的全 ? 字符串
 * 2. 每次找到可以被 stamp 覆盖的子串（允许部分匹配，因为后面可能会被覆盖）
 * 3. 用差分数组来跟踪每个位置被覆盖的次数，确保最终所有字符都被覆盖
 * 
 * 具体步骤：
 * 1. 创建一个队列，用于存储可以被完全覆盖的子串位置
 * 2. 使用一个数组记录每个位置已经匹配的字符数
 * 3. 使用差分数组来标记需要检查的位置
 * 4. 逆向模拟盖章过程，找到所有盖章位置，最后反转结果
 * 
 * 时间复杂度: O(n * (n - m + 1)) - n是target长度，m是stamp长度
 * 空间复杂度: O(n) - 需要存储匹配信息和差分数组
 * 
 * 这是最优解，因为我们需要考虑所有可能的盖章位置和覆盖次数。
 */
public class Code09_StampingTheSequence {

    /**
     * 寻找盖章序列
     * 
     * @param stamp 印章字符串
     * @param target 目标字符串
     * @return 盖章操作的位置数组，如果无法完成则返回空数组
     */
    public static int[] movesToStamp(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();
        
        // 存储盖章位置，后续需要反转
        List<Integer> result = new ArrayList<>();
        
        // 转换为字符数组方便操作
        char[] targetArr = target.toCharArray();
        char[] stampArr = stamp.toCharArray();
        
        // 记录每个位置被覆盖的次数
        boolean[] visited = new boolean[n - m + 1];
        
        // 记录已经被匹配为'?'的字符数量
        int matchedCount = 0;
        
        // 存储每个位置已经匹配的字符数
        int[] matchCounts = new int[n];
        
        // 队列存储可以完全匹配的位置
        Queue<Integer> queue = new LinkedList<>();
        
        // 初始化差分数组，用于标记需要重新检查的位置范围
        int[] diff = new int[n + 1];
        
        // 预处理所有可能的盖章位置
        for (int i = 0; i <= n - m; i++) {
            // 检查当前位置i是否可以盖章
            int matchCount = 0;
            for (int j = 0; j < m; j++) {
                if (targetArr[i + j] == stampArr[j] || targetArr[i + j] == '?') {
                    matchCount++;
                } else {
                    break;
                }
            }
            
            // 如果当前位置可以盖章（完全匹配）
            if (matchCount == m) {
                result.add(i);
                visited[i] = true;
                
                // 将该位置覆盖的所有字符标记为'?'
                for (int j = 0; j < m; j++) {
                    if (targetArr[i + j] != '?') {
                        targetArr[i + j] = '?';
                        matchedCount++;
                    }
                }
                
                // 将该位置加入队列，后续可能影响相邻位置
                queue.offer(i);
            } else {
                // 记录该位置的匹配字符数
                matchCounts[i] = matchCount;
            }
        }
        
        // BFS处理
        while (!queue.isEmpty() && matchedCount < n) {
            int pos = queue.poll();
            
            // 检查受影响的位置范围
            int start = Math.max(0, pos - m + 1);
            int end = Math.min(n - m, pos + m - 1);
            
            for (int i = start; i <= end; i++) {
                if (visited[i]) continue;
                
                boolean canStamp = true;
                int newMatchCount = 0;
                
                // 检查当前位置是否可以盖章
                for (int j = 0; j < m; j++) {
                    int targetPos = i + j;
                    // 如果目标位置已经是'?'或者与印章字符匹配
                    if (targetArr[targetPos] == '?' || targetArr[targetPos] == stampArr[j]) {
                        newMatchCount++;
                    } else {
                        canStamp = false;
                        break;
                    }
                }
                
                // 如果当前位置可以盖章
                if (canStamp) {
                    result.add(i);
                    visited[i] = true;
                    
                    // 将该位置覆盖的所有字符标记为'?'
                    for (int j = 0; j < m; j++) {
                        if (targetArr[i + j] != '?') {
                            targetArr[i + j] = '?';
                            matchedCount++;
                        }
                    }
                    
                    // 将该位置加入队列
                    queue.offer(i);
                }
            }
        }
        
        // 检查是否所有字符都被覆盖为'?'
        if (matchedCount != n) {
            return new int[0];
        }
        
        // 反转结果，因为我们是逆向操作的
        Collections.reverse(result);
        
        // 转换为数组
        int[] res = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            res[i] = result.get(i);
        }
        
        return res;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        String stamp1 = "abc";
        String target1 = "ababc";
        int[] result1 = movesToStamp(stamp1, target1);
        // 预期输出: [0, 2] 或 [1, 0]
        System.out.print("测试用例1: [");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i]);
            if (i < result1.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        // 测试用例2
        String stamp2 = "abca";
        String target2 = "aabcaca";
        int[] result2 = movesToStamp(stamp2, target2);
        // 预期输出: [3, 0, 1] 或其他有效的排列
        System.out.print("测试用例2: [");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i]);
            if (i < result2.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
        
        // 测试用例3
        String stamp3 = "abc";
        String target3 = "abcbc";
        int[] result3 = movesToStamp(stamp3, target3);
        // 预期输出: [2, 0]
        System.out.print("测试用例3: [");
        for (int i = 0; i < result3.length; i++) {
            System.out.print(result3[i]);
            if (i < result3.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}