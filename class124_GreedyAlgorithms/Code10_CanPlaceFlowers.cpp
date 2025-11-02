// 种花问题（Can Place Flowers）
// 题目来源：LeetCode 605
// 题目链接：https://leetcode.cn/problems/can-place-flowers/

/**
 * 问题描述：
 * 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
 * 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
 * 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
 * 其中 0 表示没种植花，1 表示种植了花。
 * 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
 * 能则返回 true ，不能则返回 false 。
 * 
 * 算法思路：
 * 使用贪心策略，尽可能多地种花，只要当前位置可以种花（当前位置是0，且左右都是0或边界），就种花。
 * 具体步骤：
 * 1. 遍历花坛数组
 * 2. 对于每个位置，检查是否满足种花条件：
 *    - 当前位置是0
 *    - 前一个位置是0或当前是第一个位置
 *    - 后一个位置是0或当前是最后一个位置
 * 3. 如果满足条件，就在当前位置种花（将0改为1），并减少需要种的花数量
 * 4. 如果需要种的花数量减为0，返回true
 * 5. 遍历结束后，如果需要种的花数量已减为0，返回true，否则返回false
 * 
 * 时间复杂度：O(n)，其中n是花坛数组的长度，只需遍历数组一次
 * 空间复杂度：O(1)，只使用了常数额外空间
 * 
 * 是否最优解：是。贪心策略在此问题中能得到最优解。
 * 
 * 适用场景：
 * 1. 间隔种植问题
 * 2. 资源分配问题，需要满足相邻资源不能同时使用
 * 
 * 异常处理：
 * 1. 处理空数组情况
 * 2. 处理n为0的边界情况（不需要种花，直接返回true）
 * 
 * 工程化考量：
 * 1. 输入验证：检查数组是否为空，检查n是否为非负数
 * 2. 边界条件：处理边界位置的种植判断
 * 3. 性能优化：一旦确认可以种植n朵花，立即返回结果
 */

#include <iostream>
#include <vector>

using namespace std;

/**
 * 判断是否能在不打破种植规则的情况下种入n朵花
 * 
 * @param flowerbed 表示花坛的数组，0表示没种植花，1表示种植了花
 * @param n 需要种入的花数量
 * @return 如果能种入n朵花返回true，否则返回false
 */
bool canPlaceFlowers(vector<int>& flowerbed, int n) {
    // 边界条件检查
    if (flowerbed.empty()) {
        return n == 0; // 空花坛只能种0朵花
    }
    
    if (n <= 0) {
        return true; // 不需要种花，直接返回true
    }
    
    int length = flowerbed.size();
    
    // 遍历花坛数组
    for (int i = 0; i < length; i++) {
        // 检查当前位置是否可以种花
        if (flowerbed[i] == 0) {
            // 检查左侧是否为空或边界
            bool leftEmpty = (i == 0) || (flowerbed[i - 1] == 0);
            // 检查右侧是否为空或边界
            bool rightEmpty = (i == length - 1) || (flowerbed[i + 1] == 0);
            
            if (leftEmpty && rightEmpty) {
                // 可以种花
                flowerbed[i] = 1; // 标记为已种花
                n--; // 减少需要种的花数量
                
                // 如果已经种完所有需要的花，返回true
                if (n == 0) {
                    return true;
                }
            }
        }
    }
    
    // 遍历结束后，检查是否种完了所有需要的花
    return n == 0;
}

/**
 * 打印数组内容
 * 
 * @param arr 要打印的数组
 * @param name 数组名称
 */
void printArray(const vector<int>& arr, const string& name) {
    cout << name << ": [";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 测试函数，验证算法正确性
 */
int testCanPlaceFlowers() {
    // 测试用例1: 基本情况 - 可以种花
    vector<int> flowerbed1 = {1, 0, 0, 0, 1};
    vector<int> flowerbed1Copy = flowerbed1; // 创建副本以避免修改原始数组
    int n1 = 1;
    bool result1 = canPlaceFlowers(flowerbed1Copy, n1);
    cout << "测试用例1:" << endl;
    printArray(flowerbed1, "花坛");
    cout << "需要种花数量: " << n1 << endl;
    cout << "能否种植: " << (result1 ? "true" : "false") << endl;
    cout << "期望输出: true" << endl << endl;
    
    // 测试用例2: 基本情况 - 不能种花
    vector<int> flowerbed2 = {1, 0, 0, 0, 1};
    vector<int> flowerbed2Copy = flowerbed2;
    int n2 = 2;
    bool result2 = canPlaceFlowers(flowerbed2Copy, n2);
    cout << "测试用例2:" << endl;
    printArray(flowerbed2, "花坛");
    cout << "需要种花数量: " << n2 << endl;
    cout << "能否种植: " << (result2 ? "true" : "false") << endl;
    cout << "期望输出: false" << endl << endl;
    
    // 测试用例3: 边界情况 - n为0
    vector<int> flowerbed3 = {1, 0, 0, 0, 1};
    vector<int> flowerbed3Copy = flowerbed3;
    int n3 = 0;
    bool result3 = canPlaceFlowers(flowerbed3Copy, n3);
    cout << "测试用例3:" << endl;
    printArray(flowerbed3, "花坛");
    cout << "需要种花数量: " << n3 << endl;
    cout << "能否种植: " << (result3 ? "true" : "false") << endl;
    cout << "期望输出: true" << endl << endl;
    
    // 测试用例4: 边界情况 - 全为0的花坛
    vector<int> flowerbed4 = {0, 0, 0, 0};
    vector<int> flowerbed4Copy = flowerbed4;
    int n4 = 2;
    bool result4 = canPlaceFlowers(flowerbed4Copy, n4);
    cout << "测试用例4:" << endl;
    printArray(flowerbed4, "花坛");
    cout << "需要种花数量: " << n4 << endl;
    cout << "能否种植: " << (result4 ? "true" : "false") << endl;
    cout << "期望输出: true" << endl << endl;
    
    // 测试用例5: 边界情况 - 单元素花坛
    vector<int> flowerbed5 = {0};
    vector<int> flowerbed5Copy = flowerbed5;
    int n5 = 1;
    bool result5 = canPlaceFlowers(flowerbed5Copy, n5);
    cout << "测试用例5:" << endl;
    printArray(flowerbed5, "花坛");
    cout << "需要种花数量: " << n5 << endl;
    cout << "能否种植: " << (result5 ? "true" : "false") << endl;
    cout << "期望输出: true" << endl;
    
    return 0;
}

int main() {
    return testCanPlaceFlowers();
}