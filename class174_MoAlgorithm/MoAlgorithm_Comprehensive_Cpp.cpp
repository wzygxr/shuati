#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <map>
#include <stack>
#include <cstring>
using namespace std;

// ======================= 普通莫队算法 =======================
// 区间不同数个数问题 (DQUERY)
class ClassicMoAlgorithm {
public:
    struct Query {
        int l, r, index;
        int block;
    };
    
    static vector<int> solve(vector<int>& a, vector<pair<int, int>>& queries) {
        int n = a.size();
        int q = queries.size();
        int block_size = sqrt(n) + 1;
        
        vector<Query> qs(q);
        for (int i = 0; i < q; i++) {
            qs[i].l = queries[i].first - 1; // 转换为0-based
            qs[i].r = queries[i].second - 1;
            qs[i].index = i;
            qs[i].block = qs[i].l / block_size;
        }
        
        // 排序：按左端点所在块排序，同一块内右端点升序
        sort(qs.begin(), qs.end(), [block_size](const Query& q1, const Query& q2) {
            if (q1.block != q2.block) {
                return q1.block < q2.block;
            }
            // 奇偶排序优化
            return q1.block % 2 == 0 ? q1.r < q2.r : q1.r > q2.r;
        });
        
        vector<int> ans(q);
        vector<int> count(1e6 + 1, 0); // 假设数值范围不超过1e6
        int current_ans = 0;
        int current_l = 0, current_r = -1;
        
        // 离散化 a 数组（可选优化，这里暂不实现）
        
        auto add = [&](int pos) {
            if (count[a[pos]] == 0) {
                current_ans++;
            }
            count[a[pos]]++;
        };
        
        auto remove = [&](int pos) {
            count[a[pos]]--;
            if (count[a[pos]] == 0) {
                current_ans--;
            }
        };
        
        for (auto& query : qs) {
            while (current_l > query.l) add(--current_l);
            while (current_r < query.r) add(++current_r);
            while (current_l < query.l) remove(current_l++);
            while (current_r > query.r) remove(current_r--);
            ans[query.index] = current_ans;
        }
        
        return ans;
    }
};

// ======================= 带修改莫队算法 =======================
class MoWithModifications {
public:
    struct Query {
        int l, r, t, index;
        int block;
    };
    
    struct Modification {
        int pos, old_val, new_val;
    };
    
    static vector<int> solve(vector<int>& a, vector<pair<int, int>>& queries, 
                            vector<tuple<int, int, int>>& modifications) {
        int n = a.size();
        int q = queries.size();
        int m = modifications.size();
        int block_size = pow(n, 2.0 / 3.0) + 1;
        
        vector<Query> qs(q);
        for (int i = 0; i < q; i++) {
            qs[i].l = queries[i].first - 1;
            qs[i].r = queries[i].second - 1;
            qs[i].t = 0; // 初始时间戳
            qs[i].index = i;
            qs[i].block = qs[i].l / block_size;
        }
        
        vector<Modification> mods(m);
        for (int i = 0; i < m; i++) {
            mods[i].pos = get<0>(modifications[i]) - 1;
            mods[i].old_val = a[mods[i].pos];
            mods[i].new_val = get<1>(modifications[i]);
            // 更新原数组用于下一次修改
            a[mods[i].pos] = get<1>(modifications[i]);
        }
        
        // 恢复原数组
        for (int i = m - 1; i >= 0; i--) {
            a[mods[i].pos] = mods[i].old_val;
        }
        
        // 排序查询
        sort(qs.begin(), qs.end(), [block_size](const Query& q1, const Query& q2) {
            if (q1.block != q2.block) return q1.block < q2.block;
            int block_r1 = q1.r / block_size;
            int block_r2 = q2.r / block_size;
            if (block_r1 != block_r2) return block_r1 < block_r2;
            return q1.t < q2.t;
        });
        
        vector<int> ans(q);
        vector<int> count(1e6 + 1, 0);
        int current_ans = 0;
        int current_l = 0, current_r = -1;
        int current_t = 0;
        vector<int> arr = a; // 复制原数组用于修改
        
        auto add = [&](int pos) {
            if (count[arr[pos]] == 0) {
                current_ans++;
            }
            count[arr[pos]]++;
        };
        
        auto remove = [&](int pos) {
            count[arr[pos]]--;
            if (count[arr[pos]] == 0) {
                current_ans--;
            }
        };
        
        auto applyModification = [&](int t) {
            Modification& mod = mods[t];
            if (mod.pos >= current_l && mod.pos <= current_r) {
                remove(mod.pos);
            }
            swap(arr[mod.pos], mod.new_val);
            if (mod.pos >= current_l && mod.pos <= current_r) {
                add(mod.pos);
            }
        };
        
        auto undoModification = [&](int t) {
            Modification& mod = mods[t];
            if (mod.pos >= current_l && mod.pos <= current_r) {
                remove(mod.pos);
            }
            swap(arr[mod.pos], mod.new_val);
            if (mod.pos >= current_l && mod.pos <= current_r) {
                add(mod.pos);
            }
        };
        
        for (auto& query : qs) {
            while (current_t < query.t) applyModification(current_t++);
            while (current_t > query.t) undoModification(--current_t);
            while (current_l > query.l) add(--current_l);
            while (current_r < query.r) add(++current_r);
            while (current_l < query.l) remove(current_l++);
            while (current_r > query.r) remove(current_r--);
            ans[query.index] = current_ans;
        }
        
        return ans;
    }
};

