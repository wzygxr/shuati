#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <random>

using namespace std;

// POJ 2184 Cow Exhibition
// 题目描述：奶牛们想证明它们是聪明而风趣的。为此，贝西筹备了一个奶牛博览会，
// 她已经对N头奶牛进行了面试，确定了每头奶牛的聪明度和幽默度。
// 贝西可以选择任意数量的奶牛参加展览，但希望总聪明度和总幽默度都非负。
// 在满足条件的情况下，使得总聪明度与总幽默度的和最大。
// 链接：http://poj.org/problem?id=2184
// 
// 解题思路：
// 这是一个二维费用的01背包问题，需要同时考虑两个维度（聪明度和幽默度）。
// 由于两个维度都可能为负数，需要进行坐标平移。
// 
// 状态定义：dp[i][j] 表示前i头奶牛，聪明度总和为j时的最大幽默度总和
// 状态转移方程：
//   dp[i][j] = max(dp[i-1][j], dp[i-1][j-smart[i]] + funny[i])
// 
// 关键点：
// 1. 坐标平移：由于聪明度可能为负数，需要将坐标平移到非负数范围
// 2. 二维费用：同时考虑聪明度和幽默度两个维度
// 3. 状态优化：使用滚动数组优化空间复杂度
// 
// 时间复杂度：O(N * range)，其中range是聪明度的可能范围
// 空间复杂度：O(range)，使用滚动数组优化
// 
// 工程化考量：
// 1. 异常处理：处理空输入、边界值等情况
// 2. 性能优化：坐标平移和滚动数组优化
// 3. 边界条件：处理负数范围和结果有效性检查

class CowExhibition {
private:
    static const int OFFSET = 100000;  // 坐标偏移量，处理负数
    static const int MAX_RANGE = 200000; // 总范围大小
    static const int INF = INT_MIN / 2; // 表示不可达状态
    
public:
    /**
     * 动态规划解法 - 二维费用01背包
     * @param cows 奶牛数组，每个奶牛包含聪明度和幽默度
     * @return 最大总聪明度与总幽默度的和
     */
    static int maxCowExhibition(vector<vector<int>>& cows) {
        // 参数验证
        if (cows.empty()) {
            return 0;
        }
        
        int n = cows.size();
        
        // 创建DP数组，使用滚动数组优化
        vector<int> dp(MAX_RANGE + 1, INF);
        dp[OFFSET] = 0; // 初始状态：聪明度总和为0，幽默度总和为0
        
        // 遍历每头奶牛
        for (int i = 0; i < n; i++) {
            int smart = cows[i][0];
            int funny = cows[i][1];
            
            // 根据smart的正负决定遍历方向
            if (smart >= 0) {
                // 正数：倒序遍历，避免重复选择
                for (int j = MAX_RANGE; j >= smart; j--) {
                    if (dp[j - smart] != INF) {
                        dp[j] = max(dp[j], dp[j - smart] + funny);
                    }
                }
            } else {
                // 负数：正序遍历
                for (int j = 0; j <= MAX_RANGE + smart; j++) {
                    if (dp[j - smart] != INF) {
                        dp[j] = max(dp[j], dp[j - smart] + funny);
                    }
                }
            }
        }
        
        // 寻找最大和（聪明度+幽默度）
        int maxSum = 0;
        for (int j = OFFSET; j <= MAX_RANGE; j++) {
            if (dp[j] >= 0) { // 幽默度总和需要非负
                maxSum = max(maxSum, j - OFFSET + dp[j]);
            }
        }
        
        return maxSum;
    }
    
    /**
     * 优化的动态规划解法 - 使用二维数组便于理解
     */
    static int maxCowExhibition2D(vector<vector<int>>& cows) {
        if (cows.empty()) {
            return 0;
        }
        
        int n = cows.size();
        
        // 计算聪明度的可能范围
        int minSmart = 0, maxSmart = 0;
        for (auto& cow : cows) {
            if (cow[0] < 0) minSmart += cow[0];
            else maxSmart += cow[0];
        }
        
        int range = maxSmart - minSmart;
        int offset = -minSmart;
        
        // 创建二维DP数组
        vector<vector<int>> dp(n + 1, vector<int>(range + 1, INF));
        dp[0][offset] = 0;
        
        // 动态规划
        for (int i = 1; i <= n; i++) {
            int smart = cows[i - 1][0];
            int funny = cows[i - 1][1];
            
            for (int j = 0; j <= range; j++) {
                // 不选当前奶牛
                dp[i][j] = dp[i - 1][j];
                
                // 选当前奶牛
                int prev = j - smart;
                if (prev >= 0 && prev <= range && dp[i - 1][prev] != INF) {
                    dp[i][j] = max(dp[i][j], dp[i - 1][prev] + funny);
                }
            }
        }
        
        // 寻找最大和
        int maxSum = 0;
        for (int j = offset; j <= range; j++) {
            if (dp[n][j] >= 0) {
                maxSum = max(maxSum, j - offset + dp[n][j]);
            }
        }
        
        return maxSum;
    }
    
