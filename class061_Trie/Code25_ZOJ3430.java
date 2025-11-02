package class045_Trie;

import java.util.*;

/**
 * ZOJ 3430 Detect the Virus
 * 
 * 题目描述：
 * 使用Trie树检测文件中的病毒代码。给定一些病毒代码模式和一些文件，判断每个文件是否包含病毒代码。
 * 
 * 解题思路：
 * 1. 使用AC自动机构建所有病毒代码模式的匹配机
 * 2. 对每个文件进行匹配，判断是否包含病毒代码
 * 3. 由于这是Trie专题，我们使用Trie树来实现简单的模式匹配
 * 
 * 时间复杂度：O(∑len(patterns) + ∑len(files))
 * 空间复杂度：O(∑len(patterns))
 */
public class Code25_ZOJ3430 {
    
    // Trie树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[256]; // 子节点数组，对应所有可能的字节值
        boolean isEnd = false; // 标记是否为一个模式的结尾
        TrieNode fail; // 失配指针（用于AC自动机）
    }
    
    static TrieNode root = new TrieNode(); // Trie树根节点
    
    /**
     * 向Trie树中插入一个模式
     * @param pattern 要插入的模式
     */
    public static void insert(byte[] pattern) {
        TrieNode node = root;
        for (byte b : pattern) {
            int index = b & 0xFF; // 将byte转换为0-255的索引
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true; // 标记为模式的结尾
    }
    
    /**
     * 构建失配指针（AC自动机的一部分）
     */
    public static void buildFailPointer() {
        Queue<TrieNode> queue = new LinkedList<>();
        
        // 初始化根节点的失配指针
        for (int i = 0; i < 256; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.offer(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }
        
        // BFS构建失配指针
        while (!queue.isEmpty()) {
            TrieNode current = queue.poll();
            
            for (int i = 0; i < 256; i++) {
                if (current.children[i] != null) {
                    TrieNode failNode = current.fail;
                    
                    while (failNode.children[i] == null) {
                        failNode = failNode.fail;
                    }
                    
                    current.children[i].fail = failNode.children[i];
                    
                    // 如果失配节点是模式结尾，则当前节点也是模式结尾
                    if (current.children[i].fail.isEnd) {
                        current.children[i].isEnd = true;
                    }
                    
                    queue.offer(current.children[i]);
                }
            }
        }
    }
    
    /**
     * 在文本中查找所有模式
     * @param text 要搜索的文本
     * @return 是否找到任何模式
     */
    public static boolean search(byte[] text) {
        TrieNode node = root;
        
        for (byte b : text) {
            int index = b & 0xFF; // 将byte转换为0-255的索引
            
            while (node.children[index] == null) {
                node = node.fail;
            }
            
            node = node.children[index];
            
            // 如果找到模式结尾，返回true
            if (node.isEnd) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 解码Base64字符串为字节数组
     * @param base64 Base64编码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextInt()) {
            int n = scanner.nextInt(); // 病毒代码模式数量
            scanner.nextLine(); // 消费换行符
            
            // 重新初始化Trie树
            root = new TrieNode();
            
            // 读取所有病毒代码模式
            for (int i = 0; i < n; i++) {
                String base64Pattern = scanner.nextLine().trim();
                byte[] pattern = decodeBase64(base64Pattern);
                insert(pattern);
            }
            
            // 构建失配指针
            buildFailPointer();
            
            int m = scanner.nextInt(); // 文件数量
            scanner.nextLine(); // 消费换行符
            
            // 处理每个文件
            for (int i = 0; i < m; i++) {
                String base64File = scanner.nextLine().trim();
                byte[] file = decodeBase64(base64File);
                
                // 搜索病毒代码
                if (search(file)) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO");
                }
            }
            
            // 输出空行分隔不同测试用例
            System.out.println();
        }
        
        scanner.close();
    }
}