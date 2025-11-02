// LeetCode 315. 计算右侧小于当前元素的个数
// 给定一个整数数组 nums，按要求返回一个新数组 counts。
// 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_set>
#include <unordered_map>
#include <climits>
#include <chrono>
using namespace std;
using namespace std::chrono;

/**
 * 计算右侧小于当前元素的个数 - 树状数组解法
 * 
 * 解题思路:
 * 1. 题目要求计算每个元素右侧小于它的元素个数
 * 2. 我们可以将问题转化为：对于每个元素nums[i]，统计有多少个元素nums[j] (j > i)满足 nums[j] < nums[i]
 * 3. 使用树状数组可以高效地进行这类统计
 * 4. 具体步骤：
 *    a. 将数组元素离散化，以处理可能的大范围数值
 *    b. 从右到左遍历数组，对于每个元素nums[i]：
 *       - 查询树状数组中小于nums[i]的元素个数（即前缀和）
 *       - 将nums[i]加入树状数组
 *    c. 这样就能保证每次查询的都是当前元素右侧的元素
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 构建和操作树状数组: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 树状数组: O(n)
 * - 离散化数组和映射: O(n)
 * - 结果数组: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 离散化处理：由于输入数组可能包含很大范围的整数，离散化可以有效减少空间使用
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 数据类型溢出：使用long long类型避免中间计算时的溢出
 * 4. 异常输入检查：验证输入的有效性
 * 5. 代码可读性：使用清晰的变量命名和详细的注释
 */

class Solution {
private:
    // 树状数组类
    class FenwickTree {
    private:
        vector<int> tree;  // 树状数组
        int size;          // 数组大小

    public:
        /**
         * 构造函数
         * @param size 树状数组大小
         */
        FenwickTree(int size) : size(size) {
            tree.resize(size + 1, 0);  // 树状数组下标从1开始
        }

        /**
         * lowbit操作，获取x的二进制表示中最低位的1所代表的值
         * @param x 输入整数
         * @return x & (-x)
         */
        int lowbit(int x) {
            return x & (-x);
        }

        /**
         * 在指定位置增加delta
         * @param index 索引位置（从1开始）
         * @param delta 增加的值
         */
        void update(int index, int delta) {
            // 沿树状数组向上更新所有相关节点
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }

        /**
         * 查询前缀和[1, index]
         * @param index 查询的右边界（从1开始）
         * @return 前缀和
         */
        int query(int index) {
            int sum = 0;
            // 沿树状数组向下累加所有相关节点的值
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
    };

