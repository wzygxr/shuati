package class045_Trie;

import java.util.*;
import java.io.*;

/**
 * SPOJ ADAINDEX - Ada and Indexing
 * 
 * 题目描述：
 * 给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。
 * 
 * 解题思路：
 * 这是一个标准的Trie树应用问题。我们可以：
 * 1. 使用Trie树存储所有字符串，在每个节点记录经过该节点的字符串数量
 * 2. 对于每个查询，在Trie树中查找前缀对应的节点
 * 3. 返回该节点记录的字符串数量
 * 
 * 时间复杂度：
 * - 构建Trie树：O(∑len(strings[i]))
 * - 查询：O(len(prefix))
 * 空间复杂度：O(∑len(strings[i]))
 */
public class Code33_SPOJADAINDEX {
    
    /**
     * Trie树节点类
     */
    static class TrieNode {
        Map<Character, TrieNode> children;  // 子节点映射
        int count;                          // 经过该节点的字符串数量
        
        public TrieNode() {
            children = new HashMap<>();
            count = 0;
        }
    }
    
    /**
     * Trie树类
     */
    static class Trie {
        private TrieNode root;  // 根节点
        
        public Trie() {
            root = new TrieNode();
        }
        
        /**
         * 向Trie树中插入一个单词
         * @param word 要插入的单词
         */
        public void insert(String word) {
            if (word == null || word.isEmpty()) {
                return;
            }
            
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                node.count++;  // 增加经过该节点的字符串数量
            }
        }
        
        /**
         * 统计以指定前缀开头的字符串数量
         * @param prefix 要查询的前缀
         * @return 以该前缀开头的字符串数量
         */
        public int countPrefix(String prefix) {
            if (prefix == null || prefix.isEmpty()) {
                return 0;
            }
            
            TrieNode node = root;
            // 遍历前缀中的每个字符
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return 0;  // 前缀不存在
                }
                node = node.children.get(c);
            }
            
            // 返回经过该节点的字符串数量
            return node.count;
        }
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            scanner.nextLine();  // 消费换行符
            
            // 构建Trie树
            Trie trie = new Trie();
            
            // 插入所有字符串
            for (int i = 0; i < n; i++) {
                String word = scanner.nextLine().trim();
                trie.insert(word);
            }
            
            // 处理所有查询
            for (int i = 0; i < m; i++) {
                String prefix = scanner.nextLine().trim();
                int count = trie.countPrefix(prefix);
                System.out.println(count);
            }
        }
        
        scanner.close();
    }
}