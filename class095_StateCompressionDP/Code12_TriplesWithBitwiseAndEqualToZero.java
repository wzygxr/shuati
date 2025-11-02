package class080;

// 按位与为零的三元组 (Triples with Bitwise AND Equal To Zero)
// 给你一个整数数组 nums ，返回其中按位与三元组的数目。
// 按位与三元组是由下标 (i, j, k) 组成的三元组，并满足 nums[i] & nums[j] & nums[k] == 0。
// 测试链接 : https://leetcode.cn/problems/triples-with-bitwise-and-equal-to-zero/
public class Code12_TriplesWithBitwiseAndEqualToZero {

    // 使用状态压缩动态规划解决按位与三元组问题
    // 核心思想：先计算所有两个数的按位与结果，再枚举第三个数
    // 时间复杂度: O(3^m + n^2)，其中m是最大数的位数(本题中为16)
    // 空间复杂度: O(2^m)
    public static int countTriplets(int[] nums) {
        // cnt[mask] 表示有多少个数与mask的按位与结果为0
        int[] cnt = new int[1 << 16];
        
        // 枚举所有可能的两个数的按位与结果
        for (int x : nums) {
            for (int y : nums) {
                // 统计两个数按位与的结果
                cnt[x & y]++;
            }
        }
        
        int result = 0;
        
        // 枚举第三个数
        for (int z : nums) {
            // 枚举所有与z按位与结果为0的数
            for (int mask = 0; mask < (1 << 16); mask++) {
                // 如果mask与z的按位与结果为0
                if ((mask & z) == 0) {
                    // 累加满足条件的三元组数量
                    result += cnt[mask];
                }
            }
        }
        
        return result;
    }

}