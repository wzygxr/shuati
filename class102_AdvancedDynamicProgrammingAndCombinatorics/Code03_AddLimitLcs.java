package class128;

import java.util.Arrays;

/**
 * 增加限制的最长公共子序列问题
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，s1长度为n，s2长度为m
 * 返回s1和s2的最长公共子序列长度
 * 
 * 约束条件：
 * - 两个字符串都只由小写字母组成
 * - 1 <= n <= 10^6
 * - 1 <= m <= 10^3
 * 
 * 优化背景：
 * 标准的LCS算法时间复杂度为O(n*m)，当n达到10^6而m为10^3时，
 * 直接使用标准算法会导致大约10^9次操作，显然不可行。
 * 因此需要利用题目中的限制条件进行优化。
 * 
 * 优化思路：
 * 1. 观察到s2的长度m远小于s1的长度n
 * 2. 预处理s1字符串，记录每个位置之后每个字符首次出现的位置
 * 3. 定义新的DP状态：f(i,j)表示s2的前i个字符要形成长度为j的公共子序列
 *    所需的s1的最短前缀长度
 * 4. 通过状态转移找到最大的j，使得存在i <= m且f(i,j) <= n
 * 
 * 时间复杂度分析：
 * - 预处理s1：O(n * 26) = O(n)，因为每个字符需要26个小写字母的处理
 * - DP状态数：O(m^2)，因为i和j的范围都是0到m
 * - 总时间复杂度：O(n + m^2)
 * 
 * 空间复杂度分析：
 * - next数组：O(n * 26) = O(n)
 * - dp数组：O(m^2)
 * - 总空间复杂度：O(n + m^2)
 * 
 * 输入输出示例：
 * 输入：
 * s1 = "abcde"
 * s2 = "ace"
 * 输出：3
 * 解释：最长公共子序列是"ace"，长度为3
 */
public class Code03_AddLimitLcs {
    // 常量定义
    private static final int NA = Integer.MAX_VALUE; // 表示不可行的情况
    private static final int ALPHABET_SIZE = 26;    // 字母表大小
    
    // 实例变量，提高线程安全性
    private char[] s1;          // s1的字符数组
    private char[] s2;          // s2的字符数组
    private int n, m;           // s1和s2的长度
    private int[][] next;       // next[i][c]表示s1中位置i之后字符c首次出现的位置
    private int[][] dp;         // 动态规划表
    
    /**
     * 主函数 - 用于测试两种算法的正确性和性能
     */
    public static void main(String[] args) {
        // 验证正确性
        verifyCorrectness();
        
        // 性能测试
        benchmarkPerformance();
    }
    
