package class045;

import java.util.*;

/**
 * LeetCode 1803. 统计异或值在范围内的数对有多少
 * 
 * 题目描述：
 * 给定一个整数数组nums和两个整数low和high，统计有多少数对(i, j)满足i < j且low <= (nums[i] XOR nums[j]) <= high。
 * 
 * 测试链接：https://leetcode.cn/problems/count-pairs-with-xor-in-a-range/
 * 
 * 算法思路：
 * 1. 使用前缀异或和与前缀树，通过两次查询（<=high和<low）得到结果
 * 2. 构建二进制前缀树，支持统计异或值在特定范围内的数对数量
 * 3. 利用前缀树的高效查询特性，避免暴力枚举
 * 
 * 核心优化思路：
 * 1. 范围查询转换：将[low, high]范围查询转换为两次<=查询的差值
 * 2. 前缀树优化：使用二进制前缀树存储数字，支持O(1)时间复杂度的<=查询
 * 3. 增量处理：逐个处理数组元素，动态维护前缀树
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(N * 32)，其中N是数组长度，32是整数的位数
 * - 查询过程：O(N * 32)
 * - 总体时间复杂度：O(N * 32)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N * 32)
 * - 总体空间复杂度：O(N * 32)
 * 
 * 是否最优解：是
 * 理由：使用前缀树可以在线性时间内统计异或值在范围内的数对数量
 * 
 * 工程化考虑：
 * 1. 异常处理：处理空数组和非法范围
 * 2. 边界情况：数组长度小于2或范围无效的情况
 * 3. 极端输入：大量数据或大数值的情况
 * 4. 内存管理：合理管理前缀树内存
 * 
 * 语言特性差异：
 * Java：使用二维数组实现前缀树，性能较高
 * C++：可使用指针实现，更节省空间
 * Python：可使用字典实现，代码更简洁
 * 
 * 调试技巧：
 * 1. 使用小规模数据验证算法正确性
 * 2. 打印中间结果调试查询过程
 * 3. 单元测试覆盖各种边界条件
 */
public class Code10_LeetCode1803 {
    
    /**
     * 主方法：统计异或值在[low, high]范围内的数对数量
     * 
     * 算法步骤详解：
     * 1. 边界检查：
     *    a. 检查数组是否为空或长度小于2
     *    b. 检查范围是否有效（low <= high）
     * 2. 初始化阶段：
     *    a. 初始化前缀树结构
     * 3. 增量处理阶段：
     *    a. 遍历数组中的每个数字
     *    b. 查询与之前数字的异或值在[low, high]范围内的数量
     *    c. 使用两次查询技巧：count(<=high) - count(<low)
     *    d. 将当前数字插入前缀树
     * 4. 清理阶段：
     *    a. 清空前缀树资源
     *    b. 返回结果
     * 
     * 范围查询转换原理：
     * 要统计异或值在[low, high]范围内的数对数量，
     * 可以转换为：count(<=high) - count(<low) = count(<=high) - count(<=low-1)
     * 这样可以复用相同的查询函数，简化实现。
     * 
     * 增量处理优势：
     * 1. 避免重复计算，提高效率
     * 2. 保证i < j的约束条件
     * 3. 动态维护前缀树，减少空间开销
     * 
     * @param nums 整数数组
     * @param low 范围下限
     * @param high 范围上限
     * @return 满足条件的数对数量
     */
    public static int countPairs(int[] nums, int low, int high) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        if (low > high) {
            return 0;
        }
        
        build();
        int count = 0;
        
        for (int num : nums) {
            // 查询与之前数字的异或值在[low, high]范围内的数量
            int highCount = countLessEqual(num, high);
            int lowCount = countLessEqual(num, low - 1);
            count += (highCount - lowCount);
            
            // 插入当前数字到前缀树
            insert(num);
        }
        
