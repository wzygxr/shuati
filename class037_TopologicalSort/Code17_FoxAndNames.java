package class060;

// Fox and Names - 字典序建图与拓扑排序
// 给定n个按照字典序排列的字符串，判断是否存在字母顺序使得这些字符串按此顺序排列
// 如果存在，输出任意一种可能的字母顺序；否则输出"Impossible"
// 测试链接 : https://codeforces.com/problemset/problem/510/C
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是拓扑排序在字典序问题中的应用，通过字符串比较构建字母间的顺序关系图。
 * 然后使用拓扑排序判断是否存在合法的字母顺序。
 * 
 * 算法思路：
 * 1. 比较相邻字符串，找到第一个不同的字符，建立字符间的顺序关系
 * 2. 构建有向图，边表示字符间的顺序关系
 * 3. 使用拓扑排序判断图中是否有环
 * 4. 如果无环，输出拓扑序列；否则输出"Impossible"
 * 
 * 时间复杂度：O(n * L)，其中n是字符串数量，L是字符串平均长度
 * 空间复杂度：O(26^2) = O(1)，因为只有26个字母
 * 
 * 相关题目扩展：
 * 1. Codeforces 510C Fox And Names - https://codeforces.com/problemset/problem/510/C
 * 2. LeetCode 269. 火星词典 - https://leetcode.cn/problems/alien-dictionary/
 * 3. SPOJ TOPOSORT - https://www.spoj.com/problems/TOPOSORT/
 * 4. HDU 1285 确定比赛名次 - http://acm.hdu.edu.cn/showproblem.php?pid=1285
 * 
 * 工程化考虑：
 * 1. 图构建：通过字符串比较建立字符顺序关系
 * 2. 环检测：使用拓扑排序检测非法顺序
 * 3. 边界处理：前缀关系、空字符串等情况
 * 4. 输出格式：按要求输出结果
 */
public class Code17_FoxAndNames {

    public static String alienOrder(String[] words) {
        // 构建图：26个字母
        boolean[][] graph = new boolean[26][26];
        int[] indegree = new int[26];
        
        // 标记存在的字母
        boolean[] exists = new boolean[26];
        for (String word : words) {
            for (char c : word.toCharArray()) {
                exists[c - 'a'] = true;
            }
        }
        
        // 比较相邻字符串，构建图
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            
            // 检查前缀关系：如果word1是word2的前缀，且word1更长，则非法
            if (word1.length() > word2.length() && word1.startsWith(word2)) {
                return "Impossible";
            }
            
            // 找到第一个不同的字符
            int len = Math.min(word1.length(), word2.length());
            for (int j = 0; j < len; j++) {
                char c1 = word1.charAt(j);
                char c2 = word2.charAt(j);
                
                if (c1 != c2) {
                    // 建立边 c1 -> c2
                    if (!graph[c1 - 'a'][c2 - 'a']) {
                        graph[c1 - 'a'][c2 - 'a'] = true;
                        indegree[c2 - 'a']++;
                    }
                    break; // 找到第一个不同字符后停止
                }
            }
        }
        
        // 拓扑排序
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < 26; i++) {
            if (exists[i] && indegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        StringBuilder result = new StringBuilder();
        int count = 0; // 已处理的字母数
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.append((char)('a' + u));
            count++;
            
            for (int v = 0; v < 26; v++) {
                if (graph[u][v]) {
                    if (--indegree[v] == 0) {
                        queue.offer(v);
                    }
                }
            }
        }
        
        // 检查是否有环
        if (count != getExistCount(exists)) {
            return "Impossible";
        }
        
        return result.toString();
    }
    
    private static int getExistCount(boolean[] exists) {
        int count = 0;
        for (boolean exist : exists) {
            if (exist) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        // 测试用例1：正常情况
        String[] words1 = {"rivest", "shamir", "adleman"};
        System.out.println("测试用例1: " + alienOrder(words1)); // 输出可能的字母顺序
        
        // 测试用例2：存在环
        String[] words2 = {"abc", "ab"};
        System.out.println("测试用例2: " + alienOrder(words2)); // 输出: Impossible
        
        // 测试用例3：正常顺序
        String[] words3 = {"wrt", "wrf", "er", "ett", "rftt"};
        System.out.println("测试用例3: " + alienOrder(words3)); // 输出可能的字母顺序
        
        // 测试用例4：前缀关系非法
        String[] words4 = {"apple", "app"};
        System.out.println("测试用例4: " + alienOrder(words4)); // 输出: Impossible
    }
}