#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

/**
 * POJ 2777 颜色出现次数统计问题的带修改莫队算法实现
 * 
 * 题目描述：
 * 给定一个长度为L的数组，每个元素代表一种颜色（用1到T之间的整数表示）。
 * 支持两种操作：
 * 1. C A B：将位置A的颜色改为B
 * 2. Q A B：查询区间[A,B]内有多少种不同的颜色
 * 
 * 解题思路：
 * 1. 这是一个支持单点修改和区间查询的问题，适合使用带修改莫队算法
 * 2. 带修改莫队在普通莫队的基础上增加了时间维度，对查询进行三维排序
 * 3. 维护当前区间内每种颜色的出现次数，以及当前不同颜色的数量
 * 
 * 时间复杂度分析：
 * - 带修改莫队的时间复杂度为 O(n^(5/3))，其中 n 是数组长度
 * - 空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理数组边界情况
 * 2. 性能优化：使用快速输入输出
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */

// 用于存储查询的结构
struct Query {
    int l;  // 查询的左边界
    int r;  // 查询的右边界
    int t;  // 查询的时间戳（即该查询之前有多少次修改操作）
    int idx;  // 查询的索引，用于输出答案时保持顺序
    
    Query(int l, int r, int t, int idx) : l(l), r(r), t(t), idx(idx) {}
};

// 用于存储修改操作的结构
struct Modify {
    int pos;  // 修改的位置
    int oldColor;  // 修改前的颜色
    int newColor;  // 修改后的颜色
    
    Modify(int pos, int oldColor, int newColor) : pos(pos), oldColor(oldColor), newColor(newColor) {}
};

// 全局变量
vector<int> colors;  // 数组的当前颜色
vector<int> originalColors;  // 保存原始颜色的数组，用于回滚操作
vector<Modify> modifies;  // 所有的修改操作
vector<Query> queries;  // 所有的查询操作
int blockSize;  // 块的大小
vector<int> count;  // 用于存储每种颜色出现的次数
int currentResult;  // 当前区间内不同颜色的数量
vector<int> answers;  // 答案数组

/**
 * 比较两个查询的顺序，用于带修改莫队算法的排序
 * 采用三维排序：块号 -> 右边界块号 -> 时间戳
 */
bool compareQueries(const Query& q1, const Query& q2) {
    // 首先按照左边界所在的块排序
    if (q1.l / blockSize != q2.l / blockSize) {
        return q1.l / blockSize < q2.l / blockSize;
    }
    // 对于同一块内的查询，按照右边界所在的块排序
    if (q1.r / blockSize != q2.r / blockSize) {
        return q1.r / blockSize < q2.r / blockSize;
    }
    // 对于同一块内且右边界也在同一块的查询，按照时间戳排序
    return q1.t < q2.t;
}

/**
 * 添加一个元素到当前区间
 */
void add(int pos) {
    int color = colors[pos];
    // 如果该颜色之前没有出现过，增加不同颜色的计数
    if (count[color] == 0) {
        currentResult++;
    }
    // 增加该颜色的出现次数
    count[color]++;
}

/**
 * 从当前区间移除一个元素
 */
void remove(int pos) {
    int color = colors[pos];
    // 减少该颜色的出现次数
    count[color]--;
    // 如果该颜色现在不再出现，减少不同颜色的计数
    if (count[color] == 0) {
        currentResult--;
    }
}

/**
 * 应用或回滚一个修改操作
 */
void applyModify(int modifyIdx, bool apply) {
    Modify& modify = modifies[modifyIdx];
    int pos = modify.pos;
    int oldColor = modify.oldColor;
    int newColor = modify.newColor;
    
    // 确定要切换的颜色
    int fromColor = apply ? oldColor : newColor;
    int toColor = apply ? newColor : oldColor;
    
    // 如果当前位置在查询区间内，需要更新统计
    if (pos >= queries[0].l && pos <= queries[0].r) {
        // 先移除旧颜色的影响
        remove(pos);
        // 更新颜色
        colors[pos] = toColor;
        // 再添加新颜色的影响
        add(pos);
    } else {
        // 如果当前位置不在查询区间内，直接更新颜色
        colors[pos] = toColor;
    }
}

/**
 * 主解题函数
 */
vector<int> solve(int L, int T, int O, vector<int>& initialColors, vector<pair<char, pair<int, int>>>& operations) {
    // 初始化数据结构
    colors = initialColors;
    originalColors = initialColors;
    modifies.clear();
    queries.clear();
    
    // 处理所有操作
    int queryIndex = 0;
    for (auto& op : operations) {
        char type = op.first;
        int A = op.second.first;
        int B = op.second.second;
        
        if (type == 'C') {
            // 修改操作
            modifies.emplace_back(A, colors[A], B);
            colors[A] = B;  // 立即应用修改，便于后续操作使用最新状态
        } else if (type == 'Q') {
            // 查询操作
            int t = modifies.empty() ? -1 : modifies.size() - 1;
            queries.emplace_back(A, B, t, queryIndex++);
        }
    }
    
    // 恢复原始颜色，因为我们需要重新应用修改
    colors = originalColors;
    
    // 计算块的大小，对于带修改莫队，通常取n^(2/3)
    blockSize = static_cast<int>(pow(L, 2.0 / 3.0)) + 1;
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化计数数组和答案数组
    count.assign(T + 2, 0);  // 颜色编号最大为T
    currentResult = 0;
    answers.assign(queries.size(), 0);
    
    // 初始化当前区间的左右指针和时间戳
    int curL = 1;
    int curR = 0;
    int curT = -1;
    
    // 处理每个查询
    for (auto& q : queries) {
        // 调整时间戳到目标时间
        while (curT < q.t) {
            applyModify(++curT, true);
        }
        while (curT > q.t) {
            applyModify(curT--, false);
        }
        
        // 调整左右指针到目标位置
        while (curL > q.l) add(--curL);
        while (curR < q.r) add(++curR);
        while (curL < q.l) remove(curL++);
        while (curR > q.r) remove(curR--);
        
        // 保存当前查询的结果
        answers[q.idx] = currentResult;
    }
    
    return answers;
}

/**
 * 主函数，用于测试
 */
int main() {
    // 测试用例
    int L = 10;
    int T = 3;
    int O = 4;
    // 位置从1开始，索引0不使用
    vector<int> initialColors = {0, 1, 2, 1, 3, 2, 1, 2, 3, 1, 2};
    
    vector<pair<char, pair<int, int>>> operations;
    operations.push_back({'Q', {1, 10}});
    operations.push_back({'C', {2, 3}});
    operations.push_back({'Q', {1, 10}});
    operations.push_back({'Q', {3, 6}});
    
    vector<int> results = solve(L, T, O, initialColors, operations);
    
    // 输出结果
    cout << "Query Results:" << endl;
    for (int result : results) {
        cout << result << endl;
    }
    
    return 0;
}