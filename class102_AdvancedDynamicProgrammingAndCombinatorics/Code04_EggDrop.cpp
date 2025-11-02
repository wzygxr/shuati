#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
#include <cstdio>
#include <ctime>

/**
 * 超级鸡蛋掉落问题（Super Egg Drop）
 * 
 * 问题描述：
 * 假设你有 k 个鸡蛋，并且可以使用一栋从 1 到 n 层的大楼。
 * 已知存在某个楼层 f（0 <= f <= n），从 f 楼及以下楼层抛出的鸡蛋不会碎，
 * 从 f 楼以上的楼层抛出的鸡蛋会碎。
 * 当鸡蛋被摔碎后，它就不能再使用了。
 * 请确定最少需要多少次尝试，才能保证在最坏情况下找出确切的 f 值。
 * 
 * 约束条件：
 * - 1 <= k <= 100
 * - 1 <= n <= 10^4
 * 
 * 算法思路：
 * 这个问题采用了优化的动态规划状态定义：
 * dp[i][j] 表示使用 i 个鸡蛋，尝试 j 次，最多能确定的楼层数。
 * 我们需要找到最小的 j，使得 dp[k][j] >= n。
 * 
 * 状态转移方程：
 * dp[i][j] = dp[i-1][j-1] + dp[i][j-1] + 1
 * 其中：
 * - dp[i-1][j-1] 表示鸡蛋碎了的情况，用i-1个鸡蛋在j-1次尝试中能确定的楼层数
 * - dp[i][j-1] 表示鸡蛋没碎的情况，用i个鸡蛋在j-1次尝试中能确定的楼层数
 * - +1 表示当前测试的楼层
 */
class Solution {
private:
    // 验证输入参数的有效性
    void validateInputs(int k, int n) {
        if (k < 1 || k > 100) {
            throw std::invalid_argument("鸡蛋数量必须在1到100之间");
        }
        if (n < 1 || n > 10000) {
            throw std::invalid_argument("楼层数量必须在1到10000之间");
        }
    }

public:
    /**
     * 解法1：二维dp数组实现
     * 时间复杂度：O(k*n)，但在实际执行中会早退出
     * 空间复杂度：O(k*n)
     */
    int superEggDrop1(int k, int n) {
        // 输入验证
        validateInputs(k, n);
        
        // 边界情况：如果只有1个鸡蛋，必须从1楼开始逐层测试
        if (k == 1) {
            return n;
        }
        
        // 创建dp数组
        std::vector<std::vector<int>> dp(k + 1, std::vector<int>(n + 1, 0));
        
        // j表示尝试次数，从1开始递增
        for (int j = 1; j <= n; ++j) {
            // i表示使用的鸡蛋数，从1开始递增
            for (int i = 1; i <= k; ++i) {
                // 状态转移方程
                dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1] + 1;
                
                // 当可以确定的楼层数大于等于n时，返回当前的尝试次数j
                if (dp[i][j] >= n) {
                    return j;
                }
            }
        }
        
        // 实际上不可能到达这里
        return n;
    }

    /**
     * 解法2：空间优化版本，使用一维dp数组
     * 时间复杂度：O(k*n)，但在实际执行中会早退出
     * 空间复杂度：O(k)
     */
    int superEggDrop2(int k, int n) {
        // 输入验证
        validateInputs(k, n);
        
        // 边界情况：如果只有1个鸡蛋，必须从1楼开始逐层测试
        if (k == 1) {
            return n;
        }
        
        // 空间优化：使用一维dp数组
        std::vector<int> dp(k + 1, 0);
        
        // j表示尝试次数，从1开始递增
        for (int j = 1; j <= n; ++j) {
            // 保存上一次的值，用于状态转移
            int previous = 0;
            
            // i表示使用的鸡蛋数，从1开始递增
            for (int i = 1; i <= k; ++i) {
                // 暂存当前dp[i]的值，因为它将作为下一轮的previous
                int temp = dp[i];
                
                // 状态转移
                dp[i] = dp[i] + previous + 1;
                
                // 更新previous为当前dp[i]的旧值
                previous = temp;
                
                // 当可以确定的楼层数大于等于n时，返回当前的尝试次数j
                if (dp[i] >= n) {
                    return j;
                }
            }
        }
        
        // 实际上不可能到达这里
        return n;
    }

    /**
     * 解法3：二分搜索优化版本
     * 时间复杂度：O(k*log n)
     * 空间复杂度：O(k)
     */
    int superEggDrop3(int k, int n) {
        // 输入验证
        validateInputs(k, n);
        
        // 边界情况处理
        if (k == 1) {
            return n;
        }
        
        // 计算最小需要多少次尝试才能覆盖n层楼
        int low = 1, high = n;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (computeFloors(k, mid) >= n) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        
        return low;
    }

