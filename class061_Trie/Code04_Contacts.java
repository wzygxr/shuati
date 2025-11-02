package class045;

import java.util.Arrays;

/**
 * HackerRank Contacts
 * 
 * 题目描述：
 * 我们要制作自己的联系人应用程序！该应用程序必须执行两种类型的操作：
 * 1. add name：添加名为name的联系人
 * 2. find partial：查找以partial为前缀的联系人数量
 * 
 * 给定联系人数据库，对于每个查找操作，打印与partial匹配的联系人数量。
 * 
 * 测试链接：https://www.hackerrank.com/challenges/ctci-contacts/problem
 * 
 * 算法思路：
 * 1. 使用前缀树（Trie）存储所有联系人姓名
 * 2. 每个节点维护一个计数器，记录经过该节点的字符串数量
 * 3. 添加操作：遍历姓名字符，逐个插入前缀树，并更新路径上所有节点的计数器
 * 4. 查找操作：遍历前缀字符，在前缀树中查找，返回最终节点的计数器值
 * 
 * 核心优化：
 * 在前缀树的每个节点中维护一个计数器，记录经过该节点的字符串数量，
 * 使得查找以任意前缀开头的字符串数量可以在O(1)时间内完成。
 * 
 * 时间复杂度分析：
 * - 添加操作：O(len(name))，其中len(name)是姓名长度
 * - 查找操作：O(len(partial))，其中len(partial)是前缀长度
 * - 总体时间复杂度：O(∑len(operations))，其中∑len(operations)是所有操作字符串长度之和
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(names))，用于存储所有联系人姓名
 * - 总体空间复杂度：O(∑len(names))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效地处理前缀匹配问题，避免了暴力枚举O(n*m)
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或字符串包含非法字符的情况
 * 2. 边界情况：空字符串或极长字符串的情况
 * 3. 极端输入：大量添加或查找操作的情况
 * 4. 鲁棒性：处理大小写敏感和特殊字符
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
 * 4. HackerRank Contacts (本题)
 * 5. SPOJ DICT
 * 6. SPOJ PHONELST
 * 7. LintCode 442. 实现 Trie (前缀树)
 * 8. 牛客网 NC105. 二分查找-II
 * 9. 牛客网 NC138. 字符串匹配
 * 10. CodeChef - ANAGRAMS
 */
public class Code04_Contacts {

    /**
     * 使用前缀树实现联系人管理
     * 
     * 算法步骤详解：
     * 1. 初始化前缀树结构
     * 2. 遍历操作数组：
     *    a. 对于add操作：将姓名插入前缀树，更新路径上所有节点的计数器
     *    b. 对于find操作：在前缀树中查找前缀，返回匹配的联系人数量
     * 3. 调整结果数组大小，去除未使用的空间
     * 
     * 数据结构设计：
     * - tree[i][j]：前缀树结构，表示节点i的第j个子节点
     * - pass[i]：经过节点i的字符串数量
     * - cnt：当前使用的节点数量
     * 
     * 计数器优化原理：
     * 在插入字符串时，路径上每个节点的计数器都增加1，
     * 这样在查询前缀时，可以直接返回最终节点的计数器值，
     * 无需遍历子树统计数量，大大提高了查询效率。
     * 
     * 时间复杂度分析：
     * - 添加操作：O(len(name))，其中len(name)是姓名长度
     * - 查找操作：O(len(partial))，其中len(partial)是前缀长度
     * - 总体时间复杂度：O(∑len(operations))，其中∑len(operations)是所有操作字符串长度之和
     * 
     * 空间复杂度分析：
     * - 前缀树空间：O(∑len(names))，用于存储所有联系人姓名
     * - 总体空间复杂度：O(∑len(names))
     * 
     * 是否最优解：是
     * 理由：使用前缀树可以高效地处理前缀匹配问题，避免了暴力枚举O(n*m)
     * 
     * 工程化考虑：
     * 1. 异常处理：输入为空或字符串包含非法字符的情况
     * 2. 边界情况：空字符串或极长字符串的情况
     * 3. 极端输入：大量添加或查找操作的情况
     * 4. 鲁棒性：处理大小写敏感和特殊字符
     * 
     * 语言特性差异：
     * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
     * C++：可使用指针实现前缀树节点，更节省空间
     * Python：可使用字典实现前缀树，代码更简洁
     * 
     * @param operations 操作数组，每个元素为"add name"或"find partial"
     * @return 查找操作的结果数组
     */
    public static int[] contacts(String[][] operations) {
        build();
        int[] result = new int[operations.length];
        int index = 0;
        
        for (String[] operation : operations) {
            if ("add".equals(operation[0])) {
                insert(operation[1]);
            } else if ("find".equals(operation[0])) {
                result[index++] = count(operation[1]);
            }
        }
        
        // 调整结果数组大小
        return Arrays.copyOf(result, index);
    }

    // 前缀树节点数量上限
    public static int MAXN = 1000000;

    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];

    // 经过每个节点的字符串数量
    public static int[] pass = new int[MAXN];

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
     * 向前缀树中插入字符串
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历字符串
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，则创建新节点
     *    c. 移动到子节点
     *    d. 增加当前节点的通过计数
     * 3. 插入完成后，字符串已存储在前缀树中
     * 
     * 计数器更新：
     * 在遍历路径的过程中，每个节点的通过计数都增加1，
     * 这样可以快速查询以任意前缀开头的字符串数量。
     * 
     * 时间复杂度：O(len(word))，其中len(word)是字符串长度
     * 空间复杂度：O(len(word))，最坏情况下需要创建新节点
     * 
     * @param word 待插入的字符串
     */
    public static void insert(String word) {
        int cur = 1;
        pass[cur]++;
        for (int i = 0, path; i < word.length(); i++) {
            path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
            pass[cur]++;
        }
    }

    /**
     * 查询前缀树中以pre为前缀的字符串数量
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历前缀
     * 2. 对于每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回0（无匹配）
     *    c. 移动到子节点
     * 3. 遍历完成后，返回当前节点的计数器值
     * 
     * 查询优化：
     * 由于在插入时已维护了每个节点的通过计数，
     * 查询时可以直接返回最终节点的计数器值，
     * 无需遍历子树统计，时间复杂度为O(len(pre))。
     * 
     * 时间复杂度：O(len(pre))，其中len(pre)是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param pre 前缀字符串
     * @return 匹配的字符串数量
     */
    public static int count(String pre) {
        int cur = 1;
        for (int i = 0, path; i < pre.length(); i++) {
            path = path(pre.charAt(i));
            if (tree[cur][path] == 0) {
                return 0;
            }
            cur = tree[cur][path];
        }
        return pass[cur];
    }

    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点
     * 2. 将节点的子节点数组清零
     * 3. 将节点的通过计数重置为0
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
            pass[i] = 0;
        }
    }

}