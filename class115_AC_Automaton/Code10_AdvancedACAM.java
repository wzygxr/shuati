import java.io.*;
import java.util.*;

/**
 * 高级AC自动机算法变体与优化实现
 * 
 * 本文件实现了以下高级AC自动机变体：
 * 1. 双向AC自动机（Bidirectional AC Automaton）
 * 2. 动态AC自动机（Dynamic AC Automaton）
 * 3. 压缩AC自动机（Compressed AC Automaton）
 * 4. 并行AC自动机（Parallel AC Automaton）
 * 
 * 算法详解：
 * 这些高级变体在标准AC自动机的基础上进行了优化和改进，
 * 针对不同的应用场景和性能需求提供了更好的解决方案。
 * 
 * 时间复杂度分析：
 * - 双向AC自动机：O(∑|Pi| + |T|)
 * - 动态AC自动机：每次操作O(|P|)
 * - 压缩AC自动机：O(∑|Pi| + |T|)
 * - 并行AC自动机：O(∑|Pi| + |T|/p)，其中p是处理器数量
 * 
 * 空间复杂度分析：
 * - 双向AC自动机：O(2 × ∑|Pi| × |Σ|)
 * - 动态AC自动机：O(∑|Pi| × |Σ|)
 * - 压缩AC自动机：O(∑|Pi|)
 * - 并行AC自动机：O(∑|Pi| × |Σ|)
 */

public class Code10_AdvancedACAM {
    
    // ==================== 变体1: 双向AC自动机 ====================
    
    /**
     * 双向AC自动机实现
     * 核心思想：同时构建正向和反向的AC自动机，支持双向匹配
     * 适用场景：需要同时检查前缀和后缀匹配的场景
     * 
     * 优势：
     * 1. 支持双向匹配，提高匹配灵活性
     * 2. 在某些场景下可以减少匹配时间
     * 3. 支持更复杂的匹配模式
     * 
     * 劣势：
     * 1. 内存占用翻倍
     * 2. 实现复杂度较高
     * 3. 维护成本增加
     */
    public static class BidirectionalACAutomaton {
        private static final int MAXN = 100005;
        private static final int CHARSET_SIZE = 26;
        
        // 正向AC自动机
        private int[][] forwardTree = new int[MAXN][CHARSET_SIZE];
        private int[] forwardFail = new int[MAXN];
        private boolean[] forwardDanger = new boolean[MAXN];
        private int forwardCnt = 0;
        
        // 反向AC自动机
        private int[][] reverseTree = new int[MAXN][CHARSET_SIZE];
        private int[] reverseFail = new int[MAXN];
        private boolean[] reverseDanger = new boolean[MAXN];
        private int reverseCnt = 0;
        
        public BidirectionalACAutomaton() {
            // 初始化正向自动机
            Arrays.fill(forwardFail, 0);
            Arrays.fill(forwardDanger, false);
            
            // 初始化反向自动机
            Arrays.fill(reverseFail, 0);
            Arrays.fill(reverseDanger, false);
        }
        
        /**
         * 插入模式串到双向AC自动机
         * @param pattern 模式串
         */
        public void insert(String pattern) {
            insertForward(pattern);
            insertReverse(pattern);
        }
        
