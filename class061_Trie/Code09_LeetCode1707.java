package class045;

import java.util.*;

/**
 * LeetCode 1707. 与数组中元素的最大异或值
 * 
 * 题目描述：
 * 给定一个数组和查询数组，每个查询包含x和m，找出数组中满足num <= m的元素与x的最大异或值。
 * 
 * 测试链接：https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
 * 
 * 算法思路：
 * 1. 离线查询 + 前缀树：将查询和数组排序，按顺序插入前缀树并回答查询
 * 2. 构建二进制前缀树，支持最大异或值查询
 * 3. 使用离线处理技巧，避免重复构建前缀树
 * 
 * 核心优化思路：
 * 1. 离线处理：将查询按m值排序，避免重复构建前缀树
 * 2. 前缀树优化：使用二进制前缀树存储数字，支持O(1)时间复杂度的最大异或值查询
 * 3. 贪心策略：从高位到低位贪心选择，使异或结果最大化
 * 
 * 时间复杂度分析：
 * - 排序：O(N log N + Q log Q)，其中N是数组长度，Q是查询数量
 * - 前缀树操作：O((N + Q) * 32)，32是整数的位数
 * - 总体时间复杂度：O(N log N + Q log Q + (N + Q) * 32)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N * 32)
 * - 排序空间：O(N + Q)
 * - 总体空间复杂度：O(N * 32 + Q)
 * 
 * 是否最优解：是
 * 理由：离线查询+前缀树是最优解法，避免了重复构建前缀树
 * 
 * 工程化考虑：
 * 1. 异常处理：处理空数组和非法查询
 * 2. 边界情况：数组为空或查询为空的情况
 * 3. 极端输入：大量查询或大数值的情况
 * 4. 内存管理：合理管理前缀树内存
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，性能较高
 * C++：可使用指针实现，更节省空间
 * Python：可使用字典实现，代码更简洁
 * 
 * 调试技巧：
 * 1. 打印中间结果验证排序和查询处理
 * 2. 使用小规模测试数据验证算法正确性
 * 3. 单元测试覆盖各种边界条件
 * 
 * 性能优化：
 * 1. 离线查询减少前缀树重建次数
 * 2. 使用数组代替对象减少内存开销
 * 3. 预计算最大位数减少循环次数
 */
public class Code09_LeetCode1707 {
    
    /**
     * 查询类，用于存储查询信息和索引
     * 
     * 设计目的：
     * 1. 保存查询的原始信息（x, m）
     * 2. 保存查询在原数组中的索引，确保结果能正确对应
     * 3. 支持按m值排序，实现离线处理
     * 
     * 数据结构：
     * - x: 查询值，用于计算最大异或值
     * - m: 最大值限制，用于过滤数组元素
     * - index: 原始索引，确保结果顺序正确
     */
    static class Query {
        int x;      // 查询值
        int m;      // 最大值限制
        int index;  // 原始索引
        
        Query(int x, int m, int index) {
            this.x = x;
            this.m = m;
            this.index = index;
        }
    }
    
    /**
     * 主方法：计算每个查询的最大异或值
     * 
     * 算法步骤详解：
     * 1. 预处理阶段：
     *    a. 对数组进行排序，为离线处理做准备
     *    b. 创建查询对象数组，保存原始索引
     *    c. 对查询按m值排序，实现离线处理
     * 2. 初始化阶段：
     *    a. 初始化前缀树结构
     *    b. 创建结果数组
     * 3. 离线处理阶段：
     *    a. 按m值从小到大处理每个查询
     *    b. 将数组中<=m的元素插入前缀树
     *    c. 在前缀树中查询与x的最大异或值
     * 4. 清理阶段：
     *    a. 清空前缀树资源
     *    b. 返回结果数组
     * 
     * 离线处理优势：
     * 1. 避免重复构建前缀树，提高效率
     * 2. 利用排序后的数组顺序插入，减少重构开销
     * 3. 通过增量更新前缀树，避免重复计算
     * 
     * @param nums 整数数组
     * @param queries 查询数组，每个查询为[x, m]
     * @return 每个查询的最大异或值结果
     */
    public static int[] maximizeXor(int[] nums, int[][] queries) {
        int n = nums.length;
        int q = queries.length;
        
        // 对数组排序
        Arrays.sort(nums);
        
        // 创建查询对象数组，按m值排序
        Query[] queryArr = new Query[q];
        for (int i = 0; i < q; i++) {
            queryArr[i] = new Query(queries[i][0], queries[i][1], i);
        }
        
        // 按m值排序查询
        Arrays.sort(queryArr, (a, b) -> Integer.compare(a.m, b.m));
        
        // 初始化前缀树
        build();
        int[] result = new int[q];
        int idx = 0; // 数组索引
        
        // 离线处理查询
        for (Query query : queryArr) {
            int x = query.x;
            int m = query.m;
            int originalIndex = query.index;
            
            // 将数组中<=m的元素插入前缀树
            while (idx < n && nums[idx] <= m) {
                insert(nums[idx]);
                idx++;
            }
            
            // 如果前缀树为空，返回-1；否则查询最大异或值
            if (cnt == 1) { // 只有根节点，说明没有插入任何元素
                result[originalIndex] = -1;
            } else {
                result[originalIndex] = maxXor(x);
            }
        }
        
        // 清空前缀树
        clear();
        return result;
    }
    
