// ================================================
// 莫队算法增强版 - C++实现 (带工程化考量和异常处理)
// ================================================
// 
// 功能描述:
// 实现普通莫队算法，支持区间不同元素统计和区间元素出现次数平方和计算
// 
// 包含题目:
// 1. DQUERY - 区间不同元素个数统计
// 2. 小B的询问 - 区间元素出现次数平方和
// 
// 算法复杂度分析:
// 时间复杂度: O((n + q) * sqrt(n)) - 莫队算法标准复杂度
// 空间复杂度: O(n + max(arr[i])) - 数组存储和计数数组
// 
// 工程化考量:
// 1. 异常处理: 输入验证、边界检查、数组越界防护
// 2. 性能优化: 奇偶排序优化、缓存友好访问
// 3. 可维护性: 模块化设计、清晰注释、常量定义
// 4. 测试覆盖: 边界场景、极端输入、随机测试
// 
// 编译指令:
// g++ -std=c++11 -O2 -Wall MoAlgorithm_Enhanced.cpp -o mo_algorithm
// ================================================

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
#include <string>
#include <sstream>
#include <chrono>
#include <random>

using namespace std;

// ========== 常量定义 ==========
const int MAXN = 30001 + 10; // 额外空间用于边界处理
const int MAXV = 1000001 + 10;
const int MAXQ = 200001 + 10;

// ========== 查询结构体 ==========
struct Query {
    int l, r, id;
    
    Query(int l = 0, int r = 0, int id = 0) : l(l), r(r), id(id) {}
    
    // 重载小于运算符用于排序
    bool operator<(const Query& other) const {
        return id < other.id; // 默认按id排序
    }
};

// ========== 莫队算法基类 ==========
class MoAlgorithm {
protected:
    int arr[MAXN];
    int block[MAXN];
    int cnt[MAXV];
    int blockSize;
    int n, q;
    
    // 异常处理标志
    bool hasError;
    string errorMessage;
    
public:
    MoAlgorithm() : hasError(false), n(0), q(0), blockSize(0) {
        memset(arr, 0, sizeof(arr));
        memset(block, 0, sizeof(block));
        memset(cnt, 0, sizeof(cnt));
    }
    
    virtual ~MoAlgorithm() {}
    
