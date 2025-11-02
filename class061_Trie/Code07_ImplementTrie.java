package class045;

import java.util.Arrays;

/**
 * LintCode 442. Implement Trie (Prefix Tree)
 * 
 * 题目描述：
 * 实现一个Trie（前缀树），包含insert, search, 和startsWith这三个操作。
 * 
 * 测试链接：https://www.lintcode.com/problem/442/
 * 
 * 算法思路：
 * 1. 设计前缀树数据结构，包含子节点数组和单词结尾标记
 * 2. 实现insert方法：逐个字符遍历单词，创建节点并建立连接
 * 3. 实现search方法：逐个字符遍历单词，查找节点并检查是否为单词结尾
 * 4. 实现startsWith方法：逐个字符遍历前缀，查找节点
 * 
 * 核心特性：
 * 1. 高效插入：O(len(word))时间复杂度
 * 2. 快速搜索：O(len(word))时间复杂度
 * 3. 前缀匹配：O(len(prefix))时间复杂度
 * 4. 空间优化：共享公共前缀，节省存储空间
 * 
 * 时间复杂度分析：
 * - insert操作：O(len(word))，其中len(word)是单词长度
 * - search操作：O(len(word))，其中len(word)是单词长度
 * - startsWith操作：O(len(prefix))，其中len(prefix)是前缀长度
 * 
 * 空间复杂度分析：
 * - Trie空间：O(∑len(words))，用于存储所有插入的单词
 * - 总体空间复杂度：O(∑len(words))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地处理字符串的插入、搜索和前缀匹配操作
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或字符串包含非法字符的情况
 * 2. 边界情况：空字符串的情况
 * 3. 极端输入：大量操作或极长字符串的情况
 * 4. 鲁棒性：处理大小写敏感和特殊字符
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
 * C++：可使用指针实现前缀树节点，更节省空间
 * Python：可使用字典实现前缀树，代码更简洁
 * 
 * 相关题目扩展：
 * 1. LeetCode 208. 实现 Trie (前缀树) (本题与LeetCode 208相同)
 * 2. LeetCode 212. 单词搜索 II
 * 3. LeetCode 421. 数组中两个数的最大异或值
 * 4. HackerRank Contacts
 * 5. SPOJ DICT
 * 6. SPOJ PHONELST
 * 7. LintCode 442. 实现 Trie (前缀树)
 * 8. 牛客网 NC105. 二分查找-II
 * 9. 牛客网 NC138. 字符串匹配
 * 10. CodeChef - ANAGRAMS
 */
public class Code07_ImplementTrie {

    /**
     * 实现Trie（前缀树）
     * 
     * 数据结构设计：
     * 1. 前缀树结构：tree[i][j]表示节点i的第j个子节点
     * 2. 单词结尾标记：end[i]表示节点i是否是单词结尾
     * 3. 节点计数器：cnt记录当前使用的节点数量
     * 
     * 核心操作实现：
     * 1. insert方法：逐个字符遍历单词，创建节点并建立连接，标记单词结尾
     * 2. search方法：逐个字符遍历单词，查找节点并检查是否为单词结尾
     * 3. startsWith方法：逐个字符遍历前缀，查找节点
     * 
     * 字符映射：
     * 利用字符减法将小写字母'a'-'z'映射到数组索引0-25
     * 
     * 时间复杂度分析：
     * - insert操作：O(len(word))，其中len(word)是单词长度
     * - search操作：O(len(word))，其中len(word)是单词长度
     * - startsWith操作：O(len(prefix))，其中len(prefix)是前缀长度
     * 
     * 空间复杂度分析：
     * - Trie空间：O(∑len(words))，用于存储所有插入的单词
     * - 总体空间复杂度：O(∑len(words))
     * 
     * 是否最优解：是
     * 理由：使用前缀树可以高效地处理字符串的插入、搜索和前缀匹配操作
     * 
     * 工程化考虑：
     * 1. 异常处理：输入为空或字符串包含非法字符的情况
     * 2. 边界情况：空字符串的情况
     * 3. 极端输入：大量操作或极长字符串的情况
     * 4. 鲁棒性：处理大小写敏感和特殊字符
     * 
     * 语言特性差异：
     * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
     * C++：可使用指针实现前缀树节点，更节省空间
     * Python：可使用字典实现前缀树，代码更简洁
     */
    
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
     * 算法步骤：
     * 1. 重置节点计数器为1（根节点编号为1）
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
     * 映射规则：
     * 'a' 映射到 0
     * 'b' 映射到 1
     * ...
     * 'z' 映射到 25
     * 
     * 实现原理：
     * 利用字符的ASCII码值，通过减去'a'的ASCII码值，
     * 将小写字母映射到0-25的整数范围。
     * 
     * @param cha 字符
     * @return 路径索引
     */
    public static int path(char cha) {
        return cha - 'a';
    }

    /**
     * 向前缀树中插入单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，则创建新节点
     *    c. 移动到子节点
     * 3. 遍历完成后，标记当前节点为单词结尾
     * 
     * 路径共享：
     * 如果插入的单词与已存在单词有公共前缀，
     * 则共享前缀路径，只创建新路径的节点。
     * 
     * 时间复杂度：O(len(word))，其中len(word)是单词长度
     * 空间复杂度：O(len(word))，最坏情况下需要创建新节点
     * 
     * @param word 待插入的单词
     */
    public static void insert(String word) {
        int cur = 1;
        for (int i = 0, path; i < word.length(); i++) {
            path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
        }
        end[cur] = true;
    }

    /**
     * 在前缀树中搜索单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历单词的每个字符
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回false（单词不存在）
     *    c. 移动到子节点
     * 3. 遍历完成后，检查当前节点是否为单词结尾
     * 
     * 精确匹配：
     * 不仅要求路径存在，还要求最终节点标记为单词结尾，
     * 确保搜索的是完整单词而非仅仅是前缀。
     * 
     * 时间复杂度：O(len(word))，其中len(word)是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待搜索的单词
     * @return 如果单词存在返回true，否则返回false
     */
    public static boolean search(String word) {
        int cur = 1;
        for (int i = 0, path; i < word.length(); i++) {
            path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                return false;
            }
            cur = tree[cur][path];
        }
        return end[cur];
    }

    /**
     * 检查前缀树中是否存在以prefix为前缀的单词
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀的每个字符
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回false（前缀不存在）
     *    c. 移动到子节点
     * 3. 遍历完成后，返回true（前缀存在）
     * 
     * 前缀匹配：
     * 只需要确保前缀路径存在即可，无需检查最终节点是否为单词结尾，
     * 因为只要路径存在，就必然存在以该前缀开头的单词。
     * 
     * 时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 待检查的前缀
     * @return 如果存在以prefix为前缀的单词返回true，否则返回false
     */
    public static boolean startsWith(String prefix) {
        int cur = 1;
        for (int i = 0, path; i < prefix.length(); i++) {
            path = path(prefix.charAt(i));
            if (tree[cur][path] == 0) {
                return false;
            }
            cur = tree[cur][path];
        }
        return true;
    }

    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的单词结尾标记重置为false
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
            end[i] = false;
        }
    }

}