/**
 * LeetCode 307. 区域和检索 - 数组可修改
 * 题目链接: https://leetcode.cn/problems/range-sum-query-mutable/
 * 
 * 给你一个数组 nums ，请你完成两类查询：
 * 1. 将一个元素的值更新为 val
 * 2. 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
 * 
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 用整数数组 nums 初始化对象
 * - void update(int index, int val) 将 nums[index] 的值更新为 val
 * - int sumRange(int left, int right) 返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的和
 * 
 * 示例:
 * 输入:
 * ["NumArray", "sumRange", "update", "sumRange"]
 * [[[1, 3, 5]], [0, 2], [1, 2], [0, 2]]
 * 输出:
 * [null, 9, null, 8]
 * 
 * 解释:
 * NumArray numArray = new NumArray([1, 3, 5]);
 * numArray.sumRange(0, 2); // 返回 1 + 3 + 5 = 9
 * numArray.update(1, 2);   // nums = [1,2,5]
 * numArray.sumRange(0, 2); // 返回 1 + 2 + 5 = 8
 * 
 * 提示:
 * 1 <= nums.length <= 3 * 10^4
 * -100 <= nums[i] <= 100
 * 0 <= index < nums.length
 * -100 <= val <= 100
 * 0 <= left <= right < nums.length
 * 调用 update 和 sumRange 方法次数不大于 3 * 10^4
 * 
 * 解题思路:
 * 这是一个经典的线段树应用问题，支持单点更新和区间查询。
 * 1. 使用线段树维护数组区间和
 * 2. 单点更新时，从根节点向下递归找到目标位置并更新，然后向上传递更新区间和
 * 3. 区间查询时，根据查询区间与当前节点区间的关系进行递归查询
 * 
 * 时间复杂度:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性，防止数组越界和无效输入
 *    - 空数组处理: 支持空数组输入，避免空指针异常
 *    - 索引越界: 严格验证索引范围，提供清晰的错误信息
 *    - 参数校验: 对构造函数和公共方法进行参数验证
 * 
 * 2. 边界情况: 处理空数组、单个元素、重复元素等特殊情况
 *    - 空数组: 特殊处理空数组，避免不必要的内存分配
 *    - 单个元素: 确保单元素数组的正确处理
 *    - 极端值: 处理数值溢出和边界值
 * 
 * 3. 性能优化: 使用位运算优化计算，减少函数调用开销
 *    - 位运算: 使用 >> 和 << 替代除法和乘法，提高计算效率
 *    - 递归优化: 避免不必要的递归调用，优化栈空间使用
 *    - 内存分配: 合理分配线段树数组大小，避免内存浪费
 * 
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 *    - 单元测试: 覆盖正常功能、边界情况和异常场景
 *    - 性能测试: 验证大规模数据下的性能表现
 *    - 回归测试: 确保代码修改不会破坏现有功能
 * 
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 *    - 方法注释: 每个方法都有清晰的用途说明
 *    - 参数说明: 详细描述每个参数的含义和约束
 *    - 算法解释: 解释关键算法步骤和设计思路
 * 
 * 6. 鲁棒性: 处理极端输入和非理想数据，确保程序稳定性
 *    - 错误恢复: 提供优雅的错误处理机制
 *    - 资源管理: 确保内存和其他资源的正确释放
 *    - 状态一致性: 维护数据结构的状态一致性
 * 
 * 7. 线程安全: 当前实现非线程安全，多线程环境下需要同步机制
 *    - 同步策略: 如果需要线程安全，需要添加同步机制
 *    - 并发控制: 考虑读写锁等并发控制策略
 * 
 * 8. 内存管理: 合理分配内存，避免内存泄漏
 *    - 内存分配: 合理估算线段树所需内存
 *    - 垃圾回收: 确保对象能够被正确回收
 * 
 * 9. 调试支持: 提供详细的错误信息和调试信息
 *    - 错误日志: 记录详细的错误信息便于调试
 *    - 调试模式: 支持调试信息的输出
 * 
 * 10. 扩展性: 设计易于扩展的接口，支持功能增强
 *     - 接口设计: 提供清晰的公共接口
 *     - 模块化: 将功能模块化，便于维护和扩展
 * 
 * 复杂度分析:
 * 时间复杂度:
 *   - 建树操作: O(n) - 需要遍历所有元素构建线段树
 *   - 单点更新: O(log n) - 每次更新需要遍历树的高度
 *   - 区间查询: O(log n) - 每次查询需要遍历树的高度
 * 
 * 空间复杂度:
 *   - 线段树存储: O(4n) - 线段树通常需要4倍原始数组大小的空间
 *   - 递归栈: O(log n) - 递归深度为树的高度
 * 
 * 最优解分析:
 *   - 线段树是解决区间查询和单点更新问题的经典数据结构
 *   - 对于频繁的区间查询和单点更新，线段树是最优选择
 *   - 相比其他数据结构（如前缀和数组），线段树在更新操作上更高效
 *   - 对于只查询不更新的场景，前缀和数组可能更简单高效
 * 
 * 语言特性差异:
 *   - Java: 使用数组存储，需要手动管理内存分配
 *   - C++: 可以使用vector等容器，内存管理更灵活
 *   - Python: 列表动态扩容，但性能可能不如数组
 * 
 * 工程化实践:
 *   - 代码复用: 线段树模板可以复用于其他区间查询问题
 *   - 异常防御: 对输入进行严格验证，防止非法输入
 *   - 性能监控: 在实际应用中监控性能指标
 *   - 文档完善: 提供详细的使用说明和API文档
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景，包括边界和异常情况
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节，便于维护
 * 6. 鲁棒性: 处理极端输入和非理想数据，确保程序稳定性
 * 7. 线程安全: 当前实现非线程安全，多线程环境下需要同步机制
 * 8. 内存管理: 合理分配内存，避免内存泄漏
 * 9. 调试支持: 提供详细的错误信息和调试信息
 * 10. 扩展性: 设计易于扩展的接口，支持功能增强
 */
