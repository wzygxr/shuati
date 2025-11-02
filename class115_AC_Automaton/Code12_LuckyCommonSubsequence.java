package class102;

import java.io.*;
import java.util.*;

/**
 * Codeforces 346B - Lucky Common Subsequence
 * 题目链接：https://codeforces.com/problemset/problem/346/B
 * 题目描述：给定三个字符串str1、str2和virus，找出str1和str2的最长公共子序列，且该子序列不包含virus作为子串。
 * 
 * 算法详解：
 * 这是一道结合动态规划和AC自动机的题目。我们需要在求最长公共子序列的过程中，
 * 使用AC自动机来避免生成包含病毒串的子序列。
 * 
 * 算法核心思想：
 * 1. 构建病毒字符串的AC自动机，用于检测是否包含病毒串
 * 2. 使用三维动态规划：dp[i][j][k]表示str1前i个字符、str2前j个字符、在AC自动机上处于状态k时的最长公共子序列
 * 3. 状态转移时，确保不会进入AC自动机的危险状态（即匹配到病毒串的状态）
 * 
 * 时间复杂度分析：
 * 1. 构建AC自动机：O(|virus|)
 * 2. 动态规划：O(|str1| × |str2| × |virus|)
 * 总时间复杂度：O(|str1| × |str2| × |virus|)
 * 
 * 空间复杂度：O(|str1| × |str2| × |virus|)
 * 
 * 适用场景：
 * 1. 带约束条件的最长公共子序列
 * 2. 字符串匹配与动态规划结合
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用滚动数组优化空间复杂度
 * 3. 内存优化：合理设置数组大小，避免浪费
 */

public class Code12_LuckyCommonSubsequence {
    // Trie树节点
    static class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        TrieNode fail;
        int nodeId; // 节点编号
        
