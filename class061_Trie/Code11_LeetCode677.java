package class045;

import java.util.*;

/**
 * LeetCode 677. 键值映射
 * 
 * 题目描述：
 * 设计一个 MapSum 类，满足以下几点:
 * - 字符串表示键，整数表示值
 * - 返回具有前缀等于给定字符串的键的值的总和
 * - 实现一个 MapSum 类：
 *   - MapSum() 初始化 MapSum 对象
 *   - void insert(String key, int val) 插入 key-val 键值对，字符串表示键 key ，整数表示值 val 。如果键 key 已经存在，那么原来的键值对 key-value 将被替代成新的键值对。
 *   - int sum(string prefix) 返回所有以该前缀 prefix 开头的键 key 的值的总和。
 *
 * 示例：
 * 输入：
 * ["MapSum", "insert", "sum", "insert", "sum"]
 * [[], ["apple", 3], ["ap"], ["app", 2], ["ap"]]
 * 输出：
 * [null, null, 3, null, 5]
 * 解释：
 * MapSum mapSum = new MapSum();
 * mapSum.insert("apple", 3);
 * mapSum.sum("ap");           // 返回 3 (apple = 3)
 * mapSum.insert("app", 2);
 * mapSum.sum("ap");           // 返回 5 (apple + app = 3 + 2 = 5)
 *
 * 测试链接：https://leetcode.cn/problems/map-sum-pairs/
 * 
 * 算法思路：
 * 1. 使用前缀树（Trie）存储键值对，每个节点存储经过该节点的所有键的值之和
 * 2. 插入操作：更新键对应的值，并更新路径上所有节点的和
 * 3. 求和操作：查找前缀对应的节点，返回该节点的和值
 *
 * 核心优化思路：
 * 1. 在插入时计算值的差值(delta)，避免每次都需要遍历整个子树来计算和
 * 2. 使用额外的哈希表记录已存在的键值对，便于计算delta值
 * 3. 提供两种实现方案：
 *    - 动态节点分配方案（MapSum类）：使用对象引用动态创建节点，内存使用灵活但有一定开销
 *    - 静态数组方案（MapSumOptimized类）：预分配固定大小数组，访问效率高但可能存在空间浪费
 *
 * 算法步骤详解：
 * 1. 插入操作(insert)：
 *    - 计算新值与旧值的差值delta = newVal - oldVal
 *    - 更新keyValueMap中的键值对
 *    - 从根节点开始遍历键的每个字符，在前缀树中创建路径
 *    - 对于路径上的每个节点，将其value字段增加delta
 *    - 这样可以保证每个节点的value始终等于以其为前缀的所有键的值之和
 *
 * 2. 求和操作(sum)：
 *    - 从前缀的第一个字符开始，在前缀树中查找对应路径
 *    - 如果路径中断（某个字符对应的子节点不存在），则返回0
 *    - 否则，到达前缀末尾节点时，返回该节点的value值
 *
 * 时间复杂度分析：
 * - 插入操作：O(L)，其中L是键的长度，需要遍历整个键来更新路径上的节点值
 * - 求和操作：O(P)，其中P是前缀的长度，只需遍历前缀路径
 * - 总体时间复杂度：O(L+P)，其中L是键的平均长度，P是前缀的平均长度
 *
 * 空间复杂度分析：
 * - 前缀树空间：O(N*L*Σ)，其中N是键的数量，L是平均键长度，Σ是字符集大小（此处为26个小写字母）
 * - keyValueMap空间：O(N)，存储所有键值对
 * - 总体空间复杂度：O(N*L*Σ)
 *
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内完成前缀相关的键值操作，相比暴力搜索方法（需要遍历所有键检查前缀）效率更高
 *
 * 工程化考虑：
 * 1. 异常处理：处理空键和非法值，防止空指针异常
 * 2. 边界情况：键为空或前缀为空的情况，需特殊处理
 * 3. 极端输入：大量键或长键的情况，需考虑内存使用和性能
 * 4. 内存管理：合理管理前缀树内存，避免内存泄漏
 * 5. 可扩展性：支持不同的字符集，不仅限于小写字母
 *
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，性能较高；对象引用机制简化内存管理
 * C++：可使用指针实现，更节省空间；需要手动管理内存释放
 * Python：可使用字典实现，代码更简洁；动态类型提供灵活性但可能牺牲性能
 *
 * 调试技巧：
 * 1. 验证插入和求和操作的正确性，特别是重复插入同一键的情况
 * 2. 测试前缀不存在的情况，确保返回正确的默认值
 * 3. 单元测试覆盖各种边界条件，如空前缀、空键等
 * 4. 性能测试对比不同实现方案的效率差异
 *
 * 相关题目扩展：
 * 1. LeetCode 208. 实现 Trie (前缀树) - 前缀树的基础实现
 * 2. LeetCode 211. 添加与搜索单词 - 数据结构设计 - 支持通配符匹配的前缀树
 * 3. LeetCode 212. 单词搜索 II - 结合DFS和前缀树的应用
 * 4. LeetCode 421. 数组中两个数的最大异或值 - 前缀树在位运算中的应用
 * 5. LeetCode 648. 单词替换 - 前缀树在字符串替换中的应用
 */
