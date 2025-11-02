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
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

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
int ls[MAXN];

// 右孩子
int rs[MAXN];

// 节点总数
int siz[MAXN];

// 节点优先级
double priority[MAXN];

// 更新节点大小信息
void up(int i) {
    siz[i] = siz[ls[i]] + siz[rs[i]] + 1;
}

// 按值分割树
void split(int l, int r, int i, int num) {
    if (i == 0) {
        rs[l] = ls[r] = 0;
    } else {
        if (key[i] <= num) {
            rs[l] = i;
            split(i, r, rs[i], num);
        } else {
            ls[r] = i;
            split(l, i, ls[i], num);
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
        rs[l] = merge(rs[l], r);
        up(l);
        return l;
    } else {
        ls[r] = merge(l, ls[r]);
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
        return find(ls[i], num);
    } else {
        return find(rs[i], num);
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
        siz[cnt] = 1;
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
        return small(ls[i], num);
    } else {
        return siz[ls[i]] + 1 + small(rs[i], num);
    }
}

// 获取排名
int getRank(int num) {
    return small(head, num) + 1;
}

// 获取排名为x的元素
int index(int i, int x) {
    if (siz[ls[i]] >= x) {
        return index(ls[i], x);
    } else if (siz[ls[i]] + 1 < x) {
        return index(rs[i], x - siz[ls[i]] - 1);
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
        return pre(ls[i], num);
    } else {
        return max(key[i], pre(rs[i], num));
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
        return post(rs[i], num);
    } else {
        return min(key[i], post(ls[i], num));
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