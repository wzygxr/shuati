#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <random>
#include <chrono>

using namespace std;

// AtCoder DP Contest E - Knapsack 2
// 题目描述：经典的01背包问题，但是背包容量非常大（10^9），而物品价值比较小（10^3）。
// 链接：https://atcoder.jp/contests/dp/tasks/dp_e
// 
// 解题思路：
// 当背包容量非常大时，传统的DP方法会超时或超内存。
// 需要转换思路：将价值作为背包容量，求达到某个价值所需的最小重量。
// 
// 状态定义：dp[i] 表示达到价值i所需的最小重量
// 状态转移方程：dp[i] = min(dp[i], dp[i-value[j]] + weight[j])
// 初始状态：dp[0] = 0，其他为无穷大
// 
// 时间复杂度：O(N * totalValue)，其中N是物品数量，totalValue是总价值
// 空间复杂度：O(totalValue)
// 
// 工程化考量：
// 1. 问题转化：从重量维度转为价值维度
// 2. 边界处理：处理无穷大值和结果提取
// 3. 性能优化：计算总价值作为新背包容量
// 4. 异常处理：处理空输入和边界值

class Knapsack2 {
private:
    static const long long INF = LLONG_MAX / 2; // 表示不可达状态
    
public:
    /**
     * 动态规划解法 - 价值维度DP
     * @param W 背包容量
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @return 能装入背包的最大价值
     */
    static long long knapsack2(long long W, vector<int>& weights, vector<int>& values) {
        // 参数验证
        if (weights.size() != values.size()) {
            throw invalid_argument("Weights and values arrays must have same size");
        }
        if (W < 0) {
            throw invalid_argument("Capacity W must be non-negative");
        }
        
        int n = weights.size();
        if (n == 0) {
            return 0;
        }
        
        // 计算总价值
        int totalValue = 0;
        for (int value : values) {
            totalValue += value;
        }
        
        // 创建DP数组，dp[i]表示达到价值i所需的最小重量
        vector<long long> dp(totalValue + 1, INF);
        dp[0] = 0; // 价值为0时重量为0
        
        // 遍历每个物品
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            // 倒序遍历价值，避免重复选择
            for (int j = totalValue; j >= value; j--) {
                if (dp[j - value] != INF) {
                    dp[j] = min(dp[j], dp[j - value] + weight);
                }
            }
        }
        
        // 寻找最大的价值，使得所需重量 <= W
        long long maxValue = 0;
        for (int j = totalValue; j >= 0; j--) {
            if (dp[j] <= W) {
                maxValue = j;
                break;
            }
        }
        
        return maxValue;
    }
    
    /**
     * 优化的动态规划解法 - 提前终止
     */
    static long long knapsack2Optimized(long long W, vector<int>& weights, vector<int>& values) {
        if (weights.size() != values.size()) {
            throw invalid_argument("Weights and values arrays must have same size");
        }
        if (W < 0) {
            throw invalid_argument("Capacity W must be non-negative");
        }
        
        int n = weights.size();
        if (n == 0) {
            return 0;
        }
        
        // 计算总价值
        int totalValue = 0;
        for (int value : values) {
            totalValue += value;
        }
        
        vector<long long> dp(totalValue + 1, INF);
        dp[0] = 0;
        
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            for (int j = totalValue; j >= value; j--) {
                if (dp[j - value] != INF) {
                    long long newWeight = dp[j - value] + weight;
                    if (newWeight < dp[j]) {
                        dp[j] = newWeight;
                    }
                }
            }
        }
        
        // 从大到小遍历，找到第一个满足条件的价值
        for (int j = totalValue; j >= 0; j--) {
            if (dp[j] <= W) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 传统DP解法（用于对比）- 仅适用于小容量
     */
    static long long knapsackTraditional(long long W, vector<int>& weights, vector<int>& values) {
        if (weights.size() != values.size()) {
            throw invalid_argument("Weights and values arrays must have same size");
        }
        if (W < 0) {
            throw invalid_argument("Capacity W must be non-negative");
        }
        
        int n = weights.size();
        if (n == 0) {
            return 0;
        }
        
        // 传统DP：dp[i]表示容量为i时的最大价值
        int maxW = min(W, (long long)INT_MAX);
        vector<long long> dp(maxW + 1, 0);
        
        for (int i = 0; i < n; i++) {
            int weight = weights[i];
            int value = values[i];
            
            for (int j = maxW; j >= weight; j--) {
                dp[j] = max(dp[j], dp[j - weight] + value);
            }
        }
        
        return dp[maxW];
    }
};

/**
 * 测试函数
 */
