package class070;

import java.util.Arrays;

/**
 * 删除并获得点数
 * 给你一个整数数组 nums ，你可以对它进行一些操作。
 * 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。
 * 之后，你必须删除 所有 等于 nums[i] - 1 和 nums[i] + 1 的元素。
 * 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
 * 测试链接 : https://leetcode.cn/problems/delete-and-earn/
 * 
 * 算法核心思想：
 * 1. 这个问题可以转化为打家劫舍问题的变体
 * 2. 关键观察：选择某个数字x后，就不能选择x-1和x+1，这类似于打家劫舍中不能选择相邻房屋
 * 3. 首先统计每个数字的总点数（数字值 × 出现次数）
 * 4. 然后使用动态规划在数字序列中选择不相邻的数字以获得最大点数
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n + k) - 其中n是数组长度，k是数组中的最大值
 * - 空间复杂度：O(k) - 需要额外的points数组存储每个数字的总点数
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 性能优化：使用空间优化的动态规划
 * 3. 数值范围：处理大数值范围的情况
 */
public class Code08_DeleteAndEarn {

    /**
     * 计算通过删除元素能获得的最大点数（基础版本）
     * 
     * 算法原理：
     * 1. 统计阶段：计算每个数字的总点数 points[num] = num * count(num)
     * 2. 转化阶段：问题转化为在points数组中选择不相邻元素的最大和
     * 3. 动态规划：使用打家劫舍问题的标准解法
     * 
     * 时间复杂度：O(n + k) - n是数组长度，k是最大值
     * 空间复杂度：O(k) - 需要points和dp数组
     * 
     * @param nums 输入的整数数组
     * @return 能获得的最大点数
     * @throws IllegalArgumentException 如果输入数组为空
     */
    public static int deleteAndEarn(int[] nums) {
        // 边界检查
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        // 计算数组中的最大值
        int maxVal = Arrays.stream(nums).max().getAsInt();
        
        // points[i]表示选择所有数字i能获得的总点数
        int[] points = new int[maxVal + 1];
        for (int num : nums) {
            points[num] += num;
        }
        
        // 特殊情况处理：如果最大值很小
        if (maxVal == 0) {
            return points[0];
        }
        if (maxVal == 1) {
            return Math.max(points[0], points[1]);
        }
        
        // 动态规划数组
        int[] dp = new int[maxVal + 1];
        dp[0] = points[0];
        dp[1] = Math.max(points[0], points[1]);
        
        // 状态转移：标准的打家劫舍问题
        for (int i = 2; i <= maxVal; i++) {
            // 选择points[i]就不能选择points[i-1]，可以加上dp[i-2]
            // 不选择points[i]就可以获得dp[i-1]的点数
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + points[i]);
        }
        
