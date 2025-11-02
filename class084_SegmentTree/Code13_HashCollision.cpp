// 哈希冲突问题 - 分块算法实现 (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/P3396
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
// 2. 更新操作 C x y: 将arr[x]的值更新为y
// 约束条件: 1 <= n、m <= 1.5 * 10^5
// 
// 解题思路:
// 1. 对于x <= sqrt(n)的情况，预处理dp[x][y]的值
// 2. 对于x > sqrt(n)的情况，直接暴力计算
// 3. 更新操作时，同时更新预处理结果
// 
// 时间复杂度分析:
// - 预处理: O(n * sqrt(n))
// - 查询: O(1) 对于x <= sqrt(n)，O(n/x) 对于x > sqrt(n)
// - 更新: O(sqrt(n))
// 
// 空间复杂度: O(n + sqrt(n)^2) = O(n)
// 
// 工程化考量:
// 1. 异常处理: 验证输入参数的有效性
// 2. 性能优化: 使用分块思想平衡预处理和查询的开销
// 3. 边界处理: 处理x=0或y>=x等边界情况
// 4. 内存管理: 合理设置数组大小避免内存溢出

#include <iostream>
#include <vector>
#include <cmath>
#include <stdexcept>

using namespace std;

class HashCollision {
private:
    // 定义最大数组长度和块大小
    static const int MAXN = 150001;
    static const int MAXB = 401;
    
    // n: 数组长度, m: 操作次数, blen: 块大小
    int n, blen;
    
    // arr: 原始数组
    vector<int> arr;
    
    // dp[x][y]: 存储所有满足 i % x == y 的位置i对应的arr[i]之和 (预处理结果)
    // 只对 x <= sqrt(n) 的情况进行预处理，以节省空间和时间
    vector<vector<long long>> dp;

public:
    /**
     * 构造函数
     * @param size 数组大小
     */
    HashCollision(int size) : n(size), arr(size + 1, 0) {
        // 计算块大小，通常选择sqrt(n)
        blen = (int)sqrt(n);
        
        // 初始化dp数组
        dp.resize(blen + 1, vector<long long>(blen + 1, 0));
    }
    
    /**
     * 设置数组初始值
     * @param values 初始值数组
     */
    void setArray(const vector<int>& values) {
        if (values.size() != n) {
            throw invalid_argument("初始值数组大小不匹配");
        }
        
        for (int i = 1; i <= n; i++) {
            arr[i] = values[i - 1];
        }
        
        // 进行预处理
        prepare();
    }
    
    /**
     * 查询操作 A x y
     * 查询所有满足 i % x == y 的位置i对应的arr[i]之和
     * @param x 除数，必须大于0
     * @param y 余数，必须满足 0 <= y < x
     * @return 满足条件的位置对应的元素之和
     * @throws invalid_argument 如果x <= 0 或 y < 0 或 y >= x
     */
    long long query(int x, int y) {
        // 参数验证
        if (x <= 0) {
            throw invalid_argument("除数x必须大于0");
        }
        if (y < 0 || y >= x) {
            throw invalid_argument("余数y必须满足0 <= y < x");
        }
        
        // 如果x小于等于块大小，则直接返回预处理结果
        if (x <= blen) {
            return dp[x][y];
        }
        
        // 否则暴力计算（适用于x较大的情况）
        long long ans = 0;
        for (int i = y; i <= n; i += x) {
            ans += arr[i];
        }
        return ans;
    }
    
    /**
     * 更新操作 C x y
     * 将arr[x]的值更新为y，并更新相关的预处理结果
     * @param i 要更新的位置，必须满足1 <= i <= n
     * @param v 新的值
     * @throws invalid_argument 如果位置i超出有效范围
     */
    void update(int i, int v) {
        // 参数验证
        if (i < 1 || i > n) {
            throw invalid_argument("位置i必须在1到n之间");
        }
        
        // 计算值的变化量
        int delta = v - arr[i];
        // 更新原数组
        arr[i] = v;
        
        // 更新所有相关的预处理结果
        // 只需要更新x <= sqrt(n)的情况，因为这些被预处理了
        for (int x = 1; x <= blen; x++) {
            dp[x][i % x] += delta;
        }
    }
    
    /**
     * 预处理函数
     * 对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
     */
    void prepare() {
        // 初始化dp数组为0
        for (int x = 1; x <= blen; x++) {
            for (int y = 0; y < x; y++) {
                dp[x][y] = 0;
            }
        }
        
        // 对于每个x <= sqrt(n)，计算所有y对应的dp[x][y]值
        for (int x = 1; x <= blen; x++) {
            for (int i = 1; i <= n; i++) {
                // i % x 表示位置i对x取余的结果
                // dp[x][i % x] 累加arr[i]的值
                dp[x][i % x] += arr[i];
            }
        }
    }
    
