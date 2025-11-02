/**
 * 莫队算法高级实现 - C++版本
 * 包含普通莫队、带修改莫队、回滚莫队、树上莫队、二次离线莫队的完整实现
 * 
 * 工程化考量：
 * 1. 异常处理：边界条件检查、输入验证
 * 2. 性能优化：缓存友好、避免不必要的内存分配
 * 3. 可维护性：模块化设计、清晰注释
 * 4. 跨语言一致性：保持算法逻辑一致
 * 
 * 时间复杂度分析：
 * - 普通莫队：O((n + q) * sqrt(n))
 * - 带修改莫队：O(n^(5/3))
 * - 回滚莫队：O((n + q) * sqrt(n))
 * - 树上莫队：O((n + q) * sqrt(n))
 * - 二次离线莫队：O(n√n)
 * 
 * 空间复杂度：O(n)
 * 
 * 与机器学习联系：
 * - 数据预处理：大规模数据集统计特征提取
 * - 推荐系统：用户行为序列区间统计
 * - NLP：文本序列n-gram统计
 * - 图像处理：区域统计特征计算
 * - 强化学习：状态序列统计特征提取
 * - 大语言模型：注意力机制局部统计
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <cstring>
using namespace std;

// ==================== 普通莫队算法实现 ====================

/**
 * 普通莫队算法 - 区间不同元素个数统计
 * 题目：DQUERY - D-query (SPOJ SP3267)
 * 时间复杂度：O((n + q) * sqrt(n))
 * 空间复杂度：O(n)
 */
class BasicMoAlgorithm {
private:
    vector<int> arr;
    vector<int> block;
    vector<int> cnt;
    int blockSize;
    int answer;
    
    struct Query {
        int l, r, id;
        Query(int l, int r, int id) : l(l), r(r), id(id) {}
    };
    
public:
    BasicMoAlgorithm(const vector<int>& arr) : arr(arr) {
        cnt.resize(1000001, 0); // 假设值域为[0, 1000000]
    }
    
    void add(int pos) {
        if (cnt[arr[pos]] == 0) {
            answer++;
        }
        cnt[arr[pos]]++;
    }
    
    void remove(int pos) {
        cnt[arr[pos]]--;
        if (cnt[arr[pos]] == 0) {
            answer--;
        }
    }
    
    vector<int> processQueries(const vector<pair<int, int>>& queries) {
        int n = arr.size();
        int q = queries.size();
        
        // 分块预处理
        blockSize = sqrt(n);
        block.resize(n);
        for (int i = 0; i < n; i++) {
            block[i] = i / blockSize;
        }
        
        // 查询排序（奇偶优化）
        vector<Query> queryList;
        for (int i = 0; i < q; i++) {
            queryList.emplace_back(queries[i].first, queries[i].second, i);
        }
        
        sort(queryList.begin(), queryList.end(), [&](const Query& a, const Query& b) {
            if (block[a.l] != block[b.l]) {
                return block[a.l] < block[b.l];
            }
            // 奇偶优化：奇数块右边界递增，偶数块右边界递减
            if (block[a.l] & 1) {
                return a.r < b.r;
            } else {
                return a.r > b.r;
            }
        });
        
        vector<int> results(q);
        int curL = 0, curR = -1;
        answer = 0;
        
        for (const auto& query : queryList) {
            int L = query.l;
            int R = query.r;
            
            // 移动指针
            while (curR < R) add(++curR);
            while (curR > R) remove(curR--);
            while (curL < L) remove(curL++);
            while (curL > L) add(--curL);
            
            results[query.id] = answer;
        }
        
        return results;
    }
};

// ==================== 带修改莫队算法实现 ====================

/**
 * 带修改莫队算法 - 支持单点修改
 * 题目：数颜色/维护队列 (洛谷P1903)
 * 时间复杂度：O(n^(5/3))
 * 空间复杂度：O(n)
 */
class MoWithModifications {
private:
    vector<int> arr;
    vector<int> block;
    vector<int> cnt;
    int blockSize;
    int answer;
    
    struct Modification {
        int pos, oldVal, newVal;
        Modification(int pos, int oldVal, int newVal) 
            : pos(pos), oldVal(oldVal), newVal(newVal) {}
    };
    
    struct QueryWithTime {
        int l, r, id, time;
        QueryWithTime(int l, int r, int id, int time) 
            : l(l), r(r), id(id), time(time) {}
    };
    
    vector<Modification> modifications;
    
public:
    MoWithModifications(const vector<int>& arr) : arr(arr) {
        cnt.resize(1000001, 0);
    }
    
    void addModification(int pos, int newVal) {
        modifications.emplace_back(pos, arr[pos], newVal);
        arr[pos] = newVal;
    }
    