        clear();
        return count;
    }
    
    /**
     * 统计与num异或值<=target的数字数量
     * 
     * 算法步骤：
     * 1. 边界检查：如果target < 0，返回0
     * 2. 从前缀树根节点开始，从最高位到最低位遍历：
     *    a. 提取num和target当前位的值
     *    b. 计算num当前位的相反位
     *    c. 根据target当前位的值决定搜索策略：
     *       i. 如果target当前位为1：
     *          - 选择与num相同位的所有数字都满足条件（异或结果为0）
     *          - 累加这些数字的数量
     *          - 继续在相反位搜索（异或结果为1）
     *       ii. 如果target当前位为0：
     *          - 只能选择与num相同位（异或结果为0）
     * 3. 处理最后一位，累加当前节点的数字数量
     * 
     * 核心思想：
     * 通过同时遍历num和target的二进制位，
     * 根据target当前位的值决定搜索路径，
     * 累加满足异或值<=target的数字数量。
     * 
     * @param num 当前数字
     * @param target 目标值
     * @return 异或值<=target的数字数量
     */
    public static int countLessEqual(int num, int target) {
        if (target < 0) {
            return 0;
        }
        
        int cur = 1;
        int count = 0;
        
        for (int i = 31; i >= 0; i--) {
            int numBit = (num >> i) & 1;
            int targetBit = (target >> i) & 1;
            int opposite = 1 - numBit;
            
            if (targetBit == 1) {
                // 如果target当前位为1，那么选择相同位的所有数字都满足条件
                if (tree[cur][numBit] != 0) {
                    count += pass[tree[cur][numBit]];
                }
                // 继续在相反位搜索
                if (tree[cur][opposite] != 0) {
                    cur = tree[cur][opposite];
                } else {
                    return count;
                }
            } else {
                // 如果target当前位为0，只能选择相同位
                if (tree[cur][numBit] != 0) {
                    cur = tree[cur][numBit];
                } else {
                    return count;
                }
            }
        }
        
        // 处理最后一位
        count += pass[cur];
        return count;
    }
    
    // 前缀树相关变量
    public static int MAXN = 2000000;
    public static int[][] tree = new int[MAXN][2];
    public static int[] pass = new int[MAXN];
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
     * 1. 从根节点开始，增加根节点的通过计数
     * 2. 从最高位（第31位）开始处理数字的二进制表示：
     *    a. 提取当前位的值（0或1）
     *    b. 如果对应的子节点不存在，则创建新节点
     *    c. 移动到子节点
     *    d. 增加当前节点的通过计数
     * 
     * 通过计数用途：
     * pass数组记录经过每个节点的数字数量，
     * 在查询时用于快速统计满足条件的数字数量。
     */
    public static void insert(int num) {
        int cur = 1;
        pass[cur]++;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (tree[cur][bit] == 0) {
                tree[cur][bit] = ++cnt;
            }
            cur = tree[cur][bit];
            pass[cur]++;
        }
    }
    
    /**
     * 清空前缀树
     * 
     * 算法步骤：
     * 1. 遍历所有已使用的节点（从1到cnt）
     * 2. 将节点的两个子节点引用清零
     * 3. 将节点的通过计数重置为0
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
            pass[i] = 0;
        }
    }
    
    /**
     * 暴力解法（用于验证正确性）
     * 
     * 算法步骤：
     * 1. 边界检查：检查数组是否为空或长度小于2
     * 2. 遍历所有数对(i, j)，其中i < j：
     *    a. 计算nums[i] XOR nums[j]
     *    b. 检查异或值是否在[low, high]范围内
     *    c. 如果满足条件，增加计数
     * 3. 返回结果
     * 
     * 时间复杂度：O(N^2)，其中N是数组长度
     * 空间复杂度：O(1)
     * 
     * 用途：
     * 用于验证优化算法的正确性，
     * 在小规模数据上进行对比测试。
     */
    public static int countPairsBruteForce(int[] nums, int low, int high) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        int count = 0;
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int xor = nums[i] ^ nums[j];
                if (xor >= low && xor <= high) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 单元测试方法
     * 
     * 测试用例设计：
     * 1. 基础测试：验证正常情况下的功能正确性
     * 2. 边界测试：验证空数组、单元素等边界情况
     * 3. 异常测试：验证无效范围等异常情况
     * 4. 极值测试：验证大数值情况下的正确性
     * 
     * 测试策略：
     * 1. 使用断言验证每个测试用例的正确性
     * 2. 与暴力解法对比验证结果正确性
     * 3. 覆盖各种边界条件和异常场景
     */
    public static void testCountPairs() {
        // 测试用例1：基础测试
        int[] nums1 = {1, 4, 2, 7};
        int low1 = 2, high1 = 6;
        int result1 = countPairs(nums1, low1, high1);
        int expected1 = countPairsBruteForce(nums1, low1, high1);
        assert result1 == expected1 : "测试用例1失败: " + result1 + " != " + expected1;
        
        // 测试用例2：空数组
        int[] nums2 = {};
        int result2 = countPairs(nums2, 0, 10);
        assert result2 == 0 : "测试用例2失败";
        
        // 测试用例3：单个元素
        int[] nums3 = {5};
        int result3 = countPairs(nums3, 0, 10);
        assert result3 == 0 : "测试用例3失败";
        
        // 测试用例4：无效范围
        int[] nums4 = {1, 2, 3};
        int result4 = countPairs(nums4, 5, 1);
        assert result4 == 0 : "测试用例4失败";
        
        // 测试用例5：大数值
        int[] nums5 = {Integer.MAX_VALUE, Integer.MAX_VALUE - 1};
        int result5 = countPairs(nums5, 0, Integer.MAX_VALUE);
        int expected5 = countPairsBruteForce(nums5, 0, Integer.MAX_VALUE);
        assert result5 == expected5 : "测试用例5失败";
        
        System.out.println("所有单元测试通过！");
    }
    
    /**
     * 性能测试方法
     * 
     * 测试目标：
     * 1. 验证算法在大规模数据下的性能表现
     * 2. 测量各操作的执行时间
     * 3. 验证在大数据量下的稳定性
     * 4. 与暴力解法进行对比（小规模数据）
     * 
     * 测试数据：
     * 使用10000个随机数字进行测试，
     * 模拟实际应用场景中的数据规模。
     * 
     * 性能指标：
     * 1. 优化算法执行时间
     * 2. 暴力解法执行时间（小规模数据）
     * 3. 结果一致性验证
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        int[] nums = new int[n];
        Random random = new Random();
        
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(1000000);
        }
        
        int low = 1000;
        int high = 10000;
        
        long startTime = System.currentTimeMillis();
        int result = countPairs(nums, low, high);
        long endTime = System.currentTimeMillis();
        
        System.out.println("性能测试结果: " + result + " 个数对");
        System.out.println("处理 " + n + " 个数字耗时: " + (endTime - startTime) + "ms");
        
        // 对比暴力解法（小规模验证）
        if (n <= 1000) {
            long bruteStart = System.currentTimeMillis();
            int bruteResult = countPairsBruteForce(nums, low, high);
            long bruteEnd = System.currentTimeMillis();
            
            System.out.println("暴力解法结果: " + bruteResult + " 个数对");
            System.out.println("暴力解法耗时: " + (bruteEnd - bruteStart) + "ms");
            assert result == bruteResult : "结果不一致！";
        }
    }
    
    public static void main(String[] args) {
        // 运行单元测试
        testCountPairs();
        
        // 运行性能测试
        performanceTest();
    }
}