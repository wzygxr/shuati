#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <functional>
#include <numeric>

using namespace std;

/**
 * LeetCode 47: 全排列 II (Permutations II) - C++实现
 * 题目链接：https://leetcode.cn/problems/permutations-ii/
 * 题目描述：给定一个可包含重复数字的序列nums，按任意顺序返回所有不重复的全排列
 * 
 * 二项式反演应用：
 * 当序列包含重复元素时，直接生成全排列会产生重复结果。
 * 可以使用二项式反演思想结合回溯算法来避免重复排列。
 * 
 * 算法原理：
 * 1. 先对数组排序，使相同元素相邻
 * 2. 使用回溯算法生成所有排列
 * 3. 通过剪枝避免重复排列：
 *    - 如果当前元素与前一个元素相同且前一个元素未被使用，则跳过
 *    - 这样可以确保相同元素的相对顺序固定，避免重复
 * 
 * 时间复杂度分析：
 * - 最坏情况：O(n * n!) - 需要生成所有排列，每个排列需要O(n)时间复制
 * - 平均情况：由于剪枝，实际生成的排列数远小于n!
 * 
 * 空间复杂度分析：
 * - O(n) - 递归栈深度为n，标记数组大小为n
 * 
 * 工程化考量：
 * - 使用引用传递避免不必要的拷贝
 * - 排序预处理确保相同元素相邻
 * - 剪枝策略优化性能
 */
