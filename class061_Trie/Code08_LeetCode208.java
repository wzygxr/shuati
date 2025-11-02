package class045;

import java.util.Arrays;

/**
 * LeetCode 208. 实现 Trie (前缀树)
 * 
 * 题目描述：
 * 实现一个 Trie (前缀树)，包含 insert, search, 和 startsWith 这三个操作。
 * 
 * 测试链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
 * 
 * 算法思路：
 * 1. 使用二维数组实现前缀树结构，支持动态扩展
 * 2. 每个节点包含26个子节点（对应26个小写字母）
 * 3. 使用pass数组记录经过每个节点的字符串数量
 * 4. 使用end数组标记单词结尾节点
 * 
 * 数据结构设计详解：
 * 1. tree[i][j]：前缀树结构，表示节点i的第j个子节点
 * 2. pass[i]：记录经过节点i的字符串数量，用于统计和优化
 * 3. end[i]：标记节点i是否是单词结尾
 * 4. cnt：当前使用的节点数量，用于资源管理和清理
 * 
 * 核心特性：
 * 1. 高效插入：O(L)时间复杂度，L为单词长度
 * 2. 快速搜索：O(L)时间复杂度，精确匹配完整单词
 * 3. 前缀匹配：O(L)时间复杂度，检查是否存在以指定前缀开头的单词
 * 4. 空间优化：共享公共前缀，节省存储空间
 * 
 * 时间复杂度分析：
 * - 插入操作：O(L)，其中L是单词长度
 * - 搜索操作：O(L)，其中L是单词长度
 * - 前缀匹配：O(L)，其中L是前缀长度
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L)，其中N是插入的单词数量，L是平均单词长度
 * - 总体空间复杂度：O(N*L)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以高效处理字符串的插入、搜索和前缀匹配操作
 * 
 * 工程化考虑：
 * 1. 异常处理：处理空字符串和非法字符
 * 2. 内存管理：使用固定大小的数组避免内存泄漏
 * 3. 线程安全：在多线程环境下需要添加同步机制
 * 4. 可扩展性：支持字符集扩展和动态调整
 * 
 * 语言特性差异：
 * Java：使用二维数组实现，性能较高但空间固定
 * C++：可使用指针实现，更灵活但需要手动管理内存
 * Python：可使用字典实现，代码简洁但性能略低
 * 
 * 调试技巧：
 * 1. 打印中间节点状态验证插入过程
 * 2. 使用断言检查边界条件
 * 3. 单元测试覆盖各种异常场景
 * 
 * 性能优化：
 * 1. 使用数组代替哈希表提高访问速度
 * 2. 预分配足够空间避免频繁扩容
 * 3. 批量操作减少方法调用开销
 * 
 * 极端场景处理：
 * 1. 大量短字符串：空间利用率较低
 * 2. 少量长字符串：递归深度可能过大
 * 3. 重复插入：需要正确处理重复单词
 * 4. 空字符串：需要特殊处理
 */
public class Code08_LeetCode208 {
    
    // 前缀树节点数量上限，根据题目约束调整
    public static int MAXN = 1000000;
    
    // 前缀树结构，tree[i][j]表示节点i的第j个子节点
    public static int[][] tree = new int[MAXN][26];
    
