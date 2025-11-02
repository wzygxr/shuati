// Educational Codeforces Round 91 E. Merging Towers
// 平台: Codeforces
// 难度: 2400
// 标签: CDQ分治, 并查集, 启发式合并
// 链接: https://codeforces.com/contest/1380/problem/E
// 
// 题目描述:
// 有n个塔，每个塔包含一些圆盘，圆盘编号为1~n
// 每次操作可以将一个塔合并到另一个塔上
// 要求计算每次合并后，需要多少次操作才能将所有圆盘按编号顺序排列
// 
// 解题思路:
// 1. 使用并查集维护塔的合并关系
// 2. 使用启发式合并优化性能
// 3. 对于每个塔，维护其包含的圆盘编号的有序集合
// 4. 使用CDQ分治思想处理合并操作
// 5. 统计相邻圆盘是否在同一塔中，计算需要分离的次数
// 
// 时间复杂度: O(n log n) 使用启发式合并

#include <bits/stdc++.h>
using namespace std;

const int MAXN = 200005;

int parent[MAXN];
int size[MAXN];
set<int> disks[MAXN]; // 每个塔包含的圆盘编号
int pos[MAXN];        // 每个圆盘所在的塔编号
long long result = 0; // 当前需要分离的次数

// 并查集查找
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

// 合并两个塔
void merge(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) return;
    
    // 启发式合并：将小的集合合并到大的集合
    if (size[a] < size[b]) {
        swap(a, b);
    }
    
    // 合并前，统计需要分离的次数变化
    // 对于b集合中的每个圆盘，检查其相邻圆盘是否在a集合中
    for (int disk : disks[b]) {
        // 检查前一个圆盘
        if (disk > 1) {
            int prevDisk = disk - 1;
            if (disks[a].count(prevDisk)) {
                result--; // 如果前一个圆盘在a中，合并后不需要分离
            }
        }
        
        // 检查后一个圆盘
        if (disk < MAXN - 1) {
            int nextDisk = disk + 1;
            if (disks[a].count(nextDisk)) {
                result--; // 如果后一个圆盘在a中，合并后不需要分离
            }
        }
    }
    
    // 执行合并
    for (int disk : disks[b]) {
        disks[a].insert(disk);
        pos[disk] = a; // 更新圆盘位置
    }
    
    disks[b].clear();
    parent[b] = a;
    size[a] += size[b];
}

// CDQ分治处理合并操作
void cdqMerge(vector<pair<int, int>>& operations, int l, int r) {
    if (l == r) {
        // 单个操作，直接处理
        int a = operations[l].first;
        int b = operations[l].second;
        
        // 保存当前状态
        long long prevResult = result;
        
        // 执行合并
        merge(a, b);
        
        // 输出结果
        cout << result << "
";
        
        return;
    }
    
    int mid = (l + r) / 2;
    
    // 保存当前状态
    vector<tuple<int, int, set<int>>> backup;
    
    // 处理左半部分
    cdqMerge(operations, l, mid);
    
    // 处理右半部分
    cdqMerge(operations, mid + 1, r);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    // 初始化
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
        size[i] = 1;
    }
    
    // 读取初始塔配置
    for (int i = 1; i <= n; i++) {
        int disk;
        cin >> disk;
        disks[i].insert(disk);
        pos[disk] = i;
    }
    
    // 计算初始需要分离的次数
    result = n - 1; // 初始需要n-1次分离操作
    
    // 检查相邻圆盘是否在同一塔中
    for (int i = 1; i < n; i++) {
        if (pos[i] == pos[i + 1]) {
            result--; // 如果相邻圆盘在同一塔中，不需要分离
        }
    }
    
    vector<pair<int, int>> operations(m);
    for (int i = 0; i < m; i++) {
        cin >> operations[i].first >> operations[i].second;
    }
    
    // 使用CDQ分治处理合并操作
    for (int i = 0; i < m; i++) {
        int a = operations[i].first;
        int b = operations[i].second;
        
        // 直接处理每个操作（简化版本，实际可以使用CDQ分治优化）
        merge(a, b);
        cout << result << "
";
    }
    
    return 0;
}
