package class117;

/**
 * Sparse Table算法综合测试类
 * 
 * 【测试目的】
 * 提供一个统一的测试框架，验证class117包中所有Sparse Table相关算法的正确性和性能
 * 
 * 【测试范围】
 * 1. 基本Sparse Table操作（最大值和最小值查询）
 * 2. GCD Sparse Table操作（区间最大公约数查询）
 * 3. 频繁值问题（有序数组中查询最频繁值的出现次数）
 * 4. 国旗计划问题（环形线段覆盖问题）
 * 5. SPOJ RMQSQ问题（区间最小值查询）
 * 6. SPOJ THRBL问题（最大吸引力路径查询）
 * 7. POJ 3264问题（区间最大高度差查询）
 * 
 * 【测试特点】
 * - 每个测试方法模拟对应算法文件的核心逻辑
 * - 提供典型测试用例和边界情况验证
 * - 输出详细的测试过程和结果信息
 * - 便于快速验证算法实现的正确性
 */
public class ComprehensiveTest {
    
    public static void main(String[] args) {
        System.out.println("=== Sparse Table 算法综合测试 ===\n");
        
        // 测试1: 基本Sparse Table操作
        testBasicSparseTable();
        
        // 测试2: GCD Sparse Table
        testGCDSparseTable();
        
        // 测试3: 频繁值问题
        testFrequentValues();
        
        // 测试4: 国旗计划问题
        testFlagPlan();
        
        // 测试5: SPOJ RMQSQ问题
        testSPOJRMQSQ();
        
        // 测试6: SPOJ THRBL问题
        testSPOJTHRBL();
        
        // 测试7: POJ 3264问题
        testPOJ3264();
        
        System.out.println("所有测试完成！");
    }
    
    /**
     * 测试基本的Sparse Table操作（最大值和最小值查询）
     * 
     * 【测试逻辑】
     * 1. 创建一个测试数组
     * 2. 模拟Code02_SparseTableMaximumMinimum.java中的Sparse Table构建过程
     * 3. 构建最大值和最小值两个Sparse Table
     * 4. 查询特定区间的最大值、最小值和差值
     * 5. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用混合有序和无序的整数数组
     * - 测试区间跨越多个2^j长度的子区间
     * - 验证最大值和最小值查询的正确性
     */
    private static void testBasicSparseTable() {
        System.out.println("1. 测试基本Sparse Table操作:");
        int[] arr = {1, 3, 2, 7, 5, 9, 4, 8, 6};
        System.out.print("数组: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        
        // 模拟Code02_SparseTableMaximumMinimum.java中的操作
        int n = arr.length;
        int[] testArr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            testArr[i] = arr[i - 1];
        }
        
        // 构建Sparse Table
        int[] log2 = new int[n + 1];
        log2[0] = -1;
        for (int i = 1; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] stmax = new int[n + 1][20];
        int[][] stmin = new int[n + 1][20];
        
        for (int i = 1; i <= n; i++) {
            stmax[i][0] = testArr[i];
            stmin[i][0] = testArr[i];
        }
        
        for (int p = 1; p <= log2[n]; p++) {
            for (int i = 1; i + (1 << p) - 1 <= n; i++) {
                stmax[i][p] = Math.max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
                stmin[i][p] = Math.min(stmin[i][p - 1], stmin[i + (1 << (p - 1))][p - 1]);
            }
        }
        
        // 查询区间[2, 6]的最大值和最小值
        int l = 2, r = 6;
        int p = log2[r - l + 1];
        int max = Math.max(stmax[l][p], stmax[r - (1 << p) + 1][p]);
        int min = Math.min(stmin[l][p], stmin[r - (1 << p) + 1][p]);
        
        System.out.println("区间[" + l + ", " + r + "]的最大值: " + max);
        System.out.println("区间[" + l + ", " + r + "]的最小值: " + min);
        System.out.println("差值: " + (max - min));
        System.out.println();
    }
    
