// POJ 3481 Double Queue
// 维护一个双端队列，支持以下操作：
// 1. 插入元素
// 2. 查询并删除最大值
// 3. 查询并删除最小值
// 测试链接 : http://poj.org/problem?id=3481

#include <iostream>
#include <cstdlib>
#include <cstring>
using namespace std;

const int MAXN = 100001;

// 全局变量
int head = 0;
int cnt = 0;

// 节点的key值（客户ID）
int key[MAXN];

// 节点的priority值（优先级）
int priority[MAXN];

// 左孩子
int left_[MAXN];

// 右孩子
int right_[MAXN];

// 子树大小
int size_[MAXN];

// 节点随机优先级
double randomPriority[MAXN];

// 更新节点信息
void up(int i) {
    size_[i] = size_[left_[i]] + size_[right_[i]] + 1;
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
int addNode(int i, int id, int pri) {
    if (i == 0) {
        cnt++;
        key[cnt] = id;
        priority[cnt] = pri;
        size_[cnt] = 1;
        randomPriority[cnt] = (double)rand() / RAND_MAX;
        return cnt;
    }
    if (priority[i] < pri) {
        right_[i] = addNode(right_[i], id, pri);
    } else if (priority[i] > pri) {
        left_[i] = addNode(left_[i], id, pri);
    } else {
        if (key[i] < id) {
            right_[i] = addNode(right_[i], id, pri);
        } else {
            left_[i] = addNode(left_[i], id, pri);
        }
    }
    up(i);
    if (left_[i] != 0 && randomPriority[left_[i]] > randomPriority[i]) {
        return rightRotate(i);
    }
    if (right_[i] != 0 && randomPriority[right_[i]] > randomPriority[i]) {
        return leftRotate(i);
    }
    return i;
}

// 添加元素
void add(int id, int pri) {
    head = addNode(head, id, pri);
}

// 删除指定优先级的节点
int removeNode(int i, int pri) {
    if (i == 0) return 0;
    if (priority[i] < pri) {
        right_[i] = removeNode(right_[i], pri);
    } else if (priority[i] > pri) {
        left_[i] = removeNode(left_[i], pri);
    } else {
        if (left_[i] == 0 && right_[i] == 0) {
            return 0;
        } else if (left_[i] == 0) {
            return right_[i];
        } else if (right_[i] == 0) {
            return left_[i];
        } else {
            if (randomPriority[left_[i]] > randomPriority[right_[i]]) {
                i = rightRotate(i);
                right_[i] = removeNode(right_[i], pri);
            } else {
                i = leftRotate(i);
                left_[i] = removeNode(left_[i], pri);
            }
        }
    }
    up(i);
    return i;
}

// 查找并返回最小值节点
int findMinNode(int i) {
    if (left_[i] == 0) {
        return i;
    }
    return findMinNode(left_[i]);
}

// 删除最小值并返回其ID
int removeMin() {
    if (head == 0) return -1;
    int minNode = findMinNode(head);
    int result = key[minNode];
    // 删除该节点
    head = removeNode(head, priority[minNode]);
    return result;
}

// 查找并返回最大值节点
int findMaxNode(int i) {
    if (right_[i] == 0) {
        return i;
    }
    return findMaxNode(right_[i]);
}

// 删除最大值并返回其ID
int removeMax() {
    if (head == 0) return -1;
    int maxNode = findMaxNode(head);
    int result = key[maxNode];
    // 删除该节点
    head = removeNode(head, priority[maxNode]);
    return result;
}

// 清空数据结构
void clear() {
    head = 0;
    cnt = 0;
    memset(key, 0, sizeof(key));
    memset(priority, 0, sizeof(priority));
    memset(left_, 0, sizeof(left_));
    memset(right_, 0, sizeof(right_));
    memset(size_, 0, sizeof(size_));
    memset(randomPriority, 0, sizeof(randomPriority));
}

int main() {
    // 设置随机种子
    srand(time(0));
    
    int command;
    while (cin >> command) {
        if (command == 0) {
            // 程序结束
            break;
        } else if (command == 1) {
            // 插入元素
            int id, pri;
            cin >> id >> pri;
            add(id, pri);
        } else if (command == 2) {
            // 查询并删除最大值
            int max_val = removeMax();
            if (max_val != -1) {
                cout << max_val << endl;
            } else {
                cout << 0 << endl;
            }
        } else if (command == 3) {
            // 查询并删除最小值
            int min_val = removeMin();
            if (min_val != -1) {
                cout << min_val << endl;
            } else {
                cout << 0 << endl;
            }
        }
    }
    
    clear();
    return 0;
}