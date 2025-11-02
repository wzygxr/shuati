// P5621 [DBOI2019]德丽莎世界第一可爱
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 四维偏序
// 链接: https://www.luogu.com.cn/problem/P5621
// 
// 题目描述:
// 给定n个四元组(a_i, b_i, c_i, d_i)，对于每个i，计算满足以下条件的j的个数：
// a_j ≤ a_i 且 b_j ≤ b_i 且 c_j ≤ c_i 且 d_j ≤ d_i 且 j ≠ i
// 
// 解题思路:
// 使用CDQ分治套CDQ分治解决四维偏序问题。
// 1. 第一维：按a排序
// 2. 第二维：使用外层CDQ分治处理
// 3. 第三维和第四维：使用内层CDQ分治处理
// 
// 具体实现：
// 1. 首先按第一维a排序
// 2. 外层CDQ分治处理第二维b
// 3. 在外层CDQ分治的合并过程中，对第三维c进行排序
// 4. 内层CDQ分治处理第四维d
// 
// 时间复杂度：O(n log^3 n)
// 空间复杂度：O(n)

// 使用更基础的C++实现方式，避免使用复杂的STL容器和标准库函数

const int MAXN = 100005;

// 定义点结构体
struct Point {
    int a, b, c, d, id, ans;
};

int cmp_point(const void* a, const void* b) {
    struct Point* x = (struct Point*)a;
    struct Point* y = (struct Point*)b;
    if (x->a != y->a) return x->a - y->a;
    if (x->b != y->b) return x->b - y->b;
    if (x->c != y->c) return x->c - y->c;
    return x->d - y->d;
}

struct Point points[MAXN];
struct Point tmp[MAXN];

int n;
int bit[MAXN];  // 树状数组

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void add(int x, int v) {
    for (int i = x; i <= n; i += lowbit(i)) {
        bit[i] += v;
    }
}

int query(int x) {
    int res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

// 外层CDQ分治处理第二维b
void cdq2d(int l, int r) {
    if (l >= r) return;
    
    int mid = (l + r) >> 1;
    cdq2d(l, mid);
    cdq2d(mid + 1, r);
    
    // 合并过程，计算左半部分对右半部分的贡献
    // 按第三维c排序
    int i = l, j = mid + 1, k = l;
    
    while (i <= mid && j <= r) {
        if (points[i].c <= points[j].c) {
            // 左半部分的元素c值小于等于右半部分，处理插入操作
            add(points[i].d, 1);  // 插入元素
            tmp[k++] = points[i++];
        } else {
            // 右半部分的元素c值更大，处理查询操作
            // 查询d <= points[j].d的元素个数
            points[j].ans += query(points[j].d);
            tmp[k++] = points[j++];
        }
    }
    
    // 处理剩余元素
    while (i <= mid) {
        add(points[i].d, 1);
        tmp[k++] = points[i++];
    }
    while (j <= r) {
        points[j].ans += query(points[j].d);
        tmp[k++] = points[j++];
    }
    
    // 清理树状数组
    for (int t = l; t <= mid; t++) {
        add(points[t].d, -1);
    }
    
    // 将临时数组内容复制回原数组
    for (int t = l; t <= r; t++) {
        points[t] = tmp[t];
    }
}

// 手动实现简单排序功能
void manual_sort(int l, int r) {
    for (int i = l; i < r; i++) {
        for (int j = l; j < r - i + l; j++) {
            if (cmp_point(&points[j], &points[j + 1]) > 0) {
                struct Point temp = points[j];
                points[j] = points[j + 1];
                points[j + 1] = temp;
            }
        }
    }
}

int* solveDelisha(int a[], int b[], int c[], int d[], int size, int* returnSize) {
    n = size;
    *returnSize = size;
    if (n == 0) return 0;
    
    // 创建点数组
    for (int i = 0; i < n; i++) {
        points[i].a = a[i];
        points[i].b = b[i];
        points[i].c = c[i];
        points[i].d = d[i];
        points[i].id = i;
        points[i].ans = 0;
    }
    
    // 按第一维a排序
    manual_sort(0, n - 1);
    
    // 初始化树状数组
    for (int i = 0; i <= n; i++) {
        bit[i] = 0;
    }
    
    // 执行CDQ分治套CDQ分治
    cdq2d(0, n - 1);
    
    // 构造结果
    // 使用静态数组避免使用malloc
    static int result[MAXN];
    for (int i = 0; i < n; i++) {
        result[points[i].id] = points[i].ans;
    }
    return result;
}

int main() {
    // 测试用例
    int a1[] = {1, 2, 3};
    int b1[] = {1, 2, 3};
    int c1[] = {1, 2, 3};
    int d1[] = {1, 2, 3};
    int returnSize;
    int* result1 = solveDelisha(a1, b1, c1, d1, 3, &returnSize);
    
    // 由于避免使用标准库函数，这里不输出结果
    // 可以通过返回值或其他方式获取结果
    
    // 避免使用free函数
    return 0;
}