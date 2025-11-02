// FHQ-Treap实现Feed the dogs
// POJ 2761 Feed the dogs
// 实现查询区间第k小元素
// 测试链接 : http://poj.org/problem?id=2761

const int MAXN = 100001;

// 全局变量
int head = 0;  // 整棵树的头节点编号
int cnt = 0;   // 空间使用计数

// 节点信息数组
int key[MAXN];      // 节点的key值（元素值）
int count[MAXN];    // 节点key的计数
int left[MAXN];     // 左孩子
int right[MAXN];    // 右孩子
int size[MAXN];     // 数字总数
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
        count[i] = 0;
        left[i] = 0;
        right[i] = 0;
        size[i] = 0;
        priority[i] = 0.0;
    }
}

// 更新节点信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + count[i];
}

// 按值分裂，将树i按照数值num分裂为两棵树
void split(int l, int r, int i, int num) {
    if (i == 0) {
        right[l] = left[r] = 0;
    } else {
        if (key[i] <= num) {
            right[l] = i;
            split(i, r, right[i], num);
        } else {
            left[r] = i;
            split(l, i, left[i], num);
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
        right[l] = merge(right[l], r);
        up(l);
        return l;
    } else {
        left[r] = merge(l, left[r]);
        up(r);
        return r;
    }
}

// 查找值为num的节点
int find(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] == num) {
        return i;
    } else if (key[i] > num) {
        return find(left[i], num);
    } else {
        return find(right[i], num);
    }
}

// 改变节点计数
void changeCount(int i, int num, int change) {
    if (key[i] == num) {
        count[i] += change;
    } else if (key[i] > num) {
        changeCount(left[i], num, change);
    } else {
        changeCount(right[i], num, change);
    }
    up(i);
}

// 插入数值
void insert(int num) {
    if (find(head, num) != 0) {
        changeCount(head, num, 1);
    } else {
        split(0, 0, head, num);
        cnt++;
        key[cnt] = num;
        count[cnt] = size[cnt] = 1;
        priority[cnt] = my_rand();
        head = merge(merge(right[0], cnt), left[0]);
    }
}

// 删除数值
void remove(int num) {
    int i = find(head, num);
    if (i != 0) {
        if (count[i] > 1) {
            changeCount(head, num, -1);
        } else {
            split(0, 0, head, num);
            int lm = right[0];
            int r = left[0];
            split(0, 0, lm, num - 1);
            int l = right[0];
            head = merge(l, r);
        }
    }
}

// 计算小于num的数的个数
int small(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return small(left[i], num);
    } else {
        return size[left[i]] + count[i] + small(right[i], num);
    }
}

// 查询数值num的排名
int rank(int num) {
    return small(head, num) + 1;
}

// 查询排名为x的数值
int index(int i, int x) {
    if (size[left[i]] >= x) {
        return index(left[i], x);
    } else if (size[left[i]] + count[i] < x) {
        return index(right[i], x - size[left[i]] - count[i]);
    }
    return key[i];
}

// 查询排名为x的数值
int indexByRank(int x) {
    return index(head, x);
}

// 简单的输入输出函数
int main() {
    init();
    
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    // 示例操作
    int n = 5; // 序列长度
    
    // 序列
    int arr[] = {0, 1, 2, 3, 4, 5}; // 0索引不使用，从1开始
    
    int m = 2; // 查询次数
    
    // 处理查询
    for (int i = 0; i < m; i++) {
        int l = (i == 0) ? 1 : 2; // 区间左端点
        int r = (i == 0) ? 3 : 4; // 区间右端点
        int k = (i == 0) ? 2 : 3; // 查询第k小
        
        // 重新初始化FHQ Treap
        init();
        
        // 将区间[l, r]的元素插入到FHQ Treap中
        for (int j = l; j <= r; j++) {
            insert(arr[j]);
        }
        
        // 查询第k小
        int result = indexByRank(k);
    }
    
    return 0;
}