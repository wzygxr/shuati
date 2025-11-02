#include <iostream>
#include <vector>
#include <algorithm>
#include <set>
#include <climits>
#include <cmath>

using namespace std;

/**
 * 寻找最近和次近 (C++实现)
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标1 ~ n范围，数组无重复值
 * 关于近的定义，距离的定义如下:
 * 对i位置的数字x来说，只关注右侧的数字，和x的差值绝对值越小就越近
 * 距离为差值绝对值，如果距离一样，数值越小的越近
 * 
 * 解题思路：
 * 这是一个寻找最近邻元素的问题，可以使用两种不同的方法解决。
 * 
 * 方法一：使用set（有序表）
 * 1. 从右向左遍历数组
 * 2. 对于每个元素，使用set查找最近和次近的元素
 * 3. 更新结果数组
 * 
 * 方法二：使用双向链表
 * 1. 将数组元素按值排序，建立双向链表
 * 2. 从左向右遍历原数组
 * 3. 对于每个元素，在双向链表中查找最近和次近的元素
 * 4. 删除当前元素，避免影响后续查找
 * 
 * 时间复杂度：
 * - set方法：O(n * log n)
 * - 双向链表方法：O(n * log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode 220. 存在重复元素 III (set滑动窗口)
 * 2. LeetCode 219. 存在重复元素 II (哈希表滑动窗口)
 * 3. LeetCode 480. 滑动窗口中位数
 * 4. LeetCode 992. K 个不同整数的子数组
 * 5. LeetCode 76. 最小覆盖子串
 * 6. LeetCode 3. 无重复字符的最长子串
 * 7. LintCode 363. 接雨水
 * 8. HackerRank - Sliding Window Median
 * 9. Codeforces 372C. Watching Fireworks is Fun
 * 10. AtCoder ABC134F. Permutation Oddness
 * 11. 牛客网 NC123. 滑动窗口的最大值
 * 12. 杭电OJ 6827. Master of Subgraph
 * 13. POJ 2823. Sliding Window
 * 14. UVa 11572. Unique Snowflakes
 * 15. CodeChef - CHEFCOMP
 * 
 * 工程化考量：
 * 1. 在实际应用中，最近邻查找算法常用于：
 *    - 推荐系统中的相似度计算
 *    - 图像处理中的特征匹配
 *    - 数据库查询优化
 *    - 机器学习中的K近邻算法
 * 2. 实现优化：
 *    - 对于大规模数据，可以使用KD树或球树优化
 *    - 使用空间换时间，预处理可能的查询结果
 *    - 考虑使用更高效的数据结构存储数据
 * 3. 可扩展性：
 *    - 支持多维数据的最近邻查找
 *    - 处理动态添加和删除数据
 *    - 扩展到分布式计算环境
 * 4. 鲁棒性考虑：
 *    - 处理重复值和边界情况
 *    - 优化大规模数据的性能
 *    - 处理数值溢出和精度问题
 * 5. 跨语言特性对比：
 *    - C++: 使用set和自定义比较函数，性能最优
 *    - Java: 使用TreeSet和Comparator
 *    - Python: 使用sorted和bisect模块，代码简洁
 */

class Code04_FindNear {
public:
    /**
     * 方法一：使用set查找最近和次近元素
     * 
     * @param arr 输入数组，下标从1开始
     * @return 二维数组，result[i][0]表示最近元素索引，result[i][1]表示次近元素索引
     */
    static vector<vector<int>> findNearWithSet(const vector<int>& arr) {
        int n = arr.size();
        vector<vector<int>> result(n + 1, vector<int>(2, -1));
        
        // 使用set存储右侧元素（值, 索引）
        set<pair<int, int>> rightSet;
        
        // 从右向左遍历数组
        for (int i = n; i >= 1; i--) {
            int currentValue = arr[i - 1];  // 转换为0-based索引
            
            if (!rightSet.empty()) {
                // 查找最近和次近元素
                auto currentPair = make_pair(currentValue, i);
                
                // 查找第一个大于等于当前元素的元素
                auto it = rightSet.lower_bound(currentPair);
                
                vector<pair<int, int>> candidates;
                
                // 检查右侧的较大元素
                if (it != rightSet.end()) {
                    candidates.push_back(*it);
                    if (next(it) != rightSet.end()) {
                        candidates.push_back(*next(it));
                    }
                }
                
                // 检查左侧的较小元素
                if (it != rightSet.begin()) {
                    auto prevIt = prev(it);
                    candidates.push_back(*prevIt);
                    if (prevIt != rightSet.begin()) {
                        candidates.push_back(*prev(prevIt));
                    }
                }
                
                // 按距离排序候选元素
                sort(candidates.begin(), candidates.end(), 
                    [currentValue](const pair<int, int>& a, const pair<int, int>& b) {
                        int distA = abs(a.first - currentValue);
                        int distB = abs(b.first - currentValue);
                        if (distA != distB) {
                            return distA < distB;
                        }
                        return a.first < b.first;
                    });
                
                // 取前两个作为最近和次近
                if (candidates.size() >= 1) {
                    result[i][0] = candidates[0].second;
                }
                if (candidates.size() >= 2) {
                    result[i][1] = candidates[1].second;
                }
            }
            
            // 将当前元素加入set
            rightSet.insert(make_pair(currentValue, i));
        }
        
        return result;
    }
    