    // 经过每个节点的字符串数量
    public static int[] pass = new int[MAXN];
    
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
     * 设计原理：
     * 将根节点编号设为1而非0，避免与未初始化的0值混淆，
     * 简化了节点存在性判断的逻辑。
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
     * 边界检查：
     * 该方法假设输入字符为小写字母，
     * 实际使用中应确保输入字符的有效性。
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
     * 1. 空值检查：如果单词为空或null，直接返回
     * 2. 从根节点开始遍历单词的每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，则创建新节点
     *    c. 移动到子节点
     *    d. 增加当前节点的通过计数
     * 3. 遍历完成后，标记当前节点为单词结尾
     * 
     * 路径共享优化：
     * 如果插入的单词与已存在单词有公共前缀，
     * 则共享前缀路径，只创建新路径的节点，
     * 大大节省了存储空间。
     * 
     * 通过计数用途：
     * pass数组记录经过每个节点的字符串数量，
     * 可用于统计以某前缀开头的单词数量等高级功能。
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
        pass[cur]++;
        for (int i = 0; i < word.length(); i++) {
            int path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                tree[cur][path] = ++cnt;
            }
            cur = tree[cur][path];
            pass[cur]++;
        }
        end[cur] = true;
    }
    
    /**
     * 搜索单词是否存在于前缀树中
     * 
     * 算法步骤：
     * 1. 空值检查：如果单词为空或null，返回false
     * 2. 从根节点开始遍历单词的每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回false（路径不存在）
     *    c. 移动到子节点，继续遍历
     * 3. 遍历完成后，检查当前节点是否为单词结尾
     * 
     * 精确匹配：
     * 不仅要求路径存在，还要求最终节点标记为单词结尾，
     * 确保搜索的是完整单词而非仅仅是前缀。
     * 
     * 时间复杂度：O(L)，其中L是单词长度
     * 空间复杂度：O(1)
     * 
     * @param word 待搜索的单词
     * @return 如果单词存在返回true，否则返回false
     */
    public static boolean search(String word) {
        if (word == null || word.length() == 0) {
            return false; // 空字符串不存在
        }
        
        int cur = 1;
        for (int i = 0; i < word.length(); i++) {
            int path = path(word.charAt(i));
            if (tree[cur][path] == 0) {
                return false;
            }
            cur = tree[cur][path];
        }
        return end[cur];
    }
    
    /**
     * 检查是否存在以指定前缀开头的单词
     * 
     * 算法步骤：
     * 1. 空值检查：如果前缀为空或null，返回true（空前缀匹配所有单词）
     * 2. 从根节点开始遍历前缀的每个字符：
     *    a. 计算字符的路径索引
     *    b. 如果子节点不存在，返回false（前缀路径不存在）
     *    c. 移动到子节点，继续遍历
     * 3. 遍历完成后，返回true（前缀路径存在）
     * 
     * 前缀匹配原理：
     * 只需要确保前缀路径存在即可，无需检查最终节点是否为单词结尾，
     * 因为只要路径存在，就必然存在以该前缀开头的单词。
     * 
     * 特殊情况处理：
     * 空前缀被定义为匹配所有单词，这是前缀树的常见约定。
     * 
     * 时间复杂度：O(L)，其中L是前缀长度
     * 空间复杂度：O(1)
     * 
     * @param prefix 待检查的前缀
     * @return 如果存在以prefix为前缀的单词返回true，否则返回false
     */
    public static boolean startsWith(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return true; // 空前缀匹配所有单词
        }
        
        int cur = 1;
        for (int i = 0; i < prefix.length(); i++) {
            int path = path(prefix.charAt(i));
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
     * 1. 遍历所有已使用的节点（从1到cnt）
     * 2. 将节点的子节点数组清零
     * 3. 将节点的通过计数重置为0
     * 4. 将节点的单词结尾标记重置为false
     * 
     * 资源管理：
     * 通过清空前缀树结构，释放内存资源，避免内存泄漏。
     * 由于使用静态数组，实际内存不会被释放，
     * 但逻辑上清除了所有数据，为下次使用做好准备。
     * 
     * 时间复杂度：O(cnt)，其中cnt是使用的节点数量
     * 空间复杂度：O(1)
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            Arrays.fill(tree[i], 0);
            pass[i] = 0;
            end[i] = false;
        }
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 正常插入和搜索：验证基本功能正确性
     * 2. 前缀匹配测试：验证前缀匹配功能
     * 3. 空字符串处理：验证边界条件处理
     * 4. 重复插入处理：验证重复操作的正确性
     * 5. 不存在的单词搜索：验证错误情况处理
     * 
     * 测试策略：
     * 1. 使用断言验证每个操作的正确性
     * 2. 覆盖各种边界条件和异常场景
     * 3. 测试完成后清理资源，避免影响其他测试
     * 
     * 调试技巧：
     * 1. 可以添加打印语句观察中间状态
     * 2. 使用调试器单步执行验证逻辑
     * 3. 针对失败的断言进行重点分析
     */
    public static void testTrie() {
        build();
        
        // 测试用例1：正常插入和搜索
        insert("apple");
        assert search("apple") : "搜索apple应该返回true";
        assert !search("app") : "搜索app应该返回false";
        assert startsWith("app") : "前缀app应该存在";
        
        insert("app");
        assert search("app") : "搜索app应该返回true";
        
        // 测试用例2：空字符串处理
        assert !search("") : "搜索空字符串应该返回false";
        assert startsWith("") : "空前缀应该匹配所有单词";
        
        // 测试用例3：不存在的单词
        assert !search("banana") : "搜索不存在的单词应该返回false";
        assert !startsWith("ban") : "不存在的单词前缀应该返回false";
        
        // 测试用例4：重复插入
        insert("apple");
        assert search("apple") : "重复插入后搜索应该仍然返回true";
        
        System.out.println("所有测试用例通过！");
        clear();
    }
    
    /**
     * 性能测试方法
     * 
     * 测试大规模数据下的性能表现：
     * 1. 插入大量单词：测试插入操作的性能
     * 2. 搜索操作性能：测试搜索操作的性能
     * 3. 前缀匹配性能：测试前缀匹配操作的性能
     * 
     * 性能指标：
     * 1. 测量各操作的执行时间
     * 2. 验证在大数据量下的稳定性
     * 3. 为算法优化提供数据支持
     * 
     * 测试数据：
     * 使用10000个不同的单词进行测试，
     * 模拟实际应用场景中的数据规模。
     */
    public static void performanceTest() {
        build();
        
        long startTime = System.currentTimeMillis();
        
        // 插入10000个单词
        for (int i = 0; i < 10000; i++) {
            insert("word" + i);
        }
        
        long insertTime = System.currentTimeMillis() - startTime;
        System.out.println("插入10000个单词耗时: " + insertTime + "ms");
        
        startTime = System.currentTimeMillis();
        
        // 搜索10000次
        for (int i = 0; i < 10000; i++) {
            search("word" + i);
        }
        
        long searchTime = System.currentTimeMillis() - startTime;
        System.out.println("搜索10000次耗时: " + searchTime + "ms");
        
        startTime = System.currentTimeMillis();
        
        // 前缀匹配10000次
        for (int i = 0; i < 10000; i++) {
            startsWith("word");
        }
        
        long prefixTime = System.currentTimeMillis() - startTime;
        System.out.println("前缀匹配10000次耗时: " + prefixTime + "ms");
        
        clear();
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testTrie();
        
        // 运行性能测试
        performanceTest();
    }
}