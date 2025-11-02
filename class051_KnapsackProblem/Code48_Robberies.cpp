#include <iostream>
#include <vector>
#include <algorithm>
#include <random>
#include <chrono>

using namespace std;

// HDU 2955 Robberies
// 题目描述：抢劫犯想抢劫银行，每个银行有一定的金额和被抓的概率。
// 抢劫犯希望在被抓概率不超过某个值的情况下，抢劫到最多的钱。
// 链接：https://acm.hdu.edu.cn/showproblem.php?pid=2955
// 
// 解题思路：
// 这是一个概率背包问题，需要将问题转化为标准的01背包问题。
// 关键点：将金额作为背包容量，将安全概率（1-被抓概率）作为价值。
// 
// 状态定义：dp[i] 表示抢劫到金额i时的最大安全概率
// 状态转移方程：dp[i] = max(dp[i], dp[i-money[j]] * (1-p[j]))
// 初始状态：dp[0] = 1（抢劫0元时安全概率为1）
// 
// 时间复杂度：O(N * totalMoney)，其中N是银行数量，totalMoney是总金额
// 空间复杂度：O(totalMoney)，使用一维DP数组
// 
// 工程化考量：
// 1. 精度处理：使用double类型处理概率
// 2. 边界条件：处理概率为0或1的情况
// 3. 性能优化：计算总金额作为背包容量上限
// 4. 异常处理：处理非法输入

class Robberies {
public:
    /**
     * 动态规划解法 - 概率背包问题
     * @param P 最大允许被抓概率
     * @param money 每个银行的金额数组
     * @param prob 每个银行的被抓概率数组
     * @return 在安全概率范围内的最大抢劫金额
     */
    static double rob(double P, vector<int>& money, vector<double>& prob) {
        // 参数验证
        if (money.size() != prob.size()) {
            throw invalid_argument("Money and probability arrays must have same size");
        }
        if (P < 0 || P > 1) {
            throw invalid_argument("Probability P must be between 0 and 1");
        }
        
        int n = money.size();
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额作为背包容量上限
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        // 创建DP数组，dp[i]表示抢劫到金额i时的最大安全概率
        vector<double> dp(totalMoney + 1, 0.0);
        dp[0] = 1.0; // 抢劫0元时安全概率为1
        
        // 01背包：遍历每个银行
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double p = prob[i];
            double safeProb = 1 - p; // 安全概率
            
            // 倒序遍历金额，避免重复选择
            for (int j = totalMoney; j >= m; j--) {
                if (dp[j - m] > 0) {
                    dp[j] = max(dp[j], dp[j - m] * safeProb);
                }
            }
        }
        
        // 寻找最大的金额，使得安全概率 >= 1-P
        double minSafeProb = 1 - P;
        int maxMoney = 0;
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] >= minSafeProb) {
                maxMoney = j;
                break;
            }
        }
        
        return maxMoney;
    }
    
    /**
     * 优化的动态规划解法 - 提前终止遍历
     */
    static double robOptimized(double P, vector<int>& money, vector<double>& prob) {
        if (money.size() != prob.size()) {
            throw invalid_argument("Money and probability arrays must have same size");
        }
        if (P < 0 || P > 1) {
            throw invalid_argument("Probability P must be between 0 and 1");
        }
        
        int n = money.size();
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        vector<double> dp(totalMoney + 1, 0.0);
        dp[0] = 1.0;
        
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double safeProb = 1 - prob[i];
            
            for (int j = totalMoney; j >= m; j--) {
                if (dp[j - m] > 0) {
                    double newProb = dp[j - m] * safeProb;
                    if (newProb > dp[j]) {
                        dp[j] = newProb;
                    }
                }
            }
        }
        
        double minSafeProb = 1 - P;
        // 从大到小遍历，找到第一个满足条件的金额
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] >= minSafeProb) {
                return j;
            }
        }
        
        return 0;
    }
    
    /**
     * 另一种思路：将金额作为价值，概率作为约束
     */
    static double robAlternative(double P, vector<int>& money, vector<double>& prob) {
        if (money.size() != prob.size()) {
            throw invalid_argument("Money and probability arrays must have same size");
        }
        if (P < 0 || P > 1) {
            throw invalid_argument("Probability P must be between 0 and 1");
        }
        
        int n = money.size();
        if (n == 0) {
            return 0;
        }
        
        // 计算总金额
        int totalMoney = 0;
        for (int m : money) {
            totalMoney += m;
        }
        
        // dp[i]表示达到金额i所需的最小被抓概率
        vector<double> dp(totalMoney + 1, 1.0); // 初始化为最大概率1
        dp[0] = 0.0; // 抢劫0元时被抓概率为0
        
        for (int i = 0; i < n; i++) {
            int m = money[i];
            double p = prob[i];
            
            for (int j = totalMoney; j >= m; j--) {
                // 计算选择当前银行的被抓概率
                double newProb = 1 - (1 - dp[j - m]) * (1 - p);
                if (newProb < dp[j]) {
                    dp[j] = newProb;
                }
            }
        }
        
        // 寻找最大的金额，使得被抓概率 <= P
        for (int j = totalMoney; j >= 0; j--) {
            if (dp[j] <= P) {
                return j;
            }
        }
        
        return 0;
    }
};

