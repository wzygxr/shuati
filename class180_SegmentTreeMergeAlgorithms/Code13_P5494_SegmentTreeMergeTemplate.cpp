#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

/**
 * 题目：P5494 【模板】线段树合并
 * 测试链接：https://www.luogu.com.cn/problem/P5494
 * 
 * 题目描述：
 * 给定一个长度为n的序列，支持两种操作：
 * 1. 将区间[l, r]内的所有数加上k
 * 2. 查询区间[l, r]内所有数的和
 * 
 * 解题思路：
 * 1. 线段树合并模板题，演示线段树合并的基本操作
 * 2. 支持动态开点、区间修改、区间查询
 * 3. 时间复杂度：O(n log n)
 * 
 * 核心思想：
 * - 动态开点线段树，避免内存浪费
 * - 懒标记优化区间修改操作
 * - 线段树合并用于合并两棵线段树的信息
 */

const int N = 100010;

struct Node {
    int l, r; // 左右子节点编号
    long long sum, add; // 区间和、懒标记
    Node() : l(0), r(0), sum(0), add(0) {}
};

class SegmentTree {
private:
    vector<Node> tr;
    int cnt;
    
public:
    SegmentTree(int n) {
        tr.resize(40 * n);
        cnt = 0;
    }
    
    int new_node() {
        return ++cnt;
    }
    
    void pushup(int u) {
        tr[u].sum = tr[tr[u].l].sum + tr[tr[u].r].sum;
    }
    
    void pushdown(int u, int l, int r) {
        if (tr[u].add != 0) {
            int mid = (l + r) >> 1;
            
            // 更新左子树
            if (tr[u].l != 0) {
                tr[tr[u].l].sum += tr[u].add * (mid - l + 1);
                tr[tr[u].l].add += tr[u].add;
            }
            
            // 更新右子树
            if (tr[u].r != 0) {
                tr[tr[u].r].sum += tr[u].add * (r - mid);
                tr[tr[u].r].add += tr[u].add;
            }
            
            tr[u].add = 0;
        }
    }
    
    int build(int l, int r, vector<int>& a) {
        int u = new_node();
        if (l == r) {
            tr[u].sum = a[l];
            return u;
        }
        
        int mid = (l + r) >> 1;
        tr[u].l = build(l, mid, a);
        tr[u].r = build(mid + 1, r, a);
        pushup(u);
        return u;
    }
    
    void update(int u, int l, int r, int ql, int qr, long long k) {
        if (ql <= l && r <= qr) {
            tr[u].sum += k * (r - l + 1);
            tr[u].add += k;
            return;
        }
        
        pushdown(u, l, r);
        int mid = (l + r) >> 1;
        
        if (ql <= mid) {
            update(tr[u].l, l, mid, ql, qr, k);
        }
        if (qr > mid) {
            update(tr[u].r, mid + 1, r, ql, qr, k);
        }
        
        pushup(u);
    }
    
    long long query(int u, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tr[u].sum;
        }
        
        pushdown(u, l, r);
        int mid = (l + r) >> 1;
        long long res = 0;
        
        if (ql <= mid) {
            res += query(tr[u].l, l, mid, ql, qr);
        }
        if (qr > mid) {
            res += query(tr[u].r, mid + 1, r, ql, qr);
        }
        
        return res;
    }
    
    int merge(int u, int v, int l, int r) {
        if (u == 0) return v;
        if (v == 0) return u;
        
        if (l == r) {
            // 叶子节点直接合并
            tr[u].sum += tr[v].sum;
            return u;
        }
        
        pushdown(u, l, r);
        pushdown(v, l, r);
        
        int mid = (l + r) >> 1;
        tr[u].l = merge(tr[u].l, tr[v].l, l, mid);
        tr[u].r = merge(tr[u].r, tr[v].r, mid + 1, r);
        
        pushup(u);
        return u;
    }
    
    int clone(int u) {
        int v = new_node();
        tr[v].l = tr[u].l;
        tr[v].r = tr[u].r;
        tr[v].sum = tr[u].sum;
        tr[v].add = tr[u].add;
        return v;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    cin >> n >> m;
    
    vector<int> a(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    SegmentTree seg(n);
    int root = seg.build(1, n, a);
    
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int l, r, k;
            cin >> l >> r >> k;
            seg.update(root, 1, n, l, r, k);
        } else {
            int l, r;
            cin >> l >> r;
            cout << seg.query(root, 1, n, l, r) << endl;
        }
    }
    
    return 0;
}

/**
 * 线段树合并的应用场景：
 * 1. 树形DP优化：将子树信息合并到父节点
 * 2. 可持久化线段树：支持历史版本查询
 * 3. 区间赋值问题：如CF911G Mass Change Queries
 * 4. 树上问题：如P4556 雨天的尾巴
 * 
 * 线段树合并的复杂度分析：
 * - 每次合并操作的时间复杂度为两棵线段树公共节点数
 * - 总时间复杂度为O(n log n)，因为每个节点最多被合并log n次
 * 
 * 注意事项：
 * 1. 合并前需要下传懒标记
 * 2. 合并后需要上传信息
 * 3. 注意内存分配，避免内存溢出
 * 4. 对于持久化线段树，需要复制节点
 * 
 * 类似题目推荐：
 * 1. P6773 [NOI2020] 命运 - 树形DP+线段树合并
 * 2. CF911G Mass Change Queries - 区间赋值+线段树合并
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
 * 5. P5298 [PKUWC2018]Minimax - 概率DP+线段树合并
 * 
 * 线段树合并的变种：
 * 1. 权值线段树合并：用于维护值域信息
 * 2. 主席树合并：支持可持久化操作
 * 3. 李超线段树合并：用于维护函数最值
 */