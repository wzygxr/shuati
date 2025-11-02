package class130;

import java.util.Arrays;
import java.util.List;

// 最小移动总距离
// 所有工厂和机器人都分布在x轴上
// 给定长度为n的二维数组factory，factory[i][0]为i号工厂的位置，factory[i][1]为容量
// 给定长度为m的一维数组robot，robot[j]为第j个机器人的位置
// 每个工厂所在的位置都不同，每个机器人所在的位置都不同，机器人到工厂的距离为位置差的绝对值
// 所有机器人都是坏的，但是机器人可以去往任何工厂进行修理，但是不能超过某个工厂的容量
// 测试数据保证所有机器人都可以被维修，返回所有机器人移动的最小总距离
// 1 <= n、m <= 100
// -10^9 <= factory[i][0]、robot[j] <= +10^9
// 0 <= factory[i][1] <= m
// 测试链接 : https://leetcode.cn/problems/minimum-total-distance-traveled/

public class Code05_MinimumTotalDistanceTraveled {

    // 表示不可达状态的常量
    public static long NA = Long.MAX_VALUE;

    // 最大工厂数常量
    public static int MAXN = 101;

    // 最大机器人数常量
    public static int MAXM = 101;

    // 输入参数
    public static int n, m;

    // 工厂信息数组，下标从1开始
    // fac[i][0]表示第i号工厂的位置
    // fac[i][1]表示第i号工厂的容量
    public static int[][] fac = new int[MAXN][2];

    // 机器人位置数组，下标从1开始
    // rob[i]表示第i号机器人的位置
    public static int[] rob = new int[MAXM];

    // dp数组，dp[i][j]表示前i个工厂修理前j个机器人的最小总距离
    public static long[][] dp = new long[MAXN][MAXM];

    // 前缀和数组，sum[j]表示前j个机器人去往当前工厂的距离之和
    public static long[] sum = new long[MAXM];

    // 单调队列，用于维护滑动窗口内的最优决策点
    // 存储的是机器人下标，按照value值单调递增排列
    public static int[] queue = new int[MAXM];

    // 队列的左右指针
    public static int l, r;

    /**
     * 初始化函数，对输入数据进行预处理
     * 
     * @param factory 工厂信息数组
     * @param robot 机器人位置列表
     */
    public static void build(int[][] factory, List<Integer> robot) {
        // 工厂和机器人都根据所在位置排序
        // 这样可以确保相邻的工厂和机器人在空间上也是相邻的
        Arrays.sort(factory, (a, b) -> a[0] - b[0]);
        robot.sort((a, b) -> a - b);
        n = factory.length;
        m = robot.size();
        
        // 让工厂和机器人的下标都从1开始，便于处理边界情况
        for (int i = 1; i <= n; i++) {
            fac[i][0] = factory[i - 1][0];  // 工厂位置
            fac[i][1] = factory[i - 1][1];  // 工厂容量
        }
        for (int i = 1; i <= m; i++) {
            rob[i] = robot.get(i - 1);      // 机器人位置
        }
        
        // dp初始化
        // dp[0][j]表示0个工厂修理j个机器人，这是不可能的，所以设为不可达
        for (int j = 1; j <= m; j++) {
            dp[0][j] = NA;
        }
    }

    /**
     * 计算所有机器人移动的最小总距离
     * 使用单调队列优化的动态规划解法
     * 时间复杂度：O(n * m)，每个状态最多入队和出队一次
     * 空间复杂度：O(n * m)，dp数组和单调队列的空间
     * 
     * @param robot 机器人位置列表
     * @param factory 工厂信息数组
     * @return 所有机器人移动的最小总距离
     */
    // 最优解O(n * m)
    // 其他题解都没有达到这个最优复杂度
    public static long minimumTotalDistance(List<Integer> robot, int[][] factory) {
        // 数据预处理
        build(factory, robot);
        
        // 动态规划过程
        for (int i = 1, cap; i <= n; i++) {
            // 获取第i号工厂的容量
            cap = fac[i][1];
            
            // 计算前缀和数组
            // sum[j]表示前j个机器人去往第i号工厂的距离之和
            for (int j = 1; j <= m; j++) {
                sum[j] = sum[j - 1] + dist(i, j);
            }
            
            // 初始化队列指针
            l = r = 0;
            
            // 计算前i个工厂修理前j个机器人的最小总距离
            for (int j = 1; j <= m; j++) {
                // 不选择第i号工厂的情况：继承前i-1个工厂的结果
                dp[i][j] = dp[i - 1][j];
                
                // 如果从第j号机器人开始负责是可行的，则加入队列
                if (value(i, j) != NA) {
                    // 维护队列单调性（递增）
                    // 移除所有value值大于等于当前value(i, j)的队尾元素
                    while (l < r && value(i, queue[r - 1]) >= value(i, j)) {
                        r--;
                    }
                    // 将机器人j加入队列
                    queue[r++] = j;
                }
                
                // 移除过期的决策点（超出工厂容量限制）
                if (l < r && queue[l] == j - cap) {
                    l++;
                }
                
                // 如果队列不为空，尝试选择第i号工厂来修理机器人
                if (l < r) {
                    // 选择第i号工厂的收益：value(最优起始机器人) + sum[j]
                    dp[i][j] = Math.min(dp[i][j], value(i, queue[l]) + sum[j]);
                }
            }
        }
        
        return dp[n][m];
    }

