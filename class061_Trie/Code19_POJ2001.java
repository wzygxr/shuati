package class045_Trie;

import java.util.*;

/**
 * POJ 2001 Shortest Prefixes
 * 
 * 题目描述：
 * 给定一组单词，为每个单词找出最短的唯一前缀。如果整个单词都不是其他任何单词的前缀，
 * 则输出该单词本身。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有单词，在每个节点记录经过该节点的单词数量
 * 2. 对于每个单词，在Trie树中查找第一个节点计数为1的位置，该位置即为最短唯一前缀
 * 
 * 时间复杂度：O(N*M)，其中N是单词数量，M是平均单词长度
 * 空间复杂度：O(N*M)
 */
public class Code19_POJ2001 {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[26]; // 子节点数组，对应26个小写字母
        int count = 0; // 经过该节点的单词数量
    }
    
    static TrieNode root = new TrieNode(); // Trie树根节点
    
    /**
     * 向Trie树中插入一个单词
     * @param word 要插入的单词
     */
    public static void insert(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
            node.count++; // 增加经过该节点的单词数量
        }
    }
    
    /**
     * 查找单词的最短唯一前缀
     * @param word 要查找的单词
     * @return 最短唯一前缀
     */
    public static String findShortestPrefix(String word) {
        if (word == null || word.length() == 0) {
            return "";
        }
        
        TrieNode node = root;
        StringBuilder prefix = new StringBuilder();
        
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            node = node.children[index];
            prefix.append(c);
            
            // 如果经过该节点的单词数量为1，说明这是唯一前缀
            if (node.count == 1) {
                return prefix.toString();
            }
        }
        
        // 如果整个单词都不是其他单词的前缀，返回整个单词
        return word;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> words = new ArrayList<>();
        
        // 读取所有单词
        while (scanner.hasNext()) {
            words.add(scanner.nextLine().trim());
        }
        
        // 构建Trie树
        for (String word : words) {
            insert(word);
        }
        
        // 输出每个单词的最短唯一前缀
        for (String word : words) {
            System.out.println(word + " " + findShortestPrefix(word));
        }
        
        scanner.close();
    }
}