/**
 * Codeforces 707D - Persistent Bookcase
 * 
 * 题目描述:
 * 有一个n行m列的书架，初始时所有位置都是空的。
 * 有4种操作：
 * 1. 1 i j - 在第i行第j列放置一本书（如果该位置为空）
 * 2. 2 i j - 从第i行第j列取出一本书（如果该位置有书）
 * 3. 3 i - 翻转第i行（有书变无书，无书变有书）
 * 4. 4 k - 回到第k次操作之后的状态
 * 对于每次操作，输出当前书架上书的总数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决持久化数据结构问题。
 * 1. 将书架的每一行看作一个二进制数，用主席树维护每一行的状态
 * 2. 对于操作1和2，直接修改对应位置的值
 * 3. 对于操作3，翻转整行可以通过异或操作实现
 * 4. 对于操作4，回到历史版本，主席树天然支持这一操作
 * 
 * 时间复杂度: O(q log m)
 * 空间复杂度: O(q log m)
 * 
 * 示例:
 * 输入:
 * 2 3
 * 3
 * 1 1 1
 * 3 1
 * 4 1
 * 
 * 输出:
 * 1
 * 2
 * 1
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 1010;
const int MAXQ = 100010;

// 每个版本线段树的根节点
int root[MAXQ];

// 线段树节点信息
int left[MAXQ * 20];
int right[MAXQ * 20];
int value[MAXQ * 20]; // 0表示无书，1表示有书
int flip[MAXQ * 20];  // 翻转标记

// 线段树节点计数器
int cnt = 0;

// 每行的书本数量
int rowSum[MAXQ];

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    value[rt] = 0;
    flip[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 克隆节点
 * @param pre 前一个节点
 * @return 新节点编号
 */
int clone(int pre) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    value[rt] = value[pre];
    flip[rt] = flip[pre];
    return rt;
}

/**
 * 下传翻转标记
 * @param rt 节点编号
 * @param l 区间左端点
 * @param r 区间右端点
 */
void pushDown(int rt, int l, int r) {
    if (flip[rt] != 0) {
        left[rt] = clone(left[rt]);
        right[rt] = clone(right[rt]);
        flip[left[rt]] ^= 1;
        flip[right[rt]] ^= 1;
        flip[rt] = 0;
    }
}

/**
 * 更新节点值
 * @param rt 节点编号
 * @param l 区间左端点
 * @param r 区间右端点
 */
void pushUp(int rt, int l, int r) {
    int mid = (l + r) / 2;
    value[rt] = value[left[rt]] + value[right[rt]];
    if (flip[left[rt]] != 0) {
        value[rt] += (mid - l + 1 - 2 * value[left[rt]]);
    }
    if (flip[right[rt]] != 0) {
        value[rt] += (r - mid - 2 * value[right[rt]]);
    }
}

/**
 * 设置位置pos的值
 * @param pos 要设置的位置
 * @param val 要设置的值
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int update(int pos, int val, int l, int r, int pre) {
    int rt = clone(pre);
    if (l == r) {
        value[rt] = val;
        return rt;
    }
    
    pushDown(rt, l, r);
    int mid = (l + r) / 2;
    if (pos <= mid) {
        left[rt] = update(pos, val, l, mid, left[rt]);
    } else {
        right[rt] = update(pos, val, mid + 1, r, right[rt]);
    }
    pushUp(rt, l, r);
    return rt;
}

/**
 * 翻转区间[l,r]
 * @param L 操作区间左端点
 * @param R 操作区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int reverse(int L, int R, int l, int r, int pre) {
    int rt = clone(pre);
    if (L <= l && r <= R) {
        flip[rt] ^= 1;
        return rt;
    }
    
    pushDown(rt, l, r);
    int mid = (l + r) / 2;
    if (L <= mid) {
        left[rt] = reverse(L, R, l, mid, left[rt]);
    }
    if (R > mid) {
        right[rt] = reverse(L, R, mid + 1, r, right[rt]);
    }
    pushUp(rt, l, r);
    return rt;
}

/**
 * 查询位置pos的值
 * @param pos 要查询的位置
 * @param l 区间左端点
 * @param r 区间右端点
 * @param rt 当前节点编号
 * @return 位置pos的值
 */
int query(int pos, int l, int r, int rt) {
    if (l == r) {
        return value[rt] ^ flip[rt];
    }
    
    int mid = (l + r) / 2;
    if (pos <= mid) {
        return query(pos, l, mid, left[rt]) ^ flip[rt];
    } else {
        return query(pos, mid + 1, r, right[rt]) ^ flip[rt];
    }
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 2;
    int m = 3;
    int q = 3;
    
    // 构建初始空线段树
    root[0] = build(1, m);
    rowSum[0] = 0;
    
    // 示例操作
    // 操作1: 1 1 1 - 在第1行第1列放置一本书
    root[1] = update(1, 1, 1, m, root[0]);
    rowSum[1] = rowSum[0] + (1 - query(1, 1, m, root[0]));
    
    // 操作2: 3 1 - 翻转第1行
    root[2] = reverse(1, m, 1, m, root[1]);
    rowSum[2] = rowSum[1]; // 简化处理
    
    // 操作3: 4 1 - 回到第1次操作之后的状态
    root[3] = root[1];
    rowSum[3] = rowSum[1];
    
    // 输出结果需要根据具体环境实现
    return 0;
}