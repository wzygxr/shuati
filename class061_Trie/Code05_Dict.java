package class045;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SPOJ DICT
 * 
 * 题目描述：
 * 给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词，并按字典序输出。
 * 
 * 测试链接：https://www.spoj.com/problems/DICT/
 * 
 * 算法思路：
 * 1. 构建前缀树，将字典中的所有单词插入前缀树
 * 2. 查找前缀在前缀树中的位置
 * 3. 从该位置开始，深度优先搜索所有可能的单词
 * 4. 按字典序收集所有匹配的单词
 * 
 * 核心优化：
 * 使用前缀树存储字典单词，可以快速定位前缀位置，
 * 然后通过深度优先搜索收集所有匹配单词，
 * 由于前缀树的字典序特性，搜索结果天然有序。
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
 * - 查找过程：O(len(prefix) + ∑len(results))，其中∑len(results)是所有结果单词长度之和
 * - 总体时间复杂度：O(∑len(words[i]) + len(prefix) + ∑len(results))
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
 * - 递归栈空间：O(max(len(words[i])))，其中max(len(words[i]))是最长单词的长度
 * - 结果空间：O(∑len(results))，用于存储结果单词
 * - 总体空间复杂度：O(∑len(words[i]) + max(len(words[i])) + ∑len(results))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或字典为空的情况
 * 2. 边界情况：前缀为空或不存在匹配单词的情况
 * 3. 极端输入：大量单词或极长单词的情况
 * 4. 鲁棒性：处理重复单词和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 208. 实现 Trie (前缀树)
 * 2. LeetCode 212. 单词搜索 II
 * 3. LeetCode 421. 数组中两个数的最大异或值
 * 4. HackerRank Contacts
 * 5. SPOJ DICT (本题)
 * 6. SPOJ PHONELST
 * 7. LintCode 442. 实现 Trie (前缀树)
 * 8. 牛客网 NC105. 二分查找-II
 * 9. 牛客网 NC138. 字符串匹配
 * 10. CodeChef - ANAGRAMS
 */
public class Code05_Dict {

    /**
     * 在字典中查找具有特定前缀的单词
     * 
     * 算法步骤详解：
     * 1. 构建前缀树：
     *    a. 初始化前缀树结构
     *    b. 将字典中的所有单词插入前缀树
     * 2. 查找前缀：
     *    a. 在前缀树中查找前缀位置
     *    b. 如果前缀不存在，返回空列表
     * 3. 深度优先搜索：
     *    a. 从前缀节点开始，深度优先搜索所有子树
     *    b. 收集所有以该前缀开头的单词
     * 4. 资源清理：
     *    a. 搜索完成后清空前缀树
     * 
     * 字典序保证：
     * 由于前缀树按字母顺序存储子节点（a-z），
     * 深度优先搜索的遍历顺序天然保证了结果的字典序。
     * 
     * 时间复杂度分析：
     * - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
     * - 查找过程：O(len(prefix) + ∑len(results))，其中∑len(results)是所有结果单词长度之和
     * - 总体时间复杂度：O(∑len(words[i]) + len(prefix) + ∑len(results))
     * 
     * 空间复杂度分析：
     * - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
     * - 递归栈空间：O(max(len(words[i])))，其中max(len(words[i]))是最长单词的长度
     * - 结果空间：O(∑len(results))，用于存储结果单词
     * - 总体空间复杂度：O(∑len(words[i]) + max(len(words[i])) + ∑len(results))
     * 
     * 是否最优解：是
     * 理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
     * 
     * 工程化考虑：
     * 1. 异常处理：输入为空或字典为空的情况
     * 2. 边界情况：前缀为空或不存在匹配单词的情况
     * 3. 极端输入：大量单词或极长单词的情况
     * 4. 鲁棒性：处理重复单词和特殊字符
     * 
     * 语言特性差异：
     * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
     * C++：可使用指针实现前缀树节点，更节省空间
     * Python：可使用字典实现前缀树，代码更简洁
     * 
     * @param words 字典单词数组
     * @param prefix 查找前缀
     * @return 匹配的单词列表
     */
    public static List<String> dict(String[] words, String prefix) {
        build(words);
        List<String> result = new ArrayList<>();
        int prefixNode = findPrefix(prefix);
        if (prefixNode != 0) {
            dfs(prefixNode, new StringBuilder(prefix), result);
        }
        clear();
        return result;
    }

