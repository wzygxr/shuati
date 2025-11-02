package class090;

// 跳跃游戏 II
// 给你一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
// 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度。
// 返回到达 nums[n - 1] 的最小跳跃次数。生成的测试用例可以到达 nums[n - 1]。
// 测试链接: https://leetcode.cn/problems/jump-game-ii/
public class Code11_JumpGameII {

    /**
     * 跳跃游戏 II 问题的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，在当前能到达的范围内，选择下一步能跳得最远的位置
     * 2. 维护三个变量：
     *    - maxReach: 当前能到达的最远位置
     *    - end: 当前跳跃范围的边界
     *    - jumps: 跳跃次数
     * 3. 遍历数组，当到达当前跳跃范围边界时，必须进行下一次跳跃
     * 
     * 贪心策略的正确性：
     * 我们并不关心具体是如何跳到某个位置的，只关心在当前能到达的范围内，
     * 下一步能跳到的最远位置。这样可以保证跳跃次数最少。
     * 
     * 时间复杂度：O(n)，只需要遍历数组一次
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param nums 非负整数数组，表示每个位置可以跳跃的最大长度
     * @return 到达最后一个下标的最小跳跃次数
     */
    public static int jump(int[] nums) {
        // 边界条件处理：如果数组为空或只有一个元素，则不需要跳跃
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        // 1. 初始化变量
        int maxReach = 0;  // 当前能到达的最远位置
        int end = 0;       // 当前跳跃范围的边界
        int jumps = 0;     // 跳跃次数

        // 2. 遍历数组，注意只需要遍历到倒数第二个元素
        for (int i = 0; i < nums.length - 1; i++) {
            // 3. 更新能到达的最远位置
            maxReach = Math.max(maxReach, i + nums[i]);

            // 4. 如果到达当前跳跃范围边界，必须进行下一次跳跃
            if (i == end) {
                jumps++;           // 跳跃次数增加
                end = maxReach;    // 更新跳跃范围边界

                // 5. 提前优化：如果已经能到达最后一个位置，直接返回
                if (end >= nums.length - 1) {
                    break;
                }
            }
        }

        // 6. 返回最小跳跃次数
        return jumps;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: nums = [2,3,1,1,4]
        // 输出: 2
        // 解释: 跳到最后一个位置的最小跳跃数是 2。从下标为 0 跳到下标为 1 的位置，跳 1 步，
        //       然后跳 3 步到达数组的最后一个位置。
        int[] nums1 = {2, 3, 1, 1, 4};
        System.out.println("测试用例1结果: " + jump(nums1)); // 期望输出: 2

        // 测试用例2
        // 输入: nums = [2,3,0,1,4]
        // 输出: 2
        int[] nums2 = {2, 3, 0, 1, 4};
        System.out.println("测试用例2结果: " + jump(nums2)); // 期望输出: 2

        // 测试用例3：边界情况
        // 输入: nums = [1]
        // 输出: 0
        int[] nums3 = {1};
        System.out.println("测试用例3结果: " + jump(nums3)); // 期望输出: 0

        // 测试用例4：单个元素
        // 输入: nums = [0]
        // 输出: 0
        int[] nums4 = {0};
        System.out.println("测试用例4结果: " + jump(nums4)); // 期望输出: 0

        // 测试用例5：复杂情况
        // 输入: nums = [1,1,1,1]
        // 输出: 3
        int[] nums5 = {1, 1, 1, 1};
        System.out.println("测试用例5结果: " + jump(nums5)); // 期望输出: 3
    }
}