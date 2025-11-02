package class130;

// 跳跃游戏VI
// 给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。
// 一开始你在下标 0 处。每一步，你最多可以往前跳 k 步，但你不能跳出数组的边界。
// 也就是说，你可以从下标 i 跳到 [i + 1, min(n - 1, i + k)] 包含两个端点的任意位置。
// 你的目标是到达数组最后一个位置（下标为 n - 1），你的得分为经过的所有数字之和。
// 返回你能得到的最大得分。
// 1 <= nums.length, k <= 10^5
// -10^4 <= nums[i] <= 10^4
// 测试链接 : https://leetcode.cn/problems/jump-game-vi/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code09_JumpGameVI {

    public static int MAXN = 100001;

    public static int[] nums = new int[MAXN];

    // dp[i]表示到达位置i能获得的最大得分
    public static int[] dp = new int[MAXN];

    // 单调队列，存储下标，维护单调递减队列(队首是最大值)
    public static int[] queue = new int[MAXN];

    public static int l, r;

    public static int n, k;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        k = (int) in.nval;
        for (int i = 0; i < n; i++) {
            in.nextToken();
            nums[i] = (int) in.nval;
        }
        out.println(compute());
        out.flush();
        out.close();
        br.close();
    }

    // 使用单调队列优化的动态规划解法
    // 时间复杂度：O(n)，每个元素最多入队和出队一次
    // 空间复杂度：O(n)，dp数组和单调队列的空间
    public static int compute() {
        // 初始状态：在位置0，得分为nums[0]
        dp[0] = nums[0];
        l = r = 0;
        queue[r++] = 0; // 将初始位置0加入队列

        // 从位置1开始计算每个位置的最大得分
        for (int i = 1; i < n; i++) {
            // 移除队列中超出跳跃范围的元素
            // 当前位置i最多能从i-k位置跳过来
            while (l < r && queue[l] < i - k) {
                l++;
            }

            // 状态转移：dp[i] = max{dp[j]} + nums[i]，其中j在[i-k, i-1]范围内
            dp[i] = dp[queue[l]] + nums[i];

            // 维护队列单调性，移除所有小于等于当前dp值的队尾元素
            // 因为如果dp[j] <= dp[i]，那么j永远不可能成为后续位置的最优选择
            while (l < r && dp[queue[r - 1]] <= dp[i]) {
                r--;
            }

            // 将当前位置加入队列
            queue[r++] = i;
        }

        // 返回到达最后一个位置的最大得分
        return dp[n - 1];
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是一个典型的动态规划问题
     *    - 状态定义：dp[i]表示到达位置i能获得的最大得分
     *    - 状态转移方程：dp[i] = max{dp[j]} + nums[i]，其中j ∈ [max(0, i-k), i-1]
     *    - 目标：求dp[n-1]
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n*k)，对于每个位置i，需要遍历前面k个位置找最大值
     *    - 空间复杂度：O(n)
     *    - 对于k较大时会超时
     * 
     * 3. 单调队列优化：
     *    - 观察状态转移方程，我们需要在滑动窗口[max(0, i-k), i-1]中找到dp的最大值
     *    - 这正是单调队列的经典应用场景
     *    - 使用单调递减队列，队首始终是窗口内的最大dp值
     * 
     * 4. 队列维护策略：
     *    - 队列存储下标，按照dp值单调递减排列
     *    - 队首元素：窗口内的最大dp值对应的下标
     *    - 队尾维护：移除所有dp值小于等于当前dp[i]的元素
     *    - 有效性维护：移除超出跳跃范围的队首元素
     * 
     * 5. 时间复杂度分析：
     *    - 每个元素最多入队和出队一次，均摊时间复杂度O(1)
     *    - 总时间复杂度：O(n)
     *    - 空间复杂度：O(n)
     * 
     * 6. 边界情况处理：
     *    - 当i < k时，可以从位置0跳过来
     *    - 当i >= k时，只能从[i-k, i-1]范围内跳过来
     *    - 初始状态dp[0] = nums[0]
     * 
     * 7. 为什么是最优解：
     *    - 该解法将朴素DP的O(n*k)优化到O(n)
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
     *    - n=1时，直接返回nums[0]
     *    - k=1时，只能一步步跳，退化为前缀和
     *    - nums全为负数时，仍能正确找到最大得分路径
     *    - k>=n-1时，第一步就能跳到最后，但仍需考虑中间路径
     * 
     * 10. 语言特性差异：
     *     - Java: 使用ArrayDeque实现双端队列，性能较好
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}