// P2345 [USACO04OPEN] MooFest G
// 平台: 洛谷
// 难度: 省选/NOI-
// 标签: CDQ分治, 二维数点
// 链接: https://www.luogu.com.cn/problem/P2345
// 
// 题目描述:
// 约翰的n头奶牛每年都会参加"哞哞大会"。每头奶牛的坐标为x_i，听力为v_i。
// 第i头和第j头奶牛交流，会发出max{v_i,v_j} × |x_i − x_j|的音量。
// 假设每对奶牛之间同时都在说话，请计算所有奶牛产生的音量之和是多少。
// 
// 解题思路:
// 使用CDQ分治解决二维数点问题。
// 1. 将所有奶牛按x坐标排序
// 2. 对于每对奶牛(i,j)，其中i<j，贡献为max{v_i,v_j} × |x_j - x_i|
// 3. 由于x_j > x_i，所以贡献为max{v_i,v_j} × (x_j - x_i)
// 4. 将贡献拆分为两部分：
//    - max{v_i,v_j} × x_j
//    - -max{v_i,v_j} × x_i
// 5. 对于固定的j，我们需要计算所有i<j的max{v_i,v_j}的和
// 6. 这可以通过CDQ分治来解决，将问题转化为二维数点问题
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

// 使用更基础的C++实现方式，避免使用复杂的STL容器和标准库函数
// 为了解决编译问题，我们手动实现所需功能

const int MAXN = 50005;

// 定义奶牛结构体
struct Cow {
    int x, v, id;
};

// 定义查询结构体
// type: 0表示插入，1表示查询
struct Query {
    int type, x, v, id, idx;
};

int cmp_cow(const void* a, const void* b) {
    struct Cow* x = (struct Cow*)a;
    struct Cow* y = (struct Cow*)b;
    if (x->x != y->x) return x->x - y->x;
    return x->v - y->v;
}

int cmp_query(const void* a, const void* b) {
    struct Query* x = (struct Query*)a;
    struct Query* y = (struct Query*)b;
    if (x->x != y->x) return x->x - y->x;
    return x->type - y->type;  // 插入操作优先于查询操作
}

struct Cow cows[MAXN];
struct Query queries[2 * MAXN];
struct Query tmp[2 * MAXN];

int n;
long long result[MAXN];
long long bit[MAXN];  // 树状数组
long long sumBit[MAXN];  // 用于维护v的和的树状数组
int sorted_v[MAXN];

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void add(int x, long long v, long long sv) {
    for (int i = x; i <= n; i += lowbit(i)) {
        bit[i] += v;
        sumBit[i] += sv;
    }
}

long long query(int x) {
    long long res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

long long querySum(int x) {
    long long res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += sumBit[i];
    }
    return res;
}

// CDQ分治主函数
void cdq(int l, int r) {
    if (l >= r) return;
    
    int mid = (l + r) >> 1;
    cdq(l, mid);
    cdq(mid + 1, r);
    
    // 合并过程，计算左半部分对右半部分的贡献
    int i = l, j = mid + 1, k = l;
    while (i <= mid && j <= r) {
        if (queries[i].x <= queries[j].x) {
            // 左半部分的元素x坐标小于等于右半部分，处理插入操作
            if (queries[i].type == 0) {
                add(queries[i].id, 1, queries[i].v);  // 插入元素
            }
            tmp[k++] = queries[i++];
        } else {
            // 右半部分的元素x坐标更大，处理查询操作
            if (queries[j].type == 1) {
                // 查询v <= queries[j].v的元素个数和v的和
                long long count = query(queries[j].id);
                long long sumV = querySum(queries[j].id);
                // 贡献为：count * queries[j].x - sumV
                result[queries[j].idx] += count * queries[j].x - sumV;
            }
            tmp[k++] = queries[j++];
        }
    }
    
    // 处理剩余元素
    while (i <= mid) {
        tmp[k++] = queries[i++];
    }
    while (j <= r) {
        if (queries[j].type == 1) {
            long long count = query(queries[j].id);
            long long sumV = querySum(queries[j].id);
            result[queries[j].idx] += count * queries[j].x - sumV;
        }
        tmp[k++] = queries[j++];
    }
    
    // 清理树状数组
    for (int t = l; t <= mid; t++) {
        if (queries[t].type == 0) {
            add(queries[t].id, -1, -queries[t].v);
        }
    }
    
    // 将临时数组内容复制回原数组
    for (int t = l; t <= r; t++) {
        queries[t] = tmp[t];
    }
}

