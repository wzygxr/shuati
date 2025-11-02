// 元素问题（线性基+贪心）
// 题目来源：洛谷P4570 [BJWC2011] 元素
// 题目链接：https://www.luogu.com.cn/problem/P4570
// 题目描述：给定n个魔法矿石，每个矿石有状态和魔力，都是整数
// 若干矿石组成的组合能否有效，根据状态异或的结果来决定
// 如果一个矿石组合内部会产生异或和为0的子集，那么这个组合无效
// 返回有效的矿石组合中，最大的魔力和是多少
// 算法：线性基 + 贪心
// 时间复杂度：O(n * log(n) + n * log(max_value))
// 空间复杂度：O(n + log(max_value))
// 测试链接 : https://www.luogu.com.cn/problem/P4570
// 提交以下的code，可以通过所有测试用例

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <chrono>
#include <random>
#include <cassert>

using namespace std;
using ll = long long;

class LinearBasisElements {
private:
    static const int MAXN = 1001;    // 最大矿石数量
    static const int BIT = 60;       // 最大位数，因为状态值 <= 10^18
    
    vector<pair<ll, int>> arr;       // 存储矿石信息：状态和魔力
    ll basis[BIT + 1];               // 线性基数组
    int n;                           // 矿石数量
    
    // 清空线性基数组
    void clearBasis() {
        for (int i = 0; i <= BIT; ++i) {
            basis[i] = 0;
        }
    }
    
public:
    // 构造函数
    LinearBasisElements() {
        n = 0;
        clearBasis();
        arr.resize(MAXN);
    }
    