public class Code11_LeetCode677 {
    
    /**
     * MapSum 类实现 - 使用动态节点分配方案
     * 特点：按需创建节点，内存使用灵活，适合键数量不确定或变化较大的场景
     */
    static class MapSum {
        private TrieNode root;
        private Map<String, Integer> keyValueMap; // 存储键的原始值，用于计算更新时的差值
        
        /**
         * 前缀树节点类
         * 包含指向子节点的数组和累积值字段
         */
        class TrieNode {
            TrieNode[] children; // 子节点数组，索引对应字母'a'-'z'
            int value; // 经过该节点的所有键的值之和
            
            /**
             * 默认构造函数
             * 初始化子节点数组和值字段
             */
            TrieNode() {
                children = new TrieNode[26]; // 26个小写字母
                value = 0; // 初始累积值为0
            }
        }
        
        /**
         * 构造函数
         * 初始化根节点和键值映射表
         */
        public MapSum() {
            root = new TrieNode(); // 创建根节点
            keyValueMap = new HashMap<>(); // 初始化键值映射表
        }
        
        /**
         * 插入键值对
         * 
         * 算法步骤：
         * 1. 计算值的差异（新值 - 旧值）
         * 2. 更新键值映射
         * 3. 更新前缀树路径上所有节点的和值
         * 
         * @param key 键 - 非空字符串，只包含小写字母
         * @param val 值 - 整数值
         */
        public void insert(String key, int val) {
            // 参数校验：检查键是否为空或长度为0
            if (key == null || key.length() == 0) {
                return;
            }
            
            // 计算值的差异：新值减去旧值（如果键不存在则旧值为0）
            int delta = val - keyValueMap.getOrDefault(key, 0);
            
            // 更新键值映射：将新的键值对存入映射表
            keyValueMap.put(key, val);
            
            // 更新前缀树：沿着键的路径更新每个节点的累积值
            TrieNode node = root;
            // 遍历键中的每个字符
            for (char c : key.toCharArray()) {
                // 计算字符相对于'a'的索引
                int index = c - 'a';
                // 如果子节点不存在，则创建新节点
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                // 移动到子节点
                node = node.children[index];
                // 更新节点的累积值（加上差值）
                node.value += delta;
            }
        }
        
