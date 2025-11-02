package class045_Trie;

import java.util.*;

/**
 * AtCoder ABC141 E - Who Says a Pun?
 * 
 * 题目描述：
 * 给定一个字符串，求最长的出现至少两次的不重叠子串长度。
 * 
 * 解题思路：
 * 1. 使用二分答案，对于每个长度，检查是否存在出现至少两次的不重叠子串
 * 2. 使用前缀树或哈希来检查是否存在重复子串
 * 3. 对于每个长度，枚举所有子串并在前缀树中查找是否已存在
 * 
 * 时间复杂度：O(N^2 * log N)，其中N是字符串长度
 * 空间复杂度：O(N^2)
 */
public class Code31_AtCoderABC141E {
    
    // Trie树节点类
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); // 子节点映射
        List<Integer> positions = new ArrayList<>();         // 存储子串出现的位置
    }
    
    static TrieNode root;
    
    /**
     * 初始化Trie树
     */
    public static void init() {
        root = new TrieNode();
    }
    
    /**
     * 检查是否存在长度为len的重复不重叠子串
     * @param str 字符串
     * @param len 子串长度
     * @return 是否存在重复不重叠子串
     */
    public static boolean hasDuplicateNonOverlappingSubstring(String str, int len) {
        init(); // 初始化Trie树
        
        // 枚举所有长度为len的子串
        for (int i = 0; i <= str.length() - len; i++) {
            String substr = str.substring(i, i + len);
            if (insertAndCheck(substr, i, len)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 向Trie树中插入子串并检查是否已存在不重叠的子串
     * @param substr 子串
     * @param pos 子串起始位置
     * @param len 子串长度
     * @return 是否存在不重叠的重复子串
     */
    public static boolean insertAndCheck(String substr, int pos, int len) {
        TrieNode node = root;
        for (char c : substr.toCharArray()) {
            // 如果子节点不存在，创建新节点
            if (!node.children.containsKey(c)) {
                node.children.put(c, new TrieNode());
            }
            node = node.children.get(c);
        }
        
        // 检查是否存在不重叠的子串
        for (int prevPos : node.positions) {
            if (Math.abs(pos - prevPos) >= len) {
                return true; // 找到不重叠的重复子串
            }
        }
        
        // 记录当前位置
        node.positions.add(pos);
        return false;
    }
    
    /**
     * 二分查找最长的重复不重叠子串长度
     * @param str 字符串
     * @return 最长重复不重叠子串长度
     */
    public static int findLongestDuplicateNonOverlappingSubstring(String str) {
        int left = 0;
        int right = str.length() / 2;
        int result = 0;
        
        // 二分答案
        while (left <= right) {
            int mid = (left + right) / 2;
            if (hasDuplicateNonOverlappingSubstring(str, mid)) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt(); // 字符串长度
        scanner.nextLine(); // 消费换行符
        String str = scanner.nextLine().trim(); // 字符串
        
        int result = findLongestDuplicateNonOverlappingSubstring(str);
        System.out.println(result);
        
        scanner.close();
    }
}