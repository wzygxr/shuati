#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

/**
 * 题目：CF911G Mass Change Queries
 * 测试链接：https://www.luogu.com.cn/problem/CF911G
 * 
 * 题目描述：
 * 给定一个长度为n的序列，支持m次操作，每次操作将区间[l, r]内所有等于x的数改为y。
 * 最后输出整个序列。
 * 
 * 解题思路：
 * 1. 使用线段树合并解决区间赋值问题
 * 2. 每个节点维护一个映射，表示当前区间内值的转换关系
 * 3. 使用懒标记优化区间修改操作
 * 4. 时间复杂度：O((n + m) log n)
 * 
 * 核心思想：
 * - 对于每个线段树节点，维护一个大小为100的数组，表示值i被映射到哪个值
 * - 区间修改时，更新对应区间的映射关系
 * - 查询时，通过懒标记下传和映射关系获取最终结果
 */

const int N = 200010;
const int MAX_VAL = 100; // 值的范围是1-100

struct Node {
    int l, r; // 左右子节点编号
    int map[MAX_VAL + 1]; // 映射关系
    bool lazy; // 懒标记
    
    Node() : l(0), r(0), lazy(false) {
        // 初始化映射为恒等映射
        for (int i = 1; i <= MAX_VAL; i++) {
            map[i] = i;
        }
    }
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
    
    void pushdown(int u) {
        if (tr[u].lazy) {
            // 更新左子树
            if (tr[u].l != 0) {
                apply_mapping(tr[u].l, tr[u].map);
            }
            
            // 更新右子树
            if (tr[u].r != 0) {
                apply_mapping(tr[u].r, tr[u].map);
            }
            
            // 重置当前节点的映射为恒等映射
            for (int i = 1; i <= MAX_VAL; i++) {
                tr[u].map[i] = i;
            }
            tr[u].lazy = false;
        }
    }
    
    void apply_mapping(int u, int parent_map[]) {
        // 创建新的映射：new_map[i] = parent_map[tr[u].map[i]]
        int new_map[MAX_VAL + 1];
        for (int i = 1; i <= MAX_VAL; i++) {
            new_map[i] = parent_map[tr[u].map[i]];
        }
        
        // 如果子节点已经有懒标记，需要合并映射
        if (tr[u].lazy) {
            int temp[MAX_VAL + 1];
            for (int i = 1; i <= MAX_VAL; i++) {
                temp[i] = new_map[i];
            }
            for (int i = 1; i <= MAX_VAL; i++) {
                new_map[i] = temp[i];
            }
        } else {
            // 否则直接设置映射
            for (int i = 1; i <= MAX_VAL; i++) {
                tr[u].map[i] = new_map[i];
            }
            tr[u].lazy = true;
        }
    }
    
    int build(int l, int r) {
        int u = new_node();
        if (l == r) {
            // 叶子节点不需要特殊处理，映射关系已经是恒等映射
            return u;
        }
        
        int mid = (l + r) >> 1;
        tr[u].l = build(l, mid);
        tr[u].r = build(mid + 1, r);
        return u;
    }
    
    void update(int u, int l, int r, int ql, int qr, int x, int y) {
        if (ql <= l && r <= qr) {
            // 整个区间都在查询范围内
            if (!tr[u].lazy) {
                tr[u].lazy = true;
            }
            
            // 更新映射：将x映射到y，其他值保持不变
            for (int i = 1; i <= MAX_VAL; i++) {
                if (tr[u].map[i] == x) {
                    tr[u].map[i] = y;
                }
            }
            return;
        }
        
        pushdown(u);
        int mid = (l + r) >> 1;
        
        if (ql <= mid) {
            update(tr[u].l, l, mid, ql, qr, x, y);
        }
        if (qr > mid) {
            update(tr[u].r, mid + 1, r, ql, qr, x, y);
        }
    }
    
    int query(int u, int l, int r, int pos, int a[]) {
        if (l == r) {
            // 叶子节点，应用映射关系后返回
            return tr[u].map[a[l]];
        }
        
        pushdown(u);
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            return query(tr[u].l, l, mid, pos, a);
        } else {
            return query(tr[u].r, mid + 1, r, pos, a);
        }
    }
    
    int merge(int u, int v, int l, int r) {
        if (u == 0) return v;
        if (v == 0) return u;
        
        if (l == r) {
            // 叶子节点合并：应用v的映射到u
            if (tr[v].lazy) {
                if (!tr[u].lazy) {
                    tr[u].lazy = true;
                    // 复制恒等映射
                    for (int i = 1; i <= MAX_VAL; i++) {
                        tr[u].map[i] = i;
                    }
                }
                
                // 合并映射
                for (int i = 1; i <= MAX_VAL; i++) {
                    tr[u].map[i] = tr[v].map[tr[u].map[i]];
                }
            }
            return u;
        }
        
        pushdown(u);
        pushdown(v);
        
        int mid = (l + r) >> 1;
        tr[u].l = merge(tr[u].l, tr[v].l, l, mid);
        tr[u].r = merge(tr[u].r, tr[v].r, mid + 1, r);
        
        return u;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    
    int a[n + 1];
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    SegmentTree seg(n);
    int root = seg.build(1, n);
    
    int m;
    cin >> m;
    
    for (int i = 0; i < m; i++) {
        int l, r, x, y;
        cin >> l >> r >> x >> y;
        
        if (x != y) {
            seg.update(root, 1, n, l, r, x, y);
        }
    }
    
    // 输出最终序列
    for (int i = 1; i <= n; i++) {
        cout << seg.query(root, 1, n, i, a) << " ";
    }
    cout << endl;
    
    return 0;
}

/**
 * 解题技巧总结：
 * 1. 映射关系的维护：每个节点维护一个值域大小的映射数组
 * 2. 懒标记的应用：只有当需要下传时才创建新的映射关系
 * 3. 映射合并：父节点的映射应用到子节点的映射上
 * 4. 内存优化：动态开点避免内存浪费
 * 
 * 类似题目推荐：
 * 1. P5494 【模板】线段树合并 - 线段树合并基础
 * 2. P6773 [NOI2020] 命运 - 树形DP+线段树合并
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
 * 
 * 线段树合并的变种应用：
 * 1. 区间赋值：通过维护映射关系实现高效区间修改
 * 2. 颜色段合并：维护连续相同值的区间
 * 3. 历史版本管理：通过可持久化线段树支持历史查询
 * 
 * 性能优化建议：
 * 1. 使用数组而非HashMap提高访问速度
 * 2. 懒标记及时下传避免深度递归
 * 3. 合理预分配内存减少动态分配开销
 */