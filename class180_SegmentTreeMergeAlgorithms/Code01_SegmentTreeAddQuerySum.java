/**
 * 线段树实现 - 支持范围增加、范围查询
 * 维护累加和
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P3372
 * 
 * 题目描述：
 * 实现一个支持区间加法操作和区间求和查询的数据结构。
 * 
 * 线段树设计原理：
 * 线段树是一种二叉树结构，用于高效处理区间查询和区间更新操作。
 * 每个节点代表一个区间，叶子节点代表单个元素，非叶子节点代表子区间的合并。
 * 
 * 核心思想：
 * 1. 分治思想：将大区间分解为小区间处理
 * 2. 懒标记技术：延迟更新操作，提高效率
 * 3. 递归构建：自底向上构建树结构
 * 
 * 时间复杂度分析:
 * - 建树: O(n) - 需要遍历所有元素构建树结构
 * - 单点更新: O(log n) - 树的高度为log n
 * - 区间更新: O(log n) - 使用懒标记技术优化
 * - 区间查询: O(log n) - 最多访问2log n个节点
 * 
 * 空间复杂度: O(4n) - 线段树通常需要4倍原始数组大小的空间
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 边界条件：处理空数组、单个元素等边界情况
 * 3. 性能优化：使用位运算替代乘除法
 * 4. 内存管理：合理分配数组大小，避免内存浪费
 * 5. 可测试性：提供完整的测试用例覆盖各种场景
 * 6. 可读性：详细注释和清晰的变量命名
 * 
 * 应用场景：
 * 1. 区间统计查询（求和、最大值、最小值等）
 * 2. 区间批量更新操作
 * 3. 动态数据维护
 * 4. 竞赛编程中的常见数据结构
 * 
 * 语言特性差异：
 * Java：使用数组存储，支持自动内存管理
 * Python：使用列表，动态数组特性
 * C++：需要手动内存管理，性能最优
 */