    /**
     * 深度优先搜索查找所有匹配单词
     * 
     * 算法步骤：
     * 1. 单词结尾检查：
     *    a. 如果当前节点是单词结尾，将当前路径加入结果列表
     * 2. 子节点遍历：
     *    a. 按字母顺序（a-z）遍历所有子节点
     *    b. 对于存在的子节点：
     *       i. 将对应字符添加到路径末尾
     *       ii. 递归搜索子节点的子树
     *       iii. 回溯时删除添加的字符
     * 
     * 字典序保证：
     * 由于按a-z顺序遍历子节点，搜索结果天然保持字典序
     * 
     * 回溯机制：
     * 通过StringBuilder的append和deleteCharAt操作实现路径的回溯，
     * 避免了创建大量字符串对象，提高了效率。
     * 
     * 时间复杂度：O(∑len(results))，其中∑len(results)是所有结果单词长度之和
     * 空间复杂度：O(max(len(results)))，递归栈空间
     * 
     * @param node 当前节点编号
     * @param path 当前路径字符串
     * @param result 结果列表
     */
    public static void dfs(int node, StringBuilder path, List<String> result) {
        // 如果当前节点是单词结尾，将路径加入结果
        if (end[node] != null) {
            result.add(path.toString());
        }
        
        // 遍历所有子节点
        for (int i = 0; i < 26; i++) {
            if (tree[node][i] != 0) {
                path.append((char) ('a' + i));
                dfs(tree[node][i], path, result);
                path.deleteCharAt(path.length() - 1); // 回溯
            }
        }
    }

    /**
     * 查找前缀在前缀树中的位置
     * 
     * 算法步骤：
     * 1. 从前缀的第一个字符开始查找
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回0（前缀不存在）
     *    c. 移动到子节点，继续查找
     * 3. 查找完成后，返回最终节点编号
     * 
     * 提前终止优化：
     * 一旦发现某个字符对应的子节点不存在，
     * 立即返回0，无需继续查找剩余字符。
     * 
     * 时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 前缀字符串
     * @return 前缀对应的节点编号，如果不存在则返回0
     */
    public static int findPrefix(String prefix) {
        int cur = 1;
        for (int i = 0, path; i < prefix.length(); i++) {
            path = prefix.charAt(i) - 'a';
            if (tree[cur][path] == 0) {
                return 0;
            }
            cur = tree[cur][path];
        }
        return cur;
    }

    // 前缀树节点数量上限
    public static int MAXN = 1000000;

    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];

    // 单词结尾标记，end[i]存储以节点i结尾的单词
    public static String[] end = new String[MAXN];

    // 当前使用的节点数量
    public static int cnt;

    /**
     * 构建前缀树
     * 
     * 算法步骤：
     * 1. 初始化前缀树节点计数器
     * 2. 遍历单词数组中的每个单词：
     *    a. 从根节点开始遍历单词的每个字符
     *    b. 计算字符的路径索引
     *    c. 如果子节点不存在，则创建新节点
     *    d. 移动到子节点
     *    e. 单词遍历完成后，标记单词结尾
     * 
     * 节点属性说明：
     * - tree[i][j]：节点i的第j个子节点
     * - end[i]：以节点i结尾的单词
     * 
     * 时间复杂度：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
     * 空间复杂度：O(∑len(words[i]))
     * 
     * @param words 单词数组
     */
    public static void build(String[] words) {
        cnt = 1;
        for (String word : words) {
            int cur = 1;
            for (int i = 0, path; i < word.length(); i++) {
                path = word.charAt(i) - 'a';
                if (tree[cur][path] == 0) {
                    tree[cur][path] = ++cnt;
                }
                cur = tree[cur][path];
            }
            end[cur] = word;
        }
    }

    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的单词结尾标记设为null
     * 
     * 资源管理：
     * 通过清空前缀树结构，释放内存资源，避免内存泄漏
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            Arrays.fill(tree[i], 0);
            end[i] = null;
        }
    }

}