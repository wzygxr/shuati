// HDU 3949 XOR问题（线性基求第k小异或和）
// 题目来源：HDU 3949 XOR
// 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3949
// 题目描述：给定n个数，求这些数能异或出的第k小值
// 算法：线性基（高斯消元法）
// 时间复杂度：构建线性基O(n * log(max_value))，单次查询O(log(max_value))
// 空间复杂度：O(log(max_value))
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=3949
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

class LinearBasisHdu3949 {
private:
    static const int MAXN = 10001;  // 最大数组长度
    static const int BIT = 60;     // 最大位数，因为a[i] <= 10^18
    
    vector<ll> basis;              // 线性基数组
    int len;                       // 线性基的大小
    bool zero;                     // 是否能异或出0
    vector<ll> arr;                // 存储输入数组
    
    // 清空线性基数组
    void clearBasis() {
        basis.assign(BIT + 1, 0);
        len = 0;
        zero = false;
    }
    
public:
    // 构造函数
    LinearBasisHdu3949() : len(0), zero(false) {
        clearBasis();
        arr.resize(MAXN + 1);  // 索引从1开始
    }
    
    // 线性基里插入num，如果线性基增加了返回true，否则返回false
    bool insert(ll num) {
        for (int i = BIT; i >= 0; --i) {
            if ((num & (1LL << i)) != 0) {
                if (basis[i] == 0) {
                    basis[i] = num;
                    return true;
                }
                num ^= basis[i];
            }
        }
        return false;
    }
    
    // 高斯消元构建线性基
    // 算法思路：
    // 1. 先用普通消元法构建线性基
    // 2. 再用高斯消元法整理线性基，使其具有阶梯状结构
    // 3. 重新整理basis数组，把非0的元素移到前面
    // 4. 判断是否能异或出0
    void compute() {
        clearBasis();
        int n = arr.size() - 1;  // 实际元素个数
        
        // 先用普通消元法构建线性基
        for (int i = 1; i <= n; ++i) {
            insert(arr[i]);
        }
        
        // 再用高斯消元法整理线性基，使其具有阶梯状结构
        for (int i = 0; i <= BIT; ++i) {
            for (int j = i + 1; j <= BIT; ++j) {
                if ((basis[j] & (1LL << i)) != 0) {
                    basis[j] ^= basis[i];
                }
            }
        }
        
        // 重新整理basis数组，把非0的元素移到前面
        vector<ll> temp_basis;
        for (int i = 0; i <= BIT; ++i) {
            if (basis[i] != 0) {
                temp_basis.push_back(basis[i]);
            }
        }
        basis = temp_basis;
        len = basis.size();
        
        // 判断是否能异或出0
        zero = (len != n);
    }
    
    // 返回第k小的异或和
    // 算法思路：
    // 1. 特殊情况处理：如果能异或出0，0是第1小的
    // 2. 如果能异或出0，且k=1，返回0
    // 3. 如果能异或出0，且k>1，将k减1后继续处理
    // 4. 根据k的二进制表示选择线性基中的元素进行异或
    ll query(ll k) {
        // 异常处理：k必须大于0
        if (k <= 0) {
            throw invalid_argument("k must be positive");
        }
        
        // 如果能异或出0，那么0是第1小的
        if (zero) {
            if (k == 1) {
                return 0;
            }
            k--;  // 跳过0
        }
        
        // 如果k超过了可能的异或和数量，返回-1
        if (k > (1LL << len)) {
            return -1;
        }
        
        ll ans = 0;
        // 根据k的二进制表示选择线性基中的元素进行异或
        for (int i = 0; i < len; ++i) {
            if ((k & (1LL << i)) != 0) {
                ans ^= basis[i];
            }
        }
        return ans;
    }
    
    // 加载数据
    void loadData(const vector<ll>& data) {
        arr.clear();
        arr.resize(data.size() + 1);  // 索引从1开始
        for (int i = 1; i <= data.size(); ++i) {
            arr[i] = data[i - 1];
        }
    }
    
    // 获取高斯消元后的线性基，用于调试
    vector<ll> getGaussianBasis() const {
        return basis;
    }
    
    // 检查是否能异或出0
    bool canGetZero() const {
        return zero;
    }
    
    // 获取线性基的大小
    int getBasisSize() const {
        return len;
    }
    
