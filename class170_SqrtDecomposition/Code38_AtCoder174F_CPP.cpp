#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <unordered_map>
using namespace std;

/**
 * AtCoder ABC174 F Range Set Query
 * 题目要求：区间查询不同元素个数
 * 核心技巧：莫队算法 + 分块
 * 时间复杂度：O(n√n)
 * 测试链接：https://atcoder.jp/contests/abc174/tasks/abc174_f
 * 
 * 莫队算法是一种离线查询的优化算法，适用于处理大量区间查询问题。
 * 它的核心思想是将查询按照块进行排序，然后逐个处理查询，利用之前计算的结果进行增量更新。
 * 对于不同元素个数查询，我们可以维护一个计数数组，记录当前区间内各元素出现的次数。
 */

int n; // 数组长度
int q; // 查询次数
vector<int> a; // 原始数组
int blockSize; // 块大小
vector<int> count; // 元素计数数组
int currentDistinct; // 当前区间不同元素个数
vector<int> answers; // 存储查询答案

/**
 * 查询结构体，存储查询的左右边界和索引
 */
struct Query {
    int l, r, idx;
    
    Query(int l, int r, int idx)
        : l(l), r(r), idx(idx) {}
    
    /**
     * 排序规则：先按左端点所在块排序，同块内按右端点排序
     * 对于偶数块，右端点升序；对于奇数块，右端点降序（奇偶优化）
     */
    bool operator<(const Query& other) const {
        int block1 = l / blockSize;
        int block2 = other.l / blockSize;
        if (block1 != block2) {
            return block1 < block2;
        }
        // 奇偶优化
        return (block1 % 2 == 0) ? (r < other.r) : (r > other.r);
    }
};

/**
 * 向当前区间添加元素x
 */
void add(int x) {
    if (count[x] == 0) {
        currentDistinct++;
    }
    count[x]++;
}

/**
 * 从当前区间移除元素x
 */
void remove(int x) {
    count[x]--;
    if (count[x] == 0) {
        currentDistinct--;
    }
}

/**
 * 运行莫队算法处理所有查询
 */
void solve(vector<Query>& queries) {
    // 初始化答案数组
    answers.resize(q);
    
    // 设置块大小，一般取√n
    blockSize = static_cast<int>(sqrt(n));
    
    // 找出数组中的最大值，用于确定count数组大小
    int maxValue = 0;
    for (int num : a) {
        maxValue = max(maxValue, num);
    }
    
    // 初始化计数数组
    count.assign(maxValue + 1, 0);
    currentDistinct = 0;
    
    // 对查询进行排序
    sort(queries.begin(), queries.end());
    
    // 初始指针位置
    int currentL = 0, currentR = -1;
    
    // 处理每个查询
    for (const Query& query : queries) {
        // 调整左右指针，移动到目标区间
        while (currentL > query.l) {
            currentL--;
            add(a[currentL]);
        }
        while (currentR < query.r) {
            currentR++;
            add(a[currentR]);
        }
        while (currentL < query.l) {
            remove(a[currentL]);
            currentL++;
        }
        while (currentR > query.r) {
            remove(a[currentR]);
            currentR--;
        }
        
        // 保存当前查询的答案
        answers[query.idx] = currentDistinct;
    }
}

/**
 * 使用哈希表的版本，处理元素值较大的情况
 */
void solveWithHashMap(vector<Query>& queries) {
    // 初始化答案数组
    answers.resize(q);
    
    // 设置块大小
    blockSize = static_cast<int>(sqrt(n));
    
    // 使用哈希表代替数组计数
    unordered_map<int, int> hashCount;
    currentDistinct = 0;
    
    // 对查询进行排序
    sort(queries.begin(), queries.end());
    
    // 初始指针位置
    int currentL = 0, currentR = -1;
    
    // 处理每个查询
    for (const Query& query : queries) {
        // 调整左右指针，移动到目标区间
        while (currentL > query.l) {
            currentL--;
            int x = a[currentL];
            if (hashCount[x] == 0) {
                currentDistinct++;
            }
            hashCount[x]++;
        }
        while (currentR < query.r) {
            currentR++;
            int x = a[currentR];
            if (hashCount[x] == 0) {
                currentDistinct++;
            }
            hashCount[x]++;
        }
        while (currentL < query.l) {
            int x = a[currentL];
            hashCount[x]--;
            if (hashCount[x] == 0) {
                currentDistinct--;
            }
            currentL++;
        }
        while (currentR > query.r) {
            int x = a[currentR];
            hashCount[x]--;
            if (hashCount[x] == 0) {
                currentDistinct--;
            }
            currentR--;
        }
        
        // 保存当前查询的答案
        answers[query.idx] = currentDistinct;
    }
}

/**
 * 标准测试函数
 */
