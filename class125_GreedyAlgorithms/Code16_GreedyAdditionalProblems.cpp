#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <deque>
#include <functional>
#include <cmath>

using namespace std;

/**
 * 贪心算法补充题目集合 - C++版本
 * 收集来自各大算法平台的经典贪心算法题目
 * 每个题目都提供详细的注释和复杂度分析
 */

/**
 * 题目1: LeetCode 134. 加油站
 * 算法思路：贪心策略，维护当前剩余油量和总剩余油量
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
    if (gas.size() != cost.size()) return -1;
    
    int totalGas = 0;
    int currentGas = 0;
    int startStation = 0;
    
    for (int i = 0; i < gas.size(); i++) {
        totalGas += gas[i] - cost[i];
        currentGas += gas[i] - cost[i];
        
        if (currentGas < 0) {
            startStation = i + 1;
            currentGas = 0;
        }
    }
    
    return totalGas >= 0 ? startStation : -1;
}

/**
 * 题目2: LeetCode 402. 移掉K位数字
 * 算法思路：使用单调栈保持数字序列递增
 * 时间复杂度: O(n)，空间复杂度: O(n)
 */
string removeKdigits(string num, int k) {
    if (num.length() <= k) return "0";
    
    deque<char> stack;
    
    for (char digit : num) {
        while (!stack.empty() && k > 0 && digit < stack.back()) {
            stack.pop_back();
            k--;
        }
        stack.push_back(digit);
    }
    
    // 处理剩余的删除次数
    while (k > 0) {
        stack.pop_back();
        k--;
    }
    
    // 构建结果字符串
    string result;
    while (!stack.empty()) {
        result.push_back(stack.front());
        stack.pop_front();
    }
    
    // 去除前导零
    int start = 0;
    while (start < result.length() && result[start] == '0') {
        start++;
    }
    
    return start == result.length() ? "0" : result.substr(start);
}

/**
 * 题目3: LeetCode 561. 数组拆分 I
 * 算法思路：排序后取每对的第一个元素
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int arrayPairSum(vector<int>& nums) {
    if (nums.size() % 2 != 0) return 0;
    
    sort(nums.begin(), nums.end());
    int sum = 0;
    
    for (int i = 0; i < nums.size(); i += 2) {
        sum += nums[i];
    }
    
    return sum;
}

/**
 * 题目4: LeetCode 665. 非递减数列
 * 算法思路：遇到逆序对时优先修改前面的数字
 * 时间复杂度: O(n)，空间复杂度: O(1)
 */
bool checkPossibility(vector<int>& nums) {
    if (nums.size() <= 1) return true;
    
    int count = 0;
    for (int i = 0; i < nums.size() - 1; i++) {
        if (nums[i] > nums[i + 1]) {
            count++;
            if (count > 1) return false;
            
            if (i > 0 && nums[i - 1] > nums[i + 1]) {
                nums[i + 1] = nums[i];
            } else {
                nums[i] = nums[i + 1];
            }
        }
    }
    
    return true;
}

/**
 * 题目5: HackerRank - Mark and Toys
 * 算法思路：排序价格后贪心购买
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int maximumToys(vector<int>& prices, int budget) {
    if (prices.empty() || budget <= 0) return 0;
    
    sort(prices.begin(), prices.end());
    int count = 0;
    int currentCost = 0;
    
    for (int price : prices) {
        if (currentCost + price <= budget) {
            currentCost += price;
            count++;
        } else {
            break;
        }
    }
    
    return count;
}

/**
 * 题目6: HackerRank - Luck Balance
 * 算法思路：输掉不重要比赛，输掉k场重要比赛
 * 时间复杂度: O(n log n)，空间复杂度: O(n)
 */
int luckBalance(int k, vector<vector<int>>& contests) {
    if (contests.empty()) return 0;
    
    vector<int> important;
    int totalLuck = 0;
    
    for (auto& contest : contests) {
        int luck = contest[0];
        int importance = contest[1];
        
        if (importance == 1) {
            important.push_back(luck);
        } else {
            totalLuck += luck;
        }
    }
    
    // 重要比赛按运气值降序排序
    sort(important.begin(), important.end(), greater<int>());
    
    // 输掉前k场重要比赛
    for (int i = 0; i < important.size(); i++) {
        if (i < k) {
            totalLuck += important[i];
        } else {
            totalLuck -= important[i];
        }
    }
    
    return totalLuck;
}