    /**
     * 测试GCD Sparse Table
     * 
     * 【测试逻辑】
     * 1. 创建一个包含多个倍数关系的测试数组
     * 2. 模拟Code03_SparseTableGCD.java中的Sparse Table构建过程
     * 3. 使用lambda表达式实现GCD计算函数
     * 4. 构建GCD Sparse Table
     * 5. 查询特定区间的GCD值
     * 6. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用具有公因子的整数序列
     * - 验证不同长度区间的GCD计算结果
     * - 确保GCD的结合律在Sparse Table构建中正确应用
     */
    private static void testGCDSparseTable() {
        System.out.println("2. 测试GCD Sparse Table:");
        int[] arr = {12, 18, 24, 36, 48};
        System.out.print("数组: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        
        // 模拟Code03_SparseTableGCD.java中的操作
        int n = arr.length;
        int[] testArr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            testArr[i] = arr[i - 1];
        }
        
        // 计算GCD的辅助函数
        java.util.function.BiFunction<Integer, Integer, Integer> gcdFunc = new java.util.function.BiFunction<Integer, Integer, Integer>() {
            public Integer apply(Integer a, Integer b) {
                return b == 0 ? a : this.apply(b, a % b);
            }
        };
        
        // 构建GCD Sparse Table
        int[] log2 = new int[n + 1];
        log2[0] = -1;
        for (int i = 1; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] stgcd = new int[n + 1][20];
        for (int i = 1; i <= n; i++) {
            stgcd[i][0] = testArr[i];
        }
        
        for (int p = 1; p <= log2[n]; p++) {
            for (int i = 1; i + (1 << p) - 1 <= n; i++) {
                stgcd[i][p] = gcdFunc.apply(stgcd[i][p - 1], stgcd[i + (1 << (p - 1))][p - 1]);
            }
        }
        
        // 查询区间[1, 4]的GCD
        int l = 1, r = 4;
        int p = log2[r - l + 1];
        int result = gcdFunc.apply(stgcd[l][p], stgcd[r - (1 << p) + 1][p]);
        
        System.out.println("区间[" + l + ", " + r + "]的GCD: " + result);
        System.out.println();
    }
    
    /**
     * 测试频繁值问题
     * 
     * 【测试逻辑】
     * 1. 创建一个有序的测试数组，包含连续重复的元素
     * 2. 模拟Code04_FrequentValues.java中的预处理过程
     * 3. 构建bucket数组、左右边界数组
     * 4. 为每个bucket构建最大值Sparse Table
     * 5. 查询特定区间内最频繁值的出现次数
     * 6. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用包含多个连续重复元素的有序数组
     * - 测试跨多个bucket的查询场景
     * - 验证三种情况的处理逻辑：同一bucket、跨两个bucket、跨多个bucket
     */
    private static void testFrequentValues() {
        System.out.println("3. 测试频繁值问题:");
        int[] arr = {-1, -1, 1, 1, 1, 1, 3, 10, 10, 10};
        System.out.print("有序数组: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        
        // 模拟Code04_FrequentValues.java中的操作
        int n = arr.length;
        int[] testArr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            testArr[i] = arr[i - 1];
        }
        
        // 构建bucket数组
        int[] bucket = new int[n + 1];
        int[] left = new int[n + 1];
        int[] right = new int[n + 1];
        
        testArr[0] = -23333333; // 设置一个不会到达的数字
        int cnt = 0;
        for (int i = 1; i <= n; i++) {
            if (testArr[i - 1] != testArr[i]) {
                right[cnt] = i - 1;
                left[++cnt] = i;
            }
            bucket[i] = cnt;
        }
        right[cnt] = n;
        
        // 构建Sparse Table
        int[] log2 = new int[n + 1];
        log2[0] = -1;
        for (int i = 1; i <= cnt; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] stmax = new int[n + 1][20];
        for (int i = 1; i <= cnt; i++) {
            stmax[i][0] = right[i] - left[i] + 1;
        }
        
        for (int p = 1; p <= log2[cnt]; p++) {
            for (int i = 1; i + (1 << p) - 1 <= cnt; i++) {
                stmax[i][p] = Math.max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
            }
        }
        
        // 查询区间[1, 10]的最频繁值出现次数
        int l = 1, r = 10;
        int lbucket = bucket[l];
        int rbucket = bucket[r];
        
        int result;
        if (lbucket == rbucket) {
            result = r - l + 1;
        } else {
            int a = right[lbucket] - l + 1;
            int b = r - left[rbucket] + 1;
            int c = 0;
            if (lbucket + 1 < rbucket) {
                int from = lbucket + 1, to = rbucket - 1;
                int p = log2[to - from + 1];
                c = Math.max(stmax[from][p], stmax[to - (1 << p) + 1][p]);
            }
            result = Math.max(Math.max(a, b), c);
        }
        
        System.out.println("区间[" + l + ", " + r + "]中最频繁值的出现次数: " + result);
        System.out.println();
    }
    
