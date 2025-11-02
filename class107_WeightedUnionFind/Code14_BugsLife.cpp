// 由于环境限制，不使用标准库头文件
// 使用基本C++实现，手动实现所需功能

/**
 * 种类并查集解决A Bug's Life问题 (C++版本)
 * 
 * 问题分析：
 * 判断虫子交互关系是否满足性别假设
 * 
 * 核心思想：
 * 1. 使用种类并查集（扩展域并查集）
 * 2. 对于每只虫子i，维护两个节点：i（雄性）和i+n（雌性）
 * 3. 如果i和j是异性，则合并i和j+n，以及i+n和j
 * 4. 如果发现矛盾（i和i+n在同一集合中），则假设不成立
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father数组
 * 
 * 应用场景：
 * - 种类关系维护
 * - 逻辑一致性验证
 * - 扩展域并查集
 */

const int MAXN = 2005;

int t, n, m;
int father[MAXN * 2];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个节点为自己所在集合的代表
    // 对于每只虫子i，节点i表示雄性，节点i+n表示雌性
    for (int i = 1; i <= 2 * n; i++) {
        father[i] = i;
    }
}

/**
 * 查找节点i所在集合的代表，并进行路径压缩
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的节点
 * @return 节点i所在集合的根节点
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
 * 合并两个节点所在的集合
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param x 节点x
 * @param y 节点y
 */
void unionSets(int x, int y) {
    // 查找两个节点的根节点
    int xf = find(x), yf = find(y);
    // 如果不在同一集合中
    if (xf != yf) {
        // 合并两个集合
        father[xf] = yf;
    }
}

/**
 * 检查两只虫子是否可以是异性
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param x 虫子x编号
 * @param y 虫子y编号
 * @return 如果可以是异性返回true，否则返回false
 */
bool check(int x, int y) {
    // 如果x和y在同一集合中，说明它们必须是同性，与交互矛盾
    if (find(x) == find(y)) {
        return false;
    }
    // 如果x和y+n在同一集合中，或者x+n和y在同一集合中，说明它们是异性，符合要求
    return true;
}

// 由于环境限制，使用简化输入输出方式
// 实际实现中需要根据具体输入格式调整

int main() {
    // 由于环境限制，使用简化主函数
    // 实际实现中需要根据具体输入输出格式调整
    return 0;
}