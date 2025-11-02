// 跳跃游戏 II
// 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 你的目标是使用最少的跳跃次数到达数组的最后一个下标。
// 测试链接 : https://leetcode.cn/problems/jump-game-ii/

/**
 * 跳跃游戏 II
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 维护三个变量：
 *    - jumps：跳跃次数
 *    - currentEnd：当前跳跃能到达的最远位置
 *    - farthest：下一跳能到达的最远位置
 * 2. 遍历数组（不包括最后一个元素）：
 *    - 更新farthest = max(farthest, i + nums[i])
 *    - 如果到达currentEnd，说明需要进行下一跳
 *    - 增加跳跃次数，更新currentEnd为farthest
 * 
 * 正确性分析：
 * 1. 我们不需要知道具体在哪一跳，只需要知道最少跳跃次数
 * 2. 在每一跳中，我们尽可能跳得更远
 * 3. 当到达当前跳的边界时，必须进行下一跳
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param nums 非负整数数组
 * @param numsSize 数组长度
 * @return 最少跳跃次数
 */
int jump(int nums[], int numsSize) {
    // 边界情况处理
    if (numsSize <= 1) {
        return 0;
    }
    
    int jumps = 0;        // 跳跃次数
    int currentEnd = 0;   // 当前跳跃能到达的最远位置
    int farthest = 0;     // 下一跳能到达的最远位置
    
    // 遍历数组（不包括最后一个元素）
    for (int i = 0; i < numsSize - 1; i++) {
        // 更新下一跳能到达的最远位置
        int newFarthest = i + nums[i];
        if (newFarthest > farthest) {
            farthest = newFarthest;
        }
        
        // 如果到达当前跳的边界，必须进行下一跳
        if (i == currentEnd) {
            jumps++;
            currentEnd = farthest;
            
            // 如果已经能到达最后一个位置，提前结束
            if (currentEnd >= numsSize - 1) {
                break;
            }
        }
    }
    
    return jumps;
}