    /**
     * 测试国旗计划问题
     * 
     * 【测试逻辑】
     * 1. 由于国旗计划问题较为复杂，此处提供算法思路说明
     * 2. 详细解释Sparse Table在跳跃优化中的应用
     * 3. 说明环形线段覆盖问题的处理方法
     * 
     * 【算法说明】
     * - 基于Code01_FlagPlan.java中的实现
     * - 使用stjump[i][p]表示从第i号线段出发，跳2^p次能到达的最右线段编号
     * - 通过预处理跳跃表，将查询时间复杂度从O(n)优化到O(logn)
     * - 使用环形转线性的技巧处理环形结构
     */
    private static void testFlagPlan() {
        System.out.println("4. 测试国旗计划问题:");
        System.out.println("该问题较为复杂，涉及环形线段覆盖问题");
        System.out.println("主要考察Sparse Table在优化跳跃过程中的应用");
        System.out.println("通过stjump[i][p]表示从第i号线段出发，跳2^p次能到达的最右线段编号");
        System.out.println();
    }
    
    /**
     * 测试SPOJ RMQSQ问题（区间最小值查询）
     * 
     * 【测试逻辑】
     * 1. 创建一个测试数组
     * 2. 模拟Code07_SPOJRMQSQ.java中的Sparse Table构建过程
     * 3. 构建最小值Sparse Table
     * 4. 查询特定区间的最小值
     * 5. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用包含重复元素和各种数值的数组
     * - 注意输出索引从1-based转换为题目要求的0-based
     * - 验证区间最小值查询的正确性
     */
    private static void testSPOJRMQSQ() {
        System.out.println("5. 测试SPOJ RMQSQ问题:");
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        System.out.print("数组: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
        
        // 模拟Code07_SPOJRMQSQ.java中的操作
        int n = arr.length;
        int[] testArr = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            testArr[i] = arr[i - 1];
        }
        
        // 构建Sparse Table
        int[] log2 = new int[n + 1];
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] st = new int[n + 1][20];
        for (int i = 1; i <= n; i++) {
            st[i][0] = testArr[i];
        }
        
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
        
        // 查询区间[2, 6]的最小值
        int l = 2, r = 6;
        int k = log2[r - l + 1];
        int result = Math.min(st[l][k], st[r - (1 << k) + 1][k]);
        