void runTest() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取输入
    cin >> n >> q;
    
    a.resize(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    vector<Query> queries;
    queries.reserve(q);
    for (int i = 0; i < q; i++) {
        int l, r;
        cin >> l >> r;
        l--; // 注意题目输入可能是1-based，这里转为0-based
        r--;
        queries.emplace_back(l, r, i);
    }
    
    // 自动选择适合的解法
    int maxValue = 0;
    for (int num : a) {
        maxValue = max(maxValue, num);
    }
    
    if (maxValue <= 1e6) { // 如果最大值不是特别大，使用数组版本
        solve(queries);
    } else { // 否则使用哈希表版本
        solveWithHashMap(queries);
    }
    
    // 输出答案，按照原始查询顺序
    for (int ans : answers) {
        cout << ans << '\n';
    }
}

/**
 * 正确性测试
 */
void correctnessTest() {
    cout << "=== 正确性测试 ===\n";
    
    // 测试用例1
    n = 5;
    a = {1, 2, 3, 2, 1};
    q = 3;
    vector<Query> queries;
    queries.emplace_back(0, 4, 0); // [1,2,3,2,1] 不同元素个数：3
    queries.emplace_back(1, 3, 1); // [2,3,2] 不同元素个数：2
    queries.emplace_back(0, 0, 2); // [1] 不同元素个数：1
    
    solve(queries);
    
    cout << "测试用例1结果：\n";
    for (int i = 0; i < q; i++) {
        cout << "查询 " << (i+1) << ": " << answers[i] << '\n';
    }
    
    // 测试用例2
    n = 10;
    a = {1, 1, 1, 2, 2, 3, 3, 3, 3, 4};
    q = 4;
    queries.clear();
    queries.emplace_back(0, 9, 0); // 全部元素 不同元素个数：4
    queries.emplace_back(0, 2, 1); // 前三个1 不同元素个数：1
    queries.emplace_back(3, 8, 2); // [2,2,3,3,3,3] 不同元素个数：2
    queries.emplace_back(5, 9, 3); // [3,3,3,3,4] 不同元素个数：2
    
    solve(queries);
    
    cout << "\n测试用例2结果：\n";
    for (int i = 0; i < q; i++) {
        cout << "查询 " << (i+1) << ": " << answers[i] << '\n';
    }
    
    // 测试哈希表版本
    cout << "\n哈希表版本测试：\n";
    n = 5;
    a = {1000000000, 2000000000, 3000000000, 2000000000, 1000000000};
    q = 2;
    queries.clear();
    queries.emplace_back(0, 4, 0); // 不同元素个数：3
    queries.emplace_back(1, 3, 1); // 不同元素个数：2
    
    solveWithHashMap(queries);
    cout << "查询1: " << answers[0] << '\n';
    cout << "查询2: " << answers[1] << '\n';
}

/**
 * 性能测试
 */
void performanceTest() {
    cout << "=== 性能测试 ===\n";
    
    // 测试不同规模的数据
    vector<pair<int, int>> testCases = {
        {1000, 1000},
        {10000, 10000},
        {100000, 100000}
    };
    
    for (const auto& testCase : testCases) {
        int size = testCase.first;
        int qCount = testCase.second;
        
        // 生成随机数据
        n = size;
        a.resize(n);
        mt19937 rng(42); // 固定种子，保证可复现性
        uniform_int_distribution<int> dist(1, size);
        for (int& num : a) {
            num = dist(rng);
        }
        
        // 生成随机查询
        q = qCount;
        vector<Query> queries;
        queries.reserve(q);
        for (int i = 0; i < q; i++) {
            int l = uniform_int_distribution<int>(0, n-1)(rng);
            int r = uniform_int_distribution<int>(l, n-1)(rng);
            queries.emplace_back(l, r, i);
        }
        
        // 测量运行时间
        auto startTime = chrono::high_resolution_clock::now();
        solve(queries);
        auto endTime = chrono::high_resolution_clock::now();
        
        chrono::duration<double, milli> elapsed = endTime - startTime;
        cout << "数组大小: " << size << ", 查询次数: " << qCount 
             << ", 耗时: " << elapsed.count() << " ms\n";
    }
}

/**
 * 边界测试
 */
void boundaryTest() {
    cout << "=== 边界测试 ===\n";
    
    // 测试1：所有元素相同
    n = 1000;
    a.resize(n);
    fill(a.begin(), a.end(), 42);
    q = 3;
    vector<Query> queries;
    queries.emplace_back(0, 999, 0); // 不同元素个数：1
    queries.emplace_back(0, 0, 1);   // 不同元素个数：1
    queries.emplace_back(500, 999, 2); // 不同元素个数：1
    
    solve(queries);
    cout << "所有元素相同测试结果：\n";
    for (int ans : answers) {
        cout << ans << '\n';
    }
    
    // 测试2：所有元素不同
    n = 1000;
    a.resize(n);
    for (int i = 0; i < n; i++) {
        a[i] = i + 1;
    }
    q = 3;
    queries.clear();
    queries.emplace_back(0, 999, 0); // 不同元素个数：1000
    queries.emplace_back(0, 499, 1); // 不同元素个数：500
    queries.emplace_back(500, 500, 2); // 不同元素个数：1
    
    solve(queries);
    cout << "\n所有元素不同测试结果：\n";
    for (int ans : answers) {
        cout << ans << '\n';
    }
}

