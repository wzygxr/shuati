package class_advanced_algorithms.ac_automaton;

import java.util.*;

/**
 * AC自动机实现 (Aho-Corasick Automaton)
 * 
 * AC自动机是一种多模式字符串匹配算法，能够在一次扫描中同时匹配多个模式串。
 * 
 * 算法原理：
 * 1. 构建Trie树：将所有模式串插入到Trie树中
 * 2. 构建fail指针：为Trie树中的每个节点构建失败指针
 * 3. 匹配过程：在文本串中进行匹配，利用fail指针避免重复匹配
 * 
 * 时间复杂度：
 * - 预处理：O(∑|Pi|)，其中∑|Pi|是所有模式串长度之和
 * - 匹配：O(n + z)，其中n是文本串长度，z是匹配次数
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中|Σ|是字符集大小
 * 
 * 优势：
 * 1. 支持多模式匹配
 * 2. 匹配效率高
 * 3. 适合处理大量模式串的场景
 * 
 * 劣势：
 * 1. 实现复杂度较高
 * 2. 空间消耗较大
 * 3. 对于少量模式串可能不如KMP算法高效
 * 
 * 应用场景：
 * 1. 关键词过滤
 * 2. 病毒特征码检测
 * 3. 生物信息学中的序列匹配
 * 4. 网络入侵检测
 */
public class ACAutomaton {
    
    // 字符集大小（假设只处理小写字母）
    private static final int CHARSET_SIZE = 26;
    
    /**
     * Trie树节点
     */
    private static class Node {
        Node[] children;  // 子节点数组
        Node fail;        // 失败指针
        List<Integer> output; // 输出列表，存储以该节点结尾的模式串索引
        boolean isEnd;    // 是否为某个模式串的结尾
        
        Node() {
            children = new Node[CHARSET_SIZE];
            fail = null;
            output = new ArrayList<>();
            isEnd = false;
        }
    }
    
    private Node root;           // 根节点
    private List<String> patterns; // 模式串列表
    
    /**
     * 构造函数
     */
    public ACAutomaton() {
        root = new Node();
        patterns = new ArrayList<>();
    }
    
    /**
     * 添加模式串
     * @param pattern 模式串
     */
    public void addPattern(String pattern) {
        patterns.add(pattern);
        Node current = root;
        
        // 将模式串插入到Trie树中
        for (char c : pattern.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new Node();
            }
            current = current.children[index];
        }
        
        // 标记该节点为模式串结尾
        current.isEnd = true;
        current.output.add(patterns.size() - 1);
    }
    
    /**
     * 构建失败指针
     */
    public void buildFailPointer() {
        Queue<Node> queue = new LinkedList<>();
        
        // 初始化根节点的子节点的失败指针
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (root.children[i] != null) {
                root.children[i].fail = root;
                queue.offer(root.children[i]);
            } else {
                root.children[i] = root;
            }
        }
        
        // BFS构建失败指针
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (current.children[i] != null) {
                    Node child = current.children[i];
                    Node failNode = current.fail;
                    
                    // 找到失败指针指向的节点
                    while (failNode.children[i] == null) {
                        failNode = failNode.fail;
                    }
                    
                    child.fail = failNode.children[i];
                    
                    // 合并输出列表
                    if (child.fail.isEnd) {
                        child.output.addAll(child.fail.output);
                    }
                    
                    queue.offer(child);
                }
            }
        }
    }
    
    /**
     * 在文本中查找所有模式串
     * @param text 文本串
     * @return 匹配结果列表，每个元素包含模式串索引和在文本中的位置
     */
    public List<MatchResult> search(String text) {
        List<MatchResult> results = new ArrayList<>();
        Node current = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int index = c - 'a';
            
            // 如果当前节点没有对应的子节点，则沿着失败指针查找
            while (current.children[index] == null && current != root) {
                current = current.fail;
            }
            
            // 移动到下一个节点
            if (current.children[index] != null) {
                current = current.children[index];
            }
            
            // 检查是否有模式串匹配
            if (current.isEnd || !current.output.isEmpty()) {
                for (int patternIndex : current.output) {
                    String pattern = patterns.get(patternIndex);
                    int position = i - pattern.length() + 1;
                    results.add(new MatchResult(patternIndex, pattern, position));
                }
            }
        }
        
        return results;
    }
    
    /**
     * 在文本中查找所有模式串（优化版本）
     * @param text 文本串
     * @return 匹配结果列表，每个元素包含模式串索引和在文本中的位置
     */
    public List<MatchResult> searchOptimized(String text) {
        List<MatchResult> results = new ArrayList<>();
        Node current = root;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int index = c - 'a';
            
            // 沿着失败指针查找直到找到匹配的子节点或回到根节点
            while (current.children[index] == null && current != root) {
                current = current.fail;
            }
            
            // 移动到下一个节点
            if (current.children[index] != null) {
                current = current.children[index];
            }
            
            // 沿着失败指针收集所有匹配结果
            Node temp = current;
            while (temp != root) {
                if (temp.isEnd) {
                    for (int patternIndex : temp.output) {
                        String pattern = patterns.get(patternIndex);
                        int position = i - pattern.length() + 1;
                        results.add(new MatchResult(patternIndex, pattern, position));
                    }
                }
                temp = temp.fail;
            }
        }
        
        return results;
    }
    
    /**
     * 获取模式串数量
     * @return 模式串数量
     */
    public int getPatternCount() {
        return patterns.size();
    }
    
    /**
     * 获取所有模式串
     * @return 模式串列表
     */
    public List<String> getPatterns() {
        return new ArrayList<>(patterns);
    }
    
    /**
     * 匹配结果类
     */
    public static class MatchResult {
        public final int patternIndex;  // 模式串索引
        public final String pattern;    // 模式串
        public final int position;      // 在文本中的位置
        
        public MatchResult(int patternIndex, String pattern, int position) {
            this.patternIndex = patternIndex;
            this.pattern = pattern;
            this.position = position;
        }
        
        @Override
        public String toString() {
            return String.format("Pattern[%d]='%s' at position %d", patternIndex, pattern, position);
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建AC自动机
        ACAutomaton ac = new ACAutomaton();
        
        // 添加模式串
        ac.addPattern("he");
        ac.addPattern("she");
        ac.addPattern("his");
        ac.addPattern("hers");
        
        // 构建失败指针
        ac.buildFailPointer();
        
        // 测试文本
        String text = "ushers";
        System.out.println("文本: " + text);
        System.out.println("模式串: " + ac.getPatterns());
        
        // 搜索匹配
        List<MatchResult> results = ac.searchOptimized(text);
        System.out.println("\n匹配结果:");
        for (MatchResult result : results) {
            System.out.println(result);
        }
        
        // 更复杂的测试
        System.out.println("\n=== 复杂测试 ===");
        ACAutomaton ac2 = new ACAutomaton();
        ac2.addPattern("abc");
        ac2.addPattern("bcd");
        ac2.addPattern("cde");
        ac2.addPattern("abcdef");
        ac2.buildFailPointer();
        
        String text2 = "abcdefg";
        System.out.println("文本: " + text2);
        System.out.println("模式串: " + ac2.getPatterns());
        
        List<MatchResult> results2 = ac2.searchOptimized(text2);
        System.out.println("\n匹配结果:");
        for (MatchResult result : results2) {
            System.out.println(result);
        }
    }
}