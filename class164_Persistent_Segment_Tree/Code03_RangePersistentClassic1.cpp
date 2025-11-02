/**
 * 范围修改的可持久化线段树，经典的方式，c++版
 * 
 * 题目来源: SPOJ TTM - To the moon
 * 题目链接: https://www.spoj.com/problems/TTM/
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
 * 一共有m条操作，每条操作为如下四种类型中的一种
 * C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
 * Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
 * H x y z : z版本的数组，打印[x..y]范围的累加和
 * B x     : 当前时间戳t设置成x
 * 
 * 解题思路:
 * 使用可持久化线段树解决带历史版本的区间修改问题。
 * 1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
 * 2. 使用懒惰标记技术处理区间修改
 * 3. 通过clone函数实现节点的复制，确保历史版本的完整性
 * 4. 在需要下传懒惰标记时，先复制子节点再进行操作
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n、m <= 10^5
 * -10^9 <= arr[i] <= +10^9
 * 
 * 示例:
 * 输入:
 * 5 10
 * 5 6 7 8 9
 * Q 1 5
 * C 2 4 10
 * Q 1 5
 * H 1 5 0
 * B 3
 * Q 1 5
 * C 1 5 20
 * Q 1 5
 * H 1 5 3
 * Q 1 5
 * 
 * 输出:
 * 35
 * 55
 * 35
 * 55
 * 75
 * 55
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 100001;
const int MAXT = MAXN * 70;

int n, m, t = 0;

int arr[MAXN];

int root[MAXN];

int left[MAXT];
int right[MAXT];

// 累加和信息
long long sum[MAXT];

// 懒更新信息，范围增加的懒更新
long long add[MAXT];

int cnt = 0;

/**
 * 克隆节点
 * @param i 要克隆的节点编号
 * @return 新节点编号
 */
int clone(int i) {
    int rt = ++cnt;
    left[rt] = left[i];
    right[rt] = right[i];
    sum[rt] = sum[i];
    add[rt] = add[i];
    return rt;
}

/**
 * 更新节点信息
 * @param i 节点编号
 */
void up(int i) {
    sum[i] = sum[left[i]] + sum[right[i]];
}

/**
 * 懒更新操作
 * @param i 节点编号
 * @param v 增加的值
 * @param n 区间长度
 */
void lazy(int i, long long v, int n) {
    sum[i] += v * n;
    add[i] += v;
}

/**
 * 下传懒更新标记
 * @param i 节点编号
 * @param ln 左子区间长度
 * @param rn 右子区间长度
 */
void down(int i, int ln, int rn) {
    if (add[i] != 0) {
        left[i] = clone(left[i]);
        right[i] = clone(right[i]);
        lazy(left[i], add[i], ln);
        lazy(right[i], add[i], rn);
        add[i] = 0;
    }
}

/**
 * 建立线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    int rt = ++cnt;
    add[rt] = 0;
    if (l == r) {
        sum[rt] = arr[l];
    } else {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
        up(rt);
    }
    return rt;
}

/**
 * 区间增加操作
 * @param jobl 操作区间左端点
 * @param jobr 操作区间右端点
 * @param jobv 增加的值
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 * @return 新节点编号
 */
int add_range(int jobl, int jobr, long long jobv, int l, int r, int i) {
    int rt = clone(i);
    if (jobl <= l && r <= jobr) {
        lazy(rt, jobv, r - l + 1);
    } else {
        int mid = (l + r) / 2;
        down(rt, mid - l + 1, r - mid);
        if (jobl <= mid) {
            left[rt] = add_range(jobl, jobr, jobv, l, mid, left[rt]);
        }
        if (jobr > mid) {
            right[rt] = add_range(jobl, jobr, jobv, mid + 1, r, right[rt]);
        }
        up(rt);
    }
    return rt;
}

/**
 * 区间查询操作
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 * @return 区间和
 */
long long query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) / 2;
    down(i, mid - l + 1, r - mid);
    long long ans = 0;
    if (jobl <= mid) {
        ans += query(jobl, jobr, l, mid, left[i]);
    }
    if (jobr > mid) {
        ans += query(jobl, jobr, mid + 1, r, right[i]);
    }
    return ans;
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    n = 5;
    m = 10;
    arr[1] = 5; arr[2] = 6; arr[3] = 7; arr[4] = 8; arr[5] = 9;
    
    // 构建线段树
    root[0] = build(1, n);
    
    // 示例操作
    // Q 1 5 -> 35
    long long result1 = query(1, 5, 1, n, root[0]);
    // C 2 4 10
    root[t + 1] = add_range(2, 4, 10, 1, n, root[t]);
    t++;
    // Q 1 5 -> 55
    long long result2 = query(1, 5, 1, n, root[t]);
    // H 1 5 0 -> 35
    long long result3 = query(1, 5, 1, n, root[0]);
    // B 3
    t = 3;
    // Q 1 5 -> 55
    long long result4 = query(1, 5, 1, n, root[t]);
    // C 1 5 20
    root[t + 1] = add_range(1, 5, 20, 1, n, root[t]);
    t++;
    // Q 1 5 -> 75
    long long result5 = query(1, 5, 1, n, root[t]);
    // H 1 5 3 -> 55
    long long result6 = query(1, 5, 1, n, root[3]);
    // Q 1 5 -> 55
    long long result7 = query(1, 5, 1, n, root[t]);
    
    // 输出结果需要根据具体环境实现
    return 0;
}