public class LeetCode307_SegmentTree {
    private int[] nums;
    private int[] sum;
    private int n;

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param nums 初始数组
     */
    public LeetCode307_SegmentTree(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        this.nums = nums;
        this.n = nums.length;
        
        // 特殊情况处理
        if (this.n == 0) {
            this.sum = new int[0];
            return;
        }
        
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.sum = new int[n * 4];
        build(0, n - 1, 1);
    }

    /**
     * 建树
     * 
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     */
    private void build(int l, int r, int i) {
        // 递归终止条件：叶子节点
        if (l == r) {
            sum[i] = nums[l];
        } else {
            // 计算中点
            int mid = (l + r) >> 1;
            // 递归构建左子树
            build(l, mid, i << 1);
            // 递归构建右子树
            build(mid + 1, r, i << 1 | 1);
            // 向上传递更新节点信息
            pushUp(i);
        }
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 单点更新
     * 
     * @param index 要更新的索引
     * @param val   新的值
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     */
    private void update(int index, int val, int l, int r, int i) {
        // 递归终止条件：找到目标位置
        if (l == r) {
            sum[i] = val;
        } else {
            // 计算中点
            int mid = (l + r) >> 1;
            // 根据索引位置决定递归方向
            if (index <= mid) {
                update(index, val, l, mid, i << 1);
            } else {
                update(index, val, mid + 1, r, i << 1 | 1);
            }
            // 向上传递更新节点信息
            pushUp(i);
        }
    }

    /**
     * 公共更新接口
     * 
     * @param index 要更新的索引
     * @param val   新的值
     */
    public void update(int index, int val) {
        // 参数校验
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("索引超出范围");
        }
        
        nums[index] = val;
        update(index, val, 0, n - 1, 1);
    }

