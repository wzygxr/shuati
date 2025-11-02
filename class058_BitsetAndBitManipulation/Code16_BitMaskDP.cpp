#include <iostream>
#include <vector>
#include <bitset>
#include <stdexcept>
#include <chrono>
#include <random>
#include <algorithm>
#include <functional>
#include <map>
#include <unordered_map>
#include <queue>
#include <stack>
#include <string>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <climits>
#include <cassert>
#include <limits>
#include <unordered_set>
#include <cstdint>

using namespace std;

/**
 * 状态压缩动态规划算法实现
 * 使用位掩码技术解决复杂的组合优化问题
 * 
 * 题目来源:
 * 1. LeetCode 464 - 我能赢吗
 * 2. LeetCode 691 - 贴纸拼词
 * 3. LeetCode 943 - 最短超级串
 * 4. LeetCode 1125 - 最小的必要团队
 * 5. 旅行商问题（TSP）
 * 6. 哈密顿路径问题
 * 
 * 时间复杂度分析:
 * - 状态压缩DP: O(2^n * n) 或 O(2^n * n^2)
 * - 空间复杂度: O(2^n) 或 O(2^n * n)
 * 
 * 工程化考量:
 * 1. 状态表示: 使用位掩码紧凑表示状态
 * 2. 状态转移: 优化状态转移方程
 * 3. 内存优化: 使用滚动数组或位运算压缩
 * 4. 边界处理: 处理空状态和终止状态
 */

class BitMaskDP {
public:
    /**
     * LeetCode 464 - 我能赢吗
     * 题目链接: https://leetcode.com/problems/can-i-win/
     * 两个玩家轮流选择1到maxChoosableInteger的数字，先达到或超过desiredTotal的玩家获胜
     * 
     * 方法: 记忆化搜索 + 状态压缩
     * 时间复杂度: O(2^maxChoosableInteger * maxChoosableInteger)
     * 空间复杂度: O(2^maxChoosableInteger)
     */
    static bool canIWin(int maxChoosableInteger, int desiredTotal) {
        if (maxChoosableInteger >= desiredTotal) return true;
        if (maxChoosableInteger * (maxChoosableInteger + 1) / 2 < desiredTotal) return false;
        
        vector<int> memo(1 << maxChoosableInteger, -1);  // -1: 未计算, 0: 输, 1: 赢
        
        function<int(int, int)> dfs = [&](int state, int currentTotal) -> int {
            if (memo[state] != -1) return memo[state];
            
            for (int i = 0; i < maxChoosableInteger; i++) {
                if (state & (1 << i)) continue;  // 数字i+1已经被选过
                
                int newState = state | (1 << i);
                int newTotal = currentTotal + i + 1;
                
                // 如果当前选择能直接获胜，或者对手会输
                if (newTotal >= desiredTotal || dfs(newState, newTotal) == 0) {
                    return memo[state] = 1;
                }
            }
            
            return memo[state] = 0;
        };
        
        return dfs(0, 0) == 1;
    }
    
    /**
     * LeetCode 691 - 贴纸拼词
     * 题目链接: https://leetcode.com/problems/stickers-to-spell-word/
     * 用给定的贴纸拼出目标单词，求最少需要的贴纸数量
     * 
     * 方法: 状态压缩BFS
     * 时间复杂度: O(2^len(target) * n * len(stickers))
     * 空间复杂度: O(2^len(target))
     */
    static int minStickers(vector<string>& stickers, string target) {
        int n = target.size();
        int totalStates = 1 << n;
        vector<int> dp(totalStates, INT_MAX);
        dp[0] = 0;  // 空状态需要0张贴纸
        
        for (int state = 0; state < totalStates; state++) {
            if (dp[state] == INT_MAX) continue;
            
            for (const string& sticker : stickers) {
                int newState = state;
                
                // 尝试用当前贴纸覆盖目标字符
                vector<int> count(26, 0);
                for (char c : sticker) count[c - 'a']++;
                
                for (int i = 0; i < n; i++) {
                    if (state & (1 << i)) continue;  // 该位置已经被覆盖
                    if (count[target[i] - 'a'] > 0) {
                        count[target[i] - 'a']--;
                        newState |= (1 << i);
                    }
                }
                
                if (newState != state) {
                    dp[newState] = min(dp[newState], dp[state] + 1);
                }
            }
        }
        
        return dp[totalStates - 1] == INT_MAX ? -1 : dp[totalStates - 1];
    }
    
