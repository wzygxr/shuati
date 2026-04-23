### 题目链接
https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1140

### 题目描述
给定一个无向图，求需要导游的城市数量（即割点数量）。

### 笔试/面试考察点分析
- 考察割点的概念和Tarjan算法的应用
- 输入格式的特殊处理（城市名称）
- 时间复杂度分析（O(n+m)）
- 结果的输出格式（按城市名称排序输出）

### 解题思路
1. 将城市名称映射为节点编号
2. 使用Tarjan算法遍历无向图
3. 统计割点总数并按名称排序输出

### 代码实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <map>
#include <set>
using namespace std;

const int MAXN = 105;
vector<int> adj[MAXN];
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_cut[MAXN];
map<string, int> city_to_id;
map<int, string> id_to_city;
int city_cnt = 0;

void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp;
    int child = 0;
    for (int v : adj[u]) {
        if (v == fa) continue;
        if (!dfn[v]) {
            child++;
            tarjan(v, u);
            low[u] = min(low[u], low[v]);
            if (fa == -1 && child >= 2) {
                is_cut[u] = true;
            }
            if (fa != -1 && low[v] >= dfn[u]) {
                is_cut[u] = true;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    int n, m;
    int case_num = 1;
    while (cin >> n && n != 0) {
        city_to_id.clear();
        id_to_city.clear();
        city_cnt = 0;
        for (int i = 0; i < n; ++i) {
            string city;
            cin >> city;
            city_to_id[city] = city_cnt;
            id_to_city[city_cnt] = city;
            city_cnt++;
        }
        
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        timestamp = 0;
        for (int i = 0; i < n; ++i) adj[i].clear();
        
        cin >> m;
        for (int i = 0; i < m; ++i) {
            string city1, city2;
            cin >> city1 >> city2;
            int u = city_to_id[city1];
            int v = city_to_id[city2];
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        for (int i = 0; i < n; ++i) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        
        set<string> cut_cities;
        for (int i = 0; i < n; ++i) {
            if (is_cut[i]) {
                cut_cities.insert(id_to_city[i]);
            }
        }
        
        if (case_num > 1) cout << endl;
        cout << "City map #" << case_num << ": " << cut_cities.size() << " camera(s) found" << endl;
        for (string city : cut_cities) {
            cout << city << endl;
        }
        case_num++;
    }
    
    return 0;
}
```

### 代码逐行注释
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <map>
#include <set>
using namespace std;

const int MAXN = 105;
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp = 0; // 时间戳数组和计数器
bool is_cut[MAXN]; // 标记节点是否为割点
map<string, int> city_to_id; // 城市名称到节点编号的映射
map<int, string> id_to_city; // 节点编号到城市名称的映射
int city_cnt = 0; // 城市数量

// Tarjan算法求割点
// u: 当前节点，fa: 父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    int child = 0; // 统计子节点数量
    for (int v : adj[u]) { // 遍历所有邻接节点
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 邻接节点未被访问
            child++;
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 更新low值
            
            // 根节点割点判定：有≥2个子树
            if (fa == -1 && child >= 2) {
                is_cut[u] = true;
            }
            // 非根节点割点判定：存在子节点v，low[v] ≥ dfn[u]
            if (fa != -1 && low[v] >= dfn[u]) {
                is_cut[u] = true;
            }
        } else { // 邻接节点已被访问，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    int n, m;
    int case_num = 1;
    while (cin >> n && n != 0) {
        city_to_id.clear();
        id_to_city.clear();
        city_cnt = 0;
        for (int i = 0; i < n; ++i) {
            string city;
            cin >> city;
            city_to_id[city] = city_cnt;
            id_to_city[city_cnt] = city;
            city_cnt++;
        }
        
        // 初始化数组
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        timestamp = 0;
        for (int i = 0; i < n; ++i) adj[i].clear();
        
        cin >> m;
        for (int i = 0; i < m; ++i) {
            string city1, city2;
            cin >> city1 >> city2;
            int u = city_to_id[city1];
            int v = city_to_id[city2];
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        // 遍历所有连通块
        for (int i = 0; i < n; ++i) {
            if (!dfn[i]) {
                tarjan(i, -1); // 根节点fa=-1
            }
        }
        
        // 收集割点并按名称排序
        set<string> cut_cities;
        for (int i = 0; i < n; ++i) {
            if (is_cut[i]) {
                cut_cities.insert(id_to_city[i]);
            }
        }
        
        // 输出结果
        if (case_num > 1) cout << endl;
        cout << "City map #" << case_num << ": " << cut_cities.size() << " camera(s) found" << endl;
        for (string city : cut_cities) {
            cout << city << endl;
        }
        case_num++;
    }
    
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(n+m)，n为节点数，m为边数
- 空间复杂度：O(n+m)，存储邻接表和映射关系

### 同类题目拓展
- POJ 1144 Network（割点基础题）
- POJ 3177 冗余路径（边双连通分量）
- POJ 2117 电力（割点应用）
- 洛谷 3225 矿场搭建（点双连通分量）

### ML/DL关联思考
- 割点识别可用于图结构数据的关键节点提取
- 点双连通分量可作为图社区划分的基础
- 在GNN中，割点和点双分量可作为图的结构特征，提升模型性能