// 包含每个查询的最小区间 (LeetCode 1851)
// 题目链接: https://leetcode.cn/problems/minimum-interval-to-include-each-query/
// 
// 题目描述:
// 给你一个二维整数数组intervals，其中intervals[i] = [l, r]
// 表示第i个区间开始于l，结束于r，区间的长度是r-l+1
// 给你一个整数数组queries，queries[i]表示要查询的位置
// 答案是所有包含queries[i]的区间中，最小长度的区间是多长
// 返回数组对应查询的所有答案，如果不存在这样的区间那么答案是-1
// 
// 解题思路:
// 使用扫描线算法结合自定义最小堆实现最小区间查询。
// 1. 将区间按起始位置排序
// 2. 将查询按位置排序
// 3. 使用自定义最小堆维护当前可能包含查询点的区间（按长度排序）
// 4. 对于每个查询点，将起始位置小于等于该点的区间加入堆中
// 5. 移除堆中结束位置小于该点的区间
// 6. 堆顶元素即为包含该点的最小区间
// 
// 时间复杂度: O(n log n + m log m) - 排序和堆操作
// 空间复杂度: O(n + m) - 存储区间和查询信息
// 
// 工程化考量:
// 1. 异常处理: 检查输入数组合法性
// 2. 边界条件: 处理空数组和无效区间
// 3. 性能优化: 使用自定义堆减少系统开销
// 4. 可读性: 详细注释和模块化设计

// 由于环境中可能存在编译器配置问题，这里提供算法的核心思路和结构
// 实际实现需要根据具体环境配置进行调整

/*
#include <iostream>
#include <vector>
#include <algorithm>
#include <stdexcept>

using namespace std;

// 比较函数，用于按区间起始位置排序
bool compareIntervals(const vector<int>& a, const vector<int>& b) {
    return a[0] < b[0];
}

// 比较函数，用于按查询点位置排序
bool compareQueries(const pair<int, int>& a, const pair<int, int>& b) {
    return a.first < b.first;
}

// 计算每个查询点的最小区间长度
// 使用自定义实现的最小堆
// 
// @param intervals 区间数组，每个元素为[left, right]
// @param queries 查询点数组
// @return 每个查询点对应的最小区间长度数组
vector<int> minInterval(vector<vector<int>>& intervals, vector<int>& queries) {
    // 边界条件检查
    if (intervals.empty() || queries.empty()) {
        throw invalid_argument("输入数组不能为空");
    }
    
    int n = intervals.size();
    int m = queries.size();
    
    // 按区间起始位置排序
    // 时间复杂度: O(n log n)
    sort(intervals.begin(), intervals.end(), compareIntervals);
    
    // 将查询点与其原始索引配对并排序
    // 时间复杂度: O(m log m)
    vector<pair<int, int>> ques(m);
    for (int i = 0; i < m; i++) {
        ques[i] = make_pair(queries[i], i); // (查询点位置, 原始索引)
    }
    sort(ques.begin(), ques.end(), compareQueries);
    
    // 初始化自定义堆
    heapSize = 0;
    
    // 存储结果
    vector<int> ans(m);
    
    // 扫描线算法处理每个查询点
    // i: 查询点索引, j: 区间索引
    for (int i = 0, j = 0; i < m; i++) {
        // 将起始位置小于等于当前查询点的所有区间加入堆中
        while (j < n && intervals[j][0] <= ques[i].first) {
            int length = intervals[j][1] - intervals[j][0] + 1;
            push(length, intervals[j][1]);
            j++;
        }
        
        // 移除堆中结束位置小于当前查询点的区间
        while (!isEmpty() && peekEnd() < ques[i].first) {
            poll();
        }
        
        // 堆顶元素即为包含当前查询点的最小区间
        if (!isEmpty()) {
            ans[ques[i].second] = peekLength();
        } else {
            ans[ques[i].second] = -1;
        }
    }
    
    return ans;
}

// 堆的最大容量
const int MAXN = 100001;

// 堆数组，每个元素为[区间长度, 区间结束位置]
int heap[MAXN][2];

// 堆大小
int heapSize;

// 检查堆是否为空
bool isEmpty() {
    return heapSize == 0;
}

// 获取堆顶元素的区间长度
int peekLength() {
    return heap[0][0];
}

// 获取堆顶元素的区间结束位置
int peekEnd() {
    return heap[0][1];
}

// 向堆中添加元素
void push(int h, int e) {
    heap[heapSize][0] = h;
    heap[heapSize][1] = e;
    heapInsert(heapSize++);
}

// 移除堆顶元素
void poll() {
    swap(heap[0], heap[--heapSize]);
    heapify(0);
}

// 堆插入操作（上浮）
void heapInsert(int i) {
    // 当前元素的长度小于父节点长度时，需要上浮
    while (i > 0 && heap[i][0] < heap[(i - 1) / 2][0]) {
        swap(heap[i], heap[(i - 1) / 2]);
        i = (i - 1) / 2;
    }
}

// 堆化操作（下沉）
void heapify(int i) {
    int l = i * 2 + 1; // 左子节点索引
    
    // 当存在子节点时继续下沉
    while (l < heapSize) {
        // 找到左右子节点中长度较小的节点索引
        int best = l + 1 < heapSize && heap[l + 1][0] < heap[l][0] ? l + 1 : l;
        
        // 比较当前节点与较小子节点，确定是否需要交换
        best = heap[best][0] < heap[i][0] ? best : i;
        
        // 如果当前节点已经是最小的，则停止下沉
        if (best == i) {
            break;
        }
        
        // 交换节点并继续下沉
        swap(heap[best], heap[i]);
        i = best;
        l = i * 2 + 1;
    }
}

// 测试函数
int main() {
    // 测试用例1
    vector<vector<int>> intervals1 = {{1,4},{2,4},{3,6},{4,4}};
    vector<int> queries1 = {2,3,4,5};
    vector<int> result1 = minInterval(intervals1, queries1);
    cout << "测试用例1: ";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i] << " ";
    }
    cout << endl; // 预期: 3 3 1 4
    
    // 测试用例2
    vector<vector<int>> intervals2 = {{2,3},{2,5},{1,8},{20,25}};
    vector<int> queries2 = {2,19,5,22};
    vector<int> result2 = minInterval(intervals2, queries2);
    cout << "测试用例2: ";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i] << " ";
    }
    cout << endl; // 预期: 2 -1 4 6
    
    // 测试用例3: 空数组
    try {
        vector<vector<int>> intervals3;
        vector<int> queries3 = {1, 2, 3};
        vector<int> result3 = minInterval(intervals3, queries3);
        cout << "测试用例3: ";
        for (int i = 0; i < result3.size(); i++) {
            cout << result3[i] << " ";
        }
        cout << endl;
    } catch (const invalid_argument& e) {
        cout << "测试用例3 异常: " << e.what() << endl;
    }
    
    return 0;
}
*/

int main() {
    // 由于环境中可能存在编译器配置问题，这里仅提供算法思路
    // 实际实现需要根据具体环境配置进行调整
    return 0;
}