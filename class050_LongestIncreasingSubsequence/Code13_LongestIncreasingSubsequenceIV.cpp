#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <chrono>

/**
 * 最长递增子序列 IV - LeetCode 2407
 * 题目来源：https://leetcode.cn/problems/longest-increasing-subsequence-iv/
 * 难度：困难
 * 题目描述：给你一个整数数组 nums 和一个整数 k 。找到最长子序列的长度，满足子序列中的每个元素 严格递增 ，且子序列中相邻元素的差的绝对值 至少 为 k 。
 * 子序列 是指从原数组中删除一些元素（可能一个也不删除）后，剩余元素保持原来顺序而形成的新数组。
 * 
 * 核心思路：
 * 1. 这道题是LIS问题的一个变体，增加了相邻元素差的绝对值至少为k的约束
 * 2. 使用动态规划 + 线段树优化来解决这个问题
 * 3. 对于每个元素nums[i]，我们需要找到所有满足nums[j] < nums[i]且nums[i] - nums[j] >= k的j，然后dp[i] = max(dp[j]) + 1
 * 4. 使用线段树来高效查询区间内的最大值
 * 
 * 复杂度分析：
 * 时间复杂度：O(n log n)，其中n是数组长度。离散化需要O(n log n)，构建线段树需要O(n)，每次查询和更新需要O(log n)，总共有n次查询和更新
 * 空间复杂度：O(n)，用于存储离散化后的值、dp数组和线段树
 */

class Solution {
private:
    /**
     * 线段树节点类
     */
    struct SegmentTreeNode {
        int start, end;
        int max;
        SegmentTreeNode* left;
        SegmentTreeNode* right;
        
        SegmentTreeNode(int start, int end) : start(start), end(end), max(0), left(nullptr), right(nullptr) {}
        
        ~SegmentTreeNode() {
            delete left;
            delete right;
        }
    };
    
    /**
     * 构建线段树
     */
    SegmentTreeNode* buildSegmentTree(int start, int end) {
        SegmentTreeNode* node = new SegmentTreeNode(start, end);
        if (start == end) {
            return node;
        }
        
        int mid = start + (end - start) / 2;
        node->left = buildSegmentTree(start, mid);
        node->right = buildSegmentTree(mid + 1, end);
        
        return node;
    }
    
    /**
     * 查询区间内的最大值
     */
    int queryMax(SegmentTreeNode* node, int start, int end) {
        if (start > node->end || end < node->start) {
            return 0;
        }
        
        if (start <= node->start && end >= node->end) {
            return node->max;
        }
        
        int leftMax = queryMax(node->left, start, end);
        int rightMax = queryMax(node->right, start, end);
        
        return std::max(leftMax, rightMax);
    }
    
    /**
     * 更新线段树中的值
     */
    void updateSegmentTree(SegmentTreeNode* node, int index, int value) {
        if (node->start == node->end && node->start == index) {
            node->max = std::max(node->max, value);
            return;
        }
        
        int mid = node->start + (node->end - node->start) / 2;
        if (index <= mid) {
            updateSegmentTree(node->left, index, value);
        } else {
            updateSegmentTree(node->right, index, value);
        }
        
        node->max = std::max(node->left->max, node->right->max);
    }
    
    /**
     * 数组实现的线段树查询方法
     */
    int queryMax(const std::vector<int>& tree, int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0;
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = start + (end - start) / 2;
        int leftMax = queryMax(tree, 2 * node + 1, start, mid, l, r);
        int rightMax = queryMax(tree, 2 * node + 2, mid + 1, end, l, r);
        return std::max(leftMax, rightMax);
    }
    
