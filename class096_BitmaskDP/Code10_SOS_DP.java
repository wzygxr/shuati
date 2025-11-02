package class081.补充题目;

import java.util.Arrays;

// SOS DP (Sum Over Subsets) 专题
// SOS DP是一种用于处理子集相关问题的动态规划技术
// 题目来源: CodeForces, AtCoder等竞赛平台
//
// 核心思想:
// 对于每个mask，计算所有子集的某种聚合值
// 常用于解决子集和、子集计数、子集最大值等问题
//
// 时间复杂度: O(n * 2^n)
// 空间复杂度: O(2^n)

public class Code10_SOS_DP {
    
    // 基本SOS DP：计算每个mask的所有子集的元素和
    public static int[] sosDPBasic(int[] arr) {
        int n = arr.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 初始化：每个单独元素的mask
        for (int i = 0; i < n; i++) {
            dp[1 << i] = arr[i];
        }
        
        // SOS DP递推：按位包含
        for (int mask = 0; mask < size; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        
        return dp;
    }
    
    // 优化版SOS DP：使用更高效的递推顺序
    public static int[] sosDPOptimized(int[] arr) {
        int n = arr.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[1 << i] = arr[i];
        }
        
        // 优化递推：按位处理
        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < size; mask++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] += dp[mask ^ (1 << i)];
                }
            }
        }
        
        return dp;
    }
    
    // 计算每个mask的所有子集的最大值
    public static int[] sosDPMax(int[] arr) {
        int n = arr.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[1 << i] = arr[i];
        }
        
        // 计算子集最大值
        for (int mask = 0; mask < size; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] = Math.max(dp[mask], dp[mask ^ (1 << i)]);
                }
            }
        }
        
        return dp;
    }
    
    // 计算每个mask的所有超集的元素和（超集SOS）
    public static int[] superSetSOS(int[] arr) {
        int n = arr.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[1 << i] = arr[i];
        }
        
        // 超集SOS：按位处理，方向相反
        for (int i = 0; i < n; i++) {
            for (int mask = size - 1; mask >= 0; mask--) {
                if ((mask & (1 << i)) == 0) {
                    dp[mask] += dp[mask | (1 << i)];
                }
            }
        }
        
        return dp;
    }
    
    // CodeForces 165E 最大兼容数对 - SOS DP解法
    public static int[] compatibleNumbersSOS(int[] nums) {
        int n = nums.length;
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }
        
        int bits = 0;
        while ((1 << bits) <= maxVal) {
            bits++;
        }
        
        int size = 1 << bits;
        int[] dp = new int[size];
        Arrays.fill(dp, -1);
        
        // 初始化：标记存在的数字
        for (int i = 0; i < n; i++) {
            dp[nums[i]] = i;
        }
        
        // SOS DP：填充所有子集
        for (int i = 0; i < bits; i++) {
            for (int mask = 0; mask < size; mask++) {
                if ((mask & (1 << i)) != 0 && dp[mask] != -1) {
                    int subset = mask ^ (1 << i);
                    if (dp[subset] == -1) {
                        dp[subset] = dp[mask];
                    }
                }
            }
        }
        
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        for (int i = 0; i < n; i++) {
            int complement = (size - 1) ^ nums[i];
            if (dp[complement] != -1) {
                result[i] = dp[complement];
            }
        }
        
        return result;
    }
    
    // 子集和计数问题：计算有多少个子集的和等于target
    public static int subsetSumCount(int[] arr, int target) {
        int n = arr.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[1 << i] = arr[i];
        }
        
        // SOS DP计算所有子集和
        for (int mask = 0; mask < size; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] = dp[mask ^ (1 << i)] + arr[i];
                    break; // 每个mask只需要计算一次
                }
            }
        }
        
        // 统计等于target的子集数量
        int count = 0;
        for (int mask = 1; mask < size; mask++) {
            if (dp[mask] == target) {
                count++;
            }
        }
        
        return count;
    }
    
    // 最大独立集问题（图论应用）
    public static int maxIndependentSet(boolean[][] graph) {
        int n = graph.length;
        int size = 1 << n;
        int[] dp = new int[size];
        
        // 预处理：检查每个子集是否是独立集
        boolean[] isIndependent = new boolean[size];
        for (int mask = 0; mask < size; mask++) {
            boolean independent = true;
            for (int i = 0; i < n && independent; i++) {
                if ((mask & (1 << i)) != 0) {
                    for (int j = i + 1; j < n; j++) {
                        if ((mask & (1 << j)) != 0 && graph[i][j]) {
                            independent = false;
                            break;
                        }
                    }
                }
            }
            isIndependent[mask] = independent;
            if (independent) {
                dp[mask] = Integer.bitCount(mask);
            }
        }
        
        // SOS DP计算最大独立集
        for (int mask = 0; mask < size; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    dp[mask] = Math.max(dp[mask], dp[mask ^ (1 << i)]);
                }
            }
        }
        
        return dp[size - 1];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试基本SOS DP
        int[] arr = {1, 2, 3, 4};
        System.out.println("基本SOS DP测试:");
        int[] result1 = sosDPBasic(arr);
        System.out.println("数组: " + Arrays.toString(arr));
        System.out.println("子集和结果: " + Arrays.toString(result1));
        
        // 测试最大兼容数对
        int[] nums = {3, 1, 4, 2};
        System.out.println("\n最大兼容数对测试:");
        int[] result2 = compatibleNumbersSOS(nums);
        System.out.println("输入: " + Arrays.toString(nums));
        System.out.println("结果: " + Arrays.toString(result2));
        
        // 测试子集和计数
        int[] arr2 = {1, 2, 3, 4, 5};
        int target = 5;
        System.out.println("\n子集和计数测试:");
        System.out.println("数组: " + Arrays.toString(arr2));
        System.out.println("目标和: " + target);
        System.out.println("子集数量: " + subsetSumCount(arr2, target));
        
        // 测试最大独立集
        boolean[][] graph = {
            {false, true, true, false},
            {true, false, false, true},
            {true, false, false, true},
            {false, true, true, false}
        };
        System.out.println("\n最大独立集测试:");
        System.out.println("最大独立集大小: " + maxIndependentSet(graph));
    }
}

