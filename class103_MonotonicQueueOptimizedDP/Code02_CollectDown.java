package class130;

// 向下收集获得最大能量
// 有一个n * m的区域，行和列的编号从1开始
// 每个能量点用(i, j, v)表示，i行j列上有价值为v的能量点
// 一共有k个能量点，并且所有能量点一定在不同的位置
// 一开始可以在第1行的任意位置，然后每一步必须向下移动
// 向下去往哪个格子是一个范围，如果当前在(i, j)位置
// 那么往下可以选择(i+1, j-t)...(i+1, j+t)其中的一个格子
// 到达最后一行时，收集过程停止，返回能收集到的最大能量价值
// 1 <= n、m、k、t <= 4000
// 1 <= v <= 100
// 测试链接 : https://www.luogu.com.cn/problem/P3800
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_CollectDown {

    // 最大行数常量
    public static int MAXN = 4001;
    
    // 最大列数常量
    public static int MAXM = 4001;

    // dp数组，dp[i][j]表示到达第i行第j列能收集到的最大能量
    public static int[][] dp = new int[MAXN][MAXM];

    // 单调队列，用于维护滑动窗口内的最大值
    // 存储的是列下标，按照dp值单调递减排列
    public static int[] queue = new int[MAXM];

    // 队列的左右指针
    public static int l, r;

    // 输入参数
    public static int n, m, k, t;

    /**
     * 初始化dp数组
     */
    public static void build() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = 0;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用高效IO读取输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入参数
        in.nextToken();
        n = (int) in.nval;  // 行数
        in.nextToken();
        m = (int) in.nval;  // 列数
        in.nextToken();
        k = (int) in.nval;  // 能量点数量
        in.nextToken();
        t = (int) in.nval;  // 移动范围
        
        // 初始化dp数组
        build();
        
        // 读取能量点信息
        for (int i = 1, r, c, v; i <= k; i++) {
            in.nextToken();
            r = (int) in.nval;  // 行号
            in.nextToken();
            c = (int) in.nval;  // 列号
            in.nextToken();
            v = (int) in.nval;  // 能量值
            dp[r][c] = v;       // 在对应位置设置能量值
        }
        
        // 输出计算结果
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    /**
     * 计算能收集到的最大能量
     * 使用单调队列优化的动态规划解法
     * 时间复杂度：O(n*m)，每个位置最多入队和出队一次
     * 空间复杂度：O(n*m)，dp数组和单调队列的空间
     * 
     * @return 能收集到的最大能量
     */
    public static int compute() {
        // 从第2行开始计算每行的最大能量
        for (int i = 2; i <= n; i++) {
            l = r = 0;  // 重置队列指针
            
            // 初始化队列，将前t列加入队列
            for (int j = 1; j <= t; j++) {
                add(i - 1, j);
            }
            
            // 计算第i行每列的最大能量
            for (int j = 1; j <= m; j++) {
                // 添加新的可能决策点（j+t列）
                add(i - 1, j + t);
                // 移除过期的决策点（j-t-1列）
                overdue(j - t - 1);
                // 状态转移：dp[i][j] = dp[i-1][最佳前驱位置] + dp[i][j]
                dp[i][j] += dp[i - 1][queue[l]];
            }
        }
        
        // 在最后一行中找到最大能量值
        int ans = Integer.MIN_VALUE;
        for (int j = 1; j <= m; j++) {
            ans = Math.max(ans, dp[n][j]);
        }
        return ans;
    }

    /**
     * 向单调队列中添加新的决策点
     * 
     * @param i 行号
     * @param j 列号
     */
    public static void add(int i, int j) {
        // 只有当j是有效列号时才添加
        if (j <= m) {
            // 维护队列单调性（递减）
            // 移除所有dp值小于等于当前dp[i][j]的队尾元素
            while (l < r && dp[i][queue[r - 1]] <= dp[i][j]) {
                r--;
            }
            // 将列号j加入队列
            queue[r++] = j;
        }
    }

    /**
     * 移除过期的决策点
     * 
     * @param t 要移除的列号
     */
    public static void overdue(int t) {
        // 如果队首元素是列号t，则移除它（已过期）
        if (l < r && queue[l] == t) {
            l++;
        }
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是一个二维动态规划问题
     *    - 状态定义：dp[i][j]表示到达第i行第j列能收集到的最大能量
     *    - 状态转移方程：dp[i][j] = max{dp[i-1][k]} + dp[i][j]，其中k ∈ [max(1, j-t), min(m, j+t)]
     *    - 目标：求dp[n][j]的最大值
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n*m*t)，对于每个位置需要遍历前后t个位置找最大值
     *    - 空间复杂度：O(n*m)
     *    - 对于大数据会超时
     * 
     * 3. 单调队列优化：
     *    - 观察状态转移方程，我们需要在滑动窗口[max(1, j-t), min(m, j+t)]中找到dp[i-1]的最大值
     *    - 这正是单调队列的经典应用场景
     *    - 使用单调递减队列，队首始终是窗口内的最大dp值
     * 
     * 4. 队列维护策略：
     *    - 队列存储列号，按照dp[i-1]值单调递减排列
     *    - 队首元素：窗口内的最大dp[i-1]值对应的列号
     *    - 队尾维护：移除所有dp[i-1]值小于等于当前dp[i-1][j]的元素
     *    - 有效性维护：移除超出移动范围的队首元素
     * 
     * 5. 时间复杂度分析：
     *    - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
     *    - 总时间复杂度：O(n*m)
     *    - 空间复杂度：O(n*m)
     * 
     * 6. 边界情况处理：
     *    - 第一行的初始能量值就是输入的能量点值
     *    - 边界列的移动范围需要限制在[1, m]内
     *    - 空位置的能量值为0
     * 
     * 7. 为什么是最优解：
     *    - 该解法将朴素DP的O(n*m*t)优化到O(n*m)
     *    - 利用单调队列维护滑动窗口最值，是此类问题的最优解法
     *    - 无法进一步优化时间复杂度，因为需要处理每个位置至少一次
     * 
     * 8. 工程化考量：
     *    - 输入输出使用高效IO，避免超时
     *    - 数组预分配空间，避免动态扩容
     *    - 代码结构清晰，注释详细
     *    - 异常处理完善（题目保证输入合法）
     * 
     * 9. 极端场景分析：
     *    - n=1时，直接返回第一行的最大能量值
     *    - t=0时，只能垂直向下移动
     *    - t=m时，可以在一行内任意移动
     *    - 所有位置都有能量点时，需要正确处理叠加
     * 
     * 10. 语言特性差异：
     *     - Java: 使用数组模拟队列，性能较好
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}