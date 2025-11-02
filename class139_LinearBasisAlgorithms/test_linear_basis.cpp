// 线性基测试文件 (C++版本)

#include <iostream>
#include <vector>
#include <algorithm>
#include <cassert>

using namespace std;

const int BIT = 63;

// 线性基插入操作
void insert(long long num, long long basis[]) {
    for (int i = BIT; i >= 0; i--) {
        if ((num & (1LL << i)) != 0) {
            if (basis[i] == 0) {
                basis[i] = num;
                return;
            }
            num ^= basis[i];
        }
    }
}

// 带返回值的线性基插入操作
bool insertWithReturn(long long num, long long basis[]) {
    for (int i = BIT; i >= 0; i--) {
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

// 计算最大异或和
long long findMaximumXor(const vector<long long>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    long long basis[BIT + 1] = {0};  // 初始化线性基
    
    // 构建线性基
    for (long long num : nums) {
        insert(num, basis);
    }
    
    // 计算最大异或值
    long long result = 0;
    for (int i = BIT; i >= 0; i--) {
        if (basis[i] != 0) {
            result = max(result, result ^ basis[i]);
        }
    }
    
    return result;
}

// 查询第k小异或和
long long queryKthXor(const vector<long long>& nums, long long k) {
    if (k <= 0) {
        throw invalid_argument("k must be positive");
    }
    
    long long basis[BIT + 1] = {0};  // 初始化线性基
    int basisSize = 0;
    
    // 构建线性基
    for (long long num : nums) {
        if (insertWithReturn(num, basis)) {
            basisSize++;
        }
    }
    
    // 高斯消元
    for (int i = 0; i <= BIT; i++) {
        for (int j = i + 1; j <= BIT; j++) {
            if ((basis[j] & (1LL << i)) != 0) {
                basis[j] ^= basis[i];
            }
        }
    }
    
    // 重新整理
    vector<long long> gaussianBasis;
    for (int i = 0; i <= BIT; i++) {
        if (basis[i] != 0) {
            gaussianBasis.push_back(basis[i]);
        }
    }
    
    // 判断是否能异或出0
    bool canGetZero = (gaussianBasis.size() != nums.size());
    
    // 查询第k小
    if (canGetZero) {
        if (k == 1) {
            return 0;
        }
        k--;
    }
    
    if (k > (1LL << gaussianBasis.size())) {
        return -1;
    }
    
    long long result = 0;
    for (int i = 0; i < gaussianBasis.size(); i++) {
        if ((k & (1LL << i)) != 0) {
            result ^= gaussianBasis[i];
        }
    }
    
    return result;
}

// 测试用例1: 最大异或和
void testMaximumXor() {
    cout << "=== 测试最大异或和 ===" << endl;
    vector<long long> arr1 = {3, 10, 5, 25, 2, 8};
    long long result1 = findMaximumXor(arr1);
    cout << "输入: [3, 10, 5, 25, 2, 8]" << endl;
    cout << "期望输出: 28 (5^25)" << endl;
    cout << "实际输出: " << result1 << endl;
    cout << "测试结果: " << (result1 == 28 ? "通过" : "失败") << endl;
    cout << endl;
}

// 测试用例2: 线性相关情况
void testLinearDependent() {
    cout << "=== 测试线性相关情况 ===" << endl;
    vector<long long> arr2 = {1, 2, 3};  // 1^2 = 3，线性相关
    long long result2 = findMaximumXor(arr2);
    cout << "输入: [1, 2, 3]" << endl;
    cout << "期望输出: 3" << endl;
    cout << "实际输出: " << result2 << endl;
    cout << "测试结果: " << (result2 == 3 ? "通过" : "失败") << endl;
    cout << endl;
}

// 测试用例3: 空数组
void testEmptyArray() {
    cout << "=== 测试空数组 ===" << endl;
    vector<long long> arr3 = {};
    long long result3 = findMaximumXor(arr3);
    cout << "输入: []" << endl;
    cout << "期望输出: 0" << endl;
    cout << "实际输出: " << result3 << endl;
    cout << "测试结果: " << (result3 == 0 ? "通过" : "失败") << endl;
    cout << endl;
}

// 测试用例4: 单元素数组
void testSingleElement() {
    cout << "=== 测试单元素数组 ===" << endl;
    vector<long long> arr4 = {5};
    long long result4 = findMaximumXor(arr4);
    cout << "输入: [5]" << endl;
    cout << "期望输出: 5" << endl;
    cout << "实际输出: " << result4 << endl;
    cout << "测试结果: " << (result4 == 5 ? "通过" : "失败") << endl;
    cout << endl;
}

// 测试第k小异或和
void testKthXor() {
    cout << "=== 测试第k小异或和 ===" << endl;
    vector<long long> arr5 = {1, 2, 3};
    cout << "输入: [1, 2, 3]" << endl;
    for (int k = 1; k <= 4; k++) {
        long long result = queryKthXor(arr5, k);
        cout << "第" << k << "小异或和: " << result << endl;
    }
    cout << endl;
}

int main() {
    cout << "线性基算法测试 (C++版本)" << endl;
    cout << "========================" << endl;
    
    // 运行所有测试用例
    testMaximumXor();
    testLinearDependent();
    testEmptyArray();
    testSingleElement();
    testKthXor();
    
    cout << "所有测试完成！" << endl;
    
    return 0;
}