// ======================= 回滚莫队算法 =======================
class RollbackMoAlgorithm {
public:
    struct Query {
        int l, r, index;
        int block;
    };
    
    // 区间众数问题示例实现
    static vector<int> solve(vector<int>& a, vector<pair<int, int>>& queries) {
        int n = a.size();
        int q = queries.size();
        int block_size = sqrt(n) + 1;
        
        vector<Query> qs(q);
        for (int i = 0; i < q; i++) {
            qs[i].l = queries[i].first - 1;
            qs[i].r = queries[i].second - 1;
            qs[i].index = i;
            qs[i].block = qs[i].l / block_size;
        }
        
        sort(qs.begin(), qs.end(), [block_size](const Query& q1, const Query& q2) {
            if (q1.block != q2.block) return q1.block < q2.block;
            return q1.r < q2.r; // 同一块内右端点升序
        });
        
        vector<int> ans(q);
        vector<int> count(1e6 + 1, 0);
        int current_ans = 0;
        int current_block = -1;
        int r = -1;
        
        for (auto& query : qs) {
            if (query.block != current_block) {
                // 清空之前的数据
                memset(&count[0], 0, count.size() * sizeof(int));
                current_ans = 0;
                current_block = query.block;
                r = min((current_block + 1) * block_size - 1, n - 1);
            }
            
            // 如果查询完全在同一块内，直接暴力处理
            if (query.block == query.r / block_size) {
                int local_ans = 0;
                vector<int> local_count(1e6 + 1, 0);
                for (int i = query.l; i <= query.r; i++) {
                    local_count[a[i]]++;
                    local_ans = max(local_ans, local_count[a[i]]);
                }
                ans[query.index] = local_ans;
                continue;
            }
            
            // 右端点逐步扩展（只添加不删除）
            while (r < query.r) {
                r++;
                count[a[r]]++;
                current_ans = max(current_ans, count[a[r]]);
            }
            
            // 左端点使用临时数组回滚
            int temp_ans = current_ans;
            vector<int> temp_count(count.begin(), count.end());
            for (int i = query.l; i < (current_block + 1) * block_size; i++) {
                temp_count[a[i]]++;
                temp_ans = max(temp_ans, temp_count[a[i]]);
            }
            ans[query.index] = temp_ans;
        }
        
        return ans;
    }
};

// ======================= 树上莫队算法 =======================
class TreeMoAlgorithm {
public:
    struct Query {
        int l, r, lca, index;
        int block;
    };
    
    // 树的邻接表表示
    struct Edge {
        int to, next;
    };
    
