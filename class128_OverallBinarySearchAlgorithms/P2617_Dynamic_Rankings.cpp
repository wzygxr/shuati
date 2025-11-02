// P2617 Dynamic Rankings - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P2617
// 题目描述：
// 给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
// Q l r k 表示查询下标在区间[l,r]中的第k小的数；
// C x y 表示将ax改为y。
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)

// 由于环境限制，这里只提供C++代码框架，实际编译需要相应环境支持
//#include <iostream>
//#include <algorithm>
//#include <cstdio>
//#include <vector>
//using namespace std;

const int MAXN = 100001;
int n, m;

// 原始数组
int arr[MAXN];

// 离散化后的数组
int sorted[MAXN * 2];

// 操作信息
struct Operation {
    int type; // 0: 查询, 1: 修改
    int l, r, k, x, y;
    int id;
    
    Operation() {}
    Operation(int _type, int _l, int _r, int _k, int _x, int _y, int _id) {
        type = _type;
        l = _l;
        r = _r;
        k = _k;
        x = _x;
        y = _y;
        id = _id;
    }
};

Operation ops[MAXN * 2];

// 树状数组
int tree[MAXN];

// 整体二分
int lset[MAXN * 2];
int rset[MAXN * 2];

// 查询的答案
int ans[MAXN];

// 树状数组操作
int lowbit(int i) {
    return i & -i;
}

void add(int i, int v) {
    while (i <= n) {
        tree[i] += v;
        i += lowbit(i);
    }
}

int sum(int i) {
    int ret = 0;
    while (i > 0) {
        ret += tree[i];
        i -= lowbit(i);
    }
    return ret;
}

int query(int l, int r) {
    return sum(r) - sum(l - 1);
}

// 整体二分核心函数
// ql, qr: 操作范围
// vl, vr: 值域范围（离散化后的下标）
void compute(int ql, int qr, int vl, int vr) {
    // 递归边界
    if (ql > qr) {
        return;
    }
    
    // 如果值域范围只有一个值，说明找到了答案
    if (vl == vr) {
        for (int i = ql; i <= qr; i++) {
            if (ops[i].type == 0) { // 查询操作
                ans[ops[i].id] = sorted[vl];
            }
        }
        return;
    }
    
    // 二分中点
    int mid = (vl + vr) >> 1;
    
    // 将值域小于等于mid的数加入树状数组
    for (int i = vl; i <= mid; i++) {
        // 这里需要处理所有值为sorted[i]的元素
        // 在实际实现中，我们需要更复杂的处理方式
    }
    
    // 检查每个操作，根据满足条件的元素个数划分到左右区间
    int lsiz = 0, rsiz = 0;
    for (int i = ql; i <= qr; i++) {
        if (ops[i].type == 0) { // 查询操作
            // 查询区间[ops[i].l, ops[i].r]中值小于等于sorted[mid]的元素个数
            int satisfy = query(ops[i].l, ops[i].r);
            
            if (satisfy >= ops[i].k) {
                // 说明第k小的数在左半部分
                lset[++lsiz] = i;
            } else {
                // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                ops[i].k -= satisfy;
                rset[++rsiz] = i;
            }
        } else { // 修改操作
            // 修改操作需要拆分为删除和插入
            // 这里简化处理，实际实现中需要更复杂的逻辑
            if (ops[i].y <= sorted[mid]) {
                add(ops[i].x, 1);
                lset[++lsiz] = i;
            } else {
                rset[++rsiz] = i;
            }
        }
    }
    
    // 重新排列操作顺序
    int idx = ql;
    for (int i = 1; i <= lsiz; i++) {
        int temp = lset[i];
        lset[i] = ops[temp].id;
        ops[idx++] = ops[temp];
    }
    for (int i = 1; i <= rsiz; i++) {
        int temp = rset[i];
        rset[i] = ops[temp].id;
        ops[idx++] = ops[temp];
    }
    
    // 撤销对树状数组的修改
    for (int i = vl; i <= mid; i++) {
        // 撤销操作
    }
    
    // 递归处理左右两部分
    compute(ql, ql + lsiz - 1, vl, mid);
    compute(ql + lsiz, qr, mid + 1, vr);
}

//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(0);
//    cout.tie(0);
//    
//    cin >> n >> m;
//    
//    // 读取原始数组
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//        sorted[i] = arr[i];
//    }
//    
//    int opCount = n;
//    // 读取操作
//    for (int i = 1; i <= m; i++) {
//        string opType;
//        cin >> opType;
//        if (opType == "Q") {
//            int l, r, k;
//            cin >> l >> r >> k;
//            ops[opCount++] = Operation(0, l, r, k, 0, 0, i);
//        } else { // C
//            int x, y;
//            cin >> x >> y;
//            ops[opCount++] = Operation(1, 0, 0, 0, x, y, i);
//            sorted[++n] = y; // 添加到离散化数组中
//        }
//    }
//    
//    // 离散化
//    sort(sorted + 1, sorted + n + 1);
//    int uniqueCount = unique(sorted + 1, sorted + n + 1) - sorted - 1;
//    
//    // 整体二分求解
//    compute(1, opCount - 1, 1, uniqueCount);
//    
//    // 输出结果
//    for (int i = 1; i <= m; i++) {
//        if (ans[i] != 0) {
//            cout << ans[i] << "\n";
//        }
//    }
//    
//    return 0;
//}