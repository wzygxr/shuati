// 跳跃游戏
// 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
// 数组中的每个元素代表你在该位置可以跳跃的最大长度。
// 判断你是否能够到达最后一个下标。
// 测试链接 : https://leetcode.cn/problems/jump-game/

/**
 * 跳跃游戏
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 维护一个变量maxReach表示当前能到达的最远位置
 * 2. 遍历数组，对于每个位置i：
 *    - 如果i > maxReach，说明无法到达位置i，直接返回false
 *    - 否则更新maxReach = max(maxReach, i + nums[i])
 * 3. 如果遍历完成，说明能到达最后一个位置，返回true
 * 
 * 正确性分析：
 * 1. 如果能到达某个位置，那一定能到达它前面的所有位置
 * 2. 我们只需要关注能到达的最远位置即可
 * 3. 如果最远位置超过了最后一个下标，就能到达
 * 
 * 时间复杂度：O(n) - 只需要遍历数组一次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param nums 非负整数数组，表示每个位置可以跳跃的最大长度
 * @param numsSize 数组长度
 * @return 是否能到达最后一个下标
 */
bool canJump(int nums[], int numsSize) {
    int maxReach = 0;  // 当前能到达的最远位置
    
    // 遍历数组
    for (int i = 0; i < numsSize; i++) {
        // 如果当前位置无法到达，直接返回false
        if (i > maxReach) {
            return false;
        }
        
        // 更新能到达的最远位置
        int currentReach = i + nums[i];
        if (currentReach > maxReach) {
            maxReach = currentReach;
        }
        
        // 如果已经能到达最后一个位置，提前返回true
        if (maxReach >= numsSize - 1) {
            return true;
        }
    }
    
    // 遍历完成，说明能到达最后一个位置
    return true;
}