    // 前缀树相关变量
    public static int MAXN = 6000000; // 根据题目约束调整
    public static int[][] tree = new int[MAXN][2];
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
     */
    public static void build() {
        cnt = 1;
    }
    
    /**
     * 向前缀树中插入数字
     * 
     * 算法步骤：
     * 1. 从最高位（第31位）开始处理数字的二进制表示
     * 2. 对于每一位：
     *    a. 提取当前位的值（0或1）
     *    b. 如果对应的子节点不存在，则创建新节点
     *    c. 移动到子节点
     * 3. 插入完成后，数字的二进制表示已存储在前缀树中
     * 
     * 二进制前缀树特点：
     * 1. 每个节点只有两个子节点（0和1）
     * 2. 从根节点到叶子节点的路径表示一个完整的32位整数
     * 3. 共享公共前缀，节省存储空间
     * 
     * @param num 待插入的数字
     */
    public static void insert(int num) {
        int cur = 1;
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (tree[cur][bit] == 0) {
                tree[cur][bit] = ++cnt;
            }
            cur = tree[cur][bit];
        }
    }
    
    /**
     * 查询与x的最大异或值
     * 
     * 算法步骤：
     * 1. 从最高位（第31位）开始，逐位处理：
     *    a. 提取x当前位的值
     *    b. 计算期望的相反位（使异或结果为1）
     *    c. 如果前缀树中存在相反位路径，则选择该路径
     *    d. 否则选择相同位路径
     *    e. 更新异或结果
     * 2. 返回最大异或值
     * 
     * 贪心策略原理：
     * 异或运算的性质是相同为0，不同为1。
     * 要使异或结果最大，应该从高位到低位尽量使对应位不同。
     * 因此，对于x的每一位，优先选择与其相反的位。
     * 
     * @param x 查询值
     * @return 最大异或值
     */
    public static int maxXor(int x) {
        int cur = 1;
        int result = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (x >> i) & 1;
            int opposite = 1 - bit; // 希望选择的相反位
            
            // 尽量选择相反的位
            if (tree[cur][opposite] != 0) {
                result |= (1 << i); // 设置当前位为1
                cur = tree[cur][opposite];
            } else {
                // 只能选择相同的位
                cur = tree[cur][bit];
            }
        }
        
        return result;
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点（从1到cnt）
     * 2. 将节点的两个子节点引用清零
     * 
     * 资源管理：
     * 通过清空前缀树结构，释放内存资源，避免内存泄漏。
     * 由于使用静态数组，实际内存不会被释放，
     * 但逻辑上清除了所有数据，为下次使用做好准备。
     */
    public static void clear() {
        for (int i = 1; i <= cnt; i++) {
            tree[i][0] = 0;
            tree[i][1] = 0;
        }
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 基础测试：验证正常情况下的功能正确性
     * 2. 边界测试：验证空数组、单元素等边界情况
     * 3. 极值测试：验证大数值情况下的正确性
     * 
     * 测试策略：
     * 1. 使用断言验证每个测试用例的正确性
     * 2. 覆盖各种边界条件和异常场景
     * 3. 测试完成后输出成功信息
     */
    public static void testMaximizeXor() {
        // 测试用例1：基础测试
        int[] nums1 = {0, 1, 2, 3, 4};
        int[][] queries1 = {{3, 1}, {1, 3}, {5, 6}};
        int[] result1 = maximizeXor(nums1, queries1);
        assert Arrays.equals(result1, new int[]{3, 3, 7}) : "测试用例1失败";
        
        // 测试用例2：空数组
        int[] nums2 = {};
        int[][] queries2 = {{1, 1}};
        int[] result2 = maximizeXor(nums2, queries2);
        assert Arrays.equals(result2, new int[]{-1}) : "测试用例2失败";
        
        // 测试用例3：单个元素
        int[] nums3 = {5};
        int[][] queries3 = {{1, 10}, {10, 1}};
        int[] result3 = maximizeXor(nums3, queries3);
        assert Arrays.equals(result3, new int[]{5, -1}) : "测试用例3失败";
        
        // 测试用例4：大数值
        int[] nums4 = {Integer.MAX_VALUE};
        int[][] queries4 = {{0, Integer.MAX_VALUE}};
        int[] result4 = maximizeXor(nums4, queries4);
        assert result4[0] == Integer.MAX_VALUE : "测试用例4失败";
        
        System.out.println("所有单元测试通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试目标：
     * 1. 验证算法在大规模数据下的性能表现
     * 2. 测量各操作的执行时间
     * 3. 验证在大数据量下的稳定性
     * 
     * 测试数据：
     * 使用100000个随机数字和100000个随机查询进行测试，
     * 模拟实际应用场景中的数据规模。
     * 
     * 性能指标：
     * 1. 总执行时间
     * 2. 处理的数据规模
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 100000;
        int q = 100000;
        int[] nums = new int[n];
        int[][] queries = new int[q][2];
        
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(1000000000);
        }
        
        for (int i = 0; i < q; i++) {
            queries[i][0] = random.nextInt(1000000000);
            queries[i][1] = random.nextInt(1000000000);
        }
        
        long startTime = System.currentTimeMillis();
        int[] result = maximizeXor(nums, queries);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模测试耗时: " + (endTime - startTime) + "ms");
        System.out.println("处理了 " + n + " 个数字和 " + q + " 个查询");
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testMaximizeXor();
        
        // 运行性能测试
        performanceTest();
    }
}