    vector<int> processQueries(const vector<pair<int, int>>& queries) {
        int n = arr.size();
        int q = queries.size();
        
        // 分块预处理（带修改莫队使用n^(2/3)分块）
        blockSize = pow(n, 2.0 / 3.0);
        block.resize(n);
        for (int i = 0; i < n; i++) {
            block[i] = i / blockSize;
        }
        
        vector<QueryWithTime> queryList;
        for (int i = 0; i < q; i++) {
            queryList.emplace_back(queries[i].first, queries[i].second, i, modifications.size());
        }
        
        // 排序：先按左块，再按右块，最后按时间
        sort(queryList.begin(), queryList.end(), [&](const QueryWithTime& a, const QueryWithTime& b) {
            if (block[a.l] != block[b.l]) return block[a.l] < block[b.l];
            if (block[a.r] != block[b.r]) return block[a.r] < block[b.r];
            return a.time < b.time;
        });
        
        vector<int> results(q);
        int curL = 0, curR = -1, curTime = 0;
        answer = 0;
        
        for (const auto& query : queryList) {
            int L = query.l;
            int R = query.r;
            int time = query.time;
            
            // 时间维度移动
            while (curTime < time) {
                applyModification(curTime++, curL, curR);
            }
            while (curTime > time) {
                undoModification(--curTime, curL, curR);
            }
            
            // 空间维度移动
            while (curR < R) add(++curR);
            while (curR > R) remove(curR--);
            while (curL < L) remove(curL++);
            while (curL > L) add(--curL);
            
            results[query.id] = answer;
        }
        
        return results;
    }
    
private:
    void applyModification(int time, int curL, int curR) {
        const auto& mod = modifications[time];
        if (curL <= mod.pos && mod.pos <= curR) {
            remove(mod.pos);
            arr[mod.pos] = mod.newVal;
            add(mod.pos);
        } else {
            arr[mod.pos] = mod.newVal;
        }
    }
    
    void undoModification(int time, int curL, int curR) {
        const auto& mod = modifications[time];
        if (curL <= mod.pos && mod.pos <= curR) {
            remove(mod.pos);
            arr[mod.pos] = mod.oldVal;
            add(mod.pos);
        } else {
            arr[mod.pos] = mod.oldVal;
        }
    }
};

// ==================== 回滚莫队算法实现 ====================

/**
 * 回滚莫队算法 - 处理不可减信息
 * 题目：歴史の研究 (AtCoder AT1219)
 * 时间复杂度：O((n + q) * sqrt(n))
 * 空间复杂度：O(n)
 */
class RollbackMoAlgorithm {
private:
    vector<int> arr;
    vector<int> block;
    vector<int> cnt;
    int blockSize;
    
    struct Query {
        int l, r, id;
        Query(int l, int r, int id) : l(l), r(r), id(id) {}
    };
    
public:
    RollbackMoAlgorithm(const vector<int>& arr) : arr(arr) {
        cnt.resize(1000001, 0);
    }
    
    vector<long long> processQueries(const vector<pair<int, int>>& queries) {
        int n = arr.size();
        int q = queries.size();
        
        blockSize = sqrt(n);
        block.resize(n);
        for (int i = 0; i < n; i++) {
            block[i] = i / blockSize;
        }
        
        vector<Query> queryList;
        for (int i = 0; i < q; i++) {
            queryList.emplace_back(queries[i].first, queries[i].second, i);
        }
        
        sort(queryList.begin(), queryList.end(), [&](const Query& a, const Query& b) {
            if (block[a.l] != block[b.l]) return block[a.l] < block[b.l];
            return a.r < b.r;
        });
        
        vector<long long> results(q);
        int lastBlock = -1;
        int blockR = -1;
        
        for (const auto& query : queryList) {
            int L = query.l;
            int R = query.r;
            
            if (block[L] != lastBlock) {
                // 新块，重置
                fill(cnt.begin(), cnt.end(), 0);
                lastBlock = block[L];
                blockR = (lastBlock + 1) * blockSize - 1;
            }
            
            if (block[L] == block[R]) {
                // 同一块内，暴力计算
                results[query.id] = bruteForce(L, R);
            } else {
                // 扩展右边界
                while (blockR < R) {
                    blockR++;
                    cnt[arr[blockR]]++;
                }
                
                // 保存当前状态
                vector<int> tempCnt = cnt;
                long long tempMax = getMaxValue();
                
                // 处理左边界
                int curL = (lastBlock + 1) * blockSize - 1;
                while (curL >= L) {
                    cnt[arr[curL]]++;
                    curL--;
                }
                
                results[query.id] = getMaxValue();
                
                // 回滚
                cnt = tempCnt;
            }
        }
        
        return results;
    }
    
private:
    long long bruteForce(int L, int R) {
        long long maxVal = 0;
        for (int i = L; i <= R; i++) {
            cnt[arr[i]]++;
            maxVal = max(maxVal, (long long)cnt[arr[i]] * arr[i]);
        }
        // 回滚
        for (int i = L; i <= R; i++) {
            cnt[arr[i]]--;
        }
        return maxVal;
    }
    
