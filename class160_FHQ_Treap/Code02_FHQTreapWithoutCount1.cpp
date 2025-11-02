// FHQ-Treap，不用词频压缩，FHQ-Treap最常规的实现，C++版
// 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
// 1，增加x，重复加入算多个词频
// 2，删除x，如果有多个，只删掉一个
// 3，查询x的排名，x的排名为，比x小的数的个数+1
// 4，查询数据中排名为x的数
// 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
// 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
// 所有操作的次数 <= 10^5
// -10^7 <= x <= +10^7
// 测试链接 : https://www.luogu.com.cn/problem/P3369
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <algorithm>
#include <climits>
#include <ctime>
using namespace std;

const int MAXN = 100001;

// 整棵树的头节点编号
int head = 0;

// 空间使用计数
int cnt = 0;

// 节点的key值
int key[MAXN];

// 左孩子
int left[MAXN];

// 右孩子
int right[MAXN];

// 节点总数
int size[MAXN];

// 节点优先级
double priority[MAXN];

// 更新节点大小信息
void up(int i) {
    size[i] = size[left[i]] + size[right[i]] + 1;
}

// 按值分割树
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

// 合并两棵树
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

// 添加元素
void add(int num) {
    if (find(head, num) != 0) {
        // 如果元素已存在，不重复添加（不用词频压缩）
        return;
    } else {
        // 临时变量用于存储分割结果
        int temp_l = 0, temp_r = 0;
        split(temp_l, temp_r, head, num);
        
        cnt++;
        key[cnt] = num;
        size[cnt] = 1;
        priority[cnt] = (double)rand() / RAND_MAX;
        
        // 合并树
        int left_part = merge(temp_l, cnt);
        head = merge(left_part, temp_r);
    }
}

// 删除元素
void remove(int num) {
    int i = find(head, num);
    if (i != 0) {
        // 临时变量用于存储分割结果
        int temp_l1 = 0, temp_r1 = 0;
        split(temp_l1, temp_r1, head, num);
        
        int temp_l2 = 0, temp_r2 = 0;
        split(temp_l2, temp_r2, temp_l1, num - 1);
        
        head = merge(temp_l2, temp_r1);
    }
}

// 计算小于num的元素个数
int small(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return small(left[i], num);
    } else {
        return size[left[i]] + 1 + small(right[i], num);
    }
}

// 获取排名
int getRank(int num) {
    return small(head, num) + 1;
}

// 获取排名为x的元素
int index(int i, int x) {
    if (size[left[i]] >= x) {
        return index(left[i], x);
    } else if (size[left[i]] + 1 < x) {
        return index(right[i], x - size[left[i]] - 1);
    }
    return key[i];
}

int getIndex(int x) {
    return index(head, x);
}

// 获取前驱
int pre(int i, int num) {
    if (i == 0) {
        return INT_MIN;
    }
    if (key[i] >= num) {
        return pre(left[i], num);
    } else {
        return max(key[i], pre(right[i], num));
    }
}

int getPre(int num) {
    return pre(head, num);
}

// 获取后继
int post(int i, int num) {
    if (i == 0) {
        return INT_MAX;
    }
    if (key[i] <= num) {
        return post(right[i], num);
    } else {
        return min(key[i], post(left[i], num));
    }
}

int getPost(int num) {
    return post(head, num);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 设置随机种子
    srand(time(0));
    
    int n;
    cin >> n;
    
    for (int i = 1, op, x; i <= n; i++) {
        cin >> op >> x;
        if (op == 1) {
            add(x);
        } else if (op == 2) {
            remove(x);
        } else if (op == 3) {
            cout << getRank(x) << endl;
        } else if (op == 4) {
            cout << getIndex(x) << endl;
        } else if (op == 5) {
            cout << getPre(x) << endl;
        } else {
            cout << getPost(x) << endl;
        }
    }
    
    return 0;
}