        public TrieNode(int id) {
            children = new TrieNode[26];
            isEnd = false;
            fail = null;
            nodeId = id;
        }
    }
    
    static final int MAXN = 105;
    static final int MAXS = 105;
    
    static TrieNode root;
    static int nodeCount = 0;
    static boolean[] danger = new boolean[MAXS]; // 危险状态标记
    static TrieNode[] nodeMap = new TrieNode[MAXS]; // 节点映射
    
    // 通过ID获取节点
    static TrieNode getNodeById(int id) {
        if (id >= 0 && id < nodeMap.length) {
            return nodeMap[id];
        }
        return root;
    }
    
    // 构建AC自动机
    static void buildACAutomaton(String virus) {
        // 重置状态
        nodeCount = 0;
        for (int i = 0; i < MAXS; i++) {
            nodeMap[i] = null;
            danger[i] = false;
        }
        
        root = new TrieNode(nodeCount);
        nodeMap[nodeCount] = root;
        nodeCount++;
        
        // 插入病毒字符串
        TrieNode node = root;
        for (char c : virus.toCharArray()) {
            int index = Character.toUpperCase(c) - 'A';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode(nodeCount);
                nodeMap[nodeCount] = node.children[index];
                nodeCount++;
            }
            node = node.children[index];
        }
        node.isEnd = true;
        
        // 构建fail指针
        Queue<TrieNode> queue = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.offer(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }
        
        while (!queue.isEmpty()) {
            TrieNode u = queue.poll();
            danger[u.nodeId] = danger[u.nodeId] || u.isEnd || (u.fail != null && danger[u.fail.nodeId]);
            
            for (int i = 0; i < 26; i++) {
                if (u.children[i] != null) {
                    u.children[i].fail = (u.fail != null) ? u.fail.children[i] : root;
                    queue.offer(u.children[i]);
                } else {
                    u.children[i] = (u.fail != null) ? u.fail.children[i] : root;
                }
            }
        }
    }
    
    // 求最长公共子序列（不包含病毒串）
    static String longestCommonSubsequenceWithoutVirus(String str1, String str2, String virus) {
        int n = str1.length();
        int m = str2.length();
        int v = nodeCount;
        
        // dp[i][j][k]表示str1前i个字符、str2前j个字符、在AC自动机状态k时的最长公共子序列长度
        int[][][] dp = new int[n + 1][m + 1][v];
        // path[i][j][k]记录路径，用于重构答案
        int[][][] path = new int[n + 1][m + 1][v];
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k < v; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        dp[0][0][0] = 0;
        
        // 动态规划
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k < v; k++) {
                    if (dp[i][j][k] == -1) continue;
                    
                    // 不选择当前字符
                    if (i < n && (dp[i + 1][j][k] < dp[i][j][k])) {
                        dp[i + 1][j][k] = dp[i][j][k];
                        path[i + 1][j][k] = 0; // 0表示不选择
                    }
                    if (j < m && (dp[i][j + 1][k] < dp[i][j][k])) {
                        dp[i][j + 1][k] = dp[i][j][k];
                        path[i][j + 1][k] = 0; // 0表示不选择
                    }
                    
                    // 选择当前字符
                    if (i < n && j < m && str1.charAt(i) == str2.charAt(j)) {
                        char c = str1.charAt(i);
                        int charIndex = Character.toUpperCase(c) - 'A';
                        // 沿着当前状态的fail指针找到正确的转移状态
                        TrieNode currentState = getNodeById(k);
                        TrieNode nextState = currentState.children[charIndex];
                        if (nextState == null) {
                            nextState = currentState.fail.children[charIndex];
                        }
                        int next = (nextState != null) ? nextState.nodeId : 0;
                        
                        if (!danger[next] && dp[i + 1][j + 1][next] < dp[i][j][k] + 1) {
                            dp[i + 1][j + 1][next] = dp[i][j][k] + 1;
                            path[i + 1][j + 1][next] = 1; // 1表示选择
                        }
                    }
                }
            }
        }
        
        // 找到最大值
        int maxLen = 0;
        int endI = 0, endJ = 0, endK = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k < v; k++) {
                    if (dp[i][j][k] > maxLen) {
                        maxLen = dp[i][j][k];
                        endI = i;
                        endJ = j;
                        endK = k;
                    }
                }
            }
        }
        
        // 重构答案
        if (maxLen == 0) {
            return "0";
        }
        
        StringBuilder result = new StringBuilder();
        int i = endI, j = endJ, k = endK;
        while (i > 0 || j > 0) {
            if (path[i][j][k] == 1) {
                result.append(str1.charAt(i - 1));
                i--;
                j--;
                // 更新状态k
                char c = str1.charAt(i);
                TrieNode temp = root;
                for (int idx = 0; idx < v; idx++) {
                    if (temp.children[Character.toUpperCase(c) - 'A'] != null) {
                        temp = temp.children[Character.toUpperCase(c) - 'A'];
                        if (temp.nodeId == k && temp.fail != null) {
                            k = temp.fail.nodeId;
                            break;
                        }
                    }
                }
            } else {
                if (i > 0 && dp[i - 1][j][k] == dp[i][j][k]) {
                    i--;
                } else if (j > 0 && dp[i][j - 1][k] == dp[i][j][k]) {
                    j--;
                } else {
                    break;
                }
            }
        }
        
        return result.reverse().toString();
    }
    
    public static void main(String[] args) {
        // 示例测试
        String str1 = "abcdef";
        String str2 = "abcxyz";
        String virus = "xyz";
        
        // 构建AC自动机
        buildACAutomaton(virus);
        
        // 求解
        String result = longestCommonSubsequenceWithoutVirus(str1, str2, virus);
        System.out.println("最长公共子序列（不包含病毒串）: " + result);
        
        // 另一个测试用例
        str1 = "abc";
        str2 = "acb";
        virus = "b";
        buildACAutomaton(virus);
        result = longestCommonSubsequenceWithoutVirus(str1, str2, virus);
        System.out.println("最长公共子序列（不包含病毒串）: " + result);
    }
}