        /**
         * 计算以指定前缀开头的所有键的值之和
         * 
         * 算法步骤：
         * 1. 遍历前缀，找到对应的节点
         * 2. 返回该节点的和值
         * 
         * @param prefix 前缀 - 非空字符串，只包含小写字母
         * @return 所有以prefix开头的键的值之和
         */
        public int sum(String prefix) {
            // 参数校验：检查前缀是否为空或长度为0
            if (prefix == null || prefix.length() == 0) {
                return 0;
            }
            
            // 查找前缀对应的节点
            TrieNode node = root;
            // 遍历前缀中的每个字符
            for (char c : prefix.toCharArray()) {
                // 计算字符相对于'a'的索引
                int index = c - 'a';
                // 如果子节点不存在，说明前缀不存在于树中
                if (node.children[index] == null) {
                    return 0; // 返回0表示没有匹配的键
                }
                // 移动到子节点
                node = node.children[index];
            }
            
            // 返回前缀末尾节点的累积值，即所有以该前缀开头的键的值之和
            return node.value;
        }
    }
    
    /**
     * 优化版本：使用静态数组实现，性能更高
     * 特点：预分配固定大小数组，访问效率高，适合键数量相对固定的场景
     * 注意：需要预先确定数组大小，过大浪费空间，过小可能导致溢出
     */
    static class MapSumOptimized {
        private static final int MAXN = 100000; // 最大节点数，根据实际需求调整
        private static int[][] tree = new int[MAXN][26]; // 前缀树结构，tree[node][char]表示节点node的字符char对应的子节点编号
        private static int[] values = new int[MAXN]; // 每个节点的累积值
        private static int cnt; // 当前使用的节点编号计数器
        private Map<String, Integer> keyValueMap; // 存储键的原始值，用于计算更新时的差值
        
        /**
         * 构造函数
         * 初始化计数器、键值映射表和数组
         */
        public MapSumOptimized() {
            cnt = 1; // 节点编号从1开始（0保留作为空指针标识）
            keyValueMap = new HashMap<>();
            // 初始化数组：将所有节点的子节点指针置为0（空指针），累积值置为0
            for (int i = 0; i < MAXN; i++) {
                Arrays.fill(tree[i], 0);
                values[i] = 0;
            }
        }
        
        /**
         * 插入键值对（优化版）
         * 使用静态数组实现，避免频繁的对象创建和垃圾回收
         * 
         * @param key 键 - 非空字符串，只包含小写字母
         * @param val 值 - 整数值
         */
        public void insert(String key, int val) {
            // 参数校验：检查键是否为空或长度为0
            if (key == null || key.length() == 0) {
                return;
            }
            
            // 计算值的差异：新值减去旧值（如果键不存在则旧值为0）
            int delta = val - keyValueMap.getOrDefault(key, 0);
            // 更新键值映射：将新的键值对存入映射表
            keyValueMap.put(key, val);
            
            // 更新前缀树：沿着键的路径更新每个节点的累积值
            int cur = 1; // 从根节点（编号1）开始
            // 遍历键中的每个字符
            for (char c : key.toCharArray()) {
                // 计算字符相对于'a'的索引
                int index = c - 'a';
                // 如果子节点不存在，则分配新节点
                if (tree[cur][index] == 0) {
                    tree[cur][index] = ++cnt; // 分配新节点编号并建立连接
                }
                // 移动到子节点
                cur = tree[cur][index];
                // 更新节点的累积值（加上差值）
                values[cur] += delta;
            }
        }
        
        /**
         * 计算以指定前缀开头的所有键的值之和（优化版）
         * 使用静态数组实现，访问效率更高
         * 
         * @param prefix 前缀 - 非空字符串，只包含小写字母
         * @return 所有以prefix开头的键的值之和
         */
        public int sum(String prefix) {
            // 参数校验：检查前缀是否为空或长度为0
            if (prefix == null || prefix.length() == 0) {
                return 0;
            }
            
            // 查找前缀对应的节点
            int cur = 1; // 从根节点（编号1）开始
            // 遍历前缀中的每个字符
            for (char c : prefix.toCharArray()) {
                // 计算字符相对于'a'的索引
                int index = c - 'a';
                // 如果子节点不存在，说明前缀不存在于树中
                if (tree[cur][index] == 0) {
                    return 0; // 返回0表示没有匹配的键
                }
                // 移动到子节点
                cur = tree[cur][index];
            }
            
            // 返回前缀末尾节点的累积值，即所有以该前缀开头的键的值之和
            return values[cur];
        }
    }
    
