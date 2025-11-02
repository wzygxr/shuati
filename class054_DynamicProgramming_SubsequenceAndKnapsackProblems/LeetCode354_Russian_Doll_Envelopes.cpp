#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;

/**
 * LeetCode 354. 俄罗斯套娃信封问题
 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
 * 请计算最多能有多少个信封能组成一组"俄罗斯套娃"信封。
 * 测试链接：https://leetcode.cn/problems/russian-doll-envelopes/
 * 
 * 算法详解：
 * 使用排序+LIS（最长递增子序列）方法解决俄罗斯套娃信封问题。
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组有效性
 * 2. 边界处理：单个信封的情况
 * 3. 性能优化：使用贪心+二分查找优化LIS计算
 * 4. 代码质量：清晰的排序逻辑和LIS实现
 */

class Solution {
public:
    /**
     * 最优解法：排序 + LIS（贪心+二分查找）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    static int maxEnvelopes(vector<vector<int>>& envelopes) {
        if (envelopes.empty()) {
            return 0;
        }
        
        int n = envelopes.size();
        if (n == 1) {
            return 1;
        }
        
        // 排序：按宽度升序，宽度相同时按高度降序
        sort(envelopes.begin(), envelopes.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] == b[0]) {
                return a[1] > b[1]; // 宽度相同，高度降序
            } else {
                return a[0] < b[0]; // 宽度升序
            }
        });
        
        // 提取高度序列
        vector<int> heights;
        for (const auto& env : envelopes) {
            heights.push_back(env[1]);
        }
        
        // 计算高度序列的最长递增子序列
        return lengthOfLIS(heights);
    }
    
    /**
     * 基础动态规划解法
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     */
    static int maxEnvelopesDP(vector<vector<int>>& envelopes) {
        if (envelopes.empty()) {
            return 0;
        }
        
        int n = envelopes.size();
        
        // 排序：按宽度升序，宽度相同时按高度升序
        sort(envelopes.begin(), envelopes.end(), [](const vector<int>& a, const vector<int>& b) {
            if (a[0] == b[0]) {
                return a[1] < b[1];
            } else {
                return a[0] < b[0];
            }
        });
        
        vector<int> dp(n, 1);
        int maxCount = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (envelopes[i][0] > envelopes[j][0] && envelopes[i][1] > envelopes[j][1]) {
                    dp[i] = max(dp[i], dp[j] + 1);
                }
            }
            maxCount = max(maxCount, dp[i]);
        }
        
        return maxCount;
    }
    
private:
    /**
     * 计算最长递增子序列长度（贪心+二分查找优化）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     */
    static int lengthOfLIS(const vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        vector<int> tails;
        
        for (int num : nums) {
            // 使用二分查找找到插入位置
            auto it = lower_bound(tails.begin(), tails.end(), num);
            
            if (it == tails.end()) {
                // 当前数字大于所有尾部值，扩展序列
                tails.push_back(num);
            } else {
                // 替换第一个大于等于当前数字的位置
                *it = num;
            }
        }
        
        return tails.size();
    }
};

/**
 * 测试辅助函数
 */
void runTest(const string& description, vector<vector<int>> envelopes, int expected) {
    cout << description << endl;
    cout << "输入信封: [";
    for (size_t i = 0; i < envelopes.size(); i++) {
        cout << "[" << envelopes[i][0] << "," << envelopes[i][1] << "]";
        if (i < envelopes.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "期望结果: " << expected << endl;
    
    vector<vector<int>> env1 = envelopes;
    vector<vector<int>> env2 = envelopes;
    
    int result1 = Solution::maxEnvelopes(env1);
    int result2 = Solution::maxEnvelopesDP(env2);
    
    cout << "最优解法: " << result1 << " " << (result1 == expected ? "✓" : "✗") << endl;
    cout << "基础DP: " << result2 << " " << (result2 == expected ? "✓" : "✗") << endl;
    
    if (result1 == result2 && result1 == expected) {
        cout << "测试通过 ✓" << endl;
    } else {
        cout << "测试失败 ✗" << endl;
    }
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据：大规模信封数组
    const int n = 10000;
    vector<vector<int>> envelopes(n, vector<int>(2));
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(1, 1000);
    
    for (int i = 0; i < n; i++) {
        envelopes[i][0] = dis(gen);
        envelopes[i][1] = dis(gen);
    }
    
    cout << "测试数据规模: " << n << "个信封" << endl;
    
    // 测试最优解法
    auto start = chrono::high_resolution_clock::now();
    int result1 = Solution::maxEnvelopes(envelopes);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "最优解法（贪心+二分）:" << endl;
    cout << "  结果: " << result1 << endl;
    cout << "  耗时: " << duration1.count() << " 毫秒" << endl;
    
    // 测试基础DP（仅小规模）
    if (n <= 1000) {
        start = chrono::high_resolution_clock::now();
        int result2 = Solution::maxEnvelopesDP(envelopes);
        end = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "基础DP解法:" << endl;
        cout << "  结果: " << result2 << endl;
        cout << "  耗时: " << duration2.count() << " 毫秒" << endl;
        
        if (result1 == result2) {
            cout << "结果一致性验证: 通过 ✓" << endl;
        } else {
            cout << "结果一致性验证: 失败 ✗" << endl;
        }
    } else {
        cout << "基础DP算法在大规模数据下性能较差，跳过测试" << endl;
    }
    
    cout << endl;
}

int main() {
    cout << "=== LeetCode 354 俄罗斯套娃信封问题测试 ===" << endl << endl;
    
    // 测试用例1：基本功能测试
    runTest("测试用例1 - 基本功能", {{5,4},{6,4},{6,7},{2,3}}, 3);
    
    // 测试用例2：官方示例
    runTest("测试用例2 - 官方示例", {{1,1},{1,1},{1,1}}, 1);
    
    // 测试用例3：单个信封
    runTest("测试用例3 - 单个信封", {{5,4}}, 1);
    
    // 测试用例4：空数组
    runTest("测试用例4 - 空数组", {}, 0);
    
    // 测试用例5：复杂情况
    runTest("测试用例5 - 复杂情况", {{2,3},{5,4},{6,7},{6,4},{7,5}}, 3);
    
    // 性能测试
    performanceTest();
    
    cout << "所有测试完成！" << endl;
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 最优解法（排序+LIS）：
 * - 时间：排序O(n log n) + LIS计算O(n log n) → O(n log n)
 * - 空间：排序O(log n) + LIS数组O(n) → O(n)
 * 
 * 基础动态规划：
 * - 时间：排序O(n log n) + 双重循环O(n²) → O(n²)
 * - 空间：排序O(log n) + dp数组O(n) → O(n)
 * 
 * C++特性说明：
 * 1. 使用lambda表达式简化排序逻辑
 * 2. STL算法提供高效的lower_bound函数
 * 3. 使用chrono库进行精确性能测试
 * 4. RAII机制自动管理资源
 * 
 * 工程化建议：
 * 1. 对于生产环境使用最优解法
 * 2. 添加异常处理确保程序健壮性
 * 3. 编写单元测试覆盖各种边界情况
 * 4. 使用性能分析工具优化关键路径
 */