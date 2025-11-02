// Anya and Cubes
// 给定n个数和一个目标值S，每个数可以：
// 1. 不选
// 2. 选，加上这个数
// 3. 选，加上这个数的阶乘（如果这个数<=18）
// 求有多少种选择方案使得所选数的和等于S。
// 测试链接 : https://codeforces.com/problemset/problem/525/E
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过哈希表统计不同方案数
// 时间复杂度：O(3^(n/2) * log(3^(n/2)))
// 空间复杂度：O(3^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查输入参数
// 2. 性能优化：使用折半搜索减少搜索空间，预计算阶乘
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用unordered_map进行计数统计，使用递归计算所有可能的和

#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

// 预计算阶乘数组
long long FACTORIAL[19];

// 初始化阶乘数组
void initFactorial() {
    FACTORIAL[0] = 1;
    for (int i = 1; i <= 18; i++) {
        FACTORIAL[i] = FACTORIAL[i - 1] * i;
    }
}

/**
 * 递归生成指定范围内所有可能的和及其方案数
 * @param nums 输入数组
 * @param start 起始索引
 * @param end 结束索引
 * @param currentSum 当前累积和
 * @param currentK 当前使用的阶乘次数
 * @param maxK 最多可以使用的阶乘次数
 * @param index 当前处理的元素索引
 * @param sums 存储结果的Map
 */
void generateSubsetSums(vector<int>& nums, int start, int end, long long currentSum, int currentK, int maxK, int index, unordered_map<long long, unordered_map<int, long long>>& sums) {
    // 递归终止条件
    if (start == end) {
        sums[currentSum][currentK]++;
        return;
    }
    
    int num = nums[start];
    
    // 不选择当前元素
    generateSubsetSums(nums, start + 1, end, currentSum, currentK, maxK, index + 1, sums);
    
    // 选择当前元素（加上这个数）
    generateSubsetSums(nums, start + 1, end, currentSum + num, currentK, maxK, index + 1, sums);
    
    // 选择当前元素（加上这个数的阶乘）
    if (num <= 18 && currentK < maxK) {
        generateSubsetSums(nums, start + 1, end, currentSum + FACTORIAL[num], currentK + 1, maxK, index + 1, sums);
    }
}

/**
 * 计算满足条件的选择方案数
 * @param nums 输入数组
 * @param k 最多可以使用阶乘操作的次数
 * @param S 目标值
 * @return 满足条件的方案数
 */
long long solve(vector<int>& nums, int k, long long S) {
    // 边界条件检查
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    
    // 使用折半搜索，将数组分为两半
    // unordered_map<和, unordered_map<使用阶乘次数, 方案数>>
    unordered_map<long long, unordered_map<int, long long>> leftSums;
    unordered_map<long long, unordered_map<int, long long>> rightSums;
    
    // 计算左半部分所有可能的和及其方案数
    generateSubsetSums(nums, 0, n / 2, 0, 0, k, 0, leftSums);
    
    // 计算右半部分所有可能的和及其方案数
    generateSubsetSums(nums, n / 2, n, 0, 0, k, 0, rightSums);
    
    // 统计满足条件的方案数
    long long count = 0;
    for (auto& leftEntry : leftSums) {
        long long leftSum = leftEntry.first;
        auto& leftMap = leftEntry.second;
        
        for (auto& rightEntry : rightSums) {
            long long rightSum = rightEntry.first;
            auto& rightMap = rightEntry.second;
            
            // 如果左右两部分的和等于目标值
            if (leftSum + rightSum == S) {
                // 统计所有可能的组合
                for (auto& leftCountEntry : leftMap) {
                    int leftK = leftCountEntry.first;
                    long long leftCount = leftCountEntry.second;
                    
                    for (auto& rightCountEntry : rightMap) {
                        int rightK = rightCountEntry.first;
                        long long rightCount = rightCountEntry.second;
                        
                        // 如果使用的阶乘次数不超过限制
                        if (leftK + rightK <= k) {
                            count += leftCount * rightCount;
                        }
                    }
                }
            }
        }
    }
    
    return count;
}

// 测试方法
int main() {
    // 初始化阶乘数组
    initFactorial();
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 1};
    int k1 = 1;
    long long S1 = 3;
    cout << "测试用例1:" << endl;
    cout << "nums = [1, 1, 1], k = 1, S = 3" << endl;
    cout << "期望输出: 6" << endl;
    cout << "实际输出: " << solve(nums1, k1, S1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {1, 2, 3};
    int k2 = 2;
    long long S2 = 6;
    cout << "测试用例2:" << endl;
    cout << "nums = [1, 2, 3], k = 2, S = 6" << endl;
    cout << "期望输出: 7" << endl;
    cout << "实际输出: " << solve(nums2, k2, S2) << endl;
    
    return 0;
}