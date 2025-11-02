// 分割等和子集 (Partition Equal Subset Sum)
// 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，
// 使得两个子集的元素和相等。
// 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/

class Solution {
public:
    // 使用动态规划解决分割等和子集问题
    // 核心思想：将问题转化为背包问题，判断是否能选出若干元素使其和等于总和的一半
    // 时间复杂度: O(n * sum)
    // 空间复杂度: O(sum)
    bool canPartition(int nums[], int size) {
        // 计算数组元素总和
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += nums[i];
        }
        
        // 如果总和是奇数，则无法分割成两个相等的子集
        if (sum % 2 != 0) {
            return false;
        }
        
        // 目标和为总和的一半
        int target = sum / 2;
        
        // dp[i] 表示是否能选出若干元素使其和等于i
        bool dp[20001];  // 假设最大和不超过20000
        for (int i = 0; i <= target; i++) {
            dp[i] = false;
        }
        // 初始状态：和为0总是可以实现（不选择任何元素）
        dp[0] = true;
        
        // 状态转移：枚举每个元素
        for (int i = 0; i < size; i++) {
            int num = nums[i];
            // 从后往前更新，避免重复使用同一元素
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        
        // 返回是否能选出若干元素使其和等于target
        return dp[target];
    }
};

/*
复杂度分析：
- 时间复杂度：O(n * sum)
  其中n是数组长度，sum是数组元素总和
  需要遍历n个元素，每个元素需要更新sum/2个状态

- 空间复杂度：O(sum)
  只需要一个大小为sum/2+1的dp数组

算法设计说明：
1. 问题转化：将分割等和子集问题转化为背包问题
   - 如果数组能分割成两个和相等的子集，那么每个子集的和都等于总和的一半
   - 问题转化为：能否选出若干元素使其和等于总和的一半

2. 状态定义：
   - dp[i] 表示是否能选出若干元素使其和等于i

3. 状态转移：
   - 对于每个元素num，更新dp数组：
     dp[i] = dp[i] || dp[i - num]
     表示和为i的状态可以通过不选择num或选择num达到

4. 初始化：
   - dp[0] = true，表示和为0总是可以实现（不选择任何元素）

5. 结果：
   - 返回dp[target]，表示是否能选出若干元素使其和等于target

这是本题的最优解，因为：
1. 时间复杂度已经是最优的，需要检查所有可能的组合
2. 空间复杂度已经优化到O(sum)，比二维DP更节省空间
3. 使用了滚动数组优化，避免了重复计算
*/