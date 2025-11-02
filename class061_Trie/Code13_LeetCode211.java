package class045_Trie;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 211. 添加与搜索单词 - 数据结构设计
 * 
 * 题目描述：
 * 请你设计一个数据结构，支持添加新单词和查找字符串是否与任何先前添加的字符串匹配。
 * 实现词典类 WordDictionary：
 * - WordDictionary() 初始化词典对象
 * - void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
 * - bool search(word) 如果数据结构中存在字符串与 word 匹配，则返回 true；否则返回 false。
 * word 中可能包含一些 '.'，每个 '.' 都可以表示任何一个字母。
 * 
 * 测试链接：https://leetcode.cn/problems/design-add-and-search-words-data-structure/
 * 
 * 算法思路：
 * 1. 使用前缀树（Trie）存储所有添加的单词
 * 2. 对于普通字符的搜索，按照标准前缀树搜索进行
 * 3. 对于包含 '.' 的搜索，使用深度优先搜索（DFS）尝试所有可能的字符路径
 * 
 * 核心优化：
 * 使用前缀树存储单词，对于包含通配符 '.' 的搜索使用DFS遍历所有可能路径
 * 
 * 时间复杂度分析：
 * - 添加单词：O(L)，其中L是单词长度
 * - 搜索单词：O(26^L)，其中L是单词长度（最坏情况，所有字符都是 '.'）
 * - 总体时间复杂度：O(L) 添加，O(26^L) 搜索
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L)，其中N是插入的单词数量，L是平均单词长度
 * - 递归栈空间：O(L)，其中L是最长单词的长度
 * - 总体空间复杂度：O(N*L + L)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地存储和查询单词，对于通配符搜索使用DFS是标准解法
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或单词为空的情况
 * 2. 边界情况：单词只包含 '.' 或不包含 '.' 的情况
 * 3. 极端输入：大量单词或单词很长的情况
 * 4. 鲁棒性：处理特殊字符和重复添加
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 211. 添加与搜索单词 - 数据结构设计 (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. LeetCode 212. 单词搜索 II
 * 4. LintCode 473. 单词的添加与查找
 * 5. 牛客网 NC138. 添加与搜索单词
 * 6. HackerRank - Word Search with Wildcards
 * 7. CodeChef - WILDCARD
 * 8. SPOJ - WSEARCH
 * 9. AtCoder - Wildcard Matching
 */
public class Code13_LeetCode211 {
    
    /**
     * 前缀树节点类
     * 
     * 算法思路：
     * 使用数组存储子节点，支持26个小写字母
     * 包含单词结尾标记
     * 
     * 时间复杂度分析：
     * - 初始化：O(1)
     * - 空间复杂度：O(1) 每个节点
     */
    public static class TrieNode {
        // 子节点数组（26个小写字母）
        public TrieNode[] children;
        // 标记该节点是否是单词结尾
        public boolean isEnd;
        
        /**
         * 构造函数
         * 
         * 时间复杂度：O(1)
         * 空间复杂度：O(1)
         */
        public TrieNode() {
            children = new TrieNode[26];
            isEnd = false;
        }
    }
    
    // 前缀树根节点
    private TrieNode root;
    
    /**
     * 构造函数
     * 初始化前缀树根节点
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public Code13_LeetCode211() {
        root = new TrieNode();
    }
    
    /**
     * 向前缀树中添加单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符，计算字符的路径索引（字符-'a'）
     * 3. 如果子节点不存在，则创建新节点
     * 4. 移动到子节点，继续处理下一个字符
     * 5. 单词遍历完成后，标记当前节点为单词结尾
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * @param word 待添加的单词
     */
    public void addWord(String word) {
        if (word == null || word.length() == 0) {
            return; // 空字符串不添加
        }
        
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }
    
    /**
     * 搜索单词是否存在于前缀树中
     * 
     * 算法步骤：
     * 1. 调用DFS方法从根节点开始搜索
     * 
     * 时间复杂度：O(26^L)，其中L是单词长度（最坏情况）
     * 空间复杂度：O(L)，递归栈空间
     * 
     * @param word 待搜索的单词（可能包含 '.' 通配符）
     * @return 如果单词存在返回true，否则返回false
     */
    public boolean search(String word) {
        if (word == null || word.length() == 0) {
            return false; // 空字符串不存在
        }
        
        return dfs(word, 0, root);
    }
    
    /**
     * 使用深度优先搜索查找单词
     * 
     * 算法步骤：
     * 1. 递归终止条件：已处理完所有字符
     *    a. 如果当前节点是单词结尾，返回true
     *    b. 否则返回false
     * 2. 处理当前字符：
     *    a. 如果是普通字符，检查对应子节点是否存在
     *    b. 如果是通配符 '.'，尝试所有可能的子节点
     * 3. 递归处理剩余字符
     * 
     * 时间复杂度：O(26^L)，其中L是单词长度（最坏情况）
     * 空间复杂度：O(L)，递归栈空间
     * 
     * @param word 待搜索的单词
     * @param index 当前处理的字符索引
     * @param node 当前前缀树节点
     * @return 如果能找到匹配的单词返回true，否则返回false
     */
    private boolean dfs(String word, int index, TrieNode node) {
        // 递归终止条件：已处理完所有字符
        if (index == word.length()) {
            return node.isEnd;
        }
        
        char c = word.charAt(index);
        
        // 处理当前字符
        if (c == '.') {
            // 通配符 '.'，尝试所有可能的子节点
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null && dfs(word, index + 1, node.children[i])) {
                    return true;
                }
            }
            return false;
        } else {
            // 普通字符，检查对应子节点是否存在
            int path = c - 'a';
            if (node.children[path] == null) {
                return false;
            }
            return dfs(word, index + 1, node.children[path]);
        }
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常添加和搜索：验证基本功能正确性
     * 2. 通配符搜索测试：验证 '.' 字符的处理
     * 3. 空字符串处理：验证边界条件处理
     * 4. 重复添加处理：验证重复操作的正确性
     * 5. 不存在的单词搜索：验证错误情况处理
     * 
     * 测试策略：
     * 1. 使用断言验证每个操作的正确性
     * 2. 覆盖各种边界条件和异常场景
     */
    public static void testWordDictionary() {
        Code13_LeetCode211 wordDictionary = new Code13_LeetCode211();
        
        // 测试用例1：正常添加和搜索
        wordDictionary.addWord("bad");
        wordDictionary.addWord("dad");
        wordDictionary.addWord("mad");
        
        assert !wordDictionary.search("pad"); // 应该返回false
        assert wordDictionary.search("bad");  // 应该返回true
        assert wordDictionary.search(".ad");  // 应该返回true
        assert wordDictionary.search("b..");  // 应该返回true
        
        // 测试用例2：空字符串处理
        assert !wordDictionary.search("");    // 空字符串应该返回false
        
        // 测试用例3：不存在的单词
        assert !wordDictionary.search("b..."); // 不存在的单词应该返回false
        
        // 测试用例4：重复添加
        wordDictionary.addWord("bad");
        assert wordDictionary.search("bad");  // 重复添加后搜索应该仍然返回true
        
        System.out.println("LeetCode 211 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 添加大量单词：测试添加操作的性能
     * 2. 搜索操作性能：测试普通搜索和通配符搜索的性能
     */
    public static void performanceTest() {
        Code13_LeetCode211 wordDictionary = new Code13_LeetCode211();
        
        long startTime = System.currentTimeMillis();
        
        // 添加10000个单词
        for (int i = 0; i < 10000; i++) {
            wordDictionary.addWord("word" + i);
        }
        
        long insertTime = System.currentTimeMillis() - startTime;
        System.out.println("添加10000个单词耗时: " + insertTime + "ms");
        
        startTime = System.currentTimeMillis();
        
        // 普通搜索1000次
        for (int i = 0; i < 1000; i++) {
            wordDictionary.search("word" + i);
        }
        
        long searchTime = System.currentTimeMillis() - startTime;
        System.out.println("普通搜索1000次耗时: " + searchTime + "ms");
        
        startTime = System.currentTimeMillis();
        
        // 通配符搜索100次
        for (int i = 0; i < 100; i++) {
            wordDictionary.search("w.rd" + i);
        }
        
        long wildcardTime = System.currentTimeMillis() - startTime;
        System.out.println("通配符搜索100次耗时: " + wildcardTime + "ms");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testWordDictionary();
        
        // 运行性能测试
        performanceTest();
    }
}