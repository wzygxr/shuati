#include <iostream>
#include <vector>
#include <algorithm>

// 最大的冰淇淋数量
// 夏日炎炎，小男孩 Tony 想买一些冰淇淋消消暑。
// 商店中新到 n 支冰淇淋，用长度为 n 的数组 costs 表示每支冰淇淋的价格，其中 costs[i] 表示第 i 支冰淇淋的价格。
// Tony 一共有 coins 元，他想尽可能多买几支冰淇淋。
// 给你价格数组 costs 和总金额 coins，返回 Tony 可以买到的冰淇淋的最大数量 。
// 注意：Tony 可以按任意顺序购买冰淇淋。
// 测试链接 : https://leetcode.cn/problems/maximum-ice-cream-bars/
using namespace std;

class Solution {
public:
    /**
     * 最大冰淇淋数量问题
     * 
     * 算法思路：
     * 使用贪心策略：
     * 1. 首先将冰淇淋的价格按升序排序
     * 2. 从价格最低的冰淇淋开始购买，直到用完所有的钱
     * 3. 记录购买的冰淇淋数量
     * 
     * 正确性分析：
     * 1. 为了最大化购买的冰淇淋数量，应该优先购买价格最低的冰淇淋
     * 2. 这种贪心策略可以得到最优解，因为如果存在一个更优的解，其中跳过了某个低价冰淇淋而选择了高价冰淇淋，
     *    那么我们可以将高价冰淇淋换成低价冰淇淋，这样可以得到更多的冰淇淋数量
     * 
     * 时间复杂度：O(n*logn) - 主要是排序的时间复杂度
     * 空间复杂度：O(1) - 除了输入数组外，只使用了常数级别的额外空间
     * 
     * @param costs 每支冰淇淋的价格数组
     * @param coins 总金额
     * @return 可以买到的冰淇淋的最大数量
     */
    int maxIceCream(vector<int>& costs, int coins) {
        // 边界检查
        if (costs.empty()) {
            return 0;
        }
        if (coins <= 0) {
            return 0;
        }
        
        // 将价格排序（从小到大）
        sort(costs.begin(), costs.end());
        
        int count = 0;  // 购买的冰淇淋数量
        int remainingCoins = coins;  // 剩余的钱
        
        // 逐个购买价格最低的冰淇淋
        for (int cost : costs) {
            if (remainingCoins >= cost) {
                remainingCoins -= cost;
                count++;
            } else {
                // 钱不够买当前冰淇淋了，直接返回已购买的数量
                break;
            }
        }
        
        return count;
    }
};

// 打印数组辅助函数
void printArray(const vector<int>& arr) {
    for (int num : arr) {
        cout << num << " ";
    }
    cout << endl;
}

// 测试函数
void test() {
    Solution solution;
    
    // 测试用例1: costs = [1,3,2,4,1], coins = 7 -> 输出: 4
    // 排序后: [1,1,2,3,4]
    // 购买顺序: 1 -> 1 -> 2 -> 3，总花费7元
    vector<int> costs1 = {1, 3, 2, 4, 1};
    int coins1 = 7;
    cout << "测试用例1:" << endl;
    cout << "冰淇淋价格: ";
    printArray(costs1);
    cout << "总金额: " << coins1 << endl;
    cout << "最大购买数量: " << solution.maxIceCream(costs1, coins1) << endl; // 期望输出: 4
    
    // 测试用例2: costs = [10,6,8,7,7,8], coins = 5 -> 输出: 0
    // 排序后: [6,7,7,8,8,10]
    // 最便宜的冰淇淋价格为6，大于总金额5，无法购买
    vector<int> costs2 = {10, 6, 8, 7, 7, 8};
    int coins2 = 5;
    cout << "\n测试用例2:" << endl;
    cout << "冰淇淋价格: ";
    printArray(costs2);
    cout << "总金额: " << coins2 << endl;
    cout << "最大购买数量: " << solution.maxIceCream(costs2, coins2) << endl; // 期望输出: 0
    
    // 测试用例3: costs = [1,6,3,1,2,5], coins = 20 -> 输出: 6
    // 排序后: [1,1,2,3,5,6]
    // 所有冰淇淋总价: 1+1+2+3+5+6=18 <= 20，可以全部购买
    vector<int> costs3 = {1, 6, 3, 1, 2, 5};
    int coins3 = 20;
    cout << "\n测试用例3:" << endl;
    cout << "冰淇淋价格: ";
    printArray(costs3);
    cout << "总金额: " << coins3 << endl;
    cout << "最大购买数量: " << solution.maxIceCream(costs3, coins3) << endl; // 期望输出: 6
}

int main() {
    test();
    return 0;
}