/**
 * 范围修改的可持久化线段树，标记永久化减少空间占用，C++版
 * 
 * 题目来源: HDU 4348 To the moon
 * 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=4348
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，时间戳t=0，arr认为是0版本的数组
 * 一共有m条查询，每条查询为如下四种类型中的一种
 * C x y z : 当前时间戳t版本的数组，[x..y]范围每个数字增加z，得到t+1版本数组，并且t++
 * Q x y   : 当前时间戳t版本的数组，打印[x..y]范围累加和
 * H x y z : z版本的数组，打印[x..y]范围的累加和
 * B x     : 当前时间戳t设置成x
 * 
 * 解题思路:
 * 使用标记永久化技术实现可持久化线段树，以减少空间占用。
 * 1. 标记永久化是一种优化技巧，在处理区间更新时，不立即下传标记，
 *    而是在查询时根据路径上的标记计算结果
 * 2. 在更新时，只复制被修改路径上的节点，共享未修改的部分
 * 3. 通过标记永久化减少节点复制，从而减少空间占用
 * 4. sum数组存储的是考虑所有任务后的累加和，而不是真实累加和
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

const int MAXN = 100001;
const int MAXT = MAXN * 25;
int n, m, t = 0;
long long arr[MAXN];
int root[MAXN];
int ls[MAXT];
int rs[MAXT];
long long sum[MAXT];
long long addTag[MAXT];
int cnt = 0;

// 自定义max函数
int my_max(int a, int b) {
    return a > b ? a : b;
}

// 自定义min函数
int my_min(int a, int b) {
    return a < b ? a : b;
}

/**
 * 构建线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    addTag[rt] = 0;
    if (l == r) {
        sum[rt] = arr[l];
    } else {
        int mid = (l + r) / 2;
        ls[rt] = build(l, mid);
        rs[rt] = build(mid + 1, r);
        sum[rt] = sum[ls[rt]] + sum[rs[rt]];
    }
    return rt;
}

/**
 * 区间增加操作（标记永久化）
 * @param jobl 操作区间左端点
 * @param jobr 操作区间右端点
 * @param jobv 增加的值
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 * @return 新节点编号
 */
int add(int jobl, int jobr, long long jobv, int l, int r, int i) {
    cnt++;
    int rt = cnt, a = my_max(jobl, l), b = my_min(jobr, r);
    ls[rt] = ls[i];
    rs[rt] = rs[i];
    sum[rt] = sum[i] + jobv * (b - a + 1);
    addTag[rt] = addTag[i];
    
    if (jobl <= l && r <= jobr) {
        addTag[rt] += jobv;
    } else {
        int mid = (l + r) / 2;
        if (jobl <= mid) {
            ls[rt] = add(jobl, jobr, jobv, l, mid, ls[rt]);
        }
        if (jobr > mid) {
            rs[rt] = add(jobl, jobr, jobv, mid + 1, r, rs[rt]);
        }
    }
    return rt;
}

/**
 * 区间查询操作（标记永久化）
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param addHistory 历史标记累加值
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 * @return 区间和
 */
long long query(int jobl, int jobr, long long addHistory, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i] + addHistory * (r - l + 1);
    }
    int mid = (l + r) / 2;
    long long ans = 0;
    if (jobl <= mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], l, mid, ls[i]);
    }
    if (jobr > mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], mid + 1, r, rs[i]);
    }
    return ans;
}

int main() {
    // 读取n和m
    // n = 0;
    // m = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 初始化数组
    for (int i = 1; i <= n; i++) {
        arr[i] = 0;  // 实际使用时需要读取输入
    }
    
    root[0] = build(1, n);
    
    // 操作处理
    for (int i = 1; i <= m; i++) {
        // 实际使用时需要读取操作类型和参数
        // char op;
        // int x, y;
        // long long z;
        // op = ' ';  // 实际使用时需要读取输入
        
        // if (op == 'C') {
        //     // 实际使用时需要读取x, y, z
        //     cnt++;
        //     root[t + 1] = add(x, y, z, 1, n, root[t]);
        //     t++;
        // } else if (op == 'Q') {
        //     // 实际使用时需要读取x, y
        //     // 实际使用时需要输出结果
        //     query(x, y, 0, 1, n, root[t]);
        // } else if (op == 'H') {
        //     // 实际使用时需要读取x, y, z
        //     // 实际使用时需要输出结果
        //     query(x, y, 0, 1, n, root[z]);
        // } else {
        //     // 实际使用时需要读取x
        //     t = x;
        // }
    }
    
    return 0;
}