/**
 * 测试函数
 */
void testRobberies() {
    // 测试用例
    vector<double> P = {0.1, 0.05, 0.5};
    vector<vector<int>> moneyCases = {
        {10, 20, 30},
        {1, 2, 3, 4},
        {100, 200, 300}
    };
    vector<vector<double>> probCases = {
        {0.05, 0.1, 0.2},
        {0.01, 0.02, 0.03, 0.04},
        {0.3, 0.2, 0.1}
    };
    
    cout << "抢劫银行问题测试：" << endl;
    for (size_t i = 0; i < P.size(); i++) {
        double p = P[i];
        vector<int> money = moneyCases[i];
        vector<double> prob = probCases[i];
        
        double result1 = Robberies::rob(p, money, prob);
        double result2 = Robberies::robOptimized(p, money, prob);
        double result3 = Robberies::robAlternative(p, money, prob);
        
        cout << "P=" << p << ", money=[";
        for (size_t j = 0; j < money.size(); j++) {
            cout << money[j];
            if (j < money.size() - 1) cout << ", ";
        }
        cout << "], prob=[";
        for (size_t j = 0; j < prob.size(); j++) {
            cout << prob[j];
            if (j < prob.size() - 1) cout << ", ";
        }
        cout << "]: 方法1=" << result1 
             << ", 方法2=" << result2 
             << ", 方法3=" << result3 << endl;
        
        // 验证结果一致性（允许小的浮点数误差）
        if (abs(result1 - result2) > 1e-6 || abs(result2 - result3) > 1e-6) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    int n = 50;
    vector<int> largeMoney(n);
    vector<double> largeProb(n);
    
    // 生成随机测试数据
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> moneyDist(1, 1000);
    uniform_real_distribution<> probDist(0.0, 0.1);
    
    for (int i = 0; i < n; i++) {
        largeMoney[i] = moneyDist(gen);
        largeProb[i] = probDist(gen);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    double largeResult = Robberies::robOptimized(0.1, largeMoney, largeProb);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: 银行数量=" << n << ", 结果=" << largeResult 
         << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<int> emptyMoney;
    vector<double> emptyProb;
    cout << "空数组: " << Robberies::rob(0.1, emptyMoney, emptyProb) << endl;
    
    vector<int> singleMoney = {10};
    vector<double> singleProb = {0.1};
    cout << "P=0: " << Robberies::rob(0.0, singleMoney, singleProb) << endl;
    
    vector<int> doubleMoney = {10, 20};
    vector<double> doubleProb = {0.1, 0.2};
    cout << "P=1: " << Robberies::rob(1.0, doubleMoney, doubleProb) << endl;
    
    // 特殊测试：概率为0的银行
    cout << "概率为0的银行测试：" << endl;
    vector<int> specialMoney = {100, 200};
    vector<double> specialProb = {0.0, 0.0};
    double specialResult = Robberies::rob(0.01, specialMoney, specialProb);
    cout << "特殊测试结果: " << specialResult << endl;
}

int main() {
    try {
        testRobberies();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（概率背包）
 * - 时间复杂度：O(N * totalMoney)
 *   - N: 银行数量
 *   - totalMoney: 总金额
 * - 空间复杂度：O(totalMoney)
 * 
 * 方法2：优化的动态规划
 * - 时间复杂度：O(N * totalMoney)（与方法1相同）
 * - 空间复杂度：O(totalMoney)
 * 
 * 方法3：替代思路的动态规划
 * - 时间复杂度：O(N * totalMoney)
 * - 空间复杂度：O(totalMoney)
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用STL算法进行最大值计算
 * 3. 使用随机数生成器进行性能测试
 * 4. 使用chrono进行精确性能测量
 * 
 * 关键点分析：
 * 1. 问题转化：将概率问题转化为标准的背包问题
 * 2. 精度处理：使用double类型处理概率计算
 * 3. 状态定义：dp[i]表示金额i对应的最大安全概率
 * 4. 结果提取：从后向前遍历找到第一个满足条件的金额
 * 
 * 工程化考量：
 * 1. 模块化设计：将不同解法封装为静态方法
 * 2. 异常处理：使用try-catch处理异常
 * 3. 性能优化：利用STL容器和算法
 * 4. 测试覆盖：包含各种边界情况和性能测试
 * 
 * 面试要点：
 * 1. 理解概率背包问题的转化思路
 * 2. 掌握浮点数精度处理技巧
 * 3. 了解不同状态定义对算法的影响
 * 4. 能够分析算法的时空复杂度
 */