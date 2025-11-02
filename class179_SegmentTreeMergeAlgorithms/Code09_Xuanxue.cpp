// 玄学 - UOJ #46
// 测试链接 : https://uoj.ac/problem/46

#define min(a,b) ((a)<(b)?(a):(b))

const int MAXN = 600001;
const int MAXT = 20 * MAXN; // 线段树节点数

int n, mod;
int num[MAXN]; // 原数组

// 线段树相关
int L[5 * MAXN];  // 左边界
int R[5 * MAXN];  // 右边界
int ncnt = 0;  // 节点计数器

// 变换信息
struct Tran {
    int r, a, b;
    
    Tran() : r(0), a(0), b(0) {}
    Tran(int r, int a, int b) : r(r), a(a), b(b) {}
};

Tran node[MAXT];

// 线段树节点
int ls[MAXT], rs[MAXT];

// 更新节点信息
void pushUp(int u) {
    L[u] = ncnt + 1;
    int lsiz = R[ls[u]], rsiz = R[rs[u]];
    int p = L[ls[u]], q = L[rs[u]];
    
    while (p <= lsiz && q <= rsiz) {
        long long a = (1LL * node[p].a * node[q].a) % mod;
        long long b = (1LL * node[p].b * node[q].a % mod + node[q].b) % mod;
        node[++ncnt] = Tran(min(node[p].r, node[q].r), (int)a, (int)b);
        
        if (node[p].r < node[q].r) {
            p++;
        } else if (node[p].r > node[q].r) {
            q++;
        } else {
            p++;
            q++;
        }
    }
    
    while (p <= lsiz) {
        node[++ncnt] = node[p++];
    }
    
    while (q <= rsiz) {
        node[++ncnt] = node[q++];
    }
    
    R[u] = ncnt;
}

// 插入操作
void insert(int u, int tL, int tR, int pL, int pR, int a, int b, int tp) {
    if (tL == tR) {
        L[u] = ncnt + 1;
        if (pL > 1) {
            node[++ncnt] = Tran(pL - 1, 1, 0);
        }
        node[++ncnt] = Tran(pR, a, b);
        if (pR < n) {
            node[++ncnt] = Tran(n, 1, 0);
        }
        R[u] = ncnt;
        return;
    }
    
    int mid = (tL + tR) >> 1;
    if (tp <= mid) {
        if (ls[u] == 0) {
            ls[u] = ++ncnt;
        }
        insert(ls[u], tL, mid, pL, pR, a, b, tp);
    } else {
        if (rs[u] == 0) {
            rs[u] = ++ncnt;
        }
        insert(rs[u], mid + 1, tR, pL, pR, a, b, tp);
    }
    
    if (tp == tR) {
        pushUp(u);
    }
}

// 查询操作
void query(int u, int tL, int tR, int qL, int qR, int p, int& A, int& B) {
    if (qL <= tL && tR <= qR) {
        // 二分查找变换
        int l = L[u] - 1, r = R[u];
        while (l + 1 < r) {
            int mid = (l + r) >> 1;
            if (p <= node[mid].r) {
                r = mid;
            } else {
                l = mid;
            }
        }
        
        long long a = (1LL * A * node[r].a) % mod;
        long long b = (1LL * B * node[r].a % mod + node[r].b) % mod;
        A = (int)a;
        B = (int)b;
        return;
    }
    
    int mid = (tL + tR) >> 1;
    if (qL <= mid) {
        query(ls[u], tL, mid, qL, qR, p, A, B);
    }
    if (mid + 1 <= qR) {
        query(rs[u], mid + 1, tR, qL, qR, p, A, B);
    }
}

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体编译环境实现输入输出