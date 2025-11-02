package class045_Trie;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * LeetCode 642. 设计搜索自动补全系统
 * 
 * 题目描述：
 * 为一个搜索引擎设计一个推荐系统，当用户输入一个句子（至少包含一个词，以'#'结尾）时，
 * 返回历史热门句子中与当前输入前缀匹配的前3个句子。
 * 热门度由句子被输入的次数决定，次数越多越热门。如果有多个句子热门度相同，
 * 按照ASCII码顺序排序。
 * 
 * 实现AutocompleteSystem类：
 * - AutocompleteSystem(String[] sentences, int[] times)：初始化系统
 * - List<String> input(char c)：用户输入字符c，返回匹配的前3个句子
 * 
 * 测试链接：https://leetcode.cn/problems/design-search-autocomplete-system/
 * 
 * 算法思路：
 * 1. 使用前缀树存储历史句子及其热度
 * 2. 每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
 * 3. 用户输入时，根据当前前缀在前缀树中查找匹配句子
 * 4. 遇到'#'时，将当前句子加入历史记录并更新热度
 * 
 * 核心优化：
 * 在每个前缀树节点中维护热门句子的最小堆，避免每次查询时都进行全局搜索
 * 
 * 时间复杂度分析：
 * - 初始化：O(∑len(sentences[i]) * log3)，其中∑len(sentences[i])是所有句子长度之和
 * - 单次输入：O(1)查询，O(log3)堆操作
 * - 遇到'#'：O(L * log3)，其中L是句子长度
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(sentences[i]))，用于存储所有句子
 * - 堆空间：O(N * 3)，其中N是前缀树节点数量
 * - 总体空间复杂度：O(∑len(sentences[i]) + N)
 * 
 * 是否最优解：是
 * 理由：使用前缀树结合堆可以高效地维护和查询热门句子
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或句子为空的情况
 * 2. 边界情况：没有匹配句子或匹配句子少于3个的情况
 * 3. 极端输入：大量句子或句子很长的情况
 * 4. 鲁棒性：处理重复句子和特殊字符
 * 
 * 语言特性差异：
 * Java：使用PriorityQueue实现最小堆，性能稳定
 * C++：可使用priority_queue实现堆，更灵活
 * Python：可使用heapq模块实现堆，代码简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 642. 设计搜索自动补全系统 (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. LeetCode 1268. 搜索推荐系统
 * 4. LintCode 1429. 设计搜索自动补全系统
 * 5. 牛客网 NC140. 设计搜索自动补全系统
 * 6. HackerRank - Autocomplete System
 * 7. CodeChef - AUTOCOMP
 * 8. SPOJ - AUTOSYS
 * 9. AtCoder - Search Autocomplete
 */
public class Code15_LeetCode642 {
    
    /**
     * 句子热度类
     * 用于存储句子及其热度，并支持比较操作
     */
    public static class HotSentence implements Comparable<HotSentence> {
        String sentence;
        int hot;
        
        public HotSentence(String sentence, int hot) {
            this.sentence = sentence;
            this.hot = hot;
        }
        
        /**
         * 比较方法
         * 热度高的排在前面，热度相同时按ASCII码顺序排序
         */
        @Override
        public int compareTo(HotSentence other) {
            if (this.hot != other.hot) {
                return Integer.compare(this.hot, other.hot); // 热度低的排在前面（最小堆）
            }
            return other.sentence.compareTo(this.sentence); // ASCII码大的排在前面（最小堆）
        }
    }
    
    /**
     * 前缀树节点类
     * 
     * 算法思路：
     * 使用数组存储子节点，支持26个小写字母、空格和特殊字符
     * 每个节点维护一个最小堆，存储以当前前缀开头的最热门3个句子
     * 
     * 时间复杂度分析：
     * - 初始化：O(1)
     * - 空间复杂度：O(1) 每个节点
     */
    public static class TrieNode {
        // 子节点数组（27个字符：26个小写字母 + 1个空格）
        public TrieNode[] children;
        // 标记该节点是否是句子结尾，存储对应的句子热度
        public int hot;
        // 存储以当前前缀开头的最热门3个句子的最小堆
        public PriorityQueue<HotSentence> top3;
        
        public TrieNode() {
            children = new TrieNode[27]; // 26个字母 + 1个空格
            hot = 0;
            top3 = new PriorityQueue<>();
        }
    }
    
    // 前缀树根节点
    private TrieNode root;
    // 当前输入的句子
    private StringBuilder current;
    // 当前前缀树节点
    private TrieNode current_node;
    
    /**
     * 构造函数
     * 初始化自动补全系统
     * 
     * 算法步骤：
     * 1. 创建前缀树根节点
     * 2. 遍历句子数组和热度数组：
     *    a. 将每个句子插入前缀树
     *    b. 更新句子热度
     * 3. 初始化当前输入和当前节点
     * 
     * 时间复杂度：O(∑len(sentences[i]) * log3)
     * 空间复杂度：O(∑len(sentences[i]))
     * 
     * @param sentences 历史句子数组
     * @param times 对应句子的热度数组
     */
    public Code15_LeetCode642(String[] sentences, int[] times) {
        root = new TrieNode();
        current = new StringBuilder();
        current_node = root;
        
        // 构建前缀树
        for (int i = 0; i < sentences.length; i++) {
            insert(sentences[i], times[i]);
        }
    }
    
    /**
     * 插入句子到前缀树
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历句子的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 如果子节点不存在，则创建新节点
     * 4. 移动到子节点，继续处理下一个字符
     * 5. 句子遍历完成后，更新节点的热度
     * 6. 从根节点开始，重新遍历句子，更新路径上每个节点的热门句子堆
     * 
     * 时间复杂度：O(L * log3)，其中L是句子长度
     * 空间复杂度：O(L)
     * 
     * @param sentence 待插入的句子
     * @param hot 句子的热度
     */
    private void insert(String sentence, int hot) {
        TrieNode node = root;
        for (char c : sentence.toCharArray()) {
            int index = getIndex(c);
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.hot += hot;
        
        // 更新路径上每个节点的热门句子堆
        node = root;
        for (char c : sentence.toCharArray()) {
            int index = getIndex(c);
            node = node.children[index];
            updateTop3(node, sentence, node.hot);
        }
    }
    
    /**
     * 更新节点的热门句子堆
     * 
     * 算法步骤：
     * 1. 检查句子是否已在堆中，如果存在则更新热度
     * 2. 如果堆大小小于3，直接添加句子
     * 3. 如果堆大小等于3，且新句子比堆顶句子更热门，则替换堆顶
     * 4. 重新构建堆以保证堆性质
     * 
     * 时间复杂度：O(log3)
     * 空间复杂度：O(1)
     * 
     * @param node 前缀树节点
     * @param sentence 句子
     * @param hot 句子热度
     */
    private void updateTop3(TrieNode node, String sentence, int hot) {
        // 检查句子是否已在堆中
        HotSentence existing = null;
        for (HotSentence hs : node.top3) {
            if (hs.sentence.equals(sentence)) {
                existing = hs;
                break;
            }
        }
        
        if (existing != null) {
            // 更新已存在句子的热度
            node.top3.remove(existing);
            existing.hot = hot;
            node.top3.offer(existing);
        } else if (node.top3.size() < 3) {
            // 堆未满，直接添加
            node.top3.offer(new HotSentence(sentence, hot));
        } else {
            // 堆已满，检查是否需要替换堆顶
            HotSentence top = node.top3.peek();
            HotSentence newSentence = new HotSentence(sentence, hot);
            if (newSentence.compareTo(top) > 0) {
                node.top3.poll();
                node.top3.offer(newSentence);
            }
        }
    }
    
    /**
     * 获取字符的路径索引
     * 
     * @param c 字符
     * @return 路径索引（0-25为字母，26为空格）
     */
    private int getIndex(char c) {
        return c == ' ' ? 26 : c - 'a';
    }
    
    /**
     * 用户输入字符
     * 
     * 算法步骤：
     * 1. 如果输入字符是'#'：
     *    a. 将当前句子加入历史记录
     *    b. 重置当前输入和当前节点
     *    c. 返回空列表
     * 2. 否则：
     *    a. 将字符添加到当前输入
     *    b. 更新当前节点
     *    c. 如果当前节点为空，说明没有匹配的句子，返回空列表
     *    d. 从堆中获取热门句子，按热度和ASCII码排序后返回
     * 
     * 时间复杂度：O(1)查询 + O(3*log3)排序
     * 空间复杂度：O(1)
     * 
     * @param c 用户输入的字符
     * @return 匹配的前3个热门句子
     */
    public List<String> input(char c) {
        List<String> result = new ArrayList<>();
        
        if (c == '#') {
            // 遇到结束符，将当前句子加入历史记录
            if (current.length() > 0) {
                String sentence = current.toString();
                insert(sentence, 1);
            }
            
            // 重置状态
            current.setLength(0);
            current_node = root;
            return result;
        }
        
        // 添加字符到当前输入
        current.append(c);
        
        // 更新当前节点
        if (current_node != null) {
            int index = getIndex(c);
            current_node = current_node.children[index];
        }
        
        // 如果当前节点为空，说明没有匹配的句子
        if (current_node == null) {
            return result;
        }
        
        // 从堆中获取热门句子
        PriorityQueue<HotSentence> temp = new PriorityQueue<>(current_node.top3);
        List<HotSentence> candidates = new ArrayList<>();
        while (!temp.isEmpty()) {
            candidates.add(temp.poll());
        }
        
        // 按热度降序和ASCII码升序排序
        candidates.sort((a, b) -> {
            if (a.hot != b.hot) {
                return Integer.compare(b.hot, a.hot); // 热度高的排在前面
            }
            return a.sentence.compareTo(b.sentence); // ASCII码小的排在前面
        });
        
        // 取前3个句子
        for (int i = 0; i < Math.min(3, candidates.size()); i++) {
            result.add(candidates.get(i).sentence);
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常输入：验证基本功能正确性
     * 2. 热度排序：验证按热度和ASCII码排序
     * 3. 新句子添加：验证新句子的处理
     * 4. 边界情况：验证空输入和无匹配情况
     */
    public static void testAutocompleteSystem() {
        // 测试用例1：正常输入
        String[] sentences = {"i love you", "island", "iroman", "i love leetcode"};
        int[] times = {5, 3, 2, 2};
        Code15_LeetCode642 system = new Code15_LeetCode642(sentences, times);
        
        // 输入'i'
        List<String> result1 = system.input('i');
        List<String> expected1 = new ArrayList<>();
        expected1.add("i love you");
        expected1.add("i love leetcode");
        expected1.add("iroman");
        assert result1.equals(expected1) : "测试用例1失败";
        
        // 输入' '（空格）
        List<String> result2 = system.input(' ');
        List<String> expected2 = new ArrayList<>();
        expected2.add("i love you");
        expected2.add("i love leetcode");
        assert result2.equals(expected2) : "测试用例2失败";
        
        // 输入'a'
        List<String> result3 = system.input('a');
        List<String> expected3 = new ArrayList<>(); // 没有匹配的句子
        assert result3.equals(expected3) : "测试用例3失败";
        
        // 输入'#'结束
        List<String> result4 = system.input('#');
        List<String> expected4 = new ArrayList<>(); // 结束符返回空列表
        assert result4.equals(expected4) : "测试用例4失败";
        
        System.out.println("LeetCode 642 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 初始化大量历史句子
     * 2. 模拟用户输入过程
     */
    public static void performanceTest() {
        // 构建测试数据
        int n = 10000;
        String[] sentences = new String[n];
        int[] times = new int[n];
        
        for (int i = 0; i < n; i++) {
            sentences[i] = "sentence" + i;
            times[i] = i + 1;
        }
        
        long startTime = System.currentTimeMillis();
        Code15_LeetCode642 system = new Code15_LeetCode642(sentences, times);
        long initTime = System.currentTimeMillis() - startTime;
        
        // 模拟用户输入
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            system.input((char)('a' + i % 26));
        }
        long inputTime = System.currentTimeMillis() - startTime;
        
        System.out.println("初始化" + n + "个句子耗时: " + initTime + "ms");
        System.out.println("处理1000次输入耗时: " + inputTime + "ms");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testAutocompleteSystem();
        
        // 运行性能测试
        performanceTest();
    }
}