/**
 * 莫队算法块大小优化分析
 */
void blockSizeAnalysis() {
    cout << "=== 块大小优化分析 ===\n";
    
    n = 100000;
    a.resize(n);
    mt19937 rng(42);
    uniform_int_distribution<int> dist(1, n);
    for (int& num : a) {
        num = dist(rng);
    }
    
    q = 100000;
    vector<Query> queries;
    queries.reserve(q);
    for (int i = 0; i < q; i++) {
        int l = uniform_int_distribution<int>(0, n-1)(rng);
        int r = uniform_int_distribution<int>(l, n-1)(rng);
        queries.emplace_back(l, r, i);
    }
    
    vector<int> blockSizes = {
        static_cast<int>(sqrt(n))/2,
        static_cast<int>(sqrt(n)),
        static_cast<int>(sqrt(n))*2,
        n/100,
        n/10
    };
    
    for (int bs : blockSizes) {
        // 保存原始块大小
        int originalBlockSize = blockSize;
        
        // 设置测试块大小
        blockSize = bs;
        
        // 重置计数和答案
        int maxValue = *max_element(a.begin(), a.end());
        count.assign(maxValue + 1, 0);
        currentDistinct = 0;
        answers.assign(q, 0);
        
        // 排序查询
        vector<Query> tempQueries = queries;
        sort(tempQueries.begin(), tempQueries.end());
        
        // 处理查询
        auto startTime = chrono::high_resolution_clock::now();
        int currentL = 0, currentR = -1;
        for (const Query& query : tempQueries) {
            while (currentL > query.l) { currentL--; add(a[currentL]); }
            while (currentR < query.r) { currentR++; add(a[currentR]); }
            while (currentL < query.l) { remove(a[currentL]); currentL++; }
            while (currentR > query.r) { remove(a[currentR]); currentR--; }
            answers[query.idx] = currentDistinct;
        }
        auto endTime = chrono::high_resolution_clock::now();
        
        chrono::duration<double, milli> elapsed = endTime - startTime;
        cout << "块大小: " << bs << ", 耗时: " << elapsed.count() << " ms\n";
        
        // 恢复原始块大小
        blockSize = originalBlockSize;
    }
}

/**
 * 主函数
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cout << "AtCoder ABC174 F Range Set Query 解决方案\n";
    cout << "1. 运行标准测试（按题目输入格式）\n";
    cout << "2. 运行正确性测试\n";
    cout << "3. 运行性能测试\n";
    cout << "4. 运行边界测试\n";
    cout << "5. 运行块大小优化分析\n";
    
    cout << "请选择测试类型：";
    int choice;
    cin >> choice;
    
    switch (choice) {
        case 1:
            runTest();
            break;
        case 2:
            correctnessTest();
            break;
        case 3:
            performanceTest();
            break;
        case 4:
            boundaryTest();
            break;
        case 5:
            blockSizeAnalysis();
            break;
        default:
            cout << "无效选择，运行正确性测试\n";
            correctnessTest();
            break;
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 * 2. 提供了两个版本的实现：
 *    - 使用数组计数：适用于元素值较小的情况
 *    - 使用unordered_map计数：适用于元素值较大的情况
 * 3. 使用emplace_back()代替push_back()，避免不必要的复制
 * 4. 使用vector::reserve()预分配空间，减少动态扩容
 * 5. 使用引用传递参数，避免不必要的复制
 * 6. 使用mt19937随机数生成器，保证随机数的质量和性能
 * 7. 使用chrono库进行高精度时间测量
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度：O(q log q)
 * - 处理查询的时间复杂度：
 *   - 对于块外的移动：每个查询最多移动O(√n)次块，每次块移动最多需要O(√n)次元素操作
 *   - 对于块内的移动：同一块内的右端点排序后，总共移动O(n)次
 *   - 总体时间复杂度：O((n + q)√n)
 *   - 在本题中，由于元素操作是O(1)的，所以时间复杂度为O(n√n)
 * 
 * 空间复杂度分析：
 * - 存储原始数组和查询：O(n + q)
 * - 存储计数数据结构：O(maxValue)或O(distinctValues)
 * - 总体空间复杂度：O(n + q + maxValue)或O(n + q + distinctValues)
 * 
 * C++优化技巧：
 * 1. 内存管理：合理使用reserve()和emplace_back()减少内存分配和复制
 * 2. 数据结构选择：根据元素范围选择合适的计数数据结构
 * 3. 排序优化：使用奇偶优化减少缓存未命中
 * 4. 输入输出优化：关闭同步和绑定加速IO
 * 5. 并行性考虑：对于大规模数据，可以考虑使用OpenMP进行并行处理
 * 
 * 最优解分析：
 * 对于这个问题，莫队算法是离线查询的最优解法之一
 * 对于强制在线查询，可能需要使用线段树或平衡树等数据结构
 * C++版本相比Java和Python版本有更高的性能，特别是在处理大规模数据时
 * 双版本实现（数组+哈希表）保证了算法的适用性和效率
 */