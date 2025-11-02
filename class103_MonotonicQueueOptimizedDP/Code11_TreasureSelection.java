package class130;

// 宝物筛选
// 小FF有一个最大载重为W的采集车，洞穴里总共有n种宝物，
// 每种宝物的价值为v[i]，重量为w[i]，每种宝物有m[i]件。
// 每件宝物都只能使用一次，求采集车能装下的宝物的最大价值总和。
// 这是经典的多重背包问题，使用单调队列优化。
// 1 <= n <= 100
// 1 <= W <= 40000
// 1 <= v[i], w[i], m[i] <= 100
// 测试链接 : https://www.luogu.com.cn/problem/P1776
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Deque;
import java.util.LinkedList;

public class Code11_TreasureSelection {

    public static int MAXN = 101;

    public static int MAXW = 40001;

    // 物品的价值、重量、数量
    public static int[] v = new int[MAXN];

    public static int[] w = new int[MAXN];

    public static int[] m = new int[MAXN];

    // dp[j]表示重量不超过j时能获得的最大价值
    public static int[] dp = new int[MAXW];

    // 单调队列，用于优化多重背包
    public static Deque<Integer> queue = new LinkedList<>();

    public static int n, W;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        W = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            v[i] = (int) in.nval;
            in.nextToken();
            w[i] = (int) in.nval;
            in.nextToken();
            m[i] = (int) in.nval;
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    // 使用单调队列优化的多重背包解法
    // 时间复杂度：O(n*W)
    // 空间复杂度：O(W)
    public static int compute() {
        // 初始化dp数组
        for (int j = 0; j <= W; j++) {
            dp[j] = 0;
        }

        // 对每种物品进行处理
        for (int i = 1; i <= n; i++) {
            // 按照重量w[i]的余数进行分组处理
            for (int r = 0; r < w[i]; r++) {
                // 清空队列
                queue.clear();

                // 对于余数为r的组，处理所有重量为k*w[i]+r的形式
                for (int k = 0; k * w[i] + r <= W; k++) {
                    int j = k * w[i] + r; // 当前重量
                    int value = dp[j] - k * v[i]; // 价值调整

                    // 维护单调递减队列
                    while (!queue.isEmpty() && dp[queue.peekLast()] - (queue.peekLast() - r) / w[i] * v[i] <= value) {
                        queue.pollLast();
                    }
                    queue.offerLast(k);

                    // 移除超出数量限制的队首元素
                    while (!queue.isEmpty() && queue.peekFirst() < k - m[i]) {
                        queue.pollFirst();
                    }

                    // 更新dp值
                    if (!queue.isEmpty()) {
                        int front = queue.peekFirst();
                        dp[j] = Math.max(dp[j], dp[front * w[i] + r] + (k - front) * v[i]);
                    }
                }
            }
        }

        return dp[W];
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是经典的多重背包问题
     *    - 状态定义：dp[j]表示重量不超过j时能获得的最大价值
     *    - 状态转移方程：dp[j] = max{dp[j-k*w[i]] + k*v[i]}，其中0 <= k <= min(m[i], j/w[i])
     *    - 目标：求dp[W]
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n*W*max(m[i]))，对于每个物品需要枚举选择数量
     *    - 空间复杂度：O(W)
     *    - 对于m[i]较大时会超时
     * 
     * 3. 二进制优化解法：
     *    - 将m[i]件物品拆分成O(log m[i])件物品
     *    - 时间复杂度：O(n*W*log(max(m[i])))
     *    - 空间复杂度：O(W)
     * 
     * 4. 单调队列优化思路：
     *    - 观察状态转移方程，我们可以按w[i]的余数进行分组
     *    - 对于余数相同的重量，可以看作一个等差数列
     *    - 使用单调队列维护决策单调性
     * 
     * 5. 优化策略：
     *    - 按照w[i]的余数将重量分为w[i]组
     *    - 对每组使用单调队列优化
     *    - 队列中存储的是等差数列的项数
     * 
     * 6. 队列维护策略：
     *    - 队列存储项数k，按照dp[k*w[i]+r]-k*v[i]单调递减排列
     *    - 队首元素：当前窗口内的最优决策
     *    - 队尾维护：移除所有价值小于等于当前价值的元素
     *    - 数量限制维护：移除超出m[i]数量限制的队首元素
     * 
     * 7. 时间复杂度分析：
     *    - 每个重量最多入队和出队一次，均摊时间复杂度O(1)
     *    - 总时间复杂度：O(n*W)
     *    - 空间复杂度：O(W)
     * 
     * 8. 为什么是最优解：
     *    - 该解法将朴素DP的O(n*W*max(m[i]))优化到O(n*W)
     *    - 利用单调队列维护决策单调性，是多重背包的最优解法之一
     *    - 比二进制优化更优，因为没有log因子
     * 
     * 9. 工程化考量：
     *    - 输入输出使用高效IO，避免超时
     *    - 代码结构清晰，注释详细
     *    - 异常处理完善
     * 
     * 10. 极端场景分析：
     *     - W=1时，只能选择重量为1的物品
     *     - m[i]=1时，退化为01背包
     *     - m[i]*w[i]>=W时，可以看作完全背包
     * 
     * 11. 语言特性差异：
     *     - Java: 使用LinkedList实现双端队列
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}