    /**
     * 方法二：使用双向链表查找最近和次近元素
     * 
     * @param arr 输入数组
     * @return 二维数组，result[i][0]表示最近元素索引，result[i][1]表示次近元素索引
     */
    static vector<vector<int>> findNearWithLinkedList(const vector<int>& arr) {
        int n = arr.size();
        vector<vector<int>> result(n + 1, vector<int>(2, -1));
        
        // 创建(值, 索引)对并排序
        vector<pair<int, int>> valueIndexPairs;
        for (int i = 1; i <= n; i++) {
            valueIndexPairs.push_back(make_pair(arr[i - 1], i));
        }
        
        // 按值排序
        sort(valueIndexPairs.begin(), valueIndexPairs.end());
        
        // 构建双向链表：存储排序后的索引顺序
        vector<int> prev(n + 2, -1);  // 前驱指针
        vector<int> next(n + 2, -1);  // 后继指针
        vector<int> posInSorted(n + 1, -1);  // 原始索引在排序数组中的位置
        
        // 初始化链表
        for (int i = 0; i < n; i++) {
            int originalIndex = valueIndexPairs[i].second;
            posInSorted[originalIndex] = i + 1;  // 1-based位置
            
            if (i > 0) {
                prev[i + 1] = i;
            }
            if (i < n - 1) {
                next[i + 1] = i + 2;
            }
        }
        
        // 按原始顺序从左向右处理
        for (int i = 1; i <= n; i++) {
            int currentPos = posInSorted[i];
            
            if (currentPos == -1) continue;
            
            vector<int> candidates;
            
            // 检查前驱
            if (prev[currentPos] != -1) {
                candidates.push_back(valueIndexPairs[prev[currentPos] - 1].second);
                if (prev[prev[currentPos]] != -1) {
                    candidates.push_back(valueIndexPairs[prev[prev[currentPos]] - 1].second);
                }
            }
            
            // 检查后继
            if (next[currentPos] != -1) {
                candidates.push_back(valueIndexPairs[next[currentPos] - 1].second);
                if (next[next[currentPos]] != -1) {
                    candidates.push_back(valueIndexPairs[next[next[currentPos]] - 1].second);
                }
            }
            
            // 按距离排序候选元素
            sort(candidates.begin(), candidates.end(), 
                [i, arr](int a, int b) {
                    int distA = abs(arr[a - 1] - arr[i - 1]);
                    int distB = abs(arr[b - 1] - arr[i - 1]);
                    if (distA != distB) {
                        return distA < distB;
                    }
                    return arr[a - 1] < arr[b - 1];
                });
            
            // 取前两个作为最近和次近
            if (candidates.size() >= 1) {
                result[i][0] = candidates[0];
            }
            if (candidates.size() >= 2) {
                result[i][1] = candidates[1];
            }
            
            // 从链表中删除当前元素
            if (prev[currentPos] != -1) {
                next[prev[currentPos]] = next[currentPos];
            }
            if (next[currentPos] != -1) {
                prev[next[currentPos]] = prev[currentPos];
            }
        }
        
        return result;
    }
    
    /**
     * 统一接口：根据参数选择不同的实现方法
     * 
     * @param arr 输入数组
     * @param useSet 是否使用set方法，true使用set，false使用链表
     * @return 最近和次近元素索引
     */
    static vector<vector<int>> findNear(const vector<int>& arr, bool useSet = true) {
        if (useSet) {
            return findNearWithSet(arr);
        } else {
            return findNearWithLinkedList(arr);
        }
    }
};

/**
 * 测试函数 - 验证算法正确性
 */
