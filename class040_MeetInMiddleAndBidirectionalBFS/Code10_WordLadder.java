package class063;

import java.util.*;

// Word Ladder (LeetCode 127)
// 题目来源：LeetCode
// 题目描述：
// 给定两个单词（beginWord 和 endWord）和一个字典 wordList，找出从 beginWord 到 endWord 的最短转换序列的长度。
// 转换需遵循如下规则：
// 1. 每次转换只能改变一个字母。
// 2. 转换后的单词必须是字典中的单词。
// 3. 如果不存在这样的转换序列，返回 0。
// 测试链接：https://leetcode.com/problems/word-ladder/
// 
// 算法思路：
// 使用双向BFS算法，从起点和终点同时开始搜索，当两个搜索前沿相遇时，找到最短路径
// 时间复杂度：O(M*N^2)，其中M是单词长度，N是字典大小
// 空间复杂度：O(N)
// 
// 工程化考量：
// 1. 预处理：将单词按模式分组，提高生成邻接节点的效率
// 2. 优化：始终从较小的集合开始扩展，减少搜索空间
// 3. 边界检查：处理特殊情况（如endWord不在字典中）
// 
// 语言特性差异：
// Java中使用HashSet存储访问过的节点，使用HashMap存储单词模式映射

public class Code10_WordLadder {
    
    /**
     * 计算从beginWord到endWord的最短转换序列长度
     * @param beginWord 起始单词
     * @param endWord 目标单词
     * @param wordList 单词列表
     * @return 最短转换序列的长度，如果不存在返回0
     */
    public static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // 边界条件检查
        if (!wordList.contains(endWord)) {
            return 0; // 如果endWord不在字典中，无法转换
        }
        
        // 将wordList转换为Set，提高查找效率
        Set<String> wordSet = new HashSet<>(wordList);
        
        // 创建双向BFS所需的集合
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        // 初始化
        beginSet.add(beginWord);
        endSet.add(endWord);
        int length = 1; // 初始长度为1（包含beginWord）
        
        // 开始双向BFS
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // 优化：始终从较小的集合开始扩展，减少搜索空间
            if (beginSet.size() > endSet.size()) {
                // 交换beginSet和endSet
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }
            
            // 存储当前层的下一层节点
            Set<String> nextLevel = new HashSet<>();
            
            // 遍历当前层的所有节点
            for (String word : beginSet) {
                // 生成所有可能的转换
                char[] chars = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char originalChar = chars[i];
                    
                    // 尝试将当前字符替换为其他25个小写字母
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == originalChar) {
                            continue;
                        }
                        
                        chars[i] = c;
                        String newWord = new String(chars);
                        
                        // 如果在另一个集合中找到，则找到了路径
                        if (endSet.contains(newWord)) {
                            return length + 1;
                        }
                        
                        // 如果单词在字典中且未被访问过，则加入下一层
                        if (wordSet.contains(newWord) && !visited.contains(newWord)) {
                            nextLevel.add(newWord);
                            visited.add(newWord);
                        }
                    }
                    
                    // 恢复原字符
                    chars[i] = originalChar;
                }
            }
            
            // 更新当前层
            beginSet = nextLevel;
            length++;
        }
        
        // 如果两个集合不再相交，表示没有找到路径
        return 0;
    }
    
    /**
     * 优化版本：使用单词模式映射进行优化
     */
    public static int ladderLengthOptimized(String beginWord, String endWord, List<String> wordList) {
        // 边界条件检查
        if (!wordList.contains(endWord)) {
            return 0;
        }
        
        int L = beginWord.length();
        
        // 预处理：将单词按模式分组，例如：h*t -> [hot, hit, hat...]
        Map<String, List<String>> patternToWords = new HashMap<>();
        for (String word : wordList) {
            for (int i = 0; i < L; i++) {
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1, L);
                patternToWords.putIfAbsent(pattern, new ArrayList<>());
                patternToWords.get(pattern).add(word);
            }
        }
        
        // 添加beginWord到模式映射
        for (int i = 0; i < L; i++) {
            String pattern = beginWord.substring(0, i) + "*" + beginWord.substring(i + 1, L);
            patternToWords.putIfAbsent(pattern, new ArrayList<>());
        }
        
        // 双向BFS
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        beginSet.add(beginWord);
        endSet.add(endWord);
        int length = 1;
        
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }
            
            Set<String> nextLevel = new HashSet<>();
            
            for (String word : beginSet) {
                for (int i = 0; i < L; i++) {
                    String pattern = word.substring(0, i) + "*" + word.substring(i + 1, L);
                    
                    // 获取所有匹配该模式的单词
                    for (String neighbor : patternToWords.getOrDefault(pattern, new ArrayList<>())) {
                        if (endSet.contains(neighbor)) {
                            return length + 1;
                        }
                        
                        if (!visited.contains(neighbor)) {
                            nextLevel.add(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
            
            beginSet = nextLevel;
            length++;
        }
        
        return 0;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String beginWord1 = "hit";
        String endWord1 = "cog";
        List<String> wordList1 = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        System.out.println("测试用例1：");
        System.out.println("beginWord: hit, endWord: cog, wordList: [hot, dot, dog, lot, log, cog]");
        System.out.println("期望输出：5"); // hit -> hot -> dot -> dog -> cog
        System.out.println("实际输出（普通版）：" + ladderLength(beginWord1, endWord1, wordList1));
        System.out.println("实际输出（优化版）：" + ladderLengthOptimized(beginWord1, endWord1, wordList1));
        
        // 测试用例2
        String beginWord2 = "hit";
        String endWord2 = "cog";
        List<String> wordList2 = Arrays.asList("hot", "dot", "dog", "lot", "log");
        System.out.println("\n测试用例2：");
        System.out.println("beginWord: hit, endWord: cog, wordList: [hot, dot, dog, lot, log]");
        System.out.println("期望输出：0"); // endWord不在wordList中
        System.out.println("实际输出（普通版）：" + ladderLength(beginWord2, endWord2, wordList2));
        System.out.println("实际输出（优化版）：" + ladderLengthOptimized(beginWord2, endWord2, wordList2));
        
        // 测试用例3
        String beginWord3 = "a";
        String endWord3 = "c";
        List<String> wordList3 = Arrays.asList("a", "b", "c");
        System.out.println("\n测试用例3：");
        System.out.println("beginWord: a, endWord: c, wordList: [a, b, c]");
        System.out.println("期望输出：2"); // a -> c
        System.out.println("实际输出（普通版）：" + ladderLength(beginWord3, endWord3, wordList3));
        System.out.println("实际输出（优化版）：" + ladderLengthOptimized(beginWord3, endWord3, wordList3));
    }
}