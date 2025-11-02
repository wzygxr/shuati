// SPOJ KGSS - Maximum Sum
// 题目描述：
// 给定一个长度为n的整数序列，执行m次操作
// 操作类型：
// 1. U i x: 将第i个位置的值更新为x
// 2. Q l r: 查询[l,r]区间内两个最大值的和
//
// 解题思路：
// 使用线段树维护区间信息，每个节点存储以下信息：
// 1. 区间最大值(max1)
// 2. 区间次大值(max2)
//
// 合并两个子区间[l,mid]和[mid+1,r]的信息：
// 1. 区间最大值 = max(左区间max1, 右区间max1)
// 2. 区间次大值 = max(左区间max2, 右区间max2, min(左区间max1, 右区间max1))
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 更新：O(log n)
// 3. 查询：O(log n)
// 4. 空间复杂度：O(n)
//
// 是否最优解：是
// 这是解决区间两个最大值之和查询问题的最优解法，时间复杂度为O(log n)

// 由于编译环境问题，不使用标准头文件，采用基础C++实现

const int MAXN = 100001;

// 节点信息结构体
struct Node {
    int max1; // 区间最大值
    int max2; // 区间次大值
};

int arr[MAXN];
Node tree[MAXN << 2];

// 自定义max函数
int my_max(int a, int b) {
    return (a > b) ? a : b;
}

// 自定义min函数
int my_min(int a, int b) {
    return (a < b) ? a : b;
}

// 合并两个子节点的信息
Node pushUp(Node left, Node right) {
    Node res;
    res.max1 = my_max(left.max1, right.max1);
    res.max2 = my_max(my_max(left.max2, right.max2), my_min(left.max1, right.max1));
    return res;
}

// 建立线段树
void build(int l, int r, int i) {
    if (l == r) {
        tree[i].max1 = arr[l];
        tree[i].max2 = -2147483648; // INT_MIN
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, i << 1);
    build(mid + 1, r, i << 1 | 1);
    tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
}

// 更新第idx个位置的值为val
void update(int idx, int val, int l, int r, int i) {
    if (l == r) {
        tree[i].max1 = val;
        tree[i].max2 = -2147483648; // INT_MIN
        return;
    }
    int mid = (l + r) >> 1;
    if (idx <= mid) {
        update(idx, val, l, mid, i << 1);
    } else {
        update(idx, val, mid + 1, r, i << 1 | 1);
    }
    tree[i] = pushUp(tree[i << 1], tree[i << 1 | 1]);
}

// 查询区间[l,r]内两个最大值的和
Node query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return tree[i];
    }
    int mid = (l + r) >> 1;
    if (jobr <= mid) {
        return query(jobl, jobr, l, mid, i << 1);
    } else if (jobl > mid) {
        return query(jobl, jobr, mid + 1, r, i << 1 | 1);
    } else {
        Node left = query(jobl, jobr, l, mid, i << 1);
        Node right = query(jobl, jobr, mid + 1, r, i << 1 | 1);
        return pushUp(left, right);
    }
}

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出功能