private:
    /**
     * 计算使用k个鸡蛋，尝试m次，最多能确定的楼层数
     */
    int computeFloors(int k, int m) {
        // 使用动态规划计算最多能确定的楼层数
        std::vector<int> dp(k + 1, 0);
        
        for (int i = 1; i <= m; ++i) {
            int prev = 0;
            for (int j = 1; j <= k; ++j) {
                int temp = dp[j];
                dp[j] = dp[j] + prev + 1;
                prev = temp;
                
                // 提前终止，避免整数溢出
                if (dp[j] > 10000) {
                    return 10000;
                }
            }
        }
        
        return dp[k];
    }
};

// 主函数，用于测试不同解法
int main() {
    Solution solution;
    
    // 测试用例
    std::vector<std::pair<int, int>> testCases = {
        {1, 2},    // 预期输出: 2
        {2, 6},    // 预期输出: 3
        {3, 14},   // 预期输出: 4
        {2, 100},  // 预期输出: 14
        {100, 10000} // 预期输出: 24
    };
    
    std::cout << "测试不同解法的结果：" << std::endl;
    for (const auto& testCase : testCases) {
        int k = testCase.first;
        int n = testCase.second;
        
        // 记录开始时间
        clock_t start, end;
        
        // 测试解法1
        start = clock();
        int result1 = solution.superEggDrop1(k, n);
        end = clock();
        double time1 = static_cast<double>(end - start) / CLOCKS_PER_SEC * 1000; // 转换为毫秒
        
        // 测试解法2
        start = clock();
        int result2 = solution.superEggDrop2(k, n);
        end = clock();
        double time2 = static_cast<double>(end - start) / CLOCKS_PER_SEC * 1000;
        
        // 测试解法3
        start = clock();
        int result3 = solution.superEggDrop3(k, n);
        end = clock();
        double time3 = static_cast<double>(end - start) / CLOCKS_PER_SEC * 1000;
        
        // 输出结果
        printf("鸡蛋数: %d, 楼层数: %d\n", k, n);
        printf("解法1结果: %d, 耗时: %.3fms\n", result1, time1);
        printf("解法2结果: %d, 耗时: %.3fms\n", result2, time2);
        printf("解法3结果: %d, 耗时: %.3fms\n\n", result3, time3);
    }
    
    return 0;
}

/**
 * C++工程化实战建议：
 * 
 * 1. 异常处理：
 *    - 使用异常机制处理无效输入
 *    - 在生产环境中可以考虑使用错误码而不是异常
 *    - 添加try-catch块捕获可能的异常
 * 
 * 2. 性能优化：
 *    - 对于非常大的n，优先使用二分搜索版本（解法3）
 *    - 对于内存受限的环境，使用空间优化版本（解法2）
 *    - 注意整数溢出问题，当k和m较大时，使用long long类型
 * 
 * 3. 内存管理：
 *    - 使用vector动态分配内存，避免手动管理内存
 *    - 对于频繁调用的场景，可以考虑缓存dp数组
 *    - 注意在递归或循环中避免创建过多临时对象
 * 
 * 4. 代码可读性与维护性：
 *    - 使用命名空间组织代码
 *    - 可以将类定义和实现分离到.h和.cpp文件中
 *    - 添加单元测试确保代码正确性
 * 
 * 5. 并发安全：
 *    - 当前实现是无状态的，可以安全地在多线程环境中使用
 *    - 对于有状态的实现，需要添加互斥锁或使用线程局部存储
 */

/**
 * 算法优化思考：
 * 
 * 1. 数学公式优化：
 *    可以证明，使用k个鸡蛋尝试m次，最多能确定的楼层数等于组合数之和：
 *    floor = C(m,1) + C(m,2) + ... + C(m,min(k,m))
 *    可以使用这个公式直接计算，避免动态规划的循环
 * 
 * 2. 预处理优化：
 *    对于多次调用的场景，可以预处理所有可能的k和n的组合，
 *    构建一个查询表，实现O(1)时间复杂度的查询
 * 
 * 3. 位运算优化：
 *    对于某些特殊情况，可以使用位运算加速计算
 *    例如当k >= log2(n)时，最优解就是log2(n)向上取整
 * 
 * 4. 缓存优化：
 *    可以缓存computeFloors函数的结果，避免重复计算
 *    特别是在解法3中，这个函数可能被多次调用
 */