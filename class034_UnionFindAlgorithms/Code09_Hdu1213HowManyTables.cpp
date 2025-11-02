#include <iostream>
#include <vector>
using namespace std;

/**
 * HDU 1213 How Many Tables
 * 
 * 题目描述：
 * Today is Ignatius' birthday. He invites a lot of friends. Now it's dinner time. Ignatius wants to know how many tables he needs at least. You have to notice that not all the friends know each other, and all the friends do not want to stay with strangers.
 * One important rule for this problem is that if I tell you A knows B, and B knows C, that means A, B, C know each other, so they can stay in one table.
 * For example: If I tell you A knows B, B knows C, and D knows E, so A, B, C can stay in one table, and D, E have to stay in the other one. So Ignatius needs 2 tables at least.
 * 
 * 输入格式：
 * The input starts with an integer T(1<=T<=25) which indicate the number of test cases. Then T test cases follow. Each test case starts with two integers N and M(1<=N,M<=1000). N indicates the number of friends, the friends are marked from 1 to N. Then M lines follow. Each line consists of two integers A and B(A!=B), that means friend A and friend B know each other.
 * 
 * 输出格式：
 * For each test case, just output how many tables Ignatius needs at least.
 * 
 * 样例输入：
 * 2
 * 5 3
 * 1 2
 * 2 3
 * 4 5
 * 3 3
 * 1 3
 * 2 3
 * 3 1
 * 
 * 样例输出：
 * 2
 * 1
 * 
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1213
 * 
 * 解题思路：
 * 使用并查集解决。将认识的朋友合并到同一个集合中，最后统计有多少个独立的集合，即为需要的桌子数量。
 * 
 * 时间复杂度：O(M*α(N))，其中α是阿克曼函数的反函数
 * 空间复杂度：O(N)
 * 是否为最优解：是
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入是否合法
 * 2. 可配置性：可以修改朋友关系的定义
 * 3. 线程安全：当前实现不是线程安全的
 * 
 * 与机器学习等领域的联系：
 * 1. 社交网络分析：识别社区结构
 * 2. 推荐系统：基于朋友关系的推荐
 * 
 * 语言特性差异:
 * Java: 对象引用和垃圾回收
 * C++: 指针操作和手动内存管理
 * Python: 动态类型和自动内存管理
 * 
 * 极端输入场景:
 * 1. 没有朋友关系
 * 2. 所有朋友相互认识
 * 3. 每个朋友都只认识自己
 * 
 * 性能优化:
 * 1. 路径压缩优化find操作
 * 2. 按秩合并优化union操作
 */

/**
 * 并查集类
 */
class UnionFind {
private:
    vector<int> parent;  // parent[i]表示节点i的父节点
    vector<int> rank;    // rank[i]表示以i为根的树的高度上界
    int components;      // 当前连通分量的数量

public:
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    UnionFind(int n) {
        parent.resize(n + 1);  // 朋友编号从1开始
        rank.resize(n + 1);
        components = n;
        
        // 初始时每个节点都是自己的父节点
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }
    
    /**
     * 查找节点的根节点（代表元素）
     * 使用路径压缩优化
     * @param x 要查找的节点
     * @return 节点x所在集合的根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将路径上的所有节点直接连接到根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 合并两个集合
     * 使用按秩合并优化
     * @param x 第一个节点
     * @param y 第二个节点
     */
    void unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        // 如果已经在同一个集合中，直接返回
        if (rootX == rootY) {
            return;
        }
        
        // 按秩合并：将秩小的树合并到秩大的树下
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        // 连通分量数量减1
        components--;
    }
    
    /**
     * 获取当前连通分量的数量
     * @return 连通分量数量
     */
    int getComponents() {
        return components;
    }
};

/**
 * 计算需要的桌子数量
 * @param n 朋友数量
 * @param relations 朋友关系
 * @return 需要的桌子数量
 */
int countTables(int n, const vector<pair<int, int>>& relations) {
    // 创建并查集
    UnionFind uf(n);
    
    // 处理每个朋友关系
    for (const auto& relation : relations) {
        uf.unionSets(relation.first, relation.second);
    }
    
    // 返回连通分量数量
    return uf.getComponents();
}

// 测试方法
int main() {
    int t;
    cin >> t;
    
    // 处理每个测试用例
    for (int i = 0; i < t; i++) {
        int n, m;
        cin >> n >> m;
        
        // 存储朋友关系
        vector<pair<int, int>> relations(m);
        
        // 读取朋友关系
        for (int j = 0; j < m; j++) {
            cin >> relations[j].first >> relations[j].second;
        }
        
        // 计算并输出结果
        cout << countTables(n, relations) << endl;
    }
    
    return 0;
}