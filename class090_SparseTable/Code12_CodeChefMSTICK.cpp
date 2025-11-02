#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>
#include <stdexcept>

using namespace std;

/**
 * CodeChef MSTICK - 区间最值查询 - Sparse Table应用
 * 题目链接：https://www.codechef.com/problems/MSTICK
 * 
 * 【算法核心思想】
 * 使用Sparse Table同时预处理区间最大值和最小值，实现O(1)查询。
 * 对于每个查询，分别查询最大值和最小值，然后计算它们的差值。
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n log n) - 构建两个ST表
 * - 查询：O(1) - 每次查询两次ST表查询
 * - 总时间复杂度：O(n log n + q)
 * 
 * 【空间复杂度分析】
 * - 最大值ST表：O(n log n)
 * - 最小值ST表：O(n log n)
 * - 总空间复杂度：O(n log n)
 */

// 通用的Sparse Table实现类
class SparseTable {
private:
    vector<vector<int>> st;     // ST表
    vector<int> logTable;       // 预处理log2值
    bool isMaxQuery;            // true表示最大值查询，false表示最小值查询
    
public:
    SparseTable(const vector<int>& arr, bool maxQuery) : isMaxQuery(maxQuery) {
        int n = arr.size();
        if (n == 0) return;
        
        int maxLog = log2(n) + 1;
        st.resize(n, vector<int>(maxLog));
        logTable.resize(n + 1);
        
        preprocessLog(n);
        
        // 初始化第一层
        for (int i = 0; i < n; i++) {
            st[i][0] = arr[i];
        }
        
        // 动态规划构建ST表
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                if (isMaxQuery) {
                    st[i][j] = max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                } else {
                    st[i][j] = min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
    }
    
    /**
     * 预处理log2值
     */
    void preprocessLog(int n) {
        logTable[1] = 0;
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
    }
    
    /**
     * 查询区间[l, r]的最值
     */
    int query(int l, int r) {
        if (l > r) {
            throw invalid_argument("Invalid range");
        }
        
        int len = r - l + 1;
        int k = logTable[len];
        
        if (isMaxQuery) {
            return max(st[l][k], st[r - (1 << k) + 1][k]);
        } else {
            return min(st[l][k], st[r - (1 << k) + 1][k]);
        }
    }
};

// 区间最值查询解决方案类
class RangeMinMaxQuery {
private:
    vector<int> arr;           // 原始数组
    SparseTable* maxSt;       // 最大值Sparse Table
    SparseTable* minSt;       // 最小值Sparse Table
    
public:
    RangeMinMaxQuery(const vector<int>& inputArr) : arr(inputArr) {
        maxSt = new SparseTable(arr, true);   // 最大值查询
        minSt = new SparseTable(arr, false);  // 最小值查询
    }
    
    ~RangeMinMaxQuery() {
        delete maxSt;
        delete minSt;
    }
    
    /**
     * 查询区间[l, r]的最大值和最小值的差值
     */
    int queryDifference(int l, int r) {
        if (l < 0 || r >= arr.size() || l > r) {
            throw invalid_argument("Invalid query range");
        }
        
        int maxVal = maxSt->query(l, r);
        int minVal = minSt->query(l, r);
        
        return maxVal - minVal;
    }
    
    /**
     * 分别查询最大值和最小值
     */
    pair<int, int> queryMinMax(int l, int r) {
        if (l < 0 || r >= arr.size() || l > r) {
            throw invalid_argument("Invalid query range");
        }
        
        int minVal = minSt->query(l, r);
        int maxVal = maxSt->query(l, r);
        
        return {minVal, maxVal};
    }
};

/**
 * 单元测试函数
 */
void testCodeChefMSTICK() {
    cout << "=== CodeChef MSTICK 测试 ===" << endl;
    
    // 测试用例1：CodeChef示例
    vector<int> arr1 = {1, 2, 3, 4, 5};
    RangeMinMaxQuery solver1(arr1);
    
    cout << "测试用例1 - CodeChef示例:" << endl;
    cout << "查询[0,4]: " << solver1.queryDifference(0, 4) << " (期望: 4)" << endl;
    cout << "查询[1,3]: " << solver1.queryDifference(1, 3) << " (期望: 2)" << endl;
    cout << "查询[2,4]: " << solver1.queryDifference(2, 4) << " (期望: 2)" << endl;
    
    // 测试用例2：所有元素相同
    vector<int> arr2 = {5, 5, 5, 5, 5};
    RangeMinMaxQuery solver2(arr2);
    cout << "\n测试用例2 - 所有元素相同:" << endl;
    cout << "查询[0,4]: " << solver2.queryDifference(0, 4) << " (期望: 0)" << endl;
    
    // 测试用例3：递减序列
    vector<int> arr3 = {5, 4, 3, 2, 1};
    RangeMinMaxQuery solver3(arr3);
    cout << "\n测试用例3 - 递减序列:" << endl;
    cout << "查询[0,4]: " << solver3.queryDifference(0, 4) << " (期望: 4)" << endl;
    
    // 测试用例4：随机数组
    vector<int> arr4 = {3, 7, 1, 9, 4, 6, 2, 8, 5};
    RangeMinMaxQuery solver4(arr4);
    cout << "\n测试用例4 - 随机数组:" << endl;
    cout << "查询[0,8]: " << solver4.queryDifference(0, 8) << " (期望: 8)" << endl;
    cout << "查询[2,6]: " << solver4.queryDifference(2, 6) << " (期望: 8)" << endl;
    
    // 分别查询最小值和最大值
    auto minMax = solver4.queryMinMax(2, 6);
    cout << "区间[2,6]的最小值: " << minMax.first << ", 最大值: " << minMax.second << endl;
    
    // 性能测试
    vector<int> largeArr(100000);
    mt19937 rng(42);
    uniform_int_distribution<int> dist(0, 1000000);
    
    for (int i = 0; i < largeArr.size(); i++) {
        largeArr[i] = dist(rng);
    }
    
    RangeMinMaxQuery largeSolver(largeArr);
    
    auto start = chrono::high_resolution_clock::now();
    for (int i = 0; i < 1000; i++) {
        int l = rng() % (largeArr.size() - 100);
        int r = l + rng() % 100;
        largeSolver.queryDifference(l, r);
    }
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "\n性能测试: 1000次查询耗时 " << duration.count() << "ms" << endl;
    
    // 边界测试
    cout << "\n边界测试:" << endl;
    try {
        solver1.queryDifference(-1, 4);
    } catch (const invalid_argument& e) {
        cout << "边界测试1通过: " << e.what() << endl;
    }
    
    try {
        solver1.queryDifference(3, 2);
    } catch (const invalid_argument& e) {
        cout << "边界测试2通过: " << e.what() << endl;
    }
    
    cout << "\n=== 测试完成 ===" << endl;
}

int main() {
    testCodeChefMSTICK();
    return 0;
}