    /**
     * 空间优化的解法 - 只记录有效状态
     */
    static int maxCowExhibitionOptimized(vector<vector<int>>& cows) {
        if (cows.empty()) {
            return 0;
        }
        
        // 分离正负聪明度的奶牛
        vector<vector<int>> positiveCows;
        vector<vector<int>> negativeCows;
        
        for (auto& cow : cows) {
            if (cow[0] >= 0) {
                positiveCows.push_back(cow);
            } else {
                negativeCows.push_back(cow);
            }
        }
        
        // 处理正数聪明度的奶牛
        vector<int> dp(MAX_RANGE + 1, INF);
        dp[OFFSET] = 0;
        
        for (auto& cow : positiveCows) {
            int smart = cow[0];
            int funny = cow[1];
            for (int j = MAX_RANGE; j >= smart; j--) {
                if (dp[j - smart] != INF) {
                    dp[j] = max(dp[j], dp[j - smart] + funny);
                }
            }
        }
        
        // 处理负数聪明度的奶牛
        for (auto& cow : negativeCows) {
            int smart = cow[0];
            int funny = cow[1];
            for (int j = 0; j <= MAX_RANGE + smart; j++) {
                if (dp[j - smart] != INF) {
                    dp[j] = max(dp[j], dp[j - smart] + funny);
                }
            }
        }
        
        // 寻找最大和
        int maxSum = 0;
        for (int j = OFFSET; j <= MAX_RANGE; j++) {
            if (dp[j] >= 0) {
                maxSum = max(maxSum, j - OFFSET + dp[j]);
            }
        }
        
        return maxSum;
    }
};

/**
 * 测试函数
 */
void testCowExhibition() {
    // 测试用例
    vector<vector<vector<int>>> testCases = {
        // 示例测试用例
        {
            {5, 1},
            {1, 5},
            {-5, 5},
            {5, -1}
        },
        // 边界测试用例
        {
            {10, 20},
            {15, 15}
        },
        // 包含负数的测试用例
        {
            {-1, 100},
            {2, 50},
            {-3, 200}
        },
        // 空测试用例
        {}
    };
    
    cout << "奶牛展览问题测试：" << endl;
    for (size_t i = 0; i < testCases.size(); i++) {
        auto& cows = testCases[i];
        
        int result1 = CowExhibition::maxCowExhibition(cows);
        int result2 = CowExhibition::maxCowExhibition2D(cows);
        int result3 = CowExhibition::maxCowExhibitionOptimized(cows);
        
        cout << "测试用例" << i + 1 << ": 奶牛数量=" << cows.size() 
             << ", 方法1=" << result1 << ", 方法2=" << result2 
             << ", 方法3=" << result3 << endl;
        
        // 验证结果一致性
        if (result1 != result2 || result2 != result3) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    int n = 100;
    vector<vector<int>> largeCows;
    // 生成随机测试数据
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> smartDist(-100, 100);
    uniform_int_distribution<> funnyDist(0, 100);
    
    for (int i = 0; i < n; i++) {
        largeCows.push_back({smartDist(gen), funnyDist(gen)});
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = CowExhibition::maxCowExhibitionOptimized(largeCows);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: 奶牛数量=" << n << ", 结果=" << largeResult 
         << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<vector<int>> emptyCows;
    cout << "空数组: " << CowExhibition::maxCowExhibition(emptyCows) << endl;
    
    vector<vector<int>> singleCow = {{10, 20}};
    cout << "单头奶牛: " << CowExhibition::maxCowExhibition(singleCow) << endl;
    
    vector<vector<int>> negativeCows = {{-1, 5}, {-2, 10}};
    cout << "全负数聪明度: " << CowExhibition::maxCowExhibition(negativeCows) << endl;
}

int main() {
    try {
        testCowExhibition();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（滚动数组）
 * - 时间复杂度：O(N * range)
 *   - N: 奶牛数量
 *   - range: 聪明度的可能范围（经过坐标平移）
 * - 空间复杂度：O(range)
 * 
 * 方法2：二维动态规划
 * - 时间复杂度：O(N * range)
 * - 空间复杂度：O(N * range)
 * 
 * 方法3：优化的动态规划
 * - 时间复杂度：O(N * range)（但常数更小）
 * - 空间复杂度：O(range)
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用STL算法进行最大值计算
 * 3. 使用随机数生成器进行性能测试
 * 4. 使用chrono进行精确性能测量
 * 
 * 关键点分析：
 * 1. 坐标平移：处理负数聪明度，将坐标平移到非负数范围
 * 2. 遍历方向：根据smart的正负决定遍历方向（01背包特性）
 * 3. 状态有效性：只考虑幽默度非负的状态
 * 4. 结果计算：聪明度+幽默度的最大和
 * 
 * 工程化考量：
 * 1. 模块化设计：将不同解法封装为静态方法
 * 2. 异常处理：使用try-catch处理异常
 * 3. 性能优化：利用STL容器和算法
 * 4. 测试覆盖：包含各种边界情况和性能测试
 * 
 * 面试要点：
 * 1. 理解二维费用背包问题的特点
 * 2. 掌握坐标平移处理负数的方法
 * 3. 了解不同遍历方向的原因
 * 4. 能够分析算法的时空复杂度
 */