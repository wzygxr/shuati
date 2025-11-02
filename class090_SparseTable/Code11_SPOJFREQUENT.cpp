#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <random>

using namespace std;

/**
 * SPOJ FREQUENT - 区间频繁值查询 - Sparse Table应用
 * 题目链接：https://www.spoj.com/problems/FREQUENT/
 * 
 * 【算法核心思想】
 * 结合游程编码和Sparse Table解决区间频繁值查询问题。
 * 由于数组是非降序的，可以将连续的相同数字压缩为游程，然后使用Sparse Table查询游程长度的最大值。
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n) - 游程编码 + O(m log m) - Sparse Table构建（m为游程数量）
 * - 查询：O(1) - 每次查询最多3次ST表查询
 * - 总时间复杂度：O(n + m log m + q)
 * 
 * 【空间复杂度分析】
 * - 游程数组：O(n)
 * - Sparse Table：O(m log m)
 * - 总空间复杂度：O(n + m log m)
 */

// 游程结构体
struct Run {
    int value;      // 游程的值
    int start;      // 游程开始位置
    int end;        // 游程结束位置
    int length;     // 游程长度
    
    Run(int v, int s, int e) : value(v), start(s), end(e), length(e - s + 1) {}
};

// Sparse Table类（最大值查询）
class SparseTable {
private:
    vector<vector<int>> st;
    vector<int> logTable;
    
public:
    SparseTable(const vector<int>& arr) {
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
                st[i][j] = max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
    }
    
    void preprocessLog(int n) {
        logTable[1] = 0;
        for (int i = 2; i <= n; i++) {
            logTable[i] = logTable[i / 2] + 1;
        }
    }
    
    int query(int l, int r) {
        if (l > r) return 0;
        int len = r - l + 1;
        int k = logTable[len];
        return max(st[l][k], st[r - (1 << k) + 1][k]);
    }
};

// 频繁值查询解决方案类
class FrequentQuerySolver {
private:
    vector<int> arr;           // 原始数组
    vector<Run> runs;          // 游程列表
    vector<int> runIndex;      // 每个位置对应的游程索引
    SparseTable* st;          // Sparse Table指针
    
public:
    FrequentQuerySolver(const vector<int>& inputArr) : arr(inputArr) {
        runIndex.resize(arr.size());
        
        // 执行游程编码
        runLengthEncoding();
        
        // 构建Sparse Table
        buildSparseTable();
    }
    
    ~FrequentQuerySolver() {
        delete st;
    }
    
    /**
     * 游程编码：将连续的相同数字压缩为游程
     */
    void runLengthEncoding() {
        if (arr.empty()) return;
        
        int currentValue = arr[0];
        int start = 0;
        
        for (int i = 1; i < arr.size(); i++) {
            if (arr[i] != currentValue) {
                // 结束当前游程
                runs.emplace_back(currentValue, start, i - 1);
                // 填充runIndex
                for (int j = start; j < i; j++) {
                    runIndex[j] = runs.size() - 1;
                }
                // 开始新游程
                currentValue = arr[i];
                start = i;
            }
        }
        
        // 处理最后一个游程
        runs.emplace_back(currentValue, start, arr.size() - 1);
        for (int j = start; j < arr.size(); j++) {
            runIndex[j] = runs.size() - 1;
        }
    }
    
    /**
     * 构建Sparse Table用于查询游程长度最大值
     */
    void buildSparseTable() {
        vector<int> lengths;
        for (const auto& run : runs) {
            lengths.push_back(run.length);
        }
        st = new SparseTable(lengths);
    }
    
    /**
     * 查询区间[l, r]内出现次数最多的数的出现次数
     */
    int query(int l, int r) {
        if (l > r || l < 0 || r >= arr.size()) {
            throw invalid_argument("Invalid query range");
        }
        
        int leftRunIndex = runIndex[l];
        int rightRunIndex = runIndex[r];
        
        // 情况1：查询区间完全在一个游程内
        if (leftRunIndex == rightRunIndex) {
            return r - l + 1;
        }
        
        // 情况2：查询区间跨越多个游程
        int maxFreq = 0;
        
        // 处理左边界游程
        Run& leftRun = runs[leftRunIndex];
        maxFreq = max(maxFreq, leftRun.end - l + 1);
        
        // 处理右边界游程
        Run& rightRun = runs[rightRunIndex];
        maxFreq = max(maxFreq, r - rightRun.start + 1);
        
        // 处理中间游程（如果存在）
        if (rightRunIndex - leftRunIndex > 1) {
            maxFreq = max(maxFreq, st->query(leftRunIndex + 1, rightRunIndex - 1));
        }
        
        return maxFreq;
    }
    
    // 获取游程信息（用于调试）
    const vector<Run>& getRuns() const {
        return runs;
    }
};

/**
 * 单元测试函数
 */
void testSPOJFREQUENT() {
    cout << "=== SPOJ FREQUENT 测试 ===" << endl;
    
    // 测试用例1：SPOJ示例
    vector<int> arr1 = {-1, -1, 1, 1, 1, 1, 3, 10, 10, 10};
    FrequentQuerySolver solver1(arr1);
    
    cout << "测试用例1 - SPOJ示例:" << endl;
    cout << "查询[0,1]: " << solver1.query(0, 1) << " (期望: 1)" << endl;
    cout << "查询[0,5]: " << solver1.query(0, 5) << " (期望: 2)" << endl;
    cout << "查询[5,9]: " << solver1.query(5, 9) << " (期望: 4)" << endl;
    
    // 测试用例2：所有元素相同
    vector<int> arr2 = {5, 5, 5, 5, 5};
    FrequentQuerySolver solver2(arr2);
    cout << "\n测试用例2 - 所有元素相同:" << endl;
    cout << "查询[0,4]: " << solver2.query(0, 4) << " (期望: 5)" << endl;
    
    // 测试用例3：每个元素都不同
    vector<int> arr3 = {1, 2, 3, 4, 5};
    FrequentQuerySolver solver3(arr3);
    cout << "\n测试用例3 - 每个元素都不同:" << endl;
    cout << "查询[0,4]: " << solver3.query(0, 4) << " (期望: 1)" << endl;
    
    // 测试用例4：混合情况
    vector<int> arr4 = {1, 1, 2, 2, 2, 3, 3, 3, 3, 4};
    FrequentQuerySolver solver4(arr4);
    cout << "\n测试用例4 - 混合情况:" << endl;
    cout << "查询[0,9]: " << solver4.query(0, 9) << " (期望: 4)" << endl;
    cout << "查询[2,6]: " << solver4.query(2, 6) << " (期望: 3)" << endl;
    
    // 性能测试
    vector<int> largeArr(100000);
    mt19937 rng(42);
    uniform_real_distribution<double> dist(0.0, 1.0);
    
    int current = 0;
    for (int i = 0; i < largeArr.size(); i++) {
        if (dist(rng) < 0.1) { // 10%概率改变值
            current = rng() % 100;
        }
        largeArr[i] = current;
    }
    
    FrequentQuerySolver largeSolver(largeArr);
    
    auto start = chrono::high_resolution_clock::now();
    for (int i = 0; i < 1000; i++) {
        int l = rng() % (largeArr.size() - 100);
        int r = l + rng() % 100;
        largeSolver.query(l, r);
    }
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "\n性能测试: 1000次查询耗时 " << duration.count() << "ms" << endl;
    
    cout << "\n=== 测试完成 ===" << endl;
}

int main() {
    testSPOJFREQUENT();
    return 0;
}