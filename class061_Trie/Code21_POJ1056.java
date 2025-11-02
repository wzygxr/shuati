package class045_Trie;

import java.util.*;

/**
 * POJ 1056 IMMEDIATE DECODABILITY
 * 
 * 题目描述：
 * 判断一组二进制编码是否具有即时可解码性。如果任何一个编码不是其他编码的前缀，则称这组编码是即时可解码的。
 * 
 * 解题思路：
 * 1. 使用Trie树存储所有二进制编码
 * 2. 在插入过程中检查是否存在前缀关系
 * 3. 如果在插入一个编码时，发现路径上有已经标记为完整编码的节点，或者插入完成后当前节点有子节点，
 *    则说明不具有即时可解码性
 * 
 * 时间复杂度：O(N*M)，其中N是编码数量，M是平均编码长度
 * 空间复杂度：O(N*M)
 */
public class Code21_POJ1056 {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 子节点数组，对应0和1
        boolean isEnd = false; // 标记是否为一个完整编码的结尾
    }
    
    static TrieNode root;
    static int setNumber = 1; // 组号
    
    /**
     * 初始化Trie树
     */
    public static void init() {
        root = new TrieNode();
    }
    
    /**
     * 向Trie树中插入一个二进制编码，并检查是否存在前缀关系
     * @param code 要插入的二进制编码
     * @return 如果存在前缀关系返回true，否则返回false
     */
    public static boolean insert(String code) {
        if (code == null || code.length() == 0) {
            return false;
        }
        
        TrieNode node = root;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            int index = c - '0'; // '0'->0, '1'->1
            
            // 如果子节点不存在，创建新节点
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            
            node = node.children[index];
            
            // 如果当前节点已经是某个完整编码的结尾，说明当前编码是另一个编码的前缀
            if (node.isEnd) {
                return true;
            }
        }
        
        // 标记当前节点为完整编码的结尾
        node.isEnd = true;
        
        // 检查当前节点是否有子节点，如果有说明存在前缀关系
        for (int i = 0; i < 2; i++) {
            if (node.children[i] != null) {
                return true;
            }
        }
        
        return false;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNext()) {
            init(); // 初始化Trie树
            
            boolean decodable = true;
            String line;
            
            // 读取一组编码，直到遇到分隔符"9"
            while (scanner.hasNext() && !(line = scanner.nextLine().trim()).equals("9")) {
                if (insert(line)) {
                    decodable = false;
                }
            }
            
            // 输出结果
            if (decodable) {
                System.out.println("Set " + setNumber + " is immediately decodable");
            } else {
                System.out.println("Set " + setNumber + " is not immediately decodable");
            }
            
            setNumber++; // 组号递增
        }
        
        scanner.close();
    }
}