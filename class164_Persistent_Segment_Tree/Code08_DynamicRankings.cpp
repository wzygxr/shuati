/**
 * Luogu P2617 Dynamic Rankings
 * 
 * 题目描述:
 * 给定一个含有n个数的序列a1,a2…an，需要支持两种操作：
 * Q l r k 表示查询下标在区间[l,r]中的第k小的数；
 * C x y 表示将ax改为y。
 * 
 * 解题思路:
 * 使用树状数组套主席树解决动态区间第K小问题。
 * 1. 对所有可能出现的数值进行离散化处理
 * 2. 使用树状数组维护主席树，支持单点修改和区间查询
 * 3. 对于修改操作，先删除原值再插入新值
 * 4. 对于查询操作，利用树状数组前缀和思想，通过多个主席树的差得到区间信息
 * 5. 在线段树上二分查找第K小的数
 * 
 * 时间复杂度: O(m log^2 n)
 * 空间复杂度: O(n log^2 n)
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 100010;

// 原始数组
int arr[MAXN];
// 离散化后的数组
int sorted[MAXN * 2];
// 树状数组套主席树
int root[MAXN];

// 线段树节点信息
int left[MAXN * 100];
int right[MAXN * 100];
int sum[MAXN * 100];

// 线段树节点计数器
int cnt = 0;

// 修改操作记录数组
int uL[MAXN], uR[MAXN];
int uL_size = 0, uR_size = 0;

/**
 * lowbit操作
 * @param x 数值
 * @return 最低位的1
 */
int lowbit(int x) {
    return x & (-x);
}

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 在线段树中插入一个值
 * @param pos 要插入的值（离散化后的坐标）
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @param val 插入的值（1表示插入，-1表示删除）
 * @return 新节点编号
 */
int insert(int pos, int l, int r, int pre, int val) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + val;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt], val);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt], val);
        }
    }
    return rt;
}

/**
 * 在树状数组位置x处插入值
 * @param x 树状数组位置
 * @param pos 值的位置（离散化后）
 * @param val 插入的值（1表示插入，-1表示删除）
 * @param limit 值域上限
 */
void update(int x, int pos, int val, int limit) {
    for (int i = x; i <= limit; i += lowbit(i)) {
        root[i] = insert(pos, 1, cnt, root[i], val);
    }
}

/**
 * 查询区间和
 * @param x 树状数组位置
 * @return 前缀和
 */
int querySum(int x) {
    int ans = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        ans += sum[root[i]];
    }
    return ans;
}

/**
 * 查询区间第k小的数
 * @param k 第k小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param limit 值域上限
 * @return 第k小的数在离散化数组中的位置
 */
int query(int k, int l, int r, int limit) {
    // 收集查询需要的主席树根节点
    uL_size = 0;
    uR_size = 0;
    for (int i = l - 1; i > 0; i -= lowbit(i)) {
        uL[uL_size++] = root[i];
    }
    for (int i = r; i > 0; i -= lowbit(i)) {
        uR[uR_size++] = root[i];
    }
    
    int L = 1, R = limit;
    while (L < R) {
        int mid = (L + R) / 2;
        int tmp = 0;
        for (int i = 0; i < uR_size; i++) {
            tmp += sum[left[uR[i]]];
        }
        for (int i = 0; i < uL_size; i++) {
            tmp -= sum[left[uL[i]]];
        }
        
        if (tmp >= k) {
            for (int i = 0; i < uR_size; i++) {
                uR[i] = left[uR[i]];
            }
            for (int i = 0; i < uL_size; i++) {
                uL[i] = left[uL[i]];
            }
            R = mid;
        } else {
            for (int i = 0; i < uR_size; i++) {
                uR[i] = right[uR[i]];
            }
            for (int i = 0; i < uL_size; i++) {
                uL[i] = right[uL[i]];
            }
            L = mid + 1;
            k -= tmp;
        }
    }
    return L;
}

/**
 * 离散化查找数值对应的排名
 * @param val 要查找的值
 * @param n 数组长度
 * @return 值的排名
 */
int getId(int val, int n) {
    // 简单线性查找实现
    for (int i = 1; i <= n; i++) {
        if (sorted[i] == val) {
            return i;
        }
        if (sorted[i] > val) {
            return i;
        }
    }
    return n + 1;
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 5;
    int m = 3;
    
    // 原始数组
    arr[1] = 10; arr[2] = 20; arr[3] = 30; arr[4] = 40; arr[5] = 50;
    
    // 离散化后的数组
    sorted[1] = 10; sorted[2] = 20; sorted[3] = 30; sorted[4] = 40; sorted[5] = 50;
    int size = 5;
    
    // 构建空主席树
    for (int i = 1; i <= n; i++) {
        root[i] = build(1, size);
    }
    
    // 初始化树状数组
    for (int i = 1; i <= n; i++) {
        int pos = getId(arr[i], size);
        update(i, pos, 1, n);
    }
    
    // 示例操作
    // 查询区间[2,4]第2小的数
    int pos1 = query(2, 2, 4, size);
    // 修改操作：将arr[3]改为25
    int pos2_old = getId(arr[3], size);
    update(3, pos2_old, -1, n);
    arr[3] = 25;
    // 重新离散化
    sorted[6] = 25;
    // 简单排序
    for (int i = 1; i <= 6 - 1; i++) {
        for (int j = 1; j <= 6 - i; j++) {
            if (sorted[j] > sorted[j + 1]) {
                int temp = sorted[j];
                sorted[j] = sorted[j + 1];
                sorted[j + 1] = temp;
            }
        }
    }
    size = 6;
    int pos2_new = getId(arr[3], size);
    update(3, pos2_new, 1, n);
    // 查询区间[1,5]第3小的数
    int pos3 = query(3, 1, 5, size);
    
    // 输出结果需要根据具体环境实现
    return 0;
}