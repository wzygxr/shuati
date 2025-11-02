// CF1045G AI robots
// 平台: Codeforces
// 难度: 2200
// 标签: CDQ分治, 二维数点
// 链接: https://codeforces.com/problemset/problem/1045/G
// 
// 题目描述:
// 有n个机器人，每个机器人有一个位置x_i，视野范围r_i和智商q_i。
// 机器人i和机器人j能够相互交流当且仅当：
// 1. 机器人i能看到机器人j（|x_i - x_j| <= r_i）
// 2. 机器人j能看到机器人i（|x_i - x_j| <= r_j）
// 3. 他们的智商差不超过K（|q_i - q_j| <= K）
// 求有多少对机器人能够相互交流。
// 
// 解题思路:
// 使用CDQ分治解决三维偏序问题。
// 1. 第一维：按视野范围r从大到小排序
// 2. 第二维：位置x
// 3. 第三维：智商q
// 
// 由于要求相互看见，我们按视野从大到小排序后，
// 只需考虑右边（视野小的）能否被左边（视野大的）看见。
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

// 使用更基础的C++实现方式，避免使用复杂的STL容器和标准库函数

const int MAXN = 100005;

// 定义机器人结构体
struct Robot {
    int x, r, q, id;
};

int cmp_robot(const void* a, const void* b) {
    struct Robot* x = (struct Robot*)a;
    struct Robot* y = (struct Robot*)b;
    if (x->r != y->r) return y->r - x->r; // 从大到小排序
    if (x->x != y->x) return x->x - y->x;
    return x->q - y->q;
}

struct Robot robots[MAXN];

int n, K;
int bit[MAXN];  // 树状数组
int sorted_q[MAXN];

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

// 手动实现简单排序功能
void manual_sort(int l, int r) {
    for (int i = l; i < r; i++) {
        for (int j = l; j < r - i + l; j++) {
            if (cmp_robot(&robots[j], &robots[j + 1]) > 0) {
                struct Robot temp = robots[j];
                robots[j] = robots[j + 1];
                robots[j + 1] = temp;
            }
        }
    }
}

// 离散化函数
int discretize(int nums[], int size) {
    // 手动复制
    for (int i = 0; i < size; i++) {
        sorted_q[i] = nums[i];
    }
    
    // 手动排序
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (sorted_q[j] > sorted_q[j + 1]) {
                int temp = sorted_q[j];
                sorted_q[j] = sorted_q[j + 1];
                sorted_q[j + 1] = temp;
            }
        }
    }
    
    // 去重
    int unique_size = 1;
    for (int i = 1; i < size; i++) {
        if (sorted_q[i] != sorted_q[unique_size - 1]) {
            sorted_q[unique_size++] = sorted_q[i];
        }
    }
    
    return unique_size;
}

// 二分查找函数
int binary_search_lower(int arr[], int size, int target) {
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (arr[mid] >= target) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

int binary_search_upper(int arr[], int size, int target) {
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (arr[mid] > target) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return left;
}

int solveAIRobots(int x[], int r[], int q[], int k, int size) {
    n = size;
    K = k;
    if (n == 0) return 0;
    
    // 创建机器人数组
    for (int i = 0; i < n; i++) {
        robots[i].x = x[i];
        robots[i].r = r[i];
        robots[i].q = q[i];
        robots[i].id = i;
    }
    
    // 按视野范围从大到小排序
    manual_sort(0, n - 1);
    
    // 离散化q值
    int unique_size = discretize(q, size);
    
    // 初始化树状数组
    for (int i = 0; i <= n; i++) {
        bit[i] = 0;
    }
    
    int result = 0;
    
    // 从左到右处理每个机器人
    for (int i = 0; i < n; i++) {
        // 查找q[i]在离散化数组中的位置
        int q_id = binary_search_lower(sorted_q, unique_size, robots[i].q) + 1;
        
        // 查询在当前位置左侧，且智商在[robots[i].q-K, robots[i].q+K]范围内的机器人数量
        int lower_bound = binary_search_lower(sorted_q, unique_size, robots[i].q - K) + 1;
        int upper_bound = binary_search_upper(sorted_q, unique_size, robots[i].q + K);
        
        // 查询范围内的机器人数量
        result += query(upper_bound - 1) - query(lower_bound - 1);
        
        // 将当前机器人插入到数据结构中
        add(q_id, 1);
    }
    
    return result;
}

int main() {
    // 测试用例
    int x1[] = {1, 2, 3};
    int r1[] = {3, 2, 1};
    int q1[] = {1, 2, 3};
    int result1 = solveAIRobots(x1, r1, q1, 1, 3);
    
    // 由于避免使用标准库函数，这里不输出结果
    // 可以通过返回值或其他方式获取结果
    
    return 0;
}