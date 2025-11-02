package class045_Trie;

import java.util.*;
import java.io.*;

/**
 * SPOJ DICT - Search in the dictionary!
 * 
 * 题目描述：
 * 给定一个字典（字符串列表）和多个查询，每个查询给出一个前缀，要求找出字典中所有以该前缀开头的单词，
 * 并按字典序输出。
 * 
 * 解题思路：
 * 这是一个标准的Trie树应用问题：
 * 1. 使用Trie树存储字典中的所有单词
 * 2. 对于每个查询，在Trie树中查找前缀对应的节点
 * 3. 从该节点开始深度优先搜索，收集所有单词并按字典序排序输出
 * 
 * 时间复杂度：
 * - 构建Trie树：O(∑len(strings[i]))
 * - 查询：O(len(prefix) + ∑len(results))
 * 空间复杂度：O(∑len(strings[i]))
 */
public class Code36_SPOJDICT {
    
    /**
     * Trie树节点类
     */
    static class TrieNode {
        Map<Character, TrieNode> children;  // 子节点映射
        boolean isEnd;                      // 标记是否为单词结尾
        String word;                        // 如果是单词结尾，存储完整的单词
        
        public TrieNode() {
            children = new HashMap<>();
            isEnd = false;
            word = "";
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
            }
            
            // 标记单词结尾并存储完整单词
            node.isEnd = true;
            node.word = word;
        }
        
        /**
         * 查找所有以指定前缀开头的单词
         * @param prefix 要查询的前缀
         * @return 以该前缀开头的所有单词列表（按字典序排序）
         */
        public List<String> findWordsWithPrefix(String prefix) {
            if (prefix == null || prefix.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 查找前缀对应的节点
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    return new ArrayList<>();  // 前缀不存在
                }
                node = node.children.get(c);
            }
            
            // 从该节点开始深度优先搜索，收集所有单词
            List<String> words = new ArrayList<>();
            dfsCollectWords(node, words);
            Collections.sort(words);  // 按字典序排序
            return words;
        }
        
        /**
         * 深度优先搜索收集所有单词
         * @param node 当前节点
         * @param words 存储单词的列表
         */
        private void dfsCollectWords(TrieNode node, List<String> words) {
            if (node.isEnd) {
                words.add(node.word);
            }
            
            // 按字典序遍历子节点
            List<Character> sortedKeys = new ArrayList<>(node.children.keySet());
            Collections.sort(sortedKeys);
            for (char c : sortedKeys) {
                dfsCollectWords(node.children.get(c), words);
            }
        }
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int caseNum = 1;
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            scanner.nextLine();  // 消费换行符
            
            // 构建Trie树
            Trie trie = new Trie();
            
            // 插入所有单词
            for (int i = 0; i < n; i++) {
                String word = scanner.nextLine().trim();
                trie.insert(word);
            }
            
            int m = scanner.nextInt();
            scanner.nextLine();  // 消费换行符
            
            // 处理所有查询
            for (int i = 0; i < m; i++) {
                String prefix = scanner.nextLine().trim();
                List<String> words = trie.findWordsWithPrefix(prefix);
                
                System.out.println("Case #" + caseNum + ":");
                if (words.isEmpty()) {
                    System.out.println("No match.");
                } else {
                    for (String word : words) {
                        System.out.println(word);
                    }
                }
                
                caseNum++;
            }
        }
        
        scanner.close();
    }
}