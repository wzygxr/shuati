// FHQ-Treap实现序列终结者
// 洛谷 P4146 序列终结者
// 实现序列操作，支持区间翻转、区间加、区间最大值查询等操作
// 测试链接 : https://www.luogu.com.cn/problem/P4146

const int MAXN = 500001;

// 全局变量
int head = 0;  // 整棵树的头节点编号
int cnt = 0;   // 空间使用计数

// 节点信息数组
int key[MAXN];      // 节点的key值（序列中的值）
int add[MAXN];      // 节点的加法标记
int max_val[MAXN];  // 节点的最大值
bool reverse[MAXN]; // 是否需要翻转标记
int left[MAXN];     // 左孩子
int right[MAXN];    // 右孩子
int size[MAXN];     // 子树大小
double priority[MAXN];  // 节点优先级

// 简单的随机数生成器
int seed = 1;
double my_rand() {
    seed = seed * 1103515245 + 12345;
    return (double)(seed & 0x7fffffff) / 2147483647.0;
}

// 初始化
void init() {
    head = 0;
    cnt = 0;
    for (int i = 0; i < MAXN; i++) {
        key[i] = 0;
        add[i] = 0;
        max_val[i] = 0;
        reverse[i] = false;
        left[i] = 0;
        right[i] = 0;
        size[i] = 0;
        priority[i] = 0.0;
    }
}

// 更新节点信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + 1;
    max_val[i] = key[i];
    if (left[i] != 0) {
        max_val[i] = (max_val[i] > max_val[left[i]]) ? max_val[i] : max_val[left[i]];
    }
    if (right[i] != 0) {
        max_val[i] = (max_val[i] > max_val[right[i]]) ? max_val[i] : max_val[right[i]];
    }
}

// 下传标记
void down(int i) {
    if (add[i] != 0) {
        if (left[i] != 0) {
            key[left[i]] += add[i];
            add[left[i]] += add[i];
            max_val[left[i]] += add[i];
        }
        if (right[i] != 0) {
            key[right[i]] += add[i];
            add[right[i]] += add[i];
            max_val[right[i]] += add[i];
        }
        add[i] = 0;
    }
    if (reverse[i]) {
        if (left[i] != 0) {
            reverse[left[i]] = !reverse[left[i]];
        }
        if (right[i] != 0) {
            reverse[right[i]] = !reverse[right[i]];
        }
        // 交换左右子树
        int temp = left[i];
        left[i] = right[i];
        right[i] = temp;
        reverse[i] = false;
    }
}

// 按位置分裂，将树i按照位置pos分裂为两棵树
void splitByPosition(int l, int r, int i, int pos) {
    if (i == 0) {
        right[l] = left[r] = 0;
    } else {
        down(i);
        if (size[left[i]] + 1 <= pos) {
            right[l] = i;
            splitByPosition(i, r, right[i], pos - size[left[i]] - 1);
        } else {
            left[r] = i;
            splitByPosition(l, i, left[i], pos);
        }
        up(i);
    }
}

// 合并操作，将两棵树l和r合并为一棵树
int merge(int l, int r) {
    if (l == 0 || r == 0) {
        return l + r;
    }
    if (priority[l] >= priority[r]) {
        down(l);
        right[l] = merge(right[l], r);
        up(l);
        return l;
    } else {
        down(r);
        left[r] = merge(l, left[r]);
        up(r);
        return r;
    }
}

// 区间加法
void addRange(int l, int r, int value) {
    splitByPosition(0, 0, head, l - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, r - l + 1);
    int middleTree = right[0];
    
    // 对中间的树进行操作
    key[middleTree] += value;
    add[middleTree] += value;
    max_val[middleTree] += value;
    
    // 重新合并
    head = merge(merge(left[0], middleTree), right[0]);
}

// 区间翻转
void reverseRange(int l, int r) {
    splitByPosition(0, 0, head, l - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, r - l + 1);
    int middleTree = right[0];
    
    // 对中间的树进行翻转操作
    reverse[middleTree] = !reverse[middleTree];
    
    // 重新合并
    head = merge(merge(left[0], middleTree), right[0]);
}

// 查询区间最大值
int queryMax(int l, int r) {
    splitByPosition(0, 0, head, l - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, r - l + 1);
    int middleTree = right[0];
    
    int result = max_val[middleTree];
    
    // 重新合并
    head = merge(merge(left[0], middleTree), right[0]);
    
    return result;
}

// 插入节点
void insert(int pos, int value) {
    splitByPosition(0, 0, head, pos);
    cnt++;
    key[cnt] = value;
    max_val[cnt] = value;
    size[cnt] = 1;
    priority[cnt] = my_rand();
    head = merge(merge(left[0], cnt), right[0]);
}

// 简单的输入输出函数
int main() {
    init();
    
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    // 示例操作
    insert(1, 1);
    insert(2, 2);
    insert(3, 3);
    
    // 区间加法
    addRange(1, 2, 5);
    
    // 查询区间最大值
    int result = queryMax(1, 3);
    
    return 0;
}