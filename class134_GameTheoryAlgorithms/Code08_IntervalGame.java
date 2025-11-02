package class095;

// 区间博弈 (Interval DP Game)
// 两人轮流从序列的两端取数，每次只能取左端或右端的数
// 每个玩家的目标是使自己的总得分最大化
// 假设两位玩家都采取最优策略，求先手玩家的最大得分
// 或判断先手是否必胜
// 
// 算法思路：
// 1. 使用动态规划求解区间博弈问题
// 2. 状态定义：dp[i][j] 表示在区间nums[i...j]中，当前玩家与另一位玩家的最大得分差
// 3. 状态转移：
//    dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
//    选择左端或右端，然后减去对方在剩余区间的最优得分差
// 4. 最终判断：如果dp[0][n-1] > 0，则先手必胜；否则必败
// 
// 时间复杂度：O(n^2) - 状态数为n^2，每个状态需要O(1)计算
// 空间复杂度：O(n^2) - 二维DP数组
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 序列两端取数游戏
//    - 资源分配问题
//    - 博弈双方具有完全信息且都采取最优策略
// 2. 解题技巧：
//    - 定义状态表示当前区间的最优策略差异
//    - 自底向上填充DP表
//    - 考虑先手优势和后手最优反应
// 3. 经典题目：
//    - LeetCode 486. Predict the Winner
//    - LeetCode 877. Stone Game
//    - AtCoder DP Contest L - Deque

import java.util.Arrays;

public class Code08_IntervalGame {
    
    // 区间DP求解两人取数游戏
    // 返回先手是否必胜
    public static boolean predictTheWinner(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        int n = nums.length;
        // dp[i][j] 表示在区间nums[i...j]中，当前玩家与另一位玩家的最大得分差
        int[][] dp = new int[n][n];
        
        // 初始化：单个元素的区间，得分差就是元素本身
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        
        // 自底向上填充DP表
        // len表示区间长度-1
        for (int len = 1; len < n; len++) {
            for (int i = 0; i + len < n; i++) {
                int j = i + len;
                // 当前玩家可以选择左端或右端
                // 选择后，对方将在剩余区间采取最优策略
                // 所以要减去对方的最优得分差
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
            }
        }
        
        // 如果最终得分差大于等于0，则先手必胜
        return dp[0][n - 1] >= 0;
    }
    
    // 空间优化版本：使用一维DP数组
    // 时间复杂度：O(n^2)
    // 空间复杂度：O(n)
    public static boolean predictTheWinnerOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        int n = nums.length;
        // 只使用一维数组记录当前长度的区间
        int[] dp = Arrays.copyOf(nums, n);
        
        // 自底向上填充
        for (int len = 1; len < n; len++) {
            for (int i = 0; i + len < n; i++) {
                int j = i + len;
                // dp[i] 此时存储的是上一轮（长度len-1）的dp[i+1][j]
                // 而dp[j-1] 存储的是上一轮的dp[i][j-1]
                dp[i] = Math.max(nums[i] - dp[i + 1], nums[j] - dp[i]);
            }
        }
        
        return dp[0] >= 0;
    }
    
    // 计算先手的最大得分（假设两人都采取最优策略）
    public static int maxScoreForFirstPlayer(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        int n = nums.length;
        int[][] dp = new int[n][n];
        int[][] sum = new int[n][n];
        
        // 计算区间和
        for (int i = 0; i < n; i++) {
            sum[i][i] = nums[i];
            for (int j = i + 1; j < n; j++) {
                sum[i][j] = sum[i][j - 1] + nums[j];
            }
        }
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        
        // 自底向上填充
        for (int len = 1; len < n; len++) {
            for (int i = 0; i + len < n; i++) {
                int j = i + len;
                // 当前玩家选择左端或右端后，剩下的区间对手会获得最优解
                // 当前玩家的总得分 = 区间和 - 对手的得分
                dp[i][j] = sum[i][j] - Math.min(dp[i + 1][j], dp[i][j - 1]);
            }
        }
        
        return dp[0][n - 1];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: LeetCode 486. Predict the Winner
        int[] nums1 = {1, 5, 2};
        System.out.println("测试用例1 - LeetCode 486:");
        System.out.println("数组: " + Arrays.toString(nums1));
        System.out.println("先手是否必胜: " + predictTheWinner(nums1));
        System.out.println("空间优化版本结果: " + predictTheWinnerOptimized(nums1));
        System.out.println("先手最大得分: " + maxScoreForFirstPlayer(nums1));
        
        // 测试用例2: LeetCode 877. Stone Game
        int[] nums2 = {5, 3, 4, 5};
        System.out.println("\n测试用例2 - LeetCode 877:");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("先手是否必胜: " + predictTheWinner(nums2));
        System.out.println("先手最大得分: " + maxScoreForFirstPlayer(nums2));
        
        // 测试用例3: 特殊情况 - 空数组
        try {
            predictTheWinner(new int[]{});
        } catch (IllegalArgumentException e) {
            System.out.println("\n测试用例3 - 异常处理:");
            System.out.println("预期异常: " + e.getMessage());
        }
    }
}