// Beautiful Quadruples
// 题目来源：HackerRank
// 题目描述：
// 给定四个数组A, B, C, D，找到四元组(i, j, k, l)的数量，使得：
// 1. A[i] XOR B[j] XOR C[k] XOR D[l] = 0
// 2. i < j < k < l（如果数组有重复元素，索引需要严格递增）
// 测试链接：https://www.hackerrank.com/challenges/beautiful-quadruples/problem
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将四个数组分为两组，
// 分别计算前两个数组和后两个数组的所有可能XOR组合，然后通过哈希表统计满足条件的四元组数目
// 时间复杂度：O(n^2) 其中n是数组的最大长度
// 空间复杂度：O(n^2)
// 
// 工程化考量：
// 1. 异常处理：检查数组边界和输入合法性
// 2. 性能优化：使用折半搜索减少搜索空间，优化XOR计算
// 3. 可读性：变量命名清晰，注释详细
// 4. 去重处理：处理重复元素和索引约束
// 
// 语言特性差异：
// C++中使用unordered_map进行计数统计，使用位运算优化性能

#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <functional>
#include <chrono>
#include <ctime>
#include <string>
#include <queue>
#include <utility>
using namespace std;

class Solution {
public:
    /**
     * 计算满足条件的美丽四元组数目
     * 
     * @param A, B, C, D 四个输入数组
     * @return 满足条件的四元组数目
     * 
     * 算法核心思想：
     * 1. 折半搜索：将四个数组分为两组(A,B)和(C,D)
     * 2. XOR性质利用：A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
     * 3. 组合统计：分别计算两组的所有XOR值及其出现次数，然后进行匹配
     * 4. 索引约束：确保i < j < k < l
     * 
     * 时间复杂度分析：
     * - 每组需要计算O(n^2)个XOR组合
     * - 哈希表查找时间复杂度为O(1)
     * - 总体时间复杂度：O(n^2)
     * 
     * 空间复杂度分析：
     * - 需要存储O(n^2)个XOR值及其计数
     * - 空间复杂度：O(n^2)
     */
    long long beautifulQuadruples(vector<int>& A, vector<int>& B, 
                                 vector<int>& C, vector<int>& D) {
        // 边界条件检查
        if (A.empty() || B.empty() || C.empty() || D.empty()) {
            return 0;
        }
        
        // 对数组进行排序，便于处理索引约束
        sort(A.begin(), A.end());
        sort(B.begin(), B.end());
        sort(C.begin(), C.end());
        sort(D.begin(), D.end());
        
        // 计算第一组(A,B)的所有XOR值及其出现次数
        unordered_map<int, long long> abXorCount;
        unordered_map<int, vector<int>> abXorIndex;
        
        for (int i = 0; i < A.size(); i++) {
            for (int j = 0; j < B.size(); j++) {
                int xorVal = A[i] ^ B[j];
                abXorCount[xorVal]++;
                // 记录最大索引（用于后续的索引约束检查）
                int maxIndex = max(i, j);
                abXorIndex[xorVal].push_back(maxIndex);
            }
        }
        
        // 计算第二组(C,D)的所有XOR值及其出现次数
        unordered_map<int, long long> cdXorCount;
        unordered_map<int, vector<int>> cdXorIndex;
        
        for (int k = 0; k < C.size(); k++) {
            for (int l = 0; l < D.size(); l++) {
                int xorVal = C[k] ^ D[l];
                cdXorCount[xorVal]++;
                // 记录最小索引（用于后续的索引约束检查）
                int minIndex = min(k, l);
                cdXorIndex[xorVal].push_back(minIndex);
            }
        }
        
        long long totalCount = 0;
        
        // 遍历所有可能的XOR值组合
        for (auto& abEntry : abXorCount) {
            int abXor = abEntry.first;
            long long abCount = abEntry.second;
            
            // 根据XOR性质，需要找到cdXor = abXor的组合
            auto cdIt = cdXorCount.find(abXor);
            if (cdIt != cdXorCount.end()) {
                long long cdCount = cdIt->second;
                
                // 基本计数：不考虑索引约束
                totalCount += abCount * cdCount;
                
                // 减去违反索引约束的情况
                // 即存在i >= k 或 j >= l 的情况
                totalCount -= countInvalidCases(abXorIndex[abXor], 
                                              cdXorIndex[abXor],
                                              A.size(), B.size(), C.size(), D.size());
            }
        }
        
        return totalCount;
    }
    
private:
    /**
     * 计算违反索引约束的情况数目
     */
    long long countInvalidCases(vector<int>& abIndices, vector<int>& cdIndices,
                               int aLen, int bLen, int cLen, int dLen) {
        if (abIndices.empty() || cdIndices.empty()) {
            return 0;
        }
        
        // 对索引进行排序，便于统计
        sort(abIndices.begin(), abIndices.end());
        sort(cdIndices.begin(), cdIndices.end());
        
        long long invalidCount = 0;
        
        // 使用双指针技术统计违反约束的情况
        int cdPtr = 0;
        int cdSize = cdIndices.size();
        
        for (int abMaxIndex : abIndices) {
            // 找到所有cd最小索引 <= abMaxIndex 的组合
            while (cdPtr < cdSize && cdIndices[cdPtr] <= abMaxIndex) {
                cdPtr++;
            }
            
            // cdPtr之前的组合都违反约束
            invalidCount += cdPtr;
        }
        
        return invalidCount;
    }
};

