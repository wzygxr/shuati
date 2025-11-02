// Naive Operations - HDU 6315
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=6315

const int MAXN = 100005;
const int MAXT = MAXN << 2; // 线段树节点数

int n, m;

// 线段树相关
int tree[MAXT];  // 区间和
int cnt[MAXT];   // 区间最小值
int col[MAXT];   // 延迟标记
int b[MAXN];     // b数组

// 线段树更新
void pushUp(int rt) {
    cnt[rt] = cnt[rt << 1] < cnt[rt << 1 | 1] ? cnt[rt << 1] : cnt[rt << 1 | 1];
    tree[rt] = tree[rt << 1] + tree[rt << 1 | 1];
}

void pushDown(int rt) {
    if (col[rt] != 0) {
        cnt[rt << 1] -= col[rt];
        cnt[rt << 1 | 1] -= col[rt];
        col[rt << 1] += col[rt];
        col[rt << 1 | 1] += col[rt];
        col[rt] = 0;
    }
}

void build(int l, int r, int rt) {
    if (l == r) {
        cnt[rt] = b[l];
        tree[rt] = col[rt] = 0;
        return;
    }
    
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

void update(int L, int R, int temp, int l, int r, int rt) {
    if (temp == 1) {
        if (l == r) {
            cnt[rt] = b[l];
            tree[rt] += 1;
            return;
        }
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, cnt[rt << 1] == 1 ? 1 : 0, l, mid, rt << 1);
        if (R > mid) update(L, R, cnt[rt << 1 | 1] == 1 ? 1 : 0, mid + 1, r, rt << 1 | 1);
    } else {
        if (L <= l && r <= R) {
            cnt[rt] -= 1;
            col[rt] += 1;
            return;
        }
        
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, 0, l, mid, rt << 1);
        if (R > mid) update(L, R, 0, mid + 1, r, rt << 1 | 1);
    }
    pushUp(rt);
}

int query(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt];
    }
    
    pushDown(rt);
    int mid = (l + r) >> 1;
    int ret = 0;
    if (L <= mid) ret += query(L, R, l, mid, rt << 1);
    if (R > mid) ret += query(L, R, mid + 1, r, rt << 1 | 1);
    return ret;
}

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体编译环境实现输入输出