    /**
     * 验证算法正确性
     */
    private static void verifyCorrectness() {
        System.out.println("功能测试开始");
        int n = 100;
        int m = 100;
        int testTime = 10000;
        
        // 创建实例，避免使用静态成员变量
        Code03_AddLimitLcs solution = new Code03_AddLimitLcs();
        
        for (int i = 0; i < testTime; i++) {
            int size1 = (int) (Math.random() * n) + 1;
            int size2 = (int) (Math.random() * m) + 1;
            String str1 = randomString(size1);
            String str2 = randomString(size2);
            int ans1 = solution.lcsClassic(str1, str2);
            int ans2 = solution.lcsOptimized(str1, str2);
            
            if (ans1 != ans2) {
                System.out.println("出错了!");
                System.out.println("str1: " + str1);
                System.out.println("str2: " + str2);
                System.out.println("经典算法结果: " + ans1);
                System.out.println("优化算法结果: " + ans2);
                return;
            }
        }
        System.out.println("功能测试通过");
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    private static void benchmarkPerformance() {
        System.out.println("性能测试开始");
        int n = 100000; // 注意：实际测试时可以调整为1000000
        int m = 1000;
        System.out.println("n = " + n);
        System.out.println("m = " + m);
        
        // 创建实例
        Code03_AddLimitLcs solution = new Code03_AddLimitLcs();
        
        String str1 = randomString(n);
        String str2 = randomString(m);
        
        // 只在小数据上测试经典算法
        if (n <= 10000) {
            long start = System.currentTimeMillis();
            int ans1 = solution.lcsClassic(str1, str2);
            long end = System.currentTimeMillis();
            System.out.println("经典算法运行时间 : " + (end - start) + " 毫秒");
            System.out.println("经典算法结果: " + ans1);
        } else {
            System.out.println("经典算法对于大n会超时，跳过测试");
        }
        
        // 测试优化算法
        long start = System.currentTimeMillis();
        int ans2 = solution.lcsOptimized(str1, str2);
        long end = System.currentTimeMillis();
        System.out.println("优化算法运行时间 : " + (end - start) + " 毫秒");
        System.out.println("优化算法结果: " + ans2);
        System.out.println("性能测试结束");
    }

    /**
     * 随机生成指定长度的小写字母字符串
     * 
     * @param n 字符串长度
     * @return 生成的随机字符串
     */
    private static String randomString(int n) {
        if (n <= 0) {
            return "";
        }
        
        char[] ans = new char[n];
        for (int i = 0; i < n; i++) {
            ans[i] = (char) ((int) (Math.random() * ALPHABET_SIZE) + 'a');
        }
        return new String(ans);
    }

    /**
     * 经典动态规划版本的最长公共子序列算法
     * 时间复杂度：O(n*m)，不适用于n很大的情况
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子序列的长度
     * @throws IllegalArgumentException 如果输入字符串为null
     */
    public int lcsClassic(String str1, String str2) {
        validateInputs(str1, str2);
        
        this.s1 = str1.toCharArray();
        this.s2 = str2.toCharArray();
        this.n = s1.length;
        this.m = s2.length;
        
        // 边界情况优化
        if (n == 0 || m == 0) {
            return 0;
        }
        
        // 空间优化：只使用两行dp数组
        int[][] dp = new int[2][m + 1];
        
        for (int i = 1; i <= n; i++) {
            int row = i % 2;
            int prevRow = (i - 1) % 2;
            
            for (int j = 1; j <= m; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[row][j] = 1 + dp[prevRow][j - 1];
                } else {
                    dp[row][j] = Math.max(dp[prevRow][j], dp[row][j - 1]);
                }
            }
        }
        
        return dp[n % 2][m];
    }

    /**
     * 优化版本的LCS算法主函数
     * 利用s2较短的特点进行优化
     * 时间复杂度：O(n + m^2)
     * 
     * @param str1 第一个字符串（可能很长）
     * @param str2 第二个字符串（相对较短）
     * @return 最长公共子序列的长度
     * @throws IllegalArgumentException 如果输入字符串为null
     */
    public int lcsOptimized(String str1, String str2) {
        validateInputs(str1, str2);
        
        this.s1 = str1.toCharArray();
        this.s2 = str2.toCharArray();
        this.n = s1.length;
        this.m = s2.length;
        
        // 边界情况处理
        if (n == 0 || m == 0) {
            return 0;
        }
        
        // 内存优化：动态分配next数组大小，避免静态大数组占用内存
        this.next = new int[n + 1][ALPHABET_SIZE];
        this.dp = new int[m + 1][m + 1];
        
        // 构建预处理数据结构
        build();
        
        // 寻找最大的j，使得f(m, j) <= n
        int ans = 0;
        for (int j = m; j >= 1; j--) {
            if (f(m, j) != NA) {
                ans = j;
                break;
            }
        }
        
        return ans;
    }

    /**
     * 验证输入字符串的有效性
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @throws IllegalArgumentException 如果输入字符串为null
     */
    private void validateInputs(String str1, String str2) {
        if (str1 == null || str2 == null) {
            throw new IllegalArgumentException("输入字符串不能为null");
        }
    }

