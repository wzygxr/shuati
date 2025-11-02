#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
#include <random>
#include <stdexcept>

using namespace std;

/**
 * LeetCode 2444. 统计定界子数组的数目 (Count Subarrays With Fixed Bounds) - C++版本
 * 
 * 题目来源：https://leetcode.cn/problems/count-subarrays-with-fixed-bounds/
 * 
 * 题目描述：
 * 给你一个整数数组 nums 和两个整数 minK 以及 maxK。
 * nums 的定界子数组是满足下述条件的一个子数组：
 * - 子数组中的最小值等于 minK。
 * - 子数组中的最大值等于 maxK。
 * 返回定界子数组的数目。
 * 子数组是数组中的一个连续部分。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 滑动窗口：维护满足条件的窗口
 * 2. 稀疏表：预处理范围最值查询，然后枚举所有可能的子数组
 * 3. 数学方法：通过计算包含minK和maxK的子数组数量
 * 
 * 使用稀疏表的方法：
 * 1. 预处理稀疏表用于范围最值查询
 * 2. 对于每个左端点，使用二分查找找到满足条件的右端点范围
 * 
 * 时间复杂度：
 * - 稀疏表预处理：O(n log n)
 * - 查询：O(1)
 * - 总时间复杂度：O(n²)（最坏情况）或O(n log n)（优化后）
 * - 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 数据分析：统计满足特定条件的连续数据段
 * 2. 金融：分析价格波动区间
 * 3. 信号处理：检测特定幅度的信号段
 * 
 * 相关题目：
 * 1. LeetCode 315. 计算右侧小于当前元素的个数
 * 2. LeetCode 743. 网络延迟时间
 * 3. LeetCode 1584. 连接所有点的最小费用
 */

class SparseTable {
private:
    vector<int> data;
    vector<vector<int>> stMin;  // 用于范围最小值查询的稀疏表
    vector<vector<int>> stMax;  // 用于范围最大值查询的稀疏表
    vector<int> logTable;
    int n;

public:
    /**
     * 构造函数
     * @param data 输入数组
     */
    SparseTable(const vector<int>& data) {
        if (data.empty()) {
            throw invalid_argument("输入数组不能为空");
        }
        
        this->data = data;
        this->n = data.size();
        
        // 预计算log表
        precomputeLogTable();
        
        // 构建稀疏表
        buildSparseTable();
    }
    
    /**
     * 预计算log2值表
     */
    void precomputeLogTable() {
        logTable.resize(n + 1);
        logTable[1] = 0;
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
    }
    
    /**
     * 构建稀疏表
     */
    void buildSparseTable() {
        int k = logTable[n] + 1;
        
        // 初始化稀疏表
        stMin.assign(k, vector<int>(n));
        stMax.assign(k, vector<int>(n));
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            stMin[0][i] = data[i];
            stMax[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                int prevLen = 1 << (j - 1);
                // 范围最小值查询
                stMin[j][i] = min(stMin[j-1][i], stMin[j-1][i + prevLen]);
                // 范围最大值查询
                stMax[j][i] = max(stMax[j-1][i], stMax[j-1][i + prevLen]);
            }
        }
    }
    
    /**
     * 范围最小值查询
     * 时间复杂度：O(1)
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最小值
     */
    int queryMin(int left, int right) const {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return min(stMin[k][left], stMin[k][right - (1 << k) + 1]);
    }
    
    /**
     * 范围最大值查询
     * 时间复杂度：O(1)
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最大值
     */
    int queryMax(int left, int right) const {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return max(stMax[k][left], stMax[k][right - (1 << k) + 1]);
    }
};

/**
 * 方法1：使用稀疏表的解法
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 * @param nums 输入数组
 * @param minK 最小值边界
 * @param maxK 最大值边界
 * @return 定界子数组的数目
 */
long long countSubarraysWithSparseTable(const vector<int>& nums, int minK, int maxK) {
    int n = nums.size();
    if (n == 0) return 0;
    
    // 构建稀疏表
    SparseTable st(nums);
    
    long long count = 0;
    
    // 对于每个左端点，找到满足条件的右端点范围
    for (int i = 0; i < n; i++) {
        // 跳过不满足边界条件的元素
        if (nums[i] < minK || nums[i] > maxK) {
            continue;
        }
        
        // 使用二分查找找到满足条件的右端点范围
        int leftBound = i;
        int rightBound = n - 1;
        int validRightStart = -1;
        int validRightEnd = -1;
        
        // 找到第一个满足条件的右端点
        int low = i, high = n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int minVal = st.queryMin(i, mid);
            int maxVal = st.queryMax(i, mid);
            
            if (minVal == minK && maxVal == maxK) {
                validRightStart = mid;
                high = mid - 1;
            } else if (minVal < minK || maxVal > maxK) {
                low = mid + 1;
            } else {
                low = mid + 1;
            }
        }
        
        if (validRightStart == -1) {
            continue;
        }
        
        // 找到最后一个满足条件的右端点
        low = validRightStart;
        high = n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int minVal = st.queryMin(i, mid);
            int maxVal = st.queryMax(i, mid);
            
            if (minVal == minK && maxVal == maxK) {
                validRightEnd = mid;
                low = mid + 1;
            } else if (minVal < minK || maxVal > maxK) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        
        // 计算满足条件的子数组数量
        if (validRightStart != -1 && validRightEnd != -1) {
            count += validRightEnd - validRightStart + 1;
        }
    }
    
    return count;
}

