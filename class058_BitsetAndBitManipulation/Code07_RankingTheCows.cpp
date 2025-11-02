#include <iostream>
#include <vector>
#include <bitset>

// POJ 3275 Ranking the Cows
// 题目链接: http://poj.org/problem?id=3275
// 题目大意:
// FJ想按照奶牛产奶的能力给她们排序。现在已知有N头奶牛（1 ≤ N ≤ 1,000）。
// FJ通过比较，已经知道了M（1 ≤ M ≤ 10,000）对相对关系。
// 问你最少还要确定多少对牛的关系才能将所有的牛按照一定顺序排序起来。

// 解题思路:
// 1. 这是一个传递闭包问题
// 2. 使用Floyd算法求传递闭包
// 3. 用bitset优化Floyd算法，将时间复杂度从O(N^3)优化到O(N^3/32)
// 4. 统计已知关系数，用完全图的关系数减去已知关系数就是答案
// 时间复杂度: O(N^3/32)
// 空间复杂度: O(N^2/32)

using namespace std;

// 最大奶牛数
const int MAXN = 1005;

// 使用bitset优化的邻接矩阵
// graph[i]表示第i头牛能直接或间接到达的所有牛
// 例如：graph[i][j]为1表示第i头牛能到达第j头牛
bitset<MAXN> graph[MAXN];

// 主函数，处理输入并输出结果
int main() {
    // 优化输入输出速度，关闭stdio同步，解除cin与cout的绑定
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    // 读取N和M
    // n表示奶牛的数量，m表示已知的关系对数
    cin >> n >> m;
    
    // 初始化邻接矩阵
    // reset()将所有位初始化为0
    for (int i = 1; i <= n; i++) {
        graph[i].reset();
    }
    
    // 读取已知的M对关系
    // 每一对关系表示a > b，即a到b有一条有向边
    for (int i = 0; i < m; i++) {
        int a, b;
        // 读取一对关系a > b
        cin >> a >> b;
        // a > b，即a到b有一条有向边
        // set(b)将第b位设置为1，表示a能到达b
        graph[a].set(b);
    }
    
    // Floyd求传递闭包，使用bitset优化
    // 通过Floyd算法计算所有奶牛之间的可达关系
    // 枚举中间节点k
    for (int k = 1; k <= n; k++) {
        // 枚举起点i
        for (int i = 1; i <= n; i++) {
            // 如果i到k有路径，则i到k能到达的所有点，i也能到达
            // graph[i][k] 检查第k位是否为1，即i是否能到达k
            if (graph[i][k]) {
                // graph[i] |= graph[k] 将graph[i]与graph[k]按位或
                // 这表示i能到达k能到达的所有点
                graph[i] |= graph[k];
            }
        }
    }
    
    // 统计已知关系数
    // 计算所有已知的奶牛之间的关系对数
    int known = 0;
    // 遍历每头牛
    for (int i = 1; i <= n; i++) {
        // count()返回bitset中1的个数
        // 即第i头牛能到达的牛的数量
        known += graph[i].count();
    }
    
    // 完全图的关系数是n*(n-1)/2
    // 答案是还需要确定的关系数
    // 完全图有n*(n-1)/2对关系，减去已知的关系数就是还需要确定的关系数
    int result = n * (n - 1) / 2 - known;
    // 输出结果
    cout << result << "\n";
    
    return 0;
}