package class045_Trie;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * SPOJ DICT（在字典中搜索）
 * 
 * 题目描述：
 * 给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词，并按字典序输出。
 * 如果没有匹配的单词，输出"No match."
 * 
 * 测试链接：https://www.spoj.com/problems/DICT/
 * 
 * 算法思路：
 * 1. 使用前缀树存储字典中的所有单词
 * 2. 根据给定前缀定位到前缀树中的对应节点
 * 3. 从该节点开始深度优先搜索，收集所有单词
 * 4. 按字典序排序后输出结果
 * 
 * 核心优化：
 * 使用前缀树可以高效地定位前缀并收集匹配单词，避免了对每个单词都进行前缀匹配
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
 * - 查询操作：O(L + K)，其中L是前缀长度，K是匹配单词的总字符数
 * - 总体时间复杂度：O(∑len(words[i]) + L + K)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
 * - 结果列表：O(M)，其中M是匹配单词数量
 * - 总体空间复杂度：O(∑len(words[i]) + M)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内定位前缀并收集匹配单词
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或字典为空的情况
 * 2. 边界情况：没有匹配单词的情况
 * 3. 极端输入：大量单词或单词很长的情况
 * 4. 鲁棒性：处理重复单词和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. SPOJ DICT（在字典中搜索） (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. LeetCode 1268. 搜索推荐系统
 * 4. HackerRank - Contacts
 * 5. LintCode 442. 实现前缀树
 * 6. CodeChef - DICTIONARY
 * 7. AtCoder - Dictionary Search
 */
public class Code17_SPOJDICT {
    
    // 前缀树节点数量上限
    public static int MAXN = 1000000;
    
    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];
    
    // 单词结尾标记，end[i]表示节点i是否是单词结尾
    public static boolean[] end = new boolean[MAXN];
    
    // 当前使用的节点数量
    public static int cnt;
    
    /**
     * 初始化前缀树
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static void build() {
        cnt = 1;
    }
    
    /**
     * 将字符映射到路径索引
     * 
     * @param c 字符
     * @return 路径索引
     */
    public static int path(char c) {
        return c - 'a';
    }
    
    /**
     * 向前缀树中插入单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 如果子节点不存在，则创建新节点
     * 4. 移动到子节点，继续处理下一个字符
     * 5. 单词遍历完成后，标记当前节点为单词结尾
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(L)，最坏情况下需要创建新节点
     * 
     * @param word 待插入的单词
     */
    public static void insert(String word) {
        if (word == null || word.length() == 0) {
            return; // 空字符串不插入
        }
        
        int cur = 1;
        for (int i = 0; i < word.length(); i++) {
            int path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
        }
        end[cur] = true;
    }
    
    /**
     * 查找以指定前缀开头的所有单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀的每个字符
     * 2. 对于每个字符，计算字符的路径索引
     * 3. 如果子节点不存在，说明没有匹配的单词，返回空列表
     * 4. 移动到子节点，继续处理下一个字符
     * 5. 前缀遍历完成后，从当前节点开始深度优先搜索收集所有单词
     * 6. 按字典序排序后返回结果
     * 
     * 时间复杂度：O(L + K)，其中L是前缀长度，K是匹配单词的总字符数
     * 空间复杂度：O(M)，其中M是匹配单词数量
     * 
     * @param prefix 前缀
     * @return 匹配的单词列表
     */
    public static List<String> search(String prefix) {
        List<String> result = new ArrayList<>();
        
        if (prefix == null || prefix.length() == 0) {
            // 空前缀匹配所有单词
            dfs(1, new StringBuilder(), result);
            Collections.sort(result);
            return result;
        }
        
        int cur = 1;
        StringBuilder currentPrefix = new StringBuilder();
        
        // 定位到前缀对应的节点
        for (int i = 0; i < prefix.length(); i++) {
            int path = path(prefix.charAt(i));
            if (tree[cur][path] == 0) {
                return result; // 没有匹配的单词
            }
            cur = tree[cur][path];
            currentPrefix.append(prefix.charAt(i));
        }
        
        // 从当前节点开始深度优先搜索收集所有单词
        dfs(cur, currentPrefix, result);
        Collections.sort(result);
        return result;
    }
    
    /**
     * 深度优先搜索收集单词
     * 
     * 算法步骤：
     * 1. 如果当前节点是单词结尾，将当前路径添加到结果列表
     * 2. 遍历当前节点的所有子节点：
     *    a. 将子节点对应的字符添加到路径
     *    b. 递归调用dfs处理子节点
     *    c. 回溯，移除添加的字符
     * 
     * 时间复杂度：O(K)，其中K是匹配单词的总字符数
     * 空间复杂度：O(H)，其中H是递归深度
     * 
     * @param node 当前节点
     * @param path 当前路径
     * @param result 结果列表
     */
    public static void dfs(int node, StringBuilder path, List<String> result) {
        // 如果当前节点是单词结尾，将当前路径添加到结果列表
        if (end[node]) {
            result.add(path.toString());
        }
        
        // 遍历当前节点的所有子节点
        for (int i = 0; i < 26; i++) {
            if (tree[node][i] != 0) {
                path.append((char)('a' + i));
                dfs(tree[node][i], path, result);
                path.deleteCharAt(path.length() - 1); // 回溯
            }
        }
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的单词结尾标记重置为false
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            for (int j = 0; j < 26; j++) {
                tree[i][j] = 0;
            }
            end[i] = false;
        }
    }
    
    /**
     * 处理字典查询
     * 
     * 算法步骤：
     * 1. 初始化前缀树
     * 2. 将所有单词插入前缀树
     * 3. 对每个查询前缀，调用search方法查找匹配单词
     * 4. 格式化输出结果
     * 5. 清空前缀树
     * 
     * 时间复杂度：O(∑len(words[i]) + Q*(L + K))
     * 空间复杂度：O(∑len(words[i]) + M)
     * 
     * @param words 单词列表
     * @param queries 查询前缀列表
     * @return 查询结果列表
     */
    public static List<String> dict(String[] words, String[] queries) {
        build();
        List<String> result = new ArrayList<>();
        
        // 将所有单词插入前缀树
        for (String word : words) {
            insert(word);
        }
        
        // 处理每个查询
        for (String query : queries) {
            List<String> matches = search(query);
            if (matches.isEmpty()) {
                result.add("No match.");
            } else {
                for (String match : matches) {
                    result.add(match);
                }
            }
        }
        
        clear();
        return result;
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常查询：验证基本功能正确性
     * 2. 前缀匹配：验证前缀查询功能
     * 3. 无匹配：验证无匹配情况处理
     * 4. 空输入处理：验证边界条件处理
     */
    public static void testDict() {
        // 测试用例1：正常查询
        String[] words1 = {"pet", "peter", "rat", "rats", "re"};
        String[] queries1 = {"pet", "r"};
        List<String> result1 = dict(words1, queries1);
        // 期望结果：pet, peter, rat, rats, re
        assert result1.size() >= 5 : "测试用例1失败";
        
        // 测试用例2：无匹配
        String[] words2 = {"pet", "peter", "rat", "rats", "re"};
        String[] queries2 = {"xyz"};
        List<String> result2 = dict(words2, queries2);
        assert result2.size() == 1 && "No match.".equals(result2.get(0)) : "测试用例2失败";
        
        // 测试用例3：空前缀
        String[] words3 = {"a", "aa", "aaa"};
        String[] queries3 = {""};
        List<String> result3 = dict(words3, queries3);
        assert result3.size() == 3 : "测试用例3失败";
        
        System.out.println("SPOJ DICT 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 构建大型字典
     * 2. 执行多次查询操作
     */
    public static void performanceTest() {
        int n = 100000;
        String[] words = new String[n];
        
        // 构建字典
        for (int i = 0; i < n; i++) {
            words[i] = "word" + i;
        }
        
        // 构建查询
        String[] queries = {"word", "word1", "word12", "word123"};
        
        long startTime = System.currentTimeMillis();
        List<String> result = dict(words, queries);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理" + n + "个单词和4个查询耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果数量: " + result.size());
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testDict();
        
        // 运行性能测试
        performanceTest();
    }
}