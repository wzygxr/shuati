// SPOJ ORDERSET - Order statistic set
// 维护一个可重集合，支持以下操作：
// 1. 插入元素
// 2. 删除元素
// 3. 查询元素排名
// 4. 查询第k小值
// 测试链接 : https://www.spoj.com/problems/ORDERSET/

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <ctime>
using namespace std;

const int MAXN = 200001;

// 全局变量
int head = 0;
int cnt = 0;

// 节点的key值
int key[MAXN];

// 节点key的计数
int count_[MAXN];

// 左孩子
int left_[MAXN];

// 右孩子
int right_[MAXN];

// 数字总数
int size_[MAXN];

// 节点优先级
double priority[MAXN];

// 更新节点信息
void up(int i) {
    size_[i] = size_[left_[i]] + size_[right_[i]] + count_[i];
}

// 左旋转
int leftRotate(int i) {
    int r = right_[i];
    right_[i] = left_[r];
    left_[r] = i;
    up(i);
    up(r);
    return r;
}

// 右旋转
int rightRotate(int i) {
    int l = left_[i];
    left_[i] = right_[l];
    right_[l] = i;
    up(i);
    up(l);
    return l;
}

// 添加节点
int addNode(int i, int num) {
    if (i == 0) {
        cnt++;
        key[cnt] = num;
        count_[cnt] = size_[cnt] = 1;
        priority[cnt] = (double)rand() / RAND_MAX;
        return cnt;
    }
    if (key[i] == num) {
        count_[i]++;
    } else if (key[i] > num) {
        left_[i] = addNode(left_[i], num);
    } else {
        right_[i] = addNode(right_[i], num);
    }
    up(i);
    if (left_[i] != 0 && priority[left_[i]] > priority[i]) {
        return rightRotate(i);
    }
    if (right_[i] != 0 && priority[right_[i]] > priority[i]) {
        return leftRotate(i);
    }
    return i;
}

// 添加元素
void add(int num) {
    head = addNode(head, num);
}

// 计算小于num的元素个数
int small(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return small(left_[i], num);
    } else {
        return size_[left_[i]] + count_[i] + small(right_[i], num);
    }
}

// 查询排名
int rank(int num) {
    return small(head, num) + 1;
}

// 查询第k小值
int index_k(int i, int x) {
    if (size_[left_[i]] >= x) {
        return index_k(left_[i], x);
    } else if (size_[left_[i]] + count_[i] < x) {
        return index_k(right_[i], x - size_[left_[i]] - count_[i]);
    }
    return key[i];
}

// 查询第k小值
int index(int x) {
    if (x <= 0 || x > size_[head]) {
        return -2147483648; // Integer.MIN_VALUE
    }
    return index_k(head, x);
}

// 查找前驱
int pre(int i, int num) {
    if (i == 0) {
        return -2147483648; // Integer.MIN_VALUE
    }
    if (key[i] >= num) {
        return pre(left_[i], num);
    } else {
        int rightMax = pre(right_[i], num);
        return (rightMax > key[i]) ? rightMax : key[i];
    }
}

// 查找前驱
int preFunc(int num) {
    return pre(head, num);
}

// 查找后继
int post(int i, int num) {
    if (i == 0) {
        return 2147483647; // Integer.MAX_VALUE
    }
    if (key[i] <= num) {
        return post(right_[i], num);
    } else {
        int leftMin = post(left_[i], num);
        return (leftMin < key[i]) ? leftMin : key[i];
    }
}

// 查找后继
int postFunc(int num) {
    return post(head, num);
}

// 删除节点
int removeNode(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] < num) {
        right_[i] = removeNode(right_[i], num);
    } else if (key[i] > num) {
        left_[i] = removeNode(left_[i], num);
    } else {
        if (count_[i] > 1) {
            count_[i]--;
        } else {
            if (left_[i] == 0 && right_[i] == 0) {
                return 0;
            } else if (left_[i] != 0 && right_[i] == 0) {
                i = left_[i];
            } else if (left_[i] == 0 && right_[i] != 0) {
                i = right_[i];
            } else {
                if (priority[left_[i]] >= priority[right_[i]]) {
                    i = rightRotate(i);
                    right_[i] = removeNode(right_[i], num);
                } else {
                    i = leftRotate(i);
                    left_[i] = removeNode(left_[i], num);
                }
            }
        }
    }
    up(i);
    return i;
}

// 删除元素
void remove(int num) {
    // 检查元素是否存在
    if (rank(num) != rank(num + 1)) {
        head = removeNode(head, num);
    }
}

// 清空数据结构
void clear() {
    head = 0;
    cnt = 0;
    // 重置数组（在C++中如果要重置，可以使用memset，但这里我们主要通过head=0来重置树）
}

int main() {
    // 设置随机种子
    srand(time(0));
    
    int n;
    scanf("%d", &n);
    while (n--) {
        char op[2];
        int x;
        scanf("%s %d", op, &x);
        
        if (op[0] == 'I') {
            // 插入
            add(x);
        } else if (op[0] == 'D') {
            // 删除
            remove(x);
        } else if (op[0] == 'K') {
            // 查询第k小
            int result = index(x);
            if (result == -2147483648) {
                printf("invalid\n");
            } else {
                printf("%d\n", result);
            }
        } else if (op[0] == 'C') {
            // 查询小于x的元素个数
            printf("%d\n", small(head, x));
        }
    }
    
    return 0;
}