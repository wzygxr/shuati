#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <algorithm>
#include <stdexcept>

/**
 * LeetCode 659: 分割数组为连续子序列
 *
 * 解题思路：
 * 1. 使用哈希表记录每个数字出现的次数
 * 2. 使用另一个哈希表记录以某个数字结尾的子序列的长度列表
 * 3. 遍历数组，尝试将当前数字添加到合适的子序列末尾
 *
 * 时间复杂度：O(n log n)，其中n是数组的长度
 * 空间复杂度：O(n)
 */

class Solution {
public:
    /**
     * 判断数组是否可以分割成若干个长度至少为3的连续子序列
     * 
     * @param nums 整数数组
     * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    bool isPossible(std::vector<int>& nums) {
        // 输入参数校验
        if (nums.empty()) {
            throw std::invalid_argument("输入数组不能为空");
        }
        
        // 统计每个数字的出现次数
        std::unordered_map<int, int> count;
        for (int num : nums) {
            count[num]++;
        }
        
        // 记录以每个数字结尾的子序列长度（使用最小堆，优先选择长度最短的子序列）
        // 这样可以优先将当前数字添加到较短的子序列中，尽可能让所有子序列都至少达到长度3
        std::unordered_map<int, std::priority_queue<int, std::vector<int>, std::greater<int>>> endCount;
        
        // 遍历每个数字
        for (int num : nums) {
            // 如果当前数字已经用完，跳过
            if (count[num] == 0) {
                continue;
            }
            
            // 减少当前数字的剩余次数
            count[num]--;
            
            // 尝试将当前数字添加到以num-1结尾的最短子序列后面
            if (endCount.find(num - 1) != endCount.end() && !endCount[num - 1].empty()) {
                // 获取以num-1结尾的最短子序列长度
                int minLen = endCount[num - 1].top();
                endCount[num - 1].pop();
                // 将当前数字添加到该子序列后，现在子序列以num结尾，长度+1
                endCount[num].push(minLen + 1);
            } else {
                // 无法添加到现有子序列，创建一个新的子序列，长度为1
                endCount[num].push(1);
            }
        }
        
        // 检查所有子序列的长度是否都至少为3
        for (auto& pair : endCount) {
            auto& lengths = pair.second;
            while (!lengths.empty()) {
                int length = lengths.top();
                lengths.pop();
                if (length < 3) {
                    return false;
                }
            }
        }
        
        return true;
    }
};

class AlternativeSolution {
public:
    /**
     * 判断数组是否可以分割成若干个长度至少为3的连续子序列（优化版本）
     * 
     * @param nums 整数数组
     * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    bool isPossible(std::vector<int>& nums) {
        // 输入参数校验
        if (nums.empty()) {
            throw std::invalid_argument("输入数组不能为空");
        }
        
        // 统计每个数字的出现次数
        std::unordered_map<int, int> count;
        // 记录以每个数字结尾的子序列数量
        // tail[num] 表示以num结尾的子序列数量
        std::unordered_map<int, int> tail;
        
        // 第一次遍历：统计每个数字的频率
        for (int num : nums) {
            count[num]++;
        }
        
        // 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
        for (int num : nums) {
            // 如果当前数字已经用完，跳过
            if (count[num] == 0) {
                continue;
            }
            
            // 尝试将当前数字添加到以num-1结尾的子序列
            else if (tail.find(num - 1) != tail.end() && tail[num - 1] > 0) {
                count[num]--;
                tail[num - 1]--;
                tail[num]++;
            }
            // 尝试创建一个新的子序列：num, num+1, num+2
            else if (count.find(num + 1) != count.end() && count[num + 1] > 0 && 
                     count.find(num + 2) != count.end() && count[num + 2] > 0) {
                count[num]--;
                count[num + 1]--;
                count[num + 2]--;
                tail[num + 2]++;
            }
            // 无法形成有效的子序列
            else {
                return false;
            }
        }
        
        return true;
    }
};

class OptimizedSolution {
public:
    /**
     * 判断数组是否可以分割成若干个长度至少为3的连续子序列（高效版本）
     * 
     * @param nums 整数数组
     * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
     * @throws std::invalid_argument 当输入参数无效时抛出
     */
    bool isPossible(std::vector<int>& nums) {
        // 输入参数校验
        if (nums.empty()) {
            throw std::invalid_argument("输入数组不能为空");
        }
        
        // 快速检查：如果数组长度小于3，不可能分割
        if (nums.size() < 3) {
            return false;
        }
        
        // 统计每个数字的出现次数
        std::unordered_map<int, int> count;
        // 记录以每个数字结尾的子序列的最小长度
        std::unordered_map<int, std::priority_queue<int, std::vector<int>, std::greater<int>>> end;
        
        // 第一次遍历：统计每个数字的频率
        for (int num : nums) {
            count[num]++;
        }
        
        // 获取排序后的唯一数字列表
        std::vector<int> sortedNums;
        for (const auto& pair : count) {
            sortedNums.push_back(pair.first);
        }
        std::sort(sortedNums.begin(), sortedNums.end());
        
        // 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
        for (int num : sortedNums) {  // 按顺序处理数字，确保连续性
            // 处理每个数字的所有出现次数
            while (count[num] > 0) {
                // 尝试将当前数字添加到以num-1结尾的最短子序列
                if (end.find(num - 1) != end.end() && !end[num - 1].empty()) {
                    // 获取并移除最短的子序列长度
                    int length = end[num - 1].top();
                    end[num - 1].pop();
                    // 将当前数字添加到该子序列，现在子序列以num结尾，长度+1
                    end[num].push(length + 1);
                } else {
                    // 创建新子序列
                    end[num].push(1);
                }
                
                count[num]--;
            }
        }
        
        // 验证所有子序列的长度是否都至少为3
        for (auto& pair : end) {
            auto& lengths = pair.second;
            while (!lengths.empty()) {
                int length = lengths.top();
                lengths.pop();
                if (length < 3) {
                    return false;
                }
            }
        }
        
        return true;
    }
};

