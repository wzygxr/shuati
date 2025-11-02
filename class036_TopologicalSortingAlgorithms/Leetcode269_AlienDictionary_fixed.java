import java.util.*;

/**
 * LeetCode 269. Alien Dictionary
 * 
 * 题目链接: https://leetcode.com/problems/alien-dictionary/
 * 
 * 题目描述：
 * 现有一种使用英语字母的外星语言，这门语言的字母顺序与英语顺序不同。
 * 给定一个字符串数组 words，表示这门新语言的词典。words 中的字符串按这门新语言的字母顺序排列。
 * 如果这种说法是错误的，并且按字典序排列是无效的，则返回 ""。
 * 否则，请返回该语言的唯一字母顺序，按新语言的字母顺序排列。
 * 如果有多种可能的答案，返回其中任意一个即可。
 * 
 * 解题思路：
 * 这是一个拓扑排序问题。我们需要根据给定的单词顺序推断出字符之间的顺序关系，
 * 然后使用拓扑排序来确定字符的正确顺序。
 * 
 * 步骤：
 * 1. 构建图：比较相邻的单词，找到第一个不同的字符，建立字符间的有向边
 * 2. 计算每个字符的入度
 * 3. 使用Kahn算法进行拓扑排序
 * 4. 检查结果是否包含所有字符（判断是否存在环）
 * 
 * 时间复杂度：O(C)，其中C是所有单词中字符的总数
 * 空间复杂度：O(1)，因为字符集大小是固定的（最多26个小写字母）
 * 
 * 示例：
 * 输入：words = ["wrt","wrf","er","ett","rftt"]
 * 输出："wertf"
 * 
 * 输入：words = ["z","x"]
 * 输出："zx"
 * 
 * 输入：words = ["z","x","z"]
 * 输出：""
 * 解释：不存在合法的字母顺序，因为存在环。
 */
public class Leetcode269_AlienDictionary_fixed {

    public static void main(String[] args) {
        Leetcode269_AlienDictionary_fixed solution = new Leetcode269_AlienDictionary_fixed();
        
        // 测试用例1
        String[] words1 = {"wrt", "wrf", "er", "ett", "rftt"};
        System.out.println("Test Case 1: " + solution.alienOrder(words1)); // 应该输出 "wertf"
        
        // 测试用例2
        String[] words2 = {"z", "x"};
        System.out.println("Test Case 2: " + solution.alienOrder(words2)); // 应该输出 "zx"
        
        // 测试用例3
        String[] words3 = {"z", "x", "z"};
        System.out.println("Test Case 3: " + solution.alienOrder(words3)); // 应该输出 ""
        
        // 测试用例4
        String[] words4 = {"abc", "ab"};
        System.out.println("Test Case 4: " + solution.alienOrder(words4)); // 应该输出 ""
    }
    
    /**
     * 返回外星语的字母顺序
     * @param words 按外星语字典序排列的单词数组
     * @return 外星语的字母顺序，如果不存在合法顺序则返回空字符串
     */
    public String alienOrder(String[] words) {
        // 构建图和入度数组
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        
        // 初始化所有字符
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }
        
        // 构建图：比较相邻单词，找到字符顺序关系
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            
            // 检查无效情况：word1比word2长，但word2是word1的前缀
            // 例如：["abc", "ab"] 这种情况是无效的
            if (word1.length() > word2.length()) {
                boolean isPrefix = true;
                for (int k = 0; k < word2.length(); k++) {
                    if (word1.charAt(k) != word2.charAt(k)) {
                        isPrefix = false;
                        break;
                    }
                }
                if (isPrefix) {
                    return "";
                }
            }
            
            // 找到第一个不同的字符，建立边
            for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
                char c1 = word1.charAt(j);
                char c2 = word2.charAt(j);
                
                if (c1 != c2) {
                    // 如果这条边还没有添加过
                    if (!graph.get(c1).contains(c2)) {
                        graph.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break; // 只比较第一个不同的字符
                }
            }
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSort(graph, inDegree);
    }
    
    /**
     * 使用Kahn算法进行拓扑排序，返回字符顺序
     * @param graph 字符关系图
     * @param inDegree 字符入度映射
     * @return 字符的拓扑排序结果，如果存在环则返回空字符串
     */
    private String topologicalSort(Map<Character, Set<Character>> graph, Map<Character, Integer> inDegree) {
        Queue<Character> queue = new LinkedList<>();
        
        // 将所有入度为0的字符加入队列
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }
        
        StringBuilder result = new StringBuilder();
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            char currentChar = queue.poll();
            result.append(currentChar);
            
            // 遍历当前字符的所有后续字符
            for (char nextChar : graph.get(currentChar)) {
                // 将后续字符的入度减1
                inDegree.put(nextChar, inDegree.get(nextChar) - 1);
                
                // 如果后续字符的入度变为0，则加入队列
                if (inDegree.get(nextChar) == 0) {
                    queue.offer(nextChar);
                }
            }
        }
        
        // 如果结果包含所有字符，说明不存在环，返回结果；否则返回空字符串
        return result.length() == inDegree.size() ? result.toString() : "";
    }
}