/*
 * C++ 实现
 */
// #include <iostream>
// #include <vector>
// #include <algorithm>
// #include <bitset>
// using namespace std;
// 
// // 基本SOS DP
// vector<int> sosDPBasic(vector<int>& arr) {
//     int n = arr.size();
//     int size = 1 << n;
//     vector<int> dp(size, 0);
//     
//     for (int i = 0; i < n; i++) {
//         dp[1 << i] = arr[i];
//     }
//     
//     for (int mask = 0; mask < size; mask++) {
//         for (int i = 0; i < n; i++) {
//             if (mask & (1 << i)) {
//                 dp[mask] += dp[mask ^ (1 << i)];
//             }
//         }
//     }
//     
//     return dp;
// }
// 
// // 最大兼容数对
// vector<int> compatibleNumbersSOS(vector<int>& nums) {
//     int n = nums.size();
//     int maxVal = *max_element(nums.begin(), nums.end());
//     
//     int bits = 0;
//     while ((1 << bits) <= maxVal) bits++;
//     
//     int size = 1 << bits;
//     vector<int> dp(size, -1);
//     
//     for (int i = 0; i < n; i++) {
//         dp[nums[i]] = i;
//     }
//     
//     for (int i = 0; i < bits; i++) {
//         for (int mask = 0; mask < size; mask++) {
//             if ((mask & (1 << i)) && dp[mask] != -1) {
//                 int subset = mask ^ (1 << i);
//                 if (dp[subset] == -1) {
//                     dp[subset] = dp[mask];
//                 }
//             }
//         }
//     }
//     
//     vector<int> result(n, -1);
//     for (int i = 0; i < n; i++) {
//         int complement = (size - 1) ^ nums[i];
//         if (dp[complement] != -1) {
//             result[i] = dp[complement];
//         }
//     }
//     
//     return result;
// }
// 
// int main() {
//     vector<int> arr = {1, 2, 3, 4};
//     vector<int> result = sosDPBasic(arr);
//     
//     cout << "SOS DP测试:" << endl;
//     for (int i = 0; i < result.size(); i++) {
//         cout << "mask " << bitset<4>(i) << ": " << result[i] << endl;
//     }
//     
//     return 0;
// }

/*
 * Python 实现
 */
/*
 * Python 实现
 *
 * def sos_dp_basic(arr):
 *     n = len(arr)
 *     size = 1 << n
 *     dp = [0] * size
 *     
 *     for i in range(n):
 *         dp[1 << i] = arr[i]
 *     
 *     for mask in range(size):
 *         for i in range(n):
 *             if mask & (1 << i):
 *                 dp[mask] += dp[mask ^ (1 << i)]
 *     
 *     return dp
 * 
 * def compatible_numbers_sos(nums):
 *     n = len(nums)
 *     max_val = max(nums)
 *     
 *     bits = 0
 *     while (1 << bits) <= max_val:
 *         bits += 1
 *     
 *     size = 1 << bits
 *     dp = [-1] * size
 *     
 *     for i in range(n):
 *         dp[nums[i]] = i
 *     
 *     for i in range(bits):
 *         for mask in range(size):
 *             if (mask & (1 << i)) and dp[mask] != -1:
 *                 subset = mask ^ (1 << i)
 *                 if dp[subset] == -1:
 *                     dp[subset] = dp[mask]
 *     
 *     result = [-1] * n
 *     for i in range(n):
 *         complement = (size - 1) ^ nums[i]
 *         if dp[complement] != -1:
 *             result[i] = dp[complement]
 *     
 *     return result
 * 
 * if __name__ == "__main__":
 *     arr = [1, 2, 3, 4]
 *     result = sos_dp_basic(arr)
 *     print("SOS DP测试:")
 *     for i, val in enumerate(result):
 *         print(f"mask {bin(i)}: {val}")
 */