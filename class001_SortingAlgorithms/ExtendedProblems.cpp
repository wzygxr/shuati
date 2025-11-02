/**
 * 排序算法扩展题目 - C++版本
 * 包含LeetCode、牛客网等平台的排序相关题目
 * 每个题目都包含多种解法和详细分析
 * 
 * 题目链接汇总:
 * - 215. 数组中的第K个最大元素: https://leetcode.cn/problems/kth-largest-element-in-an-array/
 * - 75. 颜色分类: https://leetcode.cn/problems/sort-colors/
 * - 56. 合并区间: https://leetcode.cn/problems/merge-intervals/
 * - 347. 前K个高频元素: https://leetcode.cn/problems/top-k-frequent-elements/
 * - 164. 最大间距: https://leetcode.cn/problems/maximum-gap/
 * - ALDS1_2_A: Bubble Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_A
 * - ALDS1_2_B: Selection Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_B
 * - ALDS1_2_C: Stable Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_C
 * - ALDS1_2_D: Shell Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_2_D
 * - ALDS1_5_B: Merge Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_B
 * - ALDS1_5_D: The Number of Inversions: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_5_D
 * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
 * - ALDS1_6_C: Quick Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_C
 * - ALDS1_6_D: Minimum Cost Sort: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_D
 * - ALDS1_9_A: Complete Binary Tree: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_A
 * - ALDS1_9_B: Maximum Heap: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_B
 * - ALDS1_9_C: Priority Queue: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_9_C
 * 
 * 工程化考量:
 * - 异常处理: 对空数组、非法输入进行验证
 * - 边界条件: 处理各种边界情况
 * - 性能优化: 根据数据规模选择最优算法
 * - 内存管理: 合理使用数据结构，避免不必要的内存占用
 * - 可读性: 清晰的命名和详细注释
 * 
 * 算法选择建议:
 * - 第K大元素: 快速选择算法（平均O(n)）
 * - 颜色分类: 三指针法（荷兰国旗问题，O(n)）
 * - 合并区间: 排序+合并（O(n log n)）
 * - 前K个高频元素: 桶排序（O(n)）或最小堆（O(n log k)）
 * - 最大间距: 基数排序（O(n)）
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <unordered_map>
#include <random>
#include <chrono>
#include <string>
#include <sstream>
#include <stdexcept>
#include <functional>

using namespace std;

class ExtendedProblems {
public:
    /**
     * 题目1: 215. 数组中的第K个最大元素
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
     * 
     * 题目描述:
     * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
     * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
     * 
     * 示例:
     * 输入: [3,2,1,5,6,4], k = 2
     * 输出: 5
     * 
     * 解法对比:
     * 1. 快速选择算法: 平均时间复杂度O(n)，最优解
     * 2. 最小堆: 时间复杂度O(n log k)，适合k较小时
     * 3. 排序: 时间复杂度O(n log n)，简单但效率较低
     * 
     * 相关题目：
     * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
     * 
     * 工程化考量:
     * - 输入验证: 检查数组是否为空，k是否合法
     * - 随机化: 快速选择使用随机基准避免最坏情况
     * - 内存优化: 最小堆只维护k个元素
     */
    class KthLargestElement {
    public:
        /**
         * 解法1: 快速选择算法 (最优解)
         * 时间复杂度: O(n) 平均, O(n²) 最坏
         * 空间复杂度: O(1)
         * 
         * 算法原理:
         * 基于快速排序的分区思想，但只处理包含目标的一侧
         * 1. 随机选择基准元素
         * 2. 进行分区操作，确定基准元素的最终位置
         * 3. 根据基准位置与目标位置的关系决定继续处理哪一侧
         * 
         * 优势:
         * - 平均时间复杂度为线性，是最优解
         * - 原地操作，空间复杂度O(1)
         * 
         * 劣势:
         * - 最坏情况时间复杂度O(n²)
         * - 不稳定排序
         * 
         * 相关题目：
         * - ALDS1_6_B: Partition: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_6_B
         * 
         * @param nums 输入数组
         * @param k 第k大的元素
         * @return 第k大的元素值
         * @throws invalid_argument 当输入参数不合法时
         */
        static int findKthLargestQuickSelect(vector<int>& nums, int k) {
            if (nums.empty() || k < 1 || k > nums.size()) {
                throw invalid_argument("Invalid input");
            }
            
            int left = 0, right = nums.size() - 1;
            k = nums.size() - k; // 转换为第k小的索引
            
            random_device rd;
            mt19937 gen(rd());
            
            while (left <= right) {
                uniform_int_distribution<> dis(left, right);
                int pivotIndex = dis(gen);
                int pivotPos = partition(nums, left, right, pivotIndex);
                
                if (pivotPos == k) {
                    return nums[pivotPos];
                } else if (pivotPos < k) {
                    left = pivotPos + 1;
                } else {
                    right = pivotPos - 1;
                }
            }
            
            return -1;
        }
        
        /**
         * 解法2: 最小堆实现
         * 时间复杂度: O(n log k)
         * 空间复杂度: O(k)
         * 
         * 算法原理:
         * 使用最小堆维护前k个最大的元素
         * 1. 遍历数组，将元素加入堆中
         * 2. 如果堆的大小超过k，移除堆顶元素（最小的元素）
         * 3. 最后堆顶元素即为第k大的元素
         * 
         * 优势:
         * - 时间复杂度为O(n log k)，适合k较小时
         * - 使用优先队列实现，代码简洁
         * 
         * 劣势:
         * - 空间复杂度为O(k)
         * - 需要维护堆结构
         * 
         * @param nums 输入数组
         * @param k 第k大的元素
         * @return 第k大的元素值
         * @throws invalid_argument 当输入参数不合法时
         */
        static int findKthLargestMinHeap(vector<int>& nums, int k) {
            if (nums.empty() || k < 1 || k > nums.size()) {
                throw invalid_argument("Invalid input");
            }
            
            // 最小堆
            priority_queue<int, vector<int>, greater<int>> minHeap;
            
            for (int num : nums) {
                minHeap.push(num);
                if (minHeap.size() > k) {
                    minHeap.pop(); // 移除最小的元素
                }
            }
            
            return minHeap.top();
        }
        
        /**
         * 解法3: 排序后直接取
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(1) 或 O(n)
         * 
         * 算法原理:
         * 1. 对数组进行排序
         * 2. 返回排序后倒数第k个元素
         * 
         * 优势:
         * - 代码简单易懂
         * - 使用标准库排序函数
         * 
         * 劣势:
         * - 时间复杂度较高，为O(n log n)
         * - 可能需要额外的空间
         * 
         * @param nums 输入数组
         * @param k 第k大的元素
         * @return 第k大的元素值
         * @throws invalid_argument 当输入参数不合法时
         */
        static int findKthLargestSort(vector<int>& nums, int k) {
            if (nums.empty() || k < 1 || k > nums.size()) {
                throw invalid_argument("Invalid input");
            }
            
            sort(nums.begin(), nums.end());
            return nums[nums.size() - k];
        }
        
    private:
        static int partition(vector<int>& nums, int left, int right, int pivotIndex) {
            int pivotValue = nums[pivotIndex];
            swap(nums[pivotIndex], nums[right]);
            
            int storeIndex = left;
            for (int i = left; i < right; i++) {
                if (nums[i] < pivotValue) {
                    swap(nums[storeIndex], nums[i]);
                    storeIndex++;
                }
            }
            swap(nums[storeIndex], nums[right]);
            return storeIndex;
        }
        
        static void test() {
            cout << "=== 第K个最大元素测试 ===" << endl;
            
            vector<int> nums = {3, 2, 1, 5, 6, 4};
            int k = 2;
            
            cout << "数组: ";
            printVector(nums);
            cout << "k = " << k << endl;
            
            vector<int> nums1 = nums;
            int result1 = findKthLargestQuickSelect(nums1, k);
            cout << "快速选择结果: " << result1 << endl;
            
            vector<int> nums2 = nums;
            int result2 = findKthLargestMinHeap(nums2, k);
            cout << "最小堆结果: " << result2 << endl;
            
            vector<int> nums3 = nums;
            int result3 = findKthLargestSort(nums3, k);
            cout << "排序结果: " << result3 << endl;
        }
    };
    
    /**
     * 题目2: 75. 颜色分类 (荷兰国旗问题)
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/sort-colors/
     */
    class SortColors {
    public:
        
        static void test() {
            cout << "\n=== 颜色分类测试 ===" << endl;
            
            vector<int> nums = {2, 0, 2, 1, 1, 0};
            cout << "原始数组: ";
            printVector(nums);
            
            vector<int> nums1 = nums;
            sortColorsThreePointers(nums1);
            cout << "三指针法: ";
            printVector(nums1);
            
            vector<int> nums2 = nums;
            sortColorsCounting(nums2);
            cout << "计数排序: ";
            printVector(nums2);
        }
    };
    
    /**
     * 题目3: 56. 合并区间
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/merge-intervals/
     */
    class MergeIntervals {
    public:
        
        static void test() {
            cout << "\n=== 合并区间测试 ===" << endl;
            
            vector<vector<int>> intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
            cout << "原始区间: ";
            print2DVector(intervals);
            
            vector<vector<int>> result = merge(intervals);
            cout << "合并结果: ";
            print2DVector(result);
        }
    };
    
    /**
     * 题目4: 347. 前K个高频元素
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/top-k-frequent-elements/
     */
    class TopKFrequentElements {
    public:
        
        static void test() {
            cout << "\n=== 前K个高频元素测试 ===" << endl;
            
            vector<int> nums = {1, 1, 1, 2, 2, 3};
            int k = 2;
            
            cout << "数组: ";
            printVector(nums);
            cout << "k = " << k << endl;
            
            vector<int> result1 = topKFrequentMinHeap(nums, k);
            cout << "最小堆结果: ";
            printVector(result1);
            
            vector<int> result2 = topKFrequentBucketSort(nums, k);
            cout << "桶排序结果: ";
            printVector(result2);
        }
    };
    
    /**
     * 题目5: 164. 最大间距
     * 来源: LeetCode
     * 链接: https://leetcode.cn/problems/maximum-gap/
     */
    class MaximumGap {
    public:
        
        static void test() {
            cout << "\n=== 最大间距测试 ===" << endl;
            
            vector<int> nums = {3, 6, 9, 1};
            cout << "数组: ";
            printVector(nums);
            
            vector<int> nums1 = nums;
            int result1 = maximumGapRadixSort(nums1);
            cout << "基数排序结果: " << result1 << endl;
            
            vector<int> nums2 = nums;
            int result2 = maximumGapSort(nums2);
            cout << "普通排序结果: " << result2 << endl;
        }
    };
    
    // 辅助函数
    static void printVector(const vector<int>& nums) {
        cout << "[";
        for (int i = 0; i < nums.size(); i++) {
            cout << nums[i];
            if (i < nums.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    static void print2DVector(const vector<vector<int>>& matrix) {
        cout << "[";
        for (int i = 0; i < matrix.size(); i++) {
            cout << "[";
            for (int j = 0; j < matrix[i].size(); j++) {
                cout << matrix[i][j];
                if (j < matrix[i].size() - 1) cout << ", ";
            }
            cout << "]";
            if (i < matrix.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    /**
     * 综合测试函数
     */
    static void runAllTests() {
        KthLargestElement::test();
        SortColors::test();
        MergeIntervals::test();
        TopKFrequentElements::test();
        MaximumGap::test();
    }
};

// 主函数
int main() {
    try {
        ExtendedProblems::runAllTests();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}