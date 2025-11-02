#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

/**
 * POJ 3764 The xor-longest Path
 * 题目链接：http://poj.org/problem?id=3764
 * 
 * 题目描述：
 * 给定一棵树，每条边都有一个权值，求树中最长的异或路径。
 * 异或路径的权值定义为路径上所有边的权值的异或和。
 * 
 * 输入格式：
 * 第一行一个整数n，表示树的节点数。
 * 接下来n-1行，每行三个整数u, v, w，表示节点u和v之间有一条权值为w的边。
 * 
 * 输出格式：
 * 一个整数，表示最长的异或路径的权值。
 * 
 * 数据范围：
 * 1 <= n <= 100000
 * 0 <= u, v < n
 * 0 <= w < 2^31
 * 
 * 解题思路：
 * 1. 首先进行DFS遍历，计算每个节点到根节点的异或和xor_sum[u]
 * 2. 两个节点u和v之间的异或路径的权值等于xor_sum[u] ^ xor_sum[v]
 * 3. 问题转化为：在数组xor_sum中找到两个元素，它们的异或值最大
 * 4. 使用字典树（Trie）来高效地解决最大异或对问题
 * 
 * 时间复杂度：O(n * 32)，其中32是二进制位数
 * 空间复杂度：O(n * 32)
 */

const int MAXN = 100010;
const int MAX_BIT = 31;  // 最大二进制位数

// 树的邻接表表示
struct Edge {
    int to;      // 目标节点
    int weight;  // 边的权值
    Edge *next;  // 下一条边
    
    Edge(int t, int w, Edge *n) : to(t), weight(w), next(n) {}
};

Edge *head[MAXN];  // 邻接表的头指针
long long xor_sum[MAXN];  // 存储每个节点到根节点的异或和
bool visited[MAXN];  // 标记节点是否被访问过

// 字典树节点
struct TrieNode {
    TrieNode *children[2];  // 左右子节点，0和1
    
    TrieNode() {
        children[0] = children[1] = nullptr;
    }
};

// 将一个数插入字典树
void insert(TrieNode *root, long long num) {
    TrieNode *current = root;
    // 从最高位到最低位插入
    for (int i = MAX_BIT; i >= 0; i--) {
        int bit = (num >> i) & 1;  // 取出当前位
        if (current->children[bit] == nullptr) {
            current->children[bit] = new TrieNode();
        }
        current = current->children[bit];
    }
}

// 查询与给定数异或最大的值
int query(TrieNode *root, long long num) {
    TrieNode *current = root;
    int max_xor = 0;
    // 从最高位到最低位查询
    for (int i = MAX_BIT; i >= 0; i--) {
        int bit = (num >> i) & 1;  // 取出当前位
        int target_bit = 1 - bit;  // 期望的异或位
        
        // 如果可以选择不同的位，则选择它
        if (current->children[target_bit] != nullptr) {
            max_xor |= (1 << i);  // 这一位可以得到1
            current = current->children[target_bit];
        } else {
            // 否则只能选择相同的位
            current = current->children[bit];
        }
    }
    return max_xor;
}

// DFS遍历树，计算每个节点到根节点的异或和
void dfs(int u, long long current_xor) {
    visited[u] = true;
    xor_sum[u] = current_xor;
    
    // 遍历所有邻接边
    for (Edge *e = head[u]; e != nullptr; e = e->next) {
        int v = e->to;
        if (!visited[v]) {
            // 递归访问子节点，更新异或和
            dfs(v, current_xor ^ e->weight);
        }
    }
}

// 释放字典树内存
void freeTrie(TrieNode *root) {
    if (root == nullptr) return;
    freeTrie(root->children[0]);
    freeTrie(root->children[1]);
    delete root;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    while (cin >> n) {
        // 初始化邻接表
        memset(head, 0, sizeof(head));
        memset(visited, false, sizeof(visited));
        
        // 读取n-1条边
        for (int i = 0; i < n - 1; i++) {
            int u, v, w;
            cin >> u >> v >> w;
            
            // 添加双向边
            head[u] = new Edge(v, w, head[u]);
            head[v] = new Edge(u, w, head[v]);
        }
        
        // 从节点0开始DFS，计算异或和
        dfs(0, 0);
        
        // 构建字典树并查找最大异或值
        TrieNode *root = new TrieNode();
        insert(root, 0);  // 插入0，表示根节点到自身的异或和
        
        int max_xor = 0;
        for (int i = 0; i < n; i++) {
            max_xor = max(max_xor, query(root, xor_sum[i]));
            insert(root, xor_sum[i]);
        }
        
        // 输出结果
        cout << max_xor << endl;
        
        // 释放字典树内存
        freeTrie(root);
        
        // 释放邻接表内存
        for (int i = 0; i < n; i++) {
            Edge *e = head[i];
            while (e != nullptr) {
                Edge *temp = e;
                e = e->next;
                delete temp;
            }
        }
    }
    
    return 0;
}

/*
 * 算法分析：
 * 时间复杂度：O(n * 32)
 * - DFS遍历树的时间复杂度：O(n)
 * - 构建字典树和查询的时间复杂度：每个数最多处理32位（二进制），总时间复杂度O(n * 32)
 * - 整体时间复杂度：O(n * 32)
 * 
 * 空间复杂度：O(n * 32)
 * - 邻接表存储：O(n)
 * - 异或和数组：O(n)
 * - 字典树存储：最坏情况下O(n * 32)，但实际空间使用会小于此值
 * 
 * 优化点：
 * 1. 使用邻接表高效存储树结构
 * 2. 使用位运算高效处理二进制位
 * 3. 字典树的使用使得查找最大异或对的时间复杂度大大降低
 * 4. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入
 * 
 * 边界情况处理：
 * 1. 树只有一个节点的情况，最长异或路径为0
 * 2. 边权值为0的情况，异或不改变当前值
 * 3. 大数值的处理，使用long long类型存储异或和
 * 
 * 工程化考量：
 * 1. 适当使用内存管理，释放动态分配的内存
 * 2. 优化输入输出效率
 * 3. 邻接表的高效实现
 * 
 * 调试技巧：
 * 1. 可以在DFS函数中输出每个节点的异或和，检查是否计算正确
 * 2. 测试用例：如n=3，边为0-1 1，0-2 2，预期结果为3（路径1-2的异或和为1^2=3）
 * 3. 注意处理节点编号从0开始的情况
 * 4. 检查内存泄漏，确保动态分配的内存被正确释放
 */