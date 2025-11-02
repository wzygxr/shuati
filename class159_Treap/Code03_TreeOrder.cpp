/*
 * Code03_TreeOrder.cpp - 树的序问题(C++版)
 * 
 * 题目来源：洛谷 P1377
 * 题目链接：https://www.luogu.com.cn/problem/P1377
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，表示依次插入数字，会形成一棵搜索二叉树
 * 也许同样的一个序列，依次插入数字后，也能形成同样形态的搜索二叉树
 * 请返回字典序尽量小的结果
 * 
 * 算法思路：
 * 1. 使用笛卡尔树（小根堆性质）构建搜索二叉树
 * 2. 通过单调栈算法在O(n)时间内构建笛卡尔树
 * 3. 使用迭代方式实现先序遍历，避免递归爆栈
 * 4. 输出字典序最小的结果
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用数组模拟栈，避免STL栈的开销
 * - 迭代遍历防止递归深度过大
 * - 输入输出优化提高效率
 * - 边界条件处理确保鲁棒性
 */

#include <iostream>
#include <cstdio>
#include <cstring>
#include <vector>
#include <stack>

using namespace std;

// 最大节点数，根据题目要求设置为10^5
const int MAXN = 100001;

// 全局变量定义
int arr[MAXN];      // 存储输入的数值，下标从1开始
int left_[MAXN];    // left_[i]表示节点i的左子节点索引，0表示没有左子节点
int right_[MAXN];   // right_[i]表示节点i的右子节点索引，0表示没有右子节点
int stack_[MAXN];   // 栈数组，用于构建笛卡尔树
int n;              // 输入数组的长度

/**
 * 构建笛卡尔树的核心方法
 * 使用单调栈算法，时间复杂度O(n)
 * 构建的笛卡尔树满足：
 * 1. 二叉搜索树性质：节点的下标满足二叉搜索树的性质
 * 2. 小根堆性质：节点的值满足小根堆的性质
 */
void build() {
    int top = 0;  // 栈顶指针
    
    for (int i = 1; i <= n; i++) {
        int pos = top;
        
        // 维护单调栈：找到第一个小于等于当前值的节点
        while (pos > 0 && arr[stack_[pos]] > arr[i]) {
            pos--;
        }
        
        // 连接右子树
        if (pos > 0) {
            right_[stack_[pos]] = i;
        }
        
        // 连接左子树
        if (pos < top) {
            left_[i] = stack_[pos + 1];
        }
        
        // 更新栈
        stack_[++pos] = i;
        top = pos;
    }
}

/**
 * 迭代方式实现先序遍历
 * 防止递归爆栈，适用于大规模数据
 * 使用显式栈模拟递归过程
 */
void preorder() {
    int size = 1;  // 栈大小
    int i = 0;     // 输出索引
    int cur;       // 当前节点
    
    // 初始化栈，放入根节点
    stack_[size] = stack_[1];  // 根节点在栈底
    
    while (size > 0) {
        cur = stack_[size--];  // 弹出栈顶元素
        arr[++i] = cur;        // 记录遍历顺序
        
        // 先右后左入栈（因为栈是后进先出，所以先序遍历需要先右后左）
        if (right_[cur] != 0) {
            stack_[++size] = right_[cur];
        }
        if (left_[cur] != 0) {
            stack_[++size] = left_[cur];
        }
    }
}

int main() {
    // 输入优化：使用scanf提高输入效率
    scanf("%d", &n);
    
    // 读取输入数组
    int val;
    for (int i = 1; i <= n; i++) {
        scanf("%d", &val);
        arr[val] = i;  // 关键：arr[值] = 位置
    }
    
    // 初始化左右子树数组
    memset(left_, 0, sizeof(left_));
    memset(right_, 0, sizeof(right_));
    
    // 构建笛卡尔树
    build();
    
    // 先序遍历
    preorder();
    
    // 输出结果
    for (int i = 1; i <= n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
    
    return 0;
}

/*
 * 算法复杂度分析：
 * 时间复杂度：O(n)
 *   - 构建笛卡尔树：每个元素入栈出栈一次，O(n)
 *   - 先序遍历：每个节点访问一次，O(n)
 *   - 总体：O(n)
 * 
 * 空间复杂度：O(n)
 *   - 数组存储：arr, left_, right_, stack_ 各需要O(n)空间
 *   - 总体：O(n)
 * 
 * 边界条件测试：
 * 1. n=1：单节点树
 * 2. n=2：两个节点的树
 * 3. 递增序列：形成右斜树
 * 4. 递减序列：形成左斜树
 * 5. 随机序列：验证正确性
 * 
 * 工程化改进建议：
 * 1. 添加输入验证，确保n在有效范围内
 * 2. 添加内存分配检查，防止数组越界
 * 3. 使用更安全的数组访问方式
 * 4. 添加详细的错误处理机制
 */