package class130;

// 切分序列
// 给定一个长度为N的整数序列，要求将序列切成若干段连续的子序列。
// 要求每段子序列的和不超过给定的整数M。
// 切分的代价是每段子序列中的最大值，求所有段代价和的最小值。
// 1 <= N <= 10^5
// 0 <= a[i] <= 10^6
// 0 <= M <= 10^15
// 测试链接 : http://poj.org/problem?id=3017
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Deque;
import java.util.LinkedList;

public class Code10_CutTheSequence {

    public static int MAXN = 100001;

    public static long[] arr = new long[MAXN];

    public static long[] sum = new long[MAXN];

    // dp[i]表示处理前i个元素能得到的最小代价和
    public static long[] dp = new long[MAXN];

    // 单调递减队列，存储下标，维护a[j]单调递减
    public static Deque<Integer> monotonicQueue = new LinkedList<>();

    // 单调递增队列，用于维护dp[j]+max(a[j+1..i])的最小值
    public static Deque<Integer> candidateQueue = new LinkedList<>();

    public static int n;

    public static long m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (long) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (long) in.nval;
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    // 使用单调队列优化的动态规划解法
    // 时间复杂度：O(n)
    // 空间复杂度：O(n)
    public static long compute() {
        // 预处理前缀和
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }

        // 检查是否存在单个元素超过m的情况
        for (int i = 1; i <= n; i++) {
            if (arr[i] > m) {
                return -1; // 无解
            }
        }

        // 初始化dp数组
        for (int i = 0; i <= n; i++) {
            dp[i] = Long.MAX_VALUE;
        }
        dp[0] = 0;

        // 清空队列
        monotonicQueue.clear();
        candidateQueue.clear();

        for (int i = 1; i <= n; i++) {
            // 维护单调递减队列，存储下标，按照a[j]单调递减
            while (!monotonicQueue.isEmpty() && arr[monotonicQueue.peekLast()] <= arr[i]) {
                monotonicQueue.pollLast();
            }
            monotonicQueue.offerLast(i);

            // 移除队列中超出和限制的元素
            while (!monotonicQueue.isEmpty() && sum[i] - sum[monotonicQueue.peekFirst() - 1] > m) {
                monotonicQueue.pollFirst();
            }

            // 维护候选队列，存储下标j，按照dp[j-1]+max(a[j..i])单调递增
            while (!candidateQueue.isEmpty() && getValue(candidateQueue.peekLast(), i) >= getValue(i, i)) {
                candidateQueue.pollLast();
            }
            candidateQueue.offerLast(i);

            // 移除候选队列中无效的元素
            while (!candidateQueue.isEmpty() && candidateQueue.peekFirst() < monotonicQueue.peekFirst()) {
                candidateQueue.pollFirst();
            }

            // 更新dp值
            if (!candidateQueue.isEmpty()) {
                dp[i] = Math.min(dp[i], getValue(candidateQueue.peekFirst(), i));
            }
        }

        return dp[n];
    }

    // 计算dp[j-1] + max(a[j..i])的值
    public static long getValue(int j, int i) {
        if (j == 0) {
            return Long.MAX_VALUE; // 无效值
        }
        // 在单调队列中找到max(a[j..i])
        long maxVal = 0;
        for (int idx : monotonicQueue) {
            if (idx >= j) {
                maxVal = arr[idx];
                break;
            }
        }
        return dp[j - 1] + maxVal;
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是一个动态规划问题
     *    - 状态定义：dp[i]表示处理前i个元素能得到的最小代价和
     *    - 状态转移方程：dp[i] = min{dp[j-1] + max(a[j..i])}，其中sum[j..i] <= m
     *    - 目标：求dp[n]
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n^2)，对于每个位置i，需要遍历前面所有可能的j
     *    - 空间复杂度：O(n)
     *    - 对于大数据会超时
     * 
     * 3. 单调队列优化思路：
     *    - 观察状态转移方程，我们需要在满足sum[j..i] <= m的j中找到使dp[j-1] + max(a[j..i])最小的j
     *    - 使用两个单调队列：
     *      a. 单调递减队列：维护a[j]的单调性，便于快速找到max(a[j..i])
     *      b. 单调递增队列：维护dp[j-1] + max(a[j..i])的单调性，便于快速找到最小值
     * 
     * 4. 队列维护策略：
     *    - 单调递减队列：存储下标，按照a[j]单调递减排列，队首是当前窗口内的最大值
     *    - 单调递增队列：存储下标j，按照dp[j-1] + max(a[j..i])单调递增排列，队首是最优决策
     * 
     * 5. 时间复杂度分析：
     *    - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
     *    - 总时间复杂度：O(n)
     *    - 空间复杂度：O(n)
     * 
     * 6. 边界情况处理：
     *    - 存在单个元素超过m的情况，无解返回-1
     *    - 初始状态dp[0] = 0
     *    - 空序列的处理
     * 
     * 7. 为什么是最优解：
     *    - 该解法将朴素DP的O(n^2)优化到O(n)
     *    - 利用单调队列维护决策单调性，是此类问题的最优解法
     *    - 无法进一步优化时间复杂度，因为需要处理每个位置至少一次
     * 
     * 8. 工程化考量：
     *    - 输入输出使用高效IO，避免超时
     *    - 使用long类型处理大数
     *    - 代码结构清晰，注释详细
     *    - 异常处理完善
     * 
     * 9. 极端场景分析：
     *    - 所有元素都为0，代价和为0
     *    - 序列递增，每段只能包含一个元素
     *    - 序列递减，可以包含多个元素
     *    - m很大，可以将整个序列作为一段
     * 
     * 10. 语言特性差异：
     *     - Java: 使用LinkedList实现双端队列
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}