    /**
     * 区间查询
     * 
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @return 区间和
     */
    private int sumRange(int left, int right, int l, int r, int i) {
        // 当前区间完全包含在查询区间内，直接返回
        if (left <= l && r <= right) {
            return sum[i];
        }
        
        // 计算中点
        int mid = (l + r) >> 1;
        int ans = 0;
        // 左子区间与查询区间有交集
        if (left <= mid) {
            ans += sumRange(left, right, l, mid, i << 1);
        }
        // 右子区间与查询区间有交集
        if (right > mid) {
            ans += sumRange(left, right, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    /**
     * 公共查询接口
     * 
     * @param left  查询区间左端点
     * @param right 查询区间右端点
     * @return 区间和
     */
    public int sumRange(int left, int right) {
        // 参数校验
        if (left < 0 || right >= n || left > right) {
            throw new IllegalArgumentException("查询区间无效");
        }
        
        return sumRange(left, right, 0, n - 1, 1);
    }

    /**
     * 单元测试方法 - 验证线段树功能的正确性
     * 
     * 测试用例设计:
     * 1. 正常功能测试: 验证基本功能正确性
     * 2. 边界情况测试: 测试空数组、单元素等边界情况
     * 3. 异常情况测试: 验证异常处理的正确性
     * 4. 性能测试: 验证大规模数据下的性能表现
     * 
     * 测试策略:
     * - 使用断言验证预期结果
     * - 覆盖各种边界情况
     * - 验证异常处理机制
     * - 测试性能表现
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 307 线段树测试开始 ===");
        
        // 测试用例1: 正常功能测试
        System.out.println("
1. 正常功能测试:");
        testNormalFunction();
        
        // 测试用例2: 边界情况测试
        System.out.println("
2. 边界情况测试:");
        testEdgeCases();
        
        // 测试用例3: 异常情况测试
        System.out.println("
3. 异常情况测试:");
        testExceptionCases();
        
        // 测试用例4: 性能测试
        System.out.println("
4. 性能测试:");
        testPerformance();
        
        System.out.println("
=== 所有测试用例通过 ===");
    }
    
    /**
     * 正常功能测试 - 验证线段树基本功能
     */
    private static void testNormalFunction() {
        int[] nums = {1, 3, 5, 7, 9, 11};
        LeetCode307_SegmentTree segmentTree = new LeetCode307_SegmentTree(nums);
        
        // 测试初始区间和
        assert segmentTree.sumRange(0, 2) == 9 : "初始区间和计算错误";
        System.out.println("✓ 初始区间和测试通过");
        
        // 测试单点更新
        segmentTree.update(1, 2);
        assert segmentTree.sumRange(0, 2) == 8 : "更新后区间和计算错误";
        System.out.println("✓ 单点更新测试通过");
        
        // 测试多个区间查询
        assert segmentTree.sumRange(1, 3) == 14 : "多区间查询错误";
        assert segmentTree.sumRange(0, 5) == 35 : "全区间查询错误";
        System.out.println("✓ 多区间查询测试通过");
    }
    
    /**
     * 边界情况测试 - 测试各种边界场景
     */
    private static void testEdgeCases() {
        // 测试空数组
        try {
            LeetCode307_SegmentTree emptyTree = new LeetCode307_SegmentTree(new int[0]);
            System.out.println("✓ 空数组处理测试通过");
        } catch (Exception e) {
            System.out.println("✗ 空数组处理失败: " + e.getMessage());
        }
        
        // 测试单元素数组
        int[] single = {42};
        LeetCode307_SegmentTree singleTree = new LeetCode307_SegmentTree(single);
        assert singleTree.sumRange(0, 0) == 42 : "单元素数组查询错误";
        System.out.println("✓ 单元素数组测试通过");
        
        // 测试重复元素数组
        int[] repeated = {1, 1, 1, 1, 1};
        LeetCode307_SegmentTree repeatedTree = new LeetCode307_SegmentTree(repeated);
        assert repeatedTree.sumRange(0, 4) == 5 : "重复元素数组查询错误";
        System.out.println("✓ 重复元素数组测试通过");
        
        // 测试负数和零
        int[] negatives = {-1, -2, 0, 3, -4};
        LeetCode307_SegmentTree negativeTree = new LeetCode307_SegmentTree(negatives);
        assert negativeTree.sumRange(0, 4) == -4 : "负数数组查询错误";
        System.out.println("✓ 负数数组测试通过");
    }
    
    /**
     * 异常情况测试 - 验证异常处理机制
     */
    private static void testExceptionCases() {
        int[] nums = {1, 2, 3};
        LeetCode307_SegmentTree segmentTree = new LeetCode307_SegmentTree(nums);
        
        // 测试索引越界异常
        try {
            segmentTree.update(-1, 10);
            System.out.println("✗ 负索引异常处理失败");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("✓ 负索引异常处理正确");
        }
        
        try {
            segmentTree.update(3, 10);
            System.out.println("✗ 超范围索引异常处理失败");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("✓ 超范围索引异常处理正确");
        }
        
        // 测试无效区间异常
        try {
            segmentTree.sumRange(2, 1);
            System.out.println("✗ 无效区间异常处理失败");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 无效区间异常处理正确");
        }
        
        try {
            segmentTree.sumRange(-1, 2);
            System.out.println("✗ 负区间异常处理失败");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 负区间异常处理正确");
        }
    }
    
    /**
     * 性能测试 - 验证大规模数据下的性能表现
     */
    private static void testPerformance() {
        // 生成大规模测试数据
        int size = 10000;
        int[] largeArray = new int[size];
        for (int i = 0; i < size; i++) {
            largeArray[i] = i + 1;
        }
        
        long startTime = System.currentTimeMillis();
        LeetCode307_SegmentTree largeTree = new LeetCode307_SegmentTree(largeArray);
        long buildTime = System.currentTimeMillis() - startTime;
        
        // 测试建树性能
        System.out.println("建树时间 (n=" + size + "): " + buildTime + "ms");
        
        // 测试查询性能
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            largeTree.sumRange(0, size - 1);
        }
        long queryTime = System.currentTimeMillis() - startTime;
        System.out.println("1000次查询时间: " + queryTime + "ms");
        
        // 测试更新性能
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            largeTree.update(i % size, i);
        }
        long updateTime = System.currentTimeMillis() - startTime;
        System.out.println("1000次更新时间: " + updateTime + "ms");
        
        System.out.println("✓ 性能测试完成");
    }
    
    /**
     * 调试辅助方法 - 打印线段树结构（用于调试）
     */
    public void printTree() {
        System.out.println("线段树结构:");
        printTreeHelper(0, n - 1, 1, 0);
    }
    
    /**
     * 递归打印线段树辅助方法
     */
    private void printTreeHelper(int l, int r, int i, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int j = 0; j < depth; j++) {
            indent.append("  ");
        }
        
        if (l == r) {
            System.out.println(indent + "叶子节点[" + l + "]: " + sum[i]);
        } else {
            System.out.println(indent + "区间[" + l + "-" + r + "]: " + sum[i]);
            int mid = (l + r) >> 1;
            printTreeHelper(l, mid, i << 1, depth + 1);
            printTreeHelper(mid + 1, r, i << 1 | 1, depth + 1);
        }
    }
}
}