/**
 * 测试分割数组为连续子序列的函数
 */
void testIsPossible() {
    // 测试用例1：基本用例 - 可以分割
    std::vector<int> nums1 = {1, 2, 3, 3, 4, 5};
    // 可以分割成 [1,2,3], [3,4,5]
    std::cout << "测试用例1：" << std::endl;
    std::cout << "数组: [1, 2, 3, 3, 4, 5]" << std::endl;
    Solution solution;
    try {
        bool result1 = solution.isPossible(nums1);
        std::cout << "结果: " << (result1 ? "true" : "false") << std::endl;
        std::cout << "预期结果: true, 测试" << (result1 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例2：基本用例 - 可以分割
    std::vector<int> nums2 = {1, 2, 3, 3, 4, 4, 5, 5};
    // 可以分割成 [1,2,3,4,5], [3,4,5]
    AlternativeSolution solution2;
    try {
        bool result2 = solution2.isPossible(nums2);
        std::cout << "测试用例2：" << std::endl;
        std::cout << "数组: [1, 2, 3, 3, 4, 4, 5, 5]" << std::endl;
        std::cout << "结果: " << (result2 ? "true" : "false") << std::endl;
        std::cout << "预期结果: true, 测试" << (result2 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例3：不可以分割
    std::vector<int> nums3 = {1, 2, 3, 4, 4, 5};
    // 无法分割，因为4,4,5不能形成长度为3的连续子序列
    OptimizedSolution solution3;
    try {
        bool result3 = solution3.isPossible(nums3);
        std::cout << "测试用例3：" << std::endl;
        std::cout << "数组: [1, 2, 3, 4, 4, 5]" << std::endl;
        std::cout << "结果: " << (result3 ? "true" : "false") << std::endl;
        std::cout << "预期结果: false, 测试" << (!result3 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例4：边界情况 - 数组长度小于3
    std::vector<int> nums4 = {1, 2};
    try {
        bool result4 = solution.isPossible(nums4);
        std::cout << "测试用例4：" << std::endl;
        std::cout << "数组: [1, 2]" << std::endl;
        std::cout << "结果: " << (result4 ? "true" : "false") << std::endl;
        std::cout << "预期结果: false, 测试" << (!result4 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "测试用例4：" << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例5：较长的数组
    std::vector<int> nums5 = {1, 2, 3, 4, 5, 5, 6, 7};
    try {
        bool result5 = solution.isPossible(nums5);
        std::cout << "测试用例5：" << std::endl;
        std::cout << "数组: [1, 2, 3, 4, 5, 5, 6, 7]" << std::endl;
        std::cout << "结果: " << (result5 ? "true" : "false") << std::endl;
        std::cout << "预期结果: true, 测试" << (result5 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例6：复杂情况
    std::vector<int> nums6 = {1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7};
    try {
        bool result6 = solution.isPossible(nums6);
        std::cout << "测试用例6：" << std::endl;
        std::cout << "数组: [1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7]" << std::endl;
        std::cout << "结果: " << (result6 ? "true" : "false") << std::endl;
        std::cout << "预期结果: true, 测试" << (result6 ? "通过" : "失败") << std::endl;
    } catch (const std::exception& e) {
        std::cout << "异常: " << e.what() << std::endl;
    }
    std::cout << std::endl;
    
    // 测试用例7：异常输入
    try {
        std::vector<int> emptyNums;
        solution.isPossible(emptyNums);
        std::cout << "测试用例7：空数组异常处理 - 失败" << std::endl;
    } catch (const std::invalid_argument& e) {
        std::cout << "测试用例7：空数组异常处理 - 通过" << std::endl;
    } catch (const std::exception& e) {
        std::cout << "测试用例7：捕获到意外异常 - " << e.what() << std::endl;
    }
}

int main() {
    testIsPossible();
    return 0;
}