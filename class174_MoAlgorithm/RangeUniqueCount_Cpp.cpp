#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <set>
using namespace std;

/**
 * 区间不同数问题的常规莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，多次查询区间[l, r]中有多少个不同的数。
 * 
 * 解题思路：
 * 1. 使用莫队算法离线处理所有查询
 * 2. 将数组分成大小为 sqrt(n) 的块
 * 3. 按照块号对查询进行排序，同一块内按右端点排序
 * 4. 维护当前区间的不同数计数
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度为 O(m log m)
 * - 处理所有查询的时间复杂度为 O(n * sqrt(n))
 * - 总体时间复杂度为 O(n * sqrt(n) + m log m)
 * 
 * 空间复杂度分析：
 * - 存储数组、查询、计数数组等需要 O(n + m) 的空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用奇偶排序优化，合理选择块的大小
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */

// 用于存储查询的结构
struct Query {
    int l;      // 查询的左边界
    int r;      // 查询的右边界
    int idx;    // 查询的索引，用于输出答案时保持顺序
    int block;  // 查询所属的块
    
    Query(int l, int r, int idx, int blockSize) 
        : l(l), r(r), idx(idx), block(l / blockSize) {}
};

/**
 * 离散化函数
 * @param arr 原始数组
 * @param discreteArr 离散化后的数组
 * @return 离散化后的值域范围
 */
int discretize(const vector<int>& arr, vector<int>& discreteArr) {
    set<int> valueSet(arr.begin(), arr.end());
    vector<int> valueList(valueSet.begin(), valueSet.end());
    
    unordered_map<int, int> valueToId;
    for (int i = 0; i < valueList.size(); i++) {
        valueToId[valueList[i]] = i;
    }
    
    discreteArr.resize(arr.size());
    for (int i = 0; i < arr.size(); i++) {
        discreteArr[i] = valueToId[arr[i]];
    }
    
    return valueList.size();
}

/**
 * 比较两个查询的顺序，用于莫队算法的排序
 * 奇偶排序优化：偶数块按r升序，奇数块按r降序
 */
bool compareQueries(const Query& q1, const Query& q2) {
    if (q1.block != q2.block) {
        return q1.block < q2.block;
    }
    // 奇偶排序优化
    return q1.block % 2 == 0 ? q1.r < q2.r : q1.r > q2.r;
}

/**
 * 使用哈希表的版本
 * @param arr 输入数组
 * @param queriesInput 查询列表
 * @return 每个查询的结果
 */