    /**
     * 获取数组当前状态
     * @return 数组内容字符串
     */
    string getStatus() const {
        string result = "数组状态: [";
        for (int i = 1; i <= min(n, 10); i++) {
            result += to_string(arr[i]);
            if (i < min(n, 10)) result += ", ";
        }
        if (n > 10) result += "...";
        result += "]";
        return result;
    }
    
    /**
     * 获取预处理状态
     * @return 预处理状态字符串
     */
    string getPreprocessStatus() const {
        string result = "预处理状态 (x <= " + to_string(blen) + "):\n";
        for (int x = 1; x <= min(blen, 5); x++) {
            result += "x=" + to_string(x) + ": [";
            for (int y = 0; y < x; y++) {
                result += to_string(dp[x][y]);
                if (y < x - 1) result += ", ";
            }
            result += "]\n";
        }
        if (blen > 5) result += "...\n";
        return result;
    }
};

/**
 * 单元测试函数
 */
void testHashCollision() {
    cout << "=== 哈希冲突算法单元测试 ===" << endl;
    
    // 测试1: 基本功能测试
    cout << "测试1: 基本功能测试" << endl;
    HashCollision hc(10);
    
    // 设置初始数组
    vector<int> values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    hc.setArray(values);
    
    // 验证查询结果
    long long result1 = hc.query(3, 0); // 3 + 6 + 9 = 18
    long long result2 = hc.query(3, 1); // 1 + 4 + 7 + 10 = 22
    long long result3 = hc.query(3, 2); // 2 + 5 + 8 = 15
    
    if (result1 == 18 && result2 == 22 && result3 == 15) {
        cout << "查询测试通过" << endl;
    } else {
        cout << "查询测试失败" << endl;
    }
    
    // 验证更新结果
    hc.update(5, 50); // 将位置5的值从5改为50
    long long result4 = hc.query(3, 1); // 1 + 4 + 7 + 10 + (50 - 5) = 22 + 45 = 67
    
    if (result4 == 67) {
        cout << "更新测试通过" << endl;
    } else {
        cout << "更新测试失败" << endl;
    }
    
    // 测试2: 异常处理测试
    cout << "\n测试2: 异常处理测试" << endl;
    try {
        hc.query(0, 0); // 应该抛出异常
        cout << "异常处理测试失败" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常处理测试通过: " << e.what() << endl;
    }
    
    cout << hc.getStatus() << endl;
    cout << hc.getPreprocessStatus() << endl;
    
    cout << "=== 单元测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 哈希冲突算法性能测试 ===" << endl;
    
    int n = 100000;
    int m = 10000;
    
    HashCollision hc(n);
    
    // 初始化数组
    vector<int> values(n);
    for (int i = 0; i < n; i++) {
        values[i] = i + 1;
    }
    hc.setArray(values);
    
    // 测试查询性能
    clock_t start = clock();
    
    long long total = 0;
    for (int i = 0; i < m; i++) {
        int x = (i % 100) + 1; // x在1-100之间
        int y = i % x;
        total += hc.query(x, y);
    }
    
    clock_t end = clock();
    double duration = (double)(end - start) / CLOCKS_PER_SEC * 1000;
    
    cout << "性能测试结果:" << endl;
    cout << "数据规模: n=" << n << ", 操作次数: m=" << m << endl;
    cout << "总查询时间: " << duration << " 毫秒" << endl;
    cout << "平均查询时间: " << duration / m << " 毫秒/次" << endl;
    cout << "查询吞吐量: " << (double)m / (duration / 1000.0) << " 次/秒" << endl;
    cout << "查询结果总和: " << total << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

int main() {
    // 运行单元测试
    testHashCollision();
    
    // 运行性能测试
    performanceTest();
    
    // 演示示例
    cout << "=== 哈希冲突算法演示 ===" << endl;
    
    HashCollision demo(20);
    vector<int> demoValues(20);
    for (int i = 0; i < 20; i++) {
        demoValues[i] = (i + 1) * 10; // 10, 20, 30, ..., 200
    }
    demo.setArray(demoValues);
    
    cout << demo.getStatus() << endl;
    
    // 演示查询操作
    cout << "\n查询演示:" << endl;
    cout << "查询所有位置为3的倍数的元素之和 (x=3, y=0): " << demo.query(3, 0) << endl;
    cout << "查询所有位置除以4余1的元素之和 (x=4, y=1): " << demo.query(4, 1) << endl;
    cout << "查询所有位置除以5余2的元素之和 (x=5, y=2): " << demo.query(5, 2) << endl;
    
    // 演示更新操作
    cout << "\n更新演示:" << endl;
    cout << "更新前位置5的值: " << demo.query(1, 4) << endl; // 查询单个位置
    demo.update(5, 999);
    cout << "更新后位置5的值: " << demo.query(1, 4) << endl;
    cout << "更新后所有位置为3的倍数的元素之和: " << demo.query(3, 0) << endl;
    
    return 0;
}