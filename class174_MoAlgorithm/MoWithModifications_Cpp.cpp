#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <set>
using namespace std;

/**
 * 带修改的莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，支持两种操作：
 * 1. 修改操作：将数组中某个位置的元素修改为新值
 * 2. 查询操作：查询区间[l, r]中有多少个不同的数
 * 
 * 解题思路：
 * 1. 使用带修改的莫队算法离线处理所有查询和修改
 * 2. 将数组分成大小为 n^(2/3) 的块（最优块大小）
 * 3. 按照块号、右端点块号、时间戳进行排序
 * 4. 维护当前区间的不同数计数和时间戳
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度为 O(m log m)
 * - 处理所有查询的时间复杂度为 O(n^(5/3))
 * - 总体时间复杂度为 O(n^(5/3) + m log m)
 * 
 * 空间复杂度分析：
 * - 存储数组、查询、修改、计数数组等需要 O(n + m) 的空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用最优的块大小 n^(2/3)
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */

// 用于存储查询的结构
struct Query {
    int l;      // 查询的左边界
    int r;      // 查询的右边界
    int t;      // 查询的时间戳（在第几次修改之后）
    int idx;    // 查询的索引，用于输出答案时保持顺序
    int blockL; // 左端点所在的块
    int blockR; // 右端点所在的块
    
    Query(int l, int r, int t, int idx, int blockSize) 
        : l(l), r(r), t(t), idx(idx), 
          blockL(l / blockSize), blockR(r / blockSize) {}
};

// 用于存储修改的结构
struct Modification {
    int pos;    // 修改的位置
    int oldVal; // 修改前的值
    int newVal; // 修改后的值
    
    Modification(int pos, int oldVal, int newVal) 
        : pos(pos), oldVal(oldVal), newVal(newVal) {}
};

/**
 * 离散化函数
 * @param arr 原始数组
 * @param modifications 修改列表
 * @param discreteArr 离散化后的数组
 * @param valueToId 原始值到离散值的映射
 * @return 离散化后的值域范围
 */
