#include <iostream>
#include <vector>
#include <random>
#include <chrono>

using namespace std;

/**
 * 相乘为正或负的子数组数量 - C++实现
 * 给定一个长度为n的数组arr，其中所有值都不是0
 * 返回有多少个子数组相乘的结果是正
 * 返回有多少个子数组相乘的结果是负
 * 1 <= n <= 10^6
 * -10^9 <= arr[i] <= +10^9，arr[i]一定不是0
 * 来自真实大厂笔试，对数器验证
 * 
 * 算法思路：
 * 1. 使用前缀和思想，维护当前位置之前正数和负数的子数组数量
 * 2. 遍历数组，维护一个变量cur表示到当前位置的累积符号（0表示正，1表示负）
 * 3. cnt[0]表示累积符号为正的前缀数量，cnt[1]表示累积符号为负的前缀数量
 * 4. 对于当前位置i：
 *    - 如果arr[i]为正数，符号不变，cur保持不变
 *    - 如果arr[i]为负数，符号改变，cur ^= 1
 * 5. 如果当前累积符号为cur，那么：
 *    - 与之前累积符号为cur的前缀组合，乘积为正数
 *    - 与之前累积符号为cur^1的前缀组合，乘积为负数
 * 6. 更新cnt数组
 * 
 * 时间复杂度：O(n)，只需要遍历一次数组
 * 空间复杂度：O(1)，只使用了常数额外空间
 */

class MultiplyPositiveNegative {
public:
    vector<int> num(vector<int>& arr) {
        // cnt[0]: 累积符号为正的前缀数量
        // cnt[1]: 累积符号为负的前缀数量
        vector<int> cnt(2, 0);
        // 初始化，空数组乘积为正数
        cnt[0] = 1;
        cnt[1] = 0;
        
        int ans1 = 0; // 正数子数组数量
        int ans2 = 0; // 负数子数组数量
        int cur = 0;  // 当前累积符号，0表示正，1表示负
        
        for (int i = 0; i < arr.size(); i++) {
            // 如果当前元素为负数，改变符号
            cur ^= (arr[i] > 0) ? 0 : 1;
            
            // 与之前相同符号的前缀组合，乘积为正数
            ans1 += cnt[cur];
            // 与之前不同符号的前缀组合，乘积为负数
            ans2 += cnt[cur ^ 1];
            
            // 更新cnt数组
            cnt[cur]++;
        }
        
        return {ans1, ans2};
    }
    
    /**
     * 暴力方法 - 用于验证
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    vector<int> right(vector<int>& arr) {
        int n = arr.size();
        int ans1 = 0;
        int ans2 = 0;
        
        for (int i = 0; i < n; i++) {
            long long cur = 1;
            for (int j = i; j < n; j++) {
                cur = cur * arr[j];
                if (cur > 0) {
                    ans1++;
                } else {
                    ans2++;
                }
            }
        }
        
        return {ans1, ans2};
    }
    
    /**
     * 生成随机数组用于测试
     * @param n 数组长度
     * @param v 数值范围[-v, v]，但不包含0
     * @return 随机数组
     */
    vector<int> randomArray(int n, int v) {
        vector<int> ans(n);
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(-v, v);
        
        for (int i = 0; i < n; i++) {
            int num;
            do {
                num = dis(gen);
            } while (num == 0);
            ans[i] = num;
        }
        
        return ans;
    }
    
    /**
     * 运行测试
     */
    void test() {
        int n = 20;
        int v = 10;
        int testTime = 10000;
        
        cout << "测试开始" << endl;
        auto start = chrono::high_resolution_clock::now();
        
        for (int i = 0; i < testTime; i++) {
            int size = rand() % n;
            vector<int> arr = randomArray(size, v);
            vector<int> ans1 = num(arr);
            vector<int> ans2 = right(arr);
            
            if (ans1[0] != ans2[0] || ans1[1] != ans2[1]) {
                cout << "出错了!" << endl;
                cout << "数组: ";
                for (int num : arr) cout << num << " ";
                cout << endl;
                cout << "算法结果: " << ans1[0] << ", " << ans1[1] << endl;
                cout << "暴力结果: " << ans2[0] << ", " << ans2[1] << endl;
                break;
            }
        }
        
        auto end = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "测试结束，耗时: " << duration.count() << "ms" << endl;
    }
    
