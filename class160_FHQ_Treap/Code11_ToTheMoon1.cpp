// FHQ-Treap实现To the moon
// SPOJ TTM - To the moon
// 实现可持久化数组操作
// 测试链接 : https://www.spoj.com/problems/TTM/

const int MAXN = 100001;
const int MAXV = 100001;

// 全局变量
int cnt = 0;   // 空间使用计数

// 节点信息数组
int key[MAXN];      // 节点的key值（数组元素值）
int add[MAXN];      // 节点的加法标记
int left[MAXN];     // 左孩子
int right[MAXN];    // 右孩子
int size[MAXN];     // 子树大小
double priority[MAXN];  // 节点优先级

// 版本数组，存储每个版本的根节点
int version[MAXV];

// 当前版本号
int currentVersion = 0;

// 简单的随机数生成器
int seed = 1;
double my_rand() {
    seed = seed * 1103515245 + 12345;
    return (double)(seed & 0x7fffffff) / 2147483647.0;
}

// 初始化
void init() {
    cnt = 0;
    currentVersion = 0;
    for (int i = 0; i < MAXN; i++) {
        key[i] = 0;
        add[i] = 0;
        left[i] = 0;
        right[i] = 0;
        size[i] = 0;
        priority[i] = 0.0;
    }
    for (int i = 0; i < MAXV; i++) {
        version[i] = 0;
    }
}

// 更新节点信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + 1;
}

// 下传标记
void down(int i) {
    if (add[i] != 0) {
        // 创建新节点以实现可持久化
        if (left[i] != 0) {
            cnt++;
            key[cnt] = key[left[i]];
            add[cnt] = add[left[i]];
            left[cnt] = left[left[i]];
            right[cnt] = right[left[i]];
            size[cnt] = size[left[i]];
            priority[cnt] = priority[left[i]];
            key[cnt] += add[i];
            add[cnt] += add[i];
            left[i] = cnt;
        }
        if (right[i] != 0) {
            cnt++;
            key[cnt] = key[right[i]];
            add[cnt] = add[right[i]];
            left[cnt] = left[right[i]];
            right[cnt] = right[right[i]];
            size[cnt] = size[right[i]];
            priority[cnt] = priority[right[i]];
            key[cnt] += add[i];
            add[cnt] += add[i];
            right[i] = cnt;
        }
        add[i] = 0;
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

// 构建初始数组
int build(int l, int r, int arr[]) {
    if (l > r) {
        return 0;
    }
    int mid = (l + r) / 2;
    cnt++;
    int root = cnt;
    key[root] = arr[mid];
    size[root] = 1;
    priority[root] = my_rand();
    
    if (l == r) {
        return root;
    }
    
    left[root] = build(l, mid - 1, arr);
    right[root] = build(mid + 1, r, arr);
    up(root);
    return root;
}

// 区间加法（可持久化）
int addRange(int root, int l, int r, int value) {
    splitByPosition(0, 0, root, l - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, r - l + 1);
    int middleTree = right[0];
    
    // 创建新节点以实现可持久化
    cnt++;
    key[cnt] = key[middleTree];
    add[cnt] = add[middleTree];
    left[cnt] = left[middleTree];
    right[cnt] = right[middleTree];
    size[cnt] = size[middleTree];
    priority[cnt] = priority[middleTree];
    key[cnt] += value;
    add[cnt] += value;
    
    // 重新合并
    return merge(merge(left[0], cnt), right[0]);
}

// 查询指定位置的值
int query(int root, int pos) {
    splitByPosition(0, 0, root, pos - 1);
    int leftTree = right[0];
    splitByPosition(0, 0, leftTree, 1);
    int middleTree = right[0];
    
    int result = key[middleTree];
    
    // 重新合并
    merge(merge(left[0], middleTree), right[0]);
    
    return result;
}

// 获取树中第pos个节点的key值
int getKth(int i, int pos) {
    if (i == 0) {
        return 0;
    }
    down(i);
    if (size[left[i]] + 1 == pos) {
        return key[i];
    } else if (size[left[i]] + 1 > pos) {
        return getKth(left[i], pos);
    } else {
        return getKth(right[i], pos - size[left[i]] - 1);
    }
}

// 获取第pos个元素
int getKthElement(int root, int pos) {
    return getKth(root, pos);
}

// 简单的输入输出函数
int main() {
    init();
    
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    // 示例操作
    int n = 5; // 数组长度
    int m = 5; // 操作次数
    
    // 初始数组
    int arr[] = {0, 1, 2, 3, 4, 5}; // 0索引不使用，从1开始
    
    // 构建初始版本
    version[currentVersion] = build(1, n, arr);
    
    // 处理操作
    // 查询操作
    int result1 = getKthElement(version[0], 2);
    
    // 修改操作
    currentVersion++;
    version[currentVersion] = addRange(version[0], 1, 3, 2);
    
    // 查询操作
    int result2 = getKthElement(version[1], 2);
    
    return 0;
}