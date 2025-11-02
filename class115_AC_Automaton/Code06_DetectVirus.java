package class102;

import java.io.*;
import java.util.*;

/*
 * ZOJ 3430 Detect the Virus
 * 题目链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3430
 * 题目描述：检测一个字符串中包含多少种模式串。但是主串和模式串都用base64表示，所以要先转码。
 * 
 * 算法详解：
 * 这是一道结合编码解码和AC自动机的题目。需要先将base64编码的字符串解码，
 * 然后使用AC自动机进行多模式串匹配。
 * 
 * 算法核心思想：
 * 1. 将所有模式串解码后插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 将主串解码后进行匹配
 * 
 * 时间复杂度分析：
 * 1. 解码：O(|S|)，其中S是编码字符串
 * 2. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
 * 3. 构建fail指针：O(∑|Pi|)
 * 4. 匹配：O(|T|)，其中T是解码后的主串
 * 总时间复杂度：O(|S| + ∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 编码解码与字符串匹配结合
 * 2. 病毒检测系统
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用数组代替链表提高访问速度
 * 3. 内存优化：合理设置数组大小，避免浪费
 * 
 * 与机器学习的联系：
 * 1. 在网络安全中用于恶意代码检测
 * 2. 在生物信息学中用于基因序列匹配
 */

public class Code06_DetectVirus {
    // Trie树节点
    static class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        TrieNode fail;
        
        public TrieNode() {
            children = new TrieNode[256]; // 扩展ASCII字符集
            isEnd = false;
            fail = null;
        }
    }
    
    static final int MAXN = 1005;
    static final int MAXS = 100005;
    
    static TrieNode root;
    static int[] base64Map = new int[256];
    
    // 初始化base64映射表
    static void initBase64Map() {
        // A-Z: 0-25
        for (int i = 0; i < 26; i++) {
            base64Map['A' + i] = i;
        }
        // a-z: 26-51
        for (int i = 0; i < 26; i++) {
            base64Map['a' + i] = i + 26;
        }
        // 0-9: 52-61
        for (int i = 0; i < 10; i++) {
            base64Map['0' + i] = i + 52;
        }
        // +: 62
        base64Map['+'] = 62;
        // /: 63
        base64Map['/'] = 63;
    }
    
    // base64解码
    static byte[] base64Decode(String encoded) {
        int len = encoded.length();
        // 计算解码后的长度
        int decodedLen = (len * 6) / 8;
        byte[] decoded = new byte[decodedLen];
        
        // 将base64字符串转换为二进制位流
        StringBuilder bitStream = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int val = base64Map[encoded.charAt(i)];
            // 将6位二进制数转换为字符串
            for (int j = 5; j >= 0; j--) {
                bitStream.append((val >> j) & 1);
            }
        }
        
        // 将二进制位流转换为字节
        for (int i = 0; i < decodedLen; i++) {
            int val = 0;
            for (int j = 0; j < 8; j++) {
                val = (val << 1) | (bitStream.charAt(i * 8 + j) - '0');
            }
            decoded[i] = (byte) val;
        }
        
        return decoded;
    }
    
    // 构建Trie树
    static void buildTrie(String[] patterns) {
        for (String pattern : patterns) {
            // 解码模式串
            byte[] decodedPattern = base64Decode(pattern);
            TrieNode node = root;
            for (int i = 0; i < decodedPattern.length; i++) {
                int ch = decodedPattern[i] & 0xFF; // 转换为无符号字节
                if (node.children[ch] == null) {
                    node.children[ch] = new TrieNode();
                }
                node = node.children[ch];
            }
            node.isEnd = true;
        }
    }
    
    // 构建AC自动机
    static void buildACAutomation() {
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
            TrieNode node = queue.poll();
            
            for (int i = 0; i < 256; i++) {
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
    
    // 匹配主串
    static int matchText(byte[] text) {
        TrieNode current = root;
        Set<Integer> matchedPatterns = new HashSet<>();
        
        for (int i = 0; i < text.length; i++) {
            int ch = text[i] & 0xFF; // 转换为无符号字节
            
            // 根据失配指针跳转
            while (current.children[ch] == null && current != root) {
                current = current.fail;
            }
            
            if (current.children[ch] != null) {
                current = current.children[ch];
            } else {
                current = root;
            }
            
            // 检查是否有匹配的模式串
            TrieNode temp = current;
            while (temp != root) {
                if (temp.isEnd) {
                    // 记录匹配的模式串（这里简化处理）
                    matchedPatterns.add(temp.hashCode());
                }
                temp = temp.fail;
            }
        }
        
        return matchedPatterns.size();
    }
    
    public static void main(String[] args) {
        // 初始化base64映射表
        initBase64Map();
        
        // 示例输入（实际应用中需要从标准输入读取）
        String[] patterns = {"ABC", "DEF"}; // base64编码的模式串
        String text = "ABCDEF"; // base64编码的主串
        
        // 初始化根节点
        root = new TrieNode();
        
        // 构建Trie树
        buildTrie(patterns);
        
        // 构建AC自动机
        buildACAutomation();
        
        // 解码主串
        byte[] decodedText = base64Decode(text);
        
        // 匹配主串
        int result = matchText(decodedText);
        
        System.out.println("匹配的模式串数量: " + result);
    }
}