/**
 * 题目7: 牛客网 - 疯狂的采药（分数背包贪心解法）
 * 算法思路：按性价比排序后贪心选择
 * 时间复杂度: O(n log n)，空间复杂度: O(n)
 */
int crazyHerbs(int time, vector<vector<int>>& herbs) {
    if (herbs.empty() || time <= 0) return 0;
    
    // 计算性价比并排序
    vector<tuple<double, int, int>> herbList; // ratio, value, cost
    for (auto& herb : herbs) {
        int value = herb[0];
        int cost = herb[1];
        double ratio = static_cast<double>(value) / cost;
        herbList.push_back({ratio, value, cost});
    }
    
    sort(herbList.begin(), herbList.end(), [](const auto& a, const auto& b) {
        return get<0>(a) > get<0>(b);
    });
    
    int totalValue = 0;
    int remainingTime = time;
    
    for (auto& herb : herbList) {
        int value = get<1>(herb);
        int cost = get<2>(herb);
        
        if (remainingTime >= cost) {
            totalValue += value;
            remainingTime -= cost;
        } else {
            totalValue += static_cast<int>(value * (static_cast<double>(remainingTime) / cost));
            break;
        }
    }
    
    return totalValue;
}

/**
 * 题目8: Codeforces 1360B - Honest Coach
 * 算法思路：排序后找最小相邻差值
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int honestCoach(vector<int>& skills) {
    if (skills.size() <= 1) return 0;
    
    sort(skills.begin(), skills.end());
    int minDiff = INT_MAX;
    
    for (int i = 1; i < skills.size(); i++) {
        minDiff = min(minDiff, skills[i] - skills[i - 1]);
    }
    
    return minDiff;
}

/**
 * 题目9: 洛谷 P1803 - 线段覆盖
 * 算法思路：经典活动选择问题
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int maxNonOverlappingIntervals(vector<vector<int>>& intervals) {
    if (intervals.empty()) return 0;
    
    // 按结束时间排序
    sort(intervals.begin(), intervals.end(), [](const vector<int>& a, const vector<int>& b) {
        return a[1] < b[1];
    });
    
    int count = 1;
    int end = intervals[0][1];
    
    for (int i = 1; i < intervals.size(); i++) {
        if (intervals[i][0] >= end) {
            count++;
            end = intervals[i][1];
        }
    }
    
    return count;
}

/**
 * 题目10: LeetCode 1005. K 次取反后最大化的数组和
 * 算法思路：优先处理负数，然后处理最小正数
 * 时间复杂度: O(n log n)，空间复杂度: O(1)
 */
int largestSumAfterKNegations(vector<int>& nums, int k) {
    if (nums.empty()) return 0;
    
    // 排序处理负数
    sort(nums.begin(), nums.end());
    
    for (int i = 0; i < nums.size() && k > 0; i++) {
        if (nums[i] < 0) {
            nums[i] = -nums[i];
            k--;
        } else {
            break;
        }
    }
    
    // 处理剩余的k
    if (k > 0 && k % 2 == 1) {
        sort(nums.begin(), nums.end());
        nums[0] = -nums[0];
    }
    
    int sum = 0;
    for (int num : nums) {
        sum += num;
    }
    
    return sum;
}

// 测试函数
int main() {
    // 测试加油站问题
    vector<int> gas = {1, 2, 3, 4, 5};
    vector<int> cost = {3, 4, 5, 1, 2};
    cout << "加油站测试: " << canCompleteCircuit(gas, cost) << endl; // 期望: 3
    
    // 测试移掉K位数字
    cout << "移掉K位数字测试: " << removeKdigits("1432219", 3) << endl; // 期望: "1219"
    
    // 测试数组拆分
    vector<int> nums = {1, 4, 3, 2};
    cout << "数组拆分测试: " << arrayPairSum(nums) << endl; // 期望: 4
    
    return 0;
}