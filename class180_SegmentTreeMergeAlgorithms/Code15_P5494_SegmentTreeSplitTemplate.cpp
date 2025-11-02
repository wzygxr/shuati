// 测试链接 : https://www.luogu.com.cn/problem/P5494
// 线段树分裂模板题 - C++实现

#include <iostream>
#include <vector>
#include <algorithm>
#include <memory>
#include <cstring>
using namespace std;

/**
 * P5494 【模板】线段树分裂
 * 
 * 题目描述：
 * 给定一个初始为空的序列，支持以下操作：
 * 1. 在某个位置插入一个数
 * 2. 将某个区间分裂成一个新的序列
 * 3. 将两个序列合并
 * 4. 查询某个区间内第k小的数
 * 
 * 核心算法：线段树分裂 + 线段树合并
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

const int MAXN = 200010;
const int MAXM = 20000000;  // 动态开点空间

struct Node {
    int l, r;      // 左右子节点索引
    long long sum;  // 区间和
    int cnt;        // 区间内元素个数
    
    Node() : l(-1), r(-1), sum(0), cnt(0) {}
};

class SegmentTreeSplit {
private:
    vector<Node> tree;
    vector<int> roots;
    int node_cnt;
    int seq_cnt;
    int maxn;
    
public:
    SegmentTreeSplit(int n = MAXN, int m = MAXM) : maxn(n) {
        tree.resize(m);
        roots.resize(MAXN, -1);
        node_cnt = 0;
        seq_cnt = 1;
        
        // 初始化根节点
        roots[1] = new_node();
    }
    
    int new_node() {
        if (node_cnt >= MAXM) {
            // 内存不足时进行优化
            tree.resize(node_cnt + 1000000);
        }
        tree[node_cnt] = Node();
        return node_cnt++;
    }
    
    void push_up(int rt) {
        if (rt == -1) return;
        
        tree[rt].sum = 0;
        tree[rt].cnt = 0;
        
        if (tree[rt].l != -1) {
            tree[rt].sum += tree[tree[rt].l].sum;
            tree[rt].cnt += tree[tree[rt].l].cnt;
        }
        
        if (tree[rt].r != -1) {
            tree[rt].sum += tree[tree[rt].r].sum;
            tree[rt].cnt += tree[tree[rt].r].cnt;
        }
    }
    
    void update(int rt, int l, int r, int pos, long long val) {
        if (l == r) {
            tree[rt].sum += val;
            tree[rt].cnt += 1;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tree[rt].l == -1) {
                tree[rt].l = new_node();
            }
            update(tree[rt].l, l, mid, pos, val);
        } else {
            if (tree[rt].r == -1) {
                tree[rt].r = new_node();
            }
            update(tree[rt].r, mid + 1, r, pos, val);
        }
        
        push_up(rt);
    }
    
    void split(int p, int q, int l, int r, int L, int R) {
        if (p == -1) return;
        if (L > r || R < l) return;
        
        if (L >= l && R <= r) {
            // 整个区间需要分裂
            if (q == -1) {
                q = new_node();
            }
            
            // 复制节点信息
            tree[q] = tree[p];
            
            // 清空原节点
            tree[p] = Node();
            return;
        }
        
        int mid = (L + R) >> 1;
        
        if (tree[p].l != -1 && l <= mid) {
            if (tree[q].l == -1) {
                tree[q].l = new_node();
            }
            split(tree[p].l, tree[q].l, l, r, L, mid);
        }
        
        if (tree[p].r != -1 && r > mid) {
            if (tree[q].r == -1) {
                tree[q].r = new_node();
            }
            split(tree[p].r, tree[q].r, l, r, mid + 1, R);
        }
        
        push_up(p);
        push_up(q);
    }
    
    int merge(int p, int q, int l, int r) {
        if (p == -1) return q;
        if (q == -1) return p;
        
        if (l == r) {
            tree[p].sum += tree[q].sum;
            tree[p].cnt += tree[q].cnt;
            return p;
        }
        
        int mid = (l + r) >> 1;
        
        if (tree[p].l != -1 && tree[q].l != -1) {
            tree[p].l = merge(tree[p].l, tree[q].l, l, mid);
        } else if (tree[q].l != -1) {
            tree[p].l = tree[q].l;
        }
        
        if (tree[p].r != -1 && tree[q].r != -1) {
            tree[p].r = merge(tree[p].r, tree[q].r, mid + 1, r);
        } else if (tree[q].r != -1) {
            tree[p].r = tree[q].r;
        }
        
        push_up(p);
        return p;
    }
    
    long long query_kth(int rt, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        
        int mid = (l + r) >> 1;
        int left_cnt = (tree[rt].l != -1) ? tree[tree[rt].l].cnt : 0;
        
        if (k <= left_cnt) {
            return query_kth(tree[rt].l, l, mid, k);
        } else {
            return query_kth(tree[rt].r, mid + 1, r, k - left_cnt);
        }
    }
    
    long long query_sum(int rt, int l, int r, int L, int R) {
        if (rt == -1 || L > r || R < l) {
            return 0;
        }
        
        if (L >= l && R <= r) {
            return tree[rt].sum;
        }
        
        int mid = (L + R) >> 1;
        long long res = 0;
        
        if (tree[rt].l != -1) {
            res += query_sum(tree[rt].l, l, r, L, mid);
        }
        
        if (tree[rt].r != -1) {
            res += query_sum(tree[rt].r, l, r, mid + 1, R);
        }
        
        return res;
    }
    
    // 对外接口
    void split_sequence(int p, int l, int r) {
        seq_cnt++;
        roots[seq_cnt] = new_node();
        split(roots[p], roots[seq_cnt], l, r, 1, maxn);
    }
    
    void merge_sequence(int p, int q) {
        roots[p] = merge(roots[p], roots[q], 1, maxn);
        roots[q] = -1;
    }
    
    void insert_value(int p, long long x) {
        int pos = tree[roots[p]].cnt + 1;
        update(roots[p], 1, maxn, pos, x);
    }
    
    long long query_range_sum(int p, int l, int r) {
        return query_sum(roots[p], l, r, 1, maxn);
    }
    
    long long query_kth_smallest(int p, int k) {
        if (tree[roots[p]].cnt < k) {
            return -1;
        }
        return query_kth(roots[p], 1, maxn, k);
    }
    
    int get_sequence_count() const {
        return seq_cnt;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    SegmentTreeSplit st(n);
    
    // 读入初始序列
    for (int i = 1; i <= n; i++) {
        long long x;
        cin >> x;
        st.insert_value(1, x);
    }
    
    vector<long long> results;
    
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        
        if (op == 0) {
            int p, l, r;
            cin >> p >> l >> r;
            st.split_sequence(p, l, r);
        } else if (op == 1) {
            int p, q;
            cin >> p >> q;
            st.merge_sequence(p, q);
        } else if (op == 2) {
            int p;
            long long x;
            cin >> p >> x;
            st.insert_value(p, x);
        } else if (op == 3) {
            int p, l, r;
            cin >> p >> l >> r;
            long long sum_val = st.query_range_sum(p, l, r);
            results.push_back(sum_val);
        } else if (op == 4) {
            int p, k;
            cin >> p >> k;
            long long kth = st.query_kth_smallest(p, k);
            results.push_back(kth);
        }
    }
    
    // 输出结果
    for (long long res : results) {
        cout << res << "\n";
    }
    
    return 0;
}

/*
 * 线段树分裂算法详解：
 * 
 * 1. 算法核心思想：
 *    线段树分裂是线段树合并的逆操作，用于将一个线段树按照区间拆分成两个独立的线段树。
 *    这种操作在处理序列分裂、区间分离等场景中非常有用。
 * 
 * 2. 数据结构设计：
 *    - 使用动态开点线段树，避免空间浪费
 *    - 每个节点维护区间和和元素个数
 *    - 支持多个序列的独立管理
 * 
 * 3. 核心操作分析：
 *    - 分裂操作：时间复杂度O(log n)，空间复杂度O(log n)
 *    - 合并操作：时间复杂度O(n1 + n2)，其中n1、n2为两棵树的大小
 *    - 查询操作：时间复杂度O(log n)
 * 
 * 4. 算法优化技巧：
 *    - 动态开点：按需分配节点，节省空间
 *    - 懒标记：支持区间操作的延迟更新
 *    - 内存池：预分配节点，提高效率
 * 
 * 5. 应用场景扩展：
 *    - 序列操作：支持序列的分裂、合并、插入、删除
 *    - 区间管理：动态管理多个独立的区间
 *    - 可持久化：实现可持久化线段树
 *    - 离线算法：处理离线查询问题
 * 
 * 6. 类似题目推荐：
 *    - P4556 [Vani有约会]雨天的尾巴 (线段树合并经典题)
 *    - P3224 [HNOI2012]永无乡 (线段树合并+并查集)
 *    - P5298 [PKUWC2018]Minimax (线段树合并+概率DP)
 *    - CF911G Mass Change Queries (线段树分裂+区间赋值)
 *    - P6773 [NOI2020]命运 (线段树合并+树形DP)
 * 
 * 7. 算法复杂度分析：
 *    - 时间复杂度：所有操作均为O(log n)级别
 *    - 空间复杂度：O(n log n)，其中n为序列长度
 *    - 实际效率：在合理优化下可以处理10^5级别的数据
 */