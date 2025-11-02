/**
 * 摆动序列
 * 
 * 题目描述：
 * 如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
 * 第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
 * 给定一个整数序列，返回作为摆动序列的最长子序列的长度。
 * 
 * 来源：LeetCode 376
 * 链接：https://leetcode.cn/problems/wiggle-subsequence/
 * 
 * 算法思路：
 * 使用贪心算法：
 * 1. 遍历数组，记录当前趋势（上升、下降或无趋势）
 * 2. 当趋势发生变化时，增加摆动序列长度
 * 3. 跳过不影响趋势变化的数字
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(1) - 只使用常数空间
 * 
 * 关键点分析：
 * - 贪心策略：选择趋势变化的转折点
 * - 趋势判断：比较相邻数字的大小关系
 * - 边界处理：处理重复数字和边界情况
 * 
 * 工程化考量：
 * - 输入验证：检查数组是否为空
 * - 性能优化：避免不必要的比较
 * - 可读性：清晰的变量命名和注释
 */
public class Code35_WiggleSubsequence {
    
    /**
     * 计算最长摆动子序列的长度
     * 
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    public static int wiggleMaxLength(int[] nums) {
        // 输入验证
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        if (nums.length < 2) {
            return nums.length;
        }
        
        int n = nums.length;
        int up = 1;   // 上升趋势的序列长度
        int down = 1; // 下降趋势的序列长度
        
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                // 当前是上升趋势
                up = down + 1;
            } else if (nums[i] < nums[i - 1]) {
                // 当前是下降趋势
                down = up + 1;
            }
            // 如果相等，保持原来的趋势不变
        }
        
        return Math.max(up, down);
    }
    
    /**
     * 另一种实现：状态机方法
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int wiggleMaxLengthStateMachine(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        if (nums.length < 2) {
            return nums.length;
        }
        
        int n = nums.length;
        int state = 0; // 0: 无趋势, 1: 上升, -1: 下降
        int count = 1; // 序列长度，至少包含第一个元素
        
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                if (state != 1) {
                    // 趋势从下降或无趋势变为上升
                    count++;
                    state = 1;
                }
            } else if (nums[i] < nums[i - 1]) {
                if (state != -1) {
                    // 趋势从上升或无趋势变为下降
                    count++;
                    state = -1;
                }
            }
            // 如果相等，保持状态不变
        }
        
        return count;
    }
    
    /**
     * 动态规划解法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static int wiggleMaxLengthDP(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为空");
        }
        if (nums.length < 2) {
            return nums.length;
        }
        
        int n = nums.length;
        int[] up = new int[n];   // 以i结尾的上升摆动序列长度
        int[] down = new int[n]; // 以i结尾的下降摆动序列长度
        up[0] = 1;
        down[0] = 1;
        
        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                up[i] = down[i - 1] + 1;
                down[i] = down[i - 1];
            } else if (nums[i] < nums[i - 1]) {
                down[i] = up[i - 1] + 1;
                up[i] = up[i - 1];
            } else {
                up[i] = up[i - 1];
                down[i] = down[i - 1];
            }
        }
        
        return Math.max(up[n - 1], down[n - 1]);
    }
    
    /**
     * 验证函数：检查序列是否为摆动序列
     */
    public static boolean isWiggleSequence(int[] nums) {
        if (nums == null || nums.length < 2) {
            return true;
        }
        
        int n = nums.length;
        int prevDiff = nums[1] - nums[0];
        
        for (int i = 2; i < n; i++) {
            int diff = nums[i] - nums[i - 1];
            if (prevDiff * diff >= 0) {
                // 趋势没有变化或相等
                return false;
            }
            prevDiff = diff;
        }
        
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: [1,7,4,9,2,5] -> 6
        int[] nums1 = {1,7,4,9,2,5};
        System.out.println("测试用例1: " + java.util.Arrays.toString(nums1));
        System.out.println("方法1结果: " + wiggleMaxLength(nums1)); // 6
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums1)); // 6
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums1)); // 6
        System.out.println("验证: " + isWiggleSequence(nums1)); // true
        
        // 测试用例2: [1,17,5,10,13,15,10,5,16,8] -> 7
        int[] nums2 = {1,17,5,10,13,15,10,5,16,8};
        System.out.println("\n测试用例2: " + java.util.Arrays.toString(nums2));
        System.out.println("方法1结果: " + wiggleMaxLength(nums2)); // 7
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums2)); // 7
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums2)); // 7
        
        // 测试用例3: [1,2,3,4,5,6,7,8,9] -> 2
        int[] nums3 = {1,2,3,4,5,6,7,8,9};
        System.out.println("\n测试用例3: " + java.util.Arrays.toString(nums3));
        System.out.println("方法1结果: " + wiggleMaxLength(nums3)); // 2
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums3)); // 2
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums3)); // 2
        
        // 测试用例4: [3,3,3,2,5] -> 3
        int[] nums4 = {3,3,3,2,5};
        System.out.println("\n测试用例4: " + java.util.Arrays.toString(nums4));
        System.out.println("方法1结果: " + wiggleMaxLength(nums4)); // 3
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums4)); // 3
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums4)); // 3
        
        // 测试用例5: [1] -> 1
        int[] nums5 = {1};
        System.out.println("\n测试用例5: " + java.util.Arrays.toString(nums5));
        System.out.println("方法1结果: " + wiggleMaxLength(nums5)); // 1
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums5)); // 1
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums5)); // 1
        
        // 边界测试：空数组
        int[] nums6 = {};
        System.out.println("\n测试用例6: " + java.util.Arrays.toString(nums6));
        System.out.println("方法1结果: " + wiggleMaxLength(nums6)); // 0
        System.out.println("方法2结果: " + wiggleMaxLengthStateMachine(nums6)); // 0
        System.out.println("方法3结果: " + wiggleMaxLengthDP(nums6)); // 0
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int n = 10000;
        int[] nums = new int[n];
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(1000);
        }
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        int result1 = wiggleMaxLength(nums);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果: " + result1);
        
        long startTime2 = System.currentTimeMillis();
        int result2 = wiggleMaxLengthStateMachine(nums);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果: " + result2);
        
        long startTime3 = System.currentTimeMillis();
        int result3 = wiggleMaxLengthDP(nums);
        long endTime3 = System.currentTimeMillis();
        System.out.println("方法3执行时间: " + (endTime3 - startTime3) + "ms");
        System.out.println("结果: " + result3);
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（贪心算法）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 只需要遍历一次数组");
        System.out.println("  - 每个元素处理一次");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法2（状态机）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 只需要遍历一次数组");
        System.out.println("  - 状态转换效率高");
        System.out.println("- 空间复杂度: O(1)");
        System.out.println("  - 只使用常数空间");
        
        System.out.println("\n方法3（动态规划）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 只需要遍历一次数组");
        System.out.println("  - 动态规划表更新");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 需要两个数组存储状态");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 摆动序列的关键在于趋势变化点");
        System.out.println("2. 选择趋势变化的转折点可以最大化序列长度");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理空数组和边界情况");
        System.out.println("2. 性能优化：选择高效的算法");
        System.out.println("3. 可读性：清晰的算法逻辑和注释");
        System.out.println("4. 测试覆盖：全面的测试用例设计");
    }
}