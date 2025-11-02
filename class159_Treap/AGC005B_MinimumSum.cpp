/**
 * AtCoder AGC005B Minimum Sum (C++版本)
 * 
 * 题目描述：
 * 给定一个长度为n的排列，求所有连续子数组最小值之和
 * 
 * 测试链接：https://atcoder.jp/contests/agc005/tasks/agc005_b
 * 
 * 算法思路：
 * 1. 使用笛卡尔树（小根堆）来解决问题
 * 2. 每个节点对结果的贡献等于其值乘以经过该节点的子数组数量
 * 3. 经过节点的子数组数量 = (左子树大小+1) * (右子树大小+1)
 * 4. 使用单调栈构建笛卡尔树，时间复杂度O(n)
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用数组模拟树结构，提高内存效率
 * - 注意C++的IO优化，使用scanf/printf或快速IO
 * - 处理大整数溢出问题，使用long long类型
 * 
 * 边界情况：
 * - n=1时，只有一个子数组，结果就是该元素值
 * - 数组完全有序时，树会退化成链
 * - 注意数组下标从1开始，避免越界
 */

#include <iostream>
#include <vector>
#include <stack>
using namespace std;

const int MAXN = 200001;

// 全局变量
int arr[MAXN];        // 存储输入的排列
int leftChild[MAXN];  // 左子节点数组
int rightChild[MAXN]; // 右子节点数组
int stackArr[MAXN];   // 单调栈数组

/**
 * 计算子树大小
 * @param u 节点索引
 * @return 子树大小
 */
int getSize(int u) {
    if (u == 0) return 0;
    return 1 + getSize(leftChild[u]) + getSize(rightChild[u]);
}

/**
 * 深度优先搜索计算结果
 * @param u 当前节点索引
 * @return 以当前节点为根的子树中所有子数组最小值之和
 */
long long dfs(int u) {
    if (u == 0) return 0;
    
    long long leftContribution = dfs(leftChild[u]);
    long long rightContribution = dfs(rightChild[u]);
    
    int leftSize = getSize(leftChild[u]);
    int rightSize = getSize(rightChild[u]);
    
    // 当前节点的贡献 = 节点值 * 经过该节点的子数组数量
    long long currentContribution = (long long)arr[u] * (leftSize + 1) * (rightSize + 1);
    
    return leftContribution + rightContribution + currentContribution;
}

/**
 * 构建笛卡尔树并计算结果
 * @param n 数组长度
 * @return 所有连续子数组最小值之和
 */
long long buildCartesianTree(int n) {
    // 初始化左右子节点数组
    for (int i = 1; i <= n; i++) {
        leftChild[i] = 0;
        rightChild[i] = 0;
    }
    
    int top = 0; // 栈顶指针
    
    // 使用单调栈构建笛卡尔树
    for (int i = 1; i <= n; i++) {
        int pos = top;
        
        // 维护单调栈，弹出比当前元素大的节点
        while (pos > 0 && arr[stackArr[pos]] > arr[i]) {
            pos--;
        }
        
        // 建立父子关系
        if (pos > 0) {
            rightChild[stackArr[pos]] = i;
        }
        if (pos < top) {
            leftChild[i] = stackArr[pos + 1];
        }
        
        // 将当前节点压入栈中
        stackArr[++pos] = i;
        top = pos;
    }
    
    // 根节点是栈底元素stackArr[1]
    return dfs(stackArr[1]);
}

int main() {
    // 关闭同步，提高IO效率
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    long long result = buildCartesianTree(n);
    cout << result << endl;
    
    return 0;
}