/**
 * C++ 线段树实现 - SPOJ GSS1. Can you answer these queries I
 * 题目链接: https://www.spoj.com/problems/GSS1/
 * 题目描述:
 * 给定一个长度为N的整数序列A1, A2, ..., AN。你需要处理M个查询。
 * 对于每个查询，给定两个整数i和j，你需要找到序列中从Ai到Aj的最大子段和。
 * 最大子段和定义为：max{Ak + Ak+1 + ... + Al | i <= k <= l <= j}
 *
 * 输入:
 * 第一行包含一个整数N (1 <= N <= 50000)，表示序列的长度。
 * 第二行包含N个整数，表示序列A1, A2, ..., AN (-15007 <= Ai <= 15007)。
 * 第三行包含一个整数M (1 <= M <= 10000)，表示查询的数量。
 * 接下来M行，每行包含两个整数i和j (1 <= i <= j <= N)，表示一个查询。
 *
 * 输出:
 * 对于每个查询，输出一行包含一个整数，表示从Ai到Aj的最大子段和。
 *
 * 示例:
 * 输入:
 * 5
 * -1 2 -3 4 -5
 * 3
 * 1 3
 * 2 5
 * 1 5
 *
 * 输出:
 * 2
 * 4
 * 4
 *
 * 解题思路:
 * 这是一个经典的线段树问题，需要维护区间最大子段和。
 * 对于每个线段树节点，我们需要维护以下信息：
 * 1. 区间和(sum)
 * 2. 区间最大子段和(maxSum)
 * 3. 区间以左端点开始的最大子段和(prefixMax)
 * 4. 区间以右端点结束的最大子段和(suffixMax)
 *
 * 合并两个子区间[l, mid]和[mid+1, r]的信息时：
 * 1. 区间和 = 左区间和 + 右区间和
 * 2. 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
 * 3. 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
 * 4. 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)
 *
 * 时间复杂度: 
 * - 建树: O(n)
 * - 查询: O(log n)
 * 空间复杂度: O(n)
 */

// 定义最大数组大小
#define MAXN 50005

// 线段树节点结构
struct Node {
    int l, r;          // 区间左右端点
    int sum;           // 区间和
    int maxSum;        // 区间最大子段和
    int prefixMax;     // 区间以左端点开始的最大子段和
    int suffixMax;     // 区间以右端点结束的最大子段和
};

// 线段树数组
Node tree[MAXN * 4];

// 原始数组
int arr[MAXN];

// 数组长度
int n;

// 向上传递
void pushUp(int i) {
    Node left = tree[i << 1];
    Node right = tree[i << 1 | 1];
    Node* current = &tree[i];
    
    // 区间和 = 左区间和 + 右区间和
    current->sum = left.sum + right.sum;
    
    // 区间最大子段和 = max(左区间最大子段和, 右区间最大子段和, 左区间后缀最大值 + 右区间前缀最大值)
    int crossSum = left.suffixMax + right.prefixMax;
    int max1 = (left.maxSum > right.maxSum) ? left.maxSum : right.maxSum;
    current->maxSum = (max1 > crossSum) ? max1 : crossSum;
    
    // 区间前缀最大值 = max(左区间前缀最大值, 左区间和 + 右区间前缀最大值)
    int prefixSum = left.sum + right.prefixMax;
    current->prefixMax = (left.prefixMax > prefixSum) ? left.prefixMax : prefixSum;
    
    // 区间后缀最大值 = max(右区间后缀最大值, 右区间和 + 左区间后缀最大值)
    int suffixSum = right.sum + left.suffixMax;
    current->suffixMax = (right.suffixMax > suffixSum) ? right.suffixMax : suffixSum;
}

// 建立线段树
void build(int l, int r, int i) {
    tree[i].l = l;
    tree[i].r = r;
    if (l == r) {
        tree[i].sum = arr[l];
        tree[i].maxSum = arr[l];
        tree[i].prefixMax = arr[l];
        tree[i].suffixMax = arr[l];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    pushUp(i);
}

// 区间查询最大子段和
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return tree[i].maxSum;
    }
    int mid = (l + r) >> 1;
    int ans = -2147483647; // MIN_INT
    
    if (jobl <= mid && jobr > mid) {
        // 查询区间跨越左右子树
        Node left = tree[i << 1];
        Node right = tree[i << 1 | 1];
        
        // 计算跨越中间点的最大子段和
        int crossSum = left.suffixMax + right.prefixMax;
        ans = (ans > crossSum) ? ans : crossSum;
        
        // 递归查询左右子树
        if (jobl <= mid) {
            int temp = query(jobl, jobr, l, mid, i << 1);
            ans = (ans > temp) ? ans : temp;
        }
        if (jobr > mid) {
            int temp = query(jobl, jobr, mid + 1, r, i << 1 | 1);
            ans = (ans > temp) ? ans : temp;
        }
    } else if (jobr <= mid) {
        // 查询区间完全在左子树
        ans = query(jobl, jobr, l, mid, i << 1);
    } else {
        // 查询区间完全在右子树
        ans = query(jobl, jobr, mid + 1, r, i << 1 | 1);
    }
    
    return ans;
}

// 初始化函数
void init(int num) {
    n = num;
}

// 主函数（演示用）
void SPOJGSS1_demo() {
    // 初始化
    init(5);
    
    // 设置数组值
    arr[1] = -1;
    arr[2] = 2;
    arr[3] = -3;
    arr[4] = 4;
    arr[5] = -5;
    
    // 建立线段树
    build(1, 5, 1);
    
    // 演示查询
    // 查询 1 3
    int result1 = query(1, 3, 1, 5, 1);
    // 期望输出: 2
    
    // 查询 2 5
    int result2 = query(2, 5, 1, 5, 1);
    // 期望输出: 4
    
    // 查询 1 5
    int result3 = query(1, 5, 1, 5, 1);
    // 期望输出: 4
}