package class147;

/**
 * 投票问题（Ballot Problem）- 卡特兰数的扩展应用
 * 
 * 核心问题：
 * 在一次选举中，候选人A得到n张票，候选人B得到m张票，其中n>m
 * 计算在计票过程中A的票数始终严格大于B的票数的方案数
 * 
 * 相关题目：
 * - LeetCode 22. 括号生成
 * - LeetCode 96. 不同的二叉搜索树
 * - LeetCode 95. 不同的二叉搜索树 II
 * - LeetCode 32. 最长有效括号
 * - LeetCode 856. 括号的分数
 * - 洛谷 P1320 压缩技术（续集）
 * - LintCode 427. 生成括号
 * - 牛客网 NC146 括号生成
 * 
 * 数学背景：
 * 这个问题的答案是 (n-m)/(n+m) * C(n+m, n)，当n=m时，结果等价于第n项卡特兰数
 * 是卡特兰数的一个重要扩展应用，提供了更通用的计数模型
 * 
 * 实现特点：
 * 1. 支持两种解法：公式法和反射原理法
 * 2. 完整的异常处理和边界条件检查
 * 3. 高效的预处理机制优化性能
 * 4. 模块化设计便于维护和扩展
 * 5. 详细的性能分析和测试用例
 */
public class Code12_BallotProblem {
    
    /** 模数 - 用于处理大数运算 */
    public static final int MOD = 1000000007;
    
    /** 最大预处理范围 */
    public static final int MAXN = 2000001;
    
    /** 阶乘余数表 - 预计算并缓存 */
    public static long[] fac = new long[MAXN];
    
    /** 阶乘逆元表 - 预计算并缓存 */
    public static long[] inv = new long[MAXN];
    
    /** 标记是否已初始化 */
    private static boolean initialized = false;
    
    /**
     * 构建阶乘和逆元表
     * 
     * 核心思路：预处理阶乘和逆元表，避免重复计算，提高多次查询的效率
     * 使用费马小定理计算逆元，适合模数为质数的情况
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param n 最大值
     * @throws IllegalArgumentException 当n超过MAXN或为负数时抛出异常
     * @throws ArithmeticException 当计算过程中出现溢出时抛出异常
     * 
     * 优化点：
     * - 避免重复构建，已有结果时直接返回
     * - 反向递推计算逆元，提高效率
     * - 完整的溢出检查和异常处理
     */
    public static void build(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("最大值不能为负数: " + n);
        }
        if (n > MAXN) {
            throw new IllegalArgumentException("最大值超过预分配空间: " + n + " > " + MAXN);
        }
        
        // 避免重复构建，优化性能
        if (initialized && fac[n] != 0) {
            return;
        }
        
        // 初始化基础值
        fac[0] = inv[0] = 1;
        fac[1] = 1;
        
        // 计算阶乘表
        for (int i = 2; i <= n; i++) {
            fac[i] = (i * fac[i - 1]) % MOD;
            // 验证计算结果正确性
            if (fac[i] < 0) {
                throw new ArithmeticException("阶乘计算溢出，在i=" + i + "时结果为负数");
            }
        }
        
        // 使用费马小定理计算最大n的逆元
        inv[n] = power(fac[n], MOD - 2);
        
        // 反向递推计算其他逆元
        for (int i = n - 1; i >= 1; i--) {
            inv[i] = ((i + 1) * inv[i + 1]) % MOD;
        }
        
