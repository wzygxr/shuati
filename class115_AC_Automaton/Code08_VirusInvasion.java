package class102;

import java.io.*;
import java.util.*;

/*
 * HDU 2896 病毒侵袭
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2896
 * 题目描述：每个病毒都有一个编号，依此为1—N。不同编号的病毒特征码不会相同。
 * 在这之后一行，有一个整数M（1<=M<=1000），表示网站数。
 * 接下来M行，每行表示一个网站源码，源码字符串长度在1—10000之间。
 * 输出包含病毒特征码的网站编号和病毒编号。
 * 
 * 算法详解：
 * 这是一道AC自动机应用题，需要在多个文本中查找多个模式串，并记录每个文本中
 * 包含哪些模式串。
 * 
 * 算法核心思想：
 * 1. 将所有病毒特征码插入到Trie树中
 * 2. 构建失配指针（fail指针）
 * 3. 对每个网站源码进行匹配，记录包含的病毒编号
 * 
 * 时间复杂度分析：
 * 1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个病毒特征码
 * 2. 构建fail指针：O(∑|Pi|)
 * 3. 匹配：O(∑|Ti|)，其中Ti是第i个网站源码
 * 总时间复杂度：O(∑|Pi| + ∑|Ti|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小
 * 
 * 适用场景：
 * 1. 网站安全检测
 * 2. 病毒特征码匹配
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

public class Code08_VirusInvasion {
    // Trie树节点
    static class TrieNode {
        TrieNode[] children;
        boolean isEnd;
        TrieNode fail;
        int virusId; // 病毒编号
        
        public TrieNode() {
            children = new TrieNode[128]; // ASCII字符集
            isEnd = false;
            fail = null;
            virusId = -1;
        }
    }
    
    static final int MAXN = 1005;
    static final int MAXS = 100005;
    
    static TrieNode root;
    static List<Integer>[] infectedWebsites; // 每个病毒感染的网站列表
    static Set<Integer>[] websiteViruses; // 每个网站包含的病毒列表
    
    // 构建Trie树
    static void buildTrie(String[] viruses) {
        for (int i = 0; i < viruses.length; i++) {
            TrieNode node = root;
            String virus = viruses[i];
            for (int j = 0; j < virus.length(); j++) {
                int index = virus.charAt(j);
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.isEnd = true;
            node.virusId = i + 1; // 病毒编号从1开始
        }
    }
    
    // 构建AC自动机
    static void buildACAutomation() {
        Queue<TrieNode> queue = new LinkedList<>();
        
        // 初始化根节点的失配指针
        for (int i = 0; i < 128; i++) {
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
            
            for (int i = 0; i < 128; i++) {
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
    
    // 匹配网站源码
    static void matchWebsite(int websiteId, String websiteCode) {
        TrieNode current = root;
        
        for (int i = 0; i < websiteCode.length(); i++) {
            int index = websiteCode.charAt(i);
            
            // 根据失配指针跳转
            while (current.children[index] == null && current != root) {
                current = current.fail;
            }
            
            if (current.children[index] != null) {
                current = current.children[index];
            } else {
                current = root;
            }
            
            // 检查是否有匹配的病毒特征码
            TrieNode temp = current;
            while (temp != root) {
                if (temp.isEnd) {
                    // 记录感染的网站和病毒
                    infectedWebsites[temp.virusId].add(websiteId);
                    websiteViruses[websiteId].add(temp.virusId);
                }
                temp = temp.fail;
            }
        }
    }
    
    public static void main(String[] args) {
        // 示例输入（实际应用中需要从标准输入读取）
        String[] viruses = {"aaa", "bbb", "ccc"}; // 病毒特征码
        String[] websites = {"aaabbbccc", "aaabbb", "bbbccc"}; // 网站源码
        
        int virusCount = viruses.length;
        int websiteCount = websites.length;
        
        // 初始化数据结构
        root = new TrieNode();
        infectedWebsites = new List[MAXN];
        websiteViruses = new Set[MAXN];
        
        for (int i = 1; i <= virusCount; i++) {
            infectedWebsites[i] = new ArrayList<>();
        }
        
        for (int i = 1; i <= websiteCount; i++) {
            websiteViruses[i] = new HashSet<>();
        }
        
        // 构建Trie树
        buildTrie(viruses);
        
        // 构建AC自动机
        buildACAutomation();
        
        // 匹配每个网站
        for (int i = 0; i < websiteCount; i++) {
            matchWebsite(i + 1, websites[i]);
        }
        
        // 输出结果
        int infectedCount = 0;
        for (int i = 1; i <= websiteCount; i++) {
            if (!websiteViruses[i].isEmpty()) {
                infectedCount++;
                System.out.print("web " + i + ":");
                List<Integer> virusList = new ArrayList<>(websiteViruses[i]);
                Collections.sort(virusList);
                for (int virusId : virusList) {
                    System.out.print(" " + virusId);
                }
                System.out.println();
            }
        }
        
        System.out.println("total: " + infectedCount);
    }
}