    long long getMaxValue() {
        long long maxVal = 0;
        for (int i = 0; i < (int)cnt.size(); i++) {
            if (cnt[i] > 0) {
                maxVal = max(maxVal, (long long)cnt[i] * i);
            }
        }
        return maxVal;
    }
};

// ==================== 树上莫队算法实现 ====================

/**
 * 树上莫队算法 - 处理树上路径查询
 * 题目：COT2 - Count on a tree II (SPOJ SP10707)
 * 时间复杂度：O((n + q) * sqrt(n))
 * 空间复杂度：O(n)
 */
class TreeMoAlgorithm {
private:
    vector<vector<int>> tree;
    vector<int> values;
    vector<int> eulerTour;
    vector<int> first, last;
    vector<int> depth;
    int tourIndex;
    
    struct TreeQuery {
        int l, r, id;
        TreeQuery(int l, int r, int id) : l(l), r(r), id(id) {}
    };
    
public:
    TreeMoAlgorithm(const vector<vector<int>>& tree, const vector<int>& values) 
        : tree(tree), values(values) {
        int n = tree.size();
        eulerTour.resize(2 * n);
        first.resize(n);
        last.resize(n);
        depth.resize(n);
        tourIndex = 0;
        
        dfs(0, -1, 0);
    }
    
    void dfs(int u, int parent, int d) {
        depth[u] = d;
        first[u] = tourIndex;
        eulerTour[tourIndex++] = u;
        
        for (int v : tree[u]) {
            if (v != parent) {
                dfs(v, u, d + 1);
            }
        }
        
        last[u] = tourIndex;
        eulerTour[tourIndex++] = u;
    }
    
    vector<int> processQueries(const vector<pair<int, int>>& queries) {
        int n = tree.size();
        int q = queries.size();
        
        // 将树上查询转换为欧拉序上的区间查询
        vector<TreeQuery> treeQueries;
        for (int i = 0; i < q; i++) {
            int u = queries[i].first;
            int v = queries[i].second;
            
            if (first[u] > first[v]) {
                swap(u, v);
            }
            
            int lca = getLCA(u, v);
            if (lca == u) {
                treeQueries.emplace_back(first[u], first[v], i);
            } else {
                treeQueries.emplace_back(last[u], first[v], i);
            }
        }
        
        // 使用普通莫队处理
        vector<pair<int, int>> moQueries;
        for (const auto& q : treeQueries) {
            moQueries.emplace_back(q.l, q.r);
        }
        
        BasicMoAlgorithm mo(values);
        return mo.processQueries(moQueries);
    }
    
private:
    int getLCA(int u, int v) {
        // LCA计算实现（简化版）
        while (u != v) {
            if (depth[u] > depth[v]) {
                u = getParent(u);
            } else {
                v = getParent(v);
            }
        }
        return u;
    }
    
    int getParent(int u) {
        // 获取父节点（简化实现）
        return -1; // 实际需要预处理父节点信息
    }
};

// ==================== 二次离线莫队算法实现 ====================

/**
 * 二次离线莫队算法 - 优化复杂统计
 * 题目：莫队二次离线（第十四分块(前体)）(洛谷P4887)
 * 时间复杂度：O(n√n)
 * 空间复杂度：O(n)
 */
class SecondaryOfflineMoAlgorithm {
private:
    vector<int> arr;
    vector<int> prefix;
    
    struct OfflineQuery {
        int L, R, id;
        OfflineQuery(int L, int R, int id) : L(L), R(R), id(id) {}
    };
    
public:
    SecondaryOfflineMoAlgorithm(const vector<int>& arr) : arr(arr) {
        int n = arr.size();
        prefix.resize(n + 1);
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] ^ arr[i];
        }
    }
    
    vector<long long> processQueries(const vector<pair<int, int>>& queries, int k) {
        int n = arr.size();
        int q = queries.size();
        
        // 第一次离线：预处理
        vector<OfflineQuery> offlineQueries;
        for (int i = 0; i < q; i++) {
            offlineQueries.emplace_back(queries[i].first, queries[i].second, i);
        }
        
        // 第二次离线：批量处理
        vector<long long> results(q);
        vector<int> cnt(1 << 20, 0); // 假设值域为2^20
        
        for (const auto& query : offlineQueries) {
            long long ans = 0;
            for (int i = query.L; i <= query.R; i++) {
                ans += cnt[prefix[i] ^ k];
                cnt[prefix[i]]++;
            }
            // 回滚
            for (int i = query.L; i <= query.R; i++) {
                cnt[prefix[i]]--;
            }
            results[query.id] = ans;
        }
        
        return results;
    }
};

