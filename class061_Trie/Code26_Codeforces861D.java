package class045_Trie;

import java.util.*;

/**
 * Codeforces 861D - Polycarp's phone book
 * 
 * 题目描述：
 * 给定n个长度为9的数字字符串，对于每个字符串，找到最短的特有子串（即该子串只在这个字符串中出现）。
 * 
 * 解题思路：
 * 1. 使用Trie树统计所有子串的出现次数
 * 2. 对于每个字符串，枚举其所有子串，在Trie树中查找出现次数为1的最短子串
 * 
 * 时间复杂度：O(N * L^3)，其中N是字符串数量，L是字符串长度
 * 空间复杂度：O(N * L^3)
 */
public class Code26_Codeforces861D {
    
    // Trie树节点类
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); // 子节点映射
        int count = 0; // 该节点表示的字符串出现次数
    }
    
    static TrieNode root = new TrieNode(); // Trie树根节点
    
    /**
     * 向Trie树中插入一个字符串
     * @param str 要插入的字符串
     */
    public static void insert(String str) {
        TrieNode node = root;
        for (char c : str.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            node = node.children.get(c);
        }
        node.count++; // 增加该字符串的出现次数
    }
    
    /**
     * 在Trie树中查找字符串的出现次数
     * @param str 要查找的字符串
     * @return 出现次数
     */
    public static int search(String str) {
        TrieNode node = root;
        for (char c : str.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return 0;
            }
            node = node.children.get(c);
        }
        return node.count;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 字符串数量
        scanner.nextLine(); // 消费换行符
        
        String[] strings = new String[n];
        // 读取所有字符串
        for (int i = 0; i < n; i++) {
            strings[i] = scanner.nextLine().trim();
        }
        
        // 将所有子串插入Trie树
        for (String str : strings) {
            // 枚举所有子串
            for (int i = 0; i < str.length(); i++) {
                for (int j = i + 1; j <= str.length(); j++) {
                    insert(str.substring(i, j));
                }
            }
        }
        
        // 对于每个字符串，找到最短的特有子串
        for (String str : strings) {
            String result = str; // 默认结果为整个字符串
            
            // 枚举所有子串，按长度递增
            outer: for (int len = 1; len <= str.length(); len++) {
                for (int i = 0; i <= str.length() - len; i++) {
                    String substr = str.substring(i, i + len);
                    // 如果该子串只出现一次，说明是特有子串
                    if (search(substr) == 1) {
                        result = substr;
                        break outer; // 找到最短的特有子串，跳出循环
                    }
                }
            }
            
            System.out.println(result);
        }
        
        scanner.close();
    }
}