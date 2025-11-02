#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <set>
using namespace std;

/**
 * 牛客网 NC15278 区间第k大问题的回滚莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，多次查询区间内的第k大元素
 * 
 * 解题思路：
 * 1. 区间第k大问题可以使用回滚莫队算法解决
 * 2. 回滚莫队主要用于解决难以撤销操作的问题
 * 3. 对于区间第k大问题，我们可以使用值域分块来维护
 * 
 * 时间复杂度分析：
 * - 回滚莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是数组长度
 * - 值域分块的查询时间为 O(sqrt(maxValue))
 * - 总体时间复杂度为 O(n * sqrt(n) * sqrt(maxValue))
 * 
 * 空间复杂度分析：
 * - 存储数组、查询结构等需要 O(n) 的空间
 * - 值域分块数组需要 O(maxValue) 的空间
 * - 总体空间复杂度为 O(n + maxValue)
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用离散化来减小值域范围
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */

// 用于存储查询的结构
struct Query {
    int l;  // 查询的左边界
    int r;  // 查询的右边界
    int k;  // 第k大
    int idx;  // 查询的索引，用于输出答案时保持顺序
    int block;  // 查询所属的块
    
    Query(int l, int r, int k, int idx, int blockSize) 
        : l(l), r(r), k(k), idx(idx), block(l / blockSize) {}
};

// 全局变量
vector<int> nums;  // 数组的原始值
vector<int> values;  // 离散化后的值
unordered_map<int, int> valueToId;  // 原始值到离散值的映射
unordered_map<int, int> idToValue;  // 离散值到原始值的映射
int blockSize;  // 块的大小
int valueRange;  // 离散化后的值域大小
int valueBlockSize;  // 值域分块的块大小
vector<int> valueCount;  // 每个值出现的次数
vector<int> blockCount;  // 每个值域块的总出现次数
vector<int> answers;  // 答案数组

/**
 * 离散化函数
 * @param arr 原始数组
 * @return 离散化后的值域范围
 */
int discretize(vector<int>& arr) {
    set<int> valueSet(arr.begin(), arr.end());
    vector<int> valueList(valueSet.begin(), valueSet.end());
    
    valueToId.clear();
    idToValue.clear();
    for (int i = 0; i < valueList.size(); i++) {
        valueToId[valueList[i]] = i + 1;  // 从1开始编号
        idToValue[i + 1] = valueList[i];
    }
    
    values.resize(arr.size());
    for (int i = 0; i < arr.size(); i++) {
        values[i] = valueToId[arr[i]];
    }
    
    return valueList.size();
}

/**
 * 比较两个查询的顺序，用于回滚莫队算法的排序
 * 左端点在同一块内的查询，按右端点降序排列；否则按左端点升序排列
 */
bool compareQueries(const Query& q1, const Query& q2) {
    if (q1.block != q2.block) {
        return q1.l < q2.l;
    }
    // 同一块内的查询，按右端点降序排列，这样可以使用回滚莫队
    return q1.r > q2.r;
}

/**
 * 查询第k大的数
 * @param k 第k大
 * @return 第k大的数（原始值）
 */
int queryKthLargest(int k, const vector<int>& vCount, const vector<int>& bCount) {
    int sum = 0;
    // 先按块查找
    for (int i = bCount.size() - 1; i >= 0; i--) {
        if (sum + bCount[i] < k) {
            sum += bCount[i];
        } else {
            // 在当前块中查找
            int start = i * valueBlockSize;
            int end = min(start + valueBlockSize - 1, valueRange);
            for (int j = end; j >= start; j--) {
                sum += vCount[j];
                if (sum >= k) {
                    // 将离散化后的值转换回原始值
                    return idToValue[j];
                }
            }
        }
    }
    return -1;  // 不应该到达这里
}

/**
 * 主解题函数
 * @param arr 输入数组
 * @param queriesInput 查询列表，每个查询包含[l, r, k]
 * @return 每个查询的第k大元素
 */
vector<int> solve(vector<int>& arr, vector<vector<int>>& queriesInput) {
    // 异常处理
    if (arr.empty() || queriesInput.empty()) {
        return {};
    }
    
    nums = arr;
    int n = arr.size();
    int q = queriesInput.size();
    
    // 离散化
    valueRange = discretize(arr);
    
    // 计算块的大小
    blockSize = static_cast<int>(sqrt(n)) + 1;
    valueBlockSize = static_cast<int>(sqrt(valueRange)) + 1;
    
    // 创建查询
    vector<Query> queries;
    for (int i = 0; i < q; i++) {
        int l = queriesInput[i][0] - 1;  // 假设输入是1-based的
        int r = queriesInput[i][1] - 1;
        int k = queriesInput[i][2];
        queries.emplace_back(l, r, k, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化答案数组
    answers.assign(q, 0);
    
    // 初始化计数数组大小
    int maxValue = valueRange + 2;
    int maxBlock = (valueRange + valueBlockSize - 1) / valueBlockSize + 2;
    
    // 处理每个块
    int currentBlock = -1;
    int curR = -1;
    
    for (const auto& qObj : queries) {
        int l = qObj.l;
        int r = qObj.r;
        int k = qObj.k;
        int idx = qObj.idx;
        int block = qObj.block;
        
        // 如果是新的块，重置当前右端点和计数
        if (block != currentBlock) {
            // 清空计数
            valueCount.assign(maxValue, 0);
            blockCount.assign(maxBlock, 0);
            currentBlock = block;
            curR = block * blockSize + blockSize - 1;
            curR = min(curR, n - 1);
        }
        
        // 处理右端点
        while (curR < r) {
            curR++;
            int val = values[curR];
            valueCount[val]++;
            blockCount[val / valueBlockSize]++;
        }
        
        // 暂时保存当前状态，用于回滚
        vector<int> tempValueCount = valueCount;
        vector<int> tempBlockCount = blockCount;
        
        // 扩展左端点
        int tempL = block * blockSize;
        while (tempL > l) {
            tempL--;
            int val = values[tempL];
            tempValueCount[val]++;
            tempBlockCount[val / valueBlockSize]++;
        }
        
        // 保存查询结果
        answers[idx] = queryKthLargest(k, tempValueCount, tempBlockCount);
    }
    
    return answers;
}

/**
 * 主函数，用于测试
 */
int main() {
    // 测试用例
    vector<int> arr = {1, 3, 2, 4, 5};
    vector<vector<int>> queries = {
        {1, 5, 2},  // 查询区间[1,5]的第2大元素
        {2, 4, 1},  // 查询区间[2,4]的第1大元素
        {3, 5, 3}   // 查询区间[3,5]的第3大元素
    };
    
    vector<int> results = solve(arr, queries);
    
    // 输出结果
    cout << "Query Results:" << endl;
    for (int result : results) {
        cout << result << endl;
    }
    
    return 0;
}