    /**
     * 构建预处理数据结构
     * 1. next数组：next[i][c]表示s1中位置i之后字符c首次出现的位置
     * 2. 初始化dp数组为-1（表示未计算）
     */
    private void build() {
        // 初始化right数组，记录每个字符最右边出现的位置
        int[] right = new int[ALPHABET_SIZE];
        Arrays.fill(right, NA);
        
        // 从右向左遍历s1，构建next数组
        for (int i = n; i >= 0; i--) {
            // 复制当前的right数组到next[i]
            System.arraycopy(right, 0, next[i], 0, ALPHABET_SIZE);
            
            // 更新right数组，如果i > 0
            if (i > 0) {
                // s1的i长度，对应的字符是s1[i-1]
                right[s1[i - 1] - 'a'] = i;
            }
        }
        
        // 初始化dp数组为-1
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], -1);
        }
    }

    /**
     * 核心动态规划函数
     * 定义：f(i,j)表示用s2的前i个字符形成长度为j的公共子序列
     *      所需的s1的最短前缀长度
     * 
     * @param i s2前缀的长度
     * @param j 目标公共子序列的长度
     * @return 所需的s1最短前缀长度，如果不可行返回NA
     */
    private int f(int i, int j) {
        // 基本情况：
        // 1. 如果i < j，不可能形成长度为j的公共子序列（因为s2只有i个字符）
        if (i < j) {
            return NA;
        }
        // 2. 如果j == 0，不需要任何s1字符
        if (j == 0) {
            return 0;
        }
        // 3. 如果已经计算过，直接返回
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        
        // 策略1：不使用s2的第i个字符（即s2[i-1]）
        // 此时结果为f(i-1,j)
        int ans = f(i - 1, j);
        
        // 策略2：使用s2的第i个字符（即s2[i-1]）
        // 我们需要先找到用s2的前i-1个字符形成长度为j-1的公共子序列所需的最短s1前缀长度pre
        // 然后在s1的pre位置之后找到第一个等于s2[i-1]的字符的位置
        int pre = f(i - 1, j - 1);
        if (pre != NA && pre <= n) { // 添加pre <= n的检查，确保pre在有效范围内
            int charIndex = s2[i - 1] - 'a';
            if (next[pre][charIndex] != NA) {
                ans = Math.min(ans, next[pre][charIndex]);
            }
        }
        
        // 记忆结果并返回
        dp[i][j] = ans;
        return ans;
    }
    
    /**
     * 获取算法的最大空间复杂度估计（字节）
     * 用于性能监控和内存使用分析
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 预估的内存使用量（字节）
     */
    public long estimateMemoryUsage(String str1, String str2) {
        validateInputs(str1, str2);
        
        int n = str1.length();
        int m = str2.length();
        
        // next数组的内存使用：(n+1) * 26 * 4字节（假设int为4字节）
        long nextMemory = (long)(n + 1) * 26 * 4;
        
        // dp数组的内存使用：(m+1) * (m+1) * 4字节
        long dpMemory = (long)(m + 1) * (m + 1) * 4;
        
        // 字符数组的内存使用
        long charArraysMemory = (n + m) * 2; // char为2字节
        
        return nextMemory + dpMemory + charArraysMemory;
    }

	/**
	 * Java工程化实战建议：
	 * 
	 * 1. 内存管理优化：
	 *    - 对于next数组，当n很大时可能占用较多内存
	 *    - 可以考虑使用更紧凑的数据结构或按需构建
	 *    - 对于多次调用，可以考虑复用部分数据结构
	 * 
	 * 2. 并发安全性：
	 *    - 当前实现使用了静态成员变量，不是线程安全的
	 *    - 在多线程环境下，应该将这些变量作为方法内的局部变量或实例变量
	 *    - 或者使用ThreadLocal来保证线程安全
	 * 
	 * 3. 异常处理：
	 *    - 应该添加输入验证，确保输入字符串不为null
	 *    - 对于极端情况（如空字符串）需要特殊处理
	 * 
	 * 4. 性能调优：
	 *    - 对于非常大的n，可以考虑分批处理s1字符串
	 *    - 使用预分配内存避免动态扩容开销
	 *    - 对于频繁调用的场景，可以缓存预处理结果
	 * 
	 * 5. 代码风格优化：
	 *    - 将常量和配置参数外部化
	 *    - 考虑使用对象封装而不是静态方法
	 *    - 为复杂的算法步骤添加更详细的注释
	 */
	
	/**
	 * 算法优化的核心思想：
	 * 
	 * 1. 问题转化：
	 *    - 传统LCS问题关注长度，这里我们转化为关注所需的s1前缀长度
	 *    - 这种转化使我们能够利用s2长度较小的特点
	 * 
	 * 2. 预处理技巧：
	 *    - next数组预处理让我们能在O(1)时间内找到字符在s1中特定位置之后的下一次出现
	 *    - 这避免了在每次查找时遍历s1
	 * 
	 * 3. 动态规划状态设计：
	 *    - f(i,j)的定义非常巧妙，专注于s2的前缀和目标长度
	 *    - 这种设计将时间复杂度从O(n*m)降低到O(m^2)
	 * 
	 * 4. 边界条件处理：
	 *    - 正确处理i < j和j = 0的情况
	 *    - 使用NA（无穷大）表示不可行的状态
	 * 
	 * 5. 贪心选择：
	 *    - 在状态转移中，我们总是选择所需s1前缀最短的方案
	 *    - 这确保了后续状态有更多的选择空间
	 */
}
