package class086;

// LeetCode 1964. 找出到每个位置为止最长的有效障碍赛跑路线
// 你打算构建一些障碍赛跑路线。给你一个下标从 0 开始的整数数组 obstacles ，
// 数组长度为 n ，其中 obstacles[i] 表示第 i 个障碍的高度。
// 对于每个介于 0 和 n - 1 之间（包含 0 和 n - 1）的下标 i ，
// 在满足下述条件的前提下，请你找出 obstacles 能构成的最长障碍路线的长度：
// 你可以选择下标介于 0 到 i 之间（包含 0 和 i）的任意个障碍。
// 在这条路线中，必须包含第 i 个障碍。
// 你必须按障碍在 obstacles 中的出现顺序布置这些障碍。
// 除此之外，路线中每个障碍的高度都必须和前一个障碍相同或更高。
// 返回长度为 n 的答案数组 ans ，其中 ans[i] 是上面所述的下标 i 对应的最长障碍赛跑路线的长度。
// 测试链接 : https://leetcode.cn/problems/find-the-longest-valid-obstacle-course-at-each-position/

public class LeetCode1964_Find_the_Longest_Valid_Obstacle_Course_at_Each_Position {
    
    /*
     * 算法详解：找出到每个位置为止最长的有效障碍赛跑路线（LeetCode 1964）
     * 
     * 问题描述：
     * 你打算构建一些障碍赛跑路线。给你一个下标从 0 开始的整数数组 obstacles ，
     * 数组长度为 n ，其中 obstacles[i] 表示第 i 个障碍的高度。
     * 对于每个介于 0 和 n - 1 之间（包含 0 和 n - 1）的下标 i ，
     * 在满足下述条件的前提下，请你找出 obstacles 能构成的最长障碍路线的长度：
     * 1. 你可以选择下标介于 0 到 i 之间（包含 0 和 i）的任意个障碍。
     * 2. 在这条路线中，必须包含第 i 个障碍。
     * 3. 你必须按障碍在 obstacles 中的出现顺序布置这些障碍。
     * 4. 除此之外，路线中每个障碍的高度都必须和前一个障碍相同或更高。
     * 返回长度为 n 的答案数组 ans ，其中 ans[i] 是上面所述的下标 i 对应的最长障碍赛跑路线的长度。
     * 
     * 算法思路：
     * 这是LIS问题的变种，需要计算每个位置结尾的最长非递减子序列长度。
     * 1. 使用贪心+二分查找的方法
     * 2. 维护一个ends数组，ends[i]表示长度为i+1的非递减子序列的最小末尾元素
     * 3. 对于每个位置，计算以该位置结尾的最长非递减子序列长度
     * 
     * 时间复杂度分析：
     * 1. 遍历数组：O(n)
     * 2. 二分查找：O(log n)
     * 3. 总体时间复杂度：O(n log n)
     * 
     * 空间复杂度分析：
     * 1. ends数组：O(n)
     * 2. 结果数组：O(n)
     * 3. 总体空间复杂度：O(n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入数组是否为空
     * 2. 边界处理：处理空数组和单元素数组的情况
     * 3. 性能优化：使用二分查找将时间复杂度从O(n^2)优化到O(n log n)
     * 
     * 极端场景验证：
     * 1. 输入数组为空的情况
     * 2. 输入数组只有一个元素的情况
     * 3. 输入数组元素全部相同的情况
     * 4. 输入数组严格递增的情况
     * 5. 输入数组严格递减的情况
     */
    
    public static int[] longestObstacleCourseAtEachPosition(int[] obstacles) {
        // 异常处理：检查输入数组是否为空
        if (obstacles == null || obstacles.length == 0) {
            return new int[0];
        }
        
        int n = obstacles.length;
        
        // 特殊情况：只有一个元素
        if (n == 1) {
            return new int[]{1};
        }
        
        // 结果数组
        int[] result = new int[n];
        
        // ends[i] 表示长度为i+1的非递减子序列的最小末尾元素
        int[] ends = new int[n];
        // 当前最长非递减子序列的长度
        int len = 0;
        
        // 遍历原数组
        for (int i = 0; i < n; i++) {
            // 使用二分查找找到obstacles[i]在ends数组中的合适位置
            int index = binarySearch(ends, len, obstacles[i]);
            
            // 如果index等于len，说明obstacles[i]比所有元素都大，需要扩展ends数组
            if (index == len) {
                len++;
            }
            
            // 更新ends数组
            ends[index] = obstacles[i];
            
            // 记录以当前位置结尾的最长非递减子序列长度
            result[i] = index + 1;
        }
        
        return result;
    }
    