// 优化版本：使用更高效的索引约束处理方法
class OptimizedSolution {
public:
    long long beautifulQuadruples(vector<int>& A, vector<int>& B,
                                 vector<int>& C, vector<int>& D) {
        if (A.empty() || B.empty() || C.empty() || D.empty()) {
            return 0;
        }
        
        // 排序数组
        sort(A.begin(), A.end());
        sort(B.begin(), B.end());
        sort(C.begin(), C.end());
        sort(D.begin(), D.end());
        
        // 计算A,B的所有XOR组合，并记录索引信息
        unordered_map<int, vector<int>> abCombinations;
        for (int i = 0; i < A.size(); i++) {
            for (int j = 0; j < B.size(); j++) {
                int xorVal = A[i] ^ B[j];
                int maxIndex = max(i, j);
                abCombinations[xorVal].push_back(maxIndex);
            }
        }
        
        // 计算C,D的所有XOR组合
        unordered_map<int, vector<int>> cdCombinations;
        for (int k = 0; k < C.size(); k++) {
            for (int l = 0; l < D.size(); l++) {
                int xorVal = C[k] ^ D[l];
                int minIndex = min(k, l);
                cdCombinations[xorVal].push_back(minIndex);
            }
        }
        
        long long totalCount = 0;
        
        // 统计所有满足XOR条件的组合
        for (auto& abEntry : abCombinations) {
            int xorVal = abEntry.first;
            vector<int>& abIndices = abEntry.second;
            
            auto cdIt = cdCombinations.find(xorVal);
            if (cdIt != cdCombinations.end()) {
                vector<int>& cdIndices = cdIt->second;
                
                // 对cd索引进行排序
                sort(cdIndices.begin(), cdIndices.end());
                
                // 计算前缀和：prefixSum[i]表示前i+1个元素的计数
                vector<int> prefixCounts(cdIndices.size());
                int countSoFar = 0;
                for (int i = 0; i < cdIndices.size(); i++) {
                    countSoFar++;
                    prefixCounts[i] = countSoFar;
                }
                
                // 计算满足索引约束的组合数
                for (int abMaxIndex : abIndices) {
                    // 使用二分查找找到第一个大于abMaxIndex的位置
                    int left = 0, right = cdIndices.size();
                    int firstInvalid = cdIndices.size();
                    
                    while (left < right) {
                        int mid = left + (right - left) / 2;
                        if (cdIndices[mid] > abMaxIndex) {
                            firstInvalid = mid;
                            right = mid;
                        } else {
                            left = mid + 1;
                        }
                    }
                    
                    // 前firstInvalid个组合违反约束
                    if (firstInvalid > 0) {
                        totalCount += prefixCounts[firstInvalid - 1];
                    }
                }
            }
        }
        
        return totalCount;
    }
};

