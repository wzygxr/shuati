#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 预测赢家 (LeetCode 486)
// 题目来源：LeetCode 486. Predict the Winner - https://leetcode.com/problems/predict-the-winner/
// 题目描述：给定一个表示分数的非负整数数组。玩家1从数组任意一端拿一个分数，随后玩家2继续从剩余数组任意一端拿分数，
// 然后玩家1继续拿，以此类推。一个玩家每次只能拿一个分数，分数被拿完后游戏结束。最终获得分数总和最多的玩家获胜。
// 如果两个玩家分数相等，那么玩家1仍为赢家。假设每个玩家都发挥最佳水平，判断玩家1是否可以成为赢家。
//
// 算法核心思想：
// 1. 动态规划：dp[i][j]表示从i到j的子数组中，先手玩家能获得的最大净胜分数
// 2. 状态转移：dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
// 3. 最终结果：dp[0][n-1] >= 0 表示玩家1可以获胜
//
// 时间复杂度分析：
// 1. 时间复杂度：O(n^2) - 需要填充n*n的dp表
// 2. 空间复杂度：O(n^2) - 使用二维dp数组
//
// 工程化考量：
// 1. 异常处理：处理空数组和边界情况
// 2. 性能优化：使用动态规划避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的分数数组
class Code21_PredictTheWinnerLeetCode486 {
public:
    /**
     * 解决预测赢家问题
     * @param nums 分数数组
     * @return 玩家1是否可以成为赢家
     */
    static bool predictTheWinner(vector<int>& nums) {
        // 异常处理：处理空数组
        if (nums.empty()) {
            return true; // 空数组，玩家1获胜（规则规定）
        }
        
        int n = nums.size();
        
        // 创建dp数组，dp[i][j]表示从i到j的子数组中，先手玩家能获得的最大净胜分数
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化对角线：当只有一个元素时，先手玩家获得该分数
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        
        // 填充dp表，从长度为2的子数组开始
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 状态转移方程：
                // 先手玩家可以选择拿左边或右边的分数
                // 如果拿左边，净胜分数 = nums[i] - 后手玩家在[i+1,j]区间的最优解
                // 如果拿右边，净胜分数 = nums[j] - 后手玩家在[i,j-1]区间的最优解
                dp[i][j] = max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
            }
        }
        
        // 如果整个区间的净胜分数大于等于0，玩家1获胜
        return dp[0][n - 1] >= 0;
    }
    
    /**
     * 优化版本：使用一维数组降低空间复杂度
     * 时间复杂度：O(n^2)，空间复杂度：O(n)
     */
    static bool predictTheWinnerOptimized(vector<int>& nums) {
        if (nums.empty()) {
            return true;
        }
        
        int n = nums.size();
        vector<int> dp(n, 0);
        
        // 初始化：复制分数值
        for (int i = 0; i < n; i++) {
            dp[i] = nums[i];
        }
        
        // 从后向前填充dp数组
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i] = max(nums[i] - dp[i + 1], nums[j] - dp[i]);
            }
        }
        
        return dp[0] >= 0;
    }
    
    /**
     * 递归+记忆化搜索版本
     * 时间复杂度：O(n^2)，空间复杂度：O(n^2)
     */
    static bool predictTheWinnerMemo(vector<int>& nums) {
        if (nums.empty()) {
            return true;
        }
        
        int n = nums.size();
        vector<vector<int>> memo(n, vector<int>(n, INT_MIN));
        return dfs(nums, 0, n - 1, memo) >= 0;
    }
    
private:
    static int dfs(vector<int>& nums, int i, int j, vector<vector<int>>& memo) {
        // 边界条件：当i > j时，没有分数可拿
        if (i > j) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[i][j] != INT_MIN) {
            return memo[i][j];
        }
        
        // 当前玩家可以选择拿左边或右边的分数
        int takeLeft = nums[i] - dfs(nums, i + 1, j, memo);
        int takeRight = nums[j] - dfs(nums, i, j - 1, memo);
        
        // 选择最优策略
        memo[i][j] = max(takeLeft, takeRight);
        return memo[i][j];
    }
};

// 测试函数
int main() {
    // 测试用例1：标准情况，玩家1失败
    vector<int> nums1 = {1, 5, 2};
    cout << "测试用例1 [1,5,2]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(nums1) << endl; // 应输出0(false)
    
    // 测试用例2：玩家1获胜
    vector<int> nums2 = {1, 5, 233, 7};
    cout << "测试用例2 [1,5,233,7]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(nums2) << endl; // 应输出1(true)
    
    // 测试用例3：玩家1获胜
    vector<int> nums3 = {1, 1};
    cout << "测试用例3 [1,1]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(nums3) << endl; // 应输出1(true)
    
    // 测试用例4：玩家1失败
    vector<int> nums4 = {1, 3, 1};
    cout << "测试用例4 [1,3,1]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(nums4) << endl; // 应输出0(false)
    
    // 测试用例5：单元素数组
    vector<int> nums5 = {5};
    cout << "测试用例5 [5]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(nums5) << endl; // 应输出1(true)
    
    // 验证优化版本
    cout << "优化版本测试:" << endl;
    cout << "测试用例1 [1,5,2]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinnerOptimized(nums1) << endl;
    cout << "测试用例2 [1,5,233,7]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinnerOptimized(nums2) << endl;
    
    // 验证记忆化搜索版本
    cout << "记忆化搜索版本测试:" << endl;
    cout << "测试用例1 [1,5,2]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinnerMemo(nums1) << endl;
    cout << "测试用例2 [1,5,233,7]: " << Code21_PredictTheWinnerLeetCode486::predictTheWinnerMemo(nums2) << endl;
    
    // 边界测试：空数组
    vector<int> emptyNums;
    cout << "空数组测试: " << Code21_PredictTheWinnerLeetCode486::predictTheWinner(emptyNums) << endl; // 应输出1(true)
    
    return 0;
}