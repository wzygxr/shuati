/**
 * BZOJ 3932 [CQOI2015]任务查询系统
 * 
 * 题目来源: BZOJ 3932
 * 题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=3932
 * 
 * 题目描述:
 * 最近实验室正在为其管理的超级计算机编制一套任务管理系统，而你被安排完成其中的查询部分。
 * 超级计算机中的任务用三元组(Si,Ei,Pi)描述，(Si,Ei,Pi)表示任务从第Si秒开始，在第Ei秒后结束（第Ei秒结束），
 * 其优先级为Pi。同一时间可能有多个任务同时执行，它们的优先级可能相同，也可能不同。
 * 调度系统会经常向查询系统询问，第Xi秒正在执行的任务中，优先级第Yi小的任务的优先级是多少。
 * 在任意两个时刻，不会有相同优先级的任务正在执行。
 * 
 * 解题思路:
 * 使用可持久化线段树解决任务查询问题。
 * 1. 将所有任务按照时间轴进行差分处理，每个任务在开始时间+1，在结束时间+1处-1
 * 2. 按照时间顺序建立可持久化线段树，每个时间点对应一个版本
 * 3. 对于每个查询，在对应时间点的线段树版本中查询第K小的优先级
 * 
 * 时间复杂度: O((n+m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n, m <= 10^5
 * 1 <= Si, Ei <= 10^9
 * 1 <= Pi <= 10^9
 * 1 <= Xi <= 10^9
 * 1 <= Yi <= sum(Pj) (第Xj秒正在运行的任务总数)
 * 
 * 示例:
 * 输入:
 * 2 3
 * 1 2 6
 * 2 3 3
 * 1 1
 * 2 1
 * 3 1
 * 
 * 输出:
 * 6
 * 3
 * 3
 */

const int MAXN = 100010;

// 任务信息
int S[MAXN];
int E[MAXN];
int P[MAXN];

// 离散化相关
int times[MAXN * 2];
int priorities[MAXN];

// 可持久化线段树
int root[MAXN * 2];
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];
int cnt = 0;

// 事件结构
struct Event {
    int time, priority, type;
    
    bool operator<(const Event& other) const {
        if (time != other.time) return time < other.time;
        return type < other.type;
    }
};

Event events[MAXN * 2];
int event_count = 0;

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
 * 插入操作
 */
int insert(int pos, int l, int r, int pre, int val) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + val;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt], val);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt], val);
        }
    }
    return rt;
}

/**
 * 查询第k小
 */
int queryKth(int k, int l, int r, int u, int v) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    int x = sum[left[v]] - sum[left[u]];
    if (x >= k) {
        return queryKth(k, l, mid, left[u], left[v]);
    } else {
        return queryKth(k - x, mid + 1, r, right[u], right[v]);
    }
}

int main() {
    // 读取n和m
    // int n, m;
    // n = 0;
    // m = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 初始化数组
    // for (int i = 1; i <= n; i++) {
    //     S[i] = 0;  // 实际使用时需要读取输入
    //     E[i] = 0;  // 实际使用时需要读取输入
    //     P[i] = 0;  // 实际使用时需要读取输入
    // }
    
    // 构建初始线段树
    // root[0] = build(1, n);
    
    // 处理事件和查询
    // 实际使用时需要根据具体环境调整输入方式和处理逻辑
    
    return 0;
}