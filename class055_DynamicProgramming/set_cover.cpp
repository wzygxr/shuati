// -*- coding: utf-8 -*-
/*
子集覆盖问题（Set Cover Problem）

问题描述：
给定一个全集U和一组子集S_1, S_2, ..., S_m，其中每个子集S_i是U的子集，并且有一个权值w_i。
我们需要选择一些子集，使得它们的并等于全集U，并且所选子集的权值和最小。

贪心策略：
每次选择能够覆盖最多未被覆盖的元素的子集（按权重计算性价比最高的）。

注意：子集覆盖问题是NP难的，贪心算法不能保证得到最优解，但可以得到一个近似比为ln(n)+1的解，
其中n是全集的大小。

时间复杂度：O(mn)，其中m是子集的数量，n是全集的大小
空间复杂度：O(n+m)，需要存储未覆盖的元素集合和子集信息

相关题目：
1. LeetCode 1541. 平衡括号字符串的最少插入次数
2. LeetCode 1689. 十-二进制数的最少数目
3. LeetCode 45. 跳跃游戏 II
*/

#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>
#include <stdexcept>
#include <string>
#include <climits>

using namespace std;

/**
 * 子集覆盖问题的贪心算法实现
 * 
 * @param universe 全集U，一个包含所有元素的集合
 * @param subsets 子集列表，每个子集是一个集合
 * @param weights 每个子集的权值列表，如果为空则默认为1
 * @return 一个pair，包含选中的子集索引列表和总权值
 * @throws invalid_argument 当输入无效时抛出异常
 */
pair<vector<int>, double> setCover(const unordered_set<int>& universe, 
                                   const vector<unordered_set<int>>& subsets, 
                                   const vector<double>& weights = {}) {
    // 参数验证
    if (subsets.empty()) {
        throw invalid_argument("subsets不能为空");
    }
    
    vector<double> actual_weights;
    if (weights.empty()) {
        actual_weights = vector<double>(subsets.size(), 1.0);
    } else if (weights.size() != subsets.size()) {
        throw invalid_argument("weights的长度必须与subsets相同");
    } else {
        actual_weights = weights;
    }
    
    // 检查是否可以覆盖全集
    unordered_set<int> all_elements;
    for (const auto& subset : subsets) {
        for (int elem : subset) {
            all_elements.insert(elem);
        }
    }
    
    for (int elem : universe) {
        if (all_elements.find(elem) == all_elements.end()) {
            throw invalid_argument("给定的子集无法覆盖全集");
        }
    }
    
    unordered_set<int> uncovered = universe;  // 未被覆盖的元素集合
    vector<int> selected_subsets;             // 选中的子集索引列表
    double total_weight = 0.0;               // 总权值
    
    while (!uncovered.empty()) {
        int best_subset_index = -1;
        double best_value = -1.0;  // 性价比 = 覆盖的新元素数量 / 权值
        
        // 找到性价比最高的子集
        for (size_t i = 0; i < subsets.size(); ++i) {
            // 计算该子集能覆盖的未被覆盖的元素数量
            int covered_new = 0;
            for (int elem : subsets[i]) {
                if (uncovered.find(elem) != uncovered.end()) {
                    covered_new++;
                }
            }
            
            if (covered_new == 0) {
                continue;  // 该子集不能覆盖新元素，跳过
            }
            
            // 计算性价比
            double value = static_cast<double>(covered_new) / actual_weights[i];
            
            if (value > best_value) {
                best_value = value;
                best_subset_index = i;
            }
        }
        
        // 如果没有找到合适的子集，说明无法覆盖全集（理论上不应该发生，因为前面已经检查过）
        if (best_subset_index == -1) {
            throw invalid_argument("无法覆盖全集");
        }
        
        // 选择该子集
        selected_subsets.push_back(best_subset_index);
        total_weight += actual_weights[best_subset_index];
        
        // 更新未被覆盖的元素集合
        for (int elem : subsets[best_subset_index]) {
            uncovered.erase(elem);
        }
    }
    
    return {selected_subsets, total_weight};
}

/**
 * LeetCode 1541. 平衡括号字符串的最少插入次数
 * 题目链接：https://leetcode-cn.com/problems/minimum-insertions-to-balance-a-parentheses-string/
 * 
 * 问题描述：
 * 给你一个括号字符串 s ，请你返回满足以下条件的 最少 插入次数：
 * - 任何左括号 '(' 必须有相应的两个右括号 '))'
 * - 左括号 '(' 必须在对应的连续两个右括号 '))' 之前
 * 
 * 解题思路：
 * 这是一个变种的括号匹配问题，可以使用贪心算法来解决。
 * 我们维护两个变量：需要的右括号数量和需要添加的左括号数量。
 * 遍历字符串，根据当前字符和状态更新这两个变量。
 * 
 * @param s 括号字符串
 * @return 最少需要插入的字符数
 */