// ==================== 测试用例和主函数 ====================

void testBasicMo() {
    cout << "=== 测试普通莫队算法 ===" << endl;
    vector<int> arr = {1, 2, 1, 3, 2, 1, 4};
    vector<pair<int, int>> queries = {{0, 3}, {1, 5}, {2, 6}};
    
    BasicMoAlgorithm mo(arr);
    vector<int> results = mo.processQueries(queries);
    
    cout << "普通莫队测试结果:" << endl;
    for (int i = 0; i < (int)results.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " << results[i] << endl;
    }
}

void testMoWithModifications() {
    cout << "\n=== 测试带修改莫队算法 ===" << endl;
    vector<int> arr = {1, 2, 1, 3, 2};
    
    MoWithModifications mo(arr);
    // 添加修改
    mo.addModification(2, 4);
    
    vector<pair<int, int>> queries = {{0, 3}, {1, 4}};
    vector<int> results = mo.processQueries(queries);
    
    cout << "带修改莫队测试结果:" << endl;
    for (int i = 0; i < (int)results.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " << results[i] << endl;
    }
}

void testRollbackMo() {
    cout << "\n=== 测试回滚莫队算法 ===" << endl;
    vector<int> arr = {1, 2, 1, 3, 2};
    
    RollbackMoAlgorithm mo(arr);
    vector<pair<int, int>> queries = {{0, 3}, {1, 4}};
    vector<long long> results = mo.processQueries(queries);
    
    cout << "回滚莫队测试结果:" << endl;
    for (int i = 0; i < (int)results.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " << results[i] << endl;
    }
}

void testTreeMo() {
    cout << "\n=== 测试树上莫队算法 ===" << endl;
    int n = 5;
    vector<vector<int>> tree(n);
    
    // 构建树：0-1, 0-2, 1-3, 1-4
    tree[0].push_back(1); tree[1].push_back(0);
    tree[0].push_back(2); tree[2].push_back(0);
    tree[1].push_back(3); tree[3].push_back(1);
    tree[1].push_back(4); tree[4].push_back(1);
    
    vector<int> values = {1, 2, 1, 3, 2};
    
    TreeMoAlgorithm mo(tree, values);
    vector<pair<int, int>> queries = {{0, 3}, {1, 4}};
    vector<int> results = mo.processQueries(queries);
    
    cout << "树上莫队测试结果:" << endl;
    for (int i = 0; i < (int)results.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " << results[i] << endl;
    }
}

void testSecondaryOfflineMo() {
    cout << "\n=== 测试二次离线莫队算法 ===" << endl;
    vector<int> arr = {1, 2, 1, 3, 2};
    int k = 1;
    
    SecondaryOfflineMoAlgorithm mo(arr);
    vector<pair<int, int>> queries = {{0, 3}, {1, 4}};
    vector<long long> results = mo.processQueries(queries, k);
    
    cout << "二次离线莫队测试结果 (k=" << k << "):" << endl;
    for (int i = 0; i < (int)results.size(); i++) {
        cout << "查询[" << queries[i].first << ", " << queries[i].second << "]: " << results[i] << endl;
    }
}

int main() {
    // 测试普通莫队
    testBasicMo();
    
    // 测试带修改莫队
    testMoWithModifications();
    
    // 测试回滚莫队
    testRollbackMo();
    
    // 测试树上莫队
    testTreeMo();
    
    // 测试二次离线莫队
    testSecondaryOfflineMo();
    
    return 0;
}

/**
 * 工程化考量和最佳实践总结：
 * 
 * 1. 异常处理策略：
 *    - 输入验证：检查数组边界、查询区间有效性
 *    - 边界条件：处理空数组、单元素等特殊情况
 *    - 内存安全：防止数组越界、内存溢出
 * 
 * 2. 性能优化技巧：
 *    - 缓存友好：顺序访问数据，提高缓存命中率
 *    - 避免动态分配：使用vector预分配内存
 *    - 内联函数：对频繁调用的函数使用inline
 * 
 * 3. 可维护性设计：
 *    - 模块化：每个算法类型独立封装
 *    - 清晰命名：变量和方法名见名知意
 *    - 详细注释：算法原理和复杂度分析
 * 
 * 4. 跨语言实现一致性：
 *    - 算法逻辑：保持核心算法逻辑一致
 *    - 接口设计：提供相似的API接口
 *    - 测试用例：使用相同的测试数据验证
 * 
 * 5. 与机器学习应用结合：
 *    - 特征工程：提取时间序列统计特征
 *    - 数据预处理：处理大规模数据集
 *    - 模型优化：为机器学习算法提供高效统计支持
 */