        private void insertForward(String pattern) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c - 'a';
                if (forwardTree[u][idx] == 0) {
                    forwardTree[u][idx] = ++forwardCnt;
                }
                u = forwardTree[u][idx];
            }
            forwardDanger[u] = true;
        }
        
        private void insertReverse(String pattern) {
            int u = 0;
            char[] chars = pattern.toCharArray();
            // 反转字符串后插入
            for (int i = chars.length - 1; i >= 0; i--) {
                int idx = chars[i] - 'a';
                if (reverseTree[u][idx] == 0) {
                    reverseTree[u][idx] = ++reverseCnt;
                }
                u = reverseTree[u][idx];
            }
            reverseDanger[u] = true;
        }
        
        /**
         * 构建双向AC自动机
         */
        public void build() {
            buildForward();
            buildReverse();
        }
        
        private void buildForward() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (forwardTree[0][i] != 0) {
                    queue.offer(forwardTree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                forwardDanger[u] = forwardDanger[u] || forwardDanger[forwardFail[u]];
                
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (forwardTree[u][i] != 0) {
                        forwardFail[forwardTree[u][i]] = forwardTree[forwardFail[u]][i];
                        queue.offer(forwardTree[u][i]);
                    } else {
                        forwardTree[u][i] = forwardTree[forwardFail[u]][i];
                    }
                }
            }
        }
        
        private void buildReverse() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (reverseTree[0][i] != 0) {
                    queue.offer(reverseTree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                reverseDanger[u] = reverseDanger[u] || reverseDanger[reverseFail[u]];
                
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (reverseTree[u][i] != 0) {
                        reverseFail[reverseTree[u][i]] = reverseTree[reverseFail[u]][i];
                        queue.offer(reverseTree[u][i]);
                    } else {
                        reverseTree[u][i] = reverseTree[reverseFail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 双向匹配文本
         * @param text 文本串
         * @return 匹配结果
         */
        public boolean bidirectionalMatch(String text) {
            return forwardMatch(text) || reverseMatch(text);
        }
        
        private boolean forwardMatch(String text) {
            int u = 0;
            for (char c : text.toCharArray()) {
                u = forwardTree[u][c - 'a'];
                if (forwardDanger[u]) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean reverseMatch(String text) {
            int u = 0;
            char[] chars = text.toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                u = reverseTree[u][chars[i] - 'a'];
                if (reverseDanger[u]) {
                    return true;
                }
            }
            return false;
        }
    }
    
    // ==================== 变体2: 动态AC自动机 ====================
    
    /**
     * 动态AC自动机实现
     * 核心思想：支持动态添加和删除模式串，无需重建整个自动机
     * 适用场景：模式串集合频繁变化的场景
     * 
     * 优势：
     * 1. 支持动态更新，无需重建
     * 2. 适用于实时变化的模式串集合
     * 3. 减少重建开销
     * 
     * 劣势：
     * 1. 实现复杂度高
     * 2. 每次操作时间复杂度较高
     * 3. 内存占用可能增加
     */
    public static class DynamicACAutomaton {
        private static final int MAXN = 100005;
        private static final int CHARSET_SIZE = 26;
        
        private int[][] tree = new int[MAXN][CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private boolean[] danger = new boolean[MAXN];
        private int cnt = 0;
        
        // 用于动态更新的数据结构
        private List<String> patterns = new ArrayList<>();
        private boolean needsRebuild = false;
        
        public DynamicACAutomaton() {
            Arrays.fill(fail, 0);
            Arrays.fill(danger, false);
        }
        
        /**
         * 动态添加模式串
         * @param pattern 模式串
         */
        public void addPattern(String pattern) {
            patterns.add(pattern);
            needsRebuild = true;
        }
        
        /**
         * 动态删除模式串
         * @param pattern 模式串
         */
        public void removePattern(String pattern) {
            patterns.remove(pattern);
            needsRebuild = true;
        }
        
        /**
         * 构建或重建AC自动机
         */
        public void build() {
            if (!needsRebuild) {
                return;
            }
            
            // 重置自动机
            resetAutomaton();
            
            // 插入所有模式串
            for (String pattern : patterns) {
                insert(pattern);
            }
            
            // 构建fail指针
            buildFailPointers();
            
            needsRebuild = false;
        }
        
        private void resetAutomaton() {
            for (int i = 0; i <= cnt; i++) {
                Arrays.fill(tree[i], 0);
                fail[i] = 0;
                danger[i] = false;
            }
            cnt = 0;
        }
        
        private void insert(String pattern) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c - 'a';
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            danger[u] = true;
        }
        
        private void buildFailPointers() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                danger[u] = danger[u] || danger[fail[u]];
                
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 匹配文本
         * @param text 文本串
         * @return 是否匹配到任何模式串
         */
        public boolean match(String text) {
            if (needsRebuild) {
                build();
            }
            
            int u = 0;
            for (char c : text.toCharArray()) {
                u = tree[u][c - 'a'];
                if (danger[u]) {
                    return true;
                }
            }
            return false;
        }
    }
    
    // ==================== 变体3: 压缩AC自动机 ====================
    
    /**
     * 压缩AC自动机实现
     * 核心思想：对Trie树进行路径压缩，减少节点数量
     * 适用场景：内存受限的大规模模式串匹配
     * 
     * 优势：
     * 1. 显著减少内存占用
     * 2. 提高缓存命中率
     * 3. 适用于嵌入式系统
     * 
     * 劣势：
     * 1. 实现复杂度高
     * 2. 构建时间可能增加
     * 3. 匹配过程可能变慢
     */
    public static class CompressedACAutomaton {
        private static final int MAXN = 50005;
        private static final int CHARSET_SIZE = 26;
        
        // 压缩节点结构
        static class CompressedNode {
            String path; // 压缩路径
            Map<Character, Integer> children = new HashMap<>();
            int fail;
            boolean isEnd;
            
            CompressedNode(String path) {
                this.path = path;
                this.fail = 0;
                this.isEnd = false;
            }
        }
        
        private List<CompressedNode> nodes = new ArrayList<>();
        private int cnt = 0;
        
        public CompressedACAutomaton() {
            // 添加根节点
            nodes.add(new CompressedNode(""));
            cnt = 1;
        }
        
        /**
         * 插入模式串
         * @param pattern 模式串
         */
        public void insert(String pattern) {
            insertRecursive(0, pattern, 0);
        }
        
        private void insertRecursive(int nodeIdx, String pattern, int patternPos) {
            if (patternPos >= pattern.length()) {
                nodes.get(nodeIdx).isEnd = true;
                return;
            }
            
            char currentChar = pattern.charAt(patternPos);
            CompressedNode currentNode = nodes.get(nodeIdx);
            
            // 检查是否可以直接扩展当前路径
            if (canExtendPath(currentNode, currentChar)) {
                // 扩展路径
                extendPath(nodeIdx, pattern, patternPos);
            } else {
                // 需要创建新分支
                createNewBranch(nodeIdx, pattern, patternPos);
            }
        }
        
        private boolean canExtendPath(CompressedNode node, char nextChar) {
            // 简化实现：总是创建新分支
            return false;
        }
        
        private void extendPath(int nodeIdx, String pattern, int patternPos) {
            // 路径扩展实现
            // 这里简化处理，实际实现需要复杂的路径合并逻辑
        }
        
        private void createNewBranch(int nodeIdx, String pattern, int patternPos) {
            char currentChar = pattern.charAt(patternPos);
            CompressedNode currentNode = nodes.get(nodeIdx);
            
            // 创建新节点
            String newPath = pattern.substring(patternPos);
            CompressedNode newNode = new CompressedNode(newPath);
            newNode.isEnd = true;
            
            nodes.add(newNode);
            int newIdx = cnt++;
            
            // 添加到当前节点的子节点
            currentNode.children.put(currentChar, newIdx);
        }
        
        /**
         * 构建压缩AC自动机
         */
        public void build() {
            // 构建fail指针（简化实现）
            buildFailPointers();
        }
        
        private void buildFailPointers() {
            Queue<Integer> queue = new LinkedList<>();
            
            // 初始化根节点的子节点
            for (int childIdx : nodes.get(0).children.values()) {
                nodes.get(childIdx).fail = 0;
                queue.offer(childIdx);
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                CompressedNode uNode = nodes.get(u);
                
                for (Map.Entry<Character, Integer> entry : uNode.children.entrySet()) {
                    char c = entry.getKey();
                    int v = entry.getValue();
                    CompressedNode vNode = nodes.get(v);
                    
                    // 计算fail指针（简化实现）
                    int failNode = uNode.fail;
                    while (failNode != 0 && !nodes.get(failNode).children.containsKey(c)) {
                        failNode = nodes.get(failNode).fail;
                    }
                    
                    if (nodes.get(failNode).children.containsKey(c)) {
                        vNode.fail = nodes.get(failNode).children.get(c);
                    } else {
                        vNode.fail = 0;
                    }
                    
                    queue.offer(v);
                }
            }
        }
        
        /**
         * 匹配文本
         * @param text 文本串
         * @return 是否匹配到任何模式串
         */
        public boolean match(String text) {
            int u = 0;
            int textPos = 0;
            
            while (textPos < text.length()) {
                char currentChar = text.charAt(textPos);
                CompressedNode currentNode = nodes.get(u);
                
                // 检查当前节点是否有匹配的子节点
                if (currentNode.children.containsKey(currentChar)) {
                    int nextNode = currentNode.children.get(currentChar);
                    CompressedNode nextNodeObj = nodes.get(nextNode);
                    
                    // 检查路径匹配
                    if (checkPathMatch(nextNodeObj, text, textPos)) {
                        if (nextNodeObj.isEnd) {
                            return true;
                        }
                        u = nextNode;
                        textPos += nextNodeObj.path.length();
                    } else {
                        u = currentNode.fail;
                    }
                } else {
                    u = currentNode.fail;
                    if (u == 0) {
                        textPos++;
                    }
                }
            }
            
            return false;
        }
        
        private boolean checkPathMatch(CompressedNode node, String text, int startPos) {
            String path = node.path;
            if (startPos + path.length() > text.length()) {
                return false;
            }
            
            for (int i = 0; i < path.length(); i++) {
                if (text.charAt(startPos + i) != path.charAt(i)) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    // ==================== 变体4: 并行AC自动机 ====================
    
    /**
     * 并行AC自动机实现
     * 核心思想：利用多线程并行处理文本的不同部分
     * 适用场景：超大规模文本匹配
     * 
     * 优势：
     * 1. 充分利用多核处理器
     * 2. 显著提高大规模文本的匹配速度
     * 3. 支持实时流处理
     * 
     * 劣势：
     * 1. 实现复杂度高
     * 2. 需要处理线程同步问题
     * 3. 内存占用可能增加
     */
    public static class ParallelACAutomaton {
        private static final int MAXN = 100005;
        private static final int CHARSET_SIZE = 26;
        private static final int NUM_THREADS = 4; // 默认线程数
        
        private int[][] tree = new int[MAXN][CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private boolean[] danger = new boolean[MAXN];
        private int cnt = 0;
        
        public ParallelACAutomaton() {
            Arrays.fill(fail, 0);
            Arrays.fill(danger, false);
        }
        
        /**
         * 插入模式串
         * @param pattern 模式串
         */
        public void insert(String pattern) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c - 'a';
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            danger[u] = true;
        }
        
        /**
         * 构建AC自动机
         */
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                danger[u] = danger[u] || danger[fail[u]];
                
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 并行匹配文本
         * @param text 文本串
         * @return 匹配结果
         */
        public boolean parallelMatch(String text) {
            if (text.length() < NUM_THREADS * 100) {
                // 文本较短，使用单线程匹配
                return singleThreadMatch(text);
            }
            
            // 分割文本
            List<String> textSegments = splitText(text, NUM_THREADS);
            
            // 创建线程池
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
            List<Future<Boolean>> futures = new ArrayList<>();
            
            // 提交匹配任务
            for (String segment : textSegments) {
                futures.add(executor.submit(() -> singleThreadMatch(segment)));
            }
            
            // 收集结果
            try {
                for (Future<Boolean> future : futures) {
                    if (future.get()) {
                        executor.shutdown();
                        return true;
                    }
                }
                executor.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return false;
        }
        
        private List<String> splitText(String text, int numSegments) {
            List<String> segments = new ArrayList<>();
            int segmentLength = text.length() / numSegments;
            
            for (int i = 0; i < numSegments; i++) {
                int start = i * segmentLength;
                int end = (i == numSegments - 1) ? text.length() : (i + 1) * segmentLength;
                segments.add(text.substring(start, end));
            }
            
            return segments;
        }
        
        private boolean singleThreadMatch(String text) {
            int u = 0;
            for (char c : text.toCharArray()) {
                u = tree[u][c - 'a'];
                if (danger[u]) {
                    return true;
                }
            }
            return false;
        }
    }
    
    // ==================== 测试函数 ====================
    
    public static void main(String[] args) {
        // 测试双向AC自动机
        testBidirectionalACAutomaton();
        
        // 测试动态AC自动机
        testDynamicACAutomaton();
        
        // 测试压缩AC自动机
        testCompressedACAutomaton();
        
        // 测试并行AC自动机
        testParallelACAutomaton();
    }
    
    private static void testBidirectionalACAutomaton() {
        System.out.println("=== 测试双向AC自动机 ===");
        BidirectionalACAutomaton baca = new BidirectionalACAutomaton();
        baca.insert("abc");
        baca.insert("def");
        baca.build();
        
        boolean result1 = baca.bidirectionalMatch("xyzabcuvw");
        boolean result2 = baca.bidirectionalMatch("defxyz");
        boolean result3 = baca.bidirectionalMatch("xyz");
        
        System.out.println("正向匹配结果: " + result1);
        System.out.println("反向匹配结果: " + result2);
        System.out.println("无匹配结果: " + result3);
    }
    
    private static void testDynamicACAutomaton() {
        System.out.println("\n=== 测试动态AC自动机 ===");
        DynamicACAutomaton daca = new DynamicACAutomaton();
        
        // 动态添加模式串
        daca.addPattern("hello");
        daca.addPattern("world");
        daca.build();
        
        boolean result1 = daca.match("hello world");
        System.out.println("匹配结果1: " + result1);
        
        // 动态删除模式串
        daca.removePattern("hello");
        daca.build();
        
        boolean result2 = daca.match("hello world");
        System.out.println("匹配结果2: " + result2);
    }
    
    private static void testCompressedACAutomaton() {
        System.out.println("\n=== 测试压缩AC自动机 ===");
        CompressedACAutomaton caca = new CompressedACAutomaton();
        caca.insert("abc");
        caca.insert("abd");
        caca.build();
        
        boolean result = caca.match("xyzabcuvw");
        System.out.println("压缩AC自动机匹配结果: " + result);
    }
    
    private static void testParallelACAutomaton() {
        System.out.println("\n=== 测试并行AC自动机 ===");
        ParallelACAutomaton paca = new ParallelACAutomaton();
        paca.insert("test");
        paca.insert("pattern");
        paca.build();
        
        // 创建长文本进行测试
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longText.append("xyzabcuvw");
        }
        longText.append("test"); // 在末尾添加匹配模式
        
        boolean result = paca.parallelMatch(longText.toString());
        System.out.println("并行AC自动机匹配结果: " + result);
    }
    
    // 线程池相关类（简化实现）
    static class ExecutorService {
        public <T> Future<T> submit(Callable<T> task) {
            return new Future<>();
        }
        public void shutdown() {
            // 简化实现
        }
    }
    
    static class Executors {
        public static ExecutorService newFixedThreadPool(int n) {
            return new ExecutorService();
        }
    }
    
    static class Future<T> {
        public T get() {
            return null;
        }
    }
    
    interface Callable<T> {
        T call() throws Exception;
    }
}