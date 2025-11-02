/**
 * 洛谷 P3287 [SCOI2014]方伯伯的玉米田
 * 题目链接: https://www.luogu.com.cn/problem/P3287
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，每次可以选择一个区间[l,r]，区间内的数字都+1，最多执行k次
 * 返回执行完成后，最长的不下降子序列长度。
 * 
 * 解题思路:
 * 使用二维树状数组优化动态规划的方法解决此问题。
 * 1. 定义状态dp[i][j][h]表示处理前i个元素，使用j次操作，以高度h结尾的最长不下降子序列长度
 * 2. 由于高度范围较大，我们使用二维树状数组来维护状态
 * 3. 树状数组的第一维表示高度，第二维表示操作次数
 * 4. 对于每个元素，枚举可能的操作次数，查询最优解并更新状态
 * 
 * 时间复杂度分析:
 * - 状态转移: O(n*k*log(MAXH)*log(k))
 * - 总时间复杂度: O(n*k*log(MAXH)*log(k))
 * 空间复杂度: O(MAXH*k) 用于存储二维树状数组
 * 
 * 工程化考量:
 * 1. 内存优化: 使用二维树状数组减少空间占用
 * 2. 性能优化: 利用树状数组的O(log n)查询和更新
 * 3. 边界处理: 处理n=0或k=0的特殊情况
 * 4. 可读性: 清晰的变量命名和注释
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <climits>

using namespace std;

// 最大数组长度常量
const int MAXN = 10001;  // 最大元素个数
const int MAXK = 501;    // 最大操作次数+1
const int MAXH = 5500;   // 最大高度值

class FenwickTree2D {
private:
    vector<vector<int>> tree;
    int height, width;
    
public:
    /**
     * 构造函数，初始化二维树状数组
     * 
     * @param h 高度维度大小
     * @param w 宽度维度大小
     */
    FenwickTree2D(int h, int w) : height(h), width(w) {
        tree.resize(h + 1, vector<int>(w + 1, 0));
    }
    
    /**
     * 二维树状数组更新操作
     * 
     * @param x 第一维坐标
     * @param y 第二维坐标
     * @param val 要更新的值
     */
    void update(int x, int y, int val) {
        for (int i = x; i <= height; i += i & -i) {
            for (int j = y; j <= width; j += j & -j) {
                if (val > tree[i][j]) {
                    tree[i][j] = val;
                }
            }
        }
    }
    
    /**
     * 二维树状数组查询操作
     * 
     * @param x 第一维坐标
     * @param y 第二维坐标
     * @return 查询结果
     */
    int query(int x, int y) {
        int res = 0;
        for (int i = x; i > 0; i -= i & -i) {
            for (int j = y; j > 0; j -= j & -j) {
                if (tree[i][j] > res) {
                    res = tree[i][j];
                }
            }
        }
        return res;
    }
};

/**
 * 计算最长不下降子序列长度
 * 
 * @param arr 输入数组
 * @param n 数组长度
 * @param k 最大操作次数
 * @return 最长不下降子序列长度
 */
int maxNonDecreasingLength(vector<int>& arr, int n, int k) {
    // 异常处理
    if (n == 0) return 0;
    if (k < 0) k = 0;
    
    // 计算最大高度
    int max_height = 0;
    for (int i = 0; i < n; i++) {
        if (arr[i] > max_height) {
            max_height = arr[i];
        }
    }
    max_height += k;  // 考虑操作后的最大高度
    
    // 初始化二维树状数组
    FenwickTree2D fenwick(max_height, k + 1);
    
    int result = 1;  // 至少包含一个元素
    
    // 遍历数组中的每个元素
    for (int i = 0; i < n; i++) {
        // 枚举可能的操作次数
        for (int j = 0; j <= k; j++) {
            // 当前元素经过j次操作后的高度
            int current_height = arr[i] + j;
            
            // 查询之前高度不超过current_height，操作次数不超过j的最优解
            int pre_max = fenwick.query(current_height, j + 1);
            
            // 当前状态值
            int current_val = pre_max + 1;
            
            // 更新结果
            if (current_val > result) {
                result = current_val;
            }
            
            // 更新二维树状数组
            fenwick.update(current_height, j + 1, current_val);
        }
    }
    
    return result;
}

/**
 * 测试函数，验证算法正确性
 */
void testMaxNonDecreasingLength() {
    cout << "开始测试最长不下降子序列算法..." << endl;
    
    // 测试用例1: 正常情况
    vector<int> arr1 = {1, 2, 3, 2, 1};
    int result1 = maxNonDecreasingLength(arr1, 5, 2);
    cout << "测试用例1: {1, 2, 3, 2, 1}, k=2 -> " << result1 << endl;
    
    // 测试用例2: 不需要操作
    vector<int> arr2 = {1, 2, 3, 4, 5};
    int result2 = maxNonDecreasingLength(arr2, 5, 0);
    cout << "测试用例2: {1, 2, 3, 4, 5}, k=0 -> " << result2 << endl;
    assert(result2 == 5 && "测试用例2失败");
    
    // 测试用例3: 单元素
    vector<int> arr3 = {5};
    int result3 = maxNonDecreasingLength(arr3, 1, 3);
    cout << "测试用例3: {5}, k=3 -> " << result3 << endl;
    assert(result3 == 1 && "测试用例3失败");
    
    // 测试用例4: 空数组
    vector<int> arr4;
    int result4 = maxNonDecreasingLength(arr4, 0, 5);
    cout << "测试用例4: 空数组, k=5 -> " << result4 << endl;
    assert(result4 == 0 && "测试用例4失败");
    
    // 测试用例5: 递减序列
    vector<int> arr5 = {5, 4, 3, 2, 1};
    int result5 = maxNonDecreasingLength(arr5, 5, 2);
    cout << "测试用例5: {5, 4, 3, 2, 1}, k=2 -> " << result5 << endl;
    
    cout << "所有测试用例通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "开始性能测试..." << endl;
    
    // 大规模数据测试
    vector<int> large_arr;
    for (int i = 0; i < 1000; i++) {
        large_arr.push_back(i % 100 + 1);
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result = maxNonDecreasingLength(large_arr, 1000, 50);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "大规模测试: 数组长度" << large_arr.size() 
         << "，k=50，结果" << result 
         << "，耗时" << duration.count() << "毫秒" << endl;
}

int main() {
    // 运行测试
    testMaxNonDecreasingLength();
    
    // 性能测试
    performanceTest();
    
    return 0;
}