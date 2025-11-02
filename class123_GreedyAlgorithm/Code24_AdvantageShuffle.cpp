#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <chrono>
#include <cstdlib>
#include <ctime>

using namespace std;

/**
 * LeetCode 870. 优势洗牌
 * 题目链接：https://leetcode.cn/problems/advantage-shuffle/
 * 难度：中等
 * 
 * C++实现版本 - 提供两种解法对比
 */

class Solution {
public:
    /**
     * 优势洗牌解决方案 - TreeMap版本
     * @param nums1 数组A
     * @param nums2 数组B
     * @return 使A优势最大化的排列
     */
    vector<int> advantageCount(vector<int>& nums1, vector<int>& nums2) {
        // 边界条件处理
        if (nums1.size() != nums2.size()) {
            throw invalid_argument("输入数组长度必须相等");
        }
        
        int n = nums1.size();
        if (n == 0) {
            return {};
        }
        
        // 排序数组A
        vector<int> sortedA = nums1;
        sort(sortedA.begin(), sortedA.end());
        
        // 使用multimap记录B的值（可能有重复值）
        multimap<int, int> map;
        for (int i = 0; i < n; i++) {
            map.insert({nums2[i], i});
        }
        
        vector<int> result(n, 0);
        
        // 贪心策略：对于A中的每个元素，在B中寻找刚好比它小的最大元素
        for (int i = 0; i < n; i++) {
            auto it = map.lower_bound(sortedA[i]);
            if (it != map.begin()) {
                // 找到可以赢的元素
                it--;
                result[it->second] = sortedA[i];
                map.erase(it);
            } else {
                // 没有找到可以赢的元素，使用田忌赛马策略
                auto maxIt = map.end();
                maxIt--;
                result[maxIt->second] = sortedA[i];
                map.erase(maxIt);
            }
        }
        
        return result;
    }
    
    /**
     * 优化版本：双指针 + 排序索引
     * 更高效的实现
     */
    vector<int> advantageCountOptimized(vector<int>& nums1, vector<int>& nums2) {
        if (nums1.size() != nums2.size()) {
            throw invalid_argument("输入数组长度必须相等");
        }
        
        int n = nums1.size();
        if (n == 0) {
            return {};
        }
        
        // 排序数组A
        vector<int> sortedA = nums1;
        sort(sortedA.begin(), sortedA.end());
        
        // 创建B的索引数组并按照B的值排序
        vector<int> indices(n);
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        sort(indices.begin(), indices.end(), [&](int i, int j) {
            return nums2[i] < nums2[j];
        });
        
        // 双指针策略
        vector<int> result(n);
        int left = 0, right = n - 1;
        
        for (int num : sortedA) {
            // 如果当前A的值大于B的最小值，则配对
            if (num > nums2[indices[left]]) {
                result[indices[left]] = num;
                left++;
            } else {
                // 否则用当前A的值配对B的最大值（田忌赛马）
                result[indices[right]] = num;
                right--;
            }
        }
        
        return result;
    }
};

/**
 * 计算A相对于B的优势数量
 */
int calculateAdvantage(const vector<int>& A, const vector<int>& B) {
    int advantage = 0;
    for (int i = 0; i < A.size(); i++) {
        if (A[i] > B[i]) {
            advantage++;
        }
    }
    return advantage;
}

/**
 * 测试函数
 */
void testAdvantageShuffle() {
    Solution solution;
    
    // 测试用例1：标准示例
    vector<int> A1 = {2, 7, 11, 15};
    vector<int> B1 = {1, 10, 4, 11};
    cout << "=== 测试用例1 ===" << endl;
    cout << "A: ";
    for (int a : A1) cout << a << " ";
    cout << endl;
    cout << "B: ";
    for (int b : B1) cout << b << " ";
    cout << endl;
    
    vector<int> result1 = solution.advantageCount(A1, B1);
    vector<int> result1Opt = solution.advantageCountOptimized(A1, B1);
    
    cout << "TreeMap版本结果: ";
    for (int r : result1) cout << r << " ";
    cout << endl;
    cout << "双指针版本结果: ";
    for (int r : result1Opt) cout << r << " ";
    cout << endl;
    
    int advantage1 = calculateAdvantage(result1, B1);
    int advantage1Opt = calculateAdvantage(result1Opt, B1);
    cout << "TreeMap版本优势: " << advantage1 << endl;
    cout << "双指针版本优势: " << advantage1Opt << endl;
    cout << endl;
    
    // 测试用例2：A全部大于B
    vector<int> A2 = {12, 24, 8, 32};
    vector<int> B2 = {13, 25, 32, 11};
    cout << "=== 测试用例2 ===" << endl;
    cout << "A: ";
    for (int a : A2) cout << a << " ";
    cout << endl;
    cout << "B: ";
    for (int b : B2) cout << b << " ";
    cout << endl;
    
    vector<int> result2 = solution.advantageCount(A2, B2);
    vector<int> result2Opt = solution.advantageCountOptimized(A2, B2);
    
    cout << "TreeMap版本结果: ";
    for (int r : result2) cout << r << " ";
    cout << endl;
    cout << "双指针版本结果: ";
    for (int r : result2Opt) cout << r << " ";
    cout << endl;
    
    int advantage2 = calculateAdvantage(result2, B2);
    int advantage2Opt = calculateAdvantage(result2Opt, B2);
    cout << "TreeMap版本优势: " << advantage2 << endl;
    cout << "双指针版本优势: " << advantage2Opt << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    Solution solution;
    
    cout << "=== 性能测试 ===" << endl;
    vector<int> largeA(10000);
    vector<int> largeB(10000);
    
    srand(time(0));
    for (int i = 0; i < largeA.size(); i++) {
        largeA[i] = rand() % 100000;
        largeB[i] = rand() % 100000;
    }
    
    // TreeMap版本性能测试
    auto start = chrono::high_resolution_clock::now();
    vector<int> largeResult = solution.advantageCount(largeA, largeB);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "TreeMap版本 - 耗时: " << duration.count() << "ms" << endl;
    
    // 双指针版本性能测试
    start = chrono::high_resolution_clock::now();
    vector<int> largeResultOpt = solution.advantageCountOptimized(largeA, largeB);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "双指针版本 - 耗时: " << duration.count() << "ms" << endl;
    
    int largeAdvantage = calculateAdvantage(largeResult, largeB);
    int largeAdvantageOpt = calculateAdvantage(largeResultOpt, largeB);
    cout << "TreeMap版本优势: " << largeAdvantage << endl;
    cout << "双指针版本优势: " << largeAdvantageOpt << endl;
}

int main() {
    // 运行测试用例
    testAdvantageShuffle();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用STL容器：vector, map, multimap
   - 使用algorithm头文件中的sort函数
   - 使用lambda表达式进行自定义排序

2. 内存管理：
   - vector自动管理内存
   - 使用引用传递避免不必要的拷贝
   - 合理使用STL容器提高效率

3. 性能优化：
   - 双指针版本效率明显高于TreeMap版本
   - 使用排序索引避免频繁的map操作
   - 算法时间复杂度为O(n log n)

4. 异常处理：
   - 使用C++异常机制处理错误
   - 边界条件检查确保程序健壮性

5. 代码风格：
   - 遵循C++命名规范
   - 使用有意义的变量名
   - 适当的注释和空行

6. 工程实践：
   - 提供完整的测试框架
   - 包含性能测试和对比
   - 支持多种算法实现
*/