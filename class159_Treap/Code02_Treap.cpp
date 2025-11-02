// Treap（树堆）模板代码
// Treap是一种自平衡二叉搜索树，结合了二叉搜索树和堆的性质
// 每个节点有一个key（用于二叉搜索树的性质）和一个priority（用于堆的性质）
// 操作时间复杂度：O(log n)

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <climits>
using namespace std;

const int MAXN = 100001;

// 全局变量
int head = 0;
int cnt = 0;

// 节点的key值
int key[MAXN + 1];

// 节点的优先级
double priority[MAXN + 1];

// 左孩子
int left_[MAXN + 1];

// 右孩子
int right_[MAXN + 1];

// 子树大小
int size_[MAXN + 1];

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
int addNode(int i, int num) {
    if (i == 0) {
        cnt++;
        key[cnt] = num;
        priority[cnt] = (double)rand() / RAND_MAX;
        size_[cnt] = 1;
        return cnt;
    }
    if (key[i] == num) {
        // 如果允许重复，可以在这里增加计数
        pass;
    } else if (key[i] > num) {
        left_[i] = addNode(left_[i], num);
    } else {
        right_[i] = addNode(right_[i], num);
    }
    up(i);
    // 维护堆性质
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
        // 找到要删除的节点
        if (left_[i] == 0 && right_[i] == 0) {
            // 叶子节点直接删除
            return 0;
        } else if (left_[i] != 0 && right_[i] == 0) {
            // 只有左子树
            return left_[i];
        } else if (left_[i] == 0 && right_[i] != 0) {
            // 只有右子树
            return right_[i];
        } else {
            // 有两个子树，根据优先级选择旋转方向
            if (priority[left_[i]] > priority[right_[i]]) {
                i = rightRotate(i);
                right_[i] = removeNode(right_[i], num);
            } else {
                i = leftRotate(i);
                left_[i] = removeNode(left_[i], num);
            }
        }
    }
    up(i);
    return i;
}

// 删除元素
void remove(int num) {
    head = removeNode(head, num);
}

// 计算小于num的元素个数
int small(int i, int num) {
    if (i == 0) {
        return 0;
    }
    if (key[i] >= num) {
        return small(left_[i], num);
    } else {
        return size_[left_[i]] + 1 + small(right_[i], num);
    }
}

// 查询排名（有多少个元素比num小 + 1）
int rank(int num) {
    return small(head, num) + 1;
}

// 查询第k小值
int index_k(int i, int x) {
    if (size_[left_[i]] >= x) {
        return index_k(left_[i], x);
    } else if (size_[left_[i]] + 1 < x) {
        return index_k(right_[i], x - size_[left_[i]] - 1);
    }
    return key[i];
}

// 查询第k小值
int index(int x) {
    if (x <= 0 || x > size_[head]) {
        return INT_MIN;  // 非法输入
    }
    return index_k(head, x);
}

// 查找前驱（比num小的最大元素）
int pre(int i, int num) {
    if (i == 0) {
        return INT_MIN;
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

// 查找后继（比num大的最小元素）
int post(int i, int num) {
    if (i == 0) {
        return INT_MAX;
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

// 中序遍历
void inorder(int i) {
    if (i == 0) {
        return;
    }
    inorder(left_[i]);
    printf("%d ", key[i]);
    inorder(right_[i]);
}

// 验证二叉搜索树性质
bool checkBST(int i, int min_val, int max_val) {
    if (i == 0) {
        return true;
    }
    if (key[i] <= min_val || key[i] >= max_val) {
        return false;
    }
    return checkBST(left_[i], min_val, key[i]) && checkBST(right_[i], key[i], max_val);
}

// 验证堆性质
bool checkHeap(int i) {
    if (i == 0) {
        return true;
    }
    if (left_[i] != 0 && priority[left_[i]] > priority[i]) {
        return false;
    }
    if (right_[i] != 0 && priority[right_[i]] > priority[i]) {
        return false;
    }
    return checkHeap(left_[i]) && checkHeap(right_[i]);
}

// 清空数据结构
void clear() {
    head = 0;
    cnt = 0;
    // 在C++中可以不重置数组，主要通过head=0来重置树
}

int main() {
    // 设置随机种子
    srand(time(0));
    
    // 测试代码
    add(5);
    add(3);
    add(7);
    add(2);
    add(4);
    add(6);
    add(8);
    
    printf("中序遍历结果:\n");
    inorder(head);
    printf("\n");
    
    printf("查询排名（元素5）: %d\n", rank(5));
    printf("查询第3小值: %d\n", index(3));
    printf("查询前驱（元素5）: %d\n", preFunc(5));
    printf("查询后继（元素5）: %d\n", postFunc(5));
    
    printf("\n删除元素5后:\n");
    remove(5);
    inorder(head);
    printf("\n");
    
    printf("验证二叉搜索树性质: %s\n", checkBST(head, INT_MIN, INT_MAX) ? "通过" : "未通过");
    printf("验证堆性质: %s\n", checkHeap(head) ? "通过" : "未通过");
    
    return 0;
}