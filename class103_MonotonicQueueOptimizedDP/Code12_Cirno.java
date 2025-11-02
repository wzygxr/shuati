import java.util.*;

/**
 * 洛谷P1725 琪露诺问题
 * 题目来源：洛谷 P1725 琪露诺
 * 网址：https://www.luogu.com.cn/problem/P1725
 * 
 * 题目描述：
 * 在幻想乡，琪露诺是以笨蛋闻名的冰之妖精。一天，琪露诺又在玩速冻青蛙，
 * 她的魔法可以在地面上形成一个冰之阶梯，用来跳跃。每次跳跃的时候，
 * 琪露诺会消耗一点魔法，然后她可以从当前的格子跳到前面任意一个格子，
 * 前提是这两个格子之间的高度差不超过一个给定的值d。
 * 
 * 问题转化为：在给定数组中，找到从位置0到位置n的一条路径，
 * 每次可以向右跳到位置i（i > 当前位置），且满足abs(v[i] - v[j]) <= d，
 * 其中j是当前位置。要求路径长度（跳跃次数）的最小值。
 * 
 * 解题思路：
 * 这是一个典型的单调队列优化动态规划问题。
 * - 状态定义：dp[i] 表示到达位置i所需要的最少跳跃次数
 * - 状态转移方程：dp[i] = min(dp[j]) + 1，其中 j 满足 i - r <= j <= i - l
 * - 使用单调队列维护滑动窗口内的最小值
 * 
 * 时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
 * 空间复杂度：O(n)，需要dp数组和单调队列
 */
public class Code12_Cirno {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int l = scanner.nextInt();
        int r = scanner.nextInt();
        long[] v = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            v[i] = scanner.nextLong();
        }
        scanner.close();
        
        System.out.println(solve(n, l, r, v));
    }
    
    /**
     * 解决琪露诺跳跃问题
     * @param n 总格子数
     * @param l 最小跳跃距离
     * @param r 最大跳跃距离
     * @param v 每个格子的高度值
     * @return 到达终点的最少跳跃次数，不可达返回-1
     */
    public static int solve(int n, int l, int r, long[] v) {
        // dp[i]表示到达位置i的最少跳跃次数
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        
        // 单调队列，保存的是索引，按照dp值单调递增
        Deque<Integer> deque = new LinkedList<>();
        deque.offerLast(0);
        
        // 遍历每个位置i
        for (int i = 1; i <= n; i++) {
            // 移除队列中不在有效范围的元素（i - r <= j）
            while (!deque.isEmpty() && deque.peekFirst() < i - r) {
                deque.pollFirst();
            }
            
            // 如果队列不为空，当前dp[i]可以由队列头部的元素转移而来
            if (!deque.isEmpty()) {
                dp[i] = dp[deque.peekFirst()] + 1;
            }
            
            // 当i >= l时，i可以作为后续位置的转移点
            if (i >= l) {
                // 维护队列的单调性，移除队列尾部dp值大于等于dp[i]的元素
                while (!deque.isEmpty() && dp[i] <= dp[deque.peekLast()]) {
                    deque.pollLast();
                }
                deque.offerLast(i);
            }
        }
        
        // 如果终点不可达，返回-1
        return dp[n] == Integer.MAX_VALUE ? -1 : dp[n];
    }
}