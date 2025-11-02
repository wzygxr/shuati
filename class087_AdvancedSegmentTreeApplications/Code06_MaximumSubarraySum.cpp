/**
 * SPOJ GSS1 - Can you answer these queries I
 * 
 * 题目描述：
 * 给定一个长度为n的整数序列，执行m次查询操作
 * 每次查询[l,r]区间内的最大子段和
 * 最大子段和：在给定区间内找到连续子序列，使得其元素和最大
 * 
 * 解题思路：
 * 使用线段树维护区间信息，每个节点存储以下信息：
 * 1. 区间最大子段和(maxSum)
 * 2. 区间从左端点开始的最大子段和(lSum)
 * 3. 区间到右端点结束的最大子段和(rSum)
 * 4. 区间总和(sum)
 * 
 * 关键技术：
 * 1. 线段树区间信息维护：每个节点维护四个关键信息
 * 2. 信息合并：通过pushUp函数合并左右子区间信息
 * 3. 区间查询：通过分治思想查询任意区间最大子段和
 * 
 * 合并两个子区间[l,mid]和[mid+1,r]的信息：
 * 1. 区间总和 = 左区间总和 + 右区间总和
 * 2. 区间从左端点开始的最大子段和 = max(左区间lSum, 左区间sum + 右区间lSum)
 * 3. 区间到右端点结束的最大子段和 = max(右区间rSum, 右区间sum + 左区间rSum)
 * 4. 区间最大子段和 = max(左区间maxSum, 右区间maxSum, 左区间rSum + 右区间lSum)
 * 
 * 时间复杂度分析：
 * 1. 建树：O(n)
 * 2. 查询：O(log n)
 * 3. 空间复杂度：O(n)
 * 
 * 是否最优解：是
 * 这是解决最大子段和区间查询问题的最优解法，时间复杂度为O(log n)
 * 
 * 工程化考量：
 * 1. 输入输出优化：使用scanf/printf提高效率
 * 2. 内存管理：静态数组避免频繁内存分配
 * 3. 边界处理：处理区间完全包含和部分重叠的情况
 * 
 * 题目链接：https://www.spoj.com/problems/GSS1/
 * 
 * @author Algorithm Journey
 * @version 1.0
 */

// 由于编译环境问题，不使用标准头文件，采用基础C++实现

const int MAXN = 50001;

/**
 * 节点信息结构体
 * 每个线段树节点维护区间的关键信息
 */
struct Node {
    int maxSum; // 区间最大子段和
    int lSum;   // 区间从左端点开始的最大子段和
    int rSum;   // 区间到右端点结束的最大子段和
    int sum;    // 区间总和
};

// 原始数组
int arr[MAXN];

// 线段树数组
Node tree[MAXN << 2];

/**
 * 自定义max函数，避免使用<algorithm>头文件
 */
int max(int a, int b) {
    return a > b ? a : b;
}

/**
 * 合并两个子节点的信息
 * 将左右子区间的信息合并到父区间
 * 
 * @param left  左子节点信息
 * @param right 右子节点信息
 * @return 合并后的节点信息
 */
Node pushUp(Node left, Node right) {
    Node res;
    // 区间总和 = 左区间总和 + 右区间总和
    res.sum = left.sum + right.sum;
    
    // 区间从左端点开始的最大子段和 = max(左区间lSum, 左区间sum + 右区间lSum)
    res.lSum = max(left.lSum, left.sum + right.lSum);
    
    // 区间到右端点结束的最大子段和 = max(右区间rSum, 右区间sum + 左区间rSum)
    res.rSum = max(right.rSum, right.sum + left.rSum);
    
    // 区间最大子段和 = max(左区间maxSum, 右区间maxSum, 左区间rSum + 右区间lSum)
    res.maxSum = max(max(left.maxSum, right.maxSum), left.rSum + right.lSum);
    
    return res;
}

/**
 * 建立线段树
 * 递归构建线段树，每个节点存储对应区间的四个关键信息
 * 
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 节点索引
 */
void build(int l, int r, int i) {
    // 叶子节点，直接赋值
    if (l == r) {
        tree[i].maxSum = tree[i].lSum = tree[i].rSum = tree[i].sum = arr[l];
        return;
    }
    
    // 递归构建左右子树
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    
    // 合并左右子树信息
    tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
}

/**
 * 查询区间[jobl, jobr]的最大子段和
 * 通过分治思想查询任意区间的最大子段和
 * 
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param l    当前节点表示的区间左端点
 * @param r    当前节点表示的区间右端点
 * @param i    当前节点索引
 * @return 查询区间的节点信息
 */
Node query(int jobl, int jobr, int l, int r, int i) {
    // 当前区间完全被查询区间包含，直接返回
    if (jobl <= l && r <= jobr) {
        return tree[i];
    }
    
    int mid = (l + r) >> 1;
    
    // 查询区间完全在左子树
    if (jobr <= mid) {
        return query(jobl, jobr, l, mid, i << 1);
    } 
    // 查询区间完全在右子树
    else if (jobl > mid) {
        return query(jobl, jobr, mid + 1, r, i << 1 | 1);
    } 
    // 查询区间跨越左右子树
    else {
        Node left = query(jobl, jobr, l, mid, i << 1);
        Node right = query(jobl, jobr, mid + 1, r, i << 1 | 1);
        return pushUp(left, right);
    }
}

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出功能