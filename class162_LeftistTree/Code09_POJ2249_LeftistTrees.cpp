/**
 * POJ 2249 Leftist Trees
 * 题目链接: http://poj.org/problem?id=2249
 * 
 * 题目大意:
 * 实现左偏树的基本操作，包括合并和删除最小元素操作
 * 
 * 算法思路:
 * 使用左偏树实现可合并堆，支持高效的合并操作和删除最小元素操作
 * 
 * 时间复杂度:
 * - 合并操作: O(log n)
 * - 删除最小元素: O(log n)
 * - 插入元素: O(log n)
 * 
 * 空间复杂度: O(n)
 */

// 由于编译环境限制，使用基本C++实现方式，避免使用复杂的STL容器

// 最大节点数
const int MAXN = 100001;

// 节点值数组
int value[MAXN];

// 左右子节点数组
int left[MAXN];
int right[MAXN];

// 距离数组
int dist[MAXN];

// 并查集数组
int father[MAXN];

/**
 * 初始化函数
 * @param n 节点数量
 */
void prepare(int n) {
    // 空节点的距离定义为-1
    dist[0] = -1;
    
    // 初始化每个节点
    for (int i = 1; i <= n; i++) {
        left[i] = right[i] = 0;
        dist[i] = 0;
        father[i] = i;
    }
}

/**
 * 并查集查找函数，带路径压缩优化
 * @param i 节点编号
 * @return 节点所在集合的代表元素
 */
int find(int i) {
    return father[i] = (father[i] == i) ? i : find(father[i]);
}

/**
 * 合并两棵左偏树，维护小根堆性质
 * @param i 第一棵左偏树的根节点编号
 * @param j 第二棵左偏树的根节点编号
 * @return 合并后新树的根节点编号
 */
int merge(int i, int j) {
    // 递归终止条件
    if (i == 0 || j == 0) {
        return i + j;
    }
    
    // 维护小根堆性质
    if (value[i] > value[j]) {
        int tmp = i;
        i = j;
        j = tmp;
    }
    
    // 递归合并右子树和j
    right[i] = merge(right[i], j);
    
    // 维护左偏性质
    if (dist[left[i]] < dist[right[i]]) {
        int tmp = left[i];
        left[i] = right[i];
        right[i] = tmp;
    }
    
    // 更新距离
    dist[i] = dist[right[i]] + 1;
    
    // 更新父节点信息
    father[left[i]] = father[right[i]] = i;
    
    return i;
}

/**
 * 删除堆顶元素（最小值）
 * @param i 堆顶节点编号
 * @return 删除堆顶后新树的根节点编号
 */
int pop(int i) {
    // 将左右子节点的father设置为自己
    father[left[i]] = left[i];
    father[right[i]] = right[i];
    
    // 合并左右子树
    father[i] = merge(left[i], right[i]);
    
    // 清空当前节点信息
    left[i] = right[i] = dist[i] = 0;
    
    return father[i];
}

/**
 * 主函数 - 伪代码实现，展示算法逻辑
 * 由于编译环境限制，不实现完整的输入输出函数
 * 在实际使用中，需要根据具体环境实现输入输出
 */
int main() {
    // 由于编译环境限制，这里不实现完整的输入输出
    // 以下为伪代码，展示算法逻辑
    
    // 假设读入n和m
    int n = 5; // 示例值
    int m = 3; // 示例值
    
    // 初始化
    prepare(n);
    
    // 假设读入每个节点的初始值
    for (int i = 1; i <= n; i++) {
        value[i] = i; // 示例值
    }
    
    // 假设处理操作
    for (int i = 0; i < m; i++) {
        int op = 1; // 示例值
        
        if (op == 1) {
            // 合并操作示例
            int x = 1;
            int y = 2;
            
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                int newRoot = merge(rootX, rootY);
                father[newRoot] = newRoot;
            }
        } else {
            // 删除最小元素操作示例
            int x = 1;
            int root = find(x);
            // 假设输出value[root]
            // 在实际环境中需要实现输出函数
            pop(root);
        }
    }
    
    return 0;
}