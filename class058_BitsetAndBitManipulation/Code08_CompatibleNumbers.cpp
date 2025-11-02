#include <iostream>
#include <vector>
#include <bitset>

// Codeforces 165E Compatible Numbers
// 题目链接: https://codeforces.com/problemset/problem/165/E
// 题目大意:
// 给定一个长度为n的数组，对于数组中的每个数，找到数组中另一个数，
// 使得这两个数的按位与结果为0。如果不存在这样的数，输出-1。

// 解题思路:
// 1. 两个数按位与结果为0，意味着它们在二进制表示中没有同为1的位
// 2. 对于每个数x，我们需要找到一个数y，使得x & y = 0
// 3. 这等价于找到一个数y，使得y是x的按位取反的子集
// 4. 我们可以使用SOS DP (Sum over Subsets DP)来预处理每个数的子集
// 5. 对于每个数x，我们查找x按位取反后是否有子集在数组中存在
// 时间复杂度: O(n + 3^k)，其中k是位数(22位)
// 空间复杂度: O(2^k)

using namespace std;

// 2^22，因为题目中数的最大值是4*10^6 < 2^22
const int MAXV = 1 << 22;

// 主函数，处理输入并输出结果
int main() {
    // 优化输入输出速度，关闭stdio同步，解除cin与cout的绑定
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    // 读取数组长度
    cin >> n;
    
    // 存储输入数组
    vector<int> a(n);
    // 使用bitset标记数组中存在哪些数
    // exists[i]为1表示数i在数组中存在
    bitset<MAXV> exists;
    // 记录每个数在数组中的位置
    // pos[i]表示数i在数组中的位置，-1表示不存在
    vector<int> pos(MAXV, -1);
    
    // 读取数组
    for (int i = 0; i < n; i++) {
        // 读取第i个元素
        cin >> a[i];
        // 标记这个数存在
        exists.set(a[i]);
        // 记录这个数在数组中的位置
        pos[a[i]] = i;
    }
    
    // 存储答案，初始化为-1表示未找到兼容数
    vector<int> answer(n, -1);
    
    // 对于每个数，找到与它兼容的数
    for (int i = 0; i < n; i++) {
        // 当前处理的数
        int x = a[i];
        // x的按位取反(22位)
        // (1 << 22) - 1 创建一个22位全为1的数
        // x ^ ((1 << 22) - 1) 对x进行按位异或，实现按位取反
        int complement = x ^ ((1 << 22) - 1);
        
        // 查找complement的子集是否有在数组中存在的
        // 使用SOS DP的思想
        // mask表示当前检查的complement的子集
        int mask = complement;
        // 循环枚举complement的所有子集
        while (mask > 0) {
            // 检查mask对应的数是否在数组中存在
            // exists[mask] 检查第mask位是否为1
            if (exists[mask]) {
                // 找到兼容数，记录其在原数组中的位置
                answer[i] = pos[mask];
                // 找到后跳出循环
                break;
            }
            // 枚举下一个子集
            // (mask - 1) & complement 计算mask的下一个子集
            mask = (mask - 1) & complement;
        }
        
        // 特殊情况：检查0是否在数组中
        // 0与任何数按位与都为0，所以如果数组中有0，它与任何数都兼容
        if (answer[i] == -1 && exists[0]) {
            answer[i] = pos[0];
        }
    }
    
    // 输出答案
    for (int i = 0; i < n; i++) {
        if (answer[i] == -1) {
            // 未找到兼容数，输出-1
            cout << "-1 ";
        } else {
            // 找到兼容数，输出该数的值
            cout << a[answer[i]] << " ";
        }
    }
    // 输出换行符
    cout << "\n";
    
    return 0;
}