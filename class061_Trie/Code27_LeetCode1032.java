package class045_Trie;

import java.util.*;

/**
 * LeetCode 1032. 字符流
 * 
 * 题目描述：
 * 实现一个数据结构，支持查询字符流的后缀是否为给定字符串数组中的某个字符串。
 * 
 * 解题思路：
 * 1. 使用前缀树存储所有单词的逆序
 * 2. 维护字符流的逆序缓冲区
 * 3. 查询时在前缀树中查找字符流后缀的逆序
 * 
 * 时间复杂度：
 * - 初始化：O(∑len(words[i]))
 * - 查询：O(max(len(query)))
 * 空间复杂度：O(∑len(words[i]))
 */
public class Code27_LeetCode1032 {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[26]; // 子节点数组
        boolean isEnd = false; // 是否为单词结尾
    }
    
    private TrieNode root; // Trie树根节点
    private StringBuilder stream; // 字符流缓冲区
    
    /**
     * 构造函数
     * @param words 单词数组
     */
    public Code27_LeetCode1032(String[] words) {
        root = new TrieNode();
        stream = new StringBuilder();
        
        // 将所有单词的逆序插入Trie树
        for (String word : words) {
            insert(new StringBuilder(word).reverse().toString());
        }
    }
    
    /**
     * 向Trie树中插入一个单词
     * @param word 要插入的单词
     */
    private void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true; // 标记为单词结尾
    }
    
    /**
     * 查询字符流的后缀是否为给定单词
     * @param letter 新加入的字符
     * @return 是否存在匹配的单词
     */
    public boolean query(char letter) {
        // 将新字符添加到字符流缓冲区
        stream.append(letter);
        
        // 在Trie树中查找字符流后缀的逆序
        TrieNode node = root;
        for (int i = stream.length() - 1; i >= 0; i--) {
            int index = stream.charAt(i) - 'a';
            
            // 如果字符不存在，返回false
            if (node.children[index] == null) {
                return false;
            }
            
            node = node.children[index];
            
            // 如果找到单词结尾，返回true
            if (node.isEnd) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        String[] words = {"cd", "f", "kl"};
        Code27_LeetCode1032 streamChecker = new Code27_LeetCode1032(words);
        
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
        for (char letter : letters) {
            boolean result = streamChecker.query(letter);
            System.out.println("Query '" + letter + "': " + result);
        }
    }
}