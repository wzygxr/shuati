// 由于环境限制，不使用标准库头文件
// 使用基本C++实现，手动实现所需功能

/**
 * 带权并查集解决Rochambeau问题 (C++版本)
 * 
 * 问题分析：
 * 判断谁是裁判以及最早在第几轮可以确定
 * 
 * 核心思想：
 * 1. 枚举每个人作为裁判
 * 2. 使用带权并查集维护三人组的关系
 * 3. dist[i]表示玩家i与根节点的关系（0:相同, 1:胜, 2:负）
 * 4. 判断是否存在矛盾
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n * m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 逻辑推理
 * - 枚举验证
 * - 关系维护
 */

const int MAXN = 505;
const int MAXM = 2005;

int n, m;
int a[MAXM], b[MAXM], c[MAXM]; // 存储游戏结果
int father[MAXN];
int dist[MAXN];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个玩家为自己所在集合的代表
    for (int i = 0; i < n; i++) {
        father[i] = i;
        // 初始时每个玩家与根节点相同
        dist[i] = 0;
    }
}

/**
 * 查找玩家i所在集合的代表，并进行路径压缩
 * 同时更新dist[i]为玩家i与根节点的关系
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的玩家编号
 * @return 玩家i所在集合的根节点
 */
int find(int i) {
    // 如果不是根节点
    if (i != father[i]) {
        // 保存父节点
        int tmp = father[i];
        // 递归查找根节点，同时进行路径压缩
        father[i] = find(tmp);
        // 更新关系：当前玩家与根节点的关系 = 当前玩家与父节点的关系 + 父节点与根节点的关系
        // 使用模3运算处理关系
        dist[i] = (dist[i] + dist[tmp]) % 3;
    }
    return father[i];
}

/**
 * 合并两个玩家所在的集合，建立关系
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param x 玩家x编号
 * @param y 玩家y编号
 * @param r 关系：0表示平局，1表示x胜，2表示y胜
 * @return 如果合并成功返回true，如果发现矛盾返回false
 */
bool unionSets(int x, int y, int r) {
    // 查找两个玩家的根节点
    int xf = find(x), yf = find(y);
    // 如果在同一集合中
    if (xf == yf) {
        // 检查是否与已有关系矛盾
        // x和y的关系应该等于r
        // x与根节点的关系 - y与根节点的关系 = x与y的关系
        int relation = (dist[x] - dist[y] + 3) % 3;
        if (relation != r) {
            // 发现矛盾
            return false;
        }
    } else {
        // 合并两个集合
        father[xf] = yf;
        // 更新关系：
        // x与y的关系 = r
        // x与根节点xf的关系 = dist[x], y与根节点yf的关系 = dist[y]
        // 根节点xf与根节点yf的关系 = (dist[y] - dist[x] + r + 3) % 3
        dist[xf] = (dist[y] - dist[x] + r + 3) % 3;
    }
    return true;
}

/**
 * 检查假设player是裁判的情况下是否存在矛盾
 * 
 * @param player 假设的裁判编号
 * @param limit  检查前limit轮游戏
 * @return 如果存在矛盾返回false，否则返回true
 */
bool check(int player, int limit) {
    // 初始化并查集
    prepare();
    
    // 检查前limit轮游戏
    for (int i = 0; i < limit; i++) {
        // 如果涉及裁判则跳过
        if (a[i] == player || b[i] == player) {
            continue;
        }
        
        // 尝试合并
        if (!unionSets(a[i], b[i], c[i])) {
            // 发现矛盾
            return false;
        }
    }
    return true;
}

// 由于环境限制，使用简化输入输出方式
// 实际实现中需要根据具体输入格式调整

int main() {
    // 由于环境限制，使用简化主函数
    // 实际实现中需要根据具体输入输出格式调整
    return 0;
}