void testFindNear() {
    cout << "=== 测试Code04_FindNear ===" << endl;
    
    // 测试用例1：基本功能测试
    vector<int> arr1 = {3, 1, 4, 2, 5};
    
    cout << "测试用例1 - 输入数组: ";
    for (int num : arr1) {
        cout << num << " ";
    }
    cout << endl;
    
    // 使用set方法
    auto result1_set = Code04_FindNear::findNearWithSet(arr1);
    cout << "Set方法结果:" << endl;
    for (int i = 1; i <= arr1.size(); i++) {
        cout << "位置" << i << " (值" << arr1[i-1] << "): ";
        cout << "最近=" << result1_set[i][0] << " (值" << (result1_set[i][0] != -1 ? arr1[result1_set[i][0]-1] : -1) << "), ";
        cout << "次近=" << result1_set[i][1] << " (值" << (result1_set[i][1] != -1 ? arr1[result1_set[i][1]-1] : -1) << ")" << endl;
    }
    
    // 使用链表方法
    auto result1_list = Code04_FindNear::findNearWithLinkedList(arr1);
    cout << "链表方法结果:" << endl;
    for (int i = 1; i <= arr1.size(); i++) {
        cout << "位置" << i << " (值" << arr1[i-1] << "): ";
        cout << "最近=" << result1_list[i][0] << " (值" << (result1_list[i][0] != -1 ? arr1[result1_list[i][0]-1] : -1) << "), ";
        cout << "次近=" << result1_list[i][1] << " (值" << (result1_list[i][1] != -1 ? arr1[result1_list[i][1]-1] : -1) << ")" << endl;
    }
    
    // 测试用例2：单元素数组
    vector<int> arr2 = {5};
    auto result2 = Code04_FindNear::findNearWithSet(arr2);
    cout << "测试用例2 - 单元素数组: " << endl;
    cout << "位置1: 最近=" << result2[1][0] << ", 次近=" << result2[1][1] << endl;
    
    // 测试用例3：有序数组
    vector<int> arr3 = {1, 2, 3, 4, 5};
    auto result3 = Code04_FindNear::findNearWithSet(arr3);
    cout << "测试用例3 - 有序数组结果:" << endl;
    for (int i = 1; i <= arr3.size(); i++) {
        cout << "位置" << i << ": 最近=" << result3[i][0] << ", 次近=" << result3[i][1] << endl;
    }
    
    cout << "=== 测试完成 ===" << endl;
}

/**
 * 性能分析函数
 */
void performanceAnalysis() {
    cout << "=== 性能分析 ===" << endl;
    
    // 生成大规模测试数据
    int n = 10000;
    vector<int> largeArr;
    for (int i = 0; i < n; i++) {
        largeArr.push_back(rand() % 1000000);
    }
    
    // 测试set方法性能
    auto start = chrono::high_resolution_clock::now();
    auto result_set = Code04_FindNear::findNearWithSet(largeArr);
    auto end = chrono::high_resolution_clock::now();
    auto duration_set = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "Set方法 - 数据规模: " << n << " 元素" << endl;
    cout << "执行时间: " << duration_set.count() << " 毫秒" << endl;
    
    // 测试链表方法性能
    start = chrono::high_resolution_clock::now();
    auto result_list = Code04_FindNear::findNearWithLinkedList(largeArr);
    end = chrono::high_resolution_clock::now();
    auto duration_list = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "链表方法 - 数据规模: " << n << " 元素" << endl;
    cout << "执行时间: " << duration_list.count() << " 毫秒" << endl;
    
    cout << "性能对比: Set方法/链表方法 = " << (double)duration_set.count() / duration_list.count() << endl;
    
    cout << "时间复杂度: O(n log n)" << endl;
    cout << "空间复杂度: O(n)" << endl;
}

/**
 * 算法复杂度分析
 */
void complexityAnalysis() {
    cout << "=== 算法复杂度分析 ===" << endl;
    
    cout << "1. Set方法复杂度分析:" << endl;
    cout << "   - 插入操作: O(log n)" << endl;
    cout << "   - 查找操作: O(log n)" << endl;
    cout << "   - 总时间复杂度: O(n log n)" << endl;
    cout << "   - 空间复杂度: O(n)" << endl;
    
    cout << "2. 链表方法复杂度分析:" << endl;
    cout << "   - 排序操作: O(n log n)" << endl;
    cout << "   - 链表操作: O(n)" << endl;
    cout << "   - 总时间复杂度: O(n log n)" << endl;
    cout << "   - 空间复杂度: O(n)" << endl;
    
    cout << "3. 优化方向:" << endl;
    cout << "   - 对于特定分布的数据，可以使用更优化的数据结构" << endl;
    cout << "   - 使用空间换时间，预处理可能的查询结果" << endl;
    cout << "   - 考虑使用更高效的数据结构存储数据" << endl;
}

/**
 * 主函数 - 程序入口
 */
int main() {
    cout << "=== Code04_FindNear C++实现 ===" << endl;
    
    // 运行测试
    testFindNear();
    
    // 性能分析
    performanceAnalysis();
    
    // 算法复杂度分析
    complexityAnalysis();
    
    return 0;
}