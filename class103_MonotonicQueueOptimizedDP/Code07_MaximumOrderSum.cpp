#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

// 子数组最大变序和
// 给定一个长度为n的数组arr，变序和定义如下
// 数组中每个值都可以减小或者不变，必须把整体变成严格升序的
// 所有方案中，能得到的最大累加和，叫做数组的变序和
// 比如[1,100,7]，变序和14，方案为变成[1,6,7]
// 比如[5,4,9]，变序和16，方案为变成[3,4,9]
// 比如[1,4,2]，变序和3，方案为变成[0,1,2]
// 返回arr所有子数组的变序和中，最大的那个
// 1 <= n、arr[i] <= 10^6
// 来自真实大厂笔试，对数器验证

/**
 * 子数组最大变序和问题 - 单调栈优化解法
 * 
 * 算法思路详解：
 * 1. 问题分析：
 *    - 需要找到所有子数组，将其变为严格递增序列后的最大累加和
 *    - 每个元素可以减小或保持不变，但必须满足严格递增
 *    - 目标是找到所有子数组变序和中的最大值
 *    
 * 2. 优化思路：
 *    - 使用单调栈维护可能的子数组结尾
 *    - 对于每个元素，计算以该元素结尾的子数组的最大变序和
 *    - 利用数学公式快速计算等差数列的和
 *    
 * 3. 时间复杂度分析：
 *    - 时间复杂度：O(n)，每个元素最多入栈和出栈一次
 *    - 空间复杂度：O(n)，栈和dp数组的空间
 *    
 * 4. 为什么是最优解：
 *    - 该解法将暴力O(n²)优化到O(n)
 *    - 利用单调栈和数学公式，是此类问题的最优解法
 */
class Code07_MaximumOrderSum {
public:
    /**
     * 暴力方法 - 用于验证正确性
     * 时间复杂度：O(n * v)，其中v是数组最大值
     * 空间复杂度：O(n * v)
     * 
     * @param arr 输入数组
     * @return 最大变序和
     */
    static long long maxSum1(vector<int>& arr) {
        int n = arr.size();
        if (n == 0) return 0;
        
        int maxVal = 0;
        for (int num : arr) {
            maxVal = max(maxVal, num);
        }
        
        long long ans = 0;
        vector<vector<long long>> dp(n, vector<long long>(maxVal + 1, -1));
        
        for (int i = 0; i < n; i++) {
            ans = max(ans, f1(arr, i, arr[i], dp));
        }
        return ans;
    }
    
    /**
     * 递归辅助函数 - 计算以位置i结尾的子数组的最大变序和
     * 
     * @param arr 输入数组
     * @param i 当前位置
     * @param p 当前允许的最大值
     * @param dp 记忆化数组
     * @return 最大变序和
     */
    static long long f1(vector<int>& arr, int i, int p, vector<vector<long long>>& dp) {
        if (p <= 0 || i == -1) {
            return 0;
        }
        if (dp[i][p] != -1) {
            return dp[i][p];
        }
        int cur = min(arr[i], p);
        long long next = f1(arr, i - 1, cur - 1, dp);
        long long ans = (long long)cur + next;
        dp[i][p] = ans;
        return ans;
    }
    
