package class102;

import java.util.*;

/*
 * LeetCode 1032. Stream of Characters
 * 题目链接：https://leetcode.com/problems/stream-of-characters/
 * 题目描述：设计一个算法，接收一个字符流，并检查这些字符的后缀是否是字符串数组words中的一个字符串
 * 
 * 算法详解：
 * 这是一道典型的AC自动机应用题。由于需要检查字符流的后缀是否匹配模式串，
 * 我们可以将模式串反转后构建AC自动机，然后在字符流中进行匹配。
 * 
 * 算法核心思想：
 * 1. 将所有模式串反转后插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 在字符流中进行匹配，每次匹配当前字符，利用fail指针避免回溯
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 查询：O(|T|)，其中T是文本串
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 字符流匹配
 * 2. 后缀匹配问题
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在自然语言处理中用于实时文本分析
 * 2. 在网络安全中用于实时恶意代码检测
 */

public class Code04_StreamOfCharacters {
    // Trie树节点
    class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        TrieNode fail;

        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
            fail = null;
        }
    }

    private TrieNode root;
    private TrieNode current;
    private String[] words;

    public Code04_StreamOfCharacters(String[] words) {
        this.words = words;
        root = new TrieNode();
        current = root;
        
        // 构建Trie树
        buildTrie();
        
        // 构建AC自动机
        buildACAutomation();
    }
    
    // 构建Trie树
    private void buildTrie() {
        for (String word : words) {
            TrieNode node = root;
            // 反转字符串插入Trie树
            for (int i = word.length() - 1; i >= 0; i--) {
                int index = word.charAt(i) - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.isEnd = true;
        }
    }
    
    // 构建AC自动机
    private void buildACAutomation() {
        Queue<TrieNode> queue = new LinkedList<>();
        
        // 初始化根节点的失配指针
        for (int i = 0; i < 26; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.offer(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }
        
        // BFS构建失配指针
        while (!queue.isEmpty()) {
            TrieNode node = queue.poll();
            
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    TrieNode failNode = node.fail;
                    while (failNode.children[i] == null) {
                        failNode = failNode.fail;
                    }
                    node.children[i].fail = failNode.children[i];
                    queue.offer(node.children[i]);
                }
            }
        }
    }
    
    public boolean query(char letter) {
        // 根据失配指针跳转
        while (current.children[letter - 'a'] == null && current != root) {
            current = current.fail;
        }
        
        if (current.children[letter - 'a'] != null) {
            current = current.children[letter - 'a'];
        } else {
            current = root;
        }
        
        // 检查是否有匹配的模式串
        TrieNode temp = current;
        while (temp != root) {
            if (temp.isEnd) {
                return true;
            }
            temp = temp.fail;
        }
        
        return false;
    }
    
    // 测试方法
    public static void main(String[] args) {
        String[] words = {"cd", "f", "kl"};
        Code04_StreamOfCharacters streamChecker = new Code04_StreamOfCharacters(words);
        
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'};
        for (char letter : letters) {
            System.out.println("Query '" + letter + "': " + streamChecker.query(letter));
        }
    }
}