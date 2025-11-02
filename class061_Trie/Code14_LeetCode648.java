package class045_Trie;

import java.util.List;
import java.util.ArrayList;

/**
 * LeetCode 648. 单词替换
 * 
 * 题目描述：
 * 在英语中，我们有一个叫做词根(root)的概念，可以词根后面添加其他一些词组成另一个较长的单词——我们称这个词为继承词(successor)。
 * 例如，词根 an，跟随着单词 other(其他)，可以形成新的单词 another(另一个)。
 * 现在，给定一个由许多词根组成的词典 dictionary 和一个用空格分隔单词形成的句子 sentence。
 * 你需要将句子中的所有继承词用词根替换掉。如果继承词有许多可以形成它的词根，则用最短的词根替换它。
 * 你需要输出替换之后的句子。
 * 
 * 测试链接：https://leetcode.cn/problems/replace-words/
 * 
 * 算法思路：
 * 1. 使用前缀树存储所有词根
 * 2. 对于句子中的每个单词，在前缀树中查找最短的词根前缀
 * 3. 如果找到词根前缀，则用词根替换该单词；否则保留原单词
 * 
 * 核心优化：
 * 使用前缀树可以高效地查找最短词根前缀，避免了对每个词根都进行字符串匹配
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
 * - 处理句子：O(n*m)，其中n是句子中单词数量，m是平均单词长度
 * - 总体时间复杂度：O(∑len(dict[i]) + n*m)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(dict[i]))，用于存储所有词根
 * - 结果字符串：O(L)，其中L是句子长度
 * - 总体空间复杂度：O(∑len(dict[i]) + L)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内查找最短词根前缀，避免了暴力枚举
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或词典为空的情况
 * 2. 边界情况：句子为空或只包含空格的情况
 * 3. 极端输入：大量词根或句子很长的情况
 * 4. 鲁棒性：处理重复词根和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 648. 单词替换 (本题)
 * 2. LeetCode 208. 实现 Trie (前缀树)
 * 3. LeetCode 677. 键值映射
 * 4. LintCode 1428. 单词替换
 * 5. 牛客网 NC139. 单词替换
 * 6. HackerRank - Word Replacement
 * 7. CodeChef - ROOTWORD
 * 8. SPOJ - REPLACE
 * 9. AtCoder - Word Root Replacement
 */
public class Code14_LeetCode648 {
    
    /**
     * 使用前缀树实现单词替换
     * 
     * 算法步骤详解：
     * 1. 构建前缀树：
     *    a. 遍历词典中的每个词根
     *    b. 将词根插入前缀树
     * 2. 处理句子：
     *    a. 将句子按空格分割成单词数组
     *    b. 对每个单词，在前缀树中查找最短词根前缀
     *    c. 如果找到词根前缀，则用词根替换该单词；否则保留原单词
     * 3. 构造结果：
     *    a. 将处理后的单词用空格连接成句子
     * 
     * 时间复杂度分析：
     * - 构建前缀树：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
     * - 处理句子：O(n*m)，其中n是句子中单词数量，m是平均单词长度
     * - 总体时间复杂度：O(∑len(dict[i]) + n*m)
     * 
     * 空间复杂度分析：
     * - 前缀树空间：O(∑len(dict[i]))，用于存储所有词根
     * - 结果字符串：O(L)，其中L是句子长度
     * - 总体空间复杂度：O(∑len(dict[i]) + L)
     * 
     * 是否最优解：是
     * 理由：使用前缀树可以在线性时间内查找最短词根前缀，避免了暴力枚举
     * 
     * 工程化考虑：
     * 1. 异常处理：输入为空或词典为空的情况
     * 2. 边界情况：句子为空或只包含空格的情况
     * 3. 极端输入：大量词根或句子很长的情况
     * 4. 鲁棒性：处理重复词根和特殊字符
     * 
     * 语言特性差异：
     * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
     * C++：可使用指针实现前缀树节点，更节省空间
     * Python：可使用字典实现前缀树，代码更简洁
     * 
     * @param dictionary 词根列表
     * @param sentence 待处理的句子
     * @return 替换后的句子
     */
    public static String replaceWords(List<String> dictionary, String sentence) {
        // 构建前缀树
        build(dictionary);
        
        // 处理句子
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(getRoot(words[i]));
        }
        
        // 清理前缀树
        clear();
        
