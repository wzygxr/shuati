#include <cstdio>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 带权并查集解决区间和问题 (C++版本)
 * 
 * 问题分析：
 * 给定一些区间的和，查询其他区间的和
 * 
 * 核心思想：
 * 1. 将区间和问题转化为前缀和问题：区间[l,r]的和等于sum[r] - sum[l-1]
 * 2. 使用带权并查集维护前缀和之间的关系
 * 3. dist[i] 表示 sum[i] - sum[find(i)]，即节点i到其根节点的"距离"
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O((m+q) * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 区间和查询与更新
 * - 差分约束系统
 * - 数据一致性验证
 */

const int MAXN = 100002;
const long long INF = 9223372036854775807LL; // LLONG_MAX

int n, m, q;
int father[MAXN];
long long dist[MAXN];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个节点为自己所在集合的代表
    for (int i = 0; i <= n; i++) {
        father[i] = i;
        // 初始时每个节点到根节点的距离为0
        dist[i] = 0;
    }
}

/**
 * 查找节点i的根节点，并进行路径压缩
 * 同时更新dist[i]为节点i到根节点的距离
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的节点
 * @return 节点i所在集合的根节点
 */
int find(int i) {
    // 如果不是根节点
    if (i != father[i]) {
        // 保存父节点
        int tmp = father[i];
        // 递归查找根节点，同时进行路径压缩
        father[i] = find(tmp);
        // 更新距离：当前节点到根节点的距离 = 当前节点到父节点的距离 + 父节点到根节点的距离
        dist[i] += dist[tmp];
    }
    return father[i];
}

/**
 * 合并两个集合，建立l和r之间的关系
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param l 左边界
 * @param r 右边界+1（转换为前缀和表示）
 * @param v 区间和值
 */
void unionSets(int l, int r, long long v) {
    // 查找两个节点的根节点
    int lf = find(l), rf = find(r);
    // 如果不在同一集合中
    if (lf != rf) {
        // 合并两个集合
        father[lf] = rf;
        // 更新距离关系：
        // sum[lf] - sum[rf] = v + (sum[r] - sum[rf]) - (sum[l] - sum[lf])
        // 整理得：dist[lf] = v + dist[r] - dist[l]
        dist[lf] = v + dist[r] - dist[l];
    }
}

/**
 * 查询区间[l,r]的和
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param l 查询区间左边界
 * @param r 查询区间右边界+1（转换为前缀和表示）
 * @return 区间和，如果无法确定则返回INF
 */
long long query(int l, int r) {
    // 如果两个节点不在同一集合中，无法确定关系
    if (find(l) != find(r)) {
        return INF;
    }
    // 区间[l,r]的和 = sum[r] - sum[l-1] = (sum[r] - sum[find(r)]) - (sum[l-1] - sum[find(l-1)])
    // 由于find(l) == find(r)，所以结果为 dist[l-1] - dist[r]
    return dist[l] - dist[r];
}

int main() {
    scanf("%d%d%d", &n, &m, &q);
    // n+1是为了处理前缀和，将区间[l,r]转换为sum[r] - sum[l-1]
    n = n + 1;
    prepare();
    
    // 处理m个给定条件
    for (int i = 1; i <= m; i++) {
        int l, r;
        long long v;
        scanf("%d%d%lld", &l, &r, &v);
        // 转换为前缀和表示
        r = r + 1;
        unionSets(l, r, v);
    }
    
    // 处理q个查询
    for (int i = 1; i <= q; i++) {
        int l, r;
        long long v;
        scanf("%d%d", &l, &r);
        // 转换为前缀和表示
        r = r + 1;
        v = query(l, r);
        if (v == INF) {
            printf("UNKNOWN\n");
        } else {
            printf("%lld\n", v);
        }
    }
    
    return 0;
}