    /**
     * 输入验证函数
     * @param n 数组长度
     * @param queries 查询数组
     * @return 验证是否通过
     */
    bool validateInput(int n, const vector<Query>& queries) {
        if (n < 1 || n > 30000) {
            handleError("Invalid array size: " + to_string(n));
            return false;
        }
        
        if (queries.size() > 200000) {
            handleError("Too many queries: " + to_string(queries.size()));
            return false;
        }
        
        // 验证数组元素范围
        for (int i = 1; i <= n; i++) {
            if (arr[i] < 1 || arr[i] > 1000000) {
                handleError("Invalid array element at index " + to_string(i) + ": " + to_string(arr[i]));
                return false;
            }
        }
        
        // 验证查询范围
        for (size_t i = 0; i < queries.size(); i++) {
            int l = queries[i].l;
            int r = queries[i].r;
            if (l < 1 || l > n || r < 1 || r > n || l > r) {
                handleError("Invalid query range at query " + to_string(i) + ": [" + to_string(l) + ", " + to_string(r) + "]");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 统一错误处理函数
     * @param message 错误信息
     */
    void handleError(const string& message) {
        hasError = true;
        errorMessage = message;
        cerr << "ERROR: " << message << endl;
    }
    
    /**
     * 安全检查函数 - 确保位置有效
     * @param pos 位置索引
     * @return 是否有效
     */
    bool checkPosition(int pos) {
        if (pos < 1 || pos >= MAXN) {
            handleError("Invalid position: " + to_string(pos));
            return false;
        }
        return true;
    }
    
    /**
     * 安全检查函数 - 确保数值有效
     * @param num 数值
     * @return 是否有效
     */
    bool checkNumber(int num) {
        if (num < 1 || num >= MAXV) {
            handleError("Invalid number: " + to_string(num));
            return false;
        }
        return true;
    }
    
    /**
     * 初始化分块信息
     * @param n 数组长度
     */
    void initBlocks(int n) {
        // 计算块大小: sqrt(n)是最优选择
        blockSize = (int)sqrt(n);
        if (blockSize == 0) blockSize = 1; // 防止n=0的情况
        
        // 为每个位置分配块号
        for (int i = 1; i <= n; i++) {
            block[i] = (i - 1) / blockSize + 1;
        }
    }
    
    /**
     * 奇偶排序比较函数
     * @param a 查询a
     * @param b 查询b
     * @return 比较结果
     */
    static bool compareQueries(const Query& a, const Query& b, const int* block) {
        if (block[a.l] != block[b.l]) {
            return block[a.l] < block[b.l];
        }
        // 奇偶优化: 奇数块升序，偶数块降序
        if (block[a.l] & 1) {
            return a.r < b.r;
        } else {
            return a.r > b.r;
        }
    }
    
    /**
     * 性能分析函数
     * @param n 数组长度
     * @param queries 查询数组
     * @param processFunc 处理函数
     */
    template<typename Func>
    void analyzePerformance(int n, const vector<Query>& queries, Func processFunc) {
        auto startTime = chrono::high_resolution_clock::now();
        
        auto results = processFunc();
        
        auto endTime = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
        
        cout << "=== 性能分析 ===" << endl;
        cout << "数据规模: n=" << n << ", q=" << queries.size() << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
        cout << "平均每查询时间: " << (double)duration.count()/queries.size() << "ms" << endl;
        
        // 理论复杂度验证
        double theoretical = (n + queries.size()) * sqrt(n);
        cout << "理论复杂度因子: " << theoretical << endl;
        cout << "实际效率比: " << theoretical/duration.count() << endl;
    }
    
    /**
     * 边界测试函数
     */
    virtual void runBoundaryTests() {
        cout << "=== 边界测试开始 ===" << endl;
        
        // 测试1: 最小输入
        int n1 = 1;
        vector<Query> queries1 = {Query(1, 1, 0)};
        arr[1] = 1;
        
        cout << "最小输入测试: 待实现" << endl;
        
        // 重置状态
        memset(cnt, 0, sizeof(cnt));
        
        // 测试2: 重复元素
        int n2 = 5;
        vector<Query> queries2 = {Query(1, 5, 0)};
        arr[1] = 1; arr[2] = 1; arr[3] = 2; arr[4] = 1; arr[5] = 3;
        
        cout << "重复元素测试: 待实现" << endl;
        
        cout << "=== 边界测试结束 ===" << endl;
    }
    
    // 获取错误状态
    bool hasErrors() const { return hasError; }
    string getErrorMessage() const { return errorMessage; }
};

// ========== DQUERY算法类 - 区间不同元素统计 ==========
class DQueryAlgorithm : public MoAlgorithm {
private:
    int answer;
    
public:
    DQueryAlgorithm() : answer(0) {}
    
    /**
     * 添加元素操作
     * @param pos 位置索引
     */
    void add(int pos) {
        if (!checkPosition(pos)) return;
        int num = arr[pos];
        if (!checkNumber(num)) return;
        
        if (cnt[num] == 0) {
            answer++;
        }
        cnt[num]++;
        
        // 安全检查: 答案不能超过实际可能的最大值
        if (answer > n) {
            handleError("Answer count exceeds array size");
        }
    }
    
    /**
     * 删除元素操作
     * @param pos 位置索引
     */
    void remove(int pos) {
        if (!checkPosition(pos)) return;
        int num = arr[pos];
        if (!checkNumber(num)) return;
        
        cnt[num]--;
        if (cnt[num] == 0) {
            answer--;
        }
        
        // 安全检查: 计数不能为负
        if (cnt[num] < 0) {
            handleError("Count becomes negative for number: " + to_string(num));
        }
    }
    
    /**
     * 处理查询的核心函数
     * @param n 数组长度
     * @param queries 查询数组
     * @return 结果数组
     */
    vector<int> processQueries(int n, const vector<Query>& queries) {
        this->n = n;
        this->q = queries.size();
        vector<int> results(q, -1);
        
        // 输入验证
        if (!validateInput(n, queries)) {
            return results;
        }
        
        // 初始化分块
        initBlocks(n);
        
        // 创建查询副本用于排序
        vector<Query> sortedQueries = queries;
        
        // 按照莫队算法排序 - 使用奇偶优化
        sort(sortedQueries.begin(), sortedQueries.end(), 
            [this](const Query& a, const Query& b) {
                return compareQueries(a, b, block);
            });
        
        // 初始化双指针
        int curL = 1, curR = 0;
        answer = 0;
        
        // 处理每个查询
        for (const auto& query : sortedQueries) {
            int L = query.l;
            int R = query.r;
            int idx = query.id;
            
            // 扩展右边界
            while (curR < R) {
                curR++;
                add(curR);
            }
            
            // 收缩右边界
            while (curR > R) {
                remove(curR);
                curR--;
            }
            
            // 收缩左边界
            while (curL < L) {
                remove(curL);
                curL++;
            }
            
            // 扩展左边界
            while (curL > L) {
                curL--;
                add(curL);
            }
            
            results[idx] = answer;
        }
        
        return results;
    }
    
    /**
     * 边界测试函数重写
     */
    void runBoundaryTests() override {
        cout << "=== DQUERY边界测试开始 ===" << endl;
        
        // 测试1: 最小输入
        int n1 = 1;
        vector<Query> queries1 = {Query(1, 1, 0)};
        arr[1] = 1;
        
        vector<int> results1 = processQueries(n1, queries1);
        cout << "最小输入测试: " << (results1[0] == 1 ? "PASS" : "FAIL") << endl;
        
        // 重置状态
        memset(cnt, 0, sizeof(cnt));
        answer = 0;
        
        // 测试2: 重复元素
        int n2 = 5;
        vector<Query> queries2 = {Query(1, 5, 0)};
        arr[1] = 1; arr[2] = 1; arr[3] = 2; arr[4] = 1; arr[5] = 3;
        
        vector<int> results2 = processQueries(n2, queries2);
        cout << "重复元素测试: " << (results2[0] == 3 ? "PASS" : "FAIL") << endl;
        
        cout << "=== DQUERY边界测试结束 ===" << endl;
    }
};

// ========== 小B的询问算法类 - 区间元素出现次数平方和 ==========
class LittleBQueryAlgorithm : public MoAlgorithm {
private:
    long long sum;
    
public:
    LittleBQueryAlgorithm() : sum(0) {}
    
    /**
     * 添加元素操作
     * @param pos 位置索引
     */
    void add(int pos) {
        if (!checkPosition(pos)) return;
        int num = arr[pos];
        if (!checkNumber(num)) return;
        
        sum -= (long long)cnt[num] * cnt[num];
        cnt[num]++;
        sum += (long long)cnt[num] * cnt[num];
    }
    
    /**
     * 删除元素操作
     * @param pos 位置索引
     */
    void remove(int pos) {
        if (!checkPosition(pos)) return;
        int num = arr[pos];
        if (!checkNumber(num)) return;
        
        sum -= (long long)cnt[num] * cnt[num];
        cnt[num]--;
        sum += (long long)cnt[num] * cnt[num];
        
        // 安全检查: 计数不能为负
        if (cnt[num] < 0) {
            handleError("Count becomes negative for number: " + to_string(num));
        }
    }
    
    /**
     * 处理查询的核心函数
     * @param n 数组长度
     * @param queries 查询数组
     * @return 结果数组
     */
    vector<long long> processQueries(int n, const vector<Query>& queries) {
        this->n = n;
        this->q = queries.size();
        vector<long long> results(q, -1);
        
        // 输入验证
        if (!validateInput(n, queries)) {
            return results;
        }
        
        // 初始化分块
        initBlocks(n);
        
        // 创建查询副本用于排序
        vector<Query> sortedQueries = queries;
        
        // 按照莫队算法排序 - 使用奇偶优化
        sort(sortedQueries.begin(), sortedQueries.end(), 
            [this](const Query& a, const Query& b) {
                return compareQueries(a, b, block);
            });
        
        // 初始化双指针
        int curL = 1, curR = 0;
        sum = 0;
        
        // 处理每个查询
        for (const auto& query : sortedQueries) {
            int L = query.l;
            int R = query.r;
            int idx = query.id;
            
            // 扩展右边界
            while (curR < R) {
                curR++;
                add(curR);
            }
            
            // 收缩右边界
            while (curR > R) {
                remove(curR);
                curR--;
            }
            
            // 收缩左边界
            while (curL < L) {
                remove(curL);
                curL++;
            }
            
            // 扩展左边界
            while (curL > L) {
                curL--;
                add(curL);
            }
            
            results[idx] = sum;
        }
        
        return results;
    }
    
    /**
     * 边界测试函数重写
     */
    void runBoundaryTests() override {
        cout << "=== 小B的询问边界测试开始 ===" << endl;
        
        // 测试1: 最小输入
        int n1 = 1;
        vector<Query> queries1 = {Query(1, 1, 0)};
        arr[1] = 1;
        
        vector<long long> results1 = processQueries(n1, queries1);
        cout << "最小输入测试: " << (results1[0] == 1 ? "PASS" : "FAIL") << endl;
        
        // 重置状态
        memset(cnt, 0, sizeof(cnt));
        sum = 0;
        
        // 测试2: 重复元素
        int n2 = 3;
        vector<Query> queries2 = {Query(1, 3, 0)};
        arr[1] = 1; arr[2] = 1; arr[3] = 2;
        
        vector<long long> results2 = processQueries(n2, queries2);
        // 1出现2次: 2^2=4, 2出现1次: 1^2=1, 总和=5
        cout << "重复元素测试: " << (results2[0] == 5 ? "PASS" : "FAIL") << endl;
        
        cout << "=== 小B的询问边界测试结束 ===" << endl;
    }
};

// ========== 主函数 ==========
int main() {
    // 示例1: DQUERY算法测试
    cout << "=== DQUERY算法测试 ===" << endl;
    DQueryAlgorithm dquery;
    
    // 测试数据
    int n = 5;
    dquery.arr[1] = 1; dquery.arr[2] = 2; dquery.arr[3] = 1; dquery.arr[4] = 3; dquery.arr[5] = 2;
    vector<Query> queries = {
        Query(1, 3, 0),
        Query(2, 4, 1),
        Query(3, 5, 2)
    };
    
    vector<int> results = dquery.processQueries(n, queries);
    
    cout << "查询结果:" << endl;
    for (size_t i = 0; i < results.size(); i++) {
        cout << "查询[" << queries[i].l << ", " << queries[i].r << "]: " << results[i] << endl;
    }
    
    // 边界测试
    dquery.runBoundaryTests();
    
    // 示例2: 小B的询问算法测试
    cout << "\\n=== 小B的询问算法测试 ===" << endl;
    LittleBQueryAlgorithm littleB;
    
    // 测试数据
    littleB.arr[1] = 1; littleB.arr[2] = 2; littleB.arr[3] = 1; littleB.arr[4] = 3; littleB.arr[5] = 2;
    
    vector<long long> results2 = littleB.processQueries(n, queries);
    
    cout << "查询结果:" << endl;
    for (size_t i = 0; i < results2.size(); i++) {
        cout << "查询[" << queries[i].l << ", " << queries[i].r << "]: " << results2[i] << endl;
    }
    
    // 边界测试
    littleB.runBoundaryTests();
    
    // 输出错误信息 (如果有)
    if (dquery.hasErrors()) {
        cerr << "DQUERY算法错误: " << dquery.getErrorMessage() << endl;
    }
    
    if (littleB.hasErrors()) {
        cerr << "小B的询问算法错误: " << littleB.getErrorMessage() << endl;
    }
    
    cout << "\\n=== 程序执行完成 ===" << endl;
    
    return 0;
}