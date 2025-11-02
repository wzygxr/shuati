package class045_Trie;

import java.util.*;
import java.io.*;

/**
 * HackerRank No Prefix Set
 * 
 * 题目描述：
 * 给定N个字符串，每个字符串只包含小写字母a-j（包含）。
 * 如果字符串集合中没有字符串是另一个字符串的前缀，则称该字符串集合为GOOD SET。
 * 否则，打印BAD SET，并在下一行打印正在检查的字符串。
 * 
 * 注意：如果两个字符串相同，它们互为前缀。
 * 
 * 解题思路：
 * 这是一个经典的Trie树应用问题，用于检测前缀关系：
 * 1. 使用Trie树存储字符串
 * 2. 在插入每个字符串时检查前缀关系
 * 3. 如果发现前缀关系，立即返回BAD SET
 * 
 * 检测前缀关系的方法：
 * 1. 在插入过程中，如果到达一个已经是单词结尾的节点，说明当前字符串是某个已插入字符串的前缀
 * 2. 如果插入完成后，当前节点还有子节点，说明某个已插入字符串是当前字符串的前缀
 * 
 * 时间复杂度：O(∑len(strings[i]))
 * 空间复杂度：O(∑len(strings[i]))
 */
public class Code35_HackerRankNoPrefixSet {
    
    /**
     * Trie树节点类
     */
    static class TrieNode {
        Map<Character, TrieNode> children;  // 子节点映射
        boolean isEnd;                      // 标记是否为单词结尾
        
        public TrieNode() {
            children = new HashMap<>();
            isEnd = false;
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
         * 向Trie树中插入单词并检查前缀关系
         * @param word 要插入的单词
         * @return 如果成功返回null，否则返回冲突的单词
         */
        public String insertAndCheck(String word) {
            if (word == null || word.isEmpty()) {
                return null;
            }
            
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                // 如果子节点不存在，创建新节点
                if (!node.children.containsKey(c)) {
                    node.children.put(c, new TrieNode());
                }
                
                node = node.children.get(c);
                
                // 如果当前节点已经是某个单词的结尾，说明当前单词是另一个单词的前缀
                if (node.isEnd) {
                    return word;
                }
            }
            
            // 标记当前节点为单词结尾
            node.isEnd = true;
            
            // 检查当前节点是否有子节点，如果有说明某个单词是当前单词的前缀
            if (!node.children.isEmpty()) {
                return word;
            }
            
            return null;
        }
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        scanner.nextLine();  // 消费换行符
        
        // 初始化Trie树
        Trie trie = new Trie();
        
        // 处理每个字符串
        for (int i = 0; i < n; i++) {
            String word = scanner.nextLine().trim();
            String conflictWord = trie.insertAndCheck(word);
            if (conflictWord != null) {
                System.out.println("BAD SET");
                System.out.println(conflictWord);
                scanner.close();
                return;
            }
        }
        
        System.out.println("GOOD SET");
        scanner.close();
    }
}