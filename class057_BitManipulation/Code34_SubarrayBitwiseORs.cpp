#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>

using namespace std;

/**
 * 子数组按位或操作
 * 测试链接：https://leetcode.cn/problems/subarray-bitwise-ors/
 * 
 * 题目描述：
 * 我们有一个非负整数数组 arr。
 * 对于每个（连续的）子数组 sub = [arr[i], arr[i+1], ..., arr[j]] （i <= j），
 * 我们对 sub 中的每个元素进行按位或操作，获得结果 arr[i] | arr[i+1] | ... | arr[j]。
 * 返回可能的结果的数量。多次出现的结果在最终答案中仅计算一次。
 * 
 * 解题思路：
 * 1. 暴力法：枚举所有子数组，计算OR值（会超时）
 * 2. 动态规划法：利用OR操作的单调性
 * 3. 集合维护法：维护当前所有可能的OR值集合
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n²)，会超时
 * - 优化方法：O(n * k)，其中k是不同OR值的数量
 * 
 * 空间复杂度分析：
 * - 优化方法：O(k)，需要存储当前所有可能的OR值
 */
class Solution {
public:
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n²)
     */
    int subarrayBitwiseORs1(vector<int>& arr) {
        unordered_set<int> result;
        int n = arr.size();
        
        for (int i = 0; i < n; i++) {
            int currentOR = 0;
            for (int j = i; j < n; j++) {
                currentOR |= arr[j];
                result.insert(currentOR);
            }
        }
        
        return result.size();
    }
    
    /**
     * 方法2：优化方法（推荐）
     * 核心思想：利用OR操作的单调性，维护当前所有可能的OR值
     * 时间复杂度：O(n * k)，其中k是不同OR值的数量
     * 空间复杂度：O(k)
     */
    int subarrayBitwiseORs2(vector<int>& arr) {
        unordered_set<int> result;
        unordered_set<int> current;
        
        for (int num : arr) {
            unordered_set<int> next;
            next.insert(num);
            
            // 将当前数字与之前的所有OR值进行OR操作
            for (int val : current) {
                next.insert(val | num);
            }
            
            result.insert(next.begin(), next.end());
            current = next;
        }
        
        return result.size();
    }
    
    /**
     * 方法3：进一步优化，使用vector代替unordered_set
     * 时间复杂度：O(n * k)
     * 空间复杂度：O(k)
     */
    int subarrayBitwiseORs3(vector<int>& arr) {
        unordered_set<int> result;
        vector<int> current;
        
        for (int num : arr) {
            vector<int> next;
            next.push_back(num);
            
            int last = num;
            for (int val : current) {
                int newVal = val | num;
                if (newVal != last) {
                    next.push_back(newVal);
                    last = newVal;
                }
            }
            
            for (int val : next) {
                result.insert(val);
            }
            current = next;
        }
        
        return result.size();
    }
    
    /**
     * 方法4：使用位运算优化的版本
     * 时间复杂度：O(n * 32)，因为最多有32个不同的位
     * 空间复杂度：O(32)
     */
    int subarrayBitwiseORs4(vector<int>& arr) {
        unordered_set<int> result;
        unordered_set<int> current;
        
        for (int num : arr) {
            unordered_set<int> next;
            next.insert(num);
            
            for (int val : current) {
                next.insert(val | num);
            }
            
            result.insert(next.begin(), next.end());
            current = next;
        }
        
        return result.size();
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：基础情况
    vector<int> arr1 = {0};
    int result1 = solution.subarrayBitwiseORs2(arr1);
    cout << "测试用例1 - 输入: [0]" << endl;
    cout << "结果: " << result1 << " (预期: 1)" << endl;
    
    // 测试用例2：重复元素
    vector<int> arr2 = {1, 1, 2};
    int result2 = solution.subarrayBitwiseORs2(arr2);
    cout << "测试用例2 - 输入: [1, 1, 2]" << endl;
    cout << "结果: " << result2 << " (预期: 3)" << endl;
    
    // 测试用例3：递增序列
    vector<int> arr3 = {1, 2, 4};
    int result3 = solution.subarrayBitwiseORs2(arr3);
    cout << "测试用例3 - 输入: [1, 2, 4]" << endl;
    cout << "结果: " << result3 << " (预期: 6)" << endl;
    
    // 测试用例4：边界情况
    vector<int> arr4 = {1};
    int result4 = solution.subarrayBitwiseORs2(arr4);
    cout << "测试用例4 - 输入: [1]" << endl;
    cout << "结果: " << result4 << " (预期: 1)" << endl;
    
    // 性能测试
    vector<int> arr5 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    auto start = chrono::high_resolution_clock::now();
    int result5 = solution.subarrayBitwiseORs2(arr5);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "性能测试 - 输入长度: " << arr5.size() << endl;
    cout << "结果: " << result5 << endl;
    cout << "耗时: " << duration.count() << "微秒" << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 暴力法:" << endl;
    cout << "  时间复杂度: O(n²) - 会超时" << endl;
    cout << "  空间复杂度: O(n²)" << endl;
    
    cout << "方法2 - 集合维护法:" << endl;
    cout << "  时间复杂度: O(n * k) - k为不同OR值数量" << endl;
    cout << "  空间复杂度: O(k)" << endl;
    
    cout << "方法3 - 数组优化版:" << endl;
    cout << "  时间复杂度: O(n * k)" << endl;
    cout << "  空间复杂度: O(k)" << endl;
    
    cout << "方法4 - 位运算优化版:" << endl;
    cout << "  时间复杂度: O(n * 32)" << endl;
    cout << "  空间复杂度: O(32)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法2是最实用的选择" << endl;
    cout << "2. 边界处理：处理空数组和单元素数组" << endl;
    cout << "3. 性能优化：避免重复计算，利用OR操作的单调性" << endl;
    cout << "4. 内存管理：及时清理不需要的中间结果" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. OR操作单调性：a | b >= max(a, b)" << endl;
    cout << "2. 集合去重：利用unordered_set自动去重" << endl;
    cout << "3. 动态维护：每次只维护当前可能的OR值集合" << endl;
    cout << "4. 位运算特性：OR操作不会减少1的个数" << endl;
    
    return 0;
}