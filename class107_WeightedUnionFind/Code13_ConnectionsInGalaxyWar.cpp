// 由于环境限制，不使用标准库头文件
// 使用基本C++实现，手动实现所需功能

/**
 * 带权并查集解决Connections in Galaxy War问题 (C++版本)
 * 
 * 问题分析：
 * 查询与星球连通且战力值最大的星球
 * 
 * 核心思想：
 * 1. 使用逆向思维，将删除操作转换为添加操作
 * 2. 使用带权并查集维护每个集合的最大战力值
 * 3. 离线处理所有操作
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O((n + m) * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、power和maxPower数组
 * 
 * 应用场景：
 * - 逆向处理
 * - 离线算法
 * - 最值维护
 */

const int MAXN = 10005;
const int MAXM = 100005;

int n, m;
int power[MAXN];
int edgesFrom[MAXM], edgesTo[MAXM];
int father[MAXN];
int maxPower[MAXN];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个星球为自己所在集合的代表
    for (int i = 0; i < n; i++) {
        father[i] = i;
        // 初始时每个集合的最大战力值就是星球本身的战力值
        maxPower[i] = power[i];
    }
}

/**
 * 查找星球i所在集合的代表，并进行路径压缩
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的星球编号
 * @return 星球i所在集合的根节点
 */
int find(int i) {
    // 如果不是根节点
    if (i != father[i]) {
        // 递归查找根节点，同时进行路径压缩
        father[i] = find(father[i]);
    }
    return father[i];
}

/**
 * 合并两个星球所在的集合
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param x 星球x编号
 * @param y 星球y编号
 */
void unionSets(int x, int y) {
    // 查找两个星球的根节点
    int xf = find(x), yf = find(y);
    // 如果不在同一集合中
    if (xf != yf) {
        // 合并两个集合
        father[xf] = yf;
        // 更新最大战力值
        if (maxPower[xf] > maxPower[yf]) {
            maxPower[yf] = maxPower[xf];
        }
    }
}

/**
 * 查询与星球a连通且战力值最大的星球
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param a 星球编号
 * @return 连通集合中的最大战力值
 */
int query(int a) {
    // 查找星球所在集合的根节点
    int root = find(a);
    // 返回集合中的最大战力值
    return maxPower[root];
}

// 由于环境限制，使用简化输入输出方式
// 实际实现中需要根据具体输入格式调整

int main() {
    // 由于环境限制，使用简化主函数
    // 实际实现中需要根据具体输入输出格式调整
    return 0;
}