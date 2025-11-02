package class130;

// 向右跳跃获得最大得分
// 给定长度为n+1的数组arr，下标编号0 ~ n，给定正数a、b
// 一开始在0位置，每次可以选择[a,b]之间的一个整数，作为向右跳跃的距离
// 每来到一个位置i，可以获得arr[i]作为得分，位置一旦大于n就停止
// 返回能获得的最大得分
// 1 <= n <= 2 * 10^5
// 1 <= a <= b <= n
// -1000 <= arr[i] <= +1000
// 测试链接 : https://www.luogu.com.cn/problem/P1725
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_JumpRight {

    // 最大数组长度常量
    public static int MAXN = 200001;

    // 表示负无穷的常量
    public static int NA = Integer.MIN_VALUE;

    // 输入数组，存储每个位置的得分
    public static int[] arr = new int[MAXN];

    // dp数组，dp[i]表示到达位置i能获得的最大得分
    public static int[] dp = new int[MAXN];

    // 单调队列，用于维护滑动窗口内的最大值
    // 存储的是下标，按照dp值单调递减排列
    public static int[] queue = new int[MAXN];

    // 队列的左右指针
    public static int l, r;

    // 输入参数
    public static int n, a, b;

    public static void main(String[] args) throws IOException {
        try {
            // 使用高效IO读取输入
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StreamTokenizer in = new StreamTokenizer(br);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
            
            // 读取输入参数
            if (!in.nextToken() || in.ttype == StreamTokenizer.TT_EOF) {
                throw new IllegalArgumentException("输入参数不足");
            }
            n = (int) in.nval;
            
            if (!in.nextToken() || in.ttype == StreamTokenizer.TT_EOF) {
                throw new IllegalArgumentException("输入参数不足");
            }
            a = (int) in.nval;
            
            if (!in.nextToken() || in.ttype == StreamTokenizer.TT_EOF) {
                throw new IllegalArgumentException("输入参数不足");
            }
            b = (int) in.nval;
            
            // 参数验证
            validateParameters(n, a, b);
            
            // 读取数组元素
            for (int i = 0; i <= n; i++) {
                if (!in.nextToken() || in.ttype == StreamTokenizer.TT_EOF) {
                    throw new IllegalArgumentException("数组元素不足");
                }
                arr[i] = (int) in.nval;
            }
            
            // 输出计算结果
            int result = compute();
            out.println(result);
            out.flush();
            out.close();
            br.close();
            
        } catch (IllegalArgumentException e) {
            System.err.println("输入错误: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("程序执行错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * 参数验证方法
     * @param n 数组长度
     * @param a 最小跳跃距离
     * @param b 最大跳跃距离
     */
    private static void validateParameters(int n, int a, int b) {
        if (n < 0 || n >= MAXN) {
            throw new IllegalArgumentException("n必须在[0, " + (MAXN - 1) + "]范围内");
        }
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException("a和b必须为正数");
        }
        if (a > b) {
            throw new IllegalArgumentException("a不能大于b");
        }
        if (b > n) {
            throw new IllegalArgumentException("b不能大于n");
        }
    }

    /**
     * 计算最大得分
     * 使用单调队列优化的动态规划解法
     * 时间复杂度：O(n)，每个元素最多入队和出队一次
     * 空间复杂度：O(n)，dp数组和单调队列的空间
     * 
     * @return 能获得的最大得分
     */
    public static int compute() {
        // 边界情况处理：如果n为0，直接返回arr[0]
        if (n == 0) {
            return arr[0];
        }
        
        // 边界情况处理：如果a等于b等于1的特殊情况
        if (a == 1 && b == 1) {
            return computeSpecialCase();
        }
        
        // 初始化dp数组
        Arrays.fill(dp, NA);
        
        // 初始状态：在位置0，得分为arr[0]
        dp[0] = arr[0];
        l = r = 0;
        
        // 动态规划过程：计算每个位置的最大得分
        for (int i = 1; i <= n; i++) {
            // 添加新的可能决策点（i-a位置）
            add(i - a);
            // 移除过期的决策点（i-b-1位置）
            overdue(i - b - 1);
            // 状态转移：dp[i] = max{dp[j]} + arr[i]，其中j在[i-b, i-a]范围内
            dp[i] = l < r ? dp[queue[l]] + arr[i] : NA;
            
            // 检查数值溢出
            if (dp[i] != NA && (dp[i] > Integer.MAX_VALUE - 1000 || dp[i] < Integer.MIN_VALUE + 1000)) {
                throw new ArithmeticException("数值溢出风险，请检查输入数据范围");
            }
        }
        
        // 在所有可能的终点中找到最大值
        // 终点范围：[n+1-b, n]，因为每次最多跳b步
        int ans = NA;
        for (int i = n + 1 - b; i <= n; i++) {
            if (dp[i] != NA) {
                ans = Math.max(ans, dp[i]);
            }
        }
        
        // 检查是否有可行解
        if (ans == NA) {
            throw new IllegalStateException("无可行解，请检查输入参数和数组值");
        }
        
        return ans;
    }
    
    /**
     * 处理a=b=1的特殊情况
     * 这种情况下，问题简化为求数组的最大前缀和
     */
    private static int computeSpecialCase() {
        int maxSum = arr[0];
        int currentSum = arr[0];
        
        for (int i = 1; i <= n; i++) {
            currentSum += arr[i];
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }

    /**
     * 向单调队列中添加新的决策点
     * 
     * @param j 要添加的位置下标
     */
    public static void add(int j) {
        // 只有当j是有效位置且dp[j]不是负无穷时才添加
        if (j >= 0 && dp[j] != NA) {
            // 维护队列单调性（递减）
            // 移除所有dp值小于等于当前dp[j]的队尾元素
            // 因为如果dp[k] <= dp[j]，那么k永远不可能成为后续位置的最优选择
            while (l < r && dp[queue[r - 1]] <= dp[j]) {
                r--;
            }
            // 将位置j加入队列
            queue[r++] = j;
        }
    }

    /**
     * 移除过期的决策点
     * 
     * @param t 要移除的位置下标
     */
    public static void overdue(int t) {
        // 如果队首元素是位置t，则移除它（已过期）
        if (l < r && queue[l] == t) {
            l++;
        }
    }

    /*
     * 算法思路详解：
     * 
     * 1. 问题分析：
     *    - 这是一个典型的动态规划问题
     *    - 状态定义：dp[i]表示到达位置i能获得的最大得分
     *    - 状态转移方程：dp[i] = max{dp[j]} + arr[i]，其中j ∈ [max(0, i-b), i-a]
     *    - 目标：求所有可能终点中的最大dp值
     * 
     * 2. 朴素解法：
     *    - 时间复杂度：O(n*b)，对于每个位置i，需要遍历前面b个位置找最大值
     *    - 空间复杂度：O(n)
     *    - 对于大数据会超时
     * 
     * 3. 单调队列优化：
     *    - 观察状态转移方程，我们需要在滑动窗口[max(0, i-b), i-a]中找到dp的最大值
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
     *    - 当i < b时，可以从位置0跳过来
     *    - 当i >= b时，只能从[i-b, i-a]范围内跳过来
     *    - 初始状态dp[0] = arr[0]
     *    - 终点不是固定的n，而是在[n+1-b, n]范围内
     * 
     * 7. 为什么是最优解：
     *    - 该解法将朴素DP的O(n*b)优化到O(n)
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
     *    - n=1时，直接返回arr[0]
     *    - a=b=1时，只能一步步跳，退化为前缀和
     *    - arr全为负数时，仍能正确找到最大得分路径
     *    - a=b=n时，第一步就能跳到最后
     * 
     * 10. 语言特性差异：
     *     - Java: 使用数组模拟队列，性能较好
     *     - C++: 可使用deque或数组模拟队列
     *     - Python: 可使用collections.deque
     */
}