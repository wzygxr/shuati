/**
 * 笛卡尔树模板(C++版)
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标从1开始
 * 构建一棵二叉树，下标按照搜索二叉树组织，值按照小根堆组织
 * 建树的过程要求时间复杂度O(n)
 * 建树之后，为了验证
 * 打印，i * (left[i] + 1)，所有信息异或起来的值
 * 打印，i * (right[i] + 1)，所有信息异或起来的值
 * 
 * 约束条件：
 * 1 <= n <= 10^7
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P5854
 * 
 * 算法思路：
 * 1. 使用单调栈算法构建笛卡尔树
 * 2. 维护一个单调递增栈，栈中存储的是节点索引
 * 3. 对于每个新节点，找到它在栈中的正确位置，建立父子关系
 * 4. 时间复杂度：O(n)，每个节点入栈出栈各一次
 * 5. 空间复杂度：O(n)，用于存储栈和树结构
 * 
 * 时间复杂度分析：
 * - 构建笛卡尔树：O(n)
 * - 验证输出：O(n)
 * - 总时间复杂度：O(n)
 * 
 * 空间复杂度分析：
 * - 存储数组：O(n)
 * - 栈空间：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用数组模拟树结构，提高内存效率
 * - 注意C++的IO优化，使用快速IO
 * - 处理大整数溢出问题，使用long long类型
 * 
 * 边界情况处理：
 * - n=1时，只有一个节点，左右子树都为空
 * - 数组完全有序时，树会退化成链
 * - 注意数组下标从1开始，避免越界
 */

#include <iostream>
#include <vector>
#include <stack>
#include <cstdio>

#define LL long long

using namespace std;

const int MAXN = 10000001;

// arr数组存储输入的数值，下标从1开始
int arr[MAXN];
// left数组存储每个节点的左子节点索引，0表示没有左子节点
int ls[MAXN];
// right数组存储每个节点的右子节点索引，0表示没有右子节点
int rs[MAXN];
// stack数组用作单调栈，存储节点索引，用于构建笛卡尔树
int sta[MAXN];
// n表示输入数组的长度
int n;

/**
 * 构建笛卡尔树的核心方法
 * 使用单调栈算法，时间复杂度O(n)
 * 构建的笛卡尔树满足：
 * 1. 二叉搜索树性质：节点的下标满足二叉搜索树的性质
 * 2. 小根堆性质：节点的值满足小根堆的性质
 */
void build() {
    int top = 0;
    for (int i = 1; i <= n; i++) {
        int pos = top;
        // 维护单调栈，弹出栈顶中值大于当前元素的节点
        // 保证栈中节点的值按从小到大排列（小根堆性质）
        while (pos > 0 && arr[sta[pos]] > arr[i]) {
            pos--;
        }
        // 如果栈不为空，建立父子关系
        if (pos > 0) {
            // 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
            rs[sta[pos]] = i;
        }
        // 如果pos < top，说明弹出了节点，建立当前节点与被弹出节点的关系
        if (pos < top) {
            // 当前节点的左子节点是最后被弹出的节点
            ls[i] = sta[pos + 1];
        }
        // 将当前节点压入栈中
        sta[++pos] = i;
        // 更新栈顶指针
        top = pos;
    }
}

int main() {
    // 关闭同步，提高IO效率
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    build();
    
    long long ans1 = 0, ans2 = 0;
    for (int i = 1; i <= n; i++) {
        ans1 ^= 1LL * i * (ls[i] + 1);
        ans2 ^= 1LL * i * (rs[i] + 1);
    }
    
    cout << ans1 << " " << ans2 << endl;
    return 0;
}