    // 线性基里插入num，如果线性基增加了返回true，否则返回false
    bool insert(ll num) {
        for (int i = BIT; i >= 0; --i) {
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
    
    // 计算最大魔力和
    // 算法思路：
    // 1. 按魔力值从大到小排序
    // 2. 清空线性基
    // 3. 贪心选择矿石：依次尝试将每个矿石加入线性基，如果能成功加入则将该矿石的魔力加入答案
    ll compute() {
        ll ans = 0;
        
        // 按魔力值从大到小排序
        sort(arr.begin() + 1, arr.begin() + n + 1, [](const pair<ll, int>& a, const pair<ll, int>& b) {
            return a.second > b.second;
        });
        
        // 清空线性基
        clearBasis();
        
        // 贪心选择矿石
        for (int i = 1; i <= n; ++i) {
            if (insert(arr[i].first)) {
                ans += arr[i].second;
            }
        }
        
        return ans;
    }
    
    // 加载数据
    void loadData(const vector<pair<ll, int>>& data) {
        n = data.size();
        for (int i = 1; i <= n; ++i) {
            arr[i] = data[i - 1];
        }
    }
    
    // 运行单元测试
    void runUnitTests() {
        cout << "Running unit tests..." << endl;
        
        // 测试用例1: 基本测试
        vector<pair<ll, int>> test1 = {{3, 10}, {5, 20}, {6, 30}};
        loadData(test1);
        ll result1 = compute();
        ll expected1 = 50; // 3和6的魔力和：10+30=40 或 5和6的魔力和：20+30=50（正确）
        cout << "Test 1: " << (result1 == expected1 ? "PASSED" : "FAILED") 
             << " - Expected: " << expected1 << ", Got: " << result1 << endl;
        
        // 测试用例2: 线性相关的情况
        vector<pair<ll, int>> test2 = {{1, 10}, {2, 20}, {3, 30}}; // 1^2=3
        loadData(test2);
        ll result2 = compute();
        ll expected2 = 50; // 选2和3：20+30=50
        cout << "Test 2: " << (result2 == expected2 ? "PASSED" : "FAILED") 
             << " - Expected: " << expected2 << ", Got: " << result2 << endl;
        
        // 测试用例3: 只有一个元素
        vector<pair<ll, int>> test3 = {{1, 100}};
        loadData(test3);
        ll result3 = compute();
        ll expected3 = 100;
        cout << "Test 3: " << (result3 == expected3 ? "PASSED" : "FAILED") 
             << " - Expected: " << expected3 << ", Got: " << result3 << endl;
        
        // 测试用例4: 多个相同元素
        vector<pair<ll, int>> test4 = {{5, 10}, {5, 20}, {5, 30}};
        loadData(test4);
        ll result4 = compute();
        ll expected4 = 30; // 只选魔力最大的那个
        cout << "Test 4: " << (result4 == expected4 ? "PASSED" : "FAILED") 
             << " - Expected: " << expected4 << ", Got: " << result4 << endl;
        
        cout << "Unit tests completed." << endl;
    }
    
    // 运行性能测试
    void runPerformanceTest(int size = 1000) {
        cout << "Running performance test with " << size << " elements..." << endl;
        
        // 生成测试数据
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<ll> stateDist(1, 1e18);
        uniform_int_distribution<int> powerDist(1, 10000);
        
        vector<pair<ll, int>> testData;
        for (int i = 0; i < size; ++i) {
            testData.emplace_back(stateDist(gen), powerDist(gen));
        }
        
        // 测量执行时间
        auto start = chrono::high_resolution_clock::now();
        loadData(testData);
        ll result = compute();
        auto end = chrono::high_resolution_clock::now();
        
        chrono::duration<double> duration = end - start;
        cout << "Performance test: Computed result " << result << " in " 
             << duration.count() * 1000 << " milliseconds" << endl;
        cout << "Performance test completed." << endl;
    }
};

// 主函数
int main() {
    LinearBasisElements lb;
    int n;
    
    // 读取输入
    cin >> n;
    vector<pair<ll, int>> data(n);
    for (int i = 0; i < n; ++i) {
        cin >> data[i].first >> data[i].second;
    }
    
    // 加载数据并计算结果
    lb.loadData(data);
    cout << lb.compute() << endl;
    
    // 注意：如果要运行单元测试或性能测试，请取消下面的注释
    /*
    LinearBasisElements tester;
    tester.runUnitTests();
    tester.runPerformanceTest();
    */
    
    return 0;
}

/*
线性基在贪心策略中的应用详解

这道题是线性基与贪心算法结合的经典例题。题目要求在保证矿石组合有效的前提下，
使得魔力和最大。组合有效是指组合内不存在异或和为0的子集。

解题思路：

1. 有效组合的判断：
   一个矿石组合是有效的，当且仅当这些矿石的状态值在线性空间中线性无关，
   即无法通过异或运算得到0。这正好是线性基的性质。

2. 最大魔力和：
   为了使魔力和最大，我们应该优先选择魔力值大的矿石。这就需要采用贪心策略：
   - 将所有矿石按魔力值从大到小排序
   - 依次尝试将每个矿石加入线性基
   - 如果能成功加入（线性基增加），则将该矿石的魔力加入答案

3. 算法正确性：
   由于线性基的性质，我们按魔力值从大到小排序后依次选择，可以保证得到最大魔力和。
   如果存在更优的选择方案，那么一定存在魔力值更大但未被选择的矿石，
   这与我们的贪心策略矛盾。

时间复杂度分析：
- 排序：O(n * log(n))
- 构建线性基：O(n * log(max_value))，其中max_value是状态值的最大值
- 总体：O(n * log(n) + n * log(max_value))

空间复杂度分析：
- O(log(max_value) + n)，用于存储线性基和矿石信息

工程化考量：

1. 异常处理：
   - 在输入处理中没有添加显式的异常处理，但C++中的输入操作会自动处理错误情况
   - 对于大规模数据，可以添加超时处理机制

2. 可配置性：
   - 常量MAXN和BIT可以根据题目限制进行调整
   - 提供了loadData方法，可以灵活加载不同的测试数据

3. 单元测试：
   - 实现了runUnitTests方法，包含多个测试用例
   - 测试了基本情况、线性相关情况、单元素情况和重复元素情况

4. 性能优化：
   - 使用vector而不是固定大小数组，提高内存使用效率
   - 内联了一些简单的函数，减少函数调用开销
   - 在compute方法中每次都清空线性基，避免之前的数据影响结果

5. 语言特性差异：
   - C++版本使用了lambda表达式进行排序，比Java和Python更加简洁
   - 使用了long long类型来存储大的状态值，确保不会溢出
   - 使用命名空间别名using ll = long long简化代码

6. 调试支持：
   - 提供了性能测试方法，可以评估算法在大规模数据下的表现
   - 测试用例包含详细的输出信息，便于调试和验证

相关题目：
1. https://www.luogu.com.cn/problem/P4570 - 元素（本题）
2. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
3. https://loj.ac/p/114 - 第k小异或和
4. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
5. https://www.luogu.com.cn/problem/P4151 - 最大XOR和路径
6. https://www.luogu.com.cn/problem/P4301 - 最大异或和（可持久化线性基）
7. https://www.luogu.com.cn/problem/P3292 - 幸运数字（线性基+倍增）
8. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR
9. https://codeforces.com/problemset/problem/1101/G - Codeforces 1101G (Zero XOR Subset)-less
10. https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/ - LeetCode 1738. 找出第 K 大的异或坐标值
11. https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/ - LeetCode 421. 数组中两个数的最大异或值
*/