    static vector<int> solve(vector<int>& values, vector<vector<int>>& edges, 
                           vector<pair<int, int>>& queries) {
        int n = values.size();
        int q = queries.size();
        
        // 构建树的邻接表
        vector<vector<int>> adj(n);
        for (auto& edge : edges) {
            int u = edge[0] - 1;
            int v = edge[1] - 1;
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        // 预处理：欧拉序和LCA所需信息
        vector<int> in(n), out(n), depth(n), parent(n, -1);
        vector<vector<int>> up(n, vector<int>(log2(n) + 1, -1));
        vector<int> euler;
        int time_stamp = 0;
        
        // DFS预处理
        function<void(int, int)> dfs = [&](int u, int p) {
            in[u] = ++time_stamp;
            euler.push_back(u);
            parent[u] = p;
            depth[u] = depth[p] + 1;
            up[u][0] = p;
            for (int k = 1; k <= log2(n); k++) {
                if (up[u][k-1] != -1) {
                    up[u][k] = up[up[u][k-1]][k-1];
                }
            }
            for (int v : adj[u]) {
                if (v != p) {
                    dfs(v, u);
                }
            }
            out[u] = time_stamp;
            euler.push_back(u);
        };
        
        dfs(0, -1); // 假设根节点为0
        
        // LCA函数
        auto lca = [&](int u, int v) {
            if (depth[u] < depth[v]) swap(u, v);
            for (int k = log2(n); k >= 0; k--) {
                if (depth[u] - (1 << k) >= depth[v]) {
                    u = up[u][k];
                }
            }
            if (u == v) return u;
            for (int k = log2(n); k >= 0; k--) {
                if (up[u][k] != -1 && up[u][k] != up[v][k]) {
                    u = up[u][k];
                    v = up[v][k];
                }
            }
            return up[u][0];
        };
        
        // 转换树查询为区间查询
        vector<Query> qs(q);
        for (int i = 0; i < q; i++) {
            int u = queries[i].first - 1;
            int v = queries[i].second - 1;
            if (in[u] > in[v]) swap(u, v);
            int ancestor = lca(u, v);
            
            if (ancestor == u) {
                qs[i].l = in[u];
                qs[i].r = in[v];
                qs[i].lca = -1;
            } else {
                qs[i].l = out[u];
                qs[i].r = in[v];
                qs[i].lca = ancestor;
            }
            qs[i].index = i;
        }
        
        int block_size = sqrt(euler.size()) + 1;
        for (auto& query : qs) {
            query.block = query.l / block_size;
        }
        
        // 排序查询
        sort(qs.begin(), qs.end(), [block_size](const Query& q1, const Query& q2) {
            if (q1.block != q2.block) return q1.block < q2.block;
            return q1.block % 2 == 0 ? q1.r < q2.r : q1.r > q2.r;
        });
        
        vector<int> ans(q);
        vector<int> count(1e6 + 1, 0);
        vector<bool> in_range(n, false);
        int current_ans = 0;
        int current_l = 1, current_r = 0; // 欧拉序从1开始
        
        auto toggle = [&](int u) {
            if (in_range[u]) {
                count[values[u]]--;
                if (count[values[u]] == 0) {
                    current_ans--;
                }
            } else {
                if (count[values[u]] == 0) {
                    current_ans++;
                }
                count[values[u]]++;
            }
            in_range[u] = !in_range[u];
        };
        
        for (auto& query : qs) {
            while (current_l > query.l) toggle(euler[--current_l - 1]);
            while (current_r < query.r) toggle(euler[current_r++]);
            while (current_l < query.l) toggle(euler[current_l++ - 1]);
            while (current_r > query.r) toggle(euler[--current_r]);
            
            if (query.lca != -1) {
                toggle(query.lca);
            }
            
            ans[query.index] = current_ans;
            
            if (query.lca != -1) {
                toggle(query.lca);
            }
        }
        
        return ans;
    }
};

// 测试代码
int main() {
    cout << "===== 测试普通莫队算法 ======" << endl;
    {
        vector<int> a = {1, 2, 1, 3, 2, 4, 1, 5};
        vector<pair<int, int>> queries = {{1, 4}, {2, 6}, {3, 8}, {1, 8}};
        vector<int> ans = ClassicMoAlgorithm::solve(a, queries);
        cout << "区间不同数个数结果: ";
        for (int x : ans) cout << x << " ";
        cout << endl;
    }
    
    cout << "\n===== 测试带修改莫队算法 ======" << endl;
    {
        vector<int> a = {1, 2, 1, 3, 2};
        vector<pair<int, int>> queries = {{1, 3}, {2, 5}, {1, 5}};
        vector<tuple<int, int, int>> modifications = {{2, 3, 1}, {4, 3, 4}}; // (位置, 旧值, 新值)
        vector<int> ans = MoWithModifications::solve(a, queries, modifications);
        cout << "带修改莫队结果: ";
        for (int x : ans) cout << x << " ";
        cout << endl;
    }
    
    cout << "\n===== 测试回滚莫队算法 ======" << endl;
    {
        vector<int> a = {1, 2, 1, 3, 2, 1, 4};
        vector<pair<int, int>> queries = {{1, 4}, {2, 6}, {1, 7}};
        vector<int> ans = RollbackMoAlgorithm::solve(a, queries);
        cout << "区间众数出现次数结果: ";
        for (int x : ans) cout << x << " ";
        cout << endl;
    }
    
    cout << "\n===== 测试树上莫队算法 ======" << endl;
    {
        vector<int> values = {1, 2, 3, 1, 2, 4}; // 每个节点的值
        vector<vector<int>> edges = {{1,2}, {1,3}, {2,4}, {2,5}, {3,6}}; // 边
        vector<pair<int, int>> queries = {{1,5}, {4,6}, {2,3}}; // 路径查询
        vector<int> ans = TreeMoAlgorithm::solve(values, edges, queries);
        cout << "树上路径不同值个数结果: ";
        for (int x : ans) cout << x << " ";
        cout << endl;
    }
    
    return 0;
}