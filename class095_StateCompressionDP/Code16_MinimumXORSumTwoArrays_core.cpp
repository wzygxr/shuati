// 最小XOR值路径 (Minimum XOR Sum of Two Arrays)
// 给你两个整数数组 nums1 和 nums2 ，它们的长度都为 n 。
// 你需要将 nums1 和 nums2 中的元素重新排列，使得 nums1[i] XOR nums2[j] 的结果之和最小。
// 返回重新排列后异或和的最小值。
// 测试链接 : https://leetcode.cn/problems/minimum-xor-sum-of-two-arrays/

#include <vector>
#include <climits>
using namespace std;

class Solution {
public:
    // 使用状态压缩动态规划解决最小XOR值路径问题
    // 核心思想：用二进制位表示nums2中已使用的元素，通过状态转移找到最小异或和
    // 时间复杂度: O(n^2 * 2^n)
    // 空间复杂度: O(2^n)
    int minimumXORSum(vector<int>& nums1, vector<int>& nums2) {
        int n = nums1.size();
        
        // dp[mask] 表示使用mask代表的nums2元素与nums1的前__builtin_popcount(mask)个元素匹配的最小异或和
        vector<int> dp(1 << n, INT_MAX);
        // 初始状态：不使用任何nums2元素，异或和为0
        dp[0] = 0;
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == INT_MAX) {
                continue;
            }
            
            // 计算已使用的nums2元素个数（即当前要匹配的nums1元素索引）
            int pos = __builtin_popcount(mask);
            
            // 枚举下一个要使用的nums2元素
            for (int i = 0; i < n; i++) {
                // 如果第i个nums2元素还未使用
                if ((mask & (1 << i)) == 0) {
                    // 计算新的状态和异或和
                    int new_mask = mask | (1 << i);
                    int xor_val = nums1[pos] ^ nums2[i];
                    // 更新状态：使用new_mask代表的元素能获得的最小异或和
                    dp[new_mask] = min(dp[new_mask], dp[mask] + xor_val);
                }
            }
        }
        
        // 返回使用所有nums2元素能获得的最小异或和
        return dp[(1 << n) - 1];
    }
};