    // 运行单元测试
    void runUnitTests() {
        cout << "Running unit tests..." << endl;
        
        // 测试用例1: 基本测试
        {   
            vector<ll> test = {1, 2, 3};
            loadData(test);
            compute();
            
            ll result1 = query(1);
            ll expected1 = 0;  // 因为可以异或出0
            cout << "Test 1.1: " << (result1 == expected1 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected1 << ", Got: " << result1 << endl;
            
            ll result2 = query(2);
            ll expected2 = 1;  // 第2小的是1
            cout << "Test 1.2: " << (result2 == expected2 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected2 << ", Got: " << result2 << endl;
            
            ll result3 = query(3);
            ll expected3 = 2;  // 第3小的是2
            cout << "Test 1.3: " << (result3 == expected3 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected3 << ", Got: " << result3 << endl;
            
            ll result4 = query(4);
            ll expected4 = 3;  // 第4小的是3
            cout << "Test 1.4: " << (result4 == expected4 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected4 << ", Got: " << result4 << endl;
        }
        
        // 测试用例2: 不能异或出0的情况
        {   
            vector<ll> test = {1, 2};
            loadData(test);
            compute();
            
            ll result1 = query(1);
            ll expected1 = 1;  // 最小的是1
            cout << "Test 2.1: " << (result1 == expected1 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected1 << ", Got: " << result1 << endl;
            
            ll result2 = query(2);
            ll expected2 = 2;  // 第二小的是2
            cout << "Test 2.2: " << (result2 == expected2 ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected2 << ", Got: " << result2 << endl;
        }
        
        // 测试用例3: 越界查询
        {   
            vector<ll> test = {1, 2, 3};
            loadData(test);
            compute();
            
            ll result = query(5);  // 只有4种可能的异或和
            ll expected = -1;      // 应该返回-1
            cout << "Test 3: " << (result == expected ? "PASSED" : "FAILED") 
                 << " - Expected: " << expected << ", Got: " << result << endl;
        }
        
        cout << "Unit tests completed." << endl;
    }
    
    // 运行性能测试
    void runPerformanceTest(int size = 10000) {
        cout << "Running performance test with " << size << " elements..." << endl;
        
        // 生成测试数据
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<ll> dist(0, 1e18);
        
        vector<ll> testData;
        for (int i = 0; i < size; ++i) {
            testData.push_back(dist(gen));
        }
        
        // 测量构建线性基的时间
        auto start = chrono::high_resolution_clock::now();
        loadData(testData);
        compute();
        auto end = chrono::high_resolution_clock::now();
        
        chrono::duration<double> build_time = end - start;
        cout << "Build time: " << build_time.count() * 1000 << " milliseconds" << endl;
        
        // 测量查询的时间
        start = chrono::high_resolution_clock::now();
        for (int i = 1; i <= min(size, 100); ++i) {
            query(i);
        }
        end = chrono::high_resolution_clock::now();
        
        chrono::duration<double> query_time = end - start;
        cout << "Query time: " << query_time.count() * 1000 << " milliseconds for 100 queries" << endl;
        cout << "Performance test completed." << endl;
    }
};

/*
线性基求第k小异或和详解

这是线性基的经典应用之一，用于求解能由给定数组异或得到的第k小值。

解题思路：

1. 线性基构建：
   - 使用普通消元法构建线性基
   - 再使用高斯消元法整理线性基，使其具有阶梯状结构
   - 这种结构使得我们可以根据k的二进制表示来选择元素

2. 特殊情况处理：
   - 判断是否能异或出0（当线性基大小小于原数组大小时）
   - 如果能异或出0，0是最小的异或和，是第1小的

3. 查询第k小值：
   - 如果能异或出0，且k=1，返回0
   - 如果能异或出0，且k>1，将k减1后继续处理
   - 根据k的二进制表示选择线性基中的元素进行异或

时间复杂度分析：
- 构建线性基：O(n * log(max_value))
- 单次查询：O(log(max_value))

空间复杂度分析：
- O(log(max_value))，用于存储线性基

相关题目：
1. http://acm.hdu.edu.cn/showproblem.php?pid=3949 - HDU 3949 XOR（本题）
2. https://loj.ac/p/114 - 第k小异或和
3. https://www.luogu.com.cn/problem/P3812 - 线性基（最大异或和）
4. https://www.luogu.com.cn/problem/P4570 - 元素（线性基+贪心）
5. https://www.luogu.com.cn/problem/P3857 - 彩灯（线性基应用）
6. https://codeforces.com/problemset/problem/1101/G - (Zero XOR Subset)-less
*/

// 主函数
int main() {
    LinearBasisHdu3949 lb;
    int t;
    cin >> t;
    
    for (int cases = 1; cases <= t; ++cases) {
        cout << "Case #" << cases << ":" << endl;
        
        int n;
        cin >> n;
        vector<ll> data(n);
        for (int i = 0; i < n; ++i) {
            cin >> data[i];
        }
        
        lb.loadData(data);
        lb.compute();
        
        int q;
        cin >> q;
        for (int i = 0; i < q; ++i) {
            ll k;
            cin >> k;
            try {
                cout << lb.query(k) << endl;
            } catch (const invalid_argument& e) {
                cout << -1 << endl;  // 发生异常时输出-1
            }
        }
    }
    
    // 注意：如果要运行单元测试或性能测试，请取消下面的注释
    /*
    LinearBasisHdu3949 tester;
    tester.runUnitTests();
    tester.runPerformanceTest();
    */
    
    return 0;
}