    /**
     * LeetCode 943 - 最短超级串
     * 题目链接: https://leetcode.com/problems/find-the-shortest-superstring/
     * 找到包含所有字符串的最短超级字符串
     * 
     * 方法: 状态压缩DP + 重叠计算
     * 时间复杂度: O(2^n * n^2)
     * 空间复杂度: O(2^n * n)
     */
    static string shortestSuperstring(vector<string>& words) {
        int n = words.size();
        
        // 计算重叠度
        vector<vector<int>> overlap(n, vector<int>(n, 0));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                int len = min(words[i].size(), words[j].size());
                for (int k = len; k >= 0; k--) {
                    if (words[i].substr(words[i].size() - k) == words[j].substr(0, k)) {
                        overlap[i][j] = k;
                        break;
                    }
                }
            }
        }
        
        // DP状态: dp[mask][last] 表示使用mask集合，以last结尾的最短长度
        vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
        vector<vector<int>> parent(1 << n, vector<int>(n, -1));
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = words[i].size();
        }
        
        // 动态规划
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (!(mask & (1 << last)) || dp[mask][last] == INT_MAX) continue;
                
                for (int next = 0; next < n; next++) {
                    if (mask & (1 << next)) continue;
                    
                    int newMask = mask | (1 << next);
                    int newLen = dp[mask][last] + words[next].size() - overlap[last][next];
                    
                    if (newLen < dp[newMask][next]) {
                        dp[newMask][next] = newLen;
                        parent[newMask][next] = last;
                    }
                }
            }
        }
        
        // 重建结果
        int finalMask = (1 << n) - 1;
        int last = min_element(dp[finalMask].begin(), dp[finalMask].end()) - dp[finalMask].begin();
        int minLen = dp[finalMask][last];
        
        // 回溯构建字符串
        string result;
        int mask = finalMask;
        vector<int> path;
        
        while (mask > 0) {
            path.push_back(last);
            int prev = parent[mask][last];
            if (prev == -1) break;
            mask ^= (1 << last);
            last = prev;
        }
        
        reverse(path.begin(), path.end());
        
        result = words[path[0]];
        for (int i = 1; i < path.size(); i++) {
            int overlapLen = overlap[path[i-1]][path[i]];
            result += words[path[i]].substr(overlapLen);
        }
        
        return result;
    }
    
    /**
     * LeetCode 1125 - 最小的必要团队
     * 题目链接: https://leetcode.com/problems/smallest-sufficient-team/
     * 找到覆盖所有技能的最小团队
     * 
     * 方法: 状态压缩DP
     * 时间复杂度: O(2^m * n) - m为技能数量，n为人员数量
     * 空间复杂度: O(2^m)
     */
    static vector<int> smallestSufficientTeam(vector<string>& req_skills, vector<vector<string>>& people) {
        int m = req_skills.size();
        int n = people.size();
        
        // 技能到索引的映射
        unordered_map<string, int> skillToIndex;
        for (int i = 0; i < m; i++) {
            skillToIndex[req_skills[i]] = i;
        }
        
        // 将每个人的技能转换为位掩码
        vector<int> peopleSkills(n, 0);
        for (int i = 0; i < n; i++) {
            for (const string& skill : people[i]) {
                if (skillToIndex.count(skill)) {
                    peopleSkills[i] |= (1 << skillToIndex[skill]);
                }
            }
        }
        
        int totalStates = 1 << m;
        vector<int> dp(totalStates, INT_MAX);
        vector<int> parentState(totalStates, -1);
        vector<int> parentPerson(totalStates, -1);
        dp[0] = 0;
        
        for (int state = 0; state < totalStates; state++) {
            if (dp[state] == INT_MAX) continue;
            
            for (int i = 0; i < n; i++) {
                int newState = state | peopleSkills[i];
                if (dp[state] + 1 < dp[newState]) {
                    dp[newState] = dp[state] + 1;
                    parentState[newState] = state;
                    parentPerson[newState] = i;
                }
            }
        }
        
        // 重建团队
        vector<int> team;
        int state = totalStates - 1;
        
        while (state > 0) {
            team.push_back(parentPerson[state]);
            state = parentState[state];
        }
        
        return team;
    }
    
    /**
     * 哈密顿路径问题
     * 在图中找到访问所有顶点恰好一次的路径
     * 
     * 方法: 状态压缩DP
     * 时间复杂度: O(2^n * n^2)
     * 空间复杂度: O(2^n * n)
     */
    static vector<int> hamiltonianPath(vector<vector<int>>& graph) {
        int n = graph.size();
        if (n == 0) return {};
        
        // dp[mask][last] 表示访问mask集合，以last结尾的路径是否存在
        vector<vector<bool>> dp(1 << n, vector<bool>(n, false));
        vector<vector<int>> parent(1 << n, vector<int>(n, -1));
        
        // 初始化：每个顶点单独作为路径起点
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = true;
        }
        
        // 动态规划
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (!dp[mask][last]) continue;
                
                for (int next = 0; next < n; next++) {
                    if (mask & (1 << next)) continue;  // 已经访问过
                    if (!graph[last][next]) continue;  // 不可达
                    
                    int newMask = mask | (1 << next);
                    dp[newMask][next] = true;
                    parent[newMask][next] = last;
                }
            }
        }
        
        // 检查是否存在哈密顿路径
        int finalMask = (1 << n) - 1;
        for (int last = 0; last < n; last++) {
            if (dp[finalMask][last]) {
                // 重建路径
                vector<int> path;
                int mask = finalMask;
                int current = last;
                
                while (mask > 0) {
                    path.push_back(current);
                    int prev = parent[mask][current];
                    if (prev == -1) break;
                    mask ^= (1 << current);
                    current = prev;
                }
                
                reverse(path.begin(), path.end());
                return path;
            }
        }
        
        return {};  // 不存在哈密顿路径
    }
    
    /**
     * 子集和问题（状态压缩版本）
     * 判断是否存在子集使得元素和等于目标值
     * 
     * 方法: 位运算枚举
     * 时间复杂度: O(2^n)
     * 空间复杂度: O(2^n)
     */
    static bool subsetSum(vector<int>& nums, int target) {
        int n = nums.size();
        int total = 1 << n;
        
        vector<bool> dp(total, false);
        dp[0] = true;
        
        for (int mask = 0; mask < total; mask++) {
            if (!dp[mask]) continue;
            
            int currentSum = 0;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    currentSum += nums[i];
                }
            }
            
            if (currentSum == target) return true;
            
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) continue;
                int newMask = mask | (1 << i);
                dp[newMask] = true;
            }
        }
        
        return false;
    }
};