/**
 * 方法2：优化的数学解法（最优解）
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * @param nums 输入数组
 * @param minK 最小值边界
 * @param maxK 最大值边界
 * @return 定界子数组的数目
 */
long long countSubarraysOptimized(const vector<int>& nums, int minK, int maxK) {
    long long result = 0;
    int badIdx = -1, leftIdx = -1, rightIdx = -1;
    
    for (int i = 0; i < nums.size(); ++i) {
        // 如果当前元素超出边界，则更新badIdx
        if (!(minK <= nums[i] && nums[i] <= maxK)) {
            badIdx = i;
        }
        
        // 更新最近的minK位置
        if (nums[i] == minK) {
            leftIdx = i;
        }
        
        // 更新最近的maxK位置
        if (nums[i] == maxK) {
            rightIdx = i;
        }
        
        // 计算以当前位置结尾的有效子数组数量
        result += max(0, min(leftIdx, rightIdx) - badIdx);
    }
    
    return result;
}

/**
 * 方法3：滑动窗口解法
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * @param nums 输入数组
 * @param minK 最小值边界
 * @param maxK 最大值边界
 * @return 定界子数组的数目
 */
long long countSubarraysSlidingWindow(const vector<int>& nums, int minK, int maxK) {
    long long result = 0;
    int start = 0;
    int minPos = -1, maxPos = -1;
    
    for (int i = 0; i < nums.size(); i++) {
        // 如果当前元素超出边界，重置窗口
        if (nums[i] < minK || nums[i] > maxK) {
            start = i + 1;
            minPos = -1;
            maxPos = -1;
        } else {
            // 更新minK和maxK的位置
            if (nums[i] == minK) {
                minPos = i;
            }
            if (nums[i] == maxK) {
                maxPos = i;
            }
            
            // 如果已经找到minK和maxK，计算有效子数组数量
            if (minPos != -1 && maxPos != -1) {
                result += min(minPos, maxPos) - start + 1;
            }
        }
    }
    
    return result;
}

/**
 * 测试函数
 */
void testCountSubarraysWithFixedBounds() {
    cout << "=== 测试 LeetCode 2444. 统计定界子数组的数目 ===" << endl;
    
    // 测试用例1
    vector<int> nums1 = {1, 3, 5, 2, 7, 5};
    int minK1 = 1, maxK1 = 5;
    cout << "测试用例1:" << endl;
    cout << "数组: [1, 3, 5, 2, 7, 5]" << endl;
    cout << "minK: " << minK1 << ", maxK: " << maxK1 << endl;
    cout << "稀疏表解法结果: " << countSubarraysWithSparseTable(nums1, minK1, maxK1) << endl;
    cout << "优化解法结果: " << countSubarraysOptimized(nums1, minK1, maxK1) << endl;
    cout << "滑动窗口解法结果: " << countSubarraysSlidingWindow(nums1, minK1, maxK1) << endl;
    cout << "期望结果: 2" << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {1, 1, 1, 1};
    int minK2 = 1, maxK2 = 1;
    cout << "测试用例2:" << endl;
    cout << "数组: [1, 1, 1, 1]" << endl;
    cout << "minK: " << minK2 << ", maxK: " << maxK2 << endl;
    cout << "稀疏表解法结果: " << countSubarraysWithSparseTable(nums2, minK2, maxK2) << endl;
    cout << "优化解法结果: " << countSubarraysOptimized(nums2, minK2, maxK2) << endl;
    cout << "滑动窗口解法结果: " << countSubarraysSlidingWindow(nums2, minK2, maxK2) << endl;
    cout << "期望结果: 10" << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 3, 5, 2, 7, 5, 1, 3, 5};
    int minK3 = 1, maxK3 = 5;
    cout << "测试用例3:" << endl;
    cout << "数组: [1, 3, 5, 2, 7, 5, 1, 3, 5]" << endl;
    cout << "minK: " << minK3 << ", maxK: " << maxK3 << endl;
    cout << "稀疏表解法结果: " << countSubarraysWithSparseTable(nums3, minK3, maxK3) << endl;
    cout << "优化解法结果: " << countSubarraysOptimized(nums3, minK3, maxK3) << endl;
    cout << "滑动窗口解法结果: " << countSubarraysSlidingWindow(nums3, minK3, maxK3) << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    mt19937 gen(42);
    uniform_int_distribution<> dis(1, 100);
    
    int arraySize = 10000;
    vector<int> nums(arraySize);
    for (int i = 0; i < arraySize; i++) {
        nums[i] = dis(gen);
    }
    int minK = 10, maxK = 50;
    
    auto start_time = chrono::high_resolution_clock::now();
    long long result1 = countSubarraysOptimized(nums, minK, maxK);
    auto end_time = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "优化解法处理" << arraySize << "个元素时间: " << duration.count() / 1000.0 << " ms, 结果: " << result1 << endl;
    
    start_time = chrono::high_resolution_clock::now();
    long long result2 = countSubarraysSlidingWindow(nums, minK, maxK);
    end_time = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(end_time - start_time);
    cout << "滑动窗口解法处理" << arraySize << "个元素时间: " << duration.count() / 1000.0 << " ms, 结果: " << result2 << endl;
}

int main() {
    testCountSubarraysWithFixedBounds();
    return 0;
}