vector<int> solveRangeUniqueCountHash(const vector<int>& arr, const vector<vector<int>>& queriesInput) {
    // 异常处理
    if (arr.empty() || queriesInput.empty()) {
        return {};
    }
    
    int n = arr.size();
    int m = queriesInput.size();
    
    // 计算块的大小
    int blockSize = static_cast<int>(sqrt(n)) + 1;
    
    // 创建查询对象
    vector<Query> queries;
    for (int i = 0; i < m; i++) {
        // 假设输入是1-based的，转换为0-based
        int l = queriesInput[i][0] - 1;
        int r = queriesInput[i][1] - 1;
        queries.emplace_back(l, r, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化结果数组
    vector<int> answers(m, 0);
    
    // 使用哈希表计数
    unordered_map<int, int> countMap;
    int currentResult = 0;  // 当前区间内不同数的数量
    
    // 初始化当前区间的左右指针
    int curL = 0;
    int curR = -1;
    
    // 处理每个查询
    for (const Query& q : queries) {
        int l = q.l;
        int r = q.r;
        int idx = q.idx;
        
        // 调整左右指针到目标位置
        // 向右扩展右端点
        while (curR < r) {
            curR++;
            int num = arr[curR];
            countMap[num]++;
            if (countMap[num] == 1) {
                currentResult++;
            }
        }
        
        // 向左收缩右端点
        while (curR > r) {
            int num = arr[curR];
            countMap[num]--;
            if (countMap[num] == 0) {
                currentResult--;
            }
            curR--;
        }
        
        // 向左扩展左端点
        while (curL > l) {
            curL--;
            int num = arr[curL];
            countMap[num]++;
            if (countMap[num] == 1) {
                currentResult++;
            }
        }
        
        // 向右收缩左端点
        while (curL < l) {
            int num = arr[curL];
            countMap[num]--;
            if (countMap[num] == 0) {
                currentResult--;
            }
            curL++;
        }
        
        // 保存当前查询的结果
        answers[idx] = currentResult;
    }
    
    return answers;
}

/**
 * 优化版本，使用离散化和数组计数提高性能
 * @param arr 输入数组
 * @param queriesInput 查询列表
 * @return 每个查询的结果
 */
vector<int> solveRangeUniqueCountOptimized(const vector<int>& arr, const vector<vector<int>>& queriesInput) {
    // 异常处理
    if (arr.empty() || queriesInput.empty()) {
        return {};
    }
    
    int n = arr.size();
    int m = queriesInput.size();
    
    // 离散化处理
    vector<int> discreteArr;
    int valueRange = discretize(arr, discreteArr);
    
    // 计算块的大小
    int blockSize = static_cast<int>(sqrt(n)) + 1;
    
    // 创建查询对象
    vector<Query> queries;
    for (int i = 0; i < m; i++) {
        // 假设输入是1-based的，转换为0-based
        int l = queriesInput[i][0] - 1;
        int r = queriesInput[i][1] - 1;
        queries.emplace_back(l, r, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化结果数组
    vector<int> answers(m, 0);
    
    // 使用数组计数
    vector<int> count(valueRange, 0);
    int currentResult = 0;  // 当前区间内不同数的数量
    
    // 初始化当前区间的左右指针
    int curL = 0;
    int curR = -1;
    
    // 处理每个查询
    for (const Query& q : queries) {
        int l = q.l;
        int r = q.r;
        int idx = q.idx;
        
        // 调整左右指针到目标位置
        // 向右扩展右端点
        while (curR < r) {
            curR++;
            int numId = discreteArr[curR];
            count[numId]++;
            if (count[numId] == 1) {
                currentResult++;
            }
        }
        
        // 向左收缩右端点
        while (curR > r) {
            int numId = discreteArr[curR];
            count[numId]--;
            if (count[numId] == 0) {
                currentResult--;
            }
            curR--;
        }
        
        // 向左扩展左端点
        while (curL > l) {
            curL--;
            int numId = discreteArr[curL];
            count[numId]++;
            if (count[numId] == 1) {
                currentResult++;
            }
        }
        
        // 向右收缩左端点
        while (curL < l) {
            int numId = discreteArr[curL];
            count[numId]--;
            if (count[numId] == 0) {
                currentResult--;
            }
            curL++;
        }
        
        // 保存当前查询的结果
        answers[idx] = currentResult;
    }
    
    return answers;
}

/**
 * 主函数，用于测试
 */
int main() {
    // 测试用例
    vector<int> arr = {1, 2, 1, 3, 4, 2, 5};
    vector<vector<int>> queries = {
        {1, 5},  // 查询区间[1,5]中不同数的数量
        {2, 6},  // 查询区间[2,6]中不同数的数量
        {3, 7}   // 查询区间[3,7]中不同数的数量
    };
    
    // 使用优化版本
    vector<int> results = solveRangeUniqueCountOptimized(arr, queries);
    
    // 输出结果
    cout << "Query Results:" << endl;
    for (int result : results) {
        cout << result << endl;
    }
    
    // 验证两种方法结果一致
    vector<int> results2 = solveRangeUniqueCountHash(arr, queries);
    bool allEqual = true;
    for (int i = 0; i < results.size(); i++) {
        if (results[i] != results2[i]) {
            allEqual = false;
            break;
        }
    }
    cout << "Results match: " << (allEqual ? "true" : "false") << endl;
    
    return 0;
}