// 离散化函数
int discretize(int nums[], int size) {
    // 手动实现memcpy功能
    for (int i = 0; i < size; i++) {
        sorted_v[i] = nums[i];
    }
    
    // 手动排序
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (sorted_v[j] > sorted_v[j + 1]) {
                int temp = sorted_v[j];
                sorted_v[j] = sorted_v[j + 1];
                sorted_v[j + 1] = temp;
            }
        }
    }
    
    // 去重
    int unique_size = 1;
    for (int i = 1; i < size; i++) {
        if (sorted_v[i] != sorted_v[unique_size - 1]) {
            sorted_v[unique_size++] = sorted_v[i];
        }
    }
    
    return unique_size;
}

// 查找离散化后的值
int find_id(int val, int size) {
    // 二分查找
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (sorted_v[mid] >= val) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return left + 1;
}

long long solveMooFest(int x[], int v[], int size) {
    n = size;
    if (n == 0) return 0;
    
    // 创建奶牛数组并按x坐标排序
    for (int i = 0; i < n; i++) {
        cows[i].x = x[i];
        cows[i].v = v[i];
        cows[i].id = i;
    }
    
    // 手动实现简单排序功能
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - 1 - i; j++) {
            if (cmp_cow(&cows[j], &cows[j + 1]) > 0) {
                struct Cow temp = cows[j];
                cows[j] = cows[j + 1];
                cows[j + 1] = temp;
            }
        }
    }
    
    // 离散化v值
    int unique_size = discretize(v, size);
    
    int cnt = 0;
    // 构造操作序列
    for (int i = 0; i < n; i++) {
        int v_id = find_id(cows[i].v, unique_size);
        
        // 插入操作
        queries[++cnt].type = 0;
        queries[cnt].x = cows[i].x;
        queries[cnt].v = cows[i].v;
        queries[cnt].id = v_id;
        queries[cnt].idx = cows[i].id;
        
        // 查询操作：查询所有v <= cows[i].v的元素个数和v的和
        queries[++cnt].type = 1;
        queries[cnt].x = cows[i].x;
        queries[cnt].v = cows[i].v;
        queries[cnt].id = v_id;
        queries[cnt].idx = cows[i].id;
    }
    
    // 按x坐标排序
    // 手动实现简单排序功能
    for (int i = 1; i < cnt; i++) {
        for (int j = 1; j < cnt - i + 1; j++) {
            if (cmp_query(&queries[j], &queries[j + 1]) > 0) {
                struct Query temp = queries[j];
                queries[j] = queries[j + 1];
                queries[j + 1] = temp;
            }
        }
    }
    
    // 初始化结果数组和树状数组
    // 手动初始化数组
    for (int i = 0; i < MAXN; i++) {
        result[i] = 0;
        if (i <= n) bit[i] = 0;
        if (i <= n) sumBit[i] = 0;
    }
    
    // 执行CDQ分治
    cdq(1, cnt);
    
    // 计算最终结果
    long long total = 0;
    for (int i = 0; i < n; i++) {
        total += result[i];
    }
    
    return total;
}

int main() {
    // 测试用例
    int x1[] = {1, 2, 3, 4};
    int v1[] = {1, 2, 3, 4};
    long long result1 = solveMooFest(x1, v1, 4);
    
    // 由于避免使用标准库函数，这里不输出结果
    // 可以通过返回值或其他方式获取结果
    
    return 0;
}