        return dp[maxVal];
    }
    
    /**
     * 空间优化版本
     * 
     * 算法改进：
     * - 使用两个变量代替dp数组，将空间复杂度从O(k)优化到O(1)
     * - 保持相同的时间复杂度
     * 
     * 时间复杂度：O(n + k) - 其中n是数组长度，k是数组中的最大值
     * 空间复杂度：O(k) - 需要额外的points数组存储每个数字的总点数
     * 
     * @param nums 输入的整数数组
     * @return 能获得的最大点数
     */
    public static int deleteAndEarnOptimized(int[] nums) {
        // 边界检查
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        
        // 计算数组中的最大值
        int maxVal = Arrays.stream(nums).max().getAsInt();
        
        // points[i]表示选择所有数字i能获得的总点数
        int[] points = new int[maxVal + 1];
        for (int num : nums) {
            points[num] += num;
        }
        
        // 特殊情况处理
        if (maxVal == 0) {
            return points[0];
        }
        if (maxVal == 1) {
            return Math.max(points[0], points[1]);
        }
        
        // 空间优化的动态规划
        int prevPrev = points[0];  // dp[i-2]
        int prev = Math.max(points[0], points[1]);  // dp[i-1]
        
        for (int i = 2; i <= maxVal; i++) {
            int current = Math.max(prev, prevPrev + points[i]);
            prevPrev = prev;
            prev = current;
        }
        
        return prev;
    }
    
    /**
     * 使用哈希表优化的版本（适用于数值范围很大的情况）
     * 
     * 算法改进：
     * - 当数值范围很大但实际出现的数字很少时，使用TreeMap存储点数
     * - 只处理实际出现的数字，避免遍历整个数值范围
     * 
     * 时间复杂度：O(n log n) - 排序和遍历
     * 空间复杂度：O(n) - 存储实际出现的数字
     * 
     * @param nums 输入的整数数组
     * @return 能获得的最大点数
     */
    public static int deleteAndEarnHashMap(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 统计每个数字的总点数
        java.util.TreeMap<Integer, Integer> pointsMap = new java.util.TreeMap<>();
        for (int num : nums) {
            pointsMap.put(num, pointsMap.getOrDefault(num, 0) + num);
        }
        
        // 如果没有数字，返回0
        if (pointsMap.isEmpty()) {
            return 0;
        }
        
        // 将数字按顺序排列
        java.util.List<Integer> keys = new java.util.ArrayList<>(pointsMap.keySet());
        int n = keys.size();
        
        // 特殊情况：只有一个数字
        if (n == 1) {
            return pointsMap.get(keys.get(0));
        }
        
        // 动态规划
        int[] dp = new int[n];
        dp[0] = pointsMap.get(keys.get(0));
        
        // 检查第二个数字是否与第一个相邻
        if (keys.get(1) - keys.get(0) == 1) {
            dp[1] = Math.max(dp[0], pointsMap.get(keys.get(1)));
        } else {
            dp[1] = dp[0] + pointsMap.get(keys.get(1));
        }
        
        for (int i = 2; i < n; i++) {
            int currentKey = keys.get(i);
            int prevKey = keys.get(i - 1);
            
            if (currentKey - prevKey == 1) {
                // 当前数字与前一个数字相邻，不能同时选择
                dp[i] = Math.max(dp[i - 1], dp[i - 2] + pointsMap.get(currentKey));
            } else {
                // 当前数字与前一个数字不相邻，可以同时选择
                dp[i] = dp[i - 1] + pointsMap.get(currentKey);
            }
        }
        
        return dp[n - 1];
    }
    
    /**
     * 主函数用于测试和演示
     */
    public static void main(String[] args) {
        // 测试用例1: 标准情况
        int[] test1 = {3, 4, 2};
        System.out.println("测试用例1: " + java.util.Arrays.toString(test1));
        System.out.println("结果: " + deleteAndEarn(test1)); // 期望: 6
        
        // 测试用例2: 重复数字
        int[] test2 = {2, 2, 3, 3, 3, 4};
        System.out.println("测试用例2: " + java.util.Arrays.toString(test2));
        System.out.println("结果: " + deleteAndEarn(test2)); // 期望: 9
        
        // 测试用例3: 单元素数组
        int[] test3 = {5};
        System.out.println("测试用例3: " + java.util.Arrays.toString(test3));
        System.out.println("结果: " + deleteAndEarn(test3)); // 期望: 5
        
        // 测试用例4: 大数值范围
        int[] test4 = {1, 1, 1, 2, 4, 5, 5, 5, 6};
        System.out.println("测试用例4: " + java.util.Arrays.toString(test4));
        System.out.println("结果: " + deleteAndEarn(test4)); // 期望: 18
        
        // 验证不同实现方法的一致性
        System.out.println("方法验证: deleteAndEarnOptimized(test1) = " + deleteAndEarnOptimized(test1));
        System.out.println("方法验证: deleteAndEarnHashMap(test1) = " + deleteAndEarnHashMap(test1));
    }
    
    /*
     * 扩展思考与深度分析：
     * 
     * 1. 算法正确性证明：
     *    - 为什么这个问题可以转化为打家劫舍问题？
     *      因为选择某个数字x后，就不能选择x-1和x+1，这类似于不能选择相邻房屋
     *    - 统计阶段的重要性：将重复数字的点数累加，简化问题
     *    - 动态规划的状态转移：标准的打家劫舍公式
     * 
     * 2. 性能优化分析：
     *    - 空间优化：从O(k)优化到O(1)的常数空间
     *    - 哈希表优化：适用于数值范围大但实际数字少的情况
     *    - 特殊情况处理：提高算法效率
     * 
     * 3. 工程应用场景：
     *    - 资源分配：在约束条件下的最优资源选择
     *    - 任务调度：避免冲突任务的最优调度
     *    - 数据清理：选择最优的数据清理策略
     * 
     * 4. 面试技巧：
     *    - 先识别问题本质：转化为已知问题（打家劫舍）
     *    - 讨论不同情况下的优化策略
     *    - 考虑边界情况和特殊输入
     */
    
    /*
     * 相关题目扩展:
     * 1. LeetCode 740. 删除并获得点数 - https://leetcode.cn/problems/delete-and-earn/
     * 2. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
     * 3. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
     * 4. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
     * 5. LeetCode 256. 粉刷房子 - https://leetcode.cn/problems/paint-house/
     * 6. LeetCode 276. 栅栏涂色 - https://leetcode.cn/problems/paint-fence/
     * 7. LeetCode 2560. 打家劫舍 IV - https://leetcode.cn/problems/house-robber-iv/
     * 8. LeetCode 1388. 3n 块披萨 - https://leetcode.cn/problems/pizza-with-3n-slices/
     * 9. LeetCode 2132. 用邮票贴满网格图 - https://leetcode.cn/problems/stamping-the-grid/
     * 10. LeetCode 2140. 解决智力问题 - https://leetcode.cn/problems/solving-questions-with-brainpower/
     */
}