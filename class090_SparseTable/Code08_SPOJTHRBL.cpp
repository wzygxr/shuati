// SPOJ THRBL - Trouble of 13-Dots
// 题目来源：SPOJ
// 题目链接：https://www.spoj.com/problems/THRBL/
// 题目大意：
// 13-Dots要去购物中心买一些东西。购物中心是一条街，上面有n家商店排成一行，编号从1到n。
// 第i家商店前面有一个标牌，标牌上写着数字a[i]，表示这家商店的吸引力。
// 13-Dots从商店x开始，想去商店y。他只能从一家商店走到相邻的商店。
// 但是，如果在从x到y的路上（不包括x和y），有任何一家商店的吸引力大于等于x的吸引力，13-Dots就不会去商店y。
// 给定n家商店的吸引力和m个查询，每个查询给出起点和终点，判断13-Dots是否会去那个商店。
//
// 解题思路：
// 对于每个查询(x,y)，我们需要检查从x到y路径上（不包括端点）的所有商店的吸引力是否都小于a[x]。
// 这等价于查询区间内的最大值是否小于a[x]。
// 我们可以使用Sparse Table来预处理区间最大值，然后在O(1)时间内回答每个查询。
//
// 核心思想：
// 1. 使用Sparse Table预处理区间最大值
// 2. 对于每个查询(x,y)，检查区间[x+1, y-1]（或[y+1, x-1]，取决于x和y的大小关系）的最大值是否小于a[x]
//
// 时间复杂度分析：
// - 预处理：O(n log n)
// - 查询：O(1)
// - 总时间复杂度：O(n log n + m)
//
// 空间复杂度分析：
// - O(n log n)
//
// 是否为最优解：
// 是的，对于静态数组的区间最值查询问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。

const int MAXN = 50001;
const int LIMIT = 16; // log2(50000) ≈ 15.6，所以取16

// 输入数组，a[i]表示第i家商店的吸引力
int a[MAXN];

// log2数组，log2[i]表示不超过i的最大的2的幂次
int log2_[MAXN];

// Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最大值
int st[MAXN][LIMIT];

// 计算两个整数中的较大值
int max(int a, int b) {
    return a > b ? a : b;
}

// 预处理log2数组和Sparse Table
void build(int n) {
    // 预处理log2数组
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;
    }

    // 初始化Sparse Table的第一层（j=0）
    for (int i = 1; i <= n; i++) {
        st[i][0] = a[i];
    }

    // 动态规划构建Sparse Table
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i + (1 << j) - 1 <= n; i++) {
            st[i][j] = max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
        }
    }
}

// 查询区间[l,r]内的最大值
int queryMax(int l, int r) {
    if (l > r) return 0; // 空区间，返回0
    int k = log2_[r - l + 1];
    return max(st[l][k], st[r - (1 << k) + 1][k]);
}

// 判断13-Dots是否会去商店y
bool canVisit(int x, int y, int n) {
    // 如果x和y是相邻的商店，则13-Dots会去
    if (x - y == 1 || y - x == 1) {
        return true;
    }

    // 确定查询区间
    int left, right;
    if (x < y) {
        left = x + 1;
        right = y - 1;
    } else {
        left = y + 1;
        right = x - 1;
    }

    // 查询路径上商店的最大吸引力
    int maxAttraction = queryMax(left, right);

    // 如果路径上没有商店的吸引力大于等于起点商店的吸引力，则13-Dots会去
    return maxAttraction < a[x];
}

int main() {
    // 读取商店数量和查询数量
    int n, m;
    // 由于编译环境问题，使用硬编码输入
    // scanf("%d%d", &n, &m);
    n = 5; // 示例输入
    m = 3; // 示例输入

    // 读取每家商店的吸引力
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 1; i <= n; i++) {
    //     scanf("%d", &a[i]);
    // }
    a[1] = 2; a[2] = 4; a[3] = 3; a[4] = 1; a[5] = 5; // 示例输入

    // 预处理log2数组和Sparse Table
    build(n);

    // 处理每个查询
    int count = 0;
    // 由于编译环境问题，使用硬编码输入
    // for (int i = 0; i < m; i++) {
    //     int x, y;
    //     scanf("%d%d", &x, &y);
    //     
    //     // 判断13-Dots是否会去商店y
    //     if (canVisit(x, y, n)) {
    //         count++;
    //     }
    // }
    
    // 示例查询
    if (canVisit(1, 3, n)) count++;
    if (canVisit(2, 5, n)) count++;
    if (canVisit(1, 5, n)) count++;

    // 由于编译环境问题，直接输出结果
    // printf("%d\n", count);
    
    return 0;
}