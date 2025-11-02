// 彩灯问题（线性基应用）
// 题目来源：洛谷P3857 [TJOI2008] 彩灯
// 题目链接：https://www.luogu.com.cn/problem/P3857
// 题目描述：一共有n个灯泡，开始都是不亮的状态，有m个开关
// 每个开关能改变若干灯泡的状态，改变是指，亮变不亮、不亮变亮
// 比如n=5，某个开关为XXOOO，表示这个开关只能改变后3个灯泡的状态
// 可以随意使用开关，返回有多少种亮灯的组合，全不亮也算一种组合
// 答案可能很大，对 2008 取模
// 算法：线性基
// 时间复杂度：O(m * n)
// 空间复杂度：O(n)
// 测试链接 : https://www.luogu.com.cn/problem/P3857
// 提交以下的code，可以通过所有测试用例

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>
#include <cassert>

using namespace std;
using ll = long long;

class LinearBasisLanterns {
private:
    static const int MAXN = 51;    // 最大灯泡数量
    static const int MOD = 2008;   // 模数
    
    ll basis[MAXN];                // 线性基数组
    ll arr[MAXN];                  // 存储每个开关的影响
    int n, m;                      // 灯泡数量和开关数量
    
    // 清空线性基数组
    void clearBasis() {
        for (int i = 0; i < MAXN; ++i) {
            basis[i] = 0;
        }
    }
    
public:
    // 构造函数
    LinearBasisLanterns() {
        n = m = 0;
        clearBasis();
        for (int i = 0; i < MAXN; ++i) {
            arr[i] = 0;
        }
    }
    
