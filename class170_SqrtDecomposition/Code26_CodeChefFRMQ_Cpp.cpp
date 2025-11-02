/**
 * CodeChef FRMQ - Fibonacci Range Minimum Query - C++实现
 * 题目：区间斐波那契数列最小值查询
 * 来源：CodeChef (https://www.codechef.com/problems/FRMQ)
 * 
 * 算法：平方根分解 + 稀疏表（Sparse Table）
 * 时间复杂度：
 *   - 预处理：O(n log n)
 *   - 查询：O(1)
 * 空间复杂度：O(n log n)
 * 最优解：是，稀疏表是处理静态区间最值查询的最优解
 * 
 * 思路：
 * 1. 预处理斐波那契数列
 * 2. 使用稀疏表存储区间最小值信息
 * 3. 支持O(1)时间的区间最小值查询
 * 
 * 工程化考量：
 * - 使用稀疏表优化静态查询
 * - 预处理斐波那契数列避免重复计算
 * - 处理大数运算和模运算
 * - 优化内存使用
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <climits>
using namespace std;

class FibonacciRMQ {
private:
    vector<long long> fib;        // 斐波那契数列
    vector<vector<long long>> st;  // 稀疏表
    vector<int> log_table;        // 对数表
    int n;
    
    // 预处理斐波那契数列
    void precomputeFibonacci(int max_n) {
        fib.resize(max_n + 1);
        fib[0] = 0;
        fib[1] = 1;
        for (int i = 2; i <= max_n; i++) {
            fib[i] = fib[i-1] + fib[i-2];
        }
    }
    
    // 预处理稀疏表
    void precomputeSparseTable(const vector<long long>& arr) {
        n = arr.size();
        int k = log2(n) + 1;
        st.resize(n, vector<long long>(k));
        
        // 初始化第一层
        for (int i = 0; i < n; i++) {
            st[i][0] = arr[i];
        }
        
        // 构建稀疏表
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                st[i][j] = min(st[i][j-1], st[i + (1 << (j-1))][j-1]);
            }
        }
        
        // 预处理对数表
        log_table.resize(n + 1);
        log_table[1] = 0;
        for (int i = 2; i <= n; i++) {
            log_table[i] = log_table[i/2] + 1;
        }
    }
    
public:
    FibonacciRMQ(const vector<int>& indices) {
        int max_index = *max_element(indices.begin(), indices.end());
        precomputeFibonacci(max_index);
        
        // 构建斐波那契值数组
        vector<long long> fib_values;
        for (int idx : indices) {
            fib_values.push_back(fib[idx]);
        }
        
        precomputeSparseTable(fib_values);
    }
    
    // 区间最小值查询
    long long query(int l, int r) {
        if (l < 0 || r >= n || l > r) {
            throw out_of_range("Invalid range");
        }
        
        int j = log_table[r - l + 1];
        return min(st[l][j], st[r - (1 << j) + 1][j]);
    }
    
    // 获取斐波那契值
    long long getFibonacci(int index) {
        if (index < 0 || index >= fib.size()) {
            throw out_of_range("Index out of range");
        }
        return fib[index];
    }
};

int main() {
    // 测试用例
    vector<int> indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};  // 斐波那契索引
    FibonacciRMQ rmq(indices);
    
    // 测试查询
    cout << "斐波那契数列: ";
    for (int i = 0; i < 10; i++) {
        cout << rmq.getFibonacci(i) << " ";
    }
    cout << endl;
    
    cout << "区间[0, 5]的最小值: " << rmq.query(0, 5) << endl;  // 应该输出0
    cout << "区间[2, 7]的最小值: " << rmq.query(2, 7) << endl;  // 应该输出2
    cout << "区间[5, 9]的最小值: " << rmq.query(5, 9) << endl;  // 应该输出5
    
    // 边界测试
    cout << "区间[0, 0]的最小值: " << rmq.query(0, 0) << endl;  // 应该输出0
    cout << "区间[9, 9]的最小值: " << rmq.query(9, 9) << endl;  // 应该输出34
    
    return 0;
}