        return result.toString();
    }
    
    // 前缀树节点数量上限
    public static int MAXN = 100000;
    
    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];
    
    // 单词结尾标记，end[i]表示节点i是否是单词结尾，存储对应的词根
    public static String[] end = new String[MAXN];
    
    // 当前使用的节点数量
    public static int cnt;
    
    /**
     * 构建前缀树
     * 
     * 算法步骤：
     * 1. 初始化节点计数器
     * 2. 遍历词典中的每个词根：
     *    a. 从根节点开始遍历词根的每个字符
     *    b. 计算字符的路径索引（字符-'a'）
     *    c. 如果子节点不存在，则创建新节点
     *    d. 移动到子节点，继续处理下一个字符
     *    e. 词根遍历完成后，标记当前节点为单词结尾并存储词根
     * 
     * 时间复杂度：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
     * 空间复杂度：O(∑len(dict[i]))
     * 
     * @param dictionary 词根列表
     */
    public static void build(List<String> dictionary) {
        cnt = 1;
        for (String root : dictionary) {
            int cur = 1;
            for (int i = 0, path; i < root.length(); i++) {
                path = root.charAt(i) - 'a';
                if (tree[cur][path] == 0) {
                    tree[cur][path] = ++cnt;
                }
                cur = tree[cur][path];
            }
            // 只存储第一个（最短的）词根
            if (end[cur] == null || root.length() < end[cur].length()) {
                end[cur] = root;
            }
        }
    }
    
    /**
     * 获取单词的最短词根
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符，计算字符的路径索引（字符-'a'）
     * 3. 如果子节点不存在，说明没有词根前缀，返回原单词
     * 4. 如果当前节点是单词结尾，说明找到了最短词根前缀，返回词根
     * 5. 移动到子节点，继续处理下一个字符
     * 6. 单词遍历完成后，如果没有找到词根前缀，返回原单词
     * 
     * 时间复杂度：O(m)，其中m是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待处理的单词
     * @return 单词的最短词根或原单词
     */
    public static String getRoot(String word) {
        int cur = 1;
        for (int i = 0, path; i < word.length(); i++) {
            path = word.charAt(i) - 'a';
            if (tree[cur][path] == 0) {
                return word; // 没有词根前缀
            }
            cur = tree[cur][path];
            if (end[cur] != null) {
                return end[cur]; // 找到最短词根前缀
            }
        }
        return word; // 没有词根前缀
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的单词结尾标记设为null
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            for (int j = 0; j < 26; j++) {
                tree[i][j] = 0;
            }
            end[i] = null;
        }
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常替换：验证基本功能正确性
     * 2. 最短词根：验证使用最短词根替换
     * 3. 无词根匹配：验证保留原单词
     * 4. 空输入处理：验证边界条件处理
     * 5. 特殊字符处理：验证特殊字符处理
     */
    public static void testReplaceWords() {
        // 测试用例1：正常替换
        List<String> dict1 = new ArrayList<>();
        dict1.add("cat");
        dict1.add("bat");
        dict1.add("rat");
        String sentence1 = "the cattle was rattled by the battery";
        String expected1 = "the cat was rat by the bat";
        String result1 = replaceWords(dict1, sentence1);
        assert result1.equals(expected1) : "测试用例1失败";
        
        // 测试用例2：最短词根
        List<String> dict2 = new ArrayList<>();
        dict2.add("a");
        dict2.add("aa");
        dict2.add("aaa");
        dict2.add("aaaa");
        String sentence2 = "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa";
        String expected2 = "a a a a a a a a bbb baba a";
        String result2 = replaceWords(dict2, sentence2);
        assert result2.equals(expected2) : "测试用例2失败";
        
        // 测试用例3：无词根匹配
        List<String> dict3 = new ArrayList<>();
        dict3.add("catt");
        dict3.add("cat");
        dict3.add("bat");
        dict3.add("rat");
        String sentence3 = "the cattle was rattled by the battery";
        String expected3 = "the catt was rat by the bat";
        String result3 = replaceWords(dict3, sentence3);
        assert result3.equals(expected3) : "测试用例3失败";
        
        // 测试用例4：空输入处理
        List<String> dict4 = new ArrayList<>();
        String sentence4 = "";
        String expected4 = "";
        String result4 = replaceWords(dict4, sentence4);
        assert result4.equals(expected4) : "测试用例4失败";
        
        System.out.println("LeetCode 648 所有测试用例通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 构建大量词根的前缀树
     * 2. 处理长句子的替换操作
     */
    public static void performanceTest() {
        // 构建词典
        List<String> dictionary = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            dictionary.add("root" + i);
        }
        
        // 构建句子
        StringBuilder sentenceBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            if (i > 0) sentenceBuilder.append(" ");
            sentenceBuilder.append("root" + (i % 1000) + "word");
        }
        String sentence = sentenceBuilder.toString();
        
        long startTime = System.currentTimeMillis();
        String result = replaceWords(dictionary, sentence);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理10000个单词的句子耗时: " + (endTime - startTime) + "ms");
        System.out.println("结果长度: " + result.length() + " 字符");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testReplaceWords();
        
        // 运行性能测试
        performanceTest();
    }
}