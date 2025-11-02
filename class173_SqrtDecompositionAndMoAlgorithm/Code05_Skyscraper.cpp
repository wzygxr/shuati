// 雅加达的摩天楼问题 - 分块算法优化BFS (C++版本)
// 题目来源: https://www.luogu.com.cn/problem/P3645
// 题目来源: https://uoj.ac/problem/111
// 题目大意: 有n个大楼，编号0~n-1，有m个狗子，编号0~m-1
// 每只狗子有两个参数，idx表示狗子的初始大楼，jump表示狗子的跳跃能力
// 狗子在i位置，可以来到 i - jump 或 i + jump，向左向右自由跳跃，但不能越界
// 0号狗子有消息希望传给1号狗子，所有狗子都可帮忙，返回至少传送几次，无法送达打印-1
// 约束条件: 1 <= n、m <= 30000

#include <cstdio>
#include <deque>
#include <bitset>
using namespace std;

// BFS节点结构体，记录当前位置、跳跃能力和已用时间
struct Node {
    int idx, jump, time;
};

const int MAXN = 30001;
int n, m;
int head[MAXN];
int nxt[MAXN];
int to[MAXN];
int cnt;

deque<Node> que;
bitset<MAXN> vis[MAXN];

/**
 * 添加狗子到邻接表
 * @param idx 大楼编号
 * @param jump 跳跃能力
 */
void add(int idx, int jump) {
    // 创建新节点
    nxt[++cnt] = head[idx];
    to[cnt] = jump;
    head[idx] = cnt;
}

/**
 * 触发大楼idx中的所有狗子
 * @param idx 大楼编号
 * @param time 当前时间
 */
void trigger(int idx, int time) {
    // 遍历大楼idx中的所有狗子
    for (int e = head[idx], jump; e; e = nxt[e]) {
        jump = to[e];
        // 如果这个状态（位置+跳跃能力）没有访问过
        if (!vis[idx].test(jump)) {
            // 标记为已访问
            vis[idx].set(jump);
            // 加入队列
            que.push_back({idx, jump, time});
        }
    }
    // 清空该大楼的狗子列表，避免重复处理
    head[idx] = 0;
}

/**
 * 扩展状态
 * @param idx 大楼编号
 * @param jump 跳跃能力
 * @param time 当前时间
 */
void extend(int idx, int jump, int time) {
    // 触发该大楼的所有狗子
    trigger(idx, time);
    
    // 如果这个状态（位置+跳跃能力）没有访问过
    if (!vis[idx].test(jump)) {
        // 标记为已访问
        vis[idx].set(jump);
        // 加入队列
        que.push_back({idx, jump, time});
    }
}

/**
 * BFS搜索最短路径
 * @param s 起始大楼
 * @param t 目标大楼
 * @return 最少传送次数，无法送达返回-1
 */
int bfs(int s, int t) {
    // 如果起始和目标相同，不需要传送
    if (s == t) {
        return 0;
    }
    
    // 初始化vis数组
    for (int i = 0; i < n; i++) {
        vis[i].reset();
    }
    
    // 清空队列
    while (!que.empty()) {
        que.pop_front();
    }
    
    // 触发起始大楼的所有狗子
    trigger(s, 0);
    
    // BFS过程
    while (!que.empty()) {
        // 取出队首节点
        Node cur = que.front();
        que.pop_front();
        int idx = cur.idx;
        int jump = cur.jump;
        int time = cur.time;
        
        // 如果向左或向右跳跃能到达目标大楼
        if (idx - jump == t || idx + jump == t) {
            // 返回传送次数+1
            return time + 1;
        }
        
        // 向左跳跃
        if (idx - jump >= 0) {
            extend(idx - jump, jump, time + 1);
        }
        
        // 向右跳跃
        if (idx + jump < n) {
            extend(idx + jump, jump, time + 1);
        }
    }
    // 无法送达
    return -1;
}

int main() {
    // 读取大楼数量n和狗子数量m
    scanf("%d%d", &n, &m);
    
    // 读取起始狗子和目标狗子的信息
    int s, sjump, t, tjump;
    scanf("%d%d%d%d", &s, &sjump, &t, &tjump);
    
    // 添加起始和目标狗子
    add(s, sjump);
    add(t, tjump);
    
    // 读取其他狗子的信息
    for (int i = 2, idx, jump; i < m; i++) {
        scanf("%d%d", &idx, &jump);
        add(idx, jump);
    }
    
    // BFS搜索最短路径
    printf("%d\n", bfs(s, t));
    return 0;
}