int discretize(const vector<int>& arr, const vector<Modification>& modifications, 
               vector<int>& discreteArr, unordered_map<int, int>& valueToId) {
    set<int> valueSet(arr.begin(), arr.end());
    for (const auto& mod : modifications) {
        valueSet.insert(mod.oldVal);
        valueSet.insert(mod.newVal);
    }
    
    vector<int> valueList(valueSet.begin(), valueSet.end());
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
 * 比较两个查询的顺序，用于带修改莫队算法的排序
 * 按照块号、右端点块号、时间戳进行排序
 */
bool compareQueries(const Query& q1, const Query& q2) {
    if (q1.blockL != q2.blockL) {
        return q1.blockL < q2.blockL;
    }
    if (q1.blockR != q2.blockR) {
        return q1.blockR < q2.blockR;
    }
    return q1.t < q2.t;
}

/**
 * 应用修改操作
 */
void applyModification(int t, vector<int>& discreteArr, int curL, int curR, 
                      vector<int>& count, const vector<Modification>& modifications, 
                      const unordered_map<int, int>& valueToId, int& currentResult) {
    const Modification& mod = modifications[t];
    int pos = mod.pos;
    int oldVal = mod.oldVal;
    int newVal = mod.newVal;
    
    // 获取离散化的值
    auto oldIdIt = valueToId.find(oldVal);
    auto newIdIt = valueToId.find(newVal);
    int oldId = oldIdIt != valueToId.end() ? oldIdIt->second : -1;
    int newId = newIdIt != valueToId.end() ? newIdIt->second : -1;
    
    // 如果修改的位置在当前区间内，需要更新计数
    if (pos >= curL && pos <= curR) {
        if (oldId != -1) {
            count[oldId]--;
            if (count[oldId] == 0) {
                currentResult--;
            }
        }
        
        if (newId != -1) {
            count[newId]++;
            if (count[newId] == 1) {
                currentResult++;
            }
        }
    }
    
    // 更新离散化数组
    discreteArr[pos] = newId;
}

/**
 * 撤销修改操作
 */
void undoModification(int t, vector<int>& discreteArr, int curL, int curR, 
                     vector<int>& count, const vector<Modification>& modifications, 
                     const unordered_map<int, int>& valueToId, int& currentResult) {
    const Modification& mod = modifications[t];
    int pos = mod.pos;
    int oldVal = mod.oldVal;
    int newVal = mod.newVal;
    
    // 获取离散化的值
    auto oldIdIt = valueToId.find(oldVal);
    auto newIdIt = valueToId.find(newVal);
    int oldId = oldIdIt != valueToId.end() ? oldIdIt->second : -1;
    int newId = newIdIt != valueToId.end() ? newIdIt->second : -1;
    
    // 如果修改的位置在当前区间内，需要更新计数
    if (pos >= curL && pos <= curR) {
        if (newId != -1) {
            count[newId]--;
            if (count[newId] == 0) {
                currentResult--;
            }
        }
        
        if (oldId != -1) {
            count[oldId]++;
            if (count[oldId] == 1) {
                currentResult++;
            }
        }
    }
    
    // 更新离散化数组
    discreteArr[pos] = oldId;
}

/**
 * 主解题函数
 */
vector<int> solveMoWithModifications(const vector<int>& arr, 
                                    const vector<vector<int>>& queriesInput, 
                                    const vector<vector<int>>& modificationsInput) {
    // 异常处理
    if (arr.empty() || queriesInput.empty()) {
        return {};
    }
    
    int n = arr.size();
    int m = queriesInput.size();
    int k = modificationsInput.size();
    
    // 计算块的大小（最优为 n^(2/3)）
    int blockSize = static_cast<int>(pow(n, 2.0 / 3)) + 1;
    
    // 创建原始数组的副本，用于记录修改
    vector<int> originalArr(arr.begin(), arr.end());
    
    // 创建修改对象
    vector<Modification> modifications;
    for (int i = 0; i < k; i++) {
        int pos = modificationsInput[i][0] - 1; // 转换为0-based
        int newVal = modificationsInput[i][1];
        int oldVal = originalArr[pos];
        modifications.emplace_back(pos, oldVal, newVal);
        originalArr[pos] = newVal; // 更新原始数组用于下一次修改
    }
    
    // 离散化处理
    vector<int> discreteArr;
    unordered_map<int, int> valueToId;
    int valueRange = discretize(arr, modifications, discreteArr, valueToId);
    
    // 创建查询对象
    vector<Query> queries;
    for (int i = 0; i < m; i++) {
        // 假设输入是1-based的，转换为0-based
        int l = queriesInput[i][0] - 1;
        int r = queriesInput[i][1] - 1;
        int t = queriesInput[i][2]; // 时间戳（从0开始）
        queries.emplace_back(l, r, t, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化结果数组
    vector<int> answers(m, 0);
    
    // 使用数组计数
    vector<int> count(valueRange, 0);
    int currentResult = 0;  // 当前区间内不同数的数量
    
    // 初始化当前区间的左右指针和时间戳
    int curL = 0;
    int curR = -1;
    int curT = 0;
    
    // 处理每个查询
    for (const Query& q : queries) {
        int l = q.l;
        int r = q.r;
        int t = q.t;
        int idx = q.idx;
        
        // 调整时间戳到目标时间
        while (curT < t) {
            applyModification(curT, discreteArr, curL, curR, count, modifications, valueToId, currentResult);
            curT++;
        }
        while (curT > t) {
            curT--;
            undoModification(curT, discreteArr, curL, curR, count, modifications, valueToId, currentResult);
        }
        
        // 调整左右指针到目标位置
        // 向右扩展右端点
        while (curR < r) {
            curR++;
            int numId = discreteArr[curR];
            if (numId != -1) {
                count[numId]++;
                if (count[numId] == 1) {
                    currentResult++;
                }
            }
        }
        
        // 向左收缩右端点
        while (curR > r) {
            int numId = discreteArr[curR];
            if (numId != -1) {
                count[numId]--;
                if (count[numId] == 0) {
                    currentResult--;
                }
            }
            curR--;
        }
        
        // 向左扩展左端点
        while (curL > l) {
            curL--;
            int numId = discreteArr[curL];
            if (numId != -1) {
                count[numId]++;
                if (count[numId] == 1) {
                    currentResult++;
                }
            }
        }
        
        // 向右收缩左端点
        while (curL < l) {
            int numId = discreteArr[curL];
            if (numId != -1) {
                count[numId]--;
                if (count[numId] == 0) {
                    currentResult--;
                }
            }
            curL++;
        }
        
        // 保存当前查询的结果
        answers[idx] = currentResult;
    }
    
    return answers;
}

/**
 * 使用哈希表的版本，适用于数值范围较大的情况
 */
vector<int> solveMoWithModificationsHash(const vector<int>& arr, 
                                        const vector<vector<int>>& queriesInput, 
                                        const vector<vector<int>>& modificationsInput) {
    // 异常处理
    if (arr.empty() || queriesInput.empty()) {
        return {};
    }
    
    int n = arr.size();
    int m = queriesInput.size();
    int k = modificationsInput.size();
    
    // 计算块的大小（最优为 n^(2/3)）
    int blockSize = static_cast<int>(pow(n, 2.0 / 3)) + 1;
    
    // 创建原始数组的副本，用于记录修改
    vector<int> originalArr(arr.begin(), arr.end());
    
    // 创建修改对象
    vector<Modification> modifications;
    for (int i = 0; i < k; i++) {
        int pos = modificationsInput[i][0] - 1; // 转换为0-based
        int newVal = modificationsInput[i][1];
        int oldVal = originalArr[pos];
        modifications.emplace_back(pos, oldVal, newVal);
        originalArr[pos] = newVal; // 更新原始数组用于下一次修改
    }
    
    // 创建查询对象
    vector<Query> queries;
    for (int i = 0; i < m; i++) {
        // 假设输入是1-based的，转换为0-based
        int l = queriesInput[i][0] - 1;
        int r = queriesInput[i][1] - 1;
        int t = queriesInput[i][2]; // 时间戳（从0开始）
        queries.emplace_back(l, r, t, i, blockSize);
    }
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化结果数组
    vector<int> answers(m, 0);
    
    // 使用哈希表计数
    unordered_map<int, int> countMap;
    int currentResult = 0;  // 当前区间内不同数的数量
    
    // 初始化当前区间的左右指针和时间戳
    int curL = 0;
    int curR = -1;
    int curT = 0;
    
    // 定义应用修改的内部函数
    auto applyMod = [&](int t) {
        const Modification& mod = modifications[t];
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            countMap[oldVal]--;
            if (countMap[oldVal] == 0) {
                currentResult--;
            }
            
            countMap[newVal]++;
            if (countMap[newVal] == 1) {
                currentResult++;
            }
        }
        
        // 更新数组
        originalArr[pos] = newVal;
    };
    
    // 定义撤销修改的内部函数
    auto undoMod = [&](int t) {
        const Modification& mod = modifications[t];
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            countMap[newVal]--;
            if (countMap[newVal] == 0) {
                currentResult--;
            }
            
            countMap[oldVal]++;
            if (countMap[oldVal] == 1) {
                currentResult++;
            }
        }
        
        // 更新数组
        originalArr[pos] = oldVal;
    };
    
    // 处理每个查询
    for (const Query& q : queries) {
        int l = q.l;
        int r = q.r;
        int t = q.t;
        int idx = q.idx;
        
        // 调整时间戳到目标时间
        while (curT < t) {
            applyMod(curT);
            curT++;
        }
        while (curT > t) {
            curT--;
            undoMod(curT);
        }
        
        // 调整左右指针到目标位置
        // 向右扩展右端点
        while (curR < r) {
            curR++;
            int num = originalArr[curR];
            countMap[num]++;
            if (countMap[num] == 1) {
                currentResult++;
            }
        }
        
        // 向左收缩右端点
        while (curR > r) {
            int num = originalArr[curR];
            countMap[num]--;
            if (countMap[num] == 0) {
                currentResult--;
            }
            curR--;
        }
        
        // 向左扩展左端点
        while (curL > l) {
            curL--;
            int num = originalArr[curL];
            countMap[num]++;
            if (countMap[num] == 1) {
                currentResult++;
            }
        }
        
        // 向右收缩左端点
        while (curL < l) {
            int num = originalArr[curL];
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
 * 主函数，用于测试
 */
int main() {
    // 测试用例
    vector<int> arr = {1, 2, 1, 3, 4, 2, 5};
    
    // 查询列表：每个查询为[l, r, t]，表示在第t次修改后查询区间[l, r]
    vector<vector<int>> queries = {
        {1, 5, 0},  // 查询区间[1,5]在第0次修改后（即初始状态）
        {2, 6, 1},  // 查询区间[2,6]在第1次修改后
        {3, 7, 2}   // 查询区间[3,7]在第2次修改后
    };
    
    // 修改列表：每个修改为[pos, newVal]，表示将位置pos的值修改为newVal
    vector<vector<int>> modifications = {
        {2, 6},     // 将位置2的值修改为6
        {4, 7},     // 将位置4的值修改为7
        {6, 8}      // 将位置6的值修改为8
    };
    
    // 使用离散化版本
    vector<int> results = solveMoWithModifications(arr, queries, modifications);
    
    // 输出结果
    cout << "Query Results:" << endl;
    for (int result : results) {
        cout << result << endl;
    }
    
    // 验证两种方法结果一致
    vector<int> results2 = solveMoWithModificationsHash(arr, queries, modifications);
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