    /**
     * 数组实现的线段树更新方法
     */
    void updateSegmentTree(std::vector<int>& tree, int node, int start, int end, int idx, int val) {
        if (start == end) {
            tree[node] = std::max(tree[node], val);
        } else {
            int mid = start + (end - start) / 2;
            if (idx <= mid) {
                updateSegmentTree(tree, 2 * node + 1, start, mid, idx, val);
            } else {
                updateSegmentTree(tree, 2 * node + 2, mid + 1, end, idx, val);
            }
            tree[node] = std::max(tree[2 * node + 1], tree[2 * node + 2]);
        }
    }

public:
    /**
     * 最优解法：动态规划 + 线段树优化
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    int lengthOfLIS(const std::vector<int>& nums, int k) {
        // 离散化处理
        std::vector<int> sortedNums(nums);
        std::sort(sortedNums.begin(), sortedNums.end());
        
        // 创建映射，将原始值映射到离散化后的值
        std::unordered_map<int, int> valueToIndex;
        int index = 1; // 从1开始，便于线段树处理
        for (int num : sortedNums) {
            if (valueToIndex.find(num) == valueToIndex.end()) {
                valueToIndex[num] = index++;
            }
        }
        
        int n = valueToIndex.size();
        SegmentTreeNode* root = buildSegmentTree(1, n);
        
        int maxLength = 0;
        for (int num : nums) {
            // 找到所有小于num且num - value >= k的元素的最大长度
            // 即找到所有value <= num - k的元素
            int target = num - k;
            // 找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.size() - 1;
            int best = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            int currentLength = 1;
            if (best != -1) {
                int queryEnd = valueToIndex[sortedNums[best]];
                currentLength = queryMax(root, 1, queryEnd) + 1;
            }
            
            // 更新线段树
            int numIndex = valueToIndex[num];
            updateSegmentTree(root, numIndex, currentLength);
            
            maxLength = std::max(maxLength, currentLength);
        }
        
        // 释放内存
        delete root;
        
        return maxLength;
    }
    
    /**
     * 另一种实现方式：使用数组实现线段树
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    int lengthOfLISAlternative(const std::vector<int>& nums, int k) {
        // 离散化处理
        std::vector<int> sortedNums(nums);
        std::sort(sortedNums.begin(), sortedNums.end());
        
        // 创建映射，将原始值映射到离散化后的值
        std::unordered_map<int, int> valueToIndex;
        int index = 1;
        for (int num : sortedNums) {
            if (valueToIndex.find(num) == valueToIndex.end()) {
                valueToIndex[num] = index++;
            }
        }
        
        int n = valueToIndex.size();
        // 使用数组模拟线段树
        std::vector<int> segmentTree(4 * n, 0);
        
        int maxLength = 0;
        for (int num : nums) {
            int target = num - k;
            // 找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.size() - 1;
            int best = -1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            int currentLength = 1;
            if (best != -1) {
                int queryEnd = valueToIndex[sortedNums[best]];
                currentLength = queryMax(segmentTree, 0, 1, n, 1, queryEnd) + 1;
            }
            
            // 更新线段树
            int numIndex = valueToIndex[num];
            updateSegmentTree(segmentTree, 0, 1, n, numIndex, currentLength);
            
            maxLength = std::max(maxLength, currentLength);
        }
        
        return maxLength;
    }
    
    /**
     * 解释性更强的版本，添加了更多注释和中间变量
     * @param nums 输入数组
     * @param k 相邻元素差的最小绝对值
     * @return 满足条件的最长递增子序列的长度
     */
    int lengthOfLISExplained(const std::vector<int>& nums, int k) {
        // 步骤1: 离散化处理
        // 由于数组中的值可能很大，我们需要对其进行离散化，以便使用线段树
        std::vector<int> sortedNums(nums);
        std::sort(sortedNums.begin(), sortedNums.end());
        
        // 创建值到索引的映射，将原始值映射到较小的范围内
        std::unordered_map<int, int> valueToIndex;
        int index = 1; // 从1开始，便于线段树处理
        for (int num : sortedNums) {
            if (valueToIndex.find(num) == valueToIndex.end()) {
                valueToIndex[num] = index++;
            }
        }
        
        // 步骤2: 初始化线段树
        int n = valueToIndex.size();
        SegmentTreeNode* root = buildSegmentTree(1, n);
        
        // 步骤3: 动态规划 + 线段树优化
        int maxLength = 0; // 用于保存最长子序列的长度
        
        for (int num : nums) {
            // 对于每个元素num，我们需要找到所有满足nums[j] < num且num - nums[j] >= k的j
            // 即nums[j] <= num - k
            int target = num - k;
            
            // 二分查找找到最大的不大于target的值
            int left = 0;
            int right = sortedNums.size() - 1;
            int best = -1; // 记录找到的索引
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (sortedNums[mid] <= target) {
                    best = mid; // 找到一个可能的候选
                    left = mid + 1; // 继续向右查找更大的符合条件的值
                } else {
                    right = mid - 1; // 向左查找
                }
            }
            
            // 计算以当前元素结尾的最长递增子序列长度
            int currentLength = 1; // 至少包含当前元素
            if (best != -1) { // 如果找到了符合条件的元素
                int queryEnd = valueToIndex[sortedNums[best]];
                // 查询区间[1, queryEnd]内的最大值，并加1
                currentLength = queryMax(root, 1, queryEnd) + 1;
            }
            
            // 更新线段树中当前值的位置
            int numIndex = valueToIndex[num];
            updateSegmentTree(root, numIndex, currentLength);
            
            // 更新全局最长子序列长度
            maxLength = std::max(maxLength, currentLength);
        }
        
