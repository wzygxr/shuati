// FHQ-Treap实现Order Statistic Set
// SPOJ ORDERSET - Order statistic set
// 实现有序集合，支持插入、删除、查询第k小数、查询某数的排名等操作
// 题目链接: https://www.spoj.com/problems/ORDERSET/
// 题目描述: 维护一个动态集合，支持插入、删除、查询第k小数、查询某数的排名等操作
// 操作类型:
// I x : 插入元素x
// D x : 删除元素x
// K x : 查询第x小的元素
// C x : 查询元素x的排名（比x小的数的个数）

const int MAXN = 200001;

// 全局变量
int head = 0;  // 整棵树的头节点编号
int cnt = 0;   // 空间使用计数

// 节点信息数组
int key[MAXN];      // 节点的key值
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
    if (x < 1 || x > size[head]) {
        return 2147483647; // 表示不存在，返回最大值
    }
    return index(head, x);
}

// 查询数值num的前驱
int pre(int i, int num) {
    if (i == 0) {
        return -2147483648; // 返回最小值
    }
    if (key[i] >= num) {
        return pre(left[i], num);
    } else {
        int res = pre(right[i], num);
        return (res == -2147483648) ? key[i] : (key[i] > res ? key[i] : res);
    }
}

// 查询数值num的前驱
int preByValue(int num) {
    return pre(head, num);
}

// 查询数值num的后继
int post(int i, int num) {
    if (i == 0) {
        return 2147483647; // 返回最大值
    }
    if (key[i] <= num) {
        return post(right[i], num);
    } else {
        int res = post(left[i], num);
        return (res == 2147483647) ? key[i] : (key[i] < res ? key[i] : res);
    }
}

// 查询数值num的后继
int postByValue(int num) {
    return post(head, num);
}

// 简单的输入输出函数
int main() {
    init();
    
    // 注意：在实际提交时，需要使用标准输入输出
    // 这里为了简化，使用硬编码的测试数据
    
    // 示例操作
    insert(1);
    insert(2);
    insert(3);
    
    // 查询第2小的数
    int result = indexByRank(2);
    if (result == 2147483647) {
        // 输出"invalid"
    } else {
        // 输出结果
    }
    
    return 0;
}