int minInsertions(const string& s) {
    int insert_count = 0;  // 需要插入的字符数
    int need_right = 0;    // 需要的右括号数量
    
    for (char c : s) {
        if (c == '(') {
            need_right += 2;  // 每个左括号需要两个右括号
            
            // 如果需要的右括号数量是奇数，说明前一个字符需要补充一个右括号
            if (need_right % 2 == 1) {
                insert_count += 1;  // 插入一个右括号
                need_right -= 1;    // 需要的右括号数量减1
            }
        } else {  // c == ')'
            need_right -= 1;
            
            // 如果右括号过多，需要添加一个左括号
            if (need_right == -1) {
                insert_count += 1;  // 插入一个左括号
                need_right = 1;     // 现在需要一个右括号
            }
        }
    }
    
    return insert_count + need_right;
}

/**
 * LeetCode 1689. 十-二进制数的最少数目
 * 题目链接：https://leetcode-cn.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/
 * 
 * 问题描述：
 * 如果一个十进制数字不含任何前导零，且每一位上的数字不是 0 就是 1，那么该数字就是一个 十-二进制数 。
 * 例如，101 和 1100 都是 十-二进制数，而 112 和 3001 不是。
 * 给你一个表示十进制整数的字符串 n ，返回和为 n 的 十-二进制数 的最少数目。
 * 
 * 解题思路：
 * 这个问题可以转化为：找到字符串中最大的数字。因为对于每一位的数字d，我们需要至少d个十-二进制数，
 * 每个数在该位上提供1。
 * 
 * @param n 表示十进制整数的字符串
 * @return 和为n的十-二进制数的最少数目
 */
int minPartitions(const string& n) {
    int max_digit = 0;
    for (char c : n) {
        max_digit = max(max_digit, c - '0');
    }
    return max_digit;
}

/**
 * LeetCode 45. 跳跃游戏 II
 * 题目链接：https://leetcode-cn.com/problems/jump-game-ii/
 * 
 * 问题描述：
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 你的目标是使用最少的跳跃次数到达数组的最后一个位置。
 * 
 * 解题思路：
 * 使用贪心算法，每次都跳转到能够到达的最远位置。
 * 具体来说，我们维护当前可以到达的最远位置end和下一跳可以到达的最远位置farthest。
 * 当我们到达end时，更新end为farthest并增加跳跃次数。
 * 
 * @param nums 非负整数数组
 * @return 最少跳跃次数
 */
int jump(const vector<int>& nums) {
    int n = nums.size();
    if (n <= 1) {
        return 0;
    }
    
    int jumps = 0;          // 跳跃次数
    int current_end = 0;    // 当前可以到达的最远位置
    int current_farthest = 0;  // 下一跳可以到达的最远位置
    
    for (int i = 0; i < n - 1; ++i) {
        // 更新下一跳可以到达的最远位置
        current_farthest = max(current_farthest, i + nums[i]);
        
        // 如果到达了当前可以到达的边界，必须跳一次
        if (i == current_end) {
            jumps++;
            current_end = current_farthest;
        }
    }
    
    return jumps;
}

// 辅助函数：打印集合
void printSet(const unordered_set<int>& s) {
    cout << "{";
    bool first = true;
    for (int elem : s) {
        if (!first) {
            cout << ", ";
        }
        cout << elem;
        first = false;
    }
    cout << "}";
}

// 测试代码
int main() {
    try {
        // 测试子集覆盖算法
        unordered_set<int> universe = {1, 2, 3, 4, 5};
        vector<unordered_set<int>> subsets = {
            {1, 2, 3},
            {2, 4},
            {3, 4},
            {4, 5}
        };
        vector<double> weights = {5, 10, 3, 8};
        
        auto result = setCover(universe, subsets, weights);
        cout << "选中的子集索引: [";
        for (size_t i = 0; i < result.first.size(); ++i) {
            if (i > 0) {
                cout << ", ";
            }
            cout << result.first[i];
        }
        cout << "]" << endl;
        cout << "总权值: " << result.second << endl;
        
        // 测试LeetCode 1541
        string s1 = "(()))";
        cout << "最少插入次数: " << minInsertions(s1) << endl;  // 应该输出 1
        
        // 测试LeetCode 1689
        string s2 = "32";
        cout << "十-二进制数的最少数目: " << minPartitions(s2) << endl;  // 应该输出 3
        
        // 测试LeetCode 45
        vector<int> nums = {2, 3, 1, 1, 4};
        cout << "最少跳跃次数: " << jump(nums) << endl;  // 应该输出 2
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
    }
    
    return 0;
}