class PerformanceTester {
public:
    static void testCanIWin() {
        cout << "=== 我能赢吗性能测试 ===" << endl;
        
        vector<pair<int, int>> testCases = {
            {10, 11},  // 简单情况
            {10, 40},  // 复杂情况
            {5, 15}    // 边界情况
        };
        
        for (auto& testCase : testCases) {
            int maxInt = testCase.first;
            int target = testCase.second;
            
            auto start = chrono::high_resolution_clock::now();
            bool result = BitMaskDP::canIWin(maxInt, target);
            auto time = chrono::duration_cast<chrono::microseconds>(
                chrono::high_resolution_clock::now() - start).count();
            
            cout << "maxChoosableInteger=" << maxInt << ", desiredTotal=" << target;
            cout << " -> 结果: " << (result ? "能赢" : "不能赢");
            cout << ", 耗时: " << time << " μs" << endl;
        }
    }
    
    static void testMinStickers() {
        cout << "\n=== 贴纸拼词性能测试 ===" << endl;
        
        vector<string> stickers = {"with", "example", "science"};
        string target = "thehat";
        
        auto start = chrono::high_resolution_clock::now();
        int result = BitMaskDP::minStickers(stickers, target);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "贴纸: ";
        for (const string& s : stickers) cout << s << " ";
        cout << endl;
        cout << "目标: " << target << endl;
        cout << "最少需要贴纸: " << result << endl;
        cout << "耗时: " << time << " μs" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 状态压缩DP单元测试 ===" << endl;
        
        // 测试我能赢吗
        assert(BitMaskDP::canIWin(10, 11) == true);
        cout << "canIWin测试通过" << endl;
        
        // 测试子集和
        vector<int> nums = {3, 34, 4, 12, 5, 2};
        assert(BitMaskDP::subsetSum(nums, 9) == true);
        assert(BitMaskDP::subsetSum(nums, 30) == false);
        cout << "subsetSum测试通过" << endl;
        
        cout << "所有单元测试通过!" << endl;
    }
};

int main() {
    cout << "状态压缩动态规划算法实现" << endl;
    cout << "使用位掩码技术解决复杂的组合优化问题" << endl;
    cout << "==========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testCanIWin();
    PerformanceTester::testMinStickers();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 我能赢吗示例
    cout << "我能赢吗示例:" << endl;
    cout << "maxChoosableInteger=10, desiredTotal=11 -> ";
    cout << (BitMaskDP::canIWin(10, 11) ? "能赢" : "不能赢") << endl;
    
    // 子集和问题示例
    vector<int> nums = {3, 34, 4, 12, 5, 2};
    int target = 9;
    cout << "子集和问题示例:" << endl;
    cout << "数组: ";
    for (int num : nums) cout << num << " ";
    cout << ", 目标: " << target << " -> ";
    cout << (BitMaskDP::subsetSum(nums, target) ? "存在" : "不存在") << endl;
    
    return 0;
}