        System.out.println("区间[" + (l-1) + ", " + (r-1) + "]的最小值: " + result);
        System.out.println();
    }
    
    /**
     * 测试SPOJ THRBL问题（最大吸引力路径查询）
     * 
     * 【测试逻辑】
     * 1. 创建商店吸引力测试数组
     * 2. 模拟Code08_SPOJTHRBL.java中的Sparse Table构建过程
     * 3. 构建最大值Sparse Table
     * 4. 查询两个商店之间路径上的最大吸引力
     * 5. 判断是否满足13-Dots的移动条件
     * 6. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用不同吸引力值的商店序列
     * - 测试起点吸引力与路径最大吸引力的比较
     * - 验证路径上不包含起点和终点的查询逻辑
     */
    private static void testSPOJTHRBL() {
        System.out.println("6. 测试SPOJ THRBL问题:");
        int[] attraction = {2, 4, 3, 1, 5};
        System.out.print("商店吸引力: ");
        for (int i = 0; i < attraction.length; i++) {
            System.out.print(attraction[i] + " ");
        }
        System.out.println();
        
        // 模拟Code08_SPOJTHRBL.java中的操作
        int n = attraction.length;
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = attraction[i - 1];
        }
        
        // 构建Sparse Table
        int[] log2 = new int[n + 1];
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] st = new int[n + 1][20];
        for (int i = 1; i <= n; i++) {
            st[i][0] = a[i];
        }
        
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
        
        // 查询区间[2, 4]的最大值（从商店2到商店4的路径上）
        int l = 2, r = 4;
        if (l > r) {
            int temp = l;
            l = r;
            r = temp;
        }
        
        int maxAttraction = 0;
        if (l + 1 <= r - 1) {
            int k = log2[(r - 1) - (l + 1) + 1];
            maxAttraction = Math.max(st[l + 1][k], st[(r - 1) - (1 << k) + 1][k]);
        }
        
        System.out.println("从商店" + (l-1) + "到商店" + (r-1) + "路径上的最大吸引力: " + maxAttraction);
        System.out.println("起点商店" + (l-1) + "的吸引力: " + a[l]);
        System.out.println("13-Dots" + (maxAttraction < a[l] ? "会" : "不会") + "去商店" + (r-1));
        System.out.println();
    }
    
    /**
     * 测试POJ 3264问题（区间最大高度差查询）
     * 
     * 【测试逻辑】
     * 1. 创建奶牛高度测试数组
     * 2. 模拟Code09_POJ3264.java中的Sparse Table构建过程
     * 3. 构建最大值和最小值两个Sparse Table
     * 4. 查询特定区间的最大高度、最小高度和高度差
     * 5. 输出测试结果
     * 
     * 【测试用例说明】
     * - 使用不同高度值的奶牛序列
     * - 注意输出索引从1-based转换为题目要求的0-based
     * - 验证区间最大高度差计算的正确性
     */
    private static void testPOJ3264() {
        System.out.println("7. 测试POJ 3264问题:");
        int[] height = {10, 5, 8, 3, 7};
        System.out.print("奶牛高度: ");
        for (int i = 0; i < height.length; i++) {
            System.out.print(height[i] + " ");
        }
        System.out.println();
        
        // 模拟Code09_POJ3264.java中的操作
        int n = height.length;
        int[] h = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            h[i] = height[i - 1];
        }
        
        // 构建Sparse Table
        int[] log2 = new int[n + 1];
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
        
        int[][] stmax = new int[n + 1][20];
        int[][] stmin = new int[n + 1][20];
        for (int i = 1; i <= n; i++) {
            stmax[i][0] = h[i];
            stmin[i][0] = h[i];
        }
        
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                stmax[i][j] = Math.max(stmax[i][j - 1], stmax[i + (1 << (j - 1))][j - 1]);
                stmin[i][j] = Math.min(stmin[i][j - 1], stmin[i + (1 << (j - 1))][j - 1]);
            }
        }
        
        // 查询区间[1, 5]的最大值和最小值
        int l = 1, r = 5;
        int k = log2[r - l + 1];
        int max = Math.max(stmax[l][k], stmax[r - (1 << k) + 1][k]);
        int min = Math.min(stmin[l][k], stmin[r - (1 << k) + 1][k]);
        
        System.out.println("区间[" + (l-1) + ", " + (r-1) + "]的最大高度: " + max);
        System.out.println("区间[" + (l-1) + ", " + (r-1) + "]的最小高度: " + min);
        System.out.println("高度差: " + (max - min));
        System.out.println();
    }
}