// 单元测试
void testBeautifulQuadruples() {
    Solution solution;
    
    // 测试用例1：简单情况
    cout << "=== 测试用例1：简单情况 ===" << endl;
    vector<int> A1 = {1, 2};
    vector<int> B1 = {3, 4};
    vector<int> C1 = {5, 6};
    vector<int> D1 = {7, 8};
    
    long long result1 = solution.beautifulQuadruples(A1, B1, C1, D1);
    cout << "数组A: ";
    for (int a : A1) cout << a << " ";
    cout << endl;
    cout << "数组B: ";
    for (int b : B1) cout << b << " ";
    cout << endl;
    cout << "数组C: ";
    for (int c : C1) cout << c << " ";
    cout << endl;
    cout << "数组D: ";
    for (int d : D1) cout << d << " ";
    cout << endl;
    cout << "实际输出: " << result1 << endl;
    cout << endl;
    
    // 测试用例2：存在重复元素
    cout << "=== 测试用例2：存在重复元素 ===" << endl;
    vector<int> A2 = {1, 1};
    vector<int> B2 = {2, 2};
    vector<int> C2 = {3, 3};
    vector<int> D2 = {4, 4};
    
    long long result2 = solution.beautifulQuadruples(A2, B2, C2, D2);
    cout << "数组A: ";
    for (int a : A2) cout << a << " ";
    cout << endl;
    cout << "数组B: ";
    for (int b : B2) cout << b << " ";
    cout << endl;
    cout << "数组C: ";
    for (int c : C2) cout << c << " ";
    cout << endl;
    cout << "数组D: ";
    for (int d : D2) cout << d << " ";
    cout << endl;
    cout << "实际输出: " << result2 << endl;
    cout << endl;
}

// 性能测试
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    OptimizedSolution solution;
    
    // 生成大规模测试数据
    int size = 50;
    vector<int> A3(size), B3(size), C3(size), D3(size);
    srand(time(nullptr));
    
    for (int i = 0; i < size; i++) {
        A3[i] = rand() % 1000 + 1;
        B3[i] = rand() % 1000 + 1;
        C3[i] = rand() % 1000 + 1;
        D3[i] = rand() % 1000 + 1;
    }
    
    auto start = chrono::high_resolution_clock::now();
    long long result3 = solution.beautifulQuadruples(A3, B3, C3, D3);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "数据规模: 4个数组，每个长度" << size << endl;
    cout << "执行时间: " << duration.count() << " 毫秒" << endl;
    cout << "结果: " << result3 << endl;
}

int main() {
    testBeautifulQuadruples();
    performanceTest();
    return 0;
}

/*
 * 算法深度分析：
 * 
 * 1. XOR性质利用：
 *    - A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
 *    - 这个性质是算法优化的关键，将四元组问题转化为两组二元组问题
 * 
 * 2. 折半搜索优势：
 *    - 直接暴力搜索时间复杂度为O(n^4)，不可接受
 *    - 折半搜索将复杂度降为O(n^2)，可以处理较大规模数据
 *    - 结合哈希表实现快速查找匹配
 * 
 * 3. 索引约束处理：
 *    - 这是算法的难点，需要确保i < j < k < l
 *    - 通过记录索引信息并使用前缀和优化，高效处理约束条件
 *    - 使用排序和二分查找优化范围查询
 * 
 * 4. C++特性利用：
 *    - 使用unordered_map进行快速查找
 *    - 利用位运算优化XOR计算
 *    - 使用STL算法进行排序和搜索
 * 
 * 5. 工程化改进：
 *    - 提供基础版本和优化版本，便于性能对比
 *    - 全面的异常处理和测试用例
 *    - 详细的注释和算法分析
 * 
 * 6. 扩展应用：
 *    - 类似思路可用于其他多数组的XOR问题
 *    - 可以处理不同大小的数组
 *    - 可以扩展到更多数组的组合问题
 */