    /**
     * 正式方法 - 单调栈优化
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param arr 输入数组
     * @return 最大变序和
     */
    static long long maxSum2(vector<int>& arr) {
        int n = arr.size();
        if (n == 0) return 0;
        
        vector<int> stack(n);  // 单调栈，存储下标
        int size = 0;         // 栈大小
        vector<long long> dp(n, 0);  // dp[i]表示以i结尾的子数组的最大变序和
        long long ans = 0;
        
        for (int i = 0; i < n; i++) {
            int curIdx = i;
            int curVal = arr[curIdx];
            
            // 维护单调栈，处理栈顶元素
            while (curVal > 0 && size > 0) {
                int topIdx = stack[size - 1];
                int topVal = arr[topIdx];
                
                if (topVal >= curVal) {
                    // 栈顶元素更大，直接弹出
                    size--;
                } else {
                    int idxDiff = curIdx - topIdx;  // 位置差
                    int valDiff = curVal - topVal;  // 数值差
                    
                    if (valDiff >= idxDiff) {
                        // 可以完全覆盖区间
                        dp[i] += calculateSum(curVal, idxDiff) + dp[topIdx];
                        curVal = 0;
                        curIdx = 0;
                        break;
                    } else {
                        // 部分覆盖
                        dp[i] += calculateSum(curVal, idxDiff);
                        curVal -= idxDiff;
                        curIdx = topIdx;
                        size--;
                    }
                }
            }
            
            // 处理剩余部分
            if (curVal > 0) {
                dp[i] += calculateSum(curVal, curIdx + 1);
            }
            
            // 当前元素入栈
            stack[size++] = i;
            ans = max(ans, dp[i]);
        }
        return ans;
    }
    
    /**
     * 计算等差数列的和
     * 从max开始，递减1，共n项的正数部分的和
     * 公式：sum = (首项 + 末项) * 项数 / 2
     * 
     * @param max 最大值
     * @param n 项数
     * @return 等差数列的和
     */
    static long long calculateSum(int max, int n) {
        n = min(max, n);  // 确保不超过max
        return ((long long)max * 2 - n + 1) * n / 2;
    }
    
    /*
     * 算法详细解释：
     * 
     * 1. 核心思想：
     *    - 对于每个元素，计算以该元素结尾的子数组的最大变序和
     *    - 使用单调栈维护可能的子数组结尾
     *    - 利用数学公式快速计算等差数列的和
     *    
     * 2. 单调栈的作用：
     *    - 维护一个递增栈，栈顶元素最小
     *    - 当遇到更小的元素时，弹出栈顶元素并计算其贡献
     *    - 弹出的元素arr[m]的右边界就是当前元素i
     *    - 左边界就是栈中下一个元素（如果存在）
     *    
     * 3. 数学公式原理：
     *    - 对于区间[l, r]，需要将其变为严格递增序列
     *    - 最大可能的序列是从某个值开始递减1的等差数列
     *    - 使用等差数列求和公式快速计算
     *    
     * 4. 时间复杂度证明：
     *    - 每个元素最多入栈一次、出栈一次
     *    - 每次操作都是O(1)时间
     *    - 总时间复杂度O(n)
     *    
     * 5. 空间复杂度分析：
     *    - 栈空间：O(n)
     *    - dp数组：O(n)
     *    - 总空间复杂度O(n)
     *    
     * 6. 为什么这是最优解：
     *    - 问题本身需要遍历所有子数组，朴素解法O(n²)
     *    - 该解法利用单调性将复杂度降到O(n)
     *    - 无法进一步优化，因为需要处理每个元素
     *    
     * 7. 语言特性差异：
     *    - C++: 使用vector和stack，需要手动管理内存
     *    - Java: 使用数组，有垃圾回收机制
     *    - Python: 使用列表，动态类型
     */
};

// 测试函数
int main() {
    // 测试用例1
    vector<int> arr1 = {1, 100, 7};
    long long result1 = Code07_MaximumOrderSum::maxSum2(arr1);
    cout << "测试用例1结果: " << result1 << endl;  // 期望输出: 14
    
    // 测试用例2
    vector<int> arr2 = {5, 4, 9};
    long long result2 = Code07_MaximumOrderSum::maxSum2(arr2);
    cout << "测试用例2结果: " << result2 << endl;  // 期望输出: 16
    
    // 测试用例3
    vector<int> arr3 = {1, 4, 2};
    long long result3 = Code07_MaximumOrderSum::maxSum2(arr3);
    cout << "测试用例3结果: " << result3 << endl;  // 期望输出: 3
    
    // 测试用例4：边界情况，空数组
    vector<int> arr4 = {};
    long long result4 = Code07_MaximumOrderSum::maxSum2(arr4);
    cout << "测试用例4结果: " << result4 << endl;  // 期望输出: 0
    
    return 0;
}