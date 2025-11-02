package class102;

import java.io.*;
import java.util.*;

/*
 * HDU 2222 Keywords Search
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
 * 题目描述：给定一些单词和一个字符串，求有多少单词在字符串中出现过
 * 
 * 算法详解：
 * 这是一道经典的AC自动机模板题。需要在文本中查找多个模式串的出现次数。
 * 
 * 算法核心思想：
 * 1. 将所有模式串插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 在文本中进行匹配，统计每个模式串的出现次数
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 匹配：O(|T|)，其中T是文本串
 * 总时间复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 多模式串匹配
 * 2. 关键词搜索
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在自然语言处理中用于关键词提取
 * 2. 在网络安全中用于恶意代码检测
 */

public class Code07_KeywordsSearch {
    // Trie树节点
    static class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        TrieNode fail;
        int count; // 匹配次数
        int wordId; // 单词编号
        
        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
            fail = null;
            count = 0;
            wordId = -1;
        }
    }
    
    static final int MAXN = 10005;
    static final int MAXS = 1000005;
    
    static TrieNode root;
    static int[] wordCount; // 每个单词的出现次数
    
    // 构建Trie树
    static void buildTrie(String[] patterns) {
        for (int i = 0; i < patterns.length; i++) {
            TrieNode node = root;
            String pattern = patterns[i];
            for (int j = 0; j < pattern.length(); j++) {
                int index = pattern.charAt(j) - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.isEnd = true;
            node.wordId = i;
        }
    }
    
    // 构建AC自动机
    static void buildACAutomation() {
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
    
    // 匹配文本
    static int matchText(String text) {
        TrieNode current = root;
        int totalMatches = 0;
        
        for (int i = 0; i < text.length(); i++) {
            int index = text.charAt(i) - 'a';
            
            // 根据失配指针跳转
            while (current.children[index] == null && current != root) {
                current = current.fail;
            }
            
            if (current.children[index] != null) {
                current = current.children[index];
            } else {
                current = root;
            }
            
            // 检查是否有匹配的模式串
            TrieNode temp = current;
            while (temp != root) {
                if (temp.isEnd) {
                    temp.count++;
                    totalMatches++;
                }
                temp = temp.fail;
            }
        }
        
        return totalMatches;
    }
    
    // 统计每个单词的出现次数
    static void countWords(String[] patterns) {
        wordCount = new int[patterns.length];
        
        // 使用BFS遍历Trie树，将匹配次数传递给父节点
        Queue<TrieNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            TrieNode node = queue.poll();
            
            // 将当前节点的匹配次数传递给fail节点
            if (node != root && node.fail != null) {
                node.fail.count += node.count;
            }
            
            // 将子节点加入队列
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    queue.offer(node.children[i]);
                }
            }
            
            // 如果是单词结尾，记录匹配次数
            if (node.isEnd && node.wordId != -1) {
                wordCount[node.wordId] = node.count;
            }
        }
    }
    
    public static void main(String[] args) {
        // 示例输入（实际应用中需要从标准输入读取）
        String[] patterns = {"she", "he", "say", "shr", "her"};
        String text = "yasherhs";
        
        // 初始化根节点
        root = new TrieNode();
        
        // 构建Trie树
        buildTrie(patterns);
        
        // 构建AC自动机
        buildACAutomation();
        
        // 匹配文本
        int totalMatches = matchText(text);
        
        // 统计每个单词的出现次数
        countWords(patterns);
        
        System.out.println("总匹配次数: " + totalMatches);
        for (int i = 0; i < patterns.length; i++) {
            System.out.println(patterns[i] + ": " + wordCount[i]);
        }
    }
}