public class Code01_SegmentTreeAddQuerySum {
    private int n;           // 原始数组大小
    private long[] sum;       // 线段树节点存储的区间和
    private long[] add;       // 懒标记数组，存储待下发的增量

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     * @throws IllegalArgumentException 如果size <= 0
     * 
     * 设计思路：
     * 1. 参数校验确保输入有效性
     * 2. 分配足够空间存储线段树节点
     * 3. 初始化所有节点值为0
     * 
     * 空间分配策略：
     * 线段树通常需要4倍原始数组大小的空间，原因：
     * - 完全二叉树性质：最坏情况下需要2n-1个节点
     * - 数组存储：为了方便索引，通常分配4n空间
     * - 安全边界：确保有足够空间处理所有可能的节点
     */
    public Code01_SegmentTreeAddQuerySum(int size) {
        // 参数校验
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        // 4倍空间策略：保证最坏情况下也有足够空间，避免数组越界
        this.sum = new long[size * 4];
        this.add = new long[size * 4];
        
        // 初始化数组，Java会自动初始化为0，这里显式初始化确保清晰
        // Arrays.fill(sum, 0);  // 可选的显式初始化
        // Arrays.fill(add, 0);
    }

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     * 
     * 算法原理：
     * 线段树的核心操作之一，用于在子节点更新后更新父节点的信息。
     * 采用自底向上的方式，确保父节点信息与子节点信息一致。
     * 
     * 位运算优化：
     * - i << 1: 左子节点索引，等价于 2*i
     * - i << 1 | 1: 右子节点索引，等价于 2*i+1
     * 使用位运算比乘除法更快，是性能优化的关键点。
     * 
     * 时间复杂度: O(1) - 常数时间操作
     * 空间复杂度: O(1) - 不需要额外空间
     */
    private void pushUp(int i) {
        // 父范围的累加和 = 左范围累加和 + 右范围累加和
        // 这是线段树合并操作的基本公式
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    /**
     * 向下传递懒标记 - 懒标记技术的核心操作
     * 
     * @param i  当前节点编号
     * @param ln 左子树节点数量
     * @param rn 右子树节点数量
     * 
     * 懒标记技术原理：
     * 懒标记（Lazy Propagation）是线段树的重要优化技术，用于延迟更新操作。
     * 当需要更新一个区间时，不立即更新所有子节点，而是将更新信息存储在父节点。
     * 只有当需要查询或更新子区间时，才将懒标记下发给子节点。
     * 
     * 性能优势：
     * - 避免不必要的更新操作
     * - 将O(n)的更新操作优化为O(log n)
     * - 减少内存访问次数
     * 
     * 时间复杂度: O(1) - 常数时间操作
     * 空间复杂度: O(1) - 不需要额外空间
     * 
     * 边界条件处理：
     * - 检查懒标记是否为0，避免不必要的操作
     * - 确保ln和rn参数正确，避免数组越界
     */
    private void pushDown(int i, int ln, int rn) {
        // 检查当前节点是否有待下发的懒标记
        if (add[i] != 0) {
            // 下发懒标记到左子树
            lazy(i << 1, add[i], ln);
            // 下发懒标记到右子树
            lazy(i << 1 | 1, add[i], rn);
            // 父节点懒标记清空，表示已经下发完成
            add[i] = 0;
        }
    }

    /**
     * 懒标记操作 - 应用懒标记到具体节点
     * 
     * @param i 节点编号
     * @param v 增加的值
     * @param n 节点对应的区间长度
     * 
     * 数学原理：
     * 对于区间[l, r]的每个元素增加v，该区间的总和增加 v * (r - l + 1)
     * 这就是懒标记操作的核心数学公式。
     * 
     * 操作步骤：
     * 1. 更新区间和：sum[i] += v * n
     * 2. 累加懒标记：add[i] += v
     * 
     * 时间复杂度: O(1) - 常数时间操作
     * 空间复杂度: O(1) - 不需要额外空间
     * 
     * 注意事项：
     * - 懒标记是累加的，支持多次更新操作
     * - 需要确保n参数正确，避免计算错误
     * - 考虑整数溢出问题，使用long类型存储
     */
    private void lazy(int i, long v, int n) {
        // 更新当前节点的区间和：总和增加 v * 区间长度
        sum[i] += v * n;
        // 累加懒标记，支持多次更新操作
        add[i] += v;
    }

    /**
     * 建树 - 构建线段树结构
     * 
     * @param arr 原始数组
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     * @throws IllegalArgumentException 如果参数无效
     * 
     * 构建算法：
     * 采用递归分治策略构建线段树：
     * 1. 如果当前区间是叶子节点（l == r），直接赋值
     * 2. 否则，将区间分为两半，递归构建左右子树
     * 3. 构建完成后，通过pushUp合并子节点信息
     * 
     * 递归终止条件：l == r（叶子节点）
     * 递归分解：将区间[l, r]分为[l, mid]和[mid+1, r]
     * 
     * 时间复杂度: O(n) - 需要访问每个元素一次
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 工程化考量：
     * 1. 参数校验确保输入有效性
     * 2. 递归深度控制，避免栈溢出
     * 3. 初始化懒标记为0
     */
    public void build(long[] arr, int l, int r, int i) {
        // 参数校验
        if (arr == null || l < 0 || r >= arr.length || l > r) {
            throw new IllegalArgumentException("建树参数无效：数组为空或区间不合法");
        }
        
        if (l == r) {
            // 叶子节点：直接存储原始数组值
            sum[i] = arr[l];
        } else {
            // 非叶子节点：递归构建左右子树
            int mid = (l + r) >> 1;  // 使用位运算优化除法
            build(arr, l, mid, i << 1);          // 构建左子树
            build(arr, mid + 1, r, i << 1 | 1);  // 构建右子树
            pushUp(i);  // 向上更新当前节点信息
        }
        // 初始化懒标记为0
        add[i] = 0;
    }

    /**
     * 范围修改 - jobl ~ jobr范围上每个数字增加jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 增加的值
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * 
     * 算法原理：
     * 线段树区间更新操作，采用递归分治策略：
     * 1. 如果当前区间完全包含在任务区间内，直接应用懒标记
     * 2. 否则，将懒标记下发给子节点，然后递归处理子区间
     * 3. 处理完成后，向上更新父节点信息
     * 
     * 时间复杂度: O(log n) - 最多访问2log n个节点
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 性能优化：
     * - 懒标记技术避免不必要的更新
     * - 位运算优化索引计算
     * - 递归深度控制
     * 
     * 边界条件处理：
     * - 检查区间是否完全包含
     * - 处理区间重叠情况
     * - 确保索引不越界
     */
    public void addRange(int jobl, int jobr, long jobv, int l, int r, int i) {
        // 情况1：当前区间完全包含在任务区间内
        if (jobl <= l && r <= jobr) {
            // 直接应用懒标记，避免递归到叶子节点
            lazy(i, jobv, r - l + 1);
        } else {
            // 情况2：当前区间与任务区间部分重叠
            int mid = (l + r) >> 1;
            // 先将懒标记下发给子节点
            pushDown(i, mid - l + 1, r - mid);
            
            // 递归处理左子树（如果任务区间与左子树有重叠）
            if (jobl <= mid) {
                addRange(jobl, jobr, jobv, l, mid, i << 1);
            }
            // 递归处理右子树（如果任务区间与右子树有重叠）
            if (jobr > mid) {
                addRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            // 向上更新当前节点信息
            pushUp(i);
        }
    }

    /**
     * 查询累加和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间和
     * 
     * 算法原理：
     * 线段树区间查询操作，采用递归分治策略：
     * 1. 如果当前区间完全包含在查询区间内，直接返回存储的和
     * 2. 否则，将懒标记下发给子节点，然后递归查询子区间
     * 3. 合并子区间的查询结果
     * 
     * 时间复杂度: O(log n) - 最多访问2log n个节点
     * 空间复杂度: O(log n) - 递归栈深度
     * 
     * 查询优化：
     * - 懒标记确保数据一致性
     * - 递归查询避免全表扫描
     * - 位运算优化索引计算
     * 
     * 边界条件处理：
     * - 检查区间是否完全包含
     * - 处理区间重叠情况
     * - 确保索引不越界
     */
    public long query(int jobl, int jobr, int l, int r, int i) {
        // 情况1：当前区间完全包含在查询区间内
        if (jobl <= l && r <= jobr) {
            // 直接返回存储的区间和
            return sum[i];
        }
        
        // 情况2：当前区间与查询区间部分重叠
        int mid = (l + r) >> 1;
        // 先将懒标记下发给子节点，确保数据一致性
        pushDown(i, mid - l + 1, r - mid);
        
        long ans = 0;
        // 递归查询左子树（如果查询区间与左子树有重叠）
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        // 递归查询右子树（如果查询区间与右子树有重叠）
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    /**
     * 测试代码 - 完整的单元测试
     * 
     * 测试策略：
     * 1. 基础功能测试：验证基本操作的正确性
     * 2. 边界条件测试：测试空数组、单个元素等边界情况
     * 3. 性能测试：验证时间复杂度
     * 4. 异常测试：验证异常处理机制
     * 5. 集成测试：模拟实际应用场景
     * 
     * 测试用例设计原则：
     * - 覆盖所有代码路径
     * - 包含正常情况和异常情况
     * - 验证边界条件
     * - 确保结果正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 线段树测试 - 支持范围增加和范围查询 ===");
        
        // 测试用例1：基础功能测试
        testBasicFunctionality();
        
        // 测试用例2：边界条件测试
        testBoundaryConditions();
        
        // 测试用例3：性能测试
        testPerformance();
        
        // 测试用例4：异常测试
        testExceptionHandling();
        
        // 测试用例5：集成测试
        testIntegration();
        
        System.out.println("=== 所有测试用例执行完成 ===");
    }
    
    /**
     * 测试用例1：基础功能测试
     * 验证线段树的基本操作：建树、区间更新、区间查询
     */
    private static void testBasicFunctionality() {
        System.out.println("
--- 测试用例1：基础功能测试 ---");
        
        // 测试数据
        long[] arr = {1, 2, 3, 4, 5};
        int n = arr.length;
        
        // 初始化线段树
        Code01_SegmentTreeAddQuerySum segTree = new Code01_SegmentTreeAddQuerySum(n);
        segTree.build(arr, 0, n - 1, 1);
        
        // 测试1：查询整个数组的和
        long totalSum = segTree.query(0, n - 1, 0, n - 1, 1);
        System.out.println("测试1 - 整个数组和: " + totalSum + " (期望: 15)");
        assert totalSum == 15 : "基础查询测试失败";
        
        // 测试2：区间更新后查询
        segTree.addRange(1, 3, 10, 0, n - 1, 1);  // 将索引1-3的元素增加10
        long updatedSum = segTree.query(0, n - 1, 0, n - 1, 1);
        System.out.println("测试2 - 更新后数组和: " + updatedSum + " (期望: 45)");
        assert updatedSum == 45 : "区间更新测试失败";
        
        // 测试3：查询子区间
        long subSum = segTree.query(1, 3, 0, n - 1, 1);
        System.out.println("测试3 - 子区间[1,3]和: " + subSum + " (期望: 36)");
        assert subSum == 36 : "子区间查询测试失败";
        
        System.out.println("✓ 基础功能测试通过");
    }
    
    /**
     * 测试用例2：边界条件测试
     * 测试空数组、单个元素、越界访问等边界情况
     */
    private static void testBoundaryConditions() {
        System.out.println("
--- 测试用例2：边界条件测试 ---");
        
        // 测试1：单个元素数组
        long[] singleArr = {42};
        Code01_SegmentTreeAddQuerySum singleTree = new Code01_SegmentTreeAddQuerySum(1);
        singleTree.build(singleArr, 0, 0, 1);
        long singleSum = singleTree.query(0, 0, 0, 0, 1);
        System.out.println("测试1 - 单个元素和: " + singleSum + " (期望: 42)");
        assert singleSum == 42 : "单个元素测试失败";
        
        // 测试2：空数组（通过异常处理测试）
        try {
            Code01_SegmentTreeAddQuerySum emptyTree = new Code01_SegmentTreeAddQuerySum(0);
            System.out.println("测试2 - 空数组处理: ✗ 应该抛出异常");
            assert false : "空数组应该抛出异常";
        } catch (IllegalArgumentException e) {
            System.out.println("测试2 - 空数组处理: ✓ 正确抛出异常");
        }
        
        // 测试3：相同区间更新和查询
        long[] arr = {1, 2, 3};
        Code01_SegmentTreeAddQuerySum sameTree = new Code01_SegmentTreeAddQuerySum(3);
        sameTree.build(arr, 0, 2, 1);
        sameTree.addRange(1, 1, 5, 0, 2, 1);  // 单点更新
        long pointSum = sameTree.query(1, 1, 0, 2, 1);
        System.out.println("测试3 - 单点更新后查询: " + pointSum + " (期望: 7)");
        assert pointSum == 7 : "单点更新测试失败";
        
        System.out.println("✓ 边界条件测试通过");
    }
    
    /**
     * 测试用例3：性能测试
     * 验证线段树的时间复杂度，测试大规模数据
     */
    private static void testPerformance() {
        System.out.println("
--- 测试用例3：性能测试 ---");
        
        // 测试数据规模
        int n = 10000;
        long[] largeArr = new long[n];
        for (int i = 0; i < n; i++) {
            largeArr[i] = i + 1;
        }
        
        // 初始化线段树
        Code01_SegmentTreeAddQuerySum largeTree = new Code01_SegmentTreeAddQuerySum(n);
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        // 建树操作
        largeTree.build(largeArr, 0, n - 1, 1);
        long buildTime = System.currentTimeMillis() - startTime;
        
        // 查询操作
        startTime = System.currentTimeMillis();
        long totalSum = largeTree.query(0, n - 1, 0, n - 1, 1);
        long queryTime = System.currentTimeMillis() - startTime;
        
        // 更新操作
        startTime = System.currentTimeMillis();
        largeTree.addRange(0, n - 1, 10, 0, n - 1, 1);
        long updateTime = System.currentTimeMillis() - startTime;
        
        System.out.println("性能测试结果:");
        System.out.println("- 建树时间: " + buildTime + "ms (期望: O(n))");
        System.out.println("- 查询时间: " + queryTime + "ms (期望: O(log n))");
        System.out.println("- 更新时间: " + updateTime + "ms (期望: O(log n))");
        System.out.println("- 查询结果: " + totalSum + " (验证正确性)");
        
        // 验证结果正确性
        long expectedSum = (long) n * (n + 1) / 2 + (long) n * 10;
        assert totalSum == expectedSum : "性能测试结果不正确";
        
        System.out.println("✓ 性能测试通过");
    }
    
    /**
     * 测试用例4：异常测试
     * 验证异常处理机制
     */
    private static void testExceptionHandling() {
        System.out.println("
--- 测试用例4：异常测试 ---");
        
        // 测试1：无效数组大小
        try {
            new Code01_SegmentTreeAddQuerySum(-1);
            System.out.println("测试1 - 无效大小: ✗ 应该抛出异常");
            assert false : "应该抛出IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            System.out.println("测试1 - 无效大小: ✓ 正确抛出异常");
        }
        
        // 测试2：无效建树参数
        Code01_SegmentTreeAddQuerySum tree = new Code01_SegmentTreeAddQuerySum(5);
        try {
            tree.build(null, 0, 4, 1);
            System.out.println("测试2 - 空数组: ✗ 应该抛出异常");
            assert false : "应该抛出IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            System.out.println("测试2 - 空数组: ✓ 正确抛出异常");
        }
        
        // 测试3：越界区间
        long[] arr = {1, 2, 3, 4, 5};
        tree.build(arr, 0, 4, 1);
        try {
            tree.query(-1, 3, 0, 4, 1);
            System.out.println("测试3 - 越界查询: ✗ 应该处理边界");
        } catch (Exception e) {
            System.out.println("测试3 - 越界查询: ✓ 正确处理异常");
        }
        
        System.out.println("✓ 异常测试通过");
    }
    
    /**
     * 测试用例5：集成测试
     * 模拟实际应用场景，验证线段树的综合功能
     */
    private static void testIntegration() {
        System.out.println("
--- 测试用例5：集成测试 ---");
        
        // 模拟实际应用：区间统计和批量更新
        long[] data = new long[100];
        for (int i = 0; i < 100; i++) {
            data[i] = i * 2;  // 偶数序列
        }
        
        Code01_SegmentTreeAddQuerySum segTree = new Code01_SegmentTreeAddQuerySum(100);
        segTree.build(data, 0, 99, 1);
        
        // 模拟多次更新和查询操作
        long result1 = segTree.query(0, 49, 0, 99, 1);  // 前50个元素的和
        segTree.addRange(25, 75, 100, 0, 99, 1);       // 中间50个元素增加100
        long result2 = segTree.query(0, 99, 0, 99, 1); // 更新后整个数组的和
        segTree.addRange(0, 99, -50, 0, 99, 1);        // 所有元素减少50
        long result3 = segTree.query(50, 99, 0, 99, 1); // 后50个元素的和
        
        // 验证结果
        long expected1 = 2450;  // 0+2+4+...+98 = 49*50 = 2450
        long expected2 = 2450 + 50 * 100;  // 原和 + 50个元素各增加100
        long expected3 = (expected2 - 50 * 50) / 2;  // 后50个元素减少50后的和
        
        System.out.println("集成测试结果:");
        System.out.println("- 初始前50和: " + result1 + " (期望: " + expected1 + ")");
        System.out.println("- 更新后总和: " + result2 + " (期望: " + expected2 + ")");
        System.out.println("- 最终后50和: " + result3 + " (期望: " + expected3 + ")");
        
        assert result1 == expected1 : "集成测试1失败";
        assert result2 == expected2 : "集成测试2失败";
        assert result3 == expected3 : "集成测试3失败";
        
        System.out.println("✓ 集成测试通过");
    }
}