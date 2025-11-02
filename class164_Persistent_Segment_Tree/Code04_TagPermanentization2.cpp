/**
 * 标记永久化，范围增加 + 查询累加和，C++版
 * 
 * 题目来源: 洛谷 P3372 【模板】线段树 1
 * 题目链接: https://www.luogu.com.cn/problem/P3372
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，一共有m条操作，操作类型如下
 * 1 x y k : 将区间[x, y]每个数加上k
 * 2 x y   : 打印区间[x, y]的累加和
 * 
 * 解题思路:
 * 使用标记永久化技术实现线段树。
 * 1. 标记永久化是一种优化技巧，在处理区间更新时，不立即下传标记，
 *    而是在查询时根据路径上的标记计算结果
 * 2. 在更新时，直接在经过的节点上记录增量，并更新sum值
 * 3. 在查询时，累加路径上所有节点的标记影响
 * 
 * 时间复杂度: O(log n)每次操作
 * 空间复杂度: O(n)
 * 
 * 1 <= n, m <= 10^5
 * -10^9 <= arr[i] <= 10^9
 * -10^9 <= k <= 10^9
 * 
 * 示例:
 * 输入:
 * 5 5
 * 1 5 4 2 3
 * 2 1 4
 * 1 2 3 2
 * 2 1 4
 * 1 1 5 1
 * 2 1 4
 * 
 * 输出:
 * 12
 * 14
 * 15
 */

const int MAXN = 100001;
long long arr[MAXN];
long long sum[MAXN << 2];
long long addTag[MAXN << 2];

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
 * @param i 当前节点编号
 */
void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = arr[l];
    } else {
        int mid = (l + r) / 2;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }
    addTag[i] = 0;
}

/**
 * 区间增加操作（标记永久化）
 * @param jobl 操作区间左端点
 * @param jobr 操作区间右端点
 * @param jobv 增加的值
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 */
void add(int jobl, int jobr, long long jobv, int l, int r, int i) {
    // 计算当前节点对总和的贡献
    int a = my_max(jobl, l), b = my_min(jobr, r);
    sum[i] += jobv * (b - a + 1);
    
    if (jobl <= l && r <= jobr) {
        // 完全覆盖当前区间，打上标记
        addTag[i] += jobv;
    } else {
        // 部分覆盖，递归处理子区间
        int mid = (l + r) / 2;
        if (jobl <= mid) {
            add(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
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
        // 完全覆盖当前区间，返回结果
        return sum[i] + addHistory * (r - l + 1);
    }
    
    int mid = (l + r) >> 1;
    long long ans = 0;
    
    // 累加当前节点的标记影响
    if (jobl <= mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], l, mid, i << 1);
    }
    if (jobr > mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], mid + 1, r, i << 1 | 1);
    }
    
    return ans;
}

int main() {
    int n, m;
    // 读取n和m
    n = 0;
    m = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 初始化数组
    for (int i = 1; i <= n; i++) {
        arr[i] = 0;  // 实际使用时需要读取输入
    }
    
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        int op, jobl, jobr;
        long long jobv;
        op = 0;  // 实际使用时需要读取输入
        
        if (op == 1) {
            // 区间增加操作
            // 实际使用时需要读取jobl, jobr, jobv
            add(jobl, jobr, jobv, 1, n, 1);
        } else {
            // 区间查询操作
            // 实际使用时需要读取jobl, jobr
            // 实际使用时需要输出结果
            query(jobl, jobr, 0, 1, n, 1);
        }
    }
    
    return 0;
}