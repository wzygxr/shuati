// 跳跃游戏II
// 给定一个长度为n的整数数组nums
// 你初始在0下标，nums[i]表示你可以从i下标往右跳的最大距离
// 比如，nums[0] = 3
// 表示你可以从0下标去往：1下标、2下标、3下标
// 你达到i下标后，可以根据nums[i]的值继续往右跳
// 返回你到达n-1下标的最少跳跃次数
// 测试用例可以保证一定能到达
// 测试链接 : https://leetcode.cn/problems/jump-game-ii/

/**
 * 跳跃游戏 II - 使用贪心算法解决
 * 
 * 算法思路：
 * 使用贪心策略，每次尽可能跳得更远。维护两个变量：
 * - cur: 当前步数内能到达的最远位置
 * - next: 下一步能到达的最远位置
 * - ans: 跳跃次数
 * 
 * 遍历数组，当当前位置超过当前步数能到达的最远位置时，
 * 就必须增加跳跃次数，并更新当前能到达的最远位置。
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(1) - 只使用了常数额外空间
 * 
 * 是否最优解：是。这是跳跃游戏问题的最优解法之一。
 * 
 * 适用场景：
 * 1. 需要找到从数组起点到终点的最少步数
 * 2. 每个位置的值表示能跳跃的最大距离
 * 
 * 相关题目：
 * 1. LeetCode 55. 跳跃游戏 - 判断是否能到达最后一个位置
 * 2. LeetCode 1306. 跳跃游戏 III - 可以前后跳跃
 * 3. LeetCode 1345. 跳跃游戏 IV - 基于值的跳跃
 * 4. LeetCode 1696. 跳跃游戏 VI - 带权值的最大得分跳跃
 * 5. 牛客网 NC48 跳跃游戏 - 与LeetCode 55相同
 * 6. LintCode 116. 跳跃游戏 - 与LeetCode 55相同
 * 7. LintCode 117. 跳跃游戏 II - 与LeetCode 45相同
 * 8. HackerRank - Jumping on the Clouds - 简化版跳跃游戏
 * 9. CodeChef - JUMP - 类似跳跃游戏的变种
 * 10. AtCoder ABC161D - Lunlun Number - BFS搜索相关
 * 11. Codeforces 1324B - Yet Another Palindrome Problem - 子序列相关
 * 12. SPOJ AIBOHP - Aibohphobia - 回文相关动态规划
 * 13. POJ 1513 - Scheduling Lectures - 区间调度相关
 * 14. HDU 2037 - 今年暑假不AC - 经典区间调度贪心问题
 * 15. USACO 2014 January Gold - Ski Course Rating - 图论相关
 * 16. 洛谷 P1579 - 哥德巴赫猜想 - 数论相关
 * 17. Project Euler 357 - Prime generating integers - 数论相关
 * 18. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
 */
int jump(int arr[], int n) {
    // 当前步以内，最右到哪
    int cur = 0;
    // 如果再一步，(当前步+1)以内，最右到哪
    int next = 0;
    // 一共需要跳几步
    int ans = 0;
    for (int i = 0; i < n; i++) {
        // 来到i下标
        // cur包括了i所在的位置，不用付出额外步数
        // cur没有包括i所在的位置，需要付出额外步数
        if (cur < i) {
            ans++;
            cur = next;
        }
        int temp = i + arr[i];
        if (next < temp) {
            next = temp;
        }
    }
    return ans;
}

// 测试函数，返回结果用于验证
int test_jump() {
    // 测试用例1: 基本情况
    int nums1[] = {2, 3, 1, 1, 4};
    int size1 = 5;
    int result1 = jump(nums1, size1);
    
    // 测试用例2: 最少步数情况
    int nums2[] = {2, 3, 0, 1, 4};
    int size2 = 5;
    int result2 = jump(nums2, size2);
    
    // 测试用例3: 单个元素
    int nums3[] = {0};
    int size3 = 1;
    int result3 = jump(nums3, size3);
    
    // 测试用例4: 两个元素
    int nums4[] = {1, 1};
    int size4 = 2;
    int result4 = jump(nums4, size4);
    
    // 返回最后一个测试用例的结果
    return result4;
}

// 主函数，用于编译和运行测试
int main() {
    return test_jump();
}