    /**
     * 计算第i号工厂和第j号机器人之间的距离
     * 
     * @param i 工厂编号
     * @param j 机器人编号
     * @return 工厂和机器人之间的距离
     */
    // i号工厂和j号机器人的距离
    public static long dist(int i, int j) {
        // 使用long类型避免溢出
        return Math.abs((long) fac[i][0] - rob[j]);
    }

    /**
     * 计算第i号工厂从第j号机器人开始负责时的指标值
     * 指标值用于比较不同起始机器人的优劣
     * 
     * @param i 工厂编号
     * @param j 机器人编号
     * @return 指标值，如果不可行则返回NA
     */
    // i号工厂从j号机器人开始负责的指标
    // 真的可行，返回指标的值
    // 如果不可行，返回NA
    public static long value(int i, int j) {
        // 如果前i-1个工厂无法修理前j-1个机器人，则不可行
        if (dp[i - 1][j - 1] == NA) {
            return NA;
        }
        // 指标值为：前i-1个工厂修理前j-1个机器人的最小距离 - 前j-1个机器人去往第i号工厂的距离之和
        // 这个值越小，说明从第j号机器人开始由第i号工厂负责越有利
        return dp[i - 1][j - 1] - sum[j - 1];
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是一个带约束的动态规划问题
     *    - 状态定义：dp[i][j]表示前i个工厂修理前j个机器人的最小总距离
     *    - 状态转移方程较为复杂，需要考虑工厂容量限制
     *    - 目标：求dp[n][m]
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n * m^2)，对于每个状态需要遍历可能的起始机器人
     *    - 空间复杂度：O(n * m)
     *    - 对于大数据会超时
     * 
     * 3. 优化思路：
     *    - 预处理：将工厂和机器人按位置排序
     *    - 使用前缀和优化距离计算
     *    - 使用单调队列优化起始机器人的选择
     * 
     * 4. 数学变换：
     *    - 将状态转移方程变形，提取公共部分
     *    - 令value(j) = dp[i-1][j-1] - sum[j-1]，则dp[i][j] = min{value(k)} + sum[j]
     *    - 这样就将问题转化为在滑动窗口内找value的最小值
     * 
     * 5. 单调队列优化：
     *    - 对于第i号工厂，我们需要在起始机器人范围[max(1, j-cap+1), j]内找到最优起始机器人
     *    - 使用单调递增队列，队首始终是窗口内的最优起始机器人
     *    - 通过value函数比较不同起始机器人的优劣
     * 
     * 6. 队列维护策略：
     *    - 队列存储起始机器人下标，按照value值单调递增排列
     *    - 队首元素：窗口内的最优起始机器人
     *    - 队尾维护：移除所有value值大于等于当前value的元素
     *    - 有效性维护：移除超出工厂容量限制的队首元素
     * 
     * 7. 时间复杂度分析：
     *    - 每个起始机器人最多入队和出队一次，均摊时间复杂度O(1)
     *    - 总时间复杂度：O(n * m)
     *    - 空间复杂度：O(n * m)
     * 
     * 8. 边界情况处理：
     *    - 没有工厂：无法修理任何机器人
     *    - 没有机器人：修理距离为0
     *    - 工厂容量为0：无法修理任何机器人
     * 
     * 9. 为什么是最优解：
     *    - 该解法将朴素DP的O(n * m^2)优化到O(n * m)
     *    - 利用单调队列维护最优决策点，是此类问题的最优解法
     *    - 无法进一步优化时间复杂度，因为需要处理每个工厂和每个机器人
     * 
     * 10. 工程化考量：
     *    - 输入数据预处理：排序确保空间相邻性
     *    - 使用前缀和优化距离计算
     *    - 使用long类型处理大数
     *    - 数组预分配空间，避免动态扩容
     * 
     * 11. 极端场景分析：
     *     - n=1时，只有一个工厂，退化为单工厂问题
     *     - m=1时，只有一个机器人，选择最近的工厂
     *     - 工厂容量很大：可以修理所有机器人
     *     - 工厂容量很小：需要多个工厂协作
     * 
     * 12. 语言特性差异：
     *     - Java: 使用数组模拟队列，性能较好
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}