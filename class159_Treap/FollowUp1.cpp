/*
 * Treap树实现普通有序表 - C++实现
 * 数据加强的测试，支持强制在线操作
 * 测试链接 : https://www.luogu.com.cn/problem/P6136
 * 
 * 功能要求：
 * 1. 插入操作：插入一个数
 * 2. 删除操作：删除一个数
 * 3. 查询排名：查询某个数的排名
 * 4. 查询第k小：查询排名为k的数
 * 5. 查询前驱：查询小于某个数的最大数
 * 6. 查询后继：查询大于某个数的最小数
 * 
 * 算法思路：
 * 1. 使用Treap（树堆）数据结构，结合二叉搜索树和堆的性质
 * 2. 通过随机优先级保持树的平衡性
 * 3. 支持所有操作的时间复杂度为O(log n)
 * 
 * 时间复杂度：O((n+m) log n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用数组模拟树结构，提高内存效率
 * - 支持强制在线操作，需要异或处理
 * - 注意内存管理和边界情况
 */

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <ctime>
#include <algorithm>
using namespace std;

const int MAXN = 2000001;
const int INF = 0x3f3f3f3f;

// Treap节点结构
struct Node {
    int key;        // 键值
    int count;      // 重复计数
    int size;       // 子树大小
    double priority; // 随机优先级
    int left;       // 左子树索引
    int right;      // 右子树索引
};

Node tree[MAXN];
int head = 0;  // 根节点索引
int cnt = 0;   // 节点计数

// 更新节点大小
void update(int i) {
    if (i == 0) return;
    tree[i].size = tree[tree[i].left].size + tree[tree[i].right].size + tree[i].count;
}

// 左旋操作
int leftRotate(int i) {
    int r = tree[i].right;
    tree[i].right = tree[r].left;
    tree[r].left = i;
    update(i);
    update(r);
    return r;
}

// 右旋操作
int rightRotate(int i) {
    int l = tree[i].left;
    tree[i].left = tree[l].right;
    tree[l].right = i;
    update(i);
    update(l);
    return l;
}

// 插入操作
int insert(int i, int num) {
    if (i == 0) {
        cnt++;
        tree[cnt].key = num;
        tree[cnt].count = tree[cnt].size = 1;
        tree[cnt].priority = (double)rand() / RAND_MAX;
        tree[cnt].left = tree[cnt].right = 0;
        return cnt;
    }
    
    if (tree[i].key == num) {
        tree[i].count++;
    } else if (tree[i].key > num) {
        tree[i].left = insert(tree[i].left, num);
    } else {
        tree[i].right = insert(tree[i].right, num);
    }
    
    update(i);
    
    // 维护堆性质
    if (tree[i].left != 0 && tree[tree[i].left].priority > tree[i].priority) {
        return rightRotate(i);
    }
    if (tree[i].right != 0 && tree[tree[i].right].priority > tree[i].priority) {
        return leftRotate(i);
    }
    
    return i;
}

// 删除操作
int remove(int i, int num) {
    if (i == 0) return 0;
    
    if (tree[i].key < num) {
        tree[i].right = remove(tree[i].right, num);
    } else if (tree[i].key > num) {
        tree[i].left = remove(tree[i].left, num);
    } else {
        if (tree[i].count > 1) {
            tree[i].count--;
        } else {
            if (tree[i].left == 0 && tree[i].right == 0) {
                return 0;
            } else if (tree[i].left != 0 && tree[i].right == 0) {
                i = tree[i].left;
            } else if (tree[i].left == 0 && tree[i].right != 0) {
                i = tree[i].right;
            } else {
                if (tree[tree[i].left].priority >= tree[tree[i].right].priority) {
                    i = rightRotate(i);
                    tree[i].right = remove(tree[i].right, num);
                } else {
                    i = leftRotate(i);
                    tree[i].left = remove(tree[i].left, num);
                }
            }
        }
    }
    
    update(i);
    return i;
}

// 查询小于num的节点数量
int querySmall(int i, int num) {
    if (i == 0) return 0;
    
    if (tree[i].key >= num) {
        return querySmall(tree[i].left, num);
    } else {
        return tree[tree[i].left].size + tree[i].count + querySmall(tree[i].right, num);
    }
}

// 查询排名
int getRank(int num) {
    return querySmall(head, num) + 1;
}

// 查询第k小的数
int getKth(int i, int k) {
    if (tree[tree[i].left].size >= k) {
        return getKth(tree[i].left, k);
    } else if (tree[tree[i].left].size + tree[i].count < k) {
        return getKth(tree[i].right, k - tree[tree[i].left].size - tree[i].count);
    }
    return tree[i].key;
}

// 查询前驱
int getPredecessor(int i, int num) {
    if (i == 0) return -INF;
    
    if (tree[i].key >= num) {
        return getPredecessor(tree[i].left, num);
    } else {
        return max(tree[i].key, getPredecessor(tree[i].right, num));
    }
}

// 查询后继
int getSuccessor(int i, int num) {
    if (i == 0) return INF;
    
    if (tree[i].key <= num) {
        return getSuccessor(tree[i].right, num);
    } else {
        return min(tree[i].key, getSuccessor(tree[i].left, num));
    }
}

// 清空树
void clear() {
    for (int i = 1; i <= cnt; i++) {
        tree[i].key = tree[i].count = tree[i].size = 0;
        tree[i].priority = 0;
        tree[i].left = tree[i].right = 0;
    }
    cnt = 0;
    head = 0;
}

int main() {
    srand(time(0));
    
    int n, m;
    scanf("%d%d", &n, &m);
    
    // 初始化插入
    for (int i = 0; i < n; i++) {
        int num;
        scanf("%d", &num);
        head = insert(head, num);
    }
    
    int lastAns = 0;
    int ans = 0;
    
    for (int i = 0; i < m; i++) {
        int op, x;
        scanf("%d%d", &op, &x);
        x ^= lastAns;  // 强制在线处理
        
        switch (op) {
            case 1:  // 插入
                head = insert(head, x);
                break;
            case 2:  // 删除
                head = remove(head, x);
                break;
            case 3:  // 查询排名
                lastAns = getRank(x);
                ans ^= lastAns;
                break;
            case 4:  // 查询第k小
                lastAns = getKth(head, x);
                ans ^= lastAns;
                break;
            case 5:  // 查询前驱
                lastAns = getPredecessor(head, x);
                ans ^= lastAns;
                break;
            case 6:  // 查询后继
                lastAns = getSuccessor(head, x);
                ans ^= lastAns;
                break;
        }
    }
    
    printf("%d\n", ans);
    clear();
    
    return 0;
}

/*
 * 算法详细解释：
 * 1. Treap结合了二叉搜索树和堆的性质，通过随机优先级保持平衡
 * 2. 插入操作：按照BST规则插入，然后通过旋转维护堆性质
 * 3. 删除操作：找到目标节点，根据子节点情况选择旋转或直接删除
 * 4. 查询操作：利用BST性质和子树大小信息进行高效查询
 * 
 * 时间复杂度分析：
 * - 所有操作的平均时间复杂度：O(log n)
 * - 最坏情况时间复杂度：O(n)，但概率极低
 * 
 * 空间复杂度分析：
 * - 每个节点需要存储键值、计数、大小、优先级和左右指针
 * - 总空间复杂度：O(n)
 * 
 * 边界情况处理：
 * - 空树处理：所有操作都要检查空树情况
 * - 重复元素：使用计数机制处理重复元素
 * - 内存管理：使用数组模拟树结构，避免动态内存分配
 */