    /**
     * 归并排序的合并操作，用于归并排序解法
     */
    void merge(vector<int>& nums, vector<int>& indexes, int left, int mid, int right, vector<int>& result) {
        vector<int> tempIndexes(right - left + 1);
        int i = left, j = mid + 1, k = 0;
        
        // 合并两个有序数组，并计算右侧小于当前元素的个数
        while (i <= mid && j <= right) {
            if (nums[indexes[i]] <= nums[indexes[j]]) {
                // 右侧比当前元素小的数量为j - (mid + 1)
                result[indexes[i]] += j - (mid + 1);
                tempIndexes[k++] = indexes[i++];
            } else {
                tempIndexes[k++] = indexes[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            result[indexes[i]] += j - (mid + 1);
            tempIndexes[k++] = indexes[i++];
        }
        
        while (j <= right) {
            tempIndexes[k++] = indexes[j++];
        }
        
        // 将临时数组复制回原数组
        for (int p = 0; p < tempIndexes.size(); p++) {
            indexes[left + p] = tempIndexes[p];
        }
    }

    /**
     * 归并排序的递归实现，用于归并排序解法
     */
    void mergeSort(vector<int>& nums, vector<int>& indexes, int left, int right, vector<int>& result) {
        if (left >= right) return;
        
        int mid = left + (right - left) / 2;
        mergeSort(nums, indexes, left, mid, result);
        mergeSort(nums, indexes, mid + 1, right, result);
        merge(nums, indexes, left, mid, right, result);
    }

public:
    /**
     * 计算右侧小于当前元素的个数 - 树状数组解法
     * @param nums 输入数组
     * @return 结果数组
     */
    vector<int> countSmaller(vector<int>& nums) {
        // 边界条件检查
        if (nums.empty()) {
            return {};
        }

        int n = nums.size();
        vector<int> result(n, 0);

        // 离散化处理
        // 1. 收集所有可能的数值
        unordered_set<long long> valuesSet;
        for (int num : nums) {
            valuesSet.insert((long long)num);
        }

        // 2. 排序并去重
        vector<long long> sortedValues(valuesSet.begin(), valuesSet.end());
        sort(sortedValues.begin(), sortedValues.end());

        // 3. 建立值到索引的映射
        unordered_map<long long, int> valueToIndex;
        for (int i = 0; i < sortedValues.size(); i++) {
            valueToIndex[sortedValues[i]] = i + 1;  // 索引从1开始
        }

        // 创建树状数组
        FenwickTree fenwickTree(sortedValues.size());

        // 从右到左遍历数组
        for (int i = n - 1; i >= 0; i--) {
            long long currentValue = (long long)nums[i];
            // 查询比当前值小的元素个数
            int count = 0;
            // 找到当前值在离散化数组中的位置
            int index = valueToIndex[currentValue];
            // 查询比当前值小的元素个数，即查询[1, index-1]的前缀和
            if (index > 1) {
                count = fenwickTree.query(index - 1);
            }
            // 将结果保存
            result[i] = count;
            // 将当前值加入树状数组
            fenwickTree.update(index, 1);
        }

        return result;
    }

    /**
     * 另一种解法：归并排序过程中计算逆序对
     * 这种方法也能在O(n log n)时间内解决问题
     * @param nums 输入数组
     * @return 结果数组
     */
    vector<int> countSmallerMergeSort(vector<int>& nums) {
        int n = nums.size();
        vector<int> result(n, 0);
        if (n == 0) return result;
        
        // 创建索引数组，用于跟踪元素原始位置
        vector<int> indexes(n);
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
        
        // 归并排序过程中计算右侧小于当前元素的个数
        mergeSort(nums, indexes, 0, n - 1, result);
        return result;
    }

    /**
     * 暴力解法（仅供比较，时间复杂度较高）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(n)
     * @param nums 输入数组
     * @return 结果数组
     */
    vector<int> countSmallerBruteForce(vector<int>& nums) {
        vector<int> result;
        if (nums.empty()) {
            return result;
        }
        
        int n = nums.size();
        result.resize(n, 0);
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = i + 1; j < n; j++) {
                if (nums[j] < nums[i]) {
                    count++;
                }
            }
            result[i] = count;
        }
        return result;
    }
};

/**
 * 打印向量的辅助函数
 */
void printVector(const vector<int>& vec) {
    cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        cout << vec[i];
        if (i < vec.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试函数
 */
void testSolution() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {5, 2, 6, 1};
    cout << "测试用例1:" << endl;
    cout << "输入: [5, 2, 6, 1]" << endl;
    vector<int> result1 = solution.countSmaller(nums1);
    cout << "树状数组解法结果: ";
    printVector(result1);  // 期望输出: [2, 1, 1, 0]
    
    vector<int> result1_mergesort = solution.countSmallerMergeSort(nums1);
    cout << "归并排序解法结果: ";
    printVector(result1_mergesort);  // 期望输出: [2, 1, 1, 0]
    
    vector<int> result1_brute = solution.countSmallerBruteForce(nums1);
    cout << "暴力解法结果: ";
    printVector(result1_brute);  // 期望输出: [2, 1, 1, 0]
    
    // 测试用例2
    vector<int> nums2 = {-1, -1};
    cout << "\n测试用例2:" << endl;
    cout << "输入: [-1, -1]" << endl;
    vector<int> result2 = solution.countSmaller(nums2);
    cout << "树状数组解法结果: ";
    printVector(result2);  // 期望输出: [0, 0]
    
    // 测试用例3 - 空数组
    vector<int> nums3 = {};
    cout << "\n测试用例3:" << endl;
    cout << "输入: []" << endl;
    vector<int> result3 = solution.countSmaller(nums3);
    cout << "树状数组解法结果: ";
    printVector(result3);  // 期望输出: []
    
    // 测试用例4 - 大规模数据
    int size = 1000;
    vector<int> nums4(size);
    for (int i = 0; i < size; i++) {
        nums4[i] = size - i;
    }
    cout << "\n测试用例4 (大规模逆序数组):" << endl;
    cout << "数组长度: " << size << endl;
    
    // 测量树状数组解法的时间
    auto start1 = high_resolution_clock::now();
    vector<int> result4 = solution.countSmaller(nums4);
    auto end1 = high_resolution_clock::now();
    auto duration1 = duration_cast<microseconds>(end1 - start1);
    cout << "树状数组解法耗时: " << duration1.count() / 1000.0 << "ms" << endl;
    
    // 测量归并排序解法的时间
    auto start2 = high_resolution_clock::now();
    vector<int> result4_mergesort = solution.countSmallerMergeSort(nums4);
    auto end2 = high_resolution_clock::now();
    auto duration2 = duration_cast<microseconds>(end2 - start2);
    cout << "归并排序解法耗时: " << duration2.count() / 1000.0 << "ms" << endl;
    
    // 验证两种方法结果是否一致
    bool resultsEqual = (result4 == result4_mergesort);
    cout << "两种方法结果一致: " << (resultsEqual ? "true" : "false") << endl;
    
    // 对比暴力解法（仅在小规模数据上测试）
    if (size <= 1000) {
        int smallSize = min(size, 500);  // 限制暴力解法的数组大小，避免超时
        vector<int> smallNums(nums4.begin(), nums4.begin() + smallSize);
        
        auto start3 = high_resolution_clock::now();
        vector<int> result4_brute = solution.countSmallerBruteForce(smallNums);
        auto end3 = high_resolution_clock::now();
        auto duration3 = duration_cast<microseconds>(end3 - start3);
        cout << "暴力解法(前" << smallSize << "个元素)耗时: " << duration3.count() / 1000.0 << "ms" << endl;
        
        // 验证暴力解法与树状数组解法在前smallSize个元素上是否一致
        vector<int> result4_small = solution.countSmaller(smallNums);
        bool bruteEqual = (result4_small == result4_brute);
        cout << "暴力解法与树状数组解法结果一致: " << (bruteEqual ? "true" : "false") << endl;
    }
}

// 主函数
int main() {
    testSolution();
    return 0;
}