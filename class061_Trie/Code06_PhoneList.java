package class045;

import java.util.Arrays;

/**
 * SPOJ PHONELST
 * 
 * 题目描述：
 * 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
 * 如果存在，输出NO；否则输出YES。
 * 
 * 测试链接：https://www.spoj.com/problems/PHONELST/
 * 
 * 算法思路：
 * 1. 构建前缀树，将所有电话号码插入前缀树
 * 2. 在插入过程中，检查是否存在以下情况：
 *    a) 当前号码是已插入号码的前缀
 *    b) 已插入号码是当前号码的前缀
 * 3. 如果存在上述情况，返回false；否则返回true
 * 
 * 核心优化：
 * 在插入电话号码的过程中实时检测前缀关系，
 * 一旦发现前缀冲突立即返回，避免不必要的计算。
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(∑len(numbers[i]))，其中∑len(numbers[i])是所有电话号码长度之和
 * - 查询过程：O(∑len(numbers[i]))
 * - 总体时间复杂度：O(∑len(numbers[i]))
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(∑len(numbers[i]))，用于存储所有电话号码
 * - 总体空间复杂度：O(∑len(numbers[i]))
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内检测前缀关系，避免了暴力枚举O(n^2)
 * 
 * 工程化考虑：
 * 1. 异常处理：输入为空或电话号码包含非法字符的情况
 * 2. 边界情况：空字符串或极长电话号码的情况
 * 3. 极端输入：大量电话号码的情况
 * 4. 鲁棒性：处理重复电话号码和特殊字符
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
 * 5. SPOJ DICT
 * 6. SPOJ PHONELST (本题)
 * 7. LintCode 442. 实现 Trie (前缀树)
 * 8. 牛客网 NC105. 二分查找-II
 * 9. 牛客网 NC138. 字符串匹配
 * 10. CodeChef - ANAGRAMS
 */
public class Code06_PhoneList {

    /**
     * 判断电话号码列表中是否存在一个号码是另一个号码的前缀
     * 
     * 算法步骤详解：
     * 1. 初始化前缀树结构
     * 2. 遍历电话号码数组：
     *    a. 将每个电话号码插入前缀树
     *    b. 在插入过程中检测前缀关系
     *    c. 如果发现前缀关系，立即返回false
     * 3. 如果所有号码都成功插入，返回true
     * 4. 清空前缀树资源
     * 
     * 提前终止优化：
     * 一旦发现任何两个号码之间存在前缀关系，
     * 立即返回false，无需检查剩余号码。
     * 
     * 时间复杂度分析：
     * - 构建前缀树：O(∑len(numbers[i]))，其中∑len(numbers[i])是所有电话号码长度之和
     * - 查询过程：O(∑len(numbers[i]))
     * - 总体时间复杂度：O(∑len(numbers[i]))
     * 
     * 空间复杂度分析：
     * - 前缀树空间：O(∑len(numbers[i]))，用于存储所有电话号码
     * - 总体空间复杂度：O(∑len(numbers[i]))
     * 
     * 是否最优解：是
     * 理由：使用前缀树可以在线性时间内检测前缀关系，避免了暴力枚举O(n^2)
     * 
     * 工程化考虑：
     * 1. 异常处理：输入为空或电话号码包含非法字符的情况
     * 2. 边界情况：空字符串或极长电话号码的情况
     * 3. 极端输入：大量电话号码的情况
     * 4. 鲁棒性：处理重复电话号码和特殊字符
     * 
     * 语言特性差异：
     * Java：使用二维数组实现前缀树，利用字符减法计算路径索引
     * C++：可使用指针实现前缀树节点，更节省空间
     * Python：可使用字典实现前缀树，代码更简洁
     * 
     * @param numbers 电话号码数组
     * @return 如果不存在前缀关系返回true，否则返回false
     */
    public static boolean phoneList(String[] numbers) {
        build();
        boolean result = true;
        
        for (String number : numbers) {
            if (!insert(number)) {
                result = false;
                break;
            }
        }
        
        clear();
        return result;
    }

    /**
     * 向前缀树中插入电话号码并检查前缀关系
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历电话号码的每个数字字符
     * 2. 对于每个数字字符：
     *    a. 计算数字字符的路径索引（字符-'0'）
     *    b. 如果子节点不存在，则创建新节点
     *    c. 移动到子节点
     *    d. 检查当前节点是否是单词结尾（前缀关系检测）
     * 3. 遍历完成后：
     *    a. 标记当前节点为单词结尾
     *    b. 检查当前节点是否有子节点（反向前缀关系检测）
     * 
     * 前缀关系检测：
     * 1. 正向检测：如果在遍历过程中遇到已标记为结尾的节点，
     *    说明当前号码是已插入号码的前缀
     * 2. 反向检测：如果遍历完成后当前节点有子节点，
     *    说明已插入号码是当前号码的前缀
     * 
     * 时间复杂度：O(len(number))，其中len(number)是电话号码长度
     * 空间复杂度：O(len(number))，最坏情况下需要创建新节点
     * 
     * @param number 电话号码
     * @return 如果不存在前缀关系返回true，否则返回false
     */
    public static boolean insert(String number) {
        int cur = 1;
        for (int i = 0, path; i < number.length(); i++) {
            path = number.charAt(i) - '0';
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
            
            // 如果当前节点是单词结尾，说明当前号码是已插入号码的前缀
            if (end[cur]) {
                return false;
            }
        }
        
        // 标记当前号码结尾
        end[cur] = true;
        
        // 检查是否有子节点，如果有说明已插入号码是当前号码的前缀
        for (int i = 0; i < 10; i++) {
            if (tree[cur][i] != 0) {
                return false;
            }
        }
        
        return true;
    }

    // 前缀树节点数量上限
    public static int MAXN = 1000000;

    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][10];

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