    // 二分查找：在ends数组中找到第一个大于target的位置
    private static int binarySearch(int[] ends, int len, int target) {
        int left = 0, right = len;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            // 注意这里是大于target，因为我们要找非递减子序列（允许相等）
            if (ends[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    // 动态规划解法：时间复杂度O(n^2)，空间复杂度O(n)
    public static int[] longestObstacleCourseAtEachPositionDP(int[] obstacles) {
        // 异常处理：检查输入数组是否为空
        if (obstacles == null || obstacles.length == 0) {
            return new int[0];
        }
        
        int n = obstacles.length;
        
        // 特殊情况：只有一个元素
        if (n == 1) {
            return new int[]{1};
        }
        
        // 结果数组
        int[] result = new int[n];
        
        // dp[i] 表示以obstacles[i]结尾的最长非递减子序列长度
        int[] dp = new int[n];
        
        // 填充dp数组和结果数组
        for (int i = 0; i < n; i++) {
            dp[i] = 1; // 每个元素自身构成长度为1的子序列
            for (int j = 0; j < i; j++) {
                // 如果obstacles[j] <= obstacles[i]，可以将obstacles[i]接在obstacles[j]后面
                if (obstacles[j] <= obstacles[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            result[i] = dp[i];
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] obstacles1 = {1,2,3,2};
        int[] result1 = longestObstacleCourseAtEachPosition(obstacles1);
        System.out.print("Test 1 (Binary Search method): ");
        for (int i = 0; i < result1.length; i++) {
            System.out.print(result1[i] + " ");
        }
        System.out.println();
        
        int[] result1DP = longestObstacleCourseAtEachPositionDP(obstacles1);
        System.out.print("Test 1 (DP method): ");
        for (int i = 0; i < result1DP.length; i++) {
            System.out.print(result1DP[i] + " ");
        }
        System.out.println();
        // 期望输出: [1,2,3,3]
        
        // 测试用例2
        int[] obstacles2 = {2,2,1};
        int[] result2 = longestObstacleCourseAtEachPosition(obstacles2);
        System.out.print("Test 2 (Binary Search method): ");
        for (int i = 0; i < result2.length; i++) {
            System.out.print(result2[i] + " ");
        }
        System.out.println();
        
        int[] result2DP = longestObstacleCourseAtEachPositionDP(obstacles2);
        System.out.print("Test 2 (DP method): ");
        for (int i = 0; i < result2DP.length; i++) {
            System.out.print(result2DP[i] + " ");
        }
        System.out.println();
        // 期望输出: [1,2,1]
        
        // 测试用例3
        int[] obstacles3 = {3,1,5,6,4,2};
        int[] result3 = longestObstacleCourseAtEachPosition(obstacles3);
        System.out.print("Test 3 (Binary Search method): ");
        for (int i = 0; i < result3.length; i++) {
            System.out.print(result3[i] + " ");
        }
        System.out.println();
        
        int[] result3DP = longestObstacleCourseAtEachPositionDP(obstacles3);
        System.out.print("Test 3 (DP method): ");
        for (int i = 0; i < result3DP.length; i++) {
            System.out.print(result3DP[i] + " ");
        }
        System.out.println();
        // 期望输出: [1,1,2,3,2,2]
        
        // 测试用例4
        int[] obstacles4 = {};
        int[] result4 = longestObstacleCourseAtEachPosition(obstacles4);
        System.out.print("Test 4 (Binary Search method): ");
        for (int i = 0; i < result4.length; i++) {
            System.out.print(result4[i] + " ");
        }
        System.out.println();
        
        int[] result4DP = longestObstacleCourseAtEachPositionDP(obstacles4);
        System.out.print("Test 4 (DP method): ");
        for (int i = 0; i < result4DP.length; i++) {
            System.out.print(result4DP[i] + " ");
        }
        System.out.println();
        // 期望输出: (空数组)
        
        // 测试用例5
        int[] obstacles5 = {1};
        int[] result5 = longestObstacleCourseAtEachPosition(obstacles5);
        System.out.print("Test 5 (Binary Search method): ");
        for (int i = 0; i < result5.length; i++) {
            System.out.print(result5[i] + " ");
        }
        System.out.println();
        
        int[] result5DP = longestObstacleCourseAtEachPositionDP(obstacles5);
        System.out.print("Test 5 (DP method): ");
        for (int i = 0; i < result5DP.length; i++) {
            System.out.print(result5DP[i] + " ");
        }
        System.out.println();
        // 期望输出: [1]
    }
}