    /**
     * 性能测试：大规模数据
     */
    void performanceTest() {
        int n = 1000000; // 100万数据
        int v = 1000;
        
        cout << "生成大规模测试数据..." << endl;
        vector<int> arr = randomArray(n, v);
        
        cout << "开始性能测试..." << endl;
        auto start = chrono::high_resolution_clock::now();
        
        vector<int> result = num(arr);
        
        auto end = chrono::high_resolution_clock::now();
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "性能测试结果:" << endl;
        cout << "正数子数组数量: " << result[0] << endl;
        cout << "负数子数组数量: " << result[1] << endl;
        cout << "耗时: " << duration.count() << "ms" << endl;
    }
};

/**
 * 优化版本：使用更简洁的代码实现
 */
class MultiplyPositiveNegativeOptimized {
public:
    vector<int> num(vector<int>& arr) {
        int cnt[2] = {1, 0}; // cnt[0]:正, cnt[1]:负
        int cur = 0, ans1 = 0, ans2 = 0;
        
        for (int num : arr) {
            cur ^= (num < 0); // 负数时异或1，正数时异或0
            ans1 += cnt[cur];
            ans2 += cnt[cur ^ 1];
            cnt[cur]++;
        }
        
        return {ans1, ans2};
    }
};

int main() {
    MultiplyPositiveNegative solution;
    
    // 基础测试
    cout << "=== 基础测试 ===" << endl;
    vector<int> test1 = {1, -2, 3, -4, 5};
    vector<int> result1 = solution.num(test1);
    cout << "测试数组: 1, -2, 3, -4, 5" << endl;
    cout << "正数子数组数量: " << result1[0] << endl;
    cout << "负数子数组数量: " << result1[1] << endl;
    
    // 运行测试
    cout << "\n=== 正确性测试 ===" << endl;
    solution.test();
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    solution.performanceTest();
    
    // 优化版本测试
    cout << "\n=== 优化版本测试 ===" << endl;
    MultiplyPositiveNegativeOptimized solutionOpt;
    vector<int> resultOpt = solutionOpt.num(test1);
    cout << "优化版本结果: " << resultOpt[0] << ", " << resultOpt[1] << endl;
    
    return 0;
}

/**
 * C++工程化实战建议：
 * 
 * 1. 内存管理：
 *    - 使用vector代替原生数组，避免手动内存管理
 *    - 注意vector的初始化方式，确保正确初始化
 *    - 对于大规模数据，考虑使用reserve预分配内存
 * 
 * 2. 类型安全：
 *    - 使用size_t处理数组索引，避免负数问题
 *    - 对于大数运算，使用long long避免溢出
 *    - 注意整数类型转换，避免精度丢失
 * 
 * 3. 性能优化：
 *    - 使用引用传递避免不必要的拷贝
 *    - 对于频繁调用的函数，考虑内联优化
 *    - 使用-O2或-O3编译优化
 * 
 * 4. 随机数生成：
 *    - 使用C++11的随机数库，比rand()更安全
 *    - 注意随机数种子的设置
 *    - 确保生成的随机数不包含0
 * 
 * 5. 时间测量：
 *    - 使用chrono库进行精确的时间测量
 *    - 注意时间单位的转换
 *    - 多次测量取平均值以获得更准确的结果
 * 
 * 6. 调试技巧：
 *    - 使用gdb进行调试
 *    - 添加assert断言验证中间结果
 *    - 使用valgrind检查内存泄漏
 * 
 * 7. 算法优化思路：
 *    - 原始暴力方法的时间复杂度为O(n^2)，无法处理大规模数据
 *    - 优化方法利用前缀和思想，将时间复杂度降至O(n)
 *    - 关键思路是维护累积符号的前缀数量
 *    - 这种方法避免了重复计算，大大提高了效率
 * 
 * 8. 相关题目扩展：
 *    - LeetCode 152: Maximum Product Subarray
 *    - LeetCode 53: Maximum Subarray
 *    - Codeforces 1215B: The Number of Products
 *    - 这些题目都涉及子数组乘积或和的统计，可以对比学习
 */