// Tree and Queries问题 - 树上Mo算法实现 (C++版本)
// 题目来源: Codeforces
// 题目链接: https://codeforces.com/problemset/problem/375/D
// 题目大意: 给定一棵树，每个节点有一个颜色，有q次查询
// 每次查询某个子树内出现次数>=k的颜色数量
// 约束条件: 1 <= n, q <= 10^5
// 解法: 树上Mo算法（离线分块）+ 欧拉序
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 是否最优解: 是，树上Mo算法是解决此类树上区间查询问题的经典方法

#include <iostream>
#include <algorithm>
#include <cmath>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 100001;
int n, q, blen;
int color[MAXN]; // 节点颜色数组
int ans[MAXN];
int count[MAXN]; // 颜色计数数组，记录每种颜色在当前窗口中的出现次数
int colorCount[MAXN]; // 颜色出现次数的计数数组，记录出现i次的颜色数量
int curAns = 0; // 当前窗口中满足条件的颜色数量

// 邻接表存储树结构
vector<int> graph[MAXN];

// 欧拉序相关变量
int euler[2 * MAXN]; // 欧拉序数组，记录DFS访问节点的顺序
int first[MAXN]; // 节点第一次出现在欧拉序中的位置
int last[MAXN]; // 节点最后一次出现在欧拉序中的位置
int eulerIdx = 0; // 欧拉序索引

// 查询结构体，用于存储查询信息
struct Query {
    int l, r, k, id;
    
    // 重载小于运算符，用于排序
    bool operator<(const Query& other) const {
        int blockA = (l - 1) / blen;
        int blockB = (other.l - 1) / blen;
        if (blockA != blockB) {
            return blockA < blockB;
        }
        return r < other.r;
    }
};

Query queries[MAXN];

// 添加元素到当前窗口
// 时间复杂度: O(1)
// 设计思路: 更新颜色计数和颜色出现次数的计数
void add(int pos) {
    int col = color[euler[pos]];
    // 更新颜色出现次数的计数
    colorCount[count[col]]--;
    count[col]++;
    colorCount[count[col]]++;
}

// 从当前窗口移除元素
// 时间复杂度: O(1)
// 设计思路: 更新颜色计数和颜色出现次数的计数
void remove(int pos) {
    int col = color[euler[pos]];
    // 更新颜色出现次数的计数
    colorCount[count[col]]--;
    count[col]--;
    colorCount[count[col]]++;
}

// DFS生成欧拉序
// 时间复杂度: O(n)
// 设计思路: 通过DFS遍历树，记录每个节点的进入和离开时间，形成欧拉序
// 这样可以将子树查询转换为区间查询
void dfs(int u, int parent) {
    eulerIdx++;
    euler[eulerIdx] = u;
    first[u] = eulerIdx;
    
    for (int v : graph[u]) {
        if (v != parent) {
            dfs(v, u);
        }
    }
    
    eulerIdx++;
    euler[eulerIdx] = u;
    last[u] = eulerIdx;
}

// Mo算法主函数
// 时间复杂度: O((n + q) * sqrt(n))
// 设计思路: 通过巧妙的排序策略，使得相邻查询之间的指针移动次数最少
void moAlgorithm() {
    // 对查询进行排序
    sort(queries + 1, queries + q + 1);
    
    // Mo算法处理
    int curL = 1, curR = 0;
    for (int i = 1; i <= q; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int k = queries[i].k;
        int id = queries[i].id;
        
        // 扩展右边界
        while (curR < r) {
            curR++;
            add(curR);
        }
        
        // 收缩右边界
        while (curR > r) {
            remove(curR);
            curR--;
        }
        
        // 收缩左边界
        while (curL < l) {
            remove(curL);
            curL++;
        }
        
        // 扩展左边界
        while (curL > l) {
            curL--;
            add(curL);
        }
        
        // 计算答案：出现次数>=k的颜色数量
        int result = 0;
        for (int j = k; j < MAXN; j++) {
            result += colorCount[j];
        }
        ans[id] = result;
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> q;
    
    for (int i = 1; i <= n; i++) {
        cin >> color[i];
    }
    
    // 读取边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 生成欧拉序
    eulerIdx = 0;
    dfs(1, 0);
    
    // 读取查询
    for (int i = 1; i <= q; i++) {
        int v, k;
        cin >> v >> k;
        // 转换为欧拉序上的区间查询
        queries[i] = {first[v], last[v], k, i};
    }
    
    // 计算块大小
    blen = (int)sqrt(2 * n);
    
    // 初始化计数数组
    memset(count, 0, sizeof(count));
    memset(colorCount, 0, sizeof(colorCount));
    
    // Mo算法处理
    moAlgorithm();
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}