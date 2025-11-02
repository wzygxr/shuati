// AtCoder Grand Contest 029 F. Construction of a tree
// 平台: AtCoder
// 难度: 2200
// 标签: CDQ分治, 图论, 二分图匹配
// 链接: https://atcoder.jp/contests/agc029/tasks/agc029_f
// 
// 题目描述:
// 给定n-1个集合，每个集合包含1~n中的一些数字
// 要求构造一棵n个节点的树，使得对于每个集合，树中至少有一条边连接集合中的两个节点
// 如果无法构造，输出-1
// 
// 解题思路:
// 1. 将问题转化为二分图匹配问题
// 2. 左边是n-1个集合，右边是n个节点
// 3. 如果集合包含节点，则在集合和节点之间连边
// 4. 使用Hall定理判断是否存在完美匹配
// 5. 如果存在完美匹配，则可以通过匹配构造树
// 
// 时间复杂度: O(n^2) 或 O(n log n) 使用优化的匹配算法

#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100005;

int n;
vector<int> sets[MAXN];  // 存储每个集合包含的节点
vector<int> graph[MAXN]; // 二分图
int match[MAXN];         // 匹配结果
bool visited[MAXN];      // 访问标记

// 二分图匹配的DFS函数
bool dfs(int u) {
    for (int v : graph[u]) {
        if (!visited[v]) {
            visited[v] = true;
            if (match[v] == -1 || dfs(match[v])) {
                match[v] = u;
                return true;
            }
        }
    }
    return false;
}

// 二分图匹配主函数
int bipartiteMatch() {
    memset(match, -1, sizeof(match));
    int result = 0;
    
    for (int i = 0; i < n - 1; i++) {
        memset(visited, false, sizeof(visited));
        if (dfs(i)) {
            result++;
        }
    }
    
    return result;
}

// 检查Hall条件
bool checkHallCondition() {
    // 对于每个子集S ⊆ 左边节点，检查|N(S)| >= |S|
    // 这里使用位运算枚举所有子集
    int leftSize = n - 1;
    
    for (int mask = 1; mask < (1 << leftSize); mask++) {
        set<int> neighbors;
        int setCount = 0;
        
        for (int i = 0; i < leftSize; i++) {
            if (mask & (1 << i)) {
                setCount++;
                for (int node : sets[i]) {
                    neighbors.insert(node);
                }
            }
        }
        
        if (neighbors.size() < setCount) {
            return false;
        }
    }
    
    return true;
}

// 构造树
void constructTree() {
    if (!checkHallCondition()) {
        cout << -1 << endl;
        return;
    }
    
    // 构建二分图
    for (int i = 0; i < n - 1; i++) {
        for (int node : sets[i]) {
            graph[i].push_back(node);
        }
    }
    
    int matchCount = bipartiteMatch();
    
    if (matchCount != n - 1) {
        cout << -1 << endl;
        return;
    }
    
    // 根据匹配结果构造树
    vector<pair<int, int>> edges;
    
    // 找到根节点（没有被匹配的节点）
    int root = -1;
    vector<bool> matched(n + 1, false);
    for (int i = 0; i < n - 1; i++) {
        for (int j = 1; j <= n; j++) {
            if (match[j] == i) {
                matched[j] = true;
                break;
            }
        }
    }
    
    for (int i = 1; i <= n; i++) {
        if (!matched[i]) {
            root = i;
            break;
        }
    }
    
    // 构造边
    for (int i = 0; i < n - 1; i++) {
        for (int j = 1; j <= n; j++) {
            if (match[j] == i) {
                edges.push_back({root, j});
                break;
            }
        }
    }
    
    // 输出结果
    for (auto edge : edges) {
        cout << edge.first << " " << edge.second << endl;
    }
}

int main() {
    cin >> n;
    
    for (int i = 0; i < n - 1; i++) {
        int size;
        cin >> size;
        sets[i].resize(size);
        for (int j = 0; j < size; j++) {
            cin >> sets[i][j];
        }
    }
    
    constructTree();
    
    return 0;
}
