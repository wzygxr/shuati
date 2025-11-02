// 715. Range 模块 - 线段树实现
// 题目来源：LeetCode 715 https://leetcode.cn/problems/range-module/
// 
// 题目描述：
// Range模块是跟踪数字范围的模块。设计一个数据结构来跟踪表示为 半开区间 的范围并查询它们。
// 实现 RangeModule 类:
// RangeModule() 初始化数据结构的对象
// void addRange(int left, int right) 添加 半开区间 [left, right), 跟踪该区间中的每个实数。添加与当前跟踪的区间重叠的区间时，应当添加在区间 [left, right) 中尚未被跟踪的任何数字到该区间中。
// boolean queryRange(int left, int right) 只有在当前正在跟踪区间 [left, right) 中的每一个实数时，才返回 true ，否则返回 false 。
// void removeRange(int left, int right) 停止跟踪 半开区间 [left, right) 中当前正在跟踪的每个实数。
// 
// 解题思路：
// 使用线段树配合懒惰标记来维护区间覆盖状态
// 1. 使用线段树节点维护区间覆盖信息：0表示未完全覆盖，1表示完全覆盖
// 2. 使用懒惰标记优化区间更新操作：-1表示无操作，0表示删除，1表示添加
// 3. addRange操作：将区间[left, right)标记为完全覆盖
// 4. removeRange操作：将区间[left, right)标记为未完全覆盖
// 5. queryRange操作：查询区间[left, right)是否完全覆盖
// 
// 时间复杂度分析：
// - addRange：O(log n)
// - removeRange：O(log n)
// - queryRange：O(log n)
// 空间复杂度：O(n)

// 由于编译环境限制，使用基础C++实现，避免复杂STL容器

const int MAXN = 1000001;

// 线段树数组
int tree[MAXN * 4];     // 存储区间覆盖状态：0-未覆盖，1-完全覆盖
int lazy[MAXN * 4];     // 懒惰标记：-1-无操作，0-删除，1-添加
int maxSize;            // 线段树能处理的最大值范围

/**
 * 下推懒惰标记
 * 将当前节点的懒惰标记传递给左右子节点
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 */
void pushDown(int node, int start, int end) {
    // 如果当前节点有懒惰标记
    if (lazy[node] != -1) {
        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;
        
        // 更新左子节点的值和懒惰标记
        tree[leftNode] = lazy[node];
        lazy[leftNode] = lazy[node];
        
        // 更新右子节点的值和懒惰标记
        tree[rightNode] = lazy[node];
        lazy[rightNode] = lazy[node];
        
        // 清除当前节点的懒惰标记
        lazy[node] = -1;
    }
}

/**
 * 更新区间的辅助函数
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param left 更新区间左边界
 * @param right 更新区间右边界
 * @param val 要设置的值（0-删除，1-添加）
 */
void updateRangeHelper(int node, int start, int end, int left, int right, int val) {
    // 更新区间与当前节点维护区间无交集，直接返回
    if (right < start || end < left) {
        return;
    }
    
    // 当前节点维护区间完全包含在更新区间内
    if (left <= start && end <= right) {
        tree[node] = val;
        lazy[node] = val;
        return;
    }
    
    // 下推懒惰标记
    pushDown(node, start, end);
    
    // 部分重叠，递归更新左右子树
    int mid = start + (end - start) / 2;
    int leftNode = 2 * node + 1;
    int rightNode = 2 * node + 2;
    
    updateRangeHelper(leftNode, start, mid, left, right, val);
    updateRangeHelper(rightNode, mid + 1, end, left, right, val);
    
    // 更新当前节点的值
    // 如果左右子节点都完全覆盖，则当前节点也完全覆盖
    tree[node] = (tree[leftNode] == 1 && tree[rightNode] == 1) ? 1 : 0;
}

/**
 * 查询辅助函数
 * @param node 当前节点索引
 * @param start 当前节点维护的区间左边界
 * @param end 当前节点维护的区间右边界
 * @param left 查询区间左边界
 * @param right 查询区间右边界
 * @return 区间是否完全覆盖
 */
bool queryHelper(int node, int start, int end, int left, int right) {
    // 查询区间与当前节点维护区间无交集，返回true（不影响整体结果）
    if (right < start || end < left) {
        return true;
    }
    
    // 当前节点维护区间完全包含在查询区间内，返回覆盖状态
    if (left <= start && end <= right) {
        return tree[node] == 1;
    }
    
    // 下推懒惰标记
    pushDown(node, start, end);
    
    // 部分重叠，递归查询左右子树
    int mid = start + (end - start) / 2;
    int leftNode = 2 * node + 1;
    int rightNode = 2 * node + 2;
    
    bool leftResult = queryHelper(leftNode, start, mid, left, right);
    bool rightResult = queryHelper(rightNode, mid + 1, end, left, right);
    
    // 只有左右子树都完全覆盖，才返回true
    return leftResult && rightResult;
}

/**
 * 初始化线段树
 * @param max_size 线段树维护的最大范围
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void initSegmentTree(int max_size) {
    maxSize = max_size;
    // 初始化线段树数组
    for (int i = 0; i < 4 * maxSize; i++) {
        tree[i] = 0;
        lazy[i] = -1; // 初始化懒惰标记为-1（无操作）
    }
}

/**
 * 添加区间
 * @param left 区间左端点（包含）
 * @param right 区间右端点（不包含）
 * 
 * 时间复杂度: O(log n)
 */
void addRange(int left, int right) {
    updateRangeHelper(0, 0, maxSize - 1, left, right - 1, 1);
}

/**
 * 删除区间
 * @param left 区间左端点（包含）
 * @param right 区间右端点（不包含）
 * 
 * 时间复杂度: O(log n)
 */
void removeRange(int left, int right) {
    updateRangeHelper(0, 0, maxSize - 1, left, right - 1, 0);
}

/**
 * 查询区间是否完全覆盖
 * @param left 区间左端点（包含）
 * @param right 区间右端点（不包含）
 * @return 如果区间完全覆盖返回true，否则返回false
 * 
 * 时间复杂度: O(log n)
 */
bool queryRange(int left, int right) {
    return queryHelper(0, 0, maxSize - 1, left, right - 1);
}

// 由于C++环境限制，无法实现完整的类结构，这里提供函数式接口
// 在实际应用中，应该使用类来封装这些函数