class Solution {
public:
    /**
     * 主函数：生成所有不重复的全排列
     * 
     * @param nums 输入数组，可能包含重复元素
     * @return 所有不重复的全排列
     */
    vector<vector<int>> permuteUnique(vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> current;
        vector<bool> used(nums.size(), false);
        
        // 关键步骤：先对数组排序，使相同元素相邻
        sort(nums.begin(), nums.end());
        
        // 开始回溯搜索
        backtrack(nums, used, current, result);
        
        return result;
    }
    
private:
    /**
     * 回溯算法核心函数
     * 
     * @param nums 排序后的输入数组
     * @param used 标记数组，记录哪些元素已被使用
     * @param current 当前正在构建的排列
     * @param result 存储所有排列的结果集
     */
    void backtrack(vector<int>& nums, vector<bool>& used, 
                   vector<int>& current, vector<vector<int>>& result) {
        // 终止条件：当前排列长度等于数组长度
        if (current.size() == nums.size()) {
            result.push_back(current);
            return;
        }
        
        // 遍历所有可能的下一位置选择
        for (int i = 0; i < nums.size(); i++) {
            // 剪枝条件1：当前元素已被使用
            if (used[i]) {
                continue;
            }
            
            // 剪枝条件2：避免重复排列的关键
            // 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过
            // 这样可以确保相同元素的相对顺序固定
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            
            // 选择当前元素
            used[i] = true;
            current.push_back(nums[i]);
            
            // 递归搜索下一层
            backtrack(nums, used, current, result);
            
            // 回溯：撤销选择
            current.pop_back();
            used[i] = false;
        }
    }
    
public:
    /**
     * 使用二项式反演思想计算不重复排列数（数学方法）
     * 
     * 算法原理：
     * 设数组中有k种不同的数字，每种数字出现次数为c1, c2, ..., ck
     * 则总排列数为：n! / (c1! * c2! * ... * ck!)
     * 
     * 时间复杂度：O(n) - 需要统计频率和计算阶乘
     * 空间复杂度：O(k) - 需要存储频率统计
     * 
     * @param nums 输入数组
     * @return 不重复排列的数量（数学计算结果）
     */
    long long countUniquePermutations(vector<int>& nums) {
        // 统计每个数字的出现频率
        unordered_map<int, int> freq;
        for (int num : nums) {
            freq[num]++;
        }
        
        // 计算n!
        long long numerator = factorial(nums.size());
        
        // 计算分母：c1! * c2! * ... * ck!
        long long denominator = 1;
        for (auto& pair : freq) {
            denominator *= factorial(pair.second);
        }
        
        return numerator / denominator;
    }
    
private:
    /**
     * 计算阶乘函数
     * 
     * @param n 非负整数
     * @return n! 的结果
     */
    long long factorial(int n) {
        if (n <= 1) return 1;
        long long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
};

/**
 * 单元测试函数
 */
void runTests() {
    Solution solution;
    
    // 测试用例1：[1,1,2]
    cout << "=== 测试用例1: [1,1,2] ===" << endl;
    vector<int> nums1 = {1, 1, 2};
    auto result1 = solution.permuteUnique(nums1);
    cout << "排列数量：" << result1.size() << endl;
    cout << "数学计算数量：" << solution.countUniquePermutations(nums1) << endl;
    cout << "排列结果：" << endl;
    for (auto& perm : result1) {
        cout << "[";
        for (int i = 0; i < perm.size(); i++) {
            cout << perm[i];
            if (i < perm.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
    
    // 测试用例2：[1,2,3]
    cout << "\n=== 测试用例2: [1,2,3] ===" << endl;
    vector<int> nums2 = {1, 2, 3};
    auto result2 = solution.permuteUnique(nums2);
    cout << "排列数量：" << result2.size() << endl;
    cout << "数学计算数量：" << solution.countUniquePermutations(nums2) << endl;
    
    // 测试用例3：[1,1,1]
    cout << "\n=== 测试用例3: [1,1,1] ===" << endl;
    vector<int> nums3 = {1, 1, 1};
    auto result3 = solution.permuteUnique(nums3);
    cout << "排列数量：" << result3.size() << endl;
    cout << "数学计算数量：" << solution.countUniquePermutations(nums3) << endl;
}

/**
 * 交互式测试函数
 */
void interactiveTest() {
    Solution solution;
    
    cout << "\n=== 交互式测试 ===" << endl;
    cout << "请输入数组元素（用空格分隔，输入q退出）：" << endl;
    
    string line;
    while (getline(cin, line)) {
        if (line == "q" || line == "quit") {
            break;
        }
        
        if (line.empty()) {
            continue;
        }
        
        // 解析输入
        vector<int> nums;
        size_t pos = 0;
        while (pos < line.length()) {
            size_t next_pos = line.find(' ', pos);
            if (next_pos == string::npos) {
                next_pos = line.length();
            }
            
            string num_str = line.substr(pos, next_pos - pos);
            if (!num_str.empty()) {
                try {
                    int num = stoi(num_str);
                    nums.push_back(num);
                } catch (const exception& e) {
                    cout << "输入格式错误，请重新输入：" << endl;
                    break;
                }
            }
            
            pos = next_pos + 1;
        }
        
        if (!nums.empty()) {
            auto result = solution.permuteUnique(nums);
            cout << "不重复排列数量：" << result.size() << endl;
            cout << "数学计算数量：" << solution.countUniquePermutations(nums) << endl;
            
            // 显示前几个排列（避免输出过多）
            int max_display = min(5, (int)result.size());
            cout << "前" << max_display << "个排列：" << endl;
            for (int i = 0; i < max_display; i++) {
                cout << "[";
                for (int j = 0; j < result[i].size(); j++) {
                    cout << result[i][j];
                    if (j < result[i].size() - 1) cout << ", ";
                }
                cout << "]" << endl;
            }
        }
        
        cout << "\n请输入数组元素（用空格分隔，输入q退出）：" << endl;
    }
}

/**
 * 主函数
 */
int main() {
    // 运行单元测试
    runTests();
    
    // 运行交互式测试
    interactiveTest();
    
    return 0;
}

/**
 * 算法优化思路：
 * 1. 剪枝策略：通过排序和相邻元素比较，避免生成重复排列
 * 2. 空间优化：使用原地交换的递归方法可以进一步减少空间使用
 * 3. 迭代器模式：实现排列生成器，支持逐个生成排列
 * 
 * 边界条件处理：
 * - 空数组：返回空向量
 * - 单元素数组：返回单个排列
 * - 全相同元素：只有一个排列
 * 
 * 异常场景：
 * - 输入数组为空：返回空结果
 * - 数组长度过大：考虑使用迭代器模式避免内存溢出
 * 
 * 工程化扩展：
 * - 模板化：支持不同类型的元素
 * - 并行计算：使用OpenMP或C++17并行算法
 * - 性能监控：添加性能统计和日志记录
 * - 内存池：对于频繁的内存分配，使用内存池优化
 */