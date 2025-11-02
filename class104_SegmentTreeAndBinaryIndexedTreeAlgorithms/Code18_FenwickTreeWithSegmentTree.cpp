// class131/Code18_FenwickTreeWithSegmentTree.cpp
// 洛谷 P3380 【模板】树套树
// 题目链接: https://www.luogu.com.cn/problem/P3380

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 50010;
const int MAX_VAL = 1000000010;
const int MAX_TREE_SIZE = MAXN * 40;

// 线段树节点定义
struct Node {
    int left, right; // 左右子节点索引
    int count;       // 该区间的元素个数
} tree[MAX_TREE_SIZE];

int root[MAXN];  // 树状数组每个节点对应的线段树根节点
int a[MAXN];     // 原始数组
vector<int> sorted; // 离散化后的数组
int cnt;         // 动态开点计数器
int n, m;        // 数组大小和操作数量

// 初始化线段树节点
void initNode(int node) {
    tree[node].left = tree[node].right = 0;
    tree[node].count = 0;
}

// 线段树更新
void updateTree(int &root, int l, int r, int pos, int val) {
    if (!root) {
        root = ++cnt;
        initNode(root);
    }
    
    tree[root].count += val;
    if (l == r) {
        return;
    }
    
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        updateTree(tree[root].left, l, mid, pos, val);
    } else {
        updateTree(tree[root].right, mid + 1, r, pos, val);
    }
}

// 线段树查询区间[l,r]中在[ql,qr]范围内的元素个数
int queryTree(int root, int l, int r, int ql, int qr) {
    if (!root || r < ql || l > qr) {
        return 0;
    }
    
    if (ql <= l && r <= qr) {
        return tree[root].count;
    }
    
    int mid = (l + r) >> 1;
    return queryTree(tree[root].left, l, mid, ql, qr) + 
           queryTree(tree[root].right, mid + 1, r, ql, qr);
}

// 计算lowbit
int lowbit(int x) {
    return x & (-x);
}

// 树状数组更新
void update(int pos, int val, int delta) {
    while (pos <= n) {
        updateTree(root[pos], 1, sorted.size() - 1, val, delta);
        pos += lowbit(pos);
    }
}

// 树状数组查询前缀和
int query(int pos, int val) {
    int sum = 0;
    while (pos > 0) {
        sum += queryTree(root[pos], 1, sorted.size() - 1, 1, val);
        pos -= lowbit(pos);
    }
    return sum;
}

// 离散化：获取数值对应的索引
int getIndex(int val) {
    return lower_bound(sorted.begin(), sorted.end(), val) - sorted.begin();
}

// 查询区间第k小
int kthSmallest(int l, int r, int k) {
    int left = 1, right = sorted.size() - 1;
    while (left < right) {
        int mid = (left + right) >> 1;
        int count = query(r, mid) - query(l - 1, mid);
        if (count >= k) {
            right = mid;
        } else {
            left = mid + 1;
        }
    }
    return sorted[left];
}

// 查询前驱（最大的小于k的数）
int getPredecessor(int l, int r, int k) {
    int left = 1, right = sorted.size() - 1;
    int ans = -MAX_VAL;
    
    while (left <= right) {
        int mid = (left + right) >> 1;
        if (sorted[mid] >= k) {
            right = mid - 1;
        } else {
            int count = query(r, mid) - query(l - 1, mid);
            if (count > 0) {
                ans = sorted[mid];
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
    }
    
    return ans;
}

// 查询后继（最小的大于k的数）
int getSuccessor(int l, int r, int k) {
    int left = 1, right = sorted.size() - 1;
    int ans = MAX_VAL;
    
    while (left <= right) {
        int mid = (left + right) >> 1;
        if (sorted[mid] <= k) {
            left = mid + 1;
        } else {
            // 检查是否有大于k的元素
            int count = query(r, sorted.size() - 1) - query(r, mid - 1) - 
                       (query(l - 1, sorted.size() - 1) - query(l - 1, mid - 1));
            if (count > 0) {
                ans = sorted[mid];
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
    }
    
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m;
    
    // 读取原始数据并复制到sorted数组用于离散化
    sorted.resize(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
        sorted[i] = a[i];
    }
    
    // 离散化处理：排序并去重
    sort(sorted.begin() + 1, sorted.end());
    sorted.erase(unique(sorted.begin() + 1, sorted.end()), sorted.end());
    
    // 初始化
    cnt = 0;
    memset(root, 0, sizeof(root));
    
    // 构建初始树状数组
    for (int i = 1; i <= n; i++) {
        int idx = getIndex(a[i]);
        update(i, idx, 1);
    }
    
    // 处理操作
    while (m--) {
        int op;
        cin >> op;
        
        if (op == 1) { // 查询排名
            int l, r, k;
            cin >> l >> r >> k;
            int idx = lower_bound(sorted.begin(), sorted.end(), k) - sorted.begin();
            int rank = (query(r, idx - 1) - query(l - 1, idx - 1)) + 1;
            cout << rank << "\n";
        } else if (op == 2) { // 查询第k小
            int l, r, k;
            cin >> l >> r >> k;
            int ans = kthSmallest(l, r, k);
            cout << ans << "\n";
        } else if (op == 3) { // 单点修改
            int pos, k;
            cin >> pos >> k;
            // 先删除旧值
            int oldIdx = getIndex(a[pos]);
            update(pos, oldIdx, -1);
            // 再插入新值
            a[pos] = k;
            int newIdx = getIndex(k);
            update(pos, newIdx, 1);
        } else if (op == 4) { // 查询前驱
            int l, r, k;
            cin >> l >> r >> k;
            int pre = getPredecessor(l, r, k);
            cout << pre << "\n";
        } else if (op == 5) { // 查询后继
            int l, r, k;
            cin >> l >> r >> k;
            int succ = getSuccessor(l, r, k);
            cout << succ << "\n";
        }
    }
    
    return 0;
}

/*
复杂度分析：
- 时间复杂度：
  - 单点修改：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 区间查询：O(log²n) - 树状数组O(logn)次操作，每次操作对应线段树O(logn)时间
  - 第k小查询：O(log²n) - 二分查找O(logn)次，每次查询O(log²n)时间
  - 前驱/后继查询：O(log²n)
- 空间复杂度：O(n log n) - 树状数组的每个节点对应一棵动态开点线段树

算法详解：
使用树套树（树状数组套线段树）解决区间问题。核心思想是用树状数组维护位置维度，
每个树状数组节点对应一棵线段树，线段树维护值域维度。通过这种二维结构可以高效处理区间查询和更新操作。

算法步骤:
1. 离散化: 将输入数据离散化到连续整数范围，减少空间消耗
2. 构建树状数组: 每个节点维护一棵动态开点线段树
3. 实现各种查询操作: 利用树状数组的前缀和性质和线段树的区间查询能力

关键优化:
1. 使用离散化减少值域范围
2. 动态开点线段树节省空间
3. 树状数组的前缀和性质简化区间查询

优化点：
1. 离散化是必须的，否则权值范围太大无法处理
2. 动态开点避免了静态线段树的空间浪费
3. 可以使用非递归实现来优化常数
4. 对于大规模数据，可以考虑使用内存池优化动态分配
*/