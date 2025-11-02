package class045_Trie;

import java.util.*;

/**
 * SPOJ ADAINDEX - Ada and Indexing
 * 
 * 题目描述：
 * 给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。
 * 
 * 解题思路：
 * 1. 使用前缀树存储所有字符串，每个节点记录经过该节点的字符串数量
 * 2. 对于每个查询，在前缀树中找到对应前缀的节点，返回该节点记录的数量
 * 
 * 时间复杂度：
 * - 构建：O(∑len(strings[i]))
 * - 查询：O(len(prefix))
 * 空间复杂度：O(∑len(strings[i]))
 */
public class Code29_SPOJADAINDEX {
    
    // Trie树节点类
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); // 子节点映射
        int count = 0; // 经过该节点的字符串数量
    }
    
    static TrieNode root;
    
    /**
     * 初始化Trie树
     */
    public static void init() {
        root = new TrieNode();
    }
    
    /**
     * 向Trie树中插入一个字符串
     * @param str 要插入的字符串
     */
    public static void insert(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        
        TrieNode node = root;
        for (char c : str.toCharArray()) {
            // 如果子节点不存在，创建新节点
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            
            node = node.children.get(c);
            node.count++; // 增加经过该节点的字符串数量
        }
    }
    
    /**
     * 查询以指定前缀开头的字符串数量
     * @param prefix 前缀
     * @return 字符串数量
     */
    public static int countPrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return 0;
        }
        
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            // 如果子节点不存在，说明没有以该前缀开头的字符串
            if (!node.children.containsKey(c)) {
                return 0;
            }
            
            node = node.children.get(c);
        }
        
        // 返回经过该节点的字符串数量
        return node.count;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 字符串数量
        int q = scanner.nextInt(); // 查询数量
        scanner.nextLine(); // 消费换行符
        
        init(); // 初始化Trie树
        
        // 读取并插入所有字符串
        for (int i = 0; i < n; i++) {
            String str = scanner.nextLine().trim();
            insert(str);
        }
        
        // 处理所有查询
        for (int i = 0; i < q; i++) {
            String prefix = scanner.nextLine().trim();
            int count = countPrefix(prefix);
            System.out.println(count);
        }
        
        scanner.close();
    }
}