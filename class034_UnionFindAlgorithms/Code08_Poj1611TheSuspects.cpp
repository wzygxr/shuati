#include <iostream>
#include <vector>
using namespace std;
using namespace std;

/**
 * POJ 1611 The Suspects
 * 
 * 题目描述：
 * Severe acute respiratory syndrome (SARS), an atypical pneumonia of unknown aetiology, was recognized as a global threat in mid-March 2003. To minimize transmission to others, the best strategy is to separate the suspects from others.
 * In the Not-Spreading-Your-Sickness University (NSYSU), there are many student groups. Students in the same group intercommunicate with each other frequently, and a student may join several groups. To prevent the possible transmissions of SARS, all the students who are members of the same group as a suspect (directly or indirectly) must be isolated.
 * However, the isolation should be minimized to avoid disturbing too many students. Your job is to find the minimum number of students that must be isolated.
 * 
 * 输入格式：
 * The input file contains several cases. Each case starts with two integers n and m (0 < n <= 30000, 0 <= m <= 500), where n is the number of students, and m is the number of groups. Students are numbered from 0 to n-1.
 * For each group, there is one line containing the number of members in the group and the members' IDs.
 * The case with n = 0 and m = 0 indicates the end of the input.
 * 
 * 输出格式：
 * For each case, output the minimum number of students that must be isolated.
 * 
 * 样例输入：
 * 100 4
 * 2 1 2
 * 5 10 13 11 12 14
 * 2 0 1
 * 2 99 2
 * 200 2
 * 1 5
 * 5 1 2 3 4 5
 * 1 0
 * 0 0
 * 
 * 样例输出：
 * 4
 * 1
 * 1
 * 
 * 题目链接：http://poj.org/problem?id=1611
 * 
 * 解题思路：
 * 使用并查集解决。将学生分组，每个组作为一个集合。学生0是初始嫌疑人，需要找出所有与学生0在同一个集合中的学生数量。
 * 
 * 时间复杂度：O(n*α(n))，其中α是阿克曼函数的反函数
 * 空间复杂度：O(n)
 * 是否为最优解：是
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入是否合法
 * 2. 可配置性：可以修改初始嫌疑人编号
 * 3. 线程安全：当前实现不是线程安全的
 * 
 * 与机器学习等领域的联系：
 * 1. 社交网络分析：识别潜在的影响者
 * 2. 传染病传播模型：模拟疾病传播路径
 * 
 * 语言特性差异:
 * Java: 对象引用和垃圾回收
 * C++: 指针操作和手动内存管理
 * Python: 动态类型和自动内存管理
 * 
 * 极端输入场景:
 * 1. 没有分组
 * 2. 所有学生在一个组中
 * 3. 每个学生单独成组
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
    vector<int> size;    // size[i]表示以i为根的集合的大小

public:
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    UnionFind(int n) {
        parent.resize(n);
        rank.resize(n);
        size.resize(n);
        
        // 初始时每个节点都是自己的父节点
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 1;
            size[i] = 1;  // 初始时每个集合大小为1
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
            size[rootX] += size[rootY];  // 更新集合大小
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];  // 更新集合大小
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
            size[rootX] += size[rootY];  // 更新集合大小
        }
    }
    
    /**
     * 获取包含指定节点的集合的大小
     * @param x 节点
     * @return 集合大小
     */
    int getSize(int x) {
        return size[find(x)];
    }
};

/**
 * 计算需要隔离的学生数量
 * @param n 学生数量
 * @param groups 分组信息
 * @return 需要隔离的学生数量
 */
int countSuspects(int n, const vector<vector<int>>& groups) {
    // 创建并查集
    UnionFind uf(n);
    
    // 处理每个分组
    for (const auto& group : groups) {
        if (!group.empty()) {
            // 将组内所有学生合并到一个集合中
            for (size_t i = 1; i < group.size(); i++) {
                uf.unionSets(group[0], group[i]);
            }
        }
    }
    
    // 返回包含学生0的集合的大小
    return uf.getSize(0);
}

// 测试方法
int main() {
    int n, m;
    
    while (true) {
        cin >> n >> m;
        
        // 输入结束条件
        if (n == 0 && m == 0) {
            break;
        }
        
        // 存储分组信息
        vector<vector<int>> groups(m);
        
        // 读取分组信息
        for (int i = 0; i < m; i++) {
            int k;
            cin >> k;
            groups[i].resize(k);
            for (int j = 0; j < k; j++) {
                cin >> groups[i][j];
            }
        }
        
        // 计算并输出结果
        cout << countSuspects(n, groups) << endl;
    }
    
    return 0;
}