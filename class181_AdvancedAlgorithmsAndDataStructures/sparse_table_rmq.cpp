#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <memory>
#include <limits>
#include <random>
#include <chrono>

using namespace std;

/**
 * 稀疏表（Sparse Table）实现 - RMQ问题解决方案 (C++版本)
 * 
 * 算法思路：
 * 稀疏表是一种用于解决范围查询问题的数据结构，特别适用于：
 * 1. 范围最小值查询（RMQ）
 * 2. 范围最大值查询
 * 3. 其他满足结合律和幂等性的操作
 * 
 * 稀疏表通过预处理，在O(n log n)时间内构建一个二维数组，
 * 然后在O(1)时间内回答任何范围查询。
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 查询：O(1)
 * 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 数据库：范围查询优化
 * 2. 图像处理：区域统计信息计算
 * 3. 金融：时间序列分析中的极值查询
 * 4. 算法竞赛：优化动态规划中的范围查询
 * 
 * 相关题目：
 * 1. LeetCode 2444. 统计定界子数组的数目
 * 2. POJ 3264 Balanced Lineup
 * 3. SPOJ RMQSQ - Range Minimum Query
 */

class SparseTableRMQ {
private:
    vector<int> data;
    vector<vector<int>> stMin;  // 用于范围最小值查询的稀疏表
    vector<vector<int>> stMax;  // 用于范围最大值查询的稀疏表
    vector<int> logTable;
    int n;
    
    /**
     * 预计算log2值表
     */
    void precomputeLogTable() {
        logTable.resize(n + 1);
        logTable[1] = 0;
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
    }
    
    /**
     * 构建稀疏表
     */
    void buildSparseTable() {
        int k = logTable[n] + 1;
        
        // 初始化稀疏表
        stMin.assign(k, vector<int>(n));
        stMax.assign(k, vector<int>(n));
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            stMin[0][i] = data[i];
            stMax[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                int prevLen = 1 << (j - 1);
                // 范围最小值查询
                stMin[j][i] = min(stMin[j-1][i], stMin[j-1][i + prevLen]);
                // 范围最大值查询
                stMax[j][i] = max(stMax[j-1][i], stMax[j-1][i + prevLen]);
            }
        }
    }
    
public:
    /**
     * 构造函数
     * @param data 输入数组
     */
    SparseTableRMQ(const vector<int>& data) {
        if (data.empty()) {
            throw invalid_argument("输入数组不能为空");
        }
        
        this->data = data;
        this->n = data.size();
        
        // 预计算log表
        precomputeLogTable();
        
        // 构建稀疏表
        buildSparseTable();
    }
    
    /**
     * 范围最小值查询
     * 时间复杂度：O(1)
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最小值
     */
    int queryMin(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return min(stMin[k][left], stMin[k][right - (1 << k) + 1]);
    }
    
    /**
     * 范围最大值查询
     * 时间复杂度：O(1)
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最大值
     */
    int queryMax(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw invalid_argument("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return max(stMax[k][left], stMax[k][right - (1 << k) + 1]);
    }
    
    /**
     * 批量处理范围查询
     * @param queries 查询数组，每个查询是[left, right]形式的数组
     * @param isMinQuery 是否是最小值查询，否则是最大值查询
     * @return 查询结果数组
     */
    vector<int> batchQuery(const vector<pair<int, int>>& queries, bool isMinQuery) {
        if (queries.empty()) {
            throw invalid_argument("查询数组不能为空");
        }
        
        vector<int> results;
        results.reserve(queries.size());
        
        for (const auto& query : queries) {
            if (isMinQuery) {
                results.push_back(queryMin(query.first, query.second));
            } else {
                results.push_back(queryMax(query.first, query.second));
            }
        }
        
        return results;
    }
};

/**
 * 测试稀疏表
 */
void testSparseTable() {
    cout << "=== 测试稀疏表 ===" << endl;
    
    vector<int> data = {1, 3, 5, 7, 9, 11, 13, 15, 17};
    
    SparseTableRMQ st(data);
    
    // 测试最小值查询
    cout << "测试最小值查询:" << endl;
    cout << "区间[1, 5]的最小值: " << st.queryMin(1, 5) << endl;  // 应该是3
    cout << "区间[0, 8]的最小值: " << st.queryMin(0, 8) << endl;  // 应该是1
    cout << "区间[4, 7]的最小值: " << st.queryMin(4, 7) << endl;  // 应该是9
    
    // 测试最大值查询
    cout << "\n测试最大值查询:" << endl;
    cout << "区间[1, 5]的最大值: " << st.queryMax(1, 5) << endl;  // 应该是11
    cout << "区间[0, 8]的最大值: " << st.queryMax(0, 8) << endl;  // 应该是17
    cout << "区间[4, 7]的最大值: " << st.queryMax(4, 7) << endl;  // 应该是15
    
    // 测试批量查询
    cout << "\n测试批量查询:" << endl;
    vector<pair<int, int>> queries = {
        {0, 2}, {1, 5}, {3, 7}, {2, 8}
    };
    
    vector<int> minResults = st.batchQuery(queries, true);
    cout << "批量最小值查询结果: ";
    for (int val : minResults) {
        cout << val << " ";
    }
    cout << endl;
    
    vector<int> maxResults = st.batchQuery(queries, false);
    cout << "批量最大值查询结果: ";
    for (int val : maxResults) {
        cout << val << " ";
    }
    cout << endl;
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    
    // 生成大数据集
    int n = 100000;
    vector<int> largeData(n);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(1, 1000000);
    
    for (int i = 0; i < n; i++) {
        largeData[i] = dis(gen);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    SparseTableRMQ largeST(largeData);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto buildTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    // 执行大量查询
    int numQueries = 100000;
    vector<pair<int, int>> largeQueries;
    uniform_int_distribution<int> dis2(0, n - 2);
    
    for (int i = 0; i < numQueries; i++) {
        // 生成有效的查询范围
        int left = dis2(gen);
        uniform_int_distribution<int> dis3(left + 1, min(left + 1000, n - 1));
        int right = dis3(gen);
        largeQueries.push_back({left, right});
    }
    
    startTime = chrono::high_resolution_clock::now();
    for (const auto& query : largeQueries) {
        largeST.queryMin(query.first, query.second);
    }
    endTime = chrono::high_resolution_clock::now();
    
    auto queryTime = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "构建100000个元素的稀疏表时间: " << buildTime.count() / 1000.0 << " ms" << endl;
    cout << "执行100000次查询时间: " << queryTime.count() / 1000.0 << " ms" << endl;
    cout << "平均每次查询时间: " << queryTime.count() / double(numQueries) << " μs" << endl;
}

int main() {
    testSparseTable();
    return 0;
}