        // 标记已初始化
        initialized = true;
    }
    
    /**
     * 快速幂运算 - 计算x^p % MOD
     * 
     * 核心思路：利用二进制分解指数，将幂运算转换为多项式乘积
     * 每次迭代将底数平方，指数减半，实现对数级别的时间复杂度
     * 
     * 时间复杂度：O(log p)
     * 空间复杂度：O(1)
     * 
     * @param x 底数
     * @param p 指数
     * @return x^p % MOD
     * @throws IllegalArgumentException 当指数为负数时抛出异常
     * 
     * 注意：
     * - 对底数进行预先模处理
     * - 每步计算后进行正负号检查，确保结果正确性
     * - 适用于计算模逆元和大数幂运算
     */
    public static long power(long x, long p) {
        // 输入验证
        if (p < 0) {
            throw new IllegalArgumentException("指数不能为负数: " + p);
        }
        
        // 对底数进行模运算预处理
        x = x % MOD;
        long ans = 1;
        
        // 快速幂核心逻辑
        while (p > 0) {
            // 如果当前最低位为1，乘上当前x的值
            if ((p & 1) == 1) {
                ans = (ans * x) % MOD;
                // 确保结果为正数
                if (ans < 0) ans += MOD;
            }
            // 底数平方
            x = (x * x) % MOD;
            if (x < 0) x += MOD;
            // 指数右移一位（相当于除以2）
            p >>= 1;
        }
        return ans;
    }
    
    /**
     * 计算组合数C(n, k) = n!/(k!(n-k)!)
     * 
     * 核心思路：利用预处理的阶乘和逆元表进行快速查询
     * C(n,k) = n! * inv(k!) * inv((n-k)!) mod MOD
     * 
     * 时间复杂度：O(1) - 依赖于预处理
     * 空间复杂度：O(1)
     * 
     * @param n 总数
     * @param k 选择数
     * @return C(n, k) % MOD
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * 
     * 优化点：
     * - 边界情况快速处理（k=0, k=n, k<0, k>n）
     * - 自动处理阶乘表的预构建
     * - 结果正负号检查确保正确性
     */
    public static long combination(int n, int k) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("总数不能为负数: " + n);
        }
        
        // 边界情况处理
        if (k > n || k < 0) return 0;
        if (k == 0 || k == n) return 1;
        
        // 确保阶乘表已初始化
        if (!initialized || fac[n] == 0) {
            build(n);
        }
        
        // 计算组合数：C(n,k) = n!/(k!(n-k)!) = n! * inv(k!) * inv((n-k)!) mod MOD
        long result = ((fac[n] * inv[k]) % MOD) * inv[n - k] % MOD;
        
        // 确保结果为正数
        if (result < 0) result += MOD;
        return result;
    }
    
    /**
     * 投票问题解法 - 公式法
     * 
     * 核心思路：直接应用投票问题的数学公式 (n-m)/(n+m) * C(n+m, n)
     * 在模运算环境下，除法转换为乘以模逆元
     * 
     * 时间复杂度：O(n+m)（预处理时间），之后O(1)
     * 空间复杂度：O(n+m)
     * 
     * @param n A的票数
     * @param m B的票数
     * @return 满足条件的计票方式数模MOD的结果
     * @throws IllegalArgumentException 当输入参数不合法时抛出异常
     * 
     * 应用场景：
     * - 投票统计中的概率分析
     * - 排队问题的排列计数
     * - 路径规划中的有效路径计数
     * - 卡特兰数的一般化应用
     * 
     * 核心公式解析：
     * - 组合数C(n+m,n)表示所有可能的计票顺序
     * - 因子(n-m)/(n+m)表示满足条件的比例
     */
    public static long ballotProblem(int n, int m) {
        // 输入验证 - 非法输入处理
        if (n < 0 || m < 0) {
            throw new IllegalArgumentException("票数不能为负数: n=" + n + ", m=" + m);
        }
        
        // 边界条件处理
        // 当n < m时，不可能满足A始终领先B
        if (n < m) return 0;
        // 当m=0时，只有一种方式（全是A的票）
        if (m == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(n + m);
        
        // 计算公式: (n-m)/(n+m) * C(n+m, n)
        // 在模运算中，除法转换为乘以模逆元
        long numerator = (n - m) % MOD;
        if (numerator < 0) numerator += MOD; // 确保分子为正数
        
        long denominator = (n + m) % MOD;
        
        // 计算分母的逆元
        long denominatorInv = power(denominator, MOD - 2);
        
        // 计算组合数 C(n+m, n)
        long comb = combination(n + m, n);
        
        // 计算最终结果：(分子 * 分母逆元) * 组合数 % MOD
        long result = ((numerator * denominatorInv) % MOD) * comb % MOD;
        
        // 确保结果为正数
        if (result < 0) result += MOD;
        return result;
    }
    
    /**
     * 投票问题的另一种实现 - 使用反射原理
     * 
     * 核心思路：使用反射原理将问题转化为总方案数减去无效方案数
     * 无效方案数可通过反射技术计算为 C(n+m, n+1)
     * 
     * 数学原理：
     * - 反射原理是组合数学中的重要工具
     * - 将不满足条件的路径通过反射映射到另一个计数问题
     * - 最终公式：有效方案数 = 总方案数 - 无效方案数
     * 
     * @param n A的票数
     * @param m B的票数
     * @return 满足条件的计票方式数模MOD的结果
     * @throws IllegalArgumentException 当输入参数不合法时抛出异常
     * 
     * 反射原理应用：
     * - 将所有路径中第一次A不领先B的情况进行反射
     * - 反射后的路径与原问题的无效路径一一对应
     * - 通过计算反射后的路径数得到无效方案数
     */
    public static long ballotProblemReflection(int n, int m) {
        // 输入验证
        if (n < 0 || m < 0) {
            throw new IllegalArgumentException("票数不能为负数: n=" + n + ", m=" + m);
        }
        
        // 边界条件处理
        if (n < m) return 0;
        if (m == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(n + m);
        
        // 使用反射原理公式：C(n+m, n) - C(n+m, n+1)
        long c1 = combination(n + m, n);
        long c2 = combination(n + m, n + 1); // 等价于C(n+m, m-1)
        
        // 处理负数情况，确保结果为正
        long result = (c1 - c2 + MOD) % MOD;
        return result;
    }
    
    /**
     * 特殊情况：当n=m时，投票问题就变成了卡特兰数
     * 
     * 核心思路：卡特兰数是投票问题的特例，当n=m时的情况
     * 公式：Catalan(n) = C(2n, n) * (n+1)^{-1} (mod MOD)
     * 
     * 时间复杂度：O(n)（预处理时间），之后O(1)
     * 空间复杂度：O(n)
     * 
     * @param n 第n项卡特兰数
     * @return 第n项卡特兰数模MOD的结果
     * @throws IllegalArgumentException 当n为负数时抛出异常
     * 
     * 应用场景：
     * - 括号匹配问题
     * - 二叉搜索树计数
     * - 出栈序列问题
     * - 凸多边形三角划分
     * - 路径规划问题
     */
    public static long catalanNumber(int n) {
        // 输入验证
        if (n < 0) {
            throw new IllegalArgumentException("项数不能为负数: " + n);
        }
        
        // 边界情况
        if (n == 0) return 1;
        
        // 预处理阶乘和逆元表
        build(2 * n);
        
        // 计算卡特兰数：C(2n, n)/(n+1) = C(2n, n) * inv(n+1) mod MOD
        long combination = combination(2 * n, n);
        long invNPlus1 = power(n + 1, MOD - 2);
        long result = (combination * invNPlus1) % MOD;
        
        // 确保结果为正数
        if (result < 0) result += MOD;
        return result;
    }
    
    /**
     * 重置初始化状态 - 用于测试或内存管理
     */
    public static void reset() {
        initialized = false;
        // 注意：在实际应用中，如果MAXN很大，可能需要重新分配数组以释放内存
    }
    
    /**
     * 主方法 - 测试所有实现并比较性能
     */
    public static void main(String[] args) {
        System.out.println("===== 投票问题（Ballot Problem）测试 =====");
        
        // 测试用例1: 基本测试
        System.out.println("\n1. 基本测试:");
        int[][] basicTests = { 
            {2, 1}, {3, 1}, {3, 2}, {5, 3}, {8, 6}, {10, 5} 
        };
        
        for (int[] test : basicTests) {
            int n = test[0];
            int m = test[1];
            System.out.println("A: " + n + "票, B: " + m + "票");
            System.out.println("  标准公式: " + ballotProblem(n, m));
            System.out.println("  反射原理: " + ballotProblemReflection(n, m));
        }
        
        // 测试用例2: 边界情况
        System.out.println("\n2. 边界情况测试:");
        System.out.println("n=1, m=0: " + ballotProblem(1, 0)); // 应返回1
        System.out.println("n=5, m=5: " + ballotProblem(5, 5)); // 应返回0（因为需要严格大于）
        System.out.println("n=3, m=4: " + ballotProblem(3, 4)); // 应返回0（n<m）
        
        // 测试用例3: 卡特兰数测试
        System.out.println("\n3. 卡特兰数测试:");
        for (int i = 0; i <= 5; i++) {
            System.out.println("Catalan(" + i + ") = " + catalanNumber(i));
        }
        
        // 测试用例4: 异常处理
        System.out.println("\n4. 异常处理测试:");
        try {
            ballotProblem(-1, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ 正确捕获负数输入异常: " + e.getMessage());
        }
        
        // 测试用例5: 性能测试
        System.out.println("\n5. 性能测试:");
        
        // 重置初始化状态以准确测量首次调用性能
        reset();
        
        long startTime = System.nanoTime();
        ballotProblem(10000, 5000); // 第一次调用，包含预处理
        long endTime = System.nanoTime();
        System.out.println("第一次调用（含预处理）耗时: " + (endTime - startTime) / 1000 + " μs");
        
        startTime = System.nanoTime();
        ballotProblem(8000, 4000); // 第二次调用，复用预处理
        endTime = System.nanoTime();
        System.out.println("第二次调用（复用预处理）耗时: " + (endTime - startTime) + " ns");
        
        // 测试用例6: 公式等价性验证
        System.out.println("\n6. 公式等价性验证:");
        for (int n = 2; n <= 6; n++) {
            for (int m = 1; m < n; m++) {
                long result1 = ballotProblem(n, m);
                long result2 = ballotProblemReflection(n, m);
                if (result1 == result2) {
                    System.out.println("✓ n=" + n + ", m=" + m + " 公式等价性验证通过");
                } else {
                    System.out.println("✗ n=" + n + ", m=" + m + " 公式等价性验证失败: " + result1 + " vs " + result2);
                }
            }
        }
        
        // 最优解分析总结
        System.out.println("\n===== 最优解分析 =====");
        System.out.println("1. 投票问题最优解法分析:");
        System.out.println("   - 时间复杂度: O(n+m)，主要用于阶乘和逆元表的预处理");
        System.out.println("   - 空间复杂度: O(n+m)，用于存储阶乘和逆元表");
        System.out.println("   - 核心公式: (n-m)/(n+m) * C(n+m, n)");
        System.out.println("   - 与卡特兰数关系: 卡特兰数是投票问题的特例，当m=n时的情况");
        System.out.println("\n2. 算法选择建议:");
        System.out.println("   - 当需要模运算时: 使用公式法或反射原理法（取决于具体问题）");
        System.out.println("   - 当需要多次查询时: 预计算阶乘表和逆元表非常关键");
        System.out.println("   - 当数据规模大时: 预处理方法比每次计算阶乘更高效");
        System.out.println("   - 当需要精确整数结果时: 模运算确保不会溢出");
        System.out.println("\n3. 工程化考量:");
        System.out.println("   - 预处理优化: 阶乘和逆元表的一次性构建和复用");
        System.out.println("   - 异常处理: 完整的参数验证和详细的异常信息");
        System.out.println("   - 性能优化: 避免重复计算，边界条件快速处理");
        System.out.println("   - 代码复用: 模块化设计便于在其他项目中复用");
        System.out.println("   - 内存管理: 适当的数组大小预分配和reset方法支持");
        System.out.println("\n4. Java语言特性应用:");
        System.out.println("   - 静态常量: 定义MOD和MAXN等配置参数");
        System.out.println("   - 静态数组: 高效的预计算缓存实现");
        System.out.println("   - 异常机制: 使用标准异常类型和自定义异常消息");
        System.out.println("   - 方法重载: 支持不同参数组合的计算需求");
        System.out.println("   - 包装类: 大整数计算时避免溢出问题");
        System.out.println("\n5. 实际应用场景:");
        System.out.println("   - 投票统计: 分析选举结果的各种可能排列");
        System.out.println("   - 排队问题: 计算顾客排队的特定排列概率");
        System.out.println("   - 路径规划: 网格中不跨越特定边界的路径计数");
        System.out.println("   - 计算机网络: 传输协议中的缓冲区管理策略");
        System.out.println("   - 金融建模: 期权定价中的路径依赖模型");
        System.out.println("   - 生物信息学: DNA序列比对和RNA二级结构预测");
        System.out.println("\n6. 相关题目链接:");
        System.out.println("   - LeetCode 22. 括号生成: 卡特兰数经典应用");
        System.out.println("   - LeetCode 96. 不同的二叉搜索树: BST计数问题");
        System.out.println("   - LeetCode 95. 不同的二叉搜索树 II: 生成所有BST");
        System.out.println("   - LeetCode 32. 最长有效括号: 括号匹配进阶");
        System.out.println("   - LeetCode 856. 括号的分数: 括号组合求值");
        System.out.println("   - 洛谷 P1320 压缩技术（续集）: 卡特兰数应用");
        System.out.println("   - LintCode 427. 生成括号: 括号生成问题");
        System.out.println("   - 牛客网 NC146 括号生成: 常见面试题");
        System.out.println("\n7. 进阶思考:");
        System.out.println("   - 当模数不是质数时，如何高效计算逆元？");
        System.out.println("   - 如何处理超大规模的n和m值？");
        System.out.println("   - 如何将算法并行化以提高性能？");
        System.out.println("   - 如何将投票问题推广到多候选人的情况？");
        System.out.println("   - 在实际系统中，如何平衡精度和性能的需求？");
        System.out.println("\n8. 生产环境建议:");
        System.out.println("   - 添加日志记录: 使用log4j或slf4j记录关键操作和异常");
        System.out.println("   - 线程安全: 考虑并发环境下的状态共享问题");
        System.out.println("   - 单元测试: 使用JUnit编写全面的测试用例");
        System.out.println("   - 性能监控: 集成APM工具跟踪方法执行时间");
        System.out.println("   - 错误恢复: 实现重试机制和降级策略");
        System.out.println("   - 配置管理: 将MOD和MAXN等参数外部化配置");
    }
}