#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 跳跃游戏 II
// 给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]
// 每个元素 nums[i] 表示从索引 i 向前跳转的最大长度
// 返回到达 nums[n - 1] 的最小跳跃次数
// 测试链接 : https://leetcode.cn/problems/jump-game-ii/

class Solution {
public:
    /*
     * 贪心算法解法
     * 
     * 核心思想：
     * 1. 使用贪心策略，每次都尽可能跳到最远的位置
     * 2. 维护当前能到达的最远位置和下一步能到达的最远位置
     * 3. 当遍历到当前能到达的最远位置时，必须进行一次跳跃
     * 
     * 时间复杂度：O(n) - 只需要遍历数组一次
     * 空间复杂度：O(1) - 只使用了常数级别的额外空间
     * 
     * 为什么这是最优解？
     * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
     * 2. 通过数学归纳法可以证明这种策略能得到全局最优解
     * 3. 无法在更少的时间内完成，因为至少需要遍历一遍数组
     * 
     * 工程化考虑：
     * 1. 边界条件处理：空数组、单元素数组
     * 2. 异常处理：题目保证可以到达终点，无需额外检查
     * 3. 可读性：变量命名清晰，注释详细
     * 
     * 算法调试技巧：
     * 1. 可以通过打印每一步的curEnd和curFarthest来观察跳跃过程
     * 2. 用断言验证中间结果是否符合预期
     */

    int jump(vector<int>& nums) {
        // 边界条件：如果数组长度小于等于1，不需要跳跃
        if (nums.size() <= 1) {
            return 0;
        }

        // jumps: 跳跃次数
        int jumps = 0;
        
        // curEnd: 当前跳跃能到达的最远位置
        int curEnd = 0;
        
        // curFarthest: 下一次跳跃能到达的最远位置
        int curFarthest = 0;

        // 遍历数组，注意不需要处理最后一个元素
        for (int i = 0; i < (int)nums.size() - 1; i++) {
            // 更新下一次跳跃能到达的最远位置
            curFarthest = max(curFarthest, i + nums[i]);

            // 如果遍历到当前跳跃能到达的最远位置
            if (i == curEnd) {
                // 必须进行一次跳跃
                jumps++;
                // 更新当前跳跃能到达的最远位置
                curEnd = curFarthest;
                
                // 如果已经能到达终点，提前结束
                if (curEnd >= (int)nums.size() - 1) {
                    break;
                }
            }
        }

        return jumps;
    }
};

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: [2,3,1,1,4] -> 2
    vector<int> nums1 = {2, 3, 1, 1, 4};
    cout << "测试用例1: ";
    for (int num : nums1) cout << num << " ";
    cout << "\n预期结果: 2, 实际结果: " << solution.jump(nums1) << endl;

    // 测试用例2: [2,3,0,1,4] -> 2
    vector<int> nums2 = {2, 3, 0, 1, 4};
    cout << "测试用例2: ";
    for (int num : nums2) cout << num << " ";
    cout << "\n预期结果: 2, 实际结果: " << solution.jump(nums2) << endl;

    // 测试用例3: [1,1,1,1] -> 3
    vector<int> nums3 = {1, 1, 1, 1};
    cout << "测试用例3: ";
    for (int num : nums3) cout << num << " ";
    cout << "\n预期结果: 3, 实际结果: " << solution.jump(nums3) << endl;
    
    // 测试用例4: [1] -> 0
    vector<int> nums4 = {1};
    cout << "测试用例4: ";
    for (int num : nums4) cout << num << " ";
    cout << "\n预期结果: 0, 实际结果: " << solution.jump(nums4) << endl;
}

int main() {
    test();
    return 0;
}