/**
 * LeetCode 887. 鸡蛋掉落
 * 题目链接: https://leetcode.com/problems/super-egg-drop/
 * 
 * 题目描述:
 * 你将获得 k 个鸡蛋，并可以使用一栋从 1 到 n 共有 n 层楼的建筑。
 * 已知存在楼层 f ，满足 0 <= f <= n ，任何从高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。
 * 每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。
 * 如果鸡蛋碎了，你就不能再使用它。如果鸡蛋没碎，你可以再次使用它。
 * 请你计算并返回要确定 f 确切的值的最小操作次数是多少？
 * 
 * 解题思路:
 * 本题可以使用动态规划结合高斯消元法进行优化。
 * 定义 dp[k][m] 表示有 k 个鸡蛋，最多允许 m 次操作时，最多能测试的楼层数。
 * 状态转移方程: dp[k][m] = dp[k][m-1] + dp[k-1][m-1] + 1
 * 
 * 时间复杂度: O(k * log n)
 * 空间复杂度: O(k)
 * 
 * 工程化考虑:
 * 1. 使用动态规划优化，避免直接高斯消元的高时间复杂度
 * 2. 处理边界情况：当 k=1 或 n=1 时的特殊情况
 * 3. 使用二分查找优化搜索过程
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>

using namespace std;

class Solution {
public:
    /**
     * 使用动态规划求解鸡蛋掉落问题
     * @param k 鸡蛋数量
     * @param n 楼层数
     * @return 最小操作次数
     */
    int superEggDrop(int k, int n) {
        // 边界情况处理
        if (n == 1) {
            return 1;
        }
        if (k == 1) {
            return n;
        }
        
        // dp[k][m] 表示有k个鸡蛋，最多允许m次操作时，最多能测试的楼层数
        // 由于n可能很大，我们使用一维数组进行优化
        vector<int> dp(k + 1, 0);
        
        int m = 0;
        while (dp[k] < n) {
            m++;
            // 从后往前更新，避免覆盖
            for (int i = k; i >= 1; i--) {
                dp[i] = dp[i] + dp[i - 1] + 1;
            }
        }
        
        return m;
    }
    
    /**
     * 使用二分查找优化的动态规划解法
     * 时间复杂度: O(k * n * log n)
     * 空间复杂度: O(k * n)
     */
    int superEggDropBinarySearch(int k, int n) {
        // dp[k][n] 表示有k个鸡蛋，n层楼时的最小操作次数
        vector<vector<int>> dp(k + 1, vector<int>(n + 1, 0));
        
        // 初始化边界条件
        for (int i = 1; i <= n; i++) {
            dp[1][i] = i; // 只有一个鸡蛋时，需要逐层测试
        }
        for (int i = 1; i <= k; i++) {
            dp[i][1] = 1; // 只有一层楼时，只需要一次测试
        }
        
        for (int i = 2; i <= k; i++) {
            for (int j = 2; j <= n; j++) {
                // 使用二分查找优化
                int left = 1, right = j;
                while (left < right) {
                    int mid = left + (right - left + 1) / 2;
                    // 鸡蛋碎了，测试下面 mid-1 层
                    int broken = dp[i - 1][mid - 1];
                    // 鸡蛋没碎，测试上面 j-mid 层
                    int notBroken = dp[i][j - mid];
                    
                    if (broken > notBroken) {
                        right = mid - 1;
                    } else {
                        left = mid;
                    }
                }
                
                dp[i][j] = max(dp[i - 1][left - 1], dp[i][j - left]) + 1;
            }
        }
        
        return dp[k][n];
    }
    
    /**
     * 测试方法
     */
    void test() {
        cout << "=== LeetCode 887. 鸡蛋掉落测试 ===" << endl;
        
        // 测试用例1: k=1, n=2
        cout << "测试用例1 - k=1, n=2: " << superEggDrop(1, 2) << endl;
        
        // 测试用例2: k=2, n=6
        cout << "测试用例2 - k=2, n=6: " << superEggDrop(2, 6) << endl;
        
        // 测试用例3: k=3, n=14
        cout << "测试用例3 - k=3, n=14: " << superEggDrop(3, 14) << endl;
        
        // 性能测试: k=10, n=10000
        auto start = chrono::high_resolution_clock::now();
        int result = superEggDrop(10, 10000);
        auto end = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "性能测试 - k=10, n=10000: " << result << ", 耗时: " << duration.count() << "ms" << endl;
        
        // 对比两种解法的结果
        cout << "\n对比两种解法:" << endl;
        cout << "k=2, n=6 - 优化解法: " << superEggDrop(2, 6) << endl;
        cout << "k=2, n=6 - 二分查找解法: " << superEggDropBinarySearch(2, 6) << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
};

/**
 * 复杂度分析:
 * 1. 优化解法 (superEggDrop):
 *    - 时间复杂度: O(k * log n)
 *    - 空间复杂度: O(k)
 *    - 优点: 效率高，适合大规模数据
 *    - 缺点: 状态转移理解较复杂
 * 
 * 2. 二分查找解法 (superEggDropBinarySearch):
 *    - 时间复杂度: O(k * n * log n)
 *    - 空间复杂度: O(k * n)
 *    - 优点: 思路直观，易于理解
 *    - 缺点: 时间和空间复杂度较高
 * 
 * 工程化建议:
 * - 对于小规模数据，可以使用二分查找解法，便于调试和理解
 * - 对于大规模数据，推荐使用优化解法
 * - 在实际应用中，可以根据数据规模动态选择解法
 */

int main() {
    Solution solution;
    solution.test();
    return 0;
}