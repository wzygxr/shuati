package class045_Trie;

import java.util.*;

/**
 * HackerRank No Prefix Set
 * 
 * 题目描述：
 * 给定一个字符串集合，判断是否存在一个字符串是另一个字符串的前缀。
 * 如果存在，输出"BAD SET"和第一个违反规则的字符串；否则输出"GOOD SET"。
 * 
 * 解题思路：
 * 1. 使用前缀树存储字符串
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入一个字符串时，发现路径上有已经标记为完整字符串的节点，
 *    或者插入完成后当前节点有子节点，说明存在前缀关系
 * 
 * 时间复杂度：O(N*M)，其中N是字符串数量，M是平均字符串长度
 * 空间复杂度：O(N*M)
 */
public class Code28_HackerRankNoPrefixSet {
    
    // Trie树节点类
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); // 子节点映射
        boolean isEnd = false; // 标记是否为一个完整字符串的结尾
    }
    
    static TrieNode root;
    
    /**
     * 初始化Trie树
     */
    public static void init() {
        root = new TrieNode();
    }
    
    /**
     * 向Trie树中插入一个字符串，并检查是否存在前缀关系
     * @param str 要插入的字符串
     * @return 如果存在前缀关系返回true，否则返回false
     */
    public static boolean insert(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        
        TrieNode node = root;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            // 如果子节点不存在，创建新节点
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            
            node = node.children.get(c);
            
            // 如果当前节点已经是某个完整字符串的结尾，说明当前字符串是另一个字符串的前缀
            if (node.isEnd) {
                return true;
            }
        }
        
        // 标记当前节点为完整字符串的结尾
        node.isEnd = true;
        
        // 检查当前节点是否有子节点，如果有说明存在前缀关系
        if (!node.children.isEmpty()) {
            return true;
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 字符串数量
        scanner.nextLine(); // 消费换行符
        
        init(); // 初始化Trie树
        
        String[] strings = new String[n];
        for (int i = 0; i < n; i++) {
            strings[i] = scanner.nextLine().trim();
        }
        
        boolean goodSet = true;
        String badString = "";
        
        for (String str : strings) {
            if (insert(str)) {
                goodSet = false;
                badString = str;
                break;
            }
        }
        
        if (goodSet) {
            System.out.println("GOOD SET");
        } else {
            System.out.println("BAD SET");
            System.out.println(badString);
        }
        
        scanner.close();
    }
}