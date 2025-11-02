/**
 * Codeforces 813E - Army Creation
 * 
 * 题目来源: Codeforces 813E
 * 题目链接: https://codeforces.com/problemset/problem/813/E
 * 
 * 题目描述:
 * Vova非常喜欢玩电脑游戏，现在他正在玩一款叫做Rage of Empires的策略游戏。
 * 在这个游戏里，Vova可以雇佣n个不同的战士，第i个战士的类型为ai。
 * Vova想要雇佣其中一些战士，从而建立一支平衡的军队。
 * 如果对于任何一种类型，军队中这种类型的战士不超过k个，那么这支军队就是平衡的。
 * 现在Vova有q个计划，第i个计划他只能雇佣区间[li, ri]之间的战士。
 * 对于每个计划，你需要求出可以组建的平衡军队的最多人数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决限制性区间选择问题。
 * 1. 预处理：对于每个位置i，计算next[i]表示从位置i开始，第k+1个与a[i]相同元素的位置
 * 2. 建立可持久化线段树，每个位置对应一个版本
 * 3. 对于每个查询，在对应区间的线段树版本中查询区间和
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 约束条件:
 * 1 <= n, q <= 10^5
 * 1 <= k <= n
 * 1 <= ai <= 10^9
 * 1 <= li <= ri <= n
 * 
 * 示例:
 * 输入:
 * 5 2
 * 1 1 2 1 1
 * 3
 * 1 5
 * 2 5
 * 1 3
 * 
 * 输出:
 * 4
 * 3
 * 3
 */

const int MAXN = 100010;

// 原始数据
int arr[MAXN];

// 预处理相关
int next_pos[MAXN];
// 由于C++中没有内置的HashMap，这里简化处理

// 可持久化线段树
int root[MAXN];
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];
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
 * 构建空线段树
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 区间更新操作（单点更新）
 */
int update(int pos, int val, int l, int r, int pre) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + val;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = update(pos, val, l, mid, left[rt]);
        } else {
            right[rt] = update(pos, val, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 区间查询操作
 */
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) / 2;
    int ans = 0;
    if (jobl <= mid) {
        ans += query(jobl, jobr, l, mid, left[i]);
    }
    if (jobr > mid) {
        ans += query(jobl, jobr, mid + 1, r, right[i]);
    }
    return ans;
}

int main() {
    // 读取n和k
    // int n, k;
    // n = 0;
    // k = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 读取数据
    // for (int i = 1; i <= n; i++) {
    //     arr[i] = 0;  // 实际使用时需要读取输入
    // }
    
    // 预处理next数组
    // 这里需要根据具体需求实现
    
    // 构建初始线段树
    // root[0] = build(1, n);
    
    // 逐个插入元素，构建可持久化线段树
    // for (int i = 1; i <= n; i++) {
    //     // 在位置i处+1，在位置next[i]处-1
    //     root[i] = update(i, 1, 1, n, root[i-1]);
    //     if (next_pos[i] <= n) {
    //         root[i] = update(next_pos[i], -1, 1, n, root[i]);
    //     }
    // }
    
    // 读取q
    // int q;
    // q = 0;
    // int last_ans = 0;
    
    // 处理查询
    // for (int i = 1; i <= q; i++) {
    //     int l, r;
    //     // 实际使用时需要读取l, r
    //     
    //     // 异或上一次的答案
    //     l = (l + last_ans - 1) % n + 1;
    //     r = (r + last_ans - 1) % n + 1;
    //     
    //     if (l > r) {
    //         int temp = l;
    //         l = r;
    //         r = temp;
    //     }
    //     
    //     // 查询区间[l,r]的和
    //     int result = query(l, r, 1, n, root[r]);
    //     // 实际使用时需要输出结果
    //     last_ans = result;
    // }
    
    return 0;
}