        // 释放内存
        delete root;
        
        return maxLength;
    }
};

// 辅助函数：打印数组
void printArray(const std::vector<int>& nums) {
    std::cout << "[";
    for (size_t i = 0; i < nums.size(); ++i) {
        std::cout << nums[i];
        if (i < nums.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

// 运行所有解法的对比测试
void runAllSolutionsTest(Solution& solution, const std::vector<int>& nums, int k) {
    std::cout << "\n对比测试：" << std::endl;
    std::cout << "数组: ";
    printArray(nums);
    std::cout << "k: " << k << std::endl;
    
    // 测试线段树节点实现
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.lengthOfLIS(nums, k);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "线段树节点实现结果: " << result1 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试数组实现的线段树
    start = std::chrono::high_resolution_clock::now();
    int result2 = solution.lengthOfLISAlternative(nums, k);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "数组实现线段树结果: " << result2 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    // 测试解释性版本
    start = std::chrono::high_resolution_clock::now();
    int result3 = solution.lengthOfLISExplained(nums, k);
    end = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
    std::cout << "解释性版本结果: " << result3 << std::endl;
    std::cout << "耗时: " << duration << " μs" << std::endl;
    
    std::cout << "----------------------------------------" << std::endl;
}

// 性能测试函数
void performanceTest(Solution& solution, int size) {
    // 生成随机测试数据
    std::vector<int> nums(size);
    for (int i = 0; i < size; i++) {
        nums[i] = rand() % 10000;
    }
    int k = rand() % 100;
    
    std::cout << "\n性能测试：数组大小 = " << size << std::endl;
    
    // 测试线段树节点实现
    auto start = std::chrono::high_resolution_clock::now();
    int result1 = solution.lengthOfLIS(nums, k);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
    std::cout << "线段树节点实现耗时: " << duration << " ms, 结果: " << result1 << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {4, 2, 1, 4, 3, 4, 5, 8, 15};
    int k1 = 3;
    std::cout << "测试用例1：" << std::endl;
    std::cout << "数组: ";
    printArray(nums1);
    std::cout << "k: " << k1 << std::endl;
    std::cout << "结果: " << solution.lengthOfLIS(nums1, k1) << "，预期: 5" << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {7, 4, 5, 1, 8, 12, 4, 7};
    int k2 = 5;
    std::cout << "测试用例2：" << std::endl;
    std::cout << "数组: ";
    printArray(nums2);
    std::cout << "k: " << k2 << std::endl;
    std::cout << "结果: " << solution.lengthOfLIS(nums2, k2) << "，预期: 3" << std::endl;
    std::cout << std::endl;
    
    // 测试用例3：边界情况
    std::vector<int> nums3 = {1};
    int k3 = 1;
    std::cout << "测试用例3：" << std::endl;
    std::cout << "数组: ";
    printArray(nums3);
    std::cout << "k: " << k3 << std::endl;
    std::cout << "结果: " << solution.lengthOfLIS(nums3, k3) << "，预期: 1" << std::endl;
    
    // 运行所有解法的对比测试
    runAllSolutionsTest(solution, nums1, k1);
    runAllSolutionsTest(solution, nums2, k2);
    runAllSolutionsTest(solution, nums3, k3);
    
    // 性能测试
    std::cout << "性能测试:" << std::endl;
    std::cout << "----------------------------------------" << std::endl;
    performanceTest(solution, 1000);
    performanceTest(solution, 5000);
    
    // 特殊测试用例：严格递增序列，且相邻差大于等于k
    std::cout << "\n特殊测试用例：严格递增序列，且相邻差大于等于k" << std::endl;
    std::vector<int> numsIncreasing = {1, 4, 7, 10, 13};
    int kIncreasing = 3;
    std::cout << "数组: ";
    printArray(numsIncreasing);
    std::cout << "k: " << kIncreasing << std::endl;
    std::cout << "结果: " << solution.lengthOfLIS(numsIncreasing, kIncreasing) << std::endl;
    
    // 特殊测试用例：所有元素相同
    std::cout << "\n特殊测试用例：所有元素相同" << std::endl;
    std::vector<int> numsSame = {5, 5, 5, 5, 5};
    int kSame = 1;
    std::cout << "数组: ";
    printArray(numsSame);
    std::cout << "k: " << kSame << std::endl;
    std::cout << "结果: " << solution.lengthOfLIS(numsSame, kSame) << std::endl;
    
    return 0;
}