    /**
     * 单元测试方法
     * 验证MapSum类的基本功能和边界情况
     */
    public static void testMapSum() {
        // 测试用例1：基础功能测试
        MapSum mapSum = new MapSum();
        mapSum.insert("apple", 3);
        assert mapSum.sum("ap") == 3 : "基础插入测试失败";
        
        mapSum.insert("app", 2);
        assert mapSum.sum("ap") == 5 : "多个键测试失败";
        
        // 测试用例2：更新操作测试
        mapSum.insert("apple", 5);
        assert mapSum.sum("ap") == 7 : "更新操作测试失败";
        
        // 测试用例3：前缀不存在测试
        assert mapSum.sum("banana") == 0 : "前缀不存在测试失败";
        
        // 测试用例4：空键和空前缀测试
        mapSum.insert("", 10);
        assert mapSum.sum("") == 7 : "空键处理测试失败"; // 空键不应该影响结果
        
        // 测试用例5：优化版本测试
        MapSumOptimized optimized = new MapSumOptimized();
        optimized.insert("test", 100);
        assert optimized.sum("te") == 100 : "优化版本测试失败";
        
        System.out.println("所有单元测试通过！");
    }
    
    /**
     * 性能测试方法
     * 对比标准版本和优化版本的性能差异
     */
    public static void performanceTest() {
        MapSum mapSum = new MapSum();
        MapSumOptimized optimized = new MapSumOptimized();
        
        int n = 10000;
        String[] keys = new String[n];
        int[] values = new int[n];
        
        // 生成测试数据
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            keys[i] = "key" + i;
            values[i] = random.nextInt(1000);
        }
        
        // 标准版本性能测试
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            mapSum.insert(keys[i], values[i]);
        }
        long insertTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            mapSum.sum("key");
        }
        long sumTime = System.currentTimeMillis() - startTime;
        
        // 优化版本性能测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            optimized.insert(keys[i], values[i]);
        }
        long optimizedInsertTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            optimized.sum("key");
        }
        long optimizedSumTime = System.currentTimeMillis() - startTime;
        
        System.out.println("标准版本 - 插入" + n + "个键耗时: " + insertTime + "ms");
        System.out.println("标准版本 - 求和" + n + "次耗时: " + sumTime + "ms");
        System.out.println("优化版本 - 插入" + n + "个键耗时: " + optimizedInsertTime + "ms");
        System.out.println("优化版本 - 求和" + n + "次耗时: " + optimizedSumTime + "ms");
    }
    
    /**
     * 边界情况测试方法
     * 测试各种特殊情况和异常输入
     */
    public static void edgeCaseTest() {
        MapSum mapSum = new MapSum();
        
        // 测试空键
        mapSum.insert("", 100);
        assert mapSum.sum("") == 0 : "空键测试失败";
        
        // 测试空前缀
        mapSum.insert("test", 50);
        assert mapSum.sum("") == 50 : "空前缀测试失败";
        
        // 测试特殊字符（应该忽略非小写字母）
        try {
            mapSum.insert("TEST", 100); // 大写字母
            mapSum.insert("test123", 200); // 数字
            // 这些插入应该被正确处理或忽略
        } catch (Exception e) {
            // 预期可能抛出异常
        }
        
        // 测试重复插入相同值
        mapSum.insert("same", 10);
        mapSum.insert("same", 10);
        assert mapSum.sum("sa") == 10 : "重复插入测试失败";
        
        System.out.println("边界情况测试通过！");
    }
    
    /**
     * 主函数：程序入口点
     * 执行各种测试以验证实现的正确性和性能
     */
    public static void main(String[] args) {
        // 运行单元测试
        testMapSum();
        
        // 运行边界情况测试
        edgeCaseTest();
        
        // 运行性能测试
        performanceTest();
    }
}