    // 线性基里插入num，如果线性基增加了返回true，否则返回false
    bool insert(ll num) {
        for (int i = n; i >= 0; --i) {
            if ((num >> i) & 1) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }
    
    // 计算线性基的大小
    // 算法思路：
    // 1. 清空线性基
    // 2. 将所有开关模式插入线性基
    // 3. 返回线性基的大小
    int compute() {
        int size = 0;
        clearBasis();  // 每次计算前清空线性基
        
        for (int i = 1; i <= m; ++i) {
            if (insert(arr[i])) {
                size++;
            }
        }
        
        return size;
    }
    
    // 计算结果
    // 算法思路：
    // 1. 计算线性基的大小
    // 2. 答案就是2^(线性基大小) % MOD
    ll calculateResult() {
        int size = compute();
        // 计算2^size % MOD
        ll result = 1;
        for (int i = 0; i < size; ++i) {
            result = (result * 2) % MOD;
        }
        return result;
    }
    
    // 加载数据
    void loadData(int n_lights, int n_switches, const vector<string>& switch_patterns) {
        n = n_lights - 1;  // 注意这里的转换，因为从0开始索引
        m = n_switches;
        
        // 将每个开关模式转换为二进制数
        for (int i = 1; i <= m; ++i) {
            const string& s = switch_patterns[i - 1];
            ll num = 0;
            for (int j = 0; j <= n; ++j) {
                if (s[j] == 'O') {
                    num |= 1LL << j;
                }
            }
            arr[i] = num;
        }
    }
    
    // 运行单元测试
    void runUnitTests() {
        cout << "Running unit tests..." << endl;
        
        // 测试用例1: 基本测试
        {   
            // 3个灯泡，2个开关：一个控制灯1和灯2，一个控制灯2和灯3
            vector<string> patterns = {"OXX", "XXO"};
            loadData(3, 2, patterns);
            ll result = calculateResult();
            ll expected = 4;  // 2^2 = 4种组合
            cout << "Test 1: " << (result == expected ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected << ", Got: " << result << endl;
        }
        
        // 测试用例2: 线性相关的情况
        {   
            // 3个灯泡，3个开关，其中第3个开关是前两个的线性组合（异或）
            vector<string> patterns = {"OXX", "XXO", "OXX"};  // 第三个开关与第一个相同
            loadData(3, 3, patterns);
            ll result = calculateResult();
            ll expected = 4;  // 线性基大小仍为2
            cout << "Test 2: " << (result == expected ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected << ", Got: " << result << endl;
        }
        
        // 测试用例3: 没有开关
        {   
            vector<string> patterns = {};
            loadData(3, 0, patterns);
            ll result = calculateResult();
            ll expected = 1;  // 只有一种状态：全不亮
            cout << "Test 3: " << (result == expected ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected << ", Got: " << result << endl;
        }
        
        // 测试用例4: 一个开关
        {   
            vector<string> patterns = {"OOO"};
            loadData(3, 1, patterns);
            ll result = calculateResult();
            ll expected = 2;  // 2种状态：全亮或全不亮
            cout << "Test 4: " << (result == expected ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected << ", Got: " << result << endl;
        }
        
        cout << "Unit tests completed." << endl;
    }
    
    // 运行性能测试
    void runPerformanceTest(int n_lights = 50, int n_switches = 50) {
        cout << "Running performance test with " << n_lights << " lights and " 
             << n_switches << " switches..." << endl;
        
        // 生成随机开关模式
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<char> switchDist(0, 1);
        
        vector<string> patterns;
        for (int i = 0; i < n_switches; ++i) {
            string s;
            for (int j = 0; j < n_lights; ++j) {
                s += (switchDist(gen) ? 'O' : 'X');
            }
            patterns.push_back(s);
        }
        
        // 测量执行时间
        auto start = chrono::high_resolution_clock::now();
        loadData(n_lights, n_switches, patterns);
        ll result = calculateResult();
        auto end = chrono::high_resolution_clock::now();
        
        chrono::duration<double> duration = end - start;
        cout << "Performance test: Computed result " << result << " in " 
             << duration.count() * 1000 << " milliseconds" << endl;
        cout << "Performance test completed." << endl;
    }
};

// 主函数
int main() {
    LinearBasisLanterns lb;
    int n, m;
    
    // 读取输入
    cin >> n >> m;
    vector<string> switch_patterns(m);
    for (int i = 0; i < m; ++i) {
        cin >> switch_patterns[i];
    }
    
    // 加载数据并计算结果
    lb.loadData(n, m, switch_patterns);
    cout << lb.calculateResult() << endl;
    
    // 注意：如果要运行单元测试或性能测试，请取消下面的注释
    /*
    LinearBasisLanterns tester;
    tester.runUnitTests();
    tester.runPerformanceTest();
    */
    
    return 0;
}

/*
线性基在线性空间中的应用详解

这道题展示了线性基在解决线性空间问题中的应用。题目要求计算通过开关操作能产生多少种不同的亮灯组合。

解题思路：

1. 问题建模：
   将每个开关对灯泡的影响看作一个二进制向量，第i位为1表示能控制第i个灯泡。
   所有开关构成一个向量集合，我们要求这个集合能张成的线性空间的维度。

2. 线性基的含义：
   线性基的大小就是这个向量集合的秩，也就是线性空间的维度。
   在模2的线性空间中，每个维度都有"选"或"不选"两种状态。
   因此，不同的组合数就是2^(线性基大小)。

3. 算法实现：
   - 将每个开关的影响用一个二进制数表示
   - 用线性基算法计算这些向量的线性无关组的大小
   - 答案就是2^(线性基大小) % MOD

时间复杂度分析：
- 构建线性基：O(m * n)，其中m是开关数量，n是灯泡数量
- 计算答案：O(线性基大小)，最坏情况下是O(min(m, n))
- 总体：O(m * n)

空间复杂度分析：
- O(n)，用于存储线性基

工程化考量：

1. 异常处理：
   - 在输入处理中没有添加显式的异常处理，但C++中的输入操作会自动处理错误情况
   - 在实际应用中，可以添加对输入参数范围的检查

2. 可配置性：
   - 常量MAXN和MOD可以根据题目限制进行调整
   - 提供了loadData方法，可以灵活加载不同的测试数据

3. 单元测试：
   - 实现了runUnitTests方法，包含多个测试用例
   - 测试了基本情况、线性相关情况、边界情况（无开关、单个开关）

4. 性能优化：
   - 使用了位运算，提高计算效率
   - 每次计算前清空线性基，避免之前的数据影响结果
   - 优化了指数计算，避免重复计算

5. 语言特性差异：
   - C++版本使用了long long类型来存储二进制数，确保不会溢出
   - 使用了向量和字符串操作，比Java和Python更加高效
   - 提供了完整的面向对象封装

6. 调试支持：
   - 提供了性能测试方法，可以评估算法在大规模数据下的表现
   - 测试用例包含详细的输出信息，便于调试和验证

相关题目：
1. https://www.luogu.com.cn/problem/P3857 - 彩灯（本题）
2. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
3. https://loj.ac/p/114 - 第k小异或和
4. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
5. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
6. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
7. https://codeforces.com/problemset/problem/1101/G - Codeforces 1101G (Zero XOR Subset)-less
8. https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/ - LeetCode 1738. 找出第 K 大的异或坐标值
9. https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/ - LeetCode 421. 数组中两个数的最大异或值
*/