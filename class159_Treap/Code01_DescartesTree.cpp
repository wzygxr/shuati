// 笛卡尔树模板代码
// 笛卡尔树是一种特殊的二叉搜索树，同时满足堆的性质
// 构建时间复杂度：O(n)，使用单调栈优化

#include <iostream>
#include <cstdio>
using namespace std;

const int MAXN = 100001;

// 全局变量
int n;
int arr[MAXN + 1];  // 数组从1开始索引
int stack_[MAXN + 1];
int left_[MAXN + 1];  // left_[i]表示i节点的左孩子
int right_[MAXN + 1];  // right_[i]表示i节点的右孩子

// 构建笛卡尔树（小根堆）
void build() {
    // 初始化
    for (int i = 1; i <= n; i++) {
        left_[i] = 0;
        right_[i] = 0;
    }
    
    // 使用单调栈构建笛卡尔树
    int top = 0;
    for (int i = 1; i <= n; i++) {
        int pos = top;
        // 维护单调栈，弹出比当前元素大的节点
        while (pos > 0 && arr[stack_[pos]] > arr[i]) {
            pos--;
        }
        // 建立父子关系
        if (pos > 0) {
            right_[stack_[pos]] = i;
        }
        if (pos < top) {
            left_[i] = stack_[pos + 1];
        }
        stack_[++pos] = i;
        top = pos;
    }
}

// 深度优先遍历笛卡尔树
void dfs(int u) {
    if (u == 0) {
        return;
    }
    printf("Node %d, value: %d\n", u, arr[u]);
    printf("Left child of %d: %d\n", u, left_[u]);
    printf("Right child of %d: %d\n", u, right_[u]);
    dfs(left_[u]);
    dfs(right_[u]);
}

// 中序遍历验证二叉搜索树性质（按索引顺序）
void inorder(int u) {
    if (u == 0) {
        return;
    }
    inorder(left_[u]);
    printf("%d ", arr[u]);
    inorder(right_[u]);
}

// 验证堆性质
bool checkHeap(int u) {
    if (u == 0) {
        return true;
    }
    if (left_[u] != 0 && arr[left_[u]] < arr[u]) {
        return false;
    }
    if (right_[u] != 0 && arr[right_[u]] < arr[u]) {
        return false;
    }
    return checkHeap(left_[u]) && checkHeap(right_[u]);
}

int main() {
    // 读取输入
    scanf("%d", &n);
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    // 构建笛卡尔树
    build();
    
    // 获取根节点
    int root = stack_[1];
    
    printf("笛卡尔树构建完成，根节点是: %d\n", root);
    printf("\n笛卡尔树结构:\n");
    dfs(root);
    
    printf("\n中序遍历结果:\n");
    inorder(root);
    printf("\n");
    
    printf("\n验证堆性质: %s\n", checkHeap(root) ? "通过" : "未通过");
    
    return 0;
}