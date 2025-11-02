// 由于环境限制，不使用标准库头文件
// 使用基本C++实现，手动实现所需功能

/**
 * 带权并查集解决Dragon Balls问题 (C++版本)
 * 
 * 问题分析：
 * 维护龙珠的转移次数和城市龙珠数量
 * 
 * 核心思想：
 * 1. 使用带权并查集维护每个龙珠被转移的次数
 * 2. 维护每个城市的龙珠数量
 * 3. 在合并操作中正确更新转移次数和数量
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、dist和count数组
 * 
 * 应用场景：
 * - 资源转移追踪
 * - 数量统计维护
 * - 操作次数记录
 */

const int MAXN = 10005;

int n, m;
int father[MAXN];
int dist[MAXN];
int count[MAXN];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个龙珠在对应城市
    for (int i = 1; i <= n; i++) {
        father[i] = i;
        // 初始时每个龙珠被转移0次
        dist[i] = 0;
        // 初始时每个城市有1个龙珠
        count[i] = 1;
    }
}

/**
 * 查找龙珠i所在城市的代表，并进行路径压缩
 * 同时更新dist[i]为龙珠i被转移的次数
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的龙珠编号
 * @return 龙珠i所在城市的代表
 */
int find(int i) {
    // 如果不是根节点
    if (i != father[i]) {
        // 保存父节点
        int tmp = father[i];
        // 递归查找根节点，同时进行路径压缩
        father[i] = find(tmp);
        // 更新转移次数：当前龙珠的转移次数 += 父节点的转移次数
        dist[i] += dist[tmp];
    }
    return father[i];
}

/**
 * 合并两个城市，将A所在城市的所有龙珠转移到B所在城市
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param a 龙珠A编号
 * @param b 龙珠B编号
 */
void unionSets(int a, int b) {
    // 查找两个龙珠所在城市的代表
    int af = find(a), bf = find(b);
    // 如果不在同一城市
    if (af != bf) {
        // 将A所在城市的所有龙珠转移到B所在城市
        father[af] = bf;
        // A所在城市的所有龙珠转移次数加1
        dist[af]++;
        // 更新B所在城市的龙珠数量
        count[bf] += count[af];
        // A所在城市的龙珠数量清零
        count[af] = 0;
    }
}

/**
 * 查询龙珠信息
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param a 龙珠编号
 * @return 龙珠所在城市编号
 */
int query(int a) {
    // 查找龙珠所在城市的代表
    find(a); // 调用find确保路径压缩完成
    return father[a];
}

// 由于环境限制，使用简化输入输出方式
// 实际实现中需要根据具体输入格式调整

int main() {
    // 由于环境限制，使用简化主函数
    // 实际实现中需要根据具体输入输出格式调整
    return 0;
}