void testKnapsack2() {
    // 测试用例
    vector<long long> W = {100, 1000, 1000000000LL};
    vector<vector<int>> weightsCases = {
        {10, 20, 30},
        {50, 100, 150},
        {1, 2, 3}
    };
    vector<vector<int>> valuesCases = {
        {60, 100, 120},
        {60, 100, 120},
        {10, 15, 40}
    };
    
    cout << "大容量背包问题测试：" << endl;
    for (size_t i = 0; i < W.size(); i++) {
        long long w = W[i];
        vector<int> weights = weightsCases[i];
        vector<int> values = valuesCases[i];
        
        long long result1 = Knapsack2::knapsack2(w, weights, values);
        long long result2 = Knapsack2::knapsack2Optimized(w, weights, values);
        
        // 对于小容量，可以用传统方法验证
        long long traditionalResult = 0;
        if (w <= 10000) {
            traditionalResult = Knapsack2::knapsackTraditional(w, weights, values);
        }
        
        cout << "W=" << w << ", weights=[";
        for (size_t j = 0; j < weights.size(); j++) {
            cout << weights[j];
            if (j < weights.size() - 1) cout << ", ";
        }
        cout << "], values=[";
        for (size_t j = 0; j < values.size(); j++) {
            cout << values[j];
            if (j < values.size() - 1) cout << ", ";
        }
        cout << "]: 方法1=" << result1 << ", 方法2=" << result2;
        
        if (w <= 10000) {
            cout << ", 传统方法=" << traditionalResult;
            // 验证结果一致性
            if (result1 != traditionalResult || result2 != traditionalResult) {
                cout << " - 警告：结果不一致！" << endl;
            } else {
                cout << " - 验证通过" << endl;
            }
        } else {
            cout << " - 大容量测试" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    int n = 100;
    vector<int> largeWeights(n);
    vector<int> largeValues(n);
    
    // 生成随机测试数据
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> weightDist(1, 1000);
    uniform_int_distribution<> valueDist(1, 100);
    
    for (int i = 0; i < n; i++) {
        largeWeights[i] = weightDist(gen);
        largeValues[i] = valueDist(gen);
    }
    
    long long largeW = 1000000000LL; // 10^9
    
    auto startTime = chrono::high_resolution_clock::now();
    long long largeResult = Knapsack2::knapsack2Optimized(largeW, largeWeights, largeValues);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: 物品数量=" << n << ", 容量=" << largeW 
         << ", 结果=" << largeResult << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<int> emptyWeights;
    vector<int> emptyValues;
    cout << "空数组: " << Knapsack2::knapsack2(100, emptyWeights, emptyValues) << endl;
    
    vector<int> singleWeight = {10};
    vector<int> singleValue = {5};
    cout << "W=0: " << Knapsack2::knapsack2(0, singleWeight, singleValue) << endl;
    
    vector<int> heavyWeights = {10, 20};
    vector<int> heavyValues = {5, 10};
    cout << "所有物品超重: " << Knapsack2::knapsack2(5, heavyWeights, heavyValues) << endl;
    
    // 特殊测试：价值为0的物品
    cout << "价值为0的物品测试：" << endl;
    vector<int> zeroValueWeights = {10, 20};
    vector<int> zeroValueValues = {0, 0};
    long long specialResult = Knapsack2::knapsack2(100, zeroValueWeights, zeroValueValues);
    cout << "特殊测试结果: " << specialResult << endl;
}

int main() {
    try {
        testKnapsack2();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：价值维度DP
 * - 时间复杂度：O(N * totalValue)
 *   - N: 物品数量
 *   - totalValue: 总价值
 * - 空间复杂度：O(totalValue)
 * 
 * 方法2：优化的价值维度DP
 * - 时间复杂度：O(N * totalValue)（与方法1相同）
 * - 空间复杂度：O(totalValue)
 * 
 * 方法3：传统重量维度DP
 * - 时间复杂度：O(N * W)
 * - 空间复杂度：O(W)
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用STL算法进行最小值和最大值计算
 * 3. 使用随机数生成器进行性能测试
 * 4. 使用chrono进行精确性能测量
 * 
 * 关键点分析：
 * 1. 问题转化：当W很大时，从重量维度转为价值维度
 * 2. 状态定义：dp[i]表示达到价值i所需的最小重量
 * 3. 结果提取：从后向前遍历找到第一个满足重量约束的价值
 * 4. 适用场景：W很大但总价值不大的情况
 * 
 * 工程化考量：
 * 1. 方法选择：根据W的大小选择合适的算法
 * 2. 内存优化：使用vector的动态分配
 * 3. 边界处理：处理各种极端情况
 * 4. 性能测试：包含大规模数据测试
 * 
 * 面试要点：
 * 1. 理解传统DP的局限性
 * 2. 掌握问题转化的思路
 * 3. 了解不同维度DP的适用场景
 * 4. 能够分析算法的时空复杂度
 */