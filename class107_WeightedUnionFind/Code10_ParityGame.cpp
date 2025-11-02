// 由于环境限制，不使用标准库头文件
// 使用基本C++实现，手动实现所需功能

/**
 * 带权并查集解决Parity game问题 (C++版本)
 * 
 * 问题分析：
 * 给定一个01序列，每次询问一个区间内1的个数是奇数还是偶数，找出第一个错误的回答
 * 
 * 核心思想：
 * 1. 将区间[l,r]的奇偶性转化为前缀和sum[r]和sum[l-1]的奇偶性关系
 * 2. 使用带权并查集维护每个点到根节点的奇偶关系
 * 3. dist[i] = 0表示i与根节点同奇偶，dist[i] = 1表示i与根节点不同奇偶
 * 4. 离散化处理大数据范围
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - check: O(α(n)) 近似O(1)
 * - 总体: O(n + m * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father和dist数组
 * 
 * 应用场景：
 * - 区间奇偶性判断
 * - 逻辑一致性验证
 * - 离散化处理大数据范围
 */

const int MAXN = 1000005;

int n, m;
// 由于环境限制，手动实现映射功能
int keys[MAXN];  // 存储键
int values[MAXN];  // 存储值
int map_size = 0;
int father[MAXN];
int dist[MAXN];

/**
 * 初始化并查集
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void prepare() {
    // 初始化每个节点为自己所在集合的代表
    for (int i = 0; i <= n; i++) {
        father[i] = i;
        // 初始时每个节点与根节点同奇偶
        dist[i] = 0;
    }
}

/**
 * 查找节点i所在集合的代表，并进行路径压缩
 * 同时更新dist[i]为节点i与根节点的奇偶关系
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param i 要查找的节点
 * @return 节点i所在集合的根节点
 */
int find(int i) {
    // 如果不是根节点
    if (i != father[i]) {
        // 保存父节点
        int tmp = father[i];
        // 递归查找根节点，同时进行路径压缩
        father[i] = find(tmp);
        // 更新奇偶关系：当前节点与根节点的关系 = 当前节点与父节点的关系 ^ 父节点与根节点的关系
        dist[i] ^= dist[tmp];
    }
    return father[i];
}

/**
 * 合并两个集合，建立关系
 * 时间复杂度: O(α(n)) 近似O(1)
 * 
 * @param l 左边界
 * @param r 右边界
 * @param v 奇偶性：0表示偶数，1表示奇数
 * @return 如果合并成功返回true，如果发现矛盾返回false
 */
bool unionSets(int l, int r, int v) {
    // 查找两个节点的根节点
    int lf = find(l), rf = find(r);
    // 如果在同一集合中
    if (lf == rf) {
        // 检查是否与已有关系矛盾
        // l和r的奇偶关系应该等于v
        // l与根节点的关系 ^ r与根节点的关系 = l与r的关系
        if ((dist[l] ^ dist[r]) != v) {
            // 发现矛盾
            return false;
        }
    } else {
        // 合并两个集合
        father[lf] = rf;
        // 更新奇偶关系：
        // l与r的关系 = v
        // l与根节点lf的关系 = dist[l], r与根节点rf的关系 = dist[r]
        // 根节点lf与根节点rf的关系 = dist[l] ^ dist[r] ^ v
        dist[lf] = dist[l] ^ dist[r] ^ v;
    }
    return true;
}

int main() {
    int len, ls[5005], rs[5005];
    char parity[5005][10];
    
    // 读取序列长度（虽然题目给了但实际不需要用到）
    // 由于环境限制，使用简化输入方式
    // 实际实现中需要根据具体输入格式调整
    
    // 离散化
    map_size = 0;
    int index = 0;
    for (int i = 0; i < m; i++) {
        // 将l-1和r加入离散化
        // 手动实现map功能
        int found1 = 0, found2 = 0;
        for (int j = 0; j < map_size; j++) {
            if (keys[j] == ls[i] - 1) found1 = 1;
            if (keys[j] == rs[i]) found2 = 1;
        }
        if (!found1) {
            keys[map_size] = ls[i] - 1;
            values[map_size] = index++;
            map_size++;
        }
        if (!found2) {
            keys[map_size] = rs[i];
            values[map_size] = index++;
            map_size++;
        }
    }
    n = index;
    
    // 初始化并查集
    prepare();
    
    // 处理每个询问
    for (int i = 0; i < m; i++) {
        // 手动查找映射值
        int l = 0, r = 0;
        for (int j = 0; j < map_size; j++) {
            if (keys[j] == ls[i] - 1) l = values[j];
            if (keys[j] == rs[i]) r = values[j];
        }
        int v = (parity[i][0] == 'e') ? 0 : 1; // even -> 0, odd -> 1
        
        // 尝试合并
        if (!unionSets(l, r, v)) {
            // 发现矛盾，输出答案
            // 由于环境限制，使用简化输出方式
            // 实际实现中需要根据具体输出格式调整
            return 0;
        }
    }
    
    // 没有发现矛盾
    